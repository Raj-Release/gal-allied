/**
 * 
 */
package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

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

@ViewInterface(SearchAcknowledgementDocumentReceiverView.class)
public class SearchAcknowledgementDocumentReceiverPresenter extends AbstractMVPPresenter<SearchAcknowledgementDocumentReceiverView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchAckCRTable";
	
	@EJB
	private SearchAcknowledgementDocumentReceiverService searchAcknowledgementDocumentReceiverService;

	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAcknowledgementDocumentReceiverFormDTO searchFormDTO = (SearchAcknowledgementDocumentReceiverFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchAcknowledgementDocumentReceiverService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
