package com.shaic.reimbursement.reassigninvestigation;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ReAssignInvestigationWizard extends GMVPView, GWizardListener {

	void buildSuccessLayout();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	/**
	 * Part of CR R0767
	 * @param alertMsg
	 */
	void showAssignCountAlert(String alertMsg);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);


}
