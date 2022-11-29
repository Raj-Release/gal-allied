package com.shaic.paclaim.health.reimbursement.financial.pages.billreview;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

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
import com.shaic.claim.ReportDto;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingTable;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.premedical.listenerTables.DiganosisDetailsListenerForPremedical;
import com.shaic.claim.premedical.listenerTables.ProcedureListenerTableForPremedical;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.wizard.PatientCareTable;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.UpdateHospitalDetails;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewIrdaNonPayablePdfPage;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.cashless.listenertables.PASectionDetailsListenerTable;
import com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization.PAHealthFinancialButtonsForFirstPage;
import com.shaic.paclaim.health.reimbursement.financial.tables.PAHealthBillClassificationEditUI;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPagePresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthFinancialReviewPageUI extends ViewComponent{

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;
	
	@EJB
	private PolicyService policyService;
	
	private GWizard wizard;
	
	private Window popup;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	@Inject
	private ViewIrdaNonPayablePdfPage pdfPageUI;
	
	@Inject
	private Instance<RoomRentMatchingTable> roomRentMatchingTable;
	
	@Inject
	private Instance<com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable> uploadDocumentListenerTable;
	
	@Inject
	private Instance<PAHealthAddOnBenefitsDataExtractionPage> addOnBenifitsPageInstance;
	
	@Inject
	private Instance<PatientCareTable> patientCareTableInstance;
	
	@Inject
	private BillClassificationUI billClassificationUI;
	
	@Inject
	private Instance<PAHealthBillClassificationEditUI> billClassificationEditUIInstance;
	
	
	private PAHealthBillClassificationEditUI billClassificationEditUIObj;
	
	@Inject
	private Instance<UpdateHospitalDetails> hospitalDetailsInstance;
	
	@Inject
	private Instance<PAHealthFinancialButtonsForFirstPage> financialButtonInstance;
	
	@Inject
	private Instance<PASectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private PASectionDetailsListenerTable sectionDetailsListenerTableObj;
	
	private PAHealthFinancialButtonsForFirstPage financialButtonObj;
	
	private UpdateHospitalDetails updateHospitalDetailsObj;
	
	private PatientCareTable patientCareTableObj;
	
	private RoomRentMatchingTable roomRentMatchingTableObj;
	
	private PAHealthAddOnBenefitsDataExtractionPage addOnBenefitsPageObj;
	
	private com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable uploadDocumentListenerTableObj;
	
	private DateField admissionDate;
	
	private DateField dischargeDate;
	
	private OptionGroup workOrNonWorkSpace;
	
	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;

	private FormLayout firstFLayout;
	
	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	private OptionGroup changeInDiagnosis;
	
	VerticalLayout tableVLayout;
	
	private Date admissionDateValue;

	private FormLayout patientStatusFLayout;
	
	private OptionGroup hospitalAddOnBenefits;
	
	private OptionGroup patientCareAddOnBenefits;

	private VerticalLayout paymentDetailsLayout;

	private OptionGroup optPaymentMode;
	
	private TextArea txtBillingRemarks;
	
	private TextArea txtMedicalRemarks;
	
	private FormLayout dynamicRemarksForm;
	
    public ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private TextField txtIfscCode;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private TextField txtReasonForChangeInDOA;
	
	private Button viewIRDAButton;
	
	private Button billingWorksheetBtn;
	
	private FormLayout forms;
	
	private Boolean isValid = false;
	
	private String alertMessages = "";
	
	private Button btnIFCSSearch;
	
	// Added below fields for Bypass functionality..............
	
	public TextField txtAdmissionDate ;
	
	public TextField txtDischargeDate;

	private TextField noOfDaysTxt;
	
	private ComboBox cmbRoomCategory;
	
	private OptionGroup ventilatorSupportOption;

	private ComboBox cmbIllness;

	private TextField preauthApprovedAmt;
	
	private TextField changeOfDOA;

	private ComboBox cmbSection;
	
	private TextField automaticRestorationTxt;
	
	private ComboBox cmbSpecifyIllness;
	
	private CheckBox criticalIllnessChk;
	
	private TextField reasonForAdmissionTxt;
	
	private TextField dischargeReason;
	
	private TextField admissionReason;
	
	public Boolean isMappingDone = false;
	
	public Boolean isMatchTheFollowing = false;
	
	//private ComboBox cmbCatastrophicLoss;
	
	private ComboBox cmbNatureOfLoss;
			
	private ComboBox cmbCauseOfLoss;
	
	private Button editBillClassification;
	
	private Button verifyAcntDtlButton;
	
	@Inject
	private Instance<VerificationAccountDeatilsTable> verificationAccountDeatilsTableInstance;
	
	private VerificationAccountDeatilsTable verificationAccountDeatilsTableObj;

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	private SelectValue existingPayeeName;

	private VerticalLayout legalHeirLayout;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	private FormLayout legaHeirLayout;
	
	private OptionGroup domicillaryHospitalisation;
	
	private DateField treatmentStartDate;
	
	private DateField treatmentEndDate;
	
	private TextField hospitalizationNoOfDays;
	
	private DiganosisDetailsListenerForPremedical diagnosisDetailsTableObj;
	
	private ProcedureListenerTableForPremedical procedurdTableObj;
	
	private CheckBox chkNomineeDeceased;
	
	@EJB
	private MasterService masterService;
	
	@Override
	public String getCaption() {
		return "Bill Review";
	}

	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
			existingPayeeName = this.bean.getPreauthDataExtractionDetails().getPayeeName();
		}
	}
	


	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}
	
	public Boolean alertMessageForPED() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
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
				bean.setIsPEDInitiated(false);
				if(bean.getIsPaymentAvailableShown()) {
					alertMessageForPaymentAvailable();
				} else if(bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				}
			}
		});
		return true;
	}
	
	public Boolean alertMessageForAutoRestroation() {/*
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.AUTO_RESTORATION_MESSAGE + "</b>",
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
				bean.setIsAutoRestorationDone(true);
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				}
			}
		});
		*/
		return true;
	}
	
	@SuppressWarnings("static-access")
	public Boolean warningMessageForLumpsum(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
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
				alertMessageForMediPremier(SHAConstants.LUMPSUM_ALERT_MESSAGE);
			}
		});
		return true;
	}
	
	@SuppressWarnings("static-access")
	public Boolean alertMessageForMediPremier(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
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
				if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} 
			}
		});
		return true;
	}
	
	public Boolean alertMessageForPaymentAvailable() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PAYMENT_AVAIL_MESSAGE + "</b>",
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
				bean.setIsPaymentAvailableShown(false);
				if(bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				}
			}
		});
		return true;
	}
	

	private void alertMessageForClaimCount(Long claimCount) {

		String msg = "";
		DBCalculationService dbService = new DBCalculationService();
		int gpaClaimCount = dbService.getGPAClaimCount(bean.getPolicyKey(),
				bean.getNewIntimationDTO().getInsuredPatient().getInsuredId());

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
		successLabel.addStyleName(ValoTheme.LABEL_H3);
		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		VerticalLayout firstForm = new VerticalLayout(dummyField, mainHor,
				homeButton);
		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		Panel panel = new Panel();
		panel.setContent(firstForm);

		if (this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <= 2) {
			panel.addStyleName("girdBorder1");
		} else if (this.bean.getClaimCount() > 2) {
			panel.addStyleName("girdBorder2");
		}

		panel.setSizeFull();

		popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
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
				} else if (bean.getNewIntimationDTO().getPolicy().getProduct()
						.getCode()
						.equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE)
						&& bean.getNewIntimationDTO().getPolicy()
								.getPolicyType() != null
						&& bean.getNewIntimationDTO().getPolicy()
								.getPolicyType().getKey()
								.equals(ReferenceTable.FRESH_POLICY)) {
					if (bean.getClaimDTO().getClaimSectionCode() != null
							&& bean.getClaimDTO()
									.getClaimSectionCode()
									.equalsIgnoreCase(
											ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(),
								SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if (bean.getClaimDTO().getClaimSectionCode() != null
							&& bean.getClaimDTO()
									.getClaimSectionCode()
									.equalsIgnoreCase(
											ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}

				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	@SuppressWarnings("static-access")
	public static Boolean alertMessageForLumpsum(String message) {
		Label successLabel = new Label("<b style = 'color: red;'>" + message
				+ "</b>", ContentMode.HTML);
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
		dialog.show(UI.getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();

			}
		});
		return true;
	}
	
	
	public Component getContent() {
		
		if(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag())) {

			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
			suspiousPopupMessage();
		} else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if(bean.getIsPaymentAvailableShown()) {
			alertMessageForPaymentAvailable();
		} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
			alertMessageForPostHosp();
		} else if(bean.getIsHospitalDiscountApplicable()){
			alertForHospitalDiscount();
		} else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
			if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
				alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
			} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
				warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
			} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
				StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
			}
		} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
			StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
		
		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
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
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.SARAL_SURAKSHA_CARE_PRODUCT_CODE)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.PA_SARAL_HOSPITALISATION_ALERT,"Information");
		}
		if(bean.getIsHospitalExpenseCoverSelected()) {
			SHAUtils.showMessageBoxWithCaption(SHAConstants.PA_SARAL_HOSPITALISATION_APPROVE_DISABLE_ALERT,"Information");
		}
		initBinder();
		isMappingDone = false;
		isMatchTheFollowing = false;
		isValid = false;
		this.bean.setAlertMessageOpened(false);
		alertMessages ="";
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setEnabled(false);
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		dischargeDate = (DateField) binder.buildAndBind(
				"Date of Discharge", "dischargeDate", DateField.class);
		noOfDaysTxt = (TextField) binder.buildAndBind("Length of Stay", "noOfDays",
				TextField.class);
		noOfDaysTxt.setRequired(true);
		noOfDaysTxt.setMaxLength(4);
		addLengthOfStayValueChangeListner();
		criticalIllnessChk = (CheckBox) binder.buildAndBind("Critical Illness",
				"criticalIllness", CheckBox.class);
		criticalIllnessChk.setEnabled(false);
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		
		ventilatorSupportOption = (OptionGroup) binder.buildAndBind(
				"Ventilator Support", "ventilatorSupport", OptionGroup.class);
		ventilatorSupportOption.addItems(getReadioButtonOptions());
		ventilatorSupportOption.setItemCaption(true, "Yes");
		ventilatorSupportOption.setItemCaption(false, "No");
		ventilatorSupportOption.setStyleName("horizontal");
		ventilatorSupportOption.setEnabled(false);
		
		cmbSpecifyIllness = (ComboBox) binder.buildAndBind("Specify",
				"specifyIllness", ComboBox.class);
		cmbSpecifyIllness.setReadOnly(true);
		
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
		automaticRestorationTxt = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		automaticRestorationTxt.setEnabled(false);
		
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt.getValue()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt.getValue()))
		{
			cmbIllness.setEnabled(false);
		}
		
		preauthApprovedAmt = (TextField) binder.buildAndBind(
				"Pre-auth Approved Amt", "preauthTotalApprAmt", TextField.class);
		preauthApprovedAmt.setEnabled(false);
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getPreauthApprAmt() == null){
			preauthApprovedAmt.setNullRepresentation("");
		}
		
		cmbSection = (ComboBox) binder.buildAndBind(
				"Section", "section", ComboBox.class);
		
		if(this.bean.getNewIntimationDTO() != null && !ReferenceTable.getSectionKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}
		
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
		workOrNonWorkSpace.setEnabled(false);
		
		Boolean shouldDisableDomicillary = StarCommonUtils.shouldDisableDomicillary(bean);
		if(!shouldDisableDomicillary) {
			bean.getPreauthDataExtractionDetails().setDomicillaryHospitalisation(false);
		}
		
		domicillaryHospitalisation = (OptionGroup) binder.buildAndBind(
				"Claim for Domicillary Hospitalisation", "domicillaryHospitalisation", OptionGroup.class);
		
		domicillaryHospitalisation.addItems(getReadioButtonOptions());
		domicillaryHospitalisation.setItemCaption(true, "Yes");
		domicillaryHospitalisation.setItemCaption(false, "No");
		domicillaryHospitalisation.setStyleName("horizontal");
		
		domicillaryHospitalisation.setEnabled(shouldDisableDomicillary);
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		if((this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER) ||
				this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)) ||
				(this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER) ||
						this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL))
				&& (this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))){
		firstFLayout = new FormLayout(admissionDate, cmbRoomCategory,ventilatorSupportOption, illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt, cmbSection);
		}
		else
		{
			firstFLayout = new FormLayout(admissionDate, cmbRoomCategory,ventilatorSupportOption, illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt, cmbSection,domicillaryHospitalisation);
		}
		patientStatusFLayout = new FormLayout(workOrNonWorkSpace,dischargeDate, noOfDaysTxt, automaticRestorationTxt, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		
		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				patientStatusFLayout);
		formHLayout.setWidth("100%");
		
		if(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() != null && this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation()) {
			generatedFieldsBasedOnDomicillaryHosp(true);
		}
		
		uploadDocumentListenerTableObj =   uploadDocumentListenerTable.get();
		uploadDocumentListenerTableObj.initPresenter(SHAConstants.FINANCIAL);
		uploadDocumentListenerTableObj.init();
		uploadDocumentListenerTableObj.setCaption("Upload Documents");
		
		
		hospitalAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Hospital Cash) Applicable ", "hospitalCashAddonBenefits", OptionGroup.class);
		
		txtBillingRemarks = (TextArea) binder.buildAndBind(
				"Billing Remarks", "billingRemarks", TextArea.class);
		
		txtMedicalRemarks =(TextArea) binder.buildAndBind("Medical Remarks", "medicalRemarks", TextArea.class);
	    forms = new FormLayout();

	    FormLayout medicalFormLayout = new FormLayout();
	    FormLayout billingFormLayout = new FormLayout();
