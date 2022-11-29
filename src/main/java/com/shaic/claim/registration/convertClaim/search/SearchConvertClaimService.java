package com.shaic.claim.registration.convertClaim.search;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class SearchConvertClaimService extends AbstractDAO<Claim> {
	
	/**
	 * Entity manager is created to load LOB value from master service.
	 * When created instance for master service and tried to reuse the same, 
	 * faced error in entity manager invocation. Also when user, @Inject or @EJB
	 * annotation, faced issues in invocation. Hence for time being created
	 * entity manager instance and using the same. Later will check with siva 
	 * for code.
	 * */
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchConvertClaimService() {
		super();
	}

/*	public Page<SearchConvertClaimTableDto> bpmnSearch(
			SearchConvertClaimFormDto formDTO, String userName, String passWord) {
		try
		{
			String strIntimationNo =  "";//formDTO.getIntimationNumber();
			String strPolicyNo = "";//formDTO.getPolicyNumber();
			SelectValue cpuCode = formDTO.getCpuCode();
			String strCpuCode = "";
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			
			PayloadBOType payloadBO = new PayloadBOType();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();;
			
			
			IntimationType intimationType = new IntimationType();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType reimbursmentIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType(); 
			
			PolicyType policyType = new PolicyType();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType reimbursementPolicyType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
			
			ProductInfoType prodType = new ProductInfoType();			
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType reimbProdType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
			
			if(formDTO.getLobKey() != null && (ReferenceTable.PA_LOB_KEY).equals(formDTO.getLobKey())){
				prodType.setLob(SHAConstants.PA_LOB);
				reimbProdType.setLob(SHAConstants.PA_LOB);
				
//				prodType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
//				reimbProdType.setLobType(SHAConstants.HEALTH_LOB_FLAG);		
				
				payloadBO.setProductInfo(prodType);
				reimbursementPayloadBO.setProductInfo(reimbProdType);
			}
			else{
				prodType.setLob(SHAConstants.HEALTH_LOB);
				reimbProdType.setLob(SHAConstants.HEALTH_LOB);
				
//				payloadBO.setProductInfo(prodType);
//				reimbursementPayloadBO.setProductInfo(reimbProdType);
			}

			if(null != formDTO.getIntimationNumber() && ! formDTO.getIntimationNumber().equals("" ))
			{
 				strIntimationNo = formDTO.getIntimationNumber();
				intimationType.setIntimationNumber(strIntimationNo);
				reimbursmentIntimationType.setIntimationNumber(strIntimationNo);
				
				payloadBO.setIntimation(intimationType);
				reimbursementPayloadBO.setIntimation(reimbursmentIntimationType);
			}
			
			
			if(null != formDTO.getPolicyNumber() && ! formDTO.getPolicyNumber().equals("" ))
			{
				strPolicyNo = formDTO.getPolicyNumber();
				policyType.setPolicyId(strPolicyNo);
				reimbursementPolicyType.setPolicyId(strPolicyNo);
				
				payloadBO.setPolicy(policyType);
				reimbursementPayloadBO.setPolicy(reimbursementPolicyType);
				
			}

			ClaimRequestType claimRequestType = new ClaimRequestType();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType reimbursementClaimRequestType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
			if(null != cpuCode)
			{				
			  strCpuCode = String.valueOf(cpuCode.getId());
			  claimRequestType.setCpuCode(strCpuCode);
			  reimbursementClaimRequestType.setCpuCode(strCpuCode);
			  
			  payloadBO.setClaimRequest(claimRequestType);
			  reimbursementPayloadBO.setClaimRequest(reimbursementClaimRequestType);
			}
			
         ClassificationType classification = null;
         com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = null;
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					classification = new ClassificationType();
					reimbursementClassification = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					classification.setPriority(priority);
					reimbursementClassification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						classification.setSource(source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
						}
						classification.setType(type);
						reimbursementClassification.setType(type);
					}
										
					 payloadBO.setClassification(classification);
					 reimbursementPayloadBO.setClassification(reimbursementClassification);
					 
			}
<<<<<<< HEAD

			ConversionQF convQF = null;			
			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strCpuCode || ("").equals(strCpuCode))))
			{
				convQF = new ConversionQF();
				convQF.setIntimationNumber(strIntimationNo);
				convQF.setPolicyId(strPolicyNo);
			}
		    
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
		    
			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer.valueOf(BPMClientContext.PAGE_SIZE) : 10);
			pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer.valueOf(BPMClientContext.PAGE_NUMBER) : 1);
			
			
			List<SearchConvertClaimTableDto> searchConvertClaimTableDTO  = new ArrayList<SearchConvertClaimTableDto>();
			com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask processConvTask = BPMClientContext.getConvertClaimSearchTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =  processConvTask.getTasks(userName, pageable, payloadBO);  //userName="zonaluser1"
			
			
			List<HumanTask> taskList = tasks.getHumanTasks();
			
			if(taskList != null && taskList.size() <= Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE)){
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) - taskList.size() : 10);

			}
			
			pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer.valueOf(BPMClientContext.PAGE_SIZE) : 10);
			pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer.valueOf(BPMClientContext.PAGE_NUMBER) : 1);
			
			AckProcessConvertClaimToReimbTask convertClaimTaskFromAck = BPMClientContext.getConvertClaimTaskFromAck(userName, passWord);
			PagedTaskList reimbursementTask = convertClaimTaskFromAck.getTasks(userName, pageable, reimbursementPayloadBO);
			//Map to set human task to table DTO.
			
			Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
			Map<Long,Integer> taskNumberMap = new HashMap<Long, Integer>();
			if(null != tasks)
			{
				List<HumanTask> humanTasks = tasks.getHumanTasks();
				List<Long> keys = new ArrayList<Long>();  
				
				for (HumanTask item: humanTasks)
			    {
					
					PayloadBOType payload = item.getPayloadCashless();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payload.getClaim();
					if(null != claimType)
					{
						//Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
						Long keyValue = claimType.getKey();
						keys.add(keyValue);
						humanTaskMap.put(keyValue, item);
						taskNumberMap.put(keyValue, item.getNumber());
						if(keys.size() == 10){
							break;
						}
					}
<<<<<<< HEAD
					BPMClientContext.printPayloadElement(item.getPayload());
					//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
					Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
					
					if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
					{
						Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
						keys.add(keyValue);	
						humanTaskMap.put(keyValue, item);
					}
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
			    }
				
				if(null != reimbursementTask)
				{
					List<HumanTask> humanTask1 = reimbursementTask.getHumanTasks();
					
					for (HumanTask item: humanTask1)
				    {
						
						com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = item.getPayload();
						ClaimType claimKey = payload.getClaim();;
						if(null != claimKey)
						{
							Long keyValue = claimKey.getKey();
							keys.add(keyValue);	
							humanTaskMap.put(keyValue, item);
							taskNumberMap.put(keyValue, item.getNumber());
						}
<<<<<<< HEAD
						BPMClientContext.printPayloadElement(item.getPayload());
						//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
						Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
						
						if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
						{
							Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
							keys.add(keyValue);	
							humanTaskMap.put(keyValue, item);
						}
=======

>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
				    }
				
				}
	
				if(null != keys && 0!= keys.size())
				{
					List<Claim> resultList = new ArrayList<Claim>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
					List<Claim> pageItemList = resultList;
					searchConvertClaimTableDTO = SearchConvertClaimMapper.getInstance()
							.getSearchConvertClaimTableDTO(pageItemList);
					
					//Assigning LOB value to Table DTO.
					
					ListIterator<Claim> iterPED = pageItemList.listIterator();
					List<Long>  hospTypeList = new ArrayList<Long>();
					
					while (iterPED.hasNext())
					{
						 Claim claim = iterPED.next();
<<<<<<< HEAD
						 MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
						 Long hospitalTypeId = hospTypeInfo.getKey();
=======
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
						 Long hospitalTypeId = claim.getIntimation().getHospital();
						 hospTypeList.add(hospitalTypeId);
					}
					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
					List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();				
					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
					searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
							.getHospitalInfoList(resultListForHospitalInfo);
					
					for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO )
					{
						objSearchConvClaimTableDTO.setAccDeath(objSearchConvClaimTableDTO.getAccDeath()!= null ? ((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(objSearchConvClaimTableDTO.getAccDeath()) ? SHAConstants.ACCIDENT : SHAConstants.DEATH): "");
						
						//MastersValue objForLOB = masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
						if(null != objSearchConvClaimTableDTO.getLob() && !("").equals(objSearchConvClaimTableDTO.getLob()))
						{
							objSearchConvClaimTableDTO.setLob(loadLobValue(Long.parseLong(objSearchConvClaimTableDTO.getLob())));
						}
						//Human task assigned to table dto
						objSearchConvClaimTableDTO.setHumanTask(humanTaskMap.get(objSearchConvClaimTableDTO.getKey()));
						objSearchConvClaimTableDTO.setTaskNumber(taskNumberMap.get(objSearchConvClaimTableDTO.getKey()));
						
						for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList)
						{
<<<<<<< HEAD
							
							*//**
=======
							/**
							 * hospitaltype is set for PA convert claim Search
							 */
						//	objSearchConvClaimTableDTO.setHospitalType(objSearchConvClaimTableDTOForHospitalInfo.getHospitalType());
							/**
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
							 * Hospital type. In Hospital.java , we store the key. 
							 * 
							 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
							 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
							 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
							 * *//*
							if(objSearchConvClaimTableDTO.getHospitalTypeId() != null && objSearchConvClaimTableDTOForHospitalInfo.getKey() != null && objSearchConvClaimTableDTO.getHospitalTypeId().equals(objSearchConvClaimTableDTOForHospitalInfo.getKey()))
							{
								objSearchConvClaimTableDTO.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo.getHospitalName());
								break;
							}
						}
						
					}
				}
			}
			Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
			page.setPageItems(searchConvertClaimTableDTO);
			page.setTotalRecords(tasks.getTotalRecords());

			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
*/
	@SuppressWarnings("unchecked")
	public HospitalAcknowledge getHospitalAcknowledgementByKey(
			Long acknowledHospitalKey) {

		Query findByKey = entityManager
				.createNamedQuery("HospitalAcknowledge.findAll");

		List<HospitalAcknowledge> hospitalList = (List<HospitalAcknowledge>) findByKey
				.getResultList();

		if (!hospitalList.isEmpty()) {
			return hospitalList.get(0);

		}
		return null;
	}

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
	}
	
	/**
	 * Method to load Lob value
	 * 
	 * */
	public String loadLobValue(Long lobID)
	{
		MastersValue a_mastersValue = new MastersValue();
		if (lobID != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", lobID);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue.getValue();
		
		
	}
	
	
	public Page<SearchConvertClaimTableDto> searchConvertToReimbursement(SearchConvertClaimFormDto formDTO, String userName, String passWord){
		
		String intimationNo = !formDTO.getIntimationNumber().isEmpty() ? formDTO.getIntimationNumber() :null;
		String policyNo = !formDTO.getPolicyNumber().isEmpty() ? formDTO.getPolicyNumber() : null;
		Long cpuCode = formDTO.getCpuCode() != null ?  formDTO.getCpuCode().getId() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		List<Claim> listOfClaim = new ArrayList<Claim>();
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(cpuCode != null){
		//Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<String>get("cpuCode"), "%"+cpuCode+"%");
		Predicate condition3 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode"), cpuCode);
		conditionList.add(condition3);
		}
		
		//Predicate pacondition = criteriaBuilder.equal(root.<Long>get("lobId"), ReferenceTable.HEALTH_LOB_KEY);
		Predicate condition8 = criteriaBuilder.notLike(root.<Intimation>get("intimation").<String>get("processClaimType"), "%"+SHAConstants.PA_TYPE+"%");
		conditionList.add(condition8);

		
		List<Long> claimTypeKey = new ArrayList<Long>();
		      claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				Expression<Long> exp = root.<MastersValue> get("claimType").<Long> get("key");
				
		Predicate condition4 = exp.in(claimTypeKey);
		conditionList.add(condition4);
				
		if(intimationNo == null && policyNo == null && cpuCode == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null && cpuCode == null /*&& listOfClaim.size()>10*/){
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listOfClaim = typedQuery.getResultList();
		}
		
		List<Long> keys = new ArrayList<Long>();
		
		List<Claim> resultList = new ArrayList<Claim>();
		
		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		
		List<SearchConvertClaimTableDto> searchConvertClaimTableDTO  = new ArrayList<SearchConvertClaimTableDto>();
		
		if(listOfClaim != null && ! listOfClaim.isEmpty()){
			for (Claim claim : listOfClaim) {
//				if(getWaitingForPreauthTask(claim)){
				List<Map<String, Object>> dbWaitingForPreauthTask = getDBTask(claim.getIntimation() , SHAConstants.WAITING_FOR_PREATH_QUEUE);
				List<Map<String, Object>> dbWaitingQueryReplyTask = getDBTask(claim.getIntimation() , SHAConstants.QUERY_REPLY_QUEUE);
				List<Map<String, Object>> dbPreauthTask = getDBTask(claim.getIntimation() , SHAConstants.PP_CURRENT_QUEUE);
				if(dbWaitingForPreauthTask != null && ! dbWaitingForPreauthTask.isEmpty()){
					
					for (Map<String, Object> outPutArray : dbWaitingForPreauthTask) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						workFlowMap.put(keyValue,outPutArray);
					
					}
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth == null){
						resultList.add(claim);
					}
				}else if(dbWaitingQueryReplyTask!= null && ! dbWaitingQueryReplyTask.isEmpty()){
					
					for (Map<String, Object> outPutArray : dbWaitingQueryReplyTask) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						workFlowMap.put(keyValue,outPutArray);
					
					}
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth == null){
						resultList.add(claim);
					}
				}else{
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth != null){
						Long valueForConversion = ReferenceTable.getConversionStatusMap().get(preauth.getStatus().getKey());
						if(valueForConversion != null){
							if(dbPreauthTask == null || dbPreauthTask.isEmpty()){
								resultList.add(claim);
							}
						}else if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS)){
							Boolean alreadyPreauthDeniedOrRejectForReopen = isAlreadyPreauthDeniedOrRejectForReopen(preauth.getKey());
							if(alreadyPreauthDeniedOrRejectForReopen){
								if(dbPreauthTask == null || dbPreauthTask.isEmpty()){
								resultList.add(claim);
								}
							}
						}
					}
				}
			}
		}
		
		if(null != resultList && 0!= resultList.size())
		{
			
//			resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
			List<Claim> pageItemList = resultList;
	
			searchConvertClaimTableDTO = SearchConvertClaimMapper.getInstance()
					.getSearchConvertClaimTableDTO(pageItemList);
			
			for (SearchConvertClaimTableDto tableDTO : searchConvertClaimTableDTO) {
				Object dbOutArray = workFlowMap.get(tableDTO.getKey());
				tableDTO.setDbOutArray(dbOutArray);
			}
			
			//Assigning LOB value to Table DTO.
		
			
			ListIterator<Claim> iterPED = pageItemList.listIterator();
			List<Long>  hospTypeList = new ArrayList<Long>();
			
			while (iterPED.hasNext())
			{
				 Claim claim = iterPED.next();
				 /*MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
				 Long hospitalTypeId = hospTypeInfo.getKey();*/
				 Long hospitalTypeId = claim.getIntimation().getHospital();
				 hospTypeList.add(hospitalTypeId);
				 //hospTypeList.add(hospitalTypeId);
			}
			List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
			List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();				
			resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
			searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
					.getHospitalInfoList(resultListForHospitalInfo);
			
			for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO )
			{
				
				//MastersValue objForLOB = masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
				if(null != objSearchConvClaimTableDTO.getLob() && !("").equals(objSearchConvClaimTableDTO.getLob()))
				{
					objSearchConvClaimTableDTO.setLob(loadLobValue(Long.parseLong(objSearchConvClaimTableDTO.getLob())));
				}
		 		
				//Human task assigned to table dto
				
				objSearchConvClaimTableDTO.setDbOutArray(workFlowMap.get(objSearchConvClaimTableDTO.getKey()));
			
				for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList)
				{
					
					/**
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
					 * Hospital type. In Hospital.java , we store the key. 
					 * 
					 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
					 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
					 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
					 * */
					if(objSearchConvClaimTableDTO.getHospitalTypeId() != null && objSearchConvClaimTableDTOForHospitalInfo.getKey() != null && objSearchConvClaimTableDTO.getHospitalTypeId().equals(objSearchConvClaimTableDTOForHospitalInfo.getKey()))
					{
						objSearchConvClaimTableDTO.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo.getHospitalName());
						break;
					}
				}
				
			}
		}
		
		Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
		Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
		if(searchConvertClaimTableDTO != null && ! searchConvertClaimTableDTO.isEmpty() && searchConvertClaimTableDTO.size() >= 10){
			formDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
		}
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(searchConvertClaimTableDTO);
		page.setIsDbSearch(true);
		return page;
		
	}
	

