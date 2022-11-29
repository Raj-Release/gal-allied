/**
 * 
 */
package com.shaic.paclaim.financial.claimapproval.nonhosiptal;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingFormDTO;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(PASearchProcessClaimAprovalNonHosView.class)
public class PASearchProcessClaimAprovalNonHosPresenter extends AbstractMVPPresenter<PASearchProcessClaimAprovalNonHosView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "do_pa_search_claim_aproval_non_hos";
	
	@EJB
	private PASearchProcessClaimAprovalNonHosService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchProcessClaimBillingFormDTO searchFormDTO = (SearchProcessClaimBillingFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
