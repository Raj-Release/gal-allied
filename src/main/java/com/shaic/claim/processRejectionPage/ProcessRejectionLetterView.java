package com.shaic.claim.processRejectionPage;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;

public interface ProcessRejectionLetterView extends GMVPView, WizardStep<ProcessRejectionDTO> {

	void openPdfFileInWindow(Claim claimByIntimation, PreauthDTO preauthDTO);
	

}
