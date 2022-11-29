package com.shaic.claim.reimbursement.billing.pages.billreview;

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
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingTable;
import com.shaic.claim.intimation.create.ViewBasePolicyDetails;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.reimbursement.billclassification.BillClassificationEditUI;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.AddOnBenefitsDataExtractionPage;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsPatientCareListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.BillingProcessButtonsUIForFirstPage;
import com.shaic.claim.reimbursement.billing.wizard.PatientCareTable;
import com.shaic.claim.reimbursement.billing.wizard.TreatmentDateListenerTable;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewIrdaNonPayablePdfPage;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class BillingReviewPageUI extends ViewComponent {

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;

	private GWizard wizard;

	////private static Window popup;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	@EJB
	private MasterService masterService;

	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;

	@Inject
	private Instance<BillingProcessButtonsUIForFirstPage> billingProcessButtonInstance;

	private BillingProcessButtonsUIForFirstPage billingProcessButtonObj;

	@Inject
	private Instance<RoomRentMatchingTable> roomRentMatchingTable;

	@Inject
	private Instance<AddOnBenefitsDataExtractionPage> addOnBenifitsPageInstance;

	@Inject
	private Instance<TreatmentDateListenerTable> treatmentListenerTable;

	@Inject
	private Instance<PatientCareTable> patientCareTableInstance;

	@Inject
	private BillClassificationUI billClassificationUI;

	@Inject
	private Instance<BillClassificationEditUI> billClassificationEditUIInstance;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;

	private BillClassificationEditUI billClassificationEditUIObj;

	private TreatmentDateListenerTable treatmentListenerTableObj;

	private PatientCareTable patientCareTableObj;

	private RoomRentMatchingTable roomRentMatchingTableObj;

	private AddOnBenefitsDataExtractionPage addOnBenefitsPageObj;

	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	@Inject
	private ViewIrdaNonPayablePdfPage pdfPageUI;
	@Inject
	private ViewBasePolicyDetails viewBasePolicyDetail;
	@EJB
	private IntimationService intimationService;

	private DateField admissionDate;

	private DateField dischargeDate;

	private TextField admissionReason;

	private TextField dischargeReason;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;
	
	//private ComboBox cmbCatastrophicLoss;
	
	private ComboBox cmbNatureOfLoss;
	
	private ComboBox cmbCauseOfLoss;

	private FormLayout firstFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;
	
	private VerticalLayout legalHeirLayout;

	private Date admissionDateValue;

	//private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

	private FormLayout patientStatusFLayout;

	private OptionGroup hospitalAddOnBenefits;

	private OptionGroup patientCareAddOnBenefits;

	private Button viewIRDAButton;

	private Button billingWorksheetBtn;

	private FormLayout admissionReasonLayout;

	private FormLayout dischargeReasonLayout;

	public Boolean isMappingDone = false;

	public Boolean isMatchTheFollowing = false;

	private Boolean isValid = false;

	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;

	// Added below fields for Bypass functionality..............

	public TextField txtAdmissionDate;

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

	private Button editBillClassification;
	
	private FormLayout legaHeirLayout;
	
	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;

	@EJB
	private PolicyService policyService;

	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;

	@EJB
	private ReimbursementQueryService reimbursementQueryService;

	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;
	
	@Inject
	private Instance<ClaimsAlertUploadDocumentPageUI> claimsAlertDocsViewUI;
	
	private ClaimsAlertUploadDocumentPageUI claimsAlertDocsView;
	
	private ComboBox caretreatment;
	private OptionGroup optPatientDayCare;

	private ComboBox cmbPatientDayCareValue;
	
	private ComboBox cmbHospitalCashDueTo;
	
	@Inject
	private Instance<AddOnBenefitsListenerTable> addOnBenefitsListenerTable;
	
	private AddOnBenefitsListenerTable addOnBenefitsListenerTableObj;
	
	@Inject
	private Instance<AddOnBenefitsPatientCareListenerTable> addOnBenefitsPatientCareListenerTable;
	
	private AddOnBenefitsPatientCareListenerTable addOnBenefitsPatientCareLiseterObj;
	

	public Double totalApprovedAmt = 0d;
	
	
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
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}


	public Boolean alertMessageForPED(final String message) {
		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ message SHAConstants.PED_RAISE_MESSAGE + "</b>", ContentMode.HTML);*/
		Label successLabel = new Label(message , ContentMode.HTML);
		final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		VerticalLayout layout = new VerticalLayout(successLabel);
		
		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		
		/*layout.addComponent(homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);*/
	/*	layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/
//		HorizontalLayout hLayout = new HorizontalLayout(layout);
//		hLayout.setMargin(true);
//		hLayout.setStyleName("borderLayout");

		/*final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
				
				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
					 alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					 bean.setMultiplePEDAvailableNotDeleted(false);
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
				} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
				}  else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} 
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
						popupMessageForWaitingPeriod();
					}*/else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}
	
	@SuppressWarnings("static-access")
	public Boolean warningMessageForLumpsum(String message) {
   		/*Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message , buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				/*
				if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
				{
					if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageForWaitingPeriod();
				}
			  }*/
			}
		});
		return true;
	}
	
	
	@SuppressWarnings("static-access")
	public Boolean alertMessageForMediPremier(String message) {
   		/*Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox( message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
						popupMessageForWaitingPeriod();
					}*/
			}
		});
		return true;
	}
	
	public Boolean alertMessageForAutoRestroation() {
   		/*Label successLabel = new Label(
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.AUTO_RESTORATION_MESSAGE, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsAutoRestorationDone(true);

				if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} 
				}  else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);

				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
						(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}

	public Component getContent() {
		DBCalculationService dbService = new DBCalculationService();
		
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
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)){
			getAlertForKavachCoronaAlert();
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)){
			getWarningRakshaAlert();
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			getAlertForCoronaAlert();
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_GRP_ALERT_MSG, "INFORMATION");
		}
		
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
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
		
				
				//Covid 19 GLX2020086
				
				/*if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)){
					popupMessageForWaitingPeriod();
				}else{*/
					if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						//popupMessageFor30DaysWaitingPeriod();
						popupMessageForWaitingPeriod();
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
				//}
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		if(this.bean.getIsCoInsurance()){
			showAlertForCoInsurance(memberType);
		}
		else if(null != memberType && !memberType.isEmpty()){
			showCMDAlert(memberType);
		}
		else if(null != this.bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getTopUpPolicyAlertFlag())){
			showTopUpAlertMessage(this.bean.getTopUpPolicyAlertMessage());
		}
		else if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && this.bean.getDuplicateInsuredList() != null && ! this.bean.getDuplicateInsuredList().isEmpty()){
			showDuplicateInsured(this.bean.getDuplicateInsuredList());
		}
		else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null &&
				(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
						bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
			showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
		}
		else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
			showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
		}
		else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}
		else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
			paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
		}	
		else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
			showICRMessage();
		}else if(bean.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		else if(bean.getIsZUAQueryAvailable()){
			zuaQueryAlertPopupMessage();
		}
		else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(this.bean.getClaimCount() >1){
			alertMessageForClaimCount(this.bean.getClaimCount());
		}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
			suspiousPopupMessage();
		}else if(!bean.getNonPreferredPopupMap()
				.isEmpty()){
			nonPreferredPopupMessage();
		}
		else if (bean.getIsPEDInitiated()) {
//			alertMessageForPED();
			if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			}else{
				alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
			}
		} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
			alertMessageForPostHosp();
		} else if(bean.getIsHospitalDiscountApplicable()){
			alertForHospitalDiscount();
		} else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		}
		 else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
				 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

			if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
				alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
			} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
					&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
				warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
			} 
			else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
					&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
				warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
			} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
				StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
			} 
			
		} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
			StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
		}else if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
				(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
				bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
			popupMessageForWaitingPeriod();
		}*/else if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
		}
		
		if(bean.getNewIntimationDTO().getHospitalDto() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBox(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");

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
			Long wkKey = dbService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.BILLING_CURRENT_KEY);
			Map<String, Integer> ageingValues = dbService.getClaimAndActivityAge(wkKey,bean.getClaimDTO().getKey(),(bean.getKey()).toString());
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

		 //code added for CR glx2020167 by noufel
		if(bean.getIsHcTopupPolicyAvail() && bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()){
			SHAUtils.showMessageBox(SHAConstants.ALERT_MSG_FOR_HC_TOPUP_POLICY + bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),"Information");
		}
		fraudAlert();
		isMappingDone = false;
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		txtAdmissionDate = new TextField();
		txtDischargeDate = new TextField();

		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setEnabled(false);
		admissionDate = (DateField) binder.buildAndBind("Date of Admission",
				"admissionDate", DateField.class);
		dischargeDate = (DateField) binder.buildAndBind("Date of Discharge",
				"dischargeDate", DateField.class);
		noOfDaysTxt = (TextField) binder.buildAndBind("Length of Stay", "noOfDays",
				TextField.class);
		noOfDaysTxt.setRequired(true);
		noOfDaysTxt.setMaxLength(4);
		addLengthOfStayValueChangeListner();
		criticalIllnessChk = (CheckBox) binder.buildAndBind("Critical Illness",
				"criticalIllness", CheckBox.class);
		cmbRoomCategory = (ComboBox) binder.buildAndBind("Room Category",
				"roomCategory", ComboBox.class);
		
		ventilatorSupportOption = (OptionGroup) binder.buildAndBind(
				"Ventilator Support", "ventilatorSupport", OptionGroup.class);
		ventilatorSupportOption.addItems(getReadioButtonOptions());
		ventilatorSupportOption.setItemCaption(true, "Yes");
		ventilatorSupportOption.setItemCaption(false, "No");
		ventilatorSupportOption.setStyleName("horizontal");
		System.out.println("Ventilator Suppoort flag"+this.bean.getPreauthDataExtractionDetails().getVentilatorSupportFlag());
		System.out.println("Ventilator Suppoort"+this.bean.getPreauthDataExtractionDetails().getVentilatorSupport());
		//ventilatorSupportOption.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupportFlag()? true : false);
		ventilatorSupportOption.setEnabled(false);
			cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cmbRoomCategory != null && cmbRoomCategory.getValue() != null) {
					SelectValue sectionValue = (SelectValue) cmbRoomCategory
							.getValue();
					System.out.println(String.format("Room Category: [%s]", sectionValue.getValue()));
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
					bean.getPreauthDataExtractionDetails().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setRoomCategory(sectionValue);
					bean.getPreauthDataExtractionDetails().getUploadDocumentDTO().setValChangedRoomCategory(sectionValue);
					uploadDocumentListenerTableObj.setRoomType(sectionValue);
					
					/*bean.getPreauthDataExtractionDetails().setVentilatorSupport(bean.getPreauthDataExtractionDetails().getVentilatorSupport());
					System.out.println("ventilatorSupportOption in value change listner"+bean.getPreauthDataExtractionDetails().getVentilatorSupport());*/
					//bean.getPreauthDataExtractionDetails().setVentilatorSupport(ventilatorSupportOption.getValue()?"true":"false");
				}
				
			}
		});
		
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
//		optPatientDayCare.select(false);

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
				if(bean.getPreauthDataExtractionDetails().getPatientDayCareDueTo() != null){
					cmbPatientDayCareValue.setValue(bean.getPreauthDataExtractionDetails().getPatientDayCareDueTo());
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

		if ((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE)
				.equals(automaticRestorationTxt.getValue())
				|| (SHAConstants.AUTO_RESTORATION_NOTDONE)
						.equals(automaticRestorationTxt.getValue())) {
			cmbIllness.setEnabled(false);
		}

		preauthApprovedAmt = (TextField) binder
				.buildAndBind("Pre-auth Approved Amt", "preauthTotalApprAmt",
						TextField.class);
		preauthApprovedAmt.setEnabled(false);
		preauthApprovedAmt.setNullRepresentation("");

		cmbSection = (ComboBox) binder.buildAndBind("Section", "section",
				ComboBox.class);

		if (this.bean.getNewIntimationDTO() != null
				&& !ReferenceTable.getSectionKeys().containsKey(
						this.bean.getNewIntimationDTO().getPolicy()
								.getProduct().getKey())) {
			cmbSection.setEnabled(false);
		}
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}

		admissionReasonLayout = new FormLayout();

		dischargeReasonLayout = new FormLayout();
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		
		if(null != bean.getNewIntimationDTO().getPolicy() && 
				ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			criticalIllnessChk.setEnabled(false);
			cmbSpecifyIllness.setEnabled(false);
		}
		
		HorizontalLayout autoRestorationLayout = new HorizontalLayout();
		autoRestorationLayout.addComponents(automaticRestorationTxt,acutomaticRestorationCount);
		autoRestorationLayout.setCaption("Automatic Restoration");
		autoRestorationLayout.setSpacing(true);
		autoRestorationLayout.setSpacing(true);
		
		
		
		firstFLayout = new FormLayout(admissionDate, cmbRoomCategory,ventilatorSupportOption,
				illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt,
				cmbSection);
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt,
					autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss,caretreatment);
		}
		else if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt,
					autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss,optPatientDayCare,cmbPatientDayCareValue,cmbHospitalCashDueTo);

		}
		else{
			patientStatusFLayout = new FormLayout(dischargeDate, noOfDaysTxt,
					autoRestorationLayout, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);

		}
		

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				patientStatusFLayout);
		formHLayout.setWidth("100%");

		uploadDocumentListenerTableObj = uploadDocumentListenerTable.get();
		// uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING);
		uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING,
				txtAdmissionDate, txtDischargeDate);
		uploadDocumentListenerTableObj.init();
		uploadDocumentListenerTableObj.setCaption("Uploaded Documents");

		hospitalAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Hospital Cash) Applicable ",
				"hospitalCashAddonBenefits", OptionGroup.class);

		hospitalAddOnBenefits.addItems(getReadioButtonOptions());
		hospitalAddOnBenefits.setItemCaption(true, "Yes");
		hospitalAddOnBenefits.setItemCaption(false, "No");
		hospitalAddOnBenefits.setStyleName("horizontal");

		patientCareAddOnBenefits = (OptionGroup) binder.buildAndBind(
				"Add on Benefits (Patient Care) Applicable ",
				"patientCareAddOnBenefits", OptionGroup.class);

		patientCareAddOnBenefits.addItems(getReadioButtonOptions());
		patientCareAddOnBenefits.setItemCaption(true, "Yes");
		patientCareAddOnBenefits.setItemCaption(false, "No");
		patientCareAddOnBenefits.setStyleName("horizontal");

		addOnBenefitsPageObj = addOnBenifitsPageInstance.get();
		Boolean isStarCare = false;
		if (bean.getNewIntimationDTO() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct() != null
				&& (bean.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.STAR_CARE_FLOATER) || bean
						.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.STAR_CARE_INVIDUAL))) {
			isStarCare = true;
		}
		bean.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO()
				.setAdmissionDate(bean.getNewIntimationDTO().getAdmissionDate());
		bean.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO()
				.setDischargeDate(
						bean.getPreauthDataExtractionDetails()
								.getDischargeDate());

		addOnBenefitsPageObj.init(bean.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO(), SHAConstants.BILLING, isStarCare);
		addOnBenefitsPageObj.getContent();

		viewIRDAButton = new Button("View IRDA Non-Payables");
		viewIRDAButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7439398898591124006L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				pdfPageUI.init(null, null, popup);
				popup.setClosable(true);
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

				uploadDocumentViewImpl
						.initPresenter(SHAConstants.BILLING_WORKSHEET);
				uploadDocumentViewImpl.init(bean, popup);
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

			}
		});
		treatmentListenerTableObj = treatmentListenerTable.get();
		treatmentListenerTableObj.init(this.bean);
		treatmentListenerTableObj.setEnabled(false);
		treatmentListenerTableObj.setCaption("Treatment Details Table");
		if (this.bean.getIsHospitalizationRepeat()) {
			treatmentListenerTableObj.setEnabled(true);
		}

		HorizontalLayout layout = new HorizontalLayout(viewIRDAButton,
				billingWorksheetBtn);
		layout.setWidth("100%");
		layout.setComponentAlignment(viewIRDAButton, Alignment.MIDDLE_RIGHT);

		if (bean.getIsFinalEnhancement()
				|| (!bean.getHospitalizaionFlag() && !bean
						.getPartialHospitalizaionFlag())) {
			admissionDate.setEnabled(true);// Making admissionDate field as editable field as per CR R1044.
			dischargeDate.setEnabled(true);// Making dischargeDate field as editable field as per CR R1044.
		}

		addTotalClaimedListener();
		billClassificationUI.init(bean);

		editBillClassification = new Button("EDIT");
		editBillClassification.setStyleName(ValoTheme.BUTTON_LINK);
		editBillClassification.setEnabled(false);
		addEditListener();
		HorizontalLayout editLayout = new HorizontalLayout(new Label(
				"BILL CLASSIFICATION"), editBillClassification);
		editLayout.setSpacing(true);
		VerticalLayout billclasihori = new VerticalLayout(editLayout,
				billClassificationUI);

		if (this.bean.getPreauthDataExtractionDetails()
				.getDocAckknowledgement() != null
				&& bean.getPreauthDataExtractionDetails()
						.getDocAckknowledgement().getDocumentReceivedFromId()
						.getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			editBillClassification.setEnabled(true);
			if(bean.getClaimDTO().getClaimSubCoverCode() != null && (bean.getClaimDTO().getClaimSubCoverCode().equalsIgnoreCase(ReferenceTable.NEW_BORN_LUMPSUM_SUB_COVER_CODE) || bean.getClaimDTO().getClaimSubCoverCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SUB_COVER_CODE)) ) {
				editBillClassification.setEnabled(false);
			}
		}
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.BILLING);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());

		wholeVLayout = new VerticalLayout(formHLayout,sectionDetailsListenerTableObj,
				treatmentListenerTableObj, layout, billclasihori,
				uploadDocumentListenerTableObj, addOnBenefitsPageObj);
		// wholeVLayout = new VerticalLayout(formHLayout,
		// treatmentListenerTableObj, layout, uploadDocumentListenerTableObj,
		// addOnBenefitsPageObj);
		wholeVLayout.setSpacing(true);

		addListener();
		admissionDateValue = admissionDate.getValue();
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(dischargeDate);
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(caretreatment);
		
		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			mandatoryFields.add(cmbSection);
		}
		
		//Added for product 076
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
		editBillClassification.setEnabled(false);

		addOnBenefitsListenerTableObj = addOnBenefitsListenerTable.get();
		
		addOnBenefitsListenerTableObj.init();
		
		addOnBenefitsPatientCareLiseterObj = addOnBenefitsPatientCareListenerTable.get();
		
		addOnBenefitsPatientCareLiseterObj.init();
		/*hospitalCashProductDetailsTable = hospitalCashProductDetailsInstance.get();
		hospitalCashProductDetailsTable.clearTableItems();
		hospitalCashProductDetailsTable.init(bean.getReceiptOfDocumentsDTO());
		wholeVLayout.addComponent(hospitalCashProductDetailsTable);*/
		
		wholeVLayout.addComponent(addOnBenefitsListenerTableObj);
		wholeVLayout.addComponent(addOnBenefitsPatientCareLiseterObj);
		setTableValuesForHospCash();
		
		}
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}
		noOfDaysTxt.setValue(bean.getPreauthDataExtractionDetails().getNoOfDays());

		billingProcessButtonObj = billingProcessButtonInstance.get();
		billingProcessButtonObj.initView(this.bean, this.wizard);
		wholeVLayout.addComponent(billingProcessButtonObj);

		showOrHideValidation(false);
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

	protected void addEditListener() {
		editBillClassification.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7023902310675004614L;

			@Override
			public void buttonClick(ClickEvent event) {

				/*Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				okBtn.setEnabled(false);*/
				List<UploadDocumentDTO> values = uploadDocumentListenerTableObj
						.getValues();
				billClassificationEditUIObj = billClassificationEditUIInstance
						.get();
				/*billClassificationEditUIObj.initForEdit(bean, values,
						uploadDocumentListenerTableObj);*/

				VerticalLayout layout = new VerticalLayout(
						billClassificationEditUIObj);
				//layout.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);

				/*final ConfirmDialog dialog = new ConfirmDialog();
				// dialog.setCaption("Alert");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
				Button okBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				okBtn.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(billClassificationEditUIObj.validatePage()){
							billClassificationUI.init(bean);
							bean.setIsEditBillClassification(true);

							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setHospitalCashAddonBenefits(
											bean.getAddOnBenefitsHospitalCash());
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setPatientCareAddOnBenefits(
											bean.getAddOnBenefitsPatientCare());
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setHospitalBenefitFlag(
											bean.getAddOnBenefitsHospitalCash() ? "HC"
													: null);
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setPatientCareBenefitFlag(
											bean.getAddOnBenefitsPatientCare() ? "PC"
													: null);
						   setOtherBenefitsValueToDTO(bean);
							
							Boolean isStarCare = false;
							if (bean.getNewIntimationDTO() != null
									&& bean.getNewIntimationDTO().getPolicy()
											.getProduct() != null
									&& (bean.getNewIntimationDTO()
											.getPolicy()
											.getProduct()
											.getKey()
											.equals(ReferenceTable.STAR_CARE_FLOATER) || bean
											.getNewIntimationDTO()
											.getPolicy()
											.getProduct()
											.getKey()
											.equals(ReferenceTable.STAR_CARE_INVIDUAL))) {
								isStarCare = true;
							}
							addOnBenefitsPageObj.init(bean
									.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO(), SHAConstants.BILLING,
									isStarCare);
							addOnBenefitsPageObj.getContent();

							Button finalOKButton = new Button("OK");
							finalOKButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

							Label label = new Label("Bill Classification Changed");

							VerticalLayout verti = new VerticalLayout(label,
									finalOKButton);
							verti.setSpacing(true);
							verti.setMargin(true);
							verti.setComponentAlignment(label, Alignment.TOP_CENTER);
							verti.setComponentAlignment(finalOKButton,
									Alignment.BOTTOM_CENTER);
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

//							dialog.close();
						}
						

					}
				});
				
				billClassificationEditUIObj.initForEdit(bean, values,
						uploadDocumentListenerTableObj, okBtn);
			}
		});
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

		List<UploadDocumentDTO> uploadDocsList = this.bean
				.getUploadDocumentDTO();
		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != admissionDate) {
					uploadDocumentDTO.setDateOfAdmission(SHAUtils
							.formatDate(admissionDate.getValue()));
					txtAdmissionDate.setValue(SHAUtils.formatDate(admissionDate
							.getValue()));
				}
				if (null != dischargeDate) {
					uploadDocumentDTO.setDateOfDischarge(SHAUtils
							.formatDate(dischargeDate.getValue()));
					txtDischargeDate.setValue(SHAUtils.formatDate(dischargeDate
							.getValue()));
				}
				uploadDocumentDTO.setIntimationNo(this.bean
						.getNewIntimationDTO().getIntimationId());
				uploadDocumentDTO.setInsuredPatientName(this.bean
						.getNewIntimationDTO().getInsuredPatientName());
			}

			this.bean.setUploadDocumentDTO(uploadDocsList);
		}

		/*if (this.uploadDocumentListenerTableObj != null) {
			uploadDocumentListenerTableObj.setReferenceData(referenceData);
			Integer i = 1;
			List<UploadDocumentDTO> uploadList = this.bean
					.getUploadDocumentDTO();
			uploadDocumentListenerTableObj.setTableInfo(uploadList);
			SectionDetailsTableDTO sectionDTO = this.sectionDetailsListenerTableObj.getValue();
			if (null != uploadList && !uploadList.isEmpty())
				for (UploadDocumentDTO uploadDocLayout : uploadList) {
					uploadDocLayout.setSeqNo(i);
					if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getProduct())
					{
						uploadDocLayout.setProductKey( bean.getNewIntimationDTO().getPolicy()
						.getProduct().getKey());
					}
					
					if(null != sectionDTO && null != sectionDTO.getSubCover() && null != sectionDTO.getSubCover().getCommonValue())
						uploadDocLayout.setSubCoverCode(this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
					this.uploadDocumentListenerTableObj
							.addBeanToList(uploadDocLayout);
					i++;
				}
			// uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
		}*/

		// reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		// Setting this feild readOnly true, since this needs to be editable as
		// per sathish sir.

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
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");
		
		BeanItemContainer<SelectValue> treatcareHealthcorona = (BeanItemContainer<SelectValue>) referenceData
				.get("treatcareHealthcorona");
		BeanItemContainer<SelectValue> patientDayCareValue = (BeanItemContainer<SelectValue>) referenceData
				.get("patientDayCareValueContainer");
		
		BeanItemContainer<SelectValue> hospitalCashDueToValue = (BeanItemContainer<SelectValue>) referenceData
				.get("hospitalCashDueTo");

		this.bean.getPolicyDto().setAdmissionDate(admissionDate.getValue());
		this.bean.getPolicyDto().setClaimKey(this.bean.getClaimKey());

		if (cmbSection != null && cmbSection.getValue() != null) {
			SelectValue sectionValue = (SelectValue) cmbSection.getValue();
			this.bean.getPreauthDataExtractionDetails()
					.setSection(sectionValue);
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

		cmbPatientDayCareValue.setContainerDataSource(patientDayCareValue);
		cmbPatientDayCareValue.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientDayCareValue.setItemCaptionPropertyId("value");
		
        cmbHospitalCashDueTo.setContainerDataSource(hospitalCashDueToValue);
        cmbHospitalCashDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        cmbHospitalCashDueTo.setItemCaptionPropertyId("value");
		
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

		List<DocumentDetailsDTO> docDTOList = (List<DocumentDetailsDTO>) referenceData
				.get(SHAConstants.BILL_CLASSIFICATION_DETAILS);
		this.bean.setDocumentDetailsDTOList(docDTOList);
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		if (this.uploadDocumentListenerTableObj != null) {
			uploadDocumentListenerTableObj.setReferenceData(referenceData);
			Integer i = 1;
			List<UploadDocumentDTO> uploadList = this.bean
					.getUploadDocumentDTO();
			uploadDocumentListenerTableObj.setTableInfo(uploadList);
			
			//SectionDetailsTableDTO sectionDTO = this.sectionDetailsListenerTableObj.getValue();
			if (null != uploadList && !uploadList.isEmpty())
				for (UploadDocumentDTO uploadDocLayout : uploadList) {
					uploadDocLayout.setSeqNo(i);
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
						uploadDocLayout.setRoomCategory(this.bean.getPreauthDataExtractionDetails().getRoomCategory());
					}
					if(null != this.bean.getClaimDTO().getClaimSubCoverCode())// && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue())
						uploadDocLayout.setSubCoverCode( this.bean.getClaimDTO().getClaimSubCoverCode());
					if(null != this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
						uploadDocLayout.setDomicillaryFlag(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation());
					this.uploadDocumentListenerTableObj
							.addBeanToList(uploadDocLayout);
					i++;
				}
			// uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
		}
		
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
		StringBuffer eMsg = new StringBuffer() ;
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

		if (this.addOnBenefitsPageObj != null
				&& !this.addOnBenefitsPageObj.validatePage()) {
			List<String> errors = this.addOnBenefitsPageObj.getErrors();
			hasError = true;
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		

		/*if (admissionDate != null && dischargeDate != null
				&& admissionDate.getValue() != null
				&& dischargeDate.getValue() != null) {
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(
					admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			if (daysBetweenDate >= 0
					&& SHAUtils.getIntegerFromString(noOfDaysTxt.getValue()) > daysBetweenDate
							.intValue()) {
				hasError = true;
				eMsg.append("No of days should be DOD-DOA+1 </br>");
			}
		}*/
		if(noOfDaysTxt != null && noOfDaysTxt.getValue() == null || (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter the Length of Stay. </br>");
		}
		
		List<UploadDocumentDTO> values = uploadDocumentListenerTableObj
				.getValues();
		if (!bean.getIsFirstPageSubmit()) {
			for (UploadDocumentDTO uploadDocumentDTO : values) {
				if (uploadDocumentDTO.getFileType() != null
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					if (!uploadDocumentDTO.getStatus()) {
						hasError = true;
						eMsg.append("Please Enter Bill Entry Details</br>");
					}
				}
			}
		}
		if (this.bean.getIsHospitalizationRepeat()) {
			if (treatmentListenerTableObj != null
					&& !treatmentListenerTableObj.isValid()) {
				List<String> errors = this.treatmentListenerTableObj
						.getErrors();
				hasError = true;
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}

		if (!hasError) {
			if (billClassificationEditUIObj != null
					&& !bean.getPreHospitalizaionFlag()
					&& billClassificationEditUIObj
							.checkSelectedClassifationAvailableOrNot(ReferenceTable.PRE_HOSPITALIZATION)) {
				// hasError = true;
				// eMsg +=
				// "Bill Entry classification should match with the Bill Classifcation. Please make the change and proceed further. </br>";
			}

			if (billClassificationEditUIObj != null
					&& !bean.getPostHospitalizaionFlag()
					&& billClassificationEditUIObj
							.checkSelectedClassifationAvailableOrNot(ReferenceTable.POST_HOSPITALIZATION)) {
				// hasError = true;
				// eMsg +=
				// "Bill Entry classification should match with the Bill Classifcation. Please make the change and proceed further. </br>";
			}
		}
		
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg.append("Please Select Ventilator Support. </br>");
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
					boolean nomineeAccPrefered = false;

					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
//							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	

						    /*if(nomineeDetailsDto.getPreference() == null || nomineeDetailsDto.getPreference().isEmpty()) {
						    	eMsg.append("Please Select Account Preference for Selected Nominee No. "+ (tableList.indexOf(nomineeDetailsDto)+1) + ".</br>");
						    	nomineeAccPrefered = false;
						    	hasError = true;
						    }
						    else {
						    	nomineeAccPrefered = true;
						    }*/
						}				
						
					}
					
					/**
					Acc. Preference disable for Nominee as per Mr. Satish on 14-12-2019
					**/
					nomineeAccPrefered = true; 
					
					bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
						bean.getNewIntimationDTO().setNomineeAddr(null);
//						hasError = false;
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
					eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
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
						 }
					}
				 }
			}					
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE) 
						||  (bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE) 
								&& !(bean.getAddOnBenefitsHospitalCash())))&&
				null != this.caretreatment && null == this.caretreatment.getValue())
		{
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
		
		//added for admission date validation by noufel
		if(admissionDate != null){
			Date dateOfAdmissionValue = admissionDate.getValue() != null ? admissionDate.getValue() : null;
			Date policyFrmDate = bean.getPolicyDto().getPolicyFromDate();;
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
			layout.addComponent(label);*/

			/*ConfirmDialog dialog = new ConfirmDialog();
			 * 
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
				if (!isMappingDone) {
					SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
					SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
					SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
					getMappingDone();
				}

				if (!bean.getIsComparisonDone()
						&& (bean.getHospitalizaionFlag()
								|| bean.getPartialHospitalizaionFlag() || bean
									.getIsHospitalizationRepeat())) {
					fireViewEvent(
							BillingReviewPagePresenter.COMPARE_WITH_PREVIOUS_ROD,
							bean);
				}
				if (this.treatmentListenerTableObj != null) {
					this.bean.getPreauthDataExtractionDetails()
							.setTreatmentDateList(
									this.treatmentListenerTableObj.getValues());
				}

				if (null != this.bean.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()) {

					if (null != this.bean.getPreauthDataExtractionDetails()
							.getUploadDocumentDTO()
							.getPatientCareAddOnBenefits()
							&& this.bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.getPatientCareAddOnBenefits()) {
						fireViewEvent(
								BillingReviewPagePresenter.SAVE_PATIENT_CARE_TABLE_VALUES,
								this.bean);
					}
					if (null != this.bean.getPreauthDataExtractionDetails()
							.getUploadDocumentDTO()
							.getHospitalCashAddonBenefits()
							&& this.bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.getHospitalCashAddonBenefits()) {
						fireViewEvent(
								BillingReviewPagePresenter.SAVE_HOSPITAL_CASH_TABLE_VALUES,
								this.bean);
					}
					
					if (null != this.bean.getPreauthDataExtractionDetails()
							.getUploadDocumentDTO()
							.getOtherBenefit()) {
						fireViewEvent(
								BillingReviewPagePresenter.SAVE_OTHER_BENEFITS_TABLE_VALUES,
								this.bean);
					}
				
				}
				
				if((ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
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
									BillingReviewPagePresenter.GET_BENEFIT_DETAILS,
									this.bean);
					  }
				}

				Date policyFromDate = bean.getNewIntimationDTO().getPolicy()
						.getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate,
						admissionDate.getValue());
				MastersValue policyType = bean.getNewIntimationDTO()
						.getPolicy().getPolicyType();

				/*if ((diffDays != 0 && diffDays > 90)
						|| (policyType != null && policyType.getKey().equals(
								ReferenceTable.RENEWAL_POLICY))
						|| !bean.getAdmissionDatePopup()) {*/
				if(policyType != null && !policyType.getKey().equals(
						ReferenceTable.FRESH_POLICY)){
					this.bean.setAlertMessageOpened(true);
				}

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
					
					if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null 
							&& ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
									|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))*/
										(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
												||SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
												|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
											&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
									||  (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
											SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
						
						if(null != bean && bean.getPreauthDataExtractionDetails() != null &&
								null != bean.getPreauthDataExtractionDetails().getCauseOfInjury() && 
								null != bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId() &&
		                                null != bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() && 
		                                null != bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId())
						{								
							if((ReferenceTable.ROAD_TRAFFIC_ACCIDENT).equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()) &&
                                    bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID))
							
						{
							fireViewEvent(BillingReviewPagePresenter.GET_RTA_BALANCE_SI, bean);
						}
						}
						}
					if(uploadDocumentListenerTableObj !=null && uploadDocumentListenerTableObj.getTotalClaimedAmount() !=null){
						bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(uploadDocumentListenerTableObj.getTotalClaimedAmount().longValue());
					}
					
				}

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
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

				popup.close();
				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap()
						.isEmpty()){
					nonPreferredPopupMessage();
				}
				else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					alertMessageForPostHosp();
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
				 else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					} 
					
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
						(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					popupMessageFor30DaysWaitingPeriod();
				}
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	*/

		
		/*String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
      Label successLabel = new Label(msg+"</b>",ContentMode.HTML);
		
   		if(this.bean.getClaimCount() >2){
	   		successLabel = new Label(msg+"<br>"
							+ additionalMessage+"</b>",
					ContentMode.HTML);
   		}

   		successLabel.addStyleName(ValoTheme.LABEL_H3);

		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor);
   		Panel panel = new Panel();
   		panel.setContent(firstForm);
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}

		panel.setSizeFull();
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", firstForm, buttonsNamewithType, GalaxyTypeofMessage.CRITICALALERT.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				} 
				else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) >= 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageFor30DaysWaitingPeriod();
				}else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
				
			}
		});*/
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		Button homeButton=new Button() ;
		
		DBCalculationService dbService = new DBCalculationService();
		int gmcClaimCount = dbService.getGMCClaimCount(bean.getPolicyKey(),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		String gmcMsg = SHAConstants.GMC_CLAIM_COUNT_MESSAGE+gmcClaimCount;
		
		if(!ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(msg, buttonsNamewithType);
			homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		}else if(this.bean.getClaimCount() >2){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCriticalBox(msg+"<br>"+additionalMessage, buttonsNamewithType);
			homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			}
		}
		else
		{
			if(gmcClaimCount > 1 && gmcClaimCount <=2){
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(gmcMsg, buttonsNamewithType);
				homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			}else if(gmcClaimCount >2){
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

				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				} 
				else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) >= 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}/*else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
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
//				if (!bean.getSuspiciousPopupMap().isEmpty()
//						&& !bean.getIsPopupMessageOpened()) {
//					suspiousPopupMessage();
//				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
//				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
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


	public Boolean showRoomRentMatchingScreen() {

		if (!bean.getIsOneMapping() || !bean.getIsICUoneMapping()) {
			roomRentMatchingTableObj = roomRentMatchingTable.get();
			final ConfirmDialog dialog = new ConfirmDialog();
			roomRentMatchingTableObj.init(dialog, bean);
			List<RoomRentMatchingDTO> roomRentMappingDTOList = bean
					.getRoomRentMappingDTOList();
			List<RoomRentMatchingDTO> icuRoomRentMappingDTOList = bean
					.getIcuRoomRentMappingDTOList();
			if (!bean.getIsOneMapping()) {
				for (RoomRentMatchingDTO roomRentMatchingDTO : roomRentMappingDTOList) {
					roomRentMatchingTableObj.addBeanToList(roomRentMatchingDTO);
				}
			}
			if (!bean.getIsICUoneMapping()) {
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

			roomRentMatchingTableObj.dummyField
					.addValueChangeListener(new ValueChangeListener() {
						private static final long serialVersionUID = -901753303877184471L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							TextField field = (TextField) event.getProperty();
							dialog.close();
							if (SHAUtils.isValidInteger(field.getValue())
									&& SHAUtils.getIntegerFromString(
											field.getValue()).equals(0)) {
								isMappingDone = true;
								isMatchTheFollowing = true;
								// fireViewEvent(BillingWizardPresenter.SUBMIT_CLAIM_BILLING_MAPPING,
								// bean);
								wizard.next();
							}
						}
					});
		}
		return isMappingDone;
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

	private PedDetailsTableDTO setPEDDetailsToDTO(
			DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		if (pedList == null) {
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

			List<AddOnBenefitsDTO> addOnBenefitsFinalList = new ArrayList<AddOnBenefitsDTO>();
			List<AddOnBenefitsDTO> addOnBenefitsList = null;
			List<AddOnBenefitsDTO> addOnBenefitsPatientList = null;

			if(null != addOnBenefitsListenerTableObj)
			{
				addOnBenefitsList = addOnBenefitsListenerTableObj.getValues();
				if(null != addOnBenefitsList && !addOnBenefitsList.isEmpty())
				{
					for (AddOnBenefitsDTO addOnBenefitsDTO : addOnBenefitsList) {
						addOnBenefitsFinalList.add(addOnBenefitsDTO);
						totalApprovedAmt += addOnBenefitsDTO.getPayableAmount();
					}
				}
				/*if(null != addOnBenefitsList && !addOnBenefitsList.isEmpty())
		{
			this.bean.setAddOnBenefitsDTO(addOnBenefitsList);
		}*/
			}
			if(null != addOnBenefitsPatientCareLiseterObj)
			{
				addOnBenefitsPatientList = addOnBenefitsPatientCareLiseterObj.getValues();

				if(null != addOnBenefitsPatientList && !addOnBenefitsPatientList.isEmpty())
				{
					for (AddOnBenefitsDTO addOnBenefitsDTO : addOnBenefitsPatientList) {
						addOnBenefitsFinalList.add(addOnBenefitsDTO);
						totalApprovedAmt += addOnBenefitsDTO.getPayableAmount();
					}
				}
			}

			if(null != addOnBenefitsFinalList && !addOnBenefitsFinalList.isEmpty())
			{
				this.bean.getReceiptOfDocumentsDTO().setAddOnBenefitsDTO(addOnBenefitsFinalList);
			}
		}
	}

	private void setTableValues() {
		if (this.bean.getPreauthDataExtractionDetails()
				.getHospitalCashAddonBenefits() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getHospitalCashAddonBenefits()) {
			generateFieldsBasedOnHospitalCashBenefits(true);
		}
		if (this.bean.getPreauthDataExtractionDetails()
				.getPatientCareAddOnBenefits() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getPatientCareAddOnBenefits()) {
			generateFieldsBasedOnPatientCareBenefits(true);
		}
		if (!this.bean.getPreauthDataExtractionDetails().getTreatmentDateList()
				.isEmpty()) {
			if (treatmentListenerTableObj != null) {
				List<PatientCareDTO> treatmentDateList = this.bean
						.getPreauthDataExtractionDetails()
						.getTreatmentDateList();
				for (PatientCareDTO patientCareDTO : treatmentDateList) {
					treatmentListenerTableObj.addBeanToList(patientCareDTO);
				}
			}
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
						BillingReviewPagePresenter.CHECK_CRITICAL_ILLNESS,
						checkValue, true);
			}
		});

		cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						BillingReviewPagePresenter.BILLING_PATIENT_STATUS_CHANGED,
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
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
								createErrorBox("Admission Date should not be before the First ROD Date.", buttonsNamewithType);
						Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						isValid = false;
						
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								
							}
						});
						event.getProperty().setValue(null);
						
					}else if (!isRODDateCheck && !(SHAUtils.isDateOfAdmissionWithPolicyRange(policyFromDate,policyToDate,enteredDate))
							/*(enteredDate.after(policyFromDate) || enteredDate
							.compareTo(policyFromDate) == 0)
							|| !(enteredDate.before(policyToDate) || enteredDate
									.compareTo(policyToDate) == 0)*/) {/*
						isValid = false;
						Button okButton = new Button("OK");
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								
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
						dialog.setClosable(true);
						dialog.show(getUI().getCurrent(), null, true);
						event.getProperty().setValue(null);
					*/

						isValid = false;
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons=GalaxyAlertBox.
								createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
						Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						okButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -7148801292961705660L;
							@Override
							public void buttonClick(ClickEvent event) {
								
							}
						});
						event.getProperty().setValue(null);
						
					}
				}

				if (isValid) {
					admissionDateValue = enteredDate;
				}

			}
		});

		dischargeDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (bean.getPreauthDataExtractionDetails().getAdmissionDate() != null) {
					// Date admission = (Date) ((DateField)
					// event.getProperty()).getData();
					Date enteredDate = (Date) ((DateField) event.getProperty())
							.getValue();

					// if(enteredDate != null && admissionDate != null &&
					// admissionDate.getValue() != null){
					// if(enteredDate.before(admissionDate.getValue())){
					// admissionDate.setValue(null);
					// }
					// }
					if (enteredDate != null && admissionDate != null
							&& admissionDate.getValue() != null) {
						Long daysBetweenDate = SHAUtils.getDaysBetweenDate(
								admissionDate.getValue(), enteredDate);
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
					//
					if (enteredDate != null && admissionDate != null
							&& admissionDate.getValue() != null) {
						if (enteredDate.before(admissionDate.getValue())) {
							// event.getProperty().setValue(null);
							dischargeDate.setValue(null);
							showErrorMessage("Discharge date cannot be lesser than the date of admission");

						} else {
							if (dischargeDate.getValue() != null) {
								admissionDate.setData(dischargeDate.getValue());
							}
						}
					}
				}

			}
		});
		// if(deathDate != null){
		// deathDate.addValueChangeListener(new ValueChangeListener() {
		// private static final long serialVersionUID = -8435623803385270083L;
		//
		// @SuppressWarnings("unchecked")
		// @Override
		// public void valueChange(ValueChangeEvent event) {
		// Date enteredDate = (Date) event.getProperty().getValue();
		// if(enteredDate != null) {
		//
		// Date admission = admissionDate.getValue();
		//
		// Date discharge = dischargeDate.getValue();
		//
		// final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setClosable(false);
		// dialog.setResizable(false);
		// if(admission != null || discharge != null){
		// if (admission != null && !enteredDate.after(admission) || discharge
		// != null && !enteredDate.after(discharge)) {
		//
		// Button okButton = new Button("OK");
		// okButton.addClickListener(new ClickListener() {
		// private static final long serialVersionUID = -7148801292961705660L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// dialog.close();
		// }
		// });
		// HorizontalLayout hLayout = new HorizontalLayout(okButton);
		// hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		// hLayout.setMargin(true);
		// VerticalLayout layout = new VerticalLayout(new
		// Label("<b style = 'color: red;'>Date of Death should be greater than Admission Date and Discharge Date </b>",
		// ContentMode.HTML));
		// layout.setMargin(true);
		// layout.setSpacing(true);
		// dialog.setContent(layout);
		// dialog.setCaption("Error");
		// dialog.setClosable(true);;
		// dialog.show(getUI().getCurrent(), null, true);
		//
		// event.getProperty().setValue(null);
		// }
		// }
		// }
		//
		// }
		// });
		// }

		hospitalAddOnBenefits
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						}
						fireViewEvent(
								BillingReviewPagePresenter.BILLING_HOSPITAL_BENEFITS,
								isChecked);
					}
				});

		patientCareAddOnBenefits
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						}
						fireViewEvent(
								BillingReviewPagePresenter.BILLING_PATIENT_CARE_BENEFITS,
								isChecked);
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
						BillingReviewPagePresenter.BILLING_UPDATE_PRODUCT_BASED_AMT,
						bean);

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

	public void showWarningMsg(String eMsg) {

	/*	Label label = new Label(eMsg, ContentMode.HTML);
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

	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		if (field != null) {
			field.setRequired(true);
			field.setValidationVisible(false);
		}
	}

	public void generateFieldsBasedOnPatientStatus() {
		if (cmbPatientStatus.getValue() != null) {
			if (this.cmbPatientStatus.getValue().toString().toLowerCase()
					.contains("deceased")) {
				deathDate = (PopupDateField) binder.buildAndBind(
						"Date Of Death", "deathDate", PopupDateField.class);

				// validation added for death date.
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
				
				

				if (this.bean.getPreauthDataExtractionDetails()
						.getTerminateCoverFlag() != null
						&& this.bean.getPreauthDataExtractionDetails()
								.getTerminateCoverFlag().toLowerCase()
								.equalsIgnoreCase("y")) {
					cmbTerminateCover.setValue(this.bean
							.getPreauthDataExtractionDetails()
							.getTerminateCover());
				}

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
				setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
				mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
				addListener();
				patientStatusFLayout.addComponent(cmbTerminateCover);
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
						&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					buildNomineeLayout();
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
						getErrorMessage("Date of Death should be between of Admission Date and Discharge Date");
						event.getProperty().setValue(null);
					} else if (enteredDate != null && dischargeDate != null
							&& dischargeDate.getValue() != null) {
						if (enteredDate.after(dischargeDate.getValue())) {
							getErrorMessage("Date of Death should be between of Admission Date and Discharge Date");
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

	private void showWarningErrors() {

		/*final ConfirmDialog dialog = new ConfirmDialog();
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
						"<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Please select valid death date. Death date is not in range between admission date and current date.</b>", buttonsNamewithType);

	}

	public void showAdmissionDateError() {

		/*final ConfirmDialog dialog = new ConfirmDialog();
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Admission Date should not be greater than Death date", buttonsNamewithType);
	}

	public void showError() {

		/*final ConfirmDialog dialog = new ConfirmDialog();
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
						"<b style = 'color: red;'>Discharge date should not less than Admission Date</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Discharge date should not less than Admission Date</b>", buttonsNamewithType);
	}

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
		}
	}

	private void setCalculatedValue(Integer value1, Integer value2,
			TextField calculatedValueField) {
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

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if (uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj
					.setupCategoryValues(selectValueContainer);
		}

	}

	public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
		if (uploadDocumentListenerTableObj != null) {
			List<UploadDocumentDTO> uploadDoc = uploadDocumentListenerTableObj
					.getValues();
			List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()) {
					if (uploadDocumentDTO.getFileType().getValue()
							.contains("Bill")) {
						/*
						 * if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(
						 * uploadDTO.getBillNo())) { uploadList.add(uploadDTO);
						 * } else { uploadList.add(uploadDocumentDTO); }
						 */

						/**
						 * Sequence number is an internal parameter maintained
						 * for updating the uploadlistener table. This is
						 * because the row for which the bill is entered should
						 * only get updated. Rest of rows should be the same.
						 * Earlier this was done with bill no. But there are
						 * chance that even bill no can be duplicate. Hence
						 * removed this and added validation based on seq no.
						 * */
						if (uploadDocumentDTO.getSeqNo().equals(
								uploadDTO.getSeqNo())) {
							//uploadList.add(uploadDTO);
						} else {
							uploadList.add(uploadDocumentDTO);
						}

					} else {
						uploadList.add(uploadDocumentDTO);
					}
				}

			}
			uploadList.add(uploadDTO);
			uploadDocumentListenerTableObj.updateTable(uploadList);
		}
	}

