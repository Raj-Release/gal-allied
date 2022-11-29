package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ClaimRequestPreviousClaimsPageInterface extends GMVPView , WizardStep<PreauthDTO> {

	void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList);

	void genertateFieldsBasedOnRelapseOfIllness(
			Map<String, Object> referenceData);

	void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList);
	
	void generateApproveLayout();
	
	void generateQueryLayout();

	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void generateEscalateReplyLayout();

	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist);

	void genertateSentToReplyLayout();

	void generateCancelRodLayout() ;

	void init(PreauthDTO bean, GWizard wizard);

}
