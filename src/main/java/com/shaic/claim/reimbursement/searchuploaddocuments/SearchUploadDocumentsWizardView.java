package com.shaic.claim.reimbursement.searchuploaddocuments;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

public interface SearchUploadDocumentsWizardView extends GMVPView, GWizardListener{
	void buildSuccessLayout(String rrcRequestNo);
	void buildSuccessLayout();
}
