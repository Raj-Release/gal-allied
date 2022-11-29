package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthClaimRequestDataExtractionPageInterface extends GMVPView , WizardStep<PreauthDTO> {
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
	
	void generateCancelRodLayout();
	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateEscalateReplyLayout();
	void generateReferCoOrdinatorLayout(
				BeanItemContainer<SelectValue> selectValueContainer);
	void genertateSpecialistLayout(
				BeanItemContainer<SelectValue> selectValueContainerForSpecialist);
	void genertateSentToReplyLayout();
}
