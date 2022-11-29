package com.shaic.reimbursement.processi_investigationi_initiated.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessInvestigationInitiatedService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager; 
	
	
	public SearchProcessInvestigationInitiatedService() {
		super();
		
	}
	public  Page<SearchProcessInvestigationInitiatedTableDTO> revisedSearchInvestigation(
			SearchProcessInvestigationInitiatedFormDTO searchFormDTO,
			String userName, String passWord)	{
		
			List<Map<String, Object>> taskParamObjList = null;
		
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		try{
			String intimationNo = null!= searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String 	policyNo =  null!= searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode =  null!= searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId().toString() : null;
			
			String priority = null!= searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null!= searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null!= searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType = null!= searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() != null ? searchFormDTO.getClaimType().getValue(): null : null;
			
			String hospitalType = null!= searchFormDTO && searchFormDTO.getHospitalType() != null ? searchFormDTO.getHospitalType().getValue() != null ? searchFormDTO.getHospitalType().getValue(): null : null;
			String networkType = null!= searchFormDTO && searchFormDTO.getNetworkHospType() != null ? searchFormDTO.getNetworkHospType().getValue() != null ? searchFormDTO.getNetworkHospType().getValue(): null : null;
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			
			Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
			Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
			Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
		
			Long investigationKey = null;
			Long rodKey = null;
			List<String> intimationNoList = new ArrayList<String>();
			
			List<Long> investgationKeyList = new ArrayList<Long>();
			List<Long> rodKeyList = new ArrayList<Long>();
			
			List<String> roleList = new ArrayList<String>();
			
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.INITIATE_INVESTIGATION_CURRENT_QUE);
			
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
			}
			if(null != policyNo && !policyNo.isEmpty()){
				
				mapValues.put(SHAConstants.POLICY_NUMBER,policyNo);
			}

			if(claimType != null && ! claimType.isEmpty()){
					
				mapValues.put(SHAConstants.CLAIM_TYPE, claimType);
			}

			if(null != cpuCode){
			
				mapValues.put(SHAConstants.CPU_CODE,cpuCode);
				
			}
			
			if((priority != null && ! priority.isEmpty()) || (source != null && ! source.isEmpty())
							|| (type != null && ! type.isEmpty())){
						
						if(priority != null && ! priority.isEmpty())
							if(priority.equalsIgnoreCase(SHAConstants.ALL)){
								priority = null;
							}

						mapValues.put(SHAConstants.PRIORITY,priority);
						
						if(source != null && ! source.isEmpty()){
							mapValues.put(SHAConstants.STAGE_SOURCE,source);
						}
						
						if(type != null && ! type.isEmpty()){
							if(type.equalsIgnoreCase(SHAConstants.ALL)){
								type = null;
							}
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}						
				}
			
				if(hospitalType != null && ! hospitalType.isEmpty()){
					mapValues.put(SHAConstants.HOSPITAL_TYPE, hospitalType);
				}
				
				if(networkType != null && ! networkType.isEmpty()){
					mapValues.put(SHAConstants.NETWORK_TYPE, networkType);
				}
				
				/*if (priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL)) {
					if (priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)) {
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
					} else {
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
					}
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
		
				//2019058 Changing the get task 
//				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForAssignInvestigationGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
//				taskParamObjList = dbCalculationService.revisedGetTaskProcedure(setMapValues);
				taskParamObjList = dbCalculationService.revisedAssignInvestigationGetTaskProcedure(setMapValues);
				Integer totalRecs = 0;
				
				Map<String, Map<String, Object>> workFlowMap= new WeakHashMap<String, Map<String, Object>>();
				
	
			if(null != taskParamObjList){
				
				for( Map<String, Object> workTask : taskParamObjList){

					if(null != workTask.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY)){
						intimationNoList.add(String.valueOf(workTask.get(SHAConstants.INTIMATION_NO)));
						roleList.add(String.valueOf(workTask.get(SHAConstants.PAYLOAD_REIMB_REQ_BY)));
						investgationKeyList.add((Long)workTask.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY));
						rodKeyList.add((Long)workTask.get(SHAConstants.PAYLOAD_ROD_KEY));
						workFlowMap.put(String.valueOf(workTask.get(SHAConstants.INTIMATION_NO)),workTask);
						totalRecs = (Integer)workTask.get(SHAConstants.TOTAL_RECORDS);
  				    }
				}
			}
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO = new ArrayList<SearchProcessInvestigationInitiatedTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				Map<String, Object> workTask = workFlowMap.get(intimationNo);
				
				if(index < investgationKeyList.size()){
				 investigationKey = investgationKeyList.get(index);
				 rodKey = rodKeyList.get(index);
				 String role = null;
				 if(roleList != null && ! roleList.isEmpty()){
					 role = roleList.get(index);
				 }
				 tableDTO.addAll(getRevisedIntimationData(intimationNo,  investigationKey,rodKey, workTask,role));
				} 
			}

		
			Page<SearchProcessInvestigationInitiatedTableDTO> page = new Page<SearchProcessInvestigationInitiatedTableDTO>();
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecs);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
		
	
	}
	
	public  Page<SearchProcessInvestigationInitiatedTableDTO> search(
			SearchProcessInvestigationInitiatedFormDTO searchFormDTO,
			String userName, String passWord) {
		
		
		/*try{
			String intimationNo = null!= searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String 	policyNo =  null!= searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode =  null!= searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId().toString() : null;
			
			String priority = null!= searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null!= searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null!= searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType = null!= searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() != null ? searchFormDTO.getClaimType().getValue(): null : null;
		
			Long investigationKey = null;
			Long rodKey = null;
			HumanTask humanTaskDTO = null;
			List<String> intimationNoList = new ArrayList<String>();
			
			List<Long> investgationKeyList = new ArrayList<Long>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();
			
			List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			List<String> roleList = new ArrayList<String>();
			PayloadBOType payloadBOType = null;
			
			IntimationType intimationType = new IntimationType();
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				if(payloadBOType == null){
					payloadBOType = new PayloadBOType();
				}
				
				intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);
				
				}
				PolicyType policyType = new PolicyType();
				if(null != policyNo && !policyNo.isEmpty()){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					policyType.setPolicyId(policyNo);
					payloadBOType.setPolicy(policyType);
					
				}
				ClaimRequestType claimRequestType = new ClaimRequestType(); 
				
				ClaimType claimType1 = new ClaimType();
				if(claimType != null && ! claimType.isEmpty()){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimType1.setClaimType(claimType);
					payloadBOType.setClaim(claimType1);
				}
				
		
				if(null != cpuCode){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimRequestType.setCpuCode(cpuCode);
					
				    payloadBOType.setClaimRequest(claimRequestType);
				
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
						
						if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}
						
						payloadBOType.setClassification(classification);
				}
			    
			    Pageable pageable = searchFormDTO.getPageable();
				
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			    
			
			ProcessInvestigationTask processInvestigationTask = BPMClientContext.getProcessInvestigationInitTask(userName, passWord);
			
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
			}
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO = new ArrayList<SearchProcessInvestigationInitiatedTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				 humanTaskDTO = humanTaskListDTO.get(index);
				
				if(index < investgationKeyList.size()){
				 investigationKey = investgationKeyList.get(index);
				 rodKey = rodKeyList.get(index);
				 Integer taskNo = taskNumber.get(index);
				 String role = null;
				 if(roleList != null && ! roleList.isEmpty()){
					 role = roleList.get(index);
				 }
				 tableDTO.addAll(getIntimationData(intimationNo,  investigationKey,rodKey, humanTaskDTO,role,taskNo));
				} 
			}

		
			Page<SearchProcessInvestigationInitiatedTableDTO> page = new Page<SearchProcessInvestigationInitiatedTableDTO>();
			page.setPageNumber(taskList.getCurrentPage());
			page.setPageItems(tableDTO);
			page.setTotalRecords(taskList.getTotalRecords());
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}*/
			return null;	
		
	}
	private List<SearchProcessInvestigationInitiatedTableDTO> getRevisedIntimationData(String intimationNo ,Long investigationKey,Long rodKey, 
			Object workTask,String role){
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
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO = SearchProcessInvestigationInitiatedMapper.getInstance().getIntimationDTO(doList);
			for (SearchProcessInvestigationInitiatedTableDTO searchProcessInvestigationInitiatedTableDTO : tableDTO) {
				
				Claim claim = getClaimByIntimation(searchProcessInvestigationInitiatedTableDTO.getKey());
				/*if(claim != null && claim.getCrcFlag()!= null && claim.getCrcFlag().equals("Y")) {
					searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("OLIVE");
				}*/
				if (claim != null) {
					
					if(searchProcessInvestigationInitiatedTableDTO.getCrmFlagged() != null){
						if(searchProcessInvestigationInitiatedTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y")) {
								searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("OLIVE");
							}
							searchProcessInvestigationInitiatedTableDTO.setCrmFlagged(null);
						}
					}
					if (claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("VIP");
					}
					if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y") 
							&& claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessInvestigationInitiatedTableDTO.setColorCodeCell("CRMVIP");
					}
						
				}
				
				searchProcessInvestigationInitiatedTableDTO.setKey(investigationKey);
				searchProcessInvestigationInitiatedTableDTO.setRodKey(rodKey);
				searchProcessInvestigationInitiatedTableDTO.setHospitalType(String.valueOf(((Map<String, Object>)workTask).get("HOSPITAL_TYPE")));
				searchProcessInvestigationInitiatedTableDTO.setNetworkHospType(((Map<String, Object>)workTask).get("NETWORK_TYPE") != null ? String.valueOf(((Map<String, Object>)workTask).get("NETWORK_TYPE")) : "");
				
				if(searchProcessInvestigationInitiatedTableDTO.getCpuCode() != null){
					Long cpuCode = searchProcessInvestigationInitiatedTableDTO.getCpuCode();
					searchProcessInvestigationInitiatedTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}				
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getRevisedHospitalDetails(tableDTO, investigationKey,rodKey, workTask,role);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}
	private List<SearchProcessInvestigationInitiatedTableDTO> getIntimationData(String intimationNo ,Long investigationKey,Long rodKey, 
			 String role,Integer taskNumber){
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
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO = SearchProcessInvestigationInitiatedMapper.getInstance().getIntimationDTO(doList);
			for (SearchProcessInvestigationInitiatedTableDTO searchProcessInvestigationInitiatedTableDTO : tableDTO) {
				searchProcessInvestigationInitiatedTableDTO.setKey(investigationKey);
				searchProcessInvestigationInitiatedTableDTO.setRodKey(rodKey);
				
				if(searchProcessInvestigationInitiatedTableDTO.getCpuCode() != null){
	        		
					Long cpuCode = searchProcessInvestigationInitiatedTableDTO.getCpuCode();
					searchProcessInvestigationInitiatedTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey,role,taskNumber);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchProcessInvestigationInitiatedTableDTO> getRevisedHospitalDetails(
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO, Long investigationKey,Long rodKey, 
			Object workTask,String role) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			 tableDTO.get(index).setDbOutArray(workTask);
			 tableDTO.get(index).setInvestigationKey(investigationKey);
			 tableDTO.get(index).setRodKey(rodKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
				 tableDTO.get(index).setInvestigationRequestedRole(role);
				 Map<String,Object> workObj = (Map<String,Object>)workTask;
				 tableDTO.get(index).setClaimType(String.valueOf(workObj.get(SHAConstants.CLAIM_TYPE)));
				 Investigation investigation = getByInvestigationKey(investigationKey);
				 if(investigation != null){
					 Date invDt = investigation.getCreatedDate();
					 String dtVal = new SimpleDateFormat("dd/MM/yyyy").format(invDt);
					 tableDTO.get(index).setDtofInvInitiated(dtVal);
				 }	 
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	private List<SearchProcessInvestigationInitiatedTableDTO> getHospitalDetails(
			List<SearchProcessInvestigationInitiatedTableDTO> tableDTO, Long investigationKey,Long rodKey, 
			 String role,Integer taskNumber) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			//
			// tableDTO.get(index).setHumanTaskDTO(humanTask);
			 tableDTO.get(index).setInvestigationKey(investigationKey);
			 tableDTO.get(index).setRodKey(rodKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setTaskNumber(taskNumber);
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
				 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
				 tableDTO.get(index).setInvestigationRequestedRole(role);
				
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
}