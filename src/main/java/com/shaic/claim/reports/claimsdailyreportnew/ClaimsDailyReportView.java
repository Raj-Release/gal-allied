package com.shaic.claim.reports.claimsdailyreportnew;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ClaimsDailyReportView extends GMVPView {
	
	public void setupDroDownValues(BeanItemContainer<SelectValue> cPUCodeContainer,
	BeanItemContainer<SelectValue> clmTypeContainer);
	public void showSearchClaimsDailyReport();
	public void searchClaimsDailyReport(List<ClaimsDailyReportDto> claimList);

}
