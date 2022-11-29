/**
 * 
 */
package com.shaic.paclaim.reimbursement.draftquery;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterFormDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterService;



/**
 * 
 *
 */

@ViewInterface(SearchDraftPAQueryLetterView.class)
public class SearchDraftPAQueryLetterPresenter extends AbstractMVPPresenter<SearchDraftPAQueryLetterView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PA_QUERY_SEARCH_BUTTON_CLICK = "doSearchPATable";
	
	@EJB
	private SearchDraftQueryLetterService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(PA_QUERY_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchDraftQueryLetterFormDTO searchFormDTO = (SearchDraftQueryLetterFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
