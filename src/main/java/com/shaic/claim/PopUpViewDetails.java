//package com.shaic.claim;
//
//import javax.ejb.EJB;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.ViewComponent;
//
//import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
//import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyDetails;
//import com.shaic.claim.coporatebuffer.view.ViewCoporateBuffer;
//import com.shaic.claim.intimatino.view.ViewIntimationDetails;
//import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
//import com.shaic.claim.preauth.view.ViewPreAuthDetailsMainClass;
//import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
//import com.shaic.claim.stoploss.view.ViewStopLossDTO;
//import com.shaic.claim.sublimit.ViewSublimitDetails;
//import com.shaic.claim.translationmiscrequest.view.ViewTranslationMiscRequestDetails;
//import com.shaic.domain.ClaimService;
//import com.shaic.domain.HospitalService;
//import com.shaic.domain.InsuredService;
//import com.shaic.domain.Intimation;
//import com.shaic.domain.IntimationService;
//import com.shaic.domain.MasterService;
//import com.shaic.domain.PolicyService;
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
//public class PopUpViewDetails extends ViewComponent {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
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
//	private ViewPEDRequestWindow viewPedRequest;
//
//	@Inject
//	private ViewStopLossDTO viewStopLoss;
//
//	@Inject
//	private ViewCoporateBuffer viewCorporateBuffer;
//
//	@Inject
//	private ViewSublimitDetails viewSublimitDetails;
//
//	@Inject
//	private ViewTranslationMiscRequestDetails viewTranslationMiscRequestDetails;
//
//	@Inject
//	private ViewCoOrdinatorReplyDetails viewCoOrdinatorReplyDetails;
//
//	@Inject
//	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
//
//	@Inject
//	private ViewPreAuthDetailsMainClass viewPreAuthDetailsMainClass;
//
//	@Inject
//	private Intimation intimation;
//
//	@Inject
//	private ViewIntimationDetails viewIntimationDetails;
//
//	@Inject
//	private ViewClaimHistoryRequest viewClaimHistoryRequest;
//
//	private static final String VIEW_PED_REQUEST = "View Ped Request";
//	private static final String VIEW_STOPLOSS = "View Stoploss";
//	private static final String VIEW_CORPORATE_BUFFER = "View Coporate Buffer";
//	private static final String VIEW_SUB_LIMIT = "View Sub Limit";
//	private static final String VIEW_TRANSLATION_MISC_REQUEST = "View Translation Misc Request";
//	private static final String VIEW_CO_ORDINATOR_REPLAY = "View Co Ordinator Replay";
//	private static final String VIEW_SPECIALITY_OPINION = "View Speciality Opinion";
//	private static final String VIEW_PRE_AUTH_DETAILS = "View Pre Auth Details";
//	private static final String VIEW_CLAIM_HISTORY = "View Claim History";
//	private static final String VIEW_INTIMATION = "View Intimation";
//
//	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
//
//	//private static Window popup;
//
//	public void init() {
//		addStyleName("view");
//		setSizeFull();
//		FormLayout viewDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//		viewDetailsForm.setWidth("-1px");
//		viewDetailsForm.setHeight("-1px");
//		viewDetailsForm.setMargin(false);
//		viewDetailsForm.setSpacing(true);
//		ComboBox viewDetailsSelect = getViewDetailsNativeSelect();
//		viewDetailsForm.addComponent(viewDetailsSelect);
//		Button goButton = getGoButton(viewDetailsSelect);
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm, goButton);
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
//		mainPanel.addComponent(horizontalLayout1);
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
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if (viewDetailsSelect.getValue() != null) {
//					switch (viewDetailsSelect.getValue().toString()) {
//					case VIEW_PED_REQUEST:
//						/*viewPedRequest.initView("00");
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View PED Request Details");
//						popup.setWidth("75%");
//						popup.setContent(viewPedRequest);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);*/
//						break;
//					case VIEW_STOPLOSS:
//						/*viewStopLoss.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Stoploss Details");
//						popup.setWidth("75%");
//						popup.setHeight("40%");
//						popup.setContent(viewStopLoss);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							public void windowClose(CloseEvent e) {
//								System.out.println("Close listener called");
//							}
//						});
//
//						popup.setModal(true);
//						UI.getCurrent().addWindow(popup);*/
//						break;
//					case VIEW_CO_ORDINATOR_REPLAY:
//						//viewCoOrdinatorReplyDetails.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Co-Ordinator Replay");
//						popup.setWidth("75%");
//						popup.setHeight("40%");
//						popup.setContent(viewCoOrdinatorReplyDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//
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
//						//viewTranslationMiscRequestDetails.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Translation/Misc Request");
//						popup.setWidth("88%");
//						popup.setHeight("40%");
//						popup.setContent(viewTranslationMiscRequestDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(true);
//						popup.addCloseListener(new Window.CloseListener() {
//
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
//
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
//						//viewSpecialistOpinionDetails.init();
//						popup = new com.vaadin.ui.Window();
//						popup.setCaption("View Speciality Opinion Details");
//						popup.setWidth("75%");
//						popup.setHeight("40%");
//						popup.setContent(viewSpecialistOpinionDetails);
//						popup.setClosable(true);
//						popup.center();
//						popup.setResizable(false);
//						popup.addCloseListener(new Window.CloseListener() {
//
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
//
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
//
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
//
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
//
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
//				}
//			}
//		});
//		return goButton;
//	}
//}
