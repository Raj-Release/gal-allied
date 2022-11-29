package com.shaic.claim.adviseonped.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchAdviseOnPEDView.class)
public class SearchAdviseOnPEDPresenter extends
		AbstractMVPPresenter<SearchAdviseOnPEDView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "aopedSearchClick";
	
	@EJB
	private SearchAdviseOnPedService searchAdviseOnPedService;	
	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchAdviseOnPEDFormDTO searchFormDTO = (SearchAdviseOnPEDFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		
		
		view.list(searchAdviseOnPedService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {

	}

}
