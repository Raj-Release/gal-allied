/**
 * 
 */
package com.shaic.paclaim.reimbursement.draftrejection;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterFormDTO;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterService;

/**
 * 
 *
 */

@ViewInterface(SearchDraftPARejectionLetterView.class)
public class SearchDraftPARejectionLetterPresenter extends AbstractMVPPresenter<SearchDraftPARejectionLetterView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_PA_REJECTION_BUTTON_CLICK = "doSearchPAdrclick";
	
	@EJB
	private SearchDraftRejectionLetterService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PA_REJECTION_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchDraftRejectionLetterFormDTO searchFormDTO = (SearchDraftRejectionLetterFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
