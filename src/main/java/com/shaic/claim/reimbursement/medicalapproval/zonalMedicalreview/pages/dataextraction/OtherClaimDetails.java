package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;

public class OtherClaimDetails extends ViewComponent {

	private static final long serialVersionUID = 2531967834252716692L;

	public BeanFieldGroup<OtherClaimDetailsDTO> binder;
	
	@Inject
	private Instance<OtherClaimDiagnosisDetails> otherClaimTable;
	
	private OtherClaimDiagnosisDetails otherClaimTableObj;
	
	private PreauthDTO bean;
	
	private List<String> errorMessages;  
	
	private TextField insuranceCompanyName;
	
	private DateField commencementDate;
	
	private TextField policyNumber;
	
	private TextField otherClaimsumInsured;
	
	private OptionGroup last4yearsHospitalisation;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private VerticalLayout tableLayout;
	
	private Map<String, Object> referenceData;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean, Map<String, Object> referenceData) {
		this.bean = bean;
		this.referenceData = referenceData;
		initBinder();
		errorMessages = new ArrayList<String>();
		
		insuranceCompanyName = (TextField) binder.buildAndBind(
				"Previous Insurance Company Name", "otherClaimCompanyName", TextField.class);
		insuranceCompanyName.setMaxLength(100);		
		CSValidator validator = new CSValidator();
		validator.extend(insuranceCompanyName);
		validator.setRegExp("[a-zA-Z 0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		commencementDate = (DateField) binder.buildAndBind(
				"Date of Commencement of First Ins Without break", "otherClaimCommencementDate", DateField.class);
		
		policyNumber = (TextField) binder.buildAndBind(
				"Policy No", "otherClaimPolicyNumber", TextField.class);
		CSValidator policyNumberValidator = new CSValidator();
		policyNumberValidator.extend(policyNumber);
		policyNumberValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		policyNumberValidator.setPreventInvalidTyping(true);
		policyNumber.setMaxLength(30);
		
		otherClaimsumInsured = (TextField) binder.buildAndBind(
				"Sum Insured", "otherClaimSumInsured", TextField.class);
		otherClaimsumInsured.setMaxLength(15);
		CSValidator otherSIValidator = new CSValidator();
		otherSIValidator.extend(otherClaimsumInsured);
		otherSIValidator.setRegExp("^[0-9]*$");
		otherSIValidator.setPreventInvalidTyping(true);
		
		last4yearsHospitalisation = (OptionGroup) binder.buildAndBind(
				"Have you been Hospitalized in the last 4 years", "last4YearsHospitalisation", OptionGroup.class);
		last4yearsHospitalisation.addItems(getReadioButtonOptions());
		last4yearsHospitalisation.setItemCaption(true, "Yes");
		last4yearsHospitalisation.setItemCaption(false, "No");
		last4yearsHospitalisation.setStyleName("horizontal");
		tableLayout = new VerticalLayout();
		addListener();
		if(bean.getPreauthDataExtractionDetails().getOtherClaimDetails() != null && bean.getPreauthDataExtractionDetails().getOtherClaimDetails().getLast4YearsHospitalisation() != null && bean.getPreauthDataExtractionDetails().getOtherClaimDetails().getLast4YearsHospitalisation()) {
			showOtherClaimsTable(true);
		}
		
		FormLayout layout = new FormLayout(new FormLayout(insuranceCompanyName, commencementDate, policyNumber, otherClaimsumInsured , last4yearsHospitalisation));
		mandatoryFields.add(insuranceCompanyName);
		showOrHideValidation(false);
		VerticalLayout wholeLayout = new VerticalLayout(layout, tableLayout);
		setCompositionRoot(wholeLayout);
	}
	
	
	private void addListener() {
		last4yearsHospitalisation.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				showOtherClaimsTable(isChecked);
			}
		});
	}
	
	private void showOtherClaimsTable(Boolean isChecked) {
		tableLayout.removeAllComponents();
		if(isChecked) {
			otherClaimTableObj = otherClaimTable.get();
			otherClaimTableObj.init("Other Claim Details", true);
			otherClaimTableObj.setReference(referenceData);
			
			if(otherClaimTableObj != null) {
				List<OtherClaimDiagnosisDTO> otherClaimDiagList = bean.getPreauthDataExtractionDetails().getOtherClaimDetailsList();
				for (OtherClaimDiagnosisDTO otherClaimDiagnosisDTO : otherClaimDiagList) {
					otherClaimTableObj.addBeanToList(otherClaimDiagnosisDTO);
				}
				
				tableLayout.addComponent(otherClaimTableObj);
			}
			
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void setTableValues() {
		if(otherClaimTableObj != null) {
			this.bean.getPreauthDataExtractionDetails().setOtherClaimDetailsList(otherClaimTableObj.getValues());
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
		this.binder = new BeanFieldGroup<OtherClaimDetailsDTO>(OtherClaimDetailsDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthDataExtractionDetails().getOtherClaimDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	


}
