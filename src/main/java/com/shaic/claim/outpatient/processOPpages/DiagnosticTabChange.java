package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.SearchHospitalContactNoUI;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.processOPpages.ConsultationTabPage.CustomSuggestionProvider;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.premedical.listenerTables.SpecialityTableListener;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OPLabPortals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.UnFreezHospitals;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

@UIScoped
public class DiagnosticTabChange extends ViewComponent{
	

	
	private BeanFieldGroup<BenefitsAvailedDTO> binder;
	
	private OutPatientDTO bean;
	
	private OptionGroup sameAsConsultation;
	
	private OptionGroup providerType;
	
	private AutocompleteTextField providerState;
	private AutocompleteTextField providerCity;
	private AutocompleteTextField txtProviderName;
	
	private TextField dummyTextField;
	private TextArea providerAddress;
	private TextField providerContactNo;
	
	private Button providerNameSearchBtn;
	
	private HorizontalLayout horLayout;
	
	private HorizontalLayout hLayout;
	
	private FormLayout leftFormLayout;
	private FormLayout rightFormLayout;
	private FormLayout thirdFormLayout;
	
	private com.vaadin.ui.Window popupWindow = new com.vaadin.ui.Window();
	
	private StringBuilder errMsg = new StringBuilder();
	
	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}
	
	@Inject
	private SearchHospitalContactNoUI searchHospitalUI; 
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	private State selectedState;
	
	private CityTownVillage selectedCity;
	
	private HospitalDto selectedHospital;
	
	private static final String stateCaption="State";
	private static final String cityCaption="City";
	
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BenefitsAvailedDTO>(BenefitsAvailedDTO.class);
		this.binder.setItemDataSource(bean.getBenefitsAvailedDto());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@SuppressWarnings("deprecation")
	public void initView(OutPatientDTO bean){
		
		this.bean = bean;
		initBinder();
		
		sameAsConsultation = (OptionGroup) binder.buildAndBind("", "sameasConsultation", OptionGroup.class);
		sameAsConsultation.addItems(getReadioButtonOptions());
		sameAsConsultation.setItemCaption(true, "Yes");
		sameAsConsultation.setItemCaption(false, "No");
//		sameAsConsultation.select(false);
		sameAsConsultation.setCaption("Same as Consultation <b style= 'color: red'>*</b>"); 
		sameAsConsultation.setCaptionAsHtml(true);
		/*if(bean.getConsulation() != null && bean.getConsulation().equals(Boolean.FALSE)){
			sameAsConsultation.select(false);
		}*/
		if(bean.getBenefitsAvailedDto().getSameasConsultation() != null && bean.getBenefitsAvailedDto().getSameasConsultation().equals(Boolean.FALSE)
				&& bean.getBenefitsAvailedDto().getProviderName() != null && !bean.getBenefitsAvailedDto().getProviderName().isEmpty()){
			sameAsConsultation.setValue(false);
		}else if(bean.getBenefitsAvailedDto().getSameasConsultation() != null && bean.getBenefitsAvailedDto().getSameasConsultation().equals(Boolean.TRUE)){
			sameAsConsultation.setValue(true);
		}
		
		providerType = (OptionGroup) binder.buildAndBind("", "networkorother", OptionGroup.class);
		providerType.addItems(getReadioButtonOptions());
		providerType.setItemCaption(true, "Network");
		providerType.setItemCaption(false, "Other than Network");
		providerType.select(true);
		providerType.setStyleName("horizontal");
		providerType.setCaption("Provider Type <b style= 'color: red'>*</b>"); 
		providerType.setCaptionAsHtml(true);
		providerType.setNullSelectionAllowed(false);
		if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null 
				&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.FALSE)
				&& bean.getBenefitsAvailedDto().getDiagnosisProvider() != null){
			providerType.setValue(false);
		}
		
