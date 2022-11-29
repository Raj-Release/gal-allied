package com.shaic.claim.viewEarlierRodDetails;


import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.thirdparty.common.css.compiler.ast.ErrorManager;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.CashLessTableDTO;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuService;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Table.FieldVisitDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDiagnosisTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewImplantDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewSectionDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.dto.FieldVisitDetailsTableDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewSectionDetailsTableDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimSection;
import com.shaic.domain.ClaimSectionCover;
import com.shaic.domain.ClaimSectionSubCover;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ViewTmpPreauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAInvestigationReviewRemarksTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@UIScoped
public class RodIntimationAndViewDetailsUI extends AbstractMVPView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private TextField txtIntimationNo;
    
    private TextField txtIntimationTime;
    
    private TextField txtCpuCode;
    
    private TextField cmbModeOfIntimation;
    
    private TextField txtIntimatedBy;
    
    private TextField cmbInsuredPatientName;
    
    private TextField txtPolicyNo;
    
    private TextField txtIssuingOffice;
    
    private TextField txtProductName;
    
    private TextField txtInsuredName;
    
    private TextField txtInpatientNumber;
    
    private TextField txtLateIntimation;
    
    private DateField admissionDate;
    
    private TextField txtAdmissionType;
    
    private TextArea txtReasonForAdmission;
   
    private TextField txtComments;
    
    private TextField txtState;
    
    private TextField txtCity;
    
    private TextField txtArea;
    
    private TextField txtSmCode;
    
    private TextField txtSmName;
    
    private TextField txtBrokerCode;
    
    private TextField txtBrokerName;
    
    private TextField txtHospitalName;
    
    private TextField txtName;
    
    private TextField txtRelationship;
    
    private TextArea txtAddress;
    
    private TextField txtHealthCardNo;
    
    private TextField txtEmpId;
        
    private TextField txtHospitalType;
    
    private TextField txtHospitalCodeInternal;
    
    private TextField txtHospitalCodeIrda;
    
    private VerticalLayout mainVerticalLayout;
    
    //Registration Details UI
    
    private TextField txtClaimNo;
    
    private TextField txtRegistrationStatus;
    
    private TextField txtCurrency;
    
    private TextField txtProvisionAmt;
    
    private TextField txtClaimType;
    
    private TextField txtRegistrationRemarks;
    
    //Cashless Details UI
    
    private TextField txtCashlessStatus;
    
    private TextField txtTotalPreauthAmt;
    
    private ComboBox cmbCovid19Vairant;
    
    private ComboBox cmbCocktailDrug;
    
    private HorizontalLayout covidVariantHLayout;
    
    //Payement details UI
    
    private TextField txtTypeOfPayment;
    
    private TextField txtBankName;
    
    private TextField txtUTRNo;
    
    private TextField txtAccountName;
    
    private TextField txtChequeNo;
    
    private TextField txtBranchName;
    
    private TextField txtNeftDate;
    
    private TextField txtChequeDate;
    
    private OptionGroup acctorDeath;
    
    private TextField categoryType;
	
	private TextField parentName;
	
	private TextField age;
	
	private TextField patientName;
	
	private TextField dob;
	
	private TextField txtHospitalflag;
	
	private TextField dataCorrectionDoneByTeam;
	
	private CheckBox chkEmergency;
    
    @Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
    
    @Inject
    private Instance<ViewSectionDetailsTable> sectionDetailsTableInstance;
    
    private ViewSectionDetailsTable sectionDetailsTableObj;
    
    @Inject
    private Instance<FieldVisitDetailsTable> fieldVisitDetailsTableInstance;
    
    private FieldVisitDetailsTable fieldVisitDetailsTableObj;
    
  
    private BeanFieldGroup<ViewClaimStatusDTO> binder;
    
    private BeanFieldGroup<ClaimStatusRegistrationDTO> registrationBinder;
    
    private BeanFieldGroup<CashLessTableDTO> cashLessBinder;
    
    private BeanFieldGroup<PreauthDataExtaractionDTO> covidBinder;
    
    @Inject
	private ViewDetails viewDetails;
    
    @Inject
	private Instance<ViewPreviousPreauthSummaryTable> preauthPreviousDetailsPage;
	
	private ViewPreviousPreauthSummaryTable preauthPreviousDetailsPageObj;
	
	@Inject
	private ReceiptOfDocumentTable receiptOfDocumentTable;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@Inject
	private BillingProcessingTable billingProcessingTable;
	
	@Inject 
	private ViewDiagnosisTable diagnosisDetailsTable;
	
	@Inject
	private FinancialApprovalTable financialApprovalTable;
	
	@Inject
    private ViewPaymentTable paymentTableObj;
    
    @Inject
    private CashlessTable cashlessTable;
    
    @EJB
    private PreauthService preauthService;
    
    @EJB
	private PreviousPreAuthService previousPreAuthService;
    
    @Inject
	private ViewPolicyDetails viewPolicyDetail;
    
    @EJB
    private MasterService masterService;
    
    @EJB
    private DiagnosisService diagnosisService;
    
    @EJB
    private IntimationService intimationService;
    
    @EJB
    private PaymentProcessCpuService paymentProcessCpuService;
    
    @EJB
	private DBCalculationService dbCalculationService;

    @EJB
    private FieldVisitRequestService fieldVisitRequestService;
    
    @EJB
    private AcknowledgementDocumentsReceivedService reimbursementService;
    
    @EJB
    private ReimbursementService reimbService;
    
    @EJB
    private CreateRODService billDetailsService;
    
    @EJB
    private PolicyService policyService;
    
    @EJB
    private ClaimService claimService;
    
    @EJB
	private HospitalService hospitalService;
    
    private Button btnViewPolicyDetails;
    
    private Button btnViewDocument;
    
    private Button btnViewTrails;
    
    private Button btnViewLinkedPolicyDetails;
    
    private TextField txtimplantApplicable;
    
    @Inject
  	private Instance<ViewImplantDetailsTable> ImplantDetailsTableinstance;
  	
  	private ViewImplantDetailsTable implantDetailsTableObj;
    
    ////private static Window popup;


    private ViewClaimStatusDTO bean;
    
    private Long rodKey;
    
    private TextArea txtSuspiciousReason;
    
    /*@Inject
	private Instance<PAInvestigationReviewRemarksTable> invsReviewRemarksTableInstance;
	
    private PAInvestigationReviewRemarksTable invsReviewRemarksTableObj;*/
    
    public void init(ViewClaimStatusDTO bean,Long rodKey){
    	
    	this.bean = bean;
    	this.rodKey = rodKey;
    	
    	if(bean!=null && bean.getDateOfIntimation()!=null){
			Date tempDate = SHAUtils.formatTimestamp(bean.getDateOfIntimation());
			bean.setDateOfIntimation(SHAUtils.formatDate(tempDate));
		}
		
		if(bean!=null && bean.getAdmissionDate()!=null){
			bean.setAdmissionDate(bean.getAdmissionDate());
		}
 
		HorizontalLayout ZohoGrievanceFlag = SHAUtils.zohoGrievanceFlag(bean.getPreauthDTO());
		
    	this.binder = new BeanFieldGroup<ViewClaimStatusDTO>(ViewClaimStatusDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		btnViewPolicyDetails = new Button("View Policy");
		btnViewPolicyDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnViewPolicyDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		btnViewLinkedPolicyDetails = new Button("View Linked Policy Details");
		btnViewLinkedPolicyDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnViewLinkedPolicyDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		btnViewDocument = new Button("View Document");
		btnViewDocument.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnViewDocument.addStyleName(ValoTheme.BUTTON_LINK);
		
		btnViewTrails = new Button("View Trails");
		btnViewTrails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnViewTrails.addStyleName(ValoTheme.BUTTON_LINK);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		
		viewDetails.initView(this.bean.getIntimationId(), (this.rodKey != null && this.rodKey.intValue() != 0 ? this.rodKey : 0l), ViewLevels.CLAIM_STATUS,"Process Claim Billing");
		
		viewDetailsForm.addComponent(viewDetails);
		
		HorizontalLayout mainHor =null;
		if(bean != null && bean.getProductName() != null && (bean.getProductName().equalsIgnoreCase(SHAConstants.GMC_GROUP_HEALTH_INSURANCE) || bean.getProductName().equalsIgnoreCase(SHAConstants.GMC_STAR_GROUP_HEALTH_INSURANCE))) {
				mainHor = new HorizontalLayout(viewDetailsForm,ZohoGrievanceFlag,btnViewPolicyDetails,btnViewLinkedPolicyDetails,btnViewDocument,btnViewTrails);
	    } else {
	    	mainHor = new HorizontalLayout(viewDetailsForm,ZohoGrievanceFlag,btnViewPolicyDetails,btnViewDocument,btnViewTrails);
	    }
		mainHor.setSpacing(true);
		
		txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
		txtIntimationTime = (TextField) binder.buildAndBind("Date & Time of Intimation","dateOfIntimation",TextField.class);
		txtCpuCode = (TextField) binder.buildAndBind("CPU Code", "cpuId", TextField.class);
		txtCpuCode.setNullRepresentation("");
		cmbModeOfIntimation = (TextField) binder.buildAndBind("Mode of Intimation", "intimationMode", TextField.class);
		txtIntimatedBy = (TextField) binder.buildAndBind("Intimated By", "intimatedBy", TextField.class);
		txtIntimatedBy.setNullRepresentation("");
		txtIntimatedBy.setRequired(false);
		txtIntimatedBy.setValidationVisible(false);
		cmbInsuredPatientName = (TextField) binder.buildAndBind("Insured Patient Name", "insuredPatientName", TextField.class);
		txtHealthCardNo = (TextField) binder.buildAndBind("Health Card No", "healthCardNo", TextField.class);
		
		if(this.bean.isJioPolicy()){
			txtEmpId = (TextField) binder.buildAndBind("JACID", "employeeCode", TextField.class);
		}	
				
		txtName = (TextField) binder.buildAndBind("Name ", "patientNotCoveredName", TextField.class);
		txtRelationship = (TextField) binder.buildAndBind("Relationship ", "relationshipWithInsuredId", TextField.class);
		admissionDate =(DateField) binder.buildAndBind("Admission Date","admissionDate",DateField.class);
		admissionDate.setRequired(false);
		admissionDate.setValidationVisible(false);
		admissionDate.setEnabled(false);
		txtAdmissionType = (TextField) binder.buildAndBind("Admission Type", "admissionType", TextField.class);
		txtAdmissionType.setRequired(false);
		txtAdmissionType.setValidationVisible(false);
		txtAdmissionType.setNullRepresentation("");
		txtReasonForAdmission = (TextArea) binder.buildAndBind("Reason for Admission","reasonForAdmission",TextArea.class);
		txtReasonForAdmission.setRequired(false);
		txtReasonForAdmission.setValidationVisible(false);
		txtInpatientNumber = (TextField) binder.buildAndBind("Inpatient Number","inpatientNumber",TextField.class);
		txtInpatientNumber.setValidationVisible(false);
		txtLateIntimation = (TextField) binder.buildAndBind("Reason for late Intimation","lateIntimationReason",TextField.class);
		txtLateIntimation.setValidationVisible(false);
		
		categoryType = new TextField("Category Type");
		parentName = new TextField("Parent Name");
		age = new TextField("Age");
		patientName = new TextField("Patient Name");
		dob = new TextField("Date of Birth");
		
		if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
		{
			categoryType.setVisible(true);
			parentName.setVisible(true);
			age.setVisible(true);
			patientName.setVisible(true);
			dob.setVisible(true);
			
		}
		else
		{
			categoryType.setVisible(false);
			parentName.setVisible(false);
			age.setVisible(false);
			patientName.setVisible(false);
			dob.setVisible(false);
		}
	    
		/**	
		 * added new field in View Claim Status according to GLX2020055
		 */
		dataCorrectionDoneByTeam =new TextField("Data Validation");
		Claim claimByKey1 = claimService.getClaimforIntimation(bean.getIntimationKey());
		if(claimByKey1 != null && claimByKey1.getCoadingFlag()!= null && claimByKey1.getCoadingFlag().toString().equalsIgnoreCase("Y")){
				dataCorrectionDoneByTeam.setValue("Yes");
			}else{
				dataCorrectionDoneByTeam.setValue("No");
			}
		
		
		//GLX2020132
		chkEmergency=new CheckBox("Emergency");
		//chkEmergency.setValue(true);
		chkEmergency.setReadOnly(true);
		FormLayout chkEmergencyLayout = new FormLayout(chkEmergency);
		
		List<Reimbursement> reimbursementDetails = reimbService
				.getRembursementDetails(this.bean.getClaimKey());
		if(reimbursementDetails != null && !reimbursementDetails.isEmpty()){
		for (Reimbursement reimbursement : reimbursementDetails) {
		 if(reimbursement.getEmergencyFlag() != null && reimbursement.getEmergencyFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 chkEmergency.setReadOnly(false);
			 chkEmergency.setValue(true);
			 chkEmergency.setReadOnly(true);
			 break;
		 }else{
			 chkEmergency.setReadOnly(false);
			 chkEmergency.setValue(false);
			chkEmergency.setReadOnly(true);
		 }
		}
		}
		
		
	    VerticalLayout vLayout = new VerticalLayout();
		
	    FormLayout firstForm = new FormLayout(txtIntimationNo,txtIntimationTime,txtCpuCode,cmbModeOfIntimation,txtIntimatedBy,cmbInsuredPatientName,txtHealthCardNo);
	    
	    if(this.bean.isJioPolicy()){
	    	firstForm.addComponent(txtEmpId);
	    }
	    firstForm.addComponents(txtName,txtRelationship,admissionDate,txtAdmissionType,txtInpatientNumber,txtLateIntimation,txtReasonForAdmission,categoryType,parentName,age,patientName,dob,dataCorrectionDoneByTeam,chkEmergencyLayout);
	    firstForm.setSpacing(true);
	    setReadOnly(firstForm, true);
	    
	    txtPolicyNo = (TextField) binder.buildAndBind("Policy No","policyNumber",TextField.class);
	    txtIssuingOffice = (TextField) binder.buildAndBind("Policy Issuing Office","policyIssuing",TextField.class);
	    txtProductName = (TextField) binder.buildAndBind("Product Name","productName",TextField.class);
	    txtInsuredName = (TextField) binder.buildAndBind("Proposer Name","patientName",TextField.class);  
	    txtState = (TextField) binder.buildAndBind("State", "state", TextField.class);
	    txtState.setNullRepresentation("");
	    txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
	    txtCity.setNullRepresentation("");
	    txtArea = (TextField) binder.buildAndBind("Area", "area", TextField.class);
	    txtArea.setNullRepresentation("");
	    txtHospitalName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
	    txtAddress = (TextArea) binder.buildAndBind("Address","hospitalAddress",TextArea.class);
	    txtHospitalType = (TextField) binder.buildAndBind("Hospital Type", "hospitalTypeValue", TextField.class);
	    txtHospitalCodeInternal = (TextField) binder.buildAndBind("Hospital Code (Internal)","hospitalInternalCode",TextField.class);
	    txtHospitalCodeIrda = (TextField) binder.buildAndBind("Hospital Code (IRDA)","hospitalIrdaCode",TextField.class);
	    txtComments = (TextField) binder.buildAndBind("Comments","comments",TextField.class);
	    txtSmCode = (TextField) binder.buildAndBind("SM Code","smCode",TextField.class);
	    txtSmName = (TextField) binder.buildAndBind("SM Name","smName",TextField.class);
	    txtBrokerCode = (TextField) binder.buildAndBind("Agent / Broker Code","agentBrokerCode",TextField.class);
	    txtBrokerName = (TextField) binder.buildAndBind("Agent / Broker Name","agentBrokerName",TextField.class);
	    
	    acctorDeath = new OptionGroup("Accident / Death");
	    acctorDeath.addItem(SHAConstants.ACCIDENT);
	    acctorDeath.addItem(SHAConstants.DEATH);
	    acctorDeath.addStyleName("horizontal");
	    acctorDeath.setEnabled(false);
	    
	    txtHospitalflag = (TextField) binder.buildAndBind("Hospital Flag","hospitalFlag",TextField.class);
	    txtHospitalflag.addStyleName("redfont");

	    
	    txtSuspiciousReason = (TextArea) binder.buildAndBind("Reason","suspiciousReason",TextArea.class);
	    txtSuspiciousReason.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	    txtSuspiciousReason.addStyleName("red");
	    //txtSuspiciousReason.setWidth(140, Unit.PIXELS);
	    
	    txtSuspiciousReason.setIcon(new ThemeResource("images/Suspecious.png"));
	    
	    
	   // if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
	    if(null !=  bean.getClaimProcessType() && (SHAConstants.PA_TYPE).equalsIgnoreCase(bean.getClaimProcessType()))
	    {
	    	if(bean.getClaimStatusRegistrionDetails().getIncidence() != null)
	    	{
		    	if(bean.getClaimStatusRegistrionDetails().getIncidence().equalsIgnoreCase(SHAConstants.ACCIDENT_FLAG))
		    	{
		    		acctorDeath.setValue(SHAConstants.ACCIDENT);
		    	}
		    	else if(bean.getClaimStatusRegistrionDetails().getIncidence().equalsIgnoreCase(SHAConstants.DEATH_FLAG))
		    	{
		    		acctorDeath.setValue(SHAConstants.DEATH);
		    	}
		    	acctorDeath.setVisible(true);
	    	}
	    }
	    else
	    {
	    	acctorDeath.setVisible(false);
	    }
	    
	    if(null != bean.getHospitalCategory()){	    	
			 String hospitalType	= bean.getHospitalTypeValue() + "("+bean.getHospitalCategory()+" Category)";
			 txtHospitalType.setValue(hospitalType);
		}
	    
	    FormLayout secondForm = new FormLayout(txtPolicyNo,acctorDeath,txtIssuingOffice,txtProductName,txtInsuredName,txtState,txtCity,txtArea,txtHospitalName,txtHospitalflag,txtSuspiciousReason,txtAddress,
	    		txtHospitalType,txtHospitalCodeInternal,txtHospitalCodeIrda,txtComments,txtSmCode,txtSmName,txtBrokerCode,txtBrokerName);
	    
	    setReadOnly(secondForm,true);
	
	    HorizontalLayout intimationHor = new HorizontalLayout(firstForm,secondForm);
	    intimationHor.setComponentAlignment(firstForm, Alignment.TOP_CENTER);
	    intimationHor.setComponentAlignment(secondForm, Alignment.MIDDLE_RIGHT);
	    intimationHor.setWidth("110%");
	    
	    intimationHor.setMargin(false);
	    intimationHor.setSpacing(true);
	    intimationHor.setSizeFull();
	    intimationHor.addStyleName("gridBorder");
	    
	    vLayout.addComponent(intimationHor);
	    
	    if(bean.getPreauthDTO() != null)
	    {
	    Label agentBranch = new Label();
	    
	    HorizontalLayout formLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean.getPreauthDTO());
		HorizontalLayout hoapitalScore = SHAUtils.hospitalScoreForView(bean.getPreauthDTO(),hospitalService);
		HorizontalLayout agentBranchHLayout = SHAUtils.icrAgentBranch(bean.getPreauthDTO());
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		if(bean.getPreauthDTO().getNewIntimationDTO() !=null && bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			agentBranchHLayout.addComponent(buyBackPedHLayout);
		}
		if(bean.getPreauthDTO().getNewIntimationDTO() !=null && bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			agentBranchHLayout.addComponent(buyBackPedHLayout);
		}
		

		Button btnInsuredChannelName = new Button("Insured/Channel Name");
		btnInsuredChannelName.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnInsuredChannelName.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				HorizontalLayout insuredChannelName= SHAUtils.getInsuredChannedName(bean.getPreauthDTO());
				/*bean.setIsInsuredChannedNameClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
				
			
			}
		});		
		agentBranchHLayout.addComponent(btnInsuredChannelName);
		
		VerticalLayout agentBranchVLayout=new VerticalLayout(agentBranch,agentBranchHLayout);
	    HorizontalLayout icrAgentBranchLayout = new HorizontalLayout(agentBranchVLayout);
	    // GXL2021044
     	HorizontalLayout HsTrafficIcon = SHAUtils.HsTraficImages(bean.getPreauthDTO());
     	
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout,hoapitalScore,icrAgentBranchLayout,hopitalFlag,HsTrafficIcon);
		crmLayout.setSpacing(true);
		//crmLayout.setWidth("100%");
		crmLayout.setHeight("25px");
		
		vLayout.addComponentAsFirst(crmLayout);
		
	    }
		
	    Label vip = new Label();
		vip.setDescription("VIP Customer");
    	vip.setIcon(new ThemeResource("images/VIP_Image.png"));
	    
	    Claim claimByKey = claimService.getClaimforIntimation(bean.getIntimationKey());
	    
	    if (claimByKey != null && claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
			
			FormLayout vipLayout = new FormLayout(vip);
			vipLayout.setHeight("70px");
			vipLayout.setWidth("50px");
		    
		    HorizontalLayout hLayout = new HorizontalLayout(vipLayout);
		    vLayout.addComponent(hLayout);
	    }
	    
	    Panel mainPanel = new Panel(vLayout);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Intimation Details");
	    
	    sectionDetailsTableObj = sectionDetailsTableInstance.get();
	    sectionDetailsTableObj.init("", false, false);
	    sectionDetailsTableObj.setCaption("Section Details");
	    
	    setSectionDetailsTable();
	    
	    
	    fieldVisitDetailsTableObj = fieldVisitDetailsTableInstance.get();
	    fieldVisitDetailsTableObj.init("", false, false);
	    fieldVisitDetailsTableObj.setCaption("Field Visit Details");
	    
	    setFieldVisitDetailsTable();
	    
	    Panel registrationPanel = registrionDetailsPanel();
	    
	    Panel cashlessPanel = cashlessDetailsPanel();
	    
	    Panel  covidPanel = covidDetailsPanel();
	    
	    Panel implantPanel = getimplantDetailsPanel();
	    
	    this.receiptOfDocumentTable.init("", false, false);
//	    List<ViewDocumentDetailsDTO> tableValues = this.bean.getReceiptOfDocumentValues();
//	    for (ViewDocumentDetailsDTO documentDetailsDTO : tableValues) {
//			this.receiptOfDocumentTable.addBeanToList(documentDetailsDTO);
//		}
//	    
	    if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
	    {
	    	this.receiptOfDocumentTable.setPAColumnsForROD();
	    }
	    
	    setReceiptOfDocumentDetailsTable();
	    
	    Panel receiptOfCashelessPanel = new Panel(this.receiptOfDocumentTable);
	    receiptOfCashelessPanel.addStyleName("girdBorder");
	    receiptOfCashelessPanel.setCaption("Receipt of Documents and Medical Processing");
	    
	    this.billingProcessingTable.init("", false, false);
	    
	    Panel billingPanel = new Panel(this.billingProcessingTable);
	    billingPanel.addStyleName("girdBorder");
	    billingPanel.setCaption("Billing Processing");
	    
	    this.financialApprovalTable.init("", false, false);
	    
	    Panel financialPanel = new Panel(this.financialApprovalTable);
	    financialPanel.addStyleName("girdBorder");
	    financialPanel.setCaption("Financial Approval");
	    
	    
	    if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
	    {
	    	this.billingProcessingTable.setPAColumnsForBilling();
	    	this.financialApprovalTable.setPAColumsnForFinancial();
	    }
	    
	    setBillProcessTableValues();
	    
	    txtTypeOfPayment = (TextField) binder.buildAndBind("Type of Payment","typeOfPayment",TextField.class);
	    txtBankName = (TextField) binder.buildAndBind("Bank Name","bankName",TextField.class);
	    txtUTRNo = (TextField) binder.buildAndBind("UTR No","utrNumber",TextField.class);
	    txtAccountName = (TextField) binder.buildAndBind("Account Number","accountName",TextField.class);  
	    txtChequeNo = (TextField) binder.buildAndBind("Cheque / DD No", "chequeNumber", TextField.class);
	  //  txtBranchName.setNullRepresentation("");
	    txtBranchName = (TextField) binder.buildAndBind("Branch Name", "branchName", TextField.class);
	  //  txtChequeDate.setNullRepresentation("");
	    txtNeftDate = (TextField) binder.buildAndBind("NEFT Date", "neftDate", TextField.class);
	   // txtArea.setNullRepresentation("");
	    txtChequeDate = (TextField) binder.buildAndBind("Cheque / DD Date","chequeDate",TextField.class);
	    
	    FormLayout payementForm1 = new FormLayout(txtTypeOfPayment,txtBankName,txtUTRNo,txtAccountName,txtChequeNo);
	    payementForm1.setSpacing(true);
	    setReadOnly(payementForm1,true);
	    
	    FormLayout payementForm2 = new FormLayout(txtBranchName,txtNeftDate,txtChequeDate);
	    payementForm2.setSpacing(true);
	    setReadOnly(payementForm2,true);
	    
	    HorizontalLayout paymentHor = new HorizontalLayout(payementForm1,payementForm2);
	    paymentHor.setSpacing(true);
	    
	    Panel paymentDetailsPanel = new Panel(paymentHor);
	    paymentDetailsPanel.setCaption("Payment Details");
	    
	    VerticalLayout dummyVertical = new VerticalLayout();
	    dummyVertical.setHeight("30px");
	    dummyVertical.setWidth("100%");
	    
	    HorizontalLayout dummyHorizontal = new HorizontalLayout();
	    dummyHorizontal.setHeight("10px");
	    
        paymentTableObj.init("Payment Details", false, false);
	    
        if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
	    {
	    	this.paymentTableObj.setPAColumsnForPayment();
	    }
        
	    setPaymentDetailsTable(this.bean.getClaimKey());
	    
	    /*List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbService.getInvsReviewRemarksDetailsByClaimKey(this.bean.getClaimKey());
		invsReviewRemarksTableObj = invsReviewRemarksTableInstance.get();
		invsReviewRemarksTableObj.init();
		invsReviewRemarksTableObj.setTableList(invsReviewRemarksList);
		
		HorizontalLayout specialistHLayout = new HorizontalLayout(invsReviewRemarksTableObj);
		specialistHLayout.setWidth("100%");
		specialistHLayout.setSpacing(true);*/

	    mainVerticalLayout = new VerticalLayout(dummyHorizontal,mainHor,mainPanel,registrationPanel,sectionDetailsTableObj,fieldVisitDetailsTableObj,cashlessPanel,covidPanel,implantPanel,dummyVertical,receiptOfCashelessPanel,billingPanel,financialPanel/*,paymentDetailsPanel*/,paymentTableObj);
	    mainVerticalLayout.setSpacing(true);
	    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
	    mainVerticalLayout.setComponentAlignment(mainHor, Alignment.TOP_RIGHT);
	    mainVerticalLayout.setMargin(true);
	    
	    addListener();
	    
	    setCompositionRoot(mainVerticalLayout);
    }
    
    private Panel registrionDetailsPanel(){
    	
    	this.registrationBinder = new BeanFieldGroup<ClaimStatusRegistrationDTO>(ClaimStatusRegistrationDTO.class);
		this.registrationBinder.setItemDataSource(this.bean.getClaimStatusRegistrionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		txtClaimNo = (TextField) registrationBinder.buildAndBind("Claim No","claimNo",TextField.class);
		txtClaimNo.setNullRepresentation("");
		txtRegistrationStatus = (TextField) registrationBinder.buildAndBind("Registration Status","registrationStatus",TextField.class);
		txtRegistrationStatus.setNullRepresentation("");
		txtCurrency = (TextField) registrationBinder.buildAndBind("Currency","currency",TextField.class);
		txtCurrency.setNullRepresentation("");
		txtProvisionAmt = (TextField) registrationBinder.buildAndBind("Provision Amount","provisionAmt",TextField.class);
		txtProvisionAmt.setNullRepresentation("");
		txtClaimType = (TextField) registrationBinder.buildAndBind("Claim Type","claimType",TextField.class);
		txtClaimType.setNullRepresentation("");
		txtRegistrationRemarks = (TextField) registrationBinder.buildAndBind("Remarks","registrationRemarks",TextField.class);
		txtRegistrationRemarks.setNullRepresentation("");
		
		FormLayout firstForm = new FormLayout(txtClaimNo,txtRegistrationStatus,txtCurrency);
		firstForm.setSpacing(true);
		
		setReadOnly(firstForm, true);
		
		FormLayout secondForm = new FormLayout(txtProvisionAmt,txtClaimType);
		secondForm.setSpacing(true);
		
		setReadOnly(secondForm, true);
		
		FormLayout thirdForm = new FormLayout(txtRegistrationRemarks);
		setReadOnly(thirdForm, true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm,thirdForm);
		mainHor.setSpacing(true);
		Panel mainPanel = new Panel(mainHor);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Registration Details");
	    return mainPanel;
		
    }
    
    private Panel cashlessDetailsPanel(){
    	
		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.searchFromTmpPreauth(this.bean.getClaimKey());

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

			previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);

		}
		
		List<Long> keys = new ArrayList<Long>();
		if(null != previousPreauthList && !previousPreauthList.isEmpty()){
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				keys.add(previousPreAuthTableDTO.getKey());
			}
		}
		
		ViewTmpPreauth preauth = null;
		Long preauthKey = null;
		if (!keys.isEmpty()) {
			preauthKey = Collections.max(keys);

			preauth = preauthService.getViewTmpPreauthById(preauthKey);
		}
		
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
		List<DiagnosisDetailsTableDTO> tempDiagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
		
		if(preauth != null && this.rodKey == null){
			tempDiagnosisList = diagnosisService.getDiagnosisListFromTmpDiagnosis(preauth.getKey());
		}
		else if(this.rodKey != null){
			tempDiagnosisList = diagnosisService.getDiagnosisListFromTmpDiagnosis(this.rodKey);
		}
		
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : tempDiagnosisList) {
			if(diagnosisDetailsTableDTO.getIcdChapterKey() != null){
				SelectValue icdChapterbyId = masterService.getIcdChapterbyId(diagnosisDetailsTableDTO.getIcdChapterKey());
				if(icdChapterbyId != null){
					diagnosisDetailsTableDTO.setIcdChapterValue(icdChapterbyId.getValue());
				}
			}
			if(diagnosisDetailsTableDTO.getIcdBlockKey() != null) {
				SelectValue icdBlock = masterService.getIcdBlock(diagnosisDetailsTableDTO.getIcdBlockKey());
				if(icdBlock != null){
					diagnosisDetailsTableDTO.setIcdBlockValue(icdBlock.getValue());
				}
			}
			if(diagnosisDetailsTableDTO.getIcdCodeKey() != null) {
				SelectValue icdCode = masterService.getIcdCodeByKey(diagnosisDetailsTableDTO.getIcdCodeKey());
				if(icdCode != null){
					diagnosisDetailsTableDTO.setIcdCodeValue(icdCode.getValue());
				}
			}
			diagnosisList.add(diagnosisDetailsTableDTO);
		}
    	
    	this.cashLessBinder = new BeanFieldGroup<CashLessTableDTO>(CashLessTableDTO.class);
		this.cashLessBinder.setItemDataSource(this.bean.getCashlessTableDetails());
		cashLessBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		txtCashlessStatus = (TextField) cashLessBinder.buildAndBind("Status Of Cashless Claim","cashlessStatus",TextField.class);
		txtCashlessStatus.setNullRepresentation("");
		txtCashlessStatus.setReadOnly(true);
		
		txtTotalPreauthAmt = (TextField) cashLessBinder.buildAndBind("Total Pre-Auth Approved Amt","totalApprovedAmt",TextField.class);
		txtTotalPreauthAmt.setNullRepresentation("");
		txtTotalPreauthAmt.setReadOnly(true);
		
		if(preauth != null){
			txtCashlessStatus.setReadOnly(false);
			
			if(preauth.getTotalApprovalAmount() != null){
				txtTotalPreauthAmt.setReadOnly(false);
				txtTotalPreauthAmt.setValue(preauth.getTotalApprovalAmount().toString());
			}
			
			if(preauth.getStatus() != null){
				txtCashlessStatus.setValue(preauth.getStatus().getProcessValue());
				if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
						|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
						|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
						|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
						|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){
					txtTotalPreauthAmt.setValue("0");
				}
			}
			
			txtCashlessStatus.setReadOnly(true);
			txtTotalPreauthAmt.setReadOnly(true);
		}
		
		FormLayout firstForm = new FormLayout(txtCashlessStatus,txtTotalPreauthAmt);
		firstForm.setSpacing(true);
		
		preauthPreviousDetailsPageObj = preauthPreviousDetailsPage.get();
		preauthPreviousDetailsPageObj.init("", false, false);
		preauthPreviousDetailsPageObj.setTableList(newList);
		preauthPreviousDetailsPageObj.setCaption("Pre-auth Summary");
		   /*int sno = 1;
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : newList) {  
	    	previousPreAuthTableDTO.setSerialNumber(sno);
	    	sno++;
	    	preauthPreviousDetailsPageObj.addBeanToList(previousPreAuthTableDTO);		
		}*/
