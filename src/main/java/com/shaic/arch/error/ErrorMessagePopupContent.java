package com.shaic.arch.error;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

public class ErrorMessagePopupContent implements PopupView.Content{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6615418476332363571L;
	private ErrorMessagePanel panel;
	
	public ErrorMessagePopupContent(String message, Button.ClickListener closeListener) {
		panel = new ErrorMessagePanel(message, closeListener);
		panel.setHeightUndefined();
	}
	
	@Override
	public String getMinimizedValueAsHTML() {
		return null;
	}

	@Override
	public Component getPopupComponent() {
		return panel;
	}
	
	public void setContent(String errorMessage)
	{
		panel.show(errorMessage);
	}

}
