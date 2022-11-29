package com.shaic.claim.pedrequest.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasUser;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

@Stateless
public class ViewpedRequestService extends
		AbstractDAO<OldInitiatePedEndorsement> {

	public ViewpedRequestService() {
		super();
	}

	public List<OldPedEndorsementDTO> search(Long intimationKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);

		Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<OldInitiatePedEndorsement> resultList = new ArrayList<OldInitiatePedEndorsement>();

		if (intimationKey != null) {
			Predicate intimationPredicate = builder.equal(searchRoot
					.<Intimation> get("intimation")
					.<Long> get("key"), intimationKey);
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
				
				if(resultDto.get(i).getRequestorId() != null){
					resultDto.get(i).setIsReviewer(isUserPedReviewer(resultDto.get(i).getRequestorId()));
				}
				
				if(resultDto.get(i).getRequestedDate() != null){
					Date formatDateTime = SHAUtils.formatTime(resultDto.get(i).getRequestedDate());
					
					resultDto.get(i).setRequestedDate(SHAUtils.formatDate(formatDateTime));
					}
				
				if(resultDto.get(i).getPedEffectiveFromDate() != null){
					Date formatDateTime = SHAUtils.formatTime(resultDto.get(i).getPedEffectiveFromDate().toString());
					
					resultDto.get(i).setPedEffectiveFromDate(SHAUtils.formatDate(formatDateTime));
					}
				
			}
			return resultDto;
		}
		return null;
	}
	
	

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}

	public Boolean isUserPedReviewer(String userName){
		try {
			Query findAllDetails = entityManager
					.createNamedQuery("MasUser.getByPedReviewer").setParameter("userId", userName.toLowerCase());
			@SuppressWarnings("unchecked")
			List<MasUser> pedInitiateList = (List<MasUser>) findAllDetails
					.getResultList();
			
			if(pedInitiateList != null && !pedInitiateList.isEmpty()) {
				return true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
}
