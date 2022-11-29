package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessClaimRequestBenefitsDecisionView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>  {
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
	//void setUpCoPayValues(Map<String, Object> referenceDataMap);
	void init(ReceiptOfDocumentsDTO bean, GWizard wizard);	 
	void resetPage();
	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	
	void generateSendToFinancialLayout();
	
	void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer);
	void generateReferCoOrdinatorLayoutForFA(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateApproveLayoutForFA();
	void generateCancelRODLayoutForFA(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateRejectLayoutForFA();
	void genertateReferToBillingLayoutForFA();
	void generateQueryLayoutForFA();
	public void genrateFieldVisitLayoutForMA(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isFVRAssigned, String repName, String repContactNo);
	void generateQueryLayoutForMA();
	void generateInvestigationLayoutForMA(boolean isDirectToAssignInv);
	void generateCancelRODLayoutForMA();
	void generateRejectLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer);
	void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer);
	void generateApproveLayoutForMA();
	void generateReferToCoordinatorLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer);
	void generateReferToMedicalLayoutForFA();
	void generateSendReplyToFinancial();
	
	
}

