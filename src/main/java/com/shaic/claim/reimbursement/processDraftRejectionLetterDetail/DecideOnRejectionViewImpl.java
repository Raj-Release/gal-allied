package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class DecideOnRejectionViewImpl extends AbstractMVPView implements DecideOnRejectionView{
	
	@Inject 
	private DecideOnRejectionPage decideOnRejectionPage;
	
	private String strCaptionString;
	
	@Inject
	private TextBundle tb;	
	
	private ClaimRejectionDto bean;

	@Override
	public void resetView() {
		
	}

	@Override
	public void init(ClaimRejectionDto bean) {

	}
	
	@Override
	public void init(ClaimRejectionDto bean, GWizard wizard) {
		// TODO Auto-generated method stub
		localize(null);
		this.bean = bean; 
		decideOnRejectionPage.init(bean,wizard);
		
	}

	@Override
	public String getCaption() {
		return strCaptionString;
	}

	@Override
	public Component getContent() {
		Component comp =  decideOnRejectionPage.getContent();
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		return decideOnRejectionPage.validatePage();
	}

	@Override
	public boolean onBack() {
		decideOnRejectionPage.setpreviousView(this.bean);
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}

	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        strCaptionString = tb.getText(textBundlePrefixString() + "decideonrejection");
    }
    
	private String textBundlePrefixString()
	{
		return "deciderejection-";
	}
	
	@Override
	public void returnPreviousPage() {
		decideOnRejectionPage.setpreviousView(this.bean);
		onBack();		
	}

	@Override
	public void buildRedraftRejectionLayout() {
		decideOnRejectionPage.buildRedraftRejectionLayout();
	}

	@Override
	public void buildDisapproveRejectionLayout() {
		decideOnRejectionPage.buildDisapproveRejectionLayout();
	}

	@Override
	public void buildApproveRejectionLayout() {
		decideOnRejectionPage.buildApproveRejectionLayout();
	}
	
	@Override
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		decideOnRejectionPage.setSubCategContainer(rejSubcategContainer);
	}

	@Override
	public void clearObject() {
		decideOnRejectionPage.clearObject();
		bean = null;
		
	}

}
