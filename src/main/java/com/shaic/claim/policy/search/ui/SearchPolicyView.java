package com.shaic.claim.policy.search.ui;

import com.shaic.arch.GMVPView;
import com.vaadin.v7.data.Property.ValueChangeEvent;

public interface SearchPolicyView extends GMVPView  {

	void searchSubmit();
    void showLayoutBasedOnSelectedItem(ValueChangeEvent valueChangeEvent);
    void resetAllValues();
	void showSearchPolicy();
	void searchPolicyWithParameter(String policyNumber, String healthCardNumber);
}
