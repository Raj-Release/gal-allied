/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Stage;

/**
 * 
 *
 */
@Stateless
public class SearchCloseClaimService  extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchCloseClaimService() {
		super();
		
	}

	public  Page<SearchCloseClaimTableDTO> search(
			SearchCloseClaimFormDTO searchFormDTO) {
		
		List<Claim> listOfClaim = new ArrayList<Claim>(); 
		try{
			String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String policyNo = null != searchFormDTO.getPolicyNo() && searchFormDTO.getPolicyNo() != null ? searchFormDTO.getPolicyNo() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		/*IMSSUPPOR-30440
		Predicate intimationPredicate = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");*/
		Predicate intimationPredicate = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
		conditionList.add(intimationPredicate);
		}
		if(policyNo != null && !policyNo.isEmpty()){
		/*IMSSUPPOR-30440
		Predicate policyNoPredicate = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");*/
		Predicate policyNoPredicate = criteriaBuilder.equal(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), policyNo);
		conditionList.add(policyNoPredicate);
		}
		
        
		if(intimationNo != null || policyNo != null){
			criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if (pageNumber > 1) 
		{
			firtResult = (pageNumber - 1) * 10;
		} 
		else 
		{
			firtResult = 0;
		}
		
	
		if(intimationNo == null && policyNo == null){
			
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			
		}else{
			listOfClaim = typedQuery.getResultList();
		}
		
		
		List<Claim> doList = new ArrayList<Claim>();
		
		for(Claim claim:listOfClaim){
			
			if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT) && 
					! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
					doList.add(claim);
			}
		
		}
