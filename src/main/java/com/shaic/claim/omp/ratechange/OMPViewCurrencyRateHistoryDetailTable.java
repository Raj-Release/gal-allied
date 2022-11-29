package com.shaic.claim.omp.ratechange;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.domain.OMPOtherCurrencyRateHistoryDto;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewCurrencyRateHistoryDetailTable extends GBaseTable<OMPOtherCurrencyRateHistoryDto>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private OMPIntimationService intimationService;
	
	private OMPClaimProcessorDTO bean;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"slno","otherurrencyCode","currencyName","country","currencyRate","date","userName","userId","processingStage"};*/


	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPOtherCurrencyRateHistoryDto>(OMPOtherCurrencyRateHistoryDto.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","otherurrencyCode","currencyName","country","currencyRate","date","userName","userId","processingStage"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}

	@Override
	public void tableSelectHandler(OMPOtherCurrencyRateHistoryDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ompcurrencyrate-details-";
	}


}
