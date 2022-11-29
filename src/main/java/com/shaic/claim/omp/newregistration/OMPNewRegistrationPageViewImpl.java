package com.shaic.claim.omp.newregistration;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPViewPolicyDetails;
import com.shaic.claim.OMPViewDetails.view.OMPViewCurrentPolicyDetailsUI;
import com.shaic.claim.OMPViewDetails.view.OMPViewRiskDetailsUI;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPPreviousClaimWindowUI;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimWindowOpen;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimsTable;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.omp.carousel.OMPViewMerDetailsUI;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationAddHospital;
import com.shaic.claim.ompviewroddetails.OMPViewIntimationDetailsUI;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPViewClaimantDetailsPageUI;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.themes.Reindeer;

public class OMPNewRegistrationPageViewImpl extends AbstractMVPView implements OMPNewRegistrationPageView{

	private static final long serialVersionUID = 1789839319768628238L;

	@Inject
	private Instance<OMPNewRegistrationRevisedCarousel> commonCarouselInstance;
	
	private Button  btnCurrentPolicyDetails;
	
	//private IWizardPartialComplete wizard;
	
	private VerticalLayout mainPanel;
	
	//private BeanFieldGroup<OMPCreateIntimationTableDTO> binder;
	
	private OMPNewRegistrationSearchDTO dtoBean;
	
	private Button addHospital;
	
	private String eMsg = "";
	
	//private List<Component> mandatoryFields = new ArrayList<Component>();
	
	private DateField intimationDate;
	private DateField lossDateAndTime;
	private TextField tpaIntimationno;
	private ComboBox eventCode;
	private BeanItemContainer<SelectValue> ompEventCodesContainer;
	private TextField placeofvisit;
	private TextField placeoflossOrDelay;
	private TextField txtSponsorName;
	private ComboBox cmbModeofIntimation;
	private ComboBox cmbInsuredName;
	private ComboBox cmbHospitalCodeOrName;
	//private ComboBox cmbCountry;
	private TextField txtAilmentOrLoss;
	private OptionGroup optionClaimtype;
	private OptionGroup optionHospitalType;
	private DateField AdmissionDate;
	private DateField DateofDischarge;
	private ComboBox cmbIntimatedBy;
	private TextField txtCallerOrIntimatorName;
	private TextField txtCallerContactNo;
	private TextField txtInitialProvisionAmt;
	private TextField txtINRConversionRate;
	private TextField txtINRValue;	
	private TextField txtCity;
	private ComboBox txtCountry;
	private TextArea txtRemarks;
	
	public TextField dummyTextField;
	
	private TextField txtPlaceOfAccident;
	
	private DateField txtLosstime;
	
	private TextField txtlossDetails;
	
	private Button registarButton;
	
	private Button rejectButton;
	
	private Window addHospitalpopup;	
	@Inject
	private OMPCreateIntimationAddHospital addHospitalLayoutContent;

	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dBCalculationService;
	
	@Inject
	private OMPViewPolicyDetails viewPolicyDetails;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	@Inject
	private OMPViewIntimationDetailsUI ompViewIntimationDetails;
	
	@Inject
	private OMPViewCurrentPolicyDetailsUI ompViewCurrentPolicyUI;
	@Inject
	private OMPViewClaimantDetailsPageUI ompViewClaimantdetailUI;
	
	@Inject
	private ViewDetails viewDetails;
	
	private Button riskButton;
	
	private Button balanceSi;
	
	private Button policyDetails;
	
	private Button vb64Button;
	
	private Button viewMedButton;
	
	private Button previousClaimButton;
	
	private VerticalLayout dynamicFieldsLayout;
	
	private FormLayout dynamicFrmLayout;
	
	private TextArea registrationRemarksTxta;

	private ComboBox suggestRejectionTxta;
	
	private TextField intimationNumber;
	
	private TextField claimNumber;
	
	private ClaimDto claimDto =null;
	
	private NewIntimationDto newIntimationDto = new NewIntimationDto();
	
	private OMPClaim claim = null;
	
	@Inject
	private OMPViewRiskDetailsUI ompRiskDetailsUI;
	@Inject
	private OMPViewClaimantDetailsPageUI ompClaimantUI;
	@Inject
	private OMPViewMerDetailsUI ompViewMerDetailUI;
	
	@Inject
	private Instance<ViewOMPPreviousClaimWindowOpen> viewOMPPreviousClaimWindowOpen;
	
	@Inject
	private ViewOMPPreviousClaimsTable ompPreviousClaimsTable;
	
	private Policy policy;
	
	@Inject
	private OMPNewRegViewIntimationDetailsUI ompnewViewIntimationUI;
	
//	//R1276
//	private boolean isTPAUser; 
	private TextArea txtaintimationRemarks;
	private TextField txtClaimedAmount;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	
	private Button earlierAcknowledgementDetailsButton;
	private Button acknowledgementDetailsButton;
	
	private boolean isTPAUserLogin;

	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}
	
	
	private void initBinder() {
		/*intimationContainer = new BeanItemContainer<NewIntimationDto>(
				NewIntimationDto.class);
		intimationContainer.addBean(bean);*/

		//this.binder = new BeanFieldGroup<OMPCreateIntimationTableDTO>(OMPCreateIntimationTableDTO.class);
		//this.binder.setItemDataSource(dtoBean);
	}

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();			
	}

	public void initView(OMPNewRegistrationSearchDTO ompIntimationDTO) {
		this.dtoBean =null;
		this.dtoBean = ompIntimationDTO;
		this.claimDto = new ClaimDto();
		mainPanel = new VerticalLayout();
		registrationRemarksTxta =null;
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		initBinder();
		
		//R1276 - Validating login user is TPA or Star
//		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
//		isTPAUser = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		OMPIntimation intimation = ompIntimationService
				.searchbyIntimationNo(ompIntimationDTO.getIntimationno());
		OMPClaim claim = ompclaimService.getClaimforIntimation(intimation.getKey());
		
		//claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId()
		claimDto.getNewIntimationDto().setPolicy(intimation.getPolicy());
		claimDto.getNewIntimationDto().setIntimationId(intimation.getIntimationId());
		
		btnCurrentPolicyDetails = new Button("View Current Policy Details");		
		btnCurrentPolicyDetails.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Policy policy = dtoBean.getPolicy();
				viewPolicyDetails.setPolicyServiceAndPolicy(policyService, policy, masterService);
				viewPolicyDetails.initView();
				UI.getCurrent().addWindow(viewPolicyDetails);
			}
		});		
		
		OMPNewRegistrationRevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(dtoBean, "Claim Registration");
		mainPanel.addComponent(intimationDetailsCarousel);
	//	viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Intimation and Hospital Details");
		
		HorizontalLayout horLayout = new HorizontalLayout();
//		horLayout.addComponent(btnCurrentPolicyDetails);
		horLayout.setSizeFull();
		horLayout.setHeight("10px");
		mainPanel.addComponent(horLayout);
//		horLayout.setComponentAlignment(btnCurrentPolicyDetails, Alignment.TOP_RIGHT);
		mainPanel.addComponent(viewDetailsButtonLayout());
		HorizontalLayout dummyhLayout = new HorizontalLayout();
		dummyhLayout.setSpacing(true);
		dummyhLayout.setMargin(true);
		mainPanel.addComponent(dummyhLayout);
		mainPanel.addComponent(commonButtonsLayout(intimation, claim));
		mainPanel.addComponent(addFooterButtons());
//		mainPanel.setMargin(true);
//		mainPanel.setSizeFull();		
//		fireViewEvent(OMPClaimRegistrationWizardPresenter.GET_BALANCE_SI,newIntimationDto);
		setCompositionRoot(mainPanel);			
		}


	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
			MasterService masterService) {
		//super("View Policy Details");
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;

	}
private VerticalLayout viewDetailsButtonLayout() {
	
	 riskButton = new Button("View Risk Details");
	riskButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
//			viewDetails.getViewRiskDetails(dtoBean.getIntimationno());
//			public void getViewOmpRiskDetails(String policyNo) {
				Window popup =null;
				popup = new com.vaadin.ui.Window();
				popup.setWidth("800px");
				popup.setHeight("280px");
				popup.setCaption("Risk Details");
				if(dtoBean.getIntimationno()!=null){
					OMPIntimation intimation = ompIntimationService
							.searchbyIntimationNo(dtoBean.getIntimationno());
					Policy policy = dtoBean.getPolicy();
					ompRiskDetailsUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
					Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
					ompRiskDetailsUI.init(apolicy,intimation);
				}else{
					
					Policy policy = dtoBean.getPolicy();
					ompRiskDetailsUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
					Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
					ompRiskDetailsUI.init(apolicy,null);
				}
				popup.setContent(ompRiskDetailsUI);
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
//		}
		
	});
	
	 balanceSi = new Button("View Balance SI");
	 if(isTPAUserLogin){
		 balanceSi.setEnabled(Boolean.FALSE); 
	 }
	 balanceSi.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			if(dtoBean.getIntimationno()!= null){
				
				viewDetails.getViewOmpBalanceSI(dtoBean.getIntimationno());
			}
			else{
				showErrorMessage("Intimation not available");
			}
		}
		
	});
	
	 policyDetails = new Button("View Policy Details");
	 policyDetails.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			Policy policy = dtoBean.getPolicy();
			ompViewCurrentPolicyUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
			ompViewCurrentPolicyUI.initView();
			UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
//			viewDetails.getViewPolicy(dtoBean.getIntimationno());
		}
		
	});
	
	 
	 vb64Button = new Button("View 64VB");
	 if(isTPAUserLogin){
		 vb64Button.setEnabled(Boolean.FALSE); 
	 }
	 vb64Button.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			if(dtoBean.getIntimationno()!= null){
//				viewDetails.getIrda64VbDetails(dtoBean.getIntimationno());
			}
			else{
				showErrorMessage("Intimation not available");
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
			if(dtoBean.getIntimationno()!= null){
				
				viewDetails.getMERDetails(dtoBean.getIntimationno());
			}
			showErrorMessage("Intimation not available");
//			Policy policy = dtoBean.getPolicy();
//			ompViewMerDetailUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
//			ompViewMerDetailUI.initView();
//			UI.getCurrent().addWindow(ompViewMerDetailUI);
		}
		
	});
	 
	 previousClaimButton = new Button("Previous Claim Details");
	 if(isTPAUserLogin){
		 previousClaimButton.setEnabled(Boolean.FALSE); 
	 }
	 previousClaimButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			if(dtoBean.getIntimationno()!= null){
				
//				viewDetails.getViewPolicy(dtoBean.getIntimationno());
			}
			else{
				showErrorMessage("Intimation not available");
			}
		}
		
	});
	 
	 Button viewHistoryButton = new Button("View Claim History");
	 viewHistoryButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			if(dtoBean.getIntimationno()!= null){
				viewDetails.getViewClaimHistory(dtoBean.getIntimationno());
			}
		}
		
	});
	 
	 final Button viewPreviousPolicyClaimButton = new Button("Prev Claim Details");
	 if(isTPAUserLogin){
		 viewPreviousPolicyClaimButton.setEnabled(Boolean.FALSE); 
	 }
	 viewPreviousPolicyClaimButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			if(dtoBean.getIntimationno()!= null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,dtoBean.getIntimationno());
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
		
		BrowserWindowOpener opener = new BrowserWindowOpener(OMPPreviousClaimWindowUI.class);
		opener.setFeatures("height=700,width=1300,resizable");
		opener.setWindowName("_blank");
		opener.extend(viewPreviousPolicyClaimButton);
			}
			else{
				showErrorMessage("Intimation not available");
			}
		}
		
	});
	 
	 Button viewClaimantButton = new Button("View Claimant Details");
	 viewClaimantButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
