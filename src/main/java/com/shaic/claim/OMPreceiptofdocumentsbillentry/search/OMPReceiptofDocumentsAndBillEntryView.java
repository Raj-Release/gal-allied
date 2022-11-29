package com.shaic.claim.OMPreceiptofdocumentsbillentry.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface OMPReceiptofDocumentsAndBillEntryView extends Searchable {
	
	public void list(Page<OMPReceiptofDocumentsAndBillEntryTableDTO> tableRows);
	public void init();

}
