package com.shaic.claim.rod.wizard.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
/*import org.vaadin.dialogs.ConfirmDialog;*/
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
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.popup.ChangeHospitalViewImpl;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class CreateRODDocumentDetailsPage  extends ViewComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private RODQueryDetailsTable rodQueryDetails;
	
	@EJB
	private MasterService masterService;
	
	/***
	 * Change Hospital Popup window created by Yosuva
	 */
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchAccountTypeWindow;
	
	@Inject
	private Instance<ChangeHospitalViewImpl> changeHospitalInstance;
	
	private ChangeHospitalViewImpl changeHospitalObj;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;
	
	////private static Window popup;
	
	/*@Inject
	private CreateRODReconsiderRequestTable reconsiderRequestDetails;*/
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;

	
/*	@Inject
	private Instance<DocumentCheckListValidationTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationTable documentCheckListValidation;*/
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	Map<String, Object> referenceData = new WeakHashMap<String, Object>();
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	private LegalHeirDetails legalHeirDetails;
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private PopupDateField documentsReceivedDate;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	//private TextField txtAdditionalRemarks;
	private TextArea txtAdditionalRemarks;
	
	private DateField dateOfAdmission;
	
	private TextField txtHospitalName;
	
	private ComboBox cmbInsuredPatientName;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;
	
	private VerticalLayout documentDetailsPageLayout;
	
	private OptionGroup optPaymentMode;
	
	public ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirName;
	
	private TextField txtAccountPref;
	
	private TextField txtAccType;
	
	private Button btnAccPrefSearch;
	
	private HorizontalLayout accPrefLayout;
	
	private TextField txtRelationship;
	
	private TextField txtNameAsPerBank;
	/*private TextField txtLegalHeirFirstNmme;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;*/
	
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
	
	private FormLayout detailsLayout1;
	
	private FormLayout detailsLayout2;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout otherBenefitsLayout;
	
	private VerticalLayout paymentDetailsLayout;
	
	//private VerticalLayout legalHeirLayout;

	private BeanItemContainer<SelectValue> reconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	
	private BeanItemContainer<SelectValue> payeeNameList;
	
	private BeanItemContainer<SelectValue> insuredPatientList;
	
	private HorizontalLayout insuredLayout;
	
	//private List<DocumentDetailsDTO> docsDetailsList ;
	
	private TextField txtHospitalizationClaimedAmt;
	
	private TextField txtPreHospitalizationClaimedAmt;
	
	private TextField txtPostHospitalizationClaimedAmt;
	
	private TextField txtOtherBenefitsClaimedAmnt;
	
    private CheckBox chkHospitalCash;
	
	private TextField txtHospitalCashClaimedAmnt;
	 
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	 
	//private  DocumentDetailsDTO docDTO ;
	 private List<DocumentDetailsDTO> docDTO = new ArrayList<DocumentDetailsDTO>();
	 
	 private static Boolean isQueryReplyReceived = null; 
	 
	 @Inject
	 private ViewDetails viewDetails;
	 
	 private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	 
	 
	 public Map<String,Boolean> reconsiderationMap = new WeakHashMap<String, Boolean>();
	
	 public String hospitalizationClaimedAmt = "";
		
	 public String preHospitalizationAmt = "";
		
	 public String postHospitalizationAmt= "";
	 
	 public String otherBenefitAmnt = "";
	 
	 public String HospitalCashClaimedAmnt = "";
	 
	 
	 private Boolean isReconsider ;
	 public ReconsiderRODRequestTableDTO reconsiderDTO = null;
	 
	 private List<UploadDocumentDTO> uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
	 
	 //private String chqEmailId;
	 
	 //private String chqPanNo;
	 
	 //private String chqPayableAt;
	 
	 //private String chqResonForChange;
	 
	 //private String chqLegalHeirName;
	 
	 //private String chqPayeeName;
	 
	 //private String bnqAccntNo;
	 //private String bnqIFSCode;
	 
	 //private String bnqBranch;
	 
	 //private String bnqBankName;
	 
	 //private String bnqCity;
	 
	 private ComboBox cmbReasonForReconsideration;
	 
	 private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 
	 private BeanItemContainer<SelectValue> patientStatusContainer;
	 
	 private Boolean isFinalEnhancement = false;
	 
	 private DateField dateOfDischarge;
	 
	 private Boolean isReasonForChangeReq = false;
	 
	 private OptionGroup optPaymentCancellation;
	 
	 private HorizontalLayout queryDetailsLayout;
	 
	 @Inject
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Button btnPopulatePreviousAccntDetails;
	 
	 
	 private Window populatePreviousWindowPopup;
	 
	 private Button btnOk;
	 
	 private Button btnCancel;
	 
	 private VerticalLayout previousPaymentVerticalLayout;
	 
	 private HorizontalLayout previousAccntDetailsButtonLayout;
	 
	 private Boolean lumpSumValidationFlag = false;
	 
	private TextField txtPayModeChangeReason;
	
	 private VerticalLayout vLayout ;

	 @EJB
		private ReimbursementService reimbursementService;
	 
	 @EJB
		private IntimationService intimationService;
	 
	 @EJB
		private PolicyService policyService;
	 
		@EJB
		private DBCalculationService dbCalculationService;
	 
	 private OptionGroup optDocumentVerified;
	 
	 private ComboBox cmbPatientStatus;
	 
	 private PopupDateField deathDate;
	 
	 private TextField txtReasonForDeath;
	 
	 private Button btnCitySearch;
	 
	 @Inject
	 private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	 
	 @Inject 
	 private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
		
	 private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	 
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
		
	// private static boolean isReconsiderRequestFlag = false;

	private FormLayout legaHeirLayout;
	
	 private ComboBox cmbDiagnosisHospitalCash;
	 
	 private BeanItemContainer<SelectValue> diagnosisHospitalCashContainer;
	 
	
	//private TextField txtHospitalPayeeName;

	  
	//private Panel billClassificationPanel;
	 
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	private VerticalLayout nomineeLegalLayout;
	
	private TextField txtPaymentPartyMode;
	
	private String presenterString;
	
	private CheckBox grievanceRepresentationOpt;
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard,String presenterString) {
		this.bean = bean;
		this.wizard = wizard;
		this.presenterString = presenterString;
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		bankDetailsTableObj =  bankDetailsTableInstance.get();
		
		vLayout = new VerticalLayout();
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
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}

	public Boolean alertMessageForPED() {
		Label successLabel = new Label(SHAConstants.PED_RAISE_MESSAGE);
		pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
		pedRaiseDetailsTableObj.init("", false, false);
		pedRaiseDetailsTableObj.initViewAck(bean.getNewIntimationDTO().getPolicy().getKey());

		VerticalLayout layout = new VerticalLayout(successLabel,pedRaiseDetailsTableObj.getTable());
		/*layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout,
						buttonsNamewithType,
						GalaxyTypeofMessage.INFORMATION.toString());
		Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		okButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				// dialog.close();
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public Component getContent() {
		
		//GLX2020168
		/*Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		Date admissionDate = this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getAdmissionDate();
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		Date currnetDate = new Date();
		boolean dateRange= SHAUtils.validateRangeDate(policyFromDate);
		boolean currentDateRange= SHAUtils.validateRangeDate(currnetDate);
		boolean validation= reimbursementService.getDiagnosisICDValidtion(bean.getNewIntimationDTO().getKey());
		if(validation && dateRange && currentDateRange && ReferenceTable.getCovidProducts().contains(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) && (diffDays != null && diffDays < 30)){
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		}*/
		
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		/*System.out.println("Document Type****************"+this.bean.getDocumentDetails().getDocumentType().getCommonValue());
		System.out.println("Document Type Value****************"+this.bean.getDocumentDetails().getDocumentType().getValue());*/
		
		//CR2019217
		//changes done for SM agent percentage by noufel on 13-01-2020
		String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
		String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
		if((bean.getPreauthDTO().getIcrAgentValue() != null && !bean.getPreauthDTO().getIcrAgentValue().isEmpty() 
				&& (Integer.parseInt(bean.getPreauthDTO().getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
				|| bean.getPreauthDTO().getSmAgentValue() != null && !bean.getPreauthDTO().getSmAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getPreauthDTO().getSmAgentValue()) >= Integer.parseInt(smpercentage))){
			SHAUtils.showICRAgentAlert(bean.getPreauthDTO().getIcrAgentValue(), agentpercentage, bean.getPreauthDTO().getSmAgentValue(), smpercentage);
		}
		
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty()){
			showCMDAlert(memberType);
		}
		else if(null != this.bean.getPreauthDTO().getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getPreauthDTO().getTopUpPolicyAlertFlag())){
			showTopUpAlertMessage(this.bean.getPreauthDTO().getTopUpPolicyAlertMessage());
		}
//		if(this.bean.getClaimCount() >1){
//			alertMessageForClaimCount(this.bean.getClaimCount());
//		}else
		else if(null != bean.getPreauthDTO().getPolicyDto().getGmcPolicyType() && bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber() != null &&
				(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
						bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
			showAlertForGMCParentLink(bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber());
		}
		else if(null != bean.getPreauthDTO().getPolicyDto().getPaymentParty()){
			showAlertForGMCPaymentParty(bean.getPreauthDTO().getPolicyDto().getPaymentParty(),bean.getPreauthDTO().getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
		}
		else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}
		else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
			showICRMessage();
		}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}		
		/*else if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
		}*/
		else if(bean.getIsPEDInitiated()){
			alertMessageForPED();
		}
		
		if(bean.getNewIntimationDTO().getHospitalDto() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getDiscount() != null
				&& SHAConstants.CAPS_YES.equalsIgnoreCase(bean.getNewIntimationDTO().getHospitalDto().getDiscount())) {
			String hsptRemark = bean.getNewIntimationDTO().getHospitalDto().getDiscountRemark();
			if(hsptRemark != null){
				SHAUtils.showMessageBox(SHAConstants.HOSPITAL_DISCOUNT_ALERT_MSG + hsptRemark,"Information - Hospital Discount");

			}
		}
		
		
		otherBenefitsLayout = new VerticalLayout();	
		
			docDTO = new ArrayList<DocumentDetailsDTO>();
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		isReconsider= false;
				
		documentDetailsPageLayout = new VerticalLayout();
	//	documentDetailsPageLayout.setSpacing(false);
	//	documentDetailsPageLayout.setMargin(true);
		
		reconsiderationLayout = new VerticalLayout();		
		reconsiderationLayout.setSpacing(false);
		
		nomineeLegalLayout = new VerticalLayout();
		nomineeLegalLayout.setSpacing(false);
		
		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			documentDetailsLayout.setEnabled(false);
		}
	//	documentDetailsLayout.setWidth("100%");
		//documentDetailsLayout.setMargin(true);
		
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.CREATE_ROD);
		documentCheckListValidation.init();
		
	//	documentCheckListValidation.init("", false);
		//documentCheckListValidation.setReference(this.referenceData);
		
		VerticalLayout docCheckListLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListLayout.setCaption("Checklist Validation");
	//	docCheckListLayout.setWidth("10%");
		docCheckListLayout.setHeight("70%");
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			docCheckListLayout.setEnabled(false);
		}
		
	
		
		
		PreauthDTO preauthDto = new PreauthDTO();
		preauthDto.setNewIntimationDTO(this.bean.getNewIntimationDTO());
		preauthDto.setClaimDTO(this.bean.getClaimDTO());
		preauthDto.setShouldDisableSection(this.bean.getShouldDisableSection());
		if(preauthDto.getShouldDisableSection() && bean.getDocumentDetails().getReconsiderationRequestValue() != null && bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("Yes")) {
			preauthDto.setShouldDisableSection(true);
		}
		
		
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(preauthDto,presenterString);
		sectionDetailsListenerTableObj.setReferenceData(referenceData);
		SectionDetailsTableDTO sectionDTO = new SectionDetailsTableDTO();
		SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(this.bean.getSectionList().getItemIds(), this.bean.getClaimDTO().getClaimSectionCode() != null ? this.bean.getClaimDTO().getClaimSectionCode() : ReferenceTable.HOSPITALIZATION_SECTION_CODE);
		sectionDTO.setSection(correctSelectValue);
		bean.getDocumentDetails().setSectionDetailsDTO(sectionDTO);
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getDocumentDetails().getSectionDetailsDTO());

		
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
		
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
		{
			cmbInsuredPatientName.setEnabled(true);
		}
		else if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			cmbInsuredPatientName.setEnabled(false);
		}		
		
		
		
		FormLayout fLayout = new FormLayout(dateOfAdmission,dateOfDischarge);
		fLayout.setMargin(false);
		fLayout.setSpacing(false);
		fLayout.setWidth("40%");
		FormLayout fLayout1 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
		fLayout1.setMargin(false);
		fLayout1.setSpacing(false);if(cmbReconsiderationRequest != null) {
			if(this.bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes") && !cmbReconsiderationRequest.isEnabled()) {
				preauthDto.setIsReconsiderationRequest(true);
			}
		}
		fLayout1.setWidth("40%");
		FormLayout fLayout2 = new FormLayout(changeHospitalBtn);
		fLayout2.setWidth("20%");
		//insuredLayout  = new HorizontalLayout(new FormLayout(dateOfAdmission,dateOfDischarge),new FormLayout(), new FormLayout(txtHospitalName, cmbInsuredPatientName),new FormLayout(changeHospitalBtn));
		//HorizontalLayout insuredLayout  = new HorizontalLayout(new FormLayout(dateOfAdmission),new FormLayout(txtHospitalName, cmbInsuredPatientName),changeHospitalBtn);
		insuredLayout  = new HorizontalLayout(fLayout,fLayout1,fLayout2);
		insuredLayout.setWidth("100%");
		insuredLayout.setMargin(false);
		insuredLayout.setSpacing(false);
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			insuredLayout.setEnabled(false);
		}
		//insuredLayout.setWidth("5px");
	/*	VerticalLayout docCheckListValidationLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListValidationLayout.setCaption("Checklist Validation");
		docCheckListValidationLayout.setMargin(true);*/
		//docCheckListValidationLayout.setMargin(true);
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Policy<br><br>Payment Information / Request");
		paymentDetailsLayout.setCaptionAsHtml(true);
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			paymentDetailsLayout.setEnabled(false);
		} 
