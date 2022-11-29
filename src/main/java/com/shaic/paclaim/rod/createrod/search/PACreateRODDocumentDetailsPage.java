package com.shaic.paclaim.rod.createrod.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.claim.rod.wizard.forms.popup.ChangeHospitalViewImpl;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.OptionalCoversTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAPamentQueryDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PARODQueryDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAReconsiderRODRequestListenerTable;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PACreateRODDocumentDetailsPage  extends ViewComponent{

	
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private PARODQueryDetailsTable rodQueryDetails;
	
	@Inject
	public PAPamentQueryDetailsTable paymentQueryDetails;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	/***
	 * Change Hospital Popup window created by Yosuva
	 */
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@Inject
	private Instance<ChangeHospitalViewImpl> changeHospitalInstance;
	
	private ChangeHospitalViewImpl changeHospitalObj;
	
	/*@Inject
	private CreateRODReconsiderRequestTable reconsiderRequestDetails;*/
	@Inject
	private PAReconsiderRODRequestListenerTable reconsiderRequestDetails;

	
/*	@Inject
	private Instance<DocumentCheckListValidationTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationTable documentCheckListValidation;*/
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	@Inject
	private AddOnCoversTable addOnCoversTable;
	
	@Inject
	private OptionalCoversTable optionalCoversTable;
	
	private VerticalLayout optionalCoverLayout;
	
	private OptionGroup accidentOrDeath;
	
	private DateField accidentOrDeathDate;
	
	private ComboBox cmbDocumentType;
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private DateField documentsReceivedDate;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	//private TextField txtAdditionalRemarks;
	private TextArea txtAdditionalRemarks;
	
	private DateField dateOfAdmission;
	
	private TextField txtHospitalName;
	
	private ComboBox cmbInsuredPatientName;
	
	private CheckBox chkDeath;
	
	private CheckBox chkPermanentPartialDisability;
	
	private CheckBox chkPermanentTotalDisability;
	
	private CheckBox chkTemporaryTotalDisability;
	
	private CheckBox chkHospitalExpensesCover;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private VerticalLayout documentDetailsPageLayout;
	
	private OptionGroup optPaymentMode;
	
	public ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirName;
	/*private TextField txtLegalHeirFirstNmme;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;*/
	
	private TextField txtAccountPref;
	
	private TextField txtAccType;
	
	private Button btnAccPrefSearch;
	
	private HorizontalLayout accPrefLayout;
	
	private TextField txtRelationship;
	
	private TextField txtNameAsPerBank;
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private TextField txtIfscCode;
	
	private Button btnIFCSSearch;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private TextField txtReasonForChangeInDOA;
	
	//private FormLayout formLayout1 = null;
	
	private Button changeHospitalBtn;
	
	private UpdateHospitalDetailsDTO changeHospitalDto;
	
	private GWizard wizard;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout paymentDetailsLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanItemContainer<SelectValue> reconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	
	private BeanItemContainer<SelectValue> documentType;
	
	private OptionGroup workOrNonWorkSpace;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	
	private BeanItemContainer<SelectValue> payeeNameList;
	
	private BeanItemContainer<SelectValue> insuredPatientList;
	
	private HorizontalLayout insuredLayout;
	
	private List<DocumentDetailsDTO> docsDetailsList ;
	
	private TextField txtHospitalizationClaimedAmt;
	
	private TextField txtPreHospitalizationClaimedAmt;
	
	private TextField txtPostHospitalizationClaimedAmt;
	
	private TextField txtBenifitClaimedAmnt;
	 
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	 
	//private  DocumentDetailsDTO docDTO ;
	 private List<DocumentDetailsDTO> docDTO;
	 
	 private static boolean isQueryReplyReceived = false; 
	 
	 @Inject
	 private ViewDetails viewDetails;
	 
	 private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	 
	 
	 public Map<String,Boolean> reconsiderationMap = new HashMap<String, Boolean>();
	
	 public String hospitalizationClaimedAmt = "";
		
	 public String preHospitalizationAmt = "";
		
	 public String postHospitalizationAmt= "";
	 
	 public String benifitClaimedAmount = "";
	 
	 
	 private Boolean isReconsider ;
	 public ReconsiderRODRequestTableDTO reconsiderDTO = null;
	 
	 private List<UploadDocumentDTO> uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
	 
	 private String chqEmailId;
	 
	 private String chqPanNo;
	 
	 private String chqPayableAt;
	 
	 private String chqResonForChange;
	 
	 private String chqLegalHeirName;
	 
	 private String chqPayeeName;
	 
	 private String bnqAccntNo;
	 private String bnqIFSCode;
	 
	 private String bnqBranch;
	 
	 private String bnqBankName;
	 
	 private String bnqCity;
	 
	 private ComboBox cmbReasonForReconsideration;
	 
	 private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 
	 private Boolean isFinalEnhancement = false;
	 
	 private DateField dateOfDischarge;
	 
	 private Boolean isReasonForChangeReq = false;
	 
	 private OptionGroup optPaymentCancellation;
	 
	 private HorizontalLayout queryDetailsLayout;
	 
	 private VerticalLayout addOnBenifitLayout;
	 
	 @Inject
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Button btnPopulatePreviousAccntDetails;
	 
	 
	 private Window populatePreviousWindowPopup;
	 
	 private Button btnOk;
	 
	 private Button btnCancel;
	 
	 private VerticalLayout previousPaymentVerticalLayout;
	 
	 private HorizontalLayout previousAccntDetailsButtonLayout;
	 
	 public HorizontalLayout documentDetailsLayout;
		
	public VerticalLayout docCheckListLayout;
	
	public Map<String, Object> referenceMap = new HashMap<String, Object>();
		
	private Boolean isValid = false;
	
	private String coverName = "";
	
	
	private TextField organisationName;
	
	 private TextField sumInsured;
		
	 private TextField parentName;
		
	 private DateField dateOfBirth;
		
	 private TextField riskName;
		
	 private TextField age;
	 
	 private TextField sectionOrclass;
		
	 private ComboBox cmbCategory;
		
	 private DateField riskDOB;
		
	 private TextField riskAge;

	 private DateField dateOfAccident;
		
	private DateField dateOfDeath;
		
	private DateField dateOfDisablement;
	
	 @EJB
		private IntimationService intimationService;
	 
	 @EJB
		private PolicyService policyService;
	 
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	private FormLayout legaHeirLayout ;
	 
	// private static boolean isReconsiderRequestFlag = false;
	
	//private TextField txtHospitalPayeeName;
	  
	//private Panel billClassificationPanel;
	
	
	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
		
	private BeanItemContainer<SelectValue> relationshipContainer;

	private VerticalLayout legalHeirLayout;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private CheckBox chkNomineeDeceased;
	
	@PostConstruct
	public void init() {
		//reconsiderRequestDetails.init();
	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		/*this.chqEmailId = bean.getDocumentDetails().getEmailId();
		this.chqPanNo = bean.getDocumentDetails().getPanNo();
		this.chqResonForChange = bean.getDocumentDetails().getReasonForChange();
		this.chqPanNo = bean.getDocumentDetails().getPanNo();
		this.chqLegalHeirName = bean.getDocumentDetails().getLegalFirstName();
		this.chqPayableAt = bean.getDocumentDetails().getPayableAt();
		this.bnqAccntNo = bean.getDocumentDetails().getAccountNo();
		this.bnqIFSCode = bean.getDocumentDetails().getIfscCode();
		this.bnqBankName  = bean.getDocumentDetails().getBankName();
		this.bnqBranch = bean.getDocumentDetails().getBranch();
		this.bnqCity = bean.getDocumentDetails().getCity();*/
		bankDetailsTableObj =  bankDetailsTableInstance.get();
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
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
					if(bean.getIsPEDInitiated()){
						alertMessageForPED();
					}	
				}
			});
		}
	
	public Component getContent() {
		
//		if(this.bean.getClaimCount() >1){
//			alertMessageForClaimCount(this.bean.getClaimCount());
//		}else 
		
		if(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag() != null
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		
		else if(bean.getIsPEDInitiated()){
			alertMessageForPED();
		}
		if(bean.getClaimDTO().getNewIntimationDto().getHospitalDto()	 != null 
				&& bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");
			}
		}
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		isReconsider= false;
				
		documentDetailsPageLayout = new VerticalLayout();
	//	documentDetailsPageLayout.setSpacing(false);
	//	documentDetailsPageLayout.setMargin(true);
		
		reconsiderationLayout = new VerticalLayout();		
		reconsiderationLayout.setSpacing(false);
		
		documentDetailsLayout = new HorizontalLayout(); 
		documentDetailsLayout.addComponent(buildDocumentDetailsLayout());
		documentDetailsLayout.setCaption("Document Details");		
	//	documentDetailsLayout.setWidth("100%");
		//documentDetailsLayout.setMargin(true);
		VerticalLayout addOnCoversLayout = new VerticalLayout();	
		addOnCoversLayout.addComponent(buildAddOncoversDetailsLayout());
		addOnCoversLayout.setSpacing(true);
		
		VerticalLayout benifitLayout  = new VerticalLayout();
		benifitLayout.addComponent(buildBenefitLayout());
		benifitLayout.setSpacing(true);
		
		VerticalLayout optionalCoversLayout = new VerticalLayout();	
		optionalCoversLayout.addComponent(buildOptionalCoversDetailsLayout());
		optionalCoversLayout.setSpacing(true);
		
		
		VerticalLayout reconsiderLayout  = new VerticalLayout(buildReconsiderationLayout());		
		reconsiderLayout.setSpacing(true);
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.PA_CREATE_ROD,SHAConstants.PA_LOB);
		documentCheckListValidation.init();
	//	documentCheckListValidation.init("", false);
		//documentCheckListValidation.setReference(this.referenceData);
		
		docCheckListLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListLayout.setCaption("Checklist Validation");
	//	docCheckListLayout.setWidth("10%");
		docCheckListLayout.setHeight("70%");

		
		changeHospitalBtn = new Button("Change Hospital");
		changeHospitalBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		changeHospitalBtn.setWidth("120px");
		changeHospitalBtn.addStyleName(ValoTheme.BUTTON_LINK);
		
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) 
		{
			if(null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()) 
					&& null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto() &&  null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalType() &&
				/*("Network").equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalTypeValue()))*/
			(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalType().getId()).equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL))
			{
				changeHospitalBtn.setEnabled(true);
			}
			else
			{	
			changeHospitalBtn.setEnabled(false);
			}
		}
		else if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) 
		{
			changeHospitalBtn.setEnabled(false);
		}
		else
		{
			changeHospitalBtn.setEnabled(true);
		}
		if(ReferenceTable.PA_LOB_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getLobId().getId())){
			changeHospitalBtn.setEnabled(false);
		}
		
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
		{
			cmbInsuredPatientName.setEnabled(true);
		}
		else if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			cmbInsuredPatientName.setEnabled(false);
		}
		
		if(null != bean.getClaimDTO().getClaimTypeValue() && SHAConstants.CASHLESS_CLAIM_TYPE.equalsIgnoreCase(bean.getClaimDTO().getClaimType().getValue())){
			
			accidentOrDeath.setEnabled(false);			
		}
		
		
		FormLayout fLayout = new FormLayout(dateOfAdmission,dateOfDischarge);
		fLayout.setMargin(false);
		fLayout.setSpacing(false);
		fLayout.setWidth("40%");
		FormLayout fLayout1 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
		fLayout1.setMargin(false);
		fLayout1.setSpacing(false);
		fLayout1.setWidth("40%");
		FormLayout fLayout2 = new FormLayout(changeHospitalBtn);
		fLayout2.setWidth("20%");
		//insuredLayout  = new HorizontalLayout(new FormLayout(dateOfAdmission,dateOfDischarge),new FormLayout(), new FormLayout(txtHospitalName, cmbInsuredPatientName),new FormLayout(changeHospitalBtn));
		//HorizontalLayout insuredLayout  = new HorizontalLayout(new FormLayout(dateOfAdmission),new FormLayout(txtHospitalName, cmbInsuredPatientName),changeHospitalBtn);
		insuredLayout  = new HorizontalLayout(fLayout,fLayout1,fLayout2);
		insuredLayout.setWidth("100%");
		insuredLayout.setMargin(false);
		insuredLayout.setSpacing(false);
		//insuredLayout.setWidth("5px");
	/*	VerticalLayout docCheckListValidationLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListValidationLayout.setCaption("Checklist Validation");
		docCheckListValidationLayout.setMargin(true);*/
		//docCheckListValidationLayout.setMargin(true);
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Information / Request");
//		paymentDetailsLayout.setSpacing(true);
//		paymentDetailsLayout.setMargin(true);
		
		
		btnIFCSSearch = new Button();
	
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		
		if(null != bean.getDocumentDetails().getDocumentReceivedFromValue() && SHAConstants.DOC_RECEIVED_FROM_HOSPITAL.equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
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
		
		previousAccntDetailsButtonLayout = new HorizontalLayout(btnOk,btnCancel);
		
		
		
		previousAccountDetailsTable.init("Previous Account Details", false, false);
		previousAccountDetailsTable.setPresenterString(SHAConstants.CREATE_ROD);
		previousPaymentVerticalLayout = new VerticalLayout();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);
		
		
		//paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
		
	//	documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,reconsiderationLayout,insuredLayout,documentCheckListValidation,paymentDetailsLayout);
		documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,benifitLayout,addOnCoversLayout,optionalCoversLayout,reconsiderationLayout,insuredLayout,docCheckListLayout,paymentDetailsLayout);
		//documentDetailsPageLayout.setWidth("2000px");
		documentDetailsPageLayout.setMargin(true);
		
		SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
		if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				   && docRecFromValue != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
			
			buildNomineeLayout();
		}
		

		if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			documentDetailsLayout.setEnabled(false);
			benifitLayout.setEnabled(false);
			addOnCoversLayout.setEnabled(false);
			optionalCoversLayout.setEnabled(false);
			insuredLayout.setEnabled(false);
			docCheckListLayout.setEnabled(false);
			paymentDetailsLayout.setEnabled(false);
			
		}
		
		addListener();
		
		setTableValues();
		addBenefitLister();
	//	addBillClassificationLister();
		
		if(null != this.bean.getDocumentDetails().getDocumentReceivedFromValue() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			fireViewEvent(CreateRODDocumentDetailsPresenter.HOSPITAL_PAYMENT_DETAILS, this.bean);
		}
		if(null != dateOfAdmission)
		validateAdmissionDate(dateOfAdmission.getValue());
		
		if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
		{
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		else
		{
			addOnCoversTable.setEnabled(true);
			optionalCoversTable.setEnabled(true);
		}

		return documentDetailsPageLayout;
	}
	
	private void getDocumentTableDataList()
	{
		fireViewEvent(DocumentDetailsPresenter.SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}
	
	
	
	
	private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
   		FormLayout firstForm = new FormLayout(successLabel,homeButton);
		Panel panel = new Panel(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
		panel.setHeight("103px");
//		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
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
				if(bean.getIsPEDInitiated()){
					alertMessageForPED();
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		
		
		accidentOrDeath = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);
		//	optPaymentMode = new OptionGroup("Payment Mode");
		accidentOrDeath.setRequired(true);
		accidentOrDeath.addItems(getReadioButtonOptions());
		accidentOrDeath.setItemCaption(true, "Accident");
		accidentOrDeath.setItemCaption(false, "Death");
		accidentOrDeath.setStyleName("horizontal");
		//Vaadin8-setImmediate() accidentOrDeath.setImmediate(true);
		accidentOrDeath.setReadOnly(false);
		
		accidentOrDeathDate = binder.buildAndBind("Date of Accident / Death" , "accidentOrDeathDate" , DateField.class);
		accidentOrDeathDate.setEnabled(true);
		//Vaadin8-setImmediate() accidentOrDeathDate.setImmediate(true);
			
		cmbDocumentType = binder.buildAndBind("Document Type" , "documentType" , ComboBox.class);
		cmbDocumentType.setEnabled(true);
		//Vaadin8-setImmediate() cmbDocumentType.setImmediate(true);
		
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");		
		
		/*SelectValue selectFreshDocument = new SelectValue();
		selectFreshDocument.setId(null);
		selectFreshDocument.setValue("Fresh Document");

		SelectValue selectReconsiderationDocument = new SelectValue();
		selectReconsiderationDocument.setId(null);
		selectReconsiderationDocument.setValue("Reconsideration Document");
		
		SelectValue selectQueryReplydocument = new SelectValue();
		selectQueryReplydocument.setId(null);
		selectQueryReplydocument.setValue("Query Reply Document");
		
		SelectValue selectPaymentQueryReply = new SelectValue();
		selectPaymentQueryReply.setId(null);
		selectPaymentQueryReply.setValue("Payment Query Reply");
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectFreshDocument);
		selectVallueList.add(selectReconsiderationDocument);
		selectVallueList.add(selectQueryReplydocument);
		selectVallueList.add(selectPaymentQueryReply);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbDocumentType.setContainerDataSource(selectValueContainer);
		cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentType.setItemCaptionPropertyId("value");
*/
		
		
		
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From" , "documentsReceivedFrom" , ComboBox.class);
		cmbDocumentsReceivedFrom.setEnabled(true);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", DateField.class);
		documentsReceivedDate.setValue(new Date());
		documentsReceivedDate.setEnabled(true);
		
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt", "modeOfReceipt", ComboBox.class);
		//Vaadin8-setImmediate() cmbModeOfReceipt.setImmediate(true);
		
		cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		
		
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		/*loadReasonForReconsiderationDropDown();*/
		
		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks","additionalRemarks",TextArea.class);
		txtAdditionalRemarks.setMaxLength(4000);
		
		
		organisationName = (TextField) binder.buildAndBind("Organisation Name", "organisationName", TextField.class);
		organisationName.setEnabled(false);
		
		sumInsured = (TextField) binder.buildAndBind("Sum Insured", "paSumInsured", TextField.class);
		sumInsured.setEnabled(false);
		
		parentName = (TextField) binder.buildAndBind("Parent Name", "parentName", TextField.class);
		//parentName.setEnabled(false);
		
		dateOfBirth = (DateField) binder.buildAndBind("Parent(DOB)", "dateOfBirth", DateField.class);
		//dateOfBirth.setEnabled(false);
		
		riskName = (TextField) binder.buildAndBind("Risk Name", "riskName", TextField.class);
		//riskName.setEnabled(false);
		
		age = (TextField) binder.buildAndBind("Parent(Age)", "age", TextField.class);
		//age.setEnabled(false);		
		
		
		riskDOB = (DateField) binder.buildAndBind("Risk(DOB)", "gpaRiskDOB", DateField.class);
		
		riskAge = (TextField) binder.buildAndBind("Risk(Age)", "gpaRiskAge", TextField.class);
		
		sectionOrclass = (TextField) binder.buildAndBind("Section/Class", "gpaSection", TextField.class);
		sectionOrclass.setEnabled(false);
		
		cmbCategory = (ComboBox) binder.buildAndBind("Category", "gpaCategory", ComboBox.class);	
		cmbCategory.setEnabled(false);
		
		BeanItemContainer<SelectValue> selectValueForCategory = dbCalculationService.getGPACategory(bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
		setDropDownValues(selectValueForCategory);
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n (Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
		txtBenifitClaimedAmnt = binder.buildAndBind("Amount Claimed\n (Benefit)", "benifitClaimedAmount",TextField.class);
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		
		txtBenifitClaimedAmnt.setEnabled(true);
		txtBenifitClaimedAmnt.setNullRepresentation("");
		txtBenifitClaimedAmnt.setMaxLength(15);
		
		CSValidator hospClaimedAmtValidator = new CSValidator();
		hospClaimedAmtValidator.extend(txtHospitalizationClaimedAmt);
		hospClaimedAmtValidator.setRegExp("^[0-9]*$");
		hospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator preHospClaimedAmtValidator = new CSValidator();
		preHospClaimedAmtValidator.extend(txtPreHospitalizationClaimedAmt);
		preHospClaimedAmtValidator.setRegExp("^[0-9]*$");
		preHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator postHospClaimedAmtValidator = new CSValidator();
		postHospClaimedAmtValidator.extend(txtPostHospitalizationClaimedAmt);
		postHospClaimedAmtValidator.setRegExp("^[0-9]*$");
		postHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator benefitAmtValidator = new CSValidator();
		benefitAmtValidator.extend(txtBenifitClaimedAmnt);
		benefitAmtValidator.setRegExp("^[0-9]*$");
		benefitAmtValidator.setPreventInvalidTyping(true);
		
	/*	if(null != this.bean.getDocumentDetails())
		{
			if(null != this.bean.getDocumentDetails().getHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationClaimedAmount()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
				
			}
			else
			{
				txtHospitalizationClaimedAmt.setEnabled(false);
			}
			if(null != this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount()))
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
			}
			else
			{
				txtPreHospitalizationClaimedAmt.setEnabled(false);
			}
			if(null != this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount()))
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
			}
			else
			{
				txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
		}*/
		
		dateOfAdmission = binder.buildAndBind("Date of\nAdmission","dateOfAdmission",DateField.class);
		dateOfAdmission.setEnabled(false);
		
	
		dateOfDischarge = binder.buildAndBind("Date Of\nDischarge", "dateOfDischarge", DateField.class);
		/*if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
				  ))*/
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) 
						{
			dateOfDischarge.setEnabled(true);
			//txtReasonForChange.setEnabled(true);
		}
		else
		{
			dateOfDischarge.setEnabled(false);
			//txtReasonForChange.setEnabled(false);
		}
//		if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
//		{
//			dateOfAdmission.setEnabled(false);
//		}
//		
//		else if (
//						((null != this.bean.getDocumentDetails().getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag())) ||
//						(null != this.bean.getDocumentDetails().getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag())) ||
//						(null != this.bean.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getLumpSumAmountFlag())) ||
//						(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag())) ||
//						(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag())))
//				&& ((null != this.bean.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag())) ||
//						(null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))))
//		{
//			dateOfAdmission.setEnabled(false);
//		}
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
				  ))
				
		{
			dateOfAdmission.setEnabled(true);
		}
		
		/*//Added for issue 1777
		 if (
				(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && null != this.bean.getDocumentDetails().getDocumentReceivedFromValue() 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentsReceivedFrom().getValue() &&  
				
		{
			dateOfAdmission.setEnabled(true);
		}*/
		
		/*if(null != this.bean.getClaimDTO().getNewIntimationDto().getAdmissionDate())
		{
			dateOfAdmission.setValue(this.bean.getClaimDTO().getNewIntimationDto().getAdmissionDate());
		}*/
		addDateOfAdmissionListener();
		if(null != bean.getDocumentDetails() && null != bean.getDocumentDetails().getDateOfAdmission())
		{
			dateOfAdmission.setValue(bean.getDocumentDetails().getDateOfAdmission());
		}
		
		txtHospitalName = binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		
		if(null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto())
		{
			txtHospitalName.setValue(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName());
		}
		
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
				  ))
				{
					txtHospitalName.setEnabled(true);
				}
		else
		{
			txtHospitalName.setEnabled(false);
		}
		
		cmbInsuredPatientName = binder.buildAndBind("Insured Patient\nName", "insuredPatientName", ComboBox.class);
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		if (accidentOrDeath.getValue() != null && !(accidentOrDeath.getValue()).equals("") && !(Boolean)accidentOrDeath.getValue()) {
			setRequiredAndValidation(dateOfDeath);
			mandatoryFields.add(dateOfDeath);
		} else {
			dateOfDeath.setRequired(false);
			mandatoryFields.remove(dateOfDeath);
			dateOfDeath.setValue(null);
		}
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);		
		
		
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			dateOfAdmission.setReadOnly(true);
			dateOfAdmission.setEnabled(false);
			
			txtHospitalName.setReadOnly(true);
			txtHospitalName.setEnabled(false);
		}*/
		
		FormLayout detailsLayout1 = new FormLayout(accidentOrDeath,dateOfAccident,cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbDocumentType,workOrNonWorkSpace);
		//detailsLayout1.setMargin(true);
	//	detailsLayout1.setSpacing(true);
		FormLayout detailsLayout2 = new FormLayout(dateOfDeath,dateOfDisablement,cmbModeOfReceipt,txtAdditionalRemarks,txtBenifitClaimedAmnt);
		detailsLayout2.setMargin(true);
	//	detailsLayout2.setSpacing(true);
		//HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,new FormLayout(),new FormLayout(),detailsLayout2);
		FormLayout detailsLayout3 = new FormLayout(organisationName,sumInsured,parentName,dateOfBirth,age,riskName,riskDOB,riskAge);
		detailsLayout3.setMargin(true);
		detailsLayout3.setCaption("UNNAMED RISK DETAILS");
		
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		
		if(null != bean.getClaimDTO() && null != bean.getClaimDTO().getNewIntimationDto() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy()&&
				null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey( bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){					
			
		 docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2,detailsLayout3);
		}
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
	//	docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		docDetailsLayout.setWidth("100%");
		
		setRequiredAndValidation(cmbDocumentsReceivedFrom);
		setRequiredAndValidation(documentsReceivedDate);
		setRequiredAndValidation(cmbModeOfReceipt);
		setRequiredAndValidation(cmbDocumentType);
		setRequiredAndValidation(txtBenifitClaimedAmnt);
		
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		mandatoryFields.add(cmbDocumentType);
		mandatoryFields.add(txtBenifitClaimedAmnt);
		//mandatoryFields.add(dateOfDeath);
		
		cmbDocumentsReceivedFrom.setReadOnly(true);
		documentsReceivedDate.setReadOnly(true);
		cmbModeOfReceipt.setReadOnly(true);
		//txtAdditionalRemarks.setReadOnly(true);	
		
	/*	if(null != cmbDocumentsReceivedFrom)
		{
			SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
			if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_FROM_INSURED.equals(selValue.getId()))){
				
				workOrNonWorkSpace.setEnabled(true);				
			}
			else
			{
				workOrNonWorkSpace.setEnabled(false);	
			}
		}*/
		
		return docDetailsLayout;
	}
	
	
	private void loadReasonForReconsiderationDropDown()
	{
		 if(null != cmbReasonForReconsideration)
		 {
			 String reasonForReconsideration = this.bean.getDocumentDetails().getReasonForReconsiderationRequestValue();
			 cmbReasonForReconsideration.setContainerDataSource(reasonForReconsiderationRequest);
			 cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			 cmbReasonForReconsideration.setItemCaptionPropertyId("value");
			 if(null != reasonForReconsiderationRequest && (null != reasonForReconsideration && !("").equalsIgnoreCase(reasonForReconsideration)))
			 {
				 for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
				 	{

						if ((reasonForReconsideration).equalsIgnoreCase(reasonForReconsiderationRequest.getIdByIndex(i).getValue()))

						{
							this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
						}
					}
			 }
		 }
	}
	
	private HorizontalLayout buildBenefitLayout()
	{
		
		chkDeath = binder.buildAndBind("Death", "death", CheckBox.class);
		//Vaadin8-setImmediate() chkDeath.setImmediate(true);
		
		//chkhospitalization.setEnabled(false);
		chkPermanentPartialDisability = binder.buildAndBind("Permanent Partial Disability", "permanentPartialDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentPartialDisability.setImmediate(true);
		//chkPartialHospitalization.setEnabled(false);
		chkPermanentTotalDisability = binder.buildAndBind("Permanent Total Disability", "permanentTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentTotalDisability.setImmediate(true);
		
		//chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		chkTemporaryTotalDisability = binder.buildAndBind("Temporary Total Disability", "temporaryTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkTemporaryTotalDisability.setImmediate(true);
		
		
		chkHospitalExpensesCover = binder.buildAndBind("Hospital Expenses Cover", "hospitalExpensesCover", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalExpensesCover.setImmediate(true);
		
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		//chkhospitalization.setEnabled(false);
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(true);
		}
		else
		{
			chkhospitalization.setEnabled(true);
		}
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
		//	chkPreHospitalization.setEnabled(false);
			//chkPostHospitalization.setEnabled(false);
		}
		else if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(true);
			/*if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
			{
				//chkPreHospitalization.setEnabled(true);
				//chkPreHospitalization.setEnabled(false);
				//chkPreHospitalization.setValue(true);
			}
			//chkPreHospitalization.setEnabled(true);
			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
			{
				chkPostHospitalization.setEnabled(true);
				//chkPostHospitalization.setEnabled(false);
				//chkPostHospitalization.setValue(true);
			}
			//chkPostHospitalization.setEnabled(true);
*/		}
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
			{
				SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
				if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
			{
				chkhospitalization.setEnabled(true);
				chkDeath.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkPermanentPartialDisability.setEnabled(false);
				chkPermanentTotalDisability.setEnabled(false);
				chkTemporaryTotalDisability.setEnabled(false);	
				addOnCoversTable.setEnabled(false);
				optionalCoversTable.setEnabled(false);
			}	
			}
			else
			{
				chkhospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(true);
				chkDeath.setEnabled(true);
				chkPermanentPartialDisability.setEnabled(true);
				chkPermanentTotalDisability.setEnabled(true);
				chkTemporaryTotalDisability.setEnabled(true);
				addOnCoversTable.setEnabled(true);
				optionalCoversTable.setEnabled(true);
			}
			
		}
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}
		
		if(SHAConstants.YES.equalsIgnoreCase(this.bean.getDocumentDetails().getReconsiderationRequestValue()))
		{		   
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
			{
				chkhospitalization.setValue(false);
				chkPartialHospitalization.setValue(false);
				chkDeath.setValue(false);
				chkPermanentPartialDisability.setValue(false);
				chkPermanentTotalDisability.setValue(false);
				chkTemporaryTotalDisability.setValue(false);
				txtBenifitClaimedAmnt.setValue(null);
				this.bean.getDocumentDetails().setAddOnCoversList(null);
				this.bean.getDocumentDetails().setOptionalCoversList(null);
				
			}
		
		
		}
		
		/*Below code only for Product - MED-PRD-074*/
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
			chkhospitalization.setEnabled(false);
			chkTemporaryTotalDisability.setEnabled(false);
		}
		
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY)){
			chkhospitalization.setEnabled(false);
			chkDeath.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
			chkPermanentPartialDisability.setEnabled(false);
			chkPermanentTotalDisability.setEnabled(true);
			chkTemporaryTotalDisability.setEnabled(false);
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		
		HorizontalLayout benifitClassificationLayout = new HorizontalLayout(chkDeath,chkPermanentPartialDisability,chkPermanentTotalDisability,chkTemporaryTotalDisability,chkhospitalization,chkPartialHospitalization);
	
		
	//	HorizontalLayout benifitClassificationLayout = new HorizontalLayout(benifitClassificationLayout1);
		//billClassificationLayout.setCaption("Document Details");
		benifitClassificationLayout.setMargin(true);
		benifitClassificationLayout.setCaption("Benefits");
		benifitClassificationLayout.setSpacing(true);
	//	billClassificationLayout.setMargin(true);
	//	billClassificationLayout.setWidth("110%");
		//addBillClassificationLister();
		
		return benifitClassificationLayout;
	}
	
	
	
	private void displayAmountClaimedDetails()
	{
		if(null != this.bean.getDocumentDetails())
		{
			if(null != this.bean.getDocumentDetails().getBenifitClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getBenifitClaimedAmount()))
			{
				txtBenifitClaimedAmnt.setEnabled(true);
				txtBenifitClaimedAmnt.setValue(this.bean.getDocumentDetails().getBenifitClaimedAmount());
			}
			
		}
	}
	
	private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		if(null != optPaymentMode && optPaymentMode.getValue() != null && (boolean) optPaymentMode.getValue())
		{
			buildBankTransferLayout();
		}
		//unbindField(optPaymentMode);
		paymentModeListener();
	//	//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		//optPaymentMode.select(true);
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		cmbPayeeName.setEnabled(true);
		
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))*/
		
		
		if(null == this.bean.getDocumentDetails().getPaymentMode())
		{
			if(null != this.bean.getClaimDTO() && 
					((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())))
					&&  ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
			{
				if(null != this.bean.getDocumentDetails() && null != this.bean.getDocumentDetails().getPaymentModeFlag() &&
						(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
				{
					
					
					if(optPaymentMode != null){
						optPaymentMode.setValue(true);
					}
					//this.bean.getDocumentDetails().setPaymentModSelectFlag("Cheque");
				}
				else
				{
					if(optPaymentMode != null){
						optPaymentMode.setValue(false);
					}
					//this.bean.getDocumentDetails().setPaymentModSelectFlag("BankTransfer");
				}
				/**
				 * The below if block is added for issue #4128.
				 * If a record comes from convert claim to create ROD queue,
				 * then the claim type will be reimbursment and doc received from would be
				 * hospital. In those cases, the payment mode should be editable. Hence
				 * adding the below block.
				 * */
				if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
				{
					if(optPaymentMode != null){
						 optPaymentMode.setReadOnly(false);
						 optPaymentMode.setEnabled(true);
					}
					 btnIFCSSearch.setEnabled(true);
					
				}
				else
				{
					if(optPaymentMode != null){
						optPaymentMode.setReadOnly(true);
						optPaymentMode.setEnabled(false);
					}
				 btnIFCSSearch.setEnabled(false);
				// btnIFCSSearch.setWidth("5px");
				}
			}
			
			else
			{
				optPaymentMode.setValue(true);
				//this.bean.getDocumentDetails().setPaymentModSelectFlag("Cheque");
			}
		}
		else if(null != this.bean.getDocumentDetails().getPaymentMode())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPaymentMode();
			/**
			 * The below if block is added to enabling the value change listener
			 * for option group. When the screen is painted for first time, the 
			 * payment mode will be null. When we proceed to next step a value
			 * is assigned in bean. When again traversing back, the same value
			 * is set to the option group, there by ,value change listener is not
			 * invoked. Hence to invoke value change listner, twice the value
			 * is set to optiongroup. This is like, selecting and unselecting the
			 * group. If the value is true, we first set false and again we set to true
			 * and vice versa.
			 * */
			if(val)
			{
				optPaymentMode.setValue(false);
			}
			else 
			{
				optPaymentMode.setValue(true);
			}
			//unbindField(optPaymentMode);
			optPaymentMode.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPayableAt()){
		txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());	
		}
		
		

		/* if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			 
		}*/
		
		//buildPaymentsLayout();
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	
	private HorizontalLayout buildChequePaymentLayout()
	{
		/*cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		cmbPayeeName.setEnabled(true);*/
		//txtHospitalPayeeName = (TextField) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		//cmbPayeeName.setRequired(true);
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailId" , TextField.class);
		//txtEmailId.setValue(this.chqEmailId);
//		CSValidator emailValidator = new CSValidator();
//		emailValidator.extend(txtEmailId);
//		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
//		emailValidator.setPreventInvalidTyping(true);
//		txtEmailId.setMaxLength(100);
		txtEmailId.setEnabled(true);
		
		//txtEmailId.setRequired(true);
		
		txtReasonForChange = (TextField) binder.buildAndBind("Reason For Change\n(Payee Name)", "reasonForChange", TextField.class);
	//	txtReasonForChange.setValue(this.chqResonForChange);
		
		CSValidator reasonForChangeValidator = new CSValidator();
		reasonForChangeValidator.extend(txtReasonForChange);
		reasonForChangeValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		reasonForChangeValidator.setPreventInvalidTyping(true);
		txtReasonForChange.setMaxLength(100);
		txtReasonForChange.setEnabled(false);
		
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
	//	txtPanNo.setValue(this.chqPanNo);
	
		
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(10);
		txtPanNo.setEnabled(true);
		
		txtLegalHeirName = (TextField) binder.buildAndBind("Legal Heir Name","legalFirstName",TextField.class);
		//txtLegalHeirName.setValue(this.chqLegalHeirName);
		txtLegalHeirName.setEnabled(false);
		txtLegalHeirName.setMaxLength(30);
		
		/*txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);
		*/
		
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		//txtPayableAt.setValue(this.chqPayableAt);
	//	mandatoryFields.add(txtPayableAt);
		txtPayableAt.setMaxLength(50);
		txtPayableAt.setEnabled(true);
/*		txtPayableAt.setRequired(true);
*/		/*
		setRequiredAndValidation(txtPayableAt);
		txtPayableAt.setRequired(true);
		showOrHideValidation(false);*/
		
		CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z /]*$");
		payableAtValidator.setPreventInvalidTyping(true);;
		
		
		txtPayableAt.setEnabled(true);
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) &&  
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))*/
		{	
			
			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getEmailId()))
			{
				txtEmailId.setReadOnly(true);
				txtEmailId.setEnabled(false);
			}
			else
			{
				txtEmailId.setReadOnly(false);
				txtEmailId.setEnabled(true);
			}
			
			
			txtReasonForChange.setReadOnly(true);
			txtReasonForChange.setEnabled(false);
			
			
			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getPanNo()))
			{
				txtPanNo.setReadOnly(true);
				txtPanNo.setEnabled(false);
			}
			else
			{
				txtPanNo.setReadOnly(false);
				txtPanNo.setEnabled(true);

			}
			txtLegalHeirName.setReadOnly(true);
			txtLegalHeirName.setEnabled(false);
			
