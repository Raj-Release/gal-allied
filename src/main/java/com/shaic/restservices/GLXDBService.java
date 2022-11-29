package com.shaic.restservices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasDocument;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.bancs.initiateped.ComplexPedDetails;
import com.shaic.restservices.bancs.initiateped.InitiatePedDetails;

public class GLXDBService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	PreauthService preauthService;
	
	@Inject
	MasterService masterService;
	
//	private String WS_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	private String WS_DATE_FORMAT = "dd-MM-yyyy";


	public JSONArray getDocumentDetailsForIntimation(GLXDocRequest argDataObject) throws JSONException{
		JSONArray docsArr = new JSONArray();
		JSONObject docObj = null;
		List<DocumentDetails> listOfDocs = getDocumentDetailsByIntimationNo(argDataObject.getIntimationNumber());
		List<String> listOfBillAssessDocs = new ArrayList<String>(Arrays.asList("BillSummaryOtherProducts", "BillAssessmentSheetSCRC"));
		Map<String, String> listOfDocTypes = getDocumentTypes();
		if(listOfDocs != null && !listOfDocs.isEmpty())
		for(DocumentDetails rec :listOfDocs){
			docObj = new JSONObject();
			if(!StringUtils.isBlank(rec.getFileName())){
				docObj.put("documentName", rec.getFileName());
			}else{
				docObj.put("documentName", "");
			}
			docObj.put("documentId", rec.getDocumentToken());
			BPMClientContext context = new BPMClientContext();
			docObj.put("documentUrl", context.getRSWebserviceUrl());
			docObj.put("documentType", rec.getDocumentType());
			docObj.put("typeOfDoc", listOfDocTypes.get(rec.getDocumentType()));
			if(listOfBillAssessDocs.contains(rec.getDocumentType())){
				ClaimPayment paymentRec = getPaymentDetailsByRodNumber(rec.getReimbursementNumber());
				docObj.put("chequeNo", paymentRec.getChequeDDNumber());
			}else{
				docObj.put("chequeNo", "");
			}
			docsArr.put(docObj);
		}
		return docsArr;
	}

	@SuppressWarnings("unchecked")
	public List<DocumentDetails> getDocumentDetailsByIntimationNo(String intimationNo){
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoWithFlag");
		query = query.setParameter("intimationNumber", intimationNo);
		List<DocumentDetails> docdetailsList = query.getResultList();
		if(null != docdetailsList && !docdetailsList.isEmpty()){
			return docdetailsList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getDocumentTypes(){
		Map<String, String> listOfTypes = new HashMap<String, String>();
		Query query = entityManager.createNamedQuery("MasDocument.findAll");
		List<MasDocument> docTypeList = query.getResultList();
		if(null != docTypeList && !docTypeList.isEmpty()){
			for(MasDocument rec : docTypeList){
				listOfTypes.put(rec.getDocumentDesc(), rec.getDocumentType());
			}
			return listOfTypes;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ClaimPayment getPaymentDetailsByRodNumber(String rodNumber){
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(rodNumber != null){
			Query findByPaymentKey = entityManager.createNamedQuery(
					"ClaimPayment.findLatestByRodNo").setParameter("rodNumber", rodNumber);
			try{
				paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
				if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
					for(ClaimPayment claimPaymentDetail: paymentDetailsList){
						entityManager.refresh(claimPaymentDetail);
					}
					return paymentDetailsList.get(0);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getIntimationDetailsToGrievance(SRMSRequest argObj) throws Exception{
		JSONObject responseObj = new JSONObject();
		Query query = entityManager.createNamedQuery("Claim.findByIntimationNo");
		query = query.setParameter("intimationNumber", argObj.getIntimationNo());
		List<Claim> claimList = query.getResultList();
		if(null != claimList && !claimList.isEmpty()){
			for(Claim rec : claimList){
				responseObj.put("finalDiagnosis", getDiagnosisInCSV(rec.getIntimation().getKey()));
				responseObj.put("claimedAmount", rec.getClaimedAmount());
				responseObj.put("claimStatus", rec.getStatus().getProcessValue());
				responseObj.put("claimType", rec.getClaimType().getValue());
				responseObj.put("claimantName", rec.getIntimation().getInsured().getInsuredName());
				SimpleDateFormat sdf = new SimpleDateFormat(WS_DATE_FORMAT);
				if(rec.getDataOfAdmission() != null){
					String AdDate = sdf.format(rec.getDataOfAdmission());
					responseObj.put("dateOfAdmission", AdDate);
				}else{
					responseObj.put("dateOfAdmission", "");
				}
				
				if(rec.getDataOfDischarge() != null){
					String DsDate = sdf.format(rec.getDataOfDischarge());
					responseObj.put("dateOfDischarge", DsDate);
				}else{
					responseObj.put("dateOfDischarge", "");
				}
			}
		}else{
			responseObj.put("finalDiagnosis", "");
			responseObj.put("claimedAmount", "");
			responseObj.put("claimStatus", "");
			responseObj.put("claimType", "");
			responseObj.put("claimantName", "");
			responseObj.put("dateOfAdmission", "");
			responseObj.put("dateOfDischarge", "");
		}
		return responseObj;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getDiagnosisInCSV(Long argKey){
		Set<Long> dIdHolder = new HashSet<Long>();
		Query diagnosisQquery = entityManager.createNamedQuery("PedValidation.findByIntimationKey");
		diagnosisQquery.setParameter("intimationKey", argKey);
		List<PedValidation> diagnosisResult = (List<PedValidation>) diagnosisQquery.getResultList();
		StringBuilder stb = new StringBuilder();
		for(PedValidation drec : diagnosisResult){
			dIdHolder.add(drec.getDiagnosisId());
		}
		if(!dIdHolder.isEmpty()){
			Iterator value = dIdHolder.iterator();
			while (value.hasNext()) {
				Query masquery = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
				masquery.setParameter("diagnosisKey", (Long)value.next());
				List<Diagnosis> diagnosisList = (List<Diagnosis>) masquery.getResultList();
				if(diagnosisList != null && !diagnosisList.isEmpty()){
					stb.append(diagnosisList.get(0).getValue());
					stb.append(",");
				}
			}
			String temp = stb.toString();
			if(!StringUtils.isBlank(temp)){
				temp = temp.substring(0, temp.length()-1);
			}
			return temp;
		}else{
			return "";
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InitiatePedDetails getPEDDetails(Long primaryKey){
		Query pedQquery = entityManager.createNamedQuery("OldInitiatePedEndorsement.findByKey");
		pedQquery.setParameter("primaryKey", primaryKey);
		Query pedDeyailsQquery = entityManager.createNamedQuery("NewInitiatePedEndorsement.findByInitateKey");
		pedDeyailsQquery.setParameter("initiateKey", primaryKey);
		List<OldInitiatePedEndorsement>  pedResult= null;
		List<NewInitiatePedEndorsement>  pedDetailsResult= null;
		InitiatePedDetails initiatePedDetails = null;
		ComplexPedDetails complexPedDetails = null;
		List<ComplexPedDetails> complexPedDetailsList = new ArrayList<ComplexPedDetails>();
		
		try{
			pedResult = (List<OldInitiatePedEndorsement>) pedQquery.getResultList();
			pedDetailsResult = (List<NewInitiatePedEndorsement>) pedDeyailsQquery.getResultList();
			
			if(pedResult != null && !pedResult.isEmpty()){
				for(OldInitiatePedEndorsement initiateResultSet: pedResult){
					 initiatePedDetails = new InitiatePedDetails();
					 initiatePedDetails.setBusinessChannel("LABPORTAL");
					 initiatePedDetails.setServiceTransactionId(initiateResultSet.getServiceTransactionID());
					 initiatePedDetails.setUserCode("T966297");
					 initiatePedDetails.setRoleCode("SUPERUSER");
					 initiatePedDetails.setOperationCode(initiateResultSet.getPedSuggestion().getMappingCode());
					 initiatePedDetails.setPolicyNumber(initiateResultSet.getPolicy()!=null?initiateResultSet.getPolicy().getPolicyNumber():"");
					 initiatePedDetails.setMemberCode("PI19488325");
					 initiatePedDetails.setClaimNumber(initiateResultSet.getIntimation()!=null?initiateResultSet.getIntimation().getIntimationId():"");
					 initiatePedDetails.setRemarks(initiateResultSet.getRemarks());
					if(pedDetailsResult != null && !pedDetailsResult.isEmpty()){
						for(NewInitiatePedEndorsement pedDetail: pedDetailsResult){
							complexPedDetails = new ComplexPedDetails();
							 IcdBlock icdBlock = preauthService.getIcdBlock(pedDetail.getIcdBlockId());
							 IcdChapter icdChapter = preauthService.getIcdChapter(pedDetail.getIcdChapterId());
							 IcdCode icdCode = preauthService.getIcdCode(pedDetail.getIcdCodeId());
							 PreExistingDisease ped = masterService.getValueByKey(pedDetail.getPedCode());
							 
							 complexPedDetails.setPedGroupCode(ped.getCode());
							 complexPedDetails.setPedICDChapter(icdChapter.getValue());
							 complexPedDetails.setPedICDBlock(icdBlock.getValue());
							 complexPedDetails.setPedICDCode(icdCode.getValue());
							 complexPedDetails.setPedDescription(ped.getValue());
							 complexPedDetailsList.add(complexPedDetails);
							 initiatePedDetails.setPedDetails(complexPedDetailsList);
						}
					}
				}
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return initiatePedDetails;
	}

	public String getRequestFormat(InitiatePedDetails pedInitiateObject) {
		JSONObject structObj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONArray dataArr = new JSONArray();
		try{
				
			if(pedInitiateObject != null){
				structObj.put("serviceTransactionId", pedInitiateObject.getServiceTransactionId());
				structObj.put("businessChannel", pedInitiateObject.getBusinessChannel());
				structObj.put("userCode", pedInitiateObject.getUserCode());
				structObj.put("roleCode", pedInitiateObject.getRoleCode());
				structObj.put("operationCode", pedInitiateObject.getOperationCode());
				structObj.put("policyNumber", pedInitiateObject.getPolicyNumber());
				structObj.put("memberCode", pedInitiateObject.getMemberCode());
				structObj.put("claimNumber", pedInitiateObject.getClaimNumber());
				structObj.put("remarks", pedInitiateObject.getRemarks());

				List<ComplexPedDetails> complexPedDetailsList = pedInitiateObject.getPedDetails();
				JSONArray detailArr = new JSONArray();
				JSONObject detailObj = null;
				if(complexPedDetailsList != null){
					for(ComplexPedDetails pedDetail: complexPedDetailsList){
						detailObj = new JSONObject();
						detailObj.put("pedGroupCode",pedDetail.getPedGroupCode());
						detailObj.put("pedICDChapter",pedDetail.getPedICDChapter() );
						detailObj.put("pedICDBlock",pedDetail.getPedICDBlock());
						detailObj.put("pedICDCode", pedDetail.getPedICDCode());
						detailObj.put("description", pedDetail.getPedDescription());
						detailArr.put(detailObj);
					}
				}
				structObj.put("pedDetails", detailArr);
				dataArr.put(structObj);
			}else{
				
			}
			response.put("message","");
			response.put("data", dataArr);
			response.put("resCode", 200);
		}catch(Exception exp){
			System.out.println("Exception occured in perform PED Initiate "+exp.getMessage());
		}
		System.out.println(response.toString());
		return response.toString();
	}
}
