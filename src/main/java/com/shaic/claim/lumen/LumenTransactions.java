package com.shaic.claim.lumen;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.lumen.components.LetterDetailsDTO;
import com.shaic.claim.lumen.components.LumenDocumentGenerator;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.policyupload.PolicyFileData;
import com.shaic.claim.lumen.querytomis.DocumentAckTableDTO;
import com.shaic.claim.lumen.upload.DocumentTableDTO;
import com.shaic.claim.lumen.upload.FileData;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.LumenDetails;
import com.shaic.domain.LumenLetterDetails;
import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenQueryDetails;
import com.shaic.domain.LumenQueryDocument;
import com.shaic.domain.LumenRequest;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class LumenTransactions {
	
	private String TO_LEVEL_II = "Process Lumen Request (Level - II)";
	private String TO_LEVEL_I = "Process Lumen Request (Level - I)";
	private String TO_COORDINATOR = "Co-ordinator";
	private String TO_INITIATOR = "Initiator";

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private MasterService masterService;

	@Resource
	private UserTransaction utx;
	
	private String userActionName;
	
	private String selectedToValue;
	
	private Long workFlowKey;
	private String outcome;
	
	public Long getWorkFlowKey() {
		return workFlowKey;
	}

	public void setWorkFlowKey(Long workFlowKey) {
		this.workFlowKey = workFlowKey;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getUserActionName() {
		return userActionName;
	}

	public void setUserActionName(String userActionName) {
		this.userActionName = userActionName;
	}

	public String getSelectedToValue() {
		return selectedToValue;
	}

	public void setSelectedToValue(String selectedToValue) {
		this.selectedToValue = selectedToValue;
	}

	public LumenRequest persistLumenObj(LumenSearchResultTableDTO dtoBean) throws Exception{
		LumenRequest newLumenReq = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			utx.begin();
			newLumenReq = new LumenRequest();
			newLumenReq.setClaim(dtoBean.getClaim());
			newLumenReq.setLumenRemarks(dtoBean.getLumenRemarks());
			newLumenReq.setLumenInitiatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setLumenType(dtoBean.getLumenType().getId());
//			if(dtoBean.getLumenType() != null && dtoBean.getLumenType().getValue().equals("Hospital Errors")){
//				newLumenReq.setHospitalErrorType(dtoBean.getLumenErrorType().getId());
//			}
			newLumenReq.setActiveStatus(1L);
			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS);
			newLumenReq.setStage(lumenStg);
			newLumenReq.setRequestedStageId(lumenStg);
			Status lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_QUERY);
			newLumenReq.setStatus(lumenStatus);		
			newLumenReq.setProcessedBy(dtoBean.getEmpName());
			newLumenReq.setCreatedBy(loginUserId);
			newLumenReq.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setIntimationNumber(dtoBean.getIntimationNumber());
			newLumenReq.setPolicyNumber(dtoBean.getPolicyNumber());
			entityManager.persist(newLumenReq);

			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(newLumenReq);
					entityManager.persist(rec);
				}
			}
			submitDBProcedureForLumenProcess(newLumenReq, "Initiate Lumen");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Insert Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newLumenReq;
	}
	
	public LumenRequest persistPolicyLumenObj(LumenPolicySearchResultTableDTO dtoBean) throws Exception{
		LumenRequest newLumenReq = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			utx.begin();
			newLumenReq = new LumenRequest();
			newLumenReq.setLumenRemarks(dtoBean.getLumenRemarks());
			newLumenReq.setLumenInitiatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setLumenType(dtoBean.getLumenType().getId());
//			if(dtoBean.getLumenType() != null && dtoBean.getLumenType().getValue().equals("Hospital Errors")){
//				newLumenReq.setHospitalErrorType(dtoBean.getLumenErrorType().getId());
//			}
			newLumenReq.setActiveStatus(1L);
			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS);
			newLumenReq.setStage(lumenStg);
			newLumenReq.setRequestedStageId(lumenStg);
			Status lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_QUERY);
			newLumenReq.setStatus(lumenStatus);		
			newLumenReq.setProcessedBy(dtoBean.getEmpName());
			newLumenReq.setInsuredName(dtoBean.getSelectedInsuredName());
			newLumenReq.setCreatedBy(loginUserId);
			newLumenReq.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setPolicyNumber(dtoBean.getPolicyNumber());
			entityManager.persist(newLumenReq);

			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(newLumenReq);
					entityManager.persist(rec);
				}
			}
			submitDBProcedureForLumenProcess(newLumenReq, "Initiate Lumen");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Insert Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newLumenReq;
	}
	
	public LumenRequest persistPopupLumenObj(LumenSearchResultTableDTO dtoBean, String argScreenName) throws Exception{
		LumenRequest newLumenReq = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			utx.begin();
			newLumenReq = new LumenRequest();
			newLumenReq.setClaim(dtoBean.getClaim());
			newLumenReq.setLumenRemarks(dtoBean.getLumenRemarks());
			newLumenReq.setLumenInitiatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setLumenType(dtoBean.getLumenType().getId());
//			if(dtoBean.getLumenType() != null && dtoBean.getLumenType().getValue().equals("Hospital Errors")){
//				newLumenReq.setHospitalErrorType(dtoBean.getLumenErrorType().getId());
//			}
			newLumenReq.setActiveStatus(1L);
			Stage lumenStg = null;
			
//			public static Long PROCESS_PRE_MEDICAL = Long.valueOf(11L);
//			public static Long PRE_MEDICAL_PROCESSING_ENHANCEMENT = Long.valueOf(12L);
//			public static Long PROCESS_PREAUTH = Long.valueOf(13L);
//			public static Long PROCESS_ENHANCEMENT_TYPE = Long.valueOf(14L);
			
//			11 - First Level Process(Pre Auth)
//			12 - First Level Process(Enhancement)
//			13 - Pre Auth Process
//			14 - Enhancement Process
			
			if(argScreenName.equals("FLP-Pre Auth")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_PRE_MEDICAL);
			}else if(argScreenName.equals("FLP-Enhancement")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PRE_MEDICAL_PROCESSING_ENHANCEMENT);
			}else if(argScreenName.equals("Process-Pre Auth")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_PREAUTH);
			}else if(argScreenName.equals("Process-Enhancement")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_ENHANCEMENT_TYPE);
			}else if(argScreenName.equals("PCR ZMR")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
			}else if(argScreenName.equals("PCR")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
			}else if(argScreenName.equals("Billing")){
				lumenStg = masterService.getStageBykey(ReferenceTable.BILLING_STAGE);
			}else if(argScreenName.equals("Financials")){
				lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
			}else if(argScreenName.equals("PED") || argScreenName.equals("Initiate PED") || argScreenName.equals("PED TL")){ // PED-Approver Only PED-Processor screen removed.
				lumenStg = masterService.getStageBykey(ReferenceTable.PED_ENDORSEMENT_STAGE);
			}else if(argScreenName.equals("SSA Reimbursement")){
				if(dtoBean.getSpecialistStage().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
					lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
				}else if(dtoBean.getSpecialistStage().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
					lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
				}				
			}else if(argScreenName.equals("SSA Cashless")){				
				if(dtoBean.getSpecialistStage().equals(ReferenceTable.PROCESS_PREAUTH)){
					lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_PREAUTH);
				}else if(dtoBean.getSpecialistStage().equals(ReferenceTable.PROCESS_ENHANCEMENT_TYPE)){
					lumenStg = masterService.getStageBykey(ReferenceTable.PROCESS_ENHANCEMENT_TYPE);
				}
			}else if(argScreenName.equals("Process Investigation")){
				lumenStg = masterService.getStageBykey(ReferenceTable.INVESTIGATION_STAGE);
			}			
		
			newLumenReq.setStage(lumenStg);
			newLumenReq.setRequestedStageId(lumenStg);
			Status lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_QUERY);
			newLumenReq.setStatus(lumenStatus);		
			newLumenReq.setProcessedBy(dtoBean.getEmpName());
			newLumenReq.setCreatedBy(loginUserId);
			newLumenReq.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newLumenReq.setIntimationNumber(dtoBean.getIntimationNumber());
			newLumenReq.setPolicyNumber(dtoBean.getPolicyNumber());
			entityManager.persist(newLumenReq);

			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(newLumenReq);
					entityManager.persist(rec);
				}
			}
			submitDBProcedureForLumenProcess(newLumenReq, "Initiate Lumen");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Insert Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newLumenReq;
	}

	public void submitDBProcedureForLumenProcess(LumenRequest lumenObj, String argScreeName){
		String userActionName = getUserActionName();
		String toValue = getSelectedToValue();
		Object[] objArray;
		objArray = SHAUtils.getArrayListForLumenSubmit(lumenObj);
		if(argScreeName.equals("Initiate Lumen")){
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "LINTLRSM";
			objArray[SHAConstants.LMN_INDEX_PROCESS] = "Create"; // Action
			objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Initiate"; // Branch
		}
		if(argScreeName.equals("Process Level I")){
			objArray[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = getWorkFlowKey();
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "LREGL1SM";
			if(userActionName.equals("Approve")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Approve";
				if(toValue.equals("Co-ordinator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Coordinator";
				}else if(toValue.equals("Process Lumen Request (Level - II)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2";
				}
			}else if(userActionName.equals("Reject")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reject";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Reject";
			}else if(userActionName.equals("Reply")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reply";
				if(toValue.equals("Co-ordinator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Coordinator";
				}else if(toValue.equals("Process Lumen Request (Level - II)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2";
				}
			}else if(userActionName.equals("Query")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Query";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Initiator";
			}else if(userActionName.equals("Close")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Close";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Close";
			}else if(userActionName.equals("Refer to MIS")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "MIS";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "MIS";
			}

		}

		if(argScreeName.equals("Initiator Query Cases")){
			objArray[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = getWorkFlowKey();
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "LQRYLQSM";
			if(userActionName.equals("Reply")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reply";
				if(toValue.equals("Co-ordinator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Coordinator";
				}else if(toValue.equals("Process Lumen Request (Level - II)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2";
				}else if(toValue.equals("Process Lumen Request (Level - I)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 1";
				}
			}
		}
		
		if(argScreeName.equals("Process Coordinator")){
			objArray[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = getWorkFlowKey();
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "LCORLCSM";
			if(userActionName.equals("Approve")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Approve";
				if(toValue.equals("Process Lumen Request (Level - II)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2";
				}
			}else if(userActionName.equals("Reply")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reply";
				if(toValue.equals("Process Lumen Request (Level - I)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 1";
				}else if(toValue.equals("Process Lumen Request (Level - II)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2";
				}
			}else if(userActionName.equals("Query")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Query";
				if(toValue.equals("Process Lumen Request (Level - I)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 1";
				}else if(toValue.equals("Initiator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Initiator";
				}
			}else if(userActionName.equals("Refer to MIS")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "MIS";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "MIS";
			}
		}
		
		if(argScreeName.equals("Process MIS Reply")){
			objArray[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = getWorkFlowKey();
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "LMISLMSM";
			objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reply"; //Action
			if(userActionName.equals("MIS Reply")){
				if(toValue.equals("Level 2")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 2"; //Branch
				}else if(toValue.equals("Level 1")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 1";
				}else if(toValue.equals("Coordinator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Coordinator";
				}
			}
		}
		
		if(argScreeName.equals("Process Level II")){
			objArray[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = getWorkFlowKey();
			objArray[SHAConstants.LMN_INDEX_OUT_COME] = "L2APLASM";
			if(userActionName.equals("Approve")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Approve";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Approve";
			}else if(userActionName.equals("Reject")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Reject";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Reject";
			}else if(userActionName.equals("Query")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Query";
				if(toValue.equals("Process Lumen Request (Level - I)")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Level 1";
				}else if(toValue.equals("Initiator")){
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Initiator";
				}else if(toValue.equals("Co-ordinator")){//Co-ordinator
					objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Coordinator";
				}
			}else if(userActionName.equals("Close")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "Close";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "Close";
			}else if(userActionName.equals("Refer to MIS")){
				objArray[SHAConstants.LMN_INDEX_PROCESS] = "MIS";
				objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "MIS";
			}
		}		
		
		//Setting the Lumen Initiated employee name on every time lumen is getting processed. 
		TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(lumenObj.getCreatedBy().toLowerCase());
		objArray[SHAConstants.LMN_INDEX_ROD_REFER_BY] = employeeObj.getEmpFirstName();
		
		System.out.println("WF_KEY :"+getWorkFlowKey());
		System.out.println("OUTCOME :"+objArray[SHAConstants.LMN_INDEX_OUT_COME]);
		System.out.println("ACTION :"+objArray[SHAConstants.LMN_INDEX_PROCESS]);
		System.out.println("BRANCH :"+objArray[SHAConstants.LMN_INDEX_PROCESS_TYPE]);
		
		Object[] resultArray = new Object[1];
		resultArray[0] = objArray;
		DBCalculationService dbCalService = new DBCalculationService();
		dbCalService.lumenSubmitTaskProcedure(resultArray);

	}

	public DocumentDetails persistDocumentDetailObj(LumenSearchResultTableDTO dtoBean, FileData fileData) throws Exception{
		DocumentDetails newDocDetails = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			newDocDetails = new DocumentDetails();
			if(dtoBean.getClaim() != null){
				newDocDetails.setIntimationNumber(dtoBean.getClaim().getIntimation().getIntimationId());
				newDocDetails.setClaimNumber(dtoBean.getClaim().getClaimId());
			}
			newDocDetails.setFileName(fileData.getFileName());
			newDocDetails.setDocumentType("Lumen");
			newDocDetails.setDocumentToken(Long.parseLong(fileData.getToken()));
			newDocDetails.setDocumentSource("Lumen");
			newDocDetails.setDocSubmittedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setCreatedBy(loginUserId);
			newDocDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setDeletedFlag("N");
			newDocDetails.setSfFileName(UUID.randomUUID().toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Doc Insert Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newDocDetails;
	}
	
	public DocumentDetails persistDocumentDetailObj(LumenPolicySearchResultTableDTO dtoBean, PolicyFileData fileData) throws Exception{
		DocumentDetails newDocDetails = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			newDocDetails = new DocumentDetails();
			newDocDetails.setFileName(fileData.getFileName());
			newDocDetails.setDocumentType("Lumen");
			newDocDetails.setDocumentToken(Long.parseLong(fileData.getToken()));
			newDocDetails.setDocumentSource("Lumen");
			newDocDetails.setDocSubmittedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setCreatedBy(loginUserId);
			newDocDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setDeletedFlag("N");
			newDocDetails.setSfFileName(UUID.randomUUID().toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Doc Insert Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newDocDetails;
	}

	public DocumentDetails persistDocumentDetailObj(LumenRequestDTO dtoBean, FileData fileData) throws Exception{
		DocumentDetails newDocDetails = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		try {
			newDocDetails = new DocumentDetails();
			if(dtoBean.getClaim() != null){
				newDocDetails.setIntimationNumber(dtoBean.getClaim().getIntimation().getIntimationId());
				newDocDetails.setClaimNumber(dtoBean.getClaim().getClaimId());
			}
			newDocDetails.setFileName(fileData.getFileName());
			newDocDetails.setDocumentType("Lumen");
			newDocDetails.setDocumentToken(Long.parseLong(fileData.getToken()));
			newDocDetails.setDocumentSource("Lumen");
			newDocDetails.setDocSubmittedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setCreatedBy(loginUserId);
			newDocDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
			newDocDetails.setDeletedFlag("N");
			newDocDetails.setSfFileName(UUID.randomUUID().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return newDocDetails;
	}

	public DocumentDetails deleteDocumentDetailObj(DocumentTableDTO dtoBean) throws Exception{
		DocumentDetails newDocDetails = null;
		try {
			utx.begin();
			newDocDetails = getDocumentObj(dtoBean);
			newDocDetails.setDeletedFlag("Y");
			entityManager.merge(newDocDetails);
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Doc Update Transaction rollbacked.....");
			throw new Exception(e);
		}
		return newDocDetails;
	}

	@SuppressWarnings("unchecked")
	private DocumentDetails getDocumentObj(DocumentTableDTO dtoBean){
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoWithFileNameAndFileType");
		query = query.setParameter("fileName", dtoBean.getFileName());
		query = query.setParameter("intimationNumber", dtoBean.getIntimationNum());
		query = query.setParameter("documentType", dtoBean.getFileType());
		List<DocumentDetails> listofDoc = query.getResultList();
		listofDoc = query.getResultList();
		if(null != listofDoc && !listofDoc.isEmpty())
			return listofDoc.get(0);
		else
			return null;
	}

	public void mergeLevelOneChanges(LumenRequestDTO dtoBean) throws Exception{
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		LumenRequest oldLumenReq = null;
		LumenDetails newLumenDetails = null;
		//List<LumenDetails> listOfDetailsObj = new ArrayList<LumenDetails>();
		LumenQuery newLumenQuery = null;
		LumenQueryDetails newLumenQueryDetail = null;
		//List<LumenQuery> listOfQueryObj = new ArrayList<LumenQuery>();
		setWorkFlowKey(dtoBean.getWorkFlowKey());
		setOutcome(dtoBean.getOutcome());
		//System.out.println("Work flow Key"+dtoBean.getWorkFlowKey());
		//System.out.println("OutCome"+dtoBean.getOutcome());
		try {
			utx.begin();

			oldLumenReq = getLumenRequestByKey(dtoBean.getLumenRequestKey());
			oldLumenReq.setLumenType(dtoBean.getLumenTypeId());
//			oldLumenReq.setHospitalErrorType(dtoBean.getErrorTypeId());
			oldLumenReq.setLumenRemarks(dtoBean.getComments());
			oldLumenReq.setRemarks(dtoBean.getRemarks());

			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS_LEVEL_I);
			oldLumenReq.setStage(lumenStg);

			Status lumenStatus = null;
			if(dtoBean.getUserActionName().equals("Close")){
				oldLumenReq.setLevel1CloseRemarks(dtoBean.getLevel1CloseRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_CLOSE);
			}

			if(dtoBean.getUserActionName().equals("Approve")){
				if(dtoBean.getLevel1ApproveTo().equals(TO_LEVEL_II)){
					oldLumenReq.setSendTo("Level 2");
				}else if(dtoBean.getLevel1ApproveTo().equals(TO_COORDINATOR)){
					oldLumenReq.setSendTo("Coordinator");
				}
				setSelectedToValue(dtoBean.getLevel1ApproveTo());
				oldLumenReq.setLevel1ApprovalRemarks(dtoBean.getLevel1ApprovalRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_APPROVE);
			}

			if(dtoBean.getUserActionName().equals("Reject")){
				oldLumenReq.setLevel1RejectRemarks(dtoBean.getLevel1RejectRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_REJECT);
			}

			if(dtoBean.getUserActionName().equals("Reply")){
				if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_II)){
					oldLumenReq.setSendTo("Level 2");
				}else if(dtoBean.getLevel1ReplyTo().equals(TO_COORDINATOR)){
					oldLumenReq.setSendTo("Coordinator");
				}// As per satish comment there is no need to handle Initiator case for reply.
				setSelectedToValue(dtoBean.getLevel1ReplyTo());
				oldLumenReq.setLevel1ReplyRemarks(dtoBean.getLevel1ReplyRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_REPLY);
			}
			
			if(dtoBean.getUserActionName().equals("Query")){
				oldLumenReq.setSendTo("Initiator");
				setSelectedToValue(dtoBean.getLevel1QueryTo());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_QUERY);
			}
			
			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				oldLumenReq.setSendTo("MIS");
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_I_MIS);
			}

			oldLumenReq.setModifiedBy(loginUserId);
			oldLumenReq.setModifiedDate((new Timestamp(System.currentTimeMillis())));
			oldLumenReq.setStatus(lumenStatus);
			oldLumenReq.setProcessedBy(dtoBean.getEmpName());
			entityManager.merge(oldLumenReq);

			// persist participant and lapse list if available and Only when approving to next level.
			//if(dtoBean.getUserActionName().equals("Approve")){
				if(dtoBean.getListOfParticipants().size() > 0){
					for (String name : dtoBean.getListOfParticipants()){
						LumenDetails tempObj = checkParticipantAvailability(name, oldLumenReq.getKey());
						if(tempObj == null){
							newLumenDetails = new LumenDetails();
							newLumenDetails.setLumenRequest(oldLumenReq);
							newLumenDetails.setParticipantType("Participant");
							newLumenDetails.setEmployeeName(name);
							newLumenDetails.setCreatedBy(loginUserId);
							newLumenDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
							newLumenDetails.setStage(lumenStg);
							newLumenDetails.setStatus(lumenStatus);
							entityManager.persist(newLumenDetails);
						}else{
							tempObj.setParticipantType("Participant");
							tempObj.setModifiedBy(loginUserId);
							tempObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
							tempObj.setStage(lumenStg);
							tempObj.setStatus(lumenStatus);
							entityManager.merge(tempObj);
						}
					}
				}

				if(dtoBean.getListOfLapse().size() > 0){
					for (String name : dtoBean.getListOfLapse()){
						LumenDetails tempObj = checkParticipantAvailability(name, oldLumenReq.getKey());
						if(tempObj == null){
							newLumenDetails = new LumenDetails();
							newLumenDetails.setLumenRequest(oldLumenReq);
							newLumenDetails.setParticipantType("Lapse");
							newLumenDetails.setEmployeeName(name);
							newLumenDetails.setCreatedBy(loginUserId);
							newLumenDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
							newLumenDetails.setStage(lumenStg);
							newLumenDetails.setStatus(lumenStatus);
							entityManager.persist(newLumenDetails);
						}else{
							tempObj.setParticipantType("Lapse");
							tempObj.setModifiedBy(loginUserId);
							tempObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
							tempObj.setStage(lumenStg);
							tempObj.setStatus(lumenStatus);
							entityManager.merge(tempObj);
						}
					}
				}
			//}

			if(dtoBean.getUserActionName().equals("Query")){
				
				//When a new query is raised an insert should happen in Query and QueryDetail table.
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("Query");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Level 1");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);
				entityManager.persist(newLumenQuery);
				
				newLumenQueryDetail = new LumenQueryDetails();
				newLumenQueryDetail.setLumenRequest(oldLumenReq);
				newLumenQueryDetail.setLumenQuery(newLumenQuery);
				newLumenQueryDetail.setQueryRemarks(dtoBean.getLevel1QueryRemarks());
				newLumenQueryDetail.setCreatedBy(loginUserId);
				newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQueryDetail.setActiveStatus(1L);			
				newLumenQueryDetail.setStage(lumenStg);
				newLumenQueryDetail.setStatus(lumenStatus);
				entityManager.persist(newLumenQueryDetail);
			}

			if(dtoBean.getUserActionName().equals("Reply")){				
				LumenQuery QryObj = getLumenQueryObj(dtoBean);
				QryObj.setRepliedBy(loginUserId);
				QryObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryObj.setRepliedRole("Level 1");
				QryObj.setModifiedBy(loginUserId);
				QryObj.setStage(lumenStg);
				QryObj.setStatus(lumenStatus);
				QryObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
				entityManager.merge(QryObj);
				
				LumenQueryDetails QryDetailsObj = getLumenQueryDetailObj(dtoBean, QryObj);
				QryDetailsObj.setReplyRemarks(dtoBean.getLevel1ReplyRemarks());
				QryDetailsObj.setRepliedBy(loginUserId);
				QryDetailsObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryDetailsObj.setRepliedRole("Level 1");
				QryDetailsObj.setStage(lumenStg);
				QryDetailsObj.setStatus(lumenStatus);
				QryDetailsObj.setModifiedBy(loginUserId);
				QryDetailsObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
				entityManager.merge(QryDetailsObj);
				
			}

			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("MIS");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Level 1");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);					
				entityManager.persist(newLumenQuery);
				for (String misQry : dtoBean.getMisQryList()){					
					newLumenQueryDetail = new LumenQueryDetails();
					newLumenQueryDetail.setLumenRequest(oldLumenReq);
					newLumenQueryDetail.setLumenQuery(newLumenQuery);
					newLumenQueryDetail.setQueryRemarks(misQry);
					newLumenQueryDetail.setCreatedBy(loginUserId);
					newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					newLumenQueryDetail.setActiveStatus(1L);
					newLumenQueryDetail.setStage(lumenStg);
					newLumenQueryDetail.setStatus(lumenStatus);
					entityManager.persist(newLumenQueryDetail);
				}
			}
			
			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(oldLumenReq);
					entityManager.persist(rec);
				}
			}
						
			setUserActionName(dtoBean.getUserActionName());			
			submitDBProcedureForLumenProcess(oldLumenReq, "Process Level I");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Level 1 Transaction rollbacked.....");
			throw new Exception(e);
		}
	}

	// this method need to be  removed
	@SuppressWarnings("unchecked")
	private LumenQuery getLumenQueryObj(LumenRequestDTO dtoBean){
		Query query = null;
		List<LumenQuery> LumenQuery = null;
		for(Long key : dtoBean.getLumenQueryKey()){
			query = entityManager.createNamedQuery("LumenQuery.findByKeyWithDate");
			query = query.setParameter("lumenReqKey", dtoBean.getLumenRequestKey());
			query = query.setParameter("queryKey", key);
			LumenQuery = query.getResultList();
		}
		if(null != LumenQuery && !LumenQuery.isEmpty())
			return LumenQuery.get(0);
		else
			return null;
	} 
	
	@SuppressWarnings("unchecked")
	private LumenQuery getLumenQueryObjByQueryKey(LumenRequestDTO dtoBean){
		Query query = entityManager.createNamedQuery("LumenQuery.findByLumenKeyWithQryKey");
		query = query.setParameter("lumenReqKey", dtoBean.getLumenRequestKey());
		query = query.setParameter("queryKey", dtoBean.getMisQueryObj().getQueryKey());		
		List<LumenQuery> LumenQuery = query.getResultList();
		if(null != LumenQuery && !LumenQuery.isEmpty())
			return LumenQuery.get(0);
		else
			return null;
	} 
	
	@SuppressWarnings("unchecked")
	private LumenQueryDetails getLumenQueryDetailObj(LumenRequestDTO dtoBean, LumenQuery queryObj){
		Query query = entityManager.createNamedQuery("LumenQueryDetails.findByLumenKeyWithQueryKey");
		query = query.setParameter("lumenReqKey", dtoBean.getLumenRequestKey());
		query = query.setParameter("lumenQueryKey", queryObj.getKey());
		List<LumenQueryDetails> lumenQueryDetails = query.getResultList();
		if(null != lumenQueryDetails && !lumenQueryDetails.isEmpty())
			return lumenQueryDetails.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	private LumenQueryDetails getLumenQueryDetailObj(Long lumenRequestKey, Long queryKey, Long queryDetailKey){
		Query query = entityManager.createNamedQuery("LumenQueryDetails.findByQueryDetailKey");
		query = query.setParameter("lumenReqKey", lumenRequestKey);
		query = query.setParameter("lumenQueryKey", queryKey);
		query = query.setParameter("queryDetailKey", queryDetailKey);		
		List<LumenQueryDetails> lumenQueryDetails = query.getResultList();
		if(null != lumenQueryDetails && !lumenQueryDetails.isEmpty())
			return lumenQueryDetails.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	private LumenRequest getLumenRequestByKey(Long argKey){
		Query query = entityManager.createNamedQuery("LumenRequest.findByKey");
		query = query.setParameter("lumenReqKey", argKey);
		List<LumenRequest> reqObj = query.getResultList();
		if(null != reqObj && !reqObj.isEmpty())
			return reqObj.get(0);
		else
			return null;
	} 

	public void mergeInitiatorChanges(LumenRequestDTO dtoBean) throws Exception{
		LumenRequest oldLumenReq = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		setWorkFlowKey(dtoBean.getWorkFlowKey());
		setOutcome(dtoBean.getOutcome());
		//System.out.println("Initiator Work flow Key"+dtoBean.getWorkFlowKey());
		//System.out.println("Initiator OutCome"+dtoBean.getOutcome());
		try {
			utx.begin();

			// updating changes to the lumenKey.
			oldLumenReq = getLumenRequestByKey(dtoBean.getLumenRequestKey());
			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS_INITIATOR_QUERY);
			oldLumenReq.setStage(lumenStg);
			oldLumenReq.setModifiedBy(loginUserId);
			oldLumenReq.setModifiedDate((new Timestamp(System.currentTimeMillis())));
			Status lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_INITIATOR_REPLY);
			oldLumenReq.setStatus(lumenStatus);
			oldLumenReq.setProcessedBy(dtoBean.getEmpName());

			if(dtoBean.getUserActionName().equals("Reply")){
				LumenQuery QryObj = getLumenQueryObj(dtoBean);
				LumenQueryDetails QryDetailsObj = getLumenQueryDetailObj(dtoBean, QryObj);
				// Query Object
				QryObj.setStage(lumenStg);
				QryObj.setStatus(lumenStatus);
				QryObj.setRepliedBy(loginUserId);
				QryObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				
				if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_II)){
					oldLumenReq.setSendTo("Level 2");
				}else if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_I)){
					oldLumenReq.setSendTo("Level 1");
				}else if(dtoBean.getLevel1ReplyTo().equals(TO_COORDINATOR)){
					oldLumenReq.setSendTo("Coordinator");
				}
				QryObj.setRepliedRole("Initiator");
				QryDetailsObj.setRepliedRole("Initiator");
				//Query Detail Obj
				QryDetailsObj.setReplyRemarks(dtoBean.getLevel1ReplyRemarks());
				QryDetailsObj.setRepliedBy(loginUserId);
				QryDetailsObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryDetailsObj.setStage(lumenStg);
				QryDetailsObj.setStatus(lumenStatus);
				setSelectedToValue(dtoBean.getLevel1ReplyTo());
				
				entityManager.merge(oldLumenReq);				
				entityManager.merge(QryObj);
				entityManager.merge(QryDetailsObj);
			}
			
			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(oldLumenReq);
					entityManager.persist(rec);
				}
			}
			
			setUserActionName(dtoBean.getUserActionName());
			submitDBProcedureForLumenProcess(oldLumenReq, "Initiator Query Cases");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Initiator Transaction rollbacked.....");
			throw new Exception(e);
		}
	}
	
	public void mergeCoordinatorChanges(LumenRequestDTO dtoBean) throws Exception{
		LumenRequest oldLumenReq = null;
		LumenQuery newLumenQuery = null;
		LumenQueryDetails newLumenQueryDetail = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		setWorkFlowKey(dtoBean.getWorkFlowKey());
		setOutcome(dtoBean.getOutcome());
		//System.out.println("Coordinator Work flow Key"+dtoBean.getWorkFlowKey());
		//System.out.println("Coordinator OutCome"+dtoBean.getOutcome());
		try {
			utx.begin();
			
			oldLumenReq = getLumenRequestByKey(dtoBean.getLumenRequestKey());
			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS_LEVEL_CO_ORDINATOR);
			oldLumenReq.setStage(lumenStg);

			Status lumenStatus = null;
			if(dtoBean.getUserActionName().equals("Approve")){
				if(dtoBean.getLevel1ApproveTo().equals(TO_LEVEL_II)){
					oldLumenReq.setSendTo("Level 2");
				}
				setSelectedToValue(dtoBean.getLevel1ApproveTo());
				oldLumenReq.setCoordApprovalRemarks(dtoBean.getCoordApprovalRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_COORDINATOR_APPROVE);
			}

			if(dtoBean.getUserActionName().equals("Reply")){
				if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_II)){
					oldLumenReq.setSendTo("Level 2");
				}else if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_I)){
					oldLumenReq.setSendTo("Level 1");
				}
				setSelectedToValue(dtoBean.getLevel1ReplyTo());
				oldLumenReq.setCoordReplyRemarks(dtoBean.getCoordReplyRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_COORDINATOR_REPLY);
			}
			
			if(dtoBean.getUserActionName().equals("Query")){
				if(dtoBean.getLevel1QueryTo().equals(TO_LEVEL_I)){
					oldLumenReq.setSendTo("Level 1");
				}else if(dtoBean.getLevel1QueryTo().equals(TO_INITIATOR)){
					oldLumenReq.setSendTo("Initiator");
				}
				setSelectedToValue(dtoBean.getLevel1QueryTo());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_COORDINATOR_QUERY);
			}
			
			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				oldLumenReq.setSendTo("MIS");
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_COORDINATOR_MIS);
			}

			oldLumenReq.setModifiedBy(loginUserId);
			oldLumenReq.setModifiedDate((new Timestamp(System.currentTimeMillis())));
			oldLumenReq.setStatus(lumenStatus);
			oldLumenReq.setProcessedBy(dtoBean.getEmpName());
			entityManager.merge(oldLumenReq);
			
			if(dtoBean.getUserActionName().equals("Query")){
				//When a new query is raised an insert should happen in Query and QueryDetail table.
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("Query");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Coordinator");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);
				entityManager.persist(newLumenQuery);

				newLumenQueryDetail = new LumenQueryDetails();
				newLumenQueryDetail.setLumenRequest(oldLumenReq);
				newLumenQueryDetail.setLumenQuery(newLumenQuery);
				newLumenQueryDetail.setQueryRemarks(dtoBean.getLevel1QueryRemarks());
				newLumenQueryDetail.setCreatedBy(loginUserId);
				newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQueryDetail.setActiveStatus(1L);		
				newLumenQueryDetail.setStage(lumenStg);
				newLumenQueryDetail.setStatus(lumenStatus);
				entityManager.persist(newLumenQueryDetail);
			}
			
			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("MIS");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Coordinator");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);					
				entityManager.persist(newLumenQuery);
				for (String misQry : dtoBean.getMisQryList()){					
					newLumenQueryDetail = new LumenQueryDetails();
					newLumenQueryDetail.setLumenRequest(oldLumenReq);
					newLumenQueryDetail.setLumenQuery(newLumenQuery);
					newLumenQueryDetail.setQueryRemarks(misQry);
					newLumenQueryDetail.setCreatedBy(loginUserId);
					newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					newLumenQueryDetail.setActiveStatus(1L);
					newLumenQueryDetail.setStage(lumenStg);
					newLumenQueryDetail.setStatus(lumenStatus);
					entityManager.persist(newLumenQueryDetail);
				}
			}
			
			if(dtoBean.getUserActionName().equals("Reply")){
				LumenQuery QryObj = getLumenQueryObj(dtoBean);
				LumenQueryDetails QryDetailsObj = getLumenQueryDetailObj(dtoBean, QryObj);
				// Query Object
				QryObj.setRepliedBy(loginUserId);
				QryObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryObj.setRepliedRole("Coordinator");
				QryObj.setStage(lumenStg);
				QryObj.setStatus(lumenStatus);
				
				/*if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_II)){
					QryObj.setRepliedRole("Level 2");
					QryDetailsObj.setRepliedRole("Level 2");
				}else if(dtoBean.getLevel1ReplyTo().equals(TO_LEVEL_I)){
					QryObj.setRepliedRole("Level 1");
					QryDetailsObj.setRepliedRole("Level 1");
				}*/
				//Query Detail Obj
				QryDetailsObj.setRepliedRole("Coordinator");
				QryDetailsObj.setReplyRemarks(dtoBean.getCoordReplyRemarks());
				QryDetailsObj.setRepliedBy(loginUserId);
				QryDetailsObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryDetailsObj.setStage(lumenStg);
				QryDetailsObj.setStatus(lumenStatus);
				setSelectedToValue(dtoBean.getLevel1ReplyTo());
				entityManager.merge(QryObj);
				entityManager.merge(QryDetailsObj);
			}
			
			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(oldLumenReq);
					entityManager.persist(rec);
				}
			}
			
			setUserActionName(dtoBean.getUserActionName());
			submitDBProcedureForLumenProcess(oldLumenReq, "Process Coordinator");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Coordinator Transaction rollbacked.....");
			throw new Exception(e);
		}
	}
	
	public void mergeMISChanges(LumenRequestDTO dtoBean) throws Exception{
		LumenRequest oldLumenReq = null;
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		setWorkFlowKey(dtoBean.getWorkFlowKey());
		setOutcome(dtoBean.getOutcome());
		//System.out.println("MIS Work flow Key"+dtoBean.getWorkFlowKey());
		//System.out.println("MIS OutCome"+dtoBean.getOutcome());
		try{
			utx.begin();
			
			oldLumenReq = getLumenRequestByKey(dtoBean.getLumenRequestKey());
			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS_LEVEL_MIS);
			oldLumenReq.setStage(lumenStg);
			Status lumenStatus = null;

			MISQueryReplyDTO qryObj = dtoBean.getMisQueryObj();
			if(dtoBean.getUserActionName().equals("MIS Reply")){
				if(qryObj.getQueryRaisedRole().equals("Level 2")){
					oldLumenReq.setSendTo("Level 2");
					setSelectedToValue("Level 2");
				}else if(qryObj.getQueryRaisedRole().equals("Level 1")){
					oldLumenReq.setSendTo("Level 1");
					setSelectedToValue("Level 1");
				}else if(qryObj.getQueryRaisedRole().equals("Coordinator")){
					oldLumenReq.setSendTo("Coordinator");
					setSelectedToValue("Coordinator");
				}
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_MIS_REPLY);
				oldLumenReq.setStatus(lumenStatus);
			}
			oldLumenReq.setModifiedBy(loginUserId);
			oldLumenReq.setModifiedDate((new Timestamp(System.currentTimeMillis())));
			oldLumenReq.setProcessedBy(dtoBean.getEmpName());
			entityManager.merge(oldLumenReq);
			
			//Update Query and QueryDetails Object;			
			LumenQuery QryObj = getLumenQueryObjByQueryKey(dtoBean);
			// Query Object
			QryObj.setRepliedBy(loginUserId);
			QryObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
			QryObj.setRepliedRole("MIS");
			QryObj.setReplyRemarks(dtoBean.getQueryTableReplyRemarks());
			QryObj.setStage(lumenStg);
			QryObj.setStatus(lumenStatus);
			entityManager.merge(QryObj);
			
			List<LumenQueryDetailsDTO> updatedList = dtoBean.getUpdatedMISReplyRemarksList();
			for(LumenQueryDetailsDTO rec : updatedList){
				LumenQueryDetails QryDetailsObj = getLumenQueryDetailObj(QryObj.getLumenRequest().getKey(), QryObj.getKey(), rec.getQueryDetailsKey());
				QryDetailsObj.setReplyRemarks(rec.getReplyRemarks());
				QryDetailsObj.setRepliedBy(loginUserId);
				QryDetailsObj.setRepliedDate((new Timestamp(System.currentTimeMillis())));
				QryDetailsObj.setRepliedRole("MIS");
				QryDetailsObj.setStage(lumenStg);
				QryDetailsObj.setStatus(lumenStatus);
				entityManager.merge(QryDetailsObj);
			}
			
			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(oldLumenReq);
					entityManager.persist(rec);
				}
			}
			// inserting ack record for all the uploaded document while submit the mis request.
			if(dtoBean.getListOfAckTableDTO() != null && !dtoBean.getListOfAckTableDTO().isEmpty()){
				for(DocumentAckTableDTO rec : dtoBean.getListOfAckTableDTO()){
					LumenQueryDocument lqd = new LumenQueryDocument();
					LumenRequest lmnReq = rec.getLumenRequest();
					LumenQuery lmnQryReq = rec.getLumenQuery();
					lqd.setLumenRequest(lmnReq);
					lqd.setLumenQuery(lmnQryReq);
					lqd.setLumenQueryDetails(getLumenQueryDetailObj(lmnReq.getKey(), lmnQryReq.getKey(), rec.getQueryDetailsKey()));
					lqd.setFileName(rec.getFileName());
					lqd.setDocumentToken(rec.getDocumentToken());
					lqd.setDeletedFlag(rec.getDeletedFlag());
					lqd.setCreatedBy(loginUserId);
					lqd.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					lqd.setIntimationNumber(oldLumenReq.getIntimationNumber());
					entityManager.persist(lqd);
				}
			}
			setUserActionName(dtoBean.getUserActionName());
			submitDBProcedureForLumenProcess(oldLumenReq, "Process MIS Reply");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen MIS Transaction rollbacked.....");
			throw new Exception(e);
		}
	}
	
	public void mergeLevelTwoChanges(LumenRequestDTO dtoBean) throws Exception{
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		LumenRequest oldLumenReq = null;
		LumenDetails newLumenDetails = null;
		LumenQuery newLumenQuery = null;
		LumenQueryDetails newLumenQueryDetail = null;
		setWorkFlowKey(dtoBean.getWorkFlowKey());
		setOutcome(dtoBean.getOutcome());
		//System.out.println("Level 2 Work flow Key"+dtoBean.getWorkFlowKey());
		//System.out.println("Level 2 OutCome"+dtoBean.getOutcome());
		try {
			utx.begin();

			oldLumenReq = getLumenRequestByKey(dtoBean.getLumenRequestKey());
			oldLumenReq.setLumenType(dtoBean.getLumenTypeId());
//			oldLumenReq.setHospitalErrorType(dtoBean.getErrorTypeId());
			oldLumenReq.setLumenRemarks(dtoBean.getComments());
			oldLumenReq.setRemarks(dtoBean.getRemarks());

			Stage lumenStg = masterService.getStageBykey(ReferenceTable.LUMEN_PROCESS_LEVEL_II);
			oldLumenReq.setStage(lumenStg);

			Status lumenStatus = null;
			if(dtoBean.getUserActionName().equals("Close")){
				oldLumenReq.setLevel2CloseRemarks(dtoBean.getLevel1CloseRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_II_CLOSE);
			}

			if(dtoBean.getUserActionName().equals("Approve")){
				oldLumenReq.setSendTo("Level 2 Approved");
				setSelectedToValue("Level 2");
				oldLumenReq.setLevel2ApprovalRemarks(dtoBean.getLevel1ApprovalRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_II_APPROVE);				
			}

			if(dtoBean.getUserActionName().equals("Reject")){
				oldLumenReq.setLevel2RejectRemarks(dtoBean.getLevel1RejectRemarks());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_II_REJECT);
			}

			if(dtoBean.getUserActionName().equals("Query")){				
				if(dtoBean.getLevel1QueryTo().equals(TO_LEVEL_I)){
					oldLumenReq.setSendTo("Level 1");
				}else if(dtoBean.getLevel1QueryTo().equals(TO_INITIATOR)){
					oldLumenReq.setSendTo("Initiator");
				}else if(dtoBean.getLevel1QueryTo().equals(TO_COORDINATOR)){
					oldLumenReq.setSendTo("Coordinator");
				}
				setSelectedToValue(dtoBean.getLevel1QueryTo());
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_II_QUERY);
			}
			
			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				oldLumenReq.setSendTo("MIS");
				lumenStatus = masterService.getStatusByKey(ReferenceTable.LUMEN_LEVEL_II_MIS);
			}

			oldLumenReq.setModifiedBy(loginUserId);
			oldLumenReq.setModifiedDate((new Timestamp(System.currentTimeMillis())));
			oldLumenReq.setStatus(lumenStatus);
			oldLumenReq.setProcessedBy(dtoBean.getEmpName());
			entityManager.merge(oldLumenReq);

			// persist participant and lapse list if available and Only when approving the request.
			//if(dtoBean.getUserActionName().equals("Approve")){
				if(dtoBean.getListOfParticipants().size() > 0){
					for (String name : dtoBean.getListOfParticipants()){
						LumenDetails tempObj = checkParticipantAvailability(name, oldLumenReq.getKey());
						if(tempObj == null){
							newLumenDetails = new LumenDetails();
							newLumenDetails.setLumenRequest(oldLumenReq);
							newLumenDetails.setParticipantType("Participant");
							newLumenDetails.setEmployeeName(name);
							newLumenDetails.setCreatedBy(loginUserId);
							newLumenDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
							newLumenDetails.setStage(lumenStg);
							newLumenDetails.setStatus(lumenStatus);
							entityManager.persist(newLumenDetails);
						}else{
							tempObj.setParticipantType("Participant");
							tempObj.setModifiedBy(loginUserId);
							tempObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
							tempObj.setStage(lumenStg);
							tempObj.setStatus(lumenStatus);
							entityManager.merge(tempObj);
						}
					}
				}

				if(dtoBean.getListOfLapse().size() > 0){
					for (String name : dtoBean.getListOfLapse()){
						LumenDetails tempObj = checkParticipantAvailability(name, oldLumenReq.getKey());
						if(tempObj == null){
							newLumenDetails = new LumenDetails();
							newLumenDetails.setLumenRequest(oldLumenReq);
							newLumenDetails.setParticipantType("Lapse");
							newLumenDetails.setEmployeeName(name);
							newLumenDetails.setCreatedBy(loginUserId);
							newLumenDetails.setCreatedDate((new Timestamp(System.currentTimeMillis())));
							newLumenDetails.setStage(lumenStg);
							newLumenDetails.setStatus(lumenStatus);
							entityManager.persist(newLumenDetails);
						}else{
							tempObj.setParticipantType("Lapse");
							tempObj.setModifiedBy(loginUserId);
							tempObj.setModifiedDate((new Timestamp(System.currentTimeMillis())));
							tempObj.setStage(lumenStg);
							tempObj.setStatus(lumenStatus);
							entityManager.merge(tempObj);
						}
					}
				}
			//}

			if(dtoBean.getUserActionName().equals("Query")){
				
				//When a new query is raised an insert should happen in Query and QueryDetail table.
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("Query");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Level 2");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);
				entityManager.persist(newLumenQuery);
				
				newLumenQueryDetail = new LumenQueryDetails();
				newLumenQueryDetail.setLumenRequest(oldLumenReq);
				newLumenQueryDetail.setLumenQuery(newLumenQuery);
				newLumenQueryDetail.setQueryRemarks(dtoBean.getLevel1QueryRemarks());
				newLumenQueryDetail.setCreatedBy(loginUserId);
				newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQueryDetail.setActiveStatus(1L);	
				newLumenQueryDetail.setStage(lumenStg);
				newLumenQueryDetail.setStatus(lumenStatus);
				entityManager.persist(newLumenQueryDetail);
			}

			if(dtoBean.getUserActionName().equals("Refer to MIS")){
				newLumenQuery = new LumenQuery();
				newLumenQuery.setLumenRequest(oldLumenReq);
				newLumenQuery.setQueryType("MIS");
				newLumenQuery.setQueryRaisedBy(loginUserId);
				newLumenQuery.setQueryRaisedDate((new Timestamp(System.currentTimeMillis())));
				newLumenQuery.setQueryRaisedRole("Level 2");
				newLumenQuery.setCreatedBy(loginUserId);
				newLumenQuery.setStage(lumenStg);
				newLumenQuery.setStatus(lumenStatus);
				newLumenQuery.setActiveStatus(1L);					
				entityManager.persist(newLumenQuery);
				for (String misQry : dtoBean.getMisQryList()){					
					newLumenQueryDetail = new LumenQueryDetails();
					newLumenQueryDetail.setLumenRequest(oldLumenReq);
					newLumenQueryDetail.setLumenQuery(newLumenQuery);
					newLumenQueryDetail.setQueryRemarks(misQry);
					newLumenQueryDetail.setCreatedBy(loginUserId);
					newLumenQueryDetail.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					newLumenQueryDetail.setActiveStatus(1L);
					newLumenQueryDetail.setStage(lumenStg);
					newLumenQueryDetail.setStatus(lumenStatus);
					entityManager.persist(newLumenQueryDetail);
				}
			}
			
			if(dtoBean.getUserActionName().equals("Approve")){
				if(dtoBean.getGenerateLetterFlag() == true){
					//generate letter code has to go here.......
					List<LetterDetailsDTO> updateList = generateAllLetter(dtoBean, dtoBean.getListOfApprovalLetters());
					LumenLetterDetails letterTableObj = null;
					for(LetterDetailsDTO rec : updateList){
						letterTableObj =  new LumenLetterDetails();
						letterTableObj.setLumenRequest(oldLumenReq);
						letterTableObj.setLetterTo(rec.getToPerson());
						letterTableObj.setAddressLine1(rec.getAddressDetails().getAddressLine1());
						letterTableObj.setAddressLine2(rec.getAddressDetails().getAddressLine2());
						letterTableObj.setAddressLine3(rec.getAddressDetails().getAddressLine3());
						letterTableObj.setLetterSubject(rec.getSubject());
						letterTableObj.setLetterContent(rec.getLetterContent());
						if(rec.getDocumentToken() != null){
							letterTableObj.setDocumentToken(rec.getDocumentToken());
						}
						letterTableObj.setActiveStatus(1L);
						letterTableObj.setCreatedBy(loginUserId);
						letterTableObj.setCreatedDate((new Timestamp(System.currentTimeMillis())));
						entityManager.persist(letterTableObj);
					}
				}
			}
			
			// inserting all the uploading document while submit the lumen initiate request.
			if(dtoBean.getListOfUserUploadedDocuments() != null && !dtoBean.getListOfUserUploadedDocuments().isEmpty()){
				for(DocumentDetails rec : dtoBean.getListOfUserUploadedDocuments()){
					rec.setSfFileName(null);
					rec.setLumenRequest(oldLumenReq);
					entityManager.persist(rec);
				}
			}
						
			setUserActionName(dtoBean.getUserActionName());			
			submitDBProcedureForLumenProcess(oldLumenReq, "Process Level II");
			utx.commit();
		} catch (Exception e) {
			utx.rollback();
			e.printStackTrace();
			System.out.println("Lumen Level 2 Transaction rollbacked.....");
			throw new Exception(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<LetterDetailsDTO> generateAllLetter(LumenRequestDTO dtoBean, List<LetterDetailsDTO> argLetterDTO){
		LumenDocumentGenerator letterObj = null;
		ReportDto reportDto = null;
		for(LetterDetailsDTO rec : argLetterDTO){
			letterObj = new LumenDocumentGenerator();
			reportDto = new ReportDto();
			if(dtoBean.getClaim() != null){
				reportDto.setClaimId(dtoBean.getClaim().getClaimId());
			}
			List<LetterDetailsDTO> letterDTOList = new ArrayList<LetterDetailsDTO>();
			letterDTOList.add(rec);		
			reportDto.setBeanList(letterDTOList);
			String generatedFilePath = letterObj.generatePdfDocument("LumenLetter", reportDto);
			WeakHashMap<String, Object> inParameters = SHAUtils.uploadFileToDMS(generatedFilePath);
			if(inParameters != null){
				rec.setDocumentToken(Long.parseLong(String.valueOf(inParameters.get("fileKey"))));
			}
		}
		return argLetterDTO;
	}
	
	@SuppressWarnings("unchecked")
	public LumenDetails checkParticipantAvailability(String argparticipantName, Long lumenReqKey){
		Query query = entityManager.createNamedQuery("LumenDetails.findByNameWithLumenKey");
		query = query.setParameter("lumenReqKey", lumenReqKey);
		query = query.setParameter("participantName", argparticipantName);
		List<LumenDetails> lumenDetailsList  = query.getResultList();
		if(null != lumenDetailsList && !lumenDetailsList.isEmpty())
			return lumenDetailsList.get(0);
		else
			return null;
	}

}
