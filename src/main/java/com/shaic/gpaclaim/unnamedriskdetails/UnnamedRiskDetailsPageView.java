package com.shaic.gpaclaim.unnamedriskdetails;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UnnamedRiskDetailsPageView extends GMVPView{

	void init(UnnamedRiskDetailsPageDTO bean);
	public void init(BeanItemContainer<SelectValue> category);
	public void buildSuccessLayout();
}
