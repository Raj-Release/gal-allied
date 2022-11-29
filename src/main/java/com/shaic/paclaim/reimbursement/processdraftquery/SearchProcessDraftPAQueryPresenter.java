/**
 * 
 */
package com.shaic.paclaim.reimbursement.processdraftquery;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryFormDTO;
import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryService;



/**
 * 
 *
 */

@ViewInterface(SearchProcessDraftPAQueryView.class)
public class SearchProcessDraftPAQueryPresenter extends AbstractMVPPresenter<SearchProcessDraftPAQueryView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_PROCESS_PA_QUERY_BUTTON_CLICK = "PAProcessDraftQuerySearch";
	
	@EJB
	private SearchProcessDraftQueryService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PROCESS_PA_QUERY_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessDraftQueryFormDTO searchFormDTO = (SearchProcessDraftQueryFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
