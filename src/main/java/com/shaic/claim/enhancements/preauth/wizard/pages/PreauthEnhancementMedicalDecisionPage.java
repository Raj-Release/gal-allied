package com.shaic.claim.enhancements.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewTopUpPolicyDetailsForGMC;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancementButtons;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
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
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.listenerTables.UpdateOtherClaimDetailsUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.InvestigationReviewRemarksTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.NewMedicalDecisionFVRGrading;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreauthEnhancementMedicalDecisionPage extends ViewComponent
implements WizardStep<PreauthDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5808617123469705368L;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;

	@Inject
	private Instance<NewMedicalDecisionFVRGrading> fvrGradingTableInstance;
	
	private NewMedicalDecisionFVRGrading fvrGradingTableObj;

	@Inject
	private Instance<PreauthEnhancementButtons> processPreauthButtonLayout;

	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;

	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;

	private PreauthEnhancementButtons processPreauthButtonObj;

	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	@Inject
	private UpdateOtherClaimDetailsUI updateOtherClaimDetailsUI;

	private AmountConsideredTable amountConsideredTable;

	private RevisedMedicalDecisionTable medicalDecisionTableObj;

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
	
	//private ComboBox cmbFvrAssignTo;
	
	private ComboBox cmbFvrPriority;

