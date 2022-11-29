package com.shaic.claim.OMPProcessOmpClaimApprover.pages;

import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreateIntimationWizardViewImpl;
import com.shaic.claim.OMPViewDetails.view.OMPViewCurrentPolicyDetailsUI;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPPreviousClaimWindowUI;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimWindowOpen;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimsTable;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationAddHospital;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPViewClaimantDetailsPageUI;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.themes.Reindeer;

public class OMPNewClaimApproverPageUI extends ViewComponent{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private OMPClaimProcessorDTO bean;
	

	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private VerticalLayout mainPanel;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	@Inject
	private OMPCreateIntimationWizardViewImpl intimationUI = new OMPCreateIntimationWizardViewImpl();
	
	@EJB
	private IntimationService intimService;
	
	private BeanFieldGroup<OMPClaimProcessorDTO> binder;
	
	private ComboBox cmbEventCode;
	
	private DateField admissionDate;
	
	private DateField dateofDischarge;
	
	private DateField lossDateField;
	
	private TextField txtAilmentLoss;
	
	private TextField txtplaceofvisit;
	
	private TextField txtPlaceOfAccident;
	
	private OptionGroup claimTypeOption;
	
	private OptionGroup hospitalOption;
	
	private TextField txtCity;
	
	private ComboBox cmbCountry;
	
	private TextField txtAvaiSi;
	
	private TextField txtInitialProvisionAmt;
	
	private TextField txtINRConversionRate;
	
	private TextField txtINRValue;
	
	private DateField lossTime;
	
	private TextField txtPlaceOfLossOrDelay;
	
	private TextField txtlossDetails;
	
	private Button addHospital;
	
	private ComboBox cmbHospitalCodeOrName;
	
	private Window addHospitalpopup;
	
	public TextField dummyTextField;
	@Inject
	private ViewDetails viewDetails;
	
	private Button riskButton;
	
	private Button balanceSi;
	
	private Button policyDetails;
	
	private Button vb64Button;
	
	private Button viewMedButton;
	
	private Button previousClaimButton;
	
	private Button btnClose;
	
	private Button btnReOpen;
	
	private CheckBox chkOpenionTaken;
	
	private Button btnSave;
	
	private Button btnCancel;
	
	private VerticalLayout  mainLayout;
	
	private TextArea txtClosedRemarks;
	
	private ComboBox cmbReopenRemarks;
	
	@Inject
	private OMPCreateIntimationAddHospital addHospitalLayoutContent;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@Inject
	private Instance<OMPNewCalculationTableForApprover> ompClaimCalcViewTableInstance;
	
	private OMPNewCalculationTableForApprover ompClaimCalcViewTableObj;
	
	@Inject
	private OMPViewCurrentPolicyDetailsUI ompViewCurrentPolicyUI;
	
	@EJB
	private PolicyService policyService;
	
	@Inject
	private Instance<ViewOMPPreviousClaimWindowOpen> viewOMPPreviousClaimWindowOpen;
	
	@Inject
	private ViewOMPPreviousClaimsTable ompPreviousClaimsTable;
	
	@Inject
	private OMPViewClaimantDetailsPageUI ompClaimantUI;
	
	private TextArea processorRemarks;
	
	private TextArea reasonForApproval;
	
	private ComboBox reasonForRejection;
	
	private ComboBox patientStatus;
	
	private OptionGroup CGOption;
	private DateField dateofCG;
	private TextField txtCGApprovedAmt;
	private TextArea txtCGRemarks;
	
	private DateField deathDate;

	private Button earlierAcknowledgementDetailsButton;
	private Button acknowledgementDetailsButton;
	
	private boolean isTPAUserLogin;

	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	public void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country){
		this.bean = bean;
		
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Process OMP Claim -Approver");
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(intimationDetailsCarousel);
		mainPanel.addComponent(viewDetailsButtonLayout());
		HorizontalLayout dummyhLayout = new HorizontalLayout();
		dummyhLayout.setSpacing(true);
		dummyhLayout.setMargin(true);
		mainPanel.addComponent(commonButtonsLayout(eventCode, country));
		mainPanel.setSizeFull();
		if(bean.getIsMutipleRod()){
			cmbEventCode.setReadOnly(Boolean.TRUE);	
			claimTypeOption.setEnabled(Boolean.FALSE);
		}
		addListenerForEventCode();
		setCompositionRoot(mainPanel);
		
	}
	
	private VerticalLayout viewDetailsButtonLayout() {
		
		 riskButton = new Button("View Risk Details");
		riskButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
					viewDetails.getViewOmpRiskDetails(bean.getNewIntimationDto().getPolicy(),bean.getIntimationId());
				}
			}
		});
		
		 balanceSi = new Button("View Balance SI");
		 if(isTPAUserLogin){
			 balanceSi.setEnabled(Boolean.FALSE); 
		 }
		 balanceSi.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					viewDetails.getViewBalanceSumInsured(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		
		 policyDetails = new Button("View Policy Details");
		 policyDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null){
					Policy policy = bean.getNewIntimationDto().getPolicy();
					ompViewCurrentPolicyUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
					ompViewCurrentPolicyUI.initView();
					UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
				}
			}
			
		});
		
		 
		 vb64Button = new Button("View 64VB");
		 if(isTPAUserLogin){
			 vb64Button.setEnabled(Boolean.FALSE); 
		 }
		 vb64Button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getIrda64VbDetails(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 viewMedButton = new Button("View MER Details");
		 if(isTPAUserLogin){
			 viewMedButton.setEnabled(Boolean.FALSE); 
		 }
		 viewMedButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					String merUrl = viewDetails.getOMPMERDetails(bean.getIntimationId());
					
					if(merUrl != null && !merUrl.isEmpty()){
						getUI().getPage().open(merUrl, "_blank",1500,800,BorderStyle.NONE);
					}
				}
