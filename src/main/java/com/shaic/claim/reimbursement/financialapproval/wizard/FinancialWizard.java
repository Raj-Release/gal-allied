package com.shaic.claim.reimbursement.financialapproval.wizard;

import java.util.List;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.TextField;
import com.zybnet.autocomplete.server.AutocompleteField;

public interface FinancialWizard extends GMVPView, GWizardListener {
	void buildSuccessLayout();
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field , List<EmployeeMasterDTO> employeeDetailsList);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void buildFailureLayout();
	void buildPaymentFailureLayout();
	void alertForAlreadyAcquired(String aquiredUser);
	void confirmMessageForApprovedAmount(String message);
	void confirmMessageFor64VBstatus(String message);
	void validationForLimit();
	void confirmMessageForDefinedLimt(Object rejectionCategoryDropdownValues);
	void confirmMessageForDefinedLimtForFirst(
			Object rejectionCategoryDropdownValues);
	void validateClaimedAmount(TextField txtField);
	void buildAlreadyExist();
	void buildInitiateLumenRequest(String intimationId);

	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
