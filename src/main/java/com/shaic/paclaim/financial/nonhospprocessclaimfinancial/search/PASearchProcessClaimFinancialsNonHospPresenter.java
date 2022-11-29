/**
 * 
 */
package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsFormDTO;



/**
 * 
 *
 */

@ViewInterface(PASearchProcessClaimFinancialsNonHospView.class)
public class PASearchProcessClaimFinancialsNonHospPresenter extends AbstractMVPPresenter<PASearchProcessClaimFinancialsNonHospView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "do_Search_pa_financial_non_hosp";
	
	@EJB
	private PASearchProcessClaimFinancialsNonHospService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessClaimFinancialsFormDTO searchFormDTO = (SearchProcessClaimFinancialsFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