//<<<<<<< HEAD
protected void addTotalClaimedListener() {
	this.uploadDocumentListenerTableObj.claimedAmtField
			.addValueChangeListener(new ValueChangeListener() {
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
							
//=======
//	protected void addTotalClaimedListener() {
//		this.uploadDocumentListenerTableObj.claimedAmtField
//				.addValueChangeListener(new ValueChangeListener() {
//
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						if (null != uploadDocumentListenerTableObj) {
//							String provisionAmt = (String) event.getProperty()
//									.getValue();
//							if (null != provisionAmt
//									&& !("").equalsIgnoreCase(provisionAmt)) {
//								if (SHAUtils.isValidDouble(provisionAmt)) {
//									Double value = Double.valueOf(provisionAmt);
//									bean.setAmountConsidered(String
//											.valueOf(value.intValue()));
//
//									if (!isMappingDone) {
//										bean = SHAUtils.roomRentNursingMapping(
//												bean, 8l, 9l, false);
//										bean = SHAUtils.roomRentNursingMapping(
//												bean, 10l, 11l, true);
//										getMappingDone();
//									}
//									if (bean.getIsICUoneMapping()) {
//										bean = SHAUtils.roomRentNursingMapping(
//												bean, 10l, 11l, true);
//										getMappingDone();
//									}
//									if (bean.getIsOneMapping()) {
//										bean = SHAUtils.roomRentNursingMapping(
//												bean, 8l, 9l, false);
//										getMappingDone();
//									}
//								}
//>>>>>>> roomrentdecimal
						
						}
					}
				}
			}

				});

	}

	public void setIrdaLevel2Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		if (this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setIrdaLevel2Values(
					selectValueContainer, cmb, value);
		}

	}

	public void setIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		if (this.uploadDocumentListenerTableObj != null) {
			this.uploadDocumentListenerTableObj.setIrdaLevel3Values(
					selectValueContainer, cmb, value);
		}
	}

	public Boolean alertMessage() {

		/*Label successLabel = new Label(
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
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Close Proximity Claim</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				isValid = true;
				bean.setAlertMessageOpened(true);
				wizard.next();
				//dialog.close();

			}
		});

		return isValid;

	}

	public void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne) {
		SHAUtils.fillMappingData(bean, mappingData, isInvokeForOneToOne);
	}

	public void setBenefitsData(List<AddOnBenefitsDTO> benefitsDTO) {
		// TODO Auto-generated method stub
		this.bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(
				benefitsDTO);
	}

	public void poupMessageForProduct() {
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);*/

		/*Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		

		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		//layout.addComponent(okButton);
		layout.setMargin(true);
		/*dialog.setContent(layout);
		dialog.setWidth("30%");*/
		bean.setIsPopupMessageOpened(true);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();
				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
				else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} 
					else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					} 
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}/*else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
			}
		});
		//dialog.show(getUI().getCurrent(), null, true);
	}

	public void showPopupForComparison(String comparisonString) {
		if (comparisonString.isEmpty()) {
			bean.setIsComparisonDone(true);
		} else {
			/*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);

			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

			

			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);*/
			VerticalLayout layout = new VerticalLayout(
					new Label("The following Attibutes has been changed from Previous ROD : </b>",
							ContentMode.HTML), new Label(comparisonString,
							ContentMode.HTML));
			layout.setMargin(true);
			/*dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
			Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			okButton.addClickListener(new ClickListener() {
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

	public void suspiousPopupMessage() {
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/

		

		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			/*Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);*/
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		/*layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);*/
		layout.setMargin(true);
		layout.setSpacing(true);
		/*dialog.setContent(layout);
		dialog.setWidth("30%");*/
		this.bean.setIsPopupMessageOpened(true);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();
				if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} 
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
						(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}

			}
		});
		//dialog.show(getUI().getCurrent(), null, true);
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
	
	public MessageBox showAlertMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createWarning()
			    .withCaptionCust("Warning")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
      }

	public void generateButtonFields(String eventName,
			BeanItemContainer<SelectValue> selectValueContainer) {
		this.bean.setStageKey(ReferenceTable.BILLING_STAGE);
		if (SHAConstants.REFER_TO_COORDINATOR.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setTypeOfCoordinatorRequest(null);
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
			billingProcessButtonObj
					.buildReferCoordinatorLayout(selectValueContainer);
		} else if (SHAConstants.MEDICAL_APPROVER.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean
					.setStatusKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
			billingProcessButtonObj.buildReferToMedicalApproverLayout();
		} else if (SHAConstants.FINANCIAL.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean
					.setStatusKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
			billingProcessButtonObj.buildSendToFinancialLayout();
		} else if (SHAConstants.BILLING_CANCEL_ROD.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.BILLING_CANCEL_ROD)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_CANCEL_ROD);
			billingProcessButtonObj.buildCancelRODLayout(selectValueContainer);
		}else if(SHAConstants.REFER_TO_BILL_ENTRY.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY);
			billingProcessButtonObj.buildReferToBillEntryLayout();
		}
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		uploadDocumentListenerTableObj.loadBillEntryValues(uploadDTO);
		// TODO Auto-generated method stub

	}

	public void updateProductBasedAmtDetails(Map<Integer, Object> detailsMap) {
		if (null != detailsMap) {
			if (this.uploadDocumentListenerTableObj != null) {
				uploadDocumentListenerTableObj.removeAllItems();
				uploadDocumentListenerTableObj.setReferenceData(referenceData);
				Integer i = 1;
				List<UploadDocumentDTO> uploadList = this.bean
						.getUploadDocumentDTO();
				uploadDocumentListenerTableObj.setTableInfo(uploadList);
				if (null != uploadList && !uploadList.isEmpty())
					for (UploadDocumentDTO uploadDocLayout : uploadList) {
						uploadDocLayout
								.setProductBasedRoomRent((Double) detailsMap
										.get(8));
						uploadDocLayout
								.setProductBasedICURent((Double) detailsMap
										.get(9));
						
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
						this.uploadDocumentListenerTableObj
								.addBeanToList(uploadDocLayout);
						i++;
					}
				// uploadDocumentListenerTableObj.setTableList(this.bean.getUploadDocumentDTO());
			}
		}

	}
	
	public Boolean alertForHospitalDiscount() {
   		/*Label successLabel = new Label(
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.HOSPITAL_DISCOUNT_ALERT + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} 
				}  else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				} /*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
						(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}
//				bean.setIsHospitalDiscountApplicable(false);
//				bean.setIsPedWatchList(false);
			}
		});
		return true;
	}

	 public Boolean alertMessageForPostHosp() {
	   		/*Label successLabel = new Label(
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(SHAConstants.POST_HOSP_ALERT_MESSAGE, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					bean.setShouldShowPostHospAlert(true);
					if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}   else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} 
					}  else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					} else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}  /*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
						popupMessageForWaitingPeriod();
					}*/else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
			return true;
		}
	 
	 public void get64VbChequeStatusAlert() {
	   		/*Label successLabel = new Label(
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
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(SHAConstants.VB64STATUSALERT, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					 bean.setIsPopupMessageOpened(true);

//						bean.setIsPopupMessageOpened(true);
						//dialog.close();
						/*if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
								(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
							popupMessageForWaitingPeriod();
						}else*/ if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}
				}
			});
			
		}

	 
	 public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
			// TODO Auto-generated method stub
			sectionDetailsListenerTableObj.setCoverList(coverContainer);
			
		}

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
		
	public void nonPreferredPopupMessage() {
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);

		Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
