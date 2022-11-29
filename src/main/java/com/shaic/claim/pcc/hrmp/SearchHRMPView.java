package com.shaic.claim.pcc.hrmp;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.domain.MasClmAuditUserMapping;

/**
 * @author GokulPrasath.A
 *
 */
public interface SearchHRMPView extends Searchable{
	
	
	public void list(Page<SearchHRMPTableDTO> page);
	public void init();
	public void setAuditUser(MasClmAuditUserMapping auditUserMap);

}
