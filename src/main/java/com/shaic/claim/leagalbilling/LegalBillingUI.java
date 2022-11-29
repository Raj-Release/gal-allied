package com.shaic.claim.leagalbilling;

import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.claim.coinsurance.view.CoInsuranceTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview.PAClaimAprNonHosReviewPagePresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview.PANonHospFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp.PAHealthBillingHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization.PAHealthFinancialHospitalizationPagePresenter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class LegalBillingUI extends ViewComponent{

	private static final long serialVersionUID = -6444854703226883601L;
	
	private BeanFieldGroup<LegalBillingDTO> legalBinder;
	
	private PreauthDTO bean;
	
	private TextField txtawardAmount;
	
	private TextField txtcost;
	
	private TextField txtcompensation;
	
	private TextField txtinterestRate;
	
	private TextField txttotalnoofdays;
	
	private TextField txtinterestCurrentClaim;
	
	private TextField txtinterestOtherClaim;
	
	private TextField txttotalInterestAmount;
	
	private PopupDateField fromDate;
	
	private PopupDateField toDate;
	
	private OptionGroup panDetails;
	
	private OptionGroup interestApplicable;
	
	private LegalBillingDTO legalBillingDTO;
	
	private VerticalLayout mainVLayout;
	
	private VerticalLayout legalBillingLayout;
	
	private HorizontalLayout legalHLayout;
	
	private HorizontalLayout interestHLayout;
	
	private FormLayout legalFormLayout;
	
	@Inject
	private Instance<LegalSettlementAmountTable> amountTableinstance;
	
	private LegalSettlementAmountTable amountTableObj;
	
	@EJB
	private PolicyService policyService;
	
	private Boolean isIntrestApplicable;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	private String presenterString;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@EJB
	private MasterService masterService;

	public void setLegalBillingDTO(LegalBillingDTO legalBillingDTO) {
		this.legalBillingDTO = legalBillingDTO;
	}

	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.legalBinder = new BeanFieldGroup<LegalBillingDTO>(LegalBillingDTO.class);
		this.legalBinder.setItemDataSource(legalBillingDTO);
		legalBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		isIntrestApplicable = policyService.isinterestApplicable(bean.getNewIntimationDTO().getIntimationId());
	}
	
	public void initView(PreauthDTO bean,String presenterString) 
	{	
		
		this.bean = bean;
		this.presenterString = presenterString;
		initBinder();
		txtawardAmount = (TextField)legalBinder.buildAndBind("Award Amount" , "awardAmount", TextField.class);
		txtawardAmount.setNullRepresentation("");	
		txtawardAmount.setEnabled(false);
		CSValidator validator = new CSValidator();
		validator.extend(txtawardAmount);
		validator.setRegExp("^[0-9]*$");	
		validator.setPreventInvalidTyping(true);
		txtawardAmount.addBlurListener(getawardAmountListener());
		
		txtcost = (TextField)legalBinder.buildAndBind("Cost" , "cost", TextField.class);
		txtcost.setNullRepresentation("");			
		CSValidator costvalidator = new CSValidator();
		costvalidator.extend(txtcost);
		costvalidator.setRegExp("^[0-9]*$");	
		costvalidator.setPreventInvalidTyping(true);
		txtcost.addBlurListener(getCostListener());
		mandatoryFields.add(txtcost);
		
		txtcompensation = (TextField)legalBinder.buildAndBind("Compensation" , "compensation", TextField.class);
		txtcompensation.setNullRepresentation("");			
		CSValidator compenvalidator = new CSValidator();
		compenvalidator.extend(txtcompensation);
		compenvalidator.setRegExp("^[0-9]*$");	
		compenvalidator.setPreventInvalidTyping(true);
		txtcompensation.addBlurListener(getCompensationListener());
		mandatoryFields.add(txtcompensation);
		
		interestApplicable = (OptionGroup) legalBinder.buildAndBind("Interest Applicable", "interestApplicable",OptionGroup.class);
		interestApplicable.setRequired(false);
		interestApplicable.addItems(getReadioButtonOptions());
		interestApplicable.setItemCaption(true, "Yes");
		interestApplicable.setItemCaption(false, "No");
		interestApplicable.setStyleName("horizontal");
		
		if(legalBillingDTO.getInterestApplicable() !=null
				&& legalBillingDTO.getInterestApplicable()){
			interestHLayout = buildinterestLayout();
			interestHLayout.setHeight("200px");
		}else{
			interestApplicable.setVisible(false);
			interestHLayout = new HorizontalLayout();
			interestHLayout.setVisible(false);
		}
		
		panDetails = (OptionGroup) legalBinder.buildAndBind("PAN Details available", "panDetails",OptionGroup.class);
		panDetails.setRequired(false);
		panDetails.addItems(getReadioButtonOptions());
		panDetails.setItemCaption(true, "Yes");
		panDetails.setItemCaption(false, "No");
		panDetails.setStyleName("horizontal");
		if(presenterString !=null && presenterString.equals(SHAConstants.PA_BILLING)){
			panDetails.setEnabled(false);
		}
		addPanListener();
		legalFormLayout = new FormLayout(txtawardAmount,txtcost,txtcompensation,interestApplicable);
		if(legalBillingDTO.getInterestApplicable() !=null
				&& legalBillingDTO.getInterestApplicable()){
			legalFormLayout.setHeight("145px");
		}else{
			legalFormLayout.setHeight("113px");
		}
		legalFormLayout.setSpacing(true);
		legalHLayout = new HorizontalLayout(legalFormLayout);
		
		FormLayout panFormLayout = new FormLayout(panDetails);
		
		if(legalBillingDTO.getInterestApplicable() !=null){
			isIntrestApplicable = legalBillingDTO.getInterestApplicable();
		}
		
		amountTableObj =   amountTableinstance.get();
		amountTableObj.initView(isIntrestApplicable,false);
		setTabelValues();
		showOrHideValidation(false);
		legalBillingLayout = new VerticalLayout();
		legalBillingLayout.addComponent(legalHLayout);
		legalBillingLayout.addComponent(interestHLayout);
		legalBillingLayout.addComponent(panFormLayout);
		legalBillingLayout.addComponent(amountTableObj);
		setCompositionRoot(legalBillingLayout);
	}
	
	private HorizontalLayout buildinterestLayout(){
		
		Label present = new Label("<div style='position: relative;right: 110px;'><div>%</div></div>", ContentMode.HTML);

		txtinterestRate = (TextField)legalBinder.buildAndBind("Interest Rate" , "interestRate", TextField.class);
		txtinterestRate.setNullRepresentation("");	
		CSValidator interestRatevalidator = new CSValidator();
		interestRatevalidator.extend(txtinterestRate);
		interestRatevalidator.setRegExp("^[0-9.]*$");	
		interestRatevalidator.setPreventInvalidTyping(true);
		txtinterestRate.addBlurListener(getInterestRateListener());
		mandatoryFields.add(txtinterestRate);
			
		fromDate = (PopupDateField)legalBinder.buildAndBind("Interest from Date" , "interestfromDate", PopupDateField.class);
		fromDate.setTabIndex(6);
		fromDate.setWidth("181px");
		fromDate.setHeight("-1px");
		fromDate.setTextFieldEnabled(false);
		fromDate.setDateFormat(("dd/MM/yyyy"));
		fromDate.setRangeEnd(new Date());
		mandatoryFields.add(fromDate);
		
		toDate = (PopupDateField)legalBinder.buildAndBind("Interest to Date" , "interesttoDate", PopupDateField.class);
		toDate.setTabIndex(6);
		toDate.setWidth("181px");
		toDate.setHeight("-1px");
		toDate.setTextFieldEnabled(false);
		toDate.setDateFormat(("dd/MM/yyyy"));
		mandatoryFields.add(toDate);
		
		txttotalnoofdays = (TextField)legalBinder.buildAndBind("Total no of days" , "totalnoofdays", TextField.class);
		txttotalnoofdays.setNullRepresentation("");	
		txttotalnoofdays.setEnabled(false);
		CSValidator noofdaysvalidator = new CSValidator();
		noofdaysvalidator.extend(txttotalnoofdays);
		noofdaysvalidator.setRegExp("^[0-9]*$");	
		noofdaysvalidator.setPreventInvalidTyping(true);
		
		
		txtinterestCurrentClaim = (TextField)legalBinder.buildAndBind("Interest Current Claim" , "interestCurrentClaim", TextField.class);
		txtinterestCurrentClaim.setNullRepresentation("");		
		txtinterestCurrentClaim.setEnabled(false);
		CSValidator currentClaimvalidator = new CSValidator();
		currentClaimvalidator.extend(txtinterestCurrentClaim);
		currentClaimvalidator.setRegExp("^[0-9]*$");	
		currentClaimvalidator.setPreventInvalidTyping(true);
		
		txtinterestOtherClaim = (TextField)legalBinder.buildAndBind("Interest Other Claim" , "interestOtherClaim", TextField.class);
		txtinterestOtherClaim.setNullRepresentation("");	
		txtinterestOtherClaim.setEnabled(false);
		CSValidator OtherClaimvalidator = new CSValidator();
		OtherClaimvalidator.extend(txtinterestOtherClaim);
		OtherClaimvalidator.setRegExp("^[0-9]*$");	
		OtherClaimvalidator.setPreventInvalidTyping(true);
		
		txttotalInterestAmount = (TextField)legalBinder.buildAndBind("Total Interest Amount" , "totalInterestAmount", TextField.class);
		txttotalInterestAmount.setNullRepresentation("");	
		txttotalInterestAmount.setEnabled(false);
		CSValidator totalInterestvalidator = new CSValidator();
		totalInterestvalidator.extend(txttotalInterestAmount);
		totalInterestvalidator.setRegExp("^[0-9]*$");	
		totalInterestvalidator.setPreventInvalidTyping(true);
		addListener();
		FormLayout form1 = new FormLayout(txtinterestRate,fromDate,txttotalnoofdays,txtinterestCurrentClaim,txtinterestOtherClaim,txttotalInterestAmount);
		FormLayout form2 = new FormLayout(present,toDate);
		HorizontalLayout interestLayout = new HorizontalLayout(form1,form2);
		interestLayout.setSpacing(true);
		
		return interestLayout;
	}
	
	private List<Field<?>> getListOfInterestFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(txtinterestRate);
		fieldList.add(fromDate);
		fieldList.add(toDate);
		fieldList.add(txttotalnoofdays);
		fieldList.add(txtinterestCurrentClaim);
		fieldList.add(txtinterestOtherClaim);
		fieldList.add(txttotalInterestAmount);
		return fieldList;
	}
	
	private void unbindField(List<Field<?>> fields) {
		if(null != fields && !fields.isEmpty())
		{
			for (Field<?> field : fields) {
				if (field != null ) {
					Object propertyId = this.legalBinder.getPropertyId(field);
					if (field!= null  && propertyId != null) {
						this.legalBinder.unbind(field);
					}
					field.setValue(null);
				}
			}
		}								
	}
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

	public void setawardAmount(String awardAmount){
		if(txtawardAmount !=null){
			txtawardAmount.setEnabled(true);
			txtawardAmount.setValue(awardAmount);
			txtawardAmount.setEnabled(false);
		}
		LegalBaseCell baseCell = new LegalBaseCell();
		baseCell.setLegalBillingAmount(Integer.parseInt(awardAmount));
		amountTableObj.setValuesForFields(baseCell,8);
		calculateInterestCurrentClaimAmt();
	}
	
	private void calcuAndsetNoOfDays(Date fromDate,Date toDate){
		try {  
			//Long diff = toDate.getTime() - fromDate.getTime();
		    Long noofdays = ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
		    noofdays += 1; 
		    if(txttotalnoofdays !=null){    	
		    	txttotalnoofdays.setEnabled(true);
		    	txttotalnoofdays.setValue(noofdays.toString());
		    	txttotalnoofdays.setEnabled(false);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	 public BlurListener getInterestRateListener() {

		 BlurListener listener = new BlurListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void blur(BlurEvent event) {
				 TextField component = (TextField) event.getComponent();
				 if(component.getValue() !=null ){
					 String intrRate = component.getValue().replace(",", "");		
					 Double interestRate = Double.parseDouble(intrRate);
					 interestRate = Double.parseDouble(new DecimalFormat("##.##").format(interestRate));
					 if(interestRate >= 0 
							 && interestRate > 100){
						 SHAUtils.showErrorMessageBoxWithCaption("Interest rate is exceeding 100%","Error - Legal Billing");
						 component.setValue(null);
					 }else{
						 if(interestRate > 18){
							 SHAUtils.showMessageBox("Interest rate is exceeding 18%","Information - Legal Billing");
						 }
						 component.setValue(interestRate.toString());
						 calculateInterestCurrentClaimAmt();
					 }	
				 }					
			 }
		 };
		 return listener;
	 }

	 @SuppressWarnings("deprecation")
	 private void calculateInterestCurrentClaimAmt(){
		 
		 if(txtinterestRate !=null && txttotalnoofdays !=null && txtawardAmount!=null){
			 if(txtinterestRate.getValue() !=null
					 && txttotalnoofdays.getValue() !=null
					 && txtawardAmount.getValue() !=null){
				 
				 Double interestRate = SHAUtils.getDoubleFromStringWithComma(txtinterestRate.getValue());
				 Double noofdays = SHAUtils.getDoubleFromStringWithComma(txttotalnoofdays.getValue());
				 Double awardAmount = SHAUtils.getDoubleFromStringWithComma(txtawardAmount.getValue());
				 Double interest = (interestRate/100);
				 Double days = (noofdays/365);
				 Double totalAmount = awardAmount*interest*days;
				 Long total = (long) Math.round(totalAmount);
				 if(txtinterestCurrentClaim != null){
					 txtinterestCurrentClaim.setEnabled(true);
					 txtinterestCurrentClaim.setValue(total.toString());
					 txtinterestCurrentClaim.setEnabled(false);
				 }
				 LegalBaseCell baseCell = new LegalBaseCell();
				 baseCell.setLegalBillingAmount(total.intValue());
				 amountTableObj.setValuesForFields(baseCell,12);
				 calculateTotalInterestAmt();
			 }		 
		 }
	 }
	 
	 private void calculateTotalInterestAmt(){
		 if(txtinterestCurrentClaim !=null && txtinterestOtherClaim !=null){    	
			 if(txtinterestCurrentClaim.getValue() !=null){
				 legalBillingDTO = dbCalculationService.getInterestOtherClaimForTaxDeductions(legalBillingDTO,bean.getKey());
				 if(legalBillingDTO.getInterestOtherClaim() !=null){
					 LegalBaseCell baseCell = new LegalBaseCell();
					 Long interestCurrent = Long.parseLong(txtinterestCurrentClaim.getValue().replace(",", ""));
					 Long interestOther = legalBillingDTO.getInterestOtherClaim();
					 if(txtinterestOtherClaim !=null){
						 txtinterestOtherClaim.setEnabled(true);
						 txtinterestOtherClaim.setValue(interestOther.toString());
						 txtinterestOtherClaim.setEnabled(false);
					 }
					 baseCell.setLegalBillingAmount(interestOther.intValue());
					 amountTableObj.setValuesForFields(baseCell,13);
					 Long totalintrestAmt = interestCurrent + interestOther;
					 if(txttotalInterestAmount !=null){
						 txttotalInterestAmount.setEnabled(true);
						 txttotalInterestAmount.setValue(totalintrestAmt.toString());
						 txttotalInterestAmount.setEnabled(false);
					 }		 
					 baseCell.setLegalBillingAmount(totalintrestAmt.intValue());
					 amountTableObj.setValuesForFields(baseCell,14);
					 if(totalintrestAmt >= 5000){
						 MastersValue value =null;
						 if((boolean) panDetails.getValue()){
							 value =  masterService.getMasterCodeFlag(SHAConstants.TDS_PAN);
						 }else{
							 value =  masterService.getMasterCodeFlag(SHAConstants.TDS_NONPAN); 
						 }
						 if(value !=null && value.getValue() !=null){
							 double tdspers = Double.parseDouble(value.getValue());
							 double tdsamount = (double)tdspers/100;
							 tdsamount *= totalintrestAmt.doubleValue();
							 Long tds = (long) Math.round(tdsamount);
							 if(legalBillingDTO.getTdsOtherClaim() !=null){
								 tds -= legalBillingDTO.getTdsOtherClaim();
								 tds = tds < 0? 0 : tds;
							 }				 
							 Long totalPayable = totalintrestAmt - tds;					 
							 baseCell.setTds(value.getValue()+"%");
							 baseCell.setLegalBillingAmount(tds.intValue());
							 LegalBaseCell totalpaybaseCell = new LegalBaseCell();
							 totalpaybaseCell.setLegalBillingAmount(totalPayable.intValue());
							 amountTableObj.setValuesForFields(baseCell,15);
							 amountTableObj.setValuesForFields(totalpaybaseCell,16);
						 }
					 }else{
						 baseCell.setTds("0");
						 baseCell.setLegalBillingAmount(0);
						 amountTableObj.setValuesForFields(baseCell,15);
						 LegalBaseCell totalpaybaseCell = new LegalBaseCell();
						 totalpaybaseCell.setLegalBillingAmount(totalintrestAmt.intValue());
						 amountTableObj.setValuesForFields(totalpaybaseCell,16);
						 SHAUtils.showMessageBox("No TAX Deduction","Information - Legal Billing");
					 }

				 }	
			 }
		 }
	 }
	 
	 private void addListener(){

		 interestApplicable.addValueChangeListener(new Property.ValueChangeListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void valueChange(ValueChangeEvent event) {

				 if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
					 fromDate.setEnabled(true);
					 toDate.setEnabled(true);
					 txtinterestRate.setEnabled(true);
					 if(legalBillingDTO.getInterestOtherClaim() !=null){
						 txtinterestOtherClaim.setEnabled(true);
						 txtinterestOtherClaim.setValue(legalBillingDTO.getInterestOtherClaim().toString());
						 txtinterestOtherClaim.setEnabled(false);
					 }
					 LegalBaseCell baseCell = new LegalBaseCell();
					 amountTableObj.setValuesForFields(baseCell,12);
					 amountTableObj.setValuesForFields(baseCell,13);
					 amountTableObj.setValuesForFields(baseCell,14);
					 amountTableObj.setValuesForFields(baseCell,15);
					 amountTableObj.setValuesForFields(baseCell,16);
				 } 
				 else if(event.getProperty() != null && event.getProperty().getValue() != null) {
					 unbindField(getListOfInterestFields());
					 fromDate.setEnabled(false);
					 toDate.setEnabled(false);
					 txtinterestRate.setEnabled(false);
					 LegalBaseCell baseCell = new LegalBaseCell();
					 baseCell.setIsRemarkEditable(false);
					 amountTableObj.setValuesForFields(baseCell,12);
					 amountTableObj.setValuesForFields(baseCell,13);
					 amountTableObj.setValuesForFields(baseCell,14);
					 amountTableObj.setValuesForFields(baseCell,15);
					 amountTableObj.setValuesForFields(baseCell,16);
				 }
			 }
		 });

		 fromDate.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = -8435623803385270083L;

			 @SuppressWarnings("unchecked")
			 @Override
			 public void valueChange(ValueChangeEvent event) {
				 Date enteredDate = (Date) event.getProperty().getValue();
				 if(null != enteredDate && null != toDate && null != toDate.getValue()){
					 if(enteredDate.after(toDate.getValue())){
						 SHAUtils.showErrorMessageBoxWithCaption("Interest from Date is Greater than TO Date","Error - Legal Billing");
						 event.getProperty().setValue(null);
					 }else if(enteredDate.equals(toDate.getValue())){
						 SHAUtils.showErrorMessageBoxWithCaption("Interest from Date is Equal TO Date","Error - Legal Billing");
						 event.getProperty().setValue(null);
					 }else{
						 calcuAndsetNoOfDays(enteredDate,toDate.getValue());
						 calculateInterestCurrentClaimAmt();
					 }
				 }
			 }
		 });

		 toDate.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = -8435623803385270083L;

			 @SuppressWarnings("unchecked")
			 @Override
			 public void valueChange(ValueChangeEvent event) {
				 Date enteredDate = (Date) event.getProperty().getValue();
				 Date currentSystemDate = new Date();
				 if(null != enteredDate && null != fromDate && null != fromDate.getValue()){
					 if(enteredDate.before(fromDate.getValue())){
						 SHAUtils.showErrorMessageBoxWithCaption("Interest from Date is Lesser than TO Date","Error - Legal Billing");
						 event.getProperty().setValue(null);
					 }else if(enteredDate.equals(fromDate.getValue())){
						 SHAUtils.showErrorMessageBoxWithCaption("Interest from Date is Equal TO Date","Error - Legal Billing");
						 event.getProperty().setValue(null);
					 }else{
						 calcuAndsetNoOfDays(fromDate.getValue(),enteredDate);
						 calculateInterestCurrentClaimAmt();
					 }
				 }
			 }
		 });
	 }

	 private BlurListener getCostListener() {
		 BlurListener listener = new BlurListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void blur(BlurEvent event) {
				 TextField value = (TextField) event.getComponent();
				 if(value != null && value.getValue() != null){
					 Integer costAmount = SHAUtils.getIntegerFromStringWithComma(value.getValue());		
					 if(amountTableObj != null){ 					
						 LegalBaseCell baseCell = new LegalBaseCell();
						 baseCell.setLegalBillingAmount(costAmount);
						 amountTableObj.setValuesForFields(baseCell,9);
					 }	
				 }
			 }					
		 };
		 return listener;
	 }
	 
	 private BlurListener getawardAmountListener() {
		 BlurListener listener = new BlurListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void blur(BlurEvent event) {
				 TextField value = (TextField) event.getComponent();
				 if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					 Integer awardAmount = SHAUtils.getIntegerFromStringWithComma(value.getValue());		
					 if(amountTableObj != null){ 					
						 LegalBaseCell baseCell = new LegalBaseCell();
						 baseCell.setLegalBillingAmount(awardAmount);
						 amountTableObj.setValuesForFields(baseCell,8);
					 }	
				 }
			 }					
		 };
		 return listener;
	 }
	 
	 private BlurListener getCompensationListener() {
		 BlurListener listener = new BlurListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void blur(BlurEvent event) {
				 TextField value = (TextField) event.getComponent();
				 if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					 Integer compensationAmount = SHAUtils.getIntegerFromStringWithComma(value.getValue());		
					 if(amountTableObj != null){ 					
						 LegalBaseCell baseCell = new LegalBaseCell();
						 baseCell.setLegalBillingAmount(compensationAmount);
						 amountTableObj.setValuesForFields(baseCell,10);
					 }	
				 }
			 }					
		 };
		 return listener;
	 }

	 public LegalBillingDTO getvalue(){
		 try {
			 this.legalBinder.commit();
			 LegalBillingDTO bean = this.legalBinder.getItemDataSource().getBean();
			 if(amountTableObj !=null){
				 bean = amountTableObj.getValues(bean);
			 }
			 return  bean;
		 } catch (CommitException e) {
			 e.printStackTrace();
		 }
		 return null;
	 }
	 
	 private void setTabelValues(){
		 
		if(legalBillingDTO !=null){
			amountTableObj.setValues(legalBillingDTO);
			if(legalBillingDTO.getInterestApplicable()){
				calculateTotalInterestAmt();
			}
		}
	 }
	 
	 private void addPanListener(){
		 panDetails.addValueChangeListener(new Property.ValueChangeListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void valueChange(ValueChangeEvent event) {

				 if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
					 if(presenterString !=null){
						 if(presenterString.equals(SHAConstants.CLAIM_BILLING)){
							 fireViewEvent(BillingHospitalizationPagePresenter.BILLING_TDS_PAN_VALIDATION,true);
						 }else if(presenterString.equals(SHAConstants.CLAIM_FINANCIAL)){
							 fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_TDS_PAN_VALIDATION,true);
						 }else if(presenterString.equals(SHAConstants.PA_CLAIM_APPROVAL)){
							 fireViewEvent(PAClaimAprNonHosReviewPagePresenter.PA_CLAIM_TDS_PAN_VALIDATION,true);
						 }else if(presenterString.equals(SHAConstants.PA_FINANCIAL)){
							 fireViewEvent(PANonHospFinancialReviewPagePresenter.PA_FINANCIAL_NONHOSP_TDS_PAN_VALIDATION,true);
						 }else if(presenterString.equals(SHAConstants.PA_FINANCIAL_HOSP)){
							 fireViewEvent(PAHealthFinancialHospitalizationPagePresenter.PA_FINANCIAL_HOSP_TDS_PAN_VALIDATION,true);
						 }else if(presenterString.equals(SHAConstants.PA_BILLING_HOSP)){
							 fireViewEvent(PAHealthBillingHospitalizationPagePresenter.PA_BILLING_HOSP_TDS_PAN_VALIDATION,true);
						 }
					 }
					 
					 calculateTotalInterestAmt();
				 } 
				 else if(event.getProperty() != null && event.getProperty().getValue() != null) {
					 if(presenterString !=null){
						 if(presenterString.equals(SHAConstants.CLAIM_BILLING)){
							 fireViewEvent(BillingHospitalizationPagePresenter.BILLING_TDS_PAN_VALIDATION,false);
						 }else if(presenterString.equals(SHAConstants.CLAIM_FINANCIAL)){
							 fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_TDS_PAN_VALIDATION,false);
						 }else if(presenterString.equals(SHAConstants.PA_CLAIM_APPROVAL)){
							 fireViewEvent(PAClaimAprNonHosReviewPagePresenter.PA_CLAIM_TDS_PAN_VALIDATION,false);
						 }else if(presenterString.equals(SHAConstants.PA_FINANCIAL)){
							 fireViewEvent(PANonHospFinancialReviewPagePresenter.PA_FINANCIAL_NONHOSP_TDS_PAN_VALIDATION,false);
						 }else if(presenterString.equals(SHAConstants.PA_FINANCIAL_HOSP)){
							 fireViewEvent(PAHealthFinancialHospitalizationPagePresenter.PA_FINANCIAL_HOSP_TDS_PAN_VALIDATION,false);
						 }else if(presenterString.equals(SHAConstants.PA_BILLING_HOSP)){
							 fireViewEvent(PAHealthBillingHospitalizationPagePresenter.PA_BILLING_HOSP_TDS_PAN_VALIDATION,false);
						 }
					 }
					 calculateTotalInterestAmt();
				 }
			 }
		 });
	 }
	 public boolean getPanDetails(){
		 if(panDetails !=null){
			 return (boolean) panDetails.getValue();
		 }
		 return false;
	 }

	 protected void showOrHideValidation(Boolean isVisible) {
		 for (Component component : mandatoryFields) {
			 AbstractField<?> field = (AbstractField<?>) component;
			 field.setRequired(!isVisible);
			 field.setValidationVisible(isVisible);
		 }
	 }
	 
	 //IMSSUPPOR-32607 changes done for this support fix
	 public String isValid(){

		 if(!(txtcost.getValue()!=null)){
			 return "Please Enter Cost Amount";
		 }else if( !(txtcompensation.getValue()!=null) ){
			 return "Please Enter Compensation Amount";
		 }
		 if(interestApplicable !=null && interestApplicable.getValue() !=null){
			 Boolean interestApplicab = (Boolean)interestApplicable.getValue();
			 if(interestApplicab){
				 if(txtinterestRate !=null 
						 && !(txtinterestRate.getValue() !=null)){
					 return "Please Enter InterestRate Amount";
				 }else if(fromDate !=null && !(fromDate.getValue() !=null)){
					 return "Please Select FromDate";
				 }else if(toDate !=null && !(toDate.getValue() !=null)){
					 return "Please Select ToDate";
				 } 
			 }
		 }	 
		 return null;
	 }

	 public Boolean getinterestApplicable(){
		 if(interestApplicable !=null && interestApplicable.getValue() !=null){
			 return (Boolean)interestApplicable.getValue();
		 }
		 return false;
	 }
}
