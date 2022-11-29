/**
 * 
 */
package com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

/**
 * @author ntv.vijayar
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.preauth.PreauthCoordinatorView;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.NewProcedureTableList;
import com.shaic.claim.preauth.wizard.pages.SpecialityTable;
import com.shaic.claim.premedical.listenerTables.PADiganosisDetailsListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestFileUploadUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimsSubmitHandler;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.InvestigationReviewRemarksTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.MedicalDecisionFVRGrading;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.MedicalVerificationTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.TreatmentQualityVerificationTable;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.OtherClaimDetails;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.OtherClaimDiagnosisDetails;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.UpdateHospitalDetails;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestMedicalDecisionButtons;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.cashless.listenertables.PAProcedureListenerTableForPremedical;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAReconsiderRODRequestListenerTable;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.shaic.paclaim.rod.wizard.tables.PABenefitsListenerTable;
import com.shaic.paclaim.rod.wizard.tables.PACoversListenerTable;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class PAClaimRequestDataExtractionPageUI extends ViewComponent implements
		ClaimsSubmitHandler {

	private static final long serialVersionUID = -5362064388584784817L;

	@Inject
	private PreauthDTO bean;

	private GWizard wizard;

	@Inject
	private Instance<PreauthCoordinatorView> reimbursementCoordinatorView;

	/*@Inject
	private Instance<ProcedureListenerTableForPremedical> procedureTableList;*/
	
	@Inject
	private Instance<PAProcedureListenerTableForPremedical> procedureTableList;

	@Inject
	private Instance<UpdateHospitalDetails> hospitalDetailsInstance;

	@Inject
	private Instance<SpecialityTable> specialityTableList;

	@Inject
	private Instance<PADiganosisDetailsListenerTable> diagnosisDetailsTable;

	@Inject
	private Instance<NewProcedureTableList> newProcedureTableList;

	@Inject
	private Instance<OtherClaimDetails> otherClaimDetailsInstance;

	@Inject
	private Instance<OtherClaimDiagnosisDetails> otherClaimTable;

//	private ProcedureListenerTableForPremedical procedurdTableObj;
	private PAProcedureListenerTableForPremedical procedurdTableObj;
	

	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;

	@EJB
	private MasterService masterService;

	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;

	private UpdateHospitalDetails hospitalDetailsObj;

	private NewProcedureTableList newProcedurdTableObj;

	private OtherClaimDiagnosisDetails otherClaimTableObj;

	private OtherClaimDetails otherClaimDetailsObj;

	private PreauthCoordinatorView reimbursementCoordinatorViewInstance;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	private ComboBox premedicalSuggestion;

	private ComboBox premedicalCategory;

	private TextArea premedicalRemarks;

	private TextField referenceNoTxt;

	private TextField reasonForAdmissionTxt;

	private DateField admissionDate;

	private DateField dischargeDate;

	//private ComboBox cmbCatastrophicLoss;

	private ComboBox cmbNatureOfLoss;

	private ComboBox cmbCauseOfLoss;


	private TextField noOfDaysTxt;

	// private ComboBox cmbNatureOfTreatment;

	// private PopupDateField firstConsultantDate;

	// private CheckBox corpBufferChk;

	// private TextField automaticRestorationTxt;

	// private CheckBox criticalIllnessChk;

	// private ComboBox cmbSpecifyIllness;

	private ComboBox cmbRoomCategory;
	
	private OptionGroup ventilatorSupportOption;

	private ComboBox cmbTreatmentType;

	private TextArea treatmentRemarksTxt;

	// private ComboBox cmbIllness;

	// private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;

	// private TextField systemOfMedicineTxt;

	// private ComboBox cmbHospitalisationDueTo;

	private DateField injuryDate;

	private OptionGroup medicalLegalCase;

	private OptionGroup reportedToPolice;

	// private OptionGroup coveredPreviousClaim;

	private OptionGroup isHospitalized;

	// private TextField preHospitalisationPeriod;

	// private TextField postHospitalisationPeriod;

	// private OptionGroup domicillaryHospitalisation;

	private ComboBox cmbCauseOfInjury;

	private DateField diseaseDetectedDate;

	private DateField deliveryDate;

	private FormLayout firstFLayout;

	private VerticalLayout otherClaimsFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	// private OptionGroup changeInDiagnosis;

	VerticalLayout tableVLayout;

	private SpecialityTable specialityTableObj;

	private PADiganosisDetailsListenerTable diagnosisDetailsTableObj;

	private Boolean processId;

	private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

	private HorizontalLayout dynamicElementsHLayout;

	private FormLayout treatmentFLayout;

	// private FormLayout patientStatusFLayout;

	private Integer changeIndiaganosisRows = 0;

	private Boolean changeInDiagSelected = false;

	// / ------ Newly added fields --------////

	// private ComboBox cmbSection;

	private TextField hospitalizationNoOfDays;

	private ComboBox cmbTypeOfDelivery;

	private DateField treatmentStartDate;

	private DateField treatmentEndDate;

	// -----------------------------------////

	private BeanItemContainer<State> container = new BeanItemContainer<State>(
			State.class);

	// Added for reason for date change.
	private TextField reasonForChangeInDOA;

	private TextField firNumberTxt;

	private OptionGroup policeReportedAttached;

	private Date admissionDateValue;

	private Boolean isValid = false;

	@Inject
	private PAReconsiderRODRequestListenerTable reconsiderRequestDetails;

	private ComboBox cmbReasonForReconsideration;

	private ComboBox cmbReconsiderationRequest;

	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;

	@Inject
	private ViewDetails viewDetailsObj;

	private VerticalLayout reconsiderationLayout;

	private TextField txtReasonForReferringFromBilling;

	private TextField txtRemarksForReferringFromBilling;

	private TextField txtReasonForReferringFromClaimApproval;

	private TextField txtRemarksForReferringFromClaimApproval;

	private TextField txtReasonForReferringFromFinancialApproval;

	private TextField txtRemarksForReferringFromFinancialApproval;

	private ComboBox cmbPABenefits;

	@Inject
	private Instance<PABenefitsListenerTable> paBenefitsTableObj;
	private PABenefitsListenerTable paBenefitsTable;

	private TextField benefitsTableCaptionFld;

	@Inject
	private Instance<PACoversListenerTable> paOptionalCoversTableObj;
	private PACoversListenerTable paOptionalCoversTable;

	@Inject
	private Instance<PACoversListenerTable> paAddOnCoversTableObj;
	private PACoversListenerTable paAddOnCoversTable;

	@Inject
	private Instance<MedicalDecisionFVRGrading> fvrGradingTableInstance;

	private MedicalDecisionFVRGrading fvrGradingTableObj;

/*	private CheckBox investigationReportReviewedChk;

	private ComboBox investigatorName;

	private TextArea investigationReviewRemarks;*/

	@Inject
	private Instance<MedicalVerificationTable> medicalVerificationInstance;

	private MedicalVerificationTable medicalVerificationTableObj;

	@Inject
	private Instance<TreatmentQualityVerificationTable> treatmentVerificationTableInstance;

	private TreatmentQualityVerificationTable treatmentVerificationTableObj;

	@Inject
	private Instance<PAClaimRequestMedicalDecisionButtons> claimRequestButton;

	private PAClaimRequestMedicalDecisionButtons claimRequestButtonObj;

	private ClaimRequestFileUploadUI specialistWindow = new ClaimRequestFileUploadUI();

	@Inject
	private UploadedFileViewUI fileViewUI;

	private OptionGroup optAccidentDeathFlag;

	private DateField accidentOrDeathDate;

	private ComboBox cmbCauseOfAccident;

	private DateField dftDate;

	private OptionGroup preExistingDisabilities;

	private FormLayout disabilitiesLayout;

	private TextArea txtDisablitiesRemarks;

	private static Validator validator;

	private List<PABenefitsDTO> changeList;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;

	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	private CheckBox chkNomineeDeceased;

	@Inject
	private Instance<InvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
	private InvestigationReviewRemarksTable invsReviewRemarksTableObj;
	
