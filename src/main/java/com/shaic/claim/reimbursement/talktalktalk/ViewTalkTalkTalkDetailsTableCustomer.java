package com.shaic.claim.reimbursement.talktalktalk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.talktalktalk.TalkTalkTalkCategoryListenerTable.ImmediateFieldFactory;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.Align;

@UIScoped
public class ViewTalkTalkTalkDetailsTableCustomer extends GBaseTable<InitiateTalkTalkTalkDTO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] { "serialNumber", "typeOfCommunication","talkSpokento","talkSpokenDate","talkMobto","remarks","processingUserName"};
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	/*	if(table!=null){
			table.clear();
		}*/
	}

	
	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<InitiateTalkTalkTalkDTO>(InitiateTalkTalkTalkDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber", "talkSpokenDate","callStartTime","callEndTime","callDuration","talkSpokento","talkMobto","processingUserName","remarks"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
//		table.setColumnWidth("serialNo",80);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						InitiateTalkTalkTalkDTO dto = (InitiateTalkTalkTalkDTO)itemId;
                        final String name = dto.getFileName();
						Button button = button = new Button();
						if(dto.getFileName() != null){
							button.setCaption("Click the link to hear Audio");
						}
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								if(dto.getFileName() != null){
									getUI().getPage().open(dto.getFileName(), "_blank", 650, 250,
											BorderStyle.NONE);
								}
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
				    	button.setWidth("200px");
						return button;
					}
				});
	}
	

	@Override
	public void tableSelectHandler(InitiateTalkTalkTalkDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "view-talk-talk-details-";
	}
}

