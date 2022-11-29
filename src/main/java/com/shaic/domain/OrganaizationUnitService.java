package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class OrganaizationUnitService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public OrganaizationUnitService(){
		super();
	}
	
	public List<OrganaizationUnit> getOrganaizationUnitByStateAndCity(Long stateId, Long cityId){
		Query query = entityManager.createNamedQuery("OrganaizationUnit.findByStateAndCity");
		query.setParameter("stateKey", stateId);
		query.setParameter("cityKey", cityId);
		List<OrganaizationUnit> organiazationUnitList = query.getResultList();
		return organiazationUnitList;
	}

}
