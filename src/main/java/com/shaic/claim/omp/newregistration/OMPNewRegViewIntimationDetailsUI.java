package com.shaic.claim.omp.newregistration;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPAckDetailsDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreIntPaymentDetails;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreIntProposerDetails;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreIntimationAckDetails;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreateIntimationWizardViewPresenter;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPPaymentDetailsDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPProposerDetailsDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPViewPolicyDetails;
import com.shaic.claim.OMPViewDetails.view.OMPViewCurrentPolicyDetailsUI;
import com.shaic.claim.OMPViewDetails.view.OMPViewRiskDetailsUI;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPPreviousClaimWindowUI;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimWindowOpen;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimsTable;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.lumen.LumenQueryDTO;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationAddHospital;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.registration.OMPPreviousClaimDetailTable;
import com.shaic.claim.omp.registration.OMPPreviousClaimTableDTO;
import com.shaic.claim.ompviewroddetails.OMPViewIntimationDetailsUI;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
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
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;




public class OMPNewRegViewIntimationDetailsUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private TextField placeofvisit;
	private TextField placeoflossOrDelay;
	private TextField txtSponsorName;
	private TextField cmbModeofIntimation;
	private TextField cmbInsuredName;
	private TextField cmbHospitalCodeOrName;
	//private ComboBox cmbCountry;
	private TextField txtAilmentOrLoss;
	private OptionGroup optionClaimtype;
	private OptionGroup optionHospitalType;
	private DateField AdmissionDate;
	private DateField DateofDischarge;
	private TextField cmbIntimatedBy;
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

	@Inject
	private OMPViewPolicyDetails viewPolicyDetails;

	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private OMPClaimService ompclaimService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private OMPViewIntimationDetailsUI ompViewIntimationDetails;

	@Inject
	private OMPViewCurrentPolicyDetailsUI ompViewCurrentPolicyUI;
	@Inject
	private OMPViewClaimantDetailsPageUI ompViewClaimantdetailUI;
	
	@Inject
	private OMPPreviousClaimDetailTable previousClaimsRgistration;

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

	private ClaimDto claimDto = new ClaimDto();

	private NewIntimationDto newIntimationDto = new NewIntimationDto();

	private OMPClaim claim = null;

	@Inject
	private OMPViewRiskDetailsUI ompRiskDetailsUI;
	@Inject
	private OMPViewClaimantDetailsPageUI ompClaimantUI;
	
	@Inject
	private OMPNewRegViewMerDetailsUI ompViewMerDetailUI;
	
	@Inject
	private Instance<ViewOMPPreviousClaimWindowOpen> viewOMPPreviousClaimWindowOpen;
	
	@Inject
	private ViewOMPPreviousClaimsTable ompPreviousClaimsTable;

	private Policy policy;
	
	private TextArea registration;
	
	private TextField rejection;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	
	//R1276
	private boolean isTPAUserLogin;
	private TextArea intimationRemarks;

	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}
	
	@Inject
	private OMPCreIntimationAckDetails ackDetailsTable;
	
	@Inject
	private OMPCreIntProposerDetails proposerDetailsTable;
	
	@Inject
	private OMPCreIntPaymentDetails paymentDetailsTable;
	
	public void initView(OMPNewRegistrationSearchDTO ompIntimationDTO,String intimationNo) {
		this.dtoBean =null;
		this.dtoBean = ompIntimationDTO;
		mainPanel = new VerticalLayout();
//		initBinder();
		//R1276
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		OMPIntimation intimation = ompIntimationService
				.searchbyIntimationNo(intimationNo);
		OMPClaim claim = ompclaimService.getClaimforIntimation(intimation
				.getKey());
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
		dtoBean.setPolicy(intimation.getPolicy());
		dtoBean.setInsured(intimation.getInsured());
		OMPNewRegistrationRevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(dtoBean, "Intimation Screen");
		
		Panel carouselPanel = new Panel(intimationDetailsCarousel);
		carouselPanel.addStyleName("myonestyle");
		mainPanel.addComponent(carouselPanel);
	//	viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Intimation and Hospital Details");
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(btnCurrentPolicyDetails);
		horLayout.setSizeFull();
		horLayout.setComponentAlignment(btnCurrentPolicyDetails, Alignment.TOP_RIGHT);
		mainPanel.addComponent(viewDetailsButtonLayout(intimation,claim));
//		mainPanel.addComponent(horLayout);
		HorizontalLayout dummyhLayout = new HorizontalLayout();
		dummyhLayout.setSpacing(true);
		dummyhLayout.setMargin(true);
		mainPanel.addComponent(dummyhLayout);
		mainPanel.addComponent(commonButtonsLayout(intimation,claim));
//		mainPanel.addComponent(addFooterButtons());
//		mainPanel.setMargin(true);
//		mainPanel.setSizeFull();		
		
		setCompositionRoot(mainPanel);			
		}
	
	private VerticalLayout viewDetailsButtonLayout(final OMPIntimation intimation,OMPClaim claim){
		
		 riskButton = new Button("View Risk Details");
		riskButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
//				viewDetails.getViewRiskDetails(dtoBean.getIntimationno());
//				public void getViewOmpRiskDetails(String policyNo) {
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
//			}
			
		});
		
		 balanceSi = new Button("View Balance SI");
		 balanceSi.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null){
					
					viewDetails.getViewBalanceSumInsured(intimation.getIntimationId());
				}
			}
			
		});
		
		 policyDetails = new Button("View Policy Details");
		 policyDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Policy policy = intimation.getPolicy();
				ompViewCurrentPolicyUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
				ompViewCurrentPolicyUI.initView();
				UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
