/**
 * 
 */
package com.shaic.paclaim.reimbursement.processdraftrejection;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.queryrejection.processdraftrejection.search.SearchProcessDraftRejectionFormDTO;
import com.shaic.reimbursement.queryrejection.processdraftrejection.search.SearchProcessDraftRejectionService;



/**
 * 
 *
 */

@ViewInterface(SearchProcessDraftPARejectionView.class)
public class SearchProcessDraftPARejectionPresenter extends AbstractMVPPresenter<SearchProcessDraftPARejectionView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "do_search_Pa_rejection_Click";
	
	@EJB
	private SearchProcessDraftRejectionService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessDraftRejectionFormDTO searchFormDTO = (SearchProcessDraftRejectionFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
