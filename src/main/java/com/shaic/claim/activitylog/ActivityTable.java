package com.shaic.claim.activitylog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ActivityTable  extends ViewComponent {
	private static final long serialVersionUID = 1L;

	private Map<ActivityTableDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ActivityTableDto, HashMap<String, AbstractField<?>>>();
	protected BeanItemContainer<ActivityTableDto> data = new BeanItemContainer<ActivityTableDto>(ActivityTableDto.class);
	
	private Table table;
	private Button btnAdd;
	private List<String> errorMessages;
	private static Validator validator;
	public List<ActivityTableDto> deletedDTO;
	
	public void init(String presenterString, Boolean showPagerFlag, BeanItemContainer<SelectValue> activityList) {
		deletedDTO = new ArrayList<ActivityTableDto>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		btnAdd.setDescription("Add a new activity");
		btnAdd.setData(activityList);
		
		initTable();
		
		VerticalLayout btnLayout = new VerticalLayout(btnAdd, table);
		btnLayout.setSpacing(false);
		btnLayout.setMargin(false);
		btnLayout.setWidth("100%");
		btnLayout.setHeight("100%");
		btnLayout.setSizeFull();
		btnLayout.setComponentAlignment(btnAdd, Alignment.TOP_RIGHT);
		btnLayout.setComponentAlignment(table, Alignment.TOP_LEFT);
		addListener();

		setSizeFull();
		setCompositionRoot(btnLayout);

		//Vaadin8-setImmediate() setImmediate(true);

//		setImmediate(true);

	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.setSizeFull();
		table.setData(data);

		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				table.setColumnWidth(columnId, 80);
				
				final Button deleteButton = new Button("Delete");
				ActivityTableDto dto = (ActivityTableDto) itemId;
				deleteButton.setEnabled(true);
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					
					public void buttonClick(ClickEvent event) {
						final ActivityTableDto currentItemId = (ActivityTableDto) event.getButton().getData();
						ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Do you want to delete ?",
								"No", "Yes", new ConfirmDialog.Listener() {
							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									ActivityTableDto dto =  (ActivityTableDto)currentItemId;
									deletedDTO.add(dto);
									table.removeItem(currentItemId);
								}
							}
						});
						dialog.setClosable(false);
					}
				});
				return deleteButton;
			}
		});
		
		table.setVisibleColumns(new Object[] {"activityName", "activityDesc", "Delete"});
		table.setColumnHeader("activityName", "Activity");
		table.setColumnHeader("activityDesc", "Activity Description");
		table.setEditable(true);

		table.setColumnAlignment("activityName", Align.CENTER);
		table.setColumnAlignment("activityDesc", Align.CENTER);
		table.setColumnAlignment("Delete", Align.CENTER);
		table.setColumnWidth("activityName", 320);
		
		// Use a custom field factory to set the edit fields as immediate
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//Vaadin8-setImmediate() table.setImmediate(true);
		table.setSizeFull();
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				final BeanItemContainer<SelectValue> activityList = (BeanItemContainer<SelectValue>) event.getButton().getData();
				
				ActivityTableDto tableDTO = new ActivityTableDto(activityList);
				BeanItem<ActivityTableDto> addItem = data.addItem(tableDTO);
			}
		});
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		@Override
		public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
			final ActivityTableDto activityTableDto = (ActivityTableDto) itemId;
			
			Map<String, AbstractField<?>> tableRow = null;
			if(tableItem.get(activityTableDto) == null) {
				tableItem.put(activityTableDto, new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(activityTableDto);
			
			if ("activityName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("activityName", box);
				box.setData(activityTableDto);
				box.setWidth("300px");
				
				setActivityLogValues(box, activityTableDto);
				if(activityTableDto != null && activityTableDto.getActivityName() != null) {
					box.setDescription(activityTableDto.getActivityName().getValue());
				}
				return box;
			} else if ("activityDesc".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setEnabled(true);
				field.setWidth("100%");
				field.setRows(2);
				field.setNullRepresentation("");
				tableRow.put("activityDesc", field);
				field.setMaxLength(4000);
				field.setData(activityTableDto);
				//Vaadin8-setImmediate() field.setImmediate(true);
				addDetailPopupForActivityDesc(field, null);
				
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				field.setSizeFull();
				return field;
			} else {
				Field<?> field = super.createField(container, itemId, propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(true);
				return field;
			}
		}
	}

	public List<ActivityTableDto> getValues() {
		@SuppressWarnings("unchecked")
		List<ActivityTableDto> itemIds = (List<ActivityTableDto>) this.table.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<ActivityTableDto>();
		}
		return itemIds;
	}

	public void removeAllItems() {
		table.removeAllItems();
	}
	
	public void addBeanToList(ActivityTableDto tableDTO) {
		data.addItem(tableDTO);
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	private void addDetailPopupForActivityDesc(TextArea txtFld, final  Listener listener) {
		ShortcutListener enterShortCut = new ShortcutListener("activityDesc", ShortcutAction.KeyCode.F8, null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForDesc(txtFld, getShortCutListenerForDesc(txtFld));
	}
	
	public  void handleShortcutForDesc(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
			}
		});
		textField.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void blur(BlurEvent event) {
				textField.removeShortcutListener(shortcutListener);
			}
		});
	}
	
	private ShortcutListener getShortCutListenerForDesc(final TextArea txtFld) {
		ShortcutListener listener =  new ShortcutListener("activityDesc",KeyCodes.KEY_F8,null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				ActivityTableDto  activityTableDto = (ActivityTableDto) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setNullRepresentation("");
				txtArea.setValue(txtFld.getValue());
				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue().trim());
					}
				});
				
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setRows(25);
				if(txtArea.getValue() != null){
					activityTableDto.setActivityDesc(txtArea.getValue());	
				}
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				dialog.setCaption("Activity Description");
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(false);
				
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.setDraggable(true);
				
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
	
	public void setTableList(List<ActivityTableDto> activityTableDtoList) {
		table.removeAllItems();
		if(null != activityTableDtoList && !activityTableDtoList.isEmpty()) {
			for (ActivityTableDto tableDto : activityTableDtoList) {
				table.addItem(tableDto);
			}
		}
		table.setPageLength(table.getItemIds().size());
	}
	
	public void setActivityLogValues(GComboBox activityCombo, ActivityTableDto dto) {
		activityCombo.setContainerDataSource((BeanItemContainer<SelectValue>)dto.getActivityList());
		activityCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		activityCombo.setItemCaptionPropertyId("value");
		if(dto != null) {
			activityCombo.setValue(dto.getActivityName());
		}
	}
	
	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ActivityTableDto> itemIds = (Collection<ActivityTableDto>) table.getItemIds();
		for (ActivityTableDto bean : itemIds) {			
			Set<ConstraintViolation<ActivityTableDto>> validate = validator.validate(bean);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ActivityTableDto> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
	
	public List<ActivityTableDto> getDeletedTaskList() {
		return deletedDTO;
	}
}