//				viewDetails.getViewPolicy(dtoBean.getIntimationno());
			}
			
		});
		
		 
		 vb64Button = new Button("View 64VB");
		 vb64Button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null){
					viewDetails.getIrda64VbDetails(intimation.getIntimationId());
				}
			}
			
		});
		 
		 viewMedButton = new Button("View MER Details");
		 viewMedButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null){
					
					viewDetails.getMERDetails(intimation.getIntimationId());
				}
//				Policy policy = dtoBean.getPolicy();
//				ompViewMerDetailUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
//				ompViewMerDetailUI.initView();
//				UI.getCurrent().addWindow(ompViewMerDetailUI);
			}
			
		});
		 
		 previousClaimButton = new Button("Previous Claim Details");
		 previousClaimButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null){
					List<OMPPreviousClaimTableDTO> previousClaimList = null;
				NewIntimationDto	newIntimationDto = ompIntimationService.getIntimationDto(intimation);
				previousClaimList = ompIntimationService.getPreviousClaimByForRegistration(newIntimationDto);
				if (null != previousClaimList && !previousClaimList.isEmpty()) {
					previousClaimsRgistration.init("", false, false);
					previousClaimsRgistration.setTableList(previousClaimList);
				}
//					viewDetails.getViewPolicy(dtoBean.getIntimationno());
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("Previous Claims");
				popup.setWidth("75%");
				popup.setHeight("75%");
				popup.setContent(previousClaimsRgistration);
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
				
		}
			});
		 
		 Button viewHistoryButton = new Button("View Claim History");
		 viewHistoryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(dtoBean.getIntimationno()!= null){
					viewDetails.getViewClaimHistory(intimation.getIntimationId());
				}
			}
			
		});
		 
		 final Button viewPreviousPolicyClaimButton = new Button("Prev Claim Details");
		 if(dtoBean.getIntimationno()!= null){
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,dtoBean.getIntimationno());
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
				if(dtoBean.getIntimationno()!= null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,dtoBean.getIntimationno());
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
//				viewDetails.getviewClaimantDetails(dtoBean.getIntimationno());
				Window popup =null;
				popup = new com.vaadin.ui.Window();
				popup.setWidth("800px");
				popup.setHeight("280px");
				popup.setCaption("View Claimant Details");
//				OMPIntimation intimation = ompIntimationService
//						.searchbyIntimationNo(intimationNo);
				Policy policy = intimation.getPolicy();
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
					BPMClientContext bpmClientContext = new BPMClientContext();
					Long dummyno = 1l;
					String dummystrin = "";
					Map<String,String> tokenInputs = new HashMap<String, String>();
					 tokenInputs.put("intimationNo", dtoBean.getIntimationno());
					 tokenInputs.put("ompdoc", dummyno.toString());
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
					String url = bpmClientContext.getGalaxyDMSUrl() + dtoBean.getIntimationno();*/
				//	getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
	intimationNumber = new TextField("Intimation Number");
	intimationNumber.setValue(intimation.getIntimationId());
	intimationNumber.setReadOnly(true);
	claimNumber = new TextField("Claim Number");
	if(claim!= null && claim.getClaimId()!= null){
		claimNumber.setValue(claim.getClaimId());
	}
	claimNumber.setReadOnly(true);
		HorizontalLayout horizontalLayout = null;
		HorizontalLayout horizontalLayout2 = null;
		
		if(isTPAUserLogin()){
			horizontalLayout = new HorizontalLayout(riskButton,policyDetails,viewHistoryButton,viewClaimantButton,viewDocButton);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setHeight("27px");
		}else{
			horizontalLayout = new HorizontalLayout(riskButton,balanceSi,policyDetails,vb64Button,viewMedButton);
			horizontalLayout2 = new HorizontalLayout(previousClaimButton,viewHistoryButton, viewPreviousPolicyClaimButton,viewClaimantButton,viewDocButton);
			horizontalLayout.setSpacing(true);
			horizontalLayout2.setSpacing(true);
			horizontalLayout.setHeight("27px");
			horizontalLayout2.setHeight("57px");
		}
