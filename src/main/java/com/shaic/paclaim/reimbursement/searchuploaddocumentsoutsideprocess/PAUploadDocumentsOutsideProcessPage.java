package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.SearchUploadDocumentsPreauthDetailsTable;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.FileUploadComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAUploadDocumentsOutsideProcessPage extends ViewComponent{



	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
	//private UploadDocumentDTO bean;
	//@Inject
//	private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private PAUploadDocumentsOutsideProcessPageTable uploadedDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	@Inject
	private PAUploadDocumentsOutsideProcessGridForm uploadDocsTable;
	
	@Inject
	private PASearchUploadDocumentsRODDetailsTable searchUploadDocumentsRoddetailsTable;
	
	@Inject
	private SearchUploadDocumentsPreauthDetailsTable searchUploadDocumentsPreauthdetailsTable;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private FileUploadComponent fileUpload;
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	private	BeanItemContainer<SelectValue> referenceNo;
	
	private TextField txtNoOfDocUploaded = new TextField();
	
	private int uploadFileCount = 0;
	@PostConstruct
	public void init() {
		uploadFileCount = 0;

	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		//setCompositionRoot(getContent());
		
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean.getUploadDocumentsDTO());
		uploadDocMainLayout = new VerticalLayout();
		uploadDocsPanel = new Panel();
		uploadedDocsPanel = new Panel();
		//uploadedDocsTable = new UploadedDocumentsTable();

	}
	
	public Component getContent() 
	{	
		initBinder();
		//uploadDocsTable.init("Upload Documents", false);
		//uploadDocsTable.init("Upload Document");
		searchUploadDocumentsPreauthdetailsTable.init("Pre-auth Details", false,false);	
		searchUploadDocumentsRoddetailsTable.init("ROD Details", false,false);	
		
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "Upload Document (Outside Process)");
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setPreseneterString(SHAConstants.CREATE_ROD);
		uploadedDocsTable.setReference(this.referenceData);
		
		
		loadUploadedDocsTable(bean.getUploadDocumentsDTO().getUploadDocsList());
		

		VerticalLayout uploadDocLayout = new VerticalLayout(searchUploadDocumentsPreauthdetailsTable,searchUploadDocumentsRoddetailsTable,uploadDocsTable);
		//uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
	//	uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		
		
		txtNoOfDocUploaded = new TextField();
		txtNoOfDocUploaded.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		HorizontalLayout countLayout = new HorizontalLayout(txtNoOfDocUploaded);
	//	HorizontalLayout btnLayout = new HorizontalLayout(txtStatusOfBills);
		countLayout.setWidth("100%");
		countLayout.setComponentAlignment(txtNoOfDocUploaded, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout uploadedDocLayout = new VerticalLayout();
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
		uploadedDocLayout.setWidth("100%");
		
		uploadedDocLayout.addComponent(countLayout);
		uploadedDocLayout.addComponent(uploadedDocsTable);

		uploadDocsPanel.setContent(uploadDocLayout);
		//uploadDocsPanel.setCaption("Upload Documents");
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		addListener();
		setTableValues();
		//setCompositionRoot(uploadDocMainLayout);
		return uploadDocMainLayout;
	}
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getUploadDocumentsDTO().getUploadDocsList())
		{
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getUploadDocumentsDTO().getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
				//uploadDocumentDTO.setReferenceNoValue(uploadDocumentDTO.getReferenceNo().getValue());
				//uploadDocumentDTO.setFileName("chestctscan.jpg");
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
				uploadedTblList.add(uploadDocumentDTO);
			}
		}
	}
	
	public void reset()
	{
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			this.uploadedTblList.clear();
		}
		
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			this.uploadDocsTable.init(new UploadDocumentDTO(),"Upload Document (Outside Process)");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setReferencNoValues(referenceNo);
		}
		
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				 this.bean.setUploadDocsList(uploadedTblList);
				
			}
		}
		
		if(null != searchUploadDocumentsPreauthdetailsTable)
		{
			if(null != this.bean.getSearchUploadpreauthUploadTblList())
				searchUploadDocumentsPreauthdetailsTable.setTableList(this.bean.getSearchUploadpreauthUploadTblList());
		}
		if(null != searchUploadDocumentsRoddetailsTable)
		{
			if(null != this.bean.getSearchUploadrodUploadTblList())
				searchUploadDocumentsRoddetailsTable.setTableList(this.bean.getSearchUploadrodUploadTblList());
		}
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.uploadDocsTable)
		{
			//this.bean.setUploadDocsList(this.uploadDocsTable.getValues());
			this.bean.setUploadDocsList(this.uploadedDocsTable.getValues());
		}
		
		if(null != uploadedDocsTable)
		{
			this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 
		 referenceNo = (BeanItemContainer<SelectValue>)referenceData.get("referenceNo");
		uploadDocsTable.setFileTypeValues(fileTypeValues);
		uploadDocsTable.setReferencNoValues(referenceNo);
		//loadUploadedDocsTable(uploadDocsDTO);
		//This needs to be changed.
		//uploadDocsTable.setReferenceData(referenceData);
	}
	

	private void loadUploadedDocsTable(List<UploadDocumentDTO> uploadDocDTOList)
	{
		if(null != uploadDocDTOList && !uploadDocDTOList.isEmpty())
		{
			if(null != uploadedTblList)
			{
				this.uploadedTblList.clear();
				addUploadedDocsTableValues();
			}
		}
		else
		{
			this.uploadedTblList.clear();
			//reset();
		}
	}
	
	protected void addListener() {
		
		

	
	}
	

	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		
		if(null != this.uploadedDocsTable)
		{
			if(null != uploadDocsDTO.getFileType())
			{
				uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			}
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			
			if(null != uploadDocsDTO.getReferenceNo())
			{
				uploadDocsDTO.setReferenceNoValue(uploadDocsDTO.getReferenceNo().getValue());
			}
			
			if(null != uploadDocsDTO.getIsEdit() && !uploadDocsDTO.getIsEdit())
			{
				uploadedTblList.add(uploadDocsDTO);
			}
			
			uploadFileCount++;
			
			//if(null != uploadDocsDTO.getIsEdit() && !uploadDocsDTO.getIsEdit())
				setValueForCountField();
			
			setTableValues();
		}
		
		
	}
	
	private void setValueForCountField()
	{
		int i = 0;
		int j=  0;
		 txtNoOfDocUploaded.setReadOnly(false);
		 if(null != uploadedTblList && !uploadedTblList.isEmpty())
		 {
			 //int iSize = uploadedTblList.size();
			 for (UploadDocumentDTO uploadDto : uploadedTblList) {
				/* if(null != uploadDto.getIsEdit() && !uploadDto.getIsEdit())*/
					i++;
				
			}
			
		 }
		 else
		 {
			 i = 0;
		 }
		 String txtMsg = i +"  records uploaded ";
		 txtNoOfDocUploaded.setValue(txtMsg);
		 txtNoOfDocUploaded.setReadOnly(true);
	}
	
public boolean validatePage() {
		
		Boolean hasError = false;
		String eMsg = "";
		if((!(null != this.uploadedDocsTable && null != this.uploadedDocsTable.getValues() && !this.uploadedDocsTable.getValues().isEmpty()))  
				&& (!(null != uploadedTblList && !uploadedTblList.isEmpty())))
		{			
			hasError = true;
			eMsg = "Please upload one document before submitting. </br>";
		}
		
		if(uploadFileCount == 0)
		{
			hasError = true;
			eMsg = "Please upload one document before submitting. </br>";
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
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;
			return !hasError;
		} 
		else
		{
			return !hasError;
		}
}
	
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable)
		{
			
			this.uploadedDocsTable.removeRow(dto);
			this.uploadedTblList.remove(dto);
			uploadedTblList.remove(dto);
			if(uploadFileCount != 0)
			{
				uploadFileCount--;
			}
		}
		
		setValueForCountField();
		
	}
	
	//Need to add code for edit
	
	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,"Upload Document (Outside Process)");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.setReferencNoValues(referenceNo);
			uploadDocsTable.setValueFromTableForReferenceNo(referenceNo, dto.getReferenceNoValue());
			
			uploadDocsTable.disableFileUploadComponent();
			
			
		}
	}


}
