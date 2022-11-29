package com.shaic.claim.rod.wizard.forms.popup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
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
import com.vaadin.ui.Window;
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

public class ChangeHospitalViewImpl extends AbstractMVPView implements ChangeHospitalView {

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

	private ComboBox state;
	
	private ComboBox city;
	
	private HospitalDto selectedHospital;
	
	/*private AutocompleteField<HospitalDto> cmbHospital;*/

	private AutocompleteTextField cmbHospitals ;
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
	
	private Window popup;
	
	private ReceiptOfDocumentsDTO rodDTO;
	
	private TextField txtHospitalName;
	
	@PostConstruct
	public void initView(){
		
	}
	public void initView(UpdateHospitalDetailsDTO bean,Window popup,ReceiptOfDocumentsDTO rodDTO,TextField txtHospitalName) {
		
		this.rodDTO = rodDTO;
		this.txtHospitalName = txtHospitalName;
		
		this.bean = bean;
		this.popup = popup;
		this.binder = new BeanFieldGroup<UpdateHospitalDetailsDTO>(
				UpdateHospitalDetailsDTO.class);
		this.binder.setItemDataSource(this.bean);
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		state = (ComboBox) binder.buildAndBind("State", "state",
				ComboBox.class);
		
//		fireViewEvent(ChangeHospitalPresenter.SET_STATE_LIST,null);
		
		List<State> stateList = masterService.getStateList();
		List<SelectValue> states = new ArrayList<SelectValue>();

		for (State state : stateList) {
			SelectValue selected = new SelectValue();
			selected.setId(state.getKey());
			selected.setValue(state.getValue());
			states.add(selected);
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(states);
		
		state.setTabIndex(3);
		
		state.setContainerDataSource(selectValueContainer);
		state.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		state.setItemCaptionPropertyId("value");
		
		state.setValue(this.bean.getState());
		
		
//		BeanItemContainer<SelectValue> areaList = masterService.getAreaList(this.bean.getCity().getId());
		
//		area=(ComboBox) binder.buildAndBind("Area", "area",
//				ComboBox.class);
//		
//		area.setContainerDataSource(areaList);
//		area.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		area.setItemCaptionPropertyId("value");
		
		List<CityTownVillage> cities = masterService.getCities(this.bean.getState().getId());
		
		List<SelectValue> cityList = new ArrayList<SelectValue>();

		for (CityTownVillage city : cities) {
			SelectValue selected = new SelectValue();
			selected.setId(city.getKey());
			selected.setValue(city.getValue());
			cityList.add(selected);
		}
		
		BeanItemContainer<SelectValue> cityContainer = new BeanItemContainer<SelectValue>(cityList);

		city=(ComboBox) binder.buildAndBind("City", "city",
				ComboBox.class);
		
		city.setContainerDataSource(cityContainer);
		city.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		city.setItemCaptionPropertyId("value");
		
		city.setValue(this.bean.getCity());
		addStateListener();
		
		
//		area.setValue(this.bean.getArea());
		
//		hospitalName = (TextField) binder.buildAndBind("Hospital Name",
//				"hospitalName", TextField.class);
		
		cmbHospitalType = (ComboBox) binder.buildAndBind("Hospital Type",
				"hospitalType", ComboBox.class);
		cmbHospitalType.setWidth("180px");
		
		
		cmbHospitalType.setContainerDataSource(masterService.getConversionReasonByValue(ReferenceTable.HOSPITAL_TYPE));
		cmbHospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalType.setItemCaptionPropertyId("value");
		
		cmbHospitalType.setValue(this.bean.getHospitalType());
		
		hospitalCodeTxt = new TextField("Hospital Code (Internal)");
		hospitalCodeTxt.setWidth("180px");
		hospitalCodeTxt.setNullRepresentation("");
		hospitalCodeTxt.setMaxLength(15);
		CSValidator designationValid = new CSValidator();
		designationValid.extend(hospitalCodeTxt);
		designationValid.setRegExp("^[a-zA-Z 0-9]*$");
		designationValid.setPreventInvalidTyping(true);
		hospitalCodeTxt.setValue(this.bean.getHospitalCode());

		hospitalCodeIrdaTxt = new TextField("Hospital Code (IRDA)");
		hospitalCodeIrdaTxt.setWidth("180px");
		hospitalCodeIrdaTxt.setNullRepresentation("");
		hospitalCodeIrdaTxt.setMaxLength(15);
		CSValidator irdaValid = new CSValidator();
		irdaValid.extend(hospitalCodeIrdaTxt);
		irdaValid.setRegExp("^[a-zA-Z 0-9]*$");
		irdaValid.setPreventInvalidTyping(true);
		hospitalCodeIrdaTxt.setValue(this.bean.getHospitalCodeIrda());

		txtHospitalAddress = new TextArea();
		txtHospitalAddress.setCaption("Address");
		txtHospitalAddress.setWidth("180px");
		txtHospitalAddress.setValue(this.bean.getAddress());

		CSValidator pinCodeValidator=new CSValidator();
		txtHospitalPinCode = new TextField();
		txtHospitalPinCode.setCaption("Pin Code");
		txtHospitalPinCode.setMaxLength(6);
		txtHospitalPinCode.setWidth("180px");
		txtHospitalPinCode.setNullRepresentation("");
		pinCodeValidator.extend(txtHospitalPinCode);
		pinCodeValidator.setRegExp("^[0-9/]*$");
		pinCodeValidator.setPreventInvalidTyping(true);
		txtHospitalPinCode.setValue(this.bean.getPincode());

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
		txtHospitalContactNumber.setValue(this.bean.getContactNo());

		
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
		txtHospitalFaxNumber.setValue(this.bean.getFaxNumber());
		

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
		txtHospitalMobileNumber.setValue(this.bean.getMobileNumber());

		txtHospitalEmailId = new TextField();
		txtHospitalEmailId.setCaption("Email ID");
		txtHospitalEmailId.setWidth("180px");
		txtHospitalEmailId.setNullRepresentation("");
		txtHospitalEmailId.setValue(this.bean.getEmailId());
		
		txtHospitalRemarks=new TextArea();
		txtHospitalRemarks.setCaption("Remarks(If any)");
		txtHospitalRemarks.setNullRepresentation("");
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
		

		/*cmbHospital = new AutocompleteField<HospitalDto>();
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
		//Vaadin8-setImmediate() cmbHospital.setImmediate(true);
		cmbHospital.setRequiredError("Please select a valid Hospital");
		cmbHospital.setValidationVisible(true);
		cmbHospital.setCaption("Hospital Name");
		cmbHospital.setWidth("180px");
		cmbHospital.setIcon(null);
		cmbHospital.setTabIndex(5);
		cmbHospital.setText(this.bean.getHospitalName());*/
		
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();
		cmbHospitals = new AutocompleteTextField();
		cmbHospitals.setCaption("Hospital Name");
		cmbHospitals.setCaptionAsHtml(true);
		cmbHospitals.setItemAsHtml(false);
		cmbHospitals.setMinChars(2);
		cmbHospitals.setScrollBehavior(ScrollBehavior.NONE);
		cmbHospitals.setSuggestionLimit(0);
		cmbHospitals.setWidth("180px");
		cmbHospitals.setSuggestionProvider(suggestionProvider);
		cmbHospitals.addSelectListener(this::onAutocompleteHospitalSelect);
		cmbHospitals.setValue(this.bean.getHospitalName());
		/*cmbHospitals.setData(this.bean.getHospital());*/
		/*cmbHospitals.setRequiredIndicatorVisible(true);*/
//		CSValidator hospitalValid = new CSValidator();
//		hospitalValid.extend(cmbHospital);
//		hospitalValid.setRegExp("^[a-zA-Z 0-9.]*$");
//		hospitalValid.setPreventInvalidTyping(true);

		firstLayout = new FormLayout(state,/*area,*/ cmbHospitals,cmbHospitalType,txtHospitalAddress,
				txtHospitalPinCode);
		secondLayout = new FormLayout(city, txtHospitalContactNumber, txtHospitalFaxNumber, txtHospitalMobileNumber,
				txtHospitalEmailId, hospitalCodeTxt, hospitalCodeIrdaTxt);

		buttonLayout = new HorizontalLayout(updateBtn, cancelBtn);
		buttonLayout.setSpacing(true);

		horizonMain = new HorizontalLayout(firstLayout, secondLayout);

		vertical = new VerticalLayout(horizonMain, buttonLayout);
		vertical.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		vertical.setSpacing(true);
			vertical.setWidth("700px");
		mainPanel=new Panel(vertical);
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Update Hospital Details");
		mainPanel.setWidth("100%");
		mainPanel.setHeight("500px");

		horizonMain.setHeight("100%");
		horizonMain.setWidth("100%");
		
		addListener();
		
		mandatoryFields.add(state);
		mandatoryFields.add(city);
		mandatoryFields.add(cmbHospitalType);
//		mandatoryFields.add(area);
		//mandatoryFields.add(area);
		
		//setUpAutoHospital(cmbHospital);
		
		showOrHideValidation(false);
     
		this.setWidth("100%");
		this.setHeight(600, Unit.PIXELS);
		setCompositionRoot(mainPanel);
//		mandatoryFields.add(txtHospitalMobileNumber);
//		mandatoryFields.add(txtHospitalFaxNumber);
	}
	
	 private void onAutocompleteHospitalSelect(AutocompleteEvents.SelectEvent event) {
	        AutocompleteSuggestion suggestion = event.getSuggestion();
	        String caption = "Suggestion selected: " + suggestion.getValue();
	        HospitalDto hospitalData = (HospitalDto) suggestion.getData();
			if (hospitalData != null) {

                
				bean.setHospitalType(hospitalData.getHospitalType());
				bean.setHospitalTypeValue(hospitalData.getHospitalTypeValue());
				handleHospitalSelection(hospitalData);
				
		
			
			} else {
				Notification
						.show("Please Select a Hospital Name or Enter Hospital Details");
				if(selectedHospital== null){
					cmbHospitalType.setValue(null);
				}
				/*setHospitalFieldsEditable(true);
				hospitalSearchHLayout.setVisible(false);
				bindTempHospitalTypeToUI();*/
			}
		
	    }
	 
	/*private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
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
		
		if(state.getValue()!=null){
			
			selected=(SelectValue)state.getValue();
			states=new State();
			
			states.setKey(selected.getId());
			states.setValue(selected.getValue());
			
		}
		if(city.getValue()!=null){
			SelectValue selectValue = (SelectValue)city.getValue();
//			CityTownVillage cityValue=(CityTownVillage)city.getValue();
			cities=new CityTownVillage();
			cities.setKey(selectValue.getId());
			cities.setValue(selectValue.getValue());
		}
//		if(area.getValue()!=null){
//			selected=(SelectValue)area.getValue();
//			locality=new Locality();
//			locality.setKey(selected.getId());
//			locality.setValue(selected.getValue());
//		}
		
		

		List<Hospitals> hospitalSearch = hospitalService
				.hospitalNameCriteriaSearch(query.getTerm(), states,
						cities/*, null*/);

