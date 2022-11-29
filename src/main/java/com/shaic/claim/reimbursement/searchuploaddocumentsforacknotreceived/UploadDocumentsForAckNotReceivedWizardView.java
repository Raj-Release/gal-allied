package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

public interface UploadDocumentsForAckNotReceivedWizardView extends GMVPView, GWizardListener{
	void buildSuccessLayout(String rrcRequestNo);
	void buildSuccessLayout();

}
