package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PABillingConsolidatedTable extends GEditableTable<PABillingConsolidatedDTO>{

	

	public PABillingConsolidatedTable() {
		super(PABillingConsolidatedTable.class);
		setUp();
	}

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "part",
		"benefitsCover", "payableAmount", "amountAlreadypaid", "netPayableAmount"};  */
	
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		// TODO Auto-generated method stub

	}
	@Override
	public void tableSelectHandler(PABillingConsolidatedDTO t) {
		
	}
	
	@Override
	public void initTable() {
        table.setContainerDataSource(new BeanItemContainer<PABillingConsolidatedDTO>(PABillingConsolidatedDTO.class));
        Object[] NATURAL_COL_ORDER = new Object[] { "part",
    		"benefitsCover", "payableAmount", "amountAlreadypaid", "netPayableAmount"};  
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnFooter("benefitsCover", "Total");
	/*	table.setColumnHeader("part", "Part");
		table.setColumnHeader("benefitsCover", "Benefits Cover");
		table.setColumnHeader("payableAmount", "Payable Amount");
		table.setColumnHeader("amountAlreadypaid", "Amount Already paid");
		table.setColumnHeader("netPayableAmount", "Net PayableAmount");
	*/}


	@Override
	public String textBundlePrefixString() {
		
		return "claim-billing-consolidated-";
	}

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

}
