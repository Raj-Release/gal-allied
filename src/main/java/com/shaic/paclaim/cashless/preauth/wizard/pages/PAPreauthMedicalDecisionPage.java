package com.shaic.paclaim.cashless.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
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
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.SpecificProductPreviousClaims;
import com.shaic.claim.preauth.ViewClaimAmountDetils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.dto.SpecificProductDeductibleTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.paclaim.cashless.listenertables.PARevisedMedicalDecisionTable;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
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

public class PAPreauthMedicalDecisionPage extends ViewComponent implements
		WizardStep<PreauthDTO> {

	private static final long serialVersionUID = -6896085051045139562L;

	private static final SpecificProductDeductibleTableDTO preauthDTO = null;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	@Inject
	private Instance<PARevisedMedicalDecisionTable> medicalDecisionTable;

	@Inject
	private Instance<PAProcessPreAuthButtonLayout> processPreauthButtonLayout;

	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;

	@Inject
	private Instance<PAAmountConsideredTable> amountConsideredTableInstance;

	private PAAmountConsideredTable amountConsideredTable;

	@Inject
	private Instance<AmountConsideredTable> balanceSumInsuredTableInstance;

	@Inject
	private Instance<SpecificProductPreviousClaims> specifictProductDeductibleTable;

	private SpecificProductPreviousClaims specifictProductDeductibleTableObj;

	private AmountConsideredTable balanceSumInsuredTableObj;

	private PAProcessPreAuthButtonLayout processPreauthButtonObj;

	private PARevisedMedicalDecisionTable medicalDecisionTableObj;

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

	private TextArea txtMedicalRemarks;

	private TextArea txtDoctorNote;

	private VerticalLayout wholeVLayout;

	private FormLayout investigationFLayout;

	private FormLayout fvrFieldFLayout;
	
	private HorizontalLayout fvrHorizontalLayout;

	private FormLayout specialistFLayout;

	private Label preauthAmtRequested;

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private Label amountConsidered;

	private Label balanceSumInsured;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	Map<String, Object> referenceData;

	Map<String, Object> sublimitCalculatedValues;

	private Double balanceSumInsuredValue;

	private String diagnosisName;

	private Button initiatePEDButton;

	// Added for intiate field visit.

	private Button initiateInvestigationButton;

	private Double copayValue = 0d;

	@Inject
	private ViewDetails viewDetails;

	public Double value;

	private TextField approvedAmtField;

	private TextField consideredAmtField;
	
	private GWizard wizard;

	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;

	private VerticalLayout optionCLayout;

	private TextField nonAllopathicTxt;
	

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

		initiateInvestigationButton = new Button("Initiate Investigation");
		// FormLayout
		initiateInvestigationButton.setEnabled(false);
		HorizontalLayout buttonLayout = new HorizontalLayout(
				initiateInvestigationButton);
		buttonLayout.setComponentAlignment(initiateInvestigationButton,
				Alignment.MIDDLE_RIGHT);
		HorizontalLayout buttonHLayout2 = new HorizontalLayout(initiatePEDButton,initiateInvestigationButton);
		buttonHLayout2.setSpacing(true);
		initiateFieldVisitRequestRadio = (OptionGroup) binder.buildAndBind(
				"Initiate Field Visit Request",
				"initiateFieldVisitRequestFlag", OptionGroup.class);
		txtMedicalRemarks = (TextArea) binder.buildAndBind("Medical Remarks",
				"medicalRemarks", TextArea.class);

		txtMedicalRemarks.setMaxLength(4000);

		CSValidator validator = new CSValidator();
		// validator.extend(txtMedicalRemarks);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);
		txtDoctorNote = (TextArea) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextArea.class);
		txtDoctorNote.setMaxLength(4000);
		// validator = new CSValidator();
		// validator.extend(txtDoctorNote);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);
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
		investigationReviewRemarks.setMaxLength(100);	
			
		fvrFieldFLayout = new FormLayout(initiateFieldVisitRequestRadio,
				cmbFVRNotRequiredRemarks);
		fvrFieldFLayout.setMargin(false);
		if(null !=this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
			initiateFieldVisitRequestRadio.select(this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag());
			Boolean isCheck=true;
			if(!this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag()){
				isCheck=false;
			}
			
			fireViewEvent(
					PAPreauthWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
					isCheck, bean.getNewIntimationDTO()
							.getKey());	
		}else{
		initiateFieldVisitRequestRadio.select(false);
		}
		fvrHorizontalLayout = new HorizontalLayout(fvrFieldFLayout);
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

		HorizontalLayout remarksHLayout = new HorizontalLayout(new FormLayout(
				txtMedicalRemarks), new FormLayout(txtDoctorNote));
		remarksHLayout.setWidth("100%");

		processPreauthButtonObj = processPreauthButtonLayout.get();
		processPreauthButtonObj.initView(this.bean, wizard);
		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				processPreauthButtonObj);
		HorizontalLayout buttonWholeLayout = new HorizontalLayout(
				buttonsHLayout);
		buttonWholeLayout.setComponentAlignment(buttonsHLayout,
				Alignment.MIDDLE_CENTER);
		buttonWholeLayout.setWidth("100%");

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
		//
		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
		
		
		
		
		amountConsidered.setValue(this.bean.getAmountConsidered());
		//
		// balanceSumInsured = new Label("Balance Sum Insured");
		// balanceSumInsured.setCaption("Balance Sum Insured");

		showBalanceSumInsured = new Button("View");
		showClaimAmtDetailsBtnDuplicate = new Button("View");
		amountConsideredViewButton = new Button("View");
		showBalanceSumInsured.setStyleName("link");
		showClaimAmtDetailsBtnDuplicate.setStyleName("link");

		amountConsideredViewButton.setStyleName("link");

		FormLayout amountform=new FormLayout(amountConsidered);
		amountform.setMargin(false);
		FormLayout amountconsiderbuttonform =new FormLayout(amountConsideredViewButton);
		amountconsiderbuttonform.setMargin(false);
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(amountform, amountconsiderbuttonform);
		// HorizontalLayout wholeAmtLayout = new
		// HorizontalLayout(amountRequestedLayout, new
		// VerticalLayout(amountConsideredLayout, new
		// FormLayout(balanceSumInsured)));
		// wholeAmtLayout.setSpacing(true);
		// wholeAmtLayout.setWidth("100%");
		// wholeAmtLayout.setComponentAlignment(amountRequestedLayout,
		// Alignment.MIDDLE_RIGHT);

		// This is used to set the balance SumInsured from DB.
		// fireViewEvent(PreauthWizardPresenter.BALANCE_SUM_INSURED,
		// this.bean.getPolicyDto());
		/**
		 * Balance SI procedure requires insured key as one of the parameter for
		 * calculation. Insured key will be available in new intimation dto and
		 * not in policy dto. Hence commented above code and added below one.
		 * */
		fireViewEvent(PAPreauthWizardPresenter.BALANCE_SUM_INSURED, this.bean);

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);

		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction </br> Amount",
				ContentMode.HTML), approvedAmtField);
		optionCLayout = new VerticalLayout(approvedFormLayout);

		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showClaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showBalanceSumInsured);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);

		HorizontalLayout hospBehaviour = new HorizontalLayout(getBehaviouroHospital());
		hospBehaviour.setSpacing(true);
		wholeVLayout = new VerticalLayout(buttonHLayout2,
				amountConsideredLayout, this.medicalDecisionTableObj,hospBehaviour,
				this.amountConsideredTable, formsHLayout, specialistHLayout,
				buttonsHLayout, remarksHLayout);
