package com.shaic.claim.policy.search.ui.opsearch;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.jensjansson.pagedtable.PagedTable;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TPolicyDetails;
import com.shaic.domain.TPolicyMaster;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class OPRegisterClaimPolicyUI extends ViewComponent {

	private static final long serialVersionUID = 4174091244258792154L;
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();
	
//	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService dbcalculationService;
	
//	@Inject
//	private IntimationService intimationService;
//	
//	@Inject
//	private ViewPreviousIntimationTable viewPreviousIntimationTable;
	
	private Panel panel_2;
	
	private VerticalLayout verticalLayout_2;
	
	private VerticalLayout wholeVerticalLayout;
	
	private VerticalLayout mainVerticalLayout;
	
	private Button resetButton;
	
	private Button submitButton;
	
	private TextField insuredName;
	
//	private Label insuredNameLabel;
	
	private TextField proposerName;
	
//	private Label proposerNameLabel;
	
	private PopupDateField proposerDOB;
	
//	private Label dobLabel;
	
//	private ComboBox productType;
	
//	private Label productTypeLabel;
	
	private TextField helathCardNo;
	
//	private Label healthCardLabel;
	
//	private Label mobileNumberLabel;
	
	private TextField mobileNumber;
	
//	private ComboBox policyCodeOrName;
	
//	private Label policyCodeLabel;
	
//	private ComboBox productName;
	
//	private Label productNameLabel;
	
//	private TextField receiptNumber;
	
//	private Label receiptNoLabel;
	
	private TextField policyNumber;
	
	private TextField intimationNumber;
	
	private TextField claimNumber;
	
//	private Label policyNoLabel;
	
	private ComboBox searchOptions;
	
	private Boolean isBancs = false;
	
	private Label searchByLabel;
	
//	private Label searchPolicyLabel;
	
	private PagedTable policyDetailsTable;
	
	private FormLayout  searchGridLayout;
	
	private FormLayout  secondFormLayout;
	
//	@Inject
//	private CashlessTable cashlessTable;

//	@Inject
//	private NewIntimationService newIntimationService;


//	@Inject
//	private CashLessTableDetails cashLessTableDetails;
	
	private HorizontalLayout buildSearchByPolicyLayout;
	
	
//	@Inject
//	private Instance<ViewProductConditions> productCondition;
	
	@Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;
	
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ViewDetails viewDetails;
	
	
//	private static Properties properties;
//	private static String dataDir = System.getProperty("jboss.server.data.dir");
	
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
	//	wholeVerticalLayout.addStyleName("view");
		wholeVerticalLayout.addComponent(buildMainLayout());
		wholeVerticalLayout.setComponentAlignment(panel_2, Alignment.MIDDLE_LEFT);
		setCompositionRoot(wholeVerticalLayout);
	}

	
	private Panel buildMainLayout() {
		panel_2 = buildPanel_2();
		return panel_2;
	}

	
	private Panel buildPanel_2() {
		panel_2 = new Panel();
		//Vaadin8-setImmediate() panel_2.setImmediate(false);
		panel_2.setCaption("Search/Create Intimation");
//		panel_2.setWidth("100%");
//		panel_2.setHeight("40%");
		panel_2.addStyleName("panelHeader");
		panel_2.addStyleName("g-search-panel");

		verticalLayout_2 = buildVerticalLayout_2();
		panel_2.setContent(verticalLayout_2);

		return panel_2;
	}

	
	private VerticalLayout buildVerticalLayout_2() {
		verticalLayout_2 = new VerticalLayout();
		//Vaadin8-setImmediate() verticalLayout_2.setImmediate(false);
//		verticalLayout_2.setWidth("100.0%");
//		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(false);
		
		mainVerticalLayout = buildAbsoluteLayout_3();
		verticalLayout_2.addComponent(mainVerticalLayout);
		
		return verticalLayout_2;
	}

	public VerticalLayout buildAbsoluteLayout_3() {
		// common part: create layout
		mainVerticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
//		mainVerticalLayout.setWidth("100.0%");
		mainVerticalLayout.setSpacing(true);
		mainVerticalLayout.setMargin(true);
//		absoluteLayout_3.addStyleName("view");
	
//		searchByLabel = new Label ("<b>Search By<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b>",Label.CONTENT_TEXT.HTML);
//		//Vaadin8-setImmediate() searchByLabel.setImmediate(false);
//		searchByLabel.setWidth("100.0%");
//		searchByLabel.setHeight("-1px");
//		mainVerticalLayout.addComponent(searchByLabel);
		
		// searchOptions
		searchOptions = new ComboBox("Type");
		//Vaadin8-setImmediate() searchOptions.setImmediate(true);
		searchOptions.setWidth("-1px");
		searchOptions.setContainerDataSource(getOPType());
		searchOptions.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		searchOptions.setItemCaptionPropertyId("value");
		
		// Set Default Value to First Option.
//		Collection<?> itemIds = searchOptions.getContainerDataSource().getItemIds();
//		searchOptions.setValue(itemIds.toArray()[0]);
//		searchOptions.setNullSelectionAllowed(false);
		
		searchOptions.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -4820170898280727113L;

			public void valueChange(ValueChangeEvent valueChangeEvent) {
				fireViewEvent(OPRegisterClaimSearchPolicyPresenter.SERACH_OPTION_CHANGES, valueChangeEvent);
				//Vaadin8-setImmediate() searchOptions.setImmediate(true);
				removeTableFromLayout();
			}
		});
		//Vaadin8-setImmediate() searchOptions.setImmediate(true);
		
//		searchGridLayout = new FormLayout();
//		searchGridLayout.addComponent(searchOptions);
		
