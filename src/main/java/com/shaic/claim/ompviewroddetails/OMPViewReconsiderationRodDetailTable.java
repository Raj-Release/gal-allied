package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewReconsiderationRodDetailTable extends GBaseTable<OMPViewReconsiderationRodTableDTO>{
	
private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"siNo","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType","amount","paymentType"
		,"bankName","chequeOrTransactionNo","chequeOrTransactionDate","accountNo","ifscCode","branchName"
		};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPViewReconsiderationRodTableDTO>(OMPViewReconsiderationRodTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"siNo","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType","amount","paymentType"
			,"bankName","chequeOrTransactionNo","chequeOrTransactionDate","accountNo","ifscCode","branchName"
			};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}

	@Override
	public void tableSelectHandler(OMPViewReconsiderationRodTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "ompreconsiderationrod-details-";
	}

}