//	private TextArea fvrTriggerPoints;
	


	private Button btnViewFVRDetails;

	private Label lblNoOfFvr;

	private TextArea txtMedicalRemarks;

	private TextArea txtDoctorNote;

	private TextField approvedAmtField;

	private VerticalLayout wholeVLayout;

	private FormLayout investigationFLayout;

	private FormLayout fvrFieldFLayout;

	private HorizontalLayout fvrHorizontalLayout;
	
	private VerticalLayout fvrVertLayout;
	
	private FormLayout fvrAssignFLayout;
	
	private HorizontalLayout fvrHLayout;

	private FormLayout specialistFLayout;

	private Label preauthAmtRequested;

	private Button showclaimAmtDetailsBtn;

	private Button showclaimAmtDetailsBtnDuplicate;

	private Label amountConsidered;

	//	private Label balanceSumInsured;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	Map<String, Object> referenceData = new HashMap<String, Object>();

	Map<String, Object> sublimitCalculatedValues;

	//	private Double balanceSumInsuredValue;

	private String diagnosisName;

	private Button initiatePEDButton;

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
	
	private TextArea fvrOthersRemarks;
	
	@Inject
	private InvestigationService investigationService;
	
	private Button addAdditionalFvrPointsBtn;
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	
	@Inject
	private Instance<InvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
	private InvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
	private OptionGroup negotiationOpinionTaken;	
	private TextArea txtNegotiationRemarks;	
	private FormLayout negotiationLayout;
	
	private boolean isFVRAssigned;
	
	private ComboBox cmbBehaviourHosp;
	
	private FormLayout fBehaviourHospFLayout;

	private CheckBox chkBehavHosColOvrPack;

	private CheckBox chkBehaviourHospMbAgdNtAply;
	
	@Inject
	private ViewTopUpPolicyDetailsForGMC viewTopUpGmcPage;

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
		
		Long claimkeytemp = bean.getClaimKey();
		Long policyKeytemp = bean.getNewIntimationDTO().getPolicy().getKey();
		Long insuredKeytemp = bean.getNewIntimationDTO().getInsuredKey();
		List<DiagnosisDetailsTableDTO> diagnosisDetailsTableDTOList = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		Object icdCodeList[] = new Object[diagnosisDetailsTableDTOList.size()];
		for (int i= 0; i < diagnosisDetailsTableDTOList.size() ; i++ ) {
			DiagnosisDetailsTableDTO diagnosisDetailsTableDTO = diagnosisDetailsTableDTOList.get(i);
			SelectValue selectValue = diagnosisDetailsTableDTO.getIcdCode();
			String icdCodeValuetemp = selectValue.getCommonValue();
			icdCodeList[i] = icdCodeValuetemp;
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		System.out.println("SIBasedICDAlertMsg ::: policyKey : "+policyKeytemp+" insuredKey : "+insuredKeytemp+" claimkey : "+claimkeytemp+" icdCodeList : "+icdCodeList.toString());
		String alertMsg = dbCalculationService.getSIBasedICDAlertMsg(claimkeytemp, policyKeytemp, insuredKeytemp, icdCodeList);
		if(alertMsg != "null")
		{
			SHAUtils.showAlertMessageBox(alertMsg);
		}
		
		this.isFVRAssigned = false;
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		bean.setAlertMessageForCopay(false);

		initiatePEDButton = new Button("Initiate PED Endorsement");
		
		if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()
				&& null != bean.getNewIntimationDTO().getPolicy().getProduct().getCode()
				&& !bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)){
			initiatePEDButton.setEnabled(false);
		}
		else{
			initiatePEDButton.setEnabled(true);
		}

		Button updatePreviousButton = new Button("Update Previous/Other Claim Details(Defined Limit)");

		updatePreviousButton.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				fireViewEvent(PreauthEnhancemetWizardPresenter.UPDATE_PREVIOUS_CLAIM_DETAILS, bean);
			}

		});
		
		addAdditionalFvrPointsBtn = new Button("Add Additional FVR Points");
		addAdditionalFvrPointsBtn.setEnabled(false);
		if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && !bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived()){
			addAdditionalFvrPointsBtn.setEnabled(true);
		}

		HorizontalLayout buttonHLayout = null;

		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			buttonHLayout = new HorizontalLayout(addAdditionalFvrPointsBtn,updatePreviousButton,initiatePEDButton);
		}else{
			buttonHLayout = new HorizontalLayout(addAdditionalFvrPointsBtn,initiatePEDButton);
		}

	//	buttonHLayout.setComponentAlignment(initiatePEDButton,Alignment.MIDDLE_RIGHT);
		buttonHLayout.setComponentAlignment(addAdditionalFvrPointsBtn,Alignment.MIDDLE_RIGHT);
		HorizontalLayout buttonInvestigationLayout = null;
		initiateFieldVisitButton = new Button("Initiate Investigation");
		initiateFieldVisitButton.setEnabled(true);
	//	initiateFieldVisitButton.setEnabled(false);
		buttonInvestigationLayout = new HorizontalLayout(initiateFieldVisitButton);
		buttonInvestigationLayout.setComponentAlignment(initiateFieldVisitButton,
				Alignment.MIDDLE_RIGHT);
		buttonInvestigationLayout.setEnabled(true);
		btnViewSpecialistOpinion = new Button("View Specialist Opinion");
		HorizontalLayout buttonLayout = new HorizontalLayout(btnViewSpecialistOpinion);
		buttonLayout.setComponentAlignment(btnViewSpecialistOpinion,
				Alignment.MIDDLE_RIGHT);

		consideredAmtField = new TextField("Amount Considered");
		consideredAmtField.setReadOnly(false);
		consideredAmtField.setValue(this.bean.getAmountConsidered());
		consideredAmtField.setReadOnly(true);

		//		FormLayout considerAmtForm = new FormLayout(consideredAmtField);

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
		
		txtMedicalRemarks.setId("medicalRmrks");
		medicalRemarksListener(txtMedicalRemarks,null);
		txtMedicalRemarks.setData(bean);
		txtMedicalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");

		txtDoctorNote = (TextArea) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextArea.class);

		txtDoctorNote.setMaxLength(4000);
		
		txtDoctorNote.setId("internalRmrks");
		medicalRemarksListener(txtDoctorNote,null);
		txtDoctorNote.setData(bean);
		txtDoctorNote.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		initiateFieldVisitRequestRadio.addItems(getReadioButtonOptions());
		initiateFieldVisitRequestRadio.setItemCaption(true, "Yes");
		initiateFieldVisitRequestRadio.setItemCaption(false, "No");
		initiateFieldVisitRequestRadio.setStyleName("horizontal");

		cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
				"FVR Not Required Remarks", "fvrNotRequiredRemarks",
				ComboBox.class);	
		cmbFVRNotRequiredRemarks.setNullSelectionAllowed(false);
				
		/**
		* Requirement Number:R1118
		* For adding Others as additional LOV and text area in FVR Not Required Remarks
		* Modified By : GokulPrasath.A
		* Modified On : 07th MAR 2018
		*
		*/
		fvrOthersRemarks = (TextArea) binder.buildAndBind(
				"Enter remarks for Others", "fvrNotRequiredOthersRemarks",
				TextArea.class);
		fvrOthersRemarks.setHeight("70%");
		fvrOthersRemarks.setMaxLength(230);
		fvrOthersRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		fvrOthersRemarks.setCaption("Enter remarks for Others");
		fvrOthersRemarks.setData(bean);
		handleTextAreaPopup(fvrOthersRemarks,null);
		fvrOthersRemarks.setData(bean);
		
		triggerPtsTableObj = triggerPtsTable.get();
		triggerPtsTableObj.init();
		
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
		
		HorizontalLayout formsHLayout = new HorizontalLayout(fvrHorizontalLayout);
		formsHLayout.setWidth("100%");
		formsHLayout.setSpacing(true);
		
		fvrVertLayout = new VerticalLayout(formsHLayout);
		
		/*if(null !=this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
			initiateFieldVisitRequestRadio.select(this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag());
			Boolean isCheck=true;
			if(!this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
				isCheck=false;
			}

			fireViewEvent(
					PreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
					isCheck, bean);
		}	*/	
		
		if(bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequest() != null &&
				null != bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()) 				
		{
			Boolean isCheck= Boolean.FALSE;
			if(!bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && !bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() &&
					bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequest().equals(1)){
				initiateFieldVisitRequestRadio.select(Boolean.TRUE);	
				isCheck= Boolean.TRUE;
			}
			else {
				initiateFieldVisitRequestRadio.select(Boolean.FALSE);
			}
			

			fireViewEvent(
					PreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
					isCheck, bean);
		}		
		else{
		
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

		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			initiateFieldVisitRequestRadio.setEnabled(false);
			cmbFVRNotRequiredRemarks.setEnabled(false);
		//	initiateFieldVisitButton.setEnabled(false);
			addAdditionalFvrPointsBtn.setEnabled(false);
		}
		specialistFLayout = new FormLayout(specialistOpinionTaken);
		specialistFLayout.setMargin(false);
		/*investigationFLayout = new FormLayout(investigationReportReviewedChk,
				investigatorName, investigationReviewRemarks);*/		
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		fireViewEvent(PreauthEnhancemetWizardPresenter.INVS_REVIEW_REMARKS_LIST, this.bean);
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
		investigationFLayout = new FormLayout(invsReviewRemarksTableObj);	
		investigationFLayout.setMargin(false);
		HorizontalLayout specialistHLayout = new HorizontalLayout(specialistFLayout, investigationFLayout);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);
		specialistHLayout.setMargin(false);
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
		
		fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		negotiationLayout.addComponent(negotiationOpinionTaken);
		negotiationLayout.setMargin(false);
		negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			fireViewEvent(
					PreauthEnhancemetWizardPresenter.NEGOTIATION_OPINION_RADIO_CHANGED,true);
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
		fireViewEvent(PreauthEnhancemetWizardPresenter.BALANCE_SUM_INSURED,
				this.bean);

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		
		processPreauthButtonObj = processPreauthButtonLayout.get();

		processPreauthButtonObj.initView(this.bean,this.wizard,this.medicalDecisionTableObj);

		//CR2019017 - Start
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
				
		// HorizontalLayout buttonsHLayout = new
		// HorizontalLayout(processPreauthButtonObj);
		Label dummyLabel = new Label("");
		dummyLabel.setWidth("10%");
		HorizontalLayout buttonWholeLayout = new HorizontalLayout(dummyLabel,processPreauthButtonObj);
		buttonWholeLayout.setWidth("10%");
		buttonWholeLayout.setComponentAlignment(processPreauthButtonObj,
				Alignment.MIDDLE_LEFT);

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
		
		//IMSSUPPOR-24086
		if(this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO() == null || this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()){
			fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_FIELD_VISIT_GRADING,
					this.bean);
		}
		
		fvrGradingTableObj = null;
		if(this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO() != null && ! this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()) {
			fvrGradingTableObj = fvrGradingTableInstance.get();
			fvrGradingTableObj.initView(bean, true);
		}
		VerticalLayout fvrVLayout = new VerticalLayout();
		if(fvrGradingTableObj != null) {
			fvrVLayout.addComponent(fvrGradingTableObj);
		}
		
		
		fvrHLayout = new HorizontalLayout(fvrVertLayout);
		if(this.isFVRAssigned){
			fvrHLayout.addComponent(fvrAssignFLayout);
		}		

		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				/*&& bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()*/){
			HorizontalLayout generateFieldBasedOnCorpBuffer = generateFieldBasedOnCorpBuffer();
			wholeVLayout = new VerticalLayout(buttonHLayout, buttonInvestigationLayout, buttonLayout,
					this.previousPreAuthDetailsTableObj, amountConsideredLayout,
					this.medicalDecisionTableObj,generateFieldBasedOnCorpBuffer,this.amountConsideredTable,
					/*fvrVertLayout*/ fvrHLayout, fvrVLayout,
					specialistHLayout,/* negotiationLayout,*/buttonWholeLayout, remarksHLayout);
		}else{
			HorizontalLayout hospBehaviour = new HorizontalLayout(getBehaviouroHospital());
			hospBehaviour.setSpacing(true);
			wholeVLayout = new VerticalLayout(buttonHLayout, buttonInvestigationLayout, buttonLayout,
					this.previousPreAuthDetailsTableObj, amountConsideredLayout,
					this.medicalDecisionTableObj,hospBehaviour, this.amountConsideredTable,
					/*fvrVertLayout*/ fvrHLayout, fvrVLayout,
					specialistHLayout, /*negotiationLayout,*/buttonWholeLayout, remarksHLayout);
		}
		wholeVLayout
		.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout
		.setComponentAlignment(buttonInvestigationLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setComponentAlignment(buttonHLayout,
				Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);

		mandatoryFields.add(cmbFVRNotRequiredRemarks);
		mandatoryFields.add(fvrOthersRemarks);
		//		mandatoryFields.add(txtMedicalRemarks);
		// mandatoryFields.add(investigatorName);
		mandatoryFields.add(investigationReviewRemarks);
		showOrHideValidation(false);
		addListener();
		addPEDListener();
		addAdditionalFvrPointsListener();

		addMedicalDecisionTableFooterListener();

		generateFieldBasedOnStatusKey();
		
		//TOP-UP POLICY
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo() != null && bean.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId() != null){
				Integer totalApprovedAmt = SHAUtils
						.getIntegerFromString(medicalDecisionTableObj.dummyField
								.getValue());
				
				Integer minOfAandC = Math.min(
						amountConsideredTable.getConsideredAmountValue(),
						totalApprovedAmt);
				
				if(minOfAandC > amountConsideredTable.getGMCBalanceSumInsuredAmt()){
					processPreauthButtonObj.enableTopupPolicy(true);
				}else{
					processPreauthButtonObj.enableTopupPolicy(false);
				}
			}
		}
		
		return wholeVLayout;
	}
	
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("FVR Not Required Remarks For Others",KeyCodes.KEY_F8,null) {

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
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(230);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(txtFld.getValue());
						}
					
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "FVR Not Required Remarks For Others";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
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
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.ENHANCEMENT_STAGE,false);	
		viewPEDRequest.setPresenterString(SHAConstants.CASHLESS_STRING);
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





	public Boolean alertMessageForPEDInitiate(String message) {/*
		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
		//   		final Boolean isClicked = false;
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
	*/
		final MessageBox showAlert = showAlertMessageBox(message);
		Button homeButton = showAlert.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showAlert.close();
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
		
	}

	public Boolean alertMessageForUniquePremium() {/*
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
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		//	dialog.setCaption("Alert");
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
		return true;
	*/

		final MessageBox showAlert = showAlertMessageBox(SHAConstants.UNIQUE_PREMIUM_EXCEEDING_MESSAGE);
		Button homeButton = showAlert.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showAlert.close();
			}
		});
		return true;
		
	}

	protected void addListener() {


		//R0953
		/*txtDoctorNote.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				// Assuming that the value type is a String
				String value = (String) event.getProperty().getValue();

				txtDoctorNote.setDescription(value);
			}
		});*/
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
					Boolean isInvsPending = Boolean.FALSE;
					if(null != claimKey){
						isInvsPending = investigationService.getInvestigationPendingForClaim(claimKey,SHAConstants.TRANSACTION_FLAG_CASHLESS,bean);
					}
					 if(!isInvsPending){
						 viewDetails.setPreAuthKey(preauthKey);
						 if(!bean.getIsInvsRaised()){
							 viewDetails.getViewInvestigationDetailsForPreauth(claimKey, true,bean.getStrUserName(),bean.getStrPassword(),bean);
						 }
						 else{
							 alertMessageForInvestigation(claimKey);
						 }
				     }
				     else
				     {
				    	 String eMsg = "Investigation Request has already been initiated";
				    	 showErrorPopUp(eMsg);
				     }
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
				if(isChecked){

					if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() != null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated().equals(Boolean.FALSE)){

						if (bean != null && bean.getNewIntimationDTO() != null
								&& bean.getNewIntimationDTO().getKey() != null) {
							fireViewEvent(
									PreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
									isChecked, bean);
						}	
					}
					else
					{
						showErrorPopUp("FVR request is in process cannot initiate another request");
						fireViewEvent(
								PreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
								isChecked, bean);
						initiateFieldVisitRequestRadio.setValue(Boolean.FALSE);
					}				
				}

				else
				{
					initiateFieldVisitRequestRadio.setValue(Boolean.FALSE);
					if (bean != null && bean.getNewIntimationDTO() != null
							&& bean.getNewIntimationDTO().getKey() != null) {
						fireViewEvent(
								PreauthEnhancemetWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
								Boolean.FALSE, bean);

					}
				}
			}
		}
				);


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
						PreauthEnhancemetWizardPresenter.SPECIALIST_OPINION_RADIO_CHANGED,
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
							PreauthEnhancemetWizardPresenter.VIEW_BALANCE_SUM_INSURED_ENH_DETAILS,
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
						PreauthEnhancemetWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
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
						PreauthEnhancemetWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		/**
		* Requirement Number:R1118
		* For adding Others as additional LOV and text area in FVR Not Required Remarks
		* Modified By : GokulPrasath.A
		* Modified On : 07th MAR 2018
		*
		*/
		cmbFVRNotRequiredRemarks.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				fvrNotRequiredRemarksChange();
				
			}


		});
	}

	public void generateFieldBasedOnStatusKey(){


		if(bean.getStatusKey() != null){
			if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);

			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);

			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_REJECTION_EVENT,bean);		

			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ESCALATE_EVENT, null);

			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_REFERCOORDINATOR_EVENT,null);

			}else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)){

//				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT,null);  // CR R1313
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT,bean.getNewIntimationDTO().getPolicy().getProduct().getKey());

			} else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){

				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_AND_REJECT_EVENT,bean);

			}
			else if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){

//				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);
				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,bean.getNewIntimationDTO().getPolicy().getProduct().getKey());


			}

		}
		
	 }
   
   public void addListenerForUpdateOtherClaims(){
		this.updateOtherClaimDetailsUI.updateClaimDetailTableObj.dummyField
		.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				amountConsideredTable.setAlreadySettledAmount(updateOtherClaimDetailsUI.updateClaimDetailTableObj.dummyField.getValue());
				Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
				processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
			}
		});
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

		BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData
				.get("investigatorName");
		
		BeanItemContainer<SelectValue> behaviourHosCombValue = (BeanItemContainer<SelectValue>) referenceData
				.get("behaviourHosCombValue");
		
		this.medicalDecisionTableObj.setReferenceData(referenceData);

		cmbFVRNotRequiredRemarks.setContainerDataSource(fvrNotRequiredRemarks);
		cmbFVRNotRequiredRemarks.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");
		cmbFVRNotRequiredRemarks.setNullSelectionAllowed(false);


		if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
			this.cmbFVRNotRequiredRemarks.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
		}
		
		fvrNotRequiredRemarksChange();

		investigatorName.setContainerDataSource(investigatorNameContainer);
		investigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		investigatorName.setItemCaptionPropertyId("investigatorName");

		if (this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null) {
			this.investigatorName.setValue(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName());
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

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
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
			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					this.bean.getNewIntimationDTO().getInsuredPatient()
					.getInsuredId().toString(), this.bean.getPolicyDto()
					.getKey(),this.bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
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
							PreauthEnhancemetWizardPresenter.SUM_INSURED_CALCULATION,
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
			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
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
							PreauthEnhancemetWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,bean);
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
						PreauthEnhancemetWizardPresenter.GET_PREAUTH_REQUESTED_AMOUT,
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
		
		/*dto.setCoPayType(procedureDTO.getCoPayTypeId());*/

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
		
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS) ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS) ||
				(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_CONTINUITY_PRODUCT_CODE) &&
						this.bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE))){
			
			dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
			dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
			dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
		}
		
		//IMSSUPPOR-23386
		SelectValue coPayValue = null;
		if(pedValidationTableDTO.getPedList().size() > 1) {
			coPayValue = pedValidationTableDTO.getPedList().get(0).getCopay(); 
			Double maxValue = pedValidationTableDTO.getPedList().get(0).getCopay() != null ? ((pedValidationTableDTO.getPedList().get(0).getCopay().getValue().isEmpty()) ? 0d : Double.valueOf(pedValidationTableDTO.getPedList().get(0).getCopay().getValue())) : 0d;
			if(pedValidationTableDTO.getPedList() != null && !pedValidationTableDTO.getPedList().isEmpty()){

				for (PedDetailsTableDTO pedDetailDto : pedValidationTableDTO.getPedList()) {
					if(pedDetailDto.getCopay() != null && pedDetailDto.getCopay().getValue() != null && !pedDetailDto.getCopay().getValue().isEmpty()){
						if(maxValue < Double.valueOf(pedDetailDto.getCopay().getValue())){
							maxValue = Double.valueOf(pedDetailDto.getCopay().getValue());
							coPayValue = pedDetailDto.getCopay();

						}
					}
				}
			}}
		
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
						if(exclusionAllDetails != null){
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
		
		/*dto.setCoPayType(pedValidationTableDTO.getCoPayTypeId());*/

		if(isZeroAppAmt) {
			dto.setIsEnabled(false);

		}

		if(pedValidationTableDTO.getPedList().size() == 1) {
			dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
		}else{
			if(coPayValue != null){
				dto.setCoPayPercentage(coPayValue);
			}
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
		StringBuffer eMsg = new StringBuffer();
		
		bean.setAmountConsideredForClaimAlert(processPreauthButtonObj.getApprovedAmount());

		if (!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}	
//		if(null !=this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
		if(null != initiateFieldVisitRequestRadio.getValue() && initiateFieldVisitRequestRadio.getValue().toString() == "true"){
			if(triggerPtsTableObj != null){
				if(!triggerPtsTableObj.isValid()){
					eMsg.append("Please Provide atleast one FVR Trigger Point.</br>");
					eMsg.append("FVR Trigger Points size will be Max. of 600.</br>");
					hasError = true;
				}
				else{
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());					
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
			}
		}
		
		else{
			   fireViewEvent(PreauthEnhancemetWizardPresenter.CHECK_ENH_FVR_NOT_REQ_REMARKS_VALIDATION, bean);
			   if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiatedMA() ){
				   cmbFVRNotRequiredRemarks.setRequired(false);
				   cmbFVRNotRequiredRemarks.setValidationVisible(true);
				   mandatoryFields.remove(cmbFVRNotRequiredRemarks);
				   hasError = false;
			   }
			   else{
								
				if(cmbFVRNotRequiredRemarks.getValue() == null && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy()))
				{
					if(null == bean.getFvrAlertFlag() || !(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag()))){
							hasError = true; 
							eMsg.append("Please Choose FVR Not Required Remarks.</br>");
					}
					else
					{
						if(cmbFVRNotRequiredRemarks.getValue() == null){
							hasError = true;
							eMsg.append("Please Choose FVR Not Required Remarks.</br>");
						}
						else{
							hasError = false;
						}
					}
				}else if(null != cmbFVRNotRequiredRemarks.getValue() && cmbFVRNotRequiredRemarks.getValue().toString().equalsIgnoreCase("Others")){
					
					if(fvrOthersRemarks.getValue() == null || ("").equalsIgnoreCase(fvrOthersRemarks.getValue()) || fvrOthersRemarks.getValue().isEmpty()){
						hasError = true;
						eMsg.append("Please Enter Remarks for Others.</br>");
					}
				}
				else{
					mandatoryFields.remove(cmbFVRNotRequiredRemarks);
					hasError = false;
				}				
				
			   }
		}

		if (!this.processPreauthButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.processPreauthButtonObj.getErrors();
			for (String error : errors) {
				eMsg.append(error);
			}
		}

		if( bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){

			if (!bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
					SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
				hasError = true;
				eMsg.append("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>");
			}
		}

		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){

				hasError = true;
				eMsg.append("Amount Entered against Ambulance charges should be equal");
				
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
		/*List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
=======

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
>>>>>>> FVRChanges
		if(fvrGradingDTO != null && ! fvrGradingDTO.isEmpty()){
			for(int i=0;i<fvrGradingDTO.size();i++){
				FvrGradingDetailsDTO  listValues = fvrGradingDTO.get(i);
				List<FVRGradingDTO> gradingValues = listValues.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDetailsDTO : gradingValues) {
					if(!(fvrGradingDetailsDTO.getStatusFlag() != null)) {
						hasError = true;
						int val = i+1;
						eMsg.append("Please Select ").append(val).append(" FVR Grading and set the Status </br>");
						break;
					}
				}
<<<<<<< HEAD
					
				}
		}*/
		
		if(null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()){
			List<DiagnosisProcedureTableDTO> medicalDecisionTableValues = this.medicalDecisionTableObj.getValues();
			if(null != medicalDecisionTableValues && !medicalDecisionTableValues.isEmpty()){
				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableValues) {
					if(diagnosisProcedureTableDTO.getCoPayType() == null){
						
						hasError = true;
						eMsg.append("Please Select Co_Pay Type </br>");
						break;
					}
				}
			}
		}
		
		if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) && bean.getIsPending() 
				&& !"Approve".equalsIgnoreCase(bean.getPreauthMedicalProcessingDetails().getVbApprovalStatus())) {
			hasError = true;
			eMsg.append("Cheque Status is Pending. Hence this Preauth Enhancement Can not be submitted.");
		}

		/*if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg.append("Please Enter Investigation Remarks and Name</br>");
		}*/
		int invCount = 0;
		if(this.bean.getStatusKey() != null && (ReferenceTable.invsAlertRequiredStatus()).containsKey(this.bean.getStatusKey())){
			if(null != bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty())
			{
				for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {			
				
					if(null == invsObj.getReportReviewed() || !(invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG))){
						hasError = true;
						eMsg.append("Investigation Review required").append("</br>");
						break;
					}
					invCount++;
				}
				
				if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size() == invCount){
					for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) 
					{		
						if((null != invsObj.getReportReviewed() && invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG)) &&
								(null == invsObj.getReviewRemarks() || ("").equalsIgnoreCase(invsObj.getReviewRemarks()))){
							hasError = true;
							if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size()== 1){
								eMsg.append("Investigation Review Remarks required for Selected Item").append("</br>");
								break;
							}
							else
							{
								eMsg.append("Investigation Review Remarks required for Selected Items").append("</br>");
								break;
							}
						}
					}
				}
			}
		}

		if(! hasError && ! this.bean.getIsRechargePopUpOpened()){

			/*if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null &&
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y")
					&& !(this.bean.getNewIntimationDTO().getPolicy().getLobId() != null && this.bean.getNewIntimationDTO().getPolicy().getLobId().equals(ReferenceTable.PACKAGE_MASTER_VALUE))){*/
			
			
			if((this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null &&
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y"))
				|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag() != null &&
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag().equalsIgnoreCase("Y"))) {

				if(approvedAmtField != null && approvedAmtField.getValue() != null && ! ("").equals(approvedAmtField.getValue())){
					Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
					Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
					if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
							((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")))) {
							approvedAmount = amountConsideredTable.getApprovedAmountForRecharge();
					}
					this.bean.setSublimitAndSIAmt(approvedAmount);
					Double balanceSI = this.bean.getBalanceSI();
					if(approvedAmount > balanceSI){

						if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null 
								&& ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
										|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))*/
											(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
												&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
														SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
														|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) 
														|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
												&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
										|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
												&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
														|| SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){

							if(bean.getPreauthDataExtractionDetails() != null 
									&& null != bean.getPreauthDataExtractionDetails().getCauseOfInjury() 
									&& null != bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()
									&& this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag() != null 
									&& this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag().equalsIgnoreCase("Y"))
							{								
								if((ReferenceTable.ROAD_TRAFFIC_ACCIDENT).equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()) &&
										(ReferenceTable.INJURY_MASTER_ID).equals(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))

								{
									fireViewEvent(PreauthEnhancemetWizardPresenter.RTA_RECHARGE_SI_FOR_PRODUCT, this.bean);
									getAlertMessage("Additional Sum Insured for RTA has been triggered – View RTA Sum insured  tab for more details");
								}
							}else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag() != null &&
										this.bean.getNewIntimationDTO().getPolicy().getProduct().getRtaRechargeSiFlag().equalsIgnoreCase("Y")) {
								fireViewEvent(PreauthEnhancemetWizardPresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
								Double balanceSI2 = this.bean.getBalanceSI();
								Double rechargedAmount = balanceSI2 - balanceSI;
								getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
							}
							this.bean.setIsRechargePopUpOpened(true);
							setApprovedAmtvalue();
							return false;
						}else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null
									&& this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y")){
							if(ReferenceTable.getSuperSurplusKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && 
											bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))){
								SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
								if(! (sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null && sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE))){
									fireViewEvent(PreauthEnhancemetWizardPresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
									Double balanceSI2 = this.bean.getBalanceSI();
									Double rechargedAmount = balanceSI2 - balanceSI;
									getAlertMessage("Balance Sum Insred Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
									setApprovedAmtvalue();
									this.bean.setIsRechargePopUpOpened(true);
									amountConsideredTable.txtAfterDefinedLimit.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
									return false;
								}
							}else if (! ReferenceTable.getSuperSurplusKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								fireViewEvent(PreauthEnhancemetWizardPresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
								Double balanceSI2 = this.bean.getBalanceSI();
								Double rechargedAmount = balanceSI2 - balanceSI;
								getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
								setApprovedAmtvalue();
								this.bean.setIsRechargePopUpOpened(true);
								return false;
							}
							
						}
						
					}

				}
			}
		}


		if(bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalReverseAllocatedAmt().equals(SHAUtils.getIntegerFromString(this.bean.getReverseAmountConsidered())  )) {
			hasError = true;
			eMsg.append("Total Final Approval Amt (B - Balance SI after Co-pay) Should be equal to Reverse column of Sub limits, Package & SI Restriction Table </br>");
		} else if(!hasError && !bean.getIsReverseAllocation() && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
			if(processPreauthButtonObj != null) {
				Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(processPreauthButtonObj.getApprovedAmount());
				Integer cValue = SHAUtils.getIntegerFromStringWithComma(this.medicalDecisionTableObj.dummyField.getValue()); 

				if(!cValue.equals(approvedAmt)) {
					hasError = true;
					if(!this.bean.getIsReverseAllocation()) {
						eMsg.append("Please enter Reverse Allocation Amount.");
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
				if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
						bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) {
					amt = processPreauthButtonObj.getHospitalAmtForUnique().intValue();
				}

				if(amt <= 0) {

					if((bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) && !ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						hasError = false;
					} else {
						hasError = true;
						eMsg.append("Approved Amount is Zero. Hence this Preauth can not be submitted. ");
					}

				}
			}
		}

		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.</br>");
		}
		
		//New FVR GRADING SEG A,B&C
		List<FvrGradingDetailsDTO> fvrBGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(fvrBGradingDTO != null && !fvrBGradingDTO.isEmpty()) {
			int i=0;
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO) {
				i++;
					if(fvrGradingDetailsDTO.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentBNotEdit() && fvrGradingDetailsDTO.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentANotEdit()){
						List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
						for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
								if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B) && fvrGradingDTO3.getSelectFlag() == null) {
									hasError = true;
									eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
									break;
								}else if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A) && fvrGradingDTO3.getCheckFlagA() == null){
									hasError = true;
									eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
									break;
								}
						}
					}else if(fvrGradingDetailsDTO.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentCNotEdit()){

						List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
						Boolean isAnySegmentCSelected = false;
						for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
								if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C) && (fvrGradingDTO3.getCheckFlag() != null && fvrGradingDTO3.getCheckFlag().equals(Boolean.TRUE))) {
									isAnySegmentCSelected = true;
									break;
								}
						}
						
						if(!isAnySegmentCSelected){
							hasError = true;
							eMsg.append("Please Select Any SEGMENT C in FVR Grading "+i+". </br>");
						}
						
						else if(fvrGradingTableObj.getSegmentCListenerTableSelectCount() > 1 ){
							hasError = true;
							eMsg.append("Please Select only one Check box value in SEGMENT C. </br>");
							break;
						}
					}
				else{
					hasError = true;
					eMsg.append("Please Select Any SEGMENT in FVR Grading "+i+". </br>");
				}
					
				if (fvrGradingTableObj != null) {
					Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj
							.getGradingRemarks();
					if (tableItem != null && !tableItem.isEmpty()) {
						if (fvrGradingDetailsDTO.getIsFVRReplied() != null
								&& fvrGradingDetailsDTO.getIsFVRReplied()
								&& fvrGradingDetailsDTO.getKey() != null) {
							if (tableItem.get(fvrGradingDetailsDTO.getKey()) == null
									&& fvrGradingDetailsDTO.getGradingRemarks() == null) {
								hasError = true;
								eMsg.append("Please Enter Grading Suggestion in FVR Grading "
										+ i + ". </br>");
							} else {
								TextArea gradeRmrks = (TextArea) tableItem
										.get(fvrGradingDetailsDTO.getKey());
								if (gradeRmrks != null
										&& (gradeRmrks.getValue() == null || gradeRmrks
												.getValue().isEmpty())) {
									hasError = true;
									eMsg.append("Please Enter Grading Suggestion in FVR Grading "
											+ i + ". </br>");
								}
							}
						}
					}
				}
				
			}
		}
		
		// GLX2020047
		String errMsg = medicalDecisionTableObj.isValidForPkgChange();

		if (errMsg != null && !errMsg.isEmpty()) {
			hasError = true;
			eMsg.append(errMsg + "<br>");
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
			List<NoOfDaysCell> copyClaimeDetailsList = SHAUtils.copyClaimeDetailsList(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList());
			this.bean.getPreauthDataExtractionDetails().setClaimedDetailsListForBenefitSheet(copyClaimeDetailsList);
		}
		
		//IMSSUPPOR-26097
		if(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
        		&& this.bean.getPreauthMedicalDecisionDetails().getDownsizedAmt() != null
        		&& (this.bean.getStatusKey() != null && (this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))&& (!ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))){
        	Double TotalApprovedAmount = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
        	Double downsizeAmt = this.bean.getPreauthMedicalDecisionDetails().getDownsizedAmt();
        	
        	if(TotalApprovedAmount <= downsizeAmt){
        		hasError = true;
        		eMsg.append("Downsize amount should be less than Preauth approved Amount. </br>");
        	}
        }
		if(! hasError){
			
			Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(bean.getStageKey(), bean.getStatusKey(), this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue():0l, 
					bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey(), bean.getKey(), "C",getUI());
			if(alertMessageForProvisionValidation){
				hasError = true;
				return !hasError;
			}
		}
		
		//CR20181338
		/*if(!hasError) {
			if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade() != null &&
					bean.getPreauthMedicalDecisionDetails().getNegotiationMade() && bean.getPreauthMedicalDecisionDetails().getNegotiationAmount() == null){
				hasError = true;
				eMsg.append("Please Enter Negotiation Amount.</br>");
			}
		}
		
		if(!hasError) {
			if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade() != null 
					&& bean.getPreauthMedicalDecisionDetails().getNegotiationMade() 
					&& bean.getPreauthMedicalDecisionDetails().getNegotiationAmount() < bean.getPreauthDataExtractionDetails().getPreauthTotalApprAmt()){
				hasError = true;
				eMsg.append("Entered Negotiate Amount is less than the pre-auth approved.</br>");
			}
		}*/
		
		if (cmbBehaviourHosp.getValue() == null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getMediBuddy().equals(1) ) {
			
			if(cmbBehaviourHosp.getValue() == null){
				hasError = true;
				eMsg.append("Please select a value from the behaviour of hospital LOV to proceed Further.</br>");
			}
			
		}

		if (hasError) {/*
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);*/
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();

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

				
				/*if(("f").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())
						&& this.bean.getPreauthMedicalDecisionDetails().getIsBeneifitSheetAvailable() != null 
						&& this.bean.getPreauthMedicalDecisionDetails().getIsBeneifitSheetAvailable()){
					if(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
						List<NoOfDaysCell> copyClaimeDetailsList = SHAUtils.copyClaimeDetailsList(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList());
						this.bean.getPreauthDataExtractionDetails().setClaimedDetailsListForBenefitSheet(copyClaimeDetailsList);
					}

				}else{
					                  
					if(! this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
						this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().clear();
					}
				}*/

				SHAUtils.doFinalEnhancementCalculationForLetter(this.medicalDecisionTableObj.getValues(), SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.getCoPayValue()) , bean);
				
				if(fvrGradingTableObj != null){
					Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj.getGradingRemarks();
					List<FvrGradingDetailsDTO> fvrBGradingDTO1 = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
					if(tableItem != null && !tableItem.isEmpty()){
						for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO1) {
							TextArea gradingRmrk = (TextArea) tableItem.get(fvrGradingDetailsDTO.getKey());
							if(gradingRmrk != null && gradingRmrk.getValue() != null){
								fvrGradingDetailsDTO.setGradingRemarks(gradingRmrk.getValue());
							}
						}
					}
				}
				
				//R1152
				if(!this.bean.getIsGeoSame()){
					if(null != bean.getIsPreauthAutoAllocationQ() && !bean.getIsPreauthAutoAllocationQ()){
						if(null != bean.getStatusKey() && !ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getStatusKey())  && bean.isAllowedToNxt()){
							getGeoBasedOnCPU();
						}
					}
				}
				
				fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
				
				if(negotiationOpinionTaken != null && ! negotiationOpinionTaken.isEnabled()){
					bean.getPreauthMedicalDecisionDetails().setIsValidNegotiation(Boolean.TRUE);
				}
				
                 fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_APPROVED_DATE, bean);
				
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

	public void getAlertMessage(String eMsg){/*

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
	*/
		
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();
	}


	@Override
	public boolean onAdvance() {
		bean.setAmountConsideredForClaimAlert(processPreauthButtonObj.getApprovedAmount());
		fireViewEvent(
				PreauthEnhancemetWizardPresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey(),this.bean);
		if(!bean.isAllowedToNxt()) {
			return false;
		}
		
		setResidualAmtToDTO();

		setCordinatorDetails();

		if (this.bean.getIsReferTOFLP()) {
			return true;
		}
		
		if(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey())){
			return true;
		}

		if(validatePage()){
			if(this.bean.getStatusKey()!=null && this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
				if(bean.getPreauthMedicalDecisionDetails()!=null && bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()!=null){
					/*Double totalApprAmt = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
					if(totalApprAmt == null){
						totalApprAmt=0d;
					}*/
					Double totalApprovedAmount = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()/* + totalApprAmt*/;
					if(totalApprovedAmount>=SHAConstants.ONE_LAKH){

						fireViewEvent(PreauthEnhancemetWizardPresenter.CHECK_PAN_CARD_DETAILS,this.bean.getPolicyKey());
					}
				}}
			if(this.bean.getStatusKey()!=null && this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){  
				if(bean.getPreauthMedicalDecisionDetails()!=null && bean.getPreauthMedicalDecisionDetails().getDownsizedAmt()!=null){
					if(bean.getPreauthMedicalDecisionDetails().getDownsizedAmt()>=SHAConstants.ONE_LAKH){
						fireViewEvent(PreauthEnhancemetWizardPresenter.CHECK_PAN_CARD_DETAILS,this.bean.getPolicyKey());
					}
				}}
			if(! bean.getAlertMessageForCopay() && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
					warningMessageForCopay();
				}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
					alertMessageForCopay();
				}else{
					return true;
				}
			}else{
				return true;
			}

		}

		return false;
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
		
		this.bean.setIsReverseAllocation(false);
		this.bean.setPreauthHoldStatusKey(null);
		
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
			
			if(bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag() != null && bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag() && bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
				triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
			}
			else{
				ViewFVRDTO trgptsDto = null;
		    	List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
		    	for(int i = 1; i<=5;i++){
		    		trgptsDto = new ViewFVRDTO();
		    		trgptsDto.setRemarks("");
		    		trgptsList.add(trgptsDto);
		    	}
		    	triggerPtsTableObj.setTableList(trgptsList);
			}

			
			unbindField(cmbAllocationTo);
			
			if(fvrDTOList != null && !fvrDTOList.isEmpty()){
				
				bean.getPreauthMedicalDecisionDetails().setAllocationTo(fvrDTOList.get(0).getAllocationTo());
				bean.getPreauthMedicalDecisionDetails().setPriority(fvrDTOList.get(0).getPriority());
				bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
			}
			
			cmbAllocationTo = (ComboBox) binder.buildAndBind("Allocation To",
					"allocationTo", ComboBox.class);
			cmbAllocationTo
			.setContainerDataSource((BeanItemContainer<SelectValue>) dropdowValues);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			
			unbindField(cmbFvrPriority);
			
			cmbFvrPriority = (ComboBox) binder.buildAndBind("Priority",
					"priority", ComboBox.class);
			cmbFvrPriority
			.setContainerDataSource((BeanItemContainer<SelectValue>) fvrprioirty);
			cmbFvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrPriority.setItemCaptionPropertyId("value");

			
			//setRequiredAndValidation(cmbFvrAssignTo);
			setRequiredAndValidation(cmbFvrPriority);
