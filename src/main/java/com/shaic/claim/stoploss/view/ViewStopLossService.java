package com.shaic.claim.stoploss.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.pedrequest.view.ViewPEDQueryMapper;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

@Stateless
public class ViewStopLossService extends AbstractDAO<OldInitiatePedEndorsement> {
	
	public ViewStopLossService(){
		super();
	}
	
	public List<OldPedEndorsementDTO> search(String intimationNo) {
		
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);

		Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<OldInitiatePedEndorsement> resultList = new ArrayList<OldInitiatePedEndorsement>();

		if (intimationNo != null) {
			Predicate intimationPredicate = builder.like(searchRoot
					.<Intimation> get("intimation")
					.<String> get("intimationId"), "%" + intimationNo + "%");
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();		

		if (resultList != null) {
			List<OldPedEndorsementDTO> resultDto = ViewPEDQueryMapper.getInstance()
					.getOldPedEndorsementDTO(resultList);
			for (int i = 0; i < resultDto.size(); i++) {
				Button button = new Button("view Details");
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				resultDto.get(i).setViewDetails(button);
			}
			return resultDto;
		}
		
		return null;
		
	}

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}

}
