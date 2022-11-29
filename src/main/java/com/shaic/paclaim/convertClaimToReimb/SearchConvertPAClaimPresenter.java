package com.shaic.paclaim.convertClaimToReimb;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimFormDto;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimService;

@ViewInterface(SearchConvertPAClaimView.class)
public class SearchConvertPAClaimPresenter extends
		AbstractMVPPresenter<SearchConvertPAClaimView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchConvertClaimService searchConvertClaimService;
	
	public static final String SEARCH_CONVERT_PA_CLAIM_BUTTON_CLICK = "searchconvertpaclaimbuttonclick";

	public void searchClick(
			@Observes @CDIEvent(SEARCH_CONVERT_PA_CLAIM_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchConvertClaimFormDto searchFormDTO = (SearchConvertClaimFormDto) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchConvertClaimService.searchPA(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {

	}

}
