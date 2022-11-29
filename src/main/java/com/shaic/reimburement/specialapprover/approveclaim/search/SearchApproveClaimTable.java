/**
 * 
 */
package com.shaic.reimburement.specialapprover.approveclaim.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class SearchApproveClaimTable extends GBaseTable<SearchApproveClaimTableDTO>{

	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo", 
		"insuredPatientName", "cpuCode", "claimType","hospitalName", "hospitalCity",  "dateOfAdmission", "reasonForAdmission"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchApproveClaimTableDTO>(SearchApproveClaimTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(SearchApproveClaimTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-approve-claim-table-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
