package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public interface PopupStopPaymentRequestWizard extends GMVPView {
	
	public void initView(StopPaymentRequestDto searchResult);
	public void cancelStopPaymentRequest();
	public void submitStopPaymentRequest();
	
	public void editUploadDocumentDetails(UploadDocumentDTO uploadDTO);
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO);
	public void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO);
}
