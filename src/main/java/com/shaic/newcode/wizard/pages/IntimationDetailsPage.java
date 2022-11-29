package com.shaic.newcode.wizard.pages;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.GTokenField;
import com.shaic.arch.GTokenFieldListener;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.create.SearchGMCInsuredUI;
import com.shaic.claim.intimation.create.SearchHospitalContactNoUI;
import com.shaic.claim.intimation.create.SearchInsuredUI;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalDTO;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalUI;
import com.shaic.claim.selecthospital.ui.HospitalMapper;
import com.shaic.claim.selecthospital.ui.SearchIntimationHospitalUI;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCoverDetails;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.domain.preauth.PremiaPreviousClaim;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
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

@ViewScoped
//@UIScoped
@CDIView(value = MenuItemBean.NEW_INTIMATION)
public class IntimationDetailsPage extends ViewComponent implements
		Serializable, GTokenFieldListener,View {
	private static final long serialVersionUID = 1L;

	@Inject
	private NewIntimationDto bean;

	public static final String INSURED_SELECTED = "intimation_details_page_insured_selected";
	
	private static final String stateCaption="State";
	private static final String cityCaption="City";
	private static final String hospitalNameCaption="Hospital Name";
	
	private Insured insuredGMC;
	
	private Policy policy;

	private ComboBox cmbModeOfIntimation;

	private ComboBox cmbIntimatedBy;

	private ComboBox cmbInsuredPatient;

	private TextField callerContactNumberTxt;

	//private TextField callerLandlineNumberTxt;

	private TextField attenderContactNumberTxt;

	//private TextField attenderLandlineNumberTxt;

	private TextField txtHospitalEmailId;

	private TextField intimatorNameTxt;

	private GTokenField txtReasonForAdmission;

	private Button insuredSearchBtn;
	
	private Button gmcInsuredSearchBtn;
	
	private Button hospitalPhnSearchBtn;

	private Button updateHospitalBtn;

	private Button searchHospitalBtn;

	private HorizontalLayout hospitalSearchHLayout;

	private HorizontalLayout hospitalUpdateHLayout;

	private ComboBox claimTypecmb;

	private List<Component> mandatoryFields = new ArrayList<Component>();

	private GridLayout firstColumnGridLayout;

	private CheckBox chkPatientNotCovered;

	private ComboBox cmbHospitalType;

	private TextField networkHospitalTypeTxt;

	// private AutocompleteField<Locality> cmbArea;

	private State selectedState;

	private CityTownVillage selectedCity;
	
	// private Locality selectedArea;

	private HospitalDto selectedHospital;

	/*private AutocompleteField<State> cmbState;*/
	private AutocompleteTextField cmbStates ;

	/*private AutocompleteField<CityTownVillage> cmbCity;*/
	private AutocompleteTextField cmbCitys ;

	/*private AutocompleteField<HospitalDto> cmbHospital;*/
	private AutocompleteTextField cmbHospitals ;

	private TextArea txtHospitalAddress;

	private TextField txtHospitalPinCode;
	
	private TextField sumOfInsured;

	private TextField txtHospitalContactNumber;

	private TextField txtHospitalFaxNumber;

	private TextField txtHospitalMobileNumber;

	private FormLayout patientNotCoveredLayout;

	private PopupDateField admissionDateField;

	private ComboBox cmbManagementType;

	private TextField doctorNameTxt;

	private ComboBox cmbRoomCategory;

	private TextArea commentstextArea;	

	private ComboBox cmbAdmissionType;

	private ComboBox cmbRelapseofIllness;

	private TextField inpatientNumbertextField;

	private TextField hospitalCodeTxt;

	private TextField hospitalCodeIrdaTxt;

	private TextArea reasonforlateintimationTxta;

	private FormLayout dynamicFieldsLayout;

	private HorizontalLayout formsHorizontalLayout;

	private VerticalLayout wholeLayout;

	private HorizontalLayout buttonHorizontalLayout;

	private FormLayout firstColumnTopFrmLayout;

	private FormLayout firstColumnbottomFrmLayout;

	private VerticalLayout newBabyTableLayout;

	private FormLayout secondColumnFrmLayout;

	private Button saveBtn;

	private Button submitBtn;

	private Button resetBtn;
	
	private Button cancelBtn;

	private Integer anhHospitalCount;

	private Map<String, Object> referenceData = new HashMap<String, Object>();

	private IntimationDetailsPage instance;
	
	@EJB
	private DBCalculationService dbCalculationService;

	@Inject
	private Instance<SearchInsuredUI> searchInsuredUI;
	
	@Inject
	private Instance<SearchGMCInsuredUI> searchGmcInsuredUI;

	@Inject
	private Instance<NewBornBabyTable> newBornBabyTableInstance;

	private NewBornBabyTable newBornBabyTable;

	private BeanItemContainer<NewIntimationDto> intimationContainer;

	private BeanFieldGroup<NewIntimationDto> binder;

	private BeanItemContainer<SelectValue> reasonForAdmissionContainer;

	@Inject
	private Instance<UpdateHospitalUI> hospitalUI;

	@Inject
	private Instance<SearchIntimationHospitalUI> searchIntimationHospitalUI;
	
    @Inject
	private SearchHospitalContactNoUI searchHospitalUI;


	@EJB
	private HospitalService hospitalService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPage;
	
	private com.vaadin.ui.Window popupWindow;
	
	
	private Panel intimationPanel;
	private Panel hospitalPanel;
	private Panel commentsPanel;
	private Panel buttonsPanel;
	private Panel riskInfoPanel;
	private Panel coverInfoPanel;
	private HorizontalLayout intimationHLayout;
	private FormLayout intimationLeftFLayout;
	private FormLayout intimationRightFLayout;
	private HorizontalLayout hospitalHLayout;
	private FormLayout hospitalLeftFLayout;
	private FormLayout hospitalRightFLayout;
	private HorizontalLayout commentsHLayout;
	private HorizontalLayout buttonsHLayout;
	
	private TextArea callerAddressTA;
	private TextField callerEmailTF;
	private CheckBox hospitalRequiredYNChkBox;
	
	private TextField tmpMainMemberNameCB;
	private ComboBox tmpMainMemberNameCB2;
	private OptionGroup accidentDeathOG; 
	private ComboBox insuredTypeCB;
//	private CheckBox patientNotCoveredChkBox;
	private Button viewCardBtn;
	
	private TextField patientNameTF;
	//private CheckBox parentInsuredChkBox;
	private TextField parentPatientNameTF;
	private PopupDateField dateOfBirthDF;
	private TextField ageTF;
	
	private PopupDateField dateOfDischargeDF;
	private CheckBox dummyChkBox;
	private TextArea suspiciousHospCommentsTA;
	private OptionGroup studentOrParent;
	private TextField txtStudentPatientName;
	private PopupDateField studentDOB;
	private TextField txtStudentAge;
	
	private GridLayout studentAgeGrid;
	private GridLayout parentAgeGrid;
	private GridLayout mainMemberGrid;
	
	
	String sumInsured = null;
	
	private Table insuredTable;
	
	private Table coverDetailsTable;
	
	/**
	 * Intimation Type
		  	HEALTH	
			GMC	
			GPA	
			PA	
			PACKAGE	HEALTH/PA
	 */
	
	private Boolean isHealthAndPA = false;
	
	@PostConstruct
	public void init() {
		this.instance = this;
	}

	public void initBinder() {
		intimationContainer = new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class);
		intimationContainer.addBean(bean);

		this.binder = new BeanFieldGroup<NewIntimationDto>(NewIntimationDto.class);
		this.binder.setItemDataSource(bean);
	}

	public Component getContent() {
		initBinder();
		
		wholeLayout = new VerticalLayout();
		wholeLayout.setHeight("100%");
		wholeLayout.setWidth("100%");
		wholeLayout.setMargin(false);
		wholeLayout.setSpacing(false);

		formsHorizontalLayout = new HorizontalLayout();
		formsHorizontalLayout.setSizeFull();
		formsHorizontalLayout.setSpacing(true);

		firstColumnGridLayout = new GridLayout(2, 5);
		firstColumnGridLayout.setSpacing(true);

		firstColumnTopFrmLayout = new FormLayout();
		firstColumnTopFrmLayout.setHeight("100%");

		firstColumnbottomFrmLayout = new FormLayout();
		firstColumnbottomFrmLayout.setHeight("100%");

		newBabyTableLayout = new VerticalLayout();
		newBabyTableLayout.setHeight("100%");

		secondColumnFrmLayout = new FormLayout();
		secondColumnFrmLayout.setHeight("100%");

		patientNotCoveredLayout = new FormLayout();

		initComponents();

//		firstColumnTopFrmLayout.addComponent(cmbModeOfIntimation);
//		firstColumnTopFrmLayout.addComponent(cmbIntimatedBy);

//		firstColumnTopFrmLayout.addComponent(intimatorNameTxt);
//		firstColumnTopFrmLayout.addComponent(callerContactNumberTxt);
//		firstColumnTopFrmLayout.addComponent(callerLandlineNumberTxt);
//		firstColumnTopFrmLayout.addComponent(attenderContactNumberTxt);
//		firstColumnTopFrmLayout.addComponent(attenderLandlineNumberTxt);
		firstColumnGridLayout.addComponent(firstColumnTopFrmLayout, 0, 0);
//		firstColumnGridLayout.addComponent(new FormLayout(cmbInsuredPatient), 0, 1);
		firstColumnGridLayout.addComponent(insuredSearchBtn, 1, 1);

		FormLayout chekpatientNotCoveredFrmLayout = new FormLayout(chkPatientNotCovered);

		chekpatientNotCoveredFrmLayout.setCaption("Patient Not Covered");

		patientNotCoveredLayout.addComponent(chekpatientNotCoveredFrmLayout);

		firstColumnGridLayout.addComponent(patientNotCoveredLayout, 0, 2);

		firstColumnGridLayout.addComponent(newBabyTableLayout, 0, 3, 1, 3);
		firstColumnGridLayout.setComponentAlignment(newBabyTableLayout, Alignment.TOP_CENTER);
		
//		firstColumnbottomFrmLayout.addComponent(admissionDateField);
		firstColumnbottomFrmLayout.addComponent(cmbRelapseofIllness);
//		firstColumnbottomFrmLayout.addComponent(cmbAdmissionType);
		//firstColumnbottomFrmLayout.addComponent(inpatientNumbertextField);
		firstColumnbottomFrmLayout.addComponent(reasonforlateintimationTxta);
		// TODO Token Field
		Panel panel = new Panel();
		panel.setWidth("180px");
		panel.setHeight("80px");
		panel.setStyleName("gray");
		panel.setContent(txtReasonForAdmission);
		// Label mandatory = new Label("*", ContentMode.HTML);
		// mandatory.setStyleName("errMessage");
		HorizontalLayout reasonlayout = new HorizontalLayout(panel);
		reasonlayout.setCaption("Reason for Admission *");
//		firstColumnbottomFrmLayout.addComponent(reasonlayout);
		// firstColumnbottomFrmLayout.addComponent(txtReasonForAdmission);
//		firstColumnGridLayout.addComponent(firstColumnbottomFrmLayout, 0, 4);

//		secondColumnFrmLayout.addComponent(cmbState);
//		secondColumnFrmLayout.addComponent(cmbCity);
		// secondColumnFrmLayout.addComponent(cmbArea);
//		secondColumnFrmLayout.addComponent(cmbHospital);
		hospitalSearchHLayout = new HorizontalLayout(new Label(
				"<b style = 'color:red'> Please prefer ANH Hospital </b>",
				Label.CONTENT_XHTML), searchHospitalBtn);
		hospitalSearchHLayout.setSpacing(true);
		hospitalSearchHLayout.setComponentAlignment(searchHospitalBtn,
				Alignment.MIDDLE_RIGHT);
		hospitalSearchHLayout.setVisible(false);
		//secondColumnFrmLayout.addComponent(hospitalSearchHLayout);
//		hospitalUpdateHLayout = new HorizontalLayout(txtHospitalAddress, updateHospitalBtn);
		hospitalUpdateHLayout = new HorizontalLayout(updateHospitalBtn);
		hospitalUpdateHLayout.setComponentAlignment(updateHospitalBtn,
				Alignment.MIDDLE_RIGHT);
		hospitalUpdateHLayout.setCaption("Hospital Address");
		secondColumnFrmLayout.addComponent(hospitalUpdateHLayout);

		secondColumnFrmLayout.addComponent(txtHospitalPinCode);
//		secondColumnFrmLayout.addComponent(txtHospitalContactNumber);
//		secondColumnFrmLayout.addComponent(txtHospitalFaxNumber);
		secondColumnFrmLayout.addComponent(txtHospitalMobileNumber);
		secondColumnFrmLayout.addComponent(txtHospitalEmailId);
//		secondColumnFrmLayout.addComponent(cmbHospitalType);
		secondColumnFrmLayout.addComponent(networkHospitalTypeTxt);
		secondColumnFrmLayout.addComponent(hospitalCodeTxt);
		secondColumnFrmLayout.addComponent(hospitalCodeIrdaTxt);
		secondColumnFrmLayout.addComponent(claimTypecmb);
//		secondColumnFrmLayout.addComponent(cmbManagementType);
//		secondColumnFrmLayout.addComponent(cmbRoomCategory);
//		secondColumnFrmLayout.addComponent(doctorNameTxt);
		secondColumnFrmLayout.addComponent(commentstextArea);

		formsHorizontalLayout.setMargin(true);
		formsHorizontalLayout.addComponents(firstColumnGridLayout, secondColumnFrmLayout);

		buttonHorizontalLayout.addComponents(saveBtn, submitBtn, resetBtn, cancelBtn);
		buttonHorizontalLayout.setSpacing(true);

		wholeLayout.addComponents(formsHorizontalLayout);
//		wholeLayout.setComponentAlignment(buttonHorizontalLayout, Alignment.MIDDLE_CENTER);

		mandatoryFields.add(cmbModeOfIntimation);
		mandatoryFields.add(cmbIntimatedBy);
//		mandatoryFields.add(cmbInsuredPatient);
		mandatoryFields.add(intimatorNameTxt);
		mandatoryFields.add(callerContactNumberTxt);
		mandatoryFields.add(attenderContactNumberTxt);
		mandatoryFields.add(admissionDateField);
		/*mandatoryFields.add(inpatientNumbertextField);
		mandatoryFields.add(reasonforlateintimationTxta);*/
		mandatoryFields.add(cmbRelapseofIllness);
		mandatoryFields.add(cmbAdmissionType);
		/*mandatoryFields.add(cmbState);*/
		/*mandatoryFields.add(cmbCity);*/
		// mandatoryFields.add(cmbArea);
		/*mandatoryFields.add(cmbHospital);*/
//		mandatoryFields.add(cmbManagementType);
		//mandatoryFields.add(callerAddressTA);
		mandatoryFields.add(txtReasonForAdmission);
		//mandatoryFields.add(cmbRoomCategory);

		showOrHideValidation(false);

		/*setUpAutoState(cmbState);*/
		/*setUpAutoCity(cmbCity);*/
		// setUpAutoArea(cmbArea);
		/*setUpAutoHospital(cmbHospital);*/

		bindBeanToUI();
		

		viewCardBtn = new Button("View Card");
		viewCardBtn.setStyleName(ValoTheme.BUTTON_LINK);
		
		viewCardBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				//pdfPage.ViewHealthCard(bean.getHealthCardNo());
				if(bean.getHealthCardNo() != null){
					Insured insured = (Insured) cmbInsuredPatient.getValue();
					getViewDocumentByPolicyNo(bean.getHealthCardNo(),bean.getPolicy().getPolicyNumber(),insured.getSourceRiskId());
				}else{
					getErrorMessage("View Care Details is not available");
				}
				
			}
		});
		
//		patientNotCoveredChkBox = new CheckBox("Patient not covered");
//		patientNameTF = new TextField("Patient Name");
//		patientNameTF.setNullRepresentation("");
//		patientNameTF.setVisible(patientNotCoveredChkBox.getValue());
//				
//		patientNotCoveredChkBox.addValueChangeListener(new Property.ValueChangeListener() {
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				if(!patientNotCoveredChkBox.getValue()) {
//					patientNameTF.setValue(null);
//				}
//				patientNameTF.setVisible(patientNotCoveredChkBox.getValue());
//				return;
//			}
//		});
		
		HorizontalLayout viewCardHLayout = new HorizontalLayout(chkPatientNotCovered, viewCardBtn);
		viewCardHLayout.setMargin(false);
		viewCardHLayout.setSpacing(true);
		viewCardHLayout.setCaption("");
		viewCardHLayout.setComponentAlignment(chkPatientNotCovered, Alignment.MIDDLE_LEFT);
		
		// Intimation Left Form Layout
		intimationLeftFLayout = new FormLayout();
		intimationLeftFLayout.setWidth("100%");
		intimationLeftFLayout.setMargin(false);
		intimationLeftFLayout.addComponent(cmbModeOfIntimation);
		intimationLeftFLayout.addComponent(cmbIntimatedBy);
		intimationLeftFLayout.addComponent(intimatorNameTxt);
		intimationLeftFLayout.addComponent(callerContactNumberTxt);
		//intimationLeftFLayout.addComponent(callerLandlineNumberTxt);
		intimationLeftFLayout.addComponent(attenderContactNumberTxt);
		//intimationLeftFLayout.addComponent(attenderLandlineNumberTxt);
		intimationLeftFLayout.addComponent(callerAddressTA);
		intimationLeftFLayout.addComponent(callerEmailTF);
		intimationLeftFLayout.addComponent(hospitalRequiredYNChkBox);
		
	   
		
		// Intimation Right Form Layout
		intimationRightFLayout = new FormLayout();
		intimationRightFLayout.setWidth("100%");
		intimationRightFLayout.setMargin(false);
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			 mainMemberGrid = new GridLayout(2,1);
			 mainMemberGrid.addComponent(tmpMainMemberNameCB, 0, 0);
			 tmpMainMemberNameCB.setStyleName("gridAlignment");
			 tmpMainMemberNameCB.addStyleName("gridAlignment");
			 mainMemberGrid.addComponent(gmcInsuredSearchBtn, 1, 0);
			 mainMemberGrid.setSpacing(true);
			 mainMemberGrid.setCaption("TMP Main Member Name");
			 mainMemberGrid.setComponentAlignment(tmpMainMemberNameCB, Alignment.TOP_CENTER);
			
			intimationRightFLayout.addComponent(mainMemberGrid);
			intimationRightFLayout.addComponent(tmpMainMemberNameCB2);
			mainMemberGrid.setComponentAlignment(tmpMainMemberNameCB, Alignment.TOP_CENTER);
		}
		
		intimationRightFLayout.addComponent(accidentDeathOG);
		intimationRightFLayout.addComponent(insuredTypeCB);
		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			intimationRightFLayout.addComponent(cmbInsuredPatient);
		}
		
		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeList().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			intimationLeftFLayout.addComponentAsFirst(sumOfInsured);
		}
		
		intimationRightFLayout.addComponent(viewCardHLayout);
		intimationRightFLayout.addComponent(patientNameTF);
		if((this.bean.getPolicySummary().getProductCode().equals(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE))
				&& ReferenceTable.getGpaUnnamedSection().containsKey(bean.getPolicySummary().getSectionCode())){
			intimationRightFLayout.addComponent(studentOrParent);
			//studentOrParent.setValue(true);
			//intimationRightFLayout.addComponent(parentInsuredChkBox);
		}
		
