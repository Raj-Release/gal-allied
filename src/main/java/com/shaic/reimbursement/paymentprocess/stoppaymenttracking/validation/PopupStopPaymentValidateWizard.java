package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;

public interface PopupStopPaymentValidateWizard extends GMVPView{
	public void initView(StopPaymentRequestDto searchResult);
	public void cancelStopPaymentValidate();
	public void submitStopPaymentValidate();
	public void showTrackingTrails(List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList);

}
