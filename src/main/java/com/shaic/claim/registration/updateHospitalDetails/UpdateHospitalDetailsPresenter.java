package com.shaic.claim.registration.updateHospitalDetails;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.domain.HospitalService;
//import com.shaic.domain.Locality;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(UpdateHospitalDetailsView.class)
public class UpdateHospitalDetailsPresenter extends AbstractMVPPresenter<UpdateHospitalDetailsView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_COMBOBOX_VALUE = "Set value to beanitem container";
	
	public static final String UPDATE_BUTTON_CLICK="Update Hospital Details";
	
	public static final String SET_AREA="Set area list for Update hospital Details";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private UpdateHospitalService updateHospitalService;
	
	@EJB
	private HospitalService hospitalService;
	
	
	public void setUpReferenceObjectForEdit(@Observes @CDIEvent(SET_COMBOBOX_VALUE) final ParameterDTO parameters){
		
		Long key=(Long)parameters.getPrimaryParameter();
		
	    BeanItemContainer<SelectValue> selected=masterService.getCityList(key);
	    
	    view.listOfCity(selected);
		
	}
	
	public void UpdateButtonClick(@Observes @CDIEvent(UPDATE_BUTTON_CLICK) final ParameterDTO parameters){
		
		UpdateHospitalDetailsDTO bean=(UpdateHospitalDetailsDTO)parameters.getPrimaryParameter();
		
		Boolean result=hospitalService.UpdateHospitalDetails(bean);
		
		if(result){
			view.result();
		}
	}
	
//public void searchAreaByCities(@Observes @CDIEvent(SET_AREA) final ParameterDTO parameters){
//		
//		Long key=(Long)parameters.getPrimaryParameter();
//		
//		BeanItemContainer<SelectValue> selected=masterService.getAreaList(key);
//		
//		view.listOfArea(selected);
//	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
