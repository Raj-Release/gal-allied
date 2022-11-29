package com.shaic.claim.common;

import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForDataExtractionPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForMedicalProcessingPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForPreviousClaimPage;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestMedicalDecisionButtons;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionButtons;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.VerticalLayout;

public class ApprovedTable extends ViewComponent implements WizardStep<PreauthDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5431109076734698028L;

	
	//private PreauthDTO bean;
	
	private PreauthButtonsUI uiPage;
	
	private ClaimRequestMedicalDecisionButtons claimRequestPage;
	private PAClaimRequestMedicalDecisionButtons paClaimRequestPage;
	private PAHealthClaimRequestMedicalDecisionButtons paHealthclaimRequestPage;
	private ClaimRequestButtonsForDataExtractionPage claimRequestDataExtractionPage;
	private ClaimRequestButtonsForPreviousClaimPage claimRequestButtonsForPreviousClaimPage;
	private ClaimRequestButtonsForMedicalProcessingPage claimRequestButtonsForMedicalProcessingPage;
	
	private VerticalLayout layout;
	
	private ConfirmDialog dialog;
	
	private Boolean isZonalReview = true;
	
	@Override
	public void init(PreauthDTO bean) {
		//this.bean = bean;
		
	}
	
	@Override
	public String getCaption() {
		return "Approval";
	}

	public void setUIPage(PreauthButtonsUI uiPage, ConfirmDialog dialog) {
		this.uiPage = uiPage;
		this.dialog = dialog;
		
	}
	
	public void setUIPageForClaimRequest(ClaimRequestMedicalDecisionButtons uiPage, ConfirmDialog dialog) {
		this.claimRequestPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	public void setUIPageForClaimRequest(ClaimRequestButtonsForMedicalProcessingPage uiPage, ConfirmDialog dialog) {
		this.claimRequestButtonsForMedicalProcessingPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	public void setUIPageForClaimRequest(ClaimRequestButtonsForPreviousClaimPage uiPage, ConfirmDialog dialog) {
		this.claimRequestButtonsForPreviousClaimPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	public void setUIPageForClaimRequest(ClaimRequestButtonsForDataExtractionPage uiPage, ConfirmDialog dialog) {
		this.claimRequestDataExtractionPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	public void setUIPageForClaimRequest(PAHealthClaimRequestMedicalDecisionButtons uiPage, ConfirmDialog dialog) {
		this.paHealthclaimRequestPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	public void setUIPageForClaimRequest(PAClaimRequestMedicalDecisionButtons uiPage, ConfirmDialog dialog) {
		this.paClaimRequestPage = uiPage;
		this.dialog = dialog;
		this.isZonalReview = false;
	}
	
	@Override
	public Component getContent() {
//		layout = new VerticalLayout();
		return layout;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		if(isZonalReview) {
			//uiPage.validatePage(this.dialog);
			uiPage.showAmtConsiderAlert(this.dialog);
		} else {
			if(claimRequestPage != null) {
				//claimRequestPage.validatePage(this.dialog, null);
				claimRequestPage.showAmtConsiderAlert(this.dialog);
			} else if(paHealthclaimRequestPage != null) {
				//paHealthclaimRequestPage.validatePage(this.dialog);
				paHealthclaimRequestPage.showAmtConsiderAlert(this.dialog);
			}
			
		}
		
		return true;
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
	
	public void setComponent(VerticalLayout object) {
		layout = new VerticalLayout();
		layout.addComponent(object);
	}

	

}
