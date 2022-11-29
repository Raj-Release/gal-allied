package com.shaic.paclaim.cashless.enhancement.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.ViewClaimAmountDetils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.MedicalDecisionFVRGrading;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancementButtons;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.paclaim.cashless.listenertables.PARevisedMedicalDecisionTable;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAPreauthEnhancementMedicalDecisionPage extends ViewComponent
		implements WizardStep<PreauthDTO> {

	private static final long serialVersionUID = 2513804939024585497L;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	@Inject
	private Instance<PARevisedMedicalDecisionTable> medicalDecisionTable;
	
	@Inject
	private Instance<MedicalDecisionFVRGrading> fvrGradingTableInstance;
	
	private MedicalDecisionFVRGrading fvrGradingTableObj;

	@Inject
	private Instance<PAPreauthEnhancementButtons> processPreauthButtonLayout;

	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;

	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;

	private PAPreauthEnhancementButtons processPreauthButtonObj;

	@Inject
	private Instance<PAAmountConsideredTable> amountConsideredTableInstance;

	private PAAmountConsideredTable amountConsideredTable;

	private PARevisedMedicalDecisionTable medicalDecisionTableObj;

	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;

	private OptionGroup initiateFieldVisitRequestRadio;

	private ComboBox cmbFVRNotRequiredRemarks;

	private OptionGroup specialistOpinionTaken;

	private ComboBox cmbSpecialistType;

	private ComboBox cmbSpecialistConsulted;

	private CheckBox investigationReportReviewedChk;

	private ComboBox investigatorName;

	private TextArea investigationReviewRemarks;

	private TextArea txtRemarksBySpecialist;

	private ComboBox cmbAllocationTo;
	
	private ComboBox cmbFvrAssignTo;
	
	private ComboBox cmbFvrPriority;

	private TextArea fvrTriggerPoints;
	
	private Button btnViewFVRDetails;

	private Label lblNoOfFvr;

	private TextArea txtMedicalRemarks;

	private TextArea txtDoctorNote;

	private TextField approvedAmtField;

	private VerticalLayout wholeVLayout;

	private FormLayout investigationFLayout;

	private FormLayout fvrFieldFLayout;
	
	private HorizontalLayout fvrHorizontalLayout;

	private FormLayout specialistFLayout;

	private Label preauthAmtRequested;

	private Button showclaimAmtDetailsBtn;

	private Button showclaimAmtDetailsBtnDuplicate;

	private Label amountConsidered;

	private Label balanceSumInsured;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	Map<String, Object> referenceData = new HashMap<String, Object>();

	Map<String, Object> sublimitCalculatedValues;

	private Double balanceSumInsuredValue;

	private String diagnosisName;

	private Button initiatePEDButton;

	//private static Window popup;

	public Double value;

	private String preAuthRequestedAmt;

	// Added for intiate field visit.

	private Button initiateFieldVisitButton;

	private Button btnViewSpecialistOpinion;

	private TextField fvrSequence;
	private TextField representativeCode;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;

	private TextField consideredAmtField;

	

	private boolean isDisabled = false;

	private Button amountConsideredViewButton;
	
	private GWizard wizard;

	private VerticalLayout optionCLayout;

	private TextField nonAllopathicTxt;
	
	private OptionGroup negotiationOpinionTaken;	
	private TextArea txtNegotiationRemarks;	
	private FormLayout negotiationLayout;
	

	private ComboBox cmbBehaviourHosp;
	
	private FormLayout fBehaviourHospFLayout;

	private CheckBox chkBehavHosColOvrPack;

	private CheckBox chkBehaviourHospMbAgdNtAply;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;

	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthMedicalDecisionDetails());
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@Override
	public String getCaption() {
		return "Medical Decision";
	}

	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		initiatePEDButton = new Button("Initiate PED Endorsement");
		initiatePEDButton.setEnabled(false);
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setComponentAlignment(initiatePEDButton,
				Alignment.MIDDLE_RIGHT);
		buttonHLayout.setEnabled(false);
		HorizontalLayout buttonInvestigationLayout = null;
		initiateFieldVisitButton = new Button("Initiate Investigation");
		buttonInvestigationLayout = new HorizontalLayout(initiateFieldVisitButton);
		buttonInvestigationLayout.setComponentAlignment(initiateFieldVisitButton,
				Alignment.MIDDLE_RIGHT);
		
		btnViewSpecialistOpinion = new Button("View Specialist Opinion");
		HorizontalLayout buttonLayout = new HorizontalLayout(btnViewSpecialistOpinion);
		buttonLayout.setComponentAlignment(btnViewSpecialistOpinion,
				Alignment.MIDDLE_RIGHT);

		consideredAmtField = new TextField("Amount Considered");
		consideredAmtField.setReadOnly(false);
		consideredAmtField.setValue(this.bean.getAmountConsidered());
		consideredAmtField.setReadOnly(true);

		FormLayout considerAmtForm = new FormLayout(consideredAmtField);

		// Fix for issue 756 -- starts
		/*
		 * if(("F").equals(this.bean.getPreauthDataExtractionDetails().
		 * getInterimOrFinalEnhancementFlag())) { isDisabled = true; }
		 */
		// Fix for issue 756 -- ends

		initiateFieldVisitRequestRadio = (OptionGroup) binder.buildAndBind(
				"Initiate Field Visit Request",
				"initiateFieldVisitRequestFlag", OptionGroup.class);

		txtMedicalRemarks = (TextArea) binder.buildAndBind("Medical Remarks",
				"medicalRemarks", TextArea.class);
		txtMedicalRemarks.setMaxLength(4000);
		
		txtDoctorNote = (TextArea) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextArea.class);

		txtDoctorNote.setMaxLength(4000);
		initiateFieldVisitRequestRadio.addItems(getReadioButtonOptions());
		initiateFieldVisitRequestRadio.setItemCaption(true, "Yes");
		initiateFieldVisitRequestRadio.setItemCaption(false, "No");
		initiateFieldVisitRequestRadio.setStyleName("horizontal");
		
		cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
				"FVR Not Required Remarks", "fvrNotRequiredRemarks",
				ComboBox.class);
		specialistOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Specialist Opinion Taken", "specialistOpinionTakenFlag",
				OptionGroup.class);
		specialistOpinionTaken.addItems(getReadioButtonOptions());
		specialistOpinionTaken.setItemCaption(true, "Yes");
		specialistOpinionTaken.setItemCaption(false, "No");
		specialistOpinionTaken.setStyleName("horizontal");
		specialistOpinionTaken.select(false);
	

		fvrSequence = (TextField) binder.buildAndBind("FVR Sequence",
				"fvrSequence", TextField.class);
		fvrSequence.setEnabled(false);
		fvrSequence.setReadOnly(true);
		representativeCode = (TextField) binder.buildAndBind(
				"Representative Code", "representativeCode", TextField.class);
		representativeCode.setEnabled(false);
		representativeCode.setReadOnly(true);

		FormLayout fvrFormlayout = new FormLayout(fvrSequence);
		FormLayout representativeCodeFormLayout = new FormLayout(
				representativeCode);
		HorizontalLayout fvrSeqLayout = new HorizontalLayout(fvrFormlayout,
				new Label(""), new Label(""), representativeCodeFormLayout);
		fvrSeqLayout.setWidth("100%");
		fvrSeqLayout.setSpacing(true);
		fvrSeqLayout
				.setComponentAlignment(fvrFormlayout, Alignment.MIDDLE_LEFT);
		fvrSeqLayout.setComponentAlignment(representativeCodeFormLayout,
				Alignment.MIDDLE_RIGHT);
		
		// FormLayout fvrSeqLayout = new FormLayout(fvrSequence);
		// HorizontalLayout fvrLayout = new HorizontalLayout(fvrSeqLayout);
		// FormLayout fvrRepLayout = new FormLayout(representativeCode);
		// HorizontalLayout fvrLayout = new
		// HorizontalLayout(fvrSeqLayout,fvrRepLayout);
		// fvrLayout.setComponentAlignment(fvrRepLayout,
		// Alignment.MIDDLE_RIGHT);

		
		/*
		 * HorizontalLayout fvrGradingLayout = new
		 * HorizontalLayout(specialistFLayout, investigationFLayout);
		 * fvrGradingLayout.setWidth("100%"); fvrGradingLayout.setSpacing(true);
		 */

		investigationReportReviewedChk = (CheckBox) binder.buildAndBind(
				"Investigation Report Reviewed", "investigationReportReviewed",
				CheckBox.class);
		investigationReportReviewedChk.setEnabled(false);
		investigatorName = (ComboBox) binder.buildAndBind("Investigator Name",
				"investigatorName", ComboBox.class);
		investigatorName.setEnabled(false);
		investigationReviewRemarks = (TextArea) binder.buildAndBind(
				"Investigation Review Remarks", "investigationReviewRemarks",
				TextArea.class);
		investigationReviewRemarks.setEnabled(false);

		fvrFieldFLayout = new FormLayout(initiateFieldVisitRequestRadio,
				cmbFVRNotRequiredRemarks);
		fvrFieldFLayout.setMargin(false);
		fvrHorizontalLayout = new HorizontalLayout(fvrFieldFLayout);
		if(null !=this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
			initiateFieldVisitRequestRadio.select(this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag());
			Boolean isCheck=true;
			if(!this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
				isCheck=false;
			}
			
			fireViewEvent(
					PAPreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
					isCheck, bean.getNewIntimationDTO()
							.getKey());
		}else{
		initiateFieldVisitRequestRadio.select(false);
		}
		if (("F").equals(this.bean.getPreauthDataExtractionDetails()
				.getInterimOrFinalEnhancementFlag())) {
			isDisabled = true;
			initiateFieldVisitRequestRadio.setEnabled(false);
		} else if (("I").equals(this.bean.getPreauthDataExtractionDetails()
				.getInterimOrFinalEnhancementFlag())) {
			isDisabled = false;
			initiateFieldVisitRequestRadio.setEnabled(true);
		}
	
		specialistFLayout = new FormLayout(specialistOpinionTaken);
		specialistFLayout.setMargin(false);
		investigationFLayout = new FormLayout(investigationReportReviewedChk,
				investigatorName, investigationReviewRemarks);

		HorizontalLayout specialistHLayout = new HorizontalLayout(
				specialistFLayout, investigationFLayout);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);

		HorizontalLayout formsHLayout = new HorizontalLayout(fvrHorizontalLayout);
		formsHLayout.setWidth("100%");
		formsHLayout.setSpacing(true);

		/*negotiationLayout = new FormLayout();
		negotiationOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Do you want to Refer for Negotiation", "negotiationDecisionTaken",
				OptionGroup.class);
		negotiationOpinionTaken.addItems(getReadioButtonOptions());
		negotiationOpinionTaken.setItemCaption(true, "Yes");
		negotiationOpinionTaken.setItemCaption(false, "No");
		negotiationOpinionTaken.setStyleName("horizontal");
	//	negotiationOpinionTaken.select(false);
		negotiationOpinionTaken.setVisible(false);
		
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		negotiationLayout.addComponent(negotiationOpinionTaken);
		negotiationLayout.setMargin(false);
		negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			fireViewEvent(
					PAPreauthEnhancemetWizardPresenter.NEGOTIATION_OPINION_RADIO_CHANGED,true);
		}
		
		
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				negotiationOpinionTaken.setEnabled(false);
				negotiationOpinionTaken.setValue(true);
				negotiationOpinionTaken.setVisible(true);
			}
			else
			{
				negotiationOpinionTaken.setVisible(true);
				negotiationOpinionTaken.setEnabled(true);
			}
		}*/
		
		HorizontalLayout remarksHLayout = new HorizontalLayout(new FormLayout(
				txtMedicalRemarks), new FormLayout(txtDoctorNote));
		remarksHLayout.setWidth("100%");

		processPreauthButtonObj = processPreauthButtonLayout.get();

		processPreauthButtonObj.initView(this.bean,this.wizard);
		// HorizontalLayout buttonsHLayout = new
		// HorizontalLayout(processPreauthButtonObj);
		Label dummyLabel = new Label("");
		dummyLabel.setWidth("10%");
		HorizontalLayout buttonWholeLayout = new HorizontalLayout(dummyLabel,processPreauthButtonObj);
		buttonWholeLayout.setWidth("10%");
		buttonWholeLayout.setComponentAlignment(processPreauthButtonObj,
				Alignment.MIDDLE_LEFT);

		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("", false, false);

		// Table nestedTable = new Table();
		// nestedTable.setWidth("100%");
		//
		// nestedTable.setCaption("");
		// nestedTable.addContainerProperty("Reference No", String.class,
		// this.bean.getPreauthDataExtractionDetails().getReferenceNo());
		// nestedTable.addContainerProperty("Treatment Type", String.class,
		// this.bean.getPreauthDataExtractionDetails().getTreatmentType().getValue());
		// nestedTable.addContainerProperty("Table", MedicalDecisionTable.class,
		// null);
		// nestedTable.addItem(new Object[] {
		// this.bean.getPreauthDataExtractionDetails().getReferenceNo(),
		// this.bean.getPreauthDataExtractionDetails().getReferenceNo(),
		// this.medicalDecisionTableObj }, 1);
		// nestedTable.setPageLength(nestedTable.getItemIds().size());

		// preauthAmtRequested = new Label("Pre-auth Requested Amount");
		// preauthAmtRequested.setCaption("Pre-auth Requested Amount");
		// preauthAmtRequested.setValue(this.bean.getAmountRequested());

		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
		amountConsidered.setValue(this.bean.getAmountConsidered());

		amountConsideredViewButton = new Button("View");
		amountConsideredViewButton.setStyleName("link");
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered), new FormLayout(amountConsideredViewButton));
		amountConsideredLayout.setMargin(false);
		// balanceSumInsured = new Label("Availabel Sum Insured");
		// balanceSumInsured.setCaption("Balance Sum Insured");

		// This is used to set the balance SumInsured from DB.
		// fireViewEvent(PreauthEnhancemetWizardPresenter.BALANCE_SUM_INSURED,
		// this.bean.getPolicyDto());
		/**
		 * Balance SI procedure requires insured key as one of the parameter for
		 * calculation. Insured key will be available in new intimation dto and
		 * not in policy dto. Hence commented above code and added below one.
		 * */
		fireViewEvent(PAPreauthEnhancemetWizardPresenter.BALANCE_SUM_INSURED,
				this.bean);

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);

		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction </br> Amount",
				ContentMode.HTML), approvedAmtField);
		
		optionCLayout = new VerticalLayout(approvedFormLayout);

		showclaimAmtDetailsBtn = new Button("View");
		showclaimAmtDetailsBtnDuplicate = new Button("View");
		showclaimAmtDetailsBtn.setStyleName("link");
		showclaimAmtDetailsBtnDuplicate.setStyleName("link");
		// HorizontalLayout amountConsideredLayout = new HorizontalLayout(new
		// FormLayout(amountConsidered), showclaimAmtDetailsBtnDuplicate);
		// VerticalLayout verticalLayout = new
		// VerticalLayout(amountConsideredLayout, new
		// FormLayout(balanceSumInsured));
		// HorizontalLayout wholeAmtLayout = new HorizontalLayout(new
		// Label(""),new Label(""),new Label(""),verticalLayout);
		// wholeAmtLayout.setSpacing(true);
		// wholeAmtLayout.setWidth("100%");
		// wholeAmtLayout.setComponentAlignment(verticalLayout,
		// Alignment.MIDDLE_RIGHT);

		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showclaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showclaimAmtDetailsBtn);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);

		// wholeVLayout = new VerticalLayout(buttonHLayout,
		// buttonLayout,this.previousPreAuthDetailsTableObj,
		// this.medicalDecisionTableObj, wholeAmtLayout, formsHLayout,
		// specialistHLayout, buttonsHLayout, remarksHLayout);
		
		
		if(! this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()) {
			fvrGradingTableObj = fvrGradingTableInstance.get();
			fvrGradingTableObj.initView(bean, true);
		}
		VerticalLayout fvrVLayout = new VerticalLayout();
		if(fvrGradingTableObj != null) {
			fvrVLayout.addComponent(fvrGradingTableObj);
		}
		

		HorizontalLayout hospBehaviour = new HorizontalLayout(getBehaviouroHospital());
		hospBehaviour.setSpacing(true);


		wholeVLayout = new VerticalLayout(buttonHLayout, buttonInvestigationLayout, buttonLayout,
				this.previousPreAuthDetailsTableObj, amountConsideredLayout,
				this.medicalDecisionTableObj,hospBehaviour, this.amountConsideredTable,
				formsHLayout, fvrVLayout,
				specialistHLayout,/*negotiationLayout,*/ buttonWholeLayout, remarksHLayout);

		wholeVLayout
				.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout
		.setComponentAlignment(buttonInvestigationLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setComponentAlignment(buttonHLayout,
				Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);

		mandatoryFields.add(cmbFVRNotRequiredRemarks);
//		mandatoryFields.add(txtMedicalRemarks);
		// mandatoryFields.add(investigatorName);
		mandatoryFields.add(investigationReviewRemarks);
		showOrHideValidation(false);
		addListener();
		addPEDListener();

		addMedicalDecisionTableFooterListener();
		
		generateFieldBasedOnStatusKey();

		return wholeVLayout;
	}

	public void addPEDListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey = bean.getKey();
				Long intimationKey = bean.getIntimationKey();
				Long policyKey = bean.getPolicyKey();
				Long claimKey = bean.getClaimKey();
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}
				}
		});
	}

	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);						
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	

	
	
