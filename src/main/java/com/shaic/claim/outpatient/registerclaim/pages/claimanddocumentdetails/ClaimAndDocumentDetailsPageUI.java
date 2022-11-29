package com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails;

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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.DocumentDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.InsuredDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.pages.InsuredDetailsTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListTable;
import com.shaic.domain.Insured;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ClaimAndDocumentDetailsPageUI extends ViewComponent{/*

	private static final long serialVersionUID = -6039649831441636195L;
	
	@Inject
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	private ComboBox cmbClaimType;
	
	private ComboBox cmbInsuredPatientName;
	
	private TextField amountClaimedTxt;
	
	private TextField provisionAmtTxt;
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private TextField txtAcknowledgementContactNo;
	
	private DateField documentsReceivedDate;
	
	private TextField txtEmailId;
	
	private ComboBox cmbModeOfReceipt;
	
	private TextArea txtAdditionalRemarks;
	
	public Boolean isSILesser = false;
	
	@Inject
	private Instance<InsuredDetailsTable> insuredDetailsTable;
	
	private InsuredDetailsTable insuredDetailsTableObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<DocumentCheckListTable> documentCheckList;
	
	private DocumentCheckListTable documentCheckListObj;
	
	private Map<String, Object> referenceData;
	
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	private VerticalLayout wholeLayout;
	
	private Long insuredKey;
	
	public void init(OutPatientDTO bean, GWizard wizard) {
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
		
		cmbClaimType = (ComboBox) binder.buildAndBind("Claim Type", "claimType", ComboBox.class);
		cmbClaimType.setNullSelectionAllowed(false);
		cmbClaimType.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -3581015654530653813L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox comboBox = (ComboBox) event.getProperty();
				if(comboBox.getValue() != null ) {
					SelectValue value = (SelectValue) comboBox.getValue();
					if(value != null && value.getValue().toString().toLowerCase().contains("out")) {
						if(cmbDocumentsReceivedFrom != null) {
							Collection<?> itemIds = cmbDocumentsReceivedFrom.getContainerDataSource().getItemIds();
							if(!itemIds.isEmpty()) {
								cmbDocumentsReceivedFrom.setValue(itemIds.toArray()[1]);
								//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
								cmbDocumentsReceivedFrom.setEnabled(false);
							}
						}
					} else {
						if(cmbDocumentsReceivedFrom != null) {
							cmbDocumentsReceivedFrom.setEnabled (true);
						}
					}
				} else {
					if(cmbDocumentsReceivedFrom != null) {
						cmbDocumentsReceivedFrom.setEnabled (true);
					}
				}
				
			}
		});
		
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
		insuredDetailsTableObj.init("", true);
		if(bean.getPolicy() != null && bean.getPolicy().getProductType() != null &&  bean.getPolicy().getProductType().getKey().equals(ReferenceTable.FLOATER_POLICY)) {
			insuredDetailsTableObj.setVisibleColumn();
		}
		
		FormLayout formLayout = new FormLayout(provisionAmtTxt);
		formLayout.setMargin(false);
		FormLayout formLayout1 = new FormLayout(amountClaimedTxt);
		formLayout1.setMargin(false);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout1, formLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setMargin(true);
		horizontalLayout.setComponentAlignment(formLayout, Alignment.BOTTOM_RIGHT);
		FormLayout form = new FormLayout(cmbClaimType, cmbInsuredPatientName);
		form.setMargin(false);
		VerticalLayout claimDetailsVLayout = new VerticalLayout(form , insuredDetailsTableObj, horizontalLayout);
		claimDetailsVLayout.setCaption("Claim Details");
		
		cmbDocumentsReceivedFrom = (ComboBox) binder.buildAndBind("Documents Received From", "documentReceivedFrom", ComboBox.class);
		
		documentsReceivedDate = (DateField) binder.buildAndBind("Documents Received Date", "documentReceivedDate", DateField.class);
		cmbModeOfReceipt = (ComboBox) binder.buildAndBind("Mode of Receipt", "modeOfReceipt", ComboBox.class);
		txtAcknowledgementContactNo = (TextField) binder.buildAndBind(
				"Acknowledgement\nContact Number\n(Docs Submitted Person)", "acknowledgementContactNumber", TextField.class);
		txtAcknowledgementContactNo.setMaxLength(15);
		CSValidator validator2 = new CSValidator();
		
		validator2.extend(txtAcknowledgementContactNo);
		validator2.setRegExp("^[0-9]*$");
		validator2.setPreventInvalidTyping(true);
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailID", TextField.class);
		txtAdditionalRemarks = (TextArea) binder.buildAndBind("Additional Document Remarks", "additionalRemarks", TextArea.class);
		
		List<DocumentCheckListDTO> dtoList = new ArrayList<DocumentCheckListDTO>();
		dtoList.addAll(this.bean.getDocumentDetails().getDocumentCheckListDTO());
		
		documentCheckListObj = documentCheckList.get();
		documentCheckListObj.init("", false);
		
		this.bean.getDocumentDetails().setDocumentCheckListDTO(dtoList);
		
		HorizontalLayout hLayout = new HorizontalLayout(new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate, cmbModeOfReceipt ), new FormLayout(txtAcknowledgementContactNo, txtEmailId) );
		hLayout.setSpacing(true);
		hLayout.setWidth("100%");
		VerticalLayout documentDetailsLayout = new VerticalLayout(hLayout, documentCheckListObj, new FormLayout(txtAdditionalRemarks));
		hLayout.setSpacing(true);
		hLayout.setCaption("Document Details");
		
		mandatoryFields.add(cmbClaimType);
		mandatoryFields.add(amountClaimedTxt);
		mandatoryFields.add(provisionAmtTxt);
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(cmbInsuredPatientName);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		mandatoryFields.add(txtAcknowledgementContactNo);
		
		wholeLayout = new VerticalLayout(claimDetailsVLayout, documentDetailsLayout);
		
		showOrHideValidation(false);
		
		addListener();
		
		return wholeLayout;
		
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
		
		cmbDocumentsReceivedFrom.setContainerDataSource(documentReceivedFrom);
		cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		
		if(this.bean.getDocumentDetails().getDocumentReceivedFrom() != null) {
			cmbDocumentsReceivedFrom.setValue(this.bean.getDocumentDetails().getDocumentReceivedFrom());
			SelectValue value = (SelectValue) cmbClaimType.getValue();
			if(value != null && value.getValue().toString().toLowerCase().contains("out")) {
				cmbDocumentsReceivedFrom.setEnabled(false);
			}
		}
		
		cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbModeOfReceipt.setItemCaptionPropertyId("value");
		
		if(this.bean.getDocumentDetails().getModeOfReceipt() != null) {
			cmbModeOfReceipt.setValue(this.bean.getDocumentDetails().getModeOfReceipt());
		}
		
		cmbInsuredPatientName.setContainerDataSource(insuredList);
		cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredPatientName.setItemCaptionPropertyId("insuredName");
		
		if(this.bean.getDocumentDetails().getInsuredPatientName() != null) {
			cmbInsuredPatientName.setValue(this.bean.getDocumentDetails().getInsuredPatientName());
		}
		
		insuredDetailsTableObj.setReference(referenceData);
		documentCheckListObj.setReference(referenceData);
		
		if(documentCheckListObj != null) {
			List<DocumentCheckListDTO> documentCheckListDTO = this.bean.getDocumentDetails().getDocumentCheckListDTO();

			int sno = 1;
			for (DocumentCheckListDTO documentCheckListDTO2 : documentCheckListDTO) {
				documentCheckListDTO2.setSlNo(sno);
				documentCheckListObj.addBeanToList(documentCheckListDTO2);
				sno++;
			}
//			documentCheckListObj.setTableList(this.bean.getDocumentDetails().getDocumentCheckListDTO());
		}
		
		if(insuredDetailsTableObj !=  null) {
			insuredDetailsTableObj.setTableList(this.bean.getDocumentDetails().getInsuredDetailsList());
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();		

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

		
		if(null != this.documentCheckListObj)
		{
			Boolean isValid = documentCheckListObj.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.documentCheckListObj.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		
		List<InsuredDetailsDTO> values = this.insuredDetailsTableObj.getValues();
		if(values.isEmpty()) {
			eMsg.append("Please Enter Atleast one Insured Details to Proceed Further. </br>");
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
				this.bean.getDocumentDetails().setDocumentCheckListDTO(this.documentCheckListObj.getValues());
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
		
		
	}
	
	protected void addListener() {


		cmbInsuredPatientName.addValueChangeListener(new ValueChangeListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured value = (Insured) event.getProperty().getValue();
				if(value != null){
					insuredKey = value.getKey();
				}
				
			}
		});
		
		
	}

	public void setSumInsuredValidation(Integer healthCheckupSumInsured) {
		isSILesser = false;
		if(healthCheckupSumInsured == 0) {
			isSILesser = true;
		} 
		
	}

	public void showSILesserPopup() {
		
		Label label = new Label("Claim Can not be created for this policy because the Selected Insured Sum Insured is less than 3,00,000.", ContentMode.HTML);
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
	
	public Long getInsuredKey(){
		return this.insuredKey;
	}
	
	public void setInsuredKey(){
		this.insuredKey = null;
	}

*/}
