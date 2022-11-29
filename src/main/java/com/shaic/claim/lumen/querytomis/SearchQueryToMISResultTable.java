package com.shaic.claim.lumen.querytomis;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class SearchQueryToMISResultTable extends GBaseTable<LumenRequestDTO>{
	
	@Inject
	private LumenDbService lumenService;
	
	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","intimationNumber","cpuDesc",
		"policyNumber","productName","insuredPatientName","claimType","initiatedScreen","misQueryRaisedFrom", "initiatedBy","initiatedDate"
	}; 

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<LumenRequestDTO>(LumenRequestDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(7);
	}

	@Override
	public void tableSelectHandler(LumenRequestDTO t) {
		LumenRequestDTO fullDataObj = lumenService.getLumenDataFromTable(t);
		fireViewEvent(MenuItemBean.QUERY_MIS_WIZARD, fullDataObj);
	}

	@Override
	public String textBundlePrefixString() {
		return "search-lumen-request-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
