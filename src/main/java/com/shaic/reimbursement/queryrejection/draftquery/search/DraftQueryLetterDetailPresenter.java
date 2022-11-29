package com.shaic.reimbursement.queryrejection.draftquery.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReimbursementQueryService;
/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftQueryLetterDetailView.class)
public class DraftQueryLetterDetailPresenter extends AbstractMVPPresenter<DraftQueryLetterDetailView>{
	
	public static final String CANCEL_DRAFT_QUERY_LETTER = "Cancel Draft Query Letter";
	public static final String SUBMIT_CLICKED = "Submit Draft Query";
	public static final String SAVE_DRAFT_QUERY = "Save Draft Query";
	public static final String DELETE_DRAFT_LETTER_REMARKS = "delete Draft Or Redraft Letter Remarks";
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private IntimationService intimationService;
	
	@Override
	public void viewEntered() {
		
	}
	
	protected void deleteQueryDraftLetterRemarks(
			@Observes @CDIEvent(DELETE_DRAFT_LETTER_REMARKS) final ParameterDTO parameters) {
		DraftQueryLetterDetailTableDto deletedRemarksDto = (DraftQueryLetterDetailTableDto)parameters.getPrimaryParameter();
		view.deleteQueryDraftOrReDraftLetterRemarks(deletedRemarksDto);
	}

	protected void showDraftQueryLetterSearch(
			@Observes @CDIEvent(CANCEL_DRAFT_QUERY_LETTER) final ParameterDTO parameters) {
		view.cancelDraftQueryLetter();
	}
	
	protected void saveDraftQueryLetter(
			@Observes @CDIEvent(SAVE_DRAFT_QUERY) final ParameterDTO parameters) {
		
		SearchDraftQueryLetterTableDTO updatedBean = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		if(updatedBean != null && !updatedBean.getHasError()){
			ReimbursementQueryDto queryDto = updatedBean.getReimbursementQueryDto();
			queryDto.setUsername(updatedBean.getUsername());
			queryDto = reimbursementQueryService.saveReimbursementDraftQuery(updatedBean.getReimbursementQueryDto());
			queryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(intimationService.getNomineeDetailsListByTransactionKey(queryDto.getReimbursementDto().getKey()));
			view.showSuccessSaveMessage(queryDto);
		}
	}	
	
	protected void submitDraftQueryLetter(
			@Observes @CDIEvent(SUBMIT_CLICKED) final ParameterDTO parameters) {
		SearchDraftQueryLetterTableDTO searchDraftQueryDto = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		ReimbursementQueryDto reimbursementQueryDto = searchDraftQueryDto.getReimbursementQueryDto();
	
		reimbursementQueryDto.setUsername(searchDraftQueryDto.getUsername());
		reimbursementQueryDto.setPassword(searchDraftQueryDto.getPassword());
		//reimbursementQueryDto.setHumanTask(searchDraftQueryDto.getHumanTaskDTO());
		
		reimbursementQueryService.updateReimbursementQueryRemarks(reimbursementQueryDto);
	}

}
