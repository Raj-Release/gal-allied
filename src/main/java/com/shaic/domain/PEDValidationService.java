package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ViewTmpDiagnosis;


@Stateless
public class PEDValidationService {
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	public List<PedValidation> getDiagnosis(Long intimationkey){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	  	CriteriaQuery<PedValidation> createQuery = cb.createQuery(PedValidation.class);
	  	Root<PedValidation> fromQuery = createQuery.from(PedValidation.class);
	  	createQuery.select(fromQuery);
	  	Expression<Long> expression = fromQuery.<Intimation>get("intimation").<Long>get("key");
	  	createQuery.where(cb.equal(expression, intimationkey));
	  	Query filterQuery = entityManager.createQuery(createQuery);
		List<PedValidation> policyConditionsList = (List<PedValidation>) filterQuery.getResultList();
		
		
		return policyConditionsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpDiagnosis> getDiagnosisFromViewTmpDiagnosis(Long intimationkey){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	  	CriteriaQuery<ViewTmpDiagnosis> createQuery = cb.createQuery(ViewTmpDiagnosis.class);
	  	Root<ViewTmpDiagnosis> fromQuery = createQuery.from(ViewTmpDiagnosis.class);
	  	createQuery.select(fromQuery);
	  	Expression<String> expression = fromQuery.get("intimation");
	  	createQuery.where(cb.equal(expression, intimationkey));
	  	Query filterQuery = entityManager.createQuery(createQuery);
		List<ViewTmpDiagnosis> policyConditionsList = (List<ViewTmpDiagnosis>) filterQuery.getResultList();
		
		
		return policyConditionsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PedValidation> getDiagnosis(Long intimationkey,EntityManager em){
		this.entityManager =em;		
		return getDiagnosis(intimationkey);	
		
	}

	
	@SuppressWarnings("unchecked")
	public List<PedValidation> getPedValidation(Long intimationkey){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	  	CriteriaQuery<PedValidation> createQuery = cb.createQuery(PedValidation.class);
	  	Root<PedValidation> fromQuery = createQuery.from(PedValidation.class);
	  	createQuery.select(fromQuery);
	  	Expression<String> expression = fromQuery.get("intimation");
	  	createQuery.where(cb.equal(expression, intimationkey));
	  	Query filterQuery = entityManager.createQuery(createQuery);
		List<PedValidation> policyConditionsList = (List<PedValidation>) filterQuery.getResultList();
		
		
		return policyConditionsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Procedure> getProcedureList(Long preauthKey){
		Query findByPreauthKey = entityManager.createNamedQuery(
				"Procedure.findByPreauthKey").setParameter("preauthKey", preauthKey);
		List<Procedure> procedureList = (List<Procedure>) findByPreauthKey
				.getResultList();
		if (procedureList.size() > 0) {
			return procedureList;
		}
		return null;
	}
	
	public List<PedValidation> getDiagnosisList(Long transacKey,EntityManager em){
		List<PedValidation> diagnosisList = null;
		try{
		entityManager = em;
		Query findByTransacKey = entityManager.createNamedQuery(
				"PedValidation.findByTransactionKey").setParameter("transactionKey", transacKey);
		diagnosisList = (List<PedValidation>) findByTransacKey.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return diagnosisList;
	}
	
	

}
