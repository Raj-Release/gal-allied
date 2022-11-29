package com.shaic.claim.translationmiscrequest.view;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyService;
import com.shaic.domain.preauth.Coordinator;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewTranslationMiscRequestTable extends GBaseTable<ViewTranslationMiscRequestTableDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Object[] COLUMN_HEADER = new Object[]{"serialNumber","requestedDate","repliedDate","requestType","requestorRole",
		"requestorNameID","requestorRemarks","coordinatorRepliedID","coordinatorRemarks"}; 
	
	@EJB
	private ViewCoOrdinatorReplyService coordinatorService;
	
	////private static Window popup;
	
	@Inject
	private UploadedFileViewUI fileViewUI;

	@Override
	public void removeRow() {
				
	}

	//@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ViewTranslationMiscRequestTableDTO>(ViewTranslationMiscRequestTableDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);		
		table.setWidth("100%");
		table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);
		table.setPageLength(5);
		addGeneratorColumn();
	}

	@Override
	public void tableSelectHandler(ViewTranslationMiscRequestTableDTO t) {
		// TODO: 
	}

	@Override
	public String textBundlePrefixString() {
		return "view-translation-or-miscrequest-";
	}
	
	private void addGeneratorColumn(){
		
		table.removeGeneratedColumn("viewfile");
		table.addGeneratedColumn("viewfile", new Table.ColumnGenerator() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button button = new Button();
				ViewTranslationMiscRequestTableDTO coordinatorDTO = (ViewTranslationMiscRequestTableDTO) itemId;
				
				final Long key = coordinatorDTO.getKey();
				
				final Coordinator coordinator = setFileName(key, button);
				button.addClickListener(new Button.ClickListener()
				{
					@Override
					public void buttonClick(ClickEvent event) {
						if (null != coordinator.getFileName()){
						Window popup = new com.vaadin.ui.Window();
						popup.setWidth("75%");
						popup.setHeight("35%");
						fileViewUI.init(popup,coordinator.getFileName(), coordinator.getFileToken());
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
						
						
					}else {
						getErrorMessage("File not Available");
					}
				} 

				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
    }
	
    public Coordinator setFileName(Long key,Button button){
		
		Coordinator coordinator = coordinatorService.getCoordinator(key);
		if(coordinator != null){
		button.setCaption(coordinator.getFileName());
		return coordinator;
		}
		else{
			return null;
		}
		
	}
    public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
}