//		 HorizontalLayout horizontalLayout = new HorizontalLayout(riskButton,balanceSi,policyDetails,vb64Button,viewMedButton);
//		 HorizontalLayout horizontalLayout2 = new HorizontalLayout(previousClaimButton,viewHistoryButton, viewPreviousPolicyClaimButton,viewClaimantButton,viewDocButton);
		 FormLayout intimationLayout = new FormLayout(intimationNumber);
		 FormLayout claimLayout = new FormLayout(claimNumber);
		 HorizontalLayout hLayout = new HorizontalLayout(intimationLayout,claimLayout);
			hLayout.setMargin(true);
			VerticalLayout vLayout = new VerticalLayout(hLayout);
			vLayout.setComponentAlignment(hLayout, Alignment.TOP_RIGHT);
			
			VerticalLayout verticalLayout = null;
			if(isTPAUserLogin()){
				verticalLayout = new VerticalLayout(horizontalLayout,vLayout);
				verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
			}else{
				verticalLayout = new VerticalLayout(horizontalLayout,horizontalLayout2,vLayout);
				verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
				verticalLayout.setComponentAlignment(horizontalLayout2, Alignment.TOP_RIGHT);
			}
			verticalLayout.setSpacing(true);
			verticalLayout.setWidth("100%");
			verticalLayout.setHeight("75px");
			return verticalLayout;
			
		
	}
	
	@SuppressWarnings("serial")
	private VerticalLayout commonButtonsLayout(OMPIntimation intimation,OMPClaim claim){

		
		FormLayout emptyFormLayoutOne = new FormLayout(new Label());
		emptyFormLayoutOne.setSizeFull();
		FormLayout emptyFormLayoutTwo = new FormLayout(new Label());	
		emptyFormLayoutTwo.setSizeFull();
						
		intimationDate = new DateField("Intimation Date");
		intimationDate.setDateFormat("dd-MM-yyyy");
		intimationDate.setValue(intimation.getIntimationDate());
		intimationDate.setRequired(true);
		intimationDate.setReadOnly(true);
		lossDateAndTime = new DateField("Loss Date & Time ");
		lossDateAndTime.setDateFormat("dd-MM-yyyy");
		lossDateAndTime.setValue(intimation.getLossDateTime());
		lossDateAndTime.setRequired(true);
		lossDateAndTime.setReadOnly(true);
		txtLosstime = new DateField("Loss Time ");
		txtLosstime.setValue(intimation.getLossTime());
		txtLosstime.setDateFormat("dd-MM-yyyy'-'HH:mm:ss");
		txtLosstime.setLocale(Locale.ENGLISH);
		txtLosstime.setLocale(new Locale("en", "GB"));
		txtLosstime.setResolution(Resolution.MINUTE);
		txtLosstime.setReadOnly(true);
		tpaIntimationno = new TextField("TPA Intimation No");
		tpaIntimationno.setValue(intimation.getTpaIntimationNumber());
		tpaIntimationno.setNullRepresentation("");
		tpaIntimationno.setReadOnly(true);
		eventCode = new ComboBox("Event Code");
		eventCode.setRequired(true);
		BeanItemContainer<SelectValue> ompEventCodes = masterService.getListMasterEventByProduct(intimation.getPolicy().getProduct().getKey());
		eventCode.setContainerDataSource(ompEventCodes);
		eventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		eventCode.setItemCaptionPropertyId("value");
		if(intimation.getEvent()!= null && intimation.getEvent().getEventCode()!= null){
			MastersEvents event = intimation.getEvent();
			SelectValue eventCodeValue = new SelectValue();
			eventCodeValue.setId(event.getKey());
			eventCodeValue.setValue(event.getEventCode());
			eventCode.setValue(eventCodeValue);
		}
		eventCode.setReadOnly(true);
		placeofvisit = new TextField("Place of Visit");
		placeofvisit.setNullRepresentation("");
		placeofvisit.setValue(intimation.getPlaceVisit());
		placeofvisit.setReadOnly(true);
		placeoflossOrDelay = new TextField("Place of Loss/Delay");
		placeoflossOrDelay.setValue(intimation.getPlaceLossDelay());
		placeoflossOrDelay.setNullRepresentation("");
		placeoflossOrDelay.setReadOnly(true);
//		txtSponsorName = new TextField("Sponsor Name");
//		txtSponsorName.setValue(intimation.getSponsorName());
		cmbModeofIntimation = new TextField("Mode of Intimation");	
	/*	BeanItemContainer<SelectValue> ompModeOfIntimation = masterService.getConversionReasonByValue(ReferenceTable.MODE_OF_INTIMATION);
		cmbModeofIntimation.setContainerDataSource(ompModeOfIntimation);
		cmbModeofIntimation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeofIntimation.setItemCaptionPropertyId("value");*/
		if(intimation.getIntimationMode()!= null && intimation.getIntimationMode().getKey()!= null){
		MastersValue master = rodBillentryService.getMaster(intimation.getIntimationMode().getKey());
		if(master != null){
			SelectValue modeofintimation = new SelectValue();
			modeofintimation.setId(master.getKey());
			modeofintimation.setValue(master.getValue());
			cmbModeofIntimation.setValue(modeofintimation.getValue());
			}
		}
		cmbModeofIntimation.setReadOnly(true);
//		FormLayout firstForm1 = new FormLayout(intimationDate,lossDateAndTime,tpaIntimationno,eventCode,placeofvisit,placeoflossOrDelay,txtSponsorName,cmbModeofIntimation);
		cmbInsuredName = new TextField("Insured Name");
		cmbInsuredName.setRequired(true);
	/*	BeanItemContainer<SelectValue> ompInsuredName = masterService.getOMPInsuredNameList(intimation.getPolicy().getInsured());
		cmbInsuredName.setContainerDataSource(ompInsuredName);
		cmbInsuredName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredName.setItemCaptionPropertyId("value");
		cmbInsuredName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue insuredValue = (SelectValue) event.getProperty().getValue();
				if(insuredValue != null && insuredValue.getId() != null){
					fireViewEvent(OMPCreateIntimationWizardViewPresenter.FIND_INSURED, insuredValue.getId());
				}
			}
		});*/
		if(intimation!= null && intimation.getInsured()!= null&& intimation.getInsured().getInsuredName()!= null /*&& intimation.getPolicy().getInsured().get(0).getInsuredName()!= null*/){
			cmbInsuredName.setValue(intimation.getInsured().getInsuredName());
		}
		cmbInsuredName.setNullRepresentation("");
		cmbInsuredName.setReadOnly(true);
		txtAilmentOrLoss = new TextField("Ailment Details");
		txtAilmentOrLoss.setValue(intimation.getAilmentLoss());
		txtAilmentOrLoss.setNullRepresentation("");
		txtAilmentOrLoss.setRequired(true);
		txtAilmentOrLoss.setReadOnly(true);
		optionClaimtype = new OptionGroup("");
		/*optionClaimtype.addItems("Cashless","Reimbursement");
		optionClaimtype.setStyleName("horizontal");*/
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
		optionClaimtype.setReadOnly(true);
		
		optionHospitalType = new OptionGroup("");	
		optionHospitalType.addItem("Non Medical");
		optionHospitalType.setStyleName("horizontal");
		if((intimation.getNonHospitalizationFlag()!= null && intimation.getNonHospitalizationFlag().equalsIgnoreCase("Y"))) {
			optionHospitalType.setValue("Non Medical");
		} 
		else{
			optionHospitalType.setValue(null);
		}
		optionHospitalType.setReadOnly(true);
		
		AdmissionDate = new DateField("Admission Date");
		AdmissionDate.setDateFormat("dd-MM-yyyy");
		AdmissionDate.setValue(intimation.getAdmissionDate());
		AdmissionDate.setReadOnly(true);
		DateofDischarge = new DateField("Date of Discharge");
		DateofDischarge.setDateFormat("dd-MM-yyyy");
		DateofDischarge.setValue(intimation.getDischargeDate());
		DateofDischarge.setReadOnly(true);
		addHospital = new Button("Add Hospital");
		addHospital.setStyleName(BaseTheme.BUTTON_LINK); 
	/*	addHospital.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				cmbHospitalCodeOrName.unselect(cmbHospitalCodeOrName.getValue());
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
			
		});*/
		addHospital.setEnabled(Boolean.FALSE);
//		addHospital.setReadOnly(true);
		//FormLayout lossDelay = new FormLayout(placeoflossOrDelay);
		FormLayout hospitalButton = new FormLayout(addHospital);
		//FormLayout dischargeDate = new FormLayout(DateofDischarge);
		
		//FormLayout hospitalButton = new FormLayout(placeoflossOrDelay,addHospital,DateofDischarge);
			
		cmbIntimatedBy = new TextField("Intimated By");
	/*	BeanItemContainer<SelectValue> intimatedBy = masterService.getConversionReasonByValue(ReferenceTable.INTIMATED_BY);
		cmbIntimatedBy.setContainerDataSource(intimatedBy);
		cmbIntimatedBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimatedBy.setItemCaptionPropertyId("value");*/
		if(intimation.getIntimationMode()!= null &&intimation.getIntimationMode().getKey()!= null && intimation.getIntimatedBy()!=null){
			MastersValue master = rodBillentryService.getMaster(intimation.getIntimatedBy().getKey());
			if(master != null){
				SelectValue modeofintimation = new SelectValue();
				modeofintimation.setId(master.getKey());
				modeofintimation.setValue(master.getValue());
				cmbIntimatedBy.setValue(modeofintimation.getValue());
				}
			}
		
		cmbIntimatedBy.setReadOnly(true);
		txtCallerOrIntimatorName = new TextField("Caller/Intimator Name");
		txtCallerOrIntimatorName.setValue(intimation.getIntimaterName());
		txtCallerOrIntimatorName.setNullRepresentation("");
		txtCallerOrIntimatorName.setReadOnly(true);
		txtCallerContactNo = new TextField("Caller Contact No ");
		txtCallerContactNo.setValue(intimation.getCallerMobileNumber());
		txtCallerContactNo.setNullRepresentation("");
		txtCallerContactNo.setReadOnly(true);
		CSValidator intialProamtTxtValidator = new CSValidator();
		txtInitialProvisionAmt = new TextField("Initial Provision Amt($)");
		txtInitialProvisionAmt.setRequired(true);
		txtInitialProvisionAmt.setValue(String.valueOf(intimation.getDollarInitProvisionAmt()));
		intialProamtTxtValidator.extend(txtInitialProvisionAmt);
		intialProamtTxtValidator.setRegExp("^[0-9.]*$");
		intialProamtTxtValidator.setPreventInvalidTyping(true);
		CSValidator iNRConversionRateTxtValidator = new CSValidator();
		txtInitialProvisionAmt.setNullRepresentation("");
		txtInitialProvisionAmt.setReadOnly(true);
		txtINRConversionRate = new TextField("INR Conversion Rate");
		txtINRConversionRate.setValue(String.valueOf(intimation.getInrConversionRate()));
		txtINRConversionRate.setRequired(true);
		iNRConversionRateTxtValidator.extend(txtINRConversionRate);
		iNRConversionRateTxtValidator.setRegExp("^[0-9.]*$");
		iNRConversionRateTxtValidator.setPreventInvalidTyping(true);
		txtINRConversionRate.setNullRepresentation("");
		txtINRConversionRate.setReadOnly(true);
		txtINRValue = new TextField("INR Value");
		String inrValue = (intimation.getInrTotalAmount() == null)?"":intimation.getInrTotalAmount().toString();
		txtINRValue.setValue(inrValue);
		txtINRValue.setNullRepresentation("");
		txtINRValue.setReadOnly(true);
		txtPlaceOfAccident=  new TextField("Place of Accident/Event");
		txtPlaceOfAccident.setValue(intimation.getEventPlace());
		txtPlaceOfAccident.setNullRepresentation("");
		txtPlaceOfAccident.setReadOnly(true);
		txtlossDetails = new TextField("Loss Details");
		txtlossDetails.setValue(intimation.getLossDetails());
		txtlossDetails.setNullRepresentation("");
		txtlossDetails.setReadOnly(true);
		FormLayout firstForm2 = new FormLayout(txtCallerOrIntimatorName,cmbModeofIntimation,cmbIntimatedBy);
		firstForm2.setSpacing(true);
		firstForm2.setMargin(true);
//		setReadOnly(firstForm2,true);
//		FormLayout firstForm2 = new FormLayout(cmbInsuredName,txtAilmentOrLoss,optionClaimtype,optionHospitalType,AdmissionDate, DateofDischarge);
		FormLayout firstForm1 = new FormLayout(intimationDate,tpaIntimationno,cmbInsuredName);
		firstForm1.setSpacing(true);
		firstForm1.setMargin(Boolean.TRUE);
//		setReadOnly(firstForm1,true);
//		firstForm1.setMargin(new MarginInfo(true, false, true, false));
		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm1,/*emptyFormLayoutOne,*/firstForm2);
		hLayout1.setSpacing(false);
		hLayout1.setHeight("100%");
		hLayout1.setCaption("Intimation Details");
