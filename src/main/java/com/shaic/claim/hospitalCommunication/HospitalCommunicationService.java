package com.shaic.claim.hospitalCommunication;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationFormDTO;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.domain.MastersValue;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;


@Stateless
public class HospitalCommunicationService extends  AbstractDAO<Preauth>{
	
	
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
	
	public HospitalCommunicationService() {
		super();
	}
	
	public Page<SearchAcknowledgeHospitalCommunicationTableDTO> search(SearchAcknowledgeHospitalCommunicationFormDTO formDTO, String userName, String passWord)
	{/*
		
		String strIntimationNo =  "";//formDTO.getIntimationNumber();
		String strPolicyNo = "";//formDTO.getPolicyNumber();
		SelectValue cpuCode = formDTO.getCpuCode();
		String strCpuCode = "";
		
		String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
		String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
		String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
		
		PayloadBOType payloadBO = new PayloadBOType();
		
		IntimationType intimationType = new IntimationType();
		
		PolicyType policyType = new PolicyType();
		
		ClaimRequestType claimRequestType = new ClaimRequestType();
		
		ProductInfoType productInfo = new ProductInfoType();
		
		
		intimationType.setReason("HEALTH");
		
		productInfo.setLob("H");

		
		payloadBO.setProductInfo(productInfo);
		
		payloadBO.setIntimation(intimationType);
		
		
		if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equals(""))
		{
			
			strIntimationNo = formDTO.getIntimationNo();
			intimationType.setIntimationNumber(strIntimationNo);
			payloadBO.setIntimation(intimationType);
		}
		
		
		if(null != formDTO.getPolicyNo() && ! formDTO.getPolicyNo().equals(""))
		{
			strCpuCode = formDTO.getPolicyNo();
			policyType.setPolicyId(strCpuCode);
			payloadBO.setPolicy(policyType);
			
		}
		if(null != cpuCode)
		{
		  strCpuCode = String.valueOf(cpuCode.getId());
		  claimRequestType.setCpuCode(strCpuCode);
		  payloadBO.setClaimRequest(claimRequestType);
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
				
				
				 payloadBO.setClassification(classification);
		}

		
		if(null != cpuCode)
		{
			strCpuCode = String.valueOf(cpuCode.getId());
			
		}
		
		AckHospitalCommunicationQF  ackHospQF = null;			
		if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strCpuCode || ("").equals(strCpuCode))))
		{
			ackHospQF = new AckHospitalCommunicationQF();
			ackHospQF.setIntimationNumber(strIntimationNo);
			ackHospQF.setPolicyId(strPolicyNo);
		}
		
		Pageable pageable = formDTO.getPageable();
		
		pageable.setPageNumber(1);
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		List<SearchAcknowledgeHospitalCommunicationTableDTO> searchAcknowledgeHospitalCommunicationTableDTO  = new ArrayList<SearchAcknowledgeHospitalCommunicationTableDTO>();
		com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationTask  processConvTask = BPMClientContext.getAckHospitalCommunicationTask(userName,passWord);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =  processConvTask.getTasks(userName, pageable, payloadBO);  //user Name="zonaluser1"
		
		AckHospitalCommunicationCLTask ackHositpitalCommunicationForPreauthQuery = BPMClientContext.getAckHositpitalCommunicationForPreauthQuery(userName, passWord);
		
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks2 = null;
		
		if(tasks != null){
			
            List<HumanTask> taskList = tasks.getHumanTasks();
			
			if(taskList != null && taskList.size() <= Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE)){
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) - taskList.size() : 10);
				
			}
			
			tasks2 = ackHositpitalCommunicationForPreauthQuery.getTasks(userName, pageable, payloadBO);
		}
		
		//Map to set human task to table DTO.
		Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
		Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
		
		if(null != tasks)
		{
			List<HumanTask> humanTasks = tasks.getHumanTasks();
			
			if(tasks2 != null){
				List<HumanTask> humanTasks2 = tasks2.getHumanTasks();
				humanTasks.addAll(humanTasks2);
			}
			
			List<Long> keys = new ArrayList<Long>();  
			
			for (HumanTask item: humanTasks)
		    {
				PayloadBOType payload = item.getPayloadCashless();
				if(null != payload)
				{
					PreAuthReqType preauthReqType = payload.getPreAuthReq();
					if(null != preauthReqType)
					{
						Long keyValue = preauthReqType.getKey();//Long.valueOf(valuesFromBPM.get("key"));
						keys.add(keyValue);	
						humanTaskMap.put(keyValue, item);
						taskNumberMap.put(keyValue, item.getNumber());
					}
				}
				
				
				//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
				
				if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
				{
					Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
					keys.add(keyValue);	
					humanTaskMap.put(keyValue, item);
				}
		    }

			if(null != keys && 0!= keys.size())
			{
				List<Preauth> resultList = new ArrayList<Preauth>();
				resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
				List<Preauth> pageItemList = resultList;
				searchAcknowledgeHospitalCommunicationTableDTO = SearchAcknowledgeHospitalCommunicationMapper.getInstance().getSearchAcknowledgeHospitalCommunicationTableDTO(pageItemList);
				for (SearchAcknowledgeHospitalCommunicationTableDTO searchDTO : searchAcknowledgeHospitalCommunicationTableDTO) {
					if(searchDTO.getRemarks() != null && searchDTO.getRemarks().length()>100){
						searchDTO.setRemarks(searchDTO.getRemarks().substring(0,99));
						
					}
				}
				ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
				List<Long>  hospTypeList = new ArrayList<Long>();
				
				while (iterPreAuth.hasNext())
				{
					 Preauth preAuth= iterPreAuth.next();
					 Long hospitalTypeId = preAuth.getIntimation().getHospital();
					 hospTypeList.add(hospitalTypeId);
				}
				
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<SearchAcknowledgeHospitalCommunicationTableDTO> searchAckHospitalCommunicationTableDTOForHospInfo = new ArrayList<SearchAcknowledgeHospitalCommunicationTableDTO>();				
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				searchAckHospitalCommunicationTableDTOForHospInfo = SearchAcknowledgeHospitalCommunicationMapper.getHospitalInfoList(resultListForHospitalInfo);
				
				for (SearchAcknowledgeHospitalCommunicationTableDTO objSearchAckHospCommTableDTO : searchAcknowledgeHospitalCommunicationTableDTO )
				{
					
					if(null != objSearchAckHospCommTableDTO.getLob() && !("").equals(objSearchAckHospCommTableDTO.getLob()))
					{
						objSearchAckHospCommTableDTO.setLob(loadLobValue(Long.parseLong(objSearchAckHospCommTableDTO.getLob())));
					}
					objSearchAckHospCommTableDTO.setHumanTask(humanTaskMap.get(objSearchAckHospCommTableDTO.getKey()));
					objSearchAckHospCommTableDTO.setTaskNumber(taskNumberMap.get(objSearchAckHospCommTableDTO.getKey()));
					
					for (SearchAcknowledgeHospitalCommunicationTableDTO objSearchAckHospCommTableDTOForHospitalInfo : searchAckHospitalCommunicationTableDTOForHospInfo)
					{
						*//**
						 * objSearchProcessPreAuthTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
						 * objSearchProcessPreAuthTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
						 * Hospital type. In Hospital.java , we store the key. 
						 * 
						 * But this key will come from intimation table hospital type id. objSearchProcessPreAuthTableDTO is of 
						 * SearchPreauthTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
						 * That is why we equate hospitalTypeId from SearchPreauthTableDTO with key from HospitalDTO.
						 * *//*
						if(objSearchAckHospCommTableDTO.getHospitalTypeId() == objSearchAckHospCommTableDTOForHospitalInfo.getKey())
						{
							objSearchAckHospCommTableDTO.setHospitalName(objSearchAckHospCommTableDTOForHospitalInfo.getHospitalName());
							break;
						}
					}
				}
			}
		}
		
		Page<SearchAcknowledgeHospitalCommunicationTableDTO> page = new Page<SearchAcknowledgeHospitalCommunicationTableDTO>();
//		Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(searchAcknowledgeHospitalCommunicationTableDTO);
		page.setPageNumber(tasks.getCurrentPage());
		page.setHasNext(tasks.getIsNextPageAvailable());
		page.setTotalRecords(tasks.getTotalRecords());
		//page.setPagesAvailable(pageCount);
		//page.setPagesAvailable(pagedList.getPagesAvailable());
		return page;
		//return null;

	*/
		return null;
		}
	
	@SuppressWarnings({ "unchecked", "unused"})
	public HospitalAcknowledge getHospitalAcknowledgementByKey(Long acknowledHospitalKey) {

//		Query findByKey = entityManager
//				.createNamedQuery("HospitalAcknowledge.findByHospitalKey").setParameter(
//						"hospitalKey", acknowledHospitalKey);
		
		Query findByKey = entityManager.createNamedQuery("HospitalAcknowledge.findAll");

		List<HospitalAcknowledge> hospitalList = (List<HospitalAcknowledge>) findByKey
				.getResultList();

		if (!hospitalList.isEmpty()) {
			return hospitalList.get(0);

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

}
