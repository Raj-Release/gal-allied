package com.shaic.claim.reimbursement.billing.wizard;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalizationCalcualtionUI extends ViewComponent {

	private static final long serialVersionUID = -4222456099408568886L;
	
	private BeanFieldGroup<HopitalizationCalulationDetailsDTO> binder;
	
	private PreauthDTO bean;
	
	private TextField netPayableAmtTxt;
	
	private TextField claimRestrictionAmtTxt;
	
	private TextField preauthAppAmtTxt;
	
	private TextField payableToHospitalAmtTxt;
	
	private TextField payableToInsAmtTxt;
	
	private TextField tdsAmtTxt;
	
	private TextField payableToHospitalAftTDSTxt;
	
	private TextField balancePremiumTxt;
	
	private TextField payableToInsAftPremiumTxt;
	
	private TextField amountAlreadyPaid;
	
	private TextField balanceToBePaid;
	
	private TextField txtHospitalDiscount;
	
	private TextField txtAfterHospitalDiscount;
	
	private String presenterString;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<HopitalizationCalulationDetailsDTO>(HopitalizationCalulationDetailsDTO.class);
		this.binder.setItemDataSource(bean.getHospitalizationCalculationDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(PreauthDTO bean) 
	{	
		
		String preauthApprAmt = null;
		if(! bean.getPreviousPreauthTableDTO().isEmpty()){
			for (PreviousPreAuthTableDTO preauth : bean.getPreviousPreauthTableDTO()) {
				preauthApprAmt = preauth.getApprovedAmt();
			}
		}
		
		this.bean = bean;
		initBinder();
		FormLayout formLayout = new FormLayout();
		netPayableAmtTxt = (TextField) binder.buildAndBind("Net Payable / Eligible Amt* (After Co-Pay)",
				"netPayableAmt", TextField.class);
		netPayableAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		netPayableAmtTxt.setValue(this.bean.getHospitalisationValue());
		netPayableAmtTxt.setReadOnly(true);
		
		
		claimRestrictionAmtTxt = (TextField) binder.buildAndBind("Claim Restriction (As Per Policy)",
				"claimRestrictionAmt", TextField.class);
		claimRestrictionAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		claimRestrictionAmtTxt.setReadOnly(true);
		
		preauthAppAmtTxt = (TextField) binder.buildAndBind("Pre-auth Approved Amt",
				"preauthAppAmt", TextField.class);
		preauthAppAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		preauthAppAmtTxt.setValidationVisible(false);
//		preauthAppAmtTxt.setValue(preauthApprAmt);
		preauthAppAmtTxt.setReadOnly(true);
		preauthAppAmtTxt.setNullRepresentation("0");
		
		
		payableToHospitalAmtTxt = (TextField) binder.buildAndBind("Paid / Payable to Hospital",
				"payableToHospitalAmt", TextField.class);
		payableToHospitalAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToHospitalAmtTxt.setReadOnly(true);
		
		//added for hospital discount change/nofel
//    	txtHospitalDiscount.setValue(String.valueOf((bean.getHospDiscountAmount() != null && bean.getHospDiscountAmount() > 0) ? bean.getHospDiscountAmount() : null));
		txtHospitalDiscount = new TextField("Network Hospital Discount");
     	txtHospitalDiscount.setValue(String.valueOf((bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0) ? bean.getHospitalizationCalculationDTO().getHospitalDiscount() : ""));
	//	txtHospitalDiscount = (TextField) binder.buildAndBind("Network Hospital Discount",
			//	"hospitalDiscount", TextField.class);
		txtHospitalDiscount.setWidth("60px");
		
		CSValidator remarksValidator = new CSValidator();
		remarksValidator.extend(txtHospitalDiscount);
		remarksValidator.setRegExp("^[0-9/]*$");
		remarksValidator.setPreventInvalidTyping(true);
		txtHospitalDiscount.setNullRepresentation("");
		
		txtAfterHospitalDiscount = (TextField) binder.buildAndBind("Payable to Hospital after Discount",
				"afterHospitalDiscount", TextField.class);
		txtAfterHospitalDiscount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		txtAfterHospitalDiscount.setEnabled(false);
		
		payableToInsAmtTxt = (TextField) binder.buildAndBind("Payable to Insured",
				"payableToInsAmt", TextField.class);
		payableToInsAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAmtTxt.setReadOnly(true);
		
		tdsAmtTxt = (TextField) binder.buildAndBind("TDS Amount",
				"tdsAmt", TextField.class);
		tdsAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		tdsAmtTxt.setReadOnly(true);
		
		payableToHospitalAftTDSTxt = (TextField) binder.buildAndBind("Payable to Hospital (After TDS)",
				"payableToHospitalAftTDSAmt", TextField.class);
		payableToHospitalAftTDSTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToHospitalAftTDSTxt.setReadOnly(true);
		
		balancePremiumTxt = (TextField) binder.buildAndBind("Balance Premium to be deducted",
				"balancePremiumAmt", TextField.class);
		balancePremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		balancePremiumTxt.setReadOnly(true);
		
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
		balancePremiumTxt.setCaption("Total Balance Installment  Premiurm");
		}
		
		payableToInsAftPremiumTxt = (TextField) binder.buildAndBind("Payable to Insured (After Premium)",
				"payableToInsuredAftPremiumAmt", TextField.class);
		payableToInsAftPremiumTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToInsAftPremiumTxt.setReadOnly(true);
		
		if(this.bean.getIsReconsiderationRequest() != null && this.bean.getIsReconsiderationRequest()) {
			amountAlreadyPaid = (TextField) binder.buildAndBind("",
					"amountAlreadyPaid", TextField.class);
			amountAlreadyPaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			amountAlreadyPaid.setReadOnly(true);
			
//			if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
//				amountAlreadyPaid.setReadOnly(false);
//				amountAlreadyPaid.setValue("0");
//				amountAlreadyPaid.setReadOnly(true);
//			}
			
			if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				if(this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					amountAlreadyPaid.setCaption("Amount already paid to Hospital(after network hospital discount)");	
					
				}else{
					amountAlreadyPaid.setCaption("Amount already paid to Insured");	
				}
				
			}else{
				amountAlreadyPaid.setCaption("Amount already paid to Insured");
			}
			
			
			if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				if(this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					balanceToBePaid = (TextField) binder.buildAndBind("Amount payable to Hospital after discount",
							"balanceToBePaid", TextField.class);	
				}else{
					balanceToBePaid = (TextField) binder.buildAndBind("Balance to be paid to Insured",
							"balanceToBePaid", TextField.class);
				}
				
			}else{
				balanceToBePaid = (TextField) binder.buildAndBind("Balance to be paid to Insured",
						"balanceToBePaid", TextField.class);
			}
			
			
			balanceToBePaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			balanceToBePaid.setReadOnly(true);
			if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				preauthAppAmtTxt.setCaption("Pre auth approved – Hospital  (after premium deduction)");
				
				TextField preauthApprAmBeforePremium = (TextField) binder.buildAndBind("Preauth approved amount (before premium deduction)",
						"preauthAppAmtBeforePremium", TextField.class);
				preauthApprAmBeforePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				preauthApprAmBeforePremium.setValidationVisible(false);
				preauthApprAmBeforePremium.setReadOnly(true);
				preauthApprAmBeforePremium.setNullRepresentation("0");
				
				TextField installmentPremium = (TextField) binder.buildAndBind("II Installment  Premiurm",
						"installmentAmount", TextField.class);
				installmentPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				installmentPremium.setValidationVisible(false);
				installmentPremium.setReadOnly(true);
				installmentPremium.setNullRepresentation("0");
				formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthApprAmBeforePremium, installmentPremium, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			}//cr2019184
			else if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) 
					&& bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
					preauthAppAmtTxt.setCaption("Pre auth approved – Hospital  (after premium deduction)");
					
					TextField preauthApprAmBeforePremium = (TextField) binder.buildAndBind("Preauth approved amount (before premium deduction)",
							"preauthAppAmtBeforePremium", TextField.class);
					preauthApprAmBeforePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					preauthApprAmBeforePremium.setValidationVisible(false);
					preauthApprAmBeforePremium.setReadOnly(true);
					preauthApprAmBeforePremium.setNullRepresentation("0");
					
					TextField installmentPremium = (TextField) binder.buildAndBind("Total Balance Installment  Premiurm",
							"installmentAmount", TextField.class);
					installmentPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					installmentPremium.setValidationVisible(false);
					installmentPremium.setReadOnly(true);
					installmentPremium.setNullRepresentation("0");
					formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthApprAmBeforePremium, installmentPremium, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
				}
			
			}
			else {
				preauthAppAmtTxt.setCaption("Pre-auth Approved Amt");
				formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt, amountAlreadyPaid, balanceToBePaid );
			}
			
		} else {
			if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				preauthAppAmtTxt.setCaption("Pre auth approved – Hospital  (after premium deduction)");
				
				TextField preauthApprAmBeforePremium = (TextField) binder.buildAndBind("Preauth approved amount (before premium deduction)",
						"preauthAppAmtBeforePremium", TextField.class);
				preauthApprAmBeforePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				preauthApprAmBeforePremium.setValidationVisible(false);
				preauthApprAmBeforePremium.setReadOnly(true);
				preauthApprAmBeforePremium.setNullRepresentation("0");
				
				TextField installmentPremium = (TextField) binder.buildAndBind("II Installment  Premiurm",
						"installmentAmount", TextField.class);
				installmentPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				installmentPremium.setValidationVisible(false);
				installmentPremium.setReadOnly(true);
				installmentPremium.setNullRepresentation("0");
				formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthApprAmBeforePremium, installmentPremium, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
			}//cr2019184
			else if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) 
					&& bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				if(presenterString != null && (presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)  || presenterString.equalsIgnoreCase(SHAConstants.PA_FINANCIAL))){
					preauthAppAmtTxt.setCaption("Pre auth approved – Hospital  (after premium deduction)");
					
					TextField preauthApprAmBeforePremium = (TextField) binder.buildAndBind("Preauth approved amount (before premium deduction)",
							"preauthAppAmtBeforePremium", TextField.class);
					preauthApprAmBeforePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					preauthApprAmBeforePremium.setValidationVisible(false);
					preauthApprAmBeforePremium.setReadOnly(true);
					preauthApprAmBeforePremium.setNullRepresentation("0");
					
					TextField installmentPremium = (TextField) binder.buildAndBind("Total Balance Installment  Premiurm",
							"installmentAmount", TextField.class);
					installmentPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					installmentPremium.setValidationVisible(false);
					installmentPremium.setReadOnly(true);
					installmentPremium.setNullRepresentation("0");
					formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthApprAmBeforePremium, installmentPremium, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
				}
			}
			else {
				preauthAppAmtTxt.setCaption("Pre-auth Approved Amt");
				formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
			}
			
		}
		
		calculateHospitalDiscount(formLayout);

