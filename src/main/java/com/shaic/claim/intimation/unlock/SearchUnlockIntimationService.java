package com.shaic.claim.intimation.unlock;

import javax.ejb.Stateless;
import javax.naming.NamingException;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Intimation;

@Stateless
public class SearchUnlockIntimationService extends AbstractDAO<Intimation> {
	

	
	public SearchUnlockIntimationService(){
		
	}
	
	
	public  Page<SearchUnlockIntimationTableDTO> searchForUnlockIntimation(
			SearchUnlockIntimationFormDTO formDTO,
			String userName, String passWord) throws NamingException {/*
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		
		
		Page<SearchUnlockIntimationTableDTO> page = new Page<SearchUnlockIntimationTableDTO>();
		
		PayloadBOType payloadBOType =  new PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimPayload = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
		
		IntimationType intimationType = new IntimationType();
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType reimIntimation = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
		
		TaskInfo taskInfo = new  TaskInfo();
		
		ProductInfoType productInfo = new ProductInfoType();
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfoReim = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
		
		
		ClaimType claimType = new ClaimType();
		
		DocReceiptACKType docReceiptACK = new DocReceiptACKType();
		
		
		intimationType.setReason("HEALTH");
		reimIntimation.setReason("HEALTH");
		
		productInfo.setLob("H");
		productInfoReim.setLob("H");
		
		claimType.setCoverBenifitType("HEALTH");
		
		docReceiptACK.setDocUpload("HEALTH");

		
		payloadBOType.setProductInfo(productInfo);
		payloadBOType.setIntimation(intimationType);
		
		reimPayload.setProductInfo(productInfoReim);
		reimPayload.setIntimation(reimIntimation);
		
		reimPayload.setClaim(claimType);
		reimPayload.setDocReceiptACK(docReceiptACK);
		
		
		
		
		
		taskInfo.setMinutes(0);
		
		
		if(null != intimationNo && !intimationNo.isEmpty()) {
			intimationType.setIntimationNumber(intimationNo);
			payloadBOType.setIntimation(intimationType);
			
			
			reimIntimation.setIntimationNumber(intimationNo);
			reimPayload.setIntimation(reimIntimation);
		}
			
		
			
		Pageable pageable = formDTO.getPageable();
		pageable.setPageNumber(1);
		
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		List<HumanTaskDetail> unlockIntmationList = new ArrayList<HumanTaskDetail>();
		
		
		AcquireHTList unlockIntimationTask = BPMClientContext.getAllUnlockedTask();
		PagedTaskListDetail tasks = unlockIntimationTask.getTasks("staradmin", pageable, payloadBOType, taskInfo);
		
		PagedTaskListDetail reimTask = unlockIntimationTask.getTasks("staradmin", pageable, reimPayload, taskInfo);
		
		if(null != tasks) {
			unlockIntmationList.addAll(tasks.getHumanTasksDetail());
		}
		
		if(null != reimTask){
			unlockIntmationList.addAll(reimTask.getHumanTasksDetail());
		}
		
		
		List<SearchUnlockIntimationTableDTO> commonList = new ArrayList<SearchUnlockIntimationTableDTO>();
		for (HumanTaskDetail humanTask : unlockIntmationList) {
			SearchUnlockIntimationTableDTO searchUnlockIntimationTableDTO = new SearchUnlockIntimationTableDTO();
			if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getIntimation() != null ){
				searchUnlockIntimationTableDTO.setIntimationNo(humanTask.getPayloadCashless().getIntimation().getIntimationNumber());
				searchUnlockIntimationTableDTO.setLockedBy(humanTask.getUserName());
				searchUnlockIntimationTableDTO.setStage(humanTask.getTaskQueueName());
				searchUnlockIntimationTableDTO.setHumanTaskDetail(humanTask);
				commonList.add(searchUnlockIntimationTableDTO);
			}else if(humanTask.getPayload() != null && humanTask.getPayload().getIntimation() != null){
				searchUnlockIntimationTableDTO.setIntimationNo(humanTask.getPayload().getIntimation().getIntimationNumber());
				searchUnlockIntimationTableDTO.setLockedBy(humanTask.getUserName());
				searchUnlockIntimationTableDTO.setStage(humanTask.getTaskQueueName());
				searchUnlockIntimationTableDTO.setHumanTaskDetail(humanTask);
				commonList.add(searchUnlockIntimationTableDTO);
			}
			
		}
		
		
		page.setPageItems(commonList);
		return page;
	*/
		return null;}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
