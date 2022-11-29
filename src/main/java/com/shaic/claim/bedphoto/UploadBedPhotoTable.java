package com.shaic.claim.bedphoto;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.IntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadBedPhotoTable extends GBaseTable<UploadBedPhotoDTO>{
	
	@Inject
	private UpdateBedPhotoFilePage bedPhotoFilePage;
	
	@EJB
	private IntimationService intimationService;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<UploadBedPhotoDTO>(
				UploadBedPhotoDTO.class));
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
						UploadBedPhotoDTO dto = (UploadBedPhotoDTO)itemId;
                        final String name = dto.getFileName();
                        final String token = dto.getToken();
						Button button = new Button(name);
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								bedPhotoFilePage.init(name,token);
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(bedPhotoFilePage);
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
		table.removeGeneratedColumn("deletepan");
		table.addGeneratedColumn("deletepan",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button updateAadharButton = new Button("Delete");
						updateAadharButton.addStyleName(ValoTheme.BUTTON_LINK);
						updateAadharButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						updateAadharButton.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								UploadBedPhotoDTO tableDTO = (UploadBedPhotoDTO) itemId;
								String token = tableDTO.getToken();
								if(token!=null){
									intimationService.setInactiveUpdateDocumentDetails(token);
									table.removeItem(itemId);
								}
								//tableSelectHandler(tableDTO);
							}

						});
						
						return updateAadharButton;
					}
				});
	
	}

	@Override
	public void tableSelectHandler(UploadBedPhotoDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-uploaded-pan-documents-";
	}
	
	public List<UploadBedPhotoDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<UploadBedPhotoDTO> itemIds = (List<UploadBedPhotoDTO>) this.table.getItemIds() ;
    	return itemIds;
    }


}
