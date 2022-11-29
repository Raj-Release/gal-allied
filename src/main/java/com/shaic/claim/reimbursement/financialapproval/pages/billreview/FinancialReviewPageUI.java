package com.shaic.claim.reimbursement.financialapproval.pages.billreview;

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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.ClaimRemarksAlerts;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.ReportDto;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingTable;
import com.shaic.claim.intimation.create.ViewBasePolicyDetails;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.premedical.listenerTables.DiganosisDetailsListenerForPremedical;
import com.shaic.claim.premedical.listenerTables.ProcedureListenerTableForPremedical;
import com.shaic.claim.reimbursement.billclassification.BillClassificationEditUI;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.AddOnBenefitsDataExtractionPage;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.wizard.PatientCareTable;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialButtonsForFirstPage;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.UpdateHospitalDetails;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.claim.rod.wizard.pages.HospitalCashProductDetailsTable;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewIrdaNonPayablePdfPage;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertDocsDTO;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertUploadDocumentPageUI;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class FinancialReviewPageUI extends ViewComponent{

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	private GWizard wizard;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	@Inject
	private ViewIrdaNonPayablePdfPage pdfPageUI;
	
	@Inject
	private Instance<RoomRentMatchingTable> roomRentMatchingTable;
	
	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;
	
	@Inject
	private Instance<AddOnBenefitsDataExtractionPage> addOnBenifitsPageInstance;
	
	@Inject
	private Instance<PatientCareTable> patientCareTableInstance;
	
	@Inject
	private BillClassificationUI billClassificationUI;
	
	@Inject
	private Instance<BillClassificationEditUI> billClassificationEditUIInstance;
	
	
	private BillClassificationEditUI billClassificationEditUIObj;
	
	@Inject
	private Instance<UpdateHospitalDetails> hospitalDetailsInstance;
	
	@Inject
	private Instance<FinancialButtonsForFirstPage> financialButtonInstance;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	@Inject
	private ViewBasePolicyDetails viewBasePolicyDetail;
 
	@EJB
	private IntimationService intimationService;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;
	
	private FinancialButtonsForFirstPage financialButtonObj;
	
	private UpdateHospitalDetails updateHospitalDetailsObj;
	
	private PatientCareTable patientCareTableObj;
	
	private RoomRentMatchingTable roomRentMatchingTableObj;
	
	private AddOnBenefitsDataExtractionPage addOnBenefitsPageObj;
	
	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;
	
	private DateField admissionDate;
	
	private DateField dischargeDate;
	
	private ComboBox cmbPatientStatus;
	
	//private ComboBox cmbCatastrophicLoss;
	
	private ComboBox cmbNatureOfLoss;
			
	private ComboBox cmbCauseOfLoss;

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

	//private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

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
	
	private StringBuffer alertMessages = new StringBuffer();
	
	private Button btnIFCSSearch;
	
	// Added below fields for Bypass functionality..............
	
	//public TextField txtAdmissionDate ;
	
	public TextField txtDischargeDate;

	private TextField noOfDaysTxt;
	
	private ComboBox cmbRoomCategory;
	
	private OptionGroup ventilatorSupportOption;

	private ComboBox cmbIllness;

	private TextField preauthApprovedAmt;
	
	private TextField changeOfDOA;

	private ComboBox cmbSection;
	
	private Label automaticRestorationTxt;
	
	private Label acutomaticRestorationCount;
	
	private ComboBox cmbSpecifyIllness;
	
	private CheckBox criticalIllnessChk;
	
	private TextField reasonForAdmissionTxt;
	
	private TextField dischargeReason;
	
	private TextField admissionReason;
	
	public Boolean isMappingDone = false;
	
	public Boolean isMatchTheFollowing = false;
	
	private Button editBillClassification;
	
	private VerticalLayout legalHeirLayout;

//	private FormLayout legaHeirLayout;
	
	/*private TextField txtHospitalizationClaimedAmt;
	 
	private TextField txtPreHospitalizationClaimedAmt;
	
	private TextField txtPostHospitalizationClaimedAmt;
	
	private TextField txtOtherBenefitClaimedAmnt;
	
	private OptionGroup optDocumentVerified;*/
	
	

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	private SelectValue existingPayeeName;
	
	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;

	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;

	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	private OptionGroup domicillaryHospitalisation;
	
	private DateField treatmentStartDate;
	
	private DateField treatmentEndDate;
	
	private TextField hospitalizationNoOfDays;
	
	private DiganosisDetailsListenerForPremedical diagnosisDetailsTableObj;
	
	private ProcedureListenerTableForPremedical procedurdTableObj;
	
	@Inject
	private Instance<ClaimsAlertUploadDocumentPageUI> claimsAlertDocsViewUI;
	
	private ClaimsAlertUploadDocumentPageUI claimsAlertDocsView;
	
	private ComboBox caretreatment;
	
	private OptionGroup optPatientDayCare;

	private ComboBox cmbPatientDayCareValue;
	
	private ComboBox cmbHospitalCashDueTo;
	
	@Inject
	private Instance<HospitalCashProductDetailsTable> hospitalCashProductDetailsInstance;
	
	private HospitalCashProductDetailsTable hospitalCashProductDetailsTable;
	
	
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
	
/*	public Boolean alertMessageForPED() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
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
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}*/
	
/*	public Boolean alertMessageForAutoRestroation() {
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
				bean.setIsAutoRestorationDone(true);
				 if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}*/
	
/*	@SuppressWarnings("static-access")
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
				alertMessageForMediPremier(SHAConstants.LUMPSUM_ALERT_MESSAGE);
			}
		});
		return true;
	}*/
	
/*	@SuppressWarnings("static-access")
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
				if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} 
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}*/
	
/*	public Boolean alertMessageForPaymentAvailable() {
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
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}*/
	
	public Component getContent() {
		
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND)) {
			List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
				for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
					if(detailsTableDTO != null && detailsTableDTO.getSublimitName() != null && detailsTableDTO.getSublimitName().getName() != null
									&& detailsTableDTO.getSublimitName().getName().equalsIgnoreCase(SHAConstants.HOSPICE_CARE)){
						SHAUtils.showMessageBoxWithCaption("Waiting period of 12 months from the policy inception is applicable<br>"+"and payable once in a lifetime","Information");
						
					}
				}
			}	
		}
		
		//IMSSUPPOR-36977
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && bean.getNewIntimationDTO().getPolicy().getRenewalPolicyNumber()==null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)){
			getAlertForKavachCoronaAlert();
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)){
			
			getAlertForRelatedOrNotRelatedCovidKavach();
		}
	
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null &&
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)){
			getWarningRakshaAlert();
			getAlertForRelatedOrNotRelatedCovidRakash();
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			getAlertForCoronaAlert();
			getAlertForRelatedOrNotRelatedCovidGrp();
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_GRP_ALERT_MSG, "INFORMATION");
		}

		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		if(bean.getAuditFlag()){
			SHAUtils.showAuditQryAlert();
		}
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		//FSP Alert (CR2019031)
		if(bean.getNewIntimationDTO().getHospitalDto().getFspFlag() != null && 
				bean.getNewIntimationDTO().getHospitalDto().getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.FSP,"Information");
		}

