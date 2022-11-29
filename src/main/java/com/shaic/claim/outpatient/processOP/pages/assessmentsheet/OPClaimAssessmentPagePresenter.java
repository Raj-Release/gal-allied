package com.shaic.claim.outpatient.processOP.pages.assessmentsheet;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.outpatient.OutpatientService;

@ViewInterface(OPClaimAssessmentPageInterface.class)
public class OPClaimAssessmentPagePresenter extends AbstractMVPPresenter<OPClaimAssessmentPageInterface>{

	private static final long serialVersionUID = 5811691782410656962L;
	public static final String SET_UP_REFERENCE = "process_claim_and_document_assessment_page_set_up_reference";
	public static final String OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS = "op_bill_entry_submit_uploaded_documents";
	public static final String OP_BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS = "op_bill_entry_delete_uploaded_documents";
	public static final String OP_BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS = "op_bill_entry_edit_uploaded_documents";
	public static final String OP_IFSC_DETAILS  = "op_search_ifsc_details";
	public static final String OP_POPULATE_PREVIOUS_ACCT_DETAILS = "op_populate_previous_acct_dtls";
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private OutpatientService outpatientService; 

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
	}
	
	public void setUpReference(@Observes @CDIEvent(SET_UP_REFERENCE) final ParameterDTO parameters) {
		//OutPatientDTO dto = (OutPatientDTO) parameters.getPrimaryParameter();		
		view.setupReferences(referenceData);
	}
	
	public void editUploadedDocumentDetails(@Observes @CDIEvent(OP_BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
	}
	
	public void submitUploadedDocuments(@Observes @CDIEvent(OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		view.loadUploadedDocsTableValues(uploadDocDTO);
	}
	
	public void deleteUploadedDocumentDetails(@Observes @CDIEvent(OP_BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
	}
	
	public void setupIFSCDetails(
			@Observes @CDIEvent(OP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(OP_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters){
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
	}
	
}
