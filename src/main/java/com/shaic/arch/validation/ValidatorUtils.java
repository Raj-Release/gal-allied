package com.shaic.arch.validation;

import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.Field;

public class ValidatorUtils {

	public static final String EMAIL_ADDRESS = "^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";

	public static final String NUMBER = "(^[0-9 ]*)$";

	public static final String ALPHA_NUMERIC = "^[a-zA-Z0-9./']*$";
	
	private ValidatorUtils() {}
	
	/*public static void installSingleValidator(Field<?> field, String attribute) {
		
		Collection<Validator> validators = field.getValidators();
		field.removeAllValidators();
		if (validators == null || validators.isEmpty()) {

			field.addValidator(new BeanValidator(IntimationBean.class, attribute));
		}
	}*/
	
	public static void installValidator(Field<?> field, String attribute, Validator validator) {
		
	  if (validator != null) 
	  {
		field.addValidator(validator);
	  }
	}
	
	public static void removeValidator(Field<?> field)
	{
		field.removeAllValidators();
	}
	
	public static boolean isNull(Object obj)
	{
		return obj == null ? true : false;
	}
	
	public static boolean isValidNumber(String numStr)
	{
		if (!isNull(numStr))
		{
			return numStr.matches(NUMBER);
		}
		return false;
	}
	
	public static String getEmailAddress() {
		return EMAIL_ADDRESS;
	}

	public static String getNumber() {
		return NUMBER;
	}

	public static String getAlphaNumeric() {
		return ALPHA_NUMERIC;
	}
	
	public static void unbindField(Field<?> field, FieldGroup fieldGroup) {
		if (!ValidatorUtils.isNull(field ) && field.isAttached())
		{
			fieldGroup.unbind(field);	
		}
	}
}