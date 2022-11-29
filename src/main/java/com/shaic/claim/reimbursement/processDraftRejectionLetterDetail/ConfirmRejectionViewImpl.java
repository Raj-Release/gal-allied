package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

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

import com.vaadin.ui.Component;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class ConfirmRejectionViewImpl extends AbstractMVPView implements
WizardStep<ClaimRejectionDto> {

	@Inject
	private ConfirmRejectionPage confirmRejectionPage;
	
	@Inject
	private Instance<DecideOnRejectionView> decideOnRejectionView;
	

	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	@Override
	public void init(ClaimRejectionDto bean) {
		localize(null);
		confirmRejectionPage.init(bean);	
	}

	@Override
	public Component getContent() {
		
		return confirmRejectionPage.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}
		
	@Override
	public String getCaption() {
		return strCaptionString;
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

	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        strCaptionString = tb.getText(textBundlePrefixString() + "confirmation");
    }
    
	private String textBundlePrefixString()
	{
		return "confirmrejection-";
	}
	
	public void clearObject(){
		confirmRejectionPage.clearObject();
	}
	
}