//				showErrorMessage("Intimation not available");
			}
			
		});
		 
		 previousClaimButton = new Button("View Deductibles");
		 if(isTPAUserLogin){
			 previousClaimButton.setEnabled(Boolean.FALSE); 
		 }
		 previousClaimButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					viewDetails.getDeductibles(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewHistoryButton = new Button("View Claim History");
		 viewHistoryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getOmpViewClaimHistory(bean.getIntimationId());
				}
			}
			
		});
		 
		 final Button viewPreviousPolicyClaimButton = new Button("Prev Claim Details");
		 if(isTPAUserLogin){
			 viewPreviousPolicyClaimButton.setEnabled(Boolean.FALSE); 
		 }
		 if(bean.getIntimationId()!= null){
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,bean.getIntimationId());
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
				
		}
		 BrowserWindowOpener opener = new BrowserWindowOpener(OMPPreviousClaimWindowUI.class);
		 	opener.setFeatures("height=700,width=1300,resizable");
		 	opener.setWindowName("_blank");
			opener.extend(viewPreviousPolicyClaimButton);
		 viewPreviousPolicyClaimButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,bean.getIntimationId());
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
			
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewClaimantButton = new Button("View Claimant Details");
		 viewClaimantButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Window popup =null;
				popup = new com.vaadin.ui.Window();
				popup.setWidth("800px");
				popup.setHeight("280px");
				popup.setCaption("View Claimant Details");
				Policy policy = bean.getNewIntimationDto().getPolicy();
				ompClaimantUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
				Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
				ompClaimantUI.init(apolicy);
				popup.setContent(ompClaimantUI);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
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
		 
		 Button viewDocButton = new Button("View Documents");
		 viewDocButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					Long dummyno = 1l;
					String dummystrin = "";
					Map<String,String> tokenInputs = new HashMap<String, String>();
					 tokenInputs.put("intimationNo", bean.getIntimationId());
					 tokenInputs.put("ompdoc", dummyno.toString());
					 String intimationNoToken = null;
					  try {
						  intimationNoToken = intimService.createJWTTokenForClaimStatusPages(tokenInputs);
					  } catch (NoSuchAlgorithmException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (ParseException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					tokenInputs = null;  
					String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
					/*Below code commented for security reason
					String url = bpmClientContext.getGalaxyDMSUrl() + bean.getIntimationId() + "&&ompdoc?" + dummyno;*/
//					String url = bpmClientContext.getGalaxyDMSUrl() + bean.getIntimationId();
				//	getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
//					viewDetails.getViewDocumentByPolicyNo(dtoBean.getIntimationno());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewOtherCurrency = new Button("View Other Currency Rate");
		 if(isTPAUserLogin){
			 viewOtherCurrency.setEnabled(Boolean.FALSE); 
		 }
		 viewOtherCurrency.addClickListener(new ClickListener() {
			 private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getviewOtherCurrencyRateDetails(bean.getIntimationId(),bean.getRodKey());
				}
			}
	});
		 
		 earlierAcknowledgementDetailsButton = new Button("Earlier Acknowledgement Details");
			earlierAcknowledgementDetailsButton.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
						viewDetails.getEarlierAcknowledgementDetailsButton(bean.getIntimationId());
					}
				}
			});
			
			acknowledgementDetailsButton = new Button("View Acknowledgement Details");
			acknowledgementDetailsButton.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
						viewDetails.getAcknowledgementDetailsButton(bean.getIntimationId());
					}
				}
			});
		 
		 HorizontalLayout horizontalLayout = new HorizontalLayout(acknowledgementDetailsButton,earlierAcknowledgementDetailsButton,riskButton,balanceSi,policyDetails,vb64Button,viewMedButton);
		 HorizontalLayout horizontalLayout2 = new HorizontalLayout(previousClaimButton,viewHistoryButton, viewPreviousPolicyClaimButton,viewClaimantButton,viewOtherCurrency,viewDocButton);
			VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout,horizontalLayout2);
			verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
			verticalLayout.setComponentAlignment(horizontalLayout2, Alignment.TOP_RIGHT);
			verticalLayout.setSpacing(true);
			verticalLayout.setWidth("100%");
			verticalLayout.setHeight("95px");
			return verticalLayout;
}

	public void initBinder() {
		this.binder = new BeanFieldGroup<OMPClaimProcessorDTO>(
				OMPClaimProcessorDTO.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	@SuppressWarnings("serial")
	private VerticalLayout commonButtonsLayout(BeanItemContainer<SelectValue> eventCode,BeanItemContainer<SelectValue> country){
		initBinder();
		FormLayout emptyFormLayoutOne = new FormLayout(new Label());
		emptyFormLayoutOne.setSizeFull();
		FormLayout emptyFormLayoutTwo = new FormLayout(new Label());	
		emptyFormLayoutTwo.setSizeFull();
//		claimTypeOption = new OptionGroup();
		claimTypeOption = binder.buildAndBind("","claimType",OptionGroup.class);
		claimTypeOption.setNullSelectionAllowed(true);
		claimTypeOption.addItems(getReadioButtonOptions());
		claimTypeOption.setItemCaption(true, "Cashless");
		claimTypeOption.setItemCaption(false, "Reimbursement");
		claimTypeOption.setStyleName("horizontal");
		hospitalOption = new OptionGroup();
//		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
		hospitalOption.addItem("Non Medical");
		if(bean.getNonHospitalisationFlag()!= null && bean.getNonHospitalisationFlag().equalsIgnoreCase("Y")){
			hospitalOption.setValue("Non Medical");
		}
		if(String.valueOf(hospitalOption.getValue()).equals("Non Medical")){
			this.bean.setNonHospitalisationFlag("Y");
		}else{
			this.bean.setNonHospitalisationFlag("N");
		}
		if(claimTypeOption!= null && claimTypeOption.getValue()!= null&& claimTypeOption.getValue().equals(Boolean.TRUE)){
			this.bean.setIsCashless(Boolean.TRUE);
		}else{
			this.bean.setIsCashless(Boolean.FALSE);
		}
	/*	hospitalOption.setItemCaption(true, "Non Medical");
		hospitalOption.setItemCaption(false, "");
		hospitalOption.setStyleName("horizontal");*/
		
		cmbEventCode = binder.buildAndBind("Event Code" , "eventCode", ComboBox.class);
		cmbEventCode.setContainerDataSource(eventCode);
		cmbEventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEventCode.setItemCaptionPropertyId("value");
		cmbEventCode.setValue(bean.getEventCode());
		cmbEventCode.setEnabled(Boolean.FALSE);
		admissionDate = new DateField("Admission Date");
		admissionDate.setValue(bean.getAdmissionDate());
		admissionDate.setDateFormat("dd-MM-yyyy");
//		admissionDate = binder.buildAndBind("Admission Date","admissionDate",DateField.class);
		admissionDate.setRequired(true);
		dateofDischarge = binder.buildAndBind("Discharge Date","dischargeDate",DateField.class);
		dateofDischarge.setDateFormat("dd-MM-yyyy");
		txtAilmentLoss= binder.buildAndBind("Ailment Details" , "ailmentLoss", TextField.class);
		txtAilmentLoss.setNullRepresentation("");
//		txtPlaceOfAccident = new TextField();
		txtPlaceOfAccident = binder.buildAndBind("Place of Accident/Event","placeEvent",TextField.class);
		txtPlaceOfAccident.setNullRepresentation("");
		txtplaceofvisit = binder.buildAndBind("Place of Visit","placeOfVisit",TextField.class);
		txtplaceofvisit.setNullRepresentation("");
		lossDateField= binder.buildAndBind("Loss Date" , "lossOfDate", DateField.class);
		lossDateField.setDateFormat("dd-MM-yyyy");
		lossDateField.setValidationVisible(false);
		lossTime = binder.buildAndBind("Loss Time","lossTime",DateField.class);
		lossTime.setDateFormat("dd-MM-yyyy'-' HH:mm:ss");
//		lossTime.setTimeZone(TimeZone.getDefault());
		lossTime.setResolution(Resolution.MINUTE);
		lossTime.setLocale(Locale.ENGLISH);
		lossTime.setLocale(new Locale("en", "GB"));
		txtCity = new TextField("City");
		txtCity.setValue(bean.getHospCity());
//		txtCity = binder.buildAndBind("City","hospCity",TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setRequired(true);
		cmbCountry = new ComboBox("Country");
		cmbCountry.setValue(bean.getHospCountry());
//		cmbCountry = binder.buildAndBind("Country","hospCountry",ComboBox.class);
		cmbCountry.setRequired(true);
		cmbCountry.setContainerDataSource(country);
		cmbCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCountry.setItemCaptionPropertyId("value");
		
		if(this.bean.getHospCountry()!=null && bean.getHospCountry().getId()!= null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(this.bean.getHospCountry().getId());
			if(countryValueByKey!= null && countryValueByKey.getValue()!= null)
				cmbCountry.setValue(countryValueByKey);
		}
		
		txtAvaiSi = binder.buildAndBind("Available Sum Insured","sumInsured",TextField.class);
		txtAvaiSi.setNullRepresentation("");
		txtAvaiSi.setRequired(true);
		txtAvaiSi.setReadOnly(true);
		txtlossDetails = binder.buildAndBind("Loss Details","lossDetails",TextField.class);
		txtlossDetails.setNullRepresentation("");
		txtInitialProvisionAmt = binder.buildAndBind("Initial Provision Amt($)","provisionAmt",TextField.class);
		txtInitialProvisionAmt.setNullRepresentation("");
		txtInitialProvisionAmt.setRequired(true);
		txtInitialProvisionAmt.setReadOnly(true);
		txtINRConversionRate = binder.buildAndBind("INR Conversion Rate","inrConversionRate",TextField.class);
		txtINRConversionRate.setNullRepresentation("");
		txtINRConversionRate.setRequired(true);
		txtINRConversionRate.setReadOnly(true);
		txtINRValue = binder.buildAndBind("INR Value","inrtotal",TextField.class);
		txtINRValue.setNullRepresentation("");
		txtINRValue.setReadOnly(true);
		txtPlaceOfLossOrDelay = binder.buildAndBind("Place Of Loss/Delay","lossOrDelay",TextField.class);
		txtPlaceOfLossOrDelay.setNullRepresentation("");
		addHospital = new Button("Add Hospital");
		addHospital.setStyleName(BaseTheme.BUTTON_LINK); 
		addHospital.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				cmbHospitalCodeOrName.unselect(cmbHospitalCodeOrName.getValue());
				addHospitalpopup = new com.vaadin.ui.Window();
				addHospitalpopup.setWidth("30%");
				addHospitalpopup.setHeight("45%");
				addHospitalLayoutContent.initPopupLayout(addHospitalpopup, dummyTextField);
				addHospitalpopup.setContent(addHospitalLayoutContent);
				addHospitalpopup.setClosable(true);
				addHospitalpopup.center();
				addHospitalpopup.setResizable(false);
				addHospitalpopup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
				});
				addHospitalpopup.setModal(true);
				UI.getCurrent().addWindow(addHospitalpopup);
			}
			
		});
		
		cmbHospitalCodeOrName = new ComboBox("Hospital Code/Name");
		cmbHospitalCodeOrName.setRequired(true);
		loadHospitalValues();
		cmbHospitalCodeOrName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					updateHospitalDetailsInFields(event.getProperty().getValue().toString());
				}else{
					updateHospitalDetailsInFields("");
				}
			}
		});
		
		deathDate = binder.buildAndBind("Date of Death" , "deathDate", DateField.class);
		if(bean.getDeathDate() != null){
			deathDate.setValue(bean.getDeathDate());
		}else{
			deathDate.setValue(null);
		}
		
		deathDate.setDateFormat("dd-MM-yyyy");
		deathDate.setValidationVisible(false);
		
		patientStatus = new ComboBox("Patient Status");
		patientStatus.setContainerDataSource(ompClaimService.getPatientStatus());
		patientStatus.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					bean.setPatientStatus((SelectValue)event.getProperty().getValue());
					if(((SelectValue)event.getProperty().getValue()).toString().equals("Deceased")){
						deathDate.setEnabled(Boolean.TRUE);
					}else {
						deathDate.setValue(null);
						deathDate.setEnabled(Boolean.FALSE);
					}
				}else{
					bean.setPatientStatus(null);
					deathDate.setEnabled(Boolean.FALSE);
				}
			}
		});
		if(bean.getPatientStatus().getId() != null){
			patientStatus.setValue(bean.getPatientStatus());
			if((bean.getPatientStatus().getValue().toString()).equals("Deceased")){
				deathDate.setEnabled(Boolean.TRUE);
			}else {
				deathDate.setEnabled(Boolean.FALSE);
			}
		}
		if(bean.getNonHospitalisationFlag().equals("N")){
			patientStatus.setEnabled(Boolean.TRUE);
			deathDate.setEnabled(Boolean.TRUE);
		}else{
			patientStatus.setEnabled(Boolean.FALSE);
			deathDate.setEnabled(Boolean.FALSE);
		}
		
		
	
		dummyTextField =  new TextField();
		dummyTextField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				loadHospitalValues();
				if(bean.getHospital()!=null){
					cmbHospitalCodeOrName.setValue(bean.getHospital());
					cmbHospitalCodeOrName.select(cmbHospitalCodeOrName.getValue());
				}
			}
		});
		if(bean.getHospital()!=null){
			cmbHospitalCodeOrName.setValue(bean.getHospital());
			cmbHospitalCodeOrName.select(cmbHospitalCodeOrName.getValue());
		}
	/*	if(bean.getHospName()!=null ){
			SelectValue newValue = new SelectValue();
			newValue.setId(0l);
			newValue.setValue(bean.getHospName());
			cmbHospitalCodeOrName.setValue(newValue);
		}*/
		/*if(bean.getHospName()!= null){
			String hospName = bean.getHospName();
			hospName = hospName.trim();
//			OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(hospName);
//			cmbHospitalCodeOrName.setValue(hospitalRec.getName());
			OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(bean.getHospName());
			if(hospitalRec!=null){
				String hospName1 = hospitalRec.getName();
				SelectValue hosNamValue = new SelectValue();
				hosNamValue.setId(0l);
				hosNamValue.setValue(hospName1);
				cmbHospitalCodeOrName.setValue(hosNamValue.getValue());
			}
		}*/
		
		FormLayout provisionLayout1 = new FormLayout(txtInitialProvisionAmt,txtINRValue);
		FormLayout provisionLayout2 = new FormLayout(txtINRConversionRate);
