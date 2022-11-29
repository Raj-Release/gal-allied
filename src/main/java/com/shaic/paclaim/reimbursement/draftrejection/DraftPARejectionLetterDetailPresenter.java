package com.shaic.paclaim.reimbursement.draftrejection;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftPARejectionLetterDetailView.class)
public class DraftPARejectionLetterDetailPresenter extends AbstractMVPPresenter<DraftPARejectionLetterDetailView>{
	
	public static final String SUBMIT_PA_REJECTION_LETTER = "Submit Draft PA Rejection Letter";
	
	public static final String Send_Draft_PA_Rejection_for_Process = "Send Draft PA Rejection for process";
	public static final String CANCEL_DRAFT_PA_REJECTION_LETTER = "Cancel Draft PA Rejection Letter";
	
	@EJB
	private ReimbursementRejectionService reimbursementRejectionService;
	
	@Override
	public void viewEntered() {
		
	}

	protected void cancelDraftRejectionLetter(
			@Observes @CDIEvent(CANCEL_DRAFT_PA_REJECTION_LETTER) final ParameterDTO parameters) {
		view.cancelDraftRejectionLetter();
	}
	
	protected void submitDraftRejectionLetter(
			@Observes @CDIEvent(SUBMIT_PA_REJECTION_LETTER) final ParameterDTO parameters) {
		SearchDraftRejectionLetterTableDTO draftRejectionLetterTableDto = (SearchDraftRejectionLetterTableDTO)parameters.getPrimaryParameter();
		
		ReimbursementRejectionDto bean = draftRejectionLetterTableDto.getReimbursementRejectionDto();
		bean.setUsername(draftRejectionLetterTableDto.getUsername());
		bean.setPassword(draftRejectionLetterTableDto.getPassword());
	//	bean.setHumanTask(draftRejectionLetterTableDto.getHumanTaskDTO());
		
		reimbursementRejectionService.updateReimbursementRejection(bean);
		
		view.openPdfFileInWindow();
	}
	

}
