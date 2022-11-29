package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorPagePresenter;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewPaymentDetailTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalBillingUI;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsPatientCareListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.ConsolidatedAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.HospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherBenefitCalculationUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerPostHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerPreHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.PostHospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.billing.wizard.PreHospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPageUIAsPopup;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestFileUploadUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimsSubmitHandler;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
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
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
@Default
public class FinancialHospitalizationPageUI extends ViewComponent implements ClaimsSubmitHandler {

	//	private static final long serialVersionUID = -6992464970175605792L;


	@Inject
	private PreauthDTO bean;

	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	private GWizard wizard;

	private TextField hospitalCashPayableAmt;

	private TextField patientCarePayableAmt;

	@Inject
	private Instance<FinancialButtons> financialButtonInstance;

	private FinancialButtons financialButtonObj;

	@Inject
	private Instance<HospitalizationCalcualtionUI> hospitalizaionInstance;

	@Inject
	private Instance<PreHospitalizationCalcualtionUI> preHospitalizationInstance;

	@Inject
	private Instance<PostHospitalizationCalcualtionUI> postHospitalizationInstance;

	private HospitalizationCalcualtionUI hospitalizaionObj;

	private PreHospitalizationCalcualtionUI preHospitalizationObj;

	private PostHospitalizationCalcualtionUI postHospitalizationObj;

	@Inject
	private Instance<AddOnBenefitsListenerTable> addOnBenefitsListenerTable;

	private AddOnBenefitsListenerTable addOnBenefitsListenerTableObj;

	@Inject
	private Instance<OtherBenefitCalculationUI> otherBenefitCalculationInstance;

	private OtherBenefitCalculationUI otherBenefitCalculationObj;

	@Inject
	private AddOnBenefitsPatientCareListenerTable addOnBenefitsPatientCareListnerTable;

	@Inject
	private UploadedFileViewUI fileViewUI;

	@Inject
	private Instance<FinancialProcessPageUIAsPopup> finacialProcessPagePopup;

	private FinancialProcessPageUIAsPopup finacialProcessPagePopupObj;

	private ClaimRequestFileUploadUI specialistWindow = new ClaimRequestFileUploadUI();

	private VerticalLayout wholeLayout;


	//private TextField otherInsurerHospAmt;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	private BeanFieldGroup<PreauthDataExtaractionDTO> paymentbinder;


	private TextField otherInsurerPreHospAmt;

	private Set<String> errorMessages;


	private TextField otherInsurerPostHospAmt;

	private OptionGroup optPaymentMode;

	private Button btnIFCSSearch;

	private TextField txtPayableAt;

	private TextField txtAccntNo;

	private VerticalLayout paymentDetailsLayout;

	public ComboBox cmbPayeeName;

	private TextField txtEmailId;

	private TextField txtReasonForChange;

	private TextField txtPanNo;
	
	private TextField txtAccountPref;
	
	private TextField txtAccType;
	
	private Button btnAccPrefSearch;
	
	private HorizontalLayout accPrefLayout;
	
	private TextField txtRelationship;
	
	private TextField txtNameAsPerBank;
	
	/*private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;*/

	private TextField txtIfscCode;

	private TextField txtBranch;

	private TextField txtBankName;

	private TextField txtCity;

	private SelectValue existingPayeeName;

	private Map<String, Object> referenceData;

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchAccountTypeWindow;
	
	@Inject
	private Instance<OtherInsurerHospSettlementAmountUI> otherInsHospInstance;

	@Inject
	private Instance<OtherInsurerPreHospSettlementAmountUI> otherInsPreHospInstance;

	@Inject
	private Instance<OtherInsurerPostHospSettlementAmountUI> otherInsPostHospInstance;

	private OtherInsurerHospSettlementAmountUI otherInsurerHospObj;

	private OtherInsurerPreHospSettlementAmountUI otherInsurerPreHospObj;

	private OtherInsurerPostHospSettlementAmountUI otherInsurerPostHospObj;

	private VerticalLayout otherInsSettlementTab ;

	@Inject
	private Instance<ConsolidatedAmountUI> consolidatedAmountInstance;

	private ConsolidatedAmountUI consolidatedAmountObj;

	private HorizontalLayout otherInsTabLayout;

	private OptionGroup otherInsApplicable;

	private Button btnOk;

	private Button btnCancel;

	private VerticalLayout previousPaymentVerticalLayout;


	@Inject
	private PreviousAccountDetailsTable previousAccountDetailsTable ;

	private Button btnPopulatePreviousAccntDetails;

	private Window populatePreviousWindowPopup;

	private HorizontalLayout previousAccountDetailsLayout;

	@EJB
	private DBCalculationService dbCalculationService;

	//@Inject
	//private EditPaymentDetailsView editPaymentDetailsView ;

	@EJB
	private MasterService masterService;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private TextField txtPayModeChangeReason;

	private VerticalLayout vLayout ;

	private TextArea txtAreaFAInternalRemarks;

	private Button btnCitySearch;

	private Button verifyAcntDtlButton;

	public TextField rechargeDummyField;

	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
	@Inject
	private Instance<VerificationAccountDeatilsTable> verificationAccountDeatilsTableInstance;

	private VerificationAccountDeatilsTable verificationAccountDeatilsTableObj;

	@Inject
	private Instance<LegalBillingUI> legalBillingUIInstance;
	
	private LegalBillingUI legalBillingUIObj;
	
	private VerticalLayout legalBillingLayout;
	
	private TextField txtPaymentPartyMode;
	
	@Override
	public String getCaption() {
		return "Bill Hospitalization";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}


