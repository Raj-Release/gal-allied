package com.shaic.reimbursement.uploadrodreports;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UploadInvestigationReportView extends GMVPView {
	
	public void setReference(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList, Long countOfAckByClaimKey);
	
	public void setUploadInvestigationReportUI(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList);
	
	public void setReferenceForCorosal(ClaimDto claimDto);
	
	public List<UploadedDocumentsDTO> getUploadedDocDtoList();		
	
//	public void updateTableValues(Long investigationKey);
	public void updateTableValues(List<UploadedDocumentsDTO> uploadedDocList);

	public void result();
	public void showSaveResult();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
