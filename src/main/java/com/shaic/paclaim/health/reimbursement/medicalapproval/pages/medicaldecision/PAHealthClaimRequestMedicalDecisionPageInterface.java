package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Investigation;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthClaimRequestMedicalDecisionPageInterface  extends GMVPView , WizardStep<PreauthDTO> {
	void init(PreauthDTO bean, GWizard wizard);

	void viewBalanceSumInsured(String intimationId);

	void viewClaimAmountDetails();

	void setPreAuthRequestedAmt(String calculatePreRequestedAmt);
	
	void generateApproveLayout();
	
	void generateCancelRodLayout();

	void generateQueryLayout();

	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateReplyLayout();

	void generateFieldsBasedOnSentTOCPU(Boolean isChecked);

	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2);

	void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist);

	void genertateSentToReplyLayout();

	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);

	void setDiagnosisSumInsuredValuesFromDB(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);

	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);

	void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO);

	void setBillEntryAmountConsiderValue(Double sumValue);

//	void setInvestigationRule(Investigation checkInitiateInvestigation);
	
//	void setInvestigationRule(Boolean isPending, Investigation checkInitiateInvestigation);

	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
}
