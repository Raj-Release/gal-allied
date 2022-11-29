package com.shaic.claim.intimation.create;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.components.ParticipantsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class CallRecordingDetailTable extends GBaseTable<CallRecordingTableDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"sno","callDateTime","Play"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {		
		table.setContainerDataSource(new BeanItemContainer<CallRecordingTableDTO>(CallRecordingTableDTO.class));
		
		table.removeGeneratedColumn("Play");
		table.addGeneratedColumn("Play", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				Button deleteButton = new Button();
				deleteButton.setIcon(new ThemeResource("images/play_icon_30.png"));
				deleteButton.setWidth("-1");
				deleteButton.setDescription("Click Here To Play Recording");
				deleteButton.setEnabled(Boolean.TRUE);
				deleteButton.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						String audio_url = ((CallRecordingTableDTO)itemId).getAudioUrl();
						getUI().getPage().open(audio_url, "_blank",300,200,BorderStyle.NONE);
					} 
				});
				deleteButton.addStyleName(BaseTheme.BUTTON_LINK);
				
				return deleteButton;
			};
		});
		table.setColumnHeader("Play", "");
		
		table.setColumnAlignment("sno", Table.ALIGN_CENTER);
		table.setColumnAlignment("callDateTime", Table.ALIGN_CENTER);
		table.setColumnAlignment("Play", Table.ALIGN_CENTER);
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(5);
	}

	@Override
	public void tableSelectHandler(CallRecordingTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "call-recording-view-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
	}

}
