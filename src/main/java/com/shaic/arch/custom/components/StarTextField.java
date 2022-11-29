package com.shaic.arch.custom.components;

import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.TextField;

public class StarTextField extends  TextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4747040664518257516L;
	
	 public StarTextField(String content, ContentMode contentMode) {
	        setValue(content);
	        setContentMode(contentMode);
	        setWidth(100, Unit.PERCENTAGE);
	    }
	 
	 public void setContentMode(ContentMode contentMode) {
	        if (contentMode == null) {
	            throw new IllegalArgumentException("Content mode can not be null");
	        }

	        getState().errorMessage = contentMode.toString();
	    }
	 
//	 @Override
//	 public String getValue() {
//	    return getFieldValue();
//	 }
}
