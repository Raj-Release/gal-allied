package com.shaic.claim.fss.filedetailsreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class FileDetailsReportTable extends GBaseTable<FileDetailsReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","clientName","storagelocation","rackDesc","shelfDesc","claimNumber","patientName","year",
		"almirahNo","bundleNo","fileStatus"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<FileDetailsReportTableDTO>(FileDetailsReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
	}
	@Override
	public void tableSelectHandler(FileDetailsReportTableDTO t) {
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "filedetailsreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
