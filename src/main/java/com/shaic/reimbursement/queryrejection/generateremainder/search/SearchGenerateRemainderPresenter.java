/**
 * 
 */
package com.shaic.reimbursement.queryrejection.generateremainder.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;



/**
 * @author ntv.narenj
 *
 */

@ViewInterface(SearchGenerateRemainderView.class)
public class SearchGenerateRemainderPresenter extends AbstractMVPPresenter<SearchGenerateRemainderView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearchgpcrTable";
	public static final String CLEAR_SEARCH_FORM = "Clear Search Form";
	public static final String RESET_SEARCH_FORM = "Reset Search Form";
	public static final String GENERATE_LETTER = "Generate Letter";
	public static final String SUBMIT_LETTER = "Submit Letter task to BPM";
	
	
	@EJB
	private SearchGenerateRemainderService searchService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService dbCalService;

	protected void submitReminderLetter(
			@Observes @CDIEvent(SUBMIT_LETTER) final ParameterDTO parameters) {
		
		SearchGenerateReminderTableDTO reminderLetterDto = (SearchGenerateReminderTableDTO )parameters.getPrimaryParameter();		
		
		String batchId = SHAConstants.SINGLE_REMINDER + dbCalService.generateReminderBatchId(SHAConstants.REMINDER_BATCH_SEQUENCE_NAME);
		reminderLetterDto.setBatchId(batchId);
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		
		reminderLetterDto.setUsername(userId);
		searchService.submitReminderLetter(reminderLetterDto);
		
	}
	
	protected void generateReminderLetter(
			@Observes @CDIEvent(GENERATE_LETTER) final ParameterDTO parameters) {
		
		SearchGenerateReminderTableDTO reminderLetterDto = (SearchGenerateReminderTableDTO )parameters.getPrimaryParameter();
		
		if(reminderLetterDto.getClaimDto().getNewIntimationDto().getKey() != null){
			Intimation intimationObj = intimationService.getIntimationByKey(reminderLetterDto.getClaimDto().getNewIntimationDto().getKey());
			NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
			reminderLetterDto.getClaimDto().setNewIntimationDto(intimationDto);
			reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
			view.generateReminderLetter(reminderLetterDto);
		}
		
		else{
				//	   	No Intimation Available for the Claim
		}
		
		
	}
	protected void clearSearchReminderLetterForm(
			@Observes @CDIEvent(CLEAR_SEARCH_FORM) final ParameterDTO parameters) {
		
		view.clearReminderLetterSearch();
	}
	
	protected void resetSearchReminderLetterForm(
			@Observes @CDIEvent(RESET_SEARCH_FORM) final ParameterDTO parameters) {
		
		view.resetReminderLetterSearch();
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchGenerateRemainderFormDTO searchFormDTO = (SearchGenerateRemainderFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		searchFormDTO.setUsername(userName);
		searchFormDTO.setPassword(passWord);
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
