package com.shaic.reimbursement.investigationmaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.RadioButton;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.fieldVisitPage.FieldVisitPagePresenter;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeTableDTO;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.themes.Reindeer;

@UIScoped
public class InvestigationMasterUI  extends ViewComponent{
	


	private ComboBox cmbInvestigatorType;
	
	private TextField txtInvestigatorName;

	private ComboBox cmbGender;
	
	private TextField txtMobileNumber;

	private TextField txtAdditionalMobileNumber;

	private TextField txtPhoneNumber;

	private OptionGroup status;
	
	private ComboBox cmbState;
	
	private ComboBox cmbCity;

	private TextField txtBranchCode;

	private TextField txtStarEmployeeID;
	
	private TextField txtEmailID;
	
	private TextField txtAllocationCount;
	
	private TextField txtStarCoordinatorCode;
	
	private TextField txtStarCoordinatorName;
	
	private TextField txtConsultancyName;
	
	private TextField txtContactPerson;
	
	private ComboBox cmbZoneName;
	
	//private TextField txtZoneName;
	
	private TextArea toEmailID;
	
	private TextArea ccEmailID;
	
	private Button btnSubmit;
	
	private Button btnCancel;

	private FormLayout investigationFormLayout;

	private VerticalLayout mainLayout;
	
	private BeanFieldGroup<InvestigationMasterDTO> binder;

	private InvestigationMasterDTO bean;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private BeanItemContainer<SelectValue> stateContainer;
	
	private BeanItemContainer<SelectValue> investigatorTypeContainer;

	private BeanItemContainer<SelectValue> cityContainer;

	private BeanItemContainer<SelectValue> zoneContainer;
	
	private BeanItemContainer<SelectValue> genderContainer;
	
	@EJB
	private MasterService masterService;


