package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard;


import java.util.List;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;
/**
 * @author Saravana Kumar P
 *
 */
public interface MedicalApprovalZonalReviewWizard extends GMVPView, GWizardListener {
	void buildSuccessLayout();
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field , List<EmployeeMasterDTO> employeeDetailsList);
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void buildFailureLayout();
	void setUpdateOtherClaimsDetails(List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails);
	void buildInitiateLumenRequest(String intimationId);
	void generateFieldsOnInvtClick(boolean directToAssignInv);
	void alertMessageForInvestigation();
	void confirmationForInvestigation(boolean directToAssignInv);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

/*	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2);
	*/
}
