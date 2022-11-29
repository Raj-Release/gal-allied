package com.shaic.claim.medical.opinion;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewMarkEscHistoryTable extends GBaseTable<ViewMarkEscHistoryDTO>{
	

	
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"sNo", "intimationNumber","dateAndTime","userName","escalatedRole","escalatedBy", "escalatedDate", "escalationReason","actionTaken","lackOfmrkPersonnel","doctorRemarks" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewMarkEscHistoryDTO>(ViewMarkEscHistoryDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(ViewMarkEscHistoryDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "mark-esc-history-";
	}

}
