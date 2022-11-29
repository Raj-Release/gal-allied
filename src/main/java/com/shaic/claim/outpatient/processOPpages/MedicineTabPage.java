/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.processOPpages.PhysioTherapyTabPage.CustomSuggestionProvider;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.UnFreezHospitals;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
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

/**
 * @author ntv.narasimhaj
 *
 */
public class MedicineTabPage extends ViewComponent{
	
	private BeanFieldGroup<BenefitsAvailedDTO> binder;
	
	private OutPatientDTO bean;
	
	private OptionGroup sameAsConsultation;
	
	
	private AutocompleteTextField txtPharmacytName;
	private TextField txtPharmacyAddress;
	
	private TextField dummyTextField;
	private TextField txtPharmacyContactNo;
	private TextField txtEmailId;
	
	private HorizontalLayout horLayout;
	
	private HorizontalLayout hLayout;
	
	private HospitalDto selectedHospital;
	
	@EJB
	private HospitalService hospitalService;
	
	private StringBuilder errMsg = new StringBuilder();
	
	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}
	
	
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
		
		sameAsConsultation = (OptionGroup) binder.buildAndBind("", "sameasMedicalConsultation", OptionGroup.class);
		sameAsConsultation.addItems(getReadioButtonOptions());
		sameAsConsultation.setItemCaption(true, "Yes");
		sameAsConsultation.setItemCaption(false, "No");
//		sameAsConsultation.select(false);
		sameAsConsultation.setCaption("Same as Consultation <b style= 'color: red'>*</b>"); 
		sameAsConsultation.setCaptionAsHtml(true);
		/*if(bean.getConsulation() != null && bean.getConsulation().equals(Boolean.FALSE)){
			sameAsConsultation.select(false);
		}*/
		if(bean.getBenefitsAvailedDto().getSameasMedicalConsultation() != null && bean.getBenefitsAvailedDto().getSameasMedicalConsultation().equals(Boolean.FALSE)
				&& bean.getBenefitsAvailedDto().getPharmacytName() != null && !bean.getBenefitsAvailedDto().getPharmacytName().isEmpty()){
			sameAsConsultation.setValue(false);
		}else if(bean.getBenefitsAvailedDto().getSameasMedicalConsultation() != null
				&& bean.getBenefitsAvailedDto().getSameasMedicalConsultation().equals(Boolean.TRUE)){
			sameAsConsultation.setValue(true);
		}
		
//		txtPharmacytName = (TextField) binder.buildAndBind("Pharmacy Name","pharmacytName",TextField.class);
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();
		txtPharmacytName = new AutocompleteTextField();
		txtPharmacytName.setCaption("Pharmacy Name"); 
		txtPharmacytName.setCaptionAsHtml(true);		
		txtPharmacytName.setItemAsHtml(false);		
		txtPharmacytName.setMinChars(2);		
		txtPharmacytName.setScrollBehavior(ScrollBehavior.NONE);		
		txtPharmacytName.setSuggestionLimit(0);		
		txtPharmacytName.setSuggestionProvider(suggestionProvider);		
		txtPharmacytName.addSelectListener(this::onAutocompleteHospitalSelect);		
