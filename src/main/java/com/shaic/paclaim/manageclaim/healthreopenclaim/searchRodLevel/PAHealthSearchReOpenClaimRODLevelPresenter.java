/**
 * 
 */
package com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel;

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

@ViewInterface(PAHealthSearchReOpenClaimRodLevelView.class)
public class PAHealthSearchReOpenClaimRODLevelPresenter extends AbstractMVPPresenter<PAHealthSearchReOpenClaimRodLevelView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchreopTablePAhealth";
	
	@EJB
	private PAHealthSearchReOpenClaimRODLevelService service;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PAHealthSearchReOpenClaimFormDTORODLevel searchFormDTO = (PAHealthSearchReOpenClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(service.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
