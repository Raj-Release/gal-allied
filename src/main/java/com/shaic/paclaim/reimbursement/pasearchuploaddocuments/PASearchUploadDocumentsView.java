package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PASearchUploadDocumentsView extends Searchable{

	public void list(Page<SearchUploadDocumentsTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> type);
	public void buildSuccessMessageLayout(String message);
}

