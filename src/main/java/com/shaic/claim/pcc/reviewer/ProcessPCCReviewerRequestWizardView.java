package com.shaic.claim.pcc.reviewer;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessPCCReviewerRequestWizardView extends GMVPView {
		
    void generateQuerryLayout();
    
    void generateResponseLayout();
	
	void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer);
	public void buildSuccessLayout();
}