//		intimationRightFLayout.addComponent(txtReasonForAdmission);
		intimationRightFLayout.addComponent(reasonlayout);
		
		// Intimation Layout
		intimationHLayout = new HorizontalLayout(intimationLeftFLayout, intimationRightFLayout);
		intimationHLayout.setWidth("100%");
		intimationHLayout.setMargin(true);
		
		// Intimation Panel
		intimationPanel = new Panel();
		intimationPanel.setWidth("100%");
		intimationPanel.setCaption("Intimation Details");
		intimationPanel.addStyleName("panelHeader");
//		intimationPanel.addStyleName("g-search-panel");
		intimationPanel.setContent(intimationHLayout);
		
		// Hospital Left Form Layout
		hospitalLeftFLayout = new FormLayout();
		hospitalLeftFLayout.setWidth("100%");
		hospitalLeftFLayout.setMargin(false);
		hospitalLeftFLayout.addComponent(admissionDateField);
		hospitalLeftFLayout.addComponent(cmbAdmissionType);
		hospitalLeftFLayout.addComponent(cmbStates);
		hospitalLeftFLayout.addComponent(cmbCitys);
		hospitalLeftFLayout.addComponent(cmbHospitals);
		hospitalLeftFLayout.addComponent(hospitalSearchHLayout);
		hospitalLeftFLayout.addComponent(txtHospitalAddress);
		
		// Hospital Right Form Layout
		hospitalRightFLayout = new FormLayout();
		hospitalRightFLayout.setWidth("100%");
		hospitalRightFLayout.setMargin(false);
		hospitalRightFLayout.addComponent(dateOfDischargeDF);
		hospitalRightFLayout.addComponent(inpatientNumbertextField);
		hospitalRightFLayout.addComponent(cmbManagementType);
		
		 GridLayout hospitalContactGrid = new GridLayout(2,1);
		 hospitalContactGrid.addComponent(txtHospitalContactNumber, 0, 0);
		 txtHospitalContactNumber.setStyleName("gridAlignment");
		 txtHospitalContactNumber.addStyleName("gridAlignment");
		 hospitalContactGrid.addComponent(hospitalPhnSearchBtn, 1, 0);
		 hospitalContactGrid.setSpacing(true);
		 hospitalContactGrid.setCaption("Hospital Contact Number");
		 hospitalContactGrid.setComponentAlignment(txtHospitalContactNumber, Alignment.TOP_CENTER);
		
		 hospitalRightFLayout.addComponent(hospitalContactGrid);

		//hospitalRightFLayout.addComponent(txtHospitalContactNumber);
		hospitalRightFLayout.addComponent(txtHospitalFaxNumber);
		hospitalRightFLayout.addComponent(doctorNameTxt);
		hospitalRightFLayout.addComponent(cmbRoomCategory);
		hospitalRightFLayout.addComponent(cmbHospitalType);
		hospitalRightFLayout.addComponent(dummyChkBox);
		
		// Hospital Layout
		hospitalHLayout = new HorizontalLayout(hospitalLeftFLayout, hospitalRightFLayout);
		hospitalHLayout.setWidth("100%");
		hospitalHLayout.setMargin(true);
		
		// Hospital Panel
		hospitalPanel = new Panel();
		hospitalPanel.setWidth("100%");
		hospitalPanel.setCaption("Hospital Details");
		hospitalPanel.addStyleName("panelHeader");
//		hospitalPanel.addStyleName("g-search-panel");
		hospitalPanel.setContent(hospitalHLayout);
		
		// Comments Layout
		commentsHLayout = new HorizontalLayout(commentstextArea, suspiciousHospCommentsTA);
		commentsHLayout.setWidth("100%");
		commentsHLayout.setMargin(true);
		
		// Comments Panel
		commentsPanel = new Panel();
		commentsPanel.setWidth("100%");
		commentsPanel.setCaption("");
		commentsPanel.addStyleName("panelHeader");
//		commentsPanel.addStyleName("g-search-panel");
		commentsPanel.setContent(commentsHLayout);
		
		// Buttons Layout
		buttonsHLayout = new HorizontalLayout(buttonHorizontalLayout);
		buttonsHLayout.setWidth("100%");
		buttonsHLayout.setMargin(true);
		buttonsHLayout.setComponentAlignment(buttonHorizontalLayout, Alignment.MIDDLE_CENTER);	
		
		insuredTable = new Table();
		insuredTable.setCaption("Insured Details");
		//Vaadin8-setImmediate() insuredTable.setImmediate(false);
		insuredTable.setWidth("100.0%");
//		insuredTable.setHeight("200px");
		insuredTable.setPageLength(4);
		
		setValuesForInsuredTable(isHealthAndPA);
		
		coverDetailsTable = new Table();
		coverDetailsTable.setCaption("Cover Information");
		//Vaadin8-setImmediate() coverDetailsTable.setImmediate(false);
		coverDetailsTable.setWidth("100.0%");
//		insuredTable.setHeight("200px");
		coverDetailsTable.setPageLength(3);
		
		setValuesForCoversTable();
		
		// Buttons Panel
		buttonsPanel = new Panel();
		buttonsPanel.setWidth("100%");
		buttonsPanel.setCaption("");
		buttonsPanel.addStyleName("panelHeader");
//		buttonsPanel.addStyleName("g-search-panel");
		buttonsPanel.setContent(buttonsHLayout);
		

		// Risk Infomration Details
		riskInfoPanel = new Panel();
		riskInfoPanel.setWidth("100%");
		riskInfoPanel.setCaption("Risk Information Details");
		riskInfoPanel.addStyleName("panelHeader");
//		riskInfoPanel.addStyleName("g-search-panel");
		
		// Cover Information Details
		coverInfoPanel = new Panel();
		coverInfoPanel.setWidth("100%");
		coverInfoPanel.setCaption("Cover Information Details");
		coverInfoPanel.addStyleName("panelHeader");
