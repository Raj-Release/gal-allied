package com.shaic.reimbursement.rod.uploadinvestication.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchUploadInvesticationView extends Searchable  {
	public void list(Page<SearchUploadInvesticationTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter);
}
