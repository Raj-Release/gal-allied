package com.shaic.claim.reimbursement.rrc.services;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchRRCStatusView extends Searchable {
	public void list(Page<SearchRRCStatusTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType,
			BeanItemContainer<SelectValue> eligiblityType);

	public void showSearchRRCStatusView(RRCDTO rrcDTO);

}
