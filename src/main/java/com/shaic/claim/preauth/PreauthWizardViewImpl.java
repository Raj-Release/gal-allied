
package com.shaic.claim.preauth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.GWizardListener;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.ViewSeriousDeficiencyUI;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACService;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthDataExtractionPage;
import com.shaic.claim.preauth.wizard.pages.PreauthDecisionCommunicationPage;
import com.shaic.claim.preauth.wizard.pages.PreauthMedicalDecisionPage;
import com.shaic.claim.preauth.wizard.pages.PreauthMedicalProcessingPage;
import com.shaic.claim.preauth.wizard.pages.PreauthPreviousClaimDetailsPage;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.scoring.HospitalScoringView;
import com.shaic.claim.viewEarlierRodDetails.EsclateClaimToRawPage;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
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
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PreauthWizardViewImpl extends AbstractMVPView implements
		PreauthWizard, GWizardListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013903728510416209L;

	@Inject
	private Instance<PreauthDataExtractionPage> preauthDataExtractionPageInstance;

	@Inject
	private Instance<PreauthMedicalDecisionPage> PreauthMedicalDecisionPageInstance;

	@Inject
	private Instance<PreauthDecisionCommunicationPage> preauthDecisionCommunicationPageInstance;

	@Inject
	private Instance<PreauthPreviousClaimDetailsPage> preauthPreviousClaimDetailsPageInstance;

	@Inject
	private Instance<PreauthMedicalProcessingPage> preauthMedicalProcessingPageInstance;

	//@Inject
	//private Instance<ProcessPreAuthButtonLayout> processPreauthButtonLayoutInstance;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private HospitalService hospitalService;

	//@Inject
	//private MasterService masterService;

	private PreauthDataExtractionPage preauthDataExtractionPage;

	private PreauthMedicalDecisionPage preauthMedicalDecisionPage;

	private PreauthPreviousClaimDetailsPage preauthPreviousClaimDetailsPage;

	//private PreauthDecisionCommunicationPage preauthDecisionCommunicationPage;

	//private ProcessPreAuthButtonLayout processPreAuthButtonLayout;

	private VerticalSplitPanel mainPanel;

//	private GWizard wizard;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;

	private PreauthDTO bean;

	private PreauthMedicalProcessingPage preauthMedicalProcessingPage;
	
	@Inject		
	private ViewSeriousDeficiencyUI revisedViewSeriousDeficiencyTable;	
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	public Button btnViewRTABalanceSI;
	
	 @Inject
		private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
		 
	@EJB
		private PolicyService policyService;
		 
	 @Inject
		private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	//private final Logger log = LoggerFactory.getLogger(PreauthWizardViewImpl.class);
	
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;
	
	@Inject
	private Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance;
	
	private EsclateClaimToRawPage esclateClaimToRawPageViewObj;
	
	@Inject
	private HospitalScoringView hospitalScoringView;
	
	private Button btnHospitalScroing;
	private Button seriousDeficiency;
	private TextArea autoAllocCancelRemarks;
	
	@Inject
	private Toolbar toolBar;

	private TextArea txtSTPRemarks;
	
	//CR2019017 - Start
	@Inject	
	private Instance<ProcessPreAuthButtonLayout> processPreauthButtonLayout;
	private ProcessPreAuthButtonLayout processPreauthButtonObj;
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;
	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	//CR2019017 - End
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;

	@EJB
	private ProcessICACService processICACService;

	private void initBinder() {

		wizard.getFinishButton().setCaption("Submit");
		wizard.getCancelButton().setEnabled(true);
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");
		item.addNestedProperty("preauthMedicalDecisionDetails");

		item.addNestedProperty("preauthDataExtractionDetails.referenceNo");
		item.addNestedProperty("preauthDataExtractionDetails.reasonForAdmission");
		item.addNestedProperty("preauthDataExtractionDetails.admissionDate");
		item.addNestedProperty("preauthDataExtractionDetails.noOfDays");
		item.addNestedProperty("preauthDataExtractionDetails.natureOfTreatment");
		item.addNestedProperty("preauthDataExtractionDetails.diagnosis");
		item.addNestedProperty("preauthDataExtractionDetails.firstConsultantDate");
		item.addNestedProperty("preauthDataExtractionDetails.enhancementType");
		item.addNestedProperty("preauthDataExtractionDetails.subLimit");
		item.addNestedProperty("preauthDataExtractionDetails.corpBuffer");
		item.addNestedProperty("preauthDataExtractionDetails.criticalIllness");
		item.addNestedProperty("preauthDataExtractionDetails.specifyIllness");
		item.addNestedProperty("preauthDataExtractionDetails.roomCategory");
		item.addNestedProperty("preauthDataExtractionDetails.ventilatorSupport");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentType");
		item.addNestedProperty("preauthDataExtractionDetails.preauthReqAmt");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentRemarks");
		item.addNestedProperty("preauthDataExtractionDetails.sublimitSpecify");
		item.addNestedProperty("preauthDataExtractionDetails.sumInsured");
		item.addNestedProperty("preauthDataExtractionDetails.sublimitSumInsured");
		item.addNestedProperty("preauthDataExtractionDetails.enhancementReqAmt");
		item.addNestedProperty("preauthDataExtractionDetails.preauthApprAmt");

		item.addNestedProperty("preauthPreviousClaimsDetails.relapseOfIllness");
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseRemarks");
		item.addNestedProperty("preauthPreviousClaimsDetails.attachToPreviousClaim");

		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReportReviewed");
		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReviewRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.investigatorName");
		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequest");
		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTaken");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistType");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistConsulted");
		item.addNestedProperty("preauthMedicalDecisionDetails.remarksBySpecialist");
		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
		item.addNestedProperty("preauthMedicalDecisionDetails.fvrTriggerPoints");
		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTakenFlag");
		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequestFlag");

		item.addNestedProperty("preauthMedicalDecisionDetails.approvedAmount");
		item.addNestedProperty("preauthMedicalDecisionDetails.selectedCopay");
		item.addNestedProperty("preauthMedicalDecisionDetails.initialTotalApprovedAmt");
		item.addNestedProperty("preauthMedicalDecisionDetails.approvalRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.queryRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionCategory");
		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForDenial");
		item.addNestedProperty("preauthMedicalDecisionDetails.denialRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.escalateTo");
		// item.addNestedProperty("preauthMedicalDecisionDetails.upLoadFile");
		item.addNestedProperty("preauthMedicalDecisionDetails.escalationRemarks");
		item.addNestedProperty("preauthMedicalDecisionDetails.typeOfCoordinatorRequest");
		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForRefering");

		item.addNestedProperty("preauthMedicalDecisionDetails.sentToCPU");
		item.addNestedProperty("preauthMedicalDecisionDetails.remarksForCPU");

		item.addNestedProperty("coordinatorDetails.refertoCoordinator");
		item.addNestedProperty("coordinatorDetails.typeofCoordinatorRequest");
		item.addNestedProperty("coordinatorDetails.reasonForRefering");
		
		item.addNestedProperty("preauthDataExtractionDetails.hospitalisationDueTo.id");
		item.addNestedProperty("preauthDataExtractionDetails.hospitalisationDueTo.value");
		item.addNestedProperty("preauthDataExtractionDetails.causeOfInjury.id");
		item.addNestedProperty("preauthDataExtractionDetails.injuryDate");
		item.addNestedProperty("preauthDataExtractionDetails.medicalLegalCaseFlag");
		item.addNestedProperty("preauthDataExtractionDetails.diseaseFirstDetectedDate");
		item.addNestedProperty("preauthDataExtractionDetails.deliveryDate");
		item.addNestedProperty("preauthDataExtractionDetails.section.id");
		item.addNestedProperty("preauthDataExtractionDetails.typeOfDelivery.id");
		item.addNestedProperty("preauthDataExtractionDetails.firNumber");
		item.addNestedProperty("preauthDataExtractionDetails.policeReportAttachedFlag");

		this.binder.setItemDataSource(item);
	}

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();
	}

	public void initView(PreauthDTO preauthDTO, Boolean processId) {
		this.bean = preauthDTO;
//		this.wizard = new IWizard();
		this.wizard = new IWizardPartialComplete();
//		Component header = this.wizard.getHeader();
//		header.addListener(new Listener() {
//			
//			@Override
//			public void componentEvent(Event event) {
//				Notification.show("kdjlkfjdklfjkld");
//			}
//		});
//		this.wizard.getFinishButton().setDisableOnClick(true);
		initBinder();
		mainPanel = new VerticalSplitPanel();
		// mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		PreauthDataExtractionPage preauthDataExtractionPage = preauthDataExtractionPageInstance
				.get();
		preauthDataExtractionPage.init(this.bean, this.wizard);
		this.preauthDataExtractionPage = preauthDataExtractionPage;
		wizard.addStep(preauthDataExtractionPage, "Data Extraction");

		PreauthPreviousClaimDetailsPage preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPageInstance
				.get();
		preauthPreviousClaimDetailsPage.init(this.bean);
		this.preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPage;
		wizard.addStep(preauthPreviousClaimDetailsPage,
				"Previous Claim Details");
		/*Below condition removed as per CR2019007 copay editable for GMC product
		if(! ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
		
		if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsTataPolicy() ||
				(bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getFlpByPassFlag().equals(1l))) {
			PreauthMedicalProcessingPage preauthMedicalProcessingPage = preauthMedicalProcessingPageInstance
					.get();
			preauthMedicalProcessingPage.init(this.bean,this.wizard);
			this.preauthMedicalProcessingPage = preauthMedicalProcessingPage;
			wizard.addStep(preauthMedicalProcessingPage, "Medical Processing");
		}else if(!bean.getNewIntimationDTO().getIsPaayasPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()) {
			PreauthMedicalProcessingPage preauthMedicalProcessingPage = preauthMedicalProcessingPageInstance
					.get();
			preauthMedicalProcessingPage.init(this.bean,this.wizard);
			this.preauthMedicalProcessingPage = preauthMedicalProcessingPage;
			wizard.addStep(preauthMedicalProcessingPage, "Medical Processing");
		}
//		}

		PreauthMedicalDecisionPage preauthMedicalDecisionPage = PreauthMedicalDecisionPageInstance
				.get();
		preauthMedicalDecisionPage.init(this.bean, this.wizard);
		this.preauthMedicalDecisionPage = preauthMedicalDecisionPage;
		wizard.addStep(preauthMedicalDecisionPage, "Medical Decision");

		PreauthDecisionCommunicationPage preauthDecisionCommunicationPage = preauthDecisionCommunicationPageInstance.get();
		preauthDecisionCommunicationPage.init(this.bean, this.wizard);
		//this.preauthDecisionCommunicationPage = preauthDecisionCommunicationPage;
		wizard.addStep(preauthDecisionCommunicationPage,
				"Decision Communication");

		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);

		// Get Button Instace
		//ProcessPreAuthButtonLayout processPreAuthButtonLayout2 = processPreauthButtonLayoutInstance.get();
		//this.processPreAuthButtonLayout = processPreAuthButtonLayout2;

		//VerticalLayout wizardLayout = new VerticalLayout(wizard);

		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
//		intimationDetailsCarousel.init(preauthDTO.getNewIntimationDTO(),
//				preauthDTO.getClaimDTO(), "Process Pre-authorization");
		intimationDetailsCarousel.init(preauthDTO, "Process Pre-authorization");
		mainPanel.setFirstComponent(intimationDetailsCarousel);

		TextField netWorkHospitalType = new TextField("Network Hosp Type");
		netWorkHospitalType.setValue(bean.getNewIntimationDTO()
				.getHospitalDto().getRegistedHospitals()
				.getNetworkHospitalType());
		;
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("200px");
		netWorkHospitalType.setHeight("20px");
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		netWorkHospitalType.setReadOnly(true);
		netWorkHospitalType.setEnabled(false);
		netWorkHospitalType.setNullRepresentation("");
		
		TextField preferredNetworkType = new TextField();
		preferredNetworkType.setValue("Preferred Network Hospital");
		preferredNetworkType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		preferredNetworkType.setReadOnly(true);
		preferredNetworkType.setStyleName("preferred");
		preferredNetworkType.setVisible(false);
		// Added for Type box .
		/*
		 * ComboBox cmbTypeBox = new ComboBox("Type");
		 * cmbTypeBox.setContainerDataSource(masterService
		 * .getType(ReferenceTable.PROCESS_PREAUTH));
		 * cmbTypeBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbTypeBox.setItemCaptionPropertyId("value");
		 */
		/*FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);*/
		// ComboBox viewDetailsSelect = new ComboBox();
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
				ViewLevels.PREAUTH_MEDICAL, true,"Process Pre-Auth");
		viewDetails.setPreAuthKey(bean.getKey());
		viewDetails.setStageKey(ReferenceTable.PREAUTH_STAGE);
		// viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
		// ViewLevels.INTIMATION);
