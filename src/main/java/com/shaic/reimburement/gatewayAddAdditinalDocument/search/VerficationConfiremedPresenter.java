package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(VerficationConfiremedView.class)
public class VerficationConfiremedPresenter extends AbstractMVPPresenter<VerficationConfiremedView>{

	


	public static final String BILL_ENTRY_DETAILS_PAGE_UPLOADED_DOC_SETUP_DROPDOWN_VALUES = "bill_entry_details_page_uploaded_doc_setup_dropdown_values_add_additional_documents";
	
	
	public static final String ADD_ADDITIONAL_SAVE_BILL_ENTRY_VALUES = "add_additional_save_bill_entry_values";
	
	public static final String ADD_ADDITIONAL_LOAD_BILL_DETAILS_VALUES = "add_additional_load_bill_details_values";
	
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private CreateRODService rodService;
	
	/*@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;*/

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void saveBillValues(
			@Observes @CDIEvent(ADD_ADDITIONAL_SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	
	
	}
	
	
