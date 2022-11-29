package com.shaic.arch.table;

import java.util.Locale;
import java.util.Map;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.premedical.CustomLazyContainer;
import com.vaadin.cdi.CDIUI;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"rawtypes", "serial"})

public class ListenerTableFactory extends DefaultFieldFactory {
	
	private Map<String, TableFieldDTO> fieldMap;
	
	private Map<String, Object> referenceData;
	
	private TableCellSelectionHandler changeListener;
	
//	public EnhanceTableFactory(Map<String, TableFieldDTO> fieldMap, Map<String, Object> referenceData) {
	public ListenerTableFactory()
	{
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		TableFieldDTO tableCell = fieldMap.get(propertyId);
		Class fieldClass = tableCell.getFieldClass();
		Boolean isEditable = tableCell.getIsEditable();
		
		int length = tableCell.getLength();
		
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
					//tableCell.setField(createdField);
					if(((String)propertyId).equalsIgnoreCase("slNo"))
					{
						createdField.setWidth("50%");
					}
					else if(((String)propertyId).equalsIgnoreCase("noOfDocuments"))
					{
						createdField.setWidth("30%");
					}
					else if(((String)propertyId).equalsIgnoreCase("value"))
					{
						createdField.setWidth("330%");
					}
					else if(((String)propertyId).equalsIgnoreCase("rodReceivedStatus"))
					{
						createdField.setWidth("180%");	
					}
					else if(((String)propertyId).equalsIgnoreCase("mandatoryDocFlag"))
					{
						createdField.setWidth("30%");
					}
					else
					{
						createdField.setWidth("80%");
					}
					tableCell.setField(createdField);
				//	createdField.setWidth("50%");
					if (propertyType.equals(SelectValue.class) && fieldType.equals(ComboBox.class))
					{
						final Object object = referenceData.get(propertyId);
						
						
						if (object != null)
						{
							final GComboBox comboBox = (GComboBox) createdField;
							
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
						//	comboBox.setId(propertyId.toString());
							
							/*if(("receivedStatus").equalsIgnoreCase(String.valueOf(propertyId)))
									{
										BeanItemContainer<SelectValue>	receivedStatusValue = (BeanItemContainer<SelectValue>) referenceData.get("receivedStatus");
										 for(int i = 0 ; i<receivedStatusValue.size() ; i++)
										 	{
												if (("Not Received").equalsIgnoreCase(receivedStatusValue.getIdByIndex(i).getValue()))
												{
													comboBox.setValue(receivedStatusValue.getIdByIndex(i));
												}
											}
									}*/
							} 
					}
					createdField.setReadOnly(!isEditable);
					if(!isEditable) {
						createdField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					}
					//Added for text field
					if (createdField instanceof TextField) {
						if(length != 0) {
							((TextField) createdField).setMaxLength(length);
						}
						if(((String)propertyId).equalsIgnoreCase("remarks") || ((String)propertyId).equalsIgnoreCase("rodRemarks"))
						{
							/*CSValidator remarksValidator = new CSValidator();
							remarksValidator.extend(((TextField) createdField));
							remarksValidator.setRegExp("^[a-zA-Z 0-9/]*$");
							remarksValidator.setPreventInvalidTyping(true);*/
						}
						
						if(((String)propertyId).equalsIgnoreCase("noOfDocuments"))
						{
							CSValidator remarksValidator = new CSValidator();
							remarksValidator.extend(((TextField) createdField));
							remarksValidator.setRegExp("^[0-9]*$");
							remarksValidator.setPreventInvalidTyping(true);
						}
						
					}
					setTableSelectionListener(createdField);
					return createdField;
				}
			}
		}
		Field createField = super.createField(container, itemId, propertyId, uiContext);
		createField.setReadOnly(!isEditable);
		setTableSelectionListener(createField);
		return createField;
	}

	private void setTableSelectionListener(Field createField)
	{
		if (createField != null)
		{
			createField.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					changeListener.itemSelected(null, event);
				}
			});	
			if (createField instanceof TextField)
			{
				TextField listener = (TextField) createField;
				listener.addBlurListener(new BlurListener() {
					
					@Override
					public void blur(BlurEvent event) {
						changeListener.itemSelected(null, null);
					}
				});
			}
		}
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

	public void setTableFieldFactory(Map<String, TableFieldDTO> filedMapping, Map<String, Object> referenceData2, TableCellSelectionHandler changeListener) {
		this.fieldMap = filedMapping;
		this.referenceData = referenceData2;
		this.changeListener = changeListener;
	}
}
