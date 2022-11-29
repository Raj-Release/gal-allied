package com.shaic.claim.OMPprocessrejection.detailPage;

import javax.enterprise.inject.Instance;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Props;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
@Theme(Props.THEME_NAME)
public class OMPPreviousClaimWindowUI extends UI{
	
	private static final long serialVersionUID = 1L;
	
	private String bean = null;
	
	@Override
	protected void init(VaadinRequest request) {
		
		getPage().setTitle("Previous Claims");
		if(request.getWrappedSession().getAttribute(SHAConstants.CLAIM_DETAILS) != null &&
				request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS) != null) {
			
			bean = (String) request.getWrappedSession().getAttribute(SHAConstants.CLAIM_DETAILS);
			Instance<ViewOMPPreviousClaimWindowOpen> viewPreviousClaims = (Instance<ViewOMPPreviousClaimWindowOpen>) request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS);
			ViewOMPPreviousClaimsTable previousClaimTable = (ViewOMPPreviousClaimsTable)request.getWrappedSession().getAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE);
			ViewOMPPreviousClaimWindowOpen viewClaims = viewPreviousClaims.get();
			viewClaims.setPreviousClaimTable(previousClaimTable);
			viewClaims.showValues(bean);
			setContent(viewClaims);

		} else {
			Label label = new Label("<b style = 'color:red;'>Previous Claims Not Available</b>", ContentMode.HTML);
			setContent(label);
		}
		
	}

}