//		//GLX2020017 commented by noufel for cr2019184 which should show common for all applicable products
//		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY)
//				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY)){
//			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
//		}
		//GLX2020017 //added for CR2019184
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& (bean.getNewIntimationDTO().getPolicy().getProduct().getCode()).equals(SHAConstants.PRODUCT_CODE_78)) {
			if(bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed() != null &&
					(bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed()).equals(SHAConstants.YES_FLAG)) {
				
				SHAUtils.showMessageBoxWithCaption("As Buy Back of PED cover is accepted, PED waiting period is <br>" + "reduced from 36 months to 12 months","Information");
				
			}
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& (bean.getNewIntimationDTO().getPolicy().getProduct().getCode()).equals(SHAConstants.PRODUCT_CODE_88)) {
			if(bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed() != null &&
					(bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed()).equals(SHAConstants.YES_FLAG)) {
				
				SHAUtils.showMessageBoxWithCaption("As Buy Back of PED cover is accepted, PED waiting period is <br>" + "reduced from 36 months to 12 months","Information");
				
			}
		}
		//CR2019217
		//changes done for SM agent percentage by noufel on 13-01-2020
				String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
				String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
				if((bean.getIcrAgentValue() != null && !bean.getIcrAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
						|| bean.getSmAgentValue() != null && !bean.getSmAgentValue().isEmpty() 
								&& (Integer.parseInt(bean.getSmAgentValue()) >= Integer.parseInt(smpercentage))){
					SHAUtils.showICRAgentAlert(bean.getIcrAgentValue(), agentpercentage, bean.getSmAgentValue(), smpercentage);
				}
		
		if(this.bean.getIsCoInsurance()){
			showAlertForCoInsurance();
		}
		else if(null != this.bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getTopUpPolicyAlertFlag())){
			showTopUpAlertMessage(this.bean.getTopUpPolicyAlertMessage());
		}
		else if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && this.bean.getDuplicateInsuredList() != null && !this.bean.getDuplicateInsuredList().isEmpty()){
			showDuplicateInsured(this.bean.getDuplicateInsuredList());
		}
		else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
				(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
						bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
			showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
		} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
			showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
		} else{
			displayAlert();
		}
		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
			}
		}
		//CR2019263
		if(bean.getNewIntimationDTO() !=null 
				&& bean.getNewIntimationDTO().getIntimationId() !=null){
			List<Long> catList = new ArrayList<Long>();
			catList.add(SHAConstants.CLAIMS_ALERT_ROD_KEY);
			catList.add(SHAConstants.CLAIMS_ALERT_OTHERS_KEY);
			List<ClaimRemarksAlerts>  claimRemarksAlerts= masterService.getClaimsAlertsByIntitmationCatKey(bean.getNewIntimationDTO().getIntimationId(),catList);

			if(claimRemarksAlerts !=null &&
					!claimRemarksAlerts.isEmpty()){
				for(ClaimRemarksAlerts remarksAlerts:claimRemarksAlerts){
					claimAlertPopUP(remarksAlerts);
				}
			}
		}
		
		//added for CR young star product by noufel on 08-04-2020
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_YOUNG_PRODUCT_CODE) ||
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91))
				&& bean.getPreauthDataExtractionDetails() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null 
				&& bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null
				&& bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.MATERNITY_MASTER_ID)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.ALERT_FOR_MATERNITY_YOUNG_PRODUCT,"Information");
		}
		
		if(bean.getDocumentReceivedFromId().equals(SHAConstants.DOC_RECIEVED_FROM_INSURED_ID))
		{
			DBCalculationService dbCalculationService = new DBCalculationService();
			Long wkKey = 0l;
			if(this.bean.getScreenName().equals("Common for Billing & FA") || this.bean.getScreenName().equals("Common for Billing & FA(Auto Allocation)"))
			{
				wkKey=dbCalculationService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.BILLING_CURRENT_KEY);
			}
			else{
				wkKey=dbCalculationService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.FA_CURRENT_QUEUE);
			}
			//Long wkKey = dbCalculationService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.FA_CURRENT_QUEUE);
			Map<String, Integer> ageingValues = dbCalculationService.getClaimAndActivityAge(wkKey,bean.getClaimDTO().getKey(),(bean.getKey()).toString());
			Integer activityAgeing = ageingValues.get(SHAConstants.LN_ACTIVITY_AGEING);
			Integer claimAgeing = ageingValues.get(SHAConstants.LN_CLAIM_AGEING);
			getAgeingAlertBox(activityAgeing,claimAgeing);
		}
		
		// CR2019257 Base Policy
		if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View Base Policy");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(SHAConstants.BASEPOLICY + "</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			Button BaseViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				
		
			@Override
			public void buttonClick(ClickEvent event) {
				}
				});
				BaseViewButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						
						if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null)
						{
						VerticalLayout verticalLayout = null;
							VerticalLayout vLayout = new VerticalLayout();
							Policy policy = policyService.getByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo());
							Intimation intimation=intimationService.getIntimationByKey(bean.getNewIntimationDTO().getKey());
							if(policy!=null){
							viewBasePolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
									masterService, intimationService);	
							viewBasePolicyDetail.initView();
							UI.getCurrent().addWindow(viewBasePolicyDetail);
						}
						else{
							getErrorMessage("Intimation not available for this Base policy");
						}
							
					}
						else
						{
							getErrorMessage("Base Policy is not available");
						}
						
					}
				});	
		}

		fraudAlert();
		
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 SHAUtils.showPolicyInstalmentAlert();
		 }
		 //code added for CR glx2020167 by noufel
		if(bean.getIsHcTopupPolicyAvail() && bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()){
			SHAUtils.showMessageBox(SHAConstants.ALERT_MSG_FOR_HC_TOPUP_POLICY + bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),"Information");
		}
		
		//GLX2020168
		/*if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
				bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
			popupMessageForWaitingPeriod();
		}*/
		
		PhysicalDocumentVerification isPhyDocPending = intimationService.getReimbursementByKeyForPhysicalVerifcation(bean.getKey());
		if(isPhyDocPending != null && isPhyDocPending.getCommunicationEnableFlag() != null && isPhyDocPending.getCommunicationEnableFlag().equalsIgnoreCase("N")){
			SHAUtils.showAlertMessageBox("Physical Document Not Verified for this ROD");
		}
		initBinder();
		isMappingDone = false;
		isMatchTheFollowing = false;
		isValid = false;
		this.bean.setAlertMessageOpened(false);
		alertMessages = new StringBuffer();
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
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		
		System.out.println(String.format("Ventilator value from bean(Get content) [%s]" , bean.getPreauthDataExtractionDetails().getVentilatorSupport()));
		//System.out.println(String.format("Ventilator value from reimbursement  [%s]" ,reimbursement.getVentilatorSupport()));
		
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
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/

		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		
		caretreatment = (ComboBox) binder.buildAndBind(
				"Type", "homeCareTreatment", ComboBox.class);
		
		/*automaticRestorationTxt = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		automaticRestorationTxt.setEnabled(false);
		
		acutomaticRestorationCount = (TextField) binder.buildAndBind("No Of Times","restorationCount",TextField.class);
		acutomaticRestorationCount.setEnabled(false);*/
		
		
		//Add for new product 076
				cmbHospitalCashDueTo = (ComboBox) binder.buildAndBind("Hospital Cash Due To",
						"hospitalCashDueTo", ComboBox.class);
				
				cmbHospitalCashDueTo.setVisible(false);
				cmbHospitalCashDueTo.setEnabled(false);
				
				optPatientDayCare = (OptionGroup) binder.buildAndBind("Day Care" , "patientDayCare" , OptionGroup.class);

				optPatientDayCare.addItems(getReadioButtonOptions());
				optPatientDayCare.setItemCaption(true, "Yes");
				optPatientDayCare.setItemCaption(false, "No");
				optPatientDayCare.setStyleName("horizontal");

				cmbPatientDayCareValue = (ComboBox) binder.buildAndBind("Day Care Procedure",
						"patientDayCareDueTo", ComboBox.class);
				
				if(bean.getReceiptOfDocumentsDTO() != null && bean.getReceiptOfDocumentsDTO().getDocumentDetails().getPatientDayCareDueTo() != null){
					this.cmbPatientDayCareValue.setValue(bean.getReceiptOfDocumentsDTO().getDocumentDetails().getPatientDayCareDueTo());
				}
				
				//added for new product
				if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					if(optPatientDayCare.getValue() != null && optPatientDayCare.getValue().toString() == "true"){
						optPatientDayCare.select(true);
						cmbPatientDayCareValue.setEnabled(true);
						cmbPatientDayCareValue.setVisible(true);
						if(bean.getReceiptOfDocumentsDTO().getDocumentDetails().getPatientDayCareDueTo() != null){
							cmbPatientDayCareValue.setValue(bean.getReceiptOfDocumentsDTO().getDocumentDetails().getPatientDayCareDueTo());
						}
					}
					else{
						optPatientDayCare.select(false);
						cmbPatientDayCareValue.setVisible(false);
					}
					optPatientDayCare.setVisible(true);

				}
				
		
		automaticRestorationTxt = new Label(bean.getPreauthDataExtractionDetails().getAutoRestoration());
		automaticRestorationTxt.setEnabled(false);
		
		acutomaticRestorationCount = new Label("No Of Times  " + bean.getPreauthDataExtractionDetails().getRestorationCount());
		acutomaticRestorationCount.setEnabled(false);
		
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cmbRoomCategory != null && cmbRoomCategory.getValue() != null) {
					SelectValue sectionValue = (SelectValue) cmbRoomCategory
							.getValue();
					
					bean.getPreauthDataExtractionDetails().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setValChangedRoomCategory(sectionValue);
					uploadDocumentListenerTableObj.setRoomType(sectionValue);
				}
				
				
			}
		});
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
		
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}
			
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
		
		if(null != bean.getNewIntimationDTO().getPolicy() && 
				ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				&& (!ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						&& !ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			criticalIllnessChk.setEnabled(false);
			cmbSpecifyIllness.setEnabled(false);
		}
		
		HorizontalLayout autoRestorationLayout = new HorizontalLayout();
		autoRestorationLayout.addComponents(automaticRestorationTxt,acutomaticRestorationCount);
		autoRestorationLayout.setCaption("Automatic Restoration");
		autoRestorationLayout.setSpacing(true);
		autoRestorationLayout.setSpacing(true);
		
		/*optDocumentVerified = (OptionGroup) binder.buildAndBind("Verified and entered the\namount claimed based on the\noriginal bills received" , "documentVerification" , OptionGroup.class);
		optDocumentVerifiedListener();
		//optDocumentVerified.setRequired(true);
		optDocumentVerified.addItems(getReadioButtonOptions());
		optDocumentVerified.setItemCaption(true, "Yes");
		optDocumentVerified.setItemCaption(false, "No");
		optDocumentVerified.setStyleName("horizontal");
		//Vaadin8-setImmediate() optDocumentVerified.setImmediate(true);
		optDocumentVerified.setEnabled(false);
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
		txtHospitalizationClaimedAmt.addBlurListener(getHospitalLisenter());
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPreHospitalizationClaimedAmt.addBlurListener(getPreHospLisenter());
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n (Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
		txtPostHospitalizationClaimedAmt.addBlurListener(getPostHospLisenter());
		txtOtherBenefitClaimedAmnt = binder.buildAndBind("Amount Claimed (Other Benefits)", "otherBenefitclaimedAmount",TextField.class);
		txtOtherBenefitClaimedAmnt.addBlurListener(getOtherBenefitLisener());
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		txtOtherBenefitClaimedAmnt.setNullRepresentation("");
		txtOtherBenefitClaimedAmnt.setMaxLength(15);*/
		
		//setEnableOrDisableClaimeAmtField();
		
		/*CSValidator hospClaimedAmtValidator = new CSValidator();
		hospClaimedAmtValidator.extend(txtHospitalizationClaimedAmt);
		hospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		hospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator preHospClaimedAmtValidator = new CSValidator();
		preHospClaimedAmtValidator.extend(txtPreHospitalizationClaimedAmt);
		preHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		preHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator postHospClaimedAmtValidator = new CSValidator();
		postHospClaimedAmtValidator.extend(txtPostHospitalizationClaimedAmt);
		postHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		postHospClaimedAmtValidator.setPreventInvalidTyping(true);		
		

		CSValidator otherBenefitClaimedAmtValidator = new CSValidator();
		otherBenefitClaimedAmtValidator.extend(txtOtherBenefitClaimedAmnt);
		otherBenefitClaimedAmtValidator.setRegExp("^[0-9.]*$");
		otherBenefitClaimedAmtValidator.setPreventInvalidTyping(true);*/
		if((this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER) ||
				this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)) || 
				(this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER) ||
						this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL))
				&& (this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))){
		firstFLayout = new FormLayout(admissionDate, cmbRoomCategory, ventilatorSupportOption,illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt, cmbSection/*,optDocumentVerified*/);
		}
		else
		{
			firstFLayout = new FormLayout(admissionDate, cmbRoomCategory,ventilatorSupportOption, illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt, cmbSection,domicillaryHospitalisation/*,optDocumentVerified*/);
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)
						||  bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt, autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss,caretreatment/*,
					txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitClaimedAmnt*/);
		}
		else if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)))
		{
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt, autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss,optPatientDayCare,cmbPatientDayCareValue,cmbHospitalCashDueTo/*,
					txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitClaimedAmnt*/);
		}
		else
		{
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt, autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss/*,
					txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitClaimedAmnt*/);
		}
		
		
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
		
		/*legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Map<String,Object> refData = new HashMap<String, Object>();
		relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		legalHeirDetails.init(bean);
		legalHeirDetails.addBeanToList(bean.getLegalHeirDto().getLegalHeir());
		List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
		list.add(bean.getLegalHeirDto());
//		legalHeirDetails.setTableList(list);
		legalHeirLayout = new VerticalLayout();
		legalHeirLayout.addComponent(legalHeirDetails);*/
		
		
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
		
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			editBillClassification.setEnabled(true);
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
			wholeVLayout = new VerticalLayout(formHLayout, layout,sectionDetailsListenerTableObj, forms,billclasihori,uploadDocumentListenerTableObj, addOnBenefitsPageObj/*,paymentDetailsLayout,legalHeirLayout*/);
			//wholeVLayout = new VerticalLayout(layout, forms,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj, paymentDetailsLayout);
			wholeVLayout.setSpacing(true);
		}else{
			wholeVLayout = new VerticalLayout(formHLayout, layout,sectionDetailsListenerTableObj, forms,billclasihori,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj/*, paymentDetailsLayout,legalHeirLayout*/);
			//wholeVLayout = new VerticalLayout(layout, forms,uploadDocumentListenerTableObj, addOnBenefitsPageObj, updateHospitalDetailsObj, paymentDetailsLayout);
			wholeVLayout.setSpacing(true);
		}
		
		if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null 
				&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
		
			buildNomineeLayout();
		}
		
		addListener();
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(dischargeDate);
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(cmbNatureOfLoss);
		mandatoryFields.add(cmbCauseOfLoss);
		mandatoryFields.add(caretreatment);
		
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			mandatoryFields.add(cmbSection);
		}
				
		if(bean.getIsFinalEnhancement() || (!bean.getHospitalizaionFlag() && !bean.getPartialHospitalizaionFlag())) {
			admissionDate.setEnabled(true);// Making admissionDate field as editable field as per CR R1044.
			dischargeDate.setEnabled(true);// Making dischargeDate field as editable field as per CR R1044.
		}
		admissionDateValue = admissionDate.getValue();
		showOrHideValidation(false);
		txtBillingRemarks.setReadOnly(true);
		txtMedicalRemarks.setReadOnly(true);
		
		
		//Added for product 076
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){

			hospitalCashProductDetailsTable = hospitalCashProductDetailsInstance.get();
			hospitalCashProductDetailsTable.clearTableItems();
			hospitalCashProductDetailsTable.init(bean.getReceiptOfDocumentsDTO());
			wholeVLayout.addComponent(hospitalCashProductDetailsTable);
			
			
			setTableValuesForHospCash();

		}
				
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}
		noOfDaysTxt.setValue(bean.getPreauthDataExtractionDetails().getNoOfDays());
		
		// As per User request added below buttons layout (Refer to Medical and Refer to Billing and Refer to Coordinator)
		 financialButtonObj =  financialButtonInstance.get();
		 financialButtonObj.initView(this.bean, this.wizard);
		 if(financialButtonObj != null){
			 wholeVLayout.removeComponent(financialButtonObj);
		 }
		 wholeVLayout.addComponent(financialButtonObj);
		 
		return wholeVLayout;
	}



	/*private void setEnableOrDisableClaimeAmtField() {
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		txtOtherBenefitClaimedAmnt.setEnabled(false);
		
		if(this.bean.getHospitalizaionFlag() != null && this.bean.getHospitalizaionFlag()){
			txtHospitalizationClaimedAmt.setEnabled(true);
		}
		if(this.bean.getPreHospitalizaionFlag() != null && this.bean.getPreHospitalizaionFlag()){
			txtPreHospitalizationClaimedAmt.setEnabled(true);
		}
		if(this.bean.getPostHospitalizaionFlag() != null && this.bean.getPostHospitalizaionFlag()){
			txtPostHospitalizationClaimedAmt.setEnabled(true);
		}
		if(this.bean.getOtherBenefitsFlag() != null && this.bean.getOtherBenefitsFlag()){
			txtOtherBenefitClaimedAmnt.setEnabled(true);
		}
		if(this.bean.getPartialHospitalizaionFlag() != null && this.bean.getPartialHospitalizaionFlag()){
			txtHospitalizationClaimedAmt.setEnabled(true);
		}
		if(this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat()){
			txtHospitalizationClaimedAmt.setEnabled(true);
		}
	}*/
	
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
	

	private void alertMessageForClaimCount(Long claimCount){/*
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		
//		Button alertIcon = new Button("");
//		alertIcon.setIcon(new ThemeResource("images/alert.png"));
//		alertIcon.setEnabled(false);
//        alertIcon.setStyleName("ling");
		
//		alertIcon.addStyleName(ValoTheme.BUTTON_BORDERLESS);

   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
		
   		if(this.bean.getClaimCount() >2){
	   		successLabel = new Label(
					"<b style = 'color: black;'>"+msg+"<br>"
							+ additionalMessage+"</b>",
					ContentMode.HTML);
   		}
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor,homeButton);
//   		firstForm.setComponentAlignment(mainHor, Alignment.MIDDLE_CENTER);
   		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//   		firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
//		panel.setHeight("143px");
		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
//		popup.setCaption("Alert");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
		popup.setClosable(true);
		
		popup.center();
		popup.setResizable(false);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				popup.close();
				 if(bean.getIsSuspicious() != null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
							
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	*/


		String msg = SHAConstants.CLAIM_COUNT_MESSAGE + claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		Button homeButton=new Button() ;
		
		DBCalculationService dbService = new DBCalculationService();
		int gmcClaimCount = dbService.getGMCClaimCount(bean.getPolicyKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		String gmcMsg = SHAConstants.GMC_CLAIM_COUNT_MESSAGE+gmcClaimCount;

		if(!ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
		if (this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <= 2) {
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(msg, buttonsNamewithType);
			homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		} else if (this.bean.getClaimCount() > 2) {
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCriticalBox(msg+"<br>"+additionalMessage, buttonsNamewithType);
			homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			}
		}
		else 
		{
			if (gmcClaimCount > 1 && gmcClaimCount <= 2) {
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(gmcMsg, buttonsNamewithType);
				homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			} else if (gmcClaimCount > 2) {
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createCriticalBox(gmcMsg+"<br>"+additionalMessage, buttonsNamewithType);
				homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			}
		}

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//popup.close();
				 if(bean.getIsSuspicious() != null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
							
			}
		});
		
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
 /*  		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createInformationBox(SHAConstants.PED_WATCHLIST, buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
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
	
	/*private List<Field<?>> getListOfPaymentFields()
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
	}*/
	
