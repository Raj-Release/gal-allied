package com.shaic.claim.reimbursement.billing.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.PostHopitalizationDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class PostHospitalizationCalcualtionUI extends ViewComponent {

	private static final long serialVersionUID = -4222456099408568886L;

	private BeanFieldGroup<PostHopitalizationDetailsDTO> binder;

	private PreauthDTO bean;

	private TextField netAmount;

	private TextField eligibleAmtTxt;

	private TextField copayAmt;

	private TextField eligibleAmtAftCopay;

	private TextField maxPayableAmtTxt;

	private TextField netPayableAmt;

	private TextField claimRestrictionAmtTxt;

	private TextField payableToInsAmtTxt;

	private TextField balancePremiumTxt;

	private TextField payableToInsAftPremiumTxt;

	private TextField availableSIAftHosp;

	private TextField restrictedSIAftHosp;

	private TextField amountPayable;

	private TextField previousRODPostHospAmt;

	private TextField otherInsuredTxt;

	private TextField amountConsideredForPaymentTxt;

	private TextField revisedEligibleAmtTxt;

	private TextField amountAlreadyPaid;

	private TextField balanceToBePaid;

	private TextField availableSublimitForBariatric;
	
	private TextField section3AvailableSIAfterHospROD;
	
	//private TextField dummyField = null;
	
	/*public void setTextField(){
		dummyField = new TextField("Dummy Field");
	}*/

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {

	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<PostHopitalizationDetailsDTO>(
				PostHopitalizationDetailsDTO.class);
		this.binder.setItemDataSource(bean
				.getPostHospitalizationCalculationDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void initView(PreauthDTO bean) {
		this.bean = bean;
		initBinder();

		netAmount = (TextField) binder.buildAndBind("Net Amount", "netAmount",
				TextField.class);
		netAmount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		// eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		netAmount.setReadOnly(true);

		otherInsuredTxt = (TextField) binder.buildAndBind(
				"Amount received from other Insurers", "otherInsurerAmt",
				TextField.class);
		otherInsuredTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		// eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		otherInsuredTxt.setReadOnly(true);

		amountConsideredForPaymentTxt = (TextField) binder.buildAndBind(
				"Amount considered for payment", "amountConsideredForPayment",
				TextField.class);
		amountConsideredForPaymentTxt
				.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		// eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		amountConsideredForPaymentTxt.setReadOnly(true);

		previousRODPostHospAmt = (TextField) binder.buildAndBind(
				"Previous ROD Post Hosp Amt", "previousRodPostHospamt",
				TextField.class);
		previousRODPostHospAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		// eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		previousRODPostHospAmt.setReadOnly(true);

		eligibleAmtTxt = (TextField) binder.buildAndBind(
				"Eligible Amt (As Per Policy)", "eligibleAmt", TextField.class);
		eligibleAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		// eligibleAmtTxt.setValue(this.bean.getPostHospitalisationValue().toString());
		eligibleAmtTxt.setReadOnly(true);

		claimRestrictionAmtTxt = (TextField) binder.buildAndBind(
				"Claim Restriction (As Per Policy)", "claimRestrictionAmt",
				TextField.class);
		claimRestrictionAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		claimRestrictionAmtTxt.setReadOnly(true);

		maxPayableAmtTxt = (TextField) binder.buildAndBind("Maximum Payable",
				"maxPayable", TextField.class);
		maxPayableAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		maxPayableAmtTxt.setReadOnly(true);

		copayAmt = (TextField) binder.buildAndBind("Co-Pay Amt        "
				+ "     "
				+ this.bean.getPostHospitalizationCalculationDTO()
						.getCopayValue().toString() + " %", "copayAmt",
				TextField.class);
		copayAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		copayAmt.setReadOnly(true);

		amountPayable = (TextField) binder.buildAndBind("Amount Payable",
				"amountPayable", TextField.class);
		amountPayable.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		amountPayable.setReadOnly(true);

		eligibleAmtAftCopay = (TextField) binder.buildAndBind(
				"Net Payable / Eligible Amt (Aft Co-Pay)", "payableAmt",
				TextField.class);
		eligibleAmtAftCopay.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		eligibleAmtAftCopay.setReadOnly(true);
		
		section3AvailableSIAfterHospROD = (TextField) binder.buildAndBind("Available balance  Under  Section 3 after Hospitalisation ROD",
				"section3AvailableSIAfterHospROD", TextField.class);
		section3AvailableSIAfterHospROD.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		section3AvailableSIAfterHospROD.setReadOnly(true);

		availableSublimitForBariatric = (TextField) binder.buildAndBind(
				"Available Sublimit balance for Bariatric",
				"avaliableSublimitBalanceForBariatric", TextField.class);
		availableSublimitForBariatric
				.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		availableSublimitForBariatric.setReadOnly(true);

		revisedEligibleAmtTxt = (TextField) binder.buildAndBind(
				"Revised eligible amount payable", "revisedEligibleAmount",
				TextField.class);
		revisedEligibleAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		revisedEligibleAmtTxt.setReadOnly(true);

		payableToInsAmtTxt = (TextField) binder.buildAndBind(
				"Revised amount payable", "payableToInsAmt", TextField.class);
		payableToInsAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAmtTxt.setReadOnly(true);

		netPayableAmt = (TextField) binder.buildAndBind(
				"Net Payable / Eligible Amt", "netPayable", TextField.class);
		netPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		netPayableAmt.setReadOnly(true);

		availableSIAftHosp = (TextField) binder.buildAndBind(
				"Available Sum Insured After Hopitalization",
				"avaliableSumInsuredAftHosp", TextField.class);
		availableSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		availableSIAftHosp.setReadOnly(true);

		if (bean.getPostHospitalizationCalculationDTO()
				.getIsSIRestrictionAvail()) {
			restrictedSIAftHosp = (TextField) binder.buildAndBind(
					"Restricted Sum Insured After Hospitalization",
					"restrictedSIAftHosp", TextField.class);
			restrictedSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			restrictedSIAftHosp.setReadOnly(true);
		} else {
			restrictedSIAftHosp = new TextField(
					"Restricted Sum Insured After Hospitalization");
			restrictedSIAftHosp.setValue("0");
			if (bean.getPostHospitalizaionFlag()) {
				restrictedSIAftHosp.setValue("NA");
			}
			restrictedSIAftHosp.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			restrictedSIAftHosp.setReadOnly(true);
		}

		balancePremiumTxt = (TextField) binder.buildAndBind(
				"Balance Premium to be deducted", "balancePremiumAmt",
				TextField.class);
		balancePremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		balancePremiumTxt.setReadOnly(true);

		payableToInsAftPremiumTxt = (TextField) binder.buildAndBind(
				"Payable to Insured (After Premium)",
				"payableToInsuredAftPremiumAmt", TextField.class);
		payableToInsAftPremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAftPremiumTxt.setReadOnly(true);

		FormLayout formLayout = null;

		if (this.bean.getIsReconsiderationRequest() != null
				&& this.bean.getIsReconsiderationRequest()) {
			amountAlreadyPaid = (TextField) binder.buildAndBind(
					"Amount already paid to Insured", "amountAlreadyPaid",
					TextField.class);
			amountAlreadyPaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			amountAlreadyPaid.setReadOnly(true);

			balanceToBePaid = (TextField) binder.buildAndBind(
					"Balance to be paid to Insured", "balanceToBePaid",
					TextField.class);
			balanceToBePaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			balanceToBePaid.setReadOnly(true);

			if ((bean.getNewIntimationDTO() != null
					&& bean.getNewIntimationDTO().getPolicy() != null
					&& bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable
					.getSeniorCitizenKeys().containsKey(
							bean.getNewIntimationDTO().getPolicy().getProduct()
									.getKey()))) {
				formLayout = new FormLayout(netAmount, otherInsuredTxt,
						amountConsideredForPaymentTxt, eligibleAmtTxt,
						maxPayableAmtTxt, amountPayable, availableSIAftHosp,
						restrictedSIAftHosp, netPayableAmt,
						claimRestrictionAmtTxt, previousRODPostHospAmt,
						payableToInsAmtTxt, balancePremiumTxt,
						payableToInsAftPremiumTxt, amountAlreadyPaid,
						balanceToBePaid);
				if (bean.getPreauthDataExtractionDetails()
						.getSectionDetailsDTO().getSection() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO().getSection()
								.getCommonValue() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO()
								.getSection()
								.getCommonValue()
								.equalsIgnoreCase(
										ReferenceTable.BARIATRIC_SECTION_CODE)) {
					formLayout.addComponent(availableSublimitForBariatric, 6);
				}
			} else {
				formLayout = new FormLayout(netAmount, otherInsuredTxt,
						amountConsideredForPaymentTxt, eligibleAmtTxt,
						maxPayableAmtTxt, amountPayable, copayAmt,
						eligibleAmtAftCopay, availableSIAftHosp,
						restrictedSIAftHosp, netPayableAmt,
						claimRestrictionAmtTxt, previousRODPostHospAmt,
						payableToInsAmtTxt, balancePremiumTxt,
						payableToInsAftPremiumTxt, amountAlreadyPaid,
						balanceToBePaid);
				if (bean.getPreauthDataExtractionDetails()
						.getSectionDetailsDTO().getSection() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO().getSection()
								.getCommonValue() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO()
								.getSection()
								.getCommonValue()
								.equalsIgnoreCase(
										ReferenceTable.BARIATRIC_SECTION_CODE)) {
					formLayout.addComponent(availableSublimitForBariatric, 6);
				}
			}
		} else {
			if ((bean.getNewIntimationDTO() != null
					&& bean.getNewIntimationDTO().getPolicy() != null
					&& bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable
					.getSeniorCitizenKeys().containsKey(
							bean.getNewIntimationDTO().getPolicy().getProduct()
									.getKey()))) {
				formLayout = new FormLayout(netAmount, otherInsuredTxt,
						amountConsideredForPaymentTxt, eligibleAmtTxt,
						maxPayableAmtTxt, amountPayable, availableSIAftHosp,
						restrictedSIAftHosp, netPayableAmt,
						claimRestrictionAmtTxt, previousRODPostHospAmt,
						payableToInsAmtTxt, balancePremiumTxt,
						payableToInsAftPremiumTxt);
				if (bean.getPreauthDataExtractionDetails()
						.getSectionDetailsDTO().getSection() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO().getSection()
								.getCommonValue() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO()
								.getSection()
								.getCommonValue()
								.equalsIgnoreCase(
										ReferenceTable.BARIATRIC_SECTION_CODE)) {
					formLayout.addComponent(availableSublimitForBariatric, 6);
				}
			} else {
				formLayout = new FormLayout(netAmount, otherInsuredTxt,
						amountConsideredForPaymentTxt, eligibleAmtTxt,
						maxPayableAmtTxt, amountPayable, copayAmt,
						eligibleAmtAftCopay, availableSIAftHosp,
						restrictedSIAftHosp, netPayableAmt,
						claimRestrictionAmtTxt, previousRODPostHospAmt,
						payableToInsAmtTxt, balancePremiumTxt,
						payableToInsAftPremiumTxt);
				if (bean.getPreauthDataExtractionDetails()
						.getSectionDetailsDTO().getSection() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO().getSection()
								.getCommonValue() != null
						&& bean.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO()
								.getSection()
								.getCommonValue()
								.equalsIgnoreCase(
										ReferenceTable.BARIATRIC_SECTION_CODE)) {
					formLayout.addComponent(availableSublimitForBariatric, 8);
				}
			}
		}
		
		/*if(dummyField != null){
			formLayout.addComponent(dummyField);
		}*/
		
		formLayout.setCaption("Post-Hospitalization Calculation");
		HorizontalLayout layout = new HorizontalLayout(formLayout);
		layout.setCaption("Post-Hospitalization Calculation");
		setCompositionRoot(formLayout);
	}

	public String getPostHospAmt() {
		if (bean.getIsReconsiderationRequest() != null
				&& bean.getIsReconsiderationRequest()) {
			return balanceToBePaid.getValue();
		}
		return payableToInsAftPremiumTxt.getValue() != null ? payableToInsAftPremiumTxt
				.getValue() : "0";
	}

	public String getPreHospAmt() {
		if (bean.getIsReconsiderationRequest() != null
				&& bean.getIsReconsiderationRequest()) {
			return balanceToBePaid.getValue();
		}
		return payableToInsAftPremiumTxt.getValue() != null ? payableToInsAftPremiumTxt
				.getValue() : "0";
	}

	// public String getAmountAlreadyPaid() {
	// // return amountAlreadyPaid.getValue();
	// return previousRODPostHospAmt.getValue() != null ?
	// previousRODPostHospAmt.getValue() : "0";
	// }

	public String getAmountAlreadyPaid() {
		if (amountAlreadyPaid != null) {
			return amountAlreadyPaid.getValue();
		}
		return "0";
	}

	public String getRevisiedEligibleAmt() {
		if (revisedEligibleAmtTxt != null) {
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
	
	//eligibleAmtAftCopay
}
