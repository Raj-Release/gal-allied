package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverFormDTO;

@ViewInterface(PASearchAckDocumentReceiverView.class)
public class PASearchAckDocumentReceiverPresenter extends AbstractMVPPresenter<PASearchAckDocumentReceiverView>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchAckreceiveTable_PA";
	
	@EJB
	private PASearchAckDocumentReceiverService searchAcknowledgementDocumentReceiverService;

	
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
