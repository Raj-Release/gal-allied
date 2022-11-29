package com.shaic.claim.withdrawWizard;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.preauth.MasterRemarks;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface WithdrawPreauthWizard extends GMVPView {

	void buildSuccessLayout();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	void buildWithdrawFields(BeanItemContainer<SelectValue> selectValueContainer);

	void buildWithdrawAndRejctFields(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2);

	void buildWithdrawAndRejctFields(
			BeanItemContainer<SelectValue> selectValueContainer);
	
	void setRemarks(MasterRemarks remarks, String decision);

	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
