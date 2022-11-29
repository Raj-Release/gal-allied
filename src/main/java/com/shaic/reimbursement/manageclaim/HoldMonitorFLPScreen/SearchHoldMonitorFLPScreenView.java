package com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface SearchHoldMonitorFLPScreenView extends Searchable{
	public void init(BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode);
	public void list(Page<SearchHoldMonitorScreenTableDTO> tableRows);
	public void buildSuccessLayout(String message);
}
