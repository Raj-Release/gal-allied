package com.shaic.reimbursement.manageclaim.closeclaim.SearchInProcessRodLevel;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimFormDTORODLevel;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimServiceRODLevel;


@ViewInterface(SearchCloseClaimInProcessView.class)
public class SearchCloseClaimInProcessPresenter extends AbstractMVPPresenter<SearchCloseClaimInProcessView> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SEARCH_BUTTON_CLICK = "search for Close Claim In process";
	
	@EJB
	private SearchCloseClaimServiceRODLevel searchAcknowledgementDocumentReceiverService;

	
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchCloseClaimFormDTORODLevel searchFormDTO = (SearchCloseClaimFormDTORODLevel) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchAcknowledgementDocumentReceiverService.searchBPMNTask(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
