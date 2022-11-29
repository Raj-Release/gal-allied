package com.shaic.paclaim.addAdditinalDocument.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentTableDTO;

public interface PAAddAdditionalDocumentsView extends Searchable {

	public void list(Page<SearchAddAdditionalDocumentTableDTO> tableRows);
	public void validation();
}