	public void init(PreauthDTO bean, GWizard wizard) {
		vLayout = new VerticalLayout();
		this.bean = bean;
		this.wizard = wizard;

		if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
			existingPayeeName = this.bean.getPreauthDataExtractionDetails().getPayeeName();
		}

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthMedicalDecisionDetails());
		this.paymentbinder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.paymentbinder.setItemDataSource(this.bean.getPreauthDataExtractionDetails());
	}

	public Component getContent() {
		initBinder();
		//added by noufel for modern sublimit
		if(this.bean.getIsModernSublimitSIExhaust()){
			SHAUtils.showAlertMessageBox(SHAConstants.MODERN_SUBLIMIT_ALERT);
		}
		addOnBenefitsListenerTableObj =  addOnBenefitsListenerTable.get();
		addOnBenefitsListenerTableObj.init();
		addOnBenefitsPatientCareListnerTable.init();

		paymentbinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		Integer hospitalCashPayableAmount = 0;
		Integer patientCarePayableAmount = 0;
		if(null != this.addOnBenefitsListenerTable)
		{
			if(null != this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() && !this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {

					try{

						if(null != addOnBenefitsDTO.getDateOfAdmission())
						{
							Date tempDateOfAdmission = SHAUtils.formatTimeFromString(addOnBenefitsDTO.getDateOfAdmission().toString());
							addOnBenefitsDTO.setDateOfAdmission(SHAUtils.formatDate(tempDateOfAdmission));
						}					
					}catch(Exception e){
						e.printStackTrace();
					}
					try{
						if(null != addOnBenefitsDTO.getDateOfDischarge())
						{
							Date tempDateOfDischarge = SHAUtils.formatTimeFromString(addOnBenefitsDTO.getDateOfDischarge().toString());
							addOnBenefitsDTO.setDateOfDischarge(SHAUtils.formatDate(tempDateOfDischarge));
						}
					}catch(Exception e){
						e.printStackTrace();
					}					

					if((ReferenceTable.HOSPITAL_CASH).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
					{
						addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
					}
					if(addOnBenefitsDTO.getParticulars().equalsIgnoreCase("Hospital Cash")){
						hospitalCashPayableAmount += addOnBenefitsDTO.getPayableAmount();
					}else{
						patientCarePayableAmount += addOnBenefitsDTO.getPayableAmount();
					}
				}
			}
		}

		if(null != this.addOnBenefitsPatientCareListnerTable)
		{
			if(null != this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() && !this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {
					if((ReferenceTable.PATIENT_CARE).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
					{
						addOnBenefitsPatientCareListnerTable.addBeanToList(addOnBenefitsDTO);
					}
				}
			}
		}
		hospitalCashPayableAmt = new TextField("Hospital Cash Payable Amount");
		hospitalCashPayableAmt.setValue(hospitalCashPayableAmount.toString());
		hospitalCashPayableAmt.setReadOnly(true);
		bean.setHospitalCashAmt(hospitalCashPayableAmount.doubleValue());

		patientCarePayableAmt = new TextField("Patient Care Payable Amount");
		patientCarePayableAmt.setValue(patientCarePayableAmount.toString());
		patientCarePayableAmt.setReadOnly(true);
		bean.setPatientCareAmt(patientCarePayableAmount.doubleValue());

		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(hospitalCashPayableAmt), new FormLayout(patientCarePayableAmt));
		horizontalLayout.setSpacing(true);

		otherInsApplicable = new OptionGroup("Other Insurer Settlement Applicable");
		otherInsApplicable.addItems(getReadioButtonOptions());
		otherInsApplicable.setItemCaption(true, "Yes");
		otherInsApplicable.setItemCaption(false, "No");
		otherInsApplicable.setStyleName("horizontal");

		//FormLayout otherinsureform = new FormLayout(otherInsApplicable);

		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			otherInsApplicable.setEnabled(false);
		} 

		otherInsApplicable.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 8226297582678969878L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					bean.getPreauthMedicalDecisionDetails().setOtherInsurerApplicable(true);
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						if(otherInsSettlementTab != null) {
							otherInsSettlementTab.addComponent(getOtherInsSettlementTab());
						}
					} else {
						if(otherInsTabLayout != null) {
							otherInsTabLayout.removeAllComponents();
						}
						if(otherInsSettlementTab != null) {
							otherInsSettlementTab.removeAllComponents();
						}
					}

				} else {
					bean.getPreauthMedicalDecisionDetails().setOtherInsurerApplicable(false);
					SHAUtils.resetOtherInsurerValues(bean);
					if(otherInsTabLayout != null) {
						otherInsTabLayout.removeAllComponents();
					}
					if(otherInsSettlementTab != null) {
						otherInsSettlementTab.removeAllComponents();
					}
					otherInsSettlementChange();

					if(bean.getIsReverseAllocation() && !bean.getIsReverseAllocationHappened() && (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						bean.setIsReverseAllocationHappened(true);
					}

					if(bean.getIsReverseAllocation() && !bean.getIsReverseAllocationHappened() && (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						if(bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null && !bean.getOtherInsHospSettlementCalcDTO().getPayableAmt().equals(bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt())) {
							bean.setIsReverseAllocationHappened(true);
						}

					}

					if(bean.getIsReverseAllocation() && bean.getIsReverseAllocationHappened()) {
						doReverseAllocationForTPA(SHAUtils.getDoubleValueFromString(bean.getAmountConsidered()));
					}

				}
				SHAUtils.setHospitalizationDetailsToDTOForFinancial(bean);
				if(hospitalizaionObj != null) {
					hospitalizaionObj.initView(bean);
					hospitalizaionObj.setPresenterString(SHAConstants.FINANCIAL);
				}
				SHAUtils.setRevisedPostHospitalizationDetailsToDTO(bean, "0");
				if(postHospitalizationObj != null) {
					postHospitalizationObj.initView(bean);
				}

				consolidatedTabChage();
			}
		});

		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Policy Payment Details");
		paymentDetailsLayout.setCaptionAsHtml(true);
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);


		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);

		if(null != bean.getDocumentReceivedFromId() && (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(bean.getDocumentReceivedFromId()))
		{
			btnPopulatePreviousAccntDetails.setEnabled(false);
		}


		getPaymentDetailsLayout();

		btnOk = new Button("OK");
		//Vaadin8-setImmediate() btnOk.setImmediate(true);
		btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnOk.setWidth("-1px");
		btnOk.setHeight("-10px");
		//btnOk.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnOk.setImmediate(true);

		btnCancel = new Button("CANCEL");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//	btnCancel.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);

		previousAccountDetailsLayout = new HorizontalLayout(btnOk,btnCancel);



		previousAccountDetailsTable.init("Previous Account Details", false, false);
		previousAccountDetailsTable.setPresenterString(SHAConstants.FINANCIAL);
		previousPaymentVerticalLayout = new VerticalLayout();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccountDetailsLayout, Alignment.TOP_CENTER);

		financialButtonObj =  financialButtonInstance.get();
		financialButtonObj.initView(this.bean, this.wizard);
		financialButtonObj.setBillingProcessPageObject(this);
		
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
			legalBillingUIObj.initView(bean,SHAConstants.CLAIM_FINANCIAL);

			legalBillingLayout = new VerticalLayout();
			legalBillingLayout.setCaption("Legal Billing");
			legalBillingLayout.setCaptionAsHtml(true);
			legalBillingLayout.addComponent(legalBillingUIObj);
			legalBillingLayout.setSpacing(true);
			legalBillingLayout.setMargin(true);
		}else
		{
			legalBillingLayout = new VerticalLayout();
			legalBillingLayout.setVisible(false);
		}
		

		wholeLayout = new VerticalLayout(builBillSummaryTabs(), addOnBenefitsListenerTableObj,  addOnBenefitsPatientCareListnerTable,legalBillingLayout, horizontalLayout,paymentDetailsLayout, financialButtonObj);
		if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null) {
			otherInsApplicable.setValue(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable());
			otherInsApplicable.select(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable());
			if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
				if(otherInsTabLayout != null) {
					otherInsTabLayout.removeAllComponents();
				}
				if(otherInsSettlementTab != null) {
					otherInsSettlementTab.removeAllComponents();
				}
				if(otherInsSettlementTab != null) {
					otherInsSettlementTab.addComponent(getOtherInsSettlementTab());
				}
			}
		}
		addListenerForBenefits();
		addPreviousPaymentPopupListener();
		setTableValues();
		if(consolidatedAmountObj !=null
				&& legalBillingUIObj !=null){
			 legalBillingUIObj.setawardAmount(consolidatedAmountObj.getTotalConsolidatedAmt());
		 }
		rechargeDummyField = new TextField();
		rechargeDummyField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				if(bean.getPostHospitalizaionFlag()) {
					//postHospitalizationObj.setTextField();
					SHAUtils.setPostHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
					postHospitalizationObj.initView(bean);
				}
				if(bean.getPreHospitalizaionFlag()){
					SHAUtils.setPreHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
					preHospitalizationObj.initView(bean);
				}
			}
		});

		return wholeLayout;
	}

	private TabSheet builBillSummaryTabs() {
		TabSheet billSummaryTab = new TabSheet();
		//Vaadin8-setImmediate() billSummaryTab.setImmediate(true);
		// previousClaimTab.setWidth("100.0%");
		// previousClaimTab.setHeight("100.0%");
		billSummaryTab.setSizeFull();
		billSummaryTab.setStyleName(ValoTheme.TABSHEET_FRAMED);

		TabSheet hospitalizationTab = getHospitalizationTab();
		billSummaryTab.setHeight("100.0%");
		billSummaryTab.addTab(hospitalizationTab, "Hospitalization", null);

		// tabSheet_2
		TabSheet preHospitalizationTab = getPreHospitalizationTab();
		billSummaryTab.addTab(preHospitalizationTab, "Pre-Hospitalization", null);

		TabSheet postHospitalizationTab = getPostHospitalizatonTab();
		billSummaryTab.addTab(postHospitalizationTab, "Post-Hospitalization", null);

		if(null != bean.getPreauthDataExtractionDetails().getDocAckknowledgement() &&
				(null != bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag() && 
				SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getOtherBenefitsFlag())))
		{
			if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				TabSheet otherBenefitCalculationTab = getOtherBenefitTab();
				billSummaryTab.addTab(otherBenefitCalculationTab,"Other Benefit",null);
			}
		}
		//			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
		/*if(false) {
>>>>>>> FHOwithproduction
				otherInsSettlementTab = new VerticalLayout();
				otherInsSettlementTab.setCaption("Other Insurer Settlement");
//				otherInsSettlementTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
//					private static final long serialVersionUID = 460697828791510201L;
		//
//					@Override
//					public void selectedTabChange(SelectedTabChangeEvent event) {
//						otherInsSettlementChange();
//					}
//				});
				billSummaryTab.addTab(otherInsSettlementTab, "Other Insurer Settlement", null);
			}*/

		VerticalLayout consolidatedAmountTab = getConsolidatedAmountTab();
		billSummaryTab.addTab(consolidatedAmountTab, "Consolidated Amount", null);

		billSummaryTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if(event.getTabSheet().getSelectedTab().getCaption() != null) {
					if(event.getTabSheet().getSelectedTab().getCaption().equalsIgnoreCase("Other Insurer Settlement")) {
						otherInsSettlementChange();
					} else if(event.getTabSheet().getSelectedTab().getCaption().equalsIgnoreCase("Consolidated Amount")) {
						consolidatedTabChage();
					}
				}

			}
		});

		return billSummaryTab;
	}

	private TabSheet getHospitalizationTab(){
		TabSheet hospitalizationTab = new TabSheet();
		hospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		hospitalizationTab.setWidth("100%");
		hospitalizationTab.setHeight("100%");
		hospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);

		hospitalizaionObj = hospitalizaionInstance.get();
		hospitalizaionObj.setPresenterString(SHAConstants.FINANCIAL);
		hospitalizaionObj.initView(this.bean);
		hospitalizaionObj.setPresenterString(SHAConstants.FINANCIAL);
		TextField field = new TextField("Amount Claimed (Hospitalisation)");
		field.setEnabled(false);
		field.setValue("");
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalizationClaimedAmount() != null) {
			field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalizationClaimedAmount().longValue()) );
		}

		//		otherInsurerHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Hosp)",
		//				"otherInsurerHospAmountClaimed", TextField.class);
		//		CSValidator hospValidator = new CSValidator();
		//		hospValidator.extend(otherInsurerHospAmt);
		//		hospValidator.setRegExp("^[0-9]*$");
		//		hospValidator.setPreventInvalidTyping(true);
		//		otherInsurerHospAmt.setNullRepresentation("");
		//		FormLayout formLayout = new FormLayout(otherInsurerHospAmt);
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field));
		horizontalLayout.setWidth("100%");
		//		horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
		VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, hospitalizaionObj);
		hospitalizationTab.addComponent(verticalLayout);

		return hospitalizationTab;
	}

	private TabSheet getPreHospitalizationTab(){
		TabSheet preHospitalizationTab = new TabSheet();
		preHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);
		preHospitalizationTab.setWidth("100%");
		preHospitalizationTab.setHeight("100%");
		preHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);

		otherInsurerPreHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Pre-Hosp)",
				"otherInsurerPreHospAmountClaimed", TextField.class);
		otherInsurerPreHospAmt.setValue(String.valueOf(SHAUtils.getDoubleValueFromString(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed()).intValue()) );
		SHAUtils.setPreHospitalizationDetailsToDTO(bean, otherInsurerPreHospAmt.getValue());
		CSValidator preHospValidator = new CSValidator();
		preHospValidator.extend(otherInsurerPreHospAmt);
		preHospValidator.setRegExp("^[0-9]*$");
		preHospValidator.setPreventInvalidTyping(true);
		otherInsurerPreHospAmt.setNullRepresentation("");
		otherInsurerPreHospAmt.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2777617364408974206L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(bean.getPreHospitalizaionFlag()) {
					SHAUtils.setPreHospitalizationDetailsToDTO(bean, otherInsurerPreHospAmt.getValue());
					preHospitalizationObj.initView(bean);
				}


			}
		});

		preHospitalizationObj = preHospitalizationInstance.get();
		preHospitalizationObj.initView(this.bean);

		TextField field = new TextField("Amount Claimed (Pre-Hosp)");
		field.setEnabled(false);
		field.setValue("");
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPreHospitalizationClaimedAmount() != null) {
			field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPreHospitalizationClaimedAmount().longValue()) );
		}


		FormLayout formLayout = new FormLayout(otherInsurerPreHospAmt);
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field), formLayout );
		horizontalLayout.setWidth("100%");
		horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
		VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, preHospitalizationObj);
		verticalLayout.setSpacing(true);

		preHospitalizationTab.addComponent(verticalLayout);

		return preHospitalizationTab;

	}

	private TabSheet getPostHospitalizatonTab(){
		TabSheet postHospitalizationTab = new TabSheet();
		postHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);
		postHospitalizationTab.setWidth("100%");
		postHospitalizationTab.setHeight("100%");
		postHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);

		otherInsurerPostHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Post-Hosp)",
				"otherInsurerPostHospAmountClaimed", TextField.class);
		otherInsurerPostHospAmt.setValue(String.valueOf(SHAUtils.getDoubleValueFromString(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed()).intValue()) );
		SHAUtils.setPostHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
		CSValidator postHospValidator = new CSValidator();
		postHospValidator.extend(otherInsurerPostHospAmt);
		postHospValidator.setRegExp("^[0-9]*$");
		postHospValidator.setPreventInvalidTyping(true);
		otherInsurerPostHospAmt.setNullRepresentation("");
		otherInsurerPostHospAmt.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2777617364408974206L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(bean.getPostHospitalizaionFlag()) {
					SHAUtils.setPostHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
					postHospitalizationObj.initView(bean);
				}


			}
		});

		postHospitalizationObj = postHospitalizationInstance.get();
		postHospitalizationObj.initView(this.bean);
		TextField field = new TextField("Amount Claimed (Post-Hosp)");
		field.setEnabled(false);
		field.setValue("");
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPostHospitalizationClaimedAmount() != null) {
			field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPostHospitalizationClaimedAmount().longValue()) );
		}


		FormLayout formLayout = new FormLayout(otherInsurerPostHospAmt);
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field), formLayout);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
		VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, postHospitalizationObj);
		verticalLayout.setSpacing(true);

		postHospitalizationTab.addComponent(verticalLayout);

		return postHospitalizationTab;
	}

	private TabSheet getOtherBenefitTab(){
		TabSheet hospitalizationTab = new TabSheet();
		hospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		hospitalizationTab.setWidth("100%");
		hospitalizationTab.setHeight("100%");
		hospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);

		otherBenefitCalculationObj = otherBenefitCalculationInstance.get();
		otherBenefitCalculationObj.initView(this.bean);

		addOtherBenefitsListener();

		//		otherInsurerHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Hosp)",
		//				"otherInsurerHospAmountClaimed", TextField.class);
		//		CSValidator hospValidator = new CSValidator();
		//		hospValidator.extend(otherInsurerHospAmt);
		//		hospValidator.setRegExp("^[0-9]*$");
		//		hospValidator.setPreventInvalidTyping(true);
		//		otherInsurerHospAmt.setNullRepresentation("");
		//		FormLayout formLayout = new FormLayout(otherInsurerHospAmt);

		VerticalLayout verticalLayout = new VerticalLayout(otherBenefitCalculationObj);
		verticalLayout.setSpacing(true);
		hospitalizationTab.addComponent(verticalLayout);

		return hospitalizationTab;
	}


	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null 
				&& !((ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {

			if(optPaymentMode.getValue() != null 
					&& !(Boolean)optPaymentMode.getValue()
					&& bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
				
				
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setValue(dto.getAccNumber());
				txtAccntNo.setReadOnly(true);
				
				txtAccountPref.setReadOnly(false);
				txtAccountPref.setValue(dto.getAccPreference());
				txtAccountPref.setReadOnly(true);
				
				txtAccType.setReadOnly(false);
				txtAccType.setValue(dto.getAccType());
				txtAccType.setReadOnly(true);
				
				txtNameAsPerBank.setReadOnly(false);
				txtNameAsPerBank.setValue(dto.getPayeeName());
				txtNameAsPerBank.setReadOnly(true);
				this.bean.setDto(dto);
				
				txtBankName.setReadOnly(false);
				txtBankName.setValue(dto.getBankName());
				txtBankName.setReadOnly(true);
				
				txtBranch.setReadOnly(false);
				txtBranch.setValue(dto.getBranchName());
				txtBranch.setReadOnly(true);
				
				txtCity.setReadOnly(false);
				txtCity.setValue(dto.getCity());
				txtCity.setReadOnly(true);
				
				txtNameAsPerBank.setReadOnly(false);
				txtNameAsPerBank.setValue(dto.getPayeeName()); 
				txtNameAsPerBank.setReadOnly(true);
				
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setValue(dto.getIfscCode());
				txtIfscCode.setReadOnly(true);
				
				this.bean.getPreauthDataExtractionDetails().setAccountNo(dto.getAccNumber());
				this.bean.getPreauthDataExtractionDetails().setAccountPref(dto.getAccPreference());
				this.bean.getPreauthDataExtractionDetails().setAccType(dto.getAccType());
				this.bean.getPreauthDataExtractionDetails().setNameAsPerBank(dto.getPayeeName());
				this.bean.getPreauthDataExtractionDetails().setBankName(dto.getBankName());
				this.bean.getPreauthDataExtractionDetails().setBranch(dto.getBranchName());
				this.bean.getPreauthDataExtractionDetails().setCity(dto.getCity());
				this.bean.getPreauthDataExtractionDetails().setIfscCode(dto.getIfscCode());
				
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

	public void generateButton(Integer clickedButton, Object dropDownValues) {
		Long holddefaultStatus = null;
		this.bean.setPreauthHoldStatusKey(holddefaultStatus);
		this.bean.setStageKey(ReferenceTable.FINANCIAL_STAGE);
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {	
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			bean.setUniquePremiumAmount(uniqueInstallmentAmount.doubleValue());
		}
		//cr2019184
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			Integer policyInstallmentAmount = PremiaService.getInstance()
					.getPolicyInstallmentAmount(bean.getNewIntimationDTO().getPolicy()
							.getPolicyNumber());
			bean.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
					.doubleValue());
		}
		
		switch (clickedButton) {
		case 1: 
			//			
			//		 this.financialButtonObj.buildSpecialistLayout(dropDownValues);
			//		 this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);
			//		 break;
			//			if(validatePage()){
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			specialistWindow.init(bean);
			final BeanItemContainer<SelectValue> masterValueByReference;
			if (null != bean.getPreauthDataExtractionDetails().getNatureOfTreatment()){
				masterValueByReference = masterService.getMasterValueByReferenceForNonAllopathic((ReferenceTable.SPECIALIST_TYPE),this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue());
			}else{
				masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
			}
			specialistWindow.buildSpecialityLayout(dropDownValues,fileViewUI, masterValueByReference);
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

			//				this.claimRequestFileUploadUI.init(bean, wizard);
			//				this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);

			// this.medicalDecisionTableObj.setVisibleApproveFields(false);
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);
			//				}
			break;

		case 2:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			if(!ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getStatusKey())){

				ViewFVRDTO trgptsDto = null;
				List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
				for(int i = 1; i<=5;i++){
					trgptsDto = new ViewFVRDTO();
					trgptsDto.setRemarks("");
					trgptsList.add(trgptsDto);
				}
				this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
			}

			this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS);
			this.financialButtonObj.buildInitiateFieldVisit(dropDownValues);

			break;

		case 3: 
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj.buildInitiateInvestigation(dropDownValues);
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS);
			break;
		case 4:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj
			.buildReferCoordinatorLayout(dropDownValues);
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS);
			break;
		case 5:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj.buildReferToMedicalApproverLayout();;
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
			break;
		case 6:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj.buildReferToBilling();
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_BILLING);
			break;

		case 7:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj.buildQueryLayout();
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_QUERY_STATUS);
			break;
		case 8:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
			if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
					bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
				this.financialButtonObj.buildRejectLayout(dropDownValues,true);

			}else{

				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId() != null && 
						bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey() != null && 
						ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getKey())){
					//						bean.getHospAmountAlreadyPaid() != null && bean.getHospAmountAlreadyPaid().intValue() != 0){
					BeanItemContainer<SelectValue> setlRejCateg = masterService.getSettledRejectionCategory();
					this.financialButtonObj.buildRejectLayout(setlRejCateg,false);
				}
				else{
					this.financialButtonObj.buildRejectLayout(dropDownValues,false);
				}	

				//this.financialButtonObj.buildRejectLayout(dropDownValues,false);				
			}

			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REJECT_STATUS);
			break;
		case 9:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			WeakHashMap<Integer, Object> valiateApprovedAmount = isValiateApprovedAmount();
			Boolean isValid = (Boolean)valiateApprovedAmount.get(1);
			if(! isValid){
				if(this.bean.getIsPending() && this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					fireViewEvent(FinancialWizardPresenter.FINANCIAL_64VB_CHQUEQ_STATUS, null);
				}else{
					if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
						checkForPremiumAndConsolidateAmt();
						if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
							//this.financialButtonObj.disableOnlyApprove(false);- Commented due to issue of enabling approve button while changing hosp discount
							SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
							bean.setShouldDetectPremium(false);
						} else {
							bean.setShouldDetectPremium(true);
							this.financialButtonObj.generateFieldsForApproval();
						}
					}	
					
				else if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (bean.getHospitalizaionFlag())) {

						Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						bean.setUniquePremiumAmount(uniqueInstallmentAmount.doubleValue());
						Double premiumAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt() + uniqueInstallmentAmount.doubleValue();
						if(bean.getHospitalizationCalculationDTO().getNetPayableAmt() < premiumAmt.intValue()) {
							bean.setShouldDetectPremium(false);
							alertMessageUniquePremium();
						} else {
							bean.setShouldDetectPremium(true);
							this.financialButtonObj.generateFieldsForApproval();
						}
					} else {
						bean.setShouldDetectPremium(true);
						this.financialButtonObj.generateFieldsForApproval();
					}
					//			this.financialButtonObj.generateFieldsForApproval();
					this.bean.setStatusKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);
					this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
				}
			}else{
				String msg = (String)valiateApprovedAmount.get(2);
				fireViewEvent(FinancialWizardPresenter.BILLING_APPROVED_AMOUNT_VALIDATION, msg);
			}
			break;
		case 10:
			this.financialButtonObj.generateCancelRODLayout(dropDownValues);
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_CANCEL_ROD);
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			break;
		case 11:
			this.bean.setPreauthHoldStatusKey(holddefaultStatus);	
			this.financialButtonObj.buildReferToBillEntryLayout();
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY);
			break;
		case 12:
			this.bean.setPreauthHoldStatusKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			this.financialButtonObj.buildHoldLayout();
			this.bean.setStatusKey(bean.getOldStatusKey());
			break;
		default:
			break;
		}
	}

	//	protected void showOrHideValidation(Boolean isVisible) {
	//		for (Component component : mandatoryFields) {
	//			AbstractField<?> field = (AbstractField<?>) component;
	//			field.setRequired(!isVisible);
	//			field.setValidationVisible(isVisible);
	//		}
	//	}


	public Boolean alertMessageForPaymentAvailable() {
		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PAYMENT_AVAIL_MESSAGE + "</b>",
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
				.createAlertBox(SHAConstants.PAYMENT_AVAIL_MESSAGE, buttonsNamewithType);
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

	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();	


		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}

		if (!this.paymentbinder.isValid()) {

			for (Field<?> field : this.paymentbinder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}



		if(!this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)){
			if(this.financialButtonObj != null && !this.financialButtonObj.isValid()) {
				hasError = true;
				List<String> errors = this.financialButtonObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		if(this.bean.getDocumentReceivedFromId() != null 
				&& !this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
					
			if(!(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId())))) {
				
						if(null != txtPayModeChangeReason && ((txtPayModeChangeReason.getValue() == null) || (txtPayModeChangeReason.getValue().equals("")))){
							
							if(null != bean.getPaymentModeId() && !(bean.getPaymentModeId().equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))){				
							
								hasError = true;
								eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
							}
							
						}
						
						if(cmbPayeeName != null){
							SelectValue selected = (SelectValue)cmbPayeeName.getValue();
							if(existingPayeeName != null && selected != null 
									&& ! existingPayeeName.getValue().equalsIgnoreCase(selected.getValue())){
								if(txtReasonForChange != null && txtReasonForChange.getValue() == null || txtReasonForChange.getValue().isEmpty()){
									hasError = true;
									eMsg.append("Please Enter Reason for changing payee name</br>");
								}
							}
						}
							
						if(null != bean.getPreauthDataExtractionDetails().getPaymentModeFlag() &&
								(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))){
						
							if(txtPayableAt != null && (null == txtPayableAt.getValue() || ("").equalsIgnoreCase(txtPayableAt.getValue()))){
								
								hasError = true;
								eMsg.append("Please Enter Payable At</br>");
							}
						}
						
						if(optPaymentMode.getValue() != null 
								&& !(Boolean)optPaymentMode.getValue()
								&& bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
								&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())) {
							if(txtAccountPref != null && txtAccountPref.getValue() == null || txtAccountPref.getValue().isEmpty()){
								hasError = true;
								eMsg.append("Please Select Account Preference</br>");
							}
						}
					
				
						if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
							&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
								if(txtAccntNo != null && txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty()){
									hasError = true;
									eMsg.append("Please Enter Account Number</br>");
								}
						
								if(txtIfscCode != null && txtIfscCode.getValue() == null || txtIfscCode.getValue().isEmpty()){
									hasError = true;
									eMsg.append("Please Enter IFSC Code</br>");
								}
						}

				}
		} else {
			if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
					&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
				if(txtAccntNo != null && txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty()){
					hasError = true;
					eMsg.append("Please Enter Account Number</br>");
				}
				
				if(txtIfscCode != null && txtIfscCode.getValue() == null || txtIfscCode.getValue().isEmpty()){
					hasError = true;
					eMsg.append("Please Enter IFSC Code</br>");
				}
			}		
		
			/*if(txtPanNo != null){
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
					
					//String totalConsolidatedAmt = consolidatedAmountObj.getTotalConsolidatedAmt();
		
					//String panNumber = txtPanNo.getValue();
					
	//				if(totalConsolidatedAmt != null && ((panNumber == null) || (panNumber != null && panNumber.equals("")))){
	//					Double totalAmt = Double.valueOf(totalConsolidatedAmt);
	//					if(SHAConstants.FINANCIAL_APPROVED_AMT_FOR_PAN_NUMBER <= totalAmt){
	//						hasError = true;
	//						eMsg += "Please Enter Pan Number</br>";
	//					}
	//				}
					
	//				if(txtPanNo.getValue() != null && ! txtPanNo.getValue().equalsIgnoreCase("")){
	//					String value = txtPanNo.getValue();
	//					if(value.length() != 10){
	//						hasError = true;
	//						eMsg += "PAN number should be 10 digit value</br>";
	//					}
	//				}
				}
			}*/
	
			if(null != txtPayModeChangeReason && ((txtPayModeChangeReason.getValue() == null) || (txtPayModeChangeReason.getValue().equals("")))){
				
				if(null != bean.getPaymentModeId() && !(bean.getPaymentModeId().equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))){				
				
					hasError = true;
					eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
				}
				
			}
		
			if(null != bean.getPreauthDataExtractionDetails().getPaymentModeFlag() &&
					(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))){
			
				if(txtPayableAt != null && (null == txtPayableAt.getValue() || ("").equalsIgnoreCase(txtPayableAt.getValue()))){
					
					hasError = true;
					eMsg.append("Please Enter Payable At</br>");
				}
			}
		}

		if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && bean.getIsPending() && bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			hasError = true;
			eMsg.append("Cheque Status is Pending. Hence this Reimbursement Can not be submitted.");
		}
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
				checkForPremiumAndConsolidateAmt();
				if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
					hasError = true;
					eMsg.append(bean.getPolicyInstalmentDetailsMsg());
				}
			}
		}
         if(bean.getPreauthHoldStatusKey() != null && bean.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)) {
        	 if(this.financialButtonObj != null && this.financialButtonObj.validateHoldRemarks()) {
        	 hasError = true;
 			eMsg.append("Please enter the HOLD Remarks."); 
        	 }
         }
		if(!hasError) {
			if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
				bindValues();
				Integer amt = 0;
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest() && bean.getHospitalizaionFlag() != null && !bean.getHospitalizaionFlag()) {
					amt = ((bean.getHospitalizationCalculationDTO().getBalanceToBePaid() != null ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() : 0));
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
						amt = bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() : 0;
					}
				} else {
					if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
						if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
							amt = ((bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0));
							if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
								amt = bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() : 0;
							}
						} else {
							amt = ((bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() != null ? bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt() : 0) );
						}
					}
				}
				if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
						amt += (bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() : 0);
						amt += (bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt() : 0);
					} else {
						amt += (bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
						amt += (bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
					}

				} else {
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
						amt += (bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt() : 0);
						amt += (bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt() != null ? bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt() : 0);
					} else {
						amt += (bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
						amt += (bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
					}
					//						if(bean.getHospitalizaionFlag() != null && !bean.getHospitalizaionFlag()) {
					//							amt += (bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
					//							amt += (bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ? bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);
					//						}
				}

				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
					amt += bean.getConsolidatedAmtDTO().getLumpusmPayableAmt() != null ? bean.getConsolidatedAmtDTO().getLumpusmPayableAmt() : 0;
				}

				if(bean.getOtherBenefitsFlag() != null && bean.getOtherBenefitsFlag()) {
					amt += bean.getConsolidatedAmtDTO().getOtherBenefitAmt() != null ? bean.getConsolidatedAmtDTO().getOtherBenefitAmt() : 0;
				}
				if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					if(bean.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null && bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCash()) {
						amt += bean.getReceiptOfDocumentsDTO().getTotalClaimedAmount() != null ? bean.getReceiptOfDocumentsDTO().getTotalClaimedAmount().intValue() : 0;
					}
				}

				Integer balancePremiumAmt = this.bean.getHospitalizationCalculationDTO().getBalancePremiumAmt();

				if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					
					if(amt <= 0){
						hasError = true;
						eMsg.append("Approved Amount is Zero. Hence this ROD can not be Apporved.").append("</br>");
						if(this.financialButtonObj != null) {
							this.financialButtonObj.disableApprove(true);
						}
					}
					
				}// bypassing zero approve Amount for installmen CR
				else if(this.bean.getIsPremiumInstAmtEql()){
					balancePremiumAmt = this.bean.getHospitalizationCalculationDTO().getBalancePremiumAmt();
				}else if(amt <= 0 && (balancePremiumAmt != null && balancePremiumAmt <= 0)) {
					hasError = true;
					eMsg.append("Approved Amount is Zero. Hence this ROD can not be Apporved.").append("</br>");
					if(this.financialButtonObj != null) {
						this.financialButtonObj.disableApprove(true);
					}
				}//IMSSUPPOR-26833
				else if(consolidatedAmountObj != null && consolidatedAmountObj.getTotalConsolidatedAmt() != null &&  (SHAUtils.getIntegerFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt()) <= 0)){
					hasError = true;
					eMsg.append("Approved Amount is Zero. Hence this ROD can not be Apporved.").append("</br>");
					if(this.financialButtonObj != null) {
						this.financialButtonObj.disableApprove(true);
					}

				} else {
					if(this.financialButtonObj != null) {
						this.financialButtonObj.disableApprove(false);
					}
				}


			}
		}

		/*if(bean.getHospitalizaionFlag() != null && bean.getHospitalizaionFlag()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
			if(validateApprovedAmount){
				hasError = true;
				eMsg.append("hospitalisation amount is Zero </br>");
			}
		}
		if(bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					eMsg.append("Partial hospitalisation amount is Zero </br>");
				}
			}

		}
		if(bean.getPreHospitalizaionFlag() != null && bean.getPreHospitalizaionFlag()){
			String hospAmt = preHospitalizationObj.getPreHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					eMsg.append("Pre hospitalisation amount is Zero </br>");
				}
			}
		}
		if(bean.getPostHospitalizaionFlag() != null && bean.getPostHospitalizaionFlag()){
			String hospAmt = postHospitalizationObj.getPostHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					eMsg.append("Post hospitalisation amount is Zero </br>");
				}
			}
		}
		if(bean.getIsHospitalizationRepeat() != null && bean.getIsHospitalizationRepeat()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					eMsg.append("Hospitalisation Repeat amount is Zero </br>");
				}
			}
		}*/

		if(!hasError && bean.getIsPaymentAvailable()) {
			hasError = true;
			eMsg.append(SHAConstants.PAYMENT_AVAIL_MESSAGE).append("</br>");
		}

		/*if(bean.getModeOfReceipt() != null && bean.getStatusKey() != null 
				//Below condition added for additional changes in Physical Doc
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)
				&& bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) 
				&& (bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)
				|| bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_EMAIL))){
			BPMClientContext clientContext = new BPMClientContext();
			String documentGateWayUrl = clientContext.getDocumentGateWayUrl();
			if(documentGateWayUrl.equalsIgnoreCase("F")){
				if(bean.getCheckerVerified() == null){
					hasError = true;
					eMsg.append("Physical document is not verified");
				}
			}
		}*/

		if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
			hasError = true;
			eMsg.append("Please Verify View Policy Schedule Button.").append("</br>");
		}
		
		if(!this.bean.getIsBusinessProfileClicked()){
			hasError = true;
			eMsg.append("Please Verify View Mini Business Profile.</br>");
		}
		if(!this.bean.getIsInsuredChannedNameClicked()){
			hasError = true;
			eMsg.append("Please Verify Insured/Channel Name Button.</br>");
		}
				

		/*if(bean.getPreauthDataExtractionDetails().getPaymentModeFlag() == ReferenceTable.PAYMENT_MODE_BANK_TRANSFER && !this.bean.getVerificationClicked()){
			hasError = true;
			eMsg.append("Please Verify Account Details.</br>");
		}*/
		/*if(consolidatedAmountObj != null && consolidatedAmountObj.getTotalConsolidatedAmt() != null && this.bean.getBalanceSI() != null && this.bean.getHospitalizationCalculationDTO() != null && this.bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null){
			String totalConsolidatedAmt = consolidatedAmountObj.getTotalConsolidatedAmt();
			Double totalAmt = Double.valueOf(totalConsolidatedAmt) + this.bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
			if(totalAmt > this.bean.getBalanceSI()){
				hasError = true;
				eMsg.append("Sum Insured Exhausted.").append("</br>");
			}
		}*/
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
				ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {

				if(bean.getConsolidatedAmtDTO().getOtherBenefitAmt() != null && bean.getPreauthMedicalDecisionDetails().getOtherBenefitPreauthApprAmt() != null){
					if(bean.getConsolidatedAmtDTO().getOtherBenefitAmt().intValue() > bean.getPreauthMedicalDecisionDetails().getOtherBenefitPreauthApprAmt()){
						hasError = true;
						eMsg.append("The Other benefits amount can not exceed preauth approved under other benefits.").append("</br>");
					}
				}
			}
		}

		if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null 
				&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& (bean.getNewIntimationDTO().getNomineeList() == null || bean.getNewIntimationDTO().getNomineeList().isEmpty())
				&& bean.getLegalHeirDTOList() != null 
				&& !bean.getLegalHeirDTOList().isEmpty()
				&& bean.getStatusKey() != null 
				&& bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {

			for (LegalHeirDTO legalHeir : bean.getLegalHeirDTOList()) {
				if((legalHeir.getDeleteLegalHeir() == null || (legalHeir.getDeleteLegalHeir() != null && SHAConstants.N_FLAG.equalsIgnoreCase(legalHeir.getDeleteLegalHeir()))) 
						&& legalHeir.getDocumentToken() == null){
					hasError = true;
					eMsg.append("Legal Heir Document Not Available.</br>Legal Heir Document Mandatory For Approval.").append("</br>");
					break;
				}
			}
			//fireViewEvent(FinancialHospitalizationPagePresenter.VERIFY_LEGAL_HEIR_DOC_UPLOADED, bean);
		
			/*if(!bean.getClaimDTO().getLegalHeirDocAvailable()) {
				hasError = true;
				eMsg.append("Legal Heir Document Not Available.</br>Legal Heir Document Mandatory For Approval.").append("</br>");				
			}*/
		}


		if(! hasError && ! this.bean.getIsRechargePopUpOpened() && ! this.bean.getIsModernSublimitSelected() && ! this.bean.getIsBalSIForSublimitCardicSelected()){

			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null &&
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y")){
				Integer availableSumInsuredAftHosp = SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getAvailableSumInsuredAftHosp());
				Integer netEligibleAmt = SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getEligibleAmount());
				Double balanceSI = this.bean.getBalanceSI();
				if(netEligibleAmt > availableSumInsuredAftHosp){


					if(ReferenceTable.getSuperSurplusKeys().equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && 
							bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
						SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
						if(! (sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null && sectionDetailsDTO.getCover().getValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE))){
							fireViewEvent(FinancialHospitalizationPagePresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
							Double balanceSI2 = this.bean.getBalanceSI();
							Double rechargedAmount = balanceSI2 - balanceSI;
							Integer balanceSIAftHosp = bean.getBalanceSIAftHosp();
							if(rechargedAmount > 0){
								bean.setBalanceSIAftHosp(balanceSIAftHosp+rechargedAmount.intValue());
							}
							getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
							rechargeDummyField.setValue(rechargedAmount.toString());
							this.bean.setIsRechargePopUpOpened(true);
							return false;
						}
					}else{
						fireViewEvent(FinancialHospitalizationPagePresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
						Double balanceSI2 = this.bean.getBalanceSI();
						Double rechargedAmount = balanceSI2 - balanceSI;
						Integer balanceSIAftHosp = bean.getBalanceSIAftHosp();
						if(rechargedAmount > 0){
							bean.setBalanceSIAftHosp(balanceSIAftHosp+rechargedAmount.intValue());
						}
						getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
						rechargeDummyField.setValue(rechargedAmount.toString());
						this.bean.setIsRechargePopUpOpened(true);
						return false;
					}



				}else{
					Integer availableSumInsuredAftHosp1 = SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getAvailableSumInsuredAftHosp());
					Integer netEligibleAmt1 = SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getEligibleAmount());

					if(netEligibleAmt1 > availableSumInsuredAftHosp1){
						if(ReferenceTable.getSuperSurplusKeys().equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
							if(! (sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null && sectionDetailsDTO.getCover().getValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE))){
								fireViewEvent(FinancialHospitalizationPagePresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
								Double balanceSI2 = this.bean.getBalanceSI();
								Double rechargedAmount = balanceSI2 - balanceSI;
								Integer balanceSIAftHosp = bean.getBalanceSIAftHosp();
								if(rechargedAmount > 0){
									bean.setBalanceSIAftHosp(balanceSIAftHosp+rechargedAmount.intValue());
								}
								getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
								rechargeDummyField.setValue(rechargedAmount.toString());
								this.bean.setIsRechargePopUpOpened(true);
								return false;
							}
						}else{
							fireViewEvent(FinancialHospitalizationPagePresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
							Double balanceSI2 = this.bean.getBalanceSI();
							Double rechargedAmount = balanceSI2 - balanceSI;
							Integer balanceSIAftHosp = bean.getBalanceSIAftHosp();
							if(rechargedAmount > 0){
								bean.setBalanceSIAftHosp(balanceSIAftHosp+rechargedAmount.intValue());
							}
							getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
							rechargeDummyField.setValue(rechargedAmount.toString());
							this.bean.setIsRechargePopUpOpened(true);
							return false;
						}
					}
				}

			}
		}

		if((bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) 
				&& (bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) && (bean.getHospitalizationCalculationDTO().getHospitalDiscount() == null)) {
			hasError = true;
			eMsg.append("Please Enter Hospital Discount Value").append("</br>"); 
		}
		 if(bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null &&  bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0 && 
					bean.getNetworkHospitalDiscount() != null && bean.getNetworkHospitalDiscount() > 0d){
				 hasError = true;
					eMsg.append("Network Hospital Discount has been entered in Cashless stage and is available in Bill Review section. Please enter only “0” to Proceed further").append("</br>"); 
			 }
		
		if(legalBillingUIObj != null){		
			String errmsg = legalBillingUIObj.isValid();
			if(errmsg !=null){
				hasError = true;
				eMsg.append(errmsg);
			}//IMSSUPPOR-32607 changes done for this support fix
			else if(legalBillingUIObj.getinterestApplicable() && legalBillingUIObj.getPanDetails()) {
				if(txtPanNo.getValue() == null || txtPanNo.getValue().isEmpty()){
					hasError = true;
					eMsg.append("Please Enter Pan Number For Legal Settlement").append("</br>");
				}	
			}			
		}

		if (hasError) {
			setRequired(true);
			/*	Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
				this.paymentbinder.commit();
				bindValues();
				consolidatedTabChage();
				bean.setHospDiscountAmount(bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null ? bean.getHospitalizationCalculationDTO().getHospitalDiscount().doubleValue() : 0d);
				if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
						//IMSSUPPOR-29809 - Allow payable to insured if partial rod otherwise payable to insured is zero - Partial settled while doing hospitalisation rod already paid to insured added and alert is showing
						if(bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0 && !bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + 
								(bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0) + (bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag() ? bean.getPayableToInsAmt():0)) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag() ? bean.getPayableToInsAmt():0)) )) {
							bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
							bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
							bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);

							if(bean.getIsReverseAllocationHappened() && (ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								Double plusAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0);
								if((plusAmt).equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0) + bean.getPayableToInsAmt()) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0) + bean.getPayableToInsAmt()) )) {
									bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
									bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
									bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);
									return true;
								}
							}
							//cr2019184
							if(bean.getIsReverseAllocationHappened() && (bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG))) {
								Double plusAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + (bean.getShouldDetectPremium() ? bean.getPolicyInstalmentPremiumAmt() : 0);
								if((plusAmt).equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? bean.getPolicyInstalmentPremiumAmt() : 0) + bean.getPayableToInsAmt()) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getShouldDetectPremium() ? bean.getPolicyInstalmentPremiumAmt() : 0) + bean.getPayableToInsAmt()) )) {
									bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
									bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
									bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);
									return true;
								}
							}


							//							if(!(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getHospitalizaionFlag() && bean.getShouldDetectPremium())) {
							if(bean.getIsReconsiderationRequest()) {
								return doReverseAllocation((bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt().intValue() : bean.getUniquePremiumAmount().intValue()) : 0)) + (bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag() ? bean.getPayableToInsAmt():0));
							} else {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")){
									if(bean.getPreauthMedicalDecisionDetails().getHospitalDiscountReverseAllocatedAmt() > 0){
										if(bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0 && !bean.getPreauthMedicalDecisionDetails().getHospitalDiscountReverseAllocatedAmt().equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0) + bean.getPayableToInsAmt()) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + bean.getPayableToInsAmt()) )) {
											return doReverseAllocation(((bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0)) /*+ bean.getPayableToInsAmt()*/));
										}
									}else{
										Double amt = ((bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0)) /*+ bean.getPayableToInsAmt()*/);
										bean.getPreauthMedicalDecisionDetails().setHospitalDiscountReverseAllocatedAmt(amt);
										return doReverseAllocation(((bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0)) /*+ bean.getPayableToInsAmt()*/));
									}
								}else{
									return doReverseAllocation(((bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? ((bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) ? bean.getPolicyInstalmentPremiumAmt() : bean.getUniquePremiumAmount()) : 0)) /*+ bean.getPayableToInsAmt()*/));
								}
							}
							//							}
							//							return doReverseAllocation(bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + bean.getPayableToInsAmt());
						}
					} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
							if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
								if(!bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().equals(otherInsurerHospObj != null ? SHAUtils.getDoubleFromStringWithComma(otherInsurerHospObj.getPayableAmount()) : 0d)) {
									bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
									bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
									bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);
									if(bean.getIsReconsiderationRequest()) {
										return doReverseAllocation(bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + bean.getPayableToInsAmt());
									}
									return doReverseAllocation(SHAUtils.getDoubleFromStringWithComma(otherInsurerHospObj.getPayableAmount()));
								}
							} 
						} 
						//						else if(bean.getIsReverseAllocation() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat())) {
						//							if(!SHAUtils.getIntegerFromStringWithComma(bean.getReverseAmountConsidered()).equals(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() :  bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) ) {
						//								bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
						//								bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
						//								if((bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() :  bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()).equals(SHAUtils.getIntegerFromStringWithComma(bean.getAmountConsidered()))) {
						//									bean.setIsReverseAllocation(false);
						//								}
						//								
						//								return doReverseAllocation(SHAUtils.getDoubleFromStringWithComma(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() != null ? String.valueOf(bean.getHospitalizationCalculationDTO().getBalanceToBePaid()) : "0" : String.valueOf(bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) ));
						//							}
						//							
						//						}
					}
				}



			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
			bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
			bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);
			/**
			 * If add on benefits is available, then the amount payable of benefits needs to be added
			 * along with approved amt. This value is saved in APPROVED_AMOUNT column of claimpayment table.
			 * */
			Double initialApprovedAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY) && bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				initialApprovedAmt = bean.getUniqueDeductedAmount();
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(initialApprovedAmt);
			}//cr2019184
			else if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				initialApprovedAmt = bean.getUniqueDeductedAmount();//Using same variable for unique and other installment policies
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(initialApprovedAmt);
			}
			ConsolidatedAmountDetailsDTO detailsDTO = bean.getConsolidatedAmtDTO();
			if(null != detailsDTO && null != detailsDTO.getAddonBenefitAmt() && null != initialApprovedAmt)
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(initialApprovedAmt + detailsDTO.getAddonBenefitAmt());

			if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if(otherBenefitCalculationObj != null){
					try{
						otherBenefitCalculationObj.setOtherBenefitsValues();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

			setUserLimitAmount();
			if(legalBillingUIObj != null){
				bean.setLegalBillingDTO(legalBillingUIObj.getvalue());
				if(legalBillingUIObj.getPanDetails()){
					if(bean.getLegalBillingDTO() !=null && bean.getLegalBillingDTO().getPanNo() == null){
						bean.getLegalBillingDTO().setPanNo(txtPanNo.getValue());
					}
				}	
			}
			return true;
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

	private void bindValues() {
		if(this.hospitalizaionObj != null) {
			this.hospitalizaionObj.isValid();
		}
		if(this.otherInsurerHospObj != null) {
			this.otherInsurerHospObj.isValid();
		}
		if(this.otherInsurerPreHospObj != null) {
			this.otherInsurerPreHospObj.isValid();
		}
		if(this.otherInsurerPostHospObj != null) {
			this.otherInsurerPostHospObj.isValid();
		}
	}

	private boolean doReverseAllocation(final Double reverseAllocatedAmt) {/*
		Button popuupOkBtn = new Button("Ok");

		Label label = new Label("<b style = 'color:red'>Hospitalisation Approved amount is lesser than the Eligible amount. Please do Reverse allocation.</b>", ContentMode.HTML);

		VerticalLayout popupLayout = new VerticalLayout(label, popuupOkBtn);
		popupLayout.setComponentAlignment(popuupOkBtn, Alignment.BOTTOM_CENTER);
		popupLayout.setSpacing(true);
		final Window popupDialog = new Window();
		popupDialog.setCaption("Errors");
		popupDialog.setClosable(true);
		popupDialog.setContent(popupLayout);
		popupDialog.setResizable(false);
		popupDialog.setModal(true);
		UI.getCurrent().addWindow(popupDialog);
//						popupDialog.show(getUI().getCurrent(), null, true);

		popuupOkBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popupDialog.close();
				bean.setIsReverseAllocation(true);
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmt);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);

				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
			    layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.center();
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
//								dialog.show(getUI().getCurrent(), null, true);

				okButton.addClickListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(true);
							dialog.close();
						}
					}
				});
			}
		});
		return false;
	 */


		String label="Hospitalisation Approved amount is lesser than the Eligible amount.<br> Please do Reverse allocation.";
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(label.toString(), buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setIsReverseAllocation(true);
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmt);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);

				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
				layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.center();
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
				//								dialog.show(getUI().getCurrent(), null, true);

				okButton.addClickListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(true);
							dialog.close();
						}
					}
				});
			}
		});
		return false;

	}

	private boolean doReverseAllocationForTPA(final Double reverseAllocatedAmt) {/*
		Button popuupOkBtn = new Button("Ok");
		bean.setIsReverseAllocation(false);
		Label label = new Label("<b style = 'color:red'>Other Insurer Amount has been cleared. Please do reverse allocation to Hopitlalization bill amount.</b>", ContentMode.HTML);

		VerticalLayout popupLayout = new VerticalLayout(label, popuupOkBtn);
		popupLayout.setComponentAlignment(popuupOkBtn, Alignment.BOTTOM_CENTER);
		popupLayout.setSpacing(true);
		final Window popupDialog = new Window();
		popupDialog.setCaption("Errors");
		popupDialog.setClosable(true);
		popupDialog.setContent(popupLayout);
		popupDialog.setResizable(false);
		popupDialog.setModal(true);
		UI.getCurrent().addWindow(popupDialog);
//						popupDialog.show(getUI().getCurrent(), null, true);

		popuupOkBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popupDialog.close();
//				bean.setIsReverseAllocation(true);
//				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmt);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);

				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
			    layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.center();
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
//								dialog.show(getUI().getCurrent(), null, true);

				okButton.addClickListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(false);
							if(hospitalizaionObj != null) {
								SHAUtils.setHospitalizationDetailsToDTOForFinancial(bean);
								hospitalizaionObj.initView(bean);
								hospitalizaionObj.setPresenterString(SHAConstants.FINANCIAL);
								consolidatedTabChage();
							}
							dialog.close();
						}
					}
				});
			}
		});
		return false;
	 */

		String label="Other Insurer Amount has been cleared. Please do reverse allocation to Hopitlalization bill amount.";
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(label.toString(), buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//				bean.setIsReverseAllocation(true);
				//				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmt);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);

				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
				layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.center();
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
				//								dialog.show(getUI().getCurrent(), null, true);

				okButton.addClickListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(false);
							if(hospitalizaionObj != null) {
								SHAUtils.setHospitalizationDetailsToDTOForFinancial(bean);
								hospitalizaionObj.initView(bean);
								consolidatedTabChage();
							}
							dialog.close();
						}
					}
				});
			}
		});
		return false;

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
				//txtPayableAt.setEnabled(true);
				btnCitySearch.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}

		}

		//buildPaymentsLayout();
		
		
		if(bean.getPreauthDataExtractionDetails().getPatientStatus() != null 
				&& (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()) 
						|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDataExtractionDetails().getPatientStatus().getId()))
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null
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
			
			if(cmbPayeeName != null) {
				cmbPayeeName.setReadOnly(false);
				cmbPayeeName.setValue(null);
				cmbPayeeName.setEnabled(false);
			}
			if(btnAccPrefSearch != null) 
				btnAccPrefSearch.setEnabled(false);
			
			if(optPaymentMode != null)
				optPaymentMode.setEnabled(false);
			
			if(btnCitySearch != null)
				btnCitySearch.setEnabled(false);
			
			if(btnIFCSSearch != null)
				btnIFCSSearch.setEnabled(false);
			
			if(txtAccntNo != null) {
				txtAccntNo.setRequired(false);
				txtAccntNo.setEnabled(false);
			}	
			
			if(txtEmailId != null) 
				txtEmailId.setEnabled(false);
			
			if(txtPayModeChangeReason != null)
				txtPayModeChangeReason.setEnabled(false);
			
			if(txtReasonForChange != null)
				txtReasonForChange.setEnabled(false);
			
		}
		txtPaymentPartyMode = new TextField();
		 if(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
//				txtPaymentPartyMode = (TextField) binder.buildAndBind("Payment Party Mode", "paymentPartyMode" , TextField.class);
				txtPaymentPartyMode.setCaption("Payment Party Mode");
				txtPaymentPartyMode.setEnabled(true);
				txtPaymentPartyMode.setVisible(true);
				if(bean.getNewIntimationDTO().getPolicy().getPaymentParty() != null && !bean.getNewIntimationDTO().getPolicy().getPaymentParty().isEmpty()){
					txtPaymentPartyMode.setReadOnly(false);
					txtPaymentPartyMode.setValue(bean.getNewIntimationDTO().getPolicy().getPaymentParty());
					txtPaymentPartyMode.setReadOnly(true);
				}
			}else {
				txtPaymentPartyMode.setEnabled(false);
				txtPaymentPartyMode.setVisible(false);
			}
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
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
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

					/*if(null != txtPayableAt && (null == txtPayableAt || ("").equalsIgnoreCase(txtPayableAt.getValue()))){

						mandatoryFields.add(txtPayableAt);
					}*/
					//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);

				}
				else 
				{

					unbindField(getListOfPaymentFields());
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}	

				if(null != bean.getPaymentModeId() && !bean.getPaymentModeId().equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag())){

					mandatoryFields.add(txtPayModeChangeReason);
					txtPayModeChangeReason.setValue("");
					showOrHideValidation(false);
				}
				else
				{
					mandatoryFields.remove(txtPayModeChangeReason);
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

	private HorizontalLayout buildBankTransferLayout()
	{

		btnIFCSSearch = new Button();
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));

		addIFSCCodeListner();
		
		unbindField(txtAccntNo);
		txtAccntNo = (TextField)paymentbinder.buildAndBind("Account No" , "accountNo", TextField.class);
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		if(patientStatus != null 
				&& (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_ADMITTED.equals(patientStatus.getId())
						|| ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatus.getId()))) {
			txtAccntNo.setEnabled(true);
		}
		else {
			txtAccntNo.setEnabled(false);
		}
