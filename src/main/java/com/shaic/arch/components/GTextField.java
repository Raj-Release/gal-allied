package com.shaic.arch.components;

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.v7.ui.TextField;

public class GTextField extends TextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GTextField(){
		this.setConverter(plainIntegerConverter);
	}
	
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};

}
