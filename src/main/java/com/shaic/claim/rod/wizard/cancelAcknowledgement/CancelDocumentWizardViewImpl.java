package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.ui.Component;

public class CancelDocumentWizardViewImpl extends AbstractMVPView 
implements CancelDocumentWizardView {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CancelDocumentDetailUI cancelDocumentDetailsObj;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private ReceiptOfDocumentsDTO bean;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		localize(null);
		this.bean = bean;
		
		cancelDocumentDetailsObj.init(bean,wizard);
	}

	@Override
	public Component getContent() {
		Component comp =  cancelDocumentDetailsObj.getComponent();
		
		fireViewEvent(CancelDocumentWizardPresenter.SETUP_DROPDOWN_VALUES, bean); 
		
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
	}

	@Override
	public boolean onAdvance() {
		return cancelDocumentDetailsObj.validatePage();
	}

	@Override
	public boolean onBack() {
		
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}
	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");//String.valueOf(propertyId).toLowerCase());
        }
	
	private String textBundlePrefixString()
	{
		return "acknowledge-document-received-";
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
		
		cancelDocumentDetailsObj.setUpDropDownValues(referenceDataMap);
		
	}

	@Override
	public void setTableValues(
			List<ViewDocumentDetailsDTO> acknowledgmentForCancel) {
		cancelDocumentDetailsObj.setTableValues(acknowledgmentForCancel);
	}

	

}
