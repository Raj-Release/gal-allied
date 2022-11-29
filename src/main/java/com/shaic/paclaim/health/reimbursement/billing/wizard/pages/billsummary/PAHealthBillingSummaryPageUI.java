
package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billsummary;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAHealthBillingSummaryPageUI extends ViewComponent {

	private static final long serialVersionUID = -924498899058179987L;
	

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	private TextField txtPreauthApprovedAmount;
	
	
	@Override
	public String getCaption() {
		return "View Bill Summary";
	}

	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	


	public Component getContent() {
		
		
        txtPreauthApprovedAmount = new TextField("Preauth Approved Amount");
		
		if(this.bean.getPreauthDataExtractionDetails().getPreauthTotalApprAmt() != null){
	    txtPreauthApprovedAmount.setValue(this.bean.getPreauthDataExtractionDetails().getPreauthTotalApprAmt().toString());
		}
		txtPreauthApprovedAmount.setNullRepresentation("");
		txtPreauthApprovedAmount.setEnabled(false);


		viewBillSummary.init(bean,bean.getKey(), false,SHAConstants.BILLING);
		//viewBillSummary.initPresenterString(SHAConstants.BILLING);



		
		VerticalLayout mainVertical = new VerticalLayout(txtPreauthApprovedAmount,viewBillSummary);
		mainVertical.setComponentAlignment(txtPreauthApprovedAmount, Alignment.TOP_LEFT);
		
		return mainVertical;
	}
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		
	}
	
	public void setPayableAmounts() {
		
		if(viewBillSummary != null){
			Map<String, String> payableAmount = viewBillSummary.getPayableAmount();
			this.bean.setHospitalisationValue(payableAmount.get(SHAConstants.HOSPITALIZATION));
			this.bean.setPreHospitalisationValue(payableAmount.get(SHAConstants.PREHOSPITALIZATION));
			this.bean.setPostHospitalisationValue(payableAmount.get(SHAConstants.POSTHOSPITALIZATION));
			this.bean.setProrataPercentage(SHAUtils.getDoubleValueFromString(payableAmount.get(SHAConstants.PRORATA_PERCENTAGE)) );
			this.bean.setProrataDeductionFlag(payableAmount.get(SHAConstants.PRORATA_DEDUCTION_FLAG));
			this.bean.setPackageAvailableFlag(payableAmount.get(SHAConstants.PACKAGE_AVAILABLE_FLAG));
			this.bean.setRodTotalClaimedAmount(SHAUtils.getClaimedAmount(payableAmount));
			
		}
	}
	
	public void setTableValuesToDTO()
	{
		if(null != viewBillSummary)
		{
			if((null != bean.getHospitalizaionFlag() && bean.getHospitalizaionFlag()) || (null != bean.getIsHospitalizationRepeat() && bean.getIsHospitalizationRepeat()))
			{
				List<BillEntryDetailsDTO> hospitalizationTabList = viewBillSummary.getHospitalizationTabValues();
				if(null != hospitalizationTabList && !hospitalizationTabList.isEmpty())
				{
					bean.setHospitalizationTabSummaryList(hospitalizationTabList);
				}
			}
			
			if((null != bean.getPreHospitalizaionFlag() && bean.getPreHospitalizaionFlag()))
			{
				List<PreHospitalizationDTO> preHospitalizationTabList = viewBillSummary.getPreHospitalizationTabValues();
				if(null != preHospitalizationTabList && !preHospitalizationTabList.isEmpty())
				{
					bean.setPreHospitalizationTabSummaryList(preHospitalizationTabList);
				}
			}
			if((null != bean.getPostHospitalizaionFlag() && bean.getPostHospitalizaionFlag()))
			{
				List<PreHospitalizationDTO> postHospitalizationTabList = viewBillSummary.getPostHospitalizationTabValues();
				if(null != postHospitalizationTabList && !postHospitalizationTabList.isEmpty())
				{
					bean.setPostHospitalizationTabSummaryList(postHospitalizationTabList);
				}
			}
			
		}
	}
	
	public Boolean validatePage()
	{
		Boolean hasError = false;
		String eMsg = "";
		
		Long productKey = bean.getPolicyDto()
				.getProduct().getKey();
		if(null != productKey && (productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)))
		{
			if((null != bean.getHospitalizaionFlag() && bean.getHospitalizaionFlag()) || (null != bean.getIsHospitalizationRepeat() && bean.getIsHospitalizationRepeat()))
			{
				List<BillEntryDetailsDTO> hospitalizationTabList = viewBillSummary.getHospitalizationTabValues();
				if(null != hospitalizationTabList && !hospitalizationTabList.isEmpty())
				{
					for (BillEntryDetailsDTO billEntryDetailsDTO : hospitalizationTabList) {
						if(null != billEntryDetailsDTO.getDeductibleOrNonPayableReason() && !("").equalsIgnoreCase(billEntryDetailsDTO.getDeductibleOrNonPayableReason()))
						{
							if(null == billEntryDetailsDTO.getDeductibleNonPayableReasonBilling() || ("").equalsIgnoreCase(billEntryDetailsDTO.getDeductibleNonPayableReasonBilling()))
							{
								hasError = true;
								eMsg += " Please enter deductible or non payable reason billing remarks in hospitalization for item  " + billEntryDetailsDTO.getItemName() + " .</br>";
							}
						}
						
					}
				}
			}
			
			if((null != bean.getPreHospitalizaionFlag() && bean.getPreHospitalizaionFlag()))
			{
				List<PreHospitalizationDTO> preHospitalizationTabList = viewBillSummary.getPreHospitalizationTabValues();
				if(null != preHospitalizationTabList && !preHospitalizationTabList.isEmpty())
				{
					for (PreHospitalizationDTO preHospitalization : preHospitalizationTabList) {
						if(null != preHospitalization.getReason() && !("").equalsIgnoreCase(preHospitalization.getReason()))
						{
							if(null == preHospitalization.getDeductibleNonPayableReasonBilling() || ("").equalsIgnoreCase(preHospitalization.getDeductibleNonPayableReasonBilling()))
							{
								hasError = true;
								eMsg += "Please enter deductible or non payable reason billing remarks in pre hospitalization for item No " + preHospitalization.getDetails()+ " .</br>";
							}
						}
						
					}
				}
			}
			if((null != bean.getPostHospitalizaionFlag() && bean.getPostHospitalizaionFlag()))
			{
				List<PreHospitalizationDTO> postHospitalizationTabList = viewBillSummary.getPostHospitalizationTabValues();
				if(null != postHospitalizationTabList && !postHospitalizationTabList.isEmpty())
				{
					for (PreHospitalizationDTO postHospitalization : postHospitalizationTabList) {
						if(null != postHospitalization.getReason() && !("").equalsIgnoreCase(postHospitalization.getReason()))
						{
							if(null == postHospitalization.getDeductibleNonPayableReasonBilling() || ("").equalsIgnoreCase(postHospitalization.getDeductibleNonPayableReasonBilling()))
							{
								hasError = true;
								eMsg += "Please enter deductible or non payable reason billing remarks in post hospitalization for item No " + postHospitalization.getDetails()+ " .</br>";
							}
						}
						
					}
				}
			}
			
			
			

			if (hasError) {
				//setRequired(true);
				Label label = new Label(eMsg, ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				hasError = true;
				return !hasError;
			} 
		}
		
		 if((productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)))
		 {
			 viewBillSummary.setValuesForSeniorCitizen();
		 }
		
		return true;
	}

}