//		FormLayout firstFormLayout = new FormLayout();
//		firstFormLayout.addComponent(searchOptions);
//		firstFormLayout.setWidth("100%");
//		firstFormLayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(buildTypeLayout());
//		buildSearchByPolicyLayout = buildCreateIntimationLayout();
//		mainVerticalLayout.addComponent(buildSearchByPolicyLayout);
		
		// searchByInsuredLayout
		//searchByInsuredLayout = buildSearchByInsuredLayout();
		//absoluteLayout_3.addComponent(searchByInsuredLayout,"top:85.0px;bottom:31.0px;left:25.0px;");
		return mainVerticalLayout;
	}
	
	private FormLayout buildTypeLayout() {
		FormLayout firstFormLayout = new FormLayout();
		firstFormLayout.addComponent(searchOptions);
		firstFormLayout.setWidth("100%");
		firstFormLayout.setHeight("60px");
		firstFormLayout.setSpacing(true);
		return firstFormLayout;
	}
	
	private AbsoluteLayout buildButtonLayout() {
		// submitButton
		submitButton = new Button();
		submitButton.setCaption("Search");
		//Vaadin8-setImmediate() submitButton.setImmediate(true);
		submitButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.setDisableOnClick(true);

		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4854461697920987945L;
			@Override
			public void buttonClick(ClickEvent event) {
				submitButton.setEnabled(true);
				if(((SelectValue)searchOptions.getValue()).getValue().equalsIgnoreCase("Create Intimation")){
					fireViewEvent(OPRegisterClaimSearchPolicyPresenter.CREATE_INTIMATION_SUBMIT, null);
				}
				if(((SelectValue)searchOptions.getValue()).getValue().equalsIgnoreCase("Search Intimation")){
					fireViewEvent(OPRegisterClaimSearchPolicyPresenter.SEARCH_INTIMATION_SUBMIT, null);
				}
			}
		});
		// resetButton
		resetButton = new Button();
		resetButton.setCaption("Reset");
		//Vaadin8-setImmediate() resetButton.setImmediate(true);
		resetButton.setTabIndex(10);
		resetButton.addStyleName(ValoTheme.BUTTON_DANGER);
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		resetButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5472521730648387559L;
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OPRegisterClaimSearchPolicyPresenter.RESET_VALUES, null);
			}
		});

		AbsoluteLayout searchIntimation_layout =  new AbsoluteLayout();
		searchIntimation_layout.addComponent(submitButton, "top:30.0px;left:220.0px;");
		searchIntimation_layout.addComponent(resetButton, "top:30.0px;left:329.0px;");
		//Vaadin8-setImmediate() searchIntimation_layout.setImmediate(false);
		searchIntimation_layout.setWidth("100.0%");
		searchIntimation_layout.setHeight("80px");
		
		return searchIntimation_layout;
	}

	
	private HorizontalLayout buildCreateIntimationLayout() {
		// common part: create layout
		
		/*if(searchGridLayout == null) {
			searchGridLayout = new FormLayout();
			searchGridLayout.setWidth("100%");
			//Vaadin8-setImmediate() searchGridLayout.setImmediate(true);
<<<<<<< HEAD
=======
//			searchGridLayout.setWidth("820px");
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
			searchGridLayout.setHeight("-1px");
			searchGridLayout.setSpacing(true);
			
			secondFormLayout = new FormLayout();
			secondFormLayout.setWidth("100%");
			//Vaadin8-setImmediate() secondFormLayout.setImmediate(true);
<<<<<<< HEAD
=======
//			searchGridLayout.setWidth("820px");
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
			secondFormLayout.setHeight("-1px");
			secondFormLayout.setSpacing(true);
		} else {
			Component comp = null;
			for(int i = 0; i < searchGridLayout.getComponentCount(); i++ ) {
				Component component = searchGridLayout.getComponent(i);
				if(component.getCaption().toLowerCase().contains("Type")) {
					comp = component;
				}
			}
			searchGridLayout.removeAllComponents();
			searchGridLayout.addComponent(comp);
			if(secondFormLayout != null) {
				secondFormLayout.removeAllComponents();
			} else {
				secondFormLayout = new FormLayout();
			}
		}*/
		FormLayout policyLayout = new FormLayout();		
		policyLayout.setWidth("100%");
		policyLayout.setSpacing(false);
		
		// policyNumber
		policyNumber = new TextField("Policy Number");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.setMaxLength(25);
		
		// helathCardNo
		FormLayout healthLayout = new FormLayout();		
		healthLayout.setWidth("100%");
		healthLayout.setSpacing(false);
		
		helathCardNo = new TextField("Health Card No");
		helathCardNo.setEnabled(true);
		//Vaadin8-setImmediate() helathCardNo.setImmediate(true);
		helathCardNo.setWidth("180px");
		helathCardNo.setHeight("-1px");
		helathCardNo.setMaxLength(25);

//		secondFormLayout.addComponent(policyNumber);
//		secondFormLayout.addComponent(helathCardNo);
		
		/*if(buildSearchByPolicyLayout == null) {
			buildSearchByPolicyLayout = new HorizontalLayout();
		} else {
			buildSearchByPolicyLayout.removeAllComponents();
		}*/
		HorizontalLayout holderLayout = new HorizontalLayout();
		policyLayout.addComponent(policyNumber);
		healthLayout.addComponent(helathCardNo);
		
		holderLayout.addComponent(policyLayout);
		holderLayout.addComponent(healthLayout);
		holderLayout.setSpacing(true);
		holderLayout.setHeight("80px");
		
		return holderLayout;
}
	
	private HorizontalLayout buildSearchIntimationLayout() {

		FormLayout policyLayout = new FormLayout();		
		policyLayout.setWidth("100%");
		policyLayout.setSpacing(false);

		// intimationNumber
		intimationNumber = new TextField("Intimation Number");
		intimationNumber.setWidth("180px");
		intimationNumber.setMaxLength(25);
		
		claimNumber = new TextField("Claim Number");
		claimNumber.setWidth("180px");

		// policyNumber
		policyNumber = new TextField("Policy Number");
		policyNumber.setWidth("180px");
		policyNumber.setMaxLength(25);

		// helathCardNo
		FormLayout healthLayout = new FormLayout();		
		healthLayout.setWidth("100%");
		healthLayout.setSpacing(false);

		helathCardNo = new TextField("Health Card No");
		helathCardNo.setWidth("180px");
		helathCardNo.setMaxLength(25);

		//		secondFormLayout.addComponent(policyNumber);
		//		secondFormLayout.addComponent(helathCardNo);

		/*if(buildSearchByPolicyLayout == null) {
			buildSearchByPolicyLayout = new HorizontalLayout();
		} else {
			buildSearchByPolicyLayout.removeAllComponents();
		}*/
		HorizontalLayout holderLayout = new HorizontalLayout();
		policyLayout.addComponent(intimationNumber);
		policyLayout.addComponent(policyNumber);
		healthLayout.addComponent(claimNumber);
		healthLayout.addComponent(helathCardNo);

		holderLayout.addComponent(policyLayout);
		holderLayout.addComponent(healthLayout);
		holderLayout.setSpacing(true);
		holderLayout.setHeight("80px");

		return holderLayout;
}

	
/*	private HorizontalLayout buildSearchByInsuredLayout() {
		
		if(searchGridLayout == null) {
			searchGridLayout = new FormLayout();
			//Vaadin8-setImmediate() searchGridLayout.setImmediate(true);
			searchGridLayout.setWidth("820px");
			searchGridLayout.setHeight("-1px");
		//	searchGridLayout.setMargin(true);
			searchGridLayout.setSpacing(true);
			secondFormLayout = new FormLayout();
			//Vaadin8-setImmediate() secondFormLayout.setImmediate(true);
			secondFormLayout.setWidth("820px");
			secondFormLayout.setHeight("-1px");
		//	searchGridLayout.setMargin(true);
			secondFormLayout.setSpacing(true);
		} else {
			Component comp = null;
			for(int i = 0; i < searchGridLayout.getComponentCount(); i++ ) {
				Component component = searchGridLayout.getComponent(i);
				if(component.getCaption().toLowerCase().contains("search policy")) {
					comp = component;
				}
			}
			searchGridLayout.removeAllComponents();
			searchGridLayout.addComponent(comp);
			if(secondFormLayout != null) {
				secondFormLayout.removeAllComponents();
			} else {
				secondFormLayout = new FormLayout();
			}
		}
		
		CSValidator nameValidator = new CSValidator();
		
		// proposerName
		proposerName = new TextField("Proposer Name");
		proposerName.setEnabled(true);
		//Vaadin8-setImmediate() proposerName.setImmediate(true);
		proposerName.setWidth("180px");
		proposerName.setHeight("-1px");
		proposerName.setMaxLength(50);
		proposerName.setTabIndex(3);
		nameValidator.extend(proposerName);
		nameValidator.setRegExp("^[a-zA-Z 0-9. /']*$");
		nameValidator.setPreventInvalidTyping(true);
		searchGridLayout.addComponent(proposerName);
		
		// proposerDOB
		proposerDOB = new PopupDateField("DOB");
		proposerDOB.setEnabled(true);
		//Vaadin8-setImmediate() proposerDOB.setImmediate(true);
		proposerDOB.setWidth("180px");
		proposerDOB.setTabIndex(4);
		proposerDOB.setHeight("-1px");
		proposerDOB.setDateFormat("dd/MM/yyyy");
		proposerDOB.setTextFieldEnabled(false);
		searchGridLayout.addComponent(proposerDOB);
	    
	    CSValidator mobileValidator=new CSValidator();
	    mobileNumber=new TextField("Registered Mobile Number");
	    mobileNumber.setEnabled(true);
	    mobileNumber.setMaxLength(12);
	    //Vaadin8-setImmediate() mobileNumber.setImmediate(true);
	    mobileNumber.setWidth("180px");
	    mobileNumber.setTabIndex(5);
	    mobileNumber.setHeight("-1px");
	    mobileValidator.extend(mobileNumber);
	    mobileValidator.setRegExp("^[0-9/]*$");
	    mobileValidator.setPreventInvalidTyping(true);
	    secondFormLayout.addComponent(mobileNumber);
		
		// insuredName
		CSValidator insuredNameValidator = new CSValidator();
		insuredName = new TextField("Insured Name");
		insuredName.setEnabled(true);
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("180px");
		insuredName.setTabIndex(6);
		insuredName.setHeight("-1px");
		insuredName.setMaxLength(50);
		insuredNameValidator.extend(insuredName);
		insuredNameValidator.setRegExp("^[a-zA-Z 0-9./s ']*$");
		insuredNameValidator.setPreventInvalidTyping(true);
		secondFormLayout.addComponent(insuredName);
		
		if(buildSearchByPolicyLayout == null) {
			buildSearchByPolicyLayout = new HorizontalLayout();
		} else {
			buildSearchByPolicyLayout.removeAllComponents();
		}
		buildSearchByPolicyLayout.addComponent(searchGridLayout);
		buildSearchByPolicyLayout.addComponent(secondFormLayout);
//		buildSearchByPolicyLayout.setWidth("60%");
		buildSearchByPolicyLayout.setSpacing(true);
		
		return buildSearchByPolicyLayout;
	}*/
	
	public void resetAlltheValues() {
		SelectValue masterRecord = (SelectValue) searchOptions.getValue();
		String selectedValue = masterRecord.getValue();
		
		if("Create Intimation".equalsIgnoreCase(selectedValue)) {
			policyNumber.setValue("");
			helathCardNo.setValue("");
			removeTableFromLayout();
		}
		
		if("Search Intimation".equalsIgnoreCase(selectedValue)) {
			intimationNumber.setValue("");
			policyNumber.setValue("");
			helathCardNo.setValue("");
			claimNumber.setValue("");
			removeTableFromLayout();
		}
		
		
		/*Iterator<Component> policyComponentIterator = searchGridLayout.iterator();
		resetComponentValues(policyComponentIterator);
		if(secondFormLayout != null) {
			Iterator<Component> insuredComponentIterator = secondFormLayout.iterator();
			resetComponentValues(insuredComponentIterator);
		}*/
		
	}	
	
