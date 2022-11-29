package com.shaic.claim.reimbursement.billing.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ContributeRRCPopupUI;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPageUI;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationPageUI;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.financialapproval.wizard.ViewBillAssessment;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
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
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class BillingProcessButtonsUI extends ViewComponent {
	
	private static final long serialVersionUID = 857833999341586418L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Intimation intimation;
	
	@Inject
	private DBCalculationService dbCalculationService;
	////private static Window popup;
	
	@Inject
	private FinancialDecisionCommunicationPageUI financialDecisionCommunicationPageObj;
	
	@EJB
	private ReimbursementService reimbService;
	
	
	
	Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private Button referCoordinatorBtn;
	private Button referToMedicalApproverBtn;
	private Button sendToFinancialApprovalBtn;
	private Button cancelROD;
	private Button btnBillAssessmentSheet;
	
	private TextArea medicalApproverRemarks;
	private TextArea billingRemarks;
	private TextArea cancelRODRemarks;
	private ComboBox reasonForCancelROD;
	
	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	private ComboBox rejectionCategoryCmb;
	private TextArea rejectionRemarksTxta;
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

	private Button submitButton;
	
	private Button cancelButton;
	
	private Button referToBillEntry;
	
	private TextArea txtBillEntryBillingRemarks;
	
	private BillingHospitalizationPageUI billingProcessPageUI;
	
	@Inject
	private ContributeRRCPopupUI contributeRRCPopupUI;
	
	private Button holdBtn;
	
	private TextArea holdRemarksTxta;
	
	private HorizontalLayout remarksLayout;
	
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
		
		HorizontalLayout buttonFirstLayout = new HorizontalLayout(referCoordinatorBtn, referToMedicalApproverBtn, sendToFinancialApprovalBtn,cancelROD,btnBillAssessmentSheet,referToBillEntry,holdBtn);
		
		if(null != referToBillEntry)
			referToBillEntry.setEnabled(true);
		
		if(null != bean.getIsRejectReconsidered() && !bean.getIsRejectReconsidered())
		{
			if(null != referToBillEntry)
			{
				referToBillEntry.setEnabled(false);
			}
		}
		
		if(bean.getScreenName() != null && SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())) {
			holdBtn.setEnabled(true);
		}else {
			holdBtn.setEnabled(false);
		}
		
		remarksLayout = new HorizontalLayout();
		remarksLayout.setWidth("100%");
		remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);
		
		//HorizontalLayout buttonFirstLayout = new HorizontalLayout(referCoordinatorBtn, referToMedicalApproverBtn, sendToFinancialApprovalBtn,cancelROD,btnBillAssessmentSheet);
		buttonFirstLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(buttonFirstLayout);
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
		
		if(bean.getIsDishonoured()) {
			referCoordinatorBtn.setEnabled(false);
			referToMedicalApproverBtn.setEnabled(false);
		} else {
			referCoordinatorBtn.setEnabled(true);
			referToMedicalApproverBtn.setEnabled(true);
		}
		
		/*if(this.bean.getIsQueryReceived() || this.bean.getIsReconsiderationRequest()){
			cancelROD.setEnabled(false);
		}*/
		
		setCompositionRoot(wholeVlayout);
	}
	
	
	private void addListener() {
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
				fireViewEvent(BillingHospitalizationPagePresenter.BILLING_REFERCOORDINATOR_EVENT, null);
			}
		});
		
		referToMedicalApproverBtn = new Button("Refer to Medical Approver");		
		referToMedicalApproverBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referToMedicalApproverBtn.setImmediate(true);
		referToMedicalApproverBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
					fireViewEvent(BillingHospitalizationPagePresenter.BILLING_REFER_TO_MEDICAL_APPROVER_EVENT, null);
			}
		});
		
		
		sendToFinancialApprovalBtn = new Button("Send to Financial Approval");		
		sendToFinancialApprovalBtn.setHeight("-1px");
		//Vaadin8-setImmediate() sendToFinancialApprovalBtn.setImmediate(true);
		sendToFinancialApprovalBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
				if (bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND)) {
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
									break;
								}
							}
						}
					}
					
				}

				if(bean.isInsuredDeleted()){
					alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}
				
				if(!isStopProcess){					
					SectionDetailsTableDTO sectionDetailsDTO = bean.getPreauthDataExtractionDetails().getSectionDetailsDTO();
					if (ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							&& sectionDetailsDTO != null && sectionDetailsDTO.getCover() != null 
							&& sectionDetailsDTO.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.MATERNITY_COVER_CODE)
							&& null != bean.getSuperSurplusAlertList() && bean.getSuperSurplusAlertList().size() > 0 ) {
								showSuperSurplusAlertList(bean.getSuperSurplusAlertList());
					}else {
						fireViewEvent(BillingHospitalizationPagePresenter.BILLING_SEND_TO_FINANCIAL_EVENT, null);
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
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(BillingHospitalizationPagePresenter.BILLING_CANCEL_ROD_EVENT, bean);
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
					fireViewEvent(BillingHospitalizationPagePresenter.BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY, null);
				}
			});
		 
		 
		btnBillAssessmentSheet = new Button("Billing Assessment Sheet");		
		btnBillAssessmentSheet.setHeight("-1px");
		//Vaadin8-setImmediate() btnBillAssessmentSheet.setImmediate(true);
		
		final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(ViewBillAssessment.class);
		final ShortcutListener sListener = callSListener();
