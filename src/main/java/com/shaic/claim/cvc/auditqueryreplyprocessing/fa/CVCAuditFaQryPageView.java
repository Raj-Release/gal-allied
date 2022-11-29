package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.claim.ClaimDto;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface CVCAuditFaQryPageView extends GMVPView{

	public void setReference(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList, Long countOfAckByClaimKey);	
	public void setReferenceForCorosal(ClaimDto claimDto);	
	public void result();	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadCVCStatusDropDownValues(AuditDetails auditDetails, BeanItemContainer<SelectValue> remediationStatusValueContainer, 
			BeanItemContainer<SelectValue> statusValueContainer, List<AuditTeam> auditTeam, List<AuditCategory> auditCategory, 
			List<AuditProcessor> auditProcessor, BeanItemContainer<SelectValue> cmbCategoryContainer, 
			BeanItemContainer<SelectValue> TeamValueContainer, BeanItemContainer<SelectValue> ErrorValueContainer, 
			BeanItemContainer<SelectValue> processorValueContainer, BeanItemContainer<SelectValue> monetaryValueContainer);
}
