package com.shaic.newcode.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.cdi.UIScoped;

@SuppressWarnings("serial")
@UIScoped
public class IWizardPartialComplete extends GWizard {

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public void showError() {
	}
	
	
	public IWizardPartialComplete() {
		if(this.finishButton != null) {
			this.finishButton.setDisableOnClick(true);
		}
	}
 	@PostConstruct
	public void initView()
	{
		
		
	}
	
	public void restView(String wizardStep)
	{
		activateStep(wizardStep);
	}
	
	public void clearWizardMap(String wizardStep)
	//public void clearWizardMap()
	{
	/*	idMap.clear();
		steps.clear();*/
		WizardStep stepToRemove = idMap.get(wizardStep);
		idMap.remove(wizardStep);
        steps.remove(stepToRemove); 
        // notify listeners
        fireEvent(new WizardStepSetChangedEvent(this));
	}
	
	public void clearCurrentStep()
	{
		currentStep = null;
	}
	
	public void finish() {
        if (currentStep.onAdvance()) {
            // next (finish) allowed -> fire complete event
            fireEvent(new WizardCompletedEvent(this));
        } else {
        	if(this.finishButton != null) {
        		this.finishButton.setEnabled(true);
        	}
        	
        }
        
//        this.cancelButton.addClickListener(new Button);
    }
	
	@Override
	public void cancel(){
		 fireEvent(new WizardCancelledEvent(this));
		 if(this.cancelButton != null){
//			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
//         	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
//     		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
//
//     		if(existingTaskNumber != null){
//     			BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
//     			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
//     		}
		 }
	}
	
	public void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		
 		Long workFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
 		}
 		
 		if(workFlowKey != null){
 			DBCalculationService db = new DBCalculationService();
 			db.callUnlockProcedure(workFlowKey);
 		}
	}

	
}