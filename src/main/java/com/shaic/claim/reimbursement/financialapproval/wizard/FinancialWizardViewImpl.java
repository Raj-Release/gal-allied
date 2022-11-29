package com.shaic.claim.reimbursement.financialapproval.wizard;

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
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.BasePolicyPreviousClaimWindowUI;
import com.shaic.claim.registration.ViewBasePolicyClaimsWindowOpen;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPageViewImpl;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPageViewImpl;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPageViewImpl;
import com.shaic.claim.reimbursement.financialapproval.pages.billsummary.FinancialSummaryPageViewImpl;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationViewImpl;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
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
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillAssessmentSheet;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewMedicalSummaryPage;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Sizeable;
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
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

public class FinancialWizardViewImpl extends AbstractMVPView implements FinancialWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	private IWizardPartialComplete wizard;
	
//	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;	
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	private TextArea remarksTxt;
	
	private VerticalLayout remarskvertical;
	
	private HorizontalLayout remarskHlayout;
	
	private Panel insuredPedDetailsPanel;
		
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<FinancialReviewPageViewImpl> billingReviewPageViewImpl;
	
	@Inject
	private Instance<FinancialSummaryPageViewImpl> billingSummaryPageViewImpl;
	
	@Inject
	private Instance<FinancialProcessPageViewImpl> billingProcessPageViewImpl;
	
	@Inject
	private Instance<FinancialHospitalizationPageViewImpl> billingHospitalizationViewImpl;
	
	@Inject		
	private ViewBusinessProfileChart viewBusinessProfileChart;	
	
	@Inject
	private Instance<FinancialDecisionCommunicationViewImpl> financialDecisionCommunicationViewImpl;
	
	private FinancialDecisionCommunicationViewImpl financialDecisionCommunicationObj;
	
	private FinancialReviewPageViewImpl billingReviewPageViewImplObj;
	
	private FinancialSummaryPageViewImpl billingSummaryPageViewImplObj;
	
	private FinancialProcessPageViewImpl billingProcessPageViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private ViewMedicalSummaryPage viewMedicalSummaryPage;
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private ViewBillAssessmentSheet viewBillAssessmentSheet;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private ViewPEDRequestWindow viewPEDRequestObj;
	
	private FinancialHospitalizationPageViewImpl billingHospitalizationPageViewImplObj;
	
    private TextArea txtBillingRemarks;
    
    private TextArea txtAreaBillingInternalRemarks;
	
	private TextArea txtMedicalRemarks;
	
	private FormLayout forms;

	
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
	
	public Button btnViewRTABalanceSI;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private CreateRODService createRodService;
	
	 @Inject
		private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
		 
	@EJB
		private PolicyService policyService;
		 
	 @Inject
		private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	private final Logger log = LoggerFactory.getLogger(FinancialWizardViewImpl.class);
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	private Button btnViewPolicySchedule;
	
	private Button btnBusinessProfileChart;
	
	private Button viewBillAssessmentBtn;
	@Inject
	private Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance;
	
	private EsclateClaimToRawPage esclateClaimToRawPageViewObj;
	
	@Inject
	private Toolbar toolBar;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;

	@Inject
	private CoInsuranceDetailView coInsuranceDetailsView;
	private TextArea txtAreaPCCRemarks;
	private TextArea autoAllocCancelRemarks;
	
	private Button viewLinkPolicyDtls;
	
	private NativeSelect viewBasePolicyDetailsSelect;
	private Button btnGo;
	
	private EnhancedBrowserWindowOpener sopener;
	
	private static final String VIEW_BASE_POLICY_CLAIMS = "Policy Claims Details";

	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	@Inject
	private Instance<ViewBasePolicyClaimsWindowOpen> ViewBasePolicyClaimsWindowOpen;
	
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
//		this.wizard = new IWizardPartialComplete();
		initBinder();
		billingReviewPageViewImplObj = billingReviewPageViewImpl.get();
		billingReviewPageViewImplObj.init(this.bean , wizard);
		wizard.addStep(billingReviewPageViewImplObj,"Bill Review");
		
		billingSummaryPageViewImplObj = billingSummaryPageViewImpl.get();
		billingSummaryPageViewImplObj.init(this.bean);
		wizard.addStep(billingSummaryPageViewImplObj,"View Bill Summary");
		
		billingProcessPageViewImplObj = billingProcessPageViewImpl.get();
		billingProcessPageViewImplObj.init(this.bean);
		wizard.addStep(billingProcessPageViewImplObj,"Billing Process");
		
		billingHospitalizationPageViewImplObj = billingHospitalizationViewImpl.get();
		billingHospitalizationPageViewImplObj.init(this.bean,this.wizard);
		wizard.addStep(billingHospitalizationPageViewImplObj,"Biling Hospitalization");
		
		financialDecisionCommunicationObj = financialDecisionCommunicationViewImpl.get();
		financialDecisionCommunicationObj.init(this.bean, this.wizard, billingReviewPageViewImplObj);
		wizard.addStep(financialDecisionCommunicationObj, "Confirmation");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Financials");
		intimationDetailsCarousel.init(this.bean,  this.bean.getScreenName());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PREAUTH_MEDICAL,"Process Claim Financials");
		remarksTxt = new TextArea("Doctor Remarks");
		remarksTxt.setHeight("70%");
		remarksTxt.setValue(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setDescription(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setReadOnly(true);
//			insuredPedDetailsPanel = getInsuredPedDetailsPanel();
		HorizontalLayout hLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		hLayout.setWidth("100%");
		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout);
		
//		wizardLayout1.setHeight("250px");
		
	    forms = new FormLayout();
	    
	    
	    txtBillingRemarks = new TextArea("Billing Remarks");
	    txtBillingRemarks.setEnabled(false);
	    
	    txtMedicalRemarks = new  TextArea("Medical Remarks");
	    txtMedicalRemarks.setEnabled(false);
	    
	    FormLayout medicalFormLayout = new FormLayout();
	    FormLayout billingFormLayout = new FormLayout();
//	    
	    if(this.bean.getIsReBilling()){
	    	txtBillingRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingRemarks());
	    	txtBillingRemarks.setNullRepresentation("-");
	    	medicalFormLayout.addComponent(txtBillingRemarks);
//	    	medicalFormLayout.setCaption("Billing");
	    	//medicalFormLayout.setHeight("150px");
	    }else {
	    	
	    	txtBillingRemarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks());
	    	txtBillingRemarks.setNullRepresentation("");
	    	medicalFormLayout.addComponent(txtBillingRemarks);
