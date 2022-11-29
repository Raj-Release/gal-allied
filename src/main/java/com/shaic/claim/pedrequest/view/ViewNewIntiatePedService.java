package com.shaic.claim.pedrequest.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;

@Stateless
public class ViewNewIntiatePedService extends
		AbstractDAO<NewInitiatePedEndorsement> {
	
	public ViewNewIntiatePedService()
	{
		super();
	}
	
public List<OldPedEndorsementDTO> searchNewIntimationTable() {
		
		String intimationNo = "002"; 

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<NewInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(NewInitiatePedEndorsement.class);

		Root<NewInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(NewInitiatePedEndorsement.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<NewInitiatePedEndorsement> resultList = new ArrayList<NewInitiatePedEndorsement>();

		if (intimationNo != null) {
			Predicate intimationPredicate = builder.like(searchRoot
					.<Intimation> get("intimation")
					.<String> get("intimationId"), "%" + intimationNo + "%");
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<NewInitiatePedEndorsement> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();
		
		System.out.println(resultList.get(0).getIcdBlockId());
		System.out.println(resultList.get(0).getIcdChapterId());
		System.out.println(resultList.get(0).getIcdCodeId());
		System.out.println(resultList.get(0).getSource().getValue());
		/*
		if (resultList != null) {
			List<OldPedEndorsementDTO> resultDto = ViewPEDQueryMapper
					.getOldPedEndorsementDTO(resultList);
			for (int i = 0; i < resultDto.size(); i++) {
				Button button = new Button("view Details");
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				resultDto.get(i).setViewDetails(button);
			}
			return resultDto;
		}*/
		return null;
	}

@Override
public Class<NewInitiatePedEndorsement> getDTOClass() {	
	return NewInitiatePedEndorsement.class;
}

}
