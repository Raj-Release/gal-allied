package com.shaic.reimbursement.printReminderLetterBulk;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

/**
 * 
 *
 */
public class SearchPrintRemainderBulkTable extends GBaseTable<PrintBulkReminderResultDto>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno", "batchid", "letterDateValue", "cpuCode", "claimType", "category", "reminderType", 
		"totalNoofRecords" }; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	private SearchPrintRemainderBulkView parent;
		
	public void initview(SearchPrintRemainderBulkView parent){
		this.parent = parent;
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PrintBulkReminderResultDto>(PrintBulkReminderResultDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300");

		table.removeGeneratedColumn("DownloadPDF");
		table.addGeneratedColumn("DownloadPDF",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
					if((PrintBulkReminderResultDto)itemId != null){
					
						final PrintBulkReminderResultDto letterDto = (PrintBulkReminderResultDto)itemId;
						String btnCaption = "";
						if((SHAConstants.N_FLAG).equalsIgnoreCase(letterDto.getPrint())){
							btnCaption = "View";
						}
						else{
							btnCaption = "Download PDF";
						}
						
						final Button generateLetterButton = new Button(btnCaption);

						generateLetterButton.setData(letterDto);
						generateLetterButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										PrintBulkReminderResultDto reminderDto = (PrintBulkReminderResultDto) event
												.getButton()
												.getData();
										if(reminderDto != null){
											
												fireViewEvent(SearchPrintRemainderBulkPresenter.PRINT_BULK_LETTER, reminderDto);

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
	
	public void generatePrintCompletedColumn(){
		table.removeGeneratedColumn("Completed");
		table.addGeneratedColumn("Completed",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
					if((PrintBulkReminderResultDto)itemId != null){
					
						final PrintBulkReminderResultDto letterDto = (PrintBulkReminderResultDto)itemId;
						String btnCaption = "";
						if((SHAConstants.N_FLAG).equalsIgnoreCase(letterDto.getPrint())){
							
							btnCaption = "Completed";
						
						final Button generateLetterButton = new Button(btnCaption);

						generateLetterButton.setData(letterDto);
						generateLetterButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										PrintBulkReminderResultDto reminderDto = (PrintBulkReminderResultDto) event
												.getButton().getData();
//										getUI().addWindow(dialog);
										
										if(letterDto != null){
											
												fireViewEvent(SearchPrintRemainderBulkPresenter.COMPLETED_BUTTON_CLICK, reminderDto);
										}
							        } 
							    });
						generateLetterButton.addStyleName("link");
					        return generateLetterButton;
							}
					}
					return "";
					
				  }
				});
	}

	public void generateExprotColumn(){
		table.removeGeneratedColumn("Excel");
		table.addGeneratedColumn("Excel",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
//							
					Button	exportBtn = new Button("Excel");
					final PrintBulkReminderResultDto BulkDto = (PrintBulkReminderResultDto)itemId;
					
					exportBtn.setData(BulkDto);
					
					exportBtn.addClickListener(new Button.ClickListener() {
							
						@Override
						public void buttonClick(ClickEvent event) {
							PrintBulkReminderResultDto reminderBulkDto = (PrintBulkReminderResultDto) event.getButton().getData();
													
							fireViewEvent(SearchPrintRemainderBulkPresenter.EXPORT_BULK_PRINT_REMINDER_LIST,reminderBulkDto);
														
						}
					});
					
					exportBtn.setStyleName(BaseTheme.BUTTON_LINK);
					return exportBtn;					
					
				}});
	}
	
	@Override
	public void tableSelectHandler(
			PrintBulkReminderResultDto t) {
		

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
