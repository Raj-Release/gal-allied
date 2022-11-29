/**
 * 
 */
package com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel;

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

@ViewInterface(SearchReOpenClaimView.class)
public class SearchReOpenClaimPresenter extends AbstractMVPPresenter<SearchReOpenClaimView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "getSearchreopTable";
	
	@EJB
	private SearchReOpenClaimService service;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchReOpenClaimFormDTO searchFormDTO = (SearchReOpenClaimFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.resultList(service.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
