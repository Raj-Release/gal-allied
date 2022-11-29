package com.shaic.claim.viewEarlierRodDetails;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;

@ViewInterface(EarlierRodDetailsView.class)
public class EarlierRodDetailsPresenter extends AbstractMVPPresenter<EarlierRodDetailsView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SET_TABLE_VALUE = "Document Details Table View";
	
	@EJB
	private AcknowledgementDocumentsReceivedService docAcknowledgementService;

	@Override
	public void viewEntered() {
		
		// TODO Auto-generated method stub
		
	}
	
	public void setTableValues(@Observes @CDIEvent(SET_TABLE_VALUE) final ParameterDTO parameters)
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		
		Long rodKey = (Long) parameters.getSecondaryParameter(0, Long.class);
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = docAcknowledgementService.listOfEarlierAckByClaimKey(claimKey,rodKey);
		
		view.setTableList(listDocumentDetails);
		
		
	}

}
