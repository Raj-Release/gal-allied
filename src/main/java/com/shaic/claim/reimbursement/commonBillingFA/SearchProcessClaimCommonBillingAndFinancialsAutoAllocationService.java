package com.shaic.claim.reimbursement.commonBillingFA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsMapper;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class SearchProcessClaimCommonBillingAndFinancialsAutoAllocationService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private MasterService masterService;

	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	private final Logger log = LoggerFactory.getLogger(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationService.class);
	
	public  Page<SearchProcessClaimFinancialsTableDTO> search(
			SearchProcessClaimFinancialsTableDTO searchFormDTO,
			String userName, String passWord) {
		try 
		{
			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME FOR COMMON BILLING FA AUTOALLOCATION%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());

			List<Map<String, Object>> taskProcedure = null ;		
			Integer totalRecords = 0; 
			List<Long> keys = new ArrayList<Long>();
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> rodList = new ArrayList<Long>();
			Long rodKey = null;
			String intimationNo = null;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.USER_ID, userName);
			if(searchFormDTO != null && searchFormDTO.getScreenName() != null && 
					searchFormDTO.getScreenName().equalsIgnoreCase(SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION)){
				taskProcedure = dbCalculationService.getTaskForFinancialApprovalAutoAllocation(userName);	
			}else {
			taskProcedure = dbCalculationService.getTaskProcedureForBillingFAAutoAllocation(userName);
			}
			if (null != taskProcedure && !taskProcedure.isEmpty()) {
				for (Map<String, Object> outPutArray : taskProcedure) {
//					Long claimKey = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
//					keys.add(claimKey);
//					Long keyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
//					workFlowMap.put(keyValue,outPutArray);
//					totalRecords = (Integer) outPutArray
//							.get(SHAConstants.TOTAL_RECORDS);
					Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
					String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
					workFlowMap.put(keyValue,outPutArray);							
					intimationNoList.add(intimationNumber);
					rodList.add(keyValue);

				}	
			}	