//		viewDetailsForm.addComponent(viewDetails);
		
		TextField typeFld = new TextField("Type");
		typeFld.setValue(null != this.bean
				.getStatusKey() ? SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()) : "");
		typeFld.setReadOnly(true);

		FormLayout hLayout = new FormLayout(netWorkHospitalType, typeFld);
		hLayout.setMargin(false);
		hLayout.setComponentAlignment(netWorkHospitalType,
				Alignment.MIDDLE_LEFT);
		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);
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
		
		HorizontalLayout commonButtons = new HorizontalLayout(commonButtons(),viewDetails);
		commonButtons.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButtons.setWidth("100%");
//		commonButtons.setHeight("70px");
		
		
		HorizontalLayout hosHor = new HorizontalLayout(hLayout,preferredNetworkType);
		hosHor.setSpacing(true);
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital() != null 
					&& bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital().equalsIgnoreCase("Y")){
				preferredNetworkType.setVisible(true);
			}
			
		}
		
		if((this.bean.getInsuredPedDetails() != null && ! this.bean.getInsuredPedDetails().isEmpty()) || (this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty())){
			hosHor.addComponent(getInsuredPedDetailsPanel());
		}
		
//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());	
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		hosHor.addComponent(crmFlaggedComponents);
		
//		mainHor.setComponentAlignment(commonButtons, Alignment.TOP_RIGHT);
//		mainHor.setComponentAlignment(hLayout, Alignment.TOP_LEFT);
//		mainHor.setWidth("100%");
//		mainHor.setHeight("100px");		
		
		HorizontalLayout show64VBlayout = show64VBlayout();
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			hosHor.addComponent(icrLayout);
			hosHor.setComponentAlignment(icrLayout, Alignment.TOP_RIGHT);
		}
		
		
		VerticalLayout mainHor = new VerticalLayout(commonButtons,hosHor);
		mainHor.setSpacing(false);
		HorizontalLayout premedicalAnd64layout = null;
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()){
			
			HorizontalLayout showPremedicalStatusHLayout = showPremedicalStatus();
		
			premedicalAnd64layout = new HorizontalLayout(showPremedicalStatusHLayout, show64VBlayout);
		}
		else
		{
			premedicalAnd64layout = new HorizontalLayout(show64VBlayout);
		}
		premedicalAnd64layout.setSpacing(true);
		VerticalLayout wizardLayout1 = new VerticalLayout(mainHor,premedicalAnd64layout);

		if(bean.getIsAutoAllocationCPUUser() && (bean.getIsAboveLimitCorpAdvise() || bean.getIsAboveLimitCorpProcess())){
//As per Urgent request from BA
			//showPremedicalStatusHLayout.addComponent(showDoctorOpinion());
		}
		
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("190px");
//		panel1.setHeight("270px");

		// mainPanel.addComponent(panel1);
		// VerticalLayout wizardLayout1 = new VerticalLayout(commonButtons(),
		// showPremedicalStatusHLayout , wizard);
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		// mainPanel.addComponent(wizardLayout1);
		mainPanel.setSecondComponent(wizardLayout2);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		if(bean.getIsPedPending()){
			getErrorMessage("Pending PED in Action");
		}

		fireViewEvent(PreauthWizardPresenter.SETUP_REFERENCE_DATA, bean);
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}
		
		}
		
