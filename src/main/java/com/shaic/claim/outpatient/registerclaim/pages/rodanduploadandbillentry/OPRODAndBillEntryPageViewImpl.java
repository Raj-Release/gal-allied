package com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Component;

public class OPRODAndBillEntryPageViewImpl extends AbstractMVPView implements OPRODAndBillEntryPageInterface {

	private static final long serialVersionUID = 5576743706797691367L;

	@Inject
	private OPRODAndBillEntryDetailsPageUI opRodAndBillEntryDetailsPage;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	@Override
	public void resetView() {
		init(bean, wizard);
		opRodAndBillEntryDetailsPage.reset();
		
	}
	
	@Override
	public String getCaption() {
		return "Create ROD, Upload Docs & Bill Entry";
	}

	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		Component comp =  opRodAndBillEntryDetailsPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(OPRODAndBillEntryPagePresenter.SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		opRodAndBillEntryDetailsPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		if(opRodAndBillEntryDetailsPage.validatePage()) {
			return true;
		} 
		
		return false;
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		opRodAndBillEntryDetailsPage.init(bean, wizard);
	}

	@Override
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO) {
		opRodAndBillEntryDetailsPage.loadUploadedDocsTableValues(uploadDocDTO);
		
	}

	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		opRodAndBillEntryDetailsPage.deleteUploadDocumentDetails(uploadDTO);
		
	}

	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		opRodAndBillEntryDetailsPage.editUploadedDocumentDetails(uploadDTO);
		
	}

}
