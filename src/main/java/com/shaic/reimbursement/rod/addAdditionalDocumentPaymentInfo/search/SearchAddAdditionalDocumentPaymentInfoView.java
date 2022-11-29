package com.shaic.reimbursement.rod.addAdditionalDocumentPaymentInfo.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


public interface SearchAddAdditionalDocumentPaymentInfoView extends Searchable {
	public void list(Page<SearchAddAdditionalDocumentPaymentInfoTableDTO> tableRows);
	public void validation();

}
