package com.shaic.paclaim.reimbursement.bulkreminder;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.reminderBulkSearch.BulkReminderResultDto;


/**
 * @author ntv.narenj
 *
 */
public interface SearchGeneratePARemainderBulkView extends Searchable  {
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