//			setRequiredAndValidation(fvrTriggerPoints);
			setRequiredAndValidation(cmbAllocationTo);

			unbindField(cmbFVRNotRequiredRemarks);
			unbindField(fvrOthersRemarks);

//			mandatoryFields.add(fvrTriggerPoints);
			mandatoryFields.add(cmbAllocationTo);
			//mandatoryFields.add(cmbFvrAssignTo);
			mandatoryFields.add(cmbFvrPriority);

			mandatoryFields.remove(cmbFVRNotRequiredRemarks);
			fvrFieldFLayout.removeComponent(cmbFVRNotRequiredRemarks);
			mandatoryFields.remove(fvrOthersRemarks);
			fvrFieldFLayout.removeComponent(fvrOthersRemarks);

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
			//fvrFieldFLayout.addComponent(cmbFvrAssignTo);
			fvrFieldFLayout.addComponent(cmbFvrPriority);
			
			if(triggerPtsTable != null){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			fvrVertLayout.addComponent(triggerPtsTableObj);
			
			if(null != bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived() && !bean.getPreauthMedicalDecisionDetails().getIsFvrReplyReceived()){
					if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty() ){
						triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
					}
					if (this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null) {
						this.cmbAllocationTo.setValue(this.bean
								.getPreauthMedicalDecisionDetails().getAllocationTo());
					}
					if (this.bean.getPreauthMedicalDecisionDetails().getPriority() != null) {
						this.cmbFvrPriority.setValue(this.bean
								.getPreauthMedicalDecisionDetails().getPriority());
					}
				}
			
		} else {

			fvrFieldFLayout.removeComponent(cmbFVRNotRequiredRemarks);
			unbindField(cmbFVRNotRequiredRemarks);
			
			fvrFieldFLayout.removeComponent(fvrOthersRemarks);
			unbindField(fvrOthersRemarks);	
			
			cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
					"FVR Not Required Remarks", "fvrNotRequiredRemarks",
					ComboBox.class);
			cmbFVRNotRequiredRemarks.setNullSelectionAllowed(false);
			BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
					.get("fvrNotRequiredRemarks");

			cmbFVRNotRequiredRemarks
			.setContainerDataSource(fvrNotRequiredRemarks);
			cmbFVRNotRequiredRemarks
			.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");
			
			fvrOthersRemarks = (TextArea) binder.buildAndBind(
					"Enter remarks for Others", "fvrNotRequiredOthersRemarks",
					TextArea.class);
			fvrOthersRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
			fvrOthersRemarks.setMaxLength(230);
			handleTextAreaPopup(fvrOthersRemarks,null);
			
			
			if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
				this.cmbFVRNotRequiredRemarks.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
			}
			
			mandatoryFields.add(cmbFVRNotRequiredRemarks);
			mandatoryFields.add(fvrOthersRemarks);
