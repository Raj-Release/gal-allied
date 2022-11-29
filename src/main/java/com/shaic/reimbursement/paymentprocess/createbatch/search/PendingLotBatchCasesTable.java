package com.shaic.reimbursement.paymentprocess.createbatch.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PendingLotBatchCasesTable extends GBaseTable<PendingLotBatchReportDto>{

	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {
	
		table.setContainerDataSource(new BeanItemContainer<PendingLotBatchReportDto>(PendingLotBatchReportDto.class));
		table.setHeight("500px");
		table.setWidth("95%");
		table.setVisibleColumns(new Object[] { "sno","intimationNo", "claimNo", "policyNo", "product", "claimType",	"CPUCode", "CPUName", "paymentMode", "lotNo", "payeeName", "bankName", "paymentType", "IFSCCode", "benefiAccNo", "branchName", "payableAt", "panNo", "approvedAmt", "providerCode", "paymentReqDate", "paymentReqUID", "emailID", "status"});	
		
	}

	@Override
	public void tableSelectHandler(PendingLotBatchReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "pending-lot-batch-table-";
	}

}
