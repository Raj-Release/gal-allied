package com.shaic.claim.search.specialist.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SpecialistCompletedTable extends GBaseTable<SubmitSpecialistTableDTO> {
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
		"intimationNo","specialityType","doctorName","dateOfRefer","doctorComments"
		 };
*/
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SubmitSpecialistTableDTO>(
				SubmitSpecialistTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo","specialityType","doctorName","dateOfRefer","doctorComments"
			 };

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("250px");
		table.setCaption("Completed");
	}

	@Override
	public void tableSelectHandler(SubmitSpecialistTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "Submit-Specialist-completed-";
	}

}
