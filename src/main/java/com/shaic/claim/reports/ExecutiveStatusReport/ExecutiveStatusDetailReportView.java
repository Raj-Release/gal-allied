package com.shaic.claim.reports.ExecutiveStatusReport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ExecutiveStatusDetailReportView extends GMVPView {
	
	public void showSearchEmpwiseReport(BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> empCPUSelectValueContainer,BeanItemContainer<SelectValue> empContainer);
	public void showEmpwiseResultReport(List<ExecutiveStatusDetailReportDto> empDetailsList);
	public void resetEmpResultView();
	public void populateFilteredEmpList(BeanItemContainer<SelectValue> empListContainer);
	

}
