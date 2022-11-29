package com.shaic.paclaim.cashless.flp.search;

import java.util.ArrayList;
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
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class PAProcessPreMedicalService  extends AbstractDAO <Claim>  {
	
	
	private final Logger log = LoggerFactory.getLogger(PAProcessPreMedicalService.class);
	
		public PAProcessPreMedicalService()
		{
			super();
		}	
		
		public Page<PAProcessPreMedicalTableDTO> search(PAProcessPreMedicalFormDTO formDTO, String userName, String passWord)
		{
			try 
			{
//				PreAuthQF preAuthQF = null;
				String strIntimationNo = "";
				String strPolicyNo = "";
				String strType = null;
				List<Map<String, Object>> taskProcedure = null ;
				
				String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
				String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
				String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
				String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
				
				if (formDTO.getSource() != null && formDTO.getSource().getId() != null && formDTO.getSource().getId().equals(ReferenceTable.REFER_TO_FLP)){
					source = "FLP";
				}				
			
				
				List<Long> keys = new ArrayList<Long>(); 			
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				mapValues.put(SHAConstants.USER_ID,userName);
				
				mapValues.put(SHAConstants.CURRENT_Q, "FLPA");
				
				/*mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
				mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);*/
							
				mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
				//mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
				
//				intimationType.setReason("HEALTH");
				
			/*	productInfo.setLobType("H");
				
				productInfo.setLob("HEALTH");
				
				payloadBO.setProductInfo(productInfo);
				
				payloadBO.setIntimation(intimationType);
				*/
				
				if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equals(""))
				{
					strIntimationNo = formDTO.getIntimationNo();					
					mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);				
//					
				}
				if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equals(""))
				{
					strPolicyNo= formDTO.getPolicyNo();					
				}
				String strIntimationSource = null;
				if(null != formDTO.getIntimationSource())
				{
					strIntimationSource = formDTO.getIntimationSource().getValue();
					//intimationType.setIntimationSource(strIntimationSource);
				}
				String strNetworkHospType = null;
				if(null != formDTO.getNetworkHospType())
				{
//					strNetworkHospType = formDTO.getNetworkHospType().getValue();
//					hospitalTypeInfo.setNetworkHospitalType(strNetworkHospType);
					
					if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.NETWORK_HOSPITAL)){
						//hospitalTypeInfo.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
					}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
						//hospitalTypeInfo.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
					}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
						//hospitalTypeInfo.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
					}
					
					//payloadBO.setHospitalInfo(hospitalTypeInfo);
				}
				
				//ClaimRequestType claimRequestType = new ClaimRequestType();
				if(cpuCode != null){
					//claimRequestType.setCpuCode(cpuCode);
					//payloadBO.setClaimRequest(claimRequestType);
				}
				
			
				
				//ClassificationType classification = null;
				
			    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
						//classification = new ClassificationType();
						
						if(priority != null && ! priority.isEmpty())
							if(priority.equalsIgnoreCase(SHAConstants.ALL)){
								priority = null;
							}
						//classification.setPriority(priority);
						if(source != null && ! source.isEmpty()){
							//classification.setSource(source);
						}
						
						if(type != null && ! type.isEmpty()){
							if(type.equalsIgnoreCase(SHAConstants.ALL)){
								type = null;
							}
							//classification.setType(type);
						}
						
						// payloadBO.setClassification(classification);
				}
			    
			   

