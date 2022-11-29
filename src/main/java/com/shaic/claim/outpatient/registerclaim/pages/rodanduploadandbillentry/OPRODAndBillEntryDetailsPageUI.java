package com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.table.OPBillDetailsListenerTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OPRODAndBillEntryDetailsPageUI extends ViewComponent{

	private static final long serialVersionUID = -6039649831441636195L;

	@Inject
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private UploadDocumentGridForm uploadDocsTable;
	
	@Inject
	private UploadedDocumentsTable uploadedDocsTable;
	
	@Inject
	private Instance<OPBillDetailsListenerTable> billDetailsListener;
	
	private OPBillDetailsListenerTable billDetailsListenerObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String, Object> referenceData;

	private VerticalLayout wholeLayout;

	private BeanItemContainer<SelectValue> fileTypeValues;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();

	private Panel uploadDocsPanel;

	private Panel uploadedDocsPanel;

	private VerticalLayout uploadDocMainLayout;

	private Button btnUpload;
	
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	public Component getContent() {
		wizard.getNextButton().setEnabled(true);
		
		uploadDocsTable.init(bean.getOpBillEntryDetails().getUploadDocumentDTO(), SHAConstants.OUTPATIENT_FLAG);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setPreseneterString(SHAConstants.OUTPATIENT_FLAG);
		uploadedDocsTable.setReference(this.referenceData);
		uploadedDocsTable.setVisibleColumns();
		addUploadedDocsTableValues();

		
		/*VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable,btnUpload);
		uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");*/
		
		billDetailsListenerObj = billDetailsListener.get();
		billDetailsListenerObj.init("", bean);
		
		VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);
		//uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
	//	uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		uploadDocLayout.setHeight("200px");
		
		
		HorizontalLayout uploadedDocLayout = new HorizontalLayout(uploadedDocsTable);
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
		uploadedDocLayout.setWidth("100%");

		uploadDocsPanel = new Panel();
		uploadedDocsPanel = new Panel();
		
		uploadDocsPanel.setContent(uploadDocLayout);
		//uploadDocsPanel.setCaption("Upload Documents");
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		uploadDocMainLayout = new VerticalLayout();
		uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		setTableValues();
		
		wholeLayout = new VerticalLayout(uploadDocMainLayout, billDetailsListenerObj);
		
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
		billDetailsListenerObj.setReferenceData(referenceData);
		
		uploadedDocsTable.setReference(referenceData);
		
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 uploadDocsTable.setFileTypeValues(fileTypeValues);
		
		if(billDetailsListenerObj != null) {
			List<OPBillDetailsDTO> billDetailsDTOList = this.bean.getOpBillEntryDetails().getBillDetailsDTOList();
			for (OPBillDetailsDTO opBillDetailsDTO : billDetailsDTOList) {
				billDetailsListenerObj.addBeanToList(opBillDetailsDTO);
			}
		}
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			this.uploadDocsTable.init(new UploadDocumentDTO(),SHAConstants.OUTPATIENT_FLAG);
			uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				/*for (UploadDocumentDTO uploadDTO : uploadedTblList) {
					 this.uploadedDocsTable.addBeanToList(uploadDTO);
				}*/
			}
		}
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.uploadDocsTable)
		{
			//this.bean.setUploadDocsList(this.uploadDocsTable.getValues());
			this.bean.getOpBillEntryDetails().setUploadDocumentDTOList(this.uploadedDocsTable.getValues());
		}
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 uploadDocsTable.setFileTypeValues(fileTypeValues);
	}
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getOpBillEntryDetails().getUploadDocumentDTOList())
		{
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getOpBillEntryDetails().getUploadDocumentDTOList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
			}
		} 
		if(this.bean.getOpBillEntryDetails().getUploadDocumentDTOList().isEmpty()) {
			this.uploadedDocsTable.removeRow();
			this.uploadedDocsTable.init("Uploaded Documents", false);
		}
	}
	
	public void reset()
	{
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			this.uploadedTblList.clear();
		}
	}
	
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable)
		{
			//Code to remove row
			//this.uploadDocsTable.removeRow(dto);
			this.uploadedDocsTable.removeRow(dto);
			this.uploadedTblList.remove(dto);
			uploadedTblList.remove(dto);
		}
		
		
	}
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			uploadedTblList.add(uploadDocsDTO);
			setTableValues();
		}
	}
	
	//Need to add code for edit
	
	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,SHAConstants.OUTPATIENT_FLAG);
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			
			//uploadDocsTable.setFileTypeValues(fileTypeValues);
			/*this.uploadDocsTable.clearTableData();
			this.uploadDocsTable.addBeanToList(dto);*/
		}
	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		this.wizard.getFinishButton().setEnabled(false);
		StringBuffer eMsg = new StringBuffer();		
		setTableValuesToDTO();
		if(!(null != this.uploadedDocsTable && null != this.uploadedDocsTable.getValues() && !this.uploadedDocsTable.getValues().isEmpty()))
		{
//			hasError = true;
//			eMsg = "Please upload one document before submitting your Register Claim </br>";
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
			this.wizard.getFinishButton().setEnabled(true);
			hasError = true;
			return !hasError;
		} else {
			
			this.bean.getOpBillEntryDetails().setBillDetailsDTOList(this.billDetailsListenerObj.getValues());
			showOrHideValidation(false);
			return true;
		}
	}
	
}