/*			txtLegalHeirMiddleName.setReadOnly(true);
			txtLegalHeirMiddleName.setEnabled(false);
			
			txtLegalHeirLastName.setReadOnly(true);
			txtLegalHeirLastName.setEnabled(false);*/
			
			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getPanNo()))
			{
				txtPayableAt.setReadOnly(true);
				txtPayableAt.setEnabled(false);
			}
			else
			{
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			
			cmbPayeeName.setEnabled(false);
			//cmbPayeeName.setReadOnly(true);
			
			txtReasonForChange.setEnabled(false);
			txtReasonForChange.setReadOnly(true);
			
		}
		else if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			txtEmailId.setReadOnly(false);
			//txtEmailId.setValue(null);
			txtEmailId.setNullRepresentation("");
			txtReasonForChange.setReadOnly(false);
		//	txtReasonForChange.setValue(null);
			txtReasonForChange.setNullRepresentation("");
			
			//txtPanNo.setValue(null);
			txtPanNo.setReadOnly(false);
		//txtPanNo.setValue(null);
			txtPanNo.setNullRepresentation("");
		//	txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(null);
			txtLegalHeirName.setNullRepresentation("");
			
			accPrefLayout = new HorizontalLayout();
			accPrefLayout.setCaption("Account Preference");
			accPrefLayout.setCaptionAsHtml(true);
			 if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
					 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())){
				 if(txtAccountPref != null){	
					 unbindField(txtAccountPref);
				 }	 
				
				unbindField(txtAccountPref); 
				txtAccountPref = (TextField) binder.buildAndBind("", "accountPreference", TextField.class);
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
			
			
			txtPayableAt.setReadOnly(false);
			//txtPayableAt.setValue(null);
			txtPayableAt.setNullRepresentation("");
			//cmbPayeeName.setReadOnly(false);
			txtReasonForChange.setReadOnly(false);
			//txtReasonForChange.setValue(null);
			txtReasonForChange.setNullRepresentation("");
		}
		
		/**
		 * Below fix added for ticket 4187
		 * */
		
		if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			//txtEmailId.setValue(null);
			txtPanNo.setValue(null);
			txtLegalHeirName.setValue(null);			
		//	txtPayableAt.setValue(null);
			// The below change done for ticket no GALAXYMAIN-6725
			//txtReasonForChange.setValue(null);
			
			if(null != this.bean.getDocumentDetails().getReasonForChange())
			{
				txtReasonForChange.setValue(this.bean.getDocumentDetails().getReasonForChange());
				
			}
		}
		else if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			if(null != this.bean.getDocumentDetails().getEmailId())
			{
				txtEmailId.setValue(this.bean.getDocumentDetails().getEmailId());
				
			}
			
			if(null != this.bean.getDocumentDetails().getPanNo())
			{
				
				/*if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
						txtPanNo.setValue(null);
				else*/
					txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
			}
			
			/*if(null != this.bean.getDocumentDetails().getLegalFirstName())
			{
				txtLegalHeirName.setValue(this.bean.getDocumentDetails().getLegalFirstName());
			
			}*/
			
			if(null != this.bean.getDocumentDetails().getPayableAt())
			{
				txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());
				
			}
			
			if(null != this.bean.getDocumentDetails().getReasonForChange())
			{
				txtReasonForChange.setValue(this.bean.getDocumentDetails().getReasonForChange());
				
			}
		}
		
		
		FormLayout formLayout1 = null;
		FormLayout formLayout2 = null;
		HorizontalLayout hLayout = null;
		
		if(null != this.bean.getDocumentDetails().getPaymentModeFlag() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) 
				&& (ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		
		//if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName);
			formLayout2.setMargin(false);
			hLayout= new HorizontalLayout(formLayout1 ,formLayout2);
			hLayout.setMargin(false);
		}
		else if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && 
				//(null != this.bean.getDocumentDetails().getPaymentMode() && this.bean.getDocumentDetails().getPaymentMode())
				(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())
				)
		{
			/*formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName);
			formLayout2.setMargin(false);*/
			 
			unbindField(txtRelationship);
			txtRelationship = (TextField) binder.buildAndBind("Relationship with Proposer", "payeeRelationship", TextField.class);
			txtRelationship.setNullRepresentation("");
			txtRelationship.setEnabled(false);
			
			unbindField(txtNameAsPerBank);
			txtNameAsPerBank = (TextField) binder.buildAndBind("Name As per Bank Account", "nameAsPerBank", TextField.class);
			txtNameAsPerBank.setNullRepresentation("");
			txtNameAsPerBank.setEnabled(false);
			
			unbindField(txtAccType);
			txtAccType = (TextField) binder.buildAndBind("Account Type", "accountType", TextField.class);
			txtAccType.setNullRepresentation("");
			txtAccType.setEnabled(false);
			
			formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtReasonForChange,txtNameAsPerBank,txtPayableAt,txtPanNo,txtEmailId);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(new Label(), txtRelationship);
			 
			 if(SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())){
				 if((ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(this.bean.getDocumentDetails().getPaymentModeFlag())) {
					 btnAccPrefSearch.setEnabled(true);
				 }
				 else {
					 btnAccPrefSearch.setEnabled(false);
				 }
				accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
				accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
				formLayout2.addComponent(accPrefLayout);
			 }
			 
			 formLayout2.addComponents(txtAccType,txtLegalHeirName);
			 formLayout2.setMargin(false);
			
			 hLayout = new HorizontalLayout(formLayout1 ,formLayout2);
			 hLayout.setMargin(true);
		}
		else
		{
			TextField dField1 = new TextField();
			dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField1.setReadOnly(true);
			TextField dField2 = new TextField();
			dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField2.setReadOnly(true);
			dField2.setWidth("5px");
			TextField dField3 = new TextField();
			dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField3.setReadOnly(true);
			dField3.setWidth("5px");
			TextField dField4 = new TextField();
			dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField4.setReadOnly(true);
			dField4.setWidth("5px");
			TextField dField5 = new TextField();
			dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField5.setReadOnly(true);
			dField5.setWidth(2,Unit.CM);
			
			/*formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName,dField1,txtBranch);
			formLayout2.setMargin(false);
			HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
			//btnHLayout.setWidth("5%");
			VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
//			btnLayout.setWidth("%");*/
			
			unbindField(txtRelationship);
			txtRelationship = (TextField) binder.buildAndBind("Relationship with Proposer", "payeeRelationship", TextField.class);
			txtRelationship.setNullRepresentation("");
			txtRelationship.setEnabled(false);
			
			unbindField(txtNameAsPerBank);
			txtNameAsPerBank = (TextField) binder.buildAndBind("Name As per Bank Account", "nameAsPerBank", TextField.class);
			txtNameAsPerBank.setNullRepresentation("");
			txtNameAsPerBank.setEnabled(false);
			
			unbindField(txtAccType);
			txtAccType = (TextField) binder.buildAndBind("Account Type", "accountType", TextField.class);
			txtAccType.setNullRepresentation("");
			txtAccType.setEnabled(false);
			
			formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtReasonForChange,txtNameAsPerBank,txtAccntNo,txtBankName,txtCity,txtEmailId);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(new Label(),txtRelationship);
			
			if(SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
			 					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())){
				accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch); 
				accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
				formLayout2.addComponent(accPrefLayout);
			}
			else{
				formLayout2.addComponent(dField1);
			}
			HorizontalLayout btnIfscHLayout = new HorizontalLayout(txtIfscCode,btnIFCSSearch);
			btnIfscHLayout.setCaption("IFSC Code");
			txtIfscCode.setCaption(null);
			
			formLayout2.addComponents(txtAccType,btnIfscHLayout,txtBranch,txtPanNo,txtLegalHeirName);
			
			formLayout2.setMargin(false);
			
			hLayout = new HorizontalLayout(formLayout1 /*,btnLayout*/,formLayout2);
			hLayout.setMargin(true);
//			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		}
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,btnIFCSSearch ,formLayout2);
		//hLayout.setWidth("70%");
	//TODO
