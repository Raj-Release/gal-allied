/**
 * 
 */
package com.shaic.claim.rod.wizard.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsForBillEntry;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryUploadDocumentsPage extends ViewComponent {
	
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	//@Inject
	//private UploadDocumentsTable uploadDocsTable;
	//private RODUploadDocumentsListenerTable uploadDocsTable;
	
	@Inject
	private UploadedDocumentsForBillEntry uploadedDocsTable;
	
	@Inject
	private UploadDocumentGridForm uploadDocsTable;
	
	private BeanFieldGroup<UploadDocumentDTO> binder;
	
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	private VerticalLayout uploadDocMainLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
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

	}
	
	public Boolean alertMessageForPED() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.PED_RAISE_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public Component getContent() 
	{	
//		if(bean.getIsPEDInitiated()) {
//			alertMessageForPED();
//		}
		initBinder();
		//uploadDocsTable.init("Upload Documents", false);
		//uploadDocsTable.init("Upload Documents");
		
		//uploadedTblList = new ArrayList<UploadDocumentDTO>();
		
		uploadDocsTable.init(bean.getUploadDocumentsDTO(), "Bill Entry");
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.initPresenter(SHAConstants.BILL_ENTRY);
		uploadedDocsTable.setReference(this.referenceData);
		
		
		//addUploadedDocsTableValues();

		
		/*VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable,btnUpload);
		uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);*/
		
		VerticalLayout uploadDocLayout = new VerticalLayout(uploadDocsTable);
		//uploadDocLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		uploadDocLayout.setCaption("Upload Documents");
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
		
		/*HorizontalLayout uploadedDocLayout = new HorizontalLayout(uploadedDocsTable);
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);*/

		uploadDocsPanel.setContent(uploadDocLayout);
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);
		
		if(!(null != uploadedTblList && !uploadedTblList.isEmpty()))
		{
			addUploadedDocsTableValues();
		}
		
		addListener();
		setTableValues();
		return uploadDocMainLayout;
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
	
	
	private void addUploadedDocsTableValues()
	{
		if(null != this.bean.getUploadDocsList())
		{
			List<UploadDocumentDTO> uploadDocsDTO = this.bean.getUploadDocsList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				
				//the below value is hardcoded. But this will be later obtained from the upload document dto
				uploadDocumentDTO.setFileTypeValue(uploadDocumentDTO.getFileType().getValue());
				//uploadDocumentDTO.setFileName("chestctscan.jpg");
				this.uploadedDocsTable.addBeanToList(uploadDocumentDTO);
				uploadedTblList.add(uploadDocumentDTO);
			}
			setValueForCountField();
		}
		
		
	}
	
	private void setTableValues()
	{
		if(null != this.uploadDocsTable)
		{
			//this.uploadDocsTable.addBeanToList(new UploadDocumentDTO());
			this.uploadDocsTable.init(new UploadDocumentDTO(), "Bill Entry");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
		}
		if(null != this.uploadedDocsTable)
		{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				System.out.println("UPlaod Action Called and added to final table"+bean.getIntimationNo());
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				/*for (UploadDocumentDTO uploadDTO : uploadedTblList) {
					 this.uploadedDocsTable.addBeanToList(uploadDTO);
				}*/
			}
			/*if(null != uploadedTblList && !uploadedTblList.isEmpty())
			{
				for (UploadDocumentDTO uploadDTO : uploadedTblList) {
					 this.uploadedDocsTable.addBeanToList(uploadDTO);
				}
			}*/
		}
		
		
	}
	
	public void setTableValuesToDTO()
	{
		/*if(null != this.uploadDocsTable)
		{
			this.bean.setUploadDocsList(this.uploadDocsTable.getValues());
		}*/
		
		if(null != this.uploadedDocsTable)
		{
			this.bean.setUploadDocsList(this.uploadedDocsTable.getValues());
		}
		if(null != uploadedDocsTable)
		{
			this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
		}
		/*if(null != this.uploadedDocsTable)
		{
			this
		}*/
	}
	
	
	public void setFileTypeValues(Map<String, Object> referenceData)
	{
		//uploadDocsTable.setReference(referenceData);
		//uploadDocsTable.setReferenceData(referenceData);
		 fileTypeValues = (BeanItemContainer<SelectValue>)referenceData.get("fileType");
		 uploadDocsTable.setFileTypeValues(fileTypeValues);

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
						if(null != uploadDoc.getFileType() && null != uploadDoc.getFileType().getValue())
							fireViewEvent(BillEntryUploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS,uploadDoc);
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
	
	
	

	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO)
	{
		if(null != this.uploadedDocsTable)
		{
			if(null != this.bean.getRodNumberForUploadTbl())
			{
				uploadDocsDTO.setRodNo(this.bean.getRodNumberForUploadTbl());
			}
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
			uploadDocsDTO.setRodNo(uploadDocsDTO.getRodNo());
			//this.uploadedDocsTable.addBeanToList(uploadDocsDTO);
			//uploadedTblList.add(uploadDocsDTO);
			
			if(uploadedDocsTable != null && uploadedDocsTable.getValues() != null && ! uploadedDocsTable.getValues().isEmpty()){
				//uploadedTblList.clear();
				System.out.println("UPlaod Action Called and added to table"+uploadDocsDTO.getRodNo());
				uploadedTblList.addAll(uploadedDocsTable.getValues());
			}
	
			if(null != uploadDocsDTO.getIsEdit() && !uploadDocsDTO.getIsEdit())
			{
				for (UploadDocumentDTO uploadDto : uploadedTblList) {
					uploadDocsDTO.setRodNo(uploadDto.getRodNo());
				}
				
				uploadedTblList.add(uploadDocsDTO);
			}
			
			
			setValueForCountField();
			//this.uploadDocsTable.removeRow(uploadDocsDTO);
			setTableValues();
		}
		
		 
		
		
	}
	
	
	private void setValueForCountField()
	{
		int i = 0;
		 txtNoOfDocUploaded.setReadOnly(false);
		 if(null != uploadedTblList && !uploadedTblList.isEmpty())
		 {
			 i = uploadedTblList.size();
			 //int iSize = uploadedTblList.size();
			 /*for (UploadDocumentDTO uploadDto : uploadedTblList) {
				
					i++;
				
			}*/
			 
			 
		 }
		 else
		 {
			 i = 0;
		 }
		 String txtMsg = i +"  records uploaded ";
		 txtNoOfDocUploaded.setValue(txtMsg);
		 txtNoOfDocUploaded.setReadOnly(true);
	}
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		
		if(null != this.uploadDocsTable)
		{
			
			this.uploadedDocsTable.removeRow(dto);

			this.uploadedTblList.clear();
			
			List<UploadDocumentDTO> values = this.uploadedDocsTable.getValues();
			
//			this.uploadedTblList.remove(dto);
			this.uploadedTblList.addAll(values);
			
			setValueForCountField();
		}
		
		
	}
	
	public Boolean isValid()
	{
		try
		{
			this.binder.commit();
			/*if(null != uploadedDocsTable)
			{
				this.bean.getUploadDocumentsDTO().setDeletedDocumentList(this.uploadedDocsTable.getDeletedDocumentList());
			}*/
		}
		catch (CommitException ce)
		{
			ce.printStackTrace();
		}
		return true;
	}

	public void editUploadedDocumentDetails(UploadDocumentDTO dto)
	{
		if(null != this.uploadDocsTable)
		{
			uploadDocsTable.init(dto,"Bill Entry");
			uploadDocsTable.setFileTypeValues(fileTypeValues);
			uploadDocsTable.setValueFromTable(fileTypeValues, dto.getFileTypeValue());
			uploadDocsTable.disableFileUploadComponent();
			/*this.uploadDocsTable.clearTableData();
			this.uploadDocsTable.addBeanToList(dto);*/
		}
	}
	
public boolean validatePage() {
		
		Boolean hasError = false;
		String eMsg = "";		
	
		if(!(null != this.uploadedDocsTable && null != this.uploadedDocsTable.getValues() && !this.uploadedDocsTable.getValues().isEmpty()))
		{
			hasError = true;
			eMsg += "Please upload one document before submitting your Bill Entry Request </br>";
		}
		

		if (hasError) {
			
			/*Label label = new Label(eMsg, ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			hasError = true;
			return !hasError;
		} 
		else
		{
			return !hasError;
		}
}
	
	public void setClearReferenceData(){
		SHAUtils.setClearReferenceData(referenceData);
		if(uploadDocMainLayout != null){
			uploadDocMainLayout.removeAllComponents();
		}
	}

}

