package com.shaic.claim.coinsurance.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ComprehensiveSublimitDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CoInsuranceTable extends GBaseTable<CoInsuranceTableDTO> {
	

	private static final long serialVersionUID = 1L;
	
	public static final Object[] COLUMN_HEADER = new Object[] {"sequenceNumber","shareType","sharePercentage","insuranceName"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {

		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<CoInsuranceTableDTO>(CoInsuranceTableDTO.class));		
		table.setVisibleColumns(COLUMN_HEADER);
		table.setSizeFull();
		table.setPageLength(table.size()+2);
		
	}

	@Override
	public void tableSelectHandler(CoInsuranceTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-coinsurance-details-table-";
	}

}
