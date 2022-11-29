/**
 * 
 */
package com.shaic.reimbursement.queryrejection.processdraftquery.search;

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
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DocAcknowledgementDto;
import com.shaic.claim.DocAcknowledgementMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReimbursementQueryMapper;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;


/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessDraftQueryService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchProcessDraftQueryService() {
		super();
		
	}
	
	public  Page<SearchProcessDraftQueryTableDTO> search(
			SearchProcessDraftQueryFormDTO searchFormDTO,
			String userName, String passWord) {
		
		
		try{
			String intimationNo = null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String 	claimNo = null != searchFormDTO  && null !=  searchFormDTO.getClaimNo() ? searchFormDTO.getClaimNo() : null;
			String cpuCode = null != searchFormDTO  && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getValue() : null;
			
			String cpuCodeValues[] = cpuCode != null ? cpuCode.split(" ") : null;
			cpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;			
			
			String priority = null != searchFormDTO  && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO  && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO  && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String querytype = null != searchFormDTO  && searchFormDTO.getQueryType() != null ? searchFormDTO.getQueryType().getValue() != null ? searchFormDTO.getQueryType().getValue(): null : null;
			
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
				
			Long queryKey = null;
			//HumanTask humanTaskDTO = null;
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> queryKeyList = new ArrayList<Long>();
		//	List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();
						
		
			
			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			workFlowMap= new WeakHashMap<Long, Object>();			
			
			Integer totalRecords = 0; 
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PROCESS_DRAFT_QUERY_LETTER_CURRENT_QUEUE);
			
			if((ReferenceTable.PA_LOB_KEY).equals(searchFormDTO.getLobKey())){				
				mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);					
				
			}
			
			if((ReferenceTable.HEALTH_LOB_KEY).equals(searchFormDTO.getLobKey())){				
				mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);					
			}
			
			Long rodKey = null;
		//	PayloadBOType payloadBOType = null;
			
		//	IntimationType intimationType = new IntimationType();
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				}
				/*ClaimType claimType = new ClaimType();
=======
			if((ReferenceTable.PA_LOB_KEY).equals(searchFormDTO.getLobKey())){
				productInfo.setLob(SHAConstants.PA_LOB);
				payloadBOType.setProductInfo(productInfo);	
			}
			
			if((ReferenceTable.HEALTH_LOB_KEY).equals(searchFormDTO.getLobKey())){
				productInfo.setLob(SHAConstants.HEALTH_LOB);
				payloadBOType.setProductInfo(productInfo);	
			}
			
			
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);
				}
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
				if(null != claimNo && !claimNo.isEmpty()){
					
					claimType.setClaimId(claimNo);
					payloadBOType.setClaim(claimType);
				}
				ClaimRequestType claimRequestType = new ClaimRequestType(); */
				if(null != cpuCode){
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}
				
				if(null != claimNo){
					
					mapValues.put(SHAConstants.CLAIM_NUMBER, claimNo);
				}
				
				//ClassificationType classification = null;
				
				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
				//	classification = new ClassificationType();
					
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
						//	priority = null;
							//mapValues.put(SHAConstants.PRIORITY, null);
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					//classification.setPriority(priority);
					
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}					
					}
				}
				
				if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
					if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
					}else{
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
					}
			
			/*ProcessDraftQueryLetterTask processDraftQueryLetterTask = BPMClientContext.getProcessDraftQueryTask(userName, passWord);
			
			PagedTaskList taskList;
			if(null != intimationNo && !intimationNo.isEmpty() || null != claimNo && !claimNo.isEmpty() || null != cpuCode){
				 taskList = processDraftQueryLetterTask.getTasks(userName,null,payloadBOType);
			}else{
				taskList = processDraftQueryLetterTask.getTasks(userName,searchFormDTO.getPageable(),payloadBOType);
			}	
			
		
			if(null != processDraftQueryLetterTask){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(null != payloadBO.getQuery() && payloadBO.getQuery().getKey() != null && null != payloadBO.getRod()){
						
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
				
					queryKeyList.add(payloadBO.getQuery().getKey());
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
							Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_QUERY_KEY);
							String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
							Long reimbKey = (Long)outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
							workFlowMap.put(keyValue,outPutArray);
							intimationNoList.add(intimationNumber);
							rodKeyList.add(reimbKey);
							queryKeyList.add(keyValue);
							//ackKeyList.add(keyValue);
						
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
					}
				}
				
			List<SearchProcessDraftQueryTableDTO> tableDTO = new ArrayList<SearchProcessDraftQueryTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				// humanTaskDTO = humanTaskListDTO.get(index);

				 if(index < rodKeyList.size()){
				 queryKey = queryKeyList.get(index);
				 
				 rodKey = rodKeyList.get(index);
			//	 Integer taskNo = taskNumber.get(index);

				 
				 tableDTO.addAll(getIntimationData(intimationNo, queryKey,  rodKey/*, humanTaskDTO,taskNo*/));
				 }
			}

		
			Page<SearchProcessDraftQueryTableDTO> page = new Page<SearchProcessDraftQueryTableDTO>();
		//	page.setPageNumber(taskList.getCurrentPage());
			page.setTotalRecords(totalRecords);
			page.setPageItems(tableDTO);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
			return null;	
			
	}
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
		else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	private List<SearchProcessDraftQueryTableDTO> getIntimationData(String intimationNo, Long queryKey, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		List<Claim> intimationsList = new ArrayList<Claim>(); 
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(intimationNo != null || !intimationNo.isEmpty()){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		
			for(Claim inti:intimationsList){
				System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Claim> doList = intimationsList;
			List<SearchProcessDraftQueryTableDTO> tableDTO = SearchProcessDraftQueryMapper.getInstance().getClaimDTO(doList);
			
			ReimbursementQueryDto reimQDto = getReimbursementQueryByKey(queryKey);
			
			if(reimQDto != null){
			
			for (SearchProcessDraftQueryTableDTO searchProcessDraftQueryTableDTO : tableDTO) {
				searchProcessDraftQueryTableDTO.setReimbursementQueryDto(reimQDto);
				searchProcessDraftQueryTableDTO.setQueryType(reimQDto != null && reimQDto.getQueryType() != null && ("Y").equalsIgnoreCase(reimQDto.getQueryType()) ? SHAConstants.PAYMENT_QUERY_TYPE : SHAConstants.NORMAL_QUERY_TYPE);
				if(searchProcessDraftQueryTableDTO.getCpuId() != null){
					searchProcessDraftQueryTableDTO.setStrCpuCode(searchProcessDraftQueryTableDTO.getCpuId().toString());
				}
				Claim claimObject = getClaimByIntimation(searchProcessDraftQueryTableDTO.getIntimationkey());
				searchProcessDraftQueryTableDTO.setCrmFlagged(claimObject.getCrcFlag());
				if(searchProcessDraftQueryTableDTO.getCrmFlagged() != null){
					if(searchProcessDraftQueryTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchProcessDraftQueryTableDTO.setColorCodeCell("OLIVE");
						searchProcessDraftQueryTableDTO.setCrmFlagged(null);
//						searchProcessDraftQueryTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
				}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			}
			tableDTO = getHospitalDetails(tableDTO, queryKey, rodKey/*, humanTask,taskNumber*/);
		
			}

		return tableDTO;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	private List<SearchProcessDraftQueryTableDTO> getHospitalDetails(
			List<SearchProcessDraftQueryTableDTO> tableDTO, Long queryKey, Long rodKey/*,HumanTask humanTask,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			tableDTO.get(index).setRodKey(rodKey);
		//	tableDTO.get(index).setHumanTaskDTO(humanTask);
			tableDTO.get(index).setQueryKey(queryKey);
			Object workflowKey = workFlowMap.get(tableDTO.get(index).getQueryKey());
			
			tableDTO.get(index).setDbOutArray(workflowKey);
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				// tableDTO.get(index).setTaskNumber(taskNumber);
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
				
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
			return null;
		}
		
	}
	
	public ReimbursementQueryDto getReimbursementQueryByKey(
			Long reimbursementQueryKey) {
		
//		ReimbursementQuery reimbursementQuery = entityManager.find(ReimbursementQuery.class, reimbursementQueryKey);
		
		ReimbursementQueryDto reimbursementQueryDto = null;
		
		try{
		
		Query reimbQry = entityManager.createNamedQuery("ReimbursementQuery.findByKey");
		reimbQry.setParameter("primaryKey", reimbursementQueryKey);
		List<ReimbursementQuery> queryList = reimbQry.getResultList(); 
		
		if(queryList != null && !queryList.isEmpty()){
		ReimbursementQuery reimbursementQuery = queryList.get(0);
		
		ReimbursementQueryMapper rqMapper = new ReimbursementQueryMapper();
		
		reimbursementQueryDto = rqMapper.getReimbursementQueryDto(reimbursementQuery);
		
		Reimbursement reimbursement = reimbursementQuery.getReimbursement();
		
		ReimbursementMapper rMapper = new ReimbursementMapper();
		
		ReimbursementDto reimbursementDto = rMapper.getReimbursementDto(reimbursement);
		
		ClaimMapper clmMapper =  ClaimMapper.getInstance();
		ClaimDto clmDto = clmMapper.getClaimDto(reimbursement.getClaim());
		
		NewIntimationDto intimationDto = (new IntimationService()).getIntimationDto(reimbursement.getClaim().getIntimation(), entityManager);
		
		clmDto.setNewIntimationDto(intimationDto);
		reimbursementDto.setClaimDto(clmDto);
		DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
		DocAcknowledgementDto docAcknowledgementDto = new  DocAcknowledgementMapper().getDocAcknowledgementDto(docAcknowledgement);
		reimbursementDto.setDocAcknowledgementDto(docAcknowledgementDto);
		
		reimbursementQueryDto.setReimbursementDto(reimbursementDto);
		
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return reimbursementQueryDto;
	}
	
	
}