package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewRejectionRodDetailTable extends GBaseTable<OMPViewRejectionRodTableDTO>{
	
	
private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"siNo","intimationNo","claimNo","policyNo","productName","eventCode","clasification",
		"subClasification","claimType","insuredPatientName","ailment","claimedAmount"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void initTable(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPViewRejectionRodTableDTO>(OMPViewRejectionRodTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"siNo","intimationNo","claimNo","policyNo","productName","eventCode","clasification",
			"subClasification","claimType","insuredPatientName","ailment","claimedAmount"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}
	
	@Override
	public void tableSelectHandler(OMPViewRejectionRodTableDTO t) {
		
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "ompviewrejectionrod-details-";
	}


}
