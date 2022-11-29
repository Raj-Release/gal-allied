package com.shaic.claim.lumen.components;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class LapseTable extends GBaseTable<LapseDTO>{

	private ParticipantOtherTable parTable;	

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"add","empName"};

	private boolean buttonEnableFlag = true; 

	public boolean isButtonEnableFlag() {
		return buttonEnableFlag;
	}

	public void setButtonEnableFlag(boolean buttonEnableFlag) {
		this.buttonEnableFlag = buttonEnableFlag;
	}

	public ParticipantOtherTable getParTable() {
		return parTable;
	}

	public void setParTable(ParticipantOtherTable parTable) {
		this.parTable = parTable;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {		
		table.setContainerDataSource(new BeanItemContainer<LapseDTO>(LapseDTO.class));
		table.removeGeneratedColumn("add");
		table.addGeneratedColumn("add",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button pushButton = new Button();
				pushButton.setIcon(new ThemeResource("images/arrowLeft.png"));
				pushButton.setWidth("-1");
				pushButton.setDescription("Move to Participants (Others) list");
				pushButton.setEnabled(isButtonEnableFlag());
				pushButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						getParTable().getTable().addItem(new ParticipantsDTO(((LapseDTO)itemId).getEmpName()));
						table.removeItem(itemId);
					}
				});
				pushButton.addStyleName(BaseTheme.BUTTON_LINK);
				return pushButton;
			}
		});

		table.setColumnHeader("add", "");
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		//table.setColumnWidth("add", 67);
		//		table.setColumnWidth("add", -1);
		//		table.setHeight(30, Unit.EM);
		//		table.setWidth(35, Unit.EM);
		//		table.setHeight("2%");
		table.setPageLength(6);
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(LapseDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-lapse-participants-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=6){
			table.setPageLength(6);
		}
	}

	/*public void makeButtonsEnabled(boolean flag){
		pushButton.setEnabled(flag);
	}*/

}
