package com.shaic.claim.reimbursement.billing.pages.billsummary;

import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;

public interface BillingSummaryPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();

	void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails);
}
