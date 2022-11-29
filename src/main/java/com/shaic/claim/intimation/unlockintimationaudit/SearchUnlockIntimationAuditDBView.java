package com.shaic.claim.intimation.unlockintimationaudit;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBTableDTO;

public interface SearchUnlockIntimationAuditDBView extends Searchable {

	public void list(Page<SearchUnlockIntimationAuditDBTableDTO> search);

}
