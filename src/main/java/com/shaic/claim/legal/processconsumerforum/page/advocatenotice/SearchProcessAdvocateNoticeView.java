package com.shaic.claim.legal.processconsumerforum.page.advocatenotice;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalAdvocate;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchProcessAdvocateNoticeView extends Searchable  {
	
	public void list(Page<AdvocateNoticeDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> moveToMasterValueByCode, BeanItemContainer<SelectValue> pendingLevelMasterValueByCode,
			BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> recievedFrom);

	public void populateFiledValues(Claim claimsByIntimationNumber, LegalAdvocate legalAdvocate, String diagnosisName);

	public void buildSuccessLayout();
	
}