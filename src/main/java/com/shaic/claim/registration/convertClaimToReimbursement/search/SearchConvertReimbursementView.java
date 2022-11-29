package com.shaic.claim.registration.convertClaimToReimbursement.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchConvertReimbursementView extends GMVPView{
	
	public void list(Page<SearchConvertClaimTableDto> tableRows);

	public void init(BeanItemContainer<SelectValue> parameter);

}
