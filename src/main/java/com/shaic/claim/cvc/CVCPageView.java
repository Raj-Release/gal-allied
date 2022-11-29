package com.shaic.claim.cvc;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface CVCPageView extends GMVPView{

	public void setReference(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList, Long countOfAckByClaimKey);	
	public void setReferenceForCorosal(ClaimDto claimDto);	
	public void result();	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadCVCStatusDropDownValues(BeanItemContainer<SelectValue> statusValueContainer,
			BeanItemContainer<SelectValue> ErrorValueContainer,BeanItemContainer<SelectValue> TeamValueContainer,
			BeanItemContainer<SelectValue> monetaryValueContainer,BeanItemContainer<SelectValue> remediationStatusValueContainer,
			BeanItemContainer<SelectValue> processorValueContainer);
}
