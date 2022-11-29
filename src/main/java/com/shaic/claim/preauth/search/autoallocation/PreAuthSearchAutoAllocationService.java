package com.shaic.claim.preauth.search.autoallocation;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class PreAuthSearchAutoAllocationService extends AbstractDAO <Preauth> {
	
	//@Inject
	//private PreauthService preauthService;
	//@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	//protected EntityManager entityManager;
	 private final Logger log = LoggerFactory.getLogger(PreAuthSearchAutoAllocationService.class);
	
	
	public PreAuthSearchAutoAllocationService()
	{
		super();
	}
	
	@SuppressWarnings("unused")
	
	
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
			List<Map<String, Object>> taskProcedure = null ;
			
			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());
			
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
		    
		   
		    List<SearchPreauthTableDTO> searchPreAuthTableDTO = new ArrayList<SearchPreauthTableDTO>();
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
			List<Long>  hospTypeList = new ArrayList<Long>();
			
			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] objectArray = (Object[])setMapValues[0];
			objectArray[21] = "";
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.getTaskProcedureForPreauthAutoAllocation(userName);	
			
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
						String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
						Long intimationKey = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
						Long claimKey = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						if(keyValue != null && keyValue != 0){
						workFlowMap.put(keyValue,outPutArray);
						}
						else{
							workFlowMap.put(intimationKey,outPutArray);	
						}
						if(claimedAmount != null){
							claimedAmountMap.put(keyValue, Double.valueOf(claimedAmount));
						}
						
						 totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
						 
							 keys.add(keyValue);
					}
				}		
 			
 				
				List<Preauth> resultList = new ArrayList<Preauth>();	
//				if( null != keys && 0 != keys.size())
//				{ 
					
					for (Long preauthKeys : keys) {
						List<Long> keyValues = new ArrayList<Long>();
						if(!preauthKeys.equals(0L)){
							keyValues.add(preauthKeys);
						List<Preauth> preauthList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keyValues);
						if(preauthList != null && preauthList.size()>0){
							resultList.add(preauthList.get(0));
						}						
						}
					}
					
					List<Preauth> pageItemList = resultList;
					searchPreAuthTableDTO = PreauthMapper.getInstance()
							.getProcessPreAuth(pageItemList);
					
					if(taskProcedure != null && !taskProcedure.isEmpty() && keys.get(0) == 0L) {
							SearchPreauthTableDTO bypassDto = new  SearchPreauthTableDTO();
							if (null != taskProcedure) {
								for (Map<String, Object> outPutArray : taskProcedure) {
									Long hospitalTypeID = (Long) outPutArray.get(SHAConstants.HOSPITAL_KEY);
									hospTypeList.add(hospitalTypeID);
									Long claimKey = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
									String intimationNo =  outPutArray.get(SHAConstants.INTIMATION_NO).toString();
									Intimation intimation = getIntimationByNo(intimationNo);
									bypassDto.setKey(intimation.getKey());
									bypassDto.setPolicyKey(intimation.getPolicy().getKey());
									bypassDto.setClaimKey(claimKey);
									bypassDto.setInsuredId(intimation.getInsured().getInsuredId());
									bypassDto.setInsuredKey(intimation.getInsured().getKey());
							searchPreAuthTableDTO.add(bypassDto);
					}	
				 }
			  }
							
					
					for (SearchPreauthTableDTO tableDto : searchPreAuthTableDTO) {
						tableDto.setIsPreauthAutoAllocationQ(true);
						if(tableDto.getKey() != null){
							Object workflowKey = workFlowMap.get(tableDto.getKey());
							Map<String, Object> wrkFlowMap = (Map<String, Object>) workflowKey;
							String currentQ = (String)wrkFlowMap.get(SHAConstants.CURRENT_Q);
							if(currentQ.equalsIgnoreCase(SHAConstants.PP_CURRENT_QUEUE))
							{
								tableDto.setType("Preauth(New)");
								tableDto.setPreauthCurrentQ(true);
							}else{
								
								tableDto.setType("Enhancement");
								tableDto.setPreauthCurrentQ(false);
							}
							
							tableDto.setDbOutArray(workflowKey);
							
//							Portal Flag updated in cashless table
							Map<String, Object> outPutArray = (Map<String, Object>) workFlowMap.get(tableDto.getKey());
							if(outPutArray != null && outPutArray.get(SHAConstants.RRC_ELIGIBILITY_TYPE) != null){
								
								Long nhpUpdKey = (Long)outPutArray.get(SHAConstants.RRC_ELIGIBILITY_TYPE);
								tableDto.setNhpUpdDocumentKey(nhpUpdKey);
							}
							
							
							
							Double claimedAmt = claimedAmountMap.get(tableDto.getKey());
							tableDto.setClaimedAmountAsPerBill(claimedAmt);
						}
						
					}
					// Implementing list to retreive hospital name and network hosp type.
					ListIterator<Preauth> iterPreAuth = pageItemList.listIterator();
					
					
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
				
//				}
			
				SHAUtils.setClearReferenceData(mapValues);
				SHAUtils.setClearMapValue(workFlowMap);
				SHAUtils.setClearMapDoubleValue(claimedAmountMap);
				
			
			Page<SearchPreauthTableDTO> page = new Page<SearchPreauthTableDTO>();
			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			
			ImsUser imsUser = formDTO.getImsUser();
				
			String[] userRoleList = imsUser.getUserRoleList();
			//page.setPageNumber(pagedList.getPageNumber());
			for (SearchPreauthTableDTO searchPreauthTableDTO2 : searchPreAuthTableDTO) {
				
				searchPreauthTableDTO2.setImsUser(imsUser);
                
				
			}
			page.setPageItems(searchPreAuthTableDTO);
			page.setIsDbSearch(Boolean.TRUE);
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
	
	
	public SearchPreauthTableDTO getAutoAllocationDetails(String userName){
		
		DBCalculationService dbcalculationService = new DBCalculationService();
		SearchPreauthTableDTO detailsForAutoAlloctionForUser = dbcalculationService.getDetailsForAutoAlloctionForUser(userName);
		
		return detailsForAutoAlloctionForUser;
		
	}
	
	public SearchPreauthTableDTO getCompletedCase(String userName){
		
		SearchPreauthTableDTO searchDto = new SearchPreauthTableDTO();
		DBCalculationService dbcalculationService = new DBCalculationService();
		
		List<Map<String, Object>> detailsForAutoAlloctionForUser = dbcalculationService.getTaskProcedureForPreauthAutoAllocation(userName);
		if(!detailsForAutoAlloctionForUser.isEmpty()){
			 Map<String, Object> map = detailsForAutoAlloctionForUser.get(0);
			 Integer completedCase = (Integer)map.get(SHAConstants.TOTAL_RECORDS);
			 searchDto.setCompletedValue(completedCase);
		}
		return searchDto;
		
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
