package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewNegotiationDetailsTable extends GBaseTable<OMPViewNegotiationDetailsTableDTO>{
	
	
	
private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","intimationNo","claimNo","eventCode","negotiationRequestedDate","negotiationCompletedDate","nameofNegotiator",
		"approvedAmount","agreedAmount","negotiationRemarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPViewNegotiationDetailsTableDTO>(OMPViewNegotiationDetailsTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","intimationNo","claimNo","eventCode","negotiationRequestedDate","negotiationCompletedDate","nameofNegotiator",
			"approvedAmount","agreedAmount","negotiationRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}
	
	@Override
	public void tableSelectHandler(OMPViewNegotiationDetailsTableDTO t) {
			
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "ompnegotiation-details-";
	}

}
