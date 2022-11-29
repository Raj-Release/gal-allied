/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryTableUI;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class UploadedDocumentsTableForBillEntry extends GListenerTable<UploadDocumentDTO> implements TableCellSelectionHandler {
	
	//private List<String> errorMessages;
	
	
	public UploadedDocumentsTableForBillEntry() {
		super(UploadedDocumentsTableForBillEntry.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"fileUpload", "fileType", "billNo", "billDate", "noOfItems", "billValue"};*///, "selectForBillEntry" ,"status"};

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();*/
	
	////private static Window popup;
	
	@Inject
	private Instance<BillEntryTableUI> billEntryTableInstance;
	
	private BillEntryTableUI billEntryTableObj;
	
	private Map<String, Object> referenceData;


	/*static {
		
		//The file upload mapping will be changed once the upload component works.
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",Upload.class, String.class, false));
		fieldMap.put("fileType", new TableFieldDTO("fileType",ComboBox.class, SelectValue.class, true));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
		//fieldMap.put("selectForBillEntry", new TableFieldDTO("selectForBillEntry", Button.class,Boolean.class, true));
		//fieldMap.put("status", new TableFieldDTO("status", OptionGroup.class,Boolean.class, true));
	}*/

	
	
	public void removeRow(UploadDocumentDTO dto) {
		
		table.removeItem(dto);
	}

	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"fileUpload", "fileType", "billNo", "billDate", "noOfItems", "billValue"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		
		table.addGeneratedColumn("selectforbillentry", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("Bill Entry");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Bill Entry");
						popup.setWidth("75%");
						popup.setHeight("85%");
						
						billEntryTableObj = billEntryTableInstance.get();
						//billEntryTableObj.init((UploadDocumentDTO)itemId,referenceData);
						popup.setContent(billEntryTableObj);
						popup.setClosable(false);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}
			    		//UI.getCurrent().addWindow(viewDetailUI); 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	button.setWidth("150px");
		    	return button;
		      }
		});
		
		table.addGeneratedColumn("status", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				
				CheckBox chkBox = new CheckBox();
				
				return chkBox;
			}
		});

		//table.setPageLength(table.getItemIds().size());
	}

	@Override
	public String textBundlePrefixString() {
		return "bill-entry-uploaded-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",Upload.class, String.class, false));
		fieldMap.put("fileType", new TableFieldDTO("fileType",ComboBox.class, SelectValue.class, true));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	public void setReference(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		super.setReference(referenceData, this);
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void setupCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		
		billEntryTableObj.setUpCategoryValues(categoryValues);
		
	}

	
}

