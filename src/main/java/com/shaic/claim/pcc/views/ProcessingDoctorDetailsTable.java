package com.shaic.claim.pcc.views;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ProcessingDoctorDetailsTable extends GBaseTable<PccDetailsTableDTO>{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4855018479636665100L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PccDetailsTableDTO>(PccDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"doctorName","pccCategory","pccSubCategory1","pccSubCategory2","escalatePccRemarks"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(PccDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "processing-doctor-details-";
	}

}
