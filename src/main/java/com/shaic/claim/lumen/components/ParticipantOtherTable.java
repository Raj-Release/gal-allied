package com.shaic.claim.lumen.components;

import java.util.HashSet;
import java.util.Set;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class ParticipantOtherTable extends GBaseTable<ParticipantsDTO>{

	private LapseTable lapTable;

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"Remove","empName","Lapse"};
	
	private boolean buttonEnableFlag = true; 
	
	private Set<String> listOfDeletedParticipants = new HashSet<String>();
	
	public boolean isButtonEnableFlag() {
		return buttonEnableFlag;
	}

	public void setButtonEnableFlag(boolean buttonEnableFlag) {
		this.buttonEnableFlag = buttonEnableFlag;
	}
	
	public Set<String> getListOfDeletedParticipants() {
		return listOfDeletedParticipants;
	}

	public void setListOfDeletedParticipants(Set<String> listOfDeletedParticipants) {
		this.listOfDeletedParticipants = listOfDeletedParticipants;
	}

	public LapseTable getLapTable() {
		return lapTable;
	}

	public void setLapTable(LapseTable lapTable) {
		this.lapTable = lapTable;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();	
	}
	
	@Override
	public void initTable() {		
		table.setContainerDataSource(new BeanItemContainer<ParticipantsDTO>(ParticipantsDTO.class));
		table.removeGeneratedColumn("Remove");
		table.addGeneratedColumn("Remove", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button deleteButton = new Button();
				deleteButton.setIcon(new ThemeResource("images/deletebtn.png"));
				deleteButton.setWidth("-1");
				deleteButton.setDescription("Delete Participant");
				deleteButton.setEnabled(isButtonEnableFlag());
				deleteButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						String deletedParName = ((ParticipantsDTO)itemId).getEmpName();
						//System.out.println("Delete itemId ParticipantOtherTable : "+deletedParName);
						listOfDeletedParticipants.add(deletedParName);
						table.removeItem(itemId);
					} 
				});
				deleteButton.addStyleName(BaseTheme.BUTTON_LINK);
				
				return deleteButton;
			};
		});
		table.setColumnHeader("Remove", "");

		table.removeGeneratedColumn("Lapse");
		table.addGeneratedColumn("Lapse", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button pushButton = new Button();
				pushButton.setIcon(new ThemeResource("images/arrowRight.png"));
				pushButton.setWidth("-1");
				pushButton.setDescription("Move to Lapse/Offender list");
				pushButton.setEnabled(isButtonEnableFlag());
				pushButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						getLapTable().getTable().addItem(new LapseDTO(((ParticipantsDTO)itemId).getEmpName()));
						table.removeItem(itemId);
					} 
				});
				pushButton.addStyleName(BaseTheme.BUTTON_LINK);
				return pushButton;
			};
		});
		table.setColumnHeader("Lapse", "");		
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(6);
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(ParticipantsDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-participants-others-";
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
		deleteButton.setEnabled(flag);
	}*/

}
