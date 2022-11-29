package com.shaic.reimbursement.paymentprocess.updatepayment;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class UpdatePaymentDetailForsuccessLayout extends GBaseTable<CreateAndSearchLotTableDTO>{

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"claimCount","approvedAmt","serviceTax","sumOfApprovedAndServiceTax","tdsAmt","netAmnt"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<CreateAndSearchLotTableDTO>(CreateAndSearchLotTableDTO.class));			
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("90px");

	}
	@Override
	public void tableSelectHandler(CreateAndSearchLotTableDTO t) {
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "search-create-batch-";
	}
	
	public void setTableData(CreateAndSearchLotTableDTO tableDTO) 
	{
		table.removeAllItems();
		table.addItem(tableDTO);
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
