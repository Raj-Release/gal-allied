package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction;

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
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.preauth.PreauthCoordinatorView;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.NewProcedureTableList;
import com.shaic.claim.preauth.wizard.pages.ViewGmcCorpBufferDetailsPage;
import com.shaic.claim.premedical.listenerTables.DiganosisDetailsListenerForPremedical;
import com.shaic.claim.premedical.listenerTables.ProcedureListenerTableForPremedical;
import com.shaic.claim.premedical.listenerTables.SpecialityTableListener;
import com.shaic.claim.reimbursement.billclassification.BillClassificationEditUI;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.AddOnBenefitsDataExtractionPage;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
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
import com.vaadin.ui.Layout;
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
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalApprovalDataExtractionPageUI extends ViewComponent {

	private static final long serialVersionUID = -5362064388584784817L;

	@Inject
	private PreauthDTO bean;

	private GWizard wizard;

	@EJB
	private MasterService masterService;

	@Inject
	private Instance<PreauthCoordinatorView> reimbursementCoordinatorView;

	@Inject
	private Instance<ProcedureListenerTableForPremedical> procedureTableList;

	@Inject
	private BillClassificationUI billClassificationUI;

	@Inject
	private Instance<BillClassificationEditUI> billClassificationEditUIInstance;

	private BillClassificationEditUI billClassificationEditUIObj;

	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	private AddOnBenefitsDataExtractionPage addOnBenefitsPageObj;

	@Inject
	private Instance<UpdateHospitalDetails> hospitalDetailsInstance;
	
	/*Below line commented and add new table for Req - R1165
<<<<<<< HEAD
>>>>>>> b3ce4003b5ff5a2a5c412796e1e02c054b5d6b0a
=======
>>>>>>> 76f50a3db34eb98c64dab32cb004c83ee650caeb
	@Inject
	private Instance<SpecialityTable> specialityTableList;*/
			
	@Inject
	private Instance<SpecialityTableListener> specialityTableList;

	@Inject
	private Instance<DiganosisDetailsListenerForPremedical> diagnosisDetailsTable;

	@Inject
	private Instance<NewProcedureTableList> newProcedureTableList;

	@Inject
	private Instance<OtherClaimDetails> otherClaimDetailsInstance;

	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;

	private SectionDetailsListenerTable sectionDetailsListenerTableObj;

	private ProcedureListenerTableForPremedical procedurdTableObj;

	private UpdateHospitalDetails hospitalDetailsObj;

	private NewProcedureTableList newProcedurdTableObj;

	private OtherClaimDetails otherClaimDetailsObj;

	private PreauthCoordinatorView reimbursementCoordinatorViewInstance;

	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;

	/*
	 * private ComboBox premedicalSuggestion;
	 * 
	 * private ComboBox premedicalCategory;
	 * 
	 * private TextArea premedicalRemarks;
	 * 
	 * private TextField referenceNoTxt;
	 */

	private TextField reasonForAdmissionTxt;

	private DateField admissionDate;

	private DateField dischargeDate;

	private TextField noOfDaysTxt;

	private ComboBox cmbNatureOfTreatment;

	private PopupDateField firstConsultantDate;

	private CheckBox corpBufferChk;

	private TextField txtCorpBufferAllocatedAmt;

	private Label automaticRestorationTxt;

	private Label acutomaticRestorationCount;

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

	// private ComboBox cmbTerminateCover;

	private TextField systemOfMedicineTxt;

	private ComboBox cmbHospitalisationDueTo;

	private DateField injuryDate;

	private OptionGroup medicalLegalCase;

	private OptionGroup reportedToPolice;

	private OptionGroup coveredPreviousClaim;

	// private OptionGroup isHospitalized;

	private TextField preHospitalisationPeriod;

	private TextField postHospitalisationPeriod;

	private OptionGroup domicillaryHospitalisation;

	private ComboBox cmbCauseOfInjury;

	private DateField diseaseDetectedDate;

	private DateField deliveryDate;

	// / ------ Newly added fields --------////

	private ComboBox cmbSection;

	private TextField hospitalizationNoOfDays;

	private ComboBox cmbTypeOfDelivery;

	private DateField treatmentStartDate;

	private DateField treatmentEndDate;

	// -----------------------------------////

	private FormLayout firstFLayout;

	private VerticalLayout otherClaimsFLayout;

	private FormLayout secondFLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private Map<String, Object> referenceData;

	VerticalLayout wholeVLayout;

	private OptionGroup changeInDiagnosis;

	VerticalLayout tableVLayout;

	/*Below line commented and add new table for Req - R1165
	private SpecialityTable specialityTableObj;*/
	
	private SpecialityTableListener specialityTableObj;

	private DiganosisDetailsListenerForPremedical diagnosisDetailsTableObj;

	// private Boolean processId;

	// private Map<String, Property.ValueChangeListener> listenerMap = new
	// HashMap<String, Property.ValueChangeListener>();

	private HorizontalLayout dynamicElementsHLayout;

	private FormLayout treatmentFLayout;

	private FormLayout patientStatusFLayout;

	/*
	 * private Integer changeIndiaganosisRows = 0;
	 * 
	 * private Boolean changeInDiagSelected = false;
	 */

	private Date admissionDateValue;

	private Boolean isValid = false;

	/*
	 * private BeanItemContainer<State> container = new
	 * BeanItemContainer<State>( State.class);
	 */

	// Added for reason for date change.
	private TextField reasonForChangeInDOA;

	private TextField firNumberTxt;

	private OptionGroup policeReportedAttached;

	private HorizontalLayout treatmentRemarksTableLayout;

	private Button corpBufferDetails;

	@Inject
	private ViewGmcCorpBufferDetailsPage gmcBufferDetails;

	private HorizontalLayout corpBufferHLayout;

	private Boolean isCorpBufferChecked = false;

	private Button btnRTAView;

	private FormLayout legaHeirLayout;
	@EJB
	private PreauthService preauthService;

	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;

	@EJB
	private PolicyService policyService;

	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;
	private Long assistedTreatment = 0l;

	/*
	 * @Inject private ReconsiderRODRequestListenerTable
	 * reconsiderRequestDetails;
	 * 
	 * private ComboBox cmbReasonForReconsideration;
	 * 
	 * private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 * 
	 * private ComboBox cmbReconsiderationRequest;
	 * 
	 * private BeanItemContainer<SelectValue> reconsiderationRequest;
	 * 
	 * private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	 * 
	 * @Inject private ViewDetails viewDetails;
	 */

	private Button referToBillEntryBtn;

	private TextArea reasonRefBillEntryTxta;

	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;
	
	private VerticalLayout legalHeirLayout;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
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
		/*
		 * premedicalSuggestion = new ComboBox("Pre-Medical Suggestion");
		 * premedicalCategory = new ComboBox("Category"); premedicalRemarks =
		 * new TextArea("Rejection Remarks (Pre-medical)");
		 */
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
	}

	public Boolean alertMessageForPED(final String message) {
//		Label successLabel = new Label("<b style = 'color: red;'>"
//				+ message /*SHAConstants.PED_RAISE_MESSAGE*/ + "</b>", ContentMode.HTML);
		final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		
		Label successLabel = new Label(message );
		
		VerticalLayout layout = new VerticalLayout(successLabel);
		
		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		
		//layout.addComponent(homeButton);
		//layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		

		/*final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
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
				/*
				 * if(bean.getClaimCount() >1){
				 * alertMessageForClaimCount(bean.getClaimCount()); }
				 */
				
				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
					 alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					 bean.setMultiplePEDAvailableNotDeleted(false);
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}

	public Boolean alertMessageForAutoRestroation() {
		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.AUTO_RESTORATION_MESSAGE + "</b>",
				ContentMode.HTML);
		// final Boolean isClicked = false;
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.AUTO_RESTORATION_MESSAGE, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
				
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsAutoRestorationDone(true);
				if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});
		return true;
	}
	
	public void get64VbChequeStatusAlert() {
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
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/

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
//				 bean.setIsPopupMessageOpened(true);

//					bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
						
				}else{
					 if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						popupMessageFor30DaysWaitingPeriod();
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			}
		});
		
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
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
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
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
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
	 
	 public Boolean alertForHospitalDiscount() {
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.HOSPITAL_DISCOUNT_ALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

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
//					bean.setIsPedWatchList(false);
					if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
						showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
					}
					else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
						showDuplicateInsured(bean.getDuplicateInsuredList());
					}
					else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
							(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
					
				}
			});
			return true;
			
			
		}
	 
	 public Boolean alertMessageForPostHosp() {
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
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

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
					} 
					else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
						showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
					}
					else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
						showDuplicateInsured(bean.getDuplicateInsuredList());
					}
					else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
							(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
				}
			});
			return true;
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
					if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} 
					else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} 
					else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
						showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
					}
					else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
						showDuplicateInsured(bean.getDuplicateInsuredList());
					}
					else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
							(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
					
				}
			});
			
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
			layout.addComponent(okButton);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	 
	 public void showDeletedInsuredAlert() {

			final MessageBox showInfoMessageBox = showInfoMessageBox("<b style = 'color: red;'>  Selected risk is deleted from policy :</b>"+bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
			Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					// bean.setIsPopupMessageOpened(true);
//						bean.setIsPopupMessageOpened(true);
					showInfoMessageBox.close();
					DBCalculationService dbService = new DBCalculationService();
					String memberType = dbService.getCMDMemberType(bean.getPolicyKey());
					if(null != memberType && !memberType.isEmpty()){
						showCMDAlert(memberType);
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} 
					else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} 
					else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
					} 
					else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
						showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
					}
					else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
						showDuplicateInsured(bean.getDuplicateInsuredList());
					}
					else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
							(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
					}
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
				}
			});
			
			
		}

	public Component getContent() {
		
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
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
				
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		if(bean.getPopupPrevClmInvst()!=null && bean.getPopupPrevClmInvst().size() == 2){
			prevClmInvstAlert(this.bean.getPopupPrevClmInvst());
		}
		
		else if(bean.getPopupSIRestrication()!=null && bean.getPopupSIRestrication().size() == 2){
			siRestricationAlert(this.bean.getPopupSIRestrication());
		}else if(bean.getNewIntimationDTO().getIsDeletedRisk()){
			showDeletedInsuredAlert();
		}
		else if(null != memberType && !memberType.isEmpty()){
			showCMDAlert(memberType);
		}
		else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		} 
		else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
			alertMessageForPostHosp();
		} 
		else if(bean.getIsHospitalDiscountApplicable()){
			alertForHospitalDiscount();
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
		//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
		else if(!bean.getIsGeoSame()) {
			 getGeoBasedOnCPU();
		 }
		else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
			if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
				multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
			}		
		}		
		else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
			getInvsAlert();
		}
		else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}
		else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
			paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
		}	
		else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
			showFVRPendingMessage();
		}
		
		else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
			showICRMessage();
		}else if(bean.getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		
		else if(bean.getIsZUAQueryAvailable()){
			zuaQueryAlertPopupMessage();
		}
		else if(this.bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
			alertMessageForClaimCount(this.bean.getClaimCount());
		}else if(!bean.getSuspiciousPopupMap().isEmpty()){
			suspiousPopupMessage();
		}
		else if(!bean.getNonPreferredPopupMap().isEmpty()){
			nonPreferredPopupMessage();
		}
		else if(bean.getIsPEDInitiated()) {
//			alertMessageForPED();
			if(bean.isInsuredDeleted()){
				alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			}else{
				alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
			}
		}else if(bean.getIsAutoRestorationDone()) {
			alertMessageForAutoRestroation();
		}else if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
			if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
				StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
			} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
				warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
			}
			
		}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
				bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
			 popupMessageFor30DaysWaitingPeriod();
		}else if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
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
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		reasonForAdmissionTxt = (TextField) binder.buildAndBind(
				"Reason For Admission", "reasonForAdmission", TextField.class);
		reasonForAdmissionTxt.setRequired(true);
		dischargeDate = (DateField) binder.buildAndBind(
				"Date of Discharge", "dischargeDate", DateField.class);
		
		admissionDate = (DateField) binder.buildAndBind(
				"Date of Admission", "admissionDate", DateField.class);
		
		systemOfMedicineTxt = (TextField)binder.buildAndBind(
				"System of Medicine", "systemOfMedicine", TextField.class);
		systemOfMedicineTxt.setMaxLength(50);
		CSValidator systemOfMedicineValidator = new CSValidator();
		systemOfMedicineValidator.extend(systemOfMedicineTxt);
		systemOfMedicineValidator.setRegExp("^[a-zA-Z 0-9]*$");
		systemOfMedicineValidator.setPreventInvalidTyping(true);
		
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
			reasonForChangeInDOA.setVisible(true);;
			reasonForChangeInDOA.setRequired(true);
		}
		
		noOfDaysTxt = (TextField) binder.buildAndBind("No of Days", "noOfDays",
				TextField.class);
		noOfDaysTxt.setRequired(true);
		noOfDaysTxt.setMaxLength(4);
		cmbNatureOfTreatment = (ComboBox) binder.buildAndBind(
				"Nature of Treatment", "natureOfTreatment", ComboBox.class);
		cmbNatureOfTreatment.setRequired(true);
		firstConsultantDate = (PopupDateField) binder.buildAndBind(
				"1st Consultation Date", "firstConsultantDate",
				PopupDateField.class);
		corpBufferChk = (CheckBox) binder.buildAndBind("", "corpBuffer",
				CheckBox.class);
		
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
		ventilatorSupportOption.setEnabled(false);
		
		cmbSpecifyIllness = (ComboBox) binder.buildAndBind("Specify",
				"specifyIllness", ComboBox.class);
		if(null != bean.getPreauthDataExtractionDetails().getCriticalIllness() && bean.getPreauthDataExtractionDetails().getCriticalIllness()){
			cmbSpecifyIllness.setReadOnly(false);
		}
		else
		{
			cmbSpecifyIllness.setReadOnly(true);
		}
		cmbTreatmentType = (ComboBox) binder.buildAndBind("Treatment Type",
				"treatmentType", ComboBox.class);
	
		
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
		/*automaticRestorationTxt = (TextField) binder.buildAndBind(
				"Automatic Restoration", "autoRestoration", TextField.class);
		automaticRestorationTxt.setEnabled(false);
		
		acutomaticRestorationCount = (TextField) binder.buildAndBind("No Of Times","restorationCount",TextField.class);
		acutomaticRestorationCount.setEnabled(false);*/
		
		automaticRestorationTxt = new Label(bean.getPreauthDataExtractionDetails().getAutoRestoration());
		automaticRestorationTxt.setEnabled(false);
		
		acutomaticRestorationCount = new Label("No Of Times  " + bean.getPreauthDataExtractionDetails().getRestorationCount());
		acutomaticRestorationCount.setEnabled(false);
		
		cmbIllness = (ComboBox) binder.buildAndBind("Illness", "illness",
				ComboBox.class);
		if((SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE).equals(automaticRestorationTxt.getValue()) || (SHAConstants.AUTO_RESTORATION_NOTDONE).equals(automaticRestorationTxt.getValue()))
		{
			cmbIllness.setEnabled(false);
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			cmbIllness.setVisible(false);
		}
		
		changeInDiagnosis = new OptionGroup("Change In Diagnosis");
		changeInDiagnosis.addItems(getReadioButtonOptions());
		changeInDiagnosis.setItemCaption(true, "Yes");
		changeInDiagnosis.setItemCaption(false, "No");
		changeInDiagnosis.select(false);
		changeInDiagnosis.setStyleName("horizontal");
		
		
		cmbSection = (ComboBox) binder.buildAndBind(
				"Section", "section", ComboBox.class);
		
		if(this.bean.getNewIntimationDTO() != null && !ReferenceTable.getSectionKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
			cmbSection.setEnabled(false);
		}
		
		if(this.bean.getNewIntimationDTO() != null && 
				ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				{
				if(this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					cmbSection.setEnabled(false);
		}
				else
				{
					if(bean.getRodNumber() != null ) {
						String[] split = bean.getRodNumber().split("/");
						String string = split[split.length - 1];
						//IMSSUPPOR-26996
						if(string != null && !Integer.valueOf(string).equals(1) && cmbSection != null && cmbSection.getValue() != null) {
							cmbSection.setEnabled(false);
						} 
						else
						{
							cmbSection.setEnabled(true);
						}
					}
				}
	}
		
		if(!this.bean.getIsCashlessType()) {
			changeInDiagnosis.setEnabled(false);
		}
		
		FormLayout illnessFLayout = new FormLayout(criticalIllnessChk);
		illnessFLayout.setCaption("Critical Illness");
		illnessFLayout.setMargin(false);
		
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
				ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			illnessFLayout.setCaptionAsHtml(Boolean.TRUE);		
			illnessFLayout.setCaption("<string style='color:black;'>Critical Illness<span style='color:red;'> *</span></string>");
			illnessFLayout.setMargin(false);
		}
		
		txtCorpBufferAllocatedAmt = new TextField("Corporate Buffer Limit");
		CSValidator validator = new CSValidator();
		validator.extend(txtCorpBufferAllocatedAmt);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		FormLayout bufferFLayout = new FormLayout(corpBufferChk);
		bufferFLayout.setCaption("Corp Buffer");
		bufferFLayout.setMargin(false);
		
		corpBufferDetails = new Button("View");
		corpBufferDetails.setStyleName("link");
		corpBufferDetails.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
			gmcBufferDetails.bindFieldGroup(bean, isCorpBufferChecked);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Corporate Buffer Details");
			popup.setWidth("30%");
			popup.setHeight("30%");
			popup.setContent(gmcBufferDetails);
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
			UI.getCurrent().addWindow(popup);}
		});
		
		txtCorpBufferAllocatedAmt.addBlurListener(getgmcCorpBufferLimitListener());
		corpBufferHLayout = new HorizontalLayout(txtCorpBufferAllocatedAmt,corpBufferDetails);
		bufferFLayout.addComponent(corpBufferHLayout);
		corpBufferHLayout.setVisible(false);
		
		//Added for corp buffer checkbox validation.
		editCorpBufferChk();
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(this.bean.getClaimDTO() != null && this.bean.getClaimDTO().getGmcCorpBufferLmt() != null){
				txtCorpBufferAllocatedAmt.setValue(this.bean.getClaimDTO().getGmcCorpBufferLmt().toString());
			}
		}
		
		Boolean shouldDisableDomicillary = StarCommonUtils.shouldDisableDomicillary(bean);
		if(!shouldDisableDomicillary) {
			bean.getPreauthDataExtractionDetails().setDomicillaryHospitalisation(false);
		}
		
		domicillaryHospitalisation = (OptionGroup) binder.buildAndBind(
				"Claim for Domicillary\nHospitalisation", "domicillaryHospitalisation", OptionGroup.class);
		
		domicillaryHospitalisation.addItems(getReadioButtonOptions());
		domicillaryHospitalisation.setItemCaption(true, "Yes");
		domicillaryHospitalisation.setItemCaption(false, "No");
		domicillaryHospitalisation.setStyleName("horizontal");
		
		domicillaryHospitalisation.setEnabled(shouldDisableDomicillary);
		
		
		
		HorizontalLayout autoRestorationLayout = new HorizontalLayout();
		autoRestorationLayout.addComponents(automaticRestorationTxt,acutomaticRestorationCount);
		autoRestorationLayout.setCaption("Automatic Restoration");
		autoRestorationLayout.setSpacing(true);
		autoRestorationLayout.setSpacing(true);

		
		firstFLayout = new FormLayout(admissionDate, reasonForChangeInDOA,
				cmbRoomCategory,ventilatorSupportOption, illnessFLayout, cmbSpecifyIllness, dischargeDate, systemOfMedicineTxt, domicillaryHospitalisation, cmbHospitalisationDueTo);
		firstFLayout.setSpacing(true);
		firstFLayout.setMargin(true);
		secondFLayout = new FormLayout(reasonForAdmissionTxt, noOfDaysTxt,
				cmbNatureOfTreatment, firstConsultantDate, /*bufferFLayout,*/  //R1167
				autoRestorationLayout, cmbIllness, cmbSection);
		secondFLayout.setSpacing(true);
		
		Boolean isChecked = false;
		if(domicillaryHospitalisation != null && null != domicillaryHospitalisation.getValue() && domicillaryHospitalisation.getValue().toString() == "true") {
			isChecked = true;
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GENERATE_DOMICILLARY_FIELDS, isChecked);	
			
		} 

		DiganosisDetailsListenerForPremedical diagnosisDetailsTableInstance = diagnosisDetailsTable.get();
		diagnosisDetailsTableInstance.init(this.bean, SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION);
		this.diagnosisDetailsTableObj = diagnosisDetailsTableInstance;

		HorizontalLayout formHLayout = new HorizontalLayout(firstFLayout,
				secondFLayout);
