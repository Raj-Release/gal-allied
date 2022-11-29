/**
 * 
 */
package com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel.ReOpenRodLevelClaimDTO;
/**
 * 
 *
 */
@Stateless
public class PASearchReOpenClaimService  extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	private final Logger log = LoggerFactory.getLogger(PASearchReOpenClaimService.class);
	
	
	public PASearchReOpenClaimService() {
		super();
		
	}
	public  Page<PASearchReOpenClaimTableDTO> search(
			PASearchReOpenClaimFormDTO searchFormDTO,
			String userName, String passWord) {
		
		List<Claim> listOfClaim = new ArrayList<Claim>(); 
		try{
		    String intimationNo = null != searchFormDTO.getIntimationNo()&& !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String policyNo = null != searchFormDTO.getPolicyNo() && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			
			Root<Claim> root = criteriaQuery.from(Claim.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();
			if(intimationNo != null){
			Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
			conditionList.add(condition1);
			}
			
			if(policyNo != null){
			Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
			conditionList.add(condition3);
			}

			Predicate closeCondition =   criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_CLOSED_STATUS);
			conditionList.add(closeCondition);
			
			//For PA Non Hosp
			/*Predicate condition4 = criteriaBuilder.equal(root.<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
			conditionList.add(condition4);*/
			
			Predicate condition5 = criteriaBuilder.equal(root.<String>get("processClaimType"), SHAConstants.PA_TYPE);
			conditionList.add(condition5);
			
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
			

			if(intimationNo == null || policyNo == null ){
				
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
			
			List<PASearchReOpenClaimTableDTO> tableDTO = PASearchReOpenClaimMapper.getInstance().getClaimDTO(doList);
		
			tableDTO = getHospitalDetails(tableDTO);
			List<PASearchReOpenClaimTableDTO> result = new ArrayList<PASearchReOpenClaimTableDTO>();
			result.addAll(tableDTO);
			
			if(tableDTO != null && !tableDTO.isEmpty()){
				for (PASearchReOpenClaimTableDTO pASearchReOpenClaimTableDTO : tableDTO) {
				
					Long claimKey = pASearchReOpenClaimTableDTO.getClaimKey();
					Query closeClaimQuery = entityManager.createNamedQuery("CloseClaim.findClaimsClosedByClaimKey");
					closeClaimQuery.setParameter("claimKey",claimKey);

					List<CloseClaim> closeClaimList = closeClaimQuery.getResultList();
					
					if(closeClaimList != null && ! closeClaimList.isEmpty()){
						closeClaimList.get(0);
						entityManager.refresh(closeClaimList.get(0));
						pASearchReOpenClaimTableDTO.setReasonForClose(closeClaimList.get(0).getClosingReasonId() != null ? closeClaimList.get(0).getClosingReasonId().getValue() : "");
					}
					
					
					Object workflowKey = workFlowMap.get(pASearchReOpenClaimTableDTO.getClaimKey());
					pASearchReOpenClaimTableDTO.setDbOutArray(workflowKey);
				}
				
			}
			
			Page<PASearchReOpenClaimTableDTO> page = new Page<PASearchReOpenClaimTableDTO>();		
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
	

	private List<PASearchReOpenClaimTableDTO> getHospitalDetails(
			List<PASearchReOpenClaimTableDTO> tableDTO) {
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
			}catch(Exception e){
				continue;
			}
		
		}
				
		return tableDTO;
	}
	
	
//	public ReOpenClaimDTO getReopenClaimDTO(Long reimbursementKey){
//		
//		CloseClaim closeClaim = getCloseClaim(reimbursementKey);
//		
//		Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
//		
//		ReOpenClaimDTO reOpenClaimDTO = new ReOpenClaimDTO();
//		
//		if(closeClaim != null){
//			reOpenClaimDTO = SearchReOpenClaimMapper.getReOpenClaimDTO(closeClaim);
//			reOpenClaimDTO.setClosedStrDate(SHAUtils.formatDate(reOpenClaimDTO.getClosedDate()));
//			Double provisionAmount = getProvisionAmount(reimbursement);
//			reOpenClaimDTO.setProvisionAmount(provisionAmount);
//		}
//		
//		return reOpenClaimDTO;
//
//	}
	
	public List<Reimbursement> submitReopenClaim(PASearchReOpenClaimTableDTO tableDTO, ReOpenRodLevelClaimDTO bean){
		
		Status status = new Status();
		
		List<ViewDocumentDetailsDTO> documentDetails = bean.getDocumentDetails();
		
		 List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : documentDetails) {
			
			if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
				
				
				
				Long reimbursementKey = viewDocumentDetailsDTO.getReimbursementKey();
				
				Reimbursement reimbursement = getReimbursementByKey(reimbursementKey);
				
				CloseClaim closeClaim = getCloseClaim(reimbursementKey);
				String userName = tableDTO.getUsername();
				userName = SHAUtils.getUserNameForDB(userName);
				closeClaim.setModifiedBy(userName);
				closeClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				closeClaim.setReOpenRemarks(bean.getReOpenRemarks());
				closeClaim.setReOpenDate(new Timestamp(System.currentTimeMillis()));
				
				HashMap<Long, Long> stageAndStatusMap = getStageAndStatusMap();
				
				Long StatusKey = stageAndStatusMap.get(closeClaim.getStage().getKey());
				status = entityManager.find(Status.class,StatusKey);
				closeClaim.setStatus(status);
				closeClaim.setStage(reimbursement.getStage());
				
//				if(bean.getReOpenReason() != null){
//					MastersValue reOpenClaim = entityManager.find(MastersValue.class, bean.getReOpenReason().getId());
//					closeClaim.setReOpenReasonId(reOpenClaim);
//				}
				entityManager.merge(closeClaim);
			    entityManager.flush();
			    log.info("------CloseClaim------>"+closeClaim+"<------------");
			    
			    
			    if(reimbursement !=  null){
			    	
			    	reimbursement.setModifiedBy(userName);
			    	reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			    	reimbursement.setStatus(status);
//			    	reimbursement.setCurrentProvisionAmt(bean.getProvisionAmount());
			    	Double provisionAmount = getProvisionAmount(reimbursement);
			    	reimbursement.setCurrentProvisionAmt(provisionAmount);
			    	
		             if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
			    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
			    		
			    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
			    		
			    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
			    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
			
			    		updateNetAmountForDiagnosisAndProcedure(reimbursement);
			    	}
		            reimbursementList.add(reimbursement);
			        entityManager.merge(reimbursement);
			        entityManager.flush();
			        log.info("------Reimbursement------>"+reimbursement+"<------------");
			    }
			    
			   /* SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(tableDTO.getUsername(), tableDTO.getPassword());
			    BPMClientContext.setActiveOrDeactive(task, tableDTO.getUsername(), viewDocumentDetailsDTO.getTaskNumber(), SHAConstants.RESUME_HUMANTASK);
			    BPMClientContext.setActiveOrDeactive(task, tableDTO.getUsername(), viewDocumentDetailsDTO.getTaskNumber(), SHAConstants.RELEASE_HUMANTASK);*/
			}
			
		}
		
		return reimbursementList;
		
	}
	
	
	public Double getProvisionAmount(Reimbursement reimbursement){
		
		Double claimedAmount = 0d;
		
    	if(reimbursement.getStage().getKey().equals(ReferenceTable.CREATE_ROD_STAGE_KEY)){
    		
    		claimedAmount = getClaimedAmount(reimbursement);
    		
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)){
    		claimedAmount = getClaimedAmount(reimbursement);
    	
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
    		
    		claimedAmount = getClaimedAmount(reimbursement);

    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
    		
    		if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS) ||
					reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_REOPENED) && reimbursement.getApprovalRemarks() != null
					|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CLOSED) && reimbursement.getApprovalRemarks() != null){
    			
    			
    			claimedAmount = reimbursement.getApprovedAmount() != null ? reimbursement.getApprovedAmount() : 0d;
    			
				if (reimbursement.getDocAcknowLedgement() != null) {
					if (reimbursement.getDocAcknowLedgement()
							.getPreHospitalizationClaimedAmount() != null) {
						claimedAmount += reimbursement.getDocAcknowLedgement()
								.getPreHospitalizationClaimedAmount();
					}
					if (reimbursement.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null) {
						claimedAmount += reimbursement.getDocAcknowLedgement()
								.getPostHospitalizationClaimedAmount();
					}
				}
    			
			}else{
				claimedAmount = getClaimedAmount(reimbursement);                              //will be executed if staus is refer to specialist, escalate, investigation from MA stage
			}
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
 
    		claimedAmount = reimbursement.getBillingApprovedAmount();
    		
    	}else if(reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
 
    		claimedAmount = reimbursement.getFinancialApprovedAmount();
    	}
    	
    	return claimedAmount;
		
	}
	
	
	public Double getClaimedAmount(Reimbursement reimbursement){
		
		DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
		
		Double totalClaimedAmount = 0d;
		
		totalClaimedAmount = getClaimedAmountForReimbursement(reimbursement);
		
		if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			
			if(docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)
					&& docAcknowLedgement.getHospitalisationFlag() != null && docAcknowLedgement.getHospitalisationFlag().equalsIgnoreCase("Y")
					&& docAcknowLedgement.getPreHospitalisationFlag() != null && docAcknowLedgement.getPreHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPostHospitalisationFlag() != null && docAcknowLedgement.getPostHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPartialHospitalisationFlag() != null && docAcknowLedgement.getPartialHospitalisationFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getLumpsumAmountFlag() != null && docAcknowLedgement.getLumpsumAmountFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getPatientCareFlag() != null && docAcknowLedgement.getPatientCareFlag().equalsIgnoreCase("N")
					&& docAcknowLedgement.getHospitalCashFlag() != null && docAcknowLedgement.getHospitalCashFlag().equalsIgnoreCase("N")){
				
				totalClaimedAmount = getPreauthApprovedAmt(reimbursement.getClaim().getKey());
				
			}
		}
		
		Double balanceSI = getBalanceSI(reimbursement);
		if(balanceSI != null){
			
			totalClaimedAmount = Math.min(balanceSI, totalClaimedAmount);
			
		}
		
		return totalClaimedAmount;
	}
	
	public Double getBalanceSI(Reimbursement reimbursement)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Map balanceSIMap = null;
		Long policyKey = reimbursement.getClaim().getIntimation().getPolicy().getKey();
		if(null != reimbursement.getClaim().getIntimation().getInsured().getInsuredId())
		{
			if(null != reimbursement.getClaim().getIntimation() && null != reimbursement.getClaim().getIntimation().getPolicy() && 
					null != reimbursement.getClaim().getIntimation().getPolicy().getProduct() && 
					null != reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey()))){	
			sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(reimbursement.getClaim().getIntimation().getInsured().getInsuredId()),
					policyKey,reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
			}
			else
			{
				sumInsured = dbCalculationService.getGPAInsuredSumInsured(String.valueOf(reimbursement.getClaim().getIntimation().getInsured().getInsuredId()),
						policyKey);
			}
		}
		
		if(null != reimbursement.getClaim().getIntimation() && null != reimbursement.getClaim().getIntimation().getPolicy() && 
				null != reimbursement.getClaim().getIntimation().getPolicy().getProduct() && 
				null != reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(reimbursement.getClaim().getIntimation().getPolicy().getProduct().getKey()))){	
		
			balanceSIMap = dbCalculationService.getBalanceSI(policyKey , 
					reimbursement.getClaim().getIntimation().getInsured().getKey() , 
					reimbursement.getClaim().getKey(),sumInsured,reimbursement.getClaim().getIntimation().getKey());
		}
		else
		{
			balanceSIMap = dbCalculationService.getGPABalanceSI(policyKey , 
					reimbursement.getClaim().getIntimation().getInsured().getKey() , 
					reimbursement.getClaim().getKey(),sumInsured,reimbursement.getClaim().getIntimation().getKey());
		}
		Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
		return balanceSI;
	
	}
	
	public Double getClaimedAmountForReimbursement(Reimbursement reimbursement) {
		try {
			Double claimedAmount = 0.0;

			DocAcknowledgement docAcknowledgment = reimbursement.getDocAcknowLedgement();
			
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", reimbursement.getKey());
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
		
			Double hospitalizationClaimedAmount = null != docAcknowledgment.getHospitalizationClaimedAmount() ? docAcknowledgment.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgment.getPostHospitalizationClaimedAmount() ? docAcknowledgment.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgment.getPreHospitalizationClaimedAmount() ? docAcknowledgment.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}

	public void updateNetAmountForDiagnosisAndProcedure(Reimbursement reimbursment){
		List<PedValidation> diagnosis = getDiagnosis(reimbursment.getKey());
		for (PedValidation pedValidation : diagnosis) {
			
			Double diffAmount = pedValidation.getDiffAmount();
			pedValidation.setApproveAmount(diffAmount);
			pedValidation.setNetApprovedAmount(diffAmount);
			pedValidation.setDiffAmount(0d);
			pedValidation.setProcessFlag("F");
			pedValidation.setStage(reimbursment.getStage());
			pedValidation.setStatus(reimbursment.getStatus());
			entityManager.merge(pedValidation);
			entityManager.flush();
			log.info("------PedValidation------>"+pedValidation+"<------------");
		}
		
		List<Procedure> procedure = getProcedure(reimbursment.getKey());
		for (Procedure procedure2 : procedure) {
			
			Double diffAmount = procedure2.getDiffAmount();
			procedure2.setApprovedAmount(diffAmount);
			procedure2.setNetApprovedAmount(diffAmount);
			procedure2.setDiffAmount(0d);
			procedure2.setProcessFlag("F");
			procedure2.setStage(reimbursment.getStage());
			procedure2.setStatus(reimbursment.getStatus());
			entityManager.merge(procedure2);
			entityManager.flush();
			log.info("------Procedure------>"+procedure2+"<------------");
		}
	}
	
	  public List<PedValidation> getDiagnosis(Long transactionKey) {	
	 		

			List<PedValidation> resultList = new ArrayList<PedValidation>();
			
			Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
			query.setParameter("transactionKey", transactionKey);
			
			resultList = (List<PedValidation>)query.getResultList();
		    
			return resultList;

		}
	  
	  public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
			DocAcknowledgement docAcknowledgement = null;
			Query query = entityManager
					.createNamedQuery("DocAcknowledgement.findByKey");
			query = query.setParameter("ackDocKey", docAckKey);
			if (null != query) {
				docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
			}
			return docAcknowledgement;
		}
	  
	  @SuppressWarnings("unchecked")
		public List<Procedure> getProcedure(Long transactionKey){
			
				Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
				query.setParameter("transactionKey", transactionKey);
				
				List<Procedure> resultList = (List<Procedure>)query.getResultList();
				
				return resultList;
		}
	
	private CloseClaim getCloseClaim(Long reimbursmentKey){
		
		Query query = entityManager.createNamedQuery("CloseClaim.findByReimbursmentKey");
		query.setParameter("reimbursmentKey", reimbursmentKey);
		query.setParameter("fromCreateRod", 132l);
		query.setParameter("toFinancial", 137l);
		
		List<CloseClaim> closeClaimList = (List<CloseClaim>)query.getResultList();
		if(closeClaimList != null && ! closeClaimList.isEmpty()){
			return closeClaimList.get(0);
		}
		
		return null;
		
	}
	
	
	   private Double getPreauthApprovedAmt(Long claimKey){
	    	
	    	Double approvedAmt = 0d;
	    	
	    	Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
			query.setParameter("claimkey", claimKey);
			
			List<Preauth> preauthList = (List<Preauth>)query.getResultList();
			if(null != preauthList && ! preauthList.isEmpty()){
				Preauth preauth = preauthList.get(0);
				approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
				/*for (Preauth preauth : preauthList) {
					approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount() : 0d;
				}*/
			}
	    	return approvedAmt;
	    	
	    }
		
	
	private CloseClaim getCloseClaimByKey(Long closeClaimkey){
		
		Query query = entityManager.createNamedQuery("CloseClaim.findByKey");
		query.setParameter("primaryKey", closeClaimkey);
		
		List<CloseClaim> closeClaimList = (List<CloseClaim>)query.getResultList();
		if(closeClaimList != null && ! closeClaimList.isEmpty()){
			return closeClaimList.get(0);
		}
		
		return null;
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
	
     public HashMap<Long, Long> getStageAndStatusMap(){
		
		HashMap<Long, Long> hashMap = new HashMap<Long, Long>();
		hashMap.put(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_REOPENED);
		hashMap.put(ReferenceTable.BILL_ENTRY_STAGE_KEY, ReferenceTable.BILL_ENTRY_REOPENED);
		hashMap.put(ReferenceTable.ZONAL_REVIEW_STAGE, ReferenceTable.ZONAL_REVIEW_REOPENED);
		hashMap.put(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY, ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.INVESTIGATION_STAGE, ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.FVR_STAGE_KEY,  ReferenceTable.CLAIM_REQUEST_REOPENED);
		hashMap.put(ReferenceTable.BILLING_STAGE, ReferenceTable.BILLING_REOPENED);
		hashMap.put(ReferenceTable.FINANCIAL_STAGE, ReferenceTable.FINANCIAL_REOPENED);
		
		return hashMap;
		
	}
	

}