//		coverInfoPanel.addStyleName("g-search-panel");
		
		// Bottom Layout
		VerticalLayout bottomVLayout = new VerticalLayout(intimationPanel, hospitalPanel, commentsPanel, buttonsPanel,insuredTable,coverDetailsTable);
		bottomVLayout.setMargin(false);
		bottomVLayout.setSpacing(true);
		return bottomVLayout;
	}
	
	private void resetIntimationCreationPage() {
		cmbModeOfIntimation.focus();
		
		// Intimation Left Layout
		cmbModeOfIntimation.setValue(null);
		cmbIntimatedBy.setValue(null);
		intimatorNameTxt.setValue(null);

		if(sumOfInsured!=null){
		sumOfInsured.setReadOnly(false);
		sumOfInsured.setValue(null);
		sumOfInsured.setReadOnly(true);
		}
		
		callerContactNumberTxt.setValue(null);
		//callerLandlineNumberTxt.setValue(null);
		attenderContactNumberTxt.setValue(null);
		//attenderLandlineNumberTxt.setValue(null);
		callerAddressTA.setValue(null);
		callerEmailTF.setValue(null);
		hospitalRequiredYNChkBox.setValue(false);
		
		// Intimation Right Layout
		if(tmpMainMemberNameCB != null)
			tmpMainMemberNameCB.setValue(null);
		if(tmpMainMemberNameCB2 != null)
			tmpMainMemberNameCB2.setValue(null);
		if(accidentDeathOG !=  null){
			accidentDeathOG.setValue(null);
			accidentDeathOG.setVisible(false);
		}
		if(insuredTypeCB != null){
			insuredTypeCB.setValue(null);
		}

		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			BeanItemContainer<Insured> insuredList = (BeanItemContainer<Insured>) cmbInsuredPatient.getContainerDataSource();
			cmbInsuredPatient.setValue(insuredList.getIdByIndex(0));
			if(insuredList.getIdByIndex(0) != null && insuredList.getIdByIndex(0).getInsuredId() != null)
			bean.setRiskId(insuredList.getIdByIndex(0).getInsuredId().toString());
			if(insuredList.getIdByIndex(0) != null && insuredList.getIdByIndex(0).getHealthCardNumber() != null){
				bean.setHealthCardNo(insuredList.getIdByIndex(0).getHealthCardNumber());
			}
		}
		
		patientNameTF.setValue(null);
		chkPatientNotCovered.setValue(false);
		if(parentPatientNameTF != null){
			parentPatientNameTF.setValue(null);
		}
		if(dateOfBirthDF != null){
			dateOfBirthDF.setValue(null);
		}
		if(ageTF != null){
			ageTF.setValue(null);
		}
		//parentInsuredChkBox.setValue(false);
		if(txtReasonForAdmission != null){
			txtReasonForAdmission.setValue(null);
		}
		
		// Hospital Left Layout
		admissionDateField.setValue(null);
		if(dateOfDischargeDF != null){
			dateOfDischargeDF.setValue(null);
			dateOfDischargeDF.setVisible(false);
		}
		cmbAdmissionType.setValue(null);
		cmbStates.setValue("");
		cmbCitys.setValue("");
		cmbHospitals.setValue("");
		txtHospitalAddress.setValue(null);
		txtHospitalAddress.setEnabled(true);
		
		// Hospital Right Layout
		cmbManagementType.setValue(null);
		txtHospitalContactNumber.setValue(null);
		txtHospitalContactNumber.setEnabled(true);
		txtHospitalFaxNumber.setValue(null);
		txtHospitalFaxNumber.setEnabled(true);
		doctorNameTxt.setValue(null);
		cmbRoomCategory.setValue(null);
		cmbHospitalType.setValue(null);
		cmbHospitalType.setEnabled(true);
		dummyChkBox.setValue(false);
		
		if(studentOrParent != null){
			studentOrParent.setValue(true);
		}
		if(dateOfBirthDF != null){
			dateOfBirthDF.setValue(null);
		}
		
		if(studentDOB != null){
			studentDOB.setValue(null);
		}
		if(parentPatientNameTF != null){
			parentPatientNameTF.setValue(null);
		}
		if(txtStudentPatientName != null){
			txtStudentPatientName.setValue(null);
		}
		
		// Comments Layout
		commentstextArea.setValue(null);
		if(suspiciousHospCommentsTA != null){
			suspiciousHospCommentsTA.setValue(null);
		}
		
		if(hospitalSearchHLayout != null){
			hospitalSearchHLayout.setVisible(false);
		}
		
		showOrHideValidation(false);
		
		submitBtn.setEnabled(true);
	}

	public void setHospitalDetails(HospitalDto hospitalDto) {
		try {
			if (selectedHospital == null) {
				selectedHospital = new HospitalDto();
			}
			selectedHospital = hospitalDto;
			selectedHospital.setKey(hospitalDto.getKey());
			this.bean.setHospitalDto(hospitalDto);
			setSelectedHospital();
			
			this.cmbHospitalType.setValue(this.bean.getHospitalDto()
					.getHospitalType());
			this.bean.setHospitalType(this.bean.getHospitalDto()
					.getHospitalType());
			
			if(this.bean.getHospitalDto()
					.getHospitalType()!= null && this.bean.getHospitalDto()
							.getHospitalType().getId() != null
					&& this.bean.getHospitalDto()
					.getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
				searchHospitalBtn.setCaption("ANH Count"
						+ " "
						+ (anhHospitalCount == null
								|| anhHospitalCount.intValue() <= 0 ? ""
								: anhHospitalCount.toString()));
			}else{
				searchHospitalBtn.setCaption("ANH Count");
			}
			hospitalSearchHLayout.setVisible(true);
			
			if(dummyChkBox != null){
				dummyChkBox.setValue(null);
				dummyChkBox.setEnabled(false);
			}

			bindBeanToUI();
			popupWindow.close();
			setHospitalFieldsEditable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setSelectedHospital() {

		if (selectedHospital != null) {

			UnFreezHospitals hospitalById = hospitalService
					.getUnFreezHospitalById(selectedHospital != null ? selectedHospital
							.getKey() : null);
			if (hospitalById != null) {
				
				if(hospitalById.getCityId() != null){
					bean.setCity(masterService.getCityByKey(hospitalById
							.getCityId()));
				}
				
				if(hospitalById.getStateId() != null){
					bean.setState(masterService.getStateByKey((hospitalById
							.getStateId())));
				}
				
				bean.getHospitalDto().setKey(selectedHospital.getKey());
				bean.getHospitalDto().setCity(selectedHospital.getCity());
				bean.getHospitalDto().setState(selectedHospital.getState());
				bean.getHospitalDto().setCpuId(selectedHospital.getCpuId());
				bean.getHospitalDto().setHospitalType(
						selectedHospital.getHospitalType());
				bean.getHospitalDto().setName(selectedHospital.getName());
				bean.getHospitalDto().setEmailId(selectedHospital.getEmailId());
			}
		}
	}

	private void tmpManualBind() {

		if (this.newBornBabyTable != null
				&& !this.newBornBabyTable.getValues().isEmpty()) {
			this.bean.setNewBabyIntimationListDto(this.newBornBabyTable
					.getValues());
			this.bean.setDeletedBabyList(this.newBornBabyTable
					.getDeltedBabyList());
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

		if (cmbStates.getValue() != null) {
//			cmbStates.setValidationVisible(false);
		}
		if (cmbCitys.getValue() != null) {
//			cmbCitys.setValidationVisible(false);
		}
		// if (cmbArea.getText() != null) {
		// cmbArea.setValidationVisible(false);
		// }
		if (cmbHospitals.getValue() != null) {
//			cmbHospitals.setValidationVisible(false);
		}

		/*
		if (this.newBornBabyTable != null
				&& (this.newBornBabyTable.getValues() == null || this.newBornBabyTable
						.getValues().isEmpty())) {
			eMsg.append("Please Provide New Baby Details.</br>");
			hasError = true;
		}

		if (this.newBornBabyTable != null
				&& this.newBornBabyTable.getValues() != null
				&& !this.newBornBabyTable.getValues().isEmpty()) {
			if (!this.newBornBabyTable.isValid()) {
				for (String error : this.newBornBabyTable.getErrors())
					eMsg.append(error).append("</br>");
				hasError = true;
			}
		}

		if (this.newBornBabyTable != null
				&& this.newBornBabyTable.getValues() != null
				&& !this.newBornBabyTable.getValues().isEmpty()
				&& this.newBornBabyTable.getValues().size() > 5) {
			eMsg.append("Maximum of Five Babies only Allowed.</br>");
			hasError = true;
		}
		*/

		// TODO Token field
		
		if (cmbAdmissionType.getValue() != null) {
			if (StringUtils.containsIgnoreCase(cmbAdmissionType.getValue()
					.toString(), "late")) {
				//mandatoryFields.add(inpatientNumbertextField);
				//mandatoryFields.add(reasonforlateintimationTxta);
				if(inpatientNumbertextField != null && inpatientNumbertextField.isVisible()){
					if(inpatientNumbertextField.getValue() == null || (inpatientNumbertextField.getValue() != null
							&& inpatientNumbertextField.getValue().equalsIgnoreCase(""))){
						eMsg.append("Please Enter Inpatient Number.</br>");
						hasError = true;
					}
				}
			}
		}
		
		if (txtReasonForAdmission.getValue() == null
				|| txtReasonForAdmission.getValueStr() == null
				|| txtReasonForAdmission.getValueStr().equals("")) {
			eMsg.append("Please Enter Reason For Admission.</br>");
			hasError = true;
		}

		if (cmbStates.getValue() == null || cmbStates.getValue().equals("")
				|| selectedState == null) {
			eMsg.append("Please Select a State.</br>");
			hasError = true;
		}

		/*if (cmbCity.getText() == null || cmbCity.getText().equals("")
				|| selectedCity == null) { // ("").equals(cmbCity.getText())
			eMsg.append("Please Select a City. </br>");
			hasError = true;
		}*/

//		if (!(this.claimTypecmb.getValue() != null)) {
//			eMsg.append("Please Select Claim Type. </br>");
//			hasError = true;
//		}
        Boolean isHospitalAlert = false;
		if (cmbHospitals.getValue() == null || cmbHospitals.getValue().equals("")) {
			hasError = true;
			if (cmbHospitalType.getValue() != null) {
				if (StringUtils.containsIgnoreCase(cmbHospitalType.getValue()
						.toString(), "network")
						&& selectedHospital == null) {
					hasError = true;
				} else {

					if (this.bean.getHospitalDto() != null
							&& this.bean.getHospitalDto()
									.getNotRegisteredHospitals() == null) {
						hasError = true;
					}
				}
			} else {
				hasError = true;
			}
			isHospitalAlert = true;
			eMsg.append("Please Select a Hospital Name. </br>");
		}

		if (cmbHospitalType.getValue() != null
				&& !StringUtils.containsIgnoreCase(cmbHospitalType.getValue()
						.toString(), "network")
				&& (txtHospitalAddress.getValue() == null || txtHospitalAddress
						.getValue().equals(""))) {
			hasError = true;
			eMsg.append("Please Provide Hospital Address. </br>");
		}else{
			if (cmbHospitalType.getValue() !=null && StringUtils.containsIgnoreCase(cmbHospitalType.getValue()
					.toString(), "Not-Registered")){
				if(dummyChkBox != null && ((dummyChkBox.getValue() == null) || (dummyChkBox.getValue() != null && ! dummyChkBox.getValue()))){
					hasError = true;
					eMsg.append("Please Select Dummy Flag </br>");
				}
			
			if(dummyChkBox != null && dummyChkBox.getValue() != null && dummyChkBox.getValue()){
				if(txtHospitalContactNumber != null && (txtHospitalContactNumber.getValue() == null 
						||(txtHospitalContactNumber.getValue() != null && txtHospitalContactNumber.getValue().equalsIgnoreCase("")))){
					hasError = true;
					eMsg.append("Please Enter Hospital Phone No.</br>");
				}
			}
			
			}
		}

		if (cmbHospitalType.getValue() != null
				&& !StringUtils.containsIgnoreCase(cmbHospitalType.getValue()
						.toString(), "network")) {

			if (!validateNumber(txtHospitalPinCode.getValue())) {

				hasError = true;
				eMsg.append("Please Provide Valid Pincode. </br>");

			}

			if (!validateNumber(txtHospitalContactNumber.getValue())) {
				hasError = true;
				eMsg.append("Please Provide Valid Contact Number. </br>");
			}

			if (!validateNumber(txtHospitalFaxNumber.getValue())) {
				hasError = true;
				eMsg.append("Please Provide Valid Fax Number. </br>");

			}

			if (!validateNumber(txtHospitalMobileNumber.getValue())) {
				hasError = true;
				eMsg.append("Please Provide Valid Mobile Number. </br>");
			}
	
//			if (!isValidEmail(txtHospitalEmailId.getValue())
//					&& this.cmbHospitalType.getValue() != null
//					&& ((SelectValue) this.cmbHospitalType.getValue()).getId() == ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID) {
//				eMsg.append("Please Provide Valid Email Id.</br>");
//				hasError = true;
//			}

		}
			/*if (!validateNumber(txtHospitalContactNumber.getValue())) {
				hasError = true;
				eMsg.append("Please Provide Valid Hospital Contact Number. </br>");
			}*/
		if (commentstextArea.getValue() != null
				&& commentstextArea.getValue().length() > 4000) {

			hasError = true;
			eMsg .append("Please Provide Comments with Maximum of 4000 Letters Only. </br>");
		}
		
		if (suspiciousHospCommentsTA.getValue() != null && suspiciousHospCommentsTA.getValue().length() > 4000) {
			hasError = true;
			eMsg .append("Please Provide Suspicious Hospital Comments with Maximum of 4000 Letters Only. </br>");
		}
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
					if(tmpMainMemberNameCB2 != null && tmpMainMemberNameCB2.getValue() == null){
						hasError = true;
						eMsg .append("Please Select Insured details </br>");
						
					}else if(! hasError){
						GmcMainMemberList insured = (GmcMainMemberList)tmpMainMemberNameCB2.getValue();
						bean.setRiskId(insured.getRiskId().toString());
					}
		}
		
		/*if (intimatorNameTxt.getValue() != null) {
			String trim = intimatorNameTxt.getValue().trim();
			if(trim.equalsIgnoreCase("")){
				hasError = true;
				eMsg .append("Please Enter Intimater Name. </br>");
			}
		}*/
		
		if (cmbHospitals.getValue() != null && ! isHospitalAlert) {
			String trim = cmbHospitals.getValue().trim();
			if(trim.equalsIgnoreCase("")){
				hasError = true;
				eMsg .append("Please Select a Hospital Name. </br>");
		    }
		}
		
		if(! hasError && bean.getHospitalType() != null && bean.getHospitalType().getValue() != null && bean.getHospitalType().getValue().toLowerCase().contains("network")) {
			Date admissionDate = admissionDateField.getValue();
			if(admissionDate != null && bean.getHospitalDto() != null 
					&& bean.getHospitalDto().getKey() != null && bean.getRiskId() != null){
				List<Intimation> findDuplicateInitmation = intimationService.findDuplicateInitmation(admissionDate, bean.getHospitalDto().getKey(), 
						bean.getPolicySummary().getPolicyNo(), bean.getRiskId());
				if(findDuplicateInitmation != null && ! findDuplicateInitmation.isEmpty()){
					alertMessage(findDuplicateInitmation.get(0).getIntimationId()); 
					return false;
				}else{
					if(bean.getHospitalDto().getHospitalCode() != null){
						PremiaPreviousClaim findPremiaIntimationDuplicate = intimationService.findPremiaIntimationDuplicate(admissionDate, bean.getHospitalDto().getHospitalCode(), 
								bean.getPolicySummary().getPolicyNo(), bean.getRiskId());
						if(findPremiaIntimationDuplicate != null){
							alertMessage(findPremiaIntimationDuplicate.getIntimationNumber());
							return false;
						}
					}
				}
			}
		}
		
		if((this.bean.getPolicySummary().getProductCode().equals(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE))
				&& ReferenceTable.getGpaUnnamedSection().containsKey(bean.getPolicySummary().getSectionCode())){
			if(studentOrParent != null && studentOrParent.getValue() != null 
					&& studentOrParent.getValue().toString().equals("true")){
				if(studentDOB.getValue() == null){
					hasError = true;
					eMsg.append("Please Enter Student Date of Birth. </br>");
				}
			}else if(studentOrParent != null && studentOrParent.getValue() != null 
					&& studentOrParent.getValue().toString().equals("false")){
				if(dateOfBirthDF.getValue() == null){
					hasError = true;
					eMsg.append("Please Enter Parent Date of Birth. </br>");
				}
				if(studentDOB.getValue() == null){
					hasError = true;
					eMsg.append("Please Enter Student Date of Birth. </br>");
				}
			}
		}
		
		if (hasError) {
//			setRequired(true);
			showErrorMessage(eMsg.toString());
			hasError = true;
			showOrHideValidation(false);
			return !hasError;
		} else {
			try {
				this.binder.commit();

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}

	private void showErrorMessage(String eMsg) {
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

	private void showOrHideValidation(Boolean isVisible) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}
			//callerLandlineNumberTxt.setValidationVisible(isVisible);
			//attenderLandlineNumberTxt.setValidationVisible(isVisible);

			if (inpatientNumbertextField != null) {
				inpatientNumbertextField.setValidationVisible(isVisible);
			}
			if (reasonforlateintimationTxta != null) {
				reasonforlateintimationTxta.setValidationVisible(isVisible);
			}
		}
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

	@SuppressWarnings("serial")
	public void addListener() {
		searchHospitalBtn.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2206691737881802049L;

			@Override
			public void buttonClick(ClickEvent event) {

				//setSelectedHospital();
				final SearchIntimationHospitalUI searchHospitalUI = searchIntimationHospitalUI
						.get();
				searchHospitalUI.init();
				popupWindow = new com.vaadin.ui.Window();
				popupWindow.setCaption("View Network Hospital");
				popupWindow.setWidth("65%");
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
		updateHospitalBtn.addClickListener(new ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 2206691737881802049L;

			@Override
			public void buttonClick(ClickEvent event) {
				UpdateHospitalDTO updateHospitalDto = new UpdateHospitalDTO(
						bean.getHospitalDto());
				if (selectedHospital != null) {
					updateHospitalDto.setCity(selectedCity.getValue());
					updateHospitalDto.setCityId(selectedCity.getKey());
					updateHospitalDto.setAddress(txtHospitalAddress.getValue());
					updateHospitalDto.setState(selectedState.getValue());
					updateHospitalDto.setStateId(selectedState.getKey());
					// if (selectedArea != null) {
					// SelectValue area = new SelectValue();
					// area.setId(selectedArea.getKey());
					// area.setValue(selectedArea.getValue());
					// updateHospitalDto.setLocalityId(area);
					// }
					updateHospitalDto.setPincode(txtHospitalPinCode.getValue());
					updateHospitalDto.setPhoneNumber(txtHospitalContactNumber
							.getValue());
					updateHospitalDto.setMobileNumber(txtHospitalMobileNumber
							.getValue());
					updateHospitalDto.setFaxNumber(txtHospitalFaxNumber
							.getValue());
					updateHospitalDto.setEmailId(txtHospitalEmailId.getValue());
					updateHospitalDto.setHospitalName(cmbHospitals.getValue());
					updateHospitalDto.setHospitalCode(hospitalCodeTxt
							.getValue());
					updateHospitalDto.setHospitalCodeIrda(hospitalCodeIrdaTxt
							.getValue());
					if (cmbIntimatedBy.getValue() != null) {
						updateHospitalDto
								.setIntimatedById(((SelectValue) cmbIntimatedBy
										.getValue()).getId());
					}
					if (cmbModeOfIntimation.getValue() != null) {
						updateHospitalDto
								.setModeOfIntimationId(((SelectValue) cmbModeOfIntimation
										.getValue()).getId());
					}
					final UpdateHospitalUI viewHospitalDetails = hospitalUI
							.get();
					viewHospitalDetails.initView(instance, updateHospitalDto);
					popupWindow = new com.vaadin.ui.Window();
					popupWindow.setCaption("Update Hospital Details");
					popupWindow.setWidth("65%");
					popupWindow.setContent(viewHospitalDetails);
					popupWindow.setClosable(true);
					popupWindow.center();
					popupWindow.setResizable(false);
					popupWindow.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							popupWindow.close();
						}
					});

					popupWindow.setModal(true);
					UI.getCurrent().addWindow(popupWindow);
				} else {
					Notification
							.show("Please Choose State, City and Hospital.");
				}
			}
		});
		submitBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8984704678920844731L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (validatePage()) {
					if (isRegisteredHospital(((SelectValue) cmbHospitalType.getValue()).getValue())) {
						setSelectedHospital();
					} else {
						saveNotRegisteredHospital();
					}
					tmpManualBind();
					setReasonForAdmissionToBean();
					setClaimTypeToBean();
					
					String userName = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
					String password = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD);
					bean.setUsername(userName);
					bean.setPassword(password);
					
					Map<String, String> userCredential = new HashMap<String, String>();
					userCredential.put(BPMClientContext.USERID, userName);
					userCredential.put(BPMClientContext.PASSWORD, password);

					Long statusKey;
					if(bean.getHospitalType().getValue().toLowerCase().contains("network")) {
						statusKey = ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY;
					} else {
						statusKey = ReferenceTable.INTIMATION_PENDING_STATUS_KEY;
					}
					fireViewEvent(IntimationDetailsPresenter.INTIMATION_SAVE_SUBMIT_EVENT, bean, userCredential, statusKey);
				}
			}
		});
		saveBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4537606997624282170L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (validatePage()) {
					if (isRegisteredHospital(cmbHospitalType.getValue().toString())) {
						setSelectedHospital();
					} else {
						saveNotRegisteredHospital();
					}
					tmpManualBind();
					setReasonForAdmissionToBean();
					setClaimTypeToBean();
					
					String userName = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
					String password = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD);
					bean.setUsername(userName);
					bean.setPassword(password);
					
					Map<String, String> userCredential = new HashMap<String, String>();
					userCredential.put(BPMClientContext.USERID, userName);
					userCredential.put(BPMClientContext.PASSWORD, password);
					
					Long statusKey;
					boolean isDummy = false;
					if(dummyChkBox.getValue() != null) {
						isDummy = dummyChkBox.getValue(); 
					}
					if(isDummy) {
						statusKey = ReferenceTable.INTIMATION_PENDING_STATUS_KEY;
					} else {
						statusKey = ReferenceTable.INTIMATION_SAVE_STATUS_KEY;
					}
					fireViewEvent(IntimationDetailsPresenter.INTIMATION_SAVE_SUBMIT_EVENT, bean, userCredential, statusKey);
				}
			}
		});
		cancelBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8306584349875887347L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						IntimationDetailsPresenter.INTIMATION_CANCEL_EVENT,
						null);
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8306584349875887347L;
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setCity(null);
				bean.setState(null);
				resetIntimationCreationPage();
			}
		});
		
		if(cmbInsuredPatient != null){

		cmbInsuredPatient
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						Insured insured = (Insured) cmbInsuredPatient
								.getValue();

						if (insured == null) {
							if (newBornBabyTable != null) {
								chkPatientNotCovered.setValue(false);
								newBabyTableLayout
										.removeComponent(newBornBabyTable);
								newBornBabyTable.setValue(null);
								newBornBabyTable = null;
							}
						}
						if (insured != null) {
							bean.setHealthCardNo(insured.getHealthCardNumber());
							if(insured.getInsuredId() != null){
								bean.setRiskId(insured.getInsuredId().toString());
							}
							if (null != insured.getInsuredGender() && 
									!StringUtils.equalsIgnoreCase("F", insured
									.getInsuredGender().getValue())) {
								chkPatientNotCovered.setValue(false);

								if (newBornBabyTable != null) {
									newBabyTableLayout
											.removeComponent(newBornBabyTable);
									newBornBabyTable = null;
								}
							}

						}

					}
				});
		}
		
		if(tmpMainMemberNameCB2 != null){

			tmpMainMemberNameCB2
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						GmcMainMemberList insured = (GmcMainMemberList) tmpMainMemberNameCB2.getValue();
						if(insured != null){
							bean.setHealthCardNo(insured.getIdCardNumber());
							if(insured.getRiskId() != null){
								bean.setRiskId(insured.getRiskId().toString());
							}
						}
					}
				});
		}

		chkPatientNotCovered.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Insured insured = cmbInsuredPatient != null ? (Insured) cmbInsuredPatient.getValue() : null;
				if (insured == null) {
					// Notification.show("Please Choose Insured Patient Name.");
					chkPatientNotCovered.setValue(false);
					if (chkPatientNotCovered != null && chkPatientNotCovered.getValue()) {
						Collection<Window> windows = UI.getCurrent().getWindows();
						for (Window window : windows) {
							window.close();
						}
						showErrorMessage("Please Choose Insured Patient Name.");
					}
					return;
				} else if (StringUtils.containsIgnoreCase(insured .getInsuredGender().getValue(), "F")) {
					
//				} else if (StringUtils.containsIgnoreCase(insured .getInsuredGender().getValue(), "F") && 
//						(StringUtils.containsIgnoreCase(insured.getRelationshipwithInsuredId().getValue(), "spouse") || 
//								StringUtils.containsIgnoreCase(insured.getRelationshipwithInsuredId().getValue(), "Self"))) {
//					fireViewEvent(IntimationDetailsPresenter.BUILD_NEW_BORN_BABY_TABLE, chkPatientNotCovered.getValue());
				} else {
					chkPatientNotCovered.setValue(false);
					
					Collection<Window> windows = UI.getCurrent().getWindows();
					for (Window window : windows) {
						window.close();
					}
					showErrorMessage("Not Applicable for the selected Insured.");
					// Notification.show("Not Applicable for the selected Insured.");
					
					return;
				}
			}
		});

		insuredSearchBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				SearchInsuredUI searchInsuredUIInstance = searchInsuredUI.get();
				searchInsuredUIInstance.initView(bean.getPolicy());

				UI.getCurrent().addWindow(
						new MyInsuredSearch("Search Insured",
								searchInsuredUIInstance));
			}
		});
		
		if(gmcInsuredSearchBtn != null){
			gmcInsuredSearchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
	
					SearchGMCInsuredUI searchInsuredUIInstance = searchGmcInsuredUI.get();
					searchInsuredUIInstance.initView(bean.getPolicySummary());
					
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setCaption("Insured List");
					popup.setContent(searchInsuredUIInstance);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
	
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
	
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					
					
					
				}
			});
		}
		
		if(hospitalPhnSearchBtn != null){
			hospitalPhnSearchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
	
					
					searchHospitalUI.initView(bean);
					popupWindow = new com.vaadin.ui.Window();
					popupWindow.setCaption("Hospital Search");
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

		/*cmbState.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<State>() {
			@Override
			public void onSuggestionPicked(State state) {
				if (state != null && cmbState.getText() != null
						&& !cmbState.getText().equals("")) {
					handleStateSelection(state);
					bean.setState(state);
				} else {
					Notification.show("Pealse Select a Valid State");
				}
				cmbCity.setText("");
				cmbCity.setValue(null);
				cmbCity.setValidationVisible(false);
				// cmbArea.setText("");
				// cmbArea.setValue(null);
				// cmbArea.setValidationVisible(false);
				cmbHospital.setText("");
				cmbHospital.setValue(null);
				cmbHospital.setValidationVisible(false);
				selectedCity = null;
				// selectedArea = null;
				selectedHospital = null;
				clearHospitalDetails();

			}
		});*/

		/*cmbCity.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<CityTownVillage>() {
			@Override
			public void onSuggestionPicked(CityTownVillage city) {
				if (city != null && cmbCity.getText() != null
						&& !cmbCity.getText().equals("")) {
					handleCitySelection(city);
					bean.setCity(city);
				} else {
					Notification.show("Pealse Select a Valid City");
				}
				// cmbArea.setText("");
				// cmbArea.setValue(null);
				// cmbArea.setValidationVisible(false);
				cmbHospital.setText("");
				cmbHospital.setValue(null);
				cmbHospital.setValidationVisible(false);
				// selectedArea = null;
				selectedHospital = null;
				clearHospitalDetails();
			}
		});*/

		// cmbArea.setSuggestionPickedListener(new
		// AutocompleteSuggestionPickedListener<Locality>() {
		// @Override
		// public void onSuggestionPicked(Locality locality) {
		// if (locality != null && cmbArea.getText() != null
		// && !cmbArea.getText().equals("")) {
		// handleAreaSelection(locality);
		// cmbHospital.setText("");
		// cmbHospital.setValue(null);
		// cmbHospital.setValidationVisible(false);
		// selectedHospital = null;
		// clearHospitalDetails();
		// }
		//
		// }
		// });
		/*cmbHospital
				.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<HospitalDto>() {

					@Override
					public void onSuggestionPicked(HospitalDto suggestion) {
						if (suggestion != null) {
							bean.setHospitalType(suggestion.getHospitalType());
							bean.setHospitalTypeValue(suggestion
									.getHospitalTypeValue());
							bean.setHospitalDto(suggestion);
							bindBeanToUI();
							handleHospitalSelection(suggestion);
							
							if(suggestion.getHospitalType()!= null && suggestion.getHospitalType().getId() != null
									&& suggestion.getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
								searchHospitalBtn.setCaption("ANH Count"
										+ " "
										+ (anhHospitalCount == null
												|| anhHospitalCount.intValue() <= 0 ? ""
												: anhHospitalCount.toString()));
							}else{
								searchHospitalBtn.setCaption("ANH Count");
							}
							hospitalSearchHLayout.setVisible(true);
						} else {
							Notification
									.show("Please Select a Hospital Name or Enter Hospital Details");
							setHospitalFieldsEditable(true);
							hospitalSearchHLayout.setVisible(false);
							bindTempHospitalTypeToUI();
						}
					}
				});*/

		cmbHospitalType
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						if (event.getProperty().getValue() != null) {
							if (StringUtils.containsIgnoreCase(event
									.getProperty().getValue().toString(),
									"network")) {
								setHospitalFieldsEditable(false);
							}
						}
					}
				});
		
		if(cmbInsuredPatient !=null && sumOfInsured !=null){
		cmbInsuredPatient.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					Insured insured = (Insured) event.getProperty().getValue();
					Long sumInsuredVlue =Long.valueOf(sumInsured);
					Map<String, Double> balanceSIRnd = dbCalculationService.getBalanceSIRnd(insured.getPolicy().getKey(),insured.getKey(),0l,0l,sumInsuredVlue);
					sumOfInsured.setReadOnly(false);
					sumOfInsured.setValue(balanceSIRnd.containsKey(SHAConstants.CURRENT_BALANCE_SI) ? balanceSIRnd.get(SHAConstants.CURRENT_BALANCE_SI).toString()  : "");					
					sumOfInsured.setReadOnly(true);
				}
			}
		});
		}
		

		cmbAdmissionType.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue admissionTypemastersValue = (SelectValue) cmbAdmissionType.getValue();
				if (admissionTypemastersValue != null && admissionTypemastersValue.getId() == 1002) {
					buildLateIntimationComponents();
				} else {
					removeLateIntimationComponents();
				}
			}
		});
		
		admissionDateField.setData(bean.getPolicySummary());
		admissionDateField
				.addValueChangeListener(new Property.ValueChangeListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void valueChange(ValueChangeEvent event) {
						PremPolicy policy = (PremPolicy) ((PopupDateField) event
								.getProperty()).getData();

						Date enteredDate = (Date) ((PopupDateField) event
								.getProperty()).getValue();
						if (enteredDate != null) {

							try {
								admissionDateField.validate();
								enteredDate = (Date) event.getProperty()
										.getValue();
							} catch (Exception e) {
								admissionDateField.setValue(null);
								showErrorMessage("Please Enter a valid Date");
								// Notification.show("Please Enter a valid Date");
								return;
							}
						}

						Date currentDate = new Date();
						Date policyFrmDate = null;
						Date policyToDate = null;
						Collection<?> itemIds = cmbAdmissionType
								.getContainerDataSource().getItemIds();
						if (policy != null) {
							String policySource = policy.getPolicySource();
						    if(policySource != null && policySource.equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						    	policyFrmDate = SHAUtils.combineDateTime(policy.getDateofInception()+ " "+ "00:00");
						    	policyToDate = SHAUtils.combineDateTime(policy.getPolicyEndDate()+ " "+ "23:59");
							   
							}else{
								policyFrmDate = new Date(policy.getDateofInception());
								policyToDate = new Date(policy.getPolicyEndDate());
							}
						}
				
						if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
								|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
								|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
								|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
							GmcMainMemberList selectedGmcInsuredList = bean.getSelectedGmcInsuredList();
							if(selectedGmcInsuredList != null){
								policyFrmDate = selectedGmcInsuredList.getEffectiveFromDate();
								policyToDate = selectedGmcInsuredList.getEffectiveToDate();
							}else{
								policyFrmDate = null;
								policyToDate = null;
								event.getProperty().setValue(null);
								cmbAdmissionType.setValue(null);
								Collection<Window> windows = UI.getCurrent().getWindows();
								for (Window window : windows) {
									window.close();
								}
								showErrorMessage("Please select insured patient name");
							}
							
						}
						
						if (enteredDate != null && policyFrmDate != null
								&& policyToDate != null) {
							long diff = Math.abs(enteredDate.getTime()
									- policyFrmDate.getTime());
							long diffDays = diff / (24 * 60 * 60 * 1000);
							if (!enteredDate.after(policyFrmDate)
									|| enteredDate.compareTo(policyToDate) > 0) {
								event.getProperty().setValue(null);
								cmbAdmissionType.setValue(null);

								showErrorMessage("Admission Date is not in range between Policy From Date and Policy To Date.");
								// Notification
								// .show("Error",
								// "Admission Date is not in range between Policy From Date and Policy To Date.",
								// Notification.Type.HUMANIZED_MESSAGE);
							} else if (enteredDate.after(currentDate)) {
								cmbAdmissionType.setValue(itemIds.toArray()[2]);
							} else if (DateUtils.isSameDay(enteredDate,
									currentDate)) {
								cmbAdmissionType.setValue(itemIds.toArray()[0]);
							} else if (enteredDate.before(currentDate)) {
								cmbAdmissionType.setValue(itemIds.toArray()[1]);
							}

							// else if (diffDays < 30) {
							// event.getProperty().setValue(null);
							// cmbAdmissionType.setValue(null);
							//
							// showErrorMessage("Admission Date is less than 30 days from Policy From Date.");
							// // Notification
							// // .show("Error",
							// //
							// "Admission Date is less than 30 days from Policy From Date.",
							// // Notification.Type.HUMANIZED_MESSAGE);
							// }
							cmbAdmissionType.setEnabled(false);
						}
					}
				});
		
		dummyChkBox.addValueChangeListener(new Property.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean isDummy = false;
				if(event.getProperty().getValue() != null) {
					isDummy = (boolean) event.getProperty().getValue();
				}
				submitBtn.setEnabled(!isDummy);
				saveBtn.setEnabled(isDummy);
			}
		});
		
		if(studentOrParent != null){
			studentOrParent.addValueChangeListener(new Property.ValueChangeListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					   studentLayout();
					}else{
						parentLayout();
					}
				}

				
			});
		}
		
		
	}

	protected void handleStateSelection(State state) {
		selectedState = state;
	}

	protected void handleCitySelection(CityTownVillage city) {
		selectedCity = city;
	}

	private void onAutocompleteStateSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		State state = (State) suggestion.getData();		

		if (state != null && cmbStates.getValue() != null		
				&& !cmbStates.getValue().equals("")) {		
			handleStateSelection(state);		
			bean.setState(state);		
		} else {		
			Notification.show("Pealse Select a Valid State");		
		}		
		cmbCitys.setValue("");		
		cmbCitys.setData(null);		
		//cmbCitys.setValidationVisible(false);		
		// cmbArea.setText("");		
		// cmbArea.setValue(null);		
		// cmbArea.setValidationVisible(false);		
		cmbHospitals.setValue("");		
		cmbHospitals.setData(null);		
		/*cmbHospital.setValidationVisible(false);*/		
		selectedCity = null;		
		// selectedArea = null;		
		selectedHospital = null;		
		clearHospitalDetails();		


	}		

	private void onAutocompleteCitySelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		CityTownVillage city = (CityTownVillage) suggestion.getData();		


		if (city != null && cmbCitys.getValue() != null		
				&& !cmbCitys.getValue().equals("")) {		
			handleCitySelection(city);		
			bean.setCity(city);		
		} else {		
			Notification.show("Pealse Select a Valid City");		
		}		
		// cmbArea.setText("");		
		// cmbArea.setValue(null);		
		// cmbArea.setValidationVisible(false);		
		cmbHospitals.setValue("");		
		cmbHospitals.setData(null);		
		//cmbHospital.setValidationVisible(false);		
		// selectedArea = null;		
		selectedHospital = null;		
		clearHospitalDetails();		
	}

	// protected void handleAreaSelection(Locality area) {
	// selectedArea = area;
	// }
	
	private void onAutocompleteHospitalSelect(AutocompleteEvents.SelectEvent event) {		
		AutocompleteSuggestion suggestion = event.getSuggestion();		
		String caption = "Suggestion selected: " + suggestion.getValue();		
		HospitalDto hospitalData = (HospitalDto) suggestion.getData();		
		if (hospitalData != null) {		
			bean.setHospitalType(hospitalData.getHospitalType());		
			bean.setHospitalTypeValue(hospitalData		
					.getHospitalTypeValue());		
			bean.setHospitalDto(hospitalData);		
			bindBeanToUI();		
			handleHospitalSelection(hospitalData);		

			if(hospitalData.getHospitalType()!= null && hospitalData.getHospitalType().getId() != null		
					&& hospitalData.getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){		
				searchHospitalBtn.setCaption("ANH Count"		
						+ " "		
						+ (anhHospitalCount == null		
						|| anhHospitalCount.intValue() <= 0 ? ""		
								: anhHospitalCount.toString()));		
			}else{		
				searchHospitalBtn.setCaption("ANH Count");		
			}		
			hospitalSearchHLayout.setVisible(true);		
		} else {		
			Notification		
			.show("Please Select a Hospital Name or Enter Hospital Details");		
			setHospitalFieldsEditable(true);		
			hospitalSearchHLayout.setVisible(false);		
			bindTempHospitalTypeToUI();		
		}		

	}

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		BeanItemContainer<SelectValue> modeOfIntimation = (BeanItemContainer<SelectValue>) referenceData
				.get("modeOfIntimation");
		BeanItemContainer<SelectValue> intimatedBy = (BeanItemContainer<SelectValue>) referenceData
				.get("intimatedBy");

		BeanItemContainer<SelectValue> claimType = (BeanItemContainer<SelectValue>) referenceData
				.get("claimType");

		BeanItemContainer<SelectValue> managementType = (BeanItemContainer<SelectValue>) referenceData
				.get("managementType");

		BeanItemContainer<SelectValue> relapseofIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("relapseofIllness");

		BeanItemContainer<Insured> insuredList = (BeanItemContainer<Insured>) referenceData
				.get("insuredList");

		BeanItemContainer<SelectValue> roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");

		BeanItemContainer<SelectValue> admissionType = (BeanItemContainer<SelectValue>) referenceData
				.get("admissionType");

		BeanItemContainer<SelectValue> hospitalType = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalType");
		
		sumInsured = (String) referenceData.get("sumInsured");	
		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeList().containsKey(bean.getPolicySummary().getProduct().getCode()))){
				Insured insured = insuredList.getIdByIndex(0);
			Long sumInsuredVlue =Long.valueOf(sumInsured);

			Map<String, Double> balanceSIRnd = dbCalculationService.getBalanceSIRnd(insured.getPolicy().getKey(),insured.getKey(),0l,0l,sumInsuredVlue);
			sumOfInsured.setValue(balanceSIRnd.containsKey(SHAConstants.CURRENT_BALANCE_SI) ? balanceSIRnd.get(SHAConstants.CURRENT_BALANCE_SI).toString()  : "");					
			sumOfInsured.setReadOnly(true);
		}
		
		
		cmbModeOfIntimation.setContainerDataSource(modeOfIntimation);
		cmbModeOfIntimation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfIntimation.setItemCaptionPropertyId("value");
		if(modeOfIntimation.getItemIds() != null && ! modeOfIntimation.getItemIds().isEmpty()){
			List<SelectValue> itemIds = modeOfIntimation.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue.getId().equals(ReferenceTable.MODE_OF_INTIMATION_PHONE)){
					cmbModeOfIntimation.setValue(selectValue);
					break;
				}
			}
			
		}

		cmbIntimatedBy.setContainerDataSource(intimatedBy);
		cmbIntimatedBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimatedBy.setItemCaptionPropertyId("value");
		
		if(intimatedBy.getItemIds() != null && ! intimatedBy.getItemIds().isEmpty()){
			cmbIntimatedBy.setValue(intimatedBy.getItemIds().get(0));
		}
		/*tmpMainMemberNameCB.setContainerDataSource(insuredList);
		tmpMainMemberNameCB.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tmpMainMemberNameCB.setItemCaptionPropertyId("insuredName");*/
		
		/*tmpMainMemberNameCB2.setContainerDataSource(insuredList);
		tmpMainMemberNameCB2.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tmpMainMemberNameCB2.setItemCaptionPropertyId("insuredName");*/
		
		// Insured Type
		BeanItemContainer<SelectValue> insuredTypeBIC = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(bean.getPolicySummary().getProduct().getKey());
		selectValue1.setValue(bean.getPolicySummary().getProductName());
		insuredTypeBIC.addItem(selectValue1);
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(10000L);
		selectValue2.setValue("Accident");
		insuredTypeBIC.addItem(selectValue2);
		
		insuredTypeCB.setContainerDataSource(insuredTypeBIC);
		insuredTypeCB.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		insuredTypeCB.setItemCaptionPropertyId("value");
		insuredTypeCB.setData(selectValue1);
		insuredTypeCB.setNullSelectionAllowed(false);
		if(insuredTypeCB.isVisible()){
			insuredTypeCB.setValue(insuredTypeCB.getData());
		}

		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			cmbInsuredPatient.setContainerDataSource(insuredList);
			cmbInsuredPatient.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbInsuredPatient.setItemCaptionPropertyId("insuredName");
			cmbInsuredPatient.setNullSelectionAllowed(false);
			if(insuredList.size()>0) {
			cmbInsuredPatient.setValue(insuredList.getIdByIndex(0));
			
			if(insuredList.getIdByIndex(0) != null && insuredList.getIdByIndex(0).getInsuredId() != null)
				bean.setRiskId(insuredList.getIdByIndex(0).getInsuredId().toString());
			
			if(insuredList.getIdByIndex(0) != null && insuredList.getIdByIndex(0).getHealthCardNumber() != null){
				bean.setHealthCardNo(insuredList.getIdByIndex(0).getHealthCardNumber());
			}
			}
			cmbInsuredPatient.setData(insuredList);
		}

		cmbRelapseofIllness.setContainerDataSource(relapseofIllness);
		cmbRelapseofIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRelapseofIllness.setItemCaptionPropertyId("value");
		cmbRelapseofIllness.setValue(relapseofIllness.getIdByIndex(0));

		intimatorNameTxt.setValue(this.bean.getIntimaterName());

		cmbHospitalType.setContainerDataSource(hospitalType);
		cmbHospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalType.setItemCaptionPropertyId("value");

		claimTypecmb.setContainerDataSource(claimType);
		claimTypecmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		claimTypecmb.setItemCaptionPropertyId("value");

		cmbAdmissionType.setContainerDataSource(admissionType);
		cmbAdmissionType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAdmissionType.setItemCaptionPropertyId("value");
		
		if(admissionType.getItemIds() != null && ! admissionType.getItemIds().isEmpty() && admissionType.getItemIds().size() > 1){
			cmbAdmissionType.setValue(admissionType.getItemIds().get(admissionType.getItemIds().size()-1));
		}

		cmbManagementType.setContainerDataSource(managementType);
		cmbManagementType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbManagementType.setItemCaptionPropertyId("value");
		
		if(managementType.getItemIds() != null && ! managementType.getItemIds().isEmpty() && managementType.getItemIds().size() > 1){
			cmbManagementType.setValue(managementType.getItemIds().get(1));
		}

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");

		setEditReferenceData();
		bindBeanToUI();
		
		if((this.bean.getPolicySummary().getProductCode().equals(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE))
				&& ReferenceTable.getGpaUnnamedSection().containsKey(bean.getPolicySummary().getSectionCode())){
			studentOrParent.setValue(true);
		}
		
		if((SHAConstants.PACKAGE_PRODUCT.equals(bean.getPolicySummary().getProduct().getIntimationType()))) {
			patientNameChangedBasedOnInsuredType();
		}
	}

	private void setEditReferenceData() {
		if (this.bean.getModeOfIntimation() != null) {
			this.cmbModeOfIntimation.setValue(this.bean.getModeOfIntimation());
		}
		
		if (this.bean.getSumInsured() != null) {
			this.sumOfInsured.setReadOnly(false);
			this.sumOfInsured.setValue(this.bean.getSumInsured());
			this.sumOfInsured.setReadOnly(true);
		}

		if (this.bean.getIntimatedBy() != null) {
			this.cmbIntimatedBy.setValue(this.bean.getIntimatedBy());
		}

		if (this.bean.getInsuredPatient() != null && this.cmbInsuredPatient != null) {
			this.cmbInsuredPatient.setValue(this.bean.getInsuredPatient());
			
		}

		if (this.bean.getNewBornFlag()) {
			this.chkPatientNotCovered.setValue(this.bean.getNewBornFlag());
			showNewBabyTable();

			if (bean.getNewBabyIntimationListDto() != null) {
				newBornBabyTable.setTableList(bean
						.getNewBabyIntimationListDto());
			}
		}

		if (this.bean.getRelapseofIllnessValue() != null) {
			Collection<?> itemIds = this.cmbRelapseofIllness
					.getContainerDataSource().getItemIds();
			List itemslist = new ArrayList();
			itemslist.addAll(itemIds);
			this.cmbRelapseofIllness
					.setValue(this.bean.getRelapseofIllnessValue()
							.toLowerCase().contains("y") ? itemslist.get(0)
							: itemslist.get(1));
		}

		if (this.bean.getAdmissionType() != null) {
			this.cmbAdmissionType.setValue(this.bean.getAdmissionType());
		}

		if (this.bean.getAdmissionType() != null
				&& this.bean.getAdmissionType().getValue() != null
				&& StringUtils.containsIgnoreCase(this.bean.getAdmissionType()
						.getValue(), "late")) {
			buildLateIntimationComponents();
			inpatientNumbertextField.setValue(this.bean.getInpatientNumber());
			reasonforlateintimationTxta.setValue(this.bean
					.getLateIntimationReason());
		}

		// TODO Token field
		if (this.bean.getReasonForAdmission() != null) {

			BeanItemContainer<SelectValue> reasonforAdmission = (BeanItemContainer<SelectValue>) referenceData
					.get("reasonForAdmission");

			if (reasonforAdmission != null) {
				List<SelectValue> reasonsList = reasonforAdmission.getItemIds();
				txtReasonForAdmission.setContainer(reasonForAdmissionContainer);
				for (SelectValue reason : reasonsList) {
					addTokenValue(reason);
				}

			}

		}

		if (this.bean.getState() != null) {
			this.cmbStates.setValue(this.bean.getState().getValue());
		}
		if (this.bean.getCity() != null) {
			this.cmbCitys.setValue(this.bean.getCity().getValue());
		}
		// if (this.bean.getArea() != null) {
		// this.cmbArea.setValue(this.bean.getArea().getValue());
		// }
		if (this.bean.getHospitalDto() != null) {
			this.cmbHospitalType.setValue(this.bean.getHospitalDto()
					.getHospitalType());
		}

		if (this.bean.getHospitalType() != null) {

			if (this.bean.getHospitalType().getId() != ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID) {
				this.networkHospitalTypeTxt.setEnabled(false);
				if (this.bean.getHospitalType().getId() == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID) {
					this.networkHospitalTypeTxt.setValue(this.bean
							.getHospitalDto().getNetworkHospitalType()
							.getValue());
					// this.claimTypecmb.setEnabled(true);
					// this.claimTypecmb.setValue(claimTypeIds.toArray()[0]);

				}
				// else{
				// this.claimTypecmb.setValue(claimTypeIds.toArray()[1]);
				// this.claimTypecmb.setEnabled(false);
				// }
			} else {
				bindTempHospitalTypeToUI();
				this.networkHospitalTypeTxt.setValue("");
				bindBeanToUI();
			}
		}

		/*
		 * to be changed.
		 */

		Collection<?> claimTypeIds = claimTypecmb.getContainerDataSource()
				.getItemIds();

		if (this.bean.getClaimType() != null) {
			if (((SelectValue) claimTypeIds.toArray()[0]).getValue()
					.equalsIgnoreCase(this.bean.getClaimType().getValue())) {
				this.claimTypecmb.setEnabled(true);
				this.claimTypecmb.setValue(claimTypeIds.toArray()[0]);
			} else {
				this.claimTypecmb.setValue(claimTypeIds.toArray()[3]);
				this.claimTypecmb.setEnabled(false);
			}
		}

		if (this.bean.getManagementType() != null) {
			this.cmbManagementType.setValue(this.bean.getManagementType());
		}

		if (this.bean.getRoomCategory() != null) {
			this.cmbRoomCategory.setValue(this.bean.getRoomCategory());
		}
		if (this.bean.getAdmissionType() != null) {
			if (StringUtils.containsIgnoreCase(this.bean.getAdmissionType()
					.getValue(), "late")) {
				buildLateIntimationComponents();
			} else {
				removeLateIntimationComponents();
			}
		}

		addListener();
	}

	@SuppressWarnings("serial")
	final class MyInsuredSearch extends Window {
		public MyInsuredSearch(String caption, ViewComponent component) {
			super(caption, component);
			this.setCaption("Insured Search");
			this.setModal(true);
			this.setResizable(true);
		}
	}

	public void init(NewIntimationDto bean) {
		this.bean = bean;
	}

	private void bindBeanToUI() {
		HospitalDto hospitalDto = this.bean.getHospitalDto();

		State id = bean.getState();
		if (!ValidatorUtils.isNull(id)) {
			cmbStates.setValue(id.getValue());
			cmbStates.setData(id);
			selectedState = id;
		}

		CityTownVillage city = bean.getCity();
		if (!ValidatorUtils.isNull(city)) {
			cmbCitys.setValue(city.getValue());
			cmbCitys.setData(city);
			selectedCity = city;
		}

		if (!ValidatorUtils.isNull(hospitalDto)
				&& hospitalDto.getName() != null) {
			cmbHospitals.setValue(hospitalDto.getName());
			cmbHospitals.setData(hospitalDto);
			if (StringUtils.containsIgnoreCase(bean.getHospitalType()
					.getValue(), "network")) {
				selectedHospital = hospitalDto;
			}

		}

		if (bean.getHospitalType() != null
				&& !StringUtils.containsIgnoreCase(bean.getHospitalType()
						.getValue(), "network")) {

			cmbHospitalType.setValue(hospitalDto.getHospitalType().getValue());
			networkHospitalTypeTxt.setValue("");
			setHospitalFieldsEditable(true);

		} else {
			setHospitalContentEditable(false);
		}

		if (hospitalDto != null && hospitalDto.getKey() != null) {
			getAndFillHospitalDetails(hospitalDto);
		}
	}

	private boolean isRegisteredHospital(String hospitalType) {
		if (!ValidatorUtils.isNull(hospitalType)) {
			if (hospitalType == "" || hospitalType == null) {
				Notification.show("Error", "Please Select a Hospital",
						Notification.Type.ERROR_MESSAGE);
				return false;
			}
			return (hospitalType.toLowerCase().contains("network"));
		} else {
			Notification.show("Network Hospital Not Available");
		}
		return false;
	}

