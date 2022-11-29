/**
 * 
 */
package com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel;

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

@ViewInterface(PASearchReOpenClaimRodLevelView.class)
public class PASearchReOpenClaimRODLevelPresenter extends AbstractMVPPresenter<PASearchReOpenClaimRodLevelView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchreopTablePA";
	
	@EJB
	private PASearchReOpenClaimRODLevelService service;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchReOpenClaimFormDTORODLevel searchFormDTO = (PASearchReOpenClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(service.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
