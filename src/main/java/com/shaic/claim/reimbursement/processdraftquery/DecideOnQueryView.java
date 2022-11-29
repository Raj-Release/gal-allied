package com.shaic.claim.reimbursement.processdraftquery;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnQueryView  extends GMVPView,WizardStep<ClaimQueryDto> {
	
	 void init(ClaimQueryDto bean, GWizard wizard);
	 void returnPreviousPage(ClaimQueryDto updatedBean);
	 void buildRedraftQueryLayout();
	 void buildRejectQueryLayout();
	 void buildApproveQueryLayout();
	 void getUpdatedBean();
	 void setUpdatedBean(ClaimQueryDto updatedBean);
	 void clearObject();
//	 void deleteDraftQueryLetterRemarks(DraftQueryLetterDetailTableDto deltedObj);
}
