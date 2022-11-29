/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.forms;

/**
 * @author ntv.vijayar
 *
 */

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.preauth.wizard.pages.DiagnosisProcedureListenerTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDecisionPresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.AddAdditionalFVRPointsPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.PendingFvrInvsQueryPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.Toolbar;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
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
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalRequestBenefitsButtonsUI  extends ViewComponent {
	private static final long serialVersionUID = 7089191150922046819L;
	

	//@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	@Inject
	private Intimation intimation;
	
	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;
	
	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;
	
	@Inject
	private Instance<DiagnosisProcedureListenerTable> medicalDecisionTable;
	
	@Inject
	private Instance<FinancialDecisionCommunicationViewImpl> financialDecisionCommunicationViewImpl;
	
	private FinancialDecisionCommunicationViewImpl financialDecisionCommunicationObj;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	private AmountConsideredTable amountConsideredTable;
	
	private Window popup;
	
	private DiagnosisProcedureListenerTable medicalDecisionTableObj;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private TextArea approvalRemarks;
	
	private Button submitButton;

	private Label amountConsidered;
	
	@Inject
	private ViewDetails viewDetails;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;
	
//	private BeanFieldGroup<PreauthMedicalDecisionDTO> binderMA;
	

	private Button approveBtn;
	private Button queryBtn;
	private Button rejectBtn;
	private Button cancelROD;
	private Button escalateClaimBtn;
	private Button sendReplyBtn;
	private Button referCoordinatorBtn;
	private Button initiateFieldVisitBtn;
	private Button initiateInvestigationBtn;
	private Button escalteReplyBtn;
	private Button referToSpecialistBtn;
	
	private Button referToMedicalApproverBtn;
	private Button referToBillingBtn;
	
	
	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextArea approvalRemarksTxta;
	private Label originalBillsReceivedLbl;
	private CheckBox originalBillsReceivedChk;
	private Button submitBtnForApprovalRemarksWithListener;
	private TextArea queryRemarksTxta;
	private ComboBox rejectionCategoryCmb;
	private TextArea rejectionRemarksTxta;
	private TextArea cancelRODRemarks;
	private ComboBox reasonForCancelRODCmb;
	private ComboBox reasonForDenialCmb;
	private TextArea denialRemarksTxta;
	private ComboBox escalateToCmb;
	private Upload uploadFile;
	private TextArea escalationRemarksTxta;
	private ComboBox typeOfCoordinatorRequestCmb;
	private TextArea reasonForReferringTxta;
	private FormLayout dynamicFrmLayout;
	
	private TextField referredByRole;
	private TextField referredByName;
	private TextField reasonForReferring;
	private TextField remarks;
	private TextArea medicalApproversReply;
	
	private ComboBox allocationTo;
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;
	
	private TextArea triggerPointsToFocus;
	private TextArea reasonForReferringIV;
	
	//Added for query table.
	
	
	//private FormLayout dynamicTblLayout;
	
	
	
	private OptionGroup sentToCPU;
	private TextArea remarksForCPU;
	
	private TextField txtQueryCount;
	
	private ArrayList<Component> mandatoryFields; 
	
	private List<String> errorMessages;
	
	private GWizard wizard;


	private TextArea escalteReplyTxt;


	private TextField escalateDesignation;


	private ComboBox specialistType;


	private TextField approvedAmtField;


	private TextArea zonalRemarks;


	private TextArea corporateRemarks;


	private Button cancelButton;


	private TextArea medicalApproverRemarks;
	
    private VerticalLayout submainlayout;
	
    private ComboBox cmbFVRNotRequiredRemarks;
	
	private VerticalLayout fvrVertLayout;
	
	private OptionGroup select;
	
	private TextArea fvrOthersRemarks;
	
	private ComboBox fvrPriority;
	
	private ComboBox reasonForCancelRodCmb;
	private TextArea cancellationRemarks;
	
	private Boolean isCancelRodLayout = Boolean.FALSE;
	
	private Boolean isApproval = false;
	
	private TextField txtDefinedLimit;
	
	private CheckBox chkSubDoc;
	private CheckBox chkFVR;
	private CheckBox chkIR;
	private CheckBox chkOthers;
	private TextArea txtaRemarks;

	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbService;
	
	@Inject
	private Toolbar toolBar;
	
	FormLayout userLayout = new FormLayout();
	
	private ComboBox cmbQueryType;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	private Button viewClaimsDMSDocument;
	
	private TextArea remarksFromDeptHead;	
	
	@Inject
	private CreateRODService createRodService;
	
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	
	private ComboBox rejectionSubCategoryCmb;
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ParallelInvestigationDetails viewInvestigationDetails;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;
	
	@Inject
	private InvestigationService investigationService;
	
	@Inject
	private AddAdditionalFVRPointsPageUI addAdditionalFvrPointsPageUI;
	
	@Inject
	private PendingFvrInvsQueryPageUI invesFvrQueryPendingPageUI;
	
	private Map<String, String> roleValidationContainer = new HashMap<String, String>();
	private Map<String, String> userValidationContainer = new HashMap<String, String>();

	public Map<String, String> getRoleValidationContainer() {
		return roleValidationContainer;
	}

	public void setRoleValidationContainer(
			Map<String, String> roleValidationContainer) {
		this.roleValidationContainer = roleValidationContainer;
	}

	public Map<String, String> getUserValidationContainer() {
		return userValidationContainer;
	}

	public void setUserValidationContainer(
			Map<String, String> userValidationContainer) {
		this.userValidationContainer = userValidationContainer;
	}
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public void initView(ReceiptOfDocumentsDTO bean, GWizard wizard) 
	{
		errorMessages = new ArrayList<String>();
		mandatoryFields = new ArrayList<Component>();
		this.bean = bean;
		this.wizard = wizard;
		wholeVlayout = new VerticalLayout();
		wholeVlayout.setHeight("-1px");
		wholeVlayout.setWidth("-1px");
		wholeVlayout.setSpacing(true);
		HorizontalLayout buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setSizeFull();
		addListener();
		if(bean.getIsDishonoured()) {
//			referToSpecialistBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			//referToMedicalApproverBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
			//referToBillingBtn.setEnabled(false);
			initiateFieldVisitBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			approveBtn.setEnabled(false);
			sendReplyBtn.setEnabled(false);
			cancelROD.setEnabled(false);
			rejectBtn.setEnabled(true);
		} else {
//			referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			//referToMedicalApproverBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			//referToBillingBtn.setEnabled(true);
			initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(true);
			sendReplyBtn.setEnabled(true);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		}
		
		if (bean.getPreauthDTO().getIsReferToMedicalApprover()) {
			approveBtn.setEnabled(false);
			sendReplyBtn.setEnabled(true);
		} else {
			approveBtn.setEnabled(true);
			sendReplyBtn.setEnabled(false);
		}
		
		Map<String, Object> outPutArray = (Map<String, Object>)bean.getDbOutArray();
		String sendReplyStatus = (String) outPutArray.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if(null != sendReplyStatus && (sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY) || sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_BILLING) ||
				sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_FA))){
			sendReplyBtn.setEnabled(true);
			approveBtn.setEnabled(false);
		}
//		HorizontalLayout buttonFirstLayout = new HorizontalLayout( referCoordinatorBtn, referToBillingBtn, queryBtn);
//		HorizontalLayout buttonSecondLayout = new HorizontalLayout( approveBtn, rejectBtn,cancelROD);
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);
		
		 viewAllDocsLayout = new HorizontalLayout();
		
		HorizontalLayout buttonFirstLayout = new HorizontalLayout( initiateFieldVisitBtn,queryBtn, initiateInvestigationBtn);
		HorizontalLayout buttonSecondLayout = new HorizontalLayout(referCoordinatorBtn,sendReplyBtn, rejectBtn, approveBtn,cancelROD);
		buttonFirstLayout.setSpacing(true);
		buttonSecondLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(buttonFirstLayout, buttonSecondLayout);
		verticalLayout.setSpacing(true);
		buttonsHLayout.addComponents(verticalLayout);
		buttonsHLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
		buttonsHLayout.setSpacing(true);
		wholeVlayout.addComponent(buttonsHLayout);
		dynamicFrmLayout = new FormLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		wholeVlayout.addComponent(dynamicFrmLayout);
		setCompositionRoot(wholeVlayout);
	
	}
	
	private void addListener() {
		approveBtn = new Button("Approve");
		approveBtn.setHeight("-1px");
		//Vaadin8-setImmediate() approveBtn.setImmediate(true);
		approveBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setUserClickAction("approval");
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getPreauthDTO().getKey(),
						SHAConstants.MA_Q,bean.getClaimDTO().getNewIntimationDto().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getPreauthDTO().getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());

				if(null != pendingFvrInvsQueryList && !pendingFvrInvsQueryList.isEmpty()){

					showPendingFvrInvsQueryPopup(bean.getPreauthDTO(),pendingFvrInvsQueryList,SHAConstants.MEDICAL_APPROVAL_BENEFITS_CLAIM_REQUEST,SHAConstants.APPROVAL);

				}
				 else {
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_APPROVE_EVENT, null);
				}
                
			}
		});
		cancelROD = new Button("Cancel ROD");
		cancelROD.setHeight("-1px");
		//Vaadin8-setImmediate() cancelROD.setImmediate(true);
		cancelROD.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				cancelRodAction();
			}
		});
		
		queryBtn = new Button("Query");
		queryBtn.setHeight("-1px");
		//Vaadin8-setImmediate() queryBtn.setImmediate(true);
		queryBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4614951723748846970L;

			@Override
			public void buttonClick(ClickEvent event) {
				
					if(null != bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsFvrIntiated() && bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsFvrIntiated()){
						alertForAdditionalFvrTriggerPoints(SHAConstants.QUERY);
					}
					else
					{
						submitQueryEvent();
					}
			}
		});
		
		rejectBtn = new Button("Reject");
		rejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() rejectBtn.setImmediate(true);
		rejectBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4241727763379504532L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.getPreauthDTO().setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.getPreauthDTO().setIsPedWatchList(false);
				}
				
				if(! bean.getPreauthDTO().getIsPedWatchList()){
				fireViewEvent(
						ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_REJECT_EVENT,
						bean.getPreauthDTO());
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});
		
	
		
		referCoordinatorBtn = new Button("Refer to Co-ordinator");		
		referCoordinatorBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referCoordinatorBtn.setImmediate(true);
		referCoordinatorBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_REFERCOORDINATOR_EVENT,null);
			}
		});
		
		initiateFieldVisitBtn = new Button("Initiate Field Visit");		
		initiateFieldVisitBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateFieldVisitBtn.setImmediate(true);
		initiateFieldVisitBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
