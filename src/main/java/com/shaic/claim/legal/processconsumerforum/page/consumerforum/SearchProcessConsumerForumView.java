package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalConsumer;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchProcessConsumerForumView extends Searchable  {
	
	public void list(Page<ConsumerForumDTO> tableRows);


	public void init(BeanItemContainer<SelectValue> repudiationMasterValueByCode,
			BeanItemContainer<SelectValue> tmpZoneList,
			BeanItemContainer<SelectValue> tmpStateList,
			BeanItemContainer<SelectValue> orderMasterValueByCode,
			BeanItemContainer<SelectValue> order1MasterValueByCode,
			BeanItemContainer<SelectValue> awardReasonMasterValueByCode,
			BeanItemContainer<SelectValue> depAmtMasterValueByCode,
			BeanItemContainer<SelectValue> caseUpdateMasterValueByCode,
			BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> statusCase);


	public void populateFiledValues(Claim claimsByIntimationNumber, LegalConsumer legalConsumer, String diagnosisName);


	public void buildSuccessLayout();

	
}