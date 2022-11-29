package com.shaic.claim.outpatient.processOP.pages.assessmentsheet;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.outpatient.processOPpages.ProcessOPBillDetailsPage;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Component;

public class OPClaimAssessmentPageViewImpl extends AbstractMVPView implements OPClaimAssessmentPageInterface {

	private static final long serialVersionUID = 5576743706797691367L;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
//	@Inject
//	private OPClaimAssessmentPageUI claimAndDocumentPage;
	
	@Inject
	private ProcessOPBillDetailsPage claimAndDocumentPage;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	@Override
	public String getCaption() {
		return "Billing & Settlement";
	}
	
	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		Component comp =  claimAndDocumentPage.getContent();
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
		if(claimAndDocumentPage.validatePage()){
			claimAndDocumentPage.showErrorMessage(claimAndDocumentPage.getErrMsg().toString());
			return false;
		}else{
			claimAndDocumentPage.removeLayout();
			return true;
		}
	}

	@Override
	public boolean onBack() {
		List<UploadDocumentDTO> uploadedDocs = claimAndDocumentPage.getUploadedDocsTableValues();
		if(uploadedDocs != null && !uploadedDocs.isEmpty()){
			bean.setUploadedDocsTableList(uploadedDocs);
		}
		bean.setApprove(false);
		bean.setReject(false);
		claimAndDocumentPage.removeLayout();
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
		claimAndDocumentPage.init(bean, wizard);
		
	}
	
	@Override
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO) {
		claimAndDocumentPage.loadUploadedDocsTableValues(uploadDocDTO);
	}

	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		claimAndDocumentPage.deleteUploadDocumentDetails(uploadDTO);
	}

	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		claimAndDocumentPage.editUploadedDocumentDetails(uploadDTO);
	}
	
	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
		claimAndDocumentPage.setUpIFSCDetails(viewSearchCriteriaDTO);		
	}

	@Override
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		claimAndDocumentPage.populatePreviousPaymentDetails(tableDTO);
	}

	@Override
	public void setPayableDtls(String payableAt) {
		claimAndDocumentPage.setUpPayableDetails(payableAt);
		
	}
	

}
