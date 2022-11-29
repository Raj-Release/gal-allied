package com.shaic.claim.pancard.page;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UpdatePanCardReportView extends GMVPView {
	
	public void setReference(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList, Long countOfAckByClaimKey);
	
	public void setUploadInvestigationReportUI(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList);
	
	public void setReferenceForCorosal(ClaimDto claimDto);
	
	public void updateTableValues(Long investigationKey);

	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	public void updateTableValues(MultipleUploadDocumentDTO dto);
}
