package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.preauth.PreauthCoordinatorView;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.SpecialityTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForDataExtractionPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestFileUploadUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimsSubmitHandler;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.OtherClaimDetails;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.OtherClaimDiagnosisDetails;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.UpdateHospitalDetails;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.cashless.listenertables.PADiganosisDetailsListenerForPremedical;
import com.shaic.paclaim.cashless.listenertables.PAProcedureListenerTableForPremedical;
import com.shaic.paclaim.cashless.listenertables.PASectionDetailsListenerTable;
import com.shaic.paclaim.health.reimbursement.listenertable.PANewProcedureTableList;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionButtonsForFirstPage;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
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
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthClaimRequestDataExtractionPageUI extends ViewComponent implements ClaimsSubmitHandler{

	private static final long serialVersionUID = -5362064388584784817L;

	@Inject
	private PreauthDTO bean;
	
	private Window popup;
	
	private GWizard wizard;

	@Inject
	private Instance<PreauthCoordinatorView> reimbursementCoordinatorView;

	@Inject
	private Instance<PAProcedureListenerTableForPremedical> procedureTableList;
	
	@Inject
	private Instance<UpdateHospitalDetails> hospitalDetailsInstance;

	@Inject
	private Instance<SpecialityTable> specialityTableList;

	@Inject
	private Instance<PADiganosisDetailsListenerForPremedical> diagnosisDetailsTable;
	
	@Inject
	private Instance<PANewProcedureTableList> newProcedureTableList;
	
	@Inject
	private Instance<OtherClaimDetails> otherClaimDetailsInstance;
	
	@Inject
	private Instance<OtherClaimDiagnosisDetails> otherClaimTable;
	
	@Inject
	private Instance<PASectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private PASectionDetailsListenerTable sectionDetailsListenerTableObj;

	private PAProcedureListenerTableForPremedical procedurdTableObj;
	
	private UpdateHospitalDetails hospitalDetailsObj;

	private PANewProcedureTableList newProcedurdTableObj;
	
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
	
	private OptionGroup workOrNonWorkSpace;
	
	private TextField noOfDaysTxt;

	private ComboBox cmbNatureOfTreatment;

	private PopupDateField firstConsultantDate;

	private CheckBox corpBufferChk;

	private TextField automaticRestorationTxt;

	private CheckBox criticalIllnessChk;

	private ComboBox cmbSpecifyIllness;

	private ComboBox cmbRoomCategory;
	
	private OptionGroup ventilatorSupportOption;

	private ComboBox cmbTreatmentType;

	private TextArea treatmentRemarksTxt;

	private ComboBox cmbIllness;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;
	
	private TextField systemOfMedicineTxt;

	private ComboBox cmbHospitalisationDueTo;
	
	private DateField injuryDate;
	
	private OptionGroup medicalLegalCase;
	
	private OptionGroup reportedToPolice;
	
	private OptionGroup coveredPreviousClaim;
	
	private OptionGroup isHospitalized;
	
	private TextField preHospitalisationPeriod;
	
	private TextField postHospitalisationPeriod;
	
	private OptionGroup domicillaryHospitalisation;
	
	private ComboBox cmbCauseOfInjury;
	
	private DateField diseaseDetectedDate;
	
	private DateField deliveryDate;

	private FormLayout firstFLayout;
	
	private VerticalLayout otherClaimsFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	private OptionGroup changeInDiagnosis;
	
	VerticalLayout tableVLayout;

	private SpecialityTable specialityTableObj;

	private PADiganosisDetailsListenerForPremedical diagnosisDetailsTableObj;

	private Boolean processId;

	private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

	private HorizontalLayout dynamicElementsHLayout;

	private FormLayout treatmentFLayout;

	private FormLayout patientStatusFLayout;
	
	private Integer changeIndiaganosisRows = 0;
	
	private Boolean changeInDiagSelected = false;
	
	/// ------ Newly added fields --------////
	
		private ComboBox cmbSection;
		
		private TextField hospitalizationNoOfDays;
		
		private ComboBox cmbTypeOfDelivery;
		
		private DateField treatmentStartDate;
		
		private DateField treatmentEndDate;
		
		// -----------------------------------////
	
	private BeanItemContainer<State> container = new BeanItemContainer<State>(
			State.class);
	
	//Added for reason for date change.
	private TextField reasonForChangeInDOA;

	private TextField firNumberTxt;

	private OptionGroup policeReportedAttached;
	
	private Date admissionDateValue;
	
	private Boolean isValid = false;
	
	private OptionGroup preExistingDisabilities;
	
	private TextArea txtDisablitiesRemarks;

	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<PAHealthClaimRequestMedicalDecisionButtonsForFirstPage> paClaimRequestDataExtractionButtonInstance;
	
	private PAHealthClaimRequestMedicalDecisionButtonsForFirstPage paClaimRequestDataExtractionButtonObj;
	
	private ClaimRequestFileUploadUI specialistWindow;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@EJB
	private MasterService masterService;
	
	private CheckBox chkNomineeDeceased;
	
	@Override
	public String getCaption() {
		return "Data Extraction";
	}

	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		specialistWindow = new ClaimRequestFileUploadUI();
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
				if(bean.getIsPedWatchList()){
					alertMessageForPEDWatchList();
				} else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
					
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}
			}
		});
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
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}
			}
		});
		*/
		return true;
	}
	
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
				bean.setIsPedWatchList(false);
				if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
					
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}
			}
		});
		return true;
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
//				bean.setIsPedWatchList(false);
				
				if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
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
		else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
			suspiousPopupMessage();
		} else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if(bean.getIsPedWatchList()){
			alertMessageForPEDWatchList();
		} else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
			alertMessageForPostHosp();
		} else if(bean.getIsHospitalDiscountApplicable()){
			alertForHospitalDiscount();
		} else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
			if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
				StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
			} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
				warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
			}
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

		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
			}
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
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.SARAL_SURAKSHA_CARE_PRODUCT_CODE)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.PA_SARAL_HOSPITALISATION_ALERT,"Information");
		}
		if(bean.getIsHospitalExpenseCoverSelected()) {
			SHAUtils.showMessageBoxWithCaption(SHAConstants.PA_SARAL_HOSPITALISATION_APPROVE_DISABLE_ALERT,"Information");
		}
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setRequired(true);
		dischargeDate = (DateField) binder.buildAndBind(
				"Date of Discharge", "dischargeDate", DateField.class);
		
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		
		systemOfMedicineTxt = (TextField)binder.buildAndBind(
				"System of Medicine", "systemOfMedicine", TextField.class);
		systemOfMedicineTxt.setMaxLength(50);
		CSValidator systemOfMedicineValidator = new CSValidator();
		systemOfMedicineValidator.extend(systemOfMedicineTxt);
		systemOfMedicineValidator.setRegExp("^[a-zA-Z 0-9]*$");
		systemOfMedicineValidator.setPreventInvalidTyping(true);
		
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
	
		
		cmbHospitalisationDueTo = (ComboBox) binder.buildAndBind(
				"Hospitalisation Due to", "hospitalisationDueTo", ComboBox.class);
		
		reasonForChangeInDOA = (TextField)binder.buildAndBind(
				"Reason For Change in DOA", "changeOfDOA", TextField.class);
		reasonForChangeInDOA.setVisible(false);
		reasonForChangeInDOA.setMaxLength(100);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getChangeOfDOA() && !("").equals(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA()))
		{
			admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
			reasonForChangeInDOA.setValue(this.bean.getPreauthDataExtractionDetails().getChangeOfDOA());
			//binder.bind(reasonForChangeInDOA, "changeOfDOA");
			reasonForChangeInDOA.setVisible(true);
			reasonForChangeInDOA.setRequired(true);
		}
		
		noOfDaysTxt = (TextField) binder.buildAndBind("Length of Stay", "noOfDays",
				TextField.class);
		noOfDaysTxt.setRequired(true);
		noOfDaysTxt.setMaxLength(4);
		addLengthOfStayValueChangeListner();
		cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
				"Nature of Treatment", "natureOfTreatment", ComboBox.class);
		cmbNatureOfTreatment.setRequired(true);
		firstConsultantDate = (PopupDateField) binder.buildAndBind(
				"1st Consultation Date", "firstConsultantDate",
				PopupDateField.class);
		corpBufferChk = (CheckBox) binder.buildAndBind("", "corpBuffer",
				CheckBox.class);
		//Added for corp buffer checkbox validation.
		editCorpBufferChk();
		
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
		cmbTreatmentType = (ComboBox) binder.buildAndBind("Treatment Type",
				"treatmentType", ComboBox.class);
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
		automaticRestorationTxt = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		automaticRestorationTxt.setEnabled(false);
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt.getValue()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt.getValue()))
		{
			cmbIllness.setEnabled(false);
		}
		
		changeInDiagnosis = (OptionGroup) binder.buildAndBind( "Change In Diagnosis", "changeInDiagnosis", OptionGroup.class);
		
