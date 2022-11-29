package com.shaic.claim.outpatient.processOPpages.assessmentsheet;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.vaadin.ui.Component;

public class OPBillAssessmentPageViewImpl extends AbstractMVPView implements OPBillAssessmentPageInterface {

	private static final long serialVersionUID = 5576743706797691367L;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private OPBillAssessmentPageUI billAssessmentPage;
	
//	@Inject
//	private ProcessOPBillDetailsPage claimAndDocumentPage;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	@Override
	public String getCaption() {
		return "Bill Assessment Sheet";
	}
	
	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		Component comp =  billAssessmentPage.getContent();
		//setCompositionRoot(comp);
//		fireViewEvent(OPClaimAssessmentPagePresenter.SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
//		claimAndDocumentPage.setupReferences(referenceData);
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}

	@Override
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		billAssessmentPage.init(bean, wizard);
		
	}
	
//	@Override
//	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO) {
//		claimAndDocumentPage.loadUploadedDocsTableValues(uploadDocDTO);
//	}
//
//	@Override
//	public void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
//		claimAndDocumentPage.deleteUploadDocumentDetails(uploadDTO);
//	}
//
//	@Override
//	public void editUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
//		claimAndDocumentPage.editUploadedDocumentDetails(uploadDTO);
//	}

}
