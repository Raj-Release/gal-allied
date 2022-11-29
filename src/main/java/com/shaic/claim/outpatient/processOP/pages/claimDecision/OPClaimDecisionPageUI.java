package com.shaic.claim.outpatient.processOP.pages.claimDecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.processOP.wizard.ProcessOPClaimWizardPresenter;
import com.shaic.claim.outpatient.registerclaim.dto.DocumentDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.InsuredDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.pages.InsuredDetailsTable;
import com.shaic.claim.outpatient.registerclaim.table.OPBillDetailsListenerTable;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.domain.Insured;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OPClaimDecisionPageUI extends ViewComponent{/*

	private static final long serialVersionUID = -6039649831441636195L;
	
	@Inject
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	private ComboBox cmbClaimType;
	
	private ComboBox cmbInsuredPatientName;
	
	private TextField amountClaimedTxt;
	
	private TextField provisionAmtTxt;
	
	private TextArea rejectionRemarks;
	
	private Button approveBtn;
	
	private Button rejectBtn;
	
	private Button btnIFCSSearch;
	
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@Inject
	private Instance<InsuredDetailsTable> insuredDetailsTable;
	
	private InsuredDetailsTable insuredDetailsTableObj;
	
	@Inject
	private Instance<OPBillDetailsListenerTable> billDetailsListener;
	
	private OPBillDetailsListenerTable billDetailsListenerObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String, Object> referenceData;
	
	private BeanFieldGroup<DocumentDetailsDTO_old> binder;

	private VerticalLayout wholeLayout;

	private VerticalLayout paymentDetailsLayout;

	private OptionGroup optPaymentMode;

	private ComboBox cmbPayeeName;

	private TextField txtEmailId;

	private TextField txtReasonForChange;

	private TextField txtPanNo;

	private TextField txtLegalHeirName;

	private TextField txtPayableAt;

	private TextField txtAccntNo;

	private TextField txtIfscCode;

	private TextField txtBranch;

	private TextField txtBankName;

	private TextField txtCity;

	private TextField eligibleAmtTxt;

	private TextField availableSI;

	private TextField amountPayable;

	private TextArea approvalRemarks;

	private Button submitButton;

	private Button cancelButton;

	private VerticalLayout approveLayout;
	
	private Boolean isButtonClicked = false;
	
	private Boolean isCancelClicked = false;
	
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO_old>(
				DocumentDetailsDTO_old.class);
		this.binder.setItemDataSource(this.bean.getDocumentDetails());
	}
	
	public Component getContent() {
		wizard.getNextButton().setEnabled(false);
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		cmbClaimType = (ComboBox) binder.buildAndBind("Claim Type", "claimType", ComboBox.class);
		cmbClaimType.setNullSelectionAllowed(false);
		
		cmbInsuredPatientName = (ComboBox) binder.buildAndBind("Insured Patient Name", "insuredPatientName", ComboBox.class);
		cmbInsuredPatientName.setNullSelectionAllowed(false);
		
		amountClaimedTxt = (TextField)binder.buildAndBind(
				"Amount Claimed", "amountClaimed", TextField.class);
		amountClaimedTxt.setNullRepresentation("");
		amountClaimedTxt.setMaxLength(25);
		CSValidator validator = new CSValidator();
		
		validator.extend(amountClaimedTxt);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		provisionAmtTxt = (TextField)binder.buildAndBind(
				"Provision Amount", "provisionAmt", TextField.class);
		provisionAmtTxt.setNullRepresentation("");
		provisionAmtTxt.setMaxLength(25);
		CSValidator validator1 = new CSValidator();
		
		validator1.extend(provisionAmtTxt);
		validator1.setRegExp("^[0-9]*$");
		validator1.setPreventInvalidTyping(true);
		
		insuredDetailsTableObj = insuredDetailsTable.get();
		insuredDetailsTableObj.init("Insured Details", true);
		if(bean.getPolicy() != null && bean.getPolicy().getProductType() != null &&  bean.getPolicy().getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY)) {
			insuredDetailsTableObj.setVisibleColumn();
		}
		
		FormLayout formLayout = new FormLayout(provisionAmtTxt);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(amountClaimedTxt), formLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setComponentAlignment(formLayout, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout claimDetailsVLayout = new VerticalLayout(new FormLayout(cmbClaimType, cmbInsuredPatientName) , insuredDetailsTableObj, horizontalLayout);
		claimDetailsVLayout.setCaption("Claim Details");
		
		billDetailsListenerObj = billDetailsListener.get();
		billDetailsListenerObj.init("", bean);
		
		approveBtn = new Button("Approve");
		rejectBtn = new Button("Reject");
		HorizontalLayout btnLayout = new HorizontalLayout(approveBtn, rejectBtn);
		btnLayout.setSpacing(true);
		btnLayout.setWidth("100%");
		btnLayout.setMargin(true);
		btnLayout.setComponentAlignment(approveBtn, Alignment.BOTTOM_RIGHT);
		addListener();
		
		mandatoryFields.add(cmbClaimType);
		mandatoryFields.add(amountClaimedTxt);
		mandatoryFields.add(provisionAmtTxt);
		mandatoryFields.add(cmbInsuredPatientName);
		
		wholeLayout = new VerticalLayout(claimDetailsVLayout, billDetailsListenerObj, btnLayout);
		
		showOrHideValidation(false);
		
		return wholeLayout;
		
	}
	
	private void addListener() {
		approveBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePageBeforeButtonClicked()){
					buildApproveLayout();
				}
				
			}
		});
		
		rejectBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				buildRejectLayout();
			}
		});
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		BeanItemContainer<Insured> insuredList = (BeanItemContainer<Insured>) referenceData
				.get("insuredList");
		
		BeanItemContainer<SelectValue> claimType = (BeanItemContainer<SelectValue>) referenceData
				.get("claimType");
		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get("commonValues");
		BeanItemContainer<SelectValue> modeOfReceipt = (BeanItemContainer<SelectValue>) referenceData
				.get("modeOfReceipt");
		BeanItemContainer<SelectValue> documentReceivedFrom = (BeanItemContainer<SelectValue>) referenceData
				.get("docReceivedFrom");
		
		List<SelectValue> values = new ArrayList<SelectValue>();
		
		List<SelectValue> itemIds = claimType.getItemIds();
		for (SelectValue selectValue : itemIds) {
			if(bean.getOutpatientFlag() && selectValue.getId().equals(ReferenceTable.OUT_PATIENT)) {
				values.add(selectValue);
			}
			
			if(bean.getHealthCheckupFlag() && selectValue.getId().equals(ReferenceTable.HEALTH_CHECK_UP)) {
				values.add(selectValue);
			}
		}
		BeanItemContainer<SelectValue> itemContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		itemContainer.addAll(values);
		
		cmbClaimType.setContainerDataSource(itemContainer);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		if(this.bean.getDocumentDetails().getClaimType() != null) {
			cmbClaimType.setValue(this.bean.getDocumentDetails().getClaimType());
		}
		
		
		
		cmbInsuredPatientName.setContainerDataSource(insuredList);
		cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredPatientName.setItemCaptionPropertyId("insuredName");
		
		if(this.bean.getDocumentDetails().getInsuredPatientName() != null) {
			cmbInsuredPatientName.setValue(this.bean.getDocumentDetails().getInsuredPatientName());
			cmbInsuredPatientName.setEnabled(false);
		}
		
		insuredDetailsTableObj.setReference(referenceData);
		
		
		if(insuredDetailsTableObj !=  null) {
			insuredDetailsTableObj.setTableList(this.bean.getDocumentDetails().getInsuredDetailsList());
		}
		
		billDetailsListenerObj.setReferenceData(referenceData);
		if(billDetailsListenerObj != null) {
			List<OPBillDetailsDTO> billDetailsDTOList = this.bean.getOpBillEntryDetails().getBillDetailsDTOList();
			for (OPBillDetailsDTO opBillDetailsDTO : billDetailsDTOList) {
				billDetailsListenerObj.addBeanToList(opBillDetailsDTO);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();	
		
		if(!isButtonClicked) {
			hasError = true;
			eMsg.append("Please Select Approve or Reject to Proceed further.");
			isCancelClicked = false;
		}

		if(isCancelClicked)
		{
			showOrHideValidation(true);
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
		}

		if(null != this.billDetailsListenerObj)
		{
			Boolean isValid = billDetailsListenerObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.billDetailsListenerObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		List<InsuredDetailsDTO> values = this.insuredDetailsTableObj.getValues();
		if(values.isEmpty()) {
			eMsg.append("Please Enter Atlease one Insured Details to Proceed Further. </br>");
			hasError = true;
		}
		
		for (InsuredDetailsDTO insuredDetailsDTO : values) {
			if(insuredDetailsDTO.getCheckupDate() != null && (!SHAUtils.isDateOfIntimationWithPolicyRange(this.bean.getPolicy().getPolicyFromDate(), this.bean.getPolicy().getPolicyToDate(), insuredDetailsDTO.getCheckupDate()) || !SHAUtils.checkAftStartDate(this.bean.getPolicy().getPolicyFromDate(), insuredDetailsDTO.getCheckupDate()))) {
				eMsg.append("Checkup Date should be between Policy Start and Policy End Date and should be 30 days greater than the policy start date</br>");
				hasError = true;
				break;
			}
		}
		
		if(this.insuredDetailsTableObj != null) {
			if(!this.insuredDetailsTableObj.isValid()) {
				hasError = true;
				List<String> errors = this.insuredDetailsTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				   }
			}
		}
		
		if(this.bean.getStatusKey() != null && txtEmailId != null && txtEmailId.getValue() != null && this.bean.getStatusKey().equals(ReferenceTable.OP_APPROVE) && !SHAUtils.isValidEmail(txtEmailId.getValue())){
			eMsg.append("Please Enter Valid Email.</br>");
			hasError = true;
		}
		 
		char ch = ' ';
		if(null !=txtAccntNo){
		 String str = txtAccntNo.getValue();
		
		if(txtAccntNo !=null && (str.indexOf(ch) == 0)){
			eMsg.append("Please Enter Valid Account No.</br>");
			hasError = true;
		}
		}
		if(txtIfscCode !=null && (txtIfscCode.getValue().indexOf(ch) == 0)){
			eMsg.append("Please Enter Valid Ifsc Code.</br>");
			hasError = true;
		}
		
		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			
			hasError = true;
			showOrHideValidation(false);
			return !hasError;
		} else {
			try {
				this.binder.commit();
				this.bean.getDocumentDetails().setInsuredDetailsList(this.insuredDetailsTableObj.getValues());
				this.billDetailsListenerObj.setBillEntryValues();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
		
		
	}
	
	public boolean validatePageBeforeButtonClicked(){
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();	
		unbindFields();
		
		showOrHideValidation(true);
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

		if(null != this.billDetailsListenerObj)
		{
			Boolean isValid = billDetailsListenerObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.billDetailsListenerObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		List<InsuredDetailsDTO> values = this.insuredDetailsTableObj.getValues();
		if(values.isEmpty()) {
			eMsg.append("Please Enter Atlease one Insured Details to Proceed Further. </br>");
			hasError = true;
		}
		
		for (InsuredDetailsDTO insuredDetailsDTO : values) {
			if(insuredDetailsDTO.getCheckupDate() != null && !SHAUtils.isDateOfIntimationWithPolicyRange(this.bean.getPolicy().getPolicyFromDate(), this.bean.getPolicy().getPolicyToDate(), insuredDetailsDTO.getCheckupDate()) && !SHAUtils.checkAftStartDate(this.bean.getPolicy().getPolicyFromDate(), insuredDetailsDTO.getCheckupDate())) {
				eMsg.append("Checkup Date should be between Policy Start and Policy End Date and should be 30 days greater than the policy start date</br>");
				hasError = true;
				break;
			}
		}
		
		if(this.insuredDetailsTableObj != null) {
			if(!this.insuredDetailsTableObj.isValid()) {
				hasError = true;
				List<String> errors = this.insuredDetailsTableObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				   }
			}
		}
		
		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			
			hasError = true;
			showOrHideValidation(false);
			return !hasError;
		} else {
			try {
				this.binder.commit();
				this.bean.getDocumentDetails().setInsuredDetailsList(this.insuredDetailsTableObj.getValues());
				this.billDetailsListenerObj.setBillEntryValues();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
		
		
	}

	public void buildApproveLayout() {
		isButtonClicked = true;
		unbindFields();
		this.bean.setStageKey(ReferenceTable.OP_STAGE);
		this.bean.setStatusKey(ReferenceTable.OP_APPROVE);
		ConfirmDialog dialog =  new ConfirmDialog();
		dialog.setResizable(true);
		dialog.setWidth("800px");
		
		eligibleAmtTxt = (TextField)binder.buildAndBind("Amount Eligible", "amountEligible", TextField.class);
		eligibleAmtTxt.setEnabled(false);
		eligibleAmtTxt.setNullRepresentation("");
		if(billDetailsListenerObj != null) {
			eligibleAmtTxt.setValue(billDetailsListenerObj.getPayableAmt().toString());
		}
		
		availableSI = (TextField)binder.buildAndBind("Available SI", "balanceSI", TextField.class);
		availableSI.setEnabled(false);
		availableSI.setNullRepresentation("");
		
		amountPayable = (TextField)binder.buildAndBind("Amount Payable", "amountPayable", TextField.class);
		amountPayable.setEnabled(false);
		amountPayable.setNullRepresentation("");
		
		Integer min = Math.min(SHAUtils.getIntegerFromStringWithComma(eligibleAmtTxt.getValue()), SHAUtils.getIntegerFromStringWithComma(availableSI.getValue()) );
		amountPayable.setValue(min.toString());
		
		approvalRemarks = (TextArea)binder.buildAndBind(
				"Approval Remarks", "approvalRemarks", TextArea.class);
		approvalRemarks.setMaxLength(100);
		mandatoryFields.add(approvalRemarks);
		setRequiredAndValidation(approvalRemarks);
		
		HorizontalLayout hLayout = new HorizontalLayout(new FormLayout(eligibleAmtTxt,amountPayable, approvalRemarks) , new FormLayout(availableSI));
		hLayout.setWidth("100%");
		hLayout.setSpacing(true);
		
		approveLayout = new VerticalLayout(hLayout);
		approveLayout.setCaption("Payment Details");
		approveLayout.setSpacing(true);
		approveLayout.setMargin(true);
		submitButton = new Button("Submit");
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);
		
		btnIFCSSearch = new Button();
		getPaymentDetailsLayout();
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton, getCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		approveLayout.addComponent(paymentDetailsLayout);
		approveLayout.addComponent(btnLayout);
		
		showInPopup(approveLayout, dialog, false);
	}

	private void unbindFields() {
		unbindField(approvalRemarks);
		unbindField(rejectionRemarks);
		unbindField(optPaymentMode);
		unbindField(cmbPayeeName);
		unbindField(txtEmailId);
		unbindField(txtPanNo);
		unbindField(txtPayableAt);
		unbindField(txtAccntNo);
		unbindField(txtIfscCode);
		unbindField(txtBranch);
		unbindField(txtBankName);
		unbindField(txtCity);
		unbindField(eligibleAmtTxt);
		unbindField(availableSI);
		unbindField(amountPayable);
	}
	
	public void buildRejectLayout() {
		isButtonClicked = true;
		this.bean.setStageKey(ReferenceTable.OP_STAGE);
		this.bean.setStatusKey(ReferenceTable.OP_REJECT);
		ConfirmDialog dialog =  new ConfirmDialog();
		unbindFields();
		rejectionRemarks = (TextArea)binder.buildAndBind(
				"Rejection Remarks", "rejectionRemarks", TextArea.class);
		mandatoryFields.add(rejectionRemarks);
		setRequiredAndValidation(rejectionRemarks);
		
		VerticalLayout vLayout  = new VerticalLayout(new FormLayout(rejectionRemarks));
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		
		submitButton = new Button("Submit");
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButton, getCancelButton(dialog));
		btnLayout.setWidth("400px");
		btnLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		vLayout.addComponent(btnLayout);
		dialog.setWidth("400px");
		showInPopup(vLayout, dialog, true);
	}
	
	private void showInPopup(Layout layout, final ConfirmDialog dialog, final Boolean isRejection) {
		dialog.setCaption("");
		//dialog.setClosable(true);
		
		Panel panel = new Panel();
		panel.setContent(layout);
		
		dialog.setContent(panel);
		dialog.setResizable(false);
		dialog.setModal(true);
		
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				//isCancelClicked = false;
				if(validatePage()) {
					dialog.close();
					if(validatePage()) {
						if(isRejection) {
							fireViewEvent(ProcessOPClaimWizardPresenter.PROECSS_OP_SUBMITTED_EVENT, bean);
						} else {
							wizard.next();
						}
						
					}
					
				}
			}

		});
		
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
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
		
		if(null != cmbPayeeName)
		{
			mandatoryFields.add(cmbPayeeName);
			setRequiredAndValidation(cmbPayeeName);
			cmbPayeeName.setRequired(true);
			showOrHideValidation(false);
		}
		
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> payeeName = (BeanItemContainer<SelectValue>) referenceData
				.get("payeeNameList");
		SelectValue value  = (SelectValue) referenceData.get("proposerName");
		cmbPayeeName.setContainerDataSource(payeeName);
		cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPayeeName.setItemCaptionPropertyId("value");
		
		if(this.bean.getDocumentDetails().getPayeeName() != null) {
			cmbPayeeName.setValue(this.bean.getDocumentDetails().getPayeeName());
		} else {
			cmbPayeeName.setValue(value);
		}
		
		
		
		if(null == this.bean.getDocumentDetails().getPaymentMode()) {
			optPaymentMode.setValue(true);
		}
		else if(null != this.bean.getDocumentDetails().getPaymentMode())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPaymentMode();
			*//**
			 * The below if block is added to enabling the value change listener
			 * for option group. When the screen is painted for first time, the 
			 * payment mode will be null. When we proceed to next step a value
			 * is assigned in bean. When again traversing back, the same value
			 * is set to the option group, there by ,value change listener is not
			 * invoked. Hence to invoke value change listner, twice the value
			 * is set to optiongroup. This is like, selecting and unselecting the
			 * group. If the value is true, we first set false and again we set to true
			 * and vice versa.
			 * *//*
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
		

		 if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			 
		}
		
		//buildPaymentsLayout();
	}
	
	
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		//fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirName);
		fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);
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
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					if(null != txtPayableAt)
					{
						mandatoryFields.add(txtPayableAt);
						setRequiredAndValidation(txtPayableAt);
						txtPayableAt.setRequired(true);
						showOrHideValidation(false);
					}
					if(null != txtAccntNo)
						{
							mandatoryFields.remove(txtAccntNo);
						}
						if(null != txtIfscCode)
						{
							mandatoryFields.remove(txtIfscCode);
						}
				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					buildBankTransferLayout();
					txtAccntNo.setEnabled(true);
					txtIfscCode.setEnabled(true);
					txtBankName.setEnabled(false);
					txtCity.setEnabled(false);
					btnIFCSSearch.setVisible(true);
					txtBranch.setEnabled(false);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					unbindField(txtPayableAt);
					mandatoryFields.remove(txtPayableAt);
					
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
			}
		});
	}
	
	private HorizontalLayout buildChequePaymentLayout()
	{
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "payeeEmailId" , TextField.class);
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);
		txtEmailId.setMaxLength(100);
		txtEmailId.setEnabled(true);
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(12);
		txtPanNo.setEnabled(true);
		
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setMaxLength(100);
		CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z0-9/]*$");
		payableAtValidator.setPreventInvalidTyping(true);;
		txtPayableAt.setEnabled(true);
		
		TextField field = new TextField();
		field.setCaption("");
		field.setEnabled(false);
		field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		if(null != this.bean.getDocumentDetails().getPayeeEmailId())
		{
			txtEmailId.setValue(this.bean.getDocumentDetails().getPayeeEmailId());
		}
		
		if(null != this.bean.getDocumentDetails().getPanNo())
		{
			txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
		}
		
		if(null != this.bean.getDocumentDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());
		}
		
		
		FormLayout formLayout1 = null;
		FormLayout formLayout2 = null;
		HorizontalLayout hLayout = null;
		
		if(null != this.bean.getDocumentDetails().getPaymentModeFlag() &&  
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		
		//if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			 
			 formLayout2 = new FormLayout(field, cmbPayeeName);
			 hLayout= new HorizontalLayout(formLayout1 ,formLayout2);
		}
		else if(
				//(null != this.bean.getDocumentDetails().getPaymentMode() && this.bean.getDocumentDetails().getPaymentMode())
				(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())
				)
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			 formLayout2 = new FormLayout(field, cmbPayeeName);
			 hLayout = new HorizontalLayout(formLayout1 ,formLayout2);
		}
		else
		{
			TextField dField1 = new TextField();
			dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField1.setReadOnly(true);
			TextField dField2 = new TextField();
			dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField2.setReadOnly(true);
			TextField dField3 = new TextField();
			dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField3.setReadOnly(true);
			TextField dField4 = new TextField();
			dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField4.setReadOnly(true);
			TextField dField5 = new TextField();
			dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			dField5.setReadOnly(true);
			dField5.setWidth(2,Unit.CM);
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
			formLayout2 = new FormLayout(cmbPayeeName,txtBranch);
			HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
			VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
			hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
			
		}
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,btnIFCSSearch ,formLayout2);
		hLayout.setWidth("90%");
	//TODO
//		if(null != txtAccntNo)
//		{
//			mandatoryFields.remove(txtAccntNo);
//		}
//		if(null != txtIfscCode)
//		{
//			mandatoryFields.remove(txtIfscCode);
//		}
		return hLayout;
	}
	
	
	
	private void buildBankTransferLayout()
	{
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		txtAccntNo.setEnabled(true);
		txtAccntNo.setMaxLength(15);
		
		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-zA-Z 0-9 ]*$");
		accntNoValidator.setPreventInvalidTyping(true);
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(true);
		txtIfscCode.setMaxLength(15);
		CSValidator ifscCodeValidator = new CSValidator();
		ifscCodeValidator.extend(txtIfscCode);
		ifscCodeValidator.setRegExp("^[a-zA-Z 0-9 ]*$");
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
		
		
		HorizontalLayout lyutIFCS = new HorizontalLayout(new FormLayout(txtIfscCode),btnIFCSSearch);
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		lyutIFCS.setComponentAlignment(btnIFCSSearch, Alignment.BOTTOM_CENTER);
		lyutIFCS.setSpacing(true);
		lyutIFCS.setWidth("88%");
		
		lyutIFCS.setCaption("IFSC Code");
		
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode);
		bankTransferLayout1.setComponentAlignment(txtAccntNo, Alignment.MIDDLE_RIGHT);
		formLayout1.addComponent(txtAccntNo);
		formLayout1.addComponent(txtIfscCode);
		formLayout1.addComponent(txtBankName);
		formLayout1.addComponent(txtCity);
		formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField,txtBranch, txtBankName,txtCity);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
		hLayout.setWidth("80%");
		hLayout.addStyleName("gridBorder");
		if(null != txtAccntNo && null != txtAccntNo.getData())
		{
			mandatoryFields.add(txtAccntNo);
			setRequiredAndValidation(txtAccntNo);
		}
		if(null != txtIfscCode)
		{
			mandatoryFields.add(txtIfscCode);
			setRequiredAndValidation(txtIfscCode);
		}
		addIFSCCodeListner();
		if(null != txtPayableAt)
		{
		unbindField(txtPayableAt);
		mandatoryFields.remove(txtPayableAt);
			//txtPayableAt.setRequired(false);
		//}
		//setRequired(false);
		
			txtAccntNo.setVisible(false);
			txtIfscCode.setVisible(false);
			txtBankName.setVisible(false);
			txtCity.setVisible(false);
			btnIFCSSearch.setVisible(false);
			txtBranch.setVisible(false);
		
	}
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				isCancelClicked = true;
				dialog.close();
			}
		});
		return cancelButton;
	}
	
	public void addIFSCCodeListner()
	{
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.OP_IFSC);
				viewSearchCriteriaWindow.initView();
				
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
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
			}
		});
		
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
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
		
		
	}

*/}
