/**
 * 
 */
package com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel;

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

@ViewInterface(PAHealthSearchReOpenClaimView.class)
public class PAHealthSearchReOpenClaimPresenter extends AbstractMVPPresenter<PAHealthSearchReOpenClaimView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "getSearchreopTablePAhealth";
	
	@EJB
	private PAHealthSearchReOpenClaimService service;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PAHealthSearchReOpenClaimFormDTO searchFormDTO = (PAHealthSearchReOpenClaimFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.resultList(service.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
