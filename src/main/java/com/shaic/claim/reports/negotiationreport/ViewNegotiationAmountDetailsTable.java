package com.shaic.claim.reports.negotiationreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewNegotiationAmountDetailsTable extends GBaseTable<NegotiationAmountDetailsDTO>{
	
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"intimationNo","stage" ,"status","hstCLTrans","claimedAmt","negotiationWith","negotiatedAmt","claimAppAmt","savedAmt","totalNegotiationSaved"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<NegotiationAmountDetailsDTO>(NegotiationAmountDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(NegotiationAmountDetailsDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-negotiationamountdetails-";
	}

}
