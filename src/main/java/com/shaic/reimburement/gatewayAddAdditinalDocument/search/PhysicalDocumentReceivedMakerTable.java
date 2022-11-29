package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PhysicalDocumentReceivedMakerTable extends GBaseTable<PhysicalDocumentReceivedMakerTableDTO>{

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
		table.setContainerDataSource(new BeanItemContainer<PhysicalDocumentReceivedMakerTableDTO>(
				PhysicalDocumentReceivedMakerTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	@Override
	public void tableSelectHandler(PhysicalDocumentReceivedMakerTableDTO t) {
//		String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//		t.setUsername(userID);
		fireViewEvent(MenuPresenter.SHOW_PHYSICAL_DOCUMENT, t);
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
