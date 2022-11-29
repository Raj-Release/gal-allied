package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ContributeRRCPopupUI;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.DMSDocumentViewListenerTable;
import com.shaic.claim.SumInsuredBonusAlertUI;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.preauth.wizard.pages.DiagnosisProcedureListenerTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationPageUI;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationViewImpl;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.financialapproval.wizard.ViewBillAssessment;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
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
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.RevisedDraftInvTriggerPointsTable;
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
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
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
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class FinancialButtons  extends ViewComponent {
	private static final long serialVersionUID = 7089191150922046819L;
	

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	@Inject
	private FinancialDecisionCommunicationPageUI financialDecisionCommunicationPageObj;
	
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
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ReimbursementService reimbService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	private AmountConsideredTable amountConsideredTable;
	
	private Window popup;
	
	private DiagnosisProcedureListenerTable medicalDecisionTableObj;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private TextArea approvalRemarks;
	
	private Button submitButton;

	private Label amountConsidered;
	
	private Boolean isDocChecked = false;
	
	@Inject
	private ViewDetails viewDetails;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	

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
	private Button btnBillAssessmentSheet;
	
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
	private TextArea rejectionRemarksTxta;
	private ComboBox cmbRejectonCategory;
	private ComboBox rejectionSubCategoryCmb;
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
	
	private TextField txtDefinedLimit;
	
	private ComboBox allocationTo;
	//private ComboBox fvrAssignTo;
	private ComboBox fvrPriority;
	
//	private TextArea fvrTriggerPoints;
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
	
	private BrowserWindowOpener claimsDMSWindow;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	private Button viewClaimsDMSDocument;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	private Button referToBillEntry;
	
	private TextArea txtBillEntryBillingRemarks;

	private FinancialHospitalizationPageUI financialHospitalizationPageUI;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	private TextField amountClaimedFrmHospTxt;
	
	@Inject
	private Instance<RevisedDraftInvTriggerPointsTable> draftTriggerPointsTableInstance;
	
	private RevisedDraftInvTriggerPointsTable draftTriggerPointsTableInstanceObj;
	
	@Inject
	private ContributeRRCPopupUI contributeRRCPopupUI;
	
	private Label hospitalDiscountLbl;
	
	private CheckBox hospitalDiscountChk;
	
	private Button submitBtnForhospitalDiscountWithListener;
	
	private OptionGroup verifiedBonus;

	private FormLayout bonusFrmLayout;

	private OptionGroup relInstalOpt;
	
	@Inject
	private SumInsuredBonusAlertUI bonusAlertUI;
	
	private Button holdBtn;
	
	private TextArea holdRemarksTxta;
	
	private HorizontalLayout remarksLayout;
	
	VerticalLayout vertLayout = new VerticalLayout();
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public void initView(PreauthDTO bean, GWizard wizard) 
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
		if(bean.getIsDishonoured() || bean.getIsHospitalizationRejected()) {
			referToSpecialistBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			referToMedicalApproverBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
			referToBillingBtn.setEnabled(false);
			initiateFieldVisitBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			approveBtn.setEnabled(false);
			cancelROD.setEnabled(false);
			rejectBtn.setEnabled(true);
		} else {
			referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			if(bean.getScreenName() != null 
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName()) ||
					(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()))) {
				referToBillingBtn.setEnabled(false);
			}
			else {
				referToBillingBtn.setEnabled(true);
			}	
			initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(true);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		}
		
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			initiateFieldVisitBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
		}
		if(bean.getScreenName() != null 
				&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName()) ||
				(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()))) {
			referToBillingBtn.setEnabled(false);
		}
		
		if(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()) ||
				bean.getScreenName() != null && SHAConstants.FINANCIALS_APPROVAL_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())) {
			holdBtn.setEnabled(true);
		}else {
			holdBtn.setEnabled(false);
		}
		
		HorizontalLayout buttonFirstLayout = new HorizontalLayout(referToSpecialistBtn, referCoordinatorBtn, referToMedicalApproverBtn , initiateInvestigationBtn,holdBtn);
		HorizontalLayout buttonSecondLayout = new HorizontalLayout( referToBillingBtn, initiateFieldVisitBtn,  queryBtn, rejectBtn, approveBtn,cancelROD,btnBillAssessmentSheet,referToBillEntry);
		
		//initiateInvestigationBtn.setEnabled(Boolean.FALSE);
		
		if(null != referToBillEntry)
			referToBillEntry.setEnabled(true);
		
		if(null != bean.getIsRejectReconsidered() && !bean.getIsRejectReconsidered())
		{
			if(null != referToBillEntry)
			{
				referToBillEntry.setEnabled(false);
			}
		}
		
	//	HorizontalLayout buttonSecondLayout = new HorizontalLayout( referToBillingBtn, initiateFieldVisitBtn,  queryBtn, rejectBtn, approveBtn,cancelROD,btnBillAssessmentSheet);
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
		
		remarksLayout = new HorizontalLayout();
		remarksLayout.setWidth("100%");
		remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);

	/*	claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
		claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
		claimsDMSWindow.extend(viewClaimsDMSDocument);*/
		
		/*if(this.bean.getIsQueryReceived() || this.bean.getIsReconsiderationRequest()){
			cancelROD.setEnabled(false);
		}*/
		viewAllDocsLayout = new HorizontalLayout();
		
		addViewAllDocsListener();
		if(this.bean.getIsReleased()){
			approveBtn.setEnabled(true);
		}
		if(this.bean.getIsPending()){
			/**
			 * As per requirement, the below code is commented, (R0761)
			 */
			//approveBtn.setEnabled(false);
			if(bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				approveBtn.setEnabled(true);
			}else{
				approveBtn.setEnabled(false);
			}
			
			
		}
		if(this.bean.getIsDishonoured()){
			approveBtn.setEnabled(false);
		}
		if(bean.getApproveBtnFlag()){
			approveBtn.setEnabled(false);
		}else{
			approveBtn.setEnabled(true);
		}
		
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
			
			if(bean.getPreauthDataExtractionDetails().getIsStarGrpCorApproveBtn()){
				approveBtn.setEnabled(false);
			}
			if(bean.getPreauthDataExtractionDetails().getTreatmentType() != null && bean.getPreauthDataExtractionDetails().getTreatmentType().getId().equals(ReferenceTable.SURGICAL_CODE)){

				approveBtn.setEnabled(false);

			}
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
				if(bean.getPreauthDataExtractionDetails().getIsStarGrpCorApproveBtn()){
					approveBtn.setEnabled(false);
				}
			}
		}
		
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)
				|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE))) {
			
			if(bean.getPreauthDataExtractionDetails().getIsStarGrpCorApproveBtn()){
				approveBtn.setEnabled(false);
			}
			if(bean.getPreauthDataExtractionDetails().getTreatmentType() != null && bean.getPreauthDataExtractionDetails().getTreatmentType().getId().equals(ReferenceTable.SURGICAL_CODE)){

				approveBtn.setEnabled(false);

			}
		}
		
			
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
				
				Boolean isStarCancerVaildAprve = getDisableAprBtnAndAlert();
				if(isStarCancerVaildAprve){
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				
				if(bean.getModeOfReceipt() != null 
						//Below condition added for additional changes in Physical Doc
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) 
						&& (bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)
						|| bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_EMAIL))){
					BPMClientContext clientContext = new BPMClientContext();
					String documentGateWayUrl = clientContext.getDocumentGateWayUrl();
					if(documentGateWayUrl.equalsIgnoreCase("F")){
						if(!isDocChecked && bean.getCheckerVerified() == null){
							wizard.getNextButton().setEnabled(false);
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createInformationBox("Physical Document Not Verified for this ROD" + "</b>", buttonsNamewithType);
							Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
									.toString());
							homeButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {
									isDocChecked = true;
									wizard.getNextButton().setEnabled(true);
									Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());

									Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
									fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_APPROVE_BSI_ALERT,null);
									
//									setBalanceSumInsuredAlert(bean);
									if(!isStopProcess){
										if(bean.isInsuredDeleted()){
											alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
										}
										SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
										if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
												&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
												&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
												&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
													showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
										}else {
											if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
												if(bean.getIsHospitalizationRODApproved()) {
														fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
												} else {
													showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
												}
											} else {
												showErrorPageForCancelledPolicy();
											}

										}
									}		
									if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
										showBonusView("Information - "+bean.getSiAlertDesc());
									}
									showRestrictedSIAlert();
									
									//changes done for GMC SI restrict CR
									if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
										if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
												ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
											showAlertPopForGMCParentSIRestrict();
										}
									}
								}
							});
							
						} else {
							Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());

							Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
							fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_APPROVE_BSI_ALERT,null);
