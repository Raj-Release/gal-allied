package com.shaic.claim.preauth.search.flpautoallocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.search.PreauthMapper;
import com.shaic.claim.preauth.search.autoallocation.PreAuthSearchAutoAllocationService;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.premedical.search.ProcessPreMedicalMapper;
import com.shaic.claim.premedical.search.ProcessPreMedicalService;
import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementMapper;
import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class FLPSearchAutoAllocationService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private ProcessPreMedicalService preMedicalService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private MasterService masterService;

	private final Logger log = LoggerFactory.getLogger(FLPSearchAutoAllocationService.class);


	public FLPSearchAutoAllocationService()
	{
		super();
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
	public Preauth getSearchPreAuthKey(Long preAuthSearchKey) {

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
	public Page<SearchFLPAutoAllocationTableDTO> search(SearchFLPAutoAllocationTableDTO formDTO, String userName, String passWord)
	{
		try 
		{
			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());

			List<Map<String, Object>> taskProcedure = null ;		
			Integer totalRecords = 0; 
			List<Long> keys = new ArrayList<Long>();
			List<SearchFLPAutoAllocationTableDTO> flpAutoAllocationTableDTOs = new ArrayList<SearchFLPAutoAllocationTableDTO>();

			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.USER_ID, userName);

			taskProcedure = dbCalculationService.getTaskProcedureForFLPAutoAllocation(userName);	
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
					Long keyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
					keys.add(keyValue);
					workFlowMap.put(keyValue,outPutArray);
					totalRecords = (Integer) outPutArray.get(SHAConstants.TOTAL_RECORDS);
				}
			}	
			if( null != keys && 0 != keys.size())	
			{
				List<Claim> resultList = new ArrayList<Claim>();
				resultList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, keys);
				if(resultList !=null && !resultList.isEmpty()){
					Map<String, Object> wrkFlowMap = (Map<String, Object>) workFlowMap.get(keys.get(0));
					String currentQ = (String)wrkFlowMap.get(SHAConstants.CURRENT_Q);
					SearchFLPAutoAllocationTableDTO flpAutoAllocationTableDTO = new SearchFLPAutoAllocationTableDTO();
					if(currentQ.equalsIgnoreCase(SHAConstants.FLP_CURRENT_QUEUE))
					{
						ProcessPreMedicalTableDTO preMedicalTableDTO = getPreMedicalDTO(workFlowMap,resultList,userName,passWord);
						flpAutoAllocationTableDTO.setPreMedicalTableDTO(preMedicalTableDTO);
						flpAutoAllocationTableDTO.setCurrentQ(currentQ);
						flpAutoAllocationTableDTOs.add(flpAutoAllocationTableDTO);
					}else if(currentQ.equalsIgnoreCase(SHAConstants.FLE_CURRENT_QUEUE)){
						SearchPreMedicalProcessingEnhancementTableDTO enhancementTableDTO = getProcessingEnhancementDTO(workFlowMap,resultList,userName,passWord);
						flpAutoAllocationTableDTO.setProcessingEnhancementTableDTO(enhancementTableDTO);
						flpAutoAllocationTableDTO.setCurrentQ(currentQ);
						flpAutoAllocationTableDTOs.add(flpAutoAllocationTableDTO);
					}
				}
			}
			SHAUtils.setClearReferenceData(mapValues);
			SHAUtils.setClearMapValue(workFlowMap);

			Page<SearchFLPAutoAllocationTableDTO> page = new Page<SearchFLPAutoAllocationTableDTO>();

			page.setPageItems(flpAutoAllocationTableDTOs);
			page.setIsDbSearch(Boolean.TRUE);		
			page.setTotalRecords(totalRecords);

			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	public ProcessPreMedicalTableDTO getPreMedicalDTO(Map<Long, Object> workFlowMap,List<Claim> pageItemList,String userName,String passWord){

		List<ProcessPreMedicalTableDTO> processPreMedicalTableDTOs = null;
		ProcessPreMedicalTableDTO processPreMedicalTableDTO;

		if(!pageItemList.isEmpty()) {

			processPreMedicalTableDTOs = ProcessPreMedicalMapper.getInstance().getProcessPreMedicalPreAuth(pageItemList);
			if(processPreMedicalTableDTOs !=null && !processPreMedicalTableDTOs.isEmpty()){

				processPreMedicalTableDTO = processPreMedicalTableDTOs.get(0);
				Claim claim = preMedicalService.getClaimByKey(processPreMedicalTableDTO.getKey());
				if(processPreMedicalTableDTO.getIntimationNo() != null){
					Intimation intimationByNo = preMedicalService.getIntimationByNo(processPreMedicalTableDTO.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = preMedicalService.getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						processPreMedicalTableDTO.setColorCode(colorCodeForGMC);
					}
				}
				if(processPreMedicalTableDTO.getKey() != null && workFlowMap !=null){
					Object workflowKey = workFlowMap.get(processPreMedicalTableDTO.getKey());
					Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(processPreMedicalTableDTO.getKey());
					if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM) != null){
						String cpuLimit = (String) outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
						if(cpuLimit != null && !cpuLimit.isEmpty() && (cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CPPA) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CP))){
							processPreMedicalTableDTO.setAboveCPULimitCorp(SHAConstants.CPU_ALLOCATION_ABOVE_LIMIT);	
						}
					}
					processPreMedicalTableDTO.setDbOutArray(workflowKey);
