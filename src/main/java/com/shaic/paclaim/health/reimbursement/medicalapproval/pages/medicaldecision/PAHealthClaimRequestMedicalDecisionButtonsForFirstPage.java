package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.Wizard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.DMSDocumentViewListenerTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.common.ApprovedTable;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsForBillEntry;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.paclaim.cashless.listenertables.PARevisedMedicalDecisionTable;
import com.shaic.paclaim.health.reimbursement.listenertable.PAHealthBillEntryWizardStep;
import com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPagePresenter;
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
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthClaimRequestMedicalDecisionButtonsForFirstPage extends ViewComponent implements
		Receiver, SucceededListener {
	private static final long serialVersionUID = 7089191150922046819L;

	@Inject
	private PreauthDTO bean;

	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	@Inject
	private Instance<PAHealthBillEntryWizardStep> billEntrywizardStepInstance;
	
	@Inject
	private Instance<ApprovedTable> approvedTableWizardStepInstance;

	@Inject
	private ViewBillSummaryPage viewBillSummary;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private Intimation intimation;
	
	@EJB
	private PreauthService preauthService;

	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;

	@Inject
	private Instance<PAHealthUploadedDocumentsListenerTable> uploadDocumentListenerTable;

	@Inject
	private UploadedDocumentsForBillEntry uploadedDocsTable;

	private PAHealthUploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	@Inject
	private Instance<PARevisedMedicalDecisionTable> medicalDecisionTable;

	@Inject
	private Instance<PAAmountConsideredTable> amountConsideredTableInstance;

	public PAAmountConsideredTable amountConsideredTable;

	private Window popup;

	private File file;

	private PARevisedMedicalDecisionTable medicalDecisionTableObj;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;

	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();

	/*private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private TextArea approvalRemarks;*/

	private Button submitButton;

	public Button cancelButton;

	private Label amountConsidered;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	/*private Button approveBtn;
	private Button queryBtn;
	private Button rejectBtn;*/
	private Button escalateClaimBtn;
	private Button sendReplyBtn;
	private Button referCoordinatorBtn;
	/*private Button initiateFieldVisitBtn;
	private Button initiateInvestigationBtn;*/
	private Button escalteReplyBtn;
	private Button referToSpecialistBtn;
	private Button cancelROD;

	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	private ComboBox rejectionCategoryCmb;
	private TextArea rejectionRemarksTxta;
	private ComboBox reasonForCancelRodCmb;
	private TextArea cancellationRemarks;
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

	/*private ComboBox allocationTo;
	private ComboBox fvrAssignTo;
	private ComboBox fvrPriority;
	
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;

	private TextArea triggerPointsToFocus;
	 */
	private TextArea reasonForReferringIV;

	// Added for query table.

	// private FormLayout dynamicTblLayout;

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

	private Boolean isApproval = false;

	private CheckBox investigationReportReviewedChk;

	private VerticalLayout optionCLayout;

	private TextField nonAllopathicTxt;
	
	
	private BrowserWindowOpener claimsDMSWindow;
	//private BrowserWindowOpener viewUploadDocWindow;
	
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	private Button viewClaimsDMSDocument;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	private Button referToBillEntryBtn;
	
	private TextArea reasonRefBillEntryTxta;

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void initView(PreauthDTO bean, GWizard wizard /*,
			CheckBox investigationReportReviewedChk*/) {
//		this.investigationReportReviewedChk = investigationReportReviewedChk;
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

		HorizontalLayout buttonFirstLayout = new HorizontalLayout(referToBillEntryBtn,sendReplyBtn,referCoordinatorBtn, escalteReplyBtn,
				escalateClaimBtn, referToSpecialistBtn,cancelROD);
				
		buttonFirstLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(buttonFirstLayout);
		verticalLayout.setSpacing(true);
		buttonsHLayout.addComponents(verticalLayout);
		buttonsHLayout.setComponentAlignment(verticalLayout,
				Alignment.MIDDLE_CENTER);
		buttonsHLayout.setSpacing(true);
		wholeVlayout.addComponent(buttonsHLayout);
		dynamicFrmLayout = new FormLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		wholeVlayout.addComponent(dynamicFrmLayout);
		
		if (this.bean.getIsReferToMedicalApprover()) {
			sendReplyBtn.setEnabled(true);
		} else {
			sendReplyBtn.setEnabled(false);
		}
		
		if(this.bean.getIsEscalateReplyEnabled()){
			escalteReplyBtn.setEnabled(true);
		}else{
			escalteReplyBtn.setEnabled(false);
		}
				
		
		if (bean.getIsDishonoured()
				|| (bean.getPreauthDataExtractionDetails().getIllness() != null && bean
						.getPreauthDataExtractionDetails().getIllness().getId()
						.equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))
				|| (null != bean.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo()
						&& !bean.getMaternityFlag() && bean
						.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo().getId()
						.equals(ReferenceTable.MATERNITY_MASTER_ID))) {
			sendReplyBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			escalteReplyBtn.setEnabled(false);
			referToSpecialistBtn.setEnabled(false);
		} else {
			sendReplyBtn.setEnabled(true);
			escalteReplyBtn.setEnabled(true);
			escalateClaimBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToSpecialistBtn.setEnabled(true);
			
			if (this.bean.getIsReferToMedicalApprover()) {
				sendReplyBtn.setEnabled(true);
			} else {
				sendReplyBtn.setEnabled(false);
			}
			
			escalteReplyBtn.setEnabled(this.bean.getIsEscalateReplyEnabled());
			
			if(this.bean.getIsEscalateReplyEnabled()){
				escalteReplyBtn.setEnabled(true);
			}else{
				escalteReplyBtn.setEnabled(false);
			}
			
		}
		
		setButtonDisabledForconsiderforPayment(this.bean.getIsConsiderForPaymentNo());
		
		Map<String, Object> outPutArray = (Map<String, Object>)bean.getDbOutArray();
		String sendReplyStatus = (String) outPutArray.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if(null != sendReplyStatus && (sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY) || sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_BILLING) ||
				sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_FA))){
			sendReplyBtn.setEnabled(true);
		}
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);

		viewAllDocsLayout = new HorizontalLayout();
		
		addViewAllDocsListener();

		setCompositionRoot(wholeVlayout);

	}

	private void addListener() {
		
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
		
		/*approveBtn = new Button("Approve");
		approveBtn.setHeight("-1px");
		//Vaadin8-setImmediate() approveBtn.setImmediate(true);
		approveBtn.addClickListener(new Button.ClickListener() {

			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
				isApproval = true;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,
						null);
				}else{
				    alertMessageForPEDWatchList();
				}
			} else {
				showErrorPageForCancelledPolicy();
			}
			}
		});*/
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
				isApproval = true;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_CANCEL_ROD_EVENT_FOR_FIRST_PAGE,
						null);
				}else{
					 alertMessageForPEDWatchList();
				}
			}
		});

		/*queryBtn = new Button("Query");
		queryBtn.setHeight("-1px");
		//Vaadin8-setImmediate() queryBtn.setImmediate(true);
		queryBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4614951723748846970L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_QUERY_BUTTON_EVENT,
						null);
				}else{
					 alertMessageForPEDWatchList();
				}
			}
		});

		rejectBtn = new Button("Reject");
		rejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() rejectBtn.setImmediate(true);
//		rejectBtn.setEnabled(this.bean.getIsConsiderForPaymentNo());
		rejectBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -4241727763379504532L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_REJECTION_EVENT,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});*/

		escalateClaimBtn = new Button("Escalate Claim");
		escalateClaimBtn.setHeight("-1px");
		//Vaadin8-setImmediate() escalateClaimBtn.setImmediate(true);
		escalateClaimBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_ESCALATE_EVENT_FOR_FIRST_PAGE,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

		escalteReplyBtn = new Button("Escalate Reply");
		escalteReplyBtn.setHeight("-1px");
		//Vaadin8-setImmediate() escalteReplyBtn.setImmediate(true);
		escalteReplyBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);	
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_ESCALATE_REPLY_EVENT_FOR_FIRST_PAGE,
						null);
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
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					//Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_REFERCOORDINATOR_EVENT_FOR_FIRST_PAGE,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

		/*initiateFieldVisitBtn = new Button("Initiate Field Visit");
		initiateFieldVisitBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateFieldVisitBtn.setImmediate(true);
		initiateFieldVisitBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_FIELD_VISIT_EVENT,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

		initiateInvestigationBtn = new Button("Initiate Investigation");
		initiateInvestigationBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateInvestigationBtn.setImmediate(true);
		initiateInvestigationBtn.addClickListener(new Button.ClickListener() {

			
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_INVESTIGATION_EVENT,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});*/

		
		referToSpecialistBtn = new Button("Refer to Specialist");
		referToSpecialistBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToSpecialistBtn.setImmediate(true);
		referToSpecialistBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				//	Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_SPECIALIST_EVENT_FOR_FIRST_PAGE,
						null);
				}else{
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
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					//Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT_FOR_FIRST_PAGE,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

		
		if (this.bean.getIsCancelPolicy()) {
			if (this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}

//			approveBtn.setEnabled(false);
//			queryBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
		}
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCancelPolicyProcess(PreauthDTO bean) {
		this.bean = bean;
		if (this.bean.getIsCancelPolicy()) {
			if (this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
//			approveBtn.setEnabled(false);
//			queryBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
		}

	}

	@SuppressWarnings("unchecked")
	public void buildApproveLayout(Integer amt) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		initialTotalApprovedAmtTxt = (TextField) binder.buildAndBind(
				"Pre-auth Approved Amt", "initialTotalApprovedAmt",
				TextField.class);
		initialTotalApprovedAmtTxt.setNullRepresentation("");
		initialTotalApprovedAmtTxt.setEnabled(false);
		initialTotalApprovedAmtTxt.setValue(amt.toString());

		approvalRemarksTxta = (TextArea) binder.buildAndBind(
				"Approval Remarks", "approvalRemarks", TextArea.class);
		approvalRemarksTxta.setMaxLength(100);
		addingSentToCPUFields();
		dynamicFrmLayout.addComponents(initialTotalApprovedAmtTxt,
				approvalRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		wholeVlayout.addComponent(dynamicFrmLayout);
		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(initialApprovedAmtTxt);
		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);
	}
	
	private void createNonAllopathicFields(Double originalAmt, Double utilizedAmt) {
		if(optionCLayout == null || (optionCLayout != null && optionCLayout.getComponentCount() == 1)) {
			nonAllopathicTxt = new TextField("Non-Allopathic Avail Amt");
			nonAllopathicTxt.setWidth("80px");
			Double availAmt = originalAmt - utilizedAmt;
			nonAllopathicTxt.setValue(String.valueOf(availAmt.intValue()) );
			nonAllopathicTxt.setEnabled(false);
			bean.setNonAllopathicAvailAmt(availAmt.intValue());
			bean.setNonAllopathicOriginalAmt(originalAmt);
			bean.setNonAllopathicUtilizedAmt(utilizedAmt);
			
			Map<String, Object> values = new HashMap<String, Object>();
			values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, originalAmt);
			values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilizedAmt);
			Button viewBtn = new Button("View");
			viewBtn.setData(values);
			viewBtn.setStyleName("link");
			viewBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 9127517383717464157L;

				@Override
				public void buttonClick(ClickEvent event) {
					Map<String, Object> values = (Map<String, Object>) event.getButton().getData();
					
					TextField originalAmt = new TextField("Total Original Amt");
					originalAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT)).intValue()));
					originalAmt.setReadOnly(true);
					originalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					TextField utilizedAmt = new TextField("Utilized Amt");
					utilizedAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT)).intValue()));
					utilizedAmt.setReadOnly(true);
					utilizedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					TextField availableAmt = new TextField("Available Amt");
					Double availAmt = (Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) - (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
					availableAmt.setValue(String.valueOf(availAmt.intValue()) );
					availableAmt.setReadOnly(true);
					availableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Non-Allopathic Details");
					dialog.setClosable(true);
					dialog.setWidth("400px");
					dialog.setResizable(false);
					dialog.setContent(new FormLayout(originalAmt, utilizedAmt, availableAmt));
					dialog.show(getUI().getCurrent(), null, true);
				}
			});
			HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(nonAllopathicTxt), viewBtn);
			horizontalLayout.setSpacing(true);
			optionCLayout.addComponent(horizontalLayout);
			optionCLayout.setSpacing(true);
		}
		
	}

	/*public void generateFieldsForSuggestApproval() {
		initBinder();
		
		if (SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) > 0) {
			Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered())- (SHAUtils.getIntegerFromString(this.bean
							.getPreHospitalisationValue()) + SHAUtils
							.getIntegerFromString(this.bean
									.getPostHospitalisationValue()));
			this.bean.setAmountConsidered(String.valueOf(amount));
			if (!this.bean.getHospitalizaionFlag() && !bean.getLumpSumAmountFlag()) {
				this.bean.setAmountConsidered(String.valueOf(this.bean
						.getHospitalizationAmount()));
			}

		}
		
		this.bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(this.bean.getAmbulanceLimitAmount() != null){
			 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
		      totalAmountConsidered -= this.bean.getAmbulanceLimitAmount();
//		      this.bean.setAmbulanceAmountConsidered(totalAmountConsidered.toString());
		      this.bean.setAmbulanceAmountConsidered(String.valueOf(totalAmountConsidered.intValue()));
			}
			
		}

		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
		amountConsidered.setValue(this.bean.getAmountConsidered());

		approvalRemarksTxta = (TextArea) binder.buildAndBind(
				"Approval Remarks", "approvalRemarks", TextArea.class);
		approvalRemarksTxta.setMaxLength(4000);

		approvalRemarksTxta.setWidth("400px");

//		mandatoryFields.removeAll(mandatoryFields);
//		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);

		showBalanceSumInsured = new Button("View");
		showClaimAmtDetailsBtnDuplicate = new Button("View");
		amountConsideredViewButton = new Button("View");
		showBalanceSumInsured.setStyleName("link");
		showClaimAmtDetailsBtnDuplicate.setStyleName("link");

		amountConsideredViewButton.setStyleName("link");
		amountConsideredViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean,bean.getKey(), true);
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					
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

		showClaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean, bean.getKey(), true);
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);

			}
		});

		showBalanceSumInsured.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewBalanceSumInsured(bean.getNewIntimationDTO()
						.getIntimationId());

			}
		});

		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		this.medicalDecisionTableObj.setWidth("1300px");

		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction </br> Amount",
				ContentMode.HTML), approvedAmtField);
		optionCLayout = new VerticalLayout(approvedFormLayout);

		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showClaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showBalanceSumInsured);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);

		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered), new FormLayout(amountConsideredViewButton));

		addRowsForProcAndDiag();
		addMedicalDecisionTableFooterListener();

		final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				validatePage(dialog);
			}

		});

		addMedicalDecisionTableFooterListener();

		Integer min = Math
				.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
								.getValue()));

		approvedAmtField.setValue(String
				.valueOf(this.medicalDecisionTableObj.dummyField.getValue()));
		if (bean.getNewIntimationDTO() != null) {
			Product product = bean.getNewIntimationDTO().getPolicy()
					.getProduct();
			if (product.getCode() != null
					&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
							.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
							.equalsIgnoreCase(product.getCode())))) {
				min = Math
						.min(amountConsideredTable.getConsideredAmountValue(),
								SHAUtils.getIntegerFromString(this.medicalDecisionTableObj.dummyField
										.getValue()));
				amountConsideredTable.hospApprovedAmountTxt.setValue(min
						.toString());
				approvedAmtField
						.setValue(this.medicalDecisionTableObj.dummyField
								.getValue());
				bean.getPreauthMedicalDecisionDetails()
						.setInitialTotalApprovedAmt(
								amountConsideredTable
										.doSuperSurplusCalculation()
										.doubleValue());
			}
		}

		uploadDocumentListenerTableObj = uploadDocumentListenerTable.get();
		uploadDocumentListenerTableObj
				.initPresenter(SHAConstants.CLAIM_REQUEST);
		uploadDocumentListenerTableObj.init();
		uploadDocumentListenerTableObj.setReferenceData(referenceData);
		Integer i = 1;
		List<UploadDocumentDTO> uploadList = this.bean.getUploadDocumentDTO();
		uploadDocumentListenerTableObj.setTableInfo(uploadList);
		if (null != uploadList && !uploadList.isEmpty())
			for (UploadDocumentDTO uploadDocLayout : uploadList) {
				uploadDocLayout.setSeqNo(i);
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getProduct())
				{
					uploadDocLayout.setProductKey( bean.getNewIntimationDTO().getPolicy()
					.getProduct().getKey());
				}
				if(null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue())
					uploadDocLayout.setSubCoverCode(this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
				if(null != this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
					uploadDocLayout.setDomicillaryFlag(this.bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation());
				this.uploadDocumentListenerTableObj
						.addBeanToList(uploadDocLayout);
				i++;
			}

		// uploadDocumentListenerTableObj.setTableList(this.bean
		// .getUploadDocumentDTO());

		// zonalRemarks = (TextArea) binder.buildAndBind("Dr Remarks (Zonal)",
		// "zonalRemarks", TextArea.class);
		// zonalRemarks.setMaxLength(100);
		// zonalRemarks.setEnabled(false);
		//
		// corporateRemarks = (TextArea)
		// binder.buildAndBind("Dr Remarks (Corporate)", "corporateRemarks",
		// TextArea.class);
		// corporateRemarks.setMaxLength(100);

		addTotalClaimedListener();
		bean.setIsNonAllopathic(bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
		if(bean.getIsNonAllopathic()) {
			createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
		}

		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,
				getCancelButton(dialog));
		btnLayout.setWidth("1200px");
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);

		VerticalLayout layout = new VerticalLayout(
				 amountConsideredLayout,
				this.medicalDecisionTableObj, this.amountConsideredTable,
				new FormLayout(approvalRemarksTxta));
		layout.setSpacing(true);
		layout.setWidth("1200px");
		layout.setMargin(true);

		Wizard wizard = new Wizard();
		
		PAHealthBillEntryWizardStep billEntryWizardStep = billEntrywizardStepInstance.get();
		billEntryWizardStep.init(this.bean);
		billEntryWizardStep.setMedicalDecisiontable(this.medicalDecisionTableObj);
		billEntryWizardStep.setReferenceData(this.referenceData);
		billEntryWizardStep.setComponent(uploadDocumentListenerTableObj, SHAConstants.CLAIM_REQUEST);
		wizard.addStep(billEntryWizardStep, "Bill View");
		ApprovedTable approvedTablewizard = approvedTableWizardStepInstance.get();
		approvedTablewizard.init(this.bean);
		approvedTablewizard.setUIPageForClaimRequest(this, dialog);
		approvedTablewizard.setComponent(layout);
		wizard.addStep(approvedTablewizard, "Approval");
		wizard.getCancelButton().addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 4612172390268743864L;

			@Override
			public void buttonClick(ClickEvent event) {
				binder = null;
				dialog.close();
			}
		});
		dialog.setCaption("");
		dialog.setClosable(false);
		
		Panel panel = new Panel();
//		panel.setHeight("400px");
		panel.setSizeFull();
//		panel.setHeight("100%");
//		panel.setWidth("100%");
		panel.setContent(wizard);
		
		dialog.setContent(panel);
		dialog.setWidth("90%");
		dialog.setHeight("60%");
		dialog.setResizable(true);
		dialog.setModal(true);
		
		dialog.show(getUI().getCurrent(), null, true);
	}*/

	
	public void validatePage(final ConfirmDialog dialog) {
		String eMsg = "";
		
		Boolean hasError = false;
		/*if (!medicalDecisionTableObj.getTotalAmountConsidered().equals(
				SHAUtils.getIntegerFromString(bean
						.getAmountConsidered()))) {
			hasError = true;
			eMsg += "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
		}*/
		if (medicalDecisionTableObj.getTotalAmountConsidered() > (
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))) {
			hasError = true;
			eMsg += "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table</br>";
		}
		
		/*if(medicalDecisionTableObj.getTotalAmountConsidered() < (
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))){
			showErrorPopup("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table</br>");
		}*/
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){
				
				hasError = true;
				eMsg += "Amount Entered against Ambulance charges should be equal";
				
			}
		}
		
		if (isValid() && !hasError) {
			setResidualAmtToDTO();
			bean.getPreauthMedicalDecisionDetails()
					.setMedicalDecisionTableDTO(
							medicalDecisionTableObj.getValues());
			dialog.close();
		} else {
			List<String> errors = getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			}
			showErrorPopup(eMsg);
		}
	}
	
	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj
				.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if (medicalDecisionTableDTO.getDiagOrProcedure() != null
					&& medicalDecisionTableDTO.getDiagOrProcedure().contains(
							"Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean
						.getResidualAmountDTO();
				residualAmountDTO
						.setAmountConsideredAmount(medicalDecisionTableDTO
								.getAmountConsidered() != null ? medicalDecisionTableDTO
								.getAmountConsidered().intValue() : 0d);
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO
						.getNetAmount() != null ? medicalDecisionTableDTO
						.getNetAmount().doubleValue() : 0d);
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d);
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO
						.getCoPayAmount() != null ? medicalDecisionTableDTO
						.getCoPayAmount().doubleValue() : 0d);
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO
						.getMinimumAmount() != null ? medicalDecisionTableDTO
						.getMinimumAmount().doubleValue() : 0);
				residualAmountDTO.setNetApprovedAmount(medicalDecisionTableDTO
						.getMinimumAmount() != null ? medicalDecisionTableDTO
						.getMinimumAmount().doubleValue() : 0);
				if(bean.getIsReverseAllocation()) {
					residualAmountDTO.setNetApprovedAmount(medicalDecisionTableDTO
							.getReverseAllocatedAmt() != null ? medicalDecisionTableDTO
							.getReverseAllocatedAmt().doubleValue() : 0);
				}
				residualAmountDTO.setRemarks(medicalDecisionTableDTO
						.getRemarks());
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
						.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
								.getValue()) : 0d);
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}