//		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		/*txtAccntNo.addValueChangeListener(new Property.ValueChangeListener() {

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
		/*	verifyAcntDtlButton = new Button("Verify Account Details");
		 changeVerifiedButtonValue(bean.getVerificationClicked());
		 verifyAcntDtlButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(txtAccntNo.getValue() == null || txtAccntNo.getValue().trim() == ""){
					getAlertMessage("Please Enter Account Number");
				}else{
        		fireViewEvent(FinancialHospitalizationPagePresenter.VERIFICATION_ACCOUNT_DETAILS, bean);
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
							fireViewEvent(FinancialHospitalizationPagePresenter.VERIFIED_ACCOUNT_DETAIL_SAVE, bean);
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


		if(null != this.bean.getPreauthDataExtractionDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getPreauthDataExtractionDetails().getAccountNo());
		}

		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-z A-Z 0-9]*$");
		accntNoValidator.setPreventInvalidTyping(true);

		txtIfscCode = (TextField) paymentbinder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
		
//		txtIfscCode.setRequired(true);
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

		FormLayout bankTransferLayout2 = new FormLayout(/*verifyAcntDtlButton*/dField,btnIFCSSearch);
		FormLayout bankTransferLayout3 = new FormLayout(dField1,dField2,txtBranch);

		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2,bankTransferLayout3);
		hLayout.setSpacing(false);//,bankTransferLayout3);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
