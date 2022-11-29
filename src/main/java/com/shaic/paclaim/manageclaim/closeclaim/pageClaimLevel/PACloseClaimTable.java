package com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;


public class PACloseClaimTable extends GBaseTable<PACloseClaimTableDTO> {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","insuredPatientName","strDateOfAdmission","provisionAmount",
		"numberOfRod","claimStatus"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<PACloseClaimTableDTO>(
				PACloseClaimTableDTO.class));
	Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","insuredPatientName","strDateOfAdmission","provisionAmount",
			"numberOfRod","claimStatus"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setHeight("75px");
		
		
	}
	
	

	@Override
	public void tableSelectHandler(PACloseClaimTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "close-claim-details-";
	}
	
	 public List<PACloseClaimTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<PACloseClaimTableDTO> itemIds = (List<PACloseClaimTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}

