package com.shaic.claim.pancard.page;

import java.util.List;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.IntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadedPanCardDocumentsTable extends GBaseTable<UploadedPanCardDocumentsDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "sno", "fileType","uploadDate","uploadBy"};*/
	
	
	@Inject
	private UpdatePanCardPdfPage updatePanCardPdfPage;
	
	@Inject
	private IntimationService intimationService;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<UploadedPanCardDocumentsDTO>(
				UploadedPanCardDocumentsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] { "sno", "fileType","uploadDate","uploadBy"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("200px");
		
		table.setColumnHeader("sno", "S.No");
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						UploadedPanCardDocumentsDTO dto = (UploadedPanCardDocumentsDTO)itemId;
                        final String name = dto.getFileName();
                        final String token = dto.getToken();
						Button button = new Button(name);
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								updatePanCardPdfPage.init(name,token);
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(updatePanCardPdfPage);
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
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("deletePan");
		table.addGeneratedColumn("deletePan",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button updatePanDetailsButton = new Button("Delete");
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						updatePanDetailsButton.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								UploadedPanCardDocumentsDTO tableDTO = (UploadedPanCardDocumentsDTO) itemId;
								String token = tableDTO.getToken();
								if(token!=null){
									intimationService.setInactiveUpdateDocumentDetails(token);
									table.removeItem(itemId);
								}
								//tableSelectHandler(tableDTO);
							}

						});
						
						return updatePanDetailsButton;
					}
				});
	
	}

	@Override
	public void tableSelectHandler(UploadedPanCardDocumentsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-uploaded-pan-documents-";
	}
	
	 public List<UploadedPanCardDocumentsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadedPanCardDocumentsDTO> itemIds = (List<UploadedPanCardDocumentsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	

}