//		if(bean.getTaskNumber() != null){
//			String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
//			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
//				compareWithUserId(aciquireByUserId);
//			}
//		}
		
		//CR2019017 - Start
		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		
		processPreauthButtonObj = processPreauthButtonLayout.get();
		processPreauthButtonObj.initView(this.bean, wizard, this.medicalDecisionTableObj);
		if(bean.getIsSDEnabled() && processPreauthButtonObj.getApproveBtn().isEnabled()){
			processPreauthButtonObj.getApproveBtn().setEnabled(false);
		}/*else{
			processPreauthButtonObj.getApproveBtn().setEnabled(true);
		}*/
		//CR2019179
		if(bean.getIsSDEnabled() && processPreauthButtonObj.getQueryBtn().isEnabled()){
			processPreauthButtonObj.getQueryBtn().setEnabled(false);
		}//CR2019179 End
		bean.setProcessPreauthButtonObj(processPreauthButtonObj);
		//CR2019017 - End
	}

	private HorizontalLayout show64VBlayout() {
		
		TextField paymentStatus = new TextField("Payment Status");
		
		//paymentStatus.setHeight("50px");
		paymentStatus.setValue(this.bean
				.getPreauthMedicalProcessingDetails().getVbPaymentStatus());
		paymentStatus.setNullRepresentation("");
		paymentStatus.setEnabled(false);
		
		paymentStatus.setReadOnly(Boolean.TRUE);
		TextField approvalStatus = new TextField("64 VB Approval Status");
		//approvalStatus.setHeight("50px");
		approvalStatus.setValue(this.bean
				.getPreauthMedicalProcessingDetails().getVbApprovalStatus());
		approvalStatus.setNullRepresentation("");
		approvalStatus.setEnabled(false);
		approvalStatus.setReadOnly(Boolean.TRUE);
		
		TextArea premedicalRemarks = new TextArea("64 VB Approval Remarks");
		//premedicalRemarks.setHeight("50px");
		premedicalRemarks.setValue(this.bean
				.getPreauthMedicalProcessingDetails().getVbApprovalRemark());
		premedicalRemarks.setNullRepresentation("");
		premedicalRemarks.setEnabled(false);
		premedicalRemarks.setReadOnly(Boolean.TRUE);
		
		FormLayout sugessionform =new FormLayout(paymentStatus, approvalStatus ,premedicalRemarks);
		HorizontalLayout horizontalLayout = new HorizontalLayout(sugessionform);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setHeight("40%");
		return horizontalLayout;
	}
	
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		fireViewEvent(PreauthWizardPresenter.PREAUTH_STEP_CHANGE_EVENT, event);

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {

	}

	
	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		if(bean.getStatusKey() != null && (bean.getStatusKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)
				|| bean.getStatusKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)
				|| bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
			
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
				fireViewEvent(PreauthWizardPresenter.PREAUTH_HOLD_SUBMIT, this.bean);
			}else{
				fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMIT_DECISION_MISMATCH_ALERT, this.bean);
				
				if(null != bean.getPreauthMedicalDecisionDetails().getIsDecisionMismatched() &&
						!(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthMedicalDecisionDetails().getIsDecisionMismatched())))
				{
					proceedForFinalSubmit();  // For R1136
				}
				else
				{
					buildSTPRemarksLayout();
				}
			}
//			fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);
		}
		catch(Exception e)
		{
			//log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void wizardCancelled(WizardCancelledEvent event) {/*
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									if(bean.getIsPreauthAutoAllocationQ()){
										autoAllocationCancelRemarksLayout();
									}else{
										
										SHAUtils.setClearPreauthDTO(bean);
										fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
												null);
										clearObject();
										fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
										VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					    				SHAUtils.clearSessionObject(currentRequest);
									}
									
								} else {
									// User did not confirm
								}
							}
						});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);

	*/
		final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Are you sure you want to cancel ?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button yesButton=getConf.getButton(ButtonType.YES);
			Button nolButton=getConf.getButton(ButtonType.NO);
			
			yesButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					fireViewEvent(PreauthWizardPresenter.PREAUTH_DYNAMICFRMLAYOUT_REMOVE_COMP, null);
					if(!bean.getIsPreauthAutoAllocationQ()){
						releaseHumanTask();
					}
					if(bean.getIsPreauthAutoAllocationQ()){
						autoAllocationCancelRemarksLayout();
					}else{						
						SHAUtils.setClearPreauthDTO(bean);
						fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
								null);
						clearObject();
						fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
						VaadinRequest currentRequest = VaadinService.getCurrentRequest();
	    				SHAUtils.clearSessionObject(currentRequest);
					}
					
				}
			});
			
			nolButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getConf.close();
				}
			});

		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWizardPageReferenceData(Map<String, Object> referenceData) {
		WizardStep currentStep = this.wizard.getCurrentStep();
		if (currentStep != null) {
			currentStep.setupReferences(referenceData);
		}

	}

	@Override
	public void generateFieldsBasedOnTreatment() {
		preauthDataExtractionPage.generatedFieldsBasedOnTreatment();
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generatePreauthApproveLayout() {
		preauthMedicalDecisionPage.generateButton(1, null);

	}

	@Override
	public void generateQueryLayout() {
		preauthMedicalDecisionPage.generateButton(2, null);

	}

	@Override
	public void generateRejectionLayout(Object rejectionCategoryDropdownValues) {
		preauthMedicalDecisionPage.generateButton(3,
				rejectionCategoryDropdownValues);
	}

	@Override
	public void generateDenialLayout(Object denialValues) {
		preauthMedicalDecisionPage.generateButton(4, denialValues);
	}
	
	@Override
	public void generateCashlessNotReqLayout() {
		preauthMedicalDecisionPage.generateButton(10, null);
	}
	

	@Override
	public void generateEscalateLayout(Object escalateValues) {
		preauthMedicalDecisionPage.generateButton(5, escalateValues);
	}

	@Override
	public void generateReferCoOrdinatorLayout() {
		preauthMedicalDecisionPage.generateButton(6, null);
	}
	
	
	private HorizontalLayout commonButtons() {
		
		TextField txtClaimCount = new TextField("Claim Count");
		txtClaimCount.setValue(this.bean.getClaimCount().toString());
		txtClaimCount.setReadOnly(true);
		TextField dummyField = new TextField();
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
		dummyField.setReadOnly(true);
//		firstForm.setWidth(txtClaimCount.getWidth(), txtClaimCount.getWidthUnits());
		Panel claimCount = new Panel(firstForm);
		claimCount.setWidth("130px");
		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.setHeight("30px");
		firstForm.setMargin(false);
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
//		txtClaimCount.addStyleName("fail");
//		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
	//	
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}
	
		
			HorizontalLayout formLayout = SHAUtils.newImageCRM(bean);
			HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
			HorizontalLayout hoapitalScore = SHAUtils.hospitalScore(bean,hospitalService);
			HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,hoapitalScore,hopitalFlag);
			HorizontalLayout icrAgentBranchLayout = SHAUtils.icrAgentBranch(bean);
//			HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,hopitalFlag);
			VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranchLayout);
			crmLayout.setSpacing(true);
			crmLayout.setWidth("100%");
		
		
		Button viewCoordinatorButton = new Button("View Co-ordinator Reply");
		viewCoordinatorButton.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getTranslationMiscRequest(bean
						.getNewIntimationDTO().getIntimationId());
			}
		});
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
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
		
		Button btnViewPackage = new Button("View Package");
		btnViewPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsPaayasPolicy()){
					pdfPageUI.init(bean.getNewIntimationDTO());
					if(pdfPageUI.isAttached()){
						pdfPageUI.detach();
					}
					UI.getCurrent().addWindow(pdfPageUI);
				}
				else if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
