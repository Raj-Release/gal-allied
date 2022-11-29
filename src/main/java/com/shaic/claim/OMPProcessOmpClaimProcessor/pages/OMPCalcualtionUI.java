package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPCalcualtionUI extends ViewComponent {

	private static final long serialVersionUID = -4222456099408568886L;
	
	private BeanFieldGroup<OMPClaimProcessorCalculationSheetDTO> binder;
	
	private OMPClaimProcessorDTO bean;
	
	private TextField totalAmtTxt;
	
	private TextField deductiblePerPolicyAmtTxt;
	
	private TextField coPayApprovedAmtTxt;
	
	private TextField negotiateAgreedAmtTxt;
	
	private TextField txtBalanceSumInured;

	private TextField totalAmtPayableTxt;

	private TextField alreadyPaidAmtTxt;

	public TextField payableAmtAmtTxt;

	private TextField inrAmtAmtTxt;

	private ComboBox cmbCoPayPercentage;

	private DateField dateOfRecovery;

	private TextField amtRecoveredINRTxt;

	private TextField amtRecoveredDollarTxt;

	private TextArea RemarksTxt;
	
	private TextField txtCoAmt;
	
	public TextField dummyField;
	
	public TextField dummyPayableAmt;
	
	public TextField dummyFieldHospital;
	
	OptionGroup  optionGroup = null;

	public TextField dummyCurrency;
	
	public TextField classification;
	
	public TextField dummyConversionRate;
	
	public TextField dummyPayableAmtNego;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<OMPClaimProcessorCalculationSheetDTO>(OMPClaimProcessorCalculationSheetDTO.class);
		this.binder.setItemDataSource(bean.getCalculationSheetDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(OMPClaimProcessorDTO bean) 
	{	
		
		String preauthApprAmt = null;
		
		this.bean = bean;
		initBinder();
		dummyField = new TextField();
		dummyFieldHospital = new TextField();
		dummyPayableAmt = new TextField();
		dummyCurrency = new TextField();
		dummyConversionRate = new TextField();
		dummyPayableAmtNego = new TextField();
		classification = new TextField();
		totalAmtTxt = (TextField) binder.buildAndBind("Total Amount","amttotal", TextField.class);
		totalAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		totalAmtTxt.setValue(this.bean.getHospitalisationValue());
		totalAmtTxt.setReadOnly(true);
		
		addListenerForTotal();
		deductiblePerPolicyAmtTxt = (TextField) binder.buildAndBind("Deductible as per Policy  ($)","deductiblePerPolicy", TextField.class);
		deductiblePerPolicyAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		if(bean.getDeductibles()!=null){
			deductiblePerPolicyAmtTxt.setValue(bean.getDeductibles().toString());
		}
		deductiblePerPolicyAmtTxt.setNullRepresentation("");
		//deductiblePerPolicyAmtTxt.setReadOnly(true);
		
		cmbCoPayPercentage = (ComboBox) binder.buildAndBind("Co-pay","coPayPercentage", ComboBox.class);
		cmbCoPayPercentage.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cmbCoPayPercentage.setWidth("50%");
		
		if(bean.getProductCode()!=null ){
			String productCode = bean.getProductCode();
			if(productCode.equalsIgnoreCase("OMP-PRD-001")){
				BeanItemContainer<SelectValue> data = new BeanItemContainer<SelectValue>(SelectValue.class);
				
				SelectValue selectValue = new SelectValue();
				selectValue.setId(1L);
				selectValue.setValue("0");
				SelectValue selectValue2 = new SelectValue();
				selectValue2.setId(2L);
				selectValue2.setValue("30");
				
				data.addBean(selectValue);
				data.addBean(selectValue2);
				cmbCoPayPercentage.setContainerDataSource(data);
				if(bean!=null && bean.getCalculationSheetDTO()!=null && bean.getCalculationSheetDTO().getCoPayPercentage()!=null
						&& bean.getCalculationSheetDTO().getCoPayPercentage().getValue()!=null){
					cmbCoPayPercentage.setValue(bean.getCalculationSheetDTO().getCoPayPercentage());
				}
				cmbCoPayPercentage.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbCoPayPercentage.setItemCaptionPropertyId("value");
			}else{
				cmbCoPayPercentage.setEnabled(false);
			}
		}
		
		txtCoAmt = (TextField) binder.buildAndBind("Co-pay Amount","coPayAmount", TextField.class);
		txtCoAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		coPayApprovedAmtTxt = (TextField) binder.buildAndBind("Approved amount after co-pay","coPayApprovedAmt", TextField.class);
		coPayApprovedAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		coPayApprovedAmtTxt.setValidationVisible(false);
//		coPayApprovedAmtTxt.setValue(preauthApprAmt);
		coPayApprovedAmtTxt.setReadOnly(true);
		coPayApprovedAmtTxt.setNullRepresentation("0");
		
		
		negotiateAgreedAmtTxt = (TextField) binder.buildAndBind("Agreed Amount ($) after Negotiation","negotiateAgreedAmt", TextField.class);
		negotiateAgreedAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		negotiateAgreedAmtTxt.setNullRepresentation("");
		negotiateAgreedAmtTxt.setReadOnly(true);
		
		
		txtBalanceSumInured = (TextField) binder.buildAndBind("BALANCE SUM INSURED","balanceSumInured", TextField.class);
		txtBalanceSumInured.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtBalanceSumInured.setReadOnly(true);
		
		totalAmtPayableTxt = (TextField) binder.buildAndBind("Total Amount Payable ($)","totalAmtPayable", TextField.class);
		totalAmtPayableTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		totalAmtPayableTxt.setNullRepresentation("");
		totalAmtPayableTxt.setReadOnly(true);
		
		alreadyPaidAmtTxt = (TextField) binder.buildAndBind("Amount Already paid ($)","alreadyPaidAmt", TextField.class);
		alreadyPaidAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		alreadyPaidAmtTxt.setReadOnly(true);
		
		payableAmtAmtTxt = (TextField) binder.buildAndBind("Payable amount ($)","payableAmt", TextField.class);
		payableAmtAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payableAmtAmtTxt.setReadOnly(true);
		
		inrAmtAmtTxt = (TextField) binder.buildAndBind("Payable amount (INR)","inrAmt", TextField.class);
		inrAmtAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		inrAmtAmtTxt.setReadOnly(true);

//		FormLayout formLayout = new FormLayout(totalAmtTxt, deductiblePerPolicyAmtTxt, coPayApprovedAmtTxt, negotiateAgreedAmtTxt, payableToInsAmtTxt, tdsAmtTxt,payableToHospitalAftTDSTxt, balancePremiumTxt, payableToInsAftPremiumTxt );
		
		FormLayout formLayout = new FormLayout(totalAmtTxt, deductiblePerPolicyAmtTxt,cmbCoPayPercentage,txtCoAmt,coPayApprovedAmtTxt,negotiateAgreedAmtTxt,
				txtBalanceSumInured, totalAmtPayableTxt, alreadyPaidAmtTxt, payableAmtAmtTxt, inrAmtAmtTxt);
		formLayout.addStyleName("borderLayout");
		formLayout.setCaption("");
		optionGroup = new OptionGroup ();
		optionGroup.setNullSelectionAllowed(true);
		optionGroup.addItems("Yes","No");
		optionGroup.setStyleName("horizontal");
//		if((bean.getCalculationSheetDTO() != null &&bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured()!= null && bean.getCalculationSheetDTO()!=null &&bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured().equalsIgnoreCase("Y"))&&
//				(bean.getCalculationSheetDTO()!=null && bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured()!= null && bean.getCalculationSheetDTO()!=null && bean.getCalculationSheetDTO().getDeductiblerecoveredfrominsured().equalsIgnoreCase("N"))) {
//			optionGroup.setValue(true);
//		} 
//		else{
//			optionGroup.setValue(false);
//		}
		HorizontalLayout layout1 = new HorizontalLayout(optionGroup);
		layout1.setCaption("Deductible recovered from insured ");
		dateOfRecovery = (DateField) binder.buildAndBind("Date of Recovery","dateOfRecovery", DateField.class);
		amtRecoveredINRTxt = (TextField) binder.buildAndBind("Amount Recovered(INR)","amtRecoveredINR", TextField.class);
		amtRecoveredDollarTxt = (TextField) binder.buildAndBind("Amount Recovered($)","amtRecoveredDollar", TextField.class);
		RemarksTxt = (TextArea) binder.buildAndBind("Remarks","remarks", TextArea.class);
		RemarksTxt.setWidth("25%");
		
		if(String.valueOf(optionGroup.getValue()).toString().equalsIgnoreCase("Yes")){
			this.bean.getCalculationSheetDTO().setDeductiblerecoveredfrominsured("Y");
		}else{
			if(String.valueOf(optionGroup.getValue()).toString().equalsIgnoreCase("No")){
			this.bean.getCalculationSheetDTO().setDeductiblerecoveredfrominsured("N");
			}
		}
		FormLayout formLayout1 = new FormLayout(layout1, dateOfRecovery, amtRecoveredINRTxt, amtRecoveredDollarTxt, RemarksTxt);
		
		HorizontalLayout layout = new HorizontalLayout(formLayout, formLayout1);
		layout.setSpacing(true);
		layout.setCaption("");
		
		layout.setCaption("REFER CALCULATION SHEET FOR CALCULATIONS");
		if(optionGroup!=null && bean!=null && bean.getDocumentsReceivedFrom()!=null){
			SelectValue documentsReceivedFrom = bean.getDocumentsReceivedFrom();
			if(documentsReceivedFrom!=null){
				if(documentsReceivedFrom.getId()!= null && !documentsReceivedFrom.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					optionGroup.setEnabled(false);
					dateOfRecovery.setValue(null);
					dateOfRecovery.setEnabled(Boolean.FALSE);
					amtRecoveredINRTxt.setValue("0");
					amtRecoveredINRTxt.setEnabled(Boolean.FALSE);
					amtRecoveredDollarTxt.setValue("0");
					amtRecoveredDollarTxt.setEnabled(Boolean.FALSE);
					RemarksTxt.setValue(null);
					RemarksTxt.setEnabled(Boolean.FALSE);
				}else{
					optionGroup.setEnabled(true);
					dateOfRecovery.setEnabled(Boolean.TRUE);
					amtRecoveredINRTxt.setEnabled(Boolean.TRUE);
					amtRecoveredDollarTxt.setEnabled(Boolean.TRUE);
					RemarksTxt.setEnabled(Boolean.TRUE);
				}
			}
		}
		addListenerForCopay();
		addListenerForHospital();
		addListenerForCurrency();
		addListenerForpayable();
		addListenerForConversion();
		addListenerForpayableNego();
		addListenerForOption();
		addListenerForDeductibles();
		setCompositionRoot(layout);
		//isValid();
	}

	
	
	
	public void isValid(){
		try {
			binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*private void addListenerForOption()
	{
	 if(optionGroup!=null){
		 optionGroup.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					OptionGroup totalValue = (OptionGroup) event.getProperty();
					if(totalValue.getValue()!=null && totalValue.getValue().equals("Yes")){
							if(optionGroup!=null){
								optionGroup.setValue(true);
							}
					}
					if(totalValue.getValue()!=null && totalValue.getValue().equals("No")){
						if(optionGroup!=null){
							optionGroup.setValue(false);
						}
				}
					
				}
			});
		  }
	}*/
	
	private void addListenerForTotal()
	{
		if(dummyField != null) {
			dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					totalAmtTxt.setReadOnly(Boolean.FALSE);
					totalAmtTxt.setValue(totalValue);
					totalAmtTxt.setReadOnly(Boolean.TRUE);
					if(!totalValue.equalsIgnoreCase("0")){
						if(txtCoAmt!=null && txtCoAmt.getValue()!=null){
							SelectValue value = (SelectValue) cmbCoPayPercentage.getValue();
							if(value!=null && value.getValue()!=null){
								String value2 = value.getValue();
								cmbCoPayPercentage.setValue(null);
								cmbCoPayPercentage.setValue(value);
							}else{
								doCalculation(totalValue);
							}
						}else{
							doCalculation(totalValue);
						}
						//doCalculation(totalValue);
					}
				}

				
			});
		}
	}
	
	private void addListenerForpayable()
	{
		if(dummyPayableAmt != null) {
			dummyPayableAmt.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(classification!=null && classification.getValue()!= null && !classification.getValue().equalsIgnoreCase("OMP Claim Related")){
						inrAmtAmtTxt.setReadOnly(Boolean.FALSE);
						inrAmtAmtTxt.setValue(totalValue);
						inrAmtAmtTxt.setReadOnly(Boolean.TRUE);
						
						totalAmtPayableTxt.setReadOnly(Boolean.FALSE);
						totalAmtPayableTxt.setValue(totalValue);
						totalAmtPayableTxt.setReadOnly(Boolean.TRUE);
						
						txtBalanceSumInured.setReadOnly(Boolean.FALSE);
						txtBalanceSumInured.setValue("0");
						txtBalanceSumInured.setReadOnly(Boolean.TRUE);
					}
					/*totalAmtPayableTxt.setReadOnly(Boolean.FALSE);
					totalAmtPayableTxt.setValue(totalValue);
					totalAmtPayableTxt.setReadOnly(Boolean.TRUE);*/
					
					/*inrAmtAmtTxt.setReadOnly(Boolean.FALSE);
					inrAmtAmtTxt.setValue(totalValue);
					inrAmtAmtTxt.setReadOnly(Boolean.TRUE);*/
					
					/*txtBalanceSumInured.setReadOnly(Boolean.FALSE);
					txtBalanceSumInured.setValue("0");
					txtBalanceSumInured.setReadOnly(Boolean.TRUE);*/
				}

				
			});
		}
	}
	
	private void addListenerForpayableNego()
	{
		if(dummyPayableAmtNego != null) {
			dummyPayableAmtNego.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(classification!=null && classification.getValue()!= null && !classification.getValue().equalsIgnoreCase("OMP Claim Related")){
					
						
						//totalAmtPayableTxt.setReadOnly(Boolean.FALSE);
						//totalAmtPayableTxt.setValue(totalValue);
						//totalAmtPayableTxt.setReadOnly(Boolean.TRUE);
						
						payableAmtAmtTxt.setReadOnly(Boolean.FALSE);
						payableAmtAmtTxt.setValue(totalValue);
						payableAmtAmtTxt.setReadOnly(Boolean.TRUE);
						
						txtBalanceSumInured.setReadOnly(Boolean.FALSE);
						txtBalanceSumInured.setValue("0");
						txtBalanceSumInured.setReadOnly(Boolean.TRUE);
					}
					/*totalAmtPayableTxt.setReadOnly(Boolean.FALSE);
					totalAmtPayableTxt.setValue(totalValue);
					totalAmtPayableTxt.setReadOnly(Boolean.TRUE);*/
					
					/*inrAmtAmtTxt.setReadOnly(Boolean.FALSE);
					inrAmtAmtTxt.setValue(totalValue);
					inrAmtAmtTxt.setReadOnly(Boolean.TRUE);*/
					
					/*txtBalanceSumInured.setReadOnly(Boolean.FALSE);
					txtBalanceSumInured.setValue("0");
					txtBalanceSumInured.setReadOnly(Boolean.TRUE);*/
				}

				
			});
		}
	}
	
	private void addListenerForHospital()
	{
		if(dummyFieldHospital != null) {
			dummyFieldHospital.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue.equalsIgnoreCase(ReferenceTable.RECEIVED_FROM_HOSPITAL.toString())){
						optionGroup.setEnabled(Boolean.TRUE);
					}else{
						optionGroup.setValue(null);
						optionGroup.setEnabled(Boolean.FALSE);
						dateOfRecovery.setValue(null);
						dateOfRecovery.setEnabled(Boolean.FALSE);
						amtRecoveredINRTxt.setValue("0");
						amtRecoveredINRTxt.setEnabled(Boolean.FALSE);
						amtRecoveredDollarTxt.setValue("0");
						amtRecoveredDollarTxt.setEnabled(Boolean.FALSE);
						RemarksTxt.setValue(null);
						RemarksTxt.setEnabled(Boolean.FALSE);
					
					}
					
				}
			});
		}
	}
	
	private void addListenerForCurrency()
	{
		if(dummyCurrency != null) {
			dummyCurrency.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					Double currency =0d;
					Double payableAmt =0d;
					Double totalPAyable =0d;
					if(totalValue!=null){
						currency = SHAUtils.getDoubleFromStringWithComma(totalValue);
						if(deductiblePerPolicyAmtTxt!=null ){
							if(bean.getDeductibles()!=null){
								deductiblePerPolicyAmtTxt.setValue(bean.getDeductibles().toString());
							}
						}
						
						if(payableAmtAmtTxt.getValue()!=null){
							payableAmt = SHAUtils.getDoubleFromStringWithComma(payableAmtAmtTxt.getValue());
							totalPAyable = payableAmt * currency;
							/*inrAmtAmtTxt.setReadOnly(false);
							inrAmtAmtTxt.setValue(totalPAyable.toString());
							inrAmtAmtTxt.setReadOnly(true);*/
						}
					}
					
				}
			});
		}
	}
	
	private void addListenerForOption()
	{
		if(optionGroup != null) {
			optionGroup.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue!=null){
					if(totalValue.equalsIgnoreCase("Yes")){
						bean.getCalculationSheetDTO().setDeductiblerecoveredfrominsured("Y");
						
						dateOfRecovery.setValue(null);
						dateOfRecovery.setEnabled(Boolean.TRUE);
						amtRecoveredINRTxt.setValue("0");
						amtRecoveredINRTxt.setEnabled(Boolean.TRUE);
						amtRecoveredDollarTxt.setValue("0");
						amtRecoveredDollarTxt.setEnabled(Boolean.TRUE);
						RemarksTxt.setValue(null);
						RemarksTxt.setEnabled(Boolean.TRUE);
					}else{
						bean.getCalculationSheetDTO().setDeductiblerecoveredfrominsured("N");
						dateOfRecovery.setValue(null);
						dateOfRecovery.setEnabled(Boolean.FALSE);
						amtRecoveredINRTxt.setValue("0");
						amtRecoveredINRTxt.setEnabled(Boolean.FALSE);
						amtRecoveredDollarTxt.setValue("0");
						amtRecoveredDollarTxt.setEnabled(Boolean.FALSE);
						RemarksTxt.setValue(null);
						RemarksTxt.setEnabled(Boolean.FALSE);
					}}
					
				}
			});
		}
	}
	
	private void addListenerForConversion()
	{
		if(dummyConversionRate != null) {
			dummyConversionRate.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					Double currency =0d;
					Double payableAmt =0d;
					Double totalPAyable =0d;
					if(totalValue!=null){
						currency = SHAUtils.getDoubleFromStringWithComma(totalValue);
						if(payableAmtAmtTxt.getValue()!=null){
							payableAmt = SHAUtils.getDoubleFromStringWithComma(payableAmtAmtTxt.getValue());
							totalPAyable = payableAmt * currency;
							inrAmtAmtTxt.setReadOnly(false);
							inrAmtAmtTxt.setValue(totalPAyable.toString());
							inrAmtAmtTxt.setReadOnly(true);
						}
						if(classification!=null && classification.getValue()!= null){
							if(classification.getValue().equalsIgnoreCase("OMP Claim Related")){
								if(deductiblePerPolicyAmtTxt!=null && deductiblePerPolicyAmtTxt.getValue()==null){
									deductiblePerPolicyAmtTxt.setValue(bean.getDeductibles().toString());
									deductiblePerPolicyAmtTxt.setEnabled(true);
									if(bean.getProductCode()!=null ){
										String productCode = bean.getProductCode();
										if(productCode.equalsIgnoreCase("OMP-PRD-001")){
											cmbCoPayPercentage.setEnabled(true);
										}
									}
								}
							}else if(classification.getValue().equalsIgnoreCase("Other Exp")){
								deductiblePerPolicyAmtTxt.setValue(null);
								deductiblePerPolicyAmtTxt.setEnabled(false);
								cmbCoPayPercentage.setEnabled(false);
							}else{
								if(deductiblePerPolicyAmtTxt!=null){
									deductiblePerPolicyAmtTxt.setValue(null);
									deductiblePerPolicyAmtTxt.setEnabled(false);
									cmbCoPayPercentage.setEnabled(false);
									negotiateAgreedAmtTxt.setReadOnly(false);
									negotiateAgreedAmtTxt.setValue(null);
									negotiateAgreedAmtTxt.setEnabled(false);
									totalAmtPayableTxt.setReadOnly(false);
									totalAmtPayableTxt.setValue(null);
									totalAmtPayableTxt.setEnabled(false);
									
								}
							}
						}
					}
					
				}
			});
		}
	}
	
	private void addListenerForCopay()
	{
		if(cmbCoPayPercentage != null) {
			cmbCoPayPercentage.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue coPaypercentageSelectvalue = (SelectValue) event.getProperty().getValue();
					if(coPaypercentageSelectvalue==null ){
						Double totalAmtDouble = 0d;
						if(totalAmtTxt!= null && totalAmtTxt.getValue()!=null){
							txtCoAmt.setReadOnly(Boolean.FALSE);
							txtCoAmt.setValue("0");
							txtCoAmt.setReadOnly(Boolean.TRUE);
						coPayApprovedAmtTxt.setReadOnly(Boolean.FALSE);
						if(deductiblePerPolicyAmtTxt!=null && deductiblePerPolicyAmtTxt.getValue()!=null){
							Double deductibles = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
							totalAmtDouble = SHAUtils.getDoubleFromStringWithComma(totalAmtTxt.getValue());
							totalAmtDouble = totalAmtDouble - deductibles;
							coPayApprovedAmtTxt.setValue(totalAmtDouble.toString());
							coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}else{
							coPayApprovedAmtTxt.setValue(totalAmtTxt.getValue());
							coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}
						coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}
						if(totalAmtDouble!=0d){
							doCalculation(totalAmtTxt.getValue().toString());
						}
					}else if(coPaypercentageSelectvalue.getValue()!=null && coPaypercentageSelectvalue.getValue().equalsIgnoreCase("0")){

						Double totalAmtDouble = 0d;
						if(totalAmtTxt!= null && totalAmtTxt.getValue()!=null){
							txtCoAmt.setReadOnly(Boolean.FALSE);
							txtCoAmt.setValue("0");
							txtCoAmt.setReadOnly(Boolean.TRUE);
						coPayApprovedAmtTxt.setReadOnly(Boolean.FALSE);
						if(deductiblePerPolicyAmtTxt!=null && deductiblePerPolicyAmtTxt.getValue()!=null){
							Double deductibles = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
							totalAmtDouble = SHAUtils.getDoubleFromStringWithComma(totalAmtTxt.getValue());
							totalAmtDouble = totalAmtDouble - deductibles;
							coPayApprovedAmtTxt.setValue(totalAmtDouble.toString());
							coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}else{
							coPayApprovedAmtTxt.setValue(totalAmtTxt.getValue());
							coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}
						coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						}
						if(totalAmtDouble!=0d){
							doCalculation(totalAmtTxt.getValue().toString());
						}
					}
					else{
					String coPaypercentage = coPaypercentageSelectvalue.getValue();
					Double coPaypercentageDouble =0d;
					Double totalAmtDouble =0d;
					Double coPayAmountDouble =0d;
					Double approvedAmtAfterCopay =0d;
					Double deductibles =0d;
					if(coPaypercentage!=null){
						coPaypercentageDouble = SHAUtils.getDoubleFromStringWithComma(coPaypercentage);
						
					}
					if(totalAmtTxt.getValue()!=null){
						totalAmtDouble = SHAUtils.getDoubleFromStringWithComma(totalAmtTxt.getValue());
						if(deductiblePerPolicyAmtTxt.getValue()!=null){
							deductibles = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
						}
						totalAmtDouble = totalAmtDouble - deductibles;
					}
					
					coPayAmountDouble =(totalAmtDouble *coPaypercentageDouble)/100;
					txtCoAmt.setReadOnly(Boolean.FALSE);
					txtCoAmt.setValue(coPayAmountDouble.toString());
					txtCoAmt.setReadOnly(Boolean.TRUE);
					if(totalAmtDouble!=0d){
						doCalculation(totalAmtTxt.getValue().toString());
					}
				}}
			});
		}else{
			if(totalAmtTxt!= null && totalAmtTxt.getValue()!=null){
				txtCoAmt.setReadOnly(Boolean.FALSE);
				txtCoAmt.setValue("0");
				txtCoAmt.setReadOnly(Boolean.TRUE);
				coPayApprovedAmtTxt.setReadOnly(Boolean.FALSE);
			if(deductiblePerPolicyAmtTxt!=null && deductiblePerPolicyAmtTxt.getValue()!=null){
				Double deductibles = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
				Double totalAmtDouble = SHAUtils.getDoubleFromStringWithComma(totalAmtTxt.getValue());
				totalAmtDouble = totalAmtDouble - deductibles;
				coPayApprovedAmtTxt.setValue(totalAmtDouble.toString());
				coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
			}else{
				coPayApprovedAmtTxt.setValue(totalAmtTxt.getValue());
				coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
			}
			
			}
		}
	}
	
	
	private void addListenerForDeductibles()
	{
		if(deductiblePerPolicyAmtTxt != null) {
			deductiblePerPolicyAmtTxt.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String dedutibles = (String) event.getProperty().getValue();
					if(dedutibles!=null && coPayApprovedAmtTxt!=null && totalAmtTxt!=null){
						//doCalculation(totalAmtTxt.getValue());
						
						coPayApprovedAmtTxt.setReadOnly(Boolean.FALSE);
						Double dedDouble =0d;
						Double total =0d;
						Double copayAmt =0d;
						dedDouble = SHAUtils.getDoubleFromStringWithComma(dedutibles);
						total = SHAUtils.getDoubleFromStringWithComma(totalAmtTxt.getValue());
						if(txtCoAmt!=null && txtCoAmt.getValue()!=null){
						copayAmt = SHAUtils.getDoubleFromStringWithComma(txtCoAmt.getValue());
						}
						if(total!=0d){
							total = total - dedDouble - copayAmt;
						}
						coPayApprovedAmtTxt.setValue(total.toString());
						coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
						doCalculation(totalAmtTxt.getValue());
					}}
			});
		}
	}
	
	
	
	private void doCalculation(String totalValue) {
		Double balanceSI = bean.getBalanceSI();
		Double negotiationAgreedDouble =0d;
		Double alreadyPaidAmtDouble =0d;
		Double payableAmtDouble =0d;
		Double totalPayableAmtDouble =0d;
		Double copayAmt =0d;
		
		/*if(deductiblePerPolicyAmtTxt!=null && bean!=null && bean.getDeductibles()!=null){
			Double dedDouble = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
			if(dedDouble!=bean.getDeductibles()){
				deductiblePerPolicyAmtTxt.setValue(bean.getDeductibles().toString());
			}
		}*/
		
		if(cmbCoPayPercentage!= null && cmbCoPayPercentage.getValue()!=null && cmbCoPayPercentage.getValue().equals("0")){/*
			SelectValue value = (SelectValue) cmbCoPayPercentage.getValue();
			if(value!=null && value.getValue()!=null){
				copayPrentage = SHAUtils.getDoubleFromStringWithComma(value.getValue());
				
			}else{
				
			}
			
		*/}else{
			
			coPayApprovedAmtTxt.setReadOnly(Boolean.FALSE);
			Double dedDouble =0d;
			Double total =0d;
			if(deductiblePerPolicyAmtTxt!=null && deductiblePerPolicyAmtTxt.getValue()!=null){
				dedDouble = SHAUtils.getDoubleFromStringWithComma(deductiblePerPolicyAmtTxt.getValue());
			}
			if(txtCoAmt!=null && txtCoAmt.getValue()!=null){
				copayAmt = SHAUtils.getDoubleFromStringWithComma(txtCoAmt.getValue());
			}
			total = SHAUtils.getDoubleFromStringWithComma(totalValue);
			if(total!=0d){
				total = total - dedDouble -copayAmt;
			}
			coPayApprovedAmtTxt.setValue(total.toString());
			coPayApprovedAmtTxt.setReadOnly(Boolean.TRUE);
		}
		
		if(bean.getBalanceSI()!=null){
			txtBalanceSumInured.setReadOnly(false);
			txtBalanceSumInured.setValue(bean.getBalanceSI().toString());
			txtBalanceSumInured.setReadOnly(true);
		}else{
			balanceSI =0d;
		}
		
		if(negotiateAgreedAmtTxt!=null & negotiateAgreedAmtTxt.getValue()!=null && bean!=null && bean.getAgreedAmt()!=null){
			negotiateAgreedAmtTxt.setReadOnly(false);
			negotiateAgreedAmtTxt.setValue(bean.getAgreedAmt().toString());
			negotiateAgreedAmtTxt.setReadOnly(true);
		}
		
		if(negotiateAgreedAmtTxt.getValue()!=null){
			negotiationAgreedDouble = SHAUtils.getDoubleFromStringWithComma(negotiateAgreedAmtTxt.getValue());
			if(negotiationAgreedDouble!=0d){
				totalAmtPayableTxt.setReadOnly(false);
				totalPayableAmtDouble = Math.min(negotiationAgreedDouble, balanceSI);
				totalAmtPayableTxt.setValue(totalPayableAmtDouble.toString());
				totalAmtPayableTxt.setReadOnly(true);
			}else{
				if(coPayApprovedAmtTxt!= null && coPayApprovedAmtTxt.getValue()!=null){
					negotiationAgreedDouble = SHAUtils.getDoubleFromStringWithComma(coPayApprovedAmtTxt.getValue());
				}
				totalPayableAmtDouble = Math.min(negotiationAgreedDouble, balanceSI);
				if(totalAmtPayableTxt!=null){
						totalAmtPayableTxt.setReadOnly(false);
						totalAmtPayableTxt.setValue(totalPayableAmtDouble.toString());
						totalAmtPayableTxt.setReadOnly(true);
				}
			}
		}else{
			if(coPayApprovedAmtTxt!= null && coPayApprovedAmtTxt.getValue()!=null){
				negotiationAgreedDouble = SHAUtils.getDoubleFromStringWithComma(coPayApprovedAmtTxt.getValue());
			}
			totalPayableAmtDouble = Math.min(negotiationAgreedDouble, balanceSI);
			if(totalAmtPayableTxt!=null){
					totalAmtPayableTxt.setReadOnly(false);
					totalAmtPayableTxt.setValue(totalPayableAmtDouble.toString());
					totalAmtPayableTxt.setReadOnly(true);
			}
		}
		
		if(alreadyPaidAmtTxt.getValue()!=null){
			alreadyPaidAmtDouble = SHAUtils.getDoubleFromStringWithComma(alreadyPaidAmtTxt.getValue());
			payableAmtDouble = totalPayableAmtDouble- alreadyPaidAmtDouble;
		}else{
			payableAmtDouble = totalPayableAmtDouble;
		}
		if(payableAmtAmtTxt!=null){
			payableAmtAmtTxt.setReadOnly(false);
			payableAmtAmtTxt.setValue(payableAmtDouble.toString());
			payableAmtAmtTxt.setReadOnly(true);
		}
		if(inrAmtAmtTxt!=null){
			Double inrPayableAmtDouble =0d; 
			if(dummyConversionRate!=null &&dummyConversionRate.getValue()!=null && !dummyConversionRate.equals("")){
				Double converSionRate = SHAUtils.getDoubleFromStringWithComma(dummyConversionRate.getValue());
				inrPayableAmtDouble= converSionRate * payableAmtDouble;
				inrAmtAmtTxt.setReadOnly(false);
				inrAmtAmtTxt.setValue(inrPayableAmtDouble.toString());
				inrAmtAmtTxt.setReadOnly(true);
			}else{
				if(bean!=null && bean.getConversionValue()!=null){
					Double conversionValue = bean.getConversionValue();
					inrPayableAmtDouble= conversionValue * payableAmtDouble;
					inrAmtAmtTxt.setReadOnly(false);
					inrAmtAmtTxt.setValue(inrPayableAmtDouble.toString());
					inrAmtAmtTxt.setReadOnly(true);
				}
			}
		}
	}
}
