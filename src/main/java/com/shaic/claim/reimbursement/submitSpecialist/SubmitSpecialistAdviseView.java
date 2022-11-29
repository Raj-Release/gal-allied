package com.shaic.claim.reimbursement.submitSpecialist;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.submitSpecialist.SubmitSpecialistDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SubmitSpecialistAdviseView extends GMVPView {

	void ListOfValues(SubmitSpecialistDTO resultList,
			NewIntimationDto intimationDto, ClaimDto claimDto);
	
	void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	void buildInitiateLumenRequest(String intimationId);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
