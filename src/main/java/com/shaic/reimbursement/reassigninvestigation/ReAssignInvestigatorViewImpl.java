package com.shaic.reimbursement.reassigninvestigation;

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

import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.ui.Component;

public class ReAssignInvestigatorViewImpl extends AbstractMVPView implements
		ReAssignInvestigatorView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ReAssignInvestigatorUI assignInvestigatorUI;
	
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
		assignInvestigatorUI.init(bean, wizard);
	}
	
	@Override
	public List<AssignInvestigatorDto> getMultiInvestigators(){
		
//		if(assignInvestigatorUI.validatePage()){
			return assignInvestigatorUI.getMultiInvestigators();
//		}
//		return null;
	}
	
	@Override
	public void getAssignCountAlert(){
		
		assignInvestigatorUI.getInvAssignCount();
	}

	@Override
	public void init(AssignInvestigatorDto bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {		
		return assignInvestigatorUI.getContent();
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		
		return true;
		//As Part of R0767
//		return assignInvestigatorUI.validatePage();
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
		return "Assign-Investigation-";
	}
	
	/**
	 * Part of CR R0767
	 */
	@Override
	public void showAssignCountAlert(String alertMsg){
		assignInvestigatorUI.showAssignCountMsg(alertMsg);
	}
}
