package com.shaic.claim.reimbursement.financialapproval.pages.billsummary;

import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;

public interface FinancialSummaryPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();

	void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails);
}
