package com.shaic.claim.pedrequest.approve.search;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class SearchPEDRequestApproveService  extends  AbstractDAO<OldInitiatePedEndorsement> {
	
	private final Logger log = LoggerFactory.getLogger(SearchPEDRequestApproveService.class);
	
	public SearchPEDRequestApproveService(){
		
	}
	
	public Page<SearchPEDRequestApproveTableDTO> search(SearchPEDRequestApproveFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			List<Map<String, Object>> taskProcedure = null ;

			String strIntimationNo = "";
			String strPolicyNo = "";
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			
			String priorityNew = formDTO.getPriorityNew() != null ? formDTO.getPriorityNew().getValue() != null ? formDTO.getPriorityNew().getValue() : null : null;
			
			Boolean priorityAll = formDTO.getPriorityAll() != null ? formDTO.getPriorityAll() : null;
			Boolean crm = formDTO.getCrm() != null ? formDTO.getCrm() : null;
			Boolean vip = formDTO.getVip() != null ? formDTO.getVip() : null;
			List<String> selectedPriority = formDTO.getSelectedPriority() != null ? formDTO.getSelectedPriority() : null;
			
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String claimTypeValue = formDTO.getClaimType() != null ? formDTO.getClaimType().getValue() != null ? formDTO.getClaimType().getValue(): null : null;
			Date fromDate = formDTO.getFromDate() != null ? formDTO.getFromDate() : null;
			Date toDate = formDTO.getToDate() != null ? formDTO.getToDate() : null;
			
			
			Long pedType = formDTO.getPedSuggestion() != null ? formDTO.getPedSuggestion().getId() != null ? formDTO.getPedSuggestion().getId(): null : null;
			
			Long pedStatusKey = formDTO.getPedStatus() != null ? formDTO.getPedStatus().getId() != null ?  formDTO.getPedStatus().getId() : null : null;
			
			String pedStatus = formDTO.getPedStatus() != null ? formDTO.getPedStatus().getValue() != null ?  formDTO.getPedStatus().getValue() : null : null;
			
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;

		/*	PayloadBOType payloadBO =  new PayloadBOType();

			IntimationType intimationType = new IntimationType();
			
			PolicyType policyType = new PolicyType();
			
			ProductInfoType productInfo = new ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);
			
			payloadBO.setIntimation(intimationType);*/



			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PED_REQUEST_APPROVER_CURRENT_QUEUE);
			
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				
				strIntimationNo = formDTO.getIntimationNo();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				
			}
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equalsIgnoreCase(""))
			{
				
				
				strPolicyNo = formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
			}
			
			
			if(pedType != null){
				
				
				String strPedType = pedType.toString();
				mapValues.put(SHAConstants.PAYLOAD_PED_TYPE, strPedType);

				
			}
			
			
			if(claimTypeValue != null){				

				mapValues.put(SHAConstants.CLAIM_TYPE, claimTypeValue.toUpperCase());
			
			}
			
			if(fromDate != null && toDate != null){
				mapValues.put(SHAConstants.ADMISSION_DATE,new SimpleDateFormat("yyyyMMdd").format(fromDate));
				mapValues.put(SHAConstants.INTIMATION_DATE,new SimpleDateFormat("yyyyMMdd").format(toDate));
			}
			
			if(pedStatusKey != null && pedStatus != null && !pedStatus.isEmpty()){
				mapValues.put(SHAConstants.PROCESS_TYPE,pedStatus);
			}
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
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
		    
		    if(cpuCode != null){
				
				mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				
			}	
		    
		    if(priorityAll !=null && priorityAll){
		    	mapValues.put(SHAConstants.PRIORITY, ReferenceTable.SELECTED_ALL_PRIORITY);
		    }else if(selectedPriority !=null && !selectedPriority.isEmpty()){
		    	 StringBuilder priorit = new StringBuilder();
	    		 selectedPriority.forEach(prio -> priorit.append(prio+" "));
		    	 System.out.println(priorit.toString().trim().replace(" ","|"));
		    	 mapValues.put(SHAConstants.PRIORITY, priorit.toString().trim().replace(" ","|"));
		    	
		    }
		    
			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			List<SearchPEDRequestApproveTableDTO> searchPEDApproveProcessTableDTO = new ArrayList<SearchPEDRequestApproveTableDTO>();
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			log.info("%%%%%%%%%%%%%%%%%%% GET TASK STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			log.info("%%%%%%%%%%%%%%%%%%% GET TASK ENDED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
			//Map to set human task to table DTO.	
			Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
			

			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
					Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_PED_KEY);
					keys.add(keyValue);
					workFlowMap.put(keyValue,outPutArray);
					
					 totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);

				}

			}	
									
				 if(null != keys && 0!= keys.size())
				 {
					List<OldInitiatePedEndorsement> resultList = new ArrayList<OldInitiatePedEndorsement>();
					log.info("%%%%%%%%%%%%%%%%%%% PED QUERY STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PED_SEARCH_SCREEN, entityManager, keys);
					log.info("%%%%%%%%%%%%%%%%%%% PED QUERY ENDED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
					List<OldInitiatePedEndorsement> pageItemList = resultList;
					searchPEDApproveProcessTableDTO = SearchPEDRequestApproveMapper.getInstance()
								.getSearchPEDRequestApproveTableDTO(pageItemList);
					
					for (SearchPEDRequestApproveTableDTO searchDTO : searchPEDApproveProcessTableDTO) {
						if(searchDTO.getPedInitiated() != null){
							searchDTO.setStrPedInitiated(SHAUtils.formatDate(searchDTO.getPedInitiated()));
						}
				 		//Claim claimObject = getClaimByIntimation(searchDTO.getIntimationNo());
						Claim claimObject = getClaimByIntimation(searchDTO.getIntimationNo());
				 		searchDTO.setCrmFlagged(claimObject.getCrcFlag());
						/*if(searchDTO.getCrmFlagged() != null){
							if(searchDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								searchDTO.setColorCodeCell("OLIVE");
								searchDTO.setCrmFlagged(null);
//								searchDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
							}
									
						}*/
				 		
				 		if (claimObject != null) {
							
							if(searchDTO.getCrmFlagged() != null){
								if(searchDTO.getCrmFlagged().equalsIgnoreCase("Y")){
									if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y")) {
										searchDTO.setColorCodeCell("OLIVE");
									}
									searchDTO.setCrmFlagged(null);
								}
							}
							if (claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
								searchDTO.setColorCodeCell("VIP");
							}
							if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y") 
									&& claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
								searchDTO.setColorCodeCell("CRMVIP");
							}
							
						}
						
						if(searchDTO.getKey() != null){
							Object workflowKey = workFlowMap.get(searchDTO.getKey());
							searchDTO.setDbOutArray(workflowKey);
						}
					}
						
						/**
						 * Implementation for hospital information
						 * */
					int iPageItemListSize = pageItemList.size();
					ListIterator<OldInitiatePedEndorsement> iterPED = pageItemList.listIterator();
					List<Long>  hospTypeList = new ArrayList<Long>();		
					while (iterPED.hasNext())
					{
						 OldInitiatePedEndorsement oldIntiatePedEndorsement = iterPED.next();
						 /*MastersValue hospTypeInfo = oldIntiatePedEndorsement.getIntimation().getHospitalType();
						 Long hospitalTypeId = hospTypeInfo.getKey();*/
						 Long hospitalTypeId = oldIntiatePedEndorsement.getIntimation().getHospital();
						 hospTypeList.add(hospitalTypeId);
					}
					
					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
					List<SearchPEDRequestApproveTableDTO> searchPEDApproveProcessTableDTOForHospitalInfoList = new ArrayList<SearchPEDRequestApproveTableDTO>();
					log.info("%%%%%%%%%%%%%%%%%%% HOSP QUERY STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
					log.info("%%%%%%%%%%%%%%%%%%% HOSP QUERY ENDED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ System.currentTimeMillis());
					searchPEDApproveProcessTableDTOForHospitalInfoList = SearchPEDRequestApproveMapper.getHospitalInfoList(resultListForHospitalInfo);
					for (SearchPEDRequestApproveTableDTO objSearchPEDApproveProcessTableDTO : searchPEDApproveProcessTableDTO )
					{
						//Setting human task to table dto.
						//objSearchPEDApproveProcessTableDTO.setHumanTask(humanTaskMap.get(objSearchPEDApproveProcessTableDTO.getKey()));
						objSearchPEDApproveProcessTableDTO.setTaskNumber(taskNumberMap.get(objSearchPEDApproveProcessTableDTO.getKey()));
						for (SearchPEDRequestApproveTableDTO objSearchPEDApproveProcessTableDTOForHospitalInfo : searchPEDApproveProcessTableDTOForHospitalInfoList)
						{
							/**
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
							 * Hospital type. In Hospital.java , we store the key. 
							 * 
							 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
							 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
							 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
							 * */
							if(objSearchPEDApproveProcessTableDTO.getHospitalTypeId() != null && 
									objSearchPEDApproveProcessTableDTOForHospitalInfo.getKey() != null && 
									objSearchPEDApproveProcessTableDTO.getHospitalTypeId().equals(objSearchPEDApproveProcessTableDTOForHospitalInfo.getKey()))
							{
								objSearchPEDApproveProcessTableDTO.setHospitalName(objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalName());
								objSearchPEDApproveProcessTableDTO.setHospitalCity(objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalCity());
								objSearchPEDApproveProcessTableDTO.setHospitalAddress((objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalAddress()));
								break;
							}
						}
						Policy policyByPolicyNumber = getPolicyByPolicyNubember(objSearchPEDApproveProcessTableDTO.getPolicyNo());
						if (policyByPolicyNumber != null){
							Long diffDays = SHAUtils.getDiffDaysWithNegative(new Timestamp(System.currentTimeMillis()), policyByPolicyNumber.getPolicyToDate());
							if (diffDays < 0){
								diffDays= 0l;
							}
							objSearchPEDApproveProcessTableDTO.setRenewalDue(diffDays);
						}
						
						objSearchPEDApproveProcessTableDTO.setSearchPEDRequestApproveFormDTO(formDTO);
					}
				}
				
			 
			Page<OldInitiatePedEndorsement> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchPEDRequestApproveTableDTO> page = new Page<SearchPEDRequestApproveTableDTO>();
			page.setPageItems(searchPEDApproveProcessTableDTO);
			page.setTotalRecords(totalRecords);
			
			return page;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	public Claim getClaimByIntimation(String intimationno) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationno != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationNo");
			findByIntimationKey = findByIntimationKey.setParameter("intimationNumber", intimationno);
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
	
	
	@SuppressWarnings("unchecked")
	public OldInitiatePedEndorsement getOldInitiatePedEndorsementByKey(Long oldInitiatePedEndorsementKey) {
		
		Query findByKey = entityManager.createNamedQuery("HospitalAcknowledge.findAll");

		List<OldInitiatePedEndorsement> oldInitiatePedEndorsementList = (List<OldInitiatePedEndorsement>) findByKey
				.getResultList();

		if (!oldInitiatePedEndorsementList.isEmpty()) {
			return oldInitiatePedEndorsementList.get(0);

		}
		return null;
	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		if(query.getResultList().size()!=0){
		Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}
}
