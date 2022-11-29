package com.shaic.claims.reibursement.rod.UploadNEFTDetails;

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
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.FileUploadComponent;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAddlDocsPaymentInfoGridForm;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAddlPaymentInfoPageTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class UploadNEFTDetailsPage extends ViewComponent{

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
	//private UploadDocumentDTO bean;
	//@Inject
//	private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private UploadNEFTDetailsPageTable uploadedDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	@Inject
	private UploadNEFTDetailsGridForm uploadDocsTable;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private FileUploadComponent fileUpload;
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
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
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "Upload NEFT Details");
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setPreseneterString(SHAConstants.UPLOAD_NEFT_DETAILS);
		uploadedDocsTable.setReference(this.referenceData);
		
		
		loadUploadedDocsTable(bean.getUploadDocumentsDTO().getUploadDocsList());
		

		VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);
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
		if(null != this.bean.getUploadDocsList())
		{
			uploadedDocsTable.removeRow();
			//Clearing the old list and re intializing the same for new values
			reset();
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
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
			this.uploadDocsTable.init(new UploadDocumentDTO(),"Upload NEFT Details");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				
			}
		}
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.uploadDocsTable)
		{
			//this.bean.setUploadDocsList(this.uploadDocsTable.getValues());
			this.bean.setUploadDocsList(this.uploadedDocsTable.getValues());
		}
		if(null != this.uploadedDocsTable)
		{
			this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		uploadDocsTable.setFileTypeValues(fileTypeValues);
		//uploadDocsTable.setFileTypeValues(new SelectValue(ReferenceTable.CLAIM_VERIFICATION_REPORT_DOCUMENT_TYPE_KEY,SHAConstants.CLAIM_VERIFICATION_REPORT));
		
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
			
			if(null != this.bean.getRodNumberForUploadTbl())
			{
				uploadDocsDTO.setRodNo(this.bean.getRodNumberForUploadTbl());
			}
			
			if(this.bean.getDocumentDetails() != null && this.bean.getDocumentDetails().getRodKey() != null){
				uploadDocsDTO.setRodKey(this.bean.getDocumentDetails().getRodKey());
			}
			
			if(null != uploadDocsDTO.getFileType())
			{
				uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			}
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			
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
			uploadDocsTable.init(dto,"Upload NEFT Details");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
			
			
		}
	}
	
	public void updateBean(ReceiptOfDocumentsDTO bean){
		this.bean = bean;
		uploadDocsTable.updateBean(bean.getUploadDocumentsDTO());
		addUploadedDocsTableValues();
	}
	
	public Boolean validateUploadDocument(){
		return uploadDocsTable.isValid();
	}
	


}
