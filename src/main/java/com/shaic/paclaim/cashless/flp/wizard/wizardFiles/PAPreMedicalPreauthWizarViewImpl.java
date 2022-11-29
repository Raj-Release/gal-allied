package com.shaic.paclaim.cashless.flp.wizard.wizardFiles;

import java.io.File;
import java.io.FileInputStream;
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
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.GWizardListener;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.HospitalService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.paclaim.cashless.flp.wizard.pages.PAPreMedicalDataExtractionPage;
import com.shaic.paclaim.cashless.flp.wizard.pages.PAPreMedicalPreAuthPreviousClaimsPage;
import com.shaic.paclaim.cashless.flp.wizard.pages.PAPreMedicalPreauthMedicalProcessingPage;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
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
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class PAPreMedicalPreauthWizarViewImpl extends AbstractMVPView implements
PAPreMedicalPreauthWizard, GWizardListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6595274879580589051L;
	
	@Inject
	private Instance<PAPreMedicalDataExtractionPage> preMedicalDataExtractionPageInstance;
	
	@EJB
	private HospitalService hospitalService;
	
	
	@Inject
	private Instance<PAPreMedicalPreAuthPreviousClaimsPage> preMedicalPreAuthPreviousClaimsPageInstance;
	
	@Inject
	private Instance<PAPreMedicalPreauthMedicalProcessingPage> preMedicalPreauthMedicalProcessingInstance;
	
	@Inject
	private Instance<PARevisedCarousel> commonCarouselInstance;
	
	
	@Inject
	private ViewDetails viewDetails;
	
	private PAPreMedicalDataExtractionPage preMedicalDataExtractionPage;
	
	private PAPreMedicalPreauthMedicalProcessingPage preMedicalPreauthMedicalProcessingPage;
	
	private PAPreMedicalPreAuthPreviousClaimsPage preMedicalPreAuthPreviousClaimsPage;
	
	//private VerticalLayout mainPanel;
	
	private VerticalSplitPanel mainPanel;
	
	private GWizard wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private Toolbar toolBar;
	
private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");
		item.addNestedProperty("preMedicalPreauthMedicalDecisionDetails");
		
		item.addNestedProperty("preauthDataExtractionDetails.referenceNo");
		item.addNestedProperty("preauthDataExtractionDetails.reasonForAdmission");
		item.addNestedProperty("preauthDataExtractionDetails.admissionDate");
		item.addNestedProperty("preauthDataExtractionDetails.noOfDays");
		item.addNestedProperty("preauthDataExtractionDetails.natureOfTreatment");
		item.addNestedProperty("preauthDataExtractionDetails.firstConsultantDate");
		item.addNestedProperty("preauthDataExtractionDetails.corpBuffer");
		item.addNestedProperty("preauthDataExtractionDetails.criticalIllness");
		item.addNestedProperty("preauthDataExtractionDetails.specifyIllness");
		item.addNestedProperty("preauthDataExtractionDetails.roomCategory");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentType");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentRemarks");
		item.addNestedProperty("preauthDataExtractionDetails.illness");
		item.addNestedProperty("preauthDataExtractionDetails.patientStatus");
		item.addNestedProperty("preauthDataExtractionDetails.deathDate");
		item.addNestedProperty("preauthDataExtractionDetails.reasonForDeath");
		item.addNestedProperty("preauthDataExtractionDetails.terminateCover");
		
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseOfIllness");
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseRemarks");
		item.addNestedProperty("preauthPreviousClaimsDetails.attachToPreviousClaim");
		
		item.addNestedProperty("preMedicalPreauthMedicalDecisionDetails.category");
		item.addNestedProperty("preMedicalPreauthMedicalDecisionDetails.queryRemarks");
		item.addNestedProperty("preMedicalPreauthMedicalDecisionDetails.rejectionRemarks");
		item.addNestedProperty("preMedicalPreauthMedicalDecisionDetails.medicalRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTaken");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistType");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistConsulted");
