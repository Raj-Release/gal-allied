package com.shaic.claim.pedrequest.process;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PEDRequestDetailsProcessView  extends GMVPView {
	
	public void generateFieldBasedOnQueryClick(Boolean isChecked);
	public void generateFieldBasedOnReferClick(Boolean isChecked);
	public void generateFieldBasedOnApproveClick(Boolean isChecked);
	public void generateFieldBasedOnWatchClick(Boolean isChecked);
	public void list(Page<PEDRequestDetailsProcessDTO> resultList);
	public void setReference(NewIntimationDto intimationDto, ClaimDto claimDto, List<InsuredPedDetails> pedByInsured, Boolean isAlreadyWatchList, Boolean watchListAvailable);
	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	public void showEditPanel(PEDRequestDetailsProcessDTO bean);
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);
	public void setPEDCode(String pedCode);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
