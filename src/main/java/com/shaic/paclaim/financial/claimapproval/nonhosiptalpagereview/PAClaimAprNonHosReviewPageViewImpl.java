package com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview;

import java.util.ArrayList;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.NursingChargesMatchingDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class PAClaimAprNonHosReviewPageViewImpl extends AbstractMVPView implements PAClaimAprNonHosReviewPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private PAClaimAprNonHosReviewPageUI aprNonHosReviewPageUI;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}
	
	@Override
	public String getCaption() {
		return "Bill Review";
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Component getContent() {
		aprNonHosReviewPageUI.init(bean, wizard);
		aprNonHosReviewPageUI.resetConsolidatedList();		
		fireViewEvent(PAClaimAprNonHosReviewPagePresenter.BILLING_REVIEW_SET_UP_REFERENCE, bean);
		Component comp =  aprNonHosReviewPageUI.getContent();
		return comp;
	}

	
	
	public List<NursingChargesMatchingDTO> getNursingCharges() {
		List<NursingChargesMatchingDTO> list = new ArrayList<NursingChargesMatchingDTO>();
		for(int i=0 ; i < 3; i++) {
			NursingChargesMatchingDTO dto = new NursingChargesMatchingDTO();
			dto.setItemName("Nursing Charges");
			dto.setBillNumber(String.valueOf(i));
			if(i == 0) {
				dto.setClaimedNoOfDays(5d);
				dto.setAllocatedClaimedNoOfDays(0d);
			} else if(i == 1) {
				dto.setClaimedNoOfDays(5d);
				dto.setAllocatedClaimedNoOfDays(0d);
			} else {
				dto.setClaimedNoOfDays(5d);
				dto.setAllocatedClaimedNoOfDays(0d);
			}
			list.add(dto);
		}
		return list;
	}

	@Override
	public boolean onAdvance() {
		aprNonHosReviewPageUI.setTableValuesToDTO();
		if(aprNonHosReviewPageUI.validatePage()){
			return true;
		}
		
			return false;
	}

	@Override
	public boolean onBack() {
		fireViewEvent(PAClaimAprNonHosReviewPagePresenter.GET_MAPPING_DATA, bean);
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
		return "billing-review-";
	}

	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		localize(null);
		this.bean = bean;
		this.wizard = wizard;
		aprNonHosReviewPageUI.init(bean , wizard);
	}
	
	@Override
	public void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne) {
		aprNonHosReviewPageUI.setMappingData(mappingData, isInvokeForOneToOne);
	}

	@Override
	public void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO) {
		// TODO Auto-generated method stub
		aprNonHosReviewPageUI.setBenefitsData(addOnBenefitsDTO);
	}

	@Override
	public void setCompareWithRODResult(String comparisonResult) {
		this.bean.setComparisonResult(comparisonResult);
		if(comparisonResult == null || comparisonResult.isEmpty()) {
			this.bean.setIsComparisonDone(true);
		}
	}
	
	@Override
	public void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer) {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.REFER_TO_COORDINATOR, selectValueContainer);
	}
	
	@Override
	public void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer){
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.PA_CLAIM_APPROVAL_CANCEL_ROD, selectValueContainer);
		
	}

	@Override
	public void generateReferToMedicalApproverLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.MEDICAL_APPROVER, null);
		
	}

	


	@Override
	public void generateReferToBillingLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.REFER_TO_BILLING, null);
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		aprNonHosReviewPageUI.setupReferences(referenceData);
		
	}

	@Override
	public void generateQueryLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.QUERY,null);
		
	}

	@Override
	public void generateApproveLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.FINANCIAL, null);
		
	}
		
	@Override
	public void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.REJECTION, selectValueContainer);
		
	}

	@Override
	public void genertateSentToReplyLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.SENT_TO_REPLY, null);
	}

	@Override
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
		aprNonHosReviewPageUI.setUpIFSCDetails(viewSearchCriteriaDTO);
		
	}
	
	@Override
	public void generatePaymentQueryLayout() {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.PAYMENT_QUERY_TYPE,null);
	}
	
	@Override
	public void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		aprNonHosReviewPageUI.generateButtonFields(SHAConstants.PROCESS_INVESTIGATION_INTIATED, selectValueContainer);
		
	}

	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2,
			BeanItemContainer<SelectValue> selectValueContainer3) {
		Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
		values.put("allocationTo", selectValueContainer);
		values.put("fvrAssignTo", selectValueContainer2);
		values.put("fvrPriority", selectValueContainer3);
		
		aprNonHosReviewPageUI.generateFieldFisitButton(values);
		
	}
	
	@Override
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto){
		aprNonHosReviewPageUI.setUpAddBankIFSCDetails(dto);
	}
	
	@Override
	public void setupPanDetailsMandatory(Boolean panDetails){
		aprNonHosReviewPageUI.setupPanDetailsMandatory(panDetails);
	}
	
	@Override
	public void populateApproveAmntOPT(List<OptionalCoversDTO> optListBilling) {
		aprNonHosReviewPageUI.populateApproveAmntOPT(optListBilling);	
	}
}