//	    	medicalFormLayout.setCaption("Billing");
	    }
	    	
	    if(this.bean.getIsReMedical()){
	    	txtMedicalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getMedicalRemarks());
	    	billingFormLayout.addComponent(txtMedicalRemarks);
//    	    billingFormLayout.setCaption("Medical");
    	//   billingFormLayout.setHeight("800px");
	    	
	    }
	    
	    // Billing Internal Remarks
		txtAreaBillingInternalRemarks = (TextArea) binder.buildAndBind("Billing Internal Remarks", "billingInternalRemarks", TextArea.class);
		txtAreaBillingInternalRemarks.setMaxLength(4000);
		txtAreaBillingInternalRemarks.setNullRepresentation("");
		txtAreaBillingInternalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		if(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks() != null) {
			txtAreaBillingInternalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks());
		}
		txtAreaBillingInternalRemarks.setReadOnly(true);
		billingInternalRemarksChangeListener(txtAreaBillingInternalRemarks, null);
	    
	    FormLayout billingInternalRemarksFormLayout = new FormLayout();
	    billingInternalRemarksFormLayout.addComponent(txtAreaBillingInternalRemarks);
	    
	    // PCC Remarks
	    txtAreaPCCRemarks = new TextArea("PCC Remarks");
	    txtAreaPCCRemarks.setMaxLength(4000);
	    txtAreaPCCRemarks.setNullRepresentation("");
	    txtAreaPCCRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
	    if(this.bean.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue() != null) {
	    	txtAreaPCCRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue());
	    }
	    txtAreaPCCRemarks.setReadOnly(true);
	    escalatePCCRemarksChangeListener(txtAreaPCCRemarks, null);

	    FormLayout pccRemarksFormLayout = new FormLayout(txtAreaPCCRemarks);
	    
	    HorizontalLayout pccRemarksHLayout = new HorizontalLayout(pccRemarksFormLayout);
	    pccRemarksHLayout.setWidth("100%");
	    pccRemarksHLayout.setSpacing(true);
	    
	    forms.addComponent(pccRemarksHLayout);
	    
	    HorizontalLayout remarksHorizontal = new HorizontalLayout(medicalFormLayout,billingFormLayout, billingInternalRemarksFormLayout);
	    remarksHorizontal.setWidth("100%");
	  //  remarksHorizontal.setHeight("900px");
	  // remarksHorizontal.setHeight("700px");
