package com.shaic.arch.test;

import java.util.Locale;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.converter.Converter;


public class StringToSelectValueConverter implements Converter<String, SelectValue> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6161668961920239922L;

	public String convertToPresentation(SelectValue name, Locale locale)
            throws ConversionException {
        if (name == null) {
            return null;
        } else {
            return name.getValue();
        }
    }

    public Class<SelectValue> getModelType() {
        return SelectValue.class;
    }

    public Class<String> getPresentationType() {
        return String.class;
    }

	@Override
	public SelectValue convertToModel(String value, Class<? extends SelectValue> targetType, Locale locale) throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		if (value == null) {
            return null;
        }
        
        SelectValue selectValue = new SelectValue();
        selectValue.setValue(value);
        return selectValue;
        
	}

	@Override
	public String convertToPresentation(SelectValue value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		 if (value == null) {
	            return null;
	        } else {
	            return value.getValue();
	        }
	}
}