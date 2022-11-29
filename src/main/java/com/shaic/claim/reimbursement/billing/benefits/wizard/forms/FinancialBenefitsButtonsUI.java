/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.forms;

/**
 * @author ntv.vijayar
 *
 */

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
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.preauth.wizard.pages.DiagnosisProcedureListenerTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDecisionCommunicationViewImpl;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDecisionPresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
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
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
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
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class FinancialBenefitsButtonsUI  extends ViewComponent {
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
	private Instance<ProcessClaimRequestBenefitsDecisionCommunicationViewImpl> benefitsDecisionCommunicationViewImpl;
	
	private ProcessClaimRequestBenefitsDecisionCommunicationViewImpl benefitsDecisionCommunicationObj;
	
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
		if(bean.getIsDishonoured() || bean.getIsHospitalizationRejected()) {
			//referToSpecialistBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			referToMedicalApproverBtn.setEnabled(false);
			//initiateInvestigationBtn.setEnabled(false);
			referToBillingBtn.setEnabled(false);
			//initiateFieldVisitBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			approveBtn.setEnabled(false);
			cancelROD.setEnabled(false);
			rejectBtn.setEnabled(true);
		} else {
			//referToSpecialistBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
			//initiateInvestigationBtn.setEnabled(true);
			referToBillingBtn.setEnabled(true);
			//initiateFieldVisitBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			approveBtn.setEnabled(true);
			cancelROD.setEnabled(true);
			rejectBtn.setEnabled(true);
		}
		HorizontalLayout buttonFirstLayout = new HorizontalLayout();
		HorizontalLayout buttonSecondLayout = new HorizontalLayout();
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
			bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
          || (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
			bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			referToMedicalApproverBtn.setEnabled(true);
			referToMedicalApproverBtn.setVisible(true);
			buttonFirstLayout = new HorizontalLayout( referCoordinatorBtn, referToMedicalApproverBtn, queryBtn);
			buttonSecondLayout  = new HorizontalLayout( approveBtn, rejectBtn,cancelROD);
		}
		else{
			referToMedicalApproverBtn.setEnabled(false);
			referToMedicalApproverBtn.setVisible(false);
		    buttonFirstLayout = new HorizontalLayout( referCoordinatorBtn, referToBillingBtn, queryBtn);
		    buttonSecondLayout = new HorizontalLayout( approveBtn, rejectBtn,cancelROD);
		  }
		/*
		HorizontalLayout buttonFirstLayout = new HorizontalLayout(referToSpecialistBtn, referCoordinatorBtn, referToMedicalApproverBtn , initiateInvestigationBtn);
		HorizontalLayout buttonSecondLayout = new HorizontalLayout( referToBillingBtn, initiateFieldVisitBtn,  queryBtn, rejectBtn, approveBtn,cancelROD);*/
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
				if(bean.getIsHospitalizationRODApproved()) {
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FIANANCIAL_APPROVE_EVENT, null);
				} else {
					showErrorPopup("Please Approve Hospitalization ROD to Proceed further.");
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
					fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FINANCIAL_CANCEL_ROD, null);
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
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FIANANCIAL_QUERY_EVENT,null);
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
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FIANANCIAL_REJECT_EVENT,null);				
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
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FINANCIAL_REFERCOORDINATOR_EVENT,null);
			}
		});
		
	/*	initiateFieldVisitBtn = new Button("Initiate Field Visit");		
		initiateFieldVisitBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateFieldVisitBtn.setImmediate(true);
		initiateFieldVisitBtn.addClickListener(new Button.ClickListener() {
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_FVR_EVENT,null);
			}
		});
		
		initiateInvestigationBtn = new Button("Refer for Investigation");		
		initiateInvestigationBtn.setHeight("-1px");
		//Vaadin8-setImmediate() initiateInvestigationBtn.setImmediate(true);
		initiateInvestigationBtn.addClickListener(new Button.ClickListener() {
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_INVESTIGATION_EVENT,null);
			}
		});
		
		referToSpecialistBtn = new Button("Refer to Specialist");		
		referToSpecialistBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToSpecialistBtn.setImmediate(true);
		referToSpecialistBtn.addClickListener(new Button.ClickListener() {
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(FinancialHospitalizationPagePresenter.FIANANCIAL_REFER_TO_SPECIALIST_EVENT,null);
			}
		});
		
		*/
		referToMedicalApproverBtn = new Button("Refer to Medical Approver");		
		referToMedicalApproverBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToMedicalApproverBtn.setImmediate(true);
		referToMedicalApproverBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT, null);
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
		unbindAndRemoveComponents(dynamicFrmLayout);
		reasonForCancelRODCmb= (ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
		cancelRODRemarks = (TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
		cancelRODRemarks.setMaxLength(100);
		dynamicFrmLayout.addComponent(cancelRODRemarks);
		alignFormComponents();
		reasonForCancelRODCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		if(null != this.bean.getDocumentDetails() && null != this.bean.getDocumentDetails().getCancellationReason())
		{
			reasonForCancelRODCmb.setValue(this.bean.getDocumentDetails().getCancellationReason());
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
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitBtnForApprovalRemarksWithListener, getFAApproveCancelButton(dialog));
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
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		intimation.setKey(this.bean.getClaimDTO().getNewIntimationDto().getKey());
		queryDetailsTableObj.setViewQueryVisibleColumn();
		
		
//		preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
//		intimation.setKey(this.bean.getIntimationKey());
//		List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.searchForReimbursementQuery(intimation);
//		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO2 : PreAuthPreviousQueryDetailsTableDTO) {
//			try{
//				Date tempDate = SHAUtils.formatTimestamp(preAuthPreviousQueryDetailsTableDTO2.getQueryRaisedDate());
//				preAuthPreviousQueryDetailsTableDTO2.setQueryRaisedDate(SHAUtils.formatDate(tempDate));
//			}catch(Exception e){
//				
//			}
//		}
		
		
		Integer setQueryValues = setQueryValues(this.bean.getDocumentDetails().getRodKey(), this.bean.getClaimDTO().getKey());
//		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		queryRemarksTxta.setMaxLength(200);
		addingSentToCPUFields();
		//dynamicFrmLayout = new FormLayout(txtQueryCount,queryRemarksTxta);
		dynamicFrmLayout.addComponent(txtQueryCount);
		dynamicFrmLayout.addComponent(queryRemarksTxta);
		dynamicFrmLayout.addComponent(sentToCPU);
		//dynamicFrmLayout.addComponent(txtQueryCount,queryRemarksTxta);
		
		/*VerticalLayout verticalQueryLayout  = new VerticalLayout();
		verticalQueryLayout.setHeight("100%");
		verticalQueryLayout.setWidth("100%");
		verticalQueryLayout.addComponent(preAuthPreviousQueryDetailsTable);
		verticalQueryLayout.addComponent(dynamicFrmLayout);*/
		
		alignFormComponents();
		/*dynamicTblLayout.setHeight("100%");
		dynamicTblLayout.setWidth("100%");
		dynamicTblLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);*/
		VerticalLayout vTblLayout = new VerticalLayout(queryDetailsTableObj,dynamicFrmLayout);
		//wholeVlayout.addComponent(new VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout));
		wholeVlayout.addComponent(vTblLayout);
		//wholeVlayout.addComponent(new VertdynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(queryRemarksTxta);
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
		
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Please select Any Action to Proceed Further. </br>");
			return !hasError;
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
					if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
							bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					if(! bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
					wizard.removeStep("Confirmation");
					wizard.getNextButton().setEnabled(false);
					wizard.getFinishButton().setEnabled(true);
					}else{
				        wizard.removeStep("Confirmation");
				        benefitsDecisionCommunicationObj = benefitsDecisionCommunicationViewImpl.get();
				        benefitsDecisionCommunicationObj.init(bean, wizard);
						wizard.addStep(benefitsDecisionCommunicationObj, "Confirmation");
//						wizard.addStep("Confirmation");
						wizard.getNextButton().setEnabled(true);
						wizard.getFinishButton().setEnabled(false);
					}
					}
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
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
					if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
							bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
							|| (bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					wizard.removeStep("Confirmation");
					benefitsDecisionCommunicationObj = benefitsDecisionCommunicationViewImpl.get();
					benefitsDecisionCommunicationObj.init(bean, wizard);
					wizard.addStep(benefitsDecisionCommunicationObj, "Confirmation");
//					wizard.addStep("Confirmation");
					wizard.getNextButton().setEnabled(true);
					wizard.getFinishButton().setEnabled(false);
					}
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
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
	@SuppressWarnings("unchecked")
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
}
