package com.shaic.claim.productbenefit.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class DayCareBenefitsTable   extends
GBaseTable<DayCareBenefitsTableDTO>{

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"dayCareBenefits", "dayCareLimits"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<DayCareBenefitsTableDTO>(
				DayCareBenefitsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(DayCareBenefitsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "day-care-benefits-";
	}
	

}
