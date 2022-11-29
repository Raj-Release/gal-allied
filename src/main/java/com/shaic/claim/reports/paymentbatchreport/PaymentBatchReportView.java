package com.shaic.claim.reports.paymentbatchreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface PaymentBatchReportView extends Searchable{

	public void list(Page<PaymentBatchReportTableDTO> tableRows);
	public void generateReport();

}
