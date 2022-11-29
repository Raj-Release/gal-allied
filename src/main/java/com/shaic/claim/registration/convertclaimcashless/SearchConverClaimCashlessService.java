package com.shaic.claim.registration.convertclaimcashless;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;

@Stateless
public class SearchConverClaimCashlessService extends  AbstractDAO<Claim> {
	
	public SearchConverClaimCashlessService(){
		super();
	}
	
	public Page<SearchConverClaimCashlessTableDTO> search(SearchConverClaimCashlessFormDTO searchFormDTO, String userName, String passWord) {
		List<Claim> listIntimations = new ArrayList<Claim>(); 
		try{
		String intimationNo = null != searchFormDTO && searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() :null;
		String policyNo = null != searchFormDTO && searchFormDTO.getPolicyNo() !=null ? searchFormDTO.getPolicyNo() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		
		if(intimationNo == null && policyNo == null ){
			criteriaQuery.select(root);
			} else{
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
			firtResult = 1;
		}
		listIntimations = typedQuery.getResultList();
		
		if( listIntimations.size()>10)
		{
			listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}
		
		
		List<Claim> doList = listIntimations;
		List<SearchConverClaimCashlessTableDTO> tableDTO =SearchConverClaimCashlessMapper.getInstance().getSearchconverclaimcashlessTableDTO(doList);
		for (SearchConverClaimCashlessTableDTO searchConverClaimCashlessTableDTO : tableDTO) {
			List<DocAcknowledgement> ackDetailsForBillClassificationValidation = getAckDetailsForBillClassificationValidation(searchConverClaimCashlessTableDTO.getKey());
			if (ackDetailsForBillClassificationValidation.isEmpty()){
				searchConverClaimCashlessTableDTO.setIsackavailable(true);
			}
			searchConverClaimCashlessTableDTO.setCpuCode(String.valueOf(searchConverClaimCashlessTableDTO.getCpuCode()));
			
			Hospitals searchbyHospitalKey = searchbyHospitalKey(searchConverClaimCashlessTableDTO.getHospitalNameIds());
			searchConverClaimCashlessTableDTO.setHospitalName(searchbyHospitalKey.getName());
			if(searchConverClaimCashlessTableDTO.getIntimationDate() != null){
				searchConverClaimCashlessTableDTO.setStrIntimationDate(SHAUtils.formatDate(searchConverClaimCashlessTableDTO.getIntimationDate()));
				
			}
			
		}
	    
		
		List<SearchConverClaimCashlessTableDTO> result = new ArrayList<SearchConverClaimCashlessTableDTO>();
		result.addAll(tableDTO);
		Page<SearchConverClaimCashlessTableDTO> page = new Page<SearchConverClaimCashlessTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10) {
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	
	
	

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
	}
	
	public List<DocAcknowledgement> getAckDetailsForBillClassificationValidation(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findNonCancelledAck");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId", ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query
				.getResultList();
		
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			for (DocAcknowledgement docAcknowledgement : reimbursementList) {
				entityManager.refresh(docAcknowledgement);
			}
		}

		return reimbursementList;

	}
	
	public Hospitals searchbyHospitalKey(Long hosLongKey) {
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hosLongKey);
		List<Hospitals> hospitalList = (List<Hospitals>) findByHospitalKey
				.getResultList();
		if (hospitalList.size() > 0) {
			return hospitalList.get(0);
		}
		return null;
	}
	
	

}