/*	private void addRowsForProcAndDiag() {
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		
		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				addDignoisToMedicalDecision(medicalDecisionDTOList, pedValidationTableDTO, false, false);
			}
			
			if(!this.bean.getDeletedDiagnosis().isEmpty()) {
				List<DiagnosisDetailsTableDTO> deletedDiagnosis = this.bean.getDeletedDiagnosis();
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					addDignoisToMedicalDecision(medicalDecisionDTOList, diagnosisDetailsTableDTO, true, true);
				}
			}
			
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, false, false);
			}
			
			List<ProcedureDTO> deletedProcedureList = bean.getDeletedProcedure();
			if(!deletedProcedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : deletedProcedureList) {
					addProcedureToMedicalDecision(medicalDecisionDTOList, procedureDTO, true, true);
				}
			}

			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			
			caluculationInputValues.put("rodKey", this.bean.getKey());

			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = 0d;
			if(null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			 insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(this.bean.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								this.bean.getPolicyDto().getKey());
			}
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			int medicalDecisionSize = 0;
			
			for (DiagnosisProcedureTableDTO medicalDecisionDto : medicalDecisionDTOList) {
				if(! medicalDecisionDto.getIsDeletedOne()){
					medicalDecisionSize++;
				}
			}

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
						caluculationInputValues.put("referenceFlag", "D");
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										medicalDecisionDto.getProcedureDTO()
												.getProcedureName() == null ? 0l
												: (medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName()
														.getId() == null ? 0l
														: medicalDecisionDto
																.getProcedureDTO()
																.getProcedureName()
																.getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", !this.bean.getPreviousPreauthKey().equals(0l) ? this.bean.getPreviousPreauthKey():this.bean.getKey());
					
					if(this.bean.getClaimDTO().getLatestPreauthKey() != null){
				    	caluculationInputValues.put("preauthKey", this.bean.getClaimDTO().getLatestPreauthKey());
				    }
					if((this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (this.bean.getIsHospitalizationRepeat() != null && this.bean.getIsHospitalizationRepeat())) {
						caluculationInputValues.put("preauthKey", 0l);
					}
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC,
							bean.getPreauthDataExtractionDetails()
									.getNatureOfTreatment() != null ? (bean
									.getPreauthDataExtractionDetails()
									.getNatureOfTreatment().getValue()
									.toLowerCase().contains("non") ? true
									: false) : false);
					caluculationInputValues
							.put(SHAConstants.INSURED_KEY, bean
									.getNewIntimationDTO().getInsuredPatient()
									.getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean
							.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimKey());

					bean.setIsNonAllopathic((Boolean) caluculationInputValues
							.get(SHAConstants.IS_NON_ALLOPATHIC));

					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					
					
					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}
						}
						
					}

					fireViewEvent(
							PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUSET_SUM_INSURED_CALCULATION,
							caluculationInputValues, medicalDecisionDto,this.bean);
				}

				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				dto.setIsAmbulanceEnable(false);
				if (this.bean.getResidualAmountDTO() != null
						&& this.bean.getResidualAmountDTO().getKey() != null) {
					ResidualAmountDTO residualAmountDTO = this.bean
							.getResidualAmountDTO();
					dto.setAmountConsidered(residualAmountDTO
							.getAmountConsideredAmount() != null ? residualAmountDTO
							.getAmountConsideredAmount().intValue() : 0);
					dto.setMinimumAmountOfAmtconsideredAndPackAmt(residualAmountDTO.getMinimumAmount() != null ? residualAmountDTO
							.getMinimumAmount().intValue() : 0);
					dto.setCoPayAmount(residualAmountDTO.getCopayAmount() != null ? residualAmountDTO
							.getCopayAmount().intValue() : 0);

					SelectValue value = new SelectValue();
					value.setId(residualAmountDTO.getCopayPercentage() != null ? residualAmountDTO
							.getCopayPercentage().longValue() : 0l);
					value.setValue(residualAmountDTO.getCopayPercentage() != null ? String
							.valueOf(residualAmountDTO.getCopayPercentage()
									.intValue()) : "0");

					dto.setCoPayPercentage(value);
					dto.setNetAmount(residualAmountDTO.getNetAmount() != null ? residualAmountDTO
							.getNetAmount().intValue() : 0);
					dto.setMinimumAmount(residualAmountDTO.getApprovedAmount() != null ? residualAmountDTO
							.getApprovedAmount().intValue() : 0);
					dto.setReverseAllocatedAmt(residualAmountDTO.getNetApprovedAmount() != null ? residualAmountDTO.getNetApprovedAmount().intValue() : 0);
				}
				this.medicalDecisionTableObj.addBeanToList(dto);
			}
		} else {
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			
			caluculationInputValues.put("rodKey", this.bean.getKey());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));
			
			caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
			caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
			caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
			
			DiagnosisProcedureTableDTO residualDTO = null;
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
						diagnosisProcedureTableDTO.setReverseAllocatedAmt(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
					SHAUtils.fillDetailsForUtilForProcedure(caluculationInputValues, diagnosisProcedureTableDTO);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
						diagnosisProcedureTableDTO.setReverseAllocatedAmt(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
					SHAUtils.fillDetailsForUtilizationForDiag(caluculationInputValues, diagnosisProcedureTableDTO);
				}
				
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null || diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					fireViewEvent(
							PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUSET_SUM_INSURED_CALCULATION,
							caluculationInputValues, diagnosisProcedureTableDTO,this.bean);
				} else {
					residualDTO = diagnosisProcedureTableDTO;
				}
				
			}
			if(residualDTO != null) {
				residualDTO.setIsAmbulanceEnable(false);
				this.medicalDecisionTableObj.addBeanToList(residualDTO);
			}
			
			bean.setIsNonAllopathic(bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
	}*/

	/*private void addProcedureToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			ProcedureDTO procedureDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		dto.setProcedureDTO(procedureDTO);
		Boolean isPaymentAvailable = true;
		if (procedureDTO.getConsiderForPaymentFlag() != null) {
			isPaymentAvailable = procedureDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
		} else {
			isPaymentAvailable = false;
			if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1l)) {
				isPaymentAvailable = true;
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		}
			
			if (isPaymentAvailable) {
				if (procedureDTO.getExclusionDetails() != null
						&& procedureDTO.getExclusionDetails()
								.getValue() != null
						&& !procedureDTO.getExclusionDetails()
								.getValue().toString().toLowerCase()
								.equalsIgnoreCase("not applicable")) {
					isPaymentAvailable = false;
				}
			}
			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		if(isZeroApprAmt) {
			dto.setMinimumAmount(0);
			dto.setReverseAllocatedAmt(0);
			dto.setIsPaymentAvailable(false);
		}
		dto.setRestrictionSI("NA");

		dto.setPackageAmt("NA");
		if (procedureDTO.getPackageRate() != null
				&& procedureDTO.getPackageRate() >= 0) {
			dto.setPackageAmt(procedureDTO.getPackageRate().toString());
		}

		if (procedureDTO.getCopay() != null) {
			dto.setCoPayPercentage(procedureDTO.getCopay());
		}

		if (procedureDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					procedureDTO.getSublimitAmount()).toString());
		}

		dto.setAmountConsidered(procedureDTO
				.getAmountConsideredAmount() != null ? procedureDTO
				.getAmountConsideredAmount().intValue() : 0);
		dto.setCoPayAmount(procedureDTO.getCopayAmount() != null ? procedureDTO
				.getCopayAmount().intValue() : 0);
		dto.setMinimumAmountOfAmtconsideredAndPackAmt(procedureDTO.getMinimumAmount() != null ? procedureDTO
				.getMinimumAmount().intValue() : 0);
		dto.setNetAmount(procedureDTO.getNetAmount() != null ? procedureDTO
				.getNetAmount().intValue() : 0);
		dto.setMinimumAmount(procedureDTO.getApprovedAmount() != null ? procedureDTO
				.getApprovedAmount().intValue() : 0);
		dto.setRemarks(procedureDTO.getApprovedRemarks() != null ? procedureDTO
				.getApprovedRemarks() : "");
		dto.setReverseAllocatedAmt(procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount().intValue() : 0);
		if(isDeletedOne) {
			dto.setIsDeletedOne(true);
		}
		
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
		}else{
			dto.setIsAmbChargeApplicable(false);
		}
		
		dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbulanceEnable(true);
		}
		
		medicalDecisionDTOList.add(dto);
	}
	
	
	

	private void addDignoisToMedicalDecision(
			List<DiagnosisProcedureTableDTO> medicalDecisionDTOList,
			DiagnosisDetailsTableDTO pedValidationTableDTO, Boolean isZeroApprAmt, Boolean isDeletedOne) {
		DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
		if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
			Boolean isPaymentAvailable = pedValidationTableDTO
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			if (isPaymentAvailable) {
				List<PedDetailsTableDTO> pedList = pedValidationTableDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

						List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
								.getExclusionAllDetails();
						String paymentFlag = "y";
						for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
							if (null != pedDetailsTableDTO
									.getExclusionDetails()
									&& exclusionDetails
											.getKey()
											.equals(pedDetailsTableDTO
													.getExclusionDetails()
													.getId())) {
								paymentFlag = exclusionDetails
										.getPaymentFlag();
							}
						}

						if (paymentFlag.toLowerCase().equalsIgnoreCase(
								"n")) {
							isPaymentAvailable = false;
							break;
						}
					}
				}
			}

			if (!isPaymentAvailable) {
				dto.setMinimumAmount(0);
				dto.setReverseAllocatedAmt(0);
			}
			dto.setIsPaymentAvailable(isPaymentAvailable);
		} else {
			dto.setIsPaymentAvailable(false);
		}
		
		if(isZeroApprAmt) {
			dto.setMinimumAmount(0);
			dto.setReverseAllocatedAmt(0);
			dto.setIsPaymentAvailable(false);
		}
		if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
			dto.setRestrictionSI("NA");
		} else {
			dto.setRestrictionSI(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSumInsuredRestriction()
							.getValue()).toString());
		}

		List<PedDetailsTableDTO> pedList = pedValidationTableDTO
				.getPedList();
		for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
			if (pedDetailsTableDTO.getCopay() != null) {
				dto.setCoPayPercentage(pedDetailsTableDTO.getCopay());
			}
		}

		if (pedValidationTableDTO.getSublimitName() == null) {
			dto.setSubLimitAmount("NA");
		} else {
			dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
					pedValidationTableDTO.getSublimitAmt()).toString());
		}
		dto.setPackageAmt("NA");
		dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
		dto.setAmountConsidered(pedValidationTableDTO
				.getAmountConsideredAmount() != null ? pedValidationTableDTO
				.getAmountConsideredAmount().intValue() : 0);
		dto.setCoPayAmount(pedValidationTableDTO.getCopayAmount() != null ? pedValidationTableDTO
				.getCopayAmount().intValue() : 0);
		dto.setMinimumAmountOfAmtconsideredAndPackAmt(pedValidationTableDTO.getMinimumAmount() != null ? pedValidationTableDTO
				.getMinimumAmount().intValue() : 0);
		dto.setNetAmount(pedValidationTableDTO.getNetAmount() != null ? pedValidationTableDTO
				.getNetAmount().intValue() : 0);
		dto.setMinimumAmount(pedValidationTableDTO.getApprovedAmount() != null ? pedValidationTableDTO
				.getApprovedAmount().intValue() : 0);
		dto.setRemarks(pedValidationTableDTO.getApproveRemarks() != null ? pedValidationTableDTO
				.getApproveRemarks() : "");
		dto.setReverseAllocatedAmt(pedValidationTableDTO.getNetApprovedAmount() != null ? pedValidationTableDTO.getNetApprovedAmount().intValue() : 0);
		if(isDeletedOne) {
			dto.setIsDeletedOne(true);
		}
		
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
		}else{
			dto.setIsAmbChargeApplicable(false);
		}
		
		dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
		dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
		
		if(this.bean.getIsAmbulanceApplicable()){
			dto.setIsAmbulanceEnable(true);
		}
		
		medicalDecisionDTOList.add(dto);
	}*/

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
		if (initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}

	/*public void buildQueryLayout() {
		
		final Integer setQueryValues = setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		if(setQueryValues > 0) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, setQueryValues);
		} else {
			generateQueryDetails(setQueryValues);
		}
		
	}*/
	
	public Boolean alertMessage(String message, final Integer count) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
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
//				generateQueryDetails(count);
			}
		});
		return true;
	}

	/*private void generateQueryDetails(Integer setQueryValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		intimation.setKey(this.bean.getIntimationKey());
		//queryDetailsTableObj.setViewQueryVisibleColumn();
		queryDetailsTableObj.setViewPAQueryDetialsColumn();
		setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks",
				"queryRemarks", TextArea.class);
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("400px");
		
		
	
		
		addingSentToCPUFields();
	//	HorizontalLayout formLayout1 = new HorizontalLayout(txtQueryCount,viewClaimsDMSDocument);
		//formLayout1.setComponentAlignment(viewClaimsDMSDocument, Alignment.MIDDLE_RIGHT);
		// dynamicFrmLayout = new FormLayout(txtQueryCount,queryRemarksTxta);
		dynamicFrmLayout.addComponent(txtQueryCount);
		//dynamicFrmLayout.addComponent(formLayout1);
		dynamicFrmLayout.addComponent(queryRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		dynamicFrmLayout.setSpacing(true);
		// dynamicFrmLayout.addComponent(txtQueryCount,queryRemarksTxta);

		
		 * VerticalLayout verticalQueryLayout = new VerticalLayout();
		 * verticalQueryLayout.setHeight("100%");
		 * verticalQueryLayout.setWidth("100%");
		 * verticalQueryLayout.addComponent(preAuthPreviousQueryDetailsTable);
		 * verticalQueryLayout.addComponent(dynamicFrmLayout);
		 

		alignFormComponents();
		
		 * dynamicTblLayout.setHeight("100%");
		 * dynamicTblLayout.setWidth("100%"); dynamicTblLayout.setMargin(true);
		 * dynamicFrmLayout.setSpacing(true);
		 
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		//viewAllDocsLayout.setComponentAlignment(viewClaimsDMSDocument, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout vTblLayout = new VerticalLayout(
				queryDetailsTableObj, viewAllDocsLayout ,dynamicFrmLayout);
		
		vTblLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		
		// wholeVlayout.addComponent(new
		// VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout));
		wholeVlayout.addComponent(vTblLayout);
		// wholeVlayout.addComponent(new VertdynamicFrmLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(queryRemarksTxta);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(vTblLayout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		
		addViewAllDocsListener();
		showInPopup(VLayout, dialog);
	}*/
	
	
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
						//getUI().getPage().open(url, "_blank");
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);


						/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
						dmsDTO.setIntimationNo(bean.getNewIntimationDTO().getIntimationId());
						dmsDTO.setClaimNo(bean.getClaimDTO().getClaimId());
						dmsDTO.setDmsDocumentDetailsDTOList(bean.getDmsDocumentDTOList());
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
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_VIEW_PAGE,dmsDocumentDetailsViewPage);
						
						dmsDocumentViewListenerTable = dmsDocumentViewListenerTableObj.get();
						
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOC_TABLE,dmsDocumentViewListenerTable);*/
						//VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_PRESENTER_STRING,SHACON);
					//	getSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						
					/*	claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
						claimsDMSWindow.setWindowName("_blank");
						
						claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
						claimsDMSWindow.extend(viewClaimsDMSDocument);*/
						//claimsDMSWindow.markAsDirty();
					//	UI.getCurrent().addWindow(popup);
						
					}
				});
			}
		}
	}

	/*public void buildRejectLayout(Object rejectionCategoryDropdownValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		// addingSentToCPUFields();
		// rejectionCategoryCmb = (ComboBox)
		// binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		// rejectionCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues);
		// rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		// rejectionCategoryCmb.setItemCaptionPropertyId("value");

		rejectionRemarksTxta = (TextArea) binder.buildAndBind(
				"Rejection Remarks", "rejectionRemarks", TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);

		rejectionRemarksTxta.setWidth("400px");
		rejectionRemarksTxta.setHeight("200px");

		dynamicFrmLayout.addComponent(rejectionRemarksTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(rejectionRemarksTxta);
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

		dynamicFrmLayout = new FormLayout(rejectionRemarksTxta);

		VerticalLayout VLayout = new VerticalLayout(viewAllDocsLayout,dynamicFrmLayout, btnLayout);
		VLayout.setComponentAlignment(viewAllDocsLayout, Alignment.BOTTOM_RIGHT);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
//		VLayout.setWidth("400px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
	//	addViewAllDocsListener();
		showInPopup(VLayout, dialog);

	}*/
	public void  builtCancelRODLayout(){
		initBinder();
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
		
		dynamicFrmLayout.addComponent(reasonForCancelRodCmb);
		dynamicFrmLayout.addComponent(cancellationRemarks);
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

		dynamicFrmLayout = new FormLayout(reasonForCancelRodCmb,cancellationRemarks);

		VerticalLayout VLayout = new VerticalLayout(dynamicFrmLayout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);	
		
	}
	
	public void buildDenialLayout(Object denialValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		addingSentToCPUFields();
		reasonForDenialCmb = (ComboBox) binder.buildAndBind(
				"Reason For Denial", "reasonForDenial", ComboBox.class);
		reasonForDenialCmb
				.setContainerDataSource((BeanItemContainer<SelectValue>) denialValues);
		reasonForDenialCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForDenialCmb.setItemCaptionPropertyId("value");
		denialRemarksTxta = (TextArea) binder.buildAndBind("Denial Remarks",
				"denialRemarks", TextArea.class);
		denialRemarksTxta.setMaxLength(100);
		dynamicFrmLayout.addComponent(reasonForDenialCmb);
		dynamicFrmLayout.addComponent(denialRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(reasonForDenialCmb);
		mandatoryFields.add(denialRemarksTxta);
		showOrHideValidation(false);
	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	public void buildEscalateLayout(Object escalateToValues) {
		
		BeanItemContainer<SelectValue> escalateValues = (BeanItemContainer<SelectValue>) escalateToValues;
	
		BeanItemContainer<SelectValue> updatedValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		List<SelectValue> itemIds = escalateValues.getItemIds();
		for (SelectValue selectValue : itemIds) {
			if(selectValue.getId().equals(ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)){
				updatedValues.addBean(selectValue);
			}
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getRMA5()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA5)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA4()){
			
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA4)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA3()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA3)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA2()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA2)){
					updatedValues.addBean(selectValue);
				}
			}
			
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA1()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA1)){
					updatedValues.addBean(selectValue);
				}
			}
		}

