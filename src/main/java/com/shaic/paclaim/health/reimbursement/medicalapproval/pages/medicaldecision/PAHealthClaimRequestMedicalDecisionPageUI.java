package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.SpecificProductPreviousClaims;
import com.shaic.claim.preauth.ViewClaimAmountDetils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.dto.SpecificProductDeductibleTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.preauth.wizard.pages.DiagnosisProcedureListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestFileUploadUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimsSubmitHandler;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.InvestigationReviewRemarksTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.MedicalDecisionFVRGrading;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.MedicalVerificationTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.TreatmentQualityVerificationTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Investigation;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class PAHealthClaimRequestMedicalDecisionPageUI  extends ViewComponent implements ClaimsSubmitHandler{
	private static final long serialVersionUID = -3466733459218208627L;

	private static final SpecificProductDeductibleTableDTO preauthDTO = null;

	@Inject
	private PreauthDTO bean;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;

	@Inject
	private Instance<DiagnosisProcedureListenerTable> medicalDecisionTable;
	
	@Inject
	private Instance<MedicalDecisionFVRGrading> fvrGradingTableInstance;
	
	private MedicalDecisionFVRGrading fvrGradingTableObj;
	
	@Inject
	private Instance<MedicalVerificationTable> medicalVerificationInstance;
	
	@EJB
	private MasterService masterService;
	
	private MedicalVerificationTable medicalVerificationTableObj;
	
//	@Inject
//	private ClaimRequestFileUploadUI claimRequestFileUploadUI;
	
	@Inject
	private Instance<TreatmentQualityVerificationTable> treatmentVerificationTableInstance;
	
	private TreatmentQualityVerificationTable treatmentVerificationTableObj;

	@Inject
	private Instance<PAHealthClaimRequestMedicalDecisionButtons> claimRequestButtonInstance;

	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;

	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;

	private AmountConsideredTable amountConsideredTable;
	
	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;
	
	@Inject
	private Instance<AmountConsideredTable> balanceSumInsuredTableInstance;

	@Inject
	private Instance<SpecificProductPreviousClaims> specifictProductDeductibleTable;

	private SpecificProductPreviousClaims specifictProductDeductibleTableObj;

	private AmountConsideredTable balanceSumInsuredTableObj;

	private PAHealthClaimRequestMedicalDecisionButtons claimRequestButtonObj;

	private DiagnosisProcedureListenerTable medicalDecisionTableObj;

	/*private CheckBox investigationReportReviewedChk;

	private ComboBox investigatorName;

	private TextArea investigationReviewRemarks;*/

	private TextField txtMedicalRemarks;

	private TextField txtDoctorNote;

	private VerticalLayout wholeVLayout;

	private FormLayout investigationFLayout;


	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private Label amountConsidered;

	private String preAuthRequestedAmt;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	Map<String, Object> referenceData = new HashMap<String, Object>();

	Map<String, Object> sublimitCalculatedValues;

	private Double balanceSumInsuredValue;

	private String diagnosisName;

	private Button initiatePEDButton;

	// Added for intiate field visit.

	private Button initiateFieldVisitButton;

	private Double copayValue = 0d;

	@Inject
	private ViewDetails viewDetails;

	public Double value;

	private TextField approvedAmtField;

	private TextField consideredAmtField;
	
	private GWizard wizard;
	
	@Inject
	private UploadedFileViewUI fileViewUI;

	private ClaimRequestFileUploadUI specialistWindow = new ClaimRequestFileUploadUI();
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;

	@Inject
	private Instance<InvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
	private InvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
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

	public Component getContent() {
		initBinder();
		Button submitButton = this.wizard.getFinishButton();
		submitButton.setEnabled(false);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		initiatePEDButton.setEnabled(false);
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setComponentAlignment(initiatePEDButton,
				Alignment.MIDDLE_RIGHT);

		initiateFieldVisitButton = new Button("View Earlier ROD Details");
		// FormLayout
		HorizontalLayout buttonLayout = new HorizontalLayout(
				initiateFieldVisitButton);
		buttonLayout.setComponentAlignment(initiateFieldVisitButton,
				Alignment.MIDDLE_RIGHT);


		txtMedicalRemarks = (TextField) binder.buildAndBind("Medical Remarks",
				"medicalRemarks", TextField.class);
		txtMedicalRemarks.setMaxLength(4000);
		CSValidator validator = new CSValidator();
		// validator.extend(txtMedicalRemarks);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);
		txtDoctorNote = (TextField) binder.buildAndBind(
				"Doctor Note(Internal Remarks)", "doctorNote", TextField.class);
		txtDoctorNote.setMaxLength(4000);
		// validator = new CSValidator();
		// validator.extend(txtDoctorNote);
		// validator.setRegExp("^[a-zA-Z 0-9.]*$");
		// validator.setPreventInvalidTyping(true);

		/*investigationReportReviewedChk = (CheckBox) binder.buildAndBind(
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
			
		investigationFLayout = new FormLayout(investigationReportReviewedChk,
				investigatorName, investigationReviewRemarks);*/
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.INVS_REVIEW_REMARKS_LIST, this.bean);
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());

		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);

		HorizontalLayout remarksHLayout = new HorizontalLayout(new FormLayout(
				txtMedicalRemarks), new FormLayout(txtDoctorNote));
		remarksHLayout.setWidth("100%");

		claimRequestButtonObj = claimRequestButtonInstance.get();
		claimRequestButtonObj.initView(this.bean, wizard/*,investigationReportReviewedChk*/);
		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				claimRequestButtonObj);
		HorizontalLayout buttonWholeLayout = new HorizontalLayout(
				buttonsHLayout);
		buttonWholeLayout.setComponentAlignment(buttonsHLayout,
				Alignment.MIDDLE_CENTER);
		buttonWholeLayout.setWidth("100%");

		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("Cashless Approval Details", false, false);
		
		if(SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) > 0) {
			Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
			this.bean.setAmountConsidered(String.valueOf(amount));
			if(!this.bean.getHospitalizaionFlag()) {
				this.bean.setAmountConsidered(String.valueOf(this.bean.getHospitalizationAmount()));
			}
			
		}
		viewBillSummary.init(bean,bean.getKey(),true);
		Map<String, String> payableAmount = viewBillSummary.getPayableAmount();
		String string = payableAmount.get(SHAConstants.HOSPITALIZATION);
		this.bean.setAmountConsidered(String.valueOf(SHAUtils.getDoubleValueFromString(string).intValue()));
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
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
		amountConsideredViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("90%");
				popup.setHeight("85%");
				
				viewBillSummary.init(bean.getKey());
				
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("90%");
		        mainPanel.setHeight("90%");
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
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		});

		// HorizontalLayout amountRequestedLayout = new HorizontalLayout(new
		// FormLayout(preauthAmtRequested), showclaimAmtDetailsBtnDuplicate );
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered),new FormLayout( amountConsideredViewButton));
		amountConsideredLayout.setHeight("55px");
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
//		fireViewEvent(PreauthWizardPresenter.BALANCE_SUM_INSURED, this.bean);

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		this.medicalDecisionTableObj.setVisibleColumns();

		medicalVerificationTableObj = medicalVerificationInstance.get();
		medicalVerificationTableObj.init("Medical Verification", false);
		
		treatmentVerificationTableObj = treatmentVerificationTableInstance.get();
		treatmentVerificationTableObj.init("Treatment Quality Verification", false);
		//treatmentVerificationTableObj.setWidth("1500px");
		
		if(!this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO().isEmpty()) {
			fvrGradingTableObj = fvrGradingTableInstance.get();
			fvrGradingTableObj.initView(bean, false);
		}
		VerticalLayout verticalLayout = new VerticalLayout();
		if(fvrGradingTableObj != null) {
			verticalLayout.addComponent(fvrGradingTableObj);
		}

		wholeVLayout = new VerticalLayout(buttonHLayout, this.previousPreAuthDetailsTableObj,
				amountConsideredLayout, this.medicalDecisionTableObj,
				 specialistHLayout, verticalLayout, medicalVerificationTableObj, treatmentVerificationTableObj, 
				 remarksHLayout, buttonsHLayout);
		wholeVLayout.setComponentAlignment(buttonHLayout,
				Alignment.MIDDLE_RIGHT);