//				/**
//				 * BPM integration starts.
//				 * 
//				 * */
//
//				/*if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strIntimationSource || ("").equals(strIntimationSource))
//						&& (null == strType || ("").equals(strType)) && (null == strNetworkHospType || ("").equals(strNetworkHospType))))
//				{
//					preAuthQF = new PreAuthQF();	
//					preAuthQF.setIntimationNumber((null != strIntimationNo && !("").equalsIgnoreCase(strIntimationNo))? strIntimationNo :null);
//					preAuthQF.setPolicyId((null != strPolicyNo && !("").equalsIgnoreCase(strPolicyNo)) ? strPolicyNo :null);
//					preAuthQF.setIntimationSource(strIntimationSource);
//					preAuthQF.setType(strType);
//					preAuthQF.setNetworkHospitalType(strNetworkHospType);
//				}*/
//				
//				//Pageable pageable = formDTO.getPageable();
//				//pageable = null;
// 				Pageable pageable = formDTO.getPageable();
//				
//				pageable.setPageNumber(1);
//				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
//				
//				if(formDTO.getSortOrder() != null && formDTO.getSortOrder().getValue() != null){
//				    	pageable.setOrderBy(formDTO.getSortOrder().getValue());
//				 }	
//

				/**
				 * BPM integration starts.
				 * 
				 * */

				/*if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strIntimationSource || ("").equals(strIntimationSource))
						&& (null == strType || ("").equals(strType)) && (null == strNetworkHospType || ("").equals(strNetworkHospType))))
				{
					preAuthQF = new PreAuthQF();	
					preAuthQF.setIntimationNumber((null != strIntimationNo && !("").equalsIgnoreCase(strIntimationNo))? strIntimationNo :null);
					preAuthQF.setPolicyId((null != strPolicyNo && !("").equalsIgnoreCase(strPolicyNo)) ? strPolicyNo :null);
					preAuthQF.setIntimationSource(strIntimationSource);
					preAuthQF.setType(strType);
					preAuthQF.setNetworkHospitalType(strNetworkHospType);
				}*/
				
				//Pageable pageable = formDTO.getPageable();
				//pageable = null;
			    
			    
			 //   payloadBO = SHAUtils.getCashlessPayloadForHealth(payloadBO);
			    
			    
 				Pageable pageable = formDTO.getPageable();
				
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
				
				if(formDTO.getSortOrder() != null && formDTO.getSortOrder().getValue() != null){
				    	pageable.setOrderBy(formDTO.getSortOrder().getValue());
				 }	

				Integer totalRecords = 0; 
				List<PAProcessPreMedicalTableDTO> processPreMedicalTableDTO = new ArrayList<PAProcessPreMedicalTableDTO>();
//				com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask preMedicalClaimTask = BPMClientContext.getProcessPreMedicalTask(userName,passWord);
//				
//				Date startDate = new Date();
//				
//				log.info("%%%%%%%%%%%%%%%%%% STARTING TIME FOR GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+startDate);
//				
//				com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =  preMedicalClaimTask.getTasks(userName, pageable,  payloadBO);  //userName="premedicalprocessor1"
//				
//				Date endDate = new Date();
//				
//				log.info("%%%%%%%%%%%%%%%%%% ENDING TIME FOR GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+startDate);
//				
//				log.info("%%%%%%%%%%%%%%%%%% TOTAL TIME FOR GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(startDate, endDate));
//				
//				//Map to set human task to table DTO.
//				Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//				Map<Long, Integer>	taskNumberMap = new HashMap<Long, Integer>();
				
//				if(null != tasks)
				Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
				
			    if(null != taskProcedure)
				{
//					List<com.shaic.ims.bpm.claim.modelv2.HumanTask> humanTasksList = tasks.getHumanTasks();
//					 
//					for (com.shaic.ims.bpm.claim.modelv2.HumanTask item: humanTasksList)
//				    {
//						PayloadBOType payloadBOCashless = item.getPayloadCashless();
//						PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq();
//						String payloadCpuCode = null;
//						if(payloadBOCashless.getClaimRequest() != null && payloadBOCashless.getClaimRequest().getCpuCode() !=  null) {
//							payloadCpuCode = payloadBOCashless.getClaimRequest().getCpuCode();
//						}
//						//ClaimType claimType = payloadBOCashless.getClaim();
//						if(null != preauthReq)
//						{
			    	for (Map<String, Object> outPutArray : taskProcedure) {
						
					
			    		Object object = outPutArray.get(SHAConstants.DB_CLAIM_KEY);
			    		
							Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
							keys.add(keyValue);
							workFlowMap.put(keyValue,outPutArray);
							
							 totalRecords = (Integer) outPutArray
									.get(SHAConstants.TOTAL_RECORDS);
//							humanTaskMap.put(keyValue, item);
//							taskNumberMap.put(keyValue,item.getNumber());
							
//							if(cpuCode != null) {
//								String[] split = cpuCode.split("-");
//								String cpuCodeStr = split[0];
//								if(cpuCodeStr != null) {
//									cpuCodeStr = cpuCodeStr.replaceAll("\\s","");
//								}
//								if(payloadCpuCode != null && cpuCodeStr != null && payloadCpuCode.equalsIgnoreCase(cpuCodeStr)) {
//									keys.add(keyValue);
//								}
//							} else {
								keys.add(Long.valueOf(keyValue));
//							}
							
						}
						/*if(null != claimType)
						{
							Long keyValue = claimType.getKey();
							humanTaskMap.put(keyValue, item);
							keys.add(keyValue);
						}*/
						
						//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
						//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
						//System.out.println("--the map----"+valuesFromBPM);
						/*if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
						{
							Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
							humanTaskMap.put(keyValue, item);
							keys.add(keyValue);
						}*/
				    }	
					
