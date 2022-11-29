package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

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
import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.DBCalculationService;
@Stateless
public class PhysicalDocumentReceivedMakerService  extends AbstractDAO<PhysicalDocumentVerification>{



	@PersistenceContext
	protected EntityManager entityManager;

	public PhysicalDocumentReceivedMakerService() {
		super();
	}

	public Page<PhysicalDocumentReceivedMakerTableDTO> search(
			PhysicalDocumentReceivedMakerFormDTO searchFormDTO, String userName,
			String passWord,String screenName) {
		List<PhysicalDocumentVerification> physicalDocumentList = new ArrayList<PhysicalDocumentVerification>();
		try {
			String intimationNo = null != searchFormDTO && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO
					.getIntimationNo() : null;
			String policyNo = null != searchFormDTO && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO
					.getPolicyNo() : null;

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<PhysicalDocumentVerification> criteriaQuery = criteriaBuilder
					.createQuery(PhysicalDocumentVerification.class);

			Root<PhysicalDocumentVerification> root = criteriaQuery.from(PhysicalDocumentVerification.class);

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
			
//			Predicate condition8 = criteriaBuilder.equal(root.<PhysicalDocumentVerification>get("claim").<Intimation> get("intimation").<Long>get("lobId"), ReferenceTable.HEALTH_LOB_KEY);
//			conditionList.add(condition8);	
			List<Long> myStatusList = new ArrayList<Long>();
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList
							.toArray(new Predicate[] {})));
			// }
			final TypedQuery<PhysicalDocumentVerification> typedQuery = entityManager
					.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 1;
			}
			/*
			listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();*/
			
			physicalDocumentList = typedQuery.getResultList();
			List<PhysicalDocumentVerification> listOfPhysicalDocument = new ArrayList<PhysicalDocumentVerification>();
			for (PhysicalDocumentVerification physicalDocument : physicalDocumentList) {
				if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
					if(physicalDocument.getStatus() != null && 
							physicalDocument.getStatus().getKey().equals(ReferenceTable.PAYMENT_MAKER_VERIFIED_STATUS)){
						listOfPhysicalDocument.add(physicalDocument);
					}
				}else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT)){
					if(physicalDocument.getStatus() != null && 
							physicalDocument.getStatus().getKey().equals(ReferenceTable.PAYMENT_VERIFICATION_PENDING_STATUS)){
						listOfPhysicalDocument.add(physicalDocument);
					}
				}
			}
			List<PhysicalDocumentVerification> doList = listOfPhysicalDocument;
			List<PhysicalDocumentReceivedMakerTableDTO> tableDTO = PhysicalDocumentReceivedMakerMapper.getInstance()
					.getIntimationDTO(doList);
			tableDTO = getHospitalDetails(tableDTO);
			List<PhysicalDocumentReceivedMakerTableDTO> result = new ArrayList<PhysicalDocumentReceivedMakerTableDTO>();

			Boolean flag = true;

			for (PhysicalDocumentReceivedMakerTableDTO searchAddAditionalDocoumentTableDTO : tableDTO) {
				searchAddAditionalDocoumentTableDTO.setUsername(userName);
				searchAddAditionalDocoumentTableDTO.setPassword(passWord);
				searchAddAditionalDocoumentTableDTO.setScreenName(screenName);
				for (PhysicalDocumentReceivedMakerTableDTO searchAddAditionalDocoumentTableDTOResult : result) {
					if (searchAddAditionalDocoumentTableDTOResult
							.getIntimationNo().equalsIgnoreCase(
									searchAddAditionalDocoumentTableDTO
											.getIntimationNo())) {
						flag = false;
					}
				}
				if (flag) {
					result.add(searchAddAditionalDocoumentTableDTO);					
				}
				flag = true;
			}

			// result.addAll(tableDTO);
			Page<PhysicalDocumentReceivedMakerTableDTO> page = new Page<PhysicalDocumentReceivedMakerTableDTO>();
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

	@Override
	public Class<PhysicalDocumentVerification> getDTOClass() {
		// TODO Auto-generated method stub
		return PhysicalDocumentVerification.class;
	}


	private List<PhysicalDocumentReceivedMakerTableDTO> getHospitalDetails(
			List<PhysicalDocumentReceivedMakerTableDTO> tableDTO) {
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

	public ClaimPayment getClaimPaymentByRodKey(Long rodkey) {
	    Query query = entityManager.createNamedQuery("ClaimPayment.findByRodKey");
	    query.setParameter("rodKey", rodkey);
	    List<ClaimPayment> claimPayment = (List<ClaimPayment>)query.getResultList();
	    
	    if(claimPayment != null && ! claimPayment.isEmpty()){
	            for (ClaimPayment claimPayment2 : claimPayment) {
	                    entityManager.refresh(claimPayment2);
	            }
	            return claimPayment.get(0);
	    }
	            return null;
	    
	}

}