/*	private void resetComponentValues(Iterator<Component> iterator) {
		while(iterator.hasNext()) {
			Component eachComponent = iterator.next() ;
			
			if(eachComponent != null && !eachComponent.getCaption().toLowerCase().contains("search policy")) {
				
				String className = eachComponent.getClass().toString();
				if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.Label")) {
				   continue;
				}
				
				if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.TextField")) {
				    TextField field = (TextField) eachComponent;
				    field.setValue("");
				} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.ComboBox")) {
					ComboBox field = (ComboBox) eachComponent;
					field.setValue(null);
				} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.DateField")) {
					DateField field = (DateField) eachComponent;
					field.setValue(null);
				} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.PopupDateField")) {
					PopupDateField field = (PopupDateField) eachComponent;
					field.setValue(null);
				}
				// Remove the table if exists..	
				removeTableFromLayout();
			}
		}
	}*/
	
	private void removeTableFromLayout(){
		// Remove the Table also if it exists.
		ArrayList<Component> componentArray = new ArrayList<Component>();
		int componentCount = wholeVerticalLayout.getComponentCount();
		for (int i=0; i < componentCount; i++) {
			Component eachComponent = wholeVerticalLayout.getComponent(i);
			String componentClass = eachComponent.getClass().toString();
			if(StringUtils.equalsIgnoreCase(componentClass, "class com.vaadin.ui.Panel")){
			   continue;
			}
			componentArray.add(eachComponent);
		}
		for (int i=0; i < componentArray.size(); i++) {
			wholeVerticalLayout.removeComponent(componentArray.get(i));
		}
	}
	
	private String getValueFromComponent(Object component){
		if(component instanceof TextField) {
			TextField field = (TextField) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue();
		} else if(component instanceof ComboBox) {
			ComboBox field = (ComboBox) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue().toString();
		} else if(component instanceof PopupDateField) {
			PopupDateField field = (PopupDateField) component; 
			if(field.getValue() == null ||  field.getValue().toString().length() == 0){
				return null;
			}
			try {
				field.validate();
				return field.getValue().toString();
			} catch (Exception e) {
				field.setValue(null);
				showErrorPopup("Please Enter a valid Date of Birth");
			}		
	  	 
		}
		return null;
	}
	
	private String getValueFromMasterTable(ComboBox component){
		 
		if(component!= null && component.getValue() == null || component.getValue() == ""){
			return null;
		}
		 
		if(component.getValue() instanceof MastersValue) {
			return ((MastersValue) component.getValue()).getValue();
		} else if(component.getValue() instanceof OrganaizationUnit) {
			return ((OrganaizationUnit) component.getValue()).getOrganizationUnitId();
		}  else if(component.getValue() instanceof Product) {
			return ((Product) component.getValue()).getCode();
		}
		
			return null;
	}
	
	public Boolean validateFields() {
		// Always clear the Map.
		enteredValues.clear();
		SelectValue masterRecord = null;
		String selectedValue = "";
		if(searchOptions.getValue() != null && !StringUtils.equals(searchOptions.getValue().toString(),"")) {
			
			masterRecord = (SelectValue) searchOptions.getValue();	
			selectedValue = masterRecord.getValue();
			
			if("Create Intimation".equalsIgnoreCase(selectedValue)) {
				enteredValues.put("polNo", getValueFromComponent(policyNumber));
				enteredValues.put("healthCardNumber", getValueFromComponent(helathCardNo));
				
			} 
			if("Search Intimation".equalsIgnoreCase(selectedValue)) {
				enteredValues.put("intimationNo", getValueFromComponent(intimationNumber));
				enteredValues.put("polNo", getValueFromComponent(policyNumber));
				enteredValues.put("healthCardNumber", getValueFromComponent(helathCardNo));
				enteredValues.put("claimNo",getValueFromComponent(claimNumber));
			}
			
			/*else if("Proposer/Insured".equalsIgnoreCase(selectedValue)) {
				enteredValues.put("polAssrName", getValueFromComponent(proposerName));
				enteredValues.put("insuredName", getValueFromComponent(insuredName));
				if( !insuredName.getValue().isEmpty()){
					enteredValues.put("insuredDateOfBirth", getValueFromComponent(proposerDOB));
					String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
					if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
						enteredValues.put("insuredDateOfBirth", SHAUtils.formatDateForSearch(getValueFromComponent(proposerDOB)));
					}
				
				}else{
					enteredValues.put("insuredDateOfBirth", null);
				}
				if( !proposerName.getValue().isEmpty()){
					enteredValues.put("proposerDateOfBirth", getValueFromComponent(proposerDOB));
					}else{
						enteredValues.put("proposerDateOfBirth", null);
					}
				if(proposerName.getValue().isEmpty() && insuredName.getValue().isEmpty()){
					String errorMessage = "Please enter either Insured Name or Proposer Name ";
					showErrorPopup(errorMessage);
					return false;
				}
				if(!(proposerDOB.getValue() == null)){
					enteredValues.put("proposerDateOfBirth", getValueFromComponent(proposerDOB));
				}
				enteredValues.put("registerdMobileNumber",getValueFromComponent(mobileNumber));
			}*/
			
		}
		// Remove null values key, value pair from MAP.
		enteredValues.values().removeAll(Collections.singleton(null));
		
		if(enteredValues.isEmpty() ) {
			String errorMessage = "";
			
			if("Create Intimation".equalsIgnoreCase(selectedValue)) {
				errorMessage = "Please provide either Policy Number or Health Card No";
			}
			if("Search Intimation".equalsIgnoreCase(selectedValue)) {
				errorMessage = "Please provide either Intimation Number or Policy Number or Health Card No";
			}
			showErrorPopup(errorMessage);
			return false;
		}
		
		if(searchOptions.getValue() != null || searchOptions.getValue() == "") {
//			SelectValue masterRecord = (SelectValue) searchOptions.getValue();	
//			String selectedValue = masterRecord.getValue();

			if("Create Intimation".equalsIgnoreCase(selectedValue)) {
				if(enteredValues.containsKey("polNo") && enteredValues.containsKey("healthCardNumber")) {
					String errorMessage = "Search is not applicable for both Policy number and Health Card Number";
					showErrorPopup(errorMessage);
					return false;
				}
				
				//Prevent create intimation for expired policies
				
				

				if((enteredValues.containsKey("polNo"))){
					
					String policyNumberInput = enteredValues.get("polNo");
					
					if(policyNumberInput != null){
						
						System.out.println(String.format("Policy Number [%s]", policyNumberInput));
						Policy dbPolicy = policyService.getByPolicyNumber(policyNumberInput);
						if(dbPolicy != null){
							Date dbpolicystartDate = dbPolicy.getPolicyFromDate();
							Date dbpolicyEndDate = dbPolicy.getPolicyToDate();
							Date currentDate= new Date();
							Boolean opAllowIntimation = (dbPolicy.getOpAllowIntimation() !=null && dbPolicy.getOpAllowIntimation().equalsIgnoreCase(SHAConstants.YES_FLAG))?true : false;
							
							
							if(dbpolicystartDate != null && dbpolicyEndDate!=null){
								Boolean isPolicyExpired = false;
								if(!SHAUtils.isDateOfIntimationWithPolicyRange(dbpolicystartDate, dbpolicyEndDate, currentDate)){
									isPolicyExpired = true;
								}
								if(isPolicyExpired && !opAllowIntimation){
									
									String errorMessage = "Policy has expired";
									showErrorPopup(errorMessage);
									return false;
								}
							}
							
						}
						else{
							BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremiaforOP(enteredValues);
							
							PremPolicy premiaPolicy = null;
							
							if(!policyContainer.getItemIds().isEmpty()){
								
								 premiaPolicy = policyContainer.getItemIds().get(0);
								 
							}
							
							if(premiaPolicy !=null){
								SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
								Date premiPolicystartDate=null;
								Date premiaPlicyEndDate=null;
								try {
									premiPolicystartDate = formatter.parse(premiaPolicy.getDateofInception());
									premiaPlicyEndDate = formatter.parse(premiaPolicy.getPolicyEndDate());
								} catch (ParseException e) {
									e.printStackTrace();
								}
									
								Date currentDate= new Date();
								
								if(premiPolicystartDate != null && premiaPlicyEndDate!=null){
									Boolean isPolicyExpired = false;
									if(!SHAUtils.isDateOfIntimationWithPolicyRange(premiPolicystartDate, premiaPlicyEndDate, currentDate)){
										isPolicyExpired = true;
									}
									if(isPolicyExpired){
										
										String errorMessage = "Policy has expired";
										showErrorPopup(errorMessage);
										return false;
									}
								}
								
							}else{
								System.out.println(String.format("Policy doesn't exist in DB & Premia [%s]", policyNumberInput ));
							}							
						}
						
					}
					
					
					
				}
				
			} /*else if("Proposer/Insured".equalsIgnoreCase(selectedValue)) {
				if(enteredValues.size() > 2 || enteredValues.size() == 1) {
					String errorMessage = "Search is only applicable for Proposer Name with DOB or Insured Name with DOB or Proposer name With Mobile number or Insured Name with Mobile number";
					showErrorPopup(errorMessage);
					return false;
				}

				if(enteredValues.containsKey("insuredName") && enteredValues.containsKey("polAssrName")) {
					String errorMessage = "Search is only applicable for Proposer Name with DOB or Insured Name with DOB or Proposer name With Mobile number or Insured Name with Mobile number";
					showErrorPopup(errorMessage);
					return false;
				}

				if(enteredValues.containsKey("insuredDateOfBirth") && enteredValues.containsKey("registeredMobileNumber")) {
					String errorMessage = "Search is only applicable for Proposer Name with DOB or Insured Name with DOB or Proposer name With Mobile number or Insured Name with Mobile number";
					showErrorPopup(errorMessage);
					return false;
				}
			}*/
		}
		
		return true;
	}
	
	private PagedTable retrieveCorrespondingValues() {
		SelectValue masterRecord = (SelectValue) searchOptions.getValue();
		String selectedValue = masterRecord.getValue();
		if("Create Intimation".equalsIgnoreCase(selectedValue)) {
			return retrieveCorrespondingValuesFromPremia();
		}
		if("Search Intimation".equalsIgnoreCase(selectedValue)) {
			return retrieveOPIntimationFromDB();
		}

		return null;

		//		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		//		 if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
		//			 return retrieveCorrespondingValuesFromPremia();
		//		 } else {
		//			 return retrieveOPIntimationFromDB();
		////			 return null;
		//		 }
	}
	
	/*private void loadSettingsPropertyFile()
	{
		InputStream input = null;
		try
		{
			input = new FileInputStream(dataDir + "/" + SHAConstants.SETTINGS_PROPERTY);
			properties = new Properties();
			properties.load(input);
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		finally{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/
		
		
		
		
		
	
	private PagedTable retrieveCorrespondingValuesFromPremia()
	{
		PagedTable table = new PagedTable();
		policyDetailsTable = new PagedTable("Intimation Table");
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setSizeFull();
		table.removeAllItems();

		try {
				if(validateFields()) {
//					Add New Method for OP because PolicySource not using
//					BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
					
					BeanItemContainer<PremPolicy> policyContainer = null;
					if(enteredValues.get("polNo") != null || enteredValues.get("healthCardNumber") != null){
						TPolicyMaster masterPolicy = null;
						TPolicyDetails policyDetails = null;
						if(enteredValues.get("polNo") != null){
							masterPolicy = policyService.getTPolicyMaster(enteredValues.get("polNo"));
						} else {
							policyDetails = policyService.getTPolicyDetails(enteredValues.get("healthCardNumber"));
						}
						if(masterPolicy != null || policyDetails != null){
							policyContainer = policyService.getBancsPolicySummaryDtls(enteredValues);
							isBancs = true;
						} else {
							policyContainer = policyService.filterPolicyDetailsPremiaforOP(enteredValues);
							isBancs = false;
						}
					} else {
						policyContainer =  policyService.filterPolicyDetailsPremiaforOP(enteredValues);
					}
					if(searchOptions.getValue() != null || searchOptions.getValue() == "") {
						SelectValue masterRecord = (SelectValue) searchOptions.getValue();	
						String selectedValue = masterRecord.getValue();
						
						if("Policy".equalsIgnoreCase(selectedValue)) {
							
						} else if("Proposer/Insured".equalsIgnoreCase(selectedValue)) {
							if((enteredValues.containsKey("polAssrName") || enteredValues.containsKey("insuredDateOfBirth"))|| (enteredValues.containsKey("registeredMobileNumber") )){
								String productType=null;
								String mobile=null;
							if(enteredValues.containsKey("productType"))
							{
								productType=enteredValues.get("productType");
							}
							
							if(enteredValues.containsKey("registeredMobileNumber"))
							{
								mobile=enteredValues.get("registeredMobileNumber");
							}
							
								Date value = proposerDOB.getValue();
							} 
							else if(enteredValues.containsKey("insuredName") && enteredValues.containsKey("insuredDateOfBirth")) {
								Date value = proposerDOB.getValue();
							}
							else {
								//return null;
							}
						}
					}
					if(policyContainer.getContainerPropertyIds().isEmpty()){
						return table;
					}
					table.setContainerDataSource(policyContainer);
					
					/*table.addGeneratedColumn("Health Card No", new Table.ColumnGenerator() {
					      @Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					         When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  PremPolicy policy = (PremPolicy) itemId ;
					    	  BeanItemContainer<TmpInsured> insuredList = insuredService.getInsuredList(policy.getPolicyNo());
					    	  TmpInsured insured = new TmpInsured();
					    	  
					    	 for (TmpInsured ins : insuredList.getItemIds()) {
								if(enteredValues.containsKey("healthCardNumber") && StringUtils.contains(ins.getHealthCardNumber(), enteredValues.get("healthCardNumber"))){
									insured = ins;
									break;
								} else if(enteredValues.containsKey("insuredName") && StringUtils.contains(ins.getInsuredName(), enteredValues.get("insuredName"))){
									insured = ins;
									break;
								} 
							}
					    	 
					    	 if(insured.getHealthCardNumber() == null) {
					    		 if(!insuredList.getItemIds().isEmpty()){
					    			 insured = insuredList.getItemIds().get(0);
					    		 }
					    		 
					    	 }
//					        final Label healthCardNumber = new Label(insured.getHealthCardNumber());
					        return insured.getHealthCardNumber();
					      }
					});*/
					
					/*table.addGeneratedColumn("Product Name", new Table.ColumnGenerator() {
					      private Product productByProductCode;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					         When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
							  PremPolicy policy = (PremPolicy) itemId ;
					    	  productByProductCode = masterService.getProductByProductCode(policy.getProductName());
					    	
					        final TextArea productName = new TextArea();
					        productName.setValue(productByProductCode.getValue());
					        productName.setReadOnly(true);
					    	productName.setWidth("-1px");
					    	productName.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
					        return productName;
					      }
					});*/
					
					
				   /* String dynamicName = "Insured Name";
					MastersValue value = (MastersValue) searchOptions.getValue();
					if(!"Policy".equalsIgnoreCase(value.getValue())){
						dynamicName = "Proposer / Insured Name";
					} 
					table.addGeneratedColumn(dynamicName, new Table.ColumnGenerator() {
					      private Product productByProductCode;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					         When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  PremPolicy policy = (PremPolicy) itemId ;
					    	  MastersValue value = (MastersValue) searchOptions.getValue();
					    	    String name = policy.getInsuredName();
								if(!"Policy".equalsIgnoreCase(value.getValue())){
									BeanItemContainer<TmpInsured> insuredList = insuredService.getInsuredList(policy.getPolicyNo());
							    	  TmpInsured insured = new TmpInsured();
							    	  
							    	 for (TmpInsured ins : insuredList.getItemIds()) {
										if(enteredValues.containsKey("insuredName") && StringUtils.contains(ins.getInsuredName(), enteredValues.get("insuredName"))){
											insured = ins;
											break;
										} 
								} 
							    	 
					    	 if(insured.getInsuredName() == null){
					    		 if(!insuredList.getItemIds().isEmpty()){
					    			 insured = insuredList.getItemIds().get(0);
					    		 }
					    	 }
					    	 
							  name = insured.getInsuredName();
							}
					    	  Label nameLabel = new Label(name);
							  return nameLabel;
						}
					});*/
					
					//Object[] columns = new Object[]{"polNo", "Health Card No", dynamicName, "polAddr01","registerdMobileNumber", "Insured Office", "Product Name", "polSumInsured", "polTelNo", "polFmDt", "polToDt"};
					//Object[] columns = new Object[]{"PolicyNo", "HealthCardNo", "InsuredName", "Address","MobileNumber", "Insured Office", "ProductName", "SumInsured", "TelephoneNo", "DateofInception", "PolicyEndDate"};
					Object[] columns = new Object[]{"Action", "View", "policyNo", "healthCardNo", "insuredName", "address","mobileNumber", "Insured Office", "productName", "sumInsured", "telephoneNo", "Date Of Inception", "Date of Expiry"};
					
					
					
					table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
					/*table.setColumnHeader("policyNo", "Policy No");
					table.setColumnHeader("polAddr01", "Address");
					table.setColumnHeader("registerdMobileNumber", "Registered Mobile Number");
					table.setColumnHeader("polSumInsured", "Sum Insured (INR)");
					table.setColumnHeader("polTelNo", "Telephone No");
					table.setColumnHeader("polFmDt", "Date Of Inception");
					table.setColumnHeader("polToDt", "End Date");*/
					table.setColumnHeader("Action", "Action");
					table.setColumnHeader("View", "View");
					table.setColumnHeader("policyNo", "Policy No");
					table.setColumnHeader("healthCardNo", "Health Card No");
					table.setColumnHeader("insuredName", "Insured Name");
					table.setColumnHeader("address", "Address");
					table.setColumnHeader("mobileNumber", "Registered Mobile Number");
					table.setColumnHeader("productName","Product Name");
					table.setColumnHeader("sumInsured", "Sum Insured");
					table.setColumnHeader("telephoneNo", "Telephone No");
