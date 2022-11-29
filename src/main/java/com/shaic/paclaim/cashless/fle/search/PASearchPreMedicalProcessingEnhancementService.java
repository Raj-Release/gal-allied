package com.shaic.paclaim.cashless.fle.search;

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
import com.shaic.domain.Claim;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class PASearchPreMedicalProcessingEnhancementService extends AbstractDAO <Claim>  {
	
	public PASearchPreMedicalProcessingEnhancementService()
	{
		super();
	}
	
	@SuppressWarnings("unused")
	public Page<PASearchPreMedicalProcessingEnhancementTableDTO> search(PASearchPreMedicalProcessingEnhancementFormDTO formDTO, String userName, String passWord)
	{
		
		List<Map<String, Object>> taskProcedure = null ;
		try 
		{
			String strIntimationNo = "";
			String strPolicyNo = "";
			String strType = null;
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
		//	PayloadBOType payloadBO = null;
			
			if (formDTO.getSource() != null && formDTO.getSource().getId() != null && formDTO.getSource().getId().equals(ReferenceTable.REFER_TO_FLE)){
				source = "FLE";
			}
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.FLE_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
			
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
			if(null != formDTO.getNetworkHospitalType()) {
				
				if(formDTO.getNetworkHospitalType().getId() != null && formDTO.getNetworkHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospitalType().getValue();
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
					
				}else if(formDTO.getNetworkHospitalType().getId() != null && formDTO.getNetworkHospitalType().getId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospitalType().getValue();
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
					
				}else if(formDTO.getNetworkHospitalType().getId() != null && formDTO.getNetworkHospitalType().getId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
					strNetworkHospType=formDTO.getNetworkHospitalType().getValue();
				
					mapValues.put(SHAConstants.NETWORK_TYPE, strNetworkHospType);
				
				}
				
			}
			
			if(cpuCode != null){
				
				mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				
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
			
			/**
			 * BPM integration starts.
			 * 
			 * */		
			
			
			
			Pageable pageable = formDTO.getPageable();
			
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			if(formDTO.getSortOrder() != null && formDTO.getSortOrder().getValue() != null) {
		    	
				pageable.setOrderBy(formDTO.getSortOrder().getValue());
			
			}				
			
			
			List<PASearchPreMedicalProcessingEnhancementTableDTO> SearchPreMedicalProcessingEnhancementTableDTO = new ArrayList<PASearchPreMedicalProcessingEnhancementTableDTO>();
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			
			//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
		//	taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						workFlowMap.put(keyValue,outPutArray);
						
						 totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
						 
							 keys.add(keyValue);
							}

					}

				if( null != keys && 0 != keys.size())	
				{
					List<Claim> resultList = new ArrayList<Claim>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
					List<Claim> pageItemList = resultList;
					if(!resultList.isEmpty())
					{
						SearchPreMedicalProcessingEnhancementTableDTO = PASearchPreMedicalProcessingEnhancementMapper.getInstance()
								.getSearchPreMedicalProcessEnhancement(pageItemList);
						
						for (PASearchPreMedicalProcessingEnhancementTableDTO dto : SearchPreMedicalProcessingEnhancementTableDTO) {
							if(dto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(dto.getProductKey())){
								if(dto.getPaPatientName() != null){
									dto.setInsuredPatientName(dto.getPaPatientName());
								}
							}
						}

						
						for (PASearchPreMedicalProcessingEnhancementTableDTO tableDto : SearchPreMedicalProcessingEnhancementTableDTO) {
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
							 Claim objClaim= iterClaim.next();
							 /*MastersValue hospTypeInfo = preAuth.getIntimation().getHospitalType();
							 Long hospitalTypeId = hospTypeInfo.getKey();*/
							 Long hospitalTypeId = objClaim.getIntimation().getHospital();
							 hospTypeList.add(hospitalTypeId);
						}
						List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
						List<PASearchPreMedicalProcessingEnhancementTableDTO> searchPreMedicalProcessingEnhancementHospitalInfoList = new ArrayList<PASearchPreMedicalProcessingEnhancementTableDTO>();
						resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
						searchPreMedicalProcessingEnhancementHospitalInfoList = PASearchPreMedicalProcessingEnhancementMapper.getHospitalInfoList(resultListForHospitalInfo);
							/**
							 * Creating instance for DBCalculationService to access method which would provide BalanceSI.
							 * */
					//	DBCalculationService dbCalculationService = new DBCalculationService();
						for (PASearchPreMedicalProcessingEnhancementTableDTO objSearchProcessPreMedicalEnhancementTableDTO : SearchPreMedicalProcessingEnhancementTableDTO )
						{
							Long lPolicyKey = objSearchProcessPreMedicalEnhancementTableDTO.getPolicyKey();
							//int iSumInsured = objSearchProcessPreMedicalEnhancementTableDTO.getSumInsured();
							
							Long insuredId = objSearchProcessPreMedicalEnhancementTableDTO.getInsuredId();
							Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey,"P");
							Long insuredKey = objSearchProcessPreMedicalEnhancementTableDTO.getInsuredKey();
							Long productKey = objSearchProcessPreMedicalEnhancementTableDTO.getProductKey();

							objSearchProcessPreMedicalEnhancementTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForPAHealth(insuredKey , objSearchProcessPreMedicalEnhancementTableDTO.getKey(),productKey,ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI));

						/*	objSearchProcessPreMedicalEnhancementTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessPreMedicalEnhancementTableDTO.getKey()));
							objSearchProcessPreMedicalEnhancementTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessPreMedicalEnhancementTableDTO.getKey()));*/
							
							objSearchProcessPreMedicalEnhancementTableDTO.setUsername(userName);
							objSearchProcessPreMedicalEnhancementTableDTO.setPassword(passWord);
							DocUploadToPremia premiaData = getUploadedDataDocument(objSearchProcessPreMedicalEnhancementTableDTO.getIntimationNo());
							if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId())
								objSearchProcessPreMedicalEnhancementTableDTO.setDocsReceivedDate(premiaData.getPfdUpFfaxSubmitId());
							//objSearchProcessPreMedicalEnhancementTableDTO.setDocsRecievedTime(String.valueOf(premiaData.getPfdUpPremiaAckDt()));
						//	objSearchProcessPreMedicalEnhancementTableDTO.setDocReceivedTimeForReg(premiaData.getPfdUpFfaxSubmitId());
							if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
								objSearchProcessPreMedicalEnhancementTableDTO.setEnhancementReqAmt(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
							
							
							for (PASearchPreMedicalProcessingEnhancementTableDTO objSearchProcessPreMedicalEnhancementTableDTOForHospitalInfo : searchPreMedicalProcessingEnhancementHospitalInfoList)
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
									if(objSearchProcessPreMedicalEnhancementTableDTO.getHospitalTypeId() != null && 
											objSearchProcessPreMedicalEnhancementTableDTOForHospitalInfo.getKey() != null && 
											objSearchProcessPreMedicalEnhancementTableDTO.getHospitalTypeId().equals(objSearchProcessPreMedicalEnhancementTableDTOForHospitalInfo.getKey()))
									{
										objSearchProcessPreMedicalEnhancementTableDTO.setHospitalName(objSearchProcessPreMedicalEnhancementTableDTOForHospitalInfo.getHospitalName());
										objSearchProcessPreMedicalEnhancementTableDTO.setNetworkHospitalType(objSearchProcessPreMedicalEnhancementTableDTOForHospitalInfo.getHospitalTypeName());
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
								for(PASearchPreMedicalProcessingEnhancementTableDTO objPreMedicalProcessingEnhTbl : SearchPreMedicalProcessingEnhancementTableDTO)
								{
									intimationNumberList.add(objPreMedicalProcessingEnhTbl.getIntimationNo());
								}
							}													 
					}
				}
			
			List<PASearchPreMedicalProcessingEnhancementTableDTO> filterDTO = new ArrayList<PASearchPreMedicalProcessingEnhancementTableDTO>();
			
			for (Long claimKeyBPMN : keys) {
				
				for (PASearchPreMedicalProcessingEnhancementTableDTO processPreMedicalTableDTO2 : SearchPreMedicalProcessingEnhancementTableDTO) {
					if(processPreMedicalTableDTO2.getKey().equals(claimKeyBPMN)){
						filterDTO.add(processPreMedicalTableDTO2);
						break;
					}
				}
				
			}
			
			Page<PASearchPreMedicalProcessingEnhancementTableDTO> page = new Page<PASearchPreMedicalProcessingEnhancementTableDTO>();
			Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
			if(pagedList != null){
				page.setPageNumber(pagedList.getPageNumber());
			}
			page.setPageItems(filterDTO);
			if(pagedList != null){
				page.setPagesAvailable(pagedList.getPagesAvailable());
			}
			page.setTotalRecords(totalRecords);
			
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
	public Claim getSearchPreMedicalProcessingEnhancement (Long searchPreMedicalProcessingEnhancementKey) {

//		Query findByKey = entityManager
//				.createNamedQuery("Preauth.findByHospitalKey").setParameter(
//						"hospitalKey", acknowledHospitalKey);
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Claim> searchPreMedicalProcessingEnhancementList = (List<Claim>) findByKey
				.getResultList();

		if (!searchPreMedicalProcessingEnhancementList.isEmpty()) {
			return searchPreMedicalProcessingEnhancementList.get(0);

		}
		return null;
	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	
	

//	@Override
//	public Class<Claim> getDTOClass() {
//		return Claim.class;
//	}
	
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
	
	

}