//		txtState = (TextField) binder.buildAndBind("","state",TextField.class);
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();

		providerState = new AutocompleteTextField();		
		providerState.setCaption("State");		
		providerState.setCaptionAsHtml(true);		
		providerState.setItemAsHtml(false);		
		providerState.setMinChars(2);		
		providerState.setScrollBehavior(ScrollBehavior.NONE);		
		providerState.setSuggestionLimit(0);				
		providerState.setRequiredIndicatorVisible(true);		
		providerState.setSuggestionProvider(suggestionProvider);		
		providerState.addSelectListener(this::onAutocompleteStateSelect);
		providerState.setCaption("State"); 
		providerState.setCaptionAsHtml(true);
		if(bean.getBenefitsAvailedDto().getProviderState() != null){
			providerState.setValue(bean.getBenefitsAvailedDto().getProviderState());
		}
		
//		txtCity = (TextField) binder.buildAndBind("","city",TextField.class);
		providerCity = new AutocompleteTextField();	
		providerCity.setCaption("City");		
		providerCity.setCaptionAsHtml(true);		
		providerCity.setItemAsHtml(false);		
		providerCity.setMinChars(2);		
		providerCity.setScrollBehavior(ScrollBehavior.NONE);		
		providerCity.setSuggestionLimit(0);		
		providerCity.setRequiredIndicatorVisible(true);	
		providerCity.setSuggestionProvider(suggestionProvider);		
		providerCity.addSelectListener(this::onAutocompleteCitySelect);
		if(bean.getBenefitsAvailedDto().getProviderCity() != null){
			providerCity.setValue(bean.getBenefitsAvailedDto().getProviderCity());
		}
		
//		txtProviderName = (TextField) binder.buildAndBind("ProviderName","providerName",TextField.class);
		txtProviderName = new AutocompleteTextField();
		txtProviderName.setCaption("Provider Name</b>"); 
		txtProviderName.setCaptionAsHtml(true);		
		txtProviderName.setItemAsHtml(false);		
		txtProviderName.setMinChars(2);		
		txtProviderName.setScrollBehavior(ScrollBehavior.NONE);		
		txtProviderName.setSuggestionLimit(0);	
		txtProviderName.setRequiredIndicatorVisible(true);	
		txtProviderName.setSuggestionProvider(suggestionProvider);		
		txtProviderName.addSelectListener(this::onAutocompleteHospitalSelect);	
		if(bean.getBenefitsAvailedDto().getProviderName() != null){
			txtProviderName.setValue(bean.getBenefitsAvailedDto().getProviderName());
		}

		
		 
		
		dummyTextField =  new TextField();
		dummyTextField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField.setEnabled(false);
		
		TextField dummyTextField1 =  new TextField();
		dummyTextField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField1.setEnabled(false);

		TextField dummyTextField2 =  new TextField();
		dummyTextField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField2.setEnabled(false);

		TextField dummyTextField3 =  new TextField();
		dummyTextField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField3.setEnabled(false);
		
		TextField dummyTextField4 =  new TextField();
		dummyTextField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField4.setEnabled(false);
		
		providerAddress = (TextArea) binder.buildAndBind("Provider Address","providerAddress",TextArea.class);
		providerContactNo = (TextField) binder.buildAndBind("Provider Contact No","providerContactNo",TextField.class);
		CSValidator faxNumber = new CSValidator();
		faxNumber.extend(providerContactNo);
		faxNumber.setRegExp("^[0-9/]*$");
		faxNumber.setPreventInvalidTyping(true);
		providerContactNo.setMaxLength(12);
		
		providerNameSearchBtn = new Button();
		providerNameSearchBtn.setStyleName("link");
		providerNameSearchBtn.setIcon(new ThemeResource("images/search.png"));
		
