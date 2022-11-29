package com.shaic.claim.omp.reports.outstandingreport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OmpOutstandingReportView extends GMVPView {
	
	public void showOmpOutstandingReport(List<OmpStatusReportDto> lumenStatusWiseList);
//	public void setupDroDownValues(BeanItemContainer<SelectValue> cpuContainer, BeanItemContainer<SelectValue> lumenStatusContainer,BeanItemContainer<SelectValue> clmTypeContainer);
	public void resetSearchView();
}
