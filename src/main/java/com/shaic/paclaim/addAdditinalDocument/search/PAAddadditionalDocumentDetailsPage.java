package com.shaic.paclaim.addAdditinalDocument.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.BillEntryRODReconsiderRequestTable;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claims.reibursement.addaditionaldocuments.AcknowledgementReceiptViewImpl;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAAddadditionalDocumentDetailsPage extends ViewComponent{

	
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;

	@Inject
	private RODQueryDetailsTable rodQueryDetails;

	/*@Inject
	private DocumentCheckListTable documentCheckList;*/

	@Inject
	private BillEntryRODReconsiderRequestTable reconsiderRequestDetails;

	
	// private BillEntryCheckListValidationTable documentCheckListValidation;
	//private DocumentCheckListTable documentCheckListValidation;
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	private OptionGroup accidentOrDeath;

	private ComboBox cmbDocumentsReceivedFrom;

	private DateField documentsReceivedDate;

	private ComboBox cmbModeOfReceipt;

	private TextField txtAcknowledgementContactNumber;

	private VerticalLayout documentDetailsPageLayout;

	private OptionGroup optPaymentMode;

	private ComboBox cmbPayeeName;

	private TextField txtReasonForChange;

	private TextField txtPanNo;

	private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;

	private TextArea txtAdditionalRemarks;

	private TextField txtPayableAt;

	private TextField txtAccntNo;

	private TextField txtIfscCode;

	private TextField txtBranch;

	private TextField txtBankName;

	private TextField txtCity;

	@SuppressWarnings("unused")
	private GWizard wizard;

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanItemContainer<SelectValue> modeOfReceipt;

	private BeanItemContainer<SelectValue> docReceivedFromRequest;

	private BeanItemContainer<SelectValue> payeeNameList;

	// private HorizontalLayout insuredLayout;

	@SuppressWarnings("unused")
	private List<DocumentDetailsDTO> docsDetailsList;

	private TextField txtEmailId;
	@SuppressWarnings("unused")
	private DocumentDetailsDTO docDTO;

	@Inject
	private PASelectRODtoAddAdditionalDocumentsTable selectRODtoAddAdditionalDocumentsTable;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	public Boolean isNext = false;
	
	private OptionGroup sourceOfDocument;
	
	private AcknowledgementReceiptViewImpl acknowledgementReceiptViewImpl;

	@PostConstruct
	public void init() {

	}

	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean.getDocumentDetails());
	}

	public Component getContent() {

		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(true);

		selectRODtoAddAdditionalDocumentsTable.init("", false, true, this.bean);

//		selectRODtoAddAdditionalDocumentsTable.init("", false, false);
//		selectRODtoAddAdditionalDocumentsTable.setTableList(this.bean
//				.getSelectRODtoAddAdditionalDocumentsDTO());
//		selectRODtoAddAdditionalDocumentsTable.setRadioButtonr(this.bean
//				.getDocumentDetails().getRodKey());	

		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout();
		documentDetailsLayout.setCaption("Document Details");
		documentDetailsLayout.setWidth("100%");

		HorizontalLayout remarksLayout = new HorizontalLayout(new FormLayout(
				txtAdditionalRemarks));
		// remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);

		List<DocumentCheckListDTO> dtoList = new ArrayList<DocumentCheckListDTO>();
		dtoList.addAll(this.bean.getDocumentDetails().getDocumentCheckList());
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		/**
		 * Presenter string which is passed for ack doc received can be
		 * passed for add additional doc also, since behaviour is same
		 * for both screen.
		 * 
		 * */
		documentCheckListValidation.initPresenter(SHAConstants.ADD_ADDITIONAL_DOCS);
		documentCheckListValidation.init();
		
	/*	documentCheckList.init("", false);
		documentCheckList.setReference(this.referenceData);*/
		
		
		//documentCheckList = documentCheckListObj.get();
		
		this.bean.getDocumentDetails().setDocumentCheckList(dtoList);

		VerticalLayout docCheckListLayout = new VerticalLayout(
				documentCheckListValidation);
		docCheckListLayout.setCaption("Document Checklist");

		documentDetailsPageLayout = new VerticalLayout(
				selectRODtoAddAdditionalDocumentsTable, documentDetailsLayout,
				docCheckListLayout, remarksLayout);

		addListener();
		setTableValues();
		
		if(null != bean.getDocumentDetails().getSourceOfDocument()){
			enableOrDisableFields(bean.getDocumentDetails().getSourceOfDocument());
		}
		
		showOrHideValidation(false);
		return documentDetailsPageLayout;
	}

	@SuppressWarnings("unused")
	private void getDocumentTableDataList() {
		fireViewEvent(
				BillEntryDocumentDetailsPresenter.BILL_ENTRY_SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES,
				null);
	}

	private HorizontalLayout buildDocumentDetailsLayout() {
		
		
		accidentOrDeath = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);	
		accidentOrDeath.setRequired(true);
		accidentOrDeath.addItems(getReadioButtonOptions());
		accidentOrDeath.setItemCaption(true, "Accident");
		accidentOrDeath.setItemCaption(false, "Death");
		accidentOrDeath.setStyleName("horizontal");
		//Vaadin8-setImmediate() accidentOrDeath.setImmediate(true);
		accidentOrDeath.setReadOnly(false);
		
		cmbDocumentsReceivedFrom = binder.buildAndBind(
				"Documents Recieved From", "documentsReceivedFrom",
				ComboBox.class);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);

		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date",
				"documentsReceivedDate", DateField.class);
		documentsReceivedDate.setValue(new Date());
		documentsReceivedDate.setEnabled(true);
		/*if(null != sourceOfDocument && null != sourceOfDocument.getValue() &&
				!(Boolean.TRUE.equals(sourceOfDocument.getValue()))){
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date",
				"documentsReceivedDate", DateField.class);
			documentsReceivedDate.setEnabled(false);
		}
		else
		{
			documentsReceivedDate = binder.buildAndBind("Documents Recieved Date",
					"documentsReceivedDate", DateField.class);
			documentsReceivedDate.setValue(new Date());
			documentsReceivedDate.setEnabled(true);
		}*/

		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt",
				"modeOfReceipt", ComboBox.class);
		//Vaadin8-setImmediate() cmbModeOfReceipt.setImmediate(true);
		cmbModeOfReceipt.setEnabled(true);

		txtAcknowledgementContactNumber = binder.buildAndBind(
				"Acknowledgement Contact Number (Docs Submitted Person)",
				"acknowledgmentContactNumber", TextField.class);
		txtAcknowledgementContactNumber.setRequired(true);
		txtAcknowledgementContactNumber.setEnabled(true);
		txtAcknowledgementContactNumber.setMaxLength(15);
		txtAcknowledgementContactNumber.setNullRepresentation("");
		CSValidator txtAckValidator = new CSValidator();
		txtAckValidator.setRegExp("^[0-9']*$");
		txtAckValidator.setPreventInvalidTyping(true);
		txtAckValidator.extend(txtAcknowledgementContactNumber);

		txtEmailId = binder
				.buildAndBind("Email ID", "emailId", TextField.class);
		txtEmailId.setEnabled(true);
		txtEmailId.setNullRepresentation("");