//		paymentDetailsLayout.setSpacing(true);
//		paymentDetailsLayout.setMargin(true);
		
		/*legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Map<String,Object> refData = new HashMap<String, Object>();
		relationshipContainer.addAll(bean.getPreauthDTO().getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		legalHeirDetails.init(bean.getPreauthDTO());
		legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDto().getLegalHeir());
		List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
		list.add(bean.getLegalHeirDto());
//		legalHeirDetails.setTableList(list);
		legalHeirLayout = new VerticalLayout();
		legalHeirLayout.addComponent(legalHeirDetails);*/
		
		
		btnIFCSSearch = new Button();
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())) {
			
			btnIFCSSearch.setEnabled(false);
		}
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		btnCitySearch = new Button();
		
		btnCitySearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnCitySearch.setIcon(new ThemeResource("images/search.png"));
		
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

		referenceData.put("sectionDetails", this.bean.getSectionList());
		
		
		
		//paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
		
	//	documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,reconsiderationLayout,insuredLayout,documentCheckListValidation,paymentDetailsLayout);
		documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,this.sectionDetailsListenerTableObj,reconsiderationLayout,insuredLayout,docCheckListLayout,paymentDetailsLayout,nomineeLegalLayout);
		//documentDetailsPageLayout.setWidth("2000px");
		documentDetailsPageLayout.setMargin(true);
		
		addListener();
		setTableValues();
		
		if(null != this.bean.getDocumentDetails().getDocumentReceivedFromValue() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			fireViewEvent(CreateRODDocumentDetailsPresenter.HOSPITAL_PAYMENT_DETAILS, this.bean);
		}
		if(null != dateOfAdmission)
		validateAdmissionDate(dateOfAdmission.getValue());

		if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
		{
			chkLumpSumAmount.setEnabled(false);
		}
		
		
		if(cmbReconsiderationRequest != null) {
			if(this.bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes")) {
				sectionDetailsListenerTableObj.setEnabled(false);
			}
		}
		
		
		return documentDetailsPageLayout;
	}
	
	/*private void getDocumentTableDataList()
	{
		fireViewEvent(DocumentDetailsPresenter.SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}*/
	
	/*private void alertMessageForClaimCount(Long claimCount){
		
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
		
		
		popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
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
				if(bean.getIsPEDInitiated()){
					alertMessageForPED();
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From" , "documentsReceivedFrom" , ComboBox.class);
		cmbDocumentsReceivedFrom.setEnabled(true);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", PopupDateField.class);
		documentsReceivedDate.setValue(new Date());
		documentsReceivedDate.setEnabled(true);
		documentsReceivedDate.setTextFieldEnabled(false);
		
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt", "modeOfReceipt", ComboBox.class);
		//Vaadin8-setImmediate() cmbModeOfReceipt.setImmediate(true);
		
		cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		
		
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration.setWidth("295px");
		/*loadReasonForReconsiderationDropDown();*/
		
		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks","additionalRemarks",TextArea.class);
		txtAdditionalRemarks.setMaxLength(100);
		
		
		CSValidator remarksValidator = new CSValidator();
		remarksValidator.extend(txtAdditionalRemarks);
		remarksValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		remarksValidator.setPreventInvalidTyping(true);
		
		optDocumentVerified = (OptionGroup) binder.buildAndBind("Verified and entered the\namount claimed based on the\noriginal bills received" , "documentVerification" , OptionGroup.class);
		optDocumentVerifiedListener();
		//optDocumentVerified.setRequired(true);
		optDocumentVerified.addItems(getReadioButtonOptions());
		optDocumentVerified.setItemCaption(true, "Yes");
		optDocumentVerified.setItemCaption(false, "No");
		optDocumentVerified.setStyleName("horizontal");
		//Vaadin8-setImmediate() optDocumentVerified.setImmediate(true);
		optDocumentVerified.setEnabled(false);
		
		cmbPatientStatus = (ComboBox) binder.buildAndBind("Patient Status",
				"patientStatus", ComboBox.class);
		//Comment for CR 078 CHange flw in hosp cash
		cmbDiagnosisHospitalCash = (ComboBox) binder.buildAndBind("Diagnosis Hospital Cash",
				"diagnosisHospitalCash", ComboBox.class);
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
		txtHospitalizationClaimedAmt.addBlurListener(getHospitalLisenter());
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed \n(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPreHospitalizationClaimedAmt.addBlurListener(getPreHospLisenter());
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n (Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
		txtPostHospitalizationClaimedAmt.addBlurListener(getPostHospLisenter());
		txtOtherBenefitsClaimedAmnt = binder.buildAndBind("Amount Claimed (Other Benefits)", "otherBenefitclaimedAmount",TextField.class);
		txtOtherBenefitsClaimedAmnt.addBlurListener(getOtherBenefitLisener());
		txtHospitalCashClaimedAmnt = binder.buildAndBind("Amount Claimed (Hospital Cash)", "HospitalCashClaimedAmnt",TextField.class);
		txtHospitalCashClaimedAmnt.addBlurListener(gethospitalCash());
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		txtOtherBenefitsClaimedAmnt.setEnabled(false);
		txtOtherBenefitsClaimedAmnt.setNullRepresentation("");
		txtOtherBenefitsClaimedAmnt.setMaxLength(15);
		txtHospitalCashClaimedAmnt.setEnabled(false);
		txtHospitalCashClaimedAmnt.setNullRepresentation("");
		txtHospitalCashClaimedAmnt.setMaxLength(15);
		
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
		

		CSValidator otherBenefitClaimedAmtValidator = new CSValidator();
		otherBenefitClaimedAmtValidator.extend(txtOtherBenefitsClaimedAmnt);
		otherBenefitClaimedAmtValidator.setRegExp("^[0-9]*$");
		otherBenefitClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator hospitalCashClaimedAmntValidator = new CSValidator();
		hospitalCashClaimedAmntValidator.extend(txtHospitalCashClaimedAmnt);
		hospitalCashClaimedAmntValidator.setRegExp("^[0-9]*$");
		hospitalCashClaimedAmntValidator.setPreventInvalidTyping(true);
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
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( (null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) ||
						 (null != this.bean.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getLumpSumAmountFlag())) || 
						 (null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
				  )))
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
				  || (null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))))
				
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
		
		
		
		
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			dateOfAdmission.setReadOnly(true);
			dateOfAdmission.setEnabled(false);
			
			txtHospitalName.setReadOnly(true);
			txtHospitalName.setEnabled(false);
		}*/
		
		
		grievanceRepresentationOpt = (CheckBox) binder.buildAndBind("" , "grievanceRepresentation" , CheckBox.class);
		addgrievanceRepresentationListener();
		grievanceRepresentationOpt.setEnabled(false);
		HorizontalLayout grievanceHLayout = new HorizontalLayout(grievanceRepresentationOpt);
		grievanceHLayout.setCaption("Grievance representation");
		
		detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbReconsiderationRequest,optDocumentVerified,grievanceHLayout,cmbPatientStatus);
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate,
					cmbReconsiderationRequest,optDocumentVerified, cmbPatientStatus,cmbDiagnosisHospitalCash);
		}
		
		//detailsLayout1.setMargin(true);
	//	detailsLayout1.setSpacing(true);
		detailsLayout2 = new FormLayout(cmbModeOfReceipt,txtAdditionalRemarks,txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitsClaimedAmnt,txtHospitalCashClaimedAmnt);
		detailsLayout2.setMargin(true);
	//	detailsLayout2.setSpacing(true);	
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
	//	docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		docDetailsLayout.setWidth("100%");
		
		setRequiredAndValidation(cmbDocumentsReceivedFrom);
		setRequiredAndValidation(documentsReceivedDate);
		setRequiredAndValidation(cmbModeOfReceipt);
		setRequiredAndValidation(cmbPatientStatus);
		
		
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		mandatoryFields.add(cmbPatientStatus);
		cmbDiagnosisHospitalCash.setVisible(false);
		
		//added for new product
		if(/*this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				||*/ this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			mandatoryFields.add(cmbDiagnosisHospitalCash);
			setRequiredAndValidation(cmbDiagnosisHospitalCash);
			cmbDiagnosisHospitalCash.setVisible(true);
			cmbDiagnosisHospitalCash.setRequired(true);
		}
		
		cmbDocumentsReceivedFrom.setReadOnly(true);
		documentsReceivedDate.setReadOnly(true);
		cmbModeOfReceipt.setReadOnly(true);
		txtAdditionalRemarks.setReadOnly(true);
		
		
		//docDetailsLayout.setCaption("Document Details");
		
		if(this.bean.getDocumentDetails()!=null &&this.bean.getDocumentDetails().getReasonForReconsiderationRequestValue()!=null){
//			if(bean.getDocumentDetails().getHospitalizationClaimedAmount()!=null ||bean.getDocumentDetails().getPreHospitalizationClaimedAmount()!=null ||bean.getDocumentDetails().getPostHospitalizationClaimedAmount()!=null||bean.getDocumentDetails().getOtherBenefitclaimedAmount()!=null){
				optDocumentVerified.setValue(null);
				optDocumentVerified.setEnabled(true);
//			}

		}
		
		
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
			 this.cmbReasonForReconsideration.setReadOnly(false);
			 if(null != reasonForReconsiderationRequest && (null != reasonForReconsideration && !("").equalsIgnoreCase(reasonForReconsideration)))
			 {
				 for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
				 	{

						if ((reasonForReconsideration).equalsIgnoreCase(reasonForReconsiderationRequest.getIdByIndex(i).getValue()))

						{
							this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
							break;
						}
					}
			 }
			 this.cmbReasonForReconsideration.setReadOnly(true);
		 }
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
		
		
		chkOtherBenefits = binder.buildAndBind("Other Benefits", "otherBenefits", CheckBox.class);
		
		chkHospitalCash = binder.buildAndBind("Hospital Cash", "hospitalCash", CheckBox.class);
		
		chkOtherBenefits.setValue(false);
		
		chkHospitalCash.setEnabled(false);
		
		chkHospitalCash.setValue(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
		
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
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
		{
			if(null != chkOtherBenefits){
			chkOtherBenefits.setEnabled(false);
			}
		}
		//GLX2020017
		if(null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
		{
			if(chkOtherBenefits != null){
				chkOtherBenefits.setEnabled(false);
			}
			if(chkAddOnBenefitsHospitalCash !=null){
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
		}
		/*Below commented bcoz below code only for doc received from Insured only
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
			chkPreHospitalization.setEnabled(true);
		}*/
		
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
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits,chkHospitalCash);
	/*	FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization);*/
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4,classificationLayout5);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setMargin(false);
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
	//	billClassificationLayout.setMargin(true);
	//	billClassificationLayout.setWidth("110%");
		addBillClassificationLister();
		
		if(null != this.bean.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getOtherBenefitsFlag()))
		{
			if(null != chkOtherBenefits){
			chkOtherBenefits.setValue(true);
			}
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
		{
			if(null != chkHospitalCash){
				chkHospitalCash.setValue(true);
			}
		}
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			if(null != chkhospitalization) {
				chkhospitalization.setEnabled(false); }
				if(null != chkHospitalizationRepeat) {
				chkHospitalizationRepeat.setEnabled(false); }
				if(null != chkPreHospitalization) {
				chkPreHospitalization.setEnabled(false); }
				if(null != chkLumpSumAmount) {
				chkLumpSumAmount.setEnabled(false); }
				if(null != chkPostHospitalization) {
				chkPostHospitalization.setEnabled(false); }
				if(null != chkPartialHospitalization) {
				chkPartialHospitalization.setEnabled(false); }
				if(null != chkAddOnBenefitsHospitalCash) {
				chkAddOnBenefitsHospitalCash.setEnabled(false); }
				if(null != chkAddOnBenefitsPatientCare) {
				chkAddOnBenefitsPatientCare.setEnabled(false); }
				if(null != chkOtherBenefits) {
				chkOtherBenefits.setEnabled(false); }
				if(null != chkHospitalCash) {
				chkHospitalCash.setEnabled(true); 
				cmbReconsiderationRequest.setEnabled(false);
				}
			
		}
		
		return billClassificationLayout;
	}
	
	private void invokeBillClassificationListner()
	{
		if(null != this.bean.getDocumentDetails().getHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkhospitalization)
			chkhospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPreHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
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
		}
		
		if(null != this.bean.getDocumentDetails().getOtherBenefits())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getOtherBenefits();
			//unbindField(optPaymentMode);
			if(null != chkOtherBenefits)
				chkOtherBenefits.setValue(val);
		}
		
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
		
		if(null != this.bean.getDocumentDetails().getHospitalizationRepeat())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalizationRepeat();
			if(null != chkHospitalizationRepeat)
			{
				if(val)
				{
					chkHospitalizationRepeat.setValue(null);
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
		}
		if(null != this.bean.getDocumentDetails().getLumpSumAmount())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getLumpSumAmount();
			//unbindField(optPaymentMode);
			if(null != chkLumpSumAmount)
			{
				chkLumpSumAmount.setValue(!val);
				chkLumpSumAmount.setValue(val);
			}
		}
		
		//added for new product
		if(null != this.bean.getDocumentDetails().getHospitalCash())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalCash();
			//unbindField(optPaymentMode);
			if(null != chkHospitalCash)
				chkHospitalCash.setValue(val);
		}
		
	}
	
	
	
	private void displayAmountClaimedDetails()
	{
		if(null != this.bean.getDocumentDetails())
		{
			if(null != this.bean.getDocumentDetails().getHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationClaimedAmount()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
				txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
			}

			else if ((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) ||
					
					(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
					
					 (null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
			}
			if(null != this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount()))
					
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
				txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
			}
			else if (null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
			}
			if(null != this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount()))
					
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
				txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
			}
			else if (null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
				//txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
			
			if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
			 {
				 chkOtherBenefits.setEnabled(true);
				 txtOtherBenefitsClaimedAmnt.setValue(this.bean.getDocumentDetails().getOtherBenefitclaimedAmount());
			 }
			
			//added for new product
			if(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())
			 {
				 chkHospitalCash.setEnabled(true);
				 txtHospitalCashClaimedAmnt.setValue(this.bean.getDocumentDetails().getHospitalCashClaimedAmnt());
			 }
		}
	}
	
	private void buildOtherBenefitsLayout(Boolean value)
	{
		if(value)
		{
			otherBenefitsLayout.removeAllComponents();
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evacuation", "emergencyMedicalEvaluation", CheckBox.class);
			
			chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
			
			chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
			
			chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
			}
			
			chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
			
			FormLayout otherBenefitsLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
			FormLayout otherBenefitsLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
			FormLayout otherBenefitsLayout3 = new FormLayout(chkRepatriationOfMortalRemains);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkSharedAccomodation.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
			}
			
			HorizontalLayout otherBenefitsLayput = new HorizontalLayout();
			otherBenefitsLayput.addComponents(otherBenefitsLayout1,otherBenefitsLayout2,otherBenefitsLayout3);
			
			if(null != this.bean.getClaimDTO().getClaimType() && (SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				if( null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docReceivedFrom = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != docReceivedFrom && null != docReceivedFrom.getId() && (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(docReceivedFrom.getId()))
					{
						otherBenefitsLayput.removeAllComponents();
						otherBenefitsLayput.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
					}	
				}else if(this.bean.getDocumentDetails().getDocumentReceivedFromValue() != null && this.bean.getDocumentDetails().getDocumentReceivedFromValue().equalsIgnoreCase("Hospital")){
					otherBenefitsLayput.removeAllComponents();
					otherBenefitsLayput.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
				}
			}
			otherBenefitsLayput.setSpacing(true);
			otherBenefitsLayput.setMargin(true);
			
			if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())*/
						(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkCompassionateTravel.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
				chkPreferredNetworkHospital.setVisible(false);
			}
			else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				chkCompassionateTravel.setVisible(false);
			}
			
			otherBenefitsLayout.addComponent(otherBenefitsLayput);
		}else {
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			otherBenefitsLayout.removeAllComponents();
		}
		
		//return otherBenefitsLayput;
		
	}
	
	private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		paymentModeListener();

		buildBankTransferLayout();

		//unbindField(optPaymentMode);
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
		
		txtPaymentPartyMode = new TextField();
		if(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
//			txtPaymentPartyMode = (TextField) binder.buildAndBind("Payment Party Mode", "paymentPartyMode" , TextField.class);
			txtPaymentPartyMode.setCaption("Payment Party Mode");
			txtPaymentPartyMode.setEnabled(true);
			txtPaymentPartyMode.setVisible(true);
			if(bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null){
				txtPaymentPartyMode.setReadOnly(false);
				txtPaymentPartyMode.setValue(bean.getPreauthDTO().getPolicyDto().getPaymentParty());
				txtPaymentPartyMode.setReadOnly(true);
			}
		}else {
			txtPaymentPartyMode.setEnabled(false);
			txtPaymentPartyMode.setVisible(false);
		}
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
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())
				&& (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())){
		
			btnAccPrefSearch.setEnabled(false);
		}
		else {
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(true);
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())) {
			
			btnIFCSSearch.setEnabled(false);
		}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	
	private List<Field<?>> getListOfOtherBenefitsChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkEmergencyMedicalEvaluation);
		fieldList.add(chkCompassionateTravel);
		fieldList.add(chkRepatriationOfMortalRemains);
		fieldList.add(chkPreferredNetworkHospital);
		fieldList.add(chkSharedAccomodation);
		return fieldList;
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
		if(bean.getPreauthDTO().getPolicyDto() != null && 
				bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()){
			
			if(bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null){
				txtEmailId.setValue(bean.getDocumentDetails().getEmailId());
			}
		}
		
		//txtEmailId.setRequired(true);
		
		txtPayModeChangeReason = (TextField) binder.buildAndBind("Reason for change (Pay Mode)", "payModeChangeReason" , TextField.class);
		CSValidator payModeChangeValidator = new CSValidator();
		payModeChangeValidator.extend(txtPayModeChangeReason);
		payModeChangeValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		payModeChangeValidator.setPreventInvalidTyping(true);
		txtPayModeChangeReason.setMaxLength(4000);		
		payModeShortcutListener(txtPayModeChangeReason, null);
		paymentModeValuechangeListener();
		
		//Commented as discussed with Satish Sir - 04-MAR-2021 based on impact of GLX2020150
		/*if(bean.getPreauthDTO().getPolicyDto() != null && 
				bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()){
			txtPayModeChangeReason.setEnabled(false);
		}*/
				
		
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
		
		txtLegalHeirName = new TextField("Legal Heir Name");
//		txtLegalHeirName = (TextField) binder.buildAndBind("Legal Heir Name","legalFirstName",TextField.class);
		//txtLegalHeirName.setValue(this.chqLegalHeirName);
		txtLegalHeirName.setEnabled(false);
		txtLegalHeirName.setMaxLength(30);
		
		/*txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);
		*/
		
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		//txtPayableAt.setValue(this.chqPayableAt);
	//	mandatoryFields.add(txtPayableAt);
		//txtPayableAt.setMaxLength(50);
/*		txtPayableAt.setRequired(true);
*/		/*
		setRequiredAndValidation(txtPayableAt);
		txtPayableAt.setRequired(true);
		showOrHideValidation(false);*/
		
		/*CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z /]*$");
		payableAtValidator.setPreventInvalidTyping(true);;*/
		
		
		txtPayableAt.setEnabled(false);
		
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
			
			/*txtLegalHeirName.setReadOnly(true);
			txtLegalHeirName.setEnabled(false);*/
			
/*			txtLegalHeirMiddleName.setReadOnly(true);
			txtLegalHeirMiddleName.setEnabled(false);
			
			txtLegalHeirLastName.setReadOnly(true);
			txtLegalHeirLastName.setEnabled(false);*/
			
			if(null != bean.getDocumentDetails().getAccountNo() && !("").equals(bean.getDocumentDetails().getPanNo()))
			{
				txtPayableAt.setReadOnly(true);
				//txtPayableAt.setEnabled(false);
				btnCitySearch.setEnabled(false);
			}
			else
			{
				txtPayableAt.setReadOnly(false);
				//txtPayableAt.setEnabled(true);
				btnCitySearch.setEnabled(true);
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
			 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
				 if(txtAccountPref != null){	
					 unbindField(txtAccountPref);
				 }	 
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
			txtPayableAt.setValue(null);
			txtReasonForChange.setValue(null);
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
		
		TextField cField1 = new TextField();
		cField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField1.setReadOnly(true);
		TextField cField2 = new TextField();
		cField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField2.setReadOnly(true);
		cField2.setWidth("5px");
		TextField cField3 = new TextField();
		cField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField3.setReadOnly(true);
		cField3.setWidth("5px");
		TextField cField4 = new TextField();
		cField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField4.setReadOnly(true);
		cField4.setWidth("5px");
		TextField cField5 = new TextField();
		cField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField5.setReadOnly(true);
		cField5.setWidth("5px");
		
		HorizontalLayout btnHLayoutT = new HorizontalLayout(txtPayableAt,btnCitySearch);
		txtPayableAt.setCaption(null);
		btnHLayoutT.setCaption("Payable At");

		if(null != this.bean.getDocumentDetails().getPaymentModeFlag() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) 
				&& (ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		
		//if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		{
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,txtPanNo,btnHLayoutT);
			formLayout1.setMargin(false);
//			FormLayout btnLayout = new FormLayout(cField1,cField2,cField3,cField4,btnHLayout);
			formLayout2 = new FormLayout(cmbPayeeName,txtPaymentPartyMode,txtReasonForChange,txtLegalHeirName);
			formLayout2.setMargin(false);
			hLayout= new HorizontalLayout(formLayout1 /*,btnLayout*/, formLayout2);
			hLayout.setMargin(true);
		}
		else if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && 
				//(null != this.bean.getDocumentDetails().getPaymentMode() && this.bean.getDocumentDetails().getPaymentMode())
				(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())
				)
		{
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
			
			formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtPaymentPartyMode,txtReasonForChange,txtNameAsPerBank,btnHLayoutT,txtPanNo,txtEmailId);
			formLayout1.setMargin(false);
			
//			FormLayout btnLayout = new FormLayout(cField1,cField2,cField3,cField4,btnHLayout);
			
			 formLayout2 = new FormLayout(txtPayModeChangeReason,txtRelationship);
			 
			 if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
					 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
				 if(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()) {
					 btnAccPrefSearch.setEnabled(false);
				 }
				 else {
					 if(btnAccPrefSearch != null)
						 btnAccPrefSearch.setEnabled(true);
				 }
				accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
				accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
				formLayout2.addComponent(accPrefLayout);
			 }
			 
			 formLayout2.addComponents(txtAccType,txtLegalHeirName);
			 formLayout2.setMargin(false);
			 hLayout = new HorizontalLayout(formLayout1 /*,btnLayout*/, formLayout2);
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
			/*TextField dField5 = new TextField();
			dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField5.setReadOnly(true);
			dField5.setWidth(2,Unit.CM);*/
			
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
			
			formLayout1 = new FormLayout(optPaymentMode,cmbPayeeName,txtPaymentPartyMode,txtReasonForChange,txtNameAsPerBank,txtAccntNo,txtBankName,txtCity,txtEmailId);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(txtPayModeChangeReason,txtRelationship);
			
			if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
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
			//btnHLayout.setWidth("5%");
//			VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
//			btnLayout.setWidth("%");
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
		
		if(bean.getPreauthDTO().getPolicyDto() != null && 
				bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()){
		
			if(bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null){
				txtEmailId.setValue(bean.getDocumentDetails().getEmailId());
			}
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())
					&& (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())){
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(false);
		}
		else {
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(true);
		}
	
		if(null != this.bean.getDocumentDetails().getPayeeName()&& this.bean.getDocumentDetails().getPayeeName().getValue()!=null
				&& payeeNameList != null && !payeeNameList.getItemIds().isEmpty())
		{
			for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if (null != this.bean.getDocumentDetails().getPayeeName() && null != this.bean.getDocumentDetails().getPayeeName().getValue() &&
						this.bean.getDocumentDetails().getPayeeName().getValue().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
				{
					//IMSSUPPOR-30552
					if(this.cmbPayeeName.isReadOnly()){
						this.cmbPayeeName.setReadOnly(false);
						this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
						this.cmbPayeeName.setReadOnly(true);
					}else{
						this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
					}
				}
		 	}
		}
		
		if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
				("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			if(txtRelationship != null)
				txtRelationship.setValue(this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName() != null ? this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName().getRelationshipWithProposer() : "");
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())) {
			txtAccntNo.setEnabled(false);
		} 
	 	
		return hLayout;
		
		
	}
	
	private void addComboPayeeNameListener()
	{
		cmbPayeeName
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
				if(null == value)
				{	
						/*txtLegalHeirName.setEnabled(true);*/
						txtReasonForChange.setEnabled(true);
				}
				else if((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(value.getValue()))
				{
					txtReasonForChange.setEnabled(false);
				}
				
				if(null != value) {
					
					txtRelationship.setValue(value.getRelationshipWithProposer());
					
					txtNameAsPerBank.setValue(value.getNameAsPerBankAccount());
				}
				
				/*else
				{
					//IMSSUPPOR-27410
					Boolean isLegalReadOnly = Boolean.FALSE;
					if(txtLegalHeirName.isReadOnly()){
						isLegalReadOnly = Boolean.TRUE;
						txtLegalHeirName.setReadOnly(false);
					}
					
					txtLegalHeirName.setValue(null);
					txtLegalHeirName.setNullRepresentation("");
					
					if(isLegalReadOnly){
						txtLegalHeirName.setReadOnly(true);
					}
					
					txtLegalHeirName.setEnabled(true);
					if(bean.getPreauthDTO().getPolicyDto() != null && 
							bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()
							&& bean.getPreauthDTO().getPolicyDto().getPaymentParty().equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_EMPLOYEE)){
						txtReasonForChange.setEnabled(false);
					} else {
					txtReasonForChange.setEnabled(true);
					}
				}*/
			}
				
			
		});
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
//		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
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
				if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
	 					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
					txtAccntNo.setEnabled(false);
				}else {						
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
			
//			txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
//			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
//			txtCity.setReadOnly(true);
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
		lyutIFCS.setCaption("IFSC Code1");
		txtIfscCode.setCaption(null);
		
		/*TextField dField1 = new TextField();
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
		dField4.setWidth("5px");*/
		
		TextField dField5 = new TextField();
		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField5.setReadOnly(true);
		dField5.setWidth(2,Unit.CM);
		
		
		//btnHLayout.setWidth("5%");
		FormLayout btnLayout = new FormLayout(dField5, btnIFCSSearch);
//		btnLayout.setWidth("%");
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,/*lyutIFCS,*/txtBankName,txtCity);
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
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 /*, btnLayout*/ , bankTransferLayout2);
		hLayout.setSpacing(true);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
	//	hLayout.setWidth("80%");
		hLayout.setMargin(false);
