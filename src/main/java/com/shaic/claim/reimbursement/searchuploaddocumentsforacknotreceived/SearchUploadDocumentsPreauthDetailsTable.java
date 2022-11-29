package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchUploadDocumentsPreauthDetailsTable extends GBaseTable<UploadDocumentsForAckNotReceivedPageTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","referenceNumber","referenceType","documentReceivedFrom","documentReceivedDate","treatmentType","treatmentRemarks","requestedAmt","status"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentsForAckNotReceivedPageTableDTO>(UploadDocumentsForAckNotReceivedPageTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("250px");
		
	}
	@Override
	public void tableSelectHandler(UploadDocumentsForAckNotReceivedPageTableDTO t) {
	
	}
		
	
	@Override
	public String textBundlePrefixString() {
		return "searchuploadpreauthdetails-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	


}
