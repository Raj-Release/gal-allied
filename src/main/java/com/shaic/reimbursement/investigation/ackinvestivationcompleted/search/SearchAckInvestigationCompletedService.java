/**
 * 
 */
package com.shaic.reimbursement.investigation.ackinvestivationcompleted.search;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpCPUCode;
/*import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType;
import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType;*/
/*import com.shaic.ims.bpm.claim.corev2.PagedTaskList;
import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.ims.bpm.claim.servicev2.investigation.search.AckInvestigationTask;
import com.shaic.ims.bpm.claim.servicev2.investigation.search.ProcessInvestigationTask;*/
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchAckInvestigationCompletedService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchAckInvestigationCompletedService() {
		super();
		
	}
	
	public  Page<SearchAckInvestigationCompletedTableDTO> search(
			SearchAckInvestigationCompletedFormDTO searchFormDTO,
			String userName, String passWord) {/*
		
		try{
			String intimationNo = null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String 	claimNo = null != searchFormDTO && null != searchFormDTO.getClaimNo() ? searchFormDTO.getClaimNo() : null;
			String cpuCode = null != searchFormDTO && null != searchFormDTO.getCpuCode()  ? searchFormDTO.getCpuCode().getId().toString() : null;	
			
			String priority =  null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source =  null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType1 =  null != searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() != null ? searchFormDTO.getClaimType().getValue(): null : null;
		
			Long investigationKey = null;
			Long rodKey = null;
			HumanTask humanTaskDTO = null;
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> investigationKeyList = new ArrayList<Long>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();
			
			List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			PayloadBOType payloadBOType = null;
			IntimationType intimationType = new IntimationType();
			
			
			
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				if(payloadBOType == null){
					payloadBOType = new PayloadBOType();
				}
				
				intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);
				}
			
			
				ClaimType claimType = new ClaimType();
				if(null != claimNo && !claimNo.isEmpty()){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimType.setClaimId(claimNo);
					payloadBOType.setClaim(claimType);
				}
				
				ClaimRequestType claimRequestType = new ClaimRequestType(); 
				
                  if(null != cpuCode){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimRequestType.setCpuCode(cpuCode);
				    payloadBOType.setClaimRequest(claimRequestType);
				
				  }
				
				
				if(claimType1 != null){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					claimType.setClaimType(claimType1);
					payloadBOType.setClaim(claimType);
				}

			AckInvestigationTask ackInvestigationTask = BPMClientContext.getAckInvestigationCplTask(userName, passWord);
			
			Pageable pageable = null;
			
			PagedTaskList taskList = ackInvestigationTask.getTasks(userName,pageable,payloadBOType);
			
			
			
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
			
			PagedTaskList taskList;

			taskList = ackInvestigationTask.getTasks(userName,pageable,payloadBOType);	

			if(null != ackInvestigationTask){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(payloadBO.getInvestigation() != null){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					investigationKeyList.add(payloadBO.getInvestigation().getKey());
					if(payloadBO.getRod() != null){
						rodKeyList.add(payloadBO.getRod().getKey());
					}else{
						rodKeyList.add(0l);
					}
					taskNumber.add(humanTask.getNumber());
					humanTaskListDTO.add(humanTask);
					}
				}
			}
			List<SearchAckInvestigationCompletedTableDTO> tableDTO = new ArrayList<SearchAckInvestigationCompletedTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				 humanTaskDTO = humanTaskListDTO.get(index);
				
				 investigationKey = investigationKeyList.get(index);
				 if(! rodKeyList.isEmpty()){
					 rodKey = rodKeyList.get(index);
				 }
				 Integer taskNo = taskNumber.get(index);
				 
				 tableDTO.addAll(getIntimationData(intimationNo, investigationKey,rodKey, humanTaskDTO,taskNo));
				 
			}

		
			Page<SearchAckInvestigationCompletedTableDTO> page = new Page<SearchAckInvestigationCompletedTableDTO>();
			page.setPageNumber(taskList.getCurrentPage());
			page.setPageItems(tableDTO);
			page.setTotalRecords(taskList.getTotalRecords());
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
		
			
	*/
		return null;
		}
	/*private List<SearchAckInvestigationCompletedTableDTO> getIntimationData(String intimationNo,Long investigationKey,
			Long rodKey, HumanTask humanTask,Integer taskNumber){
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
			List<SearchAckInvestigationCompletedTableDTO> tableDTO = SearchAckInvestigationCompletedMapper.getInstance().getClaimDTO(doList);
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey, humanTask,taskNumber);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}*/



	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	/*private List<SearchAckInvestigationCompletedTableDTO> getHospitalDetails(
			List<SearchAckInvestigationCompletedTableDTO> tableDTO, Long investigationKey,Long rodKey, HumanTask humanTask,Integer taskNumber) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			 tableDTO.get(index).setHumanTaskDTO(humanTask);
			 tableDTO.get(index).setInvestigationKey(investigationKey);
			 tableDTO.get(index).setRodKey(rodKey);
			 if(rodKey != null && rodKey.equals(0l)){
				 tableDTO.get(index).setRodKey(null);
			 }
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setTaskNumber(taskNumber);
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
				 tableDTO.get(index).setCpuCode(cpuCode);
				 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
				
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	*/
	

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
	
	/*private Long getInvestigationKey(Long rodKey){
		try{
			Query findClaimKey = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", rodKey);
			List<Reimbursement> reimbursementList = findClaimKey.getResultList();
			return getInvestigationByClaimKey(reimbursementList.get(0).getClaim().getKey());
			}catch(Exception e){
				return null;
			}
	}
	
	private Long getInvestigationByClaimKey(Long claimKey){
		
		try{
			Query findinvestigationKey = entityManager.createNamedQuery("Investigation.findByClaimKey").setParameter("claimKey", claimKey);
			List<Investigation> InvestigationList =  findinvestigationKey.getResultList();
			return InvestigationList.get(0).getKey();
			}catch(Exception e){
				return null;
			}
		
	}*/
	
}