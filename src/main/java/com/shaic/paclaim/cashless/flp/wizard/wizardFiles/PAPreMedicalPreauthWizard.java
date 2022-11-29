package com.shaic.paclaim.cashless.flp.wizard.wizardFiles;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.State;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

public interface PAPreMedicalPreauthWizard extends GMVPView {

	void setWizardPageReferenceData(Map<String, Object> referenceData);

	void generateFieldsBasedOnTreatment();

	void genertateFieldsBasedOnPatientStaus();

	void generateReferCoOrdinatorLayout();
	
	void genertateFieldsBasedOnRelapseOfIllness(Map<String, Object> referenceData);
	
	void generateFieldsOnQueryClick();

	void generateFieldsOnSuggestRejectionClick();

	void generateFieldsOnSendForProcessingClick();
	
	void searchState(List<State> stateList);
	
	void editSpecifyVisibility(Boolean checkValue);
	
	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	
	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);

	public void setRelapsedClaims(Map<String, Object> referenceData);
	
	public void getPreviousClaimDetails(List<PreviousClaimsTableDTO> previousClaimsDTO);

	void setPackageRate(Map<String, String> mappedValues);

	void setExclusionDetails(BeanItemContainer<ExclusionDetails> icdCodeContainer);

	void intiateCoordinatorRequest();
	
	void buildSuccessLayout();

	void setHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);
	
	 void setCustomDiagValueToContainer(SelectValue selValue,ComboBox cmbBox);
	 
	 
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	// void setCustomDiagValueToContainer(BeanItemContainer<SelectValue> selectValue,ComboBox cmbBox,Long key);

	void genertateFieldsBasedOnHospitalisionDueTo(SelectValue selectedValue);

	void genertateFieldsOnReportedPolice(Boolean selectedValue);

	void setCoverList(BeanItemContainer<SelectValue> coverContainer);

	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

	
	
}
