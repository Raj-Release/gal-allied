package com.shaic.claim.reports.negotiationreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class NegotiationReportTable extends GBaseTable<ViewNegotiationDetailsDTO>{
	
	private static final Object[] COLUMN_HEADER = new Object[] {"intimationNo","cashlessorReimNo","negotiatedUpdateBy","cpucodeName","negotiatedAmt","savedAmt","claimApprovedAmt","intimationStage"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewNegotiationDetailsDTO>(ViewNegotiationDetailsDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
	}

	@Override
	public void tableSelectHandler(ViewNegotiationDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "negotiation-report-";
	}

}