//	    
//	    remarksHorizontal.setSpacing(true);
//	    
	    forms.addComponent(remarksHorizontal);
	    
//		TextField field = new TextField("Billing Remarks");
//		field.setValue(this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks() != null ? this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks() : "");
		wizardLayout1.addComponent(forms);
		
		wizardLayout1.setSpacing(true);
		//wizardLayout1.setHeight("10px");
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("500px");
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
//				compareWithUserId(aciquireByUserId);
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
	
	private VerticalLayout commonButtonsLayout()
	{
		TextField typeFld = new TextField("Type");
//		typeFld.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
	
		if(this.bean.getIsCashlessType() && this.bean.getHospitalizaionFlag()&& (this.bean.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)||this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS))){
			typeFld.setValue(SHAConstants.DIRECT_TO_FINANCIAL);
		}
		typeFld.setReadOnly(true);
		
		TextField hospitalNtwrktype = new TextField("Hospital Network Type");
		hospitalNtwrktype.setNullRepresentation("-");
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
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		if (null != memberType && !memberType.isEmpty()) {
			hLayout.addComponent(cmdClubMembership);
		}
		
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
		
		hLayout.addComponent(cmbReconsiderationRequest);
		
		//cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration = new ComboBox("Reason for Reconsideration");
		
		List<SelectValue> reconsiderValues = new ArrayList<SelectValue>();
		SelectValue selValue = new SelectValue();
		if(null  != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		{
			String reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
			selValue.setId(1l);
			selValue.setValue(reasonForReconsideration);
		}
		reconsiderValues.add(selValue);
		
		
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderBeanContainer.addAll(reconsiderValues);
		
//		reconsiderBeanContainer = masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION);	
		
		cmbReasonForReconsideration.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setReadOnly(false);

		String reasonForReconsideration = "";
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
		
		 if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest())){
			 hLayout.addComponent(cmbReasonForReconsideration);
		 }
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
				showPopup(new VerticalLayout(earlierRodDetailsViewObj));
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
		
		Button billingWorksheetBtn = new Button("Billing Worksheet");
		
		billingWorksheetBtn = new Button("Billing Worksheet");
		billingWorksheetBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7439398898591124006L;

			@Override
			public void buttonClick(ClickEvent event) {
				

				Window popup = new com.vaadin.ui.Window();
				
				uploadDocumentViewImpl.initPresenter(SHAConstants.BILLING_WORKSHEET);
				uploadDocumentViewImpl.init(bean,popup);
				popup.setCaption("Billing Worksheet");
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setClosable(true);
				popup.setContent(uploadDocumentViewImpl);
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
				
			/**
			 * Commenting it as per sathish sir advice. In Billing worksheet pdf shouldn't 
			 * be showed, rather the file upload component should be shown.
			 * */
				
			//	fireViewEvent(FinancialReviewPagePresenter.VIEW_BILLING_WORKSHEET, bean);
				
			}
		});
		
		/**
		 * view bill summary button is commanded as per issue ticket no : 2820
		 */
		
//		Button viewBillSummaryButton = new Button("View Bill Summary");
//		viewBillSummaryButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				
//				
//				viewBillSummaryPage.init(bean,bean.getKey(),true);
//				Panel mainPanel = new Panel(viewBillSummaryPage);
//		        mainPanel.setWidth("2000px");
//		        popup = new com.vaadin.ui.Window();
//				popup.setCaption("");
//			//	popup.setWidth("75%");
//				//popup.setHeight("85%");
//				popup.setSizeFull();
//				popup.setContent(mainPanel);
//				popup.setClosable(true);
//				popup.center();
//				popup.setResizable(false);
//				popup.addCloseListener(new Window.CloseListener() {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void windowClose(CloseEvent e) {
//						System.out.println("Close listener called");
//					}
//				});
//
//				popup.setModal(true);
//				UI.getCurrent().addWindow(popup);
//			}
//			
//		});
		
		viewBillAssessmentBtn = new Button("Billing Assessment Sheet");
		final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BillAssessmentUI.class);
		final ShortcutListener sListener = callSListener();
		if(bean.getBillingDate() != null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.FA_BILL_ASSESSMENT_VIEW_PAGE,viewBillAssessmentSheet);
			
			opener.popupBlockerWorkaround(true);
			opener.withShortcut(sListener);
			opener.setFeatures("height=900,width=1300,resizable");
		    opener.doExtend(viewBillAssessmentBtn);
		}
		
		viewBillAssessmentBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getBillingDate() != null){
					opener.open();
				} else {
					getErrorMessage("Billing Assessment Sheet is not applicable");
				}
			}
		});
		
		Button viewIRDABillSummaryButton = new Button("View IRDA Bill Summary");
		viewIRDABillSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
