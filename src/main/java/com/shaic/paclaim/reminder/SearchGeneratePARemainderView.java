package com.shaic.paclaim.reminder;

import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateReminderTableDTO;


/**
 * @author ntv.narenj
 *
 */
public interface SearchGeneratePARemainderView extends Searchable  {
	public void list(Page<SearchGenerateReminderTableDTO> tableRows);
	public void generateReminderLetter(SearchGenerateReminderTableDTO reminderLetterDto);
	public void clearReminderLetterSearch();
	public void resetReminderLetterSearch();
	public void init(Map<String,Object> parameter);
}
