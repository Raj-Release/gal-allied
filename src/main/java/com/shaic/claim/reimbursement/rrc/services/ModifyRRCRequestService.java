/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.vaadin.ui.Notification;

/**
 * @author ntv.vijayar
 *
 * This service file is common for search and submit services , related
 * with Review RRC REQUEST menu.
 */
@Stateless
public class ModifyRRCRequestService extends AbstractDAO<RRCRequest>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	public ModifyRRCRequestService(){
		super();
	}
	public  Page<SearchModifyRRCRequestTableDTO> search(
			SearchModifyRRCRequestFormDTO searchFormDTO,
			String userName, String passWord) {
		List<RRCRequest> listIntimations = new ArrayList<RRCRequest>(); 
		try{
		String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
		String rrcRequestNo = null != searchFormDTO.getRrcRequestNo() && !searchFormDTO.getRrcRequestNo().isEmpty() ? searchFormDTO.getRrcRequestNo() : null;
		Long cpuId = null != searchFormDTO && null != searchFormDTO.getCpu() ? searchFormDTO.getCpu().getId() : null;
		Long rrcRequestTypeId = null != searchFormDTO && null != searchFormDTO.getRrcRequestType() ? searchFormDTO.getRrcRequestType().getId() : null;
		Long rrcEligbilityTypeId = null != searchFormDTO && null != searchFormDTO.getEligibilityType() ? searchFormDTO.getEligibilityType().getId() : null;
		Date fromDate = null != searchFormDTO && null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
		Date toDate = null != searchFormDTO && null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<RRCRequest> criteriaQuery = criteriaBuilder.createQuery(RRCRequest.class);
		
		Root<RRCRequest> root = criteriaQuery.from(RRCRequest.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(null != intimationNo){
			Predicate condition1 = criteriaBuilder.like(root.<Claim>get("claim").<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
			conditionList.add(condition1);
			}
			if(null != rrcRequestNo){
			Predicate condition2 = criteriaBuilder.like(root.<String>get("rrcRequestNumber"), "%"+rrcRequestNo+"%");
			conditionList.add(condition2);
			}
			if(null != rrcRequestTypeId){
				Predicate condition3 = criteriaBuilder.equal(root.<MastersValue>get("requestedTypeId").<Long>get("key"), rrcRequestTypeId);
			conditionList.add(condition3);
			}
			if(null != cpuId){
				Predicate condition4 = criteriaBuilder.equal(root.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuId);
				conditionList.add(condition4);
				}
			if(null != rrcEligbilityTypeId)
			{
//				Predicate condition5 = criteriaBuilder.equal(root.<MastersValue>get("reviewerEligibilityTypeId").<Long>get("key"), rrcEligbilityTypeId);
				Predicate condition5 = criteriaBuilder.equal(root.<MastersValue>get("eligiblityTypeId").<Long>get("key"), rrcEligbilityTypeId);
//				Predicate condition52 = criteriaBuilder.isNull(root.<MastersValue>get("reviewerEligibilityTypeId").<Long>get("key"));
//				Predicate combinedCondition = criteriaBuilder.or(condition5,condition52);
				conditionList.add(condition5);
//				
//				Predicate reviewRRCType = criteriaBuilder.equal(root.<MastersValue>get("reviewerEligibilityTypeId").<Long>get("key"), rrcEligbilityTypeId);
			}
			if(null != fromDate)
			{
				Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("rrcInitiatedDate"), fromDate);
				//Predicate condition6 = criteriaBuilder.equal(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition6);
			}
			if(null != toDate)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();		
				Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("rrcInitiatedDate"), toDate);
				//Predicate condition7 = criteriaBuilder.equal(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition7);
			}
			/*
			Predicate condition8 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127);
			conditionList.add(condition8);*/
			

			Predicate condition8 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),125);
			Predicate condition9 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127);
			
			Predicate condition10 = criteriaBuilder.or(condition8,condition9);
			conditionList.add(condition10);
	        criteriaQuery.select(root).where(
			criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				
			
//			if(null == intimationNo && null == rrcRequestNo && null == rrcRequestTypeId && null == cpuId && null == rrcEligbilityTypeId && null == fromDate && null == toDate ){
//			criteriaQuery.select(root).where(criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),125));
//			criteriaQuery.select(root).where(criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127));
//			} else
//			{
//				conditionList.add(criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),125));
//				conditionList.add(criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127));
//		        criteriaQuery.select(root).where(
//				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
//		
//		/*criteriaQuery.select(root).where(criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127));*/
//			}
			
			List<javax.persistence.criteria.Order> orderByList = new ArrayList<javax.persistence.criteria.Order>();
			orderByList.add(criteriaBuilder.desc(root.<Long>get("rrcRequestKey")));
			criteriaQuery.orderBy(orderByList);
			
			//criteriaQuery.orderBy(Order.desc(root.("rrcRequestKey")));
		final TypedQuery<RRCRequest> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *25;
		}else{
			firtResult = 1;
		}
		/*if(null == intimationNo && null == rrcRequestNo && null == rrcRequestTypeId && null == cpuId){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}*/
