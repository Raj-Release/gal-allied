package com.shaic.claim.reimbursement.billing.wizard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
/*import org.vaadin.dialogs.ConfirmDialog;*/
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.bpc.ViewBusinessProfileChart;
import com.shaic.claim.coinsurance.view.CoInsuranceDetailView;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.BasePolicyPreviousClaimWindowUI;
import com.shaic.claim.registration.ViewBasePolicyClaimsWindowOpen;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPageViewImpl;
import com.shaic.claim.reimbursement.billing.pages.billingprocess.BillingProcessPageViewImpl;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPageViewImpl;
import com.shaic.claim.reimbursement.billing.pages.billsummary.BillingSummaryPageViewImpl;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.EsclateClaimToRawPage;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.IrdaBillDetailsPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewMedicalSummaryPage;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

public class BillingWizardViewImpl extends AbstractMVPView implements BillingWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	/*@Inject
	private ViewDetails viewDetails;*/
	
	////private static Window popup;
	
	/*@Inject
	private ReconsiderRODRequestTable reconsiderRequestDetails;
	*/
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<BillingReviewPageViewImpl> billingReviewPageViewImpl;
	
	@Inject
	private Instance<BillingSummaryPageViewImpl> billingSummaryPageViewImpl;
	
	@Inject
	private Instance<BillingProcessPageViewImpl> billingProcessPageViewImpl;
	
	@Inject
	private Instance<BillingHospitalizationPageViewImpl> billingHospitalizationViewImpl;
	
	private BillingReviewPageViewImpl billingReviewPageViewImplObj;
	
	private BillingSummaryPageViewImpl billingSummaryPageViewImplObj;
	
	private BillingProcessPageViewImpl billingProcessPageViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	private BillingHospitalizationPageViewImpl billingHospitalizationPageViewImplObj;
	
	@Inject
	private ViewMedicalSummaryPage viewMedicalSummaryPage;
	

	@Inject		
	private ViewBusinessProfileChart viewBusinessProfileChart;	
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private IrdaBillDetailsPage viewIRDABillSummaryPage;

	//private ComboBox cmbReconsiderationRequest;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	private ComboBox cmbReasonForReconsideration;
	 
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	
	private ComboBox cmbReconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	@Inject
	private ViewDetails viewDetails;
	
	public Button btnViewRTABalanceSI;
	
	 @Inject
		private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
		 
	@EJB
		private PolicyService policyService;
	
	@EJB
	private CreateRODService createRodService;
		 
	 @Inject
		private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	private final Logger log = LoggerFactory.getLogger(BillingWizardViewImpl.class);
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	@Inject
	private Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance;
	
	private EsclateClaimToRawPage esclateClaimToRawPageViewObj;
	
	private Button btnViewPolicySchedule;
	
	private Button btnBusinessProfileChart;
	
	@Inject
	private Toolbar toolBar;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;

	@Inject
	private CoInsuranceDetailView coInsuranceDetailsView;
	
	private Button viewLinkPolicyDtls;
	
	private NativeSelect viewBasePolicyDetailsSelect;
	private Button btnGo;
	
	private EnhancedBrowserWindowOpener sopener;
	
	private static final String VIEW_BASE_POLICY_CLAIMS = "Policy Claims Details";

	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	@Inject
	private Instance<ViewBasePolicyClaimsWindowOpen> ViewBasePolicyClaimsWindowOpen;
	
	private Long PolicyKey = null;
	
	private TextArea autoAllocCancelRemarks;
	
	private void initBinder() {
		wizard.getFinishButton().setCaption("Submit");
		
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");
//		item.addNestedProperty("preauthMedicalDecisionDetails");

		item.addNestedProperty("preauthDataExtractionDetails.reasonForAdmission");
		item.addNestedProperty("preauthDataExtractionDetails.admissionDate");
		item.addNestedProperty("preauthDataExtractionDetails.noOfDays");
		item.addNestedProperty("preauthDataExtractionDetails.natureOfTreatment");
		item.addNestedProperty("preauthDataExtractionDetails.firstConsultantDate");
		item.addNestedProperty("preauthDataExtractionDetails.corpBuffer");
		item.addNestedProperty("preauthDataExtractionDetails.criticalIllness");
		item.addNestedProperty("preauthDataExtractionDetails.specifyIllness");
		item.addNestedProperty("preauthDataExtractionDetails.roomCategory");
		item.addNestedProperty("preauthDataExtractionDetails.ventilatorSupport");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentType");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentRemarks");

		item.addNestedProperty("preauthPreviousClaimsDetails.relapseOfIllness");
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseRemarks");
		item.addNestedProperty("preauthPreviousClaimsDetails.attachToPreviousClaim");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReportReviewed");
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReviewRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigatorName");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTaken");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistType");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistConsulted");
//		item.addNestedProperty("preauthMedicalDecisionDetails.remarksBySpecialist");
//		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrTriggerPoints");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTakenFlag");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequestFlag");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.approvedAmount");
//		item.addNestedProperty("preauthMedicalDecisionDetails.selectedCopay");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initialTotalApprovedAmt");
//		item.addNestedProperty("preauthMedicalDecisionDetails.approvalRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.queryRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionCategory");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForDenial");
//		item.addNestedProperty("preauthMedicalDecisionDetails.denialRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalateTo");
//		// item.addNestedProperty("preauthMedicalDecisionDetails.upLoadFile");
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalationRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.typeOfCoordinatorRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForRefering");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.sentToCPU");
//		item.addNestedProperty("preauthMedicalDecisionDetails.remarksForCPU");

		item.addNestedProperty("coordinatorDetails.refertoCoordinator");
		item.addNestedProperty("coordinatorDetails.typeofCoordinatorRequest");
		item.addNestedProperty("coordinatorDetails.reasonForRefering");
		this.binder.setItemDataSource(item);	
	}
	
	@PostConstruct
	public void initView() {

		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(PreauthDTO bean)
	{
		this.bean = bean;
		mainPanel = new VerticalSplitPanel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizardPartialComplete();
		initBinder();
		billingReviewPageViewImplObj = billingReviewPageViewImpl.get();
		billingReviewPageViewImplObj.init(this.bean , wizard);
		wizard.addStep(billingReviewPageViewImplObj,"Bill Review");
		
		billingSummaryPageViewImplObj = billingSummaryPageViewImpl.get();
		billingSummaryPageViewImplObj.init(this.bean);
		wizard.addStep(billingSummaryPageViewImplObj,"View Bill Summary");
		
		billingProcessPageViewImplObj = billingProcessPageViewImpl.get();
		billingProcessPageViewImplObj.init(this.bean);
		wizard.addStep(billingProcessPageViewImplObj,"Biliing Process");
		
		billingHospitalizationPageViewImplObj = billingHospitalizationViewImpl.get();
		billingHospitalizationPageViewImplObj.init(this.bean, wizard);
		wizard.addStep(billingHospitalizationPageViewImplObj,"Biling Hospitalization");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Billing");
		String caption= "Process Claim Billing";
		if(bean.getScreenName() !=null 
				&& bean.getScreenName().equals(SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)){
			caption = SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
		}
		intimationDetailsCarousel.init(this.bean,caption);
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		wizardLayout1.setSpacing(true);
		
		if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			wizardLayout1.addComponent(getReconsiderDetailsTable());
		}		
		
		if(bean.getIsReferToBilling()) {
			TextField field1 = new TextField("Reason for Referring to Billing");
			field1.setValue(bean.getPreviousReasonForReferring());
			field1.setReadOnly(true);
			field1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			TextField field2 = new TextField("Financial Approver Remarks");
			field2.setValue(bean.getPreviousRemarks());
			field2.setReadOnly(true);
			field2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			field2.setDescription(bean.getPreviousRemarks());
			FormLayout fLayout = new FormLayout(field1, field2);
			fLayout.setSpacing(true);
			VerticalLayout layout = new VerticalLayout(fLayout);
			layout.setCaption("Financial Approval Details");
			wizardLayout1.addComponent(layout);
		}
		
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		if(this.bean.getTaskNumber() != null){/*
			String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}
		*/}
		
	}
	
	private void showPopup(Layout layout) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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
	
	private VerticalLayout commonButtonsLayout() {
		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
		
		if(this.bean.getIsCashlessType() && this.bean.getHospitalizaionFlag()&& this.bean.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)){
			typeFld.setValue(SHAConstants.DIRECT_TO_BILLING);
		}
		typeFld.setReadOnly(true);
		
		TextField hospitalNtwrktype = new TextField("Hospital Network Type");
		hospitalNtwrktype.setNullRepresentation("");
		hospitalNtwrktype.setValue(this.bean.getNetworkHospitalType());
		hospitalNtwrktype.setReadOnly(true);