//	    
	    if(this.bean.getIsReBilling()){
//	    	medicalFormLayout.addComponent(txtBillingRemarks);
//	    	medicalFormLayout.setCaption("Billing");
	    } 
	    	
	    if(this.bean.getIsReMedical()){
	    	
//	    	billingFormLayout.addComponent(txtMedicalRemarks);
//    	    billingFormLayout.setCaption("Medical");
	    	
	    }
	    
	    HorizontalLayout remarksHorizontal = new HorizontalLayout(medicalFormLayout,billingFormLayout);
	    remarksHorizontal.setWidth("700px");
//	    
//	    remarksHorizontal.setSpacing(true);
//	    
	    forms.addComponent(remarksHorizontal);
		
		hospitalAddOnBenefits.addItems(getReadioButtonOptions());
		hospitalAddOnBenefits.setItemCaption(true, "Yes");
		hospitalAddOnBenefits.setItemCaption(false, "No");
		hospitalAddOnBenefits.setStyleName("horizontal");
		
		patientCareAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Patient Care) Applicable ", "patientCareAddOnBenefits", OptionGroup.class);
		
		patientCareAddOnBenefits.addItems(getReadioButtonOptions());
		patientCareAddOnBenefits.setItemCaption(true, "Yes");
		patientCareAddOnBenefits.setItemCaption(false, "No");
		patientCareAddOnBenefits.setStyleName("horizontal");
		
		addOnBenefitsPageObj = addOnBenifitsPageInstance.get();
		Boolean isStarCare = false;
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_FLOATER) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_INVIDUAL))) {
			isStarCare = true;
		}
		bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setAdmissionDate(bean.getNewIntimationDTO().getAdmissionDate());
		bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setDischargeDate(bean.getPreauthDataExtractionDetails().getDischargeDate());

		addOnBenefitsPageObj.init(bean.getPreauthDataExtractionDetails().getUploadDocumentDTO(), SHAConstants.FINANCIAL, isStarCare);
		addOnBenefitsPageObj.getContent();
		
		
		viewIRDAButton = new Button("View IRDA Non-Payables");
		viewIRDAButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7439398898591124006L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setClosable(true);
				pdfPageUI.init(null, null,popup);
				popup.setContent(pdfPageUI);
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
		
		HorizontalLayout layout = new HorizontalLayout(viewIRDAButton, billingWorksheetBtn);
		layout.setWidth("100%");
		layout.setComponentAlignment(viewIRDAButton, Alignment.MIDDLE_RIGHT);
		
		updateHospitalDetailsObj = hospitalDetailsInstance.get();
		updateHospitalDetailsObj.initView(this.bean);
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);
		
		addTotalClaimedListener();
		billClassificationUI.init(bean);
		
		editBillClassification = new Button("EDIT");
		editBillClassification.setStyleName(ValoTheme.BUTTON_LINK);
		editBillClassification.setEnabled(false);
		addEditListener();
		HorizontalLayout editLayout = new HorizontalLayout(new Label("BILL CLASSIFICATION"), editBillClassification);
		editLayout.setSpacing(true);
		VerticalLayout billclasihori =new VerticalLayout(editLayout,billClassificationUI);
		
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement()
				.getDocumentReceivedFromId()
				.getKey()
				.equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			editBillClassification.setEnabled(false);
			if(bean.getClaimDTO().getClaimSubCoverCode() != null && (bean.getClaimDTO().getClaimSubCoverCode().equalsIgnoreCase(ReferenceTable.NEW_BORN_LUMPSUM_SUB_COVER_CODE) || bean.getClaimDTO().getClaimSubCoverCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SUB_COVER_CODE)) ) {
				editBillClassification.setEnabled(false);
			}
		}
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.FINANCIAL);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());
		
//		getPaymentDetailsLayout();
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			wholeVLayout = new VerticalLayout(formHLayout, layout,sectionDetailsListenerTableObj, forms,billclasihori,uploadDocumentListenerTableObj, addOnBenefitsPageObj/*,paymentDetailsLayout*/);
			//wholeVLayout = new VerticalLayout(layout, forms,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj, paymentDetailsLayout);
			wholeVLayout.setSpacing(true);
		}else{
			wholeVLayout = new VerticalLayout(formHLayout, layout,sectionDetailsListenerTableObj, forms,billclasihori,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj/*, paymentDetailsLayout*/);
			//wholeVLayout = new VerticalLayout(layout, forms,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj, paymentDetailsLayout);
			wholeVLayout.setSpacing(true);
		}
		
		
		
		if(this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null
				&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(this.bean.getPreauthDataExtractionDetails().getPatientStatus().getId())
						|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(this.bean.getPreauthDataExtractionDetails().getPatientStatus().getId())
						|| (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue()))				
				   && bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())		
				   && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())) {
			
			buildNomineeLayout();
		}
		
		addListener();
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(dischargeDate);
		mandatoryFields.add(cmbRoomCategory);
		
		if(bean.getIsFinalEnhancement() || (!bean.getHospitalizaionFlag() && !bean.getPartialHospitalizaionFlag())) {
			admissionDate.setEnabled(false);
			dischargeDate.setEnabled(false);
		}
		admissionDateValue = admissionDate.getValue();
		showOrHideValidation(false);
		txtBillingRemarks.setReadOnly(true);
		txtMedicalRemarks.setReadOnly(true);
		
		editBillClassification.setEnabled(false);
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}
		noOfDaysTxt.setValue(bean.getPreauthDataExtractionDetails().getNoOfDays());
		
		// As per User request added below buttons layout (Refer to Medical and Refer to Billing and Refer to Coordinator)
		 financialButtonObj =  financialButtonInstance.get();
		 financialButtonObj.initView(this.bean, this.wizard);
		 wholeVLayout.addComponent(financialButtonObj);
		 
		return wholeVLayout;
	}
	
	private void addLengthOfStayValueChangeListner() {



		noOfDaysTxt.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				noOfDaysTxt.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -8435623803385270083L;

					@SuppressWarnings("unchecked")
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (admissionDate != null && dischargeDate != null
								&& admissionDate.getValue() != null
								&& dischargeDate.getValue() != null) {
							Long daysBetweenDate = SHAUtils.getDaysBetweenDate(
									admissionDate.getValue(), dischargeDate.getValue());
							daysBetweenDate++;
							
							String enteredNo = (String) event.getProperty().getValue();
							if (enteredNo != null && ((daysBetweenDate >= 0
									&& SHAUtils.getDoubleFromStringWithComma(enteredNo) > daysBetweenDate.doubleValue()) ||
									(daysBetweenDate >= 0
											&& SHAUtils.getDoubleFromStringWithComma(enteredNo) < daysBetweenDate.doubleValue()-2))) {
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Length of Stay should be DOD-DOA (+1 or -1)", buttonsNamewithType);
								event.getProperty().setValue(null);
							}
						}
					}
				});
				
			}
		});
	
		
	
		
	
		
	}



	/*@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeListener()
	{
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
			}
		});
		
		
		
		
		optPaymentMode.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
				
			}
		});
	}*/
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
//	private void alertMessageForClaimCount(Long claimCount){
//		
//		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
//		
//		
//   		Label successLabel = new Label(
//				"<b style = 'color: black;'>"+msg+"</b>",
//				ContentMode.HTML);
////   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
////   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
//   		successLabel.addStyleName(ValoTheme.LABEL_H3);
//   		Button homeButton = new Button("ok");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//   		FormLayout firstForm = new FormLayout(successLabel,homeButton);
//		Panel panel = new Panel(firstForm);
//		
//		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
//			panel.addStyleName("girdBorder1");
//		}else if(this.bean.getClaimCount() >2){
//			panel.addStyleName("girdBorder2");
//		}
//		
//		panel.setHeight("103px");
////		panel.setSizeFull();
//		
//		
//		popup = new com.vaadin.ui.Window();
//		popup.setWidth("30%");
//		popup.setHeight("20%");
////		popup.setContent( viewDocumentDetailsPage);
//		popup.setContent(panel);
//		popup.setClosable(true);
//		
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//		
//		homeButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//
//				popup.close();
//				if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
//					suspiousPopupMessage();
//				} else if(bean.getIsPEDInitiated()) {
//					alertMessageForPED();
//				} else if(bean.getIsPaymentAvailableShown()) {
//					alertMessageForPaymentAvailable();
//				} else if(bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
//					if(!bean.getShouldShowPostHospAlert()) {
//						alertMessageForPostHosp();
//					}
//				} else if(bean.getIsHospitalDiscountApplicable()){
//					alertForHospitalDiscount();
//				} else if(bean.getIsAutoRestorationDone()) {
//					alertMessageForAutoRestroation();
//				}
//				
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//	}
	
	public Boolean alertMessageForPEDWatchList() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
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
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}

	
	public void openBillingSheet(PreauthDTO preautDto){
		
		List<PreauthDTO> preauthDtoList = new ArrayList<PreauthDTO>();
		preauthDtoList.add(preautDto);
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = null;
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(preautDto.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDtoList);
		
		final String filepath = docGenarator.generatePdfDocument("BillSummaryOtherProducts", reportDto);
		
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Billing Work Sheet PDF");
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
	
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirFirstName);
		fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