//		FormLayout provisionLayout3 = new FormLayout(txtINRValue);
		
		FormLayout hospitalButton = new FormLayout(addHospital,cmbHospitalCodeOrName);
		
		HorizontalLayout hLayout = new HorizontalLayout(hospitalButton);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		TextField dummyfield = new TextField();
		dummyfield.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyfield.setReadOnly(true);
		
		FormLayout lossDetailsFrom = new FormLayout(cmbEventCode,claimTypeOption,admissionDate,dateofDischarge,txtAilmentLoss,txtPlaceOfAccident,txtplaceofvisit,lossDateField,lossTime,txtPlaceOfLossOrDelay,patientStatus,deathDate);
		
		FormLayout lossDetailsForm1 = new FormLayout(dummyfield,hospitalOption,addHospital,cmbHospitalCodeOrName,txtCity,cmbCountry,txtAvaiSi,txtlossDetails,txtplaceofvisit);
		
		//CR20181330 Start
		CGOption = new OptionGroup("Cash Guarantee");
		CGOption.addItems(getRadioButtonOptions());
		CGOption.setItemCaption(true, "Yes");
		CGOption.setItemCaption(false, "No");
		CGOption.setStyleName("horizontal");
		if(bean.getCgOption() != null){
			if(bean.getCgOption().equals("Y")){
				CGOption.select(true);
			}else if(bean.getCgOption().equals("N")){
				CGOption.select(false);
			}else{
				CGOption.select(null);
			}
		}else{
			CGOption.select(null);
		}
		CGOption.setEnabled(Boolean.FALSE);

		dateofCG = new DateField("Cash Guarantee Approved Date");
		dateofCG.setCaptionAsHtml(true);
		dateofCG.setDateFormat("dd-MM-yyyy");
		dateofCG.setValue(bean.getCgDate());
		dateofCG.setEnabled(Boolean.FALSE);

		txtCGApprovedAmt = new TextField("Cash Guarantee Approved Amount ($)");
		txtCGApprovedAmt.setNullRepresentation("");
