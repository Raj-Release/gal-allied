package com.shaic.claim.policy.updateHospital.ui;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.MasterService;

@ViewInterface(UpdateHospitalView.class)
public class UpdateHospitalPresenter extends AbstractMVPPresenter<UpdateHospitalView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4018539759600490478L;

	public static final String SET_REFERENCE_DATA = "set Area data";
	
	@EJB
	private MasterService masterService;
	
	public void setUpReference(@Observes @CDIEvent(SET_REFERENCE_DATA) final ParameterDTO parameters)
	{
		
		Long cityKey=(Long)parameters.getPrimaryParameter();
//		List<Locality> areaList=masterService.localitySearchbyCityKey(cityKey);
//		this.view.setUpAreaList(areaList);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
