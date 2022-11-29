package com.shaic.newcode.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@UIScoped
public class IWizard extends GWizard {

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public void showError() {
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
	
	
//  public void releaseHumanTask(){
//		
//		Integer existingTaskNumber= (Integer)UI.getCurrent().getSession().getAttribute(SHAConstants.TOKEN_ID);
//     	String userName=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
// 		String passWord=(String)UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD);
//
// 		if(existingTaskNumber != null){
// 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
// 			UI.getCurrent().getSession().setAttribute(SHAConstants.TOKEN_ID, null);
// 		}
//  }		
 		
 		public void releaseHumanTask(){
			
 			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
 	

	
}