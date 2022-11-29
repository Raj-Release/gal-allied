package com.shaic.reimbursement.processfieldvisit.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessFieldVisitView extends Searchable  {
	public void list(Page<SearchFieldVisitTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> cpuCode);
}
