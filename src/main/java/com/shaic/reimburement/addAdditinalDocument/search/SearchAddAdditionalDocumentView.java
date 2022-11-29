package com.shaic.reimburement.addAdditinalDocument.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchAddAdditionalDocumentView extends Searchable  {
	public void list(Page<SearchAddAdditionalDocumentTableDTO> tableRows);
	public void validation();

}
