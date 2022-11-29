package com.shaic.arch.table;

import java.util.Locale;
import java.util.Map;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.premedical.CustomLazyContainer;
import com.vaadin.cdi.CDIUI;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"rawtypes", "serial"})

public class SpecialistPageTableFactory<T> extends DefaultFieldFactory {
	
	private BeanItemContainer<T> container;
	
	public SpecialistPageTableFactory(BeanItemContainer<T> item)
	{
		container = item;
	}
	
	private Map<String, TableFieldDTO> fieldMap;
	
	private Map<String, Object> referenceData;
	
	@SuppressWarnings("unchecked")
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		Table table = (Table) uiContext;
		
		TableFieldDTO tableCell = fieldMap.get(propertyId);
		Class fieldClass = tableCell.getFieldClass();
		Boolean isEditable = tableCell.getIsEditable();
		System.out.println(uiContext.getClass());
		Object lastItem = table.getData();
		
		if (fieldClass != null)
		{
			Class fieldType = fieldClass;
			Class propertyType = tableCell.getTypeClass();
			
			if (fieldType != null && propertyType != null)
			{
				@SuppressWarnings("unchecked")
				Field createdField = EnhancedFieldGroupFieldFactory.doCreateField(propertyType, fieldType);
				
				if (createdField != null)
				{
					tableCell.setField(createdField);
					createdField.setWidth("80%");
					if (propertyType.equals(SelectValue.class) && fieldType.equals(ComboBox.class))
					{
						final Object object = referenceData.get(propertyId);
						if (object != null)
						{
							final ComboBox comboBox = (ComboBox) createdField;
							
							if(object instanceof CustomLazyContainer) {
								comboBox.setContainerDataSource((CustomLazyContainer)object);
								comboBox.setConverter(getConverter(object));
							} else {
								comboBox.setContainerDataSource((BeanItemContainer<SelectValue>)object);
								
							}
							comboBox.setFilteringMode(FilteringMode.STARTSWITH);
							comboBox.setTextInputAllowed(true);
							comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							comboBox.setItemCaptionPropertyId("value");
							comboBox.setId(propertyId.toString());
						} 
					}
					createdField.setReadOnly(!isEditable);
					if(!isEditable) {
						createdField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					}
					
					if (itemId.equals(lastItem))
					{
						createdField.setReadOnly(false);
					}
					else
					{
						createdField.setReadOnly(true);
					}
					
					return createdField;
				}
			}
		}
		Field createField = super.createField(container, itemId, propertyId, uiContext);
		createField.setReadOnly(!isEditable);
		if (itemId.equals(lastItem))
		{
			createField.setReadOnly(false);
		}
		else
		{
			createField.setReadOnly(true);
		}
		if (createField instanceof TextField)
		{
			TextField f = (TextField) createField;
			f.setNullRepresentation("");
		}
		return createField;
	}

	private Converter<Object, SelectValue> getConverter(final Object object) {
		return new Converter<Object, SelectValue>() {

			@Override
			public SelectValue convertToModel(
					Object itemId,
					Class<? extends SelectValue> targetType,
					Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
		            IndexedContainer c = (IndexedContainer) object;
		            Object propertyId = c.getContainerPropertyIds().iterator().next();
		            Object name = c.getItem(itemId).getItemProperty(propertyId).getValue();
		            return (SelectValue) name;
		        }
				return null;
			}

			@Override
			public Object convertToPresentation(
					SelectValue value,
					Class<? extends Object> targetType,
					Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (value != null) {
		            IndexedContainer c = (IndexedContainer) object;
		            Object propertyId = c.getContainerPropertyIds().iterator().next();
		            for (Object itemId : c.getItemIds()) {
		                Object name = c.getContainerProperty(itemId, propertyId).getValue();
		                if (value.equals(name)) {
		                    return itemId;
		                }
		            }
		        }
		        return null;
			}

			@Override
			public Class<SelectValue> getModelType() {
				// TODO Auto-generated method stub
				return SelectValue.class;
			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}};
	}

	public void setTableFieldFactory(Map<String, TableFieldDTO> filedMapping, Map<String, Object> referenceData2) {
		this.fieldMap = filedMapping;
		this.referenceData = referenceData2;
	}
}
