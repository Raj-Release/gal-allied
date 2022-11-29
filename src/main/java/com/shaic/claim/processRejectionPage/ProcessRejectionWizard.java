package com.shaic.claim.processRejectionPage;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;

public interface ProcessRejectionWizard  extends GMVPView, GWizardListener {

	void initView(SearchProcessRejectionTableDTO searchDTO);

	void savedResult();

}
