/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;



/**
 * 
 *
 */

@ViewInterface(SearchCloseClaimView.class)
public class SearchCloseClaimPresenter extends AbstractMVPPresenter<SearchCloseClaimView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchTable";
	
	@EJB
	private SearchCloseClaimService searchCloseClaimService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCloseClaimFormDTO searchFormDTO = (SearchCloseClaimFormDTO) parameters.getPrimaryParameter();

		
		view.getResultList(searchCloseClaimService.search(searchFormDTO));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
