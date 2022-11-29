/**
 * 
 */
package com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel;

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

@ViewInterface(SearchCloseClaimRODLevelView.class)
public class SearchCloseClaimPresenterRODLevel extends AbstractMVPPresenter<SearchCloseClaimRODLevelView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchsspsTable";
	
	@EJB
	private SearchCloseClaimServiceRODLevel searchAcknowledgementDocumentReceiverService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCloseClaimFormDTORODLevel searchFormDTO = (SearchCloseClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchAcknowledgementDocumentReceiverService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