/*	public Boolean getWaitingForPreauthTask(Claim objClaim)
=======
	
public Page<SearchConvertClaimTableDto> searchPAConvertToReimbursement(SearchConvertClaimFormDto formDTO, String userName, String passWord){
		
		String intimationNo = !formDTO.getIntimationNumber().isEmpty() ? formDTO.getIntimationNumber() :null;
		String policyNo = !formDTO.getPolicyNumber().isEmpty() ? formDTO.getPolicyNumber() : null;
		Long cpuCode = formDTO.getCpuCode() != null ?  formDTO.getCpuCode().getId() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		List<Claim> listOfClaim = new ArrayList<Claim>();
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(cpuCode != null){
		//Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<String>get("cpuCode"), "%"+cpuCode+"%");
		Predicate condition3 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode"), cpuCode);
		conditionList.add(condition3);
		}
		
		//For PA
		Predicate pacondition = criteriaBuilder.equal(root.<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
		conditionList.add(pacondition);

		
		List<Long> claimTypeKey = new ArrayList<Long>();
		      claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				Expression<Long> exp = root.<MastersValue> get("claimType").<Long> get("key");
				
		Predicate condition4 = exp.in(claimTypeKey);
		conditionList.add(condition4);
				
		if(intimationNo == null && policyNo == null && cpuCode == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null && cpuCode == null /*&& listOfClaim.size()>10){
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listOfClaim = typedQuery.getResultList();
		}
		
		List<Long> keys = new ArrayList<Long>();
		
		List<Claim> resultList = new ArrayList<Claim>();
		
		List<SearchConvertClaimTableDto> searchConvertClaimTableDTO  = new ArrayList<SearchConvertClaimTableDto>();
		
		if(listOfClaim != null && ! listOfClaim.isEmpty()){
			for (Claim claim : listOfClaim) {
				if(getPAWaitingForPreauthTask(claim)){
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth == null){
						resultList.add(claim);
					}
				}else{
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
						if(preauth != null){
						Long valueForConversion = ReferenceTable.getConversionStatusMap().get(preauth.getStatus().getKey());
						if(valueForConversion != null){
							resultList.add(claim);
						}
					}
				}
			}
		}
		
		if(null != resultList && 0!= resultList.size())
		{
			
//			resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
			List<Claim> pageItemList = resultList;
	
			searchConvertClaimTableDTO = SearchConvertClaimMapper.getInstance()
					.getSearchConvertClaimTableDTO(pageItemList);
			
			//Assigning LOB value to Table DTO.
		
			
			ListIterator<Claim> iterPED = pageItemList.listIterator();
			List<Long>  hospTypeList = new ArrayList<Long>();
			
			while (iterPED.hasNext())
			{
				 Claim claim = iterPED.next();
				 MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
				 Long hospitalTypeId = hospTypeInfo.getKey();
				 Long hospitalTypeId = claim.getIntimation().getHospital();
				 hospTypeList.add(hospitalTypeId);
				 //hospTypeList.add(hospitalTypeId);
			}
			List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
			List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();				
			resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
			searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
					.getHospitalInfoList(resultListForHospitalInfo);
			
			for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO )
			{
				
				//MastersValue objForLOB = masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
				if(null != objSearchConvClaimTableDTO.getLob() && !("").equals(objSearchConvClaimTableDTO.getLob()))
				{
					objSearchConvClaimTableDTO.setLob(loadLobValue(Long.parseLong(objSearchConvClaimTableDTO.getLob())));
				}
				//Human task assigned to table dto
			
				for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList)
				{
					
					*//**
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
					 * Hospital type. In Hospital.java , we store the key. 
					 * 
					 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
					 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
					 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
					 * *//*
					if(objSearchConvClaimTableDTO.getHospitalTypeId() != null && objSearchConvClaimTableDTOForHospitalInfo.getKey() != null && objSearchConvClaimTableDTO.getHospitalTypeId().equals(objSearchConvClaimTableDTOForHospitalInfo.getKey()))
					{
						objSearchConvClaimTableDTO.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo.getHospitalName());
						break;
					}
				}
				
			}
		}
		
		Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
		Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
		if(searchConvertClaimTableDTO != null && ! searchConvertClaimTableDTO.isEmpty() && searchConvertClaimTableDTO.size() >= 10){
			formDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
		}
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(searchConvertClaimTableDTO);
		page.setIsDbSearch(true);
		return page;
		
	}*/
	
	/*public Boolean getWaitingForPreauthTask(Claim objClaim)
	{
		Boolean isWaitingForPreauth = false;
		ReceivePreAuthTask receivePreauthTask = BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation().getIntimationId());
		payloadBO.setIntimation(objIntimationType);
	//	com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if(null != humanTaskList && !humanTaskList.isEmpty())
		{
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj.getClaim();
				//String claimId = claimType.getClaimId();
				Long claimKey = claimType.getKey();
				if(objClaim.getKey().equals(claimKey))
				{
					isWaitingForPreauth = true;
					break;
				}
			}
		}
		return isWaitingForPreauth;
	}*/

	
	
	/*public Boolean getPAWaitingForPreauthTask(Claim objClaim)
	{
		Boolean isWaitingForPreauth = false;
		ReceivePreAuthTask receivePreauthTask = BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation().getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		
		ProductInfoType productInfo = payloadBO.getProductInfo();
    	if(productInfo == null) {
    		productInfo = new ProductInfoType();
    	}
		productInfo.setLob(SHAConstants.PA_LOB);
		payloadBO.setProductInfo(productInfo);
		
		
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if(null != humanTaskList && !humanTaskList.isEmpty())
		{
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj.getClaim();
				//String claimId = claimType.getClaimId();
				Long claimKey = claimType.getKey();
				if(objClaim.getKey().equals(claimKey))
				{
					isWaitingForPreauth = true;
					break;
				}
			}
		}
		return isWaitingForPreauth;
	}*/
	

	public Preauth getLatestPreauthByClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}
			return preauth;
		}
		
		return null;
		
		
	}
	
	public List<Claim> getCashlessClaim(Long claimType){
		
		Query query = entityManager.createNamedQuery("Claim.findCashlessClaim");
		query.setParameter("claimType", claimType);
		
		List<Claim> listOfClaim = (List<Claim>) query.getResultList();
		for (Claim claim : listOfClaim) {
			entityManager.refresh(claim);
		}
		
		return listOfClaim;
	}
	
	public Page<SearchConvertClaimTableDto> searchPA(SearchConvertClaimFormDto formDTO, String userName, String passWord) {
		try {
			String strIntimationNo = "";// formDTO.getIntimationNumber();
			String strPolicyNo = "";// formDTO.getPolicyNumber();
			SelectValue cpuCode = formDTO.getCpuCode();
			String strCpuCode = "";

			String priority = formDTO.getPriority() != null ? formDTO
					.getPriority().getValue() != null ? formDTO.getPriority()
					.getValue() : null : null;
			
			String source = formDTO.getSource() != null ? formDTO.getSource()
					.getValue() != null ? formDTO.getSource().getValue() : null
					: null;
			String type = formDTO.getType() != null ? formDTO.getType()
					.getValue() != null ? formDTO.getType().getValue() : null
					: null;

			List<Map<String, Object>> taskProcedure = null;

			Map<String, Object> mapValues = new WeakHashMap<String, Object>();

			Integer totalRecords = 0;

			mapValues.put(SHAConstants.CURRENT_Q,
					SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE);

			mapValues.put(SHAConstants.USER_ID, userName);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		//	mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);

//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayloadBO = null;
//
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType reimbursmentIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
//
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType reimbursementPolicyType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
//
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType reimbursementClaimRequestType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();

			if (null != formDTO.getIntimationNumber()
					&& !formDTO.getIntimationNumber().equals("")) {

//				if (reimbursementPayloadBO == null) {
//					reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				}

				strIntimationNo = formDTO.getIntimationNumber();

				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);

//				reimbursmentIntimationType.setIntimationNumber(strIntimationNo);
//				reimbursementPayloadBO
//						.setIntimation(reimbursmentIntimationType);
			}

			if (null != formDTO.getPolicyNumber()
					&& !formDTO.getPolicyNumber().equals("")) {

//				if (reimbursementPayloadBO == null) {
//					reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				}

				strPolicyNo = formDTO.getPolicyNumber();

				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);

//				reimbursementPolicyType.setPolicyId(strPolicyNo);
//				reimbursementPayloadBO.setPolicy(reimbursementPolicyType);

			}
			if (null != cpuCode) {

//				if (reimbursementPayloadBO == null) {
//					reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				}

				strCpuCode = String.valueOf(cpuCode.getId());

				mapValues.put(SHAConstants.CPU_CODE, strCpuCode);

//				reimbursementClaimRequestType.setCpuCode(strCpuCode);
//				reimbursementPayloadBO
//						.setClaimRequest(reimbursementClaimRequestType);
			}