//					Portal Flag updated in cashless table
					if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null){
						
						String nhpUpdKey = (String) outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
						processPreMedicalTableDTO.setNhpUpdDocumentKey(Long.valueOf(nhpUpdKey));
					}
				}

				Long lPolicyNumber = processPreMedicalTableDTO.getPolicyKey();
				Long insuredId = processPreMedicalTableDTO.getInsuredId();
				Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"H");
				Long insuredKey = processPreMedicalTableDTO.getInsuredKey();


				if (claim != null) {
					processPreMedicalTableDTO.setCoverCode(claim.getClaimCoverCode());
					processPreMedicalTableDTO.setSubCoverCode(claim.getClaimSubCoverCode());
					processPreMedicalTableDTO.setSectionCode(claim.getClaimSectionCode());

					if(processPreMedicalTableDTO.getCrmFlagged() != null){
						if(processPreMedicalTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y")) {
								processPreMedicalTableDTO.setColorCodeCell("OLIVE");
							}
							processPreMedicalTableDTO.setCrmFlagged(null);
						}
					}
					if (claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						processPreMedicalTableDTO.setColorCodeCell("VIP");
					}
					if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y") 
							&& claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						processPreMedicalTableDTO.setColorCodeCell("CRMVIP");
					}

				}
				if(ReferenceTable.getGMCProductList().containsKey(processPreMedicalTableDTO.getProductKey())){
					processPreMedicalTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForGMC(lPolicyNumber, insuredKey, claim.getKey()));
				}
				else{
					processPreMedicalTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , processPreMedicalTableDTO.getKey(), sumInsured,processPreMedicalTableDTO.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
				}

				processPreMedicalTableDTO.setUsername(userName);
				processPreMedicalTableDTO.setPassword(passWord);

				DocUploadToPremia premiaData = getUploadedDataDocument(processPreMedicalTableDTO.getIntimationNo());
				if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId())
					processPreMedicalTableDTO.setDocReceivedTimeForMatchDate(premiaData.getPfdUpFfaxSubmitId());
				if(premiaData != null && null != premiaData.getPfdUpPremiaAckDt())
					processPreMedicalTableDTO.setDocReceivedTimeForRegDate(premiaData.getPfdUpPremiaAckDt());
				if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
					processPreMedicalTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));

				Long hospitalTypeId = claim.getIntimation().getHospital();		
				if(hospitalTypeId != null){
					Hospitals hospitals = masterService.getHospitalDetails(hospitalTypeId);
					if(hospitals !=null){
						processPreMedicalTableDTO.setHospitalName(hospitals.getName());
						processPreMedicalTableDTO.setNetworkHospType(hospitals.getHospitalTypeName());
					}	
				}	
				processPreMedicalTableDTO.setAutoAllocation(true);
				return processPreMedicalTableDTO;
			}
		}
		return null;
	}

	public SearchPreMedicalProcessingEnhancementTableDTO getProcessingEnhancementDTO(Map<Long, Object> workFlowMap,List<Claim> pageItemList,String userName,String passWord){

		List<SearchPreMedicalProcessingEnhancementTableDTO> searchPreMedicalProcessingEnhancementTableDTOs = null;


		if(!pageItemList.isEmpty())
		{
			searchPreMedicalProcessingEnhancementTableDTOs = SearchPreMedicalProcessingEnhancementMapper.getInstance()
					.getSearchPreMedicalProcessEnhancement(pageItemList);

			if(searchPreMedicalProcessingEnhancementTableDTOs !=null 
					&& !searchPreMedicalProcessingEnhancementTableDTOs.isEmpty()){

				SearchPreMedicalProcessingEnhancementTableDTO tableDto = searchPreMedicalProcessingEnhancementTableDTOs.get(0); 
				if(tableDto.getIntimationNo() != null){
					Intimation intimationByNo = preMedicalService.getIntimationByNo(tableDto.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = preMedicalService.getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						tableDto.setColorCode(colorCodeForGMC);
					}
				}
				if(tableDto.getKey() != null && workFlowMap !=null){
					Object workflowKey = workFlowMap.get(tableDto.getKey());
					Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(tableDto.getKey());
					if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM) != null){
						String cpuLimit = (String) outPutArray.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
						if(cpuLimit != null && !cpuLimit.isEmpty() && (cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CPPA) || cpuLimit.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CP))){
							tableDto.setAboveCPULimitCorp(SHAConstants.CPU_ALLOCATION_ABOVE_LIMIT);	
						}
					}
					tableDto.setDbOutArray(workflowKey);
