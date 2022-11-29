package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.domain.preauth.ClaimLimit;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class ClaimLimitService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BeanItemContainer<ClaimLimit> getClaimLimitDetails(Long productKey, Integer sumInsured) {	
		String limitType = "SUB LIMIT";

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<ClaimLimit> criteriaQuery = builder
				.createQuery(ClaimLimit.class);

		Root<ClaimLimit> searchRoot = criteriaQuery.from(ClaimLimit.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<ClaimLimit> resultList = new ArrayList<ClaimLimit>();

		if (productKey != null) {
			Predicate productPredicate = builder.equal(
					searchRoot.<Product> get("product").<Long> get(
							"key"),  productKey );
			predicates.add(productPredicate);
		}
		if (sumInsured != null) {
			Predicate suminsuredPredicate = builder.equal(
					searchRoot.<Long> get("sumInsured"),  sumInsured );
			predicates.add(suminsuredPredicate);
		}
		if (limitType != null) {
			Predicate limitTypePredicate = builder.equal(
					searchRoot.<Long> get("limitType"),  limitType );
			predicates.add(limitTypePredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));


		final TypedQuery<ClaimLimit> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();

		List<ClaimLimit> pageItemList = resultList;

		BeanItemContainer<ClaimLimit> limitContainer = new BeanItemContainer<ClaimLimit>(
				ClaimLimit.class);
		limitContainer.addAll(pageItemList);
		return limitContainer;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClaimLimit> getClaimByKey(Long key, EntityManager entityManager){
		this.entityManager = entityManager;
		Query query = entityManager.createNamedQuery(
				"ClaimLimit.findByKey").setParameter("limitKey", key);
		return (List<ClaimLimit>)query.getResultList();
	}


	

}
