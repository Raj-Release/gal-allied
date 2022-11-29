package com.shaic.reimbursement.reassigninvestigation;

import java.util.List;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;

public interface ReAssignInvestigatorView extends GMVPView,WizardStep<AssignInvestigatorDto> {
	void init(AssignInvestigatorDto bean, GWizard wizard);
	List<AssignInvestigatorDto> getMultiInvestigators();
	/**
	 * As part of R0676
	 */
	public void getAssignCountAlert();
	/**
	 * Part of CR R0767
	 * @param alertMsg
	 */
	public void showAssignCountAlert(String alertMsg);

}