//		wholeVLayout
//				.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		wholeVLayout.setSpacing(true);

		// mandatoryFields.add(investigatorName);
		//mandatoryFields.add(investigationReviewRemarks);
//		mandatoryFields.add(txtMedicalRemarks);
		showOrHideValidation(false);
//		unbindField(investigationReviewRemarks);
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
	protected void addListener() {


		txtDoctorNote.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		        // Assuming that the value type is a String
		        String value = (String) event.getProperty().getValue();

		        txtDoctorNote.setDescription(value);
		    }
		});
		
		
		// initiateFieldVisitButton = new Button("Intiate Investigation");


		showBalanceSumInsured.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null
						&& !bean.getNewIntimationDTO().getIntimationId()
								.equals("")) {
					fireViewEvent(
							PAHealthClaimRequestMedicalDecisionPagePresenter.VIEW_BALANCE_SUM_INSURED_DETAILS,
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
						PAHealthClaimRequestMedicalDecisionPagePresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		/*investigationReportReviewedChk.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
//				Boolean value =(Boolean)event.getProperty().getValue();
				Boolean checkValue = investigationReportReviewedChk.getValue();
				
				if(checkValue != null && checkValue){
					investigatorName.setEnabled(true);
					investigationReviewRemarks.setEnabled(true);
					mandatoryFields.remove(investigationReviewRemarks);
					mandatoryFields.add(investigationReviewRemarks);
				}
				else{
					investigatorName.setEnabled(false);
					investigationReviewRemarks.setEnabled(false);
					
				}
				
			}
		});*/


//		amountConsideredViewButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = -2259148886587320228L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(
//						ClaimRequestMedicalDecisionPagePresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
//						true);
//			}
//		});

	}

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		this.claimRequestButtonObj.setReferenceData(referenceData);
		@SuppressWarnings("unchecked")
		BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData
				.get("investigatorName");
		BeanItemContainer<SelectValue> medicalVerificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("medicalVerification");
		BeanItemContainer<SelectValue> treatmentVerificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentQualityVerification");

		/*investigatorName.setContainerDataSource(investigatorNameContainer);
		investigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		investigatorName.setItemCaptionPropertyId("investigatorName");*/
		
		/*if (this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null && this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getKey() != null) {
			for(int i=0; i<investigatorNameContainer.size() ; i++){
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getKey().equals(investigatorNameContainer.getIdByIndex(i).getKey())){
					
					this.investigatorName.setValue(investigatorNameContainer.getIdByIndex(i));
				}
			}
			
		}*/
		
		
		List<MedicalVerificationDTO> medicalVerificationTableDTO = this.bean.getPreauthMedicalDecisionDetails().getMedicalVerificationTableDTO();
		this.medicalVerificationTableObj.setReference(referenceData);
		if(!medicalVerificationTableDTO.isEmpty()) {
			int i = 1;
			for (MedicalVerificationDTO medicalVerificationDTO : medicalVerificationTableDTO) {
				medicalVerificationDTO.setSlNo(i);
				this.medicalVerificationTableObj.addBeanToList(medicalVerificationDTO);
				i++;
			}
			
			
		} else {
			int i = 1;
			List<SelectValue> itemIds = medicalVerificationContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				MedicalVerificationDTO verificationDTO = new MedicalVerificationDTO();
				verificationDTO.setDescription(selectValue.getValue());
				verificationDTO.setDescriptionId(selectValue.getId());
				verificationDTO.setSlNo(i);
				this.medicalVerificationTableObj.addBeanToList(verificationDTO);
				i++;
			}
		}
		
		List<TreatmentQualityVerificationDTO> treatmentVerificationTableDTO = this.bean.getPreauthMedicalDecisionDetails().getTreatmentVerificationDTO();
		this.treatmentVerificationTableObj.setReference(referenceData);
		if(!treatmentVerificationTableDTO.isEmpty()) {
			int i = 1;
			for (TreatmentQualityVerificationDTO treatmentQualityVerificationDTO : treatmentVerificationTableDTO) {
				treatmentQualityVerificationDTO.setSlNo(i);
				this.treatmentVerificationTableObj.addBeanToList(treatmentQualityVerificationDTO);
				i++;
			}
			
		} else {
			int i = 1;
			List<SelectValue> itemIds = treatmentVerificationContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				TreatmentQualityVerificationDTO verificationDTO = new TreatmentQualityVerificationDTO();
				verificationDTO.setDescription(selectValue.getValue());
				verificationDTO.setDescriptionId(selectValue.getId());
				verificationDTO.setSlNo(i);
				this.treatmentVerificationTableObj.addBeanToList(verificationDTO);
				i++;
			}
		}
		
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				addDiagnosisToMedicalDecision(medicalDecisionDTOList, pedValidationTableDTO, false, false);
			}
			
			if(!this.bean.getDeletedDiagnosis().isEmpty()) {
				List<DiagnosisDetailsTableDTO> deletedDiagnosis = this.bean.getDeletedDiagnosis();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					addDiagnosisToMedicalDecision(medicalDecisionDTOList, diagnosisDetailsTableDTO, true, true);
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
							.getGPAInsuredSumInsured(this.bean.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId().toString(),
									this.bean.getPolicyDto().getKey());
			}
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

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
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
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
						caluculationInputValues
								.put("diagOrProcId",
										(medicalDecisionDto.getProcedureDTO().getProcedureName() == null  || medicalDecisionDto.getProcedureDTO().getNewProcedureFlag().equals(1l)) ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName().getId());
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", !this.bean.getPreviousPreauthKey().equals(0l) ? this.bean.getPreviousPreauthKey():this.bean.getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimKey());
					
					if(this.bean.getClaimDTO().getLatestPreauthKey() != null){
				    	caluculationInputValues.put("preauthKey", this.bean.getClaimDTO().getLatestPreauthKey());
				    }
					
					if((this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat())) {
						caluculationInputValues.put("preauthKey", 0l);
					}
					
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.SUM_INSURED_CALCULATION,caluculationInputValues, medicalDecisionDto,this.bean);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				this.medicalDecisionTableObj.addBeanToList(dto);
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
			caluculationInputValues.put("preauthKey", this.bean.getPreauthKey() != null ? this.bean.getPreauthKey() : 0l);
			
			caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
			caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
			caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
			
			bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
		
			
			DiagnosisProcedureTableDTO residualDTO = null;
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
					SHAUtils.fillDetailsForUtilForProcedure(caluculationInputValues, diagnosisProcedureTableDTO);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
					SHAUtils.fillDetailsForUtilizationForDiag(caluculationInputValues, diagnosisProcedureTableDTO);
				}
				if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null || diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.SUM_INSURED_CALCULATION,caluculationInputValues, diagnosisProcedureTableDTO,this.bean);
				}else{
					residualDTO = diagnosisProcedureTableDTO;
				}
			
				
			}
			
			if(residualDTO != null){
				this.medicalDecisionTableObj.addBeanToList(residualDTO);
			}
