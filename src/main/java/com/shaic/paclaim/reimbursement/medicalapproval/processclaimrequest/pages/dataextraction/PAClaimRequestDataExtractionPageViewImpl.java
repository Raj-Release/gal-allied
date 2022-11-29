/**
 * 
 */
package com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

/**
 * @author ntv.vijayar
 *
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Investigation;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.TextField;

public class PAClaimRequestDataExtractionPageViewImpl extends AbstractMVPView 
implements PAClaimRequestDataExtractionPageInterface {
	private static final long serialVersionUID = 6478972542812705077L;

	@Inject
	private PAClaimRequestDataExtractionPageUI dataExtractionPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}
	
	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	@Override
	public Component getContent() {
		Component comp =  dataExtractionPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(PAClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_SETUP_REFERNCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		dataExtractionPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		dataExtractionPage.setTableValuesToDTO();
		
		if(! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS) && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)
				&& ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)){
		
        if(dataExtractionPage.validatePage()){
			
			if(this.bean.getAlertMessageOpened()){
				if(bean.getIsDialysis() && !bean.getDialysisOpened()) {
					dataExtractionPage.alertNoOfSittings();
					return false;
				}
				if(!bean.getIsComparisonDone()) {
					dataExtractionPage.showPopupForComparison(bean.getComparisonResult());
					return false;
				}
				return true;
			}
			dataExtractionPage.alertMessage();
			return false;
			
		}else{
			return false;
		}
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }
	
	private String textBundlePrefixString()
	{
		return "medical-approval-";
	}
	
	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		localize(null);
		this.bean = bean;
		this.wizard = wizard;
		dataExtractionPage.init(bean , wizard);
	}

	@Override
	public void editSpecifyVisibility(Boolean checkValue) {
		dataExtractionPage.editSpecifyVisible(checkValue);
		
	}

	@Override
	public void generateFieldsBasedOnTreatment() {
		dataExtractionPage.generatedFieldsBasedOnTreatment();
	}

	@Override
	public void genertateFieldsBasedOnPatientStaus() {
		dataExtractionPage.generateFieldsBasedOnPatientStatus();
	}

	@Override
	public void genertateFieldsBasedOnHospitalisionDueTo(SelectValue value) {
		dataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo(value);
	}

	@Override
	public void generateFieldsBasedOnOtherCoveredClaim(Boolean selectedValue) {
		dataExtractionPage.generatedFieldsBasedOnOtherClaims(selectedValue);
	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		dataExtractionPage.setIcdBlock(icdBlockContainer);
	}

	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		dataExtractionPage.setIcdCode(icdCodeContainer);
		
	}

	@Override
	public void setPackageRate(Map<String, String> mappedValues) {
		dataExtractionPage.setPackageRateForProcedure(mappedValues);
		
	}

	@Override
	public void intiateCoordinatorRequest() {
		if(dataExtractionPage.validatePage())
		{
			dataExtractionPage.setTableValuesToDTO();
			fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, this.bean);
		}
	}

	@Override
	public void generateFieldsBasedOnReportedPolice(Boolean selectedValue) {
		dataExtractionPage.generatedFieldsBasedOnReportedToPolice(selectedValue);
		
	}

	@Override
	public void setCompareWithRODResult(String comparisonResult) {
		this.bean.setComparisonResult(comparisonResult);
		if(comparisonResult == null || comparisonResult.isEmpty()) {
			this.bean.setIsComparisonDone(true);
		}
		
	}

	@Override
	public void genertateFieldsBasedOnDomicillaryFields(Boolean selectedValue) {
		dataExtractionPage.generatedFieldsBasedOnDomicillaryHosp(selectedValue);
	}

	@Override
	public void generateApproveLayout() {
		dataExtractionPage.generateButton(10, null);
		
	}

	@Override
	public void generateCancelRodLayout() {
		dataExtractionPage.generateButton(11, null);
		
	}

	@Override
	public void generateQueryLayout() {
		dataExtractionPage.generateButton(8, null);
		
	}

	@Override
	public void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.generateButton(9, selectValueContainer);
		
	}

	@Override
	public void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.generateButton(6, selectValueContainer);
		
	}
	

	@Override
	public void generateEscalateReplyLayout() {
		dataExtractionPage.generateButton(5, null);
		
	}

	/*@Override
	public void generateFieldsBasedOnSentTOCPU(Boolean isChecked) {
		dataExtractionPage.generateFieldBasedOnSentToCPU(isChecked);
		
	}*/

	@Override
	public void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.generateButton(4, selectValueContainer);
		
	}

	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority) {
		Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
		values.put("allocationTo", selectValueContainer);
		values.put("fvrAssignTo", fvrAssignTo);
		values.put("fvrPriority", fvrPriority);
		
		dataExtractionPage.generateButton(2, values);
		
	}
	
	@Override
	public void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.generateButton(3, selectValueContainer);
		
	}

	@Override
	public void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist) {
		dataExtractionPage.generateButton(7, selectValueContainerForSpecialist);
		
	}

	@Override
	public void genertateSentToReplyLayout() {
		dataExtractionPage.generateButton(1, null);
	}

	@Override
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		dataExtractionPage.setMedicalDecisionValues(dto, medicalDecisionTableValues);
	}
	
	/*@Override
	public void setInvestigationRule(Investigation checkInitiateInvestigation) {
		dataExtractionPage
		.setInvestigationCheck(checkInitiateInvestigation != null ? true
				: false);
		
	}*/

	@Override
	public void setPABenefitsTableValues(List<PABenefitsDTO> paBenefitsList,Long benefitsId,Map referenceDataMap,List<PABenefitsDTO> benefitsDBList) {
		dataExtractionPage.setPABenefitsValues(paBenefitsList,benefitsId,referenceDataMap,benefitsDBList);
		
	}
	
	@Override
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		dataExtractionPage.setUploadDTOForBillEntry(uploadDTO);
		
	}

	@Override
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.setCategoryValues(selectValueContainer);
		
	}

	/*@Override
	public void validateBenefitsForClaims(Boolean value,String presenterString,Long benefitsId,TextField eligibleFld , TextField amtFld) {
		dataExtractionPage.validateBenefitsForClaim(value,presenterString,benefitsId,eligibleFld,amtFld);
		
	}*/

	@Override
	public void validateCoversAndBenefitsForClaims(Boolean value,String presenterString,Long benefitsId,TextField eligibleFld , TextField amtFld,ComboBox cmb) {
		dataExtractionPage.validateCoversAndBenefitsForClaims(value,presenterString,benefitsId,eligibleFld,amtFld,cmb);
		
	}

	/*@Override
	public void loadPABenefitsValues(List<PABenefitsDTO> paBenefitsList) {
		
		dataExtractionPage.loadPABenefitsValues(paBenefitsList);
	}
*/
	@Override
	public void loadPABenefitsValues(List<PABenefitsDTO> paBenefitsList,
			GComboBox cmb) {
		dataExtractionPage.loadPABenefitsValues(paBenefitsList,cmb);
		
	}
	
	public void setClearReferenceData(){
    	dataExtractionPage.setClearReferenceData();
    }

	/*@Override
	public void validateBenefitsForClaims(Boolean isValid,
			String presenterString, Long benefitsId,
			List<PABenefitsDTO> paBenefitsValueList, Map referenceDataMap) {
		dataExtractionPage.validateBenefitsForClaim(isValid,presenterString,benefitsId, paBenefitsValueList , referenceDataMap );
		
	}*/

}
