  package com.shaic.claim.enhancements.preauth.wizard;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shaic.claim.enhancements.preauth.wizard.pages.PreauthEnhancementDataExtractionPage;
import com.shaic.claim.enhancements.preauth.wizard.pages.PreauthEnhancementMedicalDecisionPage;
import com.shaic.claim.enhancements.preauth.wizard.pages.PreauthEnhancementMedicalProcessingPage;
import com.shaic.claim.enhancements.preauth.wizard.pages.PreauthEnhancementPreviousClaimDetailsPage;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.pancard.page.UpdatePanCardReportUI;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.ProcessPreAuthButtonLayout;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
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
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
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

public class PreauthEnhancementWizardViewImpl extends AbstractMVPView implements
		PreauthEnhancementWizard, GWizardListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013903728510416209L;

	@Inject
	private Instance<PreauthEnhancementDataExtractionPage> preauthDataExtractionPageInstance;
	
	@EJB
	private HospitalService hospitalService;

	@Inject
	private Instance<PreauthEnhancementMedicalDecisionPage> PreauthMedicalDecisionPageInstance;

	@Inject
	private Instance<PreauthEnhancementPreviousClaimDetailsPage> preauthPreviousClaimDetailsPageInstance;

	@Inject
	private Instance<PreauthEnhancementMedicalProcessingPage> preauthMedicalProcessingPageInstance;
	
	@Inject
	private Instance<EnhancementDecisionCommunicationPage> preauthDecisionCommunicationPageInstance;
	
	private EnhancementDecisionCommunicationPage preauthDecisionCommunicationPage;


	@Inject
	private Instance<ProcessPreAuthButtonLayout> processPreauthButtonLayoutInstance;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private UpdatePanCardReportUI updatePanCardReportUI;

	private PreauthEnhancementDataExtractionPage preauthDataExtractionPage;

	private PreauthEnhancementMedicalDecisionPage preauthMedicalDecisionPage;

	private PreauthEnhancementPreviousClaimDetailsPage preauthPreviousClaimDetailsPage;

	private ProcessPreAuthButtonLayout processPreAuthButtonLayout;

	private VerticalSplitPanel mainPanel;

