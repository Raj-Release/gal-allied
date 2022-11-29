package com.shaic.claim.claimhistory.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewClaimAuditTable extends GBaseTable<ViewClaimHistoryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"referenceNo","dateAndTime","userID","userName","remarks","claimsReply","qryRplRemarks","qryRplDate","rplyUser","status"};

	private static final Object[] CLM_USER_COL_ORDER = new Object[] {
		"serialNumber","dateAndTime","remarks","claimsReply","qryRplRemarks","qryRplDate","rplyUser","status"};
	
	@Override 
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	public void setAuditUserVisibleColumns() {
		
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}
	
	public void setClaimUserVisibleColumns() {
		
		table.setVisibleColumns(CLM_USER_COL_ORDER);
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewClaimHistoryDTO>(
				ViewClaimHistoryDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}

	@Override
	public void tableSelectHandler(ViewClaimHistoryDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "cvc-claim-audit-trail-";
	}

}