/*	private void setUpAutoState(AutocompleteField<State> search) {
		search.setQueryListener(new AutocompleteQueryListener<State>() {
			@Override
			public void handleUserQuery(AutocompleteField<State> field,
					String query) {
				selectedState = null;
				selectedCity = null;
				handleStateSearchQuery(field, query);

			}
		});
	}

	private void setUpAutoCity(AutocompleteField<CityTownVillage> search) {
		search.setQueryListener(new AutocompleteQueryListener<CityTownVillage>() {
			@Override
			public void handleUserQuery(
					AutocompleteField<CityTownVillage> field, String query) {
				if (cmbStates.getValue() != null
						&& !cmbStates.getValue().equals("")
						&& !ValidatorUtils.isNull(selectedState)) {
					handleCitySearchQuery(field, query);
				} else {
					Notification.show("Please Select a State");
				}
			}
		});
	}*/

	// private void setUpAutoArea(AutocompleteField<Locality> area) {
	// area.setQueryListener(new AutocompleteQueryListener<Locality>() {
	// @Override
	// public void handleUserQuery(AutocompleteField<Locality> field,
	// String query) {
	// if (!ValidatorUtils.isNull(selectedState)) {
	// handleAreaSearchQuery(field, query);
	// }
	// }
	// });
	// }

