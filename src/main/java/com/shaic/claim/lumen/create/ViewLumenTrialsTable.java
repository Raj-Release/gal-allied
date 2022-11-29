package com.shaic.claim.lumen.create;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.LumenTrialsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

@SuppressWarnings("serial")
public class ViewLumenTrialsTable extends GBaseTable<LumenTrialsDTO> {
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"initiatedScreen","initiatedBy", "initiatedDate", "response", "comments"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<LumenTrialsDTO>(LumenTrialsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnAlignments(Table.Align.CENTER,Table.Align.CENTER,Table.Align.CENTER,Table.Align.CENTER,Table.Align.CENTER);
	}

	@Override
	public void tableSelectHandler(LumenTrialsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-trails-";
	}

}