//		changeInDiagnosis = new OptionGroup("Change In Diagnosis");
		changeInDiagnosis.addItems(getReadioButtonOptions());
		changeInDiagnosis.setItemCaption(true, "Yes");
		changeInDiagnosis.setItemCaption(false, "No");
		changeInDiagnosis.setValue(true);
		changeInDiagnosis.setStyleName("horizontal");
		
		if(!this.bean.getIsCashlessType()) {
			changeInDiagnosis.setEnabled(false);
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getChangeInDiagnosis() != null){
			changeInDiagnosis.setValue(this.bean.getPreauthDataExtractionDetails().getChangeInDiagnosis());
		}
				
		
		
		cmbSection = (ComboBox) binder.buildAndBind(
				"Section", "section", ComboBox.class);
		
		if(this.bean.getNewIntimationDTO() != null && !ReferenceTable.getSectionKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}
		
		if(cmbSection != null && cmbSection.getValue() != null){
			this.bean.getPreauthDataExtractionDetails().setSection((SelectValue) cmbSection.getValue());
		}		
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		illnessFLayout.setMargin(false);
		FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		bufferFLayout.setCaption("Corp Buffer");
		bufferFLayout.setMargin(false);
		
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

		firstFLayout = new FormLayout(admissionDate, reasonForChangeInDOA,
				cmbRoomCategory,ventilatorSupportOption, illnessFLayout, cmbSpecifyIllness, dischargeDate, systemOfMedicineTxt,domicillaryHospitalisation, cmbHospitalisationDueTo);
		firstFLayout.setSpacing(true);
	//	firstFLayout.setMargin(true);
		secondFLayout = new FormLayout(reasonForAdmissionTxt,workOrNonWorkSpace, noOfDaysTxt,
				cmbNatureOfTreatment, firstConsultantDate, bufferFLayout,
				automaticRestorationTxt, cmbIllness, cmbSection,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		
		if(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation() != null && this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation()) {
			generatedFieldsBasedOnDomicillaryHosp(true);
		}

		PADiganosisDetailsListenerForPremedical diagnosisDetailsTableInstance = diagnosisDetailsTable.get();
		diagnosisDetailsTableInstance.init(this.bean, SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);
		this.diagnosisDetailsTableObj = diagnosisDetailsTableInstance;

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
	//	formHLayout.setWidth("100%");
		formHLayout.setSpacing(true);
		//formHLayout.setMargin(true);
		PreauthCoordinatorView preauthCoordinatorViewInstance = reimbursementCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(this.wizard, SHAConstants.PA_CLAIM_REQUEST);
//		preauthCoordinatorViewInstance.setWizard(this.wizard , SHAConstants.PRE_AUTH);
		this.reimbursementCoordinatorViewInstance = preauthCoordinatorViewInstance;
		preauthCoordinatorViewInstance.init(this.bean);
		

		tableVLayout = new VerticalLayout();
		tableVLayout.setSpacing(true);
		
		treatmentFLayout = new FormLayout(cmbTreatmentType);
		treatmentFLayout.setMargin(false);
		patientStatusFLayout = new FormLayout(cmbPatientStatus);
		patientStatusFLayout.setMargin(false);
		dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout,
				patientStatusFLayout);
		dynamicElementsHLayout.setWidth("100%");
		dynamicElementsHLayout.setMargin(false);
		
		coveredPreviousClaim = (OptionGroup) binder.buildAndBind(
				"Previously Covered by any other Mediclaim / Health Insurance", "coveredPreviousClaim", OptionGroup.class);
		
		coveredPreviousClaim.addItems(getReadioButtonOptions());
		coveredPreviousClaim.setItemCaption(true, "Yes");
		coveredPreviousClaim.setItemCaption(false, "No");
		coveredPreviousClaim.select(false);
		coveredPreviousClaim.setStyleName("horizontal");
		
	
		preHospitalisationPeriod = (TextField) binder.buildAndBind(
				"Pre-Hospitalisation Period(days)", "preHospitalisationPeriod", TextField.class);
		preHospitalisationPeriod.setMaxLength(10);
		preHospitalisationPeriod.setWidth("40px");
		CSValidator preHospitalisationPeriodValidator = new CSValidator();
		preHospitalisationPeriodValidator.extend(preHospitalisationPeriod);
		preHospitalisationPeriodValidator.setRegExp("^[0-9]*$");
		preHospitalisationPeriodValidator.setPreventInvalidTyping(true);
		
		postHospitalisationPeriod = (TextField) binder.buildAndBind(
				"Post-Hospitalisation Period(days)", "postHospitalisationPeriod", TextField.class);
		postHospitalisationPeriod.setMaxLength(10);
		postHospitalisationPeriod.setWidth("40px");
		
		CSValidator postHospitalisationPeriodValidator = new CSValidator();
		postHospitalisationPeriodValidator.extend(postHospitalisationPeriod);
		postHospitalisationPeriodValidator.setRegExp("^[0-9]*$");
		postHospitalisationPeriodValidator.setPreventInvalidTyping(true);
		
		

		otherClaimsFLayout = new VerticalLayout();
		
		if(this.bean.getPreHospitalizaionFlag() || this.bean.getPostHospitalizaionFlag()) {
//			admissionDate.setEnabled(false);
//			dischargeDate.setEnabled(false);
		} 
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null && bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//			admissionDate.setEnabled(false);
//			dischargeDate.setEnabled(false);
		}
		
		if(admissionDate != null && admissionDate.getValue() != null) {
//			admissionDate.setEnabled(false);
		}
		
		if(dischargeDate != null && dischargeDate.getValue() != null) {
//			dischargeDate.setEnabled(false);
		}
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.CLAIM_REQUEST);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());
		
		HorizontalLayout preandPostHospitalLayout = new HorizontalLayout(new FormLayout(preHospitalisationPeriod), new FormLayout(postHospitalisationPeriod));
		preandPostHospitalLayout.setSpacing(true);
		
		hospitalDetailsObj = hospitalDetailsInstance.get();
		hospitalDetailsObj.initView(this.bean);
		
		FormLayout fLayout = new FormLayout(changeInDiagnosis);
		fLayout.setSpacing(false);
		fLayout.setMargin(false);
		FormLayout fLayout1 = new FormLayout(coveredPreviousClaim);
		fLayout1.setSpacing(false);
		fLayout1.setMargin(false);
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			/*As per satish sir instruction change in diagnosis removed from layout i.e fLayout*/
			wholeVLayout = new VerticalLayout(formHLayout,/* fLayout,*/ sectionDetailsListenerTableObj, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, fLayout1, otherClaimsFLayout, preandPostHospitalLayout, preauthCoordinatorViewInstance);
		}else{
			wholeVLayout = new VerticalLayout(formHLayout,/* fLayout,*/ sectionDetailsListenerTableObj, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, fLayout1, otherClaimsFLayout, preandPostHospitalLayout, hospitalDetailsObj, preauthCoordinatorViewInstance);
		}

		wholeVLayout.setSpacing(false);
		
		addListener();
		addAdmissionDateChangeListener();
		addDiagnosisNameChangeListener();
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(cmbTreatmentType);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(dischargeDate);
		//mandatoryFields.add(cmbCatastrophicLoss);
		mandatoryFields.add(cmbNatureOfLoss);
		mandatoryFields.add(cmbCauseOfLoss);
		
		if(this.bean.getPreauthDataExtractionDetails().getCoveredPreviousClaim() != null && this.bean.getPreauthDataExtractionDetails().getCoveredPreviousClaim()) {
			coveredPreviousClaim.setValue(true);
			generatedFieldsBasedOnOtherClaims(true);
		}
		
		admissionDateValue = admissionDate.getValue();
		
		admissionDate.setValue(bean.getPreauthDataExtractionDetails().getAdmissionDate());
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}
		
		showOrHideValidation(false);
		
		if(null != bean.getPreauthDataExtractionDetails().getPreExistingDisabilities())
		{
			Boolean value = bean.getPreauthDataExtractionDetails().getPreExistingDisabilities();
			if(null != value && null != preExistingDisabilities)
			{
				preExistingDisabilities.setValue(!value);
				preExistingDisabilities.setValue(value);
			}
		}
		
		paClaimRequestDataExtractionButtonObj =  paClaimRequestDataExtractionButtonInstance.get();
		paClaimRequestDataExtractionButtonObj.initView(this.bean, this.wizard);
		paClaimRequestDataExtractionButtonObj.setReferenceData(referenceData);
		wholeVLayout.addComponent(paClaimRequestDataExtractionButtonObj);
		
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
//		reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		//Setting this feild readOnly true, since this needs to be editable as per sathish sir.
		reasonForAdmissionTxt.setReadOnly(true);
		reasonForAdmissionTxt.setEnabled(false);
		
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
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> causeOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("causeOfLoss");

		this.bean.getPolicyDto().setAdmissionDate(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
//		fireViewEvent(PreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, this.bean);
		
		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");
		
		cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalisationDueTo.setItemCaptionPropertyId("value");
		cmbHospitalisationDueTo.setEnabled(false);

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");
		
		/*cmbCatastrophicLoss.setContainerDataSource(catastrophicLoss);
		cmbCatastrophicLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCatastrophicLoss.setItemCaptionPropertyId("value");*/
		
		cmbNatureOfLoss.setContainerDataSource(natureOfLoss);
		cmbNatureOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfLoss.setItemCaptionPropertyId("value");
		
		cmbCauseOfLoss.setContainerDataSource(causeOfLoss);
		cmbCauseOfLoss.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCauseOfLoss.setItemCaptionPropertyId("value");	
		
//		if(null != this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() && this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null && this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase().equalsIgnoreCase("n")) {
//			List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
//			List<SelectValue> allopathicValues = new ArrayList<SelectValue>();
//			for (SelectValue selectValue : itemIds2) {
//				/*if(selectValue.getValue().toString().toLowerCase().contains("allopathic")) {
//					allopathicValues.add(selectValue);
//				}*/
//				/***
//				 * Fix for issue 662. As per above code, allopathic and non-allopathic , both contains
//				 * allopathic string. Hence in the final list , both values will be added. But to exclude
//				 * non allopathic string , instead of validating allopatich string, need to check whether
//				 * the resultant value contains "non". Hence above code is commented and below is added. 
//				 */
//				if(!selectValue.getValue().toString().toLowerCase().contains("non")) {
//					allopathicValues.add(selectValue);
//				}
//			}
//			natureOfTreatment.removeAllItems();
//			natureOfTreatment.addAll(allopathicValues);
//		}

		cmbNatureOfTreatment.setContainerDataSource(natureOfTreatment);
		cmbNatureOfTreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfTreatment.setItemCaptionPropertyId("value");
		
		Collection<?> itemIds = cmbNatureOfTreatment.getContainerDataSource().getItemIds();
		if(itemIds != null && !itemIds.isEmpty() && (bean.getPreauthDataExtractionDetails().getNatureOfTreatment() == null)) {
			cmbNatureOfTreatment.setValue(itemIds.toArray()[0]);
			cmbNatureOfTreatment.setNullSelectionAllowed(false);
		}
		
		cmbSection.setContainerDataSource(section);
		cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSection.setItemCaptionPropertyId("value");

		cmbPatientStatus.setContainerDataSource(patientStatus);
		cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPatientStatus.setItemCaptionPropertyId("value");

		cmbIllness.setContainerDataSource(illness);
		cmbIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIllness.setItemCaptionPropertyId("value");
		cmbIllness.setNullSelectionAllowed(false);
		
		cmbSpecifyIllness.setReadOnly(false);
		cmbSpecifyIllness.setContainerDataSource(criticalIllness);
		cmbSpecifyIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecifyIllness.setItemCaptionPropertyId("value");
		cmbSpecifyIllness.setReadOnly(true);
		
		if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
			this.cmbSpecifyIllness.setReadOnly(false);
			this.cmbSpecifyIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getSpecifyIllness());
		}
		
		List<SelectValue> natureOfTreatmentIDS = natureOfTreatment.getItemIds();
		for (SelectValue selectValue : natureOfTreatmentIDS) {
			if(!selectValue.getId().equals(ReferenceTable.NON_ALLOPATHIC_ID)) {
				this.bean.getPreauthDataExtractionDetails().setNatureOfTreatment(selectValue);
			}
		}
		cmbNatureOfTreatment.setEnabled(false);
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
			cmbNatureOfTreatment.setEnabled(true);
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null) {
			this.cmbNatureOfTreatment.setValue(this.bean
					.getPreauthDataExtractionDetails().getNatureOfTreatment());
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

		if (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null) {
			this.cmbPatientStatus.setValue(this.bean
					.getPreauthDataExtractionDetails().getPatientStatus());
			if(this.cmbPatientStatus.getValue() != null && this.cmbPatientStatus.getValue().toString().toLowerCase()
					.contains("deceased")) {
				this.cmbPatientStatus.setEnabled(false);
			}
		}

		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			this.cmbIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getIllness());
		}
		
		List<SelectValue> hospDueTo = hospitalizationDueTo.getItemIds();
		for (SelectValue selectValue : hospDueTo) {
			if(selectValue.getId().equals(ReferenceTable.INJURY_MASTER_ID)) {
				this.bean.getPreauthDataExtractionDetails().setHospitalisationDueTo(selectValue);
			}
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null) {
			this.cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo());
		}
		
		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
			this.cmbSection.setValue(this.bean
					.getPreauthDataExtractionDetails().getSection());
		}
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



		this.reimbursementCoordinatorViewInstance.setUpReference(referenceData);
		this.diagnosisDetailsTableObj.setReferenceData(referenceData);
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
//		this.claimedDetailsTableObj.setDBCalculationValues((Map<String, Double>)referenceData.get("claimDBDetails"));
		
		if(this.procedurdTableObj != null) {
			this.procedurdTableObj.setReferenceData(referenceData);
		}
		
		if(this.bean.getClaimDTO() != null && this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && this.bean.getPreauthDataExtractionDetails().getTreatmentType() != null) {
			cmbTreatmentType.setEnabled(false);
		}
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
		
		/*if(this.cmbCatastrophicLoss.getValue() == null) {
			hasError = true;
			eMsg += "Catastrophe Loss is Manadatory. </br>";
		}*/
		if(this.cmbNatureOfLoss.getValue() == null) {
			hasError = true;
			eMsg += "Nature Of Loss is Manadatory. </br>";
		}
		if(this.cmbCauseOfLoss.getValue() == null) {
			hasError = true;
			eMsg += "Cause Of Loss is Manadatory. </br>";
		}
		
		if(null != this.cmbTreatmentType && null == this.cmbTreatmentType.getValue())
		{
			hasError = true;
			eMsg += "Please Select Treatment Type. </br>";
		}
		
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg += "Please Select Ventilator Support. </br>";
		}
		
		if(noOfDaysTxt != null && noOfDaysTxt.getValue() == null || (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().length() == 0)) {
			hasError = true;
			eMsg += "Please Enter the Length of Stay. </br>";
		} 
		if(reasonForAdmissionTxt != null && reasonForAdmissionTxt.getValue() == null || (reasonForAdmissionTxt.getValue() != null && reasonForAdmissionTxt.getValue().length() == 0)) {
			hasError = true;
			eMsg += "Please Enter Reason for Admission. </br>";
		} 
		if(cmbNatureOfTreatment != null && cmbNatureOfTreatment.getValue() == null) {
			hasError = true;
			eMsg += "Please Select Nature of Treatment. </br>";
		}
		
		if(automaticRestorationTxt.getValue() != null && (SHAConstants.AUTO_RESTORATION_DONE).equals(automaticRestorationTxt.getValue()))
		{
			if(cmbIllness != null && cmbIllness.getValue() == null){
				hasError = true;
				eMsg += "Please choose illness";
			}
		}


		try {
			if(!this.reimbursementCoordinatorViewInstance.isValid()) {
				hasError = true;
				List<String> errors = this.reimbursementCoordinatorViewInstance.getErrors();
				for (String error : errors) {
					eMsg += error;
				 }
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(this.diagnosisDetailsTableObj != null && this.diagnosisDetailsTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg += "Please Add Atleast one Diagnosis Details to Proceed Further. </br>"; 
		}
		
		
		if(this.reasonForChangeInDOA.isVisible() && !(null != this.reasonForChangeInDOA && 
				(null!= this.reasonForChangeInDOA.getValue() && !("").equalsIgnoreCase(this.reasonForChangeInDOA.getValue()))))
		{
			hasError = true;
			eMsg += "Please enter Reason For Change in DOA to Proceed Further. </br>";
					
		}
		
		if((this.procedurdTableObj != null && this.procedurdTableObj.getValues().isEmpty()) && (this.newProcedurdTableObj != null && this.newProcedurdTableObj.getValues().isEmpty())) {
			hasError = true;
			eMsg += "Please Add Atleast one Procedure List Details to Proceed Further. </br>"; 
		}
		
		/*String strMsg = validateProcedureAndDiagnosisName();
		if(null != strMsg && !("").equalsIgnoreCase(strMsg))
		{
			eMsg += strMsg;
			hasError = true;
		}
		*/
		
		if (this.diagnosisDetailsTableObj != null){
			boolean isValid = this.diagnosisDetailsTableObj.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.diagnosisDetailsTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		
		
		
		if (this.procedurdTableObj != null) {
			boolean isValid = this.procedurdTableObj.isValid();
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
			boolean isValidProcedure = this.newProcedurdTableObj.isValidProcedure();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
			
			if (!isValidProcedure) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj.getProcedureErrors();
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
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& ! this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
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
		
//		if(admissionDate != null && dischargeDate != null) {
//			if(dischargeDate.getValue().before(admissionDate.getValue())) {
//				hasError = true;
//				eMsg += "Discharge date should not be before Admission Date. </br>"; 
//			}
//		}
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			//if(daysBetweenDate >= 0 && SHAUtils.getIntegerFromString(noOfDaysTxt.getValue()) != daysBetweenDate.intValue()) {
			if(daysBetweenDate >= 0 && SHAUtils.getDoubleFromStringWithComma(noOfDaysTxt.getValue()) > daysBetweenDate.doubleValue()) {
				hasError = true;
				eMsg += "No of days should be DOD-DOA+1 </br>"; 
			}
		}
		
		if(this.procedurdTableObj != null && !this.procedurdTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj.getValues();
			List<ProcedureDTO> procedureList = this.procedurdTableObj.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				SelectValue consider = new SelectValue();
				consider.setId(1l);
				consider.setValue("yes");
				procedureDTO.setConsiderForPayment(consider);
			}
			Boolean isError = false;
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
				for (ProcedureDTO procedureDTO : procedureList) {
					if(procedureDTO.getSublimitName() != null && diagnosisDetailsTableDTO.getSublimitName() != null && procedureDTO.getSublimitName().getLimitId().equals(diagnosisDetailsTableDTO.getSublimitName().getLimitId())) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) && (null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							hasError = true;
							isError = true;
							eMsg += "Same Sublimit is Selected for both Diagnosis and Procedure. </br> Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .  </br>";
							break;
						}
					}
				}
				if(isError) {
					break;
				}
			}
		}
		
		Boolean diagnosisIsConsiderForPayment = false;
		Boolean procedureIsConsiderForPayment = false;
		if(this.diagnosisDetailsTableObj != null && !this.diagnosisDetailsTableObj.getValues().isEmpty()) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj.getValues();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if((null != diagnosisDetailsTableDTO.getConsiderForPayment() && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES))) {
							diagnosisIsConsiderForPayment =false;
							break;
						} else {
							diagnosisIsConsiderForPayment = true;
						}
					
				
			}
		}
		if(this.procedurdTableObj != null && !this.procedurdTableObj.getValues().isEmpty()) {
			List<ProcedureDTO> procedureList = this.procedurdTableObj.getValues();
			for (ProcedureDTO procedureDTO : procedureList) {
				if(null != procedureDTO.getConsiderForPayment() && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					procedureIsConsiderForPayment = false;
					break;
				} else {
					procedureIsConsiderForPayment = true;
				}
			}
		} else {
				procedureIsConsiderForPayment = false;
		}
		
		if (diagnosisIsConsiderForPayment && procedureIsConsiderForPayment){
//			hasError = true;
			this.bean.setIsConsiderForPaymentNo(true);
			alertMessageForConsiderForPayment();
		} else {
			this.bean.setIsConsiderForPaymentNo(false);
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
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
				
				/*Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
				if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
						&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
				{
					bean.getNewIntimationDTO().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getNewIntimationDTO().setNomineeAddr(legalHeirMap.get("ADDR").toString());
				}
				else{
					bean.getNewIntimationDTO().setNomineeName(null);
					bean.getNewIntimationDTO().setNomineeAddr(null);
					hasError = true;
					eMsg.append("Please Enter Claimant / Legal Heir Details");
				}*/
								
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
				if(!bean.getIsComparisonDone()  && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat())) {
					fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.COMPARE_WITH_PREVIOUS_ROD, bean);
				}else{
					bean.setIsComparisonDone(true);
				}
				if(this.bean.getDeletedDiagnosis().isEmpty()) {
					this.bean.setDeletedDiagnosis(this.diagnosisDetailsTableObj.deletedDTO);
				} else {
					List<DiagnosisDetailsTableDTO> deletedDTO = this.diagnosisDetailsTableObj.deletedDTO;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDTO) {
						if(!this.bean.getDeletedDiagnosis().contains(diagnosisDetailsTableDTO)) {
							this.bean.getDeletedDiagnosis().add(diagnosisDetailsTableDTO);
						}
						
					}
				}
				
				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
				MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
				