//		preauthPreviousDetailsPageObj.setTableList(this.bean.getPreviousPreAuthTableDTO());
	
		this.diagnosisDetailsTable.init("", false, false);
		this.diagnosisDetailsTable.setTableList(diagnosisList);
		this.diagnosisDetailsTable.setCaption("Diagnosis Details");
		/*int serialNumber = 1;
        for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
        	diagnosisDetailsTableDTO.setSerialNumber(serialNumber);
        	serialNumber ++;
        	this.diagnosisDetailsTable.addBeanToList(diagnosisDetailsTableDTO);
			
		}*/
		
		
		VerticalLayout verticalMain = new VerticalLayout(firstForm,preauthPreviousDetailsPageObj,this.diagnosisDetailsTable);
		verticalMain.setSpacing(true);
		
		
		Panel mainPanel = new Panel(verticalMain);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Cashless Details");
	    
	    return mainPanel;  	
    }
    
 private Panel covidDetailsPanel(){
    	
    	this.covidBinder = new BeanFieldGroup<PreauthDataExtaractionDTO>(PreauthDataExtaractionDTO.class);
		this.covidBinder.setItemDataSource(this.bean.getPreauthDTO().getPreauthDataExtractionDetails());
		covidBinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		cmbCovid19Vairant = (ComboBox) covidBinder.buildAndBind("COVID-19 Variant","covid19Variant", ComboBox.class);
		cmbCovid19Vairant.setReadOnly(true);
		
		cmbCocktailDrug = (ComboBox) covidBinder.buildAndBind("Cocktail drug(Monoclonal Antibody) Approved","cocktailDrug", ComboBox.class);
		cmbCocktailDrug.setReadOnly(true);
		
		
		
		List<Reimbursement> reimbursementDetails = reimbService
				.getRembursementDetails(this.bean.getClaimKey());
		if(reimbursementDetails != null && !reimbursementDetails.isEmpty()){
			for (Reimbursement reimbursement : reimbursementDetails) {
				if(reimbursement.getCovid19Variant()!=null){
					BeanItemContainer<SelectValue> covid19VariantContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					MastersValue covid19Variant = reimbursement.getCovid19Variant();
					
					covid19VariantContainer.addBean(new SelectValue(covid19Variant.getKey(), covid19Variant.getValue()));
					cmbCovid19Vairant.setReadOnly(false);
					cmbCovid19Vairant.setContainerDataSource(covid19VariantContainer);
					cmbCovid19Vairant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbCovid19Vairant.setItemCaptionPropertyId("value");
					cmbCovid19Vairant.setValue(covid19VariantContainer.size() > 0 ? covid19VariantContainer.getIdByIndex(0): null);
					cmbCovid19Vairant.setReadOnly(true);
					
				}
				
				if(reimbursement.getCocktailDrug()!=null){
					BeanItemContainer<SelectValue> cocktailDrugContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					
					MastersValue cocktailDrug = reimbursement.getCocktailDrug();
					
					/*SelectValue selectValue = new SelectValue();
					selectValue.setId((cocktailDrug != null && cocktailDrug.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : (cocktailDrug != null && cocktailDrug.equalsIgnoreCase("N")) ? ReferenceTable.COMMONMASTER_NO : 0l);
					selectValue.setValue((cocktailDrug != null && cocktailDrug.equalsIgnoreCase("Y")) ? SHAConstants.YES: (cocktailDrug != null && cocktailDrug.equalsIgnoreCase("N")) ? SHAConstants.No : "");
					*/
					cocktailDrugContainer.addBean(new SelectValue(cocktailDrug.getKey(), cocktailDrug.getValue()));
					cmbCocktailDrug.setReadOnly(false);
					cmbCocktailDrug.setContainerDataSource(cocktailDrugContainer);
					cmbCocktailDrug.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbCocktailDrug.setItemCaptionPropertyId("value");
					cmbCocktailDrug.setValue(cocktailDrugContainer.size() > 0 ? cocktailDrugContainer.getIdByIndex(0): null);
					cmbCocktailDrug.setReadOnly(true);
				}
			}
		}
		
		
		FormLayout covid19Form = new FormLayout(cmbCovid19Vairant);
		covid19Form.setSpacing(true);
		FormLayout cocktailDrugForm = new FormLayout(cmbCocktailDrug);
		cocktailDrugForm.setSpacing(true);
		covidVariantHLayout = new HorizontalLayout(covid19Form,cocktailDrugForm);
		covidVariantHLayout.setWidth("100%");
		covidVariantHLayout.setMargin(true);
		covidVariantHLayout.setSpacing(true);
		Panel mainPanel = new Panel(covidVariantHLayout);
	    mainPanel.addStyleName("girdBorder");
	    //mainPanel.setCaption("Registration Details");
	    return mainPanel;
		
    }
    
    
    public void addListener(){
    	
    	btnViewDocument.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getIntimationId() != null){
					viewUploadedDocumentDetails(bean.getIntimationId());
				}
				
			}
		});
    	
    	btnViewPolicyDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getIntimationId() != null){
					getViewPolicy(bean.getIntimationId());
				}
				
			}
		});
    	
    	btnViewLinkedPolicyDetails.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId() != null){
					viewDetails.showViewLinkedPolicy(bean);
				}
			}
		});
    	
    	btnViewTrails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getIntimationId() != null){
					getViewClaimHistory(bean.getIntimationId());
				}
				
			}
		});
    	
    }
    
	public void getViewClaimHistory(String intimationNo) {
		
		if(intimationNo != null){
		Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		
		Boolean result = true;
		
		if (intimation != null) {

		result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			
			
			if(result){
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setContent(viewClaimHistoryRequest);
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
			}else{
					getErrorMessage("Claim is not available");
			}
		 }
		
		}else{
			getErrorMessage("History is not available");
		}
	}
    
    
	public void viewUploadedDocumentDetails(String intimationNo) {
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
	//	getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);


		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
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
*/
	}
	
	public void setSectionDetailsTable()
	{
		Claim claim = claimService.getClaimByKey(this.bean.getClaimKey());
		
		ViewSectionDetailsTableDTO sectionDTO = new ViewSectionDetailsTableDTO();
		
		Intimation intimation = intimationService
				.getIntimationByNo(this.bean.getIntimationId());
		
		if(claim != null && claim.getClaimSectionCode() != null)
		{
			ClaimSection section = masterService.getSectionForView(intimation.getPolicy().getProduct().getKey(), claim.getClaimSectionCode());
		
			if(section != null)
			{
				sectionDTO.setSection(section.getSectionValue());
				
				ClaimSectionCover cover = masterService.getCoverForView(section.getSectionKey(), claim.getClaimCoverCode());
				
				if(cover != null)
				{
					sectionDTO.setCover(cover.getCoverValue());
					
					ClaimSectionSubCover subCover = masterService.getSubCoverForView(cover.getCoverKey(), claim.getClaimSubCoverCode());
					
					if(subCover != null)
					{
						sectionDTO.setSubCover(subCover.getSubCoverValue());
					}
				}
			}
			this.sectionDetailsTableObj.addBeanToList(sectionDTO);
			this.sectionDetailsTableObj.setVisible(true);
		}
		else
		{
			this.sectionDetailsTableObj.setVisible(false);
		}
	}

	
    public void setFieldVisitDetailsTable(){
    	
    	Long claimKey = 841l;    //will get claim key from this.bean dto
    	
    	List<FieldVisitRequest> fieldVisitDetails = fieldVisitRequestService.getFieldVisitByClaimKey(this.bean.getClaimKey());
    	
    	if(null != fieldVisitDetails){
    		List<FieldVisitDetailsTableDTO> tableDto = new ArrayList<FieldVisitDetailsTableDTO>();
    		int i=1;
    		for (FieldVisitRequest fieldVisitRequest : fieldVisitDetails) {
				FieldVisitDetailsTableDTO dto = new FieldVisitDetailsTableDTO();
				dto.setSno(i);
				dto.setRepresentiveName(fieldVisitRequest.getRepresentativeName());
				dto.setFvrAssignedDate(fieldVisitRequest.getAssignedDate());
				dto.setFvrReceivedDate(fieldVisitRequest.getFvrReceivedDate());
				dto.setRemarks(fieldVisitRequest.getExecutiveComments());
				if(null != fieldVisitRequest.getStatus()){
				dto.setStatus(fieldVisitRequest.getStatus().getProcessValue());
				i++;
				}
				tableDto.add(dto);
			}
    		fieldVisitDetailsTableObj.setTableList(tableDto);
    	}
    }

	public void setBillProcessTableValues() {
		
		List<ViewTmpReimbursement> reimbursementDetails = reimbursementService
				.getViewTmpRimbursementDetails(this.bean.getClaimKey()/*claimKey*/);

		List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
		List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
		if (reimbursementDetails != null) {
			int i =1;
			int snoFinance = 1;
			for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
				if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
					
					if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
	                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
	                 
	                 dto.setSno(i);
	                 i++;
	                 if(null != reimbursement.getClaim()){
	                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
	                 }
	//                 if(null != reimbursement.getBillingApprovedAmount()){
	//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
	//                 }
	                 
	                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
						if(approvedAmount >0){
							dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
					}
						
					 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
	//                 if(reimbursement.getBillingCompletedDate() != null){
	                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
	//                 }
	                 dto.setStatus(reimbursement.getStatus().getProcessValue());
	                 dto.setRodKey(reimbursement.getKey());
	                 dto.setRodNumber(reimbursement.getRodNumber());
	                 
	                 dto.setTypeOfClaim(reimbursement.getClaim().getClaimType().getValue());
	                 
	                 if(reimbursement.getBenefitsId() != null)
                	 {
                		 dto.setBenefitCover(reimbursement.getBenefitsId().getValue()+", "+reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));	 
                	 }
	                 else
	                 {
	                	 dto.setBenefitCover(reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));
	                 }
	                 
	                 if(reimbursement.getDocAcknowLedgement() != null)
	                 {
	                	 DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
	                	 
	                	 if(reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y") ){
	                		 dto.setRodType("Reconsideration");
	 	 				}else{
	 	 					dto.setRodType("Original");
	 	 				}
	                	 
	                	 if(docAcknowledgement.getDocumentReceivedFromId() != null)
	                	 {
	                		 dto.setDocReceivedFrom(docAcknowledgement.getDocumentReceivedFromId().getValue());	 
	                	 }
	                	 
	                	 if(docAcknowledgement != null){
		         				String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
		         				dto.setBillClassification(billClassificationValue);
		         			}
	                	 
	                 }
	                 
	                 billDetails.add(dto);
					}
					
					if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
						FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
						dto1.setSno(snoFinance);
						snoFinance++;
						dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
						Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
						if(approvedAmount >0){
							dto1.setAmount(String.valueOf(approvedAmount));
					     }
	//					if(reimbursement.getFinancialCompletedDate() != null){
						dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
						
						dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
	//					}
						dto1.setStatus(reimbursement.getStatus().getProcessValue());
		                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
		                dto1.setRodKey(reimbursement.getKey());
		                
		                dto1.setTypeOfClaim(reimbursement.getClaim().getClaimType().getValue());
		                
		                if(reimbursement.getDocAcknowLedgement() != null && 
		                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
		                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
		                }
		                
		                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
		                {
		                	ClaimPayment claimPayment;		                
		                	List<ClaimPayment> claimPaymetList = paymentProcessCpuService.getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
		                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
		                	{
		                		claimPayment = claimPaymetList.get(0);
		                		dto1.setPaymentType(claimPayment.getPaymentType());
		                		if(claimPayment.getChequeDDDate() != null){
		                			dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getChequeDDDate()));
		                		}
		                		
		                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
		                		
		                	}
		                	
		                }
		                dto1.setRodNumber(reimbursement.getRodNumber());
		                
		                if(reimbursement.getBenefitsId() != null)
	                	 {
		                	dto1.setBenefitCover(reimbursement.getBenefitsId().getValue()+", "+reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));	 
	                	 }
		                 else
		                 {
		                	 dto1.setBenefitCover(reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));
		                 }
		                
		                if(reimbursement.getDocAcknowLedgement() != null)
		                 {
		                	 DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
		                	 
		                	 if(reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y") 
		 	 						){
		                		 dto1.setRodType("Reconsideration");
		 	 				}else{
		 	 					dto1.setRodType("Original");
		 	 				}
		                	 
		                	 if(docAcknowledgement != null){
			         				String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
			         				dto1.setBillClassification(billClassificationValue);
			         			}
		                	 
		                 }
		                
		                
