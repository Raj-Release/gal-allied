package com.shaic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.preauth.Preauth;

public class NewIntimationService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByIntimationKey(Long intimationKey) {
		Query query = entityManager
				.createNamedQuery("Claim.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);		
		List<Claim> resultList = query.getResultList();
		if(!resultList.isEmpty())
		{
			return resultList.get(0);
		}else{
			return null;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthListByIntimationKey(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}

}
