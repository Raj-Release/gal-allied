package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAUploadDocumentsOutsideProcessView extends Searchable{


	public void list(Page<UploadDocumentsForAckNotReceivedTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> type);
	public void buildSuccessMessageLayout(String message);

}