//		hLayout1.setMargin(true);
		
		TextField txtClaimedAmount = new TextField("Amount Claimed ($)");
		txtClaimedAmount.setValue(String.valueOf(claim.getClaimedAmount()));
		txtClaimedAmount.setReadOnly(true);
		
		FormLayout secondForm1 = new FormLayout(eventCode,optionClaimtype,AdmissionDate,DateofDischarge,txtAilmentOrLoss,txtPlaceOfAccident,placeofvisit,lossDateAndTime,txtLosstime,placeoflossOrDelay,txtClaimedAmount);
//		FormLayout secondForm1 = new FormLayout(cmbIntimatedBy,txtCallerOrIntimatorName,txtCallerContactNo,txtInitialProvisionAmt,txtINRConversionRate,txtINRValue);
//		setReadOnly(secondForm1,true);
		
		
		cmbHospitalCodeOrName = new TextField("Hospital Code/Name");
		cmbHospitalCodeOrName.setRequired(true);
		//		loadHospitalValues();
	/*	cmbHospitalCodeOrName.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
//					updateHospitalDetailsInFields(event.getProperty().getValue().toString());
				}else{
//					updateHospitalDetailsInFields("");
				}
			}
		});*/
		if(intimation!= null && intimation.getHospitalName()!= null){
			OMPHospitals hospitalRec = ompIntimationService.getHospitalDetails(intimation.getHospitalName());
			if(hospitalRec!= null && hospitalRec.getHospitalCode()!= null && hospitalRec.getName()!= null){
				String hospCodeName = hospitalRec.getHospitalCode()+"-"+hospitalRec.getName();
				SelectValue hospcName = new SelectValue();
				hospcName.setId(0l);
				hospcName.setValue(hospCodeName);
				cmbHospitalCodeOrName.setValue(hospCodeName);
			}
		}
		cmbHospitalCodeOrName.setNullRepresentation("");
		cmbHospitalCodeOrName.setReadOnly(true);
		
		dummyTextField =  new TextField();
		dummyTextField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
