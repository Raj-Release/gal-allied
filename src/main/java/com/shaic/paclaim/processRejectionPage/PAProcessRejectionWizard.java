package com.shaic.paclaim.processRejectionPage;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;

public interface PAProcessRejectionWizard  extends GMVPView, GWizardListener {

	void initView(SearchProcessRejectionTableDTO searchDTO);

	void savedResult();

}
