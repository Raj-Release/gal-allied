package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchAcknowledgementDocumentReceiverView extends Searchable  {
	public void list(Page<SearchAcknowledgementDocumentReceiverTableDTO> tableRows);
}
