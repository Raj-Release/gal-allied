package com.shaic.paclaim.reimbursement.processdraftquery;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.vaadin.ui.Component;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class DecideOnPAQueryViewImpl extends AbstractMVPView implements DecideOnPAQueryView{
	
	@Inject 
	private DecideOnPAQueryPage decideOnQueryPage;
	
	private String strCaptionString;
	
	@Inject
	private TextBundle tb;	
	
	private ClaimQueryDto bean;

	@Override
	public void resetView() {
		
	}

	@Override
	public void init(ClaimQueryDto bean) {

		
	}
	
	@Override
	public void init(ClaimQueryDto bean, GWizard wizard) {
		 
		localize(null);
		this.bean = bean;
		decideOnQueryPage.init(bean,wizard);		
		
	}

	@Override
	public String getCaption() {
		return strCaptionString;
	}

	@Override
	public Component getContent() {
		Component comp =  decideOnQueryPage.getContent();
		
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		
		decideOnQueryPage.updateBean();
		
		boolean navigationAllowed = decideOnQueryPage.validatePage();
		
		return navigationAllowed;
		
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
        strCaptionString = tb.getText(textBundlePrefixString() + "decideonquery");
    }
    
	private String textBundlePrefixString()
	{
		return "queryrejection-";
	}
	
	@Override
	public void returnPreviousPage(ClaimQueryDto updatedBean) {
		this.bean = updatedBean;
		decideOnQueryPage.setpreviousView(this.bean);
		onBack();		
	}

	@Override
	public void buildRedraftQueryLayout() {
		decideOnQueryPage.buildRedraftLayout();
	}

	@Override
	public void buildRejectQueryLayout() {
		decideOnQueryPage.buildRejectQueryLayout();
	}

	@Override
	public void buildApproveQueryLayout() {
		decideOnQueryPage.buildApproveLayout();
	}

	@Override
	public void getUpdatedBean() {
		decideOnQueryPage.validatePage();
	}

	@Override
	public void setUpdatedBean(ClaimQueryDto updatedBean) {
		this.bean = updatedBean;
		decideOnQueryPage.updateBean(updatedBean);
		
	}

//	@Override
//	public void deleteDraftQueryLetterRemarks(
//			DraftQueryLetterDetailTableDto deltedObj) {
//		decideOnQueryPage.setDeltedDraftRemarksToBean(deltedObj);
//		
//	}	

	
	
}
