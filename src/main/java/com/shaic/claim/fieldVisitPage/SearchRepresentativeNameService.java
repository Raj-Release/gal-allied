package com.shaic.claim.fieldVisitPage;

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

import com.shaic.domain.State;
import com.shaic.domain.preauth.TmpFvR;

@Stateless
public class SearchRepresentativeNameService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchRepresentativeNameService(){
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<TmpFvR> getRepresentativeList(Long stateId, Long cityId, Long categoryId){
		
		List<TmpFvR> resultList = new ArrayList<TmpFvR>();
		
//		try{
//		
//		if(stateId != null && ! stateId.equals(0l)){
//		
//			Query query = entityManager.createNamedQuery("TmpFvR.findByState");
//			query.setParameter("stateId", stateId);
//	//		query.setParameter("categoryId", categoryId);	
//			resultList = query.getResultList();	
//		}else{
//			
//			Query query = entityManager.createNamedQuery("TmpFvR.findAll");
//			resultList = query.getResultList();	
//		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<TmpFvR> criteriaQuery = builder.createQuery(TmpFvR.class);
		
		Root<TmpFvR> searchRoot = criteriaQuery.from(TmpFvR.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(stateId != null && ! stateId.equals(0l)){
			Predicate statePredicate = builder.equal(searchRoot.<State> get("state").<Long> get("key"),stateId);
			predicates.add(statePredicate);

		}
		if(cityId != null && ! cityId.equals(0l)){
			
			Predicate cityPredicate = builder.equal(searchRoot.<State> get("cityTownVillage").<Long> get("key"),cityId);
			predicates.add(cityPredicate);
			
		}
		
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));
		
		final TypedQuery<TmpFvR> searchClaimQuery = entityManager
				.createQuery(criteriaQuery);
		
		resultList  = searchClaimQuery.getResultList();

		return resultList;		
	}
}
