/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.financial.pages.billreview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDataExtractionPresenter;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.wizard.PatientCareListenerTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class PAHealthAddOnBenefitsDataExtractionPage extends ViewComponent{
	
	
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;

	@Inject
	private UploadDocumentDTO bean;
	
	private OptionGroup hospitalAddOnBenefits;
	
	private OptionGroup patientCareAddOnBenefits;
	
	private VerticalLayout hospitalCashBenefitsLayout;
	
	private VerticalLayout patientCareBenefitsLayout;

	
	private TextField hospitalCashNoOfDays;
	
	private TextField hospitalCashPerDayAmt;
	
	private TextField hospitalCashTotalClaimedAmt;
	
	private TextField patientCareNoOfDays;
	
	private TextField patientCarePerDayAmt;
	
	private TextField patientCareTotalClaimedAmt;
	
	private OptionGroup treatmentPhysiotherapy;
	
	private OptionGroup treatmentInGovtHospital;
	
	@Inject
	private PatientCareListenerTable patientCareTableObj;
	
	protected Map<String, Object> referenceDataForPatientCare = new HashMap<String, Object>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private String presenterString = "";
	
	private Boolean isStarCare = false;
	
	private List<String> errorList = new ArrayList<String>();
 	
	
	@PostConstruct
	public void init() {

	}
	
	public void init(UploadDocumentDTO bean,String presenterString, Boolean isStarCare) {
		this.bean = bean;
		this.presenterString = presenterString;
		this.isStarCare = isStarCare;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	public void getContent()
	{
		initBinder();
		errorList = new ArrayList<String>();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		hospitalCashBenefitsLayout = new VerticalLayout();
		patientCareBenefitsLayout = new VerticalLayout();
		
		hospitalAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Hospital Cash) Applicable ", "hospitalCashAddonBenefits", OptionGroup.class);
		
		hospitalAddOnBenefits.addItems(getReadioButtonOptions());
		hospitalAddOnBenefits.setItemCaption(true, "Yes");
		hospitalAddOnBenefits.setItemCaption(false, "No");
		hospitalAddOnBenefits.setStyleName("horizontal");
		
		
		
		/*if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
		{
			hospitalAddOnBenefits.setEnabled(false);
		}
		else
		{
			if((ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_HOSPITAL_CASH, this.bean);
					}
		}*/
		
		
		patientCareAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Patient Care) Applicable ", "patientCareAddOnBenefits", OptionGroup.class);
		
		patientCareAddOnBenefits.addItems(getReadioButtonOptions());
		patientCareAddOnBenefits.setItemCaption(true, "Yes");
		patientCareAddOnBenefits.setItemCaption(false, "No");
		patientCareAddOnBenefits.setStyleName("horizontal");
		
		addListener();
		
		if(null != this.bean.getHospitalBenefitFlag() && ("HC").equalsIgnoreCase(this.bean.getHospitalBenefitFlag()))
		{
			hospitalAddOnBenefits.select(true);
			generateFieldsBasedOnHospitalCashBenefits(true);
		} else {
			hospitalAddOnBenefits.setEnabled(false);
		}
		
		if(null != this.bean.getPatientCareBenefitFlag() && ("PC").equalsIgnoreCase(this.bean.getPatientCareBenefitFlag()))
		{
			patientCareAddOnBenefits.select(true);
			generateFieldsBasedOnPatientCareBenefits(true);
		} else {
			patientCareAddOnBenefits.setEnabled(false);
		}
		
		if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)){
			patientCareAddOnBenefits.setEnabled(false);
			hospitalAddOnBenefits.setEnabled(false);
			if(this.bean.getHospitalCashAddonBenefitsFlag() != null && this.bean.getHospitalCashAddonBenefitsFlag().equalsIgnoreCase("Y")){
				hospitalAddOnBenefits.setEnabled(false);
				fireViewEvent(PAHealthFinancialReviewPagePresenter.CLAIM_HOSPITAL_BENEFITS, true);
			}
		}
		
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString)){
			patientCareAddOnBenefits.setEnabled(false);
			hospitalAddOnBenefits.setEnabled(false);
			if(this.bean.getPatientCareAddOnBenefitsFlag() != null && this.bean.getPatientCareAddOnBenefitsFlag().equalsIgnoreCase("Y")){
				fireViewEvent(PAHealthBillingReviewPagePresenter.CLAIM_PATIENT_CARE_BENEFITS, true);
			}
			
		}
		
		if((ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS).equalsIgnoreCase(presenterString)){
			patientCareAddOnBenefits.setEnabled(false);
			hospitalAddOnBenefits.setEnabled(false);
			if(this.bean.getPatientCareAddOnBenefitsFlag() != null && this.bean.getPatientCareAddOnBenefitsFlag().equalsIgnoreCase("Y")){
				
				fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_PATIENT_CARE_BENEFITS, true);
			}
			
		}
			
		/*if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
		{
			patientCareAddOnBenefits.setEnabled(false);
		}*/
		/*else
		{
			if((ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_PATIENT_CARE, this.bean);
					}
		}*/
		

		
		VerticalLayout benefitsLayout = new VerticalLayout();
		
		Panel benefitsPanel = new Panel();
		
