package com.shaic.claim.intimation.unlock;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUnlockIntimationView extends Searchable {

	public void list(Page<SearchUnlockIntimationTableDTO> search);

}