package com.shaic.claim.policy.search.ui.opsearch;


import com.shaic.arch.GMVPView;
import com.vaadin.v7.data.Property.ValueChangeEvent;

public interface OPRegisterClaimPolicyView extends GMVPView {

	void searchSubmit();
    void showLayoutBasedOnSelectedItem(ValueChangeEvent valueChangeEvent);
    void resetAlltheValues();
	void showSearchPolicy();
}
