/**
 * 
 */
package com.shaic.reimbursement.rod.createrod.search;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;



/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchCreateRODService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	Map<Long, Object> workFlowMap= null;
	
	public SearchCreateRODService() {
		super();
	}

	public  Page<SearchCreateRODTableDTO> search(
			SearchCreateRODFormDTO searchFormDTO,
			String userName, String passWord) {
	
		try{
		String intimationNo =   null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
		String policyNo =  null != searchFormDTO && null !=  searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
		String cpuCode =  null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString(): null : null;
		String priority =  null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
		String source =  null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
		String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
		String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
		
		String docUpload = null;
		Long ackKey = null;
		List<Map<String, Object>> taskProcedure = null ;
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		workFlowMap= new WeakHashMap<Long, Object>();
		
		
		
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.USER_ID, userName);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.ROD_CURRENT_KEY);
		
		mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
		mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
		
		
		if(null != searchFormDTO &&  null != searchFormDTO.getIsDocumentUploaded() && null != searchFormDTO.getIsDocumentUploaded().getValue())
		{
			docUpload = searchFormDTO.getIsDocumentUploaded().getValue().trim();
		}

		
		List<String> intimationNoList = new ArrayList<String>();

		List<Long> ackKeyList = new ArrayList<Long>();
		//List<Integer> taskNumber = new ArrayList<Integer>();
		
//		List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
	
	

		
		
		if(null != intimationNo && !intimationNo.isEmpty()){
			
			mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);			

		
		}
		
		if(null != policyNo && !policyNo.isEmpty()){
						
			mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
			
		}	
		

		if(null != cpuCode){			
			mapValues.put(SHAConstants.CPU_CODE, cpuCode);
		}	
	
		
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
			if(priorityNew.equalsIgnoreCase("ED Club")){
				mapValues.put(SHAConstants.PRIORITY, "ED");
			}
			else if(priorityNew.equalsIgnoreCase("MD Club")){
				mapValues.put(SHAConstants.PRIORITY, "ED");
			}
			else if(priorityNew.equalsIgnoreCase("CMD Elite Club")){
				mapValues.put(SHAConstants.PRIORITY, "CMD ELITE");
			}
			else if(priorityNew.equalsIgnoreCase("ZM Club")){
				mapValues.put(SHAConstants.PRIORITY, "ZM");
			}
			else if(priorityNew.equalsIgnoreCase("CMD Club")){
				mapValues.put(SHAConstants.PRIORITY, "CMD");
			}
			else if(priorityNew.equalsIgnoreCase("BM Club")){
				mapValues.put(SHAConstants.PRIORITY, "BM");
			}
			else if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
				mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
			}
			else if(priorityNew.equalsIgnoreCase("Corporate – High Priority")){
				mapValues.put(SHAConstants.PRIORITY, "ATOS");
			}
			else if(priorityNew.equalsIgnoreCase("VIP")){
				mapValues.put(SHAConstants.PRIORITY, "VIP");
			}else{
				mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
			}

//		CreateRODTask createRODTask = BPMClientContext.getCreateRODTask(userName, passWord);
//		
//		PagedTaskList taskList;
		
		Pageable pageable = searchFormDTO.getPageable();
		if(pageable == null){
			pageable = new Pageable();
		}
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);

