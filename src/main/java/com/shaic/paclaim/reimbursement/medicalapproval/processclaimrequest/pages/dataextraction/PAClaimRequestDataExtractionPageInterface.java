/**
 * 
 */
package com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

/**
 * @author ntv.vijayar
 *
 */

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Investigation;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public interface PAClaimRequestDataExtractionPageInterface extends GMVPView , WizardStep<PreauthDTO> {
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
	void generateApproveLayout();
	void generateCancelRodLayout();
	void generateQueryLayout();
	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateEscalateReplyLayout();
	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2,
			BeanItemContainer<SelectValue> selectValueContainer3);
	void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> masterValueByReference);
	void genertateSentToReplyLayout();
	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
	//void setInvestigationRule(Investigation checkInitiateInvestigation);
	void setPABenefitsTableValues(List<PABenefitsDTO> paBenefitsList,Long benefitsId,Map referenceDataMap,List<PABenefitsDTO> benefitsDBList);
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);
	//void validateBenefitsForClaims(Boolean isValid,String presenterString,Long benefitsId);
	void validateCoversAndBenefitsForClaims(Boolean isValid,String presenterString,Long coverId,TextField eligiblityFld , TextField amtFld, ComboBox categoryCombo);
	void loadPABenefitsValues(List<PABenefitsDTO> paBenefitsList,GComboBox cmb);
//	void validateBenefitsForClaims(Boolean isValid,String presenterString,Long coverId,List<PABenefitsDTO> paBenefitsValueList,Map referenceDataMap);
//	void loadBenefitsList(List<PABenefitsDTO> paBenefitsValueList);

}