//				isApproval = false;
				
				if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.getPreauthDTO().setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.getPreauthDTO().setIsPedWatchList(false);
				}
				
				if(! bean.getPreauthDTO().getIsPedWatchList()){
					
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsAllowInitiateFVR(preauthService.getFVRStatusByRodKey(bean.getKey()));
					
					if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails()!= null && bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR() != null && !bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsAllowInitiateFVR()){

						fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MEDICAL_REQUEST_FVR_EVENT,bean.getPreauthDTO());
					}
					else{
//						showErrorPopUp("FVR request is in process cannot initiate another request");						
						fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MEDICAL_REQUEST_FVR_EVENT,bean.getPreauthDTO());
					}
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});
		
		initiateInvestigationBtn = new Button("Initiate Investigation");		
		initiateInvestigationBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateInvestigationBtn.setImmediate(true);
		initiateInvestigationBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				//bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);

				if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.getPreauthDTO().setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.getPreauthDTO().setIsPedWatchList(false);
				}
				
				Boolean investigationAvailable = false;
				Reimbursement reimbObj = ackDocRecvdService.getReimbursementByRodNo(bean.getPreauthDTO().getRodNumber());
				
				investigationAvailable = investigationService.getInvestigationAvailableForClaim(reimbObj.getClaim().getKey());
				bean.getPreauthDTO().setIsInvestigation(investigationAvailable);
				
				if(! bean.getPreauthDTO().getIsPedWatchList()){
					if(! bean.getPreauthDTO().getIsInvestigation()){

						boolean sendToAssignInv = false;
						
						String invBypassAllowed = dbCalculationService.bypassInvestigationAllowed(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						sendToAssignInv = (SHAConstants.YES_FLAG).equalsIgnoreCase(invBypassAllowed) ? true : false;
						
//							 sendToAssignInv = true; //For Testing Purpose Need to Be commented  TODO 1227
						
						bean.getPreauthDTO().setDirectToAssignInv(sendToAssignInv);
						
						if(!bean.getPreauthDTO().getIsInvsRaised()){							
							 
							fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MEDICAL_REQUEST_INVESTIGATION_EVENT,bean.getPreauthDTO());
						}
						else{
							confirmationForInvestigation();	
						}
					}else if(bean.getPreauthDTO().getIsInvestigation()){
						alertMessageForInvestigation();
					}	
				}
				else{
					alertMessageForPEDWatchList();
				}
			}
		});
		
		sendReplyBtn = new Button("Send Reply");
		sendReplyBtn.setHeight("-1px");
		//Vaadin8-setImmediate() sendReplyBtn.setImmediate(true);
		sendReplyBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {//				isApproval = false;
				
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setUserClickAction("Send Reply");
				bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getPreauthDTO().getKey(),
						SHAConstants.MA_Q,bean.getClaimDTO().getNewIntimationDto().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getPreauthDTO().getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrInvsQueryList && !pendingFvrInvsQueryList.isEmpty()){
				
					showPendingFvrInvsQueryPopup(bean.getPreauthDTO(),pendingFvrInvsQueryList,SHAConstants.MEDICAL_APPROVAL_BENEFITS_CLAIM_REQUEST,SHAConstants.SEND_REPLY);
				
				}	
				
				else
				{
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_SENT_TO_REPLY_EVENT,bean.getPreauthDTO());
			   }
			}
		});
//		
		referToSpecialistBtn = new Button("Refer to Specialist");		
//		referToSpecialistBtn.setHeight("-1px");
//		//Vaadin8-setImmediate() referToSpecialistBtn.setImmediate(true);
//		referToSpecialistBtn.addClickListener(new Button.ClickListener() {
//			
//			*//**
//			 * 
//			 *//*
//			private static final long serialVersionUID = 7255298985095729669L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_REFER_TO_SPECIALIST_EVENT,null);
//			}
//		});
//		
//		referToMedicalApproverBtn = new Button("Refer to Medical Approver");		
//		referToMedicalApproverBtn.setHeight("-1px");
//		//Vaadin8-setImmediate() referToMedicalApproverBtn.setImmediate(true);
//		referToMedicalApproverBtn.addClickListener(new Button.ClickListener() {
//			
//			*//**
//			 * 
//			 *//*
//			private static final long serialVersionUID = 7255298985095729669L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT, null);
//			}
//		});*/
		
		referToBillingBtn = new Button("Refer To Billing");		
		referToBillingBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToBillingBtn.setImmediate(true);
		referToBillingBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FIANANCIAL_REFER_TO_BILLING_EVENT, null);
			}
		});
		
		if(this.bean.getIsCancelPolicy()) {
			if(this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
			
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
//			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
		}
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(DocumentDetailsDTO.class);
		this.binder.setItemDataSource(bean.getDocumentDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setCancelPolicyProcess(ReceiptOfDocumentsDTO bean){
		this.bean = bean;
		if(this.bean.getIsCancelPolicy()){
			if(this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildApproveLayout(Integer amt)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		initialTotalApprovedAmtTxt = (TextField) binder.buildAndBind("Pre-auth Approved Amt","initialTotalApprovedAmt",TextField.class);
		initialTotalApprovedAmtTxt.setNullRepresentation("");
		initialTotalApprovedAmtTxt.setEnabled(false);
		initialTotalApprovedAmtTxt.setValue(amt.toString());
		
		approvalRemarksTxta = (TextArea) binder.buildAndBind("Approval Remarks", "approvalRemarks",TextArea.class);
		approvalRemarksTxta.setMaxLength(100);
		addingSentToCPUFields();
		dynamicFrmLayout.addComponents(initialTotalApprovedAmtTxt, approvalRemarksTxta);
		dynamicFrmLayout.addComponent(sentToCPU);
		wholeVlayout.addComponent(dynamicFrmLayout);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(initialApprovedAmtTxt);
		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != bean.getPreauthDTO().getIsParallelInvFvrQuery() && bean.getPreauthDTO().getIsParallelInvFvrQuery()){
					bean.getPreauthDTO().setStatusKey(bean.getPreauthDTO().getParallelStatusKey());
					bean.setStatusKey(bean.getPreauthDTO().getParallelStatusKey());
				}
				binder = null;
				isApproval = false;
			}
		});
		return cancelButton;
	}
	
	private Button getFAApproveCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				binder.setItemDataSource(bean.getDocumentDetails());
				binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
				dialog.close();
			}
		});
		return cancelButton;
	}
	
	public void generateCancelRODLayout(Object dropdownValues){
		initBinder();
	//	unbindAndRemoveComponents(userLayout);
		unbindAndRemoveComponents(dynamicFrmLayout);		
		reasonForCancelRodCmb=(ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
		cancellationRemarks =(TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
		cancellationRemarks.setMaxLength(4000);
		cancellationRemarks.setWidth("400px");
		cancellationRemarks.setHeight("200px");
		
		BeanItemContainer<SelectValue> cancellationReason = (BeanItemContainer<SelectValue>) referenceData.get("cancellationReason");
		reasonForCancelRodCmb.setContainerDataSource(cancellationReason);
		reasonForCancelRodCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForCancelRodCmb.setItemCaptionPropertyId("value");
		
		/*dynamicGenarateLayout.addComponent(reasonForCancelRodCmb);
		dynamicGenarateLayout.addComponent(cancellationRemarks);*/
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(cancellationRemarks);
		mandatoryFields.add(reasonForCancelRodCmb);
		showOrHideValidation(false);
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);
		showOrHideValidation(false);
//		userLayout = buildUserRoleLayout();
		dynamicFrmLayout = new FormLayout(reasonForCancelRodCmb,cancellationRemarks);		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout);
		VerticalLayout VLayout = new VerticalLayout(hLayout, btnLayout);		
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		//VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		isCancelRodLayout = Boolean.TRUE;
		showInPopup(VLayout, dialog);
	}
	
	public void generateFieldsForApproval() {
		initBinder();
		
		approvalRemarksTxta= (TextArea) binder.buildAndBind("Approval Remarks", "financialRemarks", TextArea.class);
		approvalRemarksTxta.setMaxLength(100);
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(approvalRemarksTxta);
		
		originalBillsReceivedLbl = new Label("<p><b>Verified the amount claimed and <br>documents based on the bills received <span style='color:red;'>*</span></b></p>", ContentMode.HTML);
		
		originalBillsReceivedChk = (CheckBox) binder.buildAndBind("", "originalBillsReceived", CheckBox.class);
		mandatoryFields.add(originalBillsReceivedChk);
		addListener(originalBillsReceivedChk);
		
		HorizontalLayout originalBillsReceivedHorizontalLayout = new HorizontalLayout(originalBillsReceivedLbl, originalBillsReceivedChk);
		originalBillsReceivedHorizontalLayout.setWidth("550px");
		originalBillsReceivedHorizontalLayout.setComponentAlignment(originalBillsReceivedChk, Alignment.MIDDLE_LEFT);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		submitBtnForApprovalRemarksWithListener = getFAApproveSubmitButtonWithListener(dialog);
		submitBtnForApprovalRemarksWithListener.setEnabled(originalBillsReceivedChk.getValue());
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitBtnForApprovalRemarksWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitBtnForApprovalRemarksWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(originalBillsReceivedHorizontalLayout, new FormLayout(approvalRemarksTxta), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}

	private void addListener(final CheckBox chkBox) {	
		chkBox.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()) {  
					boolean value = (Boolean) event.getProperty().getValue();
					if(value) {
						if(submitBtnForApprovalRemarksWithListener != null) {
							submitBtnForApprovalRemarksWithListener.setEnabled(true);
						}
					} else {
						if(submitBtnForApprovalRemarksWithListener != null) {
							submitBtnForApprovalRemarksWithListener.setEnabled(false);
						}
					}
				}
			}
		});
	}
	
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("1000px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);

		// submitButton.addClickListener(new ClickListener() {
		// private static final long serialVersionUID = -5934419771562851393L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// dialog.close();
		// }
		// });

		dialog.show(getUI().getCurrent(), null, true);
		
		
	}
	
	private void showErrorPopup(String eMsg) {
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

	
	
	public void setApprovedAmtValue(Integer amount) {
		if(initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}
	
	public void generateQueryDetails(Integer setQueryValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		unbindAndRemoveComponents(userLayout);

		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		queryDetailsTableObj.setViewQueryVisibleColumn();
		
		setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());

		//R1295
		cmbQueryType = new ComboBox("Query Type");// binder.buildAndBind("Query Type","queryType",ComboBox.class);
		cmbQueryType.setContainerDataSource(masterService.getOpinionQueryType());
		cmbQueryType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbQueryType.setItemCaptionPropertyId("value");
		cmbQueryType.setWidth("200px");
		if(bean.getPreauthDTO().getQueryType() != null){
			cmbQueryType.setValue(bean.getPreauthDTO().getQueryType());
		}
		final int queryCount = setQueryValues;
		cmbQueryType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -486851813151743902L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null){
					bean.getPreauthDTO().setQueryType(value);
					bean.getPreauthDTO().setQueryCount(queryCount);

					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					generateQueryDetails(queryCount);
				}else{
					bean.getPreauthDTO().setQueryType(null);
					bean.getPreauthDTO().setQueryCount(queryCount);

					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					generateQueryDetails(queryCount);
				}
			}
		});
		
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		
		queryRemarksTxta = new TextArea("Query Remarks");		
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("350px");
		
		dynamicFrmLayout.addComponent(cmbQueryType);
		dynamicFrmLayout.addComponent(txtQueryCount);		
		dynamicFrmLayout.addComponent(queryRemarksTxta);
		dynamicFrmLayout.setSpacing(true);
		

		alignFormComponents();
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setUserClickAction("Query");
//		userLayout = buildUserRoleLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout);
		
		VerticalLayout vTblLayout = new VerticalLayout(
				queryDetailsTableObj, viewAllDocsLayout ,hLayout);
		
		vTblLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		
		wholeVlayout.addComponent(vTblLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(cmbQueryType);
		mandatoryFields.add(queryRemarksTxta);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getQuerySubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(vTblLayout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		//VLayout.setWidth("800px");
		VLayout.setMargin(true);
		
		addViewAllDocsListener();
		showInPopupForQuery(VLayout, dialog);
	}
	
	public void buildRejectLayout(Object rejectionCategoryDropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(100);
		dynamicFrmLayout.addComponent(rejectionRemarksTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(rejectionRemarksTxta);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(rejectionRemarksTxta), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}
	
	/*public void buildDenialLayout(Object denialValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		addingSentToCPUFields();
		reasonForDenialCmb = (ComboBox) binder.buildAndBind("Reason For Denial","reasonForDenial",ComboBox.class);
		reasonForDenialCmb.setContainerDataSource((BeanItemContainer<SelectValue>)denialValues);
		reasonForDenialCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForDenialCmb.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getReasonForDenial() != null){
			reasonForDenialCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getReasonForDenial());
		}
		denialRemarksTxta = (TextArea) binder.buildAndBind("Denial Remarks","denialRemarks",TextArea.class);
		denialRemarksTxta.setMaxLength(100);
		dynamicFrmLayout.addComponent(reasonForDenialCmb);
		dynamicFrmLayout.addComponent(denialRemarksTxta);
		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(reasonForDenialCmb);
		mandatoryFields.add(denialRemarksTxta);
		showOrHideValidation(false);
	}*/
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	/*public void buildEscalateLayout(Object escalateToValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		escalateToCmb = (ComboBox) binder.buildAndBind("Escalate To","escalateTo",ComboBox.class);
		escalateToCmb.setContainerDataSource((BeanItemContainer<SelectValue>)escalateToValues);
		escalateToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateToCmb.setItemCaptionPropertyId("value");
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
			escalateToCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo());
		}
//		uploadFile = (Upload) binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		uploadFile = new Upload();
		uploadFile.setCaption("File UpLoad");
		escalationRemarksTxta = (TextArea) binder.buildAndBind("Escalate Remarks","escalationRemarks",TextArea.class);
		escalationRemarksTxta.setMaxLength(100);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(escalationRemarksTxta);
		mandatoryFields.add(escalateToCmb);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener);
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(escalateToCmb, uploadFile, escalationRemarksTxta), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}*/

	private void addingSentToCPUFields() {
		unbindField(sentToCPU);
		sentToCPU = (OptionGroup) binder.buildAndBind( "Send to CPU", "sentToCPUFlag", OptionGroup.class);
		sentToCPU.addItems(getReadioButtonOptions());
		sentToCPU.setItemCaption(true, "Yes");
		sentToCPU.setItemCaption(false, "No");
		sentToCPU.setStyleName("horizontal");
		sentToCPU.select(false);
		
		sentToCPU.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				} 
				
//				fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_CPU_SELECTED, isChecked);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind("Type of Coordinator Request","typeOfCoordinatorRequest",ComboBox.class);
		typeOfCoordinatorRequestCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		reasonForReferringTxta = (TextArea) binder.buildAndBind("Reason For Refering","reasonForRefering",TextArea.class);
		reasonForReferringTxta.setMaxLength(100);
		
		if(this.bean.getDocumentDetails().getTypeOfCoordinatorRequest() != null){
			typeOfCoordinatorRequestCmb.setValue(this.bean.getDocumentDetails().getTypeOfCoordinatorRequest());
		}
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(typeOfCoordinatorRequestCmb);
		mandatoryFields.add(reasonForReferringTxta);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(typeOfCoordinatorRequestCmb, reasonForReferringTxta), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
		
	}
	
	/*public void buildSpecialistLayout(Object specialistValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		specialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistType",ComboBox.class);
		specialistType.setContainerDataSource((BeanItemContainer<SelectValue>)specialistValues);
		specialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		specialistType.setItemCaptionPropertyId("value");
//		uploadFile = (Upload) binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		
		if(this.bean.getDocumentDetails().getSpecialistType() != null){
			specialistType.setValue(this.bean.getDocumentDetails().getSpecialistType());
		}
		
		uploadFile = new Upload();
		uploadFile.setCaption("File UpLoad");
		reasonForReferringIV = (TextArea) binder.buildAndBind("Reason for Referring","reasonForRefering",TextArea.class);
		reasonForReferringIV.setMaxLength(100);
		
		HorizontalLayout layout = new HorizontalLayout(new FormLayout(specialistType, uploadFile, reasonForReferringIV));
		layout.setSpacing(true);
		
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(specialistType);
		mandatoryFields.add(reasonForReferringIV);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}*/
	
	/*public void buildEscalateReplyLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		referredByRole = new TextField("Escalted By-Role");
		referredByRole.setEnabled(false);
		
		referredByName = new TextField("Escalted By-ID / Name");
		referredByName.setEnabled(false);
		
		escalateDesignation = new TextField("Escalted By - Designation");
		escalateDesignation.setEnabled(false);
		
		remarks = new TextField("Escaltion Remarks");
		remarks.setEnabled(false);
		
		
		
		uploadFile = new Upload();
		uploadFile.setCaption("File UpLoad");
		escalteReplyTxt = (TextArea) binder.buildAndBind("Escalate Reply","escalateReply",TextArea.class);
		escalteReplyTxt.setMaxLength(100);
		
		HorizontalLayout layout = new HorizontalLayout(new FormLayout(referredByRole, remarks, escalteReplyTxt, uploadFile), new FormLayout(referredByName, escalateDesignation));
		layout.setSpacing(true);
		
	
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(escalteReplyTxt);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("1000px");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setWidth("1000px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}*/
	
	@SuppressWarnings("rawtypes")
	private void unbindAndRemoveComponents(AbstractComponent component)
	{
		for(int i=0;i<((FormLayout)component).getComponentCount();i++)
		{
			if(((FormLayout)component).getComponent(i) instanceof Upload)
			{
                continue;				
			}
			unbindField((AbstractField)((FormLayout)component).getComponent(i));
		}
		dynamicFrmLayout.removeAllComponents();
		wholeVlayout.removeComponent(dynamicFrmLayout);
		
		if( null != wholeVlayout && 0!=wholeVlayout.getComponentCount())
		{
			Iterator<Component> componentIterator = wholeVlayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  VerticalLayout )
				{
					((VerticalLayout) searchScrnComponent).removeAllComponents();
					
				}
			}
		}
	}
	
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(propertyId != null) {
				this.binder.unbind(field);	
			}
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			if(field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void alignFormComponents()
	{
		if(dynamicFrmLayout != null)
		{
			for(int i=0; i<dynamicFrmLayout.getComponentCount();i++)
			{
				dynamicFrmLayout.setExpandRatio(dynamicFrmLayout.getComponent(i), 0.5f);
			}
		}
	}
	
	
//	public boolean isValid()
//	{
//		boolean hasError = false;
//		showOrHideValidation(true);
//		errorMessages.removeAll(getErrors());
//		
//		/*if(!(null != remarksForCPU && null != remarksForCPU.getValue() && !("").equals( remarksForCPU.getValue())))
//		{
//			hasError = true;
//			errorMessages.add("Please Enter Remarks For CPU.");
//			return !hasError;
//		}
//		
//		if(!(null != approvalRemarksTxta && null != approvalRemarksTxta.getValue() && !("").equals( approvalRemarksTxta.getValue())))
//		{
//			hasError = true;
//			errorMessages.add("Please Enter Approval Remarks.");
//			return !hasError;
//		}*/
//		
//		
//		if(this.binder == null) {
//			hasError = true;
//			errorMessages.add("Please select Any Action to Proceed Further. </br>");
//			return !hasError;
//		}
//		
//		if (!this.binder.isValid()) {
//			 hasError = true;
//			 for (Field<?> field : this.binder.getFields()) {
//			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
//			    	if (errMsg != null) {
//			    		errorMessages.add(errMsg.getFormattedHtmlMessage());
//			    	}
//			  }
//		} else {
//			try {
//				this.binder.commit();
//			} catch (CommitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		showOrHideValidation(false);
//		return !hasError;
//	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void generateFieldsBasedOnSentToCPU(Boolean isChecked) {
		if(isChecked) {
			unbindField(remarksForCPU);
			remarksForCPU = (TextArea) binder.buildAndBind("Remarks for CPU","remarksForCPU",TextArea.class);
			remarksForCPU.setMaxLength(100);
			mandatoryFields.add(remarksForCPU);
			dynamicFrmLayout.addComponent(remarksForCPU);
			showOrHideValidation(false);
		} else {
			unbindField(remarksForCPU);
			mandatoryFields.remove(remarksForCPU);
			dynamicFrmLayout.removeComponent(remarksForCPU);
		}
	}
	
	public void buildSendReplyLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		referredByRole = new TextField("Referred By-Role");
		referredByRole.setEnabled(false);
		
		referredByName = new TextField("Referred By-ID / Name");
		referredByName.setEnabled(false);
		
		reasonForReferring = new TextField("Reason for reffering to Medical");
		reasonForReferring.setEnabled(false);
		
		remarks = new TextField("Remarks");
		remarks.setEnabled(false);
		
		
		medicalApproversReply = (TextArea) binder.buildAndBind("Medical Approver's Reply","approverReply",TextArea.class);
		medicalApproversReply.setMaxLength(100);
		mandatoryFields.add(medicalApproversReply);
		
		FormLayout formLayout = new FormLayout(referredByRole, reasonForReferring, remarks, medicalApproversReply);
		HorizontalLayout layout = new HorizontalLayout(formLayout, new FormLayout(referredByName));
		layout.setSpacing(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener);
		btnLayout.setWidth("800px");
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
	}

	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				
				dialog.close();
				isApproval = true;
			}
		});
		
		return submitButton;
	}
	
	private Button getFAApproveSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				isApproval = true;
				dialog.close();
			}
		});
		return submitButton;
	}
	
