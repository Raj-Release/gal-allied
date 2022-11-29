package com.shaic.claim.preauth.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.Preauth;
@Stateless
public class ViewpreAuthService extends AbstractDAO<Preauth> {
	
	public ViewpreAuthService(){
		super();
	}
	
	public List<Preauth> searchPreAuth(String intimationId) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Preauth> criteriaQuery = builder
				.createQuery(Preauth.class);

		Root<Preauth> searchRoot = criteriaQuery
				.from(Preauth.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Preauth> resultList = new ArrayList<Preauth>();

		if (intimationId != null) {
			Predicate preAuthPredicate = builder.like(
					searchRoot.<Intimation> get("intimation").<String> get(
							"intimationId"), "%" + intimationId + "%");
			predicates.add(preAuthPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Preauth> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();

		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKeyInDescendingOrder(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Preauth.findPreAuthIdInDescendingOrder");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		return resultList;
	}

	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}

}
