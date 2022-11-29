package com.shaic.paclaim.printbulkreminder;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;


/**
 * 
 *
 */
public interface SearchPAPrintRemainderBulkView extends Searchable  {
	public void list(Page<PrintBulkReminderResultDto> tableRows);
//	public void generateReminderLetter(BulkReminderResultDto reminderLetterDto);
	public void generateBulkPdfReminderLetter(String fileUrl,PrintBulkReminderResultDto bulkReminderDto);
	public void clearReminderLetterSearch();
	public void resetReminderLetterSearch();
	public void init(Map<String,Object> parameter);
	public void loadResultLayout(PrintBulkReminderResultDto bulkReminderResultDto);
	public void loadBulkReminderSearchTable(List<PrintBulkReminderResultDto> bulkReminderResultDto);
	public void exportToExcelReminderList(PrintBulkReminderResultDto bulkReminderDto);
	public void submitReminderLetterBulkReminderResultDto(
			PrintBulkReminderResultDto bulkDto);
}
