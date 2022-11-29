package com.shaic.claim.fieldVisitPage;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface FieldVisitPageView extends GMVPView {

	public void generateFieldBasedAssignFVRClick(Boolean isChecked);

	public void generateFieldBasedSkipFVRClick(Boolean isChecked);

	public void setReferenceData(FieldVisitDTO bean,
			SearchFieldVisitTableDTO searchTableDTO,
			NewIntimationDto newIntimationDto, ClaimDto claimDto,
			List<ViewFVRDTO> FvrDTOList, Intimation intimation);
	
	public void initRepresentativeSearch(BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer, BeanItemContainer<SelectValue> assignToContainer, BeanItemContainer<SelectValue> fvrPriorityContainer);
	
	public void setRepresentativeDetails(TmpFVRDTO tmpFVRDTOList);
	
	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