//			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			//unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			setRequiredAndValidation(cmbFVRNotRequiredRemarks);
			setRequiredAndValidation(fvrOthersRemarks);
//			if(fvrTriggerPoints != null){
//
//			fvrFieldFLayout.removeComponent(fvrTriggerPoints);
//			}
			if(null != bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks()){
				cmbFVRNotRequiredRemarks.setValue(bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
			}
			if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredOthersRemarks()!= null) {
				this.fvrOthersRemarks.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getFvrNotRequiredOthersRemarks());
			}
			
			if(triggerPtsTable != null){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}	
			
			if(cmbAllocationTo !=null){
				fvrFieldFLayout.removeComponent(cmbAllocationTo);
			}

			if(cmbFvrPriority != null){
				fvrFieldFLayout.removeComponent(cmbFvrPriority);
			}

			if(lblNoOfFvr !=null){
				fvrHorizontalLayout.removeComponent(lblNoOfFvr);
			}if(btnViewFVRDetails!=null){
				fvrHorizontalLayout.removeComponent(btnViewFVRDetails);
			}
			
//			mandatoryFields.remove(fvrTriggerPoints);
			mandatoryFields.remove(cmbAllocationTo);
			//mandatoryFields.remove(cmbFvrAssignTo);
			mandatoryFields.remove(cmbFvrPriority);

			fvrFieldFLayout.addComponent(cmbFVRNotRequiredRemarks);
			mandatoryFields.add(cmbFVRNotRequiredRemarks);

			/**
			* Requirement Number:R1118
			* For adding Others as additional LOV and text area in FVR Not Required Remarks
			* Modified By : GokulPrasath.A
			* Modified On : 07th MAR 2018
			*
			*/
			cmbFVRNotRequiredRemarks.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 2697682747976915503L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					
					fvrNotRequiredRemarksChange();
					
				}


			});
			
		}
	}
	
	public void generateFieldsForAssignFieldVisit(boolean isFVRAssigned, String repName, String repContactNo){
		this.isFVRAssigned = isFVRAssigned;
		
		if(isFVRAssigned){
		
			fvrAssignFLayout = new FormLayout();
			
			Label repNameLbl = new Label("Representative Name :  <b>"+ repName + "</b>",ContentMode.HTML);
			Label repContactNoLbl = new Label("Contact No :  <b>"+ repContactNo + "</b>",ContentMode.HTML);
			
			fvrAssignFLayout.addComponents(repNameLbl, repContactNoLbl);
			
			if(fvrHLayout != null){
				fvrHLayout.removeAllComponents();
			}else{
			
				fvrHLayout = new HorizontalLayout(fvrVertLayout);
			}
			if(this.isFVRAssigned){
				fvrHLayout.addComponents(fvrVertLayout,fvrAssignFLayout);
			}
			
		}
		
	}
	
	private void fvrNotRequiredRemarksChange() {
		if(null != cmbFVRNotRequiredRemarks && cmbFVRNotRequiredRemarks.getValue() != null && ((SelectValue)cmbFVRNotRequiredRemarks.getValue()).getValue().equalsIgnoreCase("others")) {
								if(fvrOthersRemarks != null){
									/*fvrOthersRemarks.setValue(StringUtils.EMPTY);*/
									fvrFieldFLayout.removeComponent(fvrOthersRemarks);
									
								}
								fvrFieldFLayout.addComponent(fvrOthersRemarks);
								
							} else {
								fvrOthersRemarks.setValue(StringUtils.EMPTY);
								bean.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(null);
								fvrFieldFLayout.removeComponent(fvrOthersRemarks);
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

			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				min = Math.min(
						amountConsideredTable.getMinimumValueForGMC(),
						SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
								.getValue()));

				if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
					Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
					Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
					if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
						bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
					}

				}

			}
			else{
				if(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() != null 
						&& bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement())
					bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
				else {
					bean.getPreauthMedicalDecisionDetails().setIsBeneifitSheetAvailable(true);
				}
			}

			//added for CR2019184 policy instalment handling
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				Long input1 =0l;
				String input2 = null;
				java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
				java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();

				java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
				java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
				DBCalculationService dbCalculationService = new DBCalculationService();
				Map<String, String> getPolicyInstallmentDetails = dbCalculationService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
						bean.getPolicyInstalmentPremiumAmt(),admissionDate,amountConsideredTable.getMinimumValue().doubleValue(),
						instalmentDate,input1,input2);
				if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
					bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
					bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
				}
				if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
					this.processPreauthButtonObj.disableApprove(false);
					SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
				} else {
					this.processPreauthButtonObj.disableApprove(true);
					this.processPreauthButtonObj.buildApproveLayout(min);
				}
			}	
			else if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				if(bean.getPreauthMedicalDecisionDetails().getUniquePremiumAmt() >= min.doubleValue()) {
					this.processPreauthButtonObj.disableApprove(false);
					alertMessageForUniquePremium();
				} else {
					this.processPreauthButtonObj.disableApprove(true);
					this.processPreauthButtonObj.buildApproveLayout(min);
				}
			} else {
				this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
				this.processPreauthButtonObj.buildApproveLayout(min);
			}
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
					/*min = Math.min(amountConsideredTable.getConsideredAmountValue(), SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
							.getValue()));*/
					/*amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					this.processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doSuperSurplusCalculation());*/
					setApprovedAmtvalue();
				} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					/*min = Math.min(amountConsideredTable.getConsideredAmountValue(), SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
							.getValue()));
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
					this.processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doRevisedSuperSurplusCalculation());*/
					setApprovedAmtvalue();
				}
			}


			// this.medicalDecisionTableObj.getTableItemIds();
			// this.medicalDecisionTableObj.setVisibleApproveFields(true);
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
			this.bean.setPreauthHoldStatusKey(null);
			break;
		case 2:
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
			this.processPreauthButtonObj.buildQueryLayout();
			this.bean.setPreauthHoldStatusKey(null);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			break;
		case 3:
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
			this.processPreauthButtonObj.buildRejectLayout(dropDownValues);
			this.bean.setPreauthHoldStatusKey(null);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			break;
		case 4:
			this.bean
			.setStatusKey(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
			this.processPreauthButtonObj.buildWithdrawLayout(dropDownValues);
			this.bean.setPreauthHoldStatusKey(null);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setProcessType("W");
			break;
		case 5:
			this.processPreauthButtonObj.buildEscalateLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
			.setStatusKey(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS);
			this.bean.setPreauthHoldStatusKey(null);
			break;
		case 6:
			this.processPreauthButtonObj
			.buildReferCoordinatorLayout(referenceData
					.get("coordinatorTypeRequest"));
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
			.setStatusKey(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
			this.bean.setPreauthHoldStatusKey(null);
			break;
		case 7:
			this.bean
			.setStatusKey(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
			this.bean.setProcessType("D");
			//			this.processPreauthButtonObj.clearFields();
			/*Integer minValue = null;
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				minValue = Math.min(
						amountConsideredTable.getMinimumValueForGMC(),
						SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
								.getValue()));
			}else{
				 minValue = Math
							.min(amountConsideredTable.getMinimumValue(),
									SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
											.getValue()));
			}
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
			}*/
			referenceData.put("downsizeReason", dropDownValues);
			
			Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
			//added for CR2019184 policy instalment handling
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				Long input1 =0l;
				String input2 = null;
				Integer minValue = null;
				 minValue = Math
							.min(amountConsideredTable.getMinimumValue(),
									SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
											.getValue()));
				java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
				java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();

				java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
				java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
				DBCalculationService dbCalculationService = new DBCalculationService();
				Map<String, String> getPolicyInstallmentDetails = dbCalculationService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
						bean.getPolicyInstalmentPremiumAmt(),admissionDate,  Double.valueOf(minValue),
						instalmentDate,input1,input2);
				if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
					bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
					bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
				}
				if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
					this.processPreauthButtonObj.disableDownsize(false);
					SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
				} else {
					this.processPreauthButtonObj.disableDownsize(true);
					this.processPreauthButtonObj.buildDownsizeLayout(minValue,
							dropDownValues);
				}
			}	
			else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey())) {
				setApprovedAmtvalue();
			}
			else {
				Integer minValue = null;
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					minValue = Math.min(
							amountConsideredTable.getMinimumValueForGMC(),
							SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
									.getValue()));
				}else{
					 minValue = Math
								.min(amountConsideredTable.getMinimumValue(),
										SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
												.getValue()));
				}
				this.processPreauthButtonObj.buildDownsizeLayout(minValue,
						dropDownValues);
			}
			
			this.bean.setPreauthHoldStatusKey(null);
			// this.medicalDecisionTableObj.setVisibleDownSizeFields(true);
			break;
		case 8:
			this.processPreauthButtonObj.build64VBCompliance(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_REFER_64_VB_COMPLIANCE);
			this.bean.setPreauthHoldStatusKey(null);
			break;
		case 9:
			this.processPreauthButtonObj.buildCPUUserLayout(dropDownValues);
			this.bean.setStatusKey(ReferenceTable.ENHN_REFER_TO_CPU);
			this.bean.setPreauthHoldStatusKey(null);
			break;
		case 10:
			this.bean.setPreauthHoldStatusKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			this.processPreauthButtonObj.buildHoldLayout();
			this.bean.setStatusKey(bean.getOldStatusKey());
