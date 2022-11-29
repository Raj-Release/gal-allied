/**
 * 
 */
package com.shaic.reimbursement.investigation.ackinvestivationcompleted.search;

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

@ViewInterface(SearchAckInvestigationCompletedView.class)
public class SearchAckInvestigationCompletedPresenter extends AbstractMVPPresenter<SearchAckInvestigationCompletedView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchAckTable";
	
	@EJB
	private SearchAckInvestigationCompletedService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAckInvestigationCompletedFormDTO searchFormDTO = (SearchAckInvestigationCompletedFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
