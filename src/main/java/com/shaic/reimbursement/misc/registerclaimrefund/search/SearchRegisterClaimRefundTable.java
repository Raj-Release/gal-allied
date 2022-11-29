package com.shaic.reimbursement.misc.registerclaimrefund.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class SearchRegisterClaimRefundTable extends GBaseTable<SearchRegisterClaimRefundTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo","healthCardNo", 
		"insuredPatientName", "cpuCode", "hospitalName", "hospitalAddress", "hospitalCity", "dateOfAdmission", "reasonForAdmission","claimStatus"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchRegisterClaimRefundTableDTO>(SearchRegisterClaimRefundTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("hospitalAddress", 350);
		table.setColumnWidth("hospitalCity", 250);
		table.setHeight("348px");
	}

	@Override
	public void tableSelectHandler(
			SearchRegisterClaimRefundTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-register-claim-refund-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
