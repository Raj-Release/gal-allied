package com.shaic.claim.pcc.hrmp;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.vaadin.v7.ui.VerticalLayout;

public class HRMPWizardViewImpl extends AbstractMVPView implements HRMPWizardView{
	
	@Inject
	private HRMPPageUI hrmpageUI;
	
	private VerticalLayout mainLayout;
	
	public void init(SearchHRMPTableDTO bean) {
		
		hrmpageUI.init(bean);
		mainLayout = new VerticalLayout(hrmpageUI);
		setCompositionRoot(mainLayout);
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

}