//		CSValidator CGTxtValidator = new CSValidator();
//		CGTxtValidator.extend(txtInitialProvisionAmt);
//		CGTxtValidator.setRegExp("^[0-9.]*$");
//		CGTxtValidator.setPreventInvalidTyping(true);
		if(bean.getCgApprovedAmt() != null){
			txtCGApprovedAmt.setValue(bean.getCgApprovedAmt().toString());
		}
		txtCGApprovedAmt.setEnabled(Boolean.FALSE);

		txtCGRemarks = new TextArea("Cash Guarantee Remarks");
		txtCGRemarks.setRows(3);
		txtCGRemarks.setColumns(28);
		handleTextAreaPopup(txtCGRemarks,null);
		if(StringUtils.isBlank(bean.getCgRemarks())){
			txtCGRemarks.setValue("");
		}else{
			txtCGRemarks.setValue(bean.getCgRemarks());
		}
		txtCGRemarks.setEnabled(Boolean.FALSE);

		FormLayout cgForm1 = new FormLayout(CGOption,dateofCG);
		FormLayout cgForm2 = new FormLayout(txtCGApprovedAmt,txtCGRemarks);
		//CR20181330 End
				
		addListener();
		cmbEventCode.setValue(null);
		cmbEventCode.setValue(bean.getEventCode());
		HorizontalLayout lossDetailsLayuot = new HorizontalLayout(lossDetailsFrom,emptyFormLayoutTwo,lossDetailsForm1);
		lossDetailsLayuot.setSpacing(true);
		lossDetailsLayuot.setMargin(true);
		lossDetailsLayuot.setCaption("Loss Details");
		
		HorizontalLayout provisnLayout = new HorizontalLayout(provisionLayout1,provisionLayout2/*,provisionLayout3*/);
		provisnLayout.setCaption("Provision Details");
		provisnLayout.setMargin(Boolean.TRUE);
		provisnLayout.setSpacing(Boolean.TRUE);

		//CR20181330 Start
		HorizontalLayout cgLayout = new HorizontalLayout(cgForm1,cgForm2);
		cgLayout.setCaption("Cash Guarantee Details");
		cgLayout.setMargin(true);
		//CR20181330 End
		
		VerticalLayout vLayout = new VerticalLayout(lossDetailsLayuot,provisnLayout,cgLayout);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getNewIntimationDto().getPolicy().getKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMembershipLayout = new FormLayout(cmdClubMembership);
		clubMembershipLayout.setMargin(false);
		
		if (null != memberType && !memberType.isEmpty()) {
			vLayout.addComponentAsFirst(clubMembershipLayout);
		}
		
		HorizontalLayout billDetailsLayout = new HorizontalLayout();
		billDetailsLayout.setCaption("Bill Details");
		billDetailsLayout.addComponent(getContent(/*eventCode*/));
		billDetailsLayout.setMargin(true);
		billDetailsLayout.setSpacing(true);
		billDetailsLayout.setSizeFull();
		/*chkOpenionTaken = new CheckBox("Legal Opinion Taken");
		btnClose = new Button("Close");
		btnReOpen = new Button("ReOpen");
		if(bean.getIsApproverScrn()){
			
		}*/
		/*reasonForApproval = binder.buildAndBind("Reason for approval","reasonForApproval",TextArea.class);
		processorRemarks = binder.buildAndBind("Processor Remarks","processorRemarks",TextArea.class);
		VerticalLayout remarksLayout = new VerticalLayout(chkOpenionTaken,processorRemarks,reasonForApproval);
		remarksLayout.setSpacing(true);*/
		/*HorizontalLayout checkLayout = new HorizontalLayout(chkOpenionTaken);
		checkLayout.setSpacing(true);
		
		HorizontalLayout closeLayout = new HorizontalLayout(checkLayout,btnClose,btnReOpen);
		closeLayout.setSpacing(true);
		closeLayout.setComponentAlignment(checkLayout, Alignment.BOTTOM_LEFT);
		closeLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_RIGHT);
		closeLayout.setComponentAlignment(btnReOpen, Alignment.BOTTOM_RIGHT);
		addListenerButton(closeLayout, eventCode);*/
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout fotterLayout = new HorizontalLayout(getSaveButtonWithListener(confirmDialog),getCancelButton(confirmDialog));
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(vLayout);
		mainLayout.addComponent(billDetailsLayout);
		mainLayout.addComponent(getApproverContent());
		mainLayout.addComponent(fotterLayout);
		mainLayout.setComponentAlignment(fotterLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(Boolean.TRUE);
		mainLayout.setMargin(Boolean.TRUE);
		
		return mainLayout;
	}
	
	private void addListener() {

		if(cmbEventCode!= null){

			cmbEventCode.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	          public void valueChange( ValueChangeEvent event ) {
				
				SelectValue eventSelectValue = (SelectValue) event.getProperty().getValue();
				if(eventSelectValue != null && eventSelectValue.getValue()!=null && eventSelectValue.getId()!= null) {
					/*	String eventCodeType = eventSelectValue.getValue();
						String[] split = eventCodeType.split("-");
						String description1 = split[0];
						String description2 = split[1];
						String description3 = split[2];
						String description = split[3];
						String eventCode = description1+"-"+description2+"-"+description3;
						eventCode = eventCode.trim();*/
//						MastersEvents events = masterService.getEventType(eventCode);
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_Approver_BSI, bean,eventSelectValue);
						if(bean.getSumInsured()!=null && txtAvaiSi!=null){
							txtAvaiSi.setReadOnly(Boolean.FALSE);
							txtAvaiSi.setValue(bean.getSumInsured().toString());
							txtAvaiSi.setReadOnly(Boolean.TRUE);
						}
						if(!bean.getIsOnLoad()){
						if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& bean.getBalanceSI()!= null){
							double provisionAmt = SHAUtils.getDoubleFromStringWithComma(txtInitialProvisionAmt.getValue());
							if(provisionAmt > bean.getBalanceSI()){
								
								showErrorMessage( "Initial provision is greater than the Balance Sum Insured = " + bean.getBalanceSI());
								txtInitialProvisionAmt.setReadOnly(Boolean.FALSE);
								txtINRConversionRate.setReadOnly(Boolean.FALSE);
								txtInitialProvisionAmt.setValue("");
//								txtInitialProvisionAmt.setReadOnly(Boolean.TRUE);
							}
							
						}
						}
						MastersEvents events = masterService.getEventTypeByKey(eventSelectValue.getId());
						if(events!= null && events.getProcessType()!= null){
							
							if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C") || events.getProcessType().equalsIgnoreCase("R")){
								if(hospitalOption!= null){
									hospitalOption.setValue(null);
									hospitalOption.setEnabled(false);
									bean.setNonHospitalisationFlag("N");
									admissionDate.setVisible(Boolean.TRUE);
									dateofDischarge.setVisible(Boolean.TRUE);
									txtAilmentLoss.setVisible(Boolean.TRUE);
									txtPlaceOfAccident.setVisible(Boolean.TRUE);
									txtplaceofvisit.setVisible(Boolean.TRUE);
									cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
									txtCity.setVisible(Boolean.TRUE);
									cmbCountry.setVisible(Boolean.TRUE);
									addHospital.setVisible(Boolean.TRUE);
									lossDateField.setVisible(Boolean.FALSE);
				  					lossTime.setVisible(Boolean.FALSE);
				  					txtPlaceOfLossOrDelay.setVisible(Boolean.FALSE);
				  					txtlossDetails.setVisible(Boolean.FALSE);
//				  					dummyTextField.setVisible(Boolean.FALSE);
				  					if(hospitalOption!=null){/*
				  						claimTypeOption.setEnabled(Boolean.TRUE);
				  						claimTypeOption.setValue("Cashless");
									*/}
				  					if(!bean.getIsOnLoad()){
				  						if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C")){
					  						claimTypeOption.setEnabled(Boolean.TRUE);
					  						claimTypeOption.setValue(true);
										}
					  					if(events.getProcessType().equalsIgnoreCase("R")){
					  						claimTypeOption.setEnabled(Boolean.TRUE);
					  						claimTypeOption.setValue(false);
					  					}
				  					}
				  					
								}
							}
							
							if(events!= null && events.getProcessType()!= null){
								if(events.getProcessType().equalsIgnoreCase("N")){
									if(hospitalOption!= null){
										hospitalOption.setEnabled(Boolean.TRUE);
										hospitalOption.setValue("Non Medical");
										bean.setNonHospitalisationFlag("Y");
										lossDateField.setVisible(Boolean.TRUE);
										lossTime.setVisible(Boolean.FALSE);
										txtPlaceOfLossOrDelay.setVisible(Boolean.TRUE);
					  					txtlossDetails.setVisible(Boolean.TRUE);
					  					txtplaceofvisit.setVisible(Boolean.TRUE);
					  					admissionDate.setVisible(Boolean.FALSE);
					  					dateofDischarge.setVisible(Boolean.FALSE);
					  					txtAilmentLoss.setVisible(Boolean.FALSE);
										txtPlaceOfAccident.setVisible(Boolean.FALSE);
//										placeofvisit.setVisible(false);
										cmbHospitalCodeOrName.setVisible(Boolean.FALSE);
										txtCity.setVisible(Boolean.FALSE);
										cmbCountry.setVisible(Boolean.FALSE);
										addHospital.setVisible(Boolean.FALSE);
//										dummyTextField.setVisible(Boolean.FALSE);
										if(hospitalOption!=null){
//											optionClaimtype.setValue("Reimbursement");
											claimTypeOption.setEnabled(Boolean.FALSE);
											claimTypeOption.setValue(null);
					  				}else{
					  					claimTypeOption.setEnabled(true);
					  					}
					  				}
								}
							}
							if(events!= null && events.getEventCode().equalsIgnoreCase("OMP-CVR-011") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-009") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-012") ||
									 events.getEventCode().equalsIgnoreCase("CFT-CVR-010") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-015") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-012")){
								
								lossTime.setVisible(Boolean.TRUE);
							
							}
							
							
						}
						Boolean copayEnable =Boolean.TRUE;
						if(bean.getProductCode()!=null){
							String productCode = bean.getProductCode();
							if(!productCode.equalsIgnoreCase("OMP-PRD-001")){
								copayEnable = Boolean.FALSE;
							}else if(!events.getEventCode().equalsIgnoreCase("OMP-CVR-004")){
								copayEnable = Boolean.FALSE;
								}else if(bean.getAge()<75){
									copayEnable = Boolean.FALSE;
									}else if(bean.getAge()>75 && bean.getAge()<80){
										List<String> planList = new ArrayList<String>();
										planList.add("PLAN C1");
										planList.add("PLAN C2");
										planList.add("PLAN D1");
										planList.add("PLAN D2");
										if(!planList.contains(bean.getPlan())){
											copayEnable = Boolean.FALSE;
									}
								}
						}else{
							copayEnable = Boolean.FALSE;
						}
						if(ompClaimCalcViewTableObj!=null){
							ompClaimCalcViewTableObj.enableCopayDisableField(copayEnable);
						}
						bean.setCopayEnable(copayEnable);
						
						if(!bean.getIsOnLoad()){
							ompClaimCalcViewTableObj.setClassificationField();
						}
					}
				}
	    	});
	    
		}
		
		if(claimTypeOption!= null){

			claimTypeOption.addValueChangeListener( new ValueChangeListener( ) {
		      
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public void valueChange( ValueChangeEvent event ) {
				Boolean claimSelectValue = (Boolean) event.getProperty().getValue();
				if(claimSelectValue!= null && claimSelectValue.equals(Boolean.TRUE)){
					bean.setIsCashless(Boolean.TRUE);
					if(ompClaimCalcViewTableObj!=null){
						ompClaimCalcViewTableObj.enableDisableField(Boolean.TRUE);
					}
				}else{
					bean.setIsCashless(Boolean.FALSE);
					if(ompClaimCalcViewTableObj!=null){
					ompClaimCalcViewTableObj.enableDisableField(Boolean.FALSE);
				}}
				
				}
			});
		}
		
		
		if(lossTime != null){
			lossTime.addValueChangeListener( new ValueChangeListener( ) {
		      
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public void valueChange( ValueChangeEvent event ) {
				
				if(lossTime.getValue()!= null){
					Notification.show("Alert",
			                  "Check the number of hours delay entered confirms the policy condition",
			                  Notification.Type.ERROR_MESSAGE);
				}
		          
			}

		  });
		  }
		
		if(lossDateField != null){
			lossDateField.addValueChangeListener( new ValueChangeListener( ) {
		    
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void valueChange( ValueChangeEvent event ) {
			
				String str1 = (lossDateField.getValue() != null && !SHAUtils.isOMPDateOfIntimationWithPolicyRange(bean.getPolicy().getPolicyFromDate(), bean.getPolicy().getPolicyToDate(), lossDateField.getValue()) ? "loss Date is not with in policy period.</br>" : "");
				
				if(!SHAUtils.isEmpty(str1)){
//					lossDateField.setValue(null);
					showErrorMessage(str1);
					}

		    	}
			});
		}
		
		if(txtInitialProvisionAmt != null){
			txtInitialProvisionAmt.addValueChangeListener( new ValueChangeListener( ) {
		      
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public void valueChange( ValueChangeEvent event ) {
				
				if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& bean.getBalanceSI()!= null){
					double provisionAmt = SHAUtils.getDoubleFromStringWithComma(txtInitialProvisionAmt.getValue());
					if(provisionAmt > bean.getBalanceSI()){
						
						showErrorMessage( "Initial provision is greater than the Balance Sum Insured = " + bean.getBalanceSI());
						txtInitialProvisionAmt.setValue("");
					}else{/*
						txtInitialProvisionAmt.setReadOnly(Boolean.TRUE);
					*/}
				}
			
			}
				
		});
		}
		
		 txtInitialProvisionAmt
			.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					TextField txtInitialProvisionAmt1 = (TextField) event.getComponent();
					if(txtInitialProvisionAmt1!=null && txtInitialProvisionAmt1.getValue()!=null && txtINRConversionRate!=null && txtINRConversionRate.getValue()!=null){
						String initialProvisionAmt = txtInitialProvisionAmt1.getValue();
						String iNRConversionRate = txtINRConversionRate.getValue();
						double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
						txtINRValue.setReadOnly(false);
						txtINRValue.setValue(String.valueOf(calculateInrValue));
						txtINRValue.setReadOnly(true);
					}
					
				}
			}
	);

