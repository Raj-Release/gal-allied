package com.shaic.paclaim.reimbursement.processdraftquery;

import javax.ejb.EJB;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizard;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;

@ViewInterface(DecideOnPAQueryWizard.class)
public class DecideOnPAQueryWizardPresenter extends
AbstractMVPPresenter<DecideOnPAQueryWizard> {

	public static final String SUBMIT_DRAFT_PA_QUERY = "Submit Draft PA Query Letter";
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void submitDraftQuery(
			@Observes @CDIEvent(SUBMIT_DRAFT_PA_QUERY) final ParameterDTO parameters) {
		
		ClaimQueryDto clmQueryDto = (ClaimQueryDto)parameters.getPrimaryParameter();
		ReimbursementQueryDto reimbursementQueryDto = clmQueryDto.getReimbursementQueryDto(); 
		ReimbursementQuery submitProcessedReimbursementQuery = reimbursementQueryService.submitProcessedReimbursementQuery(reimbursementQueryDto);
		reimbursementQueryService.submitPAProcessDraftQueryLetterDBTask(submitProcessedReimbursementQuery, reimbursementQueryDto);		
		view.buildSuccessLayout();
	}
	
	
}
