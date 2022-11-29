package com.shaic.claim.viewEarlierRodDetails.Page;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;


@ViewInterface(PreauthView.class)
public class ViewPreauthPresenter extends AbstractMVPPresenter<PreauthView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SET_VALUES = "Sublimit and SI restrictionValues";
	
	
	public void setEditReferenceData(@Observes @CDIEvent(SET_VALUES) final ParameterDTO parameters){
		
		Integer approvedAmount = (Integer) parameters.getPrimaryParameter();
		
		view.setApprovedAmountField(approvedAmount);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
