package com.shaic.restservices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GalaxyClaimsTransaction {

	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	private UserTransaction utx;
	
	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	private final String DEFAULT_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
	private static final String NAME_SEPARATOR = "_";

	private final Logger log = LoggerFactory.getLogger(GalaxyClaimsTransaction.class);




	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursement(Long argRodKey){
		Reimbursement rQuery = null;
		Query findType = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", argRodKey);
		List<Reimbursement> reimbursementList = findType.getResultList();
		if(!reimbursementList.isEmpty()){
			rQuery = reimbursementList.get(0);
		}
		return rQuery;
	}
	
	
	@SuppressWarnings("unchecked")
	public Claim getClaimRecord(String argIntimationNumber){
		Query query = null;		
		query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo", argIntimationNumber);
		List<Intimation> intimationRec = query.getResultList();
		if(!intimationRec.isEmpty()){
			query = entityManager.createNamedQuery("Claim.findByIntimationKey");
			query = query.setParameter("intimationKey", intimationRec.get(0).getKey());
			List<Claim> claimDataList = query.getResultList();
			if(!claimDataList.isEmpty()){
				return claimDataList.get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	private String generateAcknowledgeNo(Claim claim) {
		Long count = ackDocReceivedService.getCountOfAckByClaimKey(claim.getKey());
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ACK/")
				.append(claim.getIntimation().getIntimationId()).append("/").append(lackCount);
		return ackNoBuf.toString();
	}
	
	public Date convertToDate(String dateValueInString, String dateFormatToBeConverted) throws ParseException{
		SimpleDateFormat  sdf = null;
		Date date = null;
		if(!StringUtils.isBlank(dateValueInString)){
			 sdf = new SimpleDateFormat(dateFormatToBeConverted);
			 date = sdf.parse(dateValueInString);
		}
		return date;
	}
	
	@SuppressWarnings({ "unchecked" })
	public MastersValue getKeyForValue(String code, String Value) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKeyByCodeAndValue");
		query = query.setParameter("value", Value);
		query = query.setParameter("parentKey", code);
		List<MastersValue> mastersValueList = query.getResultList();
		if(!mastersValueList.isEmpty()){
			return mastersValueList.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Long getDocumentTypeId(String argDocumentTypevalue) {
		Query query = entityManager.createNamedQuery("DocumentCheckListMaster.findByValue");
		query = query.setParameter("docTypeValue", argDocumentTypevalue);
		List<DocumentCheckListMaster> documentCheckList = query.getResultList();
		if(!documentCheckList.isEmpty()){
			return documentCheckList.get(0).getKey();
		}else{
			return null;
		}
	}
	
	public boolean uploadRODDocuments(Object argObj, DocAcknowledgement ackRecord, Claim claim, List<Object> argListOfObjects){
		boolean tempStatus = false;
		List<GalaxyDocuments> listOfDocs = null;
		String claimNumber = null;
		boolean isCreateRodFlag = false;
		boolean isQueryResponseFlag = false;
		if(argObj instanceof GalaxyQueryResponseRequest){
			isQueryResponseFlag = true;
			isCreateRodFlag = false;
			listOfDocs = ((GalaxyQueryResponseRequest) argObj).getDocuments();
			claimNumber = ((GalaxyQueryResponseRequest) argObj).getClaimNumber();
		}
		if(argObj instanceof GalaxyCreateRodRequest){
			isQueryResponseFlag = false;
			isCreateRodFlag = true;
			listOfDocs = ((GalaxyCreateRodRequest) argObj).getDocuments();
			claimNumber = ((GalaxyCreateRodRequest) argObj).getClaimNumber();
		}
		if(listOfDocs != null && listOfDocs.size() > 0){
			for(GalaxyDocuments docObj : listOfDocs){
				if(!StringUtils.isBlank(docObj.getDocumentContent())){
					String fileName = (StringUtils.isBlank(docObj.getDocumentName()))?"EMPTY_FILENAME":docObj.getDocumentName();
					if(isCreateRodFlag){
						SHAFileUtils.writeStringToFileROD(fileName, docObj.getDocumentContent(), "CROD_INPUT_DOCS");
					}else if(isQueryResponseFlag){
						SHAFileUtils.writeStringToFileROD(fileName, docObj.getDocumentContent(), "QRYRES_INPUT_DOCS");
					}
					byte[] dbytes = Base64.decodeBase64(docObj.getDocumentContent());
					String intimationWithUnderScore = claimNumber;
					intimationWithUnderScore =  intimationWithUnderScore.substring(intimationWithUnderScore.lastIndexOf("/")+1, intimationWithUnderScore.length());
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
					Date date = new Date();
					String currentDate = dateFormat.format(date);//20190701_181026
					String dynamicFileName = "";
					if(isCreateRodFlag){
						dynamicFileName = "ROD"+NAME_SEPARATOR+intimationWithUnderScore+NAME_SEPARATOR+currentDate+NAME_SEPARATOR+fileName;
					}else{
						dynamicFileName = "Query"+NAME_SEPARATOR+intimationWithUnderScore+NAME_SEPARATOR+currentDate+NAME_SEPARATOR+fileName;
					}
					docObj.setDynamicFileName(dynamicFileName);
					WeakHashMap dmsStatus = SHAFileUtils.writeAndSendFileToDMS(dynamicFileName, dbytes);
					boolean isFileUploaded = (boolean) dmsStatus.get("status");
					if(isFileUploaded){
						docObj.setFileUploadInDMS(true);
						docObj.setFileDMSToken(new Long(String.valueOf(dmsStatus.get("fileKey"))));
					}else{
						docObj.setFileUploadInDMS(false);
					}
				}
			}
			for(GalaxyDocuments docObj : listOfDocs){
				if(docObj.isFileUploadInDMS()){
					tempStatus = true;
				}else{
					tempStatus = false;
					break;
				}
			}
			
			if(tempStatus){
				for(GalaxyDocuments reqDocDetails : listOfDocs){
					AcknowledgeDocument ackDocRecord =  new AcknowledgeDocument();
					ackDocRecord.setDocAcknowledgement(ackRecord);
					ackDocRecord.setClaimKey(claim.getKey());
					ackDocRecord.setFileType(getKeyForValue(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE,reqDocDetails.getDocumentType()));
					ackDocRecord.setDocToken(String.valueOf(reqDocDetails.getFileDMSToken()));
					ackDocRecord.setDeleteFlag("N");
					ackDocRecord.setCreatedBy("");
					ackDocRecord.setCreatedDate(new Date());
					ackDocRecord.setFileName(reqDocDetails.getDocumentName());
					if(isCreateRodFlag){
						SHAFileUtils.moveFilesToStatusFolderROD(true, reqDocDetails.getDynamicFileName(), "CROD_SUCCESS_DOCS", "CROD_SUCCESS_DOCS");
					}else if(isQueryResponseFlag){
						SHAFileUtils.moveFilesToStatusFolderROD(true, reqDocDetails.getDynamicFileName(), "QRYRES_SUCCESS_DOCS", "QRYRES_FAILED_DOCS");
					}
					
					
					RODDocumentCheckList rodDocListRecord = new RODDocumentCheckList();
					rodDocListRecord.setDocAcknowledgement(ackRecord);
					rodDocListRecord.setDocumentTypeId(getDocumentTypeId(reqDocDetails.getDocumentType()));
					rodDocListRecord.setReceivedStatusId(getKeyForValue(ReferenceTable.ACK_DOC_RECEIVED_STATUS,"Received Original"));
					ackDocRecord.setClaimKey(claim.getKey());
					
					argListOfObjects.add(ackDocRecord);
					argListOfObjects.add(rodDocListRecord);
				}
			}else{
				for(GalaxyDocuments reqDocDetails : listOfDocs){
					if(isCreateRodFlag){
						SHAFileUtils.moveFilesToStatusFolderROD(false, reqDocDetails.getDynamicFileName(), "CROD_SUCCESS_DOCS", "CROD_SUCCESS_DOCS");
					}else if(isQueryResponseFlag){
						SHAFileUtils.moveFilesToStatusFolderROD(false, reqDocDetails.getDynamicFileName(), "QRYRES_SUCCESS_DOCS", "QRYRES_FAILED_DOCS");
					}
				}
//				uploadStatus = "Some of the file(s) not uploaded in the DMS.";
			}
		}
		return tempStatus;
	}
	
	public void submitTaskToDB(DocAcknowledgement docAck, boolean isQueryFlag) {
		
		Claim claimObj  = docAck.getClaim();
		Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObj, hospitals);
		Object[] inputArray = (Object[]) arrayListForDBCall[0];
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;

		DBCalculationService dbCalculationService = new DBCalculationService();

		inputArray[SHAConstants.INDEX_ACK_KEY] = docAck.getKey();
		inputArray[SHAConstants.INDEX_ACK_NUMBER] = docAck
				.getAcknowledgeNumber();
		if (docAck.getHospitalisationFlag() != null) {
			inputArray[SHAConstants.INDEX_HOSPITALIZATION] = docAck
					.getHospitalisationFlag();
		}
		if (docAck.getPostHospitalisationFlag() != null) {
			inputArray[SHAConstants.INDEX_POST_HOSPITALIZATION] = docAck
					.getPostHospitalisationFlag();
		}
		if (docAck.getPartialHospitalisationFlag() != null) {
			inputArray[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = docAck
					.getPartialHospitalisationFlag();
		}
		if (docAck.getPreHospitalisationFlag() != null) {
			inputArray[SHAConstants.INDEX_PRE_HOSPITALIZATION] = docAck
					.getPreHospitalisationFlag();
		}
		if (docAck.getLumpsumAmountFlag() != null) {
			inputArray[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = docAck
					.getLumpsumAmountFlag();
		}
		if (docAck.getPatientCareFlag() != null) {
			inputArray[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = docAck
					.getPatientCareFlag();
		}
		if (docAck.getHospitalCashFlag() != null) {
			inputArray[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = docAck
					.getHospitalCashFlag();
		}
		inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = "";

		Insured insured = claimObj.getIntimation().getInsured();
		//code change done by noufel for updating cmd club memeber update
		if(claimObj != null && claimObj.getPriorityEvent() != null && !claimObj.getPriorityEvent().trim().isEmpty()){
			inputArray[SHAConstants.INDEX_PRIORITY] = claimObj.getPriorityEvent() ;
		}
		else if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		} else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		}else if (claimObj.getClaimPriorityLabel() != null && claimObj.getClaimPriorityLabel().equals("Y")) {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.ATOS;
		} else {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
		}

		inputArray[SHAConstants.INDEX_RECONSIDERATION_FLAG] = SHAConstants.N_FLAG;
		
		if(isQueryFlag){
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.QUERY_REPLY;		
		}else{
			inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
		}

		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.DOCUMENT_ACKNOWLEDGED;
		inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_SUBMIT;

		//inputArray[SHAConstants.INDEX_ROD_KEY] = reimb.getKey();

		/*if(documentList != null && !documentList.isEmpty()){
			for (RODDocumentSummary docSummary : documentList) {
				if(docSummary.getFileType() != null && docSummary.getFileType().getValue() != null && docSummary.getFileType().getValue().contains("Bill")){
					inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "Y";
					break;
				}else{
					inputArray[SHAConstants.INDEX_BILL_AVAILABLE] = "N";
				}
			}
		}*/		

		inputArray[SHAConstants.INDEX_CLAIM_TYPE] = claimObj.getClaimType().getValue();
		inputArray[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "N";
		if (docAck.getDocumentReceivedFromId() != null
				&& docAck.getDocumentReceivedFromId().getValue() != null) {
			inputArray[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = docAck.getDocumentReceivedFromId().getValue();
		}

		inputArray[SHAConstants.INDEX_ZONAL_BYPASS] = "N";
		//inputArray[SHAConstants.INDEX_CLAIMED_AMT] = ackDocReceivedService.getClaimedAmount(reimb);
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
	}
	
	@SuppressWarnings("unchecked")
	public Hospitals getHospitalById(Long key) {
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}
		return null;
	}
}
