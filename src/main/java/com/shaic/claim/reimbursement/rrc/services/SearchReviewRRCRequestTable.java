/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;


import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public class SearchReviewRRCRequestTable extends GBaseTable<SearchReviewRRCRequestTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "intimationNo", "rrcRequestNo", "dateOfRequestForTable", "requestorId",
		"requestorName", "rrcRequestType","eligibilityValue"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchReviewRRCRequestTableDTO>(SearchReviewRRCRequestTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("220px");
	}

	@Override
	public void tableSelectHandler(
			SearchReviewRRCRequestTableDTO t) {
		
		//Table handler to be devloped.
	fireViewEvent(MenuPresenter.SHOW_REVIEW_RRC_REQUEST_DETAILS, t);
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-review-rrc-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}


