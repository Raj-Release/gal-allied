package com.shaic.claim.coordinator.view;

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
import com.shaic.domain.preauth.Coordinator;

@Stateless
public class ViewCoOrdinatorReplyService extends AbstractDAO<Coordinator> {

	public ViewCoOrdinatorReplyService() {
		super();
	}

	public List<ViewCoOrdinatorDTO> search(Long intimationKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Coordinator> criteriaQuery = builder
				.createQuery(Coordinator.class);

		Root<Coordinator> searchRoot = criteriaQuery
				.from(Coordinator.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Coordinator> resultList = new ArrayList<Coordinator>();
		if (intimationKey != null) {
			Predicate intimationPredicate = builder.equal(searchRoot
					.<Intimation> get("intimation")
					.<Long> get("key"), intimationKey);
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Coordinator> coordinatorQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = coordinatorQuery.getResultList();

		List<ViewCoOrdinatorDTO> vieweCoOrdinatorReplyTableDTO = ViewCoOrdinatorReplyMapper.getInstance()
				.getViewCoOrdinatorDTO(resultList);
		return vieweCoOrdinatorReplyTableDTO;
	}
	
	public Coordinator getCoordinator(Long key){
		
		
		Query query = entityManager.createNamedQuery("Coordinator.findByKey");
		query.setParameter("primaryKey", key);
		Coordinator singleResult = (Coordinator) query.getSingleResult();
		
		return singleResult;
		
	}

	@Override
	public Class<Coordinator> getDTOClass() {
		// TODO Auto-generated method stub
		return Coordinator.class;
	}
	
}