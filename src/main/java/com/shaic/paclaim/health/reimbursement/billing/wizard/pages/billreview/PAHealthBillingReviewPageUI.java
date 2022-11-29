package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview;

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
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.wizard.TreatmentDateListenerTable;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewIrdaNonPayablePdfPage;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.cashless.listenertables.PASectionDetailsListenerTable;
import com.shaic.paclaim.health.reimbursement.billing.wizard.PAHealthBillingProcessButtonsUIForFirstPage;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthAddOnBenefitsDataExtractionPage;
import com.shaic.paclaim.health.reimbursement.financial.tables.PAHealthBillClassificationEditUI;
import com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable;
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
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthBillingReviewPageUI extends ViewComponent {

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;

	private GWizard wizard;
	
	private Window popup;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	@Inject
	private Instance<PAHealthUploadedDocumentsListenerTable> uploadDocumentListenerTable;

	@Inject
	private Instance<PAHealthBillingProcessButtonsUIForFirstPage> billingProcessButtonInstance;

	private PAHealthBillingProcessButtonsUIForFirstPage billingProcessButtonObj;

	@Inject
	private Instance<RoomRentMatchingTable> roomRentMatchingTable;

	@Inject
	private Instance<PAHealthAddOnBenefitsDataExtractionPage> addOnBenifitsPageInstance;

	@Inject
	private Instance<TreatmentDateListenerTable> treatmentListenerTable;

	@Inject
	private BillClassificationUI billClassificationUI;

	@Inject
	private Instance<PAHealthBillClassificationEditUI> billClassificationEditUIInstance;
	
	@Inject
	private Instance<PASectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private PASectionDetailsListenerTable sectionDetailsListenerTableObj;

	private PAHealthBillClassificationEditUI billClassificationEditUIObj;

	private TreatmentDateListenerTable treatmentListenerTableObj;

	private RoomRentMatchingTable roomRentMatchingTableObj;

	private PAHealthAddOnBenefitsDataExtractionPage addOnBenefitsPageObj;

	private PAHealthUploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	@Inject
	private ViewIrdaNonPayablePdfPage pdfPageUI;

	private DateField admissionDate;

	private DateField dischargeDate;
	
	private OptionGroup workOrNonWorkSpace;

	private TextField admissionReason;

	private TextField dischargeReason;

	private ComboBox cmbPatientStatus;

	private DateField deathDate;

	private TextField txtReasonForDeath;

	private ComboBox cmbTerminateCover;

	private FormLayout firstFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;

	private Date admissionDateValue;

	private Map<String, Property.ValueChangeListener> listenerMap = new HashMap<String, Property.ValueChangeListener>();

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

	private TextField automaticRestorationTxt;

	private ComboBox cmbSpecifyIllness;

	private CheckBox criticalIllnessChk;

	private TextField reasonForAdmissionTxt;

	private Button editBillClassification;

	//private ComboBox cmbCatastrophicLoss;

	private ComboBox cmbNatureOfLoss;

	private ComboBox cmbCauseOfLoss;

	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private VerticalLayout legalHeirLayout;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
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
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
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
				 if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}  else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
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

	public void popupMessageFor30DaysWaitingPeriod() {

		Date policyFromDate = bean.getNewIntimationDTO().getPolicy()
				.getPolicyFromDate();
		Date admissionDate = this.bean.getPreauthDataExtractionDetails()
				.getAdmissionDate();
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		MastersValue policyType = bean.getNewIntimationDTO().getPolicy()
				.getPolicyType();
		if ((diffDays != null && diffDays < 30)) {

			final MessageBox showAlert = showAlertMessageBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT);
			Button homeButton = showAlert.getButton(ButtonType.OK);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					bean.setIsPopupMessageOpened(true);
					showAlert.close();
					if (bean.getIsSuspicious() != null) {
						StarCommonUtils.showPopup(getUI(),
								bean.getIsSuspicious(),
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
		} else {
			if (bean.getIsSuspicious() != null) {
				StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
						bean.getClmPrcsInstruction());
			} else if (bean.getNewIntimationDTO().getHospitalDto()
					.getReInstatedBy()
					.equalsIgnoreCase(SHAConstants.HOSPITAL_SUSPENDED_BY_RAW)) {
				getHospitalReinstatedAlert();
			}
		}

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

	public MessageBox showInfoMessageBox(String message) {
		final MessageBox msgBox = MessageBox.createInfo()
				.withCaptionCust("Information").withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		return msgBox;
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
		return true;
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

		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		else if (!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if (!bean.getSuspiciousPopupMap().isEmpty()
				&& !bean.getIsPopupMessageOpened()) {
			suspiousPopupMessage();
		} else if (bean.getIsPEDInitiated()) {
			alertMessageForPED();
		} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
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
		if(bean.getNewIntimationDTO().getHospitalDto()	 != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
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

		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
		workOrNonWorkSpace.setEnabled(false);
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		admissionReasonLayout = new FormLayout();

		dischargeReasonLayout = new FormLayout();
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		firstFLayout = new FormLayout(admissionDate, cmbRoomCategory,ventilatorSupportOption,
				illnessFLayout, cmbSpecifyIllness, preauthApprovedAmt,
				cmbSection);
		patientStatusFLayout = new FormLayout(workOrNonWorkSpace,dischargeDate, noOfDaysTxt,
				automaticRestorationTxt, cmbIllness, cmbPatientStatus,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);

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
			admissionDate.setEnabled(false);
			dischargeDate.setEnabled(false);
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
			editBillClassification.setEnabled(false);
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
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null) {
			Long noOfDays = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			noOfDays++;
			noOfDaysTxt.setValue(noOfDays.toString());
		}
		noOfDaysTxt.setValue(bean.getPreauthDataExtractionDetails().getNoOfDays());
		
		editBillClassification.setEnabled(false);

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

				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				okBtn.setEnabled(false);
				List<UploadDocumentDTO> values = uploadDocumentListenerTableObj
						.getValues();
				billClassificationEditUIObj = billClassificationEditUIInstance
						.get();
				billClassificationEditUIObj.initForEdit(bean, values,
						uploadDocumentListenerTableObj, okBtn);

				VerticalLayout layout = new VerticalLayout(
						billClassificationEditUIObj, okBtn);
				layout.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);

				final ConfirmDialog dialog = new ConfirmDialog();
				// dialog.setCaption("Alert");
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

						dialog.close();

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
		BeanItemContainer<SelectValue> natureOfLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("natureOfLoss");
		
		BeanItemContainer<SelectValue> catastrophicLoss = (BeanItemContainer<SelectValue>) referenceData
				.get("catastrophicLoss");
		
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
					cmbCatastrophicLoss.setValue(val);
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

		if (this.addOnBenefitsPageObj != null
				&& !this.addOnBenefitsPageObj.validatePage()) {
			List<String> errors = this.addOnBenefitsPageObj.getErrors();
			hasError = true;
			for (String error : errors) {
				eMsg += error + "</br>";
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
				eMsg += "No of days should be DOD-DOA+1 </br>";
			}
		}*/
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

		List<UploadDocumentDTO> values = uploadDocumentListenerTableObj
				.getValues();
		if (!bean.getIsFirstPageSubmit()) {
			for (UploadDocumentDTO uploadDocumentDTO : values) {
				if (uploadDocumentDTO.getFileType() != null
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					if (!uploadDocumentDTO.getStatus()) {
						hasError = true;
						eMsg+= "Please Enter Bill Entry Details</br>";
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
					eMsg += error + "</br>";
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
					boolean nomineeAccPrefered = false;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
//							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	

						    /*if(nomineeDetailsDto.getPreference() == null || nomineeDetailsDto.getPreference().isEmpty()) {
						    	eMsg += ("Please Select Account Preference for Selected Nominee No. "+ (tableList.indexOf(nomineeDetailsDto)+1) + ".</br>");
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
					if(selectCnt > 0 && nomineeAccPrefered){
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
					eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)";
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
							PAHealthBillingReviewPagePresenter.COMPARE_WITH_PREVIOUS_ROD,
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
								PAHealthBillingReviewPagePresenter.SAVE_PATIENT_CARE_TABLE_VALUES,
								this.bean);
					}
					if (null != this.bean.getPreauthDataExtractionDetails()
							.getUploadDocumentDTO()
							.getHospitalCashAddonBenefits()
							&& this.bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.getHospitalCashAddonBenefits()) {
						fireViewEvent(
								PAHealthBillingReviewPagePresenter.SAVE_HOSPITAL_CASH_TABLE_VALUES,
								this.bean);
					}
				}

				Date policyFromDate = bean.getNewIntimationDTO().getPolicy()
						.getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate,
						admissionDate.getValue());
				MastersValue policyType = bean.getNewIntimationDTO()
						.getPolicy().getPolicyType();

/*				if ((diffDays != 0 && diffDays > 90)
						|| (policyType != null && policyType.getKey().equals(
								ReferenceTable.RENEWAL_POLICY))
						|| !bean.getAdmissionDatePopup()) {*/
				if(policyType != null && !policyType.getKey().equals(ReferenceTable.FRESH_POLICY)){
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

		cmbPatientStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(
						PAHealthBillingReviewPagePresenter.BILLING_PATIENT_STATUS_CHANGED,
						null);
			}
		});
		
		 cmbRoomCategory.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					SelectValue value = (SelectValue) event.getProperty().getValue();
					//bean.getPreauthDataExtractionDetails().setRoomCategory(value);
					
					if(value != null && value.getValue() != null){
						
						System.out.println(String.format("Room Category Value PA Health : [%s]",value.getValue()));
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

		dischargeDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
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
								PAHealthBillingReviewPagePresenter.BILLING_HOSPITAL_BENEFITS,
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
								PAHealthBillingReviewPagePresenter.BILLING_PATIENT_CARE_BENEFITS,
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
						PAHealthBillingReviewPagePresenter.BILLING_UPDATE_PRODUCT_BASED_AMT,
						bean);

			}
		});
	}

	public void showWarningMsg(String eMsg) {

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

	private void showWarningErrors() {

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
						"<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and current date.</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);

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

	public void showError() {

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
						"<b style = 'color: red;'>Discharge date should not less than Admission Date</b>",
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
			unbindField(cmbTerminateCover);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			mandatoryFields.remove(cmbTerminateCover);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
			patientStatusFLayout.removeComponent(cmbTerminateCover);
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
				wizard.next();
				dialog.close();

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
				if (!bean.getSuspiciousPopupMap().isEmpty()
						&& !bean.getIsPopupMessageOpened()) {
					suspiousPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
					alertMessageForPED();
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
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
				} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
					if(!bean.getShouldShowPostHospAlert()) {
						alertMessageForPostHosp();
					}
				} else if(bean.getIsHospitalDiscountApplicable()){
					alertForHospitalDiscount();
				} else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
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
					}else if (!bean.getSuspiciousPopupMap().isEmpty()
							&& !bean.getIsPopupMessageOpened()) {
						suspiousPopupMessage();
					} else if (bean.getIsPEDInitiated()) {
						alertMessageForPED();
					} else if(!bean.getShouldShowPostHospAlert() && bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
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
				if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						alertMessageForMediPremier(SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				} else if(StarCommonUtils.shouldShowDomicillaryAlert(bean)) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.DOMICILLARY_ALERT_MESSAGE);
				} 
