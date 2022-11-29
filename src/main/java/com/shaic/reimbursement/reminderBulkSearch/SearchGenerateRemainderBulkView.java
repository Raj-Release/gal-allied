package com.shaic.reimbursement.reminderBulkSearch;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchGenerateRemainderBulkView extends Searchable  {
	public void list(Page<BulkReminderResultDto> tableRows);
//	public void generateReminderLetter(BulkReminderResultDto reminderLetterDto);
	public void generateBulkPdfReminderLetter(String fileUrl,BulkReminderResultDto bulkReminderDto);
	public void clearReminderLetterSearch();
	public void resetReminderLetterSearch();
	public void init(Map<String,Object> parameter);
	public void loadResultLayout(BulkReminderResultDto bulkReminderResultDto);
	public void loadBulkReminderSearchTable(List<BulkReminderResultDto> bulkReminderResultDto);
	public void exportToExcelReminderList(BulkReminderResultDto bulkReminderDto);
}
