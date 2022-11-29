package com.shaic.claim;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewNegotiationDetailTable extends GBaseTable<ViewNegotiationDetailsDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"intimationStage","negotiatedAmt","savedAmt","negotiatedUpdateBy","updateDate"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewNegotiationDetailsDTO>(ViewNegotiationDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(ViewNegotiationDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-negotiation-details-";
	}

}