//					startDate = new Date();
					
//					log.info("%%%%%%%%%%%%%%%%%% STARTING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+startDate);
					
					/**
					 * BPM Integration ends
					 * */
					if( null != keys && 0 != keys.size())
					{
						List<Claim> resultList = new ArrayList<Claim>();
						/*final CriteriaBuilder IntimationBuilder = entityManager.getCriteriaBuilder();
						final CriteriaQuery<Claim> criteriaQuery = IntimationBuilder
								.createQuery(Claim.class);
						Root<Claim> searchRootForIntimation = criteriaQuery.from(Claim.class);
						criteriaQuery.where(searchRootForIntimation.<Long> get("key").in(keys));
						final TypedQuery<Claim> intimateInfoQuery = entityManager
								.createQuery(criteriaQuery);
						resultList =  intimateInfoQuery.getResultList();*/
						resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
						if(!resultList.isEmpty()) {
							
						
						List<Claim> pageItemList = resultList;
						processPreMedicalTableDTO = PAProcessPreMedicalMapper.getInstance()
								.getProcessPreMedicalPreAuth(pageItemList);	
						
						for (PAProcessPreMedicalTableDTO dto : processPreMedicalTableDTO) {
							if(dto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(dto.getProductKey())){
								if(dto.getPaPatientName() != null){
									dto.setInsuredPatientName(dto.getPaPatientName());
								}
							}
						}
						
						for (PAProcessPreMedicalTableDTO tableDto : processPreMedicalTableDTO) {
							if(tableDto.getKey() != null){
								Object workflowKey = workFlowMap.get(tableDto.getKey());
								tableDto.setDbOutArray(workflowKey);
//								Portal Flag updated in cashless table
								Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(tableDto.getKey());
								if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null){
									
									String nhpUpdKey = (String) outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
									tableDto.setNhpUpdDocumentKey(Long.valueOf(nhpUpdKey));
								}
								
								
							}
							
						}
						// Implementing list to retreive hospital name and network hosp type.
						ListIterator<Claim> iterClaim = pageItemList.listIterator();
						List<Long>  hospTypeList = new ArrayList<Long>();
						while (iterClaim.hasNext())
						{
							 Claim Claim= iterClaim.next();
							 /*MastersValue hospTypeInfo = Claim.getIntimation().getHospitalType();
							 Long hospitalTypeId = hospTypeInfo.getKey();*/
							 Long hospitalTypeId = Claim.getIntimation().getHospital();
							 hospTypeList.add(hospitalTypeId);
						}
						
						List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
						List<PAProcessPreMedicalTableDTO> searchPreMedicalTableDTOForHospitalInfoList = new ArrayList<PAProcessPreMedicalTableDTO>();				
						resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
						searchPreMedicalTableDTOForHospitalInfoList = PAProcessPreMedicalMapper.getHospitalInfoList(resultListForHospitalInfo);
						/**
						 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
						 * */
//						DBCalculationService dbCalculationService = new DBCalculationService();
						for (PAProcessPreMedicalTableDTO objSearchProcessPreMedicalClaimTableDTO : processPreMedicalTableDTO )
						{
							Long lPolicyNumber = objSearchProcessPreMedicalClaimTableDTO.getPolicyKey();
							//int iSumInsured = objSearchProcessPreMedicalClaimTableDTO.getSumInsured();
							Long insuredId = objSearchProcessPreMedicalClaimTableDTO.getInsuredId();
							Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"P");
							Long insuredKey = objSearchProcessPreMedicalClaimTableDTO.getInsuredKey();
							Long productKey = objSearchProcessPreMedicalClaimTableDTO.getProductKey();
							
							Claim claimByKey = getClaimByKey(objSearchProcessPreMedicalClaimTableDTO.getKey());
							if (claimByKey != null) {
								objSearchProcessPreMedicalClaimTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
								objSearchProcessPreMedicalClaimTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
								objSearchProcessPreMedicalClaimTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
							}
							
							
						//	objSearchProcessPreMedicalClaimTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , objSearchProcessPreMedicalClaimTableDTO.getKey(), sumInsured,objSearchProcessPreMedicalClaimTableDTO.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
							
							Double balanceSI= dbCalculationService.getBalanceSIForPAHealth(insuredKey,objSearchProcessPreMedicalClaimTableDTO.getKey(),productKey,ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI);
//							TODO Under Discussion for getting Balance SumInsured.
							//objSearchProcessPreMedicalClaimTableDTO.setBalanceSI(Double.valueOf("9999"));
							
							
							
//							objSearchProcessPreMedicalClaimTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessPreMedicalClaimTableDTO.getKey()));
//							objSearchProcessPreMedicalClaimTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessPreMedicalClaimTableDTO.getKey()));
							
							objSearchProcessPreMedicalClaimTableDTO.setUsername(userName);
							objSearchProcessPreMedicalClaimTableDTO.setPassword(passWord);
							
							DocUploadToPremia premiaData = getUploadedDataDocument(objSearchProcessPreMedicalClaimTableDTO.getIntimationNo());
							if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId())
								objSearchProcessPreMedicalClaimTableDTO.setDocReceivedTimeForMatchDate(premiaData.getPfdUpFfaxSubmitId());
								//objSearchProcessPreMedicalClaimTableDTO.setDocReceivedTimeForMatch(String.valueOf(premiaData.getPfdUpPremiaAckDt()));
							if(premiaData != null && null != premiaData.getPfdUpPremiaAckDt())
								objSearchProcessPreMedicalClaimTableDTO.setDocReceivedTimeForRegDate(premiaData.getPfdUpPremiaAckDt());
							if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
								objSearchProcessPreMedicalClaimTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));
							//objSearchProcessPreMedicalClaimTableDTO.setDocReceivedTimeForReg(String.valueOf(premiaData.getPfdUpFfaxSubmitId()));
							
							for (PAProcessPreMedicalTableDTO objSearchProcessClaimTableDTOForHospitalInfo : searchPreMedicalTableDTOForHospitalInfoList)
							{
								
								/**
								 * objSearchProcessClaimTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
								 * objSearchProcessClaimTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
								 * Hospital type. In Hospital.java , we store the key. 
								 * 
								 * But this key will come from intimation table hospital type id. objSearchProcessClaimTableDTO is of 
								 * SearchClaimTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
								 * That is why we equate hospitalTypeId from SearchClaimTableDTO with key from HospitalDTO.
								 * */
								if(objSearchProcessPreMedicalClaimTableDTO.getHospitalTypeId() != null &&
										objSearchProcessClaimTableDTOForHospitalInfo.getKey() != null && objSearchProcessPreMedicalClaimTableDTO.getHospitalTypeId().equals(objSearchProcessClaimTableDTOForHospitalInfo.getKey()))
								{
									objSearchProcessPreMedicalClaimTableDTO.setHospitalName(objSearchProcessClaimTableDTOForHospitalInfo.getHospitalName());
									objSearchProcessPreMedicalClaimTableDTO.setNetworkHospType(objSearchProcessClaimTableDTOForHospitalInfo.getHospitalTypeName());
									break;	
								}
							}
							
						}
		
						
						/*
						 * Implementation for Document Received time - Match Q and Document Recieved time -- Reg Q 
						 * */
						
						List<String> intimationNumberList = new ArrayList<String>();
						
						/**
						 * If intimation number is not provided by user, 
						 * then intimation number list is formed with help of
						 * key which was provided by BPM.
						 * */
						
						if (null == strIntimationNo || ("").equals(strIntimationNo))
						{
							for(PAProcessPreMedicalTableDTO objProcessPreMedicalTblDTO : processPreMedicalTableDTO)
							{
								intimationNumberList.add(objProcessPreMedicalTblDTO.getIntimationNo());
							}
							
						}
						 
						/*final CriteriaBuilder documentBuilder = entityManager.getCriteriaBuilder();
						final CriteriaQuery<TmpStarFaxDetails> documentCriteriaQuery = documentBuilder
								.createQuery(TmpStarFaxDetails.class);
						
						Root<TmpStarFaxDetails> searchRootForDocInfo = documentCriteriaQuery.from(TmpStarFaxDetails.class);
						
						List<Predicate> predicatesForDocInfo = new ArrayList<Predicate>();
		
						List<TmpStarFaxDetails> resultListForDoclInfo = new ArrayList<TmpStarFaxDetails>();*/
						//List<ProcessPreMedicalTableDTO> processPreMedicalTableDTOForDocInfo = new ArrayList<ProcessPreMedicalTableDTO>();
						
						/**
						 * If Intimation number is provided by user, then wildcard
						 * search is implemented. If the intimation number is not provided
						 * by user , then from BPM key we get intimation table information.
						 * From that, intimation number list is formed. With this list, using IN operator,
						 * document information list is obtained.
						 * 
						 * **/
						/*if (null != strIntimationNo && !("").equals(strIntimationNo)) {
							Predicate docPredicate = documentBuilder.like(searchRootForDocInfo. <String >get("intimationNo"), "%" + strIntimationNo + "%");
							predicatesForDocInfo.add(docPredicate);
							documentCriteriaQuery.select(searchRootForDocInfo).where(
									documentBuilder.and(predicatesForDocInfo.toArray(new Predicate[] {})));
						}
						else
						{
							documentCriteriaQuery.where(searchRootForDocInfo.<String> get("intimationNo").in(intimationNumberList));
						}
						
						final TypedQuery<TmpStarFaxDetails> documentInfoQuery = entityManager
								.createQuery(documentCriteriaQuery);
		
						resultListForDoclInfo = documentInfoQuery.getResultList();*/
						
						//List<Hospitals> hospitalItemsList = resultListForHospitalInfo;
						
						
						/*processPreMedicalTableDTOForDocInfo = ProcessPreMedicalMapper
								.getDocumentInfoList(resultListForDoclInfo);*/
						
					/*	if(null != processPreMedicalTableDTOForDocInfo)
						{
							for (int i = 0; i<processPreMedicalTableDTO.size(); i++)
							{
								for(int j=0 ; j<processPreMedicalTableDTOForDocInfo.size();j++)
								{
									*//**
									 * This code requires some change. 
									 * For time being the below code is added since test data
									 * is inserted into tmp_cls_star_fax_details.
									 * *//* 
									ProcessPreMedicalTableDTO preProcessMedicalTableDTO = processPreMedicalTableDTOForDocInfo.get(j);
									
									if(("MATCH-Q").equalsIgnoreCase(preProcessMedicalTableDTO.getTransactionType()))
									{
										processPreMedicalTableDTO.get(i).setDocReceivedTimeForMatch(preProcessMedicalTableDTO.getDocReceivedTimeForReg());
									}
									else if(("REGISTRATION-Q").equalsIgnoreCase(preProcessMedicalTableDTO.getTransactionType()))
									{
										processPreMedicalTableDTO.get(i).setDocReceivedTimeForReg(preProcessMedicalTableDTO.getDocReceivedTimeForReg());
									}
								}
							}
						}*/
					}
					}
				
				
				List<PAProcessPreMedicalTableDTO> filterDTO = new ArrayList<PAProcessPreMedicalTableDTO>();
				
				for (Long claimKeyBPMN : keys) {
					
					for (PAProcessPreMedicalTableDTO processPreMedicalTableDTO2 : processPreMedicalTableDTO) {
						if(processPreMedicalTableDTO2.getKey().equals(claimKeyBPMN)){
							filterDTO.add(processPreMedicalTableDTO2);
							break;
						}
					}
					
				}
				
