package com.shaic.claim.misc.updatesublimit;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchUpdateSublimitTable extends GBaseTable<SearchUpdateSublimitTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimedAmtAsPerBill", "strCpuCode","cpuName","claimType","documentRecvdFrm","productName",
		"insuredPatientName", "lob",  "hospitalName",  "hospitalAddress", "reasonForAdmission", "type"}; 

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchUpdateSublimitTableDTO>(SearchUpdateSublimitTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("hospitalAddress", 300);
		table.setHeight("290px");
	}

	@Override
	public void tableSelectHandler(SearchUpdateSublimitTableDTO tableDTO) {
		fireViewEvent(MenuPresenter.UPDATE_SUBLIMIT, tableDTO);
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-update-sublimit-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