//		hospitalNtwrktype.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField preferredNetworkType = new TextField();
		preferredNetworkType.setValue("Preferred Network Hospital");
		preferredNetworkType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		preferredNetworkType.setReadOnly(true);
		preferredNetworkType.setStyleName("preferred");
		preferredNetworkType.setVisible(false);
		
		FormLayout hLayout = new FormLayout (typeFld,hospitalNtwrktype);
		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);

		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		if (null != memberType && !memberType.isEmpty()) {
			hLayout.addComponent(cmdClubMembership);
		}
		
		HorizontalLayout hosHor = new HorizontalLayout(hLayout,preferredNetworkType);
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital() != null 
					&& bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital().equalsIgnoreCase("Y")){
				preferredNetworkType.setVisible(true);
			}
			
		}
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),null);
				showPopup(new VerticalLayout(earlierRodDetailsViewObj));
			}
			
		});
		
//		Button msgButton = new Button("Message Button");
//		msgButton.addClickListener(new ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				Label successLabel = new Label(
//						"<b style = 'color: green;'> Cashless Claim Record Saved Successfully.</b>",
//						ContentMode.HTML);
//
//				// Label noteLabel = new
//				// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
//				// ContentMode.HTML);
//
//				Button homeButton = new Button("Pre-auth Enhancement Home");
//				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
//				layout.setSpacing(true);
//				layout.setMargin(true);
//				HorizontalLayout hLayout = new HorizontalLayout(layout);
//				hLayout.setMargin(true);		
//
//				final ConfirmDialog dialog = new ConfirmDialog();
//				dialog.setCaption("");
//				dialog.setClosable(false);
//				dialog.setContent(hLayout);
//				dialog.setResizable(false);
//				dialog.setModal(true);		
//				dialog.show(getUI().getCurrent(), null, true);
//				dialog.setStyleName("boxshadow");
//				homeButton.addClickListener(new ClickListener() {
//					private static final long serialVersionUID = 7396240433865727954L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						dialog.close();
//						fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT, null);
//
//					}
//				});
//				
//			}
//		});
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				/*popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("85%");
				popup.setHeight("100%");
				rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
				//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				//documentDetails.setClaimDto(bean.getClaimDTO());
				rewardRecognitionRequestViewObj.initPresenter(SHAConstants.BILLING);
				rewardRecognitionRequestViewObj.init(bean, popup);
				
				//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
				popup.setCaption("Reward Recognition Request");
				popup.setContent(rewardRecognitionRequestViewObj);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);*/
				
				
			}
			
		});
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
		
		
		Button viewMedicalSummaryButton = new Button("View Medical Summary");
		viewMedicalSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewMedicalSummaryPage.init(bean.getKey());
				showPopup(new VerticalLayout(viewMedicalSummaryPage));
			}
			
		});
		
		Button viewBillSummaryButton = new Button("View Bill Summary");
		viewBillSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				/*popup.setWidth("75%");
				popup.setHeight("85%");*/
				popup.setSizeFull();
				viewBillSummaryPage.init(bean,bean.getKey(),true);
				Panel mainPanel = new Panel(viewBillSummaryPage);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
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
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				
			}
			
		});
		
		Button viewIRDASummaryButton = new Button("View IRDA Bill Summary");
		viewIRDASummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewIRDABillSummaryPage.init(bean.getKey());
				showPopup(new VerticalLayout(viewIRDABillSummaryPage));
			}
			
		});
		
		btnViewPolicySchedule = new Button("View Policy Schedule");
		if (!ReferenceTable.getGMCProductList().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
		}
		else
		{
			bean.setIsScheduleClicked(true);
		}
		
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			btnViewPolicySchedule.setEnabled(false);
		}
		btnViewPolicySchedule.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				policyScheduleAction();
			}
		});
		
		btnBusinessProfileChart = new Button("View Mini Business Profile");
		btnBusinessProfileChart.setStyleName(ValoTheme.BUTTON_DANGER);
		btnBusinessProfileChart.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				viewBusinessProfileChart.init(bean);
				
				bean.setIsBusinessProfileClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("VIEW MINI BUSINESS PROFILE");
				popup.setWidth("35%");
				popup.setHeight("75%");
				popup.setContent(viewBusinessProfileChart);
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
		
		/***
		 * new button added bellow for GLX2020065 
		 */
		Button btnPolicyScheduleWithoutRisk = new Button("Policy Schedule without Risk");
		if(ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			btnPolicyScheduleWithoutRisk.setEnabled(true);
		}else{
			btnPolicyScheduleWithoutRisk.setEnabled(false);
		}
		
		btnPolicyScheduleWithoutRisk.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				viewDetails.getViewPolicyScheduleWithoutRisk(bean.getNewIntimationDTO().getIntimationId());
				Button button = (Button)event.getSource();
			
				
			}
		});
		
		btnViewRTABalanceSI = new Button("View RTA Sum Insured");
		//btnViewRTABalanceSI.setStyleName(ValoTheme.BUTTON_DANGER);
		//btnViewRTABalanceSI.setEnabled(false);
		btnViewRTABalanceSI.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long policyKey = bean.getNewIntimationDTO().getPolicy().getKey();
				Long insuredKey = bean.getNewIntimationDTO().getInsuredPatient().getKey();
				Long claimKey = bean.getClaimDTO().getKey();
				viewDetails.getViewRTAsumInsured(policyKey, insuredKey, claimKey);;
			}
		});
		
		//VerticalLayout viewEarlierRODLayout = new VerticalLayout(btnRRC, viewMedicalSummaryButton, viewEarlierRODDetails, viewBillSummaryButton,viewIRDASummaryButton );
		//viewEarlierRODLayout.setComponentAlignment(btnRRC, Alignment.MIDDLE_RIGHT);
	//	viewEarlierRODLayout.setSpacing(true);
		
		Button btnZUAAlert = new Button("View ZUA History");
		btnZUAAlert.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues = SHAUtils.setViewTopZUAQueryHistoryTableValues(
						bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
				

				List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues = SHAUtils.setViewZUAQueryHistoryTableValues(
						bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
				
				
				if(null != setViewZUAQueryHistoryTableValues && !setViewZUAQueryHistoryTableValues.isEmpty())
				{
					
				Policy policyObj = null;
				VerticalLayout verticalLayout = null;
				if (bean.getNewIntimationDTO().getPolicy().getPolicyNumber() != null) {
					policyObj = policyService.getByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							zuaTopViewQueryTableBancs.init(" ",false,false);
							zuaTopViewQueryTableBancs.setTableList(setViewZUAQueryHistoryTableValues);//pending
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalZUALayout.addComponent(zuaTopViewQueryTableBancs);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalZUALayout);
						}
						else{
							zuaTopViewQueryTable.init(" ",false,false);
							zuaTopViewQueryTable.initTable();						
							
							zuaViewQueryHistoryTable.init(" ", false, false); 
							
							
							zuaViewQueryHistoryTable.setTableList(setViewZUAQueryHistoryTableValues);
							zuaTopViewQueryTable.setTableList(setViewTopZUAQueryHistoryTableValues);
							VerticalLayout verticalTopZUALayout = new VerticalLayout();
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalTopZUALayout.addComponent(zuaTopViewQueryTable);
							verticalZUALayout.addComponent(zuaViewQueryHistoryTable);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalTopZUALayout,verticalZUALayout);
					    	verticalLayout.setComponentAlignment(verticalTopZUALayout,Alignment.TOP_CENTER );  
						}
					}
				}
			
				
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("70%");
				popup.setHeight("70%");
				popup.setContent(verticalLayout);
				popup.setCaption("ZUA History");
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.setDraggable(true);
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
				else
				{
					getErrorMessage("ZUA History is not available");
				}
				
			}
		});
		
		Button elearnBtn = viewDetails.getElearnButton();
		
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		
		HorizontalLayout forLayout = SHAUtils.newImageCRM(bean);
		
		Label activityAgeing = new Label();
		Label claimAgeing = new Label();
		if(bean.getDocumentReceivedFromId().equals(SHAConstants.DOC_RECIEVED_FROM_INSURED_ID))
		{
			Long wkKey = dbService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.BILLING_CURRENT_KEY);
			Map<String, Integer> ageingValues = dbService.getClaimAndActivityAge(wkKey,bean.getClaimDTO().getKey(),(bean.getKey()).toString());
			Integer activityAge = ageingValues.get(SHAConstants.LN_ACTIVITY_AGEING);
			Integer claimAge = ageingValues.get(SHAConstants.LN_CLAIM_AGEING);
			
			activityAgeing.setDescription("Activity Ageing");
			if(activityAge != null)
			{
				if(activityAge >= 4 && activityAge <= 7)
				{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity_Y.png"));
				}
				else if(activityAge > 7)
				{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity_R.png"));
				}
				else{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity.png"));
				}
			}

			claimAgeing.setDescription("Claim Ageing");
			if(claimAge != null)
			{
				if(claimAge >= 4 && claimAge <= 7)
				{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing_Y.png"));
				}
				else if(claimAge > 7)
				{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing_R.png"));
				}
				else{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing.png"));
				}
			}
		}
		
		HorizontalLayout coInsurance = SHAUtils.coInsuranceIcon(bean, coInsuranceDetailsView);
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
		HorizontalLayout crmLayout = new HorizontalLayout(forLayout,activityAgeing,claimAgeing);

		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranch(bean);
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean);
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean);
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		
		Button btnInsuredChannelName = new Button("Insured/Channel Name");
		btnInsuredChannelName.setStyleName(ValoTheme.BUTTON_DANGER);
		btnInsuredChannelName.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				HorizontalLayout insuredChannelName= SHAUtils.getInsuredChannedName(bean);
				bean.setIsInsuredChannedNameClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
			
			}
		});		
		icrAgentBranch.addComponent(btnInsuredChannelName); 
		HorizontalLayout hlayout=new HorizontalLayout(crmLayout,coInsurance, hopitalFlag);
		hlayout.setSpacing(true);
		VerticalLayout icrAGBR = new VerticalLayout(hlayout,icrAgentBranch);
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		
		//GLX2020162 topup policy creation for HC policy
		viewLinkPolicyDtls = new Button("View Linked Policy");
		Policy linkPolicyKey = null;
		if((bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()) ||(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty())){
			if(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo());
			}else if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo());
			}
			viewLinkPolicyDtls.setVisible(true);
		}else {
			viewLinkPolicyDtls.setVisible(false);
		}
		if(linkPolicyKey != null && linkPolicyKey.getKey() != null){
			PolicyKey = linkPolicyKey.getKey();
		final ShortcutListener sListener = callSListener();
		final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,linkPolicyKey.getKey());					
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
		opener.popupBlockerWorkaround(true);

		opener.withShortcut(sListener);
		opener.setFeatures("height=700,width=1300,resizable");
		opener.doExtend(viewLinkPolicyDtls);
		//					setSopener(opener);
		viewLinkPolicyDtls.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,PolicyKey);					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
				opener.open();
			}
		});
		}
		/*HorizontalLayout layout = new HorizontalLayout(btnRRC,viewMedicalSummaryButton, viewEarlierRODDetails,elearnBtn);
		//layout.setComponentAlignment(viewEarlierRODLayout, Alignment.MIDDLE_RIGHT);
		layout.setSpacing(true);*/
		HorizontalLayout Layout1 = null;
		HorizontalLayout Layout2 = null;
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| /*(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_72) ||
							bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_87))
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_81)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_012))
					&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			btnViewRTABalanceSI.setEnabled(this.bean.getPreauthDataExtractionDetails().getIsRTAButtonEnable());
			Layout1 = new HorizontalLayout(btnRRC,btnLumen,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,btnViewRTABalanceSI,viewLinkPolicyDtls,viewMedicalSummaryButton, viewEarlierRODDetails,viewBillSummaryButton,viewIRDASummaryButton,btnZUAAlert,elearnBtn);
		}
		else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
			if(bean.getPolicyDto().getGmcPolicyType() != null &&
					(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))) {
				Layout1 = new HorizontalLayout(btnRRC,btnLumen,elearnBtn,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewMedicalSummaryButton, viewEarlierRODDetails,viewBillSummaryButton,viewIRDASummaryButton,btnZUAAlert,viewLinkedPolicy,icrLayout);
			} else {
				Layout1 = new HorizontalLayout(btnRRC,btnLumen,elearnBtn,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewMedicalSummaryButton, viewEarlierRODDetails,viewBillSummaryButton,viewIRDASummaryButton,btnZUAAlert,icrLayout);
			}
		}
		else
		{
			Layout1 = new HorizontalLayout(btnRRC,btnLumen,elearnBtn,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewLinkPolicyDtls,viewMedicalSummaryButton, viewEarlierRODDetails,viewBillSummaryButton,viewIRDASummaryButton,btnZUAAlert);
		}
		Layout1.setSpacing(true);
		VerticalLayout vLayout = new VerticalLayout(icrAGBR,Layout1);
				vLayout.setSpacing(true);
		HorizontalLayout viewDetailsForm = new HorizontalLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PREAUTH_MEDICAL,"Process Claim Billing");
		viewDetailsForm.addComponent(viewDetails);
	//	Label dummy = new Label();
	//	dummy.setWidth("300px");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(/*hLayout*/vLayout, viewDetailsForm);
