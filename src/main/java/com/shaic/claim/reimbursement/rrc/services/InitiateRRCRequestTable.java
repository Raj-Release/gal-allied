package com.shaic.claim.reimbursement.rrc.services;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;

public class InitiateRRCRequestTable extends GBaseTable<InitiateRRCRequestTableDTO>{

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","claimNo","intimationNo","policyNo","insuredPatientName","cpuCode","hospitalName","hospitalAddress",
		"hospitalCity","productName",};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<InitiateRRCRequestTableDTO>(InitiateRRCRequestTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		
	}
	@Override
	public void tableSelectHandler(InitiateRRCRequestTableDTO t) {
		fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST_WIZARD, t);
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "initiate-rrc-request-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
