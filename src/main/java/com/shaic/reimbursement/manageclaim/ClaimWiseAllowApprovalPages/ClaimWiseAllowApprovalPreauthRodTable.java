package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ClaimWiseAllowApprovalPreauthRodTable extends GBaseTable<ClaimWiseApprovalDto> {
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ClaimWiseApprovalDto>(
				ClaimWiseApprovalDto.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","preauthOrRodNo","documentReceivedFrom","billClassification","amountClaimed","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setHeight("200px");
	}

	@Override
	public void tableSelectHandler(ClaimWiseApprovalDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "approval-preauth-rod-";
	}

}
