package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;


@ViewInterface(SearchUploadNEFTDetailsView.class)
public class SearchUploadNEFTDetailsPresenter extends AbstractMVPPresenter<SearchUploadNEFTDetailsView> {

	
	public static final String SEARCH_NEFT_BUTTON_CLICK = "doSearchUploadNEFTDetails";
	
	public static final String SEARCH_NEFT_ERROR = "doSearchUploadNEFTDetailsError";
	
	@EJB
	private SearchUploadNEFTDetailsService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_NEFT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchUploadNEFTDetailsFormDTO searchFormDTO = (SearchUploadNEFTDetailsFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearchError(@Observes @CDIEvent(SEARCH_NEFT_ERROR) final ParameterDTO parameters) {

		view.validation();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}


}