//		if(null != txtAccntNo)
//		{
//			mandatoryFields.remove(txtAccntNo);
//		}
//		if(null != txtIfscCode)
//		{
//			mandatoryFields.remove(txtIfscCode);
//		}
		//Added for ticket 4287
		if(null != this.bean.getEmailIdForPaymentMode())
		{
			txtEmailId.setValue(this.bean.getEmailIdForPaymentMode());
		}
		addComboPayeeNameListener();
		
		return hLayout;		
		
	}
	
	private void addComboPayeeNameListener()
	{
		cmbPayeeName
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null == value)
				{	
//						txtLegalHeirName.setEnabled(true);
						txtReasonForChange.setEnabled(true);
				}
				else if((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(value.getValue()))
				{
					txtReasonForChange.setEnabled(false);
				}
				/*else
				{
					txtLegalHeirName.setValue(null);
					txtLegalHeirName.setNullRepresentation("");
//					txtLegalHeirName.setEnabled(true);
					txtReasonForChange.setEnabled(true);
				}*/
				
				if(null != value) {
					txtRelationship.setValue(value.getRelationshipWithProposer());
					txtNameAsPerBank.setValue(value.getNameAsPerBankAccount());
				}
			}				
			
		});
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
//		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
			
				txtAccntNo.setEnabled(false);
		}else {
			txtAccntNo.setEnabled(true);
		}
		
		txtAccntNo.setMaxLength(30);
		
		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-zA-Z 0-9 ]*$");
		accntNoValidator.setPreventInvalidTyping(true);
		
		
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
//		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(false);
		txtIfscCode.setMaxLength(15);
		CSValidator ifscCodeValidator = new CSValidator();
		ifscCodeValidator.extend(txtIfscCode);
		ifscCodeValidator.setRegExp("^[a-zA-Z 0-9]*$");
		ifscCodeValidator.setPreventInvalidTyping(true);
				
		
		txtBranch = (TextField) binder.buildAndBind("Branch", "branch", TextField.class);
		txtBranch.setNullRepresentation("");
		txtBranch.setEnabled(false);
		txtBranch.setMaxLength(100);
		
				
		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		txtBankName.setEnabled(false);
		txtBankName.setMaxLength(100);

		
		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setEnabled(false);
		
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		
		/** 
		 * The above code is commented and below code was added for ticket 1584.
		 * Need to check with naren, whether he has done any changes in setting read only parameter to true or false.
		 * */
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))*/
		{
			/*txtAccntNo.setReadOnly(false);
			txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(false);
			txtCity.setEnabled(false);*/

			/*txtAccntNo.setReadOnly(true);
			txtAccntNo.setEnabled(true);
			
			txtIfscCode.setReadOnly(true);
			txtIfscCode.setEnabled(true);*/
			

			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getAccountNo()))
			{
				txtAccntNo.setReadOnly(true);
				
				if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
						&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
				
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(false);
				}else {
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(true);
				}
				
			}
			else
			{
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(false);
			}
			if(null != bean.getDocumentDetails().getIfscCode() && !("").equals(bean.getDocumentDetails().getIfscCode()))
			{
				txtIfscCode.setReadOnly(true);
				//txtIfscCode.setEnabled(true);
			}
			else
			{
				txtIfscCode.setReadOnly(false);
				//txtIfscCode.setEnabled(false);
			}
			
			txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			txtCity.setEnabled(false);
			
		}
		else if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))

		{
			txtAccntNo.setReadOnly(false);
			//txtAccntNo.setEnabled(true);
			txtAccntNo.setValue(null);
			txtAccntNo.setNullRepresentation("");
			
			txtIfscCode.setReadOnly(false);
		//	txtIfscCode.setEnabled(true);
			txtIfscCode.setValue(null);
			txtIfscCode.setNullRepresentation("");
			
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(false);
			txtBranch.setValue(null);;
			
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(false);
			txtBankName.setValue(null);
			txtBankName.setNullRepresentation("");
			
			txtCity.setReadOnly(false);
			txtCity.setEnabled(false);
			txtCity.setValue(null);
			txtCity.setNullRepresentation("");
			
		}
		/*if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			txtAccntNo.setValue(null);
			txtIfscCode.setValue(null);
			txtBranch.setValue(null);
			txtBankName.setValue(null);
			txtCity.setValue(null);
		}
		else*/ if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{

			if(null != this.bean.getDocumentDetails().getAccountNo())
			{
				txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
			}
			if(null != this.bean.getDocumentDetails().getIfscCode())
			{
				txtIfscCode.setValue(this.bean.getDocumentDetails().getIfscCode());
			}
			if(null != this.bean.getDocumentDetails().getBranch())
			{
				txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
			}
			if(null != this.bean.getDocumentDetails().getBankName())
			{
				txtBankName.setValue(this.bean.getDocumentDetails().getBankName());
			}
			if(null != this.bean.getDocumentDetails().getCity())
			{
				txtCity.setValue(this.bean.getDocumentDetails().getCity());
			}
		}
		
		
		HorizontalLayout lyutIFCS = new HorizontalLayout(txtIfscCode, btnIFCSSearch);
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		lyutIFCS.setComponentAlignment(btnIFCSSearch, Alignment.BOTTOM_CENTER);
		//lyutIFCS.setSpacing(true);
		lyutIFCS.setWidth("88%");
	//	lyutIFCS.setWidth("50%");
		lyutIFCS.setCaption("IFSC Code");
		
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		dField2.setWidth("5px");
		TextField dField3 = new TextField();
		dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField3.setReadOnly(true);
		dField3.setWidth("5px");
		TextField dField4 = new TextField();
		dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField4.setReadOnly(true);
		dField4.setWidth("5px");
		TextField dField5 = new TextField();
		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField5.setReadOnly(true);
		dField5.setWidth(2,Unit.CM);
		
		
		//btnHLayout.setWidth("5%");
		FormLayout btnLayout = new FormLayout(dField5, btnIFCSSearch);
//		btnLayout.setWidth("%");
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		bankTransferLayout1.setComponentAlignment(txtAccntNo, Alignment.MIDDLE_RIGHT);
		/*formLayout1.addComponent(txtAccntNo);
		formLayout1.addComponent(txtIfscCode);
		formLayout1.addComponent(txtBankName);
		formLayout1.addComponent(txtCity);
		formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		*/
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , btnLayout , bankTransferLayout2);
		hLayout.setSpacing(true);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
	//	hLayout.setWidth("80%");
		hLayout.setMargin(false);
//		hLayout.setWidth("50%");
		hLayout.addStyleName("gridBorder");
		
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())
				&& (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())){

			if(btnAccPrefSearch != null){
				btnAccPrefSearch.setEnabled(false);
			}
		}
		else {
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(true);
		}
		
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())) {
		
			if(txtAccntNo != null)
				txtAccntNo.setEnabled(false);
			if(txtPanNo != null)
				txtPanNo.setEnabled(false);
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(false);
		}
		
		/*if(null != txtAccntNo)
		{
			mandatoryFields.add(txtAccntNo);
			setRequiredAndValidation(txtAccntNo);
		}*/
		/*if(null != txtIfscCode)
		{
			mandatoryFields.add(txtIfscCode);
			setRequiredAndValidation(txtIfscCode);
		}*/
		return hLayout;
		
		/*if(null != txtPayableAt)
		{*/
		/*unbindField(txtPayableAt);
		mandatoryFields.remove(txtPayableAt);*/
			//txtPayableAt.setRequired(false);
		//}
		//setRequired(false);
		
			/*txtAccntNo.setVisible(false);
			txtIfscCode.setVisible(false);
			txtBankName.setVisible(false);
			txtCity.setVisible(false);
			btnIFCSSearch.setVisible(false);
			txtBranch.setVisible(false);*/
		
	}
	
	private VerticalLayout buildAddOncoversDetailsLayout()
	{
				
	//	addOnCoversTable.initpresenterString(SHAConstants.CREATE_ROD);
		addOnCoversTable.init("", true);
		addOnCoversTable.setScreenName(SHAConstants.CREATE_ROD);
		//loadQueryDetailsTableValues();
		referenceMap.put("covers", bean.getDocumentDetails().getAdditionalCovers());
		addOnCoversTable.setReference(referenceMap);
		addOnCoversTable.setSizeFull();
		addOnBenifitLayout = new VerticalLayout();
		addOnBenifitLayout.setWidth("100%");
		addOnBenifitLayout.setCaption("Covers (Add On Covers)");
		addOnBenifitLayout.setSpacing(true);
		addOnBenifitLayout.setMargin(false);
		addOnBenifitLayout.addComponent(addOnCoversTable);
		//addOnBenifitLayout.addComponent(cmbReconsiderationRequest);
		if(this.bean.getDocumentDetails().getAddOnCoversList() != null && ! this.bean.getDocumentDetails().getAddOnCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getAddOnCoversList()) {
				addOnCoversTable.addBeanToList(covers);
				
			}
		}
		
		//return rodQueryDetails;
		return addOnBenifitLayout;
	}
	
	
	private VerticalLayout buildOptionalCoversDetailsLayout()
	{
				
	//	addOnCoversTable.initpresenterString(SHAConstants.CREATE_ROD);
		optionalCoversTable.init("", true);
		optionalCoversTable.setScreenName(SHAConstants.CREATE_ROD);
		//loadQueryDetailsTableValues();
		
		referenceMap.put("optionalCover", bean.getDocumentDetails().getOptionalCovers());
		optionalCoversTable.setReference(referenceMap);
		
		optionalCoverLayout = new VerticalLayout();
		optionalCoverLayout.setWidth("100%");
		optionalCoverLayout.setCaption("Optional Covers");
		optionalCoverLayout.setSpacing(true);
		optionalCoverLayout.setMargin(false);
		optionalCoverLayout.addComponent(optionalCoversTable);	
	//	addOnBenifitLayout.addComponent();
		if(this.bean.getDocumentDetails().getOptionalCoversList() != null && ! this.bean.getDocumentDetails().getOptionalCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getOptionalCoversList()) {
				optionalCoversTable.addBeanToList(covers);
				
			}
		}
		//return rodQueryDetails;
		return optionalCoverLayout;
	}
	private VerticalLayout buildReconsiderationLayout()
	{
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.addComponent(cmbReconsiderationRequest);
		
		return 	reconsiderationLayout;
	}
	
	
	//private RODQueryDetailsTable buildQueryDetailsLayout()
	private HorizontalLayout buildQueryDetailsLayout()
	{
		/*List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
		rodQueryDetails.init("", false, false);
		if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
		{
			rodQueryDetails.setTableList(rodQueryDetailsList);
		}
		if(null != reconsiderationRequest)
		{
			rodQueryDetails.generateDropDown(reconsiderationRequest);
		}
		
		rodQueryDetails.init("", false, false,reconsiderationRequest);
		if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
		{
			rodQueryDetails.setTableList(rodQueryDetailsList);

			for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.setTableList(rodQueryDetailsList);

				//rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}
			
		}*/
		
		rodQueryDetails.initpresenterString(SHAConstants.CREATE_ROD);
		rodQueryDetails.init("", false, false,reconsiderationRequest);
		loadQueryDetailsTableValues();
		
		queryDetailsLayout = new HorizontalLayout(rodQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(false);
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	
	
	private HorizontalLayout buildPaymentQueryDetailsLayout()
	{
		//rodQueryDetails = rodQueryDetailsObj.get();
		paymentQueryDetails.initpresenterString(SHAConstants.CREATE_ROD);
		paymentQueryDetails.init("", false, false,reconsiderationRequest);
//		rodQueryDetails.setReference(this.referenceData);
		loadPaymentQueryDetailsTableValues();
		//rodQueryDetails.setEditable(true);
			//rodQueryDetails.setTableList(rodQueryDetailsList);
			/*for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}*/
			
		
		/**/
		
		HorizontalLayout queryDetailsLayout = new HorizontalLayout(paymentQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
//	private HorizontalLayout buildReconsiderRequestLayout()
	private VerticalLayout buildReconsiderRequestLayout()
	{
		reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		//reconsiderRequestDetails.init("", false, false);
		reconsiderRequestDetails.initPresenter(SHAConstants.PA_CREATE_ROD);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
				{
					Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(isSelect);
			}
				//reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
			}
		}
		if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		
		optPaymentCancellation = (OptionGroup) binder.buildAndBind("Payment Cancellation Needed" , "paymentCancellationNeeded" , OptionGroup.class);
		optPaymentCancellation.addItems(getReadioButtonOptions());
		optPaymentCancellation.setItemCaption(true, "Yes");
		optPaymentCancellation.setItemCaption(false, "No");
		optPaymentCancellation.setStyleName("horizontal");
		//optPaymentMode.select(true);
		//Vaadin8-setImmediate() optPaymentCancellation.setImmediate(true);
		if(null != bean.getDocumentDetails().getPaymentCancellationNeeded())
		{
			if(bean.getDocumentDetails().getPaymentCancellationNeeded())
			{
				optPaymentCancellation.setValue(true);
			}
			else
			{
				optPaymentCancellation.setValue(false);
			}
		}
		//unbindField(optPaymentMode);
		optPaymentCancellationListener();
	//	//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentCancellation.setRequired(true);
		
		
	//	HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(reconsiderRequestDetails);
		VerticalLayout reconsiderRequestLayout = new VerticalLayout(cmbReasonForReconsideration, reconsiderRequestDetails,new FormLayout(optPaymentCancellation));
		reconsiderRequestLayout.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		reconsiderRequestLayout.setWidth("100%");
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			cmbReasonForReconsideration.setEnabled(false);
			reconsiderRequestDetails.setEnabled(false);
			if(bean.getDocumentDetails().getPaymentCancellationNeededFlag() != null &&
					bean.getDocumentDetails().getPaymentCancellationNeededFlag().equalsIgnoreCase(SHAConstants.N_FLAG)){
				optPaymentCancellation.setEnabled(false);
			}
		}
		if(this.bean.getDocumentDetails().getIsEditable() != null && this.bean.getDocumentDetails().getIsEditable()){
			optPaymentCancellation.setEnabled(true);
		}
		//return reconsiderRequestDetails;
		return reconsiderRequestLayout;
		
	}
	
	private void optPaymentCancellationListener()
	{
		optPaymentCancellation.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				String message = null;
				if(null != value && value)
				{
					message = "Please confirm that payment cancellation needs to be done and you have recieved the cheque/DD for cancellation";
					if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
						bean.getDocumentDetails().setPaymentCancellationNeeded(true);
					}
				}
				else
				{
					message = "Are you sure that payment cancellation is not required and the payment has been accepted";
					if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
						bean.getDocumentDetails().setPaymentCancellationNeeded(false);
					}
				}
				Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
				
//				Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
				
				Button homeButton = new Button("Ok");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
				horizontalLayout.setSpacing(true);
				horizontalLayout.setMargin(true);
				
				VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
				layout.setSpacing(true);
				layout.setMargin(true);
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				hLayout.setStyleName("borderLayout");
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
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

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
			}

		});
	}
	
	private void addListener()
	{

		accidentOrDeath.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
				Boolean accidentDeath = event.getProperty().getValue() != null ? (Boolean) event.getProperty().getValue() : null;
				if(accidentDeath != null && !accidentDeath) {
					if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
							   && docRecFromValue != null
							   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
						buildNomineeLayout();
						
					}
				}	
				else if(accidentDeath != null && accidentDeath){
						if(nomineeDetailsTable != null) { 
							documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
						}
						if(chkNomineeDeceased != null){
							documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
						}
						if(legalHeirLayout != null) {
							documentDetailsPageLayout.removeComponent(legalHeirLayout);
						}
						
						if(null != bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())) && 
								("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
						 {
							cmbPayeeName.setReadOnly(false);
							for(int i = 0 ; i<payeeNameList.size() ; i++)
						 	{
								if ((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
								{
									//this.cmbPayeeName.setReadOnly(false);
									cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
									//this.cmbPayeeName.setReadOnly(true);
									//this.txtReasonForChange.setEnabled(false);
								}
						 	}	
							cmbPayeeName.setEnabled(true);
						 }	
						if(paymentDetailsLayout != null) {
							cmbPayeeName.setReadOnly(false);
							cmbPayeeName.setEnabled(true);
							optPaymentMode.setEnabled(true);
							btnIFCSSearch.setEnabled(true);
							/*txtLegalHeirName.setVisible(true);*/
							if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() !=null
									&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
				 					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())
				 					&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER).equals(bean.getDocumentDetails().getPaymentModeFlag())){
								
								btnAccPrefSearch.setEnabled(true);
							}
							else {
								if(btnAccPrefSearch != null)
									btnAccPrefSearch.setEnabled(false);
							}
						}
					getValuesForNameDropDown(accidentDeath);
				}
				else {
					if(nomineeDetailsTable != null) { 
						documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
					}
					if(chkNomineeDeceased != null){
						documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
					}
					if(legaHeirLayout != null) {
						documentDetailsPageLayout.removeComponent(legaHeirLayout);
					}
					getValuesForNameDropDown(accidentDeath);
				}
				
						if (accidentDeath != null && !accidentDeath) {
							setRequiredAndValidation(dateOfDeath);
							mandatoryFields.add(dateOfDeath);
							dateOfDeath.setValue(bean.getDocumentDetails().getDateOfDeath());
						} else {
							dateOfDeath.setRequired(false);
							mandatoryFields.remove(dateOfDeath);
							dateOfDeath.setValue(null);
						}
			}
		});

		cmbDocumentType
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{	
					if (documentDetailsPageLayout != null
							&& documentDetailsPageLayout.getComponentCount() > 0) {
						documentDetailsPageLayout.removeAllComponents();
					}
					if(("Fresh Document").equalsIgnoreCase(value.getValue()))
					{	
						//isReconsiderRequestFlag = false;
						unbindField(getListOfChkBox());			
						/*documentDetailsPageLayout.addComponent(documentDetailsLayout);
						documentDetailsPageLayout.addComponent(buildBillClassificationLayout());
						documentDetailsPageLayout.addComponent(buildAddOncoversDetailsLayout());*/
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildBenefitLayout(),buildAddOncoversDetailsLayout(),buildOptionalCoversDetailsLayout(),insuredLayout,docCheckListLayout,paymentDetailsLayout);
						//documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,reconsiderationLayout,insuredLayout,docCheckListLayout,paymentDetailsLayout);
						resetReconsiderationValue();
						addBenefitLister();

					}
					
					else if(("Reconsideration Document").equalsIgnoreCase(value.getValue())){
						
						unbindField(getListOfChkBox());		
					//	documentDetailsPageLayout.addComponents(documentDetailsLayout,buildReconsiderRequestLayout(),docCheckListLayout);
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildReconsiderRequestLayout(),insuredLayout,docCheckListLayout,paymentDetailsLayout);
					}
					else if(("Query Reply Document").equalsIgnoreCase(value.getValue())){
						unbindField(getListOfChkBox());
						//documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout);
						//documentDetailsPageLayout.addComponent(buildQueryDetailsLayout());
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),insuredLayout,docCheckListLayout,paymentDetailsLayout);
					}
					else
					{
						unbindField(getListOfChkBox());
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),insuredLayout,docCheckListLayout,paymentDetailsLayout);		
					}
					
					//addBillClassificationLister();
										
					SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
					Boolean accidentDeath = accidentOrDeath.getValue() != null ? (Boolean) accidentOrDeath.getValue() : null;
					if(accidentDeath != null && !accidentDeath) {
						if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
								   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
								   && docRecFromValue != null
								   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
									
							buildNomineeLayout();
						}
						else {
							if(nomineeDetailsTable != null) { 
								documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
							}
							if(chkNomineeDeceased != null){
								documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
							}
							if(legaHeirLayout != null) {
								documentDetailsPageLayout.removeComponent(legaHeirLayout);
							}
						}
					}
					else {
						if(nomineeDetailsTable != null) { 
							documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
						}
						if(chkNomineeDeceased != null){
							documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
						}
						if(legaHeirLayout != null) {
							documentDetailsPageLayout.removeComponent(legaHeirLayout);
						}
					}
				}
			}
		});
		
		
		
		
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.PA_CREATE_ROD);
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
				btnIFCSSearch.setEnabled(true);
			}
		});
		
		cmbReconsiderationRequest
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{	
					if (reconsiderationLayout != null
							&& reconsiderationLayout.getComponentCount() > 0) {
						reconsiderationLayout.removeAllComponents();
					}
					if(("NO").equalsIgnoreCase(value.getValue()))
					{	
						//isReconsiderRequestFlag = false;
						isReconsider = false;
						unbindField(getListOfChkBox());
					//	reconsiderationLayout.addComponent(buildBillClassificationLayout());
						//reconsiderationLayout.addComponent(buildAddOncoversDetailsLayout());
						reconsiderationLayout.addComponent(buildQueryDetailsLayout());
						
					
						if(isReconsider)
						{
							//displayAmountClaimedDetails();
							//buildBillClassificationDetailsBasedOnDocRecFrom(bean.getDocumentDetails().getDocumentReceivedFromValue());
							//uploadDocumentDTOList.removeAll(uploadDocumentDTOList);
							uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
						}
						
						bean.setReconsiderRODdto(null);
						bean.setUploadDocsList(null);
						
						bean.getDocumentDetails().setReconsiderationRequestValue("No");
						
						
						/*if(null != txtHospitalizationClaimedAmt)
						{
							txtHospitalizationClaimedAmt.setValue(null);
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setValue(null);
							txtPreHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPostHospitalizationClaimedAmt)
						{
							txtPostHospitalizationClaimedAmt.setValue(null);
							txtPostHospitalizationClaimedAmt.setEnabled(false);
						}
						*/
						
						//addBillClassificationLister();
					}
					else
					{
						/*
						 * If reconsider request is Yes, then bill classification
						 * validation needs to be skipped. Hence , based on below
						 * validation flag, decision would be made to skip or
						 * to consider the validation.
						 * */
						//isReconsiderRequestFlag = true;
						bean.getDocumentDetails().setReconsiderationRequestValue("Yes");
						unbindField(getListOfChkBox());
						isReconsider = true;
						/**
						 * Fix for issue 295. -- starts
						 * This was added to auto populate
						 * the payment details fields if , reconsideration
						 * is Yes.
						 * */
						setPaymentDetails();
						/**
						 * Fix for issue 295 ends.
						 * */
						if(null != txtHospitalizationClaimedAmt)
						{
							txtHospitalizationClaimedAmt.setValue(null);
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setValue(null);
							txtPreHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPostHospitalizationClaimedAmt)
						{
							txtPostHospitalizationClaimedAmt.setValue(null);
							txtPostHospitalizationClaimedAmt.setEnabled(false);
						}
						reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
					}
				}
				
			}
		});
		
		cmbDocumentsReceivedFrom.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					if(("Insured").equalsIgnoreCase(value.getValue()))
					{	
						/*txtAcknowledgementContactNo.setRequired(true);
						txtAcknowledgementContactNo.setReadOnly(false);
						txtAcknowledgementContactNo.setEnabled(true);*/
						txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);
						//txtEmailId.setRequired(true);
						resetChkBoxValues();
						chkhospitalization.setEnabled(false);
						chkPartialHospitalization.setEnabled(true);
						chkDeath.setEnabled(true);
						chkPermanentPartialDisability.setEnabled(true);
						chkPermanentTotalDisability.setEnabled(true);
						chkTemporaryTotalDisability.setEnabled(true);
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
						{
							/*chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							if(null != chkPartialHospitalization)
							chkPartialHospitalization.setEnabled(true);*/
							
							
							chkhospitalization.setEnabled(false);
							chkPartialHospitalization.setEnabled(true);
							chkDeath.setEnabled(true);
							chkPermanentPartialDisability.setEnabled(true);
							chkPermanentTotalDisability.setEnabled(true);
							chkTemporaryTotalDisability.setEnabled(true);
							
							/*chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							{
								//chkPostHospitalization.setEnabled(false);
								chkPreHospitalization.setEnabled(true);
							}
							//chkPreHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							{
								chkPostHospitalization.setEnabled(true);
							}
							if(null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.isEnabled())
							{
								chkLumpSumAmount.setEnabled(true);
							}
							if(null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.isEnabled())
							{
								chkAddOnBenefitsHospitalCash.setEnabled(true);
							}
							if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.isEnabled())
							{
								chkAddOnBenefitsPatientCare.setEnabled(true);
							}
							//chkPartialHospitalization.setValue(false);
*/							
						}
						if(null != chkhospitalization && ("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkhospitalization)
						{
							chkhospitalization.setEnabled(true);
							if(null != chkPartialHospitalization)
							{
								chkPartialHospitalization.setEnabled(false);
								if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
									chkPartialHospitalization.setValue(false);
							}
							
							
							//chkPartialHospitalization.setEnabled(true);
							
						/*	chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);
							
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							{
								//chkPostHospitalization.setEnabled(false);
								chkPreHospitalization.setEnabled(true);
							}
							//chkPreHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							{
								chkPostHospitalization.setEnabled(true);
							}*/
							
							//chkPartialHospitalization.setValue(false);
							
						}
					}
					if(("Insured").equalsIgnoreCase(value.getValue()))
					{	
						
						txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);
//						txtEmailId.setRequired(true);
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							chkPartialHospitalization.setEnabled(true);
							
							/*chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);
							//chkPartialHospitalization.setValue(false);
*/							
						}
						else
						{
							chkhospitalization.setEnabled(true);
						}
						
						
						
						
						/*setRequiredAndValidation(txtEmailId);
						
						mandatoryFields.add(txtEmailId);*/
					}
					else
					{
						resetChkBoxValues();
						chkhospitalization.setEnabled(true);
						chkDeath.setEnabled(false);
						chkPartialHospitalization.setEnabled(false);
						chkPermanentPartialDisability.setEnabled(false);
						chkPermanentTotalDisability.setEnabled(false);
						chkTemporaryTotalDisability.setEnabled(false);	
						//addOnCoversTable.setEnabled(false);
						//optionalCoversTable.setEnabled(false);
						if(null != addOnCoversTable)
						{
							addOnCoversTable.setEnabled(false);
							addOnCoversTable.removeRow();
						}
						if(null != optionalCoversTable)
						{
							optionalCoversTable.setEnabled(false);
							optionalCoversTable.removeRow();
						}
					
						
						
						txtEmailId.setReadOnly(false);
						//txtEmailId.setEnabled();
						txtEmailId.setValue(null);
						txtEmailId.setReadOnly(true);
						txtEmailId.setEnabled(false);
						txtEmailId.setRequired(false);
						if(null != chkPartialHospitalization && null != chkhospitalization && null != chkPreHospitalization && null != chkPostHospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkPartialHospitalization.setEnabled(false);
							if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
							chkPartialHospitalization.setValue(false);
							
							chkhospitalization.setEnabled(true);
							
							//chkPreHospitalization.setEnabled(false);
							//chkPostHospitalization.setEnabled(false);
						}
						if(null != chkhospitalization)
							chkhospitalization.setEnabled(true);
						if(null != chkHospitalizationRepeat)
					//	chkHospitalizationRepeat.setEnabled(true);
							//chkHospitalizationRepeat.setEnabled(false);
						if(null != chkPartialHospitalization)
						{

							chkPartialHospitalization.setEnabled(false);
							chkPartialHospitalization.setValue(null);
						}
					/*	if(null != chkPreHospitalization)
						{

							chkPreHospitalization.setEnabled(false);
							chkPreHospitalization.setValue(null);
						}
						if(null != chkPostHospitalization)
						{

							chkPostHospitalization.setEnabled(false);
							chkPostHospitalization.setValue(null);
						}
						//	chkPostHospitalization.setEnabled(false);
						if(null != chkLumpSumAmount)
						{
							chkLumpSumAmount.setEnabled(false);
							chkLumpSumAmount.setValue(null);
						}
						if(null != chkAddOnBenefitsHospitalCash)
						{
							chkAddOnBenefitsHospitalCash.setEnabled(false);
							chkAddOnBenefitsHospitalCash.setValue(null);
							//chkAddOnBenefitsHospitalCash.setEnabled(false);
						}
						if(null != chkAddOnBenefitsPatientCare)
						{
							chkAddOnBenefitsPatientCare.setEnabled(false);
							chkAddOnBenefitsPatientCare.setValue(null);
						}*/

						
						//mandatoryFields.remove(txtEmailId);
					}
					loadQueryDetailsTableValues();
				}
				
				
				
			}
		});
		
        changeHospitalBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				fireViewEvent(PACreateRODDocumentDetailsPresenter.HOSPITAL_DETAILS, bean);
				changeHospitalObj = changeHospitalInstance.get();
				changeHospitalObj.initView(changeHospitalDto,popup,bean,txtHospitalName);
				
				popup.setContent(changeHospitalObj);
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
				
				
			}
		});
		
		
