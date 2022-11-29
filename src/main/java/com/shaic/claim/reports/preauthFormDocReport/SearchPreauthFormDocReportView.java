package com.shaic.claim.reports.preauthFormDocReport;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchPreauthFormDocReportView extends GMVPView {
	
	public void showSearchPreauthFormDocReport();
	public void setCPUContainerToForm(BeanItemContainer<SelectValue> cpuContainer);
	public void searchPreauthFormDocReport(List<NewIntimationDto> claimList);

}
