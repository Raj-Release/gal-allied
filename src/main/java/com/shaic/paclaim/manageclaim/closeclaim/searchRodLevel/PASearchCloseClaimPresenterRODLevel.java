/**
 * 
 */
package com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel;

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

@ViewInterface(PASearchCloseClaimRODLevelView.class)
public class PASearchCloseClaimPresenterRODLevel extends AbstractMVPPresenter<PASearchCloseClaimRODLevelView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchsspsTablePA";
	
	@EJB
	private PASearchCloseClaimServiceRODLevel searchAcknowledgementDocumentReceiverService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchCloseClaimFormDTORODLevel searchFormDTO = (PASearchCloseClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchAcknowledgementDocumentReceiverService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
