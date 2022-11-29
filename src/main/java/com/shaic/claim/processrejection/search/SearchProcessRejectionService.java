/**
 * 
 */
package com.shaic.claim.processrejection.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
//public class PASearchProcessRejectionService extends AbstractDAO<Preauth> {
public class SearchProcessRejectionService extends AbstractDAO<Intimation> {

	public SearchProcessRejectionService() {
		super();
	}

	@EJB
	private ClaimService claimService;
	
	@SuppressWarnings("unchecked")
	public Page<SearchProcessRejectionTableDTO> search(
			SearchProcessRejectionFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = "";
			
			String strPolicyNo = "";
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;						
			String priorityNew = formDTO.getPriorityNew() != null ? formDTO.getPriorityNew().getValue() != null ? formDTO.getPriorityNew().getValue() : null : null;
			
			List<Map<String, Object>> taskParamObjList = null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
						
//			PayloadBOType payloadBO = null;
//			IntimationType intimationType = new IntimationType();
//			
//			PolicyType policyType = new PolicyType();
			
			
			/*ProductInfoType productInfo = new ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);
			
			payloadBO.setIntimation(intimationType);*/
			
			
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);

			}
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equalsIgnoreCase(""))
			{				

				strPolicyNo = formDTO.getPolicyNo();				
				mapValues.put(SHAConstants.POLICY_NUMBER,strPolicyNo);

			}
			
		
		    if((priority != null && ! priority.isEmpty()) || (source != null && ! source.isEmpty())
						|| (type != null && ! type.isEmpty())){

					
					if(priority != null && ! priority.isEmpty() && !(SHAConstants.ALL).equalsIgnoreCase(priority)){	
						mapValues.put(SHAConstants.PRIORITY,priority);
					}	
					if(source != null && ! source.isEmpty()){				
						mapValues.put(SHAConstants.STAGE_SOURCE,source);
					}					

					if(type != null && ! type.isEmpty() && !(SHAConstants.ALL).equalsIgnoreCase(type)){

						mapValues.put(SHAConstants.RECORD_TYPE, type);

					}

			}
		    
		    if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
				if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
				}else{
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				}
			
			
			
			/*RejectionQF regQF = null;
			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo))))
			{
				
				regQF = new RejectionQF();
				regQF.setIntimationNumber(strIntimationNo);
				regQF.setPolicyId(strPolicyNo);
			}
			*/
			
		
			
			ImsUser imsUser = formDTO.getImsUser();
			
			String[] userRoleList = imsUser.getUserRoleList();
			
			
			
			List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForPreAuth = new ArrayList<SearchProcessRejectionTableDTO>();
			List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<SearchProcessRejectionTableDTO>();
			Page<SearchProcessRejectionTableDTO> page = new Page<SearchProcessRejectionTableDTO>();

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			//List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<SearchProcessRejectionTableDTO>();
//			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = null;
//			if(Arrays.asList(userRoleList).contains(SHAConstants.REJECTIONAPPROVER)){
//			com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask processRejectionTask = BPMClientContext.getProcessRejectionTask(userName,passWord);
////			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = processRejectionTask.getTasks(userName, pageable, payloadBO);
//			tasks = processRejectionTask.getTasks(userName, pageable, payloadBO);
//			}
			
			
//			PagedTaskList nonMedicalTask = null;
//			if(Arrays.asList(userRoleList).contains(SHAConstants.NONMEDICALAPPROVER)){
//				
//				 List<HumanTask> humanTasks = tasks != null ? tasks.getHumanTasks() : null;
//				 Integer endPage = humanTasks != null ? humanTasks.size() : 0;
//				 Integer pageSize = (BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10)- endPage;
//				 pageable.setPageSize(pageSize);
//				
//				ProcessRejectionNonMedicalTask processRejectionNonMedicalTask = BPMClientContext.getProcessRejectionNonMedicalTask(userName, passWord);
//				nonMedicalTask = processRejectionNonMedicalTask.getTasks(userName, pageable, payloadBO);
//			}		
			
			
			/**
			 * "rejectionapprover1" is the user used to fetch task which are rejected at pre auth level.
			 * */
			/*PagedTaskList tasks=null;
			if(userName.equals("rejectionapprover1")){
			
			tasks = processRejectionTask.getTasks(userName,passWord, pageable, regQF);
			}*/
			
			/**
			 * "nonmedicalapprover1" is the user used to fetch task which are rejected at pre auth level.
			 * */
			/*
			else if(userName.equals("nonmedicalapprover1")){
			 tasks = processRejectionTask.getTasks(userName,passWord, pageable, regQF);
			}*/
			
			//Map to set human task to table DTO.
			//Map<Long, HumanTask> humanTaskMapForPreauth = new HashMap<Long, HumanTask>();
