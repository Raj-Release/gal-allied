package com.shaic.claim.reports;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface SearchClaimPolicyReportView extends GMVPView {
	
	public void showSearchClaimPolicyReport();
	public void searchClaiPolicymwise(List<PolicywiseClaimReportDto> claimList);

}