/*	public void buildInitiateFieldVisit(Object fieldVisitValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
		allocationTo.setContainerDataSource((BeanItemContainer<SelectValue>)fieldVisitValues);
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
		}
		
		fvrTriggerPoints = (TextArea) binder.buildAndBind("FVR Trigger Points","fvrTriggerPoints",TextArea.class);
		fvrTriggerPoints.setMaxLength(100);
		
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getFVRDetails(bean.getNewIntimationDTO()
						.getIntimationId(), false);
			}
		});
		countFvr = new Label();
		if(bean.getFvrCount() != null){
			
			countFvr.setValue(bean.getFvrCount().toString() + " FVR Reports Already Exists");
		
		}
		
		FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo, fvrTriggerPoints), horizontalLayout);
		horizontalLayout2.setSpacing(true);
		horizontalLayout2.setHeight("200px");
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(fvrTriggerPoints);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
//		btnLayout.setWidth("600px");
//		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		showInPopup(VLayout, dialog);
		
	}
	*/
/*	@SuppressWarnings("unchecked")
	public void buildReferToMedicalApproverLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		reasonForReferring = (TextField) binder.buildAndBind("Reason for Referring to Medical","reasonForRefering",TextField.class);
		reasonForReferring.setMaxLength(100);
		medicalApproverRemarks = (TextArea) binder.buildAndBind("Remarks","medicalApproverRemarks",TextArea.class);
		medicalApproverRemarks.setMaxLength(100);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(reasonForReferring);
		mandatoryFields.add(medicalApproverRemarks);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(reasonForReferring, medicalApproverRemarks), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
		
	}
	*/
	public void buildReferToBilling()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		reasonForReferring  =  (TextField) binder.buildAndBind("Reason for Referring To Billings","reasonForReferringToBilling",TextField.class);
		approvalRemarks = (TextArea) binder.buildAndBind("Financial Approver Remarks","financialApproverRemarks",TextArea.class);
		approvalRemarks.setMaxLength(100);
		
		
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(reasonForReferring);
		mandatoryFields.add(approvalRemarks);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("500px");
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		FormLayout firstForm = new FormLayout(reasonForReferring, approvalRemarks);
		firstForm.setHeight("200px");
		
		VerticalLayout VLayout = new VerticalLayout(firstForm, btnLayout);
		VLayout.setWidth("700px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);
		
	}
	
	/*public void buildInitiateInvestigation(Object fieldVisitValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		allocationTo = (ComboBox) binder.buildAndBind("Allocation To",
				"allocationTo", ComboBox.class);
		allocationTo
				.setContainerDataSource((BeanItemContainer<SelectValue>) fieldVisitValues);
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");

		if (this.bean.getPreauthMedicalDecisionDetails() != null
				&& this.bean.getPreauthMedicalDecisionDetails()
						.getAllocationTo() != null) {

			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails()
					.getAllocationTo());
		}
		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason For Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(100);
		triggerPointsToFocus = (TextArea) binder.buildAndBind(
				"Trigger Points To Focus", "triggerPointsToFocus",
				TextArea.class);
		triggerPointsToFocus.setMaxLength(100);

		viewFVRDetails = new Button("View Investigation Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean != null && bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null) {
					viewDetails.getInvestigationDetails(bean
							.getNewIntimationDTO().getIntimationId(), false);
				}
			}
		});
		countFvr = new Label();
		countFvr.setValue(bean.getInvestigationSize()
				+ " Investigation reports already exists");

		VerticalLayout horizontalLayout = new VerticalLayout(countFvr,
				viewFVRDetails);
		horizontalLayout.setComponentAlignment(countFvr,
				Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(viewFVRDetails,
				Alignment.MIDDLE_CENTER);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);

		HorizontalLayout horizontalLayout2 = new HorizontalLayout(
				new FormLayout(allocationTo, reasonForReferringIV,
						triggerPointsToFocus), horizontalLayout);
		horizontalLayout2.setMargin(true);
		horizontalLayout2.setSpacing(true);

		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(reasonForReferringIV);
		mandatoryFields.add(triggerPointsToFocus);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2,
				btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setHeight("350px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}
	*/
	public void suggestApprovalClick(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);
		
	}
	
	public void setAppropriateValuesToDTOFromProcedure(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values) {
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) values.get("currentSL"))
						.intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto
				.setCoPayPercentageValues((List<String>) values
						.get("copay"));

		// need to implement in new medical listener table
		this.medicalDecisionTableObj
				.addBeanToList(medicalDecisionDto);
	}
	
	
	private void addMedicalDecisionTableFooterListener() {

		this.medicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						bean.getDocumentDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(min));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
							
							bean.getDocumentDetails().setInitialTotalApprovedAmt(min.doubleValue());
							approvedAmtField.setValue(String.valueOf(min));
						}
					}
				});

		this.amountConsideredTable.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 9193355451830325446L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString(medicalDecisionTableObj.dummyField
										.getValue());
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						bean.getDocumentDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String
								.valueOf(min));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
						bean.getDocumentDetails().setInitialTotalApprovedAmt(min.doubleValue());
						}
					}
				});

	}
	
	 public Integer setQueryValues(Long key,Long claimKey){
			
			List<ViewQueryDTO> QuerytableValues = reimbursementService.getQueryDetails(key);
			Hospitals hospitalDetails=null;    
			
			Integer count =0;
			
			Integer sno = 1;
			
			String diagnosisName = reimbursementService.getDiagnosisName(key);
			
			Claim claim = claimService.getClaimByClaimKey(claimKey);
			//need to implement
			if(claim != null){
			Long hospitalKey = claim.getIntimation().getHospital();
			hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			}	
			for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
				viewQueryDTO.setDiagnosis(diagnosisName);
				if(hospitalDetails != null){
					viewQueryDTO.setHospitalName(hospitalDetails.getName());
					viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
				}
				
				viewQueryDTO.setQueryRaised("");       //need to implement
				viewQueryDTO.setQueryRaiseRole("");    //need to implement
				viewQueryDTO.setDesignation("");    //need to implement
				viewQueryDTO.setClaim(claim);
				viewQueryDTO.setSno(sno);
				if(viewQueryDTO.getQueryRaisedDate() != null){
					viewQueryDTO.setQueryRaisedDateStr(SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate()));
				}
				queryDetailsTableObj.addBeanToList(viewQueryDTO);
				sno++;
				count++;
			}	
			
			return count;
		}
	
	 
	 public void disableApprove(Boolean shouldDisable) {
			if(shouldDisable) {
				/*referToSpecialistBtn.setEnabled(true);
				referToMedicalApproverBtn.setEnabled(true);
				initiateInvestigationBtn.setEnabled(true);
				initiateFieldVisitBtn.setEnabled(true);*/
				referCoordinatorBtn.setEnabled(true);
				referToBillingBtn.setEnabled(true);
				queryBtn.setEnabled(true);
				approveBtn.setEnabled(false);
				cancelROD.setEnabled(true);
				rejectBtn.setEnabled(true);
			} else {
				/*referToSpecialistBtn.setEnabled(true);
				referToMedicalApproverBtn.setEnabled(true);
				initiateInvestigationBtn.setEnabled(true);
				initiateFieldVisitBtn.setEnabled(true);*/
				referCoordinatorBtn.setEnabled(true);
				referToBillingBtn.setEnabled(true);
				queryBtn.setEnabled(true);
				approveBtn.setEnabled(true);
				cancelROD.setEnabled(true);
				rejectBtn.setEnabled(true);
			}
		}
	
	private boolean validateSelectedCoPay (Double selectedCoPay)
	{
		if(null != this.bean.getCopay())
		{
			if(selectedCoPay >= this.bean.getCopay())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	
	
//	@Override
	public void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isFVRAssigned, String repName, String repContactNo) {
		if(!isFVRAssigned){
			Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
			values.put("allocationTo", selectValueContainer);
			values.put("fvrAssignTo", fvrAssignTo);
			
			values.put("fvrPriority", fvrPriority);
			
			generateFvrLayout(values);
		}
		else if (isFVRAssigned) {
		
			Map<String, Object> values = new HashMap<String,Object>();
			values.put("allocationTo", selectValueContainer);
			values.put("fvrAssignTo", fvrAssignTo);
			
			values.put("fvrPriority", fvrPriority);
			
			values.put("isFVRAssigned", isFVRAssigned);
			values.put("repName", repName);
			values.put("repContactNo", repContactNo);
			
			generateFvrLayout(values);
		}
		
	}

	public void generateFvrLayout(Object dropDownValues){
		
		if(this.bean.getPreauthDTO().getStatusKey() != null && !this.bean.getPreauthDTO().getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
			 this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setReasonForRefering("");
			 this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setAllocationTo(null);
			 this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
		 }

		 if(!ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getPreauthDTO().getStatusKey())){
			 ViewFVRDTO trgptsDto = null;
			 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
			 for(int i = 1; i<=5;i++){
				 trgptsDto = new ViewFVRDTO();
				 trgptsDto.setRemarks("");
				 trgptsList.add(trgptsDto);
			 }
			 this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
		 }
		 
		   fVRVisit(dropDownValues);
		 
		 if(bean.getPreauthDTO().getIsFvrInitiate()){
				this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				bean.getPreauthDTO().setInitInvDto(null);
			}else{
				bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.FALSE);
			}
		 
		/* this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
		 this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);*/
		// this.claimRequestWizardButtons.buildInitiateParallelFieldVisit(dropDownValues);
		}
	
	public void fVRVisit(final Object fieldVisitValues) {
		submainlayout = new VerticalLayout();
		fvrVertLayout = new VerticalLayout();
		
		select = new OptionGroup();
		select.addStyleName("horizontal");
		select.addItem("yes");
		select.addItem("no");
		select.setItemCaption("yes", "Yes");
		select.setItemCaption("no", "No");
		
		if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
				|| !ReferenceTable.getGMCProductList().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getMulticlaimAvailFlag())
				&& !ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER.equals(bean.getPreauthDTO().getStatusKey())
				&& !ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER.equals(bean.getPreauthDTO().getStatusKey())) {
			select.setValue("yes");
			select.setReadOnly(true);
			select.setEnabled(false);
			bean.getPreauthDTO().setIsFvrInitiate(true);
			bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
			buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.TRUE);
						
		}
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		HorizontalLayout selectlayout = new HorizontalLayout(new Label(
				"Initiate Field Visit Request:"), select);
		selectlayout.setSpacing(true);
		submainlayout.setSpacing(true);
		submainlayout.setMargin(true);
		submainlayout.addComponent(selectlayout);
		submainlayout.setComponentAlignment(selectlayout,
				Alignment.MIDDLE_CENTER);
		
		select.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (select.getValue().equals("yes")) {
					if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails()!= null && bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsFvrIntiated() != null && bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsFvrIntiated().equals(Boolean.FALSE)){
						//buildInitiateFieldVisit(fieldVisitValues, Boolean.TRUE);					
						bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
						buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.TRUE);
					}else{
						showErrorPopUp("FVR request is in process, cannot initiate another request");
						
						Map<String,Object> fieldVisitMap = (Map<String,Object>)fieldVisitValues;
						
						boolean isAssigned = fieldVisitMap.containsKey("isFVRAssigned") &&  (boolean)fieldVisitMap.get("isFVRAssigned") ? true : false;
						
						if(isAssigned){
							String repName = ((Map<String,Object>)fieldVisitValues).get("repName").toString();
						
							String repContactNo = ((Map<String,Object>)fieldVisitValues).get("repContactNo").toString();
							buildAssignedFieldVisitDetailsLayout(repName, repContactNo);
						}
						
					}
					
				} else if (select.getValue().equals("no")) {
					//buildInitiateFieldVisit(fieldVisitValues, Boolean.FALSE);
					buildInitiateParallelFieldVisit(fieldVisitValues, Boolean.FALSE);
					
				}
			}
		});
		
		submainlayout.addComponent(fvrVertLayout);
		submainlayout.setComponentAlignment(fvrVertLayout, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout submitCancelLayout = new HorizontalLayout(getSubmitFVRButtonWithListener(dialog,select), getCancelButton(dialog));
		submainlayout.addComponent(submitCancelLayout);
		submainlayout.setComponentAlignment(submitCancelLayout, Alignment.BOTTOM_CENTER);
		
		
		showFvrInPopup(submainlayout, dialog);
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildInitiateParallelFieldVisit(Object fieldVisitValues, Boolean value) {
		bean.getPreauthDTO().setIsFvrInitiate(value);
		if(value){
			initBinder();
			unbindAndRemoveComponents(dynamicFrmLayout);
			
		    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;
		
			allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
			allocationTo.setContainerDataSource(map.get("allocationTo"));
			allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			allocationTo.setItemCaptionPropertyId("value");
			if(this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getAllocationTo() != null){
				allocationTo.setValue(this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getAllocationTo());
			}
			
			fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
			fvrPriority.setContainerDataSource(map.get("fvrPriority"));
			fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			fvrPriority.setItemCaptionPropertyId("value");
			if(this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getPriority()!= null){
				fvrPriority.setValue(this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getPriority());
			}

			triggerPtsTableObj = triggerPtsTable.get();
			triggerPtsTableObj.init();
			if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
				triggerPtsTableObj.setTableList(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
			}
			viewFVRDetails = new Button("View FVR Details");
			viewFVRDetails.addClickListener(new Button.ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					viewDetails.getFVRDetails(bean.getClaimDTO().getNewIntimationDto()
							.getIntimationId(), false);
				}
			});
			countFvr = new Label();
			if(bean.getPreauthDTO().getFvrCount() != null){
				
				countFvr.setValue(bean.getPreauthDTO().getFvrCount().toString() + " FVR Reports Already Exists");
			
			}
			
			FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
			
			HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
			horizontalLayout2.setSpacing(true);
//			horizontalLayout2.setHeight("px");
			
			mandatoryFields = new ArrayList<Component>();
			
			mandatoryFields.add(allocationTo);
			mandatoryFields.add(fvrPriority);
			showOrHideValidation(false);
			
			if(cmbFVRNotRequiredRemarks != null){
				fvrVertLayout.removeComponent(cmbFVRNotRequiredRemarks);
			}
			unbindField(cmbFVRNotRequiredRemarks);
			
			fvrVertLayout.removeAllComponents();
			
			VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj);
