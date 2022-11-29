package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class UpdateHospitalDetails extends ViewComponent {
	private static final long serialVersionUID = 2384804677886599977L;
	
	public BeanFieldGroup<ZonalReviewUpdateHospitalDetailsDTO> binder;
	
	private PreauthDTO bean;
	
	private List<String> errorMessages;  
	
	private TextField hospitalName;
	
	private TextField hospitalPhoneNumber;
	
	private TextField hospitalRegNumber;
	
	private TextField panNumber;
	
	private TextField inpatientBeds;
	
	private TextField hospitalCode;
	
	private TextField hospitalAddress1;
	
	private TextField hospitalAddress2;
	
	private TextField hospitalAddress3;
	
	private TextField hospitalCity;
	
	private TextField hospitalState;
	
	private TextField hospitalPincode;
	
	private OptionGroup OTFacility;
	
	private OptionGroup ICUFacility ;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean) {
		this.bean = bean;
		initBinder();
		errorMessages = new ArrayList<String>();
		
		hospitalName = (TextField) binder.buildAndBind("Hospital Name", "hospitalName",
				TextField.class);
		CSValidator hospitalNameValidator = new CSValidator();
		hospitalNameValidator.extend(hospitalName);
		hospitalNameValidator.setRegExp("^[A-Za-z]*$");
		hospitalNameValidator.setPreventInvalidTyping(true);
		hospitalName.setEnabled(false);
		
		hospitalPhoneNumber = (TextField) binder.buildAndBind("Hospital Ph No", "hospitalPhoneNo",
				TextField.class);
		CSValidator hospitalPhoneNumberValidator = new CSValidator();
		hospitalPhoneNumberValidator.extend(hospitalPhoneNumber);
		hospitalPhoneNumberValidator.setRegExp("^[0-9]*$");
		hospitalPhoneNumberValidator.setPreventInvalidTyping(true);
		hospitalPhoneNumber.setEnabled(false);
		
		hospitalRegNumber = (TextField) binder.buildAndBind("Hospital Registration No", "hopitalRegNumber",
				TextField.class);
		CSValidator hospitalRegNumberValidator = new CSValidator();
		hospitalRegNumberValidator.extend(hospitalRegNumber);
		hospitalRegNumberValidator.setRegExp("^[0-9]*$");
		hospitalRegNumberValidator.setPreventInvalidTyping(true);
		hospitalRegNumber.setEnabled(false);
		
		panNumber = (TextField) binder.buildAndBind("PAN No", "panNumber",
				TextField.class);
		CSValidator panNumberValidator = new CSValidator();
		panNumberValidator.extend(panNumber);
		panNumberValidator.setRegExp("^[A-Z a-z 0-9]*$");
		panNumberValidator.setPreventInvalidTyping(true);
		panNumber.setEnabled(false);
		
		inpatientBeds = (TextField) binder.buildAndBind("No of Inpatient Beds", "inpatientBeds",
				TextField.class);
		inpatientBeds.setMaxLength(5);
		inpatientBeds.setVisible(true);
		CSValidator inpatientBedsValidator = new CSValidator();
		inpatientBedsValidator.extend(inpatientBeds);
		inpatientBedsValidator.setRegExp("^[0-9]*$");
		inpatientBedsValidator.setPreventInvalidTyping(true);
		
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = bean.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		
	
		
		hospitalCode = (TextField) binder.buildAndBind("Hospital Code", "hospitalCode",
				TextField.class);
		CSValidator hospitalCodeValidator = new CSValidator();
		hospitalCodeValidator.extend(hospitalCode);
		hospitalCodeValidator.setRegExp("^[A-Z a-z 0-9]*$");
		hospitalCodeValidator.setPreventInvalidTyping(true);
		hospitalCode.setEnabled(false);
		
		hospitalAddress1 = (TextField) binder.buildAndBind("Address 1", "hospitalAddress1",
				TextField.class);
		CSValidator hospitalAddress1Validator = new CSValidator();
		hospitalAddress1Validator.extend(hospitalAddress1);
		hospitalAddress1Validator.setRegExp("^[A-Z a-z 0-9]*$");
		hospitalAddress1Validator.setPreventInvalidTyping(true);
		hospitalAddress1.setEnabled(false);
		
		hospitalAddress2 = (TextField) binder.buildAndBind("Address 2", "hospitalAddress2",
				TextField.class);
		CSValidator hospitalAddress2Validator = new CSValidator();
		hospitalAddress2Validator.extend(hospitalAddress2);
		hospitalAddress2Validator.setRegExp("^[A-Z a-z 0-9]*$");
		hospitalAddress2Validator.setPreventInvalidTyping(true);
		hospitalAddress2.setEnabled(false);
		
		hospitalAddress3 = (TextField) binder.buildAndBind("Address 3", "hospitalAddress3",
				TextField.class);
		CSValidator hospitalAddress3Validator = new CSValidator();
		hospitalAddress3Validator.extend(hospitalAddress3);
		hospitalAddress3Validator.setRegExp("^[A-Z a-z 0-9]*$");
		hospitalAddress3Validator.setPreventInvalidTyping(true);
		hospitalAddress3.setEnabled(false);
		
		hospitalCity = (TextField) binder.buildAndBind("City", "hospitalCity",
				TextField.class);
		CSValidator hospitalCityValidator = new CSValidator();
		hospitalCityValidator.extend(hospitalCity);
		hospitalCityValidator.setRegExp("^[A-Z a-z]*$");
		hospitalCityValidator.setPreventInvalidTyping(true);
		hospitalCity.setEnabled(false);
		
		hospitalState = (TextField) binder.buildAndBind("State", "hospitalState",
				TextField.class);
		CSValidator hospitalStateValidator = new CSValidator();
		hospitalStateValidator.extend(hospitalState);
		hospitalStateValidator.setRegExp("^[A-Z a-z]*$");
		hospitalStateValidator.setPreventInvalidTyping(true);
		hospitalState.setEnabled(false);
		
		hospitalPincode = (TextField) binder.buildAndBind("Pincode", "hospitalPincode",
				TextField.class);
		CSValidator hospitalPincodeValidator = new CSValidator();
		hospitalPincodeValidator.extend(hospitalPincode);
		hospitalPincodeValidator.setRegExp("^[0-9]*$");
		hospitalPincodeValidator.setPreventInvalidTyping(true);
		hospitalPincode.setEnabled(false);
		
		OTFacility = (OptionGroup) binder.buildAndBind(
				"1) OT", "otFacility", OptionGroup.class);
		
		OTFacility.addItems(getReadioButtonOptions());
		OTFacility.setItemCaption(true, "Yes");
		OTFacility.setItemCaption(false, "No");
		OTFacility.setStyleName("horizontal");
		
		ICUFacility = (OptionGroup) binder.buildAndBind(
				"2) ICU", "icuFacility", OptionGroup.class);
		
		ICUFacility.addItems(getReadioButtonOptions());
		ICUFacility.setItemCaption(true, "Yes");
		ICUFacility.setItemCaption(false, "No");
		ICUFacility.setStyleName("horizontal");
		
		FormLayout hospitalDetailLeftFLayout = new FormLayout(hospitalName, hospitalPhoneNumber, hospitalRegNumber, panNumber, inpatientBeds, OTFacility, ICUFacility);
		FormLayout hospitalDetailRightFLayout = new FormLayout(hospitalCode, hospitalAddress1, hospitalAddress2, hospitalAddress3, hospitalCity, hospitalState, hospitalPincode);
		
		HorizontalLayout hospitalDetailsHLayout = new HorizontalLayout(hospitalDetailLeftFLayout, hospitalDetailRightFLayout);
		hospitalDetailsHLayout.setSpacing(true);
		hospitalDetailsHLayout.setWidth("100%");
		hospitalDetailsHLayout.setCaption("Hospital Details");
		mandatoryFields.add(inpatientBeds);
		showOrHideValidation(false);
		
		if(updateHospitalDetails != null){
			if(updateHospitalDetails.getHospitalTypeId() != null && updateHospitalDetails.getHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
				inpatientBeds.setEnabled(true);
				OTFacility.setEnabled(false);
				ICUFacility.setEnabled(false);
			}
		}
		
		setCompositionRoot(new VerticalLayout(hospitalDetailsHLayout));
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//String eMsg = "";
		
		if (!this.binder.isValid()) {
		    
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 }  else {
			 try {
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<ZonalReviewUpdateHospitalDetailsDTO>(ZonalReviewUpdateHospitalDetailsDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthDataExtractionDetails().getUpdateHospitalDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	

}