		if(!hospitalSearch.isEmpty()){
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (Hospitals hospital : hospitalSearch) {
				
				HospitalDto resultHospitalDto = new HospitalDto(hospital);
				hospitalDtoList.add(resultHospitalDto);				
			}
			
			for (HospitalDto hospitalDto : hospitalDtoList) {
				/*field.addSuggestion(hospitalDto, hospitalDto.getName());*/
				AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(hospitalDto.getName());
				suggestioner.setData(hospitalDto);
				suggestions.add(suggestioner);
			}			
		}
	}
	public void addListener(){
		
		
		updateBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
					
				if(bean.getKey() != null){
					
						SelectValue states=new SelectValue();
						states=(SelectValue)state.getValue();
						SelectValue cityValue=new SelectValue();
						cityValue=(SelectValue)city.getValue();
						SelectValue cities = new SelectValue();
						cities.setId(cityValue.getId());
						cities.setValue(cityValue.getValue());
						if(null != cmbHospitalType.getValue() && !("").equals(cmbHospitalType.getValue())){
						SelectValue hospitalType=new SelectValue();
						hospitalType=(SelectValue)cmbHospitalType.getValue();
						
						bean.setHospitalType(hospitalType);
						bean.setHospitalId(hospitalType.getId());
						}
						bean.setState(states);
						bean.setCity(cities);
						bean.setHospitalName(cmbHospitals.getValue());
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
						
						rodDTO.setChangeHospitalDto(bean);
						
						if(txtHospitalName != null){
							txtHospitalName.setReadOnly(false);
							txtHospitalName.setValue(bean.getHospitalName());
							txtHospitalName.setReadOnly(true);
						}
						
						result();
						
//						fireViewEvent(ChangeHospitalPresenter.UPDATE_BUTTON_CLICK,bean);
					}else{
						getErrorMessage("Please select Registered Hospital");
					}
				}
			}
		});
		
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				/*ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	popup.close();
				                	
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox("Are you sure You want to Cancel ?", buttonsNamewithType);
				Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
						.toString());
				Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
						.toString());
				yesButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						
					}
					});
				noButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
					});
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
		});
		
		cmbHospital.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(selectedHospital== null){
					cmbHospitalType.setValue(null);
				}
			}
		});*/
		
	}
	private void addStateListener() {
		state.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null !=state.getValue() && !("").equals(state.getValue())){
				SelectValue selected=(SelectValue) state.getValue();
				Long key=selected.getId();
			   fireViewEvent(ChangeHospitalPresenter.SET_COMBOBOX_VALUE,key);
				}
			}
		});
		
		city.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