//		formHLayout.setWidth("100%");
	//	formHLayout.setMargin(true);
		PreauthCoordinatorView preauthCoordinatorViewInstance = reimbursementCoordinatorView
				.get();
		preauthCoordinatorViewInstance.setWizard(this.wizard , SHAConstants.PRE_AUTH);
		this.reimbursementCoordinatorViewInstance = preauthCoordinatorViewInstance;
		preauthCoordinatorViewInstance.init(this.bean);
		preauthCoordinatorViewInstance.setWizard(this.wizard, SHAConstants.ZONAL_REVIEW);

		tableVLayout = new VerticalLayout();
		tableVLayout.setSpacing(true);
		
		treatmentFLayout = new FormLayout(cmbTreatmentType);
		treatmentFLayout.setMargin(false);
		patientStatusFLayout = new FormLayout(cmbPatientStatus);
		patientStatusFLayout.setMargin(false);
		dynamicElementsHLayout = new HorizontalLayout(treatmentFLayout,
				patientStatusFLayout);
		dynamicElementsHLayout.setWidth("100%");
		dynamicElementsHLayout.setMargin(true);
		
		coveredPreviousClaim = (OptionGroup) binder.buildAndBind(
				"Previously Covered by any other Mediclaim / Health Insurance", "coveredPreviousClaim", OptionGroup.class);
		
		coveredPreviousClaim.addItems(getReadioButtonOptions());
		coveredPreviousClaim.setItemCaption(true, "Yes");
		coveredPreviousClaim.setItemCaption(false, "No");
		coveredPreviousClaim.select(false);
		coveredPreviousClaim.setStyleName("horizontal");
		
		if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			coveredPreviousClaim.setVisible(false);
		}
		
	
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
		
		HorizontalLayout preandPostHospitalLayout = new HorizontalLayout(new FormLayout(preHospitalisationPeriod), new FormLayout(postHospitalisationPeriod));
		//preandPostHospitalLayout.setSpacing(true);
		
		hospitalDetailsObj = hospitalDetailsInstance.get();
		hospitalDetailsObj.initView(this.bean);
		
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
		
		
		if(admissionDate != null && dischargeDate != null && admissionDate.getValue() != null && dischargeDate.getValue() != null){
		
			Long daysBetweenDate = SHAUtils.getDaysBetweenDate(admissionDate.getValue(), dischargeDate.getValue());
			daysBetweenDate++;
			if(daysBetweenDate != null){
				noOfDaysTxt.setValue(daysBetweenDate.toString());
			}
		}
		
		
		if(null !=cmbHospitalisationDueTo &&  this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo()!= null){
			cmbHospitalisationDueTo.setValue(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getValue());
		}
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(this.bean, SHAConstants.ZONAL_REVIEW);
		if(this.referenceData != null) {
			this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
		}
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO());
		
		//wholeVLayout = new VerticalLayout(formHLayout, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, claimedDetailsTableObj ,  preauthCoordinatorViewInstance);
		FormLayout fLayout = new FormLayout(changeInDiagnosis);
		fLayout.setSpacing(false);
		fLayout.setMargin(false);
		FormLayout claimLayout =  new FormLayout(coveredPreviousClaim);
		claimLayout.setMargin(false);
		claimLayout.setSpacing(true);
		
		billClassificationUI.init(bean);
		
		/*As per satish sir instruction change in diagnosis removed from layout i.e fLayout*/
		wholeVLayout = new VerticalLayout(billClassificationUI,formHLayout, /*fLayout,*/sectionDetailsListenerTableObj, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, claimLayout, otherClaimsFLayout, preandPostHospitalLayout);
		wholeVLayout.setSpacing(false);
				
/*		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			
//			wholeVLayout = new VerticalLayout(formHLayout, fLayout,sectionDetailsListenerTableObj, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, claimLayout, otherClaimsFLayout, preandPostHospitalLayout,preauthCoordinatorViewInstance);
//			wholeVLayout.setSpacing(false);
			
		} else {
			wholeVLayout.addComponent(hospitalDetailsObj);
//			wholeVLayout = new VerticalLayout(formHLayout, fLayout,sectionDetailsListenerTableObj, diagnosisDetailsTableInstance, dynamicElementsHLayout, tableVLayout, claimLayout, otherClaimsFLayout, preandPostHospitalLayout,  hospitalDetailsObj, preauthCoordinatorViewInstance);
//			wholeVLayout.setSpacing(false);
		}*/
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null &&
						this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId() != null
				&& !this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
			wholeVLayout.addComponent(hospitalDetailsObj);			
		}
		
		wholeVLayout.addComponent(preauthCoordinatorViewInstance);
		
		if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) 
							|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			buildNomineeLayout();
		}	
		
		referToBillEntryBtn = new Button("Refer To Bill Entry");
		
		if(null != bean.getIsRejectReconsidered() && !bean.getIsRejectReconsidered()){
			referToBillEntryBtn.setEnabled(Boolean.FALSE);
		}
		
		referToBillEntryBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				buildRefToBillEntryLayout();
				wizard.getFinishButton().setEnabled(true);
			}
		});
		
		wholeVLayout.addComponent(referToBillEntryBtn);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated() &&
				this.bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated()){
			referToBillEntryBtn.setEnabled(Boolean.FALSE);
		}
		
		addListener();
		addAdmissionDateChangeListener();
		addDiagnosisNameChangeListener();
		mandatoryFields.add(admissionDate);
		mandatoryFields.add(cmbRoomCategory);
		mandatoryFields.add(cmbTreatmentType);
		mandatoryFields.add(cmbPatientStatus);
		mandatoryFields.add(dischargeDate);
		

		if(this.bean.getNewIntimationDTO() != null && ReferenceTable.getSectionKeysOfCombiProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
				mandatoryFields.add(cmbSection);
			}
		
		if(this.bean.getPreauthDataExtractionDetails().getCoveredPreviousClaim() != null && this.bean.getPreauthDataExtractionDetails().getCoveredPreviousClaim()) {
			coveredPreviousClaim.setValue(true);
			generatedFieldsBasedOnOtherClaims(true);
		}
		 
		
		admissionDateValue = admissionDate.getValue();
		showOrHideValidation(false);
		return wholeVLayout;
	}



	private void getGeoBasedOnCPU() {

		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
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
				.createInformationBox(SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
				
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
					if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
						multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
					}		
				}
				else if (bean.getPreauthMedicalDecisionDetails()
						.getIsInvsInitiated()) {
					getInvsAlert();
				} else if (null != bean.getNewIntimationDTO().getHospitalDto()
						.getFinalGradeName()) {
					getHospitalCategory(bean.getNewIntimationDTO()
							.getHospitalDto().getFinalGradeName());
				} else if (bean.getNewIntimationDTO().getIsPaayasPolicy()
						&& bean.getNewIntimationDTO()
								.getIsClaimManuallyProcessed()) {
					paayasClaimManualyProcessedAlertMessage(bean
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredName());
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getIsFvrIntiated()) {
					showFVRPendingMessage();
				}

				else if (masterService.doGMCPolicyCheckForICR(bean
						.getPolicyDto().getPolicyNumber())) {
					showICRMessage();
				} else if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				}

				else if (bean.getIsZUAQueryAvailable()) {
					zuaQueryAlertPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}
					else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});
	
	}

	private void alertMessageForClaimCount(Long claimCount) {/*

		String msg = SHAConstants.CLAIM_COUNT_MESSAGE + claimCount;

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

		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		VerticalLayout firstForm = new VerticalLayout(dummyField, mainHor);
		//firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		// firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);

		if (this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <= 2) {
			panel.addStyleName("girdBorder1");
		} else if (this.bean.getClaimCount() > 2) {
			panel.addStyleName("girdBorder2");
		}

		// panel.setHeight("143px");
		panel.setSizeFull();

		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
		// popup.setCaption("Alert");
		// popup.setContent( viewDocumentDetailsPage);
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
				bean.setIsPopupMessageOpened(true);

				//popup.close();

				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				}
				else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}
				else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	*/


		String msg = SHAConstants.CLAIM_COUNT_MESSAGE + claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		Button homeButton=new Button() ;
		
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

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);

				if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				}
				else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}
				else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

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
		// reasonForAdmissionTxt.setValue(this.bean.getReasonForAdmission());
		// Setting this feild readOnly true, since this needs to be editable as
		// per sathish sir.
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

		this.bean.getPolicyDto().setAdmissionDate(
				this.bean.getPreauthDataExtractionDetails().getAdmissionDate());
		// fireViewEvent(PreauthWizardPresenter.SET_DB_DETAILS_TO_REFERENCE_DATA,
		// this.bean);

		cmbTreatmentType.setContainerDataSource(treatementType);
		cmbTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbTreatmentType.setItemCaptionPropertyId("value");

		cmbHospitalisationDueTo.setContainerDataSource(hospitalizationDueTo);
		cmbHospitalisationDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbHospitalisationDueTo.setItemCaptionPropertyId("value");

		if (null != cmbHospitalisationDueTo
				&& this.bean.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo() != null) {
			cmbHospitalisationDueTo.setValue(this.bean
					.getPreauthDataExtractionDetails()
					.getHospitalisationDueTo());
		}

		cmbRoomCategory.setContainerDataSource(roomCategory);
		cmbRoomCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoomCategory.setItemCaptionPropertyId("value");

		/*
		 * if(null !=
		 * this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() &&
		 * this.bean.getPolicyDto().getProduct().getNonAllopathicFlag() != null
		 * &&
		 * this.bean.getPolicyDto().getProduct().getNonAllopathicFlag().toLowerCase
		 * ().equalsIgnoreCase("n")) { List<SelectValue> itemIds2 =
		 * natureOfTreatment.getItemIds(); List<SelectValue> allopathicValues =
		 * new ArrayList<SelectValue>(); for (SelectValue selectValue :
		 * itemIds2) {
		 * if(selectValue.getValue().toString().toLowerCase().contains
		 * ("allopathic")) { allopathicValues.add(selectValue); }
		 *//***
		 * Fix for issue 662. As per above code, allopathic and
		 * non-allopathic , both contains allopathic string. Hence in the final
		 * list , both values will be added. But to exclude non allopathic
		 * string , instead of validating allopatich string, need to check
		 * whether the resultant value contains "non". Hence above code is
		 * commented and below is added.
		 */
		/*
		 * if(!selectValue.getValue().toString().toLowerCase().contains("non"))
		 * { allopathicValues.add(selectValue); } }
		 * natureOfTreatment.removeAllItems();
		 * natureOfTreatment.addAll(allopathicValues); }
		 */
