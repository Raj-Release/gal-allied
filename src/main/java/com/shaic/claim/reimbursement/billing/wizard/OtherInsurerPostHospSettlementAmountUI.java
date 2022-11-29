package com.shaic.claim.reimbursement.billing.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPostHospSettlementDetailsDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OtherInsurerPostHospSettlementAmountUI extends ViewComponent {

	private static final long serialVersionUID = -4222456099408568886L;
	
	
	private BeanFieldGroup<OtherInsPostHospSettlementDetailsDTO> binder;
	
	private PreauthDTO bean;
	
	private TextField totalClaimedAmount;
	
	private TextField nonMedicalAmt;
	
	private TextField netClaimedAmt;
	
	private TextField tpaSettledAmt;
	
	private TextField balanceAmt;
	
	private TextField hospPayableAmt;
	
	private TextField payableToIns;
	
	private TextField alreadyPaidAmt;
	
	private TextField payableAmt;
	
	public TextField payableAmtChangeListenerField;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<OtherInsPostHospSettlementDetailsDTO>(OtherInsPostHospSettlementDetailsDTO.class);
		this.binder.setItemDataSource(bean.getOtherInsPostHospSettlementCalcDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(PreauthDTO bean) 
	{	
		
		this.bean = bean;
		initBinder();
		payableAmtChangeListenerField = new TextField();
		totalClaimedAmount = (TextField) binder.buildAndBind("Total Claimed Amount\n for Post Hospitalisation",
				"totalClaimedAmt", TextField.class);
		totalClaimedAmount.setWidth("60px");
		totalClaimedAmount.setNullRepresentation("0");
		
		CSValidator remarksValidator = new CSValidator();
		remarksValidator.extend(totalClaimedAmount);
		remarksValidator.setRegExp("^[0-9]*$");
		remarksValidator.setPreventInvalidTyping(true);
//		totalClaimedAmount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		netPayableAmtTxt.setValue(this.bean.getHospitalisationValue());
		
		
		nonMedicalAmt = (TextField) binder.buildAndBind("Less: Non Medical (inadmissible) Amt",
				"nonMedicalAmt", TextField.class);
		nonMedicalAmt.setWidth("60px");
		nonMedicalAmt.setNullRepresentation("0");
		
		CSValidator nonMedicalValidator = new CSValidator();
		nonMedicalValidator.extend(nonMedicalAmt);
		nonMedicalValidator.setRegExp("^[0-9]*$");
		nonMedicalValidator.setPreventInvalidTyping(true);
		
		netClaimedAmt = (TextField) binder.buildAndBind("A. Net Claimed Amount \n- Post Hospitalisation",
				"netClaimedAmt", TextField.class);
		netClaimedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		netClaimedAmt.setValidationVisible(false);
//		preauthAppAmtTxt.setValue(preauthApprAmt);
		netClaimedAmt.setReadOnly(true);
		netClaimedAmt.setNullRepresentation("0");
		
		tpaSettledAmt = (TextField) binder.buildAndBind("B. TPA Settled Amount \nfor Post Hospitalisation",
				"tpaSettledAmt", TextField.class);
		tpaSettledAmt.setWidth("60px");
		tpaSettledAmt.setNullRepresentation("0");
		
		CSValidator tpaSettledValidator = new CSValidator();
		tpaSettledValidator.extend(tpaSettledAmt);
		tpaSettledValidator.setRegExp("^[0-9]*$");
		tpaSettledValidator.setPreventInvalidTyping(true);
		
		balanceAmt = (TextField) binder.buildAndBind("C. Balance Amount (A-B)",
				"balanceAmt", TextField.class);
		balanceAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		balanceAmt.setNullRepresentation("0");
		balanceAmt.setReadOnly(true);
		
		hospPayableAmt = (TextField) binder.buildAndBind("D. Hospital Claim Payable Amount",
				"hospPayableAmt", TextField.class);
		hospPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospPayableAmt.setNullRepresentation("0");
		hospPayableAmt.setReadOnly(true);
		
		payableToIns = (TextField) binder.buildAndBind("E. Amount Payable To Insured - \nPost Hospitalisation (min of C and D)",
				"payableToIns", TextField.class);
		payableToIns.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableToIns.setNullRepresentation("0");
		payableToIns.setReadOnly(true);
		
		alreadyPaidAmt = (TextField) binder.buildAndBind("F. Amount Already Paid",
				"amountAlreadyPaid", TextField.class);
		alreadyPaidAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		alreadyPaidAmt.setNullRepresentation("0");
		alreadyPaidAmt.setReadOnly(true);
		
		payableAmt = (TextField) binder.buildAndBind("G. Amount Payable",
				"payableAmt", TextField.class);
		payableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableAmt.setNullRepresentation("0");
		payableAmt.setReadOnly(true);
		
		FormLayout formLayout = new FormLayout(totalClaimedAmount, nonMedicalAmt, netClaimedAmt, tpaSettledAmt, balanceAmt, hospPayableAmt,payableToIns, alreadyPaidAmt, payableAmt );
		formLayout.setCaption("Post Hospitalization Settlement Calculation");
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		HorizontalLayout layout = new HorizontalLayout(formLayout);
		layout.setCaption("Post Hospitalization Settlement Calculation");
		setCompositionRoot(layout);
		addListener();
		isValid();
	}
	
	private void setNetClaimedAmount() {
		Integer calcAmt = SHAUtils.getIntegerFromStringWithComma(totalClaimedAmount.getValue()) -  SHAUtils.getIntegerFromStringWithComma(nonMedicalAmt.getValue());
		 if(calcAmt < 0){
			 calcAmt = 0;
		 }
			
		netClaimedAmt.setReadOnly(false);
		netClaimedAmt.setValue(calcAmt.toString());
		netClaimedAmt.setReadOnly(true);
		
		setBalanceAmount();
	}
	
	private void setBalanceAmount() {
		Integer calcAmt = SHAUtils.getIntegerFromStringWithComma(netClaimedAmt.getValue()) -  SHAUtils.getIntegerFromStringWithComma(tpaSettledAmt.getValue());
		 if(calcAmt < 0){
			 calcAmt = 0;
		 }
			
		balanceAmt.setReadOnly(false);
		balanceAmt.setValue(calcAmt.toString());
		balanceAmt.setReadOnly(true);
		
		setPayableToInsAmt();
	}
	
	private void setPayableToInsAmt() {
		Integer calcAmt = Math.min(SHAUtils.getIntegerFromStringWithComma(balanceAmt.getValue()), SHAUtils.getIntegerFromStringWithComma(hospPayableAmt.getValue()));
		 if(calcAmt < 0){
			 calcAmt = 0;
		 }
			
		payableToIns.setReadOnly(false);
		payableToIns.setValue(calcAmt.toString());
		payableToIns.setReadOnly(true);
		
		setPayableAmt();
	}
	
	private void setPayableAmt() {
		Integer calcAmt = SHAUtils.getIntegerFromStringWithComma(payableToIns.getValue()) -  SHAUtils.getIntegerFromStringWithComma(alreadyPaidAmt.getValue());
		 if(calcAmt < 0){
			 calcAmt = 0;
		 }
			
		payableAmt.setReadOnly(false);
		payableAmt.setValue(calcAmt.toString());
		payableAmt.setReadOnly(true);
		
		payableAmtChangeListenerField.setValue(calcAmt.toString());
	}
	
	public void addListener(){
		totalClaimedAmount.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				setNetClaimedAmount();
			}

			
		});
		
		nonMedicalAmt.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				setNetClaimedAmount();
			}

			
		});
		
		tpaSettledAmt.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				setBalanceAmount();
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
	public String getPayableAmount() {
		return payableAmt.getValue();
	}
	
}
