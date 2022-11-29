/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public interface SearchModifyRRCRequestView extends Searchable  {
	public void list(Page<SearchModifyRRCRequestTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType,BeanItemContainer<SelectValue> eligiblityType);
}

