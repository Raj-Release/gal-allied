package com.shaic.claim.policy.search.ui;

import java.util.List;

public class BancsInstallment64VBStatusResponse {


	private String errorCode;
	private String errorDescription;
	private List<BancsInstallmentDetail> installmentDetails = null;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public List<BancsInstallmentDetail> getInstallmentDetails() {
		return installmentDetails;
	}

	public void setInstallmentDetails(List<BancsInstallmentDetail> installmentDetails) {
		this.installmentDetails = installmentDetails;
	}


}