//		item.addNestedProperty("preauthMedicalDecisionDetails.remarksBySpecialist");
//		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrTriggerPoints");
//		
////		item.addNestedProperty("medicalDecisionDetails");
//		
//		item.addNestedProperty("preauthMedicalDecisionDetails.initialApprovedAmt");
//		item.addNestedProperty("preauthMedicalDecisionDetails.selectedCopay");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initialTotalApprovedAmt");
//		item.addNestedProperty("preauthMedicalDecisionDetails.approvalRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.queryRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionCategory");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForDenial");
//		item.addNestedProperty("preauthMedicalDecisionDetails.denialRemarks");		
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalateTo");
////		item.addNestedProperty("preauthMedicalDecisionDetails.upLoadFile");
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalationRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.typeOfCoordinatorRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForRefering");
		
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
	
	
	public void initView(PreauthDTO preauthDTO,Boolean processId) {
		this.bean = preauthDTO;
		this.wizard = new IWizard();
		initBinder();
		mainPanel = new VerticalSplitPanel();
//		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		this.preMedicalDataExtractionPage = preMedicalDataExtractionPageInstance.get();
		this.preMedicalDataExtractionPage.init(this.bean, wizard);
		wizard.addStep(this.preMedicalDataExtractionPage, "Data Extraction");
		
		PAPreMedicalPreAuthPreviousClaimsPage preMedicalPreAuthPreviousClaimsPage = preMedicalPreAuthPreviousClaimsPageInstance.get();
		preMedicalPreAuthPreviousClaimsPage.init(this.bean);
		this.preMedicalPreAuthPreviousClaimsPage = preMedicalPreAuthPreviousClaimsPage;
		wizard.addStep(preMedicalPreAuthPreviousClaimsPage, "Previous Claim Details");
		
		PAPreMedicalPreauthMedicalProcessingPage preMedicalPreauthMedicalProcessingPage = preMedicalPreauthMedicalProcessingInstance.get();
		preMedicalPreauthMedicalProcessingPage.init(this.bean);
		this.preMedicalPreauthMedicalProcessingPage = preMedicalPreauthMedicalProcessingPage;
		wizard.addStep(preMedicalPreauthMedicalProcessingPage, "Medical Processing");
//		
//		PreauthMedicalDecisionPage preauthMedicalDecisionPage = PreauthMedicalDecisionPageInstance.get();
//		preauthMedicalDecisionPage.init(this.bean,processId);
//		this.preauthMedicalDecisionPage = preauthMedicalDecisionPage;
//		wizard.addStep(preauthMedicalDecisionPage, "Medical Decision");
		
//		EnhancementDataExtraction enhancementDataExtractionPage=enhancementDataExtraction.get();
//		this.enhancementDataExtractionPage=enhancementDataExtractionPage;
//		enhancementDataExtractionPage.init(this.bean);
		
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
//		// Get Button Instace
//		ProcessPreAuthButtonLayout processPreAuthButtonLayout2 = processPreauthButtonLayoutInstance.get();
//		this.processPreAuthButtonLayout = processPreAuthButtonLayout2;

		VerticalLayout wizardLayout = new VerticalLayout(wizard);

		PARevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		//intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Pre-authorization");
		
		//intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Pre-medical Processing (Pre-auth)");
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "First Level Processing (Pre-auth)");
		intimationDetailsCarousel.init(this.bean,  "First Level Processing (Pre-auth)");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		TextField netWorkHospitalType = new TextField("Network Hosp Type");
		netWorkHospitalType.setValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());;
	 	//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
		netWorkHospitalType.setHeight("20px");
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		netWorkHospitalType.setReadOnly(true);
		netWorkHospitalType.setEnabled(false);
		netWorkHospitalType.setNullRepresentation("");
		
		//Added for Type box .
		/*ComboBox cmbTypeBox = new ComboBox("Type");
		cmbTypeBox.setContainerDataSource(masterService
				.getType(ReferenceTable.PROCESS_PRE_MEDICAL));
		cmbTypeBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTypeBox.setItemCaptionPropertyId("value");
		cmbTypeBox.setValue("Pre Auth(New)");*/
		TextField typeFld = new TextField("Type");
		typeFld.setValue("Pre Auth(New)");
		if(bean.getKey() != null) {
			if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)) {
				typeFld.setValue("Coordinator Reply Received");
			} else if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS)) {
				typeFld.setValue("Query Reply Received");
			}
		}
		
		typeFld.setReadOnly(true);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), ViewLevels.PA_PROCESS, false,"First Level Processing (Pre-auth)");
		viewDetails.setPreAuthKey(bean.getKey());
		viewDetails.setStageKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		
		viewDetailsForm.addComponent(viewDetails);
			
		FormLayout hLayout = new FormLayout (netWorkHospitalType,typeFld);
		hLayout.setMargin(false);