txtINRConversionRate
	.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {
			TextField conversationRate = (TextField) event.getComponent();
			if(conversationRate!=null && conversationRate.getValue()!=null && txtInitialProvisionAmt!=null && txtInitialProvisionAmt.getValue()!=null){
				String iNRConversionRate = conversationRate.getValue();
				String initialProvisionAmt = txtInitialProvisionAmt.getValue();
				double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
				txtINRValue.setReadOnly(false);
				txtINRValue.setValue(String.valueOf(calculateInrValue));
				txtINRValue.setReadOnly(true);
			}
			
		}

	});
		
	}

	private double calculateInrValue(
			String initialProvisionAmt,
			String iNRConversionRate) {
	  
	  NumberFormat format = NumberFormat.getInstance(Locale.US);
	  double iNRValue =0d;

     Number number;
     Number number1;
     
	try {
		if(initialProvisionAmt != null && !("").equals(initialProvisionAmt) && iNRConversionRate != null && !("").equals(iNRConversionRate))
		{
		number = format.parse(initialProvisionAmt);
		number1 = format.parse(iNRConversionRate);
	  double initialProvisionAmtDouble = number.doubleValue();
	  double iNRConversionRateDouble = number1.doubleValue();
	  iNRValue =(initialProvisionAmtDouble * iNRConversionRateDouble);
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 	iNRValue;
	}
	
	public void loadHospitalValues(){
		BeanItemContainer<SelectValue> omphospitalName = ompIntimationService.getAllHospitalCodeAndValue();
		cmbHospitalCodeOrName.setContainerDataSource(omphospitalName);
		cmbHospitalCodeOrName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalCodeOrName.setItemCaptionPropertyId("value");
	}
	
	public void updateHospitalDetailsInFields(String hospitalCodeOrName){
		this.bean.setHospName(hospitalCodeOrName);
		if(StringUtils.isNotBlank(hospitalCodeOrName)){
			OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(hospitalCodeOrName);
			if(hospitalRec != null){
				this.bean.setHospCity(hospitalRec.getCity());
//				this.bean.setHospCountry(hospitalRec.getCountry());
				txtCity.setValue(hospitalRec.getCity());
				
				if(hospitalRec.getCountryId()!=null){
					SelectValue countryValueByKey = masterService.getCountryValueByKey(hospitalRec.getCountryId());
					if(countryValueByKey!= null && countryValueByKey.getValue()!= null){
						cmbCountry.setValue(countryValueByKey);
					}
				}
			}else{
				this.bean.setHospCity("");
				this.bean.setCountry("");
				txtCity.setValue("");
				cmbCountry.setValue("");
			}
		}else{
			this.bean.setHospCity("");
			this.bean.setCountry("");
			txtCity.setValue("");
			cmbCountry.setValue("");
		}
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
public Component getContent(/*BeanItemContainer<SelectValue> negotiatorName*/) {
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getNewIntimationDto().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
 			showCMDAlert();
		}
	
 		fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = bean.getClaimCalculationViewTable();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
			ompClaimCalcViewTableObj.setTableList(claimCalculationViewTable);
			}
		/*if(claimCalculationViewTable!= null && !claimCalculationViewTable.isEmpty() && claimCalculationViewTable.size() >0){
			cmbEventCode.setEnabled(false);
		}*/
		/*txtRemark = binder.buildAndBind("Remarks (Processor)", "remarks",TextArea.class);
		txtRemark.setWidth("40%");
		txtRemark.setMaxLength(4000);*/
		VerticalLayout wholeLayout = new VerticalLayout(ompClaimCalcViewTableObj/*, getCalculationLayout(),txtRemark, getPaymentDetailsLayout(), buildButtons(bean,negotiatorName)*/);
		 wholeLayout.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 return wholeLayout;
	}

	public void showCMDAlert() {	 
		 
		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
	
 		final ConfirmDialog dialog = new ConfirmDialog();
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

	public Component getApproverContent(){
		chkOpenionTaken = binder.buildAndBind("Legal Opinion Taken","isLegalFlag",CheckBox.class);
		chkOpenionTaken.setReadOnly(true);
		/*reasonForApproval = binder.buildAndBind("Reason for approval","reasonForApproval",TextArea.class);
		processorRemarks = binder.buildAndBind("Processor Remarks","processorRemarks",TextArea.class);
		processorRemarks.setReadOnly(true);
		processorRemarks.setValidationVisible(false);
		reasonForRejection = binder.buildAndBind("Reason Rejection*","reasonForRejectionRemarks",ComboBox.class);
		BeanItemContainer<SelectValue> ompRejecIntimation = masterService.getConversionReasonByValue(ReferenceTable.OMP_INTIMATION_REJCTION);
		reasonForRejection.setContainerDataSource(ompRejecIntimation);
		reasonForRejection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForRejection.setItemCaptionPropertyId("value");*/
		VerticalLayout remarksLayout = new VerticalLayout(chkOpenionTaken);
		remarksLayout.setSpacing(Boolean.TRUE);
		remarksLayout.setMargin(Boolean.TRUE);
		return remarksLayout;
	}
	private Button getSaveButtonWithListener(final ConfirmDialog dialog) {
		btnSave = new Button("Save");
		btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				btnSave.setEnabled(Boolean.FALSE);
				if(!validatePage(Boolean.TRUE)) {
					dialog.close();
//					releaseWorkFlowTask();
//					if(bean.getIsApproverScrn()){
					if(cmbHospitalCodeOrName!= null && cmbHospitalCodeOrName.getValue()!= null){
						bean.setHospName(cmbHospitalCodeOrName.getValue().toString());
						OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(cmbHospitalCodeOrName.getValue().toString());
						SelectValue hospital= new SelectValue();
						hospital.setId(hospitalRec.getKey());
						hospital.setValue(hospitalRec.getName());
						bean.setHospital(hospital);
					}
					if(admissionDate!= null && admissionDate.getValue()!= null ){
						bean.setAdmissionDate(admissionDate.getValue());
					}
					if(txtCity!= null && txtCity.getValue()!= null ){
						bean.setHospCity(txtCity.getValue());
					}
					if(cmbCountry!= null && cmbCountry.getValue()!= null ){
						bean.setHospCountry((SelectValue) cmbCountry.getValue());
					}
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_CLAIM_APPROVER_SUBMIT, bean);
//					}else{
//						fireViewEvent(OMPClaimProcessorPagePresenter.OMP_CLAIM_PROCESSOR_SAVE, bean);
//					}
					//wizard.getNextButton().setEnabled(false);
					//wizard.getFinishButton().setEnabled(true);
					//bean.setIsFirstPageSubmit(true);
				} else {/*
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
				*/}
			}

			
		});
		return btnSave;
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Exit");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				releaseWorkFlowTask();
//				if(bean.getIsApproverScrn()){
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_CANCEL_APPROVER,bean);
				
