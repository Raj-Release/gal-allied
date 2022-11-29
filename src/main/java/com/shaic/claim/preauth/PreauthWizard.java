package com.shaic.claim.preauth;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.State;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PreauthWizard extends GMVPView {
	
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
	
	void setProcedureValues(BeanItemContainer<SelectValue> prodCont);
	
	public void genertateFieldsBasedOnTreatment();
	
	public void genertateFieldsBasedOnFieldVisit(Boolean isChecked, Object allocationToValues, Object beanItemContainer, Object beanItemContainer2, List<ViewFVRDTO> fvrDTOList);
	
	public void genertateFieldsBasedOnAssignFieldVisit(Boolean isChecked, String repName, String repContactNo);
	
	public void showFVRnotPosssible();
	
	public void showFVRPosssible();
	
	public void genertateFieldsBasedOnSpecialistChecked(Boolean isChecked, Map<String, Object> map);
	
	public void generatePreauthApproveLayout();
	
	public void generateQueryLayout();
	
	public void generateRejectionLayout(Object rejectionCategoryDropdownValues);
	
	public void generateDenialLayout(Object denialValues);
	
	public void generateCashlessNotReqLayout();
	
	public void generateEscalateLayout(Object escalateToValues);

	void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList);

	void generateFieldsBasedOnSentTOCPU(Boolean isChecked);

	void viewClaimAmountDetails();
	
	void viewBalanceSumInsured(String intimationId);

	void setExclusionDetails(BeanItemContainer<ExclusionDetails> icdCodeContainer);

	void setPackageRate(Map<String, String> mappedValues);

	void buildSuccessLayout();
	
	void buildFailureLayout(String acquiredUser);

	void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName);

	void setInvestigationRule(Boolean checkInitiateInvestigation);

	void setBalanceSumInsured(Double balanceSI, List<Double> copayValue);
	
	void intiateCoordinatorRequest();
	
	void setFVRPendingStatus(boolean pending);

	void setDiagnosisName(String diagnosis);

	void setHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

//	void genertateFieldsBasedOnHospitalisionDueTo(SelectValue selectedValue);
	
//	void genertateFieldsBasedOnHospitalisionDueTo(PreauthDTO bean);
	void genertateFieldsBasedOnHospitalisionDueTo();
	
	void genertateFieldsBasedOnObterBenefits(PreauthDTO bean);

	void genertateFieldsBasedOnReportedToPolice(Boolean selectedValue);

	void setBalanceSIforRechargedProcess(Double balanceSI);

	void setCoverList(BeanItemContainer<SelectValue> coverContainer);

	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);

	void setRemarks(MasterRemarks remarks,Boolean isReject);

	void setPreviousClaimDetailsForPolicy(List<PreviousClaimsTableDTO> previousClaimDTOList);
	
	void generate64VBComplainceLayout();

	void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails,
			Map<String, Object> referenceData);
	
	void generatePTCACABGLayout(Boolean value);

	void checkPanCardDetails(Boolean isAvailable);
	
	void generateOtherBenefitsPopup();

	void validationForLimitAmt(String acquiredUser);
	
	void generateCPUUserLayout(Object cpuSuggestionDropdownValues);
	
	void setAssistedReprodTreatment(Long assistValue);

	void buildInitiateLumenRequest(String intimationNumber);
	
	public void generateHoldLayout();
	
	public void generateButtonLayoutBasedOnScoring(ProcessPreAuthButtonLayout buttons);
	
	public void setPreauthQryRemarks(String remarks);

	public void generateTopUpLayout(List<PreauthDTO> topupData);
	
	public void generateTopUpIntimationLayout(String topUpIntimaiton);
	
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer);
	
	void setTreatingQualificationValues(BeanItemContainer<SelectValue> qualification);
	
	public void addCategoryValues(SelectValue categoryValues);
	
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);

	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);
	
	void setnegotiatedSavedAmount();
	
	void setAppAmountBSIAlert();
	
	void removedynamicComp();
	
	void generateFieldsBasedOnImplantApplicable(Boolean isChecked);
	
	void generateFieldsBasedOnSubCatTWO(BeanItemContainer<SelectValue> procedure);
	
	void addpccSubCategory(BeanItemContainer<SelectValue> procedure);

}
