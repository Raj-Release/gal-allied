package com.shaic.reimbursement.claims_alert.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.domain.MasterService;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.themes.Reindeer;

public class ClaimsAlertUploadDocumentTable extends GBaseTable<ClaimsAlertDocsDTO> {

	private static final long serialVersionUID = 1L;

	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@EJB
	private ClaimsAlertMasterService claimsAlertMasterService;

	private Page currentPage;
	
	private ClaimsAlertTableDTO bean;
	
	private boolean isView;

	@Override
	public void removeRow() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ClaimsAlertDocsDTO>(ClaimsAlertDocsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sNo","fileName"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnHeader("fileName", "File Name");
		table.setColumnHeader("ViewDocument","View Document");
		
		table.setColumnAlignment("slNo", Align.CENTER);
		table.setColumnAlignment("fileName", Align.CENTER);
		table.setColumnAlignment("ViewDocument", Align.CENTER);
		table.setColumnAlignment("delete", Align.CENTER);

		table.removeGeneratedColumn("ViewDocument");
		table.addGeneratedColumn("ViewDocument", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				
				final Button viewDocsButton = new Button("View Document");
				viewDocsButton.setData(itemId);

				final ClaimsAlertDocsDTO docsDTO = (ClaimsAlertDocsDTO) itemId;

				viewDocsButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(
							ClickEvent event) {			
						Window popup = new com.vaadin.ui.Window();
						popup.setWidth("75%");
						popup.setHeight("90%");
						if(docsDTO != null && docsDTO.getFileName() != null && docsDTO.getFileToken() != null){
							fileViewUI.setCurrentPage(currentPage);
							fileViewUI.init(popup,docsDTO.getFileName(),docsDTO.getFileToken());
						}
						popup.setContent(fileViewUI);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
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
				viewDocsButton.addStyleName(BaseTheme.BUTTON_LINK);
				return viewDocsButton;
			}
		});

		if(!isView){
			table.removeGeneratedColumn("delete");
			table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
					final Button deleteButton = new Button("Delete");
					deleteButton.setData(itemId);
					deleteButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;
						public void buttonClick(final ClickEvent event) {

							ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
									"Are you sure you want to delete ?",
									"No", "Yes", new ConfirmDialog.Listener() {

								private static final long serialVersionUID = 1L;
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isCanceled() && !dialog.isConfirmed()) {
										Object currentItemId = event.getButton().getData();
										ClaimsAlertDocsDTO docsDTO = (ClaimsAlertDocsDTO) currentItemId;
										if(null != docsDTO.getKey())
										{
											claimsAlertMasterService.deleteClaimsDocsDetails(docsDTO);
										}
										table.removeItem(currentItemId);
										tableSelectHandler(docsDTO);
										bean.setDocsDTOs(getValues());
									}
								}
							});
							dialog.setStyleName(Reindeer.WINDOW_BLACK);
						} 
					});
					deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
					return deleteButton;
				}
			});
		}
		
	}

	@Override
	public void tableSelectHandler(ClaimsAlertDocsDTO t) {
		// TODO Auto-generated method stub

	}

	public void setCurrentPage(Page currentPage){
		this.currentPage = currentPage;
	}

	@Override
	public String textBundlePrefixString() {
		return "claim-upload-table-";
	}
	
	protected void setTablesize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ClaimsAlertDocsDTO> getValues() {

		List<ClaimsAlertDocsDTO> itemIds = (List<ClaimsAlertDocsDTO>) this.table.getItemIds() ;
		return itemIds;
	}
	
	public void setBean(ClaimsAlertTableDTO alertTableDTO,boolean isView){
		this.bean = alertTableDTO;
		this.isView = isView;
	}

}
