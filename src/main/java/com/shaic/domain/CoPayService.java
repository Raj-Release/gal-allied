package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CoPayService {

	@PersistenceContext
	protected EntityManager entityManager;

	public MasCopay getByProduct(Long productKey) {
		MasCopay policyList = null;
		Query findAll = entityManager
				.createNamedQuery("MasCopay.findByProduct").setParameter(
						"productKey", productKey);
		if (findAll.getResultList().size() > 0) {
			policyList = (MasCopay) findAll.getSingleResult();
		}
		return policyList;
	}
	
	public MasCopay getByProductAndSi(Long productKey,Double totalSumInsured){
		
		
		Query findAll = entityManager.createNamedQuery("MasCopay.findByProduct").setParameter("productKey", productKey);
		List<MasCopay> findList = findAll.getResultList();
		MasCopay coPay = null;
		if(findList != null && !findList.isEmpty()){
			for (MasCopay masCopay : findList) {
				if(masCopay.getSiTo() !=null && totalSumInsured != null && (masCopay.getSiFrom() < totalSumInsured)){
					coPay = masCopay;
					break;
				}
			}
			return coPay;
		}
		return null;	
	}

}
