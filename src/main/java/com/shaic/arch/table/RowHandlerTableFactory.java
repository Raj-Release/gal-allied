package com.shaic.arch.table;

import java.util.Locale;
import java.util.Map;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"rawtypes", "serial"})

public class RowHandlerTableFactory extends DefaultFieldFactory {
	
	private Map<String, TableFieldDTO> fieldMap;
	
	private Map<String, Object> referenceData;
	
//	public EnhanceTableFactory(Map<String, TableFieldDTO> fieldMap, Map<String, Object> referenceData) {
	public RowHandlerTableFactory()
	{
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		TableFieldDTO tableCell = fieldMap.get(propertyId);
		if(tableCell != null) {
			Class fieldClass = tableCell.getFieldClass();
			Boolean isEditable = tableCell.getIsEditable();
			Field createdField;
			AbstractRowEnablerDTO dtoFlagObj = (AbstractRowEnablerDTO) itemId;
			
			if (fieldClass != null)
			{
				Class fieldType = fieldClass;
				Class propertyType = tableCell.getTypeClass();
				
				int length = tableCell.getLength();
				
				if (fieldType != null && propertyType != null)
				{
					createdField = EnhancedFieldGroupFieldFactory.doCreateField(propertyType, fieldType);
					
					if (createdField != null)
					{
						tableCell.setField(createdField);
						createdField.setWidth("80%");
						if (propertyType.equals(SelectValue.class) && fieldType.equals(ComboBox.class))
						{
							final Object object = referenceData.get(propertyId);
							
							
							if (object != null)
							{
//								final TableCellSelectionHandler changeListener = tableCell.getValueChangeListener();
								final ComboBox comboBox = (ComboBox) createdField;
								
								if(object instanceof CustomLazyContainer) {
									comboBox.setContainerDataSource((CustomLazyContainer)object);
									comboBox.setConverter(getConverter(object));
								} else {
									BeanItemContainer<SelectValue> dropdownValues = (BeanItemContainer<SelectValue>)object;
									comboBox.setContainerDataSource(dropdownValues);
									comboBox.setNullSelectionAllowed(false);
									String str = (String) propertyId;
									if(str != null && str.equalsIgnoreCase("copay") && itemId  != null && itemId instanceof ProcedureDTO) {
										if(itemId  != null && itemId instanceof ProcedureDTO) {
											ProcedureDTO procedureDTO = (ProcedureDTO) itemId;
											if(procedureDTO != null && procedureDTO.getCopayPercentage() == null) {
												Integer index = 100;
												Object defaultCopay = referenceData.get(SHAConstants.IS_DEFAULT_COPAY);
												
												if(defaultCopay != null && defaultCopay instanceof Boolean) {
													Boolean isDefaultCopayApplicable = (Boolean) defaultCopay;
													if(isDefaultCopayApplicable) {
														String string = (String) referenceData.get(SHAConstants.DEFAULT_COPAY_VALUE);
														for (SelectValue values : dropdownValues.getItemIds()) {
																if(values.getValue() != null && string != null && values.getValue().toString().equalsIgnoreCase(string)) {
																	procedureDTO.setCopay(values);
																	break;
															}
														}
												
													}
												}
											}
											}
										}
									if(str != null && str.equalsIgnoreCase("procedureStatus") && itemId  != null && itemId instanceof ProcedureDTO
											|| str != null && str.equalsIgnoreCase("exclusionDetails") && itemId  != null && itemId instanceof ProcedureDTO) {
										if(itemId  != null && itemId instanceof ProcedureDTO) {
											ProcedureDTO procedureDTO = (ProcedureDTO) itemId;
											if(procedureDTO.getIsGMC() != null && procedureDTO.getIsGMC()){
												comboBox.setEnabled(false);
											}
										}
									}
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
						if (createdField instanceof TextField) {
							if(length != 0) {
								((TextField) createdField).setMaxLength(length);
							}
						}
						if(!dtoFlagObj.isGmcFlag()){
							createdField.setEnabled(dtoFlagObj.isStatusFlag());
						}

						return createdField;
					}
				}
			}
			createdField = super.createField(container, itemId, propertyId, uiContext);
			//createdField.setReadOnly(!isEditable);
			if(!dtoFlagObj.isGmcFlag()){
				createdField.setEnabled(dtoFlagObj.isStatusFlag());
			}
			return createdField;
		}
		return null;
		
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
