package com.shaic.reimbursement.investigationgrading;

import java.util.ArrayList;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationFormDTO;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationMapper;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;
@Stateless
public class SearchInvestigationGradingService extends AbstractDAO<Intimation>{


	@PersistenceContext
	protected EntityManager entityManager;
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchInvestigationGradingService() {
		super();
		
	}
	
	public  Page<SearchAssignInvestigationTableDTO> search(
			SearchAssignInvestigationFormDTO searchFormDTO,
			String userName, String passWord) {
		
		
		try{
			String intimationNo = null!= searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String 	policyNo =  null!= searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode =  null!= searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId().toString() : null;
			
			String priority = null!= searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null!= searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null!= searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType = null!= searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() != null ? searchFormDTO.getClaimType().getValue(): null : null;
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			
			Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
			Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
			Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
		
			Long investigationKey = null;
			Long rodKey = null;
			Long investigationAssignedKey = null;
		//	HumanTask humanTaskDTO = null;
			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();			
			workFlowMap= new WeakHashMap<Long, Object>();
			
			Integer totalRecords = 0; 
			
			List<String> intimationNoList = new ArrayList<String>();			
			List<Long> investgationKeyList = new ArrayList<Long>();
			List<Long> invAssignedKeyList = new ArrayList<Long>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();
			
		//	List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			List<String> roleList = new ArrayList<String>();
		//	PayloadBOType payloadBOType = null;
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.INVESTIGATION_GRADING_CURRENT_QUEUE);
			
			
			
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				
				}
			
