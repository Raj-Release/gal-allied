package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class MedicalApprovalPremedicalProcessingViewImpl extends AbstractMVPView 
implements MedicalApprovalPremedicalProcessingPageInterface {
	private static final long serialVersionUID = -6487966219956247208L;

	private PreauthDTO bean;

	@Inject
	private MedicalApprovalPremedicalProcessingUI premedicalProcessingPage;
	
	BeanItemContainer<SelectValue> rejCatContainer;
	BeanItemContainer<SelectValue> setlRejCatContainer;
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCaption() {
		return "Pre-Medical Processing";
	}


	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}

	@Override
	public Component getContent() {
		premedicalProcessingPage.init(bean);
		Component comp =  premedicalProcessingPage.getContent();
		fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE, this.bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		premedicalProcessingPage.setupReferences(referenceData);
	}

	@Override
	public boolean onAdvance() {
		return premedicalProcessingPage.validatePage();
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

	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpReference(Map<String, Object> referenceDataMap) {
		
		rejCatContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("rejCateg");
		setlRejCatContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("setlRejCateg");
		
		premedicalProcessingPage.setupReferences(referenceDataMap);
		
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionDetails) {
		premedicalProcessingPage.setExclusionDetails(exclusionDetails);
		
	}

	@Override
	public void generateFieldsOnQueryClick() {
			premedicalProcessingPage.generateButtonFields(SHAConstants.QUERY,null);
	}

	@Override
	public void generateFieldsOnSuggestRejectionClick() {
		
		if(bean.getHospAmountAlreadyPaid() != null && bean.getHospAmountAlreadyPaid().intValue() != 0){
			premedicalProcessingPage.generateButtonFields(SHAConstants.REJECT,setlRejCatContainer);
		}
		else{
			premedicalProcessingPage.generateButtonFields(SHAConstants.REJECT,rejCatContainer);
		}
		
	}

	@Override
	public void generateFieldsOnSuggestApprovalClick() {
		
		premedicalProcessingPage.generateButtonFields(SHAConstants.APPROVAL,null);
		
	}
	
	@Override
	public void generateFieldsForRefToBillEntryClick(){
		
		premedicalProcessingPage.generateButtonFields(SHAConstants.REFER_TO_BILL_ENTRY,null);
		
	}
	
	@Override
	public void generateFieldsOnCancelRODClick() {
		
		premedicalProcessingPage.generateButtonFields(SHAConstants.CANCEL_ROD,null);
		
	}

	@Override
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		premedicalProcessingPage.setMedicalDecisionTableValues(dto, medicalDecisionTableValues);
		
	}

	@Override
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		premedicalProcessingPage.setCategoryValue(selectValueContainer);
		
	}

	@Override
	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		premedicalProcessingPage.setBillEntryFinalStatus(uploadDTO);
		
	}

	@Override
	public void setBillEntryAmountConsiderValue(Double sumValue) {
		premedicalProcessingPage.setBillEntryAmountConsideredValue(sumValue);
	}


	@Override
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		premedicalProcessingPage.setUploadDTOForBillEntry(uploadDTO);
		
	}

	/*@Override
	public void generateFieldsOnInvtClick() {
		premedicalProcessingPage.generateButtonFields(SHAConstants.INITIATE_INVESTIGATION,null);
		
	}

	@Override
	public void alertMessageForInvestigation() {
		premedicalProcessingPage.alertMessageForInvestigation();
	}*/

	public void setClearReferenceData(){
		premedicalProcessingPage.clearReferenceData();
	}

	@Override
	public void generateFieldsOnInitiateFvrClick() {
		premedicalProcessingPage.generateButtonFields(SHAConstants.INITIATE_FVR,null);		
	}

	public void setEnableOrDisableButtons(){
		
		if(premedicalProcessingPage != null){
			premedicalProcessingPage.setEnableOrDisableButtons();
		}
	}
}