/*		{
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
=======
>>>>>>> f624c21457d4b6f78f4946510030a263b8af43a7*/
				
		documentsReceivedDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date value = (Date)event.getProperty().getValue();
				if(null != value)
				{
					if(value.after(new Date()))
					{
						documentsReceivedDate.setValue(null);
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date cannot be greater than current system date."));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
					else if(getDifferenceBetweenDates(value) > 7)
					{

						/*documentsReceivedDate.setReadOnly(false);
						documentsReceivedDate.setEnabled(true);*/

						documentsReceivedDate.setEnabled(true);
						documentsReceivedDate.setReadOnly(false);

						documentsReceivedDate.setValue(null);
						
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date can only be 7 days prior to current system date. But entered date is "+value.toString()+". Please Re-enter a valid date"));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
				}
			}
		});
		
		btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				previousAccountDetailsTable.resetTableDataList();
				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");
				
				//setPreviousAccountDetailsValues();
				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.CREATE_ROD);
				previousPaymentVerticalLayout.removeAllComponents();
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
				populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);	
				previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
				previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);				
				
				
				setPreviousAccountDetailsValues();
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
				//clearPreviousAccountDetailsList();
				}
			}
		});
	
	btnCancel.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);
			
			/*ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
			        "No", "Yes", new ConfirmDialog.Listener() {

			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
	                          //  wizard.releaseHumanTask();
			            		//fireViewEvent(MenuItemBean.CREATE_ROD, null);
			        
			                } else {
			                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
			                }
			            }
			        });
			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
			
			//resetBankPaymentFeidls();
			//previousAccountDetailsTable.clearCheckBoxValue();
			//clearPreviousAccountDetailsList();
			
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
	
	private void addDateOfAdmissionListener()
	{
		dateOfAdmission.addValueChangeListener(new Property.ValueChangeListener() {			
			private static final long serialVersionUID = -1774887765294036092L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date dateOfAdmissionValue = (Date)event.getProperty().getValue();
				//Date d = DateFormat.getDateInstance(DateFormat.SHORT);
			//	SHAUtils.formatGregorianDate(dateOfAdmissionValue.toString());
				/*Date intimationDate = new Date(bean.getClaimDTO().getNewIntimationDto().getAdmissionDate().toString());
				Date dtoDate = new Date(bean.getDocumentDetails().getDateOfAdmission().toString());*/
				Date policyFrmDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate();
				Date policyToDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate();
				if (null != dateOfAdmissionValue && null != policyFrmDate 
						&& null != policyToDate) {
					if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
						dateOfAdmission.setValue(null);
						
						 Label label = new Label("Admission Date is not in range between Policy From Date and Policy To Date.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						
					}
				
				
					else 
				{
						
						Long diffDays = 0l;
						Long diffDays1 = 0l;
						if(null != bean.getClaimDTO().getNewIntimationDto().getAdmissionDate())
						diffDays = SHAUtils.getDaysBetweenDate(bean.getClaimDTO().getNewIntimationDto().getAdmissionDate(), dateOfAdmissionValue);
						
						if(null != bean.getDocumentDetails().getDateOfAdmission())
							diffDays1 = SHAUtils.getDaysBetweenDate(bean.getDocumentDetails().getDateOfAdmission(), dateOfAdmissionValue);
						/*if(!
								(((bean.getClaimDTO().getNewIntimationDto().getAdmissionDate()).equals(dateOfAdmissionValue))|| ((bean.getDocumentDetails().getDateOfAdmission()).equals(dateOfAdmissionValue)))
								)*/
						if(!(diffDays == 0 || diffDays1 == 0) )
						{
					if (insuredLayout != null
							&& insuredLayout.getComponentCount() > 0) {
						insuredLayout.removeAllComponents();
					}
					unbindField(txtReasonForChangeInDOA);
					txtReasonForChangeInDOA = (TextField)binder.buildAndBind("Reason For Change in DOA","changeInReasonDOA",TextField.class);
					if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && 
							(( null != bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())) 
							  ))
					{
						txtReasonForChangeInDOA.setEnabled(true);
					}
					else
					{
						txtReasonForChangeInDOA.setEnabled(false);
					}
					if(null != bean.getDocumentDetails().getReasonForChange())
					{
						txtReasonForChangeInDOA.setValue(bean.getDocumentDetails().getReasonForChange());
					}
					txtReasonForChangeInDOA.setRequired(true);
					txtReasonForChangeInDOA.setNullRepresentation("");
					FormLayout formLayout1 = new FormLayout(dateOfAdmission,txtReasonForChangeInDOA,dateOfDischarge);
					formLayout1.setWidth("40%");
					formLayout1.setSpacing(false);
					formLayout1.setMargin(false);
					/*formLayout1.addComponent(dateOfAdmission);
					formLayout1.addComponent(txtReasonForChangeInDOA);
					formLayout1.addComponent(dateOfDischarge);*/
					FormLayout formLayout2 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
					formLayout2.setWidth("40%");
					formLayout2.setSpacing(false);
					formLayout2.setMargin(false);
					/*formLayout2.addComponent(txtHospitalName);
					formLayout2.addComponent(cmbInsuredPatientName);*/
					FormLayout formLayout3 = new FormLayout(changeHospitalBtn);
					formLayout3.setWidth("20%");
					//formLayout3.addComponent(changeHospitalBtn);
					HorizontalLayout hLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3);
					/*hLayout.addComponent(formLayout1);
					hLayout.addComponent(formLayout2);
					hLayout.addComponent(formLayout3);*/
					//hLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
					hLayout.setSpacing(true);
				//	hLayout.setMargin(true);
				//	hLayout.setWidth("100%");
					setRequiredAndValidation(txtReasonForChangeInDOA);
					mandatoryFields.add(txtReasonForChangeInDOA);
					insuredLayout.addComponent(hLayout);
					isReasonForChangeReq = true;
						}
						else
						{
							unbindField(txtReasonForChangeInDOA);
							
							if (insuredLayout != null
									&& insuredLayout.getComponentCount() > 0) {
								insuredLayout.removeAllComponents();
							}
							
							FormLayout formLayout1 = new FormLayout(dateOfAdmission,dateOfDischarge);
							FormLayout formLayout2 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
							FormLayout formLayout3 = new FormLayout(changeHospitalBtn);
							HorizontalLayout hLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3);
						//	hLayout.setSpacing(true);
							hLayout.setMargin(false);
							//hLayout.setWidth("10%");
							//setRequiredAndValidation(txtReasonForChangeInDOA);
							//mandatoryFields.add(txtReasonForChangeInDOA);
							insuredLayout.addComponent(hLayout);
							isReasonForChangeReq = false;
							if(mandatoryFields.contains(txtReasonForChangeInDOA))
								mandatoryFields.remove(txtReasonForChangeInDOA);
						}
				}
				
				}
			}
		});
		
		dateOfDischarge.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Date dischargeDate = (Date)event.getProperty().getValue();
				if(null != dateOfAdmission)
				{
					Date admissionDate = dateOfAdmission.getValue();
					if(null != admissionDate)
					{
						Long diffDays = SHAUtils.getDaysBetweenDate( admissionDate,dischargeDate);
						if(diffDays<0)
						{
							dateOfDischarge.setValue(null);
							Label label = new Label("Discharge date is before than admission date. Please enter valid discharge date", ContentMode.HTML);
							label.setStyleName("errMessage");
							VerticalLayout layout = new VerticalLayout();
							layout.setMargin(true);
							layout.addComponent(label);

							ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(true);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						}
					}
				}
			}
		});
	}
	
	private void validateAdmissionDate(Date dateOfAdmissionValue)
	{

		//Date dateOfAdmissionValue = (Date)event.getProperty().getValue();
		//Date d = DateFormat.getDateInstance(DateFormat.SHORT);
	//	SHAUtils.formatGregorianDate(dateOfAdmissionValue.toString());
		/*Date intimationDate = new Date(bean.getClaimDTO().getNewIntimationDto().getAdmissionDate().toString());
		Date dtoDate = new Date(bean.getDocumentDetails().getDateOfAdmission().toString());*/
		Date policyFrmDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate();
		Date policyToDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate();
		if (null != dateOfAdmissionValue && null != policyFrmDate 
				&& null != policyToDate) {
			if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
				dateOfAdmission.setValue(null);
				
				 Label label = new Label("Admission Date is not in range between Policy From Date and Policy To Date.", ContentMode.HTML);
					label.setStyleName("errMessage");
				 HorizontalLayout layout = new HorizontalLayout(
						 label);
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					//dialog.setWidth("35%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				
			}
		
		
			else 
		{
				
				Long diffDays = 0l;
				Long diffDays1 = 0l;
				if(null != bean.getClaimDTO().getNewIntimationDto().getAdmissionDate())
				diffDays = SHAUtils.getDaysBetweenDate(bean.getClaimDTO().getNewIntimationDto().getAdmissionDate(), dateOfAdmissionValue);
				
				if(null != bean.getDocumentDetails().getDateOfAdmission())
					diffDays1 = SHAUtils.getDaysBetweenDate(bean.getDocumentDetails().getDateOfAdmission(), dateOfAdmissionValue);
				/*if(!
						(((bean.getClaimDTO().getNewIntimationDto().getAdmissionDate()).equals(dateOfAdmissionValue))|| ((bean.getDocumentDetails().getDateOfAdmission()).equals(dateOfAdmissionValue)))
						)*/
				if((diffDays != 0 || diffDays1 != 0) )
				{
			if (insuredLayout != null
					&& insuredLayout.getComponentCount() > 0) {
				insuredLayout.removeAllComponents();
			}
			unbindField(txtReasonForChangeInDOA);
			txtReasonForChangeInDOA = (TextField)binder.buildAndBind("Reason For Change in DOA","changeInReasonDOA",TextField.class);
			if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && 
					(( null != bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())) 
					  ))
			{
				txtReasonForChangeInDOA.setEnabled(true);
			}
			else
			{
				txtReasonForChangeInDOA.setEnabled(false);
			}
			if(null != bean.getDocumentDetails().getReasonForChange())
			{
				txtReasonForChangeInDOA.setValue(bean.getDocumentDetails().getReasonForChange());
			}
			txtReasonForChangeInDOA.setRequired(true);
			txtReasonForChangeInDOA.setNullRepresentation("");
			FormLayout formLayout1 = new FormLayout(dateOfAdmission,txtReasonForChangeInDOA,dateOfDischarge);
			/*formLayout1.addComponent(dateOfAdmission);
			formLayout1.addComponent(txtReasonForChangeInDOA);
			formLayout1.addComponent(dateOfDischarge);*/
			FormLayout formLayout2 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
			/*formLayout2.addComponent(txtHospitalName);
			formLayout2.addComponent(cmbInsuredPatientName);*/
			FormLayout formLayout3 = new FormLayout(changeHospitalBtn);
			//formLayout3.addComponent(changeHospitalBtn);
			HorizontalLayout hLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3);
			/*hLayout.addComponent(formLayout1);
			hLayout.addComponent(formLayout2);
			hLayout.addComponent(formLayout3);*/
			//hLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
			hLayout.setSpacing(true);
			hLayout.setMargin(false);
			hLayout.setWidth("100%");
			setRequiredAndValidation(txtReasonForChangeInDOA);
			mandatoryFields.add(txtReasonForChangeInDOA);
			insuredLayout.addComponent(hLayout);
			isReasonForChangeReq = true;
				}
				else
				{
					unbindField(txtReasonForChangeInDOA);
					
					if (insuredLayout != null
							&& insuredLayout.getComponentCount() > 0) {
						insuredLayout.removeAllComponents();
					}
					
					FormLayout formLayout1 = new FormLayout(dateOfAdmission,dateOfDischarge);
					formLayout1.setMargin(true);
					FormLayout formLayout2 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
					formLayout1.setMargin(true);
					FormLayout formLayout3 = new FormLayout(changeHospitalBtn);
					formLayout1.setMargin(true);

					HorizontalLayout hLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3);
					hLayout.setSpacing(true);
					hLayout.setMargin(false);
					//hLayout.setWidth("100%");
					//setRequiredAndValidation(txtReasonForChangeInDOA);
					//mandatoryFields.add(txtReasonForChangeInDOA);
					insuredLayout.addComponent(hLayout);
					isReasonForChangeReq = false;
					if(mandatoryFields.contains(txtReasonForChangeInDOA))
						mandatoryFields.remove(txtReasonForChangeInDOA);
				}
		}
		
		}
	
	}
	
	
	
	
	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeListener()
	{
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//TODO
				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					
					unbindField(getListOfPaymentFields());
					
					/*if(null != txtAccntNo)
					{
						bean.getDocumentDetails().setAccountNo(txtAccntNo.getValue());
					}
					if(null != txtIfscCode)
					{
						bean.getDocumentDetails().setIfscCode(txtIfscCode.getValue());
					}
					if(null != txtBranch)
					{
						bean.getDocumentDetails().setBranch(txtBranch.getValue());
					}
					if(null != txtBankName)
					{
						bean.getDocumentDetails().setBankName(txtBankName.getValue());
					}
					if(null != txtCity)
					{
						bean.getDocumentDetails().setCity(txtCity.getValue());
					}
					*/
					
					unbindField(getListOfPaymentFields());
					
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					/*if(null != txtPayableAt)
					{
						mandatoryFields.add(txtPayableAt);
						setRequiredAndValidation(txtPayableAt);
						txtPayableAt.setRequired(true);
						showOrHideValidation(false);
					}*/
					if(null != txtAccntNo)
						{
							mandatoryFields.remove(txtAccntNo);
						}
						if(null != txtIfscCode)
						{
							mandatoryFields.remove(txtIfscCode);
						}
					
				}
				else 
				{
					if(null != txtPayableAt)
					{
						bean.getDocumentDetails().setPayableAt(txtPayableAt.getValue());
					}
					/**
					 * The below code is added as a part of issue 295.
					 * Email id , reason for change , legalheirname, pan no
					 * are common for bank and cheque. Hence in case of query reply case or
					 * reconsideration case, those values needs to be auto populated.
					 * */
					/*if(null != txtEmailId)
					{
						bean.getDocumentDetails().setEmailId(txtEmailId.getValue());
					}
					if(null != txtReasonForChange)
					{
						
						bean.getDocumentDetails().setReasonForChange(txtReasonForChange.getValue());
					}
					if(null != txtLegalHeirName)
					{
						bean.getDocumentDetails().setLegalFirstName(txtLegalHeirName.getValue());
					}
					if(null != txtPanNo)
					{
						bean.getDocumentDetails().setPanNo(txtPanNo.getValue());
					}*/
					unbindField(getListOfPaymentFields());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					HorizontalLayout bankLayout = buildBankTransferLayout();
					
					if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
							&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
							&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
					
						if(txtAccntNo != null)
							txtAccntNo.setEnabled(false);
					}else {
						if(txtAccntNo != null)
							txtAccntNo.setEnabled(true);
					}
					bankLayout.setMargin(false);
					//txtIfscCode.setEnabled(true);
					txtBankName.setEnabled(false);
					txtCity.setEnabled(false);
					btnIFCSSearch.setVisible(true);
					txtBranch.setEnabled(false);
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(bankLayout);
					paymentDetailsLayout.setMargin(false);
					unbindField(txtPayableAt);
					mandatoryFields.remove(txtPayableAt);
					
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
	
	
	
	private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 chkPostHospitalization.setEnabled(true);
					 chkPreHospitalization.setEnabled(true);
					 txtHospitalizationClaimedAmt.setEnabled(true);
				 }
				 else
				 {
					 chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);
				 }
				 //validateBillClassificationDetails();
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 chkPostHospitalization.setEnabled(true);
					 chkPreHospitalization.setEnabled(true);
					 txtHospitalizationClaimedAmt.setEnabled(true);

				 }
				 else
				 {
					 chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 txtHospitalizationClaimedAmt.setEnabled(false);

				 }
				 //validateBillClassificationDetails();
			}
		});
		
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 txtPreHospitalizationClaimedAmt.setEnabled(true);
					 
				 }
				 else
				 {
					
					 txtPreHospitalizationClaimedAmt.setEnabled(false);
					 txtPreHospitalizationClaimedAmt.setValue(null);

				 }
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 txtPostHospitalizationClaimedAmt.setEnabled(true);
				 }
				 else
				 {
					
					 txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);

				 }
			}
		});
		
	}
		
	
	private int getDifferenceBetweenDates(Date value)
	{
		
		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int)((currentDay-enteredDay))/(1000 * 60 * 60 * 24);
		return diff;
	}
	
	
	
	public void loadContainerDataSources(Map<String, Object> referenceDataMap)
	{
		this.referenceData = referenceDataMap;
		reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		 cmbReconsiderationRequest.setContainerDataSource(reconsiderationRequest);
		 cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 {
			 if(null != reconsiderationRequest.getIdByIndex(i) && null != reconsiderationRequest.getIdByIndex(i).getValue())
			 {
				if ((this.bean.getDocumentDetails().getReconsiderationRequestValue()).equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue().trim()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}
			 }
		}
		 
		 reasonForReconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("reasonForReconsiderationRequest");
		 loadReasonForReconsiderationDropDown();
		 
		 
		 modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("modeOfReceipt");
		 cmbModeOfReceipt.setReadOnly(false);
		 cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbModeOfReceipt.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<modeOfReceipt.size() ; i++)
		 	{
				if ((this.bean.getDocumentDetails().getModeOfReceiptValue()).equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue()))
				{
					this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
				}
			}
		 cmbModeOfReceipt.setReadOnly(true);
		 
		 documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType");
		 cmbDocumentType.setContainerDataSource(documentType);
		 cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentType.setItemCaptionPropertyId("value");
		 
		
		 
		 docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("docReceivedFrom");
		 cmbDocumentsReceivedFrom.setReadOnly(false);
		 cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		 cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
		 	{
			 if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
				{
					if (("Insured").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
					{
						this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					}
				}
			 else if ((this.bean.getDocumentDetails().getDocumentReceivedFromValue()).equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 cmbDocumentsReceivedFrom.setReadOnly(true);
		 //cmbDocumentsReceivedFrom.setEnabled(false);
		 
		 
		
		/* for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if ((this.bean.getClaimDTO()).equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 */
		 insuredPatientList = (BeanItemContainer<SelectValue>) referenceDataMap.get("insuredPatientList");
		 cmbInsuredPatientName.setContainerDataSource(insuredPatientList);
		 cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbInsuredPatientName.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<insuredPatientList.size() ; i++)
	 	{
			if (null != this.bean.getClaimDTO().getNewIntimationDto().getInsuredPatientName() &&  (this.bean.getClaimDTO().getNewIntimationDto().getInsuredPatientName()).equalsIgnoreCase(insuredPatientList.getIdByIndex(i).getValue()))
			{
				this.cmbInsuredPatientName.setValue(insuredPatientList.getIdByIndex(i));
			}
		}
		 /**
		  * Below if block is added for issue no 999. 
		  * */
		 if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
					(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
					  ))
		 //if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())))
				 {
			 		cmbInsuredPatientName.setEnabled(true);
				 }
		 else
		 {
			 cmbInsuredPatientName.setEnabled(false);
		 }
		 
		 
		 if(null != cmbPayeeName)
		 {
			 try {
				 payeeNameList = (BeanItemContainer<SelectValue>) referenceDataMap.get("payeeNameList");
				 String docRecVal = "";
				 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				 {
					 SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					 if(null != selValue)
					 {
						 docRecVal = selValue.getValue();
					 }
				 }
				 cmbPayeeName.setContainerDataSource(payeeNameList);
				 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				 cmbPayeeName.setItemCaptionPropertyId("value");
				 for(int i = 0 ; i<payeeNameList.size() ; i++)
				 	{
					 
						if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
						{
							this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
							//this.txtReasonForChange.setEnabled(false);
						}
						if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
								(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) &&  ("Hospital").equalsIgnoreCase(docRecVal)))
						{
							
						if(null != this.bean.getDocumentDetails().getHospitalPayableAt() && this.bean.getDocumentDetails().getHospitalPayableAt().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							{
								this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
								//this.cmbPayeeName.setEnabled(false);
							}
							
							if(null !=this.bean.getDocumentDetails().getHospitalName()){
								if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
									this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
								{
								}
							}		//this.cmbPayeeName.setEnabled(false);
							
						}
						else if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
									("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
						 {
							if(null !=this.bean.getDocumentDetails().getHospitalName())
							{
							 if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
								{
								 	cmbPayeeName.removeItem(payeeNameList.getIdByIndex(i));
									//this.cmbPayeeName.setEnabled(false);
								}
							}
						 }
					// cmbPayeeName.removeItem(this.bean.getDocumentDetails().getHospitalName());
				 }
				 
				 
			 } catch(Exception e) {
				 System.out.println("ERROR OCCURED WHILE SETTING THE PAYEE NAME USING IDBYINDEX----------->"+ e.getMessage());
			 }
			
		 }
		 
		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
		// documentCheckListValidation.setReference(referenceDataMap);
		 
		 if(null != documentCheckListValidation)
			{
				documentCheckListValidation.setReferenceData(referenceDataMap);
			}
		 
		 this.docsDetailsList = (List<DocumentDetailsDTO>)referenceDataMap.get("validationDocList");
	//	 rodQueryDetails.generateDropDown(reconsiderationRequest);
		// this.docDTO = (DocumentDetailsDTO) referenceDataMap.get("billClaissificationDetails");
		 this.docDTO = (List<DocumentDetailsDTO>) referenceDataMap.get("billClaissificationDetails");
		 this.isFinalEnhancement = (Boolean)referenceDataMap.get("isFinalEnhancement");
		 if(!isReconsider)
		 {
			invokeBenifitListner();
		 }
		 
		 /**
		  * The below method will populate the amount claimed details based on the bill
		  * classification options. Hence after invoking that listener we need to populate the same.
		  * Because during invocation, the fields will get reset. Hence the values from
		  * acknowledgement screen had to be populated after invocation of listener.
		  * */
		 displayAmountClaimedDetails();
		 
		 setValuesFromDTO();
		// validateBillClassificationDetails();

		 accidentOrDeath.setValue(null);
			
		if(null != bean.getDocumentDetails().getAccidentOrDeath())
		{
			accidentOrDeath.setValue(bean.getDocumentDetails().getAccidentOrDeath());
		}
	}
	
	
	
	private void invokeBenifitListner()
	{
		if(null != this.bean.getDocumentDetails().getDeath())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getDeath();
			//unbindField(optPaymentMode);
			if(null != chkDeath)
				chkDeath.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPermanentPartialDisability())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPermanentPartialDisability();
			//unbindField(optPaymentMode);
			if(null != chkPermanentPartialDisability)
				chkPermanentPartialDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPermanentTotalDisability())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPermanentTotalDisability();
			//unbindField(optPaymentMode);
			if(null != chkPermanentTotalDisability)
			chkPermanentTotalDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getTemporaryTotalDisability() )// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getTemporaryTotalDisability();		
			
		if(null != chkTemporaryTotalDisability)
			chkTemporaryTotalDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalExpensesCover())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalExpensesCover();
			if(null != chkHospitalExpensesCover)
			{					
				chkHospitalExpensesCover.setValue(val);
			}
		}
		
		
		if(null != this.bean.getDocumentDetails().getHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkhospitalization)
			chkhospitalization.setValue(val);
		}
		
		/*if(null != this.bean.getDocumentDetails().getPreHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPreHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkPreHospitalization)
			chkPreHospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPostHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPostHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkPostHospitalization)
			chkPostHospitalization.setValue(val);
		}*/
		
		if(null != this.bean.getDocumentDetails().getPartialHospitalization() )// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPartialHospitalization();

			
			//unbindField(optPaymentMode);
			
			
			if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				chkPartialHospitalization.setValue(null);
			}
			else if(null != chkPartialHospitalization)
				chkPartialHospitalization.setValue(val);
		}
		
		/*if(null != this.bean.getDocumentDetails().getHospitalizationRepeat())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalizationRepeat();
			if(null != chkHospitalizationRepeat)
			{
				if(val)
				{
					chkHospitalizationRepeat.setValue(false);
					if(null != chkPreHospitalization)
					{
						chkPreHospitalization.setValue(false);
						chkPreHospitalization.setEnabled(false);
					}
					if(null != chkPostHospitalization)
					{
						chkPostHospitalization.setValue(false);
						chkPostHospitalization.setEnabled(false);
					}
				}
			//unbindField(optPaymentMode);
			chkHospitalizationRepeat.setValue(val);
			}
		}*/
		
	}
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkDeath);
		fieldList.add(chkPermanentPartialDisability);
		fieldList.add(chkPermanentTotalDisability);
		fieldList.add(chkTemporaryTotalDisability);		
		fieldList.add(chkAddOnBenefitsPatientCare);
		fieldList.add(chkHospitalExpensesCover);
		fieldList.add(chkhospitalization);
		fieldList.add(chkPartialHospitalization);
		if(null != cmbReasonForReconsideration)
			fieldList.add(cmbReasonForReconsideration);
		if(null != optPaymentCancellation)
			fieldList.add(optPaymentCancellation);
		
		return fieldList;
	}
	
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		//fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirName);
		
		/*fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);*/
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
		fieldList.add(txtPayableAt);
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
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}
public boolean validatePage() {

	
	Boolean hasError = false;
	showOrHideValidation(true);
	String eMsg = "";
	Boolean isReconsiderationRequest = false;
	
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
	
	/*if(!(null != this.txtAccntNo && null != this.txtAccntNo.getValue() && !("").equalsIgnoreCase(this.txtAccntNo.getValue())))
	{
		hasError = true;
		eMsg += "Please enter account no </br>";
	}
	
	if(!(null != this.txtIfscCode && null != this.txtIfscCode.getValue() && !("").equalsIgnoreCase(this.txtIfscCode.getValue())))
	{
		hasError = true;
		eMsg += "Please enter ifsc code </br>";
	}*/
	
//	if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
//	{
//		if(!isValidEmail(this.txtEmailId.getValue()))
//		{
//			hasError = true;
//			eMsg += "Please enter a valid email </br>";
//		}
//	}
	
	if(null != this.cmbReconsiderationRequest )
	{
		SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
		if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
		{
			if(null != cmbDocumentType && null != cmbDocumentType.getValue())
			{
				SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue1 && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue1.getId())))
			{//&& (cmbDocumentType.getValue().equals(SHAConstants.)))
			
			if((null != this.chkDeath && (null == this.chkDeath.getValue()) || this.chkDeath.getValue().equals(false)) &&
					(null != this.chkPermanentPartialDisability &&  (null == this.chkPermanentPartialDisability.getValue()) || this.chkPermanentPartialDisability.getValue().equals(false)) &&
					
					(null != this.chkPermanentTotalDisability && (null == this.chkPermanentTotalDisability.getValue()) || this.chkPermanentTotalDisability.getValue().equals(false)) &&
					
					(null != this.chkTemporaryTotalDisability && (null == this.chkTemporaryTotalDisability.getValue()) || this.chkTemporaryTotalDisability.getValue().equals(false)) &&
					
				//	(null != this.chkHospitalExpensesCover && null != this.chkHospitalExpensesCover.getValue() && this.chkHospitalExpensesCover.getValue().equals(false)) &&
					
					(null != this.chkhospitalization &&( null == this.chkhospitalization.getValue()) || this.chkhospitalization.getValue().equals(false)) &&
					
					(null != this.chkPartialHospitalization && ( null == this.chkPartialHospitalization.getValue()) || this.chkPartialHospitalization.getValue().equals(false)) &&
					
					(null == addOnCoversTable.getValues() || addOnCoversTable.getValues().isEmpty()) &&
					(null == optionalCoversTable.getValues() || optionalCoversTable.getValues().isEmpty()))
			{
				hasError = true;
				eMsg += "Please select Benefit Or Add on Covers Or Optional Covers</br>";
			}			
			
			List<ReconsiderRODRequestTableDTO> reimbList = bean.getReconsiderRodRequestList();

			/*if(null != this.addOnCoversTable){
				List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
				if(null != values){	
					if(null != reimbList && !reimbList.isEmpty())
					{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reimbList) {
						
						fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, reconsiderRODRequestTableDTO.getRodKey(),values);
					}
					}
					
					
				for (int cover = 0; cover < values.size()-1; cover++) 
					   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
					      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
					      {
					    	  hasError = true;
					    	  eMsg += "you can not select same Additional Covers</br>";
					      }
				}
			}*/
			/*else if(null != this.optionalCoversTable){
				List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
				if(null != values){
					if(null != reimbList && !reimbList.isEmpty())
					{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reimbList) {
						
						fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, reconsiderRODRequestTableDTO.getRodKey(),values);
					}
					}
					//fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT,  bean.getReconsiderRODdto().getRodKey(),values);
					
				for (int cover = 0; cover < values.size()-1; cover++) 
					   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
					      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
					      {
					    	  hasError = true;
					    	  eMsg += "you can not select same Optional Covers</br>";
					      }
				}
			}*/
			
			}
		}
		}
		/*else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
		{*/
		else if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(selValue.getId())))
			{
			isReconsiderationRequest = true;
			if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
			{
				hasError = true;
				eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
			}	
			/*if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
			{
				hasError = true;
				eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
			}
			*/
			else if(null != this.reconsiderDTO)
			{
				ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
				if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
				{
					hasError = true;
					eMsg += "Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>";
				}
			}
			
			if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
			{
				hasError = true;
				eMsg += "Please select payment cancellation needed since reconsideration request is yes </br>";
			}
		}
		}
	
	}