//				viewBillSummaryPage.init(bean.getKey());
//				showPopup(new VerticalLayout(viewBillSummaryPage));
			}
			
		});
		
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
				rewardRecognitionRequestViewObj.initPresenter(SHAConstants.FINANCIAL);
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
		
		Button pedButton = new Button("Initiate PED Endorsement");
		
		if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()){
			pedButton.setEnabled(false);
		}
		else{
			pedButton.setEnabled(true);
		}
		
		pedButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					viewPEDRequestObj = viewPedRequest.get();
					viewPEDRequestObj.initView(bean, bean.getKey(), bean.getIntimationKey(), bean.getPolicyKey(), bean.getClaimKey(),ReferenceTable.FINANCIAL_STAGE,false);
					viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
					showPopup(new VerticalLayout(viewPEDRequestObj));
				}
				
//				showPopup(new VerticalLayout(viewPEDRequestObj));
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
		//btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
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
		
		HorizontalLayout formLayout = SHAUtils.newImageCRM(bean);
		
		Label activityAgeing = new Label();
		Label claimAgeing = new Label();
		if(bean.getDocumentReceivedFromId().equals(SHAConstants.DOC_RECIEVED_FROM_INSURED_ID))
		{
			Long wkKey = 0l;
			if(this.bean.getScreenName().equals("Common for Billing & FA") || this.bean.getScreenName().equals("Common for Billing & FA(Auto Allocation)"))
			{
				wkKey=dbService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.BILLING_CURRENT_KEY);
			}
			else{
				wkKey=dbService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.FA_CURRENT_QUEUE);
			}
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
		
		HorizontalLayout coInsurance = SHAUtils.coInsuranceIcon(bean,coInsuranceDetailsView);
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");	
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
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
		HorizontalLayout dummy = new HorizontalLayout();
		VerticalLayout spacinLayout = new VerticalLayout(dummy,icrAgentBranch); 
		HorizontalLayout icrHS = new HorizontalLayout(spacinLayout,hopitalFlag);
		VerticalLayout vlayout=new VerticalLayout(formLayout);
		
		Button btnEsclateToRAW = new Button("Escalate To Raw");
		btnEsclateToRAW.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SHAUtils.buildEsclateToRawView(esclateClaimToRawPageInstance,esclateClaimToRawPageViewObj,bean,SHAConstants.FINANCIAL);
			}
		});
		HorizontalLayout viewEarlierRODLayout = new HorizontalLayout(vlayout,coInsurance,activityAgeing,claimAgeing,btnRRC,btnLumen,pedButton,elearnBtn,btnEsclateToRAW);
	
		VerticalLayout icrRodLAyout = new VerticalLayout(viewEarlierRODLayout,icrHS);
		btnEsclateToRAW.setEnabled(Boolean.FALSE);
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		
		icrRodLAyout.setSpacing(true);
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
		final ShortcutListener sListener1 = callSListener();
		final EnhancedBrowserWindowOpener linkOpener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,linkPolicyKey.getKey());					
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
		linkOpener.popupBlockerWorkaround(true);

		linkOpener.withShortcut(sListener1);
		linkOpener.setFeatures("height=700,width=1300,resizable");
		linkOpener.doExtend(viewLinkPolicyDtls);
		//					setSopener(opener);
		viewLinkPolicyDtls.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				linkOpener.open();
			}
		});
        }
		HorizontalLayout viewEarlierRODLayout1 = null;
		if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				|| /*(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			btnViewRTABalanceSI.setEnabled(this.bean.getPreauthDataExtractionDetails().getIsRTAButtonEnable());
			viewEarlierRODLayout1 = new HorizontalLayout(btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewIRDABillSummaryButton,viewBillAssessmentBtn,viewLinkPolicyDtls,viewMedicalSummaryButton,billingWorksheetBtn, viewEarlierRODDetails,btnZUAAlert,btnViewRTABalanceSI);
		}
		else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
			if(bean.getPolicyDto().getGmcPolicyType() != null &&
					(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				viewEarlierRODLayout1 = new HorizontalLayout(btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewIRDABillSummaryButton,viewBillAssessmentBtn,viewMedicalSummaryButton,billingWorksheetBtn, viewEarlierRODDetails,btnZUAAlert,viewLinkedPolicy,icrLayout);
			} else {
				viewEarlierRODLayout1 = new HorizontalLayout(btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewIRDABillSummaryButton,viewBillAssessmentBtn,viewMedicalSummaryButton,billingWorksheetBtn, viewEarlierRODDetails,btnZUAAlert,icrLayout);
			}
		}		
		else{
			viewEarlierRODLayout1 = new HorizontalLayout(btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,viewIRDABillSummaryButton,viewBillAssessmentBtn,viewLinkPolicyDtls,viewMedicalSummaryButton,billingWorksheetBtn, viewEarlierRODDetails,btnZUAAlert);
		}
		viewEarlierRODLayout1.setSpacing(true);
		
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(/*hLayout*/icrRodLAyout);
		
		/*HorizontalLayout lumenHorizontalLayout = new HorizontalLayout(btnLumen);
		lumenHorizontalLayout.setSpacing(true);*/
		
		HorizontalLayout hosHor = new HorizontalLayout(hLayout,preferredNetworkType/*,lumenHorizontalLayout*/);
		hosHor.setSpacing(true);
		
