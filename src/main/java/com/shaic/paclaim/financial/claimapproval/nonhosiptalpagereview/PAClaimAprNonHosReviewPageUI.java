package com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalBillingUI;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.domain.Insured;
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
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversListenerTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.CoverDetailsTobillDetailsMapper;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversListenerTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidateListenerTable;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsListenerTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAClaimAprNonHosReviewPageUI extends ViewComponent {

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;
	
	@EJB
	private PolicyService policyService;

	private GWizard wizard;
	
	private Window popup;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	@Inject
	private Instance<TableBenefitsListenerTable> tableBenefitsListenerTable;
	
	@Inject
	private Instance<OptionalCoversListenerTable> optionalCoversListenerTable;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	
	@Inject
	private Instance<AddOnCoversListenerTable> addOnCoversListenerTable;
	
	@Inject
	private Instance<PABillingConsolidateListenerTable> consolidatedTable;

	@Inject
	private Instance<PAClaimAprNonHosProcessButtonsUIForFirstPage> paClaimAprNonHosProcessButtonInstance;

	private PAClaimAprNonHosProcessButtonsUIForFirstPage paClaimAprNonHosProcessButtonObj;

	
	private TableBenefitsListenerTable tableBenefitsListenerTableObj;
	
	private OptionalCoversListenerTable optionalCoversListenerTableObj;
	
	private AddOnCoversListenerTable addOnCoversListenerTableObj;
	
	private PABillingConsolidateListenerTable consolidatedTableObj;
	

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;



	public Boolean isMappingDone = false;

	public Boolean isMatchTheFollowing = false;

	private Boolean isValid = false;

	private List<PABillingConsolidatedDTO> paBenefitsConsolidatedList;
	
	private List<PABillingConsolidatedDTO> paAddCoversConsolidatedList;
	
	private List<PABillingConsolidatedDTO> paOptionalCoversConsolidatedList;
	
	private OptionGroup optAccidentDeathFlag;
	
	private ComboBox cmbPABenefits;
	
	private TextField txtSumToInsured;

	private TextField txtPayToInsured;
	
	private Double payableToInsureForLetter;
	
	private TextField txtAmountAlreadyPaid;
	
	private TextField txtNetPayableAmt;
	
	private OptionGroup optPaymentMode;
	
	private Button btnIFCSSearch;
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private VerticalLayout paymentDetailsLayout;
	
	public ComboBox cmbPayeeName;
		
	private TextField txtEmailId;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;
	
	private TextField txtAccountPref;
	
	private TextField txtAccType;
	
	private Button btnAccPrefSearch;
	
	private HorizontalLayout accPrefLayout;
	
	private TextField txtRelationship;
	
	private TextField txtNameAsPerBank;
	
	private TextField txtIfscCode;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private TextField txtReasonForChange;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> paymentbinder;
	
	private Button btnPopulatePreviousAccntDetails;
	
	
	private Double addonCoverValue =0d;
	private Double optCoverValue =0d;
	private Double benefitCoverValue =0d;
	private List<BillEntryDetailsDTO> billEntryDetailsDTO;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	// Added below fields for Bypass functionality..............

	//private ComboBox cmbCatastrophicLoss;

	private ComboBox cmbNatureOfLoss;

	private ComboBox cmbCauseOfLoss;


	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;

	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
		
	private BeanItemContainer<SelectValue> relationshipContainer;

	private VerticalLayout legalHeirLayout;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	@Inject
	private Instance<LegalBillingUI> legalBillingUIInstance;

	private LegalBillingUI legalBillingUIObj;
	
	private CheckBox chkNomineeDeceased;
	
    private TextField balancePremium;
	
	private TextField amountPayable;

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
		payableToInsureForLetter = 0d;
		
		bankDetailsTableObj =  bankDetailsTableInstance.get();
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
		this.paymentbinder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.paymentbinder.setItemDataSource(this.bean.getPreauthDataExtractionDetails());
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
	public static Boolean warningMessageForLumpsum(String message) {
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
				alertMessageForLumpsum(SHAConstants.LUMPSUM_ALERT_MESSAGE);
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

		if(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getNewIntimationDTO().getInsuredDeceasedFlag())) {

			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
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
		} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
			if(!bean.getShouldShowPostHospAlert()) {
				alertMessageForPostHosp();
			}
		} else if(bean.getIsHospitalDiscountApplicable()){
			alertForHospitalDiscount();
		} else if(bean.getIsAutoRestorationDone()) {
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
		
		//code added for CR GLX2021016
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			SHAUtils.showPolicyInstalmentAlert();
		}
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag() != null 
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_AROGYA_ALERT,"Information");
		}
		
		isMappingDone = false;
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		paymentbinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		optAccidentDeathFlag = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);
		optAccidentDeathFlag.addItems(getReadioButtonOptions());
		optAccidentDeathFlag.setItemCaption(true, "Accident");
		optAccidentDeathFlag.setItemCaption(false, "Death");
		optAccidentDeathFlag.setStyleName("horizontal");
		optAccidentDeathFlag.setReadOnly(true);
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);
		
		getPaymentDetailsLayout();
		
		
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		dateOfAccident.setReadOnly(true);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);				
		dateOfDeath.setReadOnly(true);
		
		DateField admissionDate = binder.buildAndBind("Date Of Admission" , "admissionDate", DateField.class);
		admissionDate.setReadOnly(true);
		
		DateField dischargeDateForPa = binder.buildAndBind("Date Of Discharge" , "dischargeDateForPa", DateField.class);
		dischargeDateForPa.setReadOnly(true);
		
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		
		FormLayout leftForm = new FormLayout(optAccidentDeathFlag,dateOfAccident,dateOfDeath);
		
		//DateField dtOfAccDeath = new DateField("");
		DateField dtOfAccDeath =  binder.buildAndBind(
				"Date of Accident / Death", "deathDate", DateField.class);
		
		dtOfAccDeath.setReadOnly(true);
		
		FormLayout rightForm = new FormLayout(admissionDate,dischargeDateForPa, /*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		
		HorizontalLayout formHLayout = new HorizontalLayout(leftForm,rightForm);
		
		
		formHLayout.setWidth("100%");

		wholeVLayout = new VerticalLayout(formHLayout,tableBenefitsLayout(),addOnCoversLayout(),optionalCoverLayout(),consolidatedLayout(),paymentDetailsLayout);
		// wholeVLayout = new VerticalLayout(formHLayout,
		// treatmentListenerTableObj, layout, uploadDocumentListenerTableObj,
		// addOnBenefitsPageObj);
		wholeVLayout.setSpacing(true);

		if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				   && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())) {
			
			buildNomineeLayout();
		}

		paClaimAprNonHosProcessButtonObj = paClaimAprNonHosProcessButtonInstance.get();
		paClaimAprNonHosProcessButtonObj.initView(this.bean, this.wizard);
		wholeVLayout.addComponent(paClaimAprNonHosProcessButtonObj);
		addListener();
		loadContainerDataSource();
		showOrHideValidation(false);
		return wholeVLayout;
	}
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}


	private VerticalLayout consolidatedLayout() {
		this.consolidatedTableObj = consolidatedTable.get();
		consolidatedTableObj.init();
		/*if(this.bean.getPreauthDataExtractionDetails().getBillingConsolidatedDTOList() != null && ! this.bean.getPreauthDataExtractionDetails().getBillingConsolidatedDTOList().isEmpty()){
			for (PABillingConsolidatedDTO billingConsolidatedDTO : this.bean.getPreauthDataExtractionDetails().getBillingConsolidatedDTOList()) {
				consolidatedTableObj.addBeanToList(billingConsolidatedDTO);
			}
		}*/
		
		txtAmountAlreadyPaid = binder.buildAndBind("Amount Already Paid" , "alreadyPaidAmt" , TextField.class);
		txtNetPayableAmt = binder.buildAndBind("Net Payable Amount" , "netPayableAmt" , TextField.class);
		txtNetPayableAmt.setNullRepresentation("");
		
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){

			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				this.bean.getConsolidatedAmtDTO().setPremiumAmt(uniqueInstallmentAmount);
				balancePremium = new TextField("Total Balance Instalment Premium");
				balancePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				balancePremium.setValue(uniqueInstallmentAmount != null ? String.valueOf((uniqueInstallmentAmount.intValue()))  : "0");
				balancePremium.setReadOnly(true);

				amountPayable = new TextField("Payable to Insured (After Premium)");
				amountPayable.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				Integer totalPremiumAmount = 0;
				totalPremiumAmount  = SHAUtils.getIntegerFromStringWithComma((txtNetPayableAmt.getValue() != null ? txtNetPayableAmt.getValue() :"0") ) - SHAUtils.getIntegerFromStringWithComma(balancePremium.getValue());
				this.bean.getConsolidatedAmtDTO().setAmountPayableAfterPremium(totalPremiumAmount < 0 ? 0 : totalPremiumAmount);
				amountPayable.setValue((totalPremiumAmount != null && totalPremiumAmount > 0)? String.valueOf((totalPremiumAmount))  : "0");
				amountPayable.setReadOnly(true);
			}
		}
		
		//Double totalPayableAmount = addonCoverValue + optCoverValue + benefitCoverValue;
		//txtPayToInsured = new TextField();
		//txtPayToInsured.setValue(totalPayableAmount.toString());
		//calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
		 FormLayout leftForm = null;
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			 leftForm = new FormLayout(txtAmountAlreadyPaid,txtNetPayableAmt,balancePremium,amountPayable);
		 }else {
			 leftForm = new FormLayout(txtAmountAlreadyPaid,txtNetPayableAmt);
		 }
		
		HorizontalLayout optionaHori = new HorizontalLayout(consolidatedTableObj);
		VerticalLayout legalBillingLayout = new VerticalLayout();
		if(bean.getClaimDTO().getLegalClaim() !=null
		&& bean.getClaimDTO().getLegalClaim().equals("Y")){
			legalBillingUIObj = legalBillingUIInstance.get();
			LegalBillingDTO legalBillingDTO = null;
			if(bean.getLegalBillingDTO() != null){
				legalBillingDTO = bean.getLegalBillingDTO();
			}else{
				legalBillingDTO = policyService.getLegalBillingDetails(bean);
			}
		 	legalBillingUIObj.setLegalBillingDTO(legalBillingDTO);
			legalBillingUIObj.initView(bean,SHAConstants.PA_CLAIM_APPROVAL);
			legalBillingLayout.setCaption("Legal Billing");
			legalBillingLayout.setCaptionAsHtml(true);
			legalBillingLayout.addComponent(legalBillingUIObj);
			legalBillingLayout.setSpacing(true);
			legalBillingLayout.setMargin(true);
		}else	
		{	
			legalBillingLayout.setVisible(false);
		}
		VerticalLayout layout = new VerticalLayout(optionaHori ,legalBillingLayout, leftForm);
		optionaHori.setCaption("Consolidated Table");
		return layout;
	}

	private Panel addOnCoversLayout() {
		this.addOnCoversListenerTableObj = addOnCoversListenerTable.get();
		addOnCoversListenerTableObj.setupReferences(referenceData);
		addOnCoversListenerTableObj.init(this.bean);
		BeanItemContainer<SelectValue> addOnCovercovercontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  addOnCoverList = this.bean.getPreauthDataExtractionDetails().getAddOnCoverListContainer();
		addOnCovercovercontainer.addAll(addOnCoverList);
		
		addOnCoversListenerTableObj.setDropDownValues(addOnCovercovercontainer);
		if(this.bean.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling() != null && ! this.bean.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling().isEmpty()){
			for (AddOnCoversTableDTO benefitsDTO : this.bean.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling()) {
				addOnCoversListenerTableObj.addBeanToList(benefitsDTO);
			}
		}
		Panel optionaHori = new Panel();
		addNetAmountListenerForAddOn(addOnCoversListenerTableObj.netAmtText);
		optionaHori.setContent(addOnCoversListenerTableObj);
		optionaHori.setWidth("100%");
		optionaHori.setCaption("Part II - Add on Covers");
		
		if( null != this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()){
			  if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				 // optionaHori.setEnabled(false);
			  }else{
				 // optionaHori.setEnabled(true);
				}
			}
		
		return optionaHori;
		
	}

	private Panel optionalCoverLayout() {
		this.optionalCoversListenerTableObj = optionalCoversListenerTable.get();
		optionalCoversListenerTableObj.setupReferences(referenceData);
		optionalCoversListenerTableObj.init(this.bean);
		addNetAmountListenerForOptional(optionalCoversListenerTableObj.netAmtText);
		
		BeanItemContainer<SelectValue> optionalCovercontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  optionalCoverList = this.bean.getPreauthDataExtractionDetails().getOptionalCoverListContainer();
		optionalCovercontainer.addAll(optionalCoverList);
		
		optionalCoversListenerTableObj.setDropDownValues(optionalCovercontainer);
		if(this.bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling() != null && ! this.bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling().isEmpty()){
			for (OptionalCoversDTO benefitsDTO : this.bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling()) {
				optionalCoversListenerTableObj.addBeanToList(benefitsDTO);
			}
		}
		Panel optionaHori = new Panel();
		optionaHori.setContent(optionalCoversListenerTableObj);
		optionaHori.setWidth("100%");
		optionaHori.setCaption("Part III - Optional Covers");
		
		if( null != this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()){
			  if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				  //optionaHori.setEnabled(false);
			  }else{
				  //optionaHori.setEnabled(true);
				}
			}
		
		return optionaHori;
	}

	private void addNetAmountListenerForOptional(final TextField netAmtText) {
		
		if(null != netAmtText)
		{
			
			netAmtText.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				//netAmt.setValue(netAmtText.getValue());
				populateConsoilidatedDTO(netAmtText.getValue(),SHAConstants.PA_TABLE_OPT_COVER);
				//calculateTotalAmount(total);
				
			}
		});
		}
	}

	private void addNetAmountListenerForAddOn(final TextField netAmtText) {
		
		if(null != netAmtText)
		{
			
			netAmtText.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				//netAmt.setValue(netAmtText.getValue());
				populateConsoilidatedDTO(netAmtText.getValue(),SHAConstants.PA_TABLE_ADD_COVER);
				//calculateTotalAmount(total);
				
			}
		});
		}
	}

	private VerticalLayout tableBenefitsLayout() {
		
		
		this.tableBenefitsListenerTableObj = tableBenefitsListenerTable.get();
		tableBenefitsListenerTableObj.init(this.bean);
		BeanItemContainer<SelectValue> covercontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  containerList = this.bean.getPreauthDataExtractionDetails().getCoverListContainer();
		if(containerList!=null){
			covercontainer.addAll(containerList);
			tableBenefitsListenerTableObj.setDropDownValues(covercontainer);
		}
		
		BeanItemContainer<SelectValue> coverContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  coverContainerList = this.bean.getPreauthDataExtractionDetails().getCoverListContainer();
		if(null != coverContainerList && !coverContainerList.isEmpty())
			coverContainer.addAll(coverContainerList);
		
		tableBenefitsListenerTableObj.setDropDownValues(coverContainer);
//		TableBenefitsDTO tableBenefitsDTO = new TableBenefitsDTO();
		if(this.bean.getPreauthDataExtractionDetails().getBenefitDTOList() != null && ! this.bean.getPreauthDataExtractionDetails().getBenefitDTOList().isEmpty()){
			for (TableBenefitsDTO benefitsDTO : this.bean.getPreauthDataExtractionDetails().getBenefitDTOList()) {
				tableBenefitsListenerTableObj.addBeanToList(benefitsDTO);
			}
		}
		
		
		cmbPABenefits = (ComboBox) binder.buildAndBind(
				"Benefits", "paBenefits", ComboBox.class);
		BeanItemContainer<SelectValue> benefitsValuecontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  benefitsList = this.bean.getPreauthDataExtractionDetails().getBenefitsValueContainer();
		benefitsValuecontainer.addAll(benefitsList);
		
		
		cmbPABenefits.setContainerDataSource(benefitsValuecontainer);
		cmbPABenefits.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPABenefits.setItemCaptionPropertyId("value");
		
			 for(int i = 0 ; i<benefitsValuecontainer.size() ; i++)
			 {
				if (null != this.bean.getPreauthDataExtractionDetails().getBenefitsValue() && this.bean.getPreauthDataExtractionDetails().getBenefitsValue().equalsIgnoreCase(benefitsValuecontainer.getIdByIndex(i).getValue()))
				{
					cmbPABenefits.setValue(benefitsValuecontainer.getIdByIndex(i));
				}
			}
		
		
		//coverBenefits.setReadOnly(true);

		
		
		
		FormLayout coverForm = new FormLayout(cmbPABenefits);
		cmbPABenefits.setReadOnly(true);
		TextField txtNetAmount = new TextField("A)  Net Amount");
		txtSumToInsured = binder.buildAndBind("B)  Available Sum Insured" , "availableSI" , TextField.class);
		//TextField txtSumToInsured = new TextField("B)  Available Sum Insured")
		TextField txtAmtConsidered = new TextField("C)  Amount Considered");
		
		TextField txtPayableToInsured = binder.buildAndBind("Payable to Insured" , "payableToInsured" , TextField.class);
		//TextField txtPayableToInsured = new TextField("Payable to Insured");
		
		addNetAmountListener(txtPayableToInsured,tableBenefitsListenerTableObj.payableToInsuredAmtText);
		addNetAmountListener(txtNetAmount, tableBenefitsListenerTableObj.netAmtText);
		addNetAmountListener(txtAmtConsidered, tableBenefitsListenerTableObj.approvalAmtText);
		txtAmtConsidered.setReadOnly(Boolean.TRUE);
		if(cmbPABenefits.getValue()==null && this.bean.getPreauthDataExtractionDetails().getBenefitDTOList().isEmpty()){
			txtSumToInsured.setValue("");
			txtPayableToInsured.setValue("");
		}
		txtSumToInsured.setReadOnly(Boolean.TRUE);
		txtPayableToInsured.setNullRepresentation("");
		txtPayableToInsured.setReadOnly(Boolean.TRUE);
		txtNetAmount.setReadOnly(Boolean.TRUE);
		
		FormLayout rightForm = new FormLayout(txtNetAmount,txtSumToInsured);
		
		FormLayout leftForm = new FormLayout(txtAmtConsidered,txtPayableToInsured);
		
		HorizontalLayout hori = new HorizontalLayout(rightForm,leftForm);
		
		
		
		Panel tablePanel = new Panel();
		tablePanel.setContent(tableBenefitsListenerTableObj);
		tablePanel.setWidth("100%");
		
		
		
		VerticalLayout tableBenefitLayout = new VerticalLayout(coverForm,tablePanel,hori);
		tableBenefitLayout.setCaption("Part I - Table Benefits");
		
		if(null != this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()){
			  if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				  //tableBenefitLayout.setEnabled(false);
			  }else{
				  //tableBenefitLayout.setEnabled(true);
				}
			}
		
		return tableBenefitLayout;
		
	}

	private void calculateNetPayableAmt(TextField txtAmountAlreadyPaid,
			TextField txtNetPayableAmt) {
		Double alreadyPaidAmt = this.bean.getPreauthDataExtractionDetails().getAlreadyPaidAmt();
		
		//addNetAmountListener(txtPayToInsured,tableBenefitsListenerTableObj.payableToInsuredAmtText);
		if(alreadyPaidAmt!=null){
			txtAmountAlreadyPaid.setReadOnly(Boolean.FALSE);
			txtAmountAlreadyPaid.setValue(alreadyPaidAmt.toString());
			txtAmountAlreadyPaid.setReadOnly(Boolean.TRUE);
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			try {
				if(txtPayToInsured!=null && txtPayToInsured.getValue()!=null && !txtPayToInsured.getValue().equals("")){
				Number payableToInsured = format.parse(txtPayToInsured.getValue());
				if(payableToInsured!=null){
					Double  netAmt =  payableToInsured.doubleValue() -alreadyPaidAmt;
					if(netAmt < 0){
						netAmt =0d;
					}
					txtNetPayableAmt.setReadOnly(Boolean.FALSE);
					txtNetPayableAmt.setValue(netAmt.toString());
					txtNetPayableAmt.setReadOnly(Boolean.TRUE);
					if(legalBillingUIObj !=null){
						legalBillingUIObj.setawardAmount(String.valueOf(netAmt.longValue()));
					}
				}}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public void addNetAmountListener(final TextField netAmt, final TextField netAmtText){
		
		if(null != netAmtText)
		{
			
			netAmtText.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				netAmt.setReadOnly(Boolean.FALSE);
				netAmt.setValue(netAmtText.getValue());
				netAmt.setReadOnly(Boolean.TRUE);
				if(netAmtText.getCaption()!=null && "payableToInsuredAmtText".equals(netAmtText.getCaption())){
					populateConsoilidatedDTO(netAmtText.getValue(),SHAConstants.PA_TABLE_BENEFITS);
					/*if(netAmtText!=null){
						txtPayToInsured = new TextField();
						if(txtPayToInsured!=null){
							if(!netAmtText.getValue().equals("")){
								txtPayToInsured.setValue(netAmtText.getValue());
								txtAmountAlreadyPaid.setReadOnly(Boolean.FALSE);
								txtNetPayableAmt.setReadOnly(Boolean.FALSE);
								calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
								txtAmountAlreadyPaid.setReadOnly(Boolean.TRUE);
								txtNetPayableAmt.setReadOnly(Boolean.TRUE);
							}}
					}*/
				}
				//calculateTotalAmount(total);
				
			}
		});
		}
	}
	
	
	
	private void populateConsoilidatedDTO(String value,String presenterString)
	{
		
		PABillingConsolidatedDTO consolidatedDTO = new PABillingConsolidatedDTO();
		 Double addonCoverValue =0d;
		 Double optCoverValue =0d;
		 Double benefitCoverValue =0d;
		if(SHAConstants.PA_TABLE_BENEFITS.equalsIgnoreCase(presenterString))
		{
			if(null != paBenefitsConsolidatedList && !paBenefitsConsolidatedList.isEmpty())
			{
				paBenefitsConsolidatedList.clear();
				//benefitCoverValue =0d;
			}
			else
			{
				paBenefitsConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
				//benefitCoverValue =null != value ? Double.valueOf(value):0d;
			}
			consolidatedDTO.setPart("I");
			consolidatedDTO.setBenefitsCover(SHAConstants.PA_TABLE_BENEFITS + "    -    "+ (cmbPABenefits.getValue()!=null ? cmbPABenefits.getValue() : ""));
			consolidatedDTO.setPayableAmount(null != value ? Double.valueOf(value):0d);
			consolidatedDTO.setNetPayableAmount(null != value ? Double.valueOf(value):0d);
			benefitCoverValue = consolidatedDTO.getPayableAmount();
			paBenefitsConsolidatedList.add(consolidatedDTO);
		}
		else if(SHAConstants.PA_TABLE_ADD_COVER.equalsIgnoreCase(presenterString))
		{
			if(null != paAddCoversConsolidatedList && !paAddCoversConsolidatedList.isEmpty())
			{
				paAddCoversConsolidatedList.clear();
				//addonCoverValue = 0d;
			}
			else
			{
				paAddCoversConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
				//addonCoverValue = null != value ? Double.valueOf(value):0d;
			}
			consolidatedDTO.setPart("II");
			consolidatedDTO.setBenefitsCover(SHAConstants.PA_TABLE_ADD_COVER);
			consolidatedDTO.setPayableAmount(null != value ? Double.valueOf(value):0d);
			consolidatedDTO.setNetPayableAmount(null != value ? Double.valueOf(value):0d);
			addonCoverValue = consolidatedDTO.getPayableAmount();
			paAddCoversConsolidatedList.add(consolidatedDTO);
		}
		else if(SHAConstants.PA_TABLE_OPT_COVER.equalsIgnoreCase(presenterString))
		{
			if(null != paOptionalCoversConsolidatedList && !paOptionalCoversConsolidatedList.isEmpty())
			{
				paOptionalCoversConsolidatedList.clear();
				//optCoverValue = 0d;
			}
			else
			{
				paOptionalCoversConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
				//optCoverValue = null != value ? Double.valueOf(value):0d;
			}
			consolidatedDTO.setPart("III");
			consolidatedDTO.setBenefitsCover(SHAConstants.PA_TABLE_OPT_COVER);
			consolidatedDTO.setPayableAmount(null != value ? Double.valueOf(value):0d);
			consolidatedDTO.setNetPayableAmount(null != value ? Double.valueOf(value):0d);
			optCoverValue = consolidatedDTO.getPayableAmount();
			paOptionalCoversConsolidatedList.add(consolidatedDTO);
		}
		setConsolidatedTableValues(addonCoverValue, optCoverValue, benefitCoverValue);
	}
	
	private void setConsolidatedTableValues(Double addonCoverValue,Double optCoverValue,Double benefitCoverValue)
	{
		if(null != consolidatedTableObj)
		{
			consolidatedTableObj.removeAllItems();
			if(null != paBenefitsConsolidatedList)
			{
				addToConsolidatedTable(paBenefitsConsolidatedList);
				if(paBenefitsConsolidatedList.size()>0){
					PABillingConsolidatedDTO paBillingConsolidatedDTO = paBenefitsConsolidatedList.get(0);
					benefitCoverValue = paBillingConsolidatedDTO.getPayableAmount();
					payableToInsureForLetter = benefitCoverValue;
				}
			}
			if(null != paAddCoversConsolidatedList)
			{
				addToConsolidatedTable(paAddCoversConsolidatedList);
				if(paAddCoversConsolidatedList.size()>0){
					PABillingConsolidatedDTO paBillingConsolidatedDTO = paAddCoversConsolidatedList.get(0);
					addonCoverValue = paBillingConsolidatedDTO.getPayableAmount();
				}
			}
			if(null != paOptionalCoversConsolidatedList)
			{
				addToConsolidatedTable(paOptionalCoversConsolidatedList);
				if(paOptionalCoversConsolidatedList.size()>0){
					PABillingConsolidatedDTO paBillingConsolidatedDTO = paOptionalCoversConsolidatedList.get(0);
					optCoverValue = paBillingConsolidatedDTO.getPayableAmount();
				}
			}
			Double totalPayableAmount = addonCoverValue + optCoverValue + benefitCoverValue;
			if(txtPayToInsured!=null){
				txtPayToInsured.setValue(totalPayableAmount.toString());
				if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
					calculateNetPayableAmtForInstalment(txtAmountAlreadyPaid, txtNetPayableAmt,balancePremium,amountPayable);	
				}else {
				calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
				}
			}else{
				txtPayToInsured = new TextField();
				txtPayToInsured.setValue(totalPayableAmount.toString());
				if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
					calculateNetPayableAmtForInstalment(txtAmountAlreadyPaid, txtNetPayableAmt,balancePremium,amountPayable);	
				}else {
				calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
				}
			}
		}
	}
	
	private void addToConsolidatedTable(List<PABillingConsolidatedDTO> consolidatedList)
	{
		if(null != consolidatedTableObj)
		{
			if(null != consolidatedList && !consolidatedList.isEmpty())
			{
				for (PABillingConsolidatedDTO paBillingConsolidatedDTO : consolidatedList) {
					consolidatedTableObj.addBeanToList(paBillingConsolidatedDTO);
				}
			}
		}
	}
	
	protected Collection<String> getRadioButtonOptions() {
		Collection<String> coordinatorValues = new ArrayList<String>(2);
		coordinatorValues.add("Accident");
		coordinatorValues.add("Death");

		return coordinatorValues;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
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
		
		if(null != optionalCoversListenerTableObj && !optionalCoversListenerTableObj.getValues().isEmpty())
		{
			boolean error = optionalCoversListenerTableObj.validateDeductionAmt();
			
			if(error) {
				hasError = true;
				eMsg += "Please Enter Valid Deduction Amount for Medical Extention</br>";
			}	
		}
		
		Boolean accedentDeath = optAccidentDeathFlag.getValue() != null ? (Boolean) optAccidentDeathFlag.getValue() : null;
		Long docRecFromId = bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null ? bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey() : null;

		if(docRecFromId != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromId)
				&& accedentDeath != null 
				&& (accedentDeath
						|| !(!accedentDeath && bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())))){

			if(null != bean.getPreauthDataExtractionDetails().getPaymentModeFlag()
					&& (bean.getPreauthDataExtractionDetails().getPaymentModeFlag()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)
					&& null != txtPayableAt && (null == txtPayableAt.getValue() || ("").equalsIgnoreCase(txtPayableAt.getValue())))
			{
				eMsg += "Please enter payableAt for payment mode cheque/DD";
				hasError = true;

			}


			if(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag()) && this.txtIfscCode  != null && (this.txtIfscCode.getValue() == null || ("").equalsIgnoreCase(this.txtIfscCode.getValue())))
			{
				hasError = true;
				eMsg += "Please enter ifsc code </br>";
			}

			if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()
					&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag())
					&& txtAccntNo != null && (txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty())) {

				hasError = true;
				eMsg += "Please Enter Account No</br>";
			}
		}

		if(null != paClaimAprNonHosProcessButtonObj)
		{
			if (!this.paClaimAprNonHosProcessButtonObj.isValid()) {
				hasError = true;
				List<String> errors = this.paClaimAprNonHosProcessButtonObj.getErrors();
				for (String error : errors) {
					eMsg += error;
				}
			}
		}

		if(accedentDeath != null 
				&& !accedentDeath
				&& docRecFromId != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromId)
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
					}
					else{
						bean.getNewIntimationDTO().setNomineeName(null);
						eMsg += "Please Select Nominee<br>";		
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
					}else{
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

		if(!this.bean.getIsScheduleClicked()){
			hasError = true;
			eMsg += "Please Verify Policy Schedule Button.</br>";
		}
		if(legalBillingUIObj != null){		
			String errmsg = legalBillingUIObj.isValid();
			if(errmsg !=null){
				hasError = true;
				eMsg += errmsg;
			}//IMSSUPPOR-32607 changes done for this support fix
			else if(legalBillingUIObj.getinterestApplicable() && legalBillingUIObj.getPanDetails()) {
				if(txtPanNo.getValue() == null || txtPanNo.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please Enter Pan Number For Legal Settlement.</br>" ;
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
				this.paymentbinder.commit();
				getBillEntryDetails();
				if(legalBillingUIObj !=null){
					bean.setLegalBillingDTO(legalBillingUIObj.getvalue());
					if(legalBillingUIObj.getPanDetails()){
						if(bean.getLegalBillingDTO() !=null && bean.getLegalBillingDTO().getPanNo() == null){
							bean.getLegalBillingDTO().setPanNo(txtPanNo.getValue());
						}
					}	
				}
				// Submit
				
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

/*	private PedDetailsTableDTO setPEDDetailsToDTO(
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
	}*/

	@SuppressWarnings("unchecked")
	public void setTableValuesToDTO() {
		if(null != tableBenefitsListenerTableObj)
		{
			List<TableBenefitsDTO> benefitsList = this.tableBenefitsListenerTableObj.getValues();
			bean.getPreauthDataExtractionDetails().setBenefitDTOList(benefitsList);
		}
		
		if(null != optionalCoversListenerTableObj)
		{
			List<OptionalCoversDTO> optionalCoversList = this.optionalCoversListenerTableObj.getValues();
			bean.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(optionalCoversList);
		}
		
		if(null != addOnCoversListenerTableObj)
		{
			List<AddOnCoversTableDTO> addOnCoversTableDTOs = this.addOnCoversListenerTableObj.getValues();
			bean.getPreauthDataExtractionDetails().setAddOnCoversTableListBilling(addOnCoversTableDTOs);;
		}
		
		if(null != consolidatedTableObj)
		{
			List<PABillingConsolidatedDTO> consolidatedTableList = this.consolidatedTableObj.getValues();
			bean.getPreauthDataExtractionDetails().setPaBillingConsolidatedDTOList(consolidatedTableList);
		}

	}

	
	protected void addListener() {
	
		

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
		
		/*dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((PopupDateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((PopupDateField) event
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
	*/
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
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
		
		if (field != null ) {
			Object propertyId = this.paymentbinder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.paymentbinder.unbind(field);
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
				
				

	}

	/**
	 * Method to validate death date.
	 * 
	 * */
	private void addDeathDateValueChangeListener() {
		
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
		
	}

	private void setCalculatedValue(Integer value1, Integer value2,
			TextField calculatedValueField) {
		Integer calculatedValue = value1 * value2;

		calculatedValueField.setValue(String.valueOf(calculatedValue));
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
					} else if(bean.getPostHospitalizaionFlag() && ( SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						if(!bean.getShouldShowPostHospAlert()) {
							alertMessageForPostHosp();
						}
					} else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
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
		this.bean.setStageKey(ReferenceTable.CLAIM_APPROVAL_STAGE);
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
			paClaimAprNonHosProcessButtonObj
					.buildReferCoordinatorLayout(selectValueContainer);
		} else if (SHAConstants.MEDICAL_APPROVER.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean
					.setStatusKey(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER);
			paClaimAprNonHosProcessButtonObj.buildReferToMedicalApproverLayout();
		} else if (SHAConstants.FINANCIAL.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.CLAIM_APPROVAL_TO_FINANCIAL_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean
					.setStatusKey(ReferenceTable.CLAIM_APPROVAL_TO_FINANCIAL_APPROVER);
			paClaimAprNonHosProcessButtonObj.buildSendToFinancialLayout();
		} else if (SHAConstants.PA_CLAIM_APPROVAL_CANCEL_ROD.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.CLAIM_APPROVAL_CANCEL_ROD)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_CANCEL_ROD);
			paClaimAprNonHosProcessButtonObj.buildCancelRODLayout(selectValueContainer);
		}else if(SHAConstants.REFER_TO_BILLING.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_REFER_TO_BILLING)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_REFER_TO_BILLING);
			paClaimAprNonHosProcessButtonObj.buildReferToBillingLayout();
		}else if(SHAConstants.QUERY.equalsIgnoreCase(eventName)){
			this.paClaimAprNonHosProcessButtonObj.buildQueryLayout(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setIsPaymentQuery("N");
			this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
		}
		else if(SHAConstants.PROCESS_INVESTIGATION_INTIATED.equalsIgnoreCase(eventName)){
			
			 this.paClaimAprNonHosProcessButtonObj.buildInitiateInvestigation(selectValueContainer,ReferenceTable.CLAIM_APPROVAL_INVESTIGATION_STATUS);
			 this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_INVESTIGATION_STATUS);
		}
		else if(SHAConstants.PAYMENT_QUERY_TYPE.equalsIgnoreCase(eventName)){
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				checkForPremiumAndConsolidateAmt();
				if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
					this.paClaimAprNonHosProcessButtonObj.disableOnlyQueryApprove(false);
					SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
					bean.setShouldDetectPremium(false);
				} else {
					bean.setShouldDetectPremium(true);
					this.paClaimAprNonHosProcessButtonObj.buildPaymentQueryLayout(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
					this.bean.setIsPaymentQuery("Y");
					this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
				}
			}else {
			this.paClaimAprNonHosProcessButtonObj.buildPaymentQueryLayout(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
			this.bean.setIsPaymentQuery("Y");
			this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS);
			}
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			
		}
		/*else if(SHAConstants.MEDICAL_APPROVAL.equalsIgnoreCase(eventName)){
		this.paClaimAprNonHosProcessButtonObj.generateFieldsForSuggestApproval(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
		this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
		}*/else if(SHAConstants.REJECTION.equalsIgnoreCase(eventName)){
			this.paClaimAprNonHosProcessButtonObj.buildRejectLayout(selectValueContainer,ReferenceTable.CLAIM_APPROVAL_NON_HOSP_REJECT_STATUS);
			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_NON_HOSP_REJECT_STATUS);
		}
		else if(SHAConstants.SENT_TO_REPLY.equalsIgnoreCase(eventName)){
			this.paClaimAprNonHosProcessButtonObj.buildSendReplyLayout(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
			 this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_SEND_REPLY_STATUS);
			 
			 if(this.bean.getIsReplyToFA() != null && this.bean.getIsReplyToFA()){
				 this.bean.setStatusKey(ReferenceTable.CLAIM_APPROVAL_SEND_REPLY_FA_STATUS);
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
					}
				}
			});
			return true;
		}

		
		protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
		
		private void getPaymentDetailsLayout()
		{
			optPaymentMode = (OptionGroup) paymentbinder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
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
		}
		
		@SuppressWarnings({ "serial", "deprecation" })
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
						if(null != txtAccntNo)
						{
							mandatoryFields.remove(txtAccntNo);
							if(null != txtAccntNo.getValue() )
							{
							bean.getPreauthDataExtractionDetails().setAccountNo(txtAccntNo.getValue());
							}
						}
						if(null != txtIfscCode)
						{
							mandatoryFields.remove(txtIfscCode);
							if(null != txtIfscCode.getValue() )
							{
							bean.getPreauthDataExtractionDetails().setIfscCode(txtIfscCode.getValue());
							}
						}
						//mandatoryFields.add(txtPayableAt)
					//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
						
					}
					else 
					{

						unbindField(getListOfPaymentFields());
						bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
						btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
						btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
						paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
						paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
						paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
						paymentDetailsLayout.addComponent(buildBankTransferLayout());
						//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					}				
				}
			});
			
			
			
			/*
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
			});*/
		}
		
		private List<Field<?>> getListOfPaymentFields()
		{
			List<Field<?>>  fieldList = new ArrayList<Field<?>>();
			fieldList.add(cmbPayeeName);
			fieldList.add(txtEmailId);
			fieldList.add(txtReasonForChange);
			fieldList.add(txtPanNo);
			fieldList.add(txtLegalHeirFirstName);
			/*fieldList.add(txtLegalHeirMiddleName);
			fieldList.add(txtLegalHeirLastName);*/
			fieldList.add(txtAccntNo);
			fieldList.add(txtIfscCode);
			fieldList.add(txtBranch);
			fieldList.add(txtBankName);
			fieldList.add(txtCity);
//			fieldList.add(txtPayableAt);
			return fieldList;
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
			
			if(null != field && !field.isEmpty())
			{
				for (Field<?> field2 : field) {
					if (field2 != null ) {
						Object propertyId = this.paymentbinder.getPropertyId(field2);
						//if (field2!= null && field2.isAttached() && propertyId != null) {
						if (field2!= null  && propertyId != null) {
							this.paymentbinder.unbind(field2);
						}
					}
				}
			}
			
			
		}
		
		private HorizontalLayout buildChequePaymentLayout(Boolean paymentMode)
		{
			if(cmbPayeeName != null){
				unbindField(cmbPayeeName);
			}
			cmbPayeeName = (ComboBox) paymentbinder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
			
		    if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
			BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);

