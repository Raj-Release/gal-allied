package com.shaic.reimburement.specialapprover.approveclaim.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

/**
 * @author ntv.narenj
 *
 */
@ViewInterface(SearchApproveClaimView.class)
public class SearchApproveClaimPresenter extends AbstractMVPPresenter<SearchApproveClaimView>{

	public static final String SEARCH_BUTTON_CLICK = "SearchApproveClaim";
	public static final String SEARCH_ERROR = "SearchApproveClaimError";
	
	@EJB
	private SearchApproveClaimService  searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchApproveClaimFormDTO searchFormDTO = (SearchApproveClaimFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearchError(@Observes @CDIEvent(SEARCH_ERROR) final ParameterDTO parameters) {

		view.validation();
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
