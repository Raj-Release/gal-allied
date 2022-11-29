package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class PreviousPolicyService {

	@PersistenceContext
	protected EntityManager entityManager;

//	@SuppressWarnings("unchecked")
//	public BeanItemContainer<TmpPrevPolicy> getPreviousPolicy(
//			String a_polAssrCode) {
//		BeanItemContainer<TmpPrevPolicy> policyListContainer = new BeanItemContainer<TmpPrevPolicy>(
//				TmpPrevPolicy.class);
//		if (a_polAssrCode != null) {
//			Query query = entityManager
//					.createNamedQuery("TmpPrevPolicy.findByPolAssrCode");
//			query = query.setParameter("parentKey",
//					"%" + StringUtils.trim(a_polAssrCode) + "%");
//			List<TmpPrevPolicy> policyList = (List<TmpPrevPolicy>) query
//					.getResultList();
//			policyListContainer.addAll(policyList);
//		}
//
//		return policyListContainer;
//
//	}

	public BeanItemContainer<PreviousPolicy> getPreviousPolicyDetails(Policy policy)
	{
		String a_polAssrCode = policy.getProposerCode();
		String policyNumber = policy.getPolicyNumber();
		BeanItemContainer<PreviousPolicy> policyListContainer =
	            new BeanItemContainer<PreviousPolicy>(PreviousPolicy.class);		
		if(policyNumber != null){
			Query query = entityManager.createNamedQuery("PreviousPolicy.findByCurrentPolicy");
			query = query.setParameter("policyNumber", policyNumber);	
			List<PreviousPolicy> policyList = (List<PreviousPolicy>) query.getResultList();	
			if(policyList != null && !policyList.isEmpty()){
				for(PreviousPolicy previousPolicy:policyList)
				{
//					if(( policy.getPolicyFromDate().compareTo(previousPolicy.getPolicyToDate())) ==0 || 
//							(policy.getPolicyFromDate().compareTo(previousPolicy.getPolicyToDate()) == 1)){
					policyListContainer.addBean(previousPolicy);
//							}
				}
			}
		
		}		
		
		
		return policyListContainer;
		
	}

//	public List<TmpPrevPolicy> findTmppolicyByPolicyNo(String proposerCode) {
//		Query query = entityManager
//				.createNamedQuery("TmpPrevPolicy.findByPolAssrCode");
//		query.setParameter("parentKey", proposerCode);
//		return (List<TmpPrevPolicy>) query.getResultList();
//	}

	public List<PreviousPolicy> getPrevPolicy(String proposerCode) {
		Query query = entityManager
				.createNamedQuery("PreviousPolicy.findByPolAssrCode");
		query.setParameter("parentKey", proposerCode);
		return (List<PreviousPolicy>) query.getResultList();
	}

//	public List<Locality> localitySearchbyCityKey(Long key) {
//		Query query = entityManager
//				.createNamedQuery("Locality.searchAreaByCity");
//		query.setParameter("cityKey", key);
//
//		return (List<Locality>) query.getResultList();
//	}

}
