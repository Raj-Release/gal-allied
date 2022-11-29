package com.shaic.claim.adviseonped;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface AdviseOnPEDView  extends GMVPView {

	public void list(Page<OldPedEndorsementDTO> tableRows);

	public void setReferenceData(OldPedEndorsementDTO resultDto,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto, Map<String, Object> referenceData, OldPedEndorsementDTO editableList, ClaimDto claimDto);
	
	public void result();

	public void setSearchDtoToTableDto(OldPedEndorsementDTO editDTO);
	
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

	
}