//			VLayout.setWidth("850px");
//			VLayout.setMargin(true);
//			VLayout.setSpacing(true);
			//showInPopup(VLayout, dialog);
			fvrVertLayout.addComponent(VLayout);	
		}else if(!value){

			initBinder();
			unbindAndRemoveComponents(dynamicFrmLayout);
			
			referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.FVR_NOT_REQUIRED_REMARKS));
			
			cmbFVRNotRequiredRemarks = (ComboBox) binder.buildAndBind(
					"FVR Not Required Remarks", "fvrNotRequiredRemarks",
					ComboBox.class);
			
			/**
			* Requirement Number:R1118
			* For adding Others as additional LOV and text area in FVR Not Required Remarks
			* Modified By : GokulPrasath.A
			* Modified On : 07th MAR 2018
			*
			*/
			fvrOthersRemarks = (TextArea) binder.buildAndBind(
					"Enter remarks for Others", "fvrNotRequiredOthersRemarks",
					TextArea.class);
			fvrOthersRemarks.setHeight("70%");
			fvrOthersRemarks.setMaxLength(230);
			fvrOthersRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
			fvrOthersRemarks.setData(bean);
			handleTextAreaPopup(fvrOthersRemarks,null);
			
			
			BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
					.get("fvrNotRequiredRemarks");

			cmbFVRNotRequiredRemarks
					.setContainerDataSource(fvrNotRequiredRemarks);
			cmbFVRNotRequiredRemarks
					.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFVRNotRequiredRemarks.setItemCaptionPropertyId("value");
			
			if (this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks() != null) {
				cmbFVRNotRequiredRemarks.setValue(this.bean.
						getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks());
			}
			cmbFVRNotRequiredRemarks.setData(bean);
			mandatoryFields = new ArrayList<Component>();
			mandatoryFields.add(cmbFVRNotRequiredRemarks);
			mandatoryFields.add(fvrOthersRemarks);
			showOrHideValidation(false);
			
			unbindField(allocationTo);
			unbindField(fvrPriority);
			
			if(triggerPtsTableObj != null){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			
			if(null != allocationTo){
				fvrVertLayout.removeComponent(allocationTo);
			}
			
			if(null != fvrPriority) {
				fvrVertLayout.removeComponent(fvrPriority);
			}
			
			fvrVertLayout.removeAllComponents();
			
			final VerticalLayout VLayout = new VerticalLayout(cmbFVRNotRequiredRemarks);
			
			/**
			* Requirement Number:R1118
			* For adding Others as additional LOV and text area in FVR Not Required Remarks
			* Modified By : GokulPrasath.A
			* Modified On : 07th MAR 2018
			*
			*/
			cmbFVRNotRequiredRemarks.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 2697682747976915503L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(value != null && ((SelectValue)cmbFVRNotRequiredRemarks.getValue()).getValue().equalsIgnoreCase("others")) {
						if(fvrOthersRemarks != null){
							/*fvrOthersRemarks.setValue(StringUtils.EMPTY);*/
							VLayout.removeComponent(fvrOthersRemarks);
						}
						VLayout.addComponent(fvrOthersRemarks);
						
					} else {
						fvrOthersRemarks.setValue(StringUtils.EMPTY);
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(null);
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrNotRequiredRemarks(value);
						VLayout.removeComponent(fvrOthersRemarks);
					}
					
					
				}
			});
			
			fvrVertLayout.addComponent(VLayout);
		
		}
	}

	public void buildAssignedFieldVisitDetailsLayout(String repName, String repContactNo) {
		unbindAndRemoveComponents(dynamicFrmLayout);
			
		if(triggerPtsTableObj != null){
			fvrVertLayout.removeComponent(triggerPtsTableObj);
		}
		
		if(null != allocationTo){
			fvrVertLayout.removeComponent(allocationTo);
		}
		
		if(null != fvrPriority) {
			fvrVertLayout.removeComponent(fvrPriority);
		}
		
		fvrVertLayout.removeAllComponents();
		
		FormLayout fvrAssignFLayout = new FormLayout();
		
		Label repNameLbl = new Label("Representative Name :  <b>"+ repName + "</b>",ContentMode.HTML);
		Label repContactNoLbl = new Label("Contact No :  <b>"+ repContactNo + "</b>",ContentMode.HTML);
		
		fvrAssignFLayout.addComponents(repNameLbl, repContactNoLbl);
		
		fvrVertLayout.addComponent(fvrAssignFLayout);

	}
	
	private void showErrorPopUp(String eMsg) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	private Button getSubmitFVRButtonWithListener(final ConfirmDialog dialog,final OptionGroup select) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				//if (isValid()) {
					if (isValidFvr()) {
						
						fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MEDICAL_REQUEST_AUTO_SKIP_FVR,bean.getPreauthDTO());
						
						if(null != select && null != select && ("yes".equals(select.getValue()))){
							bean.getPreauthDTO().setIsFVRAlertOpened(Boolean.TRUE);
							bean.getPreauthDTO().setIsFvrClicked(Boolean.TRUE);
							reimbService.initiateFVR(bean.getPreauthDTO(),SHAConstants.CLAIM_REQUEST);				
							bean.getPreauthDTO().getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);
							bean.getPreauthDTO().setIsParallelInvFvrQuery(Boolean.TRUE);
							bean.getDocumentDetails().setStatusId(ReferenceTable.INITITATE_FVR);
							buildFVRSuccessLayout();
							dialog.close();

							ViewFVRDTO trgptsDto = null;
							List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
							for(int i = 1; i<=5;i++){
								trgptsDto = new ViewFVRDTO();
								trgptsDto.setRemarks("");
								trgptsList.add(trgptsDto);
							}
							bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
							bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setAllocationTo(null);
							bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setPriority(null);
						}
						else
						{
							bean.getPreauthDTO().setIsFVRAlertOpened(Boolean.TRUE);
//							bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFvrNotRequiredRemarks().setId());
							reimbService.updateFVRNotRequiredDetails(bean.getPreauthDTO());
							dialog.close();
						}
					 
						bean.getPreauthDTO().setIsFvrClicked(Boolean.TRUE);
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error);
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		return submitButton;
	}

	private void showFvrInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(false);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

	}
	
