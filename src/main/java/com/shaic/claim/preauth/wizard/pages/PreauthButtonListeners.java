package com.shaic.claim.preauth.wizard.pages;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingUI;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PreauthButtonListeners {

	void generateFieldsForQuery();
	
	void generateFieldsForSuggesRejection(Boolean showChangeInPreauth);
	
	void generateFieldsForSendForProcessing(Boolean showChangeInPreauth);
	
	void generateFieldsForSuggestApproval();
	
	void generateFieldsForReimbursementQuery();
	
	void generateFieldsForSuggesRejectionForReimbursement(Boolean showChangeInPreauth, BeanItemContainer<SelectValue> dropDownValues);
	
	void generateFieldsForInitiateInvestigation(String IntimationNo, Boolean InvRequired,Long stageKey,MedicalApprovalPremedicalProcessingUI parent);
	
	void generateFieldsForReferToBillEntry();
}
