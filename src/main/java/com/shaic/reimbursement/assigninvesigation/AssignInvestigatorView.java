package com.shaic.reimbursement.assigninvesigation;

import java.util.List;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;

public interface AssignInvestigatorView extends GMVPView,WizardStep<AssignInvestigatorDto> {
	public void init(AssignInvestigatorDto bean, GWizard wizard);
	public List<AssignInvestigatorDto> getMultiInvestigators();
	/**
	 * Part of CR R0767
	 * @param alertMsg
	 */
	public void showAssignCountAlert(String alertMsg);
	public void getAssignCountAlert();

}
