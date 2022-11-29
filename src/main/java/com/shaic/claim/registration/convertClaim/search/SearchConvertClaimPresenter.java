package com.shaic.claim.registration.convertClaim.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchConvertClaimView.class)
public class SearchConvertClaimPresenter extends
		AbstractMVPPresenter<SearchConvertClaimView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchConvertClaimService searchConvertClaimService;
	
	/*@EJB
	private MasterService masterService;*/

	//public static final String SEARCH_BUTTON_CLICK = "convertClaimSearchClick";
	public static final String SEARCH_BUTTON_CLICK = "submitSpecialistFormBtn";

	public static final String LOAD_LOB = "lodLob";

	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchConvertClaimFormDto searchFormDTO = (SearchConvertClaimFormDto) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchConvertClaimService.search(searchFormDTO,userName,passWord));
	}
	
	/*public void loadLOBValue(
			@Observes @CDIEvent(LOAD_LOB) final ParameterDTO parameters) {
		SearchConvertClaimFormDto searchFormDTO = (SearchConvertClaimFormDto) parameters
				.getPrimaryParameter();
		String strLob = (String)parameters.getPrimaryParameter();
		Long lobId = Long.parseLong((strLob));
		searchConvertClaimService.set
	}*/

	@Override
	public void viewEntered() {

	}

}
