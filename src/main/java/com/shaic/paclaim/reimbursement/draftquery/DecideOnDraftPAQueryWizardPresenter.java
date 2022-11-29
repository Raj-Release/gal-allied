package com.shaic.paclaim.reimbursement.draftquery;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ReimbursementQueryService;

@ViewInterface(DecideOnDraftPAQueryWizard.class)
public class DecideOnDraftPAQueryWizardPresenter extends
AbstractMVPPresenter<DecideOnDraftPAQueryWizard> {

	public static final String SUBMIT_DRAFT_PA_QUERY_LETTER = "Submit Draft PA Query Letter ";
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void submitDraftQuery(
			@Observes @CDIEvent(SUBMIT_DRAFT_PA_QUERY_LETTER) final ParameterDTO parameters) {
		
		view.submitQuery(parameters);
		view.buildSuccessLayout();
	}
	
	
}
