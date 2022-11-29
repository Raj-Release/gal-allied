package com.shaic.paclaim.health.reimbursement.financial.pages.billingprocess;

import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PAHealthFinancialProcessPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();

	void getValuesForMedicalDecisionTable(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues);

	void setPreAuthRequestedAmt(String calculatePreRequestedAmt);

	void setPosthospAmt(Integer amount, Map<String, Double> postHospitalizationValues, Integer balanceSI, Boolean isRestrictionSIAvail, Integer siRestrictedAmount, Integer previousPostHospAmt, Integer previousPreHospAmt);

	void setClaimRestrictionAmount(Long claimRestriction);

	void setBalanceSIforRechargedProcess(Double balanceSI);
}