public Boolean alertMessageForPEDInitiate(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}

		public Boolean alertMessageForUniquePremium() {
			Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.UNIQUE_PREMIUM_EXCEEDING_MESSAGE+ "</b>",
				ContentMode.HTML);
			final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		//	dialog.setCaption("Alert");
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
			return true;
		}

	protected void addListener() {

		
		
		txtDoctorNote.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		        // Assuming that the value type is a String
		        String value = (String) event.getProperty().getValue();

		        txtDoctorNote.setDescription(value);
		    }
		});
		// Fix for issue 756 -- starts
		if (null != initiateFieldVisitButton) {
			initiateFieldVisitButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Long preauthKey = bean.getKey();
					Long intimationKey = bean.getIntimationKey();
					Long policyKey = bean.getPolicyKey();
					Long claimKey = bean.getClaimKey();
					
					viewDetails.setPreAuthKey(preauthKey);
					viewDetails.getViewInvestigationDetailsForPreauth(
							claimKey, true,bean.getStrUserName(),bean.getStrPassword(),bean);
					
					// viewDetails.getViewInvestigationDetails(bean.getKey());
				}
			});
		}
		if (null != btnViewSpecialistOpinion) {
			btnViewSpecialistOpinion.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (bean != null
							&& bean.getNewIntimationDTO() != null
							&& bean.getNewIntimationDTO().getIntimationId() != null) {
						viewDetails.getSpecialityOpinion(bean
								.getNewIntimationDTO().getIntimationId());
					}
				}
			});
		}
		// Fix for issue 756 -- ends
		initiateFieldVisitRequestRadio
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						}
						if (bean != null && bean.getNewIntimationDTO() != null
								&& bean.getNewIntimationDTO().getKey() != null) {
							fireViewEvent(
									PAPreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
									isChecked, bean.getNewIntimationDTO()
											.getKey());
						}					
					}
				});
		

		specialistOpinionTaken
				.addValueChangeListener(new Property.ValueChangeListener() {

					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						}

						fireViewEvent(
								PAPreauthEnhancemetWizardPresenter.SPECIALIST_OPINION_RADIO_CHANGED,
								isChecked);
					}
				});

		showclaimAmtDetailsBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null){
					fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.VIEW_BALANCE_SUM_INSURED_ENH_DETAILS,
							bean.getNewIntimationDTO().getIntimationId());
				}
			}
		});

		showclaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1159870471084252041L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						PAPreauthEnhancemetWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});

		amountConsideredViewButton.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1159870471084252041L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						PAPreauthEnhancemetWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
	}
	
   public void generateFieldBasedOnStatusKey(){
	   
	   
		if(bean.getStatusKey() != null){
			if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
				
				 fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
				
			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);
				
			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_REJECTION_EVENT,null);		
				
			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_ESCALATE_EVENT, null);
				
			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_REFERCOORDINATOR_EVENT,null);
				
			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT,null);
				
			} else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_AND_REJECT_EVENT,null);
				
			}
			else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
				