/*
	if(!isReconsiderRequestFlag)
	{
		if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
				(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
				
				(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
				
				(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
				
				
				(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
				
				(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
				
				(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
				
				(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()))
				)
		{
			hasError = true;
			eMsg += "Please select any one bill classification value";
		}
	}
	*/
	if(null!=txtBenifitClaimedAmnt && (null == txtBenifitClaimedAmnt.getValue() || txtBenifitClaimedAmnt.getValue().equals("")))
	{				
			hasError = true;
			eMsg += "Please select Benefit claimed Amount</br>";
		
	}
	
	if(null!=dateOfDeath && (null == dateOfDeath.getValue() || dateOfDeath.getValue().equals("")) && accidentOrDeath.getValue().equals(false))
	{				
			hasError = true;
			eMsg += "Please select Date of Death</br>";
		
	}
	
	
	/*if(null != this.addOnCoversTable){
		List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
		if(null != values){
		for (int cover = 0; cover < values.size()-1; cover++) 
			   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
			      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
			      {
			    	  hasError = true;
			    	  eMsg += "you can not select same Additional Covers</br>";
			      }
		}
	}
	 if(null != this.optionalCoversTable){
		List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
		if(null != values){
		for (int cover = 0; cover < values.size()-1; cover++) 
			   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
			      if(values.get(cover).getOptionalCover().getId() == values.get(cover1).getOptionalCover().getId())
			      {
			    	  hasError = true;
			    	  eMsg += "you can not select same Optional Covers</br>";
			      }
		}
	}*/
	
	if(null != dateOfAdmission && ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
			(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
					  )))
	{
		if(null == dateOfAdmission.getValue())
		{
			hasError = true;
			eMsg += "Please enter admission date. </br>";
		}
		else if(!(null != this.txtReasonForChangeInDOA && !("").equalsIgnoreCase(this.txtReasonForChangeInDOA.getValue())))
		{
			if(isReasonForChangeReq)
			{
			hasError = true;
			eMsg += "Please enter reason for change in DOA. </br>";
			}
			
		}
		if(null == dateOfDischarge.getValue())
		{
			hasError = true;
			eMsg += "Please enter discharge date. </br>";
		}
	}
	
	
	if(null != txtHospitalName && ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
			(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
					  )))
	
	{
		if(null == txtHospitalName.getValue() || ("").equalsIgnoreCase(txtHospitalName.getValue()))
		{
			hasError = true;
			eMsg += "Please enter hospital name</br>";
		}
	}
	/*if(null != this.txtReasonForChangeInDOA && ("").equalsIgnoreCase(this.txtReasonForChangeInDOA.getValue()))
	{
		hasError = true;
		eMsg += "Please enter reason for change in DOA. </br>";
		
	}*/
	
	if(null != this.documentCheckListValidation)
	{
		Boolean isValid = documentCheckListValidation.isValid();
		if (!isValid) {
			hasError = true;
			List<String> errors = this.documentCheckListValidation.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
	}
	
	Boolean accedentDeath = accidentOrDeath.getValue() != null ? (Boolean) accidentOrDeath.getValue() : null;
	
	if(accedentDeath != null 
			&& (accedentDeath
			|| !(!accedentDeath && bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					  && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())))){
	
			if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				//if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getDocumentDetails().get))
				if(null != txtReasonForChange && txtReasonForChange.isEnabled() && (null == txtReasonForChange.getValue() || ("").equalsIgnoreCase(txtReasonForChange.getValue())))
				{
					hasError = true;
					eMsg += "Please enter reason for change , since payee name is changed in payment details</br>";
				}
			}
			
			if(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getDocumentDetails().getPaymentModeFlag()) 
					&& this.txtPayableAt != null 
			        && (this.txtPayableAt.getValue() == null || ("").equalsIgnoreCase(this.txtPayableAt.getValue())))
			{
				hasError = true;
				eMsg += "Please enter Payable At </br>";
			}
				
			if(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getDocumentDetails().getPaymentModeFlag()) && this.txtIfscCode  != null && (this.txtIfscCode.getValue() == null || ("").equalsIgnoreCase(this.txtIfscCode.getValue())))
			{
				hasError = true;
				eMsg += "Please enter ifsc code </br>";
			}
			
			if(("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())
					&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getDocumentDetails().getPaymentModeFlag())
					&& txtAccntNo != null && (txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty())) {
				
				hasError = true;
				eMsg += "Please Enter Account No</br>";
			}
			
			if(txtPanNo != null){
				String panNumber = txtPanNo.getValue();
				if(panNumber != null && !panNumber.isEmpty() && ! panNumber.equalsIgnoreCase("")){
					if(panNumber.length() != 10){
						hasError = true;
						eMsg += "PAN number should be 10 digit value</br>";
					}
				}
			
			}	
			
			/*if(optPaymentMode.getValue() != null 
					&& !(Boolean)optPaymentMode.getValue()
					&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())) {
				if(txtAccountPref != null && (txtAccountPref.getValue() == null || txtAccountPref.getValue().isEmpty())){
					hasError = true;
					eMsg += "Please Select Account Preference</br>";
				}
			}*/
			
			if(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getDocumentDetails().getPaymentModeFlag())){
				if(null != this.txtEmailId && (null == this.txtEmailId.getValue() || (null != this.txtEmailId.getValue() && this.txtEmailId.getValue().isEmpty())))
				{
					hasError = true;
					eMsg += "Please enter email Id </br>";
				}
			}	
		}
	
	/*if(!(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) 
			|| ( null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) || (null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
			|| (null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) || (null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()))
	{
		hasError = true;
		eMsg += "Please select any one bill classification value";
	}*/
	
	/*if(null != this.cmbDocumentsReceivedFrom) {
		SelectValue selValue = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
		if((null!= selValue) &&("Insured").equalsIgnoreCase(selValue.getValue()))
		{
			if(!(null != this.txtAcknowledgementContactNo && null != this.txtAcknowledgementContactNo.getValue() && !("").equalsIgnoreCase(this.txtAcknowledgementContactNo.getValue())))
			{
				hasError = true;
				eMsg += "Please enter Acknowledgement Contact No</br>";
			}
			if((!(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))))
			{
				hasError = true;
				eMsg += "Please enter email Id </br>";
			}
		}
		 
	}*/
	
	if(null != cmbDocumentType && null != cmbDocumentType.getValue())
	{
		SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
		if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
		{
	if (null != this.addOnCoversTable){
		boolean isValid = this.addOnCoversTable.isValid();
		if(!isValid){
			hasError = true;
			List<String> errors = this.addOnCoversTable.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		else
		{
			List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
			if(null != values && !values.isEmpty()){	
				fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getClaimDTO().getKey(),values,bean.getDocumentDetails().getDocAcknowledgementKey());
				if(this.isValid){
					hasError = true;
					eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover" + "</br>";
				}
			}
		}
	}
	
	if (null != optionalCoversTable && null != this.optionalCoversTable.getValues() && !this.optionalCoversTable.getValues().isEmpty()){
		boolean isValid = this.optionalCoversTable.isValid();
		if(!isValid){
			hasError = true;
			List<String> errors = this.optionalCoversTable.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
		}
		else
		{
			List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
			List<ReconsiderRODRequestTableDTO> reimbList = bean.getReconsiderRodRequestList();

			if(null != values && !values.isEmpty()){	
				fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, bean.getClaimDTO().getKey(),values,bean.getDocumentDetails().getDocAcknowledgementKey());
				if(this.isValid){
					hasError = true;
					eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Optional Cover" + "</br>";
				}
			}
		}
	}
		}
	}
	if(null != cmbDocumentType && null != cmbDocumentType.getValue())
 {
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if (null != selValue
					&& ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT)
							.equals(selValue.getId()))
					|| ((ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue
							.getId()))) {
				if (!isReconsiderationRequest) {
					if (null != this.rodQueryDetails) {
						Boolean isValid = rodQueryDetails.isValid();
						if (!isValid) {
							hasError = true;
							List<String> errors = this.rodQueryDetails
									.getErrors();
							for (String error : errors) {
								eMsg += error + "</br>";
							}
						}
					}
				}
			}
		}	
	
	 if(null != cmbDocumentType && null != cmbDocumentType.getValue())
	{
		SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
		if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId())) 
				|| ((ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue.getId())) )
		{
			if(!isReconsiderationRequest)
			{
				if(null != this.rodQueryDetails)
				{ 
					Boolean isQueryReplyNo = rodQueryDetails.isQueryReplyNo();
					if(isQueryReplyNo){
								
						//documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType"); 
						if(null != documentType)
						{
						 for(int i = 0 ; i<documentType.size() ; i++)
						 {					
								this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
								hasError = true;
								eMsg += "Query Reply Status selected as NO so document type has been selected as Fresh Document"+ "</br>";
								break;
								//this.cmbDocumentType.setEnabled(false);
							}
						}
					}
							
				}
			}
		}
	}
	 

	 if(ReferenceTable.getGPAProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			Boolean validationforOutPatientExpenses = validationforOutPatientExpenses();
			if(!validationforOutPatientExpenses){
				hasError = true;
				eMsg += "Can not select any benefit along with Outpatient Expences</br>";
			}
			
			
			
			Boolean validationforEarningParent = validationforOutEarningParentSI();
			if(!validationforEarningParent){
				hasError = true;
				eMsg += "Can not select any benefit along with Earning Parent SI </br>";
			}
						
		}
	 
		SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
		if(accedentDeath != null 
				&& !accedentDeath
				&& docRecFromValue != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())
				&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {  

			if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() && !isNomineeDeceased()){
				List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
			
				if(tableList != null && !tableList.isEmpty()){
					bean.getClaimDTO().getNewIntimationDto().setNomineeList(tableList);
					StringBuffer nomineeNames = new StringBuffer("");
					int selectCnt = 0;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}
					}
					bean.getClaimDTO().getNewIntimationDto().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(nomineeNames.toString());
					}
					else{
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
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
				
				bean.getClaimDTO().getNewIntimationDto().setNomineeList(null);
				bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
				
				if(this.legalHeirDetails.isValid()) {
					
					//added for support fix IMSSUPPOR-31323
					List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
					legalHeirList.addAll(this.legalHeirDetails.getValues());
					if(legalHeirList != null && !legalHeirList.isEmpty()) {
						
						List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
						
						for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
							legalHeirList.add(legalHeirDTO);
						}
						
						bean.getPreauthDTO().setLegalHeirDTOList(legalHeirList);
					}
				}
				else{
					bean.getPreauthDTO().setLegalHeirDTOList(null);
					hasError = true;
					eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)";
				}
				// For bancs account preference is mandatory added below condition
				List<LegalHeirDTO> legalHeirDtls = legalHeirDetails.getValues();
				if(legalHeirDtls != null && !legalHeirDtls.isEmpty()) {
					for (LegalHeirDTO legalHeirDtlsDTO : legalHeirDtls) {
						if((legalHeirDtlsDTO.getPaymentModeId() != null && ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(legalHeirDtlsDTO.getPaymentModeId()))
								 || (legalHeirDtlsDTO.getPaymentMode() != null && !legalHeirDtlsDTO.getPaymentMode())){
						if(legalHeirDtlsDTO.getAccountPreference() == null || legalHeirDtlsDTO.getAccountPreference().getValue() == null){
							bean.getPreauthDTO().setLegalHeirDTOList(null);
							hasError = true;
							eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %,Account Preference)";
						}
						}
					}
				}
			}
			//added by noufel for PA saral pdt
			if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
					&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
				if(optionalCoversTable.getValues() != null && !optionalCoversTable.getValues().isEmpty() && 
						((null != this.chkDeath && (null == this.chkDeath.getValue()) || this.chkDeath.getValue().equals(false)) &&
								(null != this.chkPermanentTotalDisability && (null == this.chkPermanentTotalDisability.getValue()) || this.chkPermanentTotalDisability.getValue().equals(false))) ){
					List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();	
					if(null != values){
						for(AddOnCoversTableDTO optionalList : values){
							if(optionalList.getOptionalCover().getId() != null && optionalList.getOptionalCover().getId().equals(339l)){
								hasError = true;
								eMsg += "Please select Educational Grant Cover along with Death or PTD Benefit </br>";	
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
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		hasError = true;
		return !hasError;
	} 
	else 
	//Above code has some issues. Hence commenting the same.Once fixed shall check with that.
	{
		try {
			this.binder.commit();
			
			if(null != this.addOnCoversTable){
				List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();
				this.bean.getDocumentDetails().setAddOnCoversList(values);
			}

			if(null != this.optionalCoversTable){
				List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
				this.bean.getDocumentDetails().setOptionalCoversList(values);
			}
			
			if(null != cmbReasonForReconsideration && null != cmbReasonForReconsideration.getValue())
			{	
				SelectValue selValue = (SelectValue) cmbReasonForReconsideration.getValue();
				bean.getDocumentDetails().setReasonForReconsideration(selValue);
			}
			
			if(!bean.getIsComparisonDone() && ((null != bean.getDocumentDetails().getHospitalization() && bean.getDocumentDetails().getHospitalization()) ||(null != bean.getDocumentDetails().getPartialHospitalization() && bean.getDocumentDetails().getPartialHospitalization()) || (null != bean.getDocumentDetails().getHospitalizationRepeat() && bean.getDocumentDetails().getHospitalizationRepeat()))) {
				fireViewEvent(CreateRODDocumentDetailsPresenter.COMPARE_WITH_PREVIOUS_ROD, bean);
			}
			if(null != txtHospitalizationClaimedAmt)
				hospitalizationClaimedAmt = txtHospitalizationClaimedAmt.getValue();
			
			if(null != txtPreHospitalizationClaimedAmt)
				preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
			
			if(null != txtPostHospitalizationClaimedAmt)
				postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
			
			if(null != txtBenifitClaimedAmnt)
			{	
				if(null != this.bean.getReconsiderRODdto())
				{
				this.bean.getReconsiderRODdto().setBenefitClaimedAmnt(Double.valueOf(txtBenifitClaimedAmnt.getValue()));
				}
			}	
			
			if(chkNomineeDeceased != null){
				bean.getPreauthDTO().getPreauthDataExtractionDetails().setNomineeDeceasedFlag(chkNomineeDeceased.getValue() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
			}

			
		} catch (CommitException e) {
			e.printStackTrace();
		}
		showOrHideValidation(false);
		return true;
	}		

}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
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

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void changeHospitalDetails(UpdateHospitalDetailsDTO changeHospitalDto){
		this.changeHospitalDto = changeHospitalDto;
	}

	
	private void setTableValues()
	{
		if(null != this.documentCheckListValidation)
		{
			if(null != referenceData)
			{
				BeanItemContainer<SelectValue> checkListTableContainer = this.bean.getCheckListTableContainerForROD();
				List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					if(null != documentCheckListDTO && null != checkListTableContainer)
						
					{
						for(int i = 0 ; i<checkListTableContainer.size() ; i++)
					 	{
							if (documentCheckListDTO.getDocTypeId() != null && (documentCheckListDTO.getDocTypeId()).equals(checkListTableContainer.getIdByIndex(i).getId()))
							{
								SelectValue particularsValue = new SelectValue();
								particularsValue.setId((checkListTableContainer.getIdByIndex(i).getId()));
								particularsValue.setValue(checkListTableContainer.getIdByIndex(i).getValue());
								documentCheckListDTO.setParticulars(particularsValue);
								break;
							}
						}
						//The below code except add bean to list needs to removed.
						//documentCheckListDTO.setRodReceivedStatus(false);
						if(null != documentCheckListDTO.getAckReceivedStatus() && !("").equalsIgnoreCase(documentCheckListDTO.getAckReceivedStatus())
								&& ("Not Applicable").equalsIgnoreCase(documentCheckListDTO.getAckReceivedStatus())
								)
						{
							//this.documentCheckListValidation.get
						}
						this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
					}
				}
				if(null != bean.getUploadDocsList() && !bean.getUploadDocsList().isEmpty())
				{
					setUploadedTableList(bean.getUploadDocsList());
				}
			}
		}
		
		if(null != addOnCoversTable)
		{
			List<AddOnCoversTableDTO> addOnCoversTableDTOList = this.bean.getDocumentDetails().getAddOnCoversList();
			BeanItemContainer<SelectValue> coverList = this.bean.getDocumentDetails().getAdditionalCovers();
			if(null != addOnCoversTableDTOList && !addOnCoversTableDTOList.isEmpty())
			{
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableDTOList) {
					
					if(null != coverList)
					{
						for(int i = 0 ; i<coverList.size() ; i++)
					 	{
							if(null != addOnCoversTableDTO.getCovers() && addOnCoversTableDTO.getCovers().getId().equals(coverList.getIdByIndex(i).getId()))
							{
								SelectValue selectValue = new SelectValue();
								selectValue.setId(addOnCoversTableDTO.getCovers().getId());
								selectValue.setValue(coverList.getIdByIndex(i).getValue());
								addOnCoversTableDTO.setCovers(selectValue);
								break;
							}
						}
					}
					
					addOnCoversTable.addBeanToList(addOnCoversTableDTO);					
				}
			}
		}
		
		List<AddOnCoversTableDTO> optionalCoversTableDTOList = this.bean.getDocumentDetails().getOptionalCoversList();
		BeanItemContainer<SelectValue> coverList = this.bean.getDocumentDetails().getOptionalCovers();
		if(null != optionalCoversTableDTOList && !optionalCoversTableDTOList.isEmpty())
		{
			for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversTableDTOList) {
				
				if(null != coverList)
				{
					for(int i = 0 ; i<coverList.size() ; i++)
				 	{
						if(null != addOnCoversTableDTO.getOptionalCover() && addOnCoversTableDTO.getOptionalCover().getId().equals(coverList.getIdByIndex(i).getId()))
						{
							SelectValue selectValue = new SelectValue();
							selectValue.setId(addOnCoversTableDTO.getOptionalCover().getId());
							selectValue.setValue(coverList.getIdByIndex(i).getValue());
							addOnCoversTableDTO.setOptionalCover(selectValue);
							break;
						}
					}
				}
				
				optionalCoversTable.addBeanToList(addOnCoversTableDTO);					
			}
		}
	
		
	/*	if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
			List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{
				for (List<PreviousAccountDetailsDTO> list : previousListTable) {
					for (PreviousAccountDetailsDTO previousAccountDetailsDTO : list) {
						
						previousAccountDetailsDTO.setChkSelect(false);
						previousAccountDetailsDTO.setChkSelect(null);
						previousAccountDetailsDTO.setSerialNumber(rowCount);
						previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
						rowCount ++ ;
					}
				}
			}
			
		}*/
		
	}
	
	public void setPreviousAccountDetailsValues()
	{
		if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
			List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{
				for (List<PreviousAccountDetailsDTO> list : previousListTable) {
					for (PreviousAccountDetailsDTO previousAccountDetailsDTO : list) {
						
						previousAccountDetailsDTO.setChkSelect(false);
						previousAccountDetailsDTO.setChkSelect(null);						
						previousAccountDetailsDTO.setSerialNo(rowCount);
						previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
						rowCount ++ ;
					}
				}
				
			}
			
		}
	}
	
	public void clearPreviousAccountDetailsList()
	{
		List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
		if(null != previousListTable && !previousListTable.isEmpty())
		{
			previousListTable.clear();
		}
	}
	
	public  void setTableValuesToDTO()
	{
		/**
		 * Get the list of DTO's first.
		 * Loop it and get individual object. And then assign them to dto and set this
		 * dto to list. This final list will be set in  bean again.
		 * 
		 * */
		
		List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
		this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);
