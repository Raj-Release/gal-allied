package com.shaic.arch.test;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.test.SuggestingContainer.SuggestionFilter;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;

public class InsuranceDiagnosisComboBox extends GComboBox {

	private static final long serialVersionUID = 3063731297421169019L;

	private String userId;
	
	public InsuranceDiagnosisComboBox() {
		// the item caption mode has to be PROPERTY for the filtering to work
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		this.setConverter(StringToSelectValueConverter.class);
		// define the property name of the CountryBean to use as item caption
		setItemCaptionPropertyId("value");
	}
	
	public InsuranceDiagnosisComboBox(String userId) {
		// the item caption mode has to be PROPERTY for the filtering to work
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		this.setConverter(StringToSelectValueConverter.class);
		// define the property name of the CountryBean to use as item caption
		setItemCaptionPropertyId("value");
		this.userId = userId;
	}
	
	/**
	 * Overwrite the protected method
	 * {@link ComboBox#buildFilter(String, FilteringMode)} to return a custom
	 * {@link SuggestionFilter} which is only needed to pass the given
	 * filterString on to the {@link SuggestingContainer}.
	 */
	@Override
	protected Filter buildFilter(String filterString,
			FilteringMode filteringMode) {
		return new InsuranceDiagnosisContainer.SuggestionFilter(filterString,userId);
	}
}