//		FormLayout providerName = new FormLayout(dummyTextField,dummyTextField,dummyTextField,providerNameSearchBtn);
//		providerName.setSpacing(false);
		
		HorizontalLayout searchbtnLayout = new HorizontalLayout(new FormLayout(txtProviderName),new FormLayout(providerNameSearchBtn));
		searchbtnLayout.setMargin(false);
		searchbtnLayout.setSpacing(false);
		
		leftFormLayout  = new FormLayout(providerType,dummyTextField,providerState,providerCity,txtProviderName);
		leftFormLayout.setSpacing(false);
		rightFormLayout = new FormLayout(providerAddress,providerContactNo);
		thirdFormLayout = new FormLayout(dummyTextField1,dummyTextField2,dummyTextField3,providerNameSearchBtn);
		
		horLayout = new HorizontalLayout(new FormLayout(sameAsConsultation));
		horLayout.setSpacing(false);
		horLayout.setMargin(true);
		
		hLayout = new HorizontalLayout(leftFormLayout,rightFormLayout,thirdFormLayout);
		hLayout.setVisible(false);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		VerticalLayout vLayout = new VerticalLayout(horLayout,hLayout);
		vLayout.setSpacing(false);
		setCompositionRoot(vLayout);
		vLayout.setCaption("Diagnostics Details");
		
		if(sameAsConsultation != null && sameAsConsultation.getValue() != null && !(Boolean)sameAsConsultation.getValue()/* != null && sameAsConsultation.getValue().equals(Boolean.FALSE)*/){
			hLayout.setVisible(true);
		}
		
		addListner();
		
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	private void onAutocompleteStateSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		State state = (State) suggestion.getData();		

		if (state != null && providerState.getValue() != null		
				&& !providerState.getValue().equals("")) {		
			handleStateSelection(state);		
			bean.setState(state);	
			bean.getBenefitsAvailedDto().setProviderState(state.getValue());
		} else {		
			Notification.show("Pealse Select a Valid State");		
		}		
	
		selectedHospital = null;		
//		clearHospitalDetails();		
	}
	
	private void onAutocompleteCitySelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		CityTownVillage city = (CityTownVillage) suggestion.getData();		


		if (city != null && providerCity.getValue() != null		
				&& !providerCity.getValue().equals("")) {		
			handleCitySelection(city);		
			bean.setCity(city);		
			bean.getBenefitsAvailedDto().setProviderCity(city.getValue());
		} else {		
			Notification.show("Pealse Select a Valid City");		
		}			
//		cmbHospitals.setValue("");		
//		cmbHospitals.setData(null);		
		//cmbHospital.setValidationVisible(false);		
		// selectedArea = null;		
		selectedHospital = null;		
