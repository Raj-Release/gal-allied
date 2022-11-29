package com.shaic.claim.reports.claimstatusreportnew;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ClaimsStatusReportView extends GMVPView {
	
	public void showSearchClaimsStatusReport();
	public void setupDroDownValues(BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> empContainer);
	public void searchClaimsStatusReport(List<ClaimsStatusReportDto> claimList);

}
