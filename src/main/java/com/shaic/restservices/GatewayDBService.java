package com.shaic.restservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.jdbc.OracleTypes;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.GatewayClaimRequest;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasApiDetails;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.restservices.bancs.sendped.PEDSendDetails;
import com.shaic.restservices.bancs.sendped.PEDTransaction;
import com.shaic.restservices.bancs.sendpedquery.SendPEDQueryDetails;
import com.shaic.restservices.bancs.sendpedquery.SendPEDQueryTransaction;
import com.shaic.restservices.crm.AddIntimationRequest;
import com.shaic.restservices.crm.Error;

public class GatewayDBService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@Inject
	private CreateRODService createRodService;
	
	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	private List<Object> ObjectToBeRemoved = new ArrayList<Object>();
	
	private final Logger log = LoggerFactory.getLogger(GatewayDBService.class);
	
	private final String DATE_FORMAT = "dd/MM/yyyy";
	
	private final String DEFAULT_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
	private static final String NAME_SEPARATOR = "_";
	
	@EJB		
	HosPortalTransactions hosPorTrans;
	
	@EJB		
	PEDTransaction pedTrans;
	
	@EJB		
	SendPEDQueryTransaction sendPEDQueryTransaction;

	@EJB
	FVRDocUploadTrancation fvrTransaction;
	
	@EJB
	private GalaxyClaimsTransaction glxTransaction;
	
	public DocAcknowledgement doProceedIntimationProcess(String processFlowName, RODData rodData) throws Exception{
		DocAcknowledgement docAck;
		try {
			docAck = createAckAndSubmitROD(processFlowName,rodData);
			if(entityManager != null){
				entityManager.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
			
		}
		return docAck;
	}
	
	/**
	 * 
	 * @param rodData
	 * @return
	 * @throws Exception 
	 */
	public DocAcknowledgement createAckAndSubmitROD(String flowName, RODData rodData) throws Exception{
		List<Object> listOfObjects = new ArrayList<Object>();
		DocAcknowledgement ackRecord = null;
		AcknowledgeDocument ackDocRecord =  null;
		RODDocumentCheckList rodDocListRecord = null;
		ReimbursementQuery reimbursementQuery = null;
		Claim claim = getClaimRecord(rodData.getIntimationNumber());
		
		if(claim == null){
			throw new Exception("Claim not found for the intimation "+rodData.getIntimationNumber());
		}
		try{
			// Create Ack Insert in IMS_CLS_DOC_ACKNOWLEDGEMENT table
			ackRecord = new DocAcknowledgement();
			ackRecord.setClaim(claim);		
			ackRecord.setAcknowledgeNumber(generateAcknowledgeNo(claim));
			ackRecord.setDocumentReceivedFromId(getKeyForValue(ReferenceTable.ACK_DOC_RECEIVED_FROM,rodData.getDocReceivedFrom()));
			ackRecord.setDocumentReceivedDate(convertToDate(rodData.getDocReceivedDate(), DEFAULT_DATE_TIME_FORMAT));
			ackRecord.setModeOfReceiptId(getKeyForValue(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT,rodData.getModeOfReceipt()));
			ackRecord.setReconsiderationRequest(rodData.getReconsiderationRequest());
			ackRecord.setInsuredContactNumber(rodData.getAckContactNumber());
			ackRecord.setInsuredEmailId(rodData.getEmailId());
			
			ackRecord.setHospitalisationFlag(rodData.getHospitalizationFlag());
			ackRecord.setPreHospitalisationFlag(rodData.getPreHospitalizationFlag());
			ackRecord.setPostHospitalisationFlag(rodData.getPostHospitalizationFlag());
			ackRecord.setPartialHospitalisationFlag(rodData.getPartialHospitalizationFlag());
			ackRecord.setLumpsumAmountFlag(rodData.getLumpsumAmountFlag());
			ackRecord.setHospitalCashFlag(rodData.getAddOnBenefitsHospitalCashFlag());
			ackRecord.setPatientCareFlag(rodData.getAddOnBenefitsPatientCareFlag());
			ackRecord.setAdditionalRemarks(rodData.getAdditionalRemarks());
			ackRecord.setActiveStatus(1L);
			
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			ackRecord.setStage(stage);
			
			Status status = new Status();
			status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			ackRecord.setStatus(status);
			
			ackRecord.setCreatedBy(rodData.getCreatedBy());
			ackRecord.setCreatedDate(new Date());
			ackRecord.setHospitalizationClaimedAmount(Double.parseDouble(rodData.getHospitalizationClaimedAmt()));
			ackRecord.setPreHospitalizationClaimedAmount(Double.parseDouble(rodData.getPreHospitalizationClaimedAmt()));
			ackRecord.setPostHospitalizationClaimedAmount(Double.parseDouble(rodData.getPostHospitalizationClaimedAmt()));
			ackRecord.setHospitalizationRepeatFlag(rodData.getRepeatHospitalizationFlag());
			
			if(flowName.equalsIgnoreCase("Query-Reply")){
				Long rodKey = getRod(rodData.getRodNumber());
				ackRecord.setRodKey(rodKey);
			}

			Status claimStatus = null;
			if(flowName.equalsIgnoreCase("Create-Ack")){
				claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.PROCESS_PRE_MEDICAL);
				claim.setStatus(claimStatus);
				listOfObjects.add(claim);
			}
			
			listOfObjects.add(ackRecord);
			
			// insert in IMS_CLS_ROD_DOCUMENT_LIST
			List<DocumentDetails> listOfDocuments = rodData.getDocumentDetails();
			
			 // Doc Ack Insert IMS_CLS_ACKNOWLEDGE_DOCUMENT table and Insert in  IMS_CLS_ROD_DOCUMENT_LIST
			for(DocumentDetails docRec : listOfDocuments){
				
				ackDocRecord =  new AcknowledgeDocument();
				ackDocRecord.setDocAcknowledgement(ackRecord);
				ackDocRecord.setClaimKey(claim.getKey());
				ackDocRecord.setFileType(getKeyForValue(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE,docRec.getFileType()));
				ackDocRecord.setDocToken(docRec.getDocumentToken());
				ackDocRecord.setDeleteFlag("N");
				ackDocRecord.setCreatedBy("");
				ackDocRecord.setCreatedDate(new Date());
				ackDocRecord.setFileName(docRec.getDocumentName());
				
				
				rodDocListRecord = new RODDocumentCheckList();
				rodDocListRecord.setDocAcknowledgement(ackRecord);
				rodDocListRecord.setDocumentTypeId(getDocumentTypeId(docRec.getFileType()));
				rodDocListRecord.setReceivedStatusId(getKeyForValue(ReferenceTable.ACK_DOC_RECEIVED_STATUS,"Received Original"));
				ackDocRecord.setClaimKey(claim.getKey());
				
				listOfObjects.add(ackDocRecord);
				listOfObjects.add(rodDocListRecord);
			}
			
			if(flowName.equalsIgnoreCase("Query-Reply")){
				reimbursementQuery = getReimQuery(Long.parseLong(rodData.getQueryKey()));
				reimbursementQuery.setQueryReply("Y");
				reimbursementQuery.setQueryRemarks(rodData.getQueryReplyRemarks());
				reimbursementQuery.setDocAcknowledgement(ackRecord);
				listOfObjects.add(reimbursementQuery);
			}
			
			for(Object obj: listOfObjects){
				ObjectToBeRemoved.add(obj);
				entityManager.persist(obj);
			}

			Double totalClaimedAmt = ackRecord.getHospitalizationClaimedAmount() + ackRecord.getPreHospitalizationClaimedAmount() + ackRecord.getPostHospitalizationClaimedAmount();
			DBCalculationService dbCalculationService = new DBCalculationService();
			if (null != ackRecord.getClaim()
					&& null != ackRecord.getClaim().getIntimation()
					&& null != ackRecord.getClaim().getIntimation().getInsured()
					&& null != ackRecord.getClaim().getIntimation().getInsured()
							.getInsuredId()
					&& null != ackRecord.getClaim().getIntimation().getPolicy().getKey()) {
				Double insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(ackRecord.getClaim().getIntimation()
								.getInsured().getInsuredId().toString(), ackRecord
								.getClaim().getIntimation().getPolicy()
								.getKey(),ackRecord.getClaim().getIntimation()
								.getInsured().getLopFlag());
				Double balSI = dbCalculationService
						.getBalanceSI(
								ackRecord.getClaim().getIntimation().getPolicy()
										.getKey(),
										ackRecord.getClaim().getIntimation().getInsured()
										.getKey(), ackRecord.getClaim().getKey(),
								insuredSumInsured,
								ackRecord.getClaim().getIntimation().getKey())
						.get(SHAConstants.TOTAL_BALANCE_SI);
				Double amt = Math.min(balSI, totalClaimedAmt);

				claim.setCurrentProvisionAmount(amt);
				claim.setClaimedAmount(amt);
				claim.setProvisionAmount(amt);
			}
			if(flowName.equalsIgnoreCase("Query-Reply")){
				submitTaskToDB(ackRecord,true);
			}else{
				submitTaskToDB(ackRecord,false);
			}
		}catch(Exception exp){
			if(!ObjectToBeRemoved.isEmpty()){
				for(Object obj: ObjectToBeRemoved){
					entityManager.remove(obj);
				}
			}
			exp. printStackTrace();
			log.info("Exception Occurred on ROD creation :"+ exp.getMessage());
			throw new Exception(exp);
		}
		//System.out.println("Key : "+ackRecord.getKey());
		entityManager.flush();
		return ackRecord;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateParametersForAckReport(String argIntimationNumber, DocAcknowledgement argAck){
		ReceiptOfDocumentsDTO ackReportDto = new ReceiptOfDocumentsDTO();
		DocumentDetailsDTO docDTO = new DocumentDetailsDTO();
		List<RODDocumentCheckList> docCheckList = new ArrayList();
		docCheckList = getDocumentCheckList(argAck.getAcknowledgeNumber());
		if(docCheckList != null && docCheckList.size() > 0){
			DocumentCheckListDTO chkListDTO = null;
			List<DocumentCheckListDTO> docChkListDto = new ArrayList<DocumentCheckListDTO>();
			for(RODDocumentCheckList rec : docCheckList){
				chkListDTO = new DocumentCheckListDTO();
				chkListDTO.setValue(getDocumentTypeByKey(rec.getDocumentTypeId()));
				chkListDTO.setStrReceivedStatus("Photocopy / Original");
				chkListDTO.setRemarks("Enclosed the same.");
				chkListDTO.setSerialNumber(docCheckList.indexOf(rec)+1);				
				docChkListDto.add(chkListDTO);				
			}
			docDTO.setDocumentCheckList(docChkListDto);
			docDTO.setDocumentsReceivedDate(new Date());
			ackReportDto.setDocumentDetails(docDTO);
			docDTO.setAcknowledgementNumber(argAck.getAcknowledgeNumber());
			ClaimDto clmDto = new ClaimMapper().getInstance().getClaimDto(argAck.getClaim());
			clmDto.setModifiedBy("Portal");
			NewIntimationDto intimDto = new NewIntimationMapper().getInstance().getNewIntimationDto(argAck.getClaim().getIntimation());
			OrganaizationUnit ouObj = getInsuredOfficeNameByDivisionCode(argAck.getClaim().getIntimation().getPolicy().getHomeOfficeCode());
			if(ouObj != null){
				intimDto.getPolicy().setPolOfficeAddr1(ouObj.getAddress());
				intimDto.getPolicy().setPolOfficeAddr2((ouObj.getCityTownVillage() != null)?ouObj.getCityTownVillage().getValue():"");
				intimDto.getPolicy().setPolOfficeAddr3((ouObj.getState() != null)?ouObj.getState().getValue() : "");
				intimDto.getPolicy().setPolTelephoneNumber("");
			}
			clmDto.setNewIntimationDto(intimDto);
			ackReportDto.setClaimDTO(clmDto);
			
			ReportDto reportDto = new ReportDto();
			reportDto.setClaimId(clmDto.getClaimId());
			List<ReceiptOfDocumentsDTO> rodDTOList = new ArrayList<ReceiptOfDocumentsDTO>();
			rodDTOList.add(ackReportDto);		
			reportDto.setBeanList(rodDTOList);
			OnlineDocumentGenerator docGen = new OnlineDocumentGenerator();
			String ackFilePath = docGen.generatePdfDocument("OnlineAckReceipt", reportDto);
			clmDto.setDocFilePath(ackFilePath);
			clmDto.setDocType("Portal_AckReceipt");
			uploadCoveringLetterToDMs(clmDto);
			System.out.println("Ack letter generated...."+ackFilePath);
			String ackCoveringLetterFilePath = docGen.generatePdfDocument("CoveringLetterFromInsured", reportDto);
			clmDto.setDocFilePath(ackCoveringLetterFilePath);
			clmDto.setDocType("Portal_AckCoveringLetter");
			Reimbursement reimbursement  = getReimbursementRecord(argAck.getKey());	
			if(reimbursement != null){	
				clmDto.setRodNumber(reimbursement.getRodNumber());	
			}
			uploadCoveringLetterToDMs(clmDto);
			System.out.println("Covering letter generated...."+ackCoveringLetterFilePath);
		}else{
			System.out.println("Acknowledgement letter not generated for intimation :"+argIntimationNumber);
		}
	}
	
	public void uploadCoveringLetterToDMs(ClaimDto claimDto){

		if(null != claimDto.getDocFilePath() && !claimDto.getDocFilePath().isEmpty())
		{
			String strUserName = claimDto.getModifiedBy();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			
			WeakHashMap dataMap = new WeakHashMap();
			if(null != claimDto)
			{
				dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
				dataMap.put("claimNumber",claimDto.getClaimId());
				dataMap.put("reimbursementNumber",claimDto.getRodNumber());
				dataMap.put("filePath", claimDto.getDocFilePath());
				dataMap.put("docType", claimDto.getDocType());
				dataMap.put("docSources", "Portal_ACK_Covering_Letter");
				dataMap.put("createdBy", userNameForDB);
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			}
		}
	}
	
	
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(String polDivnCode) {
		List<OrganaizationUnit> organizationUnit = null;
		if(polDivnCode != null){			
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode", polDivnCode);
			organizationUnit = (List<OrganaizationUnit>)findAll.getResultList();
		}
		if(organizationUnit != null && ! organizationUnit.isEmpty()){
			return organizationUnit.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param claimNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RODDocumentCheckList> getDocumentCheckList(String argAckNumber){
		Query query = null;		
		query = entityManager.createNamedQuery("DocAcknowledgement.findAckNo");
        query = query.setParameter("ackNo", argAckNumber);
    	List<DocAcknowledgement> ackRec = query.getResultList();
    	if(!ackRec.isEmpty()){
    		query = entityManager.createNamedQuery("RODDocumentCheckList.findByDocAck");
            query = query.setParameter("docAck", ackRec.get(0).getKey());
        	List<RODDocumentCheckList> rodDocumentCheckList = query.getResultList();
        	if(!rodDocumentCheckList.isEmpty()){
        		return rodDocumentCheckList;
        	}else{
        		return null;
        	}
    	}else{
    		return null;
    	}
	}
	
	/**
	 * 
	 * @param claimNumber
	 * @return
	 */
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
	
	
	
	@SuppressWarnings("unchecked")
	public String getDocumentTypeByKey(Long argDocumentTypeId) {
		Query query = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
		query = query.setParameter("primaryKey", argDocumentTypeId);
		List<DocumentCheckListMaster> documentCheckList = query.getResultList();
		if(!documentCheckList.isEmpty()){
			return documentCheckList.get(0).getValue();
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
	
	@SuppressWarnings("unchecked")
	public Long getRod(String argRodNumber) {
		Query query = entityManager.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", argRodNumber);
		List<Reimbursement> rodList = query.getResultList();
		if(!rodList.isEmpty()){
			return rodList.get(0).getKey();
		}else{
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @param code
	 * @param Value
	 * @return
	 */
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
	/**
	 * 
	 * @param ifscCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BankMaster getBankDetails(String ifscCode) {
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> bankList = query.getResultList();
		if (null != bankList && !bankList.isEmpty()) {
			return bankList.get(0);
		} else {
			return null;
		}

	}
	/**
	 * 
	 * @param policyNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public boolean checkPolicyNumber(String policyNo) {
    	Query query = null;
   		query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
        query = query.setParameter("policyNumber", policyNo);
    	List<Policy> policyDataList = query.getResultList();
    	if(policyDataList.isEmpty()){
    		return false;
    	}else{
    		return true;
    	}
    }
	/**
	 * 
	 * @param policyNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkRequest(String policyNo) {
    	Query query = null;
   		query = entityManager.createNamedQuery("GatewayClaimRequest.findByPolicyNumber");
        query = query.setParameter("policyNumber", policyNo);
		query = query.setParameter("readFlag", "N");
		query = query.setParameter("respondedFlag", "N");
    	List<GatewayClaimRequest> gatewayData = query.getResultList();
    	if(gatewayData.isEmpty()){
    		return false;
    	}else{
    		return true;
    	}
    }
	/**
	 * 
	 * @param obj
	 */
	public void persistObject(Object obj){
		entityManager.persist(obj);
	}
	/**
	 * 
	 * @param claim
	 * @return
	 */
	public String generateGlxRODNumber(Claim claim){
		Long count = createRodService.getACknowledgeNumberCountByClaimKey(claim.getKey());
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ROD/").append(claim.getIntimation().getIntimationId()).append("/").append(lackCount);
		return ackNoBuf.toString();
	}
	
	/**
	 * 
	 * @param claim
	 * @return
	 */
	private String generateAcknowledgeNo(Claim claim) {
		Long count = ackDocReceivedService.getCountOfAckByClaimKey(claim.getKey());
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ACK/")
				.append(claim.getIntimation().getIntimationId()).append("/").append(lackCount);
		return ackNoBuf.toString();
	}
	
	/**
	 * 
	 * @param rodNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Reimbursement getReimbursementObject(String rodNo) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if (null != reimbursementObjectList
				&& !reimbursementObjectList.isEmpty()) {
			entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param rodNo
	 * @return
	 */
/*	@SuppressWarnings("unchecked")
	private List<RODDocumentSummary> getReimbursementDocSummary(Long rodKey) {
		Query query = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);
		List<RODDocumentSummary> reimbursementDocList = query.getResultList();
		return reimbursementDocList;
	}*/
	
	
	
	/**
	 * 
	 * @param acknowledgementKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {

		Query query = entityManager.createNamedQuery("DocAcknowledgement.findByKey");
		query.setParameter("ackDocKey", acknowledgementKey);
		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param acknowledgementKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgmentByNo(String acknowledgementNumber) {
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckNo");
		query.setParameter("ackNo", acknowledgementNumber);
		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param reimKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ReimbursementQuery getReimQuery(Long reimKey){
		Query query = entityManager.createNamedQuery("ReimbursementQuery.findLatestQueryByKey");
		query.setParameter("primaryKey", reimKey);
		List<ReimbursementQuery> reimbursementList = (List<ReimbursementQuery>) query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param dateValueInString
	 * @return
	 * @throws ParseException 
	 */
	public Date convertToDate(String dateValueInString, String dateFormatToBeConverted) throws ParseException{
		SimpleDateFormat  sdf = null;
		Date date = null;
		if(!StringUtils.isBlank(dateValueInString)){
			 sdf = new SimpleDateFormat(dateFormatToBeConverted);
			 date = sdf.parse(dateValueInString);
		}
		return date;
		/*if(!StringUtils.isBlank(dateValueInString)){
			return new Date(Long.parseLong(dateValueInString));
		}else{
			return new Date();
		}*/
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
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
		} else if (claimObj.getClaimPriorityLabel() != null && claimObj.getClaimPriorityLabel().equals("Y")) {
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
	
	public byte[] dummyFileForTesting(){
		byte[] fileAsBytes = new byte[10000];
		File file = new File("D:\\dummyFileLocation\\VPCEG25EN _ E Series.pdf");
		FileInputStream inputStream = null;
		try{
			inputStream = new FileInputStream(file);
			fileAsBytes = IOUtils.toByteArray(inputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fileAsBytes;
	}
	
	public static String dummyFileForTestingBase64(){
		String base64_String = "";
		byte[] fileAsBytes = new byte[10000];
		File file = new File("D:\\dummyFileLocation\\Lighthouse.jpg");
		FileInputStream inputStream = null;
		try{
			inputStream = new FileInputStream(file);
			fileAsBytes = IOUtils.toByteArray(inputStream);
			base64_String = Base64.encodeBase64String(fileAsBytes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return base64_String;
	}
	
	public String uploadHospitalDocumentService(HospitalDocDetails argObj) throws Exception{
		String uploadStatus = "";
		try{
			uploadStatus = hosPorTrans.doHospitalTransaction(argObj);
			System.out.println(uploadStatus);		
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while uploading hospital document.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
		/*
		String uploadStatus = "";
		boolean tempStatus = false;
		try{
			if(argObj.getDocumentDetails() != null && argObj.getDocumentDetails().size() > 0){
				for(HospitalDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(!StringUtils.isBlank(docObj.getFileContent())){
						byte[] dbytes = Base64.decodeBase64(docObj.getFileContent());
						String fileName = (StringUtils.isBlank(docObj.getFileName()))?"EMPTY_FILENAME":docObj.getFileName();
						HashMap dmsStatus = SHAFileUtils.writeAndSendFileToDMS(docObj.getDocId()+"_"+fileName, dbytes);
						boolean isFileUploaded = (boolean) dmsStatus.get("status");
						if(isFileUploaded){
							docObj.setFileUploadInDMS(true);
							docObj.setFileDMSToken(new Long(String.valueOf(dmsStatus.get("fileKey"))));
						}else{
							docObj.setFileUploadInDMS(false);
						}
					}
				}
				for(HospitalDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(docObj.isFileUploadInDMS()){
						tempStatus = true;
					}else{
						tempStatus = false;
					}
				}
				
				if(tempStatus){
					// insert all the details in the table.
					String loginUserId = "ADMIN";
					Date uploadDate = null;
					Intimation intim = getIntimationByNo(argObj.getIntimationNumber());
					
					HospitalUploadDetails hosDetails = new HospitalUploadDetails();
					hosDetails.setIntimationKey(intim.getKey());
					hosDetails.setIntimationNo(intim.getIntimationId());
					hosDetails.setProviderCode(argObj.getProviderCode());
					hosDetails.setClaimedAmount((argObj.getClaimedAmount().equals("") || argObj.getClaimedAmount().equals("null"))?0:Long.parseLong(argObj.getClaimedAmount()));
					hosDetails.setHospitalName(argObj.getHospitalName());
					hosDetails.setFileTypeId(argObj.getFileTypeId());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
					uploadDate = formatter.parse(argObj.getUploadDate());
					hosDetails.setUploadDate(uploadDate);
					hosDetails.setCreatedBy(loginUserId);
					hosDetails.setCreatedDate(new Date());
					hosDetails.setActiveStatus(1L);
					entityManager.persist(hosDetails);
					
					HospitalUploadDocumentDetails docDetails = null;
					for(HospitalDocUploadDetails reqDocDetails : argObj.getDocumentDetails()){
						docDetails = new HospitalUploadDocumentDetails();
						docDetails.setHospitalUploadDetails(hosDetails);
						docDetails.setDocId(Long.parseLong(reqDocDetails.getDocId()));
						docDetails.setIntimationKey(intim.getKey());
						docDetails.setIntimationNo(intim.getIntimationId());
						docDetails.setFileTypeId((reqDocDetails.getFileTypeId() == null)?0:Long.parseLong(reqDocDetails.getFileTypeId()));
						docDetails.setFileTypeName(reqDocDetails.getFileTypeName());
						docDetails.setFileContent(reqDocDetails.getFileContent());
						docDetails.setFileName(reqDocDetails.getFileName());
						docDetails.setUploadSource("HP");
						docDetails.setFileServer("EF");
						docDetails.setFileKey((reqDocDetails.getFileDMSToken() == null)?0:reqDocDetails.getFileDMSToken());
						docDetails.setCreatedBy(loginUserId);
						docDetails.setCreatedDate(new Date());
						docDetails.setProcessedFlag("N");
						docDetails.setActiveStatus(1L);
						entityManager.persist(docDetails);
					}
					uploadStatus = "Successfully Uploaded";
					entityManager.flush();
					
					Date date=new Date(uploadDate.getTime());
			        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
			        String dateText = df2.format(date);
			        System.out.println(dateText);
					// calling the procedure
			        Long refId = getPremiaIntimation(intim.getIntimationId()).getGiIntimationId();
			        if(refId != null){
			        	System.out.println(refId+"---"+intim.getIntimationId()+"---"+hosDetails.getProviderCode()+"---"+hosDetails.getClaimedAmount()+"---"+dateText+"---"+hosDetails.getHospitalName()+"---"+Long.parseLong(hosDetails.getFileTypeId()));
			        	String procStatus = dbCalcService.callHPPushGlx(refId,intim.getIntimationId(),hosDetails.getProviderCode(),hosDetails.getClaimedAmount(),dateText,hosDetails.getHospitalName(),Long.parseLong(hosDetails.getFileTypeId()));
			        	System.out.println(procStatus);
			        }
					
				}else{
					uploadStatus = "Some of the file(s) not uploaded in the DMS.";
				}
				
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while uploading hospital document.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	*/}
	
	public String updatePEDStatusService(PEDSendDetails argObj) throws Exception{
		String uploadStatus = "";
		try{
			uploadStatus = pedTrans.doPEDTransaction(argObj);
			System.out.println(uploadStatus);		
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while Updating PED Status.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}

	public String updateSendPEDQueryService(SendPEDQueryDetails argObj) throws Exception{
		String uploadStatus = "";
		try{
			uploadStatus = sendPEDQueryTransaction.doSendPEDQueryTransaction(argObj);
			System.out.println(uploadStatus);		
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while Updating PED Status.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}

	public String uploadFVRDocumentService(FVRDocDetails fvrObject) throws Exception {
		String uploadStatus = "";
		try {
			uploadStatus = fvrTransaction.doFVRTransaction(fvrObject);
			System.out.println(uploadStatus);
		} catch (Exception exp) {
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while uploading FVR document.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("Intimation.findByIntimationNumber").setParameter("intimationNo", intimationNo);
		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PremiaIntimationTable getPremiaIntimation(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("PremiaIntimationTable.findByIntimationNumber").setParameter("intimationNumber", intimationNo);
		List<PremiaIntimationTable> intimationList = (List<PremiaIntimationTable>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	public static void main(String[] args) {
//		System.out.println(dummyFileForTestingBase64());
		callDBWebService();
	}
	
	public static void callDBWebService(){
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		Clob output = null;
		try{
//			connection = BPMClientContext.getConnection();
			Class.forName("oracle.jdbc.driver.OracleDriver");  
//			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.6.105:1526:GLXQA2DB","IMS_GALTXN","Star123");  
			
			cs = connection.prepareCall("{call PRC_WEB_SERVICE_TEST(?, ?)}");
//			cs.setString(1, "CLI/2020/151118/0057470");
			cs.setString(1, "CLI/2020/151118/0300880");			
			cs.registerOutParameter(2, Types.CLOB, "LV_REST_OUT");
			cs.execute();

			output = (Clob) cs.getClob(2);
			output.getCharacterStream();
			char clobVal[] = new char[(int) output.length()];
			Reader r = output.getCharacterStream();
			r.read(clobVal);
			StringWriter sw = new StringWriter();
			sw.write(clobVal);
			System.out.println(sw.toString());

		}catch(Exception exp){
			System.out.println("Exception occured in callDBWebService "+exp.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String performSearch(GalaxySearchRequest argSearch){
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		JSONObject structObj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONArray dataArr = new JSONArray();
		try{
			
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ESB_CLAIM_INTG_AGNT_PORTAL(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, argSearch.getAgentId());
			cs.setString(2, argSearch.getSearch());
			cs.setString(3, argSearch.getValue());
			cs.setInt(4, argSearch.getPageNo().intValue());
			cs.setInt(5, argSearch.getOffset().intValue());
			cs.registerOutParameter(6, Types.INTEGER, "LN_REC_COUNT");
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			Integer rowCount = Integer.parseInt(cs.getObject(6).toString());
			rs = (ResultSet) cs.getObject(7);

			JSONArray claimArr = new JSONArray();
			String policyNo = "";
			String policyName = "";
			String proposerCode = "";
			String proposerName = "";
			
			if (rs != null) {
					while (rs.next()) {
						policyNo = rs.getString("POLICY_NUMBER");
						policyName = rs.getString("VALUE");
						proposerCode = rs.getString("PROPOSER_CODE");
						proposerName = rs.getString("PROPOSER_NAME");

						JSONObject claimObj = new JSONObject();
						if(!StringUtils.isBlank(rs.getString("HOSPITAL_NAME")) && (!StringUtils.isBlank(rs.getString("CLAIM_STATUS")))){
							claimObj.put("hospitalName", rs.getString("HOSPITAL_NAME"));
							claimObj.put("claimDate", (rs.getDate("CLAIM_REG_DATE") == null)?"":rs.getDate("CLAIM_REG_DATE").getTime());
							claimObj.put("status", (rs.getString("CLAIM_STATUS") == null)?"":rs.getString("CLAIM_STATUS"));
							claimObj.put("dateOfIntimation", (rs.getDate("CLAIM_REG_DATE") == null)?"":rs.getDate("CLAIM_REG_DATE").getTime());
							claimObj.put("dateOfAdmission", (rs.getDate("DATE_OF_ADMISSION") == null)?"":rs.getDate("DATE_OF_ADMISSION").getTime());
							claimObj.put("dateOfDischarge", (rs.getDate("DATE_OF_DISCHARGE") == null)?"":rs.getDate("DATE_OF_DISCHARGE").getTime());
							claimObj.put("reasonForAdmission", (rs.getString("ADMISSION_REASON") == null)?"":rs.getString("ADMISSION_REASON"));
							claimObj.put("patientName", (rs.getString("INSURED_NAME") == null)?"":rs.getString("INSURED_NAME"));
							claimObj.put("claimNumber", (rs.getString("INTIMATION_NUMBER") == null)?"":rs.getString("INTIMATION_NUMBER"));
							claimObj.put("healthCardNo", (rs.getString("HEALTH_CARD_NUMBER") == null)?"":rs.getString("HEALTH_CARD_NUMBER"));
							claimArr.put(claimObj);
						}
					}
			}
			if(!StringUtils.isBlank(policyNo)){
				structObj.put("policyNumber", policyNo);
				structObj.put("policyName", policyName);
				structObj.put("iconUrl", "");
				structObj.put("name", proposerName);
				structObj.put("customerId", proposerCode);
				
//			if(claimArr.length() > 0){
				structObj.put("claimsList", claimArr);
			/*}else{
				structObj.put("claimsList", "");
			}*/
				dataArr.put(structObj);			
			}
			response.put("message","");
			response.put("data", dataArr);
			response.put("resCode", 200);
			
		}catch(Exception exp){
			System.out.println("Exception occured in performSearch "+exp.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(response.toString());
		return response.toString();
	}
	
	public String performCustomerIdSearch(GalaxyCustomerSearchRequest argSearch){
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		JSONObject structObj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONArray dataArr = new JSONArray();
		try{
			
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_ESB_CLAIM_INT_PROP_PORTAL(?, ?, ?, ?, ?, ?, ?)}");
			cs.setString(1, argSearch.getAgentId());
			cs.setString(2, argSearch.getSearch());
			cs.setString(3, argSearch.getValue());
			cs.setInt(4, argSearch.getPageNo().intValue());
			cs.setInt(5, argSearch.getOffset().intValue());
			cs.registerOutParameter(6, Types.INTEGER, "LN_REC_COUNT");
			cs.registerOutParameter(7, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			Integer rowCount = Integer.parseInt(cs.getObject(6).toString());
			rs = (ResultSet) cs.getObject(7);

			JSONArray claimArr = new JSONArray();
			String policyNo = "";
			String policyName = "";
			String proposerCode = "";
			String proposerName = "";
			
			if (rs != null) {
					while (rs.next()) {
						policyNo = rs.getString("POLICY_NUMBER");
						policyName = rs.getString("VALUE");
						proposerCode = rs.getString("PROPOSER_CODE");
						proposerName = rs.getString("PROPOSER_NAME");

						JSONObject claimObj = new JSONObject();
						if(!StringUtils.isBlank(rs.getString("HOSPITAL_NAME")) && (!StringUtils.isBlank(rs.getString("CLAIM_STATUS")))){
							claimObj.put("hospitalName", rs.getString("HOSPITAL_NAME"));
							claimObj.put("claimDate", (rs.getDate("CLAIM_REG_DATE") == null)?"":rs.getDate("CLAIM_REG_DATE").getTime());
							claimObj.put("status", (rs.getString("CLAIM_STATUS") == null)?"":rs.getString("CLAIM_STATUS"));
							claimObj.put("dateOfIntimation", (rs.getDate("CLAIM_REG_DATE") == null)?"":rs.getDate("CLAIM_REG_DATE").getTime());
							claimObj.put("dateOfAdmission", (rs.getDate("DATE_OF_ADMISSION") == null)?"":rs.getDate("DATE_OF_ADMISSION").getTime());
							claimObj.put("dateOfDischarge", (rs.getDate("DATE_OF_DISCHARGE") == null)?"":rs.getDate("DATE_OF_DISCHARGE").getTime());
							claimObj.put("reasonForAdmission", (rs.getString("ADMISSION_REASON") == null)?"":rs.getString("ADMISSION_REASON"));
							claimObj.put("patientName", (rs.getString("INSURED_NAME") == null)?"":rs.getString("INSURED_NAME"));
							claimObj.put("claimNumber", (rs.getString("INTIMATION_NUMBER") == null)?"":rs.getString("INTIMATION_NUMBER"));
							claimObj.put("healthCardNo", (rs.getString("HEALTH_CARD_NUMBER") == null)?"":rs.getString("HEALTH_CARD_NUMBER"));
							claimObj.put("queryPending", (rs.getString("QUERY_PENDING") == null)?"":rs.getString("QUERY_PENDING"));
							claimArr.put(claimObj);
						}
					}
			}
			if(!StringUtils.isBlank(policyNo)){
				structObj.put("policyNumber", policyNo);
				structObj.put("policyName", policyName);
				structObj.put("iconUrl", "");
				structObj.put("claimCount", rowCount);
				structObj.put("name", proposerName);
				structObj.put("customerId", proposerCode);
				
//			if(claimArr.length() > 0){
				structObj.put("claimsList", claimArr);
			/*}else{
				structObj.put("claimsList", "");
			}*/
				dataArr.put(structObj);			
			}
			response.put("message","");
			response.put("data", dataArr);
			response.put("resCode", 200);
			
		}catch(Exception exp){
			exp.printStackTrace();
			System.out.println("Exception occured in performCustomerIdSearch "+exp.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(response.toString());
		return response.toString();
	}
	
	@SuppressWarnings("unchecked")
	public boolean validateRequest(Object argSearch){
		boolean isValidRequest = false;
		String userName = "";
		String password = "";
		if(argSearch instanceof AgentClaimSearchRequest){
			userName = ((AgentClaimSearchRequest)argSearch).getUsername();
			password = ((AgentClaimSearchRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxySearchRequest){
			userName = ((GalaxySearchRequest)argSearch).getUsername();
			password = ((GalaxySearchRequest)argSearch).getPassword();
		}
		if(argSearch instanceof AddIntimationRequest){
			userName = ((AddIntimationRequest)argSearch).getUsername();
			password = ((AddIntimationRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxyCreateRodRequest){
			userName = ((GalaxyCreateRodRequest)argSearch).getUsername();
			password = ((GalaxyCreateRodRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxyViewQueryRequest){
			userName = ((GalaxyViewQueryRequest)argSearch).getUsername();
			password = ((GalaxyViewQueryRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxyCustomerSearchRequest){
			userName = ((GalaxyCustomerSearchRequest)argSearch).getUsername();
			password = ((GalaxyCustomerSearchRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxyQueryResponseRequest){
			userName = ((GalaxyQueryResponseRequest)argSearch).getUsername();
			password = ((GalaxyQueryResponseRequest)argSearch).getPassword();
		}
		Query query = entityManager.createNamedQuery("MasApiDetails.findByAuth");
		query = query.setParameter("userName", userName);
		query = query.setParameter("password", password);
		List<MasApiDetails> dataObjList = query.getResultList();
		if(dataObjList != null && !dataObjList.isEmpty()){
			isValidRequest = true;
		}
		return isValidRequest;
	}

	
	
	/*@SuppressWarnings("unchecked")
	public boolean validateRequest(Object argSearch){
		boolean isValidRequest = false;
		String userName = "";
		String password = "";
		if(argSearch instanceof AgentClaimSearchRequest){
			userName = ((AgentClaimSearchRequest)argSearch).getUsername();
			password = ((AgentClaimSearchRequest)argSearch).getPassword();
		}
		if(argSearch instanceof GalaxyCreateRodRequest){
			userName = ((GalaxyCreateRodRequest)argSearch).getUsername();
			password = ((GalaxyCreateRodRequest)argSearch).getPassword();
		}
		Query query = entityManager.createNamedQuery("MasApiDetails.findByAuth");
		query = query.setParameter("userName", userName);
		query = query.setParameter("password", password);
		List<MasApiDetails> dataObjList = query.getResultList();
		if(dataObjList != null && !dataObjList.isEmpty()){
			isValidRequest = true;
		}
		return isValidRequest;
	}*/
	
	public String performClaimSearch(AgentClaimSearchRequest argSearch){
		JSONObject structObj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONArray dataArr = new JSONArray();
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		 try{
//			 if(reimRec != null){
				 connection = BPMClientContext.getConnection();
				 cs = connection.prepareCall("{call PRC_AGNT_PORTAL_STATUS(?, ?, ?, ?, ?, ?)}"); // ,?
				 cs.setString(1, argSearch.getClaimNumber());
				 cs.setString(2, argSearch.getHealthCardNo());
				 cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
				 cs.registerOutParameter(4, OracleTypes.CURSOR, "SYS_REFCURSOR");
				 cs.registerOutParameter(5, OracleTypes.CURSOR, "SYS_REFCURSOR");
//				 cs.registerOutParameter(6, OracleTypes.CURSOR, "SYS_REFCURSOR");
				 cs.registerOutParameter(6, Types.VARCHAR);
				 cs.execute();
				 
				 String isValidRequest = "";
				 if (cs.getString(6) != null) {
					 isValidRequest = cs.getString(6);
				 }
				 
				 if(isValidRequest.equals("SUCCESS")){
					 rs = (ResultSet) cs.getObject(3);
					 if (rs != null) {
						 while (rs.next()) {
							 structObj.put("claimNo", (rs.getString("CLAIM_NUMBER") == null)?"":rs.getString("CLAIM_NUMBER"));
							 structObj.put("policyNo", (rs.getString("POLICY_NUMBER") == null)?"":rs.getString("POLICY_NUMBER"));
							 structObj.put("dateOfIntimation", (rs.getDate("CREATED_DATE") == null)?"":rs.getDate("CREATED_DATE").getTime());
							 structObj.put("productName", (rs.getString("PRODUCT_NAME") == null)?"":rs.getString("PRODUCT_NAME"));
							 structObj.put("insuredPatientName", (rs.getString("INSURED_NAME") == null) ?"":rs.getString("INSURED_NAME"));
							 structObj.put("proposerName", (rs.getString("PROPOSER_NAME") == null)?"":rs.getString("PROPOSER_NAME"));
							 structObj.put("healthCardNo", (rs.getString("HEALTH_CARD_NUMBER") == null)?"":rs.getString("HEALTH_CARD_NUMBER"));
							 structObj.put("hospitalName", (rs.getString("HOSPITAL_NAME") == null)?"":rs.getString("HOSPITAL_NAME"));
							 structObj.put("admissionDate", (rs.getDate("ADMISSION_DATE") == null)?"":rs.getDate("ADMISSION_DATE").getTime());
							 structObj.put("hospitalAddress", (rs.getString("ADDRESS") == null)?"":rs.getString("ADDRESS"));
							 structObj.put("reasonForAdmission", (rs.getString("ADMISSION_REASON") == null)?"":rs.getString("ADMISSION_REASON"));
							 structObj.put("hospitalType", (rs.getString("HOSPITAL_TYPE_ID") == null)?"":rs.getString("HOSPITAL_TYPE_ID"));
							 structObj.put("diagnosis", (rs.getString("DIAGNOSIS_VALUE") == null)?"":rs.getString("DIAGNOSIS_VALUE"));
						 }
					 }

					 //Cashless Section
					 rs1 = (ResultSet) cs.getObject(4);
					 JSONArray claimArr = new JSONArray();
					 JSONObject claimObj = null;
					 if (rs1 != null) {
						 while (rs1.next()) {
							 claimObj = new JSONObject();
							 claimObj.put("cashlessType", (rs1.getString("ITERATION_NUMBER") == null)?"":rs1.getString("ITERATION_NUMBER"));
							 claimObj.put("documentReceivedDate", (rs1.getDate("SFX_MATCHEDQ_DATE") == null)?"":rs1.getDate("SFX_MATCHEDQ_DATE").getTime());
							 claimObj.put("claimedAmount", (rs1.getInt("AMOUNT_CLAIMED") == 0)? 0 : rs1.getInt("AMOUNT_CLAIMED"));
							 claimObj.put("approvedAmount", (rs1.getInt("TOTAL_APPROVAL_AMOUNT") == 0)? 0 : rs1.getInt("TOTAL_APPROVAL_AMOUNT"));
							 claimArr.put(claimObj);
						 }
					 }else{
						 /*	claimObj = new JSONObject();
						claimObj.put("cashlessType","");
						claimObj.put("documentReceivedDate", "");
						claimObj.put("claimedAmount", 0);
						claimObj.put("approvedAmount", 0);
						claimArr.put(claimObj);*/
					 }

					 //Reimbursement Section
					 rs2 = (ResultSet) cs.getObject(5);
					 JSONArray reimArr = new JSONArray();
					 JSONArray reimStsArr = null;
					 JSONObject reimObj = null;
					 if (rs2 != null) {
						 while (rs2.next()) {
							 reimObj = new JSONObject();
							 reimObj.put("classification", (rs2.getString("CLASSIFICATION") == null)? "" :rs2.getString("CLASSIFICATION"));
							 reimObj.put("documentReceivedDate", (rs2.getDate("DOCUMENT_RECEIVED_DATE") == null)? "" :rs2.getDate("DOCUMENT_RECEIVED_DATE").getTime());
							 reimObj.put("claimedAmount", (rs2.getInt("CLAIMED_AMOUNT") == 0)? 0 :rs2.getInt("CLAIMED_AMOUNT"));
							 reimObj.put("approvedAmount", (rs2.getInt("APPROVED_AMOUNT") == 0)? 0 :rs2.getInt("APPROVED_AMOUNT"));
							 reimObj.put("status", (rs2.getString("STATUS") == null)? "" :rs2.getString("STATUS"));

							 reimStsArr = new JSONArray();
							 Map<String, String> dataMap = new LinkedHashMap<String, String>();
							 dataMap.put("Register Claim", rs2.getString("REGISTER_CLAIM"));
							 dataMap.put("Documents Submitted", rs2.getString("DOCUMENT_SUBMITTED"));
							 dataMap.put("Documents Scrutiny", rs2.getString("DOCUMENT_SCRUTINY"));
							 dataMap.put("Under Progress", rs2.getString("UNDER_PROGRESS"));
							 dataMap.put("Settled", rs2.getString("SETTLED"));

							 if(dataMap != null && !dataMap.isEmpty()){
								 for (Map.Entry<String,String> entry : dataMap.entrySet()){
									 JSONObject obj = new JSONObject();
									 obj.put("stageName", entry.getKey());
									 obj.put("stageStatus", entry.getValue());
									 reimStsArr.put(obj);
								 }
							 }
							 reimObj.put("statusTrail", reimStsArr);
							 reimArr.put(reimObj);
						 }
					 }else{
						 /*reimObj = new JSONObject();
					 reimObj.put("classification","");
					 reimObj.put("documentReceivedDate", "");
					 reimObj.put("claimedAmount", 0);
					 reimObj.put("approvedAmount", 0);
					 reimStsArr = new JSONArray();
					 reimObj.put("statusTrail",reimStsArr);
					 reimArr.put(reimObj);*/
					 }
					 structObj.put("cashlessDetails", claimArr);
					 structObj.put("reimbursementDetails", reimArr);
					 dataArr.put(structObj);

					 response.put("message","");
					 response.put("data", dataArr);
					 response.put("resCode", 200);
				 }else{
					 response.put("message",isValidRequest);
					 response.put("data", dataArr);
					 response.put("resCode", 406);
				 }
				 
		 }catch(Exception exp){
			 System.out.println("Exception occured in performClaimSearch  "+exp.getMessage());
		 } finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
			System.out.println(response.toString());
			return response.toString();
	}
	
	public String performClaimSearchOld(AgentClaimSearchRequest argSearch){
		JSONObject structObj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONArray dataArr = new JSONArray();
		try{
//			SimpleDateFormat sdf = new SimpleDateFormat(WS_DATE_FORMAT);
				
			Claim claimRec = getClaimRecord(argSearch.getClaimNumber());
			if(claimRec != null){
				structObj.put("claimNo", claimRec.getClaimId());
				structObj.put("policyNo", claimRec.getIntimation().getPolicy().getPolicyNumber());
				structObj.put("dateOfIntimation", (claimRec.getIntimationCreatedDate() != null)?claimRec.getIntimationCreatedDate().getTime() :"");
				structObj.put("productName", claimRec.getIntimation().getPolicy().getProductName());
				structObj.put("insuredPatientName", claimRec.getIntimation().getInsured().getInsuredName());
				structObj.put("proposerName", claimRec.getIntimation().getPolicy().getProposerFirstName());
				structObj.put("healthCardNo", claimRec.getIntimation().getInsured().getHealthCardNumber());
				Hospitals hospitalRec = getHospitalById(claimRec.getIntimation().getHospital());
				structObj.put("hospitalName", hospitalRec.getName());
				structObj.put("admissionDate", (claimRec.getDataOfAdmission() != null)?claimRec.getDataOfAdmission().getTime() :"");
				structObj.put("hospitalAddress", hospitalRec.getAddress());
				structObj.put("reasonForAdmission", claimRec.getIntimation().getAdmissionReason());
				structObj.put("hospitalType", claimRec.getIntimation().getHospitalType().getValue());
				structObj.put("diagnosis", getDiagnosisInCSV(claimRec.getIntimation().getKey()));
				//structObj.put("claimStatus", claimRec.getStatus().getProcessValue());

				//Cashless Section
				List<Preauth> listOfCashlessRec = getCashlessDetails(claimRec.getIntimation().getKey());
				JSONArray claimArr = new JSONArray();
				JSONObject claimObj = null;
				if(listOfCashlessRec != null){
					for(Preauth rec :listOfCashlessRec){
						claimObj = new JSONObject();
						claimObj.put("cashlessType",(rec.getPreauthId() != null)?rec.getPreauthId() :"");
						claimObj.put("documentReceivedDate", (rec.getSfxMatchedQDate() != null)?rec.getSfxMatchedQDate().getTime() :"");
						claimObj.put("claimedAmount", (rec.getClaimedAmt() != null)?rec.getClaimedAmt() :0);
						claimObj.put("approvedAmount", (rec.getTotalApprovalAmount() != null)?rec.getTotalApprovalAmount() :0);
						claimArr.put(claimObj);
					}
				}else{
					claimObj = new JSONObject();
					claimObj.put("cashlessType","");
					claimObj.put("documentReceivedDate", "");
					claimObj.put("claimedAmount", 0);
					claimObj.put("approvedAmount", 0);
					claimArr.put(claimObj);
				}

				//Reimbursement Section
				List<DocAcknowledgement> listOfDocAckRec = getDocAckByClaim(claimRec.getKey());
				JSONArray reimArr = new JSONArray();
				JSONArray reimStsArr = null;
				JSONObject reimObj = null;
				if(listOfDocAckRec != null && !listOfDocAckRec.isEmpty()){
					for(DocAcknowledgement rec :listOfDocAckRec){
						reimObj = new JSONObject();
						String classification = "";
						if(rec.getHospitalisationFlag().equals("Y")){
							classification += "Hospitalization";
						}
						if(rec.getPreHospitalisationFlag().equals("Y")){
							classification += " Pre-Hospitalization";
						}
						if(rec.getPostHospitalisationFlag().equals("Y")){
							classification += " Post-Hospitalization";
						}
						classification = classification.replaceAll(" ", ", ");
						classification = classification.replaceFirst(", ", "");
						reimObj.put("classification", classification);
						reimObj.put("documentReceivedDate", (rec.getDocumentReceivedDate() != null)?rec.getDocumentReceivedDate().getTime() :"");
						Double claimedAmt = ((rec.getHospitalizationClaimedAmount() != null)?rec.getHospitalizationClaimedAmount():0)+
								((rec.getPreHospitalizationClaimedAmount() != null)?rec.getPreHospitalizationClaimedAmount():0)+
								((rec.getPostHospitalizationClaimedAmount() != null)?rec.getPostHospitalizationClaimedAmount():0);
						reimObj.put("claimedAmount", claimedAmt);
						List<Reimbursement> listOfReimbursementRec =  getReimbursementDetails(rec.getClaim().getKey());
						Double approvedAmt = new Double(0);
						for(Reimbursement recz :listOfReimbursementRec){
							approvedAmt += (recz.getFinancialApprovedAmount() != null)?recz.getFinancialApprovedAmount():0;
						}
						reimObj.put("approvedAmount", approvedAmt);
						reimObj.put("status", "Claim Processing");
						reimStsArr = new JSONArray();
						//Call agent_Portal_status proc
						reimObj.put("statusTrail",generateStatusTrailSection(reimStsArr,argSearch.getClaimNumber(),rec.getRodKey()));
						reimArr.put(reimObj);
					}
				}else{
					reimObj = new JSONObject();
					reimObj.put("classification","");
					reimObj.put("documentReceivedDate","");
					reimObj.put("claimedAmount", 0);
					reimObj.put("approvedAmount", 0);
					reimObj.put("status", "");
					reimStsArr = new JSONArray();
					reimObj.put("statusTrail", reimStsArr);
					reimArr.put(reimObj);
				}
				structObj.put("cashlessDetails", claimArr);
				structObj.put("reimbursementDetails", reimArr);
				dataArr.put(structObj);
			}else{
				
			}
			response.put("message","");
			response.put("data", dataArr);
			response.put("resCode", 200);
		}catch(Exception exp){
			System.out.println("Exception occured in perform Claim old Search "+exp.getMessage());
		}
		System.out.println(response.toString());
		return response.toString();
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
	

	 @SuppressWarnings("unchecked")
	 private  List<Preauth>  getCashlessDetails(Long key) {
		 List<Preauth> claimAmtDeatil = null;
		 Query findByLatestIntimationKey = entityManager.createNamedQuery("Preauth.findByLatestIntimationKey");
		 findByLatestIntimationKey.setParameter("intimationKey",key);
		 try{
			 claimAmtDeatil = findByLatestIntimationKey.getResultList();
		 }
		 catch(Exception e) {

		 }
		 return claimAmtDeatil;
	 }
	 
	 public  List<DocAcknowledgement> getDocAckByClaim(Long claimKey){
		 Query query = entityManager.createNamedQuery("DocAcknowledgement.getByClaimKey");
		 query = query.setParameter("claimKey", claimKey);
		 List<DocAcknowledgement> docAckList = query.getResultList();
		 if(null != docAckList && !docAckList.isEmpty()) {
			 entityManager.refresh(docAckList.get(0));
			 return docAckList;
		 }
		 return null;
	 }
	
	 public List<Reimbursement> getReimbursementDetails(Long claimKey) {
			Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<Reimbursement> reimbursementDetails = query.getResultList();
			if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
				return reimbursementDetails;				
			}
			return null;
		}
	 
	 public JSONArray generateStatusTrailSection(JSONArray argStatusArray, String argIntimationNo, Long argRodKey) throws JSONException{
		 Reimbursement reimRec = getReimbursementRec(argRodKey);
		 Connection connection = null;
		 CallableStatement cs = null;
		 ResultSet rs = null;
		 try{
			 if(reimRec != null){
				 connection = BPMClientContext.getConnection();
				 cs = connection.prepareCall("{call PRC_AGNT_PORTAL_STATUS(?, ?, ?)}");
				 cs.setString(1, argIntimationNo);
				 cs.setString(2, reimRec.getRodNumber());
				 cs.registerOutParameter(3, OracleTypes.CURSOR, "SYS_REFCURSOR");
				 cs.execute();

				 rs = (ResultSet) cs.getObject(3);
				 if (rs != null) {
					 Map<String, String> dataMap = null;
					 while (rs.next()) {
						 dataMap = new LinkedHashMap<String, String>();
						 dataMap.put("Register Claim", rs.getString("REGISTER_CLAIM"));
						 dataMap.put("Documents Submitted", rs.getString("DOCUMENT_SUBMITTED"));
						 dataMap.put("Documents Scrutiny", rs.getString("DOCUMENT_SCRUTINY"));
						 dataMap.put("Under Progress", rs.getString("UNDER_PROGRESS"));
						 dataMap.put("Settled", rs.getString("SETTLED"));
						 
					 }
					 if(dataMap != null && !dataMap.isEmpty()){
						 for (Map.Entry<String,String> entry : dataMap.entrySet()){
							 JSONObject obj = new JSONObject();
							 obj.put("stageName", entry.getKey());
							 obj.put("stageStatus", entry.getValue());
							 argStatusArray.put(obj);
						 }
					 }
				 }
			 }
		 }catch(Exception exp){
			 System.out.println("Exception occured in generateStatusTrailSection "+exp.getMessage());
		 } finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		 return argStatusArray;
	 }

	 public Reimbursement getReimbursementRec(Long rodKey) {
		 Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		 query.setParameter("primaryKey", rodKey);
		 List<Reimbursement> reimbursementDetails = query.getResultList();
		 if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
			 
			 return reimbursementDetails.get(0);				
		 }
		 return null;
	 }

	 public String createRod(GalaxyCreateRodRequest argObject) throws Exception{
		 String returnVal = "";
		 try{
			 returnVal = doCreateRod(argObject);
		 }catch(Exception ex){
			 ex.printStackTrace();
			 returnVal = ex.getMessage();
		 }
		 return returnVal;
	 }
	 
	 @SuppressWarnings("unchecked")
	 public String getClaimQuery(String argClaimNo) throws Exception{
		 JSONObject containerObj = new JSONObject();
		 JSONObject queryObj = null;
		 JSONArray queryArr = new JSONArray();
		 JSONArray dataArr = new JSONArray();

		 try{
			 Claim clmRec =  getClaimRecord(argClaimNo);
			 if(clmRec != null){
				 List<Long> rodKeyList = new ArrayList<Long>();
				 Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
				 query = query.setParameter("claimKey", clmRec.getKey());
				 List<Reimbursement> rodList = query.getResultList();
				 if(null != rodList && !rodList.isEmpty()) {
					 for(Reimbursement rec : rodList){
						 rodKeyList.add(rec.getKey());
					 }
				 }

				 for(Long key : rodKeyList){
					 Query reimbursementQuery = entityManager.createNamedQuery("ReimbursementQuery.findByReimbursement");
					 reimbursementQuery = reimbursementQuery.setParameter("reimbursementKey", key);
					 List<ReimbursementQuery> reimQueryList = reimbursementQuery.getResultList();
					 if (reimQueryList != null && !reimQueryList.isEmpty()) {
						 for(ReimbursementQuery rec : reimQueryList){
							 queryObj = new JSONObject();
							 queryObj.put("queryId", rec.getKey());
							 queryObj.put("queryRemarks", rec.getQueryRemarks());
							 queryObj.put("raisedDateTime", (rec.getApprovedRejectionDate() == null)?"":convertDateWithHMS(rec.getApprovedRejectionDate()));
							 queryObj.put("status", rec.getStatus().getProcessValue());
							 queryObj.put("queryResponse", "");
							 queryObj.put("responseDateTime", (rec.getQueryReplyDate() == null)?"":convertDateWithHMS(rec.getQueryReplyDate()));
							 queryArr.put(queryObj);
						 }
					 }
				 }
				 if(queryArr.length() > 0){
					 dataArr.put(queryArr);
				 }
				 containerObj.put("data", dataArr);
				 containerObj.put("errorYN", "N");
				 JSONArray errArray = new JSONArray();
				 containerObj.put("errors", errArray);
			 }else{
				 System.out.println("Claim Record Not Available :"+argClaimNo);
				 containerObj.put("data", dataArr);
				 containerObj.put("errorYN", "N");
				 JSONArray errArray = new JSONArray();
				 containerObj.put("errors", errArray);
			 }
		 }catch(Exception ex){
			 containerObj.put("data", dataArr);
			 containerObj.put("errorYN", "Y");
			 
			 JSONObject errObj = new JSONObject();
			 JSONArray errArray = new JSONArray();
			 errObj.put("error", ex.getMessage());
			 errArray.put(errObj);
			 containerObj.put("errors", errArray);
			 ex.printStackTrace();

		 }
		 return containerObj.toString();
	 }
	 
	 public String convertDateWithHMS(Date argDateValue) throws Exception {
		 String returnDate = null;
		 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 returnDate = formatter.format(argDateValue);
		 return returnDate;
	 }

	 public String doCreateRod(GalaxyCreateRodRequest argObject) throws Exception{
		 String requestStatus = "";
		 List<Object> listOfObjects = new ArrayList<Object>();
		 List<Object> ObjectToBeRemoved = new ArrayList<Object>();
		 DocAcknowledgement ackRecord = null;
		 Claim claim = getClaimRecord(argObject.getClaimNumber());
		 if(claim == null){
			 throw new Exception("Claim not found for the intimation "+argObject.getClaimNumber());
		 }
		 try{
			 // Create Ack Insert in IMS_CLS_DOC_ACKNOWLEDGEMENT table
			 ackRecord = new DocAcknowledgement();
			 ackRecord.setClaim(claim);		
			 ackRecord.setAcknowledgeNumber(generateAcknowledgeNo(claim));
			 ackRecord.setDocumentReceivedFromId(getKeyForValue(ReferenceTable.ACK_DOC_RECEIVED_FROM,"Insured"));
			 ackRecord.setDocumentReceivedDate(new Date());
			 ackRecord.setModeOfReceiptId(getKeyForValue(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT,"Online"));
			 ackRecord.setReconsiderationRequest("N");
			 ackRecord.setInsuredContactNumber("");// to be discussed
			 ackRecord.setInsuredEmailId("");// to be discussed

			 ackRecord.setHospitalisationFlag("Y");
			 ackRecord.setPreHospitalisationFlag("N");
			 ackRecord.setPostHospitalisationFlag("N");
			 ackRecord.setPartialHospitalisationFlag("N");
			 ackRecord.setLumpsumAmountFlag("N");
			 ackRecord.setHospitalCashFlag("N");
			 ackRecord.setPatientCareFlag("N");
			 ackRecord.setAdditionalRemarks("Document Received From Online");
			 ackRecord.setActiveStatus(1L);

			 Stage stage = new Stage();
			 stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 ackRecord.setStage(stage);

			 Status status = new Status();
			 status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			 ackRecord.setStatus(status);

			 ackRecord.setCreatedBy("Customer");
			 ackRecord.setCreatedDate(new Date());
			 ackRecord.setHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setPreHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setPostHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setHospitalizationRepeatFlag("N");

			 Status claimStatus = new Status();
			 claimStatus.setKey(ReferenceTable.PROCESS_PRE_MEDICAL);
			 claim.setStatus(claimStatus);

			 listOfObjects.add(ackRecord);
			 listOfObjects.add(claim);

			 boolean docPersistFlag = uploadRODDocuments(argObject, ackRecord, claim, listOfObjects);

			 if(docPersistFlag) {
				 for(Object obj: listOfObjects){
					 ObjectToBeRemoved.add(obj);
					 entityManager.persist(obj);
				 }

				 Double totalClaimedAmt = ackRecord.getHospitalizationClaimedAmount() + ackRecord.getPreHospitalizationClaimedAmount() + ackRecord.getPostHospitalizationClaimedAmount();
				 DBCalculationService dbCalculationService = new DBCalculationService();
				 if (null != ackRecord.getClaim()
						 && null != ackRecord.getClaim().getIntimation()
						 && null != ackRecord.getClaim().getIntimation().getInsured()
						 && null != ackRecord.getClaim().getIntimation().getInsured()
						 .getInsuredId()
						 && null != ackRecord.getClaim().getIntimation().getPolicy().getKey()) {
					 Double insuredSumInsured = dbCalculationService
							 .getInsuredSumInsured(ackRecord.getClaim().getIntimation()
									 .getInsured().getInsuredId().toString(), ackRecord
									 .getClaim().getIntimation().getPolicy()
									 .getKey(),ackRecord.getClaim().getIntimation()
									 .getInsured().getLopFlag());
					 Double balSI = dbCalculationService
							 .getBalanceSI(
									 ackRecord.getClaim().getIntimation().getPolicy()
									 .getKey(),
									 ackRecord.getClaim().getIntimation().getInsured()
									 .getKey(), ackRecord.getClaim().getKey(),
									 insuredSumInsured,
									 ackRecord.getClaim().getIntimation().getKey())
									 .get(SHAConstants.TOTAL_BALANCE_SI);
					 Double amt = Math.min(balSI, totalClaimedAmt);

					 claim.setCurrentProvisionAmount(amt);
					 claim.setClaimedAmount(amt);
					 claim.setProvisionAmount(amt);

				 }
				 submitTaskToDB(ackRecord, false);
				 System.out.println("ROD Submitted Successfully" + argObject.getClaimNumber());

				 JSONObject errorMsg = new JSONObject();
				 errorMsg.put("resultMsg", "ROD Submitted Successfully");
				 errorMsg.put("errorYN", "N");
				 JSONArray errArray = new JSONArray();
				 errorMsg.put("errors", errArray);
				 requestStatus = errorMsg.toString();
			 }else{
				 System.out.println("Some of the file(s) not uploaded in the DMS. ROD" + argObject.getClaimNumber());
				 
				 JSONObject errorMsg = new JSONObject();
				 errorMsg.put("resultMsg", "Error Occurred");
				 errorMsg.put("errorYN", "Y");
				 JSONObject errObj = new JSONObject();
				 JSONArray errArray = new JSONArray();
				 errObj.put("error", "ROD Submission Failed");
				 errArray.put(errObj);
				 errorMsg.put("errors", errArray);
				 requestStatus = errorMsg.toString();
			 }
		 }catch(Exception exp){
			 exp. printStackTrace();
			 log.info("Exception Occurred in ROD creation :"+ exp.getMessage());
			 throw new Exception(exp);
		 }
		 return requestStatus;
	 }
	 
	 public String doSubmitQueryresponse(GalaxyQueryResponseRequest argReponseObj) throws Exception{
		 String returnVal = "";
		 try{
			 returnVal = submitQueryResponse(argReponseObj);
		 }catch(Exception ex){
			 ex.printStackTrace();
			 returnVal = ex.getMessage();
		 }
		 return returnVal;
	 }

	 public String submitQueryResponse(GalaxyQueryResponseRequest argObject) throws Exception{

		 String requestStatus = "";
		 List<Object> listOfObjects = new ArrayList<Object>();
		 List<Object> ObjectToBeRemoved = new ArrayList<Object>();
		 DocAcknowledgement ackRecord = null;
		 ReimbursementQuery reimQry = null;
		 Claim claim = getClaimRecord(argObject.getClaimNumber());
		 if(claim == null){
			 throw new Exception("Claim not found for the intimation "+argObject.getClaimNumber());
		 }
		 try{

			 // Create Ack Insert in IMS_CLS_DOC_ACKNOWLEDGEMENT table
			 ackRecord = new DocAcknowledgement();
			 ackRecord.setClaim(claim);		
			 ackRecord.setAcknowledgeNumber(generateAcknowledgeNo(claim));
			 ackRecord.setDocumentReceivedFromId(getKeyForValue(ReferenceTable.ACK_DOC_RECEIVED_FROM,"Insured"));
			 ackRecord.setDocumentReceivedDate(new Date());
			 ackRecord.setModeOfReceiptId(getKeyForValue(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT,"Online"));
			 ackRecord.setReconsiderationRequest("N");
			 ackRecord.setInsuredContactNumber("");// to be discussed
			 ackRecord.setInsuredEmailId("");// to be discussed

			 ackRecord.setHospitalisationFlag("Y");
			 ackRecord.setPreHospitalisationFlag("N");
			 ackRecord.setPostHospitalisationFlag("N");
			 ackRecord.setPartialHospitalisationFlag("N");
			 ackRecord.setLumpsumAmountFlag("N");
			 ackRecord.setHospitalCashFlag("N");
			 ackRecord.setPatientCareFlag("N");
			 ackRecord.setAdditionalRemarks("Document Received From Online");
			 ackRecord.setActiveStatus(1L);

			 Stage stage = new Stage();
			 stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 ackRecord.setStage(stage);

			 Status status = new Status();
			 status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
			 ackRecord.setStatus(status);

			 ackRecord.setCreatedBy("Customer");
			 ackRecord.setCreatedDate(new Date());
			 ackRecord.setHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setPreHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setPostHospitalizationClaimedAmount(Double.parseDouble("0"));
			 ackRecord.setHospitalizationRepeatFlag("N");

			 //
			 reimQry = getReimbursementQuery(argObject.getQueryId());
			 ackRecord.setRodKey(reimQry.getReimbursement().getKey());
			 listOfObjects.add(ackRecord);

			 reimQry.setQueryReply("Y");
			 reimQry.setQueryRemarks(argObject.getQueryResponse());
			 reimQry.setDocAcknowledgement(ackRecord);
			 listOfObjects.add(reimQry);

			 boolean docPersistFlag = uploadRODDocuments(argObject, ackRecord, claim, listOfObjects);

			 if(docPersistFlag) {
				 for(Object obj: listOfObjects){
					 ObjectToBeRemoved.add(obj);
					 entityManager.persist(obj);
				 }
				 Double totalClaimedAmt = ackRecord.getHospitalizationClaimedAmount() + ackRecord.getPreHospitalizationClaimedAmount() + ackRecord.getPostHospitalizationClaimedAmount();
				 DBCalculationService dbCalculationService = new DBCalculationService();
				 if (null != ackRecord.getClaim()
						 && null != ackRecord.getClaim().getIntimation()
						 && null != ackRecord.getClaim().getIntimation().getInsured()
						 && null != ackRecord.getClaim().getIntimation().getInsured()
						 .getInsuredId()
						 && null != ackRecord.getClaim().getIntimation().getPolicy().getKey()) {
					 Double insuredSumInsured = dbCalculationService
							 .getInsuredSumInsured(ackRecord.getClaim().getIntimation()
									 .getInsured().getInsuredId().toString(), ackRecord
									 .getClaim().getIntimation().getPolicy()
									 .getKey(),ackRecord.getClaim().getIntimation()
									 .getInsured().getLopFlag());
					 Double balSI = dbCalculationService
							 .getBalanceSI(
									 ackRecord.getClaim().getIntimation().getPolicy()
									 .getKey(),
									 ackRecord.getClaim().getIntimation().getInsured()
									 .getKey(), ackRecord.getClaim().getKey(),
									 insuredSumInsured,
									 ackRecord.getClaim().getIntimation().getKey())
									 .get(SHAConstants.TOTAL_BALANCE_SI);
					 Double amt = Math.min(balSI, totalClaimedAmt);

					 claim.setCurrentProvisionAmount(amt);
					 claim.setClaimedAmount(amt);
					 claim.setProvisionAmount(amt);

				 }
				 submitTaskToDB(ackRecord, true);
				 System.out.println("Query Response Successfully Submitted For" + argObject.getClaimNumber());

				 JSONObject errorMsg = new JSONObject();
				 errorMsg.put("resultMsg", "Query Response Successfully Submitted");
				 errorMsg.put("errorYN", "N");
				 JSONArray errArray = new JSONArray();
				 errorMsg.put("errors", errArray);
				 requestStatus = errorMsg.toString();
			 }else{
				 System.out.println("Some of the file(s) not uploaded in the DMS." + argObject.getClaimNumber());
				 
				 JSONObject errorMsg = new JSONObject();
				 errorMsg.put("resultMsg", "Error Occurred");
				 errorMsg.put("errorYN", "Y");
				 JSONObject errObj = new JSONObject();
				 JSONArray errArray = new JSONArray();
				 errObj.put("error", "Query Response Submission Failed");
				 errArray.put(errObj);
				 errorMsg.put("errors", errArray);
				 requestStatus = errorMsg.toString();
			 }
		 }catch(Exception exp){
			 exp. printStackTrace();
			 log.info("Exception Occurred in Query Response submission :"+ exp.getMessage());
			 throw new Exception(exp);
		 }
		 return requestStatus;
	 }

	 @SuppressWarnings("unchecked")
	 public ReimbursementQuery getReimbursementQuery(String argQId){
		 ReimbursementQuery latestQuery = null;
		 Query query2 = entityManager.createNamedQuery("ReimbursementQuery.findByKey");
		 query2 = query2.setParameter("primaryKey", Long.valueOf(argQId));
		 List<ReimbursementQuery> queryList = query2.getResultList();
		 if(!queryList.isEmpty()){
			 latestQuery = queryList.get(0);
		 }
		 return latestQuery;
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
				 //					uploadStatus = "Some of the file(s) not uploaded in the DMS.";
			 }
		 }
		 return tempStatus;
	 }
	 
	 /**	
	 * 	
	 * @param acknowledgementKey	
	 * @return	
	 */	
	@SuppressWarnings("unchecked")	
	public Reimbursement getReimbursementRecord(Long acknowledgementKey) {	
		Query query = entityManager.createNamedQuery("Reimbursement.findByAcknowledgement");	
		query.setParameter("docAcknowledgmentKey", acknowledgementKey);	
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query.getResultList();	
		if (null != reimbursementList && !reimbursementList.isEmpty()) {	
			entityManager.refresh(reimbursementList.get(0));	
			return reimbursementList.get(0);	
		}	
		return null;	
	}
	
	
	/**
	 * 
	 * @param acknowledgementKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgmentByIntimation(String intimationNumber) {
		Claim clmObj = null;
		Query iquery = entityManager.createNamedQuery("Claim.findByIntimationNumber");
		iquery = iquery.setParameter("intimationNumber", intimationNumber);
		List<Claim> clmList = (List<Claim>) iquery.getResultList();
		if (null != clmList && !clmList.isEmpty()) {
			clmObj = clmList.get(0);
		}
		if(clmObj != null){
			Query query = entityManager.createNamedQuery("DocAcknowledgement.getByClaimKeyWithRodNull");
			query.setParameter("claimkey", clmObj.getKey());
			query.setParameter("rmode", "Online");
			query.setParameter("mcode","RECMODE");
			List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query.getResultList();
			if (null != reimbursementList && !reimbursementList.isEmpty()) {
				return reimbursementList.get(0);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgmentByROD(String rodNumber) {
		Reimbursement reimObj = null;
		Query iquery = entityManager.createNamedQuery("Reimbursement.findRodByNumber");
		iquery = iquery.setParameter("rodNumber", rodNumber);
		List<Reimbursement> clmList = (List<Reimbursement>) iquery.getResultList();
		if (null != clmList && !clmList.isEmpty()) {
			reimObj = clmList.get(0);
		}
		if(reimObj != null){
			Query query = entityManager.createNamedQuery("DocAcknowledgement.getAckByRodKey");
			query.setParameter("rodKey", reimObj.getKey());
			query.setParameter("rmode", "Online");
			query.setParameter("mcode","RECMODE");
			List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query.getResultList();
			if (null != reimbursementList && !reimbursementList.isEmpty()) {
				return reimbursementList.get(0);
			}
		}
		return null;
	}
	 
}
