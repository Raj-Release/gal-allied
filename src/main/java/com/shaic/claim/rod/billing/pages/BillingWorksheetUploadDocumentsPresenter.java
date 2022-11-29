/**
 * 
 */
package com.shaic.claim.rod.billing.pages;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.ui.Window;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(BillingWorksheetUploadDocumentsView.class)
public class BillingWorksheetUploadDocumentsPresenter extends AbstractMVPPresenter<BillingWorksheetUploadDocumentsView>  {
	public static final String SUBMIT_BILLING_UPLOAD_DOCUMENTS = "submit_billing_upload_documents";
	public static final String DELETE_BILLING_UPLOADED_DOCUMENTS = "delete_billing_uploaded_documents";
	public static final String SAVE_BILLING_UPLOADED_DOCUMENTS = "save_billing_uploaded_documents";
	
	public static final String RETREIVE_VALUES_FOR_UPLOADED_TABLE = "retrieve_values_for_uploaded_table";

	@EJB
	private ReimbursementService reimbursementService;
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(SUBMIT_BILLING_UPLOAD_DOCUMENTS) final ParameterDTO parameters) {
		
		//ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		//List<UploadDocumentDTO> uploadDocLst = (List<UploadDocumentDTO>) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDoc = (UploadDocumentDTO) parameters.getPrimaryParameter();
		//view.loadUploadedDocsTableValues(uploadDocLst);
		view.loadUploadedDocsTableValues(uploadDoc);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(DELETE_BILLING_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
		//List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
	//view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	public void submitUploadDocumentValues(
			@Observes @CDIEvent(SAVE_BILLING_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		//UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Window popup = (Window) parameters.getSecondaryParameter(0, Window.class);
		//Boolean isSuccess = reimbursementService.saveBillingWorksheetUploadDocumentValues(preauthDTO);
		String isSuccess = reimbursementService.saveBillingWorksheetUploadDocumentValues(preauthDTO);
		view.buildSuccessLayout(isSuccess, popup);
		
		//List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		//view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	
	public void retreiveUploadedDocumentValues(
			@Observes @CDIEvent(RETREIVE_VALUES_FOR_UPLOADED_TABLE) final ParameterDTO parameters) {
		
		//UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		
		Long reimbursementKey = (Long) parameters.getPrimaryParameter();
		List<UploadDocumentDTO> uploadDocDTOList = reimbursementService.getRodBillSummaryDetails(reimbursementKey);
		view.setUploadedDocsTableValues(uploadDocDTOList);
		//PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		/*Window popup = (Window) parameters.getSecondaryParameter(0, Window.class);
		Boolean isSuccess = reimbursementService.saveBillingWorksheetUploadDocumentValues(preauthDTO);*/
		//view.buildSuccessLayout(isSuccess, popup);
		
		//List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		//view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
