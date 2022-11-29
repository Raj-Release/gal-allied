package com.shaic.claim.lumen.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class ReferToMISTableOld extends GEditableTable<MISDTO>{

	private static final long serialVersionUID = 9041177476010892414L;
	
	private List<MISDTO> deltedQueryMISList;

	public ReferToMISTableOld() {
		super(ReferToMISTableOld.class);
		setUp();
		deltedQueryMISList = new ArrayList<MISDTO>();
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void newRowAdded() {
		table.getContainerProperty("MISDTO", "sno").setValue(table.getContainerDataSource().size());
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		fieldMap.put("queryContent", new TableFieldDTO("queryContent", TextField.class, String.class, true,true,600,4000));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

	@Override
	public void removeRow() {

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<MISDTO>(MISDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber", "queryContent"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnCollapsingAllowed(false);

		table.setColumnExpandRatio("queryContent",80.0f);

		table.setWidth("70%");
		this.btnLayout.setWidth("70%");

		table.removeGeneratedColumn("serialNumber");
		table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				BeanItemContainer<MISDTO> container = (BeanItemContainer<MISDTO>)source.getContainerDataSource(); 
				return container.getItemIds().indexOf((MISDTO)itemId)+1;

			};
		});

		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button deleteButton = new Button("Delete");
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					public void buttonClick(ClickEvent event) {
						deleteRow(itemId);
					} 
				});
				return deleteButton;
			};
		});

		table.setPageLength(5);
	}

	public List<MISDTO> getDeltedQueryRedraftList() {
		return deltedQueryMISList;
	}


	@Override
	public void tableSelectHandler(MISDTO t) {

	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-refer-to-mis-details-";
	}

}
