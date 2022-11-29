package com.shaic.claim.cvc.auditqueryapproval;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;


public interface SearchCVCAuditQryApprovalView extends Searchable{
	
	
	public void list(Page<SearchCVCAuditActionTableDTO> tableRows);
	public void init();
	public void validation(SearchCVCAuditActionFormDTO searchCVCAuditActionFormDTO);

}
