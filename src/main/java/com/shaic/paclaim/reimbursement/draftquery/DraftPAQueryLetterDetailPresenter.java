package com.shaic.paclaim.reimbursement.draftquery;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.IntimationService;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(DraftPAQueryLetterDetailView.class)
public class DraftPAQueryLetterDetailPresenter extends AbstractMVPPresenter<DraftPAQueryLetterDetailView>{
	
	public static final String CANCEL_DRAFT_PA_QUERY_LETTER = "Cancel Draft PA Query Letter";
	public static final String SUBMIT_PA_BUTTON_CLICKED = "Submit Draft PA Query";
	public static final String SAVE_DRAFT_PA_QUERY = "Save Draft Pa Query";
	public static final String DELETE_DRAFT_PA_LETTER_REMARKS = "delete Draft Or Redraft PA Letter Remarks";
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private MasterService masterService;
	
	@Override
	public void viewEntered() {
		
	}
	
	protected void deleteQueryDraftLetterRemarks(
			@Observes @CDIEvent(DELETE_DRAFT_PA_LETTER_REMARKS) final ParameterDTO parameters) {
		DraftQueryLetterDetailTableDto deletedRemarksDto = (DraftQueryLetterDetailTableDto)parameters.getPrimaryParameter();
		view.deleteQueryDraftOrReDraftLetterRemarks(deletedRemarksDto);
	}

	protected void showDraftQueryLetterSearch(
			@Observes @CDIEvent(CANCEL_DRAFT_PA_QUERY_LETTER) final ParameterDTO parameters) {
		view.cancelDraftQueryLetter();
	}
	
	protected void saveDraftQueryLetter(
			@Observes @CDIEvent(SAVE_DRAFT_PA_QUERY) final ParameterDTO parameters) {
		
		SearchDraftQueryLetterTableDTO updatedBean = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		if(updatedBean != null && !updatedBean.getHasError()){
			ReimbursementQueryDto queryDto = updatedBean.getReimbursementQueryDto();
			queryDto.setUsername(updatedBean.getUsername());
			queryDto = reimbursementQueryService.saveReimbursementDraftQuery(updatedBean.getReimbursementQueryDto());
			queryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(intimationService.getNomineeDetailsListByTransactionKey(queryDto.getReimbursementDto().getKey()));
			
			List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(queryDto.getReimbursementDto().getKey());

			if(legalHeirList != null && !legalHeirList.isEmpty()) {
				List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
				LegalHeirDTO legalHeirDto;
				for (LegalHeir legalHeir : legalHeirList) {
					 legalHeirDto = new LegalHeirDTO(legalHeir);
					 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
					 legalHeirDTOList.add(legalHeirDto);
				}
				queryDto.getReimbursementDto().setLegalHeirDTOList(legalHeirDTOList);
			}
			
			view.showSuccessSaveMessage(queryDto);
		}
	}	
	
	protected void submitDraftQueryLetter(
			@Observes @CDIEvent(SUBMIT_PA_BUTTON_CLICKED) final ParameterDTO parameters) {
		SearchDraftQueryLetterTableDTO searchDraftQueryDto = (SearchDraftQueryLetterTableDTO)parameters.getPrimaryParameter();
		ReimbursementQueryDto reimbursementQueryDto = searchDraftQueryDto.getReimbursementQueryDto();
	
		reimbursementQueryDto.setUsername(searchDraftQueryDto.getUsername());
		reimbursementQueryDto.setPassword(searchDraftQueryDto.getPassword());
	//	reimbursementQueryDto.setHumanTask(searchDraftQueryDto.getHumanTaskDTO());
		
		reimbursementQueryService.updateReimbursementQueryRemarks(reimbursementQueryDto);
	}

}
