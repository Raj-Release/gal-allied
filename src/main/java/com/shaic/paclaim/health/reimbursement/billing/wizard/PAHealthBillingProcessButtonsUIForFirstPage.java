package com.shaic.paclaim.health.reimbursement.billing.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.domain.Intimation;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp.PAHealthBillingHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.communicationpage.PAHealthFinancialDecisionCommunicationPageUI;
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
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthBillingProcessButtonsUIForFirstPage extends ViewComponent  {
	private static final long serialVersionUID = 857833999341586418L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Intimation intimation;
	
	@Inject
	private PAHealthFinancialDecisionCommunicationPageUI financialDecisionCommunicationPageObj;
	
	
	
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
	
	//Added for refer to bill entry enhancement
	private Button referToBillEntry;
	
	private TextArea txtBillEntryBillingRemarks;
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
		
		HorizontalLayout buttonFirstLayout = new HorizontalLayout(referCoordinatorBtn,referToMedicalApproverBtn, cancelROD,referToBillEntry);
		
		if(null != referToBillEntry)
			referToBillEntry.setEnabled(true);
		
		if(null != bean.getIsRejectReconsidered() && !bean.getIsRejectReconsidered())
		{
			if(null != referToBillEntry)
			{
				referToBillEntry.setEnabled(false);
			}
		}
		/*else if(null == bean.getIsRejectReconsidered())
		{
			referToBillEntry.setEnabled(true);
		}*/
		//HorizontalLayout buttonFirstLayout = new HorizontalLayout(referCoordinatorBtn,referToMedicalApproverBtn, cancelROD);
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
		
//		if(bean.getIsDishonoured()) {
//			referCoordinatorBtn.setEnabled(false);
//			referToMedicalApproverBtn.setEnabled(false);
//		} else {
//			referCoordinatorBtn.setEnabled(true);
//			referToMedicalApproverBtn.setEnabled(true);
//		}
		//IMSSUPPOR-23738
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
				fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_REFERCOORDINATOR_EVENT, null);
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
				fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_REFER_TO_MEDICAL_APPROVER_EVENT, null);
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
				fireViewEvent(PAHealthBillingHospitalizationPagePresenter.BILLING_SEND_TO_FINANCIAL_EVENT, null);
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
				fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_CANCEL_ROD_EVENT, null);
			}
		});
		
		btnBillAssessmentSheet = new Button("Billing Assessment Sheet");		
		btnBillAssessmentSheet.setHeight("-1px");
		//Vaadin8-setImmediate() btnBillAssessmentSheet.setImmediate(true);
		btnBillAssessmentSheet.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				showBillAssessmentSheetInPopUp();
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
					fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_REFER_TO_BILL_ENTRY, null);
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
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				wizard.getFinishButton().setEnabled(false);
				wizard.getNextButton().setEnabled(true);
				bean.setIsFirstPageSubmit(false);
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
				String eMsg = "";
				if(isValid()) {
					dialog.close();
					wizard.getNextButton().setEnabled(false);
					wizard.getFinishButton().setEnabled(true);
					bean.setIsFirstPageSubmit(true);
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg += error+"</br>";
					}
					showErrorPopup(eMsg);
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
		
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(billingRemarks), btnLayout);
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
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(reasonForCancelROD,cancelRODRemarks), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);
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
		financialDecisionCommunicationPageObj.init(bean, wizard);
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

}
