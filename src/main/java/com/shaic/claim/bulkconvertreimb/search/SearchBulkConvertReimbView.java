package com.shaic.claim.bulkconvertreimb.search;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchBulkConvertReimbView extends GMVPView {

	public void list(List<SearchBulkConvertReimbTableDto> tableRows);
	public void setPrevBatchConvertList(List<SearchBatchConvertedTableDto> prevBatchList);

	public void init(BeanItemContainer<SelectValue> cpuCodeParam, BeanItemContainer<SelectValue> typeValueParam);
	public void repaintConvertedBatchTable(List<SearchBatchConvertedTableDto> prevBatchList);
	public void repaintPrintStatusOfBatchTable();
	public void generateBulkPdfCoveringLetter(String fileUrl,SearchBatchConvertedTableDto bulkConvertDto);
	public void submitBulkCoveringLetter(SearchBatchConvertedTableDto bulkConvertClaimDto);
	public void loadSearchResultLayout(String resultMsg);
	public void showSuccesslayout(SearchBatchConvertedTableDto bulkConvertClaimDto);
	public void exportConvertedListToExcel(List<SearchBulkConvertReimbTableDto> finalconvertedList);
	public List<SearchBatchConvertedTableDto> getBulkTableList();
	
}
