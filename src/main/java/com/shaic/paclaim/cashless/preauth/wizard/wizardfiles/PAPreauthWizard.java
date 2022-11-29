package com.shaic.paclaim.cashless.preauth.wizard.wizardfiles;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Investigation;
import com.shaic.domain.State;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAPreauthWizard extends GMVPView {
	
	void setWizardPageReferenceData(Map<String, Object> referenceData);

	void generateFieldsBasedOnTreatment();

	void genertateFieldsBasedOnPatientStaus();

	void generateReferCoOrdinatorLayout();
	
	void genertateFieldsBasedOnRelapseOfIllness(Map<String, Object> referenceData);
	
	void setRelapsedClaims(Map<String, Object> referenceData);

	void generateFieldsOnQueryClick();

	void generateFieldsOnSuggestRejectionClick();

	void generateFieldsOnSendForProcessingClick();
	
	void searchState(List<State> stateList);
	
	void editSpecifyVisibility(Boolean checkValue);
	
	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	
	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);
	
	public void genertateFieldsBasedOnTreatment();
	
	public void genertateFieldsBasedOnFieldVisit(Boolean isChecked, Object allocationToValues, Object beanItemContainer, Object beanItemContainer2);
	
	public void genertateFieldsBasedOnSpecialistChecked(Boolean isChecked, Map<String, Object> map);
	
	public void generatePreauthApproveLayout();
	
	public void generateQueryLayout();
	
	public void generateRejectionLayout(Object rejectionCategoryDropdownValues);
	
	public void generateDenialLayout(Object denialValues);
	
	public void generateEscalateLayout(Object escalateToValues);

	void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList);

	void generateFieldsBasedOnSentTOCPU(Boolean isChecked);

	void viewClaimAmountDetails();
	
	void viewBalanceSumInsured(String intimationId);

	void setExclusionDetails(BeanItemContainer<ExclusionDetails> icdCodeContainer);

	void setPackageRate(Map<String, String> mappedValues);

	void buildSuccessLayout();

	void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName);

	void setInvestigationRule(Investigation checkInitiateInvestigation);

	void setBalanceSumInsured(Double balanceSI, List<Double> copayValue);
	
	void intiateCoordinatorRequest();

	void setDiagnosisName(String diagnosis);

	void setHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	void genertateFieldsBasedOnHospitalisionDueTo(SelectValue selectedValue);

	void genertateFieldsBasedOnReportedToPolice(Boolean selectedValue);

	void setBalanceSIforRechargedProcess(Double balanceSI);

	void setCoverList(BeanItemContainer<SelectValue> coverContainer);

	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