//			if( null != keys && 0 != keys.size())	
//			{
//
//				SearchProcessClaimFinancialsTableDTO resultBean = new SearchProcessClaimFinancialsTableDTO();
//				if (null != keys) {
//					resultBean.setClaimKey(keys.get(0));
//					resultBean.setPassword(passWord);
//					Object workflowKey = workFlowMap.get(keys.get(0));
//					Map<String,Object> work = (Map<String, Object>) workflowKey;
//					resultBean.setDbOutArray(workflowKey);
//					BillingFaAutoAllocationTableDTOs.add(resultBean);
//				}
//
//			}
			List<SearchProcessClaimFinancialsTableDTO> tableDTO = new ArrayList<SearchProcessClaimFinancialsTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				if(index < rodList.size()){
				intimationNo = intimationNoList.get(index);
				rodKey = rodList.get(index);
				tableDTO.addAll(getIntimationData(intimationNo, rodKey,userName));
				}
			}
			SHAUtils.setClearReferenceData(mapValues);
			SHAUtils.setClearMapValue(workFlowMap);

			Page<SearchProcessClaimFinancialsTableDTO> page = new Page<SearchProcessClaimFinancialsTableDTO>();

			page.setPageItems(tableDTO);
			page.setIsDbSearch(Boolean.TRUE);		
			page.setTotalRecords(totalRecords);

			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public List<SearchProcessClaimFinancialsTableDTO> getIntimationData(String intimationNo,Long rodKey,String userName/*, HumanTask humanTask,Integer taskNumber*/){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		List<Intimation> intimationsList = new ArrayList<Intimation>(); 
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(intimationNo != null || !intimationNo.isEmpty()){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		
			for(Intimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Intimation> doList = intimationsList;
			List<SearchProcessClaimFinancialsTableDTO> tableDTO =  SearchProcessClaimFinancialsMapper.getInstance().getIntimationDTO(doList);
			
	        for (SearchProcessClaimFinancialsTableDTO searchProcessClaimFinancialsTableDTO : tableDTO) {
	        	
	        	if(searchProcessClaimFinancialsTableDTO.getIntimationNo() != null){
					Intimation intimationByNo = getIntimationByNo(searchProcessClaimFinancialsTableDTO.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						searchProcessClaimFinancialsTableDTO.setColorCode(colorCodeForGMC);
					}
				}
	        	int i=0;
	        	String productName = searchProcessClaimFinancialsTableDTO.getProductName();
				productName = productName + "/"+doList.get(i).getPolicy().getProduct().getCode();
				searchProcessClaimFinancialsTableDTO.setProductName(productName);
				searchProcessClaimFinancialsTableDTO.setUsername(userName);
	        	Claim claimByKey = getClaimByIntimation(searchProcessClaimFinancialsTableDTO.getKey());
	        	searchProcessClaimFinancialsTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
	        	if(searchProcessClaimFinancialsTableDTO.getCpuCode() != null){
	        		
					Long cpuCode = searchProcessClaimFinancialsTableDTO.getCpuCode();
					searchProcessClaimFinancialsTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
				if(claimByKey != null) {
					searchProcessClaimFinancialsTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchProcessClaimFinancialsTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchProcessClaimFinancialsTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					
					if(searchProcessClaimFinancialsTableDTO.getCrmFlagged() != null){
						if(searchProcessClaimFinancialsTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchProcessClaimFinancialsTableDTO.setColorCodeCell("OLIVE");
							}
							searchProcessClaimFinancialsTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimFinancialsTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimFinancialsTableDTO.setColorCodeCell("CRMVIP");
					}
					
				}
				searchProcessClaimFinancialsTableDTO.setIsAutoAllocationTrue(true);
			}
			
			tableDTO = getHospitalDetails(tableDTO/*, humanTask*/);
			tableDTO = getclaimNumber(tableDTO, rodKey/*, humanTask,taskNumber*/);
		
		return tableDTO;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

	private List<SearchProcessClaimFinancialsTableDTO> getHospitalDetails(
			List<SearchProcessClaimFinancialsTableDTO> tableDTO/*, HumanTask humanTask*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 if(hospitalDetail.getFspFlag()!=null){
					 tableDTO.get(index).setFspFlag(hospitalDetail.getFspFlag());
				 }
				 if( tableDTO.get(index).getFspFlag()!=null){
					 if(tableDTO.get(index).getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						 tableDTO.get(index).setColorCode("YELLOW");
					 }
				 }
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private List<SearchProcessClaimFinancialsTableDTO> getclaimNumber(List<SearchProcessClaimFinancialsTableDTO> intimationList, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
		Claim a_claim = null;
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getKey());
			
			if (intimationList.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index).getKey());
				try{
					
						a_claim = (Claim) findByIntimationKey.getSingleResult();
						if(a_claim != null){
							intimationList.get(index).setRodKey(rodKey);
							
							Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
							
							Object workflowKey = workFlowMap.get(intimationList.get(index).getRodKey());
							Map<String, Object> outPutArray = (Map<String, Object>)workflowKey;
							String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
							if(claimedAmount != null){
								intimationList.get(index).setClaimedAmountAsPerBill(Double.valueOf(claimedAmount));
							}
							
							intimationList.get(index).setDbOutArray(workflowKey);
							
							
							
							 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
									 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
								 intimationList.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
							 }
							
							intimationList.get(index).setOriginatorID(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setOriginatorName(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setClaimKey(a_claim.getKey());
							if(a_claim.getClaimType() != null){
								intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							}
							String type = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getStatus().getProcessValue() : "";
							intimationList.get(index).setType(type);
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}
	
	private Reimbursement getType(Long key){
		try{
		Query findType = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", key);
		Reimbursement reimbursement = (Reimbursement) findType.getSingleResult();
		return reimbursement;
		}catch(Exception e){
			
		}
		return null;
	}
	

	private MastersValue getLOBValue(Long getlOBId) {
		try{
		Query query = entityManager
				.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", getlOBId);
		MastersValue value = (MastersValue) query.getSingleResult();
	    return value;
		}catch(Exception e){
			
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
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
