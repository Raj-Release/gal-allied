package com.shaic.claim.reports.executivesummaryreqort;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ExecutiveStatusSummaryReportView extends GMVPView {
	
	public void showEmpwiseResultReport(List<ExecutiveStatusSummaryReportDto> empDetailsList);
	public void setupDroDownValues(BeanItemContainer<SelectValue> empCPUContainer, BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> empContainer);
	public void resetSearchView();
	public void populateFilteredEmpList(BeanItemContainer<SelectValue> empListContainer);
}