//		fieldList.add(txtPayableAt);
		return fieldList;
	}
	
	/*private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		unbindField(optPaymentMode);
		paymentModeListener();
	//	//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		//optPaymentMode.select(true);
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		
		

		if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() &&
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))
		{
			optPaymentMode.setValue(true);
		}
		else
		{
			optPaymentMode.setValue(false);
		}
		


		 if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				 && this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
		{
			 optPaymentMode.setReadOnly(true);
			 optPaymentMode.setEnabled(false);
			 if(btnIFCSSearch != null){
				 btnIFCSSearch.setEnabled(false);
			 }
		}else{
			optPaymentMode.setReadOnly(false);
			optPaymentMode.setEnabled(true);
			if(btnIFCSSearch != null){
				btnIFCSSearch.setEnabled(true);
			}
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
			
		}
		
		//buildPaymentsLayout();
	}*/
	
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
		List<UploadDocumentDTO> uploadDocsList = this.bean.getUploadDocumentDTO();
		/*if(null != uploadDocsList && !uploadDocsList.isEmpty())
		{
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if(null != admissionDate)
					uploadDocumentDTO.setDateOfAdmission(SHAUtils.formatDate(admissionDate.getValue()));
				if(null != dischargeDate)
					uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(dischargeDate.getValue()));
				uploadDocumentDTO.setIntimationNo(this.bean.getNewIntimationDTO().getIntimationId());
				uploadDocumentDTO.setInsuredPatientName(this.bean.getNewIntimationDTO().getInsuredPatientName());
			}
			
			this.bean.setUploadDocumentDTO(uploadDocsList);
		}*/
		
		if(this.uploadDocumentListenerTableObj != null) {
			uploadDocumentListenerTableObj.setReferenceData(referenceData);
			
			Integer i = 1;
			List<UploadDocumentDTO> uploadList = this.bean.getUploadDocumentDTO();
			uploadDocumentListenerTableObj.setTableInfo(uploadList);
			SectionDetailsTableDTO sectionDTO = this.sectionDetailsListenerTableObj.getValue();
			if(null !=  uploadList && !uploadList.isEmpty())
			for (UploadDocumentDTO uploadDocLayout : uploadList) {
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getProduct())
				{
					uploadDocLayout.setProductKey( bean.getNewIntimationDTO().getPolicy()
					.getProduct().getKey());
				}
				if(null != this.bean.getClaimDTO().getClaimSubCoverCode())// && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue())
					uploadDocLayout.setSubCoverCode( this.bean.getClaimDTO().getClaimSubCoverCode());
				if(null != this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
					uploadDocLayout.setDomicillaryFlag(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation());
				uploadDocLayout.setSeqNo(i);
				this.uploadDocumentListenerTableObj.addBeanToList(uploadDocLayout);
				i++;
			}
			
			//uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
		}
		
//		reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		//Setting this feild readOnly true, since this needs to be editable as per sathish sir.
		
		this.bean.getPolicyDto().setAdmissionDate(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
//		fireViewEvent(PreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean);
		

//		cmbPatientStatus.setContainerDataSource(patientStatus);
//		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbPatientStatus.setItemCaptionPropertyId("value");
//
//
//		if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
//			this.cmbPatientStatus.setValue(this.bean
//					.getPreauthDataExtractionDetails().getPatientStatus());
//		}
		
		
		BeanItemContainer<SelectValue> patientStatus = (BeanItemContainer<SelectValue>) referenceData
				.get("patientStatus");
		
		BeanItemContainer<SelectValue> roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");
		
		BeanItemContainer<SelectValue> illness = (BeanItemContainer<SelectValue>) referenceData
				.get("illness");
		
		BeanItemContainer<SelectValue> criticalIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("criticalIllness");
		
		BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
				.get("section");

		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		this.bean.getPolicyDto().setClaimKey(this.bean.getClaimKey());
		
		if(cmbSection != null && cmbSection.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails().setSection(sectionValue);
		}
		
		

		cmbPatientStatus.setContainerDataSource(patientStatus);
		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientStatus.setItemCaptionPropertyId("value");
		
		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");
		
		cmbIllness.setContainerDataSource(illness);
		cmbIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIllness.setItemCaptionPropertyId("value");
		
		cmbSpecifyIllness.setReadOnly(false);
		cmbSpecifyIllness.setContainerDataSource(criticalIllness);
		cmbSpecifyIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecifyIllness.setItemCaptionPropertyId("value");
		
		cmbSpecifyIllness.setReadOnly(true);
		
		cmbSection.setContainerDataSource(section);
		cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSection.setItemCaptionPropertyId("value");
		
//		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
//			this.cmbSection.setValue(this.bean
//					.getPreauthDataExtractionDetails().getSection());
//		}
		
		List<SelectValue> itemIds = section.getItemIds();
		

		if (this.bean.getPreauthDataExtractionDetails().getSection() != null && this.bean.getPreauthDataExtractionDetails().getSection().getId() != null) {
			for (SelectValue selectValue : itemIds) {
				if(this.bean.getPreauthDataExtractionDetails().getSection().getId().equals(selectValue.getId())){
					this.cmbSection.setValue(selectValue);
				}
			}
//			this.cmbSection.setValue(this.bean
//					.getPreauthDataExtractionDetails().getSection());
		}


		if (this.bean.getPreauthDataExtractionDetails().getRoomCategory() != null) {
			this.cmbRoomCategory.setValue(this.bean
					.getPreauthDataExtractionDetails().getRoomCategory());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()!= null) {
			System.out.println(String.format("Ventilator Support value already exist: [%s]", this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()));
			this.ventilatorSupportOption.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
			ventilatorSupportOption.setEnabled(true);
			ventilatorSupportOption.setRequired(true);
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
			this.cmbSpecifyIllness.setReadOnly(false);
			this.cmbSpecifyIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getSpecifyIllness());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
			this.cmbPatientStatus.setValue(this.bean
					.getPreauthDataExtractionDetails().getPatientStatus());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			this.cmbIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getIllness());
		}
		
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
		
		/*if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			
			this.cmbCatastrophicLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss());
		}*/
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			this.cmbNatureOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss());
		}
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			this.cmbCauseOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss());
		}
		
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);

		List<DocumentDetailsDTO> docDTOList = (List<DocumentDetailsDTO>) referenceData.get(SHAConstants.BILL_CLASSIFICATION_DETAILS);
		this.bean.setDocumentDetailsDTOList(docDTOList);
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		setTableValues();
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
		
		if(this.addOnBenefitsPageObj != null && !this.addOnBenefitsPageObj.validatePage()) {
			List<String> errors = this.addOnBenefitsPageObj.getErrors();
			hasError = true;
			for (String error : errors) {
				eMsg += error + "</br>";
			 }
		}
		
		/*if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			if(daysBetweenDate >= 0 && SHAUtils.getIntegerFromString(noOfDaysTxt.getValue()) > daysBetweenDate.intValue()) {
				hasError = true;
				eMsg += "No of days should be DOD-DOA+1 </br>"; 
			}
		}*/
		if(noOfDaysTxt != null && noOfDaysTxt.getValue() == null || (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().length() == 0)) {
			hasError = true;
			eMsg += "Please Enter the Length of Stay. </br>";
		}
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			hasError = true;
			eMsg += "Please Select Ventilator Support. </br>";
		}
		
		List<UploadDocumentDTO> values = uploadDocumentListenerTableObj.getValues();
		
		if(!bean.getIsFirstPageSubmit()){
			for (UploadDocumentDTO uploadDocumentDTO : values) {
				if(uploadDocumentDTO.getFileType() != null && uploadDocumentDTO.getFileType().getValue().contains("Bill")){
					if (!uploadDocumentDTO.getStatus()) {
						hasError = true;
						eMsg += "Please review the Bill Entry details</br>";
					}
				}
			}
		}
		
