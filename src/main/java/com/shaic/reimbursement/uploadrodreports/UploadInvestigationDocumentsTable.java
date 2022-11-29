package com.shaic.reimbursement.uploadrodreports;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Upload;

public class UploadInvestigationDocumentsTable extends
		GEditableTable<UploadInvestigationDocumentsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadInvestigationDocumentsTable() {
		super(UploadInvestigationDocumentsTable.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] { "fileUpload",
			"fileType" };
*/
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		// The file upload mapping will be changed once the upload component
		// works.
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",
				Upload.class, String.class, true));
		fieldMap.put("fileType", new TableFieldDTO("fileType", ComboBox.class,
				SelectValue.class, true));
	}*/

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		
			fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",
					Upload.class, String.class, true));
			fieldMap.put("fileType", new TableFieldDTO("fileType", ComboBox.class,
					SelectValue.class, true));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<UploadInvestigationDocumentsTableDTO>(
				UploadInvestigationDocumentsTableDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] { "fileUpload",
		"fileType" };

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setHeight("140px");
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(UploadInvestigationDocumentsTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "investigation-reports-upload-documents-";
	}

}
