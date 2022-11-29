package com.shaic.claim.reports.opclaimreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OPClaimReportTable extends GBaseTable<OPClaimReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","noOfOpClaims","claimsOpAmount"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<OPClaimReportTableDTO>(OPClaimReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("480px");
	}
	@Override
	public void tableSelectHandler(OPClaimReportTableDTO t) {
		fireViewEvent(MenuPresenter.OP_CLAIM_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "opclaimreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
