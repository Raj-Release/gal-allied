package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.CreateRODServiceForAddAdditionalDocuments;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;

@ViewInterface(ReceivedPhysicalDocumentsWizard.class)
public class ReceivedPhysicalDocumentsWizardPresenter extends AbstractMVPPresenter<ReceivedPhysicalDocumentsWizardViewImpl> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_RECEIVED_PHYSICAL_DOCUMENT_DETAILS = "submit_received_physical_documents";	
	
	
	
	@EJB
	private CreateRODServiceForAddAdditionalDocuments rodService;
		
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_RECEIVED_PHYSICAL_DOCUMENT_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodService.saveDocAcknowledgementDetailsForPhyDoc(rodDTO);
		view.buildSuccessLayout();
	}
		

}
