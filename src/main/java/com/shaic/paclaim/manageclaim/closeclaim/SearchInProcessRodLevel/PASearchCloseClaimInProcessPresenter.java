package com.shaic.paclaim.manageclaim.closeclaim.SearchInProcessRodLevel;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimFormDTORODLevel;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimServiceRODLevel;


@ViewInterface(PASearchCloseClaimInProcessView.class)
public class PASearchCloseClaimInProcessPresenter extends AbstractMVPPresenter<PASearchCloseClaimInProcessView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for PA Close Claim In process";
	
	@EJB
	private PASearchCloseClaimServiceRODLevel searchAcknowledgementDocumentReceiverService;

	
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PASearchCloseClaimFormDTORODLevel searchFormDTO = (PASearchCloseClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchAcknowledgementDocumentReceiverService.searchBPMNTask(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
