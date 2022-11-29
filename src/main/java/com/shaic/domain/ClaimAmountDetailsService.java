package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.preauth.ClaimAmountDetails;

@Stateless
public class ClaimAmountDetailsService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ClaimAmountDetailsService(){
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<ClaimAmountDetails> getClaimedAmoutnDetailsByPreAuthKey(Long preauthKey, EntityManager entityManager) {
		this.entityManager = entityManager;
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		return (List<ClaimAmountDetails>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ClaimAmountDetails> getClaimedAmoutnDetailsByPreAuthKey(Long preauthKey) {
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		return (List<ClaimAmountDetails>) query.getResultList();
	}
}