//			viewDetails.getviewClaimantDetails(dtoBean.getIntimationno());
			Window popup =null;
			popup = new com.vaadin.ui.Window();
			popup.setWidth("800px");
			popup.setHeight("280px");
			popup.setCaption("View Claimant Details");
//			OMPIntimation intimation = ompIntimationService
//					.searchbyIntimationNo(intimationNo);
			Policy policy = dtoBean.getPolicy();
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
			if(dtoBean.getIntimationno()!= null){
//				viewDetails.getViewDocumentByPolicyNo(dtoBean.getIntimationno());
			}
			else{
				showErrorMessage("Intimation not available");
			}
		}
		
	});
	 
	 earlierAcknowledgementDetailsButton = new Button("Earlier Acknowledgement Details");
		earlierAcknowledgementDetailsButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!=null){
					viewDetails.getEarlierAcknowledgementDetailsButton(dtoBean.getIntimationno());
				}
			}
		});
		
		acknowledgementDetailsButton = new Button("View Acknowledgement Details");
		acknowledgementDetailsButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null ){
					viewDetails.getAcknowledgementDetailsButton(dtoBean.getIntimationno());
				}
			}
		});
	 
intimationNumber = new TextField("Intimation Number");
//intimationNumber.setValue(dtoBean.getIntimationno());
intimationNumber.setReadOnly(true);
claimNumber = new TextField("Claim Number");
//claimNumber.setValue(dtoBean.getClaimno());
claimNumber.setReadOnly(true);
		HorizontalLayout horizontalLayout = null;
		HorizontalLayout horizontalLayout2 = null;
		
		horizontalLayout = new HorizontalLayout(acknowledgementDetailsButton,earlierAcknowledgementDetailsButton,riskButton,balanceSi,policyDetails,vb64Button,viewMedButton);
		horizontalLayout2 = new HorizontalLayout(previousClaimButton,viewHistoryButton, viewPreviousPolicyClaimButton,viewClaimantButton,viewDocButton);
		horizontalLayout.setSpacing(true);
		horizontalLayout2.setSpacing(true);
		
		/*FormLayout intimationLayout = new FormLayout(intimationNumber);
		FormLayout claimLayout = new FormLayout(claimNumber);
		HorizontalLayout hLayout = new HorizontalLayout(intimationLayout,claimLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setHeight("57px");
		horizontalLayout2.setSpacing(true);
		horizontalLayout2.setHeight("77px");
		hLayout.setMargin(true);
		VerticalLayout vLayout = new VerticalLayout(hLayout);
		vLayout.setComponentAlignment(hLayout, Alignment.TOP_RIGHT);*/
		VerticalLayout verticalLayout = null;
		verticalLayout = new VerticalLayout(horizontalLayout,horizontalLayout2/*,vLayout*/);
		verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		verticalLayout.setComponentAlignment(horizontalLayout2, Alignment.TOP_RIGHT);
		verticalLayout.setSpacing(true);
		verticalLayout.setWidth("100%");
		verticalLayout.setHeight("95px");
		return verticalLayout;
		
	}

@SuppressWarnings("serial")
private VerticalLayout commonButtonsLayout(OMPIntimation intimation, OMPClaim claim){
	
	FormLayout emptyFormLayoutOne = new FormLayout(new Label());
	emptyFormLayoutOne.setSizeFull();
	FormLayout emptyFormLayoutTwo = new FormLayout(new Label());	
	emptyFormLayoutTwo.setSizeFull();
					
	intimationDate = new DateField("Intimation Date");
	intimationDate.setDateFormat("dd-MM-yyyy");
	intimationDate.setRequired(true);
	intimationDate.setValue(intimation.getIntimationDate());
	Date currentDate = new Date();
	intimationDate.setRangeEnd(currentDate);

	
	lossDateAndTime = new DateField("Loss Date ");
	lossDateAndTime.setDateFormat("dd-MM-yyyy");
	lossDateAndTime.setRequired(true);
	lossDateAndTime.setValue(intimation.getLossDateTime());
	txtLosstime = new DateField("Loss Time ");
	txtLosstime.setDateFormat("dd-MM-yyyy'-' HH:mm:ss");
	txtLosstime.setValue(intimation.getLossTime());
//	txtLosstime.setTimeZone(TimeZone.getTimeZone("GMT-5:30"));
	txtLosstime.setLocale(Locale.ENGLISH);
	txtLosstime.setLocale(new Locale("en", "GB"));
	txtLosstime.setResolution(Resolution.MINUTE);
	tpaIntimationno = new TextField("TPA Intimation No");
	tpaIntimationno.setValue(intimation.getTpaIntimationNumber());
	  CSValidator claimedAmtTxtValidator = new CSValidator();
	  //Vaadin8-setImmediate() tpaIntimationno.setImmediate(false);
	  tpaIntimationno.setWidth("160px");
	  tpaIntimationno.setHeight("-1px");
	  tpaIntimationno.setValidationVisible(false);
	  tpaIntimationno.setMaxLength(25);
		claimedAmtTxtValidator.extend(tpaIntimationno);
		claimedAmtTxtValidator.setRegExp("^[a-zA-Z0-9.]*$");
		claimedAmtTxtValidator.setPreventInvalidTyping(true);
	eventCode = new ComboBox("Event Code");
	eventCode.setRequired(true);
	ompEventCodesContainer = masterService.getListMasterEventByProduct(dtoBean.getPolicy().getProduct().getKey());
	eventCode.setContainerDataSource(ompEventCodesContainer);
	eventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	eventCode.setItemCaptionPropertyId("value");
	if(intimation.getEvent()!= null && intimation.getEvent().getEventCode()!= null){
		MastersEvents event = intimation.getEvent();
		SelectValue eventCodeValue = new SelectValue();
		eventCodeValue.setId(event.getKey());
		eventCodeValue.setValue(event.getEventCode());
		eventCode.setValue(eventCodeValue);
	}
	
	placeofvisit = new TextField("Place of Visit");
	placeofvisit.setNullRepresentation("");
	placeofvisit.setValue(intimation.getPlaceVisit());
	placeofvisit.setMaxLength(100);
	placeoflossOrDelay = new TextField("Place of Loss/Delay");
	placeoflossOrDelay.setValue((intimation.getPlaceLossDelay() == null)?"":intimation.getPlaceLossDelay());
	placeoflossOrDelay.setMaxLength(200);
	txtSponsorName = new TextField("Sponsor Name");
	
	cmbModeofIntimation = new ComboBox("Mode of Intimation");	
	BeanItemContainer<SelectValue> ompModeOfIntimation = masterService.getConversionReasonByValue(ReferenceTable.OMP_MODE_OF_INTIMATION);
	cmbModeofIntimation.setContainerDataSource(ompModeOfIntimation);
	cmbModeofIntimation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbModeofIntimation.setItemCaptionPropertyId("value");
	if(intimation.getIntimationMode()!= null && intimation.getIntimationMode().getKey()!= null){
		MastersValue master = rodBillentryService.getMaster(intimation.getIntimationMode().getKey());
		if(master != null){
			SelectValue modeofintimation = new SelectValue();
			modeofintimation.setId(master.getKey());
			modeofintimation.setValue(master.getValue());
			cmbModeofIntimation.setValue(modeofintimation);
			}
		}
	
//	FormLayout firstForm1 = new FormLayout(intimationDate,lossDateAndTime,tpaIntimationno,eventCode,placeofvisit,placeoflossOrDelay,txtSponsorName,cmbModeofIntimation);
	cmbInsuredName = new ComboBox("Insured Name");
	cmbInsuredName.setRequired(true);
	BeanItemContainer<SelectValue> ompInsuredName = masterService.getOMPInsuredNameList(dtoBean.getPolicy().getInsured());
	cmbInsuredName.setContainerDataSource(ompInsuredName);
	cmbInsuredName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbInsuredName.setItemCaptionPropertyId("value");
	if(intimation!= null && intimation.getInsured()!= null&& intimation.getInsured().getInsuredName()!= null){
		SelectValue insuredValue = new SelectValue();
		insuredValue.setId(intimation.getInsured().getKey());
		insuredValue.setValue(intimation.getInsured().getInsuredName());
		cmbInsuredName.setValue(insuredValue);
	}
	cmbInsuredName.addValueChangeListener(new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
			SelectValue insuredValue = (SelectValue) event.getProperty().getValue();
			if(insuredValue != null && insuredValue.getId() != null){
				fireViewEvent(OMPNewRegistrationPageViewPresenter.FIND_INSURED, insuredValue.getId());
			}
		}
	});
	
	
	txtAilmentOrLoss = new TextField("Ailment Details <b style= 'color: red'>*</b>");
	txtAilmentOrLoss.setCaptionAsHtml(true);
	txtAilmentOrLoss.setMaxLength(200);
	if(!StringUtils.isBlank(intimation.getAilmentLoss())){
		txtAilmentOrLoss.setValue(intimation.getAilmentLoss());
	}else{
		txtAilmentOrLoss.setValue("");
	}
