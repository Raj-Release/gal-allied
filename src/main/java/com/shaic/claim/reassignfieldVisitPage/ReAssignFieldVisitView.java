package com.shaic.claim.reassignfieldVisitPage;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fieldVisitPage.FieldVisitDTO;
import com.shaic.claim.fieldVisitPage.TmpFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.reassignfieldvisit.search.SearchReAssignFieldVisitTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
 
public interface ReAssignFieldVisitView  extends GMVPView{

	
	public void setReferenceData(FieldVisitDTO bean,
			SearchReAssignFieldVisitTableDTO searchTableDTO,
			NewIntimationDto newIntimationDto, ClaimDto claimDto,
			List<ViewFVRDTO> FvrDTOList, Intimation intimation);
	
	public void generateFieldBasedReAssignFVRClick(Boolean isChecked);

	public void initRepresentativeSearch(
			BeanItemContainer<SelectValue> stateContainer,
			BeanItemContainer<SelectValue> allocationToContainer,
			BeanItemContainer<SelectValue> assignToContainer,
			BeanItemContainer<SelectValue> fvrPriorityContainer);

	public void setRepresentativeDetails(TmpFVRDTO tmpFVRDTOList);

	public void result();
}
