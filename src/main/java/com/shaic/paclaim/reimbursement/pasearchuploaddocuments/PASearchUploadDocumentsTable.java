package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsTableDTO;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PASearchUploadDocumentsTable extends GBaseTable<SearchUploadDocumentsTableDTO>{


	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo", "claimNo", "policyNo","acknowledgementNo","docReceivedType",
		"insuredPatientName", "cpuCode", "claimType", "hospitalName",
		"hospitalCity", "dateOfAdimissionValue", "reasonForAdmission"
		};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchUploadDocumentsTableDTO>(SearchUploadDocumentsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		
	}
	@Override
	public void tableSelectHandler(SearchUploadDocumentsTableDTO t) {
		fireViewEvent(PAMenuPresenter.PA_SEARCH_OR_UPLOAD_DOCUMENTS_WIZARD, t);
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