//		hLayout.setWidth("80%");
		
		
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
		
		if(bean.getPolicyDto().getPaymentParty() != null && !bean.getPolicyDto().getPaymentParty().isEmpty()
				&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			txtAccntNo.setEnabled(true);
			txtIfscCode.setEnabled(true);
			txtBranch.setEnabled(true);
			txtBankName.setEnabled(true);
			txtCity.setEnabled(true);
			btnIFCSSearch.setEnabled(false);

			txtAccntNo.setReadOnly(true);
			txtIfscCode.setReadOnly(true);
			txtBranch.setReadOnly(true);
			txtBankName.setReadOnly(true);
			txtCity.setReadOnly(true);

		}
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				 && this.bean.getDocumentReceivedFromId() != null
				 && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			txtAccntNo.setEnabled(false);
			btnIFCSSearch.setEnabled(false);
			txtPanNo.setEnabled(false);
		}

		return hLayout;
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

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
			//field.setValidationVisible(false);
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		/*fieldList.add(txtLegalHeirFirstName);
		fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);*/
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
		fieldList.add(txtPayModeChangeReason);
		//fieldList.add(txtPayableAt);
		return fieldList;
	}

	private HorizontalLayout buildChequePaymentLayout(Boolean paymentMode)
	{
		btnCitySearch = new Button();

		btnCitySearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnCitySearch.setIcon(new ThemeResource("images/search.png"));

		addCitySearchListener();

		if(cmbPayeeName != null){
			unbindField(cmbPayeeName);
		}
		
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		cmbPayeeName = (ComboBox) paymentbinder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		
		if(this.bean.getDocumentReceivedFromId() != null 
				&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
			cmbPayeeName.setEnabled(true);
		}
		
		if(this.bean.getDocumentReceivedFromId() != null 
				&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
		
			if(patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
					&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
						cmbPayeeName.setEnabled(false);
			}
			else {
				cmbPayeeName.setEnabled(true);
			}
		}		
		
		cmbPayeeName.setValue(null);
		cmbPayeeName.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) (event.getProperty().getValue()) : null;
				if(null != value)
				{	
					if(txtRelationship != null)
						txtRelationship.setValue(value.getRelationshipWithProposer());
					if(txtNameAsPerBank != null && value.getNameAsPerBankAccount() != null && !value.getNameAsPerBankAccount().isEmpty())
						txtNameAsPerBank.setValue(value.getNameAsPerBankAccount());
				}
			}	
			
		});
		
			if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
		    		 this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			
					 SelectValue payeeName = new SelectValue();
					 BeanItemContainer<SelectValue> payeeNameContainerForCashless = new BeanItemContainer<SelectValue>(SelectValue.class);
					 payeeName.setId(1l);
					 if(null != this.bean.getNewIntimationDTO().getHospitalDto().getHospitalPayableAt()){
						 payeeName.setValue(bean.getNewIntimationDTO().getHospitalDto().getHospitalPayableAt());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }else if(null != this.bean.getNewIntimationDTO().getHospitalDto().getName()){
						 payeeName.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getName());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }
					 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					 cmbPayeeName.setItemCaptionPropertyId("value");
					 cmbPayeeName.setValue(payeeName);
					 cmbPayeeName.setEnabled(false);
			
			}else{
		
					BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);
					payee = getValuesForNameDropDown();
					
					cmbPayeeName.setContainerDataSource(payee);
					cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbPayeeName.setItemCaptionPropertyId("value");
					
				    if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
					
			
			//		payee.addBean(this.bean.getPreauthDataExtractionDetails().getPayeeName());
					
					
					if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null) {
						List<SelectValue> itemIds = payee.getItemIds();
						for (SelectValue selectValue : itemIds) {
							if(selectValue.getValue() != null && this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue() != null && selectValue.getValue().toString().toLowerCase().equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue().toString().toLowerCase())) {
								this.bean.getPreauthDataExtractionDetails().setPayeeName(selectValue);
							}
						}
					}					
					
					cmbPayeeName.setEnabled(false);
				    }
					
				    if(null != payee)
				    {
					    for(int i = 0 ; i<payee.size() ; i++)
					 	{
						    if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
						    		 this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED))
						    {
						    	if(null != this.bean.getNewIntimationDTO().getHospitalDto().getName())
								{
								 if(this.bean.getNewIntimationDTO().getHospitalDto().getName().equalsIgnoreCase(payee.getIdByIndex(i).getValue()))
									{
									 	cmbPayeeName.removeItem(payee.getIdByIndex(i));
										//this.cmbPayeeName.setEnabled(false);
									}
								}
						    	
						    	if(null != bean.getNewIntimationDTO().getPolicy() && 
										bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CRITICARE_PLATINUM_PRODUCT_KEY)){
									this.cmbPayeeName.setReadOnly(true);
								}
						    }
					 	}
				    }
				    
				    accPrefLayout = new HorizontalLayout();
					accPrefLayout.setCaption("Account Preference");
					accPrefLayout.setCaptionAsHtml(true);
					 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
							 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
						 if(txtAccountPref != null){	
							 unbindField(txtAccountPref);
						 }	 
						
						txtAccountPref = (TextField) paymentbinder.buildAndBind("", "accountPref", TextField.class);
						txtAccountPref.setCaption(null);
						txtAccountPref.setEnabled(false);
						txtAccountPref.setNullRepresentation("");
						btnAccPrefSearch = new Button(); 
						btnAccPrefSearch.setStyleName(ValoTheme.BUTTON_LINK);
						btnAccPrefSearch.setIcon(new ThemeResource("images/search.png"));
						
