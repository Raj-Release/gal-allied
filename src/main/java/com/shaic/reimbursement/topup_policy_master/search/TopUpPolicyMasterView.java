package com.shaic.reimbursement.topup_policy_master.search;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cpuskipzmr.SkipZMRListenerTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface TopUpPolicyMasterView extends GMVPView {
	
	public void initView();

	public void buildSuccessLayout();
	public void buildFailureLayout(String message);
	void generateTableForPolicyStatus(List<TopUpPolicyMasterTableDTO> policyNo);

}