//		if(bean.getBillingDate() != null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE,financialDecisionCommunicationPageObj);
			
			opener.popupBlockerWorkaround(true);
			opener.withShortcut(sListener);
			opener.setFeatures("height=900,width=1300,resizable");
		    opener.doExtend(btnBillAssessmentSheet);
//		}	    

		btnBillAssessmentSheet.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;
			@Override
			public void buttonClick(ClickEvent event) {
				billingProcessPageUI.setTableValuesForOtherBenefits();
				opener.open();
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
				fireViewEvent(BillingHospitalizationPagePresenter.BILLING_HOLD_EVENT,null);
			}
		});
		
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
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
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
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_SMALL);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		return cancelButton;
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
	
	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind("Type of Coordinator Request","typeOfCoordinatorRequest",ComboBox.class);
		typeOfCoordinatorRequestCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest() != null){

			typeOfCoordinatorRequestCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getTypeOfCoordinatorRequest());
		}

		reasonForReferringTxta = (TextArea) binder.buildAndBind("Reason For Refering","reasonForRefering",TextArea.class);
		reasonForReferringTxta.setMaxLength(4000);
		reasonForReferringTxta.setWidth("400px");
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
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(typeOfCoordinatorRequestCmb, reasonForReferringTxta), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
	}
	
	
	@SuppressWarnings("unchecked")
	public void buildReferToMedicalApproverLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		reasonForReferring = (TextField) binder.buildAndBind("Reason for Referring to Medical","reasonForRefering",TextField.class);
		reasonForReferring.setMaxLength(50);
		medicalApproverRemarks = (TextArea) binder.buildAndBind("Remarks","medicalApproverRemarks",TextArea.class);
		medicalApproverRemarks.setMaxLength(4000);
		medicalApproverRemarks.setWidth("400px");
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
	
	@SuppressWarnings("unchecked")
	public void buildSendToFinancialLayout()
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		billingRemarks = (TextArea) binder.buildAndBind("Billing Remarks","billingRemarks",TextArea.class);
		billingRemarks.setMaxLength(4000);
		billingRemarks.setWidth("400px");
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(billingRemarks);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(billingRemarks),btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
		
	}
	
	public void buildCancelRODLayout(Object dropdownValues){

		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		reasonForCancelROD=(ComboBox) binder.buildAndBind("Reason for Cancelling", "cancellationReason", ComboBox.class);
		cancelRODRemarks = (TextArea) binder.buildAndBind("Remarks Cancellation", "cancelRemarks", TextArea.class);
		cancelRODRemarks.setMaxLength(4000);
		cancelRODRemarks.setWidth("400px");
		alignFormComponents();
		reasonForCancelROD.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getCancellationReason() != null){

			reasonForCancelROD.setValue(this.bean.getPreauthMedicalDecisionDetails().getCancellationReason());
		}
		
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cancelRODRemarks);
		mandatoryFields.add(reasonForCancelROD);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(reasonForCancelROD,cancelRODRemarks),btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
	
		showInPopup(VLayout, dialog);
		
	}
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setHeight("300px");
		
