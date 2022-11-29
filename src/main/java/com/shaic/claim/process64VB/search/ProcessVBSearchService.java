package com.shaic.claim.process64VB.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.shaic.claim.preauth.search.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.VB64ApprovalRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class ProcessVBSearchService extends AbstractDAO <Preauth> {
	
	//@Inject
	//private PreauthService preauthService;
	//@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	//protected EntityManager entityManager;
	 private final Logger log = LoggerFactory.getLogger(ProcessVBSearchService.class);
	
	
	public ProcessVBSearchService()
	{
		super();
	}
	
//	@SuppressWarnings("unused")
//	public Page<SearchPreauthTableDTO> bPMNsearch(SearchPreauthFormDTO formDTO, String userName, String passWord)
//	{
//		try 
//		{
//			String strIntimationNo = "";
//			String strPolicyNo = "";
//			String strType = null;
//			
//			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());
//			
//			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
//			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
//			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
//			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
//			String claimedAmtFrom = formDTO.getClaimedAmtFrom() != null ? formDTO.getClaimedAmtFrom() : null;
//			String claimedAmtTo =  formDTO.getClaimedAmtTo() != null ? formDTO.getClaimedAmtTo() : null;
//			
//			
//			
//			PayloadBOType payloadBO = null;
//
//			HospitalInfoType hospitalTypeInfo = null;
//			
//			IntimationType intimationType = null;
//			
//			CustomerType custInfoType = null;
//			
//			
//			PolicyType policyType = null;
//			
//			if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equalsIgnoreCase(""))
//			{
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				if(intimationType == null){
//				    intimationType = new IntimationType();
//				}
//				strIntimationNo = formDTO.getIntimationNo();
//				intimationType.setIntimationNumber(strIntimationNo);
//			}
//			
//			if(null != formDTO.getPolicyNo() && ! formDTO.getPolicyNo().equalsIgnoreCase(""))
//			{
//				
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				
//				if(policyType == null){
//					policyType = new PolicyType();
//				}
//				strPolicyNo= formDTO.getPolicyNo();
//				policyType.setPolicyId(strPolicyNo);
//			}
//			
//			
//			String strIntimationSource = null;
//			if(null != formDTO.getIntimationSource())
//			{
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				if(intimationType == null){
//					intimationType = new IntimationType();
//				}
//				strIntimationSource = formDTO.getIntimationSource().getValue();
//				
//				intimationType.setIntimationSource(strIntimationSource);
//				
//			}
//			
//			String strNetworkHospType = null;
//			if(null != formDTO.getNetworkHospType())
//			{
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				hospitalTypeInfo = new HospitalInfoType();
////				strNetworkHospType = formDTO.getNetworkHospType().getValue();
////				hospitalTypeInfo.setNetworkHospitalType(strNetworkHospType);
//				if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.NETWORK_HOSPITAL)){
//					hospitalTypeInfo.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
//				}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
//					hospitalTypeInfo.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
//				}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
//					hospitalTypeInfo.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
//				}
//				
//			}
//			
//			String strTreatMentType = null;
//			
//			if(null != formDTO.getTreatmentType())
//			{
//				
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				
//				if(custInfoType == null){
//					custInfoType = new CustomerType();
//				}
//				strTreatMentType = formDTO.getTreatmentType().getValue();
//				custInfoType.setTreatmentType(strTreatMentType);
//			}		
//			
//			String strSpeciality = null;
//			
//			if(null != formDTO.getSpeciality())
//			{
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				if(custInfoType == null){
//					custInfoType = new CustomerType();
//				}
//				strSpeciality = formDTO.getSpeciality().getValue();
//				custInfoType.setSpeciality(strSpeciality);
//			}
//			
//			ClaimRequestType claimRequestType = new ClaimRequestType();
//			if(cpuCode != null){
//				claimRequestType.setCpuCode(cpuCode);
//				if(payloadBO == null){
//					payloadBO = new PayloadBOType();
//				}
//				payloadBO.setClaimRequest(claimRequestType);
//			}
//			
//			ClassificationType classification = null;
//			
//		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
//						|| type != null && ! type.isEmpty()){
//					classification = new ClassificationType();
//					
//					if(priority != null && ! priority.isEmpty())
//						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
//							priority = null;
//						}
//					classification.setPriority(priority);
//					if(source != null && ! source.isEmpty()){
//						classification.setSource(source);
//					}
//					
//					if(type != null && ! type.isEmpty()){
//						if(type.equalsIgnoreCase(SHAConstants.ALL)){
//							type = null;
//						}
//						classification.setType(type);
//					}
//					
//					if(payloadBO == null){
//						payloadBO = new PayloadBOType();
//					}
//					
//					 payloadBO.setClassification(classification);
//			}
//			
//			/**
//			 * BPM integration starts.
//			 * 
//			 * */
//			if(payloadBO != null){
//				payloadBO.setHospitalInfo(hospitalTypeInfo);
//				payloadBO.setCustomer(custInfoType);
//				payloadBO.setPolicy(policyType);
//				payloadBO.setIntimation(intimationType);
//			}
//				
//			/*PreAuthQF preAuthQF = null;
//			
//			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strIntimationSource || ("").equals(strIntimationSource))
//					&& (null == strType || ("").equals(strType)) && (null == strNetworkHospType || ("").equals(strNetworkHospType)) && (null == strTreatMentType || ("").equals(strTreatMentType))
//					&& (null == strSpeciality || ("").equals(strSpeciality))))
//			{
//				preAuthQF = new PreAuthQF();
//				preAuthQF.setIntimationNumber((null != strIntimationNo && !("").equalsIgnoreCase(strIntimationNo))? strIntimationNo :null);
//				preAuthQF.setPolicyId((null != strPolicyNo && !("").equalsIgnoreCase(strPolicyNo)) ? strPolicyNo :null);
//				preAuthQF.setIntimationSource(strIntimationSource);
//				preAuthQF.setType(strType);
//				preAuthQF.setNetworkHospitalType(strNetworkHospType);
//				preAuthQF.setTreatmentType(strTreatMentType);
//				preAuthQF.setSpeciality(strSpeciality);
//			}*/
//					
//			//Pageable pageable = formDTO.getPageable();
//			//pageable = null;
//			Pageable pageable = formDTO.getPageable();
//			
//			pageable.setPageNumber(1);
//			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
//			
////			pageable.setPageSize(5);
//			
////			pageable.setPageSize(3);
//			List<SearchPreauthTableDTO> searchPreAuthTableDTO = new ArrayList<SearchPreauthTableDTO>();
//
//			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask preAuthTask = BPMClientContext.getProcessPreAuthTask(userName,passWord);
//			//PagedTaskList tasks =  preAuthTask.getTasks("zma1", BPMClientContext.USER_PASSWORD, pageable, preAuthQF);
//			//PagedTaskList tasks =  preAuthTask.getTasks(userName,passWord, pageable, preAuthQF);  //userName="zma1"
//			Date startDate = new Date();
//			
//			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME FOR GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+startDate);
//			
//			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = preAuthTask.getTasks(userName, pageable, payloadBO);
//			
//			Date endDate = new Date();
//			
//			log.info("%%%%%%%%%%%%%%%%%% ENDING TIME FOR GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+endDate);
//			
//			String durationFromTwoDate = SHAUtils.getDurationFromTwoDate(startDate, endDate);
//			
//			log.info("%%%%%%%%%%%%%%%%%% TOTAT TIME FOR BPMN %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+durationFromTwoDate);
//			
//			//Map to set human task to table DTO.
//			Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//		    Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
//			if(null != tasks)
//			{
//				List<HumanTask> humanTasksList = tasks.getHumanTasks();
//				List<Long> keys = new ArrayList<Long>();  
//				
//				for (HumanTask item: humanTasksList)
//			    {
//					//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
//					/*Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
//					if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
//					{
//						Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
//						//Key --> item mapping
//						humanTaskMap.put(keyValue, item);
//						keys.add(keyValue);
//					}	*/	
//					
//					PayloadBOType payloadBOCashless = item.getPayloadCashless();
//					PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
//					String payloadCpuCode = null;
//					String claimedAmt = null;
//					if(payloadBOCashless.getPreAuthReq() != null && payloadBOCashless.getPreAuthReq().getPreAuthAmt() != null) {
//						claimedAmt = String.valueOf(payloadBOCashless.getPreAuthReq().getPreAuthAmt());
//					}
//					Double payloadClaimedAmt = SHAUtils.getDoubleValueFromString(claimedAmt);
//					if(payloadBOCashless.getClaimRequest() != null && payloadBOCashless.getClaimRequest().getCpuCode() !=  null) {
//						payloadCpuCode = payloadBOCashless.getClaimRequest().getCpuCode();
//					}
////					System.out.println("Payload CPU CODE ----->" + payloadCpuCode + "  Selected CPU CODE ---->" + cpuCode);
//					if(null != preauthReq)
//					{
//						Long keyValue = preauthReq.getKey();
//						humanTaskMap.put(keyValue, item);
//						taskNumberMap.put(keyValue,item.getNumber());
////						if(cpuCode != null) {
////							String[] split = cpuCode.split("-");
////							String cpuCodeStr = split[0];
////							if(cpuCodeStr != null) {
////								cpuCodeStr = cpuCodeStr.replaceAll("\\s","");
////							}
////							if(payloadCpuCode != null && cpuCodeStr != null && payloadCpuCode.equalsIgnoreCase(cpuCodeStr)) {
////								if(SHAUtils.isWithinRange(claimedAmtFrom, claimedAmtTo, claimedAmt)) {
////									keys.add(keyValue);
////								}
////							}
////						} 
////						else {
//							if(SHAUtils.isWithinRange(claimedAmtFrom, claimedAmtTo, claimedAmt)) {
//								keys.add(keyValue);
//							}
////						}
//						
//					}
//					
//			    }
//				
//				log.info("key list --->"+keys);
//				
// 				System.out.println("---pre auth key---"+keys+"--human task map---"+humanTaskMap);
//				
//				/**
//				 * BPM Integration ends
//				 * */
// 				
// 				startDate = new Date();
// 				
// 			log.info("STARTING TIME FOR DATE FETCHING FROM DB 	"+startDate);
// 			
// 				
//				if( null != keys && 0 != keys.size())
//				{ 
//					List<Preauth> resultList = new ArrayList<Preauth>();	
//					
//					for (Long preauthKeys : keys) {
//						List<Long> keyValues = new ArrayList<Long>();
//						keyValues.add(preauthKeys);
//						List<Preauth> preauthList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keyValues);
//						if(preauthList != null && preauthList.size()>0){
//							resultList.add(preauthList.get(0));
//						}
//					}
////					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
//					
//					List<Preauth> pageItemList = resultList;
//					searchPreAuthTableDTO = PreauthMapper.getInstance()
//							.getProcessPreAuth(pageItemList);
//					// Implementing list to retreive hospital name and network hosp type.
//					ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
//					List<Long>  hospTypeList = new ArrayList<Long>();
//					
//					while (iterPreAuth.hasNext())
//					{
//						 Preauth preAuth= iterPreAuth.next();
//						 Long hospitalTypeId = preAuth.getIntimation().getHospital();
//						 hospTypeList.add(hospitalTypeId);
//					}
//					
//					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
//					List<SearchPreauthTableDTO> searchPreauthTableDTOForHospitalInfoList = new ArrayList<SearchPreauthTableDTO>();				
//					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
//					searchPreauthTableDTOForHospitalInfoList = PreauthMapper.getHospitalInfoList(resultListForHospitalInfo);
//					
//					/**
//					 * Method to populate speciality for a given pre auth key
//					 * */
//					
//					// TODO: Need to handle with claim key instead of using preauth key...
////					Map specialityMap = SHAUtils.getSpecialityInformation(entityManager, keys);
//					
//					/**
//					 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
//					 * */
//					DBCalculationService dbCalculationService = new DBCalculationService();
//				//	PreauthService preauthService = new PreauthService();
//					for (SearchPreauthTableDTO objSearchProcessPreAuthTableDTO : searchPreAuthTableDTO )
//					{
//						Long lPolicyKey = objSearchProcessPreAuthTableDTO.getPolicyKey();
//						//int iSumInsured = objSearchProcessPreAuthTableDTO.getSumInsured();
//						Long insuredId = objSearchProcessPreAuthTableDTO.getInsuredId();
//						Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey);
//						Long insuredKey = objSearchProcessPreAuthTableDTO.getInsuredKey();
//						
//						Preauth preauthById = getPreauthById(objSearchProcessPreAuthTableDTO.getKey());
//						if (preauthById != null){
//							objSearchProcessPreAuthTableDTO.setSubCoverCode(preauthById.getClaim().getClaimSubCoverCode());
//							objSearchProcessPreAuthTableDTO.setSectionCode(preauthById.getClaim().getClaimSectionCode());
//							objSearchProcessPreAuthTableDTO.setCoverCode(preauthById.getClaim().getClaimCoverCode());
//						}
//						
//						
//						
//						objSearchProcessPreAuthTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyKey, insuredKey , objSearchProcessPreAuthTableDTO.getClaimKey(), sumInsured,objSearchProcessPreAuthTableDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI));
//						objSearchProcessPreAuthTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessPreAuthTableDTO.getKey()));
//						if(objSearchProcessPreAuthTableDTO.getHumanTask() != null && objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless() != null
//								&& objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt() != null){
//							objSearchProcessPreAuthTableDTO.setClaimedAmountAsPerBill(objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt());
//						}
//						objSearchProcessPreAuthTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessPreAuthTableDTO.getKey()));
//						objSearchProcessPreAuthTableDTO.setSpeciality(getSpecialityType(objSearchProcessPreAuthTableDTO.getClaimKey()));
//						
//						DocUploadToPremia premiaData = getUploadedDataDocument(objSearchProcessPreAuthTableDTO.getIntimationNo());
//						
//						if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
//							objSearchProcessPreAuthTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));
//						
//						if(null != objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch())
//							//objSearchProcessPreAuthTableDTO.setStrDocReceivedTimeForMatch(String.valueOf(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch()));
//							objSearchProcessPreAuthTableDTO.setDocReceivedTimeForMatchDate(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch());
//						if(null != objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg())
//							//objSearchProcessPreAuthTableDTO.setStrDocReceivedTimeForReg(String.valueOf(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg()));
//							objSearchProcessPreAuthTableDTO.setDocReceivedTimeForRegDate(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg());
//						
//						
//						//Setting pre auth requested amount.
//						//objSearchProcessPreAuthTableDTO.setPreAuthReqAmt(preauthService.calculatePreRequestedAmt(entityManager,objSearchProcessPreAuthTableDTO.getKey()));
//						
////						objSearchProcessPreAuthTableDTO.setSpeciality((String)specialityMap.get(objSearchProcessPreAuthTableDTO.getKey()));
//						
//						objSearchProcessPreAuthTableDTO.setUsername(userName);
//						objSearchProcessPreAuthTableDTO.setPassword(passWord);
//						
//						for (SearchPreauthTableDTO objSearchProcessPreAuthTableDTOForHospitalInfo : searchPreauthTableDTOForHospitalInfoList)
//						{
//							/**
//							 * objSearchProcessPreAuthTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
//							 * objSearchProcessPreAuthTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
//							 * Hospital type. In Hospital.java , we store the key. 
//							 * 
//							 * But this key will come from intimation table hospital type id. objSearchProcessPreAuthTableDTO is of 
//							 * SearchPreauthTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
//							 * That is why we equate hospitalTypeId from SearchPreauthTableDTO with key from HospitalDTO.
//							 * */
//							if(objSearchProcessPreAuthTableDTO.getHospitalTypeId() != null && objSearchProcessPreAuthTableDTOForHospitalInfo.getKey() != null && objSearchProcessPreAuthTableDTO.getHospitalTypeId().equals(objSearchProcessPreAuthTableDTOForHospitalInfo.getKey()))
//							{
//								objSearchProcessPreAuthTableDTO.setHospitalName(objSearchProcessPreAuthTableDTOForHospitalInfo.getHospitalName());
//								objSearchProcessPreAuthTableDTO.setNetworkHospType(objSearchProcessPreAuthTableDTOForHospitalInfo.getHospitalTypeName());
//								break;
//							}
//						}
//					}
//					/*
//					 * Implementation for Document Received time - Match Q and Document Recieved time -- Reg Q 
//					 * */
//					
//				/*	List<String> intimationNumberList = new ArrayList<String>();
//					
//					*//**
//					 * If intimation number is not provided by user, 
//					 * then intimation number list is formed with help of
//					 * key which was provided by BPM.
//					 * *//*
//					
//					if (null == strIntimationNo || ("").equals(strIntimationNo))
//					{
//						for(SearchPreauthTableDTO searchPreAuthTbl : searchPreAuthTableDTO)
//						{
//							intimationNumberList.add(searchPreAuthTbl.getIntimationNo());
//						}
//						
//					}
//					final CriteriaBuilder documentBuilder = entityManager.getCriteriaBuilder();
//					final CriteriaQuery<TmpStarFaxDetails> documentCriteriaQuery = documentBuilder
//							.createQuery(TmpStarFaxDetails.class);
//					
//					Root<TmpStarFaxDetails> searchRootForDocInfo = documentCriteriaQuery.from(TmpStarFaxDetails.class);
//					
//					List<Predicate> predicatesForDocInfo = new ArrayList<Predicate>();
//		
//					List<TmpStarFaxDetails> resultListForDoclInfo = new ArrayList<TmpStarFaxDetails>();
//					List<SearchPreauthTableDTO> SearchPreAuthTableDTOForDocInfo = new ArrayList<SearchPreauthTableDTO>();
//					
//					*//**
//					 * If Intimation number is provided by user, then wildcard
//					 * search is implemented. If the intimation number is not provided
//					 * by user , then from BPM key we get intimation table information.
//					 * From that, intimation number list is formed. With this list, using IN operator,
//					 * document information list is obtained.
//					 * 
//					 ***//*
//					if (null != strIntimationNo && !("").equals(strIntimationNo)) {
//						Predicate docPredicate = documentBuilder.like(searchRootForDocInfo. <String >get("intimationNo"), "%" + strIntimationNo + "%");
//						predicatesForDocInfo.add(docPredicate);
//						documentCriteriaQuery.select(searchRootForDocInfo).where(
//								documentBuilder.and(predicatesForDocInfo.toArray(new Predicate[] {})));
//					}
//					else
//					{
//						documentCriteriaQuery.where(searchRootForDocInfo.<String> get("intimationNo").in(intimationNumberList));
//					}
//		
//					final TypedQuery<TmpStarFaxDetails> documentInfoQuery = entityManager
//							.createQuery(documentCriteriaQuery);
//		
//					resultListForDoclInfo = documentInfoQuery.getResultList();
//								
//					SearchPreAuthTableDTOForDocInfo = PreauthMapper
//							.getDocumentInfoList(resultListForDoclInfo);
//					
//					if(null != SearchPreAuthTableDTOForDocInfo)
//					{
//						for (int i = 0; i<searchPreAuthTableDTO.size(); i++)
//						{
//							for(int j=0 ; j<SearchPreAuthTableDTOForDocInfo.size();j++)
//							{
//								*//**
//								 * This code requires some change. 
//								 * For time being the below code is added since test data
//								 * is inserted into tmp_cls_star_fax_details.
//								 * *//* 
//								SearchPreauthTableDTO PreAuthSearchTableDTO = SearchPreAuthTableDTOForDocInfo.get(j);
//								searchPreAuthTableDTO.get(i).setEnhancementReqAmt(PreAuthSearchTableDTO.getEnhancementReqAmt());
//								if(("MATCH-Q").equalsIgnoreCase(PreAuthSearchTableDTO.getTransactionType()))
//								{
//									searchPreAuthTableDTO.get(i).setDocReceivedTimeForMatch(PreAuthSearchTableDTO.getDocReceivedTimeForReg());
//								}
//								else if(("REGISTRATION-Q").equalsIgnoreCase(PreAuthSearchTableDTO.getTransactionType()))
//								{
//									searchPreAuthTableDTO.get(i).setDocReceivedTimeForReg(PreAuthSearchTableDTO.getDocReceivedTimeForReg());
//								}
//							}
//						}
//					}*/
//				}
//			}
//			
//			endDate = new Date();
//			
//			log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%ENDING TIME FOR DATE FETCHING FROM DB%%%%%%%%%%%%%%%%%%%%%%%"+endDate);
//			
//			String durationFromTwoDate2 = SHAUtils.getDurationFromTwoDate(startDate, endDate);
//			
//			log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%TOTAL TIME FOR DB FETCHING FOR SEARCH SCREEN %%%%%%%%%%%%%%%%%%%%%%%"+durationFromTwoDate2);
//			
//			
//			Page<SearchPreauthTableDTO> page = new Page<SearchPreauthTableDTO>();
//			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
//			
//			ImsUser imsUser = formDTO.getImsUser();
//				
//			String[] userRoleList = imsUser.getUserRoleList();
//			//page.setPageNumber(pagedList.getPageNumber());
//			for (SearchPreauthTableDTO searchPreauthTableDTO2 : searchPreAuthTableDTO) {
//				
//				searchPreauthTableDTO2.setImsUser(imsUser);
//                
//				
//			}
//			page.setPageItems(searchPreAuthTableDTO);
//			page.setPageNumber(tasks.getCurrentPage());
//			page.setHasNext(tasks.getIsNextPageAvailable());
//			page.setTotalRecords(tasks.getTotalRecords());
//			//page.setPagesAvailable(pageCount);
//			//page.setPagesAvailable(pagedList.getPagesAvailable());
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;		
//	}

	
	
	private DocUploadToPremia getUploadedDataDocument(String intimationNo)
	{
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
		query = query.setParameter("intimationNo", intimationNo); 
		query.setParameter("docType", SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION);
		List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			entityManager.refresh(listOfDocUploadData.get(0));
			return listOfDocUploadData.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Preauth getSearchPreAuthKey (Long preAuthSearchKey) {
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Preauth> preAuthSearchKeyList = (List<Preauth>) findByKey
				.getResultList();

		if (!preAuthSearchKeyList.isEmpty()) {
			return preAuthSearchKeyList.get(0);

		}
		return null;
	}
	

	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return Preauth.class;
	}
	
	private String getSpecialityType(Long claimKey){
		try{
			StringBuffer specilityValue = new StringBuffer();
			Query findCpuCode = entityManager.createNamedQuery("Speciality.findByClaimKey").setParameter("claimKey", claimKey);
			List<Speciality> SpecialityList = findCpuCode.getResultList();
			for(Speciality speciality : SpecialityList){ 
				Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey").setParameter("key", speciality.getSpecialityType().getKey());
				SpecialityType result = (SpecialityType) findSpecilty.getSingleResult(); 
				specilityValue.append(result.getValue()).append(", ");
			}
			return specilityValue.toString();
			}catch(Exception e){
				return null;
			}
	}
	
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
	}	
	
	@SuppressWarnings("unused")
	public Page<SearchPreauthTableDTO> search(SearchPreauthFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			String strIntimationNo = "";
			String strPolicyNo = "";
			String strType = null;
			List<Map<String, Object>> taskProcedure = null ;
			
			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
			String claimedAmtFrom = formDTO.getClaimedAmtFrom() != null ? formDTO.getClaimedAmtFrom() : null;
			String claimedAmtTo =  formDTO.getClaimedAmtTo() != null ? formDTO.getClaimedAmtTo() : null;
			
			
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PROCESS_VB_CURRENT_QUEUE);
			

			if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equals("")) {
				strIntimationNo = formDTO.getIntimationNo();
				
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				
			}
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equals("")) {
				strPolicyNo= formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
			}
			String strIntimationSource = null;
			if(null != formDTO.getIntimationSource()) {
				strIntimationSource = formDTO.getIntimationSource().getValue();
				mapValues.put(SHAConstants.INT_SOURCE, strIntimationSource);
			}
			String strNetworkHospType = null;
			if(null != formDTO.getNetworkHospType()) {
				
				if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospType().getValue();
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
					
				}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospType().getValue();
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
					
				}else if(formDTO.getNetworkHospType().getId() != null && formDTO.getNetworkHospType().getId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospType().getValue();
				
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
				
				}
				
			}
			
			if(cpuCode != null){
				
				mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				
			}
			
			if(null != formDTO.getSpeciality()) {
				mapValues.put(SHAConstants.SPECIALITY_NAME, formDTO.getSpeciality().getValue());
			}
			
			if(null != formDTO.getTreatmentType()) {
				mapValues.put(SHAConstants.TREATEMENT_TYPE, formDTO.getTreatmentType().getValue());
			}
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					if(priority != null && ! priority.isEmpty())
						if(! priority.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					if(source != null && ! source.isEmpty()){
						
						mapValues.put(SHAConstants.STAGE_SOURCE, source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(! type.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
					}
					
			}
		    
		   
		    List<SearchPreauthTableDTO> searchPreAuthTableDTO = new ArrayList<SearchPreauthTableDTO>();
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
			
		//	Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			//taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
						String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
						workFlowMap.put(keyValue,outPutArray);
						if(claimedAmount != null){
							claimedAmountMap.put(keyValue, Double.valueOf(claimedAmount));
						}
						
						 totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
						 
						 if(SHAUtils.isWithinRange(claimedAmtFrom, claimedAmtTo, claimedAmount)) {
							 keys.add(keyValue);
							}

					}

				}	
				
 				
 			
 				
				if( null != keys && 0 != keys.size())
				{ 
					List<Preauth> resultList = new ArrayList<Preauth>();	
					
					for (Long preauthKeys : keys) {
						List<Long> keyValues = new ArrayList<Long>();
						keyValues.add(preauthKeys);
						List<Preauth> preauthList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keyValues);
						if(preauthList != null && preauthList.size()>0){
							resultList.add(preauthList.get(0));
						}
					}
