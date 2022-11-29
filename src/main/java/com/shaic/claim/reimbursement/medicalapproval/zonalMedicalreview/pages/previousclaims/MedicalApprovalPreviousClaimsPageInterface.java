package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.previousclaims;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;

public interface MedicalApprovalPreviousClaimsPageInterface extends GMVPView , WizardStep<PreauthDTO> {

	void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList);

	void genertateFieldsBasedOnRelapseOfIllness(
			Map<String, Object> referenceData);

	void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList);

}