//					Portal Flag updated in cashless table
					if(outPutArray != null && outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null){
						
						String nhpUpdKey = (String) outPutArray.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
						tableDto.setNhpUpdDocumentKey(Long.valueOf(nhpUpdKey));
					}
				}


				Long lPolicyKey = tableDto.getPolicyKey();
				Claim claim = preMedicalService.getClaimByKey(tableDto.getKey());

				if (claim != null) {
					tableDto.setCoverCode(claim.getClaimCoverCode());
					tableDto.setSubCoverCode(claim.getClaimSubCoverCode());
					tableDto.setSectionCode(claim.getClaimSectionCode());

					if(tableDto.getCrmFlagged() != null){
						if(tableDto.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y")) {
								tableDto.setColorCodeCell("OLIVE");
							}
							tableDto.setCrmFlagged(null);
						}
					}
					if (claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						tableDto.setColorCodeCell("VIP");
					}
					if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y") 
							&& claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						tableDto.setColorCodeCell("CRMVIP");
					}

				}

				Long insuredId = tableDto.getInsuredId();
				Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyKey,"H");
				Long insuredKey = tableDto.getInsuredKey();

				if((ReferenceTable.getGMCProductList().containsKey(tableDto.getProductKey()))){
					tableDto.setBalanceSI(dbCalculationService.getBalanceSIForGMC(lPolicyKey, insuredKey, claim.getKey()));
				}
				else{
					tableDto.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyKey, insuredKey , tableDto.getKey(), sumInsured,tableDto.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
				}
				tableDto.setUsername(userName);
				tableDto.setPassword(passWord);
				DocUploadToPremia premiaData = getUploadedDataDocumentForEnhancement(tableDto.getIntimationNo());
				if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId()){
					tableDto.setDocsReceivedDate(premiaData.getPfdUpFfaxSubmitId());
				}			
				if(premiaData != null && null != premiaData.getPfdUpFFAXAmt()){
					tableDto.setEnhancementReqAmt(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
				}
				Long hospitalTypeId = claim.getIntimation().getHospital();		
				if(hospitalTypeId != null){
					Hospitals hospitals = masterService.getHospitalDetails(hospitalTypeId);
					if(hospitals !=null){
						tableDto.setHospitalName(hospitals.getName());
						tableDto.setNetworkHospitalType(hospitals.getHospitalTypeName());
					}	
				}	
				tableDto.setAutoAllocation(true);
				return tableDto;
			}

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Claim> getClaimsByIntimationNO(String intimationNo) {
		Query query = entityManager.createNamedQuery("Claim.findByIntimationNo");
		query.setParameter("intimationNumber", intimationNo);	
		List<Claim> singleResult = (List<Claim>) query.getResultList();
		return singleResult;
	}
	
	private DocUploadToPremia getUploadedDataDocumentForEnhancement(String intimationNo)
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

}
