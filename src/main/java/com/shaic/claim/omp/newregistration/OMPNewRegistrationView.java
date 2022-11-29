package com.shaic.claim.omp.newregistration;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface OMPNewRegistrationView extends Searchable {
	public void list(Page<OMPNewRegistrationSearchDTO> tableRows);
}
