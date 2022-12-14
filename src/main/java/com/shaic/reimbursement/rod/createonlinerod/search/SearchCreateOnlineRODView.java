package com.shaic.reimbursement.rod.createonlinerod.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchCreateOnlineRODView extends Searchable  {
	public void list(Page<SearchCreateOnlineRODTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument);
}
