package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;

public interface BenefitDecisionCommunicationWizard extends GMVPView, WizardStep<ReceiptOfDocumentsDTO> {

	void init(ReceiptOfDocumentsDTO bean, GWizard wizard);

}