/*	private void setUpAutoHospital(AutocompleteField<HospitalDto> search) {
		search.setQueryListener(new AutocompleteQueryListener<HospitalDto>() {
			@Override
			public void handleUserQuery(AutocompleteField<HospitalDto> field,
					String query) {
				if (cmbStates.getValue() != null
						&& !cmbStates.getValue().equals("")
						&& !ValidatorUtils.isNull(selectedState)) {
					handleHospitalSearchQuery(field, query);
				} else {
					Notification
							.show("State and City both are mandatory for Hospital Selection, Please Select State and City");
				}
			}
		});
	}*/

	private void getAndFillHospitalDetails(HospitalDto hospitalDto) {
		Long key = hospitalDto.getKey();
		if (!ValidatorUtils.isNull(key)) {

			cmbHospitalType.setValue(hospitalDto.getHospitalType().getValue());
			if (bean.getHospitalType() != null) {
				if (isRegisteredHospital(bean.getHospitalType().getValue())) {
					UnFreezHospitals hospital = hospitalDto.getRegistedUnFreezHospitals();
					hospitalCodeTxt
							.setValue(hospital.getHospitalCode() != null ? hospital
									.getHospitalCode() : "");
					hospitalCodeIrdaTxt
							.setValue(hospital.getHospitalIrdaCode() != null ? hospital
									.getHospitalIrdaCode() : "");
					/*txtHospitalAddress
							.setValue(hospital.getAddress() != null ? hospital
									.getAddress() : "");*/
					
					String hospAddress = (hospital.getAddress() != null ? hospital.getAddress() : "") + ","+
							(hospital.getCity() != null ? hospital.getCity() : "") + ","+ 
							(hospital.getState() != null ? hospital.getState(): "")  +"-"+
							(hospital.getPincode() != null ? hospital.getPincode(): "");
					
						txtHospitalAddress.setValue(hospAddress != null ? hospAddress : "");
						
					txtHospitalContactNumber
							.setValue(hospital.getPhoneNumber() != null ? hospital
									.getPhoneNumber() : "");
					txtHospitalMobileNumber
							.setValue(hospital.getMobileNumber() != null ? hospital
									.getMobileNumber() : "");
					txtHospitalPinCode
							.setValue(hospital.getPincode() != null ? hospital
									.getPincode() : "");
					txtHospitalFaxNumber
							.setValue(hospital.getFax() != null ? hospital
									.getFax() : "");
					networkHospitalTypeTxt.setValue(hospitalDto
							.getNetworkHospitalType() != null ? hospitalDto
							.getNetworkHospitalType().getValue() : "");

					txtHospitalEmailId.setValue(hospital.getEmailId());
					cmbHospitals.setValue(hospital.getName());
					
					//IMSSUPPOR-27813
					doctorNameTxt.setValue(hospital.getRepresentativeName() !=null ? hospital.getRepresentativeName() : "" );

				} else if (hospitalDto.getNotRegisteredHospitals() != null) {

					/*txtHospitalAddress.setValue(hospitalDto
							.getNotRegisteredHospitals().getAddress());*/
					
					String hospAddress = (hospitalDto
							.getNotRegisteredHospitals().getAddress() != null ? hospitalDto
									.getNotRegisteredHospitals().getAddress() : "") + ","+
									(hospitalDto
											.getNotRegisteredHospitals().getCity() != null ? hospitalDto
													.getNotRegisteredHospitals().getCity() : "") + ","+ 
							(hospitalDto
									.getNotRegisteredHospitals().getState() != null ? hospitalDto
											.getNotRegisteredHospitals().getState() : "") + ","+ 
							(hospitalDto
									.getNotRegisteredHospitals().getPincode() != null ? hospitalDto
											.getNotRegisteredHospitals().getPincode(): "");
				
					txtHospitalAddress.setValue(hospAddress != null ? hospAddress : "");
					
					txtHospitalContactNumber.setValue(hospitalDto
							.getNotRegisteredHospitals().getContactNumber());
					txtHospitalPinCode
							.setValue(hospitalDto.getNotRegisteredHospitals()
									.getPincode() != null ? hospitalDto
									.getNotRegisteredHospitals().getPincode()
									.toString() : "");
					txtHospitalMobileNumber.setValue(hospitalDto
							.getNotRegisteredHospitals().getMobileNumber());
					txtHospitalEmailId.setValue(hospitalDto
							.getNotRegisteredHospitals().getEmailId());
					txtHospitalFaxNumber
							.setValue(hospitalDto.getNotRegisteredHospitals()
									.getFaxNumber() != null ? hospitalDto
									.getNotRegisteredHospitals().getFaxNumber()
									.toString() : "");
					txtHospitalEmailId.setValue(hospitalDto
							.getNotRegisteredHospitals() != null ? hospitalDto
							.getNotRegisteredHospitals().getEmailId() : "");
					networkHospitalTypeTxt.setValue("");
					
					//IMSSUPPOR-27813
					doctorNameTxt.setValue(hospitalDto
							.getNotRegisteredHospitals().getRepresentativeName() !=null ? hospitalDto
									.getNotRegisteredHospitals().getRepresentativeName() : "" );

				}
			}
		}
	}

	public void saveNotRegisteredHospital() {
		SelectValue hospitalType = (!ValidatorUtils.isNull(cmbHospitalType
				.getValue())) ? (SelectValue) cmbHospitalType.getValue() : null;

		if (cmbHospitals.getValue() != null && cmbHospitals.getValue() != "") {
			TmpHospital tmpHospital = new TmpHospital();
			tmpHospital.setHospitalName(cmbHospitals.getValue());
			tmpHospital.setAddress(txtHospitalAddress.getValue());
		/*	Long pincode = null;
			if (txtHospitalPinCode.getValue() != null
					&& txtHospitalPinCode.getValue() != "") {
				try {
					pincode = Long.parseLong(txtHospitalPinCode.getValue());
				} catch (Exception e) {
					pincode = null;

				}

				tmpHospital.setPincode(pincode);*/
				tmpHospital.setContactNumber(txtHospitalContactNumber
						.getValue());
				if(tmpHospital.getContactNumber() == null){
					tmpHospital.setContactNumber("-");
				}
				
				tmpHospital.setMobileNumber(txtHospitalMobileNumber.getValue());
				tmpHospital.setEmailId(txtHospitalEmailId.getValue());
				if(selectedCity != null){
					tmpHospital.setCityId(selectedCity.getKey());
					tmpHospital.setCity(selectedCity.getValue());
				}
				tmpHospital.setStateId(selectedState.getKey());
				tmpHospital.setState(selectedState.getValue());

				String faxNumber = null;
				if (txtHospitalFaxNumber.getValue() != null
						&& txtHospitalFaxNumber.getValue() != "") {
					try {
						faxNumber = txtHospitalFaxNumber.getValue();

					} catch (Exception e) {
					}
				}
				tmpHospital.setFaxNumber(faxNumber);
				//IMSSUPPOR-27813 - added for doctor name
				tmpHospital.setRepresentativeName(doctorNameTxt.getValue());
				
				selectedHospital = new HospitalDto(tmpHospital, hospitalType);
				selectedHospital.setCpuId(null);
				bean.setHospitalDto(selectedHospital);
				bean.getHospitalDto().setHospitalType(
						(SelectValue) cmbHospitalType.getValue());
				Long tempHospitalKey = hospitalService
						.createTmpHospital(tmpHospital);
				bean.getHospitalDto().setKey(tempHospitalKey);

				bean.setCpuId(null);
				bean.setCpuCode(null);

			} else {

				if (cmbHospitals.getValue() == null
						|| cmbHospitals.getValue() == "") {
					Notification.show("Please Enter a Hospital Name");
				}
			}
	}

	private boolean validateNumber(String value) {

		boolean hasError = true;
		Long number = null;
		if (value != null && value != "") {
			try {
				number = Long.parseLong(value);

			} catch (Exception e) {
				hasError = false;

			}
		}

		return hasError;
	}

	private void handleHospitalSelection(HospitalDto suggestion) {
		if (suggestion != null) {
			selectedHospital = suggestion;

			if (suggestion.getKey() != null) {
				UnFreezHospitals registedHospitals = suggestion.getRegistedUnFreezHospitals();
				if (registedHospitals != null) {
					/*this.txtHospitalAddress.setValue(registedHospitals
							.getAddress());*/
					String hospAddress = (registedHospitals.getAddress() != null ? registedHospitals.getAddress() : "") + ","+
							(registedHospitals.getCity() != null ? registedHospitals.getCity() : "") + ","+ 
							(registedHospitals.getState() != null ? registedHospitals.getState(): "")  +"-"+
							(registedHospitals.getPincode() != null ? registedHospitals.getPincode(): "");
					
					this.txtHospitalAddress.setValue(hospAddress != null ? hospAddress : "");					
					this.txtHospitalContactNumber.setValue(registedHospitals
							.getPhoneNumber());
					this.txtHospitalPinCode.setValue(registedHospitals
							.getPincode());
					this.txtHospitalFaxNumber.setValue(registedHospitals
							.getFax());
					this.txtHospitalEmailId.setValue(registedHospitals
							.getEmailId());
					this.txtHospitalMobileNumber.setValue(registedHospitals
							.getMobileNumber());
					this.hospitalCodeIrdaTxt.setValue(registedHospitals
							.getHospitalIrdaCode());
					this.hospitalCodeTxt.setValue(registedHospitals
							.getHospitalCode());

					setHospitalFieldsEditable(false);

					SelectValue hospitalTypeValue = new SelectValue();
					hospitalTypeValue.setId(registedHospitals.getHospitalType()
							.getKey());
					hospitalTypeValue.setValue(registedHospitals
							.getHospitalType().getValue());
					cmbHospitalType.setValue(hospitalTypeValue);

					networkHospitalTypeTxt
							.setValue(registedHospitals
									.getNetworkHospitalType() != null ? registedHospitals
									.getNetworkHospitalType() : "");

					Collection<?> claimTypeIds = claimTypecmb
							.getContainerDataSource().getItemIds();

					if (registedHospitals.getHospitalType().getValue()
							.equalsIgnoreCase("network")) {
						networkHospitalTypeTxt.setValue(registedHospitals
								.getNetworkHospitalType());

						claimTypecmb.setValue(claimTypeIds.toArray()[0]);
						claimTypecmb.setEnabled(true);
					} else {
						claimTypecmb.setValue(claimTypeIds.toArray()[3]);
						claimTypecmb.setEnabled(false);
					}
					//IMSSUPPOR-27813 - added for doctor name
					doctorNameTxt.setValue(registedHospitals.getRepresentativeName() !=null ? registedHospitals.getRepresentativeName() : "" );
				}
			}
		} else {
			setHospitalFieldsEditable(true);
			bindTempHospitalTypeToUI();
		}

	}

	private void bindTempHospitalTypeToUI() {
		Collection<?> itemIds = cmbHospitalType.getContainerDataSource()
				.getItemIds();
		
		List<SelectValue> hospitalTypeList = (List<SelectValue>)itemIds;
		for (SelectValue selectValue : hospitalTypeList) {
			if(selectValue.getId().equals(ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID)){
				cmbHospitalType.setValue(selectValue);
			}
			
		}
		cmbHospitalType.setNullSelectionAllowed(false);
		setHospitalFieldsEditable(true);
		Collection<?> claimTypeIds = claimTypecmb.getContainerDataSource()
				.getItemIds();
		claimTypecmb.setValue(claimTypeIds.toArray()[3]);
		// this.claimTypecmb.setEnabled(false);
	}

	private void setHospitalFieldsEditable(boolean isEditable) {

		this.txtHospitalAddress.setEnabled(isEditable);
		this.txtHospitalContactNumber.setEnabled(isEditable);
		this.txtHospitalPinCode.setEnabled(isEditable);
		this.txtHospitalFaxNumber.setEnabled(isEditable);
		this.txtHospitalEmailId.setEnabled(isEditable);
		this.txtHospitalMobileNumber.setEnabled(isEditable);
		this.hospitalCodeIrdaTxt.setEnabled(isEditable);
		this.hospitalCodeTxt.setEnabled(isEditable);
		this.claimTypecmb.setEnabled(!isEditable);
		// if(isEditable)
		// {
		// clearHospitalDetails();
		// }
	}

	public void clearHospitalDetails() {
		this.txtHospitalAddress.setValue("");
		this.txtHospitalContactNumber.setValue("");
		this.txtHospitalPinCode.setValue("");
		this.txtHospitalFaxNumber.setValue("");
		this.txtHospitalEmailId.setValue("");
		this.txtHospitalMobileNumber.setValue("");
		this.hospitalCodeIrdaTxt.setValue("");
		this.hospitalCodeTxt.setValue("");
		this.claimTypecmb.setValue(null);
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
		cmbCitys.setValue("");
		cmbCitys.setData(null);
		// cmbArea.setText(null);
		// cmbArea.setValue(null);
		cmbHospitals.setData(null);
		cmbHospitals.setValue("");
		selectedHospital = null;
		clearHospitalDetails();
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
			cmbHospitals.setData(null);
			cmbHospitals.setValue("");
			selectedHospital = null;
			clearHospitalDetails();
		}
	}

	// private void handleAreaSearchQuery(AutocompleteField<Locality> field,
	// String query) {
	// if (selectedCity != null) {
	// List<Locality> areaSearch = masterService.localitySearch(
	// query.toLowerCase(), selectedCity);
	// for (Locality area : areaSearch) {
	// field.addSuggestion(area, area.getValue());
	// }
	// }
	// clearHospitalDetails();
	// }

	private void handleHospitalSearchQuery(
			Collection<AutocompleteSuggestion> suggestions, AutocompleteQuery query) {
		List<UnFreezHospitals> hospitalSearch = null;
		if(bean.getIsPaayasPolicy()){
			hospitalSearch = hospitalService
					.hospitalNameCriteriaSearchForPaayas(query.getTerm(), selectedState, selectedCity);
		}else{
			hospitalSearch = hospitalService
					.UnFreezHospitalNameCriteriaSearch(query.getTerm(), selectedState, selectedCity);
		}

		if (!hospitalSearch.isEmpty()) {
			anhHospitalCount = hospitalService.getANHHospitalCountCityWise(
					selectedState, selectedCity);
			anhHospitalCount = anhHospitalCount != null && anhHospitalCount > 0 ? anhHospitalCount - 1
					: 0;
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
			if(dummyChkBox != null){
				dummyChkBox.setValue(null);
				dummyChkBox.setEnabled(false);
			}
		} else {
			this.bean.setHospitalDto(null);
			setHospitalFieldsEditable(true);
			clearHospitalDetails();
			bindTempHospitalTypeToUI();
			networkHospitalTypeTxt.setValue("");
			if(dummyChkBox != null){
				dummyChkBox.setEnabled(true);
			}
		}
	}

	private void initComponents() {
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		if(!(bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeList().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			sumOfInsured = (TextField) binder.buildAndBind("Balance Sum Insured", "sumInsured", TextField.class);
			sumOfInsured.setMaxLength(200);
		}
		cmbModeOfIntimation = (ComboBox) binder.buildAndBind("Mode of Intimation", "modeOfIntimation", ComboBox.class);
		cmbIntimatedBy = (ComboBox) binder.buildAndBind("Intimated By", "intimatedBy", ComboBox.class);
		
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			
			tmpMainMemberNameCB = (TextField) binder.buildAndBind("", "gmcMainMemberName", TextField.class);
			tmpMainMemberNameCB.setEnabled(false);
			tmpMainMemberNameCB2 = new ComboBox("Insured Patient Name");
			
			gmcInsuredSearchBtn = new Button();
			gmcInsuredSearchBtn.setStyleName("link");
			gmcInsuredSearchBtn.setIcon(new ThemeResource("images/search.png"));
			
		}
		
		
		/*if(! (bean.getPolicy() != null && bean.getPolicy().getProduct() != null && bean.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_PRODUCT_CODE))){
			tmpMainMemberNameCB.setVisible(false);	
		}*/
		
		insuredTypeCB = (ComboBox) binder.buildAndBind("Insured Type", "insuredType", ComboBox.class);
		insuredTypeCB.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				patientNameChangedBasedOnInsuredType();
				
				if(insuredTypeCB != null && insuredTypeCB.getValue() !=null){
					SelectValue insuredVal = (SelectValue) insuredTypeCB.getValue();
					if(insuredVal.getValue().equalsIgnoreCase(SHAConstants.ACCIDENT)){
						setValuesForInsuredTable(true);
					} else {
						setValuesForInsuredTable(false);
					}
				}
				
				return;
			}


		});
		
		insuredTypeCB.setVisible(false);
		if((SHAConstants.PACKAGE_PRODUCT.equals(bean.getPolicySummary().getProduct().getIntimationType()))) {
			insuredTypeCB.setVisible(true);
			insuredTypeCB.setValue(insuredTypeCB.getData());
			
		}
		
		
		intimatorNameTxt = (TextField) binder.buildAndBind("Caller / Intimator Name", "intimaterName", TextField.class);
		intimatorNameTxt.setMaxLength(200);
		CSValidator hospitalNamevalidator = new CSValidator();
		hospitalNamevalidator.setRegExp("^[a-zA-Z.%*#,$@ ()]*$");
		hospitalNamevalidator.setPreventInvalidTyping(true);
		hospitalNamevalidator.extend(intimatorNameTxt);
		
		callerContactNumberTxt = (TextField) binder.buildAndBind("Caller Contact No", "callerContactNum", TextField.class);
		callerContactNumberTxt.setMaxLength(12);
		callerContactNumberTxt.setNullRepresentation("");
		CSValidator callerContactNumberTxtValidator = new CSValidator();
		callerContactNumberTxtValidator.extend(callerContactNumberTxt);
		callerContactNumberTxtValidator.setRegExp("^[0-9+-]*$");
		callerContactNumberTxtValidator.setPreventInvalidTyping(true);

		/*callerLandlineNumberTxt = (TextField) binder.buildAndBind("Caller Contact No (Mobile)", "callerLandlineNum", TextField.class);
		callerLandlineNumberTxt.setMaxLength(15);
		callerLandlineNumberTxt.setNullRepresentation("");
		CSValidator callerLandlineNumberTxtValidator = new CSValidator();
		callerLandlineNumberTxtValidator.extend(callerLandlineNumberTxt);
		callerLandlineNumberTxtValidator.setRegExp("^[0-9+-]*$");
		callerLandlineNumberTxtValidator.setPreventInvalidTyping(true);*/
		
		attenderContactNumberTxt = (TextField) binder.buildAndBind("Attender's Contact No", "attenderContactNum", TextField.class);
		attenderContactNumberTxt.setMaxLength(12);
		attenderContactNumberTxt.setNullRepresentation("");
		CSValidator attenderContactNumberTxtValidator = new CSValidator();
		attenderContactNumberTxtValidator.extend(attenderContactNumberTxt);
		attenderContactNumberTxtValidator.setRegExp("^[0-9+-]*$");
		attenderContactNumberTxtValidator.setPreventInvalidTyping(true);
		
		/*attenderLandlineNumberTxt = (TextField) binder.buildAndBind("Attender's Contact No (Mobile)", "attenderLandlineNum", TextField.class);
		attenderLandlineNumberTxt.setMaxLength(15);
		attenderLandlineNumberTxt.setNullRepresentation("");
		CSValidator attenderLandlineNumberTxtValidator = new CSValidator();
		attenderLandlineNumberTxtValidator.extend(attenderLandlineNumberTxt);
		attenderLandlineNumberTxtValidator.setRegExp("^[0-9+-]*$");
		attenderLandlineNumberTxtValidator.setPreventInvalidTyping(true);*/

		chkPatientNotCovered = (CheckBox) binder.buildAndBind("Patient not covered", "newBornFlag", CheckBox.class);
		chkPatientNotCovered.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(!chkPatientNotCovered.getValue()) {
					patientNameTF.setValue(null);
				}
				patientNameTF.setVisible(chkPatientNotCovered.getValue());
				return;
			}
		});
		
		patientNameTF = (TextField) binder.buildAndBind("Patient Name", "paPatientName", TextField.class);
		patientNameTF.setNullRepresentation("");
		patientNameTF.setVisible(chkPatientNotCovered.getValue());
		
		
		// Intimation Left Details
		callerAddressTA = (TextArea) binder.buildAndBind("Caller Address", "callerAddress", TextArea.class);
		callerAddressTA.setCaption("Caller Address");
		callerAddressTA.setWidth("180px");
		callerAddressTA.setRows(3);
		callerAddressTA.setNullRepresentation("");
		callerAddressTA.setMaxLength(2000);
		callerAddressTA.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		textAreaPopupShortCutListener(callerAddressTA, null);
		
		callerEmailTF = (TextField) binder.buildAndBind("Email", "callerEmail", TextField.class);
		callerEmailTF.setMaxLength(200);
		callerEmailTF.setNullRepresentation("");
		
		hospitalRequiredYNChkBox = (CheckBox) binder.buildAndBind("Hospital Required Y/N", "hospitalReq", CheckBox.class);
		hospitalRequiredYNChkBox.setVisible(insuredTypeCB.isVisible());
		
		// Intimation Right Details
		accidentDeathOG = (OptionGroup) binder.buildAndBind("Accident / Death", "incidenceFlag", OptionGroup.class);
		accidentDeathOG.removeAllItems();
		accidentDeathOG.addItems("Accident", "Death");
		accidentDeathOG.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		accidentDeathOG.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				hospitalRequiredYNChkBox.setValue("Accident".equals(accidentDeathOG.getValue()));
				return;
			}
		});
		
		accidentDeathOG.setVisible(false);
		/*if(bean.getPolicy() != null && ReferenceTable.PA_LOB_KEY.equals(bean.getPolicy().getLobId())) {
			accidentDeathOG.setVisible(true);
		}*/
		
		//parentInsuredChkBox = new CheckBox("Parent Insured");
		if((SHAConstants.PA_PRODUCT.equals(bean.getPolicySummary().getProduct().getIntimationType()))){
			accidentDeathOG.setVisible(true);
			accidentDeathOG.setValue("Accident");
			hospitalRequiredYNChkBox.setVisible(true);
		}
		if((this.bean.getPolicySummary().getProduct().getKey().equals(ReferenceTable.GPA_PRODUCT_KEY)
				|| this.bean.getPolicySummary().getProduct().getKey().equals(ReferenceTable.GPA_GROUP_ACCIDENT))
				&& ReferenceTable.getGpaUnnamedSection().containsKey(bean.getPolicySummary().getSectionCode())){
			
			studentOrParent = new OptionGroup();
			studentOrParent.addItems(getReadioButtonOptions());
			studentOrParent.setItemCaption(true, "Student");
			studentOrParent.setItemCaption(false, "Parent");
			studentOrParent.setStyleName("horizontal");
			studentOrParent.setCaption("Category");
	
		}
		
		
		/*parentInsuredChkBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(!parentInsuredChkBox.getValue()) {
					parentPatientNameTF.setValue(null);
					dateOfBirthDF.setValue(null);
					ageTF.setValue(null);
				}
				parentPatientNameTF.setVisible(parentInsuredChkBox.getValue());
				dateOfBirthDF.setVisible(parentInsuredChkBox.getValue());
				ageTF.setVisible(parentInsuredChkBox.getValue());
				return;
			}
		});*/
		
		if(! (bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			cmbInsuredPatient = (ComboBox) binder.buildAndBind("Insured Patient Name", "insuredPatient", ComboBox.class);
		}
		cmbAdmissionType = (ComboBox) binder.buildAndBind("Admission Type", "admissionType", ComboBox.class);
		cmbAdmissionType.setRequired(true);
		
		insuredSearchBtn = new Button();
		insuredSearchBtn.setStyleName("link");
		insuredSearchBtn.setIcon(new ThemeResource("images/search.png"));

		admissionDateField = (PopupDateField) binder.buildAndBind("Admission Date", "admissionDate", PopupDateField.class);
		admissionDateField.setDateFormat("dd/MM/yyyy HH:mm");
		admissionDateField.setInputPrompt("DD/MM/YYYY HH:MM");
		admissionDateField.setConversionError("Invalid date formate please enter a valid date in the format dd/mm/yyyy hh:mm");
		admissionDateField.setParseErrorMessage("Invalid date formate please enter a valid date in the format dd/mm/yyyy hh:mm");
		admissionDateField.setResolution(Resolution.MINUTE);
		admissionDateField.setLocale(new Locale("en", "EN"));
		admissionDateField.setTextFieldEnabled(false);
		
		admissionDateField.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) ((DateField) event.getProperty()).getValue();
				if (enteredDate != null && dateOfDischargeDF != null && dateOfDischargeDF.getValue() != null ) {
					if (enteredDate.after(dateOfDischargeDF.getValue())) {
						admissionDateField.setValue(null);
						showErrorMessage("Admission date cannot be greater than the date of discharge");
					}
				}
			}
				
		});
		
		dateOfDischargeDF = (PopupDateField) binder.buildAndBind("Date of Discharge", "dateOfDischarge", PopupDateField.class);
		dateOfDischargeDF.setDateFormat("dd/MM/yyyy HH:mm");
		dateOfDischargeDF.setInputPrompt("DD/MM/YYYY HH:MM");
		dateOfDischargeDF.setConversionError("Invalid date formate please enter a valid date in the format dd/mm/yyyy hh:mm");
		dateOfDischargeDF.setParseErrorMessage("Invalid date formate please enter a valid date in the format dd/mm/yyyy hh:mm");
		dateOfDischargeDF.setResolution(Resolution.MINUTE);
		dateOfDischargeDF.setLocale(new Locale("en", "EN"));
		dateOfDischargeDF.setVisible(false);
		dateOfDischargeDF.setTextFieldEnabled(false);

		dateOfDischargeDF.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) ((DateField) event.getProperty()).getValue();
				if (enteredDate != null && admissionDateField != null && admissionDateField.getValue() != null ) {
					if (enteredDate.before(admissionDateField.getValue())) {
						dateOfDischargeDF.setValue(null);
						showErrorMessage("Discharge date cannot be lesser than the date of admission");
					}
				}
			}
				
		});
		
		cmbRelapseofIllness = (ComboBox) binder.buildAndBind("Relapse of Illness", "relapseofIllness", ComboBox.class);
		dynamicFieldsLayout = new FormLayout();
		dynamicFieldsLayout.setSizeFull();
		inpatientNumbertextField = (TextField) binder.buildAndBind("Inpatient Number <b style= 'color: red'>*</b>", "inpatientNumber", TextField.class);

		inpatientNumbertextField.setCaptionAsHtml(true);
		/*CSValidator inpatientNumbervalidator = new CSValidator();
		inpatientNumbervalidator.setRegExp("^[0-9/]*$");
		inpatientNumbervalidator.setPreventInvalidTyping(true);
		inpatientNumbervalidator.extend(inpatientNumbertextField);*/

		inpatientNumbertextField.setVisible(false);
		reasonforlateintimationTxta = (TextArea) binder.buildAndBind("Reason For Late Intimation", "lateIntimationReason", TextArea.class);
		reasonforlateintimationTxta.setVisible(false);
		
		txtReasonForAdmission = new GTokenField("Reason For Admission");
		txtReasonForAdmission.addGTokenListener(this);
		
		Panel panel = new Panel();
		panel.setWidth("180px");
		panel.setHeight("150px");
		panel.setStyleName("gray");
		panel.setContent(txtReasonForAdmission);
		
		HorizontalLayout reasonlayout = new HorizontalLayout(panel);
		reasonlayout.setCaption("Reason for Admission *");
		
		fireViewEvent(IntimationDetailsPresenter.INTIALIZE_REASON_FOR_ADMISSION, null);
		
		updateHospitalBtn = new Button("Update Hospital Details");
		updateHospitalBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		updateHospitalBtn.addStyleName(ValoTheme.BUTTON_LINK);

		searchHospitalBtn = new Button("ANH Count");
		searchHospitalBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		searchHospitalBtn.addStyleName(ValoTheme.BUTTON_LINK);

		cmbHospitalType = (ComboBox) binder.buildAndBind("Hospital Type", "hospitalType", ComboBox.class);
		cmbHospitalType.setWidth("180px");
		cmbHospitalType.setEnabled(false);

		networkHospitalTypeTxt = new TextField("Network Hospital Type");
		networkHospitalTypeTxt.setWidth("180px");
		networkHospitalTypeTxt.setEnabled(false);
		networkHospitalTypeTxt.setNullRepresentation("");

		/*cmbState = new AutocompleteField<State>();
		cmbState.setCaption("State <b style= 'color: red'>*</b>");
		cmbState.setWidth("180px");
		//Vaadin8-setImmediate() cmbState.setImmediate(true);
		cmbState.setRequiredError("Please select a valid state");
		cmbState.setCaptionAsHtml(true);
		cmbState.setValidationVisible(false);
		cmbState.setMinimumQueryCharacters(2);
		cmbState.setIcon(null);*/
		
		AutocompleteSuggestionProvider suggestionProvider = new CustomSuggestionProvider();

		cmbStates = new AutocompleteTextField();		
		cmbStates.setCaption(stateCaption);		
		cmbStates.setCaptionAsHtml(true);		
		cmbStates.setItemAsHtml(false);		
		cmbStates.setMinChars(2);		

		cmbStates.setScrollBehavior(ScrollBehavior.NONE);		
		cmbStates.setSuggestionLimit(0);		
		//cmbStates.setSizeFull();		
		cmbStates.setRequiredIndicatorVisible(true);		
		cmbStates.setSuggestionProvider(suggestionProvider);		
		cmbStates.addSelectListener(this::onAutocompleteStateSelect);


		/*cmbCity = new AutocompleteField<CityTownVillage>();
<<<<<<< HEAD
>>>>>>> a1b28df37a8ac40ebb73f2c4cd51036c598b388a
=======
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
>>>>>>> 4eea262d64d106bf458191aab9f1de451252fa30
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setCaption("City");
		//Vaadin8-setImmediate() cmbCity.setImmediate(true);
		cmbCity.setRequiredError("Please select a valid city ");
		cmbCity.setCaptionAsHtml(true);
		cmbCity.setValidationVisible(false);
		cmbCity.setMinimumQueryCharacters(2);
		cmbCity.setWidth("180px");
		cmbCity.setIcon(null);*/

		cmbCitys = new AutocompleteTextField();		
		cmbCitys.setCaption(cityCaption);		
		cmbCitys.setCaptionAsHtml(true);		
		cmbCitys.setItemAsHtml(false);		
		cmbCitys.setMinChars(2);		
		cmbCitys.setScrollBehavior(ScrollBehavior.NONE);		
		cmbCitys.setSuggestionLimit(0);		

		cmbCitys.setSuggestionProvider(suggestionProvider);		
		cmbCitys.addSelectListener(this::onAutocompleteCitySelect);

		// cmbArea = new AutocompleteField<Locality>();
		// //Vaadin8-setImmediate() cmbArea.setImmediate(true);
		// cmbArea.setCaption("Area");
		// //Vaadin8-setImmediate() cmbArea.setImmediate(true);
		// cmbArea.setRequiredError("Please select a valid area");
		// cmbArea.setValidationVisible(false);
		// cmbArea.setMinimumQueryCharacters(2);
		// cmbArea.setWidth("180px");
		// cmbArea.setIcon(null);