//			this.medicalDecisionTableObj.addList(filledDTO);
		
		}
		
		
		List<PreviousPreAuthTableDTO> previousPreauthList = (List<PreviousPreAuthTableDTO>) referenceData
				.get("previousPreauth");
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		if(previousPreauthList != null) {
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				if (!(this.bean.getKey() != null && this.bean.getKey().equals(
						previousPreAuthTableDTO.getKey()))) {
					fireViewEvent(
							PAHealthClaimRequestMedicalDecisionPagePresenter.GET_PREAUTH_REQUESTED_AMOUT,
							previousPreAuthTableDTO);
					previousPreAuthTableDTO.setRequestedAmt(preAuthRequestedAmt);
					newList.add(previousPreAuthTableDTO);
				}
			}
		}
		
		this.previousPreAuthDetailsTableObj.setTableList(newList);
		if(fvrGradingTableObj != null) {
			fvrGradingTableObj.setupReferences(referenceData);
		}
		
		
	}

	private void addProcedureToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			ProcedureDTO procedureDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		dto.setProcedureDTO(procedureDTO);
		if (procedureDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = procedureDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			
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
			if(isZeroApprAmt) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
				dto.setIsPaymentAvailable(false);
			}
		}
		dto.setRestrictionSI("NA");
		
		dto.setPackageAmt("NA");
		if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
			dto.setPackageAmt(procedureDTO.getPackageRate().toString());
		}
		

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}
		dto.setIsDeletedOne(isDeletedOne);
		

		dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		
		
		medicalDecisionDTOList.add(dto);
	}

	private void addDiagnosisToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			DiagnosisDetailsTableDTO pedValidationTableDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = pedValidationTableDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			if (isPaymentAvailable) {
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
			
			if(isZeroApprAmt) {
				dto.setIsPaymentAvailable(false);
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
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
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
		dto.setIsDeletedOne(true);
		
		dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		medicalDecisionDTOList.add(dto);
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

	public boolean validatePage() {
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
		
		if(this.bean.getStatusKey() != null && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
			
			if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValid()) {
				hasError = true;
				List<String> errors = this.medicalVerificationTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrorsForRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrorsforRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(!fvrGradingDTO.isEmpty()) {
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
					if(fvrGradingDTO3.getStatus() == null) {
						hasError = true;
						eMsg += "Please Select All the FVR Grading and set the Status. </br>";
						break;
					}
				}
			}
		}
		if (!this.claimRequestButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.claimRequestButtonObj.getErrors();
			for (String error : errors) {
				eMsg += error;
			}
		}
		
		if(!((ReferenceTable.INTIMATION_REGISTERED_STATUS).equals(bean.getInvestigationStatus())|| 
				(ReferenceTable.INITIATE_INVESTIGATION_APPROVED).equals(bean.getInvestigationStatus()) ||
				(ReferenceTable.DRAFT_INVESTIGATION).equals(bean.getInvestigationStatus())||
				(ReferenceTable.ASSIGN_INVESTIGATION).equals(bean.getInvestigationStatus())
				))
		{
		
		/*if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))
		{
		if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
		}
     
		if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
		}
		}*/
			
			if(bean != null && bean.getPreauthMedicalDecisionDetails() != null && bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() != null && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty()){
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getReviewRemarkskey() == null 
							&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
						hasError = true;
						eMsg += "Please Select Investigator/ RVO grade </br>";
						break;
					}
				}
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() == null
							&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
						eMsg += "Please Select RVO findings- Claim decision </br>";
						hasError = true;
						break;
					}
				}
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() != null 
							&& ( (assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_APPROVED_KEY) || 
									assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_REJECTED_KEY))
									&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey()))
									&& assignedInvestigatiorDetails.getRvoReasonKey() == null) {
						eMsg += "Please Select Reasons for not Accepting RVO Findings </br>";
						hasError = true;
						break;
					}
				}
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && (assignedInvestigatiorDetails.getReportReviewed() == null 
							|| (assignedInvestigatiorDetails.getReportReviewed() != null && assignedInvestigatiorDetails.getReportReviewed().equalsIgnoreCase(SHAConstants.N_FLAG)))
							&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
						hasError = true;
						eMsg += "Please Select Investigation Remarks checkbox </br>";
						break;
					}
				}
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() != null 
							&& ( bean.getApproveButtonExists() &&  (assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_REJECTED_KEY) || 
									assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_REJECTED_KEY)))
									&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
						eMsg += "Chosen RVO findings- Claim decision is RVO findings Accepted - Claim not Approved, hence claim cannot be approved </br>";
						hasError = true;
						break;
					}
				}
				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() != null 
							&& ( (bean.getRejectionButtonExists() &&  (assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_APPROVED_KEY) || 
									assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_APPROVED_KEY)))
									&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey()))
							) {
						eMsg += "Chosen RVO findings- Claim decision is RVO findings Accepted - Claim Approved, hence claim cannot be rejected </br>";
						hasError = true;
						break;
					}
				}
			}	
		}
