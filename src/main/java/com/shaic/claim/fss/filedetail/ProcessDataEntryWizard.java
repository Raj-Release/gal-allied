/**
 * 
 */
package com.shaic.claim.fss.filedetail;

import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

/**
 * @author ntv.vijayar
 *
 */
public interface ProcessDataEntryWizard extends GMVPView, GWizardListener,WizardStep<PreauthDTO> {
	
	void buildSuccessLayout(String rrcRequestNo);

}