//		clearHospitalDetails();		
	}
	
	private void onAutocompleteHospitalSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		HospitalDto hospitalData = (HospitalDto) suggestion.getData();		
		if (hospitalData != null) {		
			/*bean.setHospitalType(hospitalData.getHospitalType());		
			bean.setHospitalTypeValue(hospitalData		
					.getHospitalTypeValue());		
			bean.setHospitalDto(hospitalData);		
			bindBeanToUI();	*/	
			handleHospitalSelection(hospitalData);			
//			hospitalSearchHLayout.setVisible(true);		
		} else {		
			Notification		
			.show("Please Select a Hospital Name or Enter Hospital Details");		
			/*setHospitalFieldsEditable(true);		
			hospitalSearchHLayout.setVisible(false);		
			bindTempHospitalTypeToUI();	*/	
		}		

	}
	
	protected void handleStateSelection(State state) {
		selectedState = state;
	}
	
	protected void handleCitySelection(CityTownVillage city) {
		selectedCity = city;
	}
	
	private void handleHospitalSelection(HospitalDto suggestion) {
		if (suggestion != null) {
			selectedHospital = suggestion;

			if (suggestion.getKey() != null) {
//				OPLabPortals registedHospitals = suggestion.getRegistedUnFreezHospitals();
				if (suggestion != null) {
					/*this.txtHospitalAddress.setValue(registedHospitals
							.getAddress());*/
					String hospAddress = (suggestion.getAddress() != null ? suggestion.getAddress() : "");
					
					this.providerAddress.setValue(hospAddress != null ? hospAddress : "");					
					this.providerContactNo.setValue(suggestion
							.getPhoneNumber());
					
					bean.getBenefitsAvailedDto().setProviderName(suggestion.getName() != null ? suggestion.getName() : "");
					bean.getBenefitsAvailedDto().setProviderAddress(hospAddress != null ? hospAddress : "");
					bean.getBenefitsAvailedDto().setProviderContactNo(suggestion.getPhoneNumber() != null ? suggestion.getPhoneNumber() : "");

//					setHospitalFieldsEditable(false);

					SelectValue hospitalTypeValue = new SelectValue();
					if(suggestion.getHospitalTypeValue() != null && 
							suggestion.getHospitalTypeValue().equalsIgnoreCase("NETWEORK")){
						hospitalTypeValue.setId(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID);
					} else if(suggestion.getHospitalTypeValue() != null && 
						suggestion.getHospitalTypeValue().equalsIgnoreCase("NONNETWEORK")){
					hospitalTypeValue.setId(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
					}
					
					hospitalTypeValue.setValue(suggestion
							.getHospitalType().getValue());

				
				}
			}
		} else {
			/*setHospitalFieldsEditable(true);
			bindTempHospitalTypeToUI();*/
		}

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
			if(caption.equalsIgnoreCase(stateCaption)){		

				suggestions.clear();		
				handleStateSearchQuery(suggestions,query);		
			}		
			else if(caption.equalsIgnoreCase(cityCaption)){		
				handleCitySearchQuery(suggestions,query);		
			}else{		
				handleHospitalSearchQuery(suggestions,query);		
			}		
			return suggestions;		

		}		
	}
	
	private void handleStateSearchQuery(Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		selectedState = null;
		selectedCity = null;
		List<State> stateSearch = masterService
				.stateSearch(query.getTerm().toLowerCase());

		if (stateSearch != null && !stateSearch.isEmpty()) {
			for (State state : stateSearch) {
				AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(state.getValue());
				suggestioner.setData(state);
				suggestions.add(suggestioner);
//				field.addSuggestion(state, state.getValue());
			}
		} else {
			Notification.show("Please Select Valid State");
			selectedState = null;
			selectedCity = null;
		}
		selectedCity = null;
		providerState.setValue("");
		providerState.setData(null);
		// cmbArea.setText(null);
		// cmbArea.setValue(null);
//		cmbHospitals.setData(null);
//		cmbHospitals.setValue("");
		selectedHospital = null;
//		clearHospitalDetails();
	}
	
	private void handleCitySearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		if (selectedState == null) {
			Notification.show("Please Select Valid State");
		} else {
			List<CityTownVillage> citySearch = masterService.citySearch(
					query.getTerm().toLowerCase(), selectedState);

			if (citySearch != null && !citySearch.isEmpty()) {
				for (CityTownVillage city : citySearch) {
					AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(city.getValue());	
					suggestioner.setData(city);		
					suggestions.add(suggestioner);
					//field.addSuggestion(city, city.getValue());
				}
			} else {
				Notification.show("Please Select Valid City");
				selectedCity = null;
			}

			// cmbArea.setValue(null);
			// cmbArea.setText("");
			// selectedArea = null;
			txtProviderName.setData(null);
			txtProviderName.setValue("");
			selectedHospital = null;
//			clearHospitalDetails();
		}
	}
	
	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		List<OPLabPortals> hospitalSearch = null;
		/*if(bean.getIsPaayasPolicy()){
			hospitalSearch = hospitalService
					.hospitalNameCriteriaSearchForPaayas(query.getTerm(), selectedState, selectedCity);
		}else{*/
		String hospitalType = null;
		if(providerType != null && providerType.getValue().equals(Boolean.TRUE)){
			hospitalType = "Network";
		} else if (providerType != null && providerType.getValue().equals(Boolean.FALSE)){
			hospitalType = "Non Network";
		}
		if(selectedState != null && selectedCity != null){
			hospitalSearch = hospitalService
					.OPLabHospitalNameCriteriaSearch(query.getTerm(), selectedState, selectedCity,hospitalType);
		}
