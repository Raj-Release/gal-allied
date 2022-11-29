package com.shaic.gpaclaim.unnamedriskdetails;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchUnnamedRiskDetailsTable extends GBaseTable<SearchUnnamedRiskDetailsTableDTO>{
	

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","policyNo","productName","insuredName","section","category"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchUnnamedRiskDetailsTableDTO>(SearchUnnamedRiskDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("300px");
	}
	@Override
	public void tableSelectHandler(SearchUnnamedRiskDetailsTableDTO t) {
		fireViewEvent(MenuItemBean.GPA_UNNAMED_RISK_DETAILS_PAGE, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "unnamedriskdetails-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
		
	}
	



}