//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = null;

			if (priority != null && !priority.isEmpty() || source != null
					&& !source.isEmpty() || type != null && !type.isEmpty()) {

//				reimbursementClassification = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();

				if (priority != null && !priority.isEmpty())
					if (!priority.equalsIgnoreCase(SHAConstants.ALL)) {
						mapValues.put(SHAConstants.PRIORITY, priority);
					}
//				reimbursementClassification.setPriority(priority);
	
				if (source != null && !source.isEmpty()) {
					mapValues.put(SHAConstants.STAGE_SOURCE, source);
				}

				if (type != null && !type.isEmpty()) {
					if (!type.equalsIgnoreCase(SHAConstants.ALL)) {
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}
				
//					reimbursementClassification.setType(type);
				}

//				if (reimbursementPayloadBO == null) {
//					reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				}
//
//				reimbursementPayloadBO
//						.setClassification(reimbursementClassification);

			}

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer
					.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);

			pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer
					.valueOf(BPMClientContext.PAGE_SIZE) : 10);
			pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer
					.valueOf(BPMClientContext.PAGE_NUMBER) : 1);

			List<SearchConvertClaimTableDto> searchConvertClaimTableDTO = new ArrayList<SearchConvertClaimTableDto>();

			Map<Long, Object> workFlowMap = new WeakHashMap<Long, Object>();

