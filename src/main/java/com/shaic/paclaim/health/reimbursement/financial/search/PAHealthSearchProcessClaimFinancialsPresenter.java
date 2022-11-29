/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.financial.search;

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

@ViewInterface(PAHealthSearchProcessClaimFinancialsView.class)
public class PAHealthSearchProcessClaimFinancialsPresenter extends AbstractMVPPresenter<PAHealthSearchProcessClaimFinancialsView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "pa_health_financial_do_search";
	
	@EJB
	private PAHealthSearchProcessClaimFinancialsService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PAHealthSearchProcessClaimFinancialsFormDTO searchFormDTO = (PAHealthSearchProcessClaimFinancialsFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
