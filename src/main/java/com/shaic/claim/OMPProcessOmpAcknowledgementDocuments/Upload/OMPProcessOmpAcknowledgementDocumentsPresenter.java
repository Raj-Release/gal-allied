package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(OMPProcessOmpAcknowledgementDocumentsView.class)
public class OMPProcessOmpAcknowledgementDocumentsPresenter extends AbstractMVPPresenter<OMPProcessOmpAcknowledgementDocumentsView>{
	
	//public static final String RESET_SEARCH_VIEW = "OMP Processor Reset Search Fields"; 
	public static final String SUBMIT_SEARCH = "AcknowledgementDocuments Search Submit";
	//public static final String DISABLE_SEARCH_FIELDS = "OMP Processor Disable Search Filters";
	//public static final String EDIT_INTIMATION_SCREEN = "OMP Processor Edit Intimation Screen";
	
	@EJB
	private OMPProcessOmpAcknowledgementDocumentsService searchService;
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPProcessOmpAcknowledgementDocumentsFormDto searchFormDTO = (OMPProcessOmpAcknowledgementDocumentsFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

}
