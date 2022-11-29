package com.shaic.claim.reimbursement.billing.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Insured;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
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
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentInformationUI extends ViewComponent{

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<DocumentDetailsDTO> binder;
	private OptionGroup optPaymentMode;

	private ComboBox cmbPayeeName;

	private TextField txtReasonForChange;

	private TextField txtPanNo;

	private TextField txtPayableAt;

	private TextField txtAccntNo;

	private TextField txtIfscCode;

	private TextField txtBranch;

	private TextField txtBankName;

	private TextField txtCity;
	
	private TextField txtEmailId;
	
	private Button btnIFCSSearch;
	
	private Button btnPopulatePreviousAccntDetails;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	private VerticalLayout paymentDetailsLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private TextField txtPayModeChangeReason;
	
	private TextField txtLegalHeirName;
	
	private Button btnCitySearch;
	
	private VerticalLayout vLayout ;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@Inject
	private PreviousAccountDetailsTable previousAccountDetailsTable ;
	
	private Window populatePreviousWindowPopup;
	
	private VerticalLayout previousPaymentVerticalLayout;
	
	private HorizontalLayout previousAccntDetailsButtonLayout;
	
	private Button btnOk;
	 
	private Button btCancel;
	
	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	private BeanItemContainer<SelectValue> payeeNameList;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	private SelectValue existingPayeeName;
	private TextField txtPaymentPartyMode;

	public void init() 
	{
		
	}
//	public void init(ReceiptOfDocumentsDTO bean) {
//		this.bean = bean;
//		
//	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		initBinder();
		
		if(this.bean.getDocumentDetails().getPayeeName() != null){
			existingPayeeName = this.bean.getDocumentDetails().getPayeeName();
		}
	
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Information / Request");
		
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
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
		
		btCancel = new Button("CANCEL");
		//Vaadin8-setImmediate() btCancel.setImmediate(true);
		btCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btCancel.setWidth("-1px");
		btCancel.setHeight("-10px");
	//	btnCancel.setDisableOnClick(true);
		//Vaadin8-setImmediate() btCancel.setImmediate(true);
		
		previousAccntDetailsButtonLayout = new HorizontalLayout(btnOk,btCancel);
		
		previousAccountDetailsTable.init("Previous Account Details", false, false);
		previousAccountDetailsTable.setPresenterString(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO);
		previousPaymentVerticalLayout = new VerticalLayout();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);
		
		addListener();
		
		setCompositionRoot(paymentDetailsLayout);
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		btnIFCSSearch = new Button();
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		
		addIFSCCodeListner();
		
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
		}
		
		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-z A-Z 0-9]*$");
		accntNoValidator.setPreventInvalidTyping(true);
		
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(false);
		
		if(null != this.bean.getDocumentDetails().getIfscCode())
		{
			txtIfscCode.setValue(this.bean.getDocumentDetails().getIfscCode());
		}
		
		txtBranch = (TextField) binder.buildAndBind("Branch", "branch", TextField.class);
		txtBranch.setNullRepresentation("");
		txtBranch.setEnabled(false);
		
		if(null != this.bean.getDocumentDetails().getBranch())
		{
			txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
		}
		
		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		txtBankName.setEnabled(false);
		
		if(null != this.bean.getDocumentDetails().getBankName())
		{
			txtBankName.setValue(this.bean.getDocumentDetails().getBankName());
		}
		
		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setEnabled(false);
		
		if(null != this.bean.getDocumentDetails().getCity())
		{
			txtCity.setValue(this.bean.getDocumentDetails().getCity());
		}
		
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			txtAccntNo.setReadOnly(true);
			txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(true);
			txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			txtCity.setEnabled(false);
			
		}else{
			
		}
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				&& this.bean.getDocumentDetails().getDocumentReceivedFromValue() != null && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{	
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
			if(txtIfscCode != null){
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setEnabled(true);
			}
			
		}
		
		
		/*TextField dField1 = new TextField();
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
		FormLayout formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,dField1,txtBranch);
		HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
		VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
		hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);*/
		
		
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		dField.setWidth("30px");
		
		
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		
		FormLayout bankTransferLayout2 = new FormLayout(dField,btnIFCSSearch);
		FormLayout bankTransferLayout3 = new FormLayout(dField1,dField2,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2,bankTransferLayout3);
		hLayout.setSpacing(false);//,bankTransferLayout3);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
