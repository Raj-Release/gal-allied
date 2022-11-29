package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ComprehensiveHospitalisationTable extends GBaseTable<ComprehensiveHospitalisationTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"sectionI", "cover", "subCover", "sumInsured", "claimPaid", "claimOutStanding", "balance", "provisionCurrentClaim", "balanceSI" };*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ComprehensiveHospitalisationTableDTO>(
				ComprehensiveHospitalisationTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"sectionI", "cover", "subCover", "sumInsured", "claimPaid", "claimOutStanding", "balance", "provisionCurrentClaim", "balanceSI" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("180px");
		table.setWidth("100%");

	}

	@Override
	public void tableSelectHandler(ComprehensiveHospitalisationTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-hospitalisation-";
	}

}
