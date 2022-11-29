package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface AcknowledgeInvestigationCompletedView extends GMVPView  {
	
	public void setReference(Map<String, Object> referenceObj);
	
	public void setLayout();
	
	public void setReferenceForDto(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList);
	
	public void setCompleteLayout();
	
	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
