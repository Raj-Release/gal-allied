package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchAddAdditionalDocumentPaymentInfoTable extends GBaseTable<SearchAddAdditionalDocumentPaymentInfoTableDTO> {
	
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
		table.setContainerDataSource(new BeanItemContainer<SearchAddAdditionalDocumentPaymentInfoTableDTO>(
				SearchAddAdditionalDocumentPaymentInfoTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);

		
	}

	@Override
	public void tableSelectHandler(
			SearchAddAdditionalDocumentPaymentInfoTableDTO t) {
		// TODO Auto-generated method stub
		fireViewEvent(PAMenuPresenter.PA_ADD_ADDITIONAL_PAYMENT_INFORMATION, t);
		
		
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
