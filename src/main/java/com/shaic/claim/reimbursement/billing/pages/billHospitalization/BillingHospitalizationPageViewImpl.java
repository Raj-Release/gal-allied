package com.shaic.claim.reimbursement.billing.pages.billHospitalization;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;

public class BillingHospitalizationPageViewImpl extends AbstractMVPView implements BillingHospitalizationPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private BillingHospitalizationPageUI billingHospitalizationPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private PreauthDTO bean;
	
	private GWizard wizard;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCaption() {
		return "Bill Hospitalization";
	}


	

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}



	@Override
	public Component getContent() {
		billingHospitalizationPage.init(bean, wizard);
		Component comp =  billingHospitalizationPage.getContent();
//		fireViewEvent(BillingReviewPagePresenter.BILLING_REVIEW_SET_UP_REFERENCE, bean);
		return comp;
	}



	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		billingHospitalizationPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		
		if(!this.bean.getAlertMessageOpened() && this.bean.getUpdatePaymentDtlsFlag()!=null && this.bean.getUpdatePaymentDtlsFlag().equalsIgnoreCase("H")){
			billingHospitalizationPage.alertMessageForHoldPayment("This intimation is kept on hold by the user at update payment screen");
		}else if(billingHospitalizationPage.validatePage()) {
			if((bean.getPreHospitalizaionFlag() && !bean.getIsPreHospApplicable() || bean.getPostHospitalizaionFlag() && !bean.getIsPostHospApplicable()) && !bean.getAlertMessageOpened()) {
				billingHospitalizationPage.alertMessage();
				return false;
			}
			
			if(bean.getIsHospitalizationRejected() && !bean.getRejectionAlertMessageOpened()
					//Mail - Unable to refer Pre, Post claim from Billing below rejection alert bypassed while refer to bill entry
					&& bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				billingHospitalizationPage.alertMessageForRejection();
				return false;
			}
			else if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && !bean.getAlertMessageOpened()){
				if(bean.getIsPreHospApplicable()&& bean.getPreHospitalizaionFlag() && ((ReferenceTable.ROAD_TRAFFIC_ACCIDENT).equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()) 
					&& bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() !=null && (ReferenceTable.INJURY_MASTER_ID).equals(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))){
					
					billingHospitalizationPage.alertMessage();
					return false;
				}
			}
			return true;
		}
		return false;
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
	public void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer) {
		billingHospitalizationPage.generateButtonFields(SHAConstants.REFER_TO_COORDINATOR, selectValueContainer);
	}
	
	@Override
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
		// TODO Auto-generated method stub
		billingHospitalizationPage.setUpIFSCDetails(viewSearchCriteriaDTO);

	}

	@Override
	public void generateReferToMedicalApproverLayout() {
		billingHospitalizationPage.generateButtonFields(SHAConstants.MEDICAL_APPROVER, null);
	}

	@Override
	public void generateSendToFinancialLayout() {
		billingHospitalizationPage.generateButtonFields(SHAConstants.FINANCIAL, null);
	}
	@Override
	public void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer){
		billingHospitalizationPage.generateButtonFields(SHAConstants.BILLING_CANCEL_ROD, selectValueContainer);
		
	}

	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		billingHospitalizationPage.init(bean, wizard);
		
	}

	@Override
	public void generateReferToBillEntryLayout() {
		billingHospitalizationPage.generateButtonFields(SHAConstants.REFER_TO_BILL_ENTRY, null);
		
	}

	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO) {
		billingHospitalizationPage.populatePreviousPaymentDetails(tableDTO);
		
	}
	
	
	public void setClearReferenceData(){
		billingHospitalizationPage.setClearReferenceData();
	}
	
	public void generateLayoutBasedOnApprovedAmount(){
		billingHospitalizationPage.generateLayoutBasedOnApprovedAmt();
	}

	@Override
	public void setUpPayableDetails(String payableAt) {
		billingHospitalizationPage.setUpPayableDetails(payableAt);
	}
	
	public void alertMsgForCancelRod(){
		
		billingHospitalizationPage.alertMsgForCancelRod();
	}
	
	@Override
	public void setBalanceSIforRechargedProcess(Double balanceSI) {
		billingHospitalizationPage.setBalanceSIforRechargedProcessing(balanceSI);
		
	}
	
	@Override
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto){
		billingHospitalizationPage.setUpAddBankIFSCDetails(dto);
	}

	public Long getHospitalizationApprovedamt(){
		if(billingHospitalizationPage !=null){
			return billingHospitalizationPage.getHospitalizationApprovedamt();
		}
		return 0L;
	}

	public Long getHospitalizationCashlessApprovedamt(){
		if(billingHospitalizationPage !=null){
			return billingHospitalizationPage.getHospitalizationCashlessApprovedamt();
		}
		return 0L;
	}
	
	public void setupPanDetailsMandatory(Boolean panDetails){
		if(billingHospitalizationPage !=null){
			billingHospitalizationPage.setupPanDetailsMandatory(panDetails);
		}
	}

	@Override
	public void generateHoldLayout() {
		billingHospitalizationPage.generateButtonFields(SHAConstants.HOLD_REMARKS, null);
	}
}
