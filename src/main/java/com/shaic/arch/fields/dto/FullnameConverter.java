package com.shaic.arch.fields.dto;

import java.util.Locale;

import com.vaadin.v7.data.util.converter.Converter;

public class FullnameConverter implements Converter<String, FullNameDTO> {

	@Override
	public FullNameDTO convertToModel(String value,
			Class<? extends FullNameDTO> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		String parts[] = value.replaceAll("[\\(\\)]", "").split(",");
		return new FullNameDTO(parts[0], parts[1], parts[3]);
	}

	@Override
	public String convertToPresentation(FullNameDTO value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		return value.getFullName();
	}

	@Override
	public Class<FullNameDTO> getModelType() {
		// TODO Auto-generated method stub
		return FullNameDTO.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
