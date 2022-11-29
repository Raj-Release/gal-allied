package com.shaic.claim.registration.updateHospitalDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.pages.IntimationDetailsPage;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

public class UpdateHospitalDetailsViewImpl extends AbstractMVPView implements UpdateHospitalDetailsView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Inject
    private UpdateHospitalDetailsDTO bean;
	
	@EJB
	private UpdateHospitalService updateHospitalService;
	
	@EJB
	private MasterService masterService;

	private BeanFieldGroup<UpdateHospitalDetailsDTO> binder;
	
//	private List<Locality> areaList;

	public ComboBox state;
	
	public ComboBox city;
	
	private HospitalDto selectedHospital;
	
	/*private AutocompleteField<HospitalDto> cmbHospital;*/
	private AutocompleteTextField cmbHospitals;

	//private TextField hospitalName;

	
	private ComboBox cmbHospitalType;

//	private ComboBox area;

	private TextField pinCode;
	
	private ComboBox modeOfIntimation;
	
	private ComboBox intimatedBy;

	private Button updateBtn;

	private Button cancelBtn;

	private FormLayout firstLayout;

	private FormLayout secondLayout;

	private HorizontalLayout horizonMain;

	private HorizontalLayout buttonLayout;
	
	private TextField hospitalCodeTxt;
	
	private TextField hospitalCodeIrdaTxt;
	
	private TextArea txtHospitalAddress;
	
	private TextField txtHospitalPinCode;
	
	private TextField txtHospitalContactNumber;
	
	private TextField txtHospitalFaxNumber;
	
	private TextField txtHospitalMobileNumber;
	
	private TextField txtHospitalEmailId;
	
	private TextArea txtHospitalRemarks;
	
	private Panel mainPanel;

	private VerticalLayout vertical;

	private IntimationDetailsPage parent;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private BeanItemContainer<SelectValue> modeOfIntimations;
	
	private BeanItemContainer<SelectValue> intimated;
	
