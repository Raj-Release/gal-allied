package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.NEFTQueryDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class SearchUploadNEFTDetailsService extends AbstractDAO<Reimbursement>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	

	public SearchUploadNEFTDetailsService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Page<SearchUploadNEFTDetailsTableDTO> search(
			SearchUploadNEFTDetailsFormDTO searchFormDTO, String userName,
			String passWord) {
		List<NEFTQueryDetails> neftQueryDetailsList = new ArrayList<NEFTQueryDetails>();
		try {
			String intimationNo = null != searchFormDTO && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO
					.getIntimationNo() : null;
			String policyNo = null != searchFormDTO && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO
					.getPolicyNo() : null;

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<NEFTQueryDetails> criteriaQuery = criteriaBuilder
					.createQuery(NEFTQueryDetails.class);

			Root<NEFTQueryDetails> root = criteriaQuery.from(NEFTQueryDetails.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<String> get("intimationId"), "%"
								+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder.like(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<Policy> get("policy")
								.<String> get("policyNumber"), "%" + policyNo
								+ "%");
				conditionList.add(condition2);
			}
			
//			Predicate condition8 = criteriaBuilder.equal(root.<Reimbursement>get("claim").<Intimation>get("intimation").<Long>get("lobId"), ReferenceTable.HEALTH_LOB_KEY);
			//Predicate condition8 = criteriaBuilder.notLike(root.<Reimbursement>get("claim").<Intimation>get("intimation").<String>get("processClaimType"), "%"+SHAConstants.PA_TYPE+"%");
			//conditionList.add(condition8);	
			List<Long> statusListKey = new ArrayList<Long>();
			statusListKey.add(ReferenceTable.NEFT_STATUS_PENDING_KEY);
			
			Predicate statusCondition =  root.<Status>get("status").get("key").in(statusListKey); 
			conditionList.add(statusCondition); 
			
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList
							.toArray(new Predicate[] {})));
			// }
			final TypedQuery<NEFTQueryDetails> typedQuery = entityManager
					.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 1;
			}
			
			neftQueryDetailsList = typedQuery.getResultList();
			List<NEFTQueryDetails> listOfNEFTDetails = new ArrayList<NEFTQueryDetails>();
			for (NEFTQueryDetails neftQueryDetails : neftQueryDetailsList) {
				if(isBatchNotCreated(neftQueryDetails.getReimbursement().getKey())){
					listOfNEFTDetails.add(neftQueryDetails);
				}
				
				
			}
			List<NEFTQueryDetails> doList = listOfNEFTDetails;
			List<SearchUploadNEFTDetailsTableDTO> tableDTO = SearchUploadNEFTDetailsMapper.getInstance()
					.getIntimationDTO(doList);
			tableDTO = getHospitalDetails(tableDTO);
			List<SearchUploadNEFTDetailsTableDTO> result = new ArrayList<SearchUploadNEFTDetailsTableDTO>();

			Boolean flag = true;

			for (SearchUploadNEFTDetailsTableDTO searchUploadNEFTDetailsTableDTO : tableDTO) {
				searchUploadNEFTDetailsTableDTO.setUsername(userName);
				searchUploadNEFTDetailsTableDTO.setPassword(passWord);
				for (SearchUploadNEFTDetailsTableDTO searchUploadNEFTDetailsTableDTOResult : result) {
					if (searchUploadNEFTDetailsTableDTOResult
							.getIntimationNo().equalsIgnoreCase(
									searchUploadNEFTDetailsTableDTO
											.getIntimationNo())) {
						flag = false;
					}
				}
				if (flag) {
					result.add(searchUploadNEFTDetailsTableDTO);					
				}
				flag = true;
			}

			Page<SearchUploadNEFTDetailsTableDTO> page = new Page<SearchUploadNEFTDetailsTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
			if(result.isEmpty()){
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;
	}




	private List<SearchUploadNEFTDetailsTableDTO> getHospitalDetails(
			List<SearchUploadNEFTDetailsTableDTO> tableDTO) {
		// TODO Auto-generated method stub

		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {

			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key",
					tableDTO.get(index).getHospitalNameID());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey
						.getSingleResult();
				if (hospitalDetail != null) {
					tableDTO.get(index).setHospitalName(
							hospitalDetail.getName());

					tableDTO.get(index).setHospitalCity(
							hospitalDetail.getCityId() + " ,"
									+ hospitalDetail.getCity());
					tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					Long cpuCode = null != getTmpCPUCode(tableDTO.get(index)
							.getCpuId()) ? getTmpCPUCode(
							tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
					tableDTO.get(index).setCpuCode(cpuCode);
				}
			} catch (Exception e) {
				continue;
			}

		}

		return tableDTO;
	
	}



	private TmpCPUCode getTmpCPUCode(Long cpuId) {
		// TODO Auto-generated method stub

		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
		} catch (Exception e) {

		}
		return null;
	
	}
	
	private Reimbursement getType(Long key) {
		try {
			Query findType = entityManager.createNamedQuery(
					"Reimbursement.findByKey").setParameter("primaryKey", key);
			Reimbursement reimbursement = (Reimbursement) findType
					.getSingleResult();
			return reimbursement;
		} catch (Exception e) {

		}
		return null;
	}
	
public Boolean getDBTaskForCurrentQ(Intimation intimation,String currentQ,Long reimbursementKey){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			 for (Map<String, Object> map : taskProcedure) {
				 Long keyValue = (Long) map.get(SHAConstants.PAYLOAD_ROD_KEY);
				 if(keyValue != null && keyValue.equals(reimbursementKey)){
					 return true;
				 }
			}
		} 
		return false;
	}



	@Override
	public Class<Reimbursement> getDTOClass() {
		// TODO Auto-generated method stub
		return Reimbursement.class;
	}
	
	private Boolean isBatchNotCreated(Long rodKey){
		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByRodKeyOrderByKey");
		query.setParameter("rodKey", rodKey);
		Boolean isAvailable = false;
		@SuppressWarnings("unchecked")
		List<ClaimPayment> paymentList = query.getResultList();
		if (paymentList != null && !paymentList.isEmpty()) {
			ClaimPayment claimPayment = paymentList.get(0);
			if(claimPayment != null){
				if(claimPayment.getBatchNumber() == null){
					isAvailable = true;
				}else{
					isAvailable = false;
				}
			}else{
				isAvailable = true;
			}
		}else{
			isAvailable = true;
		}
		return isAvailable;
	}
	
	private Boolean isMAapproved(Reimbursement reimbursement){
		if(reimbursement.getMedicalCompletedDate() != null){
			return Boolean.TRUE;
		}else{
			if(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		
	}


}
