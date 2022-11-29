package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewHistoryRodAndBillEntryDetailTable extends GBaseTable<OMPViewHistoryRodAndBillEntryTableDTO>{
	
	
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"typeOfClaim","rodNo","docRecdFrom","rodType","classification","subClassification","dateAndTime",
		"userId","userName","claimStage","status","userRemarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initTable(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPViewHistoryRodAndBillEntryTableDTO>(OMPViewHistoryRodAndBillEntryTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"typeOfClaim","rodNo","docRecdFrom","rodType","classification","subClassification","dateAndTime",
			"userId","userName","claimStage","status","userRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}
	
	@Override
	public void tableSelectHandler(OMPViewHistoryRodAndBillEntryTableDTO t) {
		
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "ompviewhistoryrod-details-";
	}

}
