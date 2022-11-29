package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.OMPReimbursement;

@ViewInterface(OMPEarlierRodDetailsView.class)
public class OMPEarlierRodDetailsPresenter extends AbstractMVPPresenter<OMPEarlierRodDetailsView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SET_OMP_TABLE_VALUE = "Omp Document Details Table View";
	
	@EJB
	private OMPReceiptofDocumentsAndBillEntryService docAcknowledgementService;

	@Override
	public void viewEntered() {
		
		// TODO Auto-generated method stub
		
	}
	
	public void setTableValues(@Observes @CDIEvent(SET_OMP_TABLE_VALUE) final ParameterDTO parameters)
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		
		Long rodKey = (Long) parameters.getSecondaryParameter(0, Long.class);
		
		if(claimKey != null && claimKey != 0l){
		
		OMPReimbursement reimbObj = docAcknowledgementService.getReimbursement(rodKey);
			List<ViewDocumentDetailsDTO> listDocumentDetails = docAcknowledgementService.listOfEarlierAckByClaimKey(claimKey,rodKey);
			view.setTableList(listDocumentDetails,reimbObj != null && reimbObj.getInrConversionRate() != null ? reimbObj.getInrConversionRate(): 0d);
		}	
		
	}

}
