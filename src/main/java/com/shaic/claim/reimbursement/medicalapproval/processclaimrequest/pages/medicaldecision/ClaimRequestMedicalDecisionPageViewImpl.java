package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Investigation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class ClaimRequestMedicalDecisionPageViewImpl extends AbstractMVPView 
implements ClaimRequestMedicalDecisionPageInterface {
	private static final long serialVersionUID = 1362142625372930037L;

	@Inject
	private ClaimRequestMedicalDecisionPageUI medicalDecisionPage;
	
	/*@Inject
	private TextBundle tb;*/
	
	//private String strCaptionString;
	
	private PreauthDTO bean;
	
	//private GWizard wizard;
	
	@Override
	public void resetView() {
//		init(bean, this.wizard);
	}

	@Override
	public String getCaption() {
		return "Medical Decision";
	}
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		medicalDecisionPage.init(bean);
		Component comp =  medicalDecisionPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.MEDICAL_APPROVAL_SETUP_REFERNCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		medicalDecisionPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
//		medicalDecisionPage.setTableValuesToDTO();
		fireViewEvent(
				ClaimRequestMedicalDecisionPagePresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());
		if(null != this.bean.getStatusKey() && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS) && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)
				&& ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)){
			return medicalDecisionPage.validatePage();
		}
		else{
			
			this.bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks("");
			//return medicalDecisionPage.validate();			
			return ((null != this.bean.getStatusKey() && this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS) && this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)
					&& this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) ?medicalDecisionPage.validateSpecialistAndEsclate() : true);	
		}
	}

	@Override
	public boolean onBack() {
		this.bean.getPreauthMedicalDecisionDetails().setFvrGradingDTO(new ArrayList<FvrGradingDetailsDTO>());
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	/*protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
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
		//this.wizard = wizard;
		medicalDecisionPage.init(bean , wizard);
	}

	@Override
	public void viewBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			medicalDecisionPage.showBalanceSumInsured(intimationId);
		}
	}
	
	@Override
	public void viewClaimAmountDetails() {
		medicalDecisionPage.showClaimAmountDetails();
	}
	
	@Override
	public void setPreAuthRequestedAmt(String strPreAuthAmt) {
		medicalDecisionPage.setPreAuthRequestAmt(strPreAuthAmt);
		
	}

	@Override
	public void generateApproveLayout() {
		
		medicalDecisionPage.generateButton(10, null);
		
	}
	
	@Override
	public void generateCancelRodLayout() {
		medicalDecisionPage.generateButton(11, null);
		
	}

	@Override
	public void generateQueryLayout() {
		medicalDecisionPage.generateButton(8, null);
		
	}

	@Override
	public void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		medicalDecisionPage.generateButton(9, selectValueContainer);
		
	}
	
	@Override
	public void genertateFieldsBasedOnRefToBillEntry(){
		medicalDecisionPage.generateButton(12, null);
	}

	@Override
	public void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		medicalDecisionPage.generateButton(6, selectValueContainer);
		
	}

	@Override
	public void generateEscalateReplyLayout() {
		medicalDecisionPage.generateButton(5, null);
		
	}

	@Override
	public void generateFieldsBasedOnSentTOCPU(Boolean isChecked) {
		medicalDecisionPage.generateFieldBasedOnSentToCPU(isChecked);
		
	}

	@Override
	public void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		medicalDecisionPage.generateButton(4, selectValueContainer);
		
	}

/*	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,) {*/
			
	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isAssigned,String repName, String repContactNo) {
		
		if(isAssigned){
		
		Map<String, Object> values = new HashMap<String,Object>();
		values.put("allocationTo", selectValueContainer);
		values.put("fvrAssignTo", fvrAssignTo);
		values.put("fvrPriority", fvrPriority);
		
		values.put("isFvrAssigned", isAssigned);
		
		values.put("repName", repName);
		
		values.put("repContactNo", repContactNo);
		
		medicalDecisionPage.generateButton(2, values);
		}
		else{
			Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
			
			values.put("allocationTo", selectValueContainer);
			values.put("fvrAssignTo", fvrAssignTo);
			values.put("fvrPriority", fvrPriority);
			
			medicalDecisionPage.generateButton(2, values);
		}
		
		
	}

	@Override
	public void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		medicalDecisionPage.generateButton(3, selectValueContainer);
		
	}

	@Override
	public void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist) {
		medicalDecisionPage.generateButton(7, selectValueContainerForSpecialist);
		
	}

	@Override
	public void genertateSentToReplyLayout() {
		medicalDecisionPage.generateButton(1, null);
	}

	@Override
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		medicalDecisionPage.setMedicalDecisionValues(dto, medicalDecisionTableValues);
	}

	@Override
	public void setDiagnosisSumInsuredValuesFromDB(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		medicalDecisionPage.setMedicalDecisionValuesToTable(dto, medicalDecisionTableValues);
		
	}

	@Override
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		medicalDecisionPage.setCategoryValues(selectValueContainer);
		
	}

	@Override
	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		medicalDecisionPage.setBillEntryFinalStatus(uploadDTO);
		
	}

	@Override
	public void setBillEntryAmountConsiderValue(Double sumValue) {
		medicalDecisionPage.setBillEntryAmountConsideredValue(sumValue);
		
	}

