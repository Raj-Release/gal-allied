package com.shaic.claim.reimbursement.rrc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.vaadin.ui.Notification;

@Stateless
public class InitiateRRCRequestService extends AbstractDAO<Claim>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	public InitiateRRCRequestService(){
		super();
	}
	public  Page<InitiateRRCRequestTableDTO> search(InitiateRRCRequestFormDTO searchFormDTO,String userName, String passWord) 
	
	{
		List<Claim> listIntimations = new ArrayList<Claim>(); 
		try{
		String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
		String policyNo = null != searchFormDTO.getPolicyNo() && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
		
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		
		if(null != intimationNo)
		{
			
			Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
			conditionList.add(condition1);
		}
		
		if(null != policyNo)
		{
			Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
			conditionList.add(condition2);
		}
		
		Predicate condition3 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_CLOSED_STATUS);
		//Predicate condition4 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.BILL_ENTRY_CLOSED);
		//Predicate condition5 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.ZONAL_REVIEW_CLOSED);
		//Predicate condition6 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_REQUEST_CLOSED);
		//Predicate condition7 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.BILLING_CLOSED);
		//Predicate condition8 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_CLOSED);
		Predicate condition9 = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.INTIMATION_REGISTERED_STATUS);
		Predicate condition10 = criteriaBuilder.and(condition3,condition9);

		
						
		conditionList.add(condition10);
		
		if (intimationNo == null && policyNo == null) 
		{
			criteriaQuery.select(root);
			
		} 
		else 
		{
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
	/*	int pageNumber = searchFormDTO.getPageable().getPageNumber(); 
		int firtResult;
		if (pageNumber > 1) 
		{
			firtResult = (pageNumber - 1) * 10;
		} 
		else 
		{
			firtResult = 1;
		}*/
		listIntimations = typedQuery.getResultList();
		
		/*if( listIntimations.size()>10)
		{
			listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}*/
		
	    List<Claim> doList = listIntimations;

		List<InitiateRRCRequestTableDTO> tableDTO = InitiateRRCRequestMapper.getInstance().getInitiateRRCTableObjects(doList);
		
        
		tableDTO = getHospitalDetails(tableDTO);
	
		List<InitiateRRCRequestTableDTO> result = new ArrayList<InitiateRRCRequestTableDTO>();
		result.addAll(tableDTO);
		Page<InitiateRRCRequestTableDTO> page = new Page<InitiateRRCRequestTableDTO>();			
	//	searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
	//		searchFormDTO.getPageable().setPageNumber(1);
		}
	//	page.setPageNumber(pageNumber);
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
	
	
	
	
	private List<InitiateRRCRequestTableDTO> getHospitalDetails(List<InitiateRRCRequestTableDTO> tableDTO)
	{
		
		for(int index = 0; index < tableDTO.size(); index++)
		{
			Hospitals hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalNameId());
			if(hospitalDetail != null)
			{
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
			     tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
			    tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());
			    
			  		          		     
			}
			
		}
		return tableDTO;
	}
	
	
	private Hospitals getHospitalDetail(Long hospitalId){
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try{
			
		hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
		return hospitalDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<ExtraEmployeeEffortDTO> getEmployeeDetailsFromRRCDetails(Long rrcRequestKey)
	{
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;
		Query query = entityManager.createNamedQuery("RRCDetails.findByRequestKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCDetails> rrcDetailsList = query.getResultList();
		if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
		{
			ReviewRRCRequestMapper mapper =  ReviewRRCRequestMapper.getInstance();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);
		}
		return employeeEffortDTO;
		
	}
	
	public List<ExtraEmployeeEffortDTO> getCategoryDetailsFromRRCCategory(Long rrcRequestKey)
	{
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;
		Query query = entityManager.createNamedQuery("RRCCategory.findByRequestKey");
	
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCCategory> rrcCategoryList = query.getResultList();
		if(null != rrcCategoryList && !rrcCategoryList.isEmpty())
		{
			employeeEffortDTO = new ArrayList<ExtraEmployeeEffortDTO>();
			for (RRCCategory rrcCategory : rrcCategoryList) {
				ExtraEmployeeEffortDTO extraEmpEffortDTO = new ExtraEmployeeEffortDTO();
				SelectValue selCategory = new SelectValue();
				selCategory.setId(rrcCategory.getCategoryId().getKey());
				selCategory.setValue(rrcCategory.getCategoryId().getValue());
				extraEmpEffortDTO.setCategory(selCategory);
				extraEmpEffortDTO.setRrcCategoryKey(rrcCategory.getKey());
				employeeEffortDTO.add(extraEmpEffortDTO);
			}
		}
		return employeeEffortDTO;
		
	}
	
	
	
	
	
	public List<QuantumReductionDetailsDTO> getQuantumReductionDataFromRRCRequest(Long rrcRequestKey)
	{
		List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = null;
		Query query = entityManager.createNamedQuery("RRCRequest.findByKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			quantumReductionDetailsDTO = mapper.getQuantumReductionDetailsList(rrcRequestList);
		}
		return quantumReductionDetailsDTO;
		
	}
	
	public QuantumReductionDetailsDTO getQuantumReductionDetailsDTOFromRRCRequest(Long rrcRequestKey)
	{
		List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = null;
		Query query = entityManager.createNamedQuery("RRCRequest.findByKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			quantumReductionDetailsDTO = mapper.getQuantumReductionDetailsList(rrcRequestList);
		}
		if(null != quantumReductionDetailsDTO && !quantumReductionDetailsDTO.isEmpty())
		{
			return quantumReductionDetailsDTO.get(0);
		}
		return null;
		
	}
	
	
	/**
	 * Added for Process RRC Request Submit 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String submitModifyRRCRequest(RRCDTO rrcDTO)
	{
		String rrcRequestNo = "";
		try
		{
			rrcRequestNo = saveModifyRRCRequestValues(rrcDTO);
		}
		catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		return rrcRequestNo;
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	public RRCRequest getRRCRequestByKey(Long rrcRequestKey)
	{
		Query query = entityManager
				.createNamedQuery("RRCRequest.findByKey").setParameter(
						"rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		
		if(rrcRequestList != null && !rrcRequestList.isEmpty()) {
			for (RRCRequest rrcRequest : rrcRequestList) {
				entityManager.refresh(rrcRequest);
			}
			return rrcRequestList.get(0);
		}
		return null;
	}
	
	private String saveModifyRRCRequestValues(RRCDTO rrcDTO)
	{
		
		ReviewRRCRequestMapper reviewRRCRequestMapper = ReviewRRCRequestMapper.getInstance();
		/**
		 * Since RRC request stage and status is not finalized, 
		 * setting zonal stage and status for testing purpose. Once
		 * stage and status for RRC request is finalized, the same
		 * will be incorporated.
		 **/
		Reimbursement reimbursement = getReimbursementByKey(rrcDTO.getRodKey());
		
		rrcDTO.setStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		rrcDTO.setStatusKey(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS);
		//String rrcReqNo = generateRRCRequestNo(rrcDTO.getNewIntimationDTO().getIntimationId(),rrcDTO.getClaimDTO().getKey());
		String rrcReqNo = rrcDTO.getRrcRequestNo();
		RRCRequest rrcRequest = ModifyRRCRequestMapper.getInstance().getRRCRequestData(rrcDTO);
		rrcRequest.setReimbursement(reimbursement);
		
		SelectValue selValue = rrcDTO.getEligibility();
		
		
		
		
		if(null != reimbursement)
			rrcRequest.setClaim(reimbursement.getClaim());
		else
		{
			Claim claim = getClaimByClaimKey(rrcDTO.getClaimKey());
			rrcRequest.setClaim(claim);
		}
		
		//rrcRequest.setClaim(reimbursement.getClaim());
		rrcRequest.setRrcRequestNumber(rrcReqNo);
		rrcRequest.setActiveStatus(1L);
		rrcRequest.setRrcType("RRC MODIFY");
		
		Stage rrcStage = new Stage();
		/**
		 * The below stage and status will be changed
		 * once stage and status is available for 
		 * review rrc request.
		 */
		rrcStage.setKey(ReferenceTable.RRC_STAGE);
		
		Status rrcStatus = new Status();
		rrcStatus.setKey(ReferenceTable.RRC_REQUEST_REVIEWED_STATUS);
		
		MastersValue masRequestedTypeId = new MastersValue();	
		masRequestedTypeId.setKey(ReferenceTable.RRC_REQUEST_STATUS_FRESH);
		
		
		if(null != rrcDTO.getProcessingStageKey())
		{
			Stage stage = new Stage();
			stage.setKey(rrcDTO.getProcessingStageKey());
			rrcRequest.setRequestedStageId(stage);
		}
		
		
		rrcRequest.setRequestedTypeId(masRequestedTypeId);
		rrcRequest.setReviewedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setReviewedBy(rrcDTO.getStrUserName());
		rrcRequest.setStatus(rrcStatus);
		rrcRequest.setStage(rrcStage);
		
		
		rrcRequest.setRrcInitiatedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setProcessedBy(rrcDTO.getProcessedBy());
		rrcRequest.setProcessedDate(rrcDTO.getProcessedDate());
		rrcRequest.setReviewedDate(rrcDTO.getReviewedDate());
		rrcRequest.setReviewedBy(rrcDTO.getReviewedBy());
		
		rrcRequest.setModifiedBy(rrcDTO.getStrUserName());
		rrcRequest.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		
		entityManager.persist(rrcRequest);
		entityManager.flush();
		entityManager.refresh(rrcRequest);

		List<ExtraEmployeeEffortDTO> rrcCategoryList = rrcDTO.getRrcCategoryList();
		if(null != rrcCategoryList && !rrcCategoryList.isEmpty())
		{
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcCategoryList) {
				RRCCategory rrcCategory = reviewRRCRequestMapper.getRRCCategoryDetails(extraEmployeeEffortDTO);
				rrcCategory.setRrcRequest(rrcRequest.getRrcRequestKey());
				entityManager.persist(rrcCategory);
				entityManager.flush();
				entityManager.refresh(rrcCategory);
			}
		}
		List<ExtraEmployeeEffortDTO> extraEffortByEmpList = rrcDTO.getEmployeeEffortList();
		if(null != extraEffortByEmpList && !extraEffortByEmpList.isEmpty())
		{
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEffortByEmpList) {
				RRCDetails rrcDetails = reviewRRCRequestMapper.getRRCDetailsForEmployee(extraEmployeeEffortDTO);
				rrcDetails.setRrcRequest(rrcRequest.getRrcRequestKey());
				rrcDetails.setRrcDetailsKey(null);
				entityManager.persist(rrcDetails);
				entityManager.flush();
				entityManager.refresh(rrcDetails);
				/*if(rrcDTO.getRrcRequestKey().equals(extraEmployeeEffortDTO.getRrcRequestKey()))
				{
					entityManager.merge(rrcDetails);
					entityManager.flush();
				//	entityManager.refresh(rrcDetails);
				}
				else
				{
					entityManager.persist(rrcDetails);
					entityManager.flush();
					entityManager.refresh(rrcDetails);
				}*/
			}
		}	
	
		
		
		/**
		 * As of now, either fresh ROD or on hold rod alone
		 * is processed.
		 * */
	
		/*if(null != rrcRequest.getKey())
		{
			entityManager.merge(rrcRequest);
			entityManager.flush();
			entityManager.refresh(rrcRequest);
		}*/
		
		return rrcReqNo;
		}
		
	//}
	
	
	
	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	} 
	
	private String generateRRCRequestNo(String strIntimationNo,Long claimKey)
	{	
		Long count = getRRCRequestCountByClaim(claimKey);
		StringBuffer strRRCReq = new StringBuffer();
		
		Long lackCount = count + 001;
		strRRCReq.append(strIntimationNo)
				.append("/").append(lackCount);
		return strRRCReq.toString();
	}
	
	private Long getRRCRequestCountByClaim(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("RRCRequest.CountAckByClaimKey");
		query.setParameter("claimkey", claimKey);
		Long countOfRRCReq = (Long) query.getSingleResult();
		return countOfRRCReq;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	
	
}
