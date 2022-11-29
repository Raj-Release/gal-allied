package com.shaic.paclaim.rod.enterbilldetails.search;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailFormDTO;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailMapper;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailTableDTO;

@Stateless
public class PASearchEnterBillDetailsService extends AbstractDAO<Intimation>{

	
	
	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public PASearchEnterBillDetailsService() {
		super();
	}

	public  Page<SearchEnterBillDetailTableDTO> search(
			SearchEnterBillDetailFormDTO searchFormDTO,
			String userName, String passWord) {
		
		try{
			String intimationNo = null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String policyNo =  null != searchFormDTO  && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode =  null != searchFormDTO  && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			
			String priority = searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			SelectValue accidentOrDeath = null != searchFormDTO.getAccidentOrdeath() && null!= searchFormDTO.getAccidentOrdeath().getValue() ? searchFormDTO.getAccidentOrdeath(): null;	
			String incidenceType = null;
			Long billClassification =  null != searchFormDTO  && searchFormDTO.getBillClassification() != null ? searchFormDTO.getBillClassification().getId() != null ? searchFormDTO.getBillClassification().getId() : null : null;
			
			if(null != accidentOrDeath)
			{
				String incidenceType1 = accidentOrDeath.getValue();
				char incidenceArray[] = incidenceType1.toCharArray();
				incidenceType = String.valueOf(incidenceArray[0]);
				
			}
			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			workFlowMap= new WeakHashMap<Long, Object>();
			
		Long rodKey = null;
		Long ackKey = null;
	//	HumanTask humanTaskDTO = null;
		List<String> intimationNoList = new ArrayList<String>();
		List<Long> rodKeyList = new ArrayList<Long>();
		List<Long> ackKeyList = new ArrayList<Long>();
	//	List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
		List<Integer> taskNumber = new ArrayList<Integer>();
	//	PayloadBOType payloadBOType = null;
		
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.USER_ID, userName);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.BILL_ENTRY_CURRENT_KEY);
		
		mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		//mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
		
		if(null != intimationNo && !intimationNo.isEmpty()){
			
			mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);

		}
		
	
		if(null != policyNo && !policyNo.isEmpty()){
			
			mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
			
		}		
		
		if(null != cpuCode){
			
			mapValues.put(SHAConstants.CPU_CODE, cpuCode);
		}		
		

		/*if(billClassification != null){
=======
	/*	if(billClassification != null){
>>>>>>> b3d5006b0c515069c16a618af0bd5e8e6edb767e
			if(payloadBOType == null){
				 payloadBOType = new PayloadBOType();
			}
			//DocReceiptACKType docAck = new DocReceiptACKType();
			if(billClassification.equals(8l)){
				docAck.setHospitalization(true);
				docAck.setPartialhospitalization(true);
			}else if(billClassification.equals(9l)){
				docAck.setPrehospitalization(true);
			}else if(billClassification.equals(10l)){
				docAck.setPosthospitalization(true);
			}else if(billClassification.equals(11l)){
				docAck.setLumpsumamount(true);
			}
			payloadBOType.setDocReceiptACK(docAck);
		}*/
		

		if(billClassification != null){			
		
			if(billClassification.equals(8l)){
				mapValues.put(SHAConstants.PAYLOAD_BILLCLASS_HOSP, "Y");
			}else if(billClassification.equals(9l)){
				mapValues.put(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP, "Y");
			}else if(billClassification.equals(10l)){
				mapValues.put(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP, "Y");
			}
		
		}
		
