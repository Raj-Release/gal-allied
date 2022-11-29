package com.shaic.paclaim.reimbursement.processdraftquery;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnPAQueryView  extends GMVPView,WizardStep<ClaimQueryDto> {
	
	 void init(ClaimQueryDto bean, GWizard wizard);
	 void returnPreviousPage(ClaimQueryDto updatedBean);
	 void buildRedraftQueryLayout();
	 void buildRejectQueryLayout();
	 void buildApproveQueryLayout();
	 void getUpdatedBean();
	 void setUpdatedBean(ClaimQueryDto updatedBean);
//	 void deleteDraftQueryLetterRemarks(DraftQueryLetterDetailTableDto deltedObj);
}