//		if(cmbPayeeName != null){
//			SelectValue selected = (SelectValue)cmbPayeeName.getValue();
//			if(existingPayeeName != null && selected != null 
//					&& ! existingPayeeName.getValue().equalsIgnoreCase(selected.getValue())){
//				if(txtReasonForChange != null && txtReasonForChange.getValue() == null || txtReasonForChange.getValue().isEmpty()){
//					hasError = true;
//					eMsg += "Please Enter Reason for changing payee name</br>";
//				}
//			}
//		}
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& ! this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
		
			if (this.updateHospitalDetailsObj != null) {
				boolean isValid = this.updateHospitalDetailsObj.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.updateHospitalDetailsObj.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		
		if(!hasError) {
			if(billClassificationEditUIObj != null && !bean.getPreHospitalizaionFlag() && billClassificationEditUIObj.checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION)) {
//				hasError = true;
//				eMsg += "Bill Entry classification should match with the Bill Classifcation. Please make the change and proceed further. </br>";
			}
			
			if(billClassificationEditUIObj != null && !bean.getPostHospitalizaionFlag() && billClassificationEditUIObj.checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION)) {
//				hasError = true;
//				eMsg += "Bill Entry classification should match with the Bill Classifcation. Please make the change and proceed further. </br>";
			}
		}
		

		SelectValue patientStatusSelect = cmbPatientStatus.getValue() != null ? (SelectValue)cmbPatientStatus.getValue() : null;
		Boolean accedentDeath = bean.getPreauthDataExtractionDetails().getAccidentOrDeath();

		Long docRecFromId = bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null ? bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey() : null; 
		if(((patientStatusSelect != null 
				&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatusSelect.getId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatusSelect.getId())))
				|| (accedentDeath != null && !accedentDeath))	
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

	
			if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() && !isNomineeDeceased()){
				List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
			
				if(tableList != null && !tableList.isEmpty()){
					bean.getNewIntimationDTO().setNomineeList(tableList);
					StringBuffer nomineeNames = new StringBuffer("");
					int selectCnt = 0;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}

						/**
						Acc. Preference disable for Nominee hence commented below code as per Mr. Satish Sir on 14-12-2019
						**/						
						/*if(nomineeDetailsDto.getPreference() == null || nomineeDetailsDto.getPreference().isEmpty()) {
							eMsg +="Please Select Account Preference for Nominee</br>";
							hasError = true;
							break;
						}*/
						
					}
					bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
					if(selectCnt > 0 && !hasError){
						bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
						bean.getNewIntimationDTO().setNomineeAddr(null);
//						hasError = false;
					}
					else{
						bean.getNewIntimationDTO().setNomineeName(null);
						
						eMsg += "Please Select Nominee</br>";
						hasError = true;						
					}
						
					if(selectCnt > 0) {
						String payableAtValidation = nomineeDetailsTable.validatePayableAtForSelectedNominee();
						if(!payableAtValidation.isEmpty()){
							eMsg += payableAtValidation;
							hasError = true;
						}
						
						String ifscValidation = nomineeDetailsTable.validateIFSCForSelectedNominee();
						if(!ifscValidation.isEmpty()){
							eMsg += ifscValidation;
							hasError = true;
						}
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
					eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)</br>";
				}
				// Below Condition for Account Preference mandatory for Bancs
				List<LegalHeirDTO> legalHeirDtls = legalHeirDetails.getValues();
				 if(legalHeirDtls != null && !legalHeirDtls.isEmpty()) {
					 for (LegalHeirDTO legalHeirDtlsDTO : legalHeirDtls) {
						 if((legalHeirDtlsDTO.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(legalHeirDtlsDTO.getPaymentModeId()))
								 || (legalHeirDtlsDTO.getPaymentMode() != null && !legalHeirDtlsDTO.getPaymentMode())){
							if(legalHeirDtlsDTO.getAccountPreference() == null || legalHeirDtlsDTO.getAccountPreference().getValue() == null){
								bean.setLegalHeirDTOList(null);
								hasError = true;
								eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %,Account Preference)";
							}
						 }
					}
				 }
					
			}					
		}
		/*if(bean.getPreauthDataExtractionDetails().getPaymentModeFlag() == ReferenceTable.PAYMENT_MODE_BANK_TRANSFER && !this.bean.getVerificationClicked()){
			hasError = true;
			eMsg += ("Please Verify Account Details Button.</br>");
		}*/
		
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
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				if(!isMappingDone) {
					SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
					SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
					SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
					getMappingDone();
				}
				
				
				if(!bean.getIsComparisonDone()  && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat())) {
					fireViewEvent(PAHealthFinancialReviewPagePresenter.COMPARE_WITH_PREVIOUS_ROD, bean);
				}
				if(this.patientCareTableObj != null) {
					this.bean.getPreauthDataExtractionDetails().setPatientCareDTO(this.patientCareTableObj.getValues());
				}
				
				
                 if(null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO()){
					
					if(null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareAddOnBenefits() && 
							this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareAddOnBenefits())
					{
						fireViewEvent(PAHealthFinancialReviewPagePresenter.SAVE_PATIENT_CARE_TABLE_VALUES, this.bean);
					}
					if (null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getHospitalCashAddonBenefits() && 
							this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getHospitalCashAddonBenefits())
					{
						fireViewEvent(PAHealthFinancialReviewPagePresenter.SAVE_HOSPITAL_CASH_TABLE_VALUES, this.bean);
					}
				}
                 
         	if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
        				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
        				&& ! this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
	                
	         		if(this.bean.getPreauthDataExtractionDetails().getUpdateHospitalDetails() != null) {
	           	      ZonalReviewUpdateHospitalDetailsDTO hospitalDTO = this.bean.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
	           	      if(hospitalDTO.getInpatientBeds() != null){
	      	       	    	  Integer inpatientBeds = SHAUtils.getIntegerFromString(hospitalDTO.getInpatientBeds());
	      	       	    	  if(inpatientBeds > 15){

	      	       	    		alertMessages += "No of Inpatient Beds should be above 15 !!! </br>";
	      	       	    		this.bean.setAlertMessageOpened(true);
	      	       	    	  }
	           	      }
	         		}
         	     }else{
         		   this.bean.setAlertMessageOpened(true);
         	    }
         		
         		List<UploadDocumentDTO> uploadDocsList = this.bean.getUploadDocumentDTO();
				if(null != uploadDocsList && !uploadDocsList.isEmpty())
				{
					for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
						if(null != admissionDate)
							uploadDocumentDTO.setDateOfAdmission(SHAUtils.formatDate(admissionDate.getValue()));
						if(null != dischargeDate)
							uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(dischargeDate.getValue()));
						uploadDocumentDTO.setIntimationNo(this.bean.getNewIntimationDTO().getIntimationId());
						uploadDocumentDTO.setInsuredPatientName(this.bean.getNewIntimationDTO().getInsuredPatientName());
					}
					
					this.bean.setUploadDocumentDTO(uploadDocsList);
				}
         		
//                
//                Notification alertMessage = new Notification("Alert", "No of Inpatient Beds should be above 15", Type.HUMANIZED_MESSAGE);
//                alertMessage.setDelayMsec(50);
//                alertMessage.setPosition(Position.MIDDLE_CENTER);
//                alertMessage.show(this.getUI().getCurrent().getPage());
           
          
                 
			} catch (CommitException e) {
				e.printStackTrace();
			}
			
			showOrHideValidation(false);
		    return true;
          
		}
	}
	
	/*private String validateProcedureAndDiagnosisName()
	{
		String eMsg = "";
		StringBuffer strBuf = new StringBuffer();
		List<DiagnosisDetailsTableDTO> diagList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		List<ProcedureDTO> procList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if((null != diagList && !diagList.isEmpty()) && (null != procList && !procList.isEmpty()))
		{
			for(DiagnosisDetailsTableDTO diagObj : diagList)
			{
				String strDiagName = diagObj.getDiagnosis();
				
				for(ProcedureDTO procObj : procList)
				{
					String strProcName = procObj.getProcedureNameValue();
					if(strDiagName.equals(strProcName))
					{
						strBuf.append("Diagnosis and Procedure are same. Diagnosis"+" "+ strDiagName).append("\n");
					}
				}
			}
		}
		
		if(null != strBuf && strBuf.length() > 0)
		{
			eMsg = strBuf.toString();
		}
		
		return eMsg;
	}*/

	private PedDetailsTableDTO setPEDDetailsToDTO(DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		if(pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
		return dto;
	}
	@SuppressWarnings("unchecked")
	public void setTableValuesToDTO() {
		
		
	}
	
	public Boolean alertMessage() {
		

	       	    	Label successLabel = new Label("<b style = 'color: red;'> Number of Inpatient Beds should be above 15 !!!</b>", ContentMode.HTML);
	       	    	
	       	    	

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
//	       	 		dialog.setCaption("Alert");
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
	       	 			    wizard.next();
	       	 			    dialog.close();

	       	 			}
	       	 		});
		
		return isValid;
		
	}
	
  
		
	private void setTableValues() {
		if(this.bean.getPreauthDataExtractionDetails().getHospitalCashAddonBenefits() != null && this.bean.getPreauthDataExtractionDetails().getHospitalCashAddonBenefits()) {
			generateFieldsBasedOnHospitalCashBenefits(true);
		}
		if(this.bean.getPreauthDataExtractionDetails().getPatientCareAddOnBenefits() != null && this.bean.getPreauthDataExtractionDetails().getPatientCareAddOnBenefits()) {
			generateFieldsBasedOnPatientCareBenefits(true);
		}
	}

	protected void addListener() {

		cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						PAHealthFinancialReviewPagePresenter.BILLING_PATIENT_STATUS_CHANGED,
						null);
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
				
				if(enteredDate != null) {
					
					if(dischargeDate != null && dischargeDate.getValue() != null) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(enteredDate,dischargeDate.getValue());
						if(daysBetweenDate>=30){
							showWarningMsg("No of days stayed is more than 30 days");
						}
						
						if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
							Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
							noOfDays++;
							noOfDaysTxt.setValue(noOfDays.toString());
						}
					}
					
					if(enteredDate != null && deathDate != null && deathDate.getValue() != null){
						if(enteredDate.after(deathDate.getValue())){
//							admissionDate.setValue(null);
							isValid = false;
							showAdmissionDateError();
							if(admissionDateValue != null){
							admissionDate.setValue(admissionDateValue);
							}
						}
					}
					
					if(enteredDate != null && dischargeDate != null && dischargeDate.getValue() != null){
						if(enteredDate.after(dischargeDate.getValue())){
							isValid = false;
							showErrorMessage("Admission date should not be greater than Discharge date");
							if(admissionDateValue != null){
								admissionDate.setValue(admissionDateValue);
								}
						}
					}else if (enteredDate != null && dischargeDateValue != null) {
						if (dischargeDateValue.before(enteredDate)) {
//							event.getProperty().setValue(null);
//							dischargeDate.setValue(null);
							isValid = false;
							showErrorMessage("Discharge date cannot be lesser than the date of admission");
							if(admissionDateValue != null){
								admissionDate.setValue(admissionDateValue);
								}

						}
					}
					
					
					
					Date policyFromDate = bean.getPolicyDto().getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);
					
					
					if (!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate)  || enteredDate.compareTo(policyToDate) == 0)) {
						 isValid = false;
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
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);;
						dialog.show(getUI().getCurrent(), null, true);
						
						event.getProperty().setValue(null);
					}
				}
				
				if(isValid){
					admissionDateValue = enteredDate;
				}
				
			}
		});
		
		dischargeDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (bean.getPreauthDataExtractionDetails()
						.getAdmissionDate() != null) {
//					Date admission = (Date) ((DateField) event.getProperty()).getData();
					Date enteredDate = (Date) ((DateField) event.getProperty()).getValue();
					
//					if(enteredDate != null && admissionDate != null && admissionDate.getValue() != null){
//						if(enteredDate.before(admissionDate.getValue())){
//							admissionDate.setValue(null);
//						}
//					}
					if(enteredDate != null && admissionDate != null && admissionDate.getValue() != null) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), enteredDate);
						if(daysBetweenDate>=30){
							showWarningMsg("No of days stayed is more than 30 days");
						}
						
						if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
							Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
							noOfDays++;
							noOfDaysTxt.setValue(noOfDays.toString());
						}
					}