//		doList = listOfClaim;
		List<SearchCloseClaimTableDTO> tableDTO = SearchCloseClaimMapper.getInstance().getClaimDTO(doList);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
	
		tableDTO = getHospitalDetails(tableDTO);
		List<SearchCloseClaimTableDTO> result = new ArrayList<SearchCloseClaimTableDTO>();
		result.addAll(tableDTO);
		
	
		Page<SearchCloseClaimTableDTO> page = new Page<SearchCloseClaimTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
		}
	
	public Claim getReimbursementClaim(Claim claim){
		
		List<Reimbursement> reimbursementByClaimKey = getReimbursementByClaimKey(claim.getKey());
		
		if(reimbursementByClaimKey != null && ! reimbursementByClaimKey.isEmpty()){
			return claim;
		}
		return null;
		
	}

	
	@SuppressWarnings("unchecked")
	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"Reimbursement.findByClaimKey").setParameter("claimKey",
				claimKey);
		List<Reimbursement> rodList = query.getResultList();

		return rodList;
	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	@SuppressWarnings("unchecked")
	private List<SearchCloseClaimTableDTO> getHospitalDetails(
			List<SearchCloseClaimTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setCpuCode(getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode());
			 }
			 
			 int noofRods = 0;
			 Query findRODsByClaimKey = entityManager.createNamedQuery(
						"Reimbursement.CountAckByClaimKey").setParameter("claimkey", tableDTO.get(index).getClaimKey());
			 List<Object> reimbursementListByclaimKey =  (List<Object>)findRODsByClaimKey.getResultList();

			 
			 if(reimbursementListByclaimKey != null && !reimbursementListByclaimKey.isEmpty()){
					Long rodCount = (Long)reimbursementListByclaimKey.get(0);
					noofRods = rodCount != null ? rodCount.intValue() : 0;
					tableDTO.get(index).setNoofRods(noofRods);
			 }
			 
			 
			}catch(Exception e){
				continue;
			}
		
		}
				
		return tableDTO;
	}
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	
	
	/*public  Page<SearchCloseClaimTableDTO> searchBPMNTask(
			SearchCloseClaimFormDTO searchFormDTO,
			String userName, String passWord) {
		
		List<Claim> listOfClaim = new ArrayList<Claim>(); 
		try{
			String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String policyNo = searchFormDTO.getPolicyNo() != null ? searchFormDTO.getPolicyNo() : null;
			
			PayloadBOType payload = null;
			
			if(intimationNo != null && !intimationNo.isEmpty()){
				
				payload = new PayloadBOType();
				IntimationType intimationType = new IntimationType();
				intimationType.setIntimationNumber(intimationNo);
				payload.setIntimation(intimationType);
			}
			
			if(policyNo != null && ! policyNo.isEmpty()){
				if(payload == null){
					payload = new PayloadBOType();
				}
				PolicyType policyType = new PolicyType();
				policyType.setPolicyId(policyNo);
				payload.setPolicy(policyType);
			}			
			
//			SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(userName, passWord);
//		    BPMClientContext.setActiveOrDeactive(task, userName, 271807, SHAConstants.RESUME_HUMANTASK);
		
//		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
//		
//		Root<Claim> root = criteriaQuery.from(Claim.class);
//		
//		List<Predicate> conditionList = new ArrayList<Predicate>();
//		if(intimationNo != null){
//		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
//		conditionList.add(condition1);
//		}
//		if(ClaimNo != null){
//		Predicate condition2 = criteriaBuilder.like(root.<String>get("claimId"), "%"+ClaimNo+"%");
//		conditionList.add(condition2);
//		}
//		if(policyNo != null){
//		Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
//		conditionList.add(condition3);
//		}
//		if(intimationNo == null && ClaimNo == null && policyNo == null){
//			criteriaQuery.select(root);
//			}else{
//		criteriaQuery.select(root).where(
//				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
//			}
//		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
//		listOfClaim = typedQuery.getResultList();
//		
		List<Claim> doList = new ArrayList<Claim>();
//		
//		for(Claim claim:listOfClaim){
//			
//			if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT) && 
//					! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
//				Claim reimbursementClaim = getReimbursementClaim(claim);
//				if(reimbursementClaim != null){
//					doList.add(claim);
//				}
//			}
//		
//		}
		
		List<SearchCloseClaimTableDTO> tableDTO = new ArrayList<SearchCloseClaimTableDTO>();
		
		CloseClaimTask closeClaimInProcessTask = BPMClientContext.getCloseClaimInProcessTask(searchFormDTO.getUsername(), searchFormDTO.getPassword());
		
		PagedTaskList tasks = closeClaimInProcessTask.getTasks(userName, null, payload);
		
		
		if(tasks != null){
			List<HumanTask> humanTasks = tasks.getHumanTasks();
			for (HumanTask humanTask : humanTasks) {
				PayloadBOType reimbursementPayload = humanTask.getPayload();
				RODType rod = reimbursementPayload.getRod();
				if(rod != null && rod.getKey() != null){
					Reimbursement reimbursement = getReimbursementByKey(rod.getKey());
					SearchCloseClaimTableDTO searchClaimDTO = SearchCloseClaimMapper.getInstance().getSearchClaimDTO(reimbursement.getClaim());
					searchClaimDTO.setHumanTask(humanTask);
					searchClaimDTO.setClaimStatus(reimbursement.getStatus().getProcessValue());
					tableDTO.add(searchClaimDTO);
				}
			}
		}
		
//		doList = listOfClaim;
//		List<SearchCloseClaimTableDTO> tableDTO = SearchCloseClaimMapper.getClaimDTO(doList);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
	
		tableDTO = getHospitalDetails(tableDTO);
		
		List<SearchCloseClaimTableDTO> result = new ArrayList<SearchCloseClaimTableDTO>();
		result.addAll(tableDTO);
		Page<SearchCloseClaimTableDTO> page = new Page<SearchCloseClaimTableDTO>();
		page.setPageItems(result);
		page.setTotalRecords(tasks.getTotalRecords());
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}*/
	
	public List<Reimbursement> submitSearchBasedCloseClaim(ViewDocumentDetailsDTO documentDTO,SearchCloseClaimTableDTO searchDTO){
		
		List<ViewDocumentDetailsDTO> documentDetailsList = documentDTO.getDocumentDetailsList();
		
		List<Reimbursement> reimbusementList = new ArrayList<Reimbursement>();
		
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetailsList) {
			
			if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
				Long reimbursementKey = viewDocumentDetailsDTO.getReimbursementKey();
				if(reimbursementKey != null){
			
					String userName = searchDTO.getUsername();
					String userNameForDB = SHAUtils.getUserNameForDB(userName);
					
					Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
					reimbursement.setCurrentProvisionAmt(0d);
					HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMap();
					
					Long stageKey = reimbursement.getStage().getKey();
					
					if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
						if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
							stageKey = ReferenceTable.BILLING_STAGE;
						}
					}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
						if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
							stageKey = ReferenceTable.FINANCIAL_STAGE;
						}
					}
					
					Long statusKey = stageAndStatusMap.get(stageKey);
					
					Stage stage = entityManager.find(Stage.class, stageKey);
					Status status = entityManager.find(Status.class, statusKey);
					
					reimbursement.setStage(stage);
					reimbursement.setStatus(status);
					reimbursement.setModifiedBy(userNameForDB);
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
					
					reimbusementList.add(reimbursement);

					List<PedValidation> diagnosis = getDiagnosis(reimbursement.getKey());
					for (PedValidation pedValidation : diagnosis) {
						
						Double approvedAmount = pedValidation.getApproveAmount();
						pedValidation.setDiffAmount(approvedAmount);
						pedValidation.setNetAmount(0d);
						pedValidation.setProcessFlag("C");
						pedValidation.setStage(reimbursement.getStage());
						pedValidation.setStatus(reimbursement.getStatus());
						entityManager.merge(pedValidation);
						entityManager.flush();
						entityManager.clear();
					}
					
					List<Procedure> procedure = getProcedure(reimbursement.getKey());
					for (Procedure procedure2 : procedure) {
						
						Double approvedAmount = procedure2.getApprovedAmount();
						procedure2.setDiffAmount(approvedAmount);
						procedure2.setNetAmount(0d);
						procedure2.setProcessFlag("C");
						procedure2.setStage(reimbursement.getStage());
						procedure2.setStatus(reimbursement.getStatus());
						entityManager.merge(procedure2);
						entityManager.flush();
						entityManager.clear();
					}
					
