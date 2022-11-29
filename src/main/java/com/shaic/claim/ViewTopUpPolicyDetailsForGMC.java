package com.shaic.claim;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeListener;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.vaadin.v7.ui.AbstractField;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreauthMedicalProcessingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;


public class ViewTopUpPolicyDetailsForGMC extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PremiaPullService premiaPullService;

	private TextField txtPolicyNumber;
	
    private TextField txtProposerName;
    
    public BeanFieldGroup<PreauthDTO> binder;
    
    private ComboBox txtRiskName;
    
    private Window popUp;
    
   private String isHcActionClicked;
    
    public void init(List<PreauthDTO> topupData, PreauthDTO bean, String screenName, Window popup,String isHCclicked){
    	
    	this.popUp = popup;
    	this.isHcActionClicked = isHCclicked;
    	//Policy topUpPolicy = (Policy)topupData.get("topupPolicy");
    	
    	//Insured topUpInsured = (Insured)topupData.get("topupInsured");
    	
    	txtPolicyNumber = new TextField("Policy number");
    	txtPolicyNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtPolicyNumber.setValue(topupData.get(0).getTopUPPolicyNumber());
    	
    	txtProposerName = new TextField("Proposer Name");
    	txtProposerName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtProposerName.setValue(topupData.get(0).getTopUpProposerName());
    	
    	txtRiskName = new ComboBox("Risk Name");
    	//txtRiskName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	SelectValue selectValue = null;
    	SelectValue selectValueforHC = null;
    	BeanItemContainer<SelectValue> riskNameValue = new BeanItemContainer<SelectValue>(SelectValue.class);
    	for(int i=0;i<topupData.size();i++) {
    		selectValue = new SelectValue();
    		selectValue.setValue(topupData.get(i).getTopUpInsuredName());
    		selectValue.setId(topupData.get(i).getTopUpInsuredNo());
    		if(isHCclicked != null && isHCclicked.equalsIgnoreCase("HC_TOPUP") && bean.getNewIntimationDTO().getInsuredPatient() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredName() != null && topupData.get(i).getTopUpInsuredName() != null
    				&& topupData.get(i).getTopUpInsuredDOB() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth() != null){
    		if((topupData.get(i).getTopUpInsuredName() != null && topupData.get(i).getTopUpInsuredName().equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()))
    				&& (topupData.get(i).getTopUpInsuredDOB().toString().equals(bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth().toString()))){
    			selectValueforHC = new SelectValue();
    			selectValueforHC = selectValue;
    		}
    		}
    		riskNameValue.addBean(selectValue);
    	}
    	
    	
    	txtRiskName.setContainerDataSource(riskNameValue);
    
    	if(isHCclicked != null && isHCclicked.equalsIgnoreCase("HC_TOPUP") && selectValueforHC != null && selectValueforHC.getValue() != null){
        	txtRiskName.setValue(selectValueforHC);
        	}
    	
    	Button copyButton = new Button();
    	copyButton.setCaption("Copy intimation to this policy");
    	copyButton
		.addClickListener(new Button.ClickListener() {
			public void buttonClick(
					ClickEvent event) {
				if(txtRiskName.getValue() != null) {
				confirmMessageForTopUp(screenName, topupData, bean, copyButton);
				}else{
					showErrorPopUp("Risk Name is mandatory.");
				}
				
			}
		});
    	txtRiskName.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(txtRiskName.getValue() != null) {
					alertMessageForChangeValue();
				}
				
			}
		});
    	
    	Button closeButton = new Button();
    	closeButton.setCaption("Close");
    	closeButton
		.addClickListener(new Button.ClickListener() {
			public void buttonClick(
					ClickEvent event) {
				if(popUp != null)
				UI.getCurrent().removeWindow(popUp);
			}
		});
    	
    	HorizontalLayout buttonsLayout = new HorizontalLayout();

    	buttonsLayout.addComponents(copyButton,closeButton);
    			buttonsLayout.setSpacing(true);
    	
    	FormLayout firstForm = new FormLayout(txtPolicyNumber,txtProposerName,txtRiskName);
    	firstForm.addStyleName("layoutDesign");
    	firstForm.setSpacing(true);
    	firstForm.setMargin(true);
    	
    	VerticalLayout mainHor = new VerticalLayout(firstForm);
    	mainHor.addComponent(buttonsLayout);
    	mainHor.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    	mainHor.setWidth("100%");
    	mainHor.setHeight("100%");
    	
    	setCompositionRoot(mainHor);

    }
    
    public void confirmMessageForTopUp(String screenName,List<PreauthDTO> topupData, PreauthDTO bean, Button copyButton){
		final MessageBox getConf = MessageBox
			    .createQuestion()
			    .withCaptionCust("Confirmation")
			    .withMessage("Do you want to create Intimation under Top-up policy?")
			    .withYesButton(ButtonOption.caption("Yes"))
			    .withNoButton(ButtonOption.caption("No"))
			    .open();
			Button homeButton=getConf.getButton(ButtonType.YES);
			homeButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					PremPolicyDetails policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(topupData.get(0).getTopUPPolicyNumber(), String.valueOf(((SelectValue) txtRiskName.getValue()).getId()));
//					commented the below code since it is not used in any area
					if(isHcActionClicked != null && isHcActionClicked.equalsIgnoreCase("HC_TOPUP")){
					}
					else {	
					Boolean isIntegratedPolicy =premiaPullService.populateGMCandGPAPolicy(policyDetails, String.valueOf(((SelectValue) txtRiskName.getValue()).getId()),false);
					}
					String riskId = String.valueOf(((SelectValue) txtRiskName.getValue()).getId());
					if(screenName.equalsIgnoreCase(SHAConstants.PRE_AUTH)){
						bean.getPreauthMedicalDecisionDetails().setApprovalRemarks("\nClaim is already approved to the maximum extent - Sum insured Exhausted.\n\n Note: Further claim will be processed in Top-up policy.");
						fireViewEvent(PreauthWizardPresenter.PREAUTH_TOPUP_POLICY_INTIMATION_EVENT, bean, topupData, riskId);
					}else if(screenName.equalsIgnoreCase(SHAConstants.ENHANCEMENT)){
						bean.getPreauthMedicalDecisionDetails().setApprovalRemarks("\nClaim is already approved to the maximum extent - Sum insured Exhausted.\n\n Note: Further claim will be processed in Top-up policy.");
						fireViewEvent(PreauthEnhancemetWizardPresenter.ENHN_TOPUP_POLICY_INTIMATION_EVENT, bean, topupData,riskId);
					}else if(screenName.equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL)){
						fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.MA_TOPUP_POLICY_INTIMATION_EVENT, bean, topupData,riskId,isHcActionClicked);
					}
					copyButton.setEnabled(false);
					getConf.close();
					if(popUp != null)
						UI.getCurrent().removeWindow(popUp);
               	 
				}
			});
			Button cancelButton=getConf.getButton(ButtonType.NO);
			cancelButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {

                    getConf.close();
                
				}
			});
		
	}


 public void alertMessageForChangeValue() {
   		
	    final MessageBox showInfoMessageBox = showInfoMessageBox("Intimation will be created with the selected insured");
    	Button homeButton = showInfoMessageBox.getButton(ButtonType.OK);
    	
    	homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				showInfoMessageBox.close();
			}
		});
 } 	
    public MessageBox showInfoMessageBox(String message){
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
		return msgBox;
      }
    
    public void showErrorPopUp(String emsg) {
		MessageBox.createError()
    	.withCaptionCust("Errors").withHtmlMessage(emsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();	
	}
}
