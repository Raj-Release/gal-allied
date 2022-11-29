package com.shaic.reimbursement.queryrejection.draftquery.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ReimbursementQueryService;

@ViewInterface(DecideOnDraftQueryWizard.class)
public class DecideOnDraftQueryWizardPresenter extends
AbstractMVPPresenter<DecideOnDraftQueryWizard> {

	public static final String SUBMIT_DRAFT_QUERY_LETTER = "Submit Draft Query Letter ";
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void submitDraftQuery(
			@Observes @CDIEvent(SUBMIT_DRAFT_QUERY_LETTER) final ParameterDTO parameters) {
		
		view.submitQuery(parameters);
		view.buildSuccessLayout();
	}
	
	
}
