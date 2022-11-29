package com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class PASearchCloseClaimTableRODLevel extends GBaseTable<PASearchCloseClaimTableDTORODLevel>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo", "healthCardNo",
		"insuredPatientName", "cpuCode", "hospitalName", "hospitalAddress", "hospitalCity", "dateOfAdmission", "reasonForAdmission"}; 
	
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
		table.setHeight("331px");
	}

	@Override
	public void tableSelectHandler(
			PASearchCloseClaimTableDTORODLevel t) {

			fireViewEvent(PAMenuPresenter.CLOSE_CLAIM_SEARCH_BASED_ROD_LEVEL, t);

	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-close-claim-rod-lvl-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