//		List<SelectValue> newItemIds = new ArrayList<SelectValue>();
//		
//		for (SelectValue selectValue : itemIds) {
//			if(! selectValue.getId().equals(ReferenceTable.RMA3) && ! selectValue.getId().equals(ReferenceTable.RMA4)){
//				newItemIds.add(selectValue);
//			}
//		}
//		
//		if(! newItemIds.isEmpty()){
//			updatedValues.addAll(newItemIds);
//		}

		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		escalateToCmb = (ComboBox) binder.buildAndBind("Escalate To",
				"escalateTo", ComboBox.class);
		escalateToCmb
				.setContainerDataSource(updatedValues);
		escalateToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateToCmb.setItemCaptionPropertyId("value");
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
			escalateToCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo());
		}
		// uploadFile = (Upload)
		// binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		uploadFile = new Upload();
		uploadFile.setCaption("File UpLoad");
		escalationRemarksTxta = (TextArea) binder.buildAndBind(
				"Escalate Remarks", "escalationRemarks", TextArea.class);
		escalationRemarksTxta.setMaxLength(100);
		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(escalationRemarksTxta);
		mandatoryFields.add(escalateToCmb);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(submitButtonWithListener,
				Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(new FormLayout(
				escalateToCmb, uploadFile, escalationRemarksTxta), btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);

	}

	private void addingSentToCPUFields() {
//		unbindField(sentToCPU);
//		sentToCPU = (OptionGroup) binder.buildAndBind("Send to CPU",
//				"sentToCPUFlag", OptionGroup.class);
//		sentToCPU.addItems(getReadioButtonOptions());
//		sentToCPU.setItemCaption(true, "Yes");
//		sentToCPU.setItemCaption(false, "No");
//		sentToCPU.setStyleName("horizontal");
//		sentToCPU.select(false);
//
//		sentToCPU.addValueChangeListener(new Property.ValueChangeListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				Boolean isChecked = false;
//				if (event.getProperty() != null
//						&& event.getProperty().getValue().toString() == "true") {
//					isChecked = true;
//				}
//
//				fireViewEvent(
//						PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_CPU_SELECTED,
//						isChecked);
//			}
//		});
	}

	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues) {
		 initBinder();
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		unbindAndRemoveComponents(dynamicFrmLayout);

		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind(
				"Type of Coordinator Request", "typeOfCoordinatorRequest",
				ComboBox.class);
		typeOfCoordinatorRequestCmb
				.setContainerDataSource((BeanItemContainer<SelectValue>) dropdownValues);
		typeOfCoordinatorRequestCmb
				.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		typeOfCoordinatorRequestCmb.setItemCaptionPropertyId("value");

		if (this.bean.getPreauthMedicalDecisionDetails() != null
				&& this.bean.getPreauthMedicalDecisionDetails()
						.getTypeOfCoordinatorRequest() != null) {

			typeOfCoordinatorRequestCmb.setValue(this.bean
					.getPreauthMedicalDecisionDetails()
					.getTypeOfCoordinatorRequest());
		}

		reasonForReferringTxta = (TextArea) binder.buildAndBind(
				"Reason For Refering", "reasonForRefering", TextArea.class);
		reasonForReferringTxta.setMaxLength(4000);
		reasonForReferringTxta.setHeight("200px");
		reasonForReferringTxta.setWidth("400px");	
		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(typeOfCoordinatorRequestCmb);
		mandatoryFields.add(reasonForReferringTxta);
		showOrHideValidation(false);

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

		VerticalLayout VLayout = new VerticalLayout(new FormLayout(
				typeOfCoordinatorRequestCmb, reasonForReferringTxta), btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);

	}

	public void buildSpecialistLayout(Object specialistValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		specialistType = (ComboBox) binder.buildAndBind("Specialist Type",
				"specialistType", ComboBox.class);
		specialistType
				.setContainerDataSource((BeanItemContainer<SelectValue>) specialistValues);
		specialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		specialistType.setItemCaptionPropertyId("value");

		if (this.bean.getPreauthMedicalDecisionDetails() != null
				&& this.bean.getPreauthMedicalDecisionDetails()
						.getSpecialistType() != null) {

			specialistType.setValue(this.bean
					.getPreauthMedicalDecisionDetails().getSpecialistType());
		}

		// uploadFile = (Upload)
		// binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		final Upload upload = new Upload("", this);
		upload.addSucceededListener(this);
		upload.setReceiver(this);
		upload.setButtonCaption(null);

		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason for Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(100);

		HorizontalLayout layout = new HorizontalLayout(new FormLayout(
				specialistType, upload, reasonForReferringIV));
		layout.setSpacing(true);

		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(reasonForReferringIV);
		mandatoryFields.add(specialistType);
		showOrHideValidation(false);

		final ConfirmDialog dialog = new ConfirmDialog();
		// Button submitButtonWithListener = getSubmitButtonWithDMS(dialog);

		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";

				if (isValid()) {
					upload.submitUpload();
					dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
					showErrorPopup(eMsg);
				}

			}
		});

		HorizontalLayout btnLayout = new HorizontalLayout(submitButton,
				getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);

	}

	public void buildEscalateReplyLayout() {
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
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalationRemarks() != null){
			remarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalationRemarks());
		}

		uploadFile = new Upload();
		uploadFile.setCaption("File UpLoad");
		escalteReplyTxt = (TextArea) binder.buildAndBind("Escalate Reply",
				"escalateReply", TextArea.class);
		escalteReplyTxt.setMaxLength(100);

		HorizontalLayout layout = new HorizontalLayout(new FormLayout(
				referredByRole, remarks, escalteReplyTxt, uploadFile),
				new FormLayout(referredByName, escalateDesignation));
		layout.setSpacing(true);

		alignFormComponents();
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(escalteReplyTxt);
		showOrHideValidation(false);

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
		VLayout.setWidth("1000px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);

	}

	@SuppressWarnings("rawtypes")
	private void unbindAndRemoveComponents(AbstractComponent component) {
		for (int i = 0; i < ((FormLayout) component).getComponentCount(); i++) {
			if (((FormLayout) component).getComponent(i) instanceof Upload) {
				continue;
			}
			unbindField((AbstractField) ((FormLayout) component)
					.getComponent(i));
		}
		dynamicFrmLayout.removeAllComponents();
		wholeVlayout.removeComponent(dynamicFrmLayout);

		if (null != wholeVlayout && 0 != wholeVlayout.getComponentCount()) {
			Iterator<Component> componentIterator = wholeVlayout.iterator();
			while (componentIterator.hasNext()) {
				Component searchScrnComponent = componentIterator.next();
				if (searchScrnComponent instanceof VerticalLayout) {
					((VerticalLayout) searchScrnComponent)
							.removeAllComponents();

				}
			}
		}
	}

	/*
	 * private void removePreviousQueryTbl(AbstractComponent component) {
	 * if(null != dynamicTblLayout && 0 != dynamicTblLayout.getComponentCount())
	 * { dynamicTblLayout.removeAllComponents();
	 * wholeVlayout.removeComponent(dynamicTblLayout); } }
	 */

	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			if (field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void alignFormComponents() {
		if (dynamicFrmLayout != null) {
			for (int i = 0; i < dynamicFrmLayout.getComponentCount(); i++) {
				dynamicFrmLayout.setExpandRatio(
						dynamicFrmLayout.getComponent(i), 0.5f);
			}
		}
	}

	public boolean isValid() {
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());

		/*
		 * if(!(null != remarksForCPU && null != remarksForCPU.getValue() &&
		 * !("").equals( remarksForCPU.getValue()))) { hasError = true;
		 * errorMessages.add("Please Enter Remarks For CPU."); return !hasError;
		 * }
		 * 
		 * if(!(null != approvalRemarksTxta && null !=
		 * approvalRemarksTxta.getValue() && !("").equals(
		 * approvalRemarksTxta.getValue()))) { hasError = true;
		 * errorMessages.add("Please Enter Approval Remarks."); return
		 * !hasError; }
		 */
		// List<UploadDocumentDTO> uploadList = uploadedDocsTable.getValues();
		//
		//
		// if((null != uploadList && !uploadList.isEmpty()))
		// {
		// for (UploadDocumentDTO uploadDoc : uploadList)
		// {
		// List<BillEntryDetailsDTO> billEntryList =
		// uploadDoc.getBillEntryDetailList();
		// if(null != billEntryList && !billEntryList.isEmpty())
		// {
		// for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryList)
		// {
		// if(!(uploadDoc.getBillNo().equals(billEntryDetailsDTO.getBillNo())))
		// {
		// hasError = true;
		// errorMessages.add("Please enter Bill details for uploaded documents </br>");
		// break;
		// }
		// }
		// }
		// else
		// {
		// hasError = true;
		// errorMessages.add("Please enter Bill details for uploaded documents </br>");
		// }
		// }
		// }
		// else
		// {
		// hasError = true;
		// errorMessages.add("Please enter Bill details for uploaded documents </br>");
		// }
//		if (uploadDocumentListenerTableObj != null) {
//			Boolean isValid = uploadDocumentListenerTableObj
//					.getBillEntryTableInstance();
//			if (isApproval) {
//				if (!isValid) {
//					hasError = true;
//					errorMessages
//							.add("Please Enter Corporate Remarks in Bill Entry. </br>");
//				}
//			}
//
//		}
		/*if (this.binder == null) {
			hasError = true;
			errorMessages
					.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}*/

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
				if (this.bean
						.getStatusKey()
						.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
//					this.investigationReportReviewedChk.setEnabled(true);
				} else {
//					this.investigationReportReviewedChk.setValue(false);
//					this.investigationReportReviewedChk.setEnabled(false);
				}

			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	public void generateFieldsBasedOnSentToCPU(Boolean isChecked) {
		if (isChecked) {
			unbindField(remarksForCPU);
			remarksForCPU = (TextArea) binder.buildAndBind("Remarks for CPU",
					"remarksForCPU", TextArea.class);
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

	public void buildSendReplyLayout() {
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

		if (this.bean.getIsReferToMedicalApprover()) {
			remarks.setValue(bean.getPreviousRemarks() != null ? bean
					.getPreviousRemarks() : "");
			reasonForReferring
					.setValue(bean.getPreviousReasonForReferring() != null ? bean
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

	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";
				if (isValid()) {
					wizard.getNextButton().setEnabled(false);
					wizard.getFinishButton().setEnabled(true);
					dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
					showErrorPopup(eMsg);
				}
			}
		});
		return submitButton;
	}

	private Button getSubmitButtonWithDMS(final ConfirmDialog dialog) {

		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				String eMsg = "";

				if (isValid()) {
					uploadFile.submitUpload();
					dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
					showErrorPopup(eMsg);
				}
			}
		});
		return submitButton;
	}

	/*public void buildInitiateFieldVisit(Object fieldVisitValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

	    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;
	
		allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
		allocationTo.setContainerDataSource(map.get("allocationTo"));
		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		allocationTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
		}
		
		fvrAssignTo = (ComboBox) binder.buildAndBind("Assign To","assignTo",ComboBox.class);
		fvrAssignTo.setContainerDataSource(map.get("fvrAssignTo"));
		fvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrAssignTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAssignTo() != null){
			fvrAssignTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAssignTo());
		}
		
		fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
		fvrPriority.setContainerDataSource(map.get("fvrPriority"));
		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrPriority.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
		}

		fvrTriggerPoints = (TextArea) binder.buildAndBind("FVR Trigger Points","fvrTriggerPoints",TextArea.class);
		fvrTriggerPoints.setMaxLength(4000);
		fvrTriggerPoints.setWidth("400px");
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

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
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,fvrAssignTo,fvrPriority, fvrTriggerPoints), horizontalLayout);
		horizontalLayout2.setSpacing(true);
//		horizontalLayout2.setHeight("px");
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		mandatoryFields.add(fvrTriggerPoints);
		mandatoryFields.add(fvrAssignTo);
		mandatoryFields.add(fvrPriority);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
//		btnLayout.setWidth("600px");
//		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, btnLayout);
//		VLayout.setWidth("850px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		showInPopup(VLayout, dialog);
		
	}

	public void buildInitiateInvestigation(Object fieldVisitValues) {
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
		reasonForReferringIV.setMaxLength(4000);
		reasonForReferringIV.setWidth("350px");
		triggerPointsToFocus = (TextArea) binder.buildAndBind(
				"Trigger Points To Focus", "triggerPointsToFocus",
				TextArea.class);
		triggerPointsToFocus.setMaxLength(4000);
		triggerPointsToFocus.setWidth("350px");

		viewFVRDetails = new Button("View Investigation Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

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
//		VLayout.setWidth("800px");
		VLayout.setHeight("350px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);

	}

	public void suggestApprovalClick(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);

	}*/

	public void setAppropriateValuesToDTOFromProcedure(
			DiagnosisProcedureTableDTO medicalDecisionDto,
			Map<String, Object> values) {
		if(bean.getIsNonAllopathic()) {
			bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//			createNonAllopathicFields((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT), (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
		
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values.get("currentSL"))
				.intValue() > 0 ? (String.valueOf(((Double) values
				.get("currentSL")).intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto.setCoPayPercentageValues((List<String>) values
				.get("copay"));
		
		

		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else {
				isResidual = true;
			}
			
			if(!isResidual) {
				Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
				Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
				medicalDecisionDto.setSubLimitAvaliableAmt(min);
				medicalDecisionDto.setSubLimitUtilAmount(0);
			}
		}
		// need to implement in new medical listener table
		this.medicalDecisionTableObj.addBeanToList(medicalDecisionDto);
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
						if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
							if (bean.getIsNonAllopathic()) {
								min = Math.min(min, bean.getNonAllopathicAvailAmt());
							}
						}
						bean.getPreauthMedicalDecisionDetails()
								.setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {

							approvedAmtField.setValue(String
									.valueOf(totalApprovedAmt));
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product.getCode() != null
										&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
												.equalsIgnoreCase(product
														.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
												.equalsIgnoreCase(product
														.getCode())))) {
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

								
								}
							}
						} else {
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product.getCode() != null
										&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
												.equalsIgnoreCase(product
														.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
												.equalsIgnoreCase(product
														.getCode())))) {
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
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
						if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
							if (bean.getIsNonAllopathic()) {
								min = Math.min(min, bean.getNonAllopathicAvailAmt());
							}
						}
						bean.getPreauthMedicalDecisionDetails()
								.setInitialTotalApprovedAmt(min.doubleValue());
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {

							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product.getCode() != null
										&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
												.equalsIgnoreCase(product
														.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
												.equalsIgnoreCase(product
														.getCode())))) {
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						} else {
							if (bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO()
										.getPolicy().getProduct();
								if (product.getCode() != null
										&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
												.equalsIgnoreCase(product
														.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
												.equalsIgnoreCase(product
														.getCode())))) {
									min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						}
					}
				});
		
			this.medicalDecisionTableObj.ambulanceChangeField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -4052108705772482724L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField ambulanceTotal = (TextField) event.getProperty();
				if(amountConsideredTable != null) {
					amountConsideredTable.ambulanceChargeField.setValue(String.valueOf(SHAUtils.getIntegerFromStringWithComma(ambulanceTotal.getValue())));
				}
			}
		});

	}

	private boolean validateSelectedCoPay(Double selectedCoPay) {
		if (null != this.bean.getCopay()) {
			if (selectedCoPay >= this.bean.getCopay()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void setCategoryValues(
			BeanItemContainer<SelectValue> selectValueContainer) {
		if (uploadDocumentListenerTableObj != null) {
			uploadDocumentListenerTableObj
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
						 * if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
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

	protected void addTotalClaimedListener() {
		this.uploadDocumentListenerTableObj.claimedAmtField
				.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != uploadDocumentListenerTableObj) {
							String provisionAmt = (String) event.getProperty()
									.getValue();
							if (null != provisionAmt
									&& !("").equalsIgnoreCase(provisionAmt)) {
								if (SHAUtils.isValidDouble(provisionAmt)) {
									bean.setAmountConsidered(uploadDocumentListenerTableObj
											.getHospitalizationAmount() != null ? uploadDocumentListenerTableObj
											.getHospitalizationAmount() : bean
											.getAmountConsidered());
									// For lumpsum, amount consider will be setted as bill value from bill entry...
									StarCommonUtils.setAmountconsideredForLumpsum(bean);
									if (amountConsidered != null) {
										amountConsidered.setValue(bean
												.getAmountConsidered());
									}

									if (amountConsideredTable != null) {
										amountConsideredTable.setDynamicValues(
												null, true, false,true, false);
									}
								}
							}
						}
					}

				});

	}

	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				wizard.getFinishButton().setEnabled(false);
				wizard.getNextButton().setEnabled(true);
			}
		});
		return cancelButton;
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			file = new File(System.getProperty("jboss.server.data.dir") + "/"
					+ filename);
			fos = new FileOutputStream(file);
			// if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
			// fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT,
			// this.key,filename);
			// }
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file<br/>", e.getMessage(),
					Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
			return null;
		}
		return fos; // Return the output stream to write to
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		System.out.println("File uploaded" + event.getFilename());

		try {

			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

			if (null != fileAsbyteArray) {

				Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
				boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				// if(hasSpecialChar)
				// {
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(
						event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean(""
						+ uploadStatus.get("status"));
				// TO read file after load
				if (flagUploadSuccess.booleanValue()) {
					String token = "" + uploadStatus.get("fileKey");
					String fileName = event.getFilename();
					this.bean.setTokenName(token);
					this.bean.setFileName(fileName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void setBillEntryAmountConsideredValue(Double sumValue, PreauthDTO bean) {
		this.bean = bean;
		bean.setAmountConsidered(String.valueOf(sumValue.intValue()));
		
		
		
		bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(bean.getAmbulanceLimitAmount() != null){
			 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
		      totalAmountConsidered -= this.bean.getAmbulanceLimitAmount();
		      
		     bean.setAmbulanceAmountConsidered(String.valueOf(totalAmountConsidered.intValue()));
			}
			
		}
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
		
		amountConsidered.setValue(String.valueOf(sumValue.intValue()));
		
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			amountConsidered.setValue(bean.getAmountConsidered());
		}
		Double ambulanceAmtConsidered = SHAUtils.getDoubleFromStringWithComma(bean.getAmbulanceAmountConsidered());
		
		if(null != amountConsideredTable) {
			amountConsideredTable.setDynamicValues(null, true, false,false, false);
		}
		
		
	}

	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		uploadDocumentListenerTableObj.loadBillEntryValues(uploadDTO);
		
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
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public void setButtonDisabledForconsiderforPayment(Boolean visible){
		
		if (visible) {
			sendReplyBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			escalteReplyBtn.setEnabled(false);
			referToSpecialistBtn.setEnabled(false);
			cancelROD.setEnabled(false);
			
//			approveBtn.setEnabled(false);
//			queryBtn.setEnabled(false);
//			initiateFieldVisitBtn.setEnabled(false);
//			initiateInvestigationBtn.setEnabled(false);
//			rejectBtn.setEnabled(true);
		}
	}
	
	
	public void showAmtConsiderAlert(final ConfirmDialog dialog){
		
		if(medicalDecisionTableObj.getTotalAmountConsidered() < (
				SHAUtils.getIntegerFromString(bean.getAmountConsidered()))){
			buildSuccessLayout("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table", dialog);
		}else{
			validatePage(dialog);	
		}
	}
	
	private void buildSuccessLayout(String message, final ConfirmDialog confirmDialog)
	{
	  	Label successLabel = new Label("<b style = 'color: red;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final Window dialog = new Window();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		getUI().getCurrent().addWindow(dialog);
	
//		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {					
				dialog.close();
				validatePage(confirmDialog);
			}
		});
		cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	}
	
	private Boolean validatePolicyStatus(String policyNumber){
		Boolean hasError = false;
		enteredValues.put("polNo", policyNumber);
		
		BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
		List<PremPolicy> policyList = policyContainer.getItemIds();
		if(policyList !=null && !policyList.isEmpty()){
			for (PremPolicy premPolicy : policyList) {
				if(premPolicy.getPolicyStatus().equalsIgnoreCase(SHAConstants.CANCELLED_POLICY)){
					hasError = true;
				}
				
			}
		}
		return !hasError;
	}
		
public void showErrorPageForCancelledPolicy(){
		
		String message = SHAConstants.CANCELLED_POLICY_ALERT ;
		
		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		
	}
	
	public void buildRefToBillEntryLayout(){
		this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY);
		
			reasonRefBillEntryTxta = new TextArea("Reason for Referring"); 
													//, "referToBillEntryBillingRemarks", TextArea.class);
			reasonRefBillEntryTxta.setMaxLength(4000);
			reasonRefBillEntryTxta.setWidth("400px");
			reasonRefBillEntryTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			reasonRefBillEntryTxta.setId("billEntryRmrks");
			remarksPopupListener(reasonRefBillEntryTxta,null);
			
			mandatoryFields = new ArrayList<Component>();
			mandatoryFields.add(reasonRefBillEntryTxta);
			showOrHideValidation(false);
			
			FormLayout fieldsFLayout;
			fieldsFLayout = new FormLayout(reasonRefBillEntryTxta);
					
			
			final ConfirmDialog dialog = new ConfirmDialog();
			Button submitButtonWithListener = getSubmitListenerForBillEntry(dialog);
	
			HorizontalLayout btnLayout = new HorizontalLayout(
					submitButtonWithListener, getCancelButton(dialog));
			btnLayout.setWidth("400px");
			btnLayout.setComponentAlignment(submitButtonWithListener,
					Alignment.MIDDLE_CENTER);
			btnLayout.setSpacing(true);
			
			VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
	//		verticalLayout.setWidth("500px");
			verticalLayout.setWidth("800px");
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);
					
			showInPopup(verticalLayout, dialog);
	}
	
	private Button getSubmitListenerForBillEntry(final ConfirmDialog dialog) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if (reasonRefBillEntryTxta.getValue() != null && !reasonRefBillEntryTxta.getValue().isEmpty()) {
					bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks(reasonRefBillEntryTxta.getValue());
					dialog.close();
				}
				else{
					eMsg.append("Please Enter Reason for Referring to Bill Entry.</br>");
					showErrorPopup(eMsg.toString());
				}
				wizard.getNextButton().setEnabled(false);
				wizard.getFinishButton().setEnabled(true);
			}
		});
		return submitButton;
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


	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
//				if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
//				}
				
				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
					
						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
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
}