//		FormLayout formLayout = new FormLayout(netPayableAmtTxt, claimRestrictionAmtTxt, preauthAppAmtTxt, payableToHospitalAmtTxt, payableToInsAmtTxt, tdsAmtTxt,payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
		formLayout.setCaption("Hospitalization Calculation");
		HorizontalLayout layout = new HorizontalLayout(formLayout);
		layout.setCaption("Hospitalization Calculation");
		setCompositionRoot(layout);
		
		
		
		
		addListener();
		
		isValid();
	}

	private void calculateHospitalDiscount(FormLayout formLayout) {
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			formLayout.addComponent(txtHospitalDiscount,6);
			formLayout.addComponent(txtAfterHospitalDiscount,7);
			if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
				mandatoryFields.add(txtHospitalDiscount);
				setRequiredAndValidation(txtHospitalDiscount);
				}
//			commented for hospital discount zero issue when we go back to the screen
//			if(txtHospitalDiscount.getValue() != null && payableToHospitalAmtTxt.getValue() != null){
				if(payableToHospitalAmtTxt.getValue() != null){
				Integer payableToHospital = payableToHospitalAmtTxt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(payableToHospitalAmtTxt.getValue()) : null;
				Integer hospitalDiscount = txtHospitalDiscount.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(txtHospitalDiscount.getValue()) : 0;
				if(payableToHospital != null && hospitalDiscount != null){
				  Integer afterDiscount = payableToHospital-hospitalDiscount;
				  if(afterDiscount < 0){
					  afterDiscount = 0;
				  }
				  txtAfterHospitalDiscount.setReadOnly(false);
				  txtAfterHospitalDiscount.setValue(afterDiscount.toString());
				  txtAfterHospitalDiscount.setReadOnly(true);
				  payableToHospitalAftTDSTxt.setReadOnly(false);
				  payableToHospitalAftTDSTxt.setValue(afterDiscount.toString());
				  payableToHospitalAftTDSTxt.setReadOnly(true);
				}
			}
			
		} else if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
				&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPartialHospitalisationFlag() != null 
				&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
			formLayout.addComponent(txtHospitalDiscount,4);
			txtHospitalDiscount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			txtHospitalDiscount.setReadOnly(true);
			formLayout.addComponent(txtAfterHospitalDiscount,5);
			txtAfterHospitalDiscount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			txtAfterHospitalDiscount.setReadOnly(true);
			if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
			mandatoryFields.remove(txtHospitalDiscount);
			}
		}
		if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && (txtHospitalDiscount.getValue() != null)) {
			isValid();
		}
		
	}
	
	
	public void addListener(){
		
		txtHospitalDiscount.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				if(component.getValue() == null || component.getValue().isEmpty() || component.getValue().equalsIgnoreCase("")){
//					component.setValue("0");
					//added for hospital discount issue/nofel
					bean.getHospitalizationCalculationDTO().setHospitalDiscount(null);
				}else{
					Integer payableToHospital = payableToHospitalAmtTxt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(payableToHospitalAmtTxt.getValue()) : null;
					Integer hospitalDiscount = component.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(component.getValue()) : null;
					if(payableToHospital != null && hospitalDiscount != null){
						Integer amtAfterDiscount = payableToHospital - hospitalDiscount;
						
						 if(amtAfterDiscount < 0){
							 amtAfterDiscount = 0;
						  }
						
						txtAfterHospitalDiscount.setReadOnly(false);
						txtAfterHospitalDiscount.setValue(amtAfterDiscount.toString());
						txtAfterHospitalDiscount.setReadOnly(true);
						
						
						payableToHospitalAftTDSTxt.setReadOnly(false);
						payableToHospitalAftTDSTxt.setValue(amtAfterDiscount.toString());
						payableToHospitalAftTDSTxt.setReadOnly(true);
						bean.getHospitalizationCalculationDTO().setHospitalDiscount(hospitalDiscount);
						
						if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()){
							if(amountAlreadyPaid.getValue() != null){
							Integer amountAlready = SHAUtils.getIntegerFromStringWithComma(amountAlreadyPaid.getValue());
							Integer balancePaidAmt = amtAfterDiscount - amountAlready;
							if(balancePaidAmt <0){
								balancePaidAmt = 0;
							}
							balanceToBePaid.setReadOnly(false);
							balanceToBePaid.setValue(balancePaidAmt.toString());
							balanceToBePaid.setReadOnly(true);
							
							}
							
						}
						if(component.getValue() != null ){
							bean.getHospitalizationCalculationDTO().setHospitalDiscount(component.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(component.getValue()) : 0);
							bean.getHospitalizationCalculationDTO().setOnBackFlag(true);
						}
						
						isValid();
					}
				}
				
				isValid();
				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
					fireViewEvent(FinancialHospitalizationPagePresenter.CONSOLIDATE_AMT_AFTER_HOSP_NETWORK_DISCNT, bean);
				}
			}



		});
		
	}
	
	public void isValid(){
		try {
			binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHospAmt() {
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
			if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
				return balanceToBePaid.getValue();
			}
			return payableToHospitalAftTDSTxt.getValue();
		} else {
			if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
				return balanceToBePaid.getValue();
			}
			return payableToInsAftPremiumTxt.getValue();
		}
		
	}
	
	public String getAmountAlreadyPaid() {
		if(amountAlreadyPaid != null) {
			return amountAlreadyPaid.getValue();
		}
		return "0";
	}
	
	public String getNetPayableAmtAfterCopay(){
		if(netPayableAmtTxt != null) {
			return netPayableAmtTxt.getValue();
		}
		return "0";
	}
	
	public String getPayableToInsuredAmount(){
		if(payableToInsAftPremiumTxt != null) {
			return payableToInsAftPremiumTxt
					.getValue();
		}
		return "0";
	}
	
	public void setPresenterString(String presenterString)
	{
		this.presenterString = presenterString;
		
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	public Long getHospitalizationApprovedamt(){
		if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
			Long amountAlready = Long.parseLong(getAmountAlreadyPaid().replaceAll(",",""));
			if(balanceToBePaid !=null){
				Long balancePaidAmt = Long.parseLong(balanceToBePaid.getValue());
				if(balancePaidAmt <0){
					balancePaidAmt = 0L;
				}
				return amountAlready+balancePaidAmt;
			}
		}
		return 0L;
	}
	public Long getHospitalizationCashlessApprovedamt(){
		
		if(txtAfterHospitalDiscount !=null){
			Long approvedamt = Long.parseLong(txtAfterHospitalDiscount.getValue().replaceAll(",",""));
			return approvedamt;
		}
		return 0L;
	}
	
	public Integer getPremiumAmountEqualZero(){

		if(netPayableAmtTxt !=null){
			Integer approvedamt = Integer.parseInt(netPayableAmtTxt.getValue().replaceAll(",",""));
			return approvedamt;
		}
		return 0;
	}
}
