package com.shaic.claim.rod.wizard.pages;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.forms.AcknowledgeReceiptPage;
import com.vaadin.ui.Component;

public class AcknowledgeReceiptViewImpl extends AbstractMVPView  implements
WizardStep<ReceiptOfDocumentsDTO> {
	
	
	private static final long serialVersionUID = -1037622841232961579L;

	@Inject
	private AcknowledgeReceiptPage acknowledgeReceiptPage;
	
	@Inject
	private Instance<DocumentDetailsView> documentDetailsView;
	

	@Inject
	private TextBundle tb;
	
	private String strCaptionString;

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		localize(null);
		acknowledgeReceiptPage.init(bean);
		
	}

	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
	}


	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return acknowledgeReceiptPage.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		/*DocumentDetailsView documentView = documentDetailsView.get();
		documentView.returnPreviousPage();
		System.out.println("---inside the on back----");*/
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "acknowledgementReceipt");//String.valueOf(propertyId).toLowerCase());
        }
    
	

	private String textBundlePrefixString()
	{
		return "acknowledge-document-received-";
	}
	

	public void resetPage() {		
		acknowledgeReceiptPage.resetPage();
	}

	public void setClearReferenceData() {
			acknowledgeReceiptPage.setClearReferenceData();
	}
	
	

}
