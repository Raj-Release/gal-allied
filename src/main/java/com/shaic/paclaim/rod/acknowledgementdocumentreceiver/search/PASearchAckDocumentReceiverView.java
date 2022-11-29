package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverTableDTO;

public interface PASearchAckDocumentReceiverView extends Searchable{

	public void list(Page<SearchAcknowledgementDocumentReceiverTableDTO> tableRows);
}