//		hLayout.setWidth("80%");
		
		
		if(null != txtAccntNo)
		{
			mandatoryFields.add(txtAccntNo);
			setRequiredAndValidation(txtAccntNo);
		}
		
		if(null != txtIfscCode)
		{
			mandatoryFields.add(txtIfscCode);
			setRequiredAndValidation(txtIfscCode);
		}
		
		return hLayout;
		
	}
	
	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeListener()
	{
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					if(null != txtAccntNo)
					{
						mandatoryFields.remove(txtAccntNo);
						if(null != txtAccntNo.getValue() )
						{
						bean.getDocumentDetails().setAccountNo(txtAccntNo.getValue());
						}
					}
					if(null != txtIfscCode)
					{
						mandatoryFields.remove(txtIfscCode);
						if(null != txtIfscCode.getValue() )
						{
						bean.getDocumentDetails().setIfscCode(txtIfscCode.getValue());
						}
					}
					
					/*if(null != txtPayableAt && (null == txtPayableAt || ("").equalsIgnoreCase(txtPayableAt.getValue()))){
						
						mandatoryFields.add(txtPayableAt);
					}*/
				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}	
				
				if(null != bean.getDocumentDetails().getPaymentModeFlag() && bean.getPreauthDTO() != null && bean.getPreauthDTO().getPaymentModeId() != null && !(bean.getDocumentDetails().getPaymentModeFlag().equals(bean.getPreauthDTO().getPaymentModeId()))){
					
					mandatoryFields.add(txtPayModeChangeReason);
					txtPayModeChangeReason.setValue("");
					showOrHideValidation(false);
				}
				else
				{
					mandatoryFields.remove(txtPayModeChangeReason);
				}
			}

			
		});
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	@SuppressWarnings("unused")
	private List<Field<?>> getListOfPaymentFields() {
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		//fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirName);
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
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
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
		
		//txtEmailId.setRequired(true);
		
		txtPayModeChangeReason = (TextField) binder.buildAndBind("Reason for change (Pay Mode)", "payModeChangeReason" , TextField.class);
		CSValidator payModeChangeValidator = new CSValidator();
		payModeChangeValidator.extend(txtPayModeChangeReason);
		payModeChangeValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		payModeChangeValidator.setPreventInvalidTyping(true);
		txtPayModeChangeReason.setMaxLength(4000);		
		payModeShortcutListener(txtPayModeChangeReason, null);
		paymentModeValuechangeListener();
		
				
		
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
		
		txtLegalHeirName = (TextField) binder.buildAndBind("Legal Heir Name","legalFirstName",TextField.class);
		//txtLegalHeirName.setValue(this.chqLegalHeirName);
		txtLegalHeirName.setEnabled(true);
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
			txtLegalHeirName.setReadOnly(true);
			txtLegalHeirName.setEnabled(false);
			
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
			
			if(null != this.bean.getDocumentDetails().getLegalFirstName())
			{
				txtLegalHeirName.setValue(this.bean.getDocumentDetails().getLegalFirstName());
			
			}
			
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
		
		if(null != this.bean.getDocumentDetails().getPaymentModeFlag() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) 
				&& (ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		
		//if(("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && (null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue()))
		{
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,txtPanNo,txtPayableAt);
			formLayout1.setMargin(false);
			HorizontalLayout btnHLayout = new HorizontalLayout(btnCitySearch);
			VerticalLayout btnLayout = new VerticalLayout(cField1,cField2,cField3,cField4,btnHLayout);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName);
			formLayout2.setMargin(false);
			hLayout= new HorizontalLayout(formLayout1 ,btnLayout, formLayout2);
			hLayout.setMargin(true);
			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		}
		else if(("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()) && 
				//(null != this.bean.getDocumentDetails().getPaymentMode() && this.bean.getDocumentDetails().getPaymentMode())
				(null != this.optPaymentMode.getValue() && (Boolean)this.optPaymentMode.getValue())
				)
		{
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,txtPanNo,txtPayableAt);
			formLayout1.setMargin(false);
			
			HorizontalLayout btnHLayout = new HorizontalLayout(btnCitySearch);
			VerticalLayout btnLayout = new VerticalLayout(cField1,cField2,cField3,cField4,btnHLayout);
			
			 formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName);
			 formLayout2.setMargin(false);
			 hLayout = new HorizontalLayout(formLayout1 ,btnLayout, formLayout2);
			 hLayout.setMargin(true);
			 hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
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
			formLayout1 = new FormLayout(optPaymentMode,txtPayModeChangeReason,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
			formLayout1.setMargin(false);
			formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,txtLegalHeirName,dField1,txtBranch);
			formLayout2.setMargin(false);
			HorizontalLayout btnHLayout = new HorizontalLayout(/*dField5,*/btnIFCSSearch);
			//btnHLayout.setWidth("5%");
			VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
