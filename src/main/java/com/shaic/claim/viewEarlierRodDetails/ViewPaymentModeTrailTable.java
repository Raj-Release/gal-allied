package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Window;

public class ViewPaymentModeTrailTable extends GBaseTable<ViewPaymentTrailTableDTO>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Inject
//	PaymentProcessCpuPage paymentProcessCpuPage;
	
	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","claimType", "dateAndTime", "userId", 
		"claimStage", "claimStatus","paymentMode", "reasonForChange"};
	
	@Override
	public void removeRow() {
		
		table.removeAllItems();
		
	}	

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ViewPaymentTrailTableDTO>(ViewPaymentTrailTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);	
		table.setPageLength(table.size() +2);
		table.setHeight("200px");	
	}

	@Override
	public void tableSelectHandler(ViewPaymentTrailTableDTO t) {	
		
	
	}	
	

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "viewpaymenttrail-";
	}

	

}