/*	private void getPaymentDetailsLayout()
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
				if(null != this.bean.getPreauthDataExtractionDetails().getRoomCategory()) {
					uploadDocLayout.setRoomCategory(this.bean.getPreauthDataExtractionDetails().getRoomCategory());
				}
				if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey()) {
					uploadDocLayout.setDocREceivedFromId(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
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
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		BeanItemContainer<SelectValue> treatcareHealthcorona = (BeanItemContainer<SelectValue>) referenceData
				.get("treatcareHealthcorona");
		
		BeanItemContainer<SelectValue> roomCategory = (BeanItemContainer<SelectValue>) referenceData
				.get("roomCategory");
		
		BeanItemContainer<SelectValue> illness = (BeanItemContainer<SelectValue>) referenceData
				.get("illness");
		
		BeanItemContainer<SelectValue> criticalIllness = (BeanItemContainer<SelectValue>) referenceData
				.get("criticalIllness");
		
		BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
				.get("section");
		
		BeanItemContainer<SelectValue> patientDayCareValue = (BeanItemContainer<SelectValue>) referenceData
				.get("patientDayCareValueContainer");
		
		BeanItemContainer<SelectValue> hospitalCashDueToValue = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalCashDueTo");

		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		this.bean.getPolicyDto().setClaimKey(this.bean.getClaimKey());
		
		if(cmbSection != null && cmbSection.getValue() != null){
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails().setSection(sectionValue);
		}
		
		

		cmbPatientStatus.setContainerDataSource(patientStatus);
		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientStatus.setItemCaptionPropertyId("value");
		
		/*cmbCatastrophicLoss.setContainerDataSource(catastrophicLoss);  
		cmbCatastrophicLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCatastrophicLoss.setItemCaptionPropertyId("value");*/
		
		cmbNatureOfLoss.setContainerDataSource(natureOfLoss);  
		cmbNatureOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfLoss.setItemCaptionPropertyId("value");
		
		cmbCauseOfLoss.setContainerDataSource(causeOfLoss);  
		cmbCauseOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfLoss.setItemCaptionPropertyId("value");
		
		caretreatment.setContainerDataSource(treatcareHealthcorona);  
		caretreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		caretreatment.setItemCaptionPropertyId("value");
		
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
		
		cmbPatientDayCareValue.setContainerDataSource(patientDayCareValue);
		cmbPatientDayCareValue.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientDayCareValue.setItemCaptionPropertyId("value");
		
        cmbHospitalCashDueTo.setContainerDataSource(hospitalCashDueToValue);
        cmbHospitalCashDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cmbHospitalCashDueTo.setItemCaptionPropertyId("value");
		