//			btnLayout.setWidth("%");
			hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
			hLayout.setMargin(true);
			hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
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
		
		return hLayout;
		
		
	}
	
	
	private HorizontalLayout buildChequePaymentLayout(Boolean paymentMode)
	{
		btnCitySearch = new Button();
		
		btnCitySearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnCitySearch.setIcon(new ThemeResource("images/search.png"));
		
		addCitySearchListener();
		
		if(cmbPayeeName != null){
			unbindField(cmbPayeeName);
		}
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue())){
			
					 SelectValue payeeName = new SelectValue();
					 BeanItemContainer<SelectValue> payeeNameContainerForCashless = new BeanItemContainer<SelectValue>(SelectValue.class);
					 payeeName.setId(1l);
					 if(null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalPayableAt()){
						 payeeName.setValue(bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalPayableAt());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }else if(null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName()){
						 payeeName.setValue(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName());
						 payeeNameContainerForCashless.addBean(payeeName);
						 cmbPayeeName.setContainerDataSource(payeeNameContainerForCashless);
					 }
					 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					 cmbPayeeName.setItemCaptionPropertyId("value");
					 cmbPayeeName.setValue(payeeName);
					 cmbPayeeName.setEnabled(false);
			
			}else{
		
					BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);
				    if(this.bean.getDocumentDetails().getPayeeName() != null){
					
			
			//		payee.addBean(this.bean.getPreauthDataExtractionDetails().getPayeeName());
					
				    payee = getValuesForNameDropDown();
					 
					cmbPayeeName.setContainerDataSource(payee);
					cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbPayeeName.setItemCaptionPropertyId("value");
					
					if(this.bean.getDocumentDetails().getPayeeName() != null) {
						List<SelectValue> itemIds = payee.getItemIds();
						for (SelectValue selectValue : itemIds) {
							if(selectValue.getValue() != null && this.bean.getDocumentDetails().getPayeeName().getValue() != null && selectValue.getValue().toString().toLowerCase().equalsIgnoreCase(this.bean.getDocumentDetails().getPayeeName().getValue().toString().toLowerCase())) {
								this.bean.getDocumentDetails().getPayeeName().setId(selectValue.getId());
							}
						}
					}
					
					
					cmbPayeeName.setValue(this.bean.getDocumentDetails().getPayeeName());
					cmbPayeeName.setEnabled(false);
				    }
					
				    if(null != payee)
				    {
					    for(int i = 0 ; i<payee.size() ; i++)
					 	{
					    	
					    	if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) || ((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) && 
									("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
					     {
							if(null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName())
							{
							 if(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName().equalsIgnoreCase(payee.getIdByIndex(i).getValue()))
								{
								 	cmbPayeeName.removeItem(payee.getIdByIndex(i));
									//this.cmbPayeeName.setEnabled(false);
								}
							}
						 }
				 	}
				 }
		}
	    
		//cmbPayeeName.setRequired(true);
		
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailId" , TextField.class);
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);
		txtEmailId.setMaxLength(100);
		if(null != this.bean.getDocumentDetails().getEmailId())
		{
			txtEmailId.setValue(this.bean.getDocumentDetails().getEmailId());
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
		if(null != this.bean.getDocumentDetails().getPayModeChangeReason())
		{
			txtPayModeChangeReason.setValue(this.bean.getDocumentDetails().getPayModeChangeReason());
		}
		
		
		txtReasonForChange = (TextField) binder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
		
		CSValidator reasonForChangeValidator = new CSValidator();
		reasonForChangeValidator.extend(txtReasonForChange);
		reasonForChangeValidator.setRegExp("^[a-zA-Z]*$");
		reasonForChangeValidator.setPreventInvalidTyping(true);
		txtReasonForChange.setMaxLength(100);
		
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
		
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(10);
		
		if(null != this.bean.getDocumentDetails().getPanNo())
		{
			txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
		}
		
		txtLegalHeirName = (TextField) binder.buildAndBind("","legalFirstName",TextField.class);
		
		txtLegalHeirName.setNullRepresentation("");
		
		if(txtPayableAt != null){
			txtPayableAt.setReadOnly(false);
		}
		unbindField(txtPayableAt);
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setEnabled(false);
		//txtPayableAt.setMaxLength(50);
		/*CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z]*$");
		payableAtValidator.setPreventInvalidTyping(true);;*/
		//txtPayableAt.setRequired(true);
		if(null != this.bean.getDocumentDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());
			txtPayableAt.setEnabled(false);
			btnCitySearch.setEnabled(false);
		}
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && 
				("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{	
			txtEmailId.setReadOnly(true);
			txtEmailId.setEnabled(false);
			
			txtReasonForChange.setReadOnly(true);
			txtReasonForChange.setEnabled(false);
			
			txtPanNo.setReadOnly(true);
			txtPanNo.setEnabled(false);
			
			txtLegalHeirName.setReadOnly(true);
			txtLegalHeirName.setEnabled(false);
			
			txtPayableAt.setReadOnly(true);
			txtPayableAt.setEnabled(false);
			btnCitySearch.setEnabled(false);
			
		}else{
			cmbPayeeName.setEnabled(true);
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				//txtPayableAt.setEnabled(true);
				btnCitySearch.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
		}
		
