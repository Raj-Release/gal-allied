package com.shaic.claim.intimation.uprSearch;

import java.util.List;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTableDTO;
import com.vaadin.ui.Button.ClickEvent;

public interface SearchIntimationUPRDetailView extends Searchable {

	public void showIntimationDetailsView(ClickEvent event);
	public void showSearchViewDetailIntimationTable(SearchIntimationFormDto searchDto);
	public void resetSearchIntimationView();
	public void showActionClickView(ViewClaimStatusDTO intimationDetails);
	public void showSearchResultViewDetail(Page<PaymentProcessCpuPageDTO> newIntimationDtoPagedContainer);
	public void setPaymentTrials(List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList);
	public void setPaymentCancelDetails(List<ClmPaymentCancelDto> chqCancelListDto,
			List<ClmPaymentCancelDto> neftCancelListDto);
	public void setSettlementDetails(PaymentProcessCpuPageDTO paymentDto);
	public void showTrackingTrails(
			List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList);
}
