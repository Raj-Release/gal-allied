package com.shaic.claim.reports.paymentpendingdashboard;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface PaymentPendingDashboardView extends Searchable{
	
	public void list(Page<PaymentPendingDashboardTableDTO> tableRows);

}
