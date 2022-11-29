package com.shaic.reimbursement.draftinvesigation;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.vaadin.ui.Component;

public class DraftInvestigatorViewImpl extends AbstractMVPView implements
		DraftInvestigatorView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DraftInvestigatorUI draftInvestigatorUI;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(DraftInvestigatorDto bean, GWizard wizard) {
		localize(null);
		draftInvestigatorUI.init(bean, wizard);
	}

	@Override
	public void init(DraftInvestigatorDto bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {		
		return draftInvestigatorUI.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {		
		return draftInvestigatorUI.validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCaption() {
		return strCaptionString;
	}
	
	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "name");
	}
	
	private String textBundlePrefixString() {
		return "Draft-Investigation-";
	}
	
}
