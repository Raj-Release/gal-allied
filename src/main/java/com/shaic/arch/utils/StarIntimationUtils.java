package com.shaic.arch.utils;

import java.util.Iterator;


import org.apache.commons.lang3.StringUtils;

import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Product;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;

public class StarIntimationUtils {
	
	public static void setFirstElementSelected(ComboBox comboBox)
	{
		if (comboBox.getItemIds() != null && comboBox.getItemIds().size() > 0)
		{
			comboBox.setValue(comboBox.getItemIds().iterator().next());
		}
	}
	
	public static String getValueFromComponent(Object component){
		if(component instanceof TextField) {
			TextField field = (TextField) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue();
		} else if(component instanceof NativeSelect) {
			NativeSelect field = (NativeSelect) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue().toString();
		} else if(component instanceof PopupDateField) {
			PopupDateField field = (PopupDateField) component; 
			if(field.getValue() == null ||  field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue().toString();
		}
		return null;
	}
	
	public static String getValueFromMasterTable(NativeSelect component){
		 
		if(component!= null && component.getValue() == null || component.getValue() == ""){
			return null;
		}
		 
		if(component.getValue() instanceof MastersValue) {
			return ((MastersValue) component.getValue()).getValue();
		} else if(component.getValue() instanceof OrganaizationUnit) {
			return ((OrganaizationUnit) component.getValue()).getOrganizationUnitId();
		}  else if(component.getValue() instanceof Product) {
			return ((Product) component.getValue()).getCode();
		}
		
		return null;
	}
	
	public static void resetAlltheValues(FormLayout layout) {
		
		Iterator<Component> componentIterator = layout.iterator();
			while(componentIterator.hasNext()) {
				Component eachComponent = componentIterator.next() ;
				
				if(eachComponent != null) {
					
					String className = eachComponent.getClass().toString();
					if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.Label")) {
					   continue;
					}
					
					if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.TextField")) {
					    TextField field = (TextField) eachComponent;
					    field.setValue("");
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.NativeSelect")) {
						NativeSelect field = (NativeSelect) eachComponent;
						field.setValue(null);
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.PopupDateField")) {
						PopupDateField field = (PopupDateField) eachComponent;
						field.setValue(null);
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.ComboBox")) {
						ComboBox field = (ComboBox) eachComponent;
						field.setValue(null);
					} 
				}
			}
	}
	
public static void resetAlltheValues(GridLayout layout) {
		
		Iterator<Component> componentIterator = layout.iterator();
			while(componentIterator.hasNext()) {
				Component eachComponent = componentIterator.next() ;
				
				if(eachComponent != null) {
					
					String className = eachComponent.getClass().toString();
					if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.Label")) {
					   continue;
					}
					
					if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.TextField")) {
					    TextField field = (TextField) eachComponent;
					    field.setValue("");
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.NativeSelect")) {
						NativeSelect field = (NativeSelect) eachComponent;
						field.setValue(null);
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.PopupDateField")) {
						PopupDateField field = (PopupDateField) eachComponent;
						field.setValue(null);
					} else if(StringUtils.equalsIgnoreCase(className, "class com.vaadin.v7.ui.ComboBox")) {
						ComboBox field = (ComboBox) eachComponent;
						field.setValue(null);
					} 
				}
			}
	}

}
