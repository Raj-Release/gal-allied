package com.shaic.claim.pedrequest.process.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(SearchPEDRequestProcessView.class)
public class SearchPEDRequestProcessPresenter extends
		AbstractMVPPresenter<SearchPEDRequestProcessView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "pedrpSearchBtn";
	
	@EJB
	private SearchPEDRequestProcessService searchPEDRequestProcessService;

	
	public void searchClick(
			@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchPEDRequestProcessFormDTO searchFormDTO = (SearchPEDRequestProcessFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchPEDRequestProcessService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
