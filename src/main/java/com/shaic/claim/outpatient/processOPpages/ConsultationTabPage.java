package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.SearchHospitalContactNoUI;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterService;
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
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
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
public class ConsultationTabPage extends ViewComponent{
	
	private BeanFieldGroup<BenefitsAvailedDTO> binder;
	
	private OutPatientDTO bean;
	
	private OptionGroup providerType;
	
	private AutocompleteTextField txtState;
	private AutocompleteTextField txtCity;
	private AutocompleteTextField txtHospitalName;
	private TextField txtHospitalAddress;
	
	private TextField dummyTextField;
	private TextField hospitalContactNumber = new TextField();
	private TextField hospitalFaxNo;
	private TextField hospitalDoctorName;
	
	private CheckBox chkClinic;
	
	private Button hospitalPhnSearchBtn;
	
	private FormLayout leftFormLayout;
	private FormLayout rightFormLayout;
	private FormLayout hospContct;
	
	
	private State selectedState;
	
	private CityTownVillage selectedCity;
	
	private static final String stateCaption="State";
	
	private static final String cityCaption="City";
	
	private StringBuilder errMsg = new StringBuilder();
	
	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}
	
	@Inject
	private OPSpecialityListenerTable specialityListener;
	
	@Inject
	private SearchOPHospitalContactDetails searchHospitalDtls; 
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	private com.vaadin.ui.Window popupWindow = new com.vaadin.ui.Window();
	
	private HospitalDto selectedHospital;
	
	public static final String HOSPITAL_SELECTED = "hospital_details_selected";
	
	private HorizontalLayout dummyLayOut;
	@PostConstruct
	public void init() 
	{
		
	}
	
	@SuppressWarnings("deprecation")
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
		specialityListener.init(bean);
		if(bean.getSpecialityDTOList() != null && bean.getSpecialityDTOList()!= null){
			for (OPSpecialityDTO speciality : bean.getSpecialityDTOList()) {
				specialityListener.addBeanToList(speciality);
			}
		}
		
		providerType = (OptionGroup) binder.buildAndBind("", "networkorother", OptionGroup.class);
		providerType.addItems(getReadioButtonOptions());
		providerType.setItemCaption(true, "Network");
		providerType.setItemCaption(false, "Other than Network");
		providerType.setStyleName("horizontal");
		providerType.setCaption("Provider Type <b style= 'color: red'>*</b>"); 
		providerType.setCaptionAsHtml(true);
		providerType.setValue(true);

		
//		txtState = (AutocompleteTextField) binder.buildAndBind("","state",AutocompleteTextField.class);
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();

		txtState = new AutocompleteTextField();		
		txtState.setCaption("State");		
		txtState.setCaptionAsHtml(true);		
		txtState.setItemAsHtml(false);		
		txtState.setMinChars(2);		
		txtState.setScrollBehavior(ScrollBehavior.NONE);		
		txtState.setSuggestionLimit(0);				
		txtState.setRequiredIndicatorVisible(true);		
		txtState.setSuggestionProvider(suggestionProvider);		
		txtState.addSelectListener(this::onAutocompleteStateSelect);
		if(bean.getBenefitsAvailedDto().getState() != null){
			txtState.setValue(bean.getBenefitsAvailedDto().getState());
		}
		
//		txtCity = (TextField) binder.buildAndBind("City","city",TextField.class);
		txtCity = new AutocompleteTextField();	
		txtCity.setCaption("City");		
		txtCity.setCaptionAsHtml(true);		
		txtCity.setItemAsHtml(false);		
		txtCity.setMinChars(2);		
		txtCity.setScrollBehavior(ScrollBehavior.NONE);		
		txtCity.setSuggestionLimit(0);		
		txtCity.setRequiredIndicatorVisible(true);	
		txtCity.setSuggestionProvider(suggestionProvider);		
		txtCity.addSelectListener(this::onAutocompleteCitySelect);
		if(bean.getBenefitsAvailedDto().getCity() != null){
			txtCity.setValue(bean.getBenefitsAvailedDto().getCity());
		}
