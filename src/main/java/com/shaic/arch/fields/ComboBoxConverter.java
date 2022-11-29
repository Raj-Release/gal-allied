package com.shaic.arch.fields;

import java.util.Locale;

import com.shaic.domain.MastersValue;
import com.vaadin.v7.data.util.converter.Converter;

public class ComboBoxConverter implements Converter<String, MastersValue> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4508188825928420423L;

	@Override
	public MastersValue convertToModel(String value,
			Class<? extends MastersValue> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertToPresentation(MastersValue value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<MastersValue> getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<String> getPresentationType() {
		// TODO Auto-generated method stub
		return null;
	}

}
