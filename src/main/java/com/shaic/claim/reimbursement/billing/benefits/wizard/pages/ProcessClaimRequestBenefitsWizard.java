/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

/**
 * @author ntv.vijayar
 *
 */
public interface ProcessClaimRequestBenefitsWizard  extends GMVPView, GWizardListener {
	void buildSuccessLayout(String strMessage,Boolean isError);
	//void setWizardPageReferenceData(Map<String,Object> referenceData);
}