//		Below Code for SCRC REVISED - CR20181302
		if(this.bean.getPolicyDto().getProduct().getKey() != null && 
				(this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED)
						|| this.bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL))) {
			List<SelectValue> itemIds2 = natureOfTreatment.getItemIds();
			List<SelectValue> allopathicValues = new ArrayList<SelectValue>() ; 
			for (SelectValue selectValue : itemIds2) {
				if(!selectValue.getValue().toString().toLowerCase().contains("non")) {
					allopathicValues.add(selectValue);
				}
			}
			natureOfTreatment.removeAllItems();
			natureOfTreatment.addAll(allopathicValues);
		}

		cmbNatureOfTreatment.setContainerDataSource(natureOfTreatment);
		cmbNatureOfTreatment.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbNatureOfTreatment.setItemCaptionPropertyId("value");

		Collection<?> itemIds = cmbNatureOfTreatment.getContainerDataSource()
				.getItemIds();
		if (itemIds != null
				&& !itemIds.isEmpty()
				&& (bean.getPreauthDataExtractionDetails()
						.getNatureOfTreatment() == null)) {
			cmbNatureOfTreatment.setValue(itemIds.toArray()[0]);
			cmbNatureOfTreatment.setNullSelectionAllowed(false);
		}

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
		if(null != bean.getPreauthDataExtractionDetails().getCriticalIllness() && bean.getPreauthDataExtractionDetails().getCriticalIllness()){
			cmbSpecifyIllness.setReadOnly(false);
		}
		else
		{
			cmbSpecifyIllness.setReadOnly(true);
		}

		cmbSection.setContainerDataSource(section);
		cmbSection.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSection.setItemCaptionPropertyId("value");

		if (this.bean.getPreauthDataExtractionDetails().getSpecifyIllness() != null) {
			this.cmbSpecifyIllness.setReadOnly(false);
			this.cmbSpecifyIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getSpecifyIllness());
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
		}

		if (this.bean.getPreauthDataExtractionDetails().getIllness() != null) {
			this.cmbIllness.setValue(this.bean
					.getPreauthDataExtractionDetails().getIllness());
		}

		if (this.bean.getPreauthDataExtractionDetails()
				.getHospitalisationDueTo() != null) {
			this.cmbHospitalisationDueTo.setValue(this.bean
					.getPreauthDataExtractionDetails()
					.getHospitalisationDueTo());
		}

		if (this.bean.getPreauthDataExtractionDetails().getSection() != null) {
			this.cmbSection.setValue(this.bean
					.getPreauthDataExtractionDetails().getSection());
		}

		if (cmbSection != null && cmbSection.getValue() != null) {
			this.bean.getPreauthDataExtractionDetails().setSection(
					(SelectValue) cmbSection.getValue());
		}

		this.reimbursementCoordinatorViewInstance.setUpReference(referenceData);
		this.diagnosisDetailsTableObj.setReferenceData(referenceData);
		this.sectionDetailsListenerTableObj.setReferenceData(referenceData);
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

		if (noOfDaysTxt != null
				&& noOfDaysTxt.getValue() == null
				|| (noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue()
						.length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter No of Days. </br>");
		}
		if (reasonForAdmissionTxt != null
				&& reasonForAdmissionTxt.getValue() == null
				|| (reasonForAdmissionTxt.getValue() != null && reasonForAdmissionTxt
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Reason for Admission. </br>");
		}
		if (cmbNatureOfTreatment != null
				&& cmbNatureOfTreatment.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Nature of Treatment. </br>");
		}

		if (automaticRestorationTxt.getValue() != null
				&& (SHAConstants.AUTO_RESTORATION_DONE)
						.equals(automaticRestorationTxt.getValue())) {
			if (cmbIllness != null && cmbIllness.getValue() == null) {
				hasError = true;
				eMsg.append("Please choose illness. </br>");
			}
		}
		
		if(ventilatorSupportOption != null && ventilatorSupportOption.isEnabled() && (ventilatorSupportOption.getValue() == null))
		{
			System.out.println(String.format("Ventilator Support value in validate: [%s]",this.ventilatorSupportOption.getValue()));
			hasError = true;
			eMsg.append("Please Select Ventilator Support. </br>");
		}
		
		if(null != bean.getNewIntimationDTO().getPolicy() && 
				(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				&& (! ReferenceTable.STAR_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						&& ! ReferenceTable.NOVEL_CORONA_VIRUS_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(null != criticalIllnessChk && (null == criticalIllnessChk.getValue() || !criticalIllnessChk.getValue())){
				hasError = true;
				eMsg.append("Please Choose Critical Illness");	
			}else{
				if (cmbSpecifyIllness != null && cmbSpecifyIllness.getValue() == null) {
					hasError = true;
					eMsg.append("Please Select Illness (Specify)");
				}
			}
		}

		if (null != this.bean.getPreauthDataExtractionDetails()
				.getUploadDocumentDTO().getOtherBenefit()) {
			fireViewEvent(
					MedicalApprovalDataExtractionPagePresenter.SAVE_OTHER_BENEFITS_TABLE_VALUES_ZMR,
					this.bean);
		}

		if (bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null
				&& ReferenceTable.getFHORevisedKeys().containsKey(
						bean.getNewIntimationDTO().getPolicy().getProduct()
								.getKey())) {

			fireViewEvent(
					MedicalApprovalDataExtractionPagePresenter.GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_ZMR,
					bean);
			SelectValue cmbHospitalizationDueTo = (SelectValue) this.cmbHospitalisationDueTo
					.getValue();

			if (null != this.cmbHospitalisationDueTo
					&& null == this.cmbHospitalisationDueTo.getValue()
					&& (assistedTreatment).equals(bean
							.getPreauthDataExtractionDetails()
							.getSectionDetailsDTO().getCover().getId())) {
				hasError = true;
				eMsg.append("Please Select Hospitalization due to</br>");
			}

			else if (null != bean
					&& null != this.cmbHospitalisationDueTo
					&& null != this.cmbHospitalisationDueTo.getValue()
					&& (ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)
							.equals(cmbHospitalizationDueTo.getId())
					&& !(assistedTreatment).equals(bean
							.getPreauthDataExtractionDetails()
							.getSectionDetailsDTO().getCover().getId())) {
				hasError = true;
				eMsg.append("Please Change the cover since Hospitalization due to is Assisted Reproduction Treatment.</br>");
			} else {
				if (null != bean
						&& null != this.cmbHospitalisationDueTo
						&& null != this.cmbHospitalisationDueTo.getValue()
						&& !(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)
								.equals(cmbHospitalizationDueTo.getId())
						&& (assistedTreatment).equals(bean
								.getPreauthDataExtractionDetails()
								.getSectionDetailsDTO().getCover().getId())) {
					hasError = true;
					eMsg.append("Please Change the cover since Hospitalization due to is not Assisted Reproduction Treatment.</br>");
				}
			}
		}
		try {
			if (!this.reimbursementCoordinatorViewInstance.isValid()) {
				hasError = true;
				List<String> errors = this.reimbursementCoordinatorViewInstance
						.getErrors();
				for (String error : errors) {
					eMsg.append(error);
				}
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (this.diagnosisDetailsTableObj != null
				&& this.diagnosisDetailsTableObj.getValues().isEmpty()) {
			hasError = true;
			eMsg.append("Please Add Atleast one Diagnosis Details to Proceed Further. </br>");
		}

		if (this.reasonForChangeInDOA.isVisible()
				&& !(null != this.reasonForChangeInDOA && (null != this.reasonForChangeInDOA
						.getValue() && !("")
						.equalsIgnoreCase(this.reasonForChangeInDOA.getValue())))) {
			hasError = true;
			eMsg.append("Please enter Reason For Change in DOA to Proceed Further. </br>");

		}

		if ((this.procedurdTableObj != null && this.procedurdTableObj
				.getValues().isEmpty())
				&& (this.newProcedurdTableObj != null && this.newProcedurdTableObj
						.getValues().isEmpty())) {
			hasError = true;
			eMsg.append("Please Add Atleast one Procedure List Details to Proceed Further. </br>");
		}
		
		List<DiagnosisDetailsTableDTO> diagnosisList = this.diagnosisDetailsTableObj.getValues();

		if(this.procedurdTableObj != null && !this.procedurdTableObj.getValues().isEmpty()) {
			List<ProcedureDTO> procedureList = this.procedurdTableObj.getValues();
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
							eMsg.append("Same Sublimit is Selected for both Diagnosis and Procedure. </br> Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .  </br>");
							break;
						}
					}
				}
				if (isError) {
					break;
				}
			}
		}

		/*
		 * Boolean diagnosisIsConsiderForPayment = false; Boolean
		 * procedureIsConsiderForPayment = false;
		 * if(this.diagnosisDetailsTableObj != null &&
		 * !this.diagnosisDetailsTableObj.getValues().isEmpty()) {
		 * List<DiagnosisDetailsTableDTO> diagnosisList =
		 * this.diagnosisDetailsTableObj.getValues(); for
		 * (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
		 * if((null != diagnosisDetailsTableDTO.getConsiderForPayment() &&
		 * diagnosisDetailsTableDTO
		 * .getConsiderForPayment().getId().equals(ReferenceTable
		 * .COMMONMASTER_YES))) { diagnosisIsConsiderForPayment =false; break; }
		 * else { diagnosisIsConsiderForPayment = true; }
		 * 
		 * 
		 * } } if(this.procedurdTableObj != null &&
		 * !this.procedurdTableObj.getValues().isEmpty()) { List<ProcedureDTO>
		 * procedureList = this.procedurdTableObj.getValues(); for (ProcedureDTO
		 * procedureDTO : procedureList) { if(null !=
		 * procedureDTO.getConsiderForPayment() &&
		 * procedureDTO.getConsiderForPayment
		 * ().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
		 * procedureIsConsiderForPayment = false; break; } else {
		 * procedureIsConsiderForPayment = true; } } } else {
		 * procedureIsConsiderForPayment =true; }
		 */

		// if (diagnosisIsConsiderForPayment && procedureIsConsiderForPayment){
		// hasError = true;
		// eMsg += SHAConstants.DIAGNOSIS_ERROR_MSG +"</br>";
		// }

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
					eMsg.append(error).append("</br>");
				}
			}
		}

		if (this.procedurdTableObj != null) {
			boolean isValid = this.procedurdTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.procedurdTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
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
					eMsg.append(error).append("</br>");
				}
			}

			if (!isValidProcedure) {
				hasError = true;
				List<String> errors = this.newProcedurdTableObj
						.getProcedureErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}

		if (this.specialityTableObj != null) {
			boolean isValid = this.specialityTableObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.specialityTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(cmbTreatmentType != null && cmbTreatmentType.getValue() != null && cmbTreatmentType.getValue().toString().toLowerCase()
				.equalsIgnoreCase("surgical")){
			if(this.specialityTableObj != null && this.specialityTableObj.getValues().isEmpty()){
				hasError = true;
				eMsg.append("Please add atleast one Speciality to proceed further. </br>");
			}
			
		}
		
		/*if (this.specialityTableObj != null) {
			
			List<SpecialityDTO> specialityList = this.specialityTableObj.getValues();
			for (SpecialityDTO specialityDTO : specialityList) {
					if(specialityDTO.getProcedure() !=null && specialityDTO.getProcedure().getValue().equalsIgnoreCase("others") && specialityDTO.getProcedureValue() ==null){
						eMsg.append("Procedure selected as Other.Enter the Procedure");
					}else {
						hasError =false;
					}
			}
		}*/
		
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && 
				this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null
				&& ! this.bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
		
			if (this.hospitalDetailsObj != null) {
				boolean isValid = this.hospitalDetailsObj.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.hospitalDetailsObj.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
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
					eMsg.append(error).append("</br>");
				}
			} else {
				this.otherClaimDetailsObj.setTableValues();
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
				eMsg.append("No of days should be DOD-DOA+1 </br>");
			}
		}

		// Section details included for Comprehensive product. Remaining
		// products, the Hospitalization will be the default value.........
		if (this.sectionDetailsListenerTableObj != null
				&& !sectionDetailsListenerTableObj.isValid(true)) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj
					.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}

