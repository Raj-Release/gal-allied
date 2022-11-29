package com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PAReopenClaimTable extends GBaseTable<PAReopenClaimTableDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","insuredPatientName","strDateOfAdmission","provisionAmount","claimStatus", "strDateOfClosedClaim", "reasonForClosure", "closeClaimRemarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<PAReopenClaimTableDTO>(
				PAReopenClaimTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","insuredPatientName","strDateOfAdmission",
			"provisionAmount","claimStatus", "strDateOfClosedClaim", "reasonForClosure", "closeClaimRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setHeight("75px");
		
	}
	
	

	@Override
	public void tableSelectHandler(PAReopenClaimTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "close-claim-details-";
	}
	
	 public List<PAReopenClaimTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<PAReopenClaimTableDTO> itemIds = (List<PAReopenClaimTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}