//	private List<SelectValue> locality=new ArrayList<SelectValue>();
	
	private List<Component> mandatoryFields = new ArrayList<Component>();
	
	@EJB
	private HospitalService hospitalService;
	
	@PostConstruct
	public void initView() {
		
		this.binder = new BeanFieldGroup<UpdateHospitalDetailsDTO>(
				UpdateHospitalDetailsDTO.class);
		this.binder.setItemDataSource(this.bean);
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		state = (ComboBox) binder.buildAndBind("State", "state",
				ComboBox.class);
		state.setTabIndex(2);
		
		state.setContainerDataSource(this.selectValueContainer);
		state.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		state.setItemCaptionPropertyId("value");
		
		state.setTabIndex(3);
		
		
		
		modeOfIntimation=(ComboBox) binder.buildAndBind("Mode of Intimation", "modeOfIntimation",
				ComboBox.class);
		modeOfIntimation.setTabIndex(1);
		
		modeOfIntimation.setContainerDataSource(this.modeOfIntimations);
		modeOfIntimation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		modeOfIntimation.setItemCaptionPropertyId("value");
		
		
		
		intimatedBy=(ComboBox) binder.buildAndBind("Intimated By", "intimatedBy",
				ComboBox.class);
		
//		area=(ComboBox) binder.buildAndBind("Area", "area",
//				ComboBox.class);
//		area.setTabIndex(8);
		
		city=(ComboBox) binder.buildAndBind("City", "city",
				ComboBox.class);
		
		city.setTabIndex(4);
		
		intimatedBy.setContainerDataSource(this.intimated);
		intimatedBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		intimatedBy.setItemCaptionPropertyId("value");
		
		intimatedBy.setTabIndex(2);
		
//		hospitalName = (TextField) binder.buildAndBind("Hospital Name",
//				"hospitalName", TextField.class);
		
		cmbHospitalType = (ComboBox) binder.buildAndBind("Hospital Type",
				"hospitalType", ComboBox.class);
		cmbHospitalType.setWidth("180px");
		cmbHospitalType.setTabIndex(6);
		
		
		cmbHospitalType.setContainerDataSource(masterService.getConversionReasonByValue(ReferenceTable.HOSPITAL_TYPE));
		cmbHospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalType.setItemCaptionPropertyId("value");
		
		hospitalCodeTxt = new TextField("Hospital Code (Internal)");
		hospitalCodeTxt.setWidth("180px");
		hospitalCodeTxt.setNullRepresentation("");
		hospitalCodeTxt.setMaxLength(15);
		CSValidator designationValid = new CSValidator();
		designationValid.extend(hospitalCodeTxt);
		designationValid.setRegExp("^[a-zA-Z 0-9]*$");
		designationValid.setPreventInvalidTyping(true);
		hospitalCodeTxt.setTabIndex(15);

		hospitalCodeIrdaTxt = new TextField("Hospital Code (IRDA)");
		hospitalCodeIrdaTxt.setWidth("180px");
		hospitalCodeIrdaTxt.setNullRepresentation("");
		hospitalCodeIrdaTxt.setMaxLength(15);
		CSValidator irdaValid = new CSValidator();
		irdaValid.extend(hospitalCodeIrdaTxt);
		irdaValid.setRegExp("^[a-zA-Z 0-9]*$");
		irdaValid.setPreventInvalidTyping(true);
		hospitalCodeIrdaTxt.setTabIndex(16);

		txtHospitalAddress = new TextArea();
		txtHospitalAddress.setCaption("Address");
		txtHospitalAddress.setWidth("180px");
		txtHospitalAddress.setTabIndex(7);

		CSValidator pinCodeValidator=new CSValidator();
		txtHospitalPinCode = new TextField();
		txtHospitalPinCode.setCaption("Pin Code");
		txtHospitalPinCode.setMaxLength(6);
		txtHospitalPinCode.setWidth("180px");
		txtHospitalPinCode.setNullRepresentation("");
		pinCodeValidator.extend(txtHospitalPinCode);
		pinCodeValidator.setRegExp("^[0-9/]*$");
		pinCodeValidator.setPreventInvalidTyping(true);
		txtHospitalPinCode.setTabIndex(9);

		CSValidator contactNumValidator=new CSValidator();
		txtHospitalContactNumber = new TextField();
		txtHospitalContactNumber.setCaption("Contact No");
		txtHospitalContactNumber.setMaxLength(10);
		txtHospitalContactNumber.setWidth("180px");
		//Vaadin8-setImmediate() txtHospitalContactNumber.setImmediate(true);
		txtHospitalContactNumber.setNullRepresentation("");
		contactNumValidator.extend(txtHospitalContactNumber);
		contactNumValidator.setRegExp("^[0-9/]*$");
		contactNumValidator.setPreventInvalidTyping(true);
		txtHospitalContactNumber.setTabIndex(11);

		
		CSValidator faxNoValidator=new CSValidator();
		txtHospitalFaxNumber = new TextField();
		//Vaadin8-setImmediate() txtHospitalFaxNumber.setImmediate(true);
		txtHospitalFaxNumber.setCaption("Fax No");
		txtHospitalFaxNumber.setNullRepresentation("");
		txtHospitalFaxNumber.setMaxLength(12);
		txtHospitalFaxNumber.setWidth("180px");
		faxNoValidator.extend(txtHospitalFaxNumber);
		faxNoValidator.setRegExp("^[0-9/]*$");
		faxNoValidator.setPreventInvalidTyping(true);
		txtHospitalFaxNumber.setTabIndex(12);
		

		CSValidator mobileValidator=new CSValidator();
		txtHospitalMobileNumber = new TextField();
		txtHospitalMobileNumber.setCaption("Mobile No");
		txtHospitalMobileNumber.setNullRepresentation("");
		txtHospitalMobileNumber.setMaxLength(12);
		//Vaadin8-setImmediate() txtHospitalMobileNumber.setImmediate(true);
		txtHospitalMobileNumber.setWidth("180px");
		txtHospitalMobileNumber.setNullRepresentation("");
		mobileValidator.extend(txtHospitalMobileNumber);
		mobileValidator.setRegExp("^[0-9/]*$");
		mobileValidator.setPreventInvalidTyping(true);
		txtHospitalMobileNumber.setTabIndex(13);

		txtHospitalEmailId = new TextField();
		txtHospitalEmailId.setCaption("Email ID");
		txtHospitalEmailId.setWidth("180px");
		txtHospitalEmailId.setNullRepresentation("");
		txtHospitalEmailId.setTabIndex(14);
		
		txtHospitalRemarks=new TextArea();
		txtHospitalRemarks.setCaption("Remarks(If any)");
		txtHospitalRemarks.setNullRepresentation("");
		txtHospitalRemarks.setTabIndex(10);
//		CSValidator designationValid = new CSValidator();
//		designationValid.extend(txtHospitalRemarks);
//		designationValid.setRegExp("^[a-zA-Z 0-9.]*$");
//		designationValid.setPreventInvalidTyping(true);
	
		
		updateBtn = new Button("Update");
		
		cancelBtn = new Button("Cancel");
		
		updateBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		updateBtn.setWidth("-1px");
		updateBtn.setHeight("-10px");
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		/*CSValidator hospitalNameValidator=new CSValidator();
		cmbHospital = new AutocompleteField<HospitalDto>();
		CSValidator hospitalNameValidator=new CSValidator();

		/*cmbHospital = new AutocompleteField<HospitalDto>();

<<<<<<< HEAD
>>>>>>> a1b28df37a8ac40ebb73f2c4cd51036c598b388a
=======

>>>>>>> 4eea262d64d106bf458191aab9f1de451252fa30
		//Vaadin8-setImmediate() cmbHospital.setImmediate(true);
		cmbHospital.setRequiredError("Please select a valid Hospital");
		cmbHospital.setValidationVisible(true);
		cmbHospital.setCaption("Hospital Name");
		cmbHospital.setWidth("180px");
		hospitalNameValidator.setRegExp("^[A-Z a-z/]*$");
		cmbHospital.setIcon(null);
		cmbHospital.setTabIndex(5);*/
//		CSValidator hospitalValid = new CSValidator();
//		hospitalValid.extend(cmbHospital);
//		hospitalValid.setRegExp("^[a-zA-Z 0-9.]*$");
//		hospitalValid.setPreventInvalidTyping(true);

		
		Collection<String> theJavas = Arrays.asList(new String[] {	
						    "Java",		
						    "JavaScript",		
						    "Join Java",		
						    "JavaFX Script"		
						});		
					/*new Autocom*/		
					
					AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();		
							
					cmbHospitals= new AutocompleteTextField();		
					cmbHospitals.setCaption("Hospital Name");		
					// ===============================		
					// Available configuration options		
					// ===============================		
					//field.setCache(true); // Client side should cache suggestions		
					/*field.setDelay(150); */// Delay before sending a query to the server		
					cmbHospitals.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!		
					cmbHospitals.setMinChars(1); // The required value length to trigger a query		
					cmbHospitals.setScrollBehavior(ScrollBehavior.NONE); // The method that should be used to compensate scrolling of the page		
					cmbHospitals.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied		
					cmbHospitals.setWidth("180px");		
					cmbHospitals.setSuggestionProvider(suggestionProvider);		
							
					cmbHospitals.addListener(e -> {		
					    String text = "Text changed to: " + e.getComponent().getCaption();		
					    Notification.show(text, Notification.Type.TRAY_NOTIFICATION);		
					});		
					cmbHospitals.addSelectListener(this::onAutocompleteSelect);
					cmbHospitals.addValueChangeListener(e -> {		
					   /* String text = "Value changed to: " + ((ValueChangeEvent) e).getProperty().getValue();*/		
					   /* Notification notification = new Notification(		
					            text, Notification.Type.TRAY_NOTIFICATION);		
					    notification.setPosition(Position.BOTTOM_LEFT);		
					    notification.show(Page.getCurrent());		
					    Notification.show(text, Notification.Type.TRAY_NOTIFICATION);*/		
					});

		firstLayout = new FormLayout(modeOfIntimation,state, cmbHospitals,cmbHospitalType,txtHospitalAddress, /*area,*/
				txtHospitalPinCode,txtHospitalRemarks);
		firstLayout.setMargin(true);
		secondLayout = new FormLayout(intimatedBy,city, txtHospitalContactNumber, txtHospitalFaxNumber, txtHospitalMobileNumber,
				txtHospitalEmailId, hospitalCodeTxt, hospitalCodeIrdaTxt);
		secondLayout.setMargin(true);
		buttonLayout = new HorizontalLayout(updateBtn, cancelBtn);
		buttonLayout.setSpacing(true);

		horizonMain = new HorizontalLayout(firstLayout, secondLayout);

		vertical = new VerticalLayout(horizonMain, buttonLayout);
		vertical.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		vertical.setSpacing(true);
			vertical.setWidth("750px");
		mainPanel=new Panel(vertical);
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Update Hospital Details");
		mainPanel.setWidth("100%");
		mainPanel.setHeight("500px");

		horizonMain.setHeight("100%");
		horizonMain.setWidth("100%");
		
		addListener();
		
		mandatoryFields.add(modeOfIntimation);
		mandatoryFields.add(intimatedBy);
		mandatoryFields.add(state);
		mandatoryFields.add(city);
		mandatoryFields.add(cmbHospitalType);
//		mandatoryFields.add(area);
		//mandatoryFields.add(area);
		
		/*setUpAutoHospital(cmbHospital);*/
		
		showOrHideValidation(false);
     
		this.setWidth("100%");
		this.setHeight(600, Unit.PIXELS);
		setCompositionRoot(mainPanel);
//		mandatoryFields.add(txtHospitalMobileNumber);
//		mandatoryFields.add(txtHospitalFaxNumber);
	}

	private void onAutocompleteSelect(AutocompleteEvents.SelectEvent event) {
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		HospitalDto data = (HospitalDto) suggestion.getData();		
		bean.setHospitalType(data.getHospitalType());		
		bean.setHospitalTypeValue(data.getHospitalTypeValue());		
		bean.setHospitalName(data.getName());		
		handleHospitalSelection(data);		
	}
	/*
	private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
=======
	/*private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
		search.setQueryListener(new AutocompleteQueryListener<HospitalDto>() {
			@Override
			public void handleUserQuery(AutocompleteField<HospitalDto> field,String query) {
				if(null != state.getValue() && !("").equals(state) && null != city.getValue() && !("").equals(city)){
				if ( !ValidatorUtils.isNull(state))
				{
					handleHospitalSearchQuery(field, query);	
				}
				}
				else
				{
					Notification.show("Please Select State and City", Notification.TYPE_HUMANIZED_MESSAGE);
				}
			}
		});
	}*/
	
	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		SelectValue selected=new SelectValue();
		State states=null;
		CityTownVillage cities=null;