//		hLayout.setWidth("50%");
		hLayout.addStyleName("gridBorder");
		/*if(null != txtAccntNo)
		{
//			mandatoryFields.add(txtAccntNo);
//			setRequiredAndValidation(txtAccntNo);
		}
		if(null != txtIfscCode)
		{
			mandatoryFields.add(txtIfscCode);
			setRequiredAndValidation(txtIfscCode);
			txtIfscCode.setValidationVisible(false);
			
		}*/
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())
					&& (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())){
			
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(false);
		}
		else {
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(true);
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())) {
			if(txtAccntNo != null)
				txtAccntNo.setEnabled(false);
			
			if(txtPanNo != null)
			txtPanNo.setEnabled(false);
			
			if(btnAccPrefSearch != null)
				btnAccPrefSearch.setEnabled(false);
		}
		
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
		
		rodQueryDetails.setEnabled(true);
		
		queryDetailsLayout = new HorizontalLayout(rodQueryDetails);
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
		reconsiderRequestDetails.initPresenter(SHAConstants.CREATE_ROD);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setCaption("Select Earlier Request to be Reconsidered");
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
				
//				if(reconsiderList.getDocumentReceivedFrom() == null){
//					reconsiderRequestDetails.addBeanToList(reconsiderList);
//				}
				
				if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
					SelectValue selected = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					if(reconsiderList.getDocumentReceivedFrom() != null && selected.getValue().equalsIgnoreCase(reconsiderList.getDocumentReceivedFrom()))
					reconsiderRequestDetails.addBeanToList(reconsiderList);
				}else if(this.bean.getDocumentDetails().getDocumentReceivedFromValue() != null){
					if(reconsiderList.getDocumentReceivedFrom() != null && this.bean.getDocumentDetails().getDocumentReceivedFromValue().equalsIgnoreCase(reconsiderList.getDocumentReceivedFrom())){
						reconsiderRequestDetails.addBeanToList(reconsiderList);
					}
				}
				//reconsiderList.setSelect(null);
			}
		}
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			reconsiderRequestDetails.setEnabled(false);
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
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN) 
				&& !(this.bean.getDocumentDetails().getPaymentCancellationNeeded())){
			optPaymentCancellation.setEnabled(false);
		}
		if(this.bean.getDocumentDetails().getIsEditable() != null && this.bean.getDocumentDetails().getIsEditable()){
			optPaymentCancellation.setEnabled(true);
		}
		
	//	HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(reconsiderRequestDetails);
		VerticalLayout reconsiderRequestLayout = new VerticalLayout(cmbReasonForReconsideration, reconsiderRequestDetails,new FormLayout(optPaymentCancellation));
//		reconsiderRequestLayout.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		reconsiderRequestLayout.setWidth("100%");
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
					message = "Are you sure that payment cancellation is not "
							+ ""
							+ ""
							+ " and the payment has been accepted";
					if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
						bean.getDocumentDetails().setPaymentCancellationNeeded(false);
					}
				}
				//Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
				