//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

			if(bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital() != null 
					&& bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital().equalsIgnoreCase("Y")){
				preferredNetworkType.setVisible(true);
			}
			
		}
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		VerticalLayout verticalLayout = new VerticalLayout();
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest().equalsIgnoreCase("Y")){

			if(((this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) || (this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty())) && remarskvertical != null) {
				
				hosHor.addComponents(remarksTxt,getInsuredPedDetailsPanel(),crmFlaggedComponents/*insuredPedDetailsPanel*/);
			}
			else{
				hosHor.addComponents(remarksTxt,crmFlaggedComponents);
			}
			
			horizontalLayout.addComponent(hosHor);
			horizontalLayout.addComponent(commonValues());
			horizontalLayout.setSpacing(true);
			verticalLayout.addComponents(componentsHLayout,viewEarlierRODLayout1,horizontalLayout);
			verticalLayout.setSpacing(true);
		}
		else{
			remarskHlayout = new HorizontalLayout();
			remarskHlayout.setSpacing(true);
			if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty() && remarskHlayout != null) {
				remarskHlayout.addComponents(hLayout,remarksTxt,getInsuredPedDetailsPanel(),crmFlaggedComponents);
				
			}	
			verticalLayout.addComponents(componentsHLayout,viewEarlierRODLayout1,hosHor,remarskHlayout);
			verticalLayout.setSpacing(true);
		}
	
		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest())){
			verticalLayout.addComponent(commonValues());
		}
		

		/*HorizontalLayout lumenHorizontalLayout = new HorizontalLayout(btnLumen);
		lumenHorizontalLayout.setSpacing(true);*/
				
/*		verticalLayout = new VerticalLayout(componentsHLayout,viewEarlierRODLayout1,lumenHorizontalLayout,horizontalLayout);
		verticalLayout.setSpacing(true);*/

		return verticalLayout;
	}
	
	private Table getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
		table.setWidth("332px");
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
		table.setColumnWidth("pedCode", 80);
		table.setColumnWidth("pedDescription", 250);
		table.setColumnWidth("pedEffectiveFromDate", 200);
		
//		Panel tablePanel = new Panel(table);
//		return tablePanel;
		
		return table;
		
	}
	
	public Boolean alertMessageForPEDInitiate(String message) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox( message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				//Long preauthKey=bean.getKey();
				//Long intimationKey=bean.getIntimationKey();
				//Long policyKey=bean.getPolicyKey();
				//Long claimKey=bean.getClaimKey();
				viewPEDRequestObj = viewPedRequest.get();
				viewPEDRequestObj.initView(bean, bean.getKey(), bean.getIntimationKey(), bean.getPolicyKey(), bean.getClaimKey(),ReferenceTable.FINANCIAL_STAGE,false);
				viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
				showPopup(new VerticalLayout(viewPEDRequestObj));
			}
		});
		return true;
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
	
