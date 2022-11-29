package com.shaic.reimbursement.queryrejection.generateremainder.search;

import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchGenerateRemainderView extends Searchable  {
	public void list(Page<SearchGenerateReminderTableDTO> tableRows);
	public void generateReminderLetter(SearchGenerateReminderTableDTO reminderLetterDto);
	public void clearReminderLetterSearch();
	public void resetReminderLetterSearch();
	public void init(Map<String,Object> parameter);
}
