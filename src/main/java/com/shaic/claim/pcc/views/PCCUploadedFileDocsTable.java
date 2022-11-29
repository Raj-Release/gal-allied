package com.shaic.claim.pcc.views;

import java.util.ArrayList;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PCCUploadedFileDocsDTO;
import com.shaic.reimbursement.uploadrodreports.UploadDocumentPdfPage;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class PCCUploadedFileDocsTable extends GBaseTable<PCCUploadedFileDocsDTO> {
	
	//private static final long serialVersionUID = 3028729594709428726L;
	
	@Inject
	private UploadDocumentPdfPage uploadDocumentPdfPage;

	@Override
	public void removeRow() {
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PCCUploadedFileDocsDTO>(PCCUploadedFileDocsDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"fileName"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		generateColumns();
	}

	
	private void generateColumns() {
		
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						PCCUploadedFileDocsDTO dto = (PCCUploadedFileDocsDTO)itemId;
                        final String name = dto.getFileName();
                        final String token = dto.getDocToken().toString();
						Button button = new Button(name);
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								uploadDocumentPdfPage.init(name,token);
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(uploadDocumentPdfPage);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
								popup.addCloseListener(new Window.CloseListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void windowClose(CloseEvent e) {
										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	//button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	}
	
	@Override
	public void tableSelectHandler(PCCUploadedFileDocsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "pcc-uploadfile-";
	}


}
