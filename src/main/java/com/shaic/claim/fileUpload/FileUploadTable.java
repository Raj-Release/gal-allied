package com.shaic.claim.fileUpload;

import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

public class FileUploadTable extends GEditableTable<FileUploadDTO> {

	public FileUploadTable() {
		super(FileUploadDTO.class);
		setUp();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"slNo",
		"requestType", "requestorMarks", "fileUpload",
		"remarks"};
*/	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("slNo", new TableFieldDTO("slNo",
				TextField.class, String.class, false));
		fieldMap.put("requestType", new TableFieldDTO("requestType",
				TextField.class, String.class, false));
		fieldMap.put("requestorMarks", new TableFieldDTO("requestorMarks",
				TextField.class, String.class, false));
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",
				TextField.class, String.class, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextArea.class, String.class, true));
	}*/
	
	@Override
	protected void newRowAdded() {
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		fieldMap.put("slNo", new TableFieldDTO("slNo",
				TextField.class, String.class, false));
		fieldMap.put("requestType", new TableFieldDTO("requestType",
				TextField.class, String.class, false));
		fieldMap.put("requestorMarks", new TableFieldDTO("requestorMarks",
				TextField.class, String.class, false));
		fieldMap.put("fileUpload", new TableFieldDTO("fileUpload",
				TextField.class, String.class, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextArea.class, String.class, true));
		return fieldMap;
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
		table.setContainerDataSource(new BeanItemContainer<FileUploadDTO>(
				FileUploadDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"slNo",
			"requestType", "requestorMarks", "fileUpload",
			"remarks"};

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("150px");
		table.setWidth("950px");
		table.setColumnWidth("remarks", 400);
		table.setColumnWidth("requestorMarks", 200);
	}

	@Override
	public void tableSelectHandler(FileUploadDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "file-upload-translated-table-";
	}

}
