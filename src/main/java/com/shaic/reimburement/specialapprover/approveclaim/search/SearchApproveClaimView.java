/**
 * 
 */
package com.shaic.reimburement.specialapprover.approveclaim.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public interface SearchApproveClaimView extends Searchable  {
	
	public void list(Page<SearchApproveClaimTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> cpuCode);
	public void validation();
}