//		Locality locality=null;
		if(state.getValue()==null || ("").equals(state.getValue()) || city.getValue()==null|| ("").equals(city.getValue())){		
			Notification.show("Please Select Valid State and City", Notification.TYPE_HUMANIZED_MESSAGE);		
		}

		if(state.getValue()!=null){
			
			selected=(SelectValue)state.getValue();
			states=new State();
			
			states.setKey(selected.getId());
			states.setValue(selected.getValue());
			
		}
		if(city.getValue()!=null){
			selected=(SelectValue)city.getValue();
			cities=new CityTownVillage();
			cities.setKey(selected.getId());
			cities.setValue(selected.getValue());
		}
//		if(area.getValue()!=null){
//			selected=(SelectValue)area.getValue();
//			locality=new Locality();
//			locality.setKey(selected.getId());
//			locality.setValue(selected.getValue());
//		}
		
		if(state!=null && city!=null && city.getValue()!=null && state.getValue()!=null){
			List<Hospitals> hospitalSearch = hospitalService.hospitalNameCriteriaSearch(query.getTerm(), states,cities);
			if(!hospitalSearch.isEmpty()){
				List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();		
				for (Hospitals hospital : hospitalSearch) {		
					HospitalDto resultHospitalDto = new HospitalDto(hospital);		
					hospitalDtoList.add(resultHospitalDto);						
				}		

				for (HospitalDto hospitalDto : hospitalDtoList) {		
					AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(hospitalDto.getName());		
					suggestioner.setData(hospitalDto);		
					suggestions.add(suggestioner);		
				}			
			}
		}

		/*List<Hospitals> hospitalSearch = hospitalService
				.hospitalNameCriteriaSearch(query, states,
						cities, null);

		if(!hospitalSearch.isEmpty()){
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (Hospitals hospital : hospitalSearch) {
				
				HospitalDto resultHospitalDto = new HospitalDto(hospital);
				hospitalDtoList.add(resultHospitalDto);				
			}
			
			for (HospitalDto hospitalDto : hospitalDtoList) {
				field.addSuggestion(hospitalDto, hospitalDto.getName());
			}			
		}*/
	}
	public void addListener(){
		
		state.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null !=state.getValue() && !("").equals(state.getValue())){
				SelectValue selected=(SelectValue) state.getValue();
				Long key=selected.getId();
			   fireViewEvent(UpdateHospitalDetailsPresenter.SET_COMBOBOX_VALUE,key);
				}
			}
		});
		
		city.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