//	private GWizard wizard;
	
	private IWizardPartialComplete wizard;

	private FieldGroup binder;

	private PreauthDTO bean;

	private PreauthEnhancementMedicalProcessingPage preauthMedicalProcessingPage;

	//private FormLayout hLayout;

	private Button viewPreAuthButton;

	private boolean isFVRDisabled = false;
	
	public Button btnViewRTABalanceSI;

	Panel panel1 = null;
	VerticalLayout wizardLayout2 = null;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private LumenDbService lumenService;
	@Inject		
	private ViewSeriousDeficiencyUI revisedViewSeriousDeficiencyTable;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	private final Logger log = LoggerFactory.getLogger(PreauthEnhancementWizardViewImpl.class);
	
	 @Inject
		private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
		 
	@EJB
		private PolicyService policyService;
		 
	 @Inject
		private ZUATopViewQueryTable zuaTopViewQueryTable;
		
	 @Inject
		private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;
	 
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
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

	@EJB
	private ProcessICACService processICACService;
	
	@Inject
	private Toolbar toolBar;
	
	//CR2019017 - Start
	@Inject
	private Instance<PreauthEnhancementButtons> processPreauthButtonLayout;
	private PreauthEnhancementButtons processPreauthButtonObj;
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;
	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	//CR2019017 - End

	private void initBinder() {

		wizard.getFinishButton().setCaption("Submit");
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

	public void initView(PreauthDTO preauthDTO) {
		this.bean = preauthDTO;
//		this.wizard = new IWizard();
		this.wizard = new IWizardPartialComplete();
		initBinder();
		mainPanel = new VerticalSplitPanel();
		// mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		PreauthEnhancementDataExtractionPage preauthDataExtractionPage = preauthDataExtractionPageInstance
				.get();
		preauthDataExtractionPage.init(this.bean, this.wizard);
		this.preauthDataExtractionPage = preauthDataExtractionPage;
		wizard.addStep(preauthDataExtractionPage, "Data Extraction");

		PreauthEnhancementPreviousClaimDetailsPage preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPageInstance
				.get();
		preauthPreviousClaimDetailsPage.init(this.bean);
		this.preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPage;
		wizard.addStep(preauthPreviousClaimDetailsPage,
				"Previous Claim Details");
		/*Below condition removed as per CR2019007 copay editable for GMC product
		if(! ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
		if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()) {	
		PreauthEnhancementMedicalProcessingPage preauthMedicalProcessingPage = preauthMedicalProcessingPageInstance
					.get();
			preauthMedicalProcessingPage.init(this.bean, this.wizard);
			this.preauthMedicalProcessingPage = preauthMedicalProcessingPage;
			wizard.addStep(preauthMedicalProcessingPage, "Medical Processing");
//		}
		}else if(!bean.getNewIntimationDTO().getIsPaayasPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()) {
			
			PreauthEnhancementMedicalProcessingPage preauthMedicalProcessingPage = preauthMedicalProcessingPageInstance
					.get();
			preauthMedicalProcessingPage.init(this.bean, this.wizard);
			this.preauthMedicalProcessingPage = preauthMedicalProcessingPage;
			wizard.addStep(preauthMedicalProcessingPage, "Medical Processing");
		}
		PreauthEnhancementMedicalDecisionPage preauthMedicalDecisionPage = PreauthMedicalDecisionPageInstance
				.get();
		preauthMedicalDecisionPage.init(this.bean, this.wizard);
		this.preauthMedicalDecisionPage = preauthMedicalDecisionPage;
		wizard.addStep(preauthMedicalDecisionPage, "Medical Decision");
		EnhancementDecisionCommunicationPage preauthDecisionCommunicationPage = preauthDecisionCommunicationPageInstance.get();
		preauthDecisionCommunicationPage.init(this.bean);
		this.preauthDecisionCommunicationPage = preauthDecisionCommunicationPage;
		wizard.addStep(preauthDecisionCommunicationPage,
				"Decision Communication");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);

		// Get Button Instace
		ProcessPreAuthButtonLayout processPreAuthButtonLayout2 = processPreauthButtonLayoutInstance
				.get();
		this.processPreAuthButtonLayout = processPreAuthButtonLayout2;

//		VerticalLayout wizardLayout = new VerticalLayout(wizard);

		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
//		intimationDetailsCarousel.init(preauthDTO.getNewIntimationDTO(),
//				preauthDTO.getClaimDTO(), "Process Enhancement");
		
		intimationDetailsCarousel.init(preauthDTO, "Process Enhancement");
		mainPanel.setFirstComponent(intimationDetailsCarousel);

		buildCommonDetailsLayout();
		
		if(bean.getIsPedPending()){
			getErrorMessage("Pending PED in Action");
		}

		/*
		 * mainPanel.addComponent(wizardLayout1); setCompositionRoot(mainPanel);
		 */
		fireViewEvent(PreauthEnhancemetWizardPresenter.SETUP_REFERENCE_DATA,
				bean);
		
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
		processPreauthButtonObj.initView(this.bean,this.wizard, this.medicalDecisionTableObj);

		if(bean.getIsSDEnabled() && processPreauthButtonObj.getApproveBtn().isEnabled()){
			processPreauthButtonObj.getApproveBtn().setEnabled(false);
		}/*else{
			processPreauthButtonObj.getApproveBtn().setEnabled(true);
		}*/
		//CR2019179
		if(bean.getIsSDEnabled() && processPreauthButtonObj.getQueryBtn().isEnabled()){
			processPreauthButtonObj.getQueryBtn().setEnabled(false);
		} //CR2019179 End
		bean.setProcessEnhancementButtonObj(processPreauthButtonObj);
		//CR2019017 - End
	}

	private void buildCommonDetailsLayout() {

		if (null != wizardLayout2) {
			// mainPanel.removeComponent(panel1);
			mainPanel.removeComponent(wizardLayout2);
		}

		TextField netWorkHospitalType = new TextField("Network Hosp Type");
		netWorkHospitalType.setValue(bean.getNewIntimationDTO()
				.getHospitalDto().getRegistedHospitals()
				.getNetworkHospitalType());
		;
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
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

		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setReadOnly(true);
		typeFld.setNullRepresentation("");

		FormLayout hLayout = new FormLayout(netWorkHospitalType, typeFld);

		// FormLayout hLayout = new FormLayout (netWorkHospitalType);
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
		
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
				ViewLevels.INTIMATION, true,"Process Enhancements");
		viewDetails.setPreAuthKey(this.bean.getKey());
		viewDetails.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		viewDetailsForm.addComponent(viewDetails);
		
		HorizontalLayout showPremedicalStatusHLayout = showPremedicalStatus();
		
		HorizontalLayout show64VBlayout = show64VBlayout();
		HorizontalLayout premedicalAnd64layout = new HorizontalLayout(showPremedicalStatusHLayout, show64VBlayout);
		premedicalAnd64layout.setSpacing(true);
		//VerticalLayout wizardLayout1 = new VerticalLayout(mainHor,premedicalAnd64layout);
//		HorizontalLayout preauthViewLayout = viewPreAuthButton();
		if(bean.getIsAutoAllocationCPUUser() && (bean.getIsAboveLimitCorpAdvise() || bean.getIsAboveLimitCorpProcess())){
			//As per Urgent request from BA
			//showPremedicalStatusHLayout.addComponent(showDoctorOpinion());
		}
		
		HorizontalLayout commonButtons = commonButtons();
		HorizontalLayout topHorizontal = new HorizontalLayout(commonButtons,viewDetailsForm);
		topHorizontal.setWidth("100%");
		topHorizontal.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		topHorizontal.setComponentAlignment(commonButtons, Alignment.BOTTOM_LEFT)
		VerticalLayout wizardLayout1 = new VerticalLayout(topHorizontal,hosHor,premedicalAnd64layout);
		
//		preauthViewLayout.setSpacing(true);
//		VerticalLayout wizardLayout1 = new VerticalLayout(preauthViewLayout,
//				commonButtons(), showPremedicalStatusHLayout);
		wizardLayout1.setSpacing(false);
		panel1 = new Panel();
		panel1.setContent(wizardLayout1);

		panel1.setHeight("240px");

//		panel1.setHeight("210px");

		// mainPanel.addComponent(panel1);
		// VerticalLayout wizardLayout1 = new VerticalLayout(commonButtons(),
		// showPremedicalStatusHLayout , wizard);
		// wizardLayout2 = new VerticalLayout(wizard);
		// =======
		// VerticalLayout wizardLayout2 = new VerticalLayout(panel1,wizard);
		wizardLayout2 = new VerticalLayout(panel1, wizard);
		// >>>>>>> e21c62e0d66f756ebae6a1bb4d4da6cfd30ee7df
		// mainPanel.addComponent(wizardLayout1);
		mainPanel.setSecondComponent(wizardLayout2);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
	}

	/**
	 * Method added for view pre auth button
	 * */

	private HorizontalLayout viewPreAuthButton() {
		viewPreAuthButton = new Button("View Pre-auth");
		viewPreAuthButton.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				viewDetails.getPreAuthUpdateDetail(bean.getNewIntimationDTO()
						.getIntimationId());
			}
		});
		/*
		 * FormLayout viewDetailsForm = new FormLayout();
		 * //Vaadin8-setImmediate() viewDetailsForm.setImmediate(true); viewDetailsForm.setWidth("-1px");
		 * viewDetailsForm.setHeight("-1px"); viewDetailsForm.setMargin(false);
		 * viewDetailsForm.setSpacing(true);
		 */
		// ComboBox viewDetailsSelect = new ComboBox();
		// viewDetailsForm.addComponent(viewDetailsSelect);
		/*
		 * viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
		 * ViewLevels.INTIMATION); viewDetailsForm.addComponent(viewDetails);
		 * HorizontalLayout horizontalLayout1 = new HorizontalLayout(
		 * viewDetailsForm); horizontalLayout1.setSizeUndefined();
		 * //Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		 * horizontalLayout1.setSpacing(true);
		 */
