package com.shaic.reimbursement.investigationmaster;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface InvestigationMasterWizardView extends GMVPView, GWizardListener {
	
	public void initView(InvestigationMasterDTO bean);

	void buildSuccessLayout();

}
