//package com.shaic.claim.viewPedEndorsement;
//
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.enterprise.inject.Instance;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.AbstractMVPView;
//
//import com.shaic.claim.ClaimDto;
//import com.shaic.claim.IntimationDetailsCarousel;
//import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
//import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyDetails;
//import com.shaic.claim.coporatebuffer.view.ViewCoporateBuffer;
//import com.shaic.claim.intimatino.view.ViewIntimationDetails;
//import com.shaic.claim.intimation.CashLessTableDetails;
//import com.shaic.claim.intimation.CashLessTableMapper;
//import com.shaic.claim.intimation.CashlessTable;
//import com.shaic.claim.intimation.ViewIntimationStatus;
//import com.shaic.claim.intimation.create.dto.DtoConverter;
//import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
//import com.shaic.claim.preauth.InitiatePEDEndorsement;
//import com.shaic.claim.preauth.view.ViewPreAuthDetailsMainClass;
//import com.shaic.claim.preauth.wizard.view.ViewInvestigationDetails;
//import com.shaic.claim.productbenefit.view.ViewProductBenefits;
//import com.shaic.claim.registration.ViewBalanceSumInsured;
//import com.shaic.claim.registration.ViewHospitalDetails;
//import com.shaic.claim.registration.ViewIntimationRegisteredDetails;
//import com.shaic.claim.registration.ViewPreviousClaims;
//import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
//import com.shaic.claim.stoploss.view.ViewStopLossDetails;
//import com.shaic.claim.sublimit.ViewSublimitDetails;
//import com.shaic.claim.translationmiscrequest.view.ViewTranslationMiscRequestDetails;
//import com.shaic.domain.Claim;
//import com.shaic.domain.ClaimService;
//import com.shaic.domain.HospitalService;
//import com.shaic.domain.Hospitals;
//import com.shaic.domain.InsuredService;
//import com.shaic.domain.Intimation;
//import com.shaic.domain.IntimationService;
//import com.shaic.domain.MasterService;
//import com.shaic.domain.NewIntimationService;
//import com.shaic.domain.PolicyService;
//import com.shaic.newcode.wizard.dto.ClaimStatusDto;
//import com.shaic.newcode.wizard.dto.NewIntimationDto;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.v7.ui.ComboBox;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.VerticalSplitPanel;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.Window.CloseEvent;
//
//@SuppressWarnings("unused")
//public class PedEndorsementViewImpl extends AbstractMVPView implements PedEndorsementView {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private static final String VIEW_PED_REQUEST = "View Ped Request";
//	private static final String VIEW_STOPLOSS = "View Stoploss";
//	private static final String VIEW_CORPORATE_BUFFER = "View Coporate Buffer";
//	private static final String VIEW_SUB_LIMIT = "View Sub Limit";
//	private static final String VIEW_TRANSLATION_MISC_REQUEST = "View Translation Misc Request";
//	private static final String VIEW_CO_ORDINATOR_REPLAY = "View Co Ordinator Replay";
//	private static final String VIEW_SPECIALITY_OPINION = "View Speciality Opinion";
//	private static final String VIEW_PRE_AUTH_DETAILS = "View Pre Auth Details";
//	private static final String VIEW_CLAIM_HISTORY = "View Claim History";
//	private static final String VIEW_INTIATE_INVESTIGATION = "View Initiate Investigation";
//	private static final String VIEW_INTIMATION = "View FVR Details";
//	private static final String PED_REQUEST_DETAILS = "PED Request Details";
//	
//	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
//	
//	//private static Window popup;
//
//	@EJB
//	private PolicyService policyService;
//
//	@EJB
//	private InsuredService insuredService;
//
//	@EJB
//	private MasterService masterService;
//
//	@EJB
//	private HospitalService hospitalService;
//
//	@EJB
//	private IntimationService intimationService;
//
//	@EJB
//	private ClaimService claimService;
//
//	@Inject
//	private Instance<ViewBalanceSumInsured> viewBalenceSumInsured;
//	@Inject
//	private Instance<ViewHospitalDetails> viewHospitalDetails;
//	@Inject
//	private Instance<ViewProductBenefits> viewProductBenefitInstance;
//	@Inject
//	private Instance<ViewPreviousClaims> viewPreviousClaims;
//	@Inject
//	private Instance<InitiatePEDEndorsement> initiatePEDEndorsement;
//	@Inject
//	private Instance<ViewPEDRequestWindow> viewPedRequest;
//	@Inject
//	private ViewStopLossDetails viewStopLoss;
//	@Inject
//	private ViewCoporateBuffer viewCorporateBuffer;
//	@Inject
//	private ViewSublimitDetails viewSublimitDetails;
//	@Inject
//	private ViewTranslationMiscRequestDetails viewTranslationMiscRequestDetails;
//	@Inject
//	private ViewCoOrdinatorReplyDetails viewCoOrdinatorReplyDetails;
//	@Inject
//	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
//	@Inject
//	private ViewPreAuthDetailsMainClass viewPreAuthDetailsMainClass;
//	
//	@Inject
//	private ViewInvestigationDetails viewInvestigationDetails;
//	
//	
//	@Inject
//	private ViewIntimationDetails viewIntimationDetails;
//	
//	
//	@Inject
//	private ViewClaimHistoryRequest viewClaimHistoryRequest;
//	
//	@Inject
//	private Instance<IntimationDetailsCarousel> intimationDetailsCarousel;
//	
//	@Inject
//	private CashLessTableDetails cashLessTableDetails;
//
//	@Inject
//	private CashlessTable cashlessTable;
//
//	@Inject
//	private NewIntimationService newIntimationService;
//
//	@Inject
//	private CashLessTableMapper cashLessTableMapper;
//	
//	@Inject
//	private Intimation intimation;
//	
//	@Inject
//	private ClaimStatusDto claimStatusDto;
//	
//
//	
//	// @Inject
//	// private Instance<ViewPEDEndorsementDetails> viewPEDEndorsementDetails;
//	@PostConstruct
//	public void initView() {
//		addStyleName("view");
//		setSizeFull();
//
//		FormLayout viewDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//		viewDetailsForm.setWidth("-1px");
//		viewDetailsForm.setHeight("-1px");
//		viewDetailsForm.setMargin(false);
//		viewDetailsForm.setSpacing(true);
//		ComboBox viewDetailsSelect = getViewDetailsNativeSelect();
//		
//		
//		
//		viewDetailsForm.addComponent(viewDetailsSelect);
//		
//		Button goButton = getGoButton(viewDetailsSelect);
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm, goButton);
//		//horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
//
//		mainPanel.addComponent(horizontalLayout1);
////		Intimation intimations = intimationService.getIntimationByKey(Long.parseLong("27"));
////		Hospitals hospital = policyService.getVWHospitalByKey(intimations.getHospital());
////		NewIntimationDto intimationDTO = DtoConverter.intimationToIntimationDTO(intimations, hospital);
////		
////		IntimationDetailsCarousel intimationDetailsCarouselInstance =intimationDetailsCarousel.get();
////		intimationDetailsCarouselInstance.init(intimationDTO, "Test");
////		mainPanel.setFirstComponent(intimationDetailsCarouselInstance);
//		
//		mainPanel.setSplitPosition(40);
//		mainPanel.setHeight("100.0%");
//		mainPanel.setWidth("100.0%");
//		setHeight("100.0%");
//		setHeight("600px");
//		setCompositionRoot(mainPanel);
//	}
//
//	private ComboBox getViewDetailsNativeSelect() {
//		ComboBox viewDetailsSelect = new ComboBox("View Details");
//		//Vaadin8-setImmediate() viewDetailsSelect.setImmediate(true);
//		viewDetailsSelect.setWidth("164px");
//		viewDetailsSelect.addItem(VIEW_PED_REQUEST);
//		viewDetailsSelect.addItem(VIEW_STOPLOSS);
//		viewDetailsSelect.addItem(VIEW_CORPORATE_BUFFER);
//		viewDetailsSelect.addItem(VIEW_SUB_LIMIT);
//		viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
//		viewDetailsSelect.addItem(VIEW_CO_ORDINATOR_REPLAY);
//		viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
//		viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
//		viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
//		viewDetailsSelect.addItem(VIEW_INTIMATION);
//		return viewDetailsSelect;
//	}
//
//	private Button getGoButton(final ComboBox viewDetailsSelect) {
//		Button goButton = new Button();
//		goButton.setCaption("Go");
//		//Vaadin8-setImmediate() goButton.setImmediate(true);
//		goButton.addClickListener(new Button.ClickListener() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if (viewDetailsSelect.getValue() != null) {
//
//					String intimationNo = "I/2014/0000002";
//
//					switch (viewDetailsSelect.getValue().toString()) {
//					case VIEW_PED_REQUEST:						
////						ViewPEDRequest viewPEDRequest = viewPedRequest.get();
////						viewPEDRequest.initView("00");						
////						popup = new com.vaadin.ui.Window();
////						popup.setCaption("View PED Request Details");
////						popup.setWidth("65%");
////						popup.setHeight("100%");
////						popup.setContent(viewPEDRequest);
////						popup.setClosable(true);
////						popup.center();
////						popup.setResizable(true);
////						popup.addCloseListener(new Window.CloseListener() {
////							/**
////							 * 
////							 */
////							private static final long serialVersionUID = 1L;
////
////							@Override
////							public void windowClose(CloseEvent e) {
////								System.out.println("Close listener called");
////							}
////						});
////
////						popup.setModal(true);
////						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_STOPLOSS:						
//						viewStopLoss.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Stoploss Details");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewStopLoss);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_CO_ORDINATOR_REPLAY:
//						intimation.setKey(27L);
//						viewCoOrdinatorReplyDetails.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Co-Ordinator Replay");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewCoOrdinatorReplyDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_TRANSLATION_MISC_REQUEST:
//						intimation.setKey(27L);
//						viewTranslationMiscRequestDetails.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Translation/Misc Request");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewTranslationMiscRequestDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_CLAIM_HISTORY:
//						viewClaimHistoryRequest.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Claim History");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewClaimHistoryRequest);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_SPECIALITY_OPINION:
//						intimation.setKey(27L);
//						viewSpecialistOpinionDetails.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Speciality Opinion Details");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewSpecialistOpinionDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_SUB_LIMIT:
//						viewSublimitDetails.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Sub Limit Details");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewSublimitDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_CORPORATE_BUFFER:
//						viewCorporateBuffer.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Corporate Buffer");
//						popup.setWidth("75%");
//						popup.setHeight("40%");						
//						popup.setContent(viewCorporateBuffer);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_INTIATE_INVESTIGATION:						
//						viewPreAuthDetailsMainClass.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Pre Auth Details");
//						popup.setWidth("75%");
//						popup.setHeight("75%");						
//						popup.setContent(viewPreAuthDetailsMainClass);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;					
//					case VIEW_PRE_AUTH_DETAILS:						
//						viewPreAuthDetailsMainClass.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Pre Auth Details");
//						popup.setWidth("75%");
//						popup.setHeight("75%");						
//						popup.setContent(viewPreAuthDetailsMainClass);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;
//					case VIEW_INTIMATION:
//						intimation.setKey(27L);
//						viewIntimationDetails.init(intimation);
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Pre Auth Details");
//						popup.setWidth("75%");
//						popup.setHeight("75%");						
//						popup.setContent(viewIntimationDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//							/**
//							 * 
//							 */
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);
//						break;					
//					}
//
//				} else {
//					// TODO Error message
//				}
//
//			}
//
//		});
//		return goButton;
//	}
//
//	private void getViewClaimStatus(String intimationNo) {
//		final Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//
//		Hospitals hospital = policyService.getVWHospitalByKey(intimation
//				.getHospital());
//
//		NewIntimationDto a_intimationDto = intimationService
//				.getIntimationDto(intimation);
//
//		Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
//
//		if (a_claim != null) {
//			DtoConverter converter = new DtoConverter();
//			ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);
//			if (a_claimDto != null) {
//				claimStatusDto.setClaimDto(a_claimDto);
//				claimStatusDto.setNewIntimationDto(a_claimDto
//						.getNewIntimationDto());
//				ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//						claimStatusDto, intimation.getPolicy()
//								.getActiveStatus() == null,
//						cashLessTableDetails, cashlessTable,
//						cashLessTableMapper, newIntimationService, intimation);
//				UI.getCurrent().addWindow(intimationStatus);
//			}
//		} else {
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					a_intimationDto,
//					intimation.getPolicy().getActiveStatus() == null,
//					cashLessTableDetails, cashlessTable, cashLessTableMapper,
//					newIntimationService, intimation);
//			UI.getCurrent().addWindow(intimationStatus);
//		}
//	}
//
//	private void getViewProductBenefits(String registrationBean) {
//		ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
//				.get();
//		a_viewProductBenefits.showValues(registrationBean);
//		UI.getCurrent().addWindow(a_viewProductBenefits);
//	}
//
//	private void getViewBalanceSumInsured(String intimationNo) {
//		ViewBalanceSumInsured a_viewBalenceSumInsured = viewBalenceSumInsured
//				.get();
//		a_viewBalenceSumInsured.bindFieldGroup(intimationNo);
//		UI.getCurrent().addWindow(a_viewBalenceSumInsured);
//	}
//
//	private void getViewPreviousClaimDetails(String registrationBean) {
//		ViewPreviousClaims a_viewPreviousClaims = viewPreviousClaims.get();
//		a_viewPreviousClaims.showValues(registrationBean);
//		UI.getCurrent().addWindow(a_viewPreviousClaims);
//	}
//
//	private void getViewHospitalDetails(String registrationBean) {
//		ViewHospitalDetails a_viewHospitalDetails = viewHospitalDetails.get();
//		a_viewHospitalDetails.showValues(registrationBean);
//		UI.getCurrent().addWindow(a_viewHospitalDetails);
//	}
//
//	private void getViewIntimation(String registrationBean) {
//
//		final Intimation intimation = intimationService
//				.searchbyIntimationNo(registrationBean);
//
//		Hospitals hospital = policyService.getVWHospitalByKey(intimation
//				.getHospital());
//
//		NewIntimationDto a_intimationDto = intimationService.getIntimationDto(intimation);
//
//		Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
//
//	
//			ViewIntimationRegisteredDetails intimationStatus = new ViewIntimationRegisteredDetails(
//					a_intimationDto, intimation.getPolicy().getActiveStatus() == null);
//			UI.getCurrent().addWindow(intimationStatus);
//		
//	}
//
//	@Override
//	public void resetView() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void list(List<PedEndorsementTableDTO> tableRows) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	
//
//}