//		TextField dummyFld = new TextField();
//		dummyFld.setVisible(true);
//		dummyFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		dummyFld.setReadOnly(true);
//		FormLayout layoutForPreauthBtn = new FormLayout(
//				viewPreAuthButton);
////		layoutForPreauthBtn.setComponentAlignment(dummyFld,
////				Alignment.MIDDLE_RIGHT);
//		layoutForPreauthBtn.setComponentAlignment(viewPreAuthButton,
//				Alignment.MIDDLE_RIGHT);
//		// HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout,new
//		// FormLayout(),new FormLayout() ,layoutForPreauthBtn,
//		// horizontalLayout1);
//		HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout,
//				new FormLayout(), new FormLayout(), layoutForPreauthBtn,
//				new FormLayout());
//		componentsHLayout.setSizeFull();
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				viewPreAuthButton);
//		alignmentHLayout.setComponentAlignment(componentsHLayout, Alignment.TOP_LEFT);
//		alignmentHLayout.setWidth("100%");
		// alignmentHLayout.setComponentAlignment(componentsHLayout,
		// Alignment.MIDDLE_RIGHT);

		return alignmentHLayout;

	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		fireViewEvent(
				PreauthEnhancemetWizardPresenter.PREAUTH_STEP_CHANGE_EVENT,
				event);

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		
		if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
				|| bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
				|| bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)
				|| (!bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() 
						&& bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))) {
			
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
		{	if(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey())){
				fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_HOLD_SUBMITTED_EVENT, this.bean);
			}
			else{
				proceedForFinalSubmit();
			}
