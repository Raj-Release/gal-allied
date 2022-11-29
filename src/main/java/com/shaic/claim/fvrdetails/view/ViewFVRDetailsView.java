package com.shaic.claim.fvrdetails.view;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ViewFVRDetailsView extends GMVPView{
	
public void list(BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> fvrAssignTo, BeanItemContainer<SelectValue> fvrPriority);
	
	
}
