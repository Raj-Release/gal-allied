package com.shaic.claim.rod.wizard.forms.popup;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.State;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ChangeHospitalView.class)
public class ChangeHospitalPresenter extends AbstractMVPPresenter<ChangeHospitalView> {
	
public static final String SET_COMBOBOX_VALUE = "Setv value for change hospital";
	
	public static final String UPDATE_BUTTON_CLICK="Change Hospital Details";
	
	public static final String SET_AREA="Set area list for Change hospital Details";
	
	public static final String SET_STATE_LIST = "set states list for change Hospital details";
	
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
	
   public void setStates(@Observes @CDIEvent(SET_STATE_LIST) final ParameterDTO parameters){
		
	List<State> stateList = masterService.getStateList();
	List<SelectValue> states = new ArrayList<SelectValue>();

	for (State state : stateList) {
		SelectValue selected = new SelectValue();
		selected.setId(state.getKey());
		selected.setValue(state.getValue());
		states.add(selected);
	}
	BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(states);
	
	view.setStatesList(selectValueContainer);
		
	}
	
	
	
	public void UpdateButtonClick(@Observes @CDIEvent(UPDATE_BUTTON_CLICK) final ParameterDTO parameters){
		
		UpdateHospitalDetailsDTO bean=(UpdateHospitalDetailsDTO)parameters.getPrimaryParameter();
		
		Boolean result=hospitalService.changeHospitalDetails(bean);
		
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
