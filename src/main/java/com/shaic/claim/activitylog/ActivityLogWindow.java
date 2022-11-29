package com.shaic.claim.activitylog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ActivityLogService;
import com.shaic.domain.MasterService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.client.StyleConstants;
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
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.ui.Window.ResizeEvent;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class ActivityLogWindow extends Window {
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainVLayout;
	private VerticalLayout subTopVLayout;
	private VerticalLayout subBottomVLayout;
	private VerticalLayout buttonVLayout;
	private VerticalLayout activityTableVLayout;
	private VerticalLayout mainEditVLayout;
	private HorizontalLayout buttonHLayout;
	private Panel mainTopPanel;
	private Panel mainBottomPanel;
	
	private MasterService masterService;
	private ActivityLogService activityLogService;
	
	private Button btnSubmit;
	private Button btnCancel;

	private ComboBox activityLog;
	private TextArea activityDesc;
	
	@Inject
	private ActivityTable activityTable;
	@Inject
	private ActivityLogForm activityLogForm;
	
	private Table table;
	private BeanItemContainer<DoctorActivity> data = new BeanItemContainer<DoctorActivity>(DoctorActivity.class);
	
	private String empId;
	private String empName;
	private String intimationNo;
	
	private Date currentDate;
	private int activityDateEnableDiff = 2;
	private int defaultActivityTableRows = 2;
	private String masterTypeCode = "DOC_ACTVT";
	private StringBuilder errors;
	private boolean isActivityEditMode = false;
	
	private List<ActivityTableDto> activityTableDtoList;
	private ActivityTableDto activityTableDto;
	private BeanItemContainer<SelectValue> activityContainer;
	private DoctorActivity doctorActivityObj;
	
	public void init(MasterService masterService, ActivityLogService activityLogService, String intimationNo, String empId, String empName) {
		this.masterService = masterService;
		this.activityLogService = activityLogService;
		this.intimationNo = intimationNo;
		this.empId = empId;
		this.empName = empName;
		activityTableVLayout = new VerticalLayout();
	}
	
	private void showDefaultActivityTable() {
		isActivityEditMode = false;
		currentDate = new Date();
		activityLogForm.setActivityLogFormValues(intimationNo, empId, empName, currentDate, activityDateEnableDiff);
		
		activityContainer = masterService.getActivityList(masterTypeCode);
		activityTableDtoList = new ArrayList<ActivityTableDto>();
		
		for(int i = 1; i <= defaultActivityTableRows; i ++) {
			activityTableDto = new ActivityTableDto(activityContainer);
			activityTableDtoList.add(activityTableDto);
		}
		activityTable.init("", false, activityContainer);
		activityTable.setTableList(activityTableDtoList);
		activityTable.setWidth(activityTableVLayout.getWidth(), UNITS_PERCENTAGE);
		activityTable.setHeight("100%");
		activityTable.setSizeFull();
		
		activityTableVLayout.removeAllComponents();
		activityTableVLayout.setSizeFull();
		activityTableVLayout.addComponent(activityTable);
	}
	
	public void initView() {
		setCaption("Activity Log");
		setModal(true);
		setClosable(true);
		this.setResizable(true);
		this.setWidth("80%");
		this.setHeight(640, Unit.PIXELS);
		//Vaadin8-setImmediate() this.setImmediate(true);
		
		buildSubTopVLayout();
		buildSubBottomVLayout();
		buildMainVLayout();
		setContent(mainVLayout);
	}
	
	private void loadActivityLogDetailsTable() {
		List<DoctorActivity> activityLogList = activityLogService.getActivityLogByIntimationNo(intimationNo);
		data.removeAllItems();
		data.addAll(activityLogList);
		
		table = new Table("", data);
		table.setPageLength(7);
		table.setHeight("100%");
		table.setSizeFull();
		table.setData(data);
		
        Object[] NATURAL_COL_ORDER = new Object[] {
    		"serialNumber",
    		"activityDate",
    		"createdBy",
    		"activityName",
    		"activityDesc"    		
    	};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.removeGeneratedColumn("serialNumber");
		table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
		    @Override
		    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
		    	table.setColumnWidth(columnId, 50);
		        Container.Indexed container = (Container.Indexed) source.getContainerDataSource();
		        return Integer.toString(container.indexOfId(itemId) + 1);
		    }
		});
		
		table.removeGeneratedColumn("activityDate");
		table.addGeneratedColumn("activityDate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
				table.setColumnWidth(columnId, 160);
		    	DoctorActivity doctorActivity = (DoctorActivity) itemId;
		    	return SHAUtils.formatDoctorActivityDateTime(doctorActivity.getActivityDate());
			}
		});
		
		table.removeGeneratedColumn("createdBy");
		table.addGeneratedColumn("createdBy", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
				table.setColumnWidth(columnId, 250);
				DoctorActivity doctorActivity = (DoctorActivity) itemId;
		        return doctorActivity.getActivityUserId() + " - " + doctorActivity.getActivityUserName();
			}
		});
		
		table.removeGeneratedColumn("activityName");
		table.addGeneratedColumn("activityName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
				table.setColumnWidth(columnId, 300);
		    	DoctorActivity doctorActivity = (DoctorActivity) itemId;
		    	return doctorActivity.getActivityName();
			}
		});
		
		table.removeGeneratedColumn("activityDesc");
		table.addGeneratedColumn("activityDesc", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
				DoctorActivity doctorActivity = (DoctorActivity) itemId;
		        return doctorActivity.getActivityDesc();
			}
		});
		
		table.removeGeneratedColumn("Edit");
		table.addGeneratedColumn("Edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				table.setColumnWidth(columnId, 60);
						
				final Button editButton = new Button("Edit");
				editButton.setEnabled(true);
				editButton.setData(itemId);
				
				DoctorActivity doctorActivity = (DoctorActivity) itemId;
				editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					public void buttonClick(ClickEvent event) {
						final DoctorActivity currentItemId = (DoctorActivity) event.getButton().getData();
						ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Do you want to edit ?",
								"No", "Yes", new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;
							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									editActivityLogDetails(currentItemId);
								}
							}
						});
						dialog.setClosable(false);
					}
				});
				
				// Get today date
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				int todayDate = Integer.parseInt(dateFormat.format(cal.getTime()));

				// Get yesterday date
				cal.add(Calendar.DATE, -1);
				int yesterdayDate = Integer.parseInt(dateFormat.format(cal.getTime()));
				
				// Get activity created date
				Date createdDate = doctorActivity.getActivityDate();
			    int createdDateValue = Integer.parseInt(dateFormat.format(createdDate));

			    // Check edit option visibility based on activity date between today and yesterday. 
			    // And Check edit option visibility based on creator of this record.
			    editButton.setVisible(false);
			    if(((createdDateValue == todayDate) || (createdDateValue == yesterdayDate)) && ((doctorActivity.getActivityUserId().trim()).equals(empId.trim()))) {
			    	editButton.setVisible(true);
			    }
				return editButton;
			}
		});
		
		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("activityDate", "Activity Date");
		table.setColumnHeader("createdBy", "Created By");
		table.setColumnHeader("activityName", "Activity");
		table.setColumnHeader("activityDesc", "Activity Description");
		
		table.setColumnAlignment("activityDate", Align.CENTER);
		table.setColumnAlignment("Edit", Align.CENTER);
	}
	
	protected void setTablesize() {
		table.setPageLength(table.size());
		int length = table.getPageLength();
		if(length >= 7) {
			table.setPageLength(7);
		}
	}
	
	private void buildSubTopVLayout() {
		subTopVLayout = new VerticalLayout();
		subTopVLayout.setWidth("100%");
		subTopVLayout.setSpacing(false);
		subTopVLayout.setMargin(true);
		
		currentDate = new Date();
		activityLogForm.setActivityLogFormValues(intimationNo, empId, empName, currentDate, activityDateEnableDiff);
		subTopVLayout.addComponent(activityLogForm);
		
		showDefaultActivityTable();
		subTopVLayout.addComponent(activityTableVLayout);
		
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("static-access")
			public void buttonClick(ClickEvent event) {
				ActivityLogFormDto activityLogFormDto = new ActivityLogFormDto(empId, empName, intimationNo, activityLogForm.getActivityDate());
				List<ActivityTableDto> tableDtoList = activityTable.getValues();
				
				boolean isSuccess = false;
				if(isActivityEditMode) {
					if((activityLog.getValue() == null) || (activityLog.getValue().toString().trim().isEmpty())) {
						errors.append("Please select activity list");
						errors.append("</br>");						
					} else {
						doctorActivityObj.setActivityDate(activityLogForm.getActivityDate());
						doctorActivityObj.setActivityName(activityLog.getValue().toString());
						doctorActivityObj.setActivityDesc(activityDesc.getValue());
						doctorActivityObj.setModifiedBy(empId);
						doctorActivityObj.setModifiedDate(new Date());
						isSuccess = activityLogService.updateDoctorActivity(doctorActivityObj);	
					}
				} else if(isValid(tableDtoList, activityLogForm.getActivityDate())) {
					isSuccess = activityLogService.saveDoctorActivity(activityLogFormDto, tableDtoList);					
				}

				if(isSuccess) {
					ConfirmDialog cd = ConfirmDialog.show(getUI(), "Success", "Activity log submitted successfully !!!",
							"Ok", "", new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 1L;
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								showDefaultActivityTable();
								loadActivityLogDetailsTable();
							}
						}
					});
					cd.getCancelButton().setVisible(false);
				} else {
					Label label = new Label(errors.toString(), ContentMode.HTML);
					label.setStyleName("errMessage");
					
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);
					
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
			}
		});
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Do you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 1L;
					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							ConfirmDialog cd = ConfirmDialog.show(getUI(), "Alert", "All the entered data will be cleared / unsaved",
									"Ok", "", new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										showDefaultActivityTable();
									}
								}
							});
							cd.getCancelButton().setVisible(false);
						}
					}
				});
				dialog.setClosable(false);
			}
		});

		buttonHLayout = new HorizontalLayout();
		buttonHLayout.setSpacing(true);
		buttonHLayout.setMargin(false);
		buttonHLayout.addComponent(btnSubmit);
		buttonHLayout.addComponent(btnCancel);
		
		buttonVLayout = new VerticalLayout();
		buttonVLayout.setWidth("100%");
		buttonVLayout.addComponent(buttonHLayout);
		buttonVLayout.setComponentAlignment(buttonHLayout, Alignment.BOTTOM_CENTER);
		subTopVLayout.addComponent(buttonVLayout);
		
		mainTopPanel = new Panel();
		mainTopPanel.addStyleName("panelHeader");
		mainTopPanel.addStyleName("g-search-panel");
		mainTopPanel.setContent(subTopVLayout);
	}
	
	private void buildSubBottomVLayout() {
		mainBottomPanel = new Panel();
		mainBottomPanel.addStyleName("panelHeader");
		mainBottomPanel.addStyleName("g-search-panel");
		mainBottomPanel.setCaption("View Activity Details");
		
		loadActivityLogDetailsTable();
		mainBottomPanel.setContent(table);

		VerticalLayout spaceVLayout = new VerticalLayout();
		spaceVLayout.setHeight("10px");
		
		subBottomVLayout = new VerticalLayout();
		subBottomVLayout.setMargin(false);
		subBottomVLayout.addComponent(spaceVLayout);
		subBottomVLayout.addComponent(mainBottomPanel);
	}
	
	private void buildMainVLayout() {
		mainVLayout = new VerticalLayout();
		mainVLayout.setWidth("100%");
		mainVLayout.setMargin(true);
		mainVLayout.setSpacing(true);
		mainVLayout.addComponent(mainTopPanel);
		mainVLayout.addComponent(subBottomVLayout);
	}
	
	private boolean isValid(List<ActivityTableDto> tableDtoList, Date activityDate) {
		errors = new StringBuilder();
		boolean isEmptyActivityDesc = false;
		boolean isEmptyActivityName = false;
		boolean isValidData = true;
		
		for(ActivityTableDto activityTableDto : tableDtoList) {
			if((activityTableDto.getActivityName() == null) || (activityTableDto.getActivityName().getValue().trim().isEmpty())) {
				isEmptyActivityName = true;
			}
			if((activityTableDto.getActivityDesc() == null) || (activityTableDto.getActivityDesc().trim().isEmpty())) {
				isEmptyActivityDesc = true;
			}
		}

		if((activityDate == null) || (activityDate.toString().isEmpty())) {
			errors.append("Please choose activity date");
			errors.append("</br>");
			isValidData = false;
		}
		if(tableDtoList.size() == 0) {	
			errors.append("Please add atleast any one activity");
			errors.append("</br>");
			isValidData = false;
		}
		if(isEmptyActivityName) {
			errors.append("Please select activity list");
			errors.append("</br>");
			isValidData = false;
		}
		if(isEmptyActivityDesc) {
			errors.append("Please enter activity description");
			errors.append("</br>");
			isValidData = false;
		}
		return isValidData;
	}
	
	public void editActivityLogDetails(DoctorActivity doctorActivity) {
		isActivityEditMode = true;
		this.doctorActivityObj = doctorActivity;
		
		activityLogForm.setActivityDate(doctorActivity.getActivityDate());
		
		activityLog = new ComboBox("Activity");
		activityLog.setWidth("250px");
		activityLog.setRequired(true);
		activityLog.setContainerDataSource((BeanItemContainer<SelectValue>) activityContainer);
		activityLog.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		activityLog.setItemCaptionPropertyId("value");
		activityLog.setDescription(doctorActivity.getActivityName());
		
		List<SelectValue> activityList = activityContainer.getItemIds();
		for(SelectValue selectValue : activityList) {
			if(selectValue.getValue().equals(doctorActivity.getActivityName())) {
				activityLog.setValue(selectValue);
				break;
			}
		}
				
		activityDesc = new TextArea("Activity Description");
		activityDesc.setWidth("400px");
		activityDesc.setNullRepresentation("");
		activityDesc.setRequired(true);
		activityDesc.setEnabled(true);
		activityDesc.setMaxLength(4000);
		activityDesc.setData(doctorActivity);
		activityDesc.setValue(doctorActivity.getActivityDesc());
		addDetailPopupForActivityDesc(activityDesc, null);
		
		activityDesc.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		if(activityTableDto.getActivityDesc() != null) {
			activityDesc.setDescription(doctorActivity.getActivityDesc());
		}
		
		FormLayout formLayout = new FormLayout(activityLog, activityDesc);
		formLayout.setSpacing(true);
		
		mainEditVLayout = new VerticalLayout();
		mainEditVLayout.setSpacing(true);
		mainEditVLayout.setWidth("50%");
		mainEditVLayout.addComponent(formLayout);
		
		activityTableVLayout.removeAllComponents();
		activityTableVLayout.addComponent(mainEditVLayout);
	}
	
	private void addDetailPopupForActivityDesc(TextArea txtFld, final  Listener listener) {
		@SuppressWarnings("unused")
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
				DoctorActivity  doctorActivity = (DoctorActivity) txtFld.getData();
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
						txtFld.setValue(txt.getValue());
					}
				});
				
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setRows(25);
				if(txtArea.getValue() != null){
					doctorActivity.setActivityDesc(txtArea.getValue());	
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
}
