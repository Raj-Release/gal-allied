package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OPClaimStatusDoumentListTable extends
		GBaseTable<OPClaimStatusDoumentListDTO> {
	/**
* 
*/
	private static final long serialVersionUID = 1L;

	public Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber", "value",
			"mandatoryDocFlag", "requiredDocType", "strReceivedStatus",
			"noOfDocuments", "remarks" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<OPClaimStatusDoumentListDTO>(
				OPClaimStatusDoumentListDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(OPClaimStatusDoumentListDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "view-opslaimstatusdoumentlisttable-";
	}

}
