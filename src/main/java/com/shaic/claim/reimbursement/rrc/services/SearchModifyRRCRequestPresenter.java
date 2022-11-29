/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

/**
 * @author ntv.vijayar
 *
 */

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(SearchModifyRRCRequestView.class)
public class SearchModifyRRCRequestPresenter extends AbstractMVPPresenter<SearchModifyRRCRequestView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_MODIFY_RRC_REQUEST = "doModifyForProcessRRC";
	
	
	@EJB
	private ModifyRRCRequestService searchService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_MODIFY_RRC_REQUEST) final ParameterDTO parameters) {
		
		SearchModifyRRCRequestFormDTO searchFormDTO = (SearchModifyRRCRequestFormDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	
	
	
	
	@Override
	public void viewEntered() {
		
		
	}

}

