package com.shaic.claim.pancard.search.pages;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchUploadPanCardTable extends GBaseTable<SearchUploadPanCardTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "intimationNo", "policyNo", 
		"proposerName", "policyStartDate", "policyEndDate"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchUploadPanCardTableDTO>(SearchUploadPanCardTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
		table.removeGeneratedColumn("updatePan");
		table.addGeneratedColumn("updatePan",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button updatePanDetailsButton = new Button("Update Pan Details");
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						updatePanDetailsButton.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								SearchUploadPanCardTableDTO tableDTO = (SearchUploadPanCardTableDTO) itemId;
								updatePanClick(tableDTO);
							}

						});
						
						return updatePanDetailsButton;
					}
				});
		
		
		//table.setColumnWidth("hospitalAddress", 350);
		//table.setColumnWidth("hospitalCity", 250);
		table.setHeight("331px");
	}

	@Override
	public void tableSelectHandler(
			SearchUploadPanCardTableDTO t) {}
	
	public void updatePanClick(SearchUploadPanCardTableDTO t){

		
		 VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			try{
					if(! isActiveHumanTask){
					
						SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
				
						fireViewEvent(MenuPresenter.UPLOAD_PAN_CARD, t);
					}else{
						getErrorMessage("This record is already opened by another user");
					}
			}catch(Exception e){
				
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				
				e.printStackTrace();
			}
		
	
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-upload-pancard-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
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

}
