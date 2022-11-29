package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;

public interface CancelDocumentWizardView extends GMVPView,WizardStep<ReceiptOfDocumentsDTO>   {

	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard);

	public void setUpDropDownValues(Map<String, Object> referenceDataMap);

	public void setTableValues(
			List<ViewDocumentDetailsDTO> acknowledgmentForCancel);

}