//		HorizontalLayout horizontalLayout = new HorizontalLayout(viewEarlierRODLayout);
		componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		
		TextArea remarksTxt = new TextArea("Doctor Remarks");
		remarksTxt.setHeight("70%");
		remarksTxt.setValue(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setDescription(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setReadOnly(true);
		
		HorizontalLayout formLayout = null;
		if((this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) || (this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty())) {
			Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
			formLayout = new HorizontalLayout(hosHor,commonValues(),remarksTxt,insuredPedDetailsPanel);
			formLayout.setComponentAlignment(remarksTxt, Alignment.MIDDLE_LEFT);
			formLayout.setComponentAlignment(insuredPedDetailsPanel, Alignment.MIDDLE_LEFT);
		}else{
			formLayout = new HorizontalLayout(hosHor,commonValues(),remarksTxt);
		}
		
		formLayout.setSpacing(true);
		formLayout.setMargin(false);
		
//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());		
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		VerticalLayout verLayout = new VerticalLayout(formLayout,crmFlaggedComponents);
		verLayout.setMargin(false);
		verLayout.setSpacing(true);
		
		VerticalLayout verticalLayout = new VerticalLayout(componentsHLayout,verLayout);
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
//		horizontalLayout.setWidth("100%");
		return verticalLayout;
	}
	
	private Panel getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
		table.setWidth("80%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.addContainerProperty("pedEffectiveFromDate",  Timestamp.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
					table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription(),pedDetails.getPedEffectiveFromDate()}, i+1);
					i++;
				}
			}
		}
		
		if(this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty()){
			for (PreExistingDisease component : this.bean.getApprovedPedDetails()) {
				table.addItem(new Object[]{component.getCode(), component.getValue()}, i+1);
				i++;
			}
		}
		
		
		table.setPageLength(2);
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedDescription", "Description");
		table.setColumnHeader("pedEffectiveFromDate", "PED Effective from date");
		table.setColumnWidth("pedCode", 70);
		table.setColumnWidth("pedDescription", 250);
		table.setColumnWidth("pedEffectiveFromDate", 200);
		
		Panel tablePanel = new Panel(table);
		return tablePanel;
	}
	
