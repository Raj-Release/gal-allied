package com.shaic.claim.intimation.unlock;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class SearchUnlockIntimationTable extends GBaseTable<SearchUnlockIntimationTableDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","stage","lockedBy"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchUnlockIntimationTableDTO>(SearchUnlockIntimationTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("305px");
		
		
		table.removeGeneratedColumn("unlockIntimation");
		table.addGeneratedColumn("unlockIntimation",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button unlockIntimationButton = new Button("Unlock Intimation");
						unlockIntimationButton.setData(itemId);
					

		

						unlockIntimationButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										//final SearchUnlockIntimationTableDTO searchUnlockIntimationTableDTO = (SearchUnlockIntimationTableDTO) itemId;
										//BPMClientContext.setActiveOrDeactiveClaim(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, searchUnlockIntimationTableDTO.getHumanTaskDetail().getNumber(), SHAConstants.SYS_RELEASE);
										alertMessage("Intimation Unlocked");
									}
								});
						unlockIntimationButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return unlockIntimationButton;
					}
				});
		
		table.setColumnHeader("unlockIntimation", "Unlock Intimation");
	}
	
	public void alertMessage(String message) {

   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+ message + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				 dialog.close();
//				 fireViewEvent(MenuItemBean.UNLOCK_INTIMATION,null);
			}
		});
	}

	@Override
	public void tableSelectHandler(
			SearchUnlockIntimationTableDTO t) {
		
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
	public String textBundlePrefixString() {
		
		return "search-unlock-intimation-";
	}
	
	
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}