//		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
//			this.cmbSection.setValue(this.bean
//					.getPreauthDataExtractionDetails().getSection());
//		}
		
		List<SelectValue> itemIds = section.getItemIds();
		
		/*if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss() != null) {
			
			this.cmbCatastrophicLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCatastrophicLoss());
		}*/
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getNatureOfLoss() != null) {
			this.cmbNatureOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getNatureOfLoss());
		}
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			this.cmbCauseOfLoss.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfLoss());
		}
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getHomeCareTreatment() != null) {
			this.caretreatment.setValue(this.bean.getPreauthDataExtractionDetails().getHomeCareTreatment());
		}

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
			System.out.println(String.format("Ventilator support value inside set up references [%s]",this.bean.getPreauthDataExtractionDetails().getVentilatorSupport() ));
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
		
		if(this.bean.getPreauthDataExtractionDetails().getPatientDayCareDueTo() != null){
			if(null !=cmbPatientDayCareValue && null == cmbPatientDayCareValue.getValue()){
				cmbPatientDayCareValue.setValue(bean.getPreauthDataExtractionDetails().getPatientDayCareDueTo());
			}
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
		StringBuffer eMsg = new StringBuffer();		
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
		
		if(this.cmbNatureOfLoss != null && this.cmbNatureOfLoss.getValue() == null) {
			hasError = true;
			eMsg.append("Nature Of Loss is Mandatory. </br>");
		}
		
		if(this.cmbCauseOfLoss != null && this.cmbCauseOfLoss.getValue() == null) {
			hasError = true;
			eMsg.append("Cause Of Loss is Mandatory. </br>");
		}
		
		/*if(this.caretreatment != null && this.caretreatment.getValue() == null) {
			hasError = true;
			eMsg.append("Home Care Treatment  is Mandatory. </br>");
		}*/
		
		if(this.addOnBenefitsPageObj != null && !this.addOnBenefitsPageObj.validatePage()) {
			List<String> errors = this.addOnBenefitsPageObj.getErrors();
			hasError = true;
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			 }
		}
		
		/*if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			if(daysBetweenDate >= 0 && SHAUtils.getIntegerFromString(noOfDaysTxt.getValue()) > daysBetweenDate.intValue()) {
				hasError = true;
				eMsg.append("No of days should be DOD-DOA+1 </br>"); 
			}
		}*/
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg.append("Please Select Ventilator Support. </br>");
		}
		
		if(noOfDaysTxt != null && noOfDaysTxt.getValue() == null || (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter the Length of Stay. </br>");
		}
		
		
		List<UploadDocumentDTO> values = uploadDocumentListenerTableObj.getValues();
		
		if(!bean.getIsFirstPageSubmit()){
			for (UploadDocumentDTO uploadDocumentDTO : values) {
				if(uploadDocumentDTO.getFileType() != null && uploadDocumentDTO.getFileType().getValue().contains("Bill")){
					if (!uploadDocumentDTO.getStatus()) {
						hasError = true;
						eMsg.append("Please review the Bill Entry details</br>");
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
						eMsg.append(error).append("</br>");
					}
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		
		if(null != bean && null != bean.getPreHospitalizaionFlag() && bean.getPreHospitalizaionFlag() &&
				null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() && 
				null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() &&
				(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))
		{
			hasError = true;
			eMsg.append("Please Select Refer to bill Entry </br>");
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
		
			if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {					
			
			if(null != this.cmbSection && (null == this.cmbSection.getValue() || ("").equalsIgnoreCase(this.cmbSection.getValue().toString())))
			
				{
					hasError = true;
					eMsg.append("Please Select Section. </br>");
					
				}
		}


			SelectValue patientStatusSelect = cmbPatientStatus.getValue() != null ? (SelectValue)cmbPatientStatus.getValue() : null;
			if(patientStatusSelect != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatusSelect.getId()) 
							|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatusSelect.getId()))
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

				if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
					List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
				
					if(tableList != null && !tableList.isEmpty()){
						bean.getNewIntimationDTO().setNomineeList(tableList);
						StringBuffer nomineeNames = new StringBuffer("");
						int selectCnt = 0;
						for (NomineeDetailsDto nomineeDetailsDto : tableList) {
							nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
							if(nomineeDetailsDto.isSelectedNominee()) {
//								nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
								nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
							    selectCnt++;	
							}

							/**
							Acc. Preference disable for Nominee hence commented below code as per Mr. Satish Sir on 14-12-2019
							**/
							/*if(nomineeDetailsDto.getPreference() == null || nomineeDetailsDto.getPreference().isEmpty()) {
								eMsg.append("Please Select Account Preference for Nominee</br>");
								hasError = true;
								break;
							}*/
							
						}
						
						bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
						if(selectCnt>0){
							bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
							bean.getNewIntimationDTO().setNomineeAddr(null);
//							hasError = false;
						}
						else{
							bean.getNewIntimationDTO().setNomineeName(null);
							
							eMsg.append("Please Select Nominee</br>");
							hasError = true;						
						}
						
						if(selectCnt > 0) {
							String payableAtValidation = nomineeDetailsTable.validatePayableAtForSelectedNominee();
							if(!payableAtValidation.isEmpty()){
								eMsg.append(payableAtValidation +"<br>");
								hasError = true;
							}
							
							String ifscValidation = nomineeDetailsTable.validateIFSCForSelectedNominee();
							if(!ifscValidation.isEmpty()){
								eMsg.append(ifscValidation +"<br>");
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
						eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)</br>");
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
									eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %,Account Preference)");
								}
						}}
					 }
				}					
			}
			
			/*if((null != this.txtHospitalizationClaimedAmt && 
					null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())) ||
					((null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() &&
					!("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue()))) ||
					((null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() &&
					!("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue()))) ||
					((null != this.txtOtherBenefitClaimedAmnt && null != this.txtOtherBenefitClaimedAmnt.getValue() && 
					!("").equalsIgnoreCase(this.txtOtherBenefitClaimedAmnt.getValue()))))
			{
			
				if(null != this.optDocumentVerified && null == this.optDocumentVerified.getValue()){
				
					hasError = true;
					eMsg.append("Please select document verified option</br>");
				}
			//}
			
			if(this.bean.getHospitalizaionFlag() != null && this.bean.getHospitalizaionFlag()){
				if(this.txtHospitalizationClaimedAmt != null && (this.txtHospitalizationClaimedAmt.getValue() == null) || (this.txtHospitalizationClaimedAmt.getValue() != null &&
						this.txtHospitalizationClaimedAmt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Hospitalization Claimed Amount</br>");
				}
			}
			
			if(this.bean.getPreHospitalizaionFlag() != null && this.bean.getPreHospitalizaionFlag()){
				if(this.txtPreHospitalizationClaimedAmt != null && (this.txtPreHospitalizationClaimedAmt.getValue() == null) || (this.txtPreHospitalizationClaimedAmt.getValue() != null &&
						this.txtPreHospitalizationClaimedAmt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Pre-Hospitalization Claimed Amount</br>");
				}
			}
			
			if(this.bean.getPostHospitalizaionFlag() != null && this.bean.getPostHospitalizaionFlag()){
				if(this.txtPostHospitalizationClaimedAmt != null && (this.txtPostHospitalizationClaimedAmt.getValue() == null) || (this.txtPostHospitalizationClaimedAmt.getValue() != null &&
						this.txtPostHospitalizationClaimedAmt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Post-Hospitalization Claimed Amount</br>");
				}
			}
			
			if(this.bean.getOtherBenefitsFlag() != null && this.bean.getOtherBenefitsFlag()){
				if(this.txtOtherBenefitClaimedAmnt != null && (this.txtOtherBenefitClaimedAmnt.getValue() == null) || (this.txtOtherBenefitClaimedAmnt.getValue() != null &&
						this.txtOtherBenefitClaimedAmnt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Other Beneift Claimed Amount</br>");
				}
			}
			
			if(this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat()){
				if(this.txtHospitalizationClaimedAmt != null && (this.txtHospitalizationClaimedAmt.getValue() == null) || (this.txtHospitalizationClaimedAmt.getValue() != null &&
						this.txtHospitalizationClaimedAmt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Hospitalization Claimed Amount</br>");
				}
			}
			
			if(this.bean.getPartialHospitalizaionFlag() != null && this.bean.getPartialHospitalizaionFlag()){
				if(this.txtHospitalizationClaimedAmt != null && (this.txtHospitalizationClaimedAmt.getValue() == null) || (this.txtHospitalizationClaimedAmt.getValue() != null &&
						this.txtHospitalizationClaimedAmt.getValue().isEmpty())){
					hasError = true;
					eMsg.append("Please Enter Hospitalization Claimed Amount</br>");
				}
			}*/
			
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& (bean.getNewIntimationDTO()
						.getPolicy()
						.getProduct()
						.getCode()
						.equalsIgnoreCase(
								ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE) 
								||  (bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE) 
										&& !(bean.getAddOnBenefitsHospitalCash())))
				&& null != this.caretreatment
				&& null == this.caretreatment.getValue()) {
			hasError = true;
			eMsg.append("Please Select Type. </br>");
		}
			
			if(optPatientDayCare != null && optPatientDayCare.getValue() != null  && this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
				if( optPatientDayCare.getValue().toString() == "true") {
					if(cmbPatientDayCareValue == null || cmbPatientDayCareValue.isEmpty()){
						hasError = true;
						eMsg.append("Please select Patient Day Care Value");
					}
				}
			}
			
			if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) &&  
					hospitalCashProductDetailsTable != null ){
				
				List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
				Long totalNoOfDays =0l;
				Long perDayAmount =0l;
				Long noOfDaysAllowed =0l;
				for (HopsitalCashBenefitDTO hospitalCashBenefit1 : hospitalCashBenefit){
					if(hospitalCashBenefit1.getHospitalCashDays() != null && !hospitalCashBenefit1.getHospitalCashDays().equals("")){
						totalNoOfDays = totalNoOfDays + Long.valueOf(hospitalCashBenefit1.getHospitalCashDays());
					}
					if(hospitalCashBenefit1.getHospitalCashPerDayAmt() != null && !hospitalCashBenefit1.getHospitalCashPerDayAmt().equals("")){
						perDayAmount = perDayAmount + Long.valueOf(hospitalCashBenefit1.getHospitalCashPerDayAmt());
					}
					if(hospitalCashBenefit1.getNoOfDaysAllowed() != null && !hospitalCashBenefit1.getNoOfDaysAllowed().equals("")){
						noOfDaysAllowed = noOfDaysAllowed + Long.valueOf(hospitalCashBenefit1.getNoOfDaysAllowed());
					}
				}
				if(totalNoOfDays == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Claimed table Values.</br> ");
				}
				if(perDayAmount==0){
					hasError = true;
					eMsg.append("Please enter any one Per Day Claimed Amount table Values.</br> ");
				}
				if(noOfDaysAllowed == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Allowed table Values .</br>");
				}
			}
			//added for admission date validation by noufel
			if(admissionDate != null){
				Date dateOfAdmissionValue = admissionDate.getValue() != null ? admissionDate.getValue() : null;
				Date policyFrmDate = bean.getPolicyDto().getPolicyFromDate();
				Date policyToDate = bean.getPolicyDto().getPolicyToDate();
				if((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
						|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){

					policyFrmDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() : bean.getPolicyDto().getPolicyFromDate());
					policyToDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() : bean.getPolicyDto().getPolicyToDate());
					
					if(bean.getNewIntimationDTO().getPolicy().getSectionCode() != null && bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getNewIntimationDTO().getGmcMainMember() != null){
						policyFrmDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() : bean.getPolicyDto().getPolicyFromDate());
						policyToDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() : bean.getPolicyDto().getPolicyToDate());		
					}
				}
				if (null != dateOfAdmissionValue && null != policyFrmDate 
						&& null != policyToDate) {
					//if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
					if (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFrmDate,policyToDate,dateOfAdmissionValue)) {
						hasError = true;
						eMsg.append("Admission Date is not in range between Policy From Date and Policy To Date.");

					}	
				}
			}
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);

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
					fireViewEvent(FinancialReviewPagePresenter.COMPARE_WITH_PREVIOUS_ROD, bean);
				}
				if(this.patientCareTableObj != null) {
					this.bean.getPreauthDataExtractionDetails().setPatientCareDTO(this.patientCareTableObj.getValues());
				}
				
				
                 if(null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO()){
					
					if(null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareAddOnBenefits() && 
							this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getPatientCareAddOnBenefits())
					{
						fireViewEvent(FinancialReviewPagePresenter.SAVE_PATIENT_CARE_TABLE_VALUES, this.bean);
					}
					if (null != this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getHospitalCashAddonBenefits() && 
							this.bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().getHospitalCashAddonBenefits())
					{
						fireViewEvent(FinancialReviewPagePresenter.SAVE_HOSPITAL_CASH_TABLE_VALUES, this.bean);
					}
					
					if (null != this.bean.getPreauthDataExtractionDetails()
							.getUploadDocumentDTO()
							.getOtherBenefit()) {
						fireViewEvent(
								FinancialReviewPagePresenter.SAVE_OTHER_BENEFITS_TABLE_VALUES,
								this.bean);
					}
				}
                 
                 if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
                		 || ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
                		 || /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
	                		 (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
								&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
										|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
								|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
									&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
											SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
					  if(this.bean.getIsEditBillClassification()){
						  fireViewEvent(
									FinancialReviewPagePresenter.GET_BENEFIT_DETAILS,
									this.bean);
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

	      	       	    		alertMessages.append("No of Inpatient Beds should be above 15 !!! </br>");
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
         		
				
				if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null
						&& ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
								|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))*/
								(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
										|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
								|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
									&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
											SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
					
					if(null != bean && bean.getPreauthDataExtractionDetails() != null &&
							null != bean.getPreauthDataExtractionDetails().getCauseOfInjury() && 
							null != bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId() &&
									null != bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() && 
		                            null != bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId())
					{								
						if((ReferenceTable.ROAD_TRAFFIC_ACCIDENT).equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()) &&
                                bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID))
						
						{
							fireViewEvent(FinancialReviewPagePresenter.GET_RTA_BALANCE_SI, bean);
						}
					}
				}
				if(uploadDocumentListenerTableObj !=null && uploadDocumentListenerTableObj.getTotalClaimedAmount() !=null){
					bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(uploadDocumentListenerTableObj.getTotalClaimedAmount().longValue());
				}
				
				if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
				
					if (null != bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCash() && 
							bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCash())
					{
//						fireViewEvent(FinancialReviewPagePresenter.SAVE_FA_HOSPITAL_CASH_TABLE_VALUES, this.bean.getReceiptOfDocumentsDTO());
					}
					if ((null != bean.getReceiptOfDocumentsDTO().getDocumentDetails()
							.getHospitalCash() && (bean.getReceiptOfDocumentsDTO()
									.getDocumentDetails().getHospitalCash()))) {
						fireViewEvent(FinancialReviewPagePresenter.SAVE_FA_HOSPITAL_CASH_PHC_TABLE_VALUES, this.bean.getReceiptOfDocumentsDTO());
					}
					
				}
				
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
		
		//Add for product 076
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			if(null != this.bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCashFlag()))
			{
				List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
				this.bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO().setHopsitalCashBenefitDTO(hospitalCashBenefit);
			}


		}
	}
	
	public Boolean alertMessage() {
		

	       	    	/*Label successLabel = new Label("<b style = 'color: red;'> Number of Inpatient Beds should be above 15 !!!</b>", ContentMode.HTML);
	       	    	
	       	 		// Label noteLabel = new
	       	 		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
	       	 		// ContentMode.HTML);

	       	 		Button homeButton = new Button("ok");
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
//	       	 		dialog.setCaption("Alert");
	       	 		dialog.setClosable(false);
	       	 		dialog.setContent(layout);
	       	 		dialog.setResizable(false);
	       	 		dialog.setModal(true);
	       	 		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Number of Inpatient Beds should be above 15 !!!", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

	       	 		homeButton.addClickListener(new ClickListener() {
	       	 			private static final long serialVersionUID = 7396240433865727954L;

	       	 			@Override
	       	 			public void buttonClick(ClickEvent event) {
	       	 				
	       	 			    isValid = true;
	       	 			    bean.setAlertMessageOpened(true);
	       	 			    wizard.next();
	       	 			   // dialog.close();

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
		
		criticalIllnessChk
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean checkValue = criticalIllnessChk.getValue();
				fireViewEvent(
						FinancialReviewPagePresenter.CHECK_CRITICAL_ILLNESS,
						checkValue, true);
			}
		});

		cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						FinancialReviewPagePresenter.BILLING_PATIENT_STATUS_CHANGED,
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
					if((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
							|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
							|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
							|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){

						policyFromDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() : bean.getPolicyDto().getPolicyFromDate());
						policyToDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() : bean.getPolicyDto().getPolicyToDate());
						
						if(bean.getNewIntimationDTO().getPolicy().getSectionCode() != null && bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getNewIntimationDTO().getGmcMainMember() != null){
							policyFromDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() : bean.getPolicyDto().getPolicyFromDate());
							policyToDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() : bean.getPolicyDto().getPolicyToDate());		
						}
					}
					Long claimKey = bean.getClaimKey();
					boolean isRODDateCheck = reimbursementQueryService.rodAdmissionDateCompare(claimKey, admissionDate.getValue());
					
					/*final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);*/
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					
					
					if(isRODDateCheck){/*
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
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date should not be before the First ROD Date.</b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						event.getProperty().setValue(null);
					*/
						HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
								createErrorBox("Admission Date should not be before the First ROD Date.", buttonsNamewithType);
						Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						isValid = false;
						
						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								//dialog.close();
							}
						});
						
						
					}else if (!isRODDateCheck && (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFromDate,policyToDate,enteredDate))
							/*!(enteredDate.after(policyFromDate) || enteredDate.compareTo(policyFromDate) == 0) || !(enteredDate.before(policyToDate)  || enteredDate.compareTo(policyToDate) == 0)*/) {/*
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
					*/

						 isValid = false;
						 HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
									createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
							Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
							homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;

							@Override
							public void buttonClick(ClickEvent event) {
								
							}
						});
						
						event.getProperty().setValue(null);
						
					}
					if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE) 
									&& bean.getNewIntimationDTO().getPolicy().getRenewalPolicyNumber()==null)
							|| (bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)))){
						Long alertDte =null;
						Date policyStartDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
						Date changeDT = (Date) event.getProperty().getValue();
						alertDte = SHAUtils.getDiffDays(policyStartDate, changeDT);
						Long totalAlertDte = alertDte + 1;
						if(totalAlertDte < 15){
							bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
						}else{
							bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(false);
						}
					}
					if( bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
									|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
						getAlertForCoronaAlert();
						
						 if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE) &&
						    		bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
							 Long alertDte =null;
							 Long totalAlertDte = null;
						    	if(bean.getPreauthDataExtractionDetails().getAdmissionDate() != null
						    			&& dischargeDateValue != null){
						    		Date changeDT = (Date) event.getProperty().getValue();
							    	
							    	alertDte = SHAUtils.getDiffDays(changeDT,dischargeDateValue);
							        totalAlertDte = alertDte + 1;
							        if(totalAlertDte != null && totalAlertDte <= 3){
							        	System.out.println(totalAlertDte+ "Approval diabled" );
							        	bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
							        }else{
							        	System.out.println(totalAlertDte+ "Approval Enable" );
							        	bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(false);
							        }
						    	}
						    }
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
		
		hospitalAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(FinancialReviewPagePresenter.BILLING_HOSPITAL_BENEFITS, isChecked);
			}
		});
		
		patientCareAddOnBenefits.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				fireViewEvent(FinancialReviewPagePresenter.BILLING_PATIENT_CARE_BENEFITS, isChecked);
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
						FinancialReviewPagePresenter.FA_UPDATE_PRODUCT_BASED_AMT,
						bean);

			}
		});
		cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cmbRoomCategory != null && cmbRoomCategory.getValue() != null) {
					SelectValue sectionValue = (SelectValue) cmbRoomCategory
							.getValue();
					bean.getPreauthDataExtractionDetails().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setValChangedRoomCategory(sectionValue);
					
					if(sectionValue.getValue() != null && sectionValue.getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")) {
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
					
				}
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
				fireViewEvent(FinancialReviewPagePresenter.GENERATE_DOMICILLARY_FIELDS, isChecked);
			}
		});
		
		this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				String value = property.getValue();
				if(value != null ) {
					// Other than hosptialization section is selected then domicillary should get disabled....
					if(!value.equalsIgnoreCase(ReferenceTable.HOSP_SUB_COVER_CODE) && !value.equalsIgnoreCase(ReferenceTable.ASSISTED_REPRODUCTION_SUB_COVER_CODE)) {
						if(domicillaryHospitalisation.getValue() != null && domicillaryHospitalisation.getValue().toString().equalsIgnoreCase("true")) {
							domicillaryHospitalisation.setValue(false);
						}
						domicillaryHospitalisation.setEnabled(false);
					} else {
						domicillaryHospitalisation.setEnabled(StarCommonUtils.shouldDisableDomicillary(bean));
					}
					fireViewEvent(FinancialReviewPagePresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisDetailsTableObj != null && !diagnosisDetailsTableObj.getValues().isEmpty()) {
						diagnosisDetailsTableObj.changeSublimitValues();
					} 
					if(procedurdTableObj != null && !procedurdTableObj.getValues().isEmpty()) {
						procedurdTableObj.changeSublimitValues();
					}
				}
			}
		});
		
		optPatientDayCare
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if (event.getProperty() != null
						&& event.getProperty().getValue().toString() == "true") {
					cmbPatientDayCareValue.setVisible(true);
					cmbPatientDayCareValue.setRequired(true);
					isChecked = true;
				}else if(event.getProperty() != null
						&& event.getProperty().getValue().toString() == "false"){
					cmbPatientDayCareValue.setVisible(false);
					cmbPatientDayCareValue.setRequired(false);
					cmbPatientDayCareValue.setValue(null);
					
				}
			}
		});
		
		caretreatment.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && caretreatment.getValue().toString().toLowerCase()
						.contains("home care treatment")) {

					if(ReferenceTable.STAR_GRP_COVID_PROD_CODE.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) &&
							(bean.getAddOnBenefitsHospitalCash())){

						StarCommonUtils.alertMessage(getUI(), "Hospital Cash is not allowed for Home care treatment");
						mandatoryFields.remove(caretreatment);
						caretreatment.setValue(null);
					}
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
					if(legalHeirLayout != null) {
						wholeVLayout.removeComponent(legalHeirLayout);
					}
				}	

			} else {

				if(nomineeDetailsTable != null) { 
					wholeVLayout.removeComponent(nomineeDetailsTable);
				}
				if(legalHeirLayout != null) {
					wholeVLayout.removeComponent(legalHeirLayout);
				}
				removePatientStatusGeneratedFields();
			}

		} else {
			removePatientStatusGeneratedFields();
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
		
/*		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);
		
//	           Button okButton = new Button("OK");
//				 okButton.addClickListener(new ClickListener() {
//					private static final long serialVersionUID = -7148801292961705660L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						dialog.close();
//					}
//				});
//				HorizontalLayout hLayout = new HorizontalLayout(okButton);
//				hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
//				hLayout.setMargin(true);
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Discharge date should not less than Admission Date</b>", ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				dialog.setContent(layout);
				dialog.setCaption("Error");
				dialog.setClosable(true);
				dialog.show(getUI().getCurrent(), null, true);*/
	
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	GalaxyAlertBox.createErrorBox("Discharge date should not less than Admission Date", buttonsNamewithType);

	}