//		}

		if (hospitalSearch != null && !hospitalSearch.isEmpty()) {
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (OPLabPortals hospital : hospitalSearch) {

//				HospitalDto resultHospitalDto = new HospitalDto(hospital);
				HospitalDto resultHospitalDto = new HospitalDto();
				resultHospitalDto.setKey(hospital.getKey());
				resultHospitalDto.setName(hospital.getProviderName());
				resultHospitalDto.setAddress(hospital.getProviderAddressOne());
				resultHospitalDto.setHospAddr1(hospital.getProviderAddressOne());
				resultHospitalDto.setHospAddr2(hospital.getProviderAddressTwo());
				resultHospitalDto.setHospAddr3(hospital.getProviderAddressThree());
				resultHospitalDto.setHospitalTypeValue(hospital.getProviderType());
				resultHospitalDto.setState(hospital.getState());
				resultHospitalDto.setCity(hospital.getCity());
				resultHospitalDto.setPhoneNumber(hospital.getProviderContactNo());
				hospitalDtoList.add(resultHospitalDto);
			}
			
//			field.clearChoices();
			suggestions.clear();

			for (HospitalDto hospitalDto : hospitalDtoList) {
				/*field.addSuggestion(hospitalDto, hospitalDto.getName()+","+(hospitalDto.getAddress() != null ? hospitalDto.getAddress() : "")
						+","+(hospitalDto.getState() != null ? hospitalDto.getState() : "") +","+(hospitalDto.getCity() != null ? hospitalDto.getCity() : "")  +","+(hospitalDto.getPincode() != null ? hospitalDto.getPincode() : "")
						+","+hospitalDto.getHospitalTypeValue());*/
				AutocompleteSuggestion suggestioner=new AutocompleteSuggestion(hospitalDto.getName()+","+(hospitalDto.getAddress() != null ? hospitalDto.getAddress() : "")
						+","+(hospitalDto.getState() != null ? hospitalDto.getState() : "") +","+(hospitalDto.getCity() != null ? hospitalDto.getCity() : "")  +","+(hospitalDto.getPincode() != null ? hospitalDto.getPincode() : "")
						+","+hospitalDto.getHospitalTypeValue());
				suggestioner.setData(hospitalDto);
				suggestions.add(suggestioner);
			}
		} else {
			/*this.bean.setHospitalDto(null);
			setHospitalFieldsEditable(true);
			clearHospitalDetails();
			bindTempHospitalTypeToUI();
			networkHospitalTypeTxt.setValue("");
			if(dummyChkBox != null){
				dummyChkBox.setEnabled(true);
			}*/
		}
	}
	
	@SuppressWarnings("deprecation")
	public void addListner(){
		
		sameAsConsultation.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isFinalChecked = false ;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "false") {
					hLayout.setVisible(true);
					providerState.setValue("");
					providerCity.setValue("");
					providerAddress.setValue("");
					providerContactNo.setValue("");
					txtProviderName.setValue("");
					bean.getBenefitsAvailedDto().setSameasConsultation(false);
				} else {
					hLayout.setVisible(false);
				}	
			}
		});

		if(providerNameSearchBtn != null){
			providerNameSearchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Long hospitalTypeId=0l;
					if(providerType != null && providerType.getValue().equals(Boolean.TRUE)){
						hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL;
					} else if (providerType != null && providerType.getValue().equals(Boolean.FALSE)){
						hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL;
					}
					SelectValue select = new SelectValue();
					select.setId(hospitalTypeId);
					bean.getNewIntimationDTO().setHospitalType(select);
					searchHospitalUI.initView(bean.getNewIntimationDTO());
					searchHospitalUI.setPresenterString(SHAConstants.OP_DIAGNOSTIC_TAB);
					popupWindow = new com.vaadin.ui.Window();
					popupWindow.setCaption("Hospital Contact");
					popupWindow.setWidth("75%");
					popupWindow.setHeight("75%");
					popupWindow.setContent(searchHospitalUI);
					popupWindow.setClosable(true);
					popupWindow.center();
					popupWindow.setResizable(false);
					popupWindow.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popupWindow.setModal(true);
					UI.getCurrent().addWindow(popupWindow);
				}
			});
		}
		
		providerType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
				Boolean value = (Boolean) event.getProperty().getValue();
					if(!value){
						clearAllFieldsValues();
						thirdFormLayout.setVisible(true);
						
					}else{
						clearAllFieldsValues();
						thirdFormLayout.setVisible(true);
					}
				}
			}
		});
	
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		if(sameAsConsultation.getValue() == null){
			hasError = true;
			errMsg.append("Please Select Same as Consultation in Diagnostics. </br>");
		}
		else if(sameAsConsultation.getValue() != null && sameAsConsultation.getValue().equals(Boolean.FALSE)){
			if(StringUtils.isBlank(providerState.getValue())){
				hasError = true;
				errMsg.append("Please enter the Diagnostic State. </br>");
			}
		
			if(StringUtils.isBlank(providerCity.getValue())){
				hasError = true;
				errMsg.append("Please enter the Diagnostic City. </br>");
			}
		
			if(StringUtils.isBlank(txtProviderName.getValue())){
				hasError = true;
				errMsg.append("Please enter the Diagnostic Provider Name. </br>");
			}
		
			if(providerType != null && providerType.getValue() == null){
				hasError = true;
				errMsg.append("Please select the Provider Type. </br>");
			}
		}
	
		return hasError;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	public void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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
	}
	
	public void setValues(){
		
		if(sameAsConsultation != null && sameAsConsultation.getValue()!= null && sameAsConsultation.getValue().equals(Boolean.TRUE)){
			
			bean.getBenefitsAvailedDto().setProviderState(bean.getBenefitsAvailedDto().getState());
			bean.getBenefitsAvailedDto().setProviderCity(bean.getBenefitsAvailedDto().getCity());
			bean.getBenefitsAvailedDto().setProviderName(bean.getBenefitsAvailedDto().getHospitalName());
			bean.getBenefitsAvailedDto().setProviderAddress(bean.getBenefitsAvailedDto().getHospitalAddress());
			bean.getBenefitsAvailedDto().setProviderContactNo(bean.getBenefitsAvailedDto().getHospitalContactNumber());
			bean.getBenefitsAvailedDto().setDiagnosisProvider(bean.getBenefitsAvailedDto().getConsulationProvider());
			if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.TRUE)){
				bean.getBenefitsAvailedDto().setDiagnosisHospitalType(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID);
			} else if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.FALSE)){
				bean.getBenefitsAvailedDto().setDiagnosisHospitalType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
			}
			bean.getBenefitsAvailedDto().setSameasConsultation(true);
		} else {
			bean.getBenefitsAvailedDto().setProviderState(providerState.getValue());
			bean.getBenefitsAvailedDto().setProviderCity(providerCity.getValue());
			bean.getBenefitsAvailedDto().setProviderName(selectedHospital != null && selectedHospital.getName() != null ? selectedHospital.getName() : txtProviderName.getValue());
			bean.getBenefitsAvailedDto().setProviderAddress(providerAddress.getValue());
			bean.getBenefitsAvailedDto().setProviderContactNo(providerContactNo.getValue());
			bean.getBenefitsAvailedDto().setDiagnosisProvider(providerType.getValue().equals(Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE);
			if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.TRUE)){
				bean.getBenefitsAvailedDto().setDiagnosisHospitalType(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID);
			} else if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.FALSE)){
				bean.getBenefitsAvailedDto().setDiagnosisHospitalType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
			}
			bean.getBenefitsAvailedDto().setSameasConsultation(false);
		}
		
	}
	
	public void resetValues(){
		bean.getBenefitsAvailedDto().setProviderState("");
		bean.getBenefitsAvailedDto().setProviderCity("");
		bean.getBenefitsAvailedDto().setProviderName("");
		bean.getBenefitsAvailedDto().setProviderAddress("");
		bean.getBenefitsAvailedDto().setProviderContactNo("");
		bean.getBenefitsAvailedDto().setDiagnosisProvider(false);
		bean.getBenefitsAvailedDto().setSameasConsultation(false);
	}
	
	@SuppressWarnings("deprecation")
	public void setHospitalDetails(HospitalDto hospitalDto) {
		try {
			popupWindow.close();
			if (selectedHospital == null) {
				selectedHospital = new HospitalDto();
			}
			selectedHospital = hospitalDto;
			selectedHospital.setKey(hospitalDto.getKey());
			
			providerContactNo.setValue(selectedHospital.getPhoneNumber());
			txtProviderName.setValue(selectedHospital.getName());
			providerCity.setValue(selectedHospital.getCity());
			providerState.setValue(selectedHospital.getState());
			providerAddress.setValue(selectedHospital.getAddress());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearAllFieldsValues(){
		
		providerState.clear();
		providerCity.clear();
		txtProviderName.clear();
		providerContactNo.clear();
		providerAddress.clear();
	}
	

}