//			this.bean.setStatusKey(null);
			break;
		default:
			break;
		}

		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
				this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
				this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)){
			processPreauthButtonObj.enableOrDisable(false);
		}
	}

	public void generateWithdrawAndRejectButton(Object withdrawDropDownValues, Object rejectDropDownValues) {
		this.bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		this.bean.setProcessType("E");
		this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
		this.processPreauthButtonObj.buildWithdrawAndRejectLayout(withdrawDropDownValues, rejectDropDownValues);
	}
	
	public void generateWithdrawAndRejectButton(Object withdrawRejectDropDownValues) {
		this.bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
		this.bean.setProcessType("E");
		this.bean.setStatusKey(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
		this.processPreauthButtonObj.buildWithdrawAndRejectLayout(withdrawRejectDropDownValues);
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

				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					min = Math.min(
							amountConsideredTable.getMinimumValueForGMC(),
							totalApprovedAmt);

					if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
						Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
						Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
						if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
							bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
						}

					}
					
					//TOP-UP POLICY
					Integer minOfAandC = Math.min(
							amountConsideredTable.getConsideredAmountValue(),
							totalApprovedAmt);
					
					if(minOfAandC > amountConsideredTable.getGMCBalanceSumInsuredAmt()){
						processPreauthButtonObj.enableTopupPolicy(true);
					}else{
						processPreauthButtonObj.enableTopupPolicy(false);
					}

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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
								
								}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doRevisedSuperSurplusCalculation());
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
										processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doRevisedSuperSurplusCalculation());
										//bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										//processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
										//processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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

				if(bean.getNewIntimationDTO() != null) {
					Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
					if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
							bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
							bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)){
						processPreauthButtonObj.enableOrDisable(false);
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
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					min = Math.min(
							amountConsideredTable.getMinimumValueForGMC(),
							totalApprovedAmt);

					if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
						Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
						Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
						if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
							bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
						}

					}
					
					//TOP-UP POLICY
					Integer minOfAandC = Math.min(
							amountConsideredTable.getConsideredAmountValue(),
							totalApprovedAmt);
					
					if(minOfAandC > amountConsideredTable.getGMCBalanceSumInsuredAmt()){
						processPreauthButtonObj.enableTopupPolicy(true);
					}else{
						processPreauthButtonObj.enableTopupPolicy(false);
					}

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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());'
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(true);
										} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
											processPreauthButtonObj.enableOrDisable(false);
										}
									}
									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
										processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			min = Math.min(
					amountConsideredTable.getMinimumValueForGMC(),
					totalApprovedAmt);

			if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
				Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
				Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
				if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
					bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
				}

			}

		}
		if ((bean.getStatusKey()
				.equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS))) {
			//processPreauthButtonObj.buildApproveLayout(min);
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
					if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(true);
					} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(false);
					}
				} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
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
				} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(true);
					} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
						processPreauthButtonObj.enableOrDisable(false);
					}
				} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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

	public void warningMessageForCopay() {/*

		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: red;'>Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", ContentMode.HTML);

		//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);

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
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setAlertMessageForCopay(true);
				wizard.next();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

	*/


		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed?</b>", ContentMode.HTML);
		final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Selected Co-pay Percentage is Zero !!!!.\n Do you wish to Proceed.")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				bean.setAlertMessageForCopay(true);
				if(!(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey()))){
				wizard.next();
				}
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

		
	}

	public void alertMessageForCopay() {/*

		String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + "%"+"</br> Do you wish to Proceed.</b>";

		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);

		//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);

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
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setAlertMessageForCopay(true);
				wizard.next();
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});


	*/


		String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + "%"+"</br> Do you wish to Proceed.</b>";

		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);

		//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage( "Selected Co-pay Percentage is "+bean.getHighestCopay() + "%"+"\n Do you wish to Proceed")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			Button cancelButton=getConf.getButton(ButtonType.NO);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				bean.setAlertMessageForCopay(true);
				if(!(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey()))){
				wizard.next();
				}
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
				//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});


		
	}

	public void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList,Map<String, Object> referenceData) {

		Window popup = new com.vaadin.ui.Window();
		updateOtherClaimDetailsUI.init(updateOtherClaimDetailList, referenceData, bean,popup);
		addListenerForUpdateOtherClaims();
		popup.setCaption("Update Previous/Other Claim Details(Defined Limit)");
		popup.setWidth("100%");
		popup.setHeight("70%");
		popup.setContent(updateOtherClaimDetailsUI);
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

	public void setRemarks(MasterRemarks remarks, String decision) {
		processPreauthButtonObj.setRemarks(remarks,decision);

	}

	public void setEnhQryRemarks(String qryRemarks) {
		processPreauthButtonObj.setQryRemarks(qryRemarks);

	}
	
	public void setClearReferenceData(){
		/*medicalDecisionTable.destroy(medicalDecisionTableObj);
		processPreauthButtonLayout.destroy(processPreauthButtonObj);
		amountConsideredTableInstance.destroy(amountConsideredTable);*/
		SHAUtils.setClearReferenceData(referenceData);
		if(wholeVLayout!=null){
    		wholeVLayout.removeAllComponents();
    	}
	}

	public void isPanAvailable(Boolean isAvailable) {
		if(!isAvailable){
			showErrorPopUp("PAN Card Details not available");
		}

	}

	public void showErrorPopUp(String emsg) {/*
		Label label = new Label(emsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Warning");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	*/
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(emsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();	
	}

	private HorizontalLayout generateFieldBasedOnCorpBuffer(){

		String amountConsidered2 = this.bean.getAmountConsidered();
		Integer corpBufferAllocatedClaim = this.bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
		Integer availableAmt = 0;
		Integer integerFromStringWithComma = 0;
		if(amountConsidered2 != null && ! amountConsidered2.isEmpty()){
			integerFromStringWithComma = SHAUtils.getIntegerFromStringWithComma(amountConsidered2);
			if(integerFromStringWithComma != null){
				availableAmt = corpBufferAllocatedClaim - integerFromStringWithComma > 0 ? corpBufferAllocatedClaim - integerFromStringWithComma : 0;
			}

		}

		Table corpTable  = new Table();
		corpTable.setWidth("80%");
		corpTable.addContainerProperty("Particulars", String.class, null);
		corpTable.addContainerProperty("Amount",  Integer.class, null);
		//corpTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		corpTable.addItem(new Object[]{"Corporate Buffer Allocated for this claim", corpBufferAllocatedClaim },1);
		corpTable.addItem(new Object[]{"Amount claimed for this claim", integerFromStringWithComma },2);
		corpTable.addItem(new Object[]{"Corporate Buffer Balance Available for this Claim", availableAmt },3);
		corpTable.setPageLength(3);
		corpTable.setCaption("Corporate Buffer Details");

		Integer stopLossAvailableAmt = bean.getPreauthMedicalDecisionDetails().getStopLossAvailableAmt();

		Table stopLossTable  = new Table();
		stopLossTable.setWidth("80%");
		stopLossTable.addContainerProperty("Particulars", String.class, null);
		stopLossTable.addContainerProperty("Amount",  Integer.class, null);
		//stopLossTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		stopLossTable.addItem(new Object[]{"Stop Loss Amount Available(excl this claim)", stopLossAvailableAmt },1);
		stopLossTable.setPageLength(1);
		stopLossTable.setCaption("Stop Loss Details");

		HorizontalLayout mainHor = new HorizontalLayout(corpTable,stopLossTable,getBehaviouroHospital());
		mainHor.setSpacing(true);

		return mainHor;

	}
	
	public  void medicalRemarksListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForEnhnMedRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForMedicalRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}

	private ShortcutListener getShortCutListenerForMedicalRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
				if(("medicalRmrks").equalsIgnoreCase(txtFld.getId()) || ("internalRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("medicalRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(txtFld.getValue());
						}else if(("internalRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setDoctorNote(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("medicalRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Medical Remarks";
				}
			    else if(("internalRmrks").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Doctor Note(Internal Remarks)";
			    }
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
public void alertMessageForInvestigation(final Long claimKey) {/*

		
		String message = "Investigation already raised has been completed. </br>Do you want to proceed further?</br>";
		
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
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
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();	
				viewDetails.getViewInvestigationDetailsForPreauth(claimKey, true,bean.getStrUserName(),bean.getStrPassword(),bean);
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsInvestigation(false);
			}
		});
	
		
	*/


	final MessageBox getConf = MessageBox
		    .createQuestion()
		    .withCaptionCust("Confirmation")
		    .withMessage("Investigation already raised has been completed. \nDo you want to proceed further?")
		    .withYesButton(ButtonOption.caption("Yes"))
		    .withNoButton(ButtonOption.caption("No"))
		    .open();
		Button homeButton=getConf.getButton(ButtonType.YES);
		Button cancelButton=getConf.getButton(ButtonType.NO);
	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			getConf.close();	
			viewDetails.getViewInvestigationDetailsForPreauth(claimKey, true,bean.getStrUserName(),bean.getStrPassword(),bean);
		}
	});
	
	cancelButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			getConf.close();
			bean.setIsInvestigation(false);
		}
	});

	
	
}
public void addAdditionalFvrPointsListener() {
	addAdditionalFvrPointsBtn.addClickListener(new ClickListener() {
		private static final long serialVersionUID = -8159939563947706329L;

		@Override
		public void buttonClick(ClickEvent event) {
			final Window popup = new com.vaadin.ui.Window();
			
			addAdditionalFvrPointsPageUI.init(bean,popup);
		
			popup.setWidth("85%");
			popup.setHeight("60%");
			popup.setContent(addAdditionalFvrPointsPageUI);
			popup.setCaption("Add Additional FVR Trigger Points");
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
	});
}
	public void getGeoBasedOnCPU() {/*	 
	 
	 Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
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

		    final MessageBox showInfo= showInfoMessageBox(SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE);
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
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
      }

	public MessageBox showAlertMessageBox(String message){
      final MessageBox msgBox = MessageBox.createWarning()
			    .withCaptionCust("Warning") .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
		
	}

	public MessageBox showCritcalAlertMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createCritical()
			    .withCaptionCust("Critical Alert")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
	}
	 public MessageBox  showConfirmationMessageBox(String message){
		 final MessageBox msgBox =MessageBox
				 .createQuestion()
				 .withCaptionCust("Confirmation")
				 .withMessage(message)
				 .withYesButton(ButtonOption.caption(ButtonType.YES.name()))
				 .withNoButton(ButtonOption.caption(ButtonType.NO.name()))
				 .open();
		 return msgBox;
	 }

	/*@SuppressWarnings("unchecked")
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
	}*/
	
	/*protected void addNegotiationListener() {
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
						PreauthEnhancemetWizardPresenter.NEGOTIATION_OPINION_RADIO_CHANGED,
						isChecked);
			}
		});
	}*/
	
	/*@SuppressWarnings("unchecked")
	public void updateNegotiationRemarks(String updatedRemarks) {
		txtNegotiationRemarks.setReadOnly(false);
		txtNegotiationRemarks.setValue(updatedRemarks);
		txtNegotiationRemarks.setReadOnly(true);
	}*/
	 
	 public void setButtonsLayout(PreauthEnhancementButtons buttons){
		 buttons.initView(this.bean,this.wizard,this.medicalDecisionTableObj);
		 setApproveButtonByAmount();
		 
	 }
	 
	 public void setApproveButtonByAmount(){

		 if(this.medicalDecisionTableObj != null){
		 
			Integer totalApprovedAmt = SHAUtils
					.getIntegerFromString(this.medicalDecisionTableObj.dummyField.getValue());
			approvedAmtField.setValue(String
					.valueOf(totalApprovedAmt));
			Integer min = Math.min(
					amountConsideredTable.getMinimumValue(),
					totalApprovedAmt);

			if(bean.getIsNonAllopathic()) {
				min = Math.min(min, bean.getNonAllopathicAvailAmt());
			}

			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				min = Math.min(
						amountConsideredTable.getMinimumValueForGMC(),
						totalApprovedAmt);

				if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
					Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
					Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
					if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
						bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
					}

				}

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
							if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
									//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(true);
									} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(false);
									}
								}
							
							}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doRevisedSuperSurplusCalculation());
									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
								}  else {
									if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
									}
									Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
									if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
										Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
										amt =amt - doubleValueFromString;
									}
									//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
									processPreauthButtonObj.buildApproveLayout(amountConsideredTable.doRevisedSuperSurplusCalculation());
									//bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
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
							if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
									//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(true);
									} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(false);
									}
								}
							} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
									if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
										Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
										amt =amt - doubleValueFromString;
									}
									//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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
							if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									//processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
									//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.hospApprovedAmountTxt.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
									processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() > doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(true);
									} else if(bean.getPreviousPreauthPayableAmount() != null && bean.getPreviousPreauthPayableAmount() <= doSuperSurplusCalculation ) {
										processPreauthButtonObj.enableOrDisable(false);
									}
								}
								
							} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
									//processPreauthButtonObj.buildApproveLayout(doSuperSurplusCalculation);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(doSuperSurplusCalculation.doubleValue());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
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
									if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
										Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
										amt =amt - doubleValueFromString;
									}
									//amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
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

			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
						bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
						bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)){
					processPreauthButtonObj.enableOrDisable(false);
				}
			}						
		 }
		
	 }
	 
	 public void generateTopUpLayout(List<PreauthDTO> topupData){
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Top-up policy");
			popup.setWidth("35%");
			popup.setHeight("35%");
			viewTopUpGmcPage.init(topupData, bean, SHAConstants.ENHANCEMENT, popup,"HC_TOPUP");
			popup.setContent(viewTopUpGmcPage);
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
	 
	 public void setRejSugCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		 processPreauthButtonObj.setRejSubCategContainer(rejSubcategContainer);
	 }

	 public Long getApprovedAmtForRRC(){
		 if(medicalDecisionTableObj !=null){
			 return Long.valueOf(medicalDecisionTableObj.getTotalCAmount());
		 }
		 return 0l;
	 }
	 
	 public void setnegotiatedSavedAmount(Long addedDNPamoutn,Long currentClaimAmount){
		 Long coPayAmount=0L;
		 Long addedDCNAmt=0L;
		 if(medicalDecisionTableObj !=null){
			 coPayAmount =  medicalDecisionTableObj.getCopayAmountInDiagProd().longValue();
		 }
		 addedDCNAmt = addedDNPamoutn + coPayAmount;
		 if(processPreauthButtonObj !=null){
			 processPreauthButtonObj.setNegotiationSavedAmt(addedDCNAmt,currentClaimAmount);
		 }
	 }
	 
	 public void setEnhnAppAmountBSIAlert(){
		 if(processPreauthButtonObj !=null){
			 processPreauthButtonObj.setBalanceSumInsuredAlert(getApprovedAmtForRRC());
		 }
	 }
	 
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
	 
	 public void generateFieldsBasedOnSubCatTWO(BeanItemContainer<SelectValue> procedure) {
		 processPreauthButtonObj.generateFieldsBasedOnSubCatTWO(procedure);
	 }
	 
	 public void addpccSubCategory(BeanItemContainer<SelectValue> procedure) {
		 processPreauthButtonObj.addpccSubCategory(procedure);
	 }
}
