package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class LumpSumTable extends GBaseTable<LumpSumTableDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"sectionVIII", "cover", "subCover", "limit", "claimPaid", "claimOutstanding", "balance", "provisionCurrentClaim", "balanceSI" };*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
	table.setContainerDataSource(new BeanItemContainer<LumpSumTableDTO>(
			LumpSumTableDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] {
		"sectionVIII", "cover", "subCover", "limit", "claimPaid", "claimOutstanding", "balance", "provisionCurrentClaim", "balanceSI" };
	table.setVisibleColumns(NATURAL_COL_ORDER);
	table.setHeight("90px");
	table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(LumpSumTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-lumpsum-";
	}

}
