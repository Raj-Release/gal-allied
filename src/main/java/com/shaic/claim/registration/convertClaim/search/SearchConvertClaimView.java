package com.shaic.claim.registration.convertClaim.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchConvertClaimView extends GMVPView {

	public void list(Page<SearchConvertClaimTableDto> tableRows);

	public void init(BeanItemContainer<SelectValue> parameter);

}
