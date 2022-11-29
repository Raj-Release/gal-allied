package com.shaic.claim.viewsublimits.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ComprehensiveSublimitDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewSublimitsCompTable extends GBaseTable<ComprehensiveSublimitDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno","section", "subLimitName","subLimitAmt","includingCurrentClaimAmt",
		"includingCurrentClaimBal","excludingCurrentClaimAmt", "excludingCurrentClaimBal"};

	@Override
	public void removeRow() {}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ComprehensiveSublimitDTO>(ComprehensiveSublimitDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("500px");
		table.setSelectable(false);
		
	}

	@Override
	public void tableSelectHandler(ComprehensiveSublimitDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-comprehensive-sublimits-table-";
	}

}
