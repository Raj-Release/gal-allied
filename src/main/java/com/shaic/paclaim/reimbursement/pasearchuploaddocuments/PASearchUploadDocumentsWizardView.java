package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

public interface PASearchUploadDocumentsWizardView extends GMVPView, GWizardListener{

	void buildSuccessLayout(String rrcRequestNo);
	void buildSuccessLayout();

}