//					if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)
//							|| reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
//							|| reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
//							|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
//						
//						dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
//						dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
						
						

//					}
					
					
					CloseClaim closeClaim = new CloseClaim();
					closeClaim.setReimbursement(reimbursement);
					closeClaim.setStage(reimbursement.getStage());
					closeClaim.setStatus(reimbursement.getStatus());
					closeClaim.setCreatedBy(userNameForDB);
					closeClaim.setClosedDate(new Timestamp(System.currentTimeMillis()));
					
					closeClaim.setActiveStatus(1l);
					if(documentDTO.getClosingReason() != null){
						MastersValue closingReason = entityManager.find(MastersValue.class, documentDTO.getClosingReason().getId());
						closeClaim.setClosingReasonId(closingReason);
					}
					closeClaim.setClosingRemarks(documentDTO.getClosingRemarks());
					
					entityManager.persist(closeClaim);
					entityManager.flush();
					entityManager.clear();
					
					/***
					 * pending for db migration. Refractoring needs to be
					 * implemented. Hence commenting the same.
					 */
				//	SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(searchDTO.getUsername(), searchDTO.getPassword());
					
				    //BPMClientContext.setActiveOrDeactive(task, searchDTO.getUsername(), viewDocumentDetailsDTO.getTaskNumber(), SHAConstants.SUSPEND_HUMANTASK);

				}
			}
		}
		
		return reimbusementList;
		
	}
	
