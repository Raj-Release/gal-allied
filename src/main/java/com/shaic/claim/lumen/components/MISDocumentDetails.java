package com.shaic.claim.lumen.components;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

@SuppressWarnings("serial")
public class MISDocumentDetails extends GBaseTable<MISDocumentDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"queryRemarks","replyRemarks","uploadedFileType","uploadedFileName","uploadedDate","uploadedBy"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);	
		table.setContainerDataSource(new BeanItemContainer<MISDocumentDTO>(MISDocumentDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(5);
	}

	@Override
	public void tableSelectHandler(MISDocumentDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "mis-sub-document-details-";
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
