//package com.shaic.paclaim.reminder;
//
//import javax.enterprise.event.Observes;
//
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
//import org.vaadin.addon.cdimvp.CDIEvent;
//import org.vaadin.addon.cdimvp.ParameterDTO;
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
//import org.vaadin.addon.cdimvp.MVPView;
//
//import com.shaic.main.navigator.domain.MenuItemBean;
//import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
///**
// * 
// * @author Lakshminarayana
// *
// */
//@ViewInterface(GeneratePAReminderLetterDetailView.class)
//public class GeneratePAReminderLetterDetailPresenter extends AbstractMVPPresenter<GeneratePAReminderLetterDetailView>{
//	
////	public static final String GENERATE_LETTER = "Generate Letter";
//	public static final String CANCEL_GENERATE_PA_REMINDER_LETTER = "Cancel Generate PA Reminder Letter";
//	public static final String SUBMIT_PA_REMINDER_LETTER = "Submit PA Reminder Letter";
//	
//	@Override
//	public void viewEntered() {
//		
//	}
//
////	protected void generateReminderLetter(
////			@Observes @CDIEvent(GENERATE_LETTER) final ParameterDTO parameters) {
////		
////		SearchDraftQueryLetterTableDTO queryTableDto = (SearchDraftQueryLetterTableDTO )parameters.getPrimaryParameter();
////		
////		view.generateReminderLetter(queryTableDto.getReimbursementQueryDto());
////	}
//	
//	protected void cancellReminderLetterSearch(
//			@Observes @CDIEvent(CANCEL_GENERATE_PA_REMINDER_LETTER) final ParameterDTO parameters) {
//		view.cancelReminderLetter();
//	}
//	
//	protected void submitRemiderLetterSearch(
//			@Observes @CDIEvent(SUBMIT_PA_REMINDER_LETTER) final ParameterDTO parameters) {
////		view.cancelReminderLetter();
//	}
//	
//
//}