public void getErrorMessage(String eMsg){
	
	/*Label label = new Label(eMsg, ContentMode.HTML);
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
	dialog.show(getUI().getCurrent(), null, true);*/
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
    }
	
 private void showWarningErrors(){
			
		/*	final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setResizable(false);
//			 Button okButton = new Button("OK");
//			 okButton.addClickListener(new ClickListener() {
//				private static final long serialVersionUID = -7148801292961705660L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					dialog.close();
//				}
//			});
//			HorizontalLayout hLayout = new HorizontalLayout(okButton);
//			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
//			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);*/
	 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Please select valid death date. Death date is not in range between admission date and current date.", buttonsNamewithType);
			
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
		
//		payeenameListner();
		
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
		
		if(null != this.bean.getPreauthDataExtractionDetails().getAccountNo())
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
		
		FormLayout bankTransferLayout2 = new FormLayout(dField,btnIFCSSearch);
		FormLayout bankTransferLayout3 = new FormLayout(dField1,dField2,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2,bankTransferLayout3);
		hLayout.setSpacing(false);//,bankTransferLayout3);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
//		hLayout.setWidth("80%");
		
		return hLayout;
	}
	
	public void payeenameListner(){
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
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.FINANCIAL_APPROVER);
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
							if(bean.getIsICCUoneMapping()) {
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
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			this.bean.getReceiptOfDocumentsDTO().setAddOnBenefitsDTO(benefitsDTO);
		}else{
			this.bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(benefitsDTO);
		}
		
	}
	
	public String poupMessageForProduct() {
		StringBuilder productMsg = new StringBuilder();
		Map<String, String> popupMap = bean.getPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			productMsg.append(entry.getValue());
			productMsg.append(entry.getKey());
		}
		return productMsg.toString();
		
		/*final ConfirmDialog dialog = new ConfirmDialog();
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
				dialog.close();
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
		dialog.show(getUI().getCurrent(), null, true);*/
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
		 } else {/*
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
		 */
			 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox("The following Attibutes has been changed from Previous ROD :", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						bean.setIsComparisonDone(true);
						wizard.next();
						//dialog.close();
					}
				});
		
		 	 
		 }
	 }
	
	/*private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
	{
		Policy policy = policyService.getPolicy(this.bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
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
				selectValue = new SelectValue();
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
		
		
	
			SelectValue hospitalPayableAt = new SelectValue();
			hospitalPayableAt.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalPayableAt.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getHospitalPayableAt());
			payeeValueList.add(hospitalPayableAt);
		
		
		
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
	
/*	 public void suspiousPopupMessage() {
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
					if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					if(bean.getIsSuspicious() != null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getSuspiciousPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				Label label = new Label(entry.getValue(), ContentMode.HTML);
				label.setWidth(null);
			   layout.addComponent(label);
			   layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			this.bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}*/
	

	 public void showWarningMsg(String eMsg){
			
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
			
		}
	 
	 public MessageBox showAlertMessageBox(String message){
			final MessageBox msgBox = MessageBox
				    .createWarning()
				    .withCaptionCust("Warning")
				    .withHtmlMessage(message)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			
			return msgBox;
	      }

	 
	 public void showAdmissionDateError(){
	/*		final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setResizable(false);
//			 Button okButton = new Button("OK");
//			 okButton.addClickListener(new ClickListener() {
//				private static final long serialVersionUID = -7148801292961705660L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					dialog.close();
//				}
//			});
//			HorizontalLayout hLayout = new HorizontalLayout(okButton);
//			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
//			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Admission Date should not be greater than Death date</b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);*/
		 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Admission Date should not be greater than Death date", buttonsNamewithType);
		   
	   }
	 
	 private void showErrorMessage(String eMsg) {
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
							fireViewEvent(FinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL_MAPPING, bean);
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
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					this.financialButtonObj.buildRejectLayout(dropDownValues,true);
					
				}else{
					BeanItemContainer<SelectValue> setlRejCateg = masterService.getSettledRejectionCategory();
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId() != null && 
							bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey() != null && 
							ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey())){
						dropDownValues = setlRejCateg;
					}
					
					if(bean.getHospAmountAlreadyPaid() != null && bean.getHospAmountAlreadyPaid().intValue() != 0){
						this.financialButtonObj.buildRejectLayout(dropDownValues,false);
					}
					else{
						this.financialButtonObj.buildRejectLayout(dropDownValues,false);
					}	
				}
				
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
		
		protected void addEditListener() {/*
			editBillClassification.addClickListener(new ClickListener() {
				
				private static final long serialVersionUID = -7023902310675004614L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					okBtn.setEnabled(false);
					List<UploadDocumentDTO> values = uploadDocumentListenerTableObj.getValues();
					billClassificationEditUIObj = billClassificationEditUIInstance.get();
					billClassificationEditUIObj.initForEdit(bean, values, uploadDocumentListenerTableObj);
					
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
							bean.setIsEditBillClassification(true);
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
							
							//setEnableOrDisableClaimeAmtField();
							
							setOtherBenefitsValueToDTO(bean);
							
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
		*/

			editBillClassification.addClickListener(new ClickListener() {
				
				private static final long serialVersionUID = -7023902310675004614L;

				@Override
				public void buttonClick(ClickEvent event) {
				
					List<UploadDocumentDTO> values = uploadDocumentListenerTableObj.getValues();
					billClassificationEditUIObj = billClassificationEditUIInstance.get();
					
					
					VerticalLayout layout = new VerticalLayout(billClassificationEditUIObj);
			
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
					Button okBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
					okBtn.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							if(billClassificationEditUIObj.validatePage()){
							billClassificationUI.init(bean);
							bean.setIsEditBillClassification(true);
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
							setOtherBenefitsValueToDTO(bean);
							
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
						}
							
						}
					});
					
					billClassificationEditUIObj.initForEdit(bean, values, uploadDocumentListenerTableObj,okBtn);
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
						
						Product product = this.bean.getNewIntimationDTO().getPolicy().getProduct();
						
						if(product != null &&
								(product.getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
										|| product.getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))) {
							uploadDocLayout
							.setProductBasedAmbulanceAmt((Double) detailsMap
									.get(10));
						}
						if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
							uploadDocLayout
							.setProductBasedRoomRent(0d);
								uploadDocLayout
										.setProductBasedICURent(0d);
						}

						
						uploadDocLayout.setSeqNo(i);
						this.uploadDocumentListenerTableObj.addBeanToList(uploadDocLayout);
						i++;
					}
					//uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
				}
			}
			
		}
		
/*		public Boolean alertForHospitalDiscount() {
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
					layout.setStyleName("borderLayout");
					HorizontalLayout hLayout = new HorizontalLayout(layout);
					hLayout.setMargin(true);
					hLayout.setStyleName("borderLayout");

					final ConfirmDialog dialog = new ConfirmDialog();
//					dialog.setCaption("Alert");
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
							if(bean.getIsAutoRestorationDone()) {
								alertMessageForAutoRestroation();
							}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {

								if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
									alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
								} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
									warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
								}
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							}else if(bean.getIs64VBChequeStatusAlert()){
								get64VbChequeStatusAlert();
							} else if(bean.getIsSuspicious()!=null){
								StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}
						}
					});
					return true;
		}*/
		
/*	public Boolean alertMessageForPostHosp() {
		Label successLabel = new Label(
			"<b style = 'color: red;'>" + SHAConstants.POST_HOSP_ALERT_MESSAGE + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
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
//			dialog.setCaption("Alert");
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
						}else if(bean.getIs64VBChequeStatusAlert()){
							get64VbChequeStatusAlert();
					 }else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}

				}
			});
			return true;
		}*/
	
	
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		// TODO Auto-generated method stub
		sectionDetailsListenerTableObj.setCoverList(coverContainer);
		
	}
	
/*	public void get64VbChequeStatusAlert() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.VB64STATUSALERT + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
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
				 bean.setIsPopupMessageOpened(true);

//					bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
			}
		});
	}*/

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		
		sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
	}

	public void editSpecifyVisible(Boolean checkValue) {
		if (checkValue) {
			cmbSpecifyIllness.setReadOnly(false);
		} else {
			//cmbSpecifyIllness.setValue("");
			cmbSpecifyIllness.setValue(null);
			cmbSpecifyIllness.setNullSelectionAllowed(true);
			cmbSpecifyIllness.setReadOnly(true);
		}
	}
	
	public void setClearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);
		if(wholeVLayout != null){
			wholeVLayout.removeAllComponents();
		}
	}
