package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OPProcessingDetailsTable extends
		GBaseTable<OPProcessingDetailsTableDTO> {
	/**
* 
*/
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "type", "claimedAmount", "nonPayable",
			"payableAmt", "status", "typeofPayment", "transactionNo",
			"transactionDate", "remarks" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<OPProcessingDetailsTableDTO>(
				OPProcessingDetailsTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "type", "claimedAmount", "nonPayable",
			"payableAmt", "status", "typeofPayment", "transactionNo",
			"transactionDate", "remarks" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("80px");

	}

	@Override
	public void tableSelectHandler(OPProcessingDetailsTableDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "view-claimstatus-";
	}

}