//				Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
				
				/*Button homeButton = new Button("Ok");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
				horizontalLayout.setSpacing(true);
				horizontalLayout.setMargin(true);*/
				
				/*VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");*/
				
				
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox(message+"</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
						.toString());
				
				
				
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
			}

		});
	}
	
	private void addListener()
	{
		cmbPatientStatus.addValueChangeListener(new Property.ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8435623803385270083L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					generateFieldsBasedOnPatientStatus();
				}	
			}
		});
		
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.CREATE_ROD);
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
						reconsiderationLayout.addComponent(buildBillClassificationLayout());
						reconsiderationLayout.addComponent(otherBenefitsLayout);
						reconsiderationLayout.addComponent(buildQueryDetailsLayout());
						
						if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkhospitalization.setEnabled(true);
						}
						else if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkPartialHospitalization.setEnabled(false);
						}
						else
						{
							chkPartialHospitalization.setEnabled(true);
						}
						if(isReconsider)
						{
							displayAmountClaimedDetails();
							buildBillClassificationDetailsBasedOnDocRecFrom(bean.getDocumentDetails().getDocumentReceivedFromValue());
							//uploadDocumentDTOList.removeAll(uploadDocumentDTOList);
							uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
						}
						
						bean.setReconsiderRODdto(null);
						bean.setUploadDocsList(null);
						
						bean.getDocumentDetails().setReconsiderationRequestValue("No");
						
						
						if(null != txtHospitalizationClaimedAmt)
						{
							if(bean.getDocumentDetails().getHospitalizationClaimedAmount() != null && !("").equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationClaimedAmount()))
							{
							txtHospitalizationClaimedAmt.setValue(bean.getDocumentDetails().getHospitalizationClaimedAmount());
							txtHospitalizationClaimedAmt.setEnabled(true);
							}
						}
						
						if(null != txtOtherBenefitsClaimedAmnt)
						{
							if(bean.getDocumentDetails().getOtherBenefitclaimedAmount() != null && !("").equalsIgnoreCase(bean.getDocumentDetails().getOtherBenefitclaimedAmount()))
							{
								txtOtherBenefitsClaimedAmnt.setValue(bean.getDocumentDetails().getOtherBenefitclaimedAmount());
								txtOtherBenefitsClaimedAmnt.setEnabled(true);
							}
						}
						
						//added for new product
						if(null != txtHospitalCashClaimedAmnt)
						{
							if(bean.getDocumentDetails().getHospitalCashClaimedAmnt() != null && !("").equalsIgnoreCase(bean.getDocumentDetails().getHospitalCashClaimedAmnt()))
							{
								txtHospitalCashClaimedAmnt.setValue(bean.getDocumentDetails().getHospitalCashClaimedAmnt());
								txtHospitalCashClaimedAmnt.setEnabled(true);
							}
						}
						
						/*if(null != txtPreHospitalizationClaimedAmt)
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
						
						sectionDetailsListenerTableObj.enableDisable(true);
						if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())){
							sectionDetailsListenerTableObj.enableDisable(false);
						}
						wizard.getNextButton().setEnabled(true);
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
						if(validateMisApproval()){
							alertMessageForRejectionReconsider();
							
						} else {
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
						if(null != txtOtherBenefitsClaimedAmnt)
						{

							txtOtherBenefitsClaimedAmnt.setValue(null);
							txtOtherBenefitsClaimedAmnt.setEnabled(false);
						}
						
						if(null != txtHospitalCashClaimedAmnt)
						{

							txtHospitalCashClaimedAmnt.setValue(null);
							txtHospitalCashClaimedAmnt.setEnabled(false);
						}
						reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
						sectionDetailsListenerTableObj.enableDisable(false);
					}
					}
				}
				
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
					if(null != chkhospitalization) {
						chkhospitalization.setEnabled(false); }
						if(null != chkHospitalizationRepeat) {
						chkHospitalizationRepeat.setEnabled(false); }
						if(null != chkPreHospitalization) {
						chkPreHospitalization.setEnabled(false); }
						if(null != chkLumpSumAmount) {
						chkLumpSumAmount.setEnabled(false); }
						if(null != chkPostHospitalization) {
						chkPostHospitalization.setEnabled(false); }
						if(null != chkPartialHospitalization) {
						chkPartialHospitalization.setEnabled(false); }
						if(null != chkAddOnBenefitsHospitalCash) {
						chkAddOnBenefitsHospitalCash.setEnabled(false); }
						if(null != chkAddOnBenefitsPatientCare) {
						chkAddOnBenefitsPatientCare.setEnabled(false); }
						if(null != chkOtherBenefits) {
						chkOtherBenefits.setEnabled(false); }
						if(null != chkHospitalCash) {
						chkHospitalCash.setEnabled(true); 
						cmbReconsiderationRequest.setEnabled(false);
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
						txtAcknowledgementContactNo.setEnabled(true);
						txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);*/
						//txtEmailId.setRequired(true);
						
						if(txtEmailId != null) {
							txtEmailId.setReadOnly(false);
							txtEmailId.setEnabled(true);
						}
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							if(null != chkPartialHospitalization)
							chkPartialHospitalization.setEnabled(true);
							
						/*	chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);*/
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
							
							/*chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);*/
							
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
							
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
							{
								if(null != chkOtherBenefits){
									chkOtherBenefits.setEnabled(true);
								}
							}
							
							//GLX2020017
							if(null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
								||	null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
								||	null != bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
								||	null != bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
							{
								if(chkOtherBenefits != null){
									chkOtherBenefits.setEnabled(false);
								}
								if(chkAddOnBenefitsHospitalCash !=null){
									chkAddOnBenefitsHospitalCash.setEnabled(false);
								}
							}
							//chkPartialHospitalization.setValue(false);
							
						}
					}
					/*if(("Insured").equalsIgnoreCase(value.getValue()))
					{	
						
						txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);
						txtEmailId.setRequired(true);
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							chkPartialHospitalization.setEnabled(true);
							
							chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);
							//chkPartialHospitalization.setValue(false);
							
						}
						else
						{
							chkhospitalization.setEnabled(true);
						}
						
						
						
						
						setRequiredAndValidation(txtEmailId);
						
						mandatoryFields.add(txtEmailId);
					}*/
					else
					{
						if(nomineeDetailsTable != null) { 
							//documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
							nomineeLegalLayout.removeComponent(nomineeDetailsTable);
						}
						if(legalHeirDetails != null) {
							//documentDetailsPageLayout.removeComponent(legalHeirLayout);
							nomineeLegalLayout.removeComponent(legalHeirDetails);
						}
						
						/*txtEmailId.setReadOnly(false);
						//txtEmailId.setEnabled();
						txtEmailId.setValue(null);
						txtEmailId.setReadOnly(true);
						txtEmailId.setEnabled(false);
						txtEmailId.setRequired(false);*/
						/*if(null != chkPartialHospitalization && null != chkhospitalization && null != chkPreHospitalization && null != chkPostHospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkPartialHospitalization.setEnabled(false);
							if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
							chkPartialHospitalization.setValue(false);
							
							chkhospitalization.setEnabled(true);
							
							chkPreHospitalization.setEnabled(false);
							chkPostHospitalization.setEnabled(false);
						}*/
						if(null != chkhospitalization)
							chkhospitalization.setEnabled(true);
						if(null != chkHospitalizationRepeat)
					//	chkHospitalizationRepeat.setEnabled(true);
							chkHospitalizationRepeat.setEnabled(false);
						if(null != chkPartialHospitalization)
						{

							chkPartialHospitalization.setEnabled(false);
							chkPartialHospitalization.setValue(null);
						}
						if(null != chkPreHospitalization)
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
						}
						
						if(null != chkOtherBenefits)
						{
							chkOtherBenefits.setEnabled(false);
							chkOtherBenefits.setValue(null);
							//chkOtherBenefits.setValue(null);
						}
						
						if(null != bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
							if(chkOtherBenefits != null){
								chkOtherBenefits.setValue(null);
								chkOtherBenefits.setEnabled(false);
							}
						}
						
						//mandatoryFields.remove(txtEmailId);
					}
					loadQueryDetailsTableValues();
				}
				
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
					if(null != chkhospitalization) {
						chkhospitalization.setEnabled(false); }
						if(null != chkHospitalizationRepeat) {
						chkHospitalizationRepeat.setEnabled(false); }
						if(null != chkPreHospitalization) {
						chkPreHospitalization.setEnabled(false); }
						if(null != chkLumpSumAmount) {
						chkLumpSumAmount.setEnabled(false); }
						if(null != chkPostHospitalization) {
						chkPostHospitalization.setEnabled(false); }
						if(null != chkPartialHospitalization) {
						chkPartialHospitalization.setEnabled(false); }
						if(null != chkAddOnBenefitsHospitalCash) {
						chkAddOnBenefitsHospitalCash.setEnabled(false); }
						if(null != chkAddOnBenefitsPatientCare) {
						chkAddOnBenefitsPatientCare.setEnabled(false); }
						if(null != chkOtherBenefits) {
						chkOtherBenefits.setEnabled(false); }
						if(null != chkHospitalCash) {
						chkHospitalCash.setEnabled(true); 
						cmbReconsiderationRequest.setEnabled(false);
						}
					
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
				fireViewEvent(CreateRODDocumentDetailsPresenter.HOSPITAL_DETAILS, bean);
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
						/*HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date cannot be greater than current system date."));
						layout.setMargin(true);*/
						/*final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);*/
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createAlertBox("Document Received Date cannot be greater than current system date.", buttonsNamewithType);
						
					}
					else if(getDifferenceBetweenDates(value) > 7)
					{

						/*documentsReceivedDate.setReadOnly(false);
						documentsReceivedDate.setEnabled(true);*/

						documentsReceivedDate.setEnabled(true);
						documentsReceivedDate.setReadOnly(false);

						documentsReceivedDate.setValue(null);
						
						/*HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date can only be 7 days prior to current system date. But entered date is "+value.toString()+". Please renter a valid date"));*/
						/*layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);*/
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createAlertBox("Document Received Date can only be 7 days prior to current system date. But entered date is "+value.toString()+". Please Re-enter a valid date", buttonsNamewithType);
						
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
	
	this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
		
		private static final long serialVersionUID = -7831804284490287934L;

		@Override
		public void valueChange(ValueChangeEvent event) {TextField property = (TextField) event.getProperty();
		String value = property.getValue();
		subCoverBasedBillClassificationManipulation(value,bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
		}
	});	
	
	
	btnCitySearch.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			Window popup = new com.vaadin.ui.Window();
			citySearchCriteriaWindow.setPresenterString(SHAConstants.CREATE_ROD);
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
				bankDetailsTableObj.initPresenter(SHAConstants.CREATE_ROD);
				bankDetailsTableObj.init(bean);
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
				if((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
						|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
					
						policyFrmDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
						policyToDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());;
						
						if(bean.getNewIntimationDTO().getPolicy().getSectionCode() != null && bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getNewIntimationDTO().getGmcMainMember() != null){
							policyFrmDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
							policyToDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());		
						}
				}
				
				if (null != dateOfAdmissionValue && null != policyFrmDate 
						&& null != policyToDate) {
					//if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
					if (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFrmDate,policyToDate,dateOfAdmissionValue)) {
						dateOfAdmission.setValue(null);
						
						 /*Label label = new Label("Admission Date is not in range between Policy From Date and Policy To Date.", ContentMode.HTML);
							label.setStyleName("errMessage");*/
						 /*HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
							
						
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
									|| (null !=bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getHospitalCashFlag()))
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
							/*Label label = new Label("Discharge date is before than admission date. Please enter valid discharge date", ContentMode.HTML);
							label.setStyleName("errMessage");
							VerticalLayout layout = new VerticalLayout();
							layout.setMargin(true);
							layout.addComponent(label);*/

							/*ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(true);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createAlertBox("Discharge date is before than admission date. Please enter valid discharge date", buttonsNamewithType);
							
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
		
		if((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
				|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){
			
				policyFrmDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
				policyToDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());
				
				if(bean.getNewIntimationDTO().getPolicy().getSectionCode() != null && bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getNewIntimationDTO().getGmcMainMember() != null){
					policyFrmDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
					policyToDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());		
				}
		}
		if (null != dateOfAdmissionValue && null != policyFrmDate 
				&& null != policyToDate) {
			//if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
			if (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFrmDate,policyToDate,dateOfAdmissionValue)) {
				dateOfAdmission.setValue(null);
				
				 /*Label label = new Label("Admission Date is not in range between Policy From Date and Policy To Date.", ContentMode.HTML);
					label.setStyleName("errMessage");
				 HorizontalLayout layout = new HorizontalLayout(
						 label);
					layout.setMargin(true);*/
					/*final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					//dialog.setWidth("35%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
					
				
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
							|| (null != bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(bean.getDocumentDetails().getHospitalCashFlag()))
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
	
	
	private void buildBillClassificationDetailsBasedOnDocRecFrom(String strDocReceivedFrom)
	{
		
		if(("Insured").equalsIgnoreCase(strDocReceivedFrom))
		{	
			/*txtAcknowledgementContactNo.setRequired(true);
			txtAcknowledgementContactNo.setReadOnly(false);
			txtAcknowledgementContactNo.setEnabled(true);*/
			txtEmailId.setReadOnly(false);
			txtEmailId.setEnabled(true);
			//txtEmailId.setRequired(true);
			
			if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
			{
				chkhospitalization.setEnabled(false);
				if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
				chkhospitalization.setValue(false);
				
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setEnabled(true);
					chkPartialHospitalization.setValue(null);
				}
				
				
			/*	chkPreHospitalization.setEnabled(true);
				chkPostHospitalization.setEnabled(true);*/
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
				{
					//chkPostHospitalization.setEnabled(false);
					
					/**
					 * Production Enhancement.
					 * If doc rec from is insured, then by default
					 * pre and post hospitalization will be checked 
					 * and will be disabled.
					 * */
					chkPreHospitalization.setEnabled(false);
					chkPreHospitalization.setValue(true);
					
					//chkPreHospitalization.setEnabled(true);
					//chkPreHospitalization.setValue(null);
				}
				//chkPreHospitalization.setEnabled(true);
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					//chkPostHospitalization.setEnabled(true);
					/**
					 * Production Enhancement.
					 * If doc rec from is insured, then by default
					 * pre and post hospitalization will be checked 
					 * and will be disabled.
					 * */
					chkPostHospitalization.setEnabled(false);
					chkPostHospitalization.setValue(true);
					//chkPostHospitalization.setValue(null);
				}
				if(null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.isEnabled())
				{
					chkLumpSumAmount.setEnabled(true);
					chkLumpSumAmount.setValue(null);
				}
				if(null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.isEnabled())
				{
					chkAddOnBenefitsHospitalCash.setEnabled(true);
					chkAddOnBenefitsHospitalCash.setValue(null);
				}
				if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.isEnabled())
				{
					chkAddOnBenefitsPatientCare.setEnabled(true);
					chkAddOnBenefitsPatientCare.setValue(null);
				}
				//chkPartialHospitalization.setValue(false);
				
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
				
				/*chkPreHospitalization.setEnabled(true);
				chkPostHospitalization.setEnabled(true);*/
				
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
				{
					//chkPostHospitalization.setEnabled(false);
					
					chkPreHospitalization.setEnabled(false);
					chkPreHospitalization.setValue(true);
					
					/*chkPreHospitalization.setEnabled(true);
					chkPreHospitalization.setValue(false);*/
				}
				//chkPreHospitalization.setEnabled(true);
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					chkPostHospitalization.setEnabled(false);
					chkPostHospitalization.setValue(true);
					/*chkPostHospitalization.setEnabled(true);
					chkPostHospitalization.setValue(false)*/;
				}
				
				//chkPartialHospitalization.setValue(false);
				
			}
			
			/*if(null != chkOtherBenefits){
				if(null != chkEmergencyMedicalEvaluation)
				chkEmergencyMedicalEvaluation.setEnabled(true);
				if(null != chkCompassionateTravel)
				chkCompassionateTravel.setEnabled(true);
				if(null != chkRepatriationOfMortalRemains)
				chkRepatriationOfMortalRemains.setEnabled(true);
				if(null != chkPreferredNetworkHospital)
				chkPreferredNetworkHospital.setEnabled(true);
				if(null != chkSharedAccomodation)
				chkSharedAccomodation.setEnabled(true);
			}*/
			
			if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
			{
				chkLumpSumAmount.setEnabled(false);
			}
			if(null != sectionDetailsListenerTableObj)
			{
				subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
			}
			
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
				chkPreHospitalization.setEnabled(true);
			}
			
		}
		/*if(("Insured").equalsIgnoreCase(value.getValue()))
		{	
			
			txtEmailId.setReadOnly(false);
			txtEmailId.setEnabled(true);
			txtEmailId.setRequired(true);
			
			if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkPartialHospitalization)
			{
				chkhospitalization.setEnabled(false);
				if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
				chkhospitalization.setValue(false);
				
				chkPartialHospitalization.setEnabled(true);
				
				chkPreHospitalization.setEnabled(true);
				chkPostHospitalization.setEnabled(true);
				//chkPartialHospitalization.setValue(false);
				
			}
			else
			{
				chkhospitalization.setEnabled(true);
			}
			
			
			
			
			setRequiredAndValidation(txtEmailId);
			
			mandatoryFields.add(txtEmailId);
		}*/
		else
		{
			
			
			/*txtEmailId.setReadOnly(false);
			//txtEmailId.setEnabled();
			txtEmailId.setValue(null);
			txtEmailId.setReadOnly(true);
			txtEmailId.setEnabled(false);
			txtEmailId.setRequired(false);*/
			/*if(null != chkPartialHospitalization && null != chkhospitalization && null != chkPreHospitalization && null != chkPostHospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
			{
				chkPartialHospitalization.setEnabled(false);
				if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
				chkPartialHospitalization.setValue(false);
				
				chkhospitalization.setEnabled(true);
				
				chkPreHospitalization.setEnabled(false);
				chkPostHospitalization.setEnabled(false);
			}*/
			if(null != chkhospitalization)
				chkhospitalization.setEnabled(true);
			if(null != chkHospitalizationRepeat)
		//	chkHospitalizationRepeat.setEnabled(true);
				chkHospitalizationRepeat.setEnabled(false);
			if(null != chkPartialHospitalization)
			{

				chkPartialHospitalization.setEnabled(false);
				chkPartialHospitalization.setValue(null);
			}
			if(null != chkPreHospitalization)
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
			}
			/*Below commented bcoz below code only for doc received from Insured only
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
				chkPreHospitalization.setEnabled(true);
			}*/

			/*if(null != chkOtherBenefits){
				if(null != chkEmergencyMedicalEvaluation)
				chkEmergencyMedicalEvaluation.setEnabled(true);
				if(null != chkCompassionateTravel)
				chkCompassionateTravel.setEnabled(false);
				if(null != chkRepatriationOfMortalRemains)
				chkRepatriationOfMortalRemains.setEnabled(true);
				if(null != chkPreferredNetworkHospital)
				chkPreferredNetworkHospital.setEnabled(false);
				if(null != chkSharedAccomodation)
				chkSharedAccomodation.setEnabled(false);
			}*/
			
			
			//mandatoryFields.remove(txtEmailId);
		}
		
		/*SelectValue selValue = new SelectValue(ReferenceTable.COMMONMASTER_NO, "No");
		if(cmbReconsiderationRequest != null && cmbReconsiderationRequest.getValue() != null){
			SelectValue selectValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(selectValue.getId().equals(ReferenceTable.COMMONMASTER_YES)){
				cmbReconsiderationRequest.setValue(selValue);
			}
		}*/
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			if(null != chkhospitalization) {
				chkhospitalization.setEnabled(false); }
				if(null != chkHospitalizationRepeat) {
				chkHospitalizationRepeat.setEnabled(false); }
				if(null != chkPreHospitalization) {
				chkPreHospitalization.setEnabled(false); }
				if(null != chkLumpSumAmount) {
				chkLumpSumAmount.setEnabled(false); }
				if(null != chkPostHospitalization) {
				chkPostHospitalization.setEnabled(false); }
				if(null != chkPartialHospitalization) {
				chkPartialHospitalization.setEnabled(false); }
				if(null != chkAddOnBenefitsHospitalCash) {
				chkAddOnBenefitsHospitalCash.setEnabled(false); }
				if(null != chkAddOnBenefitsPatientCare) {
				chkAddOnBenefitsPatientCare.setEnabled(false); }
				if(null != chkOtherBenefits) {
				chkOtherBenefits.setEnabled(false); }
				if(null != chkHospitalCash) {
				chkHospitalCash.setEnabled(true); 
				cmbReconsiderationRequest.setEnabled(false);
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
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					bean.getDocumentDetails().setPaymentModeChangeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					/*if(null != txtPayableAt)
					{
//						mandatoryFields.add(txtPayableAt);
//						setRequiredAndValidation(txtPayableAt);
//						txtPayableAt.setRequired(true);
//						showOrHideValidation(false);
						txtPayableAt.setValidationVisible(false);
					}
					if(null != txtAccntNo)
						{
//							mandatoryFields.remove(txtAccntNo);
						}
						if(null != txtIfscCode)
						{
//							mandatoryFields.remove(txtIfscCode);
						}*/
					
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
					bean.getDocumentDetails().setPaymentModeChangeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);

					unbindField(getListOfPaymentFields());
					HorizontalLayout bankLayout = buildBankTransferLayout();
					if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
							&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
		 					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
						txtAccntNo.setEnabled(false);
					}else {						
						txtAccntNo.setEnabled(true);
					}
					bankLayout.setMargin(false);
					//txtIfscCode.setEnabled(true);
					txtBankName.setEnabled(false);
					txtCity.setEnabled(false);
					btnIFCSSearch.setVisible(true);
					txtBranch.setEnabled(false);
					paymentDetailsLayout.removeAllComponents();					
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(bankLayout);
					paymentDetailsLayout.setMargin(false);
					unbindField(txtPayableAt);
					mandatoryFields.remove(txtPayableAt);
					
					
					if(bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()
							&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())){
							if(bean.getDocumentDetails().getIfscCode() != null && bean.getDocumentDetails().getBankName() !=null){
							
								if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
										&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					 					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
									txtAccntNo.setEnabled(false);
								}else {						
									txtAccntNo.setEnabled(true);
								}
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
					}
				}
				
				if(cmbReconsiderationRequest != null  || bean.getIsQueryReplyReceived()) {
					if((bean.getDocumentDetails().getReconsiderationRequestValue() != null && bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes"))
							|| bean.getIsQueryReplyReceived()) {
						
						if(null != bean.getDocumentDetails().getPaymentModeFlag() && !(bean.getDocumentDetails().getPaymentModeFlag().equals(bean.getDocumentDetails().getPaymentModeChangeFlag()))){
							
							mandatoryFields.add(txtPayModeChangeReason);
							txtPayModeChangeReason.setValue("");
							showOrHideValidation(false);
						}
						else
						{
							mandatoryFields.remove(txtPayModeChangeReason);
						}
					}
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
	
	
	
	/*private void addBillClassificationLister()

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
		
	}*/
	
	private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 if(!bean.getIsQueryReplyReceived()){
						 validateLumpSumClassification(SHAConstants.HOSPITALIZATION, chkhospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
								label.setStyleName("errMessage");*/
							 /*HorizontalLayout layout = new HorizontalLayout(
									 label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
								
								chkhospitalization.setValue(false);
								if(null != txtHospitalizationClaimedAmt)
								{
									txtHospitalizationClaimedAmt.setValue("");
								
								}
						 }
						 
						 else
						 {
							 txtHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
					 
				 }
				 else
				 {
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 if(validateBillClassification())
					 {
						 //Label label = new Label("Pre or Post hospitalization cannot exist without hospitalization", ContentMode.HTML);
						/* Label label = new Label("None of the bill classification can exist without hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
						 layout.setMargin(true);*/
							/*final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
						 
						 	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the bill classification can exist without hospitalization", buttonsNamewithType);
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(null);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(null);
							}
							if(null != chkLumpSumAmount && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag")))
							{
								if(null != sectionDetailsListenerTableObj)
								{
									String secVal = sectionDetailsListenerTableObj.getSubCoverFieldValue();
									if(ReferenceTable.LUMPSUM_SUB_COVER_CODE.equalsIgnoreCase(secVal))
									{
										chkLumpSumAmount.setEnabled(true);
										chkLumpSumAmount.setValue(null);
									}
									else
									{
										chkLumpSumAmount.setEnabled(false);
									}
								}
								else
								{
									chkLumpSumAmount.setEnabled(false);
								}
							}
							if(null != chkAddOnBenefitsHospitalCash && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
							{
								chkAddOnBenefitsHospitalCash.setValue(null);
							}
							else if(null != chkAddOnBenefitsHospitalCash)
							{
								chkAddOnBenefitsHospitalCash.setEnabled(false);
							}
							if(null != chkAddOnBenefitsPatientCare && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
							{
								chkAddOnBenefitsPatientCare.setValue(null);
							}
							else if(null != chkAddOnBenefitsPatientCare)
							{
								chkAddOnBenefitsPatientCare.setEnabled(false);
							}
							if(null != chkHospitalizationRepeat)
							{
								chkHospitalizationRepeat.setValue(null);
							}
							//chkhospitalization.setValue(false);
					 }
					 
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		
		
		
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.PREHOSPITALIZATION, chkPreHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
							
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPreHospitalization.setValue(false);
						 }
						 else
						 {
							 txtPreHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
					 
				 }
				 else
				 {
					
					 txtPreHospitalizationClaimedAmt.setEnabled(false);
					 txtPreHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 { 
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.POSTHOSPITALIZATION, chkPostHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPostHospitalization.setValue(false);
						 }
						 else
						 {
							 txtPostHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
					 
				 }
				 else
				 {
					 txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		
		
		chkLumpSumAmount.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					validateLumpSumClassification(SHAConstants.LUMPSUMAMOUNT,chkLumpSumAmount);
					 }
					if(!lumpSumValidationFlag)
					{
						/*
						 * Lumpsum can be first rod for medi premier product. Hence if product is
						 * medipremier, then below validation will not happen.
						 * **/
						if(!(ReferenceTable.getLumsumProductKeys().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
							&& !((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
									|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
							&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
							 && !(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
							{
								 if(validateBillClassification())
								 {
									/* Label label = new Label("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
										label.setStyleName("errMessage");
									 HorizontalLayout layout = new HorizontalLayout(
												label);
										layout.setMargin(true);*/
										/*final ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										//dialog.setWidth("55%");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(false);
										dialog.setModal(true);
										dialog.show(getUI().getCurrent(), null, true);*/
										HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
										buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
										GalaxyAlertBox.createErrorBox("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
										chkLumpSumAmount.setValue(false);
										enableOrDisableBillClassification(true);
								 } 
								 else
								 {
									// warnMessageForLumpSum(); 
									 enableOrDisableBillClassification(false);
								 }
						}
						else
						{
							if(!((ReferenceTable.STAR_CANCER_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
									|| ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) 
											|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
									&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
									 || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY)
									 || (ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
							{
							if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getPolicyType() 
									&& ReferenceTable.FRESH_POLICY.equals(bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey()))
							{
								warnMessageForLumpSum();
							}
							 enableOrDisableBillClassification(false);
							}
						}
					}
				 }
				 else
				 {
					 if(null != sectionDetailsListenerTableObj)
					 {
						 String subCovervalue = sectionDetailsListenerTableObj.getSubCoverFieldValue();
						  	if(!(ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(subCovervalue))
							{
						  		enableOrDisableBillClassification(true);
								//enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
							}
					 }
				 }
			}
			}
		});
		
		chkAddOnBenefitsHospitalCash.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
						validateLumpSumClassification(SHAConstants.HOSPITALCASH,chkAddOnBenefitsHospitalCash);
					 }

						if(!lumpSumValidationFlag)
						{
							 if(validateBillClassification())
							 {
								 /*Label label = new Label("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
									label.setStyleName("errMessage");
								 HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);*/
									/*final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
								//	dialog.setWidth("50%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null, true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									chkAddOnBenefitsHospitalCash.setValue(false);
							 }
						}
				 }
			 }
			}
		});
		
		chkAddOnBenefitsPatientCare.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.PATIENTCARE,chkAddOnBenefitsPatientCare);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Patient care cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("50%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Patient care cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkAddOnBenefitsPatientCare.setValue(false);
						 } 
					 }
				 }
			}
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.PARTIALHOSPITALIZATION,chkPartialHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
	
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Already partial hospitalization is existing for this claim.", buttonsNamewithType);
							//	chkPartialHospitalization.setValue(false);
								chkPartialHospitalization.setValue(null);
						 }
						 else
						 {
						 //chkPostHospitalization.setEnabled(true);
						 //chkPreHospitalization.setEnabled(true);
							 txtHospitalizationClaimedAmt.setEnabled(true);
					 }
					}
				 }
				 else
				 {
					 
					 if(validateBillClassification())
					 {
						 /*Label label = new Label("Pre or Post hospitalization cannot exist without Partial hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);
						 layout.setMargin(true);*/
							/*final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
						 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Pre or Post hospitalization cannot exist without Partial hospitalization", buttonsNamewithType);
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(false);
							}
							//chkhospitalization.setValue(false);
					 }
					 
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 /**
					  * The below code was added for ticket 4601.
					  * */
					 
					/* txtHospitalizationClaimedAmt.setValue(null);
					 txtHospitalizationClaimedAmt.setEnabled(false);*/


				 }
			}
			}
		});
		
		chkHospitalizationRepeat
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 if(!bean.getIsQueryReplyReceived()){
						 validateLumpSumClassification(SHAConstants.HOSPITALIZATION_REPEAT, chkHospitalizationRepeat);
					 }
					 if(!lumpSumValidationFlag)
					 {

					 /*if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
									 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
									 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
									 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
									 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
									 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()))
					   )*/
					 if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
							 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
							 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
							 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
							 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
							 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()) ||
							 (null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
					 )
			   )
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Hospitalization Repeat cannot exist without hospitalization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
									 label);
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("40%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Hospitalization Repeat cannot exist without hospitalization", buttonsNamewithType);
								//chkHospitalizationRepeat.setValue(false);
								chkHospitalizationRepeat.setValue(null);
						 }
						 else
						 {
							 /**
							  * Added for ticket #5202.
							  * */
							 fireViewEvent(CreateRODDocumentDetailsPresenter.VALIDATE_HOSPITALIZATION_REPEAT, bean.getClaimDTO().getKey());
							/* txtHospitalizationClaimedAmt.setEnabled(true);
							 chkhospitalization.setEnabled(false);
							 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
							 chkPreHospitalization.setEnabled(false);
							 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
							 chkPostHospitalization.setEnabled(false);
							 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
							 chkPartialHospitalization.setEnabled(false);
							 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
							 chkLumpSumAmount.setEnabled(false);
							 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
							 chkAddOnBenefitsHospitalCash.setEnabled(false);
							 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
							 chkAddOnBenefitsPatientCare.setEnabled(false);*/
						 }
					 }
					 else
					 {
						/* Label label = new Label("None of the classification details can be selected along with hospitalization repeat", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);
							layout.setMargin(true);*/
							/*final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
						//	dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the classification details can be selected along with hospitalization repeat", buttonsNamewithType);
							chkHospitalizationRepeat.setValue(false);
							if (null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue())
							 {
								 txtHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) 
							 {
								 txtPreHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) 
							 {
								 txtPostHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if(null != chkAddOnBenefitsHospitalCash && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
								{
									chkAddOnBenefitsHospitalCash.setValue(null);
								}
								else if(null != chkAddOnBenefitsHospitalCash)
								{
									chkAddOnBenefitsHospitalCash.setEnabled(false);
								}
								if(null != chkAddOnBenefitsPatientCare && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
								{
									chkAddOnBenefitsPatientCare.setValue(null);
								}
								else if(null != chkAddOnBenefitsPatientCare)
								{
									chkAddOnBenefitsPatientCare.setEnabled(false);
								}
							 
							/* if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
							 {
								 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
								 {
									 chkPreHospitalization.setEnabled(true);
								 }
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
								 {
									 chkPostHospitalization.setEnabled(true);
								 }
								 
							 }*/
							 
							/*chkhospitalization.setValue(false);
							chkPreHospitalization.setValue(false);
							chkPostHospitalization.setValue(false);
							chkPartialHospitalization.setValue(false);
							chkLumpSumAmount.setValue(false);
							chkAddOnBenefitsHospitalCash.setValue(false);
							chkAddOnBenefitsPatientCare.setValue(false);*/
					 }	
				 }
				 else
				 {
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 
					// if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
					 //{
						 txtHospitalizationClaimedAmt.setValue(null);
					 //}
					 
					 //txtHospitalizationClaimedAmt.setValue(null);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
					 {
						 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
						 if(null != chkhospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(docRecFromVal.getValue())) ||("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 if(null != chkhospitalization && !chkhospitalization.isEnabled())
						 chkhospitalization.setEnabled(true);
						// if(null != chkPreHospitalization && !chkPreHospitalization.isEnabled())
						 if(null != chkPreHospitalization && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
						 {
							/* if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
							 {
								 chkPreHospitalization.setEnabled(false);
								 chkPreHospitalization.setValue(true);
							 }
							 else*/
							 {
								 chkPreHospitalization.setEnabled(true);
							 }
							 
						 }
						 //if(null != chkPostHospitalization && !chkPostHospitalization.isEnabled())
						 if(null != chkPostHospitalization && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
						 {
							 /*if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
							 {
								 chkPostHospitalization.setEnabled(false);
								 chkPostHospitalization.setValue(true);
							 }*/
							// else
							 {
								 chkPostHospitalization.setEnabled(true);
							 }
						 }
						 if(null != chkPartialHospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue())) //||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
						 chkPartialHospitalization.setEnabled(true);
						if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag"))) 
						 chkLumpSumAmount.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
						 chkAddOnBenefitsHospitalCash.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
						 chkAddOnBenefitsPatientCare.setEnabled(true);
					 }
					/* else
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Please select documents received from"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
					 }*/
				 }
				 }
			}
			}
		});	
		
		
		chkOtherBenefits .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();				
						 
				 buildOtherBenefitsLayout(value);
				 
				 if(value)
				 {
					 txtOtherBenefitsClaimedAmnt.setEnabled(true);
				 }
				 else{
					 txtOtherBenefitsClaimedAmnt.setEnabled(false);
					 txtOtherBenefitsClaimedAmnt.setValue(null);
				 }
						
				 }							
			}
			
		});
		
		chkHospitalCash .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();				
			
				 if(value)
				 {
					 txtHospitalCashClaimedAmnt.setEnabled(true);
				 }
				 else{
					 txtHospitalCashClaimedAmnt.setEnabled(false);
					 txtHospitalCashClaimedAmnt.setValue(null);
				 }
						
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
			if ( null != this.bean.getClaimDTO().getNewIntimationDto().getInsuredPatientName() && (this.bean.getClaimDTO().getNewIntimationDto().getInsuredPatientName()).equalsIgnoreCase(insuredPatientList.getIdByIndex(i).getValue()))
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
				 cmbPayeeName.setContainerDataSource(payeeNameList);
				 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				 cmbPayeeName.setItemCaptionPropertyId("value");
				 
				 if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
								("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())){
					 
					 SelectValue payeeName = new SelectValue();
					 BeanItemContainer<SelectValue> payeeNameContainerForCashless = new BeanItemContainer<SelectValue>(SelectValue.class);
					 payeeName.setId(1l);
					 if(null != this.bean.getDocumentDetails().getHospitalPayableAt()){
						 payeeName.setValue(this.bean.getDocumentDetails().getHospitalPayableAt());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }else if(null != this.bean.getDocumentDetails().getHospitalName()){
						 payeeName.setValue(this.bean.getDocumentDetails().getHospitalName());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }
					 cmbPayeeName.setValue(payeeName);
					 
				 }

				 
				 for(int i = 0 ; i<payeeNameList.size() ; i++)
				 	{
					 
						/*if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
						{
							this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
							//this.txtReasonForChange.setEnabled(false);
						}*/
						/*if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
								("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
						{
							
								//if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
								*//**
								 * 
								 * As a part of production fix , in case of document
								 * received from hospital, then payee name should be
								 * hospital payable at instead of hospital name.
								 * Hence, we need to check hospital payable at 
								 * instead of hospital name.
								 * 
								 * *//*
						
							//if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							if(null != this.bean.getDocumentDetails().getHospitalPayableAt() && this.bean.getDocumentDetails().getHospitalPayableAt().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
								{
									this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
									//this.cmbPayeeName.setEnabled(false);
								}
							else if(null != this.bean.getDocumentDetails().getHospitalName() && this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							{
								this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
								//this.cmbPayeeName.setEnabled(false);
							}
							//}
						}
						else*/ if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
									("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
						 {
							
							if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
							{
								//this.cmbPayeeName.setReadOnly(false);
								this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
								this.bean.getPreauthDTO().getPreauthDataExtractionDetails().setPayeeName(payeeNameList.getIdByIndex(i));
								//this.cmbPayeeName.setReadOnly(true);
								//this.txtReasonForChange.setEnabled(false);
							}
							
							if(null !=this.bean.getDocumentDetails().getHospitalName())
							{
							 if(this.bean.getDocumentDetails().getHospitalName().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
								{
								 	cmbPayeeName.removeItem(payeeNameList.getIdByIndex(i));
									//this.cmbPayeeName.setEnabled(false);
								}
							}
							
							if(txtRelationship != null)
								txtRelationship.setValue(this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName() != null ? this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName().getRelationshipWithProposer() : "");
						 }
					// cmbPayeeName.removeItem(this.bean.getDocumentDetails().getHospitalName());
				 }
			 } catch(Exception e) {
				 System.out.println("ERROR OCCURED WHILE SETTING THE PAYEE NAME USING IDBYINDEX----------->"+ e.getMessage());
			 }
			
		 }
		 
		 patientStatusContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("patientStatusContainer");
		 
		 cmbPatientStatus.setContainerDataSource(patientStatusContainer);
		 cmbPatientStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbPatientStatus.setItemCaptionPropertyId("value");
		 
		 //added for new product
		 diagnosisHospitalCashContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("diagnosisHospitalCashContainer");
		 
         cmbDiagnosisHospitalCash.setContainerDataSource(diagnosisHospitalCashContainer);
         cmbDiagnosisHospitalCash.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbDiagnosisHospitalCash.setItemCaptionPropertyId("value");
		 
		 SelectValue patientStatus = this.bean.getDocumentDetails().getPatientStatus();

		 if(this.bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes")) {
			 
			 if(reconsiderRODRequestList != null && !reconsiderRODRequestList.isEmpty()) {
				 //commented for jira IMSSUPPOR-29002
//				 for (ReconsiderRODRequestTableDTO reconsiderRODRequest : reconsiderRODRequestList) {
//					 patientStatus = reconsiderRODRequest.getSelect() != null && reconsiderRODRequest.getSelect() ? reconsiderRODRequest.getPatientStatus() : null;  
				     patientStatus = reconsiderRODRequestList.get(0).getPatientStatus();
					 cmbPatientStatus.setEnabled(false);
					 if(deathDate != null) {
						 deathDate.setEnabled(false);
					 }	 
					 
					 if(txtReasonForDeath != null) {
						 txtReasonForDeath.setEnabled(false);
					 }
//			 }
			 }
		 }
		 
		 
		 for(int i = 0 ; i<patientStatusContainer.size() ; i++)
		 {
			 if (patientStatus != null 
					 && patientStatus.getValue() != null
					 && patientStatus.getValue().equalsIgnoreCase(patientStatusContainer.getIdByIndex(i).getValue())) {
				 this.cmbPatientStatus.setValue(patientStatusContainer.getIdByIndex(i));
			 }
		}	 
		 
		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
		// documentCheckListValidation.setReference(referenceDataMap);
		 
		 if(null != documentCheckListValidation)
			{
				documentCheckListValidation.setReferenceData(referenceDataMap);
			}
		 
		 //this.docsDetailsList = (List<DocumentDetailsDTO>)referenceDataMap.get("validationDocList");
	//	 rodQueryDetails.generateDropDown(reconsiderationRequest);
		// this.docDTO = (DocumentDetailsDTO) referenceDataMap.get("billClaissificationDetails");
		 
