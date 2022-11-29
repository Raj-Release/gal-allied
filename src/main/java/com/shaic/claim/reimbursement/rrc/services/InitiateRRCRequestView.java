package com.shaic.claim.reimbursement.rrc.services;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface InitiateRRCRequestView extends Searchable {

	public void list(Page<InitiateRRCRequestTableDTO> tableRows);
	
	
}
	