package com.shaic.reimbursement.paymentprocess.processpaymentreturn.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessPaymentReturnsTable extends GBaseTable<SearchProcessPaymentReturnsTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimNo", "policyNo", "rodNo",
		"insuredPatiendName", "payeeName", "modeOfPayment","cheque_DD_UTRNo","claimType","cpuCode", "lotNo"}; 

	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchProcessPaymentReturnsTableDTO>(SearchProcessPaymentReturnsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(
			SearchProcessPaymentReturnsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-process-payment-returns-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
