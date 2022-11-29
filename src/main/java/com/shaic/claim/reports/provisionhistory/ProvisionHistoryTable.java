package com.shaic.claim.reports.provisionhistory;

import java.util.Date;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ProvisionHistoryTable extends GBaseTable<ProvisionHistoryDTO>{

	/*private final static Object[] NATURAL_COL_ORDER =new Object[]{"intimationNo",
	 "cashlessRefNo",
	 "reimbursementRefNo",
	"previousProvisionAmount",
	"currentProvisionAmount",
	 "previousClaimProvisionAmount",
	 "currentClaimProvisionAmount",
	"status",
	 "date"
	};*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		// TODO Auto-generated method stub
		 Object[] NATURAL_COL_ORDER =new Object[]{"intimationNo",
			 "cashlessRefNo",
			 "reimbursementRefNo",
			"previousProvisionAmount",
			"currentProvisionAmount",
			 "previousClaimProvisionAmount",
			 "currentClaimProvisionAmount",
			"status",
			 "date"
			};
		table.setContainerDataSource(new BeanItemContainer<ProvisionHistoryDTO>(ProvisionHistoryDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);	
		table.setPageLength(table.size());
		//table.setHeight("200px");
	   }

	@Override
	public void tableSelectHandler(ProvisionHistoryDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "provisionhistorytable-";
	}

}
