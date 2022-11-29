package com.shaic.claim.reports.callcenterDashBoard;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface CallcenterDashBoardReportView extends GMVPView {
	
	public void showSearchCallCenterDashBoard();
	public void searchCallCenterDashBoard(List<CallcenterDashBoardReportDto> claimList);
	public void hideSearchFields(String valChanged);
	public void resetSearchIntimationView();
	public void setAuditView(boolean auditView);
	
}
