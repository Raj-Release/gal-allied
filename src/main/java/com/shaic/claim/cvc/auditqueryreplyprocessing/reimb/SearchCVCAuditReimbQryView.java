package com.shaic.claim.cvc.auditqueryreplyprocessing.reimb;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;


public interface SearchCVCAuditReimbQryView extends Searchable{
	
	
	public void list(Page<SearchCVCAuditActionTableDTO> tableRows);
	public void init(SearchCVCAuditClsQryFormDTO srchFrmDto);

}
