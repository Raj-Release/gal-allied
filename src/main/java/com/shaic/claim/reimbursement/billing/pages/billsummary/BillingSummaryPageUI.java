
package com.shaic.claim.reimbursement.billing.pages.billsummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.listenerTables.UpdateOtherClaimDetailsUI;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class BillingSummaryPageUI extends ViewComponent {

	private static final long serialVersionUID = -924498899058179987L;
	

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	private TextField txtPreauthApprovedAmount;
	
	@Inject
	private UpdateOtherClaimDetailsUI updateOtherClaimDetailsUI;
	
	@EJB
	private PreauthService preauthService;
	
	
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


		Button updatePreviousButton = new Button("Update Previous/Other Claim Details(Defined Limit)");
		
		updatePreviousButton.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(BillingSummaryPagePresenter.UPDATE_PREVIOUS_CLAIM_DETAILS, bean);
			}
		});
		
		VerticalLayout mainVertical = null;
		
		Product product =bean.getNewIntimationDTO().getPolicy().getProduct();
		
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			mainVertical = new VerticalLayout(updatePreviousButton,txtPreauthApprovedAmount,viewBillSummary);
			mainVertical.setComponentAlignment(updatePreviousButton, Alignment.TOP_RIGHT);
		}else{
			mainVertical = new VerticalLayout(txtPreauthApprovedAmount,viewBillSummary);
		}
		
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
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
					Boolean isPropCheckOrNot = viewBillSummary.getIscheckedPropOrNot();
					if(isPropCheckOrNot){
						 bean.setBillEntryDetailsDTO(hospitalizationTabList);
						 viewBillSummary.calculateTotalForReport();
						 viewBillSummary.setGmcFooterValue();
//						 this.bean.setProportionateDeductionTotalAmt(footerValues.get(SHAConstants.PROPORTION_DEDUCTION_AMOUNT));
						List<Hospitalisation> hospitalisationList = preauthService.getHospitalisationList(bean.getKey());
						for (Hospitalisation hospitalisation : hospitalisationList) {
							if(ReferenceTable.getPropDedCalcValue().contains(hospitalisation.getBillTypeNumber())){
								for (BillEntryDetailsDTO billEntryDetailsDTO : hospitalizationTabList) {
									if(billEntryDetailsDTO.getBillTypeNumber() != null && hospitalisation.getBillTypeNumber() != null &&
											(billEntryDetailsDTO.getBillTypeNumber().equals(hospitalisation.getBillTypeNumber()))){
										hospitalisation.setTotalAmount(billEntryDetailsDTO.getNetPayableAmount().longValue());
										hospitalisation.setPayableAmount(billEntryDetailsDTO.getTotalDisallowances().longValue());
										hospitalisation.setNetAmount(billEntryDetailsDTO.getAmountAllowableAmount().longValue());
										hospitalisation.setProrateDeductionAmount(billEntryDetailsDTO.getProportionateDeduction());
										hospitalisation.setGmcProportionateFlag(billEntryDetailsDTO.getIsproportionateDeductionSelected() ? "Y" : "N");
										preauthService.saveHospitalisationPropDedValue(hospitalisation);
									}
								}
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
			
			if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
						(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
					bean.setNoOfdaysLimit(viewBillSummary.getTotalNoOfDays());
			}
			
		}
	}
	
	public Boolean validatePage()
	{
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		Long productKey = bean.getPolicyDto()
				.getProduct().getKey();
		/*the below code added for SCRC product(90,91)
		 * if(null != productKey && (productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)))*/
		if(null != productKey && (ReferenceTable.getSeniorCitizenKeys().containsKey(productKey)))
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
								eMsg.append(" Please enter deductible or non payable reason billing remarks in hospitalization for item  ").append(billEntryDetailsDTO.getItemName()).append(" .</br>");
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
								eMsg.append("Please enter deductible or non payable reason billing remarks in pre hospitalization for item No ").append(preHospitalization.getDetails()).append(" .</br>");
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
								eMsg.append("Please enter deductible or non payable reason billing remarks in post hospitalization for item No ").append(postHospitalization.getDetails()).append(" .</br>");
							}
						}
						
					}
				}
			}
			
			
			

			if (hasError) {
				//setRequired(true);
				/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
				hasError = true;
				return !hasError;
			} 
		}
		
		/* if((productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)))*/
		if(null != productKey && (ReferenceTable.getSeniorCitizenKeys().containsKey(productKey)))
		 {
			 viewBillSummary.setValuesForSeniorCitizen();
		 }
		
		return true;
	}
	
	public void setUpdateOtherClaimsDetails(
				List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList) {

			Window popup = new com.vaadin.ui.Window();
			updateOtherClaimDetailsUI.init(updateOtherClaimDetailList, null, bean,popup);
			popup.setCaption("Update Previous/Other Claim Details(Defined Limit)");
			popup.setWidth("100%");
			popup.setHeight("70%");
			popup.setContent(updateOtherClaimDetailsUI);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
			
		}

}
