package com.shaic.claim.cvc.auditaction;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.MasClmAuditUserMapping;

/**
 * @author GokulPrasath.A
 *
 */
public interface SearchCVCAuditActionView extends Searchable{
	
	
	public void list(Page<SearchCVCAuditActionTableDTO> tableRows);
	public void init();
	public void setAuditUser(MasClmAuditUserMapping auditUserMap);
	public void validation(SearchCVCAuditActionFormDTO searchCVCAuditActionFormDTO);

}
