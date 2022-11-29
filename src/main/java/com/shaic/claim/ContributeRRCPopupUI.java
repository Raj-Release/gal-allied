package com.shaic.claim;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("deprecation")
public class ContributeRRCPopupUI extends ViewComponent{
	
	private PreauthDTO bean;
	
	private String presenterString;
	
	private Button yesBtn;
	
	private Button noBtn;
	
	private MessageBox getConf;
	
	
	public void init(PreauthDTO bean,String presenterString){
		this.bean = bean;
		this.presenterString = presenterString;

		getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Contribute RRC ?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
		yesBtn =getConf.getButton(ButtonType.YES);
		noBtn = getConf.getButton(ButtonType.NO);
		
		addListener();
		
	}
	
	public void addListener() {
		
		noBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				getConf.close();
			}
		});
		
		yesBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
					fireViewEvent(PreauthWizardPresenter.VALIDATE_PREAUTH_USER_RRC_REQUEST, bean);
				}else if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
					fireViewEvent(PreauthEnhancemetWizardPresenter.VALIDATE_PREAUTH_ENHANCEMENT_USER_RRC_REQUEST, bean);
				}else if(presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
					fireViewEvent(ClaimRequestWizardPresenter.VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST, bean);
				}else {
					
				}
				/*else if(presenterString.equalsIgnoreCase(SHAConstants.BILLING)){
					fireViewEvent(BillingWizardPresenter.VALIDATE_BILLING_USER_RRC_REQUEST, bean);
				}else if(presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
					fireViewEvent(FinancialWizardPresenter.VALIDATE_FINANCIAL_USER_RRC_REQUEST, bean);
				}*/
				getConf.close();
			}
		});	
	}

}