//		if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
//				SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
//			hasError = true;
//			eMsg += "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
//		}
		
		if(!this.bean.getIsScheduleClicked()){
			hasError = true;
			eMsg += "Please Verify Policy Schedule Button.</br>";
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
//				this.bean.getPreauthMedicalDecisionDetails()
//						.setMedicalDecisionTableDTO(
//								this.medicalDecisionTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setMedicalVerificationTableDTO(this.medicalVerificationTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setTreatmentVerificationDTO(this.treatmentVerificationTableObj.getValues());
				if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null){
					this.bean.getPreauthMedicalDecisionDetails().setInvestigatorCode(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getInvestigatorCode());
					
				}
				// New requirement for saving Copay values to Transaction Table......... 
				SHAUtils.setCopayAmounts(bean, this.claimRequestButtonObj.amountConsideredTable);
				
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
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
	
	public void generateFieldBasedOnSentToCPU(Boolean isChecked) {
		claimRequestButtonObj.generateFieldsBasedOnSentToCPU(isChecked);
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
		this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		switch (clickedButton) {
		case 1: 
			
		 this.claimRequestButtonObj.buildSendReplyLayout();
		 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
		 
		 if(this.bean.getIsReplyToFA() != null && this.bean.getIsReplyToFA()){
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
		 }
		 
		 break;
		 
		case 2: 
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
				
			 }
			 this.claimRequestButtonObj.buildInitiateFieldVisit(dropDownValues);
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
			 
			 break;
			 
		case 3: 
			
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setTriggerPointsToFocus("");
				
			 }
			 this.claimRequestButtonObj.buildInitiateInvestigation(dropDownValues);
			
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			 break;
		case 4:
			
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 
			 }
			this.claimRequestButtonObj
					.buildReferCoordinatorLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			 
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
			break;
		case 5:
			if(fileUploadValidatePage()){
				specialistWindow.init(bean);
				specialistWindow.buildEscalateReplyLayout();
				specialistWindow.center();
				specialistWindow.setHeight("400px");
				specialistWindow.setResizable(false);
				specialistWindow.setModal(true);
				specialistWindow.addSubmitHandler(this);
				UI.getCurrent().addWindow(specialistWindow);
				
				specialistWindow.addCloseListener(new CloseListener() {
			            private static final long serialVersionUID = -4381415904461841881L;

			            public void windowClose(CloseEvent e) {
//			                System.out.println("close called");
			            }
			        });
				 
				 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS);
				}
				break;
			
		case 6:
			
