package com.shaic.claim.specialistopinion.view;

import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.fileUpload.MultipleUploadDocumentPageUI;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewSpecialistOpinionTable extends
		GBaseTable<ViewSpecialistOpinionTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	@Inject
	private MultipleUploadDocumentPageUI multipleUploadDocumentPageUI;
	
	////private static Window popup;

	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber",
			"requestedDate", "repliedDate", "specialistType",
			"specialistDrNameId","requestorNameId", "requestorRemarks", "viewFile",
			"specialistRemarks" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ViewSpecialistOpinionTableDTO>(
				ViewSpecialistOpinionTableDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
		table.setPageLength(table.size()+4);
		
		table.removeGeneratedColumn("viewFile");
		table.addGeneratedColumn("viewFile",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View File");
						final ViewSpecialistOpinionTableDTO specialistDTO = (ViewSpecialistOpinionTableDTO) itemId;

//						if(specialistDTO.getFileName() != null){
//							button.setCaption(specialistDTO.getFileName());
//						}else{
//							button.setEnabled(false);
//							button.setCaption("NIL");
//						}

						button.addClickListener(new Button.ClickListener()
						{
							@Override
							public void buttonClick(ClickEvent event) {
								
								Window popup = new com.vaadin.ui.Window();
								popup.setWidth("75%");
								popup.setHeight("90%");
//								if(specialistDTO != null && specialistDTO.getFileName() != null && specialistDTO.getFileToken() != null){
//									fileViewUI.init(popup,specialistDTO.getFileName(), specialistDTO.getFileToken());
									multipleUploadDocumentPageUI.init(SHAConstants.SPECIALIST_SCREEN, specialistDTO.getKey(), true);
									multipleUploadDocumentPageUI.setCurrentPage(getUI().getPage());
//								}
								popup.setContent(multipleUploadDocumentPageUI);
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
		
		
		
	}

	@Override
	public void tableSelectHandler(ViewSpecialistOpinionTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-specialist-opinion-";
	}

}
