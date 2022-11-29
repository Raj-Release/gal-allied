package com.shaic.claim.pedquery;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PEDQueryView  extends GMVPView {

	public void list(Page<OldPedEndorsementDTO> tableRows,OldPedEndorsementDTO initiateDetailsRow);
	
	public void setPEDEndorsementTable(OldPedEndorsementDTO tableRows, Map<String, Object> referenceData, BeanItemContainer<SelectValue> pedCodeContainer, BeanItemContainer<SelectValue> icdChapterContainer);
	
	public void setReferenceData(OldPedEndorsementDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto, ClaimDto claimDto, BeanItemContainer<SelectValue> pedValueContainer);
	
	public void setEditReferenceData(Map<String, Object> referenceData);
	
	void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer);
	
	void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer);

	public void result(Boolean result);

	public void setPEDCode(String pedCode);

	public void setReferenceEditData(OldPedEndorsementDTO resultDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto, ClaimDto claimDto,
			BeanItemContainer<SelectValue> pedValueContainer);
	
	public void setSearchDtoToTableDto(OldPedEndorsementDTO editDto);
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

	
}
