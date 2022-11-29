/**
 * 
 */
package com.shaic.reimbursement.queryrejection.draftrejection.search;

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
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchDraftRejectionLetterService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchDraftRejectionLetterService() {
		super();
		
	}
	
	public  Page<SearchDraftRejectionLetterTableDTO> search(
			SearchDraftRejectionLetterFormDTO searchFormDTO,
			String userName, String passWord) {
		
		try{
			String intimationNo = null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
			String 	claimNo = null != searchFormDTO  && null != searchFormDTO.getClaimNo() ? searchFormDTO.getClaimNo() : null;
			String cpuCode = searchFormDTO != null && null != searchFormDTO.getCpuCode() ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			
			String cpuCodeValues[] = cpuCode != null ? cpuCode.split(" ") : null;
			cpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;
			
			String priority = null != searchFormDTO  && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO  && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO  && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
		
			Long rodKey = null;
			
			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			workFlowMap= new WeakHashMap<Long, Object>();			
			
			Integer totalRecords = 0; 
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.DRAFT_REJECTION_LETTER_CURRENT_QUEUE);
			
			if((ReferenceTable.PA_LOB_KEY).equals(searchFormDTO.getLobKey())){				
				mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);					
				
			}
			
			if((ReferenceTable.HEALTH_LOB_KEY).equals(searchFormDTO.getLobKey())){				
				mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);					
			}
			
			
			
			List<Long> rodKeyList = new ArrayList<Long>();
			List<String> intimationNoList = new ArrayList<String>();
			List<Integer> taskNumber = new ArrayList<Integer>();
			
			
			
		
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				}
				
			/*	if(null != claimNo && !claimNo.isEmpty()){
=======
			List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			PayloadBOType payloadBOType = new PayloadBOType();
			IntimationType intimationType = new IntimationType();
			ProductInfoType productInfo = new ProductInfoType();
			ClaimType claimType = new ClaimType();

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
			
				if(null != claimNo && !claimNo.isEmpty()){
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
					
					
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
				
			//	ClassificationType classification = null;
				
				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){					
					
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){								
							mapValues.put(SHAConstants.PRIORITY, priority);
						}					
					
					if(source != null && ! source.isEmpty()){
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
			
		/*	DraftRejectionLetterTask draftRejectionLetterTask = BPMClientContext.getDraftRejectionLetterTask(userName, passWord);
			PagedTaskList taskList;
			if(null != intimationNo && !intimationNo.isEmpty() ||null !=claimNo && !claimNo.isEmpty() || null != cpuCode){
				 taskList = draftRejectionLetterTask.getTasks(userName,null,payloadBOType);
			}else{
				taskList = draftRejectionLetterTask.getTasks(userName,searchFormDTO.getPageable(),payloadBOType);
			}
		
			if(null != draftRejectionLetterTask){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(null != payloadBO.getRod()){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());					
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
//								Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_QUERY_KEY);
								String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
								Long reimbKey = (Long)outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
								workFlowMap.put(reimbKey,outPutArray);
								intimationNoList.add(intimationNumber);
								rodKeyList.add(reimbKey);
								//queryKeyList.add(keyValue);
								//ackKeyList.add(keyValue);
							
							totalRecords = (Integer) outPutArray
									.get(SHAConstants.TOTAL_RECORDS);
						}
					}
			List<SearchDraftRejectionLetterTableDTO> tableDTO = new ArrayList<SearchDraftRejectionLetterTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
//				 humanTaskDTO = humanTaskListDTO.get(index);
				if(index < rodKeyList.size()){
					rodKey = rodKeyList.get(index);
//				    Integer taskNo = taskNumber.get(index);
				 
				 tableDTO.addAll(getIntimationData(intimationNo, rodKey/* humanTaskDTO,taskNo*/));
				} 
			}

		
			Page<SearchDraftRejectionLetterTableDTO> page = new Page<SearchDraftRejectionLetterTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
			page.setTotalRecords(totalRecords);
			page.setPageItems(tableDTO);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
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

	private List<SearchDraftRejectionLetterTableDTO> getIntimationData(String intimationNo, Long rodKey/*,  HumanTask humanTask,Integer taskNumber*/){
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
			List<SearchDraftRejectionLetterTableDTO> tableDTO = SearchDraftRejectionLetterMapper.getInstance().getClaimDTO(doList);
			
			  for (SearchDraftRejectionLetterTableDTO searchDraftRejectionTableDTO : tableDTO) {
		        	
				  
				  if(searchDraftRejectionTableDTO.getReimbursementRejectionDto() != null && searchDraftRejectionTableDTO.getReimbursementRejectionDto().getReimbursementDto() != null && 
						  searchDraftRejectionTableDTO.getReimbursementRejectionDto().getReimbursementDto().getClaimDto() != null && searchDraftRejectionTableDTO.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto() != null)
				  {
					  String age = searchDraftRejectionTableDTO.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredFullAge();
					  searchDraftRejectionTableDTO.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setInsuredAge(age);
				  }
		        	if(searchDraftRejectionTableDTO.getCpuId() != null){
		        		
						Long cpuCode = searchDraftRejectionTableDTO.getCpuId();
						searchDraftRejectionTableDTO.setStrCpuCode(String.valueOf(cpuCode));
					}
		        	Claim claimObject = getClaimByIntimation(searchDraftRejectionTableDTO.getIntimationkey());
		        	searchDraftRejectionTableDTO.setCrmFlagged(claimObject.getCrcFlag());
					if(searchDraftRejectionTableDTO.getCrmFlagged() != null){
						if(searchDraftRejectionTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							searchDraftRejectionTableDTO.setColorCodeCell("OLIVE");
							searchDraftRejectionTableDTO.setCrmFlagged(null);
//							searchDraftQueryLetterTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
						}
								
					}
				}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getHospitalDetails(tableDTO, rodKey/*,  humanTask,taskNumber*/);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}


	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	private List<SearchDraftRejectionLetterTableDTO> getHospitalDetails(
			List<SearchDraftRejectionLetterTableDTO> tableDTO, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			tableDTO.get(index).setRodKey(rodKey);
//			tableDTO.get(index).setHumanTaskDTO(humanTask);
			
			Object workflowKey = workFlowMap.get(tableDTO.get(index).getRodKey());
//			tableDto.setDbOutArray(workflowKey);
			tableDTO.get(index).setDbOutArray(workflowKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
//				 tableDTO.get(index).setTaskNumber(taskNumber);
				 
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
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

}
