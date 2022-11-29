package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public class ClaimRequestDataExtractionPageViewImpl extends AbstractMVPView 
implements ClaimRequestDataExtractionPageInterface {
	private static final long serialVersionUID = 6478972542812705077L;

	@Inject
	private ClaimRequestDataExtractionPageUI dataExtractionPage;
	
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
	public String getCaption() {
		return "Data Extraction";
	}

	@Override
	public Component getContent() {
		dataExtractionPage.init(bean, wizard);
		Component comp =  dataExtractionPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_SETUP_REFERNCE, bean);
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
			
        	//JIRA -  GALAXYMAIN-13313
        	if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)){
        		this.bean.setAlertMessageOpened(true);
        	}

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

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	/*protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
<<<<<<< HEAD
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }*/

	
	/*private String textBundlePrefixString()
	{
		return "medical-approval-";
	}*/
	
	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		//localize(null);
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
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		
		dataExtractionPage.setCoverList(coverContainer);
		
	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		
		dataExtractionPage.setSubCoverList(subCoverContainer);
		
	}
	
	public void setClearReferenceData(){
    	dataExtractionPage.setClearReferenceData();
    }
	
	@Override
	public void checkPatientStatusForMAQuery(){
		dataExtractionPage.validatePatientStatusForMAQuery();
	}

	public void setViewRTAButton(Button btnViewRTABalanceSI) {
       dataExtractionPage.setViewRTAButton(btnViewRTABalanceSI);		
	}

	@Override
	public void setAssistedReprodTreatment(Long assistValue) {
		dataExtractionPage.setAssistedValue(assistValue);
	}
	@Override
	public void showEditBillClassification(Map<String, Object> referenceData){
		dataExtractionPage.showEditBillClassification(referenceData);
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
	
	@Override
	public void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		dataExtractionPage.generateButton(4, selectValueContainer);
		
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
	
	public void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue) {
		dataExtractionPage.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}

	@Override
	public void setProcessClaimProcedureValues(
			BeanItemContainer<SelectValue> procedures) {
		dataExtractionPage.setProcedureValues(procedures);
		
	}
	
	@Override
	 public void setTreatingQualificationValues(BeanItemContainer<SelectValue> qualification) {
		 dataExtractionPage.setTreatingqualification(qualification);		
	}
	
	 public void generateFieldsBasedOnImplantApplicable(Boolean isChecked) {
		 dataExtractionPage.generateFieldsBasedOnImplantApplicable(isChecked);
	 }

	@Override
	public void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO) {
		// TODO Auto-generated method stub
		dataExtractionPage.setBenefitsData(addOnBenefitsDTO);
	}
	 
	 public void generateadmissiontypeFields(Boolean displayadmissiontype) {
		 dataExtractionPage.generateadmissiontypeFields(displayadmissiontype);// TODO Auto-generated method stub

	 }

	 public void genertateFieldsBasedOnTypeOfAdmisstion() {
		 dataExtractionPage.genertateFieldsBasedOnTypeOfAdmisstion();

	 }
}