//			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//			DBCalculationService dbCalculationService = new DBCalculationService();
//			taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

			DBCalculationService dbCalculationService = new DBCalculationService();			
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);			

			pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer
					.valueOf(BPMClientContext.PAGE_SIZE) : 10);
			pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer
					.valueOf(BPMClientContext.PAGE_NUMBER) : 1);

//			AckProcessConvertClaimToReimbTask convertClaimTaskFromAck = BPMClientContext
//					.getConvertClaimTaskFromAck(userName, passWord);
//			PagedTaskList reimbursementTask = convertClaimTaskFromAck.getTasks(
//					userName, pageable, reimbursementPayloadBO);
			// Map to set human task to table DTO.

			List<Long> keys = new ArrayList<Long>();

//			Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//			Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();

			if (null != taskProcedure) {

				for (Map<String, Object> outPutArray : taskProcedure) {
					Long keyValue = (Long) outPutArray
							.get(SHAConstants.DB_CLAIM_KEY);
					workFlowMap.put(keyValue, outPutArray);

					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);

					keys.add(keyValue);
				}
			}

//			if (null != reimbursementTask) {
//				List<HumanTask> humanTask1 = reimbursementTask.getHumanTasks();
//
//				for (HumanTask item : humanTask1) {
//
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = item
//							.getPayload();
//					ClaimType claimKey = payload.getClaim();
//					;
//					if (null != claimKey) {
//						Long keyValue = claimKey.getKey();
//						keys.add(keyValue);
//						humanTaskMap.put(keyValue, item);
//						taskNumberMap.put(keyValue, item.getNumber());
//					}
//				}
//
//			}

			if (null != keys && 0 != keys.size()) {
				List<Claim> resultList = new ArrayList<Claim>();
				resultList = SHAUtils.getIntimationInformation(
						SHAConstants.SEARCH_CLAIM, entityManager, keys);
				List<Claim> pageItemList = resultList;
				searchConvertClaimTableDTO = SearchConvertClaimMapper
						.getInstance().getSearchConvertClaimTableDTO(
								pageItemList);

				for (SearchConvertClaimTableDto tableDto : searchConvertClaimTableDTO) {
					if (tableDto.getKey() != null) {
						Object workflowKey = workFlowMap.get(tableDto.getKey());
						tableDto.setDbOutArray(workflowKey);
					}

				}

				ListIterator<Claim> iterPED = pageItemList.listIterator();
				List<Long> hospTypeList = new ArrayList<Long>();

				while (iterPED.hasNext()) {
					Claim claim = iterPED.next();
					/*
					 * MastersValue hospTypeInfo =
					 * preAuth.getIntimation().getHospitalType(); Long
					 * hospitalTypeId = hospTypeInfo.getKey();
					 */
					Long hospitalTypeId = claim.getIntimation().getHospital();
					hospTypeList.add(hospitalTypeId);
					// hospTypeList.add(hospitalTypeId);
				}
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(
						entityManager, hospTypeList);
				searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
						.getHospitalInfoList(resultListForHospitalInfo);

				for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO) {

					// MastersValue objForLOB =
					// masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
					if (null != objSearchConvClaimTableDTO.getLob()
							&& !("").equals(objSearchConvClaimTableDTO.getLob())) {
						objSearchConvClaimTableDTO
								.setLob(loadLobValue(Long
										.parseLong(objSearchConvClaimTableDTO
												.getLob())));
					}
					// Human task assigned to table dto
//					objSearchConvClaimTableDTO.setHumanTask(humanTaskMap
//							.get(objSearchConvClaimTableDTO.getKey()));
//					objSearchConvClaimTableDTO.setTaskNumber(taskNumberMap
//							.get(objSearchConvClaimTableDTO.getKey()));

					for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList) {

						/**
						 * objSearchPEDRequestProcessTableDTOForHospitalInfo.
						 * getKey() --> will store the hosptial type id.
						 * objSearchPEDRequestProcessTableDTOForHospitalInfo
						 * list belongs to VW_HOSPITAL view. This list is of
						 * Hospital type. In Hospital.java , we store the key.
						 * 
						 * But this key will come from intimation table hospital
						 * type id. objSearchPEDRequestProcessTableDTO is of
						 * SearchPEDRequestProcessTableDTO , in which we store
						 * hospital type in a variable known as hospitalTypeId .
						 * That is why we equate hospitalTypeId from
						 * SearchPEDRequestProcessTableDTO with key from
						 * HospitalDTO.
						 * */
						if (objSearchConvClaimTableDTO.getHospitalTypeId() != null
								&& objSearchConvClaimTableDTOForHospitalInfo
										.getKey() != null
								&& objSearchConvClaimTableDTO
										.getHospitalTypeId().equals(
												objSearchConvClaimTableDTOForHospitalInfo
														.getKey())) {
							objSearchConvClaimTableDTO
									.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo
											.getHospitalName());
							break;
						}
					}

				}
			}
			Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
			// page.setPageNumber(pagedList.getPageNumber());
			page.setPageItems(searchConvertClaimTableDTO);
			page.setTotalRecords(totalRecords);

			// page.setPagesAvailable(pageCount);
			// page.setPagesAvailable(pagedList.getPagesAvailable());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public List<Map<String, Object>> getDBTask(Intimation intimation,String currentQ){
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, currentQ);
			