//				loadHospitalValues();
			}
		});

		txtCity = new TextField("City");
		// Auto filling city value
		String cityName = (intimation.getCityName() == null)?"":intimation.getCityName();
		txtCity.setValue(cityName);
		txtCity.setNullRepresentation("");
		txtCity.setRequired(true);
		txtCity.setReadOnly(true);
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
		txtCountry.setRequired(true);
//		txtRemarks = new TextArea("Remarks");
		txtCountry.setReadOnly(true);
		FormLayout hospitalName = new FormLayout(cmbHospitalCodeOrName);
		HorizontalLayout hLayout = new HorizontalLayout(hospitalName, hospitalButton);
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		FormLayout secondForm2 = new FormLayout(optionHospitalType,addHospital,cmbHospitalCodeOrName,txtCity,txtCountry,txtlossDetails,placeofvisit);
//		FormLayout secondForm2 = new FormLayout(hLayout,txtCity,txtCountry,txtRemarks);
//		setReadOnly(secondForm2,true);
/*		FormLayout provisionLayout = new FormLayout(txtInitialProvisionAmt,txtINRValue);
		provisionLayout.setSpacing(true);
		provisionLayout.setMargin(true);*/
//		setReadOnly(provisionLayout,true);
		
//		FormLayout lossdetailsLayout = new FormLayout(lossDateAndTime,txtLosstime,placeoflossOrDelay);
//		lossdetailsLayout.setSpacing(true);
//		lossdetailsLayout.setMargin(true);
//		FormLayout provisionLayout1 = new FormLayout(txtINRConversionRate);
		
		intimationRemarks = new TextArea("Intimation Remarks");
		intimationRemarks.setValue(intimation.getRemarks());
		intimationRemarks.setRows(4);
		intimationRemarks.setColumns(25);
		intimationRemarks.setReadOnly(true);
		handleTextAreaPopup(intimationRemarks, null);
		FormLayout intRemrksLayout = new FormLayout(intimationRemarks);
		
		rejection = new TextField("Rejection Remarks");
		registration = new TextArea("Registration Remarks");
		registration.setRows(4);
		registration.setColumns(25);
		if(claim!= null && claim.getRegistrationRemarks()!= null ){
			registration.setValue(claim.getRegistrationRemarks());
			rejection.setVisible(Boolean.FALSE);
		}
		registration.setReadOnly(true);
		handleTextAreaPopup(registration, null);
		if(claim!= null && claim.getSuggestedRejectionRemarks()!= null){
			rejection.setValue(claim.getSuggestedRejectionRemarks());
			registration.setVisible(Boolean.FALSE);
		}
		rejection.setReadOnly(true);
		
		TextField claimNumber_Reg = new TextField("Claim Number");
		if(claim!= null && claim.getClaimId()!= null){
			claimNumber_Reg.setValue(claim.getClaimId());
		}
		claimNumber_Reg.setReadOnly(true);

		TextField claimStatus = new TextField("Status");
		if(claim!= null && claim.getClaimId()!= null){
			claimStatus.setValue(claim.getStatus().getProcessValue());
		}
		claimStatus.setReadOnly(true);
		
		FormLayout provisionLayout = new FormLayout(txtInitialProvisionAmt,registration,rejection);
		provisionLayout.setSpacing(true);
		provisionLayout.setMargin(true);

		FormLayout clmLayout = new FormLayout(claimNumber_Reg,claimStatus);
		clmLayout.setSpacing(true);
		clmLayout.setMargin(true);
		
