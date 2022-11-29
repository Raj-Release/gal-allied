package com.shaic.claim.reimbursement.rawanalysis;

import com.shaic.arch.table.GBaseTable;

import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchProcessRawRequestTable extends GBaseTable<SearchProcessRawRequestTableDto>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","policyNo","insuredPatientName","hospitalName",
		"hospitalCity","classOfBusiness","cpuCode","productName","claimType","requestDate","category"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchProcessRawRequestTableDto>(SearchProcessRawRequestTableDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300px");
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(SearchProcessRawRequestTableDto t) {
		fireViewEvent(MenuPresenter.SHOW_PROCESS_RAW_REQUEST_WIZARD, t);
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-process-raw-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
