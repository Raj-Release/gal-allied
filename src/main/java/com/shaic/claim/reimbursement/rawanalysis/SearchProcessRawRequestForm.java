package com.shaic.claim.reimbursement.rawanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;		
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.xerces.impl.dtd.models.CMBinOp;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
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

public class SearchProcessRawRequestForm extends SearchComponent<SearchProcessRawRequestFormDto>{
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cmbCpuCode;
	private ComboBox intiatedFrom;
	private ComboBox cmbClaimType;
	private DateField fromDate;
	private DateField toDate;
	/*private AutocompleteField<HospitalDto> cmbHospital;*/
	private AutocompleteTextField cmbHospitals ;	

	@EJB
	private HospitalService hospitalService;
	
	private  SearchProcessRawRequestFormDto searchDto = null;
	
	
	
	/*public AutocompleteField<HospitalDto> getCmbHospital() {
		return cmbHospital;
	}

	public void setCmbHospital(AutocompleteField<HospitalDto> cmbHospital) {
		this.cmbHospital = cmbHospital;

	}*/
	public AutocompleteTextField getCmbHospitals() {		
		return cmbHospitals;		
	}		

	public void setCmbHospitals(AutocompleteTextField cmbHospitals) {		
		this.cmbHospitals = cmbHospitals;		
	}
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process RAW Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		
		mainVerticalLayout = new VerticalLayout();
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo", TextField.class);
		cmbCpuCode = binder.buildAndBind("Cpu Code", "cpuCode", ComboBox.class);
		intiatedFrom = binder.buildAndBind("Initiated From", "intiatedFrom", ComboBox.class);
		cmbClaimType = binder.buildAndBind("Claim Type", "claimType", ComboBox.class);
		/*fromDate = binder.buildAndBind("From Date", "fromDate", DateField.class);
		toDate = binder.buildAndBind("To Date", "toDate", DateField.class);*/
		

		/*cmbHospital = new AutocompleteField<HospitalDto>();
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
		//Vaadin8-setImmediate() cmbHospital.setImmediate(true);
		cmbHospital.setRequiredError("Please select a valid Hospital");
		cmbHospital.setValidationVisible(false);
		cmbHospital.setCaption("Hospital Name");
		cmbHospital.setWidth("180px");
		cmbHospital.setIcon(null);*/
	
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
			
		
		/*FormLayout leftFormLayout = new FormLayout(txtIntimationNo,intiatedFrom,fromDate);
		FormLayout rightFormLayout = new FormLayout(txtPolicyNo,cmbClaimType,toDate);*/

		FormLayout leftFormLayout = new FormLayout(txtIntimationNo,intiatedFrom);
		FormLayout rightFormLayout = new FormLayout(txtPolicyNo,cmbClaimType);
//		FormLayout lastLayout = new FormLayout(cmbCpuCode,cmbHospitals);
		FormLayout lastLayout = new FormLayout(cmbCpuCode);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(leftFormLayout,rightFormLayout,lastLayout);
		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:120.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("950px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("184px");
		addListener();
		/*setUpAutoHospital(cmbHospital);*/
		/*addHospitalNameListener();*/
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessRawRequestFormDto>(SearchProcessRawRequestFormDto.class);
		searchDto = new SearchProcessRawRequestFormDto();
		this.binder.setItemDataSource(searchDto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> requestedBy,BeanItemContainer<SelectValue> statusByStage){
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		
		cmbClaimType.setContainerDataSource(requestedBy);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		intiatedFrom.setContainerDataSource(statusByStage);
		intiatedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		intiatedFrom.setItemCaptionPropertyId("value");
		
	}
	/*private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
		search.setQueryListener(new AutocompleteQueryListener<HospitalDto>() {
			@Override
			public void handleUserQuery(AutocompleteField<HospitalDto> field,
					String query) {
					handleHospitalSearchQuery(field, query);
			}
		});
	} */
	
		
	private void onAutocompleteHospitalSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		HospitalDto hospitalData = (HospitalDto) suggestion.getData();		
		if (suggestion != null) {		
			searchDto.setHospitalKey(hospitalData.getKey());		
			searchDto.setHospitalCode(hospitalData.getHospitalCode());		
			cmbHospitals.setValue(hospitalData.getName());		
		} 		
	}


	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {

		List<Hospitals> hospitalSearch = hospitalService
				.hospitalNameForRawCriteriaSearch(query.getTerm());

		if (!hospitalSearch.isEmpty()) {
			
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
	/*public void addHospitalNameListener()
	{
		cmbHospital
		.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<HospitalDto>() {

			@Override
			public void onSuggestionPicked(HospitalDto suggestion) {
				if (suggestion != null) {
					searchDto.setHospitalKey(suggestion.getKey());
					searchDto.setHospitalCode(suggestion.getHospitalCode());
					cmbHospital.setValue(suggestion.getName());
				} 
			}
		});
	}*/

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


			suggestions.clear();		
			handleHospitalSearchQuery(suggestions,query);		

			return suggestions;		

		}		
	}
}