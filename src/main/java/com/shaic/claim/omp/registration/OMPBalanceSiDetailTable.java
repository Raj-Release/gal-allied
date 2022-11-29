package com.shaic.claim.omp.registration;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class OMPBalanceSiDetailTable  extends GBaseTable<OMPBalanceSiTableDTO>{
	
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {         
			"coverCode", "coverCodeDescription", "sumInsured", "claimPaid","claimOustanding","balance","provisionforcurrentclaim",
			"bSIafterProvision" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<OMPBalanceSiTableDTO>(
				OMPBalanceSiTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {         
			"coverCode", "coverCodeDescription", "sumInsured", "claimPaid","claimOustanding","balance","provisionforcurrentclaim",
			"bSIafterProvision" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("120px");

	}

	@Override
	public void tableSelectHandler(OMPBalanceSiTableDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "view-ompbalancesuminsured-";
	}
	
	public Table getBSITable(){
		return this.table;
	}

}
