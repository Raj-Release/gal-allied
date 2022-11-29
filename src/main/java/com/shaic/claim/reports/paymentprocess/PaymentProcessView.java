package com.shaic.claim.reports.paymentprocess;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PaymentProcessView extends Searchable{
	public void list(Page<PaymentProcessTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch);

}
