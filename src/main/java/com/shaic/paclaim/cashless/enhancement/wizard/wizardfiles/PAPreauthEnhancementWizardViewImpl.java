  package com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Investigation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.paclaim.cashless.enhancement.wizard.pages.PAPreauthEnhancementDataExtractionPage;
import com.shaic.paclaim.cashless.enhancement.wizard.pages.PAPreauthEnhancementMedicalDecisionPage;
import com.shaic.paclaim.cashless.enhancement.wizard.pages.PAPreauthEnhancementMedicalProcessingPage;
import com.shaic.paclaim.cashless.enhancement.wizard.pages.PAPreauthEnhancementPreviousClaimDetailsPage;
import com.shaic.paclaim.cashless.preauth.wizard.pages.PAProcessPreAuthButtonLayout;
import com.vaadin.server.StreamResource;
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
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class PAPreauthEnhancementWizardViewImpl extends AbstractMVPView implements
		PAPreauthEnhancementWizard, GWizardListener {

	private static final long serialVersionUID = -199550572008440029L;

	@Inject
	private Instance<PAPreauthEnhancementDataExtractionPage> preauthDataExtractionPageInstance;
	
	@EJB
	private HospitalService hospitalService;

	@Inject
	private Instance<PAPreauthEnhancementMedicalDecisionPage> PreauthMedicalDecisionPageInstance;

	@Inject
	private Instance<PAPreauthEnhancementPreviousClaimDetailsPage> preauthPreviousClaimDetailsPageInstance;

	@Inject
	private Instance<PAPreauthEnhancementMedicalProcessingPage> preauthMedicalProcessingPageInstance;
	
	@Inject
	private Instance<PAEnhancementDecisionCommunicationPage> preauthDecisionCommunicationPageInstance;
	
	private PAEnhancementDecisionCommunicationPage preauthDecisionCommunicationPage;


	@Inject
	private Instance<PAProcessPreAuthButtonLayout> processPreauthButtonLayoutInstance;

	@Inject
	private Instance<PARevisedCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;

	private PAPreauthEnhancementDataExtractionPage preauthDataExtractionPage;

	private PAPreauthEnhancementMedicalDecisionPage preauthMedicalDecisionPage;

	private PAPreauthEnhancementPreviousClaimDetailsPage preauthPreviousClaimDetailsPage;

	private PAProcessPreAuthButtonLayout processPreAuthButtonLayout;

	private VerticalSplitPanel mainPanel;

//	private GWizard wizard;
	
	private IWizardPartialComplete wizard;

	private FieldGroup binder;

	private PreauthDTO bean;

	private PAPreauthEnhancementMedicalProcessingPage preauthMedicalProcessingPage;

	private FormLayout hLayout;

	private Button viewPreAuthButton;

	private boolean isFVRDisabled = false;

	Panel panel1 = null;
	VerticalLayout wizardLayout2 = null;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private Toolbar toolbar;
	
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
		PAPreauthEnhancementDataExtractionPage preauthDataExtractionPage = preauthDataExtractionPageInstance
				.get();
		preauthDataExtractionPage.init(this.bean, this.wizard);
		this.preauthDataExtractionPage = preauthDataExtractionPage;
		wizard.addStep(preauthDataExtractionPage, "Data Extraction");

		PAPreauthEnhancementPreviousClaimDetailsPage preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPageInstance
				.get();
		preauthPreviousClaimDetailsPage.init(this.bean);
		this.preauthPreviousClaimDetailsPage = preauthPreviousClaimDetailsPage;
		wizard.addStep(preauthPreviousClaimDetailsPage,
				"Previous Claim Details");

		PAPreauthEnhancementMedicalProcessingPage preauthMedicalProcessingPage = preauthMedicalProcessingPageInstance
				.get();
		preauthMedicalProcessingPage.init(this.bean, this.wizard);
		this.preauthMedicalProcessingPage = preauthMedicalProcessingPage;
		wizard.addStep(preauthMedicalProcessingPage, "Medical Processing");

		PAPreauthEnhancementMedicalDecisionPage preauthMedicalDecisionPage = PreauthMedicalDecisionPageInstance
				.get();
		preauthMedicalDecisionPage.init(this.bean, this.wizard);
		this.preauthMedicalDecisionPage = preauthMedicalDecisionPage;
		wizard.addStep(preauthMedicalDecisionPage, "Medical Decision");
		
		
		PAEnhancementDecisionCommunicationPage preauthDecisionCommunicationPage = preauthDecisionCommunicationPageInstance.get();
		preauthDecisionCommunicationPage.init(this.bean);
		this.preauthDecisionCommunicationPage = preauthDecisionCommunicationPage;
		wizard.addStep(preauthDecisionCommunicationPage,
				"Decision Communication");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);

		// Get Button Instace
		PAProcessPreAuthButtonLayout processPreAuthButtonLayout2 = processPreauthButtonLayoutInstance
				.get();
		this.processPreAuthButtonLayout = processPreAuthButtonLayout2;

		VerticalLayout wizardLayout = new VerticalLayout(wizard);

		PARevisedCarousel intimationDetailsCarousel = commonCarouselInstance
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
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.SETUP_REFERENCE_DATA,
				bean);
		
		if(bean.getTaskNumber() != null){
			/*String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}*/
		}
		
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

		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setReadOnly(true);
		typeFld.setNullRepresentation("");

		hLayout = new FormLayout(netWorkHospitalType, typeFld);

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
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),
				ViewLevels.PA_PROCESS, true,"Process Enhancements");
		viewDetails.setPreAuthKey(this.bean.getKey());
		viewDetails.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		viewDetailsForm.addComponent(viewDetails);
		
		HorizontalLayout showPremedicalStatusHLayout = showPremedicalStatus();
		// VerticalLayout wizardLayout1 = new VerticalLayout(hLayout ,
		// commonButtons(), showPremedicalStatusHLayout );
		
