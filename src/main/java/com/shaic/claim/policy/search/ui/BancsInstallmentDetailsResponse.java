package com.shaic.claim.policy.search.ui;

import java.util.List;

public class BancsInstallmentDetailsResponse {

	private String errorCode;
	private String errorDescription;
	private List<InstallmentDetailDTO> installmentDetails = null;

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

	public List<InstallmentDetailDTO> getInstallmentDetails() {
		return installmentDetails;
	}

	public void setInstallmentDetails(List<InstallmentDetailDTO> installmentDetails) {
		this.installmentDetails = installmentDetails;
	}



}
