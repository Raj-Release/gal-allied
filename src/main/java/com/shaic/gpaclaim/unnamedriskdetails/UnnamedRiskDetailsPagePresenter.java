package com.shaic.gpaclaim.unnamedriskdetails;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
@ViewInterface(UnnamedRiskDetailsPageView.class)
public class UnnamedRiskDetailsPagePresenter extends AbstractMVPPresenter<UnnamedRiskDetailsPageView>{

	
	
	public static final String SUBMIT_BUTTON_CLICK = "Unnamed Risk Submit";
	
	
	@EJB
	private ClaimService claimService;    
	
	
	@SuppressWarnings({ "deprecation" })
	public void submitPayment(@Observes @CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		UnnamedRiskDetailsPageDTO unnamedRiskPageDTO = (UnnamedRiskDetailsPageDTO) parameters.getPrimaryParameter();
		
		claimService.updateUnnamedRiskCategory(unnamedRiskPageDTO); 
		view.buildSuccessLayout();
     	
	}
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	


}
