package com.shaic.claim.intimation.unlockompintimation;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.intimation.unlockbpmntodb.SearchUnlockIntimationDBTableDTO;

public interface SearchUnlockOMPIntimationDBView extends Searchable {

	public void list(Page<SearchUnlockIntimationDBTableDTO> search);

}