//				HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
				
				if(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					String url = bpmClientContext.getHospitalPackageDetails() + bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode();
					//getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				}
				
//					if(packageRatesDto != null){
//						
//					ReportDto reportDto = new ReportDto();
//					reportDto.setClaimId(bean.getNewIntimationDTO().getIntimationId());
//					List<HospitalPackageRatesDto> beanList = new ArrayList<HospitalPackageRatesDto>();
//					beanList.add(packageRatesDto);
//					reportDto.setBeanList(beanList);
//					DocumentGenerator docGen = new DocumentGenerator();
//					String fileUrl = docGen.generatePdfDocument("HospitalPackageRates", reportDto);
//					openPdfFileInWindow(fileUrl);
//					}
//					else{
//						getErrorMessage("Package Not Available for the selected Hospital");
//					}
				}
				else{
					getErrorMessage("Package Not Available for the selected Hospital");
				}
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
		
		Button btnViewDoctorRemarks = new Button("Doctor Remarks");
		btnViewDoctorRemarks.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getDoctorRemarks(bean.getNewIntimationDTO().getIntimationId());
			}
			
		});
		
		Button btnViewPolicySchedule = new Button("View Policy Schedule");
		
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
				viewDetails.getViewPolicySchedule(bean.getNewIntimationDTO().getIntimationId());
				bean.setIsScheduleClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
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
		
		btnHospitalScroing = new Button("Hospital Scoring");
		if(bean.getScoringClicked()){
			btnHospitalScroing.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		}else{
			btnHospitalScroing.setStyleName(ValoTheme.BUTTON_DANGER);
		}
		btnHospitalScroing.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showScoringView();				
			}
		});
		
		seriousDeficiency = new Button("Serious Deficiency");
		seriousDeficiency.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				revisedViewSeriousDeficiencyTable.init(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("Serious Deficiency Intimation List");
				popup.setWidth("35%");
				popup.setHeight("75%");
				popup.setContent(revisedViewSeriousDeficiencyTable);
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

		Button referToFLP = new Button("Refer to FLP");
		if(bean.getCreateDate() != null && (bean.getCreateDate().compareTo(SHAUtils.getFromDate(BPMClientContext.REFER_TO_FLP_APPLICABLE_DATE)) < 0)) {
			referToFLP.setEnabled(false);
		}
		else if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			referToFLP.setEnabled(false);
		}
		referToFLP.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		referToFLP.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				TextArea approvalRemarksTxta = new TextArea("Refer To FLP Remarks");
				approvalRemarksTxta.setMaxLength(4000);
				approvalRemarksTxta.setWidth("400px");
				approvalRemarksTxta.setHeight("200px");
				
				final ConfirmDialog dialog = new ConfirmDialog();
				Button submitButtonWithListener = getSubmitButtonWithListener(dialog, approvalRemarksTxta);
				
				HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
				btnLayout.setWidth("800px");
				btnLayout.setMargin(true);
				btnLayout.setSpacing(true);
				btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
				
				VerticalLayout VLayout = new VerticalLayout(new FormLayout(approvalRemarksTxta), btnLayout);
				VLayout.setWidth("800px");
				VLayout.setMargin(true);
				showInPopup(VLayout, dialog);
			}
			
		});
	
		Button elearnBtn = viewDetails.getElearnButton();
		
		
		
//		HorizontalLayout horizontalLayout = new HorizontalLayout(btnRRC,referToFLP,viewCoordinatorButton,btnViewPackage,elearnBtn);
//		horizontalLayout.setSpacing(true);
		
		HorizontalLayout horizontalLayout1 = null;
		/***
		 * commented below lines code due to adding new button policy schedule without risk,
		 * below ICR field move to 2nd row in button layout 
		 */
		/*TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);*/
		
		Button btnEsclateToRAW = new Button("Escalate To Raw");
		btnEsclateToRAW.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SHAUtils.buildEsclateToRawView(esclateClaimToRawPageInstance,esclateClaimToRawPageViewObj,bean,SHAConstants.PROCESS_PREAUTH);
			}
		});
		
		
		Button btnViewMBPackage = new Button("View MB Package");
		btnViewMBPackage.setVisible(false);
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy().equals(1)){
			btnViewMBPackage.setVisible(true);;
		}
		btnViewMBPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsPaayasPolicy()){
					pdfPageUI.init(bean.getNewIntimationDTO());
					if(pdfPageUI.isAttached()){
						pdfPageUI.detach();
					}
					UI.getCurrent().addWindow(pdfPageUI);
				}
				else if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
