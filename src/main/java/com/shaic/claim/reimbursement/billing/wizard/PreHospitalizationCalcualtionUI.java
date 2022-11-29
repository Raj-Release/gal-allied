package com.shaic.claim.reimbursement.billing.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.PreHopitalizationDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class PreHospitalizationCalcualtionUI extends ViewComponent {

	private static final long serialVersionUID = -4222456099408568886L;
	
	private BeanFieldGroup<PreHopitalizationDetailsDTO> binder;
	
	private PreauthDTO bean;
	
	private TextField amountconsidered;
	
	private TextField copayAmt;
	
	private TextField eligibleAmtAftCopay;
	
	private TextField netPayableAmt;
	
	private TextField claimRestrictionAmtTxt;
	
	private TextField payableToInsAmtTxt;
	
	private TextField balancePremiumTxt;
	
	private TextField payableToInsAftPremiumTxt;
	
	private TextField availableSIAftHosp;
	
	private TextField restrictedSIAftHosp;
	
	private TextField otherInsuredTxt;
	
	private TextField amountConsideredForPaymentTxt;
	
	private TextField revisedEligibleAmtTxt;

	private TextField previousRODPreHospAmt;
	
	private TextField availableSublimitForBariatric;
	
	private TextField amountAlreadyPaid;
	
	private TextField balanceToBePaid;
	
	private TextField section3AvailableSIAfterHospROD;
	
	
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreHopitalizationDetailsDTO>(PreHopitalizationDetailsDTO.class);
		this.binder.setItemDataSource(bean.getPreHospitalizationCalculationDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(PreauthDTO bean) 
	{
		this.bean = bean;
		initBinder();
		amountconsidered = (TextField) binder.buildAndBind("Amount Considered",
				"amountConsidered", TextField.class);
		amountconsidered.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		amountconsidered.setValue(this.bean.getPreHospitalisationValue().toString());
		amountconsidered.setReadOnly(true);
		
		otherInsuredTxt = (TextField) binder.buildAndBind("Amount received from other Insurers",
				"otherInsurerAmt", TextField.class);
		otherInsuredTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		otherInsuredTxt.setReadOnly(true);
		
		
		amountConsideredForPaymentTxt = (TextField) binder.buildAndBind("Amount considered for payment",
				"amountConsideredForPayment", TextField.class);
		amountConsideredForPaymentTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		amountConsideredForPaymentTxt.setReadOnly(true);
		
		claimRestrictionAmtTxt = (TextField) binder.buildAndBind("Claim Restriction (As Per Policy)",
				"claimRestrictionAmt", TextField.class);
		claimRestrictionAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		claimRestrictionAmtTxt.setReadOnly(true);
		
		copayAmt = (TextField) binder.buildAndBind("Co-Pay Amt        "+ "     " + this.bean.getPreHospitalizationCalculationDTO().getCopayValue().toString() + " %",
				"copayAmt", TextField.class);
		copayAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		copayAmt.setReadOnly(true);
		
		eligibleAmtAftCopay = (TextField) binder.buildAndBind("Net Payable / Eligible Amt (Aft Co-Pay)",
				"payableAmt", TextField.class);
		eligibleAmtAftCopay.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		eligibleAmtAftCopay.setReadOnly(true);
		
		section3AvailableSIAfterHospROD = (TextField) binder.buildAndBind("Available balance  Under  Section 3 after Hospitalisation ROD",
				"section3AvailableSIAfterHospROD", TextField.class);
		section3AvailableSIAfterHospROD.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		section3AvailableSIAfterHospROD.setReadOnly(true);
		
		availableSublimitForBariatric = (TextField) binder.buildAndBind("Available Sublimit balance for Bariatric",
				"avaliableSublimitBalanceForBariatric", TextField.class);
		availableSublimitForBariatric.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		availableSublimitForBariatric.setReadOnly(true);
		
		availableSIAftHosp = (TextField) binder.buildAndBind("Available Sum Insured after Hospitalization & Post Hospitalization",
				"availableSIAftHosp", TextField.class);
		availableSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		availableSIAftHosp.setReadOnly(true);
		
		revisedEligibleAmtTxt = (TextField) binder.buildAndBind("Revised eligible amount payable",
				"revisedEligibleAmount", TextField.class);
		revisedEligibleAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		revisedEligibleAmtTxt.setReadOnly(true);
		
		if(bean.getPreHospitalizationCalculationDTO().getIsSIRestrictionAvail()) {
			restrictedSIAftHosp = (TextField) binder.buildAndBind("Restricted Sum Insured after Hospitalization & Post Hospitalization",
					"restrictedSIAftHosp", TextField.class);
			restrictedSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			restrictedSIAftHosp.setReadOnly(true);
		} else {
			restrictedSIAftHosp = new TextField("Restricted Sum Insured After Hospitalization");
			restrictedSIAftHosp.setValue("0");
			if(bean.getPreHospitalizaionFlag()) {
				restrictedSIAftHosp.setValue("NA");
			}
			restrictedSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			restrictedSIAftHosp.setReadOnly(true);
		}
		
//		restrictedSIAftHosp = (TextField) binder.buildAndBind("Restricted Sum Insured after Hospitalization & Post Hospitalization",
//				"restrictedSIAftHosp", TextField.class);
//		restrictedSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		restrictedSIAftHosp.setReadOnly(true);
		
		payableToInsAmtTxt = (TextField) binder.buildAndBind("Payable to Insured",
				"payableToInsAmt", TextField.class);
		payableToInsAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAmtTxt.setReadOnly(true);
		
		netPayableAmt = (TextField) binder.buildAndBind("Net Payable / Eligible Amt",
				"netPayable", TextField.class);
		netPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		netPayableAmt.setReadOnly(true);
		
		balancePremiumTxt = (TextField) binder.buildAndBind("Balance Premium to be deducted",
				"balancePremiumAmt", TextField.class);
		balancePremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		balancePremiumTxt.setReadOnly(true);
		
		payableToInsAftPremiumTxt = (TextField) binder.buildAndBind("Payable to Insured (After Premium)",
				"payableToInsuredAftPremiumAmt", TextField.class);
		payableToInsAftPremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAftPremiumTxt.setReadOnly(true);
		
		previousRODPreHospAmt = (TextField) binder.buildAndBind("Previous ROD Pre-Hosp Amt",
				"previousRodPreviousPrehospAmt", TextField.class);
		previousRODPreHospAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		previousRODPreHospAmt.setReadOnly(true);
		FormLayout formLayout = null;
		if(this.bean.getIsReconsiderationRequest() != null && this.bean.getIsReconsiderationRequest()) {
			amountAlreadyPaid = (TextField) binder.buildAndBind("Amount already paid to Insured",
					"amountAlreadyPaid", TextField.class);
			amountAlreadyPaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			amountAlreadyPaid.setReadOnly(true);
			
			balanceToBePaid = (TextField) binder.buildAndBind("Balance to be paid to Insured",
					"balanceToBePaid", TextField.class);
			balanceToBePaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			balanceToBePaid.setReadOnly(true);
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay, availableSublimitForBariatric, availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			} else {
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay, availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			}
			
			/*if(null != bean.getNewIntimationDTO().getPolicy().getKey() && (ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getKey()))){
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay,section3AvailableSIAfterHospROD,availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			}*/
			
		} else {
			if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay,availableSublimitForBariatric, availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
			} else {
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay, availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
			}
			
			/*if(null != bean.getNewIntimationDTO().getPolicy().getKey() && (ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getKey()))){
				formLayout = new FormLayout(amountconsidered,otherInsuredTxt, amountConsideredForPaymentTxt, copayAmt,eligibleAmtAftCopay,section3AvailableSIAfterHospROD,availableSIAftHosp, restrictedSIAftHosp, netPayableAmt, claimRestrictionAmtTxt,revisedEligibleAmtTxt, previousRODPreHospAmt, payableToInsAmtTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			}*/
		}
		
		formLayout.setCaption("Pre-Hospitalization Calculation");
		HorizontalLayout layout = new HorizontalLayout(formLayout);
		layout.setCaption("Pre-Hospitalization Calculation");
		setCompositionRoot(layout);
	}
	
	public String getPreHospAmt() {
		if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
			return balanceToBePaid.getValue();
		}
		return payableToInsAftPremiumTxt.getValue() != null ? payableToInsAftPremiumTxt.getValue() : "0";
	}
	
	public String getAmountAlreadyPaid() {
		if(amountAlreadyPaid != null) {
			return amountAlreadyPaid.getValue();
		}
		return "0";
	}

	public String getRevisiedEligibleAmt() {
		if(revisedEligibleAmtTxt != null) {
			return revisedEligibleAmtTxt.getValue();
		}
		return "0";
	}
	
	
	public String getAvailableSumInsuredAftHosp() {
		if(availableSIAftHosp != null) {
			return availableSIAftHosp.getValue();
		}
		return "0";
	}
	
	public String getEligibleAmount() {
		if(eligibleAmtAftCopay != null) {
			return eligibleAmtAftCopay.getValue();
		}
		return "0";
	}
	
	
	
}
