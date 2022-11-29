package com.shaic.claim.lumen.components;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.LumenQueryDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

@SuppressWarnings("serial")
public class QueryDetails extends GBaseTable<LumenQueryDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"query","queryRaisedRole","queryRaisedBy","queryRaisedDate"};

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {		

		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);
		table.setContainerDataSource(new BeanItemContainer<LumenQueryDTO>(LumenQueryDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(3);
	}

	@Override
	public void tableSelectHandler(LumenQueryDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-query-reply-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=3){
			table.setPageLength(3);
		}
	}

}
