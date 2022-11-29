package com.shaic.claim.reimbursement.billing.wizard;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.billing.dto.BillingInternalRemarksTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class BillingInternalRemarksTable extends GBaseTable<BillingInternalRemarksTableDTO> {

	private static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNo", "claimType", "refNoRodNo", 
		"createdDate", "userId", "userName", "internalRemarks", "remarks"};

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<BillingInternalRemarksTableDTO>(BillingInternalRemarksTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(BillingInternalRemarksTableDTO t) {
	}

	@Override
	public String textBundlePrefixString() {
		return "billing-internal-remarks-";
	}
}