//					fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_APPROVER, null);
//				}else{
//					fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_PROCESSOR, null);
//				}
			}
		});
		return btnCancel;
	}
	
	protected void addListenerButton(final HorizontalLayout buttonHLayout, final BeanItemContainer<SelectValue> negotiatorName){
		btnClose.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setButtonflag(true);
				
				if(bean.getClaimCalculationViewTable()== null && bean.getClaimCalculationViewTable().isEmpty()){
					fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_REJECTION, buttonHLayout,negotiatorName);
				}
			}
		});
		
		btnReOpen.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setButtonflag(true);
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_NEGOTIATE, buttonHLayout,negotiatorName);
			}
		});
	}
	
/*public Component getContent(BeanItemContainer<SelectValue> negotiatorName) {
		
		fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESSOR_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
			ompClaimCalcViewTableObj.setTableList(bean.getClaimCalculationViewTable());
			}
		txtRemark = binder.buildAndBind("Remarks (Processor)", "remarks",TextArea.class);
		txtRemark.setWidth("40%");
		txtRemark.setMaxLength(4000);
		VerticalLayout wholeLayout = new VerticalLayout(ompClaimCalcViewTableObj, getCalculationLayout(),txtRemark, getPaymentDetailsLayout(), buildButtons(bean,negotiatorName));
		 wholeLayout.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		 wholeLayout.setSpacing(true);
		 wholeLayout.setSizeFull();
		 return wholeLayout;
	}*/

