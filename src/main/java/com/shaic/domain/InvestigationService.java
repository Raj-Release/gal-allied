package com.shaic.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.cms.TimeStampAndCRL;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.ReportDto;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.fileUpload.ReferenceDocument;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationCompletionDetailsDTO;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.investigationmaster.InvestigationMasterDTO;
import com.shaic.reimbursement.processi_investigationi_initiated.ProcessInvestigationInitiatedDto;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class InvestigationService {

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Investigation> getByInvestigation(Long intimationKey) {
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByIntimation").setParameter("intiationKey",
				intimationKey);
		List<Investigation> investigaionList = (List<Investigation>) findAll
				.getResultList();
		return investigaionList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Investigation> getByInvestigationByClaimKey(Long claimKey) {
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey",
				claimKey);
		List<Investigation> investigaionList = (List<Investigation>) findAll
				.getResultList();
		return investigaionList;
	}
	
	@SuppressWarnings("unchecked")	
	public Investigation getByInvestigationKey(Long investigaitonKey){
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByInvestigationKey").setParameter("investigationKey",
						investigaitonKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		if(!investigationList.isEmpty()){
			return investigationList.get(0);
		}else{
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")	
	public Investigation getInvestigationByTransactionKey(Long investigaitonKey){
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByTransactionKey").setParameter("transactionKey",
						investigaitonKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		if(!investigationList.isEmpty()){
			return investigationList.get(0);
		}else{
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Investigation getInvestigationByClaimKey(Long claimKey){
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey",
						claimKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		List<Long> keyList = new ArrayList<Long>();
        for (Investigation investigation : investigationList) {
        	keyList.add(investigation.getKey());
		}
        if(!keyList.isEmpty()){
        Long key = Collections.max(keyList);
        
        Investigation investigation = getByInvestigationKey(key);
        return investigation;
        }
        else{
        	return null;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<UploadedDocumentsDTO> getUploadDocumentList(Long InvestigationAssignedKey){
		
		List<MultipleUploadDocumentDTO> updateDocumentDetails = getUpdateDocumentDetails(InvestigationAssignedKey);
		
//		Query findAll = entityManager.createNamedQuery(
//				"Investigation.findByInvestigationKey").setParameter("investigationKey",
//						InvestigationKey);
//		List<Investigation> investigationList = (List<Investigation>) findAll
//				.getResultList();
		
		AssignedInvestigatiorDetails  assignedObj = getAssignedInvestigByKey(InvestigationAssignedKey);
		
		Integer sno = 1;
		List<UploadedDocumentsDTO> list = new ArrayList<UploadedDocumentsDTO>();
		UploadedDocumentsDTO dto = null;
		for (MultipleUploadDocumentDTO documentDto : updateDocumentDetails) {
			if(documentDto.getFileName() != null){
				dto = new UploadedDocumentsDTO();
				dto.setSno(sno);
				dto.setFileName(documentDto.getFileName());
				dto.setToken(documentDto.getFileToken());
				dto.setFileType(documentDto.getFileType() != null ? documentDto.getFileType().getValue(): "");
				dto.setKey(documentDto.getKey());
				/*Query find = entityManager.createNamedQuery(
						"Reimbursement.findByClaimKey").setParameter("claimKey",
								assignedObj.getReimbursement().getClaim().getKey());
				List<Reimbursement> reimbursement = (List<Reimbursement>)find.getResultList();*/
				
				if(assignedObj !=null && null != assignedObj.getReimbursement()){
//					if(! reimbursement.isEmpty()){
						if(assignedObj.getReimbursement().getRodNumber() !=null && !assignedObj.getReimbursement().getRodNumber().isEmpty()) {
						dto.setRODNo(assignedObj.getReimbursement().getRodNumber());
						}
//					}
				}
				list.add(dto);
				sno++;
			}
		}
		return list;
	}
	
	public List<MultipleUploadDocumentDTO> getUpdateDocumentDetails(Long key){
		
		Query query = entityManager.createNamedQuery("ReferenceDocument.findByTransactionKey");
		query.setParameter("transactionKey", key);
		
		List<MultipleUploadDocumentDTO> documentList = new ArrayList<MultipleUploadDocumentDTO>();
		
		List<ReferenceDocument> resultList = (List<ReferenceDocument>) query.getResultList();
		for (ReferenceDocument referenceDocument : resultList) {
			MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
			dto.setTransactionKey(referenceDocument.getTransactionKey());
			dto.setFileName(referenceDocument.getFileName());
			dto.setFileToken(referenceDocument.getDocumentToken());
			dto.setFileType(referenceDocument.getFileType() != null ? new SelectValue(referenceDocument.getFileType().getKey(),referenceDocument.getFileType().getValue()) : null);
			dto.setKey(referenceDocument.getKey());
			documentList.add(dto);			
		}
		
		return documentList;
	}
	
	
	public List<DMSDocumentDetailsDTO> getUploadedDocDetailsByToken(List<Long> docTokenList){
	
		List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
			if(docTokenList != null && !docTokenList.isEmpty()){
				for (Long token : docTokenList) {
					Query docdetailsQuery = entityManager.createNamedQuery("DocumentDetails.findByDocToken");
					docdetailsQuery.setParameter("documentToken", token);
					List<DocumentDetails> resultList = docdetailsQuery.getResultList();
					if(resultList != null && !resultList.isEmpty()){
						documentDetailsList.add(resultList.get(0));
					}
				}			
			
				List<DMSDocumentDetailsDTO>	documentDetailsDTOList = CreateRODMapper.getInstance().getDMSDocumentDetailsDTO(documentDetailsList);
		
				/**
				 * If data's are inserted via , star fax source, then the sf_file_name will not be null.
				 * On the other hand, if the data's are inserted via trigger , then sf_file_name will be null
				 * and file_name will hold value. Hence to avoid this confusion, sf_file_name or file_name
				 * both will be populated in file name variable of dto. 
				 */
				if(null != documentDetailsDTOList && !documentDetailsDTOList.isEmpty())
				{
					for (DMSDocumentDetailsDTO documentDetails : documentDetailsDTOList) {
						
						if(null != documentDetails.getReimbursementNumber())
						{
							documentDetails.setCashlessOrReimbursement(documentDetails.getReimbursementNumber());;
						
						}
						else
						{
							documentDetails.setCashlessOrReimbursement(documentDetails.getCashlessNumber());
						}
						
						if(null == documentDetails.getFileName() || ("").equalsIgnoreCase(documentDetails.getFileName()))
						{
							documentDetails.setFileName(documentDetails.getGalaxyFileName());
						}
					}
				
				    return documentDetailsDTOList;
				}
			}
			return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Investigation createInvestigation(InitiateInvestigationDTO a_initiateInvestigationDTO,String userName, String password) {
		if (a_initiateInvestigationDTO != null) {
			Investigation investigation = new Investigation();
			SelectValue selectValue = a_initiateInvestigationDTO
					.getAllocationTo();
			MastersValue mastersValue = new MastersValue();
			mastersValue.setKey(selectValue.getId());
			mastersValue.setValue(selectValue.getValue());
			investigation.setAllocationTo(mastersValue);
			investigation.setReasonForReferring(a_initiateInvestigationDTO
					.getReasonForRefering());
			investigation.setTriggerPoints(a_initiateInvestigationDTO
					.getTriggerPointsToFocus());
			investigation.setPolicy(entityManager.find(Policy.class,
					a_initiateInvestigationDTO.getPolicyKey()));
			Claim claimObj = entityManager.find(Claim.class,
					a_initiateInvestigationDTO.getClaimKey());
			investigation.setClaim(claimObj);
			investigation.setIntimation(entityManager.find(Intimation.class,
					a_initiateInvestigationDTO.getIntimationkey()));			
			investigation.setStage(a_initiateInvestigationDTO.getStage());
			investigation.setStatus(a_initiateInvestigationDTO.getStatus());
			String userNameForDB = SHAUtils.getUserNameForDB(userName);
			investigation.setCreatedBy(userNameForDB);
			investigation.setTransactionKey(a_initiateInvestigationDTO.getTransactionKey());
			if(claimObj.getClaimType() != null && ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(claimObj.getClaimType().getKey()))
			{
				investigation.setTransactionFlag("R");
			}
			else{
				investigation.setTransactionFlag("C");
			}
			entityManager.persist(investigation);
			entityManager.flush();
			
//			setBPMforInvestigation(investigation, userName, password);
			
			return investigation;
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Investigation create(InitiateInvestigationDTO a_initiateInvestigationDTO,Investigation investigationValue,String userName, String password,PreauthDTO bean,String docToken) {
		Investigation investigation = new Investigation();
		if (a_initiateInvestigationDTO != null) {			
			SelectValue selectValue = a_initiateInvestigationDTO
					.getAllocationTo();
			MastersValue mastersValue = new MastersValue();
			mastersValue.setKey(selectValue.getId());
			mastersValue.setValue(selectValue.getValue());
			investigation.setAllocationTo(mastersValue);
			investigation.setReasonForReferring(a_initiateInvestigationDTO
					.getReasonForRefering());
			investigation.setTriggerPoints(a_initiateInvestigationDTO
					.getTriggerPointsToFocus());
			investigation.setPolicy(entityManager.find(Policy.class,
					a_initiateInvestigationDTO.getPolicyKey()));
			investigation.setClaim(entityManager.find(Claim.class,
					a_initiateInvestigationDTO.getClaimKey()));
			investigation.setIntimation(entityManager.find(Intimation.class,
					a_initiateInvestigationDTO.getIntimationkey()));			
			investigation.setStage(a_initiateInvestigationDTO.getStage());
			investigation.setStatus(a_initiateInvestigationDTO.getStatus());
			String userNameForDB = SHAUtils.getUserNameForDB(userName);
			investigation.setCreatedBy(userNameForDB);
			investigation.setTransactionKey(a_initiateInvestigationDTO.getTransactionKey());
			investigation.setTransactionFlag("C");
			//CR2019058 persisting initiated date
			Date currentDate = new Date();
			investigation.setInitiatedDate(new Timestamp(currentDate.getTime()));
			investigation.setToken(docToken);
			entityManager.persist(investigation);
			entityManager.flush();
			investigationValue = investigation;
			
//			setBPMforInvestigation(investigation, userName, password);
			return investigation;
		}
		
		return null;
	}
	
	public void setBPMforInvestigation(Investigation investigation,String userName,String password){/*
		
		IntimationType intimationType = new IntimationType();
		ClaimType claimType = new ClaimType();
		HospitalInfoType hospitalInfoType = new HospitalInfoType();
	    com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.investigation.InvestigationType investigationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.investigation.InvestigationType();
		Status status = investigation.getStatus();
		if(status != null){
			intimationType.setStatus(status.getProcessValue());
		}
		if(investigation.getClaim() != null){
			claimType.setClaimType(investigation.getClaim().getClaimType() != null ? investigation.getClaim().getClaimType().getValue().toUpperCase() : "");
			Intimation intimation = investigation.getClaim().getIntimation();
			Policy policy = intimation.getPolicy();
			intimationType.setIntimationNumber(intimation.getIntimationId());
			intimationType.setIsClaimPending(true);
			intimationType.setIsPolicyValid(false);
			intimationType.setIsBalanceSIAvailable(true);
			hospitalInfoType.setHospitalType(intimation.getHospitalType().getValue());
			intimationType.setKey(intimation.getKey());
			
			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(policy.getPolicyNumber());
			
		    ClaimRequestType claimRequestType = new ClaimRequestType();
		    claimRequestType.setCpuCode(intimation.getCpuCode().getDescription());
		    if(null != investigation && null != investigation.getClaim() && null != investigation.getClaim().getIntimation() && null != investigation.getClaim().getIntimation().getCpuCode())
		    {
		    	claimRequestType.setCpuCode(String.valueOf(investigation.getClaim().getIntimation().getCpuCode().getCpuCode()));
		    }
			
			investigationType.setKey(investigation.getKey());
			investigationType.setIsInvestigationDisapproved(false);
			investigationType.setStatus(investigation.getClaim().getStatus().getProcessValue());
			
			Insured insured = intimation.getInsured();
			
			Claim claim = investigation.getClaim();
			
			ClassificationType classificationType = new ClassificationType();
			if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(0l)){
				
				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			}else{
				classificationType.setPriority(SHAConstants.NORMAL);
			}
		
			classificationType.setType(SHAConstants.TYPE_FRESH);
			classificationType.setSource(investigation.getStage().getStageName());
			
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob((claim.getProcessClaimType() != null  && claim.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_LOB_FLAG)) ? SHAConstants.HEALTH_LOB : SHAConstants.PA_LOB);
			productInfo.setLobType(claim.getProcessClaimType());
			
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType cashlessPayload = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
			cashlessPayload.setClaimRequest(claimRequestType);
			cashlessPayload.setIntimation(intimationType);
			cashlessPayload.setHospitalInfo(hospitalInfoType);
			cashlessPayload.setInvestigation(investigationType);
			cashlessPayload.setClassification(classificationType);
			cashlessPayload.setClaim(claimType);
			
			cashlessPayload.setPolicy(policyType);
			cashlessPayload.setProductInfo(productInfo);
			
			BPMClientContext.intiateInvestigationProcess(userName, password, cashlessPayload);
			
		}
		
	*/}

	public TmpInvestigation getInvestigatorById(String code) {
		if (code != null) {
			return entityManager.find(TmpInvestigation.class, code);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TmpInvestigation getTmpInvestigationByInvestigatorCode(String code){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByInvestigaitonCode").setParameter("investigatorCode", code);
		List<TmpInvestigation> tmpInvestigationList = query.getResultList();
		if(!tmpInvestigationList.isEmpty()){
			return tmpInvestigationList.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public TmpInvestigation getTmpInvestigationByInvestigatorCodeWithoutStatus(String code){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByInvestigaitonCodeWithoutStatus").setParameter("investigatorCode", code);
		List<TmpInvestigation> tmpInvestigationList = query.getResultList();
		if(!tmpInvestigationList.isEmpty()){
			return tmpInvestigationList.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Investigation> getByClaimKey(Long claimKey) {
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey",
				claimKey);
		List<Investigation> policyList = (List<Investigation>) findAll
				.getResultList();
		return policyList;
	}

	@SuppressWarnings("unchecked")
	public List<Investigation> getLatestListByClaimKey(Long claimKey) {
		Query findAll = entityManager.createNamedQuery("Investigation.findLatestByClaimKey").setParameter("claimKey",
				claimKey);
		List<Investigation> policyList = (List<Investigation>) findAll
				.getResultList();
		return policyList;
	}
	
	public Investigation updateIvestigation(Investigation investigation,String investigationStatus,String userName){
		
		String userNameForDB = SHAUtils.getUserNameForDB(userName);
		
		List<AssignedInvestigatiorDetails> assignedList = getAssignedListByInvestigationKey(investigation.getKey());
		
		if(assignedList != null && !assignedList.isEmpty()){
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : assignedList) {
				if(SHAConstants.UPLOAD_INVESTIGATION.equals(investigationStatus)){
					assignedInvestigatiorDetails.setReportReceivedDate(new Date());
					assignedInvestigatiorDetails.setCompletionDate(new Date());
				}
				if(!SHAConstants.ASSIGN_INVESTIGATION.equals(investigationStatus)){
					assignedInvestigatiorDetails.setModifiedBy(userNameForDB);
					assignedInvestigatiorDetails.setModifiedDate(new Date());
				}
				
				entityManager.merge(assignedInvestigatiorDetails);
				entityManager.flush();
			}
		}
		
//		if(SHAConstants.ASSIGN_INVESTIGATION.equals(investigationStatus)){
//		investigation.setAssignedDate(new Timestamp(System.currentTimeMillis()));
//		}
//		else if(SHAConstants.ACKNOWLEDGEMENT_INVESTIGATION.equals(investigationStatus)){
//			investigation.setCompletionDate(new Timestamp(System.currentTimeMillis()));
//		}
//		else if(SHAConstants.UPLOAD_INVESTIGATION.equals(investigationStatus)){
//			investigation.setReportReceivedDate(new Timestamp(System.currentTimeMillis()));
//			investigation.setCompletionDate(new Timestamp(System.currentTimeMillis()));
//		}
	
		if(investigation!=null){
			if(userNameForDB != null){
				investigation.setModifiedBy(userNameForDB);
			}
			investigation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			//CR2019058 adding drafted date field 
			Date currentDate = new Date();
			investigation.setReqDraftedDate(new Timestamp(currentDate.getTime()));
			entityManager.merge(investigation);
			entityManager.flush();
			
//			Claim claim = investigation.getClaim();
//			claim.setStatus(investigation.getStatus());
//			
//			entityManager.merge(claim);
//			entityManager.flush();
			return investigation;
		}
		return null;
	}
	
	public void saveAssignedIvestigation(SearchUploadInvesticationTableDTO tableDto){
		try{
			if(tableDto.getInvestigationAssignedKey() != null){
				AssignedInvestigatiorDetails assignedInvestigatorObj = getAssignedInvestigByKey(tableDto.getInvestigationAssignedKey());
				if(assignedInvestigatorObj!= null){
					assignedInvestigatorObj.setCompletionDate(new Date());
					assignedInvestigatorObj.setCompletionRemarks(tableDto.getInvestigationCompletionRemarks());
					assignedInvestigatorObj.setModifiedBy(SHAUtils.getUserNameForDB(tableDto.getUsername()));
					assignedInvestigatorObj.setModifiedDate(new Date());
					entityManager.merge(assignedInvestigatorObj);
				}	
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void updateAssignedIvestigation(AssignedInvestigatiorDetails assignedInvestigation,String investigationStatus,String userName,Investigation investigation){
		
		String userNameForDB = SHAUtils.getUserNameForDB(userName);
		
		AssignedInvestigatiorDetails assignedInvestigatorObj = getAssignedInvestigByKey(assignedInvestigation.getKey());
		
		if(assignedInvestigatorObj != null ){
			assignedInvestigatorObj.setStage(assignedInvestigation.getStage());
			assignedInvestigatorObj.setStatus(assignedInvestigation.getStatus());
			assignedInvestigatorObj.setModifiedDate(new Date());
				if(SHAConstants.UPLOAD_INVESTIGATION.equals(investigationStatus)){
					assignedInvestigatorObj.setReportReceivedDate(new Date());
					assignedInvestigatorObj.setCompletionDate(new Date());  //TODO completionremarks needs to set
					assignedInvestigatorObj.setCompletionRemarks(assignedInvestigation.getCompletionRemarks());
				}
				assignedInvestigatorObj.setModifiedBy(userNameForDB);
				if(SHAConstants.INVESTIGATION_GRADING.equals(investigationStatus)){
					assignedInvestigatorObj.setGradedBy(assignedInvestigation.getGradedBy());
					assignedInvestigatorObj.setGradingDate(assignedInvestigation.getGradingDate());
					assignedInvestigatorObj.setGradingCategory(assignedInvestigation.getGradingCategory());
					assignedInvestigatorObj.setGradingRemarks(assignedInvestigation.getGradingRemarks());
				}
				entityManager.merge(assignedInvestigatorObj);
				entityManager.flush();
				
				if(SHAConstants.UPLOAD_INVESTIGATION.equals(investigationStatus)){
					StageInformation stgInformation = new StageInformation();
					stgInformation.setIntimation(assignedInvestigation.getInvestigation().getClaim().getIntimation());				
					stgInformation.setClaimType(assignedInvestigation.getInvestigation().getClaim().getClaimType());
					stgInformation.setStage(assignedInvestigation.getStage());
					Status status = new Status();
					status.setKey(ReferenceTable.INVS_REPORT_UPLOADED_KEY);
					status.setProcessValue(ReferenceTable.INVS_REPORT_UPLOADED_VALUE);
					stgInformation.setStatus(status);
					stgInformation.setClaim(assignedInvestigation.getInvestigation().getClaim());
					stgInformation.setReimbursement(null !=assignedInvestigation.getReimbursement() ? assignedInvestigation.getReimbursement() :null);
					if(null != investigation.getTransactionKey() && 
							(null != investigation.getTransactionFlag() && SHAConstants.TRANSACTION_FLAG_CASHLESS.equalsIgnoreCase(investigation.getTransactionFlag()))){
						Preauth preauth = getPreauthById(investigation.getTransactionKey());
						if(null != preauth){
							stgInformation.setPreauth(preauth);
						}
					}
					stgInformation.setCreatedBy(userName);
					stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					stgInformation.setStatusRemarks(assignedInvestigation.getCompletionRemarks());
					
					entityManager.persist(stgInformation);
					entityManager.flush();
				}
				
		
		}
		int uploadCompletedStatus = 0;
		int gradingCompletedStatus = 0;
		List<AssignedInvestigatiorDetails> assignedInvList = getAssignedListByInvestigationKey(assignedInvestigation.getInvestigation().getKey());
		if(assignedInvList != null && !assignedInvList.isEmpty()){
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : assignedInvList) {
					if(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(assignedInvestigatiorDetails.getStatus().getKey()) 
							|| ReferenceTable.INVESTIGATION_GRADING.equals(assignedInvestigatiorDetails.getStatus().getKey())
							|| ReferenceTable.PARALLEL_INVES_CANCELLED.equals(assignedInvestigatiorDetails.getStatus().getKey())){
						uploadCompletedStatus++;
					}
					if(ReferenceTable.INVESTIGATION_GRADING.equals(assignedInvestigatiorDetails.getStatus().getKey())){ 
						gradingCompletedStatus++;
					}					
				}
				Investigation investObj = assignedInvestigation.getInvestigation();
				if((uploadCompletedStatus == assignedInvList.size() && SHAConstants.UPLOAD_INVESTIGATION.equalsIgnoreCase(investigationStatus)) || 
						(gradingCompletedStatus == assignedInvList.size() && SHAConstants.INVESTIGATION_GRADING.equalsIgnoreCase(investigationStatus))){
					if(investObj!=null){
						if(SHAConstants.UPLOAD_INVESTIGATION.equalsIgnoreCase(investigationStatus)){
							investObj.setReportReceivedDate(new Timestamp(System.currentTimeMillis()));
							investObj.setCompletionDate(new Timestamp(System.currentTimeMillis()));
						}
						
						investObj.setStage(assignedInvestigation.getStage());
						investObj.setStatus(assignedInvestigation.getStatus());
						
						if(userNameForDB != null){
							investObj.setModifiedBy(userNameForDB);
						}
						
						investObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(investObj);
						entityManager.flush();
						//updateReimbursementStatus(assignedInvestigation.getReimbursement().getKey(), assignedInvestigation.getStatus(), assignedInvestigation.getStage(), userNameForDB);
				    }
			    }

				if(null != investObj.getTransactionFlag() && !SHAConstants.TRANSACTION_FLAG_CASHLESS.equalsIgnoreCase(investObj.getTransactionFlag())){
					if((uploadCompletedStatus == assignedInvList.size() && SHAConstants.UPLOAD_INVESTIGATION.equalsIgnoreCase(investigationStatus))){
						updateReimbursementStatus(assignedInvestigation.getReimbursement().getKey(), assignedInvestigation.getStatus(), assignedInvestigation.getStage(), userNameForDB);
					}
				}
			/*if(uploadCompletedStatus == assignedInvList.size() && SHAConstants.UPLOAD_INVESTIGATION.equalsIgnoreCase(investigationStatus)){
				updateReimbursementStatus(assignedInvestigation.getReimbursement().getKey(), assignedInvestigation.getStatus(), assignedInvestigation.getStage(), userNameForDB);
				if(investObj!=null){
				investObj.setStage(assignedInvestigation.getStage());
				investObj.setStatus(assignedInvestigation.getStatus());
					if(userNameForDB != null){
						investObj.setModifiedBy(userNameForDB);
					}
					investObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(investObj);
					entityManager.flush();
			    }
			}
			if(gradingCompletedStatus == assignedInvList.size() && investigationStatus.equalsIgnoreCase(SHAConstants.INVESTIGATION_GRADING)){
				updateReimbursementStatus(assignedInvestigation.getReimbursement().getKey(), assignedInvestigation.getStatus(), assignedInvestigation.getStage(), userNameForDB);
				if(investObj!=null){
				investObj.setStage(assignedInvestigation.getStage());
				investObj.setStatus(assignedInvestigation.getStatus());
					if(userNameForDB != null){
						investObj.setModifiedBy(userNameForDB);
					}
					investObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(investObj);
					entityManager.flush();
			    }
			}*/
			
		}
	}	
	
	public void createAssignInvestigator(AssignedInvestigatiorDetails assignedDetails, AssignInvestigatorDto element){
		
		try{
		if(assignedDetails != null && assignedDetails.getKey() == null){
			entityManager.persist(assignedDetails);			
			entityManager.flush();
		}
		else{
			entityManager.merge(assignedDetails);
			entityManager.flush();
		}
		element.setKey(assignedDetails.getKey());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Investigation updateUploadInvestigationReport(Investigation investigation, String  eventName, String completionRemarks, String userName){
		
		String userNameForDB = SHAUtils.getUserNameForDB(userName);
		
		 if(null != investigation){
			 
			investigation.setReportReceivedDate(new Timestamp(System.currentTimeMillis()));
			
			if(UploadInvestigationReportPresenter.SUBMIT_EVENT.equalsIgnoreCase(eventName)){
				investigation.setCompletionDate(new Timestamp(System.currentTimeMillis()));
			}	
			investigation.setCompletionRemarks(completionRemarks);
		}
	
		if(investigation!=null){
			if(userNameForDB != null){
				investigation.setModifiedBy(userNameForDB);
			}
			investigation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(investigation);
			entityManager.flush();
			
//			Claim claim = investigation.getClaim();
//			claim.setStatus(investigation.getStatus());
//			
//			entityManager.merge(claim);
//			entityManager.flush();
			return investigation;
		}
		return null;
	}
	
	public void updateReimbursementStatus(Long reimbursmentKey,Status status, Stage stage,String userName){
		
		Reimbursement reimbursement = getReimbursementByKey(reimbursmentKey);
		if(reimbursement != null && !(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
				|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))){
//			reimbursement.setStage(stage);
			reimbursement.setStatus(status);
			String userNameForDB = SHAUtils.getUserNameForDB(userName);
			reimbursement.setModifiedBy(userNameForDB);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	

	public void setDBOutcome(Investigation investigation,SearchProcessInvestigationInitiatedTableDTO bean,Boolean isDisapprove){
		Map<String, Object> workMap = (Map<String, Object>)bean.getDbOutArray();
		workMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE,investigation.getApprovedDate());
		
		//CR2019058 New submit task
		workMap.put(SHAConstants.INV_APPROVED_DATE,new Timestamp(System.currentTimeMillis()));

		DBCalculationService dbCalService = new DBCalculationService();
//		Object[] out = SHAUtils.getRevisedObjArrayForSubmit(workMap);
//		dbCalService.revisedInitiateTaskProcedure(out);
		Object[] out = SHAUtils.getRevisedObjArrayForAssignInvestigationSubmit(workMap);
		dbCalService.revisedAssignInvestigationTaskProcedure(out);

		
		if(isDisapprove){
			if(SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END.equals(workMap.get(SHAConstants.OUTCOME))){
				Long wkFlowKey = (Long) workMap.get(SHAConstants.WK_KEY);
				String intimationNo = (String) workMap.get(SHAConstants.INTIMATION_NO);

				dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
			}
		}
		
	}
	
	public FieldVisitRequest createFVRRecord(ProcessInvestigationInitiatedDto processInvestigationInitiatedDto, Investigation investigation)
	{
		try
			{
			Long intimationKey = investigation.getIntimation().getKey(); 
			FieldVisitRequest fvrRequest = new FieldVisitRequest();
	
			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			Intimation objIntimation = entityManager.find(Intimation.class, intimationKey);
			Claim claim = (Claim) findByIntimationKey.getSingleResult();
			
			
//			Stage objStage = new Stage();
//			objStage.setKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			
	
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
			
			if(claim != null && claim.getIntimation() != null){
				Intimation intimation = claim.getIntimation();
				Long hospital = intimation.getHospital();
				
				Hospitals hospitalById = getHospitalById(hospital);
				
				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
				if(tmpCPUCode != null){
					fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}
				
				
			}
			
			MastersValue value = new MastersValue();
			value.setKey(processInvestigationInitiatedDto.getFvrAllocationTo().getId());
			value.setValue(processInvestigationInitiatedDto.getFvrAllocationTo().getValue());
			fvrRequest.setAllocationTo(value);
			MastersValue priorityMasterValue = new MastersValue();
			priorityMasterValue.setKey(processInvestigationInitiatedDto.getPriority().getId());
			priorityMasterValue.setValue(processInvestigationInitiatedDto.getPriority().getValue());
			fvrRequest.setPriority(priorityMasterValue);
			fvrRequest.setIntimation(objIntimation);
			fvrRequest.setClaim(claim);
			fvrRequest.setStage(investigation.getStage());
			fvrRequest.setCreatedBy(processInvestigationInitiatedDto.getUserName());
			fvrRequest.setFvrTriggerPoints(processInvestigationInitiatedDto.getFvrTriggerPoints());
			fvrRequest.setPolicy(objIntimation.getPolicy());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(1L);
			fvrRequest.setOfficeCode(objIntimation.getPolicy().getHomeOfficeCode());	
			fvrRequest.setTransactionFlag("R");
			fvrRequest.setTransactionKey(investigation.getTransactionKey());
			fvrRequest.setStatus(fvrStatus);
//			fvrRequest.setStage(objStage);
			entityManager.persist(fvrRequest);
			entityManager.flush();
			
			saveTriggerPoints(fvrRequest,processInvestigationInitiatedDto.getFvrTriggerPtsList());			
			
			return fvrRequest;
			}
		catch(Exception e){
			e.printStackTrace();
		}
	return null;
	}
	
	public void saveTriggerPoints(FieldVisitRequest fieldVisitRequestObj, List<ViewFVRDTO> trgrPtsList){
		try{
			if(trgrPtsList != null && !trgrPtsList.isEmpty()){
				FvrTriggerPoint newFVRTRGPtsObj = null;
				
				for (ViewFVRDTO viewFVRDTO : trgrPtsList) {
					newFVRTRGPtsObj = new FvrTriggerPoint();
					newFVRTRGPtsObj.setFvrKey(fieldVisitRequestObj.getKey());
//					newFVRTRGPtsObj.setSeqNo(trgrPtsList.indexOf(viewFVRDTO)+1);
					newFVRTRGPtsObj.setRemarks(viewFVRDTO.getRemarks());
					newFVRTRGPtsObj.setDeleteFlag("N");
					newFVRTRGPtsObj.setCreatedDate(new Date());
//					newFVRTRGPtsObj.setDataFrom(SHAConstants.GALAXY_APP);
					entityManager.persist(newFVRTRGPtsObj);
					entityManager.flush();
				}				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setBPMOutcome(Investigation investigation,SearchProcessInvestigationInitiatedTableDTO bean,Boolean isDisapprove){/*
>>>>>>> removalofbpmn
		
		SubmitProcessInvestigationTask investigationTask = BPMClientContext.getInvestigationTask(bean.getUsername(), bean.getPassword());
		HumanTask humanTask = bean.getHumanTaskDTO();
		PayloadBOType payloadBoType = humanTask.getPayload();
		InvestigationType investigationType = payloadBoType.getInvestigation();
		if(isDisapprove){
			
			if(investigationType != null){
				investigationType.setStatus("REJECTED");
			}
			payloadBoType.setInvestigation(investigationType);
			
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType classification = payloadBoType.getClassification();
			classification.setSource(investigation.getStatus().getProcessValue());
			payloadBoType.setClassification(classification);
			
			ProcessActorInfoType processActorInfo = payloadBoType.getProcessActorInfo();
			if(processActorInfo != null){
				processActorInfo.setEscalatedByUser(bean.getUsername());
//				payloadBoType.setProcessActorInfo(processActorInfo);
			}
			
			humanTask.setPayload(payloadBoType);
			humanTask.setOutcome("REJECTED");
			
		}
		else{
		if(investigationType != null){
			investigationType.setStatus("APPROVE");
		}
		payloadBoType.setInvestigation(investigationType);
		humanTask.setPayload(payloadBoType);
		humanTask.setOutcome("APPROVE");
		}
		
		try{
		investigationTask.execute(bean.getUsername(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
	*/}
	
	/*public void setBPMOutcomeForAssign(Investigation investigation,AssignInvestigatorDto bean){
=======
	public void setBPMOutcomeForAssign(Investigation investigation,AssignInvestigatorDto bean){/*
>>>>>>> removalofbpmn
		
		SubmitAssignInvestigationTask assignTask = BPMClientContext.getSubmitAssignInvestigationTask(bean.getUserName(), bean.getPassWord());
		HumanTask humanTask = bean.getHumanTask();
		PayloadBOType payloadBoType = humanTask.getPayload();
		InvestigationType investigationType = payloadBoType.getInvestigation();
		if(investigationType != null){
			investigationType.setStatus("APPROVE");
		}
		payloadBoType.setInvestigation(investigationType);
		humanTask.setPayload(payloadBoType);
		humanTask.setOutcome("APPROVE");
		
		try{
		assignTask.execute(bean.getUserName(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
		
<<<<<<< HEAD
	}*/
	
public void submitAssignInvestigationTaskToDB(Investigation investigation,AssignInvestigatorDto bean){
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_ASSIGN_INVESTIGATION);
		
		try{
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);			
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

public String generateDraftLetter(DraftInvestigatorDto bean) {	
	
	String docToken = null;
	if (null != bean.getDocFilePath()
			&& !("").equalsIgnoreCase(bean.getDocFilePath())) {
		WeakHashMap dataMap = new WeakHashMap();
		dataMap.put("intimationNumber", bean.getClaimDto().getNewIntimationDto().getIntimationId());

		if (null != bean.getClaimDto().getKey()) {
			dataMap.put("claimNumber", bean.getClaimDto().getClaimId());
			if (null != bean.getClaimDto().getClaimType() && null != bean.getClaimDto().getClaimType().getId()) {
				if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
						.equals(bean.getClaimDto().getClaimType().getId())) {
					Preauth preauth = getPreauthClaimKey(bean.getClaimDto().getKey());
					if (null != preauth)
						dataMap.put("cashlessNumber",
								preauth.getPreauthId());
				}
			}
		}
		dataMap.put("filePath", bean.getDocFilePath());
		dataMap.put("docType", bean.getDocType());
		dataMap.put("docSources", SHAConstants.DOC_SOURCE_DRAFT_INVESTIGATION_LETTER);
		dataMap.put("createdBy", bean.getUserName());
		docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
		SHAUtils.setClearReferenceData(dataMap);
	}
    return  docToken;
}

	
public String generateAuthorizationLetter(AssignInvestigatorDto assignInvestigatorDto) {	
	
	DocumentGenerator docGen = new DocumentGenerator();
	
	ReportDto reportDto = new ReportDto();
	reportDto.setClaimId(assignInvestigatorDto.getClaimDto().getClaimId());
	
	List<AssignInvestigatorDto> dtoList = new ArrayList<AssignInvestigatorDto>();
	
	dtoList.add(assignInvestigatorDto);
	reportDto.setBeanList(dtoList);
	
	String templateName = "AuthorizationLetter";
	
	final String filePath = docGen.generatePdfDocument(templateName, reportDto);
	
	assignInvestigatorDto.setDocFilePath(filePath);
	assignInvestigatorDto.setDocType(SHAConstants.DOC_TYPE_ASSIGN_INVESTIGATION_LETTER);			
	assignInvestigatorDto.setDocSource(SHAConstants. DOC_SOURCE_ASSIGN_INVESTIGATION_LETTER);
	
	WeakHashMap dataMap = new WeakHashMap();
	dataMap.put("intimationNumber",assignInvestigatorDto.getClaimDto().getNewIntimationDto().getIntimationId());
	dataMap.put("claimNumber",assignInvestigatorDto.getClaimDto().getClaimId());
	dataMap.put("filePath", assignInvestigatorDto.getDocFilePath());
	dataMap.put("docType", assignInvestigatorDto.getDocType());
	dataMap.put("docSources", assignInvestigatorDto.getDocSource());
	dataMap.put("createdBy", "SYSTEM");
	String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
	SHAUtils.setClearReferenceData(dataMap);
    return  docToken;
}

public String generateReAssignAuthorizationLetter(AssignInvestigatorDto assignInvestigatorDto) {	
	
	DocumentGenerator docGen = new DocumentGenerator();
	
	ReportDto reportDto = new ReportDto();
	reportDto.setClaimId(assignInvestigatorDto.getClaimDto().getClaimId());
	
	List<AssignInvestigatorDto> dtoList = new ArrayList<AssignInvestigatorDto>();
	
	dtoList.add(assignInvestigatorDto);
	reportDto.setBeanList(dtoList);
	
	String templateName = "AuthorizationLetter";
	
	final String filePath = docGen.generatePdfDocument(templateName, reportDto);
	
	assignInvestigatorDto.setDocFilePath(filePath);
	assignInvestigatorDto.setDocType(SHAConstants.DOC_TYPE_RE_ASSIGN_INVESTIGATION_LETTER);			
	assignInvestigatorDto.setDocSource(SHAConstants. DOC_SOURCE_RE_ASSIGN_INVESTIGATION_LETTER);
	
	WeakHashMap dataMap = new WeakHashMap();
	dataMap.put("intimationNumber",assignInvestigatorDto.getClaimDto().getNewIntimationDto().getIntimationId());
	dataMap.put("claimNumber",assignInvestigatorDto.getClaimDto().getClaimId());
	dataMap.put("filePath", assignInvestigatorDto.getDocFilePath());
	dataMap.put("docType", assignInvestigatorDto.getDocType());
	dataMap.put("docSources", assignInvestigatorDto.getDocSource());
	dataMap.put("createdBy", "SYSTEM");
	String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
	SHAUtils.setClearReferenceData(dataMap);
    return docToken;
}
	
  public void submitInvestigationBPMForPreauth(Investigation investigation,AssignInvestigatorDto bean){/*
		
		SubmitAssignInvestigationCLTask assignTask = BPMClientContext.getSubmitAssignInvestigationCashlessTask(bean.getUserName(), bean.getPassWord());
		HumanTask humanTask = bean.getHumanTask();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBoType = humanTask.getPayloadCashless();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.investigation.InvestigationType investigationType = payloadBoType.getInvestigation();
		if(investigationType != null){
			investigationType.setStatus("APPROVE");
		}
		payloadBoType.setInvestigation(investigationType);
		humanTask.setPayloadCashless(payloadBoType);
		humanTask.setOutcome("APPROVE");
		
		try{
		assignTask.execute(bean.getUserName(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	*/}
	
	public void setBPMOutcomeForAckInvestigation(Investigation investigation, InvestigationCompletionDetailsDTO bean){/*
		
		SubmitAckInvestigationTask assignTask = BPMClientContext.getSubmitAckInvestigationTask(bean.getUserName(), bean.getPassWord());
		HumanTask humanTask = bean.getHumanTask();
		PayloadBOType payloadBoType = humanTask.getPayload();
		InvestigationType investigationType = payloadBoType.getInvestigation();
		if(investigationType != null){
			investigationType.setStatus("APPROVE");
		}
		payloadBoType.setInvestigation(investigationType);
		humanTask.setPayload(payloadBoType);
		humanTask.setOutcome("APPROVE");
		
		try{
		assignTask.execute(bean.getUserName(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	*/}
	
	public void setBPMOutcomeForUploadInvestigation(Investigation investigation,SearchUploadInvesticationTableDTO bean){/*
		
		SubmitUploadInvestigationTask uploadTask = BPMClientContext.getSubmitUploadInvestigationTask(bean.getUsername(), bean.getPassword());
		HumanTask humanTask = bean.getHumanTaskTableDTO();
		PayloadBOType payloadBoType = humanTask.getPayload();
		InvestigationType investigationType = payloadBoType.getInvestigation();
		if(investigationType != null){
			investigationType.setStatus("RECEIVED");
		}
		payloadBoType.setInvestigation(investigationType);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType classification = payloadBoType.getClassification();
		classification.setSource(investigation.getStatus().getProcessValue());
		payloadBoType.setClassification(classification);
		
		ProcessActorInfoType processActorInfo = payloadBoType.getProcessActorInfo();
		if(processActorInfo != null){
//			processActorInfo.setEscalatedByUser(bean.getUsername());
//			payloadBoType.setProcessActorInfo(processActorInfo);
		}
		
		humanTask.setPayload(payloadBoType);
		humanTask.setOutcome("RECEIVED");
		
		try{
		uploadTask.execute(bean.getUsername(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	*/}
	
	public void submitUploadInvestigationTaskToDB(Investigation investigation,SearchUploadInvesticationTableDTO bean){
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		if(null != wrkFlowMap)
		{
			String reimbReplyBy = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
			
			if(SHAConstants.MA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_UPLOAD_BACK_TO_MA);
			else if (SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_UPLOAD_BACK_TO_FA);
			else if (SHAConstants.ZMR_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_UPLOAD_BACK_TO_ZMR);
				//wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END);
			else if(SHAConstants.CURRENT_QUEUE_CLAIM_APPROVAL.equalsIgnoreCase(reimbReplyBy))
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_UPLOAD_BACK_TO_CLAP);
			else if (SHAConstants.PP_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy) || (SHAConstants.PE_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy)))
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END);
			try
			{
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				
				if(null != bean && null != bean.getPreauthDTO().getNewIntimationDTO() && !bean.getPreauthDTO().getNewIntimationDTO().getIsTataPolicy()){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_UPLOAD_TO_GRADING);
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_INVESTIGATION_REPORT_RECEIVED);
					
					Object[] objArrayForSubmit1 = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit1);
				}
				
				if(SHAConstants.MA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy) 
						||SHAConstants.ZMR_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy) 
						||SHAConstants.PP_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy) 
						||(SHAConstants.PE_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
						||(SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))){
				
					Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
					
					dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
			     }
			}
			catch(Exception e){
				e.printStackTrace();
			}
			//intiateGradingPayload(wrkFlowMap,investigation);
		}
		
		/*SubmitUploadInvestigationTask uploadTask = BPMClientContext.getSubmitUploadInvestigationTask(bean.getUsername(), bean.getPassword());
		HumanTask humanTask = bean.getHumanTaskTableDTO();
		PayloadBOType payloadBoType = humanTask.getPayload();
		InvestigationType investigationType = payloadBoType.getInvestigation();
		if(investigationType != null){
			investigationType.setStatus("RECEIVED");
		}
		payloadBoType.setInvestigation(investigationType);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType classification = payloadBoType.getClassification();
		classification.setSource(investigation.getStatus().getProcessValue());
		payloadBoType.setClassification(classification);
		
		ProcessActorInfoType processActorInfo = payloadBoType.getProcessActorInfo();
		if(processActorInfo != null){
//			processActorInfo.setEscalatedByUser(bean.getUsername());
//			payloadBoType.setProcessActorInfo(processActorInfo);
		}
		
		humanTask.setPayload(payloadBoType);
		humanTask.setOutcome("RECEIVED");
		
		try{
		uploadTask.execute(bean.getUsername(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
	}
	
	private void intiateGradingPayload(Map<String, Object> workTaskMap,Investigation investigation) {
		if(null != investigation && null != investigation.getClaim())
		{
			Claim claimObj = investigation.getClaim();
			Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());		
	
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObj, hospitals);
	
			Object[] inputArray = (Object[])arrayListForDBCall[0];
	
			
	
			DBCalculationService dbCalculationService = new DBCalculationService();

			Long workFlowKey = 0l;
			/*if(workTaskMap.containsKey(SHAConstants.WK_KEY) && workTaskMap.get(SHAConstants.WK_KEY) != null){
				workFlowKey = (Long)workTaskMap.get(SHAConstants.WK_KEY);
			}*/	
			inputArray[SHAConstants.INDEX_WORK_FLOW_KEY] = workFlowKey;
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_UPLOAD_TO_GRADING;
			inputArray[SHAConstants.INDEX_INVESTIGATION_KEY] = investigation.getKey();
			try
			{
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
				dbCalculationService.initiateTaskProcedure(parameter);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private Hospitals getHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);

		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();

		if (!CollectionUtils.isEmpty(resultList)){
			return resultList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<TmpInvestigation> getRepresentativeList(Long stateId, Long cityId, Long categoryId){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByStateCity");
		query.setParameter("cityId", cityId);
		query.setParameter("stateId", stateId);
		query.setParameter("categoryId", categoryId);		
		List<TmpInvestigation> resultList = query.getResultList();
		return resultList;		
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRepresentativeListWithoutFilter(){
		
		BeanItemContainer<SelectValue> nameContainer = null;
		
		Query query = entityManager.createNamedQuery("TmpInvestigation.findAll");
		List<TmpInvestigation> resultList = query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (TmpInvestigation tmpInvestigation : resultList) {
				SelectValue selected = new SelectValue();
				selected.setId(tmpInvestigation.getKey());
				selected.setValue(tmpInvestigation.getInvestigatorName());
				nameContainer.addBean(selected);
			}
		}
		
		return nameContainer;		
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getInvestigatorNameList(){
		
		BeanItemContainer<SelectValue> nameContainer = null;
		
		Query query = entityManager.createNamedQuery("TmpInvestigation.findAll");
		List<TmpInvestigation> resultList = query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (TmpInvestigation tmpInvestigation : resultList) {
				SelectValue selected = new SelectValue();
				selected.setId(tmpInvestigation.getKey());
				selected.setValue(tmpInvestigation.getInvestigatorCode()+" "+tmpInvestigation.getInvestigatorName());
				nameContainer.addBean(selected);
			}
		}
		
		return nameContainer;		
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getInvestigatorOutSourceNameList(){
		
		BeanItemContainer<SelectValue> nameContainer = null;
		
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByAllocation");
		query.setParameter("allocationTo",ReferenceTable.ALLOCATION_TO_OUTSOURCE);
		List<TmpInvestigation> resultList = query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (TmpInvestigation tmpInvestigation : resultList) {
				SelectValue selected = new SelectValue();
				selected.setId(tmpInvestigation.getKey());
				selected.setValue(tmpInvestigation.getInvestigatorCode()+" "+tmpInvestigation.getInvestigatorName());
				nameContainer.addBean(selected);
			}
		}
		
		return nameContainer;		
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getInvestigatorRVONameList(){
		
		BeanItemContainer<SelectValue> nameContainer = null;
		
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByAllocation");
		query.setParameter("allocationTo",ReferenceTable.ALLOCATION_TO_RVO);
		List<TmpInvestigation> resultList = query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (TmpInvestigation tmpInvestigation : resultList) {
				SelectValue selected = new SelectValue();
				selected.setId(tmpInvestigation.getKey());
				selected.setValue(tmpInvestigation.getInvestigatorCode()+" "+tmpInvestigation.getInvestigatorName());
				nameContainer.addBean(selected);
			}
		}
		
		return nameContainer;		
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getPrivateInvestigatorNameList(){
		
		BeanItemContainer<SelectValue> nameContainer = null;
		
		Query query = entityManager.createNamedQuery("MasPrivateInvestigator.findAll");
		List<MasPrivateInvestigator> resultList = query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			nameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (MasPrivateInvestigator masPrivateInvestigator : resultList) {
				SelectValue selected = new SelectValue();
				selected.setId(masPrivateInvestigator.getPrivateInvestigationKey());
				selected.setValue(masPrivateInvestigator.getPrivateInvestigationKey()+" "+masPrivateInvestigator.getInvestigatorName());
				nameContainer.addBean(selected);
			}
		}
		
		return nameContainer;		
	}
	
	@SuppressWarnings("unchecked")
	public TmpInvestigation getRepresentativeListByInvestigationKey(Long investigationKey){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByInvestigaitonKey");
		query.setParameter("investigationkey", investigationKey);
		List<TmpInvestigation> resultList = query.getResultList();
		if(!resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}	
	
	public MasPrivateInvestigator getPrivateRepresentativeListByInvestigationKey(Long privateInvestigationKey){
		Query query = entityManager.createNamedQuery("MasPrivateInvestigator.findByInvestigaitonKey");
		query.setParameter("privateInvestigationKey", privateInvestigationKey);
		List<MasPrivateInvestigator> resultList = query.getResultList();
		if(!resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	public List<AssignedInvestigatiorDetails> getAssignedListByInvestigationKey(Long investigationKey)
	{
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		try{
			Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByInvestigaitonKey");
			query.setParameter("investigationkey", investigationKey);
			resultList = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
			return resultList;
	}

	public AssignedInvestigatiorDetails getAssignedInvestigByKey(Long assignedKey)
	{
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		if(assignedKey != null){
			try{
				Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByAssignInvestigaitonKey");
				query.setParameter("key", assignedKey);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(resultList != null && !resultList.isEmpty()){
		
			return resultList.get(0);
		}
		return null;
		
	}
	public List<AssignedInvestigatiorDetails> getAssignedInvestigByInvestigatorCode(String investigCode){
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		if(investigCode != null){
			try{
				Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByInvestigaitonCode");
				query.setParameter("investigatorCode", investigCode);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
	
/**
 * 	Part of CR R0767
 * @param code String
 * @return assignedCount int
 */
	public int getPendingInvCountByInvestigatorCode(String code){
		int count = 0;
		
		List<AssignedInvestigatiorDetails> invList = getAssignedInvestigByInvestigatorCode(code);
		
		if(invList != null && !invList.isEmpty()){
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : invList) {
				if(ReferenceTable.ASSIGN_INVESTIGATION.equals(assignedInvestigatiorDetails.getStatus().getKey()) || ReferenceTable.RE_ASSIGN_INVESTIGATION_STATUS.equals(assignedInvestigatiorDetails.getStatus().getKey())){
					count++;
				}
			}
		}		
//		count = 9;  //TODO for Testing purpose
		return count;
	}
	
	
	public List<AssignedInvestigatiorDetails> getAssignedInvestigByInvestigatorCodeAndIntimation(String investigCode, Long intimationKey){
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		if(investigCode != null){
			try{
				Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByInvestigaitonCodeNIntimation");
				query.setParameter("investigatorCode", investigCode);
				query.setParameter("intimationKey", intimationKey);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DraftTriggerPointsToFocusDetailsTableDto> getInvestigationDetailsBasedOnInvestigationKey(Long investigationKey)
	{
		 /*final CriteriaBuilder investigationBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<InvestigationDetails> investigationQuery = investigationBuilder.createQuery(InvestigationDetails.class);
		Root<InvestigationDetails> root = investigationQuery.from(InvestigationDetails.class);*/
			Session session = (Session)entityManager.getDelegate();
			List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList = session.createCriteria(InvestigationDetails.class)
					.add(Restrictions.eq("investigation.key", investigationKey))
					.addOrder(Order.asc("sno"))
					.setProjection(Projections.projectionList()
					.add(Projections.property("DraftOrReDraftRemarks"), "remarks"))
					.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(DraftTriggerPointsToFocusDetailsTableDto.class)).list();
			return triggerPointsList;

	}
	
	
	public void submitInvestigationGrade(Investigation investigation,AssignInvestigatorDto bean){
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_INVESTIGATION_GRADING);
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();		
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	}
	
	public void submitRODInvestigationDetails(List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList, Investigation investigation, DraftInvestigatorDto bean){
		
		if(triggerPointsList != null && !triggerPointsList.isEmpty()){
		
		for(DraftTriggerPointsToFocusDetailsTableDto dto: triggerPointsList){
			InvestigationDetails details = new InvestigationDetails();
			details.setInvestigation(investigation);
			details.setReimbursementKey(bean.getRodKey());
			if(bean.getReimbReqBy() != null && !bean.getReimbReqBy().isEmpty()){
				if(bean.getReimbReqBy().equalsIgnoreCase(SHAConstants.FA_CURRENT_QUEUE) || bean.getReimbReqBy().equalsIgnoreCase(SHAConstants.MA_CURRENT_QUEUE)){
				details.setProcessType(SHAConstants.REIMBURSEMENT_CHAR);
				}
			}else{
				details.setProcessType(SHAConstants.CASHLESS_CHAR);	
			}
			details.setSno(dto.getSno().longValue());
			details.setDraftOrReDraftRemarks(dto.getRemarks());
			details.setCreatedDate(new Date());
			details.setDeletedFlag(dto.getDeltedFlag());
			entityManager.persist(details);
			entityManager.flush();
			
		
		}
	}
	}
	
	
	@SuppressWarnings("unchecked")
	public void submitDraftInvestigation(Investigation investigation,
			DraftInvestigatorDto bean) {
		

		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean
				.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DRAFT_INVESTIGATION);
		//wrkFlowMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE,investigation.getApprovedDate());

		//CR2019058 New submit task 
		wrkFlowMap.put(SHAConstants.INV_REQ_DRAFTED_DATE, new Timestamp(System.currentTimeMillis()));

//		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForAssignInvestigationSubmit(wrkFlowMap);

		DBCalculationService dbCalService = new DBCalculationService();
//		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedAssignInvestigationTaskProcedure(objArrayForSubmit);

	}

	
	/**
	 * CR R1163 
	 * @param DraftInvestigatorDto 
	 */
	
	public String uploadInvLetterToDms(DraftInvestigatorDto bean) {
		String docToken = null;
		if (null != bean.getDocFilePath()
				&& !("").equalsIgnoreCase(bean.getDocFilePath())) {
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber", bean.getClaimDto().getNewIntimationDto().getIntimationId());
		
			if (null != bean.getClaimDto().getKey()) {
				dataMap.put("claimNumber", bean.getClaimDto().getClaimId());
				if (null != bean.getClaimDto().getClaimType() && null != bean.getClaimDto().getClaimType().getId()) {
					if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
							.equals(bean.getClaimDto().getClaimType().getId())) {
						Preauth preauth = getPreauthClaimKey(bean.getClaimDto().getKey());
						if (null != preauth)
							dataMap.put("cashlessNumber",
									preauth.getPreauthId());
					}
				}
			}
			dataMap.put("filePath", bean.getDocFilePath());
			dataMap.put("docType", bean.getDocType());
			dataMap.put("docSources", bean.getDocSource());
//			dataMap.put("docSources", SHAConstants.DOC_SOURCE_DRAFT_INVESTIGATION_LETTER);
			dataMap.put("createdBy", bean.getUserName());
			docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
			SHAUtils.setClearReferenceData(dataMap);
		}
		return docToken;
	}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();

		if (preauthList != null && !preauthList.isEmpty()) {
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}
	public TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
				
		}
		return null;
	}
	
//	@SuppressWarnings("unchecked")
//	public Boolean getInvestigationByClaim(Long claimKey) {
//		Query findAll = entityManager.createNamedQuery(
//				"Investigation.findByClaimKey").setParameter("claimKey",
//				claimKey);
//		List<Investigation> investigationList = (List<Investigation>) findAll
//				.getResultList();
//		if(null != investigationList && !investigationList.isEmpty())
//		{
//			return true;
//		}
//		
//		return false;
//	}

	@SuppressWarnings("unchecked")
	public Boolean getInvestigationAvailableForTransactionKey(Long rodKey) {
//	public Boolean getInvestigationByClaim(Long claimKey) {
//		Query findAll = entityManager.createNamedQuery(
//				"Investigation.findByClaimKey").setParameter("claimKey",
//				claimKey);
		boolean isavailable = false;
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByTransactionKey").setParameter("transactionKey", rodKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList(); 
		if(null != investigationList && !investigationList.isEmpty())
		{
			if(investigationList.get(0).getStatus() != null && 
					!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.INVESTIGATION_GRADING.equals(investigationList.get(0).getStatus().getKey())){
				isavailable = true;
				return isavailable; 
			}

			/*if(investigationList.get(0).getStatus() != null && !ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigationList.get(0).getStatus().getKey())
					){
					isavailable = true;	
			} */
		}
		
		return isavailable;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public Boolean getInvestigationByExcludingCurrentInvestigation(Long claimKey,Long currerntInvestigationKey) {
		Query findAll = entityManager.createNamedQuery("Investigation.findByClaimKey").setParameter("claimKey",
				claimKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList();
		if(null != investigationList && !investigationList.isEmpty())
		{
			
			for (Investigation investigation : investigationList) {
				if(!investigation.getKey().equals(currerntInvestigationKey) && investigation.getStatus() != null && 
						( (!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())) || 
								           (!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey())) ))
				{
					return true;
				}
			}
			
		}
		return false;
			
	}
		

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Investigation create(InitiateInvestigationDTO a_initiateInvestigationDTO,Investigation investigationValue,String userName, String password, Claim claim) {
		Investigation investigation = new Investigation();
		if (a_initiateInvestigationDTO != null) {
			SelectValue selectValue = a_initiateInvestigationDTO
					.getAllocationTo();
			MastersValue mastersValue = new MastersValue();
			if(null != selectValue)
			{
				mastersValue.setKey(selectValue.getId());
				mastersValue.setValue(selectValue.getValue());
				investigation.setAllocationTo(mastersValue);
			}
		
			investigation.setReasonForReferring(a_initiateInvestigationDTO
					.getReasonForRefering());
			investigation.setTriggerPoints(a_initiateInvestigationDTO
					.getTriggerPointsToFocus());
			investigation.setPolicy(entityManager.find(Policy.class,
					a_initiateInvestigationDTO.getPolicyKey()));
//			investigation.setClaim(entityManager.find(Claim.class,
//					a_initiateInvestigationDTO.getClaimKey()));
			investigation.setClaim(claim);
			investigation.setIntimation(entityManager.find(Intimation.class,
					a_initiateInvestigationDTO.getIntimationkey()));			
			investigation.setStage(a_initiateInvestigationDTO.getStage());
			investigation.setStatus(a_initiateInvestigationDTO.getStatus());
			String userNameForDB = SHAUtils.getUserNameForDB(userName);
			investigation.setCreatedBy(userNameForDB);
			investigation.setTransactionKey(a_initiateInvestigationDTO.getTransactionKey());
			investigation.setTransactionFlag(a_initiateInvestigationDTO.getTransactionFlag());
			entityManager.persist(investigation);
			entityManager.flush();
			
			a_initiateInvestigationDTO.setTransactionKey(investigation.getKey());
			
			if((ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
				setBPMforInvestigation(investigation, userName, password);	
			}
			
			return investigation;
		}
		return investigation;
	}
	
	public StageInformation getStageInfoByStatusNStage(Long stageKey, Long statusKey, Long transacKey){
		StageInformation stageObj = null;
		try{
			Query stageQuery = entityManager.createNamedQuery("StageInformation.findByReimbStageNStatus");
			stageQuery.setParameter("rodKey",transacKey);
			stageQuery.setParameter("stageKey", stageKey);
			stageQuery.setParameter("statusKey", statusKey);
			List<StageInformation> stageObjList = (List<StageInformation>)stageQuery.getResultList();
			if(stageObjList != null && !stageObjList.isEmpty()){
				stageObj = stageObjList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return stageObj;
	}
	
	public StageInformation getStageInfoByClaimStatusNStage(Long stageKey, Long statusKey, Long claimKey){
		StageInformation stageObj = null;
		try{
			Query stageQuery = entityManager.createNamedQuery("StageInformation.findClaimByStatusKey");
			stageQuery.setParameter("claimkey",claimKey);
			stageQuery.setParameter("stageKey", stageKey);
			stageQuery.setParameter("statusKey", statusKey);
			List<StageInformation> stageObjList = (List<StageInformation>)stageQuery.getResultList();
			if(stageObjList != null && !stageObjList.isEmpty()){
				stageObj = stageObjList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return stageObj;
	}
	
	public List<AssignedInvestigationHistory> getInvAssignementHistoryByAssignedKey(Long invAssignKey){
		
		List<AssignedInvestigationHistory> invAssignHistoryList = new ArrayList<AssignedInvestigationHistory>();
		try{
			Query stageQuery = entityManager.createNamedQuery("AssignedInvestigationHistory.findByAssignKey");
			stageQuery.setParameter("invAssignkey", invAssignKey);
			invAssignHistoryList = (List<AssignedInvestigationHistory>)stageQuery.getResultList();
			if(invAssignHistoryList != null && ! invAssignHistoryList.isEmpty()){
				for (AssignedInvestigationHistory assignedInvestigationHistory : invAssignHistoryList) {
					assignedInvestigationHistory.setsNo(invAssignHistoryList.indexOf(assignedInvestigationHistory)+1);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return invAssignHistoryList;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Investigation submitInvestigation(InitiateInvestigationDTO a_initiateInvestigationDTO,PreauthDTO bean) {
		if (a_initiateInvestigationDTO != null) {
			Investigation investigation = new Investigation();
			SelectValue selectValue = a_initiateInvestigationDTO
					.getAllocationTo();
			MastersValue mastersValue = new MastersValue();
			mastersValue.setKey(selectValue.getId());
			mastersValue.setValue(selectValue.getValue());
			investigation.setAllocationTo(mastersValue);
			investigation.setReasonForReferring(a_initiateInvestigationDTO
					.getReasonForRefering());
			investigation.setTriggerPoints(a_initiateInvestigationDTO
					.getTriggerPointsToFocus());
			investigation.setPolicy(entityManager.find(Policy.class,
					a_initiateInvestigationDTO.getPolicyKey()));
			investigation.setClaim(entityManager.find(Claim.class,
					a_initiateInvestigationDTO.getClaimKey()));
			investigation.setIntimation(entityManager.find(Intimation.class,
					a_initiateInvestigationDTO.getIntimationkey()));			
			investigation.setStage(a_initiateInvestigationDTO.getStage());
			investigation.setStatus(a_initiateInvestigationDTO.getStatus());
			String userNameForDB = SHAUtils.getUserNameForDB(bean.getStrUserName());
			investigation.setCreatedBy(userNameForDB);
			investigation.setTransactionKey(a_initiateInvestigationDTO.getTransactionKey());
			investigation.setTransactionFlag("R");
			//CR2019058 persisting initiated date
			Date currentDate = new Date();
			investigation.setInitiatedDate(new Timestamp(currentDate.getTime()));
			entityManager.persist(investigation);
			entityManager.flush();
			
			Reimbursement reimbursement = getReimbursementByKey(bean.getKey());
			if(null != reimbursement){
				
				reimbursement.setStage(a_initiateInvestigationDTO.getStage());
				reimbursement.setStatus(a_initiateInvestigationDTO.getStatus());
				entityManager.merge(investigation);
				entityManager.flush();
			}
			//investigationValue = investigation;
			
//			setBPMforInvestigation(investigation, userName, password);
			
			return investigation;
		}
		return null;
	}
	
	
	
	public void updateInvestigationStatus(Long investigationKey,InvesAndQueryAndFvrParallelFlowTableDTO cancelledFvrInvsOrquery,PreauthDTO bean,Long assignInvsKey)
	{
		Investigation investigation = getByInvestigationKey(investigationKey);
		AssignedInvestigatiorDetails assignInvs = getAssignedInvestigByKey(null != assignInvsKey ? assignInvsKey : 0l);

		if(null != assignInvs){
			
			if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
				
					if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
						
						assignInvs.setInvsProceedWithoutReport(SHAConstants.YES_FLAG);
					}
	
					if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
				
						assignInvs.setInvsCancelRequest(SHAConstants.YES_FLAG);
					}
	
					if(null != cancelledFvrInvsOrquery.getCancelRemarks()){
				
						assignInvs.setInvsCancelRemarks(cancelledFvrInvsOrquery.getCancelRemarks());
					}
					assignInvs.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					assignInvs.setModifiedBy(bean.getStrUserName());
					
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
					assignInvs.setStage(stage);
					
					Status status = new Status();
					status.setKey(ReferenceTable.PARALLEL_INVES_CANCELLED);
					assignInvs.setStatus(status);				
			}
			
			entityManager.merge(assignInvs);
		}
		else{
			if(null != investigation){
				
				if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
					
					investigation.setInvsProceedWithoutReport(SHAConstants.YES_FLAG);
				}

				if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
			
					investigation.setInvsCancelRequest(SHAConstants.YES_FLAG);
				}

				if(null != cancelledFvrInvsOrquery.getCancelRemarks()){
			
					investigation.setInvsCancelRemarks(cancelledFvrInvsOrquery.getCancelRemarks());
				}
				
				investigation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				investigation.setModifiedBy(bean.getStrUserName());

				if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
					investigation.setStage(stage);	
					
					Status status = new Status();
					status.setKey(ReferenceTable.PARALLEL_INVES_CANCELLED);
					investigation.setStatus(status);				
				}
				
				entityManager.merge(investigation);
				
			}
		}
		
		int uploadCompletedStatus = 0;
		List<AssignedInvestigatiorDetails> assignedInvList = getAssignedListByInvestigationKey(investigationKey);
		if(assignedInvList != null && !assignedInvList.isEmpty()){
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : assignedInvList) {
					if(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(assignedInvestigatiorDetails.getStatus().getKey()) 
							|| ReferenceTable.INVESTIGATION_GRADING.equals(assignedInvestigatiorDetails.getStatus().getKey())
							|| ReferenceTable.PARALLEL_INVES_CANCELLED.equals(assignedInvestigatiorDetails.getStatus().getKey())){
						uploadCompletedStatus++;
					}
										
				}
				
				if(uploadCompletedStatus == assignedInvList.size()){
					if(investigation!=null){
						Stage stage = new Stage();
						stage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE);
						Status status = new Status();
						status.setKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);					
						investigation.setStage(stage);
						investigation.setStatus(status);
						
						if(bean.getStrUserName() != null){
							investigation.setModifiedBy(bean.getStrUserName());
						}
						
						investigation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(investigation);
						entityManager.flush();
						updateReimbursementStatus(bean.getKey(), investigation.getStatus(), investigation.getStage(), bean.getStrUserName());
				    }
			    }
		}
	}
	
	@SuppressWarnings("unchecked")	
	public CashlessWorkFlow getByWorkFlowKey(Long workFlowKey){
		Query findAll = entityManager.createNamedQuery(
				"CashlessWorkFlow.findByKey").setParameter("workFlowKey",
						workFlowKey);
		List<CashlessWorkFlow> workFlowList = (List<CashlessWorkFlow>) findAll
				.getResultList();
		if(!workFlowList.isEmpty()){
			return workFlowList.get(0);
		}else{
			return null;
		}
		
	}
	
	public void submitInitiateLevelInvestigationTriggerPointsDetails(List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList, Investigation investigation, InitiateInvestigationDTO bean){
		
			if(triggerPointsList != null && !triggerPointsList.isEmpty()){
			
			for(DraftTriggerPointsToFocusDetailsTableDto dto: triggerPointsList){
				InvestigationDetails details = new InvestigationDetails();
				details.setInvestigation(investigation);
				details.setReimbursementKey(0l);
				if ((ReferenceTable.PREAUTH_STAGE
						.equals(bean.getStage().getKey())) ||(ReferenceTable.ENHANCEMENT_STAGE
						.equals(bean.getStage().getKey()))
						|| ReferenceTable.PREAUTH_STAGE
								.equals(bean.getStage().getKey())) {
					details.setProcessType(SHAConstants.CASHLESS_CHAR);	
				}else{
					details.setProcessType(SHAConstants.REIMBURSEMENT_CHAR);
				}
				details.setSno(dto.getSno().longValue());
				details.setDraftOrReDraftRemarks(dto.getRemarks());
				details.setCreatedDate(new Date());
				details.setDeletedFlag(dto.getDeltedFlag());
				entityManager.persist(details);
				entityManager.flush();
				
			
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getInvestigationPendingForClaim(Long claimKey,String transactionFlag,PreauthDTO bean) {
		boolean isavailable = false;
		
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKeyAndTransactionFlag");
				findAll.setParameter("claimKey", claimKey);
				findAll.setParameter("transactionFlag", transactionFlag);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList(); 
		if(null != investigationList && !investigationList.isEmpty())
		{
			bean.setIsInvsRaised(Boolean.TRUE);
			if(investigationList.get(0).getStatus() != null && 
					!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigationList.get(0).getStatus().getKey()) &&
					!ReferenceTable.INVESTIGATION_GRADING.equals(investigationList.get(0).getStatus().getKey())){
				isavailable = true;
				return isavailable; 
			}

		}
		
		return isavailable;
	}
	
	public List<AssignedInvestigatiorDetails> getAssignedInvestionByInvestigatorCodeAndIntimation(String investigCode, Long intimationKey){
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		if(investigCode != null){
			try{
				Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByInvestigaitonCodeAndIntimation");
				query.setParameter("investigatorCode", investigCode);
				query.setParameter("intimationKey", intimationKey);
				resultList = query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
	
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if (singleResult != null && !singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	public Investigation getLatestInvsByClaimKey(Long claimKey) {
		Query findAll = entityManager.createNamedQuery("Investigation.findLatestByClaimKey").setParameter("claimKey",
				claimKey);
		List<Investigation> invList = (List<Investigation>) findAll.getResultList();
		if(null != invList && !invList.isEmpty()){
			
			return invList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<InvestigationDetails> getCashlessInvsTriggerPoints(Long investigationKey) {
		Query findAll = entityManager.createNamedQuery("InvestigationDetails.findByInvestigationKey").setParameter("investigationKey",
				investigationKey);
		List<InvestigationDetails> invList = (List<InvestigationDetails>) findAll.getResultList();
		if(null != invList && !invList.isEmpty()){
			
			return invList;
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getInvestigationAvailableForClaim(Long claimKey) {
		boolean isavailable = false;
		Query findAll = entityManager.createNamedQuery(
				"Investigation.findByClaimKey").setParameter("claimKey", claimKey);
		List<Investigation> investigationList = (List<Investigation>) findAll
				.getResultList(); 
		if(null != investigationList && !investigationList.isEmpty())
		{
			for (Investigation investigation : investigationList) {
				
				if(investigation.getStatus() != null && 
						!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) &&
						!ReferenceTable.UPLOAD_INVESIGATION_COMPLETED.equals(investigation.getStatus().getKey()) &&
						!ReferenceTable.PARALLEL_INVES_CANCELLED.equals(investigation.getStatus().getKey()) &&
						!ReferenceTable.INVESTIGATION_GRADING.equals(investigation.getStatus().getKey())){
					isavailable = true;
					return isavailable; 
				}
			}
		}
		return isavailable;
	}
	public MasPrivateInvestigator getPrivateInvestigatorByKey(Long privateInvestigationKey){
		Query query = entityManager.createNamedQuery("MasPrivateInvestigator.findByInvestigaitonKey").setParameter("privateInvestigationKey", privateInvestigationKey);
		List<MasPrivateInvestigator> tmpInvestigationList = query.getResultList();
		if(!tmpInvestigationList.isEmpty()){
			return tmpInvestigationList.get(0);
		}else{
			return null;
		}
	}
	
	public AssignedInvestigatiorDetails insertInvsDetailsForTataTrust(Investigation investigation,InitiateInvestigationDTO initiateInvesDTO,PreauthDTO bean){
		
		AssignedInvestigatiorDetails assignedDetails = new AssignedInvestigatiorDetails();
		
		if (investigation != null && (initiateInvesDTO != null)) {

				List<TmpInvestigation> tmpInvestigationList = getTataTrustInvsDtls();
			
			assignedDetails.setInvestigation(investigation);
			
			if(tmpInvestigationList != null && !tmpInvestigationList.isEmpty()){
				
				for (TmpInvestigation tmpInvestigation : tmpInvestigationList) {
					assignedDetails.setInvestigatorCode(tmpInvestigation
							.getInvestigatorCode());
					assignedDetails.setInvestigatorName(tmpInvestigation
							.getInvestigatorName());
				}
			}
			
			Reimbursement reimbObj = getReimbursementByKey(investigation.getTransactionKey());

			if (reimbObj != null) {
				assignedDetails.setReimbursement(reimbObj);
			}
			Date date = new Date();
			assignedDetails.setCreatedDate(date);
			assignedDetails.setCreatedBy(bean.getStrUserName());
			Stage stage = getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
			Status status = getStatusByPreauth(ReferenceTable.ASSIGN_INVESTIGATION);
			assignedDetails.setStage(stage);
			assignedDetails.setStatus(status);
			if (initiateInvesDTO.getAllocationTo() != null) {
				SelectValue allocationToSelectValue = initiateInvesDTO
						.getAllocationTo();
				MastersValue allocationTo = new MastersValue();
				allocationTo
						.setKey(allocationToSelectValue.getId());
				allocationTo.setValue(allocationToSelectValue
						.getValue());
				assignedDetails.setAllocationTo(allocationTo);
			}

			entityManager.persist(assignedDetails);			
			entityManager.flush();
		
		}
		return assignedDetails;
	}
	
	public Stage getStageByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery("Stage.findByKey")
				.setParameter("stageKey", key);

		Stage stage = (Stage) findByKey.getSingleResult();
		if (stage != null) {
			return stage;
		}
		return null;
	}
	
	public Status getStatusByPreauth(Long key) {

		Query findByKey = entityManager.createNamedQuery("Status.findByKey")
				.setParameter("statusKey", key);

		Status status = (Status) findByKey.getSingleResult();
		if (status != null) {
			return status;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<TmpInvestigation> getTataTrustInvsDtls(){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findTataTrust");
		List<TmpInvestigation> resultList = query.getResultList();
		if(!resultList.isEmpty()){
			return resultList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TmpInvestigation getTmpInvestigationByInactiveInvestigatorCode(String code){
		Query query = entityManager.createNamedQuery("TmpInvestigation.findByInvestigaitonCodeInactive").setParameter("investigatorCode", code);
		List<TmpInvestigation> tmpInvestigationList = query.getResultList();
		if(!tmpInvestigationList.isEmpty()){
			return tmpInvestigationList.get(0);
		}else{
			return null;
		}
	}
	
}