//				if((diffDays != 0 && diffDays > 90) || (policyType != null && policyType.getKey().equals(ReferenceTable.RENEWAL_POLICY)) || !bean.getAdmissionDatePopup()){
				if(policyType != null && !policyType.getKey().equals(ReferenceTable.FRESH_POLICY)){
					this.bean.setAlertMessageOpened(true);
				}
				if(this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null && this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toString().toLowerCase().contains("non")) {
//					this.bean.setIsDishonoured(true);
				}
				
				if(SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList()) != null || SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
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
				
			    isValid = true;
			    bean.setAlertMessageOpened(true);
			    dialog.close();
			    wizard.next();
			   
			   

			}
		});

		return isValid;

	}
	
	private PedDetailsTableDTO setPEDDetailsToDTO(DiagnosisDetailsTableDTO diagnosisDetailsTableDTO,
			InsuredPedDetails pedList) {
		PedDetailsTableDTO dto = new PedDetailsTableDTO();
		dto.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
		dto.setEnableOrDisable(diagnosisDetailsTableDTO.getEnableOrDisable());
		dto.setPolicyAgeing(diagnosisDetailsTableDTO.getPolicyAgeing());
		dto.setRecTypeFlag(diagnosisDetailsTableDTO.getRecTypeFlag());
		if(pedList == null) {
			dto.setPedCode("");
			dto.setPedName("");
		} else {
			dto.setPedCode(pedList.getPedCode());
			dto.setPedName(pedList.getPedDescription());
		}
		return dto;
	}
	
	public void showWarningMsg(String eMsg){
		
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
				this.newProcedurdTableObj != null ?  getProcedureVariationList(this.newProcedurdTableObj.getValues(), 1l)  : new ArrayList<ProcedureDTO>());
		this.bean.getPreauthDataExtractionDetails().setProcedureList(this.procedurdTableObj != null ? getProcedureVariationList(this.procedurdTableObj.getValues(), 0l) : new ArrayList<ProcedureDTO>());
		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj.getValues();
		List<InsuredPedDetails> tmpPEDList = (List<InsuredPedDetails>) referenceData.get("insuredPedList");
		String policyAgeing = (String) referenceData.get("policyAgeing");
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null && diagnosisDetailsTableDTO.getDiagnosisName().getValue() != null) {
				diagnosisDetailsTableDTO.setDiagnosis(diagnosisDetailsTableDTO.getDiagnosisName().getValue());
			}
			if(diagnosisDetailsTableDTO.getDiagnosisName() != null) {
				diagnosisDetailsTableDTO.setDiagnosisId(diagnosisDetailsTableDTO.getDiagnosisName().getId());
			}
			diagnosisDetailsTableDTO.setPolicyAgeing(policyAgeing);
			List<PedDetailsTableDTO> list = new ArrayList<PedDetailsTableDTO>();
			if(diagnosisDetailsTableDTO.getPedList().isEmpty()) {
				if(tmpPEDList != null && !tmpPEDList.isEmpty()) {
					for (InsuredPedDetails insuredPEDDetails : tmpPEDList) {
						PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, insuredPEDDetails);
						list.add(setPEDDetailsToDTO);
					}
					
				}  else {
					PedDetailsTableDTO setPEDDetailsToDTO = setPEDDetailsToDTO(diagnosisDetailsTableDTO, null);
					list.add(setPEDDetailsToDTO);
				}
				diagnosisDetailsTableDTO.setPedList(list);
			} else {
				List<PedDetailsTableDTO> pedList = diagnosisDetailsTableDTO.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					pedDetailsTableDTO.setDiagnosisName(diagnosisDetailsTableDTO.getDiagnosis());
				}
			}
		}
		
		this.bean.getPreauthDataExtractionDetails().setDiagnosisTableList(diagnosisList);
		
		List<ProcedureDTO> wholeProcedureList = new ArrayList<ProcedureDTO>();
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getProcedureList());
		wholeProcedureList.addAll(this.bean.getPreauthDataExtractionDetails().getNewProcedureList());
		
		this.bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(wholeProcedureList);
		
		if(otherClaimTableObj != null) {
			this.bean.getPreauthDataExtractionDetails().setOtherClaimDetailsList(otherClaimTableObj.getValues());
		}
		
	}
	
	private List<ProcedureDTO> getProcedureVariationList(List<ProcedureDTO> procedureDTOList, Long isNewProcedure)  {
		if(!procedureDTOList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureDTOList) {
				if(isNewProcedure == 0) {
					procedureDTO.setProcedureNameValue(procedureDTO.getProcedureName() != null ? procedureDTO.getProcedureName().getValue() : null);
					procedureDTO.setProcedureCodeValue(procedureDTO.getProcedureCode() != null ? procedureDTO.getProcedureCode().getValue() : null);
				} else {
					SelectValue procedureName = new SelectValue();
					procedureName.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					SelectValue procedureCode = new SelectValue();
					procedureCode.setValue(procedureDTO.getProcedureCodeValue());
					//procedureDTO.setProcedureName(procedureCode);
					procedureDTO.setProcedureCode(procedureCode);
				}
				procedureDTO.setNewProcedureFlag(isNewProcedure);
				procedureDTO.setPolicyAging(referenceData.containsKey("policyAgeing") ? (String)referenceData.get("policyAgeing") : null);
			}
		}
		return procedureDTOList;
	}
	
	private void setTableValues() {
		
		if(this.specialityTableObj != null) {
			List<SpecialityDTO> specialityList = this.bean.getPreauthDataExtractionDetails().getSpecialityList();
			for (SpecialityDTO specialityDTO : specialityList) {
				specialityDTO.setStatusFlag(true);
				this.specialityTableObj.addBeanToList(specialityDTO);
			}
		}
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
	
		List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				if(procedureDTO.getNewProcedureFlag() == 1) {
					newProcedureDTOList.add(procedureDTO);
				} else {
					oldProcedureDTOList.add(procedureDTO);
				}
			}
		}
		this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
		this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
		if(this.newProcedurdTableObj != null) {
			List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
			for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
				this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
			}
		}
		
		if(this.procedurdTableObj != null) {
			List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
			for (ProcedureDTO procedureTableDTO : procedureList) {
				this.procedurdTableObj.addBeanToList(procedureTableDTO);
			}
		}
		
		if(this.diagnosisDetailsTableObj != null) {
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisList) {
				this.diagnosisDetailsTableObj.addBeanToList(diagnosisDTO);
			}
			this.diagnosisDetailsTableObj.init(this.bean,SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);
			
		}
		
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

	protected void addListener() {	
		

		criticalIllnessChk
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean checkValue = criticalIllnessChk.getValue();
						fireViewEvent(
								PAHealthClaimRequestDataExtractionPagePresenter.CHECK_CRITICAL_ILLNESS,
								checkValue, true);
					}
				});

		cmbTreatmentType.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && cmbTreatmentType.getValue().toString().toLowerCase()
						.contains("medical")) {
					if(!bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().isEmpty()) {
						alertMessageForChangeOfTreatmentType();
					} else {
						fireViewEvent(
								PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
							null);
				}
			}
		});
		
		 cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					SelectValue value = (SelectValue) event.getProperty().getValue();
					bean.getNewIntimationDTO().setRoomCategory(value);
					
					if(value != null && value.getValue() != null){
						
						System.out.println(String.format("Room Category Value : [%s]",value.getValue()));
					}
					
					if(value != null && value.getValue() != null && value.getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")) {
						ventilatorSupportOption.setEnabled(true);
						ventilatorSupportOption.setRequired(true);
						//ventilatorSupportOption.setVisible(true);
						mandatoryFields.add(ventilatorSupportOption);
						//bean.getPreauthDataExtractionDetails().setVentilatorSupport(true);
					} 
					else{
						ventilatorSupportOption.setValue(null);
						//unbindField(ventilatorSupportOption);
						ventilatorSupportOption.setEnabled(false);
						ventilatorSupportOption.setRequired(false);
						mandatoryFields.remove(ventilatorSupportOption);
					}
					