public void generateRejection(HorizontalLayout buttonLayout) {
//	unbindField(getListOfButtonFields());
	txtClosedRemarks = binder.buildAndBind("Closed Remarks" , "closeRemarks", TextArea.class);
	txtClosedRemarks.setMaxLength(4000);
	VerticalLayout layout = new VerticalLayout(txtClosedRemarks);
//	bean.setOutCome(SHAConstants.OUTCOME_FOR_OMP_PROCESSOR);
	mandatoryFields.add(txtClosedRemarks);
	txtClosedRemarks.setRequired(true);
//	if(wholeLayout.getComponentCount()>5){
//		wholeLayout.removeComponent(wholeLayout.getComponent(wholeLayout.getComponentCount() - 1));
//		wholeLayout.addComponent(layout);
//	}else{
	if(mainLayout.getComponentCount()>4){
		mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount() - 1));
		mainLayout.addComponent(layout);
	}else{
		
	mainLayout.addComponent(layout);
	}
	
}

public void generateNegotiate(HorizontalLayout buttonLayout,BeanItemContainer<SelectValue> negotiatorName){
	cmbReopenRemarks = binder.buildAndBind("Reopen Remarks","reopenRemarks",ComboBox.class);
//	cmbReopenRemarks = new ComboBox("ReOpen Remarks");
	VerticalLayout vLayout = new VerticalLayout(cmbReopenRemarks);
	mandatoryFields.add(cmbReopenRemarks);
	cmbReopenRemarks.setRequired(true);
	if(mainLayout.getComponentCount()>4){
		mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount() - 1));
		mainLayout.addComponent(vLayout);
	}else{
		
	mainLayout.addComponent(vLayout);
	}
	
}
	
	public boolean validatePage(Boolean true1) {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";	
		
		
		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null  && field.isEnabled()) {
					eMsg += errMsg.getFormattedHtmlMessage();
					hasError = true;
				}
			}
		}
		
		Set<String> errors = ompClaimCalcViewTableObj.validateCalculation(bean);
		if(null != errors && !errors.isEmpty()){
			for (String error : errors) {
				eMsg += error + "</br>";
				hasError = true;
			}
			
		}
		
		if( claimTypeOption!= null && claimTypeOption.getValue()==null && hospitalOption!= null && !hospitalOption.getValue().equals("Non Medical")){
			hasError = true;
			eMsg += "Please select cashless or reimbursement </br>";
		}
		if(hospitalOption!= null && hospitalOption.getValue()== null&& claimTypeOption!= null && claimTypeOption.getValue()!= null){
			
			if(cmbHospitalCodeOrName!= null &&cmbHospitalCodeOrName.getValue()== null){
				hasError = true;
				eMsg = eMsg + "Please Provide HospitalCode Or Name. </br>";
			}
			if(StringUtils.isBlank(String.valueOf(txtCity.getValue()))){
				hasError = true;
				eMsg = eMsg + "Please Provide City. </br>";
			}
			if(cmbCountry!= null && cmbCountry.getValue()==null){
				hasError = true;
				eMsg = eMsg + "Please Provide Country. </br>";
			}
			if(admissionDate!= null && admissionDate.getValue()==null){
				hasError = true;
				eMsg = eMsg + "Please Provide admissionDate. </br>";
			}
		}
		
		if(hospitalOption!= null && hospitalOption.getValue()!= null){
			if(lossDateField!= null && lossDateField.getValue()==null){
				hasError = true;
				eMsg = eMsg + "Please Provide LossDate. </br>";
			}
		}
		if(StringUtils.isBlank(String.valueOf(txtInitialProvisionAmt.getValue()))){
			hasError = true;
			eMsg = eMsg + "Please Provide Initial Provision Amount. </br>";
		}
		
		if(StringUtils.isBlank(String.valueOf(txtINRConversionRate.getValue()))){
			hasError = true;
			eMsg = eMsg + "Please Provide INR Conversion Amount. </br>";
		}
		
		if(!hasError) {
			
		}
		
		
		if (hasError) {
			setRequired(true);
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

			hasError = true;
			return hasError;
		} else {
			try {
				this.binder.commit();
				bindValues();
				List<OMPClaimCalculationViewTableDTO> claimCalcViewTableList = ompClaimCalcViewTableObj.getValues();
				if(claimCalcViewTableList!=null){
					bean.setClaimCalculationViewTable(claimCalcViewTableList);
				}
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return false;
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
			//field.setValidationVisible(false);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {
	
		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	private void releaseWorkFlowTask(){
		VaadinSession session = getSession();
		Long wrkFlowKey=(Long)session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
 		if(wrkFlowKey != null) {
 			dbService.callOMPUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
 		
	}
	
	private void bindValues() {
		//if(this.ompClaimCalcViewTableObj != null) {
	//		this.ompClaimCalcViewTableObj.
		//}
	}
	
	public void cancelIntimation(){
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {
		
					private static final long serialVersionUID = 1L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							// YES
							/*if(dtoBean.getKey() == null){
								fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
							}
							else{
								fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
							}*/
							fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_APPROVER, null);
						}
					}
				});
		
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	public void generateApprovalRemark(
			OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		unbindField(processorRemarks);
		unbindField(reasonForApproval);
		processorRemarks = binder.buildAndBind("Processor Remarks","processorRemarks",TextArea.class);
		processorRemarks.setValidationVisible(false);
		reasonForApproval = binder.buildAndBind("Reason for approval","reasonForApproval",TextArea.class);
		reasonForApproval.setValidationVisible(false);
		
		mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
		addListenerForApprovalRemark(calculationViewTableDTO);
		processorRemarks.setValue(calculationViewTableDTO.getProcessorRemarks());
		reasonForApproval.setValue(calculationViewTableDTO.getReasonForApproval());
		processorRemarks.setEnabled(Boolean.FALSE);
		reasonForApproval.setEnabled(!calculationViewTableDTO.getIsReadOnly());
		SelectValue viewforApprover = calculationViewTableDTO.getViewforApprover();
		if(calculationViewTableDTO.getSendforApprover()!=null && calculationViewTableDTO.getSendforApprover().equalsIgnoreCase("Y")){
			
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(processorRemarks);
			layout.addComponent(reasonForApproval);
			layout.setSpacing(Boolean.TRUE);
			layout.setCaption("");
			
			if(mainLayout.getComponentCount()>=4){
				mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
				mainLayout.addComponent(layout);
			}
			else if(mainLayout.getComponentCount()==3){
				mainLayout.addComponent(layout);
			}
		}
		if(calculationViewTableDTO.getSendforApprover()!=null && calculationViewTableDTO.getSendforApprover().equalsIgnoreCase("N")){
			if(mainLayout.getComponentCount()==4){
				mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
			}
		}
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout fotterLayout = new HorizontalLayout(getSaveButtonWithListener(confirmDialog),getCancelButton(confirmDialog));
		fotterLayout.setSpacing(true);
		fotterLayout.setMargin(true);
		mainLayout.addComponent(fotterLayout);
		mainLayout.setComponentAlignment(fotterLayout, Alignment.MIDDLE_CENTER);
	}

	public void generateRejectionRemark(
			OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		unbindField(processorRemarks);
		unbindField(reasonForRejection);
		processorRemarks = binder.buildAndBind("Processor Remarks","processorRemarks",TextArea.class);
		processorRemarks.setValidationVisible(false);
		reasonForRejection = binder.buildAndBind("Reason Rejection*","reasonForRejectionRemarks",ComboBox.class);
		BeanItemContainer<SelectValue> ompRejecIntimation = masterService.getConversionReasonByValue(ReferenceTable.OMP_INTIMATION_REJCTION);
		reasonForRejection.setContainerDataSource(ompRejecIntimation);
		reasonForRejection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForRejection.setItemCaptionPropertyId("value");
		addListenerForReasonRejection(calculationViewTableDTO);
		processorRemarks.setValue(calculationViewTableDTO.getProcessorRemarks());
		processorRemarks.setEnabled(Boolean.FALSE);
		reasonForRejection.setValue(calculationViewTableDTO.getReasonForRejectionRemarks());
		reasonForRejection.setEnabled(!calculationViewTableDTO.getIsReadOnly());
		mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
		if(calculationViewTableDTO.getReject()!= null && calculationViewTableDTO.getReject().equalsIgnoreCase("Y")){
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(processorRemarks);
//			layout.addComponent(reasonForRejection);
			layout.setSpacing(Boolean.TRUE);
			layout.setCaption("");
			
			if(mainLayout.getComponentCount()>=4){
				mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
				mainLayout.addComponent(layout);
				
			}else if(mainLayout.getComponentCount()<4){
						mainLayout.addComponent(layout);
			}
		}
		if(calculationViewTableDTO.getReject()!= null && calculationViewTableDTO.getReject().equalsIgnoreCase("N")){
			
			if(mainLayout.getComponentCount()>=4){
				mainLayout.removeComponent(mainLayout.getComponent(mainLayout.getComponentCount()-1));
			}
		}
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout fotterLayout = new HorizontalLayout(getSaveButtonWithListener(confirmDialog),getCancelButton(confirmDialog));
		fotterLayout.setSpacing(true);
		fotterLayout.setMargin(true);
		mainLayout.addComponent(fotterLayout);
		mainLayout.setComponentAlignment(fotterLayout, Alignment.MIDDLE_CENTER);
	}

	
	private void addListenerForReasonRejection(final OMPClaimCalculationViewTableDTO calculationViewTableDTO)
	{
		if(reasonForRejection != null) {
			reasonForRejection.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue reasonForRejectionRemarks = (SelectValue) event.getProperty().getValue();
					if(calculationViewTableDTO!=null ){
						if(reasonForRejectionRemarks!=null){
							calculationViewTableDTO.setReasonForRejectionRemarks(reasonForRejectionRemarks);
						}
					}else{
						showErrorMessage("Please select a rejected rod and select the Rejection Remark");
					}
				}
			});
		}
	}
	
	private void addListenerForApprovalRemark(final OMPClaimCalculationViewTableDTO calculationViewTableDTO)
	{
		if(reasonForApproval != null) {
			reasonForApproval.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea processorRemarks = (TextArea) event.getProperty();
					if(calculationViewTableDTO!=null ){
						if(processorRemarks!=null && processorRemarks.getValue()!=null){
							calculationViewTableDTO.setReasonForApproval(processorRemarks.getValue());
						}
					}else{
						showErrorMessage("Please select a rejected rod and select the Rejection Remark");
					}
				}
			});
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
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	
	private void addListenerForEventCode()
	{
		if(ompClaimCalcViewTableObj.dummyField != null) {
			ompClaimCalcViewTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {

					TextField eventCode = (TextField) event.getProperty();
						if(eventCode!=null && eventCode.getValue()!=null){
//							calculationViewTableDTO.setProcessorRemarks(processorRemarks.getValue());
								cmbEventCode.setEnabled(Boolean.FALSE);
								claimTypeOption.setEnabled(Boolean.FALSE);
						}
					
				
				}
			});
		}
	}
	
	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

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
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(true);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Cash Guarantee Remarks";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
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
}