//				bean.setIsHospitalDiscountApplicable(false);
//				bean.setIsPedWatchList(false);
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
					}  else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
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
			
			if(billingProcessButtonObj != null) {
				wholeVLayout.removeComponent(billingProcessButtonObj);
			}

			nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			unbindField(chkNomineeDeceased);
			chkNomineeDeceased = null;
			if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				nomineeDetailsTable.generateSelectColumn();
				nomineeDetailsTable.setScreenName(SHAConstants.PA_HEALTH_BILLING_SCREEN);
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
					
				boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
							
				legalHeirLayout = new VerticalLayout();
				
				legalHeirDetails = legalHeirObj.get();
				
				relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				Map<String,Object> refData = new HashMap<String, Object>();
				relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
				refData.put("relationship", relationshipContainer);
				legalHeirDetails.setReferenceData(refData);
				legalHeirDetails.init(bean);
				legalHeirDetails.setPresenterString(SHAConstants.PA_HEALTH_BILLING_SCREEN);
				legalHeirLayout.addComponent(legalHeirDetails);
				wholeVLayout.addComponent(legalHeirLayout);
				
				if(isNomineeDeceased()){
					enableLegalHeir = Boolean.TRUE;
					nomineeDetailsTable.setEnabled(false);
				}

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
							legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
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
