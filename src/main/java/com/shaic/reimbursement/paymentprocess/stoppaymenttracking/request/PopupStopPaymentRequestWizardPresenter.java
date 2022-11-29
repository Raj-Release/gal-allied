package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.reimbursement.paymentprocess.paymentreprocess.PaymentReprocessSearchResultDTO;


@ViewInterface(PopupStopPaymentRequestWizard.class)
public class PopupStopPaymentRequestWizardPresenter extends AbstractMVPPresenter<PopupStopPaymentRequestWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String POPUP_CANCEL_REQUEST = "popup_cancel_payment_request";
	public static final String POPUP_SUBMIT_REQUEST = "popup_submit_payment_request";
	public static final String POPUP_OTHER_REAMARKS_FIELD = "other_remarks_field";
	
	protected static final String SUBMIT_UPLOADED_DOCUMENTS = "submit_upload documents";
	
	protected static final String DELETE_UPLOADED_DOCUMENTS = "delete_upload documents";
	
	protected static final String EDIT_UPLOADED_DOCUMENTS = "edit_upload documents";
	@EJB
	private StopPaymentRequestService requestService; 
	
	@Override
	public void viewEntered() {
		System.out.println("Popup PopupStopPaymentRequestWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(POPUP_CANCEL_REQUEST) final ParameterDTO parameters) {
		view.cancelStopPaymentRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(POPUP_SUBMIT_REQUEST) final ParameterDTO parameters) {
		StopPaymentRequestDto tableDto = (StopPaymentRequestDto) parameters.getPrimaryParameter();
		requestService.submitStopReqeust(tableDto);
		view.submitStopPaymentRequest();
	}
	
	public void editUploadedDocumentDetails(@Observes @CDIEvent(EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
	}
	
	public void submitUploadedDocuments(@Observes @CDIEvent(SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		view.loadUploadedDocsTableValues(uploadDocDTO);
	}
	
	public void deleteUploadedDocumentDetails(@Observes @CDIEvent(DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
	}

	

}
