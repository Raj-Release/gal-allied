/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;

/**
 * @author ntv.srikanthp
 *
 */
@ViewInterface(WizardView.class)
public class WizardPresenter extends AbstractMVPPresenter<WizardViewImpl>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_ADD_ADDITIONAL_DOCUMENTS_DETAILS = "submit_add_additional_documents";
	
	
	@EJB
	private CreateRODServiceForAddAdditionalDocuments rodService;
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_ADD_ADDITIONAL_DOCUMENTS_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodService.submitBillEntryValues(rodDTO);
		view.buildSuccessLayout();
	}
	

	
}
