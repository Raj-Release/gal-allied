package com.alert.util.icons;

import java.io.Serializable;

import com.vaadin.ui.Component;

/**
 * This interface implements the default behavior for loading icons for the dialog. You can
 * override this class to apply an own icon set. 
 * 
 * @author Dieter Steinwedel
 */
public interface DialogIconFactory extends Serializable {

	/**
	 * Returns the question icon.
	 * 
	 * @return question icon
	 */
	public Component getQuestionIcon();
	
	/**
	 * Returns the info icon.
	 * 
	 * @return info icon
	 */
	public Component getInfoIcon();
	
	/**
	 * Returns the warning icon.
	 * 
	 * @return warning icon
	 */
	public Component getWarningIcon();
	
	/**
	 * Returns the error icon.
	 * 
	 * @return error icon
	 */
	public Component getErrorIcon();
			
}
