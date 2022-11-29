package com.shaic.paclaim.rod.enterbilldetails.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PASearchEnterBillDetailsView extends Searchable {

	
	public void list(Page<SearchEnterBillDetailTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
