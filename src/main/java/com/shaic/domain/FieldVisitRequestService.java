package com.shaic.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.ClaimRemarksAlerts;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.fieldVisitPage.FieldVisitDTO;
import com.shaic.claim.fieldVisitPage.TmpFVRDTO;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetailedview.FvrDetailedViewDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.zybnet.autocomplete.server.AutocompleteField;

@Stateless
public class FieldVisitRequestService {

	@PersistenceContext
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<TmpFVRDTO> getTmpFVRDetails(AutocompleteField<TmpFVRDTO> field,
			String query) {
		if (query != "" && field != null) {
			Query tmpFVRQuery = entityManager.createNamedQuery(
					"TmpFvR.findByRepresentativeName").setParameter(
					"representativeName", "%" + query.toLowerCase() + "%");
			List<TmpFvR> tmpFVR = tmpFVRQuery.getResultList();
			List<TmpFVRDTO> tmpFVRDTOList = new ArrayList<TmpFVRDTO>();
			if (tmpFVR != null && !tmpFVR.isEmpty()) {
				for (TmpFvR fvr : tmpFVR) {
					TmpFVRDTO tmpFVRDTO = new TmpFVRDTO();
					tmpFVRDTO.setRepresentativeCode(fvr.getRepresentiveCode());
					tmpFVRDTO.setRepresentativeName(fvr.getRepresentiveName());
					if(fvr.getPhoneNumber() != null){
						tmpFVRDTO.setPhoneNumber(fvr.getPhoneNumber());
					}
					if(fvr.getMobileNumber() != null){
						tmpFVRDTO.setMobileNumber(fvr.getMobileNumber());
					}
					tmpFVRDTOList.add(tmpFVRDTO);
				}
			}
			if (!tmpFVRDTOList.isEmpty()) {
				return tmpFVRDTOList;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public TmpFVRDTO getTmpFVRDetails(String represenativeCode) {
		if (represenativeCode != "") {
			Query tmpFVRQuery = entityManager.createNamedQuery(
					"TmpFvR.findByCode")
					.setParameter("code", represenativeCode);
			TmpFvR tmpFVR = (TmpFvR) tmpFVRQuery.getSingleResult();
			TmpFVRDTO tmpFVRDTO = new TmpFVRDTO();
			if (tmpFVR != null) {
				tmpFVRDTO.setRepresentativeCode(tmpFVR.getRepresentiveCode());
				tmpFVRDTO.setRepresentativeName(tmpFVR.getRepresentiveName());
				if(tmpFVR.getPhoneNumber() != null){
					tmpFVRDTO.setPhoneNumber(tmpFVR.getPhoneNumber());
				}
				if(tmpFVR.getMobileNumber() != null){
					tmpFVRDTO.setMobileNumber(tmpFVR.getMobileNumber());
				}

			}
			return tmpFVRDTO;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<FieldVisitRequest> getFieldVisitRequestByKey(Long key) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey", key);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			for (FieldVisitRequest fieldVisitRequest : fvrList) {
				entityManager.refresh(fieldVisitRequest);
			}
			
		}
		
		return fvrList;
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFieldVisitByKey(Long key) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey", key);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			return fvrList.get(0);
			
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFieldVisitByPreauthKey(Long preauthKey){
		
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByTransactionKey").setParameter("transactionKey", preauthKey);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			
				entityManager.refresh(fvrList.get(0));
				return fvrList.get(0);
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFieldVisitByReimbursementKey(Long preauthKey){
		
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByReimbursmentKey").setParameter("transactionKey", preauthKey);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			
				entityManager.refresh(fvrList.get(0));
				return fvrList.get(0);
		}
		
		return null;
		
	}

	@SuppressWarnings("unchecked")
	public List<FieldVisitRequest> getFieldVisitRequestByIntimationKey(
			Long intimationKey) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByIntimationKey").setParameter(
				"intimationKey", intimationKey);
		return (List<FieldVisitRequest>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<FieldVisitRequest> getFieldVisitByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByClaimKey").setParameter("claimKey",
						claimKey);
		return (List<FieldVisitRequest>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getPendingFieldVisitByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByClaimKey").setParameter("claimKey",
						claimKey);
		List<FieldVisitRequest> resultList = (List<FieldVisitRequest>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()){
			for (FieldVisitRequest fieldVisitRequest : resultList) {
				if(ReferenceTable.INITITATE_FVR.equals(fieldVisitRequest.getStatus().getKey())){
					return fieldVisitRequest;
				}
			}
		}
		
		return  null;
	}
	

	public FieldVisitDTO getDetailsByKey(Long fvrKey) {
		List<FieldVisitRequest> fieldVisitRequestList = getFieldVisitRequestByKey(fvrKey);
		if (!fieldVisitRequestList.isEmpty()) {
			FieldVisitRequest fieldVisitRequest = fieldVisitRequestList.get(0);
			FieldVisitDTO fieldVisitDto = new FieldVisitDTO();
			fieldVisitDto.setKey(fieldVisitRequest.getKey());
			fieldVisitDto.setApprovedFVR(fieldVisitRequest
					.getFvrTriggerPoints());
			fieldVisitDto.setAllocateDoctor(fieldVisitRequest.getAllocationTo()
					.getValue());
			fieldVisitDto.setName(fieldVisitRequest.getRepresentativeName());
			fieldVisitDto.setRepresentativeCode(fieldVisitRequest
					.getRepresentativeCode());
			
			if(fieldVisitRequest.getAssignTo() != null){
				fieldVisitDto.setAssignTo(fieldVisitRequest.getAssignTo().getValue());
			}
			if(fieldVisitRequest.getPriority() != null){
				fieldVisitDto.setPriority(fieldVisitRequest.getPriority().getValue());
			}
			fieldVisitDto.setStageId(fieldVisitRequest.getStage().getKey());
			return fieldVisitDto;
		}
		return null;
	}

//	@SuppressWarnings("unused")
//	public Boolean submitAssignEvent(FieldVisitDTO bean, String option,
//			SearchFieldVisitTableDTO searchTableDTO) {
//		Query query = entityManager.createNamedQuery(
//				"FieldVisitRequest.findByKey").setParameter("primaryKey",
//				bean.getKey());
//		FieldVisitRequest fieldVisitRequestList = (FieldVisitRequest) query
//				.getSingleResult();
//		Claim claim = getEntityManager().find(Claim.class,
//				fieldVisitRequestList.getClaim().getKey());
//		if (fieldVisitRequestList != null) {
//			Status status = new Status();
//			Stage stage = new Stage();
//			status.setKey(51L);
//			stage.setKey(18L);
//			fieldVisitRequestList.setExecutiveComments(bean.getComments());
//			fieldVisitRequestList.setRepresentativeCode(bean
//					.getRepresentativeCode());
//			fieldVisitRequestList.setRepresentativeName(bean.getName());
//			fieldVisitRequestList.setStage(stage);
//			fieldVisitRequestList.setStatus(status);
//			fieldVisitRequestList.setFvrReceivedDate(new Date());
//			entityManager.merge(fieldVisitRequestList);
//			entityManager.flush();
//			HumanTask humanTask = searchTableDTO.getHumanTask();
//			if (option.equals("ASSIGN")) {
//				humanTask.setOutcome("ASSIGN");
//			}
//			return initiateBPM(fieldVisitRequestList, claim, option, humanTask,
//					bean);
//		}
//		return false;
//	}

/*	public Boolean submitAssign(FieldVisitDTO bean, String option,
			SearchFieldVisitTableDTO searchTableDTO) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey",
				bean.getKey());
		FieldVisitRequest fieldVisitRequestList = (FieldVisitRequest) query
				.getSingleResult();
		Claim claim = getEntityManager().find(Claim.class,
				fieldVisitRequestList.getClaim().getKey());
		if (fieldVisitRequestList != null) {
			Status status = new Status();
//			Stage stage = new Stage();
			status.setKey(ReferenceTable.ASSIGNFVR);
//			stage.setKey(18L);
			fieldVisitRequestList.setExecutiveComments(bean.getComments());
			fieldVisitRequestList.setRepresentativeCode(bean
					.getRepresentativeCode());

			fieldVisitRequestList.setRepresentativeName(bean.getName());
//			fieldVisitRequestList.setStage(stage);
			fieldVisitRequestList.setStatus(status);
			fieldVisitRequestList.setAssignedDate(new Timestamp(System.currentTimeMillis()));
//			fieldVisitRequestList.setFvrReceivedDate(new Date());
			String userNameForDB = SHAUtils.getUserNameForDB(searchTableDTO.getUsername());
			TmpEmployee employeeDetails = getEmployeeDetails(searchTableDTO.getUsername());
			fieldVisitRequestList.setAsigneeName(employeeDetails);
			fieldVisitRequestList.setModifiedBy(userNameForDB);
			fieldVisitRequestList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(fieldVisitRequestList);
			entityManager.flush();
			
			if(searchTableDTO.getRodKey() != null){
				updateReimbursementStatus(searchTableDTO.getRodKey(),status,searchTableDTO.getUsername());
			}
			
			HumanTask reimbusementHumanTask = searchTableDTO
					.getHumanTask();
			if(reimbusementHumanTask != null) {
				if(reimbusementHumanTask.getPayload() != null) {
					if (option.equals("ASSIGN")) {
						reimbusementHumanTask.setOutcome("ASSIGN");
					} else {
						if(null == fieldVisitRequestList.getCreatedBy())
						{
							bean.setIsSystemFVRTask(true);
						}
						reimbusementHumanTask.setOutcome("SKIP");
					}
					setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
				} else {
					if (option.equals("ASSIGN")) {
						reimbusementHumanTask.setOutcome("ASSIGN");
					} else {
						reimbusementHumanTask.setOutcome("ASSIGN");
					}
					setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
				}
			}

//			if (reimbusementHumanTask == null) {
//				HumanTask cashlessHumanTask = searchTableDTO.getHumanTask();
//				if (option.equals("ASSIGN")) {
//					cashlessHumanTask.setOutcome("ASSIGN");
//				} else {
//					cashlessHumanTask.setOutcome("ASSIGN");
//				}
//				setBPMoutCome(searchTableDTO, cashlessHumanTask, bean);
//			} else {
//				if (option.equals("ASSIGN")) {
//					reimbusementHumanTask.setOutcome("ASSIGN");
//				} else {
//					reimbusementHumanTask.setOutcome("SKIP");
//				}
//				setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean);
//			}

			return true;

		}
		return false;
	}*/
	
	public FieldVisitRequest updateExistingAssignFVR(FieldVisitDTO bean){
		
		if(bean.getKey() != null){
			FieldVisitRequest fieldVisitByKey = getFieldVisitByKey(bean.getKey());
			if(fieldVisitByKey != null){
				fieldVisitByKey.setModifiedBy(bean.getUsername());
				fieldVisitByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				Status status = new Status();
				status.setKey(ReferenceTable.FVRCANCELLED);
				fieldVisitByKey.setStatus(status);
				entityManager.merge(fieldVisitByKey);
				entityManager.flush();
				
				return fieldVisitByKey;
			}
		}
		
		return null;
		
	}
	
	
	public FieldVisitRequest autoSkipFirstFVRParallel(FieldVisitRequest fvrObj,String intimationNo,String userId){
		
		if(fvrObj.getKey() != null){
			FieldVisitRequest fieldVisitByKey = getFieldVisitByKey(fvrObj.getKey());
			if(fieldVisitByKey != null){
				fieldVisitByKey.setExecutiveComments(SHAConstants.AUTO_SKIP_FVR_REMARKS);
				fieldVisitByKey.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
				fieldVisitByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				Status status = new Status();
				status.setKey(ReferenceTable.SKIPFVR);
				fieldVisitByKey.setStatus(status);
				entityManager.merge(fieldVisitByKey);
				entityManager.flush();
				
				updateWorkFLow(intimationNo,userId);
				
				return fieldVisitByKey;
			}
		}
		
		return null;
		
	}
	
	
public FieldVisitRequest autoSkipFirstFVR(FieldVisitRequest fvrObj){
		
		if(fvrObj.getKey() != null){
			FieldVisitRequest fieldVisitByKey = getFieldVisitByKey(fvrObj.getKey());
			if(fieldVisitByKey != null){
				fieldVisitByKey.setExecutiveComments(SHAConstants.AUTO_SKIP_FVR_REMARKS);
				fieldVisitByKey.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
				fieldVisitByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				Status status = new Status();
				status.setKey(ReferenceTable.SKIPFVR);
				fieldVisitByKey.setStatus(status);
				entityManager.merge(fieldVisitByKey);
				entityManager.flush();
				
				return fieldVisitByKey;
			}
		}
		
		return null;
		
	}
	public Boolean createReAssignFVR(FieldVisitRequest fieldVisitRequest){
		
		try{
			fieldVisitRequest.setReAssignFlag("Y");
			Status status = new Status();
			status.setKey(ReferenceTable.ASSIGNFVR);
			fieldVisitRequest.setStatus(status);
			entityManager.persist(fieldVisitRequest);
			entityManager.flush();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public TmpEmployee getEmployeeDetails(String loginId) {
		TmpEmployee tmpEmployee = null;
		
		loginId = loginId.toLowerCase();
		/*
		 * Query query = entityManager
		 * .createNamedQuery("TmpEmployee.findByEmpName"); query =
		 * query.setParameter("empName", empName);
		 */
		Query query = entityManager
				.createNamedQuery("TmpEmployee.getEmpByLoginId");// .setParameter("primaryKey",
																	// key);
		query.setParameter("loginId", "%" + loginId + "%");
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
			tmpEmployee = tmpEmployee2;
		}
		// TmpEmployee tmpEmployee = (TmpEmployee)query.getSingleResult();
		return tmpEmployee;
	}
	
public void updateReimbursementStatus(Long reimbursmentKey,Status status,String userName){
		
		Reimbursement reimbursement = getReimbursementByKey(reimbursmentKey);
		if(reimbursement != null && !(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
				|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))){
			
//			Claim claim = reimbursement.getClaim();
//			claim.setStatus(status);
//			entityManager.merge(claim);
//			entityManager.flush();
			
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

/*	public void setBPMoutCome(SearchFieldVisitTableDTO searchTableDTO,
			HumanTask humanTask,
			FieldVisitDTO bean, FieldVisitRequest fieldVisitRequestList) {
		if(humanTask.getPayload() != null) {
			SubmitFVRTask submitFvrTask = BPMClientContext.getSumbitFieldFisit(
					searchTableDTO.getUsername(), searchTableDTO.getPassword());
			PayloadBOType payloadBO = humanTask.getPayload();
			FieldVisitType fieldVisit = payloadBO.getFieldVisit();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimRequest = payloadBO.getClaimRequest();
			if(null == fieldVisit)
			{
				fieldVisit = new FieldVisitType();
			
				fieldVisit.setKey(searchTableDTO.getFvrKey());
			}
			
			if(null != bean.getIsSystemFVRTask() && bean.getIsSystemFVRTask())
			{
				fieldVisit.setRequestedBy(SHAConstants.NONE);
			}
//			fieldVisit.setKey(searchTableDTO.getFvrKey());
			
			
			if(fieldVisitRequestList != null && claimRequest != null){
				Intimation intimation = fieldVisitRequestList.getIntimation();
				TmpCPUCode cpuCode = intimation.getCpuCode();
				if(cpuCode != null){
					claimRequest.setCpuCode(cpuCode.getCpuCode().toString());
					
				}
			}
			payloadBO.setFieldVisit(fieldVisit);
			payloadBO.setClaimRequest(claimRequest);
			humanTask.setPayload(payloadBO);
			try{
			submitFvrTask.execute(searchTableDTO.getUsername(), humanTask);
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			SubmitAssignFieldVisitTask sumbitFieldFisitForCashless = BPMClientContext.getSumbitFieldFisitForCashless(
					searchTableDTO.getUsername(), searchTableDTO.getPassword());
			
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadCashless = humanTask.getPayloadCashless();
			FieldVisitType fieldVisit = new FieldVisitType();
			
			humanTask.setPayloadCashless(payloadCashless);
			try{
			sumbitFieldFisitForCashless.execute(searchTableDTO.getUsername(), humanTask);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
	}*/

//	public void setBPMoutCome(SearchFieldVisitTableDTO searchTableDTO,
//			HumanTask humanTask, FieldVisitDTO bean) {
//		InvokeHumanTask invokeHT = new InvokeHumanTask();
//		invokeHT.execute(searchTableDTO.getUsername(),
//				searchTableDTO.getPassword(), humanTask, null, null, null, null);
//	}

	@SuppressWarnings("unused")
	/*public Boolean submitSkipEvent(FieldVisitDTO bean, String option,
			SearchFieldVisitTableDTO searchTableDTO) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey",
				bean.getKey());

		FieldVisitRequest fieldVisitRequestList = (FieldVisitRequest) query
				.getSingleResult();

		Claim claim = getEntityManager().find(Claim.class,
				fieldVisitRequestList.getClaim().getKey());

		if (fieldVisitRequestList != null) {
			Status status = new Status();
			status.setKey(ReferenceTable.SKIPFVR);
			fieldVisitRequestList.setExecutiveComments(bean.getReasons());
			fieldVisitRequestList.setStatus(status);
			String userNameForDB = SHAUtils.getUserNameForDB(searchTableDTO.getUsername());
			fieldVisitRequestList.setModifiedBy(userNameForDB);
			fieldVisitRequestList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(fieldVisitRequestList);
			entityManager.flush();
			
			if(searchTableDTO.getRodKey() != null){
				updateReimbursementStatus(searchTableDTO.getRodKey(),status,searchTableDTO.getUsername());
			}
			
			HumanTask reimbursementHumanHask = searchTableDTO
					.getHumanTask();
			if(reimbursementHumanHask != null && option.equals("SKIP")) {
				if(reimbursementHumanHask.getPayloadCashless() != null) {
					reimbursementHumanHask.setOutcome("ASSIGN");
				} else {
					reimbursementHumanHask.setOutcome("SKIP");
					FieldVisitType fieldVisitType = reimbursementHumanHask.getPayload().getFieldVisit();
					fieldVisitType.setStatus("SKIP");
					
					if(null == fieldVisitRequestList.getCreatedBy())
					{
						bean.setIsSystemFVRTask(true);
					}
					
					reimbursementHumanHask.getPayload().setFieldVisit(fieldVisitType);
				}
				
//				if (reimbursementHumanHask != null) {	
//					cashLessHumanTask.setOutcome("ASSIGN");
//				} else {
//					if (reimbursementHumanHask != null) {
//						reimbursementHumanHask.setOutcome("SKIP");
//						FieldVisitType fieldVisitType = reimbursementHumanHask.getPayload().getFieldVisit();
//						fieldVisitType.setStatus("SKIP");
//						reimbursementHumanHask.getPayload().setFieldVisit(fieldVisitType);
//					}
//				}
			}
	
			if (reimbursementHumanHask.getPayloadCashless() != null) {
				return initiateBPMForCashless(fieldVisitRequestList, claim, option,
						reimbursementHumanHask, bean);
			} else {
				return initiateBPM(fieldVisitRequestList, claim, option,
						reimbursementHumanHask, bean);
				 setBPMoutCome(searchTableDTO, reimbursementHumanHask, bean,fieldVisitRequestList);
				 return true;
			}
		}
		return false;
	}*/

	/*public Boolean initiateBPMForCashless(FieldVisitRequest FieldVisitRequestList,
			Claim claim, String option, com.shaic.ims.bpm.claim.modelv2.HumanTask humanTask, FieldVisitDTO bean) {
		if (FieldVisitRequestList != null && claim != null) {
			Hospitals hospitals = getEntityManager().find(Hospitals.class,
					claim.getIntimation().getHospital());
			IntimationRule intimationRule = new IntimationRule();
			IntimationType intimationMessage = new IntimationType();
			PaymentInfoType paymentInfo = new PaymentInfoType();
			ClaimRequestType claimReq = new ClaimRequestType();
			PolicyType policyType = new PolicyType();
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
			HospitalInfoType hospitalInfo = new HospitalInfoType();
			paymentInfo.setClaimedAmount(claim.getClaimedAmount());
			claimType.setClaimType(claim.getClaimType().getValue());
			claimReq.setCpuCode(claim.getIntimation().getCpuCode()
					.getCpuCode().toString());
			if (hospitals != null) {
				hospitalInfo.setHospitalType(hospitals.getHospitalType()
						.getValue());
			}
			intimationMessage.setIntimationNumber(claim.getIntimation()
					.getIntimationId());
			intimationMessage.setIsBalanceSIAvailable(intimationRule
					.isSumInsuredPositive(claim.getIntimation()));
			intimationMessage.setIsClaimPending(intimationRule
					.isClaimExist(claim.getIntimation()));
			intimationMessage.setIsPolicyValid(intimationRule
					.isPolicyValid(claim.getIntimation()));
			paymentInfo.setProvisionAmount(claim.getProvisionAmount());
			intimationMessage.setKey(claim.getKey());
			PreAuthReqType preAuthReq = new PreAuthReqType();
			preAuthReq.setKey(claim.getKey());
			policyType.setPolicyId(claim.getIntimation().getPolicy()
					.getPolicyNumber());
			preAuthReq.setKey(claim.getKey());
			
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
			payloadBO.setClaimRequest(claimReq);
			payloadBO.setClaim(claimType);
			payloadBO.setHospitalInfo(hospitalInfo);
			payloadBO.setIntimation(intimationMessage);
			payloadBO.setPaymentInfo(paymentInfo);
			payloadBO.setPolicy(policyType);
			
			humanTask.setPayloadCashless(payloadBO);
			
			return executeCashless(humanTask, bean);
		} else {
			return false;
		}

	}*/

//	public Boolean initiateBPM(FieldVisitRequest FieldVisitRequestList,
//			Claim claim, String option,
//			com.shaic.ims.bpm.claim.modelv2.HumanTask humanTask,
//			FieldVisitDTO bean) {
//		if (FieldVisitRequestList != null && claim != null) {
//			Hospitals hospitals = getEntityManager().find(Hospitals.class,
//					claim.getIntimation().getHospital());
//			IntimationRule intimationRule = new IntimationRule();
//			IntimationType intimationMessage = new IntimationType();
//			intimationMessage.setAmountClaimed(claim.getClaimedAmount());
//			intimationMessage.setClaimType(claim.getClaimType().getValue());
//			intimationMessage.setCpuCode(claim.getIntimation().getCpuCode()
//					.getCpuCode().toString());
//			if (hospitals != null) {
//				intimationMessage.setHospitalType(hospitals.getHospitalType()
//						.getValue());
//			}
//			intimationMessage.setIntimationNumber(claim.getIntimation()
//					.getIntimationId());
//			intimationMessage.setIsBalanceSIAvailable(intimationRule
//					.isSumInsuredPositive(claim.getIntimation()));
//			intimationMessage.setIsClaimPending(intimationRule
//					.isClaimExist(claim.getIntimation()));
//			intimationMessage.setIsPolicyValid(intimationRule
//					.isPolicyValid(claim.getIntimation()));
//			intimationMessage.setProvisionAmount(claim.getProvisionAmount());
//			intimationMessage.setKey(claim.getKey());
//			PreAuthReqType preAuthReq = new PreAuthReqType();
//			preAuthReq.setKey(claim.getKey());
////			PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
////			preAuthReqDetails.setPolicyId(claim.getIntimation().getPolicy()
////					.getPolicyNumber());
////			preAuthReq.setPreAuthReqDetails(preAuthReqDetails);
//			preAuthReq.setKey(claim.getKey());
//			return execute(intimationMessage, preAuthReq, humanTask, bean);
//		} else {
//			return false;
//		}
//
//	}

//	@SuppressWarnings("static-access")
//	public Boolean execute(IntimationMessage intMsg, PreAuthReq preAuthReq,
//			HumanTask humanTask, FieldVisitDTO bean) {
//		BPMClientContext instance = new BPMClientContext();
//		System.out.println("BPM Init lookup called");
//		Context context = instance.getInitialContext(bean.getUsername(),
//				bean.getPassword());
//		InvokeHumanTaskService invokeHumanTaskService = null;
//		try {
//			invokeHumanTaskService = (InvokeHumanTaskService) context
//					.lookup("InvokeHumanTaskService#com.shaic.ims.bpm.claim.servicev2.InvokeHumanTaskService");
//			invokeHumanTaskService.execute(bean.getUsername(),
//					bean.getPassword(), humanTask, intMsg, preAuthReq, null,
//					null);
//			System.out.println("Look up called at server side");
//			return true;
//		} catch (NamingException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

//	@SuppressWarnings("static-access")
//	public Boolean execute(IntimationMessage intMsg, PreAuthReq preAuthReq,
//			HumanTask humanTask,
//			FieldVisitDTO bean) {
//		BPMClientContext instance = new BPMClientContext();
//		System.out.println("BPM Init lookup called");
//		Context context = instance.getInitialContext(bean.getUsername(),
//				bean.getPassword());
//		SubmitFVRTask submitFVRTask = null;
//		try {
//			submitFVRTask = (SubmitFVRTask) context
//					.lookup("SubmitFVRTask#com.shaic.ims.bpm.claim.servicev2.fvr.SubmitFVRTask");
//			try{
//			submitFVRTask.execute(bean.getUsername(), humanTask);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			System.out.println("Look up called at server side");
//			return true;
//		} catch (NamingException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	
/*	@SuppressWarnings("static-access")
	public Boolean executeCashless(HumanTask humanTask,
			FieldVisitDTO bean) {
		BPMClientContext instance = new BPMClientContext();
		System.out.println("BPM Init lookup called");
		Context context = instance.getInitialContext(bean.getUsername(),
				bean.getPassword());
		SubmitAssignFieldVisitTask submitFVRTask = null;
		try {
			submitFVRTask = (SubmitAssignFieldVisitTask) context
					.lookup("SubmitAssignFieldVisitTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitAssignFieldVisitTask");
			try{
			submitFVRTask.execute(bean.getUsername(), humanTask);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Look up called at server side");
			return true;
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}*/
	
	public Boolean submitAssignDB(FieldVisitDTO bean, String option,
			SearchFieldVisitTableDTO searchTableDTO) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey",
				bean.getKey());
		FieldVisitRequest fieldVisitRequestList = (FieldVisitRequest) query
				.getSingleResult();
		/*Claim claim = getEntityManager().find(Claim.class,
				fieldVisitRequestList.getClaim().getKey());*/
		if (fieldVisitRequestList != null) {
			Status status = new Status();
//			Stage stage = new Stage();
			status.setKey(ReferenceTable.ASSIGNFVR);
//			stage.setKey(18L);
			fieldVisitRequestList.setExecutiveComments(bean.getComments());
			fieldVisitRequestList.setRepresentativeCode(bean
					.getRepresentativeCode());

			fieldVisitRequestList.setRepresentativeName(bean.getName());
//			fieldVisitRequestList.setStage(stage);
			fieldVisitRequestList.setStatus(status);
			fieldVisitRequestList.setAssignedDate(new Timestamp(System.currentTimeMillis()));
//			fieldVisitRequestList.setFvrReceivedDate(new Date());
			String userNameForDB = SHAUtils.getUserNameForDB(searchTableDTO.getUsername());
			TmpEmployee employeeDetails = getEmployeeDetails(searchTableDTO.getUsername());
			fieldVisitRequestList.setAsigneeName(employeeDetails);
			fieldVisitRequestList.setModifiedBy(userNameForDB);
			fieldVisitRequestList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(fieldVisitRequestList);
			entityManager.flush();
			
			if(searchTableDTO.getRodKey() != null){
				updateReimbursementStatus(searchTableDTO.getRodKey(),status,searchTableDTO.getUsername());
			}			
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchTableDTO.getDbOutArray();
			Long payloadRodKey = (Long) wrkFlowMap.get(SHAConstants.PAYLOAD_ROD_KEY);
			
			if(null != payloadRodKey && !payloadRodKey.equals(0))
			{
				updateReimbursementStatus(payloadRodKey,status,searchTableDTO.getUsername());
			}
		/*	HumanTask reimbusementHumanTask = searchTableDTO
					.getHumanTask();*/
//			if(reimbusementHumanTask != null) {
//				if(reimbusementHumanTask.getPayload() != null) {
////					if (option.equals("ASSIGN")) {
////						reimbusementHumanTask.setOutcome("ASSIGN");
////					} else {
////						/*if(null == fieldVisitRequestList.getCreatedBy())
////						{
////							bean.setIsSystemFVRTask(true);
////						}*/
////						reimbusementHumanTask.setOutcome("SKIP");
////					}
////					setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
//				} else {
//					if (option.equals("ASSIGN")) {
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
//					} else {
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);
//					}
//					//setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
//					setDBoutCome(searchTableDTO,wrkFlowMap, bean,fieldVisitRequestList);
//				}
//			}
//			else
//			{				
//					if (option.equals("ASSIGN")) {
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
//					} else {
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);
//					}
//					//setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
//					setDBoutCome(searchTableDTO,wrkFlowMap, bean,fieldVisitRequestList);
//				
//			}
			
			if (option.equals("ASSIGN")) {
				wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
			} else {
				wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);
			}
			//setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean,fieldVisitRequestList);
			setDBoutCome(searchTableDTO,wrkFlowMap, bean,fieldVisitRequestList);

//			if (reimbusementHumanTask == null) {
//				HumanTask cashlessHumanTask = searchTableDTO.getHumanTask();
//				if (option.equals("ASSIGN")) {
//					cashlessHumanTask.setOutcome("ASSIGN");
//				} else {
//					cashlessHumanTask.setOutcome("ASSIGN");
//				}
//				setBPMoutCome(searchTableDTO, cashlessHumanTask, bean);
//			} else {
//				if (option.equals("ASSIGN")) {
//					reimbusementHumanTask.setOutcome("ASSIGN");
//				} else {
//					reimbusementHumanTask.setOutcome("SKIP");
//				}
//				setBPMoutCome(searchTableDTO, reimbusementHumanTask, bean);
//			}

