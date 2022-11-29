package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

@ViewInterface(UploadDocumentsForAckNotReceivedWizardView.class)
public class UploadDocumentsForAckNotReceivedWizard extends AbstractMVPPresenter<UploadDocumentsForAckNotReceivedWizardView>{
	
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_SEARCH_OR_UPLOAD_DOCUMENTS = "submit_search_or_upload_documents_for_ack_not_received";
	
	@EJB
	private UploadDocumentsForAckNotReceivedSubmitService searchUploadDocumentsSubmitService;;

	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_SEARCH_OR_UPLOAD_DOCUMENTS) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();		
		if(null != rodDTO)
		{
			UploadDocumentDTO uploadDto = rodDTO.getUploadDocumentsDTO();
			uploadDto.setUploadDocsList(rodDTO.getUploadDocsList());			
			searchUploadDocumentsSubmitService.submitSearchOrUploadDocumentsForAckNotReceived(uploadDto);
			view.buildSuccessLayout();
		}
		
		
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}
