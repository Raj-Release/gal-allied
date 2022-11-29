package com.shaic.reimbursement.processi_investigationi_initiated;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.OptionGroup;

public interface ProcessInvestigationInitiatedView extends GMVPView {
	
	public void setReference(Map<String, Object> referenceObj);
	
	public void setLayout();
	
	public void finalResult(Boolean flag);
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	public void genertateFieldsBasedOnFieldVisit(Boolean isChecked,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2,
			BeanItemContainer<SelectValue> selectValueContainer3,
			OptionGroup intiateFvrOption);
	
	public void showErrorMessage(String errMsg);

	public void buildInitiateLumenRequest(String intimationId);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
