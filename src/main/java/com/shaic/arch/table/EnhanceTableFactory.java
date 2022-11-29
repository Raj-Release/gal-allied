package com.shaic.arch.table;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.CustomLazyContainer;
import com.shaic.domain.Insured;
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
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"rawtypes", "serial"})

public class EnhanceTableFactory extends DefaultFieldFactory {
	
	private Map<String, TableFieldDTO> fieldMap;
	
	private Map<String, Object> referenceData;
	
//	public EnhanceTableFactory(Map<String, TableFieldDTO> fieldMap, Map<String, Object> referenceData) {
	public EnhanceTableFactory()
	{
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		
		TableFieldDTO tableCell = fieldMap.get(propertyId);
		int length = tableCell.getLength();
		Boolean isRequiredValidator = tableCell.getIsCSValidator();
		Class fieldClass = tableCell.getFieldClass();
		Boolean isEditable = tableCell.getIsEditable();
		SelectValue selectValue = tableCell.getSelectedValue();
		int size = tableCell.getSize();
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
					if(("rodNo").equals(propertyId))
					{
						createdField.setWidth("250px");
					}
					if ((propertyType.equals(SelectValue.class) || propertyType.equals(Insured.class)) && fieldType.equals(ComboBox.class))
					{
						final Object object = referenceData.get(propertyId);
						
						
						if (object != null)
						{
//							final TableCellSelectionHandler changeListener = tableCell.getValueChangeListener();
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
							if(propertyType != null && propertyType.equals(Insured.class)) {
								comboBox.setItemCaptionPropertyId("insuredName");
							}
							comboBox.setId(propertyId.toString());
							if(selectValue != null && selectValue.getId() != null) {
								if(itemId instanceof ProcedureDTO) {
									ProcedureDTO procedureDTO =  (ProcedureDTO) itemId;
									procedureDTO.setConsiderForDayCare(selectValue);
								}
								comboBox.setValue(selectValue);
							}
//							if (changeListener != null)
//							{
//								comboBox.addValueChangeListener(new ValueChangeListener() {
//									
//									@Override
//									public void valueChange(ValueChangeEvent event) {
//										comboBox.getUI().getSession().getLockInstance().lock();
//										changeListener.itemSelected(comboBox, event);
//										comboBox.getUI().getSession().getLockInstance().unlock();
//									}
//								});	
//							}
//							//Vaadin8-setImmediate() comboBox.setImmediate(true);
						} 
					}
					if (propertyType.equals(Boolean.class) && fieldType.equals(OptionGroup.class))
						{
							final Object object = referenceData.get(propertyId);
							if (object != null)
							{
								//Field createdComp = EnhancedFieldGroupFieldFactory.doCreateComponent(propertyType, fieldType);
								final OptionGroup optionGroup = (OptionGroup) createdField;
							} 
						}
					
					//code for file upload to be written in the below block.
					if(propertyType.equals(File.class) && fieldType.equals(Upload.class))
					{
						final Object object = referenceData.get(propertyId);
						if(null != object)
						{
							
						}
					}
					createdField.setReadOnly(!isEditable);
					if(!isEditable) {
						createdField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					}
					if (createdField instanceof TextField) {
						if(length != 0) {
							((TextField) createdField).setMaxLength(length);
						}
						if(size != 0){
							((TextField) createdField).setWidth(String.valueOf(size));
						}
					}
					
					if(null != isRequiredValidator && isRequiredValidator) {
						CSValidator validator = new CSValidator();
						if(createdField instanceof TextField) {
							TextField field = (TextField) createdField;
							validator.extend(field);
							validator.setRegExp("^[a-zA-Z 0-9/]*$");
							if(("claimedAmount").equals(propertyId))
							{
								validator.setRegExp("^[0-9/]*$");
							}
							validator.setPreventInvalidTyping(true);
						}						
					}					
					
//					if(("nonPayable").equalsIgnoreCase(tableCell.getPropertyId())){
//					
//						((TextField)tableCell.getField()).addBlurListener(tableCell.getBlurListner());
//					}					
					
					return createdField;
				}
				
				/*else if (propertyType.equals(File.class) && fieldType.equals(Upload.class))
				{
					final Object object = referenceData.get(propertyId);
					//if (object != null)
					{
						final Upload upload = (Upload) createdField;
					} 
				}*/
			}
			/*else if(fieldType.equals(Upload.class))
			{
				if (propertyType.equals(Boolean.class) && fieldType.equals(OptionGroup.class))
				{
					final Object object = referenceData.get(propertyId);
					if (object != null)
					{
						Field createdField = EnhancedFieldGroupFieldFactory.doCreateComponent(propertyType, fieldType);
						final OptionGroup optionGroup = (OptionGroup) createdField;
					} 
				}
			}*/
		}
		Field createField = super.createField(container, itemId, propertyId, uiContext);
		if(null != isRequiredValidator && isRequiredValidator) {
			CSValidator validator = new CSValidator();
			if(createField instanceof TextField) {
				TextField field = (TextField) createField;
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9/]*$");
				validator.setPreventInvalidTyping(true);
			}
			
		}
		if(((String)propertyId).equalsIgnoreCase("noOfItems") || ((String)propertyId).equalsIgnoreCase("billValue"))
		{
			TextField txtFld = (TextField)createField;
			txtFld.setNullRepresentation("");
			createField = (Field)txtFld;
			/*CSValidator remarksValidator = new CSValidator();
			remarksValidator.extend(((TextField) createField));
			remarksValidator.setRegExp("^[a-zA-Z 0-9/]*$");
			remarksValidator.setPreventInvalidTyping(true);*/
		}
		
		if(((String)propertyId).equalsIgnoreCase("draftOrRedraftRemarks"))
		{
			TextField txtFld = (TextField)createField;
			txtFld.setNullRepresentation("");
			createField = (Field)txtFld;
			((TextField) createField).setMaxLength(1000);
			/*CSValidator remarksValidator = new CSValidator();
			remarksValidator.extend(((TextField) createField));
			remarksValidator.setRegExp("^[a-zA-Z 0-9/]*$");
			remarksValidator.setPreventInvalidTyping(true);*/
		}
		
		
		
		createField.setReadOnly(!isEditable);
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

	public Map<String, TableFieldDTO> getFieldMap() {
		return fieldMap;
	}
	
	
	
	
}
