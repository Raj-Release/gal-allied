package com.shaic.claim.reimbursement.searchuploaddocuments;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

@ViewInterface(SearchUploadDocumentsWizardView.class)
public class SearchUploadDocumentsWizardPresenter extends AbstractMVPPresenter<SearchUploadDocumentsWizardView>{

	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_SEARCH_OR_UPLOAD_DOCUMENTS = "submit_search_or_upload_documents";
	
	@EJB
	private SearchUploadDocumentsSubmitService searchUploadDocumentsSubmitService;;

	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_SEARCH_OR_UPLOAD_DOCUMENTS) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		if(null != rodDTO)
		{
			UploadDocumentDTO uploadDto = rodDTO.getUploadDocumentsDTO();
			uploadDto.setUploadDocsList(rodDTO.getUploadDocsList());
			searchUploadDocumentsSubmitService.submitSearchOrUploadDocuments(uploadDto);
			view.buildSuccessLayout();
		}
		
		
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
}
