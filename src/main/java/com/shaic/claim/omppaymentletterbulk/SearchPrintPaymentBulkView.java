package com.shaic.claim.omppaymentletterbulk;

import java.util.List;
import java.util.Map;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;

public interface SearchPrintPaymentBulkView extends Searchable  {
	
	public void generateBulkPdfPaymentLetter(String fileUrl,PrintBulkPaymentResultDto bulkReminderDto);
	public void loadBulkPaymentSearchTable(List<PrintBulkPaymentResultDto> bulkReminderResultDto);
	public void showErrorMsg(String string);
	
}
