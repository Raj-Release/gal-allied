


/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

/**
 * 
 *
 */
public class SearchDataEntryTable extends GBaseTable<SearchDataEntryTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","claimNo", "patientName", "location","rackNo", "shelfNo","checkInOutStatus"};
	
	//private Map<String, Component> compMap = new HashMap<String, Component>();
	private List<Component> compList = new ArrayList<Component>();
	public HashMap<String,Component> compMap = null;
	private List<SearchDataEntryTableDTO>  tableRows = null;
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	public void setTableValuelist(List<SearchDataEntryTableDTO>  tableRows)
	{
		this.tableRows = tableRows;
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchDataEntryTableDTO>(SearchDataEntryTableDTO.class));
		table.setPageLength(3);
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("245px");
		
		
	}
	
	


/*	public void invokeView(SearchDataEntryTableDTO t) {
//		fireViewEvent(MenuPresenter, t,this);
		fireViewEvent(MenuPresenter.SHOW_SEARCH_RRC_REQUEST_VIEW, t,this);
	}*/

	@Override
	public void tableSelectHandler(
			SearchDataEntryTableDTO t) {
		String userName = (String) getUI().getSession().getAttribute(
				BPMClientContext.USERID);
		if (userName != null) {
			if(t != null){
				t.setUsername(userName);	
			}
		}
	fireViewEvent(MenuPresenter.SHOW_DATA_ENTRY_DETAILS, t,this);
	
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-data-entry-form-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	
	public List<SearchDataEntryTableDTO> getTableAllItems()
	{
		return (List<SearchDataEntryTableDTO>)table.getItemIds();
	}
	
	
}