//		HorizontalLayout preauthViewLayout = viewPreAuthButton();
		
		HorizontalLayout commonButtons = commonButtons();
		HorizontalLayout topHorizontal = new HorizontalLayout(commonButtons,viewDetailsForm);
		topHorizontal.setWidth("100%");
		topHorizontal.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		topHorizontal.setComponentAlignment(commonButtons, Alignment.BOTTOM_LEFT)
		VerticalLayout wizardLayout1 = new VerticalLayout(topHorizontal,hLayout,showPremedicalStatusHLayout);
		
//		preauthViewLayout.setSpacing(true);
//		VerticalLayout wizardLayout1 = new VerticalLayout(preauthViewLayout,
//				commonButtons(), showPremedicalStatusHLayout);
		wizardLayout1.setSpacing(false);
		panel1 = new Panel();
		panel1.setContent(wizardLayout1);

//		panel1.setHeight("240px");

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
				PAPreauthEnhancemetWizardPresenter.PREAUTH_STEP_CHANGE_EVENT,
				event);

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT,
				this.bean);

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							// Confirmed to continue
							releaseHumanTask();
							fireViewEvent(MenuItemBean.PA_PROCESS_ENHANCEMENT,
									null);
						} else {
							// User did not confirm
						}
					}
				});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);

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
		firstForm.setHeight("50px");
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
//		txtClaimCount.addStyleName("fail");
//		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
//		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}
		
		
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
				if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
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
				if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
//				HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
				
				if(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					String url = bpmClientContext.getMediBuddyPackageDetails() + bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode();
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
		
		Button btnViewDoctorRemarks = new Button("Doctor Remarks");
		btnViewDoctorRemarks.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getDoctorRemarks(bean.getNewIntimationDTO().getIntimationId());
			}
			
		});
		
		FormLayout form2 = new FormLayout();
		form2.setWidth("50px");
		
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
		
		/**
		 * View Policy Schedule button added according to CR20181321
		 */
		Button btnViewPolicySchedule = new Button("View Policy Schedule");
		btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
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
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(claimCount,btnRRC,referToFLP,btnViewPolicySchedule,btnViewPackage,btnViewMBPackage);
		alignmentHLayout.setSpacing(true);
		
//		
		HorizontalLayout hlout = new HorizontalLayout(viewPreAuthButton(),btnViewDoctorRemarks);
		hlout.setSpacing(true);
		VerticalLayout vLayout = new VerticalLayout(alignmentHLayout , hlout);
		vLayout.setSpacing(true);
//		vLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
//		vLayout.setComponentAlignment(componentsHLayout, Alignment.TOP_LEFT);
//		vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_LEFT);
		
		 HorizontalLayout componentsHLayout = new HorizontalLayout(vLayout);
		/*HorizontalLayout alignmentHLayout = new HorizontalLayout(
				componentsHLayout);*/
	
		/*alignmentHLayout.setComponentAlignment(componentsHLayout,
				Alignment.MIDDLE_RIGHT);*/

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
	
	private void showErrorPopup(String eMsg) {
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
			};
*/
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
	  
	     public void getErrorMessage(String eMsg){
			
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
 }
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.VALIDATE_PREAUTH_ENHANCEMENT_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_PROCESS_ENHANCEMENT);
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
	public void buildSuccessLayout() {
		
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.PA_PROCESS_ENHANCEMENT, null);

			}
		});
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
	public void setInvestigationRule(Investigation checkInitiateInvestigation) {
		preauthMedicalDecisionPage
				.setInvestigationCheck(checkInitiateInvestigation != null ? true
						: false);
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
					PAPreauthEnhancemetWizardPresenter.PREAUTH_SUBMITTED_EVENT,
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
	 
	 
	 public void compareWithUserId(String userId) {

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
	
 }
	 
	@Override
	public void genertateFieldsBasedOnHospitalisionDueTo(
			SelectValue selectedValue) {
		preauthDataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo(selectedValue);
		
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
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {

		preauthDataExtractionPage.setCoverList(coverContainer);

	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		preauthDataExtractionPage.setSubCoverList(subCoverContainer);

	}
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
}
