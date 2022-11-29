package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ClaimRequestPremedicalProcessingPageInterface extends GMVPView , WizardStep<PreauthDTO>{
	void init(PreauthDTO bean, GWizard wizard);
	void setUpReference(Map<String,Object> referenceDataMap);
	void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer);
	void generateFieldsOnQueryClick();
	void generateFieldsOnSuggestRejectionClick();
	void generateFieldsOnSuggestApprovalClick();
	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
	
	void generateApproveLayout();
	
	void generateQueryLayout();

	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateReplyLayout();

	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist);

	void genertateSentToReplyLayout();

	void generateCancelRodLayout() ;
}