//		hLayout.setComponentAlignment(netWorkHospitalType, Alignment.MIDDLE_LEFT);
//		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);
		
	    HorizontalLayout commonButtons = new HorizontalLayout(commonButtons(),viewDetails);
	    commonButtons.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	    commonButtons.setWidth("100%");
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		if (null != memberType && !memberType.isEmpty()) {
			hLayout.addComponent(cmdClubMembership);
		}

		VerticalLayout mainHor = new VerticalLayout(commonButtons,hLayout);
		mainHor.setSpacing(false);
		mainHor.setMargin(false);
//		HorizontalLayout mainHorizontal= new HorizontalLayout(mainHor,dummyLabel,viewDetails);
//		mainHorizontal.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
//		mainHor.setComponentAlignment(commonButtons, Alignment.TOP_RIGHT);
//		mainHor.setComponentAlignment(hLayout, Alignment.TOP_LEFT);
//		mainHor.setWidth("100%");
		
//		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout , commonButtons());
		
		VerticalLayout wizardLayout1 = new VerticalLayout(mainHor);
		wizardLayout.setWidth("1000px");
		
		//HorizontalLayout wizardLayout1 = new HorizontalLayout(hLayout , commonButtons());
	//	wizardLayout1.setMargin(new MarginInfo(false, true, false, true));
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("110px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		//mainPanel.addComponent(panel1);
		 wizardLayout2.setSpacing(true);
		//VerticalLayout wizardLayout1 = new VerticalLayout(commonButtons(), wizard);

		mainPanel.setSecondComponent(wizardLayout2);
		//mainPanel.setSpacing(true);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		
		
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.SETUP_REFERENCE_DATA, bean);
		
		
		if(bean.getTaskNumber() != null){
			/*String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}*/
		}
		
		
		
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_STEP_CHANGE_EVENT, event);
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, this.bean);
		
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    // Confirmed to continue
		                	releaseHumanTask();
		                	fireViewEvent(MenuItemBean.PA_FIRST_LEVEL_PROCESSING_PREMEDICAL, null);
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
	public void wizardSave(GWizardEvent event) {
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, this.bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setWizardPageReferenceData(Map<String, Object> referenceData) {
		@SuppressWarnings("rawtypes")
		WizardStep currentStep = this.wizard.getCurrentStep();
		if (currentStep != null)
		{
			currentStep.setupReferences(referenceData);	
		}
		
	}

	@Override
	public void generateFieldsBasedOnTreatment() {
		preMedicalDataExtractionPage.generatedFieldsBasedOnTreatment();
		
	}

	@Override
	public void genertateFieldsBasedOnPatientStaus() {
		preMedicalDataExtractionPage.generateFieldsBasedOnPatientStatus();
		
	}

	@Override
	public void generateReferCoOrdinatorLayout() {
		// TODO Auto-generated method stub
		
	}
	
	
	private HorizontalLayout commonButtons() {
	//private AbsoluteLayout commonButtons() {
		
//		TextField txtClaimCount = new TextField("Claim Count");
//		txtClaimCount.setValue(this.bean.getClaimCount().toString());
//		txtClaimCount.setReadOnly(true);
//		TextField dummyField = new TextField();
//		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
//		dummyField.setReadOnly(true);
////		firstForm.setWidth(txtClaimCount.getWidth(), txtClaimCount.getWidthUnits());
//		Panel claimCount = new Panel(firstForm);
//		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		firstForm.setHeight("50px");
//		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
////		txtClaimCount.addStyleName("fail");
////		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
////		
//		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
//			claimCount.addStyleName("girdBorder1");
//		}else if(this.bean.getClaimCount() >2){
//			claimCount.addStyleName("girdBorder2");
//		}
		
		Button viewCoordinatorButton = new Button("View Co-ordinator Reply");
		
		viewCoordinatorButton.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					viewDetails.getTranslationMiscRequest(bean.getNewIntimationDTO().getIntimationId());
			}
		});
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), ViewLevels.PREAUTH_MEDICAL, false,"First Level Processing (Pre-auth)");
		viewDetails.setPreAuthKey(bean.getKey());
		viewDetails.setStageKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		
		viewDetailsForm.addComponent(viewDetails);
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
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
		
		
		
		
	//	viewDetailsForm.addComponent(viewDetailsSelect);
	//	Button goButton = new Button("GO");
		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);*/
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm);
//		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
		HorizontalLayout componentsHLayout = new HorizontalLayout(btnRRC,btnViewPackage,btnViewMBPackage);
		componentsHLayout.setSpacing(true);
		
//		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
//		rrcBtnLayout.setSpacing(true);
		
