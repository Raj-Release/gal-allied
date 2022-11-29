package com.shaic.claim.userproduct.document.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UserManagementCpuView {
	
	void init(UserManagementDTO productDocTypeDto, BeanItemContainer<SelectValue> userTypeContainer);
}