//		txtEmailId.setRequired(true);

		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks",
				"additionalRemarks", TextArea.class);
		txtAdditionalRemarks.setMaxLength(100);
		
		sourceOfDocument = (OptionGroup) binder.buildAndBind("Source Of Document" , "sourceOfDocument" , OptionGroup.class);
		sourceOfDocumentListener();	
			sourceOfDocument.setRequired(true);
			sourceOfDocument.addItems(getReadioButtonOptions());
			sourceOfDocument.setItemCaption(true, "Insured");
			sourceOfDocument.setItemCaption(false, "Internal");
			sourceOfDocument.setStyleName("horizontal");			
			//Vaadin8-setImmediate() sourceOfDocument.setImmediate(true);

		FormLayout detailsLayout1 = new FormLayout(accidentOrDeath,cmbDocumentsReceivedFrom,
				documentsReceivedDate, cmbModeOfReceipt);
		FormLayout detailsLayout2 = new FormLayout(sourceOfDocument,
				txtAcknowledgementContactNumber, txtEmailId);

		HorizontalLayout docDetailsLayout = new HorizontalLayout(
				detailsLayout1, detailsLayout2);
		docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);

		setRequiredAndValidation(cmbDocumentsReceivedFrom);
		setRequiredAndValidation(documentsReceivedDate);
		setRequiredAndValidation(cmbModeOfReceipt);

		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);

		addListener();

		return docDetailsLayout;
	}

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

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("unused")
	private HorizontalLayout buildChequePaymentLayout() {
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name",
				"payeeName", ComboBox.class);

		cmbPayeeName.setEnabled(false);

		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailId",
				TextField.class);
		if (null != this.bean.getDocumentDetails().getEmailId()) {
			txtEmailId.setValue(this.bean.getDocumentDetails().getEmailId());
		}
		txtEmailId.setEnabled(false);

		txtReasonForChange = (TextField) binder.buildAndBind(
				"Reason For Change(Payee Name)", "reasonForChange",
				TextField.class);
		txtReasonForChange.setEnabled(false);

		txtPanNo = (TextField) binder.buildAndBind("PAN No", "panNo",
				TextField.class);

		if (null != this.bean.getDocumentDetails().getPanNo()) {
			txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
		}

		txtPanNo.setEnabled(false);

		txtLegalHeirFirstName = (TextField) binder.buildAndBind("",
				"legalFirstName", TextField.class);
		txtLegalHeirFirstName.setEnabled(false);
		txtLegalHeirMiddleName = (TextField) binder.buildAndBind("",
				"legalMiddleName", TextField.class);
		txtLegalHeirMiddleName.setEnabled(false);

		txtLegalHeirLastName = (TextField) binder.buildAndBind("",
				"legalLastName", TextField.class);
		txtLegalHeirLastName.setEnabled(false);
		txtPayableAt = (TextField) binder.buildAndBind("Payable at",
				"payableAt", TextField.class);
		if (null != this.bean.getDocumentDetails().getPayableAt()) {
			txtPayableAt
					.setValue(this.bean.getDocumentDetails().getPayableAt());
		}

		txtPayableAt.setEnabled(false);
		if (null != this.bean.getClaimDTO()
				&& (ReferenceTable.CLAIM_TYPE_CASHLESS)
						.equalsIgnoreCase(this.bean.getClaimDTO()
								.getClaimTypeValue())) {
			txtEmailId.setReadOnly(true);

			txtReasonForChange.setReadOnly(true);

			txtPanNo.setReadOnly(true);

			txtLegalHeirFirstName.setReadOnly(true);

			txtLegalHeirMiddleName.setReadOnly(true);

			txtLegalHeirLastName.setReadOnly(true);

			txtPayableAt.setReadOnly(true);

		}

		HorizontalLayout nameLayout = new HorizontalLayout(
				txtLegalHeirFirstName, txtLegalHeirMiddleName,
				txtLegalHeirLastName);
		nameLayout.setComponentAlignment(txtLegalHeirFirstName,
				Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);
		FormLayout formLayout1 = null;

		if (null != this.bean.getDocumentDetails().getPaymentModeFlag()) {
			formLayout1 = new FormLayout(optPaymentMode, txtEmailId, txtPanNo,
					txtPayableAt);
		} else {
			formLayout1 = new FormLayout(optPaymentMode, txtEmailId, txtPanNo);
		}

		FormLayout formLayout2 = new FormLayout(cmbPayeeName,
				txtReasonForChange, nameLayout);

		HorizontalLayout hLayout = new HorizontalLayout(formLayout1,
				formLayout2);
		hLayout.setWidth("80%");

		return hLayout;

	}

	@SuppressWarnings("unused")
	private HorizontalLayout buildBankTransferLayout() {
		txtAccntNo = (TextField) binder.buildAndBind("Account No", "accountNo",
				TextField.class);
		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");

		if (null != this.bean.getDocumentDetails().getAccountNo()) {
			txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
		}

		txtAccntNo.setEnabled(false);

		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode",
				TextField.class);
		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");

		if (null != this.bean.getDocumentDetails().getIfscCode()) {
			txtIfscCode.setValue(this.bean.getDocumentDetails().getAccountNo());
		}
		txtIfscCode.setEnabled(false);

		txtBranch = (TextField) binder.buildAndBind("Branch", "branch",
				TextField.class);
		txtBranch.setNullRepresentation("");

		if (null != this.bean.getDocumentDetails().getBranch()) {
			txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
		}
		txtBranch.setEnabled(false);

		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName",
				TextField.class);
		txtBankName.setNullRepresentation("");

		if (null != this.bean.getDocumentDetails().getBankName()) {
			txtBankName.setValue(this.bean.getDocumentDetails().getBranch());
		}
		txtBankName.setEnabled(false);

		txtCity = (TextField) binder.buildAndBind("City", "city",
				TextField.class);
		txtCity.setNullRepresentation("");

		if (null != this.bean.getDocumentDetails().getCity()) {
			txtCity.setValue(this.bean.getDocumentDetails().getCity());
		}
		txtCity.setEnabled(false);

		if (null != this.bean.getClaimDTO()
				&& (ReferenceTable.CLAIM_TYPE_CASHLESS)
						.equalsIgnoreCase(this.bean.getClaimDTO()
								.getClaimTypeValue())) {
			txtAccntNo.setReadOnly(true);

			txtIfscCode.setReadOnly(true);

			txtBranch.setReadOnly(true);

			txtBankName.setReadOnly(true);

			txtCity.setReadOnly(true);

		}

		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,
				txtIfscCode, txtBankName, txtCity);
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField, txtBranch);

		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1,
				bankTransferLayout2);
		hLayout.setWidth("80%");

		return hLayout;
	}

	@SuppressWarnings("unused")
	private HorizontalLayout buildQueryDetailsLayout() {
		List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean
				.getRodQueryDetailsList();
		rodQueryDetails.init("", false, false);
		if (null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty()) {
			rodQueryDetails.setTableList(rodQueryDetailsList);
		}

		HorizontalLayout queryDetailsLayout = new HorizontalLayout(
				rodQueryDetails);
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		return queryDetailsLayout;

	}

	@SuppressWarnings("unused")
	private HorizontalLayout buildReconsiderRequestLayout() {
		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList = this.bean
				.getReconsiderRodRequestList();
		reconsiderRequestDetails.init("", false, false);
		if (null != reconsiderRODRequestList
				&& !reconsiderRODRequestList.isEmpty()) {
			reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}

		HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(
				reconsiderRequestDetails);
		reconsiderRequestLayout
				.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		return reconsiderRequestLayout;

	}

	private void addListener() {
		documentsReceivedDate
				.addValueChangeListener(new Property.ValueChangeListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Date value = (Date) event.getProperty().getValue();
						if (null != value) {
							if (value.after(new Date())) {
								documentsReceivedDate.setValue(null);
								HorizontalLayout layout = new HorizontalLayout(
										new Label(
												"Document Received Date cannot be greater than current system date."));
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("");
								dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								getUI();
								dialog.show(UI.getCurrent(), null, true);
							} else if (getDifferenceBetweenDates(value) > 7) {
								documentsReceivedDate.setValue(null);
								HorizontalLayout layout = new HorizontalLayout(
										new Label(
												"Document Received Date can 7 days prior to current system date."));
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("");
								dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
							}
						}
					}
				});

		cmbDocumentsReceivedFrom
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty()
								.getValue();
						
						
						if (value == null) {
							txtAcknowledgementContactNumber.setRequired(true);
							txtAcknowledgementContactNumber.setReadOnly(false);
							txtAcknowledgementContactNumber.setEnabled(true);
							//Vaadin8-setImmediate() txtAcknowledgementContactNumber.setImmediate(true);
							txtEmailId.setReadOnly(false);
							txtEmailId.setEnabled(true);
//							txtEmailId.setRequired(true);
							//Vaadin8-setImmediate() txtEmailId.setImmediate(true);
							setRequiredAndValidation(txtAcknowledgementContactNumber);
//							setRequiredAndValidation(txtEmailId);
							mandatoryFields
									.add(txtAcknowledgementContactNumber);
//							mandatoryFields.add(txtEmailId);
						}
						if (null != value) {
							if (("Insured").equalsIgnoreCase(value.getValue())) {
								txtAcknowledgementContactNumber
										.setRequired(true);
								txtAcknowledgementContactNumber
										.setReadOnly(false);
								txtAcknowledgementContactNumber
										.setEnabled(true);

								//Vaadin8-setImmediate() txtAcknowledgementContactNumber.setImmediate(true);

//								txtAcknowledgementContactNumber
//										.setImmediate(true);

								txtEmailId.setReadOnly(false);
								txtEmailId.setEnabled(true);
//								txtEmailId.setRequired(true);
								//Vaadin8-setImmediate() txtEmailId.setImmediate(true);
								setRequiredAndValidation(txtAcknowledgementContactNumber);
//								setRequiredAndValidation(txtEmailId);
								mandatoryFields
										.add(txtAcknowledgementContactNumber);
//								mandatoryFields.add(txtEmailId);
							} else {
								txtAcknowledgementContactNumber
										.setReadOnly(false);
								txtAcknowledgementContactNumber.setValue(null);
								txtAcknowledgementContactNumber
										.setReadOnly(true);
								txtAcknowledgementContactNumber
										.setEnabled(false);
								txtAcknowledgementContactNumber
										.setRequired(false);

								txtEmailId.setReadOnly(false);
								txtEmailId.setValue(null);
								txtEmailId.setReadOnly(true);
								txtEmailId.setEnabled(false);
//								txtEmailId.setRequired(false);
								mandatoryFields
										.remove(txtAcknowledgementContactNumber);
								mandatoryFields.remove(txtEmailId);
							}
						
							}
						
						
						if(null != bean.getDocumentDetails().getSourceOfDocumentValue() && 
								(SHAConstants.SOURCE_DOC_INTERNAL.equalsIgnoreCase(bean.getDocumentDetails().getSourceOfDocumentValue()))){
							txtAcknowledgementContactNumber.setEnabled(false);
							txtEmailId.setEnabled(false);
						}
						
						if (null != value) {
							if (("Insured").equalsIgnoreCase(value.getValue())) {
								
								sourceOfDocument.setEnabled(true);
							}
							else
							{
								sourceOfDocument.setEnabled(false);
							}
						}
						
					}
				});

	}

	private int getDifferenceBetweenDates(Date value) {

		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int) ((currentDay - enteredDay)) / (1000 * 60 * 60 * 24);
		return diff;
	}

	@SuppressWarnings("unchecked")
	public void loadContainerDataSources(Map<String, Object> referenceDataMap) {
		modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap
				.get("modeOfReceipt");
		cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");

		for (int i = 0; i < modeOfReceipt.size(); i++) {
			if (this.bean.getDocumentDetails().getModeOfReceiptValue() != null) {
				if ((this.bean.getDocumentDetails().getModeOfReceiptValue()).equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue())) 
				{
					this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
				}
			}
		}

		/*
		 * docReceivedFromRequest = (BeanItemContainer<SelectValue>)
		 * referenceDataMap .get("docReceivedFrom");
		 * cmbDocumentsReceivedFrom.setContainerDataSource
		 * (docReceivedFromRequest);
		 * cmbDocumentsReceivedFrom.setItemCaptionMode(
		 * ItemCaptionMode.PROPERTY);
		 * cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		 * 
		 * for (int i = 0; i < docReceivedFromRequest.size(); i++) { if
		 * (this.bean.getDocumentDetails().getDocumentReceivedFromValue() !=
		 * null) { if ((this.bean.getDocumentDetails()
		 * .getDocumentReceivedFromValue())
		 * .equalsIgnoreCase(docReceivedFromRequest
		 * .getIdByIndex(i).getValue())) { this.cmbDocumentsReceivedFrom
		 * .setValue(docReceivedFromRequest.getIdByIndex(i)); } }
		 * 
		 * }
		 */
		docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap
				.get("docReceivedFrom");
		cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");

		if (("Cashless").equalsIgnoreCase(this.bean.getClaimDTO()
				.getClaimTypeValue())) {
			for (int i = 0; i < docReceivedFromRequest.size(); i++) {
				if (("Hospital").equalsIgnoreCase(docReceivedFromRequest
						.getIdByIndex(i).getValue())) {
					this.cmbDocumentsReceivedFrom
							.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		} else {
			for (int i = 0; i < docReceivedFromRequest.size(); i++) {
				if (("Insured").equalsIgnoreCase(docReceivedFromRequest
						.getIdByIndex(i).getValue())) {
					this.cmbDocumentsReceivedFrom
							.setValue(docReceivedFromRequest.getIdByIndex(i));
					this.cmbDocumentsReceivedFrom.setEnabled(false);
				}
			}
		}

		/**
		 * Documents received from is disabled as 
		 * per sathish sir comments.
		 * */
		cmbDocumentsReceivedFrom.setEnabled(false);
		
		if(this.bean.getClaimDTO() != null && this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT_ID)){
			cmbDocumentsReceivedFrom.setEnabled(false);
		}

		if (null != cmbPayeeName) {
			payeeNameList = (BeanItemContainer<SelectValue>) referenceDataMap
					.get("payeeNameList");
			cmbPayeeName.setContainerDataSource(payeeNameList);
			cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPayeeName.setItemCaptionPropertyId("value");
			for (int i = 0; i < payeeNameList.size(); i++) {
				if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy()
						.getProposerFirstName()).equalsIgnoreCase(payeeNameList
						.getIdByIndex(i).getValue())) {
					this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
				}
			}
			if (null != this.bean.getClaimDTO()
					&& (ReferenceTable.CLAIM_TYPE_CASHLESS)
							.equalsIgnoreCase(this.bean.getClaimDTO()
									.getClaimTypeValue())) {
				cmbPayeeName.setReadOnly(true);
				cmbPayeeName.setEnabled(false);
			}
		}

		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
		//documentCheckListValidation.setReference(referenceDataMap);
		if(null != documentCheckListValidation)
		{
			documentCheckListValidation.setReferenceData(referenceDataMap);
		}
		this.docsDetailsList = (List<DocumentDetailsDTO>) referenceDataMap
				.get("validationDocList");
		this.docDTO = (DocumentDetailsDTO) referenceDataMap
				.get("billClaissificationDetails");
		setValuesFromDTO();

	}

	@SuppressWarnings("unused")
	private List<Field<?>> getListOfPaymentFields() {
		List<Field<?>> fieldList = new ArrayList<Field<?>>();
		fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirFirstName);
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

	@SuppressWarnings("unused")
	private void unbindField(List<Field<?>> field) {
		if (null != field && !field.isEmpty()) {
			for (Field<?> field2 : field) {
				if (field2 != null) {
					Object propertyId = this.binder.getPropertyId(field2);
					// if (field2!= null && field2.isAttached() && propertyId !=
					// null) {
					if (field2 != null && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}

	private Boolean isValidEmail(String strEmail) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}

	public boolean validatePage() {

		Boolean hasError = false;
		String eMsg = "";

		
		if(null != bean.getDocumentDetails() && null != bean.getDocumentDetails().getSourceOfDocumentValue() &&
				(SHAConstants.SOURCE_DOC_INSURED.equalsIgnoreCase(bean.getDocumentDetails().getSourceOfDocumentValue())))
		{
			
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
					hasError = true;
				}
				
			}
		}

//		if (null != this.txtEmailId && null != this.txtEmailId.getValue()
//				&& !("").equalsIgnoreCase(this.txtEmailId.getValue())) {
//			if (!isValidEmail(this.txtEmailId.getValue())) {
//				hasError = true;
//				eMsg += "Please enter a valid email </br>";
//			}
//		}

		if (null != this.cmbDocumentsReceivedFrom) {
			SelectValue selValue = (SelectValue) this.cmbDocumentsReceivedFrom
					.getValue();
			if ((null != selValue)
					&& ("Insured").equalsIgnoreCase(selValue.getValue())) {
				if (!(null != this.txtAcknowledgementContactNumber
						&& null != this.txtAcknowledgementContactNumber
								.getValue() && !("")
							.equalsIgnoreCase(this.txtAcknowledgementContactNumber
									.getValue()))) {
					hasError = true;
					eMsg += "Please enter Acknowledgement Contact No</br>";
				}
//				if ((!(null != this.txtEmailId
//						&& null != this.txtEmailId.getValue() && !("")
//							.equalsIgnoreCase(this.txtEmailId.getValue())))) {
//					hasError = true;
//					eMsg += "Please enter email Id </br>";
//				}
				if (null != this.cmbModeOfReceipt) {
					SelectValue modeOfReceiptSelValue = (SelectValue) this.cmbModeOfReceipt
							.getValue();
					if (modeOfReceiptSelValue == null) {
						hasError = true;
						eMsg += "Please select Mode of Receipt </br>";
					}
				}
			} else {
				SelectValue selValue1 = (SelectValue) this.cmbDocumentsReceivedFrom
						.getValue();
				if ((null == selValue1)
						|| !("Hospital").equalsIgnoreCase(selValue1.getValue())) {
					eMsg += "Please select Documents Received From </br>";
					if (!(null != this.txtAcknowledgementContactNumber
							&& null != this.txtAcknowledgementContactNumber
									.getValue() && !("")
								.equalsIgnoreCase(this.txtAcknowledgementContactNumber
										.getValue()))) {
						hasError = true;
						eMsg += "Please enter Acknowledgement Contact No</br>";
					}
//					if ((!(null != this.txtEmailId
//							&& null != this.txtEmailId.getValue() && !("")
//								.equalsIgnoreCase(this.txtEmailId.getValue())))) {
//						hasError = true;
//						eMsg += "Please enter email Id </br>";
//					}
				}
				if (null != this.cmbModeOfReceipt) {
					SelectValue modeOfReceiptSelValue = (SelectValue) this.cmbModeOfReceipt
							.getValue();
					if (modeOfReceiptSelValue == null) {
						hasError = true;
						eMsg += "Please select Mode of Receipt </br>";
					}
				}
			}
		}

		
		if (null != this.documentCheckListValidation) {
			Boolean isValid = documentCheckListValidation.validatePageForAckScreen();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.documentCheckListValidation.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}
		 
	}
		
		if(null != selectRODtoAddAdditionalDocumentsTable)
		{
			Boolean isValid = selectRODtoAddAdditionalDocumentsTable.isValid();
			if(!isValid)
			{
				hasError = true;
				String error = this.selectRODtoAddAdditionalDocumentsTable.getErrors();
				eMsg += error + "</br>";
			}
		}
		
		if(null != bean.getDocumentDetails() && null == bean.getDocumentDetails().getSourceOfDocumentValue()){
			
			hasError = true;
			eMsg += "Please Select Source Of Document</br>";
		}	

		if (hasError) {
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
			getUI();
			dialog.show(UI.getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				sourceOfDocPopupMessage();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	private void setTableValues() {
		if (null != this.documentCheckListValidation) {
			
			List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
			
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				//The below select value code needs to be removed before checkin

				if(!isNext)
				{
					
					SelectValue selParticulars = new SelectValue();
					selParticulars.setId(documentCheckListDTO.getKey());
					selParticulars.setValue(documentCheckListDTO.getValue());
					documentCheckListDTO.setParticulars(selParticulars);
					
					SelectValue setReceivedStatus = new SelectValue();
				/*setReceivedStatus.setId(264l);
				setReceivedStatus.setValue("Not Received");*/
					setReceivedStatus.setId(ReferenceTable.DOCUMENT_CHECKLIST_NOT_APPLICABLE);
					setReceivedStatus.setValue(SHAConstants.DOC_CHECKLIST_NOT_APPLICABLE);
				
					documentCheckListDTO.setReceivedStatus(setReceivedStatus);
				}
				/*
				SelectValue setReceivedStatus = new SelectValue();
				setReceivedStatus.setId(264l);
				setReceivedStatus.setValue("Not Received");
				setReceivedStatus.setId(ReferenceTable.DOCUMENT_CHECKLIST_NOT_APPLICABLE);
				setReceivedStatus.setValue(SHAConstants.DOC_CHECKLIST_NOT_APPLICABLE);
				
				documentCheckListDTO.setReceivedStatus(setReceivedStatus);*/
				//documentCheckListDTO.setRemarks("Test");
				this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
			}

		}
	}

	public void setTableValuesToDTO() {
		/**
		 * Get the list of DTO's first. Loop it and get individual object. And
		 * then assign them to dto and set this dto to list. This final list
		 * will be set in bean again.
		 * 
		 * */

		/*List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation
				.getValues();*/
	/*	List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
		this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);*/
		
		List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
		if(null != objDocCheckList && !objDocCheckList.isEmpty())
		{
			for (DocumentCheckListDTO documentCheckListDTO : objDocCheckList) {
				if(null != documentCheckListDTO && null != documentCheckListDTO.getParticulars() && null != documentCheckListDTO.getParticulars().getValue())
				{
					documentCheckListDTO.setValue(documentCheckListDTO.getParticulars().getValue());
					if(null == documentCheckListDTO.getKey())
					{
						documentCheckListDTO.setKey(documentCheckListDTO.getParticulars().getId());
					}
				}
				
			}
		}
		this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);
		
		
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails
				.getValues();
		if (null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty()) {
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
				if (null != rodQueryDetailsDTO2.getReplyStatus()
						&& ("Yes").equals(rodQueryDetailsDTO2.getReplyStatus()
								.trim())) {
					this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
					break;
				}
			}
		}
	}

	public void setValuesFromDTO() {
		DocumentDetailsDTO documentDetails = this.bean.getDocumentDetails();

		/*
		 * if (null != documentDetails.getDocumentsReceivedDate()) {
		 * documentsReceivedDate.setValue(documentDetails
		 * .getDocumentsReceivedDate()); }
		 */

		if (null != documentDetails.getDocumentsReceivedFrom()) {
			for (int i = 0; i < docReceivedFromRequest.size(); i++) {
				if (documentDetails
						.getDocumentsReceivedFrom()
						.getValue()
						.equalsIgnoreCase(
								docReceivedFromRequest.getIdByIndex(i)
										.getValue())) {
					this.cmbDocumentsReceivedFrom
							.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		}

		if (null != documentDetails.getModeOfReceipt()) {
			for (int i = 0; i < modeOfReceipt.size(); i++) {
				if (documentDetails
						.getModeOfReceipt()
						.getValue()
						.equalsIgnoreCase(
								modeOfReceipt.getIdByIndex(i).getValue())) {
					this.cmbModeOfReceipt.setValue(modeOfReceipt
							.getIdByIndex(i));
				}
			}
		}

	}

	public void saveReconsideRequestTableValue(
			ReconsiderRODRequestTableDTO dto,
			List<UploadDocumentDTO> uploadDocsDTO) {
		this.bean.setReconsiderRODdto(dto);
		this.bean.setUploadDocsList(uploadDocsDTO);
	}
	
	public void updateBean(ReceiptOfDocumentsDTO bean){
		this.bean = bean;
		setDocumentReceivedFrom();
	}
	
	
	private void setDocumentReceivedFrom()
	{
		if(null != cmbDocumentsReceivedFrom && null != docReceivedFromRequest)
		{
			for (int i = 0; i < docReceivedFromRequest.size(); i++) {
				
				if(null != bean.getDocumentDetails() && null != bean.getDocumentDetails().getDocumentReceivedFromValue())
				{
					if( bean.getDocumentDetails().getDocumentReceivedFromValue().equalsIgnoreCase(docReceivedFromRequest
							.getIdByIndex(i).getValue()))
					{
						this.cmbDocumentsReceivedFrom
						.setValue(docReceivedFromRequest.getIdByIndex(i));
						this.cmbDocumentsReceivedFrom.setEnabled(false);
					}
				}
				
			}
		}
	}

	public void setDocumentDetailsListForValidation(
			List<DocumentDetailsDTO> documentDetailsDTO) {
		this.docsDetailsList = documentDetailsDTO;
	}
	
	public void isNextClicked()
	{
		isNext = true;
	}
	
	private void sourceOfDocumentListener()
	{
		sourceOfDocument.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();				
				if(null != value)
				{
					if(value){
						
					bean.getDocumentDetails().setSourceOfDocument(value);
					bean.getDocumentDetails().setSourceOfDocumentValue(SHAConstants.SOURCE_DOC_INSURED);	
					acknowledgementReceiptViewImpl.init(bean);
					wizard.removeStep("Acknowledgement Receipt");
					wizard.addStep(acknowledgementReceiptViewImpl,
							"Acknowledgement Receipt");
					/*documentsReceivedDate.setValue(new Date());
					documentsReceivedDate.setEnabled(true);*/
					
					}					
					else
					{
						
					wizard.removeStep("Acknowledgement Receipt");
					bean.getDocumentDetails().setSourceOfDocumentValue(SHAConstants.SOURCE_DOC_INTERNAL);
					bean.getDocumentDetails().setSourceOfDocumentValue(SHAConstants.SOURCE_DOC_INTERNAL);
					txtAcknowledgementContactNumber.setReadOnly(false);
					txtAcknowledgementContactNumber.setValue("0");
					txtAcknowledgementContactNumber.setReadOnly(true);
					/*documentsReceivedDate.setValue(bean.getDocumentDetails().getDocumentsReceivedDate());
					documentsReceivedDate.setEnabled(false);*/
				}
			}
				enableOrDisableFields(value);	
			}

		});
	}
	
	public void sourceOfDocPopupMessage() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + "Source of Document is selected as " +bean.getDocumentDetails().getSourceOfDocumentValue()+ "</b>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
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
				}
			});
		}
	
	public void enableOrDisableFields(Boolean value){
		
		if(value){
			accidentOrDeath.setEnabled(true);
			cmbDocumentsReceivedFrom.setEnabled(false);
			documentsReceivedDate.setEnabled(true);
			cmbModeOfReceipt.setEnabled(true);
			txtAcknowledgementContactNumber.setEnabled(true);
			txtEmailId.setEnabled(true);
			documentCheckListValidation.setEnabled(true);
			accidentOrDeath.setEnabled(true);
		}
		else
		{
			accidentOrDeath.setEnabled(false);
			cmbDocumentsReceivedFrom.setEnabled(false);
			documentsReceivedDate.setEnabled(false);
			cmbModeOfReceipt.setEnabled(false);
			txtAcknowledgementContactNumber.setEnabled(false);
			txtEmailId.setEnabled(false);
			documentCheckListValidation.setEnabled(false);
			bean.getDocumentDetails().setAcknowledgmentContactNumber("0");
			//txtAcknowledgementContactNumber.setValue("0");
			accidentOrDeath.setEnabled(false);
		}
		
	}
	
	public void setThirdPageInstance(AcknowledgementReceiptViewImpl viewImpl){
		this.acknowledgementReceiptViewImpl = viewImpl;
	}

}