	public void init(InvestigationMasterDTO bean,GWizard wizard) {
		this.bean = bean;
		
		this.binder = new BeanFieldGroup<InvestigationMasterDTO>(
				InvestigationMasterDTO.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		VerticalLayout vLayout = new VerticalLayout(invetigationMasterDetailsPanel(),buildButtonLayout());
		vLayout.setSpacing(true);
		vLayout.setMargin(false);
		
		//mainLayout = new VerticalLayout(vLayout);
		addListener();
		setValues();
		setCompositionRoot(vLayout);
	}

	
	private VerticalLayout invetigationMasterDetailsPanel()
	{
		
		cmbInvestigatorType = (ComboBox) binder.buildAndBind("Investigator Type","investigatorTypeSelectValue", ComboBox.class);
		investigatorTypeContainer= masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION);
		cmbInvestigatorType.setContainerDataSource(investigatorTypeContainer);
		cmbInvestigatorType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInvestigatorType.setItemCaptionPropertyId("value");
		if(this.bean.getInvestigatorTypeSelectValue() !=null){
			cmbInvestigatorType.setValue(this.bean.getInvestigatorTypeSelectValue());
		}
		cmbInvestigatorType.setEnabled(true);
		cmbInvestigatorType.setRequired(true);
	
		txtInvestigatorName = (TextField) binder.buildAndBind("Investigator Name","investigatorName", TextField.class);
		txtInvestigatorName.setEnabled(true);
		txtInvestigatorName.setRequired(true);
		txtInvestigatorName.setMaxLength(50);
		CSValidator txtInvestigatorNameValidator = new CSValidator();
		txtInvestigatorNameValidator.extend(txtInvestigatorName);
		txtInvestigatorNameValidator.setRegExp("^[a-zA-Z , .]*$");
		txtInvestigatorNameValidator.setPreventInvalidTyping(true);
		
		cmbGender = (ComboBox) binder.buildAndBind("Gender ","gender", ComboBox.class);
		genderContainer=masterService.getSelectValueContainer(ReferenceTable.GENDER);
		cmbGender.setContainerDataSource(genderContainer);
		cmbGender.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbGender.setItemCaptionPropertyId("value");
		if(bean.getGender()!=null){
			cmbGender.setValue(this.bean.getGender());
		}
		cmbGender.setEnabled(true);
		cmbGender.setRequired(true);
		
		
		txtMobileNumber = (TextField) binder.buildAndBind("Mobile Number","mobileNumber", TextField.class);
		txtMobileNumber.setEnabled(true);
		txtMobileNumber.setRequired(true);
		txtMobileNumber.setNullRepresentation("");
		txtMobileNumber.setMaxLength(10);
		CSValidator mobileValidator=new CSValidator();
		mobileValidator.extend(txtMobileNumber);
		mobileValidator.setRegExp("^[0-9/]*$");
		mobileValidator.setPreventInvalidTyping(true);
		
		txtAdditionalMobileNumber = (TextField) binder.buildAndBind("Additional Mobile Number","additionalMobileNumber", TextField.class);
		txtAdditionalMobileNumber.setNullRepresentation("");
		txtAdditionalMobileNumber.setMaxLength(10);
		CSValidator txtAdditionalMobileNumberValidator=new CSValidator();
		txtAdditionalMobileNumberValidator.extend(txtAdditionalMobileNumber);
		txtAdditionalMobileNumberValidator.setRegExp("^[0-9/]*$");
		txtAdditionalMobileNumberValidator.setPreventInvalidTyping(true);
		
		txtPhoneNumber = (TextField) binder.buildAndBind("Phone Number","phoneNumber", TextField.class);
		txtAdditionalMobileNumber.setNullRepresentation("");
		txtPhoneNumber.setMaxLength(16);
		CSValidator txtPhoneNumberValidator=new CSValidator();
		txtPhoneNumberValidator.extend(txtPhoneNumber);
		txtPhoneNumberValidator.setRegExp("^[0-9/]*$");
		txtPhoneNumberValidator.setPreventInvalidTyping(true);
		
		status = (OptionGroup) binder.buildAndBind(
				"status", "status", OptionGroup.class);
		status.addItems(getReadioButtonOptions());
		status.setItemCaption(true, "Active");
		status.setItemCaption(false, "InActive");
		status.setStyleName("horizontal");
		if(bean.getStatus()!=null && bean.getStatus().equals(Boolean.FALSE)){
			status.setValue(false);
		}else{
			status.setValue(true);
		}
		
		status.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
					bean.setStatus(true);
				} 
			}
		});
		
		cmbState = (ComboBox) binder.buildAndBind("State","state", ComboBox.class);
		List<SelectValue> stateContainerList = new ArrayList<SelectValue>();
		List<State> stateList = masterService.getStateList();
		if (!stateList.isEmpty()) {
			SelectValue stateSelectValue = null;
			for (State state : stateList) {
				stateSelectValue = new SelectValue();
				stateSelectValue.setId(state.getKey());
				stateSelectValue.setValue(state.getValue());
				stateContainerList.add(stateSelectValue);
			}
		}
		stateContainer=new BeanItemContainer<SelectValue>(stateContainerList);
		cmbState.setContainerDataSource(stateContainer);
		cmbState.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbState.setItemCaptionPropertyId("value");
		if(this.bean.getState()!=null){
			cmbState.setValue(bean.getState());
		}
		cmbState.setEnabled(true);
		cmbState.setRequired(true);
		
		cmbCity = (ComboBox) binder.buildAndBind("City","city", ComboBox.class);
		cmbCity.setEnabled(true);
		cmbCity.setRequired(true);
		
		txtBranchCode = (TextField) binder.buildAndBind("Branch code","branchCode", TextField.class);
		txtBranchCode.setMaxLength(10);
		txtBranchCode.setEnabled(true);
		CSValidator txtBranchCodeValidator = new CSValidator();
		txtBranchCodeValidator.extend(txtBranchCode);
		txtBranchCodeValidator.setRegExp("^[a-zA-Z 0-9]*$");
		txtBranchCodeValidator.setPreventInvalidTyping(true);
		
		txtStarEmployeeID = (TextField) binder.buildAndBind("Star Employee ID","starEmployeeID", TextField.class);
		txtStarEmployeeID.setEnabled(true);
		txtStarEmployeeID.setRequired(true);
		txtStarEmployeeID.setMaxLength(10);
		CSValidator txtStarEmployeeIDValidator = new CSValidator();
		txtStarEmployeeIDValidator.extend(txtStarEmployeeID);
		txtStarEmployeeIDValidator.setRegExp("^[a-zA-Z 0-9]*$");
		txtStarEmployeeIDValidator.setPreventInvalidTyping(true);
		
		
		txtEmailID = (TextField) binder.buildAndBind("Email ID","emailID", TextField.class);
		txtEmailID.setEnabled(true);
		txtEmailID.setRequired(true);
		txtEmailID.setMaxLength(150);
		
		txtAllocationCount = (TextField) binder.buildAndBind("Max Allocation Count","allocationCount", TextField.class);
		txtAllocationCount.setEnabled(true);
		txtAllocationCount.setRequired(true);
		txtAllocationCount.setMaxLength(2);
		CSValidator txtAllocationCountValidator = new CSValidator();
		txtAllocationCountValidator.extend(txtAllocationCount);
		txtAllocationCountValidator.setRegExp("^[1-9/]*$");
		txtAllocationCountValidator.setPreventInvalidTyping(true);
		
		txtStarCoordinatorCode = (TextField) binder.buildAndBind("Star Coordinator Code","coordinatorCode", TextField.class);
		txtStarCoordinatorCode.setEnabled(true);
		txtStarCoordinatorCode.setRequired(true);
		txtStarCoordinatorCode.setMaxLength(10);
		CSValidator txtStarCoordinatorCodeValidator = new CSValidator();
		txtStarCoordinatorCodeValidator.extend(txtStarCoordinatorCode);
		txtStarCoordinatorCodeValidator.setRegExp("^[a-zA-Z 0-9]*$");
		txtStarCoordinatorCodeValidator.setPreventInvalidTyping(true);
		
		txtStarCoordinatorName = (TextField) binder.buildAndBind("Star Coordinator Name","coordinatorName", TextField.class);
		txtStarCoordinatorName.setEnabled(true);
		txtStarCoordinatorName.setRequired(true);
		txtStarCoordinatorName.setMaxLength(50);
		CSValidator txtStarCoordinatorNameValidator = new CSValidator();
		txtStarCoordinatorNameValidator.extend(txtStarCoordinatorName);
		txtStarCoordinatorNameValidator.setRegExp("^[a-zA-Z , .]*$");
		txtStarCoordinatorNameValidator.setPreventInvalidTyping(true);
		
		txtConsultancyName = (TextField) binder.buildAndBind("Consultancy Name","consultancyName", TextField.class);
		txtConsultancyName.setMaxLength(100);
		txtConsultancyName.setEnabled(true);
		txtConsultancyName.setRequired(true);
		CSValidator txtConsultancyNameValidator = new CSValidator();
		txtConsultancyNameValidator.extend(txtConsultancyName);
		txtConsultancyNameValidator.setRegExp("^[a-zA-Z , .]*$");
		txtConsultancyNameValidator.setPreventInvalidTyping(true);
		
		txtContactPerson = (TextField) binder.buildAndBind("Contact Person","contactPerson", TextField.class);
		txtContactPerson.setEnabled(true);
		txtContactPerson.setRequired(true);
		txtContactPerson.setMaxLength(50);
		CSValidator txtContactPersonValidator = new CSValidator();
		txtContactPersonValidator.extend(txtContactPerson);
		txtContactPersonValidator.setRegExp("^[a-zA-Z , .]*$");
		txtContactPersonValidator.setPreventInvalidTyping(true);
		
		cmbZoneName = (ComboBox) binder.buildAndBind("Investigation Zone Name","investigationZoneName", ComboBox.class);
		zoneContainer= masterService.getInvestigatorZoneNames();
		cmbZoneName.setContainerDataSource(zoneContainer);
		cmbZoneName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbZoneName.setItemCaptionPropertyId("value");
		if(bean.getInvestigationZoneName()!=null){
			cmbZoneName.setValue(bean.getInvestigationZoneName());
		}
		cmbZoneName.setEnabled(true);
		cmbZoneName.setRequired(true);
		
		toEmailID = (TextArea) binder.buildAndBind("To Email ID","toEmailID", TextArea.class);
		toEmailID.setEnabled(true);
		toEmailID.setRequired(true);
		toEmailID.setMaxLength(500);
		
		ccEmailID = (TextArea) binder.buildAndBind("CC Email ID","ccEmailID", TextArea.class);
		ccEmailID.setEnabled(true);
		ccEmailID.setRequired(true);
		ccEmailID.setMaxLength(1000);

		FormLayout leftFormLayout = new FormLayout(cmbInvestigatorType , txtInvestigatorName,cmbGender,txtMobileNumber,txtAdditionalMobileNumber,txtPhoneNumber,status);
		FormLayout middleFormLayout = new FormLayout(cmbState,cmbCity,txtBranchCode,txtStarEmployeeID,txtEmailID,txtAllocationCount);
		FormLayout rightFormLayout = new FormLayout(txtStarCoordinatorCode , txtStarCoordinatorName,txtConsultancyName,txtContactPerson,cmbZoneName,toEmailID,ccEmailID);
		
		HorizontalLayout hLayout = new HorizontalLayout(leftFormLayout,middleFormLayout,rightFormLayout);
		VerticalLayout vLayout = new VerticalLayout(hLayout);
		vLayout.setSpacing(true);
		vLayout.setMargin(false);
		
		return vLayout;
	}


	private void addListener() {

		cmbInvestigatorType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private")) {
					cmbState.setValue(null);
					cmbCity.setValue(null);
					txtBranchCode.setValue(null);
					txtStarEmployeeID.setValue(null);
					txtAllocationCount.setValue("0");
					
					cmbState.setEnabled(false);
					cmbCity.setEnabled(false);
					txtBranchCode.setEnabled(false);
					txtStarEmployeeID.setEnabled(false);
					txtAllocationCount.setEnabled(false);
					
					cmbState.setRequired(false);
					cmbCity.setRequired(false);
					txtStarEmployeeID.setRequired(false);
					txtAllocationCount.setRequired(false);
				}else{
					cmbState.setEnabled(true);
					cmbCity.setEnabled(true);
					txtBranchCode.setEnabled(true);
					txtStarEmployeeID.setEnabled(true);
					txtAllocationCount.setEnabled(true);
					
					cmbState.setRequired(true);
					cmbCity.setRequired(true);
					txtStarEmployeeID.setRequired(true);
					txtAllocationCount.setRequired(true);
				}
				if(value != null && !(cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private"))){
					
					txtStarCoordinatorCode.setValue(null);
					txtStarCoordinatorName.setValue(null);
					txtConsultancyName.setValue(null);
					txtContactPerson.setValue(null);
					cmbZoneName.setValue(null);
					toEmailID.setValue(null);
					ccEmailID.setValue(null);
					
					txtStarCoordinatorCode.setEnabled(false);
					txtStarCoordinatorName.setEnabled(false);
					txtConsultancyName.setEnabled(false);
					txtContactPerson.setEnabled(false);
					cmbZoneName.setEnabled(false);
					toEmailID.setEnabled(false);
					ccEmailID.setEnabled(false);
					
					txtStarCoordinatorCode.setRequired(false);
					txtStarCoordinatorName.setRequired(false);
					txtConsultancyName.setRequired(false);
					txtContactPerson.setRequired(false);
					cmbZoneName.setRequired(false);
					toEmailID.setRequired(false);
					ccEmailID.setRequired(false);
					
				}else{
					
					txtStarCoordinatorCode.setEnabled(true);
					txtStarCoordinatorName.setEnabled(true);
					txtConsultancyName.setEnabled(true);
					txtContactPerson.setEnabled(true);
					cmbZoneName.setEnabled(true);
					toEmailID.setEnabled(true);
					ccEmailID.setEnabled(true);
					
					txtStarCoordinatorCode.setRequired(true);
					txtStarCoordinatorName.setRequired(true);
					txtConsultancyName.setRequired(true);
					txtContactPerson.setRequired(true);
					cmbZoneName.setRequired(true);
					toEmailID.setRequired(true);
					ccEmailID.setRequired(true);
				}
			}
		});
		
		cmbState.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					SelectValue state = (SelectValue) cmbState.getValue();
					Long state_Id = state.getId();
					fireViewEvent(InvestigationMasterWizardPresenter.GET_INVESTIGATION_MASTER_CITY, state_Id);
				}catch(Exception e){
					cmbCity.setContainerDataSource(null);
//					cmbBrachOffice.setContainerDataSource(null);
//					cmbAllocationTo.setValue(null);
				}
			}
		});
	
	btnSubmit.addClickListener(new Button.ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
				//binder.commit();
			if(validatePage())
			{
				//setTableValuesDTO();
				String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				//bean.setStrUserName(userName);
				fireViewEvent(InvestigationMasterWizardPresenter.INVESTIGATION_MASTER_WIZARD_SUBMIT,bean);
			
			}
		}
		
	});
	
	btnCancel.addClickListener(new Button.ClickListener() {

		private static final long serialVersionUID = 5677998363425252239L;

		@Override
		public void buttonClick(ClickEvent event) {
			ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Are you sure you want to cancel ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										fireViewEvent(MenuItemBean.INVESTIGATION_MASTER,
												null);
									} else {
										// User did not confirm
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
		}
	
	});
}

	private void setValues(){
		if(this.bean.getInvestigatorTypeSelectValue()!=null && this.bean.getInvestigatorTypeSelectValue().getValue().toString().equalsIgnoreCase("Private")){
				cmbState.setEnabled(false);
				cmbCity.setEnabled(false);
				txtBranchCode.setEnabled(false);
				txtStarEmployeeID.setEnabled(false);
				txtAllocationCount.setEnabled(false);
				
				cmbState.setRequired(false);
				cmbCity.setRequired(false);
				txtStarEmployeeID.setRequired(false);
				txtAllocationCount.setRequired(false);
			}
			if(this.bean.getInvestigatorTypeSelectValue()!=null && !this.bean.getInvestigatorTypeSelectValue().getValue().toString().equalsIgnoreCase("Private")){
				txtStarCoordinatorCode.setEnabled(false);
				txtStarCoordinatorName.setEnabled(false);
				txtConsultancyName.setEnabled(false);
				txtContactPerson.setEnabled(false);
				cmbZoneName.setEnabled(false);
				toEmailID.setEnabled(false);
				ccEmailID.setEnabled(false);
				
				txtStarCoordinatorCode.setRequired(false);
				txtStarCoordinatorName.setRequired(false);
				txtConsultancyName.setRequired(false);
				txtContactPerson.setRequired(false);
				cmbZoneName.setRequired(false);
				toEmailID.setRequired(false);
				ccEmailID.setRequired(false);
			}
	}


	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("Submit");
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
		hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}
	

	
	public void addCity(Long stateKey,SelectValue value) {

		fireViewEvent(InvestigationMasterWizardPresenter.GET_INVESTIGATION_MASTER_CITY, stateKey,value);
		
	}


	public void setCityContainer(
			BeanItemContainer<SelectValue> citySelectValueContainer) {
		
		if(cityContainer == null){
			cityContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		}
		
		cityContainer = citySelectValueContainer;
		
		cmbCity.setContainerDataSource(cityContainer);
		cmbCity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCity.setItemCaptionPropertyId("value");

		/*for(int i=0;i<cityContainer.size();i++){
			if (selectedCity != null) {
				if(cityContainer.getIdByIndex(i).getValue().equalsIgnoreCase(selectedCity.getValue())){
					cmbCity.setValue(cityContainer.getIdByIndex(i));
					break;
				}	
			}
		}*/
	}
	
	/*public void setInvestigatorTypeContainer(
			BeanItemContainer<SelectValue> InvestigatorTypeSelectContainer,SelectValue selectedCity) {
		
		investigatorTypeContainer = InvestigatorTypeSelectContainer;
		
		cmbInvestigatorType.setContainerDataSource(investigatorTypeContainer);
		cmbInvestigatorType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInvestigatorType.setItemCaptionPropertyId("value");

		for(int i=0;i<investigatorTypeContainer.size();i++){
			if (selectedCity != null) {
				if(investigatorTypeContainer.getIdByIndex(i).getValue().equalsIgnoreCase(selectedCity.getValue())){
					cmbInvestigatorType.setValue(investigatorTypeContainer.getIdByIndex(i));
					break;
				}	
			}
		}
	}*/

	public boolean validatePage() {
		
		Boolean hasError = false;
		Boolean errorMsg = Boolean.FALSE;
		StringBuffer eMsg = new StringBuffer();
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
			 if(cmbInvestigatorType !=null && cmbInvestigatorType.getValue() == null) 
			 {
				 hasError = true;
				 eMsg.append("Please Select the Investigator Type</br>");
			 }
			 if(txtInvestigatorName ==null || txtInvestigatorName.isEmpty()) 
			 {
				 hasError = true;
				 eMsg.append("Please Enter Investigator Name</br>");
			 }
			 if(cmbGender !=null && cmbGender.getValue() == null) 
			 {
				 hasError = true;
				 eMsg.append("Please Select Gender</br>");
			 }
			 if(txtMobileNumber ==null || txtMobileNumber.isEmpty() && txtMobileNumber.toString().length() !=10 ) 
			 {
				 hasError = true;
				 eMsg.append("Please Enter Valid Mobile Number</br>");
			 }
			 
			 if(cmbInvestigatorType !=null && cmbInvestigatorType.getValue() !=null && !(cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private"))){

				 if(cmbState !=null && cmbState.getValue() == null) 
				 {
					 hasError = true;
					 eMsg.append("Please Select State</br>");
				 }
				 if(cmbCity !=null && cmbCity.getValue() == null) 
				 {
					 hasError = true;
					 eMsg.append("Please Select City</br>");
				 }

				 if(txtStarEmployeeID ==null || txtStarEmployeeID.isEmpty()) 
				 {
					 hasError = true;
					 eMsg.append("Please Enter Star Eamployee ID</br>");
				 }

				 if(txtAllocationCount ==null || txtAllocationCount.isEmpty()) 
				 {
					 hasError = true;
					 eMsg.append("Please Enter Max Count</br>");
				 }
			 }
			 if(txtEmailID ==null || txtEmailID.isEmpty()) 
			 {
				 hasError = true;
				 eMsg.append("Please Enter Email ID</br>");
			 }
			 
				if(null != this.txtEmailID && null != this.txtEmailID.getValue() && !("").equalsIgnoreCase(this.txtEmailID.getValue()))
				{
					if(!SHAUtils.isValidEmail(this.txtEmailID.getValue()))
					{
						hasError = true;
						eMsg.append("Please Enter Valid Email ID </br>");
					}
				}
			 
				
				if(cmbInvestigatorType !=null && cmbInvestigatorType.getValue()!=null && cmbInvestigatorType.getValue().toString().equalsIgnoreCase("Private")){
					if(txtStarCoordinatorCode ==null || txtStarCoordinatorCode.isEmpty()) 
					{
						hasError = true;
						eMsg.append("Please Enter Star Coordinator Code</br>");
					}

					if(txtStarCoordinatorName ==null || txtStarCoordinatorName.isEmpty()) 
					{
						hasError = true;
						eMsg.append("Please Enter Star Coordinator Name</br>");
					}
					if(txtConsultancyName ==null || txtConsultancyName.isEmpty()) 
					{
						hasError = true;
						eMsg.append("Please Enter Consultancy Name</br>");
					}
					if(txtContactPerson ==null || txtContactPerson.isEmpty()) 
					{
						hasError = true;
						eMsg.append("Please Enter Contact Person Name</br>");
					}

					if(cmbZoneName !=null && cmbZoneName.getValue() == null) 
					{
						hasError = true;
						eMsg.append("Please Select Investigation Zone Name </br>");
					}

					if(toEmailID ==null || toEmailID.isEmpty()){
						eMsg.append("Please Enter To Email ID</br>");
						hasError = true;

					}
					if(toEmailID ==null && toEmailID.isEmpty()){
						String recipientTOEmailID =ccEmailID.getValue();
						if(recipientTOEmailID != null && !(recipientTOEmailID.isEmpty()))
						{
							String[] listToEmailIDs = recipientTOEmailID.split(";");
							Boolean bValidEmail = SHAUtils.ValidateEmailAddresses(listToEmailIDs);

							if(!bValidEmail)
							{
								eMsg.append("Atleast one email ID in the TO EmailID field is invalid. Please enter Valid email</br>");
								hasError = true;
							}
						}
					}

					if(ccEmailID ==null || ccEmailID.isEmpty()){
						eMsg.append("Please Enter CC Email ID</br>");
						hasError = true;

					}
					if(ccEmailID ==null && ccEmailID.isEmpty()){
						String recipientCCEmailID =ccEmailID.getValue();
						if(recipientCCEmailID != null && !(recipientCCEmailID.isEmpty()))
						{
							String[] listToEmailIDs = recipientCCEmailID.split(";");
							Boolean bValidEmail = SHAUtils.ValidateEmailAddresses(listToEmailIDs);

							if(!bValidEmail)
							{
								eMsg.append("Atleast one email ID in the  To field is invalid. Please enter Valid email</br>");
								hasError = true;
							}
						}
					}
				}
				
			 
		if (hasError) {
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			btnSubmit.setDisableOnClick(false);
			errorMsg = Boolean.FALSE;
		} 
		else 
		{
			btnSubmit.setDisableOnClick(true);
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			errorMsg = Boolean.TRUE;
		}	
		
		return errorMsg;
	}
	
    protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	



}