//			Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//			Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
//			
//			List<HumanTask> humanTasks = null;
//			if(tasks != null){
//				humanTasks = tasks.getHumanTasks();
//			}
//			
//			if(nonMedicalTask != null){
//				
//				if(humanTasks != null){
//					humanTasks.addAll(nonMedicalTask.getHumanTasks());
//				}else{
//					humanTasks = nonMedicalTask.getHumanTasks();
//				}
//			}
//
//			if(null != humanTasks)
//			{
//				 
//				List<Long> preauthKeys = new ArrayList<Long>();
//				List<Long> intimationKeys = new ArrayList<Long>();
//			
//				for (HumanTask item: humanTasks)
//				{
//					PayloadBOType payloadBOCashless = item.getPayloadCashless();
//					
//					if(null != payloadBOCashless)
//					{
//						
//						IntimationType intimationTypeObj = payloadBOCashless.getIntimation();
//						if(null != intimationTypeObj)
//						{
//							Long regIntKeyValue = intimationTypeObj.getKey();
//							humanTaskMap.put(regIntKeyValue, item);
//							taskNumberMap.put(regIntKeyValue, item.getNumber());
//							intimationKeys.add(regIntKeyValue);
//						}
//						/**
//						 * Reg int details need to get from joseph.
//						 * */
//					}
//					
//					/*BPMClientContext.printPayloadElement(item.getPayload());
//					Map<String, String> intmiatonValuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
//					Long regIntKeyValue = Long.valueOf(intmiatonValuesFromBPM.get("key"));
//					if(null != regIntKeyValue && 0 < regIntKeyValue)
//					{*/
//						//humanTaskMap.put(regIntKeyValue, item);
//						//intimationKeys.add(regIntKeyValue);		
//						//Map<String, String> preAuthValuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
//					PreAuthReqType preauthReqType = payloadBOCashless.getPreAuthReq();
//					if(null != preauthReqType)
//					{
//						//Long preAuthkeyValue = Long.valueOf(preAuthValuesFromBPM.get("key"));
//						Long preAuthkeyValue = preauthReqType.getKey();
//						if(null != preAuthkeyValue && 0 < preAuthkeyValue)
//						{
//							preauthKeys.add(preAuthkeyValue);		
//						}
//					}
//					//}
//				}
				