//				endDate = new Date();
				
//				log.info("%%%%%%%%%%%%%%%%%% ENDING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+endDate);
				
//				log.info("%%%%%%%%%%%%%%%%%% TOTAL TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(startDate, endDate));
				
				
				Page<PAProcessPreMedicalTableDTO> page = new Page<PAProcessPreMedicalTableDTO>();
				Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
//				page.setPageNumber(tasks.getCurrentPage());
				page.setPageItems(filterDTO);
				page.setTotalRecords(totalRecords);
				
//				page.setPagesAvailable(pagedList.getPagesAvailable());
//				page.setHasNext(tasks.getIsNextPageAvailable());
				
				if(!(null != page && null != page.getPageItems() && 0!= page.getPageItems().size()) && strIntimationNo != null && strIntimationNo.length() > 0) {
					Intimation intimationByNo = getIntimationByNo(strIntimationNo);
					if(intimationByNo == null) {
						page.setCorrectMsg("Intimation Not Found");
					} else {
						if(!isAvailableDoc(strIntimationNo)) {
							page.setCorrectMsg("Document Not Received");
						}
					}
				}
				
				
				
				return page;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;			
		}
		
		
		public DocUploadToPremia getUploadedDataDocument(String intimationNo)
		{
//			Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
			Query query = entityManager.createNamedQuery("DocUploadToPremia.findDocumentByIntimation");
			query = query.setParameter("intimationNo", intimationNo); 
//			query.setParameter("docType", SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION);
			List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
			if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
			{
				entityManager.refresh(listOfDocUploadData.get(0));
				return listOfDocUploadData.get(0);
			}
			return null;
		}
		
		public DocUploadToPremia getUploadedDataDocumentDetails(String intimationNo)
		{
			Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimation");
			query = query.setParameter("intimationNo", intimationNo); 
			List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
			if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
			{
				entityManager.refresh(listOfDocUploadData.get(0));
				return listOfDocUploadData.get(0);
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		public Claim getProcessPreMedicalClaimKey (Long processPreMedicalClaimKey) {

//			Query findByKey = entityManager
//					.createNamedQuery("HospitalAcknowledge.findByHospitalKey").setParameter(
//							"hospitalKey", acknowledHospitalKey);
			
			Query findByKey = entityManager.createNamedQuery("Claim.findAll");

			List<Claim> processPreMedicalClaimList = (List<Claim>) findByKey
					.getResultList();

			if (!processPreMedicalClaimList.isEmpty()) {
				return processPreMedicalClaimList.get(0);

			}
			return null;
		}
		
		
		@Override
		public Class<Claim> getDTOClass() {
			// TODO Auto-generated method stub
			return Claim.class;
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
		
		public Boolean isAvailableDoc(String intimationNumber)
		{ 
			Query query = entityManager
					.createNamedQuery("DocUploadToPremia.findDocumentByIntimation");
			query.setParameter("intimationNo", intimationNumber);
			List<DocUploadToPremia> docUploadPremiaList = query.getResultList();
			if(docUploadPremiaList != null && !docUploadPremiaList.isEmpty()) {
				return true;
			}
			return false;
		}
		
		
		public Claim getClaimByKey(Long key) {
			Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
			query.setParameter("claimKey", key);
			List<Claim> claim = (List<Claim>)query.getResultList();
			
			if(claim != null && ! claim.isEmpty()){
				for (Claim claim2 : claim) {
					entityManager.refresh(claim2);
				}
				return claim.get(0);
			}
			else{
				return null;
			}
		}

	}




	
	/*@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;*/
	
/*	public ProcessPreMedicalService(){
		super();
	}*/
	
	/*@SuppressWarnings("unused")
	public List<OldInitiatePedEndorsement> searchPed(ProcessPreMedicalFormDTO processPreMedicalFormDTO){
		String intimationNo = processPreMedicalFormDTO.getIntimationNo().toString();
		String policyNo = processPreMedicalFormDTO.getPolicyNo();
		String type = processPreMedicalFormDTO.getType().getValue();
		String intimationSource = processPreMedicalFormDTO.getIntimationSource().getValue();
		String networkHospType = processPreMedicalFormDTO.getNetworkHospType().getValue();
		
		final CriteriaBuilder builder = entityManager
				.getCriteriaBuilder();
		
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);
		
		Root<OldInitiatePedEndorsement> pedRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);
		Join<OldInitiatePedEndorsement, Intimation> intimationJoin = pedRoot.join(
				"intimation", JoinType.INNER);
		
		Join<OldInitiatePedEndorsement, Policy> policyJoin = pedRoot.join(
				"policy", JoinType.INNER);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (processPreMedicalFormDTO != null) {
			if (intimationNo != null) {
				Predicate intimationNoPredicate = builder.like(
						pedRoot.<Intimation> get("intimation").<String> get(
								"intimationId"), "%" + intimationNo + "%");
				predicates.add(intimationNoPredicate);
				System.out.println("Predicate Intimation value"
						+ intimationNoPredicate.getExpressions().toString());
			}

			if (policyNo != null) {
				Predicate policyNoPredicate = builder.like(pedRoot
						.<Policy> get("policy").<String> get("policyNumber"),
						"%" + policyNo + "%");
				predicates.add(policyNoPredicate);
				System.out.println("Predicate Policy value"
						+ policyNoPredicate.getExpressions().toString());
			}

			criteriaQuery.select(pedRoot).where(
					builder.and(predicates.toArray(new Predicate[] {})));

			final TypedQuery<OldInitiatePedEndorsement> pedQuery = entityManager
					.createQuery(criteriaQuery);

			return pedQuery.getResultList();
		}
		return null;
	}*/