//		IMSSUPPOR-28935
		if(this.bean.getRodQueryDetailsList() != null && !this.bean.getRodQueryDetailsList().isEmpty()) {
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails.getValues();
		if(null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
					this.bean.setRodqueryDTO(null);
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}
		}
//		IMSSUPPOR-28935
		if(this.bean.getPaymentQueryDetailsList() != null && !this.bean.getPaymentQueryDetailsList().isEmpty()) {
		List<RODQueryDetailsDTO> rodPaymentQueryDetailsDTO = this.paymentQueryDetails.getValues();
		if(null != rodPaymentQueryDetailsDTO && !rodPaymentQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodPaymentQueryDetailsDTO) {
					
					this.bean.setRodqueryDTO(null);
					
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}
		}
		
		this.bean.setUploadDocsList(uploadDocumentDTOList);
		
		
		List<AddOnCoversTableDTO> objAddOnCoversList = this.addOnCoversTable.getValues();
		if(null != objAddOnCoversList && !objAddOnCoversList.isEmpty())
		{
			this.bean.getDocumentDetails().setAddOnCoversList(objAddOnCoversList);
			this.bean.getDocumentDetails().setAddOnCoversDeletedList(this.addOnCoversTable.getDeltedAddOnCoversList());
		}
		
		List<AddOnCoversTableDTO> benefitCoversList = this.optionalCoversTable.getValues();
		if(null != benefitCoversList && !benefitCoversList.isEmpty())
		{
			this.bean.getDocumentDetails().setOptionalCoversList(benefitCoversList);
			this.bean.getDocumentDetails().setOptionalCoversDeletedList(this.optionalCoversTable.getDeltedAddOnCoversList());
		}
		
		
	}
	
	public void setValuesFromDTO()
	{
		DocumentDetailsDTO documentDetails = this.bean.getDocumentDetails();
		/*if(null != documentDetails.getDocumentsReceivedFrom())
		{
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 	{
					if ( documentDetails.getDocumentsReceivedFrom().getValue().equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
					{
						this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					}
				}
		}*/
		
		if(null != documentDetails.getDocumentsReceivedDate())
		{
			documentsReceivedDate.setReadOnly(false);
			documentsReceivedDate.setValue(documentDetails.getDocumentsReceivedDate());
			//documentsReceivedDate.setReadOnly(true);
		}
		
		if(null != documentDetails.getAccidentOrDeath())
		{
			accidentOrDeath.setValue(documentDetails.getAccidentOrDeath());
		}
		
		if(null != documentDetails.getAccidentOrDeathDate())
		{
			accidentOrDeathDate.setValue(documentDetails.getAccidentOrDeathDate());
		}
		
		if(null != documentDetails.getBenifitClaimedAmount())
		{
			txtBenifitClaimedAmnt.setValue(documentDetails.getBenifitClaimedAmount());
		}
		
	/*	if(null != documentDetails.getDocumentType().getId())
		{
			cmbDocumentType.setValue(documentDetails.getDocumentType());
		}*/
		
		if(null != documentDetails.getModeOfReceipt())
		{
			cmbModeOfReceipt.setReadOnly(false);
			 for(int i = 0 ; i<modeOfReceipt.size() ; i++)
			 	{
					if ( documentDetails.getModeOfReceipt().getValue().equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue()))
					{
						this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
					}
				}
			 cmbModeOfReceipt.setReadOnly(true);
		}
		
		if(null != documentDetails.getReconsiderationRequest())
		{
			for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 	{
				if ( documentDetails.getReconsiderationRequest().getValue().equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}
			}
		}
		
		if(null != documentDetails.getPayeeName())
		{
			for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if (documentDetails.getPayeeName().getValue().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
				{
					this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
				}
			}
		}
		
		if(null != documentDetails.getInsuredPatientName())
		{
			for(int i = 0 ; i<insuredPatientList.size() ; i++)
		 	{
				if ( documentDetails.getInsuredPatientName().getValue().equalsIgnoreCase(insuredPatientList.getIdByIndex(i).getValue()))
				{
					this.cmbInsuredPatientName.setValue(insuredPatientList.getIdByIndex(i));
				}
			}
		}
		
		
		if(null != documentDetails.getDocumentType())
		{
			for(int i = 0 ; i<documentType.size() ; i++)
		 	{
				if ( documentDetails.getDocumentType().getValue().equalsIgnoreCase(documentType.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
				}
			}
		}
	}
	
	
	public void saveReconsideRequestTableValue(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO)
	{
		this.bean.setReconsiderRODdto(dto);
		this.reconsiderDTO = dto;
		
		if(null != uploadDocumentDTOList && !uploadDocumentDTOList.isEmpty())
		{
			//uploadDocumentDTOList.clear();
			uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
		/**
		 * Adding as of now to provide a fix.
		 * */
			uploadDocumentDTOList = this.bean.getUploadDocsList();
		}
		else
		{
			setUploadedTableList(uploadDocsDTO);
		}
		//this.bean.setUploadDocsList(uploadDocsDTO);
		setClaimedAmountField(dto);

	}

	private void setUploadedTableList(List<UploadDocumentDTO> uploadDocsDTO) {
	//	uploadDocumentDTOList.clear();
		uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>(); 
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			this.uploadDocumentDTOList.add(uploadDocumentDTO);
		}
		
	}
	
	private void setClaimedAmountField(ReconsiderRODRequestTableDTO dto)
	{
		if(!reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		if(null != dto.getSelect() && dto.getSelect())
		{
			isReconsider = true;
			reconsiderationMap.put(dto.getAcknowledgementNo(), dto.getSelect());
			
			if(null != txtBenifitClaimedAmnt)
			{
				if(null != dto.getBenefitClaimedAmnt() && 0 != dto.getBenefitClaimedAmnt() && !("").equals(dto.getBenefitClaimedAmnt()))
				{
					txtBenifitClaimedAmnt.setValue(String.valueOf(dto.getBenefitClaimedAmnt()));
				}
			}

			
			if((null != dto.getHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationFlag())) || 
					(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag())) ||
					(null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
					)
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtHospitalizationClaimedAmt.setEnabled(true);
					
					if(null != hospitalizationClaimedAmt && !("").equalsIgnoreCase(hospitalizationClaimedAmt))
					{
						txtHospitalizationClaimedAmt.setValue(hospitalizationClaimedAmt);
						
					}
					else
					{
						txtHospitalizationClaimedAmt.setValue(null != dto.getHospitalizationClaimedAmt() ? String.valueOf(dto.getHospitalizationClaimedAmt()) : "");
					}
					
					
				}
			}
			if(null != dto.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPreHospitalizationFlag()))
					{
						if(null!= txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setEnabled(true);
							if(null != preHospitalizationAmt && !("").equalsIgnoreCase(preHospitalizationAmt))
							{
								txtPreHospitalizationClaimedAmt.setValue(preHospitalizationAmt);
								
							}
							else
							{
								txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
							}
							//txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
						}
					}
			if(null != dto.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPostHospitalizationFlag()))
			{
				if(null!= txtPostHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					if(null != postHospitalizationAmt && !("").equalsIgnoreCase(postHospitalizationAmt))
					{
						txtPostHospitalizationClaimedAmt.setValue(postHospitalizationAmt);
						
					}
					else
					{
						txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
					}
					//txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
			}
			
			if(null != txtEmailId){
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(null != dto.getEmailId() ? dto.getEmailId() : "");
			txtEmailId.setReadOnly(true);
			}
			
			if(null != txtAccntNo){
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(null != dto.getAccountNo() ?  dto.getAccountNo() : "");
			txtAccntNo.setReadOnly(true);
			}
			
			if(null != txtPanNo){	
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(null != dto.getPanNo() ?  dto.getPanNo() : "");
			txtPanNo.setReadOnly(true);
			}
			
			if(null != txtPayableAt){
			txtPayableAt.setReadOnly(false);
			txtPayableAt.setValue(null != bean.getDocumentDetails().getPayableAt() ?  bean.getDocumentDetails().getPayableAt()  : "");
			txtPayableAt.setReadOnly(true);
			}
			
			if(null != cmbPayeeName){
			cmbPayeeName.setReadOnly(false);
			cmbPayeeName.setValue(null != dto.getPayeeName() ?  dto.getPayeeName() : "");
			cmbPayeeName.setReadOnly(true);
			}
			
			if(null != txtReasonForChange){
			txtReasonForChange.setReadOnly(false);
			txtReasonForChange.setValue(null != dto.getReasonForChange() ?  dto.getReasonForChange() : "");
			txtReasonForChange.setReadOnly(true);
			}
			
			if(null != txtLegalHeirName){
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(null != dto.getLeagalHeirName() ?  dto.getLeagalHeirName() : "");
			txtLegalHeirName.setReadOnly(true);
			}
			
			if(null != txtBankName){
			txtBankName.setReadOnly(false);
			txtBankName.setValue(null != dto.getBankName() ?  dto.getBankName() : "");
			txtBankName.setReadOnly(true);
			}
			
			if(null != txtBranch){
			txtBranch.setReadOnly(false);
			txtBranch.setValue(null != dto.getBranchName() ?  dto.getBranchName() : "");
			txtBranch.setReadOnly(true);
			}
			
			if(null != txtIfscCode){
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(null != dto.getIfscCode() ? dto.getIfscCode() : "");
			txtIfscCode.setReadOnly(true);
			}
			
			if(null != txtCity)
			{
			txtCity.setReadOnly(false);
			txtCity.setValue(null != dto.getCity() ?  dto.getCity() : "");
			txtCity.setReadOnly(true);
			}
			/*if(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag()))
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
			}*/
			
		}
		else
		{
			if(null != txtBenifitClaimedAmnt)
			{
				txtBenifitClaimedAmnt.setValue(null);
			}

			if(null!= txtHospitalizationClaimedAmt)
			{
				txtHospitalizationClaimedAmt.setEnabled(false);
				txtHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPreHospitalizationClaimedAmt)
			{
				txtPreHospitalizationClaimedAmt.setEnabled(false);
				txtPreHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPostHospitalizationClaimedAmt)
			{
				txtPostHospitalizationClaimedAmt.setEnabled(false);
				txtPostHospitalizationClaimedAmt.setValue(null);
			}
			hospitalizationClaimedAmt = "";
			preHospitalizationAmt = "";
			postHospitalizationAmt = "";
			this.bean.setReconsiderRODdto(null);
			isReconsider = false;
		}
	}
	
	

	public void setDocumentDetailsListForValidation(List<DocumentDetailsDTO> documentDetailsDTO)
	{
		this.docsDetailsList = documentDetailsDTO;
		//validateBillClassification();
	}
	
	
	
	public void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails)
	{
		if(null != this.documentCheckListValidation)
		{
			this.chkhospitalization.setValue(false);
			//this.chkPreHospitalization.setValue(false);
		//	this.chkPostHospitalization.setValue(false);
			//this.chkLumpSumAmount.setValue(false);
			//this.chkHospitalizationRepeat.setValue(false);
			this.chkPartialHospitalization.setValue(false);
		
			this.cmbDocumentType.setEnabled(true);
					
			/** 
			 * The below code is commented as an impact of R0364 - Document checklist table enhancement.
			 * Since mandatory flag column is removed, the below code doesn't have any effect. Hence
			 * commenting the same.
			 * **/
			
				
		}
		if(null != rodQueryDetails)
		{
				if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
				{
					isQueryReplyReceived = true;
					
					if(null != rodQueryDetails.getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationFlag()))
					{
						this.chkhospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkhospitalization.setEnabled(false);
					}
					
					if(null != txtBenifitClaimedAmnt)
					{
						if(null != rodQueryDetails.getBenefitClaimedAmount() && 0 != rodQueryDetails.getBenefitClaimedAmount() && !("").equals(rodQueryDetails.getBenefitClaimedAmount()))
						{
							txtBenifitClaimedAmnt.setValue(String.valueOf(rodQueryDetails.getBenefitClaimedAmount()));
						}
					}									
					
					if(null != rodQueryDetails.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPartialHospitalizationFlag()))
					{
						this.chkPartialHospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkPartialHospitalization.setEnabled(false);
					}
				
					this.cmbDocumentType.setEnabled(true);
				//	disableBillClassification();
					
					if(rodQueryDetails.getAcknowledgementContactNumber() != null){
				//	this.txtAcknowledgementContactNo.setValue(rodQueryDetails.getAcknowledgementContactNumber());
					}
					
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				
				this.cmbDocumentType.setEnabled(true);
				String docRecFromVal = null;
				if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					docRecFromVal = docRecFrom.getValue();
				}
				if(null != chkhospitalization )//&& chkhospitalization.isEnabled())
				{
					if(null != docRecFromVal && (("Hospital").equalsIgnoreCase(docRecFromVal) || (("Insured").equalsIgnoreCase(docRecFromVal) && ("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))))
					{
						chkhospitalization.setEnabled(true);
						if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
						{
							chkhospitalization.setValue(false);
							txtHospitalizationClaimedAmt.setValue(null);
						}
					}
					
				}
				if(null != txtBenifitClaimedAmnt)
				{
					txtBenifitClaimedAmnt.setValue(null);
				}
								
				
				if(null != chkPartialHospitalization)//&& chkPartialHospitalization.isEnabled())
				{
					if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && ("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
					{
						chkPartialHospitalization.setEnabled(true);	
						if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
						{
							chkPartialHospitalization.setValue(false);
							txtHospitalizationClaimedAmt.setValue(null);
						}
					}
					//txtHospitalizationClaimedAmt.setValue(null);
				}
			
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
				{
					//chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != chkAddOnBenefitsPatientCare)
					{
						chkAddOnBenefitsPatientCare.setEnabled(true);
						if(null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue())
						{
							chkAddOnBenefitsPatientCare.setValue(false);
						}
					}
				}
								
				//resetDocCheckListTable();
				
			}
		}
	}
/*	public void validateBillClassificationDetails()
	{
		if	(
				(("Y").equalsIgnoreCase(this.bean.getHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) &&
				(("Y").equalsIgnoreCase(this.bean.getPreHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag())) &&
				(("Y").equalsIgnoreCase(this.bean.getPostHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))	
			)
			{
				HorizontalLayout layout = new HorizontalLayout(
						new Label("Already an ROD exist for hospitalization, pre and post hospitalization. Please  link to existing ROD."));
				layout.setMargin(true);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setWidth("50%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				
			}
				else if (
						(("Y").equalsIgnoreCase(this.bean.getHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) &&
						(("Y").equalsIgnoreCase(this.bean.getPreHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()))
						)
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre hospitalization. Please choose either post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
				}
				else if (
						(("Y").equalsIgnoreCase(this.bean.getHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) &&
						(("Y").equalsIgnoreCase(this.bean.getPostHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						)
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization,  post hospitalization.  Please choose either pre hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
				}
				
				else if (("Y").equalsIgnoreCase(this.bean.getHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization. Please choose either pre or  post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
				}
				
				else if(
						(("Y").equalsIgnoreCase(this.bean.getPartialHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag())) &&
						(("Y").equalsIgnoreCase(this.bean.getPreHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag())) &&
						(("Y").equalsIgnoreCase(this.bean.getPostHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))	
					)
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre and post hospitalization. Please  link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if  (
							(("Y").equalsIgnoreCase(this.bean.getPartialHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag())) &&
							(("Y").equalsIgnoreCase(this.bean.getPostHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						)
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre hospitalization. Please choose either post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
				}
				else if (
							(("Y").equalsIgnoreCase(this.bean.getPartialHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag())) &&
							(("Y").equalsIgnoreCase(this.bean.getPostHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						)
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization,  post hospitalization.  Please choose either pre hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				
				else if (("Y").equalsIgnoreCase(this.bean.getPartialHospitalizationFlag()) && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization. Please choose either pre or  post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
				}
			}
*/
	
	
	

	public void setUpPaymentDetails(ReceiptOfDocumentsDTO rodDTO) {
		this.bean = bean;
		
	}
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}
	/*public void setIFSCDetail(String ifscCode, String branchName, String bankName, String city){
		txtIfscCode.
		
	}*/

	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		
		SelectValue selected = cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
		Boolean accDeath = accidentOrDeath.getValue() != null ? (Boolean)accidentOrDeath.getValue() : null;
		
		if(selected != null
				&& selected.getId() != null		
				&& selected.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
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
			
			if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
	 					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())){
					
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
				
				if(null != this.bean.getDocumentDetails()){
					this.bean.getDocumentDetails().setBankId(dto.getBankId());
				}
			}
			else{
				//if(optPaymentMode.getValue() != null && (Boolean)optPaymentMode.getValue()) {
					
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
					
					if(null != this.bean.getDocumentDetails()){
						this.bean.getDocumentDetails().setBankId(dto.getBankId());
					}
				//}	
		 }
		}	
	}
	
	private void loadQueryDetailsTableValues()
	{
		if(null != rodQueryDetails)
		{
			List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
			if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
			{
				rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
						}
					}
					
					/*if(null != this.bean.getDocumentDetails().getAccountNo())
					{
						txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
					}
					if(null != this.bean.getDocumentDetails().getIfscCode())
					{
						txtIfscCode.setValue(this.bean.getDocumentDetails().getIfscCode());
					}
					if(null != this.bean.getDocumentDetails().getBranch())
					{
						txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
					}
					if(null != this.bean.getDocumentDetails().getBankName())
					{
						txtBankName.setValue(this.bean.getDocumentDetails().getBankName());
					}
					if(null != this.bean.getDocumentDetails().getCity())
					{
						txtCity.setValue(this.bean.getDocumentDetails().getCity());
					}
					if(null != this.bean.getDocumentDetails().getPanNo())
					{
						txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
					}
					if(null != this.bean.getDocumentDetails().getReasonForChange())
					{
						txtReasonForChange.setValue(this.bean.getDocumentDetails().getReasonForChange());
					}
					if(null != this.bean.getDocumentDetails().getLegalFirstName());
					{
						txtLegalHeirName.setValue(this.bean.getDocumentDetails().getLegalFirstName());
					}*/
				}
			}
		}
	}
	
	
	private void loadPaymentQueryDetailsTableValues()
	{
		if(null != paymentQueryDetails )
		{
			List<RODQueryDetailsDTO> rodPaymentQueryDetailsList = this.bean.getPaymentQueryDetailsList();
			if(null != rodPaymentQueryDetailsList && !rodPaymentQueryDetailsList.isEmpty())
			{
			//	rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodPaymentQueryDetailsDTO : rodPaymentQueryDetailsList) {
						
						if(rodPaymentQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							paymentQueryDetails.addBeanToList(rodPaymentQueryDetailsDTO);
						}
						else
						{
							if(null != paymentQueryDetails)
							{
								paymentQueryDetails.removeRow();
							}
						}
					}
				}
			}
		}
	}
