package com.shaic.paclaim.rod.createrod.search;

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
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.AlreadyUploadedDocumentTable;
import com.shaic.claim.rod.wizard.tables.FileUploadComponent;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsTable;
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

public class PACreateRODUploadDocumentsPage extends ViewComponent{
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
//	private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private UploadedDocumentsTable uploadedDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	@Inject
	private UploadDocumentGridForm uploadDocsTable;
	
	/*@Inject
	private AlreadyUploadDocumentListenerTable alreadyUploadDocTable;*/
	
	@Inject
	private AlreadyUploadedDocumentTable alreadyUploadDocTable;
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = null;
	
	private FileUploadComponent fileUpload;
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
	private TextField txtNoOfDocUploaded = new TextField();
	
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean
				.getUploadDocumentsDTO());
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
		uploadedTblList = new ArrayList<UploadDocumentDTO>() ;
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "PA Create ROD");
		alreadyUploadDocTable.init("Already Uploaded Documents",false);
		alreadyUploadDocTable.setPreseneterString(SHAConstants.PA_CREATE_ROD);
		alreadyUploadDocTable.setReference(this.referenceData);
	
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setPreseneterString(SHAConstants.PA_CREATE_ROD);
		uploadedDocsTable.setReference(this.referenceData);
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			uploadedDocsTable.setEnabled(false);
		}
		
		
		loadUploadedDocsTable(bean.getUploadDocsList());
		
	/*	if(null != this.bean.getUploadDocsList() && !this.bean.getUploadDocsList().isEmpty())
		{
			if(!(null != uploadedTblList && !uploadedTblList.isEmpty()))
			{
				addUploadedDocsTableValues();
			}
		}
		else
		{
			uploadedTblList.clear();
		}*/
		
		//addUploadedDocsTableValues();

		
		/*VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable,btnUpload);
		uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");*/
		
		//VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);
		VerticalLayout uploadDocLayout = new VerticalLayout();
		uploadDocLayout.addComponent(alreadyUploadDocTable);
		uploadDocLayout.addComponent(uploadDocsTable);
		//uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
	//	uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			uploadDocLayout.setEnabled(false);
		}
		//uploadDocLayout.setHeight("200px");
		
		
		
		
		/*HorizontalLayout uploadedDocLayout = new HorizontalLayout(uploadedDocsTable);
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
		uploadedDocLayout.setWidth("100%");*/
		
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
		setAlreadyUploadedTableValues();
		return uploadDocMainLayout;
	}
	
	private void addUploadedDocsTableValues()
	{
		/*if(null != this.bean.getUploadDocsList())
		{*/
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
				//uploadDocumentDTO.setFileName("chestctscan.jpg");
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
				uploadedTblList.add(uploadDocumentDTO);
			}
		//}
	}
	
	public void reset()
	{
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			this.uploadedTblList.clear();
		}
		
		if(null != this.uploadedDocsTable)
		{
			List<UploadDocumentDTO> uploadedDeletedList = this.uploadedDocsTable.getDeletedDocumentList();
			if(null != uploadedDeletedList && !uploadedDeletedList.isEmpty())
			{
				uploadedDeletedList.clear();
			}
		}
		
		/*if(null != uploadDocMainLayout)
		{
			Iterator<Component> componentIterator = uploadDocMainLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component component = componentIterator.next() ;
				if(component instanceof  Panel)
				{
					Panel panel1 = (Panel)component;
					Iterator<Component> subCompents = panel1.iterator();
					while (subCompents.hasNext())
					{
						Component indivdualComp = subCompents.next() ;
						if(indivdualComp instanceof HorizontalLayout)
						{
							HorizontalLayout hLayout2 = (HorizontalLayout)indivdualComp;
							Iterator<Component> subComp = hLayout2.iterator();
							while(subComp.hasNext())
							{
								Component indivdualSubComp = subComp.next() ;
								if(indivdualSubComp instanceof UploadedDocumentsForBillEntry)
								{
									((HorizontalLayout) indivdualComp).removeAllComponents();
									UploadedDocumentsForBillEntry uploadTbl = (UploadedDocumentsForBillEntry)indivdualSubComp;
									uploadTbl.removeRow();
								}
							}
						}
					}
				}
			}
		}*/
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			this.uploadDocsTable.init(new UploadDocumentDTO(),"PA Create ROD");
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
	
	
	private void setAlreadyUploadedTableValues()
	{
		if(null != this.alreadyUploadDocTable && null != bean.getAlreadyUploadDocsList())
		{
			/*for (UploadDocumentDTO uploadDocDTO : bean.getAlreadyUploadDocsList()) {
				this.alreadyUploadDocTable.addBeanToList(uploadDocDTO);
			}
			*/
			//this.alreadyUploadDocTable.removeRow();
			/**
			 * For issue GALAXYMAIN-5859.
			 * When moving front and back after attaching the document,
			 * in already uploaded table, entries with bill no, date and amount are observed. This is a bug.
			 * Hence to avoid this, if in dto if bill no is present, that record will not be added to the
			 * table.
			 * 
			 * */
			for (UploadDocumentDTO uploadDTO : bean.getAlreadyUploadDocsList()) {
				if(null != uploadDTO && (null == uploadDTO.getBillNo() || ("").equalsIgnoreCase(uploadDTO.getBillNo())))
						{
							this.alreadyUploadDocTable.addBeanToList(uploadDTO);
						}
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
		if(null != uploadedDocsTable)
		{
			this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		uploadDocsTable.setFileTypeValues(fileTypeValues);
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
		
		

		/*btnUpload.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(null != uploadDocsTable && !uploadDocsTable.getValues().isEmpty())
				{
					List<UploadDocumentDTO> uploadDocList = uploadDocsTable.getValues();

					if(null != uploadDocList && !uploadDocList.isEmpty())
					{
						UploadDocumentDTO uploadDoc = uploadDocList.get(0);
						synchronized (this) 
						{	
							uploadDoc.getFileUpload().upload.submitUpload();
						}
						if(null != uploadDoc.getFileType() && null != uploadDoc.getFileType().getValue())
							fireViewEvent(CreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS,uploadDoc);
						else
						{
							Label label = new Label("Please upload one document before pressing upload button", ContentMode.HTML);
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
						}
					
					}
					
				}
			}
		});*/
	}
	
	/*private void removeRowFromUploadTable(List<UploadDocumentDTO> uploadDocList)
	{
		if(null != this.uploadDocsTable)
		{
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocList) {
				
				uploadDocsTable.removeRow(uploadDocumentDTO);
			}
		}
	}*/
	
	/*public void loadUploadedDocsTableValues(List<UploadDocumentDTO> uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			if(null != uploadDocsDTO && !uploadDocsDTO.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
					
					//the below value is hardcoded. But this will be later obtained from the upload document dto
					if(null != uploadDocumentDTO.getFileType())
					{
					uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
					uploadDocumentDTO.setFileName("chestctscan.jpg");
					this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
					//this.uploadDocsTable.removeRow(uploadDocumentDTO);
					//setTableValues();

					}
					else
					{
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Please upload atleast one document to proceeed"));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
				}
				setTableValues();
			}
		}
		
	}*/
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			/**
			 * If previous ROD is linked , then that linked ROD number should be populated.
			 * This is pending. 
			 * */
			/*if(null != this.bean.getRodNumberForUploadTbl())
			{
				uploadDocsDTO.setRodNo(this.bean.getRodNumberForUploadTbl());
			}*/
			//This below if condition needs to be removed.
			if(null != uploadDocsDTO.getFileType())
			{
				uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			}
			
			ReconsiderRODRequestTableDTO reconsiderDTO = bean.getReconsiderRODdto();
			if(null != reconsiderDTO)
			{
				uploadDocsDTO.setRodNo(reconsiderDTO.getRodNo());
			}
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			//this.uploadedDocsTable.addBeanToList(uploadDocsDTO);
			
			if(null != uploadDocsDTO.getIsEdit() && !uploadDocsDTO.getIsEdit())
			{
				uploadedTblList.add(uploadDocsDTO);
			}
			
			
			/*uploadedTblList.add(uploadDocsDTO);*/
			
			
			setValueForCountField();
			//Code to remove record needs to be added
			
		//	this.uploadDocsTable.removeRow(uploadDocsDTO);
			setTableValues();
		}
		
		
	}
	
	private void setValueForCountField()
	{
		int i = 0;
		 txtNoOfDocUploaded.setReadOnly(false);
		 if(null != uploadedTblList && !uploadedTblList.isEmpty())
		 {
			 //int iSize = uploadedTblList.size();
			 for (UploadDocumentDTO uploadDto : uploadedTblList) {
				
					i++;
				
			}
			 /*String txtMsg = i +"  records uploaded ";
			 txtNoOfDocUploaded.setValue(txtMsg);
			 txtNoOfDocUploaded.setReadOnly(true);*/
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
		
		if((null != this.alreadyUploadDocTable && null != this.alreadyUploadDocTable.getValues() && !alreadyUploadDocTable.getValues().isEmpty()))
		{
			hasError = true;
			eMsg += "Please attach the uploaded documents in acknowledge stage for ROD creation </br>";
		}
		if(!(null != this.uploadedDocsTable && null != this.uploadedDocsTable.getValues() && !this.uploadedDocsTable.getValues().isEmpty()))
		{
			hasError = true;
			eMsg += "Please upload one document before submitting your create ROD Request </br>";
		}
		
		
		List<UploadDocumentDTO> uploadDocsList = this.bean.getUploadDocsList();
		if(null != uploadDocsList && !uploadDocsList.isEmpty())
		{
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				
				if(null != uploadDocumentDTO && null != uploadDocumentDTO.getIsAlreadyUploaded() && uploadDocumentDTO.getIsAlreadyUploaded() &&  
						null != uploadDocumentDTO.getFileTypeValue() && uploadDocumentDTO.getFileTypeValue().contains("Bill"))
				{
					if(null == uploadDocumentDTO.getBillNo() || ("").equalsIgnoreCase(uploadDocumentDTO.getBillNo()))
					{
						hasError = true;
						eMsg += "Please enter bill no  </br>";
					}
					if(null == uploadDocumentDTO.getBillDate())
					{
						hasError = true;
						eMsg += "Please enter bill date  </br>";
					}
					if(null == uploadDocumentDTO.getNoOfItems())
					{
						hasError = true;
						eMsg += "Please enter no Of items </br>";
					}
					if(null == uploadDocumentDTO.getBillValue())
					{
						hasError = true;
						eMsg += "Please enter bill value </br>";
					}
					
				}
			}
		}
		
		
//		if(bean.getIsDishonoured()) {
//			hasError = true;
//			eMsg = "Cheque status is Dishonounred. Hence this Rod can not be created. </br>";
//		}
		
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
			//Code to remove row
			//this.uploadDocsTable.removeRow(dto);
			this.uploadedDocsTable.removeRow(dto);
			this.uploadedTblList.remove(dto);
			uploadedTblList.remove(dto);
		}
		
		setValueForCountField();
		
	}
	
	//Need to add code for edit
	
	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,SHAConstants.CREATE_ROD_PA);
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
			
			//uploadDocsTable.setFileTypeValues(fileTypeValues);
			/*this.uploadDocsTable.clearTableData();
			this.uploadDocsTable.addBeanToList(dto);*/
		}
	}

	public void loadAlreadyUploadedDocsTableValues(
			UploadDocumentDTO uploadDocList) {
		// TODO Auto-generated method stub
		
	}
	
	public void attachDocForROD(UploadDocumentDTO uploadDTO)
	{
		if(null != alreadyUploadDocTable)
		{
			alreadyUploadDocTable.removeItem(uploadDTO);
		}
		loadUploadedDocsTableValues(uploadDTO);
	}


}
