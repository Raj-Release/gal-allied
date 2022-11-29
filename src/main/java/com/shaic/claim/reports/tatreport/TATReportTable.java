package com.shaic.claim.reports.tatreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class TATReportTable extends GBaseTable<TATReportTableDTO>{

	/**************************************************************************************************************************************
	private static final Object[] PENDING_CLAIMS_TAT = new Object[] {
		"serialNumber","intimationNo","claimType","rodNo","cpu","ackDate","latAckDate","rodDate",
		"zonalApprovalDate","maApproveDate","billApproveDate","faApprovedDate","totalDelay","status","userQueue","currentDelay"};
	
	private static final Object[] COMPLETED_CLAIMS_TAT = new Object[] {
		"serialNumber","intimationNo","claimType","rodNo","cpu","ackDate","latAckDate","rodDate",
		"zonalApprovalDate","maApproveDate","billApproveDate","faApprovedDate","settledDate","rodDelay","zonalDelay",
		"medicalDelay","billingDelay","faDelay","totalDelay","delayExcludingSunday","status"};
******************************************************************************************************************************************/		
	
	private static final Object[] REVISED_PENDING_CLAIMS_TAT = new Object[] {

		"serialNumber","intimationNo","pendingWith","rodDelay","zonalDelay","medicalDelay","billingDelay","faDelay","pendingDays","totalDelay",
		"delayExcludingSunday","claimType","userId","userName","docRecvdFrom","hospName","hospCity","rodNo","cpu","ZMRAmt","claimedAmt","reconsider","ackDate",
		"latAckDate","rodDate","zonalApprovalDate","maApproveDate","billApproveDate","faApprovedDate","lastStatusDate","investigationDate", 
		"rejectionDate", "status","officeCodeValue","zone"};
	
	private static final Object[] REVISED_COMPLETED_CLAIMS_TAT = new Object[] {
		"serialNumber","intimationNo","claimType","docRecvdFrom","hospName","hospCity","claimedBillCompletedAmt","FAApprovedAmt","rodNo","cpuCode","cpu",
		"claimedAmt","ackDate","latAckDate","rodDate","zonalApprovalDate","maApproveDate","billApproveDate","faApprovedDate","settledDate","rodDelay",
		"zonalDelay","medicalDelay","billingDelay","faDelay","totalDelay","delayExcludingSunday","status"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<TATReportTableDTO>(TATReportTableDTO.class));
//		table.setVisibleColumns(PENDING_CLAIMS_TAT);
		table.setVisibleColumns(REVISED_PENDING_CLAIMS_TAT);
		table.setHeight("350px");
		
	}
	@Override
	public void tableSelectHandler(TATReportTableDTO t) {
		//fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	 public void setVisibleColumnsForPendingClaims(){
//		 table.setVisibleColumns(PENDING_CLAIMS_TAT);
		 table.setVisibleColumns(REVISED_PENDING_CLAIMS_TAT);
		 localize(null);
	 }
	
	public void setVisibleColumnsForCompletedClaims(){
//		 table.setVisibleColumns(COMPLETED_CLAIMS_TAT);
		table.setVisibleColumns(REVISED_COMPLETED_CLAIMS_TAT);
		 localize(null);
	 }
	
	@Override
	public String textBundlePrefixString() {
		return "tatReport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
