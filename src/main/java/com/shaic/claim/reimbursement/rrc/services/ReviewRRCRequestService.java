
/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCCategorySource;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.RRCSubCategory;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;

/**
 * @author ntv.vijayar
 *
 * This service file is common for search and submit services , related
 * with Review RRC REQUEST menu.
 */
@Stateless
public class ReviewRRCRequestService extends AbstractDAO<RRCRequest>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	public ReviewRRCRequestService(){
		super();
	}
	public  Page<SearchReviewRRCRequestTableDTO> search(
			SearchReviewRRCRequestFormDTO searchFormDTO,
			String userName, String passWord) {
		
		String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
		String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
		String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
		
		List<RRCRequest> rrcRequestList = new ArrayList<RRCRequest>();
		List<Long> rrcRequestKeyList = new ArrayList<Long>();

		
		
		List<Map<String, Object>> taskProcedure = null ;

		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		workFlowMap= new WeakHashMap<Long, Object>();
		Integer totalRecords = 0;
		mapValues.put(SHAConstants.USER_ID, userName);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.REVIEW_RRC_REQUEST_CURRENT_QUEUE);		try{
			
		/*	DocReceiptACKType docReceiptACK = new DocReceiptACKType();
			
			
			ProductInfoType productInfo = new ProductInfoType();
			
			ClaimType claimType = new ClaimType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");
			
			docReceiptACK.setDocUpload("HEALTH");
			
			claimType.setCoverBenifitType("HEALTH");
			

			
			payloadBOType.setProductInfo(productInfo);
			
			payloadBOType.setIntimation(intimationType);
			
			payloadBOType.setClaim(claimType);
			
			payloadBOType.setDocReceiptACK(docReceiptACK);*/
			
			
			String intimationNo = "";
			if(null != searchFormDTO &&null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() )
			{

				mapValues.put(SHAConstants.INTIMATION_NO, searchFormDTO.getIntimationNo());
			}
			String rrcRequestNo = searchFormDTO.getRrcRequestNo();
	
			if(null != rrcRequestNo && !rrcRequestNo.isEmpty())
			{

				mapValues.put(SHAConstants.RRC_REQUEST_NUMBER, rrcRequestNo);
			}
			Long cpuId =  null;
		
			if(null != searchFormDTO && null !=  searchFormDTO.getCpu())
			{
				
				cpuId = searchFormDTO.getCpu().getId();
				mapValues.put(SHAConstants.CPU_CODE, String.valueOf(cpuId));
				/*ClaimRequestType claimRequest = new ClaimRequestType();
				claimRequest.setCpuCode(String.valueOf(cpuId));
				payloadBOType.setClaimRequest(claimRequest);*/
				//payloadBOType.getClaimRequest().setCpuCode(String.valueOf(cpuId));
			}
			
			
			String rrcRequestTypeId = null;
			if(null != searchFormDTO && null != searchFormDTO.getRrcRequestType())
			{
				
				rrcRequestTypeId = searchFormDTO.getRrcRequestType().getValue();
				if((SHAConstants.ON_HOLD).equals(rrcRequestTypeId))
				{
					//rrcType.setRequestType(SHAConstants.HOLD);
					mapValues.put(SHAConstants.RRC_REQUEST_TYPE, SHAConstants.HOLD);
				}
				else
				{
					mapValues.put(SHAConstants.RRC_REQUEST_TYPE, rrcRequestTypeId.toUpperCase());
					//rrcType.setRequestType((rrcRequestTypeId));
				}
				
			}
			Long rrcEligbilityTypeId = null ;
			if(null != searchFormDTO && null != searchFormDTO.getEligibilityType())
			{
				
			//	rrcEligbilityTypeId = searchFormDTO.getEligibilityType().getId();
				rrcEligbilityTypeId = searchFormDTO.getEligibilityType().getId();
				mapValues.put(SHAConstants.RRC_ELIGIBILITY_TYPE, rrcEligbilityTypeId);
				//rrcType.setEligibilityType(searchFormDTO.getEligibilityType().getValue().toUpperCase());
				//payloadBOType.setRrc(rrcType);
			}
			
			Date fromDate = null;
			if(null != searchFormDTO && null !=  searchFormDTO.getFromDate())
			{				
				fromDate = searchFormDTO.getFromDate();
				
			}
			
			Date toDate = null;
			if(null != searchFormDTO && null !=  searchFormDTO.getToDate())
			{
				toDate = searchFormDTO.getToDate();
				
			}
			
			
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							//priority = null;
							mapValues.put(SHAConstants.PRIORITY, null);
						}
					mapValues.put(SHAConstants.PRIORITY, priority);
				//	classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
							mapValues.put(SHAConstants.RECORD_TYPE, null);
						}
						else
						{
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
					}
					

					/*if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
					
					payloadBOType.setClassification(classification);*/
			}
			
			
			Pageable pageable = searchFormDTO.getPageable();
			
			pageable.setPageNumber(1);
			pageable.setPageSize(25);
			
			
			

			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
						String strKeyValue = (String) outPutArray.get(SHAConstants.RRC_REQUEST_KEY);
						if(null != strKeyValue)
						{
							Long keyValue = Long.parseLong(strKeyValue);
							rrcRequestKeyList.add(keyValue);
							workFlowMap.put(keyValue,outPutArray);
						}
						
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			
		/*	if(null != taskList){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				
				for(HumanTask humanTask : humanTaskList) {
					PayloadBOType payloadBO = humanTask.getPayload();
					if(null != payloadBO.getRrc()) {
						rrcRequestKeyList.add(payloadBO.getRrc().getKey());
						humanTaskMap.put(payloadBO.getRrc().getKey(), humanTask);
					}
				}
			}*/
			
	/*	String intimationNo = !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
		String rrcRequestNo = !searchFormDTO.getRrcRequestNo().isEmpty() ? searchFormDTO.getRrcRequestNo() : null;
		Long cpuId = null != searchFormDTO.getCpu() ? searchFormDTO.getCpu().getId() : null;
		Long rrcRequestTypeId = null != searchFormDTO.getRrcRequestType() ? searchFormDTO.getRrcRequestType().getId() : null;
		Long rrcEligbilityTypeId = null != searchFormDTO.getEligibilityType() ? searchFormDTO.getEligibilityType().getId() : null;
		Date fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
		Date toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;*/
		
		/*final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
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
			Predicate condition3 = criteriaBuilder.equal(root.<MastersList>get("requestedStageId").<Long>get("key"), rrcRequestTypeId);
		conditionList.add(condition3);
		}
		if(null != cpuId){
			Predicate condition4 = criteriaBuilder.equal(root.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuId);
			conditionList.add(condition4);
			}
		if(null != rrcEligbilityTypeId)
		{
			Predicate condition5 = criteriaBuilder.equal(root.<MastersValue>get("eligiblityTypeId").<Long>get("key"), rrcEligbilityTypeId);
			conditionList.add(condition5);
		}
		if(null != fromDate)
		{
			Predicate condition6 = criteriaBuilder.equal(root.<Date>get("createdDate"), fromDate);
			conditionList.add(condition6);
		}
		if(null != toDate)
		{
			Predicate condition7 = criteriaBuilder.equal(root.<Date>get("createdDate"), toDate);
			conditionList.add(condition7);
		}*/
			
		
		/*if(null == intimationNo && null == rrcRequestNo && null == rrcRequestTypeId && null == cpuId){
		//if(registerMobileNo == null && policyNo == null && healthCardNo == null){
			criteriaQuery.select(root);
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}*/
		//final TypedQuery<RRCRequest> typedQuery = entityManager.createQuery(criteriaQuery);
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
		//FIX done
		if(!rrcRequestKeyList.isEmpty()){
			rrcRequestList = getRRCRequestList(rrcRequestKeyList,firtResult);
		}
		
		List<String> loginIdList = new ArrayList<String>();
	//	List<RRCDetails> rrcDetailsList = null;
	
		
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			for(RRCRequest rrcRequest: rrcRequestList){
				if(rrcRequest.getRequestorID() != null){
				loginIdList.add(rrcRequest.getRequestorID().toLowerCase());
				}
			}	
		}
		
		List<TmpEmployee> tmpEmployeeList = new ArrayList<TmpEmployee>();
		
		if(!loginIdList.isEmpty()){
			tmpEmployeeList = getTmpEmployeeList(loginIdList);
		}
		
		/*if(null != listIntimations && !listIntimations.isEmpty())
		{
		for(RRCRequest rrcRequest: listIntimations){
			loginIdList.add(rrcRequest.getCreatedBy());
			//System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}*/
		
		/*List<SearchReviewRRCRequestTableDTO> tmpEmployeeDTOList = null;
		
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
				rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<String> get(
						"loginId").in(loginIdList));
				
				final TypedQuery<TmpEmployee> rrcDetailsInfoQuery = entityManager
						.createQuery(rrcDetailsCriteriaQuery);
				tmpEmployeeList = rrcDetailsInfoQuery.getResultList();
		} 
	}*/
		
		/*if(null != listIntimations && !listIntimations.isEmpty())
		{
		for(RRCRequest rrcRequest: listIntimations){
			loginIdList.add(rrcRequest.getCreatedBy());
			//System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}
		List<SearchReviewRRCRequestTableDTO> rrcDetailsDTOList = null;
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
		ReviewRRCRequestMapper reviewRRCRequestMapper = ReviewRRCRequestMapper.getInstance();
		List<SearchReviewRRCRequestTableDTO> tableDTO = reviewRRCRequestMapper.getRRCRequestList(rrcRequestList);
		
		if(null != tableDTO && !tableDTO.isEmpty())
		{
			for (int index = 0; index < tableDTO.size() ; index++)
			{
				SearchReviewRRCRequestTableDTO searchReviewRRCRequestTableDTO  = tableDTO.get(index);
				if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
				{
					for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
						if(searchReviewRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
						{
							searchReviewRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmpFirstName());
						}
					}
				}
				
				//searchReviewRRCRequestTableDTO.setRrcHumanTask(humanTaskMap.get(searchReviewRRCRequestTableDTO.getKey()));;
				Object workflowKey = workFlowMap.get(searchReviewRRCRequestTableDTO.getKey());
//				tableDto.setDbOutArray(workflowKey);
				tableDTO.get(index).setDbOutArray(workflowKey);
				
			}
			/*for (SearchReviewRRCRequestTableDTO SearchReviewRRCRequestTableDTO : tableDTO) {
				if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
				{
					for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
						if(SearchReviewRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
						{
							SearchReviewRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmployeeWithNames());
						}
					}
				}
				SearchReviewRRCRequestTableDTO.setRrcHumanTask(humanTaskMap.get(SearchReviewRRCRequestTableDTO.getKey()));
			}*/
		}
		
		Page<SearchReviewRRCRequestTableDTO> page = new Page<SearchReviewRRCRequestTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(tableDTO.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(tableDTO);
		page.setTotalRecords(totalRecords);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
		
	
	}
	
	public Claim getClaimByIntimation(Long intimationKey) {
		if (intimationKey != null) {
			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			List<Claim> a_claimList = null;
			try {
				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}
				if(a_claimList.size() > 0) {
					return 	a_claimList.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
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
			for (RRCDetails rrcDetails : rrcDetailsList) {
				entityManager.refresh(rrcDetails);
			}
			ReviewRRCRequestMapper mapper =ReviewRRCRequestMapper.getInstance();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);
			
			for (ExtraEmployeeEffortDTO employeeEffort : employeeEffortDTO) {
				if(employeeEffort.getEmployeeId() != null){
					TmpEmployee employeeName = getEmployeeNameWithInactiveUser(employeeEffort.getEmployeeId());
					if(null != employeeName){
						SelectValue selectValue = new SelectValue();
						selectValue.setId(employeeName.getKey());
						selectValue.setValue(employeeName.getEmpId()+"-"+employeeName.getEmpFirstName());
						employeeEffort.setSelEmployeeId(selectValue);
					}
				}
			}
		}
		return employeeEffortDTO;
		
	}
	
	public List<ExtraEmployeeEffortDTO> getEmployeeDetailsFromRRCDetailsForStatusScreen(Long rrcRequestKey)
	{
		String loginUserIdLower = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		final String loginUserId = loginUserIdLower.toUpperCase();
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;
		Query query = entityManager.createNamedQuery("RRCDetails.findByEmployeeId");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		query = query.setParameter("employeeId", loginUserId);
		List<RRCDetails> rrcDetailsList = query.getResultList();
		if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
		{
			for (RRCDetails rrcDetails : rrcDetailsList) {
				entityManager.refresh(rrcDetails);
			}
			ReviewRRCRequestMapper mapper =ReviewRRCRequestMapper.getInstance();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);
			
			for (ExtraEmployeeEffortDTO employeeEffort : employeeEffortDTO) {
				if(employeeEffort.getEmployeeId() != null){
					TmpEmployee employeeName = getEmployeeNameWithInactiveUser(employeeEffort.getEmployeeId());
					if(null != employeeName){
						if (employeeName != null && employeeName.getEmpId() != null 
								&& employeeName.getEmpId().equalsIgnoreCase(loginUserId)) {
							SelectValue selectValue = new SelectValue();
							selectValue.setId(employeeName.getKey());
							selectValue.setValue(employeeName.getEmpId()+"-"+employeeName.getEmpFirstName());
							employeeEffort.setSelEmployeeId(selectValue);
						}
					}
				}
			}
		}
		return employeeEffortDTO;
		
	}
	
	  private TmpEmployee getEmployeeName(String initiatorId)
		{
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	  
	  private TmpEmployee getEmployeeNameWithInactiveUser(String initiatorId)
		{
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginIdWithInactive").setParameter("loginId", initiatorId.toLowerCase());
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
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
				extraEmpEffortDTO.setCategoryKey(rrcCategory.getRrcCategoryKey());
				SelectValue selCategory = new SelectValue();
				selCategory.setId(rrcCategory.getCategoryId().getKey());
				selCategory.setValue(rrcCategory.getCategoryId().getValue());
				extraEmpEffortDTO.setCategory(selCategory);
				extraEmpEffortDTO.setRrcCategoryKey(rrcCategory.getKey());
				if(rrcCategory.getSubCategorykey() !=null){
					setRRCSubCatandSource(rrcCategory,extraEmpEffortDTO);
				}
				extraEmpEffortDTO.setTalkSpokento(rrcCategory.getTalkSpokento());
				extraEmpEffortDTO.setTalkSpokenDate(rrcCategory.getTalkSpokenDate());
				extraEmpEffortDTO.setTalkMobto(rrcCategory.getTalkMobto() !=null ? rrcCategory.getTalkMobto().toString() : null);
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
	public String submitReviewRRCRequest(RRCDTO rrcDTO)
	{
		String rrcRequestNo = "";
		try
		{
			rrcRequestNo = saveReviewRRCRequestValues(rrcDTO);
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
	
	private String saveReviewRRCRequestValues(RRCDTO rrcDTO)
	{
		
		ReviewRRCRequestMapper reviewRRCRequestMapper = ReviewRRCRequestMapper.getInstance();
		/**
		 * Since RRC request stage and status is not finalized, 
		 * setting zonal stage and status for testing purpose. Once
		 * stage and status for RRC request is finalized, the same
		 * will be incorporated.
		 **/
		Reimbursement reimbursement = getReimbursementByKey(rrcDTO.getRodKey());
		
		rrcDTO.setStageKey(ReferenceTable.RRC_STAGE);
		rrcDTO.setStatusKey(ReferenceTable.RRC_REQUEST_REVIEWED_STATUS);
		//String rrcReqNo = generateRRCRequestNo(rrcDTO.getNewIntimationDTO().getIntimationId(),rrcDTO.getClaimDTO().getKey());
		//String rrcReqNo = generateRRCRequestNo(rrcDTO.getNewIntimationDTO().getIntimationId(),rrcDTO.getClaimDTO().getKey());
		String rrcReqNo = rrcDTO.getRrcRequestNo();
		RRCRequest rrcRequest = reviewRRCRequestMapper.getRRCRequestData(rrcDTO);
 		rrcRequest.setReimbursement(reimbursement);
		if(null != reimbursement)
			rrcRequest.setClaim(reimbursement.getClaim());
		else
		{
			Claim claim = getClaimByClaimKey(rrcDTO.getClaimKey());
			rrcRequest.setClaim(claim);
		}
		
		if(null != rrcDTO.getProcessingStageKey())
		{
			Stage stage = new Stage();
			stage.setKey(rrcDTO.getProcessingStageKey());
			rrcRequest.setRequestedStageId(stage);
		}
		
		rrcRequest.setRrcRequestNumber(rrcReqNo);
		rrcRequest.setActiveStatus(1L);
 		rrcRequest.setRrcType("RRC REVIEW");
		
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
		
		rrcRequest.setRequestedTypeId(masRequestedTypeId);
		rrcRequest.setReviewedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setReviewedBy(rrcDTO.getStrUserName());
		rrcRequest.setStatus(rrcStatus);
		rrcRequest.setStage(rrcStage);
		
		rrcRequest.setRrcInitiatedDate(rrcDTO.getRrcintiatedDate());
		rrcRequest.setProcessedBy(rrcDTO.getProcessedBy());
		rrcRequest.setProcessedDate(rrcDTO.getProcessedDate());
		rrcRequest.setReviewedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setReviewedBy(rrcDTO.getStrUserName());
		
		rrcRequest.setCreatedBy(rrcDTO.getCreatedBy());
		rrcRequest.setCreatedDate(rrcDTO.getCreatedDate());
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
				
				/*if(null != rrcDetails.getRrcDetailsKey())
				{
					entityManager.merge(rrcDetails);
					entityManager.flush();
					
				}*/
			//	entityManager.refresh(rrcDetails);
			//}
			/*else
			{
				//rrcDetails.setRrcRequest(rrcRequest);
				entityManager.persist(rrcDetails);
				entityManager.flush();
				entityManager.refresh(rrcDetails);
			}*/
				
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
	
		//if(submitRequestRRCTaskToBPM(rrcDTO))
		if(submitReviewRRCTaskToDB(rrcDTO))
		{
			return rrcReqNo;
		}
		else
		{
			return null;
		}
		
		/*if(null != rrcRequest.getKey())
		{
			entityManager.merge(rrcRequest);
			entityManager.flush();
			entityManager.refresh(rrcRequest);
		}*/
		
		//return rrcReqNo;
		}
		
	//}
	
	
	/*public Boolean submitRequestRRCTaskToBPM(RRCDTO rrcDTO)
	{
		try
		{
			SubmitReviewRRCRequestTask reviewProcessRRCRequestTask = BPMClientContext.getReviewRRCRequestSubmitTask(rrcDTO.getStrUserName(), rrcDTO.getStrPassword());
			
			HumanTask humanTaskForRRC = rrcDTO.getRrcHumanTask();
			PayloadBOType payloadBO = humanTaskForRRC.getPayload();
			humanTaskForRRC.setOutcome("REVIEWED");
			// DocReceiptACKType receiptAckType = new DocReceiptACKType();
			// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
			// receiptAckType.setKey(reimbursement.getKey());
			RRCType rrcType = payloadBO.getRrc();
			rrcType.setOutcome("REVIEWED");
			rrcType.setStatus(rrcDTO.getRrcStatus());
			
			if(null != rrcDTO.getEligibility())
			{
				rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
			}
			
			rrcType.setSource("FRESH");
			
			rrcType.setRequestType("FRESH");
			
//			Status status = entityManager.find(Status.class, ReferenceTable.RRC_REQUEST_REVIEWED_STATUS);
//			ClassificationType classification = payloadBO.getClassification();
//			classification.setSource(status.getProcessValue());
//			payloadBO.setClassification(classification);
			
			payloadBO.setRrc(rrcType);
			humanTaskForRRC.setPayload(payloadBO);
			reviewProcessRRCRequestTask.execute(rrcDTO.getStrUserName(), humanTaskForRRC);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}*/
	
	public Boolean submitReviewRRCTaskToDB(RRCDTO rrcDTO)
	{
		try
		{
			//SubmitReviewRRCRequestTask reviewProcessRRCRequestTask = BPMClientContext.getReviewRRCRequestSubmitTask(rrcDTO.getStrUserName(), rrcDTO.getStrPassword());
			Map<String, Object> wrkFlowMap = (Map<String, Object>) rrcDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME , SHAConstants.REVIEW_RRC_OUTCOME);
			//humanTaskForRRC.setOutcome("REVIEWED");
			// DocReceiptACKType receiptAckType = new DocReceiptACKType();
			// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
			// receiptAckType.setKey(reimbursement.getKey());
			/*RRCType rrcType = payloadBO.getRrc();
			rrcType.setOutcome("REVIEWED");
			rrcType.setStatus(rrcDTO.getRrcStatus());*/
			
			if(null != rrcDTO.getEligibility())
			{
				//rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
				wrkFlowMap.put(SHAConstants.RRC_ELIGIBILITY_TYPE, rrcDTO.getEligibility().getId());
			}
			
			//rrcType.setSource("FRESH");
			
			//rrcType.setRequestType("FRESH");
			wrkFlowMap.put(SHAConstants.RRC_REQUEST_TYPE, "FRESH");
			
//			Status status = entityManager.find(Status.class, ReferenceTable.RRC_REQUEST_REVIEWED_STATUS);
//			ClassificationType classification = payloadBO.getClassification();
//			classification.setSource(status.getProcessValue());
//			payloadBO.setClassification(classification);
			
			/*payloadBO.setRrc(rrcType);
			humanTaskForRRC.setPayload(payloadBO);
			reviewProcessRRCRequestTask.execute(rrcDTO.getStrUserName(), humanTaskForRRC);*/
			
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
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
	
	public List<RRCRequest> getRRCRequestList(List<Long> rrcRequestKeyList,int firtResult )
	{
		
		final CriteriaBuilder rrcRequestBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<RRCRequest> criteriaQuery = rrcRequestBuilder
				.createQuery(RRCRequest.class);
		Root<RRCRequest> searchRootForRRCRequest = criteriaQuery
				.from(RRCRequest.class);
		criteriaQuery.where(searchRootForRRCRequest.<Long> get("rrcRequestKey")
				.in(rrcRequestKeyList));
		final TypedQuery<RRCRequest> rrcRequestQuery = entityManager
				.createQuery(criteriaQuery);
	//	List<RRCRequest> rrcRequestServiceList = rrcRequestQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();
		
		List<RRCRequest> rrcRequestServiceList = rrcRequestQuery.getResultList();
		if(null != rrcRequestServiceList && !rrcRequestServiceList.isEmpty())
		{
			for (RRCRequest rrcRequest : rrcRequestServiceList) {
				
				entityManager.refresh(rrcRequest);
			}
		}
		
		return rrcRequestServiceList;
		

		
	}
	
	public List<TmpEmployee> getTmpEmployeeList(List<String> loginIdList)
	{
		final CriteriaBuilder rrcDetailsBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<TmpEmployee> rrcDetailsCriteriaQuery = rrcDetailsBuilder
				.createQuery(TmpEmployee.class);
		Root<TmpEmployee> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
				.from(TmpEmployee.class);
		
//		rrcDetailsCriteriaQuery.where(rrcDetailsBuilder.upper(searchRootInfoForRRCDetails.<String> get(
//				"loginId")).in(loginIdList));
		
		rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<String> get(
				"loginId").in(loginIdList));
		
		final TypedQuery<TmpEmployee> rrcDetailsInfoQuery = entityManager
				.createQuery(rrcDetailsCriteriaQuery);
		return rrcDetailsInfoQuery.getResultList();
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
	public void setRRCSubCatandSource(RRCCategory rrcCategory,ExtraEmployeeEffortDTO extraEmpEffortDTO)
	{
		Query query = entityManager.createNamedQuery("RRCSubCategory.findByKey");
		query = query.setParameter("key", rrcCategory.getSubCategorykey());
		List<RRCSubCategory> rrcSubCategoryList = (List<RRCSubCategory>)query.getResultList();
		if(null != rrcSubCategoryList && !rrcSubCategoryList.isEmpty())
		{
			RRCSubCategory subCategory = rrcSubCategoryList.get(0);
			SelectValue subCat =new SelectValue();
			subCat.setId(subCategory.getKey());
			subCat.setValue(subCategory.getSubCategoryName());
			extraEmpEffortDTO.setSubCategory(subCat);
		}
		if(rrcCategory.getSourcekey() !=null){
			query = entityManager.createNamedQuery("RRCCategorySource.findByKey");
			query = query.setParameter("key", rrcCategory.getSourcekey());
			List<RRCCategorySource> rrcSourceList = (List<RRCCategorySource>)query.getResultList();
			if(null != rrcSourceList && !rrcSourceList.isEmpty())
			{
				RRCCategorySource categorySource = rrcSourceList.get(0);
				SelectValue source =new SelectValue();
				source.setId(categorySource.getKey());
				source.setValue(categorySource.getSourceName());
				extraEmpEffortDTO.setSourceOfIdentification(source);	
			}
		}
	}
	
	public RRCRequest getRrcRequestList(String rrcRequestKey) {
		
		if(rrcRequestKey != null) {
			Query findemploye = entityManager.createNamedQuery("RRCRequest.CountAckByRRCRequestNo").setParameter("rrcRequestNumber", rrcRequestKey);
			List<RRCRequest> tmpEmploye = (List<RRCRequest>) findemploye.getResultList();
			
			if(tmpEmploye != null && ! tmpEmploye.isEmpty()){
				return tmpEmploye.get(0);
			}
			
			return null; 
			}
			else {
				return null;
			}
	}
}