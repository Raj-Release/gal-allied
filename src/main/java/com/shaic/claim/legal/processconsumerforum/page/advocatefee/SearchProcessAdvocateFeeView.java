package com.shaic.claim.legal.processconsumerforum.page.advocatefee;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.LegalAdvocateFee;
import com.shaic.domain.LegalConsumer;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchProcessAdvocateFeeView extends Searchable  {
	
	public void list(Page<AdvocateFeeDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> repudiationMasterValueByCode, BeanItemContainer<SelectValue> tmpZoneList);

	public void populateFiledValues(LegalConsumer legalConsumer,LegalAdvocateFee legalAdvocate);
	
	public void buildSuccessLayout();
	
}