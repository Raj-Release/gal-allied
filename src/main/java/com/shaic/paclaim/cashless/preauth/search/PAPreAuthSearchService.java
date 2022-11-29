package com.shaic.paclaim.cashless.preauth.search;

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
import com.shaic.arch.table.Pageable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class PAPreAuthSearchService extends AbstractDAO <Preauth> {
	
	//@Inject
	//private PreauthService preauthService;
	//@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	//protected EntityManager entityManager;
	 private final Logger log = LoggerFactory.getLogger(PAPreAuthSearchService.class);
	
	
	public PAPreAuthSearchService()
	{
		super();
	}
	
	@SuppressWarnings("unused")
	public Page<PASearchPreauthTableDTO> search(PASearchPreauthFormDTO formDTO, String userName, String passWord)
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
			List<Long> claimKeys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PP_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			//mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);

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
		    
			
		//	payloadBO = SHAUtils.getCashlessPayloadForPA(payloadBO);
			
			
			Pageable pageable = formDTO.getPageable();
			
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
//			pageable.setPageSize(5);
			
//			pageable.setPageSize(3);
			List<PASearchPreauthTableDTO> searchPreAuthTableDTO = new ArrayList<PASearchPreauthTableDTO>();

		

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
						Long clamKeyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
//						workFlowMap.put(keyValue,outPutArray);
						if(claimedAmount != null){
							claimedAmountMap.put(keyValue, Double.valueOf(claimedAmount));
						}
						
						 totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
						 
/*						 if(SHAUtils.isWithinRange(claimedAmtFrom, claimedAmtTo, claimedAmount)) {
							 keys.add(keyValue);
							} */						 
						 if(null != keyValue && 0 != keyValue){
								 workFlowMap.put(keyValue,outPutArray);
								 keys.add(keyValue);
							 }
							 else
							 {
								 workFlowMap.put(clamKeyValue,outPutArray);
								 claimKeys.add(clamKeyValue);
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
					searchPreAuthTableDTO = PAPreauthMapper.getInstance()
							.getProcessPreAuth(pageItemList);
					
					for (PASearchPreauthTableDTO dto : searchPreAuthTableDTO) {
						if(dto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(dto.getProductKey())){
							if(dto.getPaPatientName() != null){
								dto.setInsuredPatientName(dto.getPaPatientName());
							}
						}
					}

					// Implementing list to retreive hospital name and network hosp type.
					ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
					List<Long>  hospTypeList = new ArrayList<Long>();
					
					for (PASearchPreauthTableDTO tableDto : searchPreAuthTableDTO) {
						if(tableDto.getKey() != null){
							Object workflowKey = workFlowMap.get(tableDto.getKey());
							tableDto.setDbOutArray(workflowKey);
//							Portal Flag updated in cashless table
							Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(tableDto.getKey());
							if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null){
								
								String nhpUpdKey = (String) outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
								tableDto.setNhpUpdDocumentKey(Long.valueOf(nhpUpdKey));
							}
							Double claimedAmt = claimedAmountMap.get(tableDto.getKey());
							tableDto.setClaimedAmountAsPerBill(claimedAmt);
						}
						
					}
					
					while (iterPreAuth.hasNext())
					{
						 Preauth preAuth= iterPreAuth.next();
						 Long hospitalTypeId = preAuth.getIntimation().getHospital();
						 hospTypeList.add(hospitalTypeId);
					}
					
					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
					List<PASearchPreauthTableDTO> searchPreauthTableDTOForHospitalInfoList = new ArrayList<PASearchPreauthTableDTO>();				
					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
					searchPreauthTableDTOForHospitalInfoList = PAPreauthMapper.getHospitalInfoList(resultListForHospitalInfo);
					
					/**
					 * Method to populate speciality for a given pre auth key
					 * */
					
					// TODO: Need to handle with claim key instead of using preauth key...
//					Map specialityMap = SHAUtils.getSpecialityInformation(entityManager, keys);
					
					/**
					 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
					 * */
					//DBCalculationService dbCalculationService = new DBCalculationService();
				//	PreauthService preauthService = new PreauthService();
					for (PASearchPreauthTableDTO objSearchProcessPreAuthTableDTO : searchPreAuthTableDTO )
					{
						Long lPolicyKey = objSearchProcessPreAuthTableDTO.getPolicyKey();
						//int iSumInsured = objSearchProcessPreAuthTableDTO.getSumInsured();
						Long insuredId = objSearchProcessPreAuthTableDTO.getInsuredId();
						Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey,"P");
						Long insuredKey = objSearchProcessPreAuthTableDTO.getInsuredKey();
						Long productKey = objSearchProcessPreAuthTableDTO.getProductKey();

						objSearchProcessPreAuthTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForPAHealth(insuredKey , objSearchProcessPreAuthTableDTO.getClaimKey(),productKey,ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI));
						/*objSearchProcessPreAuthTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessPreAuthTableDTO.getKey()));
						if(objSearchProcessPreAuthTableDTO.getHumanTask() != null && objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless() != null
								&& objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt() != null){
							objSearchProcessPreAuthTableDTO.setClaimedAmountAsPerBill(objSearchProcessPreAuthTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt());
						}
						objSearchProcessPreAuthTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessPreAuthTableDTO.getKey()));*/
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
						
						for (PASearchPreauthTableDTO objSearchProcessPreAuthTableDTOForHospitalInfo : searchPreauthTableDTOForHospitalInfoList)
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
				else{

					if( null != claimKeys && 0 != claimKeys.size())
					{
						List<Claim> resultList = new ArrayList<Claim>();
						resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, claimKeys);
						if(!resultList.isEmpty()) {
							
						
						List<Claim> pageItemList = resultList;
						searchPreAuthTableDTO = PAPreauthMapper.getInstance()
								.getPAProcessPreAuthByClaim(pageItemList);	
						
						
						for (PASearchPreauthTableDTO tableDto : searchPreAuthTableDTO) {
							if(tableDto.getCrmFlagged() != null){
								if(tableDto.getCrmFlagged().equalsIgnoreCase("Y")){
									tableDto.setColorCodeCell("OLIVE");
									tableDto.setCrmFlagged(null);
									//objSearchProcessPreAuthTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
								}
							}
							if(tableDto.getKey() != null){
								Object workflowKey = workFlowMap.get(tableDto.getKey());
								Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(tableDto.getKey());
								if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM) != null){
									String cpuLimit = (String) outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
									if(cpuLimit != null && !cpuLimit.isEmpty() && (cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CPPA) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CP))){
										tableDto.setAboveCPULimitCorp(SHAConstants.CPU_ALLOCATION_ABOVE_LIMIT);	
									}
								 }
								tableDto.setDbOutArray(workflowKey);
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
						List<PASearchPreauthTableDTO> searchPreMedicalTableDTOForHospitalInfoList = new ArrayList<PASearchPreauthTableDTO>();				
						resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
						searchPreMedicalTableDTOForHospitalInfoList = PAPreauthMapper.getHospitalInfoList(resultListForHospitalInfo);
						/**
						 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
						 * */
