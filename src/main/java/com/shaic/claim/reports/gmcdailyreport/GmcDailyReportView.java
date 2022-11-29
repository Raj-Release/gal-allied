package com.shaic.claim.reports.gmcdailyreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface GmcDailyReportView extends GMVPView {
	
	public void setupDroDownValues(BeanItemContainer<SelectValue> cPUCodeContainer,
	BeanItemContainer<SelectValue> clmTypeContainer);
	public void showSearchClaimsDailyReport();
	public void searchClaimsDailyReport(List<GmcDailyReportDto> claimList);

}