//						btnAccPrefSearch.setEnabled(false);
						
						btnAccPrefSearch.addClickListener(getAccountPreferenceSearchListener());
						if(patientStatus != null 
								&& (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_ADMITTED.equals(patientStatus.getId())
										|| ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatus.getId()))
								&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
							btnAccPrefSearch.setEnabled(true);
						}
						else {
							btnAccPrefSearch.setEnabled(false);
						}
						accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
						accPrefLayout.setComponentAlignment(txtAccountPref,Alignment.TOP_CENTER);
						accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
					 }
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

		txtPayModeChangeReason = (TextField) paymentbinder.buildAndBind("Reason for change (Pay Mode)", "payModeChangeReason" , TextField.class);
		CSValidator payModeChangeValidator = new CSValidator();
		payModeChangeValidator.extend(txtPayModeChangeReason);
		payModeChangeValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		payModeChangeValidator.setPreventInvalidTyping(true);
		txtPayModeChangeReason.setMaxLength(4000);
		payModeChangeListener(txtPayModeChangeReason, null);
		paymentModeValuechangeListener();
		if(null != this.bean.getPayModeChangeReason())
		{
			txtPayModeChangeReason.setValue(this.bean.getPayModeChangeReason());
		}


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

		/*txtLegalHeirFirstName = (TextField) paymentbinder.buildAndBind("","legalFirstName",TextField.class);
		txtLegalHeirMiddleName = (TextField) paymentbinder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) paymentbinder.buildAndBind("", "legalLastName" , TextField.class);

		txtLegalHeirFirstName.setNullRepresentation("");
		txtLegalHeirMiddleName.setNullRepresentation("");
		txtLegalHeirLastName.setNullRepresentation("");*/

		if(txtPayableAt != null){
			txtPayableAt.setReadOnly(false);
		}
		
		unbindField(txtPayableAt);
		txtPayableAt = (TextField) paymentbinder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setEnabled(false);
		//txtPayableAt.setMaxLength(50);
		/*CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z]*$");
		payableAtValidator.setPreventInvalidTyping(true);;*/
		//txtPayableAt.setRequired(true);
		if(null != this.bean.getPreauthDataExtractionDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getPreauthDataExtractionDetails().getPayableAt());
			txtPayableAt.setEnabled(false);
			btnCitySearch.setEnabled(false);
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

			/*txtLegalHeirFirstName.setReadOnly(true);
			txtLegalHeirFirstName.setEnabled(false);

			txtLegalHeirMiddleName.setReadOnly(true);
			txtLegalHeirMiddleName.setEnabled(false);

			txtLegalHeirLastName.setReadOnly(true);
			txtLegalHeirLastName.setEnabled(false);*/

			txtPayableAt.setReadOnly(true);
			txtPayableAt.setEnabled(false);
			btnCitySearch.setEnabled(false);

		}else{
			cmbPayeeName.setEnabled(true);
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				//txtPayableAt.setEnabled(true);
				btnCitySearch.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
		}

		//		GridLayout grid = new GridLayout(5,3);

		/*txtLegalHeirFirstName.setCaption(null);
		txtLegalHeirMiddleName.setCaption(null);
		txtLegalHeirLastName.setCaption(null);*/

		// Billing Internal Remarks
		unbindField(txtAreaFAInternalRemarks);
		txtAreaFAInternalRemarks = (TextArea) paymentbinder.buildAndBind("FA - Internal Remarks", "faInternalRemarks", TextArea.class);
		txtAreaFAInternalRemarks.setMaxLength(4000);
		txtAreaFAInternalRemarks.setNullRepresentation("");
		txtAreaFAInternalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");

		if(this.bean.getPreauthDataExtractionDetails().getFaInternalRemarks() != null) {
			txtAreaFAInternalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getFaInternalRemarks());
		}
		faInternalRemarksChangeListener(txtAreaFAInternalRemarks, null);
				
		FormLayout formLayout1 = null;
		FormLayout formLayout2 = null;
		HorizontalLayout hLayout = null;
		/*HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirFirstName,txtLegalHeirMiddleName,txtLegalHeirLastName);
		nameLayout.setVisible(false);
		nameLayout.setComponentAlignment(txtLegalHeirFirstName, Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);*/

		TextField cField1 = new TextField();
		cField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField1.setReadOnly(true);

		TextField cField2 = new TextField();
		cField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField2.setReadOnly(true);

		TextField cField3 = new TextField();
		cField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField3.setReadOnly(true);

		TextField cField4 = new TextField();
		cField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField4.setReadOnly(true);
		
		/*FormLayout cityLayout = new FormLayout(cField1,cField2,cField3,cField4,btnCitySearch);
		cityLayout.setWidth("80px");*/
		
		HorizontalLayout btnPayableAtHLayout = new HorizontalLayout(txtPayableAt,btnCitySearch);
		txtPayableAt.setCaption(null);
		btnPayableAtHLayout.setCaption("Payable At");
		
		txtPaymentPartyMode = new TextField();
		if(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
			//				txtPaymentPartyMode = (TextField) binder.buildAndBind("Payment Party Mode", "paymentPartyMode" , TextField.class);
			txtPaymentPartyMode.setCaption("Payment Party Mode");
			txtPaymentPartyMode.setEnabled(true);
			txtPaymentPartyMode.setVisible(true);
			if(bean.getNewIntimationDTO().getPolicy().getPaymentParty() != null && !bean.getNewIntimationDTO().getPolicy().getPaymentParty().isEmpty()){
				txtPaymentPartyMode.setReadOnly(false);
				txtPaymentPartyMode.setValue(bean.getNewIntimationDTO().getPolicy().getPaymentParty());
				txtPaymentPartyMode.setReadOnly(true);
			}
		}else {
			txtPaymentPartyMode.setEnabled(false);
			txtPaymentPartyMode.setVisible(false);
		}
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
				&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
		{
			formLayout1 = new FormLayout(optPaymentMode, txtPayModeChangeReason, txtEmailId,txtPanNo, btnPayableAtHLayout, txtAreaFAInternalRemarks);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(cmbPayeeName,txtPaymentPartyMode,txtReasonForChange/*,nameLayout*/);
			hLayout = new HorizontalLayout(formLayout1 /*,btnLayout*/, formLayout2);
			hLayout.setMargin(true);
			if(! paymentMode){
				
				if(btnPayableAtHLayout != null){
					formLayout1.removeComponent(btnPayableAtHLayout);
				}
			}
		}
		else
		{
//			formLayout1 = new FormLayout(optPaymentMode, txtPayModeChangeReason, txtEmailId, txtPanNo, txtAreaFAInternalRemarks);
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
			formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtPaymentPartyMode,txtReasonForChange,txtNameAsPerBank,btnPayableAtHLayout,txtPanNo,txtEmailId,txtAreaFAInternalRemarks);
			formLayout1.setMargin(false);
			
			formLayout2 = new FormLayout(txtPayModeChangeReason,txtRelationship);
			 
			 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
				 
				accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
				accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
				formLayout2.addComponent(accPrefLayout);
			 }
			 
			 formLayout2.addComponents(txtAccType/*,nameLayout*/);
			 formLayout2.setMargin(false);
			 hLayout = new HorizontalLayout(formLayout1 /*,btnLayout*/, formLayout2);
			 hLayout.setMargin(true);
			
		}
		