//				HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
				
				if(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					String url = bpmClientContext.getMediBuddyPackageDetails() + bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode();
					//getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				}
				
				}
				else{
					getErrorMessage("Package Not Available for the selected Hospital");
				}
			}
			
		});
		
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		
		Button viewNegotiationDtls = viewDetails.viewNegotiationDetails(bean.getNewIntimationDTO().getKey());
		
		//Parralel ICAC Process
		Button processICACBtn = new Button("Refer to ICAC");

		processICACBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				List<String> sourceList = new ArrayList<String>();
				sourceList.add(SHAConstants.PRE_AUTH);
				Boolean allowed  = processICACService.getTocheckIcacreqValidOrNot(bean.getNewIntimationDTO().getIntimationId(),sourceList);

						if(allowed){
							submitIcacProcess();
						}
						else{
							SHAUtils.showMessageBoxWithCaption(SHAConstants.ICAC_PROCESS_ALRDY_INIT,"Information");
						}
			}

		});
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| /*(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			
			preauthDataExtractionPage.setRTAButton(btnViewRTABalanceSI);
			horizontalLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,btnViewRTABalanceSI);

		}
		else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
			if(bean.getNewIntimationDTO().getPolicy().getGmcPolicyType() != null && 
					((bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)) || (bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT)))){
				horizontalLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,viewLinkedPolicy/*,icrLayout*/);

			} else {
				horizontalLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage/*,icrLayout*/);

			}
		}
		else
		{
			horizontalLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage);

		}
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(processICACBtn);
		horizontalLayout1.setSpacing(true);
		//HorizontalLayout crmLayout1 = new HorizontalLayout(crmLayout,btnEsclateToRAW);
		//btnEsclateToRAW.setEnabled(Boolean.FALSE);
		//crmLayout1.setSpacing(true);

        HorizontalLayout crmLayout1 = new HorizontalLayout(icrAGBR);
		VerticalLayout btnsLayout = new VerticalLayout(crmLayout1, horizontalLayout1,horizontalLayout2);
		horizontalLayout1.setSpacing(true);
		horizontalLayout1.setMargin(false);
		
		horizontalLayout2.setSpacing(true);
		horizontalLayout2.setMargin(false);

		btnsLayout.setSpacing(true);
		btnsLayout.setMargin(false);
		
		HorizontalLayout mainLayout = new HorizontalLayout(btnsLayout);

		return mainLayout;

	   }


	
	private Button getSubmitButtonWithListener(final ConfirmDialog dialog, final TextArea remarksfield) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				bean.setStatusKey(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS);
				bean.setStageKey(ReferenceTable.PREAUTH_STAGE);
				if(remarksfield != null && remarksfield.getValue() != null && remarksfield.getValue().length() > 0) {
					bean.getPreauthMedicalDecisionDetails().setReferToFLPremarks(remarksfield.getValue());
					bean.setIsReferTOFLP(true);
					bean.setDocFilePath(null);
					bean.setDocumentSource(null);
					bean.setPreauthHoldStatusKey(null);
					dialog.close();
					wizard.finish();
				} else {
					eMsg = "Please Enter Remarks";
					showErrorPopup(eMsg);
				}
			}
		});
		
		return submitButton;
	}
	private void showErrorPopup(String eMsg) {/*
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
	*/
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();
		
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
//				wizard.getNextButton().setEnabled(true);
//				wizard.getBackButton().setEnabled(true);
//				wizard.getFinishButton().setEnabled(false);
			}
		});
		return cancelButton;
	}
		private void showInPopup(Layout layout, ConfirmDialog dialog) {
			dialog.setCaption("");
			dialog.setClosable(true);
	
			Panel panel = new Panel();
			panel.setHeight("500px");
			panel.setWidth("850px");
			panel.setContent(layout);
			dialog.setContent(panel);
			dialog.setResizable(false);
			dialog.setModal(true);
	
			dialog.show(getUI().getCurrent(), null, true);
		
		}
	
	 /* private void openPdfFileInWindow(final String filepath) {
			
			Window window = new Window();
			// ((VerticalLayout) window.getContent()).setSizeFull();
			window.setResizable(true);
			window.setCaption("Hospital Package Rate");
			window.setWidth("800");
			window.setHeight("600");
			window.setModal(true);
			window.center();

			Path p = Paths.get(filepath);
			String fileName = p.getFileName().toString();

			StreamResource.StreamSource s = new StreamResource.StreamSource() {

				public FileInputStream getStream() {
					try {

						File f = new File(filepath);
						FileInputStream fis = new FileInputStream(f);
						return fis;

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			};

			StreamResource r = new StreamResource(s, fileName);
			Embedded e = new Embedded();
			e.setSizeFull();
			e.setType(Embedded.TYPE_BROWSER);
			r.setMIMEType("application/pdf");
			e.setSource(r);
			window.setContent(e);
			UI.getCurrent().addWindow(window);
		 }*/
	     public void getErrorMessage(String eMsg){/*

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
			
			
       */
	    	 MessageBox.createError()
		    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
		        .withOkButton(ButtonOption.caption("OK")).open();	 
	     }
	
	
	private void validateUserForRRCRequestIntiation()
	{
		if(preauthDataExtractionPage !=null){
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(preauthDataExtractionPage.getTotalRequestedAmt());
		}
		if(bean.getStatusKey() != null && (bean.getStatusKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)
				|| bean.getStatusKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS))) {
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(0L);
		}else if(preauthMedicalDecisionPage !=null){
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(preauthMedicalDecisionPage.getApprovalAmtForRRC());
		}
		fireViewEvent(PreauthWizardPresenter.VALIDATE_PREAUTH_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {

		if (!isValid) {/*
			Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		*/
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage("Same user cannot raise request more than once from same stage")
	        .withOkButton(ButtonOption.caption("OK")).open();
			
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_PREAUTH);
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
		listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);
		/*if(!listOfSettledStatus.contains(bean.getClaimDTO().getStatusId())){
			fireViewEvent(PreauthWizardPresenter.PROCESS_PREAUTH_LUMEN_REQUEST, bean);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}*/
		fireViewEvent(PreauthWizardPresenter.PROCESS_PREAUTH_LUMEN_REQUEST, bean);
	}
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {/*
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
	*/
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(eMsg)
        .withOkButton(ButtonOption.caption("OK")).open();	
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Process-Pre Auth");
		
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
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	

	@Override
	public void genertateFieldsBasedOnPatientStaus() {
		preauthDataExtractionPage.generateFieldsBasedOnPatientStatus();
	}

	@Override
	public void genertateFieldsBasedOnFieldVisit(Boolean isChecked,
			Object allocationToValues,Object assignToValues,Object priorityValues,List<ViewFVRDTO> fvrDTOList) {
		preauthMedicalDecisionPage.generateFieldsBasedOnFieldVisit(isChecked,
				allocationToValues,assignToValues,priorityValues,fvrDTOList);
	}
	
	@Override
	public void genertateFieldsBasedOnAssignFieldVisit(Boolean isChecked, String repName, String repContactNo) {
		preauthMedicalDecisionPage.generateFieldsForAssignFieldVisit(isChecked,repName, repContactNo);
	}
	
	@Override
	public void showFVRnotPosssible(){
		preauthMedicalDecisionPage.showErrorPopUp("<BR>FVR request is in process, cannot initiate another request.");
		
	}
	
	@Override
	public void showFVRPosssible(){
		preauthMedicalDecisionPage.showErrorPopUp("<BR>FVR is not initiated. Please Initiate FVR.");
	}

	@Override
	public void genertateFieldsBasedOnSpecialistChecked(Boolean isChecked,
			Map<String, Object> dropdownValues) {
		preauthMedicalDecisionPage.generateFieldsBasedOnSpecialist(isChecked,
				dropdownValues);

	}

	@Override
	public void genertateFieldsBasedOnRelapseOfIllness(
			Map<String, Object> referenceData) {
		preauthPreviousClaimDetailsPage
				.generateFieldsBasedOnRelapseOfIllness(referenceData);

	}

	@Override
	public void genertateFieldsBasedOnTreatment() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateFieldsOnQueryClick() {
		preauthMedicalProcessingPage.generateButtonFields("query");
	}

	@Override
	public void generateFieldsOnSuggestRejectionClick() {
		preauthMedicalProcessingPage.generateButtonFields("suggestion");

	}

	@Override
	public void generateFieldsOnSendForProcessingClick() {
		preauthMedicalProcessingPage.generateButtonFields("sendForProcessing");

	}

	@Override
	public void searchState(List<State> stateList) {
		// this.preauthDataExtractionPage.updateSateList(stateList);
	}

	@Override
	public void editSpecifyVisibility(Boolean checkValue) {
		this.preauthDataExtractionPage.editSpecifyVisible(checkValue);

	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		preauthDataExtractionPage.setIcdBlock(icdBlockContainer);
	}

	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		preauthDataExtractionPage.setIcdCode(icdCodeContainer);

	}

	@Override
	public void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		preauthPreviousClaimDetailsPage.setPreviousClaims(previousClaimDTOList);

	}

	@Override
	public void generateFieldsBasedOnSentTOCPU(Boolean isChecked) {
		preauthMedicalDecisionPage.generateFieldBasedOnSentToCPU(isChecked);
	}

	@Override
	public void viewClaimAmountDetails() {
		preauthMedicalDecisionPage.showClaimAmountDetails();
	}

	@Override
	public void setRelapsedClaims(Map<String, Object> referenceData) {
		preauthPreviousClaimDetailsPage.setRelapsedClaims(referenceData);
	}

	@Override
	public void setPackageRate(Map<String, String> mappedValues) {
		preauthDataExtractionPage.setPackageRateForProcedure(mappedValues);
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		preauthMedicalProcessingPage.setExclusionDetails(exclusionContainer);
	}

	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {/*
		Label successLabel = new Label(
				"<b style = 'color: green;'> Cashless Claim Record Saved Successfully.</b>",
				ContentMode.HTML);
		if(bean.getStatusKey() != null && ReferenceTable.REFER_TO_FLP_KEYS.containsKey(bean.getStatusKey())) {
			successLabel = new Label(
					"<b style = 'color: green;'> Intimation No:" + bean.getNewIntimationDTO().getIntimationId()+ "has referred to First level processor successfully</b>",
					ContentMode.HTML);
		}
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Pre-auth Home");
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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
				if(bean.getIsPreauthAutoAllocationQ()){
					
					SHAUtils.setClearPreauthDTO(bean);
					
					toolBar.countTool();
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}else{
					
					SHAUtils.setClearPreauthDTO(bean);
					toolBar.countTool();
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
							null);
					
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
	*/

		StringBuffer successLabel=new StringBuffer(" Cashless Claim Record Saved Successfully.");
		if(bean.getStatusKey() != null && ReferenceTable.REFER_TO_FLP_KEYS.containsKey(bean.getStatusKey())) {
			successLabel=new StringBuffer("Intimation No:" + bean.getNewIntimationDTO().getIntimationId()+ "has referred to First level processor successfully");
			
		}
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(successLabel.toString())
			    .withOkButton(ButtonOption.caption("Pre-auth Home"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				
				if(bean.getIsPreauthAutoAllocationQ()){
					
					SHAUtils.setClearPreauthDTO(bean);
					
					toolBar.countTool();
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}else{
					
					SHAUtils.setClearPreauthDTO(bean);
					toolBar.countTool();
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
							null);
					
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
		
	}

	@SuppressWarnings("unused")
	private HorizontalLayout showPremedicalStatus() {
		TextField premedicalSuggestion = new TextField("First Level Suggestion");
		TextArea premedicalRemarks = new TextArea("First Level Remarks");
		premedicalRemarks.setEnabled(false);
		if(this.bean.getStatusKey() !=null){
		if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
				|| this.bean.getStatusKey().equals(
						ReferenceTable.PREAUTH_QUERY_STATUS)
						|| this.bean.getStatusKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)) {
			premedicalRemarks = new TextArea("Remarks (First Level)");
			premedicalSuggestion.setValue("Preauth Query");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);

		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
			premedicalSuggestion.setValue("Suggestion Rejection");
			premedicalRemarks = new TextArea("First Level Remarks ");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)) {
			premedicalSuggestion.setValue("Send For Processing");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
			premedicalSuggestion.setValue("Escalation");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getEscalationRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		}  else if (this.bean.getStatusKey().equals(
				ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)|| this.bean.getStatusKey().equals(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS)) {
			premedicalSuggestion.setValue("Coordinator");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} 
	}
		premedicalSuggestion.setEnabled(false);
		TextField premedicalCategory = new TextField("Category");

		String category = "";
		if (this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
			category = this.bean
					.getPreauthMedicalProcessingDetails()
					.getCategory()
					.getId()
					.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY) ? "Non - Medical"
					: "Medical";
		}
		premedicalCategory.setValue(category);
		premedicalCategory.setEnabled(false);
		// TextArea premedicalRemarks = new
		// TextArea("Rejection Remarks (Pre-medical)");
		/**
		 * In pre auth screen , instead of Rejection Remarks, it should be
		 * Remarks as per sathish sir. Hence commenting the above and adding the
		 * below line code.
		 * */
		FormLayout sugessionform =new FormLayout(premedicalSuggestion, premedicalRemarks);
