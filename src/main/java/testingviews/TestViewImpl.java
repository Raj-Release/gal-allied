//package testingviews;
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
//import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
//import com.shaic.claim.intimation.CashLessTableDetails;
//import com.shaic.claim.intimation.CashLessTableMapper;
//import com.shaic.claim.intimation.CashlessTable;
//import com.shaic.claim.intimation.ViewIntimationStatus;
//import com.shaic.claim.intimation.create.ViewPolicyDetails;
//import com.shaic.claim.pedrequest.view.ViewPedRequestDetail;
//import com.shaic.claim.preauth.InitiatePEDEndorsement;
//import com.shaic.claim.preauth.view.ViewPreAuthDetailsMainClass;
//import com.shaic.claim.preauth.wizard.view.ViewInvestigationDetails;
//import com.shaic.claim.productbenefit.view.ViewProductBenefits;
//import com.shaic.claim.registration.ViewCopayDetails;
//import com.shaic.claim.registration.ViewHospitalDetails;
//import com.shaic.claim.registration.ViewIntimationRegisteredDetails;
//import com.shaic.claim.registration.ViewPreviousClaims;
//import com.shaic.claim.registration.balancesuminsured.view.ViewBalenceSumInsured;
//import com.shaic.claim.registration.previousinsurance.view.ViewPreviousInsurance;
//import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
//import com.shaic.claim.translationmiscrequest.view.ViewTranslationMiscRequestDetails;
//import com.shaic.domain.Claim;
//import com.shaic.domain.ClaimService;
//import com.shaic.domain.CoPayService;
//import com.shaic.domain.HospitalService;
//import com.shaic.domain.Hospitals;
//import com.shaic.domain.InsuredService;
//import com.shaic.domain.Intimation;
//import com.shaic.domain.IntimationService;
//import com.shaic.domain.InvestigationService;
//import com.shaic.domain.MasCopay;
//import com.shaic.domain.MasterService;
//import com.shaic.domain.NewIntimationService;
//import com.shaic.domain.Policy;
//import com.shaic.domain.PolicyService;
//import com.shaic.domain.PreviousPolicyService;
//import com.shaic.newcode.wizard.dto.ClaimStatusDto;
//import com.shaic.newcode.wizard.dto.NewIntimationDto;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.v7.ui.ComboBox;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.ui.UI;
//import com.vaadin.v7.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.Window.CloseEvent;
//
//public class TestViewImpl extends AbstractMVPView implements TestView {
//
//	private static final long serialVersionUID = 212785642469400353L;
//
//	private static final String VIEW_POLICY = "View Policy";
//	private static final String VIEW_DOCUMENTS = "View Documents";
//	private static final String VIEW_INTIMATION = "View Intimation";
//	private static final String VIEW_MER_DETAILS = "View MER Details";
//	private static final String VIEW_CLAIM_STATUS = "View Claim Status";
//	private static final String VIEW_CLAIM_HISTORY = "View Claim History";
//	private static final String VIEW_CO_PAY_DETAILS = "View Co-pay Details";
//	private static final String PED_REQUEST_DETAILS = "PED Request Details";
//	private static final String VIEW_PRODUCT_BENEFITS = "View Product Benefits";
//	private static final String VIEW_HOSPITAL_DETAILS = "View Hospital Details";
//	private static final String VIEW_BALANCE_SUM_INSURED = "View Balance Sum Insured";
//	private static final String VIEW_INVESTIGATION_DETAILS = "View Investigation Details";
//	private static final String VIEW_PREVIOUS_INSURANCE = "View Previous Insurance Details";
//	private static final String VIEW_PREVIOUS_CLAIM_DETAILS = "View Previous Claim Details";
//	private static final String VIEW_PED_ENDORSEMENT_DETAILS = "View PED Endorsement Details";
//	private static final String VIEW_TRANSLATION_MISC_REQUEST = "View Translation Misc Request";
//	private static final String VIEW_CO_ORDINATOR_REPLAY = "View Co Ordinator Replay";
//	private static final String VIEW_SPECIALITY_OPINION = "View Speciality Opinion";
//	private static final String VIEW_PRE_AUTH_DETAILS = "View Pre Auth Details";
//
//	private VerticalLayout mainPanel;
//	
//	private static Window popup;
//	
//	@Inject
//	private Instance<ViewPreviousInsurance> viewPreviousInsurance;
//
//	@Inject
//	private ViewPolicyDetails viewPolicyDetails;
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
//	@EJB
//	private PreviousPolicyService previousPolicyService;
//
//	@EJB
//	private CoPayService coPayService;
//
//	@EJB
//	private InvestigationService investigationService;
//
//	@Inject
//	private Instance<ViewBalenceSumInsured> viewBalenceSumInsured;
//	
//	@Inject
//	private Instance<ViewHospitalDetails> viewHospitalDetails;
//	
//	@Inject
//	private Instance<ViewProductBenefits> viewProductBenefitInstance;
//	
//	@Inject
//	private Instance<ViewPreviousClaims> viewPreviousClaims;
//	
//	@Inject
//	private Instance<InitiatePEDEndorsement> initiatePEDEndorsement;
//
//	@Inject
//	private Instance<ViewCopayDetails> viewCopayDetails;
//
//	@Inject
//	private Instance<ViewInvestigationDetails> viewInvestigationDetails;
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
//	private ViewClaimHistoryRequest viewClaimHistoryRequest;	
//	
//	@Inject
//	private ViewTranslationMiscRequestDetails viewTranslationMiscRequestDetails;
//	
//	@Inject
//	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
//	
//	@Inject
//	private ViewPreAuthDetailsMainClass viewPreAuthDetailsMainClass;
//	
//	@Inject
//	private ViewPedRequestDetail viewPedRequestDetails;
//	
//	@Inject
//	private ClaimStatusDto claimStatusDto;
//	
//	
//	
//	private String intimationNo = "I/2014/0000322";
//
//	public String getIntimationNo() {
//		return intimationNo;
//	}
//
//	public void setIntimationNo(String intimationNo) {
//		this.intimationNo = intimationNo;
//	}
//
//
//	@PostConstruct
//	public void init()
//	{
//		VerticalLayout mainPanel = new VerticalLayout();
//		addStyleName("view");
//		setSizeFull();
//		
//		FormLayout viewDetailsForm = new FormLayout();
//		viewDetailsForm.setSpacing(true);
//		ComboBox viewDetailsSelect = getViewDetailsNativeSelect();
//		viewDetailsForm.addComponent(viewDetailsSelect);
//		Button goButton = getGoButton(viewDetailsSelect);
//		
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(viewDetailsForm, goButton);
//		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
//		
//		mainPanel.addComponent(horizontalLayout1);
//		mainPanel.setComponentAlignment(horizontalLayout1,
//				Alignment.MIDDLE_RIGHT);
//		setHeight("25px");
//		setCompositionRoot(mainPanel);
//	}
//	// @Inject
//	// private Instance<ViewPEDEndorsementDetails> viewPEDEndorsementDetails;
//	public void initView(String intimationNo) {
//		this.intimationNo = "I/2014/0000010";
//	}
//
//	private ComboBox getViewDetailsNativeSelect() {
//		ComboBox viewDetailsSelect = new ComboBox("View Details");
//		//Vaadin8-setImmediate() viewDetailsSelect.setImmediate(true);
//		viewDetailsSelect.setWidth("164px");
//		viewDetailsSelect.addItem(VIEW_POLICY);
//		viewDetailsSelect.addItem(VIEW_DOCUMENTS);
//		viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
//		viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
//		viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
//		viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
//		viewDetailsSelect.addItem(VIEW_MER_DETAILS);
//		viewDetailsSelect.addItem(VIEW_INTIMATION);
//		viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
//		viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
//		viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
//		viewDetailsSelect.addItem(PED_REQUEST_DETAILS);
//		viewDetailsSelect.addItem(VIEW_PED_ENDORSEMENT_DETAILS);
//		viewDetailsSelect.addItem(VIEW_INVESTIGATION_DETAILS);
//		viewDetailsSelect.addItem(VIEW_PREVIOUS_INSURANCE);
//		viewDetailsSelect.addItem(VIEW_TRANSLATION_MISC_REQUEST);
//		viewDetailsSelect.addItem(VIEW_CO_ORDINATOR_REPLAY);
//		viewDetailsSelect.addItem(VIEW_SPECIALITY_OPINION);
//		viewDetailsSelect.addItem(VIEW_PRE_AUTH_DETAILS);
//
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
//					switch (viewDetailsSelect.getValue().toString()) {
//
//					case VIEW_POLICY:
//						getViewPolicy(intimationNo);
//						break;
//					case VIEW_DOCUMENTS:
//						break;
//					case VIEW_CLAIM_HISTORY:
//						getViewClaimHistory(intimationNo);
//						break;
//					case VIEW_CLAIM_STATUS:
//						getViewClaimStatus(intimationNo);
//						break;
//					case VIEW_PRODUCT_BENEFITS:
//						getViewProductBenefits(intimationNo);
//						break;
//					case VIEW_CO_PAY_DETAILS:
//						getViewCoPay(intimationNo);
//						break;
//					case VIEW_MER_DETAILS:
//						break;
//					case VIEW_INTIMATION:
//						getViewIntimation(intimationNo);
//						break;
//					case VIEW_HOSPITAL_DETAILS:
//						getViewHospitalDetails(intimationNo);
//						break;
//					case VIEW_PREVIOUS_CLAIM_DETAILS:
//						getViewPreviousClaimDetails(intimationNo);
//						break;
//					case VIEW_BALANCE_SUM_INSURED:
//						getViewBalanceSumInsured(intimationNo);
//						break;
//					case PED_REQUEST_DETAILS:
//						getPedRequestDetails(intimationNo);						
//						/*InitiatePEDEndorsement a_initiatePEDEndorsement = initiatePEDEndorsement
//								.get();
//						UI.getCurrent().addWindow(a_initiatePEDEndorsement);*/
//						break;
//					case VIEW_PED_ENDORSEMENT_DETAILS:						
//						break;
//
//					case VIEW_INVESTIGATION_DETAILS:
//						getViewInvestigationDetails();
//						break;
//
//					case VIEW_PREVIOUS_INSURANCE:
//						getViewPreviousInsuranceDetails();
//						break;
//						
//					case VIEW_TRANSLATION_MISC_REQUEST:
//						getTranslationMiscRequest(intimationNo);
//						break;
//					case VIEW_SPECIALITY_OPINION:
//						getSpecialityOpinion(intimationNo);
//						break;
//					case VIEW_CO_ORDINATOR_REPLAY:
//						getTranslationMiscRequest(intimationNo);
////						getCoOrdinatorReplay(intimationNo);
//						break;
//					case VIEW_PRE_AUTH_DETAILS:
//						getPreAuthDetail(intimationNo);
//						break;
//					}
//
//				} else {
//				}
//			}
//		});
//		return goButton;
//	}
//	
//	private void getPedRequestDetails(String intimationNo){
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		viewPedRequestDetails.init(intimation.getKey());
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("View Ped Request Details");
//		popup.setWidth("75%");
//		popup.setHeight("75%");
//		popup.setContent(viewPedRequestDetails);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);		
//	}
//	
//	private void getPreAuthDetail(String intimationNo){
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		viewPreAuthDetailsMainClass.init(intimation);
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("View Pre Auth Details");
//		popup.setWidth("75%");
//		popup.setHeight("75%");
//		popup.setContent(viewPreAuthDetailsMainClass);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//	}
//	
//	private void getSpecialityOpinion(String intimationNo){		
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		viewSpecialistOpinionDetails.init(intimation.getKey());
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("View Speciality Opinion Details");
//		popup.setWidth("75%");
//		popup.setHeight("40%");
//		popup.setContent(viewSpecialistOpinionDetails);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//	}
//	
//	private void getTranslationMiscRequest(String intimationNo){
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		viewTranslationMiscRequestDetails.init(intimation);
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("View Translation/Misc Request");
//		popup.setWidth("75%");
//		popup.setHeight("40%");
//		popup.setContent(viewTranslationMiscRequestDetails);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//	}
//
//		private void getViewPreviousInsuranceDetails() {
//			Intimation intimations = intimationService
//					.searchbyIntimationNo(intimationNo);
//			Policy policy = policyService.getPolicy(intimations.getPolicy()
//					.getPolicyNumber());
//			ViewPreviousInsurance viewPreviousInsuranceInstance = 	viewPreviousInsurance.get();
//			viewPreviousInsuranceInstance.showValue(policy);
//			UI.getCurrent().addWindow(viewPreviousInsuranceInstance);
//		}
//
//	private void getViewInvestigationDetails() {
//		Intimation intimations = intimationService
//				.searchbyIntimationNo(intimationNo);
//		ViewInvestigationDetails a_viewInvestigationDetails = viewInvestigationDetails
//				.get();
//		a_viewInvestigationDetails.showValues(8L);
//		UI.getCurrent().addWindow(a_viewInvestigationDetails);
//	}
//
//	private void getViewCoPay(String intimationNo) {
//		Intimation intimations = intimationService
//				.searchbyIntimationNo(intimationNo);
//		MasCopay copy = coPayService.getByProduct(intimations.getPolicy()
//				.getProduct().getKey());
//		ViewCopayDetails ciewCopayDetailsInstancet = viewCopayDetails.get();
//		ciewCopayDetailsInstancet.showValues(copy, intimations.getPolicy(),intimations.getInsured().getInsuredDateOfBirth());
//		UI.getCurrent().addWindow(ciewCopayDetailsInstancet);
//	}
//
//	private void getViewPolicy(String intimationNo) {
//		final Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNo);
//		Policy policy = policyService.getPolicy(intimation.getPolicy()
//				.getPolicyNumber());
//		viewPolicyDetails.setPolicyServiceAndPolicy(
//				policyService, policy, masterService);
//		viewPolicyDetails.initView();
//		UI.getCurrent().addWindow(viewPolicyDetails);
//	}
//
//	public void getViewClaimStatus(String intimationNo) {
//		final Intimation intimation = intimationService.getIntimationByNo(intimationNo);
//
//		Hospitals hospital = hospitalService.searchbyHospitalKey(intimation
//				.getKey());
//
//		NewIntimationDto intimationToIntimationDetailsDTO = intimationService.getIntimationDto(intimation);
//
//		Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
//
//		if (a_claim != null) {
//			ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);
//			claimStatusDto.setClaimDto(a_claimDto);
//			claimStatusDto
//					.setNewIntimationDto(a_claimDto.getNewIntimationDto());
//			cashLessTableMapper.getAllMapValues();
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					claimStatusDto,
//					intimation.getPolicy().getActiveStatus() == null,
//					cashLessTableDetails, cashlessTable, cashLessTableMapper,
//					newIntimationService, intimation);
//			UI.getCurrent().addWindow(intimationStatus);
//		} else {
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					intimationToIntimationDetailsDTO,
//					intimation.getPolicy().getActiveStatus() == null,
//					cashLessTableDetails, cashlessTable, cashLessTableMapper,
//					newIntimationService, intimation);
//			UI.getCurrent().addWindow(intimationStatus);
//		}
//	}
//	
//	public void getViewClaimHistory(String intimationNo){
//		Intimation intimation = intimationService.getIntimationByNo(intimationNo);
//		if(intimation!=null){
//			viewClaimHistoryRequest.init(intimation);
//			popup = new com.vaadin.ui.Window();
//			popup.setCaption("View Claim History");
//			popup.setWidth("75%");
//			popup.setHeight("40%");
//			popup.setContent(viewClaimHistoryRequest);
//			popup.setClosable(true);
//			popup.center();
//			popup.setResizable(false);
//			popup.addCloseListener(new Window.CloseListener() {
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void windowClose(CloseEvent e) {
//					System.out.println("Close listener called");
//				}
//			});
//
//			popup.setModal(true);
//			UI.getCurrent().addWindow(popup);
//		}
//	}
//
//	private void getViewProductBenefits(String registrationBean) {
//		ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
//				.get();
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(registrationBean);
//		a_viewProductBenefits.showValues(intimation.getPolicy().getPolicyNumber());
//	
//		UI.getCurrent().addWindow(a_viewProductBenefits);
//	}
//
//	private void getViewBalanceSumInsured(String intimationNo) {
//		ViewBalenceSumInsured a_viewBalenceSumInsured = viewBalenceSumInsured
//				.get();
////		a_viewBalenceSumInsured.bindFieldGroup(intimationNo);
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
//	/*private void getViewIntimation(String intimationNo) {
//		Intimation intimation = intimationService.getIntimationByNo(intimationNo);
//		
//		List<Claim> claimByIntimation = claimService
//				.getClaimByIntimation(intimation.getKey());
//		
//		boolean claimExists = (claimByIntimation != null && claimByIntimation
//				.size() > 0) ? true : false;		
//
//		NewIntimationDto intimationToIntimationDetailsDTO = policyService
//				.newIntimationToIntimationDTO(intimation);
//
//		if (intimation.getStatus() != null
//				&& intimation.getStatus().equalsIgnoreCase("SUBMITTED")
//				&& claimExists == true) {
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					intimationToIntimationDetailsDTO, intimation
//							.getPolicy().getStatus() == null,
//					cashLessTableDetails, cashlessTable,
//					cashLessTableMapper, newIntimationService, intimation);
//
//			UI.getCurrent().addWindow(intimationStatus);
//		} else if (intimation.getStatus() != null
//				&& intimation.getStatus().equalsIgnoreCase("SUBMITTED")
//				&& claimExists == false) {
//			ViewIntimation intimationDetails = new ViewIntimation(
//					intimationToIntimationDetailsDTO, hospitalService);
//			UI.getCurrent().addWindow(intimationDetails);
//		}
//
//		final Intimation intimation = intimationService
//				.searchbyIntimationNo(registrationBean);
//