//			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//			
//			DBCalculationService db = new DBCalculationService();
//			 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService db = new DBCalculationService();
			 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
			
			
			if (taskProcedure != null){
				return taskProcedure;
			} 
			return null;
		}
	
public Page<SearchConvertClaimTableDto> searchPAConvertToReimbursement(SearchConvertClaimFormDto formDTO, String userName, String passWord){
		
		String intimationNo = !formDTO.getIntimationNumber().isEmpty() ? formDTO.getIntimationNumber() :null;
		String policyNo = !formDTO.getPolicyNumber().isEmpty() ? formDTO.getPolicyNumber() : null;
		Long cpuCode = formDTO.getCpuCode() != null ?  formDTO.getCpuCode().getId() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		List<Claim> listOfClaim = new ArrayList<Claim>();
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(cpuCode != null){
		//Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<String>get("cpuCode"), "%"+cpuCode+"%");
		Predicate condition3 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode"), cpuCode);
		conditionList.add(condition3);
		}
		
		//For PA
		//Predicate pacondition = criteriaBuilder.equal(root.<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
		Predicate condition8 = criteriaBuilder.equal(root.<String>get("processClaimType"), SHAConstants.PA_TYPE);
		
		Predicate condition9 = criteriaBuilder.equal(root.<Intimation>get("intimation").<Policy>get("policy").<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
		Predicate condition10 = criteriaBuilder.equal(root.<String>get("processClaimType"), SHAConstants.HEALTH_TYPE);
		//added for CR CPU Routing Issue,since claim level Process type H and intimation level P, only for package products 11-04-2019
		Predicate condition13 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("processClaimType"), SHAConstants.PA_TYPE);
		Predicate condition14 = criteriaBuilder.equal(root.<String>get("processClaimType"), SHAConstants.HEALTH_TYPE);
		//added for CR CPU Routing Issue 11-04-2019
		
	    Predicate condition11 = criteriaBuilder.and(condition9,condition10);
	    Predicate condition15 = criteriaBuilder.and(condition13,condition14);
	    Predicate condition12 = criteriaBuilder.or(condition11,condition8,condition15);
		conditionList.add(condition12);	

		
		List<Long> claimTypeKey = new ArrayList<Long>();
		      claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				Expression<Long> exp = root.<MastersValue> get("claimType").<Long> get("key");
				
		Predicate condition4 = exp.in(claimTypeKey);
		conditionList.add(condition4);
				
		if(intimationNo == null && policyNo == null && cpuCode == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null && cpuCode == null /*&& listOfClaim.size()>10*/){
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listOfClaim = typedQuery.getResultList();
		}
		
		List<Long> keys = new ArrayList<Long>();
		
		List<Claim> resultList = new ArrayList<Claim>();
		
		List<SearchConvertClaimTableDto> searchConvertClaimTableDTO  = new ArrayList<SearchConvertClaimTableDto>();
		
		if(listOfClaim != null && ! listOfClaim.isEmpty()){
			for (Claim claim : listOfClaim) {
				List<Map<String, Object>> dbWaitingForPreauthTask = getDBTask(claim.getIntimation() , SHAConstants.WAITING_FOR_PREATH_QUEUE);
				List<Map<String, Object>> dbWaitingQueryReplyTask = getDBTask(claim.getIntimation() , SHAConstants.QUERY_REPLY_QUEUE);
				List<Map<String, Object>> dbPreauthTask = getDBTask(claim.getIntimation() , SHAConstants.PP_CURRENT_QUEUE);
				if(dbWaitingForPreauthTask != null && ! dbWaitingForPreauthTask.isEmpty()){
					
					for (Map<String, Object> outPutArray : dbWaitingForPreauthTask) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						workFlowMap.put(keyValue,outPutArray);
					
					}
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth == null){
						resultList.add(claim);
					}
				}else{
					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
					if(preauth != null){
						Long valueForConversion = ReferenceTable.getConversionStatusMap().get(preauth.getStatus().getKey());
						if(valueForConversion != null){
							if(dbPreauthTask == null || dbPreauthTask.isEmpty()){
								resultList.add(claim);
							}
						}else if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS)){
							Boolean alreadyPreauthDeniedOrRejectForReopen = isAlreadyPreauthDeniedOrRejectForReopen(preauth.getKey());
							if(alreadyPreauthDeniedOrRejectForReopen){
								if(dbPreauthTask == null || dbPreauthTask.isEmpty()){
								resultList.add(claim);
								}
							}
						}
					}
				}
			}
		}
		
		if(null != resultList && 0!= resultList.size())
		{
			
//			resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
			List<Claim> pageItemList = resultList;
	
			searchConvertClaimTableDTO = SearchConvertClaimMapper.getInstance()
					.getSearchConvertClaimTableDTO(pageItemList);
			
			//Assigning LOB value to Table DTO.
		
			for (SearchConvertClaimTableDto tableDTO : searchConvertClaimTableDTO) {
				Object dbOutArray = workFlowMap.get(tableDTO.getKey());
				tableDTO.setDbOutArray(dbOutArray);
			}

			
			ListIterator<Claim> iterPED = pageItemList.listIterator();
			List<Long>  hospTypeList = new ArrayList<Long>();
			
			while (iterPED.hasNext())
			{
				 Claim claim = iterPED.next();
				 /*MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
				 Long hospitalTypeId = hospTypeInfo.getKey();*/
				 Long hospitalTypeId = claim.getIntimation().getHospital();
				 hospTypeList.add(hospitalTypeId);
				 //hospTypeList.add(hospitalTypeId);
			}
			List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
			List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();				
			resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
			searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
					.getHospitalInfoList(resultListForHospitalInfo);
			
			for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO )
			{
				
				//MastersValue objForLOB = masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
				if(null != objSearchConvClaimTableDTO.getLob() && !("").equals(objSearchConvClaimTableDTO.getLob()))
				{
					objSearchConvClaimTableDTO.setLob(loadLobValue(Long.parseLong(objSearchConvClaimTableDTO.getLob())));
				}
				//Human task assigned to table dto
			
				for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList)
				{
					
					/**
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
					 * Hospital type. In Hospital.java , we store the key. 
					 * 
					 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
					 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
					 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
					 * */
					if(objSearchConvClaimTableDTO.getHospitalTypeId() != null && objSearchConvClaimTableDTOForHospitalInfo.getKey() != null && objSearchConvClaimTableDTO.getHospitalTypeId().equals(objSearchConvClaimTableDTOForHospitalInfo.getKey()))
					{
						objSearchConvClaimTableDTO.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo.getHospitalName());
						break;
					}
				}
				
			}
		}
		
		Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
		Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
		if(searchConvertClaimTableDTO != null && ! searchConvertClaimTableDTO.isEmpty() && searchConvertClaimTableDTO.size() >= 10){
			formDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
		}
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(searchConvertClaimTableDTO);
		page.setIsDbSearch(true);
		return page;
		
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