//		sugessionform.setMargin(false);
		FormLayout premedicalCategoryform =new FormLayout(premedicalCategory);
//		premedicalCategoryform.setMargin(false);
		HorizontalLayout premedicalHLayout = new HorizontalLayout(
				sugessionform,premedicalCategoryform);
		premedicalHLayout.setSpacing(true);
		premedicalHLayout.setHeight("40%");

		return premedicalHLayout;
	}

	@Override
	public void setInvestigationRule(Boolean checkInitiateInvestigation) {
		preauthMedicalDecisionPage
				.setInvestigationCheck(checkInitiateInvestigation);
	}

	@Override
	public void setBalanceSumInsured(Double balanceSI, List<Double> copayValue) {
		preauthMedicalDecisionPage.setBalanceSI(balanceSI, copayValue);
	}

	@Override
	public void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName) {
		preauthMedicalDecisionPage.setSumInsuredCaculationsForSublimit(
				diagnosisSumInsuredLimit, diagnosisName);

	}

	@Override
	public void setDiagnosisName(String diagnosis) {
		preauthMedicalProcessingPage.setDiagnosisName(diagnosis);

	}

	@Override
	public void setFVRPendingStatus(boolean pending) {
		preauthMedicalDecisionPage.setFVRNotRequiredValidation(pending);

	}

	@Override
	public void intiateCoordinatorRequest() {
		if (preauthDataExtractionPage.validatePage()) {
			preauthDataExtractionPage.setTableValuesToDTO();
			fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT,
					this.bean);
		}

	}

	@Override
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		preauthDataExtractionPage
				.setHospitalizationDetails(hospitalizationDetails);

	}

	@Override
	public void viewBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			preauthMedicalDecisionPage.showBalanceSumInsured(intimationId);
		}
	}

//	@Override
//	public void genertateFieldsBasedOnHospitalisionDueTo(
//			PreauthDTO bean) {
//		preauthDataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo(bean);
//		
//	}
	
	@Override
	public void genertateFieldsBasedOnHospitalisionDueTo() {
		preauthDataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo();
		
	}
	
	@Override
	public void genertateFieldsBasedOnObterBenefits(
			PreauthDTO bean){
		preauthDataExtractionPage.generatedFieldsBasedOnOtherBenefits(bean);
	}
	
	
	@Override
	public void genertateFieldsBasedOnReportedToPolice(Boolean selectedValue) {
		preauthDataExtractionPage.generatedFieldsBasedOnReportedToPolice(selectedValue);
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
	

	public void compareWithUserId(String userId) {/*
		 
		 if(userId == null){
			 userId = "";
		 }

		 final ConfirmDialog dialog = new ConfirmDialog();
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
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,null);
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
	
 */

		 
		 if(userId == null){
			 userId = "";
		 }
		final MessageBox showInfoMessageBox = showInfoMessageBox("Intimation is already opened by another user");
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
			
		homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,null);
					showInfoMessageBox.close();
				}
			});
			
	}
 
 @Override
	public void setBalanceSIforRechargedProcess(Double balanceSI) {

		preauthMedicalDecisionPage
				.setBalanceSIforRechargedProcessing(balanceSI);
	}

	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {

		preauthDataExtractionPage.setCoverList(coverContainer);

	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		preauthDataExtractionPage.setSubCoverList(subCoverContainer);

	}

	@Override
	public void setRemarks(MasterRemarks remarks,Boolean isReject) {
		preauthMedicalDecisionPage.setRemarks(remarks,isReject);
		
	}
	
	@Override
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		preauthMedicalDecisionPage.setRejSugCategContainer(rejSubcategContainer);
	}
	
	@Override
	public void setPreauthQryRemarks(String qryRemarks) {
		preauthMedicalDecisionPage.setPreauthQryRemarks(qryRemarks);
	}

	@Override
	public void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		this.preauthPreviousClaimDetailsPage.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
		
	}

	@Override
	public void generate64VBComplainceLayout() {
		preauthMedicalDecisionPage.generateButton(7, null);
		
	}

	@Override
	public void buildFailureLayout(String acquiredUser) {/*
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim is already opened by "+acquiredUser +"</b>",
				ContentMode.HTML);
		
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Pre-auth Home");
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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
				if(bean.getIsPreauthAutoAllocationQ()){
					
					SHAUtils.setClearPreauthDTO(bean);
					
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}else{
					
					SHAUtils.setClearPreauthDTO(bean);
					
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}

			}

			
		});
	*/
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(" Claim is already opened by "+acquiredUser)
			    .withOkButton(ButtonOption.caption("Pre-auth Home"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				
				if(bean.getIsPreauthAutoAllocationQ()){
					
					SHAUtils.setClearPreauthDTO(bean);
					
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}else{
					
					SHAUtils.setClearPreauthDTO(bean);
					
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}

			}

			
		});
		
	}

	@Override
	public void validationForLimitAmt(String acquiredUser) {/*
		Label successLabel = new Label(
				"<b style = 'color: red;'> Approval amount is beyond your limit. </b>",
				ContentMode.HTML);
		
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
				
			}

			
		});
	*/
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage("Approval amount is exceed for this user.")
			    .withOkButton(ButtonOption.caption("Pre-auth Home"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
			}

			
		});
		
	}
	
	
	@Override
	public void generateOtherBenefitsPopup(){
		preauthDataExtractionPage.viewBenefitTablePopup();	
	}
	
	@Override
	public void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList,Map<String, Object> referenceData) {
		
		preauthMedicalDecisionPage.setUpdateOtherClaimsDetails(updateOtherClaimDetailList, referenceData);
		
	}

	private void clearObject() {
		
		if(rewardRecognitionRequestViewObj != null){
			rewardRecognitionRequestViewObj.invalidate();
		}
		
		preauthDataExtractionPage.setClearReferenceData();
		if(preauthMedicalProcessingPage != null){
			preauthMedicalProcessingPage.setClearReferenceData();
		}
		preauthMedicalDecisionPage.setClearReferenceData();
		preauthDataExtractionPage.clearTableObj();
		preauthDataExtractionPage = null;
		preauthMedicalDecisionPage = null;
		preauthMedicalProcessingPage = null;
		preauthPreviousClaimDetailsPage = null;
	}
	
	@Override
	public void generatePTCACABGLayout(Boolean value) {
			preauthDataExtractionPage.generatedStentBasedOnSurgical();	
	}

	@Override
	public void checkPanCardDetails(Boolean isAvailable) {
		preauthMedicalDecisionPage.isPanAvailable(isAvailable);
		
	}
	
	@Override
	public void generateCPUUserLayout(Object cpuSuggestionDropdownValues) {
		preauthMedicalDecisionPage.generateButton(8, cpuSuggestionDropdownValues);
	}
	
	public FormLayout showDoctorOpinion(){
		
		TextField suggestion = new TextField("Claim Processing Suggestion");
		
		if(this.bean.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory() != null){
			suggestion.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory().getValue());
		}
		
		suggestion.setNullRepresentation("");
		suggestion.setEnabled(false);
		suggestion.setReadOnly(Boolean.TRUE);
		
		TextField amtSuggestion = new TextField("Amount suggested");
		amtSuggestion.setVisible(false);
		if(this.bean.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory() != null && this.bean.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory().getId().equals(ReferenceTable.CPU_ALLOCATION_APPROVE)){
			amtSuggestion.setVisible(true);
			if(this.bean.getPreauthMedicalDecisionDetails().getCpuAmountSuggested() != null){
				amtSuggestion.setValue(this.bean.getPreauthMedicalDecisionDetails().getCpuAmountSuggested().toString());
			}
		}else{
			amtSuggestion.setVisible(false);
		}
		
		amtSuggestion.setNullRepresentation("");
		amtSuggestion.setEnabled(false);
		amtSuggestion.setReadOnly(Boolean.TRUE);
		
		TextArea cpuRemarks = new TextArea("Opinion Remarks");
		
		if(this.bean.getPreauthMedicalDecisionDetails().getCpuRemarks() != null){
			cpuRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getCpuRemarks());
		}
		
		
		cpuRemarks.setNullRepresentation("");
		cpuRemarks.setEnabled(false);
		cpuRemarks.setReadOnly(Boolean.TRUE);
		FormLayout opinionform =new FormLayout(suggestion, amtSuggestion, cpuRemarks);
		opinionform.setCaption("Doctor's Opinion Details");
		return opinionform;
	}

	@Override
	public void setAssistedReprodTreatment(Long assistValue) {
		preauthDataExtractionPage.setAssistedValue(assistValue);		
	}
	
	private Table getInsuredPedDetailsPanel(){
		
		Table table = new Table();
		table.setWidth("100%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
					table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription()}, i+1);
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
		table.setColumnWidth("pedCode", 80);
		table.setColumnWidth("pedDescription", 320);
		table.setWidth("402px");
