package com.shaic.claim.intimation.unlockbpmntodb;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUnlockIntimationDBView extends Searchable {

	public void list(Page<SearchUnlockIntimationDBTableDTO> search);

}