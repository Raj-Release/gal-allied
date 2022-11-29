package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import com.shaic.arch.table.GBaseTable;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class StopPaymentTrackingTable extends GBaseTable<StopPaymentTrackingTableDTO> {

	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","rodNumber","utrNumber",
		"requestBy","requestedDate","resonForStopPayment","stopPaymentReqRemarks","validateBy","validateDate","action","validationRemarks"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<StopPaymentTrackingTableDTO>(StopPaymentTrackingTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300px");
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(StopPaymentTrackingTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "stop-payment-tracking-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