//			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT,
//				this.bean);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void wizardCancelled(WizardCancelledEvent event) {/*
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							// Confirmed to continue
													
							releaseHumanTask();
							if(bean.getIsPreauthAutoAllocationQ()){
								autoAllocationCancelRemarksLayout();
								
							}
							else{
								
							SHAUtils.setClearPreauthDTO(bean);
								
							fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
									null);
							
							clearObject();
							fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
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
		
		final MessageBox open = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Are you sure you want to cancel ?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
				
				Button yesBtn = open.getButton(ButtonType.YES);
				yesBtn.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// Confirmed to continue
						if(!bean.getIsPreauthAutoAllocationQ()){
							releaseHumanTask();
						}
						if(bean.getIsPreauthAutoAllocationQ()){
							autoAllocationCancelRemarksLayout();
							
						}
						else{
							
						SHAUtils.setClearPreauthDTO(bean);
							
						fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
								null);
						
						clearObject();
						fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
						VaadinRequest currentRequest = VaadinService.getCurrentRequest();
	    				SHAUtils.clearSessionObject(currentRequest);
						}
					}
				});
				
				Button noBtn = open.getButton(ButtonType.NO);
				noBtn.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						open.close();
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
	public void generateWithdrawLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		preauthMedicalDecisionPage.generateButton(4, selectValueContainer);

	}

	@Override
	public void generateDenialLayout(Object denialValues) {
		preauthMedicalDecisionPage.generateButton(4, denialValues);
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
		HorizontalLayout hoapitalScore = SHAUtils.hospitalScore(bean,hospitalService);
        HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
        
        HorizontalLayout icrAgentBranchLayout = SHAUtils.icrAgentBranch(bean);
		
		/*HorizontalLayout hlayout=new HorizontalLayout(crmLayout, hopitalFlag);
		hlayout.setSpacing(true);*/
		
		HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,hoapitalScore,hopitalFlag);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");	
		VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranchLayout);
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

		
		/*
		 * ComboBox viewDetailsSelect = new ComboBox();
		 * viewDetailsForm.addComponent(viewDetailsSelect);
		 */
		/*if (isFVRDisabled) {
			viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
					ViewLevels.INTIMATION, isFVRDisabled);
		} else {
			viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
					ViewLevels.INTIMATION, false);
		}*/
		
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
		
		
	

		/*
		 * Button goButton = new Button("GO"); HorizontalLayout
		 * horizontalLayout1 = new HorizontalLayout( viewDetailsForm, goButton);
		 */
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm);
//		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
//		horizontalLayout1.setComponentAlignment(viewDetailsForm, Alignment.BOTTOM_RIGHT);
		
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
		/*FormLayout form2 = new FormLayout();
		form2.setWidth("50px");*/
		
		Button btnViewMBPackage = new Button("View MB Package");
		btnViewMBPackage.setVisible(false);
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy().equals(1)){
			btnViewMBPackage.setVisible(true);
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
		
		VerticalLayout verticalBtns = new VerticalLayout(btnViewPackage ,btnViewMBPackage, viewCoordinatorButton,btnViewDoctorRemarks);
		verticalBtns.setSpacing(true);
		
		
//		HorizontalLayout componentsHLayout = new HorizontalLayout(verticalBtns , form2);
		//componentsHLayout.setComponentAlignment(viewCoordinatorButton, Alignment.BOTTOM_LEFT);
//		componentsHLayout.setComponentAlignment(verticalBtns, Alignment.TOP_LEFT);
		//componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_LEFT);
		
	
		Button referToFLP = new Button("Refer to FLE");
		if(bean.getCreateDate() != null && (bean.getCreateDate().compareTo(SHAUtils.getFromDate(BPMClientContext.REFER_TO_FLP_APPLICABLE_DATE)) < 0)) {
			referToFLP.setEnabled(false);
		}
		referToFLP.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		referToFLP.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				TextArea approvalRemarksTxta = new TextArea("Refer to FLE Remarks");
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
		
		HorizontalLayout alignmentHLayout1 = null;
		Button btnEsclateToRAW = new Button("Escalate To Raw");
		btnEsclateToRAW.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SHAUtils.buildEsclateToRawView(esclateClaimToRawPageInstance,esclateClaimToRawPageViewObj,bean,SHAConstants.PROCESS_ENHANCEMENT);
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
				sourceList.add(SHAConstants.PRE_AUTH_ENHANCEMENT);
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
						&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())  ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) )
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null
				&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			preauthDataExtractionPage.setRTAButton(btnViewRTABalanceSI);
			alignmentHLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,viewPreAuthButton(),btnViewRTABalanceSI);

		}
		else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
			if(bean.getNewIntimationDTO().getPolicy().getGmcPolicyType() != null && 
					((bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)) || (bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT)))){
				alignmentHLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,viewPreAuthButton(),viewLinkedPolicy/*,icrLayout*/);

			} else {
				alignmentHLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,viewPreAuthButton()/*,icrLayout*/);

			}
		}
		else
		{
			alignmentHLayout1 = new HorizontalLayout(btnRRC,referToFLP,btnLumen,btnViewDoctorRemarks,elearnBtn,btnViewPolicySchedule,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewCoordinatorButton,btnZUAAlert,viewNegotiationDtls,btnViewPackage,btnViewMBPackage,viewPreAuthButton());

		}
		alignmentHLayout1.setSpacing(true);
		/*HorizontalLayout crmLayout1 = new HorizontalLayout(crmLayout,btnEsclateToRAW);
		btnEsclateToRAW.setEnabled(Boolean.FALSE);
		crmLayout1.setSpacing(true);*/
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(processICACBtn);
		
		HorizontalLayout crmLayout1 = new HorizontalLayout(icrAGBR);
		VerticalLayout vLayout = new VerticalLayout(crmLayout1,alignmentHLayout1,horizontalLayout2);
		vLayout.setSpacing(true);
		horizontalLayout2.setSpacing(true);
		horizontalLayout2.setMargin(false);
		HorizontalLayout componentsHLayout = new HorizontalLayout(vLayout);
		return componentsHLayout;

	}
	
	private Button getSubmitButtonWithListener(final ConfirmDialog dialog, final TextArea remarksfield) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				bean.setStatusKey(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS);
				bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
				if(remarksfield != null && remarksfield.getValue() != null && remarksfield.getValue().length() > 0) {
					bean.getPreauthMedicalDecisionDetails().setReferToFLPremarks(remarksfield.getValue());
					bean.setIsReferTOFLP(true);
					bean.setDocFilePath(null);
					bean.setDocSource(null);
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
	//zz
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
	
	 private void openPdfFileInWindow(final String filepath) {
			
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
			StreamResource.StreamSource s = SHAUtils.getStreamResource(filepath);

			/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
			};*/

			StreamResource r = new StreamResource(s, fileName);
			Embedded e = new Embedded();
			e.setSizeFull();
			e.setType(Embedded.TYPE_BROWSER);
			r.setMIMEType("application/pdf");
			e.setSource(r);
			SHAUtils.closeStreamResource(s);
			window.setContent(e);
			UI.getCurrent().addWindow(window);
		 }
	  
	     public void getErrorMessage(String eMsg){/*
			
			Label label = new Label(eMsg, ContentMode.HTML);
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
		if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
				|| bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
				|| bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(0L);
		}else if(preauthMedicalDecisionPage !=null){
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(preauthMedicalDecisionPage.getApprovedAmtForRRC());
		}
		fireViewEvent(PreauthEnhancemetWizardPresenter.VALIDATE_PREAUTH_ENHANCEMENT_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_ENHANCEMENT);
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
		fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LUMEN_REQUEST, bean);
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Process-Enhancement");
		
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
			Object allocationToValues,Object assignToValues,Object Fvrprioirty, List<ViewFVRDTO> fvrDTOList) {
		preauthMedicalDecisionPage.generateFieldsBasedOnFieldVisit(isChecked,
				allocationToValues,assignToValues,Fvrprioirty, fvrDTOList);
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
		
//		MessageBox.showPlain(Icon.NONE, "Example 9", "Long plain text has to be broken manually with an '\\n':\n1. line\n2. line\n3. line\n4. line\n5. line\n6. line", ButtonId.OK);
		Label successLabel = new Label(
				"<b style = 'color: green;'> Cashless Claim Record Saved Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Pre-auth Enhancement Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
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
		dialog.setStyleName("boxshadow");
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(bean.getIsPreauthAutoAllocationQ()){
					toolBar.countTool();
					SHAUtils.setClearPreauthDTO(bean);
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
				toolBar.countTool();
				SHAUtils.setClearPreauthDTO(bean);	
					
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
	*/

		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage("Cashless Claim Record Saved Successfully.")
			    .withOkButton(ButtonOption.caption("Pre-auth Enhancement Home"))
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
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
					
				SHAUtils.setClearPreauthDTO(bean);	
				toolBar.countTool();
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
	
	}
	
	@Override
	public void buildFailureLayout(String acquiredUser) {/*
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim is already opened by "+acquiredUser +"</b>",
				ContentMode.HTML);
		
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Enhancement Home");
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
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
					
				SHAUtils.setClearPreauthDTO(bean);	
					
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
	*/
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage("Claim is already opened by "+acquiredUser)
			    .withOkButton(ButtonOption.caption("Enhancement Home"))
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
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
					
				SHAUtils.setClearPreauthDTO(bean);	
					
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}

			}
		});
		
	}
	
	@Override
	public void validationForLimitAmount() {/*
		Label successLabel = new Label(
				"<b style = 'color: red;'> Approval amount is beyond your limit.",
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
				/*if(bean.getIsPreauthAutoAllocationQ()){
					releaseHumanTask();
					SHAUtils.setClearPreauthDTO(bean);
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					
					clearObject();
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
					
					releaseHumanTask();
					
				SHAUtils.setClearPreauthDTO(bean);	
					
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}*/

			
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(" Approval amount is exceed for this user.")
			    .withOkButton(ButtonOption.caption("Enhancement Home"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				/*if(bean.getIsPreauthAutoAllocationQ()){
					releaseHumanTask();
					SHAUtils.setClearPreauthDTO(bean);
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					
					clearObject();
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
    				SHAUtils.clearSessionObject(currentRequest);
				}
				else{
					
					releaseHumanTask();
					
				SHAUtils.setClearPreauthDTO(bean);	
					
				fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,
						null);
				clearObject();
				fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				}*/

			}
		});
		
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
			premedicalRemarks.setHeight("50px");
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
	
	@SuppressWarnings("unused")
	private HorizontalLayout showPremedicalStatus() {
		TextField premedicalSuggestion = new TextField("First Level Suggestion");
		// premedicalSuggestion.setValue("Send For Processing");
		premedicalSuggestion.setEnabled(false);
		TextField premedicalCategory = new TextField("Category");
		TextField changeInPreauth = new TextField("Change in Pre-auth");

		String category = "";
		if (this.bean.getPreauthMedicalProcessingDetails().getCategory() != null) {
			category = this.bean
					.getPreauthMedicalProcessingDetails()
					.getCategory()
					.getId()
					.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY) ? "Non - Medical"
					: "Medical";
		}

		String changeInPreauthValue = "";
		if (this.bean.getPreauthMedicalProcessingDetails()
				.getChangeInPreauthFlag() != null) {
			changeInPreauthValue = this.bean
					.getPreauthMedicalProcessingDetails()
					.getChangeInPreauthFlag().toLowerCase()
					.equalsIgnoreCase("y") ? "Yes" : "No";
		}
		changeInPreauth.setValue(changeInPreauthValue);
		changeInPreauth.setEnabled(false);
		premedicalCategory.setValue(category);
		premedicalCategory.setEnabled(false);
		TextArea premedicalRemarks = new TextArea("First Level Remarks");
		if (this.bean.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)
				|| this.bean.getStatusKey().equals(
						ReferenceTable.ENHANCEMENT_QUERY_STATUS)
				|| this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS )) {
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalSuggestion.setValue("Query");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);

		} else if (this.bean
				.getStatusKey()
				.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
			premedicalSuggestion.setValue("Suggestion Rejection");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} else if (this.bean
				.getStatusKey()
				.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS)) {
			premedicalSuggestion.setValue("Send for Processing");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)) {
			premedicalSuggestion.setValue("Esclation");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean
					.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		} else if (this.bean.getStatusKey().equals(
				ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)
				|| this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS)) {
			premedicalSuggestion.setValue("Refer to Coordinator");
			premedicalRemarks = new TextArea("First Level Remarks");
			premedicalRemarks.setHeight("50px");
			premedicalRemarks.setValue(this.bean.getCoordinatorDetails()
					.getCoordinatorRemarks());
			premedicalRemarks.setNullRepresentation("");
			premedicalRemarks.setEnabled(false);
		}

		premedicalRemarks.setHeight("50px");
		premedicalRemarks.setValue(this.bean
				.getPreauthMedicalProcessingDetails().getMedicalRemarks());
		premedicalRemarks.setNullRepresentation("");
		premedicalRemarks.setEnabled(false);
		HorizontalLayout premedicalHLayout = new HorizontalLayout(
				new FormLayout(premedicalSuggestion, premedicalRemarks),
				new FormLayout(premedicalCategory, changeInPreauth));
		premedicalHLayout.setSpacing(true);

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
	public void generateDownSizeTableLayout(
			BeanItemContainer<SelectValue> downsizeReasonContainer) {
		preauthMedicalDecisionPage.generateButton(7, downsizeReasonContainer);
	}

	public void intiateCoordinatorRequest() {
		if (preauthDataExtractionPage.validatePage()) {
			preauthDataExtractionPage.setTableValuesToDTO();
			fireViewEvent(
					PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT,
					this.bean);
		}
	}

	@Override
	public void setPreAuthRequestedAmt(String strPreAuthAmt) {
		preauthMedicalDecisionPage.setPreAuthRequestAmt(strPreAuthAmt);

	}

	@Override
	public void disableFVRDetails() {
		isFVRDisabled = true;
		buildCommonDetailsLayout();

	}

	@Override
	public void enableFVRDetails() {
		isFVRDisabled = false;
		buildCommonDetailsLayout();

	}

	@Override
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		preauthDataExtractionPage
				.setHospitalizationDetails(hospitalizationDetails);

	}

	@Override
	public void viewBalanceSumInsured(String intimationId) {
		preauthMedicalDecisionPage.showBalanceSumInsured(intimationId);
	}

	
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
	 		Long workFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 		//	BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(workFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(workFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	 
	 
	 public void compareWithUserId(String userId) {/*

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
					fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,null);
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
		 
		 final MessageBox showInfo= showInfoMessageBox("Intimation is already opened by another user"+userId);
		 Button homeButton = showInfo.getButton(ButtonType.OK);

			
		 homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,null);
					showInfo.close();
				}
			});
	
 }
	 
	@Override
	public void genertateFieldsBasedOnHospitalisionDueTo(
			SelectValue selectedValue) {
		preauthDataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo(selectedValue);
		
	}
	
	@Override
	public void genertateFieldsBasedOnOtherBenefits(PreauthDTO bean){
		preauthDataExtractionPage.generatedFieldsBasedOnOtherBenefits(bean);
	}

	@Override
	public void genertateFieldsBasedOnReportedToPolice(Boolean selectedValue) {
		preauthDataExtractionPage.generatedFieldsBasedOnReportedToPolice(selectedValue);
		
	}

	@Override
	public void setBalanceSIforRechargedProcess(Double balanceSI) {
		preauthMedicalDecisionPage.setBalanceSIforRechargedProcessing(balanceSI);
	}

	@Override
	public void generateWithdrawAndRejectLayout(
			BeanItemContainer<SelectValue> withdrawContainer,
			BeanItemContainer<SelectValue> rejectionContainer) {
		preauthMedicalDecisionPage.generateWithdrawAndRejectButton(withdrawContainer, rejectionContainer);
	}
	
	@Override
	public void generateWithdrawAndRejectLayout(BeanItemContainer<SelectValue> withdrawRejectContainer){
		preauthMedicalDecisionPage.generateWithdrawAndRejectButton(withdrawRejectContainer);
	}
	
	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {

		preauthDataExtractionPage.setCoverList(coverContainer);

	}


	@Override
	public void setRemarks(MasterRemarks remarks, String decision) {
		preauthMedicalDecisionPage.setRemarks(remarks,decision);
		
	}
	
//	@Override
//	public void generate64VBComplainceLayout() {
//		preauthMedicalDecisionPage.generateButton(7, null);
//		
//	}

	public void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		this.preauthPreviousClaimDetailsPage.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
		
	}
	

	@Override
	public void generate64VBComplainceLayout() {
		preauthMedicalDecisionPage.generateButton(8, null);
		
	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		preauthDataExtractionPage.setSubCoverList(subCoverContainer);

	}
	
	private void clearObject() {
		
		if(rewardRecognitionRequestViewObj != null){
			rewardRecognitionRequestViewObj.invalidate();
		}
		
		if(preauthDataExtractionPage != null){
			preauthDataExtractionPage.setClearReferenceData();
		}
		if(preauthMedicalProcessingPage != null){
			preauthMedicalDecisionPage.setClearReferenceData();
		}
		if(preauthMedicalProcessingPage != null){
			preauthMedicalProcessingPage.setClearReferenceData();
		}
		preauthDataExtractionPage.clearTableObj();
		preauthDataExtractionPage = null;
		preauthMedicalDecisionPage = null;
		preauthMedicalProcessingPage = null;
	}

	@Override
	public void generatePTCACABGLayout(Boolean value) {
		preauthDataExtractionPage.generatedStentBasedOnSurgical();
	}
	
	@Override
	public void generatePTCACABGLayoutDelete() {
		preauthDataExtractionPage.generatedStentBasedOnSurgical();
	}
	
	public void generatePanCardLayout() {
		
		SearchUploadPanCardTableDTO searchUploadPanCardTableDTO = new SearchUploadPanCardTableDTO();
		VerticalLayout mainLayout = new VerticalLayout();
		searchUploadPanCardTableDTO.setNewIntimationDto(bean.getNewIntimationDTO());
		if(bean.getNewIntimationDTO()!=null){
			Policy policy = bean.getNewIntimationDTO().getPolicy();
			if(policy!=null){
				searchUploadPanCardTableDTO.setProposerName(policy.getProposerFirstName());
				searchUploadPanCardTableDTO.setPolicyNo(policy.getPolicyNumber());
			}
		}
		
		final ConfirmDialog dialog = new ConfirmDialog();
		//mainLayout = 
		

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("85%");
		popup.setHeight("100%");
		updatePanCardReportUI.init(searchUploadPanCardTableDTO, mainLayout, SHAConstants.PRE_AUTH_ENHANCEMENT, popup);
		
		//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
		popup.setCaption("Update Pan Card Details");
		popup.setContent(updatePanCardReportUI);
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
	
		//showInPopup(updatePanCardReportUI.init(searchUploadPanCardTableDTO, mainLayout, null), dialog);
		
		
	}

	@Override
	public void checkPanCardDetails(Boolean isAvailable) {
		preauthMedicalDecisionPage.isPanAvailable(isAvailable);
		
	}

	@Override
	public void generateOtherBenefitsPopup() {
		preauthDataExtractionPage.viewBenefitTablePopup();		
	}

	@Override
	public void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails,
			Map<String, Object> referenceData) {
		preauthMedicalDecisionPage.setUpdateOtherClaimsDetails(updateOtherClaimDetails, referenceData);
	}		

	public void generateCPUUserLayout(Object cpuSuggestionDropdownValues) {
		preauthMedicalDecisionPage.generateButton(9, cpuSuggestionDropdownValues);
	}
	
	public FormLayout showDoctorOpinion(){
		TextField processSuggestion = new TextField("Claim Processing Suggestion");
		if(this.bean
				.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory() != null){
			processSuggestion.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getCpuSuggestionCategory().getValue());
		}
		
		processSuggestion.setNullRepresentation("");
		processSuggestion.setEnabled(false);
		processSuggestion.setReadOnly(Boolean.TRUE);
		
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
		if(this.bean
				.getPreauthMedicalDecisionDetails().getCpuRemarks() != null){
			cpuRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getCpuRemarks());	
		}
		
		cpuRemarks.setNullRepresentation("");
		cpuRemarks.setEnabled(false);
		cpuRemarks.setReadOnly(Boolean.TRUE);
		FormLayout opinionform =new FormLayout(processSuggestion,amtSuggestion,cpuRemarks);
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
	public void setPreauthEnhnProcedureValues(
			BeanItemContainer<SelectValue> procedures) {
		preauthDataExtractionPage.setProcedurevalues(procedures);
	}
	
	@Override
	public void setTreatingQualificationValues(BeanItemContainer<SelectValue> qualification) {
		preauthDataExtractionPage.setTreatingqualification(qualification);
		
	}
	
	public void showScoringView(){
		hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), true);
		hospitalScoringView.setScreenName("Enhancement");
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
		preauthMedicalDecisionPage.generateButton(10, null);
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
					sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable();
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
			Label successLabel = new Label("<b style = 'color: red;'> Sublimit selected is "+selectedSublimitNames.toString()+"</b>", ContentMode.HTML);
			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton horizontalLayout);
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
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);	
			}
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT, bean);
				}
			});
	  }	

	*/

		boolean sublimitMapAvailable = false;
		List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		if(diagnosisList != null && !diagnosisList.isEmpty()){
				StringBuffer selectedSublimitNames = new StringBuffer("");
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable();
					if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null ){
						selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
					}
				}
			
			if (!sublimitMapAvailable){
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT, this.bean);	
			}
			else if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
			
				//showInfo.open();

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
						fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT, bean);
						
					}
				});
				//showInfo.open();
			
			}

	  }	

		
	}
	