//		benefitsLayout.setWidth("100%");
		
		benefitsLayout.addComponent(hospitalAddOnBenefits);
		benefitsLayout.addComponent(hospitalCashBenefitsLayout);
		benefitsLayout.addComponent(patientCareAddOnBenefits);
		benefitsLayout.addComponent(patientCareBenefitsLayout);
		benefitsPanel.setContent(benefitsLayout);
		setCompositionRoot(benefitsPanel);
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean value) {
		if(null != hospitalCashBenefitsLayout && hospitalCashBenefitsLayout.getComponentCount() > 0) {
			unbindField(hospitalCashNoOfDays);
			unbindField(hospitalCashPerDayAmt);
			unbindField(hospitalCashTotalClaimedAmt);
			unbindField(treatmentPhysiotherapy);
			unbindField(treatmentInGovtHospital);
			mandatoryFields.remove(hospitalCashPerDayAmt);
			hospitalCashBenefitsLayout.removeAllComponents();
		}
		if(value) {
			Table table = new Table();
			table.setHeight("140px");
			table.setWidth("100%");
			table.addContainerProperty("No of Days", TextField.class, null);
			table.addContainerProperty("Per Day Amount", TextField.class, null);
			table.addContainerProperty("Total Claimed Amount", TextField.class, null);
			
			Object addItem = table.addItem();
			
			
			hospitalCashNoOfDays = (TextField) binder.buildAndBind(
					"", "hospitalCashNoofDays", TextField.class);
			hospitalCashNoOfDays.setMaxLength(3);
			CSValidator hospitalCashValidator = new CSValidator();
			hospitalCashValidator.extend(hospitalCashNoOfDays);
			hospitalCashValidator.setRegExp("^[0-9]*$");
			hospitalCashValidator.setPreventInvalidTyping(true);
			hospitalCashNoOfDays.setMaxLength(5);
			
			
			/*hospitalCashNoOfDays.addBlurListener(new BlurListener() {
				
				private static final long serialVersionUID = -7944733816900171622L;

				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromString(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});*/
			//hospitalCashNoOfDays.addValueChangeListener(new org.apache.tools.ant.taskd);
			
			hospitalCashNoOfDays
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), (SHAUtils.getDoubleValueFromString(hospitalCashPerDayAmt.getValue())).intValue(), hospitalCashTotalClaimedAmt);
				}
			});
			
			hospitalCashPerDayAmt = (TextField) binder.buildAndBind(
					"", "hospitalCashPerDayAmt", TextField.class);
			hospitalCashPerDayAmt.setMaxLength(10);
			
			CSValidator perDayAmtValidator = new CSValidator();
			perDayAmtValidator.extend(hospitalCashPerDayAmt);
			perDayAmtValidator.setRegExp("^[0-9]*$");
			perDayAmtValidator.setPreventInvalidTyping(true);
		
			/*hospitalCashPerDayAmt.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), SHAUtils.getIntegerFromString(hospitalCashPerDayAmt.getValue()), hospitalCashTotalClaimedAmt);
				}
			});*/
			
			hospitalCashPerDayAmt
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(hospitalCashNoOfDays.getValue()), SHAUtils.getDoubleValueFromString(hospitalCashPerDayAmt.getValue()).intValue(), hospitalCashTotalClaimedAmt);
				}
			});
			
			hospitalCashTotalClaimedAmt = (TextField) binder.buildAndBind(
					"", "hospitalCashTotalClaimedAmt", TextField.class);
			hospitalCashTotalClaimedAmt.setEnabled(false);
			
			table.getContainerProperty(addItem, "No of Days").setValue(hospitalCashNoOfDays);
			table.getContainerProperty(addItem, "Per Day Amount").setValue(hospitalCashPerDayAmt);
			table.getContainerProperty(addItem, "Total Claimed Amount").setValue(hospitalCashTotalClaimedAmt);
			
			treatmentPhysiotherapy = (OptionGroup) binder.buildAndBind("Treatment for Physiotherapy or any Epidemic", "treatmentPhysiotherapy", OptionGroup.class);
			
			treatmentInGovtHospital = (OptionGroup) binder.buildAndBind("Treatment taken in Government Hospital", "treatmentGovtHospital", OptionGroup.class);
			if(bean.getTreatmentGovtHospFlag() != null && !bean.getTreatmentGovtHospFlag().equalsIgnoreCase("y")) {
				treatmentInGovtHospital.select(false);
			}
			if(!this.isStarCare) {
				treatmentInGovtHospital.setEnabled(false);
			}
			//hospitalCashBenefitsLayout = new VerticalLayout(table, treatmentPhysiotherapy);
			
			mandatoryFields.add(hospitalCashPerDayAmt);
			setRequiredAndValidation(hospitalCashPerDayAmt);
			showOrHideValidation(false);
			
			
			hospitalCashBenefitsLayout.addComponent(table);
			hospitalCashBenefitsLayout.addComponent(treatmentPhysiotherapy);
			hospitalCashBenefitsLayout.addComponent(treatmentInGovtHospital);
		} 
	}
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnPatientCareBenefits(Boolean value) {
		
		if(null != patientCareBenefitsLayout &&  patientCareBenefitsLayout.getComponentCount() > 0) {
			unbindField(patientCareNoOfDays);
			unbindField(patientCarePerDayAmt);
			unbindField(patientCareTotalClaimedAmt);
			mandatoryFields.remove(patientCarePerDayAmt);
			//unbindField(treatmentPhysiotherapy);
			patientCareBenefitsLayout.removeAllComponents();
		}
		if(value) {
			Table table = new Table();
			table.setHeight("140px");
			table.setWidth("100%");
			table.addContainerProperty("No of Days", TextField.class, null);
			table.addContainerProperty("Per Day Amount", TextField.class, null);
			table.addContainerProperty("Total Claimed Amount", TextField.class, null);
			
			Object addItem = table.addItem();
			
			patientCareNoOfDays = (TextField) binder.buildAndBind(
					"", "patientCareNoofDays", TextField.class);
			patientCareNoOfDays.setMaxLength(3);
			patientCareNoOfDays.setEnabled(false);
			
			patientCareTotalClaimedAmt = (TextField) binder.buildAndBind(
					"", "patientCareTotalClaimedAmt", TextField.class);
			patientCareTotalClaimedAmt.setEnabled(false);
			
			
			/*patientCareNoOfDays.addBlurListener(new BlurListener() {
				
				private static final long serialVersionUID = -7944733816900171622L;

				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getDoubleValueFromString(patientCarePerDayAmt.getValue()).intValue(), patientCareTotalClaimedAmt);
				}
			});*/
			
			patientCareNoOfDays.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getDoubleValueFromString(patientCarePerDayAmt.getValue()).intValue(), patientCareTotalClaimedAmt);
				}
			});
			
			
			
			
			patientCarePerDayAmt = (TextField) binder.buildAndBind(
					"", "patientCarePerDayAmt", TextField.class);
			patientCarePerDayAmt.setMaxLength(10);
			patientCarePerDayAmt.setEnabled(true);
			
			/*patientCarePerDayAmt.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getDoubleValueFromString(patientCarePerDayAmt.getValue()).intValue(), patientCareTotalClaimedAmt);
				}
			});*/
			
			patientCarePerDayAmt.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {			
					setCalculatedValue(SHAUtils.getIntegerFromString(patientCareNoOfDays.getValue()), SHAUtils.getDoubleValueFromString(patientCarePerDayAmt.getValue()).intValue(), patientCareTotalClaimedAmt);
				}
			});
			
			
			table.getContainerProperty(addItem, "No of Days").setValue(patientCareNoOfDays);
			table.getContainerProperty(addItem, "Per Day Amount").setValue(patientCarePerDayAmt);
			table.getContainerProperty(addItem, "Total Claimed Amount").setValue(patientCareTotalClaimedAmt);
			
			//patientCareTableObj = patientCareTableInstance.get();
			//patientCareTableObj.init(true);
			patientCareTableObj.init(true,bean.getAdmissionDate(),this.bean.getDischargeDate());
			patientCareTableObj.setHeight("200px");
			/*patientCareTableObj.init("", true);
			patientCareTableObj.setReference(referenceDataForPatientCare);*/
			
			
			addNoofDaysListener();
			
			//patientCareBenefitsLayout = new VerticalLayout(table, patientCareTableObj);
			
			patientCareBenefitsLayout.addComponent(patientCareTableObj);
			patientCareBenefitsLayout.addComponent(table);
			if(null != bean.getPatientCareDTO() && !bean.getPatientCareDTO().isEmpty())
			{
				for (PatientCareDTO patientCareDTO : bean.getPatientCareDTO()) {
					
					patientCareTableObj.addBeanToList(patientCareDTO);
				}
				calculatePatientCareTableValue();
				//patientCareTableObj.manageListeners();
			}
			mandatoryFields.add(patientCarePerDayAmt);
			setRequiredAndValidation(patientCarePerDayAmt);
			showOrHideValidation(false);
		} 
	}
	
	private void calculatePatientCareTableValue()
	{
		if(null != patientCareNoOfDays) 
		{
			Integer value = 0;
			Integer perDayAmtvalue = 0;
			if(null !=patientCareNoOfDays.getValue() && !("").equalsIgnoreCase(patientCareNoOfDays.getValue()) )
			{
				value = Integer.parseInt(patientCareNoOfDays.getValue());
			}
			if(null != patientCarePerDayAmt && null != patientCarePerDayAmt.getValue() && !("").equalsIgnoreCase(patientCarePerDayAmt.getValue()))
			{
				
				perDayAmtvalue = SHAUtils.getDoubleValueFromString(patientCarePerDayAmt.getValue()).intValue();
			}
			Integer totalAmt = value * perDayAmtvalue;
			if(null != patientCareTotalClaimedAmt)
			patientCareTotalClaimedAmt.setValue(totalAmt.toString());
		}
	}
	
	private void addNoofDaysListener()
	{
		this.patientCareTableObj.dateDiffFld.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String value = (String)event.getProperty().getValue();
				
				patientCareNoOfDays.setValue(value);
				//Vaadin8-setImmediate() patientCareNoOfDays.setImmediate(true);
			}		
		});
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	

	private void setCalculatedValue(Integer value1, Integer value2, TextField calculatedValueField) {
		Integer calculatedValue = value1 * value2;
		calculatedValueField.setValue(String.valueOf(calculatedValue));
	}
	
	
	private void addListener()
	{
		hospitalAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				if((ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS).equalsIgnoreCase(presenterString))
				{
					fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_HOSPITAL_BENEFITS, isChecked);
				} else if((SHAConstants.BILLING).equalsIgnoreCase(presenterString)) {
					fireViewEvent(PAHealthBillingReviewPagePresenter.CLAIM_HOSPITAL_BENEFITS, isChecked);
				} else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)) {
					fireViewEvent(PAHealthFinancialReviewPagePresenter.CLAIM_HOSPITAL_BENEFITS, isChecked);
				}
			}
		});
		
		patientCareAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				if((ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS).equalsIgnoreCase(presenterString))
				{
					fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_PATIENT_CARE_BENEFITS, isChecked);
				} else if((SHAConstants.BILLING).equalsIgnoreCase(presenterString)) {
					fireViewEvent(PAHealthBillingReviewPagePresenter.CLAIM_PATIENT_CARE_BENEFITS, isChecked);
				} else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)) {
					fireViewEvent(PAHealthFinancialReviewPagePresenter.CLAIM_PATIENT_CARE_BENEFITS, isChecked);
				}
			}
		});
	}
	
	public List<PatientCareDTO> patientCareTableList()
	{
		List<PatientCareDTO> patientList = null;
		if(null != this.patientCareAddOnBenefits && null != (this.patientCareAddOnBenefits.getValue()) && ("Yes").equalsIgnoreCase(this.patientCareAddOnBenefits.getValue().toString()) && null != patientCareTableObj )
		{
			patientList = patientCareTableObj.getValues();
			//this.bean.getUploadDocumentsDTO().setPatientCareDTO(patientList);
		}
		return patientList;
	}
	
	public void setHospitalCashValues(List<Object> benefitList) {
		if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
		this.bean.setHospitalCashPerDayAmt(String.valueOf(benefitList.get(3)));
		
	}

	public void setPatientCareValues(List<Object> benefitList) {
		if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
			this.bean.setPatientCarePerDayAmt(String.valueOf(benefitList.get(3)));
		
	}
	
	public List<String> getErrors() {
		return errorList;
	}
	
	public Boolean isValid() {
		Boolean isValid = true;
		
		errorList.clear();
		
		try
		{
			if(this.binder.isValid()) {
				this.binder.commit();
//				if(this.patientCareTableObj != null) {
//					this.bean.setPatientCareDTO(this.patientCareTableObj.getValues());
//					if(this.bean.getPatientCareDTO() != null && ! this.bean.getPatientCareDTO().isEmpty()){
//						for (PatientCareDTO dto : this.bean.getPatientCareDTO() ) {
//							if(dto.getEngagedFrom() == null){
//								errorList.add("Please Enter Engaged From </br>");
//								isValid = false;
//								break;
//							}else if(dto.getEngagedTo() == null){
//								errorList.add("Please Enter Engaged To </br>");
//								isValid = false;
//								break;
//							}
//						}
//					}
//				}
				if(null != patientCareAddOnBenefits && null != patientCareAddOnBenefits.getValue())
				{
					 Boolean  patientCareBenefits = (Boolean) patientCareAddOnBenefits.getValue();
					 if(null != patientCareBenefits && null != patientCareBenefits && patientCareBenefits)
					 {
						 if(null != this.patientCareTableObj)
							{
								Boolean isDublicate = patientCareTableObj.isValid();
								if (!isDublicate) {
									isValid = false;
									List<String> errors = this.patientCareTableObj.getErrors();
									for (String error : errors) {
										error = error+"</br>";
										errorList.add(error);
									}
								}
							}
					 }
				}
			} else {
				isValid = false;
			}
		}
		catch (CommitException e) {
			e.printStackTrace();
		}
		return isValid;
		
	}
		/*Boolean isValid = true;
		String eMsg = "";
		errorList.removeAll(errorList);
		try {
			if(this.binder.isValid()) {
				this.binder.commit();
				if(this.patientCareTableObj != null) {
					this.bean.setPatientCareDTO(this.patientCareTableObj.getValues());
				}
			} else {
				isValid = false;
				
				for (Field<?> field : this.binder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg += errMsg.getFormattedHtmlMessage();
					}
					errorList.add(eMsg);
				}
			}
			
			
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return isValid; 
	}*/

	
	public void setTableValuesToDTO(){
		if(null != this.patientCareAddOnBenefits && null != (this.patientCareAddOnBenefits.getValue()) && ("true").equalsIgnoreCase(this.patientCareAddOnBenefits.getValue().toString()) && null != patientCareTableObj )
		{
			List<PatientCareDTO> patientList = patientCareTableObj.getValues();
			this.bean.setPatientCareDTO(patientList);
		}
	}
	
 public boolean validatePage() { 
		
	    errorList.removeAll(getErrors());
		Boolean hasError = false;
		String eMsg = "";
		showOrHideValidation(true);
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					errorList.add(errMsg.getFormattedHtmlMessage());
//					eMsg += errMsg.getFormattedHtmlMessage();
					hasError = true;
				}
				
			}
		}
		
		if(null != patientCareAddOnBenefits && null != patientCareAddOnBenefits.getValue())
		{
			 Boolean  patientCareBenefits = (Boolean) patientCareAddOnBenefits.getValue();
			 if(null != patientCareBenefits && null != patientCareBenefits && patientCareBenefits)
			 {
				 if(null != this.patientCareTableObj)
					{
						Boolean isValid = patientCareTableObj.isValid();
						if (!isValid) {
							hasError = true;
							List<String> errors = this.patientCareTableObj.getErrors();
							for (String error : errors) {
								error = error+"</br>";
								errorList.add(error);
							}
//							for (String error : errors) {
//								eMsg += error + "</br>";
//							}
						}
					}
			 }
		}
		
		
		/*if(null != this.patientCareTableObj)
		{
			if(!(null != patientCarePerDayAmt && null != patientCarePerDayAmt.getValue() && !("").equalsIgnoreCase(patientCarePerDayAmt.getValue())))
			{
				hasError = true;
				eMsg += "Please enter patient care per day amount </br>";
			}
		}*/
		if(hasError)
		{
			return false;
			
		}
		else
		{
			try {
				this.binder.commit();
				showOrHideValidation(false);
				setTableValuesToDTO();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
}
