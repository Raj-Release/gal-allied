package com.shaic.claim.userproduct.document.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ManageUserView extends GMVPView {
	
	void init(UserManagementDTO userMgmtTypeDto, BeanItemContainer<SelectValue> userTypeContainer);
	void submitValues();

}
