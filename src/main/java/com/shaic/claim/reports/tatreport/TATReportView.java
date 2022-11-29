package com.shaic.claim.reports.tatreport;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface TATReportView extends Searchable{

	public void list(Page<TATReportTableDTO> tableRows);
	
	/*public void setDropDownValues(BeanItemContainer<SelectValue> dateType,
			BeanItemContainer<SelectValue> claimsQueueType,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> officeCode, 
			BeanItemContainer<SelectValue> tatDate);*/
	

	void init(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> officeCode);
	void setOfficeCodeDropDown(BeanItemContainer<SelectValue> officeCode);
	
	public void generateReport();
}