//		FormLayout provisionLayout2 = new FormLayout(intimationRemarks,registration,rejection);
//		setReadOnly(provisionLayout2,true);
//		FormLayout lossdetailsLayout1 = new FormLayout(txtlossDetails,placeofvisit);
//		lossdetailsLayout1.setSpacing(true);
//		lossdetailsLayout1.setMargin(true);
		addListener();
		eventCode.setReadOnly(false);
		eventCode.setValue(intimation.getEvent().getEventCode());
		eventCode.setReadOnly(true);
		HorizontalLayout hLayuot2 = new HorizontalLayout(secondForm1,emptyFormLayoutTwo,secondForm2);
		hLayuot2.setSpacing(false);
		hLayuot2.setHeight("100%");
		hLayuot2.setMargin(true);
		hLayuot2.setCaption("Loss Details");
		dynamicFrmLayout = new FormLayout();
		dynamicFieldsLayout = new VerticalLayout();
		dynamicFieldsLayout.addComponent(dynamicFrmLayout);
//		HorizontalLayout hLayoutlossdetails = new HorizontalLayout(lossdetailsLayout);
		HorizontalLayout provisnLayout = new HorizontalLayout(provisionLayout,clmLayout);
		provisnLayout.setCaption("Registration Details");
		provisionLayout.setWidth("100%");