/*		ClassificationType classification = null;
>>>>>>> e19c14c0cc4c75d8e7405ee394cb35c3d66a4fe8*/
		
		if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
				|| type != null && ! type.isEmpty()){			
			
			if(priority != null && ! priority.isEmpty())
				if(!priority.equalsIgnoreCase(SHAConstants.ALL)){				
					mapValues.put(SHAConstants.PRIORITY, priority);
				}
			
		
			if(source != null && ! source.isEmpty()){
				//classification.setSource(source);
				mapValues.put(SHAConstants.STAGE_SOURCE, source);
			}
			
			if(type != null && ! type.isEmpty()){
				if(!type.equalsIgnoreCase(SHAConstants.ALL)){					
					mapValues.put(SHAConstants.RECORD_TYPE, type);
				}
				//classification.setType(type);
				
			}	
			
			if(null != billClassification)
			{
				mapValues.put(SHAConstants.BILL_CLASSIFICATION, type);
			}
		}
	
		/*BillEntryTask createRODTask = BPMClientContext.getBillEntryTask(userName, passWord);
		PagedTaskList taskList; */
		
	    Pageable pageable = searchFormDTO.getPageable();
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
	/*	taskList = createRODTask.getTasks(userName,pageable,payloadBOType);

		if(null != createRODTask){
			List<HumanTask> humanTaskList = taskList.getHumanTasks();
			if(null == humanTaskList | humanTaskList.isEmpty()){
				return null;
			}
			
			for(HumanTask humanTask : humanTaskList){
				PayloadBOType payloadBO = humanTask.getPayload();
				if(null != payloadBO.getRod()){
				intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
				rodKeyList.add(payloadBO.getRod().getKey());
				ackKeyList.add(payloadBO.getDocReceiptACK().getKey());
				taskNumber.add(humanTask.getNumber());
				humanTaskListDTO.add(humanTask);
				}
			}
		}*/
		Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	

		if (null != taskProcedure) {
			for (Map<String, Object> outPutArray : taskProcedure) {							
					Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
					String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
					Long ackKeyValue = (Long)  outPutArray.get(SHAConstants.PAYLOAD_ACK_KEY);
					workFlowMap.put(keyValue,outPutArray);
					intimationNoList.add(intimationNumber);
					rodKeyList.add(keyValue);
					ackKeyList.add(ackKeyValue);
				
				totalRecords = (Integer) outPutArray
						.get(SHAConstants.TOTAL_RECORDS);
			}
		}
			
		List<SearchEnterBillDetailTableDTO> tableDTO = new ArrayList<SearchEnterBillDetailTableDTO>();
		for(int index = 0 ; index < intimationNoList.size() ; index++){
			
			intimationNo = intimationNoList.get(index);
			 
			// humanTaskDTO = humanTaskListDTO.get(index);
			if(index < rodKeyList.size()){
			 rodKey = rodKeyList.get(index);
			 ackKey = ackKeyList.get(index);
			// Integer taskNo = taskNumber.get(index);
			 tableDTO.addAll(getIntimationData(intimationNo, rodKey,/* humanTaskDTO,*/ackKey,/*taskNo,*/userName));
			}
		}

	
		//Page<SearchEnterBillDetailTableDTO> page = new Page<SearchEnterBillDetailTableDTO>(taskList);
		Page<SearchEnterBillDetailTableDTO> page = new Page<SearchEnterBillDetailTableDTO>();
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

	private List<SearchEnterBillDetailTableDTO> getIntimationData(String intimationNo, Long rodKey/*, HumanTask humanTask,*/ ,Long ackKey,/*Integer taskNumber,*/String userName){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		List<Intimation> intimationsList = new ArrayList<Intimation>(); 
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		}
		
		
			for(Intimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Intimation> doList = intimationsList;
			List<SearchEnterBillDetailTableDTO> tableDTO = SearchEnterBillDetailMapper.getInstance().getIntimationDTO(doList);
			
			for (SearchEnterBillDetailTableDTO searchEnterBillDetailTableDTO : tableDTO) {
				
				if(searchEnterBillDetailTableDTO.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(searchEnterBillDetailTableDTO.getProductKey())){
					if(searchEnterBillDetailTableDTO.getPaPatientName() != null){
						searchEnterBillDetailTableDTO.setInsuredPatientName(searchEnterBillDetailTableDTO.getPaPatientName());
					}
				}

				if(searchEnterBillDetailTableDTO.getCpuCode() != null){
					Long cpuCode = searchEnterBillDetailTableDTO.getCpuCode();
					searchEnterBillDetailTableDTO.setStrCpuCode(String.valueOf(cpuCode));
					searchEnterBillDetailTableDTO.setUsername(userName);
					
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getclaimNumber(tableDTO);
			tableDTO = getHospitalDetails(tableDTO, rodKey,/* humanTask*/ackKey/*,taskNumber*/);
		
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
	
	private List<SearchEnterBillDetailTableDTO> getclaimNumber(List<SearchEnterBillDetailTableDTO> tableDTO){
		Claim a_claim = null;
		for(int index = 0; index < tableDTO.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());
			
			if (tableDTO.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", tableDTO.get(index).getKey());
				try{
						a_claim = (Claim) findByIntimationKey.getSingleResult();
						if(a_claim != null){
							tableDTO.get(index).setClaimNo(a_claim.getClaimId());
							tableDTO.get(index).setClaimType(a_claim.getClaimType().getValue());
							tableDTO.get(index).setClaimkey(a_claim.getKey());
						}else{
							tableDTO.get(index).setClaimNo("");
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return tableDTO;
	}


	private List<SearchEnterBillDetailTableDTO> getHospitalDetails(
			List<SearchEnterBillDetailTableDTO> tableDTO,  Long rodKey,/* HumanTask humanTaskListDTO,*/Long ackKey/*,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			tableDTO.get(index).setRodKey(rodKey);
		//	tableDTO.get(index).setHumanTaskListDTO(humanTaskListDTO);
			tableDTO.get(index).setAckKey(ackKey);
			//tableDTO.get(index).setTaskNumber(taskNumber);
			Object workflowKey = workFlowMap.get(tableDTO.get(index).getRodKey());
			
			tableDTO.get(index).setDbOutArray(workflowKey);
			Reimbursement reimb = getReimbursementObjectByKey(rodKey);
			if(null != reimb)
			{
				if(null != reimb.getStage())
					tableDTO.get(index).setSource(reimb.getStage().getStageName());
			}
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 
//				 Long cpuCode = null != getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()) ? getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);

			 }
			 
			 if(ackKey != null){
					DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
					if(null != docAcknowledgement.getClaim().getIncidenceFlag())
					{
					if((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(docAcknowledgement.getClaim().getIncidenceFlag()))
					{
						tableDTO.get(index).setAccidentOrDeath(SHAConstants.ACCIDENT);
					}
					else
					{
						tableDTO.get(index).setAccidentOrDeath(SHAConstants.DEATH);
					}
					}
			 }
			 
			 
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private TmpCPUCode getTmpCPUCodeAndName(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}
	
	
	public Reimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", key);
		
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
		/*
		Reimbursement reimbursement = (Reimbursement) query.getSingleResult();
		entityManager.refresh(reimbursement);
		return reimbursement;*/
	}
	
	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			docAcknowledgement = docAckList.get(0);
		}
		
		return docAcknowledgement;
	}
	
}
