package com.shaic.claim;

import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


public class ViewStopLossDetalsForGMC extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtPolicyNumber;
	
    private TextField txtPremium;
    
    private TextField txtPercentage;
    
    private TextField txtStopLossAmt;
    
    private TextField txtClaimsPaid;
    
    private TextField txtOutstandingClaims;
    
    private TextField txtIncurredClaims;
    
    private TextField txtBalanceAvailable;
    
    private TextField txtAmtClaimed;
    
    private TextField txtBalanceAvailableClaim;
    
    @EJB
    private ClaimService claimService;

    public void init(Intimation intimation,Long rodKey){
    	
    	Claim claimsByIntimationNumber = claimService.getClaimforIntimation(intimation.getKey());
    	
    	txtPolicyNumber = new TextField("Policy No");
    	txtPolicyNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtPolicyNumber.setValue(intimation.getPolicy().getPolicyNumber());
    	txtPolicyNumber.setWidth("230px");
    	txtPremium = new TextField("Premium");
    	txtPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtPercentage = new TextField("Stop Loss %");
    	txtPercentage.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtStopLossAmt = new TextField("Stop Loss Amount");
    	txtStopLossAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtClaimsPaid = new TextField("Total Claims Paid");
    	txtClaimsPaid.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtOutstandingClaims = new TextField("Total Outstanding Claims");
    	txtOutstandingClaims.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtIncurredClaims = new TextField("Total Incurred Claims");
    	txtIncurredClaims.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtBalanceAvailable = new TextField("Balance Available");
    	txtBalanceAvailable.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtAmtClaimed = new TextField("Amount Claimed for this claim");
    	txtAmtClaimed.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtBalanceAvailableClaim = new TextField("Balance Available (Including the claim)");
    	txtBalanceAvailableClaim.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	
    	FormLayout firstForm = new FormLayout(txtPolicyNumber,txtPremium,txtPercentage,txtStopLossAmt);
    	firstForm.addStyleName("layoutDesign");
    	firstForm.setSpacing(true);
    	
    	FormLayout secondForm = new FormLayout(txtClaimsPaid,txtOutstandingClaims,txtIncurredClaims,txtBalanceAvailable,txtAmtClaimed,txtBalanceAvailableClaim);
    	secondForm.addStyleName("layoutDesign");
    	secondForm.setSpacing(true);
    	
    	HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
    	mainHor.setSpacing(true);
    	mainHor.setWidth("100%");
    	
    	DBCalculationService dbCalculationService = new DBCalculationService();
    	Map<String, Integer> stopLossProcedureForView = null;
    
    	if(claimsByIntimationNumber != null){
        	if(!claimsByIntimationNumber.getIntimation().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)) {
        	
        		stopLossProcedureForView = dbCalculationService.getStopLossProcedureForView(claimsByIntimationNumber.getIntimation().getPolicy().getKey(), claimsByIntimationNumber.getKey(), rodKey);
        	}else{
        		stopLossProcedureForView = dbCalculationService.getStopLossProcedureForView(intimation.getPolicy().getKey(), 0l, rodKey);
        	}
        	}
    	
    	if(stopLossProcedureForView != null && !stopLossProcedureForView.isEmpty()){
			if(stopLossProcedureForView.get(SHAConstants.PREMIUM_FOR_STOP_LOSS) != null){
				txtPremium.setValue(stopLossProcedureForView.get(SHAConstants.PREMIUM_FOR_STOP_LOSS).toString());
				txtPremium.setReadOnly(true);
			}
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_PER) != null){
				txtPercentage.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_PER).toString());
				txtPercentage.setReadOnly(true);
			}
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_AMT) != null){
				txtStopLossAmt.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_AMT).toString());
				txtStopLossAmt.setReadOnly(true);
			}
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_PAID) != null){
				txtClaimsPaid.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_PAID).toString());
				txtClaimsPaid.setReadOnly(true);
			}
			
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_OUTSTANDING) != null){
				txtOutstandingClaims.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_OUTSTANDING).toString());
				txtOutstandingClaims.setReadOnly(true);
			}
			
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_INCURRED_CLAIMS) != null){
				txtIncurredClaims.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_INCURRED_CLAIMS).toString());
				txtIncurredClaims.setReadOnly(true);
			}
			
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_BALANCE_AVAILABLE) != null){
				txtBalanceAvailable.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_BALANCE_AVAILABLE).toString());
				txtBalanceAvailable.setReadOnly(true);
			}
			
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_CURR_CLAIM_AMT) != null){
				txtAmtClaimed.setValue(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_CURR_CLAIM_AMT).toString());
				txtAmtClaimed.setReadOnly(true);
			}
			
			if(stopLossProcedureForView.get(SHAConstants.STOP_LOSS_BALANCE_AMT_CLAIM) != null){
				Integer balanceAmt = stopLossProcedureForView.get(SHAConstants.STOP_LOSS_BALANCE_AMT_CLAIM);
				if(balanceAmt < 0){
					balanceAmt = 0;
				}
				txtBalanceAvailableClaim.setValue(balanceAmt.toString());
				txtBalanceAvailableClaim.setReadOnly(true);
			}
			
			
			
		}

    	setCompositionRoot(mainHor);

    }

}
