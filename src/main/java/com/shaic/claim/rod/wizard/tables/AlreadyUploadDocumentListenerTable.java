/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class AlreadyUploadDocumentListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<UploadDocumentDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UploadDocumentDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UploadDocumentDTO> container = new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class);
	
	private Table table;

	//private Button btnAdd;
	
	//private Map<String, Object> referenceData;

	//private List<String> errorMessages;
	
	//private static Validator validator;
	
	//private BeanItemContainer<SelectValue> categoryValues;

	//private String presenterString = "";

	//private UploadDocumentDTO bean;
	//private VerticalLayout layout;

	private VerticalLayout layout;

	/*@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;*/
	
	/*@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;*/
	
	//private Button btnDeleteAll;
	
	private List<BillEntryDetailsDTO> deletedList;
	
	public void initPresenter(String presenterString) {
		//this.presenterString = presenterString;
	}
	
	public void init(UploadDocumentDTO bean) {
		//this.bean = bean;
		container.removeAllItems();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		deletedList = new ArrayList<BillEntryDetailsDTO>();
		/*validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();*/
		
		layout = new VerticalLayout();
		layout.setMargin(true);
		initTable();
		table.setWidth("100%");
		table.setHeight("200px");
		table.setPageLength(table.getItemIds().size());
		
		layout.setCaption("Already Uploaded Documents");
		layout.addComponent(table);
		layout.setWidth("100%");
		layout.setSizeFull();
		
		setCompositionRoot(layout);
		
	}
	
	public void removeRow() {
		table.removeAllItems();
	}	
	
	public void  setTableList(final List<UploadDocumentDTO> list) {
		try
		{
			
			
			if(null != list && !list.isEmpty())
			{
				table.removeAllItems();
				
				/*for (int i = 0; i < list.size(); i++) {
					
				}*/
				
			}
			
		/*for (final UploadDocumentDTO bean : list) {
			table.addItem(bean);
		}*/
		table.sort();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] {"fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue"});
		table.setColumnHeader("fileTypeValue", "File</br> Type");
		table.setColumnHeader("fileName", "File </br> Name");
		table.setColumnHeader("billNo", "Bill </br> No");
		table.setColumnHeader("billDate", "Bill </br> Date");
		table.setColumnHeader("noOfItems", "No of </br>Items");
		table.setColumnHeader("billValue", "Bill </br> Value");
		table.setEditable(true);
		table.removeGeneratedColumn("Attach");
		table.addGeneratedColumn("Attach", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				final UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
				//final Button deleteButton = new Button("Delete");

				final Button attachBtn = new Button("Attach");
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				
				attachBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				attachBtn.setWidth("-1px");
				attachBtn.setHeight("-10px");
				attachBtn.setData(itemId);
				attachBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						table.removeItem(uploadDTO);
						fireViewEvent(CreateRODUploadDocumentsPresenter.ALREADY_UPLOADED_DOCUMENTS,uploadDTO);
					}
				});

				return attachBtn;
			}
		});
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setFooterVisible(true);
		
	}
	
	public List<BillEntryDetailsDTO> getDeletedBillList()
	{
		return deletedList;
	}
	
	
	
	
	/*private void addListener() {
		
	}*/


	
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
	}
	
		
	
	
	public void manageListeners() {

		/*for (UploadDocumentDTO uploadDocDTO : tableItem.keySet()) { 
			//HashMap<String, AbstractField<?>> combos = tableItem.get(uploadDocDTO);
			
			final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");

		}*/
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			 UploadDocumentDTO entryDTO = (UploadDocumentDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			/*if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(entryDTO);
			}*/
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("fileTypeValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("fileTypeValue", field);
				return field;
			}
			
			else if ("fileName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("fileName", field);

				return field;
			}
			else if ("billNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billNo", field);

				return field;
			}
			else if ("billDate".equals(propertyId)) {
				DateField field = new DateField();
				field.setWidth("50px");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billDate", field);
				return field;
			}
			else if ("noOfItems".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfItems", field);
				return field;
			}
			else if("billValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				//field.setWidth("110px");
				//field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("billValue", field);
				enableOrDisableFldsBasedOnFileType();
				return field;

				
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	
	/*private void generateSlNo(TextField txtField)
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("itemNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}*/
	
	 public List<UploadDocumentDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadDocumentDTO> itemIds = (List<UploadDocumentDTO>) table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 
	
	 
	 
	 
	 
		/*private void extracted(ComboBox component) {
		}*/
		
			
	
	 private void enableOrDisableFldsBasedOnFileType()
	 {
		 Collection<UploadDocumentDTO> itemIds = (Collection<UploadDocumentDTO>) table.getItemIds();
		 
		 for (UploadDocumentDTO uploadDocDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(uploadDocDTO);
			 if(null != hashMap)
			 {
				 TextField billNoFld = (TextField)hashMap.get("billNo");
				 DateField dateFld = (DateField)hashMap.get("billDate");
				 TextField noOfItemsFld = (TextField)hashMap.get("noOfItems");
				 TextField billValueFld = (TextField)hashMap.get("billValue");
				 
				 if(null != uploadDocDTO.getFileTypeValue() && uploadDocDTO.getFileTypeValue().contains("Bill"))
				 {
					 if(null != billNoFld)
						 billNoFld.setEnabled(true);
					 if(null != dateFld)
						 dateFld.setEnabled(true);
					 if(null != noOfItemsFld)
						 noOfItemsFld.setEnabled(true);
					 if(null != billValueFld)
						 billValueFld.setEnabled(true);
				 }
				 else
				 {
					 if(null != billNoFld)
						 billNoFld.setEnabled(false);
					 if(null != dateFld)
						 dateFld.setEnabled(false);
					 if(null != noOfItemsFld)
						 noOfItemsFld.setEnabled(false);
					 if(null != billValueFld)
						 billValueFld.setEnabled(false);
				 }
			 }
			
		}
	 }
	 
	 
	
	 
	
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		//this.categoryValues = categoryValues;
		//addCategoryValues();
	}
	
	
	 public void addBeanToList(UploadDocumentDTO uploadDocDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(uploadDocDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	

	
	
	
	
	/*private void showDeleteRowsPopup(String message, final Boolean isConfirmBoxReq,final List<BillEntryDetailsDTO> billEntryList)
	{

		
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final Window dialog = new Window();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}
		getUI().getCurrent().addWindow(dialog);
//				dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null!= isConfirmBoxReq && isConfirmBoxReq)
				{
					if(null != table)
					{
						if(null != billEntryList && !billEntryList.isEmpty())
						{
							for (BillEntryDetailsDTO billEntryDTO : billEntryList) {
								
								deletedList.add(billEntryDTO);
								//table.removeItem(billEntryDTO);
							}
						}
						table.removeAllItems();
					}
					dialog.close();
				}
				else
				{
					dialog.close();
				}
				}
		});
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
			}
		});
	
	}*/

	

}
