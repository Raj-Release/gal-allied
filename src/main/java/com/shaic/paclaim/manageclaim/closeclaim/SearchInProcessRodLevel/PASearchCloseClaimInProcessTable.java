package com.shaic.paclaim.manageclaim.closeclaim.SearchInProcessRodLevel;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PASearchCloseClaimInProcessTable extends GBaseTable<PASearchCloseClaimTableDTORODLevel>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo", "healthCardNo",
		"insuredPatientName", "cpuCode", "hospitalName", "hospitalAddress", "hospitalCity", "dateOfAdmission", "reasonForAdmission", "claimStatus"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PASearchCloseClaimTableDTORODLevel>(PASearchCloseClaimTableDTORODLevel.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("hospitalAddress", 350);
		table.setColumnWidth("hospitalCity", 250);
	}

	@Override
	public void tableSelectHandler(
			PASearchCloseClaimTableDTORODLevel t) {
		
			fireViewEvent(PAMenuPresenter.CLOSE_CLAIM_IN_PROCESS, t);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-close-claim-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
