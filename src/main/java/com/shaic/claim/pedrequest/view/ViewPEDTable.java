package com.shaic.claim.pedrequest.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class ViewPEDTable extends GEditableTable<ViewPEDTableDTO> implements
		TableCellSelectionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewPEDTable() {
		super(ViewPEDTable.class);
		setUp();
	}

/*	public static final Object[] VISIBLE_COLUMNS = new Object[] { "pedCode",
			"description", "icdChapter", "icdBlock", "icdCode", "source",
			"othersSpecify", "doctorRemarks" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	{
		fieldMap.put("pedCode", new TableFieldDTO("pedCode", ComboBox.class,
				SelectValue.class, true, this));
		fieldMap.put("description", new TableFieldDTO("description",
				TextField.class, String.class, true, this));
		fieldMap.put("icdChapter", new TableFieldDTO("icdChapter",
				ComboBox.class, SelectValue.class, true, this));
		fieldMap.put("icdBlock", new TableFieldDTO("icdBlock", ComboBox.class,
				SelectValue.class, true, this));
		fieldMap.put("icdCode", new TableFieldDTO("icdCode", ComboBox.class,
				SelectValue.class, true, this));
		fieldMap.put("source", new TableFieldDTO("source", ComboBox.class,
				SelectValue.class, true, this));
		fieldMap.put("othersSpecify", new TableFieldDTO("othersSpecify",
				TextField.class, String.class, false, this));
		fieldMap.put("doctorRemarks", new TableFieldDTO("doctorRemarks",
				TextField.class, SelectValue.class, true, this));
	}*/

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		System.out.println("=========================== " + field.getId()
				+ " :" + event.getProperty().getValue());
	}

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
			fieldMap.put("pedCode", new TableFieldDTO("pedCode", ComboBox.class,
					SelectValue.class, true, this));
			fieldMap.put("description", new TableFieldDTO("description",
					TextField.class, String.class, true, this));
			fieldMap.put("icdChapter", new TableFieldDTO("icdChapter",
					ComboBox.class, SelectValue.class, true, this));
			fieldMap.put("icdBlock", new TableFieldDTO("icdBlock", ComboBox.class,
					SelectValue.class, true, this));
			fieldMap.put("icdCode", new TableFieldDTO("icdCode", ComboBox.class,
					SelectValue.class, true, this));
			fieldMap.put("source", new TableFieldDTO("source", ComboBox.class,
					SelectValue.class, true, this));
			fieldMap.put("othersSpecify", new TableFieldDTO("othersSpecify",
					TextField.class, String.class, false, this));
			fieldMap.put("doctorRemarks", new TableFieldDTO("doctorRemarks",
					TextField.class, SelectValue.class, true, this));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewPEDTableDTO>(
				ViewPEDTableDTO.class));
	 Object[] VISIBLE_COLUMNS = new Object[] { "pedCode",
			"description", "icdChapter", "icdBlock", "icdCode", "source",
			"othersSpecify", "doctorRemarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.size());

	}

	@Override
	public void tableSelectHandler(ViewPEDTableDTO t) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unused")
	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	public void setTableList(List<PEDQueryDTO> tableRows) {
		//TODO:
	}

	@Override
	public String textBundlePrefixString() {		
		return "view-ped-request-details-";
	}

}