			return true;

		}
		return false;
	}

	public void setDBoutCome(SearchFieldVisitTableDTO searchTableDTO,
			Map<String, Object> wrkFlowMap,
			FieldVisitDTO bean, FieldVisitRequest fieldVisitRequestList) {
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	}
	
	@SuppressWarnings("unused")
	public Boolean submitSkipEventDB(FieldVisitDTO bean, String option,
			SearchFieldVisitTableDTO searchTableDTO) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey",
				bean.getKey());

		FieldVisitRequest fieldVisitRequestList = (FieldVisitRequest) query
				.getSingleResult();

		/*Claim claim = getEntityManager().find(Claim.class,
				fieldVisitRequestList.getClaim().getKey());*/

		if (fieldVisitRequestList != null) {
			Status status = new Status();
			status.setKey(ReferenceTable.SKIPFVR);
			fieldVisitRequestList.setExecutiveComments(bean.getReasons());
			fieldVisitRequestList.setStatus(status);
			String userNameForDB = SHAUtils.getUserNameForDB(searchTableDTO.getUsername());
			fieldVisitRequestList.setModifiedBy(userNameForDB);
			fieldVisitRequestList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(fieldVisitRequestList);
			entityManager.flush();
			
			if(searchTableDTO.getRodKey() != null){
				updateReimbursementStatus(searchTableDTO.getRodKey(),status,searchTableDTO.getUsername());
			}
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchTableDTO.getDbOutArray();
			Long payloadRodKey = (Long) wrkFlowMap.get(SHAConstants.PAYLOAD_ROD_KEY);
			if(null != payloadRodKey && !payloadRodKey.equals(0))
			{
				updateReimbursementStatus(payloadRodKey,status,searchTableDTO.getUsername());
			}
			
			/*HumanTask reimbursementHumanHask = searchTableDTO
					.getHumanTask();*/
			if(option.equals("SKIP")) {
				
					wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);					

					
					if(null == fieldVisitRequestList.getCreatedBy())
					{
						bean.setIsSystemFVRTask(true);
					}
					
					
				
				
//				if (reimbursementHumanHask != null) {	
//					cashLessHumanTask.setOutcome("ASSIGN");
//				} else {
//					if (reimbursementHumanHask != null) {
//						reimbursementHumanHask.setOutcome("SKIP");
//						FieldVisitType fieldVisitType = reimbursementHumanHask.getPayload().getFieldVisit();
//						fieldVisitType.setStatus("SKIP");
//						reimbursementHumanHask.getPayload().setFieldVisit(fieldVisitType);
//					}
//				}
			}
			else
			{
				if (option.equals("ASSIGN")) {
					wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
				} else {
					wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);
				}
				
			}
			setDBoutCome(searchTableDTO,wrkFlowMap, bean,fieldVisitRequestList);
			/*if (reimbursementHumanHask.getPayloadCashless() != null) {
				return initiateBPMForCashless(fieldVisitRequestList, claim, option,
						reimbursementHumanHask, bean);
			} else {*/
				/*return initiateBPM(fieldVisitRequestList, claim, option,
						reimbursementHumanHask, bean);*/
				// setBPMoutCome(searchTableDTO, reimbursementHumanHask, bean,fieldVisitRequestList);
				
				 return true;
			//}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<FVRGradingDetail> getFvrGradingDetailsByFvrKey(Long fvrKey) {
		Query query = entityManager.createNamedQuery(
				"FVRGradingDetail.findByFvrKey").setParameter("fvrKey", fvrKey);
		
		List<FVRGradingDetail> fvrList=(List<FVRGradingDetail>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			for (FVRGradingDetail fieldVisitRequest : fvrList) {
				entityManager.refresh(fieldVisitRequest);
			}
			
		}
		
		return fvrList;
	}
	
	public FieldVisitRequest autoSkipNoFirstFVR(FieldVisitRequest fvrObj){
		
		if(fvrObj.getKey() != null){
			FieldVisitRequest fieldVisitByKey = getFieldVisitByKey(fvrObj.getKey());
			if(fieldVisitByKey != null){
				fieldVisitByKey.setExecutiveComments(SHAConstants.AUTO_SKIP_NO_FVR_REMARKS);
				fieldVisitByKey.setModifiedBy(SHAConstants.USER_ID_SYSTEM);
				fieldVisitByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				Status status = new Status();
				status.setKey(ReferenceTable.SKIPFVR);
				fieldVisitByKey.setStatus(status);
				entityManager.merge(fieldVisitByKey);
				entityManager.flush();
				
				return fieldVisitByKey;
			}
		}
		
		return null;
	}
		
	public void updateWorkFLow(String intimationNo,String userId){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		mapValues.put(SHAConstants.USER_ID, userId);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FVR_CURRENT_QUEUE);
		
		mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		
		List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
		
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
					
				outPutArray.put(SHAConstants.OUTCOME,SHAConstants.FVR_SKIP_OUTCOME);
				outPutArray.put(SHAConstants.USER_ID, userId);
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(outPutArray);
				
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);}
			}
	}
	
	public void updateFvrDetails(Long fvrKey,InvesAndQueryAndFvrParallelFlowTableDTO cancelledFvrInvsOrquery){
		
		FieldVisitRequest fieldVisit = getFVRByKey(fvrKey);
		
		if(null != fieldVisit){
			
			if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
				
				fieldVisit.setFvrProceedWithoutReport(SHAConstants.YES_FLAG);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFVRByKey(Long key) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey", key);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			return fvrList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<FvrNotRequired> getFieldRequestNotRemarksList(Long fvrNotRequiredKey) {
		Query query = entityManager.createNamedQuery(
				"FvrNotRequired.findByKey").setParameter("fvrNotRequiredKey", fvrNotRequiredKey);
		List<FvrNotRequired> fvrList=(List<FvrNotRequired>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			return fvrList;
			
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public FvrDetailedViewDTO setFVRClaimAlertRemark(String intitmationNo,FvrDetailedViewDTO fvrDetailedViewDTO) {
		List<Long> catList = new ArrayList<Long>();
		catList.add(SHAConstants.CLAIMS_ALERT_FVR_KEY);
		Query query = entityManager.createNamedQuery("ClaimRemarksAlerts.findByfindByIntimationCatKey")
				.setParameter("intitmationNo", intitmationNo)
				.setParameter("catList",catList);
		
		List<ClaimRemarksAlerts> claimRemarksAlerts=(List<ClaimRemarksAlerts>) query.getResultList();
		if(claimRemarksAlerts != null && ! claimRemarksAlerts.isEmpty()){
			ClaimRemarksAlerts alerts =  claimRemarksAlerts.get(0);
			fvrDetailedViewDTO.setFvrClaimAlertRemarks(alerts.getRemarks());
		}
		
		return fvrDetailedViewDTO;
	}
}
