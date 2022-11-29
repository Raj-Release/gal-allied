package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.vaadin.ui.Component;

public class CancelAcknowledgementLetterViewImpl extends AbstractMVPView implements
WizardStep<ReceiptOfDocumentsDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CancelAcknowledgementLetterUI cancelAcknowledgementLetter;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		this.bean = bean;
		
	}
	
	public void init(ReceiptOfDocumentsDTO rodDTO,GWizard wizard){
		localize(null);
		this.bean = rodDTO;				
		cancelAcknowledgementLetter.init(rodDTO,wizard);
	}


	@Override
	public Component getContent() {
		return cancelAcknowledgementLetter.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}
	
	@Override
	public String getCaption() {
		return strCaptionString;
	}
	
	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "confirmation");
	}
	
	private String textBundlePrefixString() {
		return "acknowledge-document-received-";
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

}
