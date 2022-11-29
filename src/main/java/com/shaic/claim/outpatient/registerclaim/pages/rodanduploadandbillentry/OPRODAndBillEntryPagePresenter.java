package com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;

@ViewInterface(OPRODAndBillEntryPageInterface.class)
public class OPRODAndBillEntryPagePresenter extends AbstractMVPPresenter<OPRODAndBillEntryPageInterface>{

	private static final long serialVersionUID = 5811691782410656962L;
	
	@EJB
	private MasterService masterService;
	
	public static final String SET_UP_REFERENCE = "op_rod_and_document_details_page_set_up_reference";
	public static final String BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS = "outpatient_bill_entry_submit_uploaded_documents_add_additional_documents";
	public static final String BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS = "outpatient_bill_entry_delete_uploaded_documents_add_additional_documents";	
	public static final String BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS = "outpatient_bill_entry_edit_uploaded_documents_add_additional_documents";
	Map<String, Object> referenceData = new HashMap<String, Object>();

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpReference(
			@Observes @CDIEvent(SET_UP_REFERENCE) final ParameterDTO parameters) {
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		OutPatientDTO dto = (OutPatientDTO) parameters.getPrimaryParameter();
		view.setupReferences(referenceData);
	}
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		view.loadUploadedDocsTableValues(uploadDocDTO);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
	}
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}


}