//				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);
				
				
			}
		
		}
		
		
		
	 }
	
	private void createNonAllopathicFields(Double originalAmt, Double utilizedAmt) {
		nonAllopathicTxt = new TextField("Non-Allopathic Avail Amt");
		nonAllopathicTxt.setWidth("80px");
		Double availAmt = originalAmt - utilizedAmt;
		nonAllopathicTxt.setValue(String.valueOf(availAmt.intValue()) );
		nonAllopathicTxt.setEnabled(false);
		bean.setNonAllopathicAvailAmt(availAmt.intValue());
		bean.setNonAllopathicOriginalAmt(originalAmt);
		bean.setNonAllopathicUtilizedAmt(utilizedAmt);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, originalAmt);
		values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilizedAmt);
		Button viewBtn = new Button("View");
		viewBtn.setData(values);
		viewBtn.setStyleName("link");
		viewBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 9127517383717464157L;

			@Override
			public void buttonClick(ClickEvent event) {
				Map<String, Object> values = (Map<String, Object>) event.getButton().getData();
				
				TextField originalAmt = new TextField("Total Original Amt");
				originalAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT)).intValue()));
				originalAmt.setReadOnly(true);
				originalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField utilizedAmt = new TextField("Utilized Amt");
				utilizedAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT)).intValue()));
				utilizedAmt.setReadOnly(true);
				utilizedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField availableAmt = new TextField("Available Amt");
				Double availAmt = (Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) - (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
				availableAmt.setValue(String.valueOf(availAmt.intValue()) );
				availableAmt.setReadOnly(true);
				availableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Non-Allopathic Details");
				dialog.setClosable(true);
				dialog.setWidth("400px");
				dialog.setResizable(false);
				dialog.setContent(new FormLayout(originalAmt, utilizedAmt, availableAmt));
				dialog.show(getUI().getCurrent(), null, true);
			}
		});
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(nonAllopathicTxt), viewBtn);
		horizontalLayout.setSpacing(true);
		optionCLayout.addComponent(horizontalLayout);
		optionCLayout.setSpacing(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
				.get("fvrNotRequiredRemarks");

		BeanItemContainer<SelectValue> investigatorNameContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("investigatorName");
		
		BeanItemContainer<SelectValue> behaviourHosCombValue = (BeanItemContainer<SelectValue>) referenceData
				.get("behaviourHosCombValue");
		
		this.medicalDecisionTableObj.setReferenceData(referenceData);
		
		cmbFVRNotRequiredRemarks.setContainerDataSource(fvrNotRequiredRemarks);
		cmbFVRNotRequiredRemarks.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");

		
		if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
			this.cmbFVRNotRequiredRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
		}
		
		
		investigatorName.setContainerDataSource(investigatorNameContainer);
		investigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		investigatorName.setItemCaptionPropertyId("investigatorName");
		
		if (this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null) {
			this.investigatorName.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getInvestigatorName());
		}

		// this.medicalDecisionTableObj.setReference(this.referenceData);
		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
				.getPreauthDataExtractionDetails().getDiagnosisTableList();
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
		if(filledDTO.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				addDiagnosisValuesToMedicalDecision(medicalDecisionDTOList, pedValidationTableDTO, false, false);
			}
			
			if(!this.bean.getDeletedDiagnosis().isEmpty()) {
				List<DiagnosisDetailsTableDTO> deletedDiagnosis = this.bean.getDeletedDiagnosis();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					addDiagnosisValuesToMedicalDecision(medicalDecisionDTOList, diagnosisDetailsTableDTO, true, true);
				}
			}
			
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, false, false);
			}
			
			List<ProcedureDTO> deletedProcedureList = bean.getDeletedProcedure();
			if(!deletedProcedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : deletedProcedureList) {
					addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, true, true);
				}
			}

			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean.getPolicyDto()
					.getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			// caluculationInputValues.put("policySumInsured",
			// this.bean.getPolicyDto().getInsuredSumInsured() != 0 ?
			// this.bean.getPolicyDto().getInsuredSumInsured().toString() :
			// String.valueOf(this.bean.getPolicyDto().getTotalSumInsured()));
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			 Double insuredSumInsured = 0d;
				if(null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() && 
						null != this.bean.getNewIntimationDTO().getPolicy().getProduct() && 
						null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
						!(ReferenceTable.getGPAProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
					insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					this.bean.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), this.bean.getPolicyDto()
							.getKey(),this.bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
				}
				else
				{
					insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
							this.bean.getNewIntimationDTO().getInsuredPatient()
									.getInsuredId().toString(), this.bean.getPolicyDto()
									.getKey());
				}
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			int medicalDecisionSize = 0;
			
			for (DiagnosisProcedureTableDTO medicalDecisionDto : medicalDecisionDTOList) {
				if(! medicalDecisionDto.getIsDeletedOne()){
					medicalDecisionSize++;
				}
			}

			// for (MedicalDecisionTableDTO medicalDecisionDto :
			// medicalDecisionDTOList) {
			if (null != medicalDecisionDTOList && medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag " + (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? "0"
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId()
												.toString());
						caluculationInputValues.put("referenceFlag", "D");
						
						
						
						
						
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc " + (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId", (medicalDecisionDto.getProcedureDTO().getProcedureName() == null  || medicalDecisionDto.getProcedureDTO().getNewProcedureFlag() == 1)? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId());
						caluculationInputValues.put("referenceFlag", "P");
						caluculationInputValues
								.put("preauthKey",
										(medicalDecisionDto.getProcedureDTO()
												.getNewProcedureFlag() != null && medicalDecisionDto
												.getProcedureDTO()
												.getNewProcedureFlag() == 0) ? this.bean
												.getPreviousPreauthKey() : 0l);
					}
					
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));

					fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,bean);
					Map<String, Object> values = this.sublimitCalculatedValues;
					
					if(bean.getIsNonAllopathic()) {
						bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
						bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
					}

					medicalDecisionDto
							.setRestrictionSI(caluculationInputValues
									.get("restrictedSIAmount") != null ? SHAUtils
									.getIntegerFromString(
											(String) caluculationInputValues
													.get("restrictedSIAmount"))
									.toString() : "NA");
					medicalDecisionDto.setAvailableAmout(((Double) values
							.get("restrictedAvailAmt")).intValue());
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto
							.setSubLimitAmount(((Double) values.get("currentSL"))
									.intValue() > 0 ? (String
									.valueOf(((Double) values.get("currentSL"))
											.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO().setDiagnosis(
								this.diagnosisName);
					}

					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
						Integer subLimitAvaliableAmt = 0;
						Boolean isResidual = false;
						if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else {
							isResidual = true;
						}
						
						if(!isResidual && bean.getEntitlmentNoOfDays() != null) {
							Float floatAvailAmt = bean.getEntitlmentNoOfDays() * subLimitAvaliableAmt;
							Integer availAmt = Math.round(floatAvailAmt);
							int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
							medicalDecisionDto.setSubLimitAvaliableAmt(min);
							medicalDecisionDto.setSubLimitUtilAmount(0);
						}
					}
					
					if(this.bean.getIsAmbulanceApplicable()){
						medicalDecisionDto.setIsAmbulanceEnable(this.bean.getIsAmbulanceApplicable());
					}
					

					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}else{
								medicalDecisionDto.setIsAmbulanceEnable(false);
								medicalDecisionDto.setIsAmbChargeApplicable(false);
							}
						}
						
					}
					
					// need to implement in new medical listener table
					this.medicalDecisionTableObj.addBeanToList(medicalDecisionDto);
					
				}
			}
			// DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
			// dto.setReferenceNo("Residual Treatment / Procedure Amount");
			// dto.setApprovedAmount(this.bean.getResidualAmountDTO().getApprovedAmount()
			// != null ?
			// this.bean.getResidualAmountDTO().getApprovedAmount().toString() :
			// "");
			// this.medicalDecisionTableObj.addBeanToList(dto);

			DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
			dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
			dto.setPackageAmt("NA");
			//GLX2020047
			dto.setAgreedPackageAmt("NA");
			dto.setSubLimitAmount("NA");
			dto.setRestrictionSI("NA");
			dto.setIsAmbulanceEnable(false);
			this.medicalDecisionTableObj.addBeanToList(dto);
			
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
			
		} else {
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));
			caluculationInputValues.put("preauthKey", 0l);
			caluculationInputValues.put("preauthKey",this.bean.getPreviousPreauthKey());
			caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
			caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
			caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
			
			bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
			
			int size = filledDTO.size();
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
					caluculationInputValues
					.put("preauthKey",
							(diagnosisProcedureTableDTO.getProcedureDTO()
									.getNewProcedureFlag() != null && diagnosisProcedureTableDTO
									.getProcedureDTO()
									.getNewProcedureFlag() == 0) ? this.bean
									.getPreviousPreauthKey() : 0l);
					SHAUtils.fillDetailsForUtilForProcedure(caluculationInputValues, diagnosisProcedureTableDTO);
					
					if(size == 2){
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								diagnosisProcedureTableDTO.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(true);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(true);
							}else{
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
							}
						}
						
					}

				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
					SHAUtils.fillDetailsForUtilizationForDiag(caluculationInputValues, diagnosisProcedureTableDTO);
					
					if(size == 2){
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								diagnosisProcedureTableDTO.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(true);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(true);
							}else{
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
							}
						}
						
					}
					
				}
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null || diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					fireViewEvent(
							PAPreauthEnhancemetWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,this.bean);
					Map<String, Object> values = this.sublimitCalculatedValues;
					setAppropriateValues(caluculationInputValues, diagnosisProcedureTableDTO, values);
				}
			
			}
			
			this.medicalDecisionTableObj.addList(filledDTO);
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
		
		
		List<PreviousPreAuthTableDTO> previousPreauthList = (List<PreviousPreAuthTableDTO>) referenceData
				.get("previousPreauth");
		
		if(previousPreauthList != null && ! previousPreauthList.isEmpty()){
			this.bean.setPreviousPreauthTableDTO(previousPreauthList);
		}
		
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
			if (!(this.bean.getKey() != null && this.bean.getKey().equals(
					previousPreAuthTableDTO.getKey()))) {
				fireViewEvent(
						PAPreauthEnhancemetWizardPresenter.GET_PREAUTH_REQUESTED_AMOUT,
						previousPreAuthTableDTO);
				previousPreAuthTableDTO.setRequestedAmt(preAuthRequestedAmt);
				newList.add(previousPreAuthTableDTO);
				
			}
		}
		this.previousPreAuthDetailsTableObj.setTableList(newList);
		
		List<Long> keyList = new ArrayList<Long>();
		for (DiagnosisProcedureTableDTO medicalDecisionDto : medicalDecisionDTOList) {
			if(medicalDecisionDto.getIsDeletedOne()) {
				if(medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
					keyList.add(medicalDecisionDto.getDiagnosisDetailsDTO().getKey());
				} else if(medicalDecisionDto.getProcedureDTO() != null) {
					keyList.add(medicalDecisionDto.getProcedureDTO().getKey());
				}
			}
			
		}
		this.medicalDecisionTableObj.setRowColor(keyList, true);
		
		if(fvrGradingTableObj != null) {
			fvrGradingTableObj.setupReferences(referenceData);
		}
		if(this.bean.getIsReverseAllocation()) {
			createReverseRelatedFields();
		}
		

		cmbBehaviourHosp.setContainerDataSource(behaviourHosCombValue);
		cmbBehaviourHosp.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbBehaviourHosp.setItemCaptionPropertyId("value");
		
	
		if (this.bean.getPreauthMedicalDecisionDetails().getBehaviourHospValue() != null) {
			this.cmbBehaviourHosp.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getBehaviourHospValue());
		}
		
		if(this.bean.getPreauthMedicalDecisionDetails().getHospCollectOvrAmtChkBx() != null 
				&& this.bean.getPreauthMedicalDecisionDetails().getHospCollectOvrAmtChkBx().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			chkBehavHosColOvrPack.setValue(true);
		}
		
		if(this.bean.getPreauthMedicalDecisionDetails().getMbAgrdDisNtAplyChkBx() != null 
				&& this.bean.getPreauthMedicalDecisionDetails().getMbAgrdDisNtAplyChkBx().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			chkBehaviourHospMbAgdNtAply.setValue(true);
			
		}
		
		if(this.bean.getPreauthMedicalDecisionDetails().getBehaviourHosp1chkbox() != null && this.bean.getPreauthMedicalDecisionDetails().getBehaviourHosp1chkbox()){
			chkBehavHosColOvrPack.setValue(true);
		}
		if(this.bean.getPreauthMedicalDecisionDetails().getBehaviourHospMbagreedchkbox() != null && this.bean.getPreauthMedicalDecisionDetails().getBehaviourHospMbagreedchkbox()){
			chkBehaviourHospMbAgdNtAply.setValue(true);
		}
	}
	

	private void addProcedureToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			ProcedureDTO procedureDTO, Boolean isZeroAppAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		dto.setProcedureDTO(procedureDTO);
		Boolean isPaymentAvailable = true;
		if (procedureDTO.getConsiderForPaymentFlag() != null) {
			isPaymentAvailable = procedureDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
		} else {
			isPaymentAvailable = false;
			if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1l)) {
				isPaymentAvailable = true;
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		}
			if(isPaymentAvailable) {
				if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
					isPaymentAvailable = false;
				}
			}
			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		dto.setRestrictionSI("NA");

		// GLX2020047 - UAT changes
		// dto.setPackageAmt("NA");
		dto.setPackageAmt("0");
		//GLX2020047
		dto.setAgreedPackageAmt("NA");
		if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
			dto.setPackageAmt(procedureDTO.getPackageRate().toString());
			//GLX2020047
			dto.setAgreedPackageAmt(procedureDTO.getPackageRate().toString());
		}

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}
		
		if(isZeroAppAmt) {
			dto.setMinimumAmount(0);
			dto.setReverseAllocatedAmt(0);
			dto.setIsPaymentAvailable(false);
		}
		dto.setCoPayPercentage(procedureDTO.getCopay());
		dto.setIsDeletedOne(isDeletedOne);