/*		cmbHospital = new AutocompleteField<HospitalDto>();
<<<<<<< HEAD
>>>>>>> a1b28df37a8ac40ebb73f2c4cd51036c598b388a
=======
>>>>>>> ccd5ce4a0dbca1a78d4b18ee9f9b548659c38081
>>>>>>> 4eea262d64d106bf458191aab9f1de451252fa30
		//Vaadin8-setImmediate() cmbHospital.setImmediate(true);
		cmbHospital.setRequiredError("Please select a valid Hospital");
		cmbHospital.setValidationVisible(false);
		cmbHospital.setCaption("Hospital Name <b style= 'color: red'>*</b>");
		cmbHospital.setCaptionAsHtml(true);
		cmbHospital.setWidth("180px");
		cmbHospital.setIcon(null);
		cmbHospital.setMinimumQueryCharacters(2);
		cmbHospital.setDelay(50);

		cmbHospital.setRequired(true);
		cmbHospital.setRequiredError("Hosptial Name is mandatory please select hosptial Name");*/

		cmbHospitals = new AutocompleteTextField();		
		cmbHospitals.setCaption(hospitalNameCaption);		
		cmbHospitals.setCaptionAsHtml(true);		
		cmbHospitals.setItemAsHtml(false);		
		cmbHospitals.setMinChars(2);		
		cmbHospitals.setScrollBehavior(ScrollBehavior.NONE);		
		cmbHospitals.setSuggestionLimit(0);		

		cmbHospitals.setSuggestionProvider(suggestionProvider);		
		cmbHospitals.addSelectListener(this::onAutocompleteHospitalSelect);		
		cmbHospitals.setRequiredIndicatorVisible(true);
		
		// CSValidator hospitalNamevalidator = new CSValidator();
		// hospitalNamevalidator.setRegExp("^[A-Z a-z]*$");
		// hospitalNamevalidator.setPreventInvalidTyping(true);
		// hospitalNamevalidator.extend(cmbHospital);

		hospitalCodeTxt = new TextField("Hospital Code (Internal)");
		hospitalCodeTxt.setWidth("180px");
		hospitalCodeTxt.setMaxLength(15);
		hospitalCodeTxt.setNullRepresentation("");

		hospitalCodeIrdaTxt = new TextField("Hospital Code (IRDA)");
		hospitalCodeIrdaTxt.setWidth("180px");
		hospitalCodeIrdaTxt.setMaxLength(15);
		hospitalCodeIrdaTxt.setNullRepresentation("");

		txtHospitalAddress = new TextArea();
		txtHospitalAddress.setCaption("Hospital Address");
		txtHospitalAddress.setWidth("180px");
		txtHospitalAddress.setRows(3);
		txtHospitalAddress.setNullRepresentation("");
		txtHospitalAddress.setMaxLength(4000);
		txtHospitalAddress.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		textAreaPopupShortCutListener(txtHospitalAddress, null);

		txtHospitalPinCode = new TextField();
		txtHospitalPinCode.setCaption("Pincode");
		txtHospitalPinCode.setMaxLength(6);
		txtHospitalPinCode.setWidth("180px");
		txtHospitalPinCode.setNullRepresentation("");
		CSValidator pincodevalidator = new CSValidator();
		pincodevalidator.setRegExp("^[0-9/]*$");
		pincodevalidator.setPreventInvalidTyping(true);
		pincodevalidator.extend(txtHospitalPinCode);

		txtHospitalContactNumber = new TextField();
		txtHospitalContactNumber.setCaption("");
		txtHospitalContactNumber.setMaxLength(12);
		txtHospitalContactNumber.setWidth("180px");
		txtHospitalContactNumber.setNullRepresentation("");
		CSValidator hospitalContactNumberValidator = new CSValidator();
		hospitalContactNumberValidator.extend(txtHospitalContactNumber);
		hospitalContactNumberValidator.setRegExp("^[0-9+-]*$");
		hospitalContactNumberValidator.setPreventInvalidTyping(true);
		//
		hospitalPhnSearchBtn = new Button();
		hospitalPhnSearchBtn.setStyleName("link");
		hospitalPhnSearchBtn.setIcon(new ThemeResource("images/search.png"));

		txtHospitalFaxNumber = new TextField();
		txtHospitalFaxNumber.setCaption("Hospital Fax No");
		txtHospitalFaxNumber.setNullRepresentation("");
		txtHospitalFaxNumber.setMaxLength(12);
		txtHospitalFaxNumber.setWidth("180px");
		txtHospitalFaxNumber.setNullRepresentation("");

		txtHospitalMobileNumber = new TextField();
		txtHospitalMobileNumber.setCaption("Mobile No");
		txtHospitalMobileNumber.setNullRepresentation("");
		txtHospitalMobileNumber.setMaxLength(12);
		txtHospitalMobileNumber.setWidth("180px");
		txtHospitalMobileNumber.setNullRepresentation("");

		txtHospitalEmailId = new TextField();
		txtHospitalEmailId.setCaption("Email ID");
		txtHospitalEmailId.setWidth("180px");
		txtHospitalEmailId.setMaxLength(50);
		txtHospitalEmailId.setNullRepresentation("");

		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtHospitalEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);

		claimTypecmb = (ComboBox) binder.buildAndBind("Claim Type", "claimType", ComboBox.class);
		claimTypecmb.setWidth("180px");

		cmbManagementType = (ComboBox) binder.buildAndBind("Management", "managementType", ComboBox.class);
		cmbManagementType.setWidth("180px");
		
		doctorNameTxt = (TextField) binder.buildAndBind("Hospital Doctor Name", "doctorName", TextField.class);
		doctorNameTxt.setWidth("180px");
		doctorNameTxt.setMaxLength(30);
		
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category", "roomCategory", ComboBox.class);
		cmbRoomCategory.setWidth("180px");

		dummyChkBox = (CheckBox) binder.buildAndBind("Dummy", "dummyFlag", CheckBox.class);
		
		commentstextArea = (TextArea) binder.buildAndBind("Comments", "comments", TextArea.class);
		commentstextArea.setCaption("Comments");
		commentstextArea.setWidth("300px");
		commentstextArea.setRows(3);
		commentstextArea.setNullRepresentation("");
		commentstextArea.setMaxLength(4000);
		commentstextArea.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		textAreaPopupShortCutListener(commentstextArea, null);
		
		suspiciousHospCommentsTA = (TextArea) binder.buildAndBind("Suspicious Hospital Comments", "hospitalComments", TextArea.class);
		suspiciousHospCommentsTA.setCaption("Suspicious Hospital Comments");
		suspiciousHospCommentsTA.setWidth("300px");
		suspiciousHospCommentsTA.setRows(3);
		suspiciousHospCommentsTA.setNullRepresentation("");
		suspiciousHospCommentsTA.setMaxLength(4000);
		suspiciousHospCommentsTA.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		textAreaPopupShortCutListener(suspiciousHospCommentsTA, null);
		
		buttonHorizontalLayout = new HorizontalLayout();
		saveBtn = new Button("Save Intimation");
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setEnabled(false);
		submitBtn = new Button("Save & Submit");
		submitBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		resetBtn = new Button("Reset");
		resetBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
	}
	
	private void studentLayout() {
		
		mandatoryFields.remove(txtStudentPatientName);
		mandatoryFields.remove(studentDOB);
		mandatoryFields.remove(txtStudentAge);
		mandatoryFields.remove(parentPatientNameTF);
		mandatoryFields.remove(dateOfBirthDF);
		mandatoryFields.remove(ageTF);
		
		unbindField(txtStudentPatientName);
		unbindField(studentDOB);
		unbindField(txtStudentAge);
		unbindField(parentPatientNameTF);
		unbindField(dateOfBirthDF);
		unbindField(ageTF);
		
		if(txtStudentPatientName != null){
			intimationRightFLayout.removeComponent(txtStudentPatientName);
		}
		if(studentDOB != null){
			studentAgeGrid.removeComponent(studentDOB);
		}
		if(txtStudentAge != null){
			studentAgeGrid.removeComponent(txtStudentAge);
		}
		if(parentPatientNameTF != null){
			intimationRightFLayout.removeComponent(parentPatientNameTF);
		}
		if(dateOfBirthDF != null){
			intimationRightFLayout.removeComponent(dateOfBirthDF);
		}
		
		if(ageTF != null){
			intimationRightFLayout.removeComponent(ageTF);
		}
		
		if(studentAgeGrid != null){
			intimationRightFLayout.removeComponent(studentAgeGrid);
		}
		
		if(parentAgeGrid != null){
			intimationRightFLayout.removeComponent(parentAgeGrid);
		}
		
		txtStudentPatientName = (TextField) binder.buildAndBind("Student Patient Name", "studentPatientName", TextField.class);
		txtStudentPatientName.setNullRepresentation("");
		txtStudentPatientName.setVisible(true);
		txtStudentPatientName.setRequired(true);
		txtStudentPatientName.setMaxLength(500);
		
		studentDOB = (PopupDateField) binder.buildAndBind("", "studentDOB", PopupDateField.class);
		studentDOB.setDateFormat("dd/MM/yyyy");
		studentDOB.setInputPrompt("DD/MM/YYYY");
		studentDOB.setRangeEnd(new Date());
		studentDOB.setConversionError("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		studentDOB.setParseErrorMessage("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		studentDOB.setWidth("130px");
		studentDOB.setTextFieldEnabled(false);
		studentDOB.setVisible(true);
		//studentDOB.setRequired(true);
		
		txtStudentAge = (TextField) binder.buildAndBind("", "studentAge", TextField.class);
		txtStudentAge.setMaxLength(3);
		txtStudentAge.setWidth("40px");
		txtStudentAge.setNullRepresentation("");
		CSValidator ageTFValidator1 = new CSValidator();
		ageTFValidator1.extend(txtStudentAge);
		ageTFValidator1.setRegExp("^[0-9]*$");
		ageTFValidator1.setPreventInvalidTyping(true);			
		txtStudentAge.setVisible(true);
		txtStudentAge.setEnabled(false);
		
		studentDOB.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(studentDOB.getValue() != null) {
					txtStudentAge.setValue(getPersonAge(studentDOB.getValue()));	
				}
				return;
			}
		});
		
		int componentIndex = intimationRightFLayout.getComponentIndex(studentOrParent);
		componentIndex++;
		
		Label lblAge = new Label("Age");
		
		intimationRightFLayout.addComponent(txtStudentPatientName,componentIndex++);
		studentAgeGrid = new GridLayout(3,3);
		
		/*txtStudentAge.setStyleName("txtgridAlignment");
		txtStudentAge.addStyleName("txtgridAlignment");*/
		studentAgeGrid.setCaption("Date Of Birth");
		studentAgeGrid.addComponent(studentDOB,0,0);
		studentAgeGrid.addComponent(lblAge,1,0);
		studentAgeGrid.addComponent(txtStudentAge,2,0);
		
		studentDOB.setStyleName("gridAlignment");
		studentDOB.addStyleName("gridAlignment");
		txtStudentAge.setStyleName("txtgridAlignment");
		txtStudentAge.addStyleName("txtgridAlignment");
		studentAgeGrid.setSpacing(true);
		studentAgeGrid.setComponentAlignment(studentDOB, Alignment.TOP_CENTER);
		studentAgeGrid.setComponentAlignment(txtStudentAge, Alignment.TOP_RIGHT);
		intimationRightFLayout.addComponent(studentAgeGrid,componentIndex++);
		
		
		mandatoryFields.add(txtStudentPatientName);
		//mandatoryFields.add(studentDOB);
		
		showOrHideValidation(false);
		
		
		
		
	}

	private void parentLayout() {
		
		mandatoryFields.remove(txtStudentPatientName);
		mandatoryFields.remove(studentDOB);
		mandatoryFields.remove(parentPatientNameTF);
		mandatoryFields.remove(dateOfBirthDF);
		
		unbindField(txtStudentPatientName);
		unbindField(studentDOB);
		unbindField(txtStudentAge);
		unbindField(parentPatientNameTF);
		unbindField(dateOfBirthDF);
		unbindField(ageTF);
		
		if(txtStudentPatientName != null){
			intimationRightFLayout.removeComponent(txtStudentPatientName);
		}
		if(studentDOB != null){
			studentAgeGrid.removeComponent(studentDOB);
		}
		if(txtStudentAge != null){
			studentAgeGrid.removeComponent(txtStudentAge);
		}
		if(parentPatientNameTF != null){
			intimationRightFLayout.removeComponent(parentPatientNameTF);
		}
		if(dateOfBirthDF != null){
			parentAgeGrid.removeComponent(dateOfBirthDF);
		}
		
		if(ageTF != null){
			parentAgeGrid.removeComponent(ageTF);
		}
		
		if(studentAgeGrid != null){
			intimationRightFLayout.removeComponent(studentAgeGrid);
		}
		
		if(parentAgeGrid != null){
			intimationRightFLayout.removeComponent(parentAgeGrid);
		}
		
		txtStudentPatientName = (TextField) binder.buildAndBind("Student Patient Name", "studentPatientName", TextField.class);
		txtStudentPatientName.setNullRepresentation("");
		txtStudentPatientName.setVisible(true);
		txtStudentPatientName.setRequired(true);
		txtStudentPatientName.setMaxLength(500);
		
		studentDOB = (PopupDateField) binder.buildAndBind("", "studentDOB", PopupDateField.class);
		studentDOB.setDateFormat("dd/MM/yyyy");
		studentDOB.setInputPrompt("DD/MM/YYYY");
		studentDOB.setRangeEnd(new Date());
		studentDOB.setConversionError("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		studentDOB.setParseErrorMessage("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		studentDOB.setWidth("130px");
		studentDOB.setTextFieldEnabled(false);
		studentDOB.setVisible(true);
		//studentDOB.setRequired(true);
		
		studentDOB.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(studentDOB.getValue() != null) {
					txtStudentAge.setValue(getPersonAge(studentDOB.getValue()));	
				}
				return;
			}
		});
		
		txtStudentAge = (TextField) binder.buildAndBind("", "studentAge", TextField.class);
		txtStudentAge.setMaxLength(3);
		txtStudentAge.setWidth("40px");
		txtStudentAge.setNullRepresentation("");
		CSValidator ageTFValidator1 = new CSValidator();
		ageTFValidator1.extend(txtStudentAge);
		ageTFValidator1.setRegExp("^[0-9]*$");
		ageTFValidator1.setPreventInvalidTyping(true);			
		txtStudentAge.setVisible(true);
		txtStudentAge.setEnabled(false);
		
		
		parentPatientNameTF = (TextField) binder.buildAndBind("Parent Patient Name", "paParentName", TextField.class);
		parentPatientNameTF.setNullRepresentation("");
     	parentPatientNameTF.setVisible(true);
     	parentPatientNameTF.setRequired(true);
     	parentPatientNameTF.setMaxLength(500);
		
		dateOfBirthDF = (PopupDateField) binder.buildAndBind("", "parentDOB", PopupDateField.class);
		dateOfBirthDF.setDateFormat("dd/MM/yyyy");
		dateOfBirthDF.setInputPrompt("DD/MM/YYYY");
		dateOfBirthDF.setRangeEnd(new Date());
		dateOfBirthDF.setConversionError("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		dateOfBirthDF.setParseErrorMessage("Invalid date formate please enter a valid date in the format dd/mm/yyyy");
		dateOfBirthDF.setWidth("130px");
		dateOfBirthDF.setTextFieldEnabled(false);
		dateOfBirthDF.setVisible(true);
		//dateOfBirthDF.setRequired(true);
		
		dateOfBirthDF.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(dateOfBirthDF.getValue() != null) {
					ageTF.setValue(getPersonAge(dateOfBirthDF.getValue()));	
				}
				return;
			}
		});
		
		ageTF = (TextField) binder.buildAndBind("", "parentAge", TextField.class);
		ageTF.setMaxLength(3);
		ageTF.setWidth("40px");
		ageTF.setNullRepresentation("");
		CSValidator ageTFValidator = new CSValidator();
		ageTFValidator.extend(ageTF);
		ageTFValidator.setRegExp("^[0-9]*$");
		ageTFValidator.setPreventInvalidTyping(true);			
		ageTF.setEnabled(false);
		
		Label lblParentAge = new Label("Age");
		
		parentAgeGrid = new GridLayout(3,1);
		parentAgeGrid.setCaption("Date Of Birth");
		parentAgeGrid.addComponent(dateOfBirthDF,0,0);
	    parentAgeGrid.addComponent(lblParentAge,1,0);
		parentAgeGrid.addComponent(ageTF,2,0);
		dateOfBirthDF.setStyleName("gridAlignment");
		dateOfBirthDF.addStyleName("gridAlignment");
		ageTF.setStyleName("txtgridAlignment");
		ageTF.addStyleName("txtgridAlignment");
		parentAgeGrid.setSpacing(true);
		parentAgeGrid.setComponentAlignment(dateOfBirthDF, Alignment.TOP_CENTER);
		parentAgeGrid.setComponentAlignment(ageTF, Alignment.TOP_RIGHT);
		
		Label lblStudentAge = new Label("Age");
		studentAgeGrid = new GridLayout(3,1);
		studentAgeGrid.setCaption("Date Of Birth");
		studentAgeGrid.addComponent(studentDOB,0,0);
		studentAgeGrid.addComponent(lblStudentAge,1,0);
		studentAgeGrid.addComponent(txtStudentAge,2,0);
		studentDOB.setStyleName("gridAlignment");
		studentDOB.addStyleName("gridAlignment");
		txtStudentAge.setStyleName("txtgridAlignment");
		txtStudentAge.addStyleName("txtgridAlignment");
		studentAgeGrid.setSpacing(true);
		studentAgeGrid.setComponentAlignment(studentDOB, Alignment.TOP_CENTER);
		studentAgeGrid.setComponentAlignment(txtStudentAge, Alignment.TOP_RIGHT);
		
		int componentIndex = intimationRightFLayout.getComponentIndex(studentOrParent);
		
		componentIndex++;
		
		intimationRightFLayout.addComponent(txtStudentPatientName,componentIndex++);
		intimationRightFLayout.addComponent(studentAgeGrid,componentIndex++);
		intimationRightFLayout.addComponent(parentPatientNameTF,componentIndex++);
		intimationRightFLayout.addComponent(parentAgeGrid,componentIndex++);
		
		mandatoryFields.add(txtStudentPatientName);
		//mandatoryFields.add(studentDOB);
		mandatoryFields.add(parentPatientNameTF);
		//mandatoryFields.add(dateOfBirthDF);

		showOrHideValidation(false);
	}

	public void buildNewBornBabyTable(Boolean isNewBorn,
			Map<String, Object> referenceData) {
		if (isNewBorn && newBabyTableLayout.getComponentCount() == 0) {
			showNewBabyTable();
		} else {
			if (newBabyTableLayout.getComponentCount() > 0) {
				if (newBornBabyTable != null) {
					newBabyTableLayout.removeComponent(this.newBornBabyTable);
					this.newBornBabyTable = null;
				}
			}
		}
	}
	
	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}

	private void showNewBabyTable() {
		newBornBabyTable = newBornBabyTableInstance.get();
		newBornBabyTable.init("", true);
		newBornBabyTable.setReference(referenceData);
		newBornBabyTable.setEditable(true);
		newBabyTableLayout.addComponent(newBornBabyTable);
	}

	private void buildLateIntimationComponents() {
		dateOfDischargeDF.setVisible(true);
		
		inpatientNumbertextField.setVisible(true);
		inpatientNumbertextField.setEnabled(true);
		reasonforlateintimationTxta.setVisible(true);
		reasonforlateintimationTxta.setEnabled(true);
		if (inpatientNumbertextField != null) {

			inpatientNumbertextField.setMaxLength(15);
		}
		if (reasonforlateintimationTxta != null) {

			reasonforlateintimationTxta.setMaxLength(200);
		}
		//mandatoryFields.add(inpatientNumbertextField);
		mandatoryFields.add(reasonforlateintimationTxta);
		showOrHideValidation(false);
	}

	private void removeLateIntimationComponents() {
		dateOfDischargeDF.setVisible(false);
		dateOfDischargeDF.setValue(null);
		
		if (!ValidatorUtils.isNull(inpatientNumbertextField)) {
			mandatoryFields.remove(inpatientNumbertextField);
			ValidatorUtils.removeValidator(inpatientNumbertextField);
			unbindField(binder.getField("inpatientNumber"));
			inpatientNumbertextField.setValue(null);
			inpatientNumbertextField.setEnabled(false);
			inpatientNumbertextField.setVisible(false);
		}

		if (!ValidatorUtils.isNull(reasonforlateintimationTxta)) {
			mandatoryFields.remove(reasonforlateintimationTxta);
			ValidatorUtils.removeValidator(reasonforlateintimationTxta);
			unbindField(reasonforlateintimationTxta);
			reasonforlateintimationTxta.setValue(null);
			reasonforlateintimationTxta.setEnabled(false);
			reasonforlateintimationTxta.setVisible(false);
		}
	}

	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	public void callUpdateHospital(UpdateHospitalDTO hospitalDTO) {
		if (popupWindow != null) {
			popupWindow.close();
		}
		fireViewEvent(IntimationDetailsPresenter.UPDATE_SUBMIT_EVENT, hospitalDTO);
	}

	public void cancelUpdateHospital() {
		if (popupWindow != null) {
			popupWindow.close();
		}
	}

	public void searchHospitalIdClick(HospitalDto hospitalDTO) {
		fireViewEvent(IntimationDetailsPresenter.SEARCH_HOSPITAL_BY_ID,
				hospitalDTO);

	}

	public void searchHospitalNameClick(HospitalDto hospitalDTO) {
		fireViewEvent(IntimationDetailsPresenter.SEARCH_HOSPITAL_BY_NAME,
				hospitalDTO);

	}

	public void searchHospitalNameKey(HospitalDto hospitalDTO) {
		fireViewEvent(IntimationDetailsPresenter.SEARCH_HOSPITAL_KEY,
				hospitalDTO);
		this.bean.setHospitalDto(hospitalDTO);
		Collection<?> claimTypeIds = claimTypecmb.getContainerDataSource()
				.getItemIds();
		if (this.bean.getHospitalDto().getRegistedHospitals() != null) {
			if (this.bean.getHospitalDto().getRegistedHospitals()
					.getHospitalType().getValue().equalsIgnoreCase("network") && networkHospitalTypeTxt != null) {
				networkHospitalTypeTxt.setValue(this.bean.getHospitalDto()
						.getRegistedHospitals().getNetworkHospitalType());

				claimTypecmb.setValue(claimTypeIds.toArray()[0]);
				claimTypecmb.setEnabled(true);
			} else {
				claimTypecmb.setValue(claimTypeIds.toArray()[3]);
				claimTypecmb.setEnabled(false);
			}
		}

		try {
			this.bean.getHospitalDto().setRegistedHospitals(
					new HospitalMapper().getHospital(hospitalDTO));
		} catch (Exception e) {

		}
	}

	public void selectedInsured(
			@Observes @CDIEvent(INSURED_SELECTED) final ParameterDTO parameters) {
		Insured insured = (Insured) parameters.getPrimaryParameter();
		cmbInsuredPatient.setValue(insured);

		Collection<Window> windows = UI.getCurrent().getWindows();
		for (Window window : windows) {
			window.close();
		}
		
	}
	
	

	// TODO TokenField
	private void setReasonForAdmissionToBean() {
		this.bean.setReasonForAdmission(txtReasonForAdmission.getValueStr());
	}

	private void setClaimTypeToBean() {
		SelectValue clmType = (SelectValue) claimTypecmb.getValue();
		this.bean.setClaimType(clmType);
	}

	public void setHospitalContentEditable(boolean isEditable) {
		setHospitalFieldsEditable(isEditable);
	}

	public void setIntimationContentEditable(boolean isEditable) {

		cmbModeOfIntimation.setEnabled(isEditable);
		cmbIntimatedBy.setEnabled(isEditable);

		intimatorNameTxt.setEnabled(isEditable);
		callerContactNumberTxt.setEnabled(isEditable);
		//callerLandlineNumberTxt.setEnabled(isEditable);
		attenderContactNumberTxt.setEnabled(isEditable);
		//attenderLandlineNumberTxt.setEnabled(isEditable);
		if(cmbInsuredPatient != null){
			cmbInsuredPatient.setEnabled(isEditable);
		}
		insuredSearchBtn.setEnabled(isEditable);
		chkPatientNotCovered.setEnabled(isEditable);
		if (newBornBabyTable != null) {
			newBornBabyTable.setEnabled(isEditable);
		}
		admissionDateField.setEnabled(isEditable);
		cmbRelapseofIllness.setEnabled(isEditable);
		cmbAdmissionType.setEnabled(isEditable);

		// if(inpatientNumbertextField != null &&
		// inpatientNumbertextField.isVisible() && bean.getAdmissionType() !=
		// null &&
		// StringUtils.containsIgnoreCase(bean.getAdmissionType().getValue(),
		// "late")){
		// inpatientNumbertextField.setEnabled(isEditable);
		// }
		// if(reasonforlateintimationTxta != null &&
		// reasonforlateintimationTxta.isVisible() && bean.getAdmissionType() !=
		// null &&
		// StringUtils.containsIgnoreCase(bean.getAdmissionType().getValue(),
		// "late")){
		// reasonforlateintimationTxta.setEnabled(isEditable);
		// }

		if (bean.getAdmissionType() != null
				&& StringUtils.containsIgnoreCase(bean.getAdmissionType()
						.getValue(), "late")) {
			inpatientNumbertextField.setEnabled(isEditable);
		}
		if (bean.getAdmissionType() != null
				&& StringUtils.containsIgnoreCase(bean.getAdmissionType()
						.getValue(), "late")) {
			reasonforlateintimationTxta.setEnabled(isEditable);
		}

		txtReasonForAdmission.setEnabled(isEditable);
		claimTypecmb.setEnabled(isEditable);
		cmbManagementType.setEnabled(isEditable);
		cmbRoomCategory.setEnabled(isEditable);
		doctorNameTxt.setEnabled(isEditable);
		commentstextArea.setEnabled(isEditable);

	}

	public void setReasonforAdmission(Map<String, Object> references) {
		this.referenceData.put("reasonForAdmission",
				references.get("reasonForAdmission"));
	}

	// private static final String[] diseases = new String[] { "Argyria",
	// "Arthritis", "Aseptic meningitis", "Asthenia", "Asthma",
	// "Astigmatism", "Atherosclerosis", "Athetosis", "Atrophy",
	// "Calculi", "Campylobacter infection", "Cancer", "Candidiasis",
	// "Carbon monoxide poisoning", "Celiacs disease", "Cerebral palsy",
	// "Chagas disease", "Chalazion", "Chancroid", "Dengue",
	// "Diabetes mellitus", "Diphtheria", "Dehydration", "Ear infection",
	// "Ebola", "Encephalitis", "Emphysema", "Epilepsy",
	// "Erectile dysfunctions" };

	/*
	 * private BeanItemContainer<SelectValue> generateTestContainer() {
	 * BeanItemContainer<SelectValue> container = new
	 * BeanItemContainer<SelectValue>( SelectValue.class);
	 * 
	 * HashSet<String> log = new HashSet<String>(); for (int i = 0; i <
	 * diseases.length;) { String name = diseases[i];
	 * 
	 * if (!log.contains(name)) { log.add(name); container.addBean(new
	 * SelectValue(Long.valueOf("" + i), name)); i++; } } return container; }
	 */

	// TODO tokenfield
	public void addTokenValue(SelectValue selectValue) {
		txtReasonForAdmission.addToken(selectValue);
	}

	@Override
	public void newValueAdded(Object token) {
		fireViewEvent(IntimationDetailsPresenter.ADD_REASON_FOR_ADMISSION,
				token);
	}

	// TODO token field
	@Override
	public void tokenClicked(Object tokenId) {

		String tokenValuesAsStr = txtReasonForAdmission.getValueStr();
		System.out.println(tokenValuesAsStr);
	}

	public void setReasonForAdmissionValues(
			BeanItemContainer<SelectValue> selectValue) {
		reasonForAdmissionContainer = selectValue;
		txtReasonForAdmission.setContainer(reasonForAdmissionContainer);
	}

	private Boolean isValidEmail(String strEmail) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	
	public String getPersonAge(Date birthDate) {
		// convert date to stringDateFormat MM-dd-yyyy
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		// convert simpleDateFormat to string
		String dateStr = formatter.format(birthDate);
		System.out.println("date String : " + dateStr);
		 
		Calendar birth = new GregorianCalendar();
		Calendar today = new GregorianCalendar();
		int age = 0;
		int factor = 0; 
		//calculate age
		try {
			Date birthDt = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			Date currentDate = new Date(); //current date
		 
			birth.setTime(birthDt);
			today.setTime(currentDate);
		 
			// check if birthday has been celebrated this year
			if(today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
				factor = -1;
				//birthday not celebrated
			}
			age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor;
		 } catch (ParseException e) {
		    e.printStackTrace();
		 } 
		 return String.valueOf(age);
	}
	
	public  void textAreaPopupShortCutListener(TextArea textArea, final  Listener listener) {
	    @SuppressWarnings("unused")
		ShortcutListener enterShortCut = new ShortcutListener("TextAreaPopupShortCut", ShortcutAction.KeyCode.F8, null) {
	    	private static final long serialVersionUID = -2267576464623389044L;
	    	@Override
	    	public void handleAction(Object sender, Object target) {
	    		((ShortcutListener) listener).handleAction(sender, target);
	    	}
	    };	  
	    handleShortcut(textArea, getTextAreaPopupShortCutListener(textArea));
	}
	
	public  void handleShortcut(final TextArea textArea, final ShortcutListener shortcutListener) {	
		textArea.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {				
				textArea.addShortcutListener(shortcutListener);
			}
		});
		textArea.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void blur(BlurEvent event) {			
				textArea.removeShortcutListener(shortcutListener);		
			}
		});
	}
	
	private ShortcutListener getTextAreaPopupShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("TextAreaPopupShortCut", KeyCodes.KEY_F8,null) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(textAreaField.getMaxLength());
				txtArea.setData(bean);
				txtArea.setValue(textAreaField.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption(textAreaField.getCaption());
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(textAreaField);
				
				dialog.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}
	
	
	public void setValuesForInsuredTable(Boolean isPA){
		
		Policy apolicy = policyService.getPolicy(bean.getPolicySummary().getPolicyNo());
		buildDateOfBirth();
		buildPreExistingDisease();
		
		BeanItemContainer<Insured> insuredContainer = new BeanItemContainer<Insured>(
				Insured.class);
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){/*
			
			Policy policyKey = policyService.getKeyByPolicyNumber(bean.getPolicySummary().getPolicyNo());
			List<Insured> insuredGMC = intimationService.getInsuredGMC(policyKey.getKey());
			List<Insured> insuredListGMC = new ArrayList<Insured>();
			if(insuredGMC != null){
				
				for (Insured insured : insuredGMC) {
					insuredListGMC.add(insured);
				}
			}
			insuredContainer.addAll(insuredListGMC);
			
			if(insuredGMC != null){
				List<Insured> insuredList = new ArrayList<Insured>();
				if(insuredGMC.getDependentRiskId() != null){
					insuredList = policyService.getInsuredListForGMC(insuredGMC.getDependentRiskId());	
				}else{
					insuredList = policyService.getInsuredListForGMC(insuredGMC.getInsuredId());	
				}
				insuredContainer.addAll(insuredList);
			}else{
				insuredContainer.addAll(apolicy.getInsured());
			}
			if(bean.getPolicy() != null && bean.getPolicy().getInsured() != null) {
				insuredContainer.addAll(bean.getPolicy().getInsured());
			}
			
		*/}else if (bean.getPolicy() != null && bean.getPolicy().getInsured() != null){
			insuredContainer.addAll(bean.getPolicy().getInsured());	
		}
		BeanItemContainer<GmcMainMemberList> insuredContainerFOrGMC = new BeanItemContainer<GmcMainMemberList>(
				GmcMainMemberList.class);
	
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))) {
		insuredTable.setContainerDataSource(insuredContainerFOrGMC);
		}else{
			if (bean.getPolicy() != null && bean.getPolicy().getInsured() != null){
				insuredContainer.addAll(bean.getPolicy().getInsured());	
			insuredTable.setContainerDataSource(insuredContainer);
		}
		}
		if(SHAConstants.PACKAGE_PRODUCT.equals(bean.getPolicySummary().getProduct().getIntimationType())){
			BeanItemContainer<Insured> healthPainsuredContainer = new BeanItemContainer<Insured>(
					Insured.class);
			List<Insured> insuredList = bean.getPolicy().getInsured();
			if(isPA){
				List<Insured> paInsuredList = new ArrayList<Insured>();
				for (Insured insured : insuredList) {
					if(insured.getLopFlag() != null && insured.getLopFlag().equalsIgnoreCase("P")){
						paInsuredList.add(insured);
					}
				}
				healthPainsuredContainer.addAll(paInsuredList);
			} else {
				List<Insured> paInsuredList = new ArrayList<Insured>();
				for (Insured insured : insuredList) {
					if(insured.getLopFlag() != null && insured.getLopFlag().equalsIgnoreCase("H")){
						paInsuredList.add(insured);
					}
				}
				healthPainsuredContainer.addAll(paInsuredList);
			}
			insuredTable.setContainerDataSource(healthPainsuredContainer);
		} else {
			insuredTable.setContainerDataSource(insuredContainer);
		}
		
		
		Object[] columnsForGMC = new Object[] {"insuredName", "insuredAge", "effectiveFromDate", "effectiveToDate"  };
		
		Object[] columns = new Object[] { "insuredName", "insuredGender",
				"DATE OF BIRTH", "insuredAge", "relationshipwithInsuredId",
				"insuredSumInsured", "PRE-EXISTING DISEASE" };
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			insuredTable.setVisibleColumns(columnsForGMC);
		}else{
			insuredTable.setVisibleColumns(columns);	
		}
		
		insuredTable.setColumnHeader("insuredName", "INSURED");
		insuredTable.setColumnHeader("insuredGender", "SEX");
		insuredTable.setColumnHeader("relationshipwithInsuredId", "RELATION");
		insuredTable.setColumnHeader("insuredAge", "AGE IN YRS");
		insuredTable.setColumnHeader("insuredSumInsured", "SUM INSURED");
		insuredTable.setColumnHeader("PRE-EXISTING DISEASE", "Risk PED/Portal PED");
		insuredTable.setColumnHeader("effectiveFromDate", "Effective From");
		insuredTable.setColumnHeader("effectiveToDate", "Effective To");
		
	}
	
	public void setValuesForCoversTable(){
		
		BeanItemContainer<PolicyCoverDetails> policyCoverContainer = new BeanItemContainer<PolicyCoverDetails>(
				PolicyCoverDetails.class);
		if(bean.getPolicyCoverDetails() != null && ! bean.getPolicyCoverDetails().isEmpty()){
			policyCoverContainer.addAll(bean.getPolicyCoverDetails());	
		}
		coverDetailsTable.setContainerDataSource(policyCoverContainer);
		
		Object[] columns = new Object[] {"coverCode", "coverCodeDescription",
				"sumInsured"};
		
		
		coverDetailsTable.setVisibleColumns(columns);	
		
		coverDetailsTable.setColumnHeader("coverCode", "Cover Code");
		coverDetailsTable.setColumnHeader("coverCodeDescription", "Cover Description");
		coverDetailsTable.setColumnHeader("sumInsured", "Sum Insured");
		
	}
	
	private void buildPreExistingDisease() {
		insuredTable.removeGeneratedColumn("PRE-EXISTING DISEASE");
		insuredTable.addGeneratedColumn("PRE-EXISTING DISEASE",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = -1827025991172604071L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
			if(!(bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
	|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
	|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
	|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))) {
						Insured insured = (Insured) itemId;
						if (insured != null) {
							List<InsuredPedDetails> insuredPedDetailsList = policyService
									.getPEDByInsured(insured.getInsuredId());

							if (insuredPedDetailsList != null && !insuredPedDetailsList.isEmpty()) {
								insured.setInsuredPedList(insuredPedDetailsList);
							}

						}
						return insured.getInsuredPedNames();
			}
			return null;
					}
				});
	}

	private void buildDateOfBirth() {
		insuredTable.removeGeneratedColumn("DATE OF BIRTH");
		insuredTable.addGeneratedColumn("DATE OF BIRTH",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = -1827025991172604071L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						if(!(bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))) {
						Insured insured = (Insured) itemId;

						try {
							return SHAUtils.formatDate(insured
									.getInsuredDateOfBirth());

						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						return null;
						}
					
					return null;
					}
				});
	}

	public void setGmcInsuredDetails(NewIntimationDto bean2) {
		BeanItemContainer<GmcMainMemberList> insuredContainerFOrGMC = new BeanItemContainer<GmcMainMemberList>(
				GmcMainMemberList.class);
 		if(bean2.getGmcInsuredList() != null && ! bean2.getGmcInsuredList().isEmpty()){
		
			BeanItemContainer<GmcMainMemberList> beanItemContainer = new BeanItemContainer<GmcMainMemberList>(GmcMainMemberList.class);
			List<GmcMainMemberList> gmcInsuredList = bean2.getGmcInsuredList();
			
				beanItemContainer.addAll(gmcInsuredList);
				tmpMainMemberNameCB2.setContainerDataSource(beanItemContainer);
				tmpMainMemberNameCB2.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				tmpMainMemberNameCB2.setItemCaptionPropertyId("insuredName");
				
				GmcMainMemberList gmcMainMemberList = gmcInsuredList.get(0);
				tmpMainMemberNameCB.setValue(gmcMainMemberList.getMainMemberName());
				bean.setHealthCardNo(gmcMainMemberList.getIdCardNumber());
	  }
		
		Collection<Window> windows = UI.getCurrent().getWindows();
		for (Window window : windows) {
			window.close();
		}
		
		//IMSSUPPOR-27773
		if(bean.getSelectedGmcInsuredList() != null && bean.getSelectedGmcInsuredList().getRecType() != null && 
				bean.getSelectedGmcInsuredList().getRecType().equalsIgnoreCase("D")){
			showErrorMessage("<b style = 'color: red;'>  Selected risk is deleted from policy :  </b>"+bean.getSelectedGmcInsuredList().getInsuredName());
		}
		/*buildDateOfBirth();
		buildPreExistingDisease();*/
		Policy apolicy = policyService.getPolicy(bean.getPolicySummary().getPolicyNo());
		BeanItemContainer<Insured> insuredContainer = new BeanItemContainer<Insured>(
				Insured.class);
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			
			List<GmcMainMemberList> insuredList = new ArrayList<GmcMainMemberList>();
			if( bean2.getGmcInsuredList().get(0).getRiskId() != null){
			GmcMainMemberList mainMemberList = 	intimationService.getMemberId(bean2.getGmcInsuredList().get(0).getRiskId(),bean2.getPolicySummary().getPolicyNo());
			insuredList = intimationService.getListByMemberId(mainMemberList.getMemberId(),bean2.getPolicySummary().getPolicyNo());
			}
			List<GmcMainMemberList> insuredListGMC = new ArrayList<GmcMainMemberList>();
			if(insuredList != null){
				
				for (GmcMainMemberList insured : insuredList) {
					insuredListGMC.add(insured);
				}
			}
			insuredContainerFOrGMC.addAll(insuredListGMC);
			
			
		}
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))) {
		insuredTable.setContainerDataSource(insuredContainerFOrGMC);
		}else{
			insuredTable.setContainerDataSource(insuredContainer);
		}
		Object[] columnsForGMC = new Object[] { "insuredName", "age", "effectiveFromDate", "effectiveToDate" };
		
		Object[] columns = new Object[] { "insuredName", "insuredGender",
				"DATE OF BIRTH", "insuredAge", "relationshipwithInsuredId",
				"insuredSumInsured", "PRE-EXISTING DISEASE" };
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			insuredTable.setVisibleColumns(columnsForGMC);
		}else{
			insuredTable.setVisibleColumns(columns);	
		}
	}
	
   public void getViewDocumentByPolicyNo(String healthCardNumber, String policyNo, String riskID) {
		VerticalLayout vLayout = new VerticalLayout();
		
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		String strDmsViewURL = null;
				
		if(policyNo != null){
			policyObj = policyService.getByPolicyNumber(policyNo);
			if (policyObj != null) {
				if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
					strDmsViewURL = BPMClientContext.BANCS_HEALTH_CARD_DOCUMENT_VIEW_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", policyNo);
					strDmsViewURL = strDmsViewURL.replace("MEMBER", riskID!=null?riskID:"");					
				}else{
					strDmsViewURL = PremiaService.getHealthCardViewDetails(healthCardNumber);
				}
			}
		}
		
		
		//Bancs Changes End
		//String strDmsViewURL = PremiaService.getHealthCardViewDetails(healthCardNumber);

		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL));
		browserFrame.setWidth("100%");
		browserFrame.setHeight("200%");
		//browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("60%");
		//popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		//popup.center();
		popup.setResizable(false);
		
		popup.setPositionX(200);
		popup.setPositionY(30);
		popup.setHeight("400px");
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			//**
			  
			 //*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		//popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
   
	public void alertMessage(String intimationNo) {
		

   		Label successLabel = new Label(
				"<b style = 'color: red;'> Same Details exists in the Intimation - </b>"+"<b style = 'color: red; font-weight: bold;'> "+intimationNo+"</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
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
	
	 public void getErrorMessage(String eMsg){

			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
    }
	 
		private void patientNameChangedBasedOnInsuredType() {
			if(insuredTypeCB != null && insuredTypeCB.getValue() != null) {
				SelectValue insuredTypeSV = (SelectValue) insuredTypeCB.getValue();
				List<Insured> itemIds = null;
				if(cmbInsuredPatient.getData() != null){
					BeanItemContainer<Insured> data = (BeanItemContainer<Insured>)cmbInsuredPatient.getData();
					itemIds = data.getItemIds();
				}
				
				BeanItemContainer<Insured> filerContainer = new BeanItemContainer<Insured>(Insured.class);
				
				if((insuredTypeSV != null) && (insuredTypeSV.getId().equals(10000L))) {
					accidentDeathOG.setVisible(true);
					accidentDeathOG.setValue("Accident");
					
					if(itemIds != null){
						for (Insured insured : itemIds) {
							if((insured.getLopFlag() != null && insured.getLopFlag().equalsIgnoreCase("P"))){
								filerContainer.addBean(insured);
							}
						}
					}
				} else {
					accidentDeathOG.setValue(null);
					accidentDeathOG.setVisible(false);
						if(itemIds != null){
							for (Insured insured : itemIds) {
								if(insured.getLopFlag() == null || (insured.getLopFlag() != null && insured.getLopFlag().equalsIgnoreCase("H"))){
									filerContainer.addBean(insured);
								}
							}
						}
				}	
				cmbInsuredPatient.setContainerDataSource(filerContainer);
				cmbInsuredPatient.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbInsuredPatient.setItemCaptionPropertyId("insuredName");
				cmbInsuredPatient.setNullSelectionAllowed(false);
				
				if(filerContainer.getItemIds() != null && ! filerContainer.getItemIds().isEmpty()){
					cmbInsuredPatient.setValue(filerContainer.getIdByIndex(0));
				}
				
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
	
}