public void autoAllocationCancelRemarksLayout(){
		
		autoAllocCancelRemarks = new TextArea("Cancel Remarks");
		autoAllocCancelRemarks.setMaxLength(1000);
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
					fireViewEvent(PreauthEnhancemetWizardPresenter.SAVE_AUTO_ALLOCATION_CANCEL_REMARKS, bean);
					popup.close();
					fireViewEvent(MenuItemBean.PROCESS_PREAUTH_AUTO_ALLOCATION,
							null);
					clearObject();
					fireViewEvent(PreauthEnhancemetWizardPresenter.REFERENCE_DATA_CLEAR, null);
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
				"<b style = 'color: red;'>" + message + "</b>",ContentMode.HTML);
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

		final MessageBox showInfo= showInfoMessageBox(message);
		Button homeButton = showInfo.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfo.close();
			}
		});
		
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
public void generateButtonLayoutBasedOnScoring(PreauthEnhancementButtons buttons) {
	if(buttons != null && preauthMedicalDecisionPage != null){
		preauthMedicalDecisionPage.setButtonsLayout(buttons);
	}
}

@Override
public void generateTopUpLayout(List<PreauthDTO> topupData) {
	if(!topupData.isEmpty())
	preauthMedicalDecisionPage.generateTopUpLayout(topupData);
	else{
		preauthMedicalDecisionPage.showInfoMessageBox("Top-up policy is not available for this claim period");
	}
}

