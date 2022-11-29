package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;

public interface CancelAcknowledgementWizardView extends GMVPView, GWizardListener {
	
	public void buildSuccessLayout();

	public void initView(ReceiptOfDocumentsDTO rodDTO);


}