//	txtAilmentOrLoss.setRequired(true);
	optionClaimtype = new OptionGroup("");
	optionClaimtype.addItems(getReadioButtonOptions());
	optionClaimtype.setItemCaption(true, "Cashless");
	optionClaimtype.setItemCaption(false, "Reimbursement");
	optionClaimtype.setStyleName("horizontal");
	
	if(intimation.getClaimType()!=null){
		MastersValue cmbclaimType = intimation.getClaimType();
		SelectValue claimType = new SelectValue();
		claimType.setId(cmbclaimType.getKey());
		claimType.setValue(cmbclaimType.getValue());
		if(claimType!=null){
			if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(claimType.getValue())){
				optionClaimtype.setValue(true);
			}else{
				optionClaimtype.setValue(false);
			}
		}
	}
	
	optionHospitalType = new OptionGroup("");	
	optionHospitalType.addItem("Non Medical");
	optionHospitalType.setStyleName("horizontal");
	if((intimation.getNonHospitalizationFlag()!= null && intimation.getNonHospitalizationFlag().equalsIgnoreCase("Y"))) {
		optionHospitalType.setValue("Non Medical");
	} 
	else{
		optionHospitalType.setValue(null);
	}
	
	AdmissionDate = new DateField("Admission Date");
	AdmissionDate.setDateFormat("dd-MM-yyyy");
	AdmissionDate.setValue(intimation.getAdmissionDate());
	
	DateofDischarge = new DateField("Date of Discharge");
	DateofDischarge.setDateFormat("dd-MM-yyyy");
	DateofDischarge.setValue(intimation.getDischargeDate());

	addHospital = new Button("Add Hospital");
	addHospital.setStyleName(BaseTheme.BUTTON_LINK); 
	addHospital.addClickListener(new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
//			cmbHospitalCodeOrName.unselect(cmbHospitalCodeOrName.getValue());
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
	
	//FormLayout lossDelay = new FormLayout(placeoflossOrDelay);
	FormLayout hospitalButton = new FormLayout(addHospital);
	//FormLayout dischargeDate = new FormLayout(DateofDischarge);
	
	//FormLayout hospitalButton = new FormLayout(placeoflossOrDelay,addHospital,DateofDischarge);
		
	cmbIntimatedBy = new ComboBox("Intimated By");
	BeanItemContainer<SelectValue> intimatedBy = masterService.getMastersValuebyTypeCodeOnStaatus(ReferenceTable.OMP_INTIMATED_BY);
	cmbIntimatedBy.setContainerDataSource(intimatedBy);
	cmbIntimatedBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbIntimatedBy.setItemCaptionPropertyId("value");
	if(intimation.getIntimationMode()!= null &&intimation.getIntimationMode().getKey()!= null && intimation.getIntimatedBy()!=null){
		MastersValue master = rodBillentryService.getMaster(intimation.getIntimatedBy().getKey());
		if(master != null){
			SelectValue modeofintimation = new SelectValue();
			modeofintimation.setId(master.getKey());
			modeofintimation.setValue(master.getValue());
			cmbIntimatedBy.setValue(modeofintimation);
			}
		}
	
	txtCallerOrIntimatorName = new TextField("Caller/Intimator Name");
	txtCallerOrIntimatorName.setValue(intimation.getIntimaterName());
	  CSValidator IntimatorNameValidator = new CSValidator();
	  //Vaadin8-setImmediate() txtCallerOrIntimatorName.setImmediate(false);
	  txtCallerOrIntimatorName.setWidth("160px");
	  txtCallerOrIntimatorName.setHeight("-1px");
	  txtCallerOrIntimatorName.setValidationVisible(false);
	  txtCallerOrIntimatorName.setMaxLength(100);
	  IntimatorNameValidator.extend(txtCallerOrIntimatorName);
	  IntimatorNameValidator.setRegExp("^[a-zA-Z.]*$");
	  IntimatorNameValidator.setPreventInvalidTyping(true);
	
	txtCallerContactNo = new TextField("Caller Contact No ");
	txtCallerContactNo.setValue(intimation.getCallerMobileNumber());
	CSValidator intialProamtTxtValidator = new CSValidator();
	txtInitialProvisionAmt = new TextField("Initial Provision Amt($)");
	txtInitialProvisionAmt.setRequired(true);
	txtInitialProvisionAmt.setValue((intimation.getDollarInitProvisionAmt() == null)?"":String.valueOf(intimation.getDollarInitProvisionAmt()));
	intialProamtTxtValidator.extend(txtInitialProvisionAmt);
	intialProamtTxtValidator.setRegExp("^[0-9.]*$");
	intialProamtTxtValidator.setPreventInvalidTyping(true);
	CSValidator iNRConversionRateTxtValidator = new CSValidator();
	txtINRConversionRate = new TextField("INR Conversion Rate");
	txtINRConversionRate.setValue((intimation.getInrConversionRate() == null)?"":String.valueOf(intimation.getInrConversionRate()));
	txtINRConversionRate.setRequired(true);
	iNRConversionRateTxtValidator.extend(txtINRConversionRate);
	iNRConversionRateTxtValidator.setRegExp("^[0-9.]*$");
	iNRConversionRateTxtValidator.setPreventInvalidTyping(true);
	txtINRValue = new TextField("INR Value");
	String inrValue = (intimation.getInrTotalAmount() == null)?"":intimation.getInrTotalAmount().toString();
	txtINRValue.setValue(inrValue);
	txtINRValue.setReadOnly(false);
	txtPlaceOfAccident=  new TextField("Place of Accident/Event");
	txtPlaceOfAccident.setValue((intimation.getEventPlace() == null)?"":intimation.getEventPlace());
	txtPlaceOfAccident.setMaxLength(100);
	txtlossDetails = new TextField("Loss Details");
	txtlossDetails.setValue((intimation.getLossDetails() == null)?"":intimation.getLossDetails());
	txtlossDetails.setMaxLength(100);
	FormLayout firstForm2 = new FormLayout(txtCallerOrIntimatorName,cmbModeofIntimation,cmbIntimatedBy);
	firstForm2.setSpacing(true);
	firstForm2.setMargin(true);
//	FormLayout firstForm2 = new FormLayout(cmbInsuredName,txtAilmentOrLoss,optionClaimtype,optionHospitalType,AdmissionDate, DateofDischarge);
	FormLayout firstForm1 = new FormLayout(intimationDate,tpaIntimationno,cmbInsuredName);
	firstForm1.setSpacing(true);
	firstForm1.setMargin(Boolean.TRUE);
//	firstForm1.setMargin(new MarginInfo(true, false, true, false));
	HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1,/*emptyFormLayoutOne,*/firstForm2);
	hLayout1.setSpacing(false);
	hLayout1.setHeight("100%");
	hLayout1.setCaption("Intimation Details");
//	hLayout1.setMargin(true);

	FormLayout secondForm1 = new FormLayout(eventCode,optionClaimtype,AdmissionDate,DateofDischarge,txtAilmentOrLoss,txtPlaceOfAccident,placeofvisit,lossDateAndTime,txtLosstime,placeoflossOrDelay);
//	FormLayout secondForm1 = new FormLayout(cmbIntimatedBy,txtCallerOrIntimatorName,txtCallerContactNo,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue);
	
	addListener();
	
	cmbHospitalCodeOrName = new ComboBox("Hospital Code/Name");
//	cmbHospitalCodeOrName.setRequired(true);
	loadHospitalValues();
	if(intimation!= null && intimation.getHospitalName()!= null){
		OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(intimation.getHospitalName());
		if(hospitalRec!= null && hospitalRec.getHospitalCode()!= null && hospitalRec.getName()!= null){
			String hospCodeName = hospitalRec.getHospitalCode()+"-"+hospitalRec.getName();
			SelectValue hospcName = new SelectValue();
			hospcName.setId(hospitalRec.getKey());
			hospcName.setValue(hospCodeName);
			cmbHospitalCodeOrName.setValue(hospcName);
			//GALAXYMAIN-11024 - Hosp Code & City is empty in View Intimation
//			updateHospitalDetailsInFields(intimation.getHospitalName());
			this.dtoBean.setHospitalname(intimation.getHospitalName());
			this.dtoBean.setCityName(hospitalRec.getCity());
			this.dtoBean.setCountryName(hospitalRec.getCountry());
		}
	}
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
	
	
	dummyTextField =  new TextField();
	dummyTextField.addValueChangeListener(new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
			loadHospitalValues();
			cmbHospitalCodeOrName.select(cmbHospitalCodeOrName.getValue());
		}
	});

	txtCity = new TextField("City");
	// Auto filling city value
	String cityName = (intimation.getCityName() == null)?"":intimation.getCityName();
	txtCity.setValue(cityName);
//	txtCity.setRequired(true);
	/*txtCountry = new TextField("Country");
	String countryName = (this.dtoBean.getCountryName() == null)?"":this.dtoBean.getCountryName();
	txtCountry.setValue(countryName);*/
	
	txtCountry = new ComboBox("Country");
	BeanItemContainer<SelectValue> country = masterService.getCountryValue();
	txtCountry.setContainerDataSource(country);
	txtCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	txtCountry.setItemCaptionPropertyId("value");
	if(intimation.getCountryId()!=null){
		SelectValue countryValueByKey = masterService.getCountryValueByKey(intimation.getCountryId());
		if(countryValueByKey!= null && countryValueByKey.getValue()!= null)
			txtCountry.setValue(countryValueByKey);
	}
//	txtCountry.setRequired(true);
	txtRemarks = new TextArea("Remarks");
	TextField dummyfield = new TextField();
	dummyfield.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummyfield.setReadOnly(true);
	FormLayout hospitalName = new FormLayout(cmbHospitalCodeOrName);
	HorizontalLayout hLayout = new HorizontalLayout(hospitalName, hospitalButton);
	hLayout.setSpacing(true);
	hLayout.setMargin(true);
	
//	R1276 - Amount Claimed field introduced in Loss Details Section.
	CSValidator amtClaimedTxtValidator = new CSValidator();
	txtClaimedAmount = new TextField("Amount Claimed ($)");
	txtClaimedAmount.setValue((claim.getClaimedAmount() == null)?"":String.valueOf(claim.getClaimedAmount()));
	txtClaimedAmount.setRequired(true);
	amtClaimedTxtValidator.extend(txtClaimedAmount);
	amtClaimedTxtValidator.setRegExp("^[0-9.]*$");
	amtClaimedTxtValidator.setPreventInvalidTyping(true);
	
	FormLayout secondForm2 = new FormLayout(dummyfield,optionHospitalType,addHospital,cmbHospitalCodeOrName,txtCity,txtCountry,txtlossDetails,placeofvisit,txtClaimedAmount);
//	FormLayout secondForm2 = new FormLayout(hLayout,txtCity,txtCountry,txtRemarks);
//	dummyTextField.setVisible(Boolean.FALSE);
	FormLayout provisionLayout = new FormLayout(txtInitialProvisionAmt,txtINRValue);
	provisionLayout.setSpacing(true);
	provisionLayout.setMargin(true);
	
//	FormLayout lossdetailsLayout = new FormLayout(lossDateAndTime,txtLosstime,placeoflossOrDelay);
//	lossdetailsLayout.setSpacing(true);
//	lossdetailsLayout.setMargin(true);
	FormLayout provisionLayout1 = new FormLayout(txtINRConversionRate);
//	FormLayout provisionLayout2 = new FormLayout(txtINRValue);
//	FormLayout lossdetailsLayout1 = new FormLayout(txtlossDetails,placeofvisit);
//	lossdetailsLayout1.setSpacing(true);
//	lossdetailsLayout1.setMargin(true);
	
	HorizontalLayout hLayuot2 = new HorizontalLayout(secondForm1,emptyFormLayoutTwo,secondForm2);
	hLayuot2.setSpacing(false);
	hLayuot2.setHeight("100%");
	hLayuot2.setMargin(true);
	hLayuot2.setCaption("Loss Details");
	dynamicFrmLayout = new FormLayout();
	dynamicFieldsLayout = new VerticalLayout();
	dynamicFieldsLayout.addComponent(dynamicFrmLayout);
//	HorizontalLayout hLayoutlossdetails = new HorizontalLayout(lossdetailsLayout);
	HorizontalLayout provisnLayout = new HorizontalLayout(provisionLayout,provisionLayout1/*provisionLayout2*/);
	provisnLayout.setCaption("Provision Details");
	provisionLayout.setWidth("100%");

	HorizontalLayout suggestRejecionAndRegisterButtonLayout = BuildClaimRegisterAndSuggestRejectionBtnLayout();
	VerticalLayout firstVlayout = new VerticalLayout(hLayout1,hLayuot2);
	firstVlayout.setSizeFull();
	firstVlayout.setSpacing(false);
	
	//R1276 - Introduced  Intimation Remarks
	txtaintimationRemarks = new TextArea("Intimation Remarks");
	txtaintimationRemarks.setValue((intimation.getRemarks() == null)?"":intimation.getRemarks());
	txtaintimationRemarks.setRows(4);
	txtaintimationRemarks.setColumns(25);
	handleTextAreaPopup(txtaintimationRemarks,null);
	FormLayout remarksForms = new FormLayout(txtaintimationRemarks);
	
	HorizontalLayout hLayoutRmrks = new HorizontalLayout(remarksForms);
	hLayoutRmrks.setSizeFull();
	
	VerticalLayout secondlayout = new VerticalLayout(provisnLayout);
	VerticalLayout mainLayout = new VerticalLayout(firstVlayout, hLayoutRmrks, secondlayout);
	mainLayout.addComponent(dynamicFieldsLayout);
	mainLayout.setWidth("100%");
	mainLayout.setHeight("100%");
	mainLayout.setMargin(false);

	mainLayout.addComponent(suggestRejecionAndRegisterButtonLayout);
	mainLayout.setComponentAlignment(suggestRejecionAndRegisterButtonLayout,Alignment.BOTTOM_RIGHT);
	//mainLayout.setMargin(new MarginInfo(false, true, false, true));
	mainLayout.setSpacing(false);
	// hiding field based on eventCode.....
	eventCodeValueChangeAction((SelectValue)eventCode.getValue());
	
	return mainLayout;
	
}

