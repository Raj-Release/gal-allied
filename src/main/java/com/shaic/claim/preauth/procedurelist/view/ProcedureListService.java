package com.shaic.claim.preauth.procedurelist.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;

@Stateless
public class ProcedureListService extends AbstractDAO<Procedure> {

	public ProcedureListService() {
		super();
	}

	public List<ProcedureListTableDTO> search(Long preAuthKey) {

//		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<Procedure> criteriaQuery = builder
//				.createQuery(Procedure.class);
//
//		Root<Procedure> searchRoot = criteriaQuery.from(Procedure.class);
//
//		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Procedure> resultList = new ArrayList<Procedure>();
		
		Query query = entityManager.createNamedQuery("Procedure.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);
		
		resultList = (List<Procedure>)query.getResultList();
		
		Query preauthQuery = entityManager.createNamedQuery("Preauth.findByKey");
		preauthQuery.setParameter("preauthKey", preAuthKey);
		
		Preauth preauth = (Preauth)preauthQuery.getSingleResult();
       

//		if (preAuthKey != null) {
//			Predicate preAuthPredicate = builder.equal(
//					searchRoot.<Preauth> get("preauth").<Long> get(
//							"key"), preAuthKey);
//			predicates.add(preAuthPredicate);
//		}
//		criteriaQuery.select(searchRoot).where(
//				builder.and(predicates.toArray(new Predicate[] {})));
//
//		final TypedQuery<Procedure> oldInitiatePedQuery = entityManager
//				.createQuery(criteriaQuery);
//
//		resultList = oldInitiatePedQuery.getResultList();
		
		

		List<Procedure> pageItemList = resultList;

		List<ProcedureListTableDTO> procedureListServiceList = ProcedureListMapper.getInstance()
				.getSearchPEDQueryTableDTOTableDTO(pageItemList);
		System.out.println(procedureListServiceList.size());
		for (ProcedureListTableDTO procedureListTableDTO : procedureListServiceList) {
			procedureListTableDTO.setReference(preauth.getPreauthId());
		}

		return procedureListServiceList;
	}

	@Override
	public Class<Procedure> getDTOClass() {
		return Procedure.class;
	}

}