//	private void initBinderForMA() {
//		this.binderMA = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
//				PreauthMedicalDecisionDTO.class);
//		this.binderMA.setItemDataSource(bean.getPreauthDTO().getPreauthMedicalDecisionDetails());
//		binderMA.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//	}
	
	
	public void buildFVRSuccessLayout() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("<b style = 'color: green;'>FVR has been initiated successfully!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
							
				if(null != bean.getStageKey() && bean.getStageKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE))
				{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
					Object[] winArray = windows.toArray();
					for(int i = 0; i < winArray.length;i++){
						((Window)winArray[i]).close();
					}
				}
				//dialog.close();

				toolBar.countTool();

			}
		});
		
	}
	
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("FVR Not Required Remarks For Others",KeyCodes.KEY_F8,null) {

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
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(230);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.getPreauthMedicalDecisionDetails().setFvrNotRequiredOthersRemarks(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "FVR Not Required Remarks For Others";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
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
	
	public boolean isValidFvr() {
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());

		if (this.binder == null) {
			hasError = true;
			errorMessages
					.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}

		if(bean.getPreauthDTO().getIsFvrInitiate()){
			if(triggerPtsTable != null){
				hasError = !triggerPtsTableObj.isValid();
				if(hasError){
					errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
					errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
				}
				else{
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
			}
		}
		else{
			
			
			if(cmbFVRNotRequiredRemarks.getValue() == null)
			{
				hasError = true;
				errorMessages.add("Please Choose FVR Not Required Remarks.</br>");
			}
			else if(null != cmbFVRNotRequiredRemarks.getValue() && cmbFVRNotRequiredRemarks.getValue().toString().equalsIgnoreCase("Others")) 
			{
				if(fvrOthersRemarks.getValue() == null || ("").equalsIgnoreCase(fvrOthersRemarks.getValue()) || 
						fvrOthersRemarks.getValue().isEmpty())	
				{
					hasError = true;
					errorMessages.add("Please Enter Remarks for Others.</br>");
				}
			}
		}
//		if(cmbFVRNotRequiredRemarks.getValue() != null && ! cmbFVRNotRequiredRemarks.isEmpty()){
////			bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrNotRequiredRemarks(cmbFVRNotRequiredRemarks.getId());
//		}
		if (!this.binder.isValid()) {
			hasError = true;
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					errorMessages.add(errMsg.getFormattedHtmlMessage());
				}
			}
		} else {
			try {
				
				if(bean.getPreauthDTO().getIsFvrInitiate()){
					bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
					bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				}
				
				this.binder.commit();
				if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
					if(triggerPtsTableObj != null){
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
					 }
				}
//				if (this.bean
//						.getStatusKey()
//						.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
////					this.investigationReportReviewedChk.setEnabled(true);
//				} else {
////					this.investigationReportReviewedChk.setValue(false);
////					this.investigationReportReviewedChk.setEnabled(false);
//				}

			} catch (CommitException e) {
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
	
	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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
	
	public Boolean alertMessage(String message, final Integer count) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				generateQueryDetails(count);
			}
		});
		return true;
	}
	
	private Button getQuerySubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (isValidQuery()) {
					//wrongly merged to production build
//					bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
//					bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
					isApproval = true;
					if(queryRemarksTxta != null){
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setQueryRemarks(queryRemarksTxta.getValue());
					}
					
					if(remarksFromDeptHead != null){
						bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(remarksFromDeptHead.getValue());
					}
					
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.SUBMIT_PARALLEL_BENEFIT_QUERY,bean.getPreauthDTO());
					bean.getPreauthDTO().setIsParallelInvFvrQuery(Boolean.TRUE);
					bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
					buildQuerySuccessLayout();
					dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error);
					}
					showErrorPopup(eMsg.toString());
				}
			}
		});
		return submitButton;
	}
	
	public void buildQuerySuccessLayout() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Query has been initiated successfully!!!", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
		
	}
	
	private void addViewAllDocsListener()
	{
		if(null != viewClaimsDMSDocument)
		{
			if(null != viewClaimsDMSDocument)
			{
				viewClaimsDMSDocument.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					@SuppressWarnings("deprecation")
					public void buttonClick(ClickEvent event) {
						
						BPMClientContext bpmClientContext = new BPMClientContext();
						Map<String,String> tokenInputs = new HashMap<String, String>();
						 tokenInputs.put("intimationNo", bean.getNewIntimationDTO().getIntimationId());
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
						String url = bpmClientContext.getGalaxyDMSUrl() +intimationNoToken;
						/*Below code commented due to security reason
						String url = bpmClientContext.getGalaxyDMSUrl() +bean.getNewIntimationDTO().getIntimationId();*/
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
					}
				});
			}
		}
	}
	