//			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//			
//			DBCalculationService dbCalculationService = new DBCalculationService();
//			taskParamObjList = dbCalculationService.getTaskProcedure(setMapValues);
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskParamObjList = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			List<Long> preauthKeys = new ArrayList<Long>();
			List<Long> intimationKeys = new ArrayList<Long>();
			Integer totalRecords = 0;
			if (null != taskParamObjList) {
				for (Map<String, Object> outPutArray : taskParamObjList) {
					Long preauthKeyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
					Long inimationKeyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
					
					if(preauthKeyValue != null){
						preauthKeys.add(preauthKeyValue);
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}					
					}
					else{
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}
					}
						
						
					workFlowMap.put(inimationKeyValue,outPutArray);
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			
			
				//intimationKeys.
				if(null != preauthKeys && 0!= preauthKeys.size())
				{
					List<Preauth> resultList = new ArrayList<Preauth>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, preauthKeys);
					List<Preauth> pageItemList = resultList;
					searchProcessRejectionTableDTOForIntimation = SearchProcessRejectionMapper.getInstance()
						.getSearchProcessRejectionTableDTOForPreauth(pageItemList);
					
					List<Claim> claimedList = new ArrayList<Claim>();
					claimedList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, preauthKeys);
					List<Claim> pageClaimedList = claimedList;
					List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForClaim = SearchProcessRejectionMapper.getSearchProcessRejectionTableDTOForClaim(pageClaimedList);
					searchProcessRejectionTableDTOForIntimation.addAll(searchProcessRejectionTableDTOForClaim);
					
					
				} if (null != intimationKeys && 0!= intimationKeys.size()) 
			 	{
					List<Intimation> resultList = new ArrayList<Intimation>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.INTIMATION_SEARCH, entityManager, intimationKeys);
					List<Intimation> pageItemList = resultList;
					searchProcessRejectionTableDTOForIntimation = SearchProcessRejectionMapper
						.getSearchProcessRejectionTableDTOForIntimation(pageItemList);
					
			 	}
				
					for (SearchProcessRejectionTableDTO objSearchProcessRejectionTableDTO :searchProcessRejectionTableDTOForIntimation)
					{
						if(objSearchProcessRejectionTableDTO.getIntimationNo() != null){
							Intimation intimationByNo = getIntimationByNo(objSearchProcessRejectionTableDTO.getIntimationNo());
							if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
								String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
								objSearchProcessRejectionTableDTO.setColorCode(colorCodeForGMC);
							}
						}
						
						List<Claim> claimObject = claimService.getClaimByIntimation(objSearchProcessRejectionTableDTO.getKey());
						objSearchProcessRejectionTableDTO.setCrmFlagged(claimObject.get(0).getCrcFlag());
						if(objSearchProcessRejectionTableDTO.getCrmFlagged() != null){
							if(objSearchProcessRejectionTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								objSearchProcessRejectionTableDTO.setColorCodeCell("OLIVE");
								objSearchProcessRejectionTableDTO.setCrmFlagged(null);
								//objSearchProcessPreAuthTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
							}
						}
						
						
						objSearchProcessRejectionTableDTO.setDbOutArray(workFlowMap.get(objSearchProcessRejectionTableDTO.getKey()));
						if(objSearchProcessRejectionTableDTO.getIntimationDate() != null){
							String formatDate = SHAUtils.formatDate(objSearchProcessRejectionTableDTO.getIntimationDate());
							objSearchProcessRejectionTableDTO.setStrIntimationDate(formatDate);
						}
//						objSearchProcessRejectionTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessRejectionTableDTO.getKey()));
//						objSearchProcessRejectionTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessRejectionTableDTO.getKey()));

						if((null != searchProcessRejectionTableDTOForIntimation && 0 != searchProcessRejectionTableDTOForIntimation.size()))
						{
							for(SearchProcessRejectionTableDTO objProcessRejectionForPreauthTableDTO :searchProcessRejectionTableDTOForIntimation)
							{
								if((objProcessRejectionForPreauthTableDTO.getIntimationNo().equalsIgnoreCase(objSearchProcessRejectionTableDTO.getIntimationNo())))
								{
									objSearchProcessRejectionTableDTO.setPreauthStatus(objProcessRejectionForPreauthTableDTO.getPreauthStatus());
								}
							}
						}
					}
					
		   // Page<Intimation> pagedList = super.pagedList(formDTO.getPageable());
			page.setPageItems(searchProcessRejectionTableDTOForIntimation);
			page.setTotalRecords(totalRecords.intValue());
			
//			if(tasks != null && tasks.getIsNextPageAvailable() || nonMedicalTask != null && nonMedicalTask.getIsNextPageAvailable()){
//				page.setHasNext(true);
//			}
			
			if(formDTO.getPageable() != null){
			page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
			}
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Preauth getProcessRejectionKey(Long processRejectionKey) {
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Preauth> processRejectionList = (List<Preauth>) findByKey
				.getResultList();

		if (!processRejectionList.isEmpty()) {
			return processRejectionList.get(0);

		}
		return null;
	}

/*	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}
*/
	
	@Override
	public Class<Intimation> getDTOClass() {
		return Intimation.class;
	}
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		//query.setParameter("insuredNumber", insuredNumber);
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
