package com.shaic.claim.bulkconvertreimb.search;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class BatchConvertedTable extends GBaseTable<SearchBatchConvertedTableDto> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno", "crNo","subCrNo", "letterDateValue", "cpuCode", "category", "noofRecords", "status"}; 

	@Override
	public void removeRow() {
	
		table.removeAllItems();
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchBatchConvertedTableDto>(
				SearchBatchConvertedTableDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300");

		table.removeGeneratedColumn("Print");
		table.addGeneratedColumn("Print", new Table.ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {

				if ((SearchBatchConvertedTableDto) itemId != null) {

					final SearchBatchConvertedTableDto letterDto = (SearchBatchConvertedTableDto) itemId;

					final Button generateLetterButton = new Button(
							"Print Letter");

					generateLetterButton.setData(letterDto);
					generateLetterButton
							.addClickListener(new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									SearchBatchConvertedTableDto convertDto = (SearchBatchConvertedTableDto) event
											.getButton().getData();
									if (convertDto != null) {
										fireViewEvent(
												SearchBulkConvertReimbPresenter.SHOW_BULK_COVERING_LETTER,
												convertDto);
									}
								}
							});
					generateLetterButton.addStyleName("link");
					return generateLetterButton;
				} else {
					return "";
				}
			}

		});

		table.removeGeneratedColumn("Export");
		table.addGeneratedColumn("Export", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {

				Button exportBtn = new Button("Export to Excel");
				final SearchBatchConvertedTableDto BulkDto = (SearchBatchConvertedTableDto) itemId;

				exportBtn.setData(BulkDto);

				exportBtn.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						SearchBatchConvertedTableDto convertBulkDto = (SearchBatchConvertedTableDto) event
								.getButton().getData();

						fireViewEvent(
								SearchBulkConvertReimbPresenter.EXPORT_BULK_CONVERT_LIST,
								convertBulkDto);

					}
				});

				exportBtn.setStyleName(BaseTheme.BUTTON_LINK);
				return exportBtn;

			}
		});

	}

	@Override
	public void tableSelectHandler(SearchBatchConvertedTableDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-batch-convert-table-";
	}

	public List<SearchBatchConvertedTableDto> getTableList(){
		
		return (List<SearchBatchConvertedTableDto>)table.getContainerDataSource().getItemIds();
	}
}