//				if(city!=null){
//				SelectValue selected=(SelectValue) city.getValue();
//				if(selected!=null){
//				Long key=selected.getId();
//					fireViewEvent(UpdateHospitalDetailsPresenter.SET_AREA,key);
//				}
//				}
			}
		});
		updateBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
				
				SelectValue states=new SelectValue();
				states=(SelectValue)state.getValue();
				SelectValue cities=new SelectValue();
				cities=(SelectValue)city.getValue();
				SelectValue modeOfIntimate=new SelectValue();
				modeOfIntimate=(SelectValue)modeOfIntimation.getValue();
				SelectValue intimated=new SelectValue();
				intimated=(SelectValue)intimatedBy.getValue();
				if(null != cmbHospitalType.getValue() && !("").equals(cmbHospitalType.getValue())){
				SelectValue hospitalType=new SelectValue();
				hospitalType=(SelectValue)cmbHospitalType.getValue();
				
				bean.setHospitalType(hospitalType);
				}
				bean.setState(states);
				bean.setCity(cities);
				bean.setModeOfIntimation(modeOfIntimate);
				bean.setIntimatedBy(intimated);
//				bean.setHospitalName(cmbHospital.getText());
				//bean.setHospitalName(hospitalName.getValue());
				bean.setAddress(txtHospitalAddress.getValue());
				if(null !=txtHospitalPinCode.getValue() && !("").equals(txtHospitalPinCode.getValue())){
				bean.setPincode(txtHospitalPinCode.getValue());
				}
				if(null !=txtHospitalContactNumber.getValue() && !("").equals(txtHospitalContactNumber.getValue())){
				bean.setPhoneNumber(txtHospitalContactNumber.getValue());
				}
				if(null !=txtHospitalMobileNumber.getValue() && !("").equals(txtHospitalContactNumber.getValue())){
				bean.setMobileNumber(txtHospitalMobileNumber.getValue());
				}
				if(null !=txtHospitalFaxNumber.getValue() && !("").equals(txtHospitalFaxNumber.getValue())){
				bean.setFaxNumber(txtHospitalFaxNumber.getValue().replaceAll("\\s", ""));
				}
				bean.setEmailId(txtHospitalEmailId.getValue());
				bean.setHospitalCode(hospitalCodeTxt.getValue());
				bean.setHospitalCodeIrda(hospitalCodeIrdaTxt.getValue());
				bean.setRemarks(txtHospitalRemarks.getValue());
				
				fireViewEvent(UpdateHospitalDetailsPresenter.UPDATE_BUTTON_CLICK,bean);
				fireViewEvent(MenuItemBean.UPDATE_HOSPITAL_INFORMATION, true);
				}
			}
		});
		
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.UPDATE_HOSPITAL_INFORMATION, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		/*cmbHospital
		.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<HospitalDto>() {

			@Override
			public void onSuggestionPicked(HospitalDto suggestion) {

				bean.setHospitalType(suggestion.getHospitalType());
				bean.setHospitalTypeValue(suggestion.getHospitalTypeValue());
				handleHospitalSelection(suggestion);
				
		
			}
		});*/
		
	}
	
	private void handleHospitalSelection(HospitalDto suggestion) {
		
		selectedHospital = suggestion;

		if(suggestion.getKey() != null)
		{
		  	Hospitals registedHospitals = suggestion.getRegistedHospitals();
			if (registedHospitals != null) {
				this.txtHospitalAddress.setValue(registedHospitals.getAddress());
				this.txtHospitalContactNumber.setValue(registedHospitals
						.getPhoneNumber());
				this.txtHospitalPinCode.setValue(registedHospitals.getPincode());
				this.txtHospitalFaxNumber.setValue(registedHospitals.getFax());
				this.txtHospitalEmailId.setValue(registedHospitals.getEmailId());
				this.txtHospitalMobileNumber.setValue(registedHospitals.getMobileNumber());
				this.hospitalCodeIrdaTxt.setValue(registedHospitals.getHospitalIrdaCode());
				this.hospitalCodeTxt.setValue(registedHospitals.getHospitalCode());
				
				//setHospitalFieldsEditable(false);
				
				SelectValue hospitalTypeValue = new SelectValue();
				hospitalTypeValue.setId(registedHospitals.getHospitalType()
						.getKey());
				hospitalTypeValue.setValue(registedHospitals.getHospitalType()
						.getValue());
				cmbHospitalType.setValue(hospitalTypeValue);
				
//				SelectValue areaValue=new SelectValue();
//				areaValue.setId(registedHospitals.getLocalityId());
//				areaValue.setValue(registedHospitals.getLocality());
//				area.setValue(areaValue);
				
				//networkHospitalTypeTxt.setValue(registedHospitals
					//	.getNetworkHospitalType() != null ? registedHospitals
					//	.getNetworkHospitalType() : "");

				if (registedHospitals.getHospitalType().getValue()
						.equalsIgnoreCase("network")) {
					//networkHospitalTypeTxt.setValue(registedHospitals
							//.getNetworkHospitalType());
				}
			}
			
		 }else{
			 	//setHospitalFieldsEditable(true);
			 	Collection<?> itemIds = cmbHospitalType.getContainerDataSource().getItemIds();
				cmbHospitalType.setValue(itemIds.toArray()[2]);
				cmbHospitalType.setNullSelectionAllowed(false);
				//setHospitalFieldsEditable(true);
		 }

	}