////		if(i>0){
//			Panel tablePanel = new Panel(table);
//			return tablePanel;
////		}
////		return null;
		
		return table;
	}

	@Override
	public void setProcedureValues(BeanItemContainer<SelectValue> prodCont) {
		preauthDataExtractionPage.setProcedureValues(prodCont);
	}
	

	public void showScoringView(){
		hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), true);
		hospitalScoringView.setScreenName("Pre-Auth");
		hospitalScoringView.setDtoBean(bean);
		hospitalScoringView.setParentScoringButton(btnHospitalScroing);
		VerticalLayout misLayout = new VerticalLayout(hospitalScoringView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		//To be Removed in version 4 - Start
		/*if(bean.getNewIntimationDTO().getClaimType().getId().intValue() == ReferenceTable.CLAIM_TYPE_CASHLESS_ID.intValue()){
			popup.setHeight("72%");
		}else{
			popup.setHeight("59%");
		}*/
		//To be Removed in version 4 - End
		popup.setHeight("58%");
		popup.setContent(misLayout);
		popup.setClosable(false);
		popup.center();
		popup.setResizable(true);
		hospitalScoringView.setPopupWindow(popup);
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
	@Override
	public void generateHoldLayout() {
		preauthMedicalDecisionPage.generateButton(9, null);
	}
	
	/**			  
	 * Part of CR R1136
	 */
	private void proceedForFinalSubmit(){
		showSublimitAlert();
	}

	/**			  
	 * Part of CR R1136
	 */
	public void showSublimitAlert() {/*
		boolean sublimitMapAvailable = false;
		List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		if(diagnosisList != null && !diagnosisList.isEmpty()){
				StringBuffer selectedSublimitNames = new StringBuffer("");
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
						sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable();
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
			Label successLabel = new Label("<b style = 'color: red;'> Sublimit selected is "+selectedSublimitNames.toString()+"</b>", ContentMode.HTML);
			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton ,horizontalLayout);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setWidth("250px");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.addCloseListener(new Window.CloseListener() {
				
				@Override
				public void windowClose(CloseEvent e) {
					wizard.getFinishButton().setEnabled(true);
					dialog.close();
					
				}
			});
			if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
			
				dialog.show(getUI().getCurrent(), null, true);
			}
			else if (!sublimitMapAvailable){
				fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);	
			}
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, bean);
				}
			});
	  }	
	
*/
		boolean sublimitMapAvailable = false;
		List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		if(diagnosisList != null && !diagnosisList.isEmpty()){
				StringBuffer selectedSublimitNames = new StringBuffer("");
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
						sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable();
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
			if (!sublimitMapAvailable){
				
				//IMSSUPPOR-28468 - VALIDATION AT SUBMIT
				if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && bean.getPreauthMedicalDecisionDetails() != null) {
					Integer amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
					if(amt <= 0) {
						showErrorMessage("Approved Amount is Zero. Hence this Preauth can not be submitted. ");
					}else{
						fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);	
					}
				}else{
					fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);	
				}
				
			}
			else if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
				final MessageBox showInfo= showInfoMessageBox("Sublimit selected is "+selectedSublimitNames.toString());
				Window window = showInfo.getWindow();
				window.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
						wizard.getFinishButton().setEnabled(true);
						showInfo.close();
						
					}
				});
			
				Button homeButton = showInfo.getButton(ButtonType.OK);
				homeButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						/*wizard.getFinishButton().setEnabled(true);
						showInfo.close();*/
						
						showInfo.close();
						
						//IMSSUPPOR-28468 - VALIDATION AT SUBMIT
						if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && bean.getPreauthMedicalDecisionDetails() != null) {
							Integer amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
							if(amt <= 0) {
								showErrorMessage("Approved Amount is Zero. Hence this Preauth can not be submitted. ");
							}else{
								fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, bean);	
							}
						}else{
							fireViewEvent(PreauthWizardPresenter.PREAUTH_SUBMITTED_EVENT, bean);	
						}	
						
						
					}
				});
				//showInfo.open();
			}
			
			 
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
		//Vaadin8-setImmediate() cancel.setImmediate(true);
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
					
					fireViewEvent(PreauthWizardPresenter.SAVE_AUTO_ALLOCATION_CANCEL_REMARKS, bean);
					popup.close();
					SHAUtils.setClearPreauthDTO(bean);
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
				}
				else
				{
					StringBuffer alertMsg = new StringBuffer();
					alertMsg.append("Please enter the Cancel Remarks");
					getautoAllocationCancelAlert(alertMsg.toString());
				}
				
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void getautoAllocationCancelAlert(String message) {/*	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>"+ message + "</b>",
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
		*/
		final MessageBox showInfoMessageBox = showInfoMessageBox(message);
		Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});	
	}
	
