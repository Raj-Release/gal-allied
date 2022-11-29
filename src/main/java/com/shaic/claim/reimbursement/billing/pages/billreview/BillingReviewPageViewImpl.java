package com.shaic.claim.reimbursement.billing.pages.billreview;

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
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.NursingChargesMatchingDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class BillingReviewPageViewImpl extends AbstractMVPView implements BillingReviewPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private BillingReviewPageUI billingReviewPage;
	
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
		Component comp =  billingReviewPage.getContent();
		fireViewEvent(BillingReviewPagePresenter.BILLING_REVIEW_SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		billingReviewPage.setupReferences(referenceData);
		
	}
	
	public List<NursingChargesMatchingDTO> getNursingCharges() {
		List<NursingChargesMatchingDTO> list = new ArrayList<NursingChargesMatchingDTO>();
		NursingChargesMatchingDTO dto = null;
		for(int i=0 ; i < 3; i++) {
			dto = new NursingChargesMatchingDTO();
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
			if(bean.getIsFirstPageSubmit()){
				return true;
			}
			fireViewEvent(BillingReviewPagePresenter.BILLING_DETAILS_UPDATE, bean);
			fireViewEvent(BillingReviewPagePresenter.GET_MAPPING_DATA, bean.getKey(), true);
			
			if(billingReviewPage.isMappingDone) {
//				if((bean.getIsOneMapping() || bean.getIsICUoneMapping()) && !billingReviewPage.isMatchTheFollowing) {
//					
//				}
				fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING_MAPPING, bean);
				if(this.bean.getAlertMessageOpened()){
					this.bean.setAlertMessageOpened(false);
					if(!bean.getIsComparisonDone() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat())) {
						billingReviewPage.showPopupForComparison(bean.getComparisonResult());
						return false;
					}
					return true;
				}
				billingReviewPage.alertMessage();
				return false;
			}
			fireViewEvent(BillingReviewPagePresenter.GET_MAPPING_DATA, bean.getKey(), false);
			billingReviewPage.showRoomRentMatchingScreen();
			return false;
		}
		
			return false;
	}

	@Override
	public boolean onBack() {
		fireViewEvent(BillingReviewPagePresenter.GET_MAPPING_DATA, bean);
		return true;
	}
	
	@Override
	public void editSpecifyVisibility(Boolean checkValue) {
		billingReviewPage.editSpecifyVisible(checkValue);
		
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
	public void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue) {
		billingReviewPage.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}
	
	
	@Override
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue) {
		billingReviewPage.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}

	@Override
	public void generateFieldsBasedOnTreatment() {
		
	}

	@Override
	public void intiateCoordinatorRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genertateFieldsBasedOnPatientStaus() {
		billingReviewPage.generateFieldsBasedOnPatientStatus();
		
	}

	@Override
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		billingReviewPage.setCategoryValues(selectValueContainer);
		
	}

	@Override
	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		billingReviewPage.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	@Override
	public void setUpIrdaLevel2Values(
			BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue value) {
		billingReviewPage.setIrdaLevel2Values(selectValueContainer,cmb,value);
		
	}
	
	@Override
	public void setUpIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		billingReviewPage.setIrdaLevel3Values(selectValueContainer,cmb,value);
		
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
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		billingReviewPage.setUploadDTOForBillEntry(uploadDTO);
		
	}


	@Override
	public void generateReferToBillEntryLayout() {
		billingReviewPage.generateButtonFields(SHAConstants.REFER_TO_BILL_ENTRY, null);
	}
		

	@Override
	public void updateProductBasedAmtDetails(Map<Integer, Object> detailsMap) {
		billingReviewPage.updateProductBasedAmtDetails(detailsMap);
	}
	
	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		billingReviewPage.setCoverList(coverContainer);
	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		billingReviewPage.setSubCoverList(subCoverContainer);
	}
	
	public void setClearReferenceData(){
		billingReviewPage.setClearReferenceData();
	}

	@Override
	public void alertMsgForCancelRod() {
		billingReviewPage.alertMsgForCancelRod();
		
	}
	
	@Override
	public void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		billingReviewPage.setUpNomineeIFSCDetails(dto);
		
	}

	public Double getTotalClaimedAmtForRRC(){
		Double claimedAmount =billingReviewPage.getTotalClaimedAmtForRRC();
		return claimedAmount;
	}
}
