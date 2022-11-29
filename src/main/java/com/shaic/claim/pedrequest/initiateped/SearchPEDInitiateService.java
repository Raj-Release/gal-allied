package com.shaic.claim.pedrequest.initiateped;

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
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;

@Stateless
public class SearchPEDInitiateService extends  AbstractDAO<Claim> {
	
	public SearchPEDInitiateService(){
		super();
	}
	
	public Page<SearchPEDInitiateTableDTO> search(SearchPEDInitiateFormDTO searchFormDTO, String userName, String passWord)
	{
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
		List<SearchPEDInitiateTableDTO> tableDTO =SearchPEDInitiateMapper.getInstance().getSearchPEDRequestProcessTableDTO(doList);
		
	    for (SearchPEDInitiateTableDTO searchPEDInitiateTableDTO : tableDTO) {
	    	
	    	Integer count = 0;
	    	String periodOfInsurance ="";
			Policy policy = getPolicyByKey(searchPEDInitiateTableDTO.getPolicyKey());
			List< Insured> insuredList =getInsuredByPolicy(policy.getKey());
			for (Insured insured : insuredList) {
				count+=1;
			}
			if(insuredList != null){
			searchPEDInitiateTableDTO.setInsuredList(insuredList);
			}
			
			
			String branchName = getBranchName(searchPEDInitiateTableDTO.getPolicyIssuingOfficeName());
			periodOfInsurance ="From "+SHAUtils.formatDate(policy.getPolicyFromDate())+" To "+SHAUtils.formatDate(policy.getPolicyToDate());
			searchPEDInitiateTableDTO.setPolicyIssuingOfficeName(branchName);
			searchPEDInitiateTableDTO.setNoOfInsured(count);
			searchPEDInitiateTableDTO.setPeriodOfInsurance(periodOfInsurance);
			
		}
		
		List<SearchPEDInitiateTableDTO> result = new ArrayList<SearchPEDInitiateTableDTO>();
		result.addAll(tableDTO);
		Page<SearchPEDInitiateTableDTO> page = new Page<SearchPEDInitiateTableDTO>();
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
	
	
	
	
	public String getBranchName(String officeCode) {
		String officeName = "";
		Query query = entityManager.createNamedQuery("OrganaizationUnit.findByBranchode");
    	query = query.setParameter("parentKey", officeCode);
    	OrganaizationUnit orgList = (OrganaizationUnit) query.getSingleResult();
    	if(null != orgList ) {
    		officeName = orgList.getOrganizationUnitName();
    		return officeName;
    	}
    	return officeName;
		
	}

	private List <Insured> getInsuredByPolicy(Long  policyKey) {
		
			Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
			 query = query.setParameter("policykey", policyKey);	
			 List<Insured> insuredList  = (List<Insured>) query.getResultList();
			 if (insuredList != null && ! insuredList.isEmpty()) {
				 return insuredList;		
			 }
			 return null;
	}

	private Policy  getPolicyByKey(Long policyKey) {
		
		Query query = entityManager.createNamedQuery("Policy.findByKey");
    	query = query.setParameter("policyKey", policyKey);
    	Policy policyList = (Policy) query.getSingleResult();
    	if (policyList != null){
    		return policyList;
    	}
		return null;
	}

	public List<Intimation> getIntimationByPolicyNumber(String policyNumber){
		return null;
	}
	

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
	}
	
	

}
