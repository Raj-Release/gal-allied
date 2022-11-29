package com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class PAHealthFinancialHospitalizationPageViewImpl extends AbstractMVPView implements PAHealthFinancialHospitalizationPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private PAHealthFinancialHospitalizationPageUI billingHospitalizationPage;
	
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
	public void init(PreauthDTO bean, GWizard wizard) {
		
		this.bean = bean;
		this.wizard = wizard;
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
		if(!this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
			
		}
		if(!this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)){
			
			return billingHospitalizationPage.validatePage();
		}
		else{
			
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

	@Override
	public void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer) {
		billingHospitalizationPage.generateButton(4, selectValueContainer);
	}
	
	@Override
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
		// TODO Auto-generated method stub
		billingHospitalizationPage.setUpIFSCDetails(viewSearchCriteriaDTO);

	}

	@Override
	public void generateReferToMedicalApproverLayout() {
		billingHospitalizationPage.generateButton(5, null);
	}

	@Override
	public void generateSendToFinancialLayout() {
	}

	@Override
	public void generateApproveLayout() {
		billingHospitalizationPage.generateButton(9, null);
		
	}

	@Override
	public void generateQueryLayout() {
		billingHospitalizationPage.generateButton(7, null);
		
	}
	@Override 
	public void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer){
		billingHospitalizationPage.generateButton(10, selectValueContainer);
	}
	@Override
	public void generateRejectLayout() {
		billingHospitalizationPage.generateButton(8, null);
		
	}

	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority) {
		Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
		values.put("allocationTo", selectValueContainer);
		values.put("fvrAssignTo", fvrAssignTo);
		values.put("fvrPriority", fvrPriority);
		
		billingHospitalizationPage.generateButton(2, values);
		
	}

	@Override
	public void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		billingHospitalizationPage.generateButton(3, selectValueContainer);
		
	}

	@Override
	public void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist) {
		billingHospitalizationPage.generateButton(1, selectValueContainerForSpecialist);
		
	}

	@Override
	public void genertateReferToBillingLayout() {
		billingHospitalizationPage.generateButton(6, null);
		
	}

	@Override
	public void generateReferToBillEntryLayout() {
		
		billingHospitalizationPage.generateButton(11, null);
		
	}
	
	@Override
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		billingHospitalizationPage.populatePreviousPaymentDetails(tableDTO);
		
	}

	@Override
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto){
		billingHospitalizationPage.setUpAddBankIFSCDetails(dto);
	}
	
	@Override
	public void setupPanDetailsMandatory(Boolean panDetails){
		billingHospitalizationPage.setupPanDetailsMandatory(panDetails);
	}

}