//				if(city!=null){
//					SelectValue selected=(SelectValue) city.getValue();
//				if(selected!=null){
//				Long key=selected.getId();
//					fireViewEvent(ChangeHospitalPresenter.SET_AREA,key);
//				}
//				}
			}
		});
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
				this.bean.setKey(registedHospitals.getKey());
				
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
	public void getErrorMessage(String eMsg){
		
		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
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
			if(field != null){
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
			}
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
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			
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

/*		Label successLabel = new Label(
				"<b style = 'color: black;'>Request for Change the Hospital Details has been created successfully!!!!!! </b>",
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Updated");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: black;'>Request for Change the Hospital Details has been created successfully!!!!!! </b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				popup.close();

			}
		});

	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatesList(
			BeanItemContainer<SelectValue> selectValueContainer) {
		
		state.setContainerDataSource(this.selectValueContainer);
		state.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		state.setItemCaptionPropertyId("value");
		
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
		   String caption = query.getExtension().getParent().getCaption();
		   Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);

			if(null != state.getValue() && !("").equals(state) && null != city.getValue() && !("").equals(city)){
			if ( !ValidatorUtils.isNull(state))
			{
				handleHospitalSearchQuery(suggestions,query);	
			}
			}
			else
			{
				Notification.show("Please Select State and City", Notification.TYPE_HUMANIZED_MESSAGE);
			}
		
		   
		   
	        return suggestions;
	        
	    }

	    
	}

}
