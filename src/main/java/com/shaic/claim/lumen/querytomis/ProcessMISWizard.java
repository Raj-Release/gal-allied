package com.shaic.claim.lumen.querytomis;

import com.shaic.arch.GMVPView;
import com.vaadin.v7.ui.Upload.SucceededEvent;

public interface ProcessMISWizard extends GMVPView{

	public void cancelLumenRequest();
	public void submitLumenRequest();
	void uploadSucceeded(SucceededEvent event);
	public void reloadDocumentPopup(DocumentAckTableDTO docTblDTO);
}