//							setBalanceSumInsuredAlert(bean);
							if(!isStopProcess){
								if(bean.isInsuredDeleted()){
									alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
								}
								SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
								if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
										&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
										&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
											showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
								}else {
									if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
										if(bean.getIsHospitalizationRODApproved()) {
												fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
										} else {
											showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
										}
									} else {
										showErrorPageForCancelledPolicy();
									}

								}
							}		
							if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
								showBonusView("Information - "+bean.getSiAlertDesc());
							}
							showRestrictedSIAlert();
						}
						
					}
	
				} else if(bean.getModeOfReceipt() != null
						//Below condition added for additional changes in Physical Doc
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null 
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null 
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) 
						&& (bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)
						|| bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_EMAIL))){
					BPMClientContext clientContext = new BPMClientContext();
					String documentGateWayUrl = clientContext.getDocumentGateWayUrl();
					if(documentGateWayUrl.equalsIgnoreCase("F")){
						if(bean.getCheckerVerified() == null){
							wizard.getNextButton().setEnabled(false);
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createInformationBox("Physical Document Not Verified for this ROD" + "</b>", buttonsNamewithType);
							Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
									.toString());
							homeButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {
									wizard.getNextButton().setEnabled(false);
								}
							});
							
						} else {

							Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());

							Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
							fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_APPROVE_BSI_ALERT,null);
//							setBalanceSumInsuredAlert(bean);
							if(!isStopProcess){
								if(bean.isInsuredDeleted()){
									alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
								}
								SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
								if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
										&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
										&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
										&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
											showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
								}else {
									if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
										if(bean.getIsHospitalizationRODApproved()) {
												fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
										} else {
											showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
										}
									} else {
										showErrorPageForCancelledPolicy();
									}

								}
							}		
							if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
								showBonusView("Information - "+bean.getSiAlertDesc());
							}
							showRestrictedSIAlert();

							//changes done for GMC SI restrict CR
							if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
										ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
									showAlertPopForGMCParentSIRestrict();
								}
							}
						}
						
					}
				} else {


					Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());

					Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
					fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_APPROVE_BSI_ALERT,null);
//					setBalanceSumInsuredAlert(bean);
					if(!isStopProcess){
						
						if(bean.isInsuredDeleted()){
							alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}
						SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
						if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
								&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
								&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
									showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
						}else {
							if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
								if(bean.getIsHospitalizationRODApproved()) {
										fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
								} else {
									showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
								}
							} else {
								showErrorPageForCancelledPolicy();
							}

						}
					}		
					if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						showBonusView("Information - "+bean.getSiAlertDesc());
					}
					showRestrictedSIAlert();
					
					//changes done for GMC SI restrict CR
					if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
								ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
							showAlertPopForGMCParentSIRestrict();
						}
					}
				
				
				}
				
				/*Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());

				Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
				fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_APPROVE_BSI_ALERT,null);
//				setBalanceSumInsuredAlert(bean);
				if(!isStopProcess){
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}
					SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
					if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
							&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
							&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
								showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
					}else {
						if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
							if(bean.getIsHospitalizationRODApproved()) {
									fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
							} else {
								showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
							}
						} else {
							showErrorPageForCancelledPolicy();
						}

					}
				}		
				if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					showBonusView("Information - "+bean.getSiAlertDesc());
				}
				showRestrictedSIAlert();*/
				
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)) {
					SHAUtils.showAlertMessageBox(SHAConstants.RAKSHAK_PRODUCT_ALERT_MESSAGE);
				}
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
					fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_CANCEL_ROD, bean);
			}
		});
		
		btnBillAssessmentSheet = new Button("FA- Billing Assessment Sheet");
		btnBillAssessmentSheet.setHeight("-1px");
		//Vaadin8-setImmediate() btnBillAssessmentSheet.setImmediate(true);
		
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE,financialDecisionCommunicationPageObj);
		
		BrowserWindowOpener opener = new BrowserWindowOpener(ViewBillAssessment.class);
		opener.setFeatures("height=800,width=1300,resizable");
	    opener.extend(btnBillAssessmentSheet);

		btnBillAssessmentSheet.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE,financialDecisionCommunicationPageObj);
				financialHospitalizationPageUI.setOtherBenefitValuesForBilling();
			}
		});
