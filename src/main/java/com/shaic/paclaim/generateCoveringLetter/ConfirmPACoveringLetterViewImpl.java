package com.shaic.paclaim.generateCoveringLetter;

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

import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.reimbursement.processdraftquery.DecideOnQueryView;
import com.vaadin.ui.Component;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class ConfirmPACoveringLetterViewImpl extends AbstractMVPView implements
WizardStep<GenerateCoveringLetterSearchTableDto> {

	@Inject
	private ConfirmPACoveringLetterPage confirmPACoveringLetterPage;
	
	@Inject
	private Instance<DecideOnQueryView> decideOnPACoveringLetterView;
	
	private GenerateCoveringLetterSearchTableDto bean;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	@Override
	public void init(GenerateCoveringLetterSearchTableDto bean) {
		localize(null);
		this.bean = bean;
		confirmPACoveringLetterPage.init(bean);	
	}

	@Override
	public Component getContent() {
		
			return confirmPACoveringLetterPage.getContent();		
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
        strCaptionString = tb.getText(textBundlePrefixString() + "confirm");
    }
    
	private String textBundlePrefixString()
	{
		return "confirmpacoveringletter-";
	}
	
}