//		HorizontalLayout hLayout2 = new HorizontalLayout(provisionLayout2);
//		hLayout2.setWidth("100%");
//		HorizontalLayout suggestRejecionAndRegisterButtonLayout = BuildClaimRegisterAndSuggestRejectionBtnLayout();
		VerticalLayout firstVlayout = new VerticalLayout(hLayout1,hLayout);
		firstVlayout.setSizeFull();
		firstVlayout.setSpacing(true);
//		VerticalLayout secondlayout = new VerticalLayout(hLayout2); //provisnLayout
		VerticalLayout secondlayout = new VerticalLayout(provisnLayout);
		
		
		VerticalLayout mainLayout = null;
		if(isTPAUserLogin){
			mainLayout = new VerticalLayout(firstVlayout,hLayuot2,secondlayout,intRemrksLayout);
		}else{
			//R1276 - Introducing Ack Details
			/*ackDetailsTable.init("Acknowledgement Details", false, false);
			List<OMPAckDetailsDTO> listOfackObj = ompclaimService.getOMPAckByClaim(claim.getKey());			
			Page<OMPAckDetailsDTO> page = new Page<OMPAckDetailsDTO>();
			page.setPageItems(listOfackObj);
			page.setTotalRecords(listOfackObj.size());
			page.setTotalList(listOfackObj);
			ackDetailsTable.setTableList(page.getTotalList());
			ackDetailsTable.setSubmitTableHeader();
			ackDetailsTable.setSizeFull();*/
			
			
			proposerDetailsTable.init("Processor / Approver Details", false, false);
			List<OMPProposerDetailsDTO> listOfProObj = ompclaimService.getOMPProposerByClaim(claim.getKey());			
			Page<OMPProposerDetailsDTO> proposerPage = new Page<OMPProposerDetailsDTO>();
			proposerPage.setPageItems(listOfProObj);
			proposerPage.setTotalRecords(listOfProObj.size());
			proposerPage.setTotalList(listOfProObj);
			proposerDetailsTable.setTableList(proposerPage.getTotalList());
			proposerDetailsTable.setSubmitTableHeader();
			proposerDetailsTable.setSizeFull();
			
			
			paymentDetailsTable.init("Payment Details", false, false);
			List<OMPPaymentDetailsDTO> listOfPayObj = ompclaimService.getOMPPaymentByClaim(claim.getClaimId(), claim.getKey());			
			Page<OMPPaymentDetailsDTO> paymentPage = new Page<OMPPaymentDetailsDTO>();
			paymentPage.setPageItems(listOfPayObj);
			paymentPage.setTotalRecords(listOfPayObj.size());
			paymentPage.setTotalList(listOfPayObj);
			paymentDetailsTable.setTableList(paymentPage.getTotalList());
			paymentDetailsTable.setSubmitTableHeader();
			paymentDetailsTable.setSizeFull();
			
			
			mainLayout = new VerticalLayout(firstVlayout,hLayuot2,secondlayout,intRemrksLayout,ackDetailsTable,proposerDetailsTable,paymentDetailsTable);
		}
		mainLayout.addComponent(dynamicFieldsLayout);