//		btnBillAssessmentSheet.addClickListener(new Button.ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				showBillAssessmentSheetInPopUp();
//			}
//		});
		
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_QUERY_EVENT,null);
				
				//changes done for GMC SI restrict CR
				if (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null && 
							ReferenceTable.getGMCRelationShipKey().containsKey(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
						showAlertPopForGMCParentSIRestrict();
					}
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

				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					fireViewEvent(FinancialWizardPresenter.DEFINED_LIMIT_ALERT,null);
				}
				else{						
					fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_REJECT_EVENT, bean);					
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_REFERCOORDINATOR_EVENT,null);
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				
				if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated() != null && 
						bean.getPreauthMedicalDecisionDetails().getIsFvrIntiated().equals(Boolean.FALSE)){
					
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_FVR_EVENT,bean);
				}
				else
				{
					showErrorPopup("FVR request is in process cannot initiate another request");
				}
			}
		});
		
		initiateInvestigationBtn = new Button("Refer for Investigation");		
		initiateInvestigationBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateInvestigationBtn.setImmediate(true);
		initiateInvestigationBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_INVESTIGATION_EVENT,bean);
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_REFER_TO_SPECIALIST_EVENT,null);
			}
		});
		
		referToMedicalApproverBtn = new Button("Refer to Medical Approver");		
		referToMedicalApproverBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToMedicalApproverBtn.setImmediate(true);
		referToMedicalApproverBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT, null);
			}
		});
	
		
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
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
					SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
				}
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_REFER_TO_BILLING_EVENT, null);
			}
		});
		
		 referToBillEntry = new Button("Refer To Bill Entry");
		 referToBillEntry.setHeight("-1px");
		 //Vaadin8-setImmediate() referToBillEntry.setImmediate(true);
		 referToBillEntry.addClickListener(new Button.ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 7255298985095729669L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							(bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
									|| bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
						SHAUtils.showMessageBoxWithCaption(SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG, "INFORMATION");
					}
					fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_HOSPITALIZATION_REFER_TO_BILL_ENTRY, null);
				}
			});
		 
		 holdBtn = new Button("Hold");
			holdBtn.setHeight("-1px");
			//Vaadin8-setImmediate() holdBtn.setImmediate(true);
			holdBtn.addClickListener(new Button.ClickListener() {

				/**
				 * 
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					bean.getPreauthMedicalDecisionDetails().setUserClickAction("Hold");
					fireViewEvent(FinancialHospitalizationPagePresenter.BILLING_HOLD_EVENT,null);
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
	
	public void addListenerForVerifiedBonus(){
		
		verifiedBonus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -486851813151643902L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(event.getProperty() != null && event.getProperty().getValue() != null 
						&& event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
					bean.getPreauthMedicalDecisionDetails().setVerifiedBonus(Boolean.TRUE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setVerifiedBonus(Boolean.FALSE);
				}
			}
		});

	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setCancelPolicyProcess(PreauthDTO bean){
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
//		dynamicFrmLayout.addComponent(sentToCPU);
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
				binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
				binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
				dialog.close();
			}
		});
		return cancelButton;
	}
	
	public void generateCancelRODLayout(Object dropdownValues){
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		reasonForCancelRODCmb= (ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
		cancelRODRemarks = (TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
		cancelRODRemarks.setMaxLength(4000);
		cancelRODRemarks.setWidth("400px");
		cancelRODRemarks.setHeight("200px");
		dynamicFrmLayout.addComponent(cancelRODRemarks);
		alignFormComponents();
		reasonForCancelRODCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getCancellationReason() != null){

			reasonForCancelRODCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getCancellationReason());
		}
		
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cancelRODRemarks);
		mandatoryFields.add(reasonForCancelRODCmb);
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(reasonForCancelRODCmb,cancelRODRemarks), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
		
	}
	public void generateFieldsForApproval() {
				initBinder();
				unbindAndRemoveComponents(dynamicFrmLayout);
			approvalRemarksTxta= (TextArea) binder.buildAndBind("Approval Remarks", "financialRemarks", TextArea.class);
			approvalRemarksTxta.setMaxLength(4000);
			approvalRemarksTxta.setWidth("400px");
			approvalRemarksTxta.setHeight("200px");
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
			
			HorizontalLayout btnLayout = new HorizontalLayout(submitBtnForApprovalRemarksWithListener, getFAApproveCancelButton(dialog));
			btnLayout.setWidth("800px");
			btnLayout.setMargin(true);
			btnLayout.setSpacing(true);
			btnLayout.setComponentAlignment(submitBtnForApprovalRemarksWithListener, Alignment.MIDDLE_CENTER);
			showOrHideValidation(false);
			
			verifiedBonus = (OptionGroup) binder.buildAndBind("Verified Bonus", "verifiedBonus",OptionGroup.class);
			verifiedBonus.addItems(getReadioButtonOptions());
			verifiedBonus.setItemCaption(true, "Yes");
			verifiedBonus.setItemCaption(false, "No");
			verifiedBonus.setStyleName("horizontal");
			verifiedBonus.setVisible(false);
			addListenerForVerifiedBonus();
			if (null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				verifiedBonus.setVisible(true);
			}

			vertLayout = new VerticalLayout(originalBillsReceivedHorizontalLayout,new FormLayout(verifiedBonus,approvalRemarksTxta), btnLayout);
			vertLayout.setWidth("800px");
			vertLayout.setMargin(true);
			
			FormLayout amtClaimedLayout = new FormLayout();
			
			if(bean.getDocumentReceivedFromId() != null && bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				amountClaimedFrmHospTxt = (TextField) binder.buildAndBind("Enter the amount claimed from hospital","amountClaimedFrmHosp",TextField.class);
				amountClaimedFrmHospTxt.setMaxLength(10);
				mandatoryFields.add(amountClaimedFrmHospTxt);
				
				hospitalDiscountLbl = new Label("<p><b> Hospital Discount has been checked <span style='color:red;'>*</span></b></p>", ContentMode.HTML);
				hospitalDiscountChk = (CheckBox) binder.buildAndBind("", "hospitalDiscount", CheckBox.class);
				mandatoryFields.add(hospitalDiscountChk);	
				
				HorizontalLayout hospitalDiscountHorizontalLayout = new HorizontalLayout(hospitalDiscountLbl, hospitalDiscountChk);
				hospitalDiscountHorizontalLayout.setWidth("550px");
				hospitalDiscountHorizontalLayout.setComponentAlignment(hospitalDiscountChk, Alignment.MIDDLE_LEFT);
				showOrHideValidation(false);
				
				final ConfirmDialog dialog1 = new ConfirmDialog();
				submitBtnForhospitalDiscountWithListener = getFAApproveSubmitButtonWithListener(dialog1);
				submitBtnForhospitalDiscountWithListener.setEnabled(hospitalDiscountChk.getValue());
				
				CSValidator amountClaimedFrmHospValidator = new CSValidator();
				amountClaimedFrmHospValidator.extend(amountClaimedFrmHospTxt);
				amountClaimedFrmHospValidator.setRegExp("^[0-9]*$");
				amountClaimedFrmHospValidator.setPreventInvalidTyping(true);
				vertLayout.addComponent(hospitalDiscountHorizontalLayout,1);
				
				if (null != bean.getPreauthMedicalDecisionDetails() && bean.getPreauthMedicalDecisionDetails().getOriginalBillsReceived()) {
					amtClaimedLayout = new FormLayout(amountClaimedFrmHospTxt);
					vertLayout.addComponent(amtClaimedLayout, 1);
				}
				
				hospitalDiscountChk.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(hospitalDiscountChk.getValue().equals(true)){
							bean.setHospitalDiscountFlag("Y");
						}
						else
						{
							bean.setHospitalDiscountFlag("N");
						}
					}
				});
				showOrHideValidation(false);
			}
			originalBillsReceivedChk.setData(amtClaimedLayout);
			
			
			originalBillsReceivedChk.addValueChangeListener(new Property.ValueChangeListener() {
		
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(bean.getDocumentReceivedFromId() != null && bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						FormLayout amtClaimedLayout1 = (FormLayout)originalBillsReceivedChk.getData();
						amtClaimedLayout1.addComponent(amountClaimedFrmHospTxt);
						if (null != event && null != event.getProperty() && null != event.getProperty().getValue() && event.getProperty().getValue().toString().equalsIgnoreCase("true")) {
								vertLayout.addComponent(amtClaimedLayout1, 1);
						}
						else {
							vertLayout.removeComponent(amtClaimedLayout1);
						}
						showOrHideValidation(false);
					}
				}
			});
			
			showInPopup(vertLayout, dialog);
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
		if(initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}
	
	public void buildQueryLayout()
	{
		final Integer setQueryValues = setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		if(setQueryValues > 0) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, setQueryValues);
		} else {
			generateQueryDetails(setQueryValues);
		}
	}
	
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
				dialog.close();
				generateQueryDetails(count);
			}
		});
		return true;
	}

	private void generateQueryDetails(final Integer setQueryValues) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		intimation.setKey(this.bean.getIntimationKey());
		queryDetailsTableObj.setViewQueryVisibleColumn();
		
		setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		
	
//		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("400px");
		addingSentToCPUFields();
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			relInstalOpt = (OptionGroup) binder.buildAndBind("Installment Related ", "relInstalmentOptFlag",OptionGroup.class);
			relInstalOpt.addItems(getReadioButtonOptions());
			relInstalOpt.setItemCaption(true, "Yes");
			relInstalOpt.setItemCaption(false, "No");
			relInstalOpt.setStyleName("horizontal");
			relInstalOpt.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					Boolean isChecked = false;
					Boolean isChangesneed = true;
					if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
						isChecked = true;
					}
				}
			});
			
			dynamicFrmLayout.addComponent(relInstalOpt);
		}
		//dynamicFrmLayout = new FormLayout(txtQueryCount,queryRemarksTxta);
		dynamicFrmLayout.addComponent(txtQueryCount);
		dynamicFrmLayout.addComponent(queryRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		//dynamicFrmLayout.addComponent(txtQueryCount,queryRemarksTxta);
		
		/*VerticalLayout verticalQueryLayout  = new VerticalLayout();
		verticalQueryLayout.setHeight("100%");
		verticalQueryLayout.setWidth("100%");
		verticalQueryLayout.addComponent(preAuthPreviousQueryDetailsTable);
		verticalQueryLayout.addComponent(dynamicFrmLayout);*/
		
		alignFormComponents();
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		
		
		/*dynamicTblLayout.setHeight("100%");
		dynamicTblLayout.setWidth("100%");
		dynamicTblLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);*/
		VerticalLayout vTblLayout = new VerticalLayout(queryDetailsTableObj,viewAllDocsLayout,dynamicFrmLayout);
		vTblLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		//wholeVlayout.addComponent(new VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout));
		wholeVlayout.addComponent(vTblLayout);
		//wholeVlayout.addComponent(new VertdynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(queryRemarksTxta);
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			mandatoryFields.add(relInstalOpt);
		}
		showOrHideValidation(false);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener , getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(vTblLayout, btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
	}
	
	public void buildRejectLayout(Object rejectionCategoryDropdownValues,Boolean isDefinedLimitReject)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		this.bean.setStageKey(ReferenceTable.FINANCIAL_STAGE);
		this.bean.setStatusKey(ReferenceTable.FINANCIAL_REJECT_STATUS);
		
		cmbRejectonCategory = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		
		rejectionSubCategoryCmb = (ComboBox) binder.buildAndBind("Rejection sub category","rejSubCategory",ComboBox.class);
		rejectionSubCategoryCmb.setWidth("240px");
		
		txtDefinedLimit = (TextField) binder.buildAndBind("Amount to be considered for defined limit","definedLimit",TextField.class);
		//txtDefinedLimit.setVisible(false);
		
		BeanItemContainer<SelectValue> dropDownValues = (BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues;
		
		BeanItemContainer<SelectValue> filterValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			List<SelectValue> itemIds = dropDownValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")){
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
		
		cmbRejectonCategory.setContainerDataSource(filterValues);
		cmbRejectonCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRejectonCategory.setItemCaptionPropertyId("value");
		
		cmbRejectonCategory.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;

				if(value != null 
						&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
					
					rejectionSubCategoryCmb.setVisible(true);
					fireViewEvent(FinancialHospitalizationPagePresenter.REJECT_SUB_CATEG_LAYOUT_FA_MEDICAL_DECISION, value.getId());
				}
			}
		});
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			 for(int ind = 0; ind < filterValues.getItemIds().size(); ind++){
				if(filterValues.getIdByIndex(ind).getId().equals(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())){
					cmbRejectonCategory.setValue(filterValues.getIdByIndex(ind));
				 }
			 }
		}
		
		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);
		rejectionRemarksTxta.setWidth("400px");
		rejectionRemarksTxta.setHeight("200px");
		dynamicFrmLayout.addComponents(rejectionRemarksTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbRejectonCategory);
		
		Long rejCategId = bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null ? bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId() : null;
		if(rejCategId != null 
				&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(rejCategId)) {
					
				rejectionSubCategoryCmb.setVisible(true);
				showOrHideValidation(false);
		}
		else {
			rejectionSubCategoryCmb.setVisible(false);
			mandatoryFields.remove(rejectionSubCategoryCmb);
		}
		
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
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		VerticalLayout vLayout = new VerticalLayout();
		if(isDefinedLimitReject)
		{
			vLayout = new VerticalLayout(viewAllDocsLayout,new FormLayout(cmbRejectonCategory,rejectionSubCategoryCmb,txtDefinedLimit,rejectionRemarksTxta), btnLayout);
		}
		else
		{
			vLayout = new VerticalLayout(viewAllDocsLayout,new FormLayout(cmbRejectonCategory,rejectionSubCategoryCmb,rejectionRemarksTxta), btnLayout);
		}
		vLayout.setComponentAlignment(viewAllDocsLayout, Alignment.BOTTOM_RIGHT);

		vLayout.setWidth("800px");
		vLayout.setMargin(true);
		showInPopup(vLayout, dialog);
		
	}
	
	public void buildDenialLayout(Object denialValues)
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
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
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
	
	public void buildEscalateLayout(Object escalateToValues)
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
		
	}

	private void addingSentToCPUFields() {
//		unbindField(sentToCPU);
//		sentToCPU = (OptionGroup) binder.buildAndBind( "Send to CPU", "sentToCPUFlag", OptionGroup.class);
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
//				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
//					isChecked = true;
//				} 
//				
////				fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_CPU_SELECTED, isChecked);
//			}
//		});
	}
	
	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind("Type of Coordinator Request","typeOfCoordinatorRequest",ComboBox.class);
		typeOfCoordinatorRequestCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		reasonForReferringTxta = (TextArea) binder.buildAndBind("Reason For Refering","reasonForRefering",TextArea.class);
		reasonForReferringTxta.setMaxLength(4000);
		reasonForReferringTxta.setWidth("400px");
		reasonForReferringTxta.setHeight("200px");
		if(this.bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest() != null){
			typeOfCoordinatorRequestCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest());
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
	
	public void buildSpecialistLayout(Object specialistValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		specialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistType",ComboBox.class);
		specialistType.setContainerDataSource((BeanItemContainer<SelectValue>)specialistValues);
		specialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		specialistType.setItemCaptionPropertyId("value");
//		uploadFile = (Upload) binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		
		if(this.bean.getPreauthMedicalDecisionDetails().getSpecialistType() != null){
			specialistType.setValue(this.bean.getPreauthMedicalDecisionDetails().getSpecialistType());
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
		
	}
	
	public void buildEscalateReplyLayout()
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
		
	}
	
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
	
	/*private void removePreviousQueryTbl(AbstractComponent component)
	{
		if(null != dynamicTblLayout && 0 != dynamicTblLayout.getComponentCount())
		{
			dynamicTblLayout.removeAllComponents();
			wholeVlayout.removeComponent(dynamicTblLayout);
		}
	}*/
	
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
	
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		/*if(!(null != remarksForCPU && null != remarksForCPU.getValue() && !("").equals( remarksForCPU.getValue())))
		{
			hasError = true;
			errorMessages.add("Please Enter Remarks For CPU.");
			return !hasError;
		}
		
		if(!(null != approvalRemarksTxta && null != approvalRemarksTxta.getValue() && !("").equals( approvalRemarksTxta.getValue())))
		{
			hasError = true;
			errorMessages.add("Please Enter Approval Remarks.");
			return !hasError;
		}*/
		
		if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
				null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){			
			
			/*Integer stoplossAmnt = dbCalculationService.getStopLossProcedure(bean.getNewIntimationDTO().getPolicy().getKey(),
					bean.getClaimDTO().getKey(), bean.getKey()).get(SHAConstants.STOP_LOSS_AVAILABLE);
			
			if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > stoplossAmnt){
				
				hasError = true;
				errorMessages.add("Amount Claimed cannot be greater than Stop Loss Amount </br>");
			}*/
		}
		
		if(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
			if(triggerPtsTable != null){
				hasError = !triggerPtsTableObj.isValid();
				if(hasError){
					errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
					errorMessages.add("FVR Trigger Points size will be Max. of 600.</br>");
				}
				else{
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
					bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
			}
		}
		
		if(bean.isDirectToAssignInv() && draftTriggerPointsTableInstanceObj != null){
			List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
			if(null == list || list.isEmpty()){
				hasError = true;
				errorMessages.add("Please Enter Trigger Points. </br>");
				return !hasError;
			}
		}		
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Please select Any Action to Proceed Further. </br>");
			return !hasError;
		}
		
		if (bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) &&
				null != bean.getSiAlertFlag() && bean.getSiAlertFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus() == null){
				hasError = true;
				errorMessages.add("Verify Bonus Applicability"+"</br>");
			}else if(bean.getPreauthMedicalDecisionDetails().getVerifiedBonus().equals(Boolean.FALSE)){
				hasError = true;
				errorMessages.add("Verified Bonus has to be selected as Yes to procees further"+"</br>");
			}
		}

		SelectValue value = cmbRejectonCategory != null && cmbRejectonCategory.getValue() != null ? (SelectValue) cmbRejectonCategory.getValue() : null;
		if(value != null
				&& ReferenceTable.PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB.equals(value.getId())) {
		
			SelectValue rejSubCategvalue = rejectionSubCategoryCmb.getValue() != null ? (SelectValue) rejectionSubCategoryCmb.getValue() : null;
			if(rejSubCategvalue == null) {
				hasError = true;
				errorMessages.add("Please Select Rejection Subcategory.</br>");
				return !hasError;
			}
			
		}
		
		if (!this.binder.isValid()) {
			 hasError = true;
			 for (Field<?> field : this.binder.getFields()) {
			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
			    	if (errMsg != null) {
			    		errorMessages.add(errMsg.getFormattedHtmlMessage());
			    	}
			  }
		} else {
			try {
				this.binder.commit();
				
				if(bean.isDirectToAssignInv() && draftTriggerPointsTableInstanceObj != null){
					List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
					
					draftTriggerPointsTableInstanceObj.deleteEmptyRows();

					if(list != null && !list.isEmpty()){
						for(DraftTriggerPointsToFocusDetailsTableDto triggerPointsDto: list){
							triggerPointsDto.setSno(list.indexOf(triggerPointsDto)+1);
						}
					}
					
					bean.getPreauthMedicalDecisionDetails().setTriggerPointsList(list);
				}
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
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
				if(isValid()) {
					dialog.close();
					if(! bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
					wizard.removeStep("Confirmation");
					wizard.getNextButton().setEnabled(false);
					wizard.getFinishButton().setEnabled(true);
					}else{
				        wizard.removeStep("Confirmation");
						financialDecisionCommunicationObj = financialDecisionCommunicationViewImpl.get();
						financialDecisionCommunicationObj.init(bean, wizard, financialDecisionCommunicationObj.getBillingReviewPageViewImplObj());
						wizard.addStep(financialDecisionCommunicationObj, "Confirmation");
//						wizard.addStep("Confirmation");
						wizard.getNextButton().setEnabled(true);
						wizard.getFinishButton().setEnabled(false);
					}
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
	
	private Button getFAApproveSubmitButtonWithListener(final ConfirmDialog dialog) {
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringBuffer eMsg = new StringBuffer();
				if(isValid()) {
					dialog.close();
					if(! bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
						wizard.removeStep("Confirmation");
						wizard.getNextButton().setEnabled(false);
						wizard.getFinishButton().setEnabled(true);
					}else{
				        wizard.removeStep("Confirmation");
						financialDecisionCommunicationObj = financialDecisionCommunicationViewImpl.get();
						financialDecisionCommunicationObj.init(bean, wizard,financialDecisionCommunicationObj.getBillingReviewPageViewImplObj());
						wizard.addStep(financialDecisionCommunicationObj, "Confirmation");
//						wizard.addStep("Confirmation");
						wizard.getNextButton().setEnabled(true);
						wizard.getFinishButton().setEnabled(false);
					}
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
	
	public void buildInitiateFieldVisit(Object fieldVisitValues)
	{
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
//		fvrTriggerPoints.setWidth("350px");
//		fvrTriggerPoints.setHeight("200px");
		
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
		
//		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,fvrAssignTo,fvrPriority, fvrTriggerPoints), horizontalLayout);
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority),horizontalLayout);
		horizontalLayout2.setSpacing(true);
//		horizontalLayout2.setHeight("200px");
		
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
		
		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2,triggerPtsTableObj, btnLayout);
//		VLayout.setWidth("850px");
//		VLayout.setMargin(true);
//		VLayout.setSpacing(true);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		showInPopup(VLayout, dialog);
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildReferToMedicalApproverLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		reasonForReferring = (TextField) binder.buildAndBind("Reason for Referring to Medical","reasonForRefering",TextField.class);
		reasonForReferring.setMaxLength(4000);
		medicalApproverRemarks = (TextArea) binder.buildAndBind("Remarks","medicalApproverRemarks",TextArea.class);
		medicalApproverRemarks.setMaxLength(4000);
		medicalApproverRemarks.setWidth("400px");
		medicalApproverRemarks.setHeight("200px");
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
	
	public void buildReferToBilling()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
	
		reasonForReferring  =  (TextField) binder.buildAndBind("Reason for Referring To Billings","reasonForReferringToBilling",TextField.class);
		approvalRemarks = (TextArea) binder.buildAndBind("Financial Approver Remarks","financialApproverRemarks",TextArea.class);
		approvalRemarks.setMaxLength(4000);
		approvalRemarks.setWidth("400px");
		approvalRemarks.setHeight("200px");
		
		
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
//		firstForm.setHeight("200px");
		
		VerticalLayout VLayout = new VerticalLayout(firstForm, btnLayout);
//		VLayout.setWidth("700px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		showInPopup(VLayout, dialog);
		
	}
	
	public void buildInitiateInvestigation(Object fieldVisitValues)
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
		
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(allocationTo);
		
		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason For Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(4000);
		reasonForReferringIV.setWidth("350px");
		reasonForReferringIV.setHeight("100px");
		
		mandatoryFields.add(reasonForReferringIV);
		
		if(bean.isDirectToAssignInv()){

			draftTriggerPointsTableInstanceObj = draftTriggerPointsTableInstance.get();
			draftTriggerPointsTableInstanceObj.init("");
			
		}else{
			
			unbindField(triggerPointsToFocus);
			triggerPointsToFocus = (TextArea) binder.buildAndBind(
					"Trigger Points To Focus", "triggerPointsToFocus",
					TextArea.class);
			triggerPointsToFocus.setMaxLength(4000);
			triggerPointsToFocus.setWidth("350px");
			triggerPointsToFocus.setHeight("100px");
			mandatoryFields.add(triggerPointsToFocus);
		}
		
		
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

		VerticalLayout verLayout = new VerticalLayout(countFvr,
				viewFVRDetails);
		verLayout.setComponentAlignment(countFvr,
				Alignment.MIDDLE_CENTER);
		verLayout.setComponentAlignment(viewFVRDetails,
				Alignment.MIDDLE_CENTER);
		verLayout.setMargin(true);
		verLayout.setSpacing(true);

		HorizontalLayout horizontalLayout2 = new HorizontalLayout();
		
		if(bean.isDirectToAssignInv()){
			
			HorizontalLayout topHLayout = new HorizontalLayout(new FormLayout(allocationTo, reasonForReferringIV),verLayout);
			
			HorizontalLayout hlayout = new HorizontalLayout(draftTriggerPointsTableInstanceObj);
			hlayout.setCaption("Trigger points to focus *");
			hlayout.setMargin(false);
			
			VerticalLayout vLayout = new VerticalLayout(topHLayout,new FormLayout(hlayout));
			vLayout.setSpacing(false);
			horizontalLayout2.addComponents(vLayout);
		}
		else{
			horizontalLayout2.addComponents(new FormLayout(allocationTo, reasonForReferringIV,
					triggerPointsToFocus), verLayout);
		}
		
		
		horizontalLayout2.setMargin(true);
		horizontalLayout2.setSpacing(true);

		alignFormComponents();

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
//		VLayout.setHeight("350px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}
	
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
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(min));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS))) {
							
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
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
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String
								.valueOf(min));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
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
				
				if(null != viewQueryDTO.getStatusId() && !ReferenceTable.PARALLEL_QUERY_CANCELLED.equals(viewQueryDTO.getStatusId())){
					
					count++;
				}
				
			}	
			
			return count;
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
						String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
						/*Below code commented due to security reason
						String url = bpmClientContext.getGalaxyDMSUrl() + bean.getNewIntimationDTO().getIntimationId();*/
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
							//** 
							 //*
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
						
						/*claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
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
	
	public void showBillAssessmentSheetInPopUp(){
		
		popup = new com.vaadin.ui.Window();
		popup.setCaption("View Document Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		financialDecisionCommunicationPageObj.init(bean, wizard,financialDecisionCommunicationObj.getBillingReviewPageViewImplObj());
		Component content = financialDecisionCommunicationPageObj.getContent();
		
		Panel panel = (Panel)content;
//		panel.setHeight("500px");
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

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void disableAllExceptReject(Boolean shouldDisable) {
		if(shouldDisable) {
			referToSpecialistBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			referToMedicalApproverBtn.setEnabled(false);
			initiateInvestigationBtn.setEnabled(false);
			referToBillingBtn.setEnabled(false);
			initiateFieldVisitBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			approveBtn.setEnabled(false);
			cancelROD.setEnabled(false);
			rejectBtn.setEnabled(true);
		} else {
			referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			if(bean.getScreenName() != null 
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName()) ||
					(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()))) {
				referToBillingBtn.setEnabled(false);
			}
			else {
				referToBillingBtn.setEnabled(true);
			}	
			initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(true);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		}
	}
	
	public void disableApprove(Boolean shouldDisable) {
		if(shouldDisable) {
			referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			if(bean.getScreenName() != null 
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName()) ||
					(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()))) {
				referToBillingBtn.setEnabled(false);
			}
			else {
				referToBillingBtn.setEnabled(true);
			}
			initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(false);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		} else {
			referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
			initiateInvestigationBtn.setEnabled(true);
			if(bean.getScreenName() != null 
					&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName()) ||
					(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName()))) {
				referToBillingBtn.setEnabled(false);
			}
			else {
				referToBillingBtn.setEnabled(true);
			}	
			initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(true);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		}
	}
	
	public void buildReferToBillEntryLayout() {
		
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		txtBillEntryBillingRemarks=(TextArea) binder.buildAndBind("Bill Entry Remarks", "referToBillEntryBillingRemarks", TextArea.class);
		txtBillEntryBillingRemarks.setMaxLength(4000);
		txtBillEntryBillingRemarks.setWidth("400px");

		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(txtBillEntryBillingRemarks);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(txtBillEntryBillingRemarks), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
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
	
	public void setBillingProcessPageObject(FinancialHospitalizationPageUI financialHospitalizationPageUI){
		this.financialHospitalizationPageUI = financialHospitalizationPageUI;
	}
 	
 	public void showSuperSurplusAlertList(List<SelectValue> superSurplusAlertList) {
		//batchCpuCountTable.init("Count For Cpu Wise", false, false);
		//batchCpuCountTable.setTableList(tableDTOList);
		Table table = new Table();
		/*table.setHeight("200px");
		table.setWidth("200px");*/
		//table.addContainerProperty("Sr.No.", String.class, null);
		table.addContainerProperty("Intimation No", String.class, null);
		table.addContainerProperty("Amount  Settled",  String.class, null);
		table.setPageLength(10);
		table.setSizeFull();
		table.setHeight("140%");
		int i = 0;
		for (SelectValue selectValue : superSurplusAlertList) {
			table.addItem(new Object[]{selectValue.getValue(),  selectValue.getCommonValue()}, i++);
		}
		
		Label proceedBtn = new Label();
		proceedBtn.setCaption("<b style = 'color: red; font-size: 150%;'>Proceed --</b>");
		proceedBtn.setCaptionAsHtml(true);
		proceedBtn.setSizeFull();
