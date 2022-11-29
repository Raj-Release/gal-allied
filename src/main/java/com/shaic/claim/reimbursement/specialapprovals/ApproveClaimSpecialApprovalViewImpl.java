package com.shaic.claim.reimbursement.specialapprovals;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class ApproveClaimSpecialApprovalViewImpl extends AbstractMVPView implements ApproveClaimSpecialApprovalView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ApproveClaimSpecialApprovalPage approveClaimSpecialApprovalPage;
	
	@Inject
	ApproveClaimSpecialApprovalDTO approveClaimSpecialApprovalDTO;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	public void init(){
		approveClaimSpecialApprovalPage.init(approveClaimSpecialApprovalDTO);
	}

}
