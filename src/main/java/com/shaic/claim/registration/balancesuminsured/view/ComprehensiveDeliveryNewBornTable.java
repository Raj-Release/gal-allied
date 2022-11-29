package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ComprehensiveDeliveryNewBornTable extends GBaseTable<ComprehensiveDeliveryNewBornTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"sectionII", "cover", "subCover", "limit", "claimPaid", "claimOutstanding", "balance", "provisionCurrentClaim", "balanceSI" };*/
		
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ComprehensiveDeliveryNewBornTableDTO>(
				ComprehensiveDeliveryNewBornTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"sectionII", "cover", "subCover", "limit", "claimPaid", "claimOutstanding", "balance", "provisionCurrentClaim", "balanceSI" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("130px");
		table.setWidth("100%");

	}

	@Override
	public void tableSelectHandler(ComprehensiveDeliveryNewBornTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-deliverynewborn-";
	}

}