public Page<SearchConvertClaimTableDto> search(SearchConvertClaimFormDto formDTO, String userName, String passWord) {
	try {
		String strIntimationNo = "";// formDTO.getIntimationNumber();
		String strPolicyNo = "";// formDTO.getPolicyNumber();
		SelectValue cpuCode = formDTO.getCpuCode();
		String strCpuCode = "";

		String priority = formDTO.getPriority() != null ? formDTO
				.getPriority().getValue() != null ? formDTO.getPriority()
				.getValue() : null : null;
		String source = formDTO.getSource() != null ? formDTO.getSource()
				.getValue() != null ? formDTO.getSource().getValue() : null
				: null;
		String type = formDTO.getType() != null ? formDTO.getType()
				.getValue() != null ? formDTO.getType().getValue() : null
				: null;
		
		String priorityNew = formDTO.getPriorityNew() != null ? formDTO.getPriorityNew().getValue() != null ? formDTO.getPriorityNew().getValue() : null : null;

		List<Map<String, Object>> taskProcedure = null;

		Map<String, Object> mapValues = new WeakHashMap<String, Object>();

		Integer totalRecords = 0;

		mapValues.put(SHAConstants.CURRENT_Q,
				SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE);

		mapValues.put(SHAConstants.USER_ID, userName);
		
		mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
		mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);

//		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayloadBO = null;
//
//		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType reimbursmentIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
//
//		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType reimbursementPolicyType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
//
//		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType reimbursementClaimRequestType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();

		if (null != formDTO.getIntimationNumber()
				&& !formDTO.getIntimationNumber().equals("")) {

//			if (reimbursementPayloadBO == null) {
//				reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//			}

			strIntimationNo = formDTO.getIntimationNumber();

			mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);

//			reimbursmentIntimationType.setIntimationNumber(strIntimationNo);
//			reimbursementPayloadBO
//					.setIntimation(reimbursmentIntimationType);
		}

		if (null != formDTO.getPolicyNumber()
				&& !formDTO.getPolicyNumber().equals("")) {

//			if (reimbursementPayloadBO == null) {
//				reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//			}

			strPolicyNo = formDTO.getPolicyNumber();

			mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);

