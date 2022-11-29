package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchUploadNEFTDetailsTable extends GBaseTable<SearchUploadNEFTDetailsTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo", "claimNo", "policyNo",
		"insuredPatientName", "cpuCode", "claimType", "hospitalName",
		"hospitalCity", "dateOfAdmission", "reasonForAdmission" };


	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<SearchUploadNEFTDetailsTableDTO>(
				SearchUploadNEFTDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);

		
	}

	@Override
	public void tableSelectHandler(
			SearchUploadNEFTDetailsTableDTO t) {
		// TODO Auto-generated method stub
		fireViewEvent(MenuPresenter.SHOW_UPLOAD_NEFT_DETAILS, t);
		
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-add-additional-doc-payment-info-";
	}
	
	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7) {
			table.setPageLength(7);
		}

	}


}