//					
					if (enteredDate != null && admissionDate != null && admissionDate.getValue() != null ) {
						if (enteredDate.before(admissionDate.getValue())) {
//							event.getProperty().setValue(null);
							dischargeDate.setValue(null);
							showErrorMessage("Discharge date cannot be lesser than the date of admission");

						}
						else{
							if(dischargeDate.getValue() != null){
							admissionDate.setData(dischargeDate.getValue());
							}
						}
					}
				}
		
	}
		});
		
		hospitalAddOnBenefits.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(PAHealthFinancialReviewPagePresenter.BILLING_HOSPITAL_BENEFITS, isChecked);
			}
		});
		
		patientCareAddOnBenefits.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(PAHealthFinancialReviewPagePresenter.BILLING_PATIENT_CARE_BENEFITS, isChecked);
			}
		});
		
		cmbSection.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				if (cmbSection != null && cmbSection.getValue() != null) {
					SelectValue sectionValue = (SelectValue) cmbSection
							.getValue();
					bean.getPreauthDataExtractionDetails().setSection(
							sectionValue);
				} else {
//					bean.getPreauthDataExtractionDetails().setSection(null);
				}

				fireViewEvent(
						PAHealthFinancialReviewPagePresenter.FA_UPDATE_PRODUCT_BASED_AMT,
						bean);

			}
		});
		
		domicillaryHospitalisation.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(PAHealthFinancialReviewPagePresenter.GENERATE_DOMICILLARY_FIELDS, isChecked);
			}
		});
		
		/*ventilatorSupportOption
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if (event.getProperty() != null){
					if( event.getProperty().getValue().toString() == "true") {
						bean.getPreauthDataExtractionDetails().setVentilatorSupport(true);
					}
					else{
						bean.getPreauthDataExtractionDetails().setVentilatorSupport(false);
					}
						
				}
				fireViewEvent(
						FinancialReviewPagePresenter.FINANCIAL_VENTILATOR_SUPPORT,
						isChecked);
			}
		});*/
		
this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				String value = property.getValue();
				if(value != null ) {
					// Other than hosptialization section is selected then domicillary should get disabled....
					if(!value.equalsIgnoreCase(ReferenceTable.HOSP_SUB_COVER_CODE)) {
						if(domicillaryHospitalisation.getValue() != null && domicillaryHospitalisation.getValue().toString().equalsIgnoreCase("true")) {
							domicillaryHospitalisation.setValue(false);
						}
						domicillaryHospitalisation.setEnabled(false);
					} else {
						domicillaryHospitalisation.setEnabled(StarCommonUtils.shouldDisableDomicillary(bean));
					}
					fireViewEvent(PAHealthFinancialReviewPagePresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisDetailsTableObj != null && !diagnosisDetailsTableObj.getValues().isEmpty()) {
						diagnosisDetailsTableObj.changeSublimitValues();
					} 
					if(procedurdTableObj != null && !procedurdTableObj.getValues().isEmpty()) {
						procedurdTableObj.changeSublimitValues();
					}
				}
			}
		});

		cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				// bean.getPreauthDataExtractionDetails().setRoomCategory(value);

				if (value != null && value.getValue() != null) {

					System.out.println(String.format(
							"Room Category Value PA Health : [%s]",
							value.getValue()));
				}

				if (value != null
						&& value.getValue() != null
						&& value.getValue().equalsIgnoreCase(
								"ICU/NICU/ICCU/CCU/PICU")) {
					ventilatorSupportOption.setEnabled(true);
					ventilatorSupportOption.setRequired(true);
					// ventilatorSupportOption.setVisible(true);
					mandatoryFields.add(ventilatorSupportOption);
					// bean.getPreauthDataExtractionDetails().setVentilatorSupport(true);
				} else {
					ventilatorSupportOption.setValue(null);
					// unbindField(ventilatorSupportOption);
					ventilatorSupportOption.setEnabled(false);
					ventilatorSupportOption.setRequired(false);
					mandatoryFields.remove(ventilatorSupportOption);
				}

			}
		});
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	public void generateFieldsBasedOnPatientStatus() {
		if (cmbPatientStatus.getValue() != null) {
			if (this.cmbPatientStatus.getValue().toString().toLowerCase()
					.contains("deceased")) {
				deathDate = (PopupDateField) binder.buildAndBind(
						"Date Of Death", "deathDate", PopupDateField.class);
				
				//validation added for death date.
				addDeathDateValueChangeListener();
				
				txtReasonForDeath = (TextField) binder.buildAndBind(
						"Reason For Death", "reasonForDeath", TextField.class);
				txtReasonForDeath.setMaxLength(100);
				cmbTerminateCover = (ComboBox) binder.buildAndBind(
						"Terminate Cover", "terminateCover", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("terminateCover");

				cmbTerminateCover.setContainerDataSource(terminateCover);
				cmbTerminateCover.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbTerminateCover.setItemCaptionPropertyId("value");
				
				if(this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag() != null && this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag().toLowerCase().equalsIgnoreCase("y")) {
					cmbTerminateCover.setValue(this.bean.getPreauthDataExtractionDetails().getTerminateCover());
				}

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
				setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
				mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
				patientStatusFLayout.addComponent(cmbTerminateCover);
				

				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					buildNomineeLayout();
				}
				else {

					if(nomineeDetailsTable != null) { 
						wholeVLayout.removeComponent(nomineeDetailsTable);
					}
					if(chkNomineeDeceased != null){
						wholeVLayout.removeComponent(chkNomineeDeceased);
					}
					if(legalHeirLayout != null) {
						wholeVLayout.removeComponent(legalHeirLayout);
					}					
			   }
				
			} else {
				
				if(nomineeDetailsTable != null) { 
					wholeVLayout.removeComponent(nomineeDetailsTable);
				}
				if(chkNomineeDeceased != null){
					wholeVLayout.removeComponent(chkNomineeDeceased);
				}
				if(legalHeirLayout != null) {
					wholeVLayout.removeComponent(legalHeirLayout);
				}
				
				removePatientStatusGeneratedFields();
			} 
		}
		else {
			if(nomineeDetailsTable != null && legaHeirLayout != null) {
				wholeVLayout.removeComponent(nomineeDetailsTable);
				wholeVLayout.removeComponent(legaHeirLayout);
			}
			if(chkNomineeDeceased != null){
				wholeVLayout.removeComponent(chkNomineeDeceased);
			}
		}
	}
	

	/**
	 * Method to validate death date.
	 * 
	 * */
	private void addDeathDateValueChangeListener()
	{
		deathDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				Date currentSystemDate = new Date();
				if(null != enteredDate && null != admissionDate && null != admissionDate.getValue())
					if(enteredDate.before(admissionDate.getValue()))
					{
						
						showWarningErrors();
						event.getProperty().setValue(null);
					}else if(enteredDate != null && dischargeDate != null && dischargeDate.getValue() != null){
					if(enteredDate.after(dischargeDate.getValue())){
						//showError();
						showWarningErrors();
						event.getProperty().setValue(null);
					}
					else if(enteredDate.after(currentSystemDate)){
						getErrorMessage("Date of Death should not be greater than current date");
						event.getProperty().setValue(null);
					}
				}else if(enteredDate.after(currentSystemDate)){
					getErrorMessage("Date of Death should not be greater than current date");
					event.getProperty().setValue(null);
				}
			}
		});
	}
	
