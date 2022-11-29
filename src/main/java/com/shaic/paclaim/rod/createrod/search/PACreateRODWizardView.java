package com.shaic.paclaim.rod.createrod.search;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PACreateRODWizardView extends GMVPView, GWizardListener {

	void buildSuccessLayout();
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void buildFailureLayout(String message);
	//void setWizardPageReferenceData(Map<String,Object> referenceData);
	void buildSuccessLayout(String docToken);
	void showDocViewPopup(Panel e);
	void buildSuccessLayoutForUpdateRod();
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
