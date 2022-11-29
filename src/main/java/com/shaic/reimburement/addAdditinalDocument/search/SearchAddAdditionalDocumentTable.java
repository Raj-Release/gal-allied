package com.shaic.reimburement.addAdditinalDocument.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchAddAdditionalDocumentTable extends GBaseTable<SearchAddAdditionalDocumentTableDTO> {
	private final static Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "intimationNo", "claimNo", "policyNo",
			"insuredPatientName", "cpuCode", "claimType", "hospitalName",
			"hospitalCity", "dateOfAdmission", "reasonForAdmission" };

	@Override
	public void removeRow() {
		table.removeAllItems(); 

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchAddAdditionalDocumentTableDTO>(
				SearchAddAdditionalDocumentTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	@Override
	public void tableSelectHandler(SearchAddAdditionalDocumentTableDTO t) {
		fireViewEvent(MenuPresenter.SHOW_ADDITIONAL_DOCUMENTS, t);
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-add-additional-doc-";
	}

	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7) {
			table.setPageLength(7);
		}

	}
}
