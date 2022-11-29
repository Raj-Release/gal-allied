package com.shaic.claim.lumen.components;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

@SuppressWarnings("serial")
public class MISSubDetails extends GBaseTable<MISSubDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"queryRaisedBy","queryRaisedDate","queryRemarks","replyRemarks"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {

		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);
		table.setContainerDataSource(new BeanItemContainer<MISSubDTO>(MISSubDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(5);
	}

	@Override
	public void tableSelectHandler(MISSubDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "mis-sub-query-reply-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
	}

}
