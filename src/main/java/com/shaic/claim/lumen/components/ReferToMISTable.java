package com.shaic.claim.lumen.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
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
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ReferToMISTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<MISDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<MISDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<MISDTO> data = new BeanItemContainer<MISDTO>(MISDTO.class);

	private Table table;
	
	private Button btnAdd;

	MISDTO bean;

	public Object[] VISIBLE_COLUMNS = new Object[] {"sno","queryContent", "Delete"};

	public void init(MISDTO bean) {
		this.bean = bean;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(5);

		
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						if (table.getItemIds().size() > 0) {							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												private static final long serialVersionUID = 1L;
												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														table.removeItem(itemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
						}
					}
				});
				return deleteButton;
			}			
		});

		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("queryContent", "Query Description");
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setEditable(true);
		table.setSelectable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		
		
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;
			@Override
			public void buttonClick(ClickEvent event) {
				List<MISDTO> listOfItems = getValues();
				MISDTO newItem = new MISDTO();
				newItem.setSno(listOfItems.size()+1);
				newItem.setQueryContent("");
				addBeanToList(newItem);
			}
		});

		layout.addComponent(btnLayout);
		layout.setMargin(true);		
		layout.addComponent(table);
	}

	public void setVisibleColumns() {
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			MISDTO misDTO = (MISDTO) itemId;
			TextField queryfield = null;
			TextField snofield = null;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(misDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(misDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(misDTO);
			}

			if("queryContent".equals(propertyId)) {
				queryfield= new TextField();
				queryfield.setNullRepresentation("");
				queryfield.setData(misDTO);
				queryfield.setWidth("1000px");
				queryfield.setMaxLength(4000);
				handleTextAreaPopup(queryfield, null);
				tableRow.put("queryRemarks", queryfield);
				return queryfield;
			}else {
				snofield = new TextField();
				snofield.setWidth("50px");
				snofield.setReadOnly(true);
				snofield.setData(misDTO);
				tableRow.put("sno", snofield);
				return snofield;
			}
		}
	}


	public void addBeanToList(MISDTO misDTO) {
		data.addBean(misDTO);
	}
	public void addList(List<MISDTO> misDTO) {
		for (MISDTO misDTO2 : misDTO) {
			data.addBean(misDTO2);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MISDTO> getValues() {
		List<MISDTO> itemIds = (List<MISDTO>) this.table.getItemIds() ;
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
				if(txtFld.isReadOnly()){
					txtArea.setReadOnly(true);
				}else{
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

				String strCaption = "Query Description";

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
