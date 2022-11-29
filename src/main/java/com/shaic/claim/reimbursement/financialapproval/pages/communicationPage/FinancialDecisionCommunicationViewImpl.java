package com.shaic.claim.reimbursement.financialapproval.pages.communicationPage;

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
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPageViewImpl;
import com.vaadin.ui.Component;

public class FinancialDecisionCommunicationViewImpl extends AbstractMVPView implements FinancialDecisionCommunicationWizard {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FinancialDecisionCommunicationPageUI financialDecisionCommuncicationPage;
	
	@Inject
	private TextBundle tb;
	
    private PreauthDTO bean;
	
	private GWizard wizard;
	
	private String strCaptionString;
	
	private FinancialReviewPageViewImpl billingReviewPageViewImplObj;
	
	public FinancialReviewPageViewImpl getBillingReviewPageViewImplObj() {
		return billingReviewPageViewImplObj;
	}

	public void setBillingReviewPageViewImplObj(
			FinancialReviewPageViewImpl billingReviewPageViewImplObj) {
		this.billingReviewPageViewImplObj = billingReviewPageViewImplObj;
	}

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
		
		financialDecisionCommuncicationPage.init(bean, wizard, billingReviewPageViewImplObj);
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
	public void init(PreauthDTO bean, GWizard wizard, FinancialReviewPageViewImpl argBillingReviewPageViewImplObj) {
		localize(null);
		this.bean = bean;
		this.wizard = wizard;
		this.billingReviewPageViewImplObj = argBillingReviewPageViewImplObj;
	}

}
