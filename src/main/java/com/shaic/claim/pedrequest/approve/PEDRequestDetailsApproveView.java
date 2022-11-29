package com.shaic.claim.pedrequest.approve;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PEDRequestDetailsApproveView  extends GMVPView {
	
	public void generateFieldBasedOnQueryClick(Boolean isChecked);
	public void generateFieldBasedOnWatchClick(Boolean isChecked);
	public void generateFieldBasedOnReferClick(Boolean isChecked);
	public void generateFieldBasedOnApproveClick(Boolean isChecked);
	public void generateFieldBasedOnEscalateClick(Boolean isChecked);
	public void generateFieldBasedOnRejectClick(Boolean isChecked);

	public void list(Page<OldPedEndorsementDTO> tableRows);
	public void setReferenceData(PEDRequestDetailsApproveDTO resultDto,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto, ClaimDto claimDto);
	
	public void setPEDEndorsementTable(OldPedEndorsementDTO tableRows, Map<String, Object> referenceData);
	
	public void result(Boolean result);
	
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	public void showEditPanel(PEDRequestDetailsApproveDTO bean);
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);
	public void setPEDCode(String pedCode);
	public void buildInitiateLumenRequest(String intimationId);
	
	public void showPEDAlreadyAvailable(String pedAvailableMsg);
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO);
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);
}
