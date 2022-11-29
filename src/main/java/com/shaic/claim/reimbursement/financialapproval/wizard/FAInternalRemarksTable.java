package com.shaic.claim.reimbursement.financialapproval.wizard;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.FAInternalRemarksTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class FAInternalRemarksTable extends GBaseTable<FAInternalRemarksTableDTO> {

	private static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNo", "claimType", "refNoRodNo", 
		"createdDate", "userId", "userName", "internalRemarks", "remarks"};

	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<FAInternalRemarksTableDTO>(FAInternalRemarksTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(FAInternalRemarksTableDTO t) {
	}

	@Override
	public String textBundlePrefixString() {
		return "fa-internal-remarks-";
	}
}