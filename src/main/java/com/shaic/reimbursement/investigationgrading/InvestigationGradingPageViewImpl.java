package com.shaic.reimbursement.investigationgrading;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InvestigationGradingPageViewImpl extends AbstractMVPView implements
InvestigationGradingPageView	{
	


	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private InvestigationGradingPage investigationGradingPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(AssignInvestigatorDto bean, GWizard wizard) {
		localize(null);
		investigationGradingPage.init(bean,wizard);
	}

	/*@Override
	public void init(AssignInvestigatorDto bean) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public Component getContent() {		
		
		return investigationGradingPage.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		return investigationGradingPage.validatePage();
		//return investigationGradingPage.validatePage();
		
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
		return "Investigation-Grading-";
	}

	@Override
	public void init(AssignInvestigatorDto bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean validatePage() {
		// TODO Auto-generated method stub
		return investigationGradingPage.validatePage();
	}
	


}
