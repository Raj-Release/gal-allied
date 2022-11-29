package com.shaic.reimbursement.billing.processclaimbilling.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessClaimBillingView extends Searchable  {
	public void list(Page<SearchProcessClaimBillingTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,
			BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> statusByStage);
}