				if(null != policyNo && !policyNo.isEmpty()){
					
					mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
					
				}		
				
			
				if(claimType != null && ! claimType.isEmpty()){
					
					mapValues.put(SHAConstants.CLAIM_TYPE, claimType);
				}
				
		
				if(null != cpuCode){
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);				
				}
				
				
				
			    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){					
						
						if(priority != null && ! priority.isEmpty())
						{
							mapValues.put(SHAConstants.PRIORITY, priority);
							}
						
						if(source != null && ! source.isEmpty()){
							mapValues.put(SHAConstants.STAGE_SOURCE, source);
						}
						
						if(type != null && ! type.isEmpty()){
							
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}						
						
						
				}
			    
			    /*if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
					if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
					}else{
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
					}*/
			    
			    if (crm != null && crm.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
			    }
			    
			    if (vip != null && vip.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PRIORITY, "VIP");
			    }
			    
			    Pageable pageable = searchFormDTO.getPageable();
				
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			    
			
			/*ProcessInvestigationTask processInvestigationTask = BPMClientContext.getProcessInvestigationInitTask(userName, passWord);
			
			PagedTaskList taskList;

			taskList = processInvestigationTask.getTasks(userName,pageable,payloadBOType);

			
			//PagedTaskList taskList = processInvestigationTask.getTasks(userName,pageable,payloadBOType);
	
			if(null != processInvestigationTask){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(null != payloadBO.getInvestigation()){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					roleList.add(payloadBO.getClaimRequest().getReimbReqBy());
					investgationKeyList.add(payloadBO.getInvestigation().getKey());
					rodKeyList.add(payloadBO.getRod().getKey());
					taskNumber.add(humanTask.getNumber());
					humanTaskListDTO.add(humanTask);
					}
				}
			}*/
				
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
				
					if (null != taskProcedure) {
						for (Map<String, Object> outPutArray : taskProcedure) {							
								Long investigKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
								String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
								Long reimbKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
								String assignedKeyValue = outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? String.valueOf(outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY)) : null;
								Long assignedKey = assignedKeyValue != null && !assignedKeyValue.isEmpty() && !("null").equalsIgnoreCase(assignedKeyValue)? Long.valueOf(assignedKeyValue) : null;
								if(assignedKey != null){
									intimationNoList.add(intimationNumber);
									rodKeyList.add(reimbKey);
									investgationKeyList.add(investigKey);
									invAssignedKeyList.add(assignedKey);
									workFlowMap.put(assignedKey,outPutArray);
									totalRecords = (Integer) outPutArray.get(SHAConstants.TOTAL_RECORDS);
								}
						}
					}
				
			List<SearchAssignInvestigationTableDTO> tableDTO = new ArrayList<SearchAssignInvestigationTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
			//	 humanTaskDTO = humanTaskListDTO.get(index);
				
				if(index < invAssignedKeyList.size()){
				 investigationAssignedKey = invAssignedKeyList.get(index);
				 rodKey = rodKeyList.get(index);
				// Integer taskNo = taskNumber.get(index);
				 investigationKey = investgationKeyList.get(index);
				 String role = null;
				 if(roleList != null && ! roleList.isEmpty()){
					 role = roleList.get(index);
				 }
				 tableDTO.addAll(getIntimationData(intimationNo,  investigationKey,rodKey,investigationAssignedKey/*, humanTaskDTO,role,taskNo*/));
				} 
			}

		
			Page<SearchAssignInvestigationTableDTO> page = new Page<SearchAssignInvestigationTableDTO>();
		//	page.setPageNumber(taskList.getCurrentPage());
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
	private List<SearchAssignInvestigationTableDTO> getIntimationData(String intimationNo, Long investigationKey, Long rodKey, Long investigationAssignedKey/*, 
			HumanTask humanTask,String role,Integer taskNumber*/){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		List<Intimation> intimationsList = new ArrayList<Intimation>(); 
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(intimationNo != null || !intimationNo.isEmpty()){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		
			for(Intimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Intimation> doList = intimationsList;
			List<SearchAssignInvestigationTableDTO> tableDTO = SearchAssignInvestigationMapper.getInstance().getIntimationDTO(doList);
			for (SearchAssignInvestigationTableDTO searchProcessInvestigationInitiatedTableDTO : tableDTO) {
				searchProcessInvestigationInitiatedTableDTO.setIntimationKey(searchProcessInvestigationInitiatedTableDTO.getKey());
				searchProcessInvestigationInitiatedTableDTO.setKey(investigationKey);
				searchProcessInvestigationInitiatedTableDTO.setInvestigationAssignedKey(investigationAssignedKey);
				AssignedInvestigatiorDetails assignedObj = getAssignedInvestigByKey(investigationAssignedKey);
				
//				Investigation investigObj = getByInvestigationKey(investigationKey);
				if(assignedObj != null && assignedObj.getInvestigatorName() != null){
					searchProcessInvestigationInitiatedTableDTO.setInvestigatorName(assignedObj.getInvestigatorName());
				}
				
				searchProcessInvestigationInitiatedTableDTO.setRodKey(rodKey);
				
				if(searchProcessInvestigationInitiatedTableDTO.getCpuCode() != null){
	        		
					Long cpuCode = searchProcessInvestigationInitiatedTableDTO.getCpuCode();
					searchProcessInvestigationInitiatedTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
				
				Claim claimByKey = getClaimByIntimation(searchProcessInvestigationInitiatedTableDTO.getIntimationKey());
				/*if(claimByKey != null && claimByKey.getCrcFlag()!= null && claimByKey.getCrcFlag().equals("Y")) {
                    searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("OLIVE");
				}*/
				if (claimByKey != null) {
					
					if(searchProcessInvestigationInitiatedTableDTO.getCrmFlagged() != null){
						if(searchProcessInvestigationInitiatedTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("OLIVE");
							}
							searchProcessInvestigationInitiatedTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("CRMVIP");
					}
						
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey,investigationAssignedKey/*, humanTask,role,taskNumber*/);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
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

	public AssignedInvestigatiorDetails getAssignedInvestigByKey(Long assignedKey)
	{
		List<AssignedInvestigatiorDetails> resultList = new ArrayList<AssignedInvestigatiorDetails>();
		try{
			Query query = entityManager.createNamedQuery("AssignedInvestigatiorDetails.findByAssignInvestigaitonKey");
			query.setParameter("key", assignedKey);
			resultList = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(resultList != null && !resultList.isEmpty()){
		
			return resultList.get(0);
		}
		return null;
		
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
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

	private List<SearchAssignInvestigationTableDTO> getHospitalDetails(
			List<SearchAssignInvestigationTableDTO> tableDTO, Long investigationKey,Long rodKey,Long investigationAssignedKey/*, 
			HumanTask humanTask,String role,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			// tableDTO.get(index).setHumanTaskDTO(humanTask);
			 tableDTO.get(index).setInvestigationKey(investigationKey);
			 tableDTO.get(index).setInvestigationAssignedKey(investigationAssignedKey);
			 tableDTO.get(index).setRodKey(rodKey);
			 
			 Object workflowKey = workFlowMap.get(investigationAssignedKey);
//				tableDto.setDbOutArray(workflowKey);
				tableDTO.get(index).setDbOutArray(workflowKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				// tableDTO.get(index).setTaskNumber(taskNumber);
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
				 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
				// tableDTO.get(index).setInvestigationRequestedRole(role);
				
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