//		 this.docDTO = (List<DocumentDetailsDTO>) referenceDataMap.get("billClaissificationDetails");
		 
		 List<DocumentDetailsDTO> reimbursementList = (List<DocumentDetailsDTO>) referenceDataMap.get("billClaissificationDetails");
		 for (DocumentDetailsDTO documentDetailsDTO : reimbursementList) {
			if(!(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId())){
				this.docDTO.add(documentDetailsDTO);
			}
		}
		
		 this.isFinalEnhancement = (Boolean)referenceDataMap.get("isFinalEnhancement");
		 if(!isReconsider)
		 {
			 invokeBillClassificationListner();
		 }
		 
		 /**
		  * The below method will populate the amount claimed details based on the bill
		  * classification options. Hence after invoking that listener we need to populate the same.
		  * Because during invocation, the fields will get reset. Hence the values from
		  * acknowledgement screen had to be populated after invocation of listener.
		  * */
		 displayAmountClaimedDetails();
		 
		 setValuesFromDTO();
		 
		 if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
					("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())) {
			 if(null != bean.getNewIntimationDTO().getPolicy() && 
						(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						|| ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) 
								|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
						 || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY)){
					this.cmbPayeeName.setReadOnly(true);
			}
		 }
		 
		 if(cmbDocumentsReceivedFrom !=null && cmbDocumentsReceivedFrom.getValue() !=null){
			 SelectValue docReceivedFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
			 if(docReceivedFrom.getId() !=null && !bean.getIsQueryReplyReceived()
					 && ReferenceTable.RECEIVED_FROM_INSURED.equals(docReceivedFrom.getId())){
				 grievanceRepresentationOpt.setEnabled(true);
			 }
		 }											
		// validateBillClassificationDetails();
	}
	
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkhospitalization);
		fieldList.add(chkPreHospitalization);
		fieldList.add(chkPostHospitalization);
		fieldList.add(chkPartialHospitalization);
		fieldList.add(chkLumpSumAmount);
		fieldList.add(chkAddOnBenefitsHospitalCash);
		fieldList.add(chkAddOnBenefitsPatientCare);
		fieldList.add(chkHospitalizationRepeat);
		if(null != cmbReasonForReconsideration)
			fieldList.add(cmbReasonForReconsideration);
		if(null != optPaymentCancellation)
			fieldList.add(optPaymentCancellation);
		if(chkOtherBenefits != null){
			fieldList.add(chkOtherBenefits);
		}
		
		return fieldList;
	}
	
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		//fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		/*fieldList.add(txtLegalHeirName);*/
		/*fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);*/
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
		fieldList.add(txtPayableAt);
		fieldList.add(txtPayModeChangeReason);
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
		StringBuffer eMsg = new StringBuffer();
		Boolean isReconsiderationRequest = false;
		
		
		
		
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

		SelectValue patientStatusvalue = cmbPatientStatus.getValue() != null ? (SelectValue) cmbPatientStatus.getValue() : null;
		
		if(cmbReconsiderationRequest != null  || bean.getIsQueryReplyReceived()) {
			if((bean.getDocumentDetails().getReconsiderationRequestValue() != null && bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes"))
					|| bean.getIsQueryReplyReceived()) {
		
				if(patientStatusvalue != null && (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatusvalue.getId()) || 
						ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatusvalue.getId()))
						&& null != txtPayModeChangeReason && ((txtPayModeChangeReason.getValue() == null) || (txtPayModeChangeReason.getValue().equals("")))){
					Long rodKey = 0l;
					
					if(bean.getIsQueryReplyReceived()){
						
						rodKey = bean.getRodqueryDTO().getReimbursementKey();
					}
					else
					{
						if(bean.getReconsiderRODdto() != null){
							rodKey = bean.getReconsiderRODdto().getRodKey();
						}
					}
					
					Reimbursement reimbursement = reimbursementService.getReimbursementByKey(rodKey);
					
					if(null != reimbursement && null != reimbursement.getPaymentModeId() && !(reimbursement.getPaymentModeId().equals(bean.getDocumentDetails().getPaymentModeChangeFlag()))){				
			
				hasError = true;
				eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
			}
			
		}
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
		
//		if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
//		{
//			if(!isValidEmail(this.txtEmailId.getValue()))
//			{
//				hasError = true;
//				eMsg += "Please enter a valid email </br>";
//			}
//		}
		
		if(patientStatusvalue != null && (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatusvalue.getId()) 
				|| ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatusvalue.getId()))) {
			
				if(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getDocumentDetails().getPaymentModeChangeFlag())
						&& this.txtPayableAt != null
						 && (this.txtPayableAt.getValue() == null || ("").equalsIgnoreCase(this.txtPayableAt.getValue())))
				{
					hasError = true;
					eMsg.append("Please enter Payable At </br>");
				}
					
				if(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getDocumentDetails().getPaymentModeChangeFlag()) && this.txtIfscCode  != null && (this.txtIfscCode.getValue() == null || ("").equalsIgnoreCase(this.txtIfscCode.getValue())))
				{
					hasError = true;
					eMsg.append("Please enter ifsc code </br>");
				}
				
				if(bean.getPreauthDTO().getPolicyDto().getPaymentParty() != null && !bean.getPreauthDTO().getPolicyDto().getPaymentParty().isEmpty()
						&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())
						&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getDocumentDetails().getPaymentModeChangeFlag())
						&& txtAccntNo != null && (txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty())) {
					
					hasError = true;
					eMsg.append("Please Enter Account No</br>");
				}		
				
		}		
		
		if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			{
				
				if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
						(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
						
						(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
						
						(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
						
						
						(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
						
						(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
						
						(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
						
						(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()) ||
						(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue()) ||
						
						(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue()))
						)
					
					
				{
					hasError = true;
					eMsg.append("Please select any one bill classification value.</br>");
				}
				
				
				if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
				{
					if(!((null != this.chkEmergencyMedicalEvaluation && null != this.chkEmergencyMedicalEvaluation.getValue() && this.chkEmergencyMedicalEvaluation.getValue()) ||
						(null != this.chkCompassionateTravel && null != this.chkCompassionateTravel.getValue() && this.chkCompassionateTravel.getValue()) ||
						(null != this.chkRepatriationOfMortalRemains && null != this.chkRepatriationOfMortalRemains.getValue() && this.chkRepatriationOfMortalRemains.getValue()) ||
						(null != this.chkPreferredNetworkHospital && null != this.chkPreferredNetworkHospital.getValue() && this.chkPreferredNetworkHospital.getValue()) ||
						(null != this.chkSharedAccomodation && null != this.chkSharedAccomodation.getValue() && this.chkSharedAccomodation.getValue())))
					{
						
						hasError = true;
						eMsg.append("Please select any one of the benefits </br>");
						
					}
				}
				
				if((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
						(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue())
						)
				{
					if(!(null != this.txtHospitalizationClaimedAmt && null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg.append("Please enter Hospitalization claimed amount </br>");
					}
				}
				if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
				{
					if(!(null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg.append("Please enter Pre Hospitalization claimed amount </br>");
					}
				}
				if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
				{
					if(!(null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg.append("Please enter Post Hospitalization claimed amount </br>");
					}
				}
				
				if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
				{
					if(!(null != this.txtOtherBenefitsClaimedAmnt && null != this.txtOtherBenefitsClaimedAmnt.getValue() && !("").equalsIgnoreCase(this.txtOtherBenefitsClaimedAmnt.getValue())))
					{
						hasError = true;
						eMsg.append("Please enter Other Benefits claimed amount </br>");
					}
				}
				
				if(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())
				{
					if(!(null != this.txtHospitalCashClaimedAmnt && null != this.txtHospitalCashClaimedAmnt.getValue() && !("").equalsIgnoreCase(this.txtHospitalCashClaimedAmnt.getValue())))
					{
						hasError = true;
						eMsg.append("Please enter hospital Cash claimed amount </br>");
					}
				}
				
				
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					
					if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
						
					    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
						
						if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
								&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
							
							if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
									(null != chkhospitalization &&  ((null == chkhospitalization.getValue()) 
											|| (chkhospitalization.getValue() != null && !chkhospitalization.getValue())))){
								hasError = true;
								eMsg.append("Document Received from can not be Hospital for classification type other than Hospital</br>");
								
							}
						}
					}
					
					//IMSSUPPOR-28062
					if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
					{
						if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
						{
							hasError = true;
							eMsg.append("No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>");
						}
						else if(null == this.bean.getReconsiderRODdto())
						{
							{
								hasError = true;
								eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
							}
						}
						
						if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
						{
							hasError = true;
							eMsg.append("Please select payment cancellation needed since reconsideration request is yes </br>");
						}
					}
					
					}
			}
			else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			{
				isReconsiderationRequest = true;
				if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
				{
					hasError = true;
					eMsg.append("No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>");
				}	
				/*if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
				{
					hasError = true;
					eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
				}
				*/
				else if(null != this.reconsiderDTO)
				{
					//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
					if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
					{
						hasError = true;
						eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
					}
					
//					IMSSUPPOR-31760
					if(this.bean.getReconsiderRODdto() != null && this.bean.getReconsiderRODdto().getSelect() == null){
						hasError = true;
						eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
					}
				}
				
				if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
				{
					hasError = true;
					eMsg.append("Please select payment cancellation needed since reconsideration request is yes </br>");
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
		
	
		
		if(null != dateOfAdmission && ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()) || (null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))) 
						  )))
		{
			if(null == dateOfAdmission.getValue())
			{
				hasError = true;
				eMsg.append("Please enter admission date. </br>");
			}
			else if(!(null != this.txtReasonForChangeInDOA && !("").equalsIgnoreCase(this.txtReasonForChangeInDOA.getValue())))
			{
				if(isReasonForChangeReq)
				{
				hasError = true;
				eMsg.append("Please enter reason for change in DOA. </br>");
				}
				
			}
			if(null == dateOfDischarge.getValue())
			{
				hasError = true;
				eMsg.append("Please enter discharge date. </br>");
			}
		}
		
		
		if(null != txtHospitalName && ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				(( null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag())) 
						  )))
		
		{
			if(null == txtHospitalName.getValue() || ("").equalsIgnoreCase(txtHospitalName.getValue()))
			{
				hasError = true;
				eMsg.append("Please enter hospital name</br>");
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
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
		{
			SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
			if(docRecFrom != null && docRecFrom.getId() != null &&   docRecFrom.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				
				Integer doubleFromString = 0;
				
				if(null != txtHospitalizationClaimedAmt)
				
				doubleFromString += SHAUtils.getDoubleFromString(txtHospitalizationClaimedAmt.getValue());
				
				if(null != txtPreHospitalizationClaimedAmt)
//					preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
				
				doubleFromString += SHAUtils.getDoubleFromString(txtPreHospitalizationClaimedAmt.getValue());
				
				if(null != txtPostHospitalizationClaimedAmt)
//					postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
					doubleFromString += SHAUtils.getDoubleFromString(txtPostHospitalizationClaimedAmt.getValue());
				
				if(null != txtOtherBenefitsClaimedAmnt)
					
					doubleFromString += SHAUtils.getDoubleFromString(txtOtherBenefitsClaimedAmnt.getValue());
				
				if(patientStatusvalue != null
						&& (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatusvalue.getId())
								|| ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatusvalue.getId()))) {
				//added for new product
	            if(null != txtHospitalCashClaimedAmnt)
					
					doubleFromString += SHAUtils.getDoubleFromString(txtHospitalCashClaimedAmnt.getValue());
				
				if(txtPanNo != null){
						String panNumber = txtPanNo.getValue();
						
						if(doubleFromString != null && ((panNumber == null) || (panNumber != null && panNumber.equals("")))){
							Double totalAmt = Double.valueOf(doubleFromString);
							if(SHAConstants.FINANCIAL_APPROVED_AMT_FOR_PAN_NUMBER <= totalAmt){
								hasError = true;
								eMsg.append("Please Enter Pan Number</br>");
							}
						}
						
						
				}
				
				
				if(txtPanNo != null && txtPanNo.getValue() != null  && ! txtPanNo.getValue().equalsIgnoreCase("")){
					String value = txtPanNo.getValue();
					if(value.length() != 10){
						hasError = true;
						eMsg.append("PAN number should be 10 digit value</br>");
					}
				}

				/*if(optPaymentMode.getValue() != null 
						&& !(Boolean)optPaymentMode.getValue()
						&& bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())) {
					if(txtAccountPref != null && (txtAccountPref.getValue() == null || txtAccountPref.getValue().isEmpty())){
						hasError = true;
						eMsg.append("Please Select Account Preference</br>");
					}
				}*/
				
				}
				
				//eMsg = validateNomineeLegalHeirTableValues(hasError,eMsg);
				 Map<Boolean, StringBuffer> resultMap = validateNomineeLegalHeirTableValues(hasError,eMsg);
				 for (Map.Entry<Boolean,StringBuffer> entry : resultMap.entrySet()){
					hasError =  entry.getKey();
                    eMsg = entry.getValue(); 
				 }
			}
		}
		
		
		if(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT.equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				&& patientStatusvalue != null
				&& (ReferenceTable.PATIENT_STATUS_DISCHARGED.equals(patientStatusvalue.getId()) 
						|| ReferenceTable.getNewPatientStatusKeys().containsKey(patientStatusvalue.getId())))
				{
					//if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getDocumentDetails().get))
					if(null != txtReasonForChange && txtReasonForChange.isEnabled() && (null == txtReasonForChange.getValue() || ("").equalsIgnoreCase(txtReasonForChange.getValue())))
							{
										hasError = true;
										eMsg.append("Please enter reason for change, since payee name is changed in payment details</br>");
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
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isValid = rodQueryDetails.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.rodQueryDetails.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		
		if((null != this.txtHospitalizationClaimedAmt && 
				null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())) ||
				((null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() &&
				!("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue()))) ||
				((null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() &&
				!("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue()))) ||
				((null != this.txtOtherBenefitsClaimedAmnt && null != this.txtOtherBenefitsClaimedAmnt.getValue() && 
				!("").equalsIgnoreCase(this.txtOtherBenefitsClaimedAmnt.getValue())))  ||
				((null != this.txtHospitalCashClaimedAmnt && null != this.txtHospitalCashClaimedAmnt.getValue() && 
				!("").equalsIgnoreCase(this.txtHospitalCashClaimedAmnt.getValue()))))
		{
		
			if(null != this.optDocumentVerified && null == this.optDocumentVerified.getValue()){
			
				hasError = true;
				eMsg.append("Please select document verified option</br>");
			}
		}
		
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
				
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
							(chkOtherBenefits != null && chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue())){
						if(! bean.getDocumentDetails().getIsOtherBenefitApplicableInPreauth()){
						hasError = true;
						eMsg.append("Other Benefits is not applicable. Since It is not applied in Cashless");
						}else{
							if(chkEmergencyMedicalEvaluation != null && chkEmergencyMedicalEvaluation.getValue() != null && chkEmergencyMedicalEvaluation.getValue() && ! this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
								hasError = true;
								eMsg.append("Emergency Medical Evaluation is not applicable. Since It is not applied in Cashless");
							}
							if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() != null && chkRepatriationOfMortalRemains.getValue() && ! this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
								hasError = true;
								eMsg.append("Repatriation Of Mortal Remains is not applicable. Since It is not applied in Cashless");
							}
							if(chkEmergencyMedicalEvaluation != null && chkEmergencyMedicalEvaluation.getValue() != null && ! chkEmergencyMedicalEvaluation.getValue() && this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
								hasError = true;
								eMsg.append("Please select Emergency Medical Evaluation. Since It is applied in Cashless");
							}
							if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() != null && ! chkRepatriationOfMortalRemains.getValue() && this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
								hasError = true;
								eMsg.append("Please select Repatriation Of Mortal Remains. Since It is applied in Cashless");
							}
							
							if(chkEmergencyMedicalEvaluation != null  &&  chkEmergencyMedicalEvaluation.getValue() == null && this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
								hasError = true;
								eMsg.append("Please select Emergency Medical Evaluation. Since It is applied in Cashless");
							}
							if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() == null && this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
								hasError = true;
								eMsg.append("Please select Repatriation Of Mortal Remains. Since It is applied in Cashless");
							}
							
							
						}
					} else if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
							(chkOtherBenefits != null && chkOtherBenefits.getValue() != null && ! chkOtherBenefits.getValue())){
						if(bean.getDocumentDetails().getIsOtherBenefitApplicableInPreauth()){
							hasError = true;
							eMsg.append("Please select Other benefit,  Since It is applied in Cashless");
						}
					}
				}
			  }
			}
		
		//IMSSUPPOR-23596
		Boolean isQueryRod = Boolean.FALSE;
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails.getValues();
		if(null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty())
		{
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {

				if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
				{
					isQueryRod = Boolean.TRUE;
					break;
				}
			}
		}
		
		//added for new product076 comment for new change in hosp cash flow
		if(/*this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				||*/ this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			if(cmbDiagnosisHospitalCash == null || cmbDiagnosisHospitalCash.isEmpty()){
				hasError = true;
				eMsg.append("Please select Hospital Diagnosis");
			}
		}
		//IMSSUPPOR-29126
		if(isReconsider != null && !isReconsider && !isQueryRod && (null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue())) {
			for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
				if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) &&  !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
					{
						hasError = true;
						eMsg.append("Already Partial Hospitalization is existing for this claim.");
						break;
					}
			}
		}
		
		//IMSSUPPOR-36030
		if(isReconsider != null && !isReconsider && !isQueryRod && (null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue())) {
			for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
				if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) &&  !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
					{
						hasError = true;
						eMsg.append("Already Hospitalization is existing for this claim.");
						break;
					}
			}
		}
		
		//added for admission date validation by noufel
		if(dateOfAdmission != null){
			Date dateOfAdmissionValue = dateOfAdmission.getValue() != null ? dateOfAdmission.getValue() : null;
			Date policyFrmDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate();
			Date policyToDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate();
			
			if((bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
					|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))){

				policyFrmDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
				policyToDate = (bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getInsuredPatient().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());
				
				if(bean.getNewIntimationDTO().getPolicy().getSectionCode() != null && bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getNewIntimationDTO().getGmcMainMember() != null){
					policyFrmDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
					policyToDate = (bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() != null ? bean.getNewIntimationDTO().getGmcMainMember().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());		
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
		/*	Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);*/

			/*ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			return !hasError;
		} 
		else 
		//Above code has some issues. Hence commenting the same.Once fixed shall check with that.
		{
			try {
				this.binder.commit();
				
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
				
				if(null != txtOtherBenefitsClaimedAmnt)
					otherBenefitAmnt = txtOtherBenefitsClaimedAmnt.getValue();
				
				//added for new product
				if(null != txtHospitalCashClaimedAmnt)
					HospitalCashClaimedAmnt = txtHospitalCashClaimedAmnt.getValue();
				
				if(!this.sectionDetailsListenerTableObj.getValues().isEmpty()) {
					bean.setSectionDetailsDTO(this.sectionDetailsListenerTableObj.getValues().get(0));
				}
				
				if(null != chkOtherBenefits && chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue() == false)
				{
					bean.getDocumentDetails().setOtherBenefitsFlag(SHAConstants.N_FLAG);
				}
			} catch (CommitException e) {
				e.printStackTrace();
			}
			
			showOrHideValidation(false);
			return true;
		}		
	}
	public Boolean validateMisApproval(){
		Boolean hasError = true;
		if(this.bean.getRejectionDetails() != null && !this.bean.getRejectionDetails().isEmpty()){
			List<ReimbursementRejectionDto> rejectionDetails = this.bean.getRejectionDetails();
			List<ReconsiderRODRequestTableDTO> reconsiderRod = this.bean.getReconsiderRodRequestList();
			SelectValue docRecvd = (SelectValue) cmbDocumentsReceivedFrom.getValue();
			for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderRod) {
					for (ReimbursementRejectionDto rejectionDetailsDto : rejectionDetails) {
						if((!(reconsiderRODRequestTableDTO.getIsSettledReconsideration())) &&
								(reconsiderRODRequestTableDTO.getReconsiderationFlag() != null && reconsiderRODRequestTableDTO.getReconsiderationFlag().equals("Y"))){
						if(reconsiderRODRequestTableDTO.getRodKey().equals(rejectionDetailsDto.getReimbursementDto().getKey())
								&&  rejectionDetailsDto.getAllowReconsider() != null && rejectionDetailsDto.getAllowReconsider().equals("Y")){
							hasError = false;
							break;
						} else {
							hasError = true;
						}
						} else {
							hasError = false;
							break;
						}
					} if(hasError) {
						break;
					}
			}

		} else {
			hasError = false;
		}
		return hasError;
	}
	
	public Boolean alertMessageForRejectionReconsider() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.ALLOW_REJECTION_RECONSIDER + "</b>",
				ContentMode.HTML);*/
   		//final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.ALLOW_REJECTION_RECONSIDER + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				wizard.getNextButton().setEnabled(false);
					
			}
		});
		return true;
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
							//	particularsValue.setId(documentCheckListDTO.getKey());
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
		//	List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
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
	
	public void clearPreviousAccountDetailsList()
	{
		//List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
		
		List<PreviousAccountDetailsDTO> previousListTable = this.bean.getPreviousAccntDetailsList();
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
		
		this.bean.setUploadDocsList(uploadDocumentDTOList);
		
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
					break;
				}
			}
		}
		
		if(null != documentDetails.getReconsiderationRequest())
		{
			for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 	{
				if ( documentDetails.getReconsiderationRequest().getValue().equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
					break;
				}
			}
		}
		
		if(null != documentDetails.getReasonForReconsideration())
		{
			cmbReasonForReconsideration.setReadOnly(false);
			for(int i = 0; i<reasonForReconsiderationRequest.size();i++){
					if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase(documentDetails.getReasonForReconsideration().getValue())){	
						cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
						break;
					}
			}
			cmbReasonForReconsideration.setReadOnly(true);
		}
		//GALAXYMAIN-11056
		if(null != documentDetails.getPayeeName()&& documentDetails.getPayeeName().getValue()!=null)
		{
			for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if (null != documentDetails.getPayeeName() && null != documentDetails.getPayeeName().getValue() &&
						documentDetails.getPayeeName().getValue().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
				{
					//this.cmbPayeeName.setReadOnly(false);
					this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
					this.bean.getPreauthDTO().getPreauthDataExtractionDetails().setPayeeName(payeeNameList.getIdByIndex(i));
					//this.cmbPayeeName.setReadOnly(true);
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
		
		/*if(optPaymentMode != null
				&& this.bean.getDocumentDetails().getPaymentMode() != null){
			
			if(null == this.bean.getDocumentDetails().getPaymentMode())
				if(!(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
				{
					optPaymentMode.setReadOnly(false);	
				}
			
			
				optPaymentMode.setValue(this.bean.getDocumentDetails().getPaymentMode());
			
				if(!(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
				{
					optPaymentMode.setReadOnly(true);	
				}
		}*/
		
		if(optPaymentMode != null && documentDetails.getPaymentMode() != null && documentDetails.getDocumentsReceivedFrom() != null
				&& documentDetails.getDocumentsReceivedFrom().getId() != null		
				&& documentDetails.getDocumentsReceivedFrom().getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& documentDetails.getPatientStatus() != null && documentDetails.getPatientStatus().getId() != null
				&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(documentDetails.getPatientStatus().getId())
						|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(documentDetails.getPatientStatus().getId()))) {
			optPaymentMode.setReadOnly(true);
		}
		
		
		
		if(txtRelationship != null)
			txtRelationship.setValue(this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName() != null ? this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getPayeeName().getRelationshipWithProposer() : "");
	}
	
	
	public void saveReconsideRequestTableValue(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO)
	{
		this.bean.setReconsiderRODdto(dto);
		this.reconsiderDTO = dto;
		
		if(null != uploadDocumentDTOList && !uploadDocumentDTOList.isEmpty())
		{
			//uploadDocumentDTOList.clear();
			//uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>();
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
		
		
		cmbReasonForReconsideration.setReadOnly(false);
		for(int i = 0; i<reasonForReconsiderationRequest.size();i++){
			if(dto.getIsSettledReconsideration()){
				if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of settled claims")){	
					cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
					break;
				}
			}
			else if(dto.getIsRejectReconsidered()){
				if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of rejected claims")){
					cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
					break;
				}
			}
		}
		cmbReasonForReconsideration.setReadOnly(true);
		
		
		SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
		if(reconsiderDTO != null 
				&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
						|| (reconsiderDTO.getPatientStatus() != null
								&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
				&& selectValue.getValue().equalsIgnoreCase("insured")
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
			
			if(bean.getNewIntimationDTO().getNomineeList() == null || bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				bean.getPreauthDTO().setLegalHeirDTOList(reconsiderDTO.getLegalHeirDTOList());
				
				if(legalHeirDetails != null) {
					legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
				}	
			}	
			
		}

	}

	private void setUploadedTableList(List<UploadDocumentDTO> uploadDocsDTO) {
	//	uploadDocumentDTOList.clear();
		//uploadDocumentDTOList = new ArrayList<UploadDocumentDTO>(); 
		/*for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			this.uploadDocumentDTOList.add(uploadDocumentDTO);
		}*/
		
		this.uploadDocumentDTOList.addAll(uploadDocsDTO);
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
			
			
			if(null != dto.getOtherBenefitFlag() && ("Y").equalsIgnoreCase(dto.getOtherBenefitFlag()))
			{
				if(null!= txtOtherBenefitsClaimedAmnt)
				{
					txtOtherBenefitsClaimedAmnt.setEnabled(true);
					if(null != otherBenefitAmnt && !("").equalsIgnoreCase(otherBenefitAmnt))
					{
						txtOtherBenefitsClaimedAmnt.setValue(otherBenefitAmnt);
						
					}
					else
					{
						txtOtherBenefitsClaimedAmnt.setValue(null != dto.getOtherBenefitClaimedAmnt() ? String.valueOf(dto.getOtherBenefitClaimedAmnt()) : "");
					}
					//txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
			}
			
//			//added for new prodct
//			if(null != dto.getOtherBenefitFlag() && ("Y").equalsIgnoreCase(dto.getOtherBenefitFlag()))
//			{
//				if(null!= txtOtherBenefitsClaimedAmnt)
//				{
//					txtOtherBenefitsClaimedAmnt.setEnabled(true);
//					if(null != otherBenefitAmnt && !("").equalsIgnoreCase(otherBenefitAmnt))
//					{
//						txtOtherBenefitsClaimedAmnt.setValue(otherBenefitAmnt);
//						
//					}
//					else
//					{
//						txtOtherBenefitsClaimedAmnt.setValue(null != dto.getOtherBenefitClaimedAmnt() ? String.valueOf(dto.getOtherBenefitClaimedAmnt()) : "");
//					}
//					//txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
//				}
//			}
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
			txtPayableAt.setValue(null != dto.getPayableAt() ?  dto.getPayableAt() : "");
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
			
			/*if(null != txtLegalHeirName){
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(null != dto.getLeagalHeirName() ?  dto.getLeagalHeirName() : "");
			txtLegalHeirName.setReadOnly(true);
			}*/
			
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
			
			 for(int i = 0 ; i<patientStatusContainer.size() ; i++)
			 {
				 SelectValue patientStatus = dto.getPatientStatus();
				 if (patientStatus != null 
						 && patientStatus.getValue() != null
						 && patientStatus.getValue().equalsIgnoreCase(patientStatusContainer.getIdByIndex(i).getValue())) {
					 this.cmbPatientStatus.setValue(patientStatusContainer.getIdByIndex(i));
				 }
			}
			
		}
		else
		{
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
			if(null != txtOtherBenefitsClaimedAmnt){
				txtOtherBenefitsClaimedAmnt.setEnabled(false);
				txtOtherBenefitsClaimedAmnt.setValue(null);
			}
			if(null != txtHospitalCashClaimedAmnt){
				txtHospitalCashClaimedAmnt.setEnabled(false);
				txtHospitalCashClaimedAmnt.setValue(null);
			}
			hospitalizationClaimedAmt = "";
			preHospitalizationAmt = "";
			postHospitalizationAmt = "";
			otherBenefitAmnt = "";
			this.bean.setReconsiderRODdto(null);
			isReconsider = false;
		}
	}
	
	

	public void setDocumentDetailsListForValidation(List<DocumentDetailsDTO> documentDetailsDTO)
	{
		//this.docsDetailsList = documentDetailsDTO;
		//validateBillClassification();
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
	
	
	public Boolean validateBillClassification() 
	{
		Boolean isError = false;
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
			
			/*if(null != docDTO)
			{
				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					if(docDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
				if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
				{
					if(docDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
			}*/
			else
			{
				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					isError = false;
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() &&this.chkPartialHospitalization.getValue())
				{
					
					isError = false;
				}
				else if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
						if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
						{
							isError = true;
						}
					}
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
					}
				}
				else if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
				{
					isError = false;
				}
				else
				{
					isError = true;
				}
			}
		}
		return isError;
		
	}

	public void setUpPaymentDetails(ReceiptOfDocumentsDTO rodDTO) {
		//this.bean = bean;
		
	}
	
	/*private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}*/
	/*public void setIFSCDetail(String ifscCode, String branchName, String bankName, String city){
		txtIfscCode.
		
	}*/

	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		
		SelectValue selected = cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
		SelectValue patientStatus = cmbPatientStatus.getValue() != null ? (SelectValue)cmbPatientStatus.getValue() : null;
		
		if(selected != null
				&& selected.getId() != null		
				&& selected.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& patientStatus != null
				&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId())
						|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))) {
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
		else  if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				        && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
	 					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())){
					
				txtAccountPref.setValue(dto.getAccPreference());
				txtAccType.setValue(dto.getAccType());
				
				
				this.bean.setDto(dto);
				txtAccntNo.setValue(dto.getAccNumber());
					
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
					int sno = 1;
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetailsDTO.setSno(sno);
							sno++;
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
	
	public void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails)
	{	
		/***
		 * Commenting the below line of code, since 
		 * there was an issue observed in query reply 
		 * scenario
		 * 
		 * **/
		/*this.chkhospitalization.setValue(false);
		this.chkPreHospitalization.setValue(false);
		this.chkPostHospitalization.setValue(false);
		this.chkLumpSumAmount.setValue(false);
		this.chkHospitalizationRepeat.setValue(false);
		this.chkPartialHospitalization.setValue(false);
		this.chkAddOnBenefitsHospitalCash.setValue(false);
		this.chkAddOnBenefitsPatientCare.setValue(false);*/
		if(null != rodQueryDetails)
		{
				if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
				{
					this.optDocumentVerified.setValue(true);
					isQueryReplyReceived = true;
					this.bean.setIsQueryReplyReceived(true);
					
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
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkLumpSumAmount.setEnabled(false);
					}
					else
					{
						/**
						 * If false is set, then in the listener level, for false, bill
						 * classifications are enabled or disabled. Hence resetting to null
						 * which will not have any effect in listener level.
						 * */
						this.chkLumpSumAmount.setValue(null);
					}
					if(null != rodQueryDetails.getAddOnBeneftisHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBeneftisHospitalCashFlag()))
					{
						this.chkAddOnBenefitsHospitalCash.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkAddOnBenefitsHospitalCash.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsHospitalCash.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsPatientCareFlag()))
					{
						this.chkAddOnBenefitsPatientCare.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkAddOnBenefitsPatientCare.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsPatientCare.setValue(false);
					}
					
					
					if(null != chkOtherBenefits){
					if(null != rodQueryDetails.getOtherBenefitsFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getOtherBenefitsFlag()))
					{
						this.chkOtherBenefits.setValue(true);
						if(null != rodQueryDetails.getOtherBenefitClaimedAmnt())
						{
							this.txtOtherBenefitsClaimedAmnt.setValue(String.valueOf(rodQueryDetails.getOtherBenefitClaimedAmnt()));
						}
						this.chkOtherBenefits.setEnabled(false);
					}
					else
					{
						this.chkOtherBenefits.setValue(false);
					}					
					}
					
					
					if(null !=chkEmergencyMedicalEvaluation)
					{						
					if(null != rodQueryDetails.getEmergencyMedicalEvaluationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getEmergencyMedicalEvaluationFlag()))
					{
						this.chkEmergencyMedicalEvaluation.setValue(true);						
						this.chkEmergencyMedicalEvaluation.setEnabled(false);
					}
					else
					{
						this.chkEmergencyMedicalEvaluation.setValue(false);
					}
					}
					
					if(null != chkCompassionateTravel){
					if(null != rodQueryDetails.getCompassionateTravelFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getCompassionateTravelFlag()))
					{
						this.chkCompassionateTravel.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkCompassionateTravel.setEnabled(false);
					}
					else
					{
						this.chkCompassionateTravel.setValue(false);
					}
					}
					
					if(null != chkRepatriationOfMortalRemains){
					if(null != rodQueryDetails.getRepatriationOfMortalRemainsFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getRepatriationOfMortalRemainsFlag()))
					{
						this.chkRepatriationOfMortalRemains.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkRepatriationOfMortalRemains.setEnabled(false);
					}
					else
					{
						this.chkRepatriationOfMortalRemains.setValue(false);
					}
					}
					
					if(null != chkPreferredNetworkHospital){
					if(null != rodQueryDetails.getPreferredNetworkHospitalFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPreferredNetworkHospitalFlag()))
					{
						this.chkPreferredNetworkHospital.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkPreferredNetworkHospital.setEnabled(false);
					}
					else
					{
						this.chkPreferredNetworkHospital.setValue(false);
					}
					}
					
					if(null != chkSharedAccomodation){
					if(null != rodQueryDetails.getSharedAccomodationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getSharedAccomodationFlag()))
					{
						this.chkSharedAccomodation.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkSharedAccomodation.setEnabled(false);
					}
					else
					{
						this.chkSharedAccomodation.setValue(false);
					}
					}
					
					
					disableBillClassification();
					txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);
					txtOtherBenefitsClaimedAmnt.setEnabled(false);
				/*	if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
					{
						setPaymentDetailsIfQueryReplyYes(rodQueryDetails);
					}
					else
					{			*/		
						setPaymentDetails();
				//	}
						if(null != this.sectionDetailsListenerTableObj)
						{
							//subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());

							this.sectionDetailsListenerTableObj.setEnabled(false);
						}
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				this.bean.setIsQueryReplyReceived(false);
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
						/*if(null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))
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
						}*/
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
						/*if(null != this.bean.getDocumentDetails().getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()))
						{
							
							txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
							//chkPreHospitalization.setValue(true);
						}
						else
						{
							//chkPreHospitalization.setValue(false);
						}*/
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
						/*if(null != this.bean.getDocumentDetails().getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						{
							txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
							//chkPostHospitalization.setValue(true);
							
						}
						else
						{
							//chkPostHospitalization.setValue(false);
						}*/
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
						/*if(null != this.bean.getDocumentDetails().getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()))
							//if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							{
								txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
								chkPartialHospitalization.setValue(true);
								
							}
						else
						{
							chkPartialHospitalization.setValue(false);
						}*/
						txtHospitalizationClaimedAmt.setValue(null);
						chkPartialHospitalization.setValue(null);
					}
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
				{	if(null != chkLumpSumAmount)
					{
						chkLumpSumAmount.setEnabled(true);
					/*//	if(null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue())
						if(null != this.bean.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getLumpSumAmountFlag()))
						{
							chkLumpSumAmount.setValue(true);
						}
						else
						{
							chkLumpSumAmount.setValue(false);
						}*/
						chkLumpSumAmount.setValue(null);
					}
				}
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
				{
					if(null != chkAddOnBenefitsHospitalCash)
					{
						chkAddOnBenefitsHospitalCash.setEnabled(true);
						//if(null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue())
						/*if(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
						{
							chkAddOnBenefitsHospitalCash.setValue(true);
						}
						else
						{
							chkAddOnBenefitsHospitalCash.setValue(false);
						}*/
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
						/*if(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))
						{
							chkAddOnBenefitsPatientCare.setValue(true);
						}
						else
						{
							chkAddOnBenefitsPatientCare.setValue(false);
						}
*/						chkAddOnBenefitsPatientCare.setValue(null);
						}
				}
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
				{					
					if(null != chkOtherBenefits)
					{
						chkOtherBenefits.setEnabled(true);
						
						chkOtherBenefits.setValue(null);
						}
				}
				
				if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal) && null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(false);
					chkHospitalizationRepeat.setValue(null);
				}

				else if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
					chkHospitalizationRepeat.setValue(null);
				}
				resetPaymentDetails();
				if(null != sectionDetailsListenerTableObj)
				{
					subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
					this.sectionDetailsListenerTableObj.setEnabled(true);
				}
				

			}
		}
		//added for new product change.
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
					if(null != chkhospitalization) {
						chkhospitalization.setEnabled(false); }
						if(null != chkHospitalizationRepeat) {
						chkHospitalizationRepeat.setEnabled(false); }
						if(null != chkPreHospitalization) {
						chkPreHospitalization.setEnabled(false); }
						if(null != chkLumpSumAmount) {
						chkLumpSumAmount.setEnabled(false); }
						if(null != chkPostHospitalization) {
						chkPostHospitalization.setEnabled(false); }
						if(null != chkPartialHospitalization) {
						chkPartialHospitalization.setEnabled(false); }
						if(null != chkAddOnBenefitsHospitalCash) {
						chkAddOnBenefitsHospitalCash.setEnabled(false); }
						if(null != chkAddOnBenefitsPatientCare) {
						chkAddOnBenefitsPatientCare.setEnabled(false); }
						if(null != chkOtherBenefits) {
						chkOtherBenefits.setEnabled(false); }
						if(null != chkHospitalCash) {
						chkHospitalCash.setEnabled(true); 
						cmbReconsiderationRequest.setEnabled(false);
						}
					
				}
	}
	
	private List<Field> getListOfTxtFlds()
	{
		List<Field> lstOfTextBox = new ArrayList<Field>();
		lstOfTextBox.add(txtEmailId);
		lstOfTextBox.add(txtPanNo);
		lstOfTextBox.add(txtPayableAt);
		lstOfTextBox.add(txtReasonForChange);
		/*lstOfTextBox.add(txtLegalHeirName);*/
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
			txtPayableAt.setReadOnly(true);

		}
		if(null != txtReasonForChange)
		{
			txtReasonForChange.setReadOnly(false);
			txtReasonForChange.setValue(bean.getDocumentDetails().getReasonForChange());
			txtReasonForChange.setReadOnly(true);
		}
		/*if(null != txtLegalHeirName)
		{
			txtLegalHeirName.setReadOnly(false);
			txtLegalHeirName.setValue(bean.getDocumentDetails().getLegalFirstName());
			txtLegalHeirName.setReadOnly(true);
		}*/
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
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
			txtAccntNo.setReadOnly(true);
		}
		
	}
	/*private void setPaymentDetailsIfQueryReplyYes(RODQueryDetailsDTO rodQueryDetails)
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
			txtPayableAt.setReadOnly(true);

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
		
	}*/
	
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
			 /*HorizontalLayout hLayout = new HorizontalLayout(okButton);
				hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
				hLayout.setMargin(true);
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>The following Attibutes has been changed from Previous ROD : </b>", ContentMode.HTML), new Label(comparisonString, ContentMode.HTML), hLayout);
				layout.setMargin(true);*/
			 
			 /* final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setResizable(false);
				dialog.setModal(true);
				
				Button okButton = new Button("OK");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				dialog.setContent(layout);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createAlertBox("The following Attibutes has been changed from Previous ROD : </b> <br/>"+comparisonString, buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
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
			if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
 					&& ("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue())) {
				txtAccntNo.setEnabled(false);
			}else {						
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
		/*Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		/*Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
		
		/*Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			/*horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);*/
			buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
		}
		/*else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}*/
		 
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
		dialog.setModal(true);*/
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}*/
		/*getUI().getCurrent().addWindow(dialog);*/
		
		
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
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
		Button cancelBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL.toString());
		if(cancelBtn != null){
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
	
	/*public ReceiptOfDocumentsDTO getBean() {
		return bean;
	}
		*/
	
	private void validateLumpSumClassification(String classificationType,CheckBox chkBox)
	{
		Long claimKey = bean.getClaimDTO().getKey();
		Long intimationKey = bean.getClaimDTO().getNewIntimationDto().getKey();
		fireViewEvent(CreateRODDocumentDetailsPresenter.CREATE_ROD_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION,claimKey, classificationType, chkBox,intimationKey);
	}
	
	public void validateLumpSumAmount(Boolean isValid,String classificationType,CheckBox checkBox) {
		// TODO Auto-generated method stub
		if(isValid && (SHAConstants.LUMPSUMAMOUNT).equalsIgnoreCase(classificationType))
		{
			 /*Label label = new Label("Section 2 - Lumpsum cannot be processed under this Intimation", ContentMode.HTML);
				label.setStyleName("errMessage");*/
			 /*HorizontalLayout layout = new HorizontalLayout(
						label);
				layout.setMargin(true);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				//dialog.setWidth("55%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Section 2 - Lumpsum cannot be processed under this Intimation", buttonsNamewithType);
				chkLumpSumAmount.setValue(null);
				lumpSumValidationFlag = true;

		}
		else if(isValid)
		{
			/*Label label = new Label("Section I cannot be processed under this Intimation", ContentMode.HTML);*/
		/*	label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
					label);
			layout.setMargin(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("55%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			//chkLumpSumAmount.setValue(false);
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Section I cannot be processed under this Intimation", buttonsNamewithType);
			if(null != checkBox)
				checkBox.setValue(null);
			lumpSumValidationFlag = true;
		}
		else
		{
			lumpSumValidationFlag = false;
		}
		
	}
	
	private void enableOrDisableBillClassification(Boolean value)
	{
		if(null != chkhospitalization)
		{
			chkhospitalization.setEnabled(value);
			chkhospitalization.setValue(null);
		}
		if(null != chkPreHospitalization)
		{
			chkPreHospitalization.setEnabled(value);
			chkPreHospitalization.setValue(null);
		}
		if(null != chkPostHospitalization)
		{
			chkPostHospitalization.setEnabled(value);
			chkPostHospitalization.setValue(null);
		}
		if(null != chkPartialHospitalization)
		{
			chkPartialHospitalization.setEnabled(value);
			chkPartialHospitalization.setValue(null);
		}
		if(null != chkAddOnBenefitsHospitalCash)
		{
			chkAddOnBenefitsHospitalCash.setEnabled(value);
			chkAddOnBenefitsHospitalCash.setValue(null);
		}
		if(null != chkAddOnBenefitsPatientCare)
		{
			chkAddOnBenefitsPatientCare.setEnabled(value);
			chkAddOnBenefitsPatientCare.setValue(null);
		}
		if(null != chkHospitalizationRepeat)
		{
			chkHospitalizationRepeat.setEnabled(value);
			chkHospitalizationRepeat.setValue(null);
		}
	}
	
	public void resetLumpsumValidationFlag()
	{
		this.lumpSumValidationFlag = false;
	}
	
	public Boolean warnMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>",
				ContentMode.HTML);*/
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
   		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox( SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				alertMessageForLumpSum();
			}
		});
		return true;
	}
	
	public Boolean alertMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>",
				ContentMode.HTML);*/
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
   		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>", buttonsNamewithType);
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
	


	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		this.sectionDetailsListenerTableObj.setCoverList(coverContainer);
		
	}
	
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		this.sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
		
	}
    
	  private void enableOrDisableClassificationBasedOnsubCover(int i,String docRecValue)
	    {
	    	
	    	switch(i)
	    	{
	    		case 1:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(true);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag") && null != chkAddOnBenefitsHospitalCash))
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			}
	    			else if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(true);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag") && null != chkAddOnBenefitsPatientCare))
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
//		    			chkAddOnBenefitsPatientCare.setValue(false);
	    			}else if(null != chkAddOnBenefitsPatientCare){
	    				
	    				chkAddOnBenefitsPatientCare.setEnabled(true);
	    				
	    			}
	    			impactOfHospitalizationRepeatClassification();
	    		break;
	    		
	    		case 2:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    				chkPostHospitalization.setValue(null);
	    			}
	    			
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    				chkPreHospitalization.setValue(null);
	    			}
	    			
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 3:
	    			
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setEnabled(false);
	    				chkhospitalization.setValue(null);
	    			}
	    			if(null != chkPartialHospitalization)
	    			{
	    				chkPartialHospitalization.setEnabled(false);
	    				chkPartialHospitalization.setValue(null);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    				chkPreHospitalization.setValue(null);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    				chkPostHospitalization.setValue(null);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 4:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(true);
	    				chkPostHospitalization.setEnabled(true);
	    			}
	    			
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			
	    		break;
	    		
	    		case 5:
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setEnabled(false);
	    				chkPartialHospitalization.setEnabled(false);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag") && null != chkLumpSumAmount))
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    			}
	    			else if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(true);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    			}
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setValue(null);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setValue(null);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setValue(null);
	    			}
	    			if(null != chkPartialHospitalization)
	    			{
	    				chkPartialHospitalization.setValue(null);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			
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
	    			
	    		break;	
	    		
	    		case 6:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(true);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			impactOfHospitalizationRepeatClassification();
	    		break;
	    		
	    		case 7:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(false);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 8:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(false);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(false);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(false);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    	}
	    	if(chkAddOnBenefitsHospitalCash != null && chkAddOnBenefitsPatientCare != null && chkOtherBenefits != null &&
	    			this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)) {
				chkAddOnBenefitsHospitalCash.setEnabled(false);
				chkAddOnBenefitsPatientCare.setEnabled(false);
				chkOtherBenefits.setEnabled(false);
				
			}
	    	
	    	if(chkAddOnBenefitsPatientCare != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
				chkAddOnBenefitsPatientCare.setEnabled(false);
	    	}
	    	
	    	//Added for new product change
			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
				if(null != chkhospitalization) {
					chkhospitalization.setEnabled(false); }
					if(null != chkHospitalizationRepeat) {
					chkHospitalizationRepeat.setEnabled(false); }
					if(null != chkPreHospitalization) {
					chkPreHospitalization.setEnabled(false); }
					if(null != chkLumpSumAmount) {
					chkLumpSumAmount.setEnabled(false); }
					if(null != chkPostHospitalization) {
					chkPostHospitalization.setEnabled(false); }
					if(null != chkPartialHospitalization) {
					chkPartialHospitalization.setEnabled(false); }
					if(null != chkAddOnBenefitsHospitalCash) {
					chkAddOnBenefitsHospitalCash.setEnabled(false); }
					if(null != chkAddOnBenefitsPatientCare) {
					chkAddOnBenefitsPatientCare.setEnabled(false); }
					if(null != chkOtherBenefits) {
					chkOtherBenefits.setEnabled(false); }
					if(null != chkHospitalCash) {
					chkHospitalCash.setEnabled(true); 
					cmbReconsiderationRequest.setEnabled(false);
					}
				
			}
	    }
	    
	    private void enableDisableHospAndPartialHosp(String docRecValue)
	    {
	    	if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
	    		if(null != chkPartialHospitalization)
	    			chkPartialHospitalization.setEnabled(true);
	    		if(null != chkhospitalization)
	    		{
	    			chkhospitalization.setEnabled(false);
					chkhospitalization.setValue(null);
	    		}
			}
			else if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setEnabled(false);
					chkPartialHospitalization.setValue(null);
				}
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(true);
				
			}
	    	
	    	if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
	    		if(null != chkhospitalization) {
					chkhospitalization.setEnabled(false); }
					if(null != chkHospitalizationRepeat) {
					chkHospitalizationRepeat.setEnabled(false); }
					if(null != chkPreHospitalization) {
					chkPreHospitalization.setEnabled(false); }
					if(null != chkLumpSumAmount) {
					chkLumpSumAmount.setEnabled(false); }
					if(null != chkPostHospitalization) {
					chkPostHospitalization.setEnabled(false); }
					if(null != chkPartialHospitalization) {
					chkPartialHospitalization.setEnabled(false); }
					if(null != chkAddOnBenefitsHospitalCash) {
					chkAddOnBenefitsHospitalCash.setEnabled(false); }
					if(null != chkAddOnBenefitsPatientCare) {
					chkAddOnBenefitsPatientCare.setEnabled(false); }
					if(null != chkOtherBenefits) {
					chkOtherBenefits.setEnabled(false); }
					if(null != chkHospitalCash) {
					chkHospitalCash.setEnabled(true); 
					cmbReconsiderationRequest.setEnabled(false);
					}
				
			}
	    }
	    
	   
	    
	    private void subCoverBasedBillClassificationManipulation(String value,Long productKey)
	    {

			String docRecFromVal = null;
			if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
			{
				SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
				docRecFromVal = selValue.getValue();
			}
				
			if(!((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal))) 
			{
				if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value) && ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))					
				{
					enableOrDisableClassificationBasedOnsubCover(6,docRecFromVal);
				}
				else if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(1,docRecFromVal);
				}
				else if(ReferenceTable.MATERNITY_NORMAL_SUB_COVER_CODE.equalsIgnoreCase(value) || ReferenceTable.MATERNITY_CEASEAREAN_SUB_COVER_CODE.equalsIgnoreCase(value) ||
						ReferenceTable.NEW_BORN_SUB_COVER_CODE.equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(2,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_CHILD_VACCINATION_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.DENTAL_OPTHALMIC_SUB_COVER_CODE).equalsIgnoreCase(value) ||
						(ReferenceTable.HOSPITAL_CASH_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.HEALTH_CHECKUP_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.ACCIDENTAL_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(3,docRecFromVal);
				}
				else if((ReferenceTable.BARIATRIC_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(4,docRecFromVal);
				}
				else if((ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_HOSPITALISATION).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(7,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(8,docRecFromVal);
				}
			}
		
	    }
	    
public void validateHospitalizationRepeat(Boolean isValid) {
			
			if(!isValid)
			{
			 /*Label label = new Label("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", ContentMode.HTML);
				label.setStyleName("errMessage");
			 HorizontalLayout layout = new HorizontalLayout(
					 label);
				layout.setMargin(true);*/
				
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				//dialog.setWidth("35%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", buttonsNamewithType);
				chkHospitalizationRepeat.setValue(null);
			}
			else
			{
				 txtHospitalizationClaimedAmt.setEnabled(true);
				 if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
				 {
					 txtHospitalizationClaimedAmt.setValue(null);
				 }
				 
				 chkhospitalization.setEnabled(false);
				 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
				 chkPreHospitalization.setEnabled(false);
				 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
				 chkPostHospitalization.setEnabled(false);
				 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
				 chkPartialHospitalization.setEnabled(false);
				 //chkPartialHospitalization.setValue(null);
				 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
				 chkLumpSumAmount.setEnabled(false);
				 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
				 chkAddOnBenefitsHospitalCash.setEnabled(false);
				 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
				 chkAddOnBenefitsPatientCare.setEnabled(false);
			}
		}

		private void impactOfHospitalizationRepeatClassification()
		{
			if(null != chkHospitalizationRepeat && null != chkHospitalizationRepeat.getValue() && chkHospitalizationRepeat.getValue())
			{
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(false);
				if(null != chkPreHospitalization)// && chkPreHospitalization.isEnabled())
					chkPreHospitalization.setEnabled(false);
				if(null != chkPostHospitalization)// && chkPostHospitalization.isEnabled())
				 chkPostHospitalization.setEnabled(false);
				if(null != chkPartialHospitalization)// && chkPartialHospitalization.isEnabled())
				 chkPartialHospitalization.setEnabled(false);
				 //chkPartialHospitalization.setValue(null);
				 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
				if(null != chkLumpSumAmount)
					chkLumpSumAmount.setEnabled(false);
				 if(null != chkAddOnBenefitsHospitalCash)// && chkAddOnBenefitsHospitalCash.isEnabled())
				 chkAddOnBenefitsHospitalCash.setEnabled(false);
				if(null != chkAddOnBenefitsPatientCare)// && chkAddOnBenefitsPatientCare.isEnabled())
				 chkAddOnBenefitsPatientCare.setEnabled(false);
			}
			
		}
		
		public void setClearReferenceData(){
			SHAUtils.setClearMapBooleanValues(reconsiderationMap);
			SHAUtils.setClearReferenceData(referenceData);
			if(documentDetailsPageLayout!=null){
				documentDetailsPageLayout.removeAllComponents();
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
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
		
			
			if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
				if(message.length()>4000){
					dialog.setWidth("55%");
				}
			}
			dialog.show(getUI().getCurrent(), null, true);*/
			
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
		
		public  void payModeShortcutListener(TextField searchField, final  Listener listener) {
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
					txtArea.setWidth("280px");
					txtArea.setHeight("150px");
					
					txtArea.setValue(bean.getDocumentDetails().getPayModeChangeReason());			
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
							txtFld.setDescription(txt.getValue());
							bean.getDocumentDetails().setPayModeChangeReason(txt.getValue());	
							
							// TODO Auto-generated method stub
							
						}
					});
					
				
					txtFld.setDescription(txtArea.getValue());
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);	
					vLayout.setWidth("300px");
					vLayout.setHeight("200px");
					vLayout.setMargin(true);
					
					
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
						txtPayModeChangeReason.setValue(value);
						txtPayModeChangeReason.setDescription(value);						
						//bean.getDocumentDetails().setPayModeChangeReason(value);
					}
					
				}
			});		
			
		}
		
		
		 public void claimedAmntAlertPopupMessage() {
				
				/*ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								SHAConstants.ACK_CLAIMED_AMOUNT_ALERT,
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											
											dialog.close();
											
										} else {
											// User did not confirm
											txtHospitalizationClaimedAmt.setValue(null);
											txtPreHospitalizationClaimedAmt.setValue(null);
											txtPostHospitalizationClaimedAmt.setValue(null);
											txtOtherBenefitsClaimedAmnt.setValue(null);
											optDocumentVerified.setValue(null);
											dialog.close();
											dialog.setClosable(false);
											//dialog.setStyleName(Reindeer.WINDOW_BLACK);

										}
									}
								});
					dialog.setClosable(false);*/
					
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
					buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createConfirmationbox(SHAConstants.ACK_CLAIMED_AMOUNT_ALERT, buttonsNamewithType);
					Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
							.toString());
					Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
							.toString());
					yesButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							optDocumentVerified.setValue(null);
						}
						});
					noButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							
						}
						});
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
							bean.getDocumentDetails().setDocumentVerificationFlag(SHAConstants.N_FLAG);
							optDocumentVerified.setValue(null);
						}
						else{
							
				/*			if((null != bean.getDocumentDetails().getHospClaimedAmountDocRec() && null != txtHospitalizationClaimedAmt && null != txtHospitalizationClaimedAmt.getValue() &&
									!(txtHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getHospClaimedAmountDocRec())))) ||
									(null != bean.getDocumentDetails().getPreHospClaimedAmountDocRec() && null != txtPreHospitalizationClaimedAmt && null != txtPreHospitalizationClaimedAmt.getValue() &&
											!(txtPreHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getPreHospClaimedAmountDocRec())))) ||
									(null != bean.getDocumentDetails().getPostHospClaimedAmountDocRec() && null != txtPostHospitalizationClaimedAmt && null != txtPostHospitalizationClaimedAmt.getValue() &&
											!(txtPostHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getPostHospClaimedAmountDocRec())))) ||
									(null != bean.getDocumentDetails().getOtherBenefitsAmountDocRec() && null != txtOtherBenefitsClaimedAmnt && null != txtOtherBenefitsClaimedAmnt.getValue() &&
											!(txtOtherBenefitsClaimedAmnt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getOtherBenefitsAmountDocRec()))))){
								
								fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,null);
								
							}*/
							
							bean.getDocumentDetails().setDocumentVerificationFlag(SHAConstants.YES_FLAG);
						}
					}
					}

				});
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

		  
		  public void policyValidationPopupMessage() {	 
				 
				 /*Label successLabel = new Label(
							"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
							ContentMode.HTML);*/
			   		//final Boolean isClicked = false;
					/*Button homeButton = new Button("OK");
					homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
					layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					layout.setStyleName("borderLayout");*/
					/*HorizontalLayout hLayout = new HorizontalLayout(layout);
					hLayout.setMargin(true);
					hLayout.setStyleName("borderLayout");*/

					/*final ConfirmDialog dialog = new ConfirmDialog();
//					dialog.setCaption("Alert");
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
						}
					});
				}
		    
		  public void setOptDocVerifiedValue(){
			  optDocumentVerified.setValue(null);
		  }
		  
		  public BlurListener getHospitalLisenter() {
				
				BlurListener listener = new BlurListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField property = (TextField) event.getComponent();
						if(null != property)
						{
							if(!("").equalsIgnoreCase(property.getValue())){
							
								/*if(null != bean.getDocumentDetails().getHospClaimedAmountDocRec() && null != txtHospitalizationClaimedAmt && null != txtHospitalizationClaimedAmt.getValue() &&
										!(txtHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getHospClaimedAmountDocRec().intValue())))){
									
									//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
									claimedAmntValidation(property);
									
								}*/
								optDocumentVerified.setEnabled(true);
								optDocumentVerified.setValue(null);
								bean.getDocumentDetails().setHospitalizationClaimedAmount(txtHospitalizationClaimedAmt.getValue());
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
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField property = (TextField) event.getComponent();
						if(null != property)
						{
							if(!("").equalsIgnoreCase(property.getValue())){
							
								/*if(null != bean.getDocumentDetails().getPreHospClaimedAmountDocRec() && null != txtPreHospitalizationClaimedAmt && null != txtPreHospitalizationClaimedAmt.getValue() &&
										!(txtPreHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getPreHospClaimedAmountDocRec().intValue())))){
									
									
									//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
									claimedAmntValidation(property);
								}*/
								optDocumentVerified.setEnabled(true);
								optDocumentVerified.setValue(null);
								bean.getDocumentDetails().setPreHospitalizationClaimedAmount(txtPreHospitalizationClaimedAmt.getValue());
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
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField property = (TextField) event.getComponent();
						if(null != property)
						{
							if(!("").equalsIgnoreCase(property.getValue())){
							
								/*if(null != bean.getDocumentDetails().getPostHospClaimedAmountDocRec() && null != txtPostHospitalizationClaimedAmt && null != txtPostHospitalizationClaimedAmt.getValue() &&
										!(txtPostHospitalizationClaimedAmt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getPostHospClaimedAmountDocRec().intValue())))){
									//fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
									claimedAmntValidation(property);
									
								}*/
								optDocumentVerified.setEnabled(true);
								optDocumentVerified.setValue(null);
								bean.getDocumentDetails().setPostHospitalizationClaimedAmount(txtPostHospitalizationClaimedAmt.getValue());
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
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField property = (TextField) event.getComponent();
						if(null != property)
						{
							if(!("").equalsIgnoreCase(property.getValue())){
							
								/*if(null != bean.getDocumentDetails().getOtherBenefitsAmountDocRec() && null != txtOtherBenefitsClaimedAmnt && null != txtOtherBenefitsClaimedAmnt.getValue() &&
										!(txtOtherBenefitsClaimedAmnt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getOtherBenefitsAmountDocRec().intValue())))){
								//	fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
									claimedAmntValidation(property);
									
								}*/
								optDocumentVerified.setEnabled(true);
								optDocumentVerified.setValue(null);
								bean.getDocumentDetails().setOtherBenefitclaimedAmount(txtOtherBenefitsClaimedAmnt.getValue());
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
		  
		  public void resetClaimedAmntValue(TextField txtField){
			  txtField.setValue(null);
		  }
		  
		  public void claimedAmntValidation(final TextField textField){
				 
				/*Label successLabel = new Label("<b style = 'color: red;'> " + "Amount Claimed - Amount entered is not matching with the amount </br><center>entered at the time of Acknowledgement </center></br><center>\nDo you wish to proceed?</center></b>", ContentMode.HTML);
				
				Button yesButton = new Button("Yes");
				yesButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
				
				/*Button noButton = new Button("No");
				noButton.setStyleName(ValoTheme.BUTTON_DANGER);
				HorizontalLayout horizontalLayout = new HorizontalLayout(yesButton,noButton);
				horizontalLayout.setMargin(true);
				horizontalLayout.setSpacing(true);*/
			//	horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_CENTER);
				
				/*VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
				layout.setSpacing(true);
				layout.setMargin(true);
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(false);
				layout.setStyleName("borderLayout");
				layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);*/
				
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
				HashMap<String,Button> messageBoxButtons = GalaxyAlertBox.createErrorBox("Amount Claimed - Amount entered is not matching with the amount </br><center>entered at the time of Acknowledgement </center></br><center>\nDo you wish to proceed?</center></b>", buttonsNamewithType);
				Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
				Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
				yesButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();					
						
					}
				});
				
				noButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						textField.setValue(null);
						//dialog.close();					
						
					}
				});
			
		  }
		  
		  private void showICRMessage(){
			  String msg = SHAConstants.ICR_MESSAGE;
			 // Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

			 /* Button homeButton = new Button("OK");
			  homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			  VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
			  firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			  firstForm.setSpacing(true);
			  firstForm.setMargin(true);
			  firstForm.setStyleName("borderLayout");*/

			  /*final ConfirmDialog dialog = new ConfirmDialog();
			  dialog.setClosable(false);
			  dialog.setContent(firstForm);
			  dialog.setResizable(false);
			  dialog.setModal(true);
			  dialog.setWidth("20%");
			  dialog.show(getUI().getCurrent(), null, true);*/
			  
			  HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(msg+"</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());

			  homeButton.addClickListener(new ClickListener() {
				  private static final long serialVersionUID = 7396240433865727954L;

				  @Override
				  public void buttonClick(ClickEvent event) {
					  //dialog.close();						
					  if(bean.getPreauthDTO().getIsPolicyValidate()){
						  policyValidationPopupMessage();
					  }/*else if(bean.getIsSuspicious()!=null){
						  StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					  }*/
					  if(bean.getIsPEDInitiated()){
						  alertMessageForPED();
					  }
				  }
			  });
		  }
		   

		  public void setUpPayableDetails(String name){
			  txtPayableAt.setReadOnly(false);
			  txtPayableAt.setValue(name);
			  txtPayableAt.setReadOnly(true);
		  }
		  
		  public void getHospitalCategory(String hospitalCategory) {	 
				 
				 /*Label successLabel = new Label(
							"<b style = 'color: red;'>" + hospitalCategory + " Category Hospital"+ "</b>",
							ContentMode.HTML);*/
					/*Button homeButton = new Button("OK");
					homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
					layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					layout.setStyleName("borderLayout");*/
			
					/*final ConfirmDialog dialog = new ConfirmDialog();
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
							if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
								showICRMessage();
							}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
								policyValidationPopupMessage();
							}		
							/*else if(bean.getIsSuspicious()!=null){
								StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}*/
							else if(bean.getIsPEDInitiated()){
								alertMessageForPED();
							}
						}
					});
				}
		  	
		  	public Boolean validatePaymentCancellation(){
		  		Boolean hasError = false;
		  		if(bean.getDocumentDetails().getPaymentCancellationNeeded() != null && 
		  				bean.getDocumentDetails().getPaymentCancellationNeeded()){
		  			hasError = true;
		  		}
		  		
		  		StringBuffer eMsg = new StringBuffer("");
		  	//IMSSUPPOR-32597d comented for update rod screen
		  		//eMsg = validateNomineeLegalHeirTableValues(hasError,eMsg);
		  		/*Map<Boolean, StringBuffer> resultMap = validateNomineeLegalHeirTableValues(hasError,eMsg);
				 for (Map.Entry<Boolean,StringBuffer> entry : resultMap.entrySet()){
					hasError =  entry.getKey();
                   eMsg = entry.getValue(); 
				 }*/
		  		
		  		return hasError;
		  	}
		  	
			public Boolean alertForvalidatePaymentCancellationFlag() {
		   		/*Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.ALERT_FOR_PAYMENT_CANCELLATION + "</b>",
						ContentMode.HTML);*/
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
						.createErrorBox(SHAConstants.ALERT_FOR_PAYMENT_CANCELLATION + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
//						wizard.getNextButton().setEnabled(false);
							
					}
				});
				return true;
			}
			
			public void showAlertForGMCPaymentParty(String paymentParty,String proposerName,String mainMemberName){	 
				/*Label successLabel = null;
				if(paymentParty != null & paymentParty.equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_CORPORATE)){
					successLabel = new Label(
						"<b style = 'color: red;'>Payment Party is Corporate - Payment to be made to " + proposerName + "</b>",
						ContentMode.HTML);
				} else {
					successLabel = new Label(
							"<b style = 'color: red;'>Payment Party is Main Member - Payment to be made to " + mainMemberName + "</b>",
							ContentMode.HTML);

				}*/
				/*Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");*/

				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				String successString = null;
				if(paymentParty != null & paymentParty.equalsIgnoreCase(SHAConstants.GMC_PAYMENT_PARTY_CORPORATE)){
					successString = "Payment Party is Corporate - Payment to be made to " + proposerName + "</b>";
				} else {
					successString = "Payment Party is Main Member - Payment to be made to " + mainMemberName + "</b>";

				}
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(successString, buttonsNamewithType);
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
						 else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}		
						/*else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}*/
						else if(bean.getIsPEDInitiated()){
							alertMessageForPED();
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
		
		//Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		
		txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
		//VerticalLayout layout = new VerticalLayout(txtArea, homeButton);
		VerticalLayout layout = new VerticalLayout(txtArea);
		//layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Remarks", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(null != bean.getPreauthDTO().getPolicyDto().getGmcPolicyType() && bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber() != null &&
							(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber());
					}
					else if(null != bean.getPreauthDTO().getPolicyDto().getPaymentParty()){
						showAlertForGMCPaymentParty(bean.getPreauthDTO().getPolicyDto().getPaymentParty(),bean.getPreauthDTO().getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}		
					else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}
					else if(bean.getIsPEDInitiated()){
						alertMessageForPED();
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
					if(null != bean.getPreauthDTO().getPolicyDto().getGmcPolicyType() && bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber() != null &&
							(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
									bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
						showAlertForGMCParentLink(bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber());
					}
					else if(null != bean.getPreauthDTO().getPolicyDto().getPaymentParty()){
						showAlertForGMCPaymentParty(bean.getPreauthDTO().getPolicyDto().getPaymentParty(),bean.getPreauthDTO().getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
					}else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
						getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
					}
					else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
						showICRMessage();
					}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
						policyValidationPopupMessage();
					}		
					/*else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}*/
					else if(bean.getIsPEDInitiated()){
						alertMessageForPED();
					}
				}
			});

		
			}
			
			public void showAlertForGMCParentLink(String policyNumber){	 
				 
			    /*Label successLabel = new Label(
						"<b style = 'color: red;'>Policy is  Linked to Policy No " + policyNumber + "</b>",
						ContentMode.HTML);*/
				/*Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");*/

				/*final ConfirmDialog dialog = new ConfirmDialog();
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
						if(null != bean.getPreauthDTO().getPolicyDto().getPaymentParty()){
							showAlertForGMCPaymentParty(bean.getPreauthDTO().getPolicyDto().getPaymentParty(),bean.getPreauthDTO().getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
						}
						else  if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
								getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
							}
						 else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}		
						/*else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}*/
						else if(bean.getIsPEDInitiated()){
							alertMessageForPED();
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
							.createInformationBox(SHAConstants.CMD_ALERT_LATEST + memberType + " Club" + "</b>", buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
					
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();
							if(null != bean.getPreauthDTO().getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getTopUpPolicyAlertFlag())){
								showTopUpAlertMessage(bean.getPreauthDTO().getTopUpPolicyAlertMessage());
							}
							else if(null != bean.getPreauthDTO().getPolicyDto().getGmcPolicyType() && bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber() != null &&
									(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
											bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
								showAlertForGMCParentLink(bean.getPreauthDTO().getPolicyDto().getLinkPolicyNumber());
							}
							else if(null != bean.getPreauthDTO().getPolicyDto().getPaymentParty()){
								showAlertForGMCPaymentParty(bean.getPreauthDTO().getPolicyDto().getPaymentParty(),bean.getPreauthDTO().getPolicyDto().getProposerFirstName(),bean.getClaimDTO().getNewIntimationDto().getGmcMainMemberName());
							}else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
								getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
							}
							else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
								showICRMessage();
							}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
								policyValidationPopupMessage();
							}		
							/*else if(bean.getIsSuspicious()!=null){
								StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}*/
							else if(bean.getIsPEDInitiated()){
								alertMessageForPED();
							}	
						}
					});
				}
			
			 public void buildNomineeLayout(){
					
				if(nomineeDetailsTable != null) { 
					//documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
					nomineeLegalLayout.removeComponent(nomineeDetailsTable);
				}
				if(legalHeirDetails != null) {
					//documentDetailsPageLayout.removeComponent(legalHeirLayout);
					nomineeLegalLayout.removeComponent(legalHeirDetails);
				}
						
				nomineeDetailsTable.init("", false, false);
						
				if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				  nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				  nomineeDetailsTable.generateSelectColumn();
				  nomineeDetailsTable.setScreenName(SHAConstants.CREATE_ROD);
				  PreauthDTO preauthDto = bean.getPreauthDTO();
				  preauthDto.setNewIntimationDTO(bean.getNewIntimationDTO());
				  SelectValue patientStatus = (SelectValue) cmbPatientStatus.getValue();
				  preauthDto.getPreauthDataExtractionDetails().setPatientStatus(patientStatus);
				  viewSearchCriteriaWindow.setPreauthDto(preauthDto);
				  nomineeDetailsTable.setPolicySource(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null || SHAConstants.PREMIA_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()) ? SHAConstants.PREMIA_POLICY : SHAConstants.BANCS_POLICY);
				  nomineeDetailsTable.setIfscView(viewSearchCriteriaWindow);
				  nomineeDetailsTable.setCitySearchCriteriaWindow(citySearchCriteriaWindow);
				}
						
				//documentDetailsPageLayout.addComponent(nomineeDetailsTable);
				nomineeLegalLayout.addComponent(nomineeDetailsTable);
					
				boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
							
				//legalHeirLayout = new VerticalLayout();
				
				legalHeirDetails = legalHeirObj.get();
				
				relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				Map<String,Object> refData = new HashMap<String, Object>();
				relationshipContainer.addAll(bean.getPreauthDTO().getLegalHeirDto().getRelationshipContainer());
				refData.put("relationship", relationshipContainer);
				legalHeirDetails.setReferenceData(refData);
				legalHeirDetails.init(bean.getPreauthDTO());
				legalHeirDetails.setPresenterString(SHAConstants.CREATE_ROD);
				//legalHeirLayout.addComponent(legalHeirDetails);
				//documentDetailsPageLayout.addComponent(legalHeirLayout);
				nomineeLegalLayout.addComponent(legalHeirDetails);

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
					if(btnAccPrefSearch != null) 
						btnAccPrefSearch.setEnabled(false);
					optPaymentMode.setEnabled(false);
					btnCitySearch.setEnabled(false);
					btnIFCSSearch.setEnabled(false);
					
					if(txtAccntNo != null)
						txtAccntNo.setRequired(false);
				}
				
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

							setRequiredAndValidation(deathDate);
							setRequiredAndValidation(txtReasonForDeath);

							mandatoryFields.add(deathDate);
							mandatoryFields.add(txtReasonForDeath);

							detailsLayout1.addComponent(deathDate);
							detailsLayout1.addComponent(txtReasonForDeath);
								
							SelectValue selected = cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
							
							if(selected != null
									&& selected.getId() != null		
									&& selected.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
									&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
								buildNomineeLayout();
							}
							//set nominee payee name list for deceased CR2019141 -  GALAXYMAIN-12655
							BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);
						    getValuesForNameDropDown(bean);
						} else {
							if(nomineeDetailsTable != null) { 
								//documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
								nomineeLegalLayout.removeComponent(nomineeDetailsTable);
							}
							if(legalHeirDetails != null) {
								//documentDetailsPageLayout.removeComponent(legalHeirLayout);
								nomineeLegalLayout.removeComponent(legalHeirDetails);
							}
							removePatientStatusGeneratedFields();
							
							if(null != this.bean.getClaimDTO() && ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
									("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
							 {
								cmbPayeeName.setReadOnly(false);
								for(int i = 0 ; i<payeeNameList.size() ; i++)
							 	{
									if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
									{
										//this.cmbPayeeName.setReadOnly(false);
										this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
										//this.cmbPayeeName.setReadOnly(true);
										//this.txtReasonForChange.setEnabled(false);
									}
							 	}	
								cmbPayeeName.setEnabled(true);

								if(null != bean.getNewIntimationDTO().getPolicy() && 
										((ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
										|| ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) 
												|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) )
										&& bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
										 || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))){
									this.cmbPayeeName.setReadOnly(true);
								}
							 }	
							
							/*txtLegalHeirName.setVisible(true);*/
							
							if(paymentDetailsLayout != null) {
								cmbPayeeName.setReadOnly(false);
								cmbPayeeName.setEnabled(true);
								optPaymentMode.setEnabled(true);
								btnCitySearch.setEnabled(true);
								if(bean.getNewIntimationDTO().getPolicy().getPolicySource() == null 
										|| (bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
												&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource()))) {
									btnIFCSSearch.setEnabled(true);
								}
								/*txtLegalHeirName.setVisible(true);*/
								if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null 
										&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					 					&& ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())
					 					&& (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())){
									
									btnAccPrefSearch.setEnabled(false);
									txtAccntNo.setEnabled(false);
								}
								else {
									if(btnAccPrefSearch != null)
										btnAccPrefSearch.setEnabled(true);
								}
							}
							
						    getValuesForNameDropDown(bean);
						}

					} else {
						removePatientStatusGeneratedFields();
						cmbPayeeName.setReadOnly(true);
						cmbPayeeName.setEnabled(true);
					}

				}	
			  
				private void removePatientStatusGeneratedFields() {  
					if (deathDate != null && txtReasonForDeath != null) {
						unbindField(deathDate);
						unbindField(txtReasonForDeath);
						mandatoryFields.remove(deathDate);
						mandatoryFields.remove(txtReasonForDeath);
						detailsLayout1.removeComponent(deathDate);
						detailsLayout1.removeComponent(txtReasonForDeath);
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
							if(null != enteredDate && null != dateOfAdmission && null != dateOfAdmission.getValue()) {
								
								StringBuffer errMsg = new StringBuffer("");
								if(enteredDate.before(dateOfAdmission.getValue()))
								{
									errMsg.append("Please select valid death date. Death date is not in range between admission date and Discharge date.<br>");
									event.getProperty().setValue(null);
								}else if(enteredDate != null && dateOfDischarge != null && dateOfDischarge.getValue() != null){
								if(enteredDate.after(dateOfDischarge.getValue())){
									errMsg.append("Please select valid death date. Death date is not in range between admission date and Discharge date.<br>");
									event.getProperty().setValue(null);
								}
								else if(enteredDate.after(currentSystemDate)){
									errMsg.append("Date of Death should not be greater than current date.<br>");
									event.getProperty().setValue(null);
								}
							}else if(enteredDate.after(currentSystemDate)){
								errMsg.append("Date of Death should not be greater than current date.<br>");
								event.getProperty().setValue(null);
							}
							
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							
							if(!errMsg.toString().isEmpty())
								GalaxyAlertBox.createErrorBox(errMsg.toString(), buttonsNamewithType);
							}
						}
					});
				}

				public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {

					SelectValue selected = cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;

					SelectValue patientStatus = cmbPatientStatus.getValue() != null ? (SelectValue)cmbPatientStatus.getValue() : null;
					
					if(selected != null
							&& selected.getId() != null		
							&& selected.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
							&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
							&& patientStatus != null
							&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId())
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))) {
						nomineeDetailsTable.getBankDetailsTableObj().setUpAddBankIFSCDetails(dto);
						
					}else{
						bankDetailsTableObj.setUpAddBankIFSCDetails(dto);
					}
					
