package com.shaic.claim.procedureexclusioncheck.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;

@Stateless
public class ViewProcedureExclusionService extends AbstractDAO<Procedure> {

	public ViewProcedureExclusionService() {
		super();
	}

	public List<ViewProcedureExclusionCheckDTO> search(String preAuthKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Procedure> criteriaQuery = builder
				.createQuery(Procedure.class);

		Root<Procedure> searchRoot = criteriaQuery.from(Procedure.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Procedure> resultList = new ArrayList<Procedure>();

		if (preAuthKey != null) {
			Predicate intimationPredicate = builder.like(searchRoot
					.<Preauth> get("preauth").<String> get("preauthId"), "%"
					+ preAuthKey + "%");
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Procedure> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();

		List<Procedure> pageItemList = resultList;

		List<ViewProcedureExclusionCheckDTO> searchPedQueryTableDTO = ViewProcedureExclusionMapper.getInstance()
				.getViewProcedureExclusionCheckDTO(pageItemList);

		return searchPedQueryTableDTO;
	}

	@Override
	public Class<Procedure> getDTOClass() {
		// TODO Auto-generated method stub
		return Procedure.class;
	}

}
