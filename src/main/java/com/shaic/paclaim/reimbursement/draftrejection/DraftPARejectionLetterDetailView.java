package com.shaic.paclaim.reimbursement.draftrejection;

import com.shaic.arch.GMVPView;


/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DraftPARejectionLetterDetailView extends GMVPView {

	public void cancelDraftRejectionLetter();
	public void submitDraftRejectionLetter();
	public void openPdfFileInWindow();
}
