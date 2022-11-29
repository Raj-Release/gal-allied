package com.shaic.claim.fvrgrading.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchFvrReportGradingView extends GMVPView {

	public void list(Page<SearchFvrReportGradingTableDto> tableRows);

	public void init(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> parameter1);

}
