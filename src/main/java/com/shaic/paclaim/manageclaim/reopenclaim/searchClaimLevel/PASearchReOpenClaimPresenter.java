/**
 * 
 */
package com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel;

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

@ViewInterface(PASearchReOpenClaimView.class)
public class PASearchReOpenClaimPresenter extends AbstractMVPPresenter<PASearchReOpenClaimView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "getSearchreopTablePA";
	
	@EJB
	private PASearchReOpenClaimService service;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchReOpenClaimFormDTO searchFormDTO = (PASearchReOpenClaimFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.resultList(service.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