//					List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//					
//					if(procedureTableObj != null){
//						List<ProcedureDTO> values = procedureTableObj.getValues();
//						procedureList.addAll(values);
//						procedureTableObj.removeRow();
//						for (ProcedureDTO procedureDTO : procedureList) {
////							procedureDTO.setProcedureCode(null);
//							procedureTableObj.addBeanToList(procedureDTO);
//						}
//					}
				
				}
			});
		
		 
		/* ventilatorSupportOption
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
		 
		cmbNatureOfTreatment.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getValue() != null && value.getValue().toString().toLowerCase().contains("non")) {
					Collection<?> itemIds = cmbTreatmentType.getContainerDataSource().getItemIds();
					cmbTreatmentType.setValue(itemIds.toArray()[0]);
					cmbTreatmentType.setEnabled(false);
				} else {
					cmbTreatmentType.setEnabled(true);
				}
			}
		});
		
		 cmbSection.addValueChangeListener(new ValueChangeListener() {
	  			
	  			@Override
	  			public void valueChange(ValueChangeEvent event) {
	  				
	  				if(cmbSection != null && cmbSection.getValue() != null){
	  					SelectValue sectionValue = (SelectValue) cmbSection.getValue();
	  					bean.getPreauthDataExtractionDetails().setSection(sectionValue);
	  				}
	  				
	  				fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.SUBLIMIT_CHANGED_BY_SECTION, bean);
	  				

	  				List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
	  				
	  				diagnosisList = diagnosisDetailsTableObj.getValues();
	  				
	  				
	  				diagnosisDetailsTableObj.init(bean,SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION);
	  				diagnosisDetailsTableObj.setReferenceData(referenceData);
	  				
	  				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
	  					
	  					diagnosisDetailsTableObj.addBeanToList(diagnosisDetailsTableDTO);
							
						}

	  				if(procedurdTableObj != null) {
	  					
	  					List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();
	  					
	  					procedureDTO = procedurdTableObj.getValues();
	  				
	  					procedurdTableObj.init(bean.getHospitalCode(), SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION, bean);
	  					procedurdTableObj.setReferenceData(referenceData);
	  					
	                     for (ProcedureDTO procedureDTO2 : procedureDTO) {
	                    	 procedurdTableObj.addBeanToList(procedureDTO2);
							}
	  				}
	  				
//	  				List<ProcedureDTO> procedureList = new ArrayList<ProcedureDTO>();
//	  				
//	  				if(procedureTableObj != null){
//	  					List<ProcedureDTO> values = procedureTableObj.getValues();
//	  					procedureList.addAll(values);
//	  					procedureTableObj.removeRow();
//	  					for (ProcedureDTO procedureDTO : procedureList) {
	  				
////	  						procedureDTO.setProcedureCode(null);
//	  						procedureTableObj.addBeanToList(procedureDTO);
//	  					}
//	  				}
	  			
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
						PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED,
						null);
			}
		});
		
//        dischargeDate.setData(bean.getPreauthDataExtractionDetails().getAdmissionDate());
		
		dischargeDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
						if (bean.getPreauthDataExtractionDetails()
								.getAdmissionDate() != null) {
//							Date admission = (Date) ((DateField) event.getProperty()).getData();
							Date enteredDate = (Date) ((DateField) event.getProperty()).getValue();
							
//							if(enteredDate != null && admissionDate != null && admissionDate.getValue() != null){
//								if(enteredDate.before(admissionDate.getValue())){
//									admissionDate.setValue(null);
//								}
//							}
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
//									event.getProperty().setValue(null);
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
		
		changeInDiagnosis.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					diagnosisDetailsTableObj.enableOrDisableDeleteButton(true);
					changeInDiagSelected = true;
					changeIndiaganosisRows = diagnosisDetailsTableObj.getValues().size();
					if(procedurdTableObj != null) {
						procedurdTableObj.enableOrDisableDeleteButton(true);
					}
					
				} else {
					diagnosisDetailsTableObj.enableOrDisableDeleteButton(false);
					if(procedurdTableObj != null) {
						procedurdTableObj.enableOrDisableDeleteButton(false);
					}
					
				}
			}
		});
		
		coveredPreviousClaim.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				
				fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS, isChecked);
			}
		});
		
		cmbHospitalisationDueTo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2577540521492098375L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
//				if(value != null) {
					fireViewEvent(
							PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO,
							value);
//				}
				
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
				fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.GENERATE_DOMICILLARY_FIELDS, isChecked);
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
					if(!value.equalsIgnoreCase(ReferenceTable.HOSP_SUB_COVER_CODE)) {
						if(domicillaryHospitalisation.getValue() != null && domicillaryHospitalisation.getValue().toString().equalsIgnoreCase("true")) {
							domicillaryHospitalisation.setValue(false);
						}
						domicillaryHospitalisation.setEnabled(false);
					} else {
						domicillaryHospitalisation.setEnabled(StarCommonUtils.shouldDisableDomicillary(bean));
					}
					fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.SET_DB_DETAILS_TO_REFERENCE_DATA, bean);
					if(diagnosisDetailsTableObj != null && !diagnosisDetailsTableObj.getValues().isEmpty()) {
						diagnosisDetailsTableObj.changeSublimitValues();
					} 
					if(procedurdTableObj != null && !procedurdTableObj.getValues().isEmpty()) {
						procedurdTableObj.changeSublimitValues();
					}
				}
			}
		});

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
	
	protected void addAdmissionDateChangeListener()
	{
		admissionDate.setValue(this.bean.getPreauthDataExtractionDetails().getAdmissionDate());

		admissionDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				admissionDate.setValue(admissionDate.getValue());
				
				reasonForChangeInDOA.setVisible(true);
				reasonForChangeInDOA.setRequired(true);
				/**
				 * If admission Date is changed, then reasonForChangeInDOA 
				 * is mandatory
				 * */
				mandatoryFields.add(reasonForChangeInDOA);
				
				if(bean.getPreauthDataExtractionDetails().getAdmissionDate() != null){
					Date enterDate = admissionDate.getValue();
					
					if(enterDate != null && enterDate.compareTo(bean.getPreauthDataExtractionDetails().getAdmissionDate())==0){
						reasonForChangeInDOA.setVisible(false);
						reasonForChangeInDOA.setRequired(false);
						
						mandatoryFields.remove(reasonForChangeInDOA);
					}
				}
				
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	
	protected void addDiagnosisNameChangeListener()
	{
		this.diagnosisDetailsTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != procedurdTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!procedurdTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedurdTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedurdTableObj.diagnosisList);
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

	public void generatedFieldsBasedOnTreatment() {
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
				unbindField(treatmentRemarksTxt);
				treatmentRemarksTxt = (TextArea) binder.buildAndBind(
						"Treatment Remarks", "treatmentRemarks",
						TextArea.class);
				treatmentRemarksTxt.setMaxLength(4000);
				treatmentFLayout.addComponent(treatmentRemarksTxt);
//				treatmentRemarksTxt.setRequired(true);
//				treatmentRemarksTxt.setValidationVisible(false);
				
				CSValidator validator = new CSValidator();
//				treatmentRemarksTxt.setMaxLength(100);
//				validator.extend(treatmentRemarksTxt);
				validator.setRegExp("^[a-zA-Z 0-9]*$");
				validator.setPreventInvalidTyping(true);
				
				Table table = new Table();
				table.setWidth("80%");
				table.addContainerProperty("S.No", String.class, null);
				table.addContainerProperty("Remarks",  String.class, null);
				table.setStyleName(ValoTheme.TABLE_NO_HEADER);
				if(this.bean.getTreatmentRemarksList() != null && !this.bean.getTreatmentRemarksList().isEmpty()) {
					for(int i = 0; i < this.bean.getTreatmentRemarksList().size(); i++) {
						if(i != this.bean.getTreatmentRemarksList().size()){
						table.addItem(new Object[]{"Treatment Remarks " + (i+1), this.bean.getTreatmentRemarksList().get(i)}, i+1);
						}
					}
					table.setPageLength(2);
					treatmentFLayout.addComponent(table);
				}
				
				
//				mandatoryFields.add(treatmentRemarksTxt);

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
//					mandatoryFields.remove(treatmentRemarksTxt);

				}
				isMedicalSeclted = false;
				
				List<DiagnosisDetailsTableDTO> diagList = null;
				if(null != diagnosisDetailsTableObj)
				{
					diagList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
				}
				
				PAProcedureListenerTableForPremedical procedureTableInstance = procedureTableList
						.get();
				procedureTableInstance.init(bean.getHospitalCode(), SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION, bean);
				procedureTableInstance.setReferenceData(referenceData);
				this.procedurdTableObj = procedureTableInstance;
				
				addDiagnosisNameChangeListener();
				
				PANewProcedureTableList newProcedureTableListInstance = newProcedureTableList
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
				
				List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
				List<ProcedureDTO> oldProcedureDTOList = new ArrayList<ProcedureDTO>();
			
				List<ProcedureDTO> newProcedureDTOList = new ArrayList<ProcedureDTO>();
				if(!procedureExclusionCheckTableList.isEmpty()) {
					for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
						if(null != procedureDTO && null != procedureDTO.getEnableOrDisable() && !procedureDTO.getEnableOrDisable())
						{
							procedureDTO.setEnableOrDisable(false);
						}
						else
						{
							procedureDTO.setEnableOrDisable(true);
						}
						procedureDTO.setStatusFlag(true);
						if(procedureDTO.getNewProcedureFlag() == 1) {
							newProcedureDTOList.add(procedureDTO);
						} else {
							oldProcedureDTOList.add(procedureDTO);
						}
					}
				}
				this.bean.getPreauthDataExtractionDetails().setProcedureList(oldProcedureDTOList);
				this.bean.getPreauthDataExtractionDetails().setNewProcedureList(newProcedureDTOList);
				if(this.newProcedurdTableObj != null) {
					List<ProcedureDTO> newProcedureList = this.bean.getPreauthDataExtractionDetails().getNewProcedureList();
					for (ProcedureDTO newProcedureTableDTO : newProcedureList) {
						newProcedureTableDTO.setEnableOrDisable(true);
						this.newProcedurdTableObj.addBeanToList(newProcedureTableDTO);
					}
				}
				
				if(this.procedurdTableObj != null) {
					List<ProcedureDTO> procedureList = this.bean.getPreauthDataExtractionDetails().getProcedureList();
					for (ProcedureDTO procedureTableDTO : procedureList) {
						if(null != procedureTableDTO && null != procedureTableDTO.getEnableOrDisable() && !procedureTableDTO.getEnableOrDisable())
						{
							procedureTableDTO.setEnableOrDisable(false);
						}
						else
						{
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
//				mandatoryFields.remove(treatmentRemarksTxt);
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
//				cmbTerminateCover = (ComboBox) binder.buildAndBind(
//						"Terminate Cover", "terminateCover", ComboBox.class);
//				
//				@SuppressWarnings("unchecked")
//				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
//						.get("terminateCover");
//
//				cmbTerminateCover.setContainerDataSource(terminateCover);
//				cmbTerminateCover.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//				cmbTerminateCover.setItemCaptionPropertyId("value");
//				
//				if(this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag() != null && this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag().toLowerCase().equalsIgnoreCase("y")) {
//					cmbTerminateCover.setValue(this.bean.getPreauthDataExtractionDetails().getTerminateCover());
//				}

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
//				setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
//				mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
//				patientStatusFLayout.addComponent(cmbTerminateCover);
				buildNomineeLayout();
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
						showError();
						event.getProperty().setValue(null);
					}else if(enteredDate != null && dischargeDate != null && dischargeDate.getValue() != null){
					if(enteredDate.after(dischargeDate.getValue())){
						showError();
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
   private void showError(){
		
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
		VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and Discharge date.</b>", ContentMode.HTML));
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
//			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
//			mandatoryFields.remove(cmbTerminateCover);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
//			patientStatusFLayout.removeComponent(cmbTerminateCover);
		}
	}
	
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer){
		diagnosisDetailsTableObj.setIcdBlock(icdBlockContainer);
		
	}
	
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer){
		diagnosisDetailsTableObj.setIcdCode(icdCodeContainer);
	}



	public void setPackageRateForProcedure(Map<String, String> mappedValues) {
		this.procedurdTableObj.setPackageRate(mappedValues);
		
	}
	
	/**
	 * If the policy type is "Group Policy" , then the corp buffer check box
	 * will be enabled. Else it will be disabled. The below method
	 * does the validation for the same.
	 * */
	
	public void editCorpBufferChk()
	{
		/**
		 * The string "Group Policy" needs to be replaced, after checkwith
		 * saravana/Sathish the valid values for Group Policy.For time being
		 * the below string is used. 
		 */

		if (null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() 
				&& null != this.bean.getNewIntimationDTO().getPolicy().getProductType() && ("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		//if (("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		{
			corpBufferChk.setReadOnly(false);
		}
		else
		{
			corpBufferChk.setReadOnly(true);
		}
	}
	
	
	public void generatedFieldsBasedOnHospitalisationDueTo(SelectValue value) {
		if (null != value) {
			if(value.getId() != null && ReferenceTable.INJURY_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				injuryDate = (DateField) binder.buildAndBind(
						"Date of Injury", "injuryDate", DateField.class);
				cmbCauseOfInjury = (ComboBox) binder.buildAndBind(
						"Cause of Injury", "causeOfInjury", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("causeOfInjury");

				cmbCauseOfInjury.setContainerDataSource(terminateCover);
				cmbCauseOfInjury.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbCauseOfInjury.setItemCaptionPropertyId("value");
				
				if(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null) {
					cmbCauseOfInjury.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury());
				}
				
				medicalLegalCase = (OptionGroup) binder.buildAndBind(
						"Medical Legal Case", "medicalLegalCase", OptionGroup.class);
				medicalLegalCase.addItems(getReadioButtonOptions());
				medicalLegalCase.setItemCaption(true, "Yes");
				medicalLegalCase.setItemCaption(false, "No");
				medicalLegalCase.setStyleName("horizontal");
				
				reportedToPolice = (OptionGroup) binder.buildAndBind(
						"Reported to Police", "reportedToPolice", OptionGroup.class);
				reportedToPolice.addItems(getReadioButtonOptions());
				reportedToPolice.setItemCaption(true, "Yes");
				reportedToPolice.setItemCaption(false, "No");
				reportedToPolice.setStyleName("horizontal");
				reportedToPolice.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Boolean isChecked = false;
						if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
							isChecked = true;
						} 
						if(event.getProperty() != null && event.getProperty().getValue() != null) {
							fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_REPORTED_TO_POLICE, isChecked);
						}
					}
				});
				
				
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

				
				firstFLayout.addComponent(injuryDate);
				firstFLayout.addComponent(medicalLegalCase);
				firstFLayout.addComponent(reportedToPolice);
				
				secondFLayout.addComponent(cmbCauseOfInjury);
				
				secondFLayout.addComponent(preExistingDisabilities);
				
				secondFLayout.addComponent(txtDisablitiesRemarks);
				
				
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
				
				if(this.bean.getPreauthDataExtractionDetails().getReportedToPolice() != null && this.bean.getPreauthDataExtractionDetails().getReportedToPolice()) {
					generatedFieldsBasedOnReportedToPolice(true);
				}
				
				
			} else if(value.getId() != null && ReferenceTable.ILLNESS_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				diseaseDetectedDate = (DateField) binder.buildAndBind(
						"Date Disease First Detected", "diseaseFirstDetectedDate", DateField.class);
				firstFLayout.addComponent(diseaseDetectedDate);
			} else if(value.getId() != null && ReferenceTable.MATERNITY_MASTER_ID.equals(value.getId())) {
				if(firNumberTxt != null){
					firstFLayout.removeComponent(firNumberTxt);
					}
				if(policeReportedAttached != null){
					secondFLayout.removeComponent(policeReportedAttached);
				}
				unbindAndRemoveFromLayout();
				if(bean.getNewIntimationDTO().getInsuredPatient() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)){
				deliveryDate = (DateField) binder.buildAndBind(
						"Date of Delivery", "deliveryDate", DateField.class);
				mandatoryFields.add(deliveryDate);
				showOrHideValidation(false);
				cmbTypeOfDelivery = (ComboBox) binder.buildAndBind(
						"Type of Delivery", "typeOfDelivery", ComboBox.class);
				
				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> terminateCover = (BeanItemContainer<SelectValue>) referenceData
						.get("typeOfDelivery");

				cmbTypeOfDelivery.setContainerDataSource(terminateCover);
				cmbTypeOfDelivery.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbTypeOfDelivery.setItemCaptionPropertyId("value");
				
				if(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery() != null) {
					cmbTypeOfDelivery.setValue(this.bean.getPreauthDataExtractionDetails().getTypeOfDelivery());
				}
				
				firstFLayout.addComponent(deliveryDate);
				firstFLayout.addComponent(cmbTypeOfDelivery);
			}else{
				getErrorMessage("Selected insured is Male.Hence Maternity is not applicable for the selected insured");
				cmbHospitalisationDueTo.setValue(null);
				//Vaadin8-setImmediate() cmbHospitalisationDueTo.setImmediate(true);
			}
				}
			
		} else {
			unbindAndRemoveFromLayout();
		}
	}
	
	private void alertMessageForClaimCount(Long claimCount){
		
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
		
		
		popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
//		popup.setCaption("Alert");
//		popup.setContent( viewDocumentDetailsPage);
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
				if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
					suspiousPopupMessage();
				} else if(bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if(bean.getIsPedWatchList()){
					alertMessageForPEDWatchList();
				} else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				} 
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void generatedFieldsBasedOnReportedToPolice(Boolean isChecked) {
		if (isChecked) {
				unbindAndRemovePoliceReportFromLayout();
				firNumberTxt = (TextField) binder.buildAndBind(
						"FIR NO", "firNumber", TextField.class);
				
				policeReportedAttached = (OptionGroup) binder.buildAndBind(
						"MLC Report & Police Report Attached", "policeReportAttached", OptionGroup.class);
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
		
		if(firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}
		
	}
	
	private void unbindAndRemoveFromLayoutForDomicillary() {
		unbindField(treatmentStartDate);
		unbindField(treatmentEndDate);
		unbindField(hospitalizationNoOfDays);
		if(treatmentStartDate != null && treatmentEndDate != null && hospitalizationNoOfDays != null) {
			firstFLayout.removeComponent(treatmentStartDate);
			firstFLayout.removeComponent(treatmentEndDate);
			secondFLayout.removeComponent(hospitalizationNoOfDays);
		}
		
		
		
	}

	
	public void generatedFieldsBasedOnOtherClaims(Boolean value) {
		if(otherClaimsFLayout.getComponentCount() > 0) {
			otherClaimDetailsObj = null;
			otherClaimsFLayout.removeAllComponents();
		}
		if(value) {
			otherClaimDetailsObj =  otherClaimDetailsInstance.get();
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
		
		
		if(injuryDate != null && cmbCauseOfInjury != null && medicalLegalCase != null && reportedToPolice != null  ) {
			firstFLayout.removeComponent(medicalLegalCase);
			firstFLayout.removeComponent(reportedToPolice);
			firstFLayout.removeComponent(injuryDate);
			secondFLayout.removeComponent(cmbCauseOfInjury);
			
		}
		
		if(firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}
		
		if(deliveryDate != null ) {
			firstFLayout.removeComponent(deliveryDate);
			mandatoryFields.remove(deliveryDate);
		}
		
		if(cmbTypeOfDelivery != null) {
			firstFLayout.removeComponent(cmbTypeOfDelivery);
		}
		
		if(diseaseDetectedDate != null) {
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
	   	 DiagnosisDetailsTableDTO dialysisDiagnosisDTO = SHAUtils.getDialysisDiagnosisDTO(bean.getPreauthDataExtractionDetails().getDiagnosisTableList());
	   	 ProcedureDTO dialysisProcedureDTO = SHAUtils.getDialysisProcedureDTO(bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList());
	   	 if(dialysisDiagnosisDTO != null || dialysisProcedureDTO != null) { 
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
					
					sittingsField.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
					if(dialysisDiagnosisDTO != null) {
						if(dialysisDiagnosisDTO.getSittingsInput() != null) {
							sittingsField.setValue(dialysisDiagnosisDTO.getSittingsInput() );
						}
					} else if(dialysisProcedureDTO != null) {
						if(dialysisProcedureDTO.getSittingsInput() != null) {
							sittingsField.setValue(dialysisProcedureDTO.getSittingsInput() );
						}
					}
					
					Button okButton = new Button("OK");
					okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					
					okButton.setData(dialysisDiagnosisDTO != null ? dialysisDiagnosisDTO : (dialysisProcedureDTO != null ? dialysisProcedureDTO : null ));
					okButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;
	
						@Override
						public void buttonClick(ClickEvent event) {
							if(event.getButton().getData() != null) {
								if((event.getButton().getData()) instanceof DiagnosisDetailsTableDTO) {
									DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) event.getButton().getData();
									dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
								} else if((event.getButton().getData()) instanceof ProcedureDTO) {
									ProcedureDTO dto = (ProcedureDTO) event.getButton().getData();
									dto.setSittingsInput(sittingsField.getValue() != null ? sittingsField.getValue() : "0");
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
					List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
                	for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
                		if(!bean.getDeletedProcedure().contains(procedureDTO) && procedureDTO.getKey() != null) {
	        				bean.getDeletedProcedure().add(procedureDTO);
	        			}
					}
                	
                	bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(new ArrayList<ProcedureDTO>());
                	fireViewEvent(
							PAHealthClaimRequestDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
							null);
					dialog.close();
				}
			});
			
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					cmbTreatmentType.setValue(cmbTreatmentType.getItemIds().toArray()[1]);
					dialog.close();
				}
			});
			HorizontalLayout hLayout = new HorizontalLayout(okButton, cancelButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("Entered Procedure Will be marked as deleted one. Do you want to proceed further ?"), hLayout);
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
					if(bean.getClaimCount() >1){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
						suspiousPopupMessage();
					} else if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsPedWatchList()){
						alertMessageForPEDWatchList();
					} else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
						
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
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
					if(bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(bean.getIsPedWatchList()){
						alertMessageForPEDWatchList();
					} else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
						
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
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
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.CONSIDER_FOR_PAYMENT_ALERT + "</b>",
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
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}
				}
			});
			return true;
		}
	  
	  @SuppressWarnings("static-access")
	  public static Boolean alertMessageForLumpsum(String message) {
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
//			dialog.setCaption("Alert");
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
	  
	  
	  @SuppressWarnings("static-access")
 	  public static Boolean warningMessageForLumpsum(String message) {
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
//				dialog.setCaption("Alert");
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
						alertMessageForLumpsum(SHAConstants.LUMPSUM_ALERT_MESSAGE);
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
						} else if(bean.getClaimCount() >1){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if(!bean.getSuspiciousPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()){
							suspiousPopupMessage();
						} else if(bean.getIsPEDInitiated()) {
							alertMessageForPED();
						} else if(bean.getIsPedWatchList()){
							alertMessageForPEDWatchList();
						} else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							}
						}	
					}
				});
			}
	    
		 public void buildNomineeLayout(){
			 
			 
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					
					if(nomineeDetailsTable != null) {
						
						wholeVLayout.removeComponent(nomineeDetailsTable);
					}
					if(chkNomineeDeceased != null){
						wholeVLayout.removeComponent(chkNomineeDeceased);
					}
					if(legalHeirLayout != null) {
						wholeVLayout.removeComponent(legalHeirLayout);
					}

					nomineeDetailsTable = nomineeDetailsTableInstance.get();
					
					nomineeDetailsTable.init("", false, false);
					unbindField(chkNomineeDeceased);
					chkNomineeDeceased = null;
					if(bean.getNewIntimationDTO().getNomineeList() != null &&
							!bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
						nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
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
//						legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
						legalHeirDetails.getBtnAdd().setEnabled(true);
					}
					else {
						legalHeirDetails.deleteRows();
						legalHeirDetails.getBtnAdd().setEnabled(false);
					}
				}
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

		 public void generateButton(Integer clickedButton, Object dropDownValues) {
				this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
				
				switch (clickedButton) {
				case 1: 
					
					 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
					 this.paClaimRequestDataExtractionButtonObj.buildSendReplyLayout();
					 
					 if(this.bean.getIsReplyToFA() != null && this.bean.getIsReplyToFA()){
						 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
					 }
					 break;
				 
				case 2:
					
					if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
						 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
						 
					 }
					this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
					this.paClaimRequestDataExtractionButtonObj
							.buildReferCoordinatorLayout(dropDownValues);
					break;
					
				case 3:
					
					this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS);
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
//					                System.out.println("close called");
					            }
					     });
						 
						}
						break;
					
				case 4:
					
					if(fileUploadValidatePage()){
					this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
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
				            }
				        });
					 
					}
					break;
					
				case 5:

					 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)) {
						 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
						 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
					 }
					if(fileUploadValidatePage()){
						this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
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
	
					            }
				        });
					
					}
					break;
					
				case 6:
					
					this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
					paClaimRequestDataExtractionButtonObj.setReferenceData(referenceData);  //  "cancellationReason"
					this.paClaimRequestDataExtractionButtonObj.builtCancelRODLayout();
					break;
				default:
					break;
				}
			}
		 
			public boolean fileUploadValidatePage() {
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
						return true;
					} catch (CommitException e) {
						e.printStackTrace();
					}
					showOrHideValidation(false);
					return false;
				}
			}
			
			@Override
			public void submit(PreauthDTO preauthDTO) {
				specialistWindow.close();
				wizard.finish();
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
