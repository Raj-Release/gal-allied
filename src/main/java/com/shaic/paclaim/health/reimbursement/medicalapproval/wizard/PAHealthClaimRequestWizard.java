package com.shaic.paclaim.health.reimbursement.medicalapproval.wizard;

import java.util.List;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author Saravana Kumar P
 *
 */
public interface PAHealthClaimRequestWizard extends GMVPView, GWizardListener {
	void buildSuccessLayout();

	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field , List<EmployeeMasterDTO> employeeDetailsList);
	void buildValidationUserRRCRequestLayout(Boolean isValid);

	void buildFailureLayout();
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);
	
	
	void generateFieldsOnInvtClick(boolean isDirectToAssignInv);
	void generateQueryLayout();
	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2,
			boolean isFVRAssigned, String repName, String repContactNo);


}
