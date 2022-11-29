package com.shaic.claim.registration;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table.Align;

@SuppressWarnings({"serial","deprecation"})
public class IntimationSourceDetailTable extends GBaseTable<Preauth> {

	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<Preauth>(Preauth.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"preauthId","source"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("preauthId", 370);
		table.setColumnWidth("source", 315);
		table.setColumnAlignment("preauthId", Align.CENTER);
		table.setColumnAlignment("source", Align.CENTER);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(Preauth t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "view-intimation-source-details-";
	}

}