//	    taskList = createRODTask.getTasks(userName,pageable,payloadBOType);

		/*if(null != createRODTask){
			List<HumanTask> humanTaskList = taskList.getHumanTasks();
			if(null == humanTaskList | humanTaskList.isEmpty()){
				return null;
			}
			
			for(HumanTask humanTask : humanTaskList){
				PayloadBOType payloadBO = humanTask.getPayload();
				
				if(null != payloadBO.getDocReceiptACK()){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());				
					ackKeyList.add(payloadBO.getDocReceiptACK().getKey());
					taskNumber.add(humanTask.getNumber());
					humanTaskListDTO.add(humanTask);
				}
			}
		}*/
		

		//Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		taskProcedure = dbCalculationService.revisedGetTaskRODOfflineProcedure(setMapValues);	
		
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ACK_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);
						ackKeyList.add(keyValue);
					
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
				
		List<SearchCreateRODTableDTO> tableDTO = new ArrayList<SearchCreateRODTableDTO>();
		for(int index = 0 ; index < intimationNoList.size() ; index++){
			
			 intimationNo = intimationNoList.get(index);
//			 humanTaskDTO = humanTaskListDTO.get(index);
			 if(index < ackKeyList.size()){
			 ackKey = ackKeyList.get(index);
//			 Integer taskNo = taskNumber.get(index);
			 tableDTO.addAll(getIntimationData(intimationNo,  ackKey/*, humanTaskDTO*//*,taskNo,*/,docUpload));
		}
		}
		
		List<SearchCreateRODTableDTO> ackDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
		List<SearchCreateRODTableDTO> nonAckDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
		if(null != docUpload)
		{	
			for (SearchCreateRODTableDTO searchCreateRODTableDTO2 : tableDTO) {
				if((SHAConstants.YES).equalsIgnoreCase(docUpload) && (SHAConstants.YES).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
				{
					ackDocUploadList.add(searchCreateRODTableDTO2);
				}
				if((SHAConstants.No).equalsIgnoreCase(docUpload) && (SHAConstants.No).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
						{
							nonAckDocUploadList.add(searchCreateRODTableDTO2);
						}
			}
		}
		
		Page<SearchCreateRODTableDTO> page = new Page<SearchCreateRODTableDTO>();
		
		SHAUtils.setClearReferenceData(mapValues);
		SHAUtils.setClearMapValue(workFlowMap);
		
		if(null != docUpload && (SHAConstants.YES).equalsIgnoreCase(docUpload))
		{
//			taskList.setTotalRecords(ackDocUploadList.size());
			page.setPageItems(ackDocUploadList);
		}
		else if(null != docUpload && (SHAConstants.No).equalsIgnoreCase(docUpload))
		{
//			taskList.setTotalRecords(nonAckDocUploadList.size());
			page.setPageItems(nonAckDocUploadList);
		}
		else
		{
			page.setPageItems(tableDTO);
		}
//			page.setTotalRecords(taskList.getTotalRecords());
		    page.setTotalRecords(totalRecords);
//			page.setPageNumber(taskList.getCurrentPage());
		return page;
		}
		
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	
	
	private AcknowledgeDocument isDocumentUploadedForAck(Long docAckKey)
	{
		Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByDocAcknowledgementKey");
		query = query.setParameter("docAckKey", docAckKey);
		List<AcknowledgeDocument> ackDocList = query.getResultList();
		if(null != ackDocList && !ackDocList.isEmpty())
		{
			entityManager.refresh(ackDocList.get(0));
			return ackDocList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private List<SearchCreateRODTableDTO> getIntimationData(String intimationNo, Long ackKey/*, HumanTask humanTask*//*,Integer taskNumber*/,String docUpload){
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
			
		if(intimationNo != null || (intimationNo != null && !intimationNo.isEmpty())){
		Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
		conditionList.add(condition1);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}else {
				return null;
			}
		
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			List<Intimation> listIntimations = new ArrayList<Intimation>(); 
			listIntimations = typedQuery.getResultList();
		
			for(Intimation inti:listIntimations){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			//List<SearchCreateRODTableDTO> uploadedAckDocumentList = new ArrayList<SearchCreateRODTableDTO>();
			List<SearchCreateRODTableDTO> tableDTO = SearchCreateRODMapper.getInstance().getIntimationDTO( listIntimations);
			for (SearchCreateRODTableDTO searchCreateRODTableDTO : tableDTO) {
				
				if(searchCreateRODTableDTO.getIntimationNo() != null){
					Intimation intimationByNo = getIntimationByNo(searchCreateRODTableDTO.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						searchCreateRODTableDTO.setColorCode(colorCodeForGMC);
					}
				}
				Claim claimByKey = getClaimByIntimation(searchCreateRODTableDTO.getKey());
				searchCreateRODTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
				if(searchCreateRODTableDTO.getCrmFlagged() != null){
					if(searchCreateRODTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchCreateRODTableDTO.setColorCodeCell("OLIVE");
						searchCreateRODTableDTO.setCrmFlagged(null);
						//searchEnterBillDetailTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
				}
				
				if(searchCreateRODTableDTO.getCpuCode() != null){
					Long cpuCode = searchCreateRODTableDTO.getCpuCode();
					searchCreateRODTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
				AcknowledgeDocument ackDoc = isDocumentUploadedForAck(ackKey);
				
				
				if(claimByKey != null) {
					searchCreateRODTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchCreateRODTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchCreateRODTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
				}
				
				/**
				 * Below blockw will be invoked , if document upload
				 * dropdown is selected. 
				 * */
				if(null != docUpload)
				{
					if(null != ackDoc && (SHAConstants.YES).equalsIgnoreCase(docUpload))
						{ 
							searchCreateRODTableDTO.setAckDocKey(ackDoc.getKey());
							if(null != ackDoc.getDocAcknowledgement())
							searchCreateRODTableDTO.setDocAcknowledgementKey(ackDoc.getDocAcknowledgement().getKey());
							searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.YES);
						}
					else if(null == ackDoc  && (SHAConstants.No).equalsIgnoreCase(docUpload))
						searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.No);
					//uploadedAckDocumentList.add(searchCreateRODTableDTO);
				}
				
				/**
				 * Below blockw will be invoked , if document upload
				 * dropdown is not selected.
				 * */
				
				else if(null != ackDoc)
				{
					searchCreateRODTableDTO.setAckDocKey(ackDoc.getKey());
					if(null != ackDoc.getDocAcknowledgement())
					searchCreateRODTableDTO.setDocAcknowledgementKey(ackDoc.getDocAcknowledgement().getKey());
					searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.YES);
				}
				else if (null == ackDoc)
				{
					searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.No);
				}
				
			}
			
			/*if(null != uploadedAckDocumentList && !uploadedAckDocumentList.isEmpty())
			{
				uploadedAckDocumentList = getclaimNumber(uploadedAckDocumentList);
				uploadedAckDocumentList = getHospitalDetails(uploadedAckDocumentList, ackKey, humanTask,taskNumber);
				return uploadedAckDocumentList;
			}
			else*/
			//{
				tableDTO = getclaimNumber(tableDTO);
				tableDTO = getHospitalDetails(tableDTO, ackKey/*, humanTask*//*,taskNumber*/);
				return tableDTO;
			//}
		
	}catch(Exception e){
		return null;
	}	
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchCreateRODTableDTO> getclaimNumber(List<SearchCreateRODTableDTO> intimationList){
		Claim a_claim = null;
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getKey());
			
			if (intimationList.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index).getKey());
				try{
						a_claim = (Claim) findByIntimationKey.getSingleResult();
					
						if(a_claim != null){
							entityManager.refresh(a_claim);
							intimationList.get(index).setClaimNo(a_claim.getClaimId());
							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							intimationList.get(index).setClaimkey(a_claim.getKey());
							intimationList.get(index).setStatus(a_claim.getStatus().getProcessValue());
						}else{
							intimationList.get(index).setClaimNo("");
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}
	
	private List<SearchCreateRODTableDTO> getHospitalDetails(
			List<SearchCreateRODTableDTO> tableDTO, Long ackKey/*, HumanTask humanTaskListDTO*//*,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			tableDTO.get(index).setAckNo(ackKey);
			//tableDTO.get(index).setTaskNumber(taskNumber);
			
			if(ackKey != null){
				DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
				String acknowledgeNumber = docAcknowledgement.getAcknowledgeNumber();
				tableDTO.get(index).setAcknowledgementNumber(acknowledgeNumber);
//				tableDTO.get(index).setStatus(docAcknowledgement.getStatus().getProcessValue());
				
			}
			
//			tableDTO.get(index).setHumanTaskListDTO(humanTaskListDTO);
			Object workflowKey = workFlowMap.get(ackKey);
//			tableDto.setDbOutArray(workflowKey);
			tableDTO.get(index).setDbOutArray(workflowKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
			 }
			}catch(Exception e){
				continue;
			}
		}
		
		return tableDTO;
	}
	
	/*private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}*/
	
	public  Page<SearchCreateRODTableDTO> searchForCancelAcknowledgement(
			SearchCreateRODFormDTO searchFormDTO,
			String userName, String passWord) {
	
		try{
		String intimationNo =   null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
		String policyNo =  null != searchFormDTO && null !=  searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
		String cpuCode =  null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString(): null : null;
		String priority =  null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
		String source =  null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
		String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
		String ackNo =  null != searchFormDTO && null !=  searchFormDTO.getAcknowledgementNo() ? searchFormDTO.getAcknowledgementNo() : null;
		String docUpload = null;
		Long ackKey = null;
	//	HumanTask humanTaskDTO = null;
		List<Map<String, Object>> taskProcedure = null ;
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		workFlowMap= new WeakHashMap<Long, Object>();
		
		
		
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.USER_ID, userName);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.ROD_CURRENT_KEY);
		
		
		if(null != searchFormDTO &&  null != searchFormDTO.getIsDocumentUploaded() && null != searchFormDTO.getIsDocumentUploaded().getValue())
		{
			docUpload = searchFormDTO.getIsDocumentUploaded().getValue().trim();
		}

		
		List<String> intimationNoList = new ArrayList<String>();

		List<Long> ackKeyList = new ArrayList<Long>();
		//List<Integer> taskNumber = new ArrayList<Integer>();
		
//		List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
		
		if(null != intimationNo && !intimationNo.isEmpty()){
			
			mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);			

		
		}
		
		if(null != policyNo && !policyNo.isEmpty()){
						
			mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
			
		}	
		
		if(null != ackNo && !ackNo.isEmpty()){
			mapValues.put(SHAConstants.PAYLOAD_ACK_NUMBER,ackNo);
		}
		

		if(null != cpuCode){			
			mapValues.put(SHAConstants.CPU_CODE, cpuCode);
		}	

		if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
				|| type != null && ! type.isEmpty()){			
			
			if(priority != null && ! priority.isEmpty())
				if(priority.equalsIgnoreCase(SHAConstants.ALL)){
					mapValues.put(SHAConstants.PRIORITY, null);
				}
			mapValues.put(SHAConstants.PRIORITY, priority);
			if(source != null && ! source.isEmpty()){
				mapValues.put(SHAConstants.STAGE_SOURCE, source);	
			}
			
			if(type != null && ! type.isEmpty()){
				if(type.equalsIgnoreCase(SHAConstants.ALL)){
					mapValues.put(SHAConstants.RECORD_TYPE, null);
				}
				mapValues.put(SHAConstants.RECORD_TYPE, type);
			}			

		}
		
		/* Commented as cancel ack is common or PA and health*/
		
//		payloadBOType =SHAUtils.getReimPayloadForHealth(payloadBOType);
		

		
//		CreateRODTask createRODTask = BPMClientContext.getCreateRODTask(userName, passWord);
//		
//		PagedTaskList taskList;
		
		Pageable pageable = searchFormDTO.getPageable();
		if(pageable == null){
			pageable = new Pageable();
		}
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);

