package com.shaic.claim.registration;

import javax.enterprise.inject.Instance;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Props;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.view.ViewRenewedPolicyPreviousClaims;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
@Theme(Props.THEME_NAME)
public class BasePolicyPreviousClaimWindowUI extends UI{
	
	private static final long serialVersionUID = 1L;
	
	private Long bean = null;
	
	@Override
	protected void init(VaadinRequest request) {
	
		getPage().setTitle("Previous Claims");
		if((request.getWrappedSession().getAttribute(SHAConstants.CLAIM_DETAILS) != null &&
				request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS) != null)){
			bean = (Long) request.getWrappedSession().getAttribute(SHAConstants.CLAIM_DETAILS);
			ViewPreviousClaimsTable previousClaimTable = (ViewPreviousClaimsTable)request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE);
			ViewRenewedPolicyPreviousClaims renewedPolicyClaimTable = (ViewRenewedPolicyPreviousClaims)request.getWrappedSession().getAttribute(SHAConstants.VIEW_RENEWED_POLICY_CLAIMS_TABLE);
			Instance<ViewBasePolicyClaimsWindowOpen> viewBasePolicyClaims = (Instance<ViewBasePolicyClaimsWindowOpen>) request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS);
			ViewBasePolicyClaimsWindowOpen viewBaseClaims=viewBasePolicyClaims.get();
			viewBaseClaims.setPreviousClaimTable(previousClaimTable);
			viewBaseClaims.setRenewedPolicyClaimDetailsTable(renewedPolicyClaimTable);
			viewBaseClaims.showValues(bean);
			setContent(viewBaseClaims);
		} else {
			Label label = new Label("<b style = 'color:red;'>Previous Claims Not Available</b>", ContentMode.HTML);
			setContent(label);
		}
		
	}

}