//		submitButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = -5934419771562851393L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//			}
//		});
		
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void showErrorPopup(String eMsg) {
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
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Please select Refer to coordinator or Refer to Medical Approver or Send to Financial Approval. </br>");
			return !hasError;
		}
		
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
		
		if(bean !=null && bean.getPreauthHoldStatusKey() !=null 
				&&bean.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)){
			if(holdRemarksTxta !=null && 
					(holdRemarksTxta.getValue() == null || holdRemarksTxta.getValue().isEmpty())){
				hasError = true;
				errorMessages.add("Please Enter the Hold Remarks. </br>");
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
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
	
	public void showBillAssessmentSheetInPopUp(){
		
		
		
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Document Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		financialDecisionCommunicationPageObj.init(bean, wizard, null);
		Component content = financialDecisionCommunicationPageObj.getContent();
		
		Panel panel = (Panel)content;
//		panel.setHeight("100%");
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
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	public void buildReferToBillEntryLayout() {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		txtBillEntryBillingRemarks=(TextArea) binder.buildAndBind("Bill Entry Remarks", "referToBillEntryBillingRemarks", TextArea.class);
		txtBillEntryBillingRemarks.setMaxLength(4000);
		txtBillEntryBillingRemarks.setWidth("400px");
		txtBillEntryBillingRemarks.setValue("");

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
	
	public void setBillingProcessPageObject(BillingHospitalizationPageUI billingProcessPageUI){
		this.billingProcessPageUI = billingProcessPageUI;
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
				fireViewEvent(BillingHospitalizationPagePresenter.BILLING_SEND_TO_FINANCIAL_EVENT, null);	
				
			}
		});
		
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727955L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window box = (Window)event.getButton().getData();
				box.close();
//				if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
				sendToFinancialApprovalBtn.setEnabled(false);
//				}	
				
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	}
	
	public Boolean alertMessageForPED(String message) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setStyleName("borderLayout");
		layout.setSpacing(true);
		layout.setMargin(true);*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();					
			}
		});
		return true;
	}
	
	@SuppressWarnings("serial")
	public ShortcutListener callSListener(){
	ShortcutListener shortcutListener = new ShortcutListener("billAssessmentInBilling", KeyCode.NUM3, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
		    @Override
		    public void handleAction(Object sender, Object target) {
		    	btnBillAssessmentSheet.click();
		    	billingProcessPageUI.setTableValuesForOtherBenefits();
		    }
		};
		getActionManager().addAction(shortcutListener);
		return shortcutListener;
	}
	
	public void buildHoldLayout(){
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		remarksLayout.removeAllComponents();
		wholeVlayout.removeComponent(remarksLayout);

		holdRemarksTxta = (TextArea) binder.buildAndBind("Hold Remarks","holdRemarks",TextArea.class);
		holdRemarksTxta.setMaxLength(4000);
		holdRemarksTxta.setWidth("400px");		
		holdRemarksTxta.setHeight("200px");
		holdRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		SHAUtils.handleTextAreaPopupDetails(holdRemarksTxta,null,getUI(),SHAConstants.HOLD_REMARKS);
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
}