//					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
					
					List<Preauth> pageItemList = resultList;
					searchPreAuthTableDTO = PreauthMapper.getInstance()
							.getProcessPreAuth(pageItemList);
					
					for (SearchPreauthTableDTO tableDto : searchPreAuthTableDTO) {
						if(tableDto.getKey() != null){
							Object workflowKey = workFlowMap.get(tableDto.getKey());
							tableDto.setDbOutArray(workflowKey);
							Double claimedAmt = claimedAmountMap.get(tableDto.getKey());
							tableDto.setClaimedAmountAsPerBill(claimedAmt);
						}
						
					}
					// Implementing list to retreive hospital name and network hosp type.
					ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
					List<Long>  hospTypeList = new ArrayList<Long>();
					
					while (iterPreAuth.hasNext())
					{
						 Preauth preAuth= iterPreAuth.next();
						 Long hospitalTypeId = preAuth.getIntimation().getHospital();
						 hospTypeList.add(hospitalTypeId);
					}
					
					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
					List<SearchPreauthTableDTO> searchPreauthTableDTOForHospitalInfoList = new ArrayList<SearchPreauthTableDTO>();				
					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
					searchPreauthTableDTOForHospitalInfoList = PreauthMapper.getHospitalInfoList(resultListForHospitalInfo);
					
					/**
					 * Method to populate speciality for a given pre auth key
					 * */
					
					// TODO: Need to handle with claim key instead of using preauth key...
