package com.shaic.claim.reports.notAdheringToANHReport;

import java.util.List;
import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchNotAdheringToANHReportView extends GMVPView {
	public void showNotAdheringToANHcReport();
	public void setCPUContainerToForm(BeanItemContainer<SelectValue> cpuContainer);
	public void searchNotAdheringToANHReport(List<NewIntimationNotAdheringToANHDto> claimList);

}
