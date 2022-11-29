package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class UploadDocumentsForAckNotReceivedTable extends GBaseTable<UploadDocumentsForAckNotReceivedTableDTO>{

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo","policyNo","healthCardIdNo",
		"insuredPatientName", "cpuCode", "claimType", "hospitalName",
		"hospitalCity", "dateOfAdimissionValue", "reasonForAdmission"
		};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentsForAckNotReceivedTableDTO>(UploadDocumentsForAckNotReceivedTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		
	}
	@Override
	public void tableSelectHandler(UploadDocumentsForAckNotReceivedTableDTO t) {
		fireViewEvent(MenuPresenter.SEARCH_OR_UPLOAD_DOCUMENTS_ACK_NOT_RECEIVED_WIZARD, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "search_or_upload_documents-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