//	public FormLayout buildUserRoleLayout(){
//		String enhancementType = "F";
//		String hospitilizationType = "N";
//		if(bean.getPreauthDTO().getHospitalizaionFlag() || bean.getPreauthDTO().getPartialHospitalizaionFlag() || bean.getPreauthDTO().getIsHospitalizationRepeat()){
//			hospitilizationType = "Y";
//		}
//		//R1295
//		Integer qryTyp;
//		if (bean.getPreauthDTO().getQueryType() == null){
//			qryTyp = 0;
//		}else{
//			qryTyp = bean.getPreauthDTO().getQueryType().getId().intValue();
//		}
//		Integer qryCnt;
//		if (bean.getPreauthDTO().getQueryCount() == null){
//			qryCnt = 0;
//		}else{
//			qryCnt = bean.getPreauthDTO().getQueryCount() + 1;
//		}
//	
//		String reconsiderationFlag = null != bean.getPreauthDTO().getIsRejectReconsidered() && bean.getPreauthDTO().getIsRejectReconsidered() ? "Y" : "N";
//		String finalClaimAmount = "";
////		if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
////			finalClaimAmount = bean.getPreauthDTO().getAmountConsidered();
////		}else{
//			finalClaimAmount = String.valueOf(createRodService.getTotalClaimedAmt(bean.getPreauthDTO().getKey(), ReferenceTable.HOSPITALIZATION).longValue());
////		}
//		finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
//		bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFinalClaimAmout(Long.valueOf(finalClaimAmount));
//
//		System.out.println("CRMD User Role For Intimation No "+bean.getClaimDTO().getNewIntimationDto().getIntimationId());
//	
//		Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getPreauthDTO().getStageKey(),bean.getPreauthDTO().getStatusKey(),
//				Long.valueOf(bean.getClaimDTO().getNewIntimationDto().getCpuCode()),reconsiderationFlag,bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType,hospitilizationType,qryTyp,qryCnt,
//				bean.getClaimDTO().getClaimType().getId(),bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey(),SHAConstants.CLAIM_REQUEST,
//				bean.getKey()); 
//		
//		BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
//		userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));
//		
//		BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
//		empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));
//		
//			if(null != opinionValues){			
//				String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
//				if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
//					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
//				}
//				else{
//					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
//				}
//				
//				String portedFlag =  (String) opinionValues.get("portedFlag");
//				if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(portedFlag)){
//					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.TRUE);
//				}else{
//					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.FALSE);
//				}
//			}
//			cmbUserRoleMulti = new ComboBoxMultiselect("Consulted With");
//			cmbUserRoleMulti.setShowSelectedOnTop(true);
//			cmbUserRoleMulti.setContainerDataSource(userRole);
//			cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			cmbUserRoleMulti.setItemCaptionPropertyId("value");	
//			cmbUserRoleMulti.setData(userRole);
//			
//					
//			cmbDoctorNameMulti = new ComboBoxMultiselect("Opinion Given by");
//			cmbDoctorNameMulti.setShowSelectedOnTop(true);
//			cmbDoctorNameMulti.setContainerDataSource(empNames);
//			cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
//			cmbDoctorNameMulti.setData(empNames);
//	
//			remarksFromDeptHead = new TextArea("Opinion Given");
//			remarksFromDeptHead.setMaxLength(2000);
//			remarksFromDeptHead.setWidth("400px");
//			SHAUtils.handleTextAreaPopup(remarksFromDeptHead,null,getUI());
//			remarksFromDeptHead.setData(bean);
//			
//			addUserRoleListener();
//			FormLayout fLayout = new FormLayout();
//			fLayout.addComponents(cmbUserRoleMulti,cmbDoctorNameMulti,remarksFromDeptHead);
//			fLayout.setMargin(Boolean.TRUE);
//			fLayout.setSpacing(Boolean.TRUE);
//			if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsMandatory()){
//				mandatoryFields.add(cmbUserRoleMulti);
//				mandatoryFields.add(cmbDoctorNameMulti);
//				mandatoryFields.add(remarksFromDeptHead);
//				showOrHideValidation(false);
//			}
//			else{
//				mandatoryFields.remove(cmbUserRoleMulti);
//				mandatoryFields.remove(cmbDoctorNameMulti);
//				mandatoryFields.remove(remarksFromDeptHead);
//			}
//			return fLayout;	
//		}
	
	public void addUserRoleListener(){
		getRoleValidationContainer().clear();
		getUserValidationContainer().clear();
		cmbUserRoleMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
	
				PreauthMedicalDecisionDTO dtoObject = bean.getPreauthDTO().getPreauthMedicalDecisionDetails();
				dtoObject.setUserRoleMulti(null);
				dtoObject.setUserRoleMulti(event.getProperty().getValue());
	
				if(cmbDoctorNameMulti != null && cmbDoctorNameMulti.getData() != null){
					BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
					List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
					BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)cmbUserRoleMulti.getData();
					getRoleValidationContainer().clear();
					List<String> roles = new ArrayList<String>();
					List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
					for (SpecialSelectValue specialSelectValue : listOfRoles) {
						if(null != docList && !docList.isEmpty() &&docList.contains(specialSelectValue.getValue())){
							roles.add(specialSelectValue.getSpecialValue());
							if(!getRoleValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
								getRoleValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
							}
						}
	
					}
					List<SpecialSelectValue> filtersValue = new ArrayList<SpecialSelectValue>();
					List<SpecialSelectValue> itemIds = listOfDoctors.getItemIds();
					for (SpecialSelectValue specialSelectValue : itemIds) {
						if( specialSelectValue.getSpecialValue() != null && 
								roles.contains(specialSelectValue.getSpecialValue())){
							filtersValue.add(specialSelectValue);
						}
					}
	
					BeanItemContainer<SpecialSelectValue> filterContainer = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					filterContainer.addAll(filtersValue);
					cmbDoctorNameMulti.setContainerDataSource(filterContainer);
					cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
				}
			}
		});
	
		cmbDoctorNameMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				PreauthMedicalDecisionDTO dtoObject = bean.getPreauthDTO().getPreauthMedicalDecisionDetails();
				dtoObject.setDoctorName(null);
				dtoObject.setDoctorName(event.getProperty().getValue());
				Set selectedObject = new HashSet<>((Collection) event.getProperty().getValue());
				getUserValidationContainer().clear();
				List<SpecialSelectValue> listOfUserSelected = new ArrayList<SpecialSelectValue>(selectedObject);
				if(listOfUserSelected.size() > 0){
					for(SpecialSelectValue specialSelectValue : listOfUserSelected){
						if(!getUserValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
							getUserValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
						}else{
							String temp = getUserValidationContainer().get(specialSelectValue.getSpecialValue());
							if(temp.contains(specialSelectValue.getValue())){
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(),specialSelectValue.getValue());
							}else{
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(), temp+","+specialSelectValue.getValue());
							}
						}
					}
				}else{
					getUserValidationContainer().clear();
				}
				List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
				BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setDoctorContainer(listOfDoctors);
			}
		});
	}
	
	
	public ParallelInvestigationDetails getRevisedInvestigationDetails(PreauthDTO preathDto, Boolean isDiabled,Long stageKey,Window popup) {
		String intimationNo = preathDto.getNewIntimationDTO().getIntimationId();
		bean.getPreauthDTO().setDirectToAssignInv(preathDto.isDirectToAssignInv());
				Intimation intimation = intimationService
							.searchbyIntimationNo(intimationNo);
					Long claimKey = null;
					if (intimation != null) {
						Claim claim = claimService.getClaimforIntimation(intimation
								.getKey());
						if (null != claim) {
							claimKey = claim.getKey();
						}
					}
					if(null != stageKey && stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE))
					{
						isDiabled = true;
					}
					else
					{
						isDiabled = false;
					}
					
					viewInvestigationDetails.init(isDiabled,bean.getPreauthDTO(),popup,null);
					viewInvestigationDetails.showRevisedValues(claimKey,stageKey,bean.getPreauthDTO());
					InitiateInvestigationDTO initateInvDto = viewInvestigationDetails.getInitateInvDto();
					bean.getPreauthDTO().setInitInvDto(initateInvDto);
					return viewInvestigationDetails;
				}
	
	
public void confirmationForInvestigation() {
	
		
		String message = "Investigation already raised has been completed. </br>Do you want to proceed further?";
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();	
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MEDICAL_REQUEST_INVESTIGATION_EVENT,bean.getPreauthDTO());	
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.getPreauthDTO().setIsInvestigation(false);
			}
		});
	
}


public void alertMessageForInvestigation() {
	
	String message = "Investigation Request has already been initiated.</b>";
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createAlertBox(message, buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
			.toString());
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			
		}
	});
	
   }

public Boolean alertMessageForPEDWatchList() {

	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
			.createAlertBox(SHAConstants.PED_WATCHLIST, buttonsNamewithType);
	Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
			.toString());
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			//dialog.close();
			bean.getPreauthDTO().setIsPEDInitiated(false);
		}
	});
	return true;
 }


public void submitQueryEvent(){

	if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
			&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//		Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
		Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);										
		bean.getPreauthDTO().setIsPedWatchList(taskAvailableInWatchListForIntimation);
		
	}else{
		bean.getPreauthDTO().setIsPedWatchList(false);
	}
	
	if(! bean.getPreauthDTO().getIsPedWatchList()){
		
		ReimbursementQuery reimbQuery = reimbService.getReplyNotReceivedQueryDetails(bean.getKey());
		
		if(null != reimbQuery){
			
			alertMessageForQuery();
		}
		else
		{					
			if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus() == null) {
				
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_QUERY_EVENT,null);
			}
			else {
				
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_QUERY_EVENT,null);
			}
		}
	}else{
		 alertMessageForPEDWatchList();
	}

}

public void alertMessageForQuery() {
	
	String message = "Query Request has already been initiated and it is in process. </br> You can not initiate another query until reply is received for this current Query.</b>";
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

public void alertForAdditionalFvrTriggerPoints(final String action) {	 
	 
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Add Additional FVR Points");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.ADDTIONAL_FVR_TRIGGER_POINTS_ALERT, buttonsNamewithType);
		Button addAdditionalFvrButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
				.toString());
		addAdditionalFvrButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				final Window popup = new com.vaadin.ui.Window();
				addAdditionalFvrPointsPageUI.init(bean.getPreauthDTO(),popup);
				popup.setWidth("85%");
				popup.setHeight("60%");
				popup.setContent(addAdditionalFvrPointsPageUI);
				popup.setCaption("Add Additional FVR Trigger Points");
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
						//dialog.close();
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				
				if(SHAConstants.QUERY.equalsIgnoreCase(action)){
					submitQueryEvent();
				}
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				if(SHAConstants.QUERY.equalsIgnoreCase(action)){
					submitQueryEvent();
				}
			}
		});
		
	}

private void cancelRodAction(){
	//	isApproval = true;
	bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
	bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setUserClickAction("Cancel ROD");
	List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getPreauthDTO().getKey(),
			SHAConstants.MA_Q,bean.getClaimDTO().getNewIntimationDto().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getPreauthDTO().getClaimDTO().getLatestPreauthKey() : 0L,
					bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());

	if(null != pendingFvrInvsQueryList && !pendingFvrInvsQueryList.isEmpty()){

		showPendingFvrInvsQueryPopup(bean.getPreauthDTO(),pendingFvrInvsQueryList,SHAConstants.MEDICAL_APPROVAL_BENEFITS_CLAIM_REQUEST,SHAConstants.CANCEL_ROD);

	}
	else{

		if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			//				Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
			Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
			bean.getPreauthDTO().setIsPedWatchList(taskAvailableInWatchListForIntimation);
		}else{
			bean.getPreauthDTO().setIsPedWatchList(false);
		}

		if(! bean.getPreauthDTO().getIsPedWatchList()){
			fireViewEvent(
					ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_CANCEL_ROD,
					null);
		}else{
			alertMessageForPEDWatchList();
		}
	}
}

