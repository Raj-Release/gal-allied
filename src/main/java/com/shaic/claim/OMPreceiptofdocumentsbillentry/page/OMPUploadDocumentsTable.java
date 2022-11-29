/**
 * 
 */
package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;

/**
 * @author ntv.vijayar
 *
 */
public class OMPUploadDocumentsTable extends GEditableTable<UploadDocumentDTO> {
	
	//private List<String> errorMessages;
	
	
	public OMPUploadDocumentsTable() {
		super(OMPUploadDocumentsTable.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"fileUpload", "fileType", "billNo", "billDate", "noOfItems", "billValue" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		
		//The file upload mapping will be changed once the upload component works.
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",Upload.class, String.class, true));
		fieldMap.put("fileType", new TableFieldDTO("fileType",ComboBox.class, SelectValue.class, true));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, true,20));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, true,10));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, true,6));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, true,6));
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
			"fileUpload", "fileType", "billNo", "billDate", "noOfItems", "billValue" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		
		
		//table.setPageLength(table.getItemIds().size());
	}

	@Override
	public String textBundlePrefixString() {
		return "bill-entry-upload-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();		
			//The file upload mapping will be changed once the upload component works.
			fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",Upload.class, String.class, true));
			fieldMap.put("fileType", new TableFieldDTO("fileType",ComboBox.class, SelectValue.class, true));
			fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, true,20));
			fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, true,10));
			fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, true,6));
			fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, true,6));
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
	
	public void clearTableData()
	{
		table.removeAllItems();
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
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
}
