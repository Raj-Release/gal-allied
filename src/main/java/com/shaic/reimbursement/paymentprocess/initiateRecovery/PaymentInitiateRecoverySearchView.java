package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface PaymentInitiateRecoverySearchView extends Searchable, GMVPView {
	
	public void resultantTable(Page<PaymentInitiateRecoveryTableDTO> tableRows);
	
	public void buildSuccessLayout();
	
	public void resetValues();

}
