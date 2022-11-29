package com.shaic.claim.reports.paymentbatchreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PaymentBatchReportTable extends GBaseTable<PaymentBatchReportTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo","productNameValue","accountBatchNo","typeOfClaim","paymentType",
		"lotNo","approvedAmt","serviceTax","sumOfApprovedAndServiceTax","tdsAmt","netAmnt","tdsPercentage","payeeName","payableAt","cpuCode","ifscCode","beneficiaryAcntNo","branchName","chequeNo",
		"strChequeDate","bankCode","bankName","panNo","providerCode","refNo","paymentReqDateValue","userID","emailID","zonalMailId","lastAckDateValue","nextDayOfFaApprovedDateValue",
		"noOfExceedingDays","exceedingIRDATatDays","penalInterestAmnt","penalTotalAmnt","penalRemarks",}; 
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PaymentBatchReportTableDTO>(PaymentBatchReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");		
	}
	@Override
	public void tableSelectHandler(PaymentBatchReportTableDTO t) {
		fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "paymentbachreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