//		listIntimations = typedQuery.getResultList();
		//listIntimations = typedQuery.getResultList();
		//Commenting below code , as it as some issues.
		//listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();
		
		listIntimations = typedQuery.getResultList();
		List<String> loginIdList = new ArrayList<String>();
	//	List<RRCDetails> rrcDetailsList = null;
		List<TmpEmployee> tmpEmployeeList =  null;
		
		if(null != listIntimations && !listIntimations.isEmpty())
		{
		for(RRCRequest rrcRequest: listIntimations){
			if(rrcRequest.getRequestorID() != null){
				loginIdList.add(rrcRequest.getRequestorID().toLowerCase());
			}
			//System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}
		
		List<SearchModifyRRCRequestTableDTO> tmpEmployeeDTOList = null;
		
		if(null != loginIdList && !loginIdList.isEmpty())
		{
			//if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
				tmpEmployeeList = new ArrayList<TmpEmployee>();
				final CriteriaBuilder rrcDetailsBuilder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<TmpEmployee> rrcDetailsCriteriaQuery = rrcDetailsBuilder
						.createQuery(TmpEmployee.class);
				Root<TmpEmployee> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
						.from(TmpEmployee.class);
				
				rrcDetailsCriteriaQuery.where(rrcDetailsBuilder.lower(searchRootInfoForRRCDetails.<String> get(
						"loginId")).in(loginIdList));
				
				/*rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<String> get(
						"loginId").in(loginIdList));*/
				
				final TypedQuery<TmpEmployee> rrcDetailsInfoQuery = entityManager
						.createQuery(rrcDetailsCriteriaQuery);
				tmpEmployeeList = rrcDetailsInfoQuery.getResultList();
		} 
	}
		
		/*if(null != listIntimations && !listIntimations.isEmpty())
		{
		for(RRCRequest rrcRequest: listIntimations){
			loginIdList.add(rrcRequest.getCreatedBy());
			//System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}
		List<SearchModifyRRCRequestTableDTO> rrcDetailsDTOList = null;
		if(null != loginIdList && !loginIdList.isEmpty())
			{
				//if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
					rrcDetailsList = new ArrayList<RRCDetails>();
					final CriteriaBuilder rrcDetailsBuilder = entityManager
							.getCriteriaBuilder();
					final CriteriaQuery<RRCDetails> rrcDetailsCriteriaQuery = rrcDetailsBuilder
							.createQuery(RRCDetails.class);
					Root<RRCDetails> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
							.from(RRCDetails.class);
					rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<String> get(
							"loginId").in(loginIdList));
					
					final TypedQuery<RRCDetails> rrcDetailsInfoQuery = entityManager
							.createQuery(rrcDetailsCriteriaQuery);
					rrcDetailsList = rrcDetailsInfoQuery.getResultList();
			} 
		}
		*/
//		List<RRCRequest> doList = listIntimations;
		
		List<RRCRequest> doList = getRevisedUniqueRecords(listIntimations);
		
		
		
		ModifyRRCRequestMapper modifyRRCRequestMapper =  ModifyRRCRequestMapper.getInstance();
		List<SearchModifyRRCRequestTableDTO> tableDTO = modifyRRCRequestMapper.getRRCRequestList(doList);
		
		
		List<String> intimationList = new ArrayList<String>();
		List<SearchModifyRRCRequestTableDTO> finalResultantTableList = new ArrayList<SearchModifyRRCRequestTableDTO>();
		if(null != tableDTO && !tableDTO.isEmpty())
		{
			for (SearchModifyRRCRequestTableDTO SearchModifyRRCRequestTableDTO : tableDTO) {
				if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
				{
					for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
						if(SearchModifyRRCRequestTableDTO.getRequestorId() != null && SearchModifyRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
						{
							SearchModifyRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmployeeWithNames());
						}
					}
				}
				if(!intimationList.contains(SearchModifyRRCRequestTableDTO.getRrcRequestNo()))
				{
					intimationList.add(SearchModifyRRCRequestTableDTO.getRrcRequestNo());
					finalResultantTableList.add(SearchModifyRRCRequestTableDTO);
				}
			}
		}
		
		Page<SearchModifyRRCRequestTableDTO> page = new Page<SearchModifyRRCRequestTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(tableDTO.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
	//	page.setPageItems(tableDTO);
		page.setPageItems(finalResultantTableList);
		page.setIsDbSearch(true);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	
	public List<ExtraEmployeeEffortDTO> getEmployeeDetailsFromRRCDetails(Long rrcRequestKey)
	{
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;
		Query query = entityManager.createNamedQuery("RRCDetails.findByRequestKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCDetails> rrcDetailsList = query.getResultList();
		if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
		{
			ReviewRRCRequestMapper mapper =ReviewRRCRequestMapper.getInstance();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);
		}
		return employeeEffortDTO;
		
	}
	
	public  List<RRCRequest> getRevisedUniqueRecords(List<RRCRequest> rrcRequestList){
		
		Long claimKey = 0l;
		
		List<Long> claimKeyList = new ArrayList<Long>();
		
		List<String> rrcRequestNumberList = new ArrayList<String>();
		
		for (RRCRequest rrcRequest : rrcRequestList) {
			
			if(! rrcRequestNumberList.contains(rrcRequest.getRrcRequestNumber())){
				rrcRequestNumberList.add(rrcRequest.getRrcRequestNumber());
			}
		}
		
		
		for (RRCRequest rrcRequest : rrcRequestList) {
			if(rrcRequest.getClaim() != null){
				claimKey = rrcRequest.getClaim().getKey();
				claimKeyList.add(claimKey);
//				break;
			}
		}
		
		Query findByStageKey = entityManager.createNamedQuery("RRCRequest.findByUniqueRecord");
		findByStageKey.setParameter("claimKey", claimKeyList);
		findByStageKey.setParameter("rrcRequestNumber", rrcRequestNumberList);
		
		List<RRCRequest> resultList = (List<RRCRequest>) findByStageKey.getResultList();
		
		return resultList;
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
			ProcessRRCRequestMapper mapper = ProcessRRCRequestMapper.getInstance();
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
			ProcessRRCRequestMapper mapper = ProcessRRCRequestMapper.getInstance();
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
		if(null != rrcDTO.getSavedAmount() && ("").equalsIgnoreCase(rrcDTO.getSavedAmount()))
			rrcDTO.setSavedAmount("0");
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
		
		
		if(rrcRequest.getModifierEligibilityTypeId() != null){
			rrcRequest.setEligiblityTypeId(rrcRequest.getModifierEligibilityTypeId());
		}
		
		
		rrcRequest.setRrcInitiatedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setProcessedBy(rrcDTO.getProcessedBy());
		rrcRequest.setProcessedDate(rrcDTO.getProcessedDate());
		rrcRequest.setReviewedDate(rrcDTO.getReviewedDate());
		rrcRequest.setReviewedBy(rrcDTO.getReviewedBy());
		
		rrcRequest.setModifiedBy(rrcDTO.getStrUserName());
		rrcRequest.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setRequestorID(rrcDTO.getRequestorID());
		
		
		entityManager.persist(rrcRequest);
		entityManager.flush();
		entityManager.refresh(rrcRequest);

		List<ExtraEmployeeEffortDTO> rrcCategoryList = rrcDTO.getRrcCategoryList();
		if(null != rrcCategoryList && !rrcCategoryList.isEmpty())
		{
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcCategoryList) {
				/*RRCCategory rrcCategory = reviewRRCRequestMapper.getRRCCategoryDetails(extraEmployeeEffortDTO);*/
				RRCCategory rrcCategory = new RRCCategory();
				rrcCategory.setRrcCategoryKey(extraEmployeeEffortDTO.getCategoryKey());
				MastersValue masCategory = new MastersValue();
				masCategory.setKey(extraEmployeeEffortDTO.getCategory().getId());
				rrcCategory.setCategoryId(masCategory);
				if(extraEmployeeEffortDTO.getSubCategory() !=null){
					rrcCategory.setSubCategorykey(extraEmployeeEffortDTO.getSubCategory().getId());
					if(extraEmployeeEffortDTO.getSourceOfIdentification() !=null){
						rrcCategory.setSourcekey(extraEmployeeEffortDTO.getSourceOfIdentification().getId());
					}
				}	
				rrcCategory.setRrcRequest(rrcRequest.getRrcRequestKey());
				rrcCategory.setTalkSpokento(extraEmployeeEffortDTO.getTalkSpokento());
				rrcCategory.setTalkSpokenDate(extraEmployeeEffortDTO.getTalkSpokenDate());
				rrcCategory.setTalkMobto(extraEmployeeEffortDTO.getTalkMobto() !=null ? Long.parseLong(extraEmployeeEffortDTO.getTalkMobto()) : null);
				if(null != rrcCategory.getRrcCategoryKey())
				{
					entityManager.merge(rrcCategory);
					entityManager.flush();
				}
				else
				{
					rrcCategory.setRrcRequest(rrcRequest.getRrcRequestKey());
					entityManager.persist(rrcCategory);
					entityManager.flush();
					entityManager.refresh(rrcCategory);
				}
			}
		}
		List<ExtraEmployeeEffortDTO> extraEffortByEmpList = rrcDTO.getEmployeeEffortList();
		if(null != extraEffortByEmpList && !extraEffortByEmpList.isEmpty())
		{
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEffortByEmpList) {
				RRCDetails rrcDetails = reviewRRCRequestMapper.getRRCDetailsForEmployee(extraEmployeeEffortDTO);
				if(rrcDetails.getEmployeeId() !=null){
					String employee[] = rrcDetails.getEmployeeId().split("-");
					rrcDetails.setEmployeeId(employee[0].trim());
					rrcDetails.setEmployeeName(employee[1].trim());
				}
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
	public Class<RRCRequest> getDTOClass() {
		// TODO Auto-generated method stub
		return RRCRequest.class;
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