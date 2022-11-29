package com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel;

import com.shaic.arch.GMVPView;

public interface PACloseClaimView extends GMVPView {

	void init(PACloseClaimPageDTO tableDTO);

	void setUploadDocumentsDTO(
			PAUploadDocumentCloseClaimDTO uploadDocumentForCloseClaim);

	void result();

	void alreadyClosed(String message);

}