//			payee.addBean(this.bean.getPreauthDataExtractionDetails().getPayeeName());
			
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
			
			txtEmailId = (TextField) paymentbinder.buildAndBind("Email ID", "emailId" , TextField.class);
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
			
			txtReasonForChange = (TextField) paymentbinder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
			
			CSValidator reasonForChangeValidator = new CSValidator();
			reasonForChangeValidator.extend(txtReasonForChange);
			reasonForChangeValidator.setRegExp("^[a-zA-Z]*$");
			reasonForChangeValidator.setPreventInvalidTyping(true);
			txtReasonForChange.setMaxLength(100);
			
			
			txtPanNo = (TextField) paymentbinder.buildAndBind("PAN No","panNo",TextField.class);
			
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
			/*txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
			txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);*/
			
			txtLegalHeirFirstName.setNullRepresentation("");
			/*txtLegalHeirMiddleName.setNullRepresentation("");
			txtLegalHeirLastName.setNullRepresentation("");*/
			
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
			}
			unbindField(txtPayableAt);
			txtPayableAt = (TextField) paymentbinder.buildAndBind("Payable at", "payableAt", TextField.class);
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
				
				/*txtLegalHeirMiddleName.setReadOnly(true);
				txtLegalHeirMiddleName.setEnabled(false);
				
				txtLegalHeirLastName.setReadOnly(true);
				txtLegalHeirLastName.setEnabled(false);*/
				
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
				
				if(this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED))
				{
					accPrefLayout = new HorizontalLayout();
					accPrefLayout.setCaption("Account Preference");
					accPrefLayout.setCaptionAsHtml(true);
					 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
							 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
						 if(txtAccountPref != null){	
							 unbindField(txtAccountPref);
						 }	 
						
						unbindField(txtAccountPref); 
						txtAccountPref = (TextField) paymentbinder.buildAndBind("", "accountPref", TextField.class);
						txtAccountPref.setCaption(null);
						txtAccountPref.setEnabled(false);
						txtAccountPref.setNullRepresentation("");
						btnAccPrefSearch = new Button(); 
						btnAccPrefSearch.setStyleName(ValoTheme.BUTTON_LINK);
						btnAccPrefSearch.setIcon(new ThemeResource("images/search.png"));
						
						btnAccPrefSearch.addClickListener(getAccountTypeSearchListener());
						accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
						accPrefLayout.setComponentAlignment(txtAccountPref,Alignment.TOP_CENTER);
						accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
					 }
				}
				
			}
			