@Override
public void generateTopUpIntimationLayout(String topUpIntimaiton) {
	String topUpIntmMsg = "Intimation "+topUpIntimaiton+ " is created from base policy intimation: "+bean.getNewIntimationDTO().getIntimationId();
	preauthMedicalDecisionPage.showInfoMessageBox(topUpIntmMsg);
}

/*@Override
public void genertateFieldsBasedOnNegotiation(Boolean isChecked) {
	preauthMedicalDecisionPage.genertateFieldsBasedOnNegotiation(isChecked);

}*/

/*@Override
public void updateNegotiationRemarks(String updatedRemarks) {
	preauthMedicalDecisionPage.updateNegotiationRemarks(updatedRemarks);

}*/

	@Override
	public void setQueryRemarks(String qryRemarks) {
	
		preauthMedicalDecisionPage.setEnhQryRemarks(qryRemarks);
		
	}

	@Override
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		preauthMedicalDecisionPage.setRejSugCategContainer(rejSubcategContainer);
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
	public void setEnhnAppAmountBSIAlert(){
		preauthMedicalDecisionPage.setEnhnAppAmountBSIAlert();
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

//		processICACRemarks.setValue(bean.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue() != null ? bean.getPreauthDataExtractionDetails().getEscalatePccRemarksvalue() : "");
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
					bean.setIcacStatusId(ReferenceTable.ENHANCEMENT_STAGE);
					bean.setIcacProcessRemark(processICACRemarks.getValue());
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_ICAC_SUBMIT_EVENT,bean);
					popup.close();
				} else {
					showErrorMessage("Please enter the Refer to ICAC remarks fields");
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	
 }
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		MessageBox.createError()
		.withCaptionCust("Errors").withHtmlMessage(eMsg)
		.withOkButton(ButtonOption.caption("OK")).open();	
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
