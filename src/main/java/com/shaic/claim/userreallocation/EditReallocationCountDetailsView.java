package com.shaic.claim.userreallocation;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.domain.AutoAllocationDetails;

public interface EditReallocationCountDetailsView extends GMVPView {

	void setUserIntimationDetails(List<AutoAllocationDetails> list);
	
	void submitValues(Boolean value);
}