/*	public void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails)
	{	
		*//***
		 * Commenting the below line of code, since 
		 * there was an issue observed in query reply 
		 * scenario
		 * 
		 * **//*
		this.chkhospitalization.setValue(false);
		this.chkPreHospitalization.setValue(false);
		this.chkPostHospitalization.setValue(false);
		this.chkLumpSumAmount.setValue(false);
		this.chkHospitalizationRepeat.setValue(false);
		this.chkPartialHospitalization.setValue(false);
		this.chkAddOnBenefitsHospitalCash.setValue(false);
		this.chkAddOnBenefitsPatientCare.setValue(false);
		if(null != rodQueryDetails)
		{
				if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
				{
					isQueryReplyReceived = true;
					
					if(null != rodQueryDetails.getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationFlag()))
					{
						this.chkhospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
						}
						this.chkhospitalization.setEnabled(false);
					}
					else
					{
						this.chkhospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPreHospitalizationFlag()))
					{
						this.chkPreHospitalization.setValue(true);
						if(null != rodQueryDetails.getPreHospitalizationClaimedAmt())
						{
							this.txtPreHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkPreHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPreHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPostHospitalizationFlag()))
					{
						this.chkPostHospitalization.setValue(true);
						if(null != rodQueryDetails.getPostHospitalizationClaimedAmt())
						{
							this.txtPostHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPostHospitalizationClaimedAmt()));
						}
						this.chkPostHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPostHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPartialHospitalizationFlag()))
					{
						this.chkPartialHospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
						}
						this.chkPartialHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPartialHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationRepeatFlag()))
					{
						this.chkHospitalizationRepeat.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkHospitalizationRepeat.setEnabled(false);
					}
					else
					{
						this.chkHospitalizationRepeat.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsLumpsumFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsLumpsumFlag()))
					{
						this.chkLumpSumAmount.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkLumpSumAmount.setEnabled(false);
					}
					else
					{
						this.chkLumpSumAmount.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBeneftisHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBeneftisHospitalCashFlag()))
					{
						this.chkAddOnBenefitsHospitalCash.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsHospitalCash.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsHospitalCash.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsPatientCareFlag()))
					{
						this.chkAddOnBenefitsPatientCare.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsPatientCare.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsPatientCare.setValue(false);
					}
					
					disableBillClassification();
					txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);
					if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
					{
						setPaymentDetailsIfQueryReplyYes(rodQueryDetails);
					}
					else
					{					
						setPaymentDetails();
				//	}
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				
				String docRecFromVal = null;
				if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					docRecFromVal = docRecFrom.getValue();
				}
				if(null != chkhospitalization )//&& chkhospitalization.isEnabled())
				{
					if(null != docRecFromVal && (("Hospital").equalsIgnoreCase(docRecFromVal) || (("Insured").equalsIgnoreCase(docRecFromVal) && ("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))))
					{
						chkhospitalization.setEnabled(true);
						if(null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))
						//if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
						{
							//txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
						
							//chkhospitalization.setValue(true);
							
							//txtHospitalizationClaimedAmt.setValue();
						}
						else
						{
							//chkhospitalization.setValue(false);
							//txtHospitalizationClaimedAmt.setValue(null);
						}
						txtHospitalizationClaimedAmt.setValue(null);
						chkhospitalization.setValue(null);
					}
					
				}
				
				
				if((null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag"))))
				{
					if(null != chkPreHospitalization)
					{
						chkPreHospitalization.setEnabled(true);
						//if(null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()))
						{
							
							txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
							//chkPreHospitalization.setValue(true);
						}
						else
						{
							//chkPreHospitalization.setValue(false);
						}
						txtPreHospitalizationClaimedAmt.setValue(null);
						chkPreHospitalization.setValue(null);
					}
				}
				
				
				
				//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					if(null != chkPostHospitalization)
					{
						chkPostHospitalization.setEnabled(true);
						//if(null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						{
							txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
							//chkPostHospitalization.setValue(true);
							
						}
						else
						{
							//chkPostHospitalization.setValue(false);
						}
						txtPostHospitalizationClaimedAmt.setValue(null);
						chkPostHospitalization.setValue(null);
						
					}
				}
				
				
				if(null != chkPartialHospitalization)//&& chkPartialHospitalization.isEnabled())
				{
					if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && ("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
					{
						chkPartialHospitalization.setEnabled(true);	
					//	if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()))
							//if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							{
								txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
								chkPartialHospitalization.setValue(true);
								
							}
						else
						{
							chkPartialHospitalization.setValue(false);
						}
						txtHospitalizationClaimedAmt.setValue(null);
						chkPartialHospitalization.setValue(null);
					}
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
				{	if(null != chkLumpSumAmount)
					{
						chkLumpSumAmount.setEnabled(true);
					//	if(null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue())
						if(null != this.bean.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getLumpSumAmountFlag()))
						{
							chkLumpSumAmount.setValue(true);
						}
						else
						{
							chkLumpSumAmount.setValue(false);
						}
						chkLumpSumAmount.setValue(null);
					}
				}
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
				{
					if(null != chkAddOnBenefitsHospitalCash)
					{
						chkAddOnBenefitsHospitalCash.setEnabled(true);
						//if(null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue())
						if(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
						{
							chkAddOnBenefitsHospitalCash.setValue(true);
						}
						else
						{
							chkAddOnBenefitsHospitalCash.setValue(false);
						}
						chkAddOnBenefitsHospitalCash.setValue(null);
					}
				}
				
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
				{
					//chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != chkAddOnBenefitsPatientCare)
					{
						chkAddOnBenefitsPatientCare.setEnabled(true);
						//if(null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue())
						if(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))
						{
							chkAddOnBenefitsPatientCare.setValue(true);
						}
						else
						{
							chkAddOnBenefitsPatientCare.setValue(false);
						}
						chkAddOnBenefitsPatientCare.setValue(null);
						}
				}
				if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
					chkHospitalizationRepeat.setValue(null);
				}
				resetPaymentDetails();
			}
		}
	}
	*/
	private List<Field> getListOfTxtFlds()
	{
		List<Field> lstOfTextBox = new ArrayList<Field>();
		lstOfTextBox.add(txtEmailId);
		lstOfTextBox.add(txtPanNo);
		lstOfTextBox.add(txtPayableAt);
		lstOfTextBox.add(txtReasonForChange);
		lstOfTextBox.add(txtLegalHeirName);
		lstOfTextBox.add(txtBankName);
		lstOfTextBox.add(txtBranch);
		lstOfTextBox.add(txtIfscCode);
		lstOfTextBox.add(txtCity);
		return lstOfTextBox;
		
	}
	
	private void setPaymentDetails()
	{
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(bean.getDocumentDetails().getEmailId());
			txtEmailId.setReadOnly(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(bean.getDocumentDetails().getPanNo());
			txtPanNo.setReadOnly(true);
		}
		if(null != txtPayableAt)
		{
			txtPayableAt.setReadOnly(false);
			txtPayableAt.setValue(bean.getDocumentDetails().getPayableAt());
			//txtPayableAt.setReadOnly(true);

		}
		if(null != txtReasonForChange)
		{
			txtReasonForChange.setReadOnly(false);
			txtReasonForChange.setValue(bean.getDocumentDetails().getReasonForChange());
			txtReasonForChange.setReadOnly(true);
		}
		if(null != txtLegalHeirName)
		{
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(bean.getDocumentDetails().getLegalFirstName());
			txtLegalHeirName.setReadOnly(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(bean.getDocumentDetails().getBankName());
			txtBankName.setReadOnly(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(bean.getDocumentDetails().getBranch());
			txtBranch.setReadOnly(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(bean.getDocumentDetails().getIfscCode());
			txtIfscCode.setReadOnly(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(bean.getDocumentDetails().getCity());
			txtCity.setReadOnly(true);
		}
		
		if(null != txtAccntNo && null != this.bean.getDocumentDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
		}
		
	}
	private void setPaymentDetailsIfQueryReplyYes(RODQueryDetailsDTO rodQueryDetails)
	{
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(rodQueryDetails.getEmailId());
			txtEmailId.setReadOnly(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(rodQueryDetails.getPanNo());
			txtPanNo.setReadOnly(true);
		}
		if(null != txtPayableAt)
		{
			
			txtPayableAt.setReadOnly(false);
			txtPayableAt.setValue(rodQueryDetails.getPayableAt());
		//	txtPayableAt.setReadOnly(true);

		}
		if(null != txtReasonForChange)
		{
			txtReasonForChange.setReadOnly(false);
			txtReasonForChange.setValue(rodQueryDetails.getReasonForchange());
			txtReasonForChange.setReadOnly(true);
		}
		if(null != txtLegalHeirName)
		{
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(rodQueryDetails.getLegalHairName());
			txtLegalHeirName.setReadOnly(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(rodQueryDetails.getBankName());
			txtBankName.setReadOnly(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(rodQueryDetails.getBranchName());
			txtBranch.setReadOnly(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(rodQueryDetails.getIfscCode());
			txtIfscCode.setReadOnly(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(rodQueryDetails.getCity());
			txtCity.setReadOnly(true);
		}
		
		if(null != txtAccntNo && null != rodQueryDetails.getAccNo())
		{
			txtAccntNo.setValue(rodQueryDetails.getAccNo());
		}
		
	}
	
	public void resetPaymentDetails()
	{
		List<Field> lstOfTextBox = getListOfTxtFlds();
		for (Field field : lstOfTextBox) {
			if(null != field)
			{
				field.setReadOnly(false);
				field.setValue(null);
				//field.setReadOnly(true);
			}
		}
	}
	
	private void disableBillClassification()
	{
		List<Field<?>> chkBoxList = getListOfChkBox();
		if(null != chkBoxList && !chkBoxList.isEmpty())
		{
			for (Field<?> field : chkBoxList) {
				field.setEnabled(false);
			}
		}
		
	}
	
	public void resetList()
	{
		uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
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
	 
	 public void resetQueryReplyReceived()
		{
			isQueryReplyReceived = false;	
		}

	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setEnabled(true);
			txtEmailId.setValue(tableDTO.getEmailId());
			//txtEmailId.setReadOnly(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setEnabled(true);
			txtPanNo.setValue(tableDTO.getPanNo());
			//txtPanNo.setReadOnly(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			
			if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource())
					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
			
				if(txtAccntNo != null)
					txtAccntNo.setEnabled(false);
			}else {
				if(txtAccntNo != null)
					txtAccntNo.setEnabled(true);
			}

			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			//txtAccntNo.setReadOnly(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setEnabled(true);

			txtIfscCode.setValue(tableDTO.getIfsccode());
			//txtIfscCode.setReadOnly(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setEnabled(true);

			txtBankName.setValue(tableDTO.getBankName());
			//txtBankName.setReadOnly(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setEnabled(true);
			txtCity.setValue(tableDTO.getBankCity());
			//txtCity.setReadOnly(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setEnabled(true);
			txtBranch.setValue(tableDTO.getBankBranch());
			//txtBranch.setReadOnly(true);
		}
		
		
		
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
	
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		/*Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
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
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}*/
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
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
					dialog.close();
					/*if(null != populatePreviousWindowPopup)
						populatePreviousWindowPopup.close();*/
					//fireViewEvent(MenuItemBean.CREATE_ROD, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
		}
	}

	
	private void addBenefitLister()

	{	
		chkDeath
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
				
				
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkDeath.setValue(false);
					 }
					
					fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),ReferenceTable.DEATH_BENEFIT_MASTER_VALUE,value,chkDeath,bean.getDocumentDetails().getDocAcknowledgementKey(),SHAConstants.DEATH_FLAGS);
					
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkPermanentPartialDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 					 
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPermanentPartialDisability.setValue(false);
					 }
					

					fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),ReferenceTable.PPD_BENEFIT_MASTER_VALUE,value,chkPermanentPartialDisability,bean.getDocumentDetails().getDocAcknowledgementKey(),SHAConstants.PPD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkHospitalExpensesCover
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalExpensesCover.setValue(false);
					 }
					
				
				 
				}
						
				}
				
		});
		
		
		chkPermanentTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					 
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPermanentTotalDisability.setValue(false);
					 }
					
					fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),ReferenceTable.PTD_BENEFIT_MASTER_VALUE,value,chkPermanentTotalDisability,bean.getDocumentDetails().getDocAcknowledgementKey(),SHAConstants.PTD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkTemporaryTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							 
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkTemporaryTotalDisability.setValue(false);
					 }
					
					fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),ReferenceTable.TTD_BENEFIT_MASTER_VALUE,value,chkTemporaryTotalDisability,bean.getDocumentDetails().getDocAcknowledgementKey(),SHAConstants.TTD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// boolean value = (Boolean) event.getProperty().getValue();	
				 Boolean value = (Boolean) event.getProperty().getValue();
				 String flag = null;
				 if(null != value && value )
				 {
					 flag = "Y";
				 }
				 else
				 {
					 flag = "N";
				 }
				// if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())))
				// {
				 if(null != value && value )
				 {
				 if(value)
				 {
					 
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false);
					 
					 if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkhospitalization.setValue(false);
					 }
					
					 
					 else if(validateDuplicationHospPartialHospClassification())
					 {
						 if(null != cmbDocumentType && null != cmbDocumentType.getValue())
							{
								SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
								if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
								{
									Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
							HorizontalLayout layout = new HorizontalLayout(label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							//chkhospitalization.setValue(false);
							chkhospitalization.setValue(null);
							if(null != txtHospitalizationClaimedAmt)
							{
								txtHospitalizationClaimedAmt.setValue("");
							
							}
					 }		
							}}
					 
					 if(value)
					 {
					 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
					 {
						 Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkhospitalization.setValue(false);
					 }
					 }
					 
				 }
			}
				 else
				 {
					 
					 
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
						{
						SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
						{

						addOnCoversTable.setEnabled(true);
						 optionalCoversTable.setEnabled(true);
						}
						}
					 /*chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);*/
				 }
				 
				// }
				
				
				 
				
					
				 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
						{
						SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
						{

						addOnCoversTable.setEnabled(true);
						 optionalCoversTable.setEnabled(true);
						}
						}

					}
			
				 //validateBillClassificationDetails();
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 Boolean value = (Boolean) event.getProperty().getValue();
				 String flag = null;
				 if(null != value && value )
				 {
					 flag = "Y";
				 }
				 else
				 {
					 flag = "N";
				 }
				 
				// if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())))
				// {
				 if(null != value && value)
				 {
					 
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false);
					 
					 if(validateDuplicationHospPartialHospClassification())
					 {
						 if(null != cmbDocumentType && null != cmbDocumentType.getValue())
							{
								SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
								if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
								{
						 Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);

							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						//	chkPartialHospitalization.setValue(false);
							chkPartialHospitalization.setValue(null);
						 }
					 }
					 
				 }
					 
					/* chkPostHospitalization.setEnabled(true);
					 chkPreHospitalization.setEnabled(true);
					 txtHospitalizationClaimedAmt.setEnabled(true);
*/
				 
				 }
				 else
				 {
					 addOnCoversTable.setEnabled(true);
					 optionalCoversTable.setEnabled(true);
					/* chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 txtHospitalizationClaimedAmt.setEnabled(false);*/

				 }
				 
				 if(validateBenifits())
				 {
					 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
						label.setStyleName("errMessage");
					 HorizontalLayout layout = new HorizontalLayout(
							 label);
						layout.setMargin(true);
						
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
						chkPartialHospitalization.setValue(false);
				 }
				 
				 
				 
				//	fireViewEvent(PACreateRODDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.PARTIALHOSPITALIZATION,value);
				 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
					 if(null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())
					 {
						addOnCoversTable.setEnabled(true);
					 }
					 else
					 {
						 addOnCoversTable.setEnabled(false);
					 }
					  if(null != optionalCoversTable.getValues()  && !optionalCoversTable.getValues().isEmpty() )
					 {
						optionalCoversTable.setEnabled(true);
					 }
					  else
					  {
						  optionalCoversTable.setEnabled(false);
					 }
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				if(value)
				{
				 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
				 {
					 Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
						label.setStyleName("errMessage");
					 HorizontalLayout layout = new HorizontalLayout(
							 label);
						layout.setMargin(true);
						
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
						chkPartialHospitalization.setValue(false);
				 }
				}
				 //validateBillClassificationDetails();
			//}
			}
		});
		
		
		
	}

	
	public Boolean validateBenifits() 
	{
		Boolean isError = false;
		
		if(((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true))) && 
				((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||
				(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
				(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||				
				(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
			}
			if(((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue()&& chkPermanentTotalDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||					
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			if(((null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue()  && chkPermanentPartialDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||					
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			if(((null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) || 
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) ||					
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}		
			
			if(((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||						
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			
			return isError;
	}
	
	private HorizontalLayout buildBillClassificationLayout()
	{
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		//chkhospitalization.setEnabled(false);
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		//chkPartialHospitalization.setEnabled(false);
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "hospitalizationRepeat", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalizationRepeat.setImmediate(true);
		
		//chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
		{
			chkPreHospitalization.setEnabled(false);
		}
		
		
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
		{
			chkPostHospitalization.setEnabled(false);
		}
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
		{
			chkLumpSumAmount.setEnabled(false);
		}
		
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
		{
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
		{
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		
	/*	chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);*/
		
		/*chkhospitalization.setEnabled(false);
		chkPreHospitalization.setEnabled(false);
		chkPostHospitalization.setEnabled(false);
		chkPartialHospitalization.setEnabled(false);
		*///System.out.println("---the claimType value---"+this.bean.getClaimDTO().getClaimTypeValue());
		/*
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(true);
		}
		else
		{
			chkhospitalization.setEnabled(true);
		}*/
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
			chkPreHospitalization.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
		}
		else if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(true);
			if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
			{
				chkPreHospitalization.setEnabled(true);
				//chkPreHospitalization.setEnabled(false);
				//chkPreHospitalization.setValue(true);
			}
			//chkPreHospitalization.setEnabled(true);
			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
			{
				chkPostHospitalization.setEnabled(true);
				//chkPostHospitalization.setEnabled(false);
				//chkPostHospitalization.setValue(true);
			}
			//chkPostHospitalization.setEnabled(true);
		}
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
			chkPartialHospitalization.setValue(null);
		}

		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
	//	classificationLayout1.setMargin(true);
	//	classificationLayout1.setWidth("20%");
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
	//	classificationLayout2.setMargin(true);
	//	classificationLayout2.setWidth("20%");
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
	//	classificationLayout3.setMargin(true);
	//	classificationLayout3.setWidth("40%");
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
	//	classificationLayout4.setMargin(true);
	//	classificationLayout4.setWidth("40%");
		
	/*	FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization);*/
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
	//	billClassificationLayout.setMargin(true);
	//	billClassificationLayout.setWidth("110%");
	//	addBillClassificationLister();
		
		return billClassificationLayout;
	}
	
	public void validateBenefitRepeat(Boolean isValid,Boolean ChkBoxValue,CheckBox chkBox,String benefitValue) {
		
		
		if(cmbDocumentType != null && cmbDocumentType.getValue() != null){
			SelectValue value = (SelectValue)cmbDocumentType.getValue();
			if(null != value && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(value.getId()))){
				if(isValid && ChkBoxValue)
				{
				 Label label = new Label("Benefit has been selected in another ROD for this claim. Please select another Benefit", ContentMode.HTML);
					label.setStyleName("errMessage");
				 HorizontalLayout layout = new HorizontalLayout(
						 label);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					//dialog.setWidth("35%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					chkBox.setValue(null);
					//chkHospitalizationRepeat.setValue(null);
				/*	chkDeath.setValue(false);
					chkPermanentPartialDisability.setValue(false);
					chkPermanentTotalDisability.setValue(false);
					chkTemporaryTotalDisability.setValue(false);
					chkHospitalExpensesCover.setValue(false);
					chkhospitalization.setValue(false);
					chkPartialHospitalization.setValue(false);*/
				}
				else
				{
					 if(ChkBoxValue){
						 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,benefitValue);
					 }
				}
			}
		}	
		
	}
	private Boolean validateDuplicationHospPartialHospClassification()
	{
			Boolean isError = false;
			isQueryReplyReceived = false;
			if(!isQueryReplyReceived)
				{
					if(null != docDTO && !docDTO.isEmpty())
					{
						for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
						
							if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
							{
								if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
								//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
							}
							if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
							{
								if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) &&  !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
								//if(documentDetailsDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
							}
							
							/**
							 * Below validation is added for cancel rod scenario. If an hospitalization rod is cancelled and user tries to deselect hospitalization
							 * and select hospitalization repeat, then below validation will not allow user to create an hospitalization repeat rod, since hospitalization
							 * rod is not yet created. -- Added for #3768 
							 */
								if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && null != this.chkHospitalizationRepeat.getValue())
								{
									if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && (ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
										//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
										{
											isError = true;
										}
								}
							
						}
					}
				}
			return isError ;
	}
	
public void validateCoversRepeat(Boolean isValid,String coverName) {
		
	this.isValid = isValid;
	this.coverName = coverName;
		if(isValid)
		{
		/* Label label = new Label("The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
				 label);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			//chkHospitalizationRepeat.setValue(null);
*/			
		}
		
		
	}

public void resetReconsiderationValue()
{
	if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
	{
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
		
		SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
		if(null != selValue1 && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue1.getId())))	
		{
			//txtBenifitClaimedAmnt.setValue(null);		
			
			
			/*List<ReconsiderRODRequestTableDTO> reconsiderList = new ArrayList<ReconsiderRODRequestTableDTO>();
			if(null != reconsiderRequestDetails)
			{
				reconsiderList = reconsiderRequestDetails.getValues();
			}
			
			if(null != reconsiderList && !reconsiderList.isEmpty())
			{
				reconsiderRODRequestList = new ArrayList<ReconsiderRODRequestTableDTO>();
				for (ReconsiderRODRequestTableDTO reconsiderDto : reconsiderList) {
					
					if(null != reconsiderDto.getSelect() && reconsiderDto.getSelect())
					{
						reconsiderDto.setSelect(false);
						txtBenifitClaimedAmnt.setValue(null);
					}
					reconsiderRODRequestList.add(reconsiderDto);
					
				}
				this.bean.setReconsiderRodRequestList(reconsiderRODRequestList);
			}
	*/	}
		}
	}
	
}
public void resetChkBoxValues()
{
	chkhospitalization.setValue(false);
	chkDeath.setValue(false);
	chkPartialHospitalization.setValue(false);
	chkPermanentPartialDisability.setValue(false);
	chkPermanentTotalDisability.setValue(false);
	chkTemporaryTotalDisability.setValue(false);

	
	
}

public void setParticularsByBenefitValue(String benefitValue) {
	
	
	List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
	List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
	documentCheckListValues.addAll(tableValue);
	documentCheckListValidation.setPaCreateRodDocumentDetailsPage(this);
	/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
	String lob = (String) wrkFlowMap.get(SHAConstants.LOB);*/		
	documentCheckListValidation.initPresenter(SHAConstants.PA_CREATE_ROD,SHAConstants.PA_LOB);
	
	List<SpecialSelectValue> particularsBasedOnBenefit = new ArrayList<SpecialSelectValue>();
	if(referenceData.get(benefitValue) != null){
		BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(benefitValue);
		if(items != null){
			particularsBasedOnBenefit.addAll(items.getItemIds());
		}
	}
	if(addOnCoversTable != null){
		List<AddOnCoversTableDTO> addOnCoversValue = addOnCoversTable.getValues();
		for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
			if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
				if(referenceData.get(addOnCoversTableDTO.getCovers().getValue()) != null){
					BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getCovers().getValue());
					if(items != null){
						particularsBasedOnBenefit.addAll(items.getItemIds());
					}
				}
			}
		}
	}
	
	if(optionalCoversTable != null){
		List<AddOnCoversTableDTO> optionalCover = optionalCoversTable.getValues();
		for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCover) {
			if(addOnCoversTableDTO.getOptionalCover() != null && addOnCoversTableDTO.getOptionalCover().getValue() != null){
				if(referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue()) != null){
					BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue());
					if(items != null){
						particularsBasedOnBenefit.addAll(items.getItemIds());
					}						
					}
			}
		}
	}

	BeanItemContainer<SelectValue> paritculars = new BeanItemContainer<SelectValue>(SelectValue.class);
	paritculars.addAll(particularsBasedOnBenefit);
	
	this.referenceData.put("particulars",paritculars);
	
	documentCheckListValidation.setReferenceData(referenceData);
	documentCheckListValidation.init();

	/*for (DocumentCheckListDTO documentCheckListDTO : tableValue) {
			
			if(particularsBasedOnBenefit != null && documentCheckListDTO.getBenefitId() == null){
				for (SpecialSelectValue selectValue : particularsBasedOnBenefit) {
					DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
					documentDTO.setParticulars(selectValue);	
					documentDTO.setBenefitId(selectValue.getCommonValue());
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentDTO.setReceivedStatus(setReceivedStatus);
					documentCheckListValidation.addBeanToList(documentDTO);
				}
			}
	}*/
	
	for (SpecialSelectValue specialSelectValue : particularsBasedOnBenefit) {
		Boolean isAlreadyAvailable = Boolean.FALSE;
		for (DocumentCheckListDTO documentCheckListDTO : documentCheckListValues) {
			if(documentCheckListDTO.getParticulars() != null && specialSelectValue.getValue().equalsIgnoreCase(documentCheckListDTO.getParticulars().getValue())){
				documentCheckListValidation.addBeanToList(documentCheckListDTO);
				isAlreadyAvailable = Boolean.TRUE;
				break;
			}
		}
		if(!isAlreadyAvailable){
			DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
			documentDTO.setParticulars(specialSelectValue);	
			documentDTO.setBenefitId(specialSelectValue.getCommonValue());
			SelectValue setReceivedStatus = new SelectValue();
			setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
			setReceivedStatus.setValue("Not Applicable");
			documentDTO.setReceivedStatus(setReceivedStatus);
			documentCheckListValidation.addBeanToList(documentDTO);
		}
	}
	
	
	
	//int i = 0;
	
	/*for (SelectValue selectValue : itemIds) {
		if(documentCheckListValues.size() > i){
			DocumentCheckListDTO documentCheckListDTO = documentCheckListValues.get(i);
			documentCheckListDTO.setParticulars(selectValue);
			documentCheckListValidation.addBeanToList(documentCheckListDTO);
		}else{
			DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
			documentDTO.setParticulars(selectValue);					
			SelectValue setReceivedStatus = new SelectValue();
			
			setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
			setReceivedStatus.setValue("Not Applicable");
			documentDTO.setReceivedStatus(setReceivedStatus);
			documentCheckListValidation.addBeanToList(documentDTO);
		}
		
		DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
		documentDTO.setParticulars(selectValue);					
		SelectValue setReceivedStatus = new SelectValue();
		
		setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
		setReceivedStatus.setValue("Not Applicable");
		documentDTO.setReceivedStatus(setReceivedStatus);
		documentCheckListValidation.addBeanToList(documentDTO);
		
		i++;
		
	}*/

}

