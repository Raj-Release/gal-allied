package com.shaic.claim.registration.convertclaimcashless;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchConverClaimCashlessView.class)
public class SearchConverClaimCashlessPresenter extends
		AbstractMVPPresenter<SearchConverClaimCashlessView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "converclaimcashlessservice";
	
	@EJB
	private SearchConverClaimCashlessService searchConverClaimCashlessService;

	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchConverClaimCashlessFormDTO searchFormDTO = (SearchConverClaimCashlessFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchConverClaimCashlessService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