//	@Override
//	public void resetView() {
//		// TODO Auto-generated method stub
//		
//	}

	public void listOfStates(BeanItemContainer<SelectValue> selectedContainer,BeanItemContainer<SelectValue> modeOfIntimation,BeanItemContainer<SelectValue> intimatedBy) {
		
	     this.selectValueContainer=selectedContainer;
	     this.modeOfIntimations=modeOfIntimation;
	     this.intimated=intimatedBy;
		
	}

	@Override
	public void listOfCity(BeanItemContainer<SelectValue> cityList) {
		
		if(city!=null && cityList!=null){
		city.setContainerDataSource(cityList);
		city.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		city.setItemCaptionPropertyId("value");
		}
	}

//	@Override
//	public void listOfArea(BeanItemContainer<SelectValue> areaList) {
//		
//		if(area!=null && areaList!=null){
//       area.setContainerDataSource(areaList);
//       area.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//       area.setItemCaptionPropertyId("value");
//		}
//	    
//	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
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
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} 
			showOrHideValidation(false);
			return true;
		}
	
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	@Override
	public void result() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Request for updating the Hospital Details has been created successfully!!!!!! </b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Updated");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();

			}
		});

	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	class CustomSuggestionProvider extends CollectionSuggestionProvider {		


		public CustomSuggestionProvider() {		

			super(Arrays.asList(new String[] {		
					"Java",		
					"JavaScript",		
					"Join Java",		
					"JavaFX Script"		
			}), MatchMode.CONTAINS, true);		
		}		
		public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {		
			Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);		
			suggestions.clear();		
			handleHospitalSearchQuery(suggestions,query);		
			return suggestions;		
		}		


		/* private void handleHospitalSearchQuery(		
			    		Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {		
					SelectValue selected=new SelectValue();		
					State states=null;		
					CityTownVillage cities=null;		
			//		Locality locality=null;		

					if(state.getValue()!=null){		

						selected=(SelectValue)state.getValue();		
						states=new State();		

						states.setKey(selected.getId());		
						states.setValue(selected.getValue());		

					}		
					if(city.getValue()!=null){		
						selected=(SelectValue)city.getValue();		
						cities=new CityTownVillage();		
						cities.setKey(selected.getId());		
						cities.setValue(selected.getValue());		
					}		
			//		if(area.getValue()!=null){		
			//			selected=(SelectValue)area.getValue();		
			//			locality=new Locality();		
			//			locality.setKey(selected.getId());		
			//			locality.setValue(selected.getValue());		
			//		}		



					List<Hospitals> hospitalSearch = hospitalService		
							.hospitalNameCriteriaSearch(query.getTerm(), states,		
									cities, null);		

					if(!hospitalSearch.isEmpty()){		
						List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();		
						for (Hospitals hospital : hospitalSearch) {		

							HospitalDto resultHospitalDto = new HospitalDto(hospital);		
							hospitalDtoList.add(resultHospitalDto);						
						}		

						for (HospitalDto hospitalDto : hospitalDtoList) {		
							 AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(hospitalDto.getName());		
							 suggestioner.setData(hospitalDto);		
							 suggestions.add(suggestioner);		
						}					
					}		
				}*/		

	}

}