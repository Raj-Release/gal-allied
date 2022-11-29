package com.shaic.claim.omp.ratechange;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationFormDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPClaimRateChangeAndOsUpdationView extends Searchable{
	
	public void list(Page<OMPClaimRateChangeAndOsUpdationTableDTO> tableRows);
	public void init();
	public void buildSuccessLayout();

}
