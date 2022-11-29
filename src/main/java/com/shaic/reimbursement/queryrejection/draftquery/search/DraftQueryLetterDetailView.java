package com.shaic.reimbursement.queryrejection.draftquery.search;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;


/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DraftQueryLetterDetailView extends GMVPView,WizardStep<SearchDraftQueryLetterTableDTO>  {

	public void init(SearchDraftQueryLetterTableDTO bean, GWizard wizard);
	public void cancelDraftQueryLetter();
//	public void submitDraftQueryLetter();
	public void buildSuccessLayout();
	public void returnPreviousPage(SearchDraftQueryLetterTableDTO updatedBean);
	public SearchDraftQueryLetterTableDTO getUpdatedBean();
	public void deleteQueryDraftOrReDraftLetterRemarks(DraftQueryLetterDetailTableDto deletedRemarksDto);
	public void showSuccessSaveMessage(ReimbursementQueryDto queryDto);
//	public void setUpdatedBean(SearchDraftQueryLetterTableDTO updatedBean);
	public void clearObject();
}
