package com.shaic.claim.submitSpecialist;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.TextArea;
import com.shaic.arch.table.GInlineTable;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.TextField;

public class SubmitSpecialistPageTable extends GInlineTable<SubmitSpecialistDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubmitSpecialistPageTable() {
		super(SubmitSpecialistPageTable.class);
		setUp();
	}
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "requestorRemarks",
		"requestedDate", "viewFile", "specialistRemarks","fileUpload" };*/
	
	
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	{
		fieldMap.put("requestorRemarks", new TableFieldDTO("requestorRemarks", TextArea.class,
				String.class, false));
		fieldMap.put("requestedDate", new TableFieldDTO(
				"requestedDate", TextArea.class, String.class, false));
		fieldMap.put("viewFile", new TableFieldDTO("viewFile", TextField.class,
				String.class, false));
		fieldMap.put("specialistRemarks", new TableFieldDTO(
				"specialistRemarks", TextArea.class, String.class, true));
		fieldMap.put("fileUpload", new TableFieldDTO(
				"fileUpload", TextField.class, String.class, true));
	}*/

	@Override
	protected void newRowAdded() {
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		
			fieldMap.put("requestorRemarks", new TableFieldDTO("requestorRemarks", TextArea.class,
					String.class, false));
			fieldMap.put("requestedDate", new TableFieldDTO(
					"requestedDate", TextArea.class, String.class, false));
			fieldMap.put("viewFile", new TableFieldDTO("viewFile", TextField.class,
					String.class, false));
			fieldMap.put("specialistRemarks", new TableFieldDTO(
					"specialistRemarks", TextArea.class, String.class, true));
			fieldMap.put("fileUpload", new TableFieldDTO(
					"fileUpload", TextField.class, String.class, true));
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

		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<SubmitSpecialistDTO>(
				SubmitSpecialistDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] { "requestorRemarks",
			"requestedDate", "viewFile", "specialistRemarks","fileUpload" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		
	}

	@Override
	public void tableSelectHandler(SubmitSpecialistDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "upload-translated-documents-";
		
	}

}
