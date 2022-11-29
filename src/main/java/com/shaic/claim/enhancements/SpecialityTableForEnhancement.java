package com.shaic.claim.enhancements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class SpecialityTableForEnhancement extends GEditableTable<SpecialityDTO> {
	
	private static final long serialVersionUID = -6874940111860437242L;

	public SpecialityTableForEnhancement() {
		super(SpecialityTableForEnhancement.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"specialityType", "remarks" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		// fieldMap.put("key", new TableFieldDTO("key",TextField.class,
		// Long.class));
		// fieldMap.put("preAuthkey", new
		// TableFieldDTO("preAuthkey",TextField.class, Long.class));
		fieldMap.put("specialityType", new TableFieldDTO("specialityType",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
				String.class, true ));
	}*/

//	public void setTableList(List<SpecialityDTO> tableRows) {
//		List<SpecialityDTO> itemIds = (List<SpecialityDTO>) table.getItemIds();
//	}

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SpecialityDTO>(
				SpecialityDTO.class));
	 Object[] VISIBLE_COLUMNS = new Object[] {
			"specialityType", "remarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(SpecialityDTO t) {
	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-sepciality-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();	
			fieldMap.put("specialityType", new TableFieldDTO("specialityType",
					ComboBox.class, SelectValue.class, false));
			fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
					String.class, true ));
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
}