//	private FormLayout legaHeirLayout;
	// private Object value;

	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}

	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		premedicalSuggestion = new ComboBox("Pre-Medical Suggestion");
		premedicalCategory = new ComboBox("Category");
		premedicalRemarks = new TextArea("Rejection Remarks (Pre-medical)");
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	public Boolean alertMessageForPED() {
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.PED_RAISE_MESSAGE + "</b>", ContentMode.HTML);
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
		// dialog.setCaption("Alert");
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
				bean.setIsPEDInitiated(false);
				if (bean.getIsPedWatchList()) {
					alertMessageForPEDWatchList();
				} else if ((bean.getHospitalizaionFlag() || bean
						.getPartialHospitalizaionFlag())
						&& bean.getPostHospitalizaionFlag()
						&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
								.getPostHospClaimedAmount(bean) > 5000d)) {
					if (!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}

				} else if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
			}
		});
		return true;
	}

	public Boolean alertMessageForAutoRestroation() {/*
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.AUTO_RESTORATION_MESSAGE + "</b>",
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
		// dialog.setCaption("Alert");
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
				bean.setIsAutoRestorationDone(true);
			}
		});
		*/
		return true;
	}
	
	public void get64VbChequeStatusAlert() {
		final MessageBox showInfo = showInfoMessageBox(SHAConstants.VB64STATUSALERT);
		Button homeButton = showInfo.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				showInfo.close();

				if (bean.getNewIntimationDTO().getPolicy().getPolicyType() != null
						&& bean.getNewIntimationDTO().getPolicy()
								.getPolicyType().getKey()
								.equals(ReferenceTable.FRESH_POLICY)
						&& bean.getClaimDTO().getClaimSectionCode() != null
						&& bean.getClaimDTO()
								.getClaimSectionCode()
								.equalsIgnoreCase(
										ReferenceTable.HOSPITALIZATION_SECTION_CODE)
						&& bean.getNewIntimationDTO()
								.getPolicy()
								.getProduct()
								.getCode()
								.equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)) {
					StarCommonUtils.alertMessage(getUI(),
							SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
				} else if (bean.getNewIntimationDTO().getPolicy()
						.getPolicyType() != null
						&& bean.getNewIntimationDTO().getPolicy()
								.getPolicyType().getKey()
								.equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct()
								.getWaitingPeriod()
								.equals(ReferenceTable.WAITING_PERIOD)) {
					popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				} else if (bean
						.getNewIntimationDTO()
						.getHospitalDto()
						.getReInstatedBy()
						.equalsIgnoreCase(
								SHAConstants.HOSPITAL_SUSPENDED_BY_RAW)) {
					getHospitalReinstatedAlert();
				}
			}
		});

	}
	 

	public Boolean alertMessageForPEDWatchList() {
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.PED_WATCHLIST + "</b>", ContentMode.HTML);
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
		// dialog.setCaption("Alert");
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
				bean.setIsPedWatchList(false);
				if ((bean.getHospitalizaionFlag() || bean
						.getPartialHospitalizaionFlag())
						&& bean.getPostHospitalizaionFlag()
						&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
								.getPostHospClaimedAmount(bean) > 5000d)) {
					if (!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}

				} else if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
			}
		});
		return true;
	}

	public Boolean alertForHospitalDiscount() {
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.HOSPITAL_DISCOUNT_ALERT + "</b>",
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
		// dialog.setCaption("Alert");
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
				// bean.setIsPedWatchList(false);

				if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
			}
		});
		return true;
	}

	public Component getContent() {

		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		} else if (!bean.getSuspiciousPopupMap().isEmpty()
				&& !bean.getIsPopupMessageOpened()) {
			suspiousPopupMessage();
		} else if (bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if (bean.getIsPedWatchList()) {
			alertMessageForPEDWatchList();
		} else if ((bean.getHospitalizaionFlag() || bean
				.getPartialHospitalizaionFlag())
				&& bean.getPostHospitalizaionFlag()
				&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
						.getPostHospClaimedAmount(bean) > 5000d)) {
			if (!bean.getShouldShowPostHospAlert()) {
				alertMessageForPostHosp();
			}
		} else if (bean.getIsHospitalDiscountApplicable()) {
			alertForHospitalDiscount();
		} else if (bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		}
				
		if (!ReferenceTable.getGPAProducts().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			if (this.bean.getClaimCount() > 1) {
				alertMessageForClaimCount(this.bean.getClaimCount());
			}
		}
		else{
			int gpaClaimCount = dbService.getGPAClaimCount(bean.getPolicyKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			if (gpaClaimCount > 1) {
				alertMessageForClaimCount(this.bean.getClaimCount());
			}
			
		}
		
		if (bean.getIs64VBChequeStatusAlert()) {
			get64VbChequeStatusAlert();
		}
		
		if(bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getInsuredPatient() != null && 
				bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null 
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey() != null
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey().equals(SHAConstants.DEPENDANT_CHILD_KEY)
				&& bean.getPolicyDto().getProduct().getCode().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_CODE)){
			SHAUtils.showAlertMessageBox(SHAConstants.DEPENDANT_CHILD_ALERT);
		}
		
		if(bean.getNewIntimationDTO().getIntimationId() != null) {
			String intimationNumber = bean.getNewIntimationDTO().getIntimationId();
			String result = dbService.getHospitalClaimAlertFlag(intimationNumber);
			if(result != null && result.equals("Y")){
				SHAUtils.showMessageBox(SHAConstants.HOSPITAL_CLAIM_ALERT_MSG,"Information");
				

			}
		}
		
		String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
		String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
		if((bean.getIcrAgentValue() != null && !bean.getIcrAgentValue().isEmpty() 
				&& (Integer.parseInt(bean.getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
				|| bean.getSmAgentValue() != null && !bean.getSmAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getSmAgentValue()) >= Integer.parseInt(smpercentage))){
			SHAUtils.showICRAgentAlert(bean.getIcrAgentValue(), agentpercentage, bean.getSmAgentValue(), smpercentage);
		}
		//code added for CR GLX2021016
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			SHAUtils.showPolicyInstalmentAlert();
		}
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
		}
		initBinder();

		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		// reasonForAdmissionTxt.setRequired(true);
		reasonForAdmissionTxt.setEnabled(true);

		dischargeDate = (DateField) binder.buildAndBind("Date of Discharge",
				"dischargeDateForPa", DateField.class);
		
		
		
		dischargeDate.setRequired(false);
		dischargeDate.setValidationVisible(false);

		// Changed as per BA team.
		dischargeDate.setEnabled(true);

		admissionDate = (DateField) binder.buildAndBind("Date of Admission",
				"admissionDate", DateField.class);
		admissionDate.setRequired(true);
		admissionDate.setValidationVisible(false);
		/*
		 * systemOfMedicineTxt = (TextField)binder.buildAndBind(
		 * "System of Medicine", "systemOfMedicine", TextField.class);
		 * systemOfMedicineTxt.setMaxLength(50); CSValidator
		 * systemOfMedicineValidator = new CSValidator();
		 * systemOfMedicineValidator.extend(systemOfMedicineTxt);
		 * systemOfMedicineValidator.setRegExp("^[a-zA-Z 0-9]*$");
		 * systemOfMedicineValidator.setPreventInvalidTyping(true);
		 */

		/*
		 * cmbHospitalisationDueTo = (ComboBox) binder.buildAndBind(
		 * "Hospitalisation Due to", "hospitalisationDueTo", ComboBox.class);
		 */
		reasonForChangeInDOA = (TextField) binder.buildAndBind(
				"Reason For Change in DOA", "changeOfDOA", TextField.class);
		reasonForChangeInDOA.setVisible(false);
		reasonForChangeInDOA.setMaxLength(100);
		reasonForChangeInDOA.setRequired(false);

		if (null != this.bean.getPreauthDataExtractionDetails()
				.getChangeOfDOA()
				&& !("").equals(this.bean.getPreauthDataExtractionDetails()
						.getChangeOfDOA())) {
			admissionDate.setValue(this.bean.getPreauthDataExtractionDetails()
					.getAdmissionDate());
			reasonForChangeInDOA.setValue(this.bean
					.getPreauthDataExtractionDetails().getChangeOfDOA());
			// binder.bind(reasonForChangeInDOA, "changeOfDOA");
			reasonForChangeInDOA.setVisible(true);
			// reasonForChangeInDOA.setRequired(true);
		}

		noOfDaysTxt = (TextField) binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		// noOfDaysTxt.setRequired(true);
		noOfDaysTxt.setMaxLength(4);
		/*
		 * cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
		 * "Nature of Treatment", "natureOfTreatment", ComboBox.class);
		 * cmbNatureOfTreatment.setRequired(true); firstConsultantDate =
		 * (PopupDateField) binder.buildAndBind( "1st Consultation Date",
		 * "firstConsultantDate", PopupDateField.class); corpBufferChk =
		 * (CheckBox) binder.buildAndBind("", "corpBuffer", CheckBox.class);
		 */
		// Added for corp buffer checkbox validation.
		// editCorpBufferChk();

		/*
		 * criticalIllnessChk = (CheckBox)
		 * binder.buildAndBind("Critical Illness", "criticalIllness",
		 * CheckBox.class);
		 */
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		cmbRoomCategory.setRequired(false);
		cmbRoomCategory.setValidationVisible(false);
		// cmbRoomCategory.setNullSelectionAllowed(true);
		/*
		 * cmbSpecifyIllness = (ComboBox) binder.buildAndBind("Specify",
		 * "specifyIllness", ComboBox.class);
		 * cmbSpecifyIllness.setReadOnly(true);
		 */
		
		ventilatorSupportOption = (OptionGroup) binder.buildAndBind(
				"Ventilator Support", "ventilatorSupport", OptionGroup.class);
		ventilatorSupportOption.addItems(getReadioButtonOptions());
		ventilatorSupportOption.setItemCaption(true, "Yes");
		ventilatorSupportOption.setItemCaption(false, "No");
		ventilatorSupportOption.setStyleName("horizontal");
		ventilatorSupportOption.setEnabled(false);
		
		cmbTreatmentType = (ComboBox) binder.buildAndBind("Treatment Type",
				"treatmentTypeForPA", ComboBox.class);
		cmbTreatmentType.setRequired(false);
		/*
		 * cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
		 * "patientStatus", ComboBox.class);
		 */
		/*
		 * automaticRestorationTxt = (TextField) binder.buildAndBind(
		 * "Automatic Restoration", "autoRestoration", TextField.class);
		 * automaticRestorationTxt.setEnabled(false); cmbIllness = (ComboBox)
		 * binder.buildAndBind("Illness", "illness", ComboBox.class);
		 * if((SHAConstants
		 * .AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt
		 * .getValue()) ||
		 * (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt
		 * .getValue())) { cmbIllness.setEnabled(false); }
		 * 
		 * changeInDiagnosis = (OptionGroup) binder.buildAndBind(
		 * "Change In Diagnosis", "changeInDiagnosis", OptionGroup.class);
		 * 
		 * // changeInDiagnosis = new OptionGroup("Change In Diagnosis");
		 * changeInDiagnosis.addItems(getReadioButtonOptions());
		 * changeInDiagnosis.setItemCaption(true, "Yes");
		 * changeInDiagnosis.setItemCaption(false, "No");
		 * changeInDiagnosis.setValue(true);
		 * changeInDiagnosis.setStyleName("horizontal");
		 * 
		 * if(!this.bean.getIsCashlessType()) {
		 * changeInDiagnosis.setEnabled(false); }
		 * 
		 * if(this.bean.getPreauthDataExtractionDetails().getChangeInDiagnosis()
		 * != null){
		 * changeInDiagnosis.setValue(this.bean.getPreauthDataExtractionDetails
		 * ().getChangeInDiagnosis()); }
		 * 
		 * 
		 * 
		 * cmbSection = (ComboBox) binder.buildAndBind( "Section", "section",
		 * ComboBox.class);
		 * 
		 * if(this.bean.getNewIntimationDTO() != null &&
		 * !ReferenceTable.getSectionKeys
		 * ().containsKey(this.bean.getNewIntimationDTO
		 * ().getPolicy().getProduct().getKey()) ) {
		 * cmbSection.setEnabled(false); }
		 * 
		 * if(cmbSection != null && cmbSection.getValue() != null){
		 * this.bean.getPreauthDataExtractionDetails().setSection((SelectValue)
		 * cmbSection.getValue()); }
		 */

		claimRequestButtonObj = claimRequestButton.get();
		claimRequestButtonObj.initView(this.bean, wizard/*,
				investigationReportReviewedChk*/);

		/*
		 * FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		 * illnessFLayout.setCaption("Critical Illness");
		 * illnessFLayout.setMargin(false);
		 */
		/*
		 * FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		 * bufferFLayout.setCaption("Corp Buffer");
		 * bufferFLayout.setMargin(false);
		 */

		/*
		 * domicillaryHospitalisation = (OptionGroup) binder.buildAndBind(
		 * "Claim for Domicillary Hospitalisation",
		 * "domicillaryHospitalisation", OptionGroup.class);
		 * 
		 * domicillaryHospitalisation.addItems(getReadioButtonOptions());
		 * domicillaryHospitalisation.setItemCaption(true, "Yes");
		 * domicillaryHospitalisation.setItemCaption(false, "No");
		 * domicillaryHospitalisation.setStyleName("horizontal");
		 */

		optAccidentDeathFlag = (OptionGroup) binder.buildAndBind(
				"Accident / Death", "accidentOrDeath", OptionGroup.class);
		optAccidentDeathFlag.addItems(getReadioButtonOptions());
		optAccidentDeathFlag.setItemCaption(true, "Accident");
		optAccidentDeathFlag.setItemCaption(false, "Death");
		optAccidentDeathFlag.setStyleName("horizontal");
		optAccidentDeathFlag.setReadOnly(false);
		/**
		 * done as a part of demo feedback.
		 * */
		// optAccidentDeathFlag.setReadOnly(true);

		preExistingDisabilities = (OptionGroup) binder.buildAndBind(
				"Pre-Existing Disabilites", "preExistingDisabilities",
				OptionGroup.class);
		preExistingDisabilities.addItems(getReadioButtonOptions());
		preExistingDisabilities.setItemCaption(true, "Yes");
		preExistingDisabilities.setItemCaption(false, "No");
		preExistingDisabilities.setStyleName("horizontal");
		preExistingDisabilities.setReadOnly(false);

		txtDisablitiesRemarks = (TextArea) binder.buildAndBind("Details",
				"disabilitesRemarks", TextArea.class);
		txtDisablitiesRemarks.setVisible(false);

		disabilitiesLayout = new FormLayout();

		accidentOrDeathDate = (DateField) binder.buildAndBind(
				"Date Of Accident/Death", "accidentDeathDate", DateField.class);

		accidentOrDeathDate.setReadOnly(false);

		cmbCauseOfAccident = (ComboBox) binder.buildAndBind(
				"Cause of Accident", "causeOfAccident", ComboBox.class);

		dftDate = (DateField) binder.buildAndBind("DFT Date", "dftDate",
				DateField.class);
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		if (optAccidentDeathFlag.getValue() != null && !(optAccidentDeathFlag.getValue()).equals("") && !(Boolean)optAccidentDeathFlag.getValue()) {
			setRequiredAndValidation(dateOfDeath);
			mandatoryFields.add(dateOfDeath);
		} else {
			dateOfDeath.setRequired(false);
			mandatoryFields.remove(dateOfDeath);
			dateOfDeath.setValue(null);
		}
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);		

		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/

		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);

		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
	
		/*
		 * firstFLayout = new FormLayout(admissionDate, reasonForChangeInDOA,
		 * cmbRoomCategory, illnessFLayout, cmbSpecifyIllness, dischargeDate,
		 * systemOfMedicineTxt,domicillaryHospitalisation,
		 * cmbHospitalisationDueTo);
		 */

		firstFLayout = new FormLayout(noOfDaysTxt,reasonForAdmissionTxt,cmbRoomCategory,ventilatorSupportOption,cmbCauseOfAccident,dftDate,preExistingDisabilities,
				txtDisablitiesRemarks);

		firstFLayout.setSpacing(true);
		// firstFLayout.setMargin(true);
		/*
		 * secondFLayout = new FormLayout(optAccidentDeathFlag,
		 * accidentOrDeathDate , cmbCauseOfAccident, dftDate
		 * ,reasonForAdmissionTxt, noOfDaysTxt, preExistingDisabilities,
		 * disabilitiesLayout, cmbNatureOfTreatment, firstConsultantDate,
		 * bufferFLayout, automaticRestorationTxt, cmbIllness, cmbSection);
		 */

		secondFLayout = new FormLayout(optAccidentDeathFlag,dateOfAccident,dateOfDeath,dateOfDisablement,admissionDate,reasonForChangeInDOA,
				dischargeDate,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);

		if (this.bean.getPreauthDataExtractionDetails()
				.getDomicillaryHospitalisation() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getDomicillaryHospitalisation()) {
			generatedFieldsBasedOnDomicillaryHosp(true);
		}

		PADiganosisDetailsListenerTable diagnosisDetailsTableInstance = diagnosisDetailsTable
				.get();
		diagnosisDetailsTableInstance.init(this.bean,
				SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);
		this.diagnosisDetailsTableObj = diagnosisDetailsTableInstance;

		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init(
				"Preauth Details (Hospitalization Expenses)", false, false);
		
		// Removing preauth table.
		/*
		 * HorizontalLayout preauthDetailsLayout = new HorizontalLayout();
		 * preauthDetailsLayout.addComponent(previousPreAuthDetailsTableObj);
		 * preauthDetailsLayout.setMargin(false);
		 */

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
		// formHLayout.setWidth("100%");
		formHLayout.setSpacing(true);
		// formHLayout.setMargin(true);
		PreauthCoordinatorView preauthCoordinatorViewInstance = reimbursementCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(this.wizard,
				SHAConstants.CLAIM_REQUEST);
		// preauthCoordinatorViewInstance.setWizard(this.wizard ,
		// SHAConstants.PRE_AUTH);
		this.reimbursementCoordinatorViewInstance = preauthCoordinatorViewInstance;
		preauthCoordinatorViewInstance.init(this.bean);

		tableVLayout = new VerticalLayout();
		tableVLayout.setSpacing(true);

		treatmentFLayout = new FormLayout(cmbTreatmentType);
		treatmentFLayout.setMargin(false);
		// patientStatusFLayout = new FormLayout(cmbPatientStatus);
		// patientStatusFLayout.setMargin(false);
		dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout);
		/*
		 * dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout,
		 * patientStatusFLayout);
		 */
		dynamicElementsHLayout.setWidth("100%");
		dynamicElementsHLayout.setMargin(false);

		/*
		 * coveredPreviousClaim = (OptionGroup) binder.buildAndBind(
		 * "Previously Covered by any other Mediclaim / Health Insurance",
		 * "coveredPreviousClaim", OptionGroup.class);
		 * 
		 * coveredPreviousClaim.addItems(getReadioButtonOptions());
		 * coveredPreviousClaim.setItemCaption(true, "Yes");
		 * coveredPreviousClaim.setItemCaption(false, "No");
		 * coveredPreviousClaim.select(false);
		 * coveredPreviousClaim.setStyleName("horizontal");
		 * 
		 * 
		 * preHospitalisationPeriod = (TextField) binder.buildAndBind(
		 * "Pre-Hospitalisation Period(days)", "preHospitalisationPeriod",
		 * TextField.class); preHospitalisationPeriod.setMaxLength(10);
		 * preHospitalisationPeriod.setWidth("40px"); CSValidator
		 * preHospitalisationPeriodValidator = new CSValidator();
		 * preHospitalisationPeriodValidator.extend(preHospitalisationPeriod);
		 * preHospitalisationPeriodValidator.setRegExp("^[0-9]*$");
		 * preHospitalisationPeriodValidator.setPreventInvalidTyping(true);
		 * 
		 * postHospitalisationPeriod = (TextField) binder.buildAndBind(
		 * "Post-Hospitalisation Period(days)", "postHospitalisationPeriod",
		 * TextField.class); postHospitalisationPeriod.setMaxLength(10);
		 * postHospitalisationPeriod.setWidth("40px");
		 * 
		 * CSValidator postHospitalisationPeriodValidator = new CSValidator();
		 * postHospitalisationPeriodValidator.extend(postHospitalisationPeriod);
		 * postHospitalisationPeriodValidator.setRegExp("^[0-9]*$");
		 * postHospitalisationPeriodValidator.setPreventInvalidTyping(true);
		 */

		otherClaimsFLayout = new VerticalLayout();

		if (this.bean.getPreHospitalizaionFlag()
				|| this.bean.getPostHospitalizaionFlag()) {
			// admissionDate.setEnabled(false);
			// dischargeDate.setEnabled(false);
		}

		if (admissionDate != null
				&& dischargeDate != null
				&& admissionDate.getValue() != null
				&& dischargeDate.getValue() != null
				&& bean.getClaimDTO().getClaimType() != null
				&& bean.getClaimDTO().getClaimType().getId() != null
				&& bean.getClaimDTO().getClaimType().getId()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			// admissionDate.setEnabled(false);
			// dischargeDate.setEnabled(false);
		}

		if (admissionDate != null && admissionDate.getValue() != null) {
			// admissionDate.setEnabled(false);
		}

		if (dischargeDate != null && dischargeDate.getValue() != null) {
			// dischargeDate.setEnabled(false);
		}

		/*
		 * HorizontalLayout preandPostHospitalLayout = new HorizontalLayout(new
		 * FormLayout(preHospitalisationPeriod), new
		 * FormLayout(postHospitalisationPeriod));
		 * preandPostHospitalLayout.setSpacing(true);
		 */

		hospitalDetailsObj = hospitalDetailsInstance.get();
		hospitalDetailsObj.initView(this.bean);

		/*
		 * FormLayout fLayout = new FormLayout(changeInDiagnosis);
		 * fLayout.setSpacing(false); fLayout.setMargin(false);
		 */
		/*
		 * FormLayout fLayout1 = new FormLayout(coveredPreviousClaim);
		 * fLayout1.setSpacing(false); fLayout1.setMargin(false);
		 */

		if (this.bean.getNewIntimationDTO().getHospitalDto() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto()
						.getHospitalType() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto()
						.getHospitalType().getId()
						.equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)) {
			// wholeVLayout = new VerticalLayout(formHLayout, fLayout,
			// diagnosisDetailsTableInstance, dynamicElementsHLayout,
			// tableVLayout, fLayout1, otherClaimsFLayout,
			// preandPostHospitalLayout, preauthCoordinatorViewInstance);
			wholeVLayout = new VerticalLayout(formHLayout,
					diagnosisDetailsTableInstance, dynamicElementsHLayout,
					tableVLayout, otherClaimsFLayout);
		} else {
			// wholeVLayout = new VerticalLayout(formHLayout, fLayout,
			// diagnosisDetailsTableInstance, dynamicElementsHLayout,
			// tableVLayout, fLayout1, otherClaimsFLayout,
			// preandPostHospitalLayout, hospitalDetailsObj,
			// preauthCoordinatorViewInstance);
			wholeVLayout = new VerticalLayout(formHLayout,
					diagnosisDetailsTableInstance, dynamicElementsHLayout,
					tableVLayout, otherClaimsFLayout, hospitalDetailsObj);
		}

		wholeVLayout.setSpacing(false);
		
		if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				   && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())) {
			
			buildNomineeLayout();
		}

		addListener();
		
		if(null != bean.getPreauthDataExtractionDetails().getPreExistingDisabilities())
		{
			Boolean value = bean.getPreauthDataExtractionDetails().getPreExistingDisabilities();
			if(null != value && null != preExistingDisabilities)
			{
				preExistingDisabilities.setValue(!value);
				preExistingDisabilities.setValue(value);
			}
		}
		
		addAdmissionDateChangeListener();
		addDiagnosisNameChangeListener();
		// mandatoryFields.add(admissionDate);
		/**
		 * Discharge date, treatment type mandatory validation has been removed
		 * as per sathish sire.
		 * */
		 mandatoryFields.add(cmbRoomCategory);
		// mandatoryFields.add(cmbTreatmentType);
		// mandatoryFields.add(cmbPatientStatus);
		 mandatoryFields.add(dischargeDate);
		 mandatoryFields.add(cmbCauseOfAccident);

		if (this.bean.getPreauthDataExtractionDetails()
				.getCoveredPreviousClaim() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getCoveredPreviousClaim()) {
			// coveredPreviousClaim.setValue(true);
			generatedFieldsBasedOnOtherClaims(true);
		}

		admissionDateValue = admissionDate.getValue();

		admissionDate.setValue(bean.getPreauthDataExtractionDetails()
				.getAdmissionDate());

		if (admissionDate != null && dischargeDate != null
				&& admissionDate.getValue() != null
				&& dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(
					admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}

		showOrHideValidation(false);

		VerticalLayout preauthAndReconsiderLayout = new VerticalLayout();
		// preauthAndReconsiderLayout.addComponent(preauthDetailsLayout);
		preauthAndReconsiderLayout.addComponent(buildRefferedToMedicalLayout());
		preauthAndReconsiderLayout.addComponent(commonValues());		
		preauthAndReconsiderLayout.addComponent(wholeVLayout);
		preauthAndReconsiderLayout.addComponent(buildBenefitsTableLayout());
		preauthAndReconsiderLayout.addComponent(buildCoversTableLayout());
		preauthAndReconsiderLayout.addComponent(buildFVRGradingTableLayout());
		preauthAndReconsiderLayout.addComponent(buildInvestigationLayout());
		// preauthAndReconsiderLayout.addComponent(buildMedicalAndTreatmentVerficationLayout());

		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				claimRequestButtonObj);
		preauthAndReconsiderLayout.addComponent(buttonsHLayout);
		fireViewEvent(
				PAClaimRequestDataExtractionPagePresenter.CHECK_INVESTIGATION_INITIATED,
				this.bean.getClaimKey());
		return preauthAndReconsiderLayout;
	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		// reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		// Setting this feild readOnly true, since this needs to be editable as
		// per sathish sir.
		// reasonForAdmissionTxt.setReadOnly(true);
		// reasonForAdmissionTxt.setEnabled(false);
		this.claimRequestButtonObj.setReferenceData(referenceData);

		BeanItemContainer<SelectValue> treatementType = (BeanItemContainer<SelectValue>) referenceData
				.get("treatmentType");
		BeanItemContainer<SelectValue> hospitalizationDueTo = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalisationDueTo");
		BeanItemContainer<SelectValue> roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");
		BeanItemContainer<SelectValue> natureOfTreatment = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfTreatment");
		BeanItemContainer<SelectValue> patientStatus = (BeanItemContainer<SelectValue>) referenceData
				.get("patientStatus");
		BeanItemContainer<SelectValue> illness = (BeanItemContainer<SelectValue>) referenceData
				.get("illness");

		BeanItemContainer<SelectValue> criticalIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("criticalIllness");

		BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
				.get("section");

		/*BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData
				.get("investigatorName");*/
		
		BeanItemContainer<SelectValue> investigatorNameContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("investigatorName");
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		/*cmbCatastrophicLoss.setContainerDataSource(catastrophicLoss);
		cmbCatastrophicLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCatastrophicLoss.setItemCaptionPropertyId("value");*/
		
		cmbNatureOfLoss.setContainerDataSource(natureOfLoss);
		cmbNatureOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfLoss.setItemCaptionPropertyId("value");
		
		cmbCauseOfLoss.setContainerDataSource(causeOfLoss);
		cmbCauseOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfLoss.setItemCaptionPropertyId("value");
		
		/*if(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			for(SelectValue val : catastrophicLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbCatastrophicLoss.setValue(val.getValue());
					break;
				}
			}
			
		}else if(cmbCatastrophicLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbCatastrophicLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCatastrophicLoss(sectionValue);
		}*/
		
		if(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			for(SelectValue val : natureOfLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbNatureOfLoss.setValue(val);
					break;
				}
			}
			
		}else if(cmbNatureOfLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbNatureOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setNatureOfLoss(sectionValue);
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			for(SelectValue val : causeOfLoss.getItemIds()) {
				if(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss().getValue().equalsIgnoreCase(val.getValue())) {
					cmbCauseOfLoss.setValue(val);
					break;
				}
			}
		}else if(cmbCauseOfLoss.getValue() == null) {
			SelectValue sectionValue = (SelectValue) cmbCauseOfLoss.getValue();
			this.bean.getPreauthDataExtractionDetails().setCauseOfLoss(sectionValue);
		}

		this.bean.getPolicyDto().setAdmissionDate(
				this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
		// fireViewEvent(PreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA,
		// this.bean);

		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");

		/*
		 * cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		 * cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbHospitalisationDueTo.setItemCaptionPropertyId("value");
		 */

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");
		
				

		// if(null !=
		// this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() &&
		// this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null
		// &&
		// this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase().equalsIgnoreCase("n"))
		// {
		// List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
		// List<SelectValue> allopathicValues = new ArrayList<SelectValue>();
		// for (SelectValue selectValue : itemIds2) {
		// /*if(selectValue.getValue().toString().toLowerCase().contains("allopathic"))
		// {
		// allopathicValues.add(selectValue);
		// }*/
		// /***
		// * Fix for issue 662. As per above code, allopathic and non-allopathic
		// , both contains
		// * allopathic string. Hence in the final list , both values will be
		// added. But to exclude
		// * non allopathic string , instead of validating allopatich string,
		// need to check whether
		// * the resultant value contains "non". Hence above code is commented
		// and below is added.
		// */
		// if(!selectValue.getValue().toString().toLowerCase().contains("non"))
		// {
		// allopathicValues.add(selectValue);
		// }
		// }
		// natureOfTreatment.removeAllItems();
		// natureOfTreatment.addAll(allopathicValues);
		// }

		/*
		 * cmbNatureOfTreatment.setContainerDataSource(natureOfTreatment);
		 * cmbNatureOfTreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbNatureOfTreatment.setItemCaptionPropertyId("value");
		 */

		/*
		 * Collection<?> itemIds =
		 * cmbNatureOfTreatment.getContainerDataSource().getItemIds();
		 * if(itemIds != null && !itemIds.isEmpty() &&
		 * (bean.getPreauthDataExtractionDetails().getNatureOfTreatment() ==
		 * null)) { cmbNatureOfTreatment.setValue(itemIds.toArray()[0]);
		 * cmbNatureOfTreatment.setNullSelectionAllowed(false); }
		 * 
		 * cmbSection.setContainerDataSource(section);
		 * cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbSection.setItemCaptionPropertyId("value");
		 */

		/*
		 * cmbPatientStatus.setContainerDataSource(patientStatus);
		 * cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbPatientStatus.setItemCaptionPropertyId("value");
		 */
		/*
		 * cmbIllness.setContainerDataSource(illness);
		 * cmbIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbIllness.setItemCaptionPropertyId("value");
		 * cmbIllness.setNullSelectionAllowed(false);
		 */

		/*
		 * cmbSpecifyIllness.setReadOnly(false);
		 * cmbSpecifyIllness.setContainerDataSource(criticalIllness);
		 * cmbSpecifyIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * cmbSpecifyIllness.setItemCaptionPropertyId("value");
		 * cmbSpecifyIllness.setReadOnly(true);
		 * 
		 * if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness()
		 * != null) { this.cmbSpecifyIllness.setReadOnly(false);
		 * this.cmbSpecifyIllness.setValue(this.bean
		 * .getPreauthDataExtractionDetails().getSpecifyIllness()); }
		 */

		if (this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null) {/*
																						 * this
																						 * .
																						 * cmbNatureOfTreatment
																						 * .
																						 * setValue
																						 * (
																						 * this
																						 * .
																						 * bean
																						 * .
																						 * getPreauthDataExtractionDetails
																						 * (
																						 * )
																						 * .
																						 * getNatureOfTreatment
																						 * (
																						 * )
																						 * )
																						 * ;
																						 */
		}

		if (this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null) {
			this.cmbRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory());
		}

		if (this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()!= null) {
			this.ventilatorSupportOption.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
			ventilatorSupportOption.setEnabled(true);
			ventilatorSupportOption.setRequired(true);
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getTreatmentType() != null) {
			this.cmbTreatmentType.setValue(this.bean
					.getPreauthDataExtractionDetails().getTreatmentType());
		}
		
		
		/*
		 * if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() !=
		 * null) { this.cmbPatientStatus.setValue(this.bean
		 * .getPreauthDataExtractionDetails().getPatientStatus());
		 * if(this.cmbPatientStatus.getValue() != null &&
		 * this.cmbPatientStatus.getValue().toString().toLowerCase()
		 * .contains("deceased")) { this.cmbPatientStatus.setEnabled(false); } }
		 */

		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {/*
																				 * this
																				 * .
																				 * cmbIllness
																				 * .
																				 * setValue
																				 * (
																				 * this
																				 * .
																				 * bean
																				 * .
																				 * getPreauthDataExtractionDetails
																				 * (
																				 * )
																				 * .
																				 * getIllness
																				 * (
																				 * )
																				 * )
																				 * ;
																				 */
		}

		if (this.bean.getPreauthDataExtractionDetails()
				.getHospitalisationDueTo() != null) {
			// this.cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo());
		}

		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {/*
																				 * this
																				 * .
																				 * cmbSection
																				 * .
																				 * setValue
																				 * (
																				 * this
																				 * .
																				 * bean
																				 * .
																				 * getPreauthDataExtractionDetails
																				 * (
																				 * )
																				 * .
																				 * getSection
																				 * (
																				 * )
																				 * )
																				 * ;
																				 */
		}

		this.reimbursementCoordinatorViewInstance.setUpReference(referenceData);
		this.diagnosisDetailsTableObj.setReferenceData(referenceData);
		// this.claimedDetailsTableObj.setDBCalculationValues((Map<String,
		// Double>)referenceData.get("claimDBDetails"));

		if (this.procedurdTableObj != null) {
			this.procedurdTableObj.setReferenceData(referenceData);
		}

		if (this.bean.getClaimDTO() != null
				&& this.bean.getClaimDTO().getClaimType() != null
				&& this.bean.getClaimDTO().getClaimType().getId()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
				&& this.bean.getPreauthDataExtractionDetails()
						.getTreatmentType() != null) {
			cmbTreatmentType.setEnabled(false);
		}

		if (fvrGradingTableObj != null) {
			fvrGradingTableObj.setupReferences(referenceData);
		}

		loadPABenefitsDropDown();
		BeanItemContainer<SelectValue> causeOfAccident = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfAccident");
		// cmbPABenefits

		cmbCauseOfAccident.setContainerDataSource(causeOfAccident);
		cmbCauseOfAccident.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfAccident.setItemCaptionPropertyId("value");
		
		 for(int i = 0 ; i<causeOfAccident.size() ; i++)
		 	{
				if (null != bean.getPreauthDataExtractionDetails().getCauseOfAccident() && null != bean.getPreauthDataExtractionDetails().getCauseOfAccident().getValue() && ( bean.getPreauthDataExtractionDetails().getCauseOfAccident().getValue()).equalsIgnoreCase(causeOfAccident.getIdByIndex(i).getValue()))
				{
					this.cmbCauseOfAccident.setValue(causeOfAccident.getIdByIndex(i));
				}
			}
		

		referenceData.put("preauthDTO", bean);

		paAddOnCoversTable.setReferenceData(referenceData);
		paOptionalCoversTable.setReferenceData(referenceData);

		/*if (null != investigatorName) {
			investigatorName.setContainerDataSource(investigatorNameContainer);
			investigatorName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			investigatorName.setItemCaptionPropertyId("value");
		}*/

		/*
		 * this.medicalVerificationTableObj.setReference(referenceData);
		 * this.treatmentVerificationTableObj.setReference(referenceData);
		 */

		setTableValues();
	}

	private void loadPABenefitsDropDown() {
		BeanItemContainer<SelectValue> pabenefits = (BeanItemContainer<SelectValue>) referenceData
				.get("paBenefits");
		// cmbPABenefits

		cmbPABenefits.setContainerDataSource(pabenefits);
		cmbPABenefits.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPABenefits.setItemCaptionPropertyId("value");

		// if((this.bean.getPreauthDataExtractionDetails().getBenefitsValue()).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			for (int i = 0; i < pabenefits.size(); i++) {
				if (null != this.bean.getPreauthDataExtractionDetails()
						.getBenefitsValue()
						&& this.bean
								.getPreauthDataExtractionDetails()
								.getBenefitsValue()
								.equalsIgnoreCase(
										pabenefits.getIdByIndex(i).getValue())) {
					this.cmbPABenefits.setValue(pabenefits.getIdByIndex(i));
				}
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
		
		
		if (this.binder != null && !this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
			
		}
		/*
		 * Set<ConstraintViolation<PreauthDataExtaractionDTO>> validate =
		 * validator.validate(bean.getPreauthDataExtractionDetails());
		 * 
		 * if (validate.size() > 0) { //hasError = true; for
		 * (ConstraintViolation<PreauthDataExtaractionDTO> constraintViolation :
		 * validate) { if(constraintViolation.getRootBean() != null){
		 * PreauthDataExtaractionDTO rootBean =
		 * constraintViolation.getRootBean(); if(null != rootBean && (null ==
		 * rootBean.getNoOfDays() || null == rootBean.getTreatmentType() && null
		 * == rootBean.getDischargeDate())) { hasError = true; eMsg +=
		 * constraintViolation.getMessage(); }
		 * //errorMessages.add(constraintViolation.getMessage()); } } }
		 */

		/**
		 * commenting binder validation, as none of the fields are mandatory in
		 * PA.
		 * */

		/*
		 * if (!this.binder.isValid()) {
		 * 
		 * 
		 * for (Field<?> field : this.binder.getFields()) { //SelectValue
		 * selValue = this.binder.getPropertyId(field);
		 * 
		 * Object propertyId = (Object)this.binder.getPropertyId(field); String
		 * value =
		 * binder.getItemDataSource().getItemProperty(propertyId).getType
		 * ().getName(); //
		 * binder.getItemDataSource().getItemProperty(propertyId).
		 * 
		 * if(propertyId.equals(this.bean.getPreauthDataExtractionDetails().
		 * getRoomCategory())) { ErrorMessage errMsg = ((AbstractField<?>)
		 * field) .getErrorMessage(); if (errMsg != null) { eMsg +=
		 * errMsg.getFormattedHtmlMessage(); } hasError = true; }
		 *//**
		 * Pre auth data extraction dto is been used across health too. Hence
		 * removing the validations in that DTO might lead to error. Hence this
		 * removal of mandatory validation is implemented in PA medical page
		 * only.
		 * */
		/*
		 * 
		 * if(null != errMsg &&
		 * (errMsg.getFormattedHtmlMessage().equalsIgnoreCase
		 * ("Please Enter atleast one Diagnosis") ||
		 * errMsg.getFormattedHtmlMessage().equalsIgnoreCase(
		 * "<div><div>Please&#32;Select&#32;Discharge&#32;Date</div></div>") ||
		 * errMsg.getFormattedHtmlMessage().equalsIgnoreCase(
		 * "<div><div>Please&#32;Select&#32;Treatment&#32;Type&#46;</div></div>"
		 * ))) { hasError = false; } } }
		 */
		/*
		 * if(null != cmbPABenefits && null == cmbPABenefits.getValue()) {
		 * hasError = true; eMsg +=
		 * "Please Select atleast one benefits before processing </br>"; }
		 */

		/*
		 * if(noOfDaysTxt != null && noOfDaysTxt.getValue() == null ||
		 * (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().length() ==
		 * 0)) { hasError = true; eMsg += "Please Enter No of Days. </br>"; }
		 * if(reasonForAdmissionTxt != null && reasonForAdmissionTxt.getValue()
		 * == null || (reasonForAdmissionTxt.getValue() != null &&
		 * reasonForAdmissionTxt.getValue().length() == 0)) { hasError = true;
		 * eMsg += "Please Enter Reason for Admission. </br>"; }
		 */
		/*
		 * if(cmbNatureOfTreatment != null && cmbNatureOfTreatment.getValue() ==
		 * null) { hasError = true; eMsg +=
		 * "Please Select Nature of Treatment. </br>"; }
		 */

		/*
		 * if(null != cmbPABenefits && null == cmbPABenefits.getValue()) {
		 * hasError = true; eMsg +=
		 * "Please Select atleast one benefits before processing </br>"; }
		 */
		try {
			if (!this.reimbursementCoordinatorViewInstance.isValid()) {
				hasError = true;
				List<String> errors = this.reimbursementCoordinatorViewInstance
						.getErrors();
				for (String error : errors) {
					eMsg += error;
				}
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		 
		 

		/*
		 * if(this.reasonForChangeInDOA.isVisible() && !(null !=
		 * this.reasonForChangeInDOA && (null!=
		 * this.reasonForChangeInDOA.getValue() &&
		 * !("").equalsIgnoreCase(this.reasonForChangeInDOA.getValue())))) {
		 * hasError = true; eMsg +=
		 * "Please enter Reason For Change in DOA to Proceed Further. </br>";
		 * 
		 * }
		 */

		/*
		 * if((this.procedurdTableObj != null &&
		 * this.procedurdTableObj.getValues().isEmpty()) &&
		 * (this.newProcedurdTableObj != null &&
		 * this.newProcedurdTableObj.getValues().isEmpty())) { hasError = true;
		 * eMsg +=
		 * "Please Add Atleast one Procedure List Details to Proceed Further. </br>"
		 * ; }
		 * 
		 * if((null != this.paAddOnCoversTable &&
		 * this.paAddOnCoversTable.getValues().isEmpty())) { hasError = true;
		 * eMsg +=
		 * "Please Add Atleast one Add on Cover Details to Proceed Further. </br>"
		 * ; }
		 * 
		 * if((null != this.paOptionalCoversTable &&
		 * this.paOptionalCoversTable.getValues().isEmpty())) { hasError = true;
		 * eMsg +=
		 * "Please Add Atleast one Optional Cover Details to Proceed Further. </br>"
		 * ; }
		 */

		if ((null != this.paBenefitsTable && this.paBenefitsTable.getValues()
				.isEmpty())
				&& (null != this.paAddOnCoversTable && this.paAddOnCoversTable
						.getValues().isEmpty())
				&& (null != this.paOptionalCoversTable && this.paOptionalCoversTable
						.getValues().isEmpty())) {
			hasError = true;
			// eMsg +=
			// "Please Add Atleast one Benefits Details to Proceed Further. </br>";
			eMsg += "Please Add either one Benefits Details or one cover details or one optional cover details to Proceed Further. </br>";
		}
		

		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg += "Please Select Ventilator Support. </br>";
		}
		
		if(null != cmbPABenefits && null != cmbPABenefits.getValue())
			{
				SelectValue selValue = (SelectValue) cmbPABenefits.getValue();
				//if(null != selValue && ((ReferenceTable.DEATH_BENEFIT_MASTER_VALUE).equals(selValue.getId())))
				{
					if(null != this.paBenefitsTable && this.paBenefitsTable.getValues()
							.isEmpty())
					{
					eMsg += "Please Add Benefits Details.</br>";
					}
				}
			}
		
		if(null != this.cmbTreatmentType && null == this.cmbTreatmentType.getValue())
		{
			hasError = true;
			eMsg += "Please Select Treatment Type. </br>";
		}
		
		
		
		if(null != cmbPABenefits && null != cmbPABenefits.getValue())
		{
			SelectValue selValue = (SelectValue) cmbPABenefits.getValue();
			if(null != selValue && !((ReferenceTable.DEATH_BENEFIT_MASTER_VALUE).equals(selValue.getId())))
			{
				if(this.diagnosisDetailsTableObj != null && this.diagnosisDetailsTableObj.getValues().isEmpty()) {
					  hasError =  true; 
					  eMsg +=	"Please Add Atleast one Diagnosis Details to Proceed Further. </br>";
				  }
			}
		}
		
		 
		

		if (this.bean.getStatusKey() != null
				&& !this.bean.getStatusKey().equals(
						ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)) {

			if (this.medicalVerificationTableObj != null
					&& !this.medicalVerificationTableObj.isValid()) {
				hasError = true;
				List<String> errors = this.medicalVerificationTableObj
						.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.treatmentVerificationTableObj != null
				&& !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj
					.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		if (this.treatmentVerificationTableObj != null
				&& !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj
					.getErrorsForRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		if (this.medicalVerificationTableObj != null
				&& !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj
					.getErrorsforRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		/*
		 * String strMsg = validateProcedureAndDiagnosisName(); if(null !=
		 * strMsg && !("").equalsIgnoreCase(strMsg)) { eMsg += strMsg; hasError
		 * = true; }
		 */

		if (this.diagnosisDetailsTableObj != null) {
			boolean isValid = this.diagnosisDetailsTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.diagnosisDetailsTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.procedurdTableObj != null) {
			boolean isValid = true;			
				isValid = this.procedurdTableObj.isValidPA();			
			if (!isValid) {
				hasError = true;
				List<String> errors = this.procedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.newProcedurdTableObj != null) {
			boolean isValid = this.newProcedurdTableObj.isValid();
			boolean isValidProcedure = this.newProcedurdTableObj
					.isValidProcedure();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}

			if (!isValidProcedure) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj
						.getProcedureErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.specialityTableObj != null) {
			boolean isValid = this.specialityTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.specialityTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (this.bean.getNewIntimationDTO().getHospitalDto() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto()
						.getHospitalType() != null
				&& !this.bean.getNewIntimationDTO().getHospitalDto()
						.getHospitalType().getId()
						.equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)) {
			if (this.hospitalDetailsObj != null) {
				boolean isValid = this.hospitalDetailsObj.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.hospitalDetailsObj.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
				}
			}
		}

		if (this.otherClaimDetailsObj != null) {
			boolean isValid = this.otherClaimDetailsObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.otherClaimDetailsObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			} else {
				this.otherClaimDetailsObj.setTableValues();
			}
		}

		if (null != paBenefitsTable) {
			boolean isValid = this.paBenefitsTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.paBenefitsTable.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (null != paAddOnCoversTable) {
			boolean isValid = this.paAddOnCoversTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.paAddOnCoversTable.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		if (null != paOptionalCoversTable) {
			boolean isValid = this.paOptionalCoversTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.paOptionalCoversTable.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}

		// if(admissionDate != null && dischargeDate != null) {
		// if(dischargeDate.getValue().before(admissionDate.getValue())) {
		// hasError = true;
		// eMsg += "Discharge date should not be before Admission Date. </br>";
		// }
		// }

		if (admissionDate != null && dischargeDate != null
				&& admissionDate.getValue() != null
				&& dischargeDate.getValue() != null) {
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(
					admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			// if(daysBetweenDate >= 0 &&
			// SHAUtils.getIntegerFromString(noOfDaysTxt.getValue()) !=
			// daysBetweenDate.intValue()) {
			if (daysBetweenDate >= 0
					&& SHAUtils.getDoubleFromStringWithComma(noOfDaysTxt
							.getValue()) > daysBetweenDate.doubleValue()) {
				hasError = true;
				eMsg += "No of days should be DOD-DOA+1 </br>";
			}
		}

		if (this.procedurdTableObj != null
				&& !this.procedurdTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj
					.getValues();
			List<ProcedureDTO> procedureList = this.procedurdTableObj
					.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				SelectValue consider = new SelectValue();
				consider.setId(1l);
				consider.setValue("yes");
				procedureDTO.setConsiderForPayment(consider);
			}
			Boolean isError = false;
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				for (ProcedureDTO procedureDTO : procedureList) {
					if (procedureDTO.getSublimitName() != null
							&& diagnosisDetailsTableDTO.getSublimitName() != null
							&& procedureDTO
									.getSublimitName()
									.getLimitId()
									.equals(diagnosisDetailsTableDTO
											.getSublimitName().getLimitId())) {
						if ((null != diagnosisDetailsTableDTO
								.getConsiderForPayment() && diagnosisDetailsTableDTO
								.getConsiderForPayment().getId()
								.equals(ReferenceTable.COMMONMASTER_YES))
								&& (null != procedureDTO
										.getConsiderForPayment() && procedureDTO
										.getConsiderForPayment()
										.getId()
										.equals(ReferenceTable.COMMONMASTER_YES))) {
							hasError = true;
							isError = true;
							eMsg += "Same Sublimit is Selected for both Diagnosis and Procedure. </br> Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .  </br>";
							break;
						}
					}
				}
				if (isError) {
					break;
				}
			}
		}

		Boolean diagnosisIsConsiderForPayment = false;
		Boolean procedureIsConsiderForPayment = false;
		if (this.diagnosisDetailsTableObj != null
				&& !this.diagnosisDetailsTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj
					.getValues();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				/*
				 * Since consider for payment is not applicable for pa, by
				 * default this is set to false , to skip the validation.
				 */
				diagnosisIsConsiderForPayment = false;/*
													 * if((null !=
													 * diagnosisDetailsTableDTO
													 * .getConsiderForPayment()
													 * &&
													 * diagnosisDetailsTableDTO
													 * .getConsiderForPayment
													 * ().getId
													 * ().equals(ReferenceTable
													 * .COMMONMASTER_YES))) {
													 * diagnosisIsConsiderForPayment
													 * =false; break; } else {
													 * diagnosisIsConsiderForPayment
													 * = true; }
													 */
			}
		}
		if (this.procedurdTableObj != null
				&& !this.procedurdTableObj.getValues().isEmpty()) {
			List<ProcedureDTO> procedureList = this.procedurdTableObj
					.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {

				/*
				 * Since consider for payment is not applicable for pa, by
				 * default this is set to false , to skip the validation.
				 */
				procedureIsConsiderForPayment = false;

				/*
				 * if(null != procedureDTO.getConsiderForPayment() &&
				 * procedureDTO
				 * .getConsiderForPayment().getId().equals(ReferenceTable
				 * .COMMONMASTER_YES)) { procedureIsConsiderForPayment = false;
				 * break; } else { procedureIsConsiderForPayment = true; }
				 */}
		} else {
			procedureIsConsiderForPayment = true;
		}

		if (diagnosisIsConsiderForPayment && procedureIsConsiderForPayment) {
			// hasError = true;
			this.bean.setIsConsiderForPaymentNo(true);
			alertMessageForConsiderForPayment();
		} else {
			this.bean.setIsConsiderForPaymentNo(false);
		}
		if (!this.claimRequestButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.claimRequestButtonObj.getErrors();
			for (String error : errors) {
				eMsg += error;
			}
		}

		/*if (bean.getStatusKey() != null && bean.getStatusKey().equals(
				ReferenceTable.UPLOAD_INVESIGATION_COMPLETED) && bean.getIsInvestigation()
				&& (investigationReviewRemarks.getValue() == null
				|| investigatorName.getValue() == null || (investigationReportReviewedChk
						.getValue() == null || !investigationReportReviewedChk
						.getValue()))) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
		}*/

		/*if (bean.getIsInvestigation()
				&& (investigationReviewRemarks.getValue() == null
						|| investigatorName.getValue() == null || (investigationReportReviewedChk
						.getValue() == null || !investigationReportReviewedChk
						.getValue()))) {
			hasError = true;
			eMsg += "Please Enter Investigation Remarks and Name";
		}*/
		
		if(null == dischargeDate.getValue() || ("").equals(dischargeDate.getValue()))
		{
			hasError = true;
			eMsg += "Please Select Date Of Discharge.<br>";
		}
		
		if(null!=dateOfDeath && (null == dateOfDeath.getValue() || dateOfDeath.getValue().equals("")) && optAccidentDeathFlag.getValue().equals(false))
		{				
				hasError = true;
				eMsg += "Please select Date of Death</br>";
			
		}

		
		Boolean accDeathOptValue = optAccidentDeathFlag.getValue() != null ? (boolean)optAccidentDeathFlag.getValue() : true;

		if(accDeathOptValue != null 
				&& !accDeathOptValue
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null		
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

			if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() && !isNomineeDeceased()){
				List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
			
				if(tableList != null && !tableList.isEmpty()){
					bean.getNewIntimationDTO().setNomineeList(tableList);
					StringBuffer nomineeNames = new StringBuffer("");
					int selectCnt = 0;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}
					}
					bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
						bean.getNewIntimationDTO().setNomineeAddr(null);
					}
					else{
						bean.getNewIntimationDTO().setNomineeName(null);
						
						eMsg += "Please Select Nominee<br>";
						hasError = true;						
					}							
				}
			}
			else{
				bean.getNewIntimationDTO().setNomineeList(null);
				bean.getNewIntimationDTO().setNomineeName(null);
								
				if(this.legalHeirDetails.isValid()) {
					
					//added for support fix IMSSUPPOR-31323
					List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
					legalHeirList.addAll(this.legalHeirDetails.getValues());
					if(legalHeirList != null && !legalHeirList.isEmpty()) {
						
						List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
						
						for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
							legalHeirList.add(legalHeirDTO);
						}
						
						bean.setLegalHeirDTOList(legalHeirList);
					}
				}	
				else{
					bean.setLegalHeirDTOList(null);
					hasError = true;
					eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)";
				}
			}					
		}
		
		if(!this.bean.getIsScheduleClicked()){
			hasError = true;
			eMsg += "Please Verify Policy Schedule Button.</br>";
		}
		if(null != this.admissionDate && null == this.admissionDate.getValue())
		{
			hasError = true;
			eMsg += "Please Select Date of Admission. </br>";
		}
		
		if(bean != null && bean.getPreauthMedicalDecisionDetails() != null && bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() != null && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty()){
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getReviewRemarkskey() == null
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					hasError = true;
					eMsg += "Please Select Investigator/ RVO grade.</br>";
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() == null
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					eMsg += "Please Select RVO findings- Claim decision.</br>";
					hasError = true;
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && assignedInvestigatiorDetails.getRvoFindingsKey() != null 
						&& ( assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_APPROVED_KEY) || 
								assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_NOT_ACCEPTED_CLAIM_REJECTED_KEY))
								&& assignedInvestigatiorDetails.getRvoReasonKey() == null
								&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					eMsg += "Please Select Reasons for not Accepting RVO Findings.</br>";
					hasError = true;
					break;
				}
			}
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
				if (assignedInvestigatiorDetails != null && (assignedInvestigatiorDetails.getReportReviewed() == null 
						|| (assignedInvestigatiorDetails.getReportReviewed() != null && assignedInvestigatiorDetails.getReportReviewed().equalsIgnoreCase(SHAConstants.N_FLAG)))
						&& bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().get(0).getKey().equals(assignedInvestigatiorDetails.getKey())) {
					hasError = true;
					eMsg += "Please Select Investigation Remarks checkbox.</br>";
					break;
				}
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
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				if (!bean.getIsComparisonDone()
						&& (bean.getHospitalizaionFlag()
								|| bean.getPartialHospitalizaionFlag() || bean
									.getIsHospitalizationRepeat())) {
					fireViewEvent(
							PAClaimRequestDataExtractionPagePresenter.COMPARE_WITH_PREVIOUS_ROD,
							bean);
				} else {
					bean.setIsComparisonDone(true);
				}
				if (this.bean.getDeletedDiagnosis().isEmpty()) {
					this.bean
							.setDeletedDiagnosis(this.diagnosisDetailsTableObj.deletedDTO);
				} else {
					List<DiagnosisDetailsTableDTO> deletedDTO = this.diagnosisDetailsTableObj.deletedDTO;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDTO) {
						if (!this.bean.getDeletedDiagnosis().contains(
								diagnosisDetailsTableDTO)) {
							this.bean.getDeletedDiagnosis().add(
									diagnosisDetailsTableDTO);
						}

					}
				}

				/*
				 * this.bean.getPreauthMedicalDecisionDetails().
				 * setMedicalVerificationTableDTO
				 * (this.medicalVerificationTableObj.getValues());
				 * this.bean.getPreauthMedicalDecisionDetails
				 * ().setTreatmentVerificationDTO
				 * (this.treatmentVerificationTableObj.getValues());
				 */
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy()
						.getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate,
						admissionDate.getValue());
				MastersValue policyType = bean.getNewIntimationDTO()
						.getPolicy().getPolicyType();

