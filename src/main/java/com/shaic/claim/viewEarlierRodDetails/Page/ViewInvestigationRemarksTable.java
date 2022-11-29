package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewInvestigationRemarksTable extends ViewComponent{


	private Map<AssignedInvestigatiorDetails, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AssignedInvestigatiorDetails, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<AssignedInvestigatiorDetails> data = new BeanItemContainer<AssignedInvestigatiorDetails>(AssignedInvestigatiorDetails.class);

	private Table table;
	private AssignedInvestigatiorDetails dto;	
	
	public void init(String screenName) {

		dto = new AssignedInvestigatiorDetails();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);
		table.setWidth("500px");
		table.setHeight("170px");
		if(null != screenName && SHAConstants.MEDICAL_SUMMARY.equalsIgnoreCase(screenName)){
			table.setWidth("550px");
		}
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);
		setSizeFull();
		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		table = new Table("", data);
		data.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());		
//		generateColumn();
		table.setVisibleColumns(new Object[] { "rollNo", "investigatorName", "reviewRemarks"});
		table.setColumnHeader("rollNo", "S.</br>No");
//		table.setColumnHeader("chkSelect", " ");
		table.setColumnHeader("investigatorName", "Investigator Name");
		table.setColumnHeader("reviewRemarks", "Investigation Review Remarks");
		table.setColumnWidth("rollNo", 35);
		table.setEditable(true);		
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			AssignedInvestigatiorDetails invsDTO = (AssignedInvestigatiorDetails) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(invsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(invsDTO, new HashMap<String, AbstractField<?>>());
			} 
			
			tableRow = tableItem.get(invsDTO);
			
		 if("investigatorName".equals(propertyId)) {
				 	TextField field = new TextField();
				 	field.setNullRepresentation("");
				 	field.setEnabled(true);
				 	field.setData(invsDTO);
					tableRow.put("investigatorName", field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
				return field;
			} 
			 else  if("reviewRemarks".equals(propertyId)) {
				 	TextField field = new TextField();
				 	field.setNullRepresentation("");
				 	field.setEnabled(true);
				 	field.setWidth("300px");
				 	field.setData(invsDTO);
				 	field.setReadOnly(true);
					tableRow.put("reviewRemarks", field);
					field.setDescription(invsDTO.getReviewRemarks());
					handleTextFieldPopup(field,null);
					return field;
				} 
		 
		 else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				return field;
			}
		}

		
	}
	 public void addBeanToList(AssignedInvestigatiorDetails benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<AssignedInvestigatiorDetails> getValues() {
			List<AssignedInvestigatiorDetails> itemIds = (List<AssignedInvestigatiorDetails>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
		public void setTableList(final List<AssignedInvestigatiorDetails> list) {
			table.removeAllItems();
			int i = 1;
			for (final AssignedInvestigatiorDetails bean : list) {
				bean.setRollNo(i);
				table.addItem(bean);
				i++;
			}
		}
		
	/*Below Code Commented is not required for View Purpose
	private void generateColumn(){
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final AssignedInvestigatiorDetails tableDTO = (AssignedInvestigatiorDetails) itemId;
				  CheckBox chkBox = new CheckBox();
				  
				  chkBox.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
							{
								boolean value = (Boolean) event.getProperty().getValue();
								if(value)
								{
									tableDTO.setReportReviewed(SHAConstants.YES_FLAG);
								}else
								{
									tableDTO.setReportReviewed(SHAConstants.N_FLAG);
								}
							}
					}
		  });
				return chkBox;
			}
		});
	}*/
	

	@SuppressWarnings("unused")
	public  void handleTextFieldPopup(TextField searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

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

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtField = new TextArea();
				txtField.setValue(txtFld.getValue());
				txtField.setNullRepresentation("");
				txtField.setSizeFull();
				txtField.setWidth("100%");
				txtField.setMaxLength(1000);
				txtField.setReadOnly(true);
				txtField.setHeight("400px");
//				txtField.setRows(25);

				txtField.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtField);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Investigation Review Remarks";

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
					dialog.setPositionX(250);
					dialog.setPositionY(100);
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