public void showError(){
		
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
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Discharge date should not less than Admission Date</b>", ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				dialog.setContent(layout);
				dialog.setCaption("Error");
				dialog.setClosable(true);
				dialog.show(getUI().getCurrent(), null, true);
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
	
	  private void showWarningErrors(){
			
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
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);
			
		}
	  
	  

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null
				&& cmbTerminateCover != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			mandatoryFields.remove(cmbTerminateCover);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
			patientStatusFLayout.removeComponent(cmbTerminateCover);
		}
	}
	
	private void setCalculatedValue(Integer value1, Integer value2, TextField calculatedValueField) {
		Integer calculatedValue = value1 * value2;
		
		calculatedValueField.setValue(String.valueOf(calculatedValue));
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean value) {
		addOnBenefitsPageObj.generateFieldsBasedOnHospitalCashBenefits(value);
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnPatientCareBenefits(Boolean value) {
		addOnBenefitsPageObj.generateFieldsBasedOnPatientCareBenefits(value);
	}
	
	/*private HorizontalLayout buildChequePaymentLayout(Boolean paymentMode)
	{
		if(cmbPayeeName != null){
			unbindField(cmbPayeeName);
		}
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		
	    if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
		BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);

//		payee.addBean(this.bean.getPreauthDataExtractionDetails().getPayeeName());
		
	    payee = getValuesForNameDropDown();
		 
		cmbPayeeName.setContainerDataSource(payee);
		cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPayeeName.setItemCaptionPropertyId("value");
		
		if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null) {
			List<SelectValue> itemIds = payee.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue.getValue() != null && this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue() != null && selectValue.getValue().toString().toLowerCase().equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue().toString().toLowerCase())) {
					this.bean.getPreauthDataExtractionDetails().getPayeeName().setId(selectValue.getId());
				}
			}
		}
		
		
		cmbPayeeName.setValue(this.bean.getPreauthDataExtractionDetails().getPayeeName());
		cmbPayeeName.setEnabled(false);
	    }
		 
		//cmbPayeeName.setRequired(true);
		
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailId" , TextField.class);
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);
		txtEmailId.setMaxLength(100);
		if(null != this.bean.getPayeeEmailId())
		{
			txtEmailId.setValue(this.bean.getPayeeEmailId());
		}
		//txtEmailId.setRequired(true);
		
		txtReasonForChange = (TextField) binder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
		
		CSValidator reasonForChangeValidator = new CSValidator();
		reasonForChangeValidator.extend(txtReasonForChange);
		reasonForChangeValidator.setRegExp("^[a-zA-Z]*$");
		reasonForChangeValidator.setPreventInvalidTyping(true);
		txtReasonForChange.setMaxLength(100);
		
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
		
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(10);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPanNo())
		{
			txtPanNo.setValue(this.bean.getPreauthDataExtractionDetails().getPanNo());
		}
		
		txtLegalHeirFirstName = (TextField) binder.buildAndBind("","legalFirstName",TextField.class);
		txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);
		
		if(txtPayableAt != null){
			txtPayableAt.setReadOnly(false);
		}
		unbindField(txtPayableAt);
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setMaxLength(50);
		CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z]*$");
		payableAtValidator.setPreventInvalidTyping(true);;
		//txtPayableAt.setRequired(true);
		if(null != this.bean.getPreauthDataExtractionDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getPreauthDataExtractionDetails().getPayableAt());
			txtPayableAt.setEnabled(false);
		}
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
		{	
			txtEmailId.setReadOnly(true);
			txtEmailId.setEnabled(false);
			
			txtReasonForChange.setReadOnly(true);
			txtReasonForChange.setEnabled(false);
			
			txtPanNo.setReadOnly(true);
			txtPanNo.setEnabled(false);
			
			txtLegalHeirFirstName.setReadOnly(true);
			txtLegalHeirFirstName.setEnabled(false);
			
			txtLegalHeirMiddleName.setReadOnly(true);
			txtLegalHeirMiddleName.setEnabled(false);
			
			txtLegalHeirLastName.setReadOnly(true);
			txtLegalHeirLastName.setEnabled(false);
			
			txtPayableAt.setReadOnly(true);
			txtPayableAt.setEnabled(false);
			
		}else{
			cmbPayeeName.setEnabled(true);
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
		}
		
//		GridLayout grid = new GridLayout(5,3);
		
		txtLegalHeirFirstName.setCaption(null);
		txtLegalHeirMiddleName.setCaption(null);
		txtLegalHeirLastName.setCaption(null);
		
		HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirFirstName,txtLegalHeirMiddleName,txtLegalHeirLastName);
		nameLayout.setComponentAlignment(txtLegalHeirFirstName, Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);
		FormLayout formLayout1 = null;
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		}
		else
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo);
		}
		
		if(! paymentMode){
			
			if(txtPayableAt != null){
				formLayout1.removeComponent(txtPayableAt);
			}
		}
		
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,nameLayout);

		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , formLayout2);
//		hLayout.setWidth("90%");
		
		payeenameListner();
		
		return hLayout;
		
		
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		
		btnIFCSSearch = new Button();
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		
		addIFSCCodeListner();
		
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
//		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
	/*	txtAccntNo.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(txtAccntNo.getValue() != null){
					//bean.setAccountNumber(txtAccntNo.getValue());
					bean.getPreauthDataExtractionDetails().setAccountNo(txtAccntNo.getValue());
					bean.setVerificationClicked(false);
					changeVerifiedButtonValue(bean.getVerificationClicked());
				}
			}
		});*/
		//should develop in future
		/*verifyAcntDtlButton = new Button("Verify Account Details");
		 changeVerifiedButtonValue(bean.getVerificationClicked());
		 verifyAcntDtlButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(txtAccntNo.getValue() == null || txtAccntNo.getValue().trim() == ""){
					getAlertMessage("Please Enter Account Number");
				}else{
        		//fireViewEvent(PAHealthFinancialReviewPagePresenter.PA_VERIFICATION_ACCOUNT_DETAILS, bean);
        		if(bean.getVerificationAccountDeatilsTableDTO() !=null && !bean.getVerificationAccountDeatilsTableDTO().isEmpty() ){
				final Window popup = new com.vaadin.ui.Window();
				List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsList = bean.getVerificationAccountDeatilsTableDTO();
				verificationAccountDeatilsTableObj =  verificationAccountDeatilsTableInstance.get();
				verificationAccountDeatilsTableObj.init(bean);
				verificationAccountDeatilsTableObj.setCaption("Account Verification Details");
				if(verificationAccountDeatilsList != null){
					verificationAccountDeatilsTableObj.setTableList(verificationAccountDeatilsList);
				}
				
				popup.setWidth("75%");
				popup.setHeight("70%");
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
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
				Button okBtn = new Button("Close");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				okBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO = verificationAccountDeatilsTableObj.getValues();
						verificationAccountDeatilsTableDTO = new ArrayList<VerificationAccountDeatilsTableDTO>();
						bean.setVerificationAccountDeatilsTableDTO(verificationAccountDeatilsTableDTO);
						popup.close();
					}
				});
				Button saveBtn = new Button("Save");
				saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				saveBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
//					
						if(!validatePagepayment(Boolean.TRUE)) {
							List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO = verificationAccountDeatilsTableObj.getValues();
							//fireViewEvent(PAHealthFinancialReviewPagePresenter.PA_VERIFIED_ACCOUNT_DETAIL_SAVE, bean);
							changeVerifiedButtonValue(bean.getVerificationClicked());
							popup.close();
						}
					}

				});
				VerticalLayout vlayout = new VerticalLayout(verificationAccountDeatilsTableObj);
				HorizontalLayout hLayout = new HorizontalLayout(saveBtn,okBtn);
				hLayout.setSpacing(true);
				vlayout.addComponent(hLayout);
				vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
				popup.setContent(vlayout);
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
        		}else{
        			showInformation("Matched Account Not Found");
        			bean.setVerificationClicked(true);
        			changeVerifiedButtonValue(bean.getVerificationClicked());
        		}
			}
		 }
		});*/
		 
		
		/*if(null != this.bean.getPreauthDataExtractionDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getPreauthDataExtractionDetails().getAccountNo());
		}
		
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
//		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getIfscCode())
		{
			txtIfscCode.setValue(this.bean.getPreauthDataExtractionDetails().getIfscCode());
		}
		
		txtBranch = (TextField) binder.buildAndBind("Branch", "branch", TextField.class);
		txtBranch.setNullRepresentation("");
		txtBranch.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getBranch())
		{
			txtBranch.setValue(this.bean.getPreauthDataExtractionDetails().getBranch());
		}
		
		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		txtBankName.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getBankName())
		{
			txtBankName.setValue(this.bean.getPreauthDataExtractionDetails().getBankName());
		}
		
		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getCity())
		{
			txtCity.setValue(this.bean.getPreauthDataExtractionDetails().getCity());
		}
		
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			txtAccntNo.setReadOnly(true);
			txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(true);
			txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			txtCity.setEnabled(false);
			
		}else{
			
		}
		
		
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		TextField dField3 = new TextField();
		dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField3.setReadOnly(true);
		TextField dField4 = new TextField();
		dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField4.setReadOnly(true);
		TextField dField5 = new TextField();
		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField5.setReadOnly(true);
		dField5.setWidth(2,Unit.CM);
		FormLayout formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,dField1,txtBranch);
		HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
		VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
		hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		
		
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		dField.setWidth("30px");
		
		
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		
		FormLayout bankTransferLayout2 = new FormLayout(verifyAcntDtlButton,dField,btnIFCSSearch);
		FormLayout bankTransferLayout3 = new FormLayout(dField1,dField2,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2,bankTransferLayout3);
		hLayout.setSpacing(false);//,bankTransferLayout3);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
//		hLayout.setWidth("80%");
		
		return hLayout;
	}*/
	
	
	/*public void payeenameListner(){
		if(cmbPayeeName != null){
			 cmbPayeeName.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						SelectValue value = (SelectValue) event.getProperty().getValue();
						bean.getNewIntimationDTO().setRoomCategory(value);
						
						SelectValue payeeName = bean.getPreauthDataExtractionDetails().getPayeeName();
						
						if(payeeName != null && ! payeeName.getId().equals(value.getId())){
							if(! (null != bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))){
								txtReasonForChange.setEnabled(true);
								mandatoryFields.add(txtReasonForChange);
								showOrHideValidation(false);
							}
							
						}else{
							unbindField(txtReasonForChange);
							mandatoryFields.remove(txtReasonForChange);
						}
						
//						List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//						
//						if(procedureTableObj != null){
//							List<ProcedureDTO> values = procedureTableObj.getValues();
//							procedureList.addAll(values);
//							procedureTableObj.removeRow();
//							for (ProcedureDTO procedureDTO : procedureList) {
////								procedureDTO.setProcedureCode(null);
//								procedureTableObj.addBeanToList(procedureDTO);
//							}
//						}
					
					}
				});
			}
	}

	
	public void addIFSCCodeListner()
	{
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
			    Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER);
				viewSearchCriteriaWindow.initView();
				
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
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
				UI.getCurrent().addWindow(popup);
			}
		});
		
	}*/
	
	public boolean validatePagepayment(Boolean true1) {
		
		Boolean hasError = false;
		String eMsg = "";
		if (verificationAccountDeatilsTableObj != null) {
			Set<String> errors = verificationAccountDeatilsTableObj.validateCalculation();
			if (null != errors && !errors.isEmpty()) {
				for (String error : errors) {
					eMsg += error + "</br>";
					hasError = true;
					// break;
				}

			}
		}
			
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
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);

					hasError = true;
					return hasError;
				
				}else{
//					errorMessages.add("Please Add Bill Details");
				}
				return false;
	}
	
	
	public void changeVerifiedButtonValue(Boolean isVerified) {
		if(isVerified){
			 verifyAcntDtlButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			 verifyAcntDtlButton.setEnabled(false);
		 }else{
			 verifyAcntDtlButton.setStyleName(ValoTheme.BUTTON_DANGER);
			 verifyAcntDtlButton.setEnabled(true);
		 }
	}


	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if(this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setupCategoryValues(selectValueContainer);
		}
		
	}
	
	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		if(uploadDocumentListenerTableObj != null) {
			List<UploadDocumentDTO> uploadDoc = uploadDocumentListenerTableObj.getValues();
			List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
				if(null != uploadDocumentDTO.getFileType() && null != uploadDocumentDTO.getFileType().getValue())
				{
					if(uploadDocumentDTO.getFileType().getValue().contains("Bill"))
					{
						/*if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(uploadDTO.getBillNo()))
						{
							uploadList.add(uploadDTO);
						}
						else
						{
							uploadList.add(uploadDocumentDTO);
						}*/
						
						/**
						 * Sequence number is an internal parameter maintained for updating the 
						 * uploadlistener table. This is because the row for which the bill is entered
						 * should only get updated. Rest of rows should  be the same. Earlier this
						 * was done with bill no. But there are chance that even bill no can be duplicate.
						 * Hence removed this and added validation based on seq no.
						 * */
						if(uploadDocumentDTO.getSeqNo().equals(uploadDTO.getSeqNo()))
						{
							//uploadList.add(uploadDTO);
						}
						else
						{
							uploadList.add(uploadDocumentDTO);
						}
						
					}
					else
					{
						uploadList.add(uploadDocumentDTO);
					}
				}
				
			}
			uploadList.add(uploadDTO);
			uploadDocumentListenerTableObj.updateTable(uploadList);
		}
	}
	
	protected void addTotalClaimedListener()
	{
		this.uploadDocumentListenerTableObj.claimedAmtField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != uploadDocumentListenerTableObj)
				{
					String provisionAmt = (String)event.getProperty().getValue();
					if(null != provisionAmt && !("").equalsIgnoreCase(provisionAmt))
					{
						if(SHAUtils.isValidDouble(provisionAmt)) {
							Double value = Double.valueOf(provisionAmt);
//							bean.setAmountConsidered(String.valueOf(value.intValue()));
							if(!isMappingDone) {
								bean = SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
								bean = SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
								bean = SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
								getMappingDone();
							} 
							if(bean.getIsICUoneMapping()) {
								bean = SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
								getMappingDone();
							}
							if(bean.getIsOneMapping()) {
								bean = SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
								getMappingDone();
							}
							if(bean.getIsICUoneMapping()) {
								bean = SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
								getMappingDone();
							}
						}
					}
				}
			}
			
			
		});
		
	}



	public void setIrdaLevel2Values(
			BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue value) {
		if(this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setIrdaLevel2Values(selectValueContainer,cmb,value);
		}
		
	}



	public void setIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		if(this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setIrdaLevel3Values(selectValueContainer,cmb,value);
		}		
	}
	
	public void setBenefitsData(List<AddOnBenefitsDTO> benefitsDTO) {
		// TODO Auto-generated method stub
		this.bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(benefitsDTO);
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
					if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					} else if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsPaymentAvailableShown()) {
						alertMessageForPaymentAvailable();
					} else if(bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
					if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					} else if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsPaymentAvailableShown()) {
						alertMessageForPaymentAvailable();
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						}
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}	
				}
			});
		}
	

		/*public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
			
			
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(dto.getIfscCode());
			txtIfscCode.setReadOnly(true);
			
			txtBankName.setReadOnly(false);
			txtBankName.setValue(dto.getBankName());
			txtBankName.setReadOnly(true);
			
			txtBranch.setReadOnly(false);
			txtBranch.setValue(dto.getBranchName());
			txtBranch.setReadOnly(true);
			
			txtCity.setReadOnly(false);
			txtCity.setValue(dto.getCity());
			txtCity.setReadOnly(true);
			
			if(null != this.bean.getPreauthDataExtractionDetails()){
				this.bean.getPreauthDataExtractionDetails().setBankId(dto.getBankId());
			}
			
		}*/

	public void showPopupForComparison(String comparisonString) {
		 if(comparisonString.isEmpty()) {
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
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>The following Attibutes has been changed from Previous ROD : </b>", ContentMode.HTML), new Label(comparisonString, ContentMode.HTML), hLayout);
				layout.setMargin(true);
				dialog.setContent(layout);
				dialog.show(getUI().getCurrent(), null, true);
		 }
	 }
	
