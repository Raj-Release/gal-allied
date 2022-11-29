package com.shaic.claim.pcc.views;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.ViewPCCTrailsDTO;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPCCTrailsTable extends GBaseTable<ViewPCCTrailsDTO>{
	

	
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"dateAndTime", "userID","userName","raiseRole","pccRemarksType", "status", "statusRemark" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewPCCTrailsDTO>(ViewPCCTrailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(ViewPCCTrailsDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "pcc-trails-";
	}



}
