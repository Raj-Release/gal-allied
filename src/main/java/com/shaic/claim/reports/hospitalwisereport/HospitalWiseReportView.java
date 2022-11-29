package com.shaic.claim.reports.hospitalwisereport;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface HospitalWiseReportView  extends Searchable{
	public void list(Page<HospitalWiseReportTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> dateType);

	public void generateReport();

}
