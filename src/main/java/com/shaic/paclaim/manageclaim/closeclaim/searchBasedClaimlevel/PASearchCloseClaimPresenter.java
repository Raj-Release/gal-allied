/**
 * 
 */
package com.shaic.paclaim.manageclaim.closeclaim.searchBasedClaimlevel;

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

@ViewInterface(PASearchCloseClaimView.class)
public class PASearchCloseClaimPresenter extends AbstractMVPPresenter<PASearchCloseClaimView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchTablePA";
	
	@EJB
	private PASearchCloseClaimService pASearchCloseClaimService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchCloseClaimFormDTO searchFormDTO = (PASearchCloseClaimFormDTO) parameters.getPrimaryParameter();

		
		view.getResultList(pASearchCloseClaimService.search(searchFormDTO));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
