package com.alert.util.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import com.vaadin.server.VaadinSession;
import com.alert.util.ButtonType;
import com.alert.util.i18n.captions.ButtonCaptions_en;

/**
 * Manages the translation of button captions. 
 * 
 * @author Dieter Steinwedel
 */
public class ButtonCaptionFactory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String LANGUAGE_SESSION_KEY = "messagebox_language";
	
	/**
	 * Translates the caption for the given buttonType.
	 * 
	 * @param buttonType The ButtonType
	 * @param defaultLanguage The default language
	 * 
	 * @return The translated caption
	 */
	public String translate(ButtonType buttonType, Locale defaultLanguage) {
		if (buttonType == null) {
			return "";
		}
		
		ResourceBundle resourceBundle = null;
	String basename = ButtonCaptions_en.class.getName();
		
		
		try {
			Locale locale = defaultLanguage;
			Object value = VaadinSession.getCurrent().getAttribute(LANGUAGE_SESSION_KEY);
			if (value != null && value instanceof Locale) {
				locale = (Locale) value;
			}
		
			resourceBundle = ResourceBundle.getBundle(basename, locale);
		} catch(Throwable t) {
			resourceBundle = ResourceBundle.getBundle(basename, Locale.ENGLISH);
		}
		return resourceBundle.getString(buttonType.name());
	}
		
}