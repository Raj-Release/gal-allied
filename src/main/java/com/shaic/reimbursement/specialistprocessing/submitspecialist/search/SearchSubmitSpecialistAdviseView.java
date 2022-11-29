package com.shaic.reimbursement.specialistprocessing.submitspecialist.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchSubmitSpecialistAdviseView extends Searchable  {
	public void list(Page<SearchSubmitSpecialistAdviseTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter);
}