/*	public void nonPreferredPopupMessage() {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3535135712222253226L;
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				dialog.close();		
				if(bean.getIsSuspicious() != null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});

		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getNonPreferredPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setWidth("30%");
		this.bean.setIsPopupMessageOpened(true);
		dialog.show(getUI().getCurrent(), null, true);
	}*/

	protected void showPopup(final String message) {/*

		 String message1 = message.replaceAll("(.{200})", "$1<br />");
		 message1 = message1.replaceAll("(\r\n|\n)", "<br />");

		Label successLabel = new Label(
				"<b style = 'color: red;'>" +   message1 + "</br>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		//layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		layout.setHeightUndefined();
		if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
			layout.setHeight("100%");
			layout.setWidth("100%");
			}
			
		}			
		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
	
		
		if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
				dialog.setWidth("55%");
			}
		}
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	dialog.close();
					 	  if(bean.getClmPrcsInstruction()!=null && !bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
					 		showPopup(bean.getClmPrcsInstruction());
					 	}
				}
			});
   
	*/


		 String message1 = message.replaceAll("(.{200})", "$1<br />");
		 message1 = message1.replaceAll("(\r\n|\n)", "<br />");
		 
		 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(message1, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	
					 	  if(bean.getClmPrcsInstruction()!=null && !bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
					 		showPopup(bean.getClmPrcsInstruction());
					 	}
				}
			});
  
		
	}
	
	
	public void confirmRejectionLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject){
		financialButtonObj.buildRejectLayout(rejectionCategoryDropdownValues, isDefinedLimitReject);
	}
	
/*	 public void policyValidationPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
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
//			dialog.setCaption("Alert");
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
					if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
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
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
		}*/
	 
	 
	 
	 
/*	 public void zuaQueryAlertPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.ZUA_QUERY_ALERT + "</b>",
					ContentMode.HTML);
	   		
		 	Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button zuaViewButton = new Button("View ZUA History");
			zuaViewButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
			HorizontalLayout btnLayout = new HorizontalLayout(homeButton , zuaViewButton);
			btnLayout.setSpacing(true);
			VerticalLayout layout = new VerticalLayout(successLabel, btnLayout);
			layout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
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
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
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
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
			
			
			zuaViewButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues = SHAUtils.setViewTopZUAQueryHistoryTableValues(
							bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
					

					List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues = SHAUtils.setViewZUAQueryHistoryTableValues(
							bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
					
					zuaTopViewQueryTable.init(" ",false,false);
					zuaTopViewQueryTable.initTable();						
					
					zuaViewQueryHistoryTable.init(" ", false, false); 
					
					
					zuaViewQueryHistoryTable.setTableList(setViewZUAQueryHistoryTableValues);
					zuaTopViewQueryTable.setTableList(setViewTopZUAQueryHistoryTableValues);
					VerticalLayout verticalTopZUALayout = new VerticalLayout();
					VerticalLayout verticalZUALayout = new VerticalLayout();
					
					verticalTopZUALayout.addComponent(zuaTopViewQueryTable);
					verticalZUALayout.addComponent(zuaViewQueryHistoryTable);
					
					VerticalLayout verticalLayout = new VerticalLayout();
			    	verticalLayout.addComponents(verticalTopZUALayout,verticalZUALayout);
			    	verticalLayout.setComponentAlignment(verticalTopZUALayout,Alignment.TOP_CENTER );
					
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
						*//**
					 * 
					 *//*
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {								
							//dialog.close();
							System.out.println("Close listener called");
						}
					});
					
					
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			
					
				}
			});
		}*/
	 
	/* public BlurListener getHospitalLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
						
							if(null != bean.getPreauthDataExtractionDetails().getHospClaimedAmountDocRec() && null != txtHospitalizationClaimedAmt && null != txtHospitalizationClaimedAmt.getValue() &&
									!(SHAUtils.getIntegerFromStringWithComma(txtHospitalizationClaimedAmt.getValue()).equals(bean.getPreauthDataExtractionDetails().getHospClaimedAmountDocRec()))){
								
								//fireViewEvent(FinancialWizardPresenter.CLAIMED_AMNT_ALERT,property);
								claimedAmntValidation(property);
								
							}
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
							bean.getPreauthDataExtractionDetails().setHospitalizationClaimedAmount(SHAUtils.getIntegerFromStringWithComma(txtHospitalizationClaimedAmt.getValue()));
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
						
					}
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getPreHospLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
						
							if(null != bean.getPreauthDataExtractionDetails().getPreHospClaimedAmountDocRec() && null != txtPreHospitalizationClaimedAmt && null != txtPreHospitalizationClaimedAmt.getValue() &&
									!(SHAUtils.getIntegerFromStringWithComma(txtPreHospitalizationClaimedAmt.getValue()).equals(bean.getPreauthDataExtractionDetails().getPreHospClaimedAmountDocRec()))){
								
								
								//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
								claimedAmntValidation(property);
							}
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
							bean.getPreauthDataExtractionDetails().setPreHospitalizationClaimedAmount(SHAUtils.getIntegerFromStringWithComma(txtPreHospitalizationClaimedAmt.getValue()));
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
					}
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getPostHospLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
						
							if(null != bean.getPreauthDataExtractionDetails().getPostHospClaimedAmountDocRec()&& null != txtPostHospitalizationClaimedAmt && null != txtPostHospitalizationClaimedAmt.getValue() &&
									!(SHAUtils.getIntegerFromStringWithComma(txtPostHospitalizationClaimedAmt.getValue()).equals(bean.getPreauthDataExtractionDetails().getPostHospClaimedAmountDocRec()))){
								//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
								claimedAmntValidation(property);
								
							}
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
							bean.getPreauthDataExtractionDetails().setPostHospitalizationClaimedAmount(SHAUtils.getIntegerFromStringWithComma(txtPostHospitalizationClaimedAmt.getValue()));
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
					}
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getOtherBenefitLisener() {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
						
							if(null != bean.getPreauthDataExtractionDetails().getOtherBenefitsAmountDocRec() && null != txtOtherBenefitClaimedAmnt && null != txtOtherBenefitClaimedAmnt.getValue() &&
									!(SHAUtils.getIntegerFromStringWithComma(txtOtherBenefitClaimedAmnt.getValue()).equals(bean.getPreauthDataExtractionDetails().getOtherBenefitsAmountDocRec()))){
								//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
								claimedAmntValidation(property);
								
							}
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
							bean.getPreauthDataExtractionDetails().setOtherBenefitclaimedAmount(SHAUtils.getIntegerFromStringWithComma(txtOtherBenefitClaimedAmnt.getValue()));
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
					}
				}
			};
			return listener;
			
		}
	  
	  private void optDocumentVerifiedListener()
		{
			optDocumentVerified.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					Boolean value = (Boolean) event.getProperty().getValue();
					String message = null;
					if(null != value){
						
					if(!value)
					{
						message = "Please verify the original bills before proceeding";
						showErrorMessage(message);
						bean.getPreauthDataExtractionDetails().setDocumentVerificationFlag(SHAConstants.N_FLAG);
						optDocumentVerified.setValue(null);
					}
					
				}
				}

			});
		}*/
	  
	  public void claimedAmntValidation(final TextField textField){/*
		 
			Label successLabel = new Label("<b style = 'color: red;'> " + "Amount Claimed - Amount entered is not matching with the amount </br><center>entered at the time of ROD </center></br><center>\nDo you wish to proceed?</center></b>", ContentMode.HTML);
			
			Button yesButton = new Button("Yes");
			yesButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button noButton = new Button("No");
			noButton.setStyleName(ValoTheme.BUTTON_DANGER);
			HorizontalLayout horizontalLayout = new HorizontalLayout(yesButton,noButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
		//	horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_CENTER);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(false);
			layout.setStyleName("borderLayout");
			layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();					
					
				}
			});
			
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					textField.setValue(null);
					dialog.close();					
					
				}
			});
		
	  */
		  HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Amount Claimed - Amount entered is not matching with the amount </br><center>entered at the time of ROD </center></br><center>\nDo you wish to proceed?</center></b>", buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
			 
			
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
				}
			});
			
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					textField.setValue(null);
									
					
				}
			});
		
	    
	  }
	  
		private void setOtherBenefitsValueToDTO(PreauthDTO bean)
		{
			if(null != bean){			
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setOtherBenefit(bean.getOtherBenefitsFlag());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setEmergencyMedicalEvaluation(bean.getEmergencyMedicalEvaluation());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setRepatriationOfMortalRemains(bean.getRepatriationOfMortalRemains());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setCompassionateTravel(bean.getCompassionateTravel());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setPreferredNetworkHospital(bean.getPreferredNetworkHospital());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setSharedAccomodation(bean.getSharedAccomodation());
			}
		}
		