public void setParticularsByAddonCovers(SelectValue value) {
	
	if(value != null && value.getId() != null &&( value.getId().equals(ReferenceTable.OUTPATIENT_EXPENSES) || 
			value.getId().equals(ReferenceTable.EARNING_PARENT_SI))){
		Boolean uncheckBenefitsValue = uncheckBenefitsValue();
		if(chkDeath != null){
			chkDeath.setValue(false);
		}
		if(chkTemporaryTotalDisability != null){
			chkTemporaryTotalDisability.setValue(false);
		}
		
		if(chkPermanentTotalDisability != null){
			chkPermanentTotalDisability.setValue(false);
		}
		
		if(chkPermanentPartialDisability != null){
			chkPermanentPartialDisability.setValue(false);
		}
		
		if(chkhospitalization != null){
			chkhospitalization.setValue(false);
		}
		
		if(chkPartialHospitalization != null){
			chkPartialHospitalization.setValue(false);
		}
		if(! uncheckBenefitsValue){
		showErrorMessage("Can not select any benefit along with Outpatient Expences");
		}
	}

	
	/*if(documentCheckListValidation != null){
		
		List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
		List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
		documentCheckListValues.addAll(tableValue);
		documentCheckListValidation.setPaCreateRodDocumentDetailsPage(this);;
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		String lob = (String) wrkFlowMap.get(SHAConstants.LOB);		
		documentCheckListValidation.initPresenter(SHAConstants.PA_CREATE_ROD,SHAConstants.PA_LOB);
		
		List<SpecialSelectValue> particularsBasedOnBenefit = new ArrayList<SpecialSelectValue>();
		
		String benefitValue = "";
		if(chkDeath != null && chkDeath.getValue() != null && chkDeath.getValue()){
			benefitValue = SHAConstants.DEATH_FLAGS;
		}else if(chkTemporaryTotalDisability != null && chkTemporaryTotalDisability.getValue() != null && chkTemporaryTotalDisability.getValue()){
			benefitValue = SHAConstants.TTD;
		}else if(chkPermanentTotalDisability != null && chkPermanentTotalDisability.getValue() != null && chkPermanentTotalDisability.getValue()){
			benefitValue = SHAConstants.PTD;
		}else if(chkPermanentPartialDisability != null && chkPermanentPartialDisability.getValue() != null && chkPermanentPartialDisability.getValue()){
			benefitValue = SHAConstants.PPD;
		}
		if(referenceData.get(benefitValue) != null){
			BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(benefitValue);
			if(items != null){
				particularsBasedOnBenefit.addAll(items.getItemIds());
			}
		}
		if(addOnCoversTable != null){
			List<AddOnCoversTableDTO> addOnCoversValue = addOnCoversTable.getValues();
			for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
				if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
					if(referenceData.get(addOnCoversTableDTO.getCovers().getValue()) != null){
						BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getCovers().getValue());
						if(items != null){
							particularsBasedOnBenefit.addAll(items.getItemIds());
						}
					}
				}
			}
		}
		
		if(optionalCoversTable != null){
			List<AddOnCoversTableDTO> optionalCover = optionalCoversTable.getValues();
			for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCover) {
				if(addOnCoversTableDTO.getOptionalCover() != null && addOnCoversTableDTO.getOptionalCover().getValue() != null){
					if(referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue()) != null){
						BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue());
						if(items != null){
							particularsBasedOnBenefit.addAll(items.getItemIds());
						}						
						}
				}
			}
		}
	
		BeanItemContainer<SelectValue> paritculars = new BeanItemContainer<SelectValue>(SelectValue.class);
		paritculars.addAll(particularsBasedOnBenefit);
		
		this.referenceData.put("particulars",paritculars);
		
		documentCheckListValidation.setReferenceData(referenceData);
		documentCheckListValidation.init();
		
		for (SpecialSelectValue specialSelectValue : particularsBasedOnBenefit) {
			Boolean isAlreadyAvailable = Boolean.FALSE;
			for (DocumentCheckListDTO documentCheckListDTO : documentCheckListValues) {
				if(documentCheckListDTO.getParticulars() != null && specialSelectValue.getValue().equalsIgnoreCase(documentCheckListDTO.getParticulars().getValue())){
					documentCheckListValidation.addBeanToList(documentCheckListDTO);
					isAlreadyAvailable = Boolean.TRUE;
					break;
				}
			}
			if(!isAlreadyAvailable){
				DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
				documentDTO.setParticulars(specialSelectValue);	
				documentDTO.setBenefitId(specialSelectValue.getCommonValue());
				SelectValue setReceivedStatus = new SelectValue();
				setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
				setReceivedStatus.setValue("Not Applicable");
				documentDTO.setReceivedStatus(setReceivedStatus);
				documentCheckListValidation.addBeanToList(documentDTO);
			}
		}

	
	}
*/
	
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



public void setDropDownValues(BeanItemContainer<SelectValue> category) 
{	

	cmbCategory.setContainerDataSource(category);
	cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbCategory.setItemCaptionPropertyId("value");		
   
	if(null != category){
		SelectValue defaultCategory = category.getIdByIndex(0);
		cmbCategory.setValue(defaultCategory);
	}
       
}	

public Boolean validationforOutPatientExpenses(){
	
	Boolean isOutPatient = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.OUTPATIENT_EXPENSES)){
			isOutPatient = uncheckBenefitsValue();
			
			break;
		}
		
	}
	
	return isOutPatient;
}

public Boolean validationforOutEarningParentSI(){
	
	Boolean isEarningParentSI = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.EARNING_PARENT_SI)){
			isEarningParentSI = uncheckBenefitsValue();
			break;
		}
		
	}
	
	return isEarningParentSI;
}

	public Boolean uncheckBenefitsValue(){
	
				
		if(chkDeath != null && chkDeath.getValue() != null && chkDeath.getValue()){
			return false;
		}
		if(chkPermanentPartialDisability != null && chkPermanentPartialDisability.getValue() != null && chkPermanentPartialDisability.getValue()){
			return false;
		}
		
		if(chkPermanentTotalDisability != null && chkPermanentTotalDisability.getValue() != null && chkPermanentTotalDisability.getValue()){
			return false;
		}
		
		if(chkTemporaryTotalDisability != null && chkTemporaryTotalDisability.getValue() != null && chkTemporaryTotalDisability.getValue()){
			return false;
		}
		
		if( chkhospitalization != null && chkhospitalization.getValue() != null && chkhospitalization.getValue()){
			return false;
		}
		
		if(chkPartialHospitalization != null && chkPartialHospitalization.getValue() != null && chkPartialHospitalization.getValue()){
			return false;
		}
		
		return true;
	
	
}
	
	public void resetBenefitsValue(){
		if(chkDeath != null){
			chkDeath.setValue(false);
		}
		if(chkTemporaryTotalDisability != null){
			chkTemporaryTotalDisability.setValue(false);
		}
		
		if(chkPermanentTotalDisability != null){
			chkPermanentTotalDisability.setValue(false);
		}
		
		if(chkPermanentPartialDisability != null){
			chkPermanentPartialDisability.setValue(false);
		}
		
		if(chkhospitalization != null){
			chkhospitalization.setValue(false);
		}
		
		if(chkPartialHospitalization != null){
			chkPartialHospitalization.setValue(false);
		}
	}
	
	public Boolean validatePaymentCancellation(){
  		Boolean hasError = false;
  		if(bean.getDocumentDetails().getPaymentCancellationNeeded() != null && 
  				bean.getDocumentDetails().getPaymentCancellationNeeded()){
  			hasError = true;
  		}
  		return hasError;
  	}
	public Boolean alertForvalidatePaymentCancellationFlag() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.ALERT_FOR_PAYMENT_CANCELLATION + "</b>",
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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
//				wizard.getNextButton().setEnabled(false);
					
			}
		});
		return true;
	}
	
	public void buildNomineeLayout() {
		
		if(nomineeDetailsTable != null) { 
			documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
		}
		
		if(chkNomineeDeceased != null){
			documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
		}

		if(legalHeirLayout != null) {
			documentDetailsPageLayout.removeComponent(legalHeirLayout);
		}
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		chkNomineeDeceased = null;
		if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) { 
			nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.generateSelectColumn();
			nomineeDetailsTable.setScreenName(SHAConstants.PA_CREATE_ROD);
			nomineeDetailsTable.setPolicySource(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
			PreauthDTO preauthDto = bean.getPreauthDTO();
			preauthDto.setNewIntimationDTO(bean.getNewIntimationDTO());
			SelectValue patientStatus = new SelectValue(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB, ""); 
			preauthDto.getPreauthDataExtractionDetails().setPatientStatus(patientStatus);
			viewSearchCriteriaWindow.setPreauthDto(preauthDto);
			nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);
			chkNomineeDeceased = new CheckBox("Nominee Deceased");
			if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getNomineeDeceasedFlag() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				chkNomineeDeceased.setValue(Boolean.TRUE);
			}
		}	
		
		documentDetailsPageLayout.addComponent(nomineeDetailsTable);
		if(chkNomineeDeceased != null){
			documentDetailsPageLayout.addComponent(chkNomineeDeceased);
			addNomineeDeceasedListener();
		}
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
				
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Map<String,Object> refData = new HashMap<String, Object>();
		relationshipContainer.addAll(bean.getPreauthDTO().getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		legalHeirDetails.init(bean.getPreauthDTO());
		legalHeirDetails.setPresenterString(SHAConstants.PA_CREATE_ROD);
		legalHeirLayout.addComponent(legalHeirDetails);
		documentDetailsPageLayout.addComponent(legalHeirLayout);

		if(isNomineeDeceased()){
			enableLegalHeir = Boolean.TRUE;
			nomineeDetailsTable.setEnabled(false);
		}
		
		if(enableLegalHeir) {
			
			legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
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
			btnIFCSSearch.setEnabled(false);
			
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
				bean.getDocumentDetails().setPayeeName(value);
				bean.getDocumentDetails().getPayeeName().setSourceRiskId(value.getSourceRiskId());
				bean.setSourceRiskID(value.getSourceRiskId());
//				bankDetailsTableObj =  bankDetailsTableInstance.get();
				bankDetailsTableObj.init(bean);
				bankDetailsTableObj.initPresenter(SHAConstants.PA_CREATE_ROD);
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
	 private void getValuesForNameDropDown(Boolean accidentDeath)
	 {

			Policy policy = policyService.getPolicy(bean.getClaimDTO()
					.getNewIntimationDto().getPolicy().getPolicyNumber());
			if (null != policy) {
				SelectValue selectValue = null;				
					List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());

					if (null != policy.getProduct().getKey()
							&& (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()) || ReferenceTable.getRevisedCriticareProducts().containsKey(policy.getProduct().getKey()))) {

						pNomineeDetails = intimationService
								.getPolicyNomineeListForInsured(bean
										.getClaimDTO().getNewIntimationDto()
										.getInsuredPatient().getKey());
					}

					for (PolicyNominee pNominee : pNomineeDetails) {
						selectValue = new SelectValue();
						selectValue.setId(pNominee.getKey());
						selectValue.setValue(pNominee.getNomineeName());
						if (accidentDeath != null && !accidentDeath &&
								bean.getDocumentDetails().getDocumentReceivedFromValue() != null
								&& SHAConstants.DOC_RECEIVED_FROM_INSURED.equals(bean.getDocumentDetails().getDocumentReceivedFromValue())
								&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
							payeeNameList.addItem(selectValue);
						}else{
							payeeNameList.removeItem(selectValue);
						}	
						selectValue = null;
					}
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

							if(bean.getPreauthDTO().getLegalHeirDTOList() != null && !bean.getPreauthDTO().getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
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