//			GridLayout grid = new GridLayout(5,3);
			
			txtLegalHeirFirstName.setCaption(null);
			txtLegalHeirFirstName.setEnabled(false);
			/*txtLegalHeirMiddleName.setCaption(null);
			txtLegalHeirLastName.setCaption(null);*/
			
			HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirFirstName/*,txtLegalHeirMiddleName,txtLegalHeirLastName*/);
			nameLayout.setComponentAlignment(txtLegalHeirFirstName, Alignment.TOP_LEFT);
			nameLayout.setCaption("Legal Heir Name");
			nameLayout.setWidth("100%");
			nameLayout.setSpacing(true);
			nameLayout.setMargin(false);
			FormLayout formLayout1 = null;
			FormLayout formLayout2 = null;
			HorizontalLayout hLayout = null; 

			if(this.bean.getDocumentReceivedFromId() != null 
					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					/*&&	(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())*/) {
				unbindField(txtRelationship);
				txtRelationship = (TextField) paymentbinder.buildAndBind("Relationship with Proposer", "payeeRelationship", TextField.class);
				txtRelationship.setNullRepresentation("");
				txtRelationship.setEnabled(false);
				
				unbindField(txtNameAsPerBank);
				txtNameAsPerBank = (TextField) paymentbinder.buildAndBind("Name As per Bank Account", "nameAsPerBank", TextField.class);
				txtNameAsPerBank.setNullRepresentation("");
				txtNameAsPerBank.setEnabled(false);
				
				unbindField(txtAccType);
				txtAccType = (TextField) paymentbinder.buildAndBind("Account Type", "accType", TextField.class);
				txtAccType.setNullRepresentation("");
				txtAccType.setEnabled(false);
				
				formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtReasonForChange,txtNameAsPerBank,txtPayableAt,txtPanNo,txtEmailId);
				formLayout1.setMargin(false);
				formLayout2 = new FormLayout(new Label(), txtRelationship);
				 
				 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
					 if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
						 btnAccPrefSearch.setEnabled(true);
					 }
					 else {
						 btnAccPrefSearch.setEnabled(false);
					 }
					accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
					accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
					formLayout2.addComponent(accPrefLayout);
				 }
				 
				 formLayout2.addComponents(txtAccType,nameLayout);
				 formLayout2.setMargin(false);
				
				 hLayout = new HorizontalLayout(formLayout1 ,formLayout2);
				 hLayout.setMargin(true);
			}
			else if(this.bean.getDocumentReceivedFromId() != null 
					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
					/*&& (ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))*/
			{
			
				/*if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())
				{
					formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
				}
				else
				{
					formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo);
				}
				
				hLayout = new HorizontalLayout(formLayout1 , formLayout2);*/
				
				formLayout1 = new FormLayout(optPaymentMode, txtEmailId,txtPanNo, txtPayableAt );
				formLayout1.setMargin(false);
				formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,nameLayout);
				hLayout = new HorizontalLayout(formLayout1 /*,btnLayout*/, formLayout2);
				hLayout.setMargin(true);

				if(! paymentMode){
					
					if(txtPayableAt != null){
						formLayout1.removeComponent(txtPayableAt);
					}
				}
			
			}
			/****
			 * commented Below line of code to sync with Health FA Screen
			 */
			/*else
			{
				unbindField(txtRelationship);
				txtRelationship = (TextField) paymentbinder.buildAndBind("Relationship with Proposer", "payeeRelationship", TextField.class);
				txtRelationship.setNullRepresentation("");
				txtRelationship.setEnabled(false);
				
				unbindField(txtNameAsPerBank);
				txtNameAsPerBank = (TextField) paymentbinder.buildAndBind("Name As per Bank Account", "nameAsPerBank", TextField.class);
				txtNameAsPerBank.setNullRepresentation("");
				txtNameAsPerBank.setEnabled(false);
				
				unbindField(txtAccType);
				txtAccType = (TextField) paymentbinder.buildAndBind("Account Type", "accountType", TextField.class);
				txtAccType.setNullRepresentation("");
				txtAccType.setEnabled(false);
				
				formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtReasonForChange,txtNameAsPerBank,txtAccntNo,txtBankName,txtCity,txtEmailId);
				formLayout1.setMargin(false);
				formLayout2 = new FormLayout(new Label(),txtRelationship);
				
				if(SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				 					&& this.bean.getDocumentReceivedFromId() != null 
				 					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch); 
					accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
					formLayout2.addComponent(accPrefLayout);
				}
				
				HorizontalLayout btnIfscHLayout = new HorizontalLayout(txtIfscCode,btnIFCSSearch);
				btnIfscHLayout.setCaption("IFSC Code");
				txtIfscCode.setCaption(null);
				
				formLayout2.addComponents(txtAccType,btnIfscHLayout,txtBranch,txtPanNo,nameLayout);
				
				formLayout2.setMargin(false);
				
				hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
				hLayout.setMargin(true);
			}*/

