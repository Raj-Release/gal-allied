package com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.vaadin.ui.Component;

public class ClaimAndDocumentDetailsPageViewImpl extends AbstractMVPView implements ClaimAndDocumentDetailsPageInterface {

	private static final long serialVersionUID = 7195213288077038794L;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(OutPatientDTO bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(OutPatientDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSumInsuredValidation(Integer healthCheckupSumInsured) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getInsuredKey() {
		// TODO Auto-generated method stub
		return null;
	}/*

	private static final long serialVersionUID = 5576743706797691367L;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private ClaimAndDocumentDetailsPageUI claimAndDocumentPage;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	
	@Override
	public String getCaption() {
		return "Claim and Document Details";
	}
	
	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		Component comp =  claimAndDocumentPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ClaimAndDocumentDetailsPagePresenter.SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
		claimAndDocumentPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		if(claimAndDocumentPage.validatePage()) {
			if(this.bean.getHealthCheckupFlag() && this.bean.getDocumentDetails().getClaimType().getValue().toString().toLowerCase().contains("health")) {
				fireViewEvent(ClaimAndDocumentDetailsPagePresenter.CHECK_HEALTH_CHECK_UP_SUM_INSURED_VALIDATION, bean.getDocumentDetails().getInsuredPatientName().getKey());
				if(claimAndDocumentPage.isSILesser) {
					claimAndDocumentPage.showSILesserPopup();
					return false;
				}
				return true;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		claimAndDocumentPage.init(bean, wizard);
	}


	@Override
	public void setSumInsuredValidation(Integer healthCheckupSumInsured) {
		claimAndDocumentPage.setSumInsuredValidation(healthCheckupSumInsured);
	}
	
	@Override
	public Long getInsuredKey() {
		Long insuredKey = claimAndDocumentPage.getInsuredKey();
		return insuredKey;
	}
	
	public void setInsuredKey(){
		claimAndDocumentPage.setInsuredKey();
		
	}

*/}