public void showPendingFvrInvsQueryPopup(final PreauthDTO bean,List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList,String screenName,String event){

	final Window popup = new com.vaadin.ui.Window();
	
		invesFvrQueryPendingPageUI.init(pendingFvrInvsQueryList, bean, popup,screenName,event);
	
		popup.setWidth("85%");
		popup.setHeight("60%");
		popup.setContent(invesFvrQueryPendingPageUI);
		popup.setClosable(false);
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

@SuppressWarnings("unchecked")
public void  builtCancelRODLayout(){
	initBinder();
//	unbindAndRemoveComponents(userLayout);
	unbindAndRemoveComponents(dynamicFrmLayout);		
	referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
	reasonForCancelRodCmb=(ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
	cancellationRemarks =(TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
	cancellationRemarks.setMaxLength(4000);
	cancellationRemarks.setWidth("400px");
	cancellationRemarks.setHeight("200px");
	
	BeanItemContainer<SelectValue> cancellationReason = (BeanItemContainer<SelectValue>) referenceData.get("cancellationReason");
	reasonForCancelRodCmb.setContainerDataSource(cancellationReason);
	reasonForCancelRodCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	reasonForCancelRodCmb.setItemCaptionPropertyId("value");
	
	/*dynamicGenarateLayout.addComponent(reasonForCancelRodCmb);
	dynamicGenarateLayout.addComponent(cancellationRemarks);*/
	alignFormComponents();
	wholeVlayout.addComponent(dynamicFrmLayout);
	mandatoryFields = new ArrayList<Component>();
	mandatoryFields.add(cancellationRemarks);
	mandatoryFields.add(reasonForCancelRodCmb);
	showOrHideValidation(false);
	final ConfirmDialog dialog = new ConfirmDialog();
	Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

	HorizontalLayout btnLayout = new HorizontalLayout(
			submitButtonWithListener, getCancelButton(dialog));
	btnLayout.setWidth("400px");
	btnLayout.setComponentAlignment(submitButtonWithListener,
			Alignment.MIDDLE_CENTER);
	btnLayout.setSpacing(true);
	showOrHideValidation(false);
//	userLayout = buildUserRoleLayout();
	dynamicFrmLayout = new FormLayout(reasonForCancelRodCmb,cancellationRemarks);		
	HorizontalLayout hLayout = new HorizontalLayout();
	hLayout.addComponents(dynamicFrmLayout);
	VerticalLayout VLayout = new VerticalLayout(hLayout, btnLayout);		
	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	//VLayout.setWidth("800px");
	VLayout.setMargin(true);
	VLayout.setSpacing(true);
	isCancelRodLayout = Boolean.TRUE;
	showInPopup(VLayout, dialog);
}

@SuppressWarnings("unchecked")
public void buildRejectLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject) {
	initBinder();
	unbindAndRemoveComponents(dynamicFrmLayout);

	// addingSentToCPUFields();
	this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
	this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
	if(!bean.getPreauthDTO().getIsFvrInitiate()){
		bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
	 }
	 rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
	 rejectionCategoryCmb.setWidth("240px");
	 
	 rejectionSubCategoryCmb = (ComboBox) binder.buildAndBind("Rejection sub category","rejSubCategory",ComboBox.class);
	 rejectionSubCategoryCmb.setWidth("240px");
	 		 
	 txtDefinedLimit = (TextField) binder.buildAndBind("Amount to be considered for defined limit","definedLimit",TextField.class);
	 
	 BeanItemContainer<SelectValue> dropDownValues = (BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues;
		
		BeanItemContainer<SelectValue> filterValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Product product = bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct();
		
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				((bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() == null) || (bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan() != null && bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))) {
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else{
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if((! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)) && (! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT))){
					filterValues.addBean(selectValue);
				}
			}
		}
		
	 rejectionCategoryCmb.setContainerDataSource(filterValues);
	 rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 rejectionCategoryCmb.setItemCaptionPropertyId("value");

	 rejectionCategoryCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;

				if(value != null 
						&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
					
					rejectionSubCategoryCmb.setVisible(true);
					rejectionSubCategoryCmb.setRequired(true);
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.MA_BENEFIT_SUB_REJECT_CATEG_LAYOUT, value.getId());
				}
			}
	 });

	 if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
		 for(int ind = 0; ind < dropDownValues.getItemIds().size(); ind++){
			if(dropDownValues.getIdByIndex(ind).getId().equals(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejectionCategory().getId())){
				rejectionCategoryCmb.setValue(dropDownValues.getIdByIndex(ind));
			 }
		 }		 
	 }
	 
	rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks", "rejectionRemarks", TextArea.class);
	rejectionRemarksTxta.setMaxLength(4000);

	rejectionRemarksTxta.setWidth("270px");

	dynamicFrmLayout.addComponent(rejectionRemarksTxta);
	alignFormComponents();
	wholeVlayout.addComponent(dynamicFrmLayout);
	
	mandatoryFields = new ArrayList<Component>();
	mandatoryFields.add(rejectionCategoryCmb);
	mandatoryFields.add(rejectionRemarksTxta);
	
	if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			
			if(ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejectionCategory().getId())) {
					
				rejectionSubCategoryCmb.setVisible(true);
				mandatoryFields.add(rejectionSubCategoryCmb);
				showOrHideValidation(false);
			}
			else {
				rejectionSubCategoryCmb.setVisible(false);
				mandatoryFields.remove(rejectionSubCategoryCmb);
			}
			rejectionCategoryCmb.setValue(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejectionCategory());
	}
	else {
		rejectionSubCategoryCmb.setVisible(false);
		mandatoryFields.remove(rejectionSubCategoryCmb);
	}
	
	showOrHideValidation(false);

	final ConfirmDialog dialog = new ConfirmDialog();
	Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

	HorizontalLayout btnLayout = new HorizontalLayout(
			submitButtonWithListener, getCancelButton(dialog));
	btnLayout.setWidth("400px");
	btnLayout.setComponentAlignment(submitButtonWithListener,
			Alignment.MIDDLE_CENTER);
	btnLayout.setSpacing(true);
	showOrHideValidation(false);
	

	if (viewAllDocsLayout != null
			&& viewAllDocsLayout.getComponentCount() > 0) {
		viewAllDocsLayout.removeAllComponents();
	}
	
	viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
//	viewAllDocsLayout.setComponentAlignment(viewClaimsDMSDocument, Alignment.TOP_RIGHT);
	if(isDefinedLimitReject)
	{
//		dynamicFrmLayout.addComponents(rejectionCategoryCmb,txtDefinedLimit,rejectionRemarksTxta);
		dynamicFrmLayout.addComponents(rejectionCategoryCmb,rejectionSubCategoryCmb,txtDefinedLimit,rejectionRemarksTxta);
		
	}
	else
	{
//		dynamicFrmLayout.addComponents(rejectionCategoryCmb,rejectionRemarksTxta);
		dynamicFrmLayout.addComponents(rejectionCategoryCmb,rejectionSubCategoryCmb,rejectionRemarksTxta);
		
	}

//	userLayout = buildUserRoleLayout();
	//R1256
	HorizontalLayout evidenceHolderlayout = buildEvidenceObtainedFrom();
	evidenceHolderlayout.setMargin(false);
	FormLayout evidenceFL = new FormLayout(evidenceHolderlayout);
	evidenceFL.setCaption("<b style='font-size: 14.5px;'>Evidence Obtained From<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b> </b>"); //b>Search By<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b>
	evidenceFL.setCaptionAsHtml(true);
	
	HorizontalLayout hLayout = new HorizontalLayout();
	hLayout.addComponents(dynamicFrmLayout);
	VerticalLayout vLayout = new VerticalLayout(viewAllDocsLayout,hLayout, evidenceFL, btnLayout);			
	
	userLayout.setMargin(Boolean.FALSE);
	//VerticalLayout VLayout = new VerticalLayout(viewAllDocsLayout,dynamicFrmLayout,txtDefinedLimit, btnLayout);
	vLayout.setComponentAlignment(viewAllDocsLayout, Alignment.BOTTOM_RIGHT);
	vLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
//	VLayout.setWidth("400px");
	vLayout.setMargin(true);
	vLayout.setSpacing(true);
//	addViewAllDocsListener();
	showInPopup(vLayout, dialog);

 }

private HorizontalLayout buildEvidenceObtainedFrom(){

	chkSubDoc = binder.buildAndBind("Submitted Documents",	"chkSubmittedDoc", CheckBox.class);
	//Vaadin8-setImmediate() chkSubDoc.setImmediate(true);

	chkFVR = binder.buildAndBind("Field Visit Report",	"chkFieldVisitReport", CheckBox.class);
	//Vaadin8-setImmediate() chkFVR.setImmediate(true);

	chkIR = binder.buildAndBind("Investigation Report",	"chkInvestigationReport", CheckBox.class);
	//Vaadin8-setImmediate() chkIR.setImmediate(true);

	chkOthers = binder.buildAndBind("Others", "chkOthers", CheckBox.class);
	//Vaadin8-setImmediate() chkOthers.setImmediate(true);

	txtaRemarks = binder.buildAndBind("<b style='font-size: 13.5px;'>Remarks for Others<b style='color:red; font-size: 12.5px; font-weight:600;'> * </b> </b>",	"txtaOthersRemarks", TextArea.class);
	txtaRemarks.setCaptionAsHtml(true);
	txtaRemarks.setId("evidenceRemarks");
	txtaRemarks.setMaxLength(4000);
	txtaRemarks.setColumns(17);
	txtaRemarks.setRows(3);
	remarksPopupListener(txtaRemarks,null);
	if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getChkOthers()){
		txtaRemarks.setVisible(true);
	}else{
		txtaRemarks.setVisible(false);
	}

	chkOthers.addValueChangeListener(new Property.ValueChangeListener() {
		private static final long serialVersionUID = 1L;
		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean value = (Boolean) event.getProperty().getValue();
			if(value){
				txtaRemarks.setVisible(true);
			}else{
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setTxtaOthersRemarks(null);
				txtaRemarks.setValue("");
				txtaRemarks.setVisible(false);
			}
		}
	});

	FormLayout fOne = new FormLayout(chkSubDoc, chkIR);
	FormLayout fTwo = new FormLayout(chkFVR, chkOthers);
	FormLayout fThree = new FormLayout(txtaRemarks);

	HorizontalLayout holderL =  new HorizontalLayout(fOne,fTwo,fThree);
	holderL.setSpacing(true);
	return holderL;
}

public  void remarksPopupListener(TextArea searchField, final  Listener listener) {
	
    ShortcutListener enterShortCut = new ShortcutListener(
        "ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
	
      private static final long serialVersionUID = 1L;
      @Override
      public void handleAction(Object sender, Object target) {
        ((ShortcutListener) listener).handleAction(sender, target);
      }
    };
    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForRemarks(searchField));
    
  }

public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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

public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
	
	rejectionSubCategoryCmb.setVisible(true);
	rejectionSubCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejSubcategContainer);
	rejectionSubCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	rejectionSubCategoryCmb.setItemCaptionPropertyId("value");
	
	mandatoryFields.add(rejectionSubCategoryCmb);
	
	if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejSubCategory() != null){
		rejectionSubCategoryCmb.setValue(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getRejSubCategory());
	}

	showOrHideValidation(false);
}


