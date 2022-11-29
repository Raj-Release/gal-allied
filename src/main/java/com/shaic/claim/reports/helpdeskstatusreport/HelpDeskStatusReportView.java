package com.shaic.claim.reports.helpdeskstatusreport;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface HelpDeskStatusReportView extends Searchable {
	public void list(Page<HelpDeskStatusReportTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> claimType);
	public void generateReport();
	public void resetSearchValue();

}
