package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface SearchHoldMonitorScreenView extends Searchable{
	public void init(BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName);
	public void list(Page<SearchHoldMonitorScreenTableDTO> tableRows);
	public void buildSuccessLayout(String message);
}