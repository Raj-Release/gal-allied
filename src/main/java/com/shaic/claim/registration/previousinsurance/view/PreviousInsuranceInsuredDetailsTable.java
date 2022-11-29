package com.shaic.claim.registration.previousinsurance.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PreviousInsuranceInsuredDetailsTable  extends
GBaseTable<PreviousInsuranceInsuredDetailsTableDTO> {
	
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
		"insuredName","sex","DOB","age","relation", "sumInsured", "pedDescription"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreviousInsuranceInsuredDetailsTableDTO>(
				PreviousInsuranceInsuredDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	

	}

	@Override
	public void tableSelectHandler(PreviousInsuranceInsuredDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "previous-insurance-insured-details-";
	}
	protected void setTableSize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
