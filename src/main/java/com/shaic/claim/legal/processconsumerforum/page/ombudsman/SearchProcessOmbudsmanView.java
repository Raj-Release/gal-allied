package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalOmbudsman;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchProcessOmbudsmanView extends Searchable  {
	
	public void list(Page<ConsumerForumDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> statusoftheCase,
			BeanItemContainer<SelectValue> ombudsmanDetailsByDesc,
			BeanItemContainer<SelectValue> addDays,
			BeanItemContainer<SelectValue> pendingLevel,
			BeanItemContainer<SelectValue> hearingStatus,
			BeanItemContainer<SelectValue> awardStatus,
			BeanItemContainer<SelectValue> compromiseStatus,
			BeanItemContainer<SelectValue> decision,
			BeanItemContainer<SelectValue> recievedFrom,BeanItemContainer<SelectValue> movedTO,BeanItemContainer<SelectValue> grievanceOutcome);

	public void populateFiledValues(Claim claimsByIntimationNumber,LegalOmbudsman legalOmbudsman,String diagnosis);
	
	public void buildSuccessLayout();

	public void setOmbudsmanContact(String contactOmbudsman);
	
}