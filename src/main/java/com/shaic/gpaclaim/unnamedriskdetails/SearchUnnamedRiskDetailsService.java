package com.shaic.gpaclaim.unnamedriskdetails;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
@Stateless
public class SearchUnnamedRiskDetailsService extends AbstractDAO<Intimation>{
	

	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchUnnamedRiskDetailsService() {
		super();
	}
	
	public  Page<SearchUnnamedRiskDetailsTableDTO> search(SearchUnnamedRiskDetailsFormDTO unnamedRiskFormDTO,String userName, String passWord) {
		
		

		List<Intimation> listIntimations = new ArrayList<Intimation>(); 
		try
		{
			String intimationNo = null != unnamedRiskFormDTO && !unnamedRiskFormDTO.getIntimationNo().isEmpty() ? unnamedRiskFormDTO.getIntimationNo() : null;
					
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
			
			Root<Intimation> root = criteriaQuery.from(Intimation.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
					
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(root.<String> get("intimationId"), "%"+ intimationNo + "%");
				conditionList.add(condition1);
			}
			
		//	Predicate condition1= criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), reportType);				
//			conditionList.add(condition1);
			
			Predicate condition2= criteriaBuilder.equal(root.<Policy>get("policy").<Product>get("product").<Long> get("key"), ReferenceTable.GPA_PRODUCT_KEY);				
			conditionList.add(condition2);
			
			Predicate condition3= criteriaBuilder.equal(root.<Policy>get("policy").<String>get("gpaPolicyType"), SHAConstants.GPA_UN_NAMED_POLICY_TYPE);				
			conditionList.add(condition3);
			
			if (intimationNo == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = unnamedRiskFormDTO.getPageable().getPageNumber();
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
			
		    List<Intimation> doList = listIntimations;

			List<SearchUnnamedRiskDetailsTableDTO> tableDTO = SearchUnnamedRiskDetailsMapper.getInstance().getUnnamedRiskDetailsTableObjects(doList);
					
			List<SearchUnnamedRiskDetailsTableDTO> result = new ArrayList<SearchUnnamedRiskDetailsTableDTO>();
			result.addAll(tableDTO);
			Page<SearchUnnamedRiskDetailsTableDTO> page = new Page<SearchUnnamedRiskDetailsTableDTO>();			
			unnamedRiskFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(result.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (result.isEmpty()) {
				unnamedRiskFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);

			return page;
		  }
			catch (Exception e) 
		   {
		     e.printStackTrace();
		   }
		   return null;	
		}
	


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
		
	



}
