package com.shaic.claim.policy.search.ui.opsearch;

import com.shaic.arch.GMVPView;
import com.vaadin.v7.data.Property.ValueChangeEvent;

public interface OPExpiredPolicyClaimView  extends GMVPView {

	void searchSubmit();
    void resetAlltheValues();
	void showSearchPolicy();

}
