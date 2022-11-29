package com.shaic.reimbursement.queryrejection.generateremainder.search;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;


/**
 * 
 * @author Lakshminarayana
 *
 */
public interface GenerateReminderLetterDetailView extends GMVPView {

	public void generateReminderLetter(ReimbursementQueryDto  queryDto);
	public void submitReminderLetter(SearchDraftQueryLetterTableDTO queryDto);
	public void cancelReminderLetter();
	
}