public void buildSTPRemarksLayout(){
	
	    Integer deductible = this.bean.getTotalDeductibleAmount() != null ? Integer.valueOf(this.bean.getTotalDeductibleAmount()):0;
	    Integer totalNonPayable = this.bean.getToatlNonPayableAmt() != null ? this.bean.getToatlNonPayableAmt().intValue():0;
	    
	    String userClaimedAmt = this.bean.getAmountRequested();
	    String userApprovedAmt = this.bean.getAmountConsidered();
	    Integer totalDeduction = deductible + totalNonPayable;
	
		Table table = new Table();
		/*table.setHeight("200px");
		table.setWidth("200px");*/
		table.addContainerProperty("type", String.class, null);
		table.addContainerProperty("amountClaimed",  String.class, null);
		table.addContainerProperty("deductibleAmount",  String.class, null);
		table.addContainerProperty("approvedAmt",  String.class, null);
		table.addContainerProperty("decision",  String.class, null);
		table.setColumnHeader("type", "Type");
		table.setColumnHeader("amountClaimed", "Amount <br>Claimed");
		table.setColumnHeader("deductibleAmount", "Deductible <br>Amt");
		table.setColumnHeader("approvedAmt", "Approved Amount");
		table.setColumnHeader("decision", "Decision");
		table.setPageLength(2);
		table.setSizeFull();
		table.setHeight("140%");
		int i = 0;
		/*for (SelectValue selectValue : duplicateInsured) {
			table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
		}*/
			table.addItem(new Object[]{"System",  bean.getPreauthMedicalDecisionDetails().getStpClaimedAmt(),bean.getPreauthMedicalDecisionDetails().getStpDeductibleAmt(),
					bean.getPreauthMedicalDecisionDetails().getStpApprovedAmt(),
					bean.getPreauthMedicalDecisionDetails().getStpStatus()}, i++);
		
		if(bean.getStatusKey() != null && ! bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
			txtSTPRemarks = new TextArea("Reason for Difference in decision");
			table.addItem(new Object[]{"User","-","-","-","-"}, i++);
		}else{
			txtSTPRemarks = new TextArea("Reason for Difference in deduction");
			String status = SHAUtils.getStatusMap().get(bean.getStatusKey());
			table.addItem(new Object[]{"User",  userClaimedAmt,totalDeduction.toString(),userApprovedAmt,status}, i++);
		}
		
//		Label successLabel = new Label("<b style = 'color: red; font-size:17px;'>The decision suggested by the system is different from the user decision,<br> Please Enter the remarks</b>", ContentMode.HTML);
	   
		txtSTPRemarks.setMaxLength(4000);
		txtSTPRemarks.setWidth("350px");
		txtSTPRemarks.setHeight("150px");
		txtSTPRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtSTPRemarks,null,getUI(),SHAConstants.STP_REMARKS);
		Button cancel = new Button("OK");
		//Vaadin8-setImmediate() cancel.setImmediate(true);
		cancel.addStyleName(ValoTheme.BUTTON_DANGER);
		cancel.setWidth("-1px");
		cancel.setHeight("-10px");
		cancel.setEnabled(true);
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(table,txtSTPRemarks,cancel);
		vLayout.setMargin(true);
		vLayout.setSpacing(true);
		vLayout.setComponentAlignment(txtSTPRemarks, Alignment.MIDDLE_CENTER);
		vLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("60%");
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				wizard.getFinishButton().setEnabled(true);
			}
		});
		
		cancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(null != txtSTPRemarks && null != txtSTPRemarks.getValue() && !("").equalsIgnoreCase(txtSTPRemarks.getValue()) && StringUtils.isNotBlank(txtSTPRemarks.getValue())){
					bean.setStpRemarks(txtSTPRemarks.getValue());
					
					fireViewEvent(PreauthWizardPresenter.SAVE_STP_REMARKS, bean);
					popup.close();
					proceedForFinalSubmit();
				}
				else
				{
					String eMsg = "Please Enter Remarks";
					showErrorPopup(eMsg);
					wizard.getFinishButton().setEnabled(true);
				}
					
			}
		});
				
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}


	public MessageBox showInfoMessageBox(String message){
	
		final MessageBox msgBox = MessageBox
		    .createInfo()
		    .withCaptionCust("Information")
		    .withMessage(message)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	
		return msgBox;
	}

	@Override
	public void generateButtonLayoutBasedOnScoring(ProcessPreAuthButtonLayout buttons) {
		this.medicalDecisionTableObj = medicalDecisionTable.get();
		//this.medicalDecisionTableObj.init(this.bean);
		
		buttons.initView(this.bean, wizard, this.medicalDecisionTableObj);
	}

	@Override
	public void generateTopUpLayout(List<PreauthDTO> topupData) {
		if(!topupData.isEmpty()){
			preauthMedicalDecisionPage.generateTopUpLayout(topupData);		
		}else{
			preauthMedicalDecisionPage.showInfoMessageBox("Top-up policy is not available for this claim period");
		}
	}
	
	@Override
	public void generateTopUpIntimationLayout(String topupData) {
		String topUpIntmMsg = "Intimation "+topupData+ " is created from base policy intimation: "+bean.getNewIntimationDTO().getIntimationId();
		preauthMedicalDecisionPage.showInfoMessageBox(topUpIntmMsg);		
	}

	@Override
	public void setTreatingQualificationValues(BeanItemContainer<SelectValue> qualification) {
		preauthDataExtractionPage.setTreatingqualification(qualification);
		
	}

	@Override
	public void addCategoryValues(SelectValue categoryValues) {
		preauthDataExtractionPage.addCategoryValues(categoryValues);
		
	}
	
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	}

	@Override
	public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	}
	
	@Override
	public void setnegotiatedSavedAmount(){
		Long addedDNPamoutn = 0L;
		if(preauthDataExtractionPage !=null){
			addedDNPamoutn = preauthDataExtractionPage.getDiscountandNonPayableAmt();
		}
		preauthMedicalDecisionPage.setnegotiatedSavedAmount(addedDNPamoutn,preauthDataExtractionPage.getTotalRequestedAmt());
		
	}
	
	@Override
	public void setAppAmountBSIAlert(){
		preauthMedicalDecisionPage.setAppAmountBSIAlert();
	}
	
	@Override
	public void removedynamicComp(){
		preauthMedicalDecisionPage.removedynamicComp();
	}
	
	@Override
	public void generateFieldsBasedOnImplantApplicable(Boolean isChecked) {
		preauthDataExtractionPage.generateFieldsBasedOnImplantApplicable(isChecked);
	}
	
	private void submitIcacProcess(){

		HorizontalLayout horLayout = new HorizontalLayout();
		TextArea processICACRemarks = new TextArea(
				"Refer to ICAC Remarks");
		bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer to ICAC");

		processICACRemarks.setMaxLength(3000);
		processICACRemarks.setWidth("400px");
		processICACRemarks.setHeight("200px");
		processICACRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(processICACRemarks,null,getUI(),SHAConstants.ICAC_REMARKS);

		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		popup.setHeight("50%");
		Button submitBtn = new Button("Submit");
		Button submitCancel = new Button("Cancel");

		FormLayout icacForm = new FormLayout(processICACRemarks);
		HorizontalLayout hLayout = new HorizontalLayout(icacForm);
		submitBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		submitBtn.setHeight("-1px");
		submitCancel.setHeight("-1px");
		horLayout.addComponents(submitBtn);
		horLayout.addComponents(submitCancel);
		horLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponents(hLayout, horLayout);
		verticalLayout.setComponentAlignment(horLayout,
				Alignment.MIDDLE_CENTER);
		popup.setContent(verticalLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);

		submitCancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Cancel listener called");
				popup.close();
			}
		});
		popup.addCloseListener(new Window.CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		submitBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String pccRemarkValue = "";
				System.out.println("Submit listener called");

				if (processICACRemarks.getValue() != null
						&& !processICACRemarks.getValue().trim()
						.isEmpty()) {

					System.out.println("----ICAC-----"
							+ processICACRemarks + "-----------");
					//Parraell Processing submit
					bean.setIcacProcessRemark(processICACRemarks.getValue());
					bean.setIcacStatusId(ReferenceTable.PREAUTH_STAGE);
					fireViewEvent(PreauthWizardPresenter.PREAUTH_ICAC_SUBMIT_EVENT,bean);
					popup.close();
				} else {
					showErrorMessage("Please enter the Refer to ICAC remarks fields");
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

 }

	@Override
	public void generateFieldsBasedOnSubCatTWO(BeanItemContainer<SelectValue> procedure) {
		preauthMedicalDecisionPage.generateFieldsBasedOnSubCatTWO(procedure);
	}
	
	@Override
	public void addpccSubCategory(BeanItemContainer<SelectValue> procedure) {
		preauthMedicalDecisionPage.addpccSubCategory(procedure);
	}
}