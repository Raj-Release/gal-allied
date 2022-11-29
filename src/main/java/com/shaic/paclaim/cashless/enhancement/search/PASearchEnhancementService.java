package com.shaic.paclaim.cashless.enhancement.search;

import java.util.ArrayList;
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
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthFormDTO;
import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthTableDTO;

@Stateless
public class PASearchEnhancementService extends AbstractDAO <Preauth> {
	
	public PASearchEnhancementService()
	{
		super();
	}
	
	@SuppressWarnings("unused")
	public Page<PASearchPreauthTableDTO> search(PASearchPreauthFormDTO formDTO, String userName, String passWord)
	{
		try {
			String strIntimationNo = "";
			String strPolicyNo = "";
			String strType = null;
			List<Map<String, Object>> taskProcedure = null ;
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
			
			String claimedAmtFrom = formDTO.getClaimedAmtFrom() != null ? formDTO.getClaimedAmtFrom() : null;
			String claimedAmtTo =  formDTO.getClaimedAmtTo() != null ? formDTO.getClaimedAmtTo() : null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			List<PASearchPreauthTableDTO> processEnhancementTableDTO = new ArrayList<PASearchPreauthTableDTO>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PE_CURRENT_QUEUE);
			mapValues.put(SHAConstants.USER_ID, userName);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		//	mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
			

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
			
			if(null != formDTO.getTreatmentType())
			{
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
			
            Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			  Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			    Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
				
				//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			    Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				//taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
				
					if (null != taskProcedure) {
						for (Map<String, Object> outPutArray : taskProcedure) {
//							String keyValue = (String) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
							
							Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
							String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
							if(claimedAmount != null){
								claimedAmountMap.put(keyValue, Double.valueOf(claimedAmount));
							}
							workFlowMap.put(keyValue,outPutArray);
							
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
				resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
				List<Preauth> pageItemList = resultList;
				 processEnhancementTableDTO = PAEnhancementMapper.getInstance()
						.getProcessEnhancement(pageItemList);
				 
				 for (PASearchPreauthTableDTO dto : processEnhancementTableDTO) {
						if(dto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(dto.getProductKey())){
							if(dto.getPaPatientName() != null){
								dto.setInsuredPatientName(dto.getPaPatientName());
							}
						}
					}

				 
				 for (PASearchPreauthTableDTO tableDto : processEnhancementTableDTO) {
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
				// Implementing list to retreive hospital name and network hosp type.
				ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
				List<Long>  hospTypeList = new ArrayList<Long>();
				while (iterPreAuth.hasNext())
				{
					 Preauth preAuth= iterPreAuth.next();
					 /*MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
					 Long hospitalTypeId = hospTypeInfo.getKey();*/
					 Long hospitalTypeId = preAuth.getIntimation().getHospital();
					 hospTypeList.add(hospitalTypeId);
				}
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<PASearchPreauthTableDTO> searchEnhancementTableDTOForHospitalInfoList = new ArrayList<PASearchPreauthTableDTO>();				
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				searchEnhancementTableDTOForHospitalInfoList = PAEnhancementMapper.getHospitalInfoList(resultListForHospitalInfo);
				
				/**
				 * Method to populate speciality for a given pre auth key
				 * */
				
//				Map specialityMap = SHAUtils.getSpecialityInformation(entityManager, keys);
				
				/**
				 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
				 * */
				
			//	DBCalculationService dbCalculationService = new DBCalculationService();
				for (PASearchPreauthTableDTO objSearchProcessEnhancementTableDTO : processEnhancementTableDTO )
				{
					Long lPolicyKey = objSearchProcessEnhancementTableDTO.getPolicyKey();
					
					Long insuredId = objSearchProcessEnhancementTableDTO.getInsuredId();
					Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey,"P");
					Long insuredKey = objSearchProcessEnhancementTableDTO.getInsuredKey();
					Long productKey = objSearchProcessEnhancementTableDTO.getProductKey();

					objSearchProcessEnhancementTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForPAHealth(insuredKey , objSearchProcessEnhancementTableDTO.getClaimKey(),productKey,ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI));
		
				//	objSearchProcessEnhancementTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessEnhancementTableDTO.getKey()));
					
					/*if(objSearchProcessEnhancementTableDTO.getHumanTask() != null && objSearchProcessEnhancementTableDTO.getHumanTask().getPayloadCashless() != null
							&& objSearchProcessEnhancementTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt() != null){
						objSearchProcessEnhancementTableDTO.setClaimedAmountAsPerBill(objSearchProcessEnhancementTableDTO.getHumanTask().getPayloadCashless().getPreAuthReq().getPreAuthAmt());
					}
					objSearchProcessEnhancementTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessEnhancementTableDTO.getKey()));*/
					objSearchProcessEnhancementTableDTO.setSpeciality(getSpecialityType(objSearchProcessEnhancementTableDTO.getClaimKey()));
					objSearchProcessEnhancementTableDTO.setUsername(userName);
					objSearchProcessEnhancementTableDTO.setPassword(passWord);
					
					DocUploadToPremia premiaData = getUploadedDataDocument(objSearchProcessEnhancementTableDTO.getIntimationNo());
					if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
						objSearchProcessEnhancementTableDTO.setEnhancementReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));
					
					if(null != objSearchProcessEnhancementTableDTO.getDocReceivedTimeForMatch())
						//objSearchProcessEnhancementTableDTO.setStrDocReceivedTimeForMatch(String.valueOf(objSearchProcessEnhancementTableDTO.getDocReceivedTimeForMatch()));
						objSearchProcessEnhancementTableDTO.setDocReceivedTimeForMatchDate(objSearchProcessEnhancementTableDTO.getDocReceivedTimeForMatch());
					for (PASearchPreauthTableDTO objSearchProcessEnhancementTableDTOForHospitalInfo : searchEnhancementTableDTOForHospitalInfoList)
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
						if(objSearchProcessEnhancementTableDTO.getHospitalTypeId() != null && 
								objSearchProcessEnhancementTableDTOForHospitalInfo.getKey() != null &&
								objSearchProcessEnhancementTableDTO.getHospitalTypeId().equals(objSearchProcessEnhancementTableDTOForHospitalInfo.getKey()))
						{
							objSearchProcessEnhancementTableDTO.setHospitalName(objSearchProcessEnhancementTableDTOForHospitalInfo.getHospitalName());
							objSearchProcessEnhancementTableDTO.setNetworkHospType(objSearchProcessEnhancementTableDTOForHospitalInfo.getHospitalTypeName());
							break;
						}
					}
					
				}			
			}
		
			
			
		
		Page<PASearchPreauthTableDTO> page = new Page<PASearchPreauthTableDTO>();
		Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
		
		for (PASearchPreauthTableDTO searchPreauthTableDTO : processEnhancementTableDTO) {
			searchPreauthTableDTO.setImsUser(formDTO.getImsUser());
		}
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(processEnhancementTableDTO);
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
		query.setParameter("docType", SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT);
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

	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return Preauth.class;
	}
	
	
	
}