//		mainLayout.addComponent(suggestRejecionAndRegisterButtonLayout);
//		mainLayout.setComponentAlignment(suggestRejecionAndRegisterButtonLayout,Alignment.BOTTOM_RIGHT);
		//mainLayout.setMargin(new MarginInfo(false, true, false, true));
		mainLayout.setSpacing(true);
		
		return mainLayout;
		

	}

	private void addListener() {
		if(eventCode!= null){
			

//	    	eventCode.addValueChangeListener( new ValueChangeListener( ) {
	          
	          /**
			 * 
			 */
//			private static final long serialVersionUID = 1L;

//			@Override
//	          public void valueChange( ValueChangeEvent event ) {
				
				SelectValue eventSelectValue = (SelectValue) eventCode.getValue();
				if(eventSelectValue != null && eventSelectValue.getValue()!=null) {
						/*String eventCodeType = eventSelectValue.getValue();
						String[] split = eventCodeType.split("-");
						String description1 = split[0];
						String description2 = split[1];
						String description3 = split[2];
//						String description = split[3];
						String eventCode = description1+"-"+description2+"-"+description3;
						eventCode = eventCode.trim();*/
//						MastersEvents events = masterService.getEventType(eventCode);
						MastersEvents events = masterService.getEventTypeByKey(eventSelectValue.getId());
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
				  					if(optionHospitalType!=null){
				  						optionClaimtype.setEnabled(Boolean.TRUE);
				  						optionClaimtype.setReadOnly(false);
										optionClaimtype.setValue("Cashless");
										optionClaimtype.setReadOnly(true);
									}
								}
							}
							
							if(events!= null && events.getProcessType()!= null){
								if(events.getProcessType().equalsIgnoreCase("N")){
									if(optionHospitalType!= null){
										optionHospitalType.setEnabled(Boolean.TRUE);
										optionHospitalType.setValue("Non Medical");
										
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
										if(optionHospitalType!=null){
//											optionClaimtype.setValue("Reimbursement");
					  						optionClaimtype.setEnabled(Boolean.FALSE);
					  						optionClaimtype.setReadOnly(Boolean.FALSE);
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
		
			}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
//	 @SuppressWarnings({ "deprecation" })
//	 	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
//	 		Iterator<Component> formLayoutLeftComponent = a_formLayout
//	 				.getComponentIterator();
//	 		while (formLayoutLeftComponent.hasNext()) {
//	 			Component c = formLayoutLeftComponent.next();
//	 			if (c instanceof TextField) {
//	 				TextField field = (TextField) c;
//	 				field.setWidth("300px");
//	 				field.setNullRepresentation("-");
//	 				field.setReadOnly(readOnly);
//	 				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//	 			} else if (c instanceof TextArea) {
//	 				TextArea field = (TextArea) c;
//	 				field.setWidth("350px");
//	 		        field.setNullRepresentation("-");
//	 				field.setReadOnly(readOnly);
//	 				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//	 			}
//	 		}
//	 	}

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
		ShortcutListener listener =  new ShortcutListener("Initiator Remarks",KeyCodes.KEY_F8,null) {

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

				String strCaption = "";
				if(txtFld.getCaption().equalsIgnoreCase("Registration Remarks")){
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
}
