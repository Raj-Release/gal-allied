package com.shaic.claim.reimbursement.talktalktalk;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface InitiateTalkTalkTalkWizardView extends GMVPView, GWizardListener{

	void builTalkTalkTalkSuccessLayout();
	void enabledCallButtons();
}
