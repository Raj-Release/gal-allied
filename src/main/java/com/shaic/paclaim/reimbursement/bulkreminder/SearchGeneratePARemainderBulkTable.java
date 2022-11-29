package com.shaic.paclaim.reimbursement.bulkreminder;

import com.shaic.arch.table.GBaseTable;
import com.shaic.reimbursement.reminderBulkSearch.BulkReminderResultDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

/**
 * 
 *
 */
public class SearchGeneratePARemainderBulkTable extends GBaseTable<BulkReminderResultDto>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno", "batchid","subBatchid", "letterDateValue", "cpuCode", "claimType", "documentReceivedFrom", "category", "reminderType", 
		"totalNoofRecords", "status"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	private SearchGeneratePARemainderBulkView parent;
		
	public void initview(SearchGeneratePARemainderBulkView parent){
		this.parent = parent;
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<BulkReminderResultDto>(BulkReminderResultDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300");
		
		table.removeGeneratedColumn("Export");
		table.addGeneratedColumn("Export",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
//							BulkReminderResultDto reminderBulkDto = (BulkReminderResultDto)itemId;
//							Component expoLayout = exportView.initView(reminderBulkDto);
//							
//							return expoLayout;					
					
					Button	exportBtn = new Button("Export to Excel");
					final BulkReminderResultDto BulkDto = (BulkReminderResultDto)itemId;
					
					exportBtn.setData(BulkDto);
					
					exportBtn.addClickListener(new Button.ClickListener() {
							
						@Override
						public void buttonClick(ClickEvent event) {
							BulkReminderResultDto reminderBulkDto = (BulkReminderResultDto) event.getButton().getData();
																				
//							List<SearchGenerateReminderBulkTableDTO> eportList = reminderBulkDto.getResultListDto();
//							
//							if(eportList != null && !eportList.isEmpty()){
//								
//								searchresultExportTable.init("", false, false);
//								searchresultExportTable.setTableList(eportList);
//								
//								ExcelExport excelExport;
//								excelExport = new ExcelExport(searchresultExportTable.getTable());
//								excelExport.setReportTitle("Bulk Reminder List");
//								excelExport.setDisplayTotals(false);
//								excelExport.export();
//							}
									
							fireViewEvent(SearchGeneratePARemainderBulkPresenter.EXPORT_PA_BULK_REMINDER_LIST,reminderBulkDto);
//							parent.exportToExcelReminderList(reminderBulkDto);
														
						}
					});
					
					exportBtn.setStyleName(BaseTheme.BUTTON_LINK);
					return exportBtn;					
					
				}});
		
		table.removeGeneratedColumn("Print");
		table.addGeneratedColumn("Print",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
					if((BulkReminderResultDto)itemId != null){
					
						final BulkReminderResultDto letterDto = (BulkReminderResultDto)itemId;
						
						final Button generateLetterButton = new Button(
								"Print Letter");

						generateLetterButton.setData(letterDto);
						generateLetterButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										BulkReminderResultDto reminderDto = (BulkReminderResultDto) event
												.getButton()
												.getData();
										if(reminderDto != null){
											
												fireViewEvent(SearchGeneratePARemainderBulkPresenter.GENERATE_PA_BULK_LETTER, reminderDto);

										}
							        } 
							    });
						generateLetterButton.addStyleName("link");
					        return generateLetterButton;
							}
					else{
						return "";
					}
					}
				});
	}

	@Override
	public void tableSelectHandler(
			BulkReminderResultDto t) {
		

	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-bulk-remainder-letter-";
	}
	protected void tablesize(){
//		table.setPageLength(table.size()+1);
//		int length =table.getPageLength();
//		if(length>=7){
//			table.setPageLength(7);
//		}
		
		table.setPageLength(table.getItemIds().size()+1);		
	}
}
