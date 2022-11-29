package com.shaic.claim.hospitalCommunication;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface HospitalView extends GMVPView {

	void setHospitalData(HospitalAcknowledgeDTO hospitalDto);

	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
