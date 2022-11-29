package com.shaic.claim.fileUpload;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry.OPRODAndBillEntryPagePresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.paclaim.rod.createrod.search.PACreateRODUploadDocumentsPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class WaivedQueryUploadDocumentTable extends GBaseTable<MultipleUploadDocumentDTO>{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Page currentPage;
	
	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	
	private WaivedQueryUploadDocumentPageUI queryUploadDocumentPageUI;
	
	@EJB
	private ReimbursementService reimbService;
	
	public void setQueryUploadDocumentPageUI(
			WaivedQueryUploadDocumentPageUI queryUploadDocumentPageUI) {
		this.queryUploadDocumentPageUI = queryUploadDocumentPageUI;
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
        table.setContainerDataSource(new BeanItemContainer<MultipleUploadDocumentDTO>(MultipleUploadDocumentDTO.class));
        Object[] NATURAL_COL_ORDER = new Object[] {"sNo","fileName","uploadLetterDate","noOfPages"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		generateColumns();
		table.setColumnHeader("fileName", "File Name");
		table.setColumnHeader("uploadLetterDate", "Upload Date");
		table.setColumnHeader("noOfPages", "No Of Pages");
		table.setColumnHeader("edit", "Edit");
		table.setColumnHeader("delete", "Delete");
		table.setHeight("290px");
		
		
		/*table.removeGeneratedColumn("ViewDocument");
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
										*//**
										 * 
										 *//*
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
		});*/
		
		//table.setColumnHeader("ViewDocument","View Document");

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
		return "waived-query-upload-table-";
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

		private void generateColumns() {
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
							MultipleUploadDocumentDTO uploadDTO = (MultipleUploadDocumentDTO) itemId;
							//uploadDTO.setIsEdit(true);
							uploadDTO.setIsEdit(true);
							queryUploadDocumentPageUI.setEditValues(uploadDTO);
							
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
			    	deleteButton.setData(itemId);
			    	deleteButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {
				        	final Object currentItemId = event.getButton().getData();
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														table.removeItem(currentItemId);
														MultipleUploadDocumentDTO uploadDocsDTO = (MultipleUploadDocumentDTO) itemId;
														tableSelectHandler(uploadDocsDTO);
														if(null != uploadDocsDTO.getDocKey())
														{
															reimbService.saveDeletedDocumentValues(uploadDocsDTO);
														}
													} 
												}
											});
							
							
				        } 
				    });
			    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
			        return deleteButton;
			      }
			    });
		}
		
}
