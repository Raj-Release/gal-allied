package com.shaic.paclaim.financial.claimapproval.hosiptal;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;



public interface PASearchProcessClaimAprovalHosView extends Searchable  {
	public void list(Page<SearchProcessClaimBillingTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,
			BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
