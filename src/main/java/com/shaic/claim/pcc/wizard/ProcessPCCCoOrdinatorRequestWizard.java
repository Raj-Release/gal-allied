package com.shaic.claim.pcc.wizard;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessPCCCoOrdinatorRequestWizard extends GMVPView {
	    
	void buildSuccessLayout();
	
	void generateapproveLayout();
	
	void generateQuerryLayout();
	
	void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer);

}
