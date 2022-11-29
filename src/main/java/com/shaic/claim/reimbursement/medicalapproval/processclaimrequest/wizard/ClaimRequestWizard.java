package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author Saravana Kumar P
 *
 */
public interface ClaimRequestWizard extends GMVPView, GWizardListener {
	void buildSuccessLayout();

	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field , List<EmployeeMasterDTO> employeeDetailsList);
	void buildValidationUserRRCRequestLayout(Boolean isValid);

	void buildFailureLayout();	
	void alertForAlreadyAcquired(String aquiredUser);
	void validationForLimit();
	void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails);	
	void confirmMessageForDefinedLimt(Object rejectionCategoryDropdownValues);
	void buildInitiateLumenRequest(String intimationId);
/*	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2);*/
	
	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2,
			boolean isFVRAssigned, String repName, String repContactNo);
	
	void generateFieldsOnInvtClick(boolean isDirectToAssignInv);
	void alertMessageForInvestigation(boolean isDirectToAssignInv);
	void generateQueryLayout();
	void validateCancelRequest(InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO);

	void validateCancelQueryRequest();

	void validateUploadQueryLetter();

	void validateForEnableParallel(SelectValue selectedValue,SelectValue selectedValue1);

	void validateIllnessEnableParallel(SelectValue selectedValue,SelectValue selectedValue1);
	void generateButtonLayoutBasedOnScoring(ClaimRequestMedicalDecisionButtons buttons);

	void checkPatientStatusForMAQuery();
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);
	void generateFieldsBasedOnImplantApplicable(Boolean isChecked);

	 void generatePPCoadingField(Boolean ischecked,List<PPCodingDTO> codingDTOs,Map<String,Boolean> selectedPPCoding);
	void genertateFieldsBasedOnTypeOfAdmisstion();

	void generateadmissiontypeFields(Boolean displayadmissiontype);

}
