package com.shaic.claim.pcc.views;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.ZonalMedicalDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ZonalMedicalDetailsTable extends GBaseTable<ZonalMedicalDetailsTableDTO> {

	private final static Object[] NATURAL_COL_ORDER_ZONAL_MEDICAL_DEATILS = new Object[]{"medicalIdAndName","remarks","assignDateAndTime"};
	
	@Override
	public void removeRow() {
	}

	@Override
	public void initTable() {
			
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ZonalMedicalDetailsTableDTO>(ZonalMedicalDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"medicalIdAndName","remarks","assignDateAndTime"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(ZonalMedicalDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "zonal-medical-details-";
	}




}