//	@Override
//	public void setInvestigationRule(Investigation checkInitiateInvestigation) {
//		medicalDecisionPage
//		.setInvestigationCheck(checkInitiateInvestigation != null ? true
//				: false);
//  }

//	@Override
//	public void setInvestigationRule(Investigation checkInitiateInvestigation) {
//	public void setInvestigationRule(Boolean isPending){
//		medicalDecisionPage
//		.setInvestigationCheck(checkInitiateInvestigation != null ? true
//				: false);
	@Override
	public void setInvestigationRule(Boolean isPending){
		medicalDecisionPage.setInvestigationCheck(isPending);
	}
	
	@Override
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		medicalDecisionPage.setUploadDTOForBillEntry(uploadDTO);
		
	}
	
	public void setClearReferenceData(){
		medicalDecisionPage.setClearReferenceData();
    }

	public void confirmRejectionLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject){
		medicalDecisionPage.confirmRejectionLayout(rejectionCategoryDropdownValues, isDefinedLimitReject);
	}

	public void generateFvrLayout(Object dropDownValues){
		medicalDecisionPage.generateFvrLayout(dropDownValues);
    }

	public void generateButton(){
		medicalDecisionPage.generateButton(8,null);
    }
	
	@Override
	public void setBalanceSIforRechargedProcess(Double balanceSI) {
		medicalDecisionPage.setBalanceSIforRechargedProcessing(balanceSI);
	}

	@Override
	public void generateTopUpLayout(List<PreauthDTO> topupData,String screenName) {
		if(!topupData.isEmpty()){
			medicalDecisionPage.generateTopUpLayout(topupData,screenName);	
		}
		else{
			medicalDecisionPage.showInfoMessageBox("Top-up policy is not available for this claim period");
		}
	}

	@Override
	public void generateTopUpIntimationLayout(String topUpIntimaiton) {
		String topUpIntmMsg = "Intimation "+topUpIntimaiton+ " is created from base policy intimation: "+bean.getNewIntimationDTO().getIntimationId();
		medicalDecisionPage.showInfoMessageBox(topUpIntmMsg);		
			
	}
	
	@Override
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		medicalDecisionPage.setSubCategContainer(rejSubcategContainer);
	}
	
	public Long getTotalApprovedAmnt(){
		return medicalDecisionPage.getTotalApprovedAmnt();
	}
	
	@Override
	 public void setBalanceSumInsuredAlert(){
		 medicalDecisionPage.setBalanceSumInsuredAlert();
	 }
	
	@Override
	public void generateHoldLayout() {
		medicalDecisionPage.generateButton(13, null);
	}
}