private VerticalLayout commonValues() {
		
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
		
		
		
//		FormLayout fLayout = new FormLayout(cmbReconsiderationRequest);
//		fLayout.setSpacing(true);
//		fLayout.setMargin(false);
////		FormLayout fLayout1 = new FormLayout(cmbReasonForReconsideration);
//		
//		HorizontalLayout vLayout = new HorizontalLayout(fLayout);
//		vLayout.setMargin(true);
////		vLayout.setCaption("Earlier Request Reconsidered");
//		
//		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
//		{
//			fLayout.addComponent(cmbReasonForReconsideration);
//			
//			vLayout.addComponent(reconsiderRequestDetails);
//			
////			reconsiderationLayout = new VerticalLayout(vLayout);
//			
////			HorizontalLayout hlayout = new HorizontalLayout();
////			
////			if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty() && remarskvertical != null) {
////				hlayout.addComponents(remarskvertical, vLayout);
////				reconsiderationLayout = new VerticalLayout(fLayout, hlayout);
////			}else{
////				hlayout.addComponents(vLayout,remarksTxt);
////				reconsiderationLayout = new VerticalLayout(fLayout, hlayout);
////			}			
//		}
//		else
//		{
////			reconsiderationLayout = new VerticalLayout(fLayout);
//		}
		reconsiderationLayout = new VerticalLayout(reconsiderRequestDetails);
		reconsiderationLayout.setWidth("100%");
		return reconsiderationLayout;
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
				&&(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
						|| bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS)
						|| bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER))){

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
		try{
			/*if(this.bean.getUpdatePaymentDtlsFlag()!=null && this.bean.getUpdatePaymentDtlsFlag().equalsIgnoreCase("H")){
				warningMessageForPaymentHold();
			}*/
			if(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey())){
				fireViewEvent(FinancialWizardPresenter.BILLING_FA_HOLD_SUBMIT, this.bean);
			}
			else {
			Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(bean.getStageKey(), bean.getStatusKey(), this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue():0l, 
					bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey(), bean.getKey(), "C",getUI());
			if(! alertMessageForProvisionValidation){
				
				if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
				{
					if(this.cmbReasonForReconsideration.getValue() != null){
						MastersValue reconsiderReasonMaster = new MastersValue();
						SelectValue reasonSelectValue = (SelectValue)this.cmbReasonForReconsideration.getValue();
						reconsiderReasonMaster.setKey(reasonSelectValue.getId());
						reconsiderReasonMaster.setValue(reasonSelectValue.getValue());			
						this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().setReconsiderationReasonId(reconsiderReasonMaster);
					}
				}
				/*if(isAlertMessageNeededForNEFTDetails()){
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox("Select mode of payment as Bank Transfer only", buttonsNamewithType);

				}else*/ if(this.bean.getIsHospitalDiscountApplicable() && 
						this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() == 0){
					confirmMessageForHospitalDiscount();
				}else{
					
					if(this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
						if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
							warningMessageForCopay();
						}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
							alertMessageForCopay();
						}else{
							fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, this.bean);
						}
					}else{
						fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, this.bean);
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
									fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,
											null);
									billingReviewPageViewImplObj.setClrearReferenceData();
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
					
					Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
					Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
					
					yesButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							// Confirmed to continue
							if(bean.getScreenName() != null
									&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())
									|| bean.getScreenName() != null
											&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
								autoAllocationCancelRemarksLayout();
							}
							else {
							releaseHumanTask();
							SHAUtils.setClearPreauthDTO(bean);
							if(bean.getScreenName() != null
									&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
								fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
										null);
							}
							else {
								fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,
										null);
							}
							clearObject();
							billingReviewPageViewImplObj.setClrearReferenceData();
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

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout() {
		/*Label successLabel = new Label(
				"<b style = 'color: green;'> Claim record saved successfully !!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Financial Home");
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
				&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
			btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION;
		}
		else if(bean.getScreenName() != null
				&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
			btnCaption = SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION;
		}
	 	else if(bean.getScreenName() != null
				&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
			btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA;
		}
		else {
			btnCaption = "Financial Home";
		}
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Claim record saved successfully !!!", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

			//	dialog.close();
				toolBar.countTool();
				SHAUtils.setClearPreauthDTO(bean);
				if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
							null);
				}
				else if(bean.getScreenName() != null
						&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
							null);
				}
				else if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
							null);
				}
				else {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS, null);
				}
				clearObject();
				billingReviewPageViewImplObj.setClrearReferenceData();
				billingProcessPageViewImplObj.setClearReferenceData();
				billingHospitalizationPageViewImplObj.setClearReferenceData();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);

			}
		});
		
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
	private void validateUserForRRCRequestIntiation()
	{
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
		fireViewEvent(FinancialWizardPresenter.VALIDATE_FINANCIAL_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.FINANCIAL);
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
			fireViewEvent(FinancialWizardPresenter.VALIDATE_FINANCIAL_USER_LUMEN_REQUEST, bean);
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
		dialog.show(getUI().getCurrent(), null, true);
		*/
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
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Financials");
		
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
			
			
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox.createAlertBox("Intimation is already opened by another user"+userId, buttonsNamewithType);
			Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,null);
					}	
					//dialog.close();
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
										if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
											if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
												warningMessageForCopay();
											}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
												alertMessageForCopay();
											}else{
												fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
											}
										}else{
											fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
										}
									} else {
										wizard.getFinishButton().setEnabled(true);
									}
								}
							});
			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Hospital  is eligible for Discount !!!</br>Do you want to proceed without Discount ? ", buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					// Confirmed to continue
					if(bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
						if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0)){
							warningMessageForCopay();
						}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0)){
							alertMessageForCopay();
						}else{
							fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
						}
					}else{
						fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
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
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Financial Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);*/
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);*/
			
			/*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
		 
		 	String btnCaption = "";
		 	if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION;
			}
		 	else if(bean.getScreenName() != null
					&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION;
			}
		 	else if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
		 		btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA;
		 	}
		 	else {
		 		btnCaption = "Financial Home";
		 	}
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,
								null);
					}	
					clearObject();
					billingReviewPageViewImplObj.setClrearReferenceData();
					billingProcessPageViewImplObj.setClearReferenceData();
					billingHospitalizationPageViewImplObj.setClearReferenceData();
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
	 
	 @Override
		public void buildAlreadyExist() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Another ROD exists/settled  for same classification.!!!</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Financial Home");
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
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION;
			}
		 	else if(bean.getScreenName() != null
					&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION;
			}
		 	else if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
		 		btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA;
		 	}
		 	else {
		 		btnCaption = "Financial Home";
		 	}
		 
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Another ROD exists/settled  for same classification.!!!", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,
								null);
					}	
					clearObject();
					billingReviewPageViewImplObj.setClrearReferenceData();
					billingProcessPageViewImplObj.setClearReferenceData();
					billingHospitalizationPageViewImplObj.setClearReferenceData();
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
					.createAlertBox("Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
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
			
			String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() +"%" +"</br> Do you wish to Proceed.</b>";
			
			/*//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
			
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
					.createInformationBox("<b style = 'color: blue;'>"+message, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
					
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
		     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
		 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
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
		public void buildPaymentFailureLayout() {
			// TODO Auto-generated method stub
			
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Payment initiated/made for this ROD Already. Please contact IMS Support.", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Financial Home");
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
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION;
			}else if(bean.getScreenName() != null
					&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
				btnCaption = SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION;
			}
			else if(bean.getScreenName() != null
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
		 		btnCaption = SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA;
		 	}
		 	else {
		 		btnCaption = "Financial Home";
		 	}
		 	
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), btnCaption);
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Payment initiated/made for this ROD Already. Please contact IMS Support.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS,
								null);
					}	
					clearObject();
					billingReviewPageViewImplObj.setClrearReferenceData();
					billingProcessPageViewImplObj.setClearReferenceData();
					billingHospitalizationPageViewImplObj.setClearReferenceData();
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
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
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("Claim is already opened by "+aquiredUser +"</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					SHAUtils.setClearPreauthDTO(bean);
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS, null);
					}	
					clearObject();
					billingReviewPageViewImplObj.setClrearReferenceData();
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
					.createInformationBox(message+" amount is zero </br> Do you want to continue ? ", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					
					if(bean.getIsPending() && bean.getDocumentReceivedFromId() != null && bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						fireViewEvent(FinancialWizardPresenter.FINANCIAL_64VB_CHQUEQ_STATUS, null);
					}else{
						billingHospitalizationPageViewImplObj.generateLayoutBasedOnApprovedAmount();
					}
					
					
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

		@Override
		public void confirmMessageFor64VBstatus(String message) {
			
			/*Label successLabel = new Label("<b style = 'color: black;'> 64 VB status is still Pending. Do you wish to Proceed ?", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Ok");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("Cancel");
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
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("64 VB status is still Pending. Do you wish to Proceed ?", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
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
		
		@Override
		public void validationForLimit() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Amount exceeds your limit and you cannot proceed further.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Home");
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
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Amount exceeds your limit and you cannot proceed further.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					releaseHumanTask();
					if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,
								null);
					}
					else if(bean.getScreenName() != null
							&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
								null);
					}
					else {
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS, null);
					}	
					clearObject();
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}

		@Override
		public void confirmMessageForDefinedLimt(final Object rejectionCategoryDropdownValues) {		
			
			/*ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Is the Defined Limit is te reason for Rejection ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										billingHospitalizationPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
										
									} else {
										billingHospitalizationPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Is the Defined Limit is the reason for Rejection ?", buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					billingHospitalizationPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
					
					}
				});
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					billingHospitalizationPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
				
				}
				});
			
		}
		
	
		@Override
		public void confirmMessageForDefinedLimtForFirst(final Object rejectionCategoryDropdownValues) {		
			
			/*ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Is the Defined Limit is te reason for Rejection ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										billingReviewPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
										
									} else {
										billingReviewPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Is the Defined Limit is te reason for Rejection ?", buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					billingReviewPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
					
					}
				});
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					billingReviewPageViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
				
				}
				});
			
		}

		@Override
		public void validateClaimedAmount(TextField txtField) {
			// TODO Auto-generated method stub
			
		}
		
		public  void billingInternalRemarksChangeListener(TextArea textArea, final  Listener listener) {
		    @SuppressWarnings("unused")
			ShortcutListener enterShortCut = new ShortcutListener("ShortcutForBillingInternalRemarks", ShortcutAction.KeyCode.F8, null) {
		    	private static final long serialVersionUID = -2267576464623389044L;
		    	@Override
		    	public void handleAction(Object sender, Object target) {
		    		((ShortcutListener) listener).handleAction(sender, target);
		    	}
		    };	  
		    handleShortcut(textArea, getBillingInternalRemarksShortCutListener(textArea));
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
		
		private ShortcutListener getBillingInternalRemarksShortCutListener(final TextArea textAreaField) {
			ShortcutListener listener =  new ShortcutListener("ShortcutForBillingInternalRemarks", KeyCodes.KEY_F8,null) {
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
					txtArea.setMaxLength(4000);
					txtArea.setData(bean);
					txtArea.setValue(textAreaField.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					txtArea.setReadOnly(true);
					
					final Window dialog = new Window();
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					
					txtArea.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;
						@Override
						public void valueChange(ValueChangeEvent event) {
							textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
//							PreauthDTO mainDto = (PreauthDTO) textAreaField.getData();
//							mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(textAreaField.getValue());
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					dialog.setCaption("Billing Internal Remarks");
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

		@SuppressWarnings("serial")
		public ShortcutListener callSListener(){
		ShortcutListener shortcutListener = new ShortcutListener("billAssessment", KeyCode.NUM3, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
			    @Override
			    public void handleAction(Object sender, Object target) {
			    	if(bean.getBillingDate() != null){
			    		viewBillAssessmentBtn.click();
					} else {
						getErrorMessage("Billing Assessment Sheet is not applicable");
					}
			    }
			};
			getActionManager().addAction(shortcutListener);
			return shortcutListener;
		}
		
		// PCC Remarks
		public  void escalatePCCRemarksChangeListener(TextArea textArea, final  Listener listener) {
			@SuppressWarnings("unused")
			ShortcutListener enterShortCut = new ShortcutListener("Escalate PCC Remarks", ShortcutAction.KeyCode.F8, null) {
				private static final long serialVersionUID = -2267576464623389044L;
				@Override
				public void handleAction(Object sender, Object target) {
					((ShortcutListener) listener).handleAction(sender, target);
				}
			};	  
			handleShortcut(textArea, getescalatePCCRemarksShortCutListener(textArea));
		}

		private ShortcutListener getescalatePCCRemarksShortCutListener(final TextArea textAreaField) {
			ShortcutListener listener =  new ShortcutListener("Escalate PCC Remarks", KeyCodes.KEY_F8,null) {
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
					txtArea.setMaxLength(4000);
					txtArea.setData(bean);
					txtArea.setValue(textAreaField.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					txtArea.setReadOnly(true);

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

					dialog.setCaption("Escalate PCC Remarks");
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
							fireViewEvent(FinancialWizardPresenter.BILLING_FA_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS, bean);
							popup.close();
							SHAUtils.setClearPreauthDTO(bean);
							if(bean.getScreenName() != null
									&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,null);
							}
							else if(bean.getScreenName() != null
									&& SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
								fireViewEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION,null);
							}
							clearObject();
							billingReviewPageViewImplObj.setClrearReferenceData();
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
		 
		 
}