//		GridLayout grid = new GridLayout(5,3);
		
		txtLegalHeirName.setCaption(null);
		
				
		HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirName);
		nameLayout.setComponentAlignment(txtLegalHeirName, Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);
		FormLayout formLayout1 = null;
		
		TextField cField1 = new TextField();
		cField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField1.setReadOnly(true);
		
		TextField cField2 = new TextField();
		cField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField2.setReadOnly(true);
		
		TextField cField3 = new TextField();
		cField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField3.setReadOnly(true);
		
		TextField cField4 = new TextField();
		cField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cField4.setReadOnly(true);
		
		FormLayout cityLayout = new FormLayout(cField1,cField2,cField3,cField4,btnCitySearch);
		cityLayout.setWidth("80px");
		
		txtPaymentPartyMode = new TextField();
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() != null 
				&& (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))){
			//				txtPaymentPartyMode = (TextField) binder.buildAndBind("Payment Party Mode", "paymentPartyMode" , TextField.class);
			txtPaymentPartyMode.setCaption("Payment Party Mode");
			txtPaymentPartyMode.setEnabled(true);
			txtPaymentPartyMode.setVisible(true);
			if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPaymentParty() != null && !this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPaymentParty().isEmpty()){
				txtPaymentPartyMode.setReadOnly(false);
				txtPaymentPartyMode.setValue(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPaymentParty());
				txtPaymentPartyMode.setReadOnly(true);
			}
		}else {
			txtPaymentPartyMode.setEnabled(false);
			txtPaymentPartyMode.setVisible(false);
		}
		
		if(null != this.bean.getDocumentDetails().getPaymentModeFlag())
		{
			formLayout1 = new FormLayout(optPaymentMode, txtPayModeChangeReason, txtEmailId,txtPanNo, txtPayableAt);
		}
		else
		{
			formLayout1 = new FormLayout(optPaymentMode, txtPayModeChangeReason, txtEmailId, txtPanNo);
		}
		
		if(! paymentMode){
			
			if(txtPayableAt != null){
				formLayout1.removeComponent(txtPayableAt);
			}
		}
		
		if(paymentMode){
			if(null != txtPayableAt)
			{
				mandatoryFields.add(txtPayableAt);
				setRequiredAndValidation(txtPayableAt);
			}
			
		}else{
			if(null != txtPayableAt)
			{
				unbindField(txtPayableAt);
				mandatoryFields.remove(txtPayableAt);
			}
		}
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtPaymentPartyMode,txtReasonForChange,nameLayout);

		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,cityLayout,  formLayout2);
