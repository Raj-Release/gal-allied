package com.shaic.claim.OMPoutstandingdetailreport.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPOutstandingDetailReportView extends Searchable{
	
	
	public void list(Page<OMPOutstandingDetailReportFormDto> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
		BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument);

}