//		txtPharmacytName.setRequiredIndicatorVisible(true);
		if(bean.getBenefitsAvailedDto().getPharmacytName() != null){
			txtPharmacytName.setValue(bean.getBenefitsAvailedDto().getPharmacytName());
		}
		
		txtPharmacyAddress = (TextField) binder.buildAndBind("Pharmacy Address","pharmacyAddress",TextField.class);
		 
		
		dummyTextField =  new TextField();
		dummyTextField.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		dummyTextField.setEnabled(false);

		txtPharmacyContactNo = (TextField) binder.buildAndBind("Pharmacy Contact No","pharmacyContactNo",TextField.class);
		txtPharmacyContactNo.setMaxLength(12);
		CSValidator contactNumber = new CSValidator();
		contactNumber.extend(txtPharmacyContactNo);
		contactNumber.setRegExp("^[0-9/]*$");
		contactNumber.setPreventInvalidTyping(true);
		txtEmailId = (TextField) binder.buildAndBind("Email Id","pharmacyemailId",TextField.class);
		CSValidator emailIdTxtValidator = new CSValidator();
		emailIdTxtValidator.extend(txtEmailId);
		emailIdTxtValidator.setRegExp("^[a-z A-Z 0-9 @ . _]*$");
		emailIdTxtValidator.setPreventInvalidTyping(true);
		
		FormLayout leftFormLayout  = new FormLayout(txtPharmacytName, txtPharmacyAddress);
		FormLayout rightFormLayout = new FormLayout(txtPharmacyContactNo, txtEmailId);
		
		horLayout = new HorizontalLayout(new FormLayout(sameAsConsultation));
		horLayout.setSpacing(false);
		horLayout.setMargin(true);
		
		hLayout = new HorizontalLayout(leftFormLayout,rightFormLayout);
		hLayout.setVisible(false);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		VerticalLayout vLayout = new VerticalLayout(horLayout, hLayout);
		setCompositionRoot(vLayout);
		vLayout.setCaption("Medicine Details");
		
		if(sameAsConsultation != null && sameAsConsultation.getValue() != null && !(Boolean)sameAsConsultation.getValue()/* != null && sameAsConsultation.getValue().toString().equals(Boolean.FALSE)*/){
			hLayout.setVisible(true);
		}
		
		addListener();
		
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
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
	
	private void handleHospitalSelection(HospitalDto suggestion) {
		if (suggestion != null) {
			selectedHospital = suggestion;

			if (suggestion.getKey() != null) {
				UnFreezHospitals registedHospitals = suggestion.getRegistedUnFreezHospitals();
				if (registedHospitals != null) {
					/*this.txtHospitalAddress.setValue(registedHospitals
							.getAddress());*/
					String hospAddress = (registedHospitals.getAddress() != null ? registedHospitals.getAddress() : "");
					
					this.txtPharmacyAddress.setValue(hospAddress != null ? hospAddress : "");					
					this.txtPharmacyContactNo.setValue(registedHospitals
							.getPhoneNumber());
					this.txtEmailId.setValue(registedHospitals
							.getEmailId());
					
					bean.getBenefitsAvailedDto().setHospitalName(registedHospitals.getName() != null ? registedHospitals.getName() : "");
					bean.getBenefitsAvailedDto().setHospitalAddress(hospAddress);
					bean.getBenefitsAvailedDto().setHospitalContactNumber(registedHospitals.getPhoneNumber() != null ? registedHospitals.getPhoneNumber() : "");
					bean.getBenefitsAvailedDto().setEmailId(registedHospitals.getEmailId() != null ? registedHospitals.getEmailId() : "");

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
			/*if(caption.equalsIgnoreCase(stateCaption)){		

				suggestions.clear();		
				handleStateSearchQuery(suggestions,query);		
			}	
			else if(caption.equalsIgnoreCase(cityCaption)){		
				handleCitySearchQuery(suggestions,query);		
			}else{*/		
				handleHospitalSearchQuery(suggestions,query);		
//			}		
			return suggestions;		

		}		
	}
	
	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		List<UnFreezHospitals> hospitalSearch = null;
		/*if(bean.getIsPaayasPolicy()){
			hospitalSearch = hospitalService
					.hospitalNameCriteriaSearchForPaayas(query.getTerm(), selectedState, selectedCity);
		}else{*/
			hospitalSearch = hospitalService
					.getHospitalsDetailsForOP(query.getTerm());
//		}

		if (!hospitalSearch.isEmpty()) {
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
	
	public void addListener(){
		sameAsConsultation.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isFinalChecked = false ;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "false") {
					hLayout.setVisible(true);
					txtPharmacytName.setValue("");
					txtPharmacyAddress.setValue("");
					txtPharmacyContactNo.setValue("");
					txtEmailId.setValue("");;
		
				} else {
					hLayout.setVisible(false);
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
			errMsg.append("Please Select Same as Consultation in Medicine. </br>");
		}
		if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
		{
			if(!isValidEmail(this.txtEmailId.getValue()))
			{
				hasError = true;
				errMsg.append("Please enter a valid email in Medicine</br>");
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
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	
	public void setValues(){
		
		if(sameAsConsultation != null && sameAsConsultation.getValue() != null &&  sameAsConsultation.getValue().equals(Boolean.TRUE)){
			bean.getBenefitsAvailedDto().setPharmacytName(bean.getBenefitsAvailedDto().getHospitalName());
			bean.getBenefitsAvailedDto().setPharmacyAddress(bean.getBenefitsAvailedDto().getHospitalAddress());
			bean.getBenefitsAvailedDto().setPharmacyContactNo(bean.getBenefitsAvailedDto().getHospitalContactNumber());
			bean.getBenefitsAvailedDto().setPharmacyemailId(txtEmailId.getValue());
			bean.getBenefitsAvailedDto().setSameasMedicalConsultation(true);
		} else {
			bean.getBenefitsAvailedDto().setPharmacytName(selectedHospital != null && selectedHospital.getName() != null ? selectedHospital.getName() : txtPharmacytName.getValue());
			bean.getBenefitsAvailedDto().setPharmacyAddress(txtPharmacyAddress.getValue());
			bean.getBenefitsAvailedDto().setPharmacyContactNo(txtPharmacyContactNo.getValue());
			bean.getBenefitsAvailedDto().setPharmacyemailId(txtEmailId.getValue());
			bean.getBenefitsAvailedDto().setSameasMedicalConsultation(false);
		}

	}
	
	public void resetValues(){
		bean.getBenefitsAvailedDto().setPharmacytName("");
		bean.getBenefitsAvailedDto().setPharmacyAddress("");
		bean.getBenefitsAvailedDto().setPharmacyContactNo("");
		bean.getBenefitsAvailedDto().setPharmacyemailId("");
		bean.getBenefitsAvailedDto().setSameasMedicalConsultation(false);
	}


}
