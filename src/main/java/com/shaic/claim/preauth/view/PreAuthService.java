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
import com.shaic.domain.preauth.Preauth;

@Stateless
public class PreAuthService extends AbstractDAO<Preauth> {

	public PreAuthService() {
		super();
	}

	public ViewPreAuthDetailsDTO search(Long preAuthKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Preauth> criteriaQuery = builder
				.createQuery(Preauth.class);

		Root<Preauth> searchRoot = criteriaQuery.from(Preauth.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Preauth> resultList = new ArrayList<Preauth>();

		if (preAuthKey != null) {
			Predicate intimationPredicate = builder.equal(
					searchRoot.<Long> get("key"), preAuthKey);
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Preauth> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();

		List<Preauth> pageItemList = resultList;

		List<ViewPreAuthDetailsDTO> searchPedQueryTableDTO = PreAuthMapper.getInstance()
				.getViewPreAuthDetailsDTODTO(pageItemList);

		return searchPedQueryTableDTO.get(0);
	}
	
	public Preauth searchByKey(Long key)
	{
		if(key != null){
			Query findByPreauthKey = entityManager.createNamedQuery(
					"Preauth.findByKey").setParameter("preauthKey", key);
			try {
				List<Preauth> preauth = (List<Preauth>) findByPreauthKey.getResultList();
				if(!preauth.isEmpty()){
					return preauth.get(0);
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
		
		//return null;
	}
	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}	

}