//			this.claimRequestButtonObj.buildEscalateLayout(dropDownValues);
//			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
//			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
//			break;
			if(fileUploadValidatePage()){
			specialistWindow.init(bean);
			BeanItemContainer<SelectValue> masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
			specialistWindow.buildEscalateLayout(dropDownValues,fileViewUI,masterValueByReference);
			specialistWindow.center();
			specialistWindow.setHeight("400px");
			specialistWindow.setResizable(false);
			specialistWindow.setModal(true);
			specialistWindow.addSubmitHandler(this);
			UI.getCurrent().addWindow(specialistWindow);
			
			specialistWindow.addCloseListener(new CloseListener() {
		            private static final long serialVersionUID = -4381415904461841881L;

		            public void windowClose(CloseEvent e) {
//		                System.out.println("close called");
		            }
		        });
			 
//			this.claimRequestFileUploadUI.init(bean, wizard);
//			this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
			
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
//			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
//				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
//			 }
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
			}
			break;
			
		case 7:
//			this.claimRequestButtonObj.buildSpecialistLayout(dropDownValues);
			 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			 }
			if(fileUploadValidatePage()){
			specialistWindow.init(bean);
			specialistWindow.buildSpecialityLayout(dropDownValues,fileViewUI);
			specialistWindow.center();
			specialistWindow.setHeight("400px");
			specialistWindow.setResizable(false);
			specialistWindow.setModal(true);
			specialistWindow.addSubmitHandler(this);
			UI.getCurrent().addWindow(specialistWindow);
			
			specialistWindow.addCloseListener(new CloseListener() {
		            private static final long serialVersionUID = -4381415904461841881L;

		            public void windowClose(CloseEvent e) {
//		                System.out.println("close called");
		            }
		        });
			 
//			this.claimRequestFileUploadUI.init(bean, wizard);
//			this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
			
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
			}
			break;
		case 8:
			
			this.claimRequestButtonObj.buildQueryLayout();
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			break;
		case 9:
			
			this.claimRequestButtonObj.buildRejectLayout(dropDownValues);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			break;
		case 10:
//			Integer min = Math
//					.min(amountConsideredTable.getMinimumValue(),
//							SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
//									.getValue()));
			
			this.claimRequestButtonObj.generateFieldsForSuggestApproval();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
			break;
		case 11:
			this.claimRequestButtonObj.builtCancelRODLayout();
			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
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

	/*public void setInvestigationCheck(Boolean checkInitiateInvestigation) {
		bean.setIsInvestigation(checkInitiateInvestigation);
		if (!checkInitiateInvestigation) {
<<<<<<< HEAD
		unbindField(investigationReportReviewedChk);
		unbindField(investigationReviewRemarks);
		unbindField(investigatorName);
		mandatoryFields.remove(investigationReviewRemarks);
	} else if (checkInitiateInvestigation && bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
		
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
	
	if(((ReferenceTable.PA_LOB_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getLobId().getId())) || 
			((ReferenceTable.PACKAGE_MASTER_VALUE).equals(bean.getClaimDTO().getNewIntimationDto().getLobId().getId())))
	{
		bean.setIsInvestigation(checkInitiateInvestigation);
	}

	}*/

	public void setInvestigationStatusAndStage(Investigation investigation)
	{
		if(null != investigation)
		{
		bean.setInvestigationStage(investigation.getStage().getKey());
		bean.setInvestigationStatus(investigation.getStatus().getKey());
		}
	}

	public void setBalanceSI(Double balanceSI, List<Double> productCopay) {
		if (balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(productCopay);
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
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							Integer min = Math.min(
									amountConsideredTable.getMinimumValue(),
									totalApprovedAmt);
							claimRequestButtonObj.setApprovedAmtValue(min);
						}
					}
				});