//			reimbursementPolicyType.setPolicyId(strPolicyNo);
//			reimbursementPayloadBO.setPolicy(reimbursementPolicyType);

		}
		if (null != cpuCode) {

//			if (reimbursementPayloadBO == null) {
//				reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//			}

			strCpuCode = String.valueOf(cpuCode.getId());

			mapValues.put(SHAConstants.CPU_CODE, strCpuCode);

//			reimbursementClaimRequestType.setCpuCode(strCpuCode);
//			reimbursementPayloadBO
//					.setClaimRequest(reimbursementClaimRequestType);
		}

//		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = null;

		if (priority != null && !priority.isEmpty() || source != null
				&& !source.isEmpty() || type != null && !type.isEmpty()) {

//			reimbursementClassification = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();

			if (priority != null && !priority.isEmpty())
				if (!priority.equalsIgnoreCase(SHAConstants.ALL)) {
					mapValues.put(SHAConstants.PRIORITY, priority);
				}
//			reimbursementClassification.setPriority(priority);

			if (source != null && !source.isEmpty()) {
				mapValues.put(SHAConstants.STAGE_SOURCE, source);
			}

			if (type != null && !type.isEmpty()) {
				if (!type.equalsIgnoreCase(SHAConstants.ALL)) {
					mapValues.put(SHAConstants.RECORD_TYPE, type);
				}
			
//				reimbursementClassification.setType(type);
			}

//			if (reimbursementPayloadBO == null) {
//				reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//			}
//
//			reimbursementPayloadBO
//					.setClassification(reimbursementClassification);

		}
		
		if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
			if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
				mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
			}else{
				mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
			}

		Pageable pageable = formDTO.getPageable();
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer
				.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);

		pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer
				.valueOf(BPMClientContext.PAGE_SIZE) : 10);
		pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer
				.valueOf(BPMClientContext.PAGE_NUMBER) : 1);

		List<SearchConvertClaimTableDto> searchConvertClaimTableDTO = new ArrayList<SearchConvertClaimTableDto>();

		Map<Long, Object> workFlowMap = new WeakHashMap<Long, Object>();

//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//		DBCalculationService dbCalculationService = new DBCalculationService();
//		taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

		DBCalculationService dbCalculationService = new DBCalculationService();			
		taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);			

		pageable.setPageSize(BPMClientContext.PAGE_SIZE != null ? Integer
				.valueOf(BPMClientContext.PAGE_SIZE) : 10);
		pageable.setPageNumber(BPMClientContext.PAGE_NUMBER != null ? Integer
				.valueOf(BPMClientContext.PAGE_NUMBER) : 1);

