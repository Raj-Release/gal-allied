package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.premedicalprocessing;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class PAHealthClaimRequestPremedicalProcessingViewImpl extends AbstractMVPView 
implements PAHealthClaimRequestPremedicalProcessingPageInterface {
	private static final long serialVersionUID = -6487966219956247208L;

	private PreauthDTO bean;

	@Inject
	private PAHealthClaimRequestPremedicalProcessingUI premedicalProcessingPage;
	
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
	public Component getContent() {
		premedicalProcessingPage.init(bean);
		Component comp =  premedicalProcessingPage.getContent();
		fireViewEvent(PAHealthClaimRequestPremedicalProcessingPagePresenter.MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE, this.bean);
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
	public void init(PreauthDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		
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


}
