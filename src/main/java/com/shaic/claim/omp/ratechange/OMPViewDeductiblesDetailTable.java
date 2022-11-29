package com.shaic.claim.omp.ratechange;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewDeductiblesDetailTable extends GBaseTable<OMPClaimProcessorDTO>{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private OMPIntimationService intimationService;
	
	private OMPClaimProcessorDTO bean;
	
	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"slno","eventCode","eventdescription","description","deductibles"};
*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPClaimProcessorDTO>(OMPClaimProcessorDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","eventCode","eventdescription","description","deductibles"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}

	@Override
	public void tableSelectHandler(OMPClaimProcessorDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ompdeductibles-details-";
	}

}
