package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;


public interface SearchCVCAuditFaQryView extends Searchable{
	
	
	public void list(Page<SearchCVCAuditActionTableDTO> tableRows);
	public void init(SearchCVCAuditClsQryFormDTO srchFrmDto);

}
