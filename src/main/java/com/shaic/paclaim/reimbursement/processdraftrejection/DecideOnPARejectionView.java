package com.shaic.paclaim.reimbursement.processdraftrejection;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.ClaimRejectionDto;
/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnPARejectionView  extends GMVPView,WizardStep<ClaimRejectionDto> {
	
	 void init(ClaimRejectionDto bean, GWizard wizard);
	 void returnPreviousPage();
	 void buildRedraftRejectionLayout();
	 void buildDisapproveRejectionLayout();
	 void buildApproveRejectionLayout();
}
