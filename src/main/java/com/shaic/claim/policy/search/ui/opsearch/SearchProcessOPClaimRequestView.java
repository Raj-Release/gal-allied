/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.vijayar
 *
 */
public interface SearchProcessOPClaimRequestView  extends Searchable  {
	public void list(Page<SearchProcessOPClaimRequestTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> claimType,
						BeanItemContainer<SelectValue> pioCode);
	
}
