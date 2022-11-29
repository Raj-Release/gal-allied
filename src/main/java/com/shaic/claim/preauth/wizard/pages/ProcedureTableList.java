package com.shaic.claim.preauth.wizard.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class ProcedureTableList extends GEditableTable<ProcedureDTO> {
	
	public ProcedureTableList() {
		super(ProcedureTableList.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"procedureName", "procedureCode", "packageRate","dayCareProcedure", "considerForDayCare", "sublimitApplicable","sublimitName","sublimitDesc","sublimitAmount", "considerForPayment", "remarks"  };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		// fieldMap.put("key", new TableFieldDTO("key",TextField.class,
		// Long.class));
		// fieldMap.put("preAuthkey", new
		// TableFieldDTO("preAuthkey",TextField.class, Long.class));
		fieldMap.put("procedureName", new TableFieldDTO("procedureName",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("procedureCode", new TableFieldDTO("procedureCode",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("packageRate", new TableFieldDTO("packageRate", TextField.class,
				String.class, false));
		fieldMap.put("dayCareProcedure", new TableFieldDTO("dayCareProcedure", TextField.class,
				String.class, false));
		fieldMap.put("considerForDayCare", new TableFieldDTO("considerForDayCare",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("sublimitApplicable", new TableFieldDTO("sublimitApplicable",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("sublimitName", new TableFieldDTO("sublimitName",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("sublimitDesc", new TableFieldDTO("sublimitDesc", TextField.class,
				String.class, false));
		fieldMap.put("sublimitAmount", new TableFieldDTO("sublimitAmount", TextField.class,
				String.class, false));
		fieldMap.put("considerForPayment", new TableFieldDTO("considerForPayment",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
				String.class, false));
	}*/

	public void setTableList(List<ProcedureTableDTO> tableRows) {
	}

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ProcedureDTO>(
				ProcedureDTO.class));
		table.setWidth("100%");
		Object[] VISIBLE_COLUMNS = new Object[] {
			"procedureName", "procedureCode", "packageRate","dayCareProcedure", "considerForDayCare", "sublimitApplicable","sublimitName","sublimitDesc","sublimitAmount", "considerForPayment", "remarks"  };
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	@Override
	public String textBundlePrefixString() {
		return "procedure-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

		
			fieldMap.put("procedureName", new TableFieldDTO("procedureName",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("procedureCode", new TableFieldDTO("procedureCode",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("packageRate", new TableFieldDTO("packageRate", TextField.class,
					String.class, false));
			fieldMap.put("dayCareProcedure", new TableFieldDTO("dayCareProcedure", TextField.class,
					String.class, false));
			fieldMap.put("considerForDayCare", new TableFieldDTO("considerForDayCare",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("sublimitApplicable", new TableFieldDTO("sublimitApplicable",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("sublimitName", new TableFieldDTO("sublimitName",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("sublimitDesc", new TableFieldDTO("sublimitDesc", TextField.class,
					String.class, false));
			fieldMap.put("sublimitAmount", new TableFieldDTO("sublimitAmount", TextField.class,
					String.class, false));
			fieldMap.put("considerForPayment", new TableFieldDTO("considerForPayment",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
					String.class, false));
	
		return fieldMap;
	}

	public void validateFields() {
		/*Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
			System.out.println(object);
		}*/
	}

	@Override
	public void tableSelectHandler(ProcedureDTO t) {
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	public void addListener() {
		/*ValueChangeListener procedureNameListner = new ValueChangeListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
			};
		};
		
		ValueChangeListener procedureCodeListner = new ValueChangeListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
			};
		};*/
}
}