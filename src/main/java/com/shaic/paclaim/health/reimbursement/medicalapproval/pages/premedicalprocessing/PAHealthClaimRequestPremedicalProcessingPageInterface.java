package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.premedicalprocessing;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthClaimRequestPremedicalProcessingPageInterface extends GMVPView , WizardStep<PreauthDTO>{
	void init(PreauthDTO bean, GWizard wizard);
	void setUpReference(Map<String,Object> referenceDataMap);
	void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer);
	void generateFieldsOnQueryClick();
	void generateFieldsOnSuggestRejectionClick();
	void generateFieldsOnSuggestApprovalClick();
	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
}
