package com.shaic.reimburement.specialapprover.processclaim.search;

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
@ViewInterface(SearchProcessClaimView.class)
public class SearchProcessClaimPresenter extends AbstractMVPPresenter<SearchProcessClaimView>{

	public static final String SEARCH_BUTTON_CLICK = "SearchProcessClaim";
	
	
	@EJB
	private SearchProcessClaimService  searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessClaimFormDTO searchFormDTO = (SearchProcessClaimFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
