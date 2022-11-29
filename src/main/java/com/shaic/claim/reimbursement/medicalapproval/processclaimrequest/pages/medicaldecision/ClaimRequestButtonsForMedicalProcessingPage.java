package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.Wizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.common.ApprovedTable;
import com.shaic.claim.common.BillEntryWizardStep;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing.ClaimRequestPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
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
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
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

public class ClaimRequestButtonsForMedicalProcessingPage extends ViewComponent implements
Receiver, SucceededListener{
	

	private static final long serialVersionUID = 7089191150922046819L;

	@Inject
	private PreauthDTO bean;

	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	@Inject
	private Instance<BillEntryWizardStep> billEntrywizardStepInstance;
	
	@Inject
	private Instance<ApprovedTable> approvedTableWizardStepInstance;

	@Inject
	private ViewBillSummaryPage viewBillSummary;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private Intimation intimation;

	/*@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;*/

	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;

	/*@Inject
	private UploadedDocumentsForBillEntry uploadedDocsTable;*/

	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;

	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;

	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;

	public AmountConsideredTable amountConsideredTable;

	private Window popup;

	private File file;

	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private PendingFvrInvsQueryPageUI invesFvrQueryPendingPageUI;
	
	/*@Inject
	private ClaimRequestMedicalDecisionPageUI medicalDecisionPage;*/

	Map<String, Object> referenceData = new HashMap<String, Object>();

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	//private TextArea approvalRemarks;

	private Button submitButton;

	public Button cancelButton;

	private Label amountConsidered;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;

	private Button approveBtn;
	//private Button queryBtn;
	private Button rejectBtn;
	private Button escalateClaimBtn;
	private Button sendReplyBtn;
	private Button referCoordinatorBtn;
/*	private Button initiateFieldVisitBtn;
	private Button initiateInvestigationBtn;*/
	private Button escalteReplyBtn;
	private Button referToSpecialistBtn;
	private Button cancelROD;

	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	//private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	//private ComboBox rejectionCategoryCmb;
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

	private ComboBox allocationTo;
	//private ComboBox fvrAssignTo;
	private ComboBox fvrPriority;
	
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;

	private TextArea triggerPointsToFocus;
	private TextArea reasonForReferringIV;

	// Added for query table.

	// private FormLayout dynamicTblLayout;

	//private OptionGroup sentToCPU;
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
	
	@EJB
	private MasterService masterService;
	
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	/*@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;*/
	
	private Button viewClaimsDMSDocument;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	@Inject
	private InvestigationService investigationService;
	
	@Inject
	private ReimbursementService rodService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;
	
	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	private TextArea remarksFromDeptHead;
	
	FormLayout userLayout = new FormLayout();
	FormLayout dynamicGenarateLayout = new FormLayout();
	private Boolean isCancelRodLayout = Boolean.FALSE;
	
	// R1207
	private Map<String, String> roleValidationContainer = new HashMap<String, String>();
	private Map<String, String> userValidationContainer = new HashMap<String, String>();
	
	private HorizontalLayout corpBufferLayout;

	private TextField totalnonpayableDedAmtTxt;
	private TextField amountConsidered2Txt; 
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
	
	@Inject
	private CreateRODService createRodService;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void initView(PreauthDTO bean, GWizard wizard) {
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

		HorizontalLayout buttonFirstLayout = new HorizontalLayout(sendReplyBtn,				
				referCoordinatorBtn, escalteReplyBtn,escalateClaimBtn,referToSpecialistBtn,cancelROD);
		buttonFirstLayout.setSpacing(true);
	//	buttonSecondLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(buttonFirstLayout/*,buttonSecondLayout*/);
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
			approveBtn.setEnabled(false);
			sendReplyBtn.setEnabled(true);
		} else {
			approveBtn.setEnabled(true);
			sendReplyBtn.setEnabled(false);
		}
		
		if(this.bean.getIsEscalateReplyEnabled()){
			escalteReplyBtn.setEnabled(true);
		}else{
			escalteReplyBtn.setEnabled(false);
		}
		
		

		escalteReplyBtn.setEnabled(this.bean.getIsEscalateReplyEnabled());
		if (this.bean.getIsReferToMedicalApprover()) {
			approveBtn.setEnabled(false);
			sendReplyBtn.setEnabled(true);
		} else {
			approveBtn.setEnabled(true);
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
						.equals(ReferenceTable.MATERNITY_MASTER_ID) && !(ReferenceTable.getMaternityValidateProducts().containsKey(bean.getPolicyDto().getProduct().getKey())))) {
			approveBtn.setEnabled(false);
		//	queryBtn.setEnabled(false);
			sendReplyBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			escalteReplyBtn.setEnabled(false);
		//	initiateFieldVisitBtn.setEnabled(false);
		//	initiateInvestigationBtn.setEnabled(false);
			referToSpecialistBtn.setEnabled(false);
		} else {
			approveBtn.setEnabled(true);
		//	queryBtn.setEnabled(true);
			sendReplyBtn.setEnabled(true);
			escalteReplyBtn.setEnabled(true);
			escalateClaimBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
		//	initiateFieldVisitBtn.setEnabled(true);
		//	initiateInvestigationBtn.setEnabled(true);
			referToSpecialistBtn.setEnabled(true);
			if (this.bean.getIsReferToMedicalApprover()) {
				approveBtn.setEnabled(false);
				sendReplyBtn.setEnabled(true);
			} else {
				approveBtn.setEnabled(true);
				sendReplyBtn.setEnabled(false);
			}
			
			if(this.bean.getIsEscalateReplyEnabled()){
				escalteReplyBtn.setEnabled(true);
			}else{
				escalteReplyBtn.setEnabled(false);
			}
			
			

			escalteReplyBtn.setEnabled(this.bean.getIsEscalateReplyEnabled());
			if (this.bean.getIsReferToMedicalApprover()) {
				approveBtn.setEnabled(false);
				sendReplyBtn.setEnabled(true);
			} else {
				approveBtn.setEnabled(true);
				sendReplyBtn.setEnabled(false);
			}
			
			if(this.bean.getIsEscalateReplyEnabled()){
				escalteReplyBtn.setEnabled(true);
			}else{
				escalteReplyBtn.setEnabled(false);
			}
			
		}
		
		Map<String, Object> outPutArray = (Map<String, Object>)bean.getDbOutArray();
		String sendReplyStatus = (String) outPutArray.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if(null != sendReplyStatus && (sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY) || sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_BILLING) ||
				sendReplyStatus.equalsIgnoreCase(SHAConstants.SEND_REPLY_FA))){
			sendReplyBtn.setEnabled(true);
			approveBtn.setEnabled(false);
		}
		
		setButtonDisabledForconsiderforPayment(this.bean.getIsConsiderForPaymentNo());
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);

	/*	claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
		claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
		claimsDMSWindow.extend(viewClaimsDMSDocument);*/
		
		/*if (this.bean.getIsQueryReceived() || this.bean.getIsReconsiderationRequest()){
			cancelROD.setEnabled(false);
		}*/
		

		if(null != bean && null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() && 
				null != this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() &&
				(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(this.bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))
		{
			approveBtn.setEnabled(false);
		}
		
		 viewAllDocsLayout = new HorizontalLayout();
		
		addViewAllDocsListener();

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
				
			
				isApproval = true;
				//comment by noufel since flow already change for physical verification
//				Below alert for PhysicalDoc Verification additional changes
//				if(bean.getModeOfReceipt() != null 
//						&& bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)){
//					if(bean.getCheckerVerified() == null){
//						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
//						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
//						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
//								.createInformationBox("Physical Document Not Verified for this ROD" + "</b>", buttonsNamewithType);
//						Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
//								.toString());
//						homeButton.addClickListener(new ClickListener() {
//							private static final long serialVersionUID = 7396240433865727954L;
//
//							@Override
//							public void buttonClick(ClickEvent event) {
//								//dialog.close();
//								if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
//										&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
////								Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
//								Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
//								bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
//								}else{
//									bean.setIsPedWatchList(false);
//								}
//								
//								if(! bean.getIsPedWatchList()){
//								fireViewEvent(
//										ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,
//										null);
//								}else{
//								    alertMessageForPEDWatchList();
//								}
//							}
//						});
//					}
//				} else {

					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
							&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
					}else{
						bean.setIsPedWatchList(false);
					}
					
					if(! bean.getIsPedWatchList()){
					fireViewEvent(
							ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,
							null);
					}else{
					    alertMessageForPEDWatchList();
					}
				
