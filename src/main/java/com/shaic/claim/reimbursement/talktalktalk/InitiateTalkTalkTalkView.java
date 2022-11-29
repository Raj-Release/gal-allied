package com.shaic.claim.reimbursement.talktalktalk;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface InitiateTalkTalkTalkView extends Searchable {

	public void list(Page<InitiateTalkTalkTalkTableDTO> tableRows);
	
	public void init();

}
