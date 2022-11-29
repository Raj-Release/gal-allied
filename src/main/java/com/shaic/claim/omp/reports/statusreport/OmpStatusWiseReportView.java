package com.shaic.claim.omp.reports.statusreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.omp.reports.outstandingreport.OmpStatusReportDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OmpStatusWiseReportView extends GMVPView {
	
	public void showOmpStatusWiseReport(List<OmpStatusReportDto> ompStatusWiseList);
	public void resetSearchView();
}