//					table.setColumnHeader("dateofInception", "Date Of Inception");
//					table.setColumnHeader("policyEndDate", "Date of expiry");
					table.setColumnHeader("Date Of Inception", "Date Of Inception");
					table.setColumnHeader("Date of Expiry", "Date of Expiry");			
					table.setColumnWidth("address", 180);
					table.setColumnWidth("healthCardNo", 100);
					
					table.removeGeneratedColumn("Date Of Inception");
					table.addGeneratedColumn("Date Of Inception", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;
						@Override
						public Object generateCell(final Table source, final Object itemId, Object columnId) {
							if(!isBancs){
								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
								try {
									Date date = formatter.parse(((PremPolicy)itemId).getDateofInception());
									return new SimpleDateFormat("dd/MM/yyyy").format(date);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							return ((PremPolicy)itemId).getDateofInception();
						}
					});
					table.removeGeneratedColumn("Date of Expiry");
					table.addGeneratedColumn("Date of Expiry", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;
						@Override
						public Object generateCell(final Table source, final Object itemId, Object columnId) {
							if(!isBancs){
								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
								try {
									Date date = formatter.parse(((PremPolicy)itemId).getPolicyEndDate());
									return new SimpleDateFormat("dd/MM/yyyy").format(date);
								} catch (ParseException e) {
	
									e.printStackTrace();
								}
							}
							return ((PremPolicy)itemId).getPolicyEndDate();
						}
					});
					
					table.removeGeneratedColumn("Insured Office");
					table.addGeneratedColumn("Insured Office", new Table.ColumnGenerator() {
					      
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					     //    When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  //TODO : Fix this column
					    	  PremPolicy policy = (PremPolicy) itemId ;
					    	  List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService.getInsuredOfficeNameByDivisionCode(policy.getOfficeCode());
//					    	  Label insuredOfficeName = null; 
					    	  TextField insuredOfficeName = new TextField();
					    	  if(!insuredOfficeNameByDivisionCode.isEmpty()){
//					    		  insuredOfficeName = new Label();
					    		  
					    		  insuredOfficeName.setValue(insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName());
					    		  					    		  
					    	  }
					    	  insuredOfficeName.setReadOnly(true);
				    		  insuredOfficeName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					        return insuredOfficeName;
					      }
					});
					
					table.removeGeneratedColumn("View");
					table.addGeneratedColumn("View", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					       //  When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  HorizontalLayout buttonLayout =new HorizontalLayout();
					 
					    	 final Button viewPolicyConditonsButton = new Button("View Policy Condtions");
						        
						        viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
							    /**
									 * 
									 */
									private static final long serialVersionUID = 1L;

								public void buttonClick(ClickEvent event) {
								    	Item item = source.getItem(itemId);
								    	PremPolicy tmpPolicy = (PremPolicy) itemId;
								    	PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								    	ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
												.get();
							
								    	a_viewProductBenefits.showValues(tmpPolicy.getPolicyNo());
										UI.getCurrent().addWindow(a_viewProductBenefits);
							        } 
							    });
						    	viewPolicyConditonsButton.addStyleName("link");
						    	buttonLayout.addComponent(viewPolicyConditonsButton);
					        return buttonLayout;
					      }
					    });
					
					table.removeGeneratedColumn("Action");
					table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					      //   When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	final Button addIntimationButton = new Button("Register Claim");

					    	addIntimationButton.addClickListener(new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@SuppressWarnings("unused")
								public void buttonClick(ClickEvent event) {
						        	//TmpPolicy tmpPolicy = (TmpPolicy)event.getButton().getData();
//						        	Item item = source.getItem(itemId);
//						        //	CreateIntimationWizard wizard = new CreateIntimationWizard(item);
//						        	Page.getCurrent().setUriFragment("!" + MenuItemBean.NEW_INTIMATION);
//		//						        	 ViewPreviousIntimation viewIntimation = new ViewPreviousIntimation();
//		//							         UI.getCurrent().addWindow(viewIntimation);
						    		String userCode=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						        	PremPolicy tmpPolicy = (PremPolicy) itemId;
						        	if(!isBancs){
						        	PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
						        	/*Below Condition Commented as per Raja Instruction
						        	Map<String, String> opUserValidation = dbcalculationService.getOPUserValidation(userCode,policyDetails.getOfficeCode());
						        	String status = opUserValidation.get("status");*/
						        	
						        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
										try {
											Date StartDate = new Date();
											Date endDate = new Date();
											StartDate = formatter.parse(policyDetails.getPolicyStartDate());
											endDate = formatter.parse(policyDetails.getPolicyEndDate());
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										/*Below Condition Commented as per Raja Instruction
										if(status !=null && status.equalsIgnoreCase("Y")){*/
											if(policyDetails.getPolicyStatus() != null && SHAConstants.NEW_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus()) 
													|| SHAConstants.ENDORESED_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus())) {
												LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
									    		Product product = masterService.getProductByProductCode(policyDetails.getProductCode());
									    		OrganaizationUnit organaizationUnit = policyService.getOrgUnitName(policyDetails.getOfficeCode());
									    		policyValues.put("Policy Number", policyDetails.getPolicyNo());
									    	//	policyValues.put("Product Type", policyDetails.getProductType().toString());
									    		policyValues.put("Insured Name", "");
									    		policyValues.put("Product Name", product.getValue());
									    		policyValues.put("PIO Name", organaizationUnit.getOrganizationUnitName());
									    		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
									    		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
									    		policyValues.put(BPMClientContext.USERID, userName);
									    		policyValues.put(BPMClientContext.PASSWORD, passWord);
									    		if(product != null && product.getOutPatientFlag() != null && product.getOutPatientFlag().equalsIgnoreCase("Y")){
									    			fireViewEvent(MenuPresenter.SHOW_OP_REGISTER_CLAIM, policyDetails, policyValues,false);
									    		} else {
									    			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
													buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
													GalaxyAlertBox.createErrorBox("This policy does not have an OP benefit", buttonsNamewithType);
									    		}
											}else {
												showErrorPopup("This Policy is Invalid.");	
											}
						        	} else {
						        		PremPolicyDetails policyDetails = new PremPolicyDetails();
						        		policyDetails.setPolicyNo(tmpPolicy.getPolicyNo());
						        		Policy policyDtls = policyService.getPolicyByPolicyNubember(tmpPolicy.getPolicyNo());
						        		Boolean isPulled = true;
						        		if(policyDtls == null){
						        			isPulled = BancsSevice.getInstance().isPolicydetailspulled(tmpPolicy.getPolicyNo());
						        			if(isPulled){
						        				Policy policy = policyService.getPolicyByPolicyNubember(tmpPolicy.getPolicyNo());
						        				if(policy != null && policy.getProduct() != null
						        						&& policy.getProduct().getCode() != null){
						        					Product product = masterService.getProductByProductCode(policy.getProduct().getCode());
						        					if(product != null && product.getOutPatientFlag() != null && product.getOutPatientFlag().equalsIgnoreCase("Y")){
						        						LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
														fireViewEvent(MenuPresenter.SHOW_OP_REGISTER_CLAIM, policyDetails, policyValues, true);
						        					} else {
						        						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
														buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
														GalaxyAlertBox.createErrorBox("This policy does not have an OP benefit", buttonsNamewithType);
						        					}
						        				}
									        	
								        	} else {
								        		showErrorPopup("Unable to register claim. Please contact IMS Team");
								        	}
						        		} else {
						        			Product product = masterService.getProductByProductCode(policyDtls.getProduct().getCode());
				        					if(product != null && product.getOutPatientFlag() != null && product.getOutPatientFlag().equalsIgnoreCase("Y")){
							        			LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
												fireViewEvent(MenuPresenter.SHOW_OP_REGISTER_CLAIM, policyDetails, policyValues, true);
				        					} else {
				        						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
												buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
												GalaxyAlertBox.createErrorBox("This policy does not have an OP benefit", buttonsNamewithType);
				        					}
						        		}
						        		
						        	}
										/*Below Condition Commented as per Raja Instruction
										}else if(status !=null && status.equalsIgnoreCase("N")) {
											showErrorPopup(opUserValidation.get("remarks"));
										}*/
						        	} 
						    });
					    	addIntimationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					    	addIntimationButton.addStyleName("linkButton");
					        return addIntimationButton;
					      }
					    });
					
					table.setVisibleColumns(columns);
				}else {
					return null;
				}
		} catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		if(table.getItemIds().size() > 10){
			table.setPageLength(10);
		} else {
			table.setPageLength(table.getItemIds().size());
		}
		
		return table;
	
	}
	
	
	
	private PagedTable retrieveOPIntimationFromDB(){
		PagedTable table = new PagedTable();
		policyDetailsTable = new PagedTable("Intimation Table");
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setSizeFull();

		try {
			if(validateFields()) {
				BeanItemContainer<OPSearchIntimationDTO> policyContainer =  policyService.getOPIntimationData(enteredValues);
				/*if(searchOptions.getValue() != null || searchOptions.getValue() == "") {
					SelectValue masterRecord = (SelectValue) searchOptions.getValue();	
					String selectedValue = masterRecord.getValue();

					if("Policy".equalsIgnoreCase(selectedValue)) {

					} else if("Proposer/Insured".equalsIgnoreCase(selectedValue)) {
						if((enteredValues.containsKey("polAssrName") || enteredValues.containsKey("insuredDateOfBirth"))|| (enteredValues.containsKey("registeredMobileNumber") )){
							String productType=null;
							String mobile=null;
							if(enteredValues.containsKey("productType"))
							{
								productType=enteredValues.get("productType");
							}

							if(enteredValues.containsKey("registeredMobileNumber"))
							{
								mobile=enteredValues.get("registeredMobileNumber");
							}

							Date value = proposerDOB.getValue();
						} 
						else if(enteredValues.containsKey("insuredName") && enteredValues.containsKey("insuredDateOfBirth")) {
							Date value = proposerDOB.getValue();
						}
						else {
							//return null;
						}
					}
				}*/
				
				if(policyContainer.getItemIds().isEmpty()){
					return table;
				}
				table.setContainerDataSource(policyContainer);
				
				Object[] columns = new Object[]{"Status", "View Document", "View Trails", "opIntimationNo","opClaimNo", "policyNo", "healthCardNo", "productName", "insuredPatientName", "claimType", "opHealthCheckupDate", "opVisitReason"};

				table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
				table.setColumnHeader("S.No", "S.No");
				table.setColumnHeader("Status", "Status");
				table.setColumnHeader("View Document", "View Document");
				table.setColumnHeader("View Trails", "View Trails");
				table.setColumnHeader("opIntimationNo", "Intimation No");
				table.setColumnHeader("opClaimNo", "Claim No");
				table.setColumnHeader("policyNo", "Policy No");
				table.setColumnHeader("healthCardNo", "Health Card No");
				table.setColumnHeader("productName","Product Name");
				table.setColumnHeader("insuredPatientName", "Insured Patient Name");
				table.setColumnHeader("claimType", "Claim Type");
				table.setColumnHeader("opHealthCheckupDate", "OP Date");
				table.setColumnHeader("opVisitReason", "Reason for OP");

				table.removeGeneratedColumn("View Trails");
				table.addGeneratedColumn("View Trails", new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source, final Object itemId, Object columnId) {
						/*	//  When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
						HorizontalLayout buttonLayout =new HorizontalLayout();

						final Button viewPolicyConditonsButton = new Button("View Claim Trails");

						viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								Item item = source.getItem(itemId);
								PremPolicy tmpPolicy = (PremPolicy) itemId;
								PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
										.get();

								a_viewProductBenefits.showValues(policyDetails);
								UI.getCurrent().addWindow(a_viewProductBenefits);
							} 
						});
						viewPolicyConditonsButton.addStyleName("link");
						buttonLayout.addComponent(viewPolicyConditonsButton);
						return buttonLayout;*/

						final Button viewIntimationDetailsButton = new Button("View Trails");
						viewIntimationDetailsButton.setData(itemId);
//						final Long intimationKey = ((OPSearchIntimationDTO) itemId).getIntimation().getKey();

						viewIntimationDetailsButton	.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = -1191683984431501220L;

							public void buttonClick(ClickEvent event) {
								Long intimationKey = ((OPSearchIntimationDTO) event.getButton().getData()).getIntimation().getKey();
								OPIntimation intimation = intimationService.getOPIntimationByKey(intimationKey);
								getViewClaimHistory(intimation.getIntimationId());
							}
						});
						viewIntimationDetailsButton.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});

				table.removeGeneratedColumn("View Document");
				table.addGeneratedColumn("View Document", new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source, final Object itemId, Object columnId) {
						/*//  When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
						HorizontalLayout buttonLayout =new HorizontalLayout();

						final Button viewPolicyConditonsButton = new Button("View Document Details");

						viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								Item item = source.getItem(itemId);
								PremPolicy tmpPolicy = (PremPolicy) itemId;
								PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
										.get();

								a_viewProductBenefits.showValues(policyDetails);
								UI.getCurrent().addWindow(a_viewProductBenefits);
							} 
						});
						viewPolicyConditonsButton.addStyleName("link");
						buttonLayout.addComponent(viewPolicyConditonsButton);
						return buttonLayout;*/
						
						final Button viewDocumentDetailsButton = new Button("View Document Details");
						viewDocumentDetailsButton.setData(itemId);
						final Long intimationKey = ((OPSearchIntimationDTO) itemId).getIntimation().getKey();

						viewDocumentDetailsButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = -5582931723068435620L;
									public void buttonClick(ClickEvent event) {
										OPIntimation intimation = intimationService.getOPIntimationByKey(intimationKey);
										viewUploadedDocumentDetails(intimation.getIntimationId());
									}
								});
						viewDocumentDetailsButton.addStyleName(BaseTheme.BUTTON_LINK);
						return viewDocumentDetailsButton;
					}
				});

				table.removeGeneratedColumn("Status");
				table.addGeneratedColumn("Status", new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source, final Object itemId, Object columnId) {/*
						final Button addIntimationButton = new Button("View Claim Status");

						addIntimationButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@SuppressWarnings("unused")
							public void buttonClick(ClickEvent event) {
								PremPolicy tmpPolicy = (PremPolicy) itemId;
								PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());

								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
								try {
									Date StartDate = new Date();
									Date endDate = new Date();
									StartDate = formatter.parse(policyDetails.getPolicyStartDate());
									endDate = formatter.parse(policyDetails.getPolicyEndDate());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(policyDetails.getPolicyStatus() != null && SHAConstants.NEW_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus()) || SHAConstants.ENDORESED_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus())) {
									LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
									Product product = masterService.getProductByProductCode(policyDetails.getProductCode());
									OrganaizationUnit organaizationUnit = policyService.getOrgUnitName(policyDetails.getOfficeCode());
									policyValues.put("Policy Number", policyDetails.getPolicyNo());
									policyValues.put("Insured Name", "");
									policyValues.put("Product Name", product.getValue());
									policyValues.put("PIO Name", organaizationUnit.getOrganizationUnitName());
									String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
									String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
									policyValues.put(BPMClientContext.USERID, userName);
									policyValues.put(BPMClientContext.PASSWORD, passWord);
									fireViewEvent(MenuPresenter.SHOW_OP_REGISTER_CLAIM, policyDetails, policyValues);
								} else {
									showErrorPopup("This Policy is Invalid.");	
								}
							} 
						});
						addIntimationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						addIntimationButton.addStyleName("linkButton");
						return addIntimationButton;
					*/
						
						final Button viewStatusButton = new Button();
						viewStatusButton.setData(itemId);
						viewStatusButton.setCaption("View Claim Status");
						

//						Claim a_claim = claimService.getClaimforIntimation(intimationKey);

						/*if (a_claim != null
								&& a_claim.getClaimId() != null)

						{
							viewStatusButton
									.setCaption("View Claim Status");
						} else {
							viewStatusButton
									.setCaption("View Intimation Details");

						}*/
						viewStatusButton.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 5230497581671397176L;
							public void buttonClick(ClickEvent event) {

								//								NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;

								Long intimationKey = ((OPSearchIntimationDTO) itemId).getIntimation().getKey();
								OPIntimation a_intimation = intimationService.getOPIntimationByKey(intimationKey);

								OPClaim a_claim = claimService.getOPClaimforIntimation(intimationKey);

								if (a_claim != null) {
									viewDetails.viewOPClaimStatusUpdated(a_intimation.getIntimationId());
								}
								/*else if (a_claim == null) {
									viewDetails.getViewIntimation(a_intimation.getIntimationId());
								}*/
							}
						});
						viewStatusButton.addStyleName(BaseTheme.BUTTON_LINK);
						return viewStatusButton;
					}
				});

				table.setVisibleColumns(columns);
			}else {
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		if(table.getItemIds().size() > 10){
			table.setPageLength(10);
		} else {
			table.setPageLength(table.getItemIds().size());
		}

		return table;

	}
	
	public void getViewClaimHistory(String intimationNo) {

		if(intimationNo != null){
			OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
			Boolean result = true;		
			if (intimation != null) {
				result = viewClaimHistoryRequest.showOPCashlessAndReimbursementHistory(intimation);
				if(result){
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setContent(viewClaimHistoryRequest);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						private static final long serialVersionUID = 1L;		
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				}else{
					showErrorPopup("History is not available");
				}
			}

		}else{
			showErrorPopup("History is not available");
		}
	}
	
	
	public void showTable(){

		// Remove the table from layout if it exists already.
		removeTableFromLayout();
		PagedTable policyTable = retrieveCorrespondingValues();
		if (!policyTable.getItemIds().isEmpty()) {
			PagedTable retrieveCorrespondingValuesFromDB = policyTable;

			/*HorizontalLayout createControls = retrieveCorrespondingValuesFromDB.createControls();

			for (Component component : createControls) {
				component.setVisible(true);
				break;
			}*/
			retrieveCorrespondingValuesFromDB.setWidth(wholeVerticalLayout.getWidth(), Unit.PERCENTAGE);
			retrieveCorrespondingValuesFromDB.setPageLength(retrieveCorrespondingValuesFromDB.getItemIds().size() > 10 ? 10 :  retrieveCorrespondingValuesFromDB.getItemIds().size() );

			VerticalLayout tablevertical = new VerticalLayout();
			Panel tablepanel = new Panel(retrieveCorrespondingValuesFromDB);
//			tablevertical.addComponent(createControls);
			tablepanel.setWidth("100%");
			tablepanel.setHeight("250px");
			tablevertical.addComponent(tablepanel);
			wholeVerticalLayout.addComponent(tablevertical);
			
			//Alert Message if op SI limit is 0
			//getDataForOPAlert();- commented as mentioned by Raja for getting covercode in claim based on policy
			
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Record found for entered search criteria.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create Intimation Home");
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
					fireViewEvent(MenuItemBean.REGISTER_CLAIM, null);

				}
			});
		}
	}
	
	//OP Alert Data
		public void getDataForOPAlert(){
			
			System.out.println("CALling getDataForOPAlert .....................");
			
			BeanItemContainer<OPSearchIntimationDTO> policyContainer =  policyService.getOPIntimationData(enteredValues);
			if(!policyContainer.getItemIds().isEmpty()){
				
				OPSearchIntimationDTO opsDTO =  policyContainer.getItemIds().get(0);
				OPClaim opClaim = opsDTO.getClaim();
				
				Long claimKey =  opClaim.getKey();
				Long insuredKey = opClaim.getIntimation().getInsured().getKey();
				Long ClaimType= opClaim.getClaimType().getKey();
				String ClaimCoversec=opClaim.getOpcoverSection() != null ? opClaim.getOpcoverSection() : "0";
				
				System.out.println(String.format(" claimKey [%s]", claimKey));
				System.out.println(String.format("OP insuredKey [%s]", insuredKey));
				System.out.println(String.format("OP ClaimType [%s]", ClaimType));
				System.out.println(String.format("OP ClaimCoversec [%s]", ClaimCoversec));
				
				Map<String, Integer> claimAmt = dbcalculationService.getOPAvailableAmount(insuredKey,claimKey, ClaimType,ClaimCoversec);
				Integer opAvailableAmount = 0;
				
				
				if(claimAmt != null && !claimAmt.isEmpty()){
					opAvailableAmount = claimAmt.get(SHAConstants.CURRENT_BALANCE_SI);
					
					if(opAvailableAmount == 0){
						
						final MessageBox showInfoMessageBox = showInfoMessageBox("Available OP SI Limit : "+opAvailableAmount );
						Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
						
						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								showInfoMessageBox.close();
							}
						});
					}

				}
			}
			
		}
		
		

	public MessageBox showInfoMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
	  }
	
	@SuppressWarnings("static-access")
	private void showErrorPopup(String errorMessage){
		Label label = new Label(errorMessage, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public void showLayoutBasedOnSelection(ValueChangeEvent valueChangeEvent){

		if(valueChangeEvent != null && valueChangeEvent.getProperty() != null && valueChangeEvent.getProperty().getValue() != null) {
			//			MastersValue masterRecord = (String) valueChangeEvent.getProperty().getValue();	
			SelectValue selectedValue = (SelectValue) valueChangeEvent.getProperty().getValue();
			//removeTableFromLayout();
			if(StringUtils.equalsIgnoreCase(selectedValue.getValue(), "Create Intimation")) {
				HorizontalLayout tempLayout = buildCreateIntimationLayout();
				mainVerticalLayout.removeAllComponents();
				mainVerticalLayout.addComponent(buildTypeLayout());
				mainVerticalLayout.addComponent(tempLayout);
				mainVerticalLayout.addComponent(buildButtonLayout());
			} else if(StringUtils.equalsIgnoreCase(selectedValue.getValue(), "Search Intimation")) {
				HorizontalLayout tempLayout = buildSearchIntimationLayout();
				mainVerticalLayout.removeAllComponents();
				mainVerticalLayout.addComponent(buildTypeLayout());
				mainVerticalLayout.addComponent(tempLayout);
				mainVerticalLayout.addComponent(buildButtonLayout());
			} 
		} else {
			mainVerticalLayout.removeAllComponents();
			mainVerticalLayout.addComponent(buildTypeLayout());
//			mainVerticalLayout.addComponent(buildButtonLayout());
		}

	}
	
	
	
	public void refresh(){
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
	
/*	private boolean isDateOfIntimationWithPolicyRange(Date policyFrmDate , Date policyToDate)
	{
		Date intimationCreationDate = new Date();
		if((policyFrmDate.equals(intimationCreationDate) || policyFrmDate.before(intimationCreationDate)) 
			&& (policyToDate.equals(intimationCreationDate) || policyToDate.after(intimationCreationDate)))
		{
			return true;
		}	
		else
		{
			return false;
		}
	}*/
	
	@SuppressWarnings("deprecation")
	public BeanItemContainer<SelectValue> getOPType() {
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		Map<String, String> tempVal = new  HashMap<>();
		
		tempVal.put("Create Intimation", "Create Intimation");
		tempVal.put("Search Intimation", "Search Intimation");

		for (Map.Entry<String, String> entry : tempVal.entrySet()) {
			SelectValue selected = new SelectValue();
			selected.setValue(entry.getValue());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
		return container;
	}
	
	public void viewUploadedDocumentDetails(String intimationNo) {		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}
	
}
