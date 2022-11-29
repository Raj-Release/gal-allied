

/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODUploadDocumentsPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class AlreadyUploadedDocumentTable extends GEditableTable<UploadDocumentDTO> {
	
	private static final long serialVersionUID = -5524600977147253320L;
	private String presenterString = "";
	//private Window popup;
	final Embedded imageViewer = new Embedded("Uploaded Image");

	public AlreadyUploadedDocumentTable() {
		super(AlreadyUploadedDocumentTable.class);
		setUp();
	}
	
	
/*	public static final Object[] VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
	}*/

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void setPreseneterString(String presenterString) {
		this.presenterString = presenterString;
	}
	
	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		
		generateColumns();
		
		//table.setPageLength(table.getItemIds().size());
	}

	private void generateColumns() {
		table.removeGeneratedColumn("Verify");
		table.addGeneratedColumn("Verify", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				final UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
				

				//final Button deleteButton = new Button("Delete");

				final Button attachBtn = new Button("Verify");
				attachBtn.setData(itemId);

				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				
				attachBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				attachBtn.setWidth("-1px");
				attachBtn.setHeight("-10px");
				attachBtn.setData(itemId);
				attachBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						uploadDTO.setIsAlreadyUploaded(true);
						table.removeItem(currentItemId);
						fireViewEvent(CreateRODUploadDocumentsPresenter.ALREADY_UPLOADED_DOCUMENTS,uploadDTO);
						if(SHAConstants.PA_CREATE_ROD.equals(presenterString))
						{
							fireViewEvent(PACreateRODUploadDocumentsPresenter.ALREADY_UPLOADED_DOCUMENTS,uploadDTO);
						}
					}
				});

				return attachBtn;
			}
		});
		
	}

	@Override
	public String textBundlePrefixString() {
		return "rod-alreadyuploaded-documents-";
	}

	
	public void removeItem(UploadDocumentDTO uploadDTO)
	{
		if(null != uploadDTO)
			table.removeItem(uploadDTO);
	}
	
	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
			fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
			fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
			fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
			fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
		return fieldMap;
	}

	public void validateFields() {
		/*Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}*/

	}

	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {/*
		if(SHAConstants.OUTPATIENT_FLAG.equalsIgnoreCase(presenterString)) {
			fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS, t);
		} else {
			fireViewEvent(CreateRODUploadDocumentsPresenter.DELETE_UPLOADED_DOCUMENTS,t);
			
		}
		
	*/}
	
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}
	/*
	public void addBeanToList(UploadDocumentDTO dto) {
    	//container.addBean(uploadDocumentsDTO);
	 container.addItem(dto);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
    }*/
}


