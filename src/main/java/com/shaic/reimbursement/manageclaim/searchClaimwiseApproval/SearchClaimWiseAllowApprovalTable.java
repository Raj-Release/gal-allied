package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchClaimWiseAllowApprovalTable extends GBaseTable<SearchClaimWiseAllowApprovalDto> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "policyNo", "claimNo", "claimType",
		"insuredPatientName", "cpuCode", "hospitalName", "hospitalCity", "noOfRods", "dateOfAdmission"}; 

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchClaimWiseAllowApprovalDto>(SearchClaimWiseAllowApprovalDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("331px");
	
	}

	@Override
	public void tableSelectHandler(SearchClaimWiseAllowApprovalDto t) {
		fireViewEvent(MenuPresenter.CLAIM_WISE_ALLLOW_APPROVAL, t);
	}

	@Override
	public String textBundlePrefixString() {

		return "search-claimwise-approval-";
	}

}