private HorizontalLayout BuildClaimRegisterAndSuggestRejectionBtnLayout() {
	registarButton = new Button();
	registarButton.setCaption("Register");
	//Vaadin8-setImmediate() registarButton.setImmediate(true);
	registarButton.setWidth("-1px");
	registarButton.setHeight("-1px");
	registarButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	registarButton.addClickListener(new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			claimDto.setStatusName("Registered");
			fireViewEvent(OMPNewRegistrationPageViewPresenter.CLICK_REGISTER_BUTTON,
					null);
		}
	});

	rejectButton = new Button();
	rejectButton.setCaption("Rejection");
	//Vaadin8-setImmediate() rejectButton.setImmediate(true);
	rejectButton.setWidth("-1px");
	rejectButton.setHeight("-1px");
	rejectButton.setStyleName(ValoTheme.BUTTON_DANGER);
	
	rejectButton.addClickListener(new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			claimDto.setStatusName("SuggestRejection");
			fireViewEvent(OMPNewRegistrationPageViewPresenter.SUGGEST_REJECTION,
					null);
		}
	});
	HorizontalLayout submitAndSuggestBtnLayout = new HorizontalLayout(
			registarButton, rejectButton);
	submitAndSuggestBtnLayout.setSpacing(true);
	return submitAndSuggestBtnLayout;
}

@Override
public void registerClicked() {
	registerClick();
}

public void registerClick() {
	/*Boolean hasError = false;
	eMsg ="";
	
	if(dtoBean.getCurrentBalanceSI() != null && dtoBean.getCurrentBalanceSI() < 0){
		Notification.show("ERROR",
				"Balance Sum Insured is not sufficient to proceed the Registration.",
				Notification.Type.ERROR_MESSAGE);
		registarButton.setEnabled(false);
		if (dynamicFieldsLayout.getComponentCount() > 0) {
			dynamicFieldsLayout.removeAllComponents();
		}
		hasError = true;
		return;
	}else {
		registarButton.setEnabled(true);
	}
	
	if(!claimDto.getSuggestRejection() && !isProceedfurther) {
		Notification.show("ERROR",
				"Rejection only applicable for this policy because of cheque realization is dishonored OR Cheque status is pending which ever applicable.",
				Notification.Type.ERROR_MESSAGE);
		registarButton.setEnabled(false);
		if (dynamicFieldsLayout.getComponentCount() > 0) {
			dynamicFieldsLayout.removeAllComponents();
		}
		hasError = true;
		return;
	} else {
		registarButton.setEnabled(true);
	}
	
	if(!hasError){*/
		claimDto.setSuggestRejection(false);
		if (dynamicFieldsLayout != null) {
			if (dynamicFieldsLayout.getComponentCount() > 0) {
				dynamicFieldsLayout.removeAllComponents();
			}

		}
		dynamicFieldsLayout = buildRegisterFields();
	/*}else{
		return;
	}*/
}

private VerticalLayout buildRegisterFields() {
	claimDto.setSuggestRejection(false);
		if (registrationRemarksTxta == null) {
			registrationRemarksTxta = new TextArea("Registration Remarks");
			//registrationRemarksTxta.setRequired(true);//
			registrationRemarksTxta.setValidationVisible(false);//
			registrationRemarksTxta.setMaxLength(4000);
			registrationRemarksTxta.setRows(4);
			registrationRemarksTxta.setColumns(25);
			handleTextAreaPopup(registrationRemarksTxta,null);
			registrationRemarksTxta.setRequiredError("Please Provide Suggesion Remarks for Registration");
//			mandatoryFields.add(registrationRemarksTxta);
			if(registrationRemarksTxta!= null && registrationRemarksTxta.getValue()!= null){
//				dtoBean.setRemarks(registrationRemarksTxta.getValue());
				claimDto.setSuggestRejection(false);
			}
		}

		if (suggestRejectionTxta != null) {
			dynamicFrmLayout.removeComponent(suggestRejectionTxta);
			suggestRejectionTxta = null;

//			claimDto.setSuggestRejection(false);
		}

		dynamicFrmLayout.addComponent(registrationRemarksTxta);
		dynamicFieldsLayout.addComponent(dynamicFrmLayout);

	return dynamicFieldsLayout;

}

@Override
public void suggestRejectionClicked() {
	
	suggestRejectionClick();
}

public void suggestRejectionClick() {		
	claimDto.setSuggestRejection(true);
	if (dynamicFieldsLayout != null) {
		if (dynamicFieldsLayout.getComponentCount() > 0) {
			dynamicFieldsLayout.removeAllComponents();
		}
	}
		dynamicFieldsLayout
				.addComponent(buildSuggestRejectionFields(dynamicFrmLayout));
}

private FormLayout buildSuggestRejectionFields(FormLayout dynamicFrmLayout) {
	claimDto.setSuggestRejection(true);

	if (dynamicFrmLayout == null) {
		dynamicFrmLayout = new FormLayout();
	}

	if (dynamicFrmLayout.getComponentCount() > 0) {
		if (registrationRemarksTxta != null) {
			dynamicFrmLayout.removeComponent(registrationRemarksTxta);
			registrationRemarksTxta.setValue("");
		}
	}

	if (suggestRejectionTxta != null) {
		dynamicFrmLayout.removeComponent(suggestRejectionTxta);
	}
//	suggestRejectionTxta = new TextArea("Suggested Rejection Remarks");
	suggestRejectionTxta = new ComboBox("Rejection Remarks");
	BeanItemContainer<SelectValue> ompRejecIntimation = masterService.getConversionReasonByValue(ReferenceTable.OMP_INTIMATION_REJCTION);
	suggestRejectionTxta.setContainerDataSource(ompRejecIntimation);
	suggestRejectionTxta.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	suggestRejectionTxta.setItemCaptionPropertyId("value");
	suggestRejectionTxta.setRequired(true);
	suggestRejectionTxta.setValidationVisible(false);
//	suggestRejectionTxta.setMaxLength(200);

//	dtoBean.setRemarks(suggestRejectionTxta.getValue().toString());
	//Vaadin8-setImmediate() suggestRejectionTxta.setImmediate(false);
	suggestRejectionTxta
			.setRequiredError("Please Provide Suggesion Remarks for Rejection");
//	mandatoryFields.add(suggestRejectionTxta);
	dynamicFrmLayout.addComponent(suggestRejectionTxta);

	return dynamicFrmLayout;
}

@Override
public void submitClaimRegister(ClaimDto claimDto, Boolean isProceedFurther, String vb64Status) {
		submitRegistration(claimDto, isProceedFurther, vb64Status);
}

public void submitRegistration(ClaimDto claimDto, Boolean isProceedfurther, String vb64Status) {

	// Check Whether all the mandatory fields are entered.
	Boolean hasError = false;
	hasError = validatePage();

	FormLayout frmLayout = dynamicFrmLayout;

	if (claimDto.getStatusName() == null|| claimDto.getStatusName() == "") {
		hasError = true;
		Notification
				.show("ERROR",
						"Please Click Register Or Suggest Reject Button before Submitting the Claim. ",
						Notification.Type.ERROR_MESSAGE);
		return;
	}

	if (suggestRejectionTxta != null
			&& suggestRejectionTxta.getValue() == null) {
		Notification
				.show("ERROR",
						"Please Enter Manadatory Fields by Clicking Register Or Suggest Reject Button. ",
						Notification.Type.ERROR_MESSAGE);
		return;
	}
	
	if(dtoBean.getCurrentBalanceSI() != null && dtoBean.getCurrentBalanceSI() < 0){
		Notification.show("ERROR",
				"Balance Sum Insured is not sufficient to proceed the Registration.",
				Notification.Type.ERROR_MESSAGE);
		registarButton.setEnabled(false);
		if (dynamicFieldsLayout.getComponentCount() > 0) {
			dynamicFieldsLayout.removeAllComponents();
		}
		hasError = true;
		return;
	}else {
		registarButton.setEnabled(true);
	}
	
	if(!claimDto.getSuggestRejection() && !isProceedfurther) {
		String message = "";
		if(vb64Status.equals("D")){
			message = "Rejection only applicable for this policy because of cheque realization is dishonored";
		}else if(vb64Status.equals("P")){
			message = "Cheque status is pending";
		}else{
			
		}
		Notification.show("ERROR", message, Notification.Type.ERROR_MESSAGE);
		registarButton.setEnabled(false);
		if (dynamicFieldsLayout.getComponentCount() > 0) {
			dynamicFieldsLayout.removeAllComponents();
		}
		hasError = true;
		return;
	} else {
		registarButton.setEnabled(true);
	}

	if (frmLayout.getComponentCount() == 3 && suggestRejectionTxta != null
			&& suggestRejectionTxta.getValue() == null) {
		suggestRejectionTxta.setValidationVisible(true);
		hasError = true;
	}

	if (suggestRejectionTxta != null) {
		suggestRejectionTxta.setValidationVisible(false);
	}

	if (hasError) {
		return;
	}
	if (!hasError) {
		claimDto.setNewIntimationDto(newIntimationDto);
		claimDto.setStatusDate(new Timestamp(System.currentTimeMillis()));
		
		claimDto.setRegistrationRemarks((registrationRemarksTxta == null || registrationRemarksTxta.getValue() == null) ? null : registrationRemarksTxta.getValue());
//		SelectValue eventCodeMast = new SelectValue();
//		claimDto.setSuggestedRejectionRemarks((suggestRejectionTxta == null || suggestRejectionTxta.getValue() == null) ? null : suggestRejectionTxta.getValue());
		if(suggestRejectionTxta!= null){
			claimDto.setSuggestedRejectionRemarks(suggestRejectionTxta.getValue().toString());
		}
		doSaveOrSubmitIntimation();
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		dtoBean.setUsername(userName);
		dtoBean.setClaimDto(claimDto);
		dtoBean.setIntimationDto(newIntimationDto);
		
		if(txtClaimedAmount.getValue() != null){
			dtoBean.setIntimationClmAmount(Double.parseDouble(txtClaimedAmount.getValue()));
		}else{
			dtoBean.setIntimationClmAmount(null);
		}
		if(txtaintimationRemarks.getValue() != null){
			dtoBean.setIntimationRemarks(txtaintimationRemarks.getValue());
		}else{
			dtoBean.setIntimationRemarks(null);
		}
		
		fireViewEvent(OMPNewRegistrationPageViewPresenter.SUBMIT_INTIMATION, dtoBean);
		fireViewEvent(OMPNewRegistrationPageViewPresenter.SUBMIT_CLAIM_REGISTRATION, dtoBean, claimDto);
	}
}



public void enableDisableNonHospitalisationFields(boolean enable){
	
	if(!enable){
		cmbHospitalCodeOrName.setValue(null);
		txtCity.setValue("");
		txtCountry.setValue("");
		AdmissionDate.setValue(null);
		DateofDischarge.setValue(null);
	}
	cmbHospitalCodeOrName.setEnabled(enable);
	addHospital.setEnabled(enable);
	txtCity.setEnabled(enable);
	txtCountry.setEnabled(enable);
	AdmissionDate.setEnabled(enable);
	DateofDischarge.setEnabled(enable);		
}

