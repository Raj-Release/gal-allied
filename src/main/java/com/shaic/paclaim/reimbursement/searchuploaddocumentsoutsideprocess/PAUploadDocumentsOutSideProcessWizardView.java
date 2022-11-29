package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

public interface PAUploadDocumentsOutSideProcessWizardView extends GMVPView, GWizardListener{

	void buildSuccessLayout(String rrcRequestNo);
	void buildSuccessLayout();
}