//		AckProcessConvertClaimToReimbTask convertClaimTaskFromAck = BPMClientContext
//				.getConvertClaimTaskFromAck(userName, passWord);
//		PagedTaskList reimbursementTask = convertClaimTaskFromAck.getTasks(
//				userName, pageable, reimbursementPayloadBO);
		// Map to set human task to table DTO.

		List<Long> keys = new ArrayList<Long>();

//		Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//		Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();

		if (null != taskProcedure) {

			for (Map<String, Object> outPutArray : taskProcedure) {
				Long keyValue = (Long) outPutArray
						.get(SHAConstants.DB_CLAIM_KEY);
				workFlowMap.put(keyValue, outPutArray);

				totalRecords = (Integer) outPutArray
						.get(SHAConstants.TOTAL_RECORDS);

				keys.add(keyValue);
			}
		}

//		if (null != reimbursementTask) {
//			List<HumanTask> humanTask1 = reimbursementTask.getHumanTasks();
//
//			for (HumanTask item : humanTask1) {
//
//				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = item
//						.getPayload();
//				ClaimType claimKey = payload.getClaim();
//				;
//				if (null != claimKey) {
//					Long keyValue = claimKey.getKey();
//					keys.add(keyValue);
//					humanTaskMap.put(keyValue, item);
//					taskNumberMap.put(keyValue, item.getNumber());
//				}
//			}
//
//		}

		if (null != keys && 0 != keys.size()) {
			List<Claim> resultList = new ArrayList<Claim>();
			resultList = SHAUtils.getIntimationInformation(
					SHAConstants.SEARCH_CLAIM, entityManager, keys);
			List<Claim> pageItemList = resultList;
			searchConvertClaimTableDTO = SearchConvertClaimMapper
					.getInstance().getSearchConvertClaimTableDTO(
							pageItemList);

			for (SearchConvertClaimTableDto tableDto : searchConvertClaimTableDTO) {
				if (tableDto.getKey() != null) {
					Object workflowKey = workFlowMap.get(tableDto.getKey());
					tableDto.setDbOutArray(workflowKey);
				}

			}

			ListIterator<Claim> iterPED = pageItemList.listIterator();
			List<Long> hospTypeList = new ArrayList<Long>();

			while (iterPED.hasNext()) {
				Claim claim = iterPED.next();
				/*
				 * MastersValue hospTypeInfo =
				 * preAuth.getIntimation().getHospitalType(); Long
				 * hospitalTypeId = hospTypeInfo.getKey();
				 */
				Long hospitalTypeId = claim.getIntimation().getHospital();
				hospTypeList.add(hospitalTypeId);
				// hospTypeList.add(hospitalTypeId);
			}
			List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
			List<SearchConvertClaimTableDto> searchConvClaimTableDTOForHospitalInfoList = new ArrayList<SearchConvertClaimTableDto>();
			resultListForHospitalInfo = SHAUtils.getHospitalInformation(
					entityManager, hospTypeList);
			searchConvClaimTableDTOForHospitalInfoList = SearchConvertClaimMapper
					.getHospitalInfoList(resultListForHospitalInfo);

			for (SearchConvertClaimTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO) {

				// MastersValue objForLOB =
				// masterService.getMaster(Long.parseLong(objSearchConvClaimTableDTO.getLob()));
				if (null != objSearchConvClaimTableDTO.getLob()
						&& !("").equals(objSearchConvClaimTableDTO.getLob())) {
					objSearchConvClaimTableDTO
							.setLob(loadLobValue(Long
									.parseLong(objSearchConvClaimTableDTO
											.getLob())));
				}
				Claim claim = getClaimByIntimation(objSearchConvClaimTableDTO.getIntimationKey());
				if(claim != null && claim.getCrcFlag()!= null && claim.getCrcFlag().equals("Y")) {
					objSearchConvClaimTableDTO.setColorCodeCell("OLIVE");
				}
				// Human task assigned to table dto
//				objSearchConvClaimTableDTO.setHumanTask(humanTaskMap
//						.get(objSearchConvClaimTableDTO.getKey()));
//				objSearchConvClaimTableDTO.setTaskNumber(taskNumberMap
//						.get(objSearchConvClaimTableDTO.getKey()));

				for (SearchConvertClaimTableDto objSearchConvClaimTableDTOForHospitalInfo : searchConvClaimTableDTOForHospitalInfoList) {

					/**
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo.
					 * getKey() --> will store the hosptial type id.
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo
					 * list belongs to VW_HOSPITAL view. This list is of
					 * Hospital type. In Hospital.java , we store the key.
					 * 
					 * But this key will come from intimation table hospital
					 * type id. objSearchPEDRequestProcessTableDTO is of
					 * SearchPEDRequestProcessTableDTO , in which we store
					 * hospital type in a variable known as hospitalTypeId .
					 * That is why we equate hospitalTypeId from
					 * SearchPEDRequestProcessTableDTO with key from
					 * HospitalDTO.
					 * */
					if (objSearchConvClaimTableDTO.getHospitalTypeId() != null
							&& objSearchConvClaimTableDTOForHospitalInfo
									.getKey() != null
							&& objSearchConvClaimTableDTO
									.getHospitalTypeId().equals(
											objSearchConvClaimTableDTOForHospitalInfo
													.getKey())) {
						objSearchConvClaimTableDTO
								.setHospitalName(objSearchConvClaimTableDTOForHospitalInfo
										.getHospitalName());
						break;
					}
				}

			}
		}
		Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
		Page<SearchConvertClaimTableDto> page = new Page<SearchConvertClaimTableDto>();
		// page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(searchConvertClaimTableDTO);
		page.setTotalRecords(totalRecords);

		// page.setPagesAvailable(pageCount);
		// page.setPagesAvailable(pagedList.getPagesAvailable());
		return page;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public Boolean isAlreadyPreauthDeniedOrRejectForReopen(Long cashlessKey){

	
	String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CASHLESS_KEY =" + cashlessKey
			+ " AND STATUS_ID NOT IN (22) AND STATUS_ID IN (23,26)";
	
	Query nativeQuery = entityManager.createNativeQuery(query);
	List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return true;
	}
	return false;
	 
}

}
