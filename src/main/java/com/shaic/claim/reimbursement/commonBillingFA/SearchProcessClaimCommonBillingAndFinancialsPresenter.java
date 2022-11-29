/**
 * 
 */
package com.shaic.claim.reimbursement.commonBillingFA;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsFormDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchProcessClaimCommonBillingAndFinancialsView.class)
public class SearchProcessClaimCommonBillingAndFinancialsPresenter extends AbstractMVPPresenter<SearchProcessClaimCommonBillingAndFinancialsView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchCommBillFATable";
	
	@EJB
	private SearchProcessClaimCommonBillingAndFinancialsService searchService;

	
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