//		if(isDeletedOne) {
//			dto.setAmountConsidered(0);
//		}
		
//		dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
//		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
//		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
	}

	private void addDiagnosisValuesToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			DiagnosisDetailsTableDTO pedValidationTableDTO, Boolean isZeroAppAmt, Boolean isDeletedOne) {

		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = pedValidationTableDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			if (isPaymentAvailable && !isZeroAppAmt) {
				List<PedDetailsTableDTO> pedList = pedValidationTableDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

						List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
								.getExclusionAllDetails();
						String paymentFlag = "y";

						for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
							if (null != pedDetailsTableDTO
									.getExclusionDetails()
									&& exclusionDetails.getKey().equals(
											pedDetailsTableDTO
													.getExclusionDetails()
													.getId())) {
								paymentFlag = exclusionDetails
										.getPaymentFlag();
							}
						}

						if (paymentFlag.toLowerCase().equalsIgnoreCase("n")) {
							isPaymentAvailable = false;
							break;
						}
					}
				}
			}

			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
			
			if(isZeroAppAmt) {
				dto.setMinimumAmount(0);
				dto.setIsPaymentAvailable(false);
			}
		} else {
			dto.setIsPaymentAvailable(false);
		}
		if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
			dto.setRestrictionSI("NA");
		} else {
			dto.setRestrictionSI(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSumInsuredRestriction()
							.getValue()).toString());
		}

		if (pedValidationTableDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSublimitAmt()).toString());
		}
		dto.setPackageAmt("NA");
		//GLX2020047
		dto.setAgreedPackageAmt("NA");
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);

		if(isZeroAppAmt) {
			dto.setIsEnabled(false);
			
		}
		
		if(pedValidationTableDTO.getPedList().size() == 1) {
			dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
		}
		dto.setIsDeletedOne(isDeletedOne);
		
