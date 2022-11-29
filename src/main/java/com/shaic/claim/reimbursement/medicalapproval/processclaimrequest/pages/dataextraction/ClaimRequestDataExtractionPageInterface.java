package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ClaimRequestDataExtractionPageInterface extends GMVPView , WizardStep<PreauthDTO> {
	void init(PreauthDTO bean, GWizard wizard);
	void editSpecifyVisibility(Boolean checkValue);
	void generateFieldsBasedOnTreatment();
	void genertateFieldsBasedOnPatientStaus();
	void genertateFieldsBasedOnHospitalisionDueTo(SelectValue value);
	void generateFieldsBasedOnOtherCoveredClaim(Boolean selectedValue);
	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);
	void setPackageRate(Map<String, String> mappedValues);
	void intiateCoordinatorRequest();
	void generateFieldsBasedOnReportedPolice(Boolean selectedValue);
	void setCompareWithRODResult(String comparisonResult);
	void genertateFieldsBasedOnDomicillaryFields(Boolean selectedValue);
	void setCoverList(BeanItemContainer<SelectValue> coverContainer);
	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void setAssistedReprodTreatment(Long assistValue);
	void generateApproveLayout();
	void generateQueryLayout();

	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateReplyLayout();

	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist);

	void genertateSentToReplyLayout();

	void generateCancelRodLayout() ;
	void showEditBillClassification(Map<String, Object> referenceData);
	void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue);
	void setProcessClaimProcedureValues(
			BeanItemContainer<SelectValue> procedures);
	
	void checkPatientStatusForMAQuery();
	
	void setTreatingQualificationValues(BeanItemContainer<SelectValue> procedures);
	void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO);
}
