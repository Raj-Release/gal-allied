package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class MoreDetailSpecialityTable extends GBaseTable<SpecialityDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"serialNumber","specialityType", "remarks" };
*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SpecialityDTO>(
				SpecialityDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNo","specialityType","procedure", "remarks" };

		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(SpecialityDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-speciality-table-";
	}

}
