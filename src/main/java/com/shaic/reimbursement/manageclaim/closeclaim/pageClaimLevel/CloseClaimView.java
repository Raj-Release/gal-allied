package com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel;

import com.shaic.arch.GMVPView;

public interface CloseClaimView extends GMVPView {

	void init(CloseClaimPageDTO tableDTO);

	void setUploadDocumentsDTO(
			UploadDocumentCloseClaimDTO uploadDocumentForCloseClaim);

	void result();

	void alreadyClosed(String message);

}