//		wholeVLayout.setComponentAlignment(buttonHLayout,
//				Alignment.MIDDLE_RIGHT);
		wholeVLayout
				.setComponentAlignment(buttonHLayout2, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);

		mandatoryFields.add(cmbFVRNotRequiredRemarks);
//		mandatoryFields.add(txtMedicalRemarks);
		// mandatoryFields.add(investigatorName);
		mandatoryFields.add(investigationReviewRemarks);
		showOrHideValidation(false);
		addListener();
		addPEDListener();

		addMedicalDecisionTableFooterListener();

		return wholeVLayout;
	}

	private void getSpecificTableDTO(Integer approvedAmt) {
		SpecificProductDeductibleTableDTO dto = new SpecificProductDeductibleTableDTO();
		dto.setClaims("Claims ");
		Integer floatFromString = SHAUtils.getIntegerFromString(this.bean
				.getAmountConsidered());
		dto.setAmountConsidered(floatFromString);
		dto.setOriginalSI(this.bean.getNewIntimationDTO().getPolicy()
				.getTotalSumInsured() != null ? this.bean.getNewIntimationDTO()
				.getPolicy().getTotalSumInsured().intValue() : 0);
		dto.setAmountPayable(approvedAmt);
		dto.setDeductible(300000);
		Integer max = Math.max(dto.getAmountConsidered(), dto.getOriginalSI());
		dto.setAmountToBeConsidered(max);
		Integer value = max - dto.getDeductible();
		dto.setEligibleAmountPayable(value);
		dto.setBalanceSI(preauthDTO.getBalanceSI() != null ? preauthDTO
				.getBalanceSI().intValue() : 0);
		Integer payableAmt = Math.min(value, dto.getBalanceSI());
		dto.setPayableAmount(payableAmt);
		// previousClaimList.add(dto);
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
		// initiateFieldVisitButton = new Button("Intiate Investigation");

		initiateInvestigationButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Long preauthKey = bean.getKey();
				Long intimationKey = bean.getIntimationKey();
				Long policyKey = bean.getPolicyKey();
				Long claimKey = bean.getClaimKey();
			     
				// TODO : Should give true or false based on investigation initiated.
				viewDetails.setPreAuthKey(bean.getKey());
				viewDetails.getViewInvestigationDetailsForPreauth(claimKey, true,bean.getStrUserName(),bean.getStrPassword(),bean);
				
			}
		});		

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
									PAPreauthWizardPresenter.FIELD_VISIT_RADIO_CHANGED,
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
								PAPreauthWizardPresenter.SPECIALIST_OPINION_RADIO_CHANGED,
								isChecked);
					}
				});

		showBalanceSumInsured.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null
						&& !bean.getNewIntimationDTO().getIntimationId()
								.equals("")) {
					fireViewEvent(
							PAPreauthWizardPresenter.VIEW_BALANCE_SUM_INSURED_DETAILS,
							bean.getNewIntimationDTO().getIntimationId());
				}
			}
		});

		showClaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1159870471084252041L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						PAPreauthWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});

		/*
		 * showBalanceSumInsured.addClickListener(new ClickListener() {
		 *//**
			 * 
			 */
		/*
		 * private static final long serialVersionUID = 1159870471084252041L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) { if
		 * (bean.getNewIntimationDTO() != null &&
		 * bean.getNewIntimationDTO().getIntimationId() != null &&
		 * !bean.getNewIntimationDTO().getIntimationId() .equals("")){
		 * fireViewEvent(
		 * PreauthWizardPresenter.VIEW_BALANCE_SUM_INSURED_DETAILS,
		 * bean.getNewIntimationDTO().getIntimationId()); }
		 * 
		 * } });
		 */

		amountConsideredViewButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2259148886587320228L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						PAPreauthWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
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
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				setMedicalDecisionDiagnosis(medicalDecisionDTOList, pedValidationTableDTO, false, false);
			}
			
			if(!this.bean.getDeletedDiagnosis().isEmpty()) {
				List<DiagnosisDetailsTableDTO> deletedDiagnosis = this.bean.getDeletedDiagnosis();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					setMedicalDecisionDiagnosis(medicalDecisionDTOList, diagnosisDetailsTableDTO, true, true);
				}
			}
			
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				setMedicalDecisionProcedures(medicalDecisionDTOList, procedureDTO, false, false);
			}

			List<ProcedureDTO> deletedProcedureList = bean.getDeletedProcedure();
			if(!deletedProcedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : deletedProcedureList) {
					setMedicalDecisionProcedures(medicalDecisionDTOList, procedureDTO, true, true);
				}
			}
			
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = 0d;
			if(null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			 insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				 insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(this.bean.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
								.getInsuredPatient().getLopFlag());
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

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
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
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
						caluculationInputValues.put("referenceFlag", "D");
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", 0l);
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					
					fireViewEvent(
							PAPreauthWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,bean);
					Map<String, Object> values = this.sublimitCalculatedValues;
					
					if(bean.getIsNonAllopathic()) {
						bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
						bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
					}
					
					setAppropriateValues(caluculationInputValues, medicalDecisionDto,
							values);
					
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
					
					this.medicalDecisionTableObj
							.addBeanToList(medicalDecisionDto);
				}
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
							PAPreauthWizardPresenter.SUM_INSURED_CALCULATION,
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

	private void setMedicalDecisionDiagnosis(
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
									&& exclusionDetails
											.getKey()
											.equals(pedDetailsTableDTO
													.getExclusionDetails()
													.getId())) {
								paymentFlag = exclusionDetails
										.getPaymentFlag();
							}
						}

						if (paymentFlag.toLowerCase().equalsIgnoreCase(
								"n")) {
							isPaymentAvailable = false;
							break;
						}
					}
				}
			}

			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
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
		dto.setIsDeletedOne(isDeletedOne);
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
		if(pedValidationTableDTO.getPedList().size() == 1) {
			dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
		}
		
		dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
	}

	private void setMedicalDecisionProcedures(
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
				dto.setReverseAllocatedAmt(0);
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
		
		if(isZeroAppAmt) {
			dto.setMinimumAmount(0);
			dto.setReverseAllocatedAmt(0);
			dto.setIsPaymentAvailable(false);
		}

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}
		dto.setIsDeletedOne(isDeletedOne);
		dto.setCoPayPercentage(procedureDTO.getCopay());
		
		dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		
		
		medicalDecisionDTOList.add(dto);
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
		
		if(bean.getStatusKey() != null && ! bean.getStatusKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS)){
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
		
		
		
		if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && bean.getIsPending()) {
			hasError = true;
			eMsg += "Cheque Status is Pending. Hence this Preauth Can not be submitted.";
		}
		
		if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
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
						
						fireViewEvent(PAPreauthWizardPresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
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
		} else if(!hasError && !bean.getIsReverseAllocation() && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
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
		
		
		if(!hasError) {
			if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
				Integer amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
				if(amt <= 0) {
					hasError = true;
					eMsg += "Approved Amount is Zero. Hence this Preauth can not be submitted. ";
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
				this.bean.getPreauthMedicalDecisionDetails()
						.setMedicalDecisionTableDTO(this.medicalDecisionTableObj.getValues());
				
				
				SHAUtils.setCopayAmounts(bean, this.amountConsideredTable);
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null){
					this.bean.getPreauthMedicalDecisionDetails().setInvestigatorCode(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getInvestigatorCode());
				}
			
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
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
	
	private void createReverseRelatedFields() {
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
				this.bean.setIsReverseAllocation(false);
			}
		}
	}

	@Override
	public boolean onAdvance() {
		fireViewEvent(PAPreauthWizardPresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());

		setResidualAmtToDTO();

		setCordinatorDetails();
		
		if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS))
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

	@Override
	public boolean onBack() {
		this.bean.getPreauthMedicalDecisionDetails()
				.setMedicalDecisionTableDTO(
						new ArrayList<DiagnosisProcedureTableDTO>());
		this.bean.getPreauthMedicalDecisionDetails().getInitiateFieldVisitRequestFlag();
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
			Object dropdowValues, Object assignToValues, Object priorityValues) {
		if (isChecked) {
			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			fvrTriggerPoints = (TextArea) binder.buildAndBind(
					"FVR Trigger Points", "fvrTriggerPoints", TextArea.class);
			fvrTriggerPoints.setMaxLength(4000);
			fvrTriggerPoints.setWidth("100%");
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
					.setContainerDataSource((BeanItemContainer<SelectValue>) priorityValues);
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
			
			if (this.bean.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
				this.cmbFVRNotRequiredRemarks.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
			}
			
			mandatoryFields.add(cmbFVRNotRequiredRemarks);
			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			setRequiredAndValidation(cmbFVRNotRequiredRemarks);
			if(null != fvrTriggerPoints ){
				fvrFieldFLayout.removeComponent(fvrTriggerPoints);
			}
			if(null != cmbAllocationTo){
				fvrFieldFLayout.removeComponent(cmbAllocationTo);
			}
			if(null != cmbFvrAssignTo) {
				fvrFieldFLayout.removeComponent(cmbFvrAssignTo);
			}
			
			if(null != cmbFvrPriority) {
				fvrFieldFLayout.removeComponent(cmbFvrPriority);
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

			txtRemarksBySpecialist = (TextArea) binder.buildAndBind(
					"Remarks by Specialist", "remarksBySpecialist",
					TextArea.class);
			txtRemarksBySpecialist.setMaxLength(4000);
			
			if (this.bean.getPreauthMedicalDecisionDetails().getSpecialistType() != null) {
				this.cmbSpecialistType.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getSpecialistType());
			}
			
			if (this.bean.getPreauthMedicalDecisionDetails().getSpecialistConsulted() != null) {
				this.cmbSpecialistConsulted.setValue(this.bean
						.getPreauthMedicalDecisionDetails().getSpecialistConsulted());
			}
			
			setRequiredAndValidation(cmbSpecialistConsulted);
			setRequiredAndValidation(txtRemarksBySpecialist);
			setRequiredAndValidation(cmbSpecialistType);
			specialistFLayout.addComponent(cmbSpecialistType);
			specialistFLayout.addComponent(cmbSpecialistConsulted);
			specialistFLayout.addComponent(txtRemarksBySpecialist);
			mandatoryFields.add(cmbSpecialistConsulted);
			mandatoryFields.add(txtRemarksBySpecialist);
			mandatoryFields.add(cmbSpecialistType);
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

	public void showBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			viewDetails.getViewBalanceSumInsured(intimationId);
		}
	}

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		this.bean.setStageKey(ReferenceTable.PREAUTH_STAGE);
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
					processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
				}
			} 
			
			this.bean.setStatusKey(ReferenceTable.PREAUTH_APPROVE_STATUS);
			break;
		case 2:
			this.processPreauthButtonObj.buildQueryLayout();
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.PREAUTH_QUERY_STATUS);
			break;
		case 3:
			this.processPreauthButtonObj.buildRejectLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.PREAUTH_REJECT_STATUS);
			break;
		case 4:
			this.processPreauthButtonObj.buildDenialLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
					.setStatusKey(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
			break;
		case 5:
			this.processPreauthButtonObj.buildEscalateLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.PREAUTH_ESCALATION_STATUS);
			break;
		case 6:
			this.processPreauthButtonObj
					.buildReferCoordinatorLayout(referenceData
							.get("coordinatorTypeRequest"));
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean
					.setStatusKey(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS);
			break;
		default:
			break;
		}
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
	} else*/ if (checkInitiateInvestigation && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) {
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

	public void setBalanceSI(Double balanceSI, List<Double> productCopay) {
		if (balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(productCopay);
	}
	
	public void setBalanceSIforRechargedProcessing(Double balanceSI){
		if(balanceSI != null){
			this.bean.setBalanceSI(balanceSI);
			if(this.amountConsideredTable != null){
				this.amountConsideredTable.setBalanceSumInsuredAfterRecharge(balanceSI);
			}
		}
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
						if (bean.getStatusKey() != null && (bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							
							processPreauthButtonObj.setApprovedAmtValue(min);
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
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
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
//										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
//										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}
								
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
								}
							}
						}
						
						
					}
				});

		this.amountConsideredTable.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 9193355451830325446L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString(medicalDecisionTableObj.dummyField
										.getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							Integer min = Math.min(
									amountConsideredTable.getMinimumValue(),
									totalApprovedAmt);
							if(bean.getIsNonAllopathic()) {
								min = Math.min(min, bean.getNonAllopathicAvailAmt());
							}
							
							
							processPreauthButtonObj.setApprovedAmtValue(min);
							if(bean.getIsReverseAllocation()) {
								Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(processPreauthButtonObj.getApprovedAmount());
								Integer cValue = SHAUtils.getIntegerFromStringWithComma(medicalDecisionTableObj.dummyField.getValue()); 
								if(cValue.equals(approvedAmt) && !bean.getReverseAmountConsidered().equals(String.valueOf(approvedAmt))) {
									bean.setIsReverseAllocation(false);
									medicalDecisionTableObj.deleteReverseAllocation();
								}
							}
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}
									
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
								     Integer min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
								     bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
										if(!bean.getIsReverseAllocation()) {
											amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
											approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
//											processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
										}  else {
											if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
												bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
											}
											Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
											amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
//											processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										}
									
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
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
				.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
			
			processPreauthButtonObj.setApprovedAmtValue(min);
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
				}
			}
		} else {
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//					processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
				}
			}
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

}