/*	private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
	{
		Policy policy = policyService.getPolicy(this.bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			SelectValue selectValue = new SelectValue();
			SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
			}
			
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getName());
			payeeValueList.add(hospitalName);
		}

		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		payeeNameValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return payeeNameValueContainer;
		
		}
		
		return null;
	}*/
	
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
					
					if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsPaymentAvailableShown()) {
						alertMessageForPaymentAvailable();
					} else if(bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
	

	 public void showWarningMsg(String eMsg){
			
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
	 
		public void getAlertMessage(String eMsg){

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	 public void showAdmissionDateError(){
		   

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
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date should not be greater than Death date</b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);
		   
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
		}
	 
		public Boolean showRoomRentMatchingScreen() {
			
			if(!bean.getIsOneMapping() || !bean.getIsICUoneMapping()) {
				roomRentMatchingTableObj  = roomRentMatchingTable.get();
				final ConfirmDialog dialog = new ConfirmDialog();
				roomRentMatchingTableObj.init(dialog, bean);
				List<RoomRentMatchingDTO> roomRentMappingDTOList = bean.getRoomRentMappingDTOList();
				List<RoomRentMatchingDTO> icuRoomRentMappingDTOList = bean.getIcuRoomRentMappingDTOList();
				if(!bean.getIsOneMapping()) {
					for (RoomRentMatchingDTO roomRentMatchingDTO : roomRentMappingDTOList) {
						roomRentMatchingTableObj.addBeanToList(roomRentMatchingDTO);
					}
				}
				if(!bean.getIsICUoneMapping()) {
					for (RoomRentMatchingDTO roomRentMatchingDTO : icuRoomRentMappingDTOList) {
						roomRentMatchingTableObj.addBeanToList(roomRentMatchingDTO);
					}
				}
			
				dialog.setCaption("Room Rent & Nursing Matching Table.");
				dialog.setClosable(true);
				dialog.setContent(roomRentMatchingTableObj);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				
				roomRentMatchingTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -901753303877184471L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField field =  (TextField) event.getProperty();
						if(SHAUtils.isValidInteger(field.getValue()) && SHAUtils.getIntegerFromString(field.getValue()) == 0) {
							fireViewEvent(PAHealthFinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL_MAPPING, bean);
							isMappingDone = true;
							isMatchTheFollowing = true;
							wizard.next();
						}
					}
				});
			}
			return isMappingDone;
		}
		
		private void getMappingDone() {
			if (bean.getIsOneMapping()) {
				isMappingDone = true;
				if (!bean.getIsICUoneMapping() && !bean.getIsICCUoneMapping()
						&& !bean.getIcuRoomRentMappingDTOList().isEmpty() && !bean.getIccuRoomRentMappingDTOList().isEmpty()) {
					isMappingDone = false;
				}
			}

			if (bean.getIsICUoneMapping()) {
				isMappingDone = true;
				if (!bean.getIsOneMapping() && !bean.getIsICCUoneMapping()
						&& !bean.getRoomRentMappingDTOList().isEmpty()&& !bean.getIccuRoomRentMappingDTOList().isEmpty()) {
					isMappingDone = false;
				}
			}
			
			if (bean.getIsICCUoneMapping()) {
				isMappingDone = true;
				if (!bean.getIsOneMapping() && !bean.getIsICUoneMapping()
						&& !bean.getRoomRentMappingDTOList().isEmpty()&& !bean.getIcuRoomRentMappingDTOList().isEmpty()) {
					isMappingDone = false;
				}
			}

			if (!isMappingDone
					&& (bean.getRoomRentMappingDTOList().isEmpty() && bean
							.getIcuRoomRentMappingDTOList().isEmpty()&& !bean.getIccuRoomRentMappingDTOList().isEmpty())) {
				isMappingDone = true;
			}
		}
		
		public void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne) {
			SHAUtils.fillMappingData(bean, mappingData, isInvokeForOneToOne);
			bean.setIsBack(true);
		}
		
		public void generateButton(Integer clickedButton, Object dropDownValues) {
			this.bean.setStageKey(ReferenceTable.FINANCIAL_STAGE);
			switch (clickedButton) {
			case 1: 
//				
//			
					break;
			 
			case 2:
				
				
				 this.financialButtonObj.buildInitiateFieldVisit(dropDownValues);
				 this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS);
				 break;
				 
			case 3: 
				 this.financialButtonObj.buildInitiateInvestigation(dropDownValues);
				 this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS);
				 break;
			case 4:
				this.financialButtonObj
						.buildReferCoordinatorLayout(dropDownValues);
				 this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS);
				break;
			case 5:
				this.financialButtonObj.buildReferToMedicalApproverLayout();;
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
				break;
			case 6:
				this.financialButtonObj.buildReferToBilling();
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_BILLING);
				break;
				
			case 7:
				this.financialButtonObj.buildQueryLayout();
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_QUERY_STATUS);
				break;
			case 8:
				this.financialButtonObj.buildRejectLayout(dropDownValues);
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REJECT_STATUS);
				break;
			case 9:
				this.financialButtonObj.generateFieldsForApproval();
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
				break;
			case 10:
				this.financialButtonObj.generateCancelRODLayout(dropDownValues);
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_CANCEL_ROD);
				break;
			case 11:
				this.financialButtonObj.buildReferToBillEntryLayout();
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY);
				break;
			default:
				break;
			}
		}



		public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
			// TODO Auto-generated method stub
			uploadDocumentListenerTableObj.loadBillEntryValues(uploadDTO);
			
		}
		
		protected void addEditListener() {
			editBillClassification.addClickListener(new ClickListener() {
				
				private static final long serialVersionUID = -7023902310675004614L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					okBtn.setEnabled(false);
					List<UploadDocumentDTO> values = uploadDocumentListenerTableObj.getValues();
					billClassificationEditUIObj = billClassificationEditUIInstance.get();
					billClassificationEditUIObj.initForEdit(bean, values, uploadDocumentListenerTableObj,okBtn);
					
					VerticalLayout layout = new VerticalLayout(billClassificationEditUIObj, okBtn);
					layout.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);
					
					
					final ConfirmDialog dialog = new ConfirmDialog();
//					dialog.setCaption("Alert");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					okBtn.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							billClassificationEditUIObj.validatePage();
							billClassificationUI.init(bean);
							bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setHospitalCashAddonBenefits(bean.getAddOnBenefitsHospitalCash());
							bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setPatientCareAddOnBenefits(bean.getAddOnBenefitsPatientCare());
							bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setHospitalBenefitFlag(bean.getAddOnBenefitsHospitalCash() ? "HC" : null);
							bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setPatientCareBenefitFlag(bean.getAddOnBenefitsPatientCare() ? "PC" : null);
							Boolean isStarCare = false;
							if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_FLOATER) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_INVIDUAL))) {
								isStarCare = true;
							}
							addOnBenefitsPageObj.init(bean.getPreauthDataExtractionDetails().getUploadDocumentDTO(), SHAConstants.FINANCIAL, isStarCare);
							addOnBenefitsPageObj.getContent();
							
							Button finalOKButton = new Button("OK");
							finalOKButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
							
							Label label = new Label("Bill Classification Changed");
							
							VerticalLayout verti =new VerticalLayout(label,finalOKButton);
							verti.setMargin(true);
							verti.setComponentAlignment(label, Alignment.TOP_CENTER);
							verti.setComponentAlignment(finalOKButton, Alignment.BOTTOM_CENTER);
							final Window popup = new com.vaadin.ui.Window();
							popup.setWidth("20%");
							popup.setHeight("10%");
							popup.setContent(verti);
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
							
							finalOKButton.addClickListener(new ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
										
									popup.close();
									
								}
							});
												
							dialog.close();
							
						}
					});
				}
			});
		}
	 
		public void updateProductBasedAmtDetails(Map<Integer, Object> detailsMap) {
			if(null != detailsMap)
			{
				if(this.uploadDocumentListenerTableObj != null) {
					uploadDocumentListenerTableObj.removeAllItems();
					uploadDocumentListenerTableObj.setReferenceData(referenceData);
					Integer i = 1;
					List<UploadDocumentDTO> uploadList = this.bean.getUploadDocumentDTO();
					uploadDocumentListenerTableObj.setTableInfo(uploadList);
					if(null !=  uploadList && !uploadList.isEmpty())
					for (UploadDocumentDTO uploadDocLayout : uploadList) {
						uploadDocLayout.setProductBasedRoomRent((Double) detailsMap
								.get(8));
						uploadDocLayout
								.setProductBasedICURent((Double) detailsMap.get(9));
						
						uploadDocLayout.setSeqNo(i);
						this.uploadDocumentListenerTableObj.addBeanToList(uploadDocLayout);
						i++;
					}
					//uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
				}
			}
			
		}
		
		public Boolean alertForHospitalDiscount() {
			Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.HOSPITAL_DISCOUNT_ALERT + "</b>",
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
//					dialog.setCaption("Alert");
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
							if(bean.getIsAutoRestorationDone()) {
								alertMessageForAutoRestroation();
							} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
								if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
									alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
								} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
									warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
								}
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							}
						}
					});
					return true;
					
					
		}
	public Boolean alertMessageForPostHosp() {
		Label successLabel = new Label(
			"<b style = 'color: red;'>" + SHAConstants.POST_HOSP_ALERT_MESSAGE + "</b>",
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
//			dialog.setCaption("Alert");
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
					 if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
					 } else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					 } else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							}
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						}

				}
			});
			return true;
		}
	
	
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		// TODO Auto-generated method stub
		sectionDetailsListenerTableObj.setCoverList(coverContainer);
		
	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		
		sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
		
	}
	
	public void buildNomineeLayout() {

		if (bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
				&& bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement().getDocumentReceivedFromId()
						.getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getNewIntimationDTO().getInsuredPatient()
						.getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean
						.getNewIntimationDTO().getInsuredPatient()
						.getRelationshipwithInsuredId().getKey())) {

			if (nomineeDetailsTable != null) {

				wholeVLayout.removeComponent(nomineeDetailsTable);
			}
			if(chkNomineeDeceased != null){
				wholeVLayout.removeComponent(chkNomineeDeceased);
			}
			if (legalHeirLayout != null) {
				wholeVLayout.removeComponent(legalHeirLayout);
			}
			if (financialButtonObj != null) {
				wholeVLayout.removeComponent(financialButtonObj);
			}

			nomineeDetailsTable = nomineeDetailsTableInstance.get();

			nomineeDetailsTable.init("", false, false);
			unbindField(chkNomineeDeceased);
			chkNomineeDeceased = null;
			
			if (bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO()
						.getNomineeList());
				nomineeDetailsTable.generateSelectColumn();
				nomineeDetailsTable.setScreenName(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER);
				nomineeDetailsTable.setPolicySource(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
				viewSearchCriteriaWindow.setPreauthDto(bean);
				nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);
				
				chkNomineeDeceased = (CheckBox) binder.buildAndBind(
						"Nominee Deceased", "isNomineeDeceased",
						CheckBox.class);
			}

			wholeVLayout.addComponent(nomineeDetailsTable);
			
			if(chkNomineeDeceased != null){
				wholeVLayout.addComponent(chkNomineeDeceased);
				addNomineeDeceasedListener();
			}

			boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null
					&& !nomineeDetailsTable.getTableList().isEmpty() ? false
					: true;

			legalHeirLayout = new VerticalLayout();
			
			legalHeirDetails = legalHeirObj.get();
			
			relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			Map<String,Object> refData = new HashMap<String, Object>();
			relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
			refData.put("relationship", relationshipContainer);
			legalHeirDetails.setReferenceData(refData);
			legalHeirDetails.init(bean);
			legalHeirDetails.setPresenterString(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER);
			legalHeirLayout.addComponent(legalHeirDetails);
			wholeVLayout.addComponent(legalHeirLayout);
			
			if(isNomineeDeceased()){
				enableLegalHeir = Boolean.TRUE;
				nomineeDetailsTable.setEnabled(false);
			}
			
			if (enableLegalHeir) {
				
				legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
				legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}

			if (financialButtonObj != null) {
				wholeVLayout.addComponent(financialButtonObj);
			}
		}

	}
	
	public void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty() && !isNomineeDeceased()) {
			
			NomineeDetailsDto nomineeDto = nomineeDetailsTable.getBankDetailsTableObj() != null 
					&& nomineeDetailsTable.getBankDetailsTableObj().getNomineeDto() != null
			   ? nomineeDetailsTable.getBankDetailsTableObj().getNomineeDto()
			   : (viewSearchCriteriaWindow.getNomineeDto() != null ? viewSearchCriteriaWindow.getNomineeDto() : new NomineeDetailsDto());

			nomineeDetailsTable.setNomineeBankDetails(dto, nomineeDto);
		}
		else {
			legalHeirDetails.setBankDetails(dto, viewSearchCriteriaWindow.getLegalHeirDto());
		}
	}	


	public void showInformation(String eMsg) {
			MessageBox.create()
			.withCaptionCust("Information").withHtmlMessage(eMsg.toString())
		    .withOkButton(ButtonOption.caption("OK")).open();
	 }

	  public void generatedFieldsBasedOnDomicillaryHosp(Boolean isChecked) {
			 unbindAndRemoveFromLayoutForDomicillary();
			if(isChecked) {
				unbindAndRemoveFromLayoutForDomicillary();
				treatmentStartDate = (DateField) binder.buildAndBind(
						"Treatment Start Date", "treatmentStartDate", DateField.class);
				
				treatmentEndDate = (DateField) binder.buildAndBind(
						"Treatment End Date", "treatmentEndDate", DateField.class);
				
				
				
				hospitalizationNoOfDays = (TextField) binder.buildAndBind(
						"No of Days", "hospitalizationNoOfDays", TextField.class);
				
				
				treatmentStartDate.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if (treatmentEndDate != null && treatmentEndDate.getValue() != null && treatmentStartDate != null && treatmentStartDate.getValue() != null) {
							if (treatmentStartDate.getValue().after(treatmentEndDate.getValue())) {
								treatmentStartDate.setValue(null);
								showErrorMessage("Treatment Start date cannot be greater than the Treatment End Date");
							}
						}
						
						if(treatmentStartDate != null && treatmentEndDate != null && treatmentStartDate.getValue() != null && treatmentEndDate.getValue() != null) {
							Long noOfDays = SHAUtils.getDaysBetweenDate(treatmentStartDate.getValue(), treatmentEndDate.getValue());
							if(noOfDays < 0l) {
								noOfDays = 0l;
							}
							if(noOfDays <= 3l) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							}
							hospitalizationNoOfDays.setValue(noOfDays.toString());
						}
						
					}
				});
				
				treatmentEndDate.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if (treatmentEndDate != null && treatmentEndDate.getValue() != null && treatmentStartDate != null && treatmentStartDate.getValue() != null) {
							if (treatmentEndDate.getValue().before(treatmentStartDate.getValue())) {
								treatmentEndDate.setValue(null);
								showErrorMessage("Treatment End date cannot be lesser than the Treatment Start Date");

							} else {
								if(treatmentStartDate != null && treatmentEndDate != null && treatmentStartDate.getValue() != null && treatmentEndDate.getValue() != null) {
									Long noOfDays = SHAUtils.getDaysBetweenDate(treatmentStartDate.getValue(), treatmentEndDate.getValue());
									if(noOfDays <= 3l) {
										StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
									}
									hospitalizationNoOfDays.setValue(noOfDays.toString());
								}
							}
						}
					}
				});
				
				int componentIndex = firstFLayout.getComponentIndex(domicillaryHospitalisation);
				firstFLayout.addComponent(treatmentStartDate, componentIndex + 1);
				firstFLayout.addComponent(treatmentEndDate, componentIndex + 2 );
				
				firstFLayout.addComponent(hospitalizationNoOfDays,componentIndex + 3);
			} 
		}
	  
		private void unbindAndRemoveFromLayoutForDomicillary() {
			unbindField(treatmentStartDate);
			unbindField(treatmentEndDate);
			unbindField(hospitalizationNoOfDays);
			if(treatmentStartDate != null && treatmentEndDate != null && hospitalizationNoOfDays != null) {
				firstFLayout.removeComponent(treatmentStartDate);
				firstFLayout.removeComponent(treatmentEndDate);
				firstFLayout.removeComponent(hospitalizationNoOfDays);
			}
		}
		
	private void addNomineeDeceasedListener() {
		chkNomineeDeceased
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {

						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							boolean value = (Boolean) event.getProperty()
									.getValue();

							if (value) {
								if (nomineeDetailsTable != null) {
									nomineeDetailsTable.setEnabled(false);
								}
								if (legalHeirDetails != null) {
									legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);

									if(bean.getLegalHeirDTOList() != null && !bean.getLegalHeirDTOList().isEmpty()){
										legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
									}
									legalHeirDetails.getBtnAdd().setEnabled(true);
								
								}
							} else {
								if (nomineeDetailsTable != null) {
									nomineeDetailsTable.setEnabled(true);
								}
								if (legalHeirDetails != null) {
									legalHeirDetails.deleteRows();
									legalHeirDetails.getBtnAdd().setEnabled(
											false);
								}
							}

						}

					}

				});
	}

	private Boolean isNomineeDeceased() {
		if (chkNomineeDeceased != null && chkNomineeDeceased.getValue() != null && chkNomineeDeceased.getValue()) {
			return true;
		}
		return false;
	}
	
}