//		                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
		                financialDetails.add(dto1);
					}
				}
			}
		}
		billingProcessingTable.setTableList(billDetails);
		financialApprovalTable.setTableList(financialDetails);

	}
	
	public void setReceiptOfDocumentDetailsTable(){
		List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService.getReceiptOfMedicalApproverByReimbursement(this.bean.getClaimKey(),this.bean.getRodKey());
		int serialNumber = 1;
		if(listDocumentDetails != null){
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : listDocumentDetails) {
			viewDocumentDetailsDTO.setSno(serialNumber);
			serialNumber ++;
			this.receiptOfDocumentTable.addBeanToList(viewDocumentDetailsDTO);
			
			if(viewDocumentDetailsDTO.getColorCodeCell() != null && !viewDocumentDetailsDTO.getColorCodeCell().isEmpty()){
				this.receiptOfDocumentTable.setRowColor(viewDocumentDetailsDTO);
				this.receiptOfDocumentTable.setCelldescription(viewDocumentDetailsDTO);
			}
		 }
		}
	}
	
public void setPaymentDetailsTable(Long claimKey){
		
		Claim claim = claimService.getClaimByKey(claimKey);
		
		List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(claim.getClaimId());
		
		List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
		for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
			Reimbursement reimbursement = null;
		    if(paymentProcessCpuTableDTO2.getRodKey() != null){
		    	reimbursement = reimbursementService.getReimbursementByKey(paymentProcessCpuTableDTO2.getRodKey());
		    }else{
		    	reimbursement = reimbursementService.getReimbursementByRodNo(paymentProcessCpuTableDTO2.getRodNo());
		    }

			if(reimbursement != null)
			{
				if(reimbursement.getBenefitsId() != null)
	           	 {
	           		paymentProcessCpuTableDTO2.setBenefitCover(reimbursement.getBenefitsId().getValue()+", "+reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));
	           	 }
				else
				{
					paymentProcessCpuTableDTO2.setBenefitCover(reimbursementService.getCoverValueForViewBasedOnrodKey(reimbursement.getKey()));
				}
			}
			
			if(reimbursement !=null && reimbursement.getDocAcknowLedgement() != null)
			{

           	 DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
           	 
           	 if(reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y") 
 						){
           		paymentProcessCpuTableDTO2.setRodType("Reconsideration");
 				}else{
 					paymentProcessCpuTableDTO2.setRodType("Original");
 				}
           	 
           	if(docAcknowledgement != null){
  				String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
  				paymentProcessCpuTableDTO2.setBillClassification(billClassificationValue);
  			}
           	 
			}
			
			if(paymentProcessCpuTableDTO2.getChequeDate() != null){
				paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
			}
			
			if(paymentProcessCpuTableDTO2.getPolicyNumber() !=null){
				Policy policy = policyService.getByPolicyNumber(paymentProcessCpuTableDTO2.getPolicyNumber());
			
				if(policy !=null && policy.getPolicySource() !=null){
					paymentProcessCpuTableDTO2.setPolicySource(policy.getPolicySource());
				}
			}
		}
		
		paymentTableObj.setTableList(paymentProcessCpuTableDto);
	}
	

    @SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
		        field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
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
	
	public void getViewPolicy(String intimationNo) {
		final Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());
		//IMSSUPPOR-29207
		if(policy.getProduct() != null && (ReferenceTable.getGMCProductCodeList().containsKey(policy.getProduct().getCode()) || policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
				|| policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE))
				|| policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.GROUP_TOPUP_PRODUCT_CODE_96)){
			viewPolicyDetail.setPolicyServiceAndPolicyForGMC(policyService, policy,intimation.getInsured(),
					masterService,intimationService);	
		}else{		
		viewPolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
				masterService, intimationService);
		}
		viewPolicyDetail.initView();
		UI.getCurrent().addWindow(viewPolicyDetail);
	}
    
    public void setAuditDetails()
    {
    	
    	this.preauthPreviousDetailsPageObj.generateAuditDetails();
    	this.financialApprovalTable.generateAuditDetails();
    	this.receiptOfDocumentTable.generateAuditDetails();
    	
    }

    private Panel getimplantDetailsPanel(){
    	
    	boolean implantApplicable = false;
    	
    	Claim claim = claimService.getClaimByKey(bean.getClaimKey());
    	if(claim !=null && claim.getClaimType() !=null){
    		if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
        		Reimbursement reimbursement = reimbursementService.getLatestReimbursementDetails(this.bean.getClaimKey());
        		if(reimbursement !=null && reimbursement.getImplantFlag() !=null 
        				&& reimbursement.getImplantFlag().equals("Y")){
        			implantApplicable = true;
        		}
        	}else{
        		Preauth preauth = preauthService.getLatestPreauthByClaimKey(this.bean.getClaimKey());
        		if(preauth !=null && preauth.getImplantFlag() !=null 
        				&& preauth.getImplantFlag().equals("Y")){
        			implantApplicable = true;
        		}
        	}
    	}
    	
    	txtimplantApplicable = new TextField("Implant Applicable");
    	txtimplantApplicable.setWidth("300px");
    	txtimplantApplicable.setNullRepresentation("-");
    	if(implantApplicable){
    		txtimplantApplicable.setValue("Yes");
    	}else{
    		txtimplantApplicable.setValue("No");
    	}
    	txtimplantApplicable.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtimplantApplicable.setReadOnly(true);
    	
    	FormLayout firstForm = new FormLayout(txtimplantApplicable);
    	firstForm.setSpacing(true);
    	
    	if(implantApplicable){
    		
    		List<ImplantDetailsDTO> implantDetailsDTOs =null;
    		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
    		List<ImplantDetails> findImplantDetailsByclaimKey = preauthService.findImplantDetailsByClaimKey(this.bean.getClaimKey());
    		if(findImplantDetailsByclaimKey !=null && !findImplantDetailsByclaimKey.isEmpty()){
    			implantDetailsDTOs = premedicalMapper.getimplantDetailsDTOList(findImplantDetailsByclaimKey);
    		}
    		
    		implantDetailsTableObj = ImplantDetailsTableinstance.get();
        	implantDetailsTableObj.init("", false, false);
        	if(implantDetailsDTOs !=null){
            	implantDetailsTableObj.setTableList(implantDetailsDTOs);
        	}
        	implantDetailsTableObj.setCaption("Implant Details");
    	}
    	VerticalLayout verticalMain;

    	if(implantApplicable){
    		verticalMain = new VerticalLayout(firstForm,implantDetailsTableObj);
    	}else{
    		verticalMain = new VerticalLayout(firstForm);
    	}
    	verticalMain.setSpacing(true);
	
		Panel mainPanel = new Panel(verticalMain);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Implant Details");
	    
	    return mainPanel;  	
    }

    public void initForUTRSearch(ViewClaimStatusDTO bean,Long rodKey){
    	init(bean,rodKey);
 
    	btnViewPolicyDetails.setVisible(false);
		btnViewLinkedPolicyDetails.setVisible(false);
		btnViewTrails.setVisible(false);
		btnViewDocument.setVisible(false);
    }
    
    public void clearObjects(){
    	this.bean = null;
    	this.rodKey = null;
    	this.binder = null;
    	this.registrationBinder = null;
    	this.cashLessBinder = null;
    	if(this.preauthPreviousDetailsPageObj != null){
    		this.preauthPreviousDetailsPageObj.removeRow();
    	}
    	this.preauthPreviousDetailsPageObj = null;
    	this.diagnosisDetailsTable.removeRow();
    	this.sectionDetailsTableObj.removeRow();
    	this.sectionDetailsTableObj = null;
    	this.fieldVisitDetailsTableObj.removeRow();
    	this.fieldVisitDetailsTableObj = null;
    	this.receiptOfDocumentTable.removeRow();
    	this.billingProcessingTable.removeRow();
    	this.financialApprovalTable.removeRow();
    	this.paymentTableObj.removeRow();
    	mainVerticalLayout.removeAllComponents();
    	mainVerticalLayout = null;
    	
    }
    
    public void getHtmlString(String inputy)
    {
    	
    }
}
