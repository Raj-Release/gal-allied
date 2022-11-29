package com.shaic.arch.components;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.validator.RegexpValidator;
import com.vaadin.v7.ui.TextField;

public class STextField extends TextField {

	private static final long serialVersionUID = -642269756565661455L;
	
	public STextField(String caption, Property dataSource, String errorMessage) {
		super(caption, dataSource);
		this.addValidator(new RegexpValidator("/^[A-Za-z]+$/", errorMessage));
	}
}
