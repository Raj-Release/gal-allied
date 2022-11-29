package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billingprocess;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.ui.Component;

public class PAHealthBillingProcessPageViewImpl extends AbstractMVPView implements PAHealthBillingProcessPageWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private PAHealthBillingProcessPageUI billingProcessPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	
	@Override
	public String getCaption() {
		return "Billing Process";
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}


	@Override
	public Component getContent() {
		billingProcessPage.init(bean, wizard);
		Component comp =  billingProcessPage.getContent();
		fireViewEvent(PAHealthBillingProcessPagePresenter.BILLING_PROCESS_SET_UP_REFERENCE, bean);
		return comp;
	}


	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		billingProcessPage.setupReferences(referenceData);
		
	}


	@Override
	public boolean onAdvance() {
		if(billingProcessPage.validatePage()) {
			if(bean.getPostHospitalizaionFlag() || bean.getPreHospitalizaionFlag()) {
				bean.setPostHospAmt(billingProcessPage.getPayableAmt());
				fireViewEvent(PAHealthBillingProcessPagePresenter.POST_HOSPITALIZATION_AMOUNT, bean);
			}
			billingProcessPage.setCalculationValues();
			
			Integer balanceSumInsuredAmt = billingProcessPage.getBalanceSumInsuredAmt();
			String coPayValue = billingProcessPage.getCoPayValue();
			Integer consideredAmountValue = billingProcessPage.getConsideredAmountValue();
			this.bean.setBalanceSumInsuredAfterCoPay(Double.valueOf(balanceSumInsuredAmt));
//			this.bean.setCoPayValue(Integer.valueOf(coPayValue));
			this.bean.setAmountConsidedAfterCoPay(consideredAmountValue.doubleValue());
			
			return true;
		}
		return false;
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
	public void getValuesForMedicalDecisionTable(
			DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		billingProcessPage.setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);
	}


	@Override
	public void setPreAuthRequestedAmt(String calculatePreRequestedAmt) {
		billingProcessPage.setPreauthRequestedAmount(calculatePreRequestedAmt);
	}

	@Override
	public void setPosthospAmt(Integer amount, Map<String, Double> postHospValues, Integer balanceSI, Boolean isRestrictionSIAvail, Integer siRestrictedAmount, Integer previousPostHospAmt, Integer previousPreHospAmt) {
		if(bean.getPostHospitalizaionFlag()) {
			bean.setPostHospAmt(amount);
			bean.setPostHospPercentage(postHospValues.get(SHAConstants.POST_HOSP_LIMIT_PERCENTAGE));
			bean.setPostHospclaimRestrictionAmount(postHospValues.get(SHAConstants.POST_HOSP_LIMIT_AMOUNT));
		}
		bean.setPreviousPostHospAmount(previousPostHospAmt);
		bean.setIsSIRestrictionAvail(isRestrictionSIAvail);
		bean.setBalanceSIAftHosp(balanceSI);
		bean.setSiRestrictionAmount(siRestrictedAmount);
		bean.setPreviousPreHospAmount(previousPreHospAmt);
		
	}

	@Override
	public void setClaimRestrictionAmount(Long claimRestriction) {
		bean.setClaimRestrictionAmount(claimRestriction);
		
	}

	@Override
	public void setBalanceSIforRechargedProcess(Double balanceSI) {
		billingProcessPage.setBalanceSIforRechargedProcessing(balanceSI);
	}

}
