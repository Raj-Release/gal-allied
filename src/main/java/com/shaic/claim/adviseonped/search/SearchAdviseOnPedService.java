package com.shaic.claim.adviseonped.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class SearchAdviseOnPedService  extends  AbstractDAO<OldInitiatePedEndorsement>  {
	
	public SearchAdviseOnPedService(){
		
	}
	
	public Page<SearchAdviseOnPEDTableDTO> search(SearchAdviseOnPEDFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			
			String strIntimationNo = "";
			String strPolicyNo = "";
			List<Map<String, Object>> taskProcedure = null ;

			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			
			Long pedType = formDTO.getPedSuggestion() != null ? formDTO.getPedSuggestion().getId() != null ? formDTO.getPedSuggestion().getId(): null : null;


			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
					

			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.ADVISE_ON_PED_CURRENT_Q);

			/*PolicyType policyType = new PolicyType();
			
			ProductInfoType productInfo = new ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);
			
			payloadBO.setIntimation(intimationType);*/


			
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				
				strIntimationNo = formDTO.getIntimationNo();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				/*intimationType.setIntimationNumber(strIntimationNo);
				payloadBO.setIntimation(intimationType);*/
			}
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equalsIgnoreCase(""))
			{
				
				strPolicyNo = formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
				/*policyType.setPolicyId(strPolicyNo);
				
				payloadBO.setPolicy(policyType);*/
				
			}			
			
			if(pedType != null){
				
				String strPedType = pedType.toString();
				mapValues.put(SHAConstants.PED_TYPE, strPedType);
				/*pedReqTypePayload.setStatus(strPedType);
				payloadBO.setPedReq(pedReqTypePayload);*/
				
			}
			
			
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					//classification.setPriority(priority);
					mapValues.put(SHAConstants.PRIORITY,priority);
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE,source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
						}
						//classification.setType(type);
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}

					
					
					 //payloadBO.setClassification(classification);
			}
			
			
		/*	String strIntimationNo = formDTO.getIntimationNo();
			String strPolicyNo = formDTO.getPolicyNo();
		
			
			PedQF pedQF = null;
			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo))))
			{
				pedQF = new PedQF();
				pedQF.setIntimationNumber(strIntimationNo);
				pedQF.setPolicyId(strPolicyNo);
			}*/
					
			//Pageable pageable = formDTO.getPageable();
			//pageable = null;
		    
		    Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			
			List<SearchAdviseOnPEDTableDTO> searchAdviseOnPEDTableDTO = new ArrayList<SearchAdviseOnPEDTableDTO>();
			

			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			
			/*com.shaic.ims.bpm.claim.servicev2.ped.search.AdviseOnPedTask adviseOnPedTask = BPMClientContext.getAdviseOnPedTask(userName, passWord);          //user name and password
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = adviseOnPedTask.getTasks(userName, pageable, payloadBO);*/    //userName="specialist1"
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
				//keys.add(33l);
				//keys.add(9l);
				
				if(null != keys && 0!= keys.size())
				{
					List<OldInitiatePedEndorsement> resultList = new ArrayList<OldInitiatePedEndorsement>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PED_SEARCH_SCREEN, entityManager, keys);
					List<OldInitiatePedEndorsement> pageItemList = resultList;
					searchAdviseOnPEDTableDTO = SearchAdviseOnPEDMapper.getInstance()
							.getSearchAdviseOnPEDTableDTO(pageItemList);
					/**
					 * Implementation for hospital information
					 * */
				//	int iPageItemListSize = pageItemList.size();
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
					List<SearchAdviseOnPEDTableDTO> searchAdviseOnPEDTableDTOForHospitalInfoList = new ArrayList<SearchAdviseOnPEDTableDTO>();				
					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
					searchAdviseOnPEDTableDTOForHospitalInfoList = SearchAdviseOnPEDMapper.getHospitalInfoList(resultListForHospitalInfo);
					for (SearchAdviseOnPEDTableDTO objSearchAdviseOnPEDTableDTO : searchAdviseOnPEDTableDTO )
					{
						
						
						if(null != objSearchAdviseOnPEDTableDTO.getKey()){
							Object workflowKey = workFlowMap.get(objSearchAdviseOnPEDTableDTO.getKey());
							objSearchAdviseOnPEDTableDTO.setDbOutArray(workflowKey);
						}
						//Assigning human task to table DTO.
						//objSearchAdviseOnPEDTableDTO.setHumanTask(humanTaskMap.get(objSearchAdviseOnPEDTableDTO.getKey()));
						objSearchAdviseOnPEDTableDTO.setTaskNumber(taskNumberMap.get(objSearchAdviseOnPEDTableDTO.getKey()));
						for (SearchAdviseOnPEDTableDTO objSearchAdviseOnPEDTableDTOForHospitalInfo : searchAdviseOnPEDTableDTOForHospitalInfoList)
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
							if(objSearchAdviseOnPEDTableDTO.getHospitalTypeId() != null &&
									objSearchAdviseOnPEDTableDTOForHospitalInfo.getKey() != null && objSearchAdviseOnPEDTableDTO.getHospitalTypeId().equals(objSearchAdviseOnPEDTableDTOForHospitalInfo.getKey()))
							{
								objSearchAdviseOnPEDTableDTO.setHospitalName(objSearchAdviseOnPEDTableDTOForHospitalInfo.getHospitalName());
								objSearchAdviseOnPEDTableDTO.setHospitalCity(objSearchAdviseOnPEDTableDTOForHospitalInfo.getHospitalCity());
								objSearchAdviseOnPEDTableDTO.setHospitalAddress(objSearchAdviseOnPEDTableDTOForHospitalInfo.getHospitalAddress());
								break;
							}
						}
					}	
				}
			//}
		//	Page<OldInitiatePedEndorsement> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchAdviseOnPEDTableDTO> page = new Page<SearchAdviseOnPEDTableDTO>();
			//page.setPageNumber(pagedList.getPageNumber());
			page.setPageItems(searchAdviseOnPEDTableDTO);
			page.setTotalRecords(totalRecords);
			//page.setPagesAvailable(pageCount);
			//page.setPagesAvailable(pagedList.getPagesAvailable());
			return page;
			//return null;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
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

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}
}
