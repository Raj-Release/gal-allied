package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewProductBenefitsTable extends GBaseTable<ViewProductBenefitsTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"conditionCode", "description","longDescription"};
	
	@Override
	public void removeRow() {}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewProductBenefitsTableDTO>(ViewProductBenefitsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");	
		table.setSelectable(false);
		
	}

	@Override
	public void tableSelectHandler(ViewProductBenefitsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-product-benefits-table-";
	}
}