//			hLayout.setWidth("90%");
			
//			payeenameListner();
			
			return hLayout;
			
			
		}
		
		private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
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
				payeeValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
				payeeValue.setSourceRiskId(insured.getSourceRiskId());
				payeeValue.setNameAsPerBankAccount(insured.getNameOfAccountHolder());
				
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
			payeeSelValue.setSourceRiskId(policy.getProposerCode());
			payeeSelValue.setRelationshipWithProposer(SHAConstants.RELATIONSHIP_SELF);
			
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
		}
		
		private HorizontalLayout buildBankTransferLayout()
		{
			
			btnIFCSSearch = new Button();
			btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
			btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
			
			addIFSCCodeListner();
			
			txtAccntNo = (TextField)paymentbinder.buildAndBind("Account No" , "accountNo", TextField.class);
			txtAccntNo.setRequired(true);
			txtAccntNo.setNullRepresentation("");
			
			if(null != this.bean.getPreauthDataExtractionDetails().getAccountNo())
			{
				txtAccntNo.setValue(this.bean.getPreauthDataExtractionDetails().getAccountNo());
			}
			
			CSValidator accntNoValidator = new CSValidator();
			accntNoValidator.extend(txtAccntNo);
			accntNoValidator.setRegExp("^[a-z A-Z 0-9]*$");
			accntNoValidator.setPreventInvalidTyping(true);
			
			txtIfscCode = (TextField) paymentbinder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
//			txtIfscCode.setRequired(true);
			txtIfscCode.setNullRepresentation("");
			txtIfscCode.setEnabled(false);
			
			if(null != this.bean.getPreauthDataExtractionDetails().getIfscCode())
			{
				txtIfscCode.setValue(this.bean.getPreauthDataExtractionDetails().getIfscCode());
			}
			
			txtBranch = (TextField) paymentbinder.buildAndBind("Branch", "branch", TextField.class);
			txtBranch.setNullRepresentation("");
			txtBranch.setEnabled(false);
			
			if(null != this.bean.getPreauthDataExtractionDetails().getBranch())
			{
				txtBranch.setValue(this.bean.getPreauthDataExtractionDetails().getBranch());
			}
			
			txtBankName = (TextField) paymentbinder.buildAndBind("Bank Name", "bankName", TextField.class);
			txtBankName.setNullRepresentation("");
			txtBankName.setEnabled(false);
			
			if(null != this.bean.getPreauthDataExtractionDetails().getBankName())
			{
				txtBankName.setValue(this.bean.getPreauthDataExtractionDetails().getBankName());
			}
			
			txtCity = (TextField) paymentbinder.buildAndBind("City", "city", TextField.class);
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
			if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
					&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED))
			{	
				if(txtAccntNo != null){
					txtAccntNo.setReadOnly(false);
					txtAccntNo.setEnabled(true);
				}
				if(txtIfscCode != null){
					txtIfscCode.setReadOnly(false);
					txtIfscCode.setEnabled(true);
				}
				
			}
			
			
			/*TextField dField1 = new TextField();
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
			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);*/
			
			
			
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
//			hLayout.setWidth("80%");
			
			
			/*if(null != txtAccntNo)
			{
				mandatoryFields.add(txtAccntNo);
				setRequiredAndValidation(txtAccntNo);
			}
			
			if(null != txtIfscCode)
			{
				mandatoryFields.add(txtIfscCode);
				setRequiredAndValidation(txtIfscCode);
			}*/
			
			if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& this.bean.getDocumentReceivedFromId() != null
					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& this.optPaymentMode.getValue() != null
					&& (Boolean)this.optPaymentMode.getValue() ){
			
			btnAccPrefSearch.setEnabled(false);
			}
			else {
				if(btnAccPrefSearch != null)
					btnAccPrefSearch.setEnabled(true);
			}
			
			return hLayout;
		}
		
		public void addIFSCCodeListner()
		{
			btnIFCSSearch.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Window popup = new com.vaadin.ui.Window();
					viewSearchCriteriaWindow.setWindowObject(popup);
					viewSearchCriteriaWindow.setPresenterString(SHAConstants.PA_CLAIM_APPROVAL);
					viewSearchCriteriaWindow.initView();
					
					popup.setWidth("75%");
					popup.setHeight("90%");
					popup.setContent(viewSearchCriteriaWindow);
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
			});
			
		}

		public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
			Long docreceivedFrom = this.bean.getDocumentReceivedFromId() != null ? this.bean.getDocumentReceivedFromId() : null;
			Boolean accDeath = optAccidentDeathFlag.getValue() != null ? (Boolean)optAccidentDeathFlag.getValue() : null;
			
			if(docreceivedFrom != null
					&& docreceivedFrom.equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& accDeath != null
					&& !accDeath
					&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
				
				if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty() && !isNomineeDeceased()) {
					
					nomineeDetailsTable.setNomineeBankDetails(dto, nomineeDetailsTable.getBankDetailsTableObj().getNomineeDto());
				}
				else {
					legalHeirDetails.setBankDetails(dto, viewSearchCriteriaWindow.getLegalHeirDto());
				}
				
			}
			else{
				
				if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
							&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
		 					&& docreceivedFrom != null
		 					&& docreceivedFrom.equals(ReferenceTable.RECEIVED_FROM_INSURED)){
						
					txtAccountPref.setValue(dto.getAccPreference());
					txtAccType.setValue(dto.getAccType());
				
					txtAccntNo.setValue(dto.getAccNumber());

					this.bean.setDto(dto);
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
						this.bean.setBankId(dto.getBankId());
					}
				}
				else{
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
						this.bean.setBankId(dto.getBankId());
					}
				
				}
			}
			
		}
		
		public void getBillEntryDetails()
		{
			billEntryDetailsDTO = new ArrayList<BillEntryDetailsDTO>();
			Double paTotalClaimedAmnt = 0d;
			Double paTotalDisAllowance = 0d;
			Double paTotalApprovedAmnt = 0d;
			Double alreadyPaidAmnt = 0d;
			CoverDetailsTobillDetailsMapper coverTobillDetailsMapper = CoverDetailsTobillDetailsMapper.getInstance();
			
			if(null != tableBenefitsListenerTableObj && !tableBenefitsListenerTableObj.getValues().isEmpty())
			{
				List<BillEntryDetailsDTO> benefitsDTOList = coverTobillDetailsMapper.getBenefitsDTO(tableBenefitsListenerTableObj.getValues());
				if(null!= benefitsDTOList && !benefitsDTOList.isEmpty())
				{
					if(null != payableToInsureForLetter){
						paTotalApprovedAmnt = payableToInsureForLetter;
					}
					
				for (BillEntryDetailsDTO billEntryDetailsDTO : benefitsDTOList) {
					//IMSSUPPOR-29828
					if(null != billEntryDetailsDTO.getItemValue()) {
						paTotalClaimedAmnt += billEntryDetailsDTO.getItemValue();
						billEntryDetailsDTO.setApprovedAmountForAssessmentSheet(billEntryDetailsDTO.getItemValue() - (null != billEntryDetailsDTO.getTotalDisallowances() ? billEntryDetailsDTO.getTotalDisallowances() : 0d));
					}
			/*		if(null != billEntryDetailsDTO.getItemValue())
						paTotalClaimedAmnt += billEntryDetailsDTO.getItemValue();*/
					if(null != billEntryDetailsDTO.getTotalDisallowances())
						paTotalDisAllowance += billEntryDetailsDTO.getTotalDisallowances();
					/*if(null != billEntryDetailsDTO.getApprovedAmountForAssessmentSheet())
						paTotalApprovedAmnt += billEntryDetailsDTO.getApprovedAmountForAssessmentSheet();*/
//					billEntryDetailsDTO.setApprovedAmountForAssessmentSheet(paTotalApprovedAmnt);
				}
				
				
				billEntryDetailsDTO.addAll(benefitsDTOList);
			}
			}
			
			if(null != addOnCoversListenerTableObj && !addOnCoversListenerTableObj.getValues().isEmpty())
			{		
				List<BillEntryDetailsDTO> addOnCoversDTOList = coverTobillDetailsMapper.getaddOnCovresDTO(addOnCoversListenerTableObj.getValues());
				if(null!= addOnCoversDTOList && !addOnCoversDTOList.isEmpty())
				{
				for (BillEntryDetailsDTO billEntryDetailsDTO : addOnCoversDTOList) {
					if(null != billEntryDetailsDTO.getItemValue())
						paTotalClaimedAmnt += billEntryDetailsDTO.getItemValue();
					if(null != billEntryDetailsDTO.getTotalDisallowances())
						paTotalDisAllowance += billEntryDetailsDTO.getTotalDisallowances();
					if(null != billEntryDetailsDTO.getApprovedAmountForAssessmentSheet())
						paTotalApprovedAmnt += billEntryDetailsDTO.getApprovedAmountForAssessmentSheet();
				}
				
				billEntryDetailsDTO.addAll(addOnCoversDTOList);
				}
			}
			if(null != optionalCoversListenerTableObj && !optionalCoversListenerTableObj.getValues().isEmpty())
			{
				List<BillEntryDetailsDTO> optionalCoversDTOList = coverTobillDetailsMapper.getOptionalCoversDTO(optionalCoversListenerTableObj.getValues());
				if(null!= optionalCoversDTOList && !optionalCoversDTOList.isEmpty())
				{
				for (BillEntryDetailsDTO billEntryDetailsDTO : optionalCoversDTOList) {
					if(null != billEntryDetailsDTO.getItemValue())
						paTotalClaimedAmnt += billEntryDetailsDTO.getItemValue();
					if(null !=  billEntryDetailsDTO.getTotalDisallowances())
						paTotalDisAllowance += billEntryDetailsDTO.getTotalDisallowances();
					if(null != billEntryDetailsDTO.getApprovedAmountForAssessmentSheet())
						paTotalApprovedAmnt += billEntryDetailsDTO.getApprovedAmountForAssessmentSheet();
				}
				
				billEntryDetailsDTO.addAll(optionalCoversDTOList);
				}
			}
			
			
			bean.setBillEntryDetailsDTO(billEntryDetailsDTO);
			bean.setPaTotalClaimedAmnt(paTotalClaimedAmnt);
			bean.setPaTotalDisAllowance(paTotalDisAllowance);
			bean.setPaTotalApprovedAmnt(paTotalApprovedAmnt);
			
		//	return billEntryDetailsDTO;
			
		}

		
		public void resetConsolidatedList()
		{
			if(null != paAddCoversConsolidatedList)
			{
				paAddCoversConsolidatedList.clear();
			}
			else
			{
				paAddCoversConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
			}
			if(null != paOptionalCoversConsolidatedList)
			{
				paOptionalCoversConsolidatedList.clear();
			}
			else
			{
				paOptionalCoversConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
			}
			if(null != paBenefitsConsolidatedList)
			{
				paBenefitsConsolidatedList.clear();
			}
			else
			{
				paBenefitsConsolidatedList = new ArrayList<PABillingConsolidatedDTO>();
			}	
		}

		public void generateFieldFisitButton(Object dropDownValues) {
			if (this.bean.getStatusKey() != null
					&& !this.bean
							.getStatusKey()
							.equals(ReferenceTable.CLAIM_APPROVAL_FIELD_VISIT_STATUS)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setAllocationTo(null);
				this.bean.getPreauthMedicalDecisionDetails()
						.setFvrTriggerPoints("");

			}
			this.paClaimAprNonHosProcessButtonObj
					.buildInitiateFieldVisit(
							dropDownValues,
							ReferenceTable.CLAIM_APPROVAL_FIELD_VISIT_STATUS);
			this.bean
					.setStatusKey(ReferenceTable.CLAIM_APPROVAL_FIELD_VISIT_STATUS);

			
		}
		
		public void buildNomineeLayout() {
			
			nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			unbindField(chkNomineeDeceased);
			chkNomineeDeceased = null;
			if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) { 
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				nomineeDetailsTable.generateSelectColumn();
				nomineeDetailsTable.setScreenName(SHAConstants.PA_CLAIM_APPROVAL);
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
					
			FormLayout legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
			
			legalHeirLayout = new VerticalLayout();
			
			legalHeirDetails = legalHeirObj.get();
			
			relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			Map<String,Object> refData = new HashMap<String, Object>();
			relationshipContainer.addAll(bean.getLegalHeirDto().getRelationshipContainer());
			refData.put("relationship", relationshipContainer);
			legalHeirDetails.setReferenceData(refData);
			legalHeirDetails.init(bean);
			legalHeirDetails.setPresenterString(SHAConstants.PA_CLAIM_APPROVAL);
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
			
			if(paymentDetailsLayout != null) {
				cmbPayeeName.setReadOnly(false);
				cmbPayeeName.setValue(null);
				cmbPayeeName.setEnabled(false);
				/*txtLegalHeirName.setVisible(false);*/
				if(btnAccPrefSearch != null ) 
					btnAccPrefSearch.setEnabled(false);
				optPaymentMode.setEnabled(false);
				if(btnIFCSSearch != null ){
					btnIFCSSearch.setEnabled(false);
				}
				
				if(txtAccntNo != null)
					txtAccntNo.setRequired(false);
			}		
		
		}

		private ClickListener getAccountTypeSearchListener(){
			
			ClickListener listener = new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {				
					final Window popup = new com.vaadin.ui.Window();
				//	List<BankDetailsTableDTO> verificationAccountDeatilsList = bean.getVerificationAccountDeatilsTableDTO();
					SelectValue value = (SelectValue) cmbPayeeName.getValue();
					ReceiptOfDocumentsDTO beanDto = new ReceiptOfDocumentsDTO();
					DocumentDetailsDTO documentDetails = beanDto.getDocumentDetails();
					documentDetails.setPayeeName(value);
					documentDetails.getPayeeName().setSourceRiskId(value.getSourceRiskId());
					beanDto.setDocumentDetails(documentDetails);
					beanDto.setSourceRiskID(value.getSourceRiskId());
//					bankDetailsTableObj =  bankDetailsTableInstance.get();
					bankDetailsTableObj.init(beanDto);
					bankDetailsTableObj.initPresenter(SHAConstants.PA_CLAIM_APPROVAL);
					bankDetailsTableObj.setCaption("Bank Details");
					/*if(verificationAccountDeatilsList != null){
						verificationAccountDeatilsTableObj.setTableList(verificationAccountDeatilsList);
					}*/
					
					popup.setWidth("75%");
					popup.setHeight("70%");
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
					Button okBtn = new Button("Cancel");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					okBtn.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							List<BankDetailsTableDTO> bankDetailsTableDTO = bankDetailsTableObj.getValues();
							bankDetailsTableDTO = new ArrayList<BankDetailsTableDTO>();
							//bean.setVerificationAccountDeatilsTableDTO(bankDetailsTableDTO);
							popup.close();
						}
					});
			
					VerticalLayout vlayout = new VerticalLayout(bankDetailsTableObj);
					HorizontalLayout hLayout = new HorizontalLayout(okBtn);
					hLayout.setSpacing(false);
					vlayout.setMargin(false);
					vlayout.addComponent(hLayout);
					vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
					popup.setContent(vlayout);
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
	        		
			 }
			};
			return listener;
		}
		
		public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {
			bankDetailsTableObj.setUpAddBankIFSCDetails(dto);
		}
		
		public void loadContainerDataSource(){
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

		}

		public void setupPanDetailsMandatory(Boolean panDetails){
			if(panDetails !=null
					&& panDetails){
				if(txtPanNo !=null
						&& (txtPanNo.getValue() == null || txtPanNo.getValue().equals(""))){
					txtPanNo.setEnabled(true);
					mandatoryFields.add(txtPanNo);
					showOrHideValidation(false);
				}

			}else{
				mandatoryFields.remove(txtPanNo);
			}
		}
		//R1022
		public void populateApproveAmntOPT(List<OptionalCoversDTO> optListBilling) {
			optionalCoversListenerTableObj.removeandAddAllItems(optListBilling);	
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
		 
		 private void calculateNetPayableAmtForInstalment(TextField txtAmountAlreadyPaid,
				 TextField txtNetPayableAmt,TextField balancePremium,TextField afterPremiumPayableToInsured) {
				Double alreadyPaidAmt = this.bean.getPreauthDataExtractionDetails().getAlreadyPaidAmt();
				
				//addNetAmountListener(txtPayToInsured,tableBenefitsListenerTableObj.payableToInsuredAmtText);
				if(alreadyPaidAmt!=null){
					txtAmountAlreadyPaid.setReadOnly(Boolean.FALSE);
					txtAmountAlreadyPaid.setValue(alreadyPaidAmt.toString());
					txtAmountAlreadyPaid.setReadOnly(Boolean.TRUE);
					NumberFormat format = NumberFormat.getInstance(Locale.US);
					try {
						if(txtPayToInsured!=null && txtPayToInsured.getValue()!=null && !txtPayToInsured.getValue().equals("")){
						Number payableToInsured = format.parse(txtPayToInsured.getValue());
						if(payableToInsured!=null){
							Double  netAmt =  payableToInsured.doubleValue() -alreadyPaidAmt;
							if(netAmt < 0){
								netAmt =0d;
							}
							Double  netAmtAfterPremium =  netAmt.doubleValue() -Double.valueOf(balancePremium.getValue());
							if(netAmtAfterPremium < 0){
								netAmtAfterPremium =0d;
							}else if (netAmtAfterPremium == 0){
								this.bean.setIsPremiumInstAmtEql(true);
							}
							txtNetPayableAmt.setReadOnly(Boolean.FALSE);
							txtNetPayableAmt.setValue(netAmt.toString());
							txtNetPayableAmt.setReadOnly(Boolean.TRUE);
							afterPremiumPayableToInsured.setReadOnly(Boolean.FALSE);
							afterPremiumPayableToInsured.setValue(netAmtAfterPremium.toString());
							this.bean.getConsolidatedAmtDTO().setAmountPayableAfterPremium(netAmtAfterPremium.intValue());
							this.bean.setUniqueDeductedAmount(netAmtAfterPremium);
							this.bean.setPolicyInstalmentPremiumAmt(Double.valueOf(balancePremium.getValue()));
							afterPremiumPayableToInsured.setReadOnly(Boolean.TRUE);
							if(legalBillingUIObj !=null){
								legalBillingUIObj.setawardAmount(String.valueOf(netAmt.longValue()));
							}
						}}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		 
		 
		 public void checkForPremiumAndConsolidateAmt(){
			 Long input1 =0l;
			 String input2 = null;
			 java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
			 java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();

			 java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
			 java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
			 Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(this.bean.getConsolidatedAmtDTO().getAmountPayableAfterPremium().toString());
			 //added for instalment issue by noufel
			 if( bean.getClaimDTO() != null && bean.getClaimDTO().getClaimType() != null 
					 && bean.getClaimDTO().getClaimType().getId() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				 Integer premiumAmount = bean.getPolicyInstalmentPremiumAmt().intValue();
				 approvedAmt = approvedAmt + premiumAmount;
			 }

			 //				if(bean.getIsPremiumInstAmtEql()){
			 //					approvedAmt = hospitalizaionObj.getPremiumAmountEqualZero();
			 //				}
			 
			 if(bean.getIsPremiumInstAmtEql()){
				 approvedAmt =  bean.getPolicyInstalmentPremiumAmt().intValue();
			 }
				DBCalculationService dbService = new DBCalculationService();
			 Map<String, String> getPolicyInstallmentDetails = dbService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
					 bean.getPolicyInstalmentPremiumAmt(),admissionDate,approvedAmt.doubleValue(),
					 instalmentDate,input1,input2);
			 if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
				 bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
				 bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
			 }
		 }
}
