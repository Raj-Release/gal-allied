/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryUploadDocumentsPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.UploadDocumentsPresenter;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocUploadDocumentsPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillentryUploadDocumentsPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class UploadedDocumentsForBillEntry extends GEditableTable<UploadDocumentDTO> {
	
	//private List<String> errorMessages;
	
	private String presenterString;
	
	public UploadedDocumentsForBillEntry() {
		super(UploadedDocumentsForBillEntry.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/

	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	//Have a map for storing container value

	static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
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
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}

	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.removeGeneratedColumn("edit");
		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("Edit");
		    	editButton.setData(itemId);
		    	
		    	UploadDocumentDTO uploadDTOForEdit = (UploadDocumentDTO) itemId;
		    /*	if(null != uploadDTOForEdit && null != uploadDTOForEdit.getFileTypeValue() && ("Cashless Settlement Bill").equalsIgnoreCase(uploadDTOForEdit.getFileTypeValue()))
		    	{
		    		editButton.setEnabled(false);
		    	}
		    	else
		    	{
		    		editButton.setEnabled(true);
		    	}*/
		    	
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;

						uploadDTO.setIsEdit(true);

						if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
							fireViewEvent(BillEntryUploadDocumentsPresenter.BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
						else if (SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
							fireViewEvent(UploadDocumentsPresenter.BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
						else if (SHAConstants.PA_ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
							fireViewEvent(PAAddAdditionalDocUploadDocumentsPresenter.ADD_ADDITIONAL_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
						else if (SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
							fireViewEvent(PABillentryUploadDocumentsPresenter.BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
						

						
			        	/*Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);*/
			        } 
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
		
		table.removeGeneratedColumn("delete");
		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
						uploadDocsDTO.setIsEdit(false);
						//BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) currentItemId;
						if(null != uploadDocsDTO.getDocSummaryKey())
						{
							//billEntryDetailsDTO.setDeletedFlag("Y");
							deletedList.add(uploadDocsDTO);
						}
						table.removeItem(currentItemId);
						tableSelectHandler(uploadDocsDTO);
						
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		//table.setPageLength(table.getItemIds().size());
	}
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
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
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
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
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	/*protected void addProcedureNameChangeListener()
	{
		Item item = table.getItem("procedureNameValue");
		ComboBox cmb = (ComboBox)item.
		
		item.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != procedureTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
			}
			
			
		});
		
	}*/
	/*public boolean isValidProcedure()
	{
		boolean hasError = false;
		errorMessages.removeAll(getProcedureErrors());
		Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
		for (ProcedureDTO bean : itemIds) {
			if(bean.getProcedureNameValue() == null || (null != bean.getProcedureNameValue() && bean.getProcedureNameValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Name");
			}
			
			if(bean.getProcedureCodeValue() == null || (null != bean.getProcedureCodeValue() && bean.getProcedureCodeValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Code");
			}
		}
		return !hasError;
	}*/
	
/*
	public List<String> getProcedureErrors() {
		return this.errorMessages;
	}
*/
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString)){
			fireViewEvent(BillEntryUploadDocumentsPresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS,t);
		}else if(SHAConstants.PA_ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString)){
			fireViewEvent(PAAddAdditionalDocUploadDocumentsPresenter.ADD_ADDITIONAL_DELETE_UPLOADED_DOCUMENTS,t);
		}else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString)){
			fireViewEvent(PABillentryUploadDocumentsPresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS,t);
		}
		else{
			fireViewEvent(UploadDocumentsPresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS,t);
		}
		
		
	}
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}
	
}