//		if (this.corpBufferChk != null && this.corpBufferChk.getValue() != null
//				&& this.corpBufferChk.getValue()) {
//			if (this.txtCorpBufferAllocatedAmt != null
//					&& this.txtCorpBufferAllocatedAmt.getValue() != null
//					&& this.txtCorpBufferAllocatedAmt.getValue()
//							.equalsIgnoreCase("")) {
//				txtCorpBufferAllocatedAmt.setValidationVisible(true);
//				hasError = true;
//				eMsg.append("Enter Corporate Buffer Limit Amount. </br>");
//			}
//
//		}

		if (bean.getClaimKey() != null) {
			/**
			 * Release Number : R3 Requirement Number:R0725 Modified By : Durga
			 * Rao Modified On : 15th May 2017
			 */
			List<Preauth> preauthByClaimnKey = preauthService
					.getPreauthByClaimnKey(bean.getClaimKey());
			if (preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
				for (Preauth preauthDto : preauthByClaimnKey) {
					if (preauthDto != null) {
						if (preauthDto.getStatus().getKey() != null
								&& ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS
										.equals(preauthDto.getStatus().getKey())) {
							if (!this.bean.getIsViewCashlessDocClicked()) {
								hasError = true;
								eMsg.append("Please verify the cashless document");
							}
						}
					}
				}
			}

		}

		if (this.bean.getNewIntimationDTO() != null
				&& ReferenceTable.getSectionKeysOfCombiProducts().containsKey(
						this.bean.getNewIntimationDTO().getPolicy()
								.getProduct().getKey())) {

			if (null != this.cmbSection
					&& (null == this.cmbSection.getValue() || ("")
							.equalsIgnoreCase(this.cmbSection.getValue()
									.toString())))

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
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
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
//							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}
					}
					bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
						bean.getNewIntimationDTO().setNomineeAddr(null);
						hasError = false;
					}
					else{
						bean.getNewIntimationDTO().setNomineeName(null);
						
						eMsg.append("Please Select Nominee</br>");
						hasError = true;						
					}							
				}
			}
			else{
				bean.getNewIntimationDTO().setNomineeList(null);
				bean.getNewIntimationDTO().setNomineeName(null);
				Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
				if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
						&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
				{
					bean.getNewIntimationDTO().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getNewIntimationDTO().setNomineeAddr(legalHeirMap.get("ADDR").toString());
					hasError = false;
				}
				else{
					bean.getNewIntimationDTO().setNomineeName(null);
					bean.getNewIntimationDTO().setNomineeAddr(null);
					hasError = true;
					eMsg.append("Please Enter Claimant / Legal Heir Details</br>");
				}
					
			}					
		}		
		
		
		/*
		 * if(cmbTreatmentType != null && cmbTreatmentType.getValue() != null &&
		 * cmbTreatmentType.getValue().toString().toLowerCase()
		 * .contains("medical")) {
		 * 
		 * if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.
		 * getNewIntimationDTO().getPolicy().getProduct().getKey()) && (null !=
		 * bean
		 * .getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection
		 * () &&
		 * (ReferenceTable.HOSPITALISATION_SURGICAL_SECTION_CODE.equalsIgnoreCase
		 * (
		 * bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection
		 * ().getCommonValue())))){ hasError = true; eMsg.append(
		 * "Please Change the Treatement Type as Surgical since Section is Hospitalisation Surgical.</br>"
		 * ); } }else if(cmbTreatmentType != null && cmbTreatmentType.getValue()
		 * != null && cmbTreatmentType.getValue().toString().toLowerCase()
		 * .contains("surgical")) {
		 * if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY
		 * .equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
		 * && (null !=
		 * bean.getPreauthDataExtractionDetails().getSectionDetailsDTO
		 * ().getSection() &&
		 * (ReferenceTable.HOSP_NON_SURGICAL_SECTION_CODE.equalsIgnoreCase
		 * (bean.getPreauthDataExtractionDetails
		 * ().getSectionDetailsDTO().getSection().getCommonValue())))){ hasError
		 * = true; eMsg.append(
		 * "Please Change the Treatement Type as Medical since Section is Hospitalisation Non-surgical.</br>"
		 * ); } }
		 */

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
				Map<Integer, Object> detailsMap = (Map) referenceData
						.get("detailsMap");
				if (detailsMap != null) {
					List<UploadDocumentDTO> rodSummaryDetails = bean
							.getUploadDocumentDTO();
					for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
						uploadDocumentDTO
								.setProductBasedRoomRent((Double) detailsMap
										.get(8));
						uploadDocumentDTO
								.setProductBasedICURent((Double) detailsMap
										.get(9));

						Product product = this.bean.getNewIntimationDTO()
								.getPolicy().getProduct();
						if (product != null
								&& ReferenceTable.getSuperSurplusKeys()
										.containsKey(product.getKey())
								&& this.bean.getNewIntimationDTO().getPolicy()
										.getPolicyPlan() != null
								&& this.bean.getNewIntimationDTO().getPolicy()
										.getPolicyPlan().equalsIgnoreCase("G")) {
							uploadDocumentDTO.setProductBasedRoomRent(0d);
							uploadDocumentDTO.setProductBasedICURent(0d);
						}

					}
					bean.setUploadDocumentDTO(rodSummaryDetails);
				}

				if (!bean.getIsComparisonDone()
						&& (bean.getHospitalizaionFlag()
								|| bean.getPartialHospitalizaionFlag() || bean
									.getIsHospitalizationRepeat())) {
					fireViewEvent(
							MedicalApprovalDataExtractionPagePresenter.COMPARE_WITH_PREVIOUS_ROD,
							bean);
				}

				bean.setIsNonAllopathic(bean.getPreauthDataExtractionDetails()
						.getNatureOfTreatment() != null ? (bean
						.getPreauthDataExtractionDetails()
						.getNatureOfTreatment().getValue().toLowerCase()
						.contains("non") ? true : false) : false);

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
				
				/**			  
				 * Part of CR R1136
				 */
				if(diagnosisList != null && !diagnosisList.isEmpty()){
					StringBuffer selectedSublimitNames = new StringBuffer("");
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null){
							selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
						}
					}
					if(!selectedSublimitNames.toString().isEmpty()){
						Collection<Window> windows = UI.getCurrent().getWindows();
						for (Window window : windows) {
							if(window.getId() != null && window.getId().equalsIgnoreCase("sublimitAlert")){
								window.close();
								break;
							}
						}
						StarCommonUtils.showPopup(getUI(),selectedSublimitNames.insert(0, "Sublimit selected is ").toString(),null);
					}	
				}
				//============= END of CR R1136 - ICD Sublimit Map ==========================================================
				

				Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate.getValue());
				
				MastersValue policyType = bean.getNewIntimationDTO().getPolicy().getPolicyType();
				
				if(this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null && this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toString().toLowerCase().contains("non")) {
//					this.bean.setIsDishonoured(true);
				}

				if ((diffDays != 0 && diffDays > 90)
						|| (policyType != null && policyType.getKey().equals(
								ReferenceTable.RENEWAL_POLICY))
						|| !bean.getAdmissionDatePopup()) {
					this.bean.setAlertMessageOpened(true);
				}

				if (SHAUtils.getDialysisDiagnosisDTO(bean
						.getPreauthDataExtractionDetails()
						.getDiagnosisTableList()) != null
						|| SHAUtils.getDialysisProcedureDTO(bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()) != null) {
					this.bean.setIsDialysis(true);
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

				if (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct()
						.getKey())) {
					if (corpBufferChk != null
							&& corpBufferChk.getValue() != null
							&& corpBufferChk.getValue()
							&& txtCorpBufferAllocatedAmt != null) {
						if (txtCorpBufferAllocatedAmt.getValue() != null
								&& !txtCorpBufferAllocatedAmt.getValue()
										.isEmpty()) {
							bean.getPreauthDataExtractionDetails()
									.setCorpBufferAllocatedClaim(
											SHAUtils.getIntegerFromStringWithComma(txtCorpBufferAllocatedAmt
													.getValue()));
						}

					}
				}

				if (this.bean.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo() != null) {
					this.cmbHospitalisationDueTo.setValue(this.bean
							.getPreauthDataExtractionDetails()
							.getHospitalisationDueTo());
				}

				if (null != cmbHospitalisationDueTo
						&& null != cmbHospitalisationDueTo.getValue()) {
					SelectValue hospitalisationDueTo = (SelectValue) cmbHospitalisationDueTo
							.getValue();
					this.bean.getPreauthDataExtractionDetails()
							.setHospitalisationDueTo(hospitalisationDueTo);
				}

				if ((bean.getNewIntimationDTO().getPolicy().getProduct()
						.getKey() != null
						&& ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
								|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) )*/
									(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
										&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
												|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
												|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
						|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
								&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){

					if (null != cmbCauseOfInjury
							&& cmbCauseOfInjury.getValue() != null && null != cmbHospitalisationDueTo
									&& null != cmbHospitalisationDueTo.getValue()) {
						SelectValue causeOfInjuryId = (SelectValue) cmbCauseOfInjury
								.getValue();
						
						SelectValue hospDueTo = (SelectValue) cmbHospitalisationDueTo
								.getValue();
						
						if ((ReferenceTable.ROAD_TRAFFIC_ACCIDENT)
								.equals(causeOfInjuryId.getId()) && (ReferenceTable.INJURY_MASTER_ID)
								.equals(hospDueTo.getId()))

						{
							fireViewEvent(
									MedicalApprovalDataExtractionPagePresenter.GET_RTA_BALANCE_SI,
									bean);
						}
					}
				}

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);

			return true;

		}
	}
	
	public Boolean validateProcedure(){
		Boolean hasError = false;
		if (this.specialityTableObj != null) {
			List<SpecialityDTO> specialityList = this.specialityTableObj.getValues();
			for (SpecialityDTO specialityDTO : specialityList) {
					if(specialityDTO.getProcedure() !=null && specialityDTO.getProcedure().getValue() !=null){
						if(specialityDTO.getProcedure().getValue().equalsIgnoreCase("others")  && specialityDTO.getProcedureValue() == null) {
						hasError =true;
						break;
					} else{
						hasError =false;
					}
					}
			}
		}
		
		return hasError;
	}
	
	public void procedurePopUp(){
 		/*Label successLabel = new Label("<b style = 'color: red;'>Procedure selected as Other. Enter the Procedure </b>", ContentMode.HTML);
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox.createAlertBox("Procedure selected as Other. Enter the Procedure </b>", buttonsNamewithType);
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
		
		 * HorizontalLayout hLayout = new HorizontalLayout(layout);
		 * hLayout.setMargin(true); hLayout.setStyleName("borderLayout");
		 

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
				.createAlertBox("Close Proximity Claim", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				isValid = true;
				bean.setAlertMessageOpened(true);
				//dialog.close();
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

	}

	private List<ProcedureDTO> getProcedureVariationList(
			List<ProcedureDTO> procedureDTOList, Long isNewProcedure) {
		if (!procedureDTOList.isEmpty()) {
			SelectValue procedureName = null;
			SelectValue procedureCode = null;
			for (ProcedureDTO procedureDTO : procedureDTOList) {
				if (isNewProcedure == 0) {
					procedureDTO.setProcedureNameValue(procedureDTO
							.getProcedureName() != null ? procedureDTO
							.getProcedureName().getValue() : null);
					procedureDTO.setProcedureCodeValue(procedureDTO
							.getProcedureCode() != null ? procedureDTO
							.getProcedureCode().getValue() : null);
				} else {
					procedureName = new SelectValue();
					procedureName
							.setValue(procedureDTO.getProcedureNameValue());
					procedureDTO.setProcedureName(procedureName);
					procedureCode = new SelectValue();
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
			this.diagnosisDetailsTableObj.init(this.bean,
					SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION);

		}

	}

	public void editSpecifyVisible(Boolean checkValue) {
		if (checkValue) {
			cmbSpecifyIllness.setReadOnly(false);
			cmbSpecifyIllness.setRequired(true);
			mandatoryFields.add(cmbSpecifyIllness);
		} else {
			// cmbSpecifyIllness.setValue("");
			cmbSpecifyIllness.setValue(null);
			cmbSpecifyIllness.setNullSelectionAllowed(true);
			cmbSpecifyIllness.setRequired(false);
			mandatoryFields.remove(cmbSpecifyIllness);
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
								MedicalApprovalDataExtractionPagePresenter.CHECK_CRITICAL_ILLNESS,
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

				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if (value != null
						&& cmbTreatmentType.getValue().toString().toLowerCase()
								.contains("medical")) {

					/*
					 * if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean
					 * .getNewIntimationDTO().getPolicy().getProduct().getKey())
					 * && (null !=
					 * bean.getPreauthDataExtractionDetails().getSectionDetailsDTO
					 * ().getSection() &&
					 * (ReferenceTable.HOSPITALISATION_SURGICAL_SECTION_CODE
					 * .equalsIgnoreCase
					 * (bean.getPreauthDataExtractionDetails().getSectionDetailsDTO
					 * ().getSection().getCommonValue())))){
					 * 
					 * StarCommonUtils.alertMessage(getUI(),
					 * "Please Change the Treatement Type as Surgical since Section is Hospitalisation Surgical."
					 * ); cmbTreatmentType.setValue(null); }
					 */

					if (!bean.getPreauthMedicalProcessingDetails()
							.getProcedureExclusionCheckTableList().isEmpty()) {
						alertMessageForChangeOfTreatmentType();
					} else {
						fireViewEvent(
								MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
								null);
					}
				} else {
					fireViewEvent(
							MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
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
					//bean.getPreauthDataExtractionDetails().setVentilatorSupport(true);
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

		cmbSection.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				if (cmbSection != null && cmbSection.getValue() != null) {
					SelectValue sectionValue = (SelectValue) cmbSection
							.getValue();
					bean.getPreauthDataExtractionDetails().setSection(
							sectionValue);

					if (null != bean.getNewIntimationDTO().getPolicy()
							.getProduct().getKey()
							&& (ReferenceTable.STAR_CARDIAC_CARE.equals(bean
									.getNewIntimationDTO().getPolicy()
									.getProduct().getKey()))) {

						if (null != bean.getPreauthDataExtractionDetails()
								.getSection().getValue()
								&& (SHAConstants.SECTION_2
										.equalsIgnoreCase(bean
												.getPreauthDataExtractionDetails()
												.getSection().getValue()))) {

							Date policyFromDate = bean.getNewIntimationDTO()
									.getPolicy().getPolicyFromDate();
							Long diffDays = 0l;
							if (admissionDate.getValue() != null) {
								diffDays = SHAUtils.getDiffDays(policyFromDate,
										admissionDate.getValue());
							}

							if (null != bean.getNewIntimationDTO().getPolicy()
									.getPolicyType().getKey()
									&& (ReferenceTable.FRESH_POLICY.equals(bean
											.getNewIntimationDTO().getPolicy()
											.getPolicyType().getKey()))
									&& diffDays < 90) {

								cardiaCareSectionAlert();
							}

							else if (null != bean.getNewIntimationDTO()
									.getPolicy().getPolicyPlan()) {
								alertMessageForPlocyPlan();

							}
						}
					}
				}

				fireViewEvent(
						MedicalApprovalDataExtractionPagePresenter.SUBLIMIT_CHANGED_BY_SECTION,
						bean);

				List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
   				
   				diagnosisList.addAll(diagnosisDetailsTableObj.getValues());
   				diagnosisDetailsTableObj.removeAllItems();
   				diagnosisDetailsTableObj.clearTableItems();
				diagnosisDetailsTableObj.init(bean,
						SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION);
				diagnosisDetailsTableObj.setReferenceData(referenceData);

				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
					diagnosisDetailsTableDTO.setSublimitName(null);
					diagnosisDetailsTableObj
							.addBeanToList(diagnosisDetailsTableDTO);

				}

				if (procedurdTableObj != null) {

					List<ProcedureDTO> procedureDTO = new ArrayList<ProcedureDTO>();

					procedureDTO = procedurdTableObj.getValues();

					procedurdTableObj.init(bean.getHospitalCode(),
							SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION, bean);
					procedurdTableObj.setReferenceData(referenceData);

					for (ProcedureDTO procedureDTO2 : procedureDTO) {
						procedurdTableObj.addBeanToList(procedureDTO2);
					}
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

		cmbNatureOfTreatment.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty()
						.getValue();
				if (value != null
						&& value.getValue() != null
						&& value.getValue().toString().toLowerCase()
								.contains("non")) {
					Collection<?> itemIds = cmbTreatmentType
							.getContainerDataSource().getItemIds();
					cmbTreatmentType.setValue(itemIds.toArray()[0]);
					cmbTreatmentType.setEnabled(false);
				} else {
					cmbTreatmentType.setEnabled(true);
				}
			}
		});

		// dischargeDate.setData(bean.getPreauthDataExtractionDetails().getAdmissionDate());

		dischargeDate
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (bean.getPreauthDataExtractionDetails()
								.getAdmissionDate() != null) {
							Date enteredDate = (Date) ((DateField) event
									.getProperty()).getValue();

							// if(enteredDate != null && admissionDate != null
							// && admissionDate.getValue() != null){
							// if(enteredDate.before(admissionDate.getValue())){
							// admissionDate.setValue(null);
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
							// }
							//
							if (enteredDate != null && admissionDate != null
									&& admissionDate.getValue() != null) {
								if (enteredDate.before(admissionDate.getValue())) {
									// event.getProperty().setValue(null);
									dischargeDate.setValue(null);
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

		admissionDate
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (bean.getPreauthDataExtractionDetails()
								.getAdmissionDate() != null) {

							Boolean isValid = true;

							Date discharge = (Date) ((DateField) event
									.getProperty()).getData();
							Date enteredDate = (Date) ((DateField) event
									.getProperty()).getValue();

							if (enteredDate != null && dischargeDate != null
									&& dischargeDate.getValue() != null) {
								Long daysBetweenDate = SHAUtils
										.getDaysBetweenDate(enteredDate,
												dischargeDate.getValue());
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

							if (enteredDate != null && deathDate != null
									&& deathDate.getValue() != null) {
								if (enteredDate.after(deathDate.getValue())) {
									// admissionDate.setValue(null);
									isValid = false;
									showAdmissionDateError();
									if (admissionDateValue != null) {
										admissionDate
												.setValue(admissionDateValue);
									}
								}
							}

							if (enteredDate != null && dischargeDate != null
									&& dischargeDate.getValue() != null) {
								if (enteredDate.after(dischargeDate.getValue())) {
									isValid = false;
									showErrorMessage("Admission date should not be greater than Discharge date");
									if (admissionDateValue != null) {
										admissionDate
												.setValue(admissionDateValue);
									}
								}
							} else if (enteredDate != null && discharge != null) {
								if (discharge.before(enteredDate)) {
									// event.getProperty().setValue(null);
									// dischargeDate.setValue(null);
									isValid = false;
									showErrorMessage("Discharge date cannot be lesser than the date of admission");
									if (admissionDateValue != null) {
										admissionDate
												.setValue(admissionDateValue);
									}

								}
							}

							if (isValid) {
								admissionDateValue = enteredDate;
							}
						}

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
						MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED,
						null);
			}
		});

		admissionDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8435623803385270083L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date enteredDate = (Date) event.getProperty().getValue();
				if (enteredDate != null) {
					Date policyFromDate = bean.getPolicyDto()
							.getPolicyFromDate();
					Date policyToDate = bean.getPolicyDto().getPolicyToDate();

					/*final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setClosable(false);
					dialog.setResizable(false);*/

					if (!(enteredDate.after(policyFromDate) || enteredDate
							.compareTo(policyFromDate) == 0)
							|| !(enteredDate.before(policyToDate) || enteredDate
									.compareTo(policyToDate) == 0)) {

						/*
						 * Button okButton = new Button("OK");
						 * okButton.addClickListener(new ClickListener() {
						 * private static final long serialVersionUID =
						 * -7148801292961705660L;
						 * 
						 * @Override public void buttonClick(ClickEvent event) {
						 * dialog.close(); } }); HorizontalLayout hLayout = new
						 * HorizontalLayout(okButton);
						 * hLayout.setComponentAlignment(okButton,
						 * Alignment.MIDDLE_CENTER); hLayout.setMargin(true);
						 */
						/*VerticalLayout layout = new VerticalLayout(
								new Label(
										"<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>",
										ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						;
						dialog.show(getUI().getCurrent(), null, true);*/
						
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						GalaxyAlertBox.createErrorBox("<b style = 'color: red;'>Admission Date is not in range between Policy From Date and Policy To Date. </b>", buttonsNamewithType);
						event.getProperty().setValue(null);
					}
				}

			}
		});

		changeInDiagnosis
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						// Boolean isChecked = false;
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "true") {
							diagnosisDetailsTableObj
									.enableOrDisableDeleteButton(true);
							/*
							 * changeInDiagSelected = true;
							 * changeIndiaganosisRows =
							 * diagnosisDetailsTableObj.getValues().size();
							 */
							if (procedurdTableObj != null) {
								procedurdTableObj
										.enableOrDisableDeleteButton(true);
							}

						} else {
							diagnosisDetailsTableObj
									.enableOrDisableDeleteButton(false);
							if (procedurdTableObj != null) {
								procedurdTableObj
										.enableOrDisableDeleteButton(false);
							}

						}
					}
				});

		coveredPreviousClaim
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
								MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS,
								isChecked);
					}
				});

		cmbHospitalisationDueTo
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2577540521492098375L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty()
								.getValue();

						if (null != value) {
							bean.getPreauthDataExtractionDetails()
									.setHospitalisationDueTo(value);
						}
						if (bean.getNewIntimationDTO().getPolicy().getProduct()
								.getKey() != null
								&& ReferenceTable.getFHORevisedKeys()
										.containsKey(
												bean.getNewIntimationDTO()
														.getPolicy()
														.getProduct().getKey())) {

							if (value != null
									&& value.getValue() != null
									&& !value.getValue().isEmpty()
									&& (ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY)
											.equals(value.getId())) {

								Window window = new Window();
								window.setResizable(true);
								window.setCaption("ALERT");
								window.setModal(true);
								Label alertMsg = new Label(
										"<B>Waiting period of 36 Months and once in block of THREE YEARS are applicable and <BR>Both HUSBAND and WIFE to be covered during the period of 36 Months</B>",
										ContentMode.HTML);
								alertMsg.setSizeFull();
								VerticalLayout vlayout = new VerticalLayout(
										alertMsg);
								vlayout.setSizeFull();
								vlayout.setMargin(true);
								vlayout.setComponentAlignment(alertMsg,
										Alignment.MIDDLE_CENTER);
								window.setContent(alertMsg);
								window.setWidth("530px");
								window.setHeight("100px");
								window.setResizable(false);
								window.center();
								window.setStyleName(Reindeer.WINDOW_BLACK);
								UI.getCurrent().addWindow(window);
							}
						}
						// if(value != null) {
						fireViewEvent(
								MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO,
								value);
						// }

					}
				});

		domicillaryHospitalisation
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
								MedicalApprovalDataExtractionPagePresenter.GENERATE_DOMICILLARY_FIELDS,
								isChecked);
					}
				});

		this.sectionDetailsListenerTableObj.dummySubCoverField
				.addValueChangeListener(new ValueChangeListener() {

					private static final long serialVersionUID = -7831804284490287934L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField property = (TextField) event.getProperty();
						String value = property.getValue();
						if (value != null) {
							// Other than hosptialization section is selected
							// then domicillary should get disabled....
							if (!value
									.equalsIgnoreCase(ReferenceTable.HOSP_SUB_COVER_CODE)
									&& !value
											.equalsIgnoreCase(ReferenceTable.ASSISTED_REPRODUCTION_SUB_COVER_CODE)) {
								if (domicillaryHospitalisation.getValue() != null
										&& domicillaryHospitalisation
												.getValue().toString()
												.equalsIgnoreCase("true")) {
									domicillaryHospitalisation.setValue(false);
								}
								domicillaryHospitalisation.setEnabled(false);
							} else {
								domicillaryHospitalisation.setEnabled(StarCommonUtils
										.shouldDisableDomicillary(bean));
							}
							fireViewEvent(
									MedicalApprovalDataExtractionPagePresenter.SET_DB_DETAILS_TO_REFERENCE_DATA,
									bean);
							if (diagnosisDetailsTableObj != null
									&& !diagnosisDetailsTableObj.getValues()
											.isEmpty()) {
								diagnosisDetailsTableObj.changeSublimitValues();
							}
							if (procedurdTableObj != null
									&& !procedurdTableObj.getValues().isEmpty()) {
								procedurdTableObj.changeSublimitValues();
							}
						}
					}
				});

	}

	public void addCorporateBufferListener() {
		if (corpBufferChk != null) {
			corpBufferChk.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {

					Boolean checkValue = corpBufferChk.getValue();
					if (checkValue) {
						if (corpBufferHLayout != null) {
							corpBufferHLayout.setVisible(true);
						}
						txtCorpBufferAllocatedAmt.setRequired(true);
						isCorpBufferChecked = true;
					} else {
						if (txtCorpBufferAllocatedAmt != null
								&& txtCorpBufferAllocatedAmt.getValue() != null) {
							txtCorpBufferAllocatedAmt.setValue("");
						}
						if (corpBufferHLayout != null) {
							corpBufferHLayout.setVisible(false);
						}
						txtCorpBufferAllocatedAmt.setRequired(false);
						isCorpBufferChecked = false;
					}

				}
			});
		}
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
						reasonForChangeInDOA.setRequired(true);
						/**
						 * If admission Date is changed, then
						 * reasonForChangeInDOA is mandatory
						 * */
						mandatoryFields.add(reasonForChangeInDOA);

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
		if (treatmentRemarksTableLayout != null) {
			treatmentRemarksTableLayout.removeAllComponents();

			treatmentFLayout.removeComponent(treatmentRemarksTableLayout);
		}
		if (cmbTreatmentType.getValue() != null) {
			Boolean isMedicalSeclted = true;
			if (cmbTreatmentType.getValue().toString().toLowerCase()
					.contains("medical")) {
				unbindField(treatmentRemarksTxt);
				if (treatmentRemarksTxt != null) {
					treatmentFLayout.removeComponent(treatmentRemarksTxt);
				}
				treatmentRemarksTxt = (TextArea) binder
						.buildAndBind("Treatment Remarks", "treatmentRemarks",
								TextArea.class);
				treatmentRemarksTxt.setMaxLength(4000);
				treatmentFLayout.addComponent(treatmentRemarksTxt);
				// treatmentRemarksTxt.setRequired(true);
				/**
				 * Due to below condition , treatment remarks is getting cleared
				 * when user is navigating to and fro. Not sure of the impact.
				 * Hence kept this on hold. Need to check with yosuva on this.
				 * */
				if (!this.bean.getIsQueryReceived()) {
					// treatmentRemarksTxt.setValue("");
				}
				treatmentRemarksTxt.setValidationVisible(false);

				// CSValidator validator = new CSValidator();
				// treatmentRemarksTxt.setMaxLength(100);
				// validator.extend(treatmentRemarksTxt);
				// validator.setRegExp("^[a-zA-Z 0-9]*$");
				// validator.setPreventInvalidTyping(true);

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
					if (treatmentRemarksTableLayout == null) {
						treatmentRemarksTableLayout = new HorizontalLayout();
					}
					treatmentRemarksTableLayout.addComponent(table);
					treatmentFLayout.addComponent(treatmentRemarksTableLayout);
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
					if (treatmentRemarksTableLayout != null) {
						treatmentFLayout
								.removeComponent(treatmentRemarksTableLayout);
					}

					// mandatoryFields.remove(treatmentRemarksTxt);

				}
				isMedicalSeclted = false;

				// List<DiagnosisDetailsTableDTO> diagList = null;
				/*
				 * if(null != diagnosisDetailsTableObj) { diagList =
				 * this.bean.getPreauthDataExtractionDetails
				 * ().getDiagnosisTableList(); }
				 */

				ProcedureListenerTableForPremedical procedureTableInstance = procedureTableList
						.get();
				procedureTableInstance.init(bean.getHospitalCode(),
						SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION, bean);
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
			SpecialityTableListener specialityTableInstance = specialityTableList.get();
			specialityTableInstance.init(this.bean, SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION);			
			referenceData.put("specialityType", 
					referenceData.get("medicalSpeciality"));
			// if (!isMedicalSeclted)
			if (!isMedicalSeclted) {
				referenceData.put("specialityType",
						referenceData.get("surgicalSpeciality"));
			}
			specialityTableInstance.setReferenceData(referenceData);
			this.specialityTableObj = specialityTableInstance;

			if (specialityTableInstance != null) {
				List<SpecialityDTO> specialityList = this.bean
						.getPreauthDataExtractionDetails().getSpecialityList();
				for (SpecialityDTO specialityDTO : specialityList) {
					specialityDTO.setStatusFlag(true);
					specialityTableInstance.addBeanToList(specialityDTO);
				}
			}

			tableVLayout.addComponent(specialityTableObj);
		} else {
			if (treatmentRemarksTxt != null) {
				unbindField(treatmentRemarksTxt);
				treatmentFLayout.removeComponent(treatmentRemarksTxt);
				if (treatmentRemarksTableLayout != null) {
					treatmentFLayout
							.removeComponent(treatmentRemarksTableLayout);
				}
				// mandatoryFields.remove(treatmentRemarksTxt);
			}

			if (tableVLayout != null && tableVLayout.getComponentCount() > 0) {
				tableVLayout.removeAllComponents();
			}
			this.procedurdTableObj = null;
			this.newProcedurdTableObj = null;
		}
	}

	public void showAdmissionDateError() {

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);*/
		/*
		 * Button okButton = new Button("OK"); okButton.addClickListener(new
		 * ClickListener() { private static final long serialVersionUID =
		 * -7148801292961705660L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) { dialog.close();
		 * } }); HorizontalLayout hLayout = new HorizontalLayout(okButton);
		 * hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		 * hLayout.setMargin(true);
		 */
		/*VerticalLayout layout = new VerticalLayout(
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
		GalaxyAlertBox.createErrorBox("<b style = 'color: red;'>Admission Date should not be greater than Death date</b>", buttonsNamewithType);

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

				// validation added for death date.
				addDeathDateValueChangeListener();

				txtReasonForDeath = (TextField) binder.buildAndBind(
						"Reason For Death", "reasonForDeath", TextField.class);
				txtReasonForDeath.setMaxLength(100);
				// cmbTerminateCover = (ComboBox) binder.buildAndBind(
				// "Terminate Cover", "terminateCover", ComboBox.class);

				// @SuppressWarnings("unchecked")
				// BeanItemContainer<SelectValue> terminateCover =
				// (BeanItemContainer<SelectValue>) referenceData
				// .get("terminateCover");

				// cmbTerminateCover.setContainerDataSource(terminateCover);
				// cmbTerminateCover.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				// cmbTerminateCover.setItemCaptionPropertyId("value");
				//
				// if(this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag()
				// != null &&
				// this.bean.getPreauthDataExtractionDetails().getTerminateCoverFlag().toLowerCase().equalsIgnoreCase("y"))
				// {
				// cmbTerminateCover.setValue(this.bean.getPreauthDataExtractionDetails().getTerminateCover());
				// }

				setRequiredAndValidation(deathDate);
				setRequiredAndValidation(txtReasonForDeath);
				// setRequiredAndValidation(cmbTerminateCover);

				mandatoryFields.add(deathDate);
				mandatoryFields.add(txtReasonForDeath);
				// mandatoryFields.add(cmbTerminateCover);

				patientStatusFLayout.addComponent(deathDate);
				patientStatusFLayout.addComponent(txtReasonForDeath);
				// patientStatusFLayout.addComponent(cmbTerminateCover);
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
						&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					buildNomineeLayout();
				}
				
			} else {
				
				if(nomineeDetailsTable != null) { 
					wholeVLayout.removeComponent(nomineeDetailsTable);
				}
				if(legaHeirLayout != null) {
					wholeVLayout.removeComponent(legaHeirLayout);
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

						showError();
						event.getProperty().setValue(null);
					}
					// else if(enteredDate != null && dischargeDate != null &&
					// dischargeDate.getValue() != null){
					/*
					 * if(enteredDate.after(dischargeDate.getValue())){
					 * showError(); event.getProperty().setValue(null); }
					 */
					else if (enteredDate.after(currentSystemDate)) {
						getErrorMessage("Date of Death should not be greater than current date");
						event.getProperty().setValue(null);
					}
				// }
				// else if(enteredDate.after(currentSystemDate)){
				// getErrorMessage("Date of Death should not be greater than current date");
				// event.getProperty().setValue(null);
				// }
			}
		});
	}

	private void showError() {

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setResizable(false);*/
		/*
		 * Button okButton = new Button("OK"); okButton.addClickListener(new
		 * ClickListener() { private static final long serialVersionUID =
		 * -7148801292961705660L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) { dialog.close();
		 * } }); HorizontalLayout hLayout = new HorizontalLayout(okButton);
		 * hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		 * hLayout.setMargin(true);
		 */
		/*VerticalLayout layout = new VerticalLayout(
				new Label(
						"<b style = 'color: red;'>Please select valid death date. Death date is not in range between admission date and Discharge date.</b>",
						ContentMode.HTML));
		layout.setMargin(true);
		layout.setSpacing(true);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox("Please select valid death date. Death date is not in range between admission date and Discharge date.", buttonsNamewithType);

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

	private void removePatientStatusGeneratedFields() {
		if (deathDate != null && txtReasonForDeath != null) {
			unbindField(deathDate);
			unbindField(txtReasonForDeath);
			mandatoryFields.remove(deathDate);
			mandatoryFields.remove(txtReasonForDeath);
			patientStatusFLayout.removeComponent(deathDate);
			patientStatusFLayout.removeComponent(txtReasonForDeath);
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

	public void editCorpBufferChk() {
		/**
		 * The string "Group Policy" needs to be replaced, after checkwith
		 * saravana/Sathish the valid values for Group Policy.For time being the
		 * below string is used.
		 */

		if (null != this.bean.getNewIntimationDTO()
				&& null != this.bean.getNewIntimationDTO().getPolicy()
				&& null != this.bean.getNewIntimationDTO().getPolicy()
						.getProductType()
				&& ("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO()
						.getPolicy().getProductType().getValue()))
		// if
		// (("Group").equalsIgnoreCase(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue()))
		{
			/*addCorporateBufferListener();
			corpBufferChk.setReadOnly(false);
			corpBufferChk.setValue(false);
			if (this.bean.getPreauthDataExtractionDetails().getCorpBuffer() != null
					&& this.bean.getPreauthDataExtractionDetails()
							.getCorpBuffer()) {
				corpBufferChk.setValue(this.bean
						.getPreauthDataExtractionDetails().getCorpBuffer());
				isCorpBufferChecked = this.bean
						.getPreauthDataExtractionDetails().getCorpBuffer();
			} else {
				isCorpBufferChecked = false;
			}*/
			//R1167
			corpBufferChk.setReadOnly(true);
		} else {
			corpBufferChk.setReadOnly(true);
		}
	}

	public void generatedFieldsBasedOnHospitalisationDueTo(SelectValue value) {
		if (null != value) {
			if (value.getId() != null
					&& ReferenceTable.INJURY_MASTER_ID.equals(value.getId())) {
				unbindAndRemoveFromLayout();
				injuryDate = (DateField) binder.buildAndBind(
						"Date of Injury/Accident", "injuryDate",
						DateField.class);
				cmbCauseOfInjury = (ComboBox) binder.buildAndBind(
						"Cause of Injury/Accident", "causeOfInjury",
						ComboBox.class);

				@SuppressWarnings("unchecked")
				BeanItemContainer<SelectValue> causeOfInjuryContainer = (BeanItemContainer<SelectValue>) referenceData
						.get("causeOfInjury");

				cmbCauseOfInjury.setContainerDataSource(causeOfInjuryContainer);
				cmbCauseOfInjury.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbCauseOfInjury.setItemCaptionPropertyId("value");

				if (this.bean.getPreauthDataExtractionDetails()
						.getCauseOfInjury() != null) {
					cmbCauseOfInjury.setValue(this.bean
							.getPreauthDataExtractionDetails()
							.getCauseOfInjury());
				} else {
					cmbCauseOfInjury.setValue(causeOfInjuryContainer
							.getItemIds().get(0));
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
											MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_REPORTED_TO_POLICE,
											isChecked);
								}
							}
						});

				firstFLayout.addComponent(injuryDate);
				firstFLayout.addComponent(medicalLegalCase);
				firstFLayout.addComponent(reportedToPolice);

				causeOfInsuryListener();

				secondFLayout.addComponent(cmbCauseOfInjury);

				if (this.bean.getPreauthDataExtractionDetails()
						.getReportedToPolice() != null
						&& this.bean.getPreauthDataExtractionDetails()
								.getReportedToPolice()) {
					generatedFieldsBasedOnReportedToPolice(true);
				}

			} else if (value.getId() != null
					&& ReferenceTable.ILLNESS_MASTER_ID.equals(value.getId())) {

				if (firNumberTxt != null) {
					firstFLayout.removeComponent(firNumberTxt);
				}

				if (policeReportedAttached != null) {
					secondFLayout.removeComponent(policeReportedAttached);
				}
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
				if ((bean.getNewIntimationDTO().getInsuredPatient() != null
						&& bean.getNewIntimationDTO().getInsuredPatient()
								.getInsuredGender() != null && bean
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredGender().getKey()
						.equals(ReferenceTable.FEMALE_GENDER))
						|| (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct()
								.getKey()
								))) {

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
					addTypeOfDeliveryChangeListner();
					firstFLayout.addComponent(deliveryDate);
					firstFLayout.addComponent(cmbTypeOfDelivery);
				} else {
					getErrorMessage("Selected insured is Male.Hence Maternity is not applicable for the selected insured");
					cmbHospitalisationDueTo.setValue(null);
					//Vaadin8-setImmediate() cmbHospitalisationDueTo.setImmediate(true);
				}

			} else if (value.getId() != null
					&& ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT
							.equals(value.getId())) {
				unbindAndRemoveFromLayout();
			}
		} else {
			unbindAndRemoveFromLayout();
		}
	}

	public void generatedFieldsBasedOnReportedToPolice(Boolean isChecked) {
		if (isChecked) {
			unbindAndRemovePoliceReportFromLayout();
			firNumberTxt = (TextField) binder.buildAndBind("FIR NO",
					"firNumber", TextField.class);
			firNumberTxt.setMaxLength(15);

			CSValidator validator = new CSValidator();
			validator.extend(firNumberTxt);
			validator.setRegExp("^[a-zA-Z 0-9]*$");
			validator.setPreventInvalidTyping(true);

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

	private void unbindAndRemovePoliceReportFromLayout() {
		unbindField(firNumberTxt);
		unbindField(policeReportedAttached);

		if (firNumberTxt != null && policeReportedAttached != null) {
			firstFLayout.removeComponent(firNumberTxt);
			secondFLayout.removeComponent(policeReportedAttached);
		}

	}

	private void showErrorMessage(String eMsg) {
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
			VerticalLayout layout = new VerticalLayout(sittingsField);
			layout.setMargin(true);
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
		/*	VerticalLayout layout = new VerticalLayout(
					new Label(
							"<b style = 'color:red'>The following Attibutes has been changed from Previous ROD : </b>",
							ContentMode.HTML), new Label(comparisonString,
							ContentMode.HTML));
			layout.setMargin(true);*/
			/*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);*/

			/*Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
			
			VerticalLayout layout = new VerticalLayout(
					new Label(
							"The following Attibutes has been changed from Previous ROD",
							ContentMode.HTML), new Label(comparisonString,
							ContentMode.HTML));
			layout.setMargin(true);
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
			/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);*/
			
			/*dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);*/
		}
	}

	public void alertMessageForChangeOfTreatmentType() {
		
		VerticalLayout layout = new VerticalLayout(
				new Label("Entered Procedure Will be marked as deleted one. Do you want to proceed further ?"));
		layout.setMargin(true);
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setResizable(false);
		dialog.setModal(true);
*/
		/*Button okButton = new Button("Yes");
		Button cancelButton = new Button("No");*/
		//okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Confirmation", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
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
						MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED,
						null);
				//dialog.close();
			}
		});

		/*cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				cmbTreatmentType.setValue(cmbTreatmentType.getItemIds()
						.toArray()[1]);
				//dialog.close();
			}
		});
		/*HorizontalLayout hLayout = new HorizontalLayout(okButton, cancelButton);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		hLayout.setMargin(true);*/
		
		/*dialog.setContent(layout);
		dialog.show(getUI().getCurrent(), null, true);*/
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
								noOfDays++;
								if (noOfDays < 0l) {
									noOfDays = 0l;
								}

								if (noOfDays <= 3l) {
									StarCommonUtils
											.alertMessage(
													getUI(),
													SHAConstants.DOMICILLARY_ALERT_MESSAGE);
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
										if (noOfDays <= 3l) {
											StarCommonUtils
													.alertMessage(
															getUI(),
															SHAConstants.DOMICILLARY_ALERT_MESSAGE);
										}
										noOfDays++;
										hospitalizationNoOfDays
												.setValue(noOfDays.toString());
									}
								}
							}
						}
					});

			int componentIndex = firstFLayout
					.getComponentIndex(domicillaryHospitalisation);
			firstFLayout.addComponent(treatmentStartDate, componentIndex + 1);
			firstFLayout.addComponent(treatmentEndDate, componentIndex + 2);

			secondFLayout.addComponent(hospitalizationNoOfDays);
		}
	}

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		// TODO Auto-generated method stub
		sectionDetailsListenerTableObj.setCoverList(coverContainer);

	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);

	}

	public void suspiousPopupMessage() {
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);*/
		VerticalLayout layout = new VerticalLayout();
		Map<String, String> popupMap = bean.getSuspiciousPopupMap();
		for (Map.Entry<String, String> entry : popupMap.entrySet()) {
			/*Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(null);
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);*/
			layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
		}
		layout.setMargin(true);
		layout.setSpacing(true);
		/*Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		okButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();
				if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				}else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				} 
			}
		});

		/*
		 * HorizontalLayout hLayout = new HorizontalLayout(okButton);
		 * hLayout.setSpacing(true); hLayout.setComponentAlignment(okButton,
		 * Alignment.MIDDLE_CENTER); hLayout.setMargin(true);
		 */
		
		//layout.addComponent(okButton);
		//layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		
		/*dialog.setContent(layout);
		dialog.setWidth("30%");*/
		this.bean.setIsPopupMessageOpened(true);
		//dialog.show(getUI().getCurrent(), null, true);
	}

	public void nonPreferredPopupMessage() {
	/*	final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setModal(true);*/
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
		/*Button okButton = new Button("OK");
		okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		okButton.addClickListener(new ClickListener() {

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
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});

		/*
		 * HorizontalLayout hLayout = new HorizontalLayout(okButton);
		 * hLayout.setSpacing(true); hLayout.setComponentAlignment(okButton,
		 * Alignment.MIDDLE_CENTER); hLayout.setMargin(true);
		 */
		
		/*dialog.setContent(layout);
		dialog.setWidth("30%");*/
		this.bean.setIsPopupMessageOpened(true);
		/*dialog.show(getUI().getCurrent(), null, true);*/
	}

	public void clearReferenceData() {
		SHAUtils.setClearReferenceData(referenceData);
		this.diagnosisDetailsTableObj.clearObject();
		this.diagnosisDetailsTableObj = null;
		this.sectionDetailsListenerTableObj = null;
		this.specialityTableObj = null;
		this.procedurdTableObj = null;
	}

	private BlurListener getgmcCorpBufferLimitListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if (value != null && value.getValue() != null
						&& !value.getValue().isEmpty()) {

					Double diff = Double.valueOf(value.getValue());
					Double si = bean.getPreauthDataExtractionDetails()
							.getCorpBufferSI() != null ? bean
							.getPreauthDataExtractionDetails()
							.getCorpBufferSI() : 0d;
					Double utilisedAmt = bean.getPreauthDataExtractionDetails()
							.getCorpBufferUtilisedAmt() != null ? bean
							.getPreauthDataExtractionDetails()
							.getCorpBufferUtilisedAmt() : 0d;

					if (diff > (si - utilisedAmt)) {
						getGMCAlert();
						bean.getClaimDTO().setGmcCorpBufferLmt(0);
						value.setValue(bean.getClaimDTO().getGmcCorpBufferLmt() != null ? bean
								.getClaimDTO().getGmcCorpBufferLmt().toString()
								: "");
					} else {
						bean.getClaimDTO().setGmcCorpBufferLmt(
								Integer.valueOf(value.getValue()));
					}

				} else {
					bean.getClaimDTO().setGmcCorpBufferLmt(null);
				}

			}
		};
		return listener;
	}

	public void getGMCAlert() {
		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.GMC_ALERT_AMT + "</b>", ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

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
				.createAlertBox(SHAConstants.GMC_ALERT_AMT + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});

	}

	public void causeOfInsuryListener() {

		if (cmbCauseOfInjury != null) {
			cmbCauseOfInjury.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {

					SelectValue value = (SelectValue) event.getProperty()
							.getValue();
					if (value != null && value.getId() != null
							&& value.getId().equals(1102L)) {
						if (btnRTAView != null) {
							btnRTAView.setEnabled(true);
						}
					} else {
						if (btnRTAView != null) {
							btnRTAView.setEnabled(false);
						}
					}
				}
			});
		}
	}

	public void setViewRTAButton(Button btnViewRTAButton) {
		this.btnRTAView = btnViewRTAButton;
	}

	protected void showPopup(final String message) {

		String message1 = message.replaceAll("(.{200})", "$1<br />");
		message1 = message1.replaceAll("(\r\n|\n)", "<br />");

		/*Label successLabel = new Label("<b style = 'color: red;'>" + message1
				+ "</br>", ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		// layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		layout.setHeightUndefined();
		
		 * if(bean.getClmPrcsInstruction()!=null &&
		 * bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
		 * if(message.length()>4000){ layout.setHeight("100%");
		 * layout.setWidth("100%"); }
		 * 
		 * }
		 
		final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message1, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		/*
		 * if(bean.getClmPrcsInstruction()!=null &&
		 * bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
		 * if(message.length()>4000){ dialog.setWidth("55%"); } }
		 */
		//dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getClmPrcsInstruction() != null
						&& !bean.getClmPrcsInstruction().equalsIgnoreCase(
								message)) {
					showPopup(bean.getClmPrcsInstruction());
				}
			}
		});

	}

	private void cardiaCareSectionAlert() {

		String msg = SHAConstants.CARDIAC_CARE_SECTION_ALERT;

		/*Label successLabel = new Label("<b style = 'color: red;'>" + msg
				+ "</b>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setStyleName("borderLayout");
		layout.setSpacing(true);
		layout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(msg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				// bean.setIsPopupMessageOpened(true);
				//dialog.close();

				if (null != bean.getNewIntimationDTO().getPolicy()
						.getPolicyPlan()) {
					alertMessageForPlocyPlan();

				}

			}

		});

		//dialog.setModal(true);
		// dialog.close();
		// UI.getCurrent().addWindow(popup);
	}

	public Boolean alertMessageForPlocyPlan() {

		String msg = "";

		if (SHAConstants.POLICY_GOLD_PLAN.equalsIgnoreCase(bean
				.getNewIntimationDTO().getPolicy().getPolicyPlan())) {

			msg = SHAConstants.GOLD_ALERT_MSG;
		} else if (SHAConstants.POLICY_SILVER_PLAN.equalsIgnoreCase(bean
				.getNewIntimationDTO().getPolicy().getPolicyPlan())) {

			msg = SHAConstants.SILVER_ALERT_MSG;

		}

		/*Label successLabel = new Label("<b style = 'color: red;'>" + msg
				+ "</b>", ContentMode.HTML);
		// final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setStyleName("borderLayout");
		layout.setSpacing(true);
		layout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(msg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		return true;
	}

	public void policyValidationPopupMessage() {

		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
				ContentMode.HTML);
		// final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		 * HorizontalLayout hLayout = new HorizontalLayout(layout);
		 * hLayout.setMargin(true); hLayout.setStyleName("borderLayout");
		 

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
				.createAlertBox(SHAConstants.POLICY_VALIDATION_ALERT, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getClaimCount() > 1 && !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});
	}

	public void zuaQueryAlertPopupMessage() {

		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ SHAConstants.ZUA_QUERY_ALERT + "</b>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		Button zuaViewButton = new Button("View ZUA History");
		zuaViewButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		HorizontalLayout btnLayout = new HorizontalLayout(homeButton,
				zuaViewButton);
		btnLayout.setSpacing(true);

		VerticalLayout layout = new VerticalLayout(successLabel, btnLayout);
		layout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);

		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		 * HorizontalLayout hLayout = new HorizontalLayout(layout);
		 * hLayout.setMargin(true); hLayout.setStyleName("borderLayout");
		 

		final ConfirmDialog dialog = new ConfirmDialog();
		// dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View ZUA History");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.ZUA_QUERY_ALERT, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button zuaViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getClaimCount() > 1 && !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				} else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});

		zuaViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues = SHAUtils
						.setViewTopZUAQueryHistoryTableValues(bean
								.getNewIntimationDTO().getPolicy()
								.getPolicyNumber(), policyService);

				List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues = SHAUtils
						.setViewZUAQueryHistoryTableValues(bean
								.getNewIntimationDTO().getPolicy()
								.getPolicyNumber(), policyService);

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

	public void setAssistedValue(Long value) {
		this.assistedTreatment = value;
	}

	private void showFVRPendingMessage() {

		String msg = bean.getFVRPendingRsn();
		/*Label successLabel = new Label(
				"<div style = 'text-align:center;'><b style = 'color: red;'>"
						+ msg + "</b></div>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout firstForm = new VerticalLayout(successLabel, homeButton);
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
				.createAlertBox(msg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();
			}
		});

	}

	private void showICRMessage() {
		String msg = SHAConstants.ICR_MESSAGE;
		/*Label successLabel = new Label(
				"<div style = 'text-align:center;'><b style = 'color: red;'>"
						+ msg + "</b></div>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout firstForm = new VerticalLayout(successLabel, homeButton);
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
				.createAlertBox(msg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsPopupMessageOpened(true);
				//dialog.close();

				if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});
	}

	public void buildRefToBillEntryLayout() {
		this.bean.setStatusKey(ReferenceTable.ZMR_REFER_TO_BILL_ENTRY);

		reasonRefBillEntryTxta = new TextArea(
				"Reason For Referring To Bill Entry");
		// , "referToBillEntryBillingRemarks", TextArea.class);
		reasonRefBillEntryTxta.setMaxLength(4000);
		reasonRefBillEntryTxta.setWidth("400px");
		reasonRefBillEntryTxta
				.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		reasonRefBillEntryTxta.setId("billEntryRmrks");
		remarksPopupListener(reasonRefBillEntryTxta, null);

		FormLayout fieldsFLayout;
		fieldsFLayout = new FormLayout(reasonRefBillEntryTxta);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);

		VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout,btnLayout);
		// verticalLayout.setWidth("500px");
		verticalLayout.setWidth("800px");
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		
		showInPopup(verticalLayout, dialog);
	}

	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (reasonRefBillEntryTxta.getValue() != null
						&& !reasonRefBillEntryTxta.getValue().isEmpty()) {
					bean.getPreauthMedicalDecisionDetails()
							.setReferToBillEntryBillingRemarks(
									reasonRefBillEntryTxta.getValue());
					dialog.close();
				} else {
					eMsg.append("Please Enter Reason for Referring to Bill Entry.");
					showErrorMessage(eMsg.toString());
				}
			}
		});
		return submitButton;
	}

	private Button getCancelButton(final ConfirmDialog dialog) {
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		return cancelButton;
	}

	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);

		dialog.show(getUI().getCurrent(), null, true);

	}

	public void remarksPopupListener(TextArea searchField,
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForMedicalRemarks(searchField,
				getShortCutListenerForRemarks(searchField));

	}

	public void handleShortcutForMedicalRemarks(final TextArea textField,
			final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}

	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("", KeyCodes.KEY_F8,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout = new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				// txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				// txtArea.setSizeFull();

				// if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) ||
				// ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				txtArea.setReadOnly(false);
				// }

				txtArea.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						txtFld.setValue(((TextArea) event.getProperty())
								.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				String strCaption = "Reason for Referring to Billentry";

				dialog.setCaption(strCaption);

				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	public void paayasClaimManualyProcessedAlertMessage(String insuredName) {

		/*Label successLabel = new Label(
				"<b style = 'color: red;'>"
						+ "Claim processed outside the system for the insured-"
						+ insuredName
						+ " Verify  and restirict sum insured and other benefits  before proceeding "
						+ "</b>", ContentMode.HTML);
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
				.createAlertBox("Claim processed outside the system for the insured-"
						+ insuredName
						+ " Verify  and restirict sum insured and other benefits  before proceeding "
						+ "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()) {
					showFVRPendingMessage();
				}

				if (masterService.doGMCPolicyCheckForICR(bean.getPolicyDto()
						.getPolicyNumber())) {
					showICRMessage();
				} else if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				}

				else if (bean.getIsZUAQueryAvailable()) {
					zuaQueryAlertPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				}else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});
	}

	public void getHospitalCategory(String hospitalCategory) {

		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ hospitalCategory + " Category Hospital" + "</b>",
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
				.createAlertBox(hospitalCategory + " Category Hospital" + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getNewIntimationDTO().getIsPaayasPolicy()
						&& bean.getNewIntimationDTO()
								.getIsClaimManuallyProcessed()) {
					paayasClaimManualyProcessedAlertMessage(bean
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredName());
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getIsFvrIntiated()) {
					showFVRPendingMessage();
				} else if (masterService.doGMCPolicyCheckForICR(bean
						.getPolicyDto().getPolicyNumber())) {
					showICRMessage();
				} else if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				}

				else if (bean.getIsZUAQueryAvailable()) {
					zuaQueryAlertPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				} else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});
	}

	public void multipleInvsAlertMessage(Integer invstigatorsCount,
			Integer invsReplyReceivedCount) {

		/*Label successLabel = new Label("<b style = 'color: red;'>"
				+ "Investigation assigned to " + invstigatorsCount
				+ " investigators. Reply received from "
				+ invsReplyReceivedCount + " investigator" + "</b>",
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
				.createAlertBox("Investigation assigned to " + invstigatorsCount
				+ " investigators. Reply received from "
				+ invsReplyReceivedCount + " investigator", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (bean.getPreauthMedicalDecisionDetails()
						.getIsInvsInitiated()) {
					getInvsAlert();
				} else if (null != bean.getNewIntimationDTO().getHospitalDto()
						.getFinalGradeName()) {
					getHospitalCategory(bean.getNewIntimationDTO()
							.getHospitalDto().getFinalGradeName());
				} else if (bean.getNewIntimationDTO().getIsPaayasPolicy()
						&& bean.getNewIntimationDTO()
								.getIsClaimManuallyProcessed()) {
					paayasClaimManualyProcessedAlertMessage(bean
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredName());
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getIsFvrIntiated()) {
					showFVRPendingMessage();
				}

				else if (masterService.doGMCPolicyCheckForICR(bean
						.getPolicyDto().getPolicyNumber())) {
					showICRMessage();
				} else if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				}

				else if (bean.getIsZUAQueryAvailable()) {
					zuaQueryAlertPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}

			}
		});
	}

	public void getInvsAlert() {

		/*Label successLabel = new Label(
				"<b style = 'color: red;'> Investigation has been initiated and Report is pending "
						+ "</b>", ContentMode.HTML);
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
				.createAlertBox("Investigation has been initiated and Report is pending ", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if (null != bean.getNewIntimationDTO().getHospitalDto()
						.getFinalGradeName()) {
					getHospitalCategory(bean.getNewIntimationDTO()
							.getHospitalDto().getFinalGradeName());
				} else if (bean.getNewIntimationDTO().getIsPaayasPolicy()
						&& bean.getNewIntimationDTO()
								.getIsClaimManuallyProcessed()) {
					paayasClaimManualyProcessedAlertMessage(bean
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredName());
				} else if (bean.getPreauthMedicalDecisionDetails()
						.getIsFvrIntiated()) {
					showFVRPendingMessage();
				}
				else if (masterService.doGMCPolicyCheckForICR(bean
						.getPolicyDto().getPolicyNumber())) {
					showICRMessage();
				} else if (bean.getIsPolicyValidate()) {
					policyValidationPopupMessage();
				}

				else if (bean.getIsZUAQueryAvailable()) {
					zuaQueryAlertPopupMessage();
				} else if (bean.getClaimCount() > 1
						&& !bean.getIsPopupMessageOpened()) {
					alertMessageForClaimCount(bean.getClaimCount());
				} else if (!bean.getSuspiciousPopupMap().isEmpty()) {
					suspiousPopupMessage();
				} else if (!bean.getNonPreferredPopupMap().isEmpty()) {
					nonPreferredPopupMessage();
				} else if (bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				} else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				} else if (bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}  else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
				}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
						bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
					 popupMessageFor30DaysWaitingPeriod();
				} else if (bean.getIsSuspicious() != null) {
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(),
							bean.getClmPrcsInstruction());
				}
			}
		});
	}
	
	public void setBillEntryDisable(Boolean isFvrInvInitiated) {
		this.referToBillEntryBtn.setEnabled(Boolean.FALSE);
	}

		public void setProcedureValues(BeanItemContainer<SelectValue> procedures) {
			specialityTableObj.setProcedure(procedures);
			
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
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					} else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
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
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					} else if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
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
			});

			
			
		}
		
		public void showTopUpAlertMessage(String remarks) {	 
			 
			/*Label successLabel = new Label(
			"<b style = 'color: red;'>" + remarks + "</b>",
			ContentMode.HTML);*/
	TextArea txtArea = new TextArea();
	txtArea.setMaxLength(4000);
	txtArea.setData(bean);
	//txtArea.setStyleName("Boldstyle");
	txtArea.setValue(remarks);
	txtArea.setNullRepresentation("");
	txtArea.setSizeFull();
	txtArea.setWidth("100%");
	txtArea.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
	
	/*Button homeButton = new Button("OK");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
	
	
	txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
	VerticalLayout layout = new VerticalLayout(txtArea);
	//layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	layout.setStyleName("borderLayout");
	
	/*final ConfirmDialog dialog = new ConfirmDialog();
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setHeight(layout.getHeight(), Sizeable.UNITS_PERCENTAGE);
	dialog.setWidth("45%");
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
			    
			    if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && !bean.getDuplicateInsuredList().isEmpty()){
					showDuplicateInsured(bean.getDuplicateInsuredList());
				}else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
						(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
								bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
					showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
				}
				//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
				else if(!bean.getIsGeoSame()) {
					 getGeoBasedOnCPU();
				}
				else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
					if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
						multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
					}		
				}		
				else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
					getInvsAlert();
				}
				else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}
				else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
					paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
				}	
				else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
					showFVRPendingMessage();
				}
				
				else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getIsPolicyValidate()){		
					policyValidationPopupMessage();
				}
				
				else if(bean.getIsZUAQueryAvailable()){
					zuaQueryAlertPopupMessage();
				}
				else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
					alertMessageForClaimCount(bean.getClaimCount());
				}else if(!bean.getSuspiciousPopupMap().isEmpty()){
					suspiousPopupMessage();
				}
				else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIsPEDInitiated()) {
//					alertMessageForPED();
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}else{
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}
				}else if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}else if(bean.getIsAutoRestorationDone()) {
					alertMessageForAutoRestroation();
				}else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
					if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
					} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
						warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
					}
					
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
	 	    	/*boolean dateRange= SHAUtils.validateRangeDate(policyFromDate);
		    	if(dateRange && (ReferenceTable.getCovidProducts().contains(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
		    		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createAlertBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG, buttonsNamewithType);
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
		    	}else{*/
		    		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createAlertBox(SHAConstants.THIRTY_DAYS_WAITING_ALERT, buttonsNamewithType);
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

		public void showAlertForGMCParentLink(String policyNumber){	 
			 
		    /*Label successLabel = new Label(
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
					if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
						alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
				}
			});
		}
		
		public void showCMDAlert(String memberType) {	 
			 
			/* Label successLabel = new Label(
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
						.createInformationBox(SHAConstants.CMD_ALERT_LATEST + memberType + " Club", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						} 
						else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
							alertMessageForPostHosp();
						} 
						else if(bean.getIsHospitalDiscountApplicable()){
							alertForHospitalDiscount();
						} 
						else if(null != bean.getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getTopUpPolicyAlertFlag())){
							showTopUpAlertMessage(bean.getTopUpPolicyAlertMessage());
						}
						else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getDuplicateInsuredList() != null && ! bean.getDuplicateInsuredList().isEmpty()){
							showDuplicateInsured(bean.getDuplicateInsuredList());
						}
						else if(bean.getPolicyDto().getGmcPolicyType() != null && bean.getPolicyDto().getLinkPolicyNumber() != null && 
								(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
										bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
							showAlertForGMCParentLink(bean.getPolicyDto().getLinkPolicyNumber());
						}
						//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
						else if(!bean.getIsGeoSame()) {
							 getGeoBasedOnCPU();
						 }
						else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
							if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
								multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
							}		
						}		
						else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
							getInvsAlert();
						}
						else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
							getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
						}
						else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
							paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
						}	
						else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
							showFVRPendingMessage();
						}
						
						else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						
						else if(bean.getIsZUAQueryAvailable()){
							zuaQueryAlertPopupMessage();
						}
						else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
							alertMessageForClaimCount(bean.getClaimCount());
						}else if(!bean.getSuspiciousPopupMap().isEmpty()){
							suspiousPopupMessage();
						}
						else if(!bean.getNonPreferredPopupMap().isEmpty()){
							nonPreferredPopupMessage();
						}
						else if(bean.getIsPEDInitiated()) {
//							alertMessageForPED();
							if(bean.isInsuredDeleted()){
								alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
							}else{
								alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
							}
						}else if(bean.getIsAutoRestorationDone()) {
							alertMessageForAutoRestroation();
						}else if(bean.getIs64VBChequeStatusAlert()){
							get64VbChequeStatusAlert();
						}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
							if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
								StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
							} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
								warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
							}
							
						}else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
								bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
								&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
							 popupMessageFor30DaysWaitingPeriod();
						}	
					}
				});
			}
		
		
		private void siRestricationAlert(List<String> list) {
			 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				if(list.size()==2){
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(list.get(1).toString(), buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {DBCalculationService dbService = new DBCalculationService();
					String memberType = dbService.getCMDMemberType(bean.getPolicyKey());
					if(bean.getNewIntimationDTO().getIsDeletedRisk()){
						showDeletedInsuredAlert();
					}
					else if(null != memberType && !memberType.isEmpty()){
						showCMDAlert(memberType);
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} 
					else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} 
					else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
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
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
}
				});
			}
		 }
				
		
		private void prevClmInvstAlert(List<String> popupPrevClmInvst) {
			// TODO Auto-generated method stub

			 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				if(popupPrevClmInvst.size()==2){
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(popupPrevClmInvst.get(1).toString(), buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						
					DBCalculationService dbService = new DBCalculationService();
					String memberType = dbService.getCMDMemberType(bean.getPolicyKey());
					
					if(bean.getPopupSIRestrication()!=null && bean.getPopupSIRestrication().size() == 2){
								siRestricationAlert(bean.getPopupSIRestrication());
					}
					else if(bean.getNewIntimationDTO().getIsDeletedRisk()){
						showDeletedInsuredAlert();
					}
					else if(null != memberType && !memberType.isEmpty()){
						showCMDAlert(memberType);
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					} 
					else if(!bean.getShouldShowPostHospAlert() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag() && (bean.getRodTotalClaimedAmount() >= 5000d || SHAUtils.getPostHospClaimedAmount(bean) > 5000d) ) {
						alertMessageForPostHosp();
					} 
					else if(bean.getIsHospitalDiscountApplicable()){
						alertForHospitalDiscount();
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
					//comparing geographical location of Hospital Location(Hospital Location CPU) with Insured Residence(Insured Residence CPU)
					else if(!bean.getIsGeoSame()) {
						 getGeoBasedOnCPU();
					 }
					else if(bean.getPreauthDataExtractionDetails().getIsMultipleInvsAssigned()){
						if(null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() && null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()){
							multipleInvsAlertMessage(bean.getPreauthDataExtractionDetails().getInvestigatorsCount(),bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount());
						}		
					}		
					else if(bean.getPreauthMedicalDecisionDetails().getIsInvsInitiated()){
						getInvsAlert();
					}
					else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsClaimManuallyProcessed()){
						paayasClaimManualyProcessedAlertMessage(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName());
					}	
					else if(bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						showFVRPendingMessage();
					}
					
					else if(masterService.doGMCPolicyCheckForICR(bean.getPolicyDto().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}
					
					else if(bean.getIsZUAQueryAvailable()){
						zuaQueryAlertPopupMessage();
					}
					else if(bean.getClaimCount() >1  && !bean.getIsPopupMessageOpened()){
						alertMessageForClaimCount(bean.getClaimCount());
					}else if(!bean.getSuspiciousPopupMap().isEmpty()){
						suspiousPopupMessage();
					}
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIsPEDInitiated()) {
//						alertMessageForPED();
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}else{
							alertMessageForPED(SHAConstants.PED_RAISE_MESSAGE);
						}
					}else if(bean.getIsAutoRestorationDone()) {
						alertMessageForAutoRestroation();
					}else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(ReferenceTable.MEDIPREMIER_PRODUCT_CODE) && bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)) {
						if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
							StarCommonUtils.alertMessage(getUI(), SHAConstants.MEDI_PREMIER_HOSP_ALERT_MESSAGE);
						} else if(bean.getClaimDTO().getClaimSectionCode() != null && bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
							warningMessageForLumpsum(SHAConstants.LUMPSUM_WARNING_MESSAGE);
						}
						
					}else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}else if(bean.getNewIntimationDTO().getPolicy().getPolicyType() != null && 
							bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey().equals(ReferenceTable.FRESH_POLICY)
							&& bean.getNewIntimationDTO().getPolicy().getProduct().getWaitingPeriod().equals(ReferenceTable.WAITING_PERIOD)){
						 popupMessageFor30DaysWaitingPeriod();
					}
}
				});
			}
		 
			
			
		}
		
		
		public MessageBox showInfoMessageBox(String message){
			final MessageBox msgBox = MessageBox
				    .createInfo()
				    .withCaptionCust("Information")
				    .withHtmlMessage(message)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			
			return msgBox;
	      }
		
		public void buildNomineeLayout(){
			
			if(referToBillEntryBtn != null){
				wholeVLayout.removeComponent(referToBillEntryBtn);
			}
			
			if(nomineeDetailsTable != null) { 
				wholeVLayout.removeComponent(nomineeDetailsTable);
			}
			if(legalHeirLayout != null) {
				wholeVLayout.removeComponent(legalHeirLayout);
			}
					
			nomineeDetailsTable.init("", false, false);
					
			if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
			  nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
			  nomineeDetailsTable.setViewColumnDetails();
			  nomineeDetailsTable.generateSelectColumn();
			  
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
			legalHeirDetails.setViewColumnDetails();
			legalHeirLayout.addComponent(legalHeirDetails);
			wholeVLayout.addComponent(legalHeirLayout);

			if(enableLegalHeir) {
				
				legalHeirDetails.addBeanToList(bean.getLegalHeirDTOList());
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}
			
			if(referToBillEntryBtn != null){
				wholeVLayout.addComponent(referToBillEntryBtn);
			}
			
			
		  }
		//added for CR - Young star product by noufel on 08-04-2020
		public void addTypeOfDeliveryChangeListner(){
			cmbTypeOfDelivery.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = -2577540521492098375L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_YOUNG_PRODUCT_CODE) ||
									bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_91))){


						if(value != null && value.getValue() != null){
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createInformationBox("Waiting period of 36 months for Maternity from the date of first commencement of this policy" , buttonsNamewithType);
							Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						}
					}
				}
			});
		}
}
