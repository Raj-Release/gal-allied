package com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;

public interface ClaimWiseAllowApprovalView extends GMVPView {
	
	public void init(SearchClaimWiseAllowApprovalDto Dto);
	
	public void setTableList(List<ClaimWiseApprovalDto> listDetails);
	
	public void buildSuccessLayout();

}