//					Map specialityMap = SHAUtils.getSpecialityInformation(entityManager, keys);
					
					/**
					 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
					 * */
				//	PreauthService preauthService = new PreauthService();
					for (SearchPreauthTableDTO objSearchProcessPreAuthTableDTO : searchPreAuthTableDTO )
					{
						Long lPolicyKey = objSearchProcessPreAuthTableDTO.getPolicyKey();
						//int iSumInsured = objSearchProcessPreAuthTableDTO.getSumInsured();
						Long insuredId = objSearchProcessPreAuthTableDTO.getInsuredId();
						Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey,"H");
						Long insuredKey = objSearchProcessPreAuthTableDTO.getInsuredKey();
						
						Preauth preauthById = getPreauthById(objSearchProcessPreAuthTableDTO.getKey());
						if (preauthById != null){
							objSearchProcessPreAuthTableDTO.setSubCoverCode(preauthById.getClaim().getClaimSubCoverCode());
							objSearchProcessPreAuthTableDTO.setSectionCode(preauthById.getClaim().getClaimSectionCode());
							objSearchProcessPreAuthTableDTO.setCoverCode(preauthById.getClaim().getClaimCoverCode());
						}
						
						
						
						objSearchProcessPreAuthTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyKey, insuredKey , objSearchProcessPreAuthTableDTO.getClaimKey(), sumInsured,objSearchProcessPreAuthTableDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI));
//						objSearchProcessPreAuthTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessPreAuthTableDTO.getKey()));
//						if(objSearchProcessPreAuthTableDTO.getHumanTask() != null && objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless() != null
//								&& objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt() != null){
////							objSearchProcessPreAuthTableDTO.setClaimedAmountAsPerBill(objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt());
//						}
//						objSearchProcessPreAuthTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessPreAuthTableDTO.getKey()));
						objSearchProcessPreAuthTableDTO.setSpeciality(getSpecialityType(objSearchProcessPreAuthTableDTO.getClaimKey()));
						
						DocUploadToPremia premiaData = getUploadedDataDocument(objSearchProcessPreAuthTableDTO.getIntimationNo());
						
						if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
							objSearchProcessPreAuthTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));
						
						if(null != objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch())
							//objSearchProcessPreAuthTableDTO.setStrDocReceivedTimeForMatch(String.valueOf(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch()));
							objSearchProcessPreAuthTableDTO.setDocReceivedTimeForMatchDate(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForMatch());
						if(null != objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg())
							//objSearchProcessPreAuthTableDTO.setStrDocReceivedTimeForReg(String.valueOf(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg()));
							objSearchProcessPreAuthTableDTO.setDocReceivedTimeForRegDate(objSearchProcessPreAuthTableDTO.getDocReceivedTimeForReg());
						
						
						//Setting pre auth requested amount.
						//objSearchProcessPreAuthTableDTO.setPreAuthReqAmt(preauthService.calculatePreRequestedAmt(entityManager,objSearchProcessPreAuthTableDTO.getKey()));
						
//						objSearchProcessPreAuthTableDTO.setSpeciality((String)specialityMap.get(objSearchProcessPreAuthTableDTO.getKey()));
						
						objSearchProcessPreAuthTableDTO.setUsername(userName);
						objSearchProcessPreAuthTableDTO.setPassword(passWord);
						
						for (SearchPreauthTableDTO objSearchProcessPreAuthTableDTOForHospitalInfo : searchPreauthTableDTOForHospitalInfoList)
						{
							/**
							 * objSearchProcessPreAuthTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
							 * objSearchProcessPreAuthTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
							 * Hospital type. In Hospital.java , we store the key. 
							 * 
							 * But this key will come from intimation table hospital type id. objSearchProcessPreAuthTableDTO is of 
							 * SearchPreauthTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
							 * That is why we equate hospitalTypeId from SearchPreauthTableDTO with key from HospitalDTO.
							 * */
							if(objSearchProcessPreAuthTableDTO.getHospitalTypeId() != null && objSearchProcessPreAuthTableDTOForHospitalInfo.getKey() != null && objSearchProcessPreAuthTableDTO.getHospitalTypeId().equals(objSearchProcessPreAuthTableDTOForHospitalInfo.getKey()))
							{
								objSearchProcessPreAuthTableDTO.setHospitalName(objSearchProcessPreAuthTableDTOForHospitalInfo.getHospitalName());
								objSearchProcessPreAuthTableDTO.setNetworkHospType(objSearchProcessPreAuthTableDTOForHospitalInfo.getHospitalTypeName());
								break;
							}
						}
					}
				
				}
			
			
			
			Page<SearchPreauthTableDTO> page = new Page<SearchPreauthTableDTO>();
			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			
			ImsUser imsUser = formDTO.getImsUser();
				
			String[] userRoleList = imsUser.getUserRoleList();
			//page.setPageNumber(pagedList.getPageNumber());
			for (SearchPreauthTableDTO searchPreauthTableDTO2 : searchPreAuthTableDTO) {
				
				searchPreauthTableDTO2.setImsUser(imsUser);
                
				
			}
			page.setPageItems(searchPreAuthTableDTO);
//			page.setPageNumber(tasks.getCurrentPage());
//			page.setHasNext(tasks.getIsNextPageAvailable());
			page.setTotalRecords(totalRecords);
			//page.setPagesAvailable(pageCount);
			//page.setPagesAvailable(pagedList.getPagesAvailable());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	public void updateVB64ApprovalRequest(VB64ApprovalRequest approvalRequest,
			SearchPreauthTableDTO bean, String paymentStatus,Preauth preauth) {
		
		Status status = new Status();
		if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE)){
			status.setKey(ReferenceTable.VB_COMPLIANCE_TO_PREAUTH);
		}else{
			status.setKey(ReferenceTable.VB_COMPLIANCE_TO_ENHANCEMENT);
		}
		
		preauth.setStatus(status);
		preauth.setModifiedBy(bean.getUsername());
		preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(preauth);
		entityManager.flush();
		entityManager.clear();
		
		if(paymentStatus!=null ){
			approvalRequest.setStatusFlag(paymentStatus);
		}
		approvalRequest.setStatus(status);
		approvalRequest.setApproverRemarks(bean.getProcessVBremarks());
		approvalRequest.setModifiedBy(bean.getUsername());
		approvalRequest.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(approvalRequest);	
		entityManager.flush();
		entityManager.clear();
	}

}