//	    taskList = createRODTask.getTasks(userName,pageable,payloadBOType);

		/*if(null != createRODTask){
			List<HumanTask> humanTaskList = taskList.getHumanTasks();
			if(null == humanTaskList | humanTaskList.isEmpty()){
				return null;
			}
			
			for(HumanTask humanTask : humanTaskList){
				PayloadBOType payloadBO = humanTask.getPayload();
				
				if(null != payloadBO.getDocReceiptACK()){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());				
					ackKeyList.add(payloadBO.getDocReceiptACK().getKey());
					taskNumber.add(humanTask.getNumber());
					humanTaskListDTO.add(humanTask);
				}
			}
		}*/
		

		//Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
		
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ACK_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);
						ackKeyList.add(keyValue);
					
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
				
		List<SearchCreateRODTableDTO> tableDTO = new ArrayList<SearchCreateRODTableDTO>();
		for(int index = 0 ; index < intimationNoList.size() ; index++){
			
			 intimationNo = intimationNoList.get(index);
//			 humanTaskDTO = humanTaskListDTO.get(index);
			 if(index < ackKeyList.size()){
			 ackKey = ackKeyList.get(index);
//			 Integer taskNo = taskNumber.get(index);
			 tableDTO.addAll(getIntimationData(intimationNo,  ackKey/*, humanTaskDTO*//*,taskNo,*/,docUpload));
		}
		}
		
		List<SearchCreateRODTableDTO> ackDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
		List<SearchCreateRODTableDTO> nonAckDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
		if(null != docUpload)
		{	
			for (SearchCreateRODTableDTO searchCreateRODTableDTO2 : tableDTO) {
				if((SHAConstants.YES).equalsIgnoreCase(docUpload) && (SHAConstants.YES).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
				{
					ackDocUploadList.add(searchCreateRODTableDTO2);
				}
				if((SHAConstants.No).equalsIgnoreCase(docUpload) && (SHAConstants.No).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
						{
							nonAckDocUploadList.add(searchCreateRODTableDTO2);
						}
			}
		}
		
		Page<SearchCreateRODTableDTO> page = new Page<SearchCreateRODTableDTO>();
		SHAUtils.setClearMapValue(workFlowMap);
		if(null != docUpload && (SHAConstants.YES).equalsIgnoreCase(docUpload))
		{
//			taskList.setTotalRecords(ackDocUploadList.size());
			page.setPageItems(ackDocUploadList);
		}
		else if(null != docUpload && (SHAConstants.No).equalsIgnoreCase(docUpload))
		{
//			taskList.setTotalRecords(nonAckDocUploadList.size());
			page.setPageItems(nonAckDocUploadList);
		}
		else
		{
			page.setPageItems(tableDTO);
		}
//			page.setTotalRecords(taskList.getTotalRecords());
		    page.setTotalRecords(totalRecords);
//			page.setPageNumber(taskList.getCurrentPage());
		return page;
		}
		
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	
	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		if (null != query) {
			docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
		}
		return docAcknowledgement;
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
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
	//	query.setParameter("insuredNumber", insuredNumber);
		List<IncurredClaimRatio> result = (List<IncurredClaimRatio>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}


	public String getColorCodeForGMC(String policyNumber, String insuredNumber){
		IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(policyNumber, insuredNumber);
		if(incurredClaimRatio != null){
			return incurredClaimRatio.getClaimColour();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}	
	
}