//		if(! paymentMode && this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
				&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
			if(! paymentMode){
				if(txtPayableAt != null){
					formLayout1.removeComponent(btnPayableAtHLayout);
				}			
			}
		}
		
		/*if(null != txtPayableAt)
		{
			mandatoryFields.add(txtPayableAt);
			setRequiredAndValidation(txtPayableAt);
		}*/

//		payeenameListner();
		
		cmbPayeeName.setValue(this.bean.getPreauthDataExtractionDetails().getPayeeName());
		
		if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
	    		 this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED))
		{
			if(null != bean.getNewIntimationDTO().getPolicy() && 
					bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CRITICARE_PLATINUM_PRODUCT_KEY)){
				this.cmbPayeeName.setReadOnly(true);
			}
		}

		return hLayout;
	}

	private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
	{
		Policy policy = policyService.getPolicy(this.bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
		if(null != policy)
		{
			//Added for slowness issue reported for GMC policies - 31-12-2018
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(!ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
		
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		SelectValue payeeValue = null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			payeeValue.setSourceRiskId(insured.getSourceRiskId());
			payeeValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null && insured.getRelationshipwithInsuredId().getValue() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
			payeeValue.setNameAsPerBankAccount(insured.getNameOfAccountHolder());
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		/*for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
			}
			
		}*/
		
		if( (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null && this.bean.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB)) 
				&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
				&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
		
			List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());
		
		for (PolicyNominee pNominee : pNomineeDetails) {
			selectValue = new SelectValue();
			selectValue.setId(pNominee.getKey());
			selectValue.setValue(pNominee.getNomineeName());
			payeeValueList.add(selectValue);
			selectValue = null;
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
				
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)){
			
			SelectValue hospitalPayableAt = new SelectValue();
			hospitalPayableAt.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalPayableAt.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getHospitalPayableAt());
			payeeValueList.add(hospitalPayableAt);
			
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getName());
			payeeValueList.add(hospitalName);
		}

		/*BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);*/
		
		/*if(!ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){*/
			
			payeeNameValueContainer.addAll(payeeValueList);
		}
		else
		{
			payeeNameValueContainer = dbCalculationService.getPayeeName(this.bean.getNewIntimationDTO().getPolicy().getKey(),
					this.bean.getNewIntimationDTO().getKey());
		}
		
		payeeNameValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return payeeNameValueContainer;
		
		}
		
		return null;
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

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	@Override
	public void submit(PreauthDTO preauthDTO) {
		specialistWindow.close();
		wizard.finish();
	}


	private VerticalLayout getOtherInsSettlementTab(){
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			if(otherInsTabLayout == null) {
				otherInsTabLayout = new HorizontalLayout();
				otherInsTabLayout.setSpacing(false);
				otherInsTabLayout.setWidth("100%");
			} else {
				otherInsTabLayout.removeAllComponents();
			}

			if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
				if(otherInsurerHospObj == null) {
					otherInsurerHospObj = otherInsHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementHospValues(bean, hospitalizaionObj != null ? hospitalizaionObj.getHospAmt() : "0", hospitalizaionObj != null ? hospitalizaionObj.getAmountAlreadyPaid() : "0");
				otherInsurerHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerHospObj);
			} 
			if(bean.getPreHospitalizaionFlag()){
				if(otherInsurerPreHospObj == null) {
					otherInsurerPreHospObj = otherInsPreHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementPreHospValues(bean, preHospitalizationObj != null ? preHospitalizationObj.getPreHospAmt() : "0", preHospitalizationObj != null ? preHospitalizationObj.getAmountAlreadyPaid() : "0");
				otherInsurerPreHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerPreHospObj);
			}
			if(bean.getPostHospitalizaionFlag()) {
				if(otherInsurerPostHospObj == null) {
					otherInsurerPostHospObj = otherInsPostHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementPostHospValues(bean, postHospitalizationObj != null ? postHospitalizationObj.getPostHospAmt() : "0", postHospitalizationObj != null ? postHospitalizationObj.getAmountAlreadyPaid() : "0");
				otherInsurerPostHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerPostHospObj);
			}
		} else {
			if(otherInsTabLayout != null) {
				otherInsTabLayout.removeAllComponents();
			}
		}
		addListenerForConsolidated();

		/*TabSheet otherInsSettlementTab = new TabSheet();
		//Vaadin8-setImmediate() otherInsSettlementTab.setImmediate(true);
		otherInsSettlementTab.setWidth("100%");
		otherInsSettlementTab.setHeight("100%");
		otherInsSettlementTab.setSizeFull();
		//Vaadin8-setImmediate() otherInsSettlementTab.setImmediate(true);*/

		VerticalLayout verticalLayout = new VerticalLayout(otherInsTabLayout);
		verticalLayout.setStyleName("tpabackground");
		verticalLayout.setWidth("100%");
		verticalLayout.setCaption("Other Insurer Settlement");
		//		otherInsSettlementTab.addComponent(verticalLayout);

		return verticalLayout;
	}

	private VerticalLayout getConsolidatedAmountTab(){
		/*TabSheet consolidateAmtTab = new TabSheet();
		//Vaadin8-setImmediate() consolidateAmtTab.setImmediate(true);
		consolidateAmtTab.setWidth("100%");
		consolidateAmtTab.setHeight("100%");
		consolidateAmtTab.setSizeFull();
		//Vaadin8-setImmediate() consolidateAmtTab.setImmediate(true);*/
		if(consolidatedAmountObj == null) {
			consolidatedAmountObj = consolidatedAmountInstance.get();
		}
		//		consolidatedAmountObj.initView(this.bean, true);
		consolidatedTabChage();

		VerticalLayout verticalLayout = new VerticalLayout(consolidatedAmountObj);
		verticalLayout.setCaption("Consolidated Amount");
		verticalLayout.setSpacing(true);

		//		consolidateAmtTab.addComponent(verticalLayout);

		return verticalLayout;

	}


	private void consolidatedTabChage() {
		ConsolidatedAmountDetailsDTO consolidatedAmtDTO = bean.getConsolidatedAmtDTO();
		if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {

				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(otherInsurerHospObj != null) {
						consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerHospObj.getPayableAmount()));
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(otherInsurerPreHospObj != null) {
						consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPreHospObj.getPayableAmount()));
					}
				}

				if(bean.getPostHospitalizaionFlag()) {
					if(otherInsurerPostHospObj != null) {
						consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPostHospObj.getPayableAmount()));
					}
				}
				if(consolidatedAmountObj != null) {
					consolidatedAmountObj.initView(bean, true);
				}
			} else {
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(hospitalizaionObj != null) {
						consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
					}
				}else{
					consolidatedAmtDTO.setHospPayableAmt(0);
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(preHospitalizationObj != null) {
						consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
					}
				}else{
					consolidatedAmtDTO.setPreHospPayableAmt(0);
				}

				if(bean.getPostHospitalizaionFlag()) {
					if(postHospitalizationObj != null) {
						consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPostHospAmt()));
					}
				}else{
					consolidatedAmtDTO.setPostHospPayableAmt(0);
				}
				if(consolidatedAmountObj != null) {
					consolidatedAmountObj.initView(bean, true);
				}
			}
		} else {
			if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
				if(hospitalizaionObj != null) {
					consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
				}

			}
			else {
				consolidatedAmtDTO.setHospPayableAmt(0);
			}
			if(bean.getPreHospitalizaionFlag()) {
				if(preHospitalizationObj != null) {
					consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
				}
			}else {
				consolidatedAmtDTO.setPreHospPayableAmt(0);
			}

			if(bean.getPostHospitalizaionFlag()) {
				if(postHospitalizationObj != null) {
					consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPostHospAmt()));
				} 
			}
			else {
				consolidatedAmtDTO.setPostHospPayableAmt(0);
			}
			if(consolidatedAmountObj != null) {
				consolidatedAmountObj.initView(bean, true);
			}
		}

		List<AddOnBenefitsDTO> hospCashList = null;
		List<AddOnBenefitsDTO> patientCareList = null;
		List<AddOnBenefitsDTO> consolidatedDTOList = new ArrayList<AddOnBenefitsDTO>();
		Double consolidatedNetAppAmt = 0d;
		if(null != addOnBenefitsListenerTableObj)
			hospCashList = addOnBenefitsListenerTableObj.getValues();
		if(null != addOnBenefitsPatientCareListnerTable)
			patientCareList = addOnBenefitsPatientCareListnerTable.getValues();

		if(null != hospCashList && !hospCashList.isEmpty())
		{
			for (AddOnBenefitsDTO addOnBenefitsDTO : hospCashList) {
				consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
				consolidatedDTOList.add(addOnBenefitsDTO);
			}

		}
		if(null != patientCareList && !patientCareList.isEmpty())
		{
			for (AddOnBenefitsDTO addOnBenefitsDTO : patientCareList) {
				consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
				consolidatedDTOList.add(addOnBenefitsDTO);
			}
		}
		if(null != consolidatedNetAppAmt)
			consolidatedAmtDTO.setAddonBenefitAmt(consolidatedNetAppAmt.intValue());
		if(null !=consolidatedDTOList && !consolidatedDTOList.isEmpty())
			bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(consolidatedDTOList);

	}


	private void otherInsSettlementChange() {
		if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(otherInsurerHospObj != null ) {
						otherInsurerHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementHospValues(bean, hospitalizaionObj != null ? hospitalizaionObj.getHospAmt() : "0", hospitalizaionObj != null ? hospitalizaionObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerHospObj != null) {
						otherInsurerHospObj.initView(bean);
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(otherInsurerPreHospObj != null ) {
						otherInsurerPreHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementPreHospValues(bean, preHospitalizationObj != null ? preHospitalizationObj.getPreHospAmt() : "0", preHospitalizationObj != null ? preHospitalizationObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerPreHospObj != null) {
						otherInsurerPreHospObj.initView(bean);
					}
				}

				if(bean.getPostHospitalizaionFlag()) {
					if(otherInsurerPostHospObj != null ) {
						otherInsurerPostHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementPostHospValues(bean, postHospitalizationObj != null ? postHospitalizationObj.getPostHospAmt() : "0", postHospitalizationObj != null ? postHospitalizationObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerPostHospObj != null) {
						otherInsurerPostHospObj.initView(bean);
					}
				}
			}
		} else {

		}
	}

	public void addListenerForConsolidated() {
		if(otherInsurerHospObj != null) {
			otherInsurerHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();

				}
			});
		}

		if(otherInsurerPostHospObj != null) {
			otherInsurerPostHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();

				}
			});
		}

		if(otherInsurerPreHospObj != null) {
			otherInsurerPreHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();

				}
			});
		}
	}

	private void addListenerForBenefits()
	{
		if(addOnBenefitsListenerTableObj != null) {
			addOnBenefitsListenerTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null) {
						Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(totalValue);
						hospitalCashPayableAmt.setReadOnly(false);
						hospitalCashPayableAmt.setValue(String.valueOf(doubleFromStringWithComma.intValue()) );
						bean.setHospitalCashAmt(doubleFromStringWithComma);
						hospitalCashPayableAmt.setReadOnly(true);
					}

				}
			});
		}

		if(addOnBenefitsPatientCareListnerTable != null) {
			addOnBenefitsPatientCareListnerTable.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null) {
						Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(totalValue);
						patientCarePayableAmt.setReadOnly(false);
						patientCarePayableAmt.setValue(String.valueOf(doubleFromStringWithComma.intValue()) );
						bean.setPatientCareAmt(doubleFromStringWithComma);
						patientCarePayableAmt.setReadOnly(true);

					}

				}
			});
		}	
	}

	public void alertMessageUniquePremium() {
		/*Label successLabel = new Label();
		successLabel = new Label(
				"<b style = 'color: red;'> II Instalment premium cannot be adjusted as amount claimed is lower, Please collect the premium from the insured </b>",
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("II Instalment premium cannot be adjusted as amount claimed is lower, Please collect the premium from the insured ", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				financialButtonObj.generateFieldsForApproval();
			}
		});
	}
	private void addPreviousPaymentPopupListener()
	{
		btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {



				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");
				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.FINANCIAL);
				previousPaymentVerticalLayout = new VerticalLayout();
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsLayout);
				previousPaymentVerticalLayout.setComponentAlignment(previousAccountDetailsLayout, Alignment.TOP_CENTER);
				populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);
				setTableValues();
				populatePreviousWindowPopup.setClosable(true);
				populatePreviousWindowPopup.center();
				populatePreviousWindowPopup.setResizable(true);

				populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				populatePreviousWindowPopup.setModal(true);
				populatePreviousWindowPopup.setClosable(false);

				UI.getCurrent().addWindow(populatePreviousWindowPopup);
				btnPopulatePreviousAccntDetails.setEnabled(true);
			}
		});


		btnOk.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String err = previousAccountDetailsTable.isValidate();
				if("" == err)
				{
					buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK);
					//populatePreviousWindowPopup.close();
					//previousAccountDetailsTable.clearCheckBoxValue();
				}
			}
		});

		btnCancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);				//resetBankPaymentFeidls();
				//previousAccountDetailsTable.clearCheckBoxValue();
			}
		});
	}

	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(tableDTO.getEmailId());
			txtEmailId.setEnabled(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(tableDTO.getPanNo());
			txtPanNo.setEnabled(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			txtAccntNo.setEnabled(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(tableDTO.getIfsccode());
			txtIfscCode.setEnabled(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(tableDTO.getBankName());
			txtBankName.setEnabled(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(tableDTO.getBankCity());
			txtCity.setEnabled(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(tableDTO.getBankBranch());
			txtBranch.setEnabled(true);
		}



	}

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

	public Set<String> getErrors()
	{
		return this.errorMessages;
	}



	public void resetBankPaymentFeidls() {
		if(null != txtEmailId)
		{	
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(null);
		}	
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(null);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(null);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(null);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(null);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(null);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(null);
		}

	}

	/*private void buildDialogBox(String message,final Window populatePreviousWindowPopup)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);

		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> healthrelease
=======
	public void alertMessageUniquePremium() {
		Label successLabel = new Label();
		successLabel = new Label(
				"<b style = 'color: red;'> II Instalment premium cannot be adjusted as amount claimed is lower, Please collect the premium from the insured </b>",
				ContentMode.HTML);
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
>>>>>>> 603126e515bb2060ed88156fe705b49310558eee
<<<<<<< HEAD
>>>>>>> augustRelease1
=======
=======
>>>>>>> healthproducts
>>>>>>> healthrelease
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
<<<<<<< HEAD
<<<<<<< HEAD

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
=======
=======
>>>>>>> healthrelease
<<<<<<< HEAD

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
=======
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
>>>>>>> 603126e515bb2060ed88156fe705b49310558eee
<<<<<<< HEAD
>>>>>>> augustRelease1
=======
=======

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
>>>>>>> healthproducts
>>>>>>> healthrelease
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> augustRelease1
=======
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> healthproducts
>>>>>>> healthrelease
		if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);


		}
		getUI().getCurrent().addWindow(dialog);
=======
		dialog.show(getUI().getCurrent(), null, true);

>>>>>>> 603126e515bb2060ed88156fe705b49310558eee
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
<<<<<<< HEAD
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});
	}*/

	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		/*Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);

		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);*/
		/*HorizontalLayout horizontalLayout = new HorizontalLayout();*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");


		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Camcel");
			/*horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);*/
		}
		else
		{
			/*horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);*/
		}

		/*horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);*/
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);

		/*VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);*/
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);


		}
		getUI().getCurrent().addWindow(dialog);*/


		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button cancelBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

			}
		});
		if(null != cancelBtn)
		{
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					/*if(null != populatePreviousWindowPopup)
						populatePreviousWindowPopup.close();*/
					//fireViewEvent(MenuItemBean.CREATE_ROD, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);

				}
			});
		}
	}
	private void setTableValues()
	{
		if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
			//List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
			List<PreviousAccountDetailsDTO> previousListTable = this.bean.getPreviousAccntDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{				
				for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousListTable) {

					previousAccountDetailsDTO.setChkSelect(false);
					previousAccountDetailsDTO.setChkSelect(null);						
					previousAccountDetailsDTO.setSerialNo(rowCount);
					previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
					rowCount ++ ;
				}
			}

		}
	}

	public void alertMessageForInvestigation() {

		String message = "Investigation request has already been initiated";

		/*Label successLabel = new Label("<b style = 'color: Red;'>"+message, ContentMode.HTML);

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

	public void addOtherBenefitsListener(){
		this.otherBenefitCalculationObj.getDummyFieldForLisener()
		.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String otherBenefitsAmt = (String) event.getProperty().getValue();
				consolidatedAmountObj.setOtherBenefitAmount(otherBenefitsAmt);

			}
		});
	}

	public void setClearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);
		if(wholeLayout != null){
			wholeLayout.removeAllComponents();
		}
	}

	public void setOtherBenefitValuesForBilling(){
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
				ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
				(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			if(otherBenefitCalculationObj != null){
				otherBenefitCalculationObj.setOtherBenefitsValues();
			}
		}
		if(legalBillingUIObj !=null){
			this.bean.setLegalBillingDTO(legalBillingUIObj.getvalue());	
		}		
	}
	public WeakHashMap<Integer, Object> isValiateApprovedAmount(){
		WeakHashMap<Integer, Object> map = new WeakHashMap<Integer, Object>();
		Boolean hasError = false;
		String msg = "";
		if(bean.getHospitalizaionFlag() != null && bean.getHospitalizaionFlag()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
			if(validateApprovedAmount){
				hasError = true;
				if(msg.isEmpty()){
					msg += "Hospitalization";
				}else{
					msg += "/ Hospitalization";
				}
				//eMsg.append("hospitalisation amount is Zero</br>");
			}
		}
		if(bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean,SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					if(msg.isEmpty()){
						msg += "Partial Hospitalization";
					}else{
						msg += "/ Partial Hospitalization";
					}
					//eMsg.append("Partial hospitalisation amount is Zero</br>");
				}
			}

		}
		if(bean.getPreHospitalizaionFlag() != null && bean.getPreHospitalizaionFlag()){
			String hospAmt = preHospitalizationObj.getPreHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					if(msg.isEmpty()){
						msg += "Pre Hospitalization";
					}else{
						msg += "/ Pre Hospitalization";
					}
					//eMsg.append("Pre hospitalisation amount is Zero");
				}
			}
		}
		if(bean.getPostHospitalizaionFlag() != null && bean.getPostHospitalizaionFlag()){
			String hospAmt = postHospitalizationObj.getPostHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					if(msg.isEmpty()){
						msg += "Post Hospitalization";
					}else{
						msg += "/ Post Hospitalization";
					}
					//eMsg.append("Post hospitalisation amount is Zero</br>");
				}
			}
		}
		if(bean.getIsHospitalizationRepeat() != null && bean.getIsHospitalizationRepeat()){
			String hospAmt = hospitalizaionObj.getHospAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, SHAUtils.getIntegerFromStringWithComma(hospAmt));
				if(validateApprovedAmount){
					hasError = true;
					if(msg.isEmpty()){
						msg += "Hospitalization Repeat";
					}else{
						msg += "/ Hospitalization Repeat";
					}
					//eMsg.append("Hospitalisation Repeat amount is Zero</br>");
				}
			}
		}

		if(bean.getOtherBenefitsFlag() != null && bean.getOtherBenefitsFlag()){
			Double hospAmt = bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt();
			if(hospAmt != null){
				Boolean validateApprovedAmount = SHAUtils.validateApprovedAmount(bean, hospAmt.intValue());
				if(validateApprovedAmount){
					hasError = true;
					if(msg.isEmpty()){
						msg += "Other Benefit";
					}else{
						msg += "/ Other Benefit";
					}
					//eMsg.append("Pre hospitalisation amount is Zero");
				}
			}
		}

		map.put(1, hasError);
		map.put(2, msg);
		return map;
	}

	public void generateLayoutBasedOnApprovedAmt() {
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			Long input1 =0l;
			String input2 = null;
			java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
			java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();
					
		    java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
		    java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
		    Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt());
		    //added for instalment issue by noufel
		    if( bean.getClaimDTO() != null && bean.getClaimDTO().getClaimType() != null 
		    		&& bean.getClaimDTO().getClaimType().getId() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
		    	Integer premiumAmount = bean.getPolicyInstalmentPremiumAmt().intValue();
		    	approvedAmt = approvedAmt + premiumAmount;
		    }
	    if(bean.getIsPremiumInstAmtEql()){
		    	approvedAmt = hospitalizaionObj.getPremiumAmountEqualZero();
		    }
			Map<String, String> getPolicyInstallmentDetails = dbCalculationService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
					bean.getPolicyInstalmentPremiumAmt(),admissionDate,approvedAmt.doubleValue(),
					instalmentDate,input1,input2);
			 if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
				 bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
				 bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
                }
			 if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
				this.financialButtonObj.disableOnlyApprove(false);
				SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
				bean.setShouldDetectPremium(false);
			} else {
				bean.setShouldDetectPremium(true);
				this.financialButtonObj.generateFieldsForApproval();
			}
		}
		else if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (bean.getHospitalizaionFlag())) {

			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			bean.setUniquePremiumAmount(uniqueInstallmentAmount.doubleValue());
			Double premiumAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt() + uniqueInstallmentAmount.doubleValue();
			if(bean.getHospitalizationCalculationDTO().getNetPayableAmt() < premiumAmt.intValue()) {
				bean.setShouldDetectPremium(false);
				alertMessageUniquePremium();
			} else {
				bean.setShouldDetectPremium(true);
				this.financialButtonObj.generateFieldsForApproval();
			}
		} else {
			bean.setShouldDetectPremium(true);
			this.financialButtonObj.generateFieldsForApproval();
		}
		//		this.financialButtonObj.generateFieldsForApproval();
		this.bean.setStatusKey(ReferenceTable.FINANCIAL_APPROVE_STATUS);

	}

	public void setUserLimitAmount(){
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			if(bean.getIsReconsiderationRequest() != null && ! bean.getIsReconsiderationRequest()){
				if(bean.getHospitalizaionFlag()){
					String hospitalizationPayableAmt = consolidatedAmountObj.getHospitalizationPayableAmt();
					bean.setUserLimitAmount(hospitalizationPayableAmt);
				}
				//code changed by noufel since for cashless claim and doc recevied from insured case we have to consider pre post amount in user limit amount
				else {
					String totalPayableAmt = consolidatedAmountObj.getTotalConsolidatedAmt();
					bean.setUserLimitAmount(totalPayableAmt);
				}
			}else{
//				String netPayableAmtAfterCopay = hospitalizaionObj.getNetPayableAmtAfterCopay();
//				bean.setUserLimitAmount(netPayableAmtAfterCopay);
				//above code commented and below code added for getting the claimed amount 
				String netPayableAmtAfterCopay = hospitalizaionObj.getNetPayableAmtAfterCopay();
				Integer netPayable = SHAUtils.getIntegerFromStringWithComma(netPayableAmtAfterCopay);
				String revisiedEligibleAmt = preHospitalizationObj.getRevisiedEligibleAmt();
				Integer preRevised = SHAUtils.getIntegerFromStringWithComma(revisiedEligibleAmt);
				String revisiedEligibleAmt2 = postHospitalizationObj.getRevisiedEligibleAmt();
				Integer postRevised = SHAUtils.getIntegerFromStringWithComma(revisiedEligibleAmt2);
				Integer totalAmt = 0;
				if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPreHospitalizaionFlag() && bean.getPostHospitalizaionFlag()){

					totalAmt = netPayable + preRevised + postRevised;

				}else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPreHospitalizaionFlag()){

					totalAmt = netPayable + preRevised;

				}else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()) && bean.getPostHospitalizaionFlag()){

					totalAmt = netPayable + postRevised;

				}else if(bean.getPreHospitalizaionFlag() && bean.getPostHospitalizaionFlag()){

					totalAmt = preRevised + postRevised;

				}else if((bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag())){

					totalAmt = netPayable;
				}
				bean.setUserLimitAmount(totalAmt.toString());
			}
		}else{
			if(bean.getIsReconsiderationRequest() != null && ! bean.getIsReconsiderationRequest()){
				String totalPayableAmt = consolidatedAmountObj.getTotalConsolidatedAmt();
				bean.setUserLimitAmount(totalPayableAmt);
			}else{
				String netPayableAmtAfterCopay = hospitalizaionObj.getNetPayableAmtAfterCopay();
				Integer netPayable = SHAUtils.getIntegerFromStringWithComma(netPayableAmtAfterCopay);
				String revisiedEligibleAmt = preHospitalizationObj.getRevisiedEligibleAmt();
				Integer preRevised = SHAUtils.getIntegerFromStringWithComma(revisiedEligibleAmt);
				String revisiedEligibleAmt2 = postHospitalizationObj.getRevisiedEligibleAmt();
				Integer postRevised = SHAUtils.getIntegerFromStringWithComma(revisiedEligibleAmt2);
				Integer totalAmt = 0;
				if(bean.getHospitalizaionFlag() && bean.getPreHospitalizaionFlag() && bean.getPostHospitalizaionFlag()){

					totalAmt = netPayable + preRevised + postRevised;

				}else if(bean.getHospitalizaionFlag() && bean.getPreHospitalizaionFlag()){

					totalAmt = netPayable + preRevised;

				}else if(bean.getHospitalizaionFlag() && bean.getPostHospitalizaionFlag()){

					totalAmt = netPayable + postRevised;

				}else if(bean.getPreHospitalizaionFlag() && bean.getPostHospitalizaionFlag()){

					totalAmt = preRevised + postRevised;

				}else if(bean.getHospitalizaionFlag()){

					totalAmt = netPayable;
				}
				bean.setUserLimitAmount(totalAmt.toString());
			}
		}
	}


	public void confirmRejectionLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject){
		financialButtonObj.buildRejectLayout(rejectionCategoryDropdownValues, isDefinedLimitReject);
	}


	public  void payModeChangeListener(TextField searchField, final  Listener listener) {
		ShortcutListener enterShortCut = new ShortcutListener(
				"EnterShortcut", ShortcutAction.KeyCode.F8, null) {
			private static final long serialVersionUID = -2267576464623389044L;
			@Override
			public void handleAction( Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};	  
		handleShortcut(searchField, getShortCutListener(searchField));
	}


	public  void handleShortcut(final TextField textField, final ShortcutListener shortcutListener) {	
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

	private ShortcutListener getShortCutListener(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {							

				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}

				TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setMaxLength(4000);

				txtArea.setValue(txtPayModeChangeReason.getValue());			
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
						// TODO Auto-generated method stub

					}
				});

				bean.setPayModeChangeReason(txtArea.getValue());	
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);			


				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);

				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}


	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeValuechangeListener()
	{
		txtPayModeChangeReason.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				String value = (String) event.getProperty().getValue();				
				if(null != value){
					//bean.setPayModeChangeReason(value);
					txtPayModeChangeReason.setDescription(value);
				}

			}
		});		

	}

	public void alertMessageForHoldPayment(String string) {

		String message = string +"</b>";

		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);

//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);


		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.setAlertMessageHoldOpened(true);
				wizard.next();
				// dialog.close();	

			}
		});
	}

	public  void faInternalRemarksChangeListener(TextArea textArea, final  Listener listener) {
		@SuppressWarnings("unused")
		ShortcutListener enterShortCut = new ShortcutListener("ShortcutForFAInternalRemarks", ShortcutAction.KeyCode.F8, null) {
			private static final long serialVersionUID = -2267576464623389044L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};	  
		handleShortcut(textArea, getFAInternalRemarksShortCutListener(textArea));
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

	public void alertMsgForCancelRod(String screenName){

		String message = "";
		if(null != screenName && SHAConstants.MEDICAL.equalsIgnoreCase(screenName)){
			message = SHAConstants.ALERT_FOR_CANCEL_ROD;
		}
		else if(null != screenName && SHAConstants.FINANCIAL.equalsIgnoreCase(screenName))
		{
			message = SHAConstants.FA_ALERT_FOR_CANCEL_ROD;
		}
		/*Label successLabel = new Label(
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
				.createAlertBox( message, buttonsNamewithType);
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

	private ShortcutListener getFAInternalRemarksShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("ShortcutForFAInternalRemarks", KeyCodes.KEY_F8,null) {
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

				dialog.setCaption("FA - Internal Remarks");
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

	public void addCitySearchListener(){
		btnCitySearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				citySearchCriteriaWindow.setPresenterString(SHAConstants.FINANCIAL_APPROVER);
				citySearchCriteriaWindow.initView(popup);
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(citySearchCriteriaWindow);
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
				btnCitySearch.setEnabled(true);
			}
		});
	}

	public void setUpPayableDetails(String name){
		txtPayableAt.setReadOnly(false);
		txtPayableAt.setValue(name);
		txtPayableAt.setReadOnly(true);
	}

	public void confirmationForInvestigation() {


		String message = "Investigation already raised has been completed. </br>Do you want to proceed further?";

		/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);

		Button homeButton = new Button("Yes");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		Button cancelButton = new Button("No");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);

		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();	
				generateButton(3,masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
			}
		});

		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsInvestigation(false);
			}
		});
	}

	public void alertMsgForInvsPending(final String decisionStatus) {

		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.INV_PENDING_ALERT_FA + "</b>",
				ContentMode.HTML);
		//final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
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
				.createAlertBox(SHAConstants.INV_PENDING_ALERT_FA + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null != decisionStatus && decisionStatus.equalsIgnoreCase(SHAConstants.REJECT)){
					generateButton(8,masterService.getReimbRejCategoryByValue());
				}
				else if(null != decisionStatus && decisionStatus.equalsIgnoreCase(SHAConstants.APPROVAL)){
					generateButton(9, null);
				}
				//dialog.close();
			}
		});
	}

	public void getAlertMessage(String eMsg){

		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("alertMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Alert");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	public void setBalanceSIforRechargedProcessing(Double balanceSI){
		if(balanceSI != null){
			this.bean.setBalanceSI(balanceSI);
			rechargeDummyField.setValue(balanceSI.toString());
		}
	}

	public void setTotalConsolidateAmtAfterHospitalDiscnt(PreauthDTO bean){
		consolidatedTabChage(bean);
	}

	private void consolidatedTabChage(PreauthDTO bean) {
		ConsolidatedAmountDetailsDTO consolidatedAmtDTO = bean.getConsolidatedAmtDTO();
		if(consolidatedAmtDTO != null){
			if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {

					if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
						if(otherInsurerHospObj != null) {
							consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerHospObj.getPayableAmount()));
						}
					}
					if(bean.getPreHospitalizaionFlag()) {
						if(otherInsurerPreHospObj != null) {
							consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPreHospObj.getPayableAmount()));
						}
					}

					if(bean.getPostHospitalizaionFlag()) {
						if(otherInsurerPostHospObj != null) {
							consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPostHospObj.getPayableAmount()));
						}
					}
					if(consolidatedAmountObj != null) {
						consolidatedAmountObj.initView(bean, true);
					}
				} else {
					if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
						if(hospitalizaionObj != null) {
							consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
						}
					}else{
						consolidatedAmtDTO.setHospPayableAmt(0);
					}
					if(bean.getPreHospitalizaionFlag()) {
						if(preHospitalizationObj != null) {
							consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
						}
					}else{
						consolidatedAmtDTO.setPreHospPayableAmt(0);
					}

					if(bean.getPostHospitalizaionFlag()) {
						if(postHospitalizationObj != null) {
							consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPostHospAmt()));
						}
					}else{
						consolidatedAmtDTO.setPostHospPayableAmt(0);
					}
					if(consolidatedAmountObj != null) {
						consolidatedAmountObj.initView(bean, true);
					}
				}
			} else {
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(hospitalizaionObj != null) {
						consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
					}

				}
				else {
					consolidatedAmtDTO.setHospPayableAmt(0);
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(preHospitalizationObj != null) {
						consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
					}
				}else {
					consolidatedAmtDTO.setPreHospPayableAmt(0);
				}

				if(bean.getPostHospitalizaionFlag()) {
					if(postHospitalizationObj != null) {
						consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPostHospAmt()));
					} 
				}
				else {
					consolidatedAmtDTO.setPostHospPayableAmt(0);
				}
				if(consolidatedAmountObj != null) {
					consolidatedAmountObj.initView(bean, true);
				}
			}

			List<AddOnBenefitsDTO> hospCashList = null;
			List<AddOnBenefitsDTO> patientCareList = null;
			List<AddOnBenefitsDTO> consolidatedDTOList = new ArrayList<AddOnBenefitsDTO>();
			Double consolidatedNetAppAmt = 0d;
			if(null != addOnBenefitsListenerTableObj)
				hospCashList = addOnBenefitsListenerTableObj.getValues();
			if(null != addOnBenefitsPatientCareListnerTable)
				patientCareList = addOnBenefitsPatientCareListnerTable.getValues();

			if(null != hospCashList && !hospCashList.isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : hospCashList) {
					consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
					consolidatedDTOList.add(addOnBenefitsDTO);
				}

			}
			if(null != patientCareList && !patientCareList.isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : patientCareList) {
					consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
					consolidatedDTOList.add(addOnBenefitsDTO);
				}
			}
			if(null != consolidatedNetAppAmt)
				consolidatedAmtDTO.setAddonBenefitAmt(consolidatedNetAppAmt.intValue());
			if(null !=consolidatedDTOList && !consolidatedDTOList.isEmpty())
				bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(consolidatedDTOList);

			if(consolidatedAmountObj !=null
				&& legalBillingUIObj !=null){
					legalBillingUIObj.setawardAmount(consolidatedAmountObj.getTotalConsolidatedAmt());
			}
		}
		
	}
	public void showInformation(String eMsg) {
		MessageBox.create()
		.withCaptionCust("Information").withHtmlMessage(eMsg.toString())
		.withOkButton(ButtonOption.caption("OK")).open();
	}

	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		financialButtonObj.setSubCategContainer(rejSubcategContainer);
	}
	public Long getHospitalizationApprovedamt(){
		if(hospitalizaionObj !=null){
			return hospitalizaionObj.getHospitalizationApprovedamt();
		}
		return 0L;
	}

	private ClickListener getAccountPreferenceSearchListener(){

		ClickListener listener = new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {				
				final Window popup = new com.vaadin.ui.Window();
				SelectValue value = (SelectValue) cmbPayeeName.getValue();
				ReceiptOfDocumentsDTO beanDto = new ReceiptOfDocumentsDTO();
				beanDto.setSourceRiskID(value.getSourceRiskId());
				bankDetailsTableObj =  bankDetailsTableInstance.get();
				bankDetailsTableObj.init(beanDto);
				bankDetailsTableObj.initPresenter(SHAConstants.FINANCIAL_APPROVER);
				bankDetailsTableObj.setCaption("Bank Details");

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

	public Long getHospitalizationCashlessApprovedamt(){
		if(hospitalizaionObj !=null){
			return hospitalizaionObj.getHospitalizationCashlessApprovedamt();
		}
		return 0L;
	}
	 
	 public void setFaAppAmountBSIAlert(){
		 if(financialButtonObj != null){
			 financialButtonObj.setBalanceSumInsuredAlert(Long.parseLong(bean.getConsolidatedFinalAppAmnt()));
		 }
	 }
	 
	 public void checkForPremiumAndConsolidateAmt(){
		 Long input1 =0l;
			String input2 = null;
			java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
			java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();
					
		    java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
		    java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
		    Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt());
		  //added for instalment issue by noufel
		    if( bean.getClaimDTO() != null && bean.getClaimDTO().getClaimType() != null 
		    		&& bean.getClaimDTO().getClaimType().getId() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
		    	Integer premiumAmount = bean.getPolicyInstalmentPremiumAmt().intValue();
		    	approvedAmt = approvedAmt + premiumAmount;
		    }
		    
		    if(bean.getIsPremiumInstAmtEql()){
		    	approvedAmt = hospitalizaionObj.getPremiumAmountEqualZero();
		    }
			Map<String, String> getPolicyInstallmentDetails = dbCalculationService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
					bean.getPolicyInstalmentPremiumAmt(),admissionDate,approvedAmt.doubleValue(),
					instalmentDate,input1,input2);
			 if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
				 bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
				 bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
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
}
