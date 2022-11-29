package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface PaymentReprocessSearchView extends Searchable, GMVPView	{

	public void renderTable(Page<PaymentReprocessSearchResultDTO> tableRows);
	public void buildSuccessLayout();
	public void buildCancelLayout();
	void showClaimsDMS(String url);
}
