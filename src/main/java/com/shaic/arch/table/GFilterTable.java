package com.shaic.arch.table;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.tepi.filtertable.FilterTable;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable.RowHeaderMode;

@SuppressWarnings("serial")
//@ApplicationScoped
public abstract class GFilterTable<T> extends ViewComponent {

	protected FilterTable table;
	
	private IndexedContainer cont;

	private Map<String, Class<?>>  fieldMap = new HashMap<String, Class<?>>();
	public enum State {
		CREATED, PROCESSING, PROCESSED, FINISHED;
	}
	
	@Inject
	private TextBundle tb;

	public void init() {
		setSizeFull();
		
		table = new FilterTable();
		cont = new IndexedContainer();
		
		initTable();
		this.table = buildFilterTable();
		setCompositionRoot(table);
		table.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				Object itemId = event.getItemId();
				System.out.println(itemId);
				tableSelectHandler(itemId);
			}
		});
		
		localize(null);
	}

	public abstract void removeRow();

	public abstract void initTable();
	
	protected abstract String[] getVisibleColumns();
	
	public abstract void setTableList(final Collection<T> list);

	public void setValue(final T bean) {
		table.setValue(bean);
	}

	public Item getSelectedItem() {
		return table.getItem(table.getValue());
	}

	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		for (final Object propertyId : table.getVisibleColumns()) {
			final String header = tb.getText(textBundlePrefixString()
					+ String.valueOf(propertyId).toLowerCase());
			table.setColumnHeader(propertyId, header);
		}
	}

	private FilterTable buildFilterTable() {
		table.setSizeFull();
		table.setFilterDecorator(new GFilterDecorator());
		table.setFilterGenerator(new GFilterGenerator());

		table.setFilterBarVisible(true);

		table.setSelectable(true);
		//Vaadin8-setImmediate() table.setImmediate(true);
		table.setMultiSelect(true);

		table.setRowHeaderMode(RowHeaderMode.INDEX);

		table.setColumnCollapsingAllowed(true);

		table.setColumnReorderingAllowed(true);
		table.setContainerDataSource(cont);
		// table.setContainerDataSource(buildContainer());

//		table.setColumnCollapsed("state", true);

		table.setVisibleColumns((Object[]) getVisibleColumns());

		table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {

			@Override
			public String generateDescription(Component source, Object itemId,
					Object propertyId) {
				return "Just testing ItemDescriptionGenerator";
			}
		});

		return table;
	}

	public void constructMapping(Class<T> clazz)
	{
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String name = propertyDescriptor.getName();
				Class<?> propertyType = propertyDescriptor.getPropertyType();
				
				cont.addContainerProperty(name, propertyType, null);
				fieldMap.put(name, propertyType);
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public void addItem(Map<String, Object> rowItem, String keyField) {
		Object keyValue = rowItem.get(keyField);
		cont.addItem(keyValue);
		for (String colItemId : fieldMap.keySet()) {
			cont.getContainerProperty(keyValue, colItemId).setValue(rowItem.get(colItemId));
		}
		table.sort();
	}
	
	@SuppressWarnings("unchecked")
	public Container buildContainer(Class<T> clazz) 
	{
		constructMapping(clazz);
		Calendar c = Calendar.getInstance();
		return cont;
	}

	public abstract void tableSelectHandler(Object key);

	public abstract String textBundlePrefixString();
}