private VerticalLayout commonValues() {
		
		cmbReconsiderationRequest = new ComboBox("Reconsideration Request");
		//cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		String reconsiderationFlag = "";
		List<SelectValue> values = new ArrayList<SelectValue>();
		SelectValue value = new SelectValue();
		if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			reconsiderationFlag = "Yes";
			value.setId(1l);
			value.setValue("Yes");
		}
		else
		{
			reconsiderationFlag = "No";
			value.setId(1l);
			value.setValue("No");
		}
		values.add(value);
		BeanItemContainer<SelectValue> beanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		beanContainer.addAll(values);
		cmbReconsiderationRequest.setContainerDataSource(beanContainer);
		cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		cmbReconsiderationRequest.setReadOnly(false);
		
		for(int i = 0 ; i<beanContainer.size() ; i++)
		{
			 if(null != beanContainer.getIdByIndex(i) && null != beanContainer.getIdByIndex(i).getValue())
			 {
				if ((reconsiderationFlag).equalsIgnoreCase(beanContainer.getIdByIndex(i).getValue().trim()))
				{
					this.cmbReconsiderationRequest.setValue(beanContainer.getIdByIndex(i));
				}
			 }
		}
		cmbReconsiderationRequest.setReadOnly(true);
		
		//cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration = new ComboBox("Reason for Reconsideration");
		List<SelectValue> reconsiderValues = new ArrayList<SelectValue>();
		SelectValue selValue = new SelectValue();
		String reasonForReconsideration = "";
		if(null  != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		{
			reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
			selValue.setId(1l);
			selValue.setValue(reasonForReconsideration);
		}
		reconsiderValues.add(selValue);
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderBeanContainer.addAll(reconsiderValues);
		cmbReasonForReconsideration.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setReadOnly(false);

		 if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		 {
			 reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
		 }
		
		 for(int i = 0 ; i<reconsiderBeanContainer.size() ; i++)
		 	{
				if ((reasonForReconsideration).equalsIgnoreCase(reconsiderBeanContainer.getIdByIndex(i).getValue()))
				{
					this.cmbReasonForReconsideration.setValue(reconsiderBeanContainer.getIdByIndex(i));
				}
			}
		 cmbReasonForReconsideration.setReadOnly(true);
		
/*//		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
//		reconsiderRequestDetails.initPresenter(SHAConstants.ZONAL_REVIEW);
//		reconsiderRequestDetails.init();
//		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
//		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
//		{
//			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
//				//if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
//				{
//					//Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
//					reconsiderList.setSelect(true);
//				}
//				//reconsiderList.setSelect(null);
//				reconsiderRequestDetails.addBeanToList(reconsiderList);
//			}
//			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
//		}
//		reconsiderRequestDetails.setEnabled(false);
//		
//		
//		HorizontalLayout vLayout = new HorizontalLayout(reconsiderRequestDetails);*/
		 
		 //vLayout.setMargin(true);
//		vLayout.setCaption("Earlier Request Reconsidered");
		
		FormLayout fLayout = new FormLayout(cmbReconsiderationRequest);
		fLayout.setSpacing(true);
//		fLayout.setMargin(false);
		FormLayout fLayout1 = new FormLayout(cmbReasonForReconsideration);
		
		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
		{
			fLayout.addComponent(cmbReasonForReconsideration);
//			reconsiderationLayout = new VerticalLayout(fLayout,fLayout1/*, vLayout*/);
			reconsiderationLayout = new VerticalLayout(fLayout);
		}
		else
		{
			reconsiderationLayout = new VerticalLayout(fLayout);
		}
		
		return reconsiderationLayout;
	}
	private ReconsiderRODRequestListenerTable getReconsiderDetailsTable(){
		
		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		reconsiderRequestDetails.initPresenter(SHAConstants.ZONAL_REVIEW);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				//if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
				{
					//Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(true);
				}
				//reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
			}
			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}
		reconsiderRequestDetails.setEnabled(false);
		
		return reconsiderRequestDetails;
	
	}

	@Override
	public void resetView() {
		/*if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap("Bill Review");
			this.wizard.clearWizardMap("View Bill Summary");
			this.wizard.clearWizardMap("Biliing Process");
			this.wizard.clearWizardMap("Biling Hospitalization");
			this.wizard.clearCurrentStep();
		}*/
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		
		if(isInsured() 
				&& bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
			MessageBox getContRRC = MessageBox
					.createQuestion()
					.withCaptionCust("Confirmation")
					.withMessage("Contribute RRC ?")
					.withYesButton(ButtonOption.caption("Yes"))
					.withNoButton(ButtonOption.caption("No"))
					.open();
			Button yesBtn =getContRRC.getButton(ButtonType.YES);
			yesBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					getContRRC.close();
					finalSubmit();
					validateUserForRRCRequestIntiation();
				}
			});	

			Button noBtn = getContRRC.getButton(ButtonType.NO);
			noBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					getContRRC.close();
					finalSubmit();
				}
			});
			
		}else{
			finalSubmit();
		}
			
	}
	private void finalSubmit(){
		try
		{
			if(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey())){
				fireViewEvent(BillingWizardPresenter.BILLING_HOLD_SUBMIT, this.bean);
			}else{

				Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(bean.getStageKey(), bean.getStatusKey(), this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue():0l, 
						bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey(), bean.getKey(), "C",getUI());
				if(! alertMessageForProvisionValidation){
					if(this.bean.getIsHospitalDiscountApplicable() && 
							this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() == 0){
						confirmMessageForHospitalDiscount();
					} else{
						getSession().setAttribute(SHAConstants.TOKEN_ID, null);
						if(this.bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
							if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
								warningMessageForCopay();
							}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
								alertMessageForCopay();
							}else{
								fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
							}
						}else{
							fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
						}
					}
				}
			}

		}catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}	
	}
	

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									SHAUtils.setClearPreauthDTO(bean);
									fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING,
											null);
									billingReviewPageViewImplObj.setClearReferenceData();
									billingProcessPageViewImplObj.setClearReferenceData();
									billingHospitalizationPageViewImplObj.setClearReferenceData();
									VaadinRequest currentRequest = VaadinService.getCurrentRequest();
									SHAUtils.clearSessionObject(currentRequest);
									
								} else {
									// User did not confirm
								}
							}
						});
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Confirmed to continue
				if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					autoAllocationCancelRemarksLayout();
				}
				else {
					releaseHumanTask();
					SHAUtils.setClearPreauthDTO(bean);
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING,
							null);
					clearObject();
					billingReviewPageViewImplObj.setClearReferenceData();
					billingProcessPageViewImplObj.setClearReferenceData();
					billingHospitalizationPageViewImplObj.setClearReferenceData();
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
				}
			}
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			}
			});
	}
	
	public void confirmMessageForHospitalDiscount(){
		
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Hospital  is eligible for Discount !!!",
						"Do you want to proceed without Discount ? ",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									getSession().setAttribute(SHAConstants.TOKEN_ID, null);
									if(bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
										if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
											warningMessageForCopay();
										}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
											alertMessageForCopay();
										}else{
											fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
										}
									}else{
										fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
									}
								} else {
									wizard.getFinishButton().setEnabled(true);
								}
							}
						});
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
		
		/*HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Hospital  is eligible for Discount !!!", "Do you want to proceed without Discount ? ", buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Hospital  is eligible for Discount !!!, \nDo you want to proceed without Discount ? ", buttonsNamewithType);
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				// Confirmed to continue
				getSession().setAttribute(SHAConstants.TOKEN_ID, null);
				if(bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
					if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0)){
						warningMessageForCopay();
					}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0)){
						alertMessageForCopay();
					}else{
						fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
					}
				}else{
					fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
				}
			
			}
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				wizard.getFinishButton().setEnabled(true);
			
			}
			});
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim record saved successfully !!!.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		/*Button homeButton = new Button("Billing Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		String btnCaption = "";
		if(bean.getScreenName() != null
				&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
			btnCaption = SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
		}
		else {
			btnCaption = "Billing Home";
		}
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Claim record saved successfully !!!.", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				//dialog.close();
                toolBar.countTool();
				SHAUtils.setClearPreauthDTO(bean);
				if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION,null);
				}
				else {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
				}
				
				clearObject();
				billingReviewPageViewImplObj.setClearReferenceData();
				billingProcessPageViewImplObj.setClearReferenceData();
				billingHospitalizationPageViewImplObj.setClearReferenceData();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);

			}
		});
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)) {
			SHAUtils.showAlertMessageBox(SHAConstants.RAKSHAK_PRODUCT_ALERT_MESSAGE);
		}
		
	}
	
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}


	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;	
	}

	
	@Override
	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		
		rewardRecognitionRequestViewObj.loadEmployeeMasterData(field,  employeeDetailsList);
			
	}
	
	private void validateUserForRRCRequestIntiation(){

		if(billingReviewPageViewImplObj !=null){
			Double totalClimedAmt =billingReviewPageViewImplObj.getTotalClaimedAmtForRRC();
			if(totalClimedAmt !=null){
				bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(totalClimedAmt.longValue());
			}else{
				bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(0l);
			}
		}
		if(bean.getIsReconsiderationRequest() != null 
				&& bean.getIsReconsiderationRequest()) {
			if(billingHospitalizationPageViewImplObj !=null){
				Long HospitalizationApprovedamt = billingHospitalizationPageViewImplObj.getHospitalizationApprovedamt();
				if(HospitalizationApprovedamt == 0){
					HospitalizationApprovedamt = bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt().longValue();
				}
				Long approvedAmuntForRRC= HospitalizationApprovedamt +
						bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() +
						bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt();
				bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(approvedAmuntForRRC);
			}
		}else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			if(billingHospitalizationPageViewImplObj !=null){
				Long HospitalizationApprovedamt = billingHospitalizationPageViewImplObj.getHospitalizationCashlessApprovedamt();
				Long approvedAmuntForRRC= HospitalizationApprovedamt +
						bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() +
						bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt();
				bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(approvedAmuntForRRC);
			}
		}
		fireViewEvent(BillingWizardPresenter.VALIDATE_BILLING_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				/*Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Same user cannot raise request more than once from same stage", buttonsNamewithType);
				
			} 
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.BILLING);
			rewardRecognitionRequestViewObj.init(bean, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
		}
	
	private void invokePreMedicalLumenRequest(){
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		/*listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);*/
		if(!listOfSettledStatus.contains(bean.getClaimDTO().getStatusId())){
			fireViewEvent(BillingWizardPresenter.VALIDATE_BILLING_USER_LUMEN_REQUEST, bean);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Billing");
		
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(initiateLumenRequestWizardObj);
		popup.setContent(containerLayout);
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
	}
	
	 public void compareWithUserId(String userId) {
		 
		 if(userId == null){
			 userId = "";
		 }

		 /*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING,null);
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);*/
	
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Intimation is already opened by another user"+userId, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING,null);
					//dialog.close();
				}
			});
 }
	 
	 @Override
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Bill Entry Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
		 	String btnCaption = "";
			if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
			}
			else {
				btnCaption = "Bill Entry Home";
			}
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					releaseHumanTask();
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION,null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
					}
					clearObject();
					billingReviewPageViewImplObj.setClearReferenceData();
					billingProcessPageViewImplObj.setClearReferenceData();
					billingHospitalizationPageViewImplObj.setClearReferenceData();
					SHAUtils.setClearPreauthDTO(bean);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
	 
	 public void warningMessageForCopay() {
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Yes");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("No");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					wizard.getFinishButton().setEnabled(true);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
		
		public void alertMessageForCopay() {
			
			String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + "%" + "</br> Do you wish to Proceed.</b>";
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Yes");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("No");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox(message, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					wizard.getFinishButton().setEnabled(true);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			
		}
		  private void releaseHumanTask(){
				
				Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
		     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
		 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
		 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

		 		if(existingTaskNumber != null){
		 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
		 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		 		}
		 		
		 		if(wrkFlowKey != null){
		 			DBCalculationService dbService = new DBCalculationService();
		 			dbService.callUnlockProcedure(wrkFlowKey);
		 			getSession().setAttribute(SHAConstants.WK_KEY, null);
		 		}
			}

		@Override
		public void alertForAlreadyAcquired(String aquiredUser) {
			/*Label successLabel = new Label(
					"<b style = 'color: green;'> Claim is already opened by "+aquiredUser +"</b>",
					ContentMode.HTML);
			
			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			String btnCaption ="";
			if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
			}
			else {
				btnCaption = "Home";
			}
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Claim is already opened by "+aquiredUser +"</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
				//dialog.close();
				SHAUtils.setClearPreauthDTO(bean);
				if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION,null);
				}
				else {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
				}
				billingReviewPageViewImplObj.setClearReferenceData();
				billingProcessPageViewImplObj.setClearReferenceData();
				billingHospitalizationPageViewImplObj.setClearReferenceData();
				}
			});
		
			
		}
		
		@Override
		 public void confirmMessageForApprovedAmount(String message){
				
//				ConfirmDialog dialog = ConfirmDialog
//						.show(getUI(),
//								message + " amount is zero",
//								"Do you want to continue ? ",
//								"No", "Yes", new ConfirmDialog.Listener() {
//
//									public void onClose(ConfirmDialog dialog) {
//										
//										if (!dialog.isConfirmed()) {
//											billingHospitalizationPageViewImplObj.generateLayoutBasedOnApprovedAmount();
//										} else {
//											wizard.getFinishButton().setEnabled(true);
//										}
//									}
//								});
//				dialog.setClosable(false);
//				dialog.setWidth("500px");
//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				
				//String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() +"%" +"</br> Do you wish to Proceed.</b>";
				
				//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
				/*Label successLabel = new Label("<b style = 'color: blue;'>"+message+" amount is zero </br> Do you want to continue ? ", ContentMode.HTML);
				
//				Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
				
				Button homeButton = new Button("Yes");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				Button cancelButton = new Button("No");
				cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
				
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
				horizontalLayout.setMargin(true);
				horizontalLayout.setSpacing(true);
				
				VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
				layout.setSpacing(true);
				layout.setMargin(true);
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox(message+" amount is zero </br> Do you want to continue ? ", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
						.toString());
				Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						billingHospitalizationPageViewImplObj.generateLayoutBasedOnApprovedAmount();
						
						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
						
					}
				});
				
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						//wizard.getFinishButton().setEnabled(true);
						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
						
					}
				});
				
			}
		
		 public void getErrorMessage(String eMsg){
				
				/*Label label = new Label(eMsg, ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			}
		 
		 public ShortcutListener setWizardBackShortcut(){
			 int[] modifierArr = new int[]{ModifierKey.CTRL,ModifierKey.ALT};
			 ShortcutListener listener = new ShortcutListener("wizardBack", KeyCode.B, modifierArr) {
				 private static final long serialVersionUID = 1L;
				 @Override
				 public void handleAction(Object sender, Object target) {
					 if(wizard.getBackButton().isEnabled()){
						 wizard.back();
					 }
				 }
			 };

			 return listener;
		 }

		 public ShortcutListener setWizardNextShortcut(){
			 int[] modifierArr = new int[]{ModifierKey.CTRL,ModifierKey.ALT};
			 ShortcutListener listener = new ShortcutListener("wizardNext", KeyCode.N, modifierArr) {
				 private static final long serialVersionUID = 1L;
				 @Override
				 public void handleAction(Object sender, Object target) {
					 if(wizard.getNextButton().isEnabled()){
						 wizard.next();
					 }
				 }
			 };
			 return listener;
		 }

		 public ShortcutListener setPolicyScheduleShortcut(){
			 int[] modifierArr = new int[]{ModifierKey.CTRL,ModifierKey.ALT};
			 ShortcutListener listener = new ShortcutListener("policySchedule", KeyCode.NUM1, modifierArr) {
				 private static final long serialVersionUID = 1L;
				 @Override
				 public void handleAction(Object sender, Object target) {
					 policyScheduleAction();
				 }
			 };
			 return listener;
		 }

		 private void policyScheduleAction(){
			 viewDetails.getViewPolicySchedule(bean.getNewIntimationDTO().getIntimationId());
			 bean.setIsScheduleClicked(true);
			 btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		 }
		 	 
		 private boolean isInsured(){
			 if(bean !=null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				 return true;
			 }
			 return false;
		 }
		 
		 @Override
		 public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
			 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
		 }

		 @Override
		 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
			 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
		 }
		
		 private void clearObject() {
				
			if(rewardRecognitionRequestViewObj != null){
					rewardRecognitionRequestViewObj.invalidate();
				}
			}
		 public void addVaadinSessionAttribute(Long policyKey){
			 VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,policyKey);					
			 VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
			 VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);

			 final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
			 opener.popupBlockerWorkaround(true);
			 opener.withShortcut(callPreviousClaimsDetailsViewSListener());
			 opener.setFeatures("height=700,width=1300,resizable");
			 opener.doExtend(viewLinkPolicyDtls);
			 setSopener(opener);

			 viewLinkPolicyDtls.addClickListener(new Button.ClickListener() {
				 @Override
				 public void buttonClick(ClickEvent event) {
					 getSopener().open();	
				 }
			 });

		 }
		 
		 @SuppressWarnings("serial")
			public ShortcutListener callPreviousClaimsDetailsViewSListener(){
				ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM2, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
					@Override
					public void handleAction(Object sender, Object target) {
						viewBasePolicyDetailsSelect.setValue(VIEW_BASE_POLICY_CLAIMS);
						if(viewBasePolicyDetailsSelect.getValue() != null){
							viewBasePolicyDetailsSelect.select(viewBasePolicyDetailsSelect.getValue());
							//btnGo.click();
						}
					}
				};
				getActionManager().addAction(shortcutListener);
				return shortcutListener;
			}
		 
		 public EnhancedBrowserWindowOpener getSopener() {
				return sopener;
			}

		 public void setSopener(EnhancedBrowserWindowOpener sopener) {
			 this.sopener = sopener;
		 }
		 @SuppressWarnings("serial")
		 public ShortcutListener callSListener(){
			 ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM3, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
				 @Override
				 public void handleAction(Object sender, Object target) {
					 viewLinkPolicyDtls.click();
				 }
			 };
			 getActionManager().addAction(shortcutListener);
			 return shortcutListener;
		 }
		 
		 public Boolean isAlertMessageNeededForNEFTDetails(){
			 List<UploadDocumentDTO> uploadDocsList = bean.getUploadDocumentDTO();

			 if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
				 for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
					 if (null != uploadDocumentDTO.getFileType()
							 && null != uploadDocumentDTO.getFileType().getValue()
							 && uploadDocumentDTO.getFileType().getValue()
							 .contains(SHAConstants.NEFT_DETAILS)) {
						 bean.setIsNEFTDetailsAvailableinDMS(true);

					 }
				 }
			 }
			 
			 List<DocumentDetails> documentDetails = createRodService.getDocumentDetailsByIntimationNumber(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.NEFT_DETAILS);
				if(documentDetails !=null){
					System.out.println("NEFT Details Available in DMS");
					bean.setIsNEFTDetailsAvailableinDMS(true);
					
				}
			
			//If nominee/Legal details Neft details mandatory should not call - As per Mr.Raja on 05-MAR-2021	
			if(this.bean.getDocumentReceivedFromId() != null 
					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& (bean.getPreauthDataExtractionDetails().getPatientStatus() != null
							&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
							&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))){
				return false;
			}
			
			 if(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag()!=null && this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)&& bean.getIsNEFTDetailsAvailableinDMS()){
				 return true;

			 }
			 else{

				 return false;
			 }
		 }

		 public void autoAllocationCancelRemarksLayout(){

			 autoAllocCancelRemarks = new TextArea("Cancel Remarks");
			 autoAllocCancelRemarks.setMaxLength(4000);
			 autoAllocCancelRemarks.setWidth("350px");
			 autoAllocCancelRemarks.setHeight("150px");
			 autoAllocCancelRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
			 SHAUtils.handleTextAreaPopupDetails(autoAllocCancelRemarks,null,getUI(),null);
			 Button cancel = new Button("OK");
			 cancel.addStyleName(ValoTheme.BUTTON_DANGER);
			 cancel.setWidth("-1px");
			 cancel.setHeight("-10px");
			 cancel.setEnabled(true);
			 VerticalLayout vLayout = new VerticalLayout();
			 vLayout.addComponents(autoAllocCancelRemarks,cancel);
			 vLayout.setMargin(true);
			 vLayout.setSpacing(true);
			 vLayout.setComponentAlignment(autoAllocCancelRemarks, Alignment.MIDDLE_CENTER);
			 vLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
			 final Window popup = new com.vaadin.ui.Window();
			 popup.setWidth("35%");
			 popup.setHeight("30%");
			 popup.setContent(vLayout);
			 popup.setClosable(true);
			 popup.center();
			 popup.setResizable(true);
			 popup.addCloseListener(new Window.CloseListener() {
				 private static final long serialVersionUID = 1L;
				 @Override
				 public void windowClose(CloseEvent e) {
					 System.out.println("Close listener called");
				 }
			 });

			 cancel.addClickListener(new ClickListener() {
				 private static final long serialVersionUID = -5934419771562851393L;
				 @Override
				 public void buttonClick(ClickEvent event) {
					 if(null != autoAllocCancelRemarks && null != autoAllocCancelRemarks.getValue() && !("").equalsIgnoreCase(autoAllocCancelRemarks.getValue())){
						 releaseHumanTask();
						 bean.setAutoAllocCancelRemarks(autoAllocCancelRemarks.getValue());
						 fireViewEvent(BillingWizardPresenter.BILLING_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS, bean);
						 popup.close();
						 SHAUtils.setClearPreauthDTO(bean);
						 fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION,null);
						 clearObject();
						 billingReviewPageViewImplObj.setClearReferenceData();
						 billingProcessPageViewImplObj.setClearReferenceData();
						 billingHospitalizationPageViewImplObj.setClearReferenceData();
						 VaadinRequest currentRequest = VaadinService.getCurrentRequest();
						 SHAUtils.clearSessionObject(currentRequest);
					 }else
					 {
						 SHAUtils.showAlertMessageBox("Please enter the Cancel Remarks");
					 }

				 }
			 });
			 popup.setModal(true);
			 UI.getCurrent().addWindow(popup);
		 }
		 

}
