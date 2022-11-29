package com.shaic.claim.reimbursement.billing.pages.billingprocess;

import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface BillingProcessPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();

	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);

	void setPreAuthRequestedAmt(String calculatePreRequestedAmt);

	void setPosthospAmt(Integer amount, Map<String, Double> postHospitalizationValues, Integer balanceSI, Boolean isRestrictionSIAvail, Integer siRestrictedAmount, Integer previousPosthospAmt, Integer previousPrehospAmt);

	void setClaimRestrictionAmount(Long claimRestriction);

	void setBalanceSIforRechargedProcess(Double balanceSI);

	void editSublimitValues(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);
}
