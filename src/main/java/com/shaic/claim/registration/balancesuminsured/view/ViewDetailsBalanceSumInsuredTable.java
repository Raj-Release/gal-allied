package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewDetailsBalanceSumInsuredTable extends
GBaseTable<BalanceSumInsuredTableDTO>{

	/**
* 
*/
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"sectionII", "sumInsured", "utilised", "balance" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<BalanceSumInsuredTableDTO>(
				BalanceSumInsuredTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"sectionII", "sumInsured", "utilised", "balance" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(BalanceSumInsuredTableDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "view-balancesuminsured-";
	}



}
