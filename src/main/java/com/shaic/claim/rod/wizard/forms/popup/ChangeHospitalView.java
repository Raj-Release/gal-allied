package com.shaic.claim.rod.wizard.forms.popup;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ChangeHospitalView extends GMVPView {
	
public void listOfCity(BeanItemContainer<SelectValue> selectedValue);
	
//	public void listOfArea(BeanItemContainer<SelectValue> areaList);
	
	public void result();

	public void setStatesList(
			BeanItemContainer<SelectValue> selectValueContainer);
	

}
