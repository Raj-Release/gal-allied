package com.shaic.reimbursement.queryrejection.draftquery.search;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnDraftQueryWizard extends GMVPView, GWizardListener {
	
		void buildSuccessLayout();
		void submitQuery(ParameterDTO parameter);
	
}
