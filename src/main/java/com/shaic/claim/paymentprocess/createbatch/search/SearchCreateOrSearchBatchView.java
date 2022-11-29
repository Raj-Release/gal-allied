package com.shaic.claim.paymentprocess.createbatch.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchCreateOrSearchBatchView extends Searchable  {
	public void list(Page<SearchCreateOrSearchBatchTableDTO> tableRows);
}