protected void addListener() {
	
	
//	optionHospitalType.addValueChangeListener(new Property.ValueChangeListener() {
//		
//		@Override
//		public void valueChange(ValueChangeEvent event) {
//			String hospitalization = optionHospitalType.getValue() != null ? optionHospitalType.getValue().toString() : "";
//			String clmType = optionClaimtype.getValue() != null ? optionClaimtype.getValue().toString() : "";
//			if(!hospitalization.isEmpty() && ("Non Hospitalisation").equalsIgnoreCase(hospitalization) && !clmType.isEmpty() && ("Reimbursement").equalsIgnoreCase(clmType)){
//				enableDisableNonHospitalisationFields(false);
//			}
//			else{
//				enableDisableNonHospitalisationFields(true);
//			}
//		}
//	});
	
	  txtInitialProvisionAmt
				.addBlurListener(new BlurListener() {
					
					@Override
					public void blur(BlurEvent event) {
						TextField txtInitialProvisionAmt1 = (TextField) event.getComponent();
						if(txtInitialProvisionAmt1!=null && txtInitialProvisionAmt1.getValue()!=null && txtINRConversionRate!=null && txtINRConversionRate.getValue()!=null){
							String initialProvisionAmt = txtInitialProvisionAmt1.getValue();
							String iNRConversionRate = txtINRConversionRate.getValue();
							double calculateInrValue = calculateInrValue(initialProvisionAmt, iNRConversionRate);
							calculateInrValue = Math.round( calculateInrValue * 100.0 ) / 100.0;
							BigDecimal bigDecimal = new BigDecimal(calculateInrValue);
							bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
							txtINRValue.setReadOnly(false);
							txtINRValue.setValue(bigDecimal.toString());
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
					calculateInrValue = Math.round( calculateInrValue * 100.0 ) / 100.0;
					BigDecimal bigDecimal = new BigDecimal(calculateInrValue);
					bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
					txtINRValue.setReadOnly(false);
					txtINRValue.setValue(bigDecimal.toString());
					txtINRValue.setReadOnly(true);
				}
				
			}

		});
	  if(optionClaimtype!=null){/*
	  optionClaimtype.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7455756225751111662L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				OptionGroup totalValue = (OptionGroup) event.getProperty();
				if(totalValue.getValue()!=null && totalValue.getValue().equals("Cashless")){
						if(optionHospitalType!=null){
							optionHospitalType.setValue("Medical");
						}
				}
				if(totalValue.getValue()!=null && totalValue.getValue().equals("Reimbursement")){
					if(optionHospitalType!=null){
						optionHospitalType.setValue("Non Medical");
					}
			}
				
			}
		});
	  */}
	  if(DateofDischarge != null){
		  DateofDischarge.addValueChangeListener( new ValueChangeListener( ) {
          
          /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
          public void valueChange( ValueChangeEvent event ) {
              
              if(DateofDischarge.getValue( )!=null && AdmissionDate.getValue( )!=null && (AdmissionDate.getValue( )).after( DateofDischarge.getValue( ) ))
              {
            	  DateofDischarge.setValue(null);
                 showErrorMessage( "Date of Discharge should not be lesser than the Admission Date" );
//                  throw new Property.ConversionException("From date must be less than To date");
              }
//              else {
//                  expiryDateTo.setComponentError(null);
//              }

          }
      } );
	  }
	  
	  if(AdmissionDate != null){
		  AdmissionDate.addValueChangeListener( new ValueChangeListener( ) {
          
          /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
          public void valueChange( ValueChangeEvent event ) {
		
			String str1 = (AdmissionDate.getValue() != null && !SHAUtils.isOMPDateOfIntimationWithPolicyRange(dtoBean.getPolicy().getPolicyFromDate(), dtoBean.getPolicy().getPolicyToDate(), AdmissionDate.getValue()) ? "Admission Date is not with in policy period.</br>" : "");
			
			String str2 = null;
			if(AdmissionDate.getValue()!=null && intimationDate.getValue()!=null){
				Date value = AdmissionDate.getValue();
				Date value2 = intimationDate.getValue();
				if(value.after(value2)){
					str2 = "Admission Date should not be greater than intimation date";
				}
			}
			
			if(!SHAUtils.isEmpty(str1)){
				//AdmissionDate.setValue(null);
				showErrorMessage(str1);
			}
			if(!SHAUtils.isEmpty(str2)){
				//AdmissionDate.setValue(null);
				showErrorMessage(str2);
			}
		if(DateofDischarge.getValue( )!=null && AdmissionDate.getValue( )!=null && (AdmissionDate.getValue( )).after( DateofDischarge.getValue( ) )){
			AdmissionDate.setValue(null);
//		}
	}
          }
      } );
	  }
	  
//	  
	    if(lossDateAndTime != null){
	    	lossDateAndTime.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;					///  we have to do

			@Override
	          public void valueChange( ValueChangeEvent event ) {
				
				String str1 = (lossDateAndTime.getValue() != null && !SHAUtils.isOMPDateOfIntimationWithPolicyRange(dtoBean.getPolicy().getPolicyFromDate(), dtoBean.getPolicy().getPolicyToDate(), lossDateAndTime.getValue()) ? "Loss Date-Time is not with in policy period.</br>" : "");
				
				if(!SHAUtils.isEmpty(str1)){
//					lossDateAndTime.setValue(null);
					showErrorMessage(str1);
				}
				if(intimationDate.getValue( )!=null && lossDateAndTime.getValue()!=null && (lossDateAndTime.getValue()).after( intimationDate.getValue() )){
					showErrorMessage( "Loss Date should not be grater than the Intimation Date" );
//					lossDateAndTime.setValue(null);
				}
	          }
	      });
	  }
	    
	    if(intimationDate != null){
	    	intimationDate.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	          public void valueChange( ValueChangeEvent event ) {
			
				String str2 = null;
				if(AdmissionDate.getValue()!=null && intimationDate.getValue()!=null){
					Date value = AdmissionDate.getValue();
					Date value2 = intimationDate.getValue();
					if(value.after(value2)){
						str2 = "Admission Date should not be greater than intimation date";
					}
				}
				if(!SHAUtils.isEmpty(str2)){
					//intimationDate.setValue(null);
					showErrorMessage(str2);
				}
			if(lossDateAndTime.getValue( )!=null && intimationDate.getValue( )!=null && (intimationDate.getValue( )).before( lossDateAndTime.getValue( ) )){
				intimationDate.setValue(null);
				showErrorMessage( "Please check Intimation Date & Loss Date " );
			}
        }
	});
	   }
	    
	    if(optionClaimtype!=null){
	  	  optionClaimtype.addValueChangeListener(new ValueChangeListener() {
	  			private static final long serialVersionUID = 7455756225751111662L;

	  			@Override
	  			public void valueChange(ValueChangeEvent event) {
	  				OptionGroup totalValue = (OptionGroup) event.getProperty();
	  				if(totalValue.getValue()!=null && totalValue.getValue().equals("Cashless")){
	  					AdmissionDate.setVisible(Boolean.TRUE);
						DateofDischarge.setVisible(Boolean.TRUE);
						txtAilmentOrLoss.setVisible(Boolean.TRUE);
						txtPlaceOfAccident.setVisible(Boolean.TRUE);
						placeofvisit.setVisible(Boolean.TRUE);
						cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
						txtCity.setVisible(Boolean.TRUE);
						txtCountry.setVisible(Boolean.TRUE);
						addHospital.setVisible(Boolean.TRUE);
						lossDateAndTime.setVisible(Boolean.FALSE);
	  					txtLosstime.setVisible(Boolean.FALSE);
	  					placeoflossOrDelay.setVisible(Boolean.FALSE);
	  					txtlossDetails.setVisible(Boolean.FALSE);
//	  					placeofvisit.setVisible(false);
	  				}
	  				if(totalValue.getValue()!=null && totalValue.getValue().equals("Reimbursement")){
	  					lossDateAndTime.setVisible(Boolean.FALSE);
	  					txtLosstime.setVisible(Boolean.FALSE);
	  					placeoflossOrDelay.setVisible(Boolean.FALSE);
	  					txtlossDetails.setVisible(Boolean.FALSE);
	  					placeofvisit.setVisible(Boolean.TRUE);
	  					AdmissionDate.setVisible(Boolean.TRUE);
						DateofDischarge.setVisible(Boolean.TRUE);
						txtAilmentOrLoss.setVisible(Boolean.TRUE);
						txtPlaceOfAccident.setVisible(Boolean.TRUE);
//						placeofvisit.setVisible(false);
						cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
						txtCity.setVisible(Boolean.TRUE);
						txtCountry.setVisible(Boolean.TRUE);
						addHospital.setVisible(Boolean.TRUE);
	  				}
//	  				if(totalValue.getValue()!=null && totalValue.getValue().equals("Reimbursement")){
//	  					if(optionHospitalType!=null){
//	  						optionHospitalType.setValue("Non Hospitalisation");
//	  					}
//	  			}
	  				
	  			}
	  		});
	  	  }
	    
	    
	    if(optionHospitalType!=null){
	    	optionHospitalType.addValueChangeListener(new ValueChangeListener() {
		  			private static final long serialVersionUID = 7455756225751111662L;

		  			@Override
		  			public void valueChange(ValueChangeEvent event) {
		  				OptionGroup totalValue = (OptionGroup) event.getProperty();
		  				if(totalValue.getValue()!=null && totalValue.getValue().equals("Non Medical")){
		  					lossDateAndTime.setVisible(Boolean.TRUE);
		  					txtLosstime.setVisible(Boolean.TRUE);
		  					placeoflossOrDelay.setVisible(Boolean.TRUE);
		  					txtlossDetails.setVisible(Boolean.TRUE);
		  					placeofvisit.setVisible(Boolean.TRUE);
		  					AdmissionDate.setVisible(Boolean.FALSE);
							DateofDischarge.setVisible(Boolean.FALSE);
							txtAilmentOrLoss.setVisible(Boolean.FALSE);
							txtPlaceOfAccident.setVisible(Boolean.FALSE);
//							placeofvisit.setVisible(false);
							cmbHospitalCodeOrName.setVisible(Boolean.FALSE);
							txtCity.setVisible(Boolean.FALSE);
							txtCountry.setVisible(Boolean.FALSE);
							addHospital.setVisible(Boolean.FALSE);
							if(optionHospitalType!=null){
//								optionClaimtype.setValue("Reimbursement");
		  						optionClaimtype.setEnabled(Boolean.TRUE);
		  						optionClaimtype.setValue(null);
		  				}else{
		  						optionClaimtype.setEnabled(true);
		  					}
		  				}
		  				if(totalValue.getValue()!= null && totalValue.getValue().equals("Medical")){/*
		  					AdmissionDate.setVisible(Boolean.TRUE);
							DateofDischarge.setVisible(Boolean.TRUE);
							txtAilmentOrLoss.setVisible(Boolean.TRUE);
							txtPlaceOfAccident.setVisible(Boolean.TRUE);
							placeofvisit.setVisible(Boolean.TRUE);
							cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
							txtCity.setVisible(Boolean.TRUE);
							txtCountry.setVisible(Boolean.TRUE);
							addHospital.setVisible(Boolean.TRUE);
							lossDateAndTime.setVisible(Boolean.FALSE);
		  					txtLosstime.setVisible(Boolean.FALSE);
		  					placeoflossOrDelay.setVisible(Boolean.FALSE);
		  					txtlossDetails.setVisible(Boolean.FALSE);
		  					if(optionHospitalType!=null){
								optionClaimtype.setValue("Cashless");
								optionClaimtype.setEnabled(Boolean.TRUE);
							}
		  				*/}

		  			}
	    	});
	    }
	    
	    if(eventCode != null){
	    	eventCode.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	          public void valueChange( ValueChangeEvent event ) {
				
				SelectValue eventSelectValue = (SelectValue) event.getProperty().getValue();
				if(eventSelectValue != null && eventSelectValue.getValue()!=null && eventSelectValue.getId()!= null) {
						MastersEvents events = masterService.getEventTypeByKey(eventSelectValue.getId());
						DBCalculationService dbCalculationService = new DBCalculationService();
						if(dtoBean.getPolicy()!= null && dtoBean.getPolicy().getKey()!= null&&events.getEventCode()!= null){
							Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(dtoBean.getPolicy().getKey(), events.getEventCode());
							Map<String, Double> balanceSIMap = dbCalculationService.getOmpBalanceSIByPolicyKey(dtoBean.getPolicy().getKey(), events.getEventCode());
							Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
							Double CurrBalanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
							
							if(balanceSI!= null){
								dtoBean.setBalanceSI(balanceSI);
							}
							if(CurrBalanceSI!= null){
								dtoBean.setCurrentBalanceSI(CurrBalanceSI);
							}
						}
						if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& dtoBean.getBalanceSI()!=null ){
							if(!StringUtils.isBlank(txtInitialProvisionAmt.getValue())){
								double provisionAmt = Double.parseDouble( txtInitialProvisionAmt.getValue());
								if(provisionAmt > dtoBean.getBalanceSI()){
									showErrorMessage( "Initial provision is greater than the Balance Sum Insured = "+ dtoBean.getBalanceSI() );
									txtInitialProvisionAmt.setValue("");
								}
							}
							
						}
						if(events!= null && events.getProcessType()!= null){
							if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C") || events.getProcessType().equalsIgnoreCase("R")){
								if(optionHospitalType!= null){
									optionHospitalType.setValue(null);
									optionHospitalType.setEnabled(false);
									
									AdmissionDate.setVisible(Boolean.TRUE);
									DateofDischarge.setVisible(Boolean.TRUE);
									txtAilmentOrLoss.setVisible(Boolean.TRUE);
									txtPlaceOfAccident.setVisible(Boolean.TRUE);
									placeofvisit.setVisible(Boolean.TRUE);
									cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
									txtCity.setVisible(Boolean.TRUE);
									txtCountry.setVisible(Boolean.TRUE);
									addHospital.setVisible(Boolean.TRUE);
									lossDateAndTime.setVisible(Boolean.FALSE);
				  					txtLosstime.setVisible(Boolean.FALSE);
				  					placeoflossOrDelay.setVisible(Boolean.FALSE);
				  					txtlossDetails.setVisible(Boolean.FALSE);
//				  					dummyTextField.setVisible(Boolean.FALSE);
				  					if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C")){
				  						optionClaimtype.setEnabled(Boolean.TRUE);
										optionClaimtype.setValue("Cashless");
									}
				  					if(events.getProcessType().equalsIgnoreCase("R")){
				  						optionClaimtype.setEnabled(Boolean.TRUE);
										optionClaimtype.setValue("Reimbursement");
				  					}
								}
							}
							
							if(events!= null && events.getProcessType()!= null){
								if(events.getProcessType().equalsIgnoreCase("N")){
									if(optionHospitalType!= null){
										optionHospitalType.setEnabled(Boolean.TRUE);
										optionHospitalType.setValue("Non Medical");
										optionHospitalType.setEnabled(Boolean.TRUE);
										lossDateAndTime.setVisible(Boolean.TRUE);
					  					txtLosstime.setVisible(Boolean.FALSE);
					  					placeoflossOrDelay.setVisible(Boolean.TRUE);
					  					txtlossDetails.setVisible(Boolean.TRUE);
					  					placeofvisit.setVisible(Boolean.TRUE);
					  					AdmissionDate.setVisible(Boolean.FALSE);
										DateofDischarge.setVisible(Boolean.FALSE);
										txtAilmentOrLoss.setVisible(Boolean.FALSE);
										txtPlaceOfAccident.setVisible(Boolean.FALSE);
//										placeofvisit.setVisible(false);
										cmbHospitalCodeOrName.setVisible(Boolean.FALSE);
										txtCity.setVisible(Boolean.FALSE);
										txtCountry.setVisible(Boolean.FALSE);
										addHospital.setVisible(Boolean.FALSE);
//										dummyTextField.setVisible(Boolean.FALSE);
										if(optionHospitalType!=null){
//											optionClaimtype.setValue("Reimbursement");
					  						optionClaimtype.setEnabled(Boolean.FALSE);
					  						optionClaimtype.setValue(null);
					  				}else{
					  						optionClaimtype.setEnabled(true);
					  					}
					  				}
								}
							}
							if(events!= null && events.getEventCode().equalsIgnoreCase("OMP-CVR-011") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-009") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-012") ||
									 events.getEventCode().equalsIgnoreCase("CFT-CVR-010") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-015") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-012")){
								
								txtLosstime.setVisible(Boolean.TRUE);
								if(StringUtils.isBlank(String.valueOf(lossDateAndTime.getValue()))){
//			  						hasError = true;
//			  						eMsg = eMsg + "Please Provide lossDate. </br>";
			  					}
							}
