package com.shaic.reimbursement.paymentprocess.processpaymentreturn.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessPaymentReturnsView extends Searchable  {
	public void list(Page<SearchProcessPaymentReturnsTableDTO> tableRows);
}