//				if ((diffDays != 0 && diffDays > 90)
//						|| (policyType != null && policyType.getKey().equals(
//								ReferenceTable.RENEWAL_POLICY))
//						|| !bean.getAdmissionDatePopup()) {
				if(policyType != null && !policyType.getKey().equals(ReferenceTable.FRESH_POLICY)){
					this.bean.setAlertMessageOpened(true);
				}
				if (this.bean.getPreauthDataExtractionDetails()
						.getNatureOfTreatment() != null
						&& this.bean.getPreauthDataExtractionDetails()
								.getNatureOfTreatment().getValue().toString()
								.toLowerCase().contains("non")) {
					// this.bean.setIsDishonoured(true);
				}

				if (SHAUtils.getDialysisDiagnosisDTO(bean
						.getPreauthDataExtractionDetails()
						.getDiagnosisTableList()) != null
						|| SHAUtils.getDialysisProcedureDTO(bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
				}

				if (null != dischargeDate)
					this.bean.getPreauthDataExtractionDetails()
							.setDischargeDate(dischargeDate.getValue());

				List<UploadDocumentDTO> uploadDocsList = this.bean
						.getUploadDocumentDTO();
				if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
						if (null != admissionDate)
							uploadDocumentDTO.setDateOfAdmission(SHAUtils
									.formatDate(admissionDate.getValue()));
						if (null != dischargeDate)
							uploadDocumentDTO.setDateOfDischarge(SHAUtils
									.formatDate(dischargeDate.getValue()));
						uploadDocumentDTO.setIntimationNo(this.bean
								.getNewIntimationDTO().getIntimationId());
						uploadDocumentDTO.setInsuredPatientName(this.bean
								.getNewIntimationDTO().getInsuredPatientName());
					}

					this.bean.setUploadDocumentDTO(uploadDocsList);
				}
				
			}

			catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}

	/*
	 * private String validateProcedureAndDiagnosisName() { String eMsg = "";
	 * StringBuffer strBuf = new StringBuffer(); List<DiagnosisDetailsTableDTO>
	 * diagList =
	 * this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
	 * List<ProcedureDTO> procList =
	 * this.bean.getPreauthMedicalProcessingDetails
	 * ().getProcedureExclusionCheckTableList(); if((null != diagList &&
	 * !diagList.isEmpty()) && (null != procList && !procList.isEmpty())) {
	 * for(DiagnosisDetailsTableDTO diagObj : diagList) { String strDiagName =
	 * diagObj.getDiagnosis();
	 * 
	 * for(ProcedureDTO procObj : procList) { String strProcName =
	 * procObj.getProcedureNameValue(); if(strDiagName.equals(strProcName)) {
	 * strBuf.append("Diagnosis and Procedure are same. Diagnosis"+" "+
	 * strDiagName).append("\n"); } } } }
	 * 
	 * if(null != strBuf && strBuf.length() > 0) { eMsg = strBuf.toString(); }
	 * 
	 * return eMsg; }
	 */

	public Boolean alertMessage() {

		Label successLabel = new Label(
				"<b style = 'color: red;'> Close Proximity Claim</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				isValid = true;
				bean.setAlertMessageOpened(true);
				dialog.close();
				wizard.next();

			}
		});

		return isValid;

	}

	private PedDetailsTableDTO setPEDDetailsToDTO(
			DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		dto.setRecTypeFlag(diagnosisDetailsTableDTO.getRecTypeFlag());
		if (pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
		return dto;
	}

	public void showWarningMsg(String eMsg) {

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

	}

	@SuppressWarnings("unchecked")
	public void setTableValuesToDTO() {
		this.bean.getPreauthDataExtractionDetails().setSpecialityList(
				this.specialityTableObj != null ? this.specialityTableObj
						.getValues() : new ArrayList<SpecialityDTO>());
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(
				this.newProcedurdTableObj != null ? getProcedureVariationList(
						this.newProcedurdTableObj.getValues(), 1l)
						: new ArrayList<ProcedureDTO>());
		this.bean.getPreauthDataExtractionDetails().setProcedureList(
				this.procedurdTableObj != null ? getProcedureVariationList(
						this.procedurdTableObj.getValues(), 0l)
						: new ArrayList<ProcedureDTO>());

		List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj
				.getValues();
		List<InsuredPedDetails> tmpPEDList = (List<InsuredPedDetails>) referenceData
				.get("insuredPedList");
		String policyAgeing = (String) referenceData.get("policyAgeing");
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
			if (diagnosisDetailsTableDTO.getDiagnosisName() != null
					&& diagnosisDetailsTableDTO.getDiagnosisName().getValue() != null) {
				diagnosisDetailsTableDTO.setDiagnosis(diagnosisDetailsTableDTO
						.getDiagnosisName().getValue());
			}
			if (diagnosisDetailsTableDTO.getDiagnosisName() != null) {
				diagnosisDetailsTableDTO
						.setDiagnosisId(diagnosisDetailsTableDTO
								.getDiagnosisName().getId());
			}
			diagnosisDetailsTableDTO.setPolicyAgeing(policyAgeing);
			List<PedDetailsTableDTO> list = new ArrayList<PedDetailsTableDTO>();
			if (diagnosisDetailsTableDTO.getPedList().isEmpty()) {
				if (tmpPEDList != null && !tmpPEDList.isEmpty()) {
					for (InsuredPedDetails insuredPEDDetails : tmpPEDList) {
						PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(
								diagnosisDetailsTableDTO, insuredPEDDetails);
						list.add(setPEDDetailsToDTO);
					}

				} else {
					PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(
							diagnosisDetailsTableDTO, null);
					list.add(setPEDDetailsToDTO);
				}
				diagnosisDetailsTableDTO.setPedList(list);
			} else {
				List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO
						.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					pedDetailsTableDTO
							.setDiagnosisName(diagnosisDetailsTableDTO
									.getDiagnosis());
				}
			}
		}

		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(
				diagnosisList);

		List<ProcedureDTO> wholeProcedureList = new ArrayList<ProcedureDTO>();
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails()
				.getProcedureList());
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails()
				.getNewProcedureList());

		this.bean.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(wholeProcedureList);

		if (otherClaimTableObj != null) {
			this.bean.getPreauthDataExtractionDetails()
					.setOtherClaimDetailsList(otherClaimTableObj.getValues());
		}

		if (null != paBenefitsTable) {
			List<PABenefitsDTO> pabenefitsList = paBenefitsTable.getValues();
			this.bean.getPreauthDataExtractionDetails().setPaBenefitsList(
					pabenefitsList);
			List<PABenefitsDTO> deletedBillList = paBenefitsTable
					.getDeletedBillList();
			if (changeList != null) {
				deletedBillList.addAll(changeList);

			}
			this.bean.getPreauthDataExtractionDetails()
					.setDeletedPaBenefitsList(deletedBillList);
		}
		if (null != paAddOnCoversTable) {
			List<AddOnCoversTableDTO> addOnCoversTableList = paAddOnCoversTable
					.getValues();
			this.bean.getPreauthDataExtractionDetails()
					.setAddOnCoversTableList(addOnCoversTableList);
			this.bean.getPreauthDataExtractionDetails()
					.setAddOnCoversTableDeletedList(
							paAddOnCoversTable.getDeletedBillList());
		}
		if (null != paOptionalCoversTable) {
			List<AddOnCoversTableDTO> optionalCoversTableList = paOptionalCoversTable
					.getValues();
			this.bean.getPreauthDataExtractionDetails()
					.setOptionalCoversTableList(optionalCoversTableList);
			this.bean.getPreauthDataExtractionDetails()
					.setOptionalCoversTableDeletedList(
							paOptionalCoversTable.getDeletedBillList());
		}

	}

	private List<ProcedureDTO> getProcedureVariationList(
			List<ProcedureDTO> procedureDTOList, Long isNewProcedure) {
		if (!procedureDTOList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureDTOList) {
				if (isNewProcedure == 0) {
					procedureDTO.setProcedureNameValue(procedureDTO
							.getProcedureName() != null ? procedureDTO
							.getProcedureName().getValue() : null);
					procedureDTO.setProcedureCodeValue(procedureDTO
							.getProcedureCode() != null ? procedureDTO
							.getProcedureCode().getValue() : null);
				} else {
					SelectValue procedureName = new SelectValue();
					procedureName
							.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					SelectValue procedureCode = new SelectValue();
					procedureCode
							.setValue(procedureDTO.getProcedureCodeValue());
					// procedureDTO.setProcedureName(procedureCode);
					procedureDTO.setProcedureCode(procedureCode);
				}
				procedureDTO.setNewProcedureFlag(isNewProcedure);
				procedureDTO.setPolicyAging(referenceData
						.containsKey("policyAgeing") ? (String) referenceData
						.get("policyAgeing") : null);
			}
		}
		return procedureDTOList;
	}

	private void setTableValues() {

		if (this.specialityTableObj != null) {
			List<SpecialityDTO> specialityList = this.bean
					.getPreauthDataExtractionDetails().getSpecialityList();
			for (SpecialityDTO specialityDTO : specialityList) {
				specialityDTO.setStatusFlag(true);
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}

		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();

		List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
		if (!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				if (procedureDTO.getNewProcedureFlag() == 1) {
					newProcedureDTOList.add(procedureDTO);
				} else {
					oldProcedureDTOList.add(procedureDTO);
				}
			}
		}
		this.bean.getPreauthDataExtractionDetails().setProcedureList(
				oldProcedureDTOList);
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(
				newProcedureDTOList);
		if (this.newProcedurdTableObj != null) {
			List<ProcedureDTO> newProcedureList = this.bean
					.getPreauthDataExtractionDetails().getNewProcedureList();
			for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
				this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
			}
		}

		if (this.procedurdTableObj != null) {
			List<ProcedureDTO> procedureList = this.bean
					.getPreauthDataExtractionDetails().getProcedureList();
			for (ProcedureDTO procedureTableDTO : procedureList) {
				this.procedurdTableObj.addBeanToList(procedureTableDTO);
			}
		}

		if (this.diagnosisDetailsTableObj != null) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisList) {
				this.diagnosisDetailsTableObj.addBeanToList(diagnosisDTO);
			}
			this.diagnosisDetailsTableObj
					.init(this.bean,
							SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);

		}

		/*
		 * BeanItemContainer<SelectValue> medicalVerificationContainer =
		 * (BeanItemContainer<SelectValue>) referenceData
		 * .get("medicalVerification"); BeanItemContainer<SelectValue>
		 * treatmentVerificationContainer = (BeanItemContainer<SelectValue>)
		 * referenceData .get("treatmentQualityVerification");
		 * 
		 * List<MedicalVerificationDTO> medicalVerificationTableDTO =
		 * this.bean.getPreauthMedicalDecisionDetails
		 * ().getMedicalVerificationTableDTO();
		 * //this.medicalVerificationTableObj.setReference(referenceData);
		 * if(!medicalVerificationTableDTO.isEmpty()) { int i = 1; for
		 * (MedicalVerificationDTO medicalVerificationDTO :
		 * medicalVerificationTableDTO) { medicalVerificationDTO.setSlNo(i);
		 * this
		 * .medicalVerificationTableObj.addBeanToList(medicalVerificationDTO);
		 * i++; }
		 * 
		 * 
		 * } else { int i = 1; List<SelectValue> itemIds =
		 * medicalVerificationContainer.getItemIds(); for (SelectValue
		 * selectValue : itemIds) { MedicalVerificationDTO verificationDTO = new
		 * MedicalVerificationDTO();
		 * verificationDTO.setDescription(selectValue.getValue());
		 * verificationDTO.setDescriptionId(selectValue.getId());
		 * verificationDTO.setSlNo(i);
		 * this.medicalVerificationTableObj.addBeanToList(verificationDTO); i++;
		 * } }
		 * 
		 * List<TreatmentQualityVerificationDTO> treatmentVerificationTableDTO =
		 * this
		 * .bean.getPreauthMedicalDecisionDetails().getTreatmentVerificationDTO
		 * (); //this.treatmentVerificationTableObj.setReference(referenceData);
		 * if(!treatmentVerificationTableDTO.isEmpty()) { int i = 1; for
		 * (TreatmentQualityVerificationDTO treatmentQualityVerificationDTO :
		 * treatmentVerificationTableDTO) {
		 * treatmentQualityVerificationDTO.setSlNo(i);
		 * this.treatmentVerificationTableObj
		 * .addBeanToList(treatmentQualityVerificationDTO); i++; }
		 * 
		 * } else { int i = 1; List<SelectValue> itemIds =
		 * treatmentVerificationContainer.getItemIds(); for (SelectValue
		 * selectValue : itemIds) { TreatmentQualityVerificationDTO
		 * verificationDTO = new TreatmentQualityVerificationDTO();
		 * verificationDTO.setDescription(selectValue.getValue());
		 * verificationDTO.setDescriptionId(selectValue.getId());
		 * verificationDTO.setSlNo(i);
		 * this.treatmentVerificationTableObj.addBeanToList(verificationDTO);
		 * i++; } }
		 */

		if (null != paAddOnCoversTable) {
			List<AddOnCoversTableDTO> addOnCoversTableDTOList = this.bean
					.getPreauthDataExtractionDetails()
					.getAddOnCoversTableList();
			BeanItemContainer<SelectValue> coverList = (BeanItemContainer<SelectValue>) referenceData
					.get("addOnCovers");
			List<AddOnCoversTableDTO> addOnCoversList = (List<AddOnCoversTableDTO>) referenceData
					.get("addOnCoverProc");
			if (null != addOnCoversTableDTOList
					&& !addOnCoversTableDTOList.isEmpty()) {
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableDTOList) {

					if (null != coverList) {
						for (int i = 0; i < coverList.size(); i++) {
							if (null != addOnCoversTableDTO.getCovers()
									&& addOnCoversTableDTO
											.getCovers()
											.getId()
											.equals(coverList.getIdByIndex(i)
													.getId())) {
								/*
								 * SelectValue selectValue = new SelectValue();
								 * selectValue
								 * .setId(coverList.getIdByIndex(i).getId());
								 * selectValue
								 * .setValue(coverList.getIdByIndex(i)
								 * .getValue());
								 */
								// addOnCoversTableDTO.setCovers(coverList.getIdByIndex(i));
								addOnCoversTableDTO.setAddOnCovers(coverList
										.getIdByIndex(i));
								break;
							}
						}
						for (AddOnCoversTableDTO dto : addOnCoversList) {
							
							if (null != addOnCoversTableDTO.getAddOnCovers()
									&& addOnCoversTableDTO.getAddOnCovers()
											.getId().equals(dto.getCoverId())) {
								addOnCoversTableDTO.setEligibleForPolicy(dto
										.getEligibleForPolicy());
							}
							
							if (this.bean.getPreauthDataExtractionDetails()
									.getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
									.getReconsiderationFlag()))
							{
								addOnCoversTableDTO.setIsReconsideration(true);
							}
							
						}
						
						
						paAddOnCoversTable.addBeanToList(addOnCoversTableDTO);
					}

				}
			}
		}

		if (null != paOptionalCoversTable) {
			List<AddOnCoversTableDTO> addOnCoversTableDTOList = this.bean
					.getPreauthDataExtractionDetails()
					.getOptionalCoversTableList();
			BeanItemContainer<SelectValue> coverList = (BeanItemContainer<SelectValue>) referenceData
					.get("optionalCovers");

			List<AddOnCoversTableDTO> optionalCoversList = (List<AddOnCoversTableDTO>) referenceData
					.get("optionalCoverProc");
			if (null != addOnCoversTableDTOList
					&& !addOnCoversTableDTOList.isEmpty()) {
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableDTOList) {

					if (null != coverList) {
						for (int i = 0; i < coverList.size(); i++) {
							if (null != addOnCoversTableDTO.getOptionalCover()
									&& addOnCoversTableDTO
											.getOptionalCover()
											.getId()
											.equals(coverList.getIdByIndex(i)
													.getId())) {
								/*
								 * SelectValue selectValue = new SelectValue();
								 * selectValue
								 * .setId(coverList.getIdByIndex(i).getId());
								 * selectValue
								 * .setValue(coverList.getIdByIndex(i)
								 * .getValue());
								 */
								// addOnCoversTableDTO.setCovers(coverList.getIdByIndex(i));
								addOnCoversTableDTO.setAddOnCovers(coverList
										.getIdByIndex(i));
								break;
							}
						}
						for (AddOnCoversTableDTO dto : optionalCoversList) {
							if (null != addOnCoversTableDTO.getAddOnCovers()
									&& addOnCoversTableDTO.getAddOnCovers()
											.getId().equals(dto.getCoverId())) {
								addOnCoversTableDTO.setEligibleForPolicy(dto
										.getEligibleForPolicy());
							}							

							if (("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
									.getDocAckknowledgement().getReconsiderationRequest()))
							{
								addOnCoversTableDTO.setIsReconsideration(true);
							}
						}

						paOptionalCoversTable
								.addBeanToList(addOnCoversTableDTO);
					}

				}
			}
		}

	}

	public void editSpecifyVisible(Boolean checkValue) {/*
														 * if (checkValue) {
														 * cmbSpecifyIllness
														 * .setReadOnly(false);
														 * } else {
														 * //cmbSpecifyIllness
														 * .setValue("");
														 * cmbSpecifyIllness
														 * .setValue(null);
														 * cmbSpecifyIllness
														 * .setNullSelectionAllowed
														 * (true);
														 * cmbSpecifyIllness
														 * .setReadOnly(true); }
														 */
	}

	protected void addListener() {

		/*
		 * criticalIllnessChk .addValueChangeListener(new
		 * Property.ValueChangeListener() { private static final long
		 * serialVersionUID = 1L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) { Boolean
		 * checkValue = criticalIllnessChk.getValue(); fireViewEvent(
		 * PAClaimRequestDataExtractionPagePresenter.CHECK_CRITICAL_ILLNESS,
		 * checkValue, true); } });
		 */

		optAccidentDeathFlag.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean accidentDeath = event.getProperty().getValue() != null ? (Boolean) event.getProperty().getValue() : null;
				if(accidentDeath != null && !accidentDeath) {
					if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
							   && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
							   && ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())) {
								
						buildNomineeLayout();
					}
				}
				else{
					wholeVLayout.removeComponent(nomineeDetailsTable);
					if(chkNomineeDeceased != null){
						wholeVLayout.removeComponent(chkNomineeDeceased);
					}
					wholeVLayout.removeComponent(legalHeirLayout);
				}
				
				if (accidentDeath != null && !accidentDeath) {
					setRequiredAndValidation(dateOfDeath);
					mandatoryFields.add(dateOfDeath);
					dateOfDeath.setValue(bean.getPreauthDataExtractionDetails().getDateOfDeath());
				} else {
					dateOfDeath.setRequired(false);
					mandatoryFields.remove(dateOfDeath);
					dateOfDeath.setValue(null);
				}
				
			}
		});
		
		cmbTreatmentType.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if (value != null
						&& cmbTreatmentType.getValue().toString().toLowerCase()
								.contains("medical")) {
					if (!bean.getPreauthMedicalProcessingDetails()
							.getProcedureExclusionCheckTableList().isEmpty()) {
						alertMessageForChangeOfTreatmentType();
					} else {
						fireViewEvent(
								PAClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							PAClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
							null);
				}
			}
		});

		cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				bean.getNewIntimationDTO().setRoomCategory(value);
				
				if(value != null && value.getValue() != null){
					
					System.out.println(String.format("Room Category Value : [%s]",value.getValue()));
				}
				
				if(value != null && value.getValue() != null && value.getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")) {
					ventilatorSupportOption.setEnabled(true);
					ventilatorSupportOption.setRequired(true);
					//ventilatorSupportOption.setVisible(true);
					mandatoryFields.add(ventilatorSupportOption);
				} 
				else{
					ventilatorSupportOption.setValue(null);
					//unbindField(ventilatorSupportOption);
					ventilatorSupportOption.setEnabled(false);
					ventilatorSupportOption.setRequired(false);
					mandatoryFields.remove(ventilatorSupportOption);
				}
				

				// List<ProcedureDTO> procedureList = new
				// ArrayList<ProcedureDTO>();
				//
				// if(procedureTableObj != null){
				// List<ProcedureDTO> values = procedureTableObj.getValues();
				// procedureList.addAll(values);
				// procedureTableObj.removeRow();
				// for (ProcedureDTO procedureDTO : procedureList) {
				// // procedureDTO.setProcedureCode(null);
				// procedureTableObj.addBeanToList(procedureDTO);
				// }
				// }

			}
		});

		/*
		 * cmbNatureOfTreatment.addValueChangeListener(new ValueChangeListener()
		 * { private static final long serialVersionUID = 2697682747976915503L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) {
		 * SelectValue value = (SelectValue) event.getProperty().getValue();
		 * if(value != null && value.getValue() != null &&
		 * value.getValue().toString().toLowerCase().contains("non")) {
		 * Collection<?> itemIds =
		 * cmbTreatmentType.getContainerDataSource().getItemIds();
		 * cmbTreatmentType.setValue(itemIds.toArray()[0]);
		 * cmbTreatmentType.setEnabled(false); } else {
		 * cmbTreatmentType.setEnabled(true); } } });
		 */

		/*
		 * cmbSection.addValueChangeListener(new ValueChangeListener() {
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) {
		 * 
		 * if(cmbSection != null && cmbSection.getValue() != null){ SelectValue
		 * sectionValue = (SelectValue) cmbSection.getValue();
		 * bean.getPreauthDataExtractionDetails().setSection(sectionValue); }
		 * 
		 * fireViewEvent(PAClaimRequestDataExtractionPagePresenter.
		 * SUBLIMIT_CHANGED_BY_SECTION, bean);
		 * 
		 * 
		 * List<DiagnosisDetailsTableDTO> diagnosisList = new
		 * ArrayList<DiagnosisDetailsTableDTO>();
		 * 
		 * diagnosisList = diagnosisDetailsTableObj.getValues();
		 * 
		 * 
		 * diagnosisDetailsTableObj.init(bean,SHAConstants.
		 * MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);
		 * diagnosisDetailsTableObj.setReferenceData(referenceData);
		 * 
		 * for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO :
		 * diagnosisList) {
		 * 
		 * diagnosisDetailsTableObj.addBeanToList(diagnosisDetailsTableDTO);
		 * 
		 * }
		 * 
		 * if(procedurdTableObj != null) {
		 * 
		 * List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
		 * 
		 * procedureDTO = procedurdTableObj.getValues();
		 * 
		 * procedurdTableObj.init(bean.getHospitalCode(),
		 * SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION, bean);
		 * procedurdTableObj.setReferenceData(referenceData);
		 * 
		 * for (ProcedureDTO procedureDTO2 : procedureDTO) {
		 * procedurdTableObj.addBeanToList(procedureDTO2); } }
		 * 
		 * // List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
		 * // // if(procedureTableObj != null){ // List<ProcedureDTO> values =
		 * procedureTableObj.getValues(); // procedureList.addAll(values); //
		 * procedureTableObj.removeRow(); // for (ProcedureDTO procedureDTO :
		 * procedureList) {
		 * 
		 * //// procedureDTO.setProcedureCode(null); //
		 * procedureTableObj.addBeanToList(procedureDTO); // } // }
		 * 
		 * } });
		 */

		/*
		 * cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {
		 *//**
			 * 
			 */
		/*
		 * private static final long serialVersionUID = -8435623803385270083L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) {
		 * fireViewEvent( PAClaimRequestDataExtractionPagePresenter.
		 * MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED, null); } });
		 */

		// dischargeDate.setData(bean.getPreauthDataExtractionDetails().getAdmissionDate());

		dischargeDate
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (bean.getPreauthDataExtractionDetails()
								.getAdmissionDate() != null) {
							// Date admission = (Date) ((DateField)
							// event.getProperty()).getData();
							Date enteredDate = (Date) ((DateField) event
									.getProperty()).getValue();

							// if(enteredDate != null && admissionDate != null
							// && admissionDate.getValue() != null){
							// if(enteredDate.before(admissionDate.getValue())){
							// admissionDate.setValue(null);
							// }
							// }
							if (enteredDate != null && admissionDate != null
									&& admissionDate.getValue() != null) {
								Long daysBetweenDate = SHAUtils
										.getDaysBetweenDate(
												admissionDate.getValue(),
												enteredDate);
								if (daysBetweenDate >= 30) {
									showWarningMsg("No of days stayed is more than 30 days");
								}

								if (admissionDate != null
										&& dischargeDate != null
										&& admissionDate.getValue() != null
										&& dischargeDate.getValue() != null) {
									Long noOfDays = SHAUtils
											.getDaysBetweenDate(
													admissionDate.getValue(),
													dischargeDate.getValue());
									noOfDays++;
									noOfDaysTxt.setValue(noOfDays.toString());
								}
							}
							//
							if (enteredDate != null && admissionDate != null
									&& admissionDate.getValue() != null) {
								if (enteredDate.before(admissionDate.getValue())) {
									// event.getProperty().setValue(null);
									dischargeDate.setValue(null);
									if (null != noOfDaysTxt)
										noOfDaysTxt.setValue(null);
									showErrorMessage("Discharge date cannot be lesser than the date of admission");

								} else {
									if (dischargeDate.getValue() != null) {
										admissionDate.setData(dischargeDate
												.getValue());
									}
								}
							}
						}

					}
				});

		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				Date dischargeDateValue = dischargeDate.getValue();

				Boolean isValid = true;

				if (enteredDate != null) {

					if (dischargeDate != null
							&& dischargeDate.getValue() != null) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(
								enteredDate, dischargeDate.getValue());
						if (daysBetweenDate >= 30) {
							showWarningMsg("No of days stayed is more than 30 days");
						}

						if (admissionDate != null && dischargeDate != null
								&& admissionDate.getValue() != null
								&& dischargeDate.getValue() != null) {
							Long noOfDays = SHAUtils.getDaysBetweenDate(
									admissionDate.getValue(),
									dischargeDate.getValue());
							noOfDays++;
							noOfDaysTxt.setValue(noOfDays.toString());
						}
					}

					if (enteredDate != null && deathDate != null
							&& deathDate.getValue() != null) {
						if (enteredDate.after(deathDate.getValue())) {
							// admissionDate.setValue(null);
							isValid = false;
							showAdmissionDateError();
							if (admissionDateValue != null) {
								admissionDate.setValue(admissionDateValue);
							}
						}
					}

					if (enteredDate != null && dischargeDate != null
							&& dischargeDate.getValue() != null) {
						if (enteredDate.after(dischargeDate.getValue())) {
							isValid = false;
							showErrorMessage("Admission date should not be greater than Discharge date");
							if (admissionDateValue != null) {
								admissionDate.setValue(admissionDateValue);
							}
						}
					} else if (enteredDate != null
							&& dischargeDateValue != null) {
						if (dischargeDateValue.before(enteredDate)) {
							// event.getProperty().setValue(null);
							// dischargeDate.setValue(null);
							isValid = false;
							showErrorMessage("Discharge date cannot be lesser than the date of admission");
							if (admissionDateValue != null) {
								admissionDate.setValue(admissionDateValue);
							}

						}
					}

					Date policyFromDate = bean.getPolicyDto()
							.getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();

					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);

					if (!(enteredDate.after(policyFromDate) || enteredDate
							.compareTo(policyFromDate) == 0)
							|| !(enteredDate.before(policyToDate) || enteredDate
									.compareTo(policyToDate) == 0)) {
						isValid = false;
						Button okButton = new Button("OK");
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});
						HorizontalLayout hLayout = new HorizontalLayout(
								okButton);
						hLayout.setComponentAlignment(okButton,
								Alignment.MIDDLE_CENTER);
						hLayout.setMargin(true);
						VerticalLayout layout = new VerticalLayout(
								new Label(
										"<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>",
										ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						;
						dialog.show(getUI().getCurrent(), null, true);

						event.getProperty().setValue(null);
					}
				}

				if (isValid) {
					admissionDateValue = enteredDate;
				}

			}
		});

		/*
		 * changeInDiagnosis.addValueChangeListener(new
		 * Property.ValueChangeListener() { private static final long
		 * serialVersionUID = 1L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) { Boolean
		 * isChecked = false; if(event.getProperty() != null &&
		 * event.getProperty().getValue().toString() == "true") {
		 * diagnosisDetailsTableObj.enableOrDisableDeleteButton(true);
		 * changeInDiagSelected = true; changeIndiaganosisRows =
		 * diagnosisDetailsTableObj.getValues().size(); if(procedurdTableObj !=
		 * null) { procedurdTableObj.enableOrDisableDeleteButton(true); }
		 * 
		 * } else { diagnosisDetailsTableObj.enableOrDisableDeleteButton(false);
		 * if(procedurdTableObj != null) {
		 * procedurdTableObj.enableOrDisableDeleteButton(false); }
		 * 
		 * } } });
		 */

		/*
		 * coveredPreviousClaim.addValueChangeListener(new
		 * Property.ValueChangeListener() { private static final long
		 * serialVersionUID = 1L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) { Boolean
		 * isChecked = false; if(event.getProperty() != null &&
		 * event.getProperty().getValue().toString() == "true") { isChecked =
		 * true; }
		 * 
		 * fireViewEvent(PAClaimRequestDataExtractionPagePresenter.
		 * MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS, isChecked); } });
		 */

		/*
		 * cmbHospitalisationDueTo.addValueChangeListener(new
		 * ValueChangeListener() { private static final long serialVersionUID =
		 * -2577540521492098375L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) {
		 * SelectValue value = (SelectValue) event.getProperty().getValue(); //
		 * if(value != null) { fireViewEvent(
		 * PAClaimRequestDataExtractionPagePresenter
		 * .MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO, value); // }
		 * 
		 * } });
		 */

		/*
		 * domicillaryHospitalisation.addValueChangeListener(new
		 * Property.ValueChangeListener() { private static final long
		 * serialVersionUID = 1L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent event) { Boolean
		 * isChecked = false; if(event.getProperty() != null &&
		 * event.getProperty().getValue().toString() == "true") { isChecked =
		 * true; } fireViewEvent(PAClaimRequestDataExtractionPagePresenter.
		 * GENERATE_DOMICILLARY_FIELDS, isChecked); } });
		 */

		preExistingDisabilities
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = (Boolean) event.getProperty()
								.getValue();
						if (isChecked) {
							/*
							 * if (disabilitiesLayout != null &&
							 * disabilitiesLayout.getComponentCount() > 0) {
							 * disabilitiesLayout.removeAllComponents(); }
							 * disabilitiesLayout
							 * .addComponent(txtDisablitiesRemarks);
							 */
							txtDisablitiesRemarks.setVisible(true);
						} else {
							txtDisablitiesRemarks.setVisible(false);
							/*
							 * if (disabilitiesLayout != null &&
							 * disabilitiesLayout.getComponentCount() > 0) {
							 * disabilitiesLayout.removeAllComponents(); }
							 */
						}
					}
				});
		

		dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfAccident.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfAccident.setValue(null);
						showErrorMessage("Please Enter a valid Accident Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
					}
				}
			}
		});		
		
		dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDeath.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDeath.setValue(null);
						showErrorMessage("Please Enter a valid Death Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				/*if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
					}
				}*/
				if(null != enteredDate)
				{
					Date accidentDate = new Date();
					if(null != dateOfAccident.getValue()){
						accidentDate = dateOfAccident.getValue();
					}
					if (accidentDate != null && null != enteredDate) {
						
						Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
						
						if(null != diffDays && diffDays>365)
						{
							showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
						}
					}
				}
				
				
				
			}
		});		
		
		dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDisablement.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDisablement.setValue(null);
						showErrorMessage("Please Enter a valid Disablement Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date accidentDate = new Date();
				if(null != dateOfAccident.getValue()){
					accidentDate = dateOfAccident.getValue();
				}
				if (accidentDate != null && null != enteredDate) {
					
					Long diffMonths = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
					
					if(null != diffMonths && diffMonths>365)
					{
						showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
					}
				}
				
			}
		});				
	

	}

	public void addPABenefitsListener() {
		if (null != cmbPABenefits) {
			cmbPABenefits
					.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							SelectValue checkValue = (SelectValue) cmbPABenefits
									.getValue();
							if (null != checkValue) {
								changeList = new ArrayList<PABenefitsDTO>();
								changeList.addAll(paBenefitsTable.getValues());
								fireViewEvent(
										PAClaimRequestDataExtractionPagePresenter.PA_VALIDATE_BENEFITS,
										bean, checkValue.getId(),
										SHAConstants.PA_BENEFITS);

								/*
								 * fireViewEvent(
								 * PAClaimRequestDataExtractionPagePresenter
								 * .PA_BENEFITS_TABLE_VALUES,
								 * checkValue.getId(),
								 * bean.getNewIntimationDTO()
								 * .getInsuredPatient().getKey());
								 */
							}
						}
					});
		}
	}

	public void showAdmissionDateError() {

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);
		Button okButton = new Button("OK");
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7148801292961705660L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout(
				new Label(
						"<b style = 'color: red;'>Admission Date should not be greater than Death date</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);

	}

	protected void addAdmissionDateChangeListener() {
		admissionDate.setValue(this.bean.getPreauthDataExtractionDetails()
				.getAdmissionDate());

		admissionDate
				.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						admissionDate.setValue(admissionDate.getValue());

						reasonForChangeInDOA.setVisible(true);
						// reasonForChangeInDOA.setRequired(true);
						/**
						 * If admission Date is changed, then
						 * reasonForChangeInDOA is mandatory
						 * */
						// mandatoryFields.add(reasonForChangeInDOA);

						if (bean.getPreauthDataExtractionDetails()
								.getAdmissionDate() != null) {
							Date enterDate = admissionDate.getValue();

							if (enterDate != null
									&& enterDate.compareTo(bean
											.getPreauthDataExtractionDetails()
											.getAdmissionDate()) == 0) {
								reasonForChangeInDOA.setVisible(false);
								reasonForChangeInDOA.setRequired(false);

								mandatoryFields.remove(reasonForChangeInDOA);
							}
						}

						// TODO Auto-generated method stub

					}
				});

	}

	protected void addDiagnosisNameChangeListener() {
		this.diagnosisDetailsTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != procedurdTableObj) {
							String diagnosisValue = (String) event
									.getProperty().getValue();
							if (!procedurdTableObj.diagnosisList
									.contains(diagnosisValue)) {
								procedurdTableObj.diagnosisList
										.add(diagnosisValue);
							}
							System.out.println("---the diagnosisListValue----"
									+ procedurdTableObj.diagnosisList);
						}
					}

				});

	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	public void generatedFieldsBasedOnTreatment() {
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
				unbindField(treatmentRemarksTxt);
				treatmentRemarksTxt = (TextArea) binder
						.buildAndBind("Treatment Remarks", "treatmentRemarks",
								TextArea.class);
				treatmentRemarksTxt.setMaxLength(4000);
				treatmentFLayout.addComponent(treatmentRemarksTxt);
				// treatmentRemarksTxt.setRequired(true);
				// treatmentRemarksTxt.setValidationVisible(false);

				CSValidator validator = new CSValidator();
				// treatmentRemarksTxt.setMaxLength(100);
				// validator.extend(treatmentRemarksTxt);
				validator.setRegExp("^[a-zA-Z 0-9]*$");
				validator.setPreventInvalidTyping(true);

				Table table = new Table();
				table.setWidth("80%");
				table.addContainerProperty("S.No", String.class, null);
				table.addContainerProperty("Remarks", String.class, null);
				table.setStyleName(ValoTheme.TABLE_NO_HEADER);
				if (this.bean.getTreatmentRemarksList() != null
						&& !this.bean.getTreatmentRemarksList().isEmpty()) {
					for (int i = 0; i < this.bean.getTreatmentRemarksList()
							.size(); i++) {
						if (i != this.bean.getTreatmentRemarksList().size()) {
							table.addItem(
									new Object[] {
											"Treatment Remarks " + (i + 1),
											this.bean.getTreatmentRemarksList()
													.get(i) }, i + 1);
						}
					}
					table.setPageLength(2);
					treatmentFLayout.addComponent(table);
				}

				// mandatoryFields.add(treatmentRemarksTxt);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				this.procedurdTableObj = null;
				this.newProcedurdTableObj = null;

			} else {
				if (treatmentRemarksTxt != null) {
					unbindField(treatmentRemarksTxt);
					treatmentFLayout.removeComponent(treatmentRemarksTxt);
					// mandatoryFields.remove(treatmentRemarksTxt);

				}
				isMedicalSeclted = false;

				List<DiagnosisDetailsTableDTO> diagList = null;
				if (null != diagnosisDetailsTableObj) {
					diagList = this.bean.getPreauthDataExtractionDetails()
							.getDiagnosisTableList();
				}

				PAProcedureListenerTableForPremedical procedureTableInstance = procedureTableList
						.get();
				procedureTableInstance
						.init(bean.getHospitalCode(),
								SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION,
								bean);
				procedureTableInstance.setReferenceData(referenceData);
				this.procedurdTableObj = procedureTableInstance;

				addDiagnosisNameChangeListener();

				NewProcedureTableList newProcedureTableListInstance = newProcedureTableList
						.get();
				newProcedureTableListInstance.init("Add New Procedure", true);
				newProcedureTableListInstance.setReference(referenceData);
				this.newProcedurdTableObj = newProcedureTableListInstance;
				this.newProcedurdTableObj.setPreauthDTO(bean);

				if (tableVLayout != null
						&& tableVLayout.getComponentCount() > 0) {
					tableVLayout.removeAllComponents();
				}
				tableVLayout.addComponent(procedurdTableObj);
				tableVLayout.addComponent(newProcedurdTableObj);

				List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
						.getPreauthMedicalProcessingDetails()
						.getProcedureExclusionCheckTableList();
				List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();

				List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
				if (!procedureExclusionCheckTableList.isEmpty()) {
					for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
						if (null != procedureDTO
								&& null != procedureDTO.getEnableOrDisable()
								&& !procedureDTO.getEnableOrDisable()) {
							procedureDTO.setEnableOrDisable(false);
						} else {
							procedureDTO.setEnableOrDisable(true);
						}
						procedureDTO.setStatusFlag(true);
						if (procedureDTO.getNewProcedureFlag() == 1) {
							newProcedureDTOList.add(procedureDTO);
						} else {
							oldProcedureDTOList.add(procedureDTO);
						}
					}
				}
				this.bean.getPreauthDataExtractionDetails().setProcedureList(
						oldProcedureDTOList);
				this.bean.getPreauthDataExtractionDetails()
						.setNewProcedureList(newProcedureDTOList);
				if (this.newProcedurdTableObj != null) {
					List<ProcedureDTO> newProcedureList = this.bean
							.getPreauthDataExtractionDetails()
							.getNewProcedureList();
					for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
						newProcedureTableDTO.setEnableOrDisable(true);
						this.newProcedurdTableObj
								.addBeanToList(newProcedureTableDTO);
					}
				}

				if (this.procedurdTableObj != null) {
					List<ProcedureDTO> procedureList = this.bean
							.getPreauthDataExtractionDetails()
							.getProcedureList();
					for (ProcedureDTO procedureTableDTO : procedureList) {
						if (null != procedureTableDTO
								&& null != procedureTableDTO
										.getEnableOrDisable()
								&& !procedureTableDTO.getEnableOrDisable()) {
							procedureTableDTO.setEnableOrDisable(false);
						} else {
							procedureTableDTO.setEnableOrDisable(true);
						}
						procedureTableDTO.setEnableOrDisable(true);
						this.procedurdTableObj.addBeanToList(procedureTableDTO);
					}
				}

			}
			SpecialityTable specialityTableInstance = specialityTableList.get();
			specialityTableInstance.init("Speciality", true);
			referenceData.put("specialityType",
					referenceData.get("medicalSpeciality"));
			if (!isMedicalSeclted) {
				referenceData.put("specialityType",
						referenceData.get("surgicalSpeciality"));
			}
			specialityTableInstance.setReference(referenceData);
			this.specialityTableObj = specialityTableInstance;
			tableVLayout.addComponent(specialityTableObj);
		} else {
			if (treatmentRemarksTxt != null) {
				unbindField(treatmentRemarksTxt);
				treatmentFLayout.removeComponent(treatmentRemarksTxt);
				// mandatoryFields.remove(treatmentRemarksTxt);
			}

			if (tableVLayout != null && tableVLayout.getComponentCount() > 0) {
				tableVLayout.removeAllComponents();
			}
			this.procedurdTableObj = null;
			this.newProcedurdTableObj = null;
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	public void generateFieldsBasedOnPatientStatus() {/*
													 * if
													 * (cmbPatientStatus.getValue
													 * () != null) { if
													 * (this.cmbPatientStatus
													 * .getValue
													 * ().toString().toLowerCase
													 * () .contains("deceased"))
													 * { deathDate =
													 * (PopupDateField)
													 * binder.buildAndBind(
													 * "Date Of Death",
													 * "deathDate",
													 * PopupDateField.class);
													 * 
													 * //validation added for
													 * death date.
													 * addDeathDateValueChangeListener
													 * ();
													 * 
													 * txtReasonForDeath =
													 * (TextField)
													 * binder.buildAndBind(
													 * "Reason For Death",
													 * "reasonForDeath",
													 * TextField.class);
													 * txtReasonForDeath
													 * .setMaxLength(100); //
													 * cmbTerminateCover =
													 * (ComboBox)
													 * binder.buildAndBind( //
													 * "Terminate Cover",
													 * "terminateCover",
													 * ComboBox.class); // //
													 * @SuppressWarnings
													 * ("unchecked") //
													 * BeanItemContainer
													 * <SelectValue>
													 * terminateCover =
													 * (BeanItemContainer
													 * <SelectValue>)
													 * referenceData //
													 * .get("terminateCover");
													 * // // cmbTerminateCover.
													 * setContainerDataSource
													 * (terminateCover); //
													 * cmbTerminateCover
													 * .setItemCaptionMode
													 * (ItemCaptionMode
													 * .PROPERTY); //
													 * cmbTerminateCover
													 * .setItemCaptionPropertyId
													 * ("value"); // //
													 * if(this.bean.
													 * getPreauthDataExtractionDetails
													 * (
													 * ).getTerminateCoverFlag()
													 * != null && this.bean.
													 * getPreauthDataExtractionDetails
													 * (
													 * ).getTerminateCoverFlag()
													 * .toLowerCase().
													 * equalsIgnoreCase("y")) {
													 * //
													 * cmbTerminateCover.setValue
													 * (this.bean.
													 * getPreauthDataExtractionDetails
													 * ().getTerminateCover());
													 * // }
													 * 
													 * setRequiredAndValidation(
													 * deathDate);
													 * setRequiredAndValidation
													 * (txtReasonForDeath); //
													 * setRequiredAndValidation
													 * (cmbTerminateCover);
													 * 
													 * mandatoryFields.add(deathDate
													 * ); mandatoryFields.add(
													 * txtReasonForDeath); //
													 * mandatoryFields
													 * .add(cmbTerminateCover);
													 * 
													 * patientStatusFLayout.
													 * addComponent(deathDate);
													 * patientStatusFLayout
													 * .addComponent
													 * (txtReasonForDeath); //
													 * patientStatusFLayout
													 * .addComponent
													 * (cmbTerminateCover); }
													 * else {
													 * removePatientStatusGeneratedFields
													 * (); }
													 * 
													 * } else {
													 * removePatientStatusGeneratedFields
													 * (); }
													 */
	}

	/**
	 * Method to validate death date.
	 * 
	 * */
	private void addDeathDateValueChangeListener() {
		deathDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				Date currentSystemDate = new Date();
				if (null != enteredDate && null != admissionDate
						&& null != admissionDate.getValue())
					if (enteredDate.before(admissionDate.getValue())) {
						showError();
						event.getProperty().setValue(null);
					} else if (enteredDate != null && dischargeDate != null
							&& dischargeDate.getValue() != null) {
						if (enteredDate.after(dischargeDate.getValue())) {
							showError();
							event.getProperty().setValue(null);
						} else if (enteredDate.after(currentSystemDate)) {
							getErrorMessage("Date of Death should not be greater than current date");
							event.getProperty().setValue(null);
						}
					} else if (enteredDate.after(currentSystemDate)) {
						getErrorMessage("Date of Death should not be greater than current date");
						event.getProperty().setValue(null);
					}
			}
		});
	}

	public void getErrorMessage(String eMsg) {

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

	private void showError() {

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);
		Button okButton = new Button("OK");
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7148801292961705660L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout(
				new Label(
						"<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and Discharge date.</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);

	}

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			// unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			// mandatoryFields.remove(cmbTerminateCover);
			// patientStatusFLayout.removeComponent(deathDate);
			// patientStatusFLayout.removeComponent(txtReasonForDeath);
			// patientStatusFLayout.removeComponent(cmbTerminateCover);
		}
	}

	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		diagnosisDetailsTableObj.setIcdBlock(icdBlockContainer);

	}

	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		diagnosisDetailsTableObj.setIcdCode(icdCodeContainer);
	}

	public void setPackageRateForProcedure(Map<String, String> mappedValues) {
		this.procedurdTableObj.setPackageRate(mappedValues);

	}

	/**
	 * If the policy type is "Group Policy" , then the corp buffer check box
	 * will be enabled. Else it will be disabled. The below method does the
	 * validation for the same.
	 * */

	/*
	 * public void editCorpBufferChk() {
	 *//**
	 * The string "Group Policy" needs to be replaced, after checkwith
	 * saravana/Sathish the valid values for Group Policy.For time being the
	 * below string is used.
	 */
	/*
	 * 
	 * if (null != this.bean.getNewIntimationDTO() && null !=
	 * this.bean.getNewIntimationDTO().getPolicy() && null !=
	 * this.bean.getNewIntimationDTO().getPolicy().getProductType() &&
	 * ("Group").
	 * equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType
	 * ().getValue())) //if
	 * (("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO
	 * ().getPolicy().getProductType().getValue())) {
	 * corpBufferChk.setReadOnly(false); } else {
	 * corpBufferChk.setReadOnly(true); } }
	 */

	public void generatedFieldsBasedOnHospitalisationDueTo(SelectValue value) {
		if (null != value) {
			if (value.getId() != null
					&& ReferenceTable.INJURY_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				injuryDate = (DateField) binder.buildAndBind("Date of Injury",
						"injuryDate", DateField.class);
				cmbCauseOfInjury = (ComboBox) binder.buildAndBind(
						"Cause of Injury", "causeOfInjury", ComboBox.class);

				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("causeOfInjury");

				cmbCauseOfInjury.setContainerDataSource(terminateCover);
				cmbCauseOfInjury.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbCauseOfInjury.setItemCaptionPropertyId("value");

				if (this.bean.getPreauthDataExtractionDetails()
						.getCauseOfInjury() != null) {
					cmbCauseOfInjury.setValue(this.bean
							.getPreauthDataExtractionDetails()
							.getCauseOfInjury());
				}

				medicalLegalCase = (OptionGroup) binder.buildAndBind(
						"Medical Legal Case", "medicalLegalCase",
						OptionGroup.class);
				medicalLegalCase.addItems(getReadioButtonOptions());
				medicalLegalCase.setItemCaption(true, "Yes");
				medicalLegalCase.setItemCaption(false, "No");
				medicalLegalCase.setStyleName("horizontal");

				reportedToPolice = (OptionGroup) binder.buildAndBind(
						"Reported to Police", "reportedToPolice",
						OptionGroup.class);
				reportedToPolice.addItems(getReadioButtonOptions());
				reportedToPolice.setItemCaption(true, "Yes");
				reportedToPolice.setItemCaption(false, "No");
				reportedToPolice.setStyleName("horizontal");
				reportedToPolice
						.addValueChangeListener(new Property.ValueChangeListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(ValueChangeEvent event) {
								Boolean isChecked = false;
								if (event.getProperty() != null
										&& event.getProperty().getValue() != null
										&& event.getProperty().getValue()
												.toString() == "true") {
									isChecked = true;
								}
								if (event.getProperty() != null
										&& event.getProperty().getValue() != null) {
									fireViewEvent(
											PAClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_REPORTED_TO_POLICE,
											isChecked);
								}
							}
						});

				firstFLayout.addComponent(injuryDate);
				firstFLayout.addComponent(medicalLegalCase);
				firstFLayout.addComponent(reportedToPolice);

				secondFLayout.addComponent(cmbCauseOfInjury);
				if (this.bean.getPreauthDataExtractionDetails()
						.getReportedToPolice() != null
						&& this.bean.getPreauthDataExtractionDetails()
								.getReportedToPolice()) {
					generatedFieldsBasedOnReportedToPolice(true);
				}

			} else if (value.getId() != null
					&& ReferenceTable.ILLNESS_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				diseaseDetectedDate = (DateField) binder.buildAndBind(
						"Date Disease First Detected",
						"diseaseFirstDetectedDate", DateField.class);
				firstFLayout.addComponent(diseaseDetectedDate);
			} else if (value.getId() != null
					&& ReferenceTable.MATERNITY_MASTER_ID.equals(value.getId())) {
				if (firNumberTxt != null) {
					firstFLayout.removeComponent(firNumberTxt);
				}
				if (policeReportedAttached != null) {
					secondFLayout.removeComponent(policeReportedAttached);
				}
				unbindAndRemoveFromLayout();
				if (bean.getNewIntimationDTO().getInsuredPatient() != null
						&& bean.getNewIntimationDTO().getInsuredPatient()
								.getInsuredGender() != null
						&& bean.getNewIntimationDTO().getInsuredPatient()
								.getInsuredGender().getKey()
								.equals(ReferenceTable.FEMALE_GENDER)) {
					deliveryDate = (DateField) binder
							.buildAndBind("Date of Delivery", "deliveryDate",
									DateField.class);
					mandatoryFields.add(deliveryDate);
					showOrHideValidation(false);
					cmbTypeOfDelivery = (ComboBox) binder.buildAndBind(
							"Type of Delivery", "typeOfDelivery",
							ComboBox.class);

					@SuppressWarnings("unchecked")
					BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
							.get("typeOfDelivery");

					cmbTypeOfDelivery.setContainerDataSource(terminateCover);
					cmbTypeOfDelivery
							.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbTypeOfDelivery.setItemCaptionPropertyId("value");

					if (this.bean.getPreauthDataExtractionDetails()
							.getTypeOfDelivery() != null) {
						cmbTypeOfDelivery.setValue(this.bean
								.getPreauthDataExtractionDetails()
								.getTypeOfDelivery());
					}

					firstFLayout.addComponent(deliveryDate);
					firstFLayout.addComponent(cmbTypeOfDelivery);
				} else {
					getErrorMessage("Selected insured is Male.Hence Maternity is not applicable for the selected insured");
					// cmbHospitalisationDueTo.setValue(null);
					// //Vaadin8-setImmediate() cmbHospitalisationDueTo.setImmediate(true);
				}
			}

		} else {
			unbindAndRemoveFromLayout();
		}
	}

	private void alertMessageForClaimCount(Long claimCount) {

		//String msg = SHAConstants.CLAIM_COUNT_MESSAGE + claimCount;
		
		String msg ="";
		DBCalculationService dbService = new DBCalculationService();
		int gpaClaimCount = dbService.getGPAClaimCount(bean.getPolicyKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		
		if (!ReferenceTable.getGPAProducts().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			msg = msg + SHAConstants.CLAIM_COUNT_MESSAGE + claimCount;
		} else {
			msg = msg + SHAConstants.GPA_CLAIM_COUNT_MESSAGE + gpaClaimCount;
		}

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;

		Label successLabel = new Label("<b style = 'color: black;'>" + msg
				+ "</b>", ContentMode.HTML);

		if (this.bean.getClaimCount() > 2) {
			successLabel = new Label("<b style = 'color: black;'>" + msg
					+ "<br>" + additionalMessage + "</b>", ContentMode.HTML);
		}
		// successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		// successLabel.addStyleName(ValoTheme.LABEL_BOLD);
		successLabel.addStyleName(ValoTheme.LABEL_H3);
		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout firstForm = new VerticalLayout(successLabel, homeButton);
		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		// firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);

		if (this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <= 2) {
			panel.addStyleName("girdBorder1");
		} else if (this.bean.getClaimCount() > 2) {
			panel.addStyleName("girdBorder2");
		}

		panel.setHeight("150px");
		// panel.setSizeFull();

		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
		// popup.setCaption("Alert");
		// popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();
				if (!bean.getSuspiciousPopupMap().isEmpty()
						&& !bean.getIsPopupMessageOpened()) {
					suspiousPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if (bean.getIsPedWatchList()) {
					alertMessageForPEDWatchList();
				} else if ((bean.getHospitalizaionFlag() || bean
						.getPartialHospitalizaionFlag())
						&& bean.getPostHospitalizaionFlag()
						&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
								.getPostHospClaimedAmount(bean) > 5000d)) {
					if (!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void generatedFieldsBasedOnReportedToPolice(Boolean isChecked) {
		if (isChecked) {
			unbindAndRemovePoliceReportFromLayout();
			firNumberTxt = (TextField) binder.buildAndBind("FIR NO",
					"firNumber", TextField.class);

			policeReportedAttached = (OptionGroup) binder.buildAndBind(
					"MLC Report & Police Report Attached",
					"policeReportAttached", OptionGroup.class);
			policeReportedAttached.addItems(getReadioButtonOptions());
			policeReportedAttached.setItemCaption(true, "Yes");
			policeReportedAttached.setItemCaption(false, "No");
			policeReportedAttached.setStyleName("horizontal");

			firstFLayout.addComponent(firNumberTxt);

			secondFLayout.addComponent(policeReportedAttached);
		} else {
			unbindAndRemovePoliceReportFromLayout();
		}
	}

	private void unbindAndRemovePoliceReportFromLayout() {
		unbindField(firNumberTxt);
		unbindField(policeReportedAttached);

		if (firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}

	}

	private void unbindAndRemoveFromLayoutForDomicillary() {
		unbindField(treatmentStartDate);
		unbindField(treatmentEndDate);
		unbindField(hospitalizationNoOfDays);
		if (treatmentStartDate != null && treatmentEndDate != null
				&& hospitalizationNoOfDays != null) {
			firstFLayout.removeComponent(treatmentStartDate);
			firstFLayout.removeComponent(treatmentEndDate);
			secondFLayout.removeComponent(hospitalizationNoOfDays);
		}

	}

	public void generatedFieldsBasedOnOtherClaims(Boolean value) {
		if (otherClaimsFLayout.getComponentCount() > 0) {
			otherClaimDetailsObj = null;
			otherClaimsFLayout.removeAllComponents();
		}
		if (value) {
			otherClaimDetailsObj = otherClaimDetailsInstance.get();
			otherClaimDetailsObj.initView(bean, referenceData);

			otherClaimsFLayout.addComponent(otherClaimDetailsObj);
		}
	}

	private void unbindAndRemoveFromLayout() {
		unbindField(injuryDate);
		unbindField(cmbCauseOfInjury);
		unbindField(medicalLegalCase);
		unbindField(reportedToPolice);
		unbindField(deliveryDate);
		unbindField(diseaseDetectedDate);
		unbindField(firNumberTxt);
		unbindField(policeReportedAttached);
		unbindField(cmbTypeOfDelivery);

		if (injuryDate != null && cmbCauseOfInjury != null
				&& medicalLegalCase != null && reportedToPolice != null) {
			firstFLayout.removeComponent(medicalLegalCase);
			firstFLayout.removeComponent(reportedToPolice);
			firstFLayout.removeComponent(injuryDate);
			secondFLayout.removeComponent(cmbCauseOfInjury);

		}

		if (firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}

		if (deliveryDate != null) {
			firstFLayout.removeComponent(deliveryDate);
			mandatoryFields.remove(deliveryDate);
		}

		if (cmbTypeOfDelivery != null) {
			firstFLayout.removeComponent(cmbTypeOfDelivery);
		}

		if (diseaseDetectedDate != null) {
			firstFLayout.removeComponent(diseaseDetectedDate);
		}
	}

	private void showErrorMessage(String eMsg) {
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
		cmbPABenefits.setValue(null);
	}
	
	public void popupMessageFor30DaysWaitingPeriod() {

		Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
	    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
	    if((diffDays != null && diffDays < 30)){
	    	
	    	final MessageBox showAlert = showAlertMessageBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT);
 	    	Button homeButton = showAlert.getButton(ButtonType.OK);

	    		homeButton.addClickListener(new ClickListener() {
	    			private static final long serialVersionUID = 7396240433865727954L;

	    			@Override
	    			public void buttonClick(ClickEvent event) {
	    				 
	   					bean.setIsPopupMessageOpened(true);
	   					showAlert.close();
	    					 if(bean.getIsSuspicious()!=null){
	    						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
	    					}else if(bean.getNewIntimationDTO().getHospitalDto().getReInstatedBy().equalsIgnoreCase(SHAConstants.HOSPITAL_SUSPENDED_BY_RAW)) {
	    						getHospitalReinstatedAlert();
	    					}
	    	}
		});
	    }else{
	    	if(bean.getIsSuspicious()!=null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}else if(bean.getNewIntimationDTO().getHospitalDto().getReInstatedBy().equalsIgnoreCase(SHAConstants.HOSPITAL_SUSPENDED_BY_RAW)) {
				getHospitalReinstatedAlert();
			}
	    }
		
	}

	public void alertNoOfSittings() {
		DiagnosisDetailsTableDTO dialysisDiagnosisDTO = SHAUtils
				.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails()
						.getDiagnosisTableList());
		ProcedureDTO dialysisProcedureDTO = SHAUtils
				.getDialysisProcedureDTO(bean
						.getPreauthMedicalProcessingDetails()
						.getProcedureExclusionCheckTableList());
		if (dialysisDiagnosisDTO != null || dialysisProcedureDTO != null) {
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);

			final TextField sittingsField = new TextField("No. Of sittings");

			CSValidator validator = new CSValidator();
			validator.extend(sittingsField);
			validator.setRegExp("^[0-9]*$");
			validator.setPreventInvalidTyping(true);

			sittingsField
					.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO
							: (dialysisProcedureDTO != null ? dialysisProcedureDTO
									: null));
			if (dialysisDiagnosisDTO != null) {
				if (dialysisDiagnosisDTO.getSittingsInput() != null) {
					sittingsField.setValue(dialysisDiagnosisDTO
							.getSittingsInput());
				}
			} else if (dialysisProcedureDTO != null) {
				if (dialysisProcedureDTO.getSittingsInput() != null) {
					sittingsField.setValue(dialysisProcedureDTO
							.getSittingsInput());
				}
			}

			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

			okButton.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO
					: (dialysisProcedureDTO != null ? dialysisProcedureDTO
							: null));
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getData() != null) {
						if ((event.getButton().getData()) instanceof DiagnosisDetailsTableDTO) {
							DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) event
									.getButton().getData();
							dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField
									.getValue() : "0");
						} else if ((event.getButton().getData()) instanceof ProcedureDTO) {
							ProcedureDTO dto = (ProcedureDTO) event.getButton()
									.getData();
							dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField
									.getValue() : "0");
						}
						bean.setDialysisOpened(true);
					}
					dialog.close();
					wizard.next();

				}
			});
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(sittingsField, hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
		} else {
			bean.setIsDialysis(false);
			bean.setDialysisOpened(false);
		}
	}

	public void showPopupForComparison(String comparisonString) {
		if (comparisonString.isEmpty()) {
			bean.setIsComparisonDone(true);
		} else {
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
					bean.setIsComparisonDone(true);
					wizard.next();
					dialog.close();
				}
			});

			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(
					new Label(
							"<b style = 'color:red'>The following Attibutes has been changed from Previous ROD : </b>",
							ContentMode.HTML), new Label(comparisonString,
							ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
		}
	}

	public void alertMessageForChangeOfTreatmentType() {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("Yes");
		Button cancelButton = new Button("No");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				List<ProcedureDTO> procedureExclusionCheckTableList = bean
						.getPreauthMedicalProcessingDetails()
						.getProcedureExclusionCheckTableList();
				for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
					if (!bean.getDeletedProcedure().contains(procedureDTO)
							&& procedureDTO.getKey() != null) {
						bean.getDeletedProcedure().add(procedureDTO);
					}
				}

				bean.getPreauthMedicalProcessingDetails()
						.setProcedureExclusionCheckTableList(
								new ArrayList<ProcedureDTO>());
				fireViewEvent(
						PAClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
						null);
				dialog.close();
			}
		});

		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				cmbTreatmentType.setValue(cmbTreatmentType.getItemIds()
						.toArray()[1]);
				dialog.close();
			}
		});
		HorizontalLayout hLayout = new HorizontalLayout(okButton, cancelButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout(
				new Label(
						"Entered Procedure Will be marked as deleted one. Do you want to proceed further ?"),
				hLayout);
		layout.setMargin(true);
		dialog.setContent(layout);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void poupMessageForProduct() {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if (bean.getClaimCount() > 1) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()
						&& !bean.getIsPopupMessageOpened()) {
					suspiousPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if (bean.getIsPedWatchList()) {
					alertMessageForPEDWatchList();
				} else if ((bean.getHospitalizaionFlag() || bean
						.getPartialHospitalizaionFlag())
						&& bean.getPostHospitalizaionFlag()
						&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
								.getPostHospClaimedAmount(bean) > 5000d)) {
					if (!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}

				} else if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}

			}
		});

		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setMargin(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void generatedFieldsBasedOnDomicillaryHosp(Boolean isChecked) {
		unbindAndRemoveFromLayoutForDomicillary();
		if (isChecked) {
			unbindAndRemoveFromLayoutForDomicillary();
			treatmentStartDate = (DateField) binder.buildAndBind(
					"Treatment Start Date", "treatmentStartDate",
					DateField.class);

			treatmentEndDate = (DateField) binder.buildAndBind(
					"Treatment End Date", "treatmentEndDate", DateField.class);

			hospitalizationNoOfDays = (TextField) binder.buildAndBind(
					"No of Days", "hospitalizationNoOfDays", TextField.class);

			treatmentStartDate
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {

							if (treatmentEndDate != null
									&& treatmentEndDate.getValue() != null
									&& treatmentStartDate != null
									&& treatmentStartDate.getValue() != null) {
								if (treatmentStartDate.getValue().after(
										treatmentEndDate.getValue())) {
									treatmentStartDate.setValue(null);
									showErrorMessage("Treatment Start date cannot be greater than the Treatment End Date");
								}
							}

							if (treatmentStartDate != null
									&& treatmentEndDate != null
									&& treatmentStartDate.getValue() != null
									&& treatmentEndDate.getValue() != null) {
								Long noOfDays = SHAUtils.getDaysBetweenDate(
										treatmentStartDate.getValue(),
										treatmentEndDate.getValue());
								if (noOfDays < 0l) {
									noOfDays = 0l;
								}
								hospitalizationNoOfDays.setValue(noOfDays
										.toString());
							}

						}
					});

			treatmentEndDate
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {

							if (treatmentEndDate != null
									&& treatmentEndDate.getValue() != null
									&& treatmentStartDate != null
									&& treatmentStartDate.getValue() != null) {
								if (treatmentEndDate.getValue().before(
										treatmentStartDate.getValue())) {
									treatmentEndDate.setValue(null);
									showErrorMessage("Treatment End date cannot be lesser than the Treatment Start Date");

								} else {
									if (treatmentStartDate != null
											&& treatmentEndDate != null
											&& treatmentStartDate.getValue() != null
											&& treatmentEndDate.getValue() != null) {
										Long noOfDays = SHAUtils
												.getDaysBetweenDate(
														treatmentStartDate
																.getValue(),
														treatmentEndDate
																.getValue());
										hospitalizationNoOfDays
												.setValue(noOfDays.toString());
									}
								}
							}
						}
					});

			// int componentIndex =
			// firstFLayout.getComponentIndex(domicillaryHospitalisation);
			/*
			 * firstFLayout.addComponent(treatmentStartDate, componentIndex +
			 * 1); firstFLayout.addComponent(treatmentEndDate, componentIndex +
			 * 2 );
			 */

			secondFLayout.addComponent(hospitalizationNoOfDays);
		}
	}

	public void suspiousPopupMessage() {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();
				if (bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if (bean.getIsPedWatchList()) {
					alertMessageForPEDWatchList();
				} else if ((bean.getHospitalizaionFlag() || bean
						.getPartialHospitalizaionFlag())
						&& bean.getPostHospitalizaionFlag()
						&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
								.getPostHospClaimedAmount(bean) > 5000d)) {
					if (!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}

				} else if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
			}
		});

		HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public Boolean alertMessageForConsiderForPayment() {
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.CONSIDER_FOR_PAYMENT_ALERT + "</b>",
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
		// dialog.setCaption("Alert");
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

	public Boolean alertMessageForPostHosp() {
		Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.POST_HOSP_ALERT_MESSAGE + "</b>",
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
		// dialog.setCaption("Alert");
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
				bean.setShouldShowPostHospAlert(true);
				if (bean.getIsHospitalDiscountApplicable()) {
					alertForHospitalDiscount();
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
			}
		});
		return true;
	}
	
	public void showCMDAlert() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
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
					if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} else if (bean.getClaimCount() > 1) {
						alertMessageForClaimCount(bean.getClaimCount());
					} else if (!bean.getSuspiciousPopupMap().isEmpty()
							&& !bean.getIsPopupMessageOpened()) {
						suspiousPopupMessage();
					} else if (bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if (bean.getIsPedWatchList()) {
						alertMessageForPEDWatchList();
					} else if ((bean.getHospitalizaionFlag() || bean
							.getPartialHospitalizaionFlag())
							&& bean.getPostHospitalizaionFlag()
							&& (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils
									.getPostHospClaimedAmount(bean) > 5000d)) {
						if (!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
					} else if (bean.getIsHospitalDiscountApplicable()) {
						alertForHospitalDiscount();
					} else if (bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}	
				}
			});
		}

	private VerticalLayout commonValues() {

		cmbReconsiderationRequest = new ComboBox("Reconsideration Request");
		// cmbReconsiderationRequest =
		// binder.buildAndBind("Reconsideration Request",
		// "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		String reconsiderationFlag = "";
		List<SelectValue> values = new ArrayList<SelectValue>();
		SelectValue value = new SelectValue();
		if (this.bean.getPreauthDataExtractionDetails()
				.getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
				.getReconsiderationFlag())) {
			reconsiderationFlag = "Yes";
			value.setId(1l);
			value.setValue("Yes");
		} else {
			reconsiderationFlag = "No";
			value.setId(1l);
			value.setValue("No");
		}
		values.add(value);
		BeanItemContainer<SelectValue> beanContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		beanContainer.addAll(values);
		cmbReconsiderationRequest.setContainerDataSource(beanContainer);
		cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		cmbReconsiderationRequest.setReadOnly(false);

		for (int i = 0; i < beanContainer.size(); i++) {
			if (null != beanContainer.getIdByIndex(i)
					&& null != beanContainer.getIdByIndex(i).getValue()) {
				if ((reconsiderationFlag).equalsIgnoreCase(beanContainer
						.getIdByIndex(i).getValue().trim())) {
					this.cmbReconsiderationRequest.setValue(beanContainer
							.getIdByIndex(i));
				}
			}
		}
		cmbReconsiderationRequest.setReadOnly(true);

		cmbReasonForReconsideration = binder.buildAndBind(
				"Reason for Reconsideration", "reasonForReconsideration",
				ComboBox.class);
		// cmbReasonForReconsideration = new
		// ComboBox("Reason for Reconsideration");
		List<SelectValue> reconsiderValues = new ArrayList<SelectValue>();
		SelectValue selValue = new SelectValue();
		String reasonForReconsideration = "";
		if (null != this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement().getReconsiderationReasonId()) {
			reasonForReconsideration = this.bean
					.getPreauthDataExtractionDetails().getDocAckknowledgement()
					.getReconsiderationReasonId().getValue();
			selValue.setId(1l);
			selValue.setValue(reasonForReconsideration);
		}
		reconsiderValues.add(selValue);
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		reconsiderBeanContainer.addAll(reconsiderValues);
		cmbReasonForReconsideration
				.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration
				.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setReadOnly(false);

		if (null != this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement().getReconsiderationReasonId()) {
			reasonForReconsideration = this.bean
					.getPreauthDataExtractionDetails().getDocAckknowledgement()
					.getReconsiderationReasonId().getValue();
		}

		for (int i = 0; i < reconsiderBeanContainer.size(); i++) {
			if ((reasonForReconsideration)
					.equalsIgnoreCase(reconsiderBeanContainer.getIdByIndex(i)
							.getValue())) {
				this.cmbReasonForReconsideration
						.setValue(reconsiderBeanContainer.getIdByIndex(i));
			}
		}
		cmbReasonForReconsideration.setReadOnly(true);

		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		reconsiderRequestDetails.initPresenter(SHAConstants.ZONAL_REVIEW);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setViewDetailsObj(viewDetailsObj);
		if (null != reconsiderRODRequestList
				&& !reconsiderRODRequestList.isEmpty()) {
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				// if(null != reconsiderationMap &&
				// !reconsiderationMap.isEmpty())
				{
					// Boolean isSelect =
					// reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(true);
				}
				// reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
			}
			// reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}
		reconsiderRequestDetails.setEnabled(false);

		HorizontalLayout vLayout = new HorizontalLayout(
				reconsiderRequestDetails);
		vLayout.setMargin(true);
		vLayout.setCaption("Earlier Request Reconsidered");

		if (("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement().getReconsiderationRequest())) {
			reconsiderationLayout = new VerticalLayout(new FormLayout(
					cmbReconsiderationRequest, cmbReasonForReconsideration),
					vLayout);
		} else {
			reconsiderationLayout = new VerticalLayout(new FormLayout(
					cmbReconsiderationRequest));
		}

		return reconsiderationLayout;
	}

	private HorizontalLayout buildRefferedToMedicalLayout() {
		txtReasonForReferringFromBilling = (TextField) binder.buildAndBind(
				"Reason for reffering to Medical", "billingMedicalReason",
				TextField.class);
		txtReasonForReferringFromBilling.setEnabled(false);

		txtRemarksForReferringFromBilling = (TextField) binder.buildAndBind(
				"Remarks", "billingMedicalRemarks", TextField.class);
		txtRemarksForReferringFromBilling.setEnabled(false);

		txtReasonForReferringFromClaimApproval = (TextField) binder
				.buildAndBind("Reason for reffering to Medical",
						"claimApprovalMedicalReason", TextField.class);
		txtReasonForReferringFromClaimApproval.setEnabled(false);

		txtRemarksForReferringFromClaimApproval = (TextField) binder
				.buildAndBind("Remarks", "claimApprovalMedicalRemarks",
						TextField.class);

		txtRemarksForReferringFromClaimApproval.setEnabled(false);

		txtReasonForReferringFromFinancialApproval = (TextField) binder
				.buildAndBind("Reason for reffering to Medical",
						"financialApprovalMedicalReason", TextField.class);

		txtReasonForReferringFromFinancialApproval.setEnabled(false);

		txtRemarksForReferringFromFinancialApproval = (TextField) binder
				.buildAndBind("Remarks", "financialApprovalMedicalRemarks",
						TextField.class);

		txtRemarksForReferringFromFinancialApproval.setEnabled(false);

		FormLayout billingDetailsFormLayout = new FormLayout(
				txtReasonForReferringFromBilling,
				txtRemarksForReferringFromBilling);
		billingDetailsFormLayout.setCaption("Billing Details");

		FormLayout claimApprovalFormLayout = new FormLayout(
				txtReasonForReferringFromClaimApproval,
				txtRemarksForReferringFromClaimApproval);
		claimApprovalFormLayout.setCaption("Claim Approval Details");

		FormLayout financialApprovalFormLayout = new FormLayout(
				txtReasonForReferringFromFinancialApproval,
				txtRemarksForReferringFromFinancialApproval);
		financialApprovalFormLayout.setCaption("Financial Approval Details");

		HorizontalLayout refferToMedicalLayout = new HorizontalLayout();
		refferToMedicalLayout.addComponent(billingDetailsFormLayout);
		refferToMedicalLayout.addComponent(claimApprovalFormLayout);
		refferToMedicalLayout.addComponent(financialApprovalFormLayout);

		return refferToMedicalLayout;

	}

	private VerticalLayout buildBenefitsTableLayout() {
		cmbPABenefits = (ComboBox) binder.buildAndBind("Benefits",
				"paBenefits", ComboBox.class);
		benefitsTableCaptionFld = new TextField();
		paBenefitsTable = paBenefitsTableObj.get();
		paBenefitsTable.init();
		VerticalLayout benefitsLayout = new VerticalLayout();
		benefitsLayout.addComponent(cmbPABenefits);
		benefitsLayout.addComponent(paBenefitsTable);

		benefitsLayout.setCaption("Part I - Table Benefits");

		addPABenefitsListener();

		if ( null != this.bean.getPreauthDataExtractionDetails()
						.getReconsiderationFlag()) {
			if (this.bean.getPreauthDataExtractionDetails()
					.getReconsiderationFlag()
					.equalsIgnoreCase(SHAConstants.YES_FLAG)
					&& this.bean.getIsPaymentSettled()) {
				//benefitsLayout.setEnabled(false);
				cmbPABenefits.setEnabled(false);
				paBenefitsTable.setEnableBasedOnNoOfWeeks();
			} else {
				benefitsLayout.setEnabled(true);
			}
		}
		
		//IMSSUPPOR-28381 - reject reconsider
		/*if (("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
				.getReconsiderationFlag()))
		{
			cmbPABenefits.setEnabled(false);
			if(null != paBenefitsTable)
				paBenefitsTable.setEnabled(false);
		}*/
		return benefitsLayout;

	}

	private VerticalLayout buildCoversTableLayout() {
		paOptionalCoversTable = paOptionalCoversTableObj.get();
		paOptionalCoversTable.init(SHAConstants.OPTIONAL_COVER_TABLE,
				referenceData);

		paAddOnCoversTable = paAddOnCoversTableObj.get();
		paAddOnCoversTable.init(SHAConstants.ADDITIONAL_COVER_TABLE,
				referenceData);
	
		VerticalLayout coversTableLayout = new VerticalLayout();
		coversTableLayout.addComponent(paAddOnCoversTable);
		coversTableLayout.addComponent(paOptionalCoversTable);

		/*if (null != this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement()
				&& null != this.bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement().getReconsiderationRequest()) {
			if (this.bean.getPreauthDataExtractionDetails()
					.getDocAckknowledgement().getReconsiderationRequest()
					.equalsIgnoreCase(SHAConstants.YES_FLAG)
					&& this.bean.getIsPaymentSettled()) {
				coversTableLayout.setEnabled(false);
			} else {
				coversTableLayout.setEnabled(true);
			}
		}*/

		/*
		if (("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement().getReconsiderationRequest()))
		{
			if(null != paAddOnCoversTable)
				paAddOnCoversTable.setEnabled(false);
			
			if(null != paOptionalCoversTable)
				paOptionalCoversTable.setEnabled(false);
		}*/
		return coversTableLayout;
	}

	private VerticalLayout buildFVRGradingTableLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();
		if (!this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO()
				.isEmpty()) {
			fvrGradingTableObj = fvrGradingTableInstance.get();
			fvrGradingTableObj.initView(bean, false);
			verticalLayout.addComponent(fvrGradingTableObj);
		}

		/*
		 * if(fvrGradingTableObj != null) {
		 * verticalLayout.addComponent(fvrGradingTableObj); }
		 */

		return verticalLayout;
	}

	private HorizontalLayout buildInvestigationLayout() {
		FormLayout investigationFLayout = new FormLayout();

		/*investigationReportReviewedChk = (CheckBox) binder.buildAndBind(
				"Investigation Report Reviewed", "investigationReportReviewed",
				CheckBox.class);
		investigationReportReviewedChk.setEnabled(false);
		investigatorName = (ComboBox) binder.buildAndBind("Investigator Name",
				"investigatorName", ComboBox.class);
		
		investigatorName = (ComboBox) binder.buildAndBind("Investigator Name",
				"investigationNameVal", ComboBox.class);
		
		investigatorName.setEnabled(false);
		investigationReviewRemarks = (TextArea) binder.buildAndBind(
				"Investigation Review Remarks", "investigationReviewRemarks",
				TextArea.class);
		investigationReviewRemarks.setEnabled(false);
		investigationReviewRemarks.setMaxLength(100);*/

		/*investigationFLayout = new FormLayout(investigationReportReviewedChk,
				investigatorName, investigationReviewRemarks);*/
		
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.INVS_REVIEW_REMARKS_LIST, this.bean);
		invsReviewRemarksTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
	//	HorizontalLayout specialistHLayout = new HorizontalLayout(investigationFLayout);
		
		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);

		/*VerticalLayout investigationLayout = new VerticalLayout();
		investigationLayout.addComponent(investigationFLayout);
		investigationLayout.setComponentAlignment(investigationFLayout,
				Alignment.MIDDLE_RIGHT);*/
		return specialistHLayout;
	}

	private VerticalLayout buildMedicalAndTreatmentVerficationLayout() {
		VerticalLayout medicalAndTreatmentVerificationLayout = new VerticalLayout();

		medicalVerificationTableObj = medicalVerificationInstance.get();
		medicalVerificationTableObj.init("Medical Verification", false);

		treatmentVerificationTableObj = treatmentVerificationTableInstance
				.get();
		treatmentVerificationTableObj.init("Treatment Quality Verification",
				false);

		medicalAndTreatmentVerificationLayout
				.addComponent(medicalVerificationTableObj);
		medicalAndTreatmentVerificationLayout
				.addComponent(treatmentVerificationTableObj);

		return medicalAndTreatmentVerificationLayout;
	}

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		if (null != claimRequestButtonObj) {
			this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			switch (clickedButton) {
			case 1:

				if (null != this.bean.getIsReBilling()
						&& this.bean.getIsReBilling()) {
					this.claimRequestButtonObj.buildSendReplyLayout(
							ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS,
							SHAConstants.PA_BILLING);
					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
				} else if (this.bean.getIsReplyToFA() != null
						&& this.bean.getIsReplyToFA()) {
					this.claimRequestButtonObj.buildSendReplyLayout(
							ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS,
							SHAConstants.PA_FINANCIAL);
					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
				} else {
					this.claimRequestButtonObj
							.buildSendReplyLayout(
									ReferenceTable.CLAIM_REQUEST_SEND_REPLY_CLAIM_APPROVAL_STATUS,
									SHAConstants.PA_CLAIM_APPROVAL);
					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_CLAIM_APPROVAL_STATUS);
				}

				break;

			case 2:
				if (this.bean.getStatusKey() != null
						&& !this.bean
								.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
					this.bean.getPreauthMedicalDecisionDetails()
							.setReasonForRefering("");
					this.bean.getPreauthMedicalDecisionDetails()
							.setAllocationTo(null);
					this.bean.getPreauthMedicalDecisionDetails()
							.setFvrTriggerPoints("");

				}
				this.claimRequestButtonObj
						.buildInitiateFieldVisit(
								dropDownValues,
								ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);

				break;

			case 3:

				if (this.bean.getStatusKey() != null
						&& !this.bean
								.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
					this.bean.getPreauthMedicalDecisionDetails()
							.setReasonForRefering("");
					this.bean.getPreauthMedicalDecisionDetails()
							.setAllocationTo(null);
					this.bean.getPreauthMedicalDecisionDetails()
							.setTriggerPointsToFocus("");

				}
				this.claimRequestButtonObj
						.buildInitiateInvestigation(
								dropDownValues,
								ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);

				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
				break;
			case 4:

				if (this.bean.getStatusKey() != null
						&& !this.bean
								.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
					this.bean.getPreauthMedicalDecisionDetails()
							.setReasonForRefering("");

				}
				this.claimRequestButtonObj
						.buildReferCoordinatorLayout(
								dropDownValues,
								ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);

				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
				break;
			case 5:
				if (fileUploadValidatePage()) {
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
							// System.out.println("close called");
						}
					});

					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS);
				}
				break;

			case 6:

				// this.claimRequestButtonObj.buildEscalateLayout(dropDownValues);
				// //
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);
				// this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
				// break;
				if (fileUploadValidatePage()) {
					specialistWindow.init(bean);
					BeanItemContainer<SelectValue> masterValueByReference = masterService
							.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
					specialistWindow.buildEscalateLayout(dropDownValues,
							fileViewUI, masterValueByReference);
					specialistWindow.center();
					specialistWindow.setHeight("400px");
					specialistWindow.setResizable(false);
					specialistWindow.setModal(true);
					specialistWindow.addSubmitHandler(this);
					UI.getCurrent().addWindow(specialistWindow);

					specialistWindow.addCloseListener(new CloseListener() {
						private static final long serialVersionUID = -4381415904461841881L;

						public void windowClose(CloseEvent e) {
							// System.out.println("close called");
						}
					});

					// this.claimRequestFileUploadUI.init(bean, wizard);
					// this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);

					// this.medicalDecisionTableObj.setVisibleApproveFields(false);
					// if(this.bean.getStatusKey() != null &&
					// !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS))
					// {
					// this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
					// }
					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
				}
				break;

			case 7:
				// this.claimRequestButtonObj.buildSpecialistLayout(dropDownValues);
				if (this.bean.getStatusKey() != null
						&& !this.bean.getStatusKey().equals(
								ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
					this.bean.getPreauthMedicalDecisionDetails()
							.setReasonForRefering("");
					this.bean.getPreauthMedicalDecisionDetails()
							.setReasonForReferring("");
				}
				if (fileUploadValidatePage()) {
					specialistWindow.init(bean);
					specialistWindow.buildSpecialityLayout(dropDownValues,
							fileViewUI);
					specialistWindow.center();
					specialistWindow.setHeight("400px");
					specialistWindow.setResizable(false);
					specialistWindow.setModal(true);
					specialistWindow.addSubmitHandler(this);
					UI.getCurrent().addWindow(specialistWindow);

					specialistWindow.addCloseListener(new CloseListener() {
						private static final long serialVersionUID = -4381415904461841881L;

						public void windowClose(CloseEvent e) {
							// System.out.println("close called");
						}
					});

					// this.claimRequestFileUploadUI.init(bean, wizard);
					// this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);

					// this.medicalDecisionTableObj.setVisibleApproveFields(false);

					this.bean
							.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
				}
				break;
			case 8:

				this.claimRequestButtonObj
						.buildQueryLayout(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);
				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
				break;
			case 9:

				this.claimRequestButtonObj.buildRejectLayout(dropDownValues,
						ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);
				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
				break;
			case 10:
				// Integer min = Math
				// .min(amountConsideredTable.getMinimumValue(),
				// SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
				// .getValue()));

				this.claimRequestButtonObj
						.generateFieldsForSuggestApproval(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
				break;
			case 11:
				this.claimRequestButtonObj
						.builtCancelRODLayout(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
				this.bean
						.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
				break;
			default:
				break;
			}
		}
	}

	public boolean fileUploadValidatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";

		/*
		 * fireViewEvent(
		 * PAClaimRequestDataExtractionPagePresenter.CHECK_INVESTIGATION_INITIATED
		 * , this.bean.getClaimKey());
		 */

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

		if (this.medicalVerificationTableObj != null
				&& !this.medicalVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		if (this.treatmentVerificationTableObj != null
				&& !this.treatmentVerificationTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj
					.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		if (this.treatmentVerificationTableObj != null
				&& !this.treatmentVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.treatmentVerificationTableObj
					.getErrorsForRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		if (this.medicalVerificationTableObj != null
				&& !this.medicalVerificationTableObj.isValidTable()) {
			hasError = true;
			List<String> errors = this.medicalVerificationTableObj
					.getErrorsforRemarks();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}

		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean
				.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		if (!fvrGradingDTO.isEmpty()) {
			for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
				List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO
						.getFvrGradingDTO();
				for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
					if (fvrGradingDTO3.getStatus() == null) {
						hasError = true;
						eMsg += "Please Select All the FVR Grading and set the Status. </br>";
						break;
					}
				}
			}
		}

		// if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
		// SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
		// hasError = true;
		// eMsg +=
		// "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
		// }

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
				// this.bean.getPreauthMedicalDecisionDetails()
				// .setMedicalDecisionTableDTO(
				// this.medicalDecisionTableObj.getValues());
				/*
				 * this.bean.getPreauthMedicalDecisionDetails().
				 * setMedicalVerificationTableDTO
				 * (this.medicalVerificationTableObj.getValues());
				 * this.bean.getPreauthMedicalDecisionDetails
				 * ().setTreatmentVerificationDTO
				 * (this.treatmentVerificationTableObj.getValues());
				 */
				return true;
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}

	public void setMedicalDecisionValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		this.claimRequestButtonObj.suggestApprovalClick(dto,
				medicalDecisionTableValues);
	}

	/*public void setInvestigationCheck(Boolean checkInitiateInvestigation) {
		bean.setIsInvestigation(checkInitiateInvestigation);
		
		 * if (!checkInitiateInvestigation) { <<<<<<< HEAD
		 * unbindField(investigationReportReviewedChk);
		 * unbindField(investigationReviewRemarks);
		 * unbindField(investigatorName);
		 * mandatoryFields.remove(investigationReviewRemarks); } else
		 if (checkInitiateInvestigation
				&& bean.getStatusKey() != null
				&& bean.getStatusKey().equals(
						ReferenceTable.UPLOAD_INVESIGATION_COMPLETED)) {
			investigationReportReviewedChk.setEnabled(true);
			investigationReviewRemarks.setEnabled(true);
			investigatorName.setEnabled(true);
			// mandatoryFields.remove(investigationReviewRemarks);
		} else {
			// unbindField(investigationReportReviewedChk);
			// unbindField(investigationReviewRemarks);
			// unbindField(investigatorName);
			bean.setIsInvestigation(false);
			investigationReportReviewedChk.setEnabled(false);
			investigationReviewRemarks.setEnabled(false);
			investigatorName.setEnabled(false);
		}

	}*/

	public void generateFieldBasedOnSentToCPU(Boolean isChecked) {
		claimRequestButtonObj.generateFieldsBasedOnSentToCPU(isChecked);
	}

	@Override
	public void submit(PreauthDTO preauthDTO) {
		specialistWindow.close();
		wizard.finish();

	}

	public void setPABenefitsValues(List<PABenefitsDTO> paBenefitsList,
			Long benefitsId, Map referenceDataMap,List<PABenefitsDTO> benefitsDBList) {
		if (null != paBenefitsTable) {
			BeanItemContainer<SelectValue> benefitContainer = (BeanItemContainer<SelectValue>) referenceDataMap
					.get("coverContainerValue");
			if (null != referenceData) {
				PreauthDTO dto = (PreauthDTO) referenceData.get("preauthDTO");
				referenceDataMap.put("preauthDTO", dto);
			}
			paBenefitsTable.initReferenceId(benefitsId);
			paBenefitsTable.setReferenceData(referenceDataMap);
			paBenefitsTable.init();
			paBenefitsTable.removeAllItems();
			paBenefitsTable.setTableData(paBenefitsList);
			if (null != paBenefitsList && !paBenefitsList.isEmpty()) {
				PABenefitsDTO dto = paBenefitsList.get(0);
				for (int i = 0; i < benefitContainer.size(); i++) {
					if ((benefitContainer.getIdByIndex(i).getId()).equals(dto
							.getBenefitsId())) {
						dto.setBenefitCoverValue(benefitContainer
								.getIdByIndex(i));
					}
				}

				if (null != benefitsId
						&& null != this.bean.getPreauthDataExtractionDetails()
								.getOnloadBenefitId()
						&& benefitsId.equals(this.bean
								.getPreauthDataExtractionDetails()
								.getOnloadBenefitId())) {
					List<PABenefitsDTO> benefitsDTOList = this.bean
							.getPreauthDataExtractionDetails()
							.getPaBenefitsList();
					/*if (null != benefitsDTOList && !benefitsDTOList.isEmpty()) {
						paBenefitsTable.setTableList(benefitsDTOList);*/

						if (null != benefitsDBList && !benefitsDBList.isEmpty()) {
							paBenefitsTable.setTableList(benefitsDBList);
						/*
						 * for (PABenefitsDTO paBenefitsDTO : benefitsDTOList) {
						 * if(null != paBenefitsDTO.getBenefitCoverValue() &&
						 * paBenefitsDTO
						 * .getBenefitCoverValue().getId().equals(dto
						 * .getBenefitCoverValue().getId())) {
						 * dto.setKey(paBenefitsDTO.getKey()); } }
						 */
					}
					// paBenefitsTable.setPABenefitTableValue(paBenefitsList.get(0));
					// paBenefitsTable.addBeanToList(dto);
				} else {
					paBenefitsTable.addBeanToList(dto);
				}
			}
		}

	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		claimRequestButtonObj.setUploadDTOForBillEntry(uploadDTO);
		// TODO Auto-generated method stub

	}

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if (claimRequestButtonObj != null) {
			claimRequestButtonObj.setCategoryValues(selectValueContainer);
		}
	}

	public void validateCoversAndBenefitsForClaims(Boolean value,
			String presenterString, Long benefitId, TextField eligibleFld,
			TextField amtFld, ComboBox categoryCmb) {
		/**
		 * If validation is false. then error message will be thrown.
		 * */
		if (SHAConstants.PA_BENEFITS.equalsIgnoreCase(presenterString)) {
			if (!value) {
				// cmbPABenefits.setValue(bean.getPreauthDataExtractionDetails().getPaBenefits());				
				showErrorMessage(SHAConstants.BENEFITS_ADD_OPT_ERR_MESSAGE);
				loadPABenefitsDropDown();
			} else {
				/*fireViewEvent(
						PAClaimRequestDataExtractionPagePresenter.PA_BENEFITS_TABLE_VALUES,
						benefitId, bean.getNewIntimationDTO()
								.getInsuredPatient().getKey(),bean.getKey());*/
				fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_BENEFITS_TABLE_VALUES,bean,benefitId);
			}
		} else if (SHAConstants.ADDITIONAL_COVER_TABLE
				.equalsIgnoreCase(presenterString)) {
			List<AddOnCoversTableDTO> addOnCoversList = (List<AddOnCoversTableDTO>) referenceData
					.get("addOnCoverProc");

			if (!value) {
				// cmbPABenefits.setValue(bean.getPreauthDataExtractionDetails().getPaBenefits());
				showErrorMessage(SHAConstants.BENEFITS_ADD_OPT_ERR_MESSAGE);
				paAddOnCoversTable.resetCoversDropDown(categoryCmb, amtFld,
						eligibleFld);
				loadPABenefitsDropDown();
				// categoryCmb.setValue(null);
			} else {
				paAddOnCoversTable.populateCoversTableData(eligibleFld, amtFld,
						benefitId, addOnCoversList);
			}
		} else if (SHAConstants.OPTIONAL_COVER_TABLE
				.equalsIgnoreCase(presenterString)) {
			List<AddOnCoversTableDTO> optionalCoversList = (List<AddOnCoversTableDTO>) referenceData
					.get("optionalCoverProc");
			if (!value) {
				showErrorMessage(SHAConstants.BENEFITS_ADD_OPT_ERR_MESSAGE);
				paAddOnCoversTable.resetCoversDropDown(categoryCmb, amtFld,
						eligibleFld);
				loadPABenefitsDropDown();
			} else {
				paAddOnCoversTable.populateCoversTableData(eligibleFld, amtFld,
						benefitId, optionalCoversList);
			}
		}

	}

	public void validateBenefitsForClaim(Boolean isValid2,
			String presenterString, Long benefitsId,
			List<PABenefitsDTO> paBenefitsValueList, Map referenceDataMap) {
		// TODO Auto-generated method stub

	}

	public void loadPABenefitsValues(List<PABenefitsDTO> paBenefitsList,
			GComboBox cmb) {

		paBenefitsTable.loadBenefitsTableBasedOnBenefit(paBenefitsList, cmb);
	}

	public void buildNomineeLayout() {
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		unbindField(chkNomineeDeceased);
		chkNomineeDeceased = null;
		if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null &&
				!bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) { 
			nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.setViewColumnDetails();
			nomineeDetailsTable.generateSelectColumn();
			chkNomineeDeceased = (CheckBox) binder.buildAndBind(
					"Nominee Deceased", "isNomineeDeceased",
					CheckBox.class);
		}	
		
		wholeVLayout.addComponent(nomineeDetailsTable);
		
		if(chkNomineeDeceased != null){
			wholeVLayout.addComponent(chkNomineeDeceased);
			addNomineeDeceasedListener();
		}
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
		
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Map<String,Object> refData = new HashMap<String, Object>();
		relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		legalHeirDetails.init(bean);
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		wholeVLayout.addComponent(legalHeirLayout);
		
		if(isNomineeDeceased()){
			enableLegalHeir = Boolean.TRUE;
			nomineeDetailsTable.setEnabled(false);
		}

		if(enableLegalHeir) {
			
			legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
//			legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
			legalHeirDetails.getBtnAdd().setEnabled(true);
		}
		else {
			legalHeirDetails.deleteRows();
			legalHeirDetails.getBtnAdd().setEnabled(false);
		}
	}
	
	public void setClearReferenceData(){
    	this.diagnosisDetailsTableObj.clearObject();
    	this.diagnosisDetailsTableObj = null;
    }
	public MessageBox showInfoMessageBox(String message) {
		final MessageBox msgBox = MessageBox.createInfo()
				.withCaptionCust("Information").withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		return msgBox;
	}
	
	public MessageBox showAlertMessageBox(String message) {
		final MessageBox msgBox = MessageBox.createWarning()
				.withCaptionCust("Warning").withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		return msgBox;

	}
	
	private void getHospitalReinstatedAlert() {

	    final MessageBox showInfoMessageBox = showInfoMessageBox("Hospital reinstated after Suspension, Mandatory to discuss with Cluster Head");
    	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
    	
    	homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
	});
	}
	
	private void addNomineeDeceasedListener(){
		 chkNomineeDeceased
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					boolean value = (Boolean) event.getProperty().getValue();
					
					if(value){
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(false);
						}
						if(legalHeirDetails != null){
							if(bean.getLegalHeirDTOList() != null && !bean.getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
							}
							legalHeirDetails.getBtnAdd().setEnabled(true);
						}
					}else{
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(true);
						}
						if(legalHeirDetails != null){
							legalHeirDetails.deleteRows();
							legalHeirDetails.getBtnAdd().setEnabled(false);
						}
					}
					 
					}
							
					}
					
			});
	 }
	 
	 private Boolean isNomineeDeceased(){
		 if(chkNomineeDeceased != null && chkNomineeDeceased.getValue() != null && chkNomineeDeceased.getValue()){
			 return true;
		 }
		 return false;
	 }
	
}
