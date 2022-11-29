package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;

public class OMPPaymentDetailsTable  extends GBaseTable<OMPPaymentDetailsTableDTO>{
	
private static final long serialVersionUID = 1L;
	
	private Window popup;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType",
		"amount","paymentType","bankName","chequeOrTransactionno","chequeOrTransactionDate","accountno","ifscCode","branchName"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPPaymentDetailsTableDTO>(OMPPaymentDetailsTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType",
			"amount","paymentType","bankName","chequeOrTransactionno","chequeOrTransactionDate","accountno","ifscCode","branchName"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}
	
	@Override
	public void tableSelectHandler(OMPPaymentDetailsTableDTO t) {
		
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "omppayment-details-";
	}

}
