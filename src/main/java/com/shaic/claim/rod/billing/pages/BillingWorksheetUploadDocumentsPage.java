/**
 * 
 */
package com.shaic.claim.rod.billing.pages;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.FileUploadComponent;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class BillingWorksheetUploadDocumentsPage extends ViewComponent {
	
	
	@Inject
	private PreauthDTO bean;
	
	//@Inject
//	private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private BillingWorksheetUploadedDocumentsTable uploadedDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	@Inject
	private UploadDocumentGridForm uploadDocsTable;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	private FileUploadComponent fileUpload;
	
	private	BeanItemContainer<SelectValue> fileTypeValues ;
	
	private String presenterString;
	
	private Button btnSubmit;
	private Window popup;
	
	
	@PostConstruct
	public void init() {

	}
	
	public void init(PreauthDTO bean,Window popup) {
		this.bean = bean;
		this.popup = popup;	
		//this.wizard = wizard;
	}
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean
				.getUploadDocDTO());
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
		uploadDocsTable.init(bean.getUploadDocDTO(), this.presenterString);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.initPresenter(this.presenterString);
		//uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.init("",false);
		//uploadedDocsTable.setCaption("");
		uploadedDocsTable.setReference(this.referenceData);
		
		
		if(!(null != uploadedTblList && !uploadedTblList.isEmpty()))
		{
			addUploadedDocsTableValues();
		}

		
		/*VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable,btnUpload);
		uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");*/
		
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

		uploadDocsPanel.setContent(uploadDocLayout);
		//uploadDocsPanel.setCaption("Upload Documents");
		//uploadedDocsPanel.setContent(uploadedDocLayout);
		uploadedDocsPanel.setCaption("Uploaded Documents");
		uploadedDocsPanel.setContent(uploadedDocsTable);
		//
		uploadDocMainLayout.addComponent(uploadDocsPanel);
	//	uploadDocMainLayout.addComponent(uploadedDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		
		HorizontalLayout btnLayout = new HorizontalLayout(getSubmitBtn());
		uploadDocMainLayout.addComponent(btnLayout);
		uploadDocMainLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		
		addListener();
		setTableValues();
	//	setCompositionRoot(uploadDocMainLayout);
		return uploadDocMainLayout;
	}
	
	private Button getSubmitBtn()
	{
		btnSubmit = new Button("Submit");
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		return btnSubmit;
	}
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getUploadDocDTO().getBillingWorkSheetUploadDocumentList() && !this.bean.getUploadDocDTO().getBillingWorkSheetUploadDocumentList().isEmpty())
		{
			List<UploadDocumentDTO> uploadDocsDTO =  this.bean.getUploadDocDTO().getBillingWorkSheetUploadDocumentList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				//uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
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
			this.uploadDocsTable.init(new UploadDocumentDTO(),this.presenterString);
			//uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		
		if(null != this.uploadedDocsTable)
		{
			/*if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				uploadedTblList.clear();
			}*/
			fireViewEvent(BillingWorksheetUploadDocumentsPresenter.RETREIVE_VALUES_FOR_UPLOADED_TABLE, this.bean.getKey());
			/*uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				for (UploadDocumentDTO uploadDTO : uploadedTblList) {
					 this.uploadedDocsTable.addBeanToList(uploadDTO);
				}
			}*/
		}
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != this.uploadedDocsTable)
		{
			//this.bean.setUploadDocsList(this.uploadDocsTable.getValues());
			//this.bean.setUploadDocumentDTO(this.uploadedDocsTable.getValues());
			this.bean.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(this.uploadedDocsTable.getValues());
			this.bean.getUploadDocDTO().setBillingWorksheetDeletedList(this.uploadedDocsTable.getDeletedDocumentList());
			
		}
		
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		uploadDocsTable.setFileTypeValues(fileTypeValues);
		//This needs to be changed.
		//uploadDocsTable.setReferenceData(referenceData);
	}
	

	
	protected void addListener() {
		
		

		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				setTableValuesToDTO();
				//if(validatePage())
				{
					fireViewEvent(BillingWorksheetUploadDocumentsPresenter.SAVE_BILLING_UPLOADED_DOCUMENTS, bean,popup);
				}
				/*if(null != uploadDocsTable && !uploadDocsTable.getValues().isEmpty())
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
					
				}*/
			}
		});
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
			/*if(null != uploadDocsDTO.getFileType())
			{
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			}*/
			uploadDocsDTO.setRodNo(this.bean.getRodNumber());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			uploadDocsDTO.setCreatedBy(this.bean.getStrUserName());
			uploadDocsDTO.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			//this.uploadedDocsTable.addBeanToList(uploadDocsDTO);
			uploadedTblList.add(uploadDocsDTO);
			
			//Code to remove record needs to be added
			
		//	this.uploadDocsTable.removeRow(uploadDocsDTO);
			//setTableValues();
			
			setUploadedDocsTableValues();
		}
		
		
	}
	
public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			for (UploadDocumentDTO uploadDTO : uploadedTblList) {
				if(!(null != uploadDTO.getBillingWorkSheetRemarks() && !("").equalsIgnoreCase(uploadDTO.getBillingWorkSheetRemarks())))
				{
					hasError = true;
					eMsg.append("Please add remarks for the document which is to be uploaded.");
				}
				
			}
		}
		
		if(!(null != this.uploadedDocsTable && null != this.uploadedDocsTable.getValues() && !this.uploadedDocsTable.getValues().isEmpty()))
		{
			hasError = true;
			eMsg.append("Please upload one document before submitting </br>");
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
		
		
	}

	public void setUploadedDocsTableValues() {
		
		
		if(null != this.uploadDocsTable)
		{
			this.uploadDocsTable.init(new UploadDocumentDTO(),this.presenterString);
			//uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		
		uploadedDocsTable.removeRow();
		if(null != uploadedTblList && !uploadedTblList.isEmpty())
		{
			
			 this.uploadedDocsTable.setTableList(uploadedTblList);
			for (UploadDocumentDTO uploadDTO : uploadedTblList) {
				 this.uploadedDocsTable.addBeanToList(uploadDTO);
			}
			//this.uploadedTblList = uploadList;
		}
		
	}
	
	public void intializeUploadedTableList(List<UploadDocumentDTO> uploadList)
	{
		if(null != uploadedTblList)
		{
			uploadedTblList.clear();
			if(null != uploadList && !uploadList.isEmpty())
			{
				this.uploadedTblList = uploadList;
				setUploadedDocsTableValues();
			}
		}
	}
	
	//Need to add code for edit
	/*
	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,"Create ROD");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
			
			//uploadDocsTable.setFileTypeValues(fileTypeValues);
			this.uploadDocsTable.clearTableData();
			this.uploadDocsTable.addBeanToList(dto);
		}
	}*/

}