//		txtHospitalName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		txtHospitalName = new AutocompleteTextField();	
		txtHospitalName.setCaption("Hospital Name");		
		txtHospitalName.setCaptionAsHtml(true);		
		txtHospitalName.setItemAsHtml(false);		
		txtHospitalName.setMinChars(2);		
		txtHospitalName.setScrollBehavior(ScrollBehavior.NONE);		
		txtHospitalName.setSuggestionLimit(0);	
		txtHospitalName.setRequiredIndicatorVisible(true);
		txtHospitalName.setSuggestionProvider(suggestionProvider);		
		txtHospitalName.addSelectListener(this::onAutocompleteHospitalSelect);
		if(bean.getBenefitsAvailedDto().getHospitalName() != null){
			txtHospitalName.setValue(bean.getBenefitsAvailedDto().getHospitalName());
		}
		txtHospitalAddress = (TextField) binder.buildAndBind("Hospital Address","hospitalAddress",TextField.class);
		
		dummyTextField =  new TextField();
		dummyTextField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyTextField.setEnabled(false);
		hospitalContactNumber = (TextField) binder.buildAndBind("","hospitalContactNumber",TextField.class);
		hospitalContactNumber.setCaption(null);
		CSValidator contactNumber = new CSValidator();
		contactNumber.extend(hospitalContactNumber);
		contactNumber.setRegExp("^[0-9/]*$");
		contactNumber.setPreventInvalidTyping(true);
		hospitalContactNumber.setMaxLength(12);
		
		
		hospitalFaxNo = (TextField) binder.buildAndBind("Hospital Fax No","hospitalFaxNo",TextField.class);
		CSValidator faxNumber = new CSValidator();
		faxNumber.extend(hospitalFaxNo);
		faxNumber.setRegExp("^[0-9/]*$");
		faxNumber.setPreventInvalidTyping(true);
		hospitalDoctorName = (TextField) binder.buildAndBind("Hospital Doctor Name","hospitalDoctorName",TextField.class);
		CSValidator doctorNameValidator = new CSValidator();
		doctorNameValidator.extend(hospitalDoctorName);
		doctorNameValidator.setRegExp("^[a-zA-Z . ]*$");
		doctorNameValidator.setPreventInvalidTyping(true);
		
		hospitalPhnSearchBtn = new Button();
		hospitalPhnSearchBtn.setStyleName("link");
		hospitalPhnSearchBtn.setIcon(new ThemeResource("images/search.png"));
		
		chkClinic = new CheckBox("Clinic");
		chkClinic.setVisible(false);
		
		TextField dummy1 = new TextField();
		dummy1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummy1.setEnabled(false);
		
		TextField dummy2 = new TextField();
		dummy2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummy2.setEnabled(false);
		
		leftFormLayout  = new FormLayout(providerType,txtState,txtCity,txtHospitalName,txtHospitalAddress);
		dummyLayOut = new HorizontalLayout(hospitalContactNumber,hospitalPhnSearchBtn);
		dummyLayOut.setCaptionAsHtml(true);
		dummyLayOut.setSpacing(true);
		dummyLayOut.setCaption("Hospital Contact Number");
		rightFormLayout = new FormLayout(dummy1,dummyLayOut,hospitalFaxNo,hospitalDoctorName);
//		hospContct = new FormLayout(dummy2,hospitalPhnSearchBtn);
		
		if(bean.getBenefitsAvailedDto().getConsulationProvider() != null 
				&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.FALSE)){
			providerType.setValue(false);
			chkClinic.clear();
			chkClinic.setVisible(true);
			if(bean.getBenefitsAvailedDto().getClinic() != null && 
					bean.getBenefitsAvailedDto().getClinic()){
				chkClinic.setValue(true);
			}
			rightFormLayout.addComponentAsFirst(chkClinic);
			rightFormLayout.removeComponent(hospitalFaxNo);
		}
		
		HorizontalLayout hLayout = new HorizontalLayout(leftFormLayout,rightFormLayout);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		VerticalLayout vlayout = new VerticalLayout(hLayout,specialityListener);
		vlayout.setSpacing(false);
