package com.shaic.claim.pedrequest.initiateped;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchPEDInitiateView.class)
public class SearchPEDInitiatePresenter extends
		AbstractMVPPresenter<SearchPEDInitiateView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "pedInitiateSearchBtn";
	
	@EJB
	private SearchPEDInitiateService searchInitiateProcessService;

	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchPEDInitiateFormDTO searchFormDTO = (SearchPEDInitiateFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchInitiateProcessService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