//		proceedBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		Button yesButton = new Button("Yes");
		yesButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button noButton = new Button("No");
		noButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		HorizontalLayout buttonLayout = new HorizontalLayout(proceedBtn, yesButton, noButton);
		buttonLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(table, buttonLayout);
		layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setSizeFull();
		//layout.setStyleName("borderLayout");
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("<b style = 'color: red; size'>Maternity benefit settled  in earlier policies - </b>");
		popup.setCaptionAsHtml(true);
		popup.setWidth("30%");
		popup.setHeight("35%");
		popup.setContent(layout);
		popup.setClosable(false);
		popup.center();
		popup.setResizable(false);
		yesButton.setData(popup);
		noButton.setData(popup);
		
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window box = (Window)event.getButton().getData();
				box.close();
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				if(validatePolicyStatus(bean.getPolicyDto().getPolicyNumber()) || (intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y"))){
					if(bean.getIsHospitalizationRODApproved()) {
						fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_APPROVE_EVENT, bean);
					} else {
						showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
					}
				} else {
					showErrorPageForCancelledPolicy();
				}	
				
			}
		});
		
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727955L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window box = (Window)event.getButton().getData();
				box.close();
//				if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					approveBtn.setEnabled(false);
//				}	
				
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	}
		
	public Boolean alertMessageForPED(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);

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
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();					
			}
		});
		return true;
	}

	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		
		rejectionSubCategoryCmb.setVisible(true);
		rejectionSubCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejSubcategContainer);
		rejectionSubCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionSubCategoryCmb.setItemCaptionPropertyId("value");
		
		mandatoryFields.add(rejectionSubCategoryCmb);
		
		if(bean.getPreauthMedicalDecisionDetails().getRejSubCategory() != null){
			rejectionSubCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejSubCategory());
		}

		showOrHideValidation(false);
	}
	
	public void setBalanceSumInsuredAlert(Long appAmount) 
	{
		NewIntimationDto intimationDTO = bean.getNewIntimationDTO();

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());

		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			bean = dbCalculationService.getBalanceSIForGMCAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), appAmount.doubleValue(), bean);
		}else{
			if(ReferenceTable.getFHORevisedKeys().containsKey(intimationDTO.getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				if(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
						bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID) && bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null && ReferenceTable.CAUSE_OF_INJURY_ACCIDENT_KEY.equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId())){
//					bean = dbCalculationService.getRTABalanceSIAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(), 0l).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				else{
					bean = dbCalculationService.getBalanceSIForReimbursementAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),appAmount.doubleValue(), bean);	
				}

			}
			else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(intimationDTO.getPolicy().getProduct().getKey())){
				String subCover = "";
				if(null != bean.getPreauthDataExtractionDetails() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
					subCover = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
				}
				bean = dbCalculationService.getBalanceSIForReimbursementStarCancerGoldAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),subCover, appAmount.doubleValue(), bean);	
			}
			else{
				bean = dbCalculationService.getBalanceSIForReimbursementAlert(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey(),appAmount.doubleValue(), bean);
			}
		}	
	}
	
	private void showBonusView(String tableCaption){
				
			bonusAlertUI.setbonusAlertDTO(bean.getBonusAlertDTO());
			bonusAlertUI.init(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getNewIntimationDTO());

			VerticalLayout bonusLayout = new VerticalLayout(bonusAlertUI);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Information - "+bean.getSiAlertDesc());
			popup.setWidth("80%");
			popup.setHeight("80%");
			popup.setContent(bonusLayout);
			popup.setClosable(false);
			popup.center();
			popup.setResizable(true);
			bonusAlertUI.setPopupWindow(popup);
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
	
	public void showRestrictedSIAlert(){

		if(bean.getPreauthDataExtractionDetails() !=null
				&& bean.getPreauthDataExtractionDetails().getDiagnosisTableList()!=null
				&& !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			for (DiagnosisDetailsTableDTO detailsTableDTO: bean.getPreauthDataExtractionDetails().getDiagnosisTableList()){
				if(detailsTableDTO.getSumInsuredRestriction() !=null
						&& detailsTableDTO.getSumInsuredRestriction().getValue() !=null){
					SHAUtils.showAlertMessageBox(SHAConstants.RESTRICTED_SIALERT);
					break;
				}
			}
		}
	}
	
	public void disableOnlyApprove(Boolean isEnabled){
		approveBtn.setEnabled(isEnabled);
	}
	
	public void showAlertPopForGMCParentSIRestrict(){

		String message = SHAConstants.GMC_PARENT_RESTRICT_ALERT ;

		final MessageBox showInfo= showInfoMessageBox(message);
		Button homeButton = showInfo.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfo.close();
			}
		});


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
	
	public void buildHoldLayout()

	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);


		holdRemarksTxta = (TextArea) binder.buildAndBind("Hold Remarks","holdRemarks",TextArea.class);

		holdRemarksTxta.setMaxLength(4000);

		holdRemarksTxta.setWidth("400px");		

		holdRemarksTxta.setHeight("200px");
		holdRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		handleTextAreaPopup(holdRemarksTxta,null);
		holdRemarksTxta.setData(bean);

		if(bean.getPreauthMedicalDecisionDetails().getHoldRemarks() != null){
			holdRemarksTxta.setValue(bean.getPreauthMedicalDecisionDetails().getHoldRemarks());
		}

		dynamicFrmLayout.addComponent(holdRemarksTxta);

		alignFormComponents();

		wholeVlayout.addComponents(dynamicFrmLayout,remarksLayout);

		mandatoryFields= new ArrayList<Component>();

		mandatoryFields.add(holdRemarksTxta);

		showOrHideValidation(false);

		wizard.getFinishButton().setEnabled(Boolean.TRUE);

		wizard.getNextButton().setEnabled(Boolean.FALSE);

	}
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForHoldRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForHoldRemarks(searchField, getShortCutListenerForHoldRemarks(searchField));

	}
	
	public  void handleShortcutForHoldRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForHoldRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Hold Remarks",KeyCodes.KEY_F8,null) {

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
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.getPreauthMedicalDecisionDetails().setHoldRemarks(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Hold Remarks";
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
	
	public Boolean validateHoldRemarks(){
		Boolean holdremarksEntered = false;
		if(holdRemarksTxta != null && (holdRemarksTxta.getValue() == null || holdRemarksTxta.isEmpty())){
			holdremarksEntered = true;
	}
		return holdremarksEntered;
	}
	
	public Boolean getDisableAprBtnAndAlert(){
		Boolean isValid = true;
		if (bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND)) {
			
			Integer diffDays = SHAUtils.differenceInMonths(bean.getNewIntimationDTO().getPolicyInceptionDate() != null ? 
					 bean.getNewIntimationDTO().getPolicyInceptionDate() : bean.getNewIntimationDTO().getPolicy().getPolicyFromDate(),bean.getPreauthDataExtractionDetails().getAdmissionDate());
	 		if(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
                   ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()) 
                   && diffDays < 30){
	 			SHAUtils.showErrorMessageBoxWithCaption(SHAConstants.STAR_CANCER_PLATINUM_ADMISSION_ALERT, "ERROR");
				approveBtn.setEnabled(false);
				isValid =false;
	 		}else {
				List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
				if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId() != null
							&& bean.getNewIntimationDTO().getHospitalDto().getHospitalType() != null && !bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)){
					for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
						if(detailsTableDTO != null && detailsTableDTO.getSublimitName() != null 
								&& detailsTableDTO.getSublimitName().getName().equalsIgnoreCase(SHAConstants.HOSPICE_CARE)){
							SHAUtils.showErrorMessageBoxWithCaption(SHAConstants.HOSPICE_CARE_ALERT, "ERROR");
							approveBtn.setEnabled(false);
							isValid =false;
							break;
						}
					}
					}
				}
			}
	 		
			List<DiagnosisDetailsTableDTO> detailsTableDTOs = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(detailsTableDTOs !=null && !detailsTableDTOs.isEmpty()){
				for(DiagnosisDetailsTableDTO detailsTableDTO:detailsTableDTOs){
					if(detailsTableDTO != null
							&& detailsTableDTO.getSublimitName() != null && detailsTableDTO.getSublimitName().getName() != null
							&& detailsTableDTO.getSublimitName().getName().equalsIgnoreCase(SHAConstants.HOSPICE_CARE)){
						Boolean isAvaLimitIds=reimbService.getSettledByPolicyKey(bean.getNewIntimationDTO().getPolicy().getKey());
						Boolean isNotSettledLimitIds=reimbService.getNotSettledRodByPolicyKey(bean.getKey() ,bean.getNewIntimationDTO().getPolicy().getKey());
						if(isAvaLimitIds || isNotSettledLimitIds){
							SHAUtils.showMessageBox("Hospice care sublimit has been processed already. Kindly check the<br>"+"policy clause before approving","Information");
							if(isAvaLimitIds){
							approveBtn.setEnabled(false);
							isValid = false;
							}
							break;
						}
					}
				}
				
			}
			
		}
		return isValid;
	}
}