//		vlayout.addComponent(specialityListener);
		vlayout.setCaption("Consultation Details");
		setCompositionRoot(vlayout);
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

		if (state != null && txtState.getValue() != null		
				&& !txtState.getValue().equals("")) {		
			handleStateSelection(state);		
			bean.getBenefitsAvailedDto().setState(state.getValue());		
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


		if (city != null && txtCity.getValue() != null		
				&& !txtCity.getValue().equals("")) {		
			handleCitySelection(city);		
			bean.getBenefitsAvailedDto().setCity(city.getValue());
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
				UnFreezHospitals registedHospitals = suggestion.getRegistedUnFreezHospitals();
				if (registedHospitals != null) {
					/*this.txtHospitalAddress.setValue(registedHospitals
							.getAddress());*/
					String hospAddress = (registedHospitals.getAddress() != null ? registedHospitals.getAddress() : "");
					
					this.txtHospitalAddress.setValue(hospAddress != null ? hospAddress : "");					
					this.hospitalContactNumber.setValue(registedHospitals
							.getPhoneNumber());
					this.hospitalFaxNo.setValue(registedHospitals
							.getFax());
					
					bean.getBenefitsAvailedDto().setHospitalAddress(hospAddress != null ? hospAddress : "");
					bean.getBenefitsAvailedDto().setHospitalContactNumber(registedHospitals
							.getPhoneNumber() != null ? registedHospitals
									.getPhoneNumber() : "");
					bean.getBenefitsAvailedDto().setHospitalFaxNo(registedHospitals
							.getFax() != null ? registedHospitals
									.getFax() : "");
					bean.getBenefitsAvailedDto().setHospitalName(registedHospitals.getName() != null ? registedHospitals.getName() :"");
//					setHospitalFieldsEditable(false);

					SelectValue hospitalTypeValue = new SelectValue();
					hospitalTypeValue.setId(registedHospitals.getHospitalType()
							.getKey());
					hospitalTypeValue.setValue(registedHospitals
							.getHospitalType().getValue());

				
				}
			}
		} else {
			/*setHospitalFieldsEditable(true);
			bindTempHospitalTypeToUI();*/
		}

	}
	
	@SuppressWarnings("deprecation")
	public void addListner(){
		if(hospitalPhnSearchBtn != null){
			hospitalPhnSearchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Long hospitalTypeId=0l;
					if(providerType != null && providerType.getValue().equals(Boolean.TRUE)){
						hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL;
					} else if (providerType != null && providerType.getValue().equals(Boolean.FALSE)){
						hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL;
					}
					searchHospitalDtls.initView(bean.getNewIntimationDTO(),hospitalTypeId);
					popupWindow = new com.vaadin.ui.Window();
					popupWindow.setCaption("Hospital Contact");
					popupWindow.setWidth("75%");
					popupWindow.setHeight("75%");
					popupWindow.setContent(searchHospitalDtls);
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
						chkClinic.clear();
						chkClinic.setVisible(true);
						clearAllFieldsValues();
						rightFormLayout.addComponentAsFirst(chkClinic);
						rightFormLayout.removeComponent(hospitalFaxNo);
						
					}else{
						chkClinic.setVisible(false);
						clearAllFieldsValues();
						rightFormLayout.removeAllComponents();
						rightFormLayout.addComponents(dummyTextField,dummyLayOut,hospitalFaxNo,hospitalDoctorName);
						addFieldValues();
					}
				}
			}
		});
		
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
			hospitalContactNumber.setValue(selectedHospital.getPhoneNumber());
			txtHospitalName.setValue(selectedHospital.getName());
			txtCity.setValue(selectedHospital.getCity());
			txtState.setValue(selectedHospital.getState());
			hospitalFaxNo.setValue(selectedHospital.getFax());
			hospitalDoctorName.setValue(selectedHospital.getRepresentativeName());
			txtHospitalAddress.setValue(selectedHospital.getAddress());
			
			
//			hospitalSearchHLayout.setVisible(true);

//			bindBeanToUI();
//			setHospitalFieldsEditable(false);
		} catch (Exception e) {
			e.printStackTrace();
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
		txtState.setValue("");
		txtState.setData(null);
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
			txtHospitalName.setData(null);
			txtHospitalName.setValue("");
			selectedHospital = null;
//			clearHospitalDetails();
		}
	}
	
	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		List<UnFreezHospitals> hospitalSearch = null;
		/*if(bean.getIsPaayasPolicy()){
			hospitalSearch = hospitalService
					.hospitalNameCriteriaSearchForPaayas(query.getTerm(), selectedState, selectedCity);
		}else{*/
		if(selectedState != null && selectedCity != null){
			Long hospitalTypeId = 0l;
			if(providerType != null && providerType.getValue().equals(Boolean.TRUE)){
				hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL;
			} else if (providerType != null && providerType.getValue().equals(Boolean.FALSE)){
				hospitalTypeId = ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL;
			}
			hospitalSearch = hospitalService
					.OPUnFreezHospitalNameCriteriaSearch(query.getTerm(), selectedState, selectedCity,hospitalTypeId);
		}