//					bankDetailsTableObj.setUpAddBankIFSCDetails(dto);

				}
				
				public void setUpBankDetails(ReceiptOfDocumentsDTO dto) {
					SelectValue selected = cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
					SelectValue patientStatus = cmbPatientStatus.getValue() != null ? (SelectValue)cmbPatientStatus.getValue() : null;
					if(selected != null
							&& selected.getId() != null		
							&& selected.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
							&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
							&& patientStatus != null
							&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId())
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))) {
						nomineeDetailsTable.getBankDetailsTableObj().getBankDetails();
						
					}else{
						bankDetailsTableObj.getBankDetails();
					}
				}
				
				public Map<Boolean,StringBuffer> validateNomineeLegalHeirTableValues(Boolean hasError, StringBuffer eMsg){
					
					Map<Boolean,StringBuffer> resultMap = new WeakHashMap<Boolean, StringBuffer>();

					SelectValue patientStatusvalue = cmbPatientStatus.getValue() != null ? (SelectValue) cmbPatientStatus.getValue() : null;
					
					if((SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
							|| (patientStatusvalue != null
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatusvalue.getId())
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatusvalue.getId()))))
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
//											nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
											nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
										    selectCnt++;	
										}
										/*if(nomineeDetailsDto.getPreference() == null || nomineeDetailsDto.getPreference().isEmpty()) {
											eMsg.append("Please Select Account Preference for Nominee</br>");
											nomineeAccPrefered = false;
											break;
										}
										else {
											nomineeAccPrefered = true;
										}*/
									}

									/**
									Acc. Preference disable for Nominee as per Mr. Satish Sir on 14-12-2019
									**/
									nomineeAccPrefered = true;  
									
									bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
									if(selectCnt > 0 && nomineeAccPrefered){
										bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
										bean.getNewIntimationDTO().setNomineeAddr(null);
										
									}
									else{
										bean.getNewIntimationDTO().setNomineeName(null);
										
										eMsg.append("Please Select Nominee<br>");
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
										
										bean.getPreauthDTO().setLegalHeirDTOList(legalHeirList);
									}
									
								}
								else{
									bean.getPreauthDTO().setLegalHeirDTOList(null);
									hasError = true;
									eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
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
												eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %,Account Preference)");
											}
										 }
									}
								 }
									
							}							
		
					}
					
					resultMap.put(hasError, eMsg);
					return resultMap;
				}
				
				 public BlurListener gethospitalCash() {
						
						BlurListener listener = new BlurListener() {
							
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void blur(BlurEvent event) {
								TextField property = (TextField) event.getComponent();
								if(null != property)
								{
									if(!("").equalsIgnoreCase(property.getValue())){
									
										/*if(null != bean.getDocumentDetails().getOtherBenefitsAmountDocRec() && null != txtOtherBenefitsClaimedAmnt && null != txtOtherBenefitsClaimedAmnt.getValue() &&
												!(txtOtherBenefitsClaimedAmnt.getValue().equalsIgnoreCase(String.valueOf(bean.getDocumentDetails().getOtherBenefitsAmountDocRec().intValue())))){
										//	fireViewEvent(CreateRODWizardPresenter.CLAIMED_AMNT_ALERT,property);
											claimedAmntValidation(property);
											
										}*/
										optDocumentVerified.setEnabled(true);
										optDocumentVerified.setValue(null);
										bean.getDocumentDetails().setHospitalCashClaimedAmnt(txtHospitalCashClaimedAmnt.getValue());
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
 private void getValuesForNameDropDown(ReceiptOfDocumentsDTO rodDTO)
 {

		Policy policy = policyService.getPolicy(bean.getClaimDTO()
				.getNewIntimationDto().getPolicy().getPolicyNumber());
		if (null != policy) {
			SelectValue selectValue = null;

				List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());

				if (null != policy.getProduct().getKey()
						&& (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()) || ReferenceTable.getRevisedCriticareProducts().containsKey(policy.getProduct().getKey())
								 || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))) {

					pNomineeDetails = intimationService
							.getPolicyNomineeListForInsured(bean
									.getClaimDTO().getNewIntimationDto()
									.getInsuredPatient().getKey());
				}

				for (PolicyNominee pNominee : pNomineeDetails) {
					selectValue = new SelectValue();
					selectValue.setId(pNominee.getKey());
					selectValue.setValue(pNominee.getNomineeName());
					if (this.cmbPatientStatus.getValue() !=null && this.cmbPatientStatus.getValue().toString().toLowerCase().contains("deceased") &&
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
 
 private void addgrievanceRepresentationListener(){
	 grievanceRepresentationOpt .addValueChangeListener(new Property.ValueChangeListener() {
		 private static final long serialVersionUID = 1L;

		 @Override
		 public void valueChange(ValueChangeEvent event) {
			 if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
			 {
				 Boolean check = (Boolean)event.getProperty().getValue();
				 if(check){
					 boolean alertMessageBox = true;
					 if(cmbReasonForReconsideration !=null && cmbReasonForReconsideration.getValue() !=null){
						 SelectValue reason = (SelectValue)cmbReasonForReconsideration.getValue();
						 if(reason.getId() !=null && ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(reason.getId())){
							 alertMessageBox = false;
						 }
					 }
					 if(alertMessageBox){
						 final MessageBox msgBox = MessageBox.createWarning()
									.withCaptionCust("Information") .withHtmlMessage("Grievance representation is selected for the ROD. Please uncheck, if Grievance representation is not required")
									.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
									.open();
					 }				
				 }			 
			 }
		 }
	 });
 }
 
 
}