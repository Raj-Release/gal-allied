package com.shaic.paclaim.billing.processclaimbilling.page.billassesmentsheet;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.ui.Component;

public class PABillingAssesmentSheetViewImpl extends AbstractMVPView implements PABillingAssesmentSheetWizard {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PABillingAssesmentSheetPageUI financialDecisionCommuncicationPage;
	
	@Inject
	private TextBundle tb;
	
    private PreauthDTO bean;
	
	private GWizard wizard;
	
	private String strCaptionString;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		
		financialDecisionCommuncicationPage.init(bean, wizard);
		Component comp =  financialDecisionCommuncicationPage.getContent();
		return comp;
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
		return "Confirmation";
	}


	@Override
	public boolean onSave() {
		
		return false;
	}
	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }
	
	private String textBundlePrefixString()
	{
		return "billing-review-";
	}

	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		localize(null);
		this.bean = bean;
		this.wizard = wizard;
	}

}