//		}

		if (hospitalSearch != null && !hospitalSearch.isEmpty()) {
			List<HospitalDto> hospitalDtoList = new ArrayList<HospitalDto>();
			for (UnFreezHospitals hospital : hospitalSearch) {

				HospitalDto resultHospitalDto = new HospitalDto(hospital);
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
	public boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		
		if(StringUtils.isBlank(txtState.getValue())){
			hasError = true;
			errMsg.append("Please enter the Consultation State. </br>");
		}
		if(StringUtils.isBlank(txtCity.getValue())){
			hasError = true;
			errMsg.append("Please enter the Consultation City. </br>");
		}
		if(StringUtils.isBlank(txtHospitalName.getValue())){
			hasError = true;
			errMsg.append("Please enter the Consultation Hospital Name. </br>");
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
		
		bean.getBenefitsAvailedDto().setCity(txtCity.getValue());
		bean.getBenefitsAvailedDto().setHospitalName(selectedHospital != null && selectedHospital.getName() != null ? selectedHospital.getName() : txtHospitalName.getValue());
		bean.getBenefitsAvailedDto().setHospitalAddress(txtHospitalAddress.getValue());
		bean.getBenefitsAvailedDto().setHospitalContactNumber(hospitalContactNumber.getValue());
		bean.getBenefitsAvailedDto().setHospitalFaxNo(hospitalFaxNo.getValue());
		bean.getBenefitsAvailedDto().setHospitalDoctorName(hospitalDoctorName.getValue());
		bean.getBenefitsAvailedDto().setConsulationProvider(providerType.getValue().equals(Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE);
		if(bean.getBenefitsAvailedDto().getConsulationProvider() != null
				&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.TRUE)){
			bean.getBenefitsAvailedDto().setConsulationHospitalType(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID);
		} else if(bean.getBenefitsAvailedDto().getConsulationProvider() != null
				&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.FALSE)){
			bean.getBenefitsAvailedDto().setConsulationHospitalType(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
			bean.getBenefitsAvailedDto().setClinic(chkClinic != null && chkClinic.getValue());
		}
		
		List<OPSpecialityDTO> specialityList = specialityListener.getValues();
		if(specialityList != null && !specialityList.isEmpty()){
			bean.setSpecialityDTOList(specialityList);
		}
		
	}
	
	public void clearTable(){
		specialityListener.removeAllItems();
	}
	
	public void clearAllFieldsValues(){
		hospitalContactNumber.clear();
		txtHospitalName.clear();
		txtCity.clear();
		txtState.clear();
		hospitalFaxNo.clear();
		hospitalDoctorName.clear();
		txtHospitalAddress.clear();
	}
	
	public void addFieldValues(){
		if(bean.getBenefitsAvailedDto().getState() != null){
			txtState.setValue(bean.getBenefitsAvailedDto().getState());
		}
		if(bean.getBenefitsAvailedDto().getCity() != null){
			txtCity.setValue(bean.getBenefitsAvailedDto().getCity());
		}
		if(bean.getBenefitsAvailedDto().getHospitalName() != null){
			txtHospitalName.setValue(bean.getBenefitsAvailedDto().getHospitalName());
		}
		if(bean.getBenefitsAvailedDto().getHospitalContactNumber() != null){
			hospitalContactNumber.setValue(bean.getBenefitsAvailedDto().getHospitalContactNumber());
		}
		if(bean.getBenefitsAvailedDto().getHospitalDoctorName() != null){
			hospitalDoctorName.setValue(bean.getBenefitsAvailedDto().getHospitalDoctorName());
		}
		if(bean.getBenefitsAvailedDto().getHospitalAddress() != null){
			txtHospitalAddress.setValue(bean.getBenefitsAvailedDto().getHospitalAddress());
		}
	}
}
