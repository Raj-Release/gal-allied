package com.shaic.claim.userreallocation;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AutoAllocationAssignedDetailsTable extends GBaseTable<AutoAllocationDetailsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	DoctorReallocationSearchCriteriaService searchService;
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"sNumber","intimationNo","doctorId","doctorName","claimedAmt","cpu","assignedDate","completedDate"};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<AutoAllocationDetailsTableDTO>(AutoAllocationDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(AutoAllocationDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "intimation-list-re-allocation-assigned-";
	}
}
