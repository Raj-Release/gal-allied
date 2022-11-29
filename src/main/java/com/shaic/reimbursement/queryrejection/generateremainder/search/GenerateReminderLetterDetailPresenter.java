package com.shaic.reimbursement.queryrejection.generateremainder.search;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
/**
 * 
 * @author Lakshminarayana
 *
 */
@ViewInterface(GenerateReminderLetterDetailView.class)
public class GenerateReminderLetterDetailPresenter extends AbstractMVPPresenter<GenerateReminderLetterDetailView>{
	
//	public static final String GENERATE_LETTER = "Generate Letter";
	public static final String CANCEL_GENERATE_REMINDER_LETTER = "Cancel Generate Reminder Letter";
	public static final String SUBMIT_REMINDER_LETTER = "Submit Reminder Letter";
	
	@Override
	public void viewEntered() {
		
	}

//	protected void generateReminderLetter(
//			@Observes @CDIEvent(GENERATE_LETTER) final ParameterDTO parameters) {
//		
//		SearchDraftQueryLetterTableDTO queryTableDto = (SearchDraftQueryLetterTableDTO )parameters.getPrimaryParameter();
//		
//		view.generateReminderLetter(queryTableDto.getReimbursementQueryDto());
//	}
	
	protected void cancellReminderLetterSearch(
			@Observes @CDIEvent(CANCEL_GENERATE_REMINDER_LETTER) final ParameterDTO parameters) {
		view.cancelReminderLetter();
	}
	
	protected void submitRemiderLetterSearch(
			@Observes @CDIEvent(SUBMIT_REMINDER_LETTER) final ParameterDTO parameters) {
//		view.cancelReminderLetter();
	}
	

}