//		this.amountConsideredTable.dummyField
//				.addValueChangeListener(new ValueChangeListener() {
//					private static final long serialVersionUID = 9193355451830325446L;
//
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						Integer totalApprovedAmt = SHAUtils
//								.getIntegerFromString(medicalDecisionTableObj.dummyField
//										.getValue());
//						approvedAmtField.setValue(String
//								.valueOf(totalApprovedAmt));
//						if ((bean.getStatusKey()
//								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
//							Integer min = Math.min(
//									amountConsideredTable.getMinimumValue(),
//									totalApprovedAmt);
//							claimRequestButtonObj.setApprovedAmtValue(min);
//						}
//					}
//				});

	}

	public void setPreAuthRequestAmt(String strPreAuthAmt) {
		this.preAuthRequestedAmt = strPreAuthAmt;
	}
	
	public void setMedicalDecisionValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		this.claimRequestButtonObj.suggestApprovalClick(dto, medicalDecisionTableValues);
	}
	
	@SuppressWarnings("unchecked")
	public void setMedicalDecisionValuesToTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		
		dto.setAvailableAmout(((Double) medicalDecisionTableValues
				.get("restrictedAvailAmt")).intValue());
		dto.setUtilizedAmt(((Double) medicalDecisionTableValues
				.get("restrictedUtilAmt")).intValue());
		dto.setSubLimitAmount(((Double) medicalDecisionTableValues
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) medicalDecisionTableValues.get("currentSL"))
						.intValue())) : "NA");
		dto.setSubLimitUtilAmount(((Double) medicalDecisionTableValues
				.get("SLUtilAmt")).intValue());
		dto.setSubLimitAvaliableAmt(((Double) medicalDecisionTableValues
				.get("SLAvailAmt")).intValue());
		dto
				.setCoPayPercentageValues((List<String>) medicalDecisionTableValues
						.get("copay"));

		if (dto.getDiagnosisDetailsDTO() != null) {
//			dto.getDiagnosisDetailsDTO()
//					.setDiagnosis(this.diagnosisName);
		}

		// need to implement in new medical listener table
		
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getSublimitName() != null && (dto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || dto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = dto.getSubLimitAvaliableAmt();
			} else if (dto.getProcedureDTO() != null && dto.getProcedureDTO().getSublimitName() != null && (dto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || dto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = dto.getSubLimitAvaliableAmt();
			} else {
				isResidual = true;
			}
			
			if(!isResidual && bean.getPreauthDataExtractionDetails().getNoOfDays() != null) {
				Integer availAmt = SHAUtils.getIntegerFromString(bean.getPreauthDataExtractionDetails().getNoOfDays()) * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(dto.getSubLimitAmount()) , availAmt);
				dto.setSubLimitAvaliableAmt(min);
				dto.setSubLimitUtilAmount(0);
			}
		}
		this.medicalDecisionTableObj
				.addBeanToList(dto);
	}

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if(claimRequestButtonObj != null) {
			claimRequestButtonObj.setCategoryValues(selectValueContainer);
		}
		
	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		claimRequestButtonObj.setBillEntryFinalStatus(uploadDTO);
		
	}

	@Override
	public void submit(PreauthDTO preauthDTO) {
		specialistWindow.close();
		wizard.finish();
	}

	public void setBillEntryAmountConsideredValue(Double sumValue) {
		claimRequestButtonObj.setBillEntryAmountConsideredValue(sumValue, bean);
	}
	
	public boolean fileUploadValidatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";
		
		fireViewEvent(
				PAHealthClaimRequestMedicalDecisionPagePresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());

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
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj.getErrorsForRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrorsforRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if(!fvrGradingDTO.isEmpty()) {
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
					if(fvrGradingDTO3.getStatus() == null) {
						hasError = true;
						eMsg += "Please Select All the FVR Grading and set the Status. </br>";
						break;
					}
				}
			}
		}

//		if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
//				SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
//			hasError = true;
//			eMsg += "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
//		}

		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(label);
			dialog.setResizable(false);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
//				this.bean.getPreauthMedicalDecisionDetails()
//						.setMedicalDecisionTableDTO(
//								this.medicalDecisionTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setMedicalVerificationTableDTO(this.medicalVerificationTableObj.getValues());
				this.bean.getPreauthMedicalDecisionDetails().setTreatmentVerificationDTO(this.treatmentVerificationTableObj.getValues());
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		claimRequestButtonObj.setUploadDTOForBillEntry(uploadDTO);
		// TODO Auto-generated method stub
		
	}
}