public List<Reimbursement> submitCloseClaimInProcess(ViewDocumentDetailsDTO documentDTO,SearchCloseClaimTableDTO searchDTO){
	
	List<ViewDocumentDetailsDTO> documentDetailsList = documentDTO.getDocumentDetailsList();
	
	List<Reimbursement> reimbusementList = new ArrayList<Reimbursement>();
	
	for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetailsList) {
		
			Long reimbursementKey = viewDocumentDetailsDTO.getReimbursementKey();
			if(reimbursementKey != null){
		
				String userName = searchDTO.getUsername();
				String userNameForDB = SHAUtils.getUserNameForDB(userName);
				
				Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
				reimbursement.setCurrentProvisionAmt(0d);
				HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMap();
				
				Long stageKey = reimbursement.getStage().getKey();
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
					if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
						stageKey = ReferenceTable.BILLING_STAGE;
					}
				}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
					if(reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
						stageKey = ReferenceTable.FINANCIAL_STAGE;
					}
				}
				
				Long statusKey = stageAndStatusMap.get(stageKey);
				
				Stage stage = entityManager.find(Stage.class, stageKey);
				Status status = entityManager.find(Status.class, statusKey);
				
				reimbursement.setStage(stage);
				reimbursement.setStatus(status);
				reimbursement.setModifiedBy(userNameForDB);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
				
				reimbusementList.add(reimbursement);
				
				
				
				List<PedValidation> diagnosis = getDiagnosis(reimbursement.getKey());
				for (PedValidation pedValidation : diagnosis) {
					
					Double approvedAmount = pedValidation.getApproveAmount();
					pedValidation.setDiffAmount(approvedAmount);
					pedValidation.setNetAmount(0d);
					pedValidation.setProcessFlag("C");
					pedValidation.setStage(reimbursement.getStage());
					pedValidation.setStatus(reimbursement.getStatus());
					entityManager.merge(pedValidation);
					entityManager.flush();
					entityManager.clear();
				}
				
				List<Procedure> procedure = getProcedure(reimbursement.getKey());
				for (Procedure procedure2 : procedure) {
					
					Double approvedAmount = procedure2.getApprovedAmount();
					procedure2.setDiffAmount(approvedAmount);
					procedure2.setNetAmount(0d);
					procedure2.setProcessFlag("C");
					procedure2.setStage(reimbursement.getStage());
					procedure2.setStatus(reimbursement.getStatus());
					entityManager.merge(procedure2);
					entityManager.flush();
					entityManager.clear();
				}
				
//				if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)
//						|| reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
//						|| reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
//						|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
//					
//					dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
//					dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
					
					

//				}
				
				
				CloseClaim closeClaim = new CloseClaim();
				closeClaim.setReimbursement(reimbursement);
				closeClaim.setStage(reimbursement.getStage());
				closeClaim.setStatus(reimbursement.getStatus());
				closeClaim.setCreatedBy(userNameForDB);
				closeClaim.setActiveStatus(1l);
				if(documentDTO.getClosingReason() != null){
					MastersValue closingReason = entityManager.find(MastersValue.class, documentDTO.getClosingReason().getId());
					closeClaim.setClosingReasonId(closingReason);
				}
				closeClaim.setClosingRemarks(documentDTO.getClosingRemarks());
				
				entityManager.persist(closeClaim);
				entityManager.flush();
				entityManager.clear();
				
				/***
				 * pending for db migration. Refractoring needs to be
				 * implemented. Hence commenting the same.
				 */
				/*SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(searchDTO.getUsername(), searchDTO.getPassword());
				
			    BPMClientContext.setActiveOrDeactive(task, searchDTO.getUsername(), viewDocumentDetailsDTO.getTaskNumber(), SHAConstants.SUSPEND_HUMANTASK);*/

			}
		
	}
	
	return reimbusementList;
	
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
	
  public List<PedValidation> getDiagnosis(Long transactionKey) {	
 		

		List<PedValidation> resultList = new ArrayList<PedValidation>();
		
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", transactionKey);
		
		resultList = (List<PedValidation>)query.getResultList();
	    
		return resultList;

	}
	
	public HashMap<Long, Long> getStageAndStatusMap(){
		
		HashMap<Long, Long> hashMap = new HashMap<Long, Long>();
		hashMap.put(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_CLOSED);
		hashMap.put(ReferenceTable.BILL_ENTRY_STAGE_KEY, ReferenceTable.BILL_ENTRY_CLOSED);
		hashMap.put(ReferenceTable.ZONAL_REVIEW_STAGE, ReferenceTable.ZONAL_REVIEW_CLOSED);
		hashMap.put(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY, ReferenceTable.CLAIM_REQUEST_CLOSED);
		hashMap.put(ReferenceTable.INVESTIGATION_STAGE, ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.FVR_STAGE_KEY,  ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.BILLING_STAGE, ReferenceTable.BILLING_CLOSED);
		hashMap.put(ReferenceTable.FINANCIAL_STAGE, ReferenceTable.FINANCIAL_CLOSED);
		
		return hashMap;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Procedure> getProcedure(Long transactionKey){
		
			Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
			query.setParameter("transactionKey", transactionKey);
			
			List<Procedure> resultList = (List<Procedure>)query.getResultList();
			
			return resultList;
	}
	

}