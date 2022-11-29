package com.shaic.claim.pcc.zonalCoordinator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.zonalMedicalHead.ProcessPCCZonalMedicalHeadRequestPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadDocumentPdfPage;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;

public class ProcessPCCUploadDocumentsPageTableZC extends GBaseTable<PCCUploadDocumentsDTO>{

	private static final long serialVersionUID = -5524600977147253320L;
	/*private String presenterString = "";
	private Window popup;*/
	//final Embedded imageViewer = new Embedded("Uploaded Image");
	
	List<PCCUploadDocumentsDTO> deletedDocList;
	
	@Inject
	private UploadDocumentPdfPage uploadDocumentPdfPage;

	
	public static String dataDir = System.getProperty("jboss.server.data.dir");



	
	@Override
	public void removeRow() {
		//table.removeAllItems();
	}
	

	
	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<PCCUploadDocumentsDTO>(
				PCCUploadDocumentsDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {"fileName","fileUploadRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setHeight("100px");
		table.setWidth("100%");
		generateColumns();
		
	}

	private void generateColumns() {
		
		deletedDocList = new ArrayList<PCCUploadDocumentsDTO>();
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						PCCUploadDocumentsDTO dto = (PCCUploadDocumentsDTO)itemId;
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
				    	//button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
		table.removeGeneratedColumn("fileUploadRemarks");
		table.addGeneratedColumn("fileUploadRemarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final PCCUploadDocumentsDTO dto = (PCCUploadDocumentsDTO)itemId;	
				
				com.vaadin.v7.ui.TextField txtRemarks = new com.vaadin.v7.ui.TextField();
			
				txtRemarks.setWidth("150px");
				if(null != dto.getFileUploadRemarks()){
					txtRemarks.setValue(dto.getFileUploadRemarks());
				}
				System.out.println(String.format("addGeneratedColumn File Remarks[%s]", dto.getFileUploadRemarks()));
				txtRemarks.setReadOnly(true);
				return txtRemarks;
			}
		});
		
		
		table.removeGeneratedColumn("edit");
		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("Edit");
		    	editButton.setData(itemId);
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						PCCUploadDocumentsDTO uploadDTO = (PCCUploadDocumentsDTO) itemId;
						uploadDTO.setIsEdit(true);
							fireViewEvent(ProcessPCCZonalCoordinatorRequestPresenter.PCC_ZC_EDIT_UPLOAD_EVENT, uploadDTO);
			        } 
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
		
		table.removeGeneratedColumn("delete");
		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	PCCUploadDocumentsDTO dto = (PCCUploadDocumentsDTO) itemId;
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final PCCUploadDocumentsDTO currentItemId = (PCCUploadDocumentsDTO) event.getButton().getData();
					
						if (table.getItemIds().size() > 0) {
							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														PCCUploadDocumentsDTO deletedRowDTO =  (PCCUploadDocumentsDTO)currentItemId;
														if(deletedRowDTO.getKey() != null) {
															deletedDocList.add(deletedRowDTO);
														}
														//Removing the document from view
														table.removeItem(currentItemId);
														fireViewEvent(ProcessPCCZonalCoordinatorRequestPresenter.PCC_ZC_DELETE_UPLOAD_EVENT, deletedRowDTO);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
							
						} else {
							HorizontalLayout layout = new HorizontalLayout(new Label("One File is Mandatory."));
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
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
	}

	@Override
	public String textBundlePrefixString() {
		return "pcc-uploaded-documents-";
	}


	@Override
	public void tableSelectHandler(PCCUploadDocumentsDTO t) {
		// TODO Auto-generated method stub
		
	}

	 public List<PCCUploadDocumentsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<PCCUploadDocumentsDTO> itemIds =(List<PCCUploadDocumentsDTO>) this.table.getItemIds();
	    	return itemIds;
	    }


	 public List<PCCUploadDocumentsDTO> getdeletedList(){
		 
		 return deletedDocList;
	 }
	



}
