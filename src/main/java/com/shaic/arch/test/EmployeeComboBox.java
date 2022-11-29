package com.shaic.arch.test;

import com.shaic.arch.components.GComboBox;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class EmployeeComboBox extends GComboBox{

	private static final long serialVersionUID = 3063731297421169019L;
	
	public EmployeeComboBox() {
		
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		this.setConverter(StringToSelectValueConverter.class);
		setItemCaptionPropertyId("value");
	}
	
	@Override
	protected Filter buildFilter(String filterString,FilteringMode filteringMode) {
		return new EmployeeContainer.EmployeeFilter(filterString);
	}
}
