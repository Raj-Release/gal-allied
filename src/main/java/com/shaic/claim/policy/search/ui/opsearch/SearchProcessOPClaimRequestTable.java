/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public class SearchProcessOPClaimRequestTable extends GBaseTable<SearchProcessOPClaimRequestTableDTO>{

	private static final long serialVersionUID = -2709165698739147665L;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo", "healthCardIDNo", "insuredPatientName",
		"claimType", "healthCheckupDate", "reasonForHealthVisit", "amountClaimed"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchProcessOPClaimRequestTableDTO>(SearchProcessOPClaimRequestTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(5);
		table.setHeight("250px");
	}

	@Override
	public void tableSelectHandler(SearchProcessOPClaimRequestTableDTO t) {
		fireViewEvent(MenuPresenter.SHOW_PROCESS_OP_CLAIM, t);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-process-op-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
		
	}

}


