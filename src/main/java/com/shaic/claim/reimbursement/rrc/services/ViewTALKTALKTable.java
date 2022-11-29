package com.shaic.claim.reimbursement.rrc.services;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewTALKTALKTable extends GBaseTable<ExtraEmployeeEffortDTO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] { "slNo", "category","subCategory","sourceOfIdentification","talkSpokento","talkSpokenDate","talkMobto"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ExtraEmployeeEffortDTO>(ExtraEmployeeEffortDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		table.removeGeneratedColumn("viewRemarks");
		table.addGeneratedColumn("viewRemarks", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				ExtraEmployeeEffortDTO effortDTO = (ExtraEmployeeEffortDTO)itemId;
				final Button view = new Button("View Remarks");
				view.addStyleName(ValoTheme.BUTTON_LINK);
				view.setWidth("-1px");
				view.setHeight("-10px");
				view.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						VerticalLayout vLayout =  new VerticalLayout();
						//vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
						//vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
						vLayout.setHeight("30%");
						vLayout.setMargin(true);
						vLayout.setSpacing(true);
						final TextArea txtArea = new TextArea();
						txtArea.setValue(effortDTO.getRemarks());
						txtArea.setNullRepresentation("");
						//txtArea.setSizeFull();
						txtArea.setWidth("20%");
						txtArea.setHeight("10%");						
						txtArea.setRows(10);
						Button okBtn = new Button("OK");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						vLayout.addComponent(txtArea);
						vLayout.addComponent(okBtn);
						vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

						final Window dialog = new Window();

						dialog.setCaption("Talk, Talk, Talk Remarks");
						//dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
						dialog.setWidth("45%");
						dialog.setHeight("30%");
						dialog.setClosable(true);
						dialog.setContent(vLayout);
						dialog.setResizable(true);
						dialog.setModal(true);
						dialog.setDraggable(true);

						dialog.addCloseListener(new Window.CloseListener() {
							@Override
							public void windowClose(CloseEvent e) {
								dialog.close();
							}
						});

						if(UI.getCurrent().getPage().getWebBrowser().isIE()) {
							dialog.setPositionX(250);
							dialog.setPositionY(100);
						}
						UI.getCurrent().addWindow(dialog);
						okBtn.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void buttonClick(ClickEvent event) {
								dialog.close();
							}
						});	

					}
				});
				return view;
			}
		});

		table.setColumnHeader("viewRemarks", "View Remarks");


	}

	@Override
	public void tableSelectHandler(ExtraEmployeeEffortDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "talk-talk-details-";
	}
}
