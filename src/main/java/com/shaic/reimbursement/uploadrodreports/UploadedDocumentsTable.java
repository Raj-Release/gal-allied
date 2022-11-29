package com.shaic.reimbursement.uploadrodreports;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadedDocumentsTable extends GBaseTable<UploadedDocumentsDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "sno",
			"RODNo", "fileType"};*/
	
	////private static Window popup;
	
	List<UploadedDocumentsDTO> deletedDocList;
	
	@Inject
	private UploadDocumentPdfPage uploadDocumentPdfPage;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<UploadedDocumentsDTO>(
				UploadedDocumentsDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "sno",
			"RODNo", "fileType"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("200px");
		
		table.setColumnHeader("sno", "S.No");
		
		deletedDocList = new ArrayList<UploadedDocumentsDTO>();
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						UploadedDocumentsDTO dto = (UploadedDocumentsDTO)itemId;
                        final String name = dto.getFileName();
                        final String token = dto.getToken();
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
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				UploadedDocumentsDTO dto = (UploadedDocumentsDTO) itemId;
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						final UploadedDocumentsDTO currentItemId = (UploadedDocumentsDTO) event.getButton().getData();
						if (table.getItemIds().size() > 0) {
							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														UploadedDocumentsDTO dto =  (UploadedDocumentsDTO)currentItemId;
														if(dto.getKey() != null) {
															deletedDocList.add(dto);
														}
														table.removeItem(currentItemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
							
						} else {
							HorizontalLayout layout = new HorizontalLayout(
									new Label("One File is Mandatory."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						}
						
					}
				});
				return deleteButton;
			}
		});		
	
	}

	@Override
	public void tableSelectHandler(UploadedDocumentsDTO t) {
		// TODO Auto-generated method stub
		
	}	
	

	@Override
	public String textBundlePrefixString() {
		return "view-uploaded-documents-";
	}
	
	 public List<UploadedDocumentsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadedDocumentsDTO> itemIds = (List<UploadedDocumentsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	 public List<UploadedDocumentsDTO> getdeletedList(){
		 
		 return deletedDocList;
	 }
	

}
