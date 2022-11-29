package com.shaic.claim.reimbursement.processdraftquery;

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
import com.vaadin.v7.ui.Label;

import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class ConfirmQueryViewImpl extends AbstractMVPView implements
WizardStep<ClaimQueryDto> {

	@Inject
	private ConfirmQueryPage confirmQueryPage;
	
	@Inject
	private Instance<DecideOnQueryView> decideOnQueryView;
	
	private ClaimQueryDto bean;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	@Override
	public void init(ClaimQueryDto bean) {
		localize(null);
		this.bean = bean;
		confirmQueryPage.init(bean);	
	}

	@Override
	public Component getContent() {
		
		if(bean.getReimbursementQueryDto().getQueryLetterRemarks() != null )
		{
			return confirmQueryPage.getContent();	
		}
		
		return new VerticalLayout(new Label("Claim Record Saved Successfully."));
		
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
		return "confirmquery-";
	}
	
	public void clearObject(){
		confirmQueryPage.clearObject();
		bean = null;
	}
	
}