//							if(description.equalsIgnoreCase(" Emergency Medical Expenses M1")){
//								
//								showErrorMessage("The selected event code - Pertaining to PA processing  cannot be processed in OMP Module");
//								
//							}
						}

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
				
				if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& dtoBean.getBalanceSI()!=null ){
					double provisionAmt = Double.parseDouble( txtInitialProvisionAmt.getValue());
					if(provisionAmt > dtoBean.getBalanceSI()){
						
						showErrorMessage( "Initial provision is greater than the Balance Sum Insured = "+ dtoBean.getBalanceSI() );
						txtInitialProvisionAmt.setValue("");
					}
					
				}
			
			}
				
    	});
	    }
	    
	    if(txtLosstime != null){
	    	txtLosstime.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
	    	 * 
	    	 */
	    	private static final long serialVersionUID = 1L;

	    	@Override
	          public void valueChange( ValueChangeEvent event ) {
	    		
	    		if(txtLosstime.getValue()!= null){
	    			Notification.show("Alert",
	    	                  "Check the number of hours delay entered confirms the policy condition",
	    	                  Notification.Type.ERROR_MESSAGE);
	    		}
	              
	    	}

	      });
	      }
	    
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
	this.dtoBean.setHospitalname(hospitalCodeOrName);
	if(StringUtils.isNotBlank(hospitalCodeOrName)){
		OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(hospitalCodeOrName);
		if(hospitalRec != null){
			this.dtoBean.setCityName(hospitalRec.getCity());
			this.dtoBean.setCountryName(hospitalRec.getCountry());
			txtCity.setValue(hospitalRec.getCity());
			
			if(hospitalRec.getCountryId()!=null){
				SelectValue countryValueByKey = masterService.getCountryValueByKey(hospitalRec.getCountryId());
				if(countryValueByKey!= null && countryValueByKey.getValue()!= null){
					txtCountry.setValue(countryValueByKey);
				}
			}
//			txtCountry.setValue(hospitalRec.getCountry());
		}else{
			this.dtoBean.setCityName("");
			this.dtoBean.setCountryName("");
			txtCity.setValue("");
			txtCountry.setValue("");
		}
	}else{
		this.dtoBean.setCityName("");
		this.dtoBean.setCountryName("");
		txtCity.setValue("");
		txtCountry.setValue("");
	}
	
}

@SuppressWarnings("serial")
public VerticalLayout addFooterButtons(){
	
	HorizontalLayout buttonsLayout = new HorizontalLayout();
	
	Button	cancelButton = new Button("Cancel");
	cancelButton.addClickListener(new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			fireViewEvent(OMPNewRegistrationPageViewPresenter.CANCEL_INTIMATION,dtoBean);
		}
	});

	Button	saveButton = new Button("Save");
	saveButton.addClickListener(new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if(!validatePage()){
				doSaveOrSubmitIntimation();
				fireViewEvent(OMPNewRegistrationPageViewPresenter.SAVE_INTIMATION,dtoBean);
				
			}else{
				showErrorMessage(eMsg);
			}					
		}
	});
	saveButton.setVisible(false);// to enable save button remove this line of code.

	final Button	submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					if(registrationRemarksTxta!= null &&registrationRemarksTxta.getValue()!= null || suggestRejectionTxta!= null && suggestRejectionTxta.getValue()!= null){
						fireViewEvent(OMPNewRegistrationPageViewPresenter.SUBMIT_CLAIM_CLICK, claimDto);
					} else {
						Notification
								.show("ERROR",
										"Please Click Register Or Suggest Reject Button before Submitting the Claim. ",
										Notification.Type.ERROR_MESSAGE);
					}
				}else{
					showErrorMessage(eMsg);
				}			
			}
		});
	
		buttonsLayout.addComponents(cancelButton,saveButton,submitButton);
		buttonsLayout.setSpacing(true);
		VerticalLayout	btnLayout = new VerticalLayout(buttonsLayout);
		btnLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
		
		return btnLayout;
	}