//		HorizontalLayout vLayout = new HorizontalLayout(componentsHLayout,viewDetailsForm);
//		vLayout.setSpacing(true);
//		vLayout.setComponentAlignment(horizontalLayout1, Alignment.TOP_RIGHT);
//		vLayout.setComponentAlignment(componentsHLayout, Alignment.TOP_LEFT);
//		vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_LEFT);
		
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
		
//		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
		
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
//		alignmentHLayout.setWidth("70%");
//		alignmentHLayout.setComponentAlignment(componentsHLayout, Alignment.MIDDLE_RIGHT);
		return componentsHLayout;

	}
	
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PAPreMedicalPreauthWizardPresenter.VALIDATE_PREMEDICAL_PREAUTH_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_PROCESS_PRE_MEDICAL);
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
	public void genertateFieldsBasedOnRelapseOfIllness(Map<String, Object> referenceData) {
		preMedicalPreAuthPreviousClaimsPage.generateFieldsBasedOnRelapseOfIllness(referenceData);
	}

	@Override
	public void generateFieldsOnQueryClick() {
		preMedicalPreauthMedicalProcessingPage.generateButtonFields("query");
	}

	@Override
	public void generateFieldsOnSuggestRejectionClick() {
		preMedicalPreauthMedicalProcessingPage.generateButtonFields("suggestion");
		
	}

	@Override
	public void generateFieldsOnSendForProcessingClick() {
		preMedicalPreauthMedicalProcessingPage.generateButtonFields("sendForProcessing");
	}

	@Override
	public void searchState(List<State> stateList) {
//		this.preMedicalDataExtractionPage.updateSateList(stateList);
	}

	@Override
	public void editSpecifyVisibility(Boolean checkValue) {
		this.preMedicalDataExtractionPage.editSpecifyVisible(checkValue);
		
	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		preMedicalDataExtractionPage.setIcdBlock(icdBlockContainer);
	}

	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		preMedicalDataExtractionPage.setIcdCode(icdCodeContainer);
		
	}
	
	@Override
	public void setRelapsedClaims(Map<String, Object> referenceData) {
		
		preMedicalPreAuthPreviousClaimsPage.setRelapsedClaims(referenceData);
	}

	@Override
	public void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimsDTO) {
		preMedicalPreAuthPreviousClaimsPage.setPreviousClaims(previousClaimsDTO);
		
	}

	@Override
	public void setPackageRate(Map<String, String> mappedValues) {
		preMedicalDataExtractionPage.setPackageRateForProcedure(mappedValues);
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
		preMedicalPreauthMedicalProcessingPage.setExclusionDetails(exclusionContainer);
	}

	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: green;'> Cashless Claim Record Saved Successfully. </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("First Level Pre-auth Home");
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				toolBar.countTool();
				fireViewEvent(MenuItemBean.PA_FIRST_LEVEL_PROCESSING_PREMEDICAL, null);
				
			}
		});

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

	@Override
	public void intiateCoordinatorRequest() {
		if(preMedicalDataExtractionPage.validatePage())
		{
			preMedicalDataExtractionPage.setTableValuesToDTO();
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_SUBMITTED_EVENT, this.bean);
		}
	}

	@Override
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		preMedicalDataExtractionPage.setHospitalizationDetails(hospitalizationDetails);
		
	}

	@Override
	public void setCustomDiagValueToContainer(SelectValue selValue,ComboBox cmbBox) {
		preMedicalDataExtractionPage.setCustomDiagValue(selValue,cmbBox);
	}
	/*@Override
	public void setCustomDiagValueToContainer(BeanItemContainer<SelectValue> selectValue,ComboBox cmbBox,Long key) {
		preMedicalDataExtractionPage.setCustomDiagValue(selectValue,cmbBox,key);
	}*/

	@Override
	public void genertateFieldsBasedOnHospitalisionDueTo(
			SelectValue selectedValue) {
		preMedicalDataExtractionPage.generatedFieldsBasedOnHospitalisationDueTo(selectedValue);
	}

	@Override
	public void genertateFieldsOnReportedPolice(Boolean selectedValue) {
		preMedicalDataExtractionPage.generatedFieldsBasedOnReportedToPolice(selectedValue);
		
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
					fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL, null);
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+ userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
	
 }

	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		
		preMedicalDataExtractionPage.setCoverList(coverContainer);
		
	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		
		preMedicalDataExtractionPage.setSubCoverList(subCoverContainer);
		
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
