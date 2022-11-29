package com.shaic.claim.registration.updateHospitalDetails;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
//import com.shaic.domain.Locality;

public interface UpdateHospitalDetailsView extends GMVPView {
	
	public void listOfCity(BeanItemContainer<SelectValue> selectedValue);
	
//	public void listOfArea(BeanItemContainer<SelectValue> areaList);
	
	public void result();
	

}
