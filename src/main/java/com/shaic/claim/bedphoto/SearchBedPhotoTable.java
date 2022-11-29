package com.shaic.claim.bedphoto;


import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SearchBedPhotoTable extends GBaseTable<SearchBedPhotoTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","policyNo","insuredPatientName","hospitalName","dateOfAdmsissionStr","dateOfDischargeStr"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchBedPhotoTableDTO>(SearchBedPhotoTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
		table.removeGeneratedColumn("updateBedPhoto");
		table.addGeneratedColumn("updateBedPhoto",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button updateBedPhotoButton = new Button("Upload BedPhoto");
						updateBedPhotoButton.addStyleName(ValoTheme.BUTTON_LINK);
						updateBedPhotoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						updateBedPhotoButton.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								SearchBedPhotoTableDTO tableDTO = (SearchBedPhotoTableDTO) itemId;
								uploadBedPhoto(tableDTO);
							}

						});
						
						return updateBedPhotoButton;
					}
				});
		

		table.setHeight("338px");
		table.setSizeFull();
	}
	
	public void uploadBedPhoto(SearchBedPhotoTableDTO dto){

		
		 VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(dto.getUsername(),dto.getPassword(),dto.getTaskNumber(), session);
			try{
					if(! isActiveHumanTask){
					
						SHAUtils.setActiveOrDeactiveClaim(dto.getUsername(),dto.getPassword(),dto.getTaskNumber(),session);
				
						fireViewEvent(MenuPresenter.UPLOAD_BED_PHOTO_WIZARD, dto);
					}else{
						getErrorMessage("This record is already opened by another user");
					}
			}catch(Exception e){
				
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(dto.getUsername(), dto.getPassword(), existingTaskNumber, session);
				
				e.printStackTrace();
			}
		
	
	}
	
	public void getErrorMessage(String eMsg){
		
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

	@Override
	public void tableSelectHandler(SearchBedPhotoTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "upload-bed-photo-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(7);
		}
		
	}


}