public void doSaveOrSubmitIntimation(){
	this.dtoBean.setIntimationdate(intimationDate.getValue());
	this.dtoBean.setLossDateTime(lossDateAndTime.getValue());
	this.dtoBean.setTpaIntimationNumber(tpaIntimationno.getValue());
	this.dtoBean.setEventCode((SelectValue)eventCode.getValue());
	this.dtoBean.setPlaceOfVisit(placeofvisit.getValue());
	this.dtoBean.setPlaceOfLossDelay(placeoflossOrDelay.getValue());
	this.dtoBean.setSponsorName(txtSponsorName.getValue());
	this.dtoBean.setIntimaticmode((SelectValue)cmbModeofIntimation.getValue());
	this.dtoBean.setInsuredName(cmbInsuredName.getValue().toString());
	this.dtoBean.setAilmentLoss(txtAilmentOrLoss.getValue());
	this.dtoBean.setLobId(ReferenceTable.OMP_LOB_KEY);
	if(optionClaimtype.getValue() != null){
		SelectValue claimTypeSelection = new SelectValue();
		if(String.valueOf(optionClaimtype.getValue()).equals("true")){
//			System.out.println("cashless");
			claimTypeSelection.setValue("Cashless");
			claimTypeSelection.setId(401L);
		}else{
//			System.out.println("Reimbursement");
			claimTypeSelection.setValue("Reimbursement");
			claimTypeSelection.setId(402L);
		}
		this.dtoBean.setClaimType(claimTypeSelection);
	}
	
	if(String.valueOf(optionHospitalType.getValue()).equals("Non Medical")){
//		this.dtoBean.setHospitalFlag("Y");
		this.dtoBean.setNonHospitalFlag("Y");
	}else{
//		this.dtoBean.setHospitalFlag("N");
		this.dtoBean.setNonHospitalFlag("N");
	}
	this.dtoBean.setAdmissionDate(AdmissionDate.getValue());
	this.dtoBean.setDateOfDischarge(DateofDischarge.getValue());
	this.dtoBean.setIntimatedby((SelectValue)cmbIntimatedBy.getValue());	
	this.dtoBean.setIntimatername(txtCallerOrIntimatorName.getValue());
	this.dtoBean.setContactNumber(txtCallerContactNo.getValue());
	this.dtoBean.setInitProvisionAmount(Double.parseDouble(txtInitialProvisionAmt.getValue()));
	this.dtoBean.setInrConversionRate(Double.parseDouble(txtINRConversionRate.getValue()));
	if(txtINRValue.getValue()!=null && !txtINRValue.getValue().equals("")){
		txtINRValue.setReadOnly(false);
		this.dtoBean.setInrTotalAmount(Double.parseDouble(txtINRValue.getValue()));
		txtINRValue.setReadOnly(true);
	}
	
	this.dtoBean.getInsured().setKey(((SelectValue)cmbInsuredName.getValue()).getId());
	this.dtoBean.setPlaceofAccidentOrEvent(txtPlaceOfAccident.getValue());
	this.dtoBean.setLossTime(txtLosstime.getValue());
	this.dtoBean.setLossDetails(txtlossDetails.getValue());
	if(txtCountry!= null && txtCountry.getValue()!= null){
		this.dtoBean.setCountryId(((SelectValue)txtCountry.getValue()).getId());
	}
	this.dtoBean.setLossDetails(txtlossDetails.getValue());
	this.dtoBean.setPlaceofAccidentOrEvent(txtPlaceOfAccident.getValue());
	this.dtoBean.setHospitalId((SelectValue) cmbHospitalCodeOrName.getValue());
	if(registrationRemarksTxta!= null && registrationRemarksTxta.getValue()!= null&&!registrationRemarksTxta.getValue().equalsIgnoreCase("")){
		this.dtoBean.setRemarks(registrationRemarksTxta.getValue());
	}
	if(suggestRejectionTxta!= null && suggestRejectionTxta.getValue()!= null){
			this.dtoBean.setRemarks(suggestRejectionTxta.getValue().toString());
	}
}

@Override
public void buildSuccessLayout() {

}


@Override
public void resetView() {

}

public void cancelIntimation(){
	ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
			"Are you sure you want to cancel ?",
			"No", "Yes", new ConfirmDialog.Listener() {
	
				private static final long serialVersionUID = 1L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isCanceled() && !dialog.isConfirmed()) {
						// YES
						if(dtoBean.getKey() == null){
							fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
						}
						else{
							fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
						}
						
					}
				}
			});
	
	dialog.setStyleName(Reindeer.WINDOW_BLACK);
}

private boolean validatePage() {
	
	Boolean hasError = false;
	
	eMsg ="";
	if(intimationDate!= null &&intimationDate.getValue()==null){
		hasError = true;
		eMsg = eMsg + "Please Provide Intimation Date. </br>";
	}
	
	if(cmbInsuredName.getValue()==null){
		hasError = true;
		eMsg = eMsg + "Please Select Insured Name. </br>";
	}
	
//	if(lossDateAndTime.getValue()==null){
//		hasError = true;
//		eMsg = eMsg + "Please Select Loss Date & Time. </br>";
//	}
	
	if(eventCode.getValue()==null){
		hasError = true;
		eMsg = eMsg + "Please Select Event Code. </br>";
	}
	
	if(StringUtils.isBlank(String.valueOf(txtInitialProvisionAmt.getValue()))){
		hasError = true;
		eMsg = eMsg + "Please Provide Initial Provision Amount. </br>";
	}
	
	if(StringUtils.isBlank(String.valueOf(txtINRConversionRate.getValue()))){
		hasError = true;
		eMsg = eMsg + "Please Provide INR Conversion Amount. </br>";
	}
	
	if(("Registered").equalsIgnoreCase(claimDto.getStatusName())){
		if(registrationRemarksTxta.getValue() == null ||  registrationRemarksTxta.getValue().isEmpty()){
			hasError = true;
			eMsg += "Please enter Registration Remarks</br>";
		}
	}
	
	if(("SuggestRejection").equalsIgnoreCase(claimDto.getStatusName())){
		if(StringUtils.isBlank(String.valueOf(suggestRejectionTxta.getValue()))){
			hasError = true;
			eMsg += "Please enter Rejection Remarks</br>";
		}
	}	
	
	if(optionHospitalType!= null && optionHospitalType.getValue()!= null&& optionHospitalType.getValue().equals("Non Medical")){
		
		if(lossDateAndTime!= null&& lossDateAndTime.getValue()==null){
			hasError = true;
			eMsg = eMsg + "Please Provide lossDate. </br>";
		}
	}
	if(optionHospitalType!= null && optionHospitalType.getValue()== null && cmbHospitalCodeOrName!= null && cmbHospitalCodeOrName.getValue()!= null){
		if(StringUtils.isBlank(String.valueOf(txtCity.getValue()))){
			hasError = true;
			eMsg = eMsg + "Please Provide City Name. </br>";
		}
		if(txtCountry!= null&& txtCountry.getValue()==null){
			hasError = true;
			eMsg = eMsg + "Please Provide Country Name. </br>";
		}
	}
/*	if( optionClaimtype!= null && optionClaimtype.getValue()==null&& optionHospitalType!= null && optionHospitalType.getValue()!= null && !optionHospitalType.getValue().equals("Non Medical")){
		hasError = true;
		eMsg += "Please select cashless or reimbursement </br>";
	}*/
	if( optionClaimtype!= null && optionClaimtype.getValue()!=null){
		if(txtAilmentOrLoss!=null && txtAilmentOrLoss.getValue()==null || txtAilmentOrLoss.getValue().equalsIgnoreCase("")){
		hasError = true;
		eMsg += "Please Enter Ailment Details </br>";
		}
	}
	
	if(StringUtils.isBlank(String.valueOf(txtClaimedAmount.getValue()))){
		hasError = true;
		eMsg = eMsg + "Please Provide Amount Claimed </br>";
	}
	
//	if(lossDateAndTime.getValue() != null){
//		String str1 = (lossDateAndTime.getValue() != null && !SHAUtils.isDateOfIntimationWithPolicyRange(dtoBean.getPolicy().getPolicyFromDate(), dtoBean.getPolicy().getPolicyToDate(), lossDateAndTime.getValue()) ? "Loss Date-Time is not with in policy period.</br>" : "");
//		
//		if(!SHAUtils.isEmpty(str1)){
//			hasError = true;
//			eMsg = eMsg + str1;
//		}	
//	}
//	if(intimationDate.getValue() != null && optionClaimtype.getValue() != null && ("Reimbursement").equals(String.valueOf(optionClaimtype.getValue()))){
//		String str1 = (intimationDate.getValue() != null && (SHAUtils.getDiffDaysWithNegative(dtoBean.getInsured().getReturnDate(), intimationDate.getValue()) < 0 ||  SHAUtils.getDiffDaysWithNegative(dtoBean.getInsured().getReturnDate(), intimationDate.getValue()) > 30) ? "Intimation date is more than 30 days from the date of return to India." :"");
//		if(!SHAUtils.isEmpty(str1)){
//			eMsg = eMsg + str1;
//			hasError = true;
//		}
//	}
	
	return hasError;
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

@Override
public void setInsuredInPage(Insured insured) {
	cmbInsuredName.setValue(new SelectValue(insured.getKey(), insured.getInsuredName()));
	this.dtoBean.setInsured(insured);
	this.dtoBean.getInsured().setKey(insured.getKey());
}

public void showSubmitMessage(OMPClaim claim){
	
	final Window	popup = new Window();
	popup.setCaption("");
	popup.setWidth("55%");
	popup.setHeight("55%");
	popup.setClosable(true);
	popup.center();
	popup.setModal(true);
	popup.setResizable(false);
	popup.setContent(buildRegisterSuccessLayout(claim,popup));
	popup.setClosable(true);
	 popup.addCloseListener(new Window.CloseListener() {
		 	 
		 private static final long serialVersionUID = 1L;
		 
		@Override
		public void windowClose(CloseEvent e) {
			System.out.println("Close listener called");
			popup.close();
			fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
		}
	});
		UI.getCurrent().addWindow(popup);
}
	


public VerticalLayout buildRegisterSuccessLayout(final OMPClaim claim,final Window popup) {

	FormLayout formFieldsLayout = buildBasicInfoLayout(claim);
	HorizontalLayout buttonLayoutForSuccess = new HorizontalLayout();

	TextField claimdAmt = new TextField("Amount Claimed($)");
	claimdAmt.setValue(String.valueOf(claim.getClaimedAmount()));
	claimdAmt.setReadOnly(true);
	claimdAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//	formFieldsLayout.addComponent(registrationRemarksTxta);
	formFieldsLayout.addComponent(claimdAmt);
	
	if (registrationRemarksTxta == null) {
		registrationRemarksTxta = new TextArea("Registration Remarks *");
	}if(claimDto.getRegistrationRemarks()!= null){
		if(registrationRemarksTxta!= null && registrationRemarksTxta.getValue()!= null){
//		registrationRemarksTxta.setValue(claimDto.getRegistrationRemarks());
//			registrationRemarksTxta.setValue(registrationRemarksTxta.getValue());
		}
	}
	registrationRemarksTxta.setReadOnly(true);
	registrationRemarksTxta
			.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	registrationRemarksTxta.setHeight("-1px");
	TextField rejectionTxt = new TextField(" Registration Remarks");
	rejectionTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rejectionTxt.setValue(registrationRemarksTxta.getValue());//
		rejectionTxt.setReadOnly(true);
//		claim.setRegistrationRemarks(rejectionTxt.getValue());
//	formFieldsLayout.addComponent(registrationRemarksTxta);
	formFieldsLayout.addComponent(rejectionTxt);
	
	TextField suggestRejectionTxt = new TextField(" Rejection Remarks");
		if (claimDto.getSuggestRejection()) {

			if (registrationRemarksTxta != null) {
				formFieldsLayout.removeComponent(rejectionTxt);
			}

			if (suggestRejectionTxta == null) {
				suggestRejectionTxta = new ComboBox(       // we have to change ComboBox
						"Suggested Rejection Remarks *");
			}
//			suggestRejectionTxta.setValue(claimDto
//					.getSuggestedRejectionRemarks());
			suggestRejectionTxta.setReadOnly(true);
			suggestRejectionTxta.setHeight("-1px");
			suggestRejectionTxta
					.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			if(suggestRejectionTxta!= null && suggestRejectionTxta.getValue()!= null){
				suggestRejectionTxt.setValue(suggestRejectionTxta.getValue().toString());
			}
			suggestRejectionTxt.setReadOnly(true);
			suggestRejectionTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//			claim.setSuggestedRejectionRemarks(suggestRejectionTxt.getValue());
			formFieldsLayout.addComponent(suggestRejectionTxt);	
	}
		
		claimNumber = new TextField("Claim Number");
		claimNumber.setValue(claim.getClaimId());
		claimNumber.setReadOnly(true);
		claimNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		claimNumber.setWidth("200px");
		intimationNumber.setReadOnly(false);
		intimationNumber.setValue(claim.getIntimation().getIntimationId());
		intimationNumber.setReadOnly(true);
		intimationNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		intimationNumber.addStyleName("my_bg_style");
		
		formFieldsLayout.addComponent(intimationNumber);
		formFieldsLayout.addComponent(claimNumber);

	HorizontalLayout buttonLayoutForSuccess1 = new HorizontalLayout(
			buttonLayoutForSuccess);
	buttonLayoutForSuccess1.setMargin(true);
	buttonLayoutForSuccess1.setWidth("100%");

	VerticalLayout layout = new VerticalLayout();
	
	 Button viewIntimationButton = new Button("View Intimation");
	 viewIntimationButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
//			showSubmitMessagePanel("", claim);
			viewintimation(popup);
//			viewDetails.getViewIntimation(claim.getIntimation().getIntimationId());
		}
		
	});
	 
	 Button viewHomeButton = new Button("Intimations Home");
	 viewHomeButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
