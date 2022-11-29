package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
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
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class CancelDocumentDetailUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CancelDocumentDetailsTable documentDetailsTable;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;
	
	private ComboBox cmbReasonForCancel;
	
	private TextArea txtCancelRemarks;
	
	private ReceiptOfDocumentsDTO bean;
	
	private OptionGroup generateLetterOption;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private GWizard wizard;

	
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
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	
	public Component getComponent(){
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
        documentDetailsTable.init("Document Details", false, false);
        
        if(this.bean.getClaimDTO() != null && this.bean.getClaimDTO().getLobId() != null && ReferenceTable.PA_LOB_KEY.equals(this.bean.getClaimDTO().getLobId())){
        	documentDetailsTable.setPAColumns();
        }
		
		fireViewEvent(CancelDocumentWizardPresenter.SETUP_TABLE_DATA, this.bean.getDocumentDetails().getDocAcknowledgementKey());
		
		cmbReasonForCancel =(ComboBox) binder.buildAndBind("Reason for Cancelling Acknowledgement","cancelAcknowledgementReason", ComboBox.class);
	
		cmbReasonForCancel.setNullSelectionAllowed(false);
		
		txtCancelRemarks = (TextArea) binder.buildAndBind("Remarks for Cancelling the Acknowledgement","cancelAcknowledgementRemarks", TextArea.class);
		
		generateLetterOption = (OptionGroup) binder.buildAndBind( "", "isLetterGenerated", OptionGroup.class);
		generateLetterOption.addItems(getReadioButtonOptions());
		generateLetterOption.setItemCaption(true, "Yes");
		generateLetterOption.setItemCaption(false, "No");
		generateLetterOption.setStyleName("horizontal");
		generateLetterOption.setCaption("Do you want to generate the letter");
		
//		fireViewEvent(CloseClaimPagePresenter.SET_DATA_FIELD, searchDTO);

		FormLayout mainForm = new FormLayout(cmbReasonForCancel,txtCancelRemarks,generateLetterOption);
		mainForm.setSpacing(true);
		
		HorizontalLayout horLayout = new HorizontalLayout(mainForm);
		horLayout.setComponentAlignment(mainForm, Alignment.MIDDLE_CENTER);
		

		
		VerticalLayout mainHor = new VerticalLayout(documentDetailsTable,horLayout);
		mainHor.setComponentAlignment(horLayout, Alignment.MIDDLE_CENTER);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		addListener();
		
		mandatoryFields.add(cmbReasonForCancel);
		mandatoryFields.add(generateLetterOption);
		
		showOrHideValidation(false);
		
		return mainHor;
		
		
	}
	
	public void addListener(){
		
		generateLetterOption.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isFinalChecked = false ;
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					isFinalChecked = true;
				}
				generateLetterPage(isFinalChecked);
			}

			
		});
	}
	
	private void generateLetterPage(Boolean isFinalChecked) {
		if(isFinalChecked) {
			
			this.wizard.getNextButton().setEnabled(true);
			this.wizard.getFinishButton().setEnabled(false);

		} else {
           this.wizard.getNextButton().setEnabled(false);
           this.wizard.getFinishButton().setEnabled(true);
		}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@SuppressWarnings("unchecked")
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
	
		BeanItemContainer<SelectValue> reasonForAcknowledgment = (BeanItemContainer<SelectValue>)referenceDataMap.get("reasonForCancel");
		
		cmbReasonForCancel.setContainerDataSource(reasonForAcknowledgment);
		cmbReasonForCancel.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForCancel.setItemCaptionPropertyId("value");
		
		
	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();		
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
		
		if(documentDetailsTable != null){
			List<ViewDocumentDetailsDTO> values = documentDetailsTable.getValues();
			Boolean isValid = true;
			for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : values) {
				if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
					isValid = false;
					break;
				}
			}
			if(isValid){
				eMsg.append("Please select the Acknowledgement for cancel");
				hasError = true;
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
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			
			try {
				this.binder.commit();
				
//				if(documentDetailsTable != null){
//					List<ViewDocumentDetailsDTO> values = documentDetailsTable.getValues();
//					this.bean.setDocumentDetailsList(values);
//				}
				
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
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

	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	public void setTableValues(
			List<ViewDocumentDetailsDTO> acknowledgmentForCancel) {
		documentDetailsTable.setTableList(acknowledgmentForCancel);
		
	}
	
	
	
	
	

}
