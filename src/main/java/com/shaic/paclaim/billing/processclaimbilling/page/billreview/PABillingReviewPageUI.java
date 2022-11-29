package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalBillingUI;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
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
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PABillingReviewPageUI extends ViewComponent {

	private static final long serialVersionUID = -8077475767907171312L;

	@Inject
	private PreauthDTO bean;
	
	private Window popup;

	private GWizard wizard;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	@Inject
	private Instance<TableBenefitsListenerTable> tableBenefitsListenerTable;
	
	@Inject
	private Instance<OptionalCoversListenerTable> optionalCoversListenerTable;
	
	
	@Inject
	private Instance<AddOnCoversListenerTable> addOnCoversListenerTable;
	
	@Inject
	private Instance<PABillingConsolidateListenerTable> consolidatedTable;
	
	@Inject
	private Instance<PABillingProcessButtonsUIForFirstPage> paBillingProcessButtonInstance;

	private PABillingProcessButtonsUIForFirstPage paBillingProcessButtonObj;

	
	private TableBenefitsListenerTable tableBenefitsListenerTableObj;
	
	private OptionalCoversListenerTable optionalCoversListenerTableObj;
	
	private AddOnCoversListenerTable addOnCoversListenerTableObj;
	
	private PABillingConsolidateListenerTable consolidatedTableObj;
	

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	VerticalLayout tableVLayout;

	private VerticalLayout legalHeirLayout;

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
	
	private TextArea txtAreaBillingInternalRemarks;
	
	private List<BillEntryDetailsDTO> billEntryDetailsDTO;	
	
	private TextField txtCAReasonForReferringFromBilling;
	
	private TextField txtClaimApproverRemarks;
	
	private TextField txtFAReasonForRefferingFromBilling;
	
	private TextField txtFinancialApproverRemarks;
	
	private TextField txtMAReasonForRefferingFromBilling;
	
	private TextField txtMedicalApproverRemarks;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	
	//private ComboBox cmbCatastrophicLoss;
	private ComboBox cmbNatureOfLoss;
	private ComboBox cmbCauseOfLoss;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	// Added below fields for Bypass functionality..............
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@Inject
	private Instance<LegalBillingUI> legalBillingUIInstance;

	private LegalBillingUI legalBillingUIObj;
	
	@EJB
	private PolicyService policyService;

	@EJB
	private DBCalculationService dbCalculationService;
	
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
		payableToInsureForLetter = 0d;
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
		/*
		if (this.bean.getClaimCount() > 1) {
			alertMessageForClaimCount(this.bean.getClaimCount());
		}*/
		
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
		isMappingDone = false;
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		optAccidentDeathFlag = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);
		optAccidentDeathFlag.addItems(getReadioButtonOptions());
		optAccidentDeathFlag.setItemCaption(true, "Accident");
		optAccidentDeathFlag.setItemCaption(false, "Death");
		optAccidentDeathFlag.setStyleName("horizontal");
		optAccidentDeathFlag.setReadOnly(true);	
		
		
		
		//DateField dtOfAccDeath = new DateField("");
		DateField dtOfAccDeath =  binder.buildAndBind(
				"Date of Accident / Death", "deathDate", DateField.class);
		
		//dtOfAccDeath.setReadOnly(true);
		
		dateOfAccident = binder.buildAndBind("Date Of Accident" , "dateOfAccident", DateField.class);
		dateOfAccident.setReadOnly(true);
		
		 dateOfDeath = binder.buildAndBind("Date Of Death" , "dateOfDeath", DateField.class);
		dateOfDeath.setReadOnly(true);
		
		dateOfDisablement = binder.buildAndBind("Date Of Disablement" , "dateOfDisablement", DateField.class);
		dateOfDisablement.setReadOnly(true);
		
		DateField admissionDate = binder.buildAndBind("Date Of Admission" , "admissionDate", DateField.class);
		//admissionDate.setReadOnly(true);
		
		DateField dischargeDateForPa = binder.buildAndBind("Date Of Discharge" , "dischargeDateForPa", DateField.class);
		//dischargeDateForPa.setReadOnly(true);
		
		/*cmbCatastrophicLoss = (ComboBox) binder.buildAndBind(
				"Catastrophe Loss", "catastrophicLoss", ComboBox.class);*/
		
		cmbNatureOfLoss = (ComboBox) binder.buildAndBind(
				"Nature Of Loss", "natureOfLoss", ComboBox.class);
		
		cmbCauseOfLoss = (ComboBox) binder.buildAndBind(
				"Cause Of Loss", "causeOfLoss", ComboBox.class);
		
		FormLayout rightForm = new FormLayout(dateOfAccident,dateOfDeath,dateOfDisablement,/*cmbCatastrophicLoss,*/cmbNatureOfLoss,cmbCauseOfLoss);
		
		FormLayout leftForm = new FormLayout(optAccidentDeathFlag,admissionDate,dischargeDateForPa);
		
		HorizontalLayout formHLayout = new HorizontalLayout(leftForm,rightForm);
		
		
		formHLayout.setWidth("100%");

		
		wholeVLayout = new VerticalLayout(formHLayout,buildRefferedToMedicalLayout(),tableBenefitsLayout(),addOnCoversLayout(),optionalCoverLayout(),consolidatedLayout());
		// wholeVLayout = new VerticalLayout(formHLayout,
		// treatmentListenerTableObj, layout, uploadDocumentListenerTableObj,
		// addOnBenefitsPageObj);
		wholeVLayout.setSpacing(true);


		
		paBillingProcessButtonObj = paBillingProcessButtonInstance.get();
		paBillingProcessButtonObj.initView(this.bean, this.wizard);
		wholeVLayout.addComponent(paBillingProcessButtonObj);
		if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				&& (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())) {
			
			buildNomineeLayout();
		}
		addListener();
		showOrHideValidation(false);
		//addOnCoversListenerTableObj.onLoad = Boolean.FALSE;
		return wholeVLayout;
	}
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
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
			this.cmbCatastrophicLoss.setValue(this.bean
					.getPreauthDataExtractionDetails().getCatastrophicLoss());
		}*/
		if (this.bean.getPreauthDataExtractionDetails() != null
				&& this.bean.getPreauthDataExtractionDetails()
						.getNatureOfLoss() != null) {
			this.cmbNatureOfLoss.setValue(this.bean
					.getPreauthDataExtractionDetails().getNatureOfLoss());
		}
		if (this.bean.getPreauthDataExtractionDetails() != null
				&& this.bean.getPreauthDataExtractionDetails().getCauseOfLoss() != null) {
			this.cmbCauseOfLoss.setValue(this.bean
					.getPreauthDataExtractionDetails().getCauseOfLoss());
		}
		
		//IMSSUPPOR-30475
		if(addOnCoversListenerTableObj != null){
			addOnCoversListenerTableObj.setupReferences(referenceData);
		}
		if(optionalCoversListenerTableObj != null){
			optionalCoversListenerTableObj.setupReferences(referenceData);
		}
	}

	
	  private HorizontalLayout buildRefferedToMedicalLayout()
	  {
		  txtCAReasonForReferringFromBilling = (TextField) binder.buildAndBind(
					"Reason for reffering to Billing", "caReasonForRefferingToBilling", TextField.class);
		  txtCAReasonForReferringFromBilling.setEnabled(false);
		  	 
		  txtClaimApproverRemarks = (TextField) binder.buildAndBind(
					"Claim Approver Remarks", "caApproverRemarks", TextField.class);
		  txtClaimApproverRemarks.setEnabled(false);
			
		  txtFAReasonForRefferingFromBilling = (TextField) binder.buildAndBind(
					"Reason for reffering to Billing", "faReasonForRefferingToBilling", TextField.class);
		  txtFAReasonForRefferingFromBilling.setEnabled(false);
			
		  txtFinancialApproverRemarks =  (TextField) binder.buildAndBind(
					"Financial Approver Remarks Remarks", "faApproverRemarks", TextField.class);		    
		  txtFinancialApproverRemarks.setEnabled(false);
			
		
		  txtMAReasonForRefferingFromBilling = (TextField) binder.buildAndBind(
					"Reason for reffering to Billing", "maReasonForRefferingToBilling", TextField.class);
		  txtMAReasonForRefferingFromBilling.setEnabled(false);
			
		  txtMedicalApproverRemarks =  (TextField) binder.buildAndBind(
					"Medical Approver Remarks ", "maApproverRemarks", TextField.class);
		    
		  txtMedicalApproverRemarks.setEnabled(false);
			
			FormLayout billingDetailsFormLayout = new FormLayout (txtCAReasonForReferringFromBilling, txtClaimApproverRemarks);
			billingDetailsFormLayout.setCaption("Claim Approval Details");
			
			FormLayout claimApprovalFormLayout  = new FormLayout (txtFAReasonForRefferingFromBilling,txtFinancialApproverRemarks);
			claimApprovalFormLayout.setCaption("Financial Approval Details");
			
			FormLayout medicalFormLayout  = new FormLayout (txtMAReasonForRefferingFromBilling,txtMedicalApproverRemarks);
			medicalFormLayout.setCaption("Medical Approval Details");
			
			HorizontalLayout refferToMedicalLayout = new HorizontalLayout();
			refferToMedicalLayout.addComponent(billingDetailsFormLayout);
			refferToMedicalLayout.addComponent(claimApprovalFormLayout);
			refferToMedicalLayout.addComponent(medicalFormLayout);
		
			
			return refferToMedicalLayout;
			
			
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
		
		// Billing Internal Remarks
		unbindField(txtAreaBillingInternalRemarks);
		txtAreaBillingInternalRemarks = (TextArea) binder.buildAndBind("Billing Non-Hospitalization Internal Remarks", "billingInternalRemarks", TextArea.class);
		txtAreaBillingInternalRemarks.setMaxLength(4000);
		txtAreaBillingInternalRemarks.setNullRepresentation("");
		txtAreaBillingInternalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		if(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks() != null) {
			txtAreaBillingInternalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks());
		}
		billingInternalRemarksChangeListener(txtAreaBillingInternalRemarks, null);
		
		//Double totalPayableAmount = addonCoverValue + optCoverValue + benefitCoverValue;
		//txtPayToInsured = new TextField();
		//txtPayToInsured.setValue(totalPayableAmount.toString());
		//calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
		FormLayout leftForm = new FormLayout(txtAmountAlreadyPaid,txtNetPayableAmt,txtAreaBillingInternalRemarks);
		
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
			legalBillingUIObj.initView(bean,SHAConstants.PA_BILLING);
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
					
					//this.bean.getPreauthDataExtractionDetails().setPayableToInsured(netAmt);
					
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
	
	public void addToCoverList(AddOnCoversTableDTO benefitsDTO){
		addOnCoversListenerTableObj.addBeanToList(benefitsDTO);
		//addOnCoversLayout();
	}

	private Panel addOnCoversLayout() {
		this.addOnCoversListenerTableObj = addOnCoversListenerTable.get();
		addOnCoversListenerTableObj.init(this.bean);
		
		BeanItemContainer<SelectValue> addOnCoverListContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  containerList = this.bean.getPreauthDataExtractionDetails().getAddOnCoverListContainer();
		addOnCoverListContainer.addAll(containerList);
		
		addOnCoversListenerTableObj.setDropDownValues(addOnCoverListContainer);
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
		
		/*if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() && null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()){
			  if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				  optionaHori.setEnabled(false);
			  }else{
				  optionaHori.setEnabled(true);
				}
			}*/
		
		return optionaHori;
		
	}

	private Panel optionalCoverLayout() {
		this.optionalCoversListenerTableObj = optionalCoversListenerTable.get();
		optionalCoversListenerTableObj.init(this.bean);
		addNetAmountListenerForOptional(optionalCoversListenerTableObj.netAmtText);
		
		
		
		BeanItemContainer<SelectValue> optionalCovercontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<SelectValue>  OptionalCoverList = this.bean.getPreauthDataExtractionDetails().getOptionalCoverListContainer();
		optionalCovercontainer.addAll(OptionalCoverList);
		
		
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
		
		/*if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() && null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()){
			  if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				  optionaHori.setEnabled(false);
			  }else{
				  optionaHori.setEnabled(true);
				}
			}*/
				
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
		if(this.bean.getPreauthDataExtractionDetails().getAvailableSI() != null ){
		double calSI= Double.valueOf(this.bean.getPreauthDataExtractionDetails().getAvailableSI());
//		originalSI.setValue(insuredSumInsured != null ? insuredSumInsured.toString() : "0");
		BigDecimal bigDecimal = new BigDecimal(calSI);
		bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
		txtSumToInsured.setValue(bigDecimal.toString());
		}
		txtSumToInsured.setReadOnly(true);
		//TextField txtSumToInsured = new TextField("B)  Available Sum Insured")
		TextField txtAmtConsidered = new TextField("C)  Amount Considered");
		
		TextField txtPayableToInsured = binder.buildAndBind("Payable to Insured" , "payableToInsured" , TextField.class);
		//TextField txtPayableToInsured = new TextField("Payable to Insured");
		
		addNetAmountListener(txtPayableToInsured,tableBenefitsListenerTableObj.payableToInsuredAmtText);
		addNetAmountListener(txtNetAmount, tableBenefitsListenerTableObj.netAmtText);
		addNetAmountListener(txtAmtConsidered, tableBenefitsListenerTableObj.approvalAmtText);
		txtAmtConsidered.setReadOnly(Boolean.TRUE);
		if(cmbPABenefits.getValue()==null && this.bean.getPreauthDataExtractionDetails().getBenefitDTOList().isEmpty()){
			//IMSSUPPOR-30385
			txtSumToInsured.setReadOnly(Boolean.FALSE);
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
		
		if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() && null != this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()){
			  if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && this.bean.getIsPaymentSettled()){
				  //tableBenefitLayout.setEnabled(false);
			  }else{
				  //tableBenefitLayout.setEnabled(true);
				}
			}
		
		return tableBenefitLayout;
		
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
//				if(netAmt != null && netAmtText.getValue() != null){
//					 List<OptionalCoversDTO> optCoverDTOListPrc = dbCalculationService.getOptValuesForMedicalExtension(bean.getKey(),SHAUtils.getLongFromString(netAmtText.getValue()));
//					 
////					 bean.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(paOptionalCoverListByRodKey);
//					 List<OptionalCoversDTO> optionalCoverDtls = bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling();
//					 for (OptionalCoversDTO optionalCoversDTO : optionalCoverDtls) {
//						 if(null != optCoverDTOListPrc && !optCoverDTOListPrc.isEmpty())
//							{
//								for (OptionalCoversDTO optionalCoversDTOList : optCoverDTOListPrc) {
//									/*if(coverId.equals(optionalCoversDTO.getCoverId()))
//									{*/
//										optionalCoversDTO.setNoOfDaysUtilised(optionalCoversDTOList.getNoOfDaysUtilised());
//										optionalCoversDTO.setNoOfDaysAvailable(optionalCoversDTOList.getNoOfDaysAvailable());
//										optionalCoversDTO.setAllowedAmountPerDay(optionalCoversDTOList.getAllowedAmountPerDay());
//										optionalCoversDTO.setMaxNoOfDaysPerHospital(optionalCoversDTOList.getMaxNoOfDaysPerHospital());
//										optionalCoversDTO.setMaxDaysAllowed(optionalCoversDTOList.getMaxDaysAllowed());
//										optionalCoversDTO.setSiLimit(optionalCoversDTOList.getSiLimit());
//										optionalCoversDTO.setLimit(optionalCoversDTOList.getLimit());
//										optionalCoversDTO.setBalanceSI(optionalCoversDTOList.getBalanceSI());
//										
////									}
//									
//								}
//							}
//					}
//					 if(bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling() != null && ! bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling().isEmpty()){
//							for (OptionalCoversDTO benefitsDTO : bean.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling()) {
//								optionalCoversListenerTableObj.addBeanToList(benefitsDTO);
//							}
//						}
//				}
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
				calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
			}else{
				txtPayToInsured = new TextField();
				txtPayToInsured.setValue(totalPayableAmount.toString());
				calculateNetPayableAmt(txtAmountAlreadyPaid, txtNetPayableAmt);
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
		
		
		if(null != paBillingProcessButtonObj)
		{
			if (!this.paBillingProcessButtonObj.isValid()) {
				hasError = true;
				List<String> errors = this.paBillingProcessButtonObj.getErrors();
				for (String error : errors) {
					eMsg += error;
				}
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
		
		if(null != consolidatedTableObj)
		{
			Double amtPayableToInsured = 0d;
			List<PABillingConsolidatedDTO> paConsolidatedDTO = consolidatedTableObj.getValues();
			if(null != paConsolidatedDTO && !paConsolidatedDTO.isEmpty())
			{
				for (PABillingConsolidatedDTO paBillingConsolidatedDTO : paConsolidatedDTO) {
					if(null != paBillingConsolidatedDTO.getPayableAmount())
					{
						amtPayableToInsured += paBillingConsolidatedDTO.getPayableAmount();
					}
				}
					bean.getPreauthMedicalDecisionDetails().setBillingApprovedAmt(amtPayableToInsured);
			}
		}

		Boolean accedentDeath = bean.getPreauthDataExtractionDetails().getAccidentOrDeath();
		
		Long docRecFromId = bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null ? bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey() : null; 
		if(accedentDeath != null 
				&& !accedentDeath
				&& docRecFromId != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromId)
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
//							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
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
				
				/*// Below Condition for Account Preference mandatory for Bancs
				List<LegalHeirDTO> legalHeirDtls = legalHeirDetails.getValues();
				 if(legalHeirDtls != null && !legalHeirDtls.isEmpty()) {
					 for (LegalHeirDTO legalHeirDtlsDTO : legalHeirDtls) {
							if(legalHeirDtlsDTO.getAccountPreference() == null || legalHeirDtlsDTO.getAccountPreference().getValue() == null){
								bean.setLegalHeirDTOList(null);
								hasError = true;
								eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %,Account Preference)";
							}
					}
				 }*/
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
			    if(this.bean.getPreauthDataExtractionDetails().getDischargeDateForPa() != null){
			    	this.bean.getPreauthDataExtractionDetails().setDischargeDate(this.bean.getPreauthDataExtractionDetails().getDischargeDateForPa());
			    }
				getBillEntryDetails();
				if(legalBillingUIObj !=null){
					bean.setLegalBillingDTO(legalBillingUIObj.getvalue());
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
		
		// CR2019100 -  Deduction for Medical Extention ROD
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
			paBillingProcessButtonObj
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
			paBillingProcessButtonObj.buildReferToMedicalApproverLayout();
		} else if (SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(eventName)) {
			if (this.bean.getStatusKey() != null
					&& !this.bean.getStatusKey().equals(
							ReferenceTable.PA_BILLING_SEND_TO_CLAIM_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails()
						.setReasonForReferring("");
			}
			this.bean
					.setStatusKey(ReferenceTable.PA_BILLING_SEND_TO_CLAIM_APPROVER);
			paBillingProcessButtonObj.buildSendToClaimApprovalLayout();
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
			paBillingProcessButtonObj.buildCancelRODLayout(selectValueContainer);
		}else if(SHAConstants.REFER_TO_BILL_ENTRY.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY);
			paBillingProcessButtonObj.buildReferToBillEntryLayout();
		}
		
		else if(SHAConstants.PA_BILLING_SEND_TO_FA.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.PA_BILLING_SEND_TO_FA)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.PA_BILLING_SEND_TO_FA);
			paBillingProcessButtonObj.buildSendToFinancialLayout();
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
		
		protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
		public void getBillEntryDetails()
		{
			billEntryDetailsDTO = new ArrayList<BillEntryDetailsDTO>();
			Double paTotalClaimedAmnt = 0d;
			Double paTotalDisAllowance = 0d;
			Double paTotalApprovedAmnt = 0d;
			Double alreadyPaidAmnt = 0d;
			StringBuffer benefitsSelected = new StringBuffer("");
			StringBuffer addOnSelected = new StringBuffer("");
			StringBuffer optionalSelected = new StringBuffer("");
			StringBuffer finalCoverSelected = new StringBuffer("");
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
					
					benefitsSelected.append(billEntryDetailsDTO.getItemName() + ", ");
					//IMSSUPPOR-29828
					if(null != billEntryDetailsDTO.getItemValue()) {
						paTotalClaimedAmnt += billEntryDetailsDTO.getItemValue();
						billEntryDetailsDTO.setApprovedAmountForAssessmentSheet(billEntryDetailsDTO.getItemValue() - (null != billEntryDetailsDTO.getTotalDisallowances() ? billEntryDetailsDTO.getTotalDisallowances() : 0d));
					}
					if(null != billEntryDetailsDTO.getTotalDisallowances())
						paTotalDisAllowance += billEntryDetailsDTO.getTotalDisallowances();
				/*	if(null != billEntryDetailsDTO.getApprovedAmountForAssessmentSheet())
						paTotalApprovedAmnt += billEntryDetailsDTO.getApprovedAmountForAssessmentSheet();		*/	
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
					
					addOnSelected.append(billEntryDetailsDTO.getItemName() + ", ");
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
					optionalSelected.append(billEntryDetailsDTO.getItemName() + ", ");
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
			
			if((null != tableBenefitsListenerTableObj && !tableBenefitsListenerTableObj.getValues().isEmpty() &&
					(null != addOnCoversListenerTableObj && addOnCoversListenerTableObj.getValues().isEmpty() &&
					null != optionalCoversListenerTableObj && optionalCoversListenerTableObj.getValues().isEmpty())) ||
					(null != tableBenefitsListenerTableObj && !tableBenefitsListenerTableObj.getValues().isEmpty() &&
					null != addOnCoversListenerTableObj && !addOnCoversListenerTableObj.getValues().isEmpty() &&
					null != optionalCoversListenerTableObj && !optionalCoversListenerTableObj.getValues().isEmpty())){
				
				finalCoverSelected = benefitsSelected;
				
			}
			
			if((null != addOnCoversListenerTableObj && !addOnCoversListenerTableObj.getValues().isEmpty()) &&
					((null != tableBenefitsListenerTableObj && tableBenefitsListenerTableObj.getValues().isEmpty()) &&
							(null != optionalCoversListenerTableObj && optionalCoversListenerTableObj.getValues().isEmpty())))
			{
				finalCoverSelected = addOnSelected;
			}
			
			if((null != addOnCoversListenerTableObj && !addOnCoversListenerTableObj.getValues().isEmpty()) &&
					((null != tableBenefitsListenerTableObj && tableBenefitsListenerTableObj.getValues().isEmpty()) &&
							(null != optionalCoversListenerTableObj && optionalCoversListenerTableObj.getValues().isEmpty())))
			{
				finalCoverSelected = addOnSelected;
			}
			
			if((null != addOnCoversListenerTableObj && !addOnCoversListenerTableObj.getValues().isEmpty()) &&
					((null != tableBenefitsListenerTableObj && tableBenefitsListenerTableObj.getValues().isEmpty()) &&
							(null != optionalCoversListenerTableObj && !optionalCoversListenerTableObj.getValues().isEmpty())))
			{
				finalCoverSelected.append(SHAConstants.ADD_ON_COVERS_AND_OPTIONAL_COVERS_PAYMENT);
			}
			
			
			bean.setBillEntryDetailsDTO(billEntryDetailsDTO);
			bean.setPaTotalClaimedAmnt(paTotalClaimedAmnt);
			bean.setPaTotalDisAllowance(paTotalDisAllowance);
			bean.setPaTotalApprovedAmnt(paTotalApprovedAmnt);
			bean.setPaCoversSelected(finalCoverSelected.toString());
			
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

		public void addToCoverList(AddOnCoversTableDTO setAddonDtoforCoverId,
				ComboBox cmb) {
			addOnCoversListenerTableObj.addValue(setAddonDtoforCoverId,cmb);
			
		}
		
		
		public void validateOptionalCovers(PreauthDTO dto,Long coverId, TextField eligibleFld)
		{			
//			optionalCoversListenerTableObj.populateCoversTableData(coverId,eligibleFld,dto.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling());
		}
		
		public void buildNomineeLayout() {


			if(nomineeDetailsTable != null) {
				wholeVLayout.removeComponent(nomineeDetailsTable);
			}
			if(chkNomineeDeceased != null){
				wholeVLayout.removeComponent(chkNomineeDeceased);
			}

			if(legalHeirLayout != null) {
				wholeVLayout.removeComponent(legalHeirLayout);
			}

			if(paBillingProcessButtonObj != null) {
				wholeVLayout.removeComponent(paBillingProcessButtonObj);
			}

			nomineeDetailsTable = nomineeDetailsTableInstance.get();

			nomineeDetailsTable.init("", false, false);
			unbindField(chkNomineeDeceased);
			chkNomineeDeceased = null;
			//IMSSUPPOR-30886 - Changed as suggested by Laxmi
			if(bean.getNewIntimationDTO().getNomineeList() != null &&
					!bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				nomineeDetailsTable.setViewColumnDetails();
				nomineeDetailsTable.generateSelectColumn();
				
				/*nomineeDetailsTable.setScreenName(SHAConstants.PA_HEALTH_BILLING_SCREEN);
				nomineeDetailsTable.setPolicySource(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
				nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);*/
				
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
			//IMSSUPPOR-30886 - Commented as suggested by Laxmi
			//legalHeirDetails.setPresenterString(SHAConstants.PA_HEALTH_BILLING_SCREEN);
			legalHeirLayout.addComponent(legalHeirDetails);
			wholeVLayout.addComponent(legalHeirLayout);
			
			if(isNomineeDeceased()){
				enableLegalHeir = Boolean.TRUE;
				nomineeDetailsTable.setEnabled(false);
			}

			if(enableLegalHeir) {

				legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
				//IMSSUPPOR-30886 - Commented as suggested by Laxmi
				//legalHeirDetails.setIFSCView(viewSearchCriteriaWindow);
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}

			if(paBillingProcessButtonObj != null) {
				wholeVLayout.addComponent(paBillingProcessButtonObj);
			}	

		}
	
	void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty() && !isNomineeDeceased()) {
			nomineeDetailsTable.setNomineeBankDetails(dto, nomineeDetailsTable.getBankDetailsTableObj().getNomineeDto());
		} else {
			legalHeirDetails.setBankDetails(dto, viewSearchCriteriaWindow.getLegalHeirDto());
		}
	}
	
	public  void billingInternalRemarksChangeListener(TextArea textArea, final  Listener listener) {
	    @SuppressWarnings("unused")
		ShortcutListener enterShortCut = new ShortcutListener("ShortcutForBillingInternalRemarks", ShortcutAction.KeyCode.F8, null) {
	    	private static final long serialVersionUID = -2267576464623389044L;
	    	@Override
	    	public void handleAction(Object sender, Object target) {
	    		((ShortcutListener) listener).handleAction(sender, target);
	    	}
	    };	  
	    handleShortcut(textArea, getBillingInternalRemarksShortCutListener(textArea));
	}
	
	public  void handleShortcut(final TextArea textArea, final ShortcutListener shortcutListener) {	
		textArea.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {				
				textArea.addShortcutListener(shortcutListener);
			}
		});
		textArea.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void blur(BlurEvent event) {			
				textArea.removeShortcutListener(shortcutListener);		
			}
		});
	}
	
	private ShortcutListener getBillingInternalRemarksShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("ShortcutForBillingInternalRemarks", KeyCodes.KEY_F8,null) {
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
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setValue(textAreaField.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
//						PreauthDTO mainDto = (PreauthDTO) textAreaField.getData();
//						mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(textAreaField.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption("Billing Non-Hospitalization Internal Remarks");
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(textAreaField);
				
				dialog.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
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
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}
	
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