/*		private void showICRMessage(){
			String msg = SHAConstants.ICR_MESSAGE;
			Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
			firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			firstForm.setSpacing(true);
			firstForm.setMargin(true);
			firstForm.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(firstForm);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setWidth("20%");
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					dialog.close();

					if(bean.getIsPolicyValidate()){
						policyValidationPopupMessage();
					}else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
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
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}

				}
			});
		}*/
		
		
		private void showAlertMessage(VerticalLayout errLayout  /*String argMsg*/){
//			Label successLabel = new Label("<div style = 'text-align:left;'><b style = 'color: red;'>"+argMsg+"</b></div>", ContentMode.HTML);

	     /*	Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//			VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
//			firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//			firstForm.setSpacing(true);
//			firstForm.setMargin(true);
//			firstForm.setStyleName("borderLayout");
			
			errLayout.addComponent(homeButton);
			errLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			errLayout.setSpacing(true);
			errLayout.setMargin(true);
			errLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
//			dialog.setContent(firstForm);
			dialog.setContent(errLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("Information", errLayout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					//dialog.close();					
					if(bean.getClaimCount() > 1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(bean.getIsSuspicious() != null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
		}
		
		public void displayAlert(){
			String LINE_BRK = "</br>";
			List<String> alertList = new ArrayList<String>();
			//String get64vbStatus = PremiaService.getInstance().get64VBStatus(bean.getPolicyDto().getPolicyNumber());
			/*if(null != this.bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getTopUpPolicyAlertFlag())){
				alertList.add(this.bean.getTopUpPolicyAlertMessage()+LINE_BRK);
			}*/
			DBCalculationService dbService = new DBCalculationService();
			String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
			if(null != memberType && !memberType.isEmpty()){
				alertList.add(SHAConstants.CMD_ALERT_LATEST + memberType + " Club" +LINE_BRK);
			}
			if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
				alertList.add("Investigation has been initiated and report is pending"+LINE_BRK);
			}
			if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
				if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
					alertList.add("Investigation assigned to "+ bean.getPreauthDataExtractionDetails().getInvestigatorsCount() + " investigators. Reply received from "+ bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount() + " investigator"+LINE_BRK);
				}
			}			
			if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				alertList.add(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName() + " Category Hospital"+ LINE_BRK);
			}
			if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
				
				
				alertList.add("Claim processed outside the system for the insured-"+ bean.getNewIntimationDTO().getInsuredPatient().getInsuredName() + " Verify  and restirict sum insured and other benefits  before proceeding "+LINE_BRK);
			}
						
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(bean.getPolicyDto().getPolicyNumber(), bean.getNewIntimationDTO().getIntimationId());
			if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
				alertList.add("Cheque Status is Pending"+LINE_BRK);
			}
			
			if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
				alertList.add("Cheque Status is Due"+LINE_BRK);
			}
			
			if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
				//showICRMessage();			
				alertList.add(SHAConstants.ICR_MESSAGE+LINE_BRK);
			}
			if(bean.getIsPolicyValidate()){	
				//policyValidationPopupMessage();
				alertList.add(SHAConstants.POLICY_VALIDATION_ALERT+LINE_BRK);
			}
			if(bean.getIsZUAQueryAvailable()){
				//zuaQueryAlertPopupMessage();
				alertList.add(SHAConstants.ZUA_QUERY_ALERT+LINE_BRK);
			}
			if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
				alertList.add(poupMessageForProduct()+LINE_BRK);
			}
			if(bean.getIsPEDInitiated()) {
				//alertMessageForPED();
//				alertList.add(SHAConstants.PED_RAISE_MESSAGE+LINE_BRK);
				
				if(bean.isInsuredDeleted()){
					alertList.add(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()) + LINE_BRK);
				}
				if(bean.isMultiplePEDAvailableNotDeleted()){
					alertList.add(SHAConstants.PED_RAISE_MESSAGE + LINE_BRK);
				}
				
			}
			
			if(bean.getIsPaymentAvailableShown()) {
				//alertMessageForPaymentAvailable();
				alertList.add(SHAConstants.PAYMENT_AVAIL_MESSAGE+LINE_BRK);
			} 
			if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && (SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
				//alertMessageForPostHosp();
				alertList.add(SHAConstants.POST_HOSP_ALERT_MESSAGE+LINE_BRK);
			} 
			if(bean.getIsHospitalDiscountApplicable()){
				//alertForHospitalDiscount();
				alertList.add(SHAConstants.HOSPITAL_DISCOUNT_ALERT+LINE_BRK);
			} 
			if(bean.getIsAutoRestorationDone()) {
				//alertMessageForAutoRestroation();
				alertList.add(SHAConstants.AUTO_RESTORATION_MESSAGE+LINE_BRK);
			} 
			if(ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
				if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
					//alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					alertList.add(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE+LINE_BRK);
				} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
						&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
					//warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					alertList.add(SHAConstants.LUMPSUM_WARNING_MESSAGE+LINE_BRK);
				} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
						&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
					//warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					alertList.add(SHAConstants.CORONA_WARNING_MESSAGE+LINE_BRK);
				}else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					//StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					alertList.add(SHAConstants.DOMICILLARY_ALERT_MESSAGE+LINE_BRK);
				}
			} 
			if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
				//StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				alertList.add(SHAConstants.DOMICILLARY_ALERT_MESSAGE+LINE_BRK);
			} 
			if(bean.getIs64VBChequeStatusAlert()){
				//get64VbChequeStatusAlert();
				alertList.add(SHAConstants.VB64STATUSALERT+LINE_BRK);
			}	
			if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					 && (bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
					bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
					&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				Date admissionDate = bean.getPolicyDto().getAdmissionDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
				if(is15daysWaitingPeriodApplicable(bean)){
					alertList.add(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG+LINE_BRK);
				}
				else{
					if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					alertList.add(SHAConstants.THIRTY_DAYS_WAITING_ALERT+LINE_BRK);
					}
				}
				
			}
			if(!bean.getSuspiciousPopupMap().isEmpty()){
				Map<String, String> popupMap = bean.getSuspiciousPopupMap();
				for (Map.Entry<String, String> entry : popupMap.entrySet()) {
					//alertList.add(entry.getValue()+LINE_BRK); //kk
					alertList.add(entry.getKey()+LINE_BRK);
				}
			}
			if(!bean.getNonPreferredPopupMap().isEmpty()){
				Map<String, String> popupMap = bean.getNonPreferredPopupMap();
				for (Map.Entry<String, String> entry : popupMap.entrySet()) {
					//alertList.add(entry.getValue()+LINE_BRK);//kk
					alertList.add(entry.getKey()+LINE_BRK);
				}
			}
			 
			
			
			if(!alertList.isEmpty()){
				StringBuilder consolidatedMsg = new StringBuilder("");
				int i = 0;
				for(i=0; i<alertList.size(); i++){
					consolidatedMsg.append((i+1) + ". " + alertList.get(i));
					if((SHAConstants.PED_RAISE_MESSAGE+LINE_BRK).equalsIgnoreCase(alertList.get(i)))
						break;	
				}
				Label lbl1 = getFormatedLabel(consolidatedMsg.toString());
				VerticalLayout errorLayout = new VerticalLayout(lbl1);
				
				if(i < alertList.size() && (SHAConstants.PED_RAISE_MESSAGE+LINE_BRK).equalsIgnoreCase(alertList.get(i))){
				
					pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
					pedRaiseDetailsTableObj.init("", false, false);
					pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
					
					errorLayout.addComponent(pedRaiseDetailsTableObj.getTable());
										
					consolidatedMsg = new StringBuilder("");

					for(int j = i+1; j < alertList.size(); j++){
						consolidatedMsg.append((j+1) + ". " + alertList.get(j));
					}
				
					Label lbl2 = getFormatedLabel(consolidatedMsg.toString());
					errorLayout.addComponent(lbl2);
					errorLayout.setWidth("100%");
				}
				
				showAlertMessage(errorLayout /*consolidatedMsg.toString()*/);
			}else if(bean.getClaimCount() > 1){
				alertMessageForClaimCount(bean.getClaimCount());
			}else if(bean.getIsSuspicious() != null){
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}
		}

		public Label getFormatedLabel(String consolidatedMsg){
			
			//Label errorLbl = new Label("<div style = 'text-align:left;'><b style = 'color: red;'>" + consolidatedMsg + "</b></div>", ContentMode.HTML);
			Label errorLbl = new Label(consolidatedMsg, ContentMode.HTML);
			
			
			return errorLbl;
						
		}
		public void alertMsgForCancelRod(String screenName) {
					 
					String message = "";
					if(null != screenName && SHAConstants.MEDICAL.equalsIgnoreCase(screenName)){
						message = SHAConstants.ALERT_FOR_CANCEL_ROD;
					}
					else if(null != screenName && SHAConstants.FINANCIAL.equalsIgnoreCase(screenName))
					{
						message = SHAConstants.FA_ALERT_FOR_CANCEL_ROD;
					}
					
					/* Label successLabel = new Label(
								"<b style = 'color: red;'>" + message + "</b>",
								ContentMode.HTML);
				   		//final Boolean isClicked = false;
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
						dialog.show(getUI().getCurrent(), null, true);*/
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createAlertBox(message, buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								//dialog.close();
							}
						});
					}
			
		public void alertMsgForInvsPending(final String decisionStatus) {
					
					 /*Label successLabel = new Label(
								"<b style = 'color: red;'>" + SHAConstants.INV_PENDING_ALERT_FA + "</b>",
								ContentMode.HTML);
				   		//final Boolean isClicked = false;
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
						dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox(SHAConstants.INV_PENDING_ALERT_FA, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());

						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								if(null != decisionStatus && decisionStatus.equalsIgnoreCase(SHAConstants.REJECT)){
									generateButton(8,masterService.getReimbRejCategoryByValue());
								}
								//dialog.close();
							}
						});
					}
	 public void showTopUpAlertMessage(String remarks) {/*	 
					 
						Label successLabel = new Label(
						"<b style = 'color: red;'>" + remarks + "</b>",
						ContentMode.HTML);
				TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(remarks);
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				
				txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
				VerticalLayout layout = new VerticalLayout(txtArea, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setHeight(layout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setResizable(false);
				dialog.setModal(true);		
				dialog.show(getUI().getCurrent(), null, true);
				txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
				VerticalLayout layout = new VerticalLayout(txtArea);
				layout.setSpacing(true);
				layout.setMargin(true);
			
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
				

					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();
							if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && bean.getDuplicateInsuredList().isEmpty()){
								showDuplicateInsured(bean.getDuplicateInsuredList());
							}else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
									(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
											bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
								showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
							} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
								showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
							}else{
								displayAlert();
							}
						}
					});

			*/
		 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(remarks, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
						(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
								bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
					showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
				} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
					showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
				}else{
					displayAlert();
				}
			}
		});

	 
	 }

				public void showDuplicateInsured(List<SelectValue> duplicateInsured) {/*
					//batchCpuCountTable.init("Count For Cpu Wise", false, false);
					//batchCpuCountTable.setTableList(tableDTOList);
					Table table = new Table();
					table.setHeight("200px");
					table.setWidth("200px");
					table.addContainerProperty("Insured Name", String.class, null);
					table.addContainerProperty("Health Card Number",  String.class, null);
					table.setPageLength(10);
					table.setSizeFull();
					table.setHeight("140%");
					int i = 0;
					for (SelectValue selectValue : duplicateInsured) {
						table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
					}
					
					Button homeButton = new Button("OK");
					homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					
					VerticalLayout layout = new VerticalLayout(table, homeButton);
					layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					layout.setSizeFull();
					//layout.setStyleName("borderLayout");
					
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("<b style = 'color: red;'>Duplicate Insured Details !!!</b>");
					popup.setCaptionAsHtml(true);
					popup.setWidth("30%");
					popup.setHeight("35%");
					popup.setContent(layout);
					popup.setClosable(false);
					popup.center();
					popup.setResizable(false);
					homeButton.setData(popup);
					
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							Window box = (Window)event.getButton().getData();
							box.close();
							if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
									(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
											bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
								showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
							} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
								showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
							}else{
								displayAlert();
							}
							
						}
					});

					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
					// TODO Auto-generated method stub
					
				*/
					Table table = new Table();
					table.addContainerProperty("Insured Name", String.class, null);
					table.addContainerProperty("Health Card Number",  String.class, null);
					table.setPageLength(10);
					table.setSizeFull();
					table.setHeight("140%");
					int i = 0;
					for (SelectValue selectValue : duplicateInsured) {
						table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
					}
					
					VerticalLayout layout=new VerticalLayout();
					Label successLabel = new Label("Duplicate Insured Details !!!");
					
					layout.addComponent(successLabel);
					layout.addComponent(table);
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
					
					
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							Window box = (Window)event.getButton().getData();
							box.close();
							if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
									(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
											bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
								showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
							} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
								showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
							}else{
								displayAlert();
							}
							
						}
					});

				}
				
				 public void showAlertForGMCParentLink(String policyNumber){	 
					 
				/*	    Label successLabel = new Label(
								"<b style = 'color: red;'>Policy is  Linked to Policy No " + policyNumber + "</b>",
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
						dialog.show(getUI().getCurrent(), null, true);*/
					 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createInformationBox("Policy is  Linked to Policy No " + policyNumber, buttonsNamewithType);
						Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								//dialog.close();
								if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
									showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
								} else {
									displayAlert();
								}
								
							}
						});
					}



				 public void showAlertForGMCPaymentParty(String paymentParty,String proposerName,String mainMemberName){/*	 
						Label successLabel =null;
						if(paymentParty != null && paymentParty.equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_CORPORATE)){
						     successLabel = new Label(
									"<b style = 'color: red;'>Payment Party is Corporate - Payment to be made to " + proposerName + "</b>",
									ContentMode.HTML);
						} else {
							 successLabel = new Label(
										"<b style = 'color: red;'>Payment Party is Main Member - Payment to be made to " + mainMemberName + "</b>",
										ContentMode.HTML);
						}
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
					 
						Label successLabel =null;
						if(paymentParty != null && paymentParty.equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_CORPORATE)){
						     successLabel = new Label("Payment Party is Corporate - Payment to be made to " + proposerName + "</b>",
									ContentMode.HTML);
						} else {
							 successLabel = new Label("Payment Party is Main Member - Payment to be made to " + mainMemberName + "</b>",
										ContentMode.HTML);
						}
						
						VerticalLayout layout = new VerticalLayout(successLabel);
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
						Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								// Below method is missing in PROD Branch below method added in Alert Refactory
								displayAlert();
						
							}
						});
						 
				 }

				 public void popupMessageFor30DaysWaitingPeriod() {
						Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
						Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
						Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
					    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
					    if((diffDays != null && diffDays < 30)){/*
					    	 Label successLabel = new Label(
					    				"<b style = 'color: red;'>" + SHAConstants.THIRTY_DAYS_WAITING_ALERT + "</b>",
					    				ContentMode.HTML);
					    			//final Boolean isClicked = false;
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
//					    		dialog.setCaption("Alert");
					    		dialog.setClosable(false);
					    		dialog.setContent(layout);
					    		dialog.setResizable(false);
					    		dialog.setModal(true);
					    		dialog.show(getUI().getCurrent(), null, true);

					    		homeButton.addClickListener(new ClickListener() {
					    			private static final long serialVersionUID = 7396240433865727954L;

					    			@Override
					    			public void buttonClick(ClickEvent event) {
					    				// bean.setIsPopupMessageOpened(true);
					    				 
					   					bean.setIsPopupMessageOpened(true);
					    					dialog.close();
					    					if(bean.getIsSuspicious() != null){
					    						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					    					}
					    	}
						});
					    */

					    	 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
										.createInformationBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT, buttonsNamewithType);
								Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

					    		homeButton.addClickListener(new ClickListener() {
					    			private static final long serialVersionUID = 7396240433865727954L;

					    			@Override
					    			public void buttonClick(ClickEvent event) {
					    				// bean.setIsPopupMessageOpened(true);
					    				 
					   					bean.setIsPopupMessageOpened(true);
					    					if(bean.getIsSuspicious() != null){
					    						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					    					}
					    	}
						});
					    	
					    }
					}
				 
				//Covid 19 GLX2020086
					public void popupMessageForWaitingPeriod() {

				 		Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				 		Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
				 		String productCode=bean.getNewIntimationDTO().getPolicy().getProduct().getCode();
				 		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
				 		/*Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("01-07-2020");
				 		Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("30-09-2020");*/
				 		//GLX2020168
				 		Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("09-12-2020");
						Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("31-03-2021");
				 		Date currnetDate = new Date();

				 		if((diffDays != null && diffDays < 30)
				 				/*&&(ReferenceTable.getCovidSpecifiedProducts().contains(productCode))*/
				 				&&ReferenceTable.getCovidProducts().contains(productCode)
				 				&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, policyFromDate))
				 				&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, currnetDate))){
				 			final MessageBox showAlert = showAlertMessageBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG);
				 			Button homeButton = showAlert.getButton(ButtonType.OK);


				 			homeButton.addClickListener(new ClickListener() {
				 				private static final long serialVersionUID = 7396240433865727954L;

				 				@Override
				 				public void buttonClick(ClickEvent event) {

				 					bean.setIsPopupMessageOpened(true);
				 					showAlert.close();
				 				}
				 			});
				 		}else{
				 			popupMessageFor30DaysWaitingPeriod();
				 		}

				 	}
					
					public Boolean is15daysWaitingPeriodApplicable(PreauthDTO preAuthDto){

							Date policyFromDate = preAuthDto.getNewIntimationDTO().getPolicy().getPolicyFromDate();
							Date admissionDate  = preAuthDto.getPreauthDataExtractionDetails().getAdmissionDate();
							String productCode  = preAuthDto.getNewIntimationDTO().getPolicy().getProduct().getCode();
							Long diffDays       = SHAUtils.getDiffDays(policyFromDate, admissionDate);
							/*Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("01-07-2020");
							Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("30-09-2020");*/
							Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("09-12-2020");
							Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("31-03-2021");
							Date currnetDate = new Date();

							Boolean is15DaysWaitingPeriod = false;

							if((diffDays != null && diffDays < 30)
									/*&&(ReferenceTable.getCovidSpecifiedProducts().contains(productCode))*/
									&& (ReferenceTable.getCovidProducts().contains(productCode))
									&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, policyFromDate))
									&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, currnetDate))){

								if(!this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()&& this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() !=null){
					 				List<DiagnosisDetailsTableDTO> diagnosisList =this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();

									for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {

										if(diagnosisDetailsTableDTO.getIcdCode()!=null && diagnosisDetailsTableDTO.getIcdCode().getValue()!=null){

											String insuranceDiagnosisCode = diagnosisDetailsTableDTO.getIcdCode().getValue();
											System.out.println("insuranceDiagnosisCode.."+insuranceDiagnosisCode);

											if(ReferenceTable.getCovidInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
												is15DaysWaitingPeriod= true;
											}
										}
									}
								}
								
							}
							
							return is15DaysWaitingPeriod;
							

						}
					
					

				 
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
	
		financialButtonObj.setSubCategContainer(rejSubcategContainer);
			
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
				
				treatmentStartDate.addValueChangeListener(new ValueChangeListener() {
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
				
				treatmentEndDate.addValueChangeListener(new ValueChangeListener() {
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
				firstFLayout.addComponent(hospitalizationNoOfDays, componentIndex + 3 );
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


			if (legalHeirLayout != null) {
				wholeVLayout.removeComponent(legalHeirLayout);
			}
			if (financialButtonObj != null) {
				wholeVLayout.removeComponent(financialButtonObj);
			}

			nomineeDetailsTable = nomineeDetailsTableInstance.get();

			nomineeDetailsTable.init("", false, false);

			if (bean.getNewIntimationDTO().getNomineeList() != null) {
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO()
						.getNomineeList());
				nomineeDetailsTable.generateSelectColumn();
				nomineeDetailsTable.setScreenName(SHAConstants.FINANCIAL_APPROVER);
				nomineeDetailsTable.setPolicySource(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
				viewSearchCriteriaWindow.setPreauthDto(bean);
				nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);

			}

			wholeVLayout.addComponent(nomineeDetailsTable);

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
			legalHeirDetails.setPresenterString(SHAConstants.FINANCIAL_APPROVER);
			legalHeirLayout.addComponent(legalHeirDetails);
			wholeVLayout.addComponent(legalHeirLayout);
			
			if (enableLegalHeir) {
				
				legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
				legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}

			wholeVLayout.addComponent(legalHeirLayout);

			if (financialButtonObj != null) {
				wholeVLayout.addComponent(financialButtonObj);
			}
		}

	}
	
	public void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		
		if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
			
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

		//CR2019202
	public void fraudAlert(){	
		if(bean.getFraudAlertFlag() != null && bean.getFraudAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && bean.getFraudAlertMsg() != null){
			SHAUtils.showFraudAlertMessageBox(bean.getFraudAlertMsg());
		}
	}
	
	 //  CR2019199
	 public void showAlertForCoInsurance(){
		 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(SHAConstants.COINSURANCE_ALERT+ "</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
					if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
						showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
					}
					else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
						showDuplicateInsured(bean.getDuplicateInsuredList());
					}else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
							(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					} else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
						showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}else{
						displayAlert();
					}
				}
			});
	 }
	 
	 public Double getTotalClaimedAmtForRRC(){
		 Double claimedAmount =this.uploadDocumentListenerTableObj.getTotalClaimedAmount();
		 return claimedAmount;
	 }

	 private void claimAlertPopUP(ClaimRemarksAlerts remarksAlerts){			

		 final MessageBox InfoMsg = MessageBox
				 .createInfo()
				 .withCaptionCust(SHAConstants.CLAIMS_ALERT_CAPTION +remarksAlerts.getAlertCategory().getValue())
				 .withMessage(remarksAlerts.getRemarks())
				 .withYesButton(ButtonOption.caption(ButtonType.OK.name()))
				 .withHelpButton(ButtonOption.caption("View Documents"))
				 .open();

		 Button homeButton=InfoMsg.getButton(ButtonType.YES);
		 homeButton.addClickListener(new ClickListener() {
			 private static final long serialVersionUID = 7396240433865727954L;

			 @Override
			 public void buttonClick(ClickEvent event) {
				 InfoMsg.close();
			 }
		 });
		 Button viewDocuments=InfoMsg.getButton(ButtonType.HELP);
		 viewDocuments.setEnabled(false);				
		 if(remarksAlerts.getRemarksDocs() != null && !remarksAlerts.getRemarksDocs().isEmpty()){
			 viewDocuments.setEnabled(true);
			 viewDocuments.addClickListener(new ClickListener() {
				 private static final long serialVersionUID = 7396240433865727954L;
				 @Override
				 public void buttonClick(ClickEvent event) {
					 PreauthMapper preauthMapper = PreauthMapper.getInstance();
					 viewClaimAlertUploadDocument(preauthMapper.getClaimsAlertDocsDTOList(remarksAlerts.getRemarksDocs()));
				 }
			 });
		 }
	 }
	 private void viewClaimAlertUploadDocument(List<ClaimsAlertDocsDTO> docsDTOs) {

		 claimsAlertDocsView = claimsAlertDocsViewUI.get();
		 claimsAlertDocsView.init(null,true);
		 claimsAlertDocsView.setCurrentPage(UI.getCurrent().getPage());
		 claimsAlertDocsView.setDocsTablesValue(docsDTOs);

		 Window popup = new com.vaadin.ui.Window();
		 popup.setCaption("View Uploaded Files");
		 popup.setWidth("75%");
		 popup.setHeight("75%");
		 popup.setContent(claimsAlertDocsView);
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
	 
	 public void getAlertForCoronaAlert() {
		 if(bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null && this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null){
		 	Long alertDte =null;
			Date policyStartDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			
			alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
			Long totalAlertDte = alertDte + 1;
			if(totalAlertDte < 15){
				bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
			}
		    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		    SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);
		    
		    if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE) &&
		    		bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
		    	totalAlertDte = null;
		    	if(this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null
		    			&& this.bean.getPreauthDataExtractionDetails().getDischargeDate() != null){
		    		Date DOA = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			    	Date DOD = this.bean.getPreauthDataExtractionDetails().getDischargeDate();
			    	
			    	alertDte = SHAUtils.getDiffDays(DOA, DOD);
			        totalAlertDte = alertDte + 1;
			        if(totalAlertDte != null && totalAlertDte <= 3){
			        	System.out.println(totalAlertDte+ "Approval diabled" );
			        	bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
			        }
		    	}
		    }
		
		 }
	 }
	 
	 public void getAlertForRelatedOrNotRelatedCovidGrp(){
		 List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
				for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
					if(detailsTableDTO != null && detailsTableDTO.getIcdCode() != null 
							&& !(detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_NOT_IDENT_KEY)
							|| detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY))){
						SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_ICD_MSG, "INFORMATION");
						bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
						break;
					}
				}
			}
	 }
	 
	 public void getWarningRakshaAlert() {
		 if(bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null && this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null){
		 	Long alertDte =null;
			Date policyStartDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			
			alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
			Long totalAlertDte = alertDte + 1;
			if(totalAlertDte < 15){
				bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
				SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);
			}
		    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		    
		
		 }
		
	 }
	 
	 public void getAlertForKavachCoronaAlert() {
		 if(bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null && this.bean.getPreauthDataExtractionDetails().getAdmissionDate() != null){
		 	Long alertDte =null;
			Date policyStartDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			
			alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
			Long totalAlertDte = alertDte + 1;
			if(totalAlertDte < 15){
				bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
			}
		    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		    SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);
			
		 }
	 }
	 
	 public void getAlertForRelatedOrNotRelatedCovidKavach(){
		 List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
				for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
					if(detailsTableDTO != null && detailsTableDTO.getIcdCode() != null 
							&& !(detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY))){
						SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_ICD_MSG, "INFORMATION");
						bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
						break;
					}
				}
			}
			
	 }
	 
	 public void getAlertForRelatedOrNotRelatedCovidRakash(){
		 List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
				for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
					if(detailsTableDTO != null && detailsTableDTO.getIcdCode() != null 
							&& !(/*detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_NOT_IDENT_KEY)
							||*/ detailsTableDTO.getIcdCode().getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY))){
						SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_ICD_MSG, "INFORMATION");
						bean.getPreauthDataExtractionDetails().setIsStarGrpCorApproveBtn(true);
						break;
					}
				}
			}
	 }
	 //Added for product 076
	 private void setTableValuesForHospCash()
	 {
		 //MED-PRD-076
		 if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				 this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				 || (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				 this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			 if(null != hospitalCashProductDetailsTable)
			 {
				 List<HopsitalCashBenefitDTO> hospiitalCashList = bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO().getHopsitalCashBenefitDTO();
				 if(null != hospiitalCashList && !hospiitalCashList.isEmpty())
				 {
					 //hospitalCashProductDetailsTable.removeAllItems();
					 for (HopsitalCashBenefitDTO hospiitalCashList2 : hospiitalCashList) {
						 hospitalCashProductDetailsTable.addBeanToList(hospiitalCashList2);
					 }

				 }
			 }
		 }
	 }
	 
	 public void getAgeingAlertBox(Integer activityAge,Integer claimAge) {	 
		 
		String activityColor = getColor(activityAge);
		String claimColor = getColor(claimAge);
		
		String activityabel = "<b style = 'background-color:"+activityColor+";'> Activity Ageing : "+activityAge+ " Days </b>";
		String claimLabel = "<b style = 'background-color:"+claimColor+";'> Claim Ageing : "+claimAge+ " Days </b>";
		if(activityColor != null && activityColor.equalsIgnoreCase("red")){
			activityabel = "<b style = 'background-color:"+activityColor+"; color: white;'> Activity Ageing : "+activityAge+ " Days </b>";
		}
		if(claimColor != null && claimColor.equalsIgnoreCase("red")){
			claimLabel = "<b style = 'background-color:"+claimColor+"; color: white;'> Claim Ageing : "+claimAge+ " Days </b>";
		}
		
		String content = claimLabel +"<br>"+activityabel;
		
		//SHAUtils.showMessageBoxWithCaption(content, "Alert");
		getAlertMessage(content);
	}
		
		public String getColor(Integer age)
		{
			String color = null;
			if(age != null)
			{
				if(age >= 4 && age <= 7)
				{
					color = "yellow";
				}
				else if(age > 7)
				{
					color = "red";
				}
				else{
					color = "white";
				}
			}
			return color;
		}
		
		public static Boolean getAlertMessage(String alertMsg) {
			//String alertMsg = SHAConstants.POLICY_INSTALMENT_PREMIUM_EXCEEDING_MESSAGE;
			final MessageBox msgBox = MessageBox.createWarning()
				    .withCaptionCust("Warning") .withHtmlMessage(alertMsg)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			return true;
		}
 }
