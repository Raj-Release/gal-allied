package com.shaic.paclaim.reimbursement.draftquery;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnDraftPAQueryWizard extends GMVPView, GWizardListener {
	
		void buildSuccessLayout();
		void submitQuery(ParameterDTO parameter);
	
}