//		hLayout.setWidth("90%");
		
//		payeenameListner();
		if(! paymentMode){
			
			if(cityLayout != null){
				hLayout.removeComponent(cityLayout);
			}
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
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null == value)
				{	
						txtLegalHeirName.setEnabled(true);
						txtReasonForChange.setEnabled(true);
				}
				else if((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(value.getValue()))
				{
					txtReasonForChange.setEnabled(false);
				}
				else
				{
					txtLegalHeirName.setValue(null);
					txtLegalHeirName.setNullRepresentation("");
					txtLegalHeirName.setEnabled(true);
					txtReasonForChange.setEnabled(true);
				}
			}
				
			
		});
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
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}
	
	private void addListener()
	{
		btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				previousAccountDetailsTable.resetTableDataList();
				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");
				
				//setPreviousAccountDetailsValues();
				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO);
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
	
		btCancel.addClickListener(new ClickListener() {
		
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
	
	private void getPaymentDetailsLayout(){
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		
		unbindField(optPaymentMode);
		
		/*if(null != optPaymentMode && optPaymentMode.getValue() != null && (boolean) optPaymentMode.getValue())
		{
			buildBankTransferLayout();
		}*/
		paymentModeListener();
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		
		
		if(null != this.bean.getDocumentDetails() && null != this.bean.getDocumentDetails().getPaymentModeFlag() &&
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		{
			optPaymentMode.setValue(true);
		}
		else
		{
			optPaymentMode.setValue(false);
		}
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				 && this.bean.getDocumentDetails().getDocumentReceivedFromValue() != null && this.bean.getDocumentDetails().getDocumentReceivedFromValue().equalsIgnoreCase(("Hospital")))
		{
			 optPaymentMode.setReadOnly(true);
			 optPaymentMode.setEnabled(false);
			 if(btnIFCSSearch != null){
				 btnIFCSSearch.setEnabled(false);
			 }
		}else{
			optPaymentMode.setReadOnly(false);
			optPaymentMode.setEnabled(true);
			if(btnIFCSSearch != null){
				btnIFCSSearch.setEnabled(true);
			}
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				//txtPayableAt.setEnabled(true);
				btnCitySearch.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setEnabled(true);
			}
			
		}
		
		/*cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		cmbPayeeName.setEnabled(true);
		
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
				}
				else
				{
					if(optPaymentMode != null){
						optPaymentMode.setValue(false);
					}
				}

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
				}
			}
			
			else
			{
				optPaymentMode.setValue(true);

			}
		}
		else if(null != this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPaymentMode();
			if(val)
			{
				optPaymentMode.setValue(false);
			}
			else 
			{
				optPaymentMode.setValue(true);
			}
			optPaymentMode.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPayableAt()){
		txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());	
		}*/
	}
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		/*Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}*/
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		if(null != cancelBtn)
		{
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					/*if(null != populatePreviousWindowPopup)
						populatePreviousWindowPopup.close();*/
					//fireViewEvent(MenuItemBean.CREATE_ROD, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
		}
	}
	
	
	public boolean validatePage(){
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();	
		
		
		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if (null != txtPayModeChangeReason
				&& ((txtPayModeChangeReason.getValue() == null) || (txtPayModeChangeReason
						.getValue().equals("")))) {
			Reimbursement reimbursement = reimbursementService
					.getReimbursementByKey(this.bean.getDocumentDetails()
							.getRodKey());

			if (null != reimbursement
					&& null != reimbursement.getPaymentModeId()
					&& !(reimbursement.getPaymentModeId().equals(bean
							.getDocumentDetails().getPaymentModeFlag()))) {

				hasError = true;
				eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
			}
		}
		
		if(txtPanNo != null && txtPanNo.getValue() != null  && ! txtPanNo.getValue().equalsIgnoreCase("")){
			String value = txtPanNo.getValue();
			if(value.length() != 10){
				hasError = true;
				eMsg.append("PAN number should be 10 digit value</br>");
			}
		}
		
		/*if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			//if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getDocumentDetails().get))
			if(null != txtReasonForChange && txtReasonForChange.isEnabled() && (null == txtReasonForChange.getValue() || ("").equalsIgnoreCase(txtReasonForChange.getValue())))
					{
								hasError = true;
								eMsg.append("Please enter reason for change , since payee name is changed in payment details</br>");
					}
		}*/
		
		if(null != bean.getDocumentDetails().getDocumentReceivedFromValue() && ! (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			if(cmbPayeeName != null){
				SelectValue selected = (SelectValue)cmbPayeeName.getValue();
				if(existingPayeeName != null && selected != null 
						&& ! existingPayeeName.getValue().equalsIgnoreCase(selected.getValue())){
					if(txtReasonForChange != null && txtReasonForChange.getValue() == null || txtReasonForChange.getValue().isEmpty()){
						hasError = true;
						eMsg.append("Please Enter Reason for changing payee name</br>");
					}
				}
			}
		}
		
		if(null != bean.getDocumentDetails().getPaymentModeFlag() &&
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(bean.getDocumentDetails().getPaymentModeFlag()))){
		
		if(txtPayableAt != null && (null == txtPayableAt.getValue() || ("").equalsIgnoreCase(txtPayableAt.getValue()))){
			
			hasError = true;
			eMsg.append("Please Enter Payable At</br>");
		}
		}
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} 
		else 
			{
				try {
					this.binder.commit();
				} catch (CommitException e) {
					e.printStackTrace();
				}
				showOrHideValidation(false);
				return true;
			}	
	}
	
	
	
	public void setUpPayableDetails(String name){

		txtPayableAt.setReadOnly(false);
		  txtPayableAt.setValue(name);
		  txtPayableAt.setReadOnly(true);	
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
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto){
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
	}
	
	private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
	{
		Policy policy = policyService.getPolicy(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		SelectValue payeeValue = null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
			}
			
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		
		SelectValue hospitalPayableAt = new SelectValue();
		hospitalPayableAt.setId(Long.valueOf(payeeValueList.size()+1));
		hospitalPayableAt.setValue(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getHospitalPayableAt());
		payeeValueList.add(hospitalPayableAt);
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName());
			payeeValueList.add(hospitalName);
		}

		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(!ReferenceTable.getGMCProductList().containsKey(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			
			payeeNameValueContainer.addAll(payeeValueList);
		}
		else
		{
			payeeNameValueContainer = dbCalculationService.getPayeeName(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),
					this.bean.getClaimDTO().getNewIntimationDto().getKey());
		}
		
		payeeNameValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return payeeNameValueContainer;
		
		}
		return null;
	}
		
	public void addCitySearchListener(){
		btnCitySearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				citySearchCriteriaWindow.setPresenterString(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO);
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
	
	public void addIFSCCodeListner()
	{
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO);
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
		
	}
	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO){
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(tableDTO.getEmailId());
			txtEmailId.setEnabled(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(tableDTO.getPanNo());
			txtPanNo.setEnabled(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			txtAccntNo.setEnabled(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(tableDTO.getIfsccode());
			txtIfscCode.setEnabled(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(tableDTO.getBankName());
			txtBankName.setEnabled(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(tableDTO.getBankCity());
			txtCity.setEnabled(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(tableDTO.getBankBranch());
			txtBranch.setEnabled(true);
		}
	}
	
	
	
	
	

	
}




