package com.shaic.claim.processtranslation.search;


/**
 * 
 */

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

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


/**
 * @author VijayaRagavender
 *
 */
@Stateless
public class SearchProcessTranslationService extends AbstractDAO<Preauth> {
	
	
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
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();

	public SearchProcessTranslationService() {
		super();
	}

	public Page<SearchProcessTranslationTableDTO> search(
			SearchProcessTranslationFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = "";
			String strPolicyNo = "";
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getValue() != null ? formDTO.getCpuCode().getValue(): null : null;
			

			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			workFlowMap= new WeakHashMap<Long, Object>();
			
			
			Integer totalRecords = 0; 
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.COORDINATOR_REPLY_CURRENT_QUEUE);
			
		/*	mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);*/
			
		/*	
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
			
			payloadBO.setIntimation(intimationType);*/
			
			if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equals(""))
			{
			strIntimationNo = formDTO.getIntimationNo();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);		
			}
		
			if(null != formDTO.getPolicyNo() && ! formDTO.getPolicyNo().equals(""))
			{
				strPolicyNo = formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);	
			}
			
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					if(priority != null && ! priority.isEmpty())
					{
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.PRIORITY, priority);
						}			
							
					}
					if(source != null && ! source.isEmpty()){
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
						//classification.setSource(source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
					}
			

			}
		    
		    if(cpuCode != null){
		    	cpuCode = cpuCode.substring(0, cpuCode.indexOf("-")).trim();
		    	mapValues.put(SHAConstants.CPU_CODE, cpuCode);
		    }

			//formDTO.getPolicyNo();
			
			/*TranslationQF transQF = null;
			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo))))
			{
				transQF = new TranslationQF();
				transQF.setIntimationNumber(strIntimationNo);
				transQF.setPolicyId(strPolicyNo);
			}*/
			
			Pageable pageable = formDTO.getPageable();
			
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);

			List<SearchProcessTranslationTableDTO> searchProcessTranslationTableDTO = new ArrayList<SearchProcessTranslationTableDTO>();
			List<SearchProcessTranslationTableDTO> finalList = new ArrayList<SearchProcessTranslationTableDTO>();
			

			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			List<Long> keys = new ArrayList<Long>();  
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> rodKeyList = new ArrayList<Long>();
			Map<String,Long> cpuCodeHolder = new HashMap<String,Long>();
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {
							String currentQueue = (String) outPutArray.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
							String intimationNo = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
							String _cpuCode = (String) outPutArray.get(SHAConstants.CPU_CODE);
							intimationNoList.add(intimationNo);
							if(!StringUtils.isBlank(_cpuCode)){
								cpuCodeHolder.put(intimationNo, Long.parseLong(_cpuCode));
							}
							if(SHAConstants.MA_CURRENT_QUEUE.equalsIgnoreCase(currentQueue) || SHAConstants.BILLING_CURRENT_QUEUE.equalsIgnoreCase(currentQueue) ||SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(currentQueue))
							{
								Long rodKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
								rodKeyList.add(rodKey);
								keys.add(0l);
								workFlowMap.put(rodKey,outPutArray);
							}
							else
							{
								Long cashlessKey  = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
								keys.add(cashlessKey);
								rodKeyList.add(0l);
								workFlowMap.put(cashlessKey,outPutArray);
							}
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
					}
				}
		    if(null != keys && 0!= keys.size())
		    {
				List<Preauth> resultList = new ArrayList<Preauth>();
				resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
				List<Preauth> pageItemList = resultList;
					searchProcessTranslationTableDTO = SearchProcessTranslationMapper.getInstance()
						.getSearchProcessTranslationTableDTO(pageItemList);
					for (int index = 0; index < searchProcessTranslationTableDTO.size(); index++) {
						
						SearchProcessTranslationTableDTO objSearchProcessTranslationTblDTO = searchProcessTranslationTableDTO.get(index);
						if(null != objSearchProcessTranslationTblDTO.getLob() && !("").equals(objSearchProcessTranslationTblDTO.getLob()))
						{
							objSearchProcessTranslationTblDTO.setLob(loadLobValue(Long.parseLong(objSearchProcessTranslationTblDTO.getLob())));
						}
						Object workflowKey = workFlowMap.get(objSearchProcessTranslationTblDTO.getKey());
						objSearchProcessTranslationTblDTO.setDbOutArray(workflowKey);
						finalList.add(objSearchProcessTranslationTblDTO);
					}
					/*
					for(SearchProcessTranslationTableDTO objSearchProcessTranslationTblDTO :searchProcessTranslationTableDTO)
					{
						
						if(null != objSearchProcessTranslationTblDTO.getLob() && !("").equals(objSearchProcessTranslationTblDTO.getLob()))
						{
							objSearchProcessTranslationTblDTO.setLob(loadLobValue(Long.parseLong(objSearchProcessTranslationTblDTO.getLob())));
						}
						
						
						
						finalList.add(objSearchProcessTranslationTblDTO);
					}*/
		    }
		    
		    
		    List<SearchProcessTranslationTableDTO> tableDTO = new ArrayList<SearchProcessTranslationTableDTO>();
		    String intimationNumber = "";
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNumber = intimationNoList.get(index);  
				 Long rod = rodKeyList.get(index);
				 tableDTO.addAll(getIntimationData(intimationNumber,rod));
				 
				 
			}
	
			finalList.addAll(tableDTO);
			
		    Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchProcessTranslationTableDTO> page = new Page<SearchProcessTranslationTableDTO>();
			page.setPageItems(finalList);
			page.setTotalRecords(totalRecords);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Preauth getProcessTranslationKey(Long processTranslationKey) {
		
		Query findByKey = entityManager.createNamedQuery("Coordinator.findAll");

		List<Preauth> processTranslationList = (List<Preauth>) findByKey
				.getResultList();

		if (!processTranslationList.isEmpty()) {
			return processTranslationList.get(0);

		}
		return null;
	}

	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
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
//--------------------------------------------------------------R3 by NarenJ--------------------------------------------------------
/*	public Page<SearchProcessTranslationTableDTO> searchR3(
			SearchProcessTranslationFormDTO searchFormDTO, String userName,
			String passWord) {

		try{
			String intimationNo =  searchFormDTO.getIntimationNo() ;
			String policyNo = searchFormDTO.getPolicyNo();
			
			com.shaic.ims.bpm.claim.modelv2.HumanTask humanTaskDTO = null;
			Long rodKey;
			String intimationNumber;
			List<String> intimationNoList = new ArrayList<String>();
			List<com.shaic.ims.bpm.claim.modelv2.HumanTask> humanTaskListDTO = new ArrayList<com.shaic.ims.bpm.claim.modelv2.HumanTask>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();
			
			PayloadBOType payloadBOType = new PayloadBOType();;
			IntimationType intimationType = new IntimationType();
			
			String priority = searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			
			
			
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBOType.setProductInfo(productInfo);
			
			payloadBOType.setIntimation(intimationType);
			
			
			if(!intimationNo.isEmpty()){
				
				intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);
				}
			
			
				PolicyType policyType = new PolicyType();
				
				if(!policyNo.isEmpty()){
					
					
					
					policyType.setPolicyId(policyNo);
					payloadBOType.setPolicy(policyType);
				}
				
				ClassificationType classification = null;
				
			    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
						classification = new ClassificationType();
						
						if(priority != null && ! priority.isEmpty())
							if(priority.equalsIgnoreCase(SHAConstants.ALL)){
								priority = null;
							}
						classification.setPriority(priority);
						if(source != null && ! source.isEmpty()){
							classification.setSource(source);
						}
						
						if(type != null && ! type.isEmpty()){
							if(type.equalsIgnoreCase(SHAConstants.ALL)){
								type = null;
							}
							classification.setType(type);
						}
						payloadBOType.setClassification(classification);
				}
				
				
			CoordinatorReplyTask specialistTask = BPMClientContext.getCoordinatorReplyTask(userName, passWord);
			
			Pageable pageable = searchFormDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			com.shaic.ims.bpm.claim.corev2.PagedTaskList taskList;
		
			taskList = specialistTask.getTasks(userName,pageable,payloadBOType);

			if(null != specialistTask){
				List<com.shaic.ims.bpm.claim.modelv2.HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(com.shaic.ims.bpm.claim.modelv2.HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(payloadBO.getRod() != null){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					humanTaskListDTO.add(humanTask);

					rodKeyList.add(payloadBO.getRod().getKey());
					taskNumber.add(humanTask.getNumber());
				}
					
				}
			}
			List<SearchProcessTranslationTableDTO> tableDTO = new ArrayList<SearchProcessTranslationTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNumber = intimationNoList.get(index); 
				
				 humanTaskDTO = humanTaskListDTO.get(index);
				 
				 Long rod = rodKeyList.get(index);
				 Integer taskNo = taskNumber.get(index);
				// tableDTO.addAll(getIntimationData(intimationNumber,  humanTaskDTO,rod,taskNo));
				 
			}
		
			Page<SearchProcessTranslationTableDTO> page = new Page<SearchProcessTranslationTableDTO>(taskList);
			page.setPageItems(tableDTO);
			page.setPageNumber(taskList.getCurrentPage());
			page.setTotalRecords(taskList.getTotalRecords());
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
		
			
	}*/
	private List<SearchProcessTranslationTableDTO> getIntimationData(String intimationNo,  
			Long rodKey){
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
	
		List<Claim> claimList = new ArrayList<Claim>(); 
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
	
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
			criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			claimList = typedQuery.getResultList();
		}
			
	
			for(Claim inti:claimList){
			System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			
			List<SearchProcessTranslationTableDTO> tableDTO = SearchProcessTranslationMapperR3.getInstance().getSearchProcessTranslationTableDTO(claimList);
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = setHumantaskAndLob(tableDTO,rodKey);
	
				return tableDTO;
		}catch(Exception e){
			return null;
		}
	}

	


	private List<SearchProcessTranslationTableDTO> setHumantaskAndLob(
			List<SearchProcessTranslationTableDTO> tableDTO, Long rodKey) {
		
		for(int index = 0; index < tableDTO.size(); index++){

				// tableDTO.get(index).setHumanTaskR3(humanTask); 
				 tableDTO.get(index).setLob(getLOBValue((tableDTO.get(index).getLOBID())).getValue());
				 tableDTO.get(index).setRodKey(rodKey);
					Object workflowKey = workFlowMap.get(tableDTO.get(index).getRodKey());
//					tableDto.setDbOutArray(workflowKey);
					tableDTO.get(index).setDbOutArray(workflowKey);
				 //tableDTO.get(index).setTaskNumber(taskNumber);
				
		}
		
		
		return tableDTO;
	}
	
	

	private MastersValue getLOBValue(Long getlOBId) {
		try{
		Query query = entityManager
				.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", getlOBId);
		MastersValue value = (MastersValue) query.getSingleResult();
	    return value;
		}catch(Exception e){
			
		}
		return null;
	}

}

