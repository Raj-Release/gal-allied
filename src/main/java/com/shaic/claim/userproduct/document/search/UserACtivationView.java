package com.shaic.claim.userproduct.document.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UserACtivationView extends Searchable{
	
	void init(UserManagementDTO productDocTypeDto, BeanItemContainer<SelectValue> userTypeContainer,BeanItemContainer<SelectValue> documentTypeContainer,BeanItemContainer<SelectValue> claimFlagTypeContainer);
	
	void submitValues();

}
