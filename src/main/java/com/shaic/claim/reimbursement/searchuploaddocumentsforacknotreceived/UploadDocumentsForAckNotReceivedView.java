package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UploadDocumentsForAckNotReceivedView extends Searchable{

	public void list(Page<UploadDocumentsForAckNotReceivedTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> type);
	public void buildSuccessMessageLayout(String message);
}