//			 Window win = event.getButton().getWindow() ;
//		      win.getParent().removeWindow(win) ;
			popup.close();
			fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
			
		}
		
	});
	 
	
	//HorizontalLayout hlayoutSucess = showSubmitMessagePanel("");
	//hlayoutSucess = new HorizontalLayout(); Intimation and claim has been generated successfully!!!! 
	Label label = new Label(
			"<b style = 'color:red'>Intimation and Claim has been generated successfully!!!! </b>",
			Label.CONTENT_XHTML);
	formFieldsLayout.addComponent(label);
	HorizontalLayout layoutButton = new HorizontalLayout(viewHomeButton,viewIntimationButton);
	Panel sucessPanel = new Panel();
	 sucessPanel.setContent(formFieldsLayout);
	 sucessPanel.addStyleName("girdBorder");
	layout.addComponents(sucessPanel/*, label,buttonLayoutForSuccess1*/);
	buttonLayoutForSuccess1.setComponentAlignment(
			buttonLayoutForSuccess, Alignment.MIDDLE_CENTER);
	layout.addComponent(layoutButton);
	layout.setComponentAlignment(layoutButton, Alignment.BOTTOM_CENTER);
	layout.addStyleName("myonestyle");
	return layout;
}

private FormLayout buildBasicInfoLayout(OMPClaim claim) {
//	private FormLayout buildBasicInfoLayout() {
		String value = "";
		if (claim != null) {
		}
//		claim.setClaimedAmount(this.searchDTO.getInrValue());
		if(claim.getCurrencyId() != null){
		value = claim.getCurrencyId().getValue();
		}
		FormLayout formFieldsLayout = new FormLayout();

		return formFieldsLayout;
	}

public void viewintimation(final Window popup2){

	// YES
	if(dtoBean!= null && dtoBean.getIntimationno() != null && !dtoBean.getIntimationno().isEmpty()){
								
		OMPIntimation intimation =ompIntimationService.getIntimationByNo(dtoBean.getIntimationno());
//		ompViewIntimationDetails.init(intimation);
		ompnewViewIntimationUI.initView(dtoBean,dtoBean.getIntimationno());
		final Window	popup = new Window();
		popup.setCaption("View Claim Status");
		popup.setWidth("85%");
		popup.setHeight("85%");
		popup.setClosable(true);
//		popup.setContent(ompnewViewIntimationUI);
		popup.center();
		popup.setModal(true);
		popup.setResizable(false);
		//popup.setContent(buildRegisterSuccessLayout(claim));
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				popup2.close();
				fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
			}
		});
		Button okBtn = new Button("Close");
		okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		okBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
//				dialog.close();
				popup.close();
			}
		});
		
		VerticalLayout vlayout = new VerticalLayout(ompnewViewIntimationUI,okBtn);
		vlayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
		popup.setContent(vlayout);
		UI.getCurrent().addWindow(popup);
		
//		fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
	}
	

}

public HorizontalLayout showSubmitMessagePanel(String messageInfo,final OMPClaim claim){
	HorizontalLayout horizontalLayout = new HorizontalLayout();
	ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Web Information", messageInfo, "Intimation Home", "View Intimation", new ConfirmDialog.Listener() {
	
				private static final long serialVersionUID = 1L;
				public void onClose(final ConfirmDialog dialog) {
					if (dialog.isCanceled() && !dialog.isConfirmed()) {
						// YES
						if(dtoBean.getIntimationno() != null && !dtoBean.getIntimationno().isEmpty()){
													
							OMPIntimation intimation =ompIntimationService.getIntimationByNo(dtoBean.getIntimationno());
							ompViewIntimationDetails.init(intimation);
							final Window	popup = new Window();
							popup.setCaption("View Claim Status");
							popup.setWidth("75%");
							popup.setHeight("85%");
							popup.setClosable(true);
							popup.center();
							popup.setModal(true);
							popup.setResizable(false);
							//popup.setContent(buildRegisterSuccessLayout(claim));
							popup.addCloseListener(new Window.CloseListener() {
								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
									fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
								}
							});
							Button okBtn = new Button("OK");
							okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
							okBtn.addClickListener(new Button.ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
									dialog.close();
									popup.close();
								}
							});
							
							VerticalLayout vlayout = new VerticalLayout(ompViewIntimationDetails,okBtn);
							vlayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
							popup.setContent(vlayout);
							UI.getCurrent().addWindow(popup);
							
//							fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
						}
						
					}
						else {
							if(dtoBean.getKey() == null){
								fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
							}
						}
										
				}
			});
	
	dialog.setStyleName(Reindeer.WINDOW_BLACK);
	return horizontalLayout;
}

public void showErrorMessagePanel(String messageInfo){
	ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Error Information", messageInfo, "Intimation Home", "View Intimation", new ConfirmDialog.Listener() {
	
				private static final long serialVersionUID = 1L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isCanceled() && !dialog.isConfirmed()) {
						// YES
						if(dtoBean.getKey() == null){
							fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
						}
						else{
							fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
						}
					}else{
						fireViewEvent(MenuItemBean.OMP_INTIMATION_REG, null);
					}
				}
			});
	
	dialog.setStyleName(Reindeer.WINDOW_BLACK);
}

@Override
public void setClaimDetails(ClaimDto newclaimDto) {
	claimDto.setClaimId(newclaimDto.getClaimId());
	claimDto.setKey(newclaimDto.getKey());
	claim =  OMPClaimMapper.getInstance().getClaim(newclaimDto);

	if (claim != null) {/*
		registrationDetailsLayout.removeComponent(registerBtnLayout);
		registrationDetailsLayout.removeComponent(submitButtonLayout);
		registrationDetailsLayout
				.addComponent(buildRegisterSuccessLayout(claim));
//		registrationDetailsLayout
//		.addComponent(buildRegisterSuccessLayout());
	*/}
	
}

@Override
public HorizontalLayout showSubmitMessagePanel(String messageInfo) {
	// TODO Auto-generated method stub
	return null;
}

//R1276 - Introduced Textarea and enable F8 option in it.
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
			txtArea.setReadOnly(false);
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

			String strCaption = "";
			if(txtFld.getCaption().equals("")){
				strCaption = "Registration Remarks";
			}else{
				strCaption = "Intimation Remarks";
			}

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

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	private void eventCodeValueChangeAction(SelectValue argSelected){
		MastersEvents events = masterService.getEventTypeByKey(argSelected.getId());
		DBCalculationService dbCalculationService = new DBCalculationService();
		if(dtoBean.getPolicy()!= null && dtoBean.getPolicy().getKey()!= null&&events.getEventCode()!= null){
			Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(dtoBean.getPolicy().getKey(), events.getEventCode());
			Map<String, Double> balanceSIMap = dbCalculationService.getOmpBalanceSIByPolicyKey(dtoBean.getPolicy().getKey(), events.getEventCode());
			Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			Double CurrBalanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			if(balanceSI!= null){
				dtoBean.setBalanceSI(balanceSI);
			}
			if(CurrBalanceSI!= null){
				dtoBean.setCurrentBalanceSI(CurrBalanceSI);
			}
		}
		if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& dtoBean.getBalanceSI()!=null ){
			if(!StringUtils.isBlank(txtInitialProvisionAmt.getValue())){
				double provisionAmt = Double.parseDouble( txtInitialProvisionAmt.getValue());
				if(provisionAmt > dtoBean.getBalanceSI()){
					showErrorMessage( "Initial provision is greater than the Balance Sum Insured = "+ dtoBean.getBalanceSI() );
					txtInitialProvisionAmt.setValue("");
				}
			}
			
		}
		if(events!= null && events.getProcessType()!= null){
			if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C") || events.getProcessType().equalsIgnoreCase("R")){
				if(optionHospitalType!= null){
					optionHospitalType.setValue(null);
					optionHospitalType.setEnabled(false);
					
					AdmissionDate.setVisible(Boolean.TRUE);
					DateofDischarge.setVisible(Boolean.TRUE);
					txtAilmentOrLoss.setVisible(Boolean.TRUE);
					txtPlaceOfAccident.setVisible(Boolean.TRUE);
					placeofvisit.setVisible(Boolean.TRUE);
					cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
					txtCity.setVisible(Boolean.TRUE);
					txtCountry.setVisible(Boolean.TRUE);
					addHospital.setVisible(Boolean.TRUE);
					lossDateAndTime.setVisible(Boolean.FALSE);
  					txtLosstime.setVisible(Boolean.FALSE);
  					placeoflossOrDelay.setVisible(Boolean.FALSE);
  					txtlossDetails.setVisible(Boolean.FALSE);
//  					dummyTextField.setVisible(Boolean.FALSE);
  					if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C")){
  						optionClaimtype.setEnabled(Boolean.TRUE);
						optionClaimtype.setValue("Cashless");
					}
  					if(events.getProcessType().equalsIgnoreCase("R")){
  						optionClaimtype.setEnabled(Boolean.TRUE);
						optionClaimtype.setValue("Reimbursement");
  					}
				}
			}
			
			if(events!= null && events.getProcessType()!= null){
				if(events.getProcessType().equalsIgnoreCase("N")){
					if(optionHospitalType!= null){
						optionHospitalType.setEnabled(Boolean.TRUE);
						optionHospitalType.setValue("Non Medical");
						optionHospitalType.setEnabled(Boolean.TRUE);
						lossDateAndTime.setVisible(Boolean.TRUE);
	  					txtLosstime.setVisible(Boolean.FALSE);
	  					placeoflossOrDelay.setVisible(Boolean.TRUE);
	  					txtlossDetails.setVisible(Boolean.TRUE);
	  					placeofvisit.setVisible(Boolean.TRUE);
	  					AdmissionDate.setVisible(Boolean.FALSE);
						DateofDischarge.setVisible(Boolean.FALSE);
						txtAilmentOrLoss.setVisible(Boolean.FALSE);
						txtPlaceOfAccident.setVisible(Boolean.FALSE);
//						placeofvisit.setVisible(false);
						cmbHospitalCodeOrName.setVisible(Boolean.FALSE);
						txtCity.setVisible(Boolean.FALSE);
						txtCountry.setVisible(Boolean.FALSE);
						addHospital.setVisible(Boolean.FALSE);
//						dummyTextField.setVisible(Boolean.FALSE);
						if(optionHospitalType!=null){
//							optionClaimtype.setValue("Reimbursement");
	  						optionClaimtype.setEnabled(Boolean.FALSE);
	  						optionClaimtype.setValue(null);
	  				}else{
	  						optionClaimtype.setEnabled(true);
	  					}
	  				}
				}
			}
			if(events!= null && events.getEventCode().equalsIgnoreCase("OMP-CVR-011") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-009") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-012") ||
					 events.getEventCode().equalsIgnoreCase("CFT-CVR-010") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-015") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-012")){
				txtLosstime.setVisible(Boolean.TRUE);
			}
		}
	}
	
}