//				}
				
				/*if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//				Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
				Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
				bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,
						null);
				}else{
				    alertMessageForPEDWatchList();
				}*/
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
				if(bean.getScoringClicked()){
					cancelRodAction();
				}else{
					showErrorPopup("Please Complete the Hospital Scoring before Cancelling ROD");
				}
			}
	});


		rejectBtn = new Button("Reject");
		rejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() rejectBtn.setImmediate(true);
//		rejectBtn.setEnabled(this.bean.getIsConsiderForPaymentNo());
		rejectBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4241727763379504532L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;
				
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_REJECTION_EVENT,
						null);
				}else{
					alertMessageForPEDWatchList();
				}
			}
		});

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
				
				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS);
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Escalate Claim");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrQueryInvsList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(), 
						SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrQueryInvsList && !pendingFvrQueryInvsList.isEmpty()){
					
					alertMessageForPendingCases(pendingFvrQueryInvsList);
				
				}					
				else{
					
					buildLayoutBasedOnButtons();
					
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
				
				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS);
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Escalate Reply");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrQueryInvsList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(), 
						SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrQueryInvsList && !pendingFvrQueryInvsList.isEmpty()){
					
					alertMessageForPendingCases(pendingFvrQueryInvsList);
				
				}				
				else{
					
					buildLayoutBasedOnButtons();
					
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
				
				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer to Co-ordinator");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrQueryInvsList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(),
						SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrQueryInvsList && !pendingFvrQueryInvsList.isEmpty()){
					
					alertMessageForPendingCases(pendingFvrQueryInvsList);
				
				}				
				else
				{
					
					buildLayoutBasedOnButtons();
				}
			}
		});


		referToSpecialistBtn = new Button("Refer to Specialist");
		referToSpecialistBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToSpecialistBtn.setImmediate(true);
		referToSpecialistBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApproval = false;

				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer to Specialist");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrQueryInvsList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(), 
						SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrQueryInvsList && !pendingFvrQueryInvsList.isEmpty()){
					
					alertMessageForPendingCases(pendingFvrQueryInvsList);
				
				}		
				else
				{
					buildLayoutBasedOnButtons();
				
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
				

				bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Send Reply");
				bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(),
						SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
								bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
				
				if(null != pendingFvrInvsQueryList && !pendingFvrInvsQueryList.isEmpty()){
				
					showPendingFvrInvsQueryPopup(bean,pendingFvrInvsQueryList,SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING,SHAConstants.SEND_REPLY);
				
				}		
				else
				{
					buildLayoutBasedOnButtons();
				}
			}
		});

		
		if (this.bean.getIsCancelPolicy()) {
			if (this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}

			approveBtn.setEnabled(false);
		//	queryBtn.setEnabled(false);
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
			approveBtn.setEnabled(false);
		//	queryBtn.setEnabled(false);
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

	public void generateFieldsForSuggestApproval() {
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
					/**
					 * 
					 */
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
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			min = Math.min(
					amountConsideredTable.getMinimumValueForGMC(),
					SHAUtils.getIntegerFromString(medicalDecisionTableObj.dummyField
							.getValue()));
			
			if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
				Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
				Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
				if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
					bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
				}
				
			}
			
		}

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

		VerticalLayout layout = null;
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				/*&& bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()*/){
			HorizontalLayout generateFieldBasedOnCorpBuffer = generateFieldBasedOnCorpBuffer();
			layout = new VerticalLayout(amountConsideredLayout,this.medicalDecisionTableObj,generateFieldBasedOnCorpBuffer, this.amountConsideredTable,new FormLayout(approvalRemarksTxta));
		}else{
			layout = new VerticalLayout(amountConsideredLayout,this.medicalDecisionTableObj, this.amountConsideredTable,new FormLayout(approvalRemarksTxta));
		}
		
		layout.setSpacing(true);
		layout.setWidth("1200px");
		layout.setMargin(true);

		Wizard wizard = new Wizard();
		
		BillEntryWizardStep billEntryWizardStep = billEntrywizardStepInstance.get();
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
				dialog.close();
				binder = null;
			}
		});
		dialog.setCaption("");
		dialog.setClosable(true);
		
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
	}

	
	public void validatePage(final ConfirmDialog dialog) {
		StringBuffer eMsg = new StringBuffer();
		
		Boolean hasError = false;
		if (!medicalDecisionTableObj.getTotalAmountConsidered().equals(
				SHAUtils.getIntegerFromString(bean
						.getAmountConsidered()))) {
			hasError = true;
			eMsg.append("Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>");
		}
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){
				
				hasError = true;
				eMsg.append("Amount Entered against Ambulance charges should be equal");
				
			}
		}
		
		//added for corp buffer cr
		String bufferType =null;
		if(bean.getPreauthDataExtractionDetails().getBufferTypeValue() != null &&
				!bean.getPreauthDataExtractionDetails().getBufferTypeValue().isEmpty()){
			bufferType = bean.getPreauthDataExtractionDetails().getBufferTypeValue();
		}
		if(bufferType != null && bufferType.equalsIgnoreCase("NACB")) {
			if(totalnonpayableDedAmtTxt != null && amountConsidered2Txt != null){
				if(amountConsidered2Txt.getValue() != null && amountConsidered2Txt.getValue().isEmpty()){
					hasError = true;
					eMsg.append("Please enter Amount consider value for NACB </br>");
				}
				if(totalnonpayableDedAmtTxt.getValue() != null && amountConsidered2Txt.getValue() != null && !amountConsidered2Txt.getValue().isEmpty()){
					Long nonpayableAmt = Long.valueOf(totalnonpayableDedAmtTxt.getValue());
					Long amountConsider =Long.valueOf(amountConsidered2Txt.getValue());
					bean.getPreauthDataExtractionDetails().setNacBufferUtilizedAmount(amountConsider.intValue());
					if(amountConsider > nonpayableAmt){
						hasError = true;
						eMsg.append("Entered amount is exceeding the Deductibles/Non payable amount</br>");
					}
				}

			}
		}
		
		if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
				null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){			
			
			/*Integer stoplossAmnt = dbCalculationService.getStopLossProcedure(bean.getNewIntimationDTO().getPolicy().getKey(),
					bean.getClaimDTO().getKey(), bean.getKey()).get(SHAConstants.STOP_LOSS_AVAILABLE);
			
			if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > stoplossAmnt){
				
				eMsg.append("Amount Claimed cannot be greater than Stop Loss Amount </br>");
			}*/
		}
		
		if (isValid() && !hasError) {
			setResidualAmtToDTO();
			bean.getPreauthMedicalDecisionDetails()
					.setMedicalDecisionTableDTO(
							medicalDecisionTableObj.getValues());
				
			SHAUtils.doFinalEnhancementCalculationForLetter(this.medicalDecisionTableObj.getValues(), SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.getCoPayValue()) , bean);

			dialog.close();
		} else {
			List<String> errors = getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
			showErrorPopup(eMsg.toString());
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

	private void addRowsForProcAndDiag() {
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

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
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
							ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUSET_SUM_INSURED_CALCULATION,
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
									.doubleValue()) : "0");

					dto.setCoPayPercentage(value);
					dto.setNetAmount(residualAmountDTO.getNetAmount() != null ? residualAmountDTO
							.getNetAmount().intValue() : 0);
					dto.setMinimumAmount(residualAmountDTO.getApprovedAmount() != null ? residualAmountDTO
							.getApprovedAmount().intValue() : 0);
					dto.setReverseAllocatedAmt(residualAmountDTO.getNetApprovedAmount() != null ? residualAmountDTO.getNetApprovedAmount().intValue() : 0);
					//dto.setCoPayType(residualAmountDTO.getCoPayTypeId());
				}
				this.medicalDecisionTableObj.addBeanToList(dto);
			}
		} else {
			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
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
							ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUSET_SUM_INSURED_CALCULATION,
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
	}

	private void addProcedureToMedicalDecision(
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
		dto.setCoPayType(procedureDTO.getCoPayTypeId());
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
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS) ||
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS) ||
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GMC_CONTINUITY_PRODUCT_CODE) &&
				bean.getNewIntimationDTO().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE))){
			
			dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
			dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
			dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
		}
		
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
						if(exclusionAllDetails != null){
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
		dto.setCoPayType(pedValidationTableDTO.getCoPayTypeId());
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
	}

	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(false);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		if(isCancelRodLayout){
			panel.setWidth("1100px");
			isCancelRodLayout = Boolean.FALSE;
		}
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
		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
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

	public void setApprovedAmtValue(Integer amount) {
		if (initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}

	public void buildQueryLayout() {
		
		final Integer setQueryValues = setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		if(setQueryValues > 0) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, setQueryValues);
		} else {
			generateQueryDetails(setQueryValues);
		}
		
	}
	
	public Boolean alertMessage(String message, final Integer count) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
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
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

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

	private void generateQueryDetails(Integer setQueryValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);

		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		intimation.setKey(this.bean.getIntimationKey());
		queryDetailsTableObj.setViewQueryVisibleColumn();
		
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

		/*
		 * VerticalLayout verticalQueryLayout = new VerticalLayout();
		 * verticalQueryLayout.setHeight("100%");
		 * verticalQueryLayout.setWidth("100%");
		 * verticalQueryLayout.addComponent(preAuthPreviousQueryDetailsTable);
		 * verticalQueryLayout.addComponent(dynamicFrmLayout);
		 */

		alignFormComponents();
		/*
		 * dynamicTblLayout.setHeight("100%");
		 * dynamicTblLayout.setWidth("100%"); dynamicTblLayout.setMargin(true);
		 * dynamicFrmLayout.setSpacing(true);
		 */
		
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

	public void buildRejectLayout(Object rejectionCategoryDropdownValues) {
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

	}
	public void  builtCancelRODLayout(){
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		unbindAndRemoveComponents(userLayout);
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
		userLayout = buildUserRoleLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		VerticalLayout VLayout = new VerticalLayout(hLayout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		//VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		isCancelRodLayout = Boolean.TRUE;
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
//						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_CPU_SELECTED,
//						isChecked);
//			}
//		});
	}

	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues) {
		// initBinder();
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

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();

				if (isValid()) {
					upload.submitUpload();
					dialog.close();
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
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
		if (this.binder == null) {
			hasError = true;
			errorMessages
					.add("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
		}

		if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
			if(triggerPtsTable != null){
				hasError = !triggerPtsTableObj.isValid();
				if(hasError){
					errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
					errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
				}
				else{
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
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
		}
		
		if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
			/*if((cmbUserRoleMulti.getValue() == null || cmbUserRoleMulti.isEmpty()) ||(cmbDoctorNameMulti.getValue() == null || cmbDoctorNameMulti.isEmpty()) ||
				(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue()) || remarksFromDeptHead.getValue().isEmpty())){
				hasError = true;
				errorMessages.add(SHAConstants.USER_ROLE_VALIDATION + " </br>");
			}*/
			if(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue())){
				hasError = true;
				errorMessages.add("Please provide Opinion Given is mandatory"+ "</br>");
			}
			
//			R0001	MEDICAL HEAD
//			R0002	UNIT HEAD
//			R0003	DIVISION HEAD
//			R0004	CLUSTER HEAD
//			R0005	SPECIALIST
//			R0006	ZONAL HEAD
//			R00011	MEDICAL HEAD - GMC
//			R00012	DEPUTY MEDICAL HEAD - GMC
			List<String> roleList = null;
			Map<String, String> selectedRole = getRoleValidationContainer();
			Map<String, String> selectedUser = getUserValidationContainer();
			
			System.out.println("Role Val :"+selectedRole);
			System.out.println("User Val :"+selectedUser);
			if(bean.getPreauthMedicalDecisionDetails().getIsSDMarkedForOpinion()){
				System.out.println("ClaimType : "+bean.getNewIntimationDTO().getClaimType().getValue());
				boolean roleAvailabilityFlag = false;
				if(bean.getNewIntimationDTO().getClaimType() != null && bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
					roleList =  Arrays.asList("R0002", "R0006");
					hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
				}else{
					roleList =  Arrays.asList("R0002");
					hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
				}

			}else{
				if(bean.getPreauthMedicalDecisionDetails().getIsPortedPolicy()){
					System.out.println("This is a Ported Policy.......");
					//				if((Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L) && bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						roleList =  Arrays.asList("R0004", "R0003", "R0001", "R00011", "R00012", "R0007");
					}
					System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());
					List<String> userPortedActionList = Arrays.asList("Cancel ROD");
					boolean roleAvailabilityFlag = false;
					if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
						hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
					}else if(userPortedActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){					
						roleList =  Arrays.asList("R0004", "R0003", "R0001", "R0007");
						hasError = doPortedPolicyRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						System.out.println("CRPC Ported Policy ErrorFlg : "+hasError);
					}

				}else{

					if(Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L){
						roleList =  Arrays.asList("R0001", "R0002", "R00011", "R00012", "R0007");
					}else{
						roleList =  Arrays.asList("R0003", "R0001", "R0002", "R00011", "R00012", "R0007");
					}

					System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());

					boolean roleAvailabilityFlag = false;
					List<String> userActionList = Arrays.asList("Cancel ROD");
					if(userActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){
						hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						System.out.println("CRPC ErrorFlg : "+hasError);
					}
				}
			}
		}else if(!bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
			String nonMandateerrorMessage = "Please provide Consulted With, Opinion Given by, and Opinion Given or Make Consulted With, Opinion Given by, and Opinion Given as Empty"+"</br>";
			if(cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null){
				if((!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if((cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if((cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue()))){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}					

				if(!cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if(!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}

				if(cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
					hasError = true;
					errorMessages.add(nonMandateerrorMessage);
				}
			}
		} 
		if(!hasError){
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
				StringBuffer eMsg = new StringBuffer();
				if (isValid()) {
					wizard.getNextButton().setEnabled(false);
					wizard.getFinishButton().setEnabled(true);
				//	fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_PARALLEL_QUERY,bean);
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

	private Button getSubmitButtonWithDMS(final ConfirmDialog dialog) {

		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();

				if (isValid()) {
					uploadFile.submitUpload();
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

	public void buildInitiateFieldVisit(Object fieldVisitValues) {
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
		
		/*fvrAssignTo = (ComboBox) binder.buildAndBind("Assign To","assignTo",ComboBox.class);
		fvrAssignTo.setContainerDataSource(map.get("fvrAssignTo"));
		fvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrAssignTo.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getAssignTo() != null){
			fvrAssignTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAssignTo());
		}*/
		
		fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
		fvrPriority.setContainerDataSource(map.get("fvrPriority"));
		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrPriority.setItemCaptionPropertyId("value");
		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
		}

//		fvrTriggerPoints = (TextArea) binder.buildAndBind("FVR Trigger Points","fvrTriggerPoints",TextArea.class);
//		fvrTriggerPoints.setMaxLength(4000);
//		fvrTriggerPoints.setWidth("400px");
		
		triggerPtsTableObj = triggerPtsTable.get();
		triggerPtsTableObj.init();
		if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
			triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
		}
		viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
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
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
		horizontalLayout2.setSpacing(true);
//		horizontalLayout2.setHeight("px");
		
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
//		mandatoryFields.add(fvrTriggerPoints);
		//mandatoryFields.add(fvrAssignTo);
		mandatoryFields.add(fvrPriority);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
//		btnLayout.setWidth("600px");
//		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		btnLayout.setSpacing(true);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj, btnLayout);
//		VLayout.setWidth("850px");
//		VLayout.setMargin(true);
//		VLayout.setSpacing(true);
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

			/**
			 * 
			 */
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

	}

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
						if (bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							min = Math.min(
									amountConsideredTable.getMinimumValueForGMC(),
									SHAUtils.getIntegerFromString(medicalDecisionTableObj.dummyField
											.getValue()));
							
							if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
								Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
								Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
								if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
									bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
								}
								
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
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
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
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
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
						if (bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							min = Math.min(
									amountConsideredTable.getMinimumValueForGMC(),
									SHAUtils.getIntegerFromString(medicalDecisionTableObj.dummyField
											.getValue()));
							
							if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
								Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
								Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
								if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
									bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
								}
								
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
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
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
									/*min = Math.min(amountConsideredTable
											.getConsideredAmountValue(),
											totalApprovedAmt);*/
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
				if(null != bean.getIsParallelInvFvrQuery() && bean.getIsParallelInvFvrQuery()){
					bean.setStatusKey(bean.getParallelStatusKey());
				}
				binder = null;
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
					uploadStatus = null;
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
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
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
				.createInformationBox(SHAConstants.PED_WATCHLIST, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public void setButtonDisabledForconsiderforPayment(Boolean visible){
		
		if (visible) {
			approveBtn.setEnabled(false);
		//	queryBtn.setEnabled(false);
			sendReplyBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			escalteReplyBtn.setEnabled(false);
		//	initiateFieldVisitBtn.setEnabled(false);
		//	initiateInvestigationBtn.setEnabled(false);
			referToSpecialistBtn.setEnabled(false);
			rejectBtn.setEnabled(true);
			cancelROD.setEnabled(false);
		}
	}
		
	/*public Boolean alertMessageForInvestigation() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'> Investigation Request has already been initiated for this Claim once </b>",
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
				bean.setIsInvestigation(false);
			}
		});
		return true;
	}
	*/
	public void alertMessageForInvestigation() {
		
		String message = "Investigation Request has already been initiated. </br> Do you still want to initiate another Investigation request?</b>";
	/*	
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
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
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();		
	
				fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_INVESTIGATION_EVENT,null);				
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				wizard.getFinishButton().setEnabled(true);
				bean.setIsInvestigation(false);
//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
	}
	
	public boolean isValidButton(){
		
		Boolean hasError = false;
		String msg = null;
		if (this.binder == null) {
			hasError = true;				
		 }
		 return hasError;
	}
	
	private HorizontalLayout generateFieldBasedOnCorpBuffer(){
		
		Integer amountConsidered2 = Integer.valueOf(this.bean.getAmountConsidered());
		Integer corpBufferAllocatedClaim = this.bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
		Integer corpBufferLimit = this.bean.getPreauthDataExtractionDetails().getCorpBufferLimit().intValue();
		Integer utilizedCorpBufferAmount =this.bean.getPreauthDataExtractionDetails().getCorpBufferUtilizedAmt();
		Integer availableAmt = 0;
		if(corpBufferAllocatedClaim != null){
				availableAmt = corpBufferAllocatedClaim - utilizedCorpBufferAmount;
		}
		String bufferType ="Corporate";
		if(bean.getPreauthDataExtractionDetails().getBufferTypeValue() != null &&
				!bean.getPreauthDataExtractionDetails().getBufferTypeValue().isEmpty()){
		 bufferType = bean.getPreauthDataExtractionDetails().getBufferTypeValue();
		}
		Table corpTable  = new Table();
		corpTable.setWidth("80%");
		corpTable.addContainerProperty("Particulars", String.class, null);
		corpTable.addContainerProperty("Amount",  Integer.class, null);
		//corpTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		corpTable.addItem(new Object[]{bufferType+" Buffer Allocated for Employee", corpBufferAllocatedClaim },1);
		corpTable.addItem(new Object[]{bufferType+" Buffer utilized Amount", utilizedCorpBufferAmount },2);
		corpTable.addItem(new Object[]{"Available Balance ", availableAmt },3);
		corpTable.setPageLength(3);
		corpTable.setCaption(bufferType+" Buffer Details");
		
		Integer stopLossAvailableAmt = bean.getPreauthMedicalDecisionDetails().getStopLossAvailableAmt();
		
		Table stopLossTable  = new Table();
		stopLossTable.setWidth("80%");
		stopLossTable.addContainerProperty("Particulars", String.class, null);
		stopLossTable.addContainerProperty("Amount",  Integer.class, null);
		//stopLossTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		stopLossTable.addItem(new Object[]{"Stop Loss Amount Available(excl this claim)", stopLossAvailableAmt },1);
		stopLossTable.setPageLength(1);
		stopLossTable.setCaption("Stop Loss Details");
		
		if(bufferType != null && bufferType.equalsIgnoreCase("NACB")) {
			List<RODBillDetails> billEntryDetails = ackDocReceivedService.getBillEntryDetails(bean.getKey());
			 if(billEntryDetails != null && !billEntryDetails.isEmpty()){
				 Double nonPayableAmt = 0d;
				 Double deductibleAmt = 0d;
				 for (RODBillDetails rodBillDetails : billEntryDetails) {
					 nonPayableAmt += rodBillDetails.getNonPayableAmount() != null ?  rodBillDetails.getNonPayableAmount() : 0d;
					 deductibleAmt += rodBillDetails.getDeductibleAmount() != null ?  rodBillDetails.getDeductibleAmount() : 0d;
				 }
				 bean.getPreauthDataExtractionDetails().setNonPayableAmt(nonPayableAmt.intValue());
				 bean.getPreauthDataExtractionDetails().setDeductibleAmt(deductibleAmt.intValue());
			 }
			Integer nonPayableAmt =this.bean.getPreauthDataExtractionDetails().getNonPayableAmt();
			Integer deductibleAmt =this.bean.getPreauthDataExtractionDetails().getDeductibleAmt();
			Integer totalnonpayableDedAmt =nonPayableAmt + deductibleAmt;
			Integer amountConsider = this.bean.getPreauthDataExtractionDetails().getNacBufferUtilizedAmount();
		Table nacbNonPyableTab  = new Table();
		nacbNonPyableTab.setWidth("80%");
		nacbNonPyableTab.addContainerProperty("Particulars", String.class, null);
		nacbNonPyableTab.addContainerProperty("Amount",  TextField.class, null);
		//corpTable.setStyleName(ValoTheme.TABLE_NO_HEADER);
		TextField nonPayableAmtTxt = new TextField();
		nonPayableAmtTxt.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		nonPayableAmtTxt.setReadOnly(false);
		nonPayableAmtTxt.setValue(nonPayableAmt.toString());
		nonPayableAmtTxt.setReadOnly(true);
		TextField deductibleAmtTxt = new TextField();
		deductibleAmtTxt.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		deductibleAmtTxt.setReadOnly(false);
		deductibleAmtTxt.setValue(deductibleAmt.toString());
		deductibleAmtTxt.setReadOnly(true);
		totalnonpayableDedAmtTxt = new TextField();
		totalnonpayableDedAmtTxt.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		totalnonpayableDedAmtTxt.setReadOnly(false);
		totalnonpayableDedAmtTxt.setValue(totalnonpayableDedAmt.toString());
		totalnonpayableDedAmtTxt.setReadOnly(true);
		 amountConsidered2Txt = new TextField();
		 amountConsidered2Txt.setValue(amountConsider.toString());
		nacbNonPyableTab.addItem(new Object[]{"Deductible Amount", deductibleAmtTxt },1);
		nacbNonPyableTab.addItem(new Object[]{"Non Payable Amount", nonPayableAmtTxt },2);
		nacbNonPyableTab.addItem(new Object[]{"Total Deductible/Non Payables Amount ", totalnonpayableDedAmtTxt },3);
		nacbNonPyableTab.addItem(new Object[]{"Amount Considered ", amountConsidered2Txt },4);
		nacbNonPyableTab.setPageLength(3);
		nacbNonPyableTab.setCaption("Deductible/Non Payable Details");
		corpBufferLayout = new HorizontalLayout(corpTable,stopLossTable,nacbNonPyableTab);
		}
		else{
		corpBufferLayout = new HorizontalLayout(corpTable,stopLossTable);
		}
		corpBufferLayout.setSpacing(true);
		
		return corpBufferLayout;

	}
public void showErrorPopUp(String emsg) {
	Label label = new Label(emsg, ContentMode.HTML);
    label.setStyleName("errMessage");
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.addComponent(label);
    
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setCaption("Warning");
    dialog.setClosable(true);
    dialog.setContent(layout);
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.show(getUI().getCurrent(), null, true);
}
	
public void buildInitiateParallelFieldVisit(Object fieldVisitValues) {
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
	
	/*fvrAssignTo = (ComboBox) binder.buildAndBind("Assign To","assignTo",ComboBox.class);
	fvrAssignTo.setContainerDataSource(map.get("fvrAssignTo"));
	fvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	fvrAssignTo.setItemCaptionPropertyId("value");
	if(this.bean.getPreauthMedicalDecisionDetails().getAssignTo() != null){
		fvrAssignTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAssignTo());
	}*/
	
	fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
	fvrPriority.setContainerDataSource(map.get("fvrPriority"));
	fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	fvrPriority.setItemCaptionPropertyId("value");
	if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
		fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
	}

//	fvrTriggerPoints = (TextArea) binder.buildAndBind("FVR Trigger Points","fvrTriggerPoints",TextArea.class);
//	fvrTriggerPoints.setMaxLength(4000);
//	fvrTriggerPoints.setWidth("400px");
	
	triggerPtsTableObj = triggerPtsTable.get();
	triggerPtsTableObj.init();
	if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
		triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
	}
	viewFVRDetails = new Button("View FVR Details");
	viewFVRDetails.addClickListener(new Button.ClickListener() {

		/**
		 * 
		 */
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
	
	HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
	horizontalLayout2.setSpacing(true);
//	horizontalLayout2.setHeight("px");
	
	alignFormComponents();
	mandatoryFields= new ArrayList<Component>();
	mandatoryFields.add(allocationTo);
//	mandatoryFields.add(fvrTriggerPoints);
	//mandatoryFields.add(fvrAssignTo);
	mandatoryFields.add(fvrPriority);
	showOrHideValidation(false);
	
	final ConfirmDialog dialog = new ConfirmDialog();
	Button submitButtonWithListener = getSubmitFVRButtonWithListener(dialog);
	
	HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
//	btnLayout.setWidth("600px");
//	btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
	btnLayout.setSpacing(true);
	showOrHideValidation(false);
	
	VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj, btnLayout);
//	VLayout.setWidth("850px");
//	VLayout.setMargin(true);
//	VLayout.setSpacing(true);
	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	showInPopup(VLayout, dialog);
	
}


private Button getSubmitFVRButtonWithListener(final ConfirmDialog dialog) {
	submitButton = new Button("Submit");
	submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	submitButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = -5934419771562851393L;

		@Override
		public void buttonClick(ClickEvent event) {
			StringBuffer eMsg = new StringBuffer();
			if (isValid()) {
				reimbService.initiateFVR(bean,SHAConstants.CLAIM_REQUEST);				
				bean.getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);
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

	public void showPendingFvrInvsQueryPopup(final PreauthDTO bean,List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList,String screenName,String event){

	final Window popup = new com.vaadin.ui.Window();
	
		invesFvrQueryPendingPageUI.init(pendingFvrInvsQueryList, bean, popup,screenName,event);
	
		popup.setWidth("75%");
		popup.setHeight("90%");
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
	
	
	public Boolean alertMessageForPendingCases(List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrQueryInvsList) {/*
		
		Label alertMsg = new Label();
		
		for (InvesAndQueryAndFvrParallelFlowTableDTO invesAndQueryAndFvrParallelFlowTableDTO : pendingFvrQueryInvsList) {
			
			if(null != invesAndQueryAndFvrParallelFlowTableDTO && null != invesAndQueryAndFvrParallelFlowTableDTO.getType()){
				if((SHAConstants.PARALLEL_FVR_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					  bean.setIsFvrPending(Boolean.TRUE);
				}
				if((SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					bean.setIsInvsPending(Boolean.TRUE);
				}
				if((SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					bean.setIsQueryPending(Boolean.TRUE);
				}
			}
		}
				if((bean.getIsFvrPending()) && (bean.getIsInvsPending()) && (bean.getIsQueryPending())){
					
					alertMsg = new Label(
							"<b style = 'color: red;'>" + "FVR, Investigation And Query Reply are pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
					
				}
				else if((bean.getIsFvrPending()) && (bean.getIsInvsPending())){
					
					alertMsg = new Label(
							"<b style = 'color: red;'>" + "FVR and Investigation Reply are pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
				}
				else if((bean.getIsFvrPending()) && (bean.getIsQueryPending())){

					alertMsg = new Label(
							"<b style = 'color: red;'>" + "FVR and Query Reply are pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
				}
				
				else if((bean.getIsInvsPending()) && (bean.getIsQueryPending())){

					alertMsg = new Label(
							"<b style = 'color: red;'>" + "Investigation and Query Reply are pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
				}
				else if((bean.getIsFvrPending())){
					
					 alertMsg = new Label(
							 "<b style = 'color: red;'>" + "FVR Reply is pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
				}
				else if((bean.getIsInvsPending())){
					
					alertMsg = new Label(
							"<b style = 'color: red;'>" + "Investigation Reply is pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML); 
				}
				else if((bean.getIsQueryPending())){
					
					alertMsg = new Label(
							"<b style = 'color: red;'>" + "Query Reply is pending" + "<br> Do you want to proceed?" + "</b>",
							ContentMode.HTML);
				}
				
   		
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(alertMsg, homeButton);
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
				
				buildLayoutBasedOnButtons();
		}
		});
		return true;
	*/

		
		Label alertMsg = new Label();
		
		for (InvesAndQueryAndFvrParallelFlowTableDTO invesAndQueryAndFvrParallelFlowTableDTO : pendingFvrQueryInvsList) {
			
			if(null != invesAndQueryAndFvrParallelFlowTableDTO && null != invesAndQueryAndFvrParallelFlowTableDTO.getType()){
				if((SHAConstants.PARALLEL_FVR_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					  bean.setIsFvrPending(Boolean.TRUE);
				}
				if((SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					bean.setIsInvsPending(Boolean.TRUE);
				}
				if((SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(invesAndQueryAndFvrParallelFlowTableDTO.getType())){
					bean.setIsQueryPending(Boolean.TRUE);
				}
			}
		}
				if((bean.getIsFvrPending()) && (bean.getIsInvsPending()) && (bean.getIsQueryPending())){
					
					alertMsg = new Label("FVR, Investigation And Query Reply are pending" + "<br> Do you want to proceed?",ContentMode.HTML);
					
				}
				else if((bean.getIsFvrPending()) && (bean.getIsInvsPending())){
					
					alertMsg = new Label("FVR and Investigation Reply are pending" + "<br> Do you want to proceed?",ContentMode.HTML);
				}
				else if((bean.getIsFvrPending()) && (bean.getIsQueryPending())){

					alertMsg = new Label("FVR and Query Reply are pending" + "<br> Do you want to proceed?",ContentMode.HTML);
				}
				
				else if((bean.getIsInvsPending()) && (bean.getIsQueryPending())){

					alertMsg = new Label("Investigation and Query Reply are pending" + "<br> Do you want to proceed?",ContentMode.HTML);
				}
				else if((bean.getIsFvrPending())){
					
					 alertMsg = new Label("FVR Reply is pending" + "<br> Do you want to proceed?",ContentMode.HTML);
				}
				else if((bean.getIsInvsPending())){
					
					alertMsg = new Label("Investigation Reply is pending" + "<br> Do you want to proceed?",ContentMode.HTML); 
				}
				else if((bean.getIsQueryPending())){
					
					alertMsg = new Label("Query Reply is pending" + "<br> Do you want to proceed?",ContentMode.HTML);
				}
				
   		
   		final Boolean isClicked = false;
		VerticalLayout layout = new VerticalLayout(alertMsg);
		layout.setSpacing(true);
		layout.setMargin(true);
		
		//IMSSUPPOR-28422
		/*HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox.createQuestionCust("Confirmation", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
				
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
		cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				if(null != bean.getIsParallelInvFvrQuery() && bean.getIsParallelInvFvrQuery()){
					bean.setStatusKey(bean.getParallelStatusKey());
				}
				
			}
		});
		//IMSSUPPOR-28422

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				
				buildLayoutBasedOnButtons();
		}
		});
		return true;
		
	}

			
		public void buildLayoutBasedOnButtons(){
			
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//				Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
				Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
				bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
			}else{
				bean.setIsPedWatchList(false);
			}
			
			if(! bean.getIsPedWatchList()){
				
				if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS.equals(bean.getStatusKey()))){
			fireViewEvent(
					ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_ESCALATE_EVENT,
					null);
				}
				else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS.equals(bean.getStatusKey()))){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_ESCALATE_REPLY_EVENT,
							null);	
				}
				else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS.equals(bean.getStatusKey()))){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_REFERCOORDINATOR_EVENT,
							null);	
				}
				else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS.equals(bean.getStatusKey()))){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_SPECIALIST_EVENT,
							null);	
				}
				else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS.equals(bean.getStatusKey()))){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,
							null);	
				}
				/*else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS.equals(bean.getStatusKey()))){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,
							null);	
				}*/
				
			}else{
		
				alertMessageForPEDWatchList();
			}
				
			
		}

		public FormLayout buildUserRoleLayout(){
			String enhancementType = "F";
			String hospitilizationType = "N";
			if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()){
				hospitilizationType = "Y";
			}
			//R1295
			Integer qryTyp = 0;
			Integer qryCnt = 0;
			
			String reconsiderationFlag = null != bean.getIsRejectReconsidered() && bean.getIsRejectReconsidered() ? "Y" : "N";
			
			String finalClaimAmount = "";
			if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
				finalClaimAmount = bean.getAmountConsidered();
			}else{
				finalClaimAmount = String.valueOf(createRodService.getTotalClaimedAmt(bean.getKey(), ReferenceTable.HOSPITALIZATION).longValue());
			}
			finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
			bean.getPreauthMedicalDecisionDetails().setFinalClaimAmout(Long.valueOf(finalClaimAmount));
			
			System.out.println("CRPC User Role For Intimation No "+bean.getNewIntimationDTO().getIntimationId());
			
			Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getStageKey(),bean.getStatusKey(),
					Long.valueOf(bean.getNewIntimationDTO().getCpuCode()),reconsiderationFlag,bean.getNewIntimationDTO().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType,hospitilizationType,qryTyp,qryCnt,
					bean.getClaimDTO().getClaimType().getId(),bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey(),SHAConstants.CLAIM_REQUEST,
					bean.getKey(), bean.getStatusKey(),SHAConstants.N_FLAG); 
			
			BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
			userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));
			BeanItemContainer<SpecialSelectValue> userRoleWithoutSGm = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
			if(bean.getNewIntimationDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS)) {
				
				for (SpecialSelectValue component : userRole.getItemIds()) {
					if(!component.getSpecialValue().equalsIgnoreCase("R0007")) {
						userRoleWithoutSGm.addItem(component);
						
					}
				}
			}
			BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
			empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));
			
			if(null != opinionValues){			
				String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
				if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
					bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
				}
				else{
					bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				}
				
				String portedFlag =  (String) opinionValues.get("portedFlag");
				if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(portedFlag)){
					bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.TRUE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.FALSE);
				}
				
				String seriousDefiencyFlagForOpinion = (String) opinionValues.get("seriousDeficiencyFlag");
				System.out.println("seriousDefiencyFlagForOpinion : "+seriousDefiencyFlagForOpinion);
				if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.TRUE);
				}else if(null != portedFlag && SHAConstants.N_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.FALSE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(null);
				}
			}
			cmbUserRoleMulti = new ComboBoxMultiselect("Consulted With");
			cmbUserRoleMulti.setShowSelectedOnTop(true);
			cmbUserRoleMulti.setComparator(SHAUtils.getComparator());
			if(bean.getNewIntimationDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS)) {
				cmbUserRoleMulti.setContainerDataSource(userRoleWithoutSGm);
			}else{
			cmbUserRoleMulti.setContainerDataSource(userRole);
			}
			cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbUserRoleMulti.setItemCaptionPropertyId("value");	
			cmbUserRoleMulti.setData(userRole);
			
					
			cmbDoctorNameMulti = new ComboBoxMultiselect("Opinion Given by");
			cmbDoctorNameMulti.setShowSelectedOnTop(true);
			cmbDoctorNameMulti.setContainerDataSource(empNames);
			cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
			cmbDoctorNameMulti.setData(empNames);
			
			remarksFromDeptHead = (TextArea) binder.buildAndBind("Opinion Given", "remarksFromDeptHead",TextArea.class);		
			remarksFromDeptHead.setMaxLength(2000);
			remarksFromDeptHead.setWidth("300px");
			addUserRoleListener();
			FormLayout fLayout = new FormLayout();
			fLayout.addComponents(cmbUserRoleMulti,cmbDoctorNameMulti,remarksFromDeptHead);
			fLayout.setMargin(Boolean.TRUE);
			fLayout.setSpacing(Boolean.TRUE);
			if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
				mandatoryFields.add(cmbUserRoleMulti);
				mandatoryFields.add(cmbDoctorNameMulti);
				mandatoryFields.add(remarksFromDeptHead);
				showOrHideValidation(false);
			}
			else{
				mandatoryFields.remove(cmbUserRoleMulti);
				mandatoryFields.remove(cmbDoctorNameMulti);
				mandatoryFields.remove(remarksFromDeptHead);
			}
			return fLayout;	
			
		}


		private void cancelRodAction(){
			isApproval = true;
			bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
			bean.getPreauthMedicalDecisionDetails().setUserClickAction("Cancel ROD");
			List<InvesAndQueryAndFvrParallelFlowTableDTO> pendingFvrInvsQueryList = dbCalculationService.getParallelFlowFvrInvsQuery(bean.getKey(),
					SHAConstants.MA_Q,bean.getNewIntimationDTO().getKey(),null != bean.getClaimDTO().getLatestPreauthKey()? bean.getClaimDTO().getLatestPreauthKey() : 0L,
							bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());

			if(null != pendingFvrInvsQueryList && !pendingFvrInvsQueryList.isEmpty()){

				showPendingFvrInvsQueryPopup(bean,pendingFvrInvsQueryList,SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING,SHAConstants.CANCEL_ROD);

			}
			else
			{

				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
					//					Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
					Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
					bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}

				if(! bean.getIsPedWatchList()){
					fireViewEvent(
							ClaimRequestPremedicalProcessingPagePresenter.CLAIM_CANCEL_ROD_EVENT,
							null);
				}else{
					alertMessageForPEDWatchList();
				}
			}

		}

		public void addUserRoleListener(){
			getRoleValidationContainer().clear();
			getUserValidationContainer().clear();
			cmbUserRoleMulti.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
					PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
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
					BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
					PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
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
					bean.getPreauthMedicalDecisionDetails().setDoctorContainer(listOfDoctors);
				}
			});
		}
		
		private boolean doPortedPolicyRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			if(selectedRole.get("R0005") != null){
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}

				if(!roleAvailabilityFlag){
					System.out.println("Specialist Alone Selected.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical)"+"</br>");
				}

				if(!hasError){					
					if(selectedUser != null){
						if(selectedUser.get("R0005") == null){
							hasError = true;
							errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
						}
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						if(selectedUser.get("R0005") != null){
							System.out.println("Specialist is Selected");
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
								if(roleAvailabilityFlag){
									break;
								}
							}

							if(!roleAvailabilityFlag){
								System.out.println("Specialist User Alone Selected.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
							}
						}

						for (Map.Entry<String, String> entry : selectedUser.entrySet()){
							if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP 0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
								break;
							}
							if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("PP !0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head /  Division Head / Cluster Head )"+"</br>");
								break;
							}
							System.out.println(entry.getKey() + "/" + entry.getValue());
						}
					}else{
						hasError = true;
						errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Selected user Map is empty.......");
					}
				}
			}else{
				//			R0001	MEDICAL HEAD
				//			R0002	UNIT HEAD
				//			R0003	DIVISION HEAD
				//			R0004	CLUSTER HEAD
				//			R0005	SPECIALIST
				//			R0006	ZONAL HEAD
				//			R00011	MEDICAL HEAD - GMC
				//			R00012	DEPUTY MEDICAL HEAD - GMC

				if((selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") == null && selectedRole.get("R0001") == null)){
					hasError = true;
					errorMessages.add("Cluster Head/ Division Head selection is mandatory"+"</br>");
					System.out.println("PP : Cluster Head/ Division Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") == null) || (selectedRole.get("R0004") == null && selectedRole.get("R0001") != null)){
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by Medical Head along with Cluster Head or Specialist"+"</br>");
					System.out.println("PP : Cluster Head/ Medical Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") != null && selectedRole.get("R0001") == null || (selectedRole.get("R0003") == null && selectedRole.get("R0001") != null))){
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by Medical Head along with Divisional Head or Specialist"+"</br>");
					System.out.println("PP : Division Head/ Medical Head not selected");
				}

				if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") != null) || (selectedRole.get("R0003") != null && selectedRole.get("R0001") != null)){
					if(selectedUser == null || selectedUser.size() == 0){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}else if(selectedUser != null){
						if((selectedUser.get("R0004") != null) && (selectedUser.get("R0001") == null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null) && (selectedUser.get("R0001") != null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") != null) && (selectedUser.get("R0001") == null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

						if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") == null) && (selectedUser.get("R0001") != null)){
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("PP : Division Head/ Medical Head not selected");
						}

					}
				}

			}
			return hasError;
		}
		
		private boolean doRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			if(selectedRole.get("R0005") != null){
				System.out.println("Specialist is Selected");
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}
				
				if(!roleAvailabilityFlag){
					System.out.println("Specialist Alone Selected.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical)"+"</br>");
				}
				
				if(!hasError){					
					if(selectedUser != null){
						if(selectedUser.get("R0005") == null){
							hasError = true;
							errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
						}
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						if(selectedUser.get("R0005") != null){
							System.out.println("Specialist is Selected");
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
								if(roleAvailabilityFlag){
									break;
								}
							}
							
							if(!roleAvailabilityFlag){
								System.out.println("Specialist User Alone Selected.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
							}
						}
						
						
						for (Map.Entry<String, String> entry : selectedUser.entrySet()){
							if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
								break;
							}
							if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
								hasError = true;
								System.out.println("! 0005: "+entry.getKey()+"<------>"+entry.getValue());
								errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical / Medical Head - GMC / Deputy Medical Head - GMC)"+ "</br>");
								break;
							}
							System.out.println(entry.getKey() + "/" + entry.getValue());
						}
					}else{
						hasError = true;
						errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("Selected user Map is empty.......");
					}
				}
			}else{
				if(selectedRole != null && selectedRole.size() > 0){
					for(String key : roleList){
						if(selectedRole.containsKey(key)){
							roleAvailabilityFlag = true;
						}
						if(roleAvailabilityFlag){
							break;
						}
					}
					
					if(!roleAvailabilityFlag){
						System.out.println("Required Department Role is not Selected / not Available.");
						hasError = true;
						errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head)"+"</br>");
					}
					
					if(!hasError){
						if(selectedUser != null){
							roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
							for(String key : roleList){
								if(selectedUser.containsKey(key)){
									roleAvailabilityFlag = true;
								}
							}
							
							if(!roleAvailabilityFlag){
								System.out.println("Required Department User is not Selected / not Available.");
								hasError = true;
								errorMessages.add("User Selection is Mandatory, Please select User for the selected Department Heads"+"</br>");
							}
							
						}else{
							hasError = true;
							errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
							System.out.println("Selected user Map is empty.......");
						}
					}
					
				}else{
					hasError = true;
					errorMessages.add("Consulted With selection is Mandatory"+"</br>");
					System.out.println("Selected user Map is empty.......");
				}
			}
			
			return hasError;	
		}
		
		 private boolean doOpinionSDValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
			 if(selectedRole != null && selectedRole.size() > 0){
				 for(String key : roleList){
					 if(selectedRole.containsKey(key)){
						 roleAvailabilityFlag = true;
					 }
					 if(roleAvailabilityFlag){
						 break;
					 }
				 }

				 if(!roleAvailabilityFlag){
					 System.out.println("SDValidation Required Department Role is not Selected / not Available.");
					 hasError = true;
					 if(bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
						 errorMessages.add("Intimation Decision has to be validated by Zonal Head / Unit Head"+"</br>");
					 }else{
						 errorMessages.add("Intimation Decision has to be validated by Unit Head"+"</br>");
					 }
				 }

				 if(!hasError){
					 if(selectedUser != null){
						 roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						 for(String key : roleList){
							 if(selectedUser.containsKey(key)){
								 roleAvailabilityFlag = true;
							 }
						 }

						 if(!roleAvailabilityFlag){
							 System.out.println("SDValidation Required Department User is not Selected / not Available.");
							 hasError = true;
							 errorMessages.add("User Selection is Mandatory, Please select User for the selected department"+"</br>");
						 }

					 }else{
						 hasError = true;
						 errorMessages.add("User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						 System.out.println("SDValidation Selected user Map is empty.......");
					 }
				 }

			 }else{
				 hasError = true;
				 errorMessages.add("Consulted With selection is Mandatory"+"</br>");
				 System.out.println("SDValidation Selected user Map is empty outer.......");
			 }
			 return hasError;
		 }
}
