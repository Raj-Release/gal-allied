package com.shaic.claim.fileUpload;


import java.util.List;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;

public class MultipleUploadDocumentTable extends GBaseTable<MultipleUploadDocumentDTO>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Page currentPage;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sNo","fileName"};*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
        table.setContainerDataSource(new BeanItemContainer<MultipleUploadDocumentDTO>(MultipleUploadDocumentDTO.class));
        Object[] NATURAL_COL_ORDER = new Object[] {"sNo","fileName"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnHeader("fileName", "File Name");
		
		
		table.removeGeneratedColumn("ViewDocument");
		table.addGeneratedColumn("ViewDocument", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View Document");
					viewIntimationDetailsButton.setData(itemId);
					
					final MultipleUploadDocumentDTO dto = (MultipleUploadDocumentDTO) itemId;

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {

									
									Window popup = new com.vaadin.ui.Window();
									popup.setWidth("75%");
									popup.setHeight("90%");
									if(dto != null && dto.getFileName() != null && dto.getFileToken() != null){
										fileViewUI.setCurrentPage(currentPage);
										fileViewUI.init(popup,dto.getFileName(), dto.getFileToken());
									}
									popup.setContent(fileViewUI);
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
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
		});
		
		table.setColumnHeader("ViewDocument","View Document");

	}

	@Override
	public void tableSelectHandler(MultipleUploadDocumentDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setCurrentPage(Page currentPage){
		this.currentPage = currentPage;
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "multi-upload-table-";
	}
	protected void setTablesize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	 public List<MultipleUploadDocumentDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<MultipleUploadDocumentDTO> itemIds = (List<MultipleUploadDocumentDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
}
