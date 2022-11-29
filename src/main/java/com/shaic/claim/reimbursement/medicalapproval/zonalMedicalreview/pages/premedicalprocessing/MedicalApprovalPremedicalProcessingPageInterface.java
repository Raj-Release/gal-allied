package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface MedicalApprovalPremedicalProcessingPageInterface extends GMVPView , WizardStep<PreauthDTO>{
	void init(PreauthDTO bean, GWizard wizard);
	void setUpReference(Map<String,Object> referenceDataMap);
	void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer);
	//void generateFieldsOnInvtClick();
	void generateFieldsOnQueryClick();
	void generateFieldsOnSuggestRejectionClick();
	void generateFieldsOnSuggestApprovalClick();
	void generateFieldsForRefToBillEntryClick();
	void generateFieldsOnCancelRODClick();
	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);
	void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO);
	void setBillEntryAmountConsiderValue(Double sumValue);
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
	//void alertMessageForInvestigation();
	
	void generateFieldsOnInitiateFvrClick();
	
}
