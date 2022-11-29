package com.shaic.claims.reibursement.addaditionaldocuments;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.vaadin.ui.Component;

public class AcknowledgementReceiptViewImpl extends AbstractMVPView implements
		WizardStep<ReceiptOfDocumentsDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AcknowledgementReceiptUI acknowledgementReceiptUI;
	
	@SuppressWarnings("unused")
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	public void init(ReceiptOfDocumentsDTO assignInvestigatorDto){
		localize(null);
		this.bean = assignInvestigatorDto;				
		acknowledgementReceiptUI.init(assignInvestigatorDto);
	}

	
	
	@Override
	public Component getContent() {		
		return acknowledgementReceiptUI.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
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
	public boolean onSave() {
		return false;
	}
	
	@Override
	public String getCaption() {
		return strCaptionString;
	}
	
	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "acknowledgementReceipt");
	}
	
	private String textBundlePrefixString() {
		return "Assign-Investigation-";
	}

}