//		dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
//		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
//		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
	}

	// Pre auth requested from ClaimAmountDetails Table.
	public void setPreAuthRequestAmt(String strPreAuthAmt) {
		this.preAuthRequestedAmt = strPreAuthAmt;
	}

	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";

		if (!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		if (!this.processPreauthButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.processPreauthButtonObj.getErrors();
			for (String error : errors) {
				eMsg += error;
			}
		}
       
		if(! bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) && ! bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
		
			if (!bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
					SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
				hasError = true;
				eMsg += "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
			}
		}
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){
				
				hasError = true;
				eMsg += "Amount Entered against Ambulance charges should be equal";
				
			}
		}
		
//	
//		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
//		if(fvrGradingDTO != null && ! fvrGradingDTO.isEmpty()){
//		FvrGradingDetailsDTO values = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().get(0);
//		int val = values.getNumber();
//			if(!fvrGradingDTO.isEmpty()) {
//			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
//				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
//				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
//					FVRGradingDTO firstrecord = fvrGradingDTO2.get(0);
//					if(!(firstrecord.getStatus() != null))
//					if(!(fvrGradingDTO3.getStatus() != null)) {
//						hasError = true;
//						int serialNumber = fvrGradingDTO3.getSerialNumber();
//						eMsg += "Please Select " +val+ "st FVR Grading and set the Status </br>";
//						break;
//					}
//				}
//			}
//		}
//		}
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(fvrGradingDTO != null && ! fvrGradingDTO.isEmpty()){
			for(int i=0;i<fvrGradingDTO.size();i++){
				FvrGradingDetailsDTO  listValues = fvrGradingDTO.get(i);
				List<FVRGradingDTO> gradingValues = listValues.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDetailsDTO : gradingValues) {
					if(!(fvrGradingDetailsDTO.getStatusFlag() != null)) {
						hasError = true;
						int val = i+1;
						eMsg += "Please Select "+val+ " FVR Grading and set the Status </br>";
						break;
					}
				}
					
				}
		}
		
		if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) && bean.getIsPending()) {
			hasError = true;
			eMsg += "Cheque Status is Pending. Hence this Preauth Enhancement Can not be submitted.";
		}
		
		if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
		}
		
        /*if(! hasError && ! this.bean.getIsRechargePopUpOpened()){
			
			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null &&
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y")){
			
				if(approvedAmtField != null && approvedAmtField.getValue() != null && ! ("").equals(approvedAmtField.getValue())){
					Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
					this.bean.setSublimitAndSIAmt(approvedAmount);
					Double balanceSI = this.bean.getBalanceSI();
					if(approvedAmount > balanceSI){
						
						fireViewEvent(PAPreauthEnhancemetWizardPresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
						Double balanceSI2 = this.bean.getBalanceSI();
						Double rechargedAmount = balanceSI2 - balanceSI;
						getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
						this.bean.setIsRechargePopUpOpened(true);
						setApprovedAmtvalue();
						return false;
					 }
					
				  }
			   }
		     }*/

		
		if(bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalReverseAllocatedAmt().equals(SHAUtils.getIntegerFromString(this.bean.getReverseAmountConsidered())  )) {
			hasError = true;
			eMsg += "Total Final Approval Amt (B - Balance SI after Co-pay) Should be equal to Reverse column of Sub limits, Package & SI Restriction Table </br>";
		} else if(!hasError && !bean.getIsReverseAllocation() && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
			if(processPreauthButtonObj != null) {
				Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(processPreauthButtonObj.getApprovedAmount());
				Integer cValue = SHAUtils.getIntegerFromStringWithComma(this.medicalDecisionTableObj.dummyField.getValue()); 
				
				if(!cValue.equals(approvedAmt)) {
					hasError = true;
					if(!this.bean.getIsReverseAllocation()) {
						eMsg += "Please enter Reverse Allocation Amount.";
					} 
					this.bean.setIsReverseAllocation(true);
					
					this.medicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
					bean.setReverseAmountConsidered(String.valueOf(approvedAmt));
					
					
				}  else {
//					this.bean.setIsReverseAllocation(false);
				}
			}
		}
		
		if(approvedAmtField != null && approvedAmtField.getValue() != null && ! ("").equals(approvedAmtField.getValue())){
			this.bean.setSublimitAndSIAmt(SHAUtils.getDoubleValueFromString(approvedAmtField.getValue()));
		}
		
		if(!hasError) {
			if (bean.getStatusKey() != null && (bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))) {
				Integer amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
				if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					amt = processPreauthButtonObj.getHospitalAmtForUnique().intValue();
				}
				
				if(amt <= 0) {
					
					if((bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) && !ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						hasError = false;
					} else {
						hasError = true;
						eMsg += "Approved Amount is Zero. Hence this Preauth can not be submitted. ";
					}
					
				}
			}
		}

		if(!this.bean.getIsScheduleClicked()){
			hasError = true;
			eMsg += "Please Verify Policy Schedule Button.</br>";
		}
		

		// GLX2020047
		String errMsg = medicalDecisionTableObj.isValidForPkgChange();

		if (errMsg != null && !errMsg.isEmpty()) {
			hasError = true;
			eMsg += errMsg + "<br>";
		}
		
		if (cmbBehaviourHosp.getValue() == null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy().equals(1) ) {
			
			if(cmbBehaviourHosp.getValue() == null){
				hasError = true;
				eMsg +=  "Please select a value from the behaviour of hospital LOV to proceed Further.</br>";
			}
			
		}
		

		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				
				// New requirement for saving Copay values to Transaction Table......... 
				SHAUtils.setCopayAmounts(bean, this.amountConsideredTable);
				
				this.bean.getPreauthMedicalDecisionDetails()
						.setMedicalDecisionTableDTO(
								this.medicalDecisionTableObj.getValues());
				
				this.bean.setDiagnosisProcedureDtoList(bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO());
				
				this.bean.setCoPayValue(SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.selectedCopay) );
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null){
					this.bean.getPreauthMedicalDecisionDetails().setInvestigatorCode(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getInvestigatorCode());
				}
				
				Integer consideredAmountValue = getConsideredAmountValue();
				if(consideredAmountValue != null){
					this.bean.setAmountConsidedAfterCoPay(Double.valueOf(consideredAmountValue));
				}
				Integer balanceSumInsuredAmt = getBalanceSumInsuredAmt();
				if(balanceSumInsuredAmt != null){
					this.bean.setBalanceSumInsuredAfterCoPay(Double.valueOf(balanceSumInsuredAmt));
				}
				
				// Below line added for Final enhancement to show all the amounts in the letter . (As per BA we have done this change.......)
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() != null && !bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement()) {
					SHAUtils.doFinalEnhancementCalculationForLetter(this.medicalDecisionTableObj.getValues(), SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.getCoPayValue()) , bean);
				}
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);

				if(negotiationOpinionTaken != null && ! negotiationOpinionTaken.isEnabled()){
					bean.getPreauthMedicalDecisionDetails().setIsValidNegotiation(Boolean.TRUE);
				}
				
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}
	
	public void createReverseRelatedFields() {
		if(processPreauthButtonObj != null) {
			Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(processPreauthButtonObj.getApprovedAmount());
			Integer cValue = this.medicalDecisionTableObj.getTotalCAmount();
			
			if(!cValue.equals(approvedAmt)) {
				this.bean.setIsReverseAllocation(true);
				if(SHAUtils.getIntegerFromStringWithComma(bean.getReverseAmountConsidered()) <=  approvedAmt ) {
					this.medicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
					bean.setReverseAmountConsidered(String.valueOf(approvedAmt));
				} else {
					bean.setIsReverseAllocation(false);
				}
				
			}  else {
//				this.bean.setIsReverseAllocation(false);
			}
		}
	}
	
	 public void getAlertMessage(String eMsg){

			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("alertMessage");
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
  }


	@Override
	public boolean onAdvance() {
		fireViewEvent(
				PAPreauthEnhancemetWizardPresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());
		setResidualAmtToDTO();

		setCordinatorDetails();

		if(!this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS))
		{
			return validatePage();
		}
		else
		{
			return true;
		}
	}

	private void setCordinatorDetails() {
		SelectValue referToCordinatorSelValue = bean
				.getPreauthMedicalDecisionDetails()
				.getTypeOfCoordinatorRequest();
		if (null != referToCordinatorSelValue) {
			this.bean.getCoordinatorDetails().setTypeofCoordinatorRequest(
					referToCordinatorSelValue);
			this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest()
					.setId(referToCordinatorSelValue.getId());
			this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest()
					.setValue(referToCordinatorSelValue.getValue());

			if (null != bean.getPreauthMedicalDecisionDetails()
					.getReasonForRefering()) {
				this.bean.getCoordinatorDetails().setReasonForRefering(
						bean.getPreauthMedicalDecisionDetails()
								.getReasonForRefering());
			}
		}
	}

	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj
				.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if (medicalDecisionTableDTO.getDiagOrProcedure() != null
					&& medicalDecisionTableDTO.getDiagOrProcedure().contains(
							"Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean
						.getResidualAmountDTO();
				residualAmountDTO
						.setAmountConsideredAmount(medicalDecisionTableDTO
								.getAmountConsidered() != null ? medicalDecisionTableDTO
								.getAmountConsidered().intValue() : 0d);
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO
						.getNetAmount() != null ? medicalDecisionTableDTO
						.getNetAmount().doubleValue() : 0d);
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d);
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO
						.getCoPayAmount() != null ? medicalDecisionTableDTO
						.getCoPayAmount().doubleValue() : 0d);
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO
						.getMinimumAmount() != null ? medicalDecisionTableDTO
						.getMinimumAmount().doubleValue() : 0);
				residualAmountDTO.setNetApprovedAmount(medicalDecisionTableDTO
						.getReverseAllocatedAmt() != null ? medicalDecisionTableDTO
						.getReverseAllocatedAmt().doubleValue() : 0);
				residualAmountDTO.setRemarks(medicalDecisionTableDTO
						.getRemarks());
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
						.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
								.getValue()) : 0d);
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}


	@Override
	public boolean onBack() {
		List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
				.getPreauthDataExtractionDetails().getDiagnosisTableList();
		for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
			pedValidationTableDTO.setApprovedAmount(pedValidationTableDTO
					.getOldApprovedAmount());
		}
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
			procedureDTO.setApprovedAmount(procedureDTO.getOldApprovedAmount());
		}
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	private void setRequiredAndValidation(Component component) {
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnFieldVisit(Boolean isChecked,
			Object dropdowValues, Object assignToValues, Object fvrprioirty, List<ViewFVRDTO> fvrDTOList) {
		if (isChecked) {
			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			fvrTriggerPoints = (TextArea) binder.buildAndBind(
					"FVR Trigger Points", "fvrTriggerPoints", TextArea.class);
			fvrTriggerPoints.setMaxLength(4000);
			fvrTriggerPoints.setWidth("110%");
			cmbAllocationTo = (ComboBox) binder.buildAndBind("Allocation To",
					"allocationTo", ComboBox.class);
			cmbAllocationTo
					.setContainerDataSource((BeanItemContainer<SelectValue>) dropdowValues);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			if (this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null) {
				this.cmbAllocationTo.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getAllocationTo());
			}
			
			cmbFvrAssignTo = (ComboBox) binder.buildAndBind("Assign To",
					"assignTo", ComboBox.class);
			cmbFvrAssignTo
					.setContainerDataSource((BeanItemContainer<SelectValue>) assignToValues);
			cmbFvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrAssignTo.setItemCaptionPropertyId("value");
			
			if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
				this.cmbFvrAssignTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAssignTo());
			}
			
			cmbFvrPriority = (ComboBox) binder.buildAndBind("Priority",
					"priority", ComboBox.class);
			cmbFvrPriority
					.setContainerDataSource((BeanItemContainer<SelectValue>) fvrprioirty);
			cmbFvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrPriority.setItemCaptionPropertyId("value");
			
			if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
				this.cmbFvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
			}
			
			setRequiredAndValidation(cmbFvrAssignTo);
			setRequiredAndValidation(cmbFvrPriority);
			setRequiredAndValidation(fvrTriggerPoints);
			setRequiredAndValidation(cmbAllocationTo);

			unbindField(cmbFVRNotRequiredRemarks);

			mandatoryFields.add(fvrTriggerPoints);
			mandatoryFields.add(cmbAllocationTo);
			mandatoryFields.add(cmbFvrAssignTo);
			mandatoryFields.add(cmbFvrPriority);

			mandatoryFields.remove(cmbFVRNotRequiredRemarks);
			fvrFieldFLayout.removeComponent(cmbFVRNotRequiredRemarks);
			
			lblNoOfFvr = new Label(fvrDTOList.size() + " FVR Reports Already Exists");
			btnViewFVRDetails = new Button("View FVR Details");
			btnViewFVRDetails.addClickListener(new Button.ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (bean != null && bean.getNewIntimationDTO() != null
							&& bean.getNewIntimationDTO().getIntimationId() != null) {
						// TODO: Should give true or false based on fvr initiated
						viewDetails.getFVRDetails(bean.getNewIntimationDTO()
								.getIntimationId(), false);
					}
				}
			});		
			fvrHorizontalLayout.addComponent(lblNoOfFvr);
			fvrHorizontalLayout.addComponent(btnViewFVRDetails);
			fvrHorizontalLayout.setSpacing(true);
			
			
			
			fvrFieldFLayout.addComponent(cmbAllocationTo);
			fvrFieldFLayout.addComponent(cmbFvrAssignTo);
			fvrFieldFLayout.addComponent(cmbFvrPriority);
			fvrFieldFLayout.addComponent(fvrTriggerPoints);

		} else {
			
			
			fvrFieldFLayout.removeComponent(cmbFVRNotRequiredRemarks);
			unbindField(cmbFVRNotRequiredRemarks);
			cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
					"FVR Not Required Remarks", "fvrNotRequiredRemarks",
					ComboBox.class);
			BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
					.get("fvrNotRequiredRemarks");

			cmbFVRNotRequiredRemarks
					.setContainerDataSource(fvrNotRequiredRemarks);
			cmbFVRNotRequiredRemarks
					.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");
			mandatoryFields.add(cmbFVRNotRequiredRemarks);
			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			setRequiredAndValidation(cmbFVRNotRequiredRemarks);
			if(fvrTriggerPoints != null){

			fvrFieldFLayout.removeComponent(fvrTriggerPoints);
			}
			if(cmbAllocationTo !=null){
			fvrFieldFLayout.removeComponent(cmbAllocationTo);
			}
			
			if(cmbFvrAssignTo != null){
				fvrFieldFLayout.removeComponent(cmbFvrAssignTo);
			}
			if(cmbFvrPriority != null){
				fvrFieldFLayout.removeComponent(cmbFvrPriority);
			}
			
			if(lblNoOfFvr !=null){
			fvrHorizontalLayout.removeComponent(lblNoOfFvr);
			}if(btnViewFVRDetails!=null){
			fvrHorizontalLayout.removeComponent(btnViewFVRDetails);
			}
			mandatoryFields.remove(fvrTriggerPoints);
			mandatoryFields.remove(cmbAllocationTo);
			mandatoryFields.remove(cmbFvrAssignTo);
			mandatoryFields.remove(cmbFvrPriority);

			fvrFieldFLayout.addComponent(cmbFVRNotRequiredRemarks);
			mandatoryFields.add(cmbFVRNotRequiredRemarks);
		}
	}

	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnSpecialist(Boolean isChecked,
			Map<String, Object> dropdownValuesMap) {
		if (isChecked) {
			cmbSpecialistType = (ComboBox) binder.buildAndBind(
					"Specialist Type", "specialistType", ComboBox.class);
			cmbSpecialistType
					.setContainerDataSource((BeanItemContainer<SelectValue>) dropdownValuesMap
							.get("specialistType"));
			cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSpecialistType.setItemCaptionPropertyId("value");

			cmbSpecialistConsulted = (ComboBox) binder.buildAndBind(
					"Specialist Consulted", "specialistConsulted",
					ComboBox.class);
			cmbSpecialistConsulted
					.setContainerDataSource((BeanItemContainer<SelectValue>) dropdownValuesMap
							.get("specialistConsulted"));
			cmbSpecialistConsulted.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSpecialistConsulted.setItemCaptionPropertyId("value");
			txtRemarksBySpecialist = (TextArea) binder.buildAndBind("Remarks by Specialist", "remarksBySpecialist", TextArea.class);
			txtRemarksBySpecialist.setMaxLength(4000);
			setRequiredAndValidation(cmbSpecialistConsulted);
			setRequiredAndValidation(txtRemarksBySpecialist);
			setRequiredAndValidation(cmbSpecialistType);
			specialistFLayout.addComponent(cmbSpecialistType);
			specialistFLayout.addComponent(cmbSpecialistConsulted);
			specialistFLayout.addComponent(txtRemarksBySpecialist);
			mandatoryFields.add(cmbSpecialistConsulted);
			mandatoryFields.add(cmbSpecialistType);
			mandatoryFields.add(txtRemarksBySpecialist);
		} else {
			if (cmbSpecialistType != null && cmbSpecialistConsulted != null
					&& txtRemarksBySpecialist != null) {
				unbindField(cmbSpecialistType);
				unbindField(cmbSpecialistConsulted);
				unbindField(txtRemarksBySpecialist);
				specialistFLayout.removeComponent(cmbSpecialistType);
				specialistFLayout.removeComponent(cmbSpecialistConsulted);
				specialistFLayout.removeComponent(txtRemarksBySpecialist);
				mandatoryFields.remove(cmbSpecialistConsulted);
				mandatoryFields.remove(txtRemarksBySpecialist);
				mandatoryFields.remove(cmbSpecialistType);
			}
		}
	}

	public void generateFieldBasedOnSentToCPU(Boolean isChecked) {
		processPreauthButtonObj.generateFieldsBasedOnSentToCPU(isChecked);
	}

	public void showClaimAmountDetails() {
		ViewClaimAmountDetils claimDetails = viewClaimAmountDetails.get();
		claimDetails.showDetails(this.bean, this.referenceData);
		UI.getCurrent().addWindow(claimDetails);
	}
	
	public void setBalanceSIforRechargedProcessing(Double balanceSI){
		if(balanceSI != null){
			this.bean.setBalanceSI(balanceSI);
			if(this.amountConsideredTable != null){
				this.amountConsideredTable.setBalanceSumInsuredAfterRecharge(balanceSI);
			}
		}
	}

	public void showBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			viewDetails.getViewBalanceSumInsured(intimationId);
		}
	}

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		this.bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		this.bean.setProcessType("E");
		switch (clickedButton) {

		case 1:
			Integer min = Math
					.min(amountConsideredTable.getMinimumValue(),
							SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
									.getValue()));
			if(bean.getIsNonAllopathic()) {
				min = Math.min(min, bean.getNonAllopathicAvailAmt());
			}
			
			
			if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				if(bean.getPreauthMedicalDecisionDetails().getUniquePremiumAmt() >= min.doubleValue()) {
					this.processPreauthButtonObj.disableApprove(false);
					alertMessageForUniquePremium();
				} else {
					this.processPreauthButtonObj.disableApprove(true);
					this.processPreauthButtonObj.buildApproveLayout(min);
				}
			} else {
				this.processPreauthButtonObj.buildApproveLayout(min);
			}
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
							.getValue()));
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					this.processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doSuperSurplusCalculation());
				}
			}
			
			
			// this.medicalDecisionTableObj.getTableItemIds();
			// this.medicalDecisionTableObj.setVisibleApproveFields(true);
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
			break;
		case 2:
			this.processPreauthButtonObj.buildQueryLayout();
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
			break;
		case 3:
			this.processPreauthButtonObj.buildRejectLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
			break;
		case 4:
			this.processPreauthButtonObj.buildWithdrawLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setProcessType("W");
			this.bean
					.setStatusKey(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
			break;
		case 5:
			this.processPreauthButtonObj.buildEscalateLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
					.setStatusKey(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS);
			break;
		case 6:
			this.processPreauthButtonObj
					.buildReferCoordinatorLayout(referenceData
							.get("coordinatorTypeRequest"));
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
					.setStatusKey(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
			break;
		case 7:
			this.bean.setProcessType("D");
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
//			this.processPreauthButtonObj.clearFields();
			Integer minValue = Math
					.min(amountConsideredTable.getMinimumValue(),
							SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
									.getValue()));
			this.processPreauthButtonObj.buildDownsizeLayout(minValue,
					dropDownValues);
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					minValue = Math.min(amountConsideredTable.getConsideredAmountValue(), SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
							.getValue()));
					amountConsideredTable.hospApprovedAmountTxt.setValue(minValue.toString());
					this.processPreauthButtonObj.buildDownsizeLayout(amountConsideredTable.doSuperSurplusCalculation(),
							dropDownValues);
				}
			}
			referenceData.put("downsizeReason", dropDownValues);
			// this.medicalDecisionTableObj.setVisibleDownSizeFields(true);
			break;
		default:
			break;
		}
	}
	
	public void generateWithdrawAndRejectButton(Object withdrawDropDownValues, Object rejectDropDownValues) {
		this.bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		this.bean.setProcessType("E");
		this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
		this.processPreauthButtonObj.buildWithdrawAndRejectLayout(withdrawDropDownValues, rejectDropDownValues);
	}

	public void setSumInsuredCaculationsForSublimit(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName) {
		this.sublimitCalculatedValues = diagnosisSumInsuredLimit;
		this.diagnosisName = diagnosisName;
	}

	public void setInvestigationCheck(Boolean checkInitiateInvestigation) {
	
		bean.setIsInvestigation(checkInitiateInvestigation);
		/*if (!checkInitiateInvestigation) {
		unbindField(investigationReportReviewedChk);
		unbindField(investigationReviewRemarks);
		unbindField(investigatorName);
		mandatoryFields.remove(investigationReviewRemarks);
	} else*/ if (checkInitiateInvestigation && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
		investigationReportReviewedChk.setEnabled(true);
		investigationReviewRemarks.setEnabled(true);
		investigatorName.setEnabled(true);
//		mandatoryFields.remove(investigationReviewRemarks);
	}else{
//		unbindField(investigationReportReviewedChk);
//		unbindField(investigationReviewRemarks);
//		unbindField(investigatorName);
		bean.setIsInvestigation(false);
		investigationReportReviewedChk.setEnabled(false);
		investigationReviewRemarks.setEnabled(false);
		investigatorName.setEnabled(false);
	}
	}

	public void setBalanceSI(Double balanceSI, List<Double> copayValue) {
		if (balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(copayValue);
	}

	private void addMedicalDecisionTableFooterListener() {

		this.medicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > min ) {
							processPreauthButtonObj.enableOrDisable(true);
							if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
								processPreauthButtonObj.unbindAndRemoveComponents(processPreauthButtonObj.dynamicFrmLayout);
							}
						} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= min ) {
							processPreauthButtonObj.enableOrDisable(false);
							if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
								processPreauthButtonObj.unbindAndRemoveComponents(processPreauthButtonObj.dynamicFrmLayout);
							}
						}
						
						
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))) {
							//processPreauthButtonObj.buildApproveLayout(min);
							
							if(!processPreauthButtonObj.approveBtn.isEnabled() &&  bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
								processPreauthButtonObj.unbindAndRemoveComponents(processPreauthButtonObj.dynamicFrmLayout);
							}
							
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
								
								}
							}
						} else if ((bean.getStatusKey()
								.equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS))) {
							
							processPreauthButtonObj.buildDownsizeLayout(min,
									referenceData.get("downsizeReason"));
							
							if(!processPreauthButtonObj.downSizePreauthBtn.isEnabled() &&  bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
								processPreauthButtonObj.unbindAndRemoveComponents(processPreauthButtonObj.dynamicFrmLayout);
							}
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								}
							}
						}

					}
				});

		this.amountConsideredTable.dummyField
				.addValueChangeListener(new ValueChangeListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString(medicalDecisionTableObj.dummyField
										.getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(bean.getIsReverseAllocation()) {
							Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(processPreauthButtonObj.getApprovedAmount());
							if(!approvedAmt.equals(min)) {
								approvedAmt = min;
							}
							Integer cValue = SHAUtils.getIntegerFromStringWithComma(medicalDecisionTableObj.dummyField.getValue()); 
							if(cValue.equals(approvedAmt) && !bean.getReverseAmountConsidered().equals(String.valueOf(approvedAmt))) {
								bean.setIsReverseAllocation(false);
								medicalDecisionTableObj.deleteReverseAllocation();
							}
						}
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))) {
							processPreauthButtonObj.buildApproveLayout(min);
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
									
								}
							}
						} else if ((bean.getStatusKey()
								.equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS))) {
							
							processPreauthButtonObj.buildDownsizeLayout(min,
									referenceData.get("downsizeReason"));
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								}
							}
						}

					}
				});
		
		this.medicalDecisionTableObj.ambulanceChangeField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -4052108705772482724L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField ambulanceTotal = (TextField) event.getProperty();
				if(amountConsideredTable != null) {
					amountConsideredTable.ambulanceChargeField.setValue(String.valueOf(SHAUtils.getIntegerFromStringWithComma(ambulanceTotal.getValue())));
				}
			}
		});

	}
	
	public Integer getConsideredAmountValue()
	{
		if(amountConsideredTable != null){
		return amountConsideredTable.getConsideredAmountValue();
		}else{
			return 0;
		}
		
	}
	
	private void setApprovedAmtvalue() {
		
//		Integer totalApprovedAmt = SHAUtils
//				.getIntegerFromString(medicalDecisionTableObj.dummyField
//						.getValue());
		
		Integer totalApprovedAmt = this.medicalDecisionTableObj.getTotalCAmount();
		
		approvedAmtField.setValue(String
				.valueOf(totalApprovedAmt));
		Integer min = Math.min(
				amountConsideredTable.getMinimumValue(),
				totalApprovedAmt);
		if(bean.getIsNonAllopathic()) {
			min = Math.min(min, bean.getNonAllopathicAvailAmt());
		}
		if ((bean.getStatusKey()
				.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))) {
			processPreauthButtonObj.buildApproveLayout(min);
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
					if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(true);
					} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(false);
					}
				}
			}
		} else if ((bean.getStatusKey()
				.equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS))) {
			
			processPreauthButtonObj.buildDownsizeLayout(min,
					referenceData.get("downsizeReason"));
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					processPreauthButtonObj.buildDownsizeLayout(doSuperSurplusCalculation,
							referenceData.get("downsizeReason"));
					if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(true);
					} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(false);
					}
				}
			}
		} else {
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(true);
					} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(false);
					}
				}
			}
		}

	}
	
  public Integer getBalanceSumInsuredAmt(){
		
		if(amountConsideredTable != null){
			return amountConsideredTable.getBalanceSumInsuredAmt();
			}else{
				return 0;
			}
	}
  
  private void setAppropriateValues(Map<String, Object> caluculationInputValues,
			DiagnosisProcedureTableDTO medicalDecisionDto,
			Map<String, Object> values) {
		medicalDecisionDto.setRestrictionSI(caluculationInputValues
				.get("restrictedSIAmount") != null ? SHAUtils
				.getIntegerFromString(
						(String) caluculationInputValues
								.get("restrictedSIAmount")).toString()
				: "NA");
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) values.get("currentSL"))
						.intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto
				.setCoPayPercentageValues((List<String>) values
						.get("copay"));

		if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
			medicalDecisionDto.getDiagnosisDetailsDTO()
					.setDiagnosis(this.diagnosisName);
		}

		// need to implement in new medical listener table
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			}else {
				isResidual = true;
			}
			
			if(!isResidual && bean.getEntitlmentNoOfDays() != null) {
				Float floatAvailAmt = bean.getEntitlmentNoOfDays() * subLimitAvaliableAmt;
				Integer availAmt = Math.round(floatAvailAmt);
				int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
				medicalDecisionDto.setSubLimitAvaliableAmt(min);
				medicalDecisionDto.setSubLimitUtilAmount(0);
			}
		}
	}
  
 /* @SuppressWarnings("unchecked")
	public void genertateFieldsBasedOnNegotiation(Boolean isChecked) {
		if (isChecked) {
			txtNegotiationRemarks = (TextArea) binder.buildAndBind("Points to Negotiate", "negotiatePoints", TextArea.class);
			txtNegotiationRemarks.setMaxLength(4000);
			txtNegotiationRemarks.setWidth("45%");			
			setRequiredAndValidation(txtNegotiationRemarks);
			negotiationLayout.addComponent(txtNegotiationRemarks);
			mandatoryFields.add(txtNegotiationRemarks);			
			if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
				if(bean.getIsNegotiationPending()){
					txtNegotiationRemarks.setReadOnly(false);
					txtNegotiationRemarks.setValue(bean.getPreauthMedicalDecisionDetails().getNegotiatePoints());
					txtNegotiationRemarks.setReadOnly(true);
				}
			}
			SHAUtils.handleTextAreaPopUp(txtNegotiationRemarks,null,getUI(),SHAConstants.STP_REMARKS,bean);
		} else {
			if (txtNegotiationRemarks != null) {
				unbindField(txtNegotiationRemarks);
				negotiationLayout.removeComponent(txtNegotiationRemarks);
				mandatoryFields.remove(txtNegotiationRemarks);
			}
		}
	}
  
  protected void addNegotiationListener() {
		negotiationOpinionTaken
		.addValueChangeListener(new Property.ValueChangeListener() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if (event.getProperty() != null
						&& event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				}

				fireViewEvent(
						PAPreauthEnhancemetWizardPresenter.NEGOTIATION_OPINION_RADIO_CHANGED,
						isChecked);
			}
		});
	}
  @SuppressWarnings("unchecked")
	public void updateNegotiationRemarks(String updatedRemarks) {
		txtNegotiationRemarks.setReadOnly(false);
		txtNegotiationRemarks.setValue(updatedRemarks);
		txtNegotiationRemarks.setReadOnly(true);
	}*/
  
  public VerticalLayout getBehaviouroHospital(){

		 fBehaviourHospFLayout = new FormLayout();

		 Label formHeadName = new Label("<b style = 'color: black;'>Behaviour of the Hospital</b>", ContentMode.HTML);

		 cmbBehaviourHosp = (ComboBox) binder.buildAndBind("", "behaviourHospValue",ComboBox.class);
		 cmbBehaviourHosp.setNullSelectionAllowed(false);
	

		 chkBehavHosColOvrPack = (CheckBox) binder.buildAndBind("Hospital collected Money over and above package/ SOC from the client", "behaviourHosp1chkbox",
				 CheckBox.class);

		 chkBehaviourHospMbAgdNtAply = (CheckBox) binder.buildAndBind("MB agreed Discount not applied", "behaviourHospMbagreedchkbox",
				 CheckBox.class);
		 
		 Label dummy2 = new Label("<b style = 'color: red;'>*</b>", ContentMode.HTML);
		 
		 if( bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy() != null 
				 && !bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy().equals(1)){
			 chkBehaviourHospMbAgdNtAply.setEnabled(false);
			 chkBehavHosColOvrPack.setEnabled(false);
			 cmbBehaviourHosp.setEnabled(false);
			 dummy2.setVisible(false);
			
		 }


		 cmbBehaviourHosp.addValueChangeListener(new ValueChangeListener() {
			 private static final long serialVersionUID = 2697682747976915503L;

			 @Override
			 public void valueChange(ValueChangeEvent event) {

			 }
		 });
		
		 
		 HorizontalLayout BehaviourHospHLayout = new HorizontalLayout(cmbBehaviourHosp,dummy2);

		 BehaviourHospHLayout.setComponentAlignment(cmbBehaviourHosp, Alignment.BOTTOM_RIGHT);
		 BehaviourHospHLayout.setComponentAlignment(dummy2, Alignment.MIDDLE_RIGHT);
		 BehaviourHospHLayout.setSpacing(false);
		 BehaviourHospHLayout.setMargin(false);
		 
		 
		 fBehaviourHospFLayout.addComponent(formHeadName);
		 fBehaviourHospFLayout.addComponent(BehaviourHospHLayout);	 
		 fBehaviourHospFLayout.addComponent(chkBehavHosColOvrPack);
		 fBehaviourHospFLayout.addComponent(chkBehaviourHospMbAgdNtAply);
		 fBehaviourHospFLayout.setSpacing(true);
	
		 
		 VerticalLayout behaviourhosp = new VerticalLayout();
		 behaviourhosp.addComponent(fBehaviourHospFLayout);

		 return behaviourhosp;

	 }
}
