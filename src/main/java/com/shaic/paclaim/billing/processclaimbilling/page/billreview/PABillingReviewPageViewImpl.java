package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.util.ArrayList;
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
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.TextField;

public class PABillingReviewPageViewImpl extends AbstractMVPView implements PABillingReviewPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private PABillingReviewPageUI billingReviewPage;
	
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
		billingReviewPage.init(bean, wizard);
		billingReviewPage.resetConsolidatedList();
		Component comp =  billingReviewPage.getContent();
		fireViewEvent(PABillingReviewPagePresenter.BILLING_REVIEW_SET_UP_REFERENCE, bean);
//		
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
		billingReviewPage.setTableValuesToDTO();
		if(billingReviewPage.validatePage()){
			return true;
		}
		
			return false;
	}

	@Override
	public boolean onBack() {
		fireViewEvent(PABillingReviewPagePresenter.GET_MAPPING_DATA, bean);
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
		billingReviewPage.init(bean , wizard);
	}
	
	@Override
	public void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne) {
		billingReviewPage.setMappingData(mappingData, isInvokeForOneToOne);
	}

	@Override
	public void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO) {
		// TODO Auto-generated method stub
		billingReviewPage.setBenefitsData(addOnBenefitsDTO);
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
		billingReviewPage.generateButtonFields(SHAConstants.REFER_TO_COORDINATOR, selectValueContainer);
	}
	
	@Override
	public void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer){
		billingReviewPage.generateButtonFields(SHAConstants.BILLING_CANCEL_ROD, selectValueContainer);
		
	}

	@Override
	public void generateReferToMedicalApproverLayout() {
		billingReviewPage.generateButtonFields(SHAConstants.MEDICAL_APPROVER, null);
		
	}

	


	@Override
	public void generateReferToBillEntryLayout() {
		billingReviewPage.generateButtonFields(SHAConstants.REFER_TO_BILL_ENTRY, null);
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		billingReviewPage.setupReferences(referenceData);
		
	}

	@Override
	public void generateSendToClaimApprovalLayout() {
		billingReviewPage.generateButtonFields(SHAConstants.PA_CLAIM_APPROVAL, null);		
	}

	@Override
	public void generateAddOnCoverValue(
			AddOnCoversTableDTO setAddonDtoforCoverId,ComboBox cmb) {
		billingReviewPage.addToCoverList(setAddonDtoforCoverId,cmb);
		
	}

	@Override
	public void generateSendToFALayout() {
		// TODO Auto-generated method stub
		billingReviewPage.generateButtonFields(SHAConstants.PA_BILLING_SEND_TO_FA, null);
	}

	public void validateOptionalCovers(Long coverId,
			TextField eligiblityFld) {
//		this.billingReviewPage.validateOptionalCovers(coverId, eligiblityFld);
		
	}

	/*@Override
	public void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		billingReviewPage.setUpNomineeIFSCDetails(dto);
		
	}*/
	
	@Override
	public void populateApproveAmntOPT(List<OptionalCoversDTO> optListBilling) {
		billingReviewPage.populateApproveAmntOPT(optListBilling);	
	}
	
}
