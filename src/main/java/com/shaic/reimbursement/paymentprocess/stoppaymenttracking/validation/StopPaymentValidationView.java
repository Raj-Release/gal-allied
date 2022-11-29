package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;

public interface StopPaymentValidationView extends Searchable, GMVPView {
	
	public void showSearchViewDetailIntimationTable(SearchIntimationFormDto searchDto);
	public void resetSearchIntimationView();
	public void showSearchResultViewDetail(Page<StopPaymentRequestDto> newIntimationDtoPagedContainer);

}