public boolean isValid() {
	boolean hasError = false;
	showOrHideValidation(true);
	errorMessages.removeAll(getErrors());
/*
	if (this.binder == null) {
		hasError = true;
		errorMessages
				.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator or Refer to Bill Entry. </br>");
		return !hasError;
	}*/

	if(bean.getStatusKey().equals(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS)){
		if(bean.getPreauthDTO().getIsFvrInitiate()){
			if(triggerPtsTable != null){
				hasError = !triggerPtsTableObj.isValid();
				if(hasError){
					errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
					errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
				}
				else{
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
			}
		}
	}
	
	Boolean isParallelProcessHappen = Boolean.FALSE;
	
	if(null != bean.getClaimDTO().getClaimType() && 
			bean.getClaimDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE) &&
			ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getPreauthDTO().getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
		isParallelProcessHappen = investigationService.getInvestigationPendingForClaim(bean.getPreauthDTO().getClaimKey(),SHAConstants.TRANSACTION_FLAG_CASHLESS,bean.getPreauthDTO());	
		if(!isParallelProcessHappen){
			isParallelProcessHappen = reimbService.isFvrOrInvesOrQueryInitiated(bean.getPreauthDTO());
		}
	}else{
		isParallelProcessHappen = reimbService.isFvrOrInvesOrQueryInitiated(bean.getPreauthDTO());
	}
	
	if(null != bean.getPreauthDTO().getScreenName() && ((!(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName())) &&
			!isParallelProcessHappen) || (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName()))))
	{
		if (this.binder != null && ! isParallelProcessHappen && ! isApproval) {
			hasError = true;
			errorMessages
			.add("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}
	}
	
	//added for UAT fix
	if(null != bean.getPreauthDTO().getScreenName() && ((!(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName())) &&
			!isParallelProcessHappen) || (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName()))))
	{
		if (this.binder == null) {
			hasError = true;
			errorMessages
			.add("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}
	}
	
	if(null != bean.getPreauthDTO().getScreenName() && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName()))){
		if (this.binder == null ) {
			hasError = true;
			errorMessages
			.add("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}
	}
	if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
		if(triggerPtsTable != null && triggerPtsTableObj != null){
			hasError = !triggerPtsTableObj.isValid();
			if(hasError){
				errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
				errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
			}
			
		}
	}		
	if (null != binder && !this.binder.isValid()) {
		hasError = true;
		for (Field<?> field : this.binder.getFields()) {
			ErrorMessage errMsg = ((AbstractField<?>) field)
					.getErrorMessage();
			if (errMsg != null) {
				errorMessages.add(errMsg.getFormattedHtmlMessage());
			}
		}
	}
	
//	if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsMandatory()){
//		/*if((cmbUserRoleMulti.getValue() == null || cmbUserRoleMulti.isEmpty()) ||(cmbDoctorNameMulti.getValue() == null || cmbDoctorNameMulti.isEmpty()) ||
//			(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue()) || remarksFromDeptHead.getValue().isEmpty())){
//			hasError = true;
//			errorMessages.add(SHAConstants.USER_ROLE_VALIDATION);
//		}*/
//		
//		if(remarksFromDeptHead != null && ((remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue())))){
//			hasError = true;
//			errorMessages.add("Please provide Opinion Given is mandatory"+ "</br>");
//		}
//		
////		R0001	MEDICAL HEAD
////		R0002	UNIT HEAD
////		R0003	DIVISION HEAD
////		R0004	CLUSTER HEAD
////		R0005	SPECIALIST
////		R0006	ZONAL HEAD
////		R00011	MEDICAL HEAD - GMC
////		R00012	DEPUTY MEDICAL HEAD - GMC
//		List<String> roleList = null;
//		Map<String, String> selectedRole = getRoleValidationContainer();
//		Map<String, String> selectedUser = getUserValidationContainer();
//		
//		System.out.println("Role Val :"+selectedRole);
//		System.out.println("User Val :"+selectedUser);
//		
//		if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsPortedPolicy()){
//			System.out.println("This is a Ported Policy.......");
////			if((Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L) && bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
//			if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
//				roleList =  Arrays.asList("R0004", "R0003", "R0001", "R00011", "R00012", "R0007");
//			}
//			System.out.println(bean.getClaimDTO().getNewIntimationDto().getIntimationId()+"<------>"+bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction());
//			List<String> userPortedActionList = Arrays.asList("Cancel ROD");
//			boolean roleAvailabilityFlag = false;
//			if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
//				hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
//			}else if(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction().equals("Reject")){		
//				roleList =  Arrays.asList("R0002", "R0001", "R0007");
//				String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
//				boolean isSeniorDocLogin = masterService.checkSeniorDoctor(loginUserId.toUpperCase());
//				if(!isSeniorDocLogin){
//					hasError = doPortedPolicyRejectionRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
//				}else{
//					hasError = doPPRejectionValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
//				}
//				System.out.println("CRMD Ported Policy Rejection ErrorFlg : "+hasError);
//			}else if(userPortedActionList.contains(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction())){					
//				roleList =  Arrays.asList("R0004", "R0003", "R0001", "R0007");
//				hasError = doPortedPolicyRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
//				System.out.println("CRMD Ported Policy ErrorFlg : "+hasError);
//			}
//			
//		}else{
//
//			if(Long.valueOf(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L){
//				roleList =  Arrays.asList("R0001", "R0002", "R00011", "R00012", "R0007");
//			}else{
//				roleList =  Arrays.asList("R0003", "R0001", "R0002", "R00011", "R00012", "R0007");
//			}
//			
//			System.out.println(bean.getClaimDTO().getNewIntimationDto().getIntimationId()+"<------>"+bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction());
//			
//			boolean roleAvailabilityFlag = false;
//			List<String> userActionList = Arrays.asList("Approve", "Cancel ROD", "Reject");
//			if(userActionList.contains(bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getUserClickAction())){
//				hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
//				System.out.println("CRMD ErrorFlg : "+hasError);
//			}
//		}
//		
//		
//	}else if(!bean.getPreauthDTO().getPreauthMedicalDecisionDetails().getIsMandatory()){
//		String nonMandateerrorMessage = "Please provide Consulted With, Opinion Given by, and Opinion Given or Make Consulted With, Opinion Given by, and Opinion Given as Empty"+"</br>";
//		if(cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null){
//			if((!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}
//
//			if((cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}
//
//			if((cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue()))){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}					
//
//			if(!cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue())){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}
//
//			if(!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}
//
//			if(cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
//				hasError = true;
//				errorMessages.add(nonMandateerrorMessage);
//			}
//		}
//	}
	
	//R1256
	if(bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS)){
		if(chkOthers != null){
			boolean isEOF_SD = chkSubDoc.getValue();
			boolean isEOF_FVR = chkFVR.getValue();
			boolean isEOF_IR = chkIR.getValue();
			boolean isEOF_Others = chkOthers.getValue();
			String isEOF_OthersRmrks = txtaRemarks.getValue();

			if(!isEOF_SD && !isEOF_FVR && !isEOF_IR && !isEOF_Others){
				hasError = true;
				errorMessages.add("Any one of the Evidence Obtained From has to be selected.");
			}

			if(isEOF_Others && StringUtils.isBlank(isEOF_OthersRmrks)){
				hasError = true;
				errorMessages.add("Evidence Obtained From Others Remarks is mandatory.");
			}
		}
		
		SelectValue value = rejectionCategoryCmb != null && rejectionCategoryCmb.getValue() != null ? (SelectValue) rejectionCategoryCmb.getValue() : null;
		if(value != null
				&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
		
			SelectValue rejSubCategvalue = rejectionSubCategoryCmb.getValue() != null ? (SelectValue) rejectionSubCategoryCmb.getValue() : null;
			if(rejSubCategvalue == null) {
				hasError = true;
				errorMessages.add("Please Select Rejection Subcategory.</br>");
			}
		}
	}
	
	if(!hasError) {
		try {
			
			if(null != binder){
				this.binder.commit();
			}
			if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
				if(triggerPtsTable != null && triggerPtsTableObj != null){
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				 }
			}
			if(remarksFromDeptHead != null){
				bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(remarksFromDeptHead.getValue());
			}
//			if (this.bean
//					.getStatusKey()
//					.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
////				this.investigationReportReviewedChk.setEnabled(true);
//			} else {
////				this.investigationReportReviewedChk.setValue(false);
////				this.investigationReportReviewedChk.setEnabled(false);
//			}

		} catch (CommitException e) {
			e.printStackTrace();
		}
	}
	
	showOrHideValidation(false);
	return !hasError;
}


public boolean isValidQuery() {
	boolean hasError = false;
	showOrHideValidation(true);
	errorMessages.removeAll(getErrors());
	
	if(this.bean.getStatusKey() != null && this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
		if(queryRemarksTxta != null && (queryRemarksTxta.getValue() == null) || (queryRemarksTxta.getValue() != null && queryRemarksTxta.getValue().isEmpty())){
			errorMessages.add("Please Enter Query Remarks </br>");
			hasError = true;
		}
		if(cmbQueryType.getValue() == null){
			errorMessages.add("Please Select Query Type </br>");
			hasError = true;
		}
		}
		
	if (!this.binder.isValid()) {
		hasError = true;
		for (Field<?> field : this.binder.getFields()) {
			ErrorMessage errMsg = ((AbstractField<?>) field)
					.getErrorMessage();
			if (errMsg != null) {
				errorMessages.add(errMsg.getFormattedHtmlMessage());
			}
		}
	} else {
		try {
			this.binder.commit();

		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	showOrHideValidation(false);
	return !hasError;
}

private void showInPopupForQuery(Layout layout, ConfirmDialog dialog) {
	Collection<Window> windows = UI.getCurrent().getWindows();
	for (Window window : windows) {
		if(window.getId() != null && window.getId().equalsIgnoreCase("duplicate_popup")){
			window.close();
		}
	}
	dialog.setCaption("");
	dialog.setClosable(true);
	dialog.setId("duplicate_popup");

	Panel panel = new Panel();
	panel.setHeight("600px");
	panel.setWidth("1100px");
	panel.setContent(layout);
	dialog.setContent(panel);
	dialog.setResizable(false);
	dialog.setModal(true);

	dialog.show(getUI().getCurrent(), null, true);

}

public void builtSendReplyToFinancialLayout() {
	initBinder();
	unbindAndRemoveComponents(dynamicFrmLayout);

	referredByRole = new TextField("Referred By-Role");
	referredByRole.setEnabled(false);
	referredByRole.setValue(this.bean.getModifiedBy());

	referredByName = new TextField("Referred By-ID / Name");
	referredByName.setEnabled(false);
	referredByName.setValue(this.bean.getModifiedBy());

	reasonForReferring = new TextField("Reason for reffering to Medical");
	reasonForReferring.setEnabled(false);
	reasonForReferring.setMaxLength(4000);

	remarks = new TextField("Remarks");
	remarks.setEnabled(false);
	remarks.setMaxLength(4000);

	if (this.bean.getPreauthDTO().getIsReferToMedicalApprover()) {
		remarks.setValue(bean.getPreauthDTO().getPreviousRemarks() != null ? bean.getPreauthDTO()
				.getPreviousRemarks() : "");
		reasonForReferring
				.setValue(bean.getPreauthDTO().getPreviousReasonForReferring() != null ? bean.getPreauthDTO()
						.getPreviousReasonForReferring() : "");
	}

	medicalApproversReply = (TextArea) binder.buildAndBind(
			"Medical Approver's Reply", "approverReply", TextArea.class);
	medicalApproversReply.setMaxLength(4000);
	mandatoryFields.add(medicalApproversReply);
	medicalApproversReply.setWidth("400px");

	FormLayout formLayout = new FormLayout(referredByRole,
			reasonForReferring, remarks, medicalApproversReply);
	HorizontalLayout layout = new HorizontalLayout(formLayout,
			new FormLayout(referredByName));
	layout.setSpacing(true);

	final ConfirmDialog dialog = new ConfirmDialog();
	Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

	HorizontalLayout btnLayout = new HorizontalLayout(
			submitButtonWithListener, getCancelButton(dialog));
	btnLayout.setWidth("400px");
	btnLayout.setMargin(true);
	btnLayout.setSpacing(true);
	btnLayout.setComponentAlignment(submitButtonWithListener,
			Alignment.MIDDLE_CENTER);
	showOrHideValidation(false);

	VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	VLayout.setWidth("900px");
	VLayout.setMargin(true);
	VLayout.setSpacing(true);
	showInPopup(VLayout, dialog);
}
}
