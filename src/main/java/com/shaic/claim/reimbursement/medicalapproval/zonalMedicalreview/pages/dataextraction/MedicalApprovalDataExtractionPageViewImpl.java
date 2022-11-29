package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public class MedicalApprovalDataExtractionPageViewImpl extends AbstractMVPView 
implements MedicalApprovalDataExtractionPageInterface {

	private static final long serialVersionUID = 7319785704709451762L;

	@Inject
	private MedicalApprovalDataExtractionPageUI dataExtractionPage;
	
	/*@Inject
	private TextBundle tb;*/
	
	//private String strCaptionString;
	
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
	public Component getContent() {
		Component comp =  dataExtractionPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_SETUP_REFERNCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		dataExtractionPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		dataExtractionPage.setTableValuesToDTO();
		if(dataExtractionPage.validateProcedure()){
			dataExtractionPage.procedurePopUp();
			return false;
		}
		if(dataExtractionPage.validatePage()){
			
			if(this.bean.getAlertMessageOpened()){
				if(bean.getIsDialysis() && !bean.getDialysisOpened()) {
					dataExtractionPage.alertNoOfSittings();
					return false;
				}
				if(!bean.getIsComparisonDone() && bean.getComparisonResult() != null && ! bean.getComparisonResult().isEmpty()) {
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
            // strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }
	
	/*private String textBundlePrefixString()
	{
		return "medical-approval-";
	}*/

	@Override
	public String getCaption() {
		return "Data Extraction";
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
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, this.bean);
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
	public void genertateFieldsBasedOnDomicillaryHosp(Boolean selectedValue) {
		dataExtractionPage.generatedFieldsBasedOnDomicillaryHosp(selectedValue);

	}

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {

		dataExtractionPage.setCoverList(coverContainer);

	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		dataExtractionPage.setSubCoverList(subCoverContainer);

	}
	
	public void setClearReferenceData(){
		dataExtractionPage.clearReferenceData();
	}
	
	public void setViewRTAButton(Button btnViewRTAButton){
		dataExtractionPage.setViewRTAButton(btnViewRTAButton);
		
	}

	@Override
	public void setAssistedReprodTreatment(Long assistValue) {
		dataExtractionPage.setAssistedValue(assistValue);		
	}

	public void setBillEntryDisable(Boolean isFvrInvInitiated) {
		dataExtractionPage.setBillEntryDisable(isFvrInvInitiated);		
	}
	@Override
	public void setZonalProcedureValues(
			BeanItemContainer<SelectValue> procedures) {
		dataExtractionPage.setProcedureValues(procedures);
	}

}
