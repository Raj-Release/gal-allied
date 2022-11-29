package com.shaic.claim.lumen.querytomis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.claim.lumen.LumenQueryDetailsDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class MISQueryReplyTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<LumenQueryDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<LumenQueryDetailsDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<LumenQueryDetailsDTO> data = new BeanItemContainer<LumenQueryDetailsDTO>(LumenQueryDetailsDTO.class);

	private Table table;

	LumenQueryDetailsDTO bean;

	public Object[] VISIBLE_COLUMNS = new Object[] {"sno","queryRemarks","replyRemarks"};

	public void init(LumenQueryDetailsDTO bean) {
		this.bean = bean;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		table = new Table("Reply Details", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(5);
		table.setVisibleColumns(VISIBLE_COLUMNS);

		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("queryRemarks", "Query Description");
		table.setColumnHeader("replyRemarks", "Reply Remarks");
		table.setEditable(true);
		table.setSelectable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		layout.addComponent(table);
	}

	public void setVisibleColumns() {
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			LumenQueryDetailsDTO queryDetailsTableDto = (LumenQueryDetailsDTO) itemId;
			TextField queryfield = null;
			TextField remarksfield = null;
			TextField snofield = null;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(queryDetailsTableDto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(queryDetailsTableDto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(queryDetailsTableDto);
			}

			if("queryRemarks".equals(propertyId)) {
				queryfield= new TextField();
				queryfield.setNullRepresentation("");
				queryfield.setData(queryDetailsTableDto);
				queryfield.setWidth("750px");
				queryfield.setMaxLength(4000);
				handleTextAreaPopup(queryfield, null);
				queryfield.setReadOnly(true);
				tableRow.put("queryRemarks", queryfield);
				return queryfield;
			} else if("replyRemarks".equals(propertyId)) {
				remarksfield = new TextField();
				remarksfield.setNullRepresentation("");
				remarksfield.setData(queryDetailsTableDto);
				remarksfield.setWidth("750px");
				remarksfield.setMaxLength(4000);
				tableRow.put("replyRemarks", remarksfield);
				handleTextAreaPopup(remarksfield, null);
				return remarksfield;
			}else {
				snofield = new TextField();
				snofield.setWidth("50px");
				snofield.setReadOnly(true);
				snofield.setData(queryDetailsTableDto);
				tableRow.put("sno", snofield);
				return snofield;
			}
		}
	}


	public void addBeanToList(LumenQueryDetailsDTO diagnosisProcedureTableDTO) {
		data.addBean(diagnosisProcedureTableDTO);
	}
	public void addList(List<LumenQueryDetailsDTO> diagnosisProcedureTableDTO) {
		for (LumenQueryDetailsDTO diagnosisProcedureTableDTO2 : diagnosisProcedureTableDTO) {
			data.addBean(diagnosisProcedureTableDTO2);
		}
	}

	@SuppressWarnings("unchecked")
	public List<LumenQueryDetailsDTO> getValues() {
		List<LumenQueryDetailsDTO> itemIds = (List<LumenQueryDetailsDTO>) this.table.getItemIds() ;
		return itemIds;
	}

	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextField searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		ShortcutListener txtlistener = getShortCutListenerForRemarks(searchField);
		handleShortcutForRedraft(searchField, txtlistener);
	}

	@SuppressWarnings("serial")
	public  void handleShortcutForRedraft(final TextField textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}

	private ShortcutListener getShortCutListenerForRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation", "serial" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				String strCaption = "";
				if(txtFld.isReadOnly()){
					strCaption = "Query Remarks";
					txtArea.setReadOnly(true);
				}else{
					strCaption = "Reply Remarks";
					txtArea.setReadOnly(false);
				}
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

}
