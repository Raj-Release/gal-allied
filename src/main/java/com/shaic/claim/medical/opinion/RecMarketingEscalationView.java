package com.shaic.claim.medical.opinion;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface RecMarketingEscalationView extends GMVPView {

	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);

	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);

	void buildSuccessLayout();

	public void buildInitiateLumenRequest(String intimationId);
	
}