//						DBCalculationService dbCalculationService = new DBCalculationService();
						for (PASearchPreauthTableDTO objSearchProcessPreMedicalClaimTableDTO : searchPreAuthTableDTO )
						{
							Long lPolicyNumber = objSearchProcessPreMedicalClaimTableDTO.getPolicyKey();
							//int iSumInsured = objSearchProcessPreMedicalClaimTableDTO.getSumInsured();
							Long insuredId = objSearchProcessPreMedicalClaimTableDTO.getInsuredId();
							Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"H");
							Long insuredKey = objSearchProcessPreMedicalClaimTableDTO.getInsuredKey();
							
							Claim claimByKey = getClaimByKey(objSearchProcessPreMedicalClaimTableDTO.getKey());
							if (claimByKey != null) {
								objSearchProcessPreMedicalClaimTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
								objSearchProcessPreMedicalClaimTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
								objSearchProcessPreMedicalClaimTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
								
								if(objSearchProcessPreMedicalClaimTableDTO.getCrmFlagged() != null){
									if(objSearchProcessPreMedicalClaimTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
										if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
											objSearchProcessPreMedicalClaimTableDTO.setColorCodeCell("OLIVE");
										}
										objSearchProcessPreMedicalClaimTableDTO.setCrmFlagged(null);
									}
								}
								if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
									objSearchProcessPreMedicalClaimTableDTO.setColorCodeCell("VIP");
								}
								if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
										&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
									objSearchProcessPreMedicalClaimTableDTO.setColorCodeCell("CRMVIP");
								}
								
							}
							
							if(ReferenceTable.getGMCProductList().containsKey(objSearchProcessPreMedicalClaimTableDTO.getProductKey())){
								objSearchProcessPreMedicalClaimTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForGMC(lPolicyNumber, insuredKey, claimByKey.getKey()));
							}
							else{
								objSearchProcessPreMedicalClaimTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , objSearchProcessPreMedicalClaimTableDTO.getKey(), sumInsured,objSearchProcessPreMedicalClaimTableDTO.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
							}
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
							
							for (PASearchPreauthTableDTO objSearchProcessClaimTableDTOForHospitalInfo : searchPreMedicalTableDTOForHospitalInfoList)
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
							for(PASearchPreauthTableDTO objProcessPreMedicalTblDTO : searchPreAuthTableDTO)
							{
								intimationNumberList.add(objProcessPreMedicalTblDTO.getIntimationNo());
							}
							
						}	

					}
				}
			
				}
						
			Page<PASearchPreauthTableDTO> page = new Page<PASearchPreauthTableDTO>();
			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			
			ImsUser imsUser = formDTO.getImsUser();
				
			String[] userRoleList = imsUser.getUserRoleList();
			//page.setPageNumber(pagedList.getPageNumber());
			for (PASearchPreauthTableDTO searchPreauthTableDTO2 : searchPreAuthTableDTO) {
				
				searchPreauthTableDTO2.setImsUser(imsUser);
                
				
			}
			page.setPageItems(searchPreAuthTableDTO);
			//page.setPageNumber(tasks.getCurrentPage());
		//	page.setHasNext(tasks.getIsNextPageAvailable());
			page.setTotalRecords(totalRecords);
			//page.setPagesAvailable(pageCount);
			//page.setPagesAvailable(pagedList.getPagesAvailable());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	
	
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
			String specilityValue ="";
			Query findCpuCode = entityManager.createNamedQuery("Speciality.findByClaimKey").setParameter("claimKey", claimKey);
			List<Speciality> SpecialityList = findCpuCode.getResultList();
			for(Speciality speciality : SpecialityList){ 
				Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey").setParameter("key", speciality.getSpecialityType().getKey());
				SpecialityType result = (SpecialityType) findSpecilty.getSingleResult(); 
				specilityValue += result.getValue()+", ";
			}
			return specilityValue;
			}catch(Exception e){
				return null;
			}
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
