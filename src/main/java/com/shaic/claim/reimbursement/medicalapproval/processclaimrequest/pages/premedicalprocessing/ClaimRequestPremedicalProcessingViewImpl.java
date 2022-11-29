package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class ClaimRequestPremedicalProcessingViewImpl extends AbstractMVPView 
implements ClaimRequestPremedicalProcessingPageInterface {
	private static final long serialVersionUID = -6487966219956247208L;

	private PreauthDTO bean;
	
	private GWizard wizard;

	@Inject
	private ClaimRequestPremedicalProcessingUI premedicalProcessingPage;
	
	@Override
	public String getCaption() {
		return "Medical Processing";
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	@Override
	public void init(PreauthDTO bean,GWizard wizard) {
		
		this.bean = bean;
		this.wizard = wizard;
	}

	@Override
	public Component getContent() {
		premedicalProcessingPage.init(bean,this.wizard);
		Component comp =  premedicalProcessingPage.getContent();
		fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE, this.bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		premedicalProcessingPage.setupReferences(referenceData);
	}

	@Override
	public boolean onAdvance() {
		return premedicalProcessingPage.validatePage();
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
	public void setUpReference(Map<String, Object> referenceDataMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionDetails) {
		premedicalProcessingPage.setExclusionDetails(exclusionDetails);
		
	}

	@Override
	public void generateFieldsOnQueryClick() {
//		premedicalProcessingPage.generateButtonFields(SHAConstants.QUERY);
	}

	@Override
	public void generateFieldsOnSuggestRejectionClick() {
//		premedicalProcessingPage.generateButtonFields(SHAConstants.REJECT);
		
	}

	@Override
	public void generateFieldsOnSuggestApprovalClick() {
		
//		premedicalProcessingPage.generateButtonFields(SHAConstants.APPROVAL);
		
	}

	@Override
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
//		premedicalProcessingPage.setMedicalDecisionTableValues(dto, medicalDecisionTableValues);
		
	}
	
	public void setClearReferenceData(){
		premedicalProcessingPage.setClearReferenceData();
    }
	@Override
	public void generateApproveLayout() {
		
		premedicalProcessingPage.generateButton(10, null);
		
	}
	
	@Override
	public void generateCancelRodLayout() {
		premedicalProcessingPage.generateButton(11, null);
		
	}
	
	@Override
	public void generateQueryLayout() {
		premedicalProcessingPage.generateButton(8, null);
		
	}

	@Override
	public void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		premedicalProcessingPage.generateButton(9, selectValueContainer);
		
	}

	@Override
	public void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		premedicalProcessingPage.generateButton(6, selectValueContainer);
		
	}

	@Override
	public void generateEscalateReplyLayout() {
		premedicalProcessingPage.generateButton(5, null);
		
	}
	
	@Override
	public void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		premedicalProcessingPage.generateButton(4, selectValueContainer);
		
	}
	
	@Override
	public void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist) {
		premedicalProcessingPage.generateButton(7, selectValueContainerForSpecialist);
		
	}

	@Override
	public void genertateSentToReplyLayout() {
		premedicalProcessingPage.generateButton(1, null);
	}

}
