package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class SearchClaimWiseApprovalService extends AbstractDAO<Claim>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PolicyService policyService;
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();
	
	private final Logger log = LoggerFactory.getLogger(SearchClaimWiseApprovalService.class);
	
	public  Page<SearchClaimWiseAllowApprovalDto> search(
			SearchClaimWiseAllowApprovalFormDto searchFormDTO,
			String userName, String passWord){
		
		List<Claim> listOfClaims = new ArrayList<Claim>();
		try{
			String intimationNo = searchFormDTO.getIntimationNo() != null && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo():null;
			String claimNo = searchFormDTO.getClaimNo() != null && !searchFormDTO.getClaimNo().isEmpty() ? searchFormDTO.getClaimNo() : null;
			String policyNo = searchFormDTO.getPolicyNo() != null && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			
			Root<Claim> root = criteriaQuery.from(Claim.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();

			if(intimationNo != null){
			Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
			conditionList.add(condition1);
			}
			if(claimNo != null){
			Predicate condition2 = criteriaBuilder.like(root.<String>get("claimId"), "%"+claimNo+"%");
			conditionList.add(condition2);
			}
			if(policyNo != null){
			Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
			conditionList.add(condition3);
			}
			if(intimationNo == null && claimNo == null && policyNo == null){
				criteriaQuery.select(root);
				}else{
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 0;
			}
			
			listOfClaims = typedQuery.getResultList();
			
			List<SearchClaimWiseAllowApprovalDto> tablelistValues = SearchClaimWiseApprovalMapper.getInstance().getClaimDTO(listOfClaims);
			tablelistValues = getHospitalDetails(tablelistValues);
			tablelistValues = getReimbursementDetails(tablelistValues);
			
			List<SearchClaimWiseAllowApprovalDto> resultList = new ArrayList<SearchClaimWiseAllowApprovalDto>();
			for (SearchClaimWiseAllowApprovalDto searchClaimWiseAllowApprovalDto : tablelistValues) {
				int i = 1;
				if(validateCancelPolicyApprovalStatus(searchClaimWiseAllowApprovalDto.getIntimationkey(),searchClaimWiseAllowApprovalDto.getPolicyNo())){
					searchClaimWiseAllowApprovalDto.setSerialNo(i);
					resultList.add(searchClaimWiseAllowApprovalDto);
					i++;
					/*resultList.addAll(tablelistValues);*/
				}
			}
			
			Page<SearchClaimWiseAllowApprovalDto> page = new Page<SearchClaimWiseAllowApprovalDto>();		
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(resultList.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (resultList.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(true);
	
			return page;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	
	private List<SearchClaimWiseAllowApprovalDto> getHospitalDetails(
			List<SearchClaimWiseAllowApprovalDto> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setCpuCode(String.valueOf(getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode()));

			 }
			 
			}catch(Exception e){
				continue;
			}
		
		}
				
		return tableDTO;
	}
	
	private List<SearchClaimWiseAllowApprovalDto> getReimbursementDetails(List<SearchClaimWiseAllowApprovalDto> tablelistValues){
		List<Reimbursement> reimbursement;
		for(int index = 0; index < tablelistValues.size(); index++){
			reimbursement = reimbursementService.getPreviousRODByClaimKey(tablelistValues.get(index).getClaimKey());
			tablelistValues.get(index).setNoOfRods(reimbursement.size());
		}
		return tablelistValues;
	}
	
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	
	private Boolean validateCancelPolicyApprovalStatus(Long intimationKey,String policyNumber){
		Boolean hasError = false;
		Intimation intimationDtls = intimationService.getIntimationByKey(intimationKey);
		if(validatePolicyStatus(policyNumber)){
			hasError = false;
		}else {
			hasError = true;
		}
		
		return hasError;
	}
	
	private Boolean validatePolicyStatus(String policyNumber){
		Boolean hasError = false;
		enteredValues.put("polNo", policyNumber);
		
		BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
		List<PremPolicy> policyList = policyContainer.getItemIds();
		if(policyList !=null && !policyList.isEmpty()){
			for (PremPolicy premPolicy : policyList) {
				if(premPolicy.getPolicyStatus().equalsIgnoreCase(SHAConstants.CANCELLED_POLICY)){
					hasError = true;
				}
				
			}
		}
		return !hasError;
	}

}
