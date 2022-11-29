package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class VerficationConfiremedPage extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ReceiptOfDocumentsDTO bean;

	@Inject
	private SelectedRODForPhysicaldocumentVerificationTable selectedRodTable;	

	private BeanFieldGroup<UploadDocumentDTO> binder;

	private Panel uploadDocsPanel;

	private VerticalLayout uploadDocMainLayout;

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private VerticalLayout mainLayout;	

	protected Map<String, Object> referenceDataForPatientCare = new HashMap<String, Object>();

	private List<String> errorList;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private TextArea makerVerificationRemarks;

	@PostConstruct
	public void init() {

	}

	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		// this.wizard = wizard;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean.getUploadDocumentsDTO());

		uploadDocMainLayout = new VerticalLayout();
		uploadDocsPanel = new Panel();		

		mainLayout = new VerticalLayout();

	}

	public Component getContent() {
		initBinder();
		errorList = new ArrayList<String>();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
			makerVerificationRemarks = new TextArea("Checker Verification Remarks");
		}else {
		makerVerificationRemarks = new TextArea("Maker Verification Remarks");
		}
		
		selectedRodTable.init("", false, false);	
		SelectRODtoAddAdditionalDocumentsDTO selectedPhysicalDocumentsDTO = bean.getSelectedPhysicalDocumentsDTO();
		if(selectedPhysicalDocumentsDTO != null){
			selectedRodTable.addBeanToList(selectedPhysicalDocumentsDTO);
		}
	
		VerticalLayout uploadedDocsLayout = new VerticalLayout(
				selectedRodTable,makerVerificationRemarks);
		
		uploadedDocsLayout.setCaption("Select ROD for Physical Document Verification");
		uploadedDocsLayout.setHeight("100%");
		uploadedDocsLayout.setSpacing(true);
		uploadedDocsLayout.setMargin(true);

		uploadDocMainLayout.addComponent(uploadedDocsLayout);
		uploadDocsPanel.setContent(uploadDocMainLayout);	
		setTableValues();		
		
		mainLayout.addComponent(uploadDocsPanel);
	
		
		return mainLayout;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}	
	

	public boolean validatePage() {

		Boolean hasError = false;
		Boolean errorMsg = Boolean.FALSE;
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
		
		if(selectedRodTable != null){
			List<SelectRODtoAddAdditionalDocumentsDTO> values = selectedRodTable.getValues();
			if(! values.isEmpty()){
				for (SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO : values) {
					if(selectRODtoAddAdditionalDocumentsDTO.getIsDocumentVerified() == null || 
							(selectRODtoAddAdditionalDocumentsDTO.getIsDocumentVerified() != null) && !selectRODtoAddAdditionalDocumentsDTO.getIsDocumentVerified()){
						eMsg.append("Please verify original Document");
						hasError = true;
					}
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
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			errorMsg = Boolean.FALSE;
			//return !hasError;

		} else {
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			errorMsg = Boolean.TRUE;
			//return true;
		}
		
		return errorMsg;

	}

	public List<String> getErrors() {
		return errorList;
	}	
	
	public Void setTableValues()
	{
		return null;
	
	}

}
