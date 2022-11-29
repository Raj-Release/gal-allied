//package com.shaic.claim.OMPprocessrejection.wizard.pages;
//
//import javax.enterprise.event.Observes;
//
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
//import org.vaadin.addon.cdimvp.CDIEvent;
//import org.vaadin.addon.cdimvp.ParameterDTO;
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
//
//import com.shaic.claim.OMPprocessrejection.search.SearchOMPProcessRejectionFormDto;
//
//
//
//@ViewInterface(OMPProcessRejectionWizardView.class)
//public class OMPProcessRejectionWizardViewPresenter  extends AbstractMVPPresenter<OMPProcessRejectionWizardView>{
//	
//	public static final String SUBMIT_PROCESSREJECTION = "submit_process_rejection";
//	public static final String CLEAR_SEARCH_FORM = "clear_search_form";
//	public static final String RESET_SEARCH_FROM = "reset_search_form";
//	@Override
//	public void viewEntered() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	
//	@SuppressWarnings("static-access")
//	public void submitWizard(
//			@Observes @CDIEvent(SUBMIT_PROCESSREJECTION) final ParameterDTO parameters) {
//	SearchOMPProcessRejectionFormDto searchFormDTO = (SearchOMPProcessRejectionFormDto) parameters.getPrimaryParameter();
//	}
//
//	protected void clearSearchReminderLetterForm(
//			@Observes @CDIEvent(CLEAR_SEARCH_FORM) final ParameterDTO parameters) {
//		
//	//	view.clearReminderLetterSearch();
//	}
//	
//	protected void resetSearchReminderLetterForm(
//			@Observes @CDIEvent(RESET_SEARCH_FROM) final ParameterDTO parameters) {
//		
//		//view.resetReminderLetterSearch();
//	}
//	
//}