*/
		

		/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getNonPreferredPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			/*Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);*/
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		/*layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);*/
		layout.setMargin(true);
		layout.setSpacing(true);
		/*dialog.setContent(layout);
		dialog.setWidth("30%");*/
		this.bean.setIsPopupMessageOpened(true);
		
		/*HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("", buttonsNamewithType);
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		homeButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();
				if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) >= 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}   else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (!ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							     && !ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())	)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					}
				}  else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				} /*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
						(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					popupMessageForWaitingPeriod();
				}*/else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}

			}
		});
		//dialog.show(getUI().getCurrent(), null, true);
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
	protected void showPopup(final String message) {

		 String message1 = message.replaceAll("(.{200})", "$1<br />");
		 message1 = message1.replaceAll("(\r\n|\n)", "<br />");

		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" +   message1 + "</br>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		//layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		layout.setHeightUndefined();*/
		/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
			layout.setHeight("100%");
			layout.setWidth("100%");
			}
			
		}		*/	
		/*final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);*/
	
		
		/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
			if(message.length()>4000){
				dialog.setWidth("55%");
			}
		}*/
		//dialog.show(getUI().getCurrent(), null, true);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message1 + "</br>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					 	//dialog.close();
					 	if(bean.getClmPrcsInstruction()!=null && !bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
					 		showPopup(bean.getClmPrcsInstruction());
					 	}
				}
			});
   
	}
	
	
	 public void policyValidationPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			/*Button homeButton = new Button("OK");
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(SHAConstants.POLICY_VALIDATION_ALERT + "</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap()
							.isEmpty()){
						nonPreferredPopupMessage();
					}
					else if (bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}
					else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						} 
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
						popupMessageForWaitingPeriod();
					}*/else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
		}
	 
	 
	 
	 

	  public void zuaQueryAlertPopupMessage() {	 
			 
			/* Label successLabel = new Label(
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
//				dialog.setCaption("Alert");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View ZUA History");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(SHAConstants.ZUA_QUERY_ALERT + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				Button zuaViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
							suspiousPopupMessage();
						}else if(!bean.getNonPreferredPopupMap()
								.isEmpty()){
							nonPreferredPopupMessage();
						}
						else if (bean.getIsPEDInitiated()) {
//							alertMessageForPED();
							if(bean.isInsuredDeleted()){
								alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
							}else{
								alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
							}
						} else if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}
						 else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
								 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
											&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							} 
							else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
							}
							else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							}
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						}else if(bean.getIs64VBChequeStatusAlert()){
							get64VbChequeStatusAlert();
						}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
								(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
							popupMessageForWaitingPeriod();
						}*/else if(bean.getIsSuspicious()!=null){
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
			}
	  
	  private void showICRMessage(){
		 String msg = SHAConstants.ICR_MESSAGE;
		 /* Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

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
		  dialog.show(getUI().getCurrent(), null, true);*/
		  HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(msg+"</b></div>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
		  homeButton.addClickListener(new ClickListener() {
			  private static final long serialVersionUID = 7396240433865727954L;

			  @Override
			  public void buttonClick(ClickEvent event) {
				  bean.setIsPopupMessageOpened(true);
				 // dialog.close();

				  if(bean.getIsPolicyValidate()){
					  policyValidationPopupMessage();
				  }else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					  poupMessageForProduct();
				  }else if(bean.getClaimCount() >1){
					  alertMessageForClaimCount(bean.getClaimCount());
				  }else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					  suspiousPopupMessage();
				  }else if(!bean.getNonPreferredPopupMap()
						  .isEmpty()){
					  nonPreferredPopupMessage();
				  }
				  else if (bean.getIsPEDInitiated()) {
//					  alertMessageForPED();
					  if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					  }else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					  }
				  } else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				  } else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					  alertMessageForPostHosp();
				  } else if(bean.getIsHospitalDiscountApplicable()){
					  alertForHospitalDiscount();
				  } else if(bean.getIsAutoRestorationDone()) {
					  alertMessageForAutoRestroation();
				  }
				  else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						} 
						
					}else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					  StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				  }else if(bean.getIs64VBChequeStatusAlert()){
					  get64VbChequeStatusAlert();
				  }/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
					  popupMessageForWaitingPeriod();
					}*/else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}

			  }
		  });
	  }
	
	  public void alertMsgForCancelRod(){
			 
			 /*Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.ALERT_FOR_CANCEL_ROD + "</b>",
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
						.createAlertBox(SHAConstants.ALERT_FOR_CANCEL_ROD + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
					}
				});
			}
	  
	  public void paayasClaimManualyProcessedAlertMessage(String insuredName) {	 
			 
			 /*Label successLabel = new Label(
						"<b style = 'color: red;'>" + "Claim processed outside the system for the insured-"+ insuredName + " Verify  and restirict sum insured and other benefits  before proceeding "+ "</b>",
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
						.createAlertBox("Claim processed outside the system for the insured-"+ insuredName + " Verify  and restirict sum insured and other benefits  before proceeding "+ "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						else if(bean.getIsZUAQueryAvailable()){
							zuaQueryAlertPopupMessage();
						}
						else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
							suspiousPopupMessage();
						}else if(!bean.getNonPreferredPopupMap()
								.isEmpty()){
							nonPreferredPopupMessage();
						}
						else if (bean.getIsPEDInitiated()) {
//							alertMessageForPED();
							if(bean.isInsuredDeleted()){
								alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
							}else{
								alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
							}
						} else if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}
						else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
								 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
											&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							} 
							else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							} 
							
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						}else if(bean.getIs64VBChequeStatusAlert()){
							get64VbChequeStatusAlert();
						}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
								(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
							popupMessageForWaitingPeriod();
						}*/else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}

					}
				});
			}
	  
	  public void getHospitalCategory(String hospitalCategory) {	 
			 
			/* Label successLabel = new Label(
						"<b style = 'color: red;'>" + hospitalCategory + " Category Hospital"+ "</b>",
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
						.createInformationBox(hospitalCategory + " Category Hospital"+ "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;
		
					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
							paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
						}	
						else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						else if(bean.getIsZUAQueryAvailable()){
							zuaQueryAlertPopupMessage();
						}
						else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
							suspiousPopupMessage();
						}else if(!bean.getNonPreferredPopupMap()
								.isEmpty()){
							nonPreferredPopupMessage();
						}
						else if (bean.getIsPEDInitiated()) {
//							alertMessageForPED();
							if(bean.isInsuredDeleted()){
								alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
							}else{
								alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
							}
						} else if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}
						else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
								 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (!ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
											&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							} 
							else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
							} 
							
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						}else if(bean.getIs64VBChequeStatusAlert()){
							get64VbChequeStatusAlert();
						}/*else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
								(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
							popupMessageForWaitingPeriod();
						}*/else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
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
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
						showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap()
							.isEmpty()){
						nonPreferredPopupMessage();
					}
					else if (bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}
					else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						} 
						
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) &&
							(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD))){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
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
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
						showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap()
							.isEmpty()){
						nonPreferredPopupMessage();
					}
					else if (bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}
					 else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						} 
						
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}/*else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageForWaitingPeriod();
					}*/else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
					
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
	VerticalLayout layout = new VerticalLayout(txtArea);
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
	homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}
				else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null &&
						(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
								bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
					showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
				}
				else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
					showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
				}
				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}	
				else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){		
					policyValidationPopupMessage();
				}
				else if(bean.getIsZUAQueryAvailable()){
					zuaQueryAlertPopupMessage();
				}
				else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getClaimCount() >1){
					alertMessageForClaimCount(bean.getClaimCount());
				}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap()
						.isEmpty()){
					nonPreferredPopupMessage();
				}
				else if (bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					alertMessageForPostHosp();
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
				else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
						 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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

				if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}
				else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null &&
						(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
								bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
					showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
				}
				else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
					showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
				}
				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}	
				else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){		
					policyValidationPopupMessage();
				}
				else if(bean.getIsZUAQueryAvailable()){
					zuaQueryAlertPopupMessage();
				}
				else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getClaimCount() >1){
					alertMessageForClaimCount(bean.getClaimCount());
				}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				}else if(!bean.getNonPreferredPopupMap()
						.isEmpty()){
					nonPreferredPopupMessage();
				}
				else if (bean.getIsPEDInitiated()) {
					alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
				} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					alertMessageForPostHosp();
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
				 else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {

					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					} 
					else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
							&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
					} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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

  
	  }
	  public void popupMessageFor30DaysWaitingPeriod() {
			Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
		    if((diffDays != null && diffDays < 30)){
		    	/* Label successLabel = new Label(
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
//		    		dialog.setCaption("Alert");
		    		dialog.setClosable(false);
		    		dialog.setContent(layout);
		    		dialog.setResizable(false);
		    		dialog.setModal(true);
		    		dialog.show(getUI().getCurrent(), null, true);*/
		    	
		    	//GLX2020168
				Date currnetDate = new Date();
				boolean dateRange= SHAUtils.validateRangeDate(policyFromDate);
				boolean currentDateRange= SHAUtils.validateRangeDate(currnetDate);
				
				Boolean is15DaysWaitingPeriod = false;
					
		 		if((diffDays != null && diffDays < 30)
		 				&&(ReferenceTable.getCovidProducts().contains(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
		 			
	 				if(!this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()&& this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() !=null){
	 				List<DiagnosisDetailsTableDTO> diagnosisList =this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
		 				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
		 					
		 					if(diagnosisDetailsTableDTO.getIcdCode()!=null && diagnosisDetailsTableDTO.getIcdCode().getValue() !=null){
		 						
		 						String insuranceDiagnosisCode = diagnosisDetailsTableDTO.getIcdCode().getValue();
		 						System.out.println("insuranceDiagnosisCode.."+insuranceDiagnosisCode);
		 						
		 						if(ReferenceTable.getCovidInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
		 							
		 							is15DaysWaitingPeriod=true;
		 							
		 						}
		 					}
		 				}
		 			}
		 			
		 		}
				
				if(is15DaysWaitingPeriod && dateRange && currentDateRange && ReferenceTable.getCovidProducts().contains(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) && (diffDays != null && diffDays < 30)){
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createAlertBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG, buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
				}else{

		    		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createAlertBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT + "</b>", buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
		    		homeButton.addClickListener(new ClickListener() {
		    			private static final long serialVersionUID = 7396240433865727954L;

		    			@Override
		    			public void buttonClick(ClickEvent event) {
		    				// bean.setIsPopupMessageOpened(true);
		    				 
		   					bean.setIsPopupMessageOpened(true);
		    					//dialog.close();
		    					 if(bean.getIsSuspicious()!=null){
									StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
								}
		    	}
			});
		    }
		    }
		}

	//Covid 19 GLX2020086
	  public void popupMessageForWaitingPeriod() {

	 		Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
	 		Date admissionDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
	 		String productCode=bean.getNewIntimationDTO().getPolicy().getProduct().getCode();
	 		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
	 		// GLX2020168
	 		/*Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("01-07-2020");
	 		Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("30-09-2020");*/
	 		Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("09-12-2020");
			Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("31-03-2021");
	 		Date currnetDate = new Date();
	 		
	 		Boolean is15DaysWaitingPeriod = false;
	 					
	 		if((diffDays != null && diffDays < 15)
	 				//&&(ReferenceTable.getCovidSpecifiedProducts().contains(productCode))
	 				&&(ReferenceTable.getCovidProducts().contains(productCode))
	 				&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, policyFromDate))
	 				&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, currnetDate))){
	 			
	 			//if(this.diagnosisDetailsTableObj != null && !this.diagnosisDetailsTableObj.getValues().isEmpty()){
 				if(!this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()&& this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() !=null){
 				List<DiagnosisDetailsTableDTO> diagnosisList =this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
	 				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
	 					
	 					if(diagnosisDetailsTableDTO.getIcdCode()!=null && diagnosisDetailsTableDTO.getIcdCode().getValue() !=null){
	 						
	 						String insuranceDiagnosisCode = diagnosisDetailsTableDTO.getIcdCode().getValue();
	 						System.out.println("insuranceDiagnosisCode.."+insuranceDiagnosisCode);
	 						
	 						if(ReferenceTable.getCovidInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
	 							
	 							is15DaysWaitingPeriod=true;
	 							
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
	 						}
	 					}
	 				}
	 			}
	 			
	 		}
	 		
	 		if(!(is15DaysWaitingPeriod)){
	 			popupMessageFor30DaysWaitingPeriod();
	 		}

	 	}

	  
	  public void showAlertForGMCParentLink(String policyNumber){	 
			 
		   /* Label successLabel = new Label(
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
					.createInformationBox("Policy is  Linked to Policy No " + policyNumber + "</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
						showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap()
							.isEmpty()){
						nonPreferredPopupMessage();
					}
					else if (bean.getIsPEDInitiated()) {
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}
					else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
		}
	  
		public void showAlertForGMCPaymentParty(String paymentParty,String proposerName,String mainMemberName){	 
		/*	Label successLabel = null;
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
			dialog.show(getUI().getCurrent(), null, true);*/
			String successLabel=null;
			if(paymentParty != null && paymentParty.equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_CORPORATE)){
			   successLabel = new String("Payment Party is Corporate - Payment to be made to " + proposerName) ;
			} else {
				successLabel = new String("Payment Party is Main Member - Payment to be made to " + mainMemberName);
			}
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(successLabel, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
						suspiousPopupMessage();
					}else if(!bean.getNonPreferredPopupMap()
							.isEmpty()){
						nonPreferredPopupMessage();
					}
					else if (bean.getIsPEDInitiated()) {
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}
					else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						} 
						else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
								&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
						} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
						} 
						
					}else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
			
				}
			});
		}
		
		public void showCMDAlert(String memberType) {	 
			 
			 /*Label successLabel = new Label(
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(SHAConstants.CMD_ALERT_LATEST + memberType + " Club" + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
							showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
						}
						else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
							showDuplicateInsured(bean.getDuplicateInsuredList());
						}
						else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null &&
								(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
										bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
							showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
						}
						else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
							showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
						}
						else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
							getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
						}
						else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
							paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
						}	
						else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						else if(bean.getIsZUAQueryAvailable()){
							zuaQueryAlertPopupMessage();
						}
						else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
							suspiousPopupMessage();
						}else if(!bean.getNonPreferredPopupMap()
								.isEmpty()){
							nonPreferredPopupMessage();
						}
						else if (bean.getIsPEDInitiated()) {
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}
						else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
								 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
											&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							} 
							else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
			}
		 /*public void buildNomineeLayout(){
			 
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

					if(billingProcessButtonObj != null) {
						wholeVLayout.removeComponent(billingProcessButtonObj);
					}

					nomineeDetailsTable = nomineeDetailsTableInstance.get();
					
					nomineeDetailsTable.init("", false, false);
					
					if(bean.getNewIntimationDTO().getNomineeList() != null) {
						nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
						nomineeDetailsTable.generateSelectColumn();
					}
					
					wholeVLayout.addComponent(nomineeDetailsTable);
				
					boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
							
					legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
				
					if(enableLegalHeir) {
						nomineeDetailsTable.setLegalHeirDetails(
						bean.getNewIntimationDTO().getNomineeName(),
						bean.getNewIntimationDTO().getNomineeAddr());
					}	
					
					wholeVLayout.addComponent(legaHeirLayout);	
					
					if(billingProcessButtonObj != null) {
						 wholeVLayout.addComponent(billingProcessButtonObj);
				 	}
				}

			}*/		
		public void buildNomineeLayout(){
			 
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

				if(nomineeDetailsTable != null) {
					wholeVLayout.removeComponent(nomineeDetailsTable);
				}
				
				if(legalHeirLayout != null) {
					wholeVLayout.removeComponent(legalHeirLayout);
				}
				
				if(billingProcessButtonObj != null) {
					wholeVLayout.removeComponent(billingProcessButtonObj);
				}

				nomineeDetailsTable = nomineeDetailsTableInstance.get();
				
				nomineeDetailsTable.init("", false, false);
				
				if(bean.getNewIntimationDTO().getNomineeList() != null) {
					nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
					nomineeDetailsTable.generateSelectColumn();
					nomineeDetailsTable.setScreenName(SHAConstants.BILLING_SCREEN);
					nomineeDetailsTable.setPolicySource(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
					nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);
				}
							
					wholeVLayout.addComponent(nomineeDetailsTable);
						
					boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
								
					legalHeirLayout = new VerticalLayout();
					
					legalHeirDetails = legalHeirObj.get();
					
					relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					Map<String,Object> refData = new HashMap<String, Object>();
					relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
					refData.put("relationship", relationshipContainer);
					legalHeirDetails.setReferenceData(refData);
					legalHeirDetails.init(bean);
					legalHeirDetails.setPresenterString(SHAConstants.BILLING_SCREEN);
					legalHeirLayout.addComponent(legalHeirDetails);
					wholeVLayout.addComponent(legalHeirLayout);

					if(enableLegalHeir) {
						
						legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
						legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
						legalHeirDetails.getBtnAdd().setEnabled(true);
					}
					else {
						legalHeirDetails.deleteRows();
						legalHeirDetails.getBtnAdd().setEnabled(false);
					}
					
				if(billingProcessButtonObj != null) {
					 wholeVLayout.addComponent(billingProcessButtonObj);
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
		 public void showAlertForCoInsurance(String memberType){
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
						
						if(null != memberType && !memberType.isEmpty()){
							showCMDAlert(memberType);
						}
						else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
							showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
						}
						else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
							showDuplicateInsured(bean.getDuplicateInsuredList());
						}
						else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null &&
								(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
										bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
							showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
						}
						else if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()){
							showAlertForGMCPaymentParty(bean.getPolicyDto().getPaymentParty(),bean.getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
						}
						else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
							getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
						}
						else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
							paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
						}	
						else if(masterService.doGMCPolicyCheckForICR(bean.getNewIntimationDTO().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						else if(bean.getIsZUAQueryAvailable()){
							zuaQueryAlertPopupMessage();
						}
						else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if (!bean.getSuspiciousPopupMap().isEmpty()) {
							suspiousPopupMessage();
						}else if(!bean.getNonPreferredPopupMap()
								.isEmpty()){
							nonPreferredPopupMessage();
						}
						else if (bean.getIsPEDInitiated()) {
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}
						else if((ReferenceTable.getLumsumAlertApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
								 bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY))) {

							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
											&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							} 
							else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)
									&& (ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								warningMessageForLumpsum(SHAConstants.CORONA_WARNING_MESSAGE);
							} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
			    MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
			    SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);
				
			 }
		 }
		 
	public void getAlertForKavachCoronaAlert() {
		if (bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getAdmissionDate() != null) {
			Long alertDte = null;
			Date policyStartDate = bean.getNewIntimationDTO().getPolicy()
					.getPolicyFromDate();

			Date admissionDate = this.bean.getPreauthDataExtractionDetails()
					.getAdmissionDate();

			alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
			Long totalAlertDte = alertDte + 1;
			MastersValue policyType = bean.getNewIntimationDTO().getPolicy()
					.getPolicyType();
			SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);

		}
	}

	public void getWarningRakshaAlert() {
		if (bean.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getAdmissionDate() != null) {
			Long alertDte = null;
			Date policyStartDate = bean.getNewIntimationDTO().getPolicy()
					.getPolicyFromDate();

			Date admissionDate = this.bean.getPreauthDataExtractionDetails()
					.getAdmissionDate();

			alertDte = SHAUtils.getDiffDays(policyStartDate, admissionDate);
			Long totalAlertDte = alertDte + 1;
			if (totalAlertDte < 15) {
				SHAUtils.popupMessageForWaitingPeriodForNovelCorona(totalAlertDte);
			}
			MastersValue policyType = bean.getNewIntimationDTO().getPolicy()
					.getPolicyType();

		}
	}
	
		 //Added for product 076
		 private void setTableValuesForHospCash()
			{
				if(null != this.addOnBenefitsListenerTableObj && !(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) 
						|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
				{
					if(null != this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO() && !this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO().isEmpty())
					{
						for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO()) {
							if((ReferenceTable.HOSPITAL_CASH).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
									{
										addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
									}
								
						}
					}
				}
				if(null != this.addOnBenefitsPatientCareLiseterObj && !(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) 
						|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
				{
					if(null != this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO() && !this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO().isEmpty())
					{
						for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO()) {
							if((ReferenceTable.PATIENT_CARE).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
									{
										
										addOnBenefitsPatientCareLiseterObj.addBeanToList(addOnBenefitsDTO);
									}
						}
					}
				}
				
				if(null != this.addOnBenefitsListenerTableObj && (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
				{
					if(null != this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO() && !this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO().isEmpty())
					{
						for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getReceiptOfDocumentsDTO().getAddOnBenefitsDTO()) {
							
										addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
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
