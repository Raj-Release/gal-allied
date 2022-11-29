package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PABalanceSumInsuredTable extends GBaseTable<PABalanceSumInsuredTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"orginalSI", "cumulativeBonus","totalSI","claimPaid","claimOutStanding","balanceSI","currentClaim","availableSI" };*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<PABalanceSumInsuredTableDTO>(
				PABalanceSumInsuredTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"orginalSI", "cumulativeBonus","totalSI","claimPaid","claimOutStanding","balanceSI","currentClaim","availableSI" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(PABalanceSumInsuredTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-balancesuminsuredpa-";
	}

}
