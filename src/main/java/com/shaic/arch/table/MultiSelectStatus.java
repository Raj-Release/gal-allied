package com.shaic.arch.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MultiSelectStatus extends ViewComponent {

	private Map<SelectValue, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SelectValue, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SelectValue> data = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private ComboBox cpuCodeCmb;
	
	private Table table;
	
	public void init(String selectTitle,BeanItemContainer<SelectValue> cpuCodeContainer) {
		cpuCodeCmb = new ComboBox(selectTitle);
		
		cpuCodeCmb.setContainerDataSource(cpuCodeContainer);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		VerticalLayout layout = new VerticalLayout();
		FormLayout cmbFrmLayout = new FormLayout(cpuCodeCmb);
		cmbFrmLayout.setSpacing(Boolean.FALSE);
		cmbFrmLayout.setMargin(Boolean.FALSE);
		layout.addComponent(cmbFrmLayout);
		layout.setMargin(Boolean.FALSE);
		layout.setSpacing(Boolean.FALSE);
		
		initTable();
		table.setWidth("260px");
		
		table.setHeight("100px");
		
//		if(table.getItemIds().size() <= 5){
//		
			table.setPageLength(5);
//		}
//		else{
//			table.setHeight("100px");
//		}
		
		addListener();
		
		layout.addComponent(table);
		setSizeFull();

		setCompositionRoot(layout);
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		data.removeAllItems();
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] {"value"});
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
//		table.setColumnHeader("value", "Description");
		
		table.setEditable(true);
		//		manageListeners();
		
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button deleteButton = new Button();
		    	deleteButton.setDescription("Delete");
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
			        } 
			    });
		    	return deleteButton;
		      };
		});

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			SelectValue cpuCode = (SelectValue) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(cpuCode) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(cpuCode, new HashMap<String, AbstractField<?>>());
			} 
			
			tableRow = tableItem.get(cpuCode);
			
			if("value".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setData(cpuCode);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("value", field);
				
				return field;
			} 
			 
			 return null;
		 }		
	}	
	
	 public void addBeanToList(SelectValue benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<SelectValue> getValues() {
			List<SelectValue> itemIds = (List<SelectValue>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
	 public String getSelectedCpuIds(){
			StringBuffer cpuIdlst = new StringBuffer("");
			if(table.getItemIds().size() > 0){
				Collection<SelectValue> tableItems = (Collection<SelectValue>) table.getItemIds();
				
				if(tableItems != null && !tableItems.isEmpty()){
					for (SelectValue selectedCPUDto : tableItems) {
						cpuIdlst = cpuIdlst.length() > 0 ?  cpuIdlst.append(",").append(selectedCPUDto.getId()) : cpuIdlst.append(selectedCPUDto.getId());	
					}
				}
			}
			return cpuIdlst.toString();
		}
		public void setTableList(final List<SelectValue> list) {
			//int i = 1;
			table.removeAllItems();
			for (final SelectValue bean : list) {
				//bean.setSerialNumber(i);
				table.addItem(bean);
				//i++;
			}
		}
	
	private void addListener() {
		cpuCodeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue selectedCpu = (SelectValue) event.getProperty().getValue();
				if(selectedCpu != null)
					data.addItem(selectedCpu);	
			}
		});	}
	
	public void reset(){
		if(table != null){
			table.removeAllItems();
		}
		if(cpuCodeCmb != null){
			cpuCodeCmb.setValue(null);
		}
	}
		
}
