
package com.shaic.claim.rod.wizard.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class RODUploadDocumentsListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<UploadDocumentDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UploadDocumentDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UploadDocumentDTO> container = new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	//private BeanItemContainer<SelectValue> classificationContainer ;
	
	//private BeanItemContainer<SelectValue> category;
	
	//private List<String> errorMessages;
	
	//private static Validator validator;
	
	//private BillEntryTableUI billEntryTableObj;
	
	////private static Window popup;
	
	//private Boolean billEntryFinalStatus = false;
	
	/*@Inject
	private Instance<BillEntryTableUI> billEntryTableInstance;*/
	
	//private String billEntryStatus = "NO";
	public Boolean billEntryStatus = false;
	
	public Double totalClaimedValue ;
	
	//private File file;
	

//	private ReceiptOfDocumentsDTO bean;
	
	public void init(String strCaption) {
		container.removeAllItems();
		/*ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();*/
		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
//		/layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setCaption(strCaption);
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<UploadDocumentDTO> list) {
		table.removeAllItems();
		for (final UploadDocumentDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] {"fileUpload", "fileType", "billNo", "billDate", "noOfItems", "billValue" });
		table.setColumnHeader("fileUpload","File Upload");
		table.setColumnHeader("fileType", "File Type");
		table.setColumnHeader("billNo", "Bill No");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("noOfItems", "No of Items");
		table.setColumnHeader("billValue", "Bill Value");
		/*table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		table.setColumnHeader("status", "Status");*/
		table.setEditable(true);	
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	

	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				//BeanItem<UploadDocumentDTO> addItem = container.addItem(new UploadDocumentDTO());
				container.addItem(new UploadDocumentDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	
	public void manageListeners() {

		/*for (UploadDocumentDTO billEntryDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(billEntryDetailsDTO);

		}*/
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory   {
/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*		private static final long serialVersionUID = -2192723245525925990L;
*/
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			UploadDocumentDTO entryDTO = (UploadDocumentDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			}/* else {
				tableRow = tableItem.get(entryDTO);
			}*/
			
			
			tableRow = tableItem.get(entryDTO);
			if("fileUpload".equals(propertyId)) {
				
				FileUploadComponent fileUpload = new FileUploadComponent();
				fileUpload.setData(entryDTO);
				tableRow.put("fileUpload", fileUpload);
				
				//fileUploadComponent = fileUpload;
				/*Upload upload =  new Upload("", this);	
		        upload.addSucceededListener(this);
		        upload.setButtonCaption(null);
		        upload.setData(entryDTO);
		        addUploadBtnListener(upload);
		        
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setValue(entryDTO.getRodNo());
				field.setEnabled(false);
				tableRow.put("fileUpload", field);*/
				return fileUpload;
			} 
			else if ("fileType".equals(propertyId)) {
				
			
				ComboBox box = new ComboBox();
				box.setWidth("200px");
				tableRow.put("fileType", box);
				box.setData(entryDTO);
				addFileTypeValues(box);
				fileTypeComboBoxListener(box);
				//valueChangeLisenerForCombo(box);
				//addCommonValues(box, "source");
				return box;
			}
			else if("billNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setMaxLength(20);
				field.setData(entryDTO);
				field.setValue(entryDTO.getBillNo());
				//field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billNo", field);
				return field;
			} 
			else if ("billDate".equals(propertyId)) {
				DateField date = new DateField();
				date.setWidth("200px");
				date.setDateFormat("dd/MM/yyyy");
				//date.setEnabled(false);
				date.setData(entryDTO);
				date.setValue(entryDTO.getBillDate());
				//date.setEnabled(false);
				tableRow.put("billDate", date);
				return date;
			}
			else if ("noOfItems".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				
				field.setMaxLength(6);
				field.setData(entryDTO);
				field.setValue(String.valueOf(entryDTO.getNoOfItems()));
				//field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfItems", field);
				return field;
			}
			else if ("billValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);
				field.setValue(String.valueOf(entryDTO.getBillValue()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billValue", field);
				
			//	entryDTO.setTotalClaimedAmount(totalClaimedValue);
				return field;
			}
			
			else
			{
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
		
		
		/*@SuppressWarnings("deprecation")
		private void addUploadBtnListener(final Upload upload)
		{
			upload.addListener(new SucceededListener() {
	            private static final long serialVersionUID = 6053253347529760665L;

	            public void uploadSucceeded(SucceededEvent event) {
	            	try
	            	{
		            	byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
		            	if(null != fileAsbyteArray && fileAsbyteArray.length>0)
		            	{
			    			HashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
			    			Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
			    			//TO read file after load
			    			if (flagUploadSuccess.booleanValue())
			    			{
			    				String token = "" + uploadStatus.get("fileKey");
			    				UploadDocumentDTO uploadDocDTO = 
			    				((UploadDocumentDTO)upload.getData()).setFileUpload(token);	
			    				((UploadDocumentDTO)upload.getData()).setFileName((String)uploadStatus.get("fileName"));
			    			}
		            	}
	            	}
	            	catch(Exception e)
	            	{
	            		e.printStackTrace();
	            	}
	    			
	            }
			});
		}*/

		/*@Override
		public void uploadSucceeded(SucceededEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// TODO Auto-generated method stub
			return null;
		}*/
	}
	
	
	/*@SuppressWarnings("deprecation")
	private void addUploadBtnListener(final Upload upload)
	{
		upload.addListener(new SucceededListener() {
            private static final long serialVersionUID = 6053253347529760665L;

            public void uploadSucceeded(SucceededEvent event) {
            	try
            	{
	            	byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
	            	if(null != fileAsbyteArray && fileAsbyteArray.length>0)
	            	{
		    			HashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
		    			Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
		    			//TO read file after load
		    			if (flagUploadSuccess.booleanValue())
		    			{
		    				String token = "" + uploadStatus.get("fileKey");
		    				UploadDocumentDTO uploadDocDTO = 
		    				((UploadDocumentDTO)upload.getData()).setFileUpload(token);	
		    				((UploadDocumentDTO)upload.getData()).setFileName((String)uploadStatus.get("fileName"));
		    			}
	            	}
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            	}
    			
            }
		});
	}
	*/
	public void fileTypeComboBoxListener(final ComboBox cmbFileType){
		
		if(null != cmbFileType)
		{
			cmbFileType
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					//SelectValue value = (SelectValue)event.getProperty().getValue();
					
					List<UploadDocumentDTO> itemIconPropertyId = (List<UploadDocumentDTO>) table.getItemIds();
					 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
					 {
						 for (UploadDocumentDTO uploadDocDTO : itemIconPropertyId) {
							 HashMap<String, AbstractField<?>> hashMap = tableItem.get(uploadDocDTO);
							 if(null != uploadDocDTO.getFileType() && !uploadDocDTO.getFileType().getValue().contains("Bill"))
							 {
								
								 editFeilds(hashMap,false);
							 }
							 else
							 {
								 editFeilds(hashMap,true);
							 }
						 }
					 }
				}
			});
		}
	}
	
	 private void editFeilds( HashMap<String, AbstractField<?>> hashMap, Boolean value)
	 {
		 TextField billNo = (TextField) hashMap.get("billNo");
		 billNo.setEnabled(value);
		 DateField billDate = (DateField) hashMap.get("billDate");
		 billDate.setEnabled(value);
		 TextField noOfItems = (TextField) hashMap.get("noOfItems");
		 noOfItems.setEnabled(value);
		 TextField billValue = (TextField) hashMap.get("billValue");
		 billValue.setEnabled(value);
	 }
	 
	
	
	public void addFileTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("fileType");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	 public void addBeanToList(UploadDocumentDTO uploadDocumentsDTO) {
		 container.addItem(uploadDocumentsDTO);
	    }
	
	
	 public List<UploadDocumentDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadDocumentDTO> itemIds = (List<UploadDocumentDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}
	
	public void clearTableData()
	{
		table.removeAllItems();
	}
	
	public FileUploadComponent getFileUpload()
	{
		FileUploadComponent fUpl = null;
		//List<UploadDocumentDTO> itemIds = (List<UploadDocumentDTO>) this.table.getItemIds() ;
	
		/*for (UploadDocumentDTO uploadDocumentDTO : itemIds) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(uploadDocumentDTO);
			fUpl = (FileUploadComponent) combos.get("fileUpload");
			//fUpl = uploadDocumentDTO.getFileUpload();
		}*/
		return fUpl;
		
	}

	/*@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		return null;
